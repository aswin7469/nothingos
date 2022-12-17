package com.android.settings.deviceinfo.imei;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.deviceinfo.PhoneNumberSummaryPreference;
import com.android.settings.network.telephony.TelephonyUtils;
import com.qti.extphone.QtiImeiInfo;
import java.util.ArrayList;
import java.util.List;

public class ImeiInfoPreferenceController extends BasePreferenceController {
    private static final String KEY_PREFERENCE_CATEGORY = "device_detail_category";
    private static final String TAG = "ImeiInfoPreferenceController";
    private Fragment mFragment;
    private final boolean mIsMultiSim;
    private final List<Preference> mPreferenceList = new ArrayList();
    private QtiImeiInfo[] mQtiImeiInfo;
    private final TelephonyManager mTelephonyManager;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

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
        if (Utils.isSupportCTPA(this.mContext) && phoneCount >= 2) {
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
    }

    private void addPreferenceNotInList(PreferenceScreen preferenceScreen, int i, int i2, String str, boolean z) {
        Preference createNewPreference = createNewPreference(preferenceScreen.getContext());
        createNewPreference.setOrder(i2);
        createNewPreference.setKey(str);
        ((PreferenceCategory) preferenceScreen.findPreference(KEY_PREFERENCE_CATEGORY)).addPreference(createNewPreference);
        if (z) {
            createNewPreference.setTitle(getTitleForCdmaPhone(i));
            createNewPreference.setSummary((CharSequence) this.mTelephonyManager.getMeid(i));
            return;
        }
        createNewPreference.setTitle(getTitleForGsmPhone(i));
        createNewPreference.setSummary((CharSequence) getImei(i));
    }

    private void addPreference(PreferenceScreen preferenceScreen, int i, int i2, String str, boolean z) {
        Preference createNewPreference = createNewPreference(preferenceScreen.getContext());
        createNewPreference.setOrder(i2);
        createNewPreference.setKey(str);
        preferenceScreen.addPreference(createNewPreference);
        this.mPreferenceList.add(createNewPreference);
        if (z) {
            createNewPreference.setTitle(getTitleForCdmaPhone(i));
            createNewPreference.setSummary((CharSequence) this.mTelephonyManager.getMeid(i));
            return;
        }
        createNewPreference.setTitle(getTitleForGsmPhone(i));
        createNewPreference.setSummary((CharSequence) getImei(i));
    }

    public void updateState(Preference preference) {
        if (preference != null) {
            Log.d("ImeiInfoPreferenceController", "updateState");
            this.mQtiImeiInfo = TelephonyUtils.getImeiInfo();
            int size = this.mPreferenceList.size();
            for (int i = 0; i < size; i++) {
                updatePreference(this.mPreferenceList.get(i), i);
            }
        }
    }

    public CharSequence getSummary() {
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

    public int getAvailabilityStatus() {
        return (!((UserManager) this.mContext.getSystemService(UserManager.class)).isAdminUser() || com.android.settingslib.Utils.isWifiOnly(this.mContext)) ? 3 : 0;
    }

    private void updatePreference(Preference preference, int i) {
        preference.setTitle(getTitle(i));
        preference.setSummary(getSummary(i));
    }

    private boolean isPrimaryImeiSlot(int i) {
        if (this.mQtiImeiInfo == null) {
            this.mQtiImeiInfo = TelephonyUtils.getImeiInfo();
        }
        if (this.mQtiImeiInfo == null) {
            return false;
        }
        int i2 = 0;
        while (true) {
            QtiImeiInfo[] qtiImeiInfoArr = this.mQtiImeiInfo;
            if (i2 >= qtiImeiInfoArr.length) {
                return false;
            }
            QtiImeiInfo qtiImeiInfo = qtiImeiInfoArr[i2];
            if (qtiImeiInfo != null && qtiImeiInfo.getSlotId() == i && this.mQtiImeiInfo[i2].getImeiType() == 1) {
                return true;
            }
            i2++;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0031  */
    /* JADX WARNING: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getImei(int r4) {
        /*
            r3 = this;
            com.qti.extphone.QtiImeiInfo[] r0 = r3.mQtiImeiInfo
            if (r0 != 0) goto L_0x000a
            com.qti.extphone.QtiImeiInfo[] r0 = com.android.settings.network.telephony.TelephonyUtils.getImeiInfo()
            r3.mQtiImeiInfo = r0
        L_0x000a:
            com.qti.extphone.QtiImeiInfo[] r0 = r3.mQtiImeiInfo
            if (r0 == 0) goto L_0x002a
            r0 = 0
        L_0x000f:
            com.qti.extphone.QtiImeiInfo[] r1 = r3.mQtiImeiInfo
            int r2 = r1.length
            if (r0 >= r2) goto L_0x002a
            r1 = r1[r0]
            if (r1 == 0) goto L_0x0027
            int r1 = r1.getSlotId()
            if (r1 != r4) goto L_0x0027
            com.qti.extphone.QtiImeiInfo[] r1 = r3.mQtiImeiInfo
            r0 = r1[r0]
            java.lang.String r0 = r0.getImei()
            goto L_0x002b
        L_0x0027:
            int r0 = r0 + 1
            goto L_0x000f
        L_0x002a:
            r0 = 0
        L_0x002b:
            boolean r1 = android.text.TextUtils.isEmpty(r0)
            if (r1 == 0) goto L_0x0037
            android.telephony.TelephonyManager r3 = r3.mTelephonyManager
            java.lang.String r0 = r3.getImei(r4)
        L_0x0037:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.deviceinfo.imei.ImeiInfoPreferenceController.getImei(int):java.lang.String");
    }

    private CharSequence getTitleForGsmPhone(int i) {
        String str;
        if (this.mIsMultiSim) {
            str = this.mContext.getString(R$string.imei_multi_sim, new Object[]{Integer.valueOf(i + 1)});
        } else {
            str = this.mContext.getString(R$string.status_imei);
        }
        if (!this.mIsMultiSim || !isPrimaryImeiSlot(i)) {
            return str;
        }
        return str + " (Primary)";
    }

    private CharSequence getTitleForCdmaPhone(int i) {
        if (!this.mIsMultiSim) {
            return this.mContext.getString(R$string.status_meid_number);
        }
        return this.mContext.getString(R$string.meid_multi_sim, new Object[]{Integer.valueOf(i + 1)});
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

    /* access modifiers changed from: package-private */
    public Preference createNewPreference(Context context) {
        return new PhoneNumberSummaryPreference(context);
    }
}
