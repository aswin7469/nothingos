package com.android.settings.deviceinfo.hardwareinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.android.settings.R$bool;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;

public class HardwareRevisionPreferenceController extends BasePreferenceController {
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

    public HardwareRevisionPreferenceController(Context context, String str) {
        super(context, str);
    }

    public int getAvailabilityStatus() {
        return (!this.mContext.getResources().getBoolean(R$bool.config_show_device_model) || TextUtils.isEmpty(getSummary())) ? 3 : 0;
    }

    public CharSequence getSummary() {
        String string;
        if (!Utils.isSupportCTPA(this.mContext) || (string = Utils.getString(this.mContext, "ext_hardware_version")) == null || string.isEmpty()) {
            return SystemProperties.get("ro.boot.hardware.revision");
        }
        return string;
    }
}
