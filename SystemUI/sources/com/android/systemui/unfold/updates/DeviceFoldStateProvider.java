package com.android.systemui.unfold.updates;

import android.app.ActivityManager;
import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Handler;
import androidx.core.util.Consumer;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.unfold.updates.FoldStateProvider;
import com.android.systemui.unfold.updates.hinge.HingeAngleProvider;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0011\u0018\u00002\u00020\u0001:\u000489:;BC\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r\u0012\b\b\u0001\u0010\u000e\u001a\u00020\u000f¢\u0006\u0002\u0010\u0010J\u0010\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020%H\u0016J\b\u0010-\u001a\u00020+H\u0002J\u000f\u0010.\u001a\u0004\u0018\u00010\u0014H\u0002¢\u0006\u0002\u0010/J\u0010\u00100\u001a\u00020+2\u0006\u00101\u001a\u00020\u0014H\u0002J\u0010\u00102\u001a\u00020+2\u0006\u00103\u001a\u00020\"H\u0002J\u0010\u00104\u001a\u00020+2\u0006\u0010,\u001a\u00020%H\u0016J\b\u00105\u001a\u00020+H\u0002J\b\u00106\u001a\u00020+H\u0016J\b\u00107\u001a\u00020+H\u0016R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u00060\u0012R\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\u0015\u001a\u00060\u0016R\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0017\u001a\u00020\u00188VX\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0019R\u000e\u0010\u001a\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001b\u001a\u00020\u00188BX\u0004¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0019R\u000e\u0010\u001c\u001a\u00020\u0018X\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u001d\u001a\u0004\u0018\u00010\u0014X\u000e¢\u0006\n\n\u0002\u0010 \u0012\u0004\b\u001e\u0010\u001fR\u000e\u0010!\u001a\u00020\"X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010#\u001a\b\u0012\u0004\u0012\u00020%0$X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010&\u001a\u00060'R\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010(\u001a\u00060)R\u00020\u0000X\u0004¢\u0006\u0002\n\u0000¨\u0006<"}, mo65043d2 = {"Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider;", "Lcom/android/systemui/unfold/updates/FoldStateProvider;", "context", "Landroid/content/Context;", "hingeAngleProvider", "Lcom/android/systemui/unfold/updates/hinge/HingeAngleProvider;", "screenStatusProvider", "Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider;", "deviceStateManager", "Landroid/hardware/devicestate/DeviceStateManager;", "activityManager", "Landroid/app/ActivityManager;", "mainExecutor", "Ljava/util/concurrent/Executor;", "handler", "Landroid/os/Handler;", "(Landroid/content/Context;Lcom/android/systemui/unfold/updates/hinge/HingeAngleProvider;Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider;Landroid/hardware/devicestate/DeviceStateManager;Landroid/app/ActivityManager;Ljava/util/concurrent/Executor;Landroid/os/Handler;)V", "foldStateListener", "Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider$FoldStateListener;", "halfOpenedTimeoutMillis", "", "hingeAngleListener", "Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider$HingeAngleListener;", "isFinishedOpening", "", "()Z", "isFolded", "isTransitionInProgress", "isUnfoldHandled", "lastFoldUpdate", "getLastFoldUpdate$annotations", "()V", "Ljava/lang/Integer;", "lastHingeAngle", "", "outputListeners", "", "Lcom/android/systemui/unfold/updates/FoldStateProvider$FoldUpdatesListener;", "screenListener", "Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider$ScreenStatusListener;", "timeoutRunnable", "Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider$TimeoutRunnable;", "addCallback", "", "listener", "cancelTimeout", "getClosingThreshold", "()Ljava/lang/Integer;", "notifyFoldUpdate", "update", "onHingeAngle", "angle", "removeCallback", "rescheduleAbortAnimationTimeout", "start", "stop", "FoldStateListener", "HingeAngleListener", "ScreenStatusListener", "TimeoutRunnable", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DeviceFoldStateProvider.kt */
public final class DeviceFoldStateProvider implements FoldStateProvider {
    private final ActivityManager activityManager;
    private final DeviceStateManager deviceStateManager;
    private final FoldStateListener foldStateListener;
    private final int halfOpenedTimeoutMillis;
    private final Handler handler;
    private final HingeAngleListener hingeAngleListener = new HingeAngleListener();
    /* access modifiers changed from: private */
    public final HingeAngleProvider hingeAngleProvider;
    /* access modifiers changed from: private */
    public boolean isFolded;
    /* access modifiers changed from: private */
    public boolean isUnfoldHandled;
    private Integer lastFoldUpdate;
    /* access modifiers changed from: private */
    public float lastHingeAngle;
    private final Executor mainExecutor;
    /* access modifiers changed from: private */
    public final List<FoldStateProvider.FoldUpdatesListener> outputListeners = new ArrayList();
    private final ScreenStatusListener screenListener = new ScreenStatusListener();
    private final ScreenStatusProvider screenStatusProvider;
    private final TimeoutRunnable timeoutRunnable;

    private static /* synthetic */ void getLastFoldUpdate$annotations() {
    }

    @Inject
    public DeviceFoldStateProvider(Context context, HingeAngleProvider hingeAngleProvider2, ScreenStatusProvider screenStatusProvider2, DeviceStateManager deviceStateManager2, ActivityManager activityManager2, @Main Executor executor, @Main Handler handler2) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(hingeAngleProvider2, "hingeAngleProvider");
        Intrinsics.checkNotNullParameter(screenStatusProvider2, "screenStatusProvider");
        Intrinsics.checkNotNullParameter(deviceStateManager2, "deviceStateManager");
        Intrinsics.checkNotNullParameter(activityManager2, "activityManager");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        Intrinsics.checkNotNullParameter(handler2, "handler");
        this.hingeAngleProvider = hingeAngleProvider2;
        this.screenStatusProvider = screenStatusProvider2;
        this.deviceStateManager = deviceStateManager2;
        this.activityManager = activityManager2;
        this.mainExecutor = executor;
        this.handler = handler2;
        this.foldStateListener = new FoldStateListener(this, context);
        this.timeoutRunnable = new TimeoutRunnable();
        this.halfOpenedTimeoutMillis = context.getResources().getInteger(17694958);
        this.isUnfoldHandled = true;
    }

    public void start() {
        this.deviceStateManager.registerCallback(this.mainExecutor, this.foldStateListener);
        this.screenStatusProvider.addCallback(this.screenListener);
        this.hingeAngleProvider.addCallback(this.hingeAngleListener);
    }

    public void stop() {
        this.screenStatusProvider.removeCallback(this.screenListener);
        this.deviceStateManager.unregisterCallback(this.foldStateListener);
        this.hingeAngleProvider.removeCallback(this.hingeAngleListener);
        this.hingeAngleProvider.stop();
    }

    public void addCallback(FoldStateProvider.FoldUpdatesListener foldUpdatesListener) {
        Intrinsics.checkNotNullParameter(foldUpdatesListener, "listener");
        this.outputListeners.add(foldUpdatesListener);
    }

    public void removeCallback(FoldStateProvider.FoldUpdatesListener foldUpdatesListener) {
        Intrinsics.checkNotNullParameter(foldUpdatesListener, "listener");
        this.outputListeners.remove((Object) foldUpdatesListener);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.lastFoldUpdate;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0010, code lost:
        r2 = r2.lastFoldUpdate;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isFinishedOpening() {
        /*
            r2 = this;
            boolean r0 = r2.isFolded
            if (r0 != 0) goto L_0x001e
            java.lang.Integer r0 = r2.lastFoldUpdate
            if (r0 != 0) goto L_0x0009
            goto L_0x0010
        L_0x0009:
            int r0 = r0.intValue()
            r1 = 4
            if (r0 == r1) goto L_0x001c
        L_0x0010:
            java.lang.Integer r2 = r2.lastFoldUpdate
            if (r2 != 0) goto L_0x0015
            goto L_0x001e
        L_0x0015:
            int r2 = r2.intValue()
            r0 = 3
            if (r2 != r0) goto L_0x001e
        L_0x001c:
            r2 = 1
            goto L_0x001f
        L_0x001e:
            r2 = 0
        L_0x001f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.unfold.updates.DeviceFoldStateProvider.isFinishedOpening():boolean");
    }

    private final boolean isTransitionInProgress() {
        Integer num = this.lastFoldUpdate;
        if (num != null && num.intValue() == 0) {
            return true;
        }
        Integer num2 = this.lastFoldUpdate;
        if (num2 != null && num2.intValue() == 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public final void onHingeAngle(float f) {
        boolean z = false;
        boolean z2 = f < this.lastHingeAngle;
        Integer closingThreshold = getClosingThreshold();
        boolean z3 = closingThreshold == null || f < ((float) closingThreshold.intValue());
        boolean z4 = 180.0f - f < 15.0f;
        Integer num = this.lastFoldUpdate;
        if (num != null && num.intValue() == 1) {
            z = true;
        }
        if (z2 && z3 && !z && !z4) {
            notifyFoldUpdate(1);
        }
        if (isTransitionInProgress()) {
            if (z4) {
                notifyFoldUpdate(4);
                cancelTimeout();
            } else {
                rescheduleAbortAnimationTimeout();
            }
        }
        this.lastHingeAngle = f;
        for (FoldStateProvider.FoldUpdatesListener onHingeAngleUpdate : this.outputListeners) {
            onHingeAngleUpdate.onHingeAngleUpdate(f);
        }
    }

    private final Integer getClosingThreshold() {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        List<ActivityManager.RunningTaskInfo> runningTasks = this.activityManager.getRunningTasks(1);
        if (runningTasks == null || (runningTaskInfo = (ActivityManager.RunningTaskInfo) CollectionsKt.getOrNull(runningTasks, 0)) == null) {
            return null;
        }
        if (runningTaskInfo.topActivityType != 2) {
            return 60;
        }
        Integer num = null;
        return null;
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider$FoldStateListener;", "Landroid/hardware/devicestate/DeviceStateManager$FoldStateListener;", "context", "Landroid/content/Context;", "(Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider;Landroid/content/Context;)V", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DeviceFoldStateProvider.kt */
    private final class FoldStateListener extends DeviceStateManager.FoldStateListener {
        final /* synthetic */ DeviceFoldStateProvider this$0;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public FoldStateListener(DeviceFoldStateProvider deviceFoldStateProvider, Context context) {
            super(context, new C3251x9a6fedba(deviceFoldStateProvider));
            Intrinsics.checkNotNullParameter(context, "context");
            this.this$0 = deviceFoldStateProvider;
        }

        /* access modifiers changed from: private */
        /* renamed from: _init_$lambda-0  reason: not valid java name */
        public static final void m3291_init_$lambda0(DeviceFoldStateProvider deviceFoldStateProvider, boolean z) {
            Intrinsics.checkNotNullParameter(deviceFoldStateProvider, "this$0");
            deviceFoldStateProvider.isFolded = z;
            deviceFoldStateProvider.lastHingeAngle = 0.0f;
            if (z) {
                deviceFoldStateProvider.hingeAngleProvider.stop();
                deviceFoldStateProvider.notifyFoldUpdate(5);
                deviceFoldStateProvider.cancelTimeout();
                deviceFoldStateProvider.isUnfoldHandled = false;
                return;
            }
            deviceFoldStateProvider.notifyFoldUpdate(0);
            deviceFoldStateProvider.rescheduleAbortAnimationTimeout();
            deviceFoldStateProvider.hingeAngleProvider.start();
        }
    }

    /* access modifiers changed from: private */
    public final void notifyFoldUpdate(int i) {
        for (FoldStateProvider.FoldUpdatesListener onFoldUpdate : this.outputListeners) {
            onFoldUpdate.onFoldUpdate(i);
        }
        this.lastFoldUpdate = Integer.valueOf(i);
    }

    /* access modifiers changed from: private */
    public final void rescheduleAbortAnimationTimeout() {
        if (isTransitionInProgress()) {
            cancelTimeout();
        }
        this.handler.postDelayed(this.timeoutRunnable, (long) this.halfOpenedTimeoutMillis);
    }

    /* access modifiers changed from: private */
    public final void cancelTimeout() {
        this.handler.removeCallbacks(this.timeoutRunnable);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider$ScreenStatusListener;", "Lcom/android/systemui/unfold/updates/screen/ScreenStatusProvider$ScreenListener;", "(Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider;)V", "onScreenTurnedOn", "", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DeviceFoldStateProvider.kt */
    private final class ScreenStatusListener implements ScreenStatusProvider.ScreenListener {
        public ScreenStatusListener() {
        }

        public void onScreenTurnedOn() {
            if (!DeviceFoldStateProvider.this.isFolded && !DeviceFoldStateProvider.this.isUnfoldHandled) {
                for (FoldStateProvider.FoldUpdatesListener onFoldUpdate : DeviceFoldStateProvider.this.outputListeners) {
                    onFoldUpdate.onFoldUpdate(2);
                }
                DeviceFoldStateProvider.this.isUnfoldHandled = true;
            }
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0002H\u0016¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider$HingeAngleListener;", "Landroidx/core/util/Consumer;", "", "(Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider;)V", "accept", "", "angle", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DeviceFoldStateProvider.kt */
    private final class HingeAngleListener implements Consumer<Float> {
        public HingeAngleListener() {
        }

        public /* bridge */ /* synthetic */ void accept(Object obj) {
            accept(((Number) obj).floatValue());
        }

        public void accept(float f) {
            DeviceFoldStateProvider.this.onHingeAngle(f);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider$TimeoutRunnable;", "Ljava/lang/Runnable;", "(Lcom/android/systemui/unfold/updates/DeviceFoldStateProvider;)V", "run", "", "shared_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DeviceFoldStateProvider.kt */
    private final class TimeoutRunnable implements Runnable {
        public TimeoutRunnable() {
        }

        public void run() {
            DeviceFoldStateProvider.this.notifyFoldUpdate(3);
        }
    }
}
