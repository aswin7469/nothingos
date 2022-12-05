package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ActionReceiver.kt */
/* loaded from: classes.dex */
final class ActionReceiver$removeReceiver$1 extends Lambda implements Function1<ReceiverData, Boolean> {
    final /* synthetic */ BroadcastReceiver $receiver;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ActionReceiver$removeReceiver$1(BroadcastReceiver broadcastReceiver) {
        super(1);
        this.$receiver = broadcastReceiver;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1949invoke(ReceiverData receiverData) {
        return Boolean.valueOf(invoke2(receiverData));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final boolean invoke2(ReceiverData receiverData) {
        return Intrinsics.areEqual(receiverData.getReceiver(), this.$receiver);
    }
}
