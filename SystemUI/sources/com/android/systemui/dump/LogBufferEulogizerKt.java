package com.android.systemui.dump;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "MAX_AGE_TO_DUMP", "", "MIN_WRITE_GAP", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogBufferEulogizer.kt */
public final class LogBufferEulogizerKt {
    /* access modifiers changed from: private */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.f700US);
    /* access modifiers changed from: private */
    public static final long MAX_AGE_TO_DUMP = TimeUnit.HOURS.toMillis(48);
    /* access modifiers changed from: private */
    public static final long MIN_WRITE_GAP = TimeUnit.MINUTES.toMillis(5);
    private static final String TAG = "BufferEulogizer";
}
