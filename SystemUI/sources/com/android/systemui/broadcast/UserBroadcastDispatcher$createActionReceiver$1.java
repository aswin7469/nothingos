package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.UserHandle;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0002\b\u0005"}, mo64987d2 = {"<anonymous>", "", "Landroid/content/BroadcastReceiver;", "it", "Landroid/content/IntentFilter;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UserBroadcastDispatcher.kt */
final class UserBroadcastDispatcher$createActionReceiver$1 extends Lambda implements Function2<BroadcastReceiver, IntentFilter, Unit> {
    final /* synthetic */ int $flags;
    final /* synthetic */ String $permission;
    final /* synthetic */ UserBroadcastDispatcher this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    UserBroadcastDispatcher$createActionReceiver$1(UserBroadcastDispatcher userBroadcastDispatcher, String str, int i) {
        super(2);
        this.this$0 = userBroadcastDispatcher;
        this.$permission = str;
        this.$flags = i;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        invoke((BroadcastReceiver) obj, (IntentFilter) obj2);
        return Unit.INSTANCE;
    }

    public final void invoke(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "$this$$receiver");
        Intrinsics.checkNotNullParameter(intentFilter, "it");
        this.this$0.context.registerReceiverAsUser(broadcastReceiver, UserHandle.of(this.this$0.userId), intentFilter, this.$permission, this.this$0.bgHandler, this.$flags);
        this.this$0.logger.logContextReceiverRegistered(this.this$0.userId, this.$flags, intentFilter);
    }
}
