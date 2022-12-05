package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.Lazy;
import java.util.HashSet;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: UnlockedScreenOffAnimationController.kt */
/* loaded from: classes.dex */
public final class UnlockedScreenOffAnimationController implements WakefulnessLifecycle.Observer {
    private boolean aodUiAnimationPlaying;
    @NotNull
    private final Context context;
    @Nullable
    private Boolean decidedToAnimateGoingToSleep;
    @NotNull
    private final Lazy<DozeParameters> dozeParameters;
    @NotNull
    private final GlobalSettings globalSettings;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    @NotNull
    private final Lazy<KeyguardViewMediator> keyguardViewMediatorLazy;
    private boolean lightRevealAnimationPlaying;
    private final ValueAnimator lightRevealAnimator;
    private LightRevealScrim lightRevealScrim;
    private boolean shouldAnimateInKeyguard;
    private StatusBar statusBar;
    @NotNull
    private final StatusBarStateControllerImpl statusBarStateControllerImpl;
    @NotNull
    private final WakefulnessLifecycle wakefulnessLifecycle;
    @NotNull
    private final Handler handler = new Handler();
    private float animatorDurationScale = 1.0f;
    @NotNull
    private HashSet<Callback> callbacks = new HashSet<>();
    @NotNull
    private final ContentObserver animatorDurationScaleObserver = new ContentObserver() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$animatorDurationScaleObserver$1
        /* JADX INFO: Access modifiers changed from: package-private */
        {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            UnlockedScreenOffAnimationController.this.updateAnimatorDurationScale();
        }
    };

    /* compiled from: UnlockedScreenOffAnimationController.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        void onUnlockedScreenOffProgressUpdate(float f, float f2);
    }

    public UnlockedScreenOffAnimationController(@NotNull Context context, @NotNull WakefulnessLifecycle wakefulnessLifecycle, @NotNull StatusBarStateControllerImpl statusBarStateControllerImpl, @NotNull Lazy<KeyguardViewMediator> keyguardViewMediatorLazy, @NotNull KeyguardStateController keyguardStateController, @NotNull Lazy<DozeParameters> dozeParameters, @NotNull GlobalSettings globalSettings) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(statusBarStateControllerImpl, "statusBarStateControllerImpl");
        Intrinsics.checkNotNullParameter(keyguardViewMediatorLazy, "keyguardViewMediatorLazy");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(dozeParameters, "dozeParameters");
        Intrinsics.checkNotNullParameter(globalSettings, "globalSettings");
        this.context = context;
        this.wakefulnessLifecycle = wakefulnessLifecycle;
        this.statusBarStateControllerImpl = statusBarStateControllerImpl;
        this.keyguardViewMediatorLazy = keyguardViewMediatorLazy;
        this.keyguardStateController = keyguardStateController;
        this.dozeParameters = dozeParameters;
        this.globalSettings = globalSettings;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat.setDuration(750L);
        ofFloat.setInterpolator(Interpolators.LINEAR);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$lightRevealAnimator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LightRevealScrim lightRevealScrim;
                lightRevealScrim = UnlockedScreenOffAnimationController.this.lightRevealScrim;
                if (lightRevealScrim == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
                    throw null;
                }
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                lightRevealScrim.setRevealAmount(((Float) animatedValue).floatValue());
                Object animatedValue2 = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue2, "null cannot be cast to non-null type kotlin.Float");
                UnlockedScreenOffAnimationController.this.sendUnlockedScreenOffProgressUpdate(1.0f - valueAnimator.getAnimatedFraction(), 1.0f - ((Float) animatedValue2).floatValue());
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$lightRevealAnimator$1$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(@Nullable Animator animator) {
                LightRevealScrim lightRevealScrim;
                lightRevealScrim = UnlockedScreenOffAnimationController.this.lightRevealScrim;
                if (lightRevealScrim != null) {
                    lightRevealScrim.setRevealAmount(1.0f);
                    UnlockedScreenOffAnimationController.this.lightRevealAnimationPlaying = false;
                    UnlockedScreenOffAnimationController.this.sendUnlockedScreenOffProgressUpdate(0.0f, 0.0f);
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("lightRevealScrim");
                throw null;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator) {
                UnlockedScreenOffAnimationController.this.lightRevealAnimationPlaying = false;
            }
        });
        Unit unit = Unit.INSTANCE;
        this.lightRevealAnimator = ofFloat;
    }

    public final void initialize(@NotNull StatusBar statusBar, @NotNull LightRevealScrim lightRevealScrim) {
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(lightRevealScrim, "lightRevealScrim");
        this.lightRevealScrim = lightRevealScrim;
        this.statusBar = statusBar;
        updateAnimatorDurationScale();
        this.globalSettings.registerContentObserver(Settings.Global.getUriFor("animator_duration_scale"), false, this.animatorDurationScaleObserver);
        this.wakefulnessLifecycle.addObserver(this);
    }

    public final void updateAnimatorDurationScale() {
        this.animatorDurationScale = this.globalSettings.getFloat("animator_duration_scale", 1.0f);
    }

    public final void animateInKeyguard(@NotNull final View keyguardView, @NotNull final Runnable after) {
        Intrinsics.checkNotNullParameter(keyguardView, "keyguardView");
        Intrinsics.checkNotNullParameter(after, "after");
        this.shouldAnimateInKeyguard = false;
        keyguardView.setAlpha(0.0f);
        keyguardView.setVisibility(0);
        float y = keyguardView.getY();
        keyguardView.setY(y - (keyguardView.getHeight() * 0.1f));
        AnimatableProperty animatableProperty = AnimatableProperty.Y;
        PropertyAnimator.cancelAnimation(keyguardView, animatableProperty);
        long j = 500;
        PropertyAnimator.setProperty(keyguardView, animatableProperty, y, new AnimationProperties().setDuration(j), true);
        keyguardView.animate().setDuration(j).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$animateInKeyguard$1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator) {
                Lazy lazy;
                StatusBar statusBar;
                UnlockedScreenOffAnimationController.this.aodUiAnimationPlaying = false;
                lazy = UnlockedScreenOffAnimationController.this.keyguardViewMediatorLazy;
                ((KeyguardViewMediator) lazy.get()).maybeHandlePendingLock();
                statusBar = UnlockedScreenOffAnimationController.this.statusBar;
                if (statusBar == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("statusBar");
                    throw null;
                }
                statusBar.updateIsKeyguard();
                after.run();
                UnlockedScreenOffAnimationController.this.decidedToAnimateGoingToSleep = null;
                keyguardView.animate().setListener(null);
            }
        }).start();
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedWakingUp() {
        this.decidedToAnimateGoingToSleep = null;
        this.shouldAnimateInKeyguard = false;
        this.lightRevealAnimator.cancel();
        this.handler.removeCallbacksAndMessages(null);
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onFinishedWakingUp() {
        this.aodUiAnimationPlaying = false;
        if (this.dozeParameters.get().canControlUnlockedScreenOff()) {
            StatusBar statusBar = this.statusBar;
            if (statusBar != null) {
                statusBar.updateIsKeyguard(true);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("statusBar");
                throw null;
            }
        }
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public void onStartedGoingToSleep() {
        if (this.dozeParameters.get().shouldControlUnlockedScreenOff()) {
            this.decidedToAnimateGoingToSleep = Boolean.TRUE;
            this.shouldAnimateInKeyguard = true;
            this.lightRevealAnimationPlaying = true;
            this.lightRevealAnimator.start();
            this.handler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.UnlockedScreenOffAnimationController$onStartedGoingToSleep$1
                @Override // java.lang.Runnable
                public final void run() {
                    StatusBar statusBar;
                    UnlockedScreenOffAnimationController.this.aodUiAnimationPlaying = true;
                    statusBar = UnlockedScreenOffAnimationController.this.statusBar;
                    if (statusBar != null) {
                        statusBar.getNotificationPanelViewController().showAodUi();
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("statusBar");
                        throw null;
                    }
                }
            }, ((float) 600) * this.animatorDurationScale);
            return;
        }
        this.decidedToAnimateGoingToSleep = Boolean.FALSE;
    }

    public final boolean shouldPlayUnlockedScreenOffAnimation() {
        StatusBar statusBar;
        if (!Intrinsics.areEqual(this.decidedToAnimateGoingToSleep, Boolean.FALSE) && this.dozeParameters.get().canControlUnlockedScreenOff() && this.statusBarStateControllerImpl.getState() == 0 && (statusBar = this.statusBar) != null) {
            if (statusBar != null) {
                if (statusBar.getNotificationPanelViewController().isFullyCollapsed()) {
                    return this.keyguardStateController.isKeyguardScreenRotationAllowed() || this.context.getResources().getConfiguration().orientation == 1;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("statusBar");
                throw null;
            }
        }
        return false;
    }

    public final void addCallback(@NotNull Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.callbacks.add(callback);
    }

    public final void removeCallback(@NotNull Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.callbacks.remove(callback);
    }

    public final void sendUnlockedScreenOffProgressUpdate(float f, float f2) {
        for (Callback callback : this.callbacks) {
            callback.onUnlockedScreenOffProgressUpdate(f, f2);
        }
    }

    public final boolean isScreenOffAnimationPlaying() {
        return this.lightRevealAnimationPlaying || this.aodUiAnimationPlaying;
    }

    public final boolean shouldAnimateInKeyguard() {
        return this.shouldAnimateInKeyguard;
    }

    public final boolean isScreenOffLightRevealAnimationPlaying() {
        return this.lightRevealAnimationPlaying;
    }
}
