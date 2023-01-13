package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.GlobalSettings;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.doze.AODController;
import dagger.Lazy;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0015\b\u0007\u0018\u0000 G2\u00020\u00012\u00020\u0002:\u0001GBe\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n\u0012\u0006\u0010\f\u001a\u00020\r\u0012\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\n\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\b\b\u0002\u0010\u0016\u001a\u00020\u0017¢\u0006\u0002\u0010\u0018J\u0018\u0010.\u001a\u00020/2\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u000203H\u0016J\u0018\u00104\u001a\u00020/2\u0006\u00105\u001a\u00020,2\u0006\u0010)\u001a\u00020*H\u0016J\b\u00106\u001a\u00020 H\u0016J\b\u00107\u001a\u00020 H\u0016J\u0006\u00108\u001a\u00020 J\u0006\u00109\u001a\u00020 J\b\u0010:\u001a\u00020/H\u0016J\b\u0010;\u001a\u00020/H\u0016J\b\u0010<\u001a\u00020 H\u0016J\u000e\u0010=\u001a\u00020/2\u0006\u0010$\u001a\u00020 J\b\u0010>\u001a\u00020 H\u0016J\b\u0010-\u001a\u00020 H\u0016J\b\u0010?\u001a\u00020 H\u0016J\b\u0010@\u001a\u00020 H\u0016J\b\u0010A\u001a\u00020 H\u0016J\b\u0010B\u001a\u00020 H\u0016J\u0006\u0010C\u001a\u00020 J\b\u0010D\u001a\u00020 H\u0016J\b\u0010E\u001a\u00020 H\u0016J\u0006\u0010F\u001a\u00020/R\u000e\u0010\u0019\u001a\u00020\u001aX\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u001b\u001a\u00020\u001c¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u000e\u0010\u001f\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010!\u001a\u0004\u0018\u00010 X\u000e¢\u0006\u0004\n\u0002\u0010\"R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010&\u001a\n (*\u0004\u0018\u00010'0'X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020*X.¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020,X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006H"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/UnlockedScreenOffAnimationController;", "Lcom/android/systemui/keyguard/WakefulnessLifecycle$Observer;", "Lcom/android/systemui/statusbar/phone/ScreenOffAnimation;", "context", "Landroid/content/Context;", "wakefulnessLifecycle", "Lcom/android/systemui/keyguard/WakefulnessLifecycle;", "statusBarStateControllerImpl", "Lcom/android/systemui/statusbar/StatusBarStateControllerImpl;", "keyguardViewMediatorLazy", "Ldagger/Lazy;", "Lcom/android/systemui/keyguard/KeyguardViewMediator;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "dozeParameters", "Lcom/android/systemui/statusbar/phone/DozeParameters;", "globalSettings", "Lcom/android/systemui/util/settings/GlobalSettings;", "interactionJankMonitor", "Lcom/android/internal/jank/InteractionJankMonitor;", "powerManager", "Landroid/os/PowerManager;", "handler", "Landroid/os/Handler;", "(Landroid/content/Context;Lcom/android/systemui/keyguard/WakefulnessLifecycle;Lcom/android/systemui/statusbar/StatusBarStateControllerImpl;Ldagger/Lazy;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Ldagger/Lazy;Lcom/android/systemui/util/settings/GlobalSettings;Lcom/android/internal/jank/InteractionJankMonitor;Landroid/os/PowerManager;Landroid/os/Handler;)V", "animatorDurationScale", "", "animatorDurationScaleObserver", "Landroid/database/ContentObserver;", "getAnimatorDurationScaleObserver", "()Landroid/database/ContentObserver;", "aodUiAnimationPlaying", "", "decidedToAnimateGoingToSleep", "Ljava/lang/Boolean;", "initialized", "landscapeScreenOff", "lightRevealAnimationPlaying", "lightRevealAnimator", "Landroid/animation/ValueAnimator;", "kotlin.jvm.PlatformType", "lightRevealScrim", "Lcom/android/systemui/statusbar/LightRevealScrim;", "mCentralSurfaces", "Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "shouldAnimateInKeyguard", "animateInKeyguard", "", "keyguardView", "Landroid/view/View;", "after", "Ljava/lang/Runnable;", "initialize", "centralSurfaces", "isAnimationPlaying", "isKeyguardShowDelayed", "isLandscapeScreenOff", "isScreenOffLightRevealAnimationPlaying", "onFinishedWakingUp", "onStartedWakingUp", "overrideNotificationsDozeAmount", "setIsLandscapeOff", "shouldAnimateAodIcons", "shouldDelayDisplayDozeTransition", "shouldDelayKeyguardShow", "shouldHideScrimOnWakeUp", "shouldPlayAnimation", "shouldPlayUnlockedScreenOffAnimation", "shouldShowAodIconsWhenShade", "startAnimation", "updateAnimatorDurationScale", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UnlockedScreenOffAnimationController.kt */
public final class UnlockedScreenOffAnimationController implements WakefulnessLifecycle.Observer, ScreenOffAnimation {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "UnlockedScreenOffAnimationController";
    private float animatorDurationScale;
    private final ContentObserver animatorDurationScaleObserver;
    /* access modifiers changed from: private */
    public boolean aodUiAnimationPlaying;
    private final Context context;
    /* access modifiers changed from: private */
    public Boolean decidedToAnimateGoingToSleep;
    private final Lazy<DozeParameters> dozeParameters;
    private final GlobalSettings globalSettings;
    private final Handler handler;
    private boolean initialized;
    /* access modifiers changed from: private */
    public final InteractionJankMonitor interactionJankMonitor;
    private final KeyguardStateController keyguardStateController;
    private final Lazy<KeyguardViewMediator> keyguardViewMediatorLazy;
    private boolean landscapeScreenOff;
    /* access modifiers changed from: private */
    public boolean lightRevealAnimationPlaying;
    private final ValueAnimator lightRevealAnimator;
    /* access modifiers changed from: private */
    public LightRevealScrim lightRevealScrim;
    /* access modifiers changed from: private */
    public CentralSurfaces mCentralSurfaces;
    private final PowerManager powerManager;
    private boolean shouldAnimateInKeyguard;
    private final StatusBarStateControllerImpl statusBarStateControllerImpl;
    private final WakefulnessLifecycle wakefulnessLifecycle;

    @Inject
    public UnlockedScreenOffAnimationController(Context context2, WakefulnessLifecycle wakefulnessLifecycle2, StatusBarStateControllerImpl statusBarStateControllerImpl2, Lazy<KeyguardViewMediator> lazy, KeyguardStateController keyguardStateController2, Lazy<DozeParameters> lazy2, GlobalSettings globalSettings2, InteractionJankMonitor interactionJankMonitor2, PowerManager powerManager2, Handler handler2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle2, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(statusBarStateControllerImpl2, "statusBarStateControllerImpl");
        Intrinsics.checkNotNullParameter(lazy, "keyguardViewMediatorLazy");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(lazy2, "dozeParameters");
        Intrinsics.checkNotNullParameter(globalSettings2, "globalSettings");
        Intrinsics.checkNotNullParameter(interactionJankMonitor2, "interactionJankMonitor");
        Intrinsics.checkNotNullParameter(powerManager2, "powerManager");
        Intrinsics.checkNotNullParameter(handler2, "handler");
        this.context = context2;
        this.wakefulnessLifecycle = wakefulnessLifecycle2;
        this.statusBarStateControllerImpl = statusBarStateControllerImpl2;
        this.keyguardViewMediatorLazy = lazy;
        this.keyguardStateController = keyguardStateController2;
        this.dozeParameters = lazy2;
        this.globalSettings = globalSettings2;
        this.interactionJankMonitor = interactionJankMonitor2;
        this.powerManager = powerManager2;
        this.handler = handler2;
        this.animatorDurationScale = 1.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
        ofFloat.setDuration(750);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new UnlockedScreenOffAnimationController$$ExternalSyntheticLambda0(this));
        ofFloat.addListener(new UnlockedScreenOffAnimationController$lightRevealAnimator$1$2(this));
        this.lightRevealAnimator = ofFloat;
        this.animatorDurationScaleObserver = new C3130x7f4c478e(this);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ UnlockedScreenOffAnimationController(Context context2, WakefulnessLifecycle wakefulnessLifecycle2, StatusBarStateControllerImpl statusBarStateControllerImpl2, Lazy lazy, KeyguardStateController keyguardStateController2, Lazy lazy2, GlobalSettings globalSettings2, InteractionJankMonitor interactionJankMonitor2, PowerManager powerManager2, Handler handler2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context2, wakefulnessLifecycle2, statusBarStateControllerImpl2, lazy, keyguardStateController2, lazy2, globalSettings2, interactionJankMonitor2, powerManager2, (i & 512) != 0 ? new Handler() : handler2);
    }

    /* access modifiers changed from: private */
    /* renamed from: lightRevealAnimator$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3200lightRevealAnimator$lambda1$lambda0(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(unlockedScreenOffAnimationController, "this$0");
        LightRevealScrim lightRevealScrim2 = unlockedScreenOffAnimationController.lightRevealScrim;
        LightRevealScrim lightRevealScrim3 = null;
        if (lightRevealScrim2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
            lightRevealScrim2 = null;
        }
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            lightRevealScrim2.setRevealAmount(((Float) animatedValue).floatValue());
            LightRevealScrim lightRevealScrim4 = unlockedScreenOffAnimationController.lightRevealScrim;
            if (lightRevealScrim4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
            } else {
                lightRevealScrim3 = lightRevealScrim4;
            }
            if (lightRevealScrim3.isScrimAlmostOccludes() && unlockedScreenOffAnimationController.interactionJankMonitor.isInstrumenting(40)) {
                unlockedScreenOffAnimationController.interactionJankMonitor.end(40);
                return;
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public final ContentObserver getAnimatorDurationScaleObserver() {
        return this.animatorDurationScaleObserver;
    }

    public void initialize(CentralSurfaces centralSurfaces, LightRevealScrim lightRevealScrim2) {
        Intrinsics.checkNotNullParameter(centralSurfaces, "centralSurfaces");
        Intrinsics.checkNotNullParameter(lightRevealScrim2, "lightRevealScrim");
        this.initialized = true;
        this.lightRevealScrim = lightRevealScrim2;
        this.mCentralSurfaces = centralSurfaces;
        updateAnimatorDurationScale();
        this.globalSettings.registerContentObserver(Settings.Global.getUriFor("animator_duration_scale"), false, this.animatorDurationScaleObserver);
        this.wakefulnessLifecycle.addObserver(this);
    }

    public final void updateAnimatorDurationScale() {
        this.animatorDurationScale = this.globalSettings.getFloat("animator_duration_scale", 1.0f);
    }

    public boolean shouldDelayKeyguardShow() {
        return shouldPlayAnimation();
    }

    public boolean isKeyguardShowDelayed() {
        return isAnimationPlaying();
    }

    public void animateInKeyguard(View view, Runnable runnable) {
        Intrinsics.checkNotNullParameter(view, "keyguardView");
        Intrinsics.checkNotNullParameter(runnable, "after");
        this.shouldAnimateInKeyguard = false;
        view.setAlpha(0.0f);
        view.setVisibility(0);
        float y = view.getY();
        view.setY(y - (((float) view.getHeight()) * 0.1f));
        PropertyAnimator.cancelAnimation(view, AnimatableProperty.f375Y);
        long j = (long) 500;
        PropertyAnimator.setProperty(view, AnimatableProperty.f375Y, y, new AnimationProperties().setDuration(j), true);
        view.animate().setDuration(j).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f).withEndAction(new UnlockedScreenOffAnimationController$$ExternalSyntheticLambda2(this, runnable, view)).setListener(new UnlockedScreenOffAnimationController$animateInKeyguard$2(this, view)).start();
    }

    /* access modifiers changed from: private */
    /* renamed from: animateInKeyguard$lambda-2  reason: not valid java name */
    public static final void m3199animateInKeyguard$lambda2(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController, Runnable runnable, View view) {
        Intrinsics.checkNotNullParameter(unlockedScreenOffAnimationController, "this$0");
        Intrinsics.checkNotNullParameter(runnable, "$after");
        Intrinsics.checkNotNullParameter(view, "$keyguardView");
        unlockedScreenOffAnimationController.aodUiAnimationPlaying = false;
        unlockedScreenOffAnimationController.keyguardViewMediatorLazy.get().maybeHandlePendingLock();
        CentralSurfaces centralSurfaces = unlockedScreenOffAnimationController.mCentralSurfaces;
        if (centralSurfaces == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
            centralSurfaces = null;
        }
        centralSurfaces.updateIsKeyguard();
        runnable.run();
        unlockedScreenOffAnimationController.decidedToAnimateGoingToSleep = null;
        view.animate().setListener((Animator.AnimatorListener) null);
        unlockedScreenOffAnimationController.interactionJankMonitor.end(41);
    }

    public void onStartedWakingUp() {
        this.decidedToAnimateGoingToSleep = null;
        this.shouldAnimateInKeyguard = false;
        this.lightRevealAnimator.cancel();
        this.handler.removeCallbacksAndMessages((Object) null);
        this.landscapeScreenOff = false;
    }

    public void onFinishedWakingUp() {
        this.aodUiAnimationPlaying = false;
        if (this.dozeParameters.get().canControlUnlockedScreenOff()) {
            CentralSurfaces centralSurfaces = this.mCentralSurfaces;
            if (centralSurfaces == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
                centralSurfaces = null;
            }
            centralSurfaces.updateIsKeyguard(true);
        }
    }

    public boolean startAnimation() {
        if (shouldPlayUnlockedScreenOffAnimation()) {
            this.decidedToAnimateGoingToSleep = true;
            this.shouldAnimateInKeyguard = true;
            this.lightRevealAnimationPlaying = true;
            LightRevealScrim lightRevealScrim2 = this.lightRevealScrim;
            if (lightRevealScrim2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
                lightRevealScrim2 = null;
            }
            if (lightRevealScrim2.getRevealEffect() instanceof CircleReveal) {
                this.lightRevealAnimator.setDuration(500);
            } else {
                this.lightRevealAnimator.setDuration(750);
            }
            this.lightRevealAnimator.start();
            this.handler.postDelayed(new UnlockedScreenOffAnimationController$$ExternalSyntheticLambda1(this), (long) (((float) 600) * this.animatorDurationScale));
            return true;
        }
        this.decidedToAnimateGoingToSleep = false;
        return false;
    }

    /* access modifiers changed from: private */
    /* renamed from: startAnimation$lambda-3  reason: not valid java name */
    public static final void m3201startAnimation$lambda3(UnlockedScreenOffAnimationController unlockedScreenOffAnimationController) {
        Intrinsics.checkNotNullParameter(unlockedScreenOffAnimationController, "this$0");
        if (!unlockedScreenOffAnimationController.powerManager.isInteractive()) {
            CentralSurfaces centralSurfaces = null;
            if (!unlockedScreenOffAnimationController.dozeParameters.get().getAlwaysOn() || (unlockedScreenOffAnimationController.dozeParameters.get().getAlwaysOn() && ((AODController) NTDependencyEx.get(AODController.class)).checkNightMode())) {
                CentralSurfaces centralSurfaces2 = unlockedScreenOffAnimationController.mCentralSurfaces;
                if (centralSurfaces2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
                    centralSurfaces2 = null;
                }
                centralSurfaces2.getNotificationPanelViewController().setAlpha(0.0f);
            }
            unlockedScreenOffAnimationController.aodUiAnimationPlaying = true;
            CentralSurfaces centralSurfaces3 = unlockedScreenOffAnimationController.mCentralSurfaces;
            if (centralSurfaces3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mCentralSurfaces");
            } else {
                centralSurfaces = centralSurfaces3;
            }
            centralSurfaces.getNotificationPanelViewController().showAodUi();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0054, code lost:
        if (r0.getNotificationPanelViewController().isPanelExpanded() != false) goto L_0x0056;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean shouldPlayUnlockedScreenOffAnimation() {
        /*
            r3 = this;
            boolean r0 = r3.initialized
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            dagger.Lazy<com.android.systemui.statusbar.phone.DozeParameters> r0 = r3.dozeParameters
            java.lang.Object r0 = r0.get()
            com.android.systemui.statusbar.phone.DozeParameters r0 = (com.android.systemui.statusbar.phone.DozeParameters) r0
            boolean r0 = r0.canControlUnlockedScreenOff()
            if (r0 != 0) goto L_0x0015
            return r1
        L_0x0015:
            java.lang.Boolean r0 = r3.decidedToAnimateGoingToSleep
            java.lang.Boolean r2 = java.lang.Boolean.valueOf((boolean) r1)
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r2)
            if (r0 == 0) goto L_0x0022
            return r1
        L_0x0022:
            android.content.Context r0 = r3.context
            android.content.ContentResolver r0 = r0.getContentResolver()
            java.lang.String r2 = "animator_duration_scale"
            java.lang.String r0 = android.provider.Settings.Global.getString(r0, r2)
            java.lang.String r2 = "0"
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r2)
            if (r0 == 0) goto L_0x0037
            return r1
        L_0x0037:
            com.android.systemui.statusbar.StatusBarStateControllerImpl r0 = r3.statusBarStateControllerImpl
            int r0 = r0.getState()
            if (r0 == 0) goto L_0x0040
            return r1
        L_0x0040:
            com.android.systemui.statusbar.phone.CentralSurfaces r0 = r3.mCentralSurfaces
            if (r0 == 0) goto L_0x0056
            if (r0 != 0) goto L_0x004c
            java.lang.String r0 = "mCentralSurfaces"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r0)
            r0 = 0
        L_0x004c:
            com.android.systemui.statusbar.phone.NotificationPanelViewController r0 = r0.getNotificationPanelViewController()
            boolean r0 = r0.isPanelExpanded()
            if (r0 == 0) goto L_0x005d
        L_0x0056:
            boolean r0 = r3.isAnimationPlaying()
            if (r0 != 0) goto L_0x005d
            return r1
        L_0x005d:
            com.android.systemui.statusbar.policy.KeyguardStateController r0 = r3.keyguardStateController
            boolean r0 = r0.isKeyguardScreenRotationAllowed()
            r2 = 1
            if (r0 != 0) goto L_0x007d
            android.content.Context r0 = r3.context
            android.view.Display r0 = r0.getDisplay()
            int r0 = r0.getRotation()
            if (r0 == 0) goto L_0x007d
            android.os.PowerManager r0 = r3.powerManager
            boolean r0 = r0.isInteractive()
            if (r0 != 0) goto L_0x007c
            r3.landscapeScreenOff = r2
        L_0x007c:
            return r1
        L_0x007d:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController.shouldPlayUnlockedScreenOffAnimation():boolean");
    }

    public boolean shouldDelayDisplayDozeTransition() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    public boolean isAnimationPlaying() {
        return this.lightRevealAnimationPlaying || this.aodUiAnimationPlaying;
    }

    public boolean shouldAnimateInKeyguard() {
        return this.shouldAnimateInKeyguard;
    }

    public boolean shouldHideScrimOnWakeUp() {
        return isScreenOffLightRevealAnimationPlaying();
    }

    public boolean overrideNotificationsDozeAmount() {
        return shouldPlayUnlockedScreenOffAnimation() && isAnimationPlaying();
    }

    public boolean shouldShowAodIconsWhenShade() {
        return isAnimationPlaying();
    }

    public boolean shouldAnimateAodIcons() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    public boolean shouldPlayAnimation() {
        return shouldPlayUnlockedScreenOffAnimation();
    }

    public final boolean isScreenOffLightRevealAnimationPlaying() {
        return this.lightRevealAnimationPlaying;
    }

    public final boolean isLandscapeScreenOff() {
        return this.landscapeScreenOff;
    }

    public final void setIsLandscapeOff(boolean z) {
        this.landscapeScreenOff = z;
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/UnlockedScreenOffAnimationController$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: UnlockedScreenOffAnimationController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
