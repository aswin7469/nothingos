package com.android.systemui.broadcast.logging;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.settingslib.SliceBroadcastRelay;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessage;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.log.dagger.BroadcastDispatcherLog;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Iterator;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u0000 \"2\u00020\u0001:\u0001\"B\u0011\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004JE\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0017\u0010\t\u001a\u0013\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u00060\n¢\u0006\u0002\b\f2\u0019\b\b\u0010\r\u001a\u0013\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u000e0\n¢\u0006\u0002\b\fH\bJ \u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u0013\u001a\u00020\u0014J\u001e\u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u0018J\u0016\u0010\u0019\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u001e\u0010\u001a\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u001dJ\u0016\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u000eJ\u001e\u0010\u001f\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u001b\u001a\u00020\u0011J\u0016\u0010 \u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014J\u0016\u0010!\u001a\u00020\u00062\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u0014R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, mo64987d2 = {"Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;", "", "buffer", "Lcom/android/systemui/log/LogBuffer;", "(Lcom/android/systemui/log/LogBuffer;)V", "log", "", "logLevel", "Lcom/android/systemui/log/LogLevel;", "initializer", "Lkotlin/Function1;", "Lcom/android/systemui/log/LogMessage;", "Lkotlin/ExtensionFunctionType;", "printer", "", "logBroadcastDispatched", "broadcastId", "", "action", "receiver", "Landroid/content/BroadcastReceiver;", "logBroadcastReceived", "user", "intent", "Landroid/content/Intent;", "logClearedAfterRemoval", "logContextReceiverRegistered", "flags", "filter", "Landroid/content/IntentFilter;", "logContextReceiverUnregistered", "logReceiverRegistered", "logReceiverUnregistered", "logTagForRemoval", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BroadcastDispatcherLogger.kt */
public final class BroadcastDispatcherLogger {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private final LogBuffer buffer;

    @Inject
    public BroadcastDispatcherLogger(@BroadcastDispatcherLog LogBuffer logBuffer) {
        Intrinsics.checkNotNullParameter(logBuffer, "buffer");
        this.buffer = logBuffer;
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger$Companion;", "", "()V", "flagToString", "", "flag", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: BroadcastDispatcherLogger.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String flagToString(int i) {
            StringBuilder sb = new StringBuilder("");
            if ((i & 1) != 0) {
                sb.append("instant_apps,");
            }
            if ((i & 4) != 0) {
                sb.append("not_exported,");
            }
            if ((i & 2) != 0) {
                sb.append("exported");
            }
            if (sb.length() == 0) {
                sb.append(i);
            }
            String sb2 = sb.toString();
            Intrinsics.checkNotNullExpressionValue(sb2, "b.toString()");
            return sb2;
        }
    }

    public final void logBroadcastReceived(int i, int i2, Intent intent) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        String intent2 = intent.toString();
        Intrinsics.checkNotNullExpressionValue(intent2, "intent.toString()");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("BroadcastDispatcherLog", LogLevel.INFO, BroadcastDispatcherLogger$logBroadcastReceived$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setInt2(i2);
        obtain.setStr1(intent2);
        logBuffer.commit(obtain);
    }

    public final void logBroadcastDispatched(int i, String str, BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        String broadcastReceiver2 = broadcastReceiver.toString();
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("BroadcastDispatcherLog", LogLevel.DEBUG, BroadcastDispatcherLogger$logBroadcastDispatched$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        obtain.setStr2(broadcastReceiver2);
        logBuffer.commit(obtain);
    }

    public final void logReceiverRegistered(int i, BroadcastReceiver broadcastReceiver, int i2) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        String broadcastReceiver2 = broadcastReceiver.toString();
        String flagToString = Companion.flagToString(i2);
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("BroadcastDispatcherLog", LogLevel.INFO, BroadcastDispatcherLogger$logReceiverRegistered$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(broadcastReceiver2);
        obtain.setStr2(flagToString);
        logBuffer.commit(obtain);
    }

    public final void logTagForRemoval(int i, BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        String broadcastReceiver2 = broadcastReceiver.toString();
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("BroadcastDispatcherLog", LogLevel.DEBUG, BroadcastDispatcherLogger$logTagForRemoval$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(broadcastReceiver2);
        logBuffer.commit(obtain);
    }

    public final void logClearedAfterRemoval(int i, BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        String broadcastReceiver2 = broadcastReceiver.toString();
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("BroadcastDispatcherLog", LogLevel.DEBUG, BroadcastDispatcherLogger$logClearedAfterRemoval$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(broadcastReceiver2);
        logBuffer.commit(obtain);
    }

    public final void logReceiverUnregistered(int i, BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        String broadcastReceiver2 = broadcastReceiver.toString();
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("BroadcastDispatcherLog", LogLevel.INFO, BroadcastDispatcherLogger$logReceiverUnregistered$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(broadcastReceiver2);
        logBuffer.commit(obtain);
    }

    public final void logContextReceiverRegistered(int i, int i2, IntentFilter intentFilter) {
        String str;
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        Iterator<String> actionsIterator = intentFilter.actionsIterator();
        Intrinsics.checkNotNullExpressionValue(actionsIterator, "filter.actionsIterator()");
        String joinToString$default = SequencesKt.joinToString$default(SequencesKt.asSequence(actionsIterator), NavigationBarInflaterView.BUTTON_SEPARATOR, "Actions(", NavigationBarInflaterView.KEY_CODE_END, 0, (CharSequence) null, (Function1) null, 56, (Object) null);
        if (intentFilter.countCategories() != 0) {
            Iterator<String> categoriesIterator = intentFilter.categoriesIterator();
            Intrinsics.checkNotNullExpressionValue(categoriesIterator, "filter.categoriesIterator()");
            str = SequencesKt.joinToString$default(SequencesKt.asSequence(categoriesIterator), NavigationBarInflaterView.BUTTON_SEPARATOR, "Categories(", NavigationBarInflaterView.KEY_CODE_END, 0, (CharSequence) null, (Function1) null, 56, (Object) null);
        } else {
            str = "";
        }
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("BroadcastDispatcherLog", LogLevel.INFO, BroadcastDispatcherLogger$logContextReceiverRegistered$2.INSTANCE);
        obtain.setInt1(i);
        if (!Intrinsics.areEqual((Object) str, (Object) "")) {
            joinToString$default = joinToString$default + 10 + str;
        }
        obtain.setStr1(joinToString$default);
        obtain.setStr2(Companion.flagToString(i2));
        logBuffer.commit(obtain);
    }

    public final void logContextReceiverUnregistered(int i, String str) {
        Intrinsics.checkNotNullParameter(str, "action");
        LogBuffer logBuffer = this.buffer;
        LogMessage obtain = logBuffer.obtain("BroadcastDispatcherLog", LogLevel.INFO, BroadcastDispatcherLogger$logContextReceiverUnregistered$2.INSTANCE);
        obtain.setInt1(i);
        obtain.setStr1(str);
        logBuffer.commit(obtain);
    }

    private final void log(LogLevel logLevel, Function1<? super LogMessage, Unit> function1, Function1<? super LogMessage, String> function12) {
        LogBuffer logBuffer = this.buffer;
        LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, function12);
        function1.invoke(obtain);
        logBuffer.commit(obtain);
    }
}
