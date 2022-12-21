package com.android.systemui.log;

import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&J\u0018\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH&R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\rÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/log/LogcatEchoTracker;", "", "logInBackgroundThread", "", "getLogInBackgroundThread", "()Z", "isBufferLoggable", "bufferName", "", "level", "Lcom/android/systemui/log/LogLevel;", "isTagLoggable", "tagName", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogcatEchoTracker.kt */
public interface LogcatEchoTracker {
    boolean getLogInBackgroundThread();

    boolean isBufferLoggable(String str, LogLevel logLevel);

    boolean isTagLoggable(String str, LogLevel logLevel);
}
