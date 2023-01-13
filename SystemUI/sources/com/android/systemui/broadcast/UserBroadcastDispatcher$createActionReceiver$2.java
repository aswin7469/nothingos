package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.util.Log;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "", "Landroid/content/BroadcastReceiver;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UserBroadcastDispatcher.kt */
final class UserBroadcastDispatcher$createActionReceiver$2 extends Lambda implements Function1<BroadcastReceiver, Unit> {
    final /* synthetic */ String $action;
    final /* synthetic */ UserBroadcastDispatcher this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    UserBroadcastDispatcher$createActionReceiver$2(UserBroadcastDispatcher userBroadcastDispatcher, String str) {
        super(1);
        this.this$0 = userBroadcastDispatcher;
        this.$action = str;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((BroadcastReceiver) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(BroadcastReceiver broadcastReceiver) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "$this$$receiver");
        try {
            this.this$0.context.unregisterReceiver(broadcastReceiver);
            this.this$0.logger.logContextReceiverUnregistered(this.this$0.userId, this.$action);
        } catch (IllegalArgumentException e) {
            Log.e("UserBroadcastDispatcher", "Trying to unregister unregistered receiver for user " + this.this$0.userId + ", action " + this.$action, new IllegalStateException((Throwable) e));
        }
    }
}
