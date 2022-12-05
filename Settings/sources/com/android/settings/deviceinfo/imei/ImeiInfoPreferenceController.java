package com.android.settings.deviceinfo.imei;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.network.telephony.TelephonyUtils;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.slices.Sliceable;
import com.qti.extphone.QtiImeiInfo;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ImeiInfoPreferenceController extends BasePreferenceController {
    private static final String KEY_PREFERENCE_CATEGORY = "device_detail_category";
    private static final String TAG = "ImeiInfoPreferenceController";
    private Fragment mFragment;
    private final boolean mIsMultiSim;
    private final List<Preference> mPreferenceList = new ArrayList();
    private QtiImeiInfo[] mQtiImeiInfo;
    private final TelephonyManager mTelephonyManager;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public boolean useDynamicSliceSummary() {
        return true;
    }

    public ImeiInfoPreferenceController(Context context, String str) {
        super(context, str);
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mTelephonyManager = telephonyManager;
        boolean z = telephonyManager.getPhoneCount() <= 1 ? false : true;
        this.mIsMultiSim = z;
        if (z) {
            TelephonyUtils.connectExtTelephonyService(context);
        }
    }

    public void setHost(Fragment fragment) {
        this.mFragment = fragment;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(KEY_PREFERENCE_CATEGORY);
        this.mPreferenceList.add(findPreference);
        updatePreference(findPreference, 0);
        int order = findPreference.getOrder();
        for (int i = 1; i < this.mTelephonyManager.getPhoneCount(); i++) {
            Preference createNewPreference = createNewPreference(preferenceScreen.getContext());
            createNewPreference.setOrder(order + i);
            createNewPreference.setKey(getPreferenceKey() + i);
            preferenceCategory.addPreference(createNewPreference);
            this.mPreferenceList.add(createNewPreference);
            updatePreference(createNewPreference, i);
        }
        int phoneCount = this.mTelephonyManager.getPhoneCount();
        if (!Utils.isSupportCTPA(this.mContext) || phoneCount < 2) {
            return;
        }
        int currentPhoneTypeForSlot = this.mTelephonyManager.getCurrentPhoneTypeForSlot(0);
        int currentPhoneTypeForSlot2 = this.mTelephonyManager.getCurrentPhoneTypeForSlot(1);
        if (2 != currentPhoneTypeForSlot && 2 != currentPhoneTypeForSlot2) {
            addPreferenceNotInList(preferenceScreen, 0, order + phoneCount, getPreferenceKey() + phoneCount, true);
        } else if (2 == currentPhoneTypeForSlot) {
            addPreferenceNotInList(preferenceScreen, 0, order + phoneCount, getPreferenceKey() + phoneCount, false);
        } else if (2 == currentPhoneTypeForSlot2) {
            addPreferenceNotInList(preferenceScreen, 1, order + phoneCount, getPreferenceKey() + phoneCount, false);
        }
    }

    private void addPreferenceNotInList(PreferenceScreen preferenceScreen, int i, int i2, String str, boolean z) {
        Preference createNewPreference = createNewPreference(preferenceScreen.getContext());
        createNewPreference.setOrder(i2);
        createNewPreference.setKey(str);
        ((PreferenceCategory) preferenceScreen.findPreference(KEY_PREFERENCE_CATEGORY)).addPreference(createNewPreference);
        if (z) {
            createNewPreference.setTitle(getTitleForCdmaPhone(i));
            createNewPreference.setSummary(this.mTelephonyManager.getMeid(i));
            return;
        }
        createNewPreference.setTitle(getTitleForGsmPhone(i));
        createNewPreference.setSummary(getImei(i));
    }

    private void addPreference(PreferenceScreen preferenceScreen, int i, int i2, String str, boolean z) {
        Preference createNewPreference = createNewPreference(preferenceScreen.getContext());
        createNewPreference.setOrder(i2);
        createNewPreference.setKey(str);
        preferenceScreen.addPreference(createNewPreference);
        this.mPreferenceList.add(createNewPreference);
        if (z) {
            createNewPreference.setTitle(getTitleForCdmaPhone(i));
            createNewPreference.setSummary(this.mTelephonyManager.getMeid(i));
            return;
        }
        createNewPreference.setTitle(getTitleForGsmPhone(i));
        createNewPreference.setSummary(getImei(i));
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        if (preference == null) {
            return;
        }
        Log.d(TAG, "updateState");
        this.mQtiImeiInfo = TelephonyUtils.getImeiInfo();
        int size = this.mPreferenceList.size();
        for (int i = 0; i < size; i++) {
            updatePreference(this.mPreferenceList.get(i), i);
        }
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        return getSummary(0);
    }

    private CharSequence getSummary(int i) {
        int phoneType = getPhoneType(i);
        if (Utils.isSupportCTPA(this.mContext) && 2 == phoneType) {
            i = 0;
        }
        if (phoneType == 2) {
            return this.mTelephonyManager.getMeid(i);
        }
        return getImei(i);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        int indexOf = this.mPreferenceList.indexOf(preference);
        if (indexOf == -1) {
            return false;
        }
        if (Utils.isSupportCTPA(this.mContext)) {
            return true;
        }
        ImeiInfoDialogFragment.show(this.mFragment, indexOf, preference.getTitle().toString());
        return true;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return (!((UserManager) this.mContext.getSystemService(UserManager.class)).isAdminUser() || com.android.settingslib.Utils.isWifiOnly(this.mContext)) ? 3 : 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public void copy() {
        Sliceable.setCopyContent(this.mContext, getSummary(0), getTitle(0));
    }

    private void updatePreference(Preference preference, int i) {
        preference.setTitle(getTitle(i));
        preference.setSummary(getSummary(i));
    }

    private boolean isPrimaryImeiSlot(int i) {
        if (this.mQtiImeiInfo == null) {
            this.mQtiImeiInfo = TelephonyUtils.getImeiInfo();
        }
        if (this.mQtiImeiInfo != null) {
            int i2 = 0;
            while (true) {
                QtiImeiInfo[] qtiImeiInfoArr = this.mQtiImeiInfo;
                if (i2 >= qtiImeiInfoArr.length) {
                    return false;
                }
                if (qtiImeiInfoArr[i2] != null && qtiImeiInfoArr[i2].getSlotId() == i && this.mQtiImeiInfo[i2].getImeiType() == 1) {
                    return true;
                }
                i2++;
            }
        } else {
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002f  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String getImei(int i) {
        String str;
        if (this.mQtiImeiInfo == null) {
            this.mQtiImeiInfo = TelephonyUtils.getImeiInfo();
        }
        if (this.mQtiImeiInfo != null) {
            int i2 = 0;
            while (true) {
                QtiImeiInfo[] qtiImeiInfoArr = this.mQtiImeiInfo;
                if (i2 >= qtiImeiInfoArr.length) {
                    break;
                } else if (qtiImeiInfoArr[i2].getSlotId() == i) {
                    str = this.mQtiImeiInfo[i2].getImei();
                    break;
                } else {
                    i2++;
                }
            }
            return !TextUtils.isEmpty(str) ? this.mTelephonyManager.getImei(i) : str;
        }
        str = null;
        if (!TextUtils.isEmpty(str)) {
        }
    }

    private CharSequence getTitleForGsmPhone(int i) {
        String string = this.mIsMultiSim ? this.mContext.getString(R.string.imei_multi_sim, Integer.valueOf(i + 1)) : this.mContext.getString(R.string.status_imei);
        if (!this.mIsMultiSim || !isPrimaryImeiSlot(i)) {
            return string;
        }
        return ((Object) string) + " (Primary)";
    }

    private CharSequence getTitleForCdmaPhone(int i) {
        return this.mIsMultiSim ? this.mContext.getString(R.string.meid_multi_sim, Integer.valueOf(i + 1)) : this.mContext.getString(R.string.status_meid_number);
    }

    private CharSequence getTitle(int i) {
        if (getPhoneType(i) == 2) {
            return getTitleForCdmaPhone(i);
        }
        return getTitleForGsmPhone(i);
    }

    private int getPhoneType(int i) {
        if (Utils.isSupportCTPA(this.mContext)) {
            return this.mTelephonyManager.getCurrentPhoneTypeForSlot(i);
        }
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(i);
        return this.mTelephonyManager.getCurrentPhoneType(activeSubscriptionInfoForSimSlotIndex != null ? activeSubscriptionInfoForSimSlotIndex.getSubscriptionId() : Integer.MAX_VALUE);
    }

    Preference createNewPreference(Context context) {
        return new Preference(context);
    }
}
