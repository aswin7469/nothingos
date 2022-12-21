package com.android.systemui.log;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016R\u0014\u0010\u0003\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/log/LogcatEchoTrackerProd;", "Lcom/android/systemui/log/LogcatEchoTracker;", "()V", "logInBackgroundThread", "", "getLogInBackgroundThread", "()Z", "isBufferLoggable", "bufferName", "", "level", "Lcom/android/systemui/log/LogLevel;", "isTagLoggable", "tagName", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogcatEchoTrackerProd.kt */
public final class LogcatEchoTrackerProd implements LogcatEchoTracker {
    private final boolean logInBackgroundThread;

    public boolean getLogInBackgroundThread() {
        return this.logInBackgroundThread;
    }

    public boolean isBufferLoggable(String str, LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(str, "bufferName");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        return logLevel.compareTo(LogLevel.WARNING) >= 0;
    }

    public boolean isTagLoggable(String str, LogLevel logLevel) {
        Intrinsics.checkNotNullParameter(str, "tagName");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        return logLevel.compareTo(LogLevel.WARNING) >= 0;
    }
}
