package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import com.android.settingslib.SliceBroadcastRelay;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010#\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0016\u0018\u0000 32\u00020\u0001:\u000234B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ'\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u001e\u001a\u00020\u001c2\b\u0010\u001f\u001a\u0004\u0018\u00010\u001c2\u0006\u0010 \u001a\u00020\u0005H\u0011¢\u0006\u0002\b!J%\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020%2\u000e\u0010&\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001c0'H\u0016¢\u0006\u0002\u0010(J\u0018\u0010)\u001a\u00020#2\u0006\u0010*\u001a\u00020+2\u0006\u0010 \u001a\u00020\u0005H\u0002J\u0010\u0010,\u001a\u00020#2\u0006\u0010-\u001a\u00020\u001aH\u0002J\u0015\u0010.\u001a\u00020/2\u0006\u0010-\u001a\u00020\u001aH\u0001¢\u0006\u0002\b0J\u0018\u00101\u001a\u00020#2\u0006\u0010*\u001a\u00020+2\u0006\u0010 \u001a\u00020\u0005H\u0007J\u0010\u00102\u001a\u00020#2\u0006\u0010-\u001a\u00020\u001aH\u0007R(\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u00020\u0011\u0012\u0004\u0012\u00020\u00120\u00108\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R \u0010\u0019\u001a\u0014\u0012\u0004\u0012\u00020\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u001c0\u001b0\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000¨\u00065"}, mo65043d2 = {"Lcom/android/systemui/broadcast/UserBroadcastDispatcher;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "userId", "", "bgLooper", "Landroid/os/Looper;", "bgExecutor", "Ljava/util/concurrent/Executor;", "logger", "Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;", "removalPendingStore", "Lcom/android/systemui/broadcast/PendingRemovalStore;", "(Landroid/content/Context;ILandroid/os/Looper;Ljava/util/concurrent/Executor;Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;Lcom/android/systemui/broadcast/PendingRemovalStore;)V", "actionsToActionsReceivers", "Landroid/util/ArrayMap;", "Lcom/android/systemui/broadcast/UserBroadcastDispatcher$ReceiverProperties;", "Lcom/android/systemui/broadcast/ActionReceiver;", "getActionsToActionsReceivers$SystemUI_nothingRelease$annotations", "()V", "getActionsToActionsReceivers$SystemUI_nothingRelease", "()Landroid/util/ArrayMap;", "bgHandler", "Landroid/os/Handler;", "receiverToActions", "Landroid/content/BroadcastReceiver;", "", "", "createActionReceiver", "action", "permission", "flags", "createActionReceiver$SystemUI_nothingRelease", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "handleRegisterReceiver", "receiverData", "Lcom/android/systemui/broadcast/ReceiverData;", "handleUnregisterReceiver", "receiver", "isReceiverReferenceHeld", "", "isReceiverReferenceHeld$SystemUI_nothingRelease", "registerReceiver", "unregisterReceiver", "Companion", "ReceiverProperties", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UserBroadcastDispatcher.kt */
public class UserBroadcastDispatcher implements Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final AtomicInteger index = new AtomicInteger(0);
    private final ArrayMap<ReceiverProperties, ActionReceiver> actionsToActionsReceivers = new ArrayMap<>();
    private final Executor bgExecutor;
    /* access modifiers changed from: private */
    public final Handler bgHandler;
    private final Looper bgLooper;
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public final BroadcastDispatcherLogger logger;
    private final ArrayMap<BroadcastReceiver, Set<String>> receiverToActions = new ArrayMap<>();
    private final PendingRemovalStore removalPendingStore;
    /* access modifiers changed from: private */
    public final int userId;

    public static /* synthetic */ void getActionsToActionsReceivers$SystemUI_nothingRelease$annotations() {
    }

    public UserBroadcastDispatcher(Context context2, int i, Looper looper, Executor executor, BroadcastDispatcherLogger broadcastDispatcherLogger, PendingRemovalStore pendingRemovalStore) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(looper, "bgLooper");
        Intrinsics.checkNotNullParameter(executor, "bgExecutor");
        Intrinsics.checkNotNullParameter(broadcastDispatcherLogger, "logger");
        Intrinsics.checkNotNullParameter(pendingRemovalStore, "removalPendingStore");
        this.context = context2;
        this.userId = i;
        this.bgLooper = looper;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
        this.removalPendingStore = pendingRemovalStore;
        this.bgHandler = new Handler(looper);
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/broadcast/UserBroadcastDispatcher$Companion;", "", "()V", "index", "Ljava/util/concurrent/atomic/AtomicInteger;", "getIndex", "()Ljava/util/concurrent/atomic/AtomicInteger;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UserBroadcastDispatcher.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final AtomicInteger getIndex() {
            return UserBroadcastDispatcher.index;
        }
    }

    @Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0004\b\b\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003HÆ\u0003J\t\u0010\u000e\u001a\u00020\u0005HÆ\u0003J\u000b\u0010\u000f\u001a\u0004\u0018\u00010\u0003HÆ\u0003J)\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0005HÖ\u0001J\t\u0010\u0015\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/broadcast/UserBroadcastDispatcher$ReceiverProperties;", "", "action", "", "flags", "", "permission", "(Ljava/lang/String;ILjava/lang/String;)V", "getAction", "()Ljava/lang/String;", "getFlags", "()I", "getPermission", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "toString", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UserBroadcastDispatcher.kt */
    public static final class ReceiverProperties {
        private final String action;
        private final int flags;
        private final String permission;

        public static /* synthetic */ ReceiverProperties copy$default(ReceiverProperties receiverProperties, String str, int i, String str2, int i2, Object obj) {
            if ((i2 & 1) != 0) {
                str = receiverProperties.action;
            }
            if ((i2 & 2) != 0) {
                i = receiverProperties.flags;
            }
            if ((i2 & 4) != 0) {
                str2 = receiverProperties.permission;
            }
            return receiverProperties.copy(str, i, str2);
        }

        public final String component1() {
            return this.action;
        }

        public final int component2() {
            return this.flags;
        }

        public final String component3() {
            return this.permission;
        }

        public final ReceiverProperties copy(String str, int i, String str2) {
            Intrinsics.checkNotNullParameter(str, "action");
            return new ReceiverProperties(str, i, str2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ReceiverProperties)) {
                return false;
            }
            ReceiverProperties receiverProperties = (ReceiverProperties) obj;
            return Intrinsics.areEqual((Object) this.action, (Object) receiverProperties.action) && this.flags == receiverProperties.flags && Intrinsics.areEqual((Object) this.permission, (Object) receiverProperties.permission);
        }

        public int hashCode() {
            int hashCode = ((this.action.hashCode() * 31) + Integer.hashCode(this.flags)) * 31;
            String str = this.permission;
            return hashCode + (str == null ? 0 : str.hashCode());
        }

        public String toString() {
            return "ReceiverProperties(action=" + this.action + ", flags=" + this.flags + ", permission=" + this.permission + ')';
        }

        public ReceiverProperties(String str, int i, String str2) {
            Intrinsics.checkNotNullParameter(str, "action");
            this.action = str;
            this.flags = i;
            this.permission = str2;
        }

        public final String getAction() {
            return this.action;
        }

        public final int getFlags() {
            return this.flags;
        }

        public final String getPermission() {
            return this.permission;
        }
    }

    public final ArrayMap<ReceiverProperties, ActionReceiver> getActionsToActionsReceivers$SystemUI_nothingRelease() {
        return this.actionsToActionsReceivers;
    }

    public final boolean isReceiverReferenceHeld$SystemUI_nothingRelease(BroadcastReceiver broadcastReceiver) {
        boolean z;
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Collection<ActionReceiver> values = this.actionsToActionsReceivers.values();
        Intrinsics.checkNotNullExpressionValue(values, "actionsToActionsReceivers.values");
        Iterable iterable = values;
        if (!((Collection) iterable).isEmpty()) {
            Iterator it = iterable.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((ActionReceiver) it.next()).hasReceiver(broadcastReceiver)) {
                        z = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        z = false;
        if (z || this.receiverToActions.containsKey(broadcastReceiver)) {
            return true;
        }
        return false;
    }

    public final void registerReceiver(ReceiverData receiverData, int i) {
        Intrinsics.checkNotNullParameter(receiverData, "receiverData");
        handleRegisterReceiver(receiverData, i);
    }

    public final void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        handleUnregisterReceiver(broadcastReceiver);
    }

    private final void handleRegisterReceiver(ReceiverData receiverData, int i) {
        Sequence<T> sequence;
        Preconditions.checkState(this.bgLooper.isCurrentThread(), "This method should only be called from BG thread");
        Map map = this.receiverToActions;
        BroadcastReceiver receiver = receiverData.getReceiver();
        Object obj = map.get(receiver);
        if (obj == null) {
            obj = new ArraySet();
            map.put(receiver, obj);
        }
        Collection collection = (Collection) obj;
        Iterator<String> actionsIterator = receiverData.getFilter().actionsIterator();
        if (actionsIterator == null || (sequence = SequencesKt.asSequence(actionsIterator)) == null) {
            sequence = SequencesKt.emptySequence();
        }
        CollectionsKt.addAll(collection, sequence);
        Iterator<String> actionsIterator2 = receiverData.getFilter().actionsIterator();
        Intrinsics.checkNotNullExpressionValue(actionsIterator2, "receiverData.filter.actionsIterator()");
        while (actionsIterator2.hasNext()) {
            String next = actionsIterator2.next();
            Map map2 = this.actionsToActionsReceivers;
            Intrinsics.checkNotNullExpressionValue(next, "it");
            ReceiverProperties receiverProperties = new ReceiverProperties(next, i, receiverData.getPermission());
            Object obj2 = map2.get(receiverProperties);
            if (obj2 == null) {
                obj2 = createActionReceiver$SystemUI_nothingRelease(next, receiverData.getPermission(), i);
                map2.put(receiverProperties, obj2);
            }
            ((ActionReceiver) obj2).addReceiverData(receiverData);
        }
        this.logger.logReceiverRegistered(this.userId, receiverData.getReceiver(), i);
    }

    public ActionReceiver createActionReceiver$SystemUI_nothingRelease(String str, String str2, int i) {
        Intrinsics.checkNotNullParameter(str, "action");
        return new ActionReceiver(str, this.userId, new UserBroadcastDispatcher$createActionReceiver$1(this, str2, i), new UserBroadcastDispatcher$createActionReceiver$2(this, str), this.bgExecutor, this.logger, new UserBroadcastDispatcher$createActionReceiver$3(this.removalPendingStore));
    }

    private final void handleUnregisterReceiver(BroadcastReceiver broadcastReceiver) {
        Preconditions.checkState(this.bgLooper.isCurrentThread(), "This method should only be called from BG thread");
        Object orDefault = this.receiverToActions.getOrDefault(broadcastReceiver, new LinkedHashSet());
        Intrinsics.checkNotNullExpressionValue(orDefault, "receiverToActions.getOrD…receiver, mutableSetOf())");
        for (String str : (Iterable) orDefault) {
            for (Map.Entry entry : this.actionsToActionsReceivers.entrySet()) {
                ActionReceiver actionReceiver = (ActionReceiver) entry.getValue();
                if (Intrinsics.areEqual((Object) ((ReceiverProperties) entry.getKey()).getAction(), (Object) str)) {
                    actionReceiver.removeReceiver(broadcastReceiver);
                }
            }
        }
        this.receiverToActions.remove(broadcastReceiver);
        this.logger.logReceiverUnregistered(this.userId, broadcastReceiver);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        boolean z = printWriter instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        for (Map.Entry entry : this.actionsToActionsReceivers.entrySet()) {
            ReceiverProperties receiverProperties = (ReceiverProperties) entry.getKey();
            ActionReceiver actionReceiver = (ActionReceiver) entry.getValue();
            StringBuilder append = new StringBuilder(NavigationBarInflaterView.KEY_CODE_START).append(receiverProperties.getAction()).append(": ").append(BroadcastDispatcherLogger.Companion.flagToString(receiverProperties.getFlags()));
            String str = "):";
            if (receiverProperties.getPermission() != null) {
                str = ":" + receiverProperties.getPermission() + str;
            }
            printWriter.println(append.append(str).toString());
            actionReceiver.dump(printWriter, strArr);
        }
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }
}
