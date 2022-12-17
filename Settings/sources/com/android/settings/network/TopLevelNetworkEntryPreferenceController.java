package com.android.settings.network;

import android.content.Context;
import android.content.IntentFilter;
import android.text.BidiFormatter;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;

public class TopLevelNetworkEntryPreferenceController extends BasePreferenceController {
    private final MobileNetworkPreferenceController mMobileNetworkPreferenceController = new MobileNetworkPreferenceController(this.mContext);

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

    public TopLevelNetworkEntryPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return Utils.isDemoUser(this.mContext) ? 3 : 0;
    }

    public CharSequence getSummary() {
        if (this.mMobileNetworkPreferenceController.isAvailable()) {
            return BidiFormatter.getInstance().unicodeWrap(this.mContext.getString(R$string.network_dashboard_summary_mobile));
        }
        return BidiFormatter.getInstance().unicodeWrap(this.mContext.getString(R$string.network_dashboard_summary_no_mobile));
    }
}
