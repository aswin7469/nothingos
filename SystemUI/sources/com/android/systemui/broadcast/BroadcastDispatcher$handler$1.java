package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.core.app.NotificationCompat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, mo65043d2 = {"com/android/systemui/broadcast/BroadcastDispatcher$handler$1", "Landroid/os/Handler;", "handleMessage", "", "msg", "Landroid/os/Message;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: BroadcastDispatcher.kt */
public final class BroadcastDispatcher$handler$1 extends Handler {
    final /* synthetic */ BroadcastDispatcher this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    BroadcastDispatcher$handler$1(BroadcastDispatcher broadcastDispatcher, Looper looper) {
        super(looper);
        this.this$0 = broadcastDispatcher;
    }

    public void handleMessage(Message message) {
        int i;
        int i2;
        Intrinsics.checkNotNullParameter(message, NotificationCompat.CATEGORY_MESSAGE);
        int i3 = message.what;
        if (i3 == 0) {
            Object obj = message.obj;
            if (obj != null) {
                ReceiverData receiverData = (ReceiverData) obj;
                int i4 = message.arg1;
                if (receiverData.getUser().getIdentifier() == -2) {
                    i = this.this$0.userTracker.getUserId();
                } else {
                    i = receiverData.getUser().getIdentifier();
                }
                if (i >= -1) {
                    UserBroadcastDispatcher userBroadcastDispatcher = (UserBroadcastDispatcher) this.this$0.receiversByUser.get(i, this.this$0.createUBRForUser(i));
                    this.this$0.receiversByUser.put(i, userBroadcastDispatcher);
                    userBroadcastDispatcher.registerReceiver(receiverData, i4);
                    return;
                }
                throw new IllegalStateException("Attempting to register receiver for invalid user {" + i + '}');
            }
            throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.broadcast.ReceiverData");
        } else if (i3 == 1) {
            int size = this.this$0.receiversByUser.size();
            int i5 = 0;
            while (i5 < size) {
                UserBroadcastDispatcher userBroadcastDispatcher2 = (UserBroadcastDispatcher) this.this$0.receiversByUser.valueAt(i5);
                Object obj2 = message.obj;
                if (obj2 != null) {
                    userBroadcastDispatcher2.unregisterReceiver((BroadcastReceiver) obj2);
                    i5++;
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.content.BroadcastReceiver");
                }
            }
            PendingRemovalStore access$getRemovalPendingStore$p = this.this$0.removalPendingStore;
            Object obj3 = message.obj;
            if (obj3 != null) {
                access$getRemovalPendingStore$p.clearPendingRemoval((BroadcastReceiver) obj3, -1);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.content.BroadcastReceiver");
        } else if (i3 != 2) {
            super.handleMessage(message);
        } else {
            if (message.arg1 == -2) {
                i2 = this.this$0.userTracker.getUserId();
            } else {
                i2 = message.arg1;
            }
            UserBroadcastDispatcher userBroadcastDispatcher3 = (UserBroadcastDispatcher) this.this$0.receiversByUser.get(i2);
            if (userBroadcastDispatcher3 != null) {
                Object obj4 = message.obj;
                if (obj4 != null) {
                    userBroadcastDispatcher3.unregisterReceiver((BroadcastReceiver) obj4);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.content.BroadcastReceiver");
                }
            }
            PendingRemovalStore access$getRemovalPendingStore$p2 = this.this$0.removalPendingStore;
            Object obj5 = message.obj;
            if (obj5 != null) {
                access$getRemovalPendingStore$p2.clearPendingRemoval((BroadcastReceiver) obj5, i2);
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type android.content.BroadcastReceiver");
        }
    }
}
