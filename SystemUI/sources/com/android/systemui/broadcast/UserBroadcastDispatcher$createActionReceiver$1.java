package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.UserHandle;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: UserBroadcastDispatcher.kt */
/* loaded from: classes.dex */
public final class UserBroadcastDispatcher$createActionReceiver$1 extends Lambda implements Function2<BroadcastReceiver, IntentFilter, Unit> {
    final /* synthetic */ UserBroadcastDispatcher this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public UserBroadcastDispatcher$createActionReceiver$1(UserBroadcastDispatcher userBroadcastDispatcher) {
        super(2);
        this.this$0 = userBroadcastDispatcher;
    }

    @Override // kotlin.jvm.functions.Function2
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1950invoke(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        invoke2(broadcastReceiver, intentFilter);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(@NotNull BroadcastReceiver $receiver, @NotNull IntentFilter it) {
        Context context;
        int i;
        UserBroadcastDispatcher$bgHandler$1 userBroadcastDispatcher$bgHandler$1;
        BroadcastDispatcherLogger broadcastDispatcherLogger;
        int i2;
        Intrinsics.checkNotNullParameter($receiver, "$this$$receiver");
        Intrinsics.checkNotNullParameter(it, "it");
        context = this.this$0.context;
        i = this.this$0.userId;
        UserHandle of = UserHandle.of(i);
        userBroadcastDispatcher$bgHandler$1 = this.this$0.bgHandler;
        context.registerReceiverAsUser($receiver, of, it, null, userBroadcastDispatcher$bgHandler$1);
        broadcastDispatcherLogger = this.this$0.logger;
        i2 = this.this$0.userId;
        broadcastDispatcherLogger.logContextReceiverRegistered(i2, it);
    }
}
