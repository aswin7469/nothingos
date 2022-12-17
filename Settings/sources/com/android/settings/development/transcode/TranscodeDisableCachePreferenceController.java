package com.android.settings.development.transcode;

import android.content.Context;
import android.content.IntentFilter;
import android.os.SystemProperties;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;

public class TranscodeDisableCachePreferenceController extends TogglePreferenceController {
    @VisibleForTesting
    static final String TRANSCODE_DISABLE_CACHE_SYS_PROP_KEY = "persist.sys.fuse.disable_transcode_cache";

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

    public TranscodeDisableCachePreferenceController(Context context, String str) {
        super(context, str);
    }

    public boolean isChecked() {
        return SystemProperties.getBoolean(TRANSCODE_DISABLE_CACHE_SYS_PROP_KEY, false);
    }

    public boolean setChecked(boolean z) {
        SystemProperties.set(TRANSCODE_DISABLE_CACHE_SYS_PROP_KEY, String.valueOf(z));
        return true;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_system;
    }
}
