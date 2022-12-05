package com.android.settings.deviceinfo.hardwareinfo;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.slices.Sliceable;
/* loaded from: classes.dex */
public class HardwareRevisionPreferenceController extends BasePreferenceController {
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

    public HardwareRevisionPreferenceController(Context context, String str) {
        super(context, str);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return (!this.mContext.getResources().getBoolean(R.bool.config_show_device_model) || TextUtils.isEmpty(mo485getSummary())) ? 3 : 0;
    }

    @Override // com.android.settings.slices.Sliceable
    public void copy() {
        Sliceable.setCopyContent(this.mContext, mo485getSummary(), this.mContext.getText(R.string.hardware_revision));
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        String string;
        return (!Utils.isSupportCTPA(this.mContext) || (string = Utils.getString(this.mContext, "ext_hardware_version")) == null || string.isEmpty()) ? SystemProperties.get("ro.boot.hardware.revision") : string;
    }
}
