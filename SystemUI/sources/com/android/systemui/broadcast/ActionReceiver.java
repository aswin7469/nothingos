package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: ActionReceiver.kt */
/* loaded from: classes.dex */
public final class ActionReceiver extends BroadcastReceiver implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final AtomicInteger index = new AtomicInteger(0);
    @NotNull
    private final String action;
    @NotNull
    private final Executor bgExecutor;
    @NotNull
    private final BroadcastDispatcherLogger logger;
    @NotNull
    private final Function2<BroadcastReceiver, IntentFilter, Unit> registerAction;
    private boolean registered;
    @NotNull
    private final Function1<BroadcastReceiver, Unit> unregisterAction;
    private final int userId;
    @NotNull
    private final ArraySet<ReceiverData> receiverDatas = new ArraySet<>();
    @NotNull
    private final ArraySet<String> activeCategories = new ArraySet<>();

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        String joinToString$default;
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        if (pw instanceof IndentingPrintWriter) {
            ((IndentingPrintWriter) pw).increaseIndent();
        }
        pw.println(Intrinsics.stringPlus("Registered: ", Boolean.valueOf(getRegistered())));
        pw.println("Receivers:");
        boolean z = pw instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) pw).increaseIndent();
        }
        for (ReceiverData receiverData : this.receiverDatas) {
            pw.println(receiverData.getReceiver());
        }
        if (z) {
            ((IndentingPrintWriter) pw).decreaseIndent();
        }
        joinToString$default = CollectionsKt___CollectionsKt.joinToString$default(this.activeCategories, ", ", null, null, 0, null, null, 62, null);
        pw.println(Intrinsics.stringPlus("Categories: ", joinToString$default));
        if (z) {
            ((IndentingPrintWriter) pw).decreaseIndent();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ActionReceiver(@NotNull String action, int i, @NotNull Function2<? super BroadcastReceiver, ? super IntentFilter, Unit> registerAction, @NotNull Function1<? super BroadcastReceiver, Unit> unregisterAction, @NotNull Executor bgExecutor, @NotNull BroadcastDispatcherLogger logger) {
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(registerAction, "registerAction");
        Intrinsics.checkNotNullParameter(unregisterAction, "unregisterAction");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(logger, "logger");
        this.action = action;
        this.userId = i;
        this.registerAction = registerAction;
        this.unregisterAction = unregisterAction;
        this.bgExecutor = bgExecutor;
        this.logger = logger;
    }

    /* compiled from: ActionReceiver.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final AtomicInteger getIndex() {
            return ActionReceiver.index;
        }
    }

    public final boolean getRegistered() {
        return this.registered;
    }

    public final void addReceiverData(@NotNull ReceiverData receiverData) throws IllegalArgumentException {
        boolean addAll;
        Intrinsics.checkNotNullParameter(receiverData, "receiverData");
        if (!receiverData.getFilter().hasAction(this.action)) {
            throw new IllegalArgumentException("Trying to attach to " + this.action + " without correct action,receiver: " + receiverData.getReceiver());
        }
        ArraySet<String> arraySet = this.activeCategories;
        Iterator<String> categoriesIterator = receiverData.getFilter().categoriesIterator();
        Sequence asSequence = categoriesIterator == null ? null : SequencesKt__SequencesKt.asSequence(categoriesIterator);
        if (asSequence == null) {
            asSequence = SequencesKt__SequencesKt.emptySequence();
        }
        addAll = CollectionsKt__MutableCollectionsKt.addAll(arraySet, asSequence);
        if (this.receiverDatas.add(receiverData) && this.receiverDatas.size() == 1) {
            this.registerAction.mo1950invoke(this, createFilter());
            this.registered = true;
        } else if (!addAll) {
        } else {
            this.unregisterAction.mo1949invoke(this);
            this.registerAction.mo1950invoke(this, createFilter());
        }
    }

    public final boolean hasReceiver(@NotNull BroadcastReceiver receiver) {
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        ArraySet<ReceiverData> arraySet = this.receiverDatas;
        if (!(arraySet instanceof Collection) || !arraySet.isEmpty()) {
            for (ReceiverData receiverData : arraySet) {
                if (Intrinsics.areEqual(receiverData.getReceiver(), receiver)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private final IntentFilter createFilter() {
        IntentFilter intentFilter = new IntentFilter(this.action);
        for (String str : this.activeCategories) {
            intentFilter.addCategory(str);
        }
        return intentFilter;
    }

    public final void removeReceiver(@NotNull BroadcastReceiver receiver) {
        boolean removeAll;
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        removeAll = CollectionsKt__MutableCollectionsKt.removeAll(this.receiverDatas, new ActionReceiver$removeReceiver$1(receiver));
        if (!removeAll || !this.receiverDatas.isEmpty() || !this.registered) {
            return;
        }
        this.unregisterAction.mo1949invoke(this);
        this.registered = false;
        this.activeCategories.clear();
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(@NotNull final Context context, @NotNull final Intent intent) throws IllegalStateException {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (!Intrinsics.areEqual(intent.getAction(), this.action)) {
            throw new IllegalStateException("Received intent for " + ((Object) intent.getAction()) + " in receiver for " + this.action + '}');
        }
        final int andIncrement = Companion.getIndex().getAndIncrement();
        this.logger.logBroadcastReceived(andIncrement, this.userId, intent);
        this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.broadcast.ActionReceiver$onReceive$1
            @Override // java.lang.Runnable
            public final void run() {
                ArraySet<ReceiverData> arraySet;
                arraySet = ActionReceiver.this.receiverDatas;
                final Intent intent2 = intent;
                final ActionReceiver actionReceiver = ActionReceiver.this;
                final Context context2 = context;
                final int i = andIncrement;
                for (final ReceiverData receiverData : arraySet) {
                    if (receiverData.getFilter().matchCategories(intent2.getCategories()) == null) {
                        receiverData.getExecutor().execute(new Runnable() { // from class: com.android.systemui.broadcast.ActionReceiver$onReceive$1$1$1
                            @Override // java.lang.Runnable
                            public final void run() {
                                BroadcastDispatcherLogger broadcastDispatcherLogger;
                                String str;
                                ReceiverData.this.getReceiver().setPendingResult(actionReceiver.getPendingResult());
                                ReceiverData.this.getReceiver().onReceive(context2, intent2);
                                broadcastDispatcherLogger = actionReceiver.logger;
                                int i2 = i;
                                str = actionReceiver.action;
                                broadcastDispatcherLogger.logBroadcastDispatched(i2, str, ReceiverData.this.getReceiver());
                            }
                        });
                    }
                }
            }
        });
    }
}
