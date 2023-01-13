package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.dagger.NotificationHeadsUpLog;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\t\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u000e\u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/stack/StackStateLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "appearAnimationEnded", "", "key", "", "disappearAnimationEnded", "logHUNViewAppearing", "logHUNViewAppearingWithAddEvent", "logHUNViewDisappearing", "logHUNViewDisappearingWithRemoveEvent", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StackStateLogger.kt */
public final class StackStateLogger {
    private final LogBuffer buffer;

    @Inject
    public StackStateLogger(@NotificationHeadsUpLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logHUNViewDisappearing(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("StackScroll", LogLevel.INFO, StackStateLogger$logHUNViewDisappearing$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logHUNViewAppearing(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("StackScroll", LogLevel.INFO, StackStateLogger$logHUNViewAppearing$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logHUNViewDisappearingWithRemoveEvent(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("StackScroll", LogLevel.ERROR, StackStateLogger$logHUNViewDisappearingWithRemoveEvent$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void logHUNViewAppearingWithAddEvent(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("StackScroll", LogLevel.ERROR, StackStateLogger$logHUNViewAppearingWithAddEvent$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void disappearAnimationEnded(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("StackScroll", LogLevel.INFO, StackStateLogger$disappearAnimationEnded$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    public final void appearAnimationEnded(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("StackScroll", LogLevel.INFO, StackStateLogger$appearAnimationEnded$2.INSTANCE);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }
}
