package com.android.systemui.toast;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.log.dagger.ToastLog;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004JE\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0017\u0010\t\u001a\u0013\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00060\n¢\u0006\u0002\b\f2\u0019\b\b\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000e0\n¢\u0006\u0002\b\fH\bJ\u0016\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000eJ&\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000eJ\u0016\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0017\u001a\u00020\u0018R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/toast/ToastLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "log", "", "logLevel", "Lcom/android/systemui/log/LogLevel;", "initializer", "Lkotlin/Function1;", "Lcom/android/systemui/log/LogMessage;", "Lkotlin/ExtensionFunctionType;", "printer", "", "logOnHideToast", "packageName", "token", "logOnShowToast", "uid", "", "text", "logOrientationChange", "isPortrait", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ToastLogger.kt */
public final class ToastLogger {
    private final LogBuffer buffer;

    @Inject
    public ToastLogger(@ToastLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    public final void logOnShowToast(int i, String str, String str2, String str3) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(str2, "text");
        Intrinsics.checkNotNullParameter(str3, "token");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ToastLog", LogLevel.DEBUG, ToastLogger$logOnShowToast$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        obtain.setStr3(str3);
        logBuffer.commit(obtain);
    }

    public final void logOnHideToast(String str, String str2) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Intrinsics.checkNotNullParameter(str2, "token");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ToastLog", LogLevel.DEBUG, ToastLogger$logOnHideToast$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setStr2(str2);
        logBuffer.commit(obtain);
    }

    public final void logOrientationChange(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "text");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("ToastLog", LogLevel.DEBUG, ToastLogger$logOrientationChange$2.INSTANCE);
        obtain.setStr1(str);
        obtain.setBool1(z);
        logBuffer.commit(obtain);
    }

    private final void log(LogLevel logLevel, Function1<? super LogMessage, Unit> function1, Function1<? super LogMessage, String> function12) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("ToastLog", logLevel, function12);
        function1.invoke(obtain);
        logBuffer.commit(obtain);
    }
}
