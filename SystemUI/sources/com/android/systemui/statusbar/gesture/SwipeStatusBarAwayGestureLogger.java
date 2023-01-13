package com.android.systemui.statusbar.gesture;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.SwipeStatusBarAwayLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\u000b\u001a\u00020\u0006J\u0006\u0010\f\u001a\u00020\u0006R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/gesture/SwipeStatusBarAwayGestureLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "logGestureDetected", "", "y", "", "logGestureDetectionEndedWithoutTriggering", "logGestureDetectionStarted", "logInputListeningStarted", "logInputListeningStopped", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SwipeStatusBarAwayGestureLogger.kt */
public final class SwipeStatusBarAwayGestureLogger {
    private final LogBuffer buffer;

    @Inject
    public SwipeStatusBarAwayGestureLogger(@SwipeStatusBarAwayLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logGestureDetectionStarted(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.DEBUG, SwipeStatusBarAwayGestureLogger$logGestureDetectionStarted$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logGestureDetectionEndedWithoutTriggering(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.DEBUG, C2643x6caa9506.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logGestureDetected(int i) {
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.INFO, SwipeStatusBarAwayGestureLogger$logGestureDetected$2.INSTANCE);
        obtain.setInt1(i);
        logBuffer.commit(obtain);
    }

    public final void logInputListeningStarted() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.VERBOSE, SwipeStatusBarAwayGestureLogger$logInputListeningStarted$2.INSTANCE));
    }

    public final void logInputListeningStopped() {
        LogBuffer logBuffer = this.buffer;
        logBuffer.commit(logBuffer.obtain("SwipeStatusBarAwayGestureHandler", LogLevel.VERBOSE, SwipeStatusBarAwayGestureLogger$logInputListeningStopped$2.INSTANCE));
    }
}
