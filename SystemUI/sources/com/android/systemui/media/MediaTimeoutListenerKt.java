package com.android.systemui.media;

import android.os.SystemProperties;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\n\n\u0000\n\u0002\u0010\t\n\u0002\b\b\"\u001c\u0010\u0000\u001a\u00020\u00018\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\"\u001c\u0010\u0006\u001a\u00020\u00018\u0006X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0007\u0010\u0003\u001a\u0004\b\b\u0010\u0005¨\u0006\t"}, mo65043d2 = {"PAUSED_MEDIA_TIMEOUT", "", "getPAUSED_MEDIA_TIMEOUT$annotations", "()V", "getPAUSED_MEDIA_TIMEOUT", "()J", "RESUME_MEDIA_TIMEOUT", "getRESUME_MEDIA_TIMEOUT$annotations", "getRESUME_MEDIA_TIMEOUT", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaTimeoutListener.kt */
public final class MediaTimeoutListenerKt {
    private static final long PAUSED_MEDIA_TIMEOUT = SystemProperties.getLong("debug.sysui.media_timeout", TimeUnit.MINUTES.toMillis(10));
    private static final long RESUME_MEDIA_TIMEOUT = SystemProperties.getLong("debug.sysui.media_timeout_resume", TimeUnit.DAYS.toMillis(3));

    public static /* synthetic */ void getPAUSED_MEDIA_TIMEOUT$annotations() {
    }

    public static /* synthetic */ void getRESUME_MEDIA_TIMEOUT$annotations() {
    }

    public static final long getPAUSED_MEDIA_TIMEOUT() {
        return PAUSED_MEDIA_TIMEOUT;
    }

    public static final long getRESUME_MEDIA_TIMEOUT() {
        return RESUME_MEDIA_TIMEOUT;
    }
}
