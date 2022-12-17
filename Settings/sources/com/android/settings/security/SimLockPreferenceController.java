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
import java.util.List;

public class SimLockPreferenceController extends BasePreferenceController {
    private final CarrierConfigManager mCarrierConfigManager = ((CarrierConfigManager) this.mContext.getSystemService("carrier_config"));
    private final SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private final UserManager mUserManager;

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

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public SimLockPreferenceController(Context context, String str) {
        super(context, str);
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
    }

    public int getAvailabilityStatus() {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList != null && this.mUserManager.isAdminUser() && !isHideSimLockSetting(activeSubscriptionInfoList)) {
            return 0;
        }
        return 4;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Preference findPreference = preferenceScreen.findPreference(getPreferenceKey());
        if (findPreference != null) {
            findPreference.setEnabled(isSimReady());
        }
    }

    private boolean isSimReady() {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (activeSubscriptionInfoList == null) {
            return false;
        }
        for (SubscriptionInfo simSlotIndex : activeSubscriptionInfoList) {
            int simState = this.mTelephonyManager.getSimState(simSlotIndex.getSimSlotIndex());
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
        for (SubscriptionInfo next : list) {
            TelephonyManager createForSubscriptionId = this.mTelephonyManager.createForSubscriptionId(next.getSubscriptionId());
            PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(next.getSubscriptionId());
            int simState = this.mTelephonyManager.getSimState(next.getSimSlotIndex());
            if (createForSubscriptionId.hasIccCard() && configForSubId != null && !configForSubId.getBoolean("hide_sim_lock_settings_bool") && simState != 6) {
                return false;
            }
        }
        return true;
    }
}
