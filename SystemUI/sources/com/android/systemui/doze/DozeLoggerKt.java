package com.android.systemui.doze;

import java.text.SimpleDateFormat;
import java.util.Locale;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\"\u0011\u0010\u0000\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, mo65043d2 = {"DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "getDATE_FORMAT", "()Ljava/text/SimpleDateFormat;", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DozeLogger.kt */
public final class DozeLoggerKt {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.S", Locale.f698US);
    private static final String TAG = "DozeLog";

    public static final SimpleDateFormat getDATE_FORMAT() {
        return DATE_FORMAT;
    }
}
