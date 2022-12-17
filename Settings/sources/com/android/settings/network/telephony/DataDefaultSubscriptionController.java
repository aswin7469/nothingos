package com.android.settings.network.telephony;

import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;

public class DataDefaultSubscriptionController extends DefaultSubscriptionController {
    private static final String SETTING_USER_PREF_DATA_SUB = "user_preferred_data_sub";

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

    public DataDefaultSubscriptionController(Context context, String str) {
        super(context, str);
    }

    /* access modifiers changed from: protected */
    public SubscriptionInfo getDefaultSubscriptionInfo() {
        return this.mManager.getActiveSubscriptionInfo(getDefaultSubscriptionId());
    }

    /* access modifiers changed from: protected */
    public int getDefaultSubscriptionId() {
        return SubscriptionManager.getDefaultDataSubscriptionId();
    }

    /* access modifiers changed from: protected */
    public void setDefaultSubscription(int i) {
        this.mManager.setDefaultDataSubId(i);
        setUserPrefDataSubIdInDb(i);
    }

    private void setUserPrefDataSubIdInDb(int i) {
        Settings.Global.putInt(this.mContext.getContentResolver(), SETTING_USER_PREF_DATA_SUB, i);
    }
}
