package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.SparseArray;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: BroadcastDispatcher.kt */
/* loaded from: classes.dex */
public class BroadcastDispatcher implements Dumpable {
    @NotNull
    private final Executor bgExecutor;
    @NotNull
    private final Looper bgLooper;
    @NotNull
    private final Context context;
    @NotNull
    private final DumpManager dumpManager;
    @NotNull
    private final BroadcastDispatcher$handler$1 handler;
    @NotNull
    private final BroadcastDispatcherLogger logger;
    @NotNull
    private final SparseArray<UserBroadcastDispatcher> receiversByUser = new SparseArray<>(20);
    @NotNull
    private final UserTracker userTracker;

    public final void registerReceiver(@NotNull BroadcastReceiver receiver, @NotNull IntentFilter filter) {
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        Intrinsics.checkNotNullParameter(filter, "filter");
        registerReceiver$default(this, receiver, filter, null, null, 12, null);
    }

    public final void registerReceiver(@NotNull BroadcastReceiver receiver, @NotNull IntentFilter filter, @Nullable Executor executor) {
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        Intrinsics.checkNotNullParameter(filter, "filter");
        registerReceiver$default(this, receiver, filter, executor, null, 8, null);
    }

    public final void registerReceiverWithHandler(@NotNull BroadcastReceiver receiver, @NotNull IntentFilter filter, @NotNull Handler handler) {
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        Intrinsics.checkNotNullParameter(filter, "filter");
        Intrinsics.checkNotNullParameter(handler, "handler");
        registerReceiverWithHandler$default(this, receiver, filter, handler, null, 8, null);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.broadcast.BroadcastDispatcher$handler$1] */
    public BroadcastDispatcher(@NotNull Context context, @NotNull final Looper bgLooper, @NotNull Executor bgExecutor, @NotNull DumpManager dumpManager, @NotNull BroadcastDispatcherLogger logger, @NotNull UserTracker userTracker) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bgLooper, "bgLooper");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(logger, "logger");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        this.context = context;
        this.bgLooper = bgLooper;
        this.bgExecutor = bgExecutor;
        this.dumpManager = dumpManager;
        this.logger = logger;
        this.userTracker = userTracker;
        this.handler = new Handler(bgLooper) { // from class: com.android.systemui.broadcast.BroadcastDispatcher$handler$1
            @Override // android.os.Handler
            public void handleMessage(@NotNull Message msg) {
                int identifier;
                SparseArray sparseArray;
                SparseArray sparseArray2;
                UserTracker userTracker2;
                SparseArray sparseArray3;
                SparseArray sparseArray4;
                SparseArray sparseArray5;
                Intrinsics.checkNotNullParameter(msg, "msg");
                int i = msg.what;
                if (i == 0) {
                    Object obj = msg.obj;
                    Objects.requireNonNull(obj, "null cannot be cast to non-null type com.android.systemui.broadcast.ReceiverData");
                    ReceiverData receiverData = (ReceiverData) obj;
                    if (receiverData.getUser().getIdentifier() == -2) {
                        userTracker2 = BroadcastDispatcher.this.userTracker;
                        identifier = userTracker2.getUserId();
                    } else {
                        identifier = receiverData.getUser().getIdentifier();
                    }
                    if (identifier >= -1) {
                        sparseArray = BroadcastDispatcher.this.receiversByUser;
                        UserBroadcastDispatcher userBroadcastDispatcher = (UserBroadcastDispatcher) sparseArray.get(identifier, BroadcastDispatcher.this.createUBRForUser(identifier));
                        sparseArray2 = BroadcastDispatcher.this.receiversByUser;
                        sparseArray2.put(identifier, userBroadcastDispatcher);
                        userBroadcastDispatcher.registerReceiver(receiverData);
                        return;
                    }
                    throw new IllegalStateException("Attempting to register receiver for invalid user {" + identifier + '}');
                } else if (i != 1) {
                    if (i == 2) {
                        sparseArray5 = BroadcastDispatcher.this.receiversByUser;
                        UserBroadcastDispatcher userBroadcastDispatcher2 = (UserBroadcastDispatcher) sparseArray5.get(msg.arg1);
                        if (userBroadcastDispatcher2 == null) {
                            return;
                        }
                        Object obj2 = msg.obj;
                        Objects.requireNonNull(obj2, "null cannot be cast to non-null type android.content.BroadcastReceiver");
                        userBroadcastDispatcher2.unregisterReceiver((BroadcastReceiver) obj2);
                        return;
                    }
                    super.handleMessage(msg);
                } else {
                    int i2 = 0;
                    sparseArray3 = BroadcastDispatcher.this.receiversByUser;
                    int size = sparseArray3.size();
                    if (size <= 0) {
                        return;
                    }
                    while (true) {
                        int i3 = i2 + 1;
                        sparseArray4 = BroadcastDispatcher.this.receiversByUser;
                        Object obj3 = msg.obj;
                        Objects.requireNonNull(obj3, "null cannot be cast to non-null type android.content.BroadcastReceiver");
                        ((UserBroadcastDispatcher) sparseArray4.valueAt(i2)).unregisterReceiver((BroadcastReceiver) obj3);
                        if (i3 >= size) {
                            return;
                        }
                        i2 = i3;
                    }
                }
            }
        };
    }

    public final void initialize() {
        DumpManager dumpManager = this.dumpManager;
        String name = BroadcastDispatcher.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
    }

    public static /* synthetic */ void registerReceiverWithHandler$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler, UserHandle userHandle, int i, Object obj) {
        if (obj == null) {
            if ((i & 8) != 0) {
                userHandle = broadcastDispatcher.context.getUser();
                Intrinsics.checkNotNullExpressionValue(userHandle, "fun registerReceiverWithHandler(\n        receiver: BroadcastReceiver,\n        filter: IntentFilter,\n        handler: Handler,\n        user: UserHandle = context.user\n    ) {\n        registerReceiver(receiver, filter, HandlerExecutor(handler), user)\n    }");
            }
            broadcastDispatcher.registerReceiverWithHandler(broadcastReceiver, intentFilter, handler, userHandle);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: registerReceiverWithHandler");
    }

    public void registerReceiverWithHandler(@NotNull BroadcastReceiver receiver, @NotNull IntentFilter filter, @NotNull Handler handler, @NotNull UserHandle user) {
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        Intrinsics.checkNotNullParameter(filter, "filter");
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(user, "user");
        registerReceiver(receiver, filter, new HandlerExecutor(handler), user);
    }

    public static /* synthetic */ void registerReceiver$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle, int i, Object obj) {
        if (obj == null) {
            if ((i & 4) != 0) {
                executor = null;
            }
            if ((i & 8) != 0) {
                userHandle = null;
            }
            broadcastDispatcher.registerReceiver(broadcastReceiver, intentFilter, executor, userHandle);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: registerReceiver");
    }

    public void registerReceiver(@NotNull BroadcastReceiver receiver, @NotNull IntentFilter filter, @Nullable Executor executor, @Nullable UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        Intrinsics.checkNotNullParameter(filter, "filter");
        checkFilter(filter);
        BroadcastDispatcher$handler$1 broadcastDispatcher$handler$1 = this.handler;
        if (executor == null) {
            executor = this.context.getMainExecutor();
        }
        Intrinsics.checkNotNullExpressionValue(executor, "executor ?: context.mainExecutor");
        if (userHandle == null) {
            userHandle = this.context.getUser();
        }
        Intrinsics.checkNotNullExpressionValue(userHandle, "user ?: context.user");
        broadcastDispatcher$handler$1.obtainMessage(0, new ReceiverData(receiver, filter, executor, userHandle)).sendToTarget();
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
        if (TextUtils.isEmpty(sb)) {
            return;
        }
        throw new IllegalArgumentException(sb.toString());
    }

    public void unregisterReceiver(@NotNull BroadcastReceiver receiver) {
        Intrinsics.checkNotNullParameter(receiver, "receiver");
        obtainMessage(1, receiver).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @VisibleForTesting
    @NotNull
    public UserBroadcastDispatcher createUBRForUser(int i) {
        return new UserBroadcastDispatcher(this.context, i, this.bgLooper, this.bgExecutor, this.logger);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        pw.println("Broadcast dispatcher:");
        PrintWriter indentingPrintWriter = new IndentingPrintWriter(pw, "  ");
        indentingPrintWriter.increaseIndent();
        int size = this.receiversByUser.size();
        if (size > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                indentingPrintWriter.println(Intrinsics.stringPlus("User ", Integer.valueOf(this.receiversByUser.keyAt(i))));
                this.receiversByUser.valueAt(i).dump(fd, indentingPrintWriter, args);
                if (i2 >= size) {
                    break;
                }
                i = i2;
            }
        }
        indentingPrintWriter.decreaseIndent();
    }
}
