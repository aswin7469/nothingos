package com.android.settings.security;

import android.content.Context;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import java.util.List;
/* loaded from: classes.dex */
public class SimLockPreferenceController extends BasePreferenceController {
    private static final String KEY_SIM_LOCK = "sim_lock_settings";
    private final CarrierConfigManager mCarrierConfigManager;
    private final SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private final UserManager mUserManager;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

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
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SimLockPreferenceController(Context context) {
        this(context, KEY_SIM_LOCK);
    }

    public SimLockPreferenceController(Context context, String str) {
        super(context, str);
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mCarrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        return (activeSubscriptionInfoList != null && this.mUserManager.isAdminUser() && !isHideSimLockSetting(activeSubscriptionInfoList)) ? 0 : 4;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference == null) {
            return;
        }
        findPreference.setEnabled(isSimReady());
    }

    private boolean isSimReady() {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            return false;
        }
        for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
            int simState = this.mTelephonyManager.getSimState(subscriptionInfo.getSimSlotIndex());
            if (simState != 1 && simState != 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isHideSimLockSetting(List<SubscriptionInfo> list) {
        if (list == null) {
            return true;
        }
        for (SubscriptionInfo subscriptionInfo : list) {
            TelephonyManager createForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(subscriptionInfo.getSubscriptionId());
            PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(subscriptionInfo.getSubscriptionId());
            int simState = this.mTelephonyManager.getSimState(subscriptionInfo.getSimSlotIndex());
            if (createForSubscriptionId.hasIccCard() && configForSubId != null && !configForSubId.getBoolean("hide_sim_lock_settings_bool") && simState != 6) {
                return false;
            }
        }
        return true;
    }
}
