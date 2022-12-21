package com.android.systemui.unfold;

import com.android.systemui.demomode.DemoMode;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.util.time.SystemClock;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0010\u0007\n\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J\u0010\u0010\u0016\u001a\u00020\u00142\u0006\u0010\u0017\u001a\u00020\fH\u0002J\b\u0010\u0018\u001a\u00020\u0014H\u0016J\u0010\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\fH\u0016J\u0010\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0010\u0010\u001e\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0012H\u0016J\b\u0010\u001f\u001a\u00020\u0014H\u0016R\u0012\u0010\b\u001a\u0004\u0018\u00010\tX\u000e¢\u0006\u0004\n\u0002\u0010\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0018\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\n\n\u0002\u0010\u000f\u0012\u0004\b\r\u0010\u000eR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00120\u0011X\u0004¢\u0006\u0002\n\u0000¨\u0006 "}, mo64987d2 = {"Lcom/android/systemui/unfold/FoldStateLoggingProviderImpl;", "Lcom/android/systemui/unfold/FoldStateLoggingProvider;", "Lcom/android/systemui/unfold/updates/FoldStateProvider$FoldUpdatesListener;", "foldStateProvider", "Lcom/android/systemui/unfold/updates/FoldStateProvider;", "clock", "Lcom/android/systemui/util/time/SystemClock;", "(Lcom/android/systemui/unfold/updates/FoldStateProvider;Lcom/android/systemui/util/time/SystemClock;)V", "actionStartMillis", "", "Ljava/lang/Long;", "lastState", "", "getLastState$annotations", "()V", "Ljava/lang/Integer;", "outputListeners", "", "Lcom/android/systemui/unfold/FoldStateLoggingProvider$FoldStateLoggingListener;", "addCallback", "", "listener", "dispatchState", "current", "init", "onFoldUpdate", "update", "onHingeAngleUpdate", "angle", "", "removeCallback", "uninit", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FoldStateLoggingProviderImpl.kt */
public final class FoldStateLoggingProviderImpl implements FoldStateLoggingProvider, FoldStateProvider.FoldUpdatesListener {
    private Long actionStartMillis;
    private final SystemClock clock;
    private final FoldStateProvider foldStateProvider;
    private Integer lastState;
    private final List<FoldStateLoggingProvider.FoldStateLoggingListener> outputListeners = new ArrayList();

    private static /* synthetic */ void getLastState$annotations() {
    }

    public void onHingeAngleUpdate(float f) {
    }

    public FoldStateLoggingProviderImpl(FoldStateProvider foldStateProvider2, SystemClock systemClock) {
        Intrinsics.checkNotNullParameter(foldStateProvider2, "foldStateProvider");
        Intrinsics.checkNotNullParameter(systemClock, DemoMode.COMMAND_CLOCK);
        this.foldStateProvider = foldStateProvider2;
        this.clock = systemClock;
    }

    public void init() {
        this.foldStateProvider.addCallback(this);
        this.foldStateProvider.start();
    }

    public void uninit() {
        this.foldStateProvider.removeCallback(this);
        this.foldStateProvider.stop();
    }

    public void onFoldUpdate(int i) {
        long elapsedRealtime = this.clock.elapsedRealtime();
        if (i == 0) {
            this.lastState = 2;
            this.actionStartMillis = Long.valueOf(elapsedRealtime);
        } else if (i == 1) {
            this.actionStartMillis = Long.valueOf(elapsedRealtime);
        } else if (i == 3) {
            dispatchState(3);
        } else if (i == 4) {
            dispatchState(1);
        } else if (i == 5) {
            dispatchState(2);
        }
    }

    private final void dispatchState(int i) {
        long elapsedRealtime = this.clock.elapsedRealtime();
        Integer num = this.lastState;
        Long l = this.actionStartMillis;
        if (!(num == null || num.intValue() == i || l == null)) {
            FoldStateChange foldStateChange = new FoldStateChange(num.intValue(), i, elapsedRealtime - l.longValue());
            for (FoldStateLoggingProvider.FoldStateLoggingListener onFoldUpdate : this.outputListeners) {
                onFoldUpdate.onFoldUpdate(foldStateChange);
            }
        }
        this.actionStartMillis = null;
        this.lastState = Integer.valueOf(i);
    }

    public void addCallback(FoldStateLoggingProvider.FoldStateLoggingListener foldStateLoggingListener) {
        Intrinsics.checkNotNullParameter(foldStateLoggingListener, "listener");
        this.outputListeners.add(foldStateLoggingListener);
    }

    public void removeCallback(FoldStateLoggingProvider.FoldStateLoggingListener foldStateLoggingListener) {
        Intrinsics.checkNotNullParameter(foldStateLoggingListener, "listener");
        this.outputListeners.remove((Object) foldStateLoggingListener);
    }
}
