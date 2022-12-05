package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.Log;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: UserBroadcastDispatcher.kt */
/* loaded from: classes.dex */
public final class UserBroadcastDispatcher$createActionReceiver$2 extends Lambda implements Function1<BroadcastReceiver, Unit> {
    final /* synthetic */ String $action;
    final /* synthetic */ UserBroadcastDispatcher this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UserBroadcastDispatcher$createActionReceiver$2(UserBroadcastDispatcher userBroadcastDispatcher, String str) {
        super(1);
        this.this$0 = userBroadcastDispatcher;
        this.$action = str;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(BroadcastReceiver broadcastReceiver) {
        invoke2(broadcastReceiver);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(@NotNull BroadcastReceiver $receiver) {
        int i;
        Context context;
        BroadcastDispatcherLogger broadcastDispatcherLogger;
        int i2;
        Intrinsics.checkNotNullParameter($receiver, "$this$$receiver");
        try {
            context = this.this$0.context;
            context.unregisterReceiver($receiver);
            broadcastDispatcherLogger = this.this$0.logger;
            i2 = this.this$0.userId;
            broadcastDispatcherLogger.logContextReceiverUnregistered(i2, this.$action);
        } catch (IllegalArgumentException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Trying to unregister unregistered receiver for user ");
            i = this.this$0.userId;
            sb.append(i);
            sb.append(", action ");
            sb.append(this.$action);
            Log.e("UserBroadcastDispatcher", sb.toString(), new IllegalStateException(e));
        }
    }
}
