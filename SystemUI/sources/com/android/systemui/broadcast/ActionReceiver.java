package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.settingslib.SliceBroadcastRelay;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.p026io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 .2\u00020\u00012\u00020\u0002:\u0001.Bw\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u001d\u0010\u0007\u001a\u0019\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b¢\u0006\u0002\b\u000b\u0012\u0017\u0010\f\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\n0\r¢\u0006\u0002\b\u000b\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0018\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00130\b¢\u0006\u0002\u0010\u0014J\u000e\u0010\u001d\u001a\u00020\n2\u0006\u0010\u001e\u001a\u00020\u0018J\b\u0010\u001f\u001a\u00020\tH\u0002J%\u0010 \u001a\u00020\n2\u0006\u0010!\u001a\u00020\"2\u000e\u0010#\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u00040$H\u0016¢\u0006\u0002\u0010%J\u000e\u0010&\u001a\u00020\u00132\u0006\u0010'\u001a\u00020\u0001J\u0018\u0010(\u001a\u00020\n2\u0006\u0010)\u001a\u00020*2\u0006\u0010+\u001a\u00020,H\u0016J\u000e\u0010-\u001a\u00020\n2\u0006\u0010'\u001a\u00020\u0001R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00040\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00180\u0016X\u0004¢\u0006\u0002\n\u0000R%\u0010\u0007\u001a\u0019\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\b¢\u0006\u0002\b\u000bX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u0019\u001a\u00020\u0013@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR \u0010\u0012\u001a\u0014\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00130\bX\u0004¢\u0006\u0002\n\u0000R\u001f\u0010\f\u001a\u0013\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\n0\r¢\u0006\u0002\b\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006/"}, mo64987d2 = {"Lcom/android/systemui/broadcast/ActionReceiver;", "Landroid/content/BroadcastReceiver;", "Lcom/android/systemui/Dumpable;", "action", "", "userId", "", "registerAction", "Lkotlin/Function2;", "Landroid/content/IntentFilter;", "", "Lkotlin/ExtensionFunctionType;", "unregisterAction", "Lkotlin/Function1;", "bgExecutor", "Ljava/util/concurrent/Executor;", "logger", "Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;", "testPendingRemovalAction", "", "(Ljava/lang/String;ILkotlin/jvm/functions/Function2;Lkotlin/jvm/functions/Function1;Ljava/util/concurrent/Executor;Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;Lkotlin/jvm/functions/Function2;)V", "activeCategories", "Landroid/util/ArraySet;", "receiverDatas", "Lcom/android/systemui/broadcast/ReceiverData;", "<set-?>", "registered", "getRegistered", "()Z", "addReceiverData", "receiverData", "createFilter", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "hasReceiver", "receiver", "onReceive", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "removeReceiver", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ActionReceiver.kt */
public final class ActionReceiver extends BroadcastReceiver implements Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final AtomicInteger index = new AtomicInteger(0);
    private final String action;
    private final ArraySet<String> activeCategories = new ArraySet<>();
    private final Executor bgExecutor;
    private final BroadcastDispatcherLogger logger;
    private final ArraySet<ReceiverData> receiverDatas = new ArraySet<>();
    private final Function2<BroadcastReceiver, IntentFilter, Unit> registerAction;
    private boolean registered;
    private final Function2<BroadcastReceiver, Integer, Boolean> testPendingRemovalAction;
    private final Function1<BroadcastReceiver, Unit> unregisterAction;
    private final int userId;

    public ActionReceiver(String str, int i, Function2<? super BroadcastReceiver, ? super IntentFilter, Unit> function2, Function1<? super BroadcastReceiver, Unit> function1, Executor executor, BroadcastDispatcherLogger broadcastDispatcherLogger, Function2<? super BroadcastReceiver, ? super Integer, Boolean> function22) {
        Intrinsics.checkNotNullParameter(str, "action");
        Intrinsics.checkNotNullParameter(function2, "registerAction");
        Intrinsics.checkNotNullParameter(function1, "unregisterAction");
        Intrinsics.checkNotNullParameter(executor, "bgExecutor");
        Intrinsics.checkNotNullParameter(broadcastDispatcherLogger, "logger");
        Intrinsics.checkNotNullParameter(function22, "testPendingRemovalAction");
        this.action = str;
        this.userId = i;
        this.registerAction = function2;
        this.unregisterAction = function1;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
        this.testPendingRemovalAction = function22;
    }

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/broadcast/ActionReceiver$Companion;", "", "()V", "index", "Ljava/util/concurrent/atomic/AtomicInteger;", "getIndex", "()Ljava/util/concurrent/atomic/AtomicInteger;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ActionReceiver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final AtomicInteger getIndex() {
            return ActionReceiver.index;
        }
    }

    public final boolean getRegistered() {
        return this.registered;
    }

    public final void addReceiverData(ReceiverData receiverData) throws IllegalArgumentException {
        Sequence<T> sequence;
        Intrinsics.checkNotNullParameter(receiverData, "receiverData");
        if (receiverData.getFilter().hasAction(this.action)) {
            Collection collection = this.activeCategories;
            Iterator<String> categoriesIterator = receiverData.getFilter().categoriesIterator();
            if (categoriesIterator == null || (sequence = SequencesKt.asSequence(categoriesIterator)) == null) {
                sequence = SequencesKt.emptySequence();
            }
            boolean addAll = CollectionsKt.addAll(collection, sequence);
            if (this.receiverDatas.add(receiverData) && this.receiverDatas.size() == 1) {
                this.registerAction.invoke(this, createFilter());
                this.registered = true;
            } else if (addAll) {
                this.unregisterAction.invoke(this);
                this.registerAction.invoke(this, createFilter());
            }
        } else {
            throw new IllegalArgumentException("Trying to attach to " + this.action + " without correct action,receiver: " + receiverData.getReceiver());
        }
    }

    public final boolean hasReceiver(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Iterable<ReceiverData> iterable = this.receiverDatas;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (ReceiverData receiver : iterable) {
            if (Intrinsics.areEqual((Object) receiver.getReceiver(), (Object) broadcastReceiver)) {
                return true;
            }
        }
        return false;
    }

    private final IntentFilter createFilter() {
        IntentFilter intentFilter = new IntentFilter(this.action);
        for (String addCategory : this.activeCategories) {
            intentFilter.addCategory(addCategory);
        }
        return intentFilter;
    }

    public final void removeReceiver(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        if (CollectionsKt.removeAll(this.receiverDatas, new ActionReceiver$removeReceiver$1(broadcastReceiver)) && this.receiverDatas.isEmpty() && this.registered) {
            this.unregisterAction.invoke(this);
            this.registered = false;
            this.activeCategories.clear();
        }
    }

    public void onReceive(Context context, Intent intent) throws IllegalStateException {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (Intrinsics.areEqual((Object) intent.getAction(), (Object) this.action)) {
            int andIncrement = index.getAndIncrement();
            this.logger.logBroadcastReceived(andIncrement, this.userId, intent);
            this.bgExecutor.execute(new ActionReceiver$$ExternalSyntheticLambda0(this, intent, context, andIncrement));
            return;
        }
        throw new IllegalStateException("Received intent for " + intent.getAction() + " in receiver for " + this.action + '}');
    }

    /* access modifiers changed from: private */
    /* renamed from: onReceive$lambda-3  reason: not valid java name */
    public static final void m2592onReceive$lambda3(ActionReceiver actionReceiver, Intent intent, Context context, int i) {
        Intrinsics.checkNotNullParameter(actionReceiver, "this$0");
        Intrinsics.checkNotNullParameter(intent, "$intent");
        Intrinsics.checkNotNullParameter(context, "$context");
        for (ReceiverData receiverData : actionReceiver.receiverDatas) {
            if (receiverData.getFilter().matchCategories(intent.getCategories()) == null && !actionReceiver.testPendingRemovalAction.invoke(receiverData.getReceiver(), Integer.valueOf(actionReceiver.userId)).booleanValue()) {
                receiverData.getExecutor().execute(new ActionReceiver$$ExternalSyntheticLambda1(receiverData, actionReceiver, context, intent, i));
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onReceive$lambda-3$lambda-2$lambda-1  reason: not valid java name */
    public static final void m2593onReceive$lambda3$lambda2$lambda1(ReceiverData receiverData, ActionReceiver actionReceiver, Context context, Intent intent, int i) {
        Intrinsics.checkNotNullParameter(actionReceiver, "this$0");
        Intrinsics.checkNotNullParameter(context, "$context");
        Intrinsics.checkNotNullParameter(intent, "$intent");
        receiverData.getReceiver().setPendingResult(actionReceiver.getPendingResult());
        receiverData.getReceiver().onReceive(context, intent);
        actionReceiver.logger.logBroadcastDispatched(i, actionReceiver.action, receiverData.getReceiver());
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        boolean z = printWriter instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        printWriter.println("Registered: " + this.registered);
        printWriter.println("Receivers:");
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        for (ReceiverData receiver : this.receiverDatas) {
            printWriter.println((Object) receiver.getReceiver());
        }
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
        printWriter.println("Categories: " + CollectionsKt.joinToString$default(this.activeCategories, ", ", (CharSequence) null, (CharSequence) null, 0, (CharSequence) null, (Function1) null, 62, (Object) null));
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }
}
