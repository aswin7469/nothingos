package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UserBroadcastDispatcher.kt */
/* synthetic */ class UserBroadcastDispatcher$createActionReceiver$3 extends FunctionReferenceImpl implements Function2<BroadcastReceiver, Integer, Boolean> {
    UserBroadcastDispatcher$createActionReceiver$3(Object obj) {
        super(2, obj, PendingRemovalStore.class, "isPendingRemoval", "isPendingRemoval(Landroid/content/BroadcastReceiver;I)Z", 0);
    }

    public final Boolean invoke(BroadcastReceiver broadcastReceiver, int i) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "p0");
        return Boolean.valueOf(((PendingRemovalStore) this.receiver).isPendingRemoval(broadcastReceiver, i));
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return invoke((BroadcastReceiver) obj, ((Number) obj2).intValue());
    }
}
