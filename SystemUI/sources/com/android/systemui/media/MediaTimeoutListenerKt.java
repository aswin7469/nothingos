package com.android.systemui.media;

import android.os.SystemProperties;
import com.android.internal.annotations.VisibleForTesting;
import java.util.concurrent.TimeUnit;
/* compiled from: MediaTimeoutListener.kt */
/* loaded from: classes.dex */
public final class MediaTimeoutListenerKt {
    private static final long PAUSED_MEDIA_TIMEOUT = SystemProperties.getLong("debug.sysui.media_timeout", TimeUnit.MINUTES.toMillis(10));
    private static final long RESUME_MEDIA_TIMEOUT = SystemProperties.getLong("debug.sysui.media_timeout_resume", TimeUnit.DAYS.toMillis(3));

    @VisibleForTesting
    public static /* synthetic */ void getPAUSED_MEDIA_TIMEOUT$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getRESUME_MEDIA_TIMEOUT$annotations() {
    }

    public static final long getPAUSED_MEDIA_TIMEOUT() {
        return PAUSED_MEDIA_TIMEOUT;
    }

    public static final long getRESUME_MEDIA_TIMEOUT() {
        return RESUME_MEDIA_TIMEOUT;
    }
}
