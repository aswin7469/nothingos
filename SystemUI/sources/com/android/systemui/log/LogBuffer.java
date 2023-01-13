package com.android.systemui.log;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Trace;
import android.util.Log;
import com.android.settingslib.datetime.ZoneGetter;
import com.android.systemui.util.collection.RingBuffer;
import java.p026io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.concurrent.ThreadsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B)\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u000e\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0010J\u0016\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u0005J\u0018\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J \u0010\u001f\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00102\u0006\u0010 \u001a\u00020\t2\u0006\u0010!\u001a\u00020\tH\u0002J\u0010\u0010\"\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u0010H\u0002J\u0018\u0010#\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00102\u0006\u0010$\u001a\u00020\u0003H\u0002J\u0018\u0010%\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u00102\u0006\u0010$\u001a\u00020\u0003H\u0002J\u0006\u0010&\u001a\u00020\u0018JP\u0010'\u001a\u00020\u00182\u0006\u0010(\u001a\u00020\u00032\u0006\u0010)\u001a\u00020*2\u0017\u0010+\u001a\u0013\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00180,¢\u0006\u0002\b-2\u0019\b\b\u0010.\u001a\u0013\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00030,¢\u0006\u0002\b-H\bø\u0001\u0000J*\u0010/\u001a\u00020\r2\u0006\u0010(\u001a\u00020\u00032\u0006\u0010)\u001a\u00020*2\u0012\u0010.\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00030,J\u0006\u00100\u001a\u00020\u0018R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0012\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\t@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\u00020\t8BX\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000\u0002\u0007\n\u0005\b20\u0001¨\u00061"}, mo65043d2 = {"Lcom/android/systemui/log/LogBuffer;", "", "name", "", "maxSize", "", "logcatEchoTracker", "Lcom/android/systemui/log/LogcatEchoTracker;", "systrace", "", "(Ljava/lang/String;ILcom/android/systemui/log/LogcatEchoTracker;Z)V", "buffer", "Lcom/android/systemui/util/collection/RingBuffer;", "Lcom/android/systemui/log/LogMessageImpl;", "echoMessageQueue", "Ljava/util/concurrent/BlockingQueue;", "Lcom/android/systemui/log/LogMessage;", "<set-?>", "frozen", "getFrozen", "()Z", "mutable", "getMutable", "commit", "", "message", "dump", "pw", "Ljava/io/PrintWriter;", "tailLength", "dumpMessage", "echo", "toLogcat", "toSystrace", "echoToDesiredEndpoints", "echoToLogcat", "strMessage", "echoToSystrace", "freeze", "log", "tag", "level", "Lcom/android/systemui/log/LogLevel;", "initializer", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "printer", "obtain", "unfreeze", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LogBuffer.kt */
public final class LogBuffer {
    private final RingBuffer<LogMessageImpl> buffer;
    /* access modifiers changed from: private */
    public final BlockingQueue<LogMessage> echoMessageQueue;
    private boolean frozen;
    private final LogcatEchoTracker logcatEchoTracker;
    private final int maxSize;
    private final String name;
    private final boolean systrace;

    @Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: LogBuffer.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[LogLevel.values().length];
            iArr[LogLevel.VERBOSE.ordinal()] = 1;
            iArr[LogLevel.DEBUG.ordinal()] = 2;
            iArr[LogLevel.INFO.ordinal()] = 3;
            iArr[LogLevel.WARNING.ordinal()] = 4;
            iArr[LogLevel.ERROR.ordinal()] = 5;
            iArr[LogLevel.WTF.ordinal()] = 6;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public LogBuffer(String str, int i, LogcatEchoTracker logcatEchoTracker2) {
        this(str, i, logcatEchoTracker2, false, 8, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(logcatEchoTracker2, "logcatEchoTracker");
    }

    public LogBuffer(String str, int i, LogcatEchoTracker logcatEchoTracker2, boolean z) {
        Intrinsics.checkNotNullParameter(str, ZoneGetter.KEY_DISPLAYNAME);
        Intrinsics.checkNotNullParameter(logcatEchoTracker2, "logcatEchoTracker");
        this.name = str;
        this.maxSize = i;
        this.logcatEchoTracker = logcatEchoTracker2;
        this.systrace = z;
        this.buffer = new RingBuffer<>(i, LogBuffer$buffer$1.INSTANCE);
        BlockingQueue<LogMessage> arrayBlockingQueue = logcatEchoTracker2.getLogInBackgroundThread() ? new ArrayBlockingQueue<>(10) : null;
        this.echoMessageQueue = arrayBlockingQueue;
        if (logcatEchoTracker2.getLogInBackgroundThread() && arrayBlockingQueue != null) {
            ThreadsKt.thread$default(true, false, (ClassLoader) null, "LogBuffer-" + str, 5, new Function0<Unit>(this) {
                final /* synthetic */ LogBuffer this$0;

                {
                    this.this$0 = r1;
                }

                public final void invoke() {
                    while (true) {
                        try {
                            LogBuffer logBuffer = this.this$0;
                            Object take = logBuffer.echoMessageQueue.take();
                            Intrinsics.checkNotNullExpressionValue(take, "echoMessageQueue.take()");
                            logBuffer.echoToDesiredEndpoints((LogMessage) take);
                        } catch (InterruptedException unused) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                }
            }, 6, (Object) null);
        }
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ LogBuffer(String str, int i, LogcatEchoTracker logcatEchoTracker2, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i, logcatEchoTracker2, (i2 & 8) != 0 ? true : z);
    }

    public final boolean getFrozen() {
        return this.frozen;
    }

    private final boolean getMutable() {
        return !this.frozen && this.maxSize > 0;
    }

    public final void log(String str, LogLevel logLevel, Function1<? super LogMessage, Unit> function1, Function1<? super LogMessage, String> function12) {
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        Intrinsics.checkNotNullParameter(function1, "initializer");
        Intrinsics.checkNotNullParameter(function12, "printer");
        LogMessageImpl obtain = obtain(str, logLevel, function12);
        function1.invoke(obtain);
        commit(obtain);
    }

    public final synchronized LogMessageImpl obtain(String str, LogLevel logLevel, Function1<? super LogMessage, String> function1) {
        Intrinsics.checkNotNullParameter(str, "tag");
        Intrinsics.checkNotNullParameter(logLevel, "level");
        Intrinsics.checkNotNullParameter(function1, "printer");
        if (!getMutable()) {
            return LogBufferKt.FROZEN_MESSAGE;
        }
        LogMessageImpl advance = this.buffer.advance();
        advance.reset(str, logLevel, System.currentTimeMillis(), function1);
        return advance;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:12|13|14|15) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x001e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized void commit(com.android.systemui.log.LogMessage r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            java.lang.String r0 = "message"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)     // Catch:{ all -> 0x0027 }
            boolean r0 = r1.getMutable()     // Catch:{ all -> 0x0027 }
            if (r0 != 0) goto L_0x000e
            monitor-exit(r1)
            return
        L_0x000e:
            java.util.concurrent.BlockingQueue<com.android.systemui.log.LogMessage> r0 = r1.echoMessageQueue     // Catch:{ all -> 0x0027 }
            if (r0 == 0) goto L_0x0022
            int r0 = r0.remainingCapacity()     // Catch:{ all -> 0x0027 }
            if (r0 <= 0) goto L_0x0022
            java.util.concurrent.BlockingQueue<com.android.systemui.log.LogMessage> r0 = r1.echoMessageQueue     // Catch:{ InterruptedException -> 0x001e }
            r0.put(r2)     // Catch:{ InterruptedException -> 0x001e }
            goto L_0x0025
        L_0x001e:
            r1.echoToDesiredEndpoints(r2)     // Catch:{ all -> 0x0027 }
            goto L_0x0025
        L_0x0022:
            r1.echoToDesiredEndpoints(r2)     // Catch:{ all -> 0x0027 }
        L_0x0025:
            monitor-exit(r1)
            return
        L_0x0027:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.log.LogBuffer.commit(com.android.systemui.log.LogMessage):void");
    }

    /* access modifiers changed from: private */
    public final void echoToDesiredEndpoints(LogMessage logMessage) {
        echo(logMessage, this.logcatEchoTracker.isBufferLoggable(this.name, logMessage.getLevel()) || this.logcatEchoTracker.isTagLoggable(logMessage.getTag(), logMessage.getLevel()), this.systrace);
    }

    public final synchronized void dump(PrintWriter printWriter, int i) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        int i2 = 0;
        if (i > 0) {
            i2 = Math.max(0, this.buffer.getSize() - i);
        }
        int size = this.buffer.getSize();
        while (i2 < size) {
            dumpMessage(this.buffer.get(i2), printWriter);
            i2++;
        }
    }

    public final synchronized void freeze() {
        if (!this.frozen) {
            LogMessageImpl obtain = obtain("LogBuffer", LogLevel.DEBUG, LogBuffer$freeze$2.INSTANCE);
            obtain.setStr1(this.name);
            commit(obtain);
            this.frozen = true;
        }
    }

    public final synchronized void unfreeze() {
        if (this.frozen) {
            LogMessageImpl obtain = obtain("LogBuffer", LogLevel.DEBUG, LogBuffer$unfreeze$2.INSTANCE);
            obtain.setStr1(this.name);
            commit(obtain);
            this.frozen = false;
        }
    }

    private final void dumpMessage(LogMessage logMessage, PrintWriter printWriter) {
        printWriter.print(LogBufferKt.DATE_FORMAT.format(Long.valueOf(logMessage.getTimestamp())));
        printWriter.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        printWriter.print(logMessage.getLevel().getShortString());
        printWriter.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        printWriter.print(logMessage.getTag());
        printWriter.print(": ");
        printWriter.println(logMessage.getPrinter().invoke(logMessage));
    }

    private final void echo(LogMessage logMessage, boolean z, boolean z2) {
        if (z || z2) {
            String invoke = logMessage.getPrinter().invoke(logMessage);
            if (z2) {
                echoToSystrace(logMessage, invoke);
            }
            if (z) {
                echoToLogcat(logMessage, invoke);
            }
        }
    }

    private final void echoToSystrace(LogMessage logMessage, String str) {
        Trace.instantForTrack(4096, "UI Events", this.name + " - " + logMessage.getLevel().getShortString() + ' ' + logMessage.getTag() + ": " + str);
    }

    private final void echoToLogcat(LogMessage logMessage, String str) {
        switch (WhenMappings.$EnumSwitchMapping$0[logMessage.getLevel().ordinal()]) {
            case 1:
                Log.v(logMessage.getTag(), str);
                return;
            case 2:
                Log.d(logMessage.getTag(), str);
                return;
            case 3:
                Log.i(logMessage.getTag(), str);
                return;
            case 4:
                Log.w(logMessage.getTag(), str);
                return;
            case 5:
                Log.e(logMessage.getTag(), str);
                return;
            case 6:
                Log.wtf(logMessage.getTag(), str);
                return;
            default:
                return;
        }
    }
}
