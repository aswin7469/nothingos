package com.android.systemui.toast;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ToastLogger.kt */
/* loaded from: classes2.dex */
public final class ToastLogger {
    @NotNull
    private final LogBuffer buffer;

    public ToastLogger(@NotNull LogBuffer buffer) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
    }

    public final void logOnShowToast(int i, @NotNull String packageName, @NotNull String text, @NotNull String token) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(text, "text");
        Intrinsics.checkNotNullParameter(token, "token");
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOnShowToast$2 toastLogger$logOnShowToast$2 = ToastLogger$logOnShowToast$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOnShowToast$2);
            obtain.setInt1(i);
            obtain.setStr1(packageName);
            obtain.setStr2(text);
            obtain.setStr3(token);
            logBuffer.push(obtain);
        }
    }

    public final void logOnHideToast(@NotNull String packageName, @NotNull String token) {
        Intrinsics.checkNotNullParameter(packageName, "packageName");
        Intrinsics.checkNotNullParameter(token, "token");
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOnHideToast$2 toastLogger$logOnHideToast$2 = ToastLogger$logOnHideToast$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOnHideToast$2);
            obtain.setStr1(packageName);
            obtain.setStr2(token);
            logBuffer.push(obtain);
        }
    }

    public final void logOrientationChange(@NotNull String text, boolean z) {
        Intrinsics.checkNotNullParameter(text, "text");
        LogLevel logLevel = LogLevel.DEBUG;
        ToastLogger$logOrientationChange$2 toastLogger$logOrientationChange$2 = ToastLogger$logOrientationChange$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        if (!logBuffer.getFrozen()) {
            LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, toastLogger$logOrientationChange$2);
            obtain.setStr1(text);
            obtain.setBool1(z);
            logBuffer.push(obtain);
        }
    }
}
