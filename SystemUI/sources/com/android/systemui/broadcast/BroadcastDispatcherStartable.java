package com.android.systemui.broadcast;

import android.content.Context;
import com.android.systemui.CoreStartable;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\t\u001a\u00020\nH\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u000b"}, mo64987d2 = {"Lcom/android/systemui/broadcast/BroadcastDispatcherStartable;", "Lcom/android/systemui/CoreStartable;", "context", "Landroid/content/Context;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "(Landroid/content/Context;Lcom/android/systemui/broadcast/BroadcastDispatcher;)V", "getBroadcastDispatcher", "()Lcom/android/systemui/broadcast/BroadcastDispatcher;", "start", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: BroadcastDispatcherStartable.kt */
public final class BroadcastDispatcherStartable extends CoreStartable {
    private final BroadcastDispatcher broadcastDispatcher;

    public final BroadcastDispatcher getBroadcastDispatcher() {
        return this.broadcastDispatcher;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public BroadcastDispatcherStartable(Context context, BroadcastDispatcher broadcastDispatcher2) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        this.broadcastDispatcher = broadcastDispatcher2;
    }

    public void start() {
        this.broadcastDispatcher.initialize();
    }
}
