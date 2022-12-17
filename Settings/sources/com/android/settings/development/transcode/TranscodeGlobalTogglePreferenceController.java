package com.android.settings.development.transcode;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class TranscodeGlobalTogglePreferenceController extends TogglePreferenceController {
    private static final String TRANSCODE_ENABLED_PROP_KEY = "persist.sys.fuse.transcode_enabled";

    public int getAvailabilityStatus() {
        return 0;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public TranscodeGlobalTogglePreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return SystemProperties.getBoolean(TRANSCODE_ENABLED_PROP_KEY, true);
    }

    public boolean setChecked(boolean z) {
        SystemProperties.set(TRANSCODE_ENABLED_PROP_KEY, String.valueOf(z));
        return true;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_system;
    }
}
