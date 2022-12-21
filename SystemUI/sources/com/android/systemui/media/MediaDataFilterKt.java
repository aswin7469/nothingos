package com.android.systemui.media;

import android.os.SystemProperties;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0006\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u001c\u0010\u0005\u001a\u00020\u00068\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\n\"\u000e\u0010\u000b\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo64987d2 = {"DEBUG", "", "EXPORTED_SMARTSPACE_TRAMPOLINE_ACTIVITY_NAME", "", "RESUMABLE_MEDIA_MAX_AGE_SECONDS_KEY", "SMARTSPACE_MAX_AGE", "", "getSMARTSPACE_MAX_AGE$annotations", "()V", "getSMARTSPACE_MAX_AGE", "()J", "TAG", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaDataFilter.kt */
public final class MediaDataFilterKt {
    private static final boolean DEBUG = true;
    private static final String EXPORTED_SMARTSPACE_TRAMPOLINE_ACTIVITY_NAME = "com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity";
    private static final String RESUMABLE_MEDIA_MAX_AGE_SECONDS_KEY = "resumable_media_max_age_seconds";
    private static final long SMARTSPACE_MAX_AGE = SystemProperties.getLong("debug.sysui.smartspace_max_age", TimeUnit.MINUTES.toMillis(30));
    private static final String TAG = "MediaDataFilter";

    public static /* synthetic */ void getSMARTSPACE_MAX_AGE$annotations() {
    }

    public static final long getSMARTSPACE_MAX_AGE() {
        return SMARTSPACE_MAX_AGE;
    }
}
