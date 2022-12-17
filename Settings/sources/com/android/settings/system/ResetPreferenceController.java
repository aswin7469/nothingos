package com.android.settings.system;

import android.content.Context;
import android.content.IntentFilter;
import android.os.UserManager;
import com.android.settings.R$bool;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.network.NetworkResetPreferenceController;

public class ResetPreferenceController extends BasePreferenceController {
    private final FactoryResetPreferenceController mFactpruReset;
    private final NetworkResetPreferenceController mNetworkReset;
    private final UserManager mUm;

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

    public ResetPreferenceController(Context context, String str) {
        super(context, str);
        this.mUm = (UserManager) context.getSystemService("user");
        this.mNetworkReset = new NetworkResetPreferenceController(context);
        this.mFactpruReset = new FactoryResetPreferenceController(context);
    }

    public int getAvailabilityStatus() {
        return this.mContext.getResources().getBoolean(R$bool.config_show_reset_dashboard) ? 0 : 3;
    }
}
