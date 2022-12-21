package com.android.systemui.log;

import java.text.SimpleDateFormat;
import java.util.Locale;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, mo64987d2 = {"DATE_FORMAT", "Ljava/text/SimpleDateFormat;", "FROZEN_MESSAGE", "Lcom/android/systemui/log/LogMessageImpl;", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogBuffer.kt */
public final class LogBufferKt {
    /* access modifiers changed from: private */
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.f700US);
    /* access modifiers changed from: private */
    public static final LogMessageImpl FROZEN_MESSAGE = LogMessageImpl.Factory.create();
    private static final String TAG = "LogBuffer";
}
