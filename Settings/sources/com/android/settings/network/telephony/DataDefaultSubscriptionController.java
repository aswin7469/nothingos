package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import com.android.settings.slices.SliceBackgroundWorker;
/* loaded from: classes.dex */
public class DataDefaultSubscriptionController extends DefaultSubscriptionController {
    private static final String SETTING_USER_PREF_DATA_SUB = "user_preferred_data_sub";

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController, com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController, com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController, com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController, com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController, com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController, com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController, com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController, com.android.settings.network.telephony.TelephonyBasePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DataDefaultSubscriptionController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController
    protected SubscriptionInfo getDefaultSubscriptionInfo() {
        return this.mManager.getActiveSubscriptionInfo(getDefaultSubscriptionId());
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController
    protected int getDefaultSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    @Override // com.android.settings.network.telephony.DefaultSubscriptionController
    protected void setDefaultSubscription(int i) {
        this.mManager.setDefaultDataSubId(i);
        setUserPrefDataSubIdInDb(i);
    }

    private void setUserPrefDataSubIdInDb(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), SETTING_USER_PREF_DATA_SUB, i);
    }
}
