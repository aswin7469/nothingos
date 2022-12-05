package com.android.systemui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: RingerModeTrackerImpl.kt */
/* loaded from: classes2.dex */
public final class RingerModeLiveData extends MutableLiveData<Integer> {
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final Executor executor;
    @NotNull
    private final IntentFilter filter;
    @NotNull
    private final Function0<Integer> getter;
    private boolean initialSticky;
    @NotNull
    private final RingerModeLiveData$receiver$1 receiver = new BroadcastReceiver() { // from class: com.android.systemui.util.RingerModeLiveData$receiver$1
        @Override // android.content.BroadcastReceiver
        public void onReceive(@NotNull Context context, @NotNull Intent intent) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(intent, "intent");
            RingerModeLiveData.this.initialSticky = isInitialStickyBroadcast();
            RingerModeLiveData.this.postValue(Integer.valueOf(intent.getIntExtra("android.media.EXTRA_RINGER_MODE", -1)));
        }
    };

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.util.RingerModeLiveData$receiver$1] */
    public RingerModeLiveData(@NotNull BroadcastDispatcher broadcastDispatcher, @NotNull Executor executor, @NotNull String intent, @NotNull Function0<Integer> getter) {
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(intent, "intent");
        Intrinsics.checkNotNullParameter(getter, "getter");
        this.broadcastDispatcher = broadcastDispatcher;
        this.executor = executor;
        this.getter = getter;
        this.filter = new IntentFilter(intent);
    }

    public final boolean getInitialSticky() {
        return this.initialSticky;
    }

    @Override // androidx.lifecycle.LiveData
    @NotNull
    /* renamed from: getValue */
    public Integer mo1438getValue() {
        Integer num = (Integer) super.mo1438getValue();
        return Integer.valueOf(num == null ? -1 : num.intValue());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.LiveData
    public void onActive() {
        super.onActive();
        this.broadcastDispatcher.registerReceiver(this.receiver, this.filter, this.executor, UserHandle.ALL);
        this.executor.execute(new Runnable() { // from class: com.android.systemui.util.RingerModeLiveData$onActive$1
            @Override // java.lang.Runnable
            public final void run() {
                Function0 function0;
                RingerModeLiveData ringerModeLiveData = RingerModeLiveData.this;
                function0 = ringerModeLiveData.getter;
                ringerModeLiveData.postValue(function0.mo1951invoke());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.LiveData
    public void onInactive() {
        super.onInactive();
        this.broadcastDispatcher.unregisterReceiver(this.receiver);
    }
}
