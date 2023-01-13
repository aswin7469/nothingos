package com.android.systemui.media;

import android.content.Intent;
import android.util.Log;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"DEBUG", "", "TAG", "", "settingsIntent", "Landroid/content/Intent;", "kotlin.jvm.PlatformType", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaCarouselController.kt */
public final class MediaCarouselControllerKt {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "MediaCarouselController";
    /* access modifiers changed from: private */
    public static final Intent settingsIntent = new Intent().setAction("android.settings.ACTION_MEDIA_CONTROLS_SETTINGS");
}
