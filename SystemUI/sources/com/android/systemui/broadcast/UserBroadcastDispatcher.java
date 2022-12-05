package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: UserBroadcastDispatcher.kt */
/* loaded from: classes.dex */
public class UserBroadcastDispatcher implements Dumpable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final AtomicInteger index = new AtomicInteger(0);
    @NotNull
    private final Executor bgExecutor;
    @NotNull
    private final UserBroadcastDispatcher$bgHandler$1 bgHandler;
    @NotNull
    private final Looper bgLooper;
    @NotNull
    private final Context context;
    @NotNull
    private final BroadcastDispatcherLogger logger;
    private final int userId;
    @NotNull
    private final ArrayMap<String, ActionReceiver> actionsToActionsReceivers = new ArrayMap<>();
    @NotNull
    private final ArrayMap<BroadcastReceiver, Set<String>> receiverToActions = new ArrayMap<>();

    public static /* synthetic */ void getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        boolean z = pw instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) pw).increaseIndent();
        }
        for (Map.Entry<String, ActionReceiver> entry : getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core().entrySet()) {
            pw.println(Intrinsics.stringPlus(entry.getKey(), ":"));
            entry.getValue().dump(fd, pw, args);
        }
        if (z) {
            ((IndentingPrintWriter) pw).decreaseIndent();
        }
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.broadcast.UserBroadcastDispatcher$bgHandler$1] */
    public UserBroadcastDispatcher(@NotNull Context context, int i, @NotNull final Looper bgLooper, @NotNull Executor bgExecutor, @NotNull BroadcastDispatcherLogger logger) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bgLooper, "bgLooper");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(logger, "logger");
        this.context = context;
        this.userId = i;
        this.bgLooper = bgLooper;
        this.bgExecutor = bgExecutor;
        this.logger = logger;
        this.bgHandler = new Handler(bgLooper) { // from class: com.android.systemui.broadcast.UserBroadcastDispatcher$bgHandler$1
            @Override // android.os.Handler
            public void handleMessage(@NotNull Message msg) {
                Intrinsics.checkNotNullParameter(msg, "msg");
                int i2 = msg.what;
                if (i2 == 0) {
                    UserBroadcastDispatcher userBroadcastDispatcher = UserBroadcastDispatcher.this;
                    Object obj = msg.obj;
                    Objects.requireNonNull(obj, "null cannot be cast to non-null type com.android.systemui.broadcast.ReceiverData");
                    userBroadcastDispatcher.handleRegisterReceiver((ReceiverData) obj);
                } else if (i2 != 1) {
                } else {
                    UserBroadcastDispatcher userBroadcastDispatcher2 = UserBroadcastDispatcher.this;
                    Object obj2 = msg.obj;
                    Objects.requireNonNull(obj2, "null cannot be cast to non-null type android.content.BroadcastReceiver");
                    userBroadcastDispatcher2.handleUnregisterReceiver((BroadcastReceiver) obj2);
                }
            }
        };
    }

    /* compiled from: UserBroadcastDispatcher.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @NotNull
    public final ArrayMap<String, ActionReceiver> getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.actionsToActionsReceivers;
    }

    public final boolean isReceiverReferenceHeld$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull BroadcastReceiver receiver) {
        boolean z;
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        Collection<ActionReceiver> values = this.actionsToActionsReceivers.values();
        Intrinsics.checkNotNullExpressionValue(values, "actionsToActionsReceivers.values");
        if (!values.isEmpty()) {
            for (ActionReceiver actionReceiver : values) {
                if (actionReceiver.hasReceiver(receiver)) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        return z || this.receiverToActions.containsKey(receiver);
    }

    public final void registerReceiver(@NotNull ReceiverData receiverData) {
        Intrinsics.checkNotNullParameter(receiverData, "receiverData");
        obtainMessage(0, receiverData).sendToTarget();
    }

    public final void unregisterReceiver(@NotNull BroadcastReceiver receiver) {
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        obtainMessage(1, receiver).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleRegisterReceiver(ReceiverData receiverData) {
        Preconditions.checkState(getLooper().isCurrentThread(), "This method should only be called from BG thread");
        ArrayMap<BroadcastReceiver, Set<String>> arrayMap = this.receiverToActions;
        BroadcastReceiver receiver = receiverData.getReceiver();
        Set<String> set = arrayMap.get(receiver);
        if (set == null) {
            set = new ArraySet<>();
            arrayMap.put(receiver, set);
        }
        Set<String> set2 = set;
        Iterator<String> actionsIterator = receiverData.getFilter().actionsIterator();
        Sequence asSequence = actionsIterator == null ? null : SequencesKt__SequencesKt.asSequence(actionsIterator);
        if (asSequence == null) {
            asSequence = SequencesKt__SequencesKt.emptySequence();
        }
        CollectionsKt__MutableCollectionsKt.addAll(set2, asSequence);
        Iterator<String> actionsIterator2 = receiverData.getFilter().actionsIterator();
        Intrinsics.checkNotNullExpressionValue(actionsIterator2, "receiverData.filter.actionsIterator()");
        while (actionsIterator2.hasNext()) {
            String it = actionsIterator2.next();
            ArrayMap<String, ActionReceiver> actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            ActionReceiver actionReceiver = actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core.get(it);
            if (actionReceiver == null) {
                Intrinsics.checkNotNullExpressionValue(it, "it");
                actionReceiver = createActionReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core(it);
                actionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core.put(it, actionReceiver);
            }
            actionReceiver.addReceiverData(receiverData);
        }
        this.logger.logReceiverRegistered(this.userId, receiverData.getReceiver());
    }

    @NotNull
    public ActionReceiver createActionReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@NotNull String action) {
        Intrinsics.checkNotNullParameter(action, "action");
        return new ActionReceiver(action, this.userId, new UserBroadcastDispatcher$createActionReceiver$1(this), new UserBroadcastDispatcher$createActionReceiver$2(this, action), this.bgExecutor, this.logger);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void handleUnregisterReceiver(BroadcastReceiver broadcastReceiver) {
        Preconditions.checkState(getLooper().isCurrentThread(), "This method should only be called from BG thread");
        Set<String> orDefault = this.receiverToActions.getOrDefault(broadcastReceiver, new LinkedHashSet());
        Intrinsics.checkNotNullExpressionValue(orDefault, "receiverToActions.getOrDefault(receiver, mutableSetOf())");
        for (String str : orDefault) {
            ActionReceiver actionReceiver = getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core().get(str);
            if (actionReceiver != null) {
                actionReceiver.removeReceiver(broadcastReceiver);
            }
        }
        this.receiverToActions.remove(broadcastReceiver);
        this.logger.logReceiverUnregistered(this.userId, broadcastReceiver);
    }
}
