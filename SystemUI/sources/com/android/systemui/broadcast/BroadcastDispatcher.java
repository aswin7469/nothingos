package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.SparseArray;
import com.android.settingslib.SliceBroadcastRelay;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import java.p026io.PrintWriter;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0012\b\u0017\u0018\u00002\u00020\u0001BC\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u0010\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0002J\u0010\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001dH\u0015J%\u0010\u001e\u001a\u00020\u00182\u0006\u0010\u001f\u001a\u00020 2\u000e\u0010!\u001a\n\u0012\u0006\b\u0001\u0012\u00020#0\"H\u0016¢\u0006\u0002\u0010$J\u0006\u0010%\u001a\u00020\u0018JF\u0010&\u001a\u00020\u00182\u0006\u0010'\u001a\u00020(2\u0006\u0010\u0019\u001a\u00020\u001a2\n\b\u0002\u0010)\u001a\u0004\u0018\u00010\u00072\n\b\u0002\u0010*\u001a\u0004\u0018\u00010+2\b\b\u0002\u0010,\u001a\u00020\u001d2\n\b\u0002\u0010-\u001a\u0004\u0018\u00010#H\u0017J@\u0010.\u001a\u00020\u00182\u0006\u0010'\u001a\u00020(2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020/2\b\b\u0002\u0010*\u001a\u00020+2\b\b\u0002\u0010,\u001a\u00020\u001d2\n\b\u0002\u0010-\u001a\u0004\u0018\u00010#H\u0017J\u0010\u00100\u001a\u00020\u00182\u0006\u0010'\u001a\u00020(H\u0016J\u0018\u00101\u001a\u00020\u00182\u0006\u0010'\u001a\u00020(2\u0006\u0010*\u001a\u00020+H\u0016R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0004\n\u0002\u0010\u0013R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000¨\u00062"}, mo64987d2 = {"Lcom/android/systemui/broadcast/BroadcastDispatcher;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "bgLooper", "Landroid/os/Looper;", "bgExecutor", "Ljava/util/concurrent/Executor;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "logger", "Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "removalPendingStore", "Lcom/android/systemui/broadcast/PendingRemovalStore;", "(Landroid/content/Context;Landroid/os/Looper;Ljava/util/concurrent/Executor;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/broadcast/logging/BroadcastDispatcherLogger;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/broadcast/PendingRemovalStore;)V", "handler", "com/android/systemui/broadcast/BroadcastDispatcher$handler$1", "Lcom/android/systemui/broadcast/BroadcastDispatcher$handler$1;", "receiversByUser", "Landroid/util/SparseArray;", "Lcom/android/systemui/broadcast/UserBroadcastDispatcher;", "checkFilter", "", "filter", "Landroid/content/IntentFilter;", "createUBRForUser", "userId", "", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "initialize", "registerReceiver", "receiver", "Landroid/content/BroadcastReceiver;", "executor", "user", "Landroid/os/UserHandle;", "flags", "permission", "registerReceiverWithHandler", "Landroid/os/Handler;", "unregisterReceiver", "unregisterReceiverForUser", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BroadcastDispatcher.kt */
public class BroadcastDispatcher implements Dumpable {
    private final Executor bgExecutor;
    private final Looper bgLooper;
    private final Context context;
    private final DumpManager dumpManager;
    private final BroadcastDispatcher$handler$1 handler;
    private final BroadcastDispatcherLogger logger;
    /* access modifiers changed from: private */
    public final SparseArray<UserBroadcastDispatcher> receiversByUser = new SparseArray<>(20);
    /* access modifiers changed from: private */
    public final PendingRemovalStore removalPendingStore;
    /* access modifiers changed from: private */
    public final UserTracker userTracker;

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        registerReceiver$default(this, broadcastReceiver, intentFilter, (Executor) null, (UserHandle) null, 0, (String) null, 60, (Object) null);
    }

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        registerReceiver$default(this, broadcastReceiver, intentFilter, executor, (UserHandle) null, 0, (String) null, 56, (Object) null);
    }

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        registerReceiver$default(this, broadcastReceiver, intentFilter, executor, userHandle, 0, (String) null, 48, (Object) null);
    }

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle, int i) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        registerReceiver$default(this, broadcastReceiver, intentFilter, executor, userHandle, i, (String) null, 32, (Object) null);
    }

    @Deprecated(message = "Replacing Handler for Executor in SystemUI", replaceWith = @ReplaceWith(expression = "registerReceiver(receiver, filter, executor, user, permission)", imports = {}))
    public final void registerReceiverWithHandler(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler2) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        Intrinsics.checkNotNullParameter(handler2, "handler");
        registerReceiverWithHandler$default(this, broadcastReceiver, intentFilter, handler2, (UserHandle) null, 0, (String) null, 56, (Object) null);
    }

    @Deprecated(message = "Replacing Handler for Executor in SystemUI", replaceWith = @ReplaceWith(expression = "registerReceiver(receiver, filter, executor, user, permission)", imports = {}))
    public final void registerReceiverWithHandler(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler2, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        registerReceiverWithHandler$default(this, broadcastReceiver, intentFilter, handler2, userHandle, 0, (String) null, 48, (Object) null);
    }

    @Deprecated(message = "Replacing Handler for Executor in SystemUI", replaceWith = @ReplaceWith(expression = "registerReceiver(receiver, filter, executor, user, permission)", imports = {}))
    public final void registerReceiverWithHandler(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler2, UserHandle userHandle, int i) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        registerReceiverWithHandler$default(this, broadcastReceiver, intentFilter, handler2, userHandle, i, (String) null, 32, (Object) null);
    }

    @Inject
    public BroadcastDispatcher(Context context2, @Background Looper looper, @Background Executor executor, DumpManager dumpManager2, BroadcastDispatcherLogger broadcastDispatcherLogger, UserTracker userTracker2, PendingRemovalStore pendingRemovalStore) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(looper, "bgLooper");
        Intrinsics.checkNotNullParameter(executor, "bgExecutor");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcherLogger, "logger");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        Intrinsics.checkNotNullParameter(pendingRemovalStore, "removalPendingStore");
        this.context = context2;
        this.bgLooper = looper;
        this.bgExecutor = executor;
        this.dumpManager = dumpManager2;
        this.logger = broadcastDispatcherLogger;
        this.userTracker = userTracker2;
        this.removalPendingStore = pendingRemovalStore;
        this.handler = new BroadcastDispatcher$handler$1(this, looper);
    }

    public final void initialize() {
        DumpManager dumpManager2 = this.dumpManager;
        String name = getClass().getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager2.registerDumpable(name, this);
    }

    public static /* synthetic */ void registerReceiverWithHandler$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler2, UserHandle userHandle, int i, String str, int i2, Object obj) {
        if (obj == null) {
            if ((i2 & 8) != 0) {
                userHandle = broadcastDispatcher.context.getUser();
                Intrinsics.checkNotNullExpressionValue(userHandle, "context.user");
            }
            UserHandle userHandle2 = userHandle;
            if ((i2 & 16) != 0) {
                i = 2;
            }
            int i3 = i;
            if ((i2 & 32) != 0) {
                str = null;
            }
            broadcastDispatcher.registerReceiverWithHandler(broadcastReceiver, intentFilter, handler2, userHandle2, i3, str);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: registerReceiverWithHandler");
    }

    @Deprecated(message = "Replacing Handler for Executor in SystemUI", replaceWith = @ReplaceWith(expression = "registerReceiver(receiver, filter, executor, user, permission)", imports = {}))
    public void registerReceiverWithHandler(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler2, UserHandle userHandle, int i, String str) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        registerReceiver(broadcastReceiver, intentFilter, new HandlerExecutor(handler2), userHandle, i, str);
    }

    public static /* synthetic */ void registerReceiver$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle, int i, String str, int i2, Object obj) {
        if (obj == null) {
            broadcastDispatcher.registerReceiver(broadcastReceiver, intentFilter, (i2 & 4) != 0 ? null : executor, (i2 & 8) != 0 ? null : userHandle, (i2 & 16) != 0 ? 2 : i, (i2 & 32) != 0 ? null : str);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: registerReceiver");
    }

    public void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle, int i, String str) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(intentFilter, SliceBroadcastRelay.EXTRA_FILTER);
        checkFilter(intentFilter);
        if (executor == null) {
            executor = this.context.getMainExecutor();
        }
        Executor executor2 = executor;
        Intrinsics.checkNotNullExpressionValue(executor2, "executor ?: context.mainExecutor");
        if (userHandle == null) {
            userHandle = this.context.getUser();
        }
        UserHandle userHandle2 = userHandle;
        Intrinsics.checkNotNullExpressionValue(userHandle2, "user ?: context.user");
        this.handler.obtainMessage(0, i, 0, new ReceiverData(broadcastReceiver, intentFilter, executor2, userHandle2, str)).sendToTarget();
    }

    private final void checkFilter(IntentFilter intentFilter) {
        StringBuilder sb = new StringBuilder();
        if (intentFilter.countActions() == 0) {
            sb.append("Filter must contain at least one action. ");
        }
        if (intentFilter.countDataAuthorities() != 0) {
            sb.append("Filter cannot contain DataAuthorities. ");
        }
        if (intentFilter.countDataPaths() != 0) {
            sb.append("Filter cannot contain DataPaths. ");
        }
        if (intentFilter.countDataSchemes() != 0) {
            sb.append("Filter cannot contain DataSchemes. ");
        }
        if (intentFilter.countDataTypes() != 0) {
            sb.append("Filter cannot contain DataTypes. ");
        }
        if (intentFilter.getPriority() != 0) {
            sb.append("Filter cannot modify priority. ");
        }
        if (!TextUtils.isEmpty(sb)) {
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        this.removalPendingStore.tagForRemoval(broadcastReceiver, -1);
        this.handler.obtainMessage(1, broadcastReceiver).sendToTarget();
    }

    public void unregisterReceiverForUser(BroadcastReceiver broadcastReceiver, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, SliceBroadcastRelay.EXTRA_RECEIVER);
        Intrinsics.checkNotNullParameter(userHandle, "user");
        this.removalPendingStore.tagForRemoval(broadcastReceiver, userHandle.getIdentifier());
        this.handler.obtainMessage(2, userHandle.getIdentifier(), 0, broadcastReceiver).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public UserBroadcastDispatcher createUBRForUser(int i) {
        return new UserBroadcastDispatcher(this.context, i, this.bgLooper, this.bgExecutor, this.logger, this.removalPendingStore);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("Broadcast dispatcher:");
        PrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        int size = this.receiversByUser.size();
        for (int i = 0; i < size; i++) {
            indentingPrintWriter.println("User " + this.receiversByUser.keyAt(i));
            this.receiversByUser.valueAt(i).dump(indentingPrintWriter, strArr);
        }
        indentingPrintWriter.println("Pending removal:");
        this.removalPendingStore.dump(indentingPrintWriter, strArr);
        indentingPrintWriter.decreaseIndent();
    }
}
