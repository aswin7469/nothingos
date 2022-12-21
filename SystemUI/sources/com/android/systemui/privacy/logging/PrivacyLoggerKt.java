package com.android.systemui.privacy.logging;

import java.text.SimpleDateFormat;
import java.util.Locale;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000¨\u0006\u0004"}, mo64987d2 = {"DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyLogger.kt */
public final class PrivacyLoggerKt {
    /* access modifiers changed from: private */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.f700US);
    private static final String TAG = "PrivacyLog";
}
