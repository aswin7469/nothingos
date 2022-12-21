package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, mo64987d2 = {"<anonymous>", "", "it", "Lcom/android/systemui/broadcast/ReceiverData;", "kotlin.jvm.PlatformType", "invoke", "(Lcom/android/systemui/broadcast/ReceiverData;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ActionReceiver.kt */
final class ActionReceiver$removeReceiver$1 extends Lambda implements Function1<ReceiverData, Boolean> {
    final /* synthetic */ BroadcastReceiver $receiver;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ActionReceiver$removeReceiver$1(BroadcastReceiver broadcastReceiver) {
        super(1);
        this.$receiver = broadcastReceiver;
    }

    public final Boolean invoke(ReceiverData receiverData) {
        return Boolean.valueOf(Intrinsics.areEqual((Object) receiverData.getReceiver(), (Object) this.$receiver));
    }
}
