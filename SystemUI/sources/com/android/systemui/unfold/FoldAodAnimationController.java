package com.android.systemui.unfold;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Handler;
import androidx.core.view.OneShotPreDrawListener;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.ScreenOffAnimation;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.settings.GlobalSettings;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0017\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u00032\u00020\u0004:\u0002=>B;\b\u0007\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010¢\u0006\u0002\u0010\u0011J\u0010\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0002H\u0016J\u0018\u0010%\u001a\u00020#2\u0006\u0010&\u001a\u00020\u001a2\u0006\u0010'\u001a\u00020(H\u0016J\b\u0010\u0014\u001a\u00020\u0013H\u0016J\b\u0010)\u001a\u00020\u0013H\u0016J\u0010\u0010*\u001a\u00020#2\u0006\u0010+\u001a\u00020\u0013H\u0016J\u0006\u0010,\u001a\u00020#J\u000e\u0010-\u001a\u00020#2\u0006\u0010.\u001a\u00020\u001cJ\u0010\u0010/\u001a\u00020#2\u0006\u00100\u001a\u00020\u0013H\u0016J\b\u00101\u001a\u00020#H\u0016J\u0010\u00102\u001a\u00020#2\u0006\u0010$\u001a\u00020\u0002H\u0016J\u0010\u00103\u001a\u00020#2\u0006\u00104\u001a\u00020\u0013H\u0002J\u000e\u00105\u001a\u00020#2\u0006\u00106\u001a\u00020\u0013J\b\u00107\u001a\u00020\u0013H\u0016J\b\u00108\u001a\u00020\u0013H\u0016J\b\u00109\u001a\u00020\u0013H\u0016J\b\u0010:\u001a\u00020\u0013H\u0016J\b\u0010\u001d\u001a\u00020\u0013H\u0016J\b\u0010;\u001a\u00020\u0013H\u0016J\b\u0010<\u001a\u00020\u0013H\u0016R\u000e\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX.¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\u001cX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u00020\u00020 j\b\u0012\u0004\u0012\u00020\u0002`!X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000¨\u0006?"}, mo64987d2 = {"Lcom/android/systemui/unfold/FoldAodAnimationController;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/unfold/FoldAodAnimationController$FoldAodAnimationStatus;", "Lcom/android/systemui/statusbar/phone/ScreenOffAnimation;", "Lcom/android/systemui/keyguard/WakefulnessLifecycle$Observer;", "handler", "Landroid/os/Handler;", "executor", "Ljava/util/concurrent/Executor;", "context", "Landroid/content/Context;", "deviceStateManager", "Landroid/hardware/devicestate/DeviceStateManager;", "wakefulnessLifecycle", "Lcom/android/systemui/keyguard/WakefulnessLifecycle;", "globalSettings", "Lcom/android/systemui/util/settings/GlobalSettings;", "(Landroid/os/Handler;Ljava/util/concurrent/Executor;Landroid/content/Context;Landroid/hardware/devicestate/DeviceStateManager;Lcom/android/systemui/keyguard/WakefulnessLifecycle;Lcom/android/systemui/util/settings/GlobalSettings;)V", "alwaysOnEnabled", "", "isAnimationPlaying", "isDozing", "isFoldHandled", "isFolded", "isScrimOpaque", "mCentralSurfaces", "Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "pendingScrimReadyCallback", "Ljava/lang/Runnable;", "shouldPlayAnimation", "startAnimationRunnable", "statusListeners", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "addCallback", "", "listener", "initialize", "centralSurfaces", "lightRevealScrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "isKeyguardHideDelayed", "onAlwaysOnChanged", "alwaysOn", "onScreenTurnedOn", "onScreenTurningOn", "onReady", "onScrimOpaqueChanged", "isOpaque", "onStartedWakingUp", "removeCallback", "setAnimationState", "playing", "setIsDozing", "dozing", "shouldAnimateAodIcons", "shouldAnimateClockChange", "shouldAnimateDozingChange", "shouldDelayDisplayDozeTransition", "shouldShowAodIconsWhenShade", "startAnimation", "FoldAodAnimationStatus", "FoldListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@SysUIUnfoldScope
/* compiled from: FoldAodAnimationController.kt */
public final class FoldAodAnimationController implements CallbackController<FoldAodAnimationStatus>, ScreenOffAnimation, WakefulnessLifecycle.Observer {
    private boolean alwaysOnEnabled;
    /* access modifiers changed from: private */
    public final Context context;
    private final DeviceStateManager deviceStateManager;
    private final Executor executor;
    private final GlobalSettings globalSettings;
    private final Handler handler;
    private boolean isAnimationPlaying;
    private boolean isDozing;
    /* access modifiers changed from: private */
    public boolean isFoldHandled = true;
    /* access modifiers changed from: private */
    public boolean isFolded;
    private boolean isScrimOpaque;
    private CentralSurfaces mCentralSurfaces;
    private Runnable pendingScrimReadyCallback;
    private boolean shouldPlayAnimation;
    private final Runnable startAnimationRunnable = new FoldAodAnimationController$$ExternalSyntheticLambda0(this);
    private final ArrayList<FoldAodAnimationStatus> statusListeners = new ArrayList<>();
    private final WakefulnessLifecycle wakefulnessLifecycle;

    @Metadata(mo64986d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0004À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/unfold/FoldAodAnimationController$FoldAodAnimationStatus;", "", "onFoldToAodAnimationChanged", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: FoldAodAnimationController.kt */
    public interface FoldAodAnimationStatus {
        void onFoldToAodAnimationChanged();
    }

    @Inject
    public FoldAodAnimationController(@Main Handler handler2, @Main Executor executor2, Context context2, DeviceStateManager deviceStateManager2, WakefulnessLifecycle wakefulnessLifecycle2, GlobalSettings globalSettings2) {
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(deviceStateManager2, "deviceStateManager");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle2, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(globalSettings2, "globalSettings");
        this.handler = handler2;
        this.executor = executor2;
        this.context = context2;
        this.deviceStateManager = deviceStateManager2;
        this.wakefulnessLifecycle = wakefulnessLifecycle2;
        this.globalSettings = globalSettings2;
    }

    /* access modifiers changed from: private */
    /* renamed from: startAnimationRunnable$lambda-1  reason: not valid java name */
    public static final void m3263startAnimationRunnable$lambda1(FoldAodAnimationController foldAodAnimationController) {
        Intrinsics.checkNotNullParameter(foldAodAnimationController, "this$0");
        CentralSurfaces centralSurfaces = foldAodAnimationController.mCentralSurfaces;
        if (centralSurfaces == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
            centralSurfaces = null;
        }
        centralSurfaces.getNotificationPanelViewController().startFoldToAodAnimation(new FoldAodAnimationController$$ExternalSyntheticLambda1(foldAodAnimationController));
    }

    /* access modifiers changed from: private */
    /* renamed from: startAnimationRunnable$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3264startAnimationRunnable$lambda1$lambda0(FoldAodAnimationController foldAodAnimationController) {
        Intrinsics.checkNotNullParameter(foldAodAnimationController, "this$0");
        foldAodAnimationController.setAnimationState(false);
    }

    public void initialize(CentralSurfaces centralSurfaces, LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(centralSurfaces, "centralSurfaces");
        Intrinsics.checkNotNullParameter(lightRevealScrim, "lightRevealScrim");
        this.mCentralSurfaces = centralSurfaces;
        this.deviceStateManager.registerCallback(this.executor, new FoldListener());
        this.wakefulnessLifecycle.addObserver(this);
    }

    public boolean shouldPlayAnimation() {
        return this.shouldPlayAnimation;
    }

    public boolean startAnimation() {
        if (!this.alwaysOnEnabled || this.wakefulnessLifecycle.getLastSleepReason() != 13 || Intrinsics.areEqual((Object) this.globalSettings.getString("animator_duration_scale"), (Object) "0")) {
            setAnimationState(false);
            return false;
        }
        setAnimationState(true);
        CentralSurfaces centralSurfaces = this.mCentralSurfaces;
        if (centralSurfaces == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
            centralSurfaces = null;
        }
        centralSurfaces.getNotificationPanelViewController().prepareFoldToAodAnimation();
        return true;
    }

    public void onStartedWakingUp() {
        if (this.isAnimationPlaying) {
            this.handler.removeCallbacks(this.startAnimationRunnable);
            CentralSurfaces centralSurfaces = this.mCentralSurfaces;
            if (centralSurfaces == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
                centralSurfaces = null;
            }
            centralSurfaces.getNotificationPanelViewController().cancelFoldToAodAnimation();
        }
        setAnimationState(false);
    }

    private final void setAnimationState(boolean z) {
        this.shouldPlayAnimation = z;
        this.isAnimationPlaying = z;
        for (FoldAodAnimationStatus onFoldToAodAnimationChanged : this.statusListeners) {
            onFoldToAodAnimationChanged.onFoldToAodAnimationChanged();
        }
    }

    public final void onScreenTurningOn(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "onReady");
        if (this.shouldPlayAnimation) {
            if (this.isScrimOpaque) {
                runnable.run();
            } else {
                this.pendingScrimReadyCallback = runnable;
            }
        } else if (!this.isFolded || this.isFoldHandled || !this.alwaysOnEnabled || !this.isDozing) {
            runnable.run();
        } else {
            setAnimationState(true);
            CentralSurfaces centralSurfaces = this.mCentralSurfaces;
            CentralSurfaces centralSurfaces2 = null;
            if (centralSurfaces == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
                centralSurfaces = null;
            }
            centralSurfaces.getNotificationPanelViewController().prepareFoldToAodAnimation();
            CentralSurfaces centralSurfaces3 = this.mCentralSurfaces;
            if (centralSurfaces3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
            } else {
                centralSurfaces2 = centralSurfaces3;
            }
            OneShotPreDrawListener.add(centralSurfaces2.getNotificationPanelViewController().getView(), runnable);
        }
    }

    public void onScrimOpaqueChanged(boolean z) {
        this.isScrimOpaque = z;
        if (z) {
            Runnable runnable = this.pendingScrimReadyCallback;
            if (runnable != null) {
                runnable.run();
            }
            this.pendingScrimReadyCallback = null;
        }
    }

    public final void onScreenTurnedOn() {
        if (this.shouldPlayAnimation) {
            this.handler.removeCallbacks(this.startAnimationRunnable);
            this.handler.post(this.startAnimationRunnable);
            this.shouldPlayAnimation = false;
        }
    }

    public final void setIsDozing(boolean z) {
        this.isDozing = z;
    }

    public boolean isAnimationPlaying() {
        return this.isAnimationPlaying;
    }

    public boolean isKeyguardHideDelayed() {
        return isAnimationPlaying();
    }

    public boolean shouldShowAodIconsWhenShade() {
        return shouldPlayAnimation();
    }

    public boolean shouldAnimateAodIcons() {
        return !shouldPlayAnimation();
    }

    public boolean shouldAnimateDozingChange() {
        return !shouldPlayAnimation();
    }

    public boolean shouldAnimateClockChange() {
        return !isAnimationPlaying();
    }

    public boolean shouldDelayDisplayDozeTransition() {
        return shouldPlayAnimation();
    }

    public void onAlwaysOnChanged(boolean z) {
        this.alwaysOnEnabled = z;
    }

    public void addCallback(FoldAodAnimationStatus foldAodAnimationStatus) {
        Intrinsics.checkNotNullParameter(foldAodAnimationStatus, "listener");
        this.statusListeners.add(foldAodAnimationStatus);
    }

    public void removeCallback(FoldAodAnimationStatus foldAodAnimationStatus) {
        Intrinsics.checkNotNullParameter(foldAodAnimationStatus, "listener");
        this.statusListeners.remove((Object) foldAodAnimationStatus);
    }

    @Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo64987d2 = {"Lcom/android/systemui/unfold/FoldAodAnimationController$FoldListener;", "Landroid/hardware/devicestate/DeviceStateManager$FoldStateListener;", "(Lcom/android/systemui/unfold/FoldAodAnimationController;)V", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: FoldAodAnimationController.kt */
    private final class FoldListener extends DeviceStateManager.FoldStateListener {
        public FoldListener() {
            super(FoldAodAnimationController.this.context, new C3237xb90e095d(FoldAodAnimationController.this));
        }

        /* access modifiers changed from: private */
        /* renamed from: _init_$lambda-0  reason: not valid java name */
        public static final void m3266_init_$lambda0(FoldAodAnimationController foldAodAnimationController, Boolean bool) {
            Intrinsics.checkNotNullParameter(foldAodAnimationController, "this$0");
            if (!bool.booleanValue()) {
                foldAodAnimationController.isFoldHandled = false;
            }
            Intrinsics.checkNotNullExpressionValue(bool, "isFolded");
            foldAodAnimationController.isFolded = bool.booleanValue();
        }
    }
}
