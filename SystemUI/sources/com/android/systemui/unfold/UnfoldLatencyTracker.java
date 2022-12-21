package com.android.systemui.unfold;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.keyguard.ScreenLifecycle;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001:\u0001\u0018B1\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0006\u0010\u0014\u001a\u00020\u0015J\u0010\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u0010H\u0002J\b\u0010\u0017\u001a\u00020\u0015H\u0016R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\r\u001a\u00060\u000eR\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0004\n\u0002\u0010\u0011R\u0014\u0010\u0012\u001a\u00020\u00108BX\u0004¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldLatencyTracker;", "Lcom/android/systemui/keyguard/ScreenLifecycle$Observer;", "latencyTracker", "Lcom/android/internal/util/LatencyTracker;", "deviceStateManager", "Landroid/hardware/devicestate/DeviceStateManager;", "uiBgExecutor", "Ljava/util/concurrent/Executor;", "context", "Landroid/content/Context;", "screenLifecycle", "Lcom/android/systemui/keyguard/ScreenLifecycle;", "(Lcom/android/internal/util/LatencyTracker;Landroid/hardware/devicestate/DeviceStateManager;Ljava/util/concurrent/Executor;Landroid/content/Context;Lcom/android/systemui/keyguard/ScreenLifecycle;)V", "foldStateListener", "Lcom/android/systemui/unfold/UnfoldLatencyTracker$FoldStateListener;", "folded", "", "Ljava/lang/Boolean;", "isFoldable", "()Z", "init", "", "onFoldEvent", "onScreenTurnedOn", "FoldStateListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UnfoldLatencyTracker.kt */
public final class UnfoldLatencyTracker implements ScreenLifecycle.Observer {
    private final Context context;
    private final DeviceStateManager deviceStateManager;
    private final FoldStateListener foldStateListener;
    private Boolean folded;
    private final LatencyTracker latencyTracker;
    private final ScreenLifecycle screenLifecycle;
    private final Executor uiBgExecutor;

    @Inject
    public UnfoldLatencyTracker(LatencyTracker latencyTracker2, DeviceStateManager deviceStateManager2, @UiBackground Executor executor, Context context2, ScreenLifecycle screenLifecycle2) {
        Intrinsics.checkNotNullParameter(latencyTracker2, "latencyTracker");
        Intrinsics.checkNotNullParameter(deviceStateManager2, "deviceStateManager");
        Intrinsics.checkNotNullParameter(executor, "uiBgExecutor");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(screenLifecycle2, "screenLifecycle");
        this.latencyTracker = latencyTracker2;
        this.deviceStateManager = deviceStateManager2;
        this.uiBgExecutor = executor;
        this.context = context2;
        this.screenLifecycle = screenLifecycle2;
        this.foldStateListener = new FoldStateListener(this, context2);
    }

    private final boolean isFoldable() {
        int[] intArray = this.context.getResources().getIntArray(17236068);
        Intrinsics.checkNotNullExpressionValue(intArray, "context\n                …onfig_foldedDeviceStates)");
        return !(intArray.length == 0);
    }

    public final void init() {
        if (isFoldable()) {
            this.deviceStateManager.registerCallback(this.uiBgExecutor, this.foldStateListener);
            this.screenLifecycle.addObserver(this);
        }
    }

    public void onScreenTurnedOn() {
        if (Intrinsics.areEqual((Object) this.folded, (Object) false)) {
            this.latencyTracker.onActionEnd(13);
        }
    }

    /* access modifiers changed from: private */
    public final void onFoldEvent(boolean z) {
        if (!Intrinsics.areEqual((Object) this.folded, (Object) Boolean.valueOf(z))) {
            this.folded = Boolean.valueOf(z);
            if (!z) {
                this.latencyTracker.onActionStart(13);
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldLatencyTracker$FoldStateListener;", "Landroid/hardware/devicestate/DeviceStateManager$FoldStateListener;", "context", "Landroid/content/Context;", "(Lcom/android/systemui/unfold/UnfoldLatencyTracker;Landroid/content/Context;)V", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: UnfoldLatencyTracker.kt */
    private final class FoldStateListener extends DeviceStateManager.FoldStateListener {
        final /* synthetic */ UnfoldLatencyTracker this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public FoldStateListener(UnfoldLatencyTracker unfoldLatencyTracker, Context context) {
            super(context, new UnfoldLatencyTracker$FoldStateListener$$ExternalSyntheticLambda0(unfoldLatencyTracker));
            Intrinsics.checkNotNullParameter(context, "context");
            this.this$0 = unfoldLatencyTracker;
        }

        /* access modifiers changed from: private */
        /* renamed from: _init_$lambda-0  reason: not valid java name */
        public static final void m3268_init_$lambda0(UnfoldLatencyTracker unfoldLatencyTracker, Boolean bool) {
            Intrinsics.checkNotNullParameter(unfoldLatencyTracker, "this$0");
            Intrinsics.checkNotNullExpressionValue(bool, "it");
            unfoldLatencyTracker.onFoldEvent(bool.booleanValue());
        }
    }
}
