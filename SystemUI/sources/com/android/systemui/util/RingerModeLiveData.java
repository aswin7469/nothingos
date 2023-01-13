package com.android.systemui.util;

import android.content.IntentFilter;
import android.os.UserHandle;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000E\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002*\u0001\u0014\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B+\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\n¢\u0006\u0002\u0010\u000bJ\r\u0010\u0016\u001a\u00020\u0002H\u0016¢\u0006\u0002\u0010\u0017J\b\u0010\u0018\u001a\u00020\u0019H\u0014J\b\u0010\u001a\u001a\u00020\u0019H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u000e\u001a\u00020\u000f@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0010\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0004\n\u0002\u0010\u0015¨\u0006\u001b"}, mo65043d2 = {"Lcom/android/systemui/util/RingerModeLiveData;", "Landroidx/lifecycle/MutableLiveData;", "", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "executor", "Ljava/util/concurrent/Executor;", "intent", "", "getter", "Lkotlin/Function0;", "(Lcom/android/systemui/broadcast/BroadcastDispatcher;Ljava/util/concurrent/Executor;Ljava/lang/String;Lkotlin/jvm/functions/Function0;)V", "filter", "Landroid/content/IntentFilter;", "<set-?>", "", "initialSticky", "getInitialSticky", "()Z", "receiver", "com/android/systemui/util/RingerModeLiveData$receiver$1", "Lcom/android/systemui/util/RingerModeLiveData$receiver$1;", "getValue", "()Ljava/lang/Integer;", "onActive", "", "onInactive", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RingerModeTrackerImpl.kt */
public final class RingerModeLiveData extends MutableLiveData<Integer> {
    private final BroadcastDispatcher broadcastDispatcher;
    private final Executor executor;
    private final IntentFilter filter;
    private final Function0<Integer> getter;
    /* access modifiers changed from: private */
    public boolean initialSticky;
    private final RingerModeLiveData$receiver$1 receiver = new RingerModeLiveData$receiver$1(this);

    public RingerModeLiveData(BroadcastDispatcher broadcastDispatcher2, Executor executor2, String str, Function0<Integer> function0) {
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(str, "intent");
        Intrinsics.checkNotNullParameter(function0, "getter");
        this.broadcastDispatcher = broadcastDispatcher2;
        this.executor = executor2;
        this.getter = function0;
        this.filter = new IntentFilter(str);
    }

    public final boolean getInitialSticky() {
        return this.initialSticky;
    }

    public Integer getValue() {
        Integer num = (Integer) super.getValue();
        return Integer.valueOf(num == null ? -1 : num.intValue());
    }

    /* access modifiers changed from: protected */
    public void onActive() {
        super.onActive();
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, this.receiver, this.filter, this.executor, UserHandle.ALL, 0, (String) null, 48, (Object) null);
        this.executor.execute(new RingerModeLiveData$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onActive$lambda-0  reason: not valid java name */
    public static final void m3307onActive$lambda0(RingerModeLiveData ringerModeLiveData) {
        Intrinsics.checkNotNullParameter(ringerModeLiveData, "this$0");
        ringerModeLiveData.postValue(ringerModeLiveData.getter.invoke());
    }

    /* access modifiers changed from: protected */
    public void onInactive() {
        super.onInactive();
        this.broadcastDispatcher.unregisterReceiver(this.receiver);
    }
}
