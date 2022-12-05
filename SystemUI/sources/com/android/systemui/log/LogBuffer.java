package com.android.systemui.log;

import android.util.Log;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: LogBuffer.kt */
/* loaded from: classes.dex */
public final class LogBuffer {
    @NotNull
    private final ArrayDeque<LogMessageImpl> buffer = new ArrayDeque<>();
    private boolean frozen;
    @NotNull
    private final LogcatEchoTracker logcatEchoTracker;
    private final int maxLogs;
    @NotNull
    private final String name;
    private final int poolSize;

    /* compiled from: LogBuffer.kt */
    /* loaded from: classes.dex */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[LogLevel.valuesCustom().length];
            iArr[LogLevel.VERBOSE.ordinal()] = 1;
            iArr[LogLevel.DEBUG.ordinal()] = 2;
            iArr[LogLevel.INFO.ordinal()] = 3;
            iArr[LogLevel.WARNING.ordinal()] = 4;
            iArr[LogLevel.ERROR.ordinal()] = 5;
            iArr[LogLevel.WTF.ordinal()] = 6;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public LogBuffer(@NotNull String name, int i, int i2, @NotNull LogcatEchoTracker logcatEchoTracker) {
        Intrinsics.checkNotNullParameter(name, "name");
        Intrinsics.checkNotNullParameter(logcatEchoTracker, "logcatEchoTracker");
        this.name = name;
        this.maxLogs = i;
        this.poolSize = i2;
        this.logcatEchoTracker = logcatEchoTracker;
    }

    public final boolean getFrozen() {
        return this.frozen;
    }

    @NotNull
    public final synchronized LogMessageImpl obtain(@NotNull String tag, @NotNull LogLevel level, @NotNull Function1<? super LogMessage, String> printer) {
        LogMessageImpl message;
        Intrinsics.checkNotNullParameter(tag, "tag");
        Intrinsics.checkNotNullParameter(level, "level");
        Intrinsics.checkNotNullParameter(printer, "printer");
        if (this.frozen) {
            message = LogMessageImpl.Factory.create();
        } else {
            message = this.buffer.size() > this.maxLogs - this.poolSize ? this.buffer.removeFirst() : LogMessageImpl.Factory.create();
        }
        message.reset(tag, level, System.currentTimeMillis(), printer);
        Intrinsics.checkNotNullExpressionValue(message, "message");
        return message;
    }

    public final synchronized void push(@NotNull LogMessage message) {
        Intrinsics.checkNotNullParameter(message, "message");
        if (this.frozen) {
            return;
        }
        if (this.buffer.size() == this.maxLogs) {
            Log.e("LogBuffer", "LogBuffer " + this.name + " has exceeded its pool size");
            this.buffer.removeFirst();
        }
        this.buffer.add((LogMessageImpl) message);
        if (this.logcatEchoTracker.isBufferLoggable(this.name, ((LogMessageImpl) message).getLevel()) || this.logcatEchoTracker.isTagLoggable(((LogMessageImpl) message).getTag(), ((LogMessageImpl) message).getLevel())) {
            echoToLogcat(message);
        }
    }

    public final synchronized void dump(@NotNull PrintWriter pw, int i) {
        Intrinsics.checkNotNullParameter(pw, "pw");
        int i2 = 0;
        int size = i <= 0 ? 0 : this.buffer.size() - i;
        Iterator<LogMessageImpl> it = this.buffer.iterator();
        while (it.hasNext()) {
            int i3 = i2 + 1;
            LogMessageImpl message = it.next();
            if (i2 >= size) {
                Intrinsics.checkNotNullExpressionValue(message, "message");
                dumpMessage(message, pw);
            }
            i2 = i3;
        }
    }

    public final synchronized void freeze() {
        if (!this.frozen) {
            LogLevel logLevel = LogLevel.DEBUG;
            LogBuffer$freeze$2 logBuffer$freeze$2 = LogBuffer$freeze$2.INSTANCE;
            if (!getFrozen()) {
                LogMessageImpl obtain = obtain("LogBuffer", logLevel, logBuffer$freeze$2);
                obtain.setStr1(this.name);
                push(obtain);
            }
            this.frozen = true;
        }
    }

    public final synchronized void unfreeze() {
        if (this.frozen) {
            LogLevel logLevel = LogLevel.DEBUG;
            LogBuffer$unfreeze$2 logBuffer$unfreeze$2 = LogBuffer$unfreeze$2.INSTANCE;
            if (!getFrozen()) {
                LogMessageImpl obtain = obtain("LogBuffer", logLevel, logBuffer$unfreeze$2);
                obtain.setStr1(this.name);
                push(obtain);
            }
            this.frozen = false;
        }
    }

    private final void dumpMessage(LogMessage logMessage, PrintWriter printWriter) {
        SimpleDateFormat simpleDateFormat;
        simpleDateFormat = LogBufferKt.DATE_FORMAT;
        printWriter.print(simpleDateFormat.format(Long.valueOf(logMessage.getTimestamp())));
        printWriter.print(" ");
        printWriter.print(logMessage.getLevel().getShortString());
        printWriter.print(" ");
        printWriter.print(logMessage.getTag());
        printWriter.print(": ");
        printWriter.println(logMessage.getPrinter().mo1949invoke(logMessage));
    }

    private final void echoToLogcat(LogMessage logMessage) {
        String mo1949invoke = logMessage.getPrinter().mo1949invoke(logMessage);
        switch (WhenMappings.$EnumSwitchMapping$0[logMessage.getLevel().ordinal()]) {
            case 1:
                Log.v(logMessage.getTag(), mo1949invoke);
                return;
            case 2:
                Log.d(logMessage.getTag(), mo1949invoke);
                return;
            case 3:
                Log.i(logMessage.getTag(), mo1949invoke);
                return;
            case 4:
                Log.w(logMessage.getTag(), mo1949invoke);
                return;
            case 5:
                Log.e(logMessage.getTag(), mo1949invoke);
                return;
            case 6:
                Log.wtf(logMessage.getTag(), mo1949invoke);
                return;
            default:
                return;
        }
    }
}
