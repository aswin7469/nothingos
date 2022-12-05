package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.WallpaperManager;
import android.os.SystemClock;
import android.os.Trace;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewRootImpl;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.PanelExpansionListener;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.nothingos.utils.SystemUIUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: NotificationShadeDepthController.kt */
/* loaded from: classes.dex */
public final class NotificationShadeDepthController implements PanelExpansionListener, Dumpable {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final BiometricUnlockController biometricUnlockController;
    @Nullable
    private View blurRoot;
    @NotNull
    private final BlurUtils blurUtils;
    private boolean blursDisabledForAppLaunch;
    private boolean brightnessMirrorVisible;
    @NotNull
    private final Choreographer choreographer;
    @NotNull
    private final DozeParameters dozeParameters;
    private boolean isBlurred;
    private boolean isOpen;
    @Nullable
    private Animator keyguardAnimator;
    @NotNull
    private final NotificationShadeDepthController$keyguardStateCallback$1 keyguardStateCallback;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    private int lastAppliedBlur;
    @Nullable
    private Animator notificationAnimator;
    @NotNull
    private final NotificationShadeWindowController notificationShadeWindowController;
    private float panelPullDownMinFraction;
    private int prevShadeDirection;
    private float prevShadeVelocity;
    private boolean prevTracking;
    private float qsPanelExpansion;
    public View root;
    private boolean scrimsVisible;
    private float shadeExpansion;
    @NotNull
    private final NotificationShadeDepthController$statusBarStateCallback$1 statusBarStateCallback;
    @NotNull
    private final StatusBarStateController statusBarStateController;
    private float transitionToFullShadeProgress;
    private boolean updateScheduled;
    private float wakeAndUnlockBlurRadius;
    @NotNull
    private final WallpaperManager wallpaperManager;
    private boolean isClosed = true;
    @NotNull
    private List<DepthListener> listeners = new ArrayList();
    private long prevTimestamp = -1;
    @NotNull
    private DepthAnimation shadeAnimation = new DepthAnimation(this);
    @NotNull
    private DepthAnimation brightnessMirrorSpring = new DepthAnimation(this);
    @NotNull
    private final Choreographer.FrameCallback updateBlurCallback = new Choreographer.FrameCallback() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$updateBlurCallback$1
        @Override // android.view.Choreographer.FrameCallback
        public final void doFrame(long j) {
            boolean shouldApplyShadeBlur;
            float f;
            boolean z;
            boolean z2;
            View view;
            List<NotificationShadeDepthController.DepthListener> list;
            NotificationShadeWindowController notificationShadeWindowController;
            boolean z3 = false;
            NotificationShadeDepthController.this.updateScheduled = false;
            float constrain = MathUtils.constrain(NotificationShadeDepthController.this.getShadeAnimation().getRadius(), NotificationShadeDepthController.this.blurUtils.getMinBlurRadius(), NotificationShadeDepthController.this.blurUtils.getMaxBlurRadius());
            BlurUtils blurUtils = NotificationShadeDepthController.this.blurUtils;
            shouldApplyShadeBlur = NotificationShadeDepthController.this.shouldApplyShadeBlur();
            float f2 = 0.0f;
            float max = Math.max(Math.max((blurUtils.blurRadiusOfRatio(Interpolators.getNotificationScrimAlpha(shouldApplyShadeBlur ? NotificationShadeDepthController.this.getShadeExpansion() : 0.0f, false, SystemUIUtils.getInstance().shouldUseSplitNotificationShade())) * 0.8f) + (constrain * 0.19999999f), NotificationShadeDepthController.this.blurUtils.blurRadiusOfRatio(Interpolators.getNotificationScrimAlpha(NotificationShadeDepthController.this.getQsPanelExpansion(), false, SystemUIUtils.getInstance().shouldUseSplitNotificationShade()) * NotificationShadeDepthController.this.getShadeExpansion())), NotificationShadeDepthController.this.blurUtils.blurRadiusOfRatio(NotificationShadeDepthController.this.getTransitionToFullShadeProgress()));
            f = NotificationShadeDepthController.this.wakeAndUnlockBlurRadius;
            float max2 = Math.max(max, f);
            if (NotificationShadeDepthController.this.getBlursDisabledForAppLaunch()) {
                max2 = 0.0f;
            }
            float saturate = MathUtils.saturate(NotificationShadeDepthController.this.blurUtils.ratioOfBlurRadius(max2));
            int i = (int) max2;
            z = NotificationShadeDepthController.this.scrimsVisible;
            if (z) {
                i = 0;
            } else {
                f2 = saturate;
            }
            if (!NotificationShadeDepthController.this.blurUtils.supportsBlursOnWindows()) {
                i = 0;
            }
            int ratio = (int) (i * (1.0f - NotificationShadeDepthController.this.getBrightnessMirrorSpring().getRatio()));
            z2 = NotificationShadeDepthController.this.scrimsVisible;
            if (z2 && !NotificationShadeDepthController.this.getBlursDisabledForAppLaunch()) {
                z3 = true;
            }
            Trace.traceCounter(4096L, "shade_blur_radius", ratio);
            BlurUtils blurUtils2 = NotificationShadeDepthController.this.blurUtils;
            view = NotificationShadeDepthController.this.blurRoot;
            ViewRootImpl viewRootImpl = view == null ? null : view.getViewRootImpl();
            if (viewRootImpl == null) {
                viewRootImpl = NotificationShadeDepthController.this.getRoot().getViewRootImpl();
            }
            blurUtils2.applyBlur(viewRootImpl, ratio, z3);
            NotificationShadeDepthController.this.lastAppliedBlur = ratio;
            list = NotificationShadeDepthController.this.listeners;
            for (NotificationShadeDepthController.DepthListener depthListener : list) {
                depthListener.onWallpaperZoomOutChanged(f2);
                depthListener.onBlurRadiusChanged(ratio);
            }
            notificationShadeWindowController = NotificationShadeDepthController.this.notificationShadeWindowController;
            notificationShadeWindowController.setBackgroundBlurRadius(ratio);
        }
    };

    /* compiled from: NotificationShadeDepthController.kt */
    /* loaded from: classes.dex */
    public interface DepthListener {
        default void onBlurRadiusChanged(int i) {
        }

        void onWallpaperZoomOutChanged(float f);
    }

    public static /* synthetic */ void getBrightnessMirrorSpring$annotations() {
    }

    public static /* synthetic */ void getShadeExpansion$annotations() {
    }

    public static /* synthetic */ void getUpdateBlurCallback$annotations() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.lang.Object, com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1] */
    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.statusbar.NotificationShadeDepthController$statusBarStateCallback$1, com.android.systemui.plugins.statusbar.StatusBarStateController$StateListener] */
    public NotificationShadeDepthController(@NotNull StatusBarStateController statusBarStateController, @NotNull BlurUtils blurUtils, @NotNull BiometricUnlockController biometricUnlockController, @NotNull KeyguardStateController keyguardStateController, @NotNull Choreographer choreographer, @NotNull WallpaperManager wallpaperManager, @NotNull NotificationShadeWindowController notificationShadeWindowController, @NotNull DozeParameters dozeParameters, @NotNull DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(blurUtils, "blurUtils");
        Intrinsics.checkNotNullParameter(biometricUnlockController, "biometricUnlockController");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(choreographer, "choreographer");
        Intrinsics.checkNotNullParameter(wallpaperManager, "wallpaperManager");
        Intrinsics.checkNotNullParameter(notificationShadeWindowController, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(dozeParameters, "dozeParameters");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.statusBarStateController = statusBarStateController;
        this.blurUtils = blurUtils;
        this.biometricUnlockController = biometricUnlockController;
        this.keyguardStateController = keyguardStateController;
        this.choreographer = choreographer;
        this.wallpaperManager = wallpaperManager;
        this.notificationShadeWindowController = notificationShadeWindowController;
        this.dozeParameters = dozeParameters;
        ?? r3 = new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public void onKeyguardFadingAwayChanged() {
                KeyguardStateController keyguardStateController2;
                BiometricUnlockController biometricUnlockController2;
                Animator animator;
                DozeParameters dozeParameters2;
                KeyguardStateController keyguardStateController3;
                keyguardStateController2 = NotificationShadeDepthController.this.keyguardStateController;
                if (keyguardStateController2.isKeyguardFadingAway()) {
                    biometricUnlockController2 = NotificationShadeDepthController.this.biometricUnlockController;
                    if (biometricUnlockController2.getMode() != 1) {
                        return;
                    }
                    animator = NotificationShadeDepthController.this.keyguardAnimator;
                    if (animator != null) {
                        animator.cancel();
                    }
                    NotificationShadeDepthController notificationShadeDepthController = NotificationShadeDepthController.this;
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
                    final NotificationShadeDepthController notificationShadeDepthController2 = NotificationShadeDepthController.this;
                    dozeParameters2 = notificationShadeDepthController2.dozeParameters;
                    ofFloat.setDuration(dozeParameters2.getWallpaperFadeOutDuration());
                    keyguardStateController3 = notificationShadeDepthController2.keyguardStateController;
                    ofFloat.setStartDelay(keyguardStateController3.getKeyguardFadingAwayDelay());
                    ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                    ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$1
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(@NotNull ValueAnimator animation) {
                            Intrinsics.checkNotNullParameter(animation, "animation");
                            NotificationShadeDepthController notificationShadeDepthController3 = NotificationShadeDepthController.this;
                            BlurUtils blurUtils2 = notificationShadeDepthController3.blurUtils;
                            Object animatedValue = animation.getAnimatedValue();
                            Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                            notificationShadeDepthController3.setWakeAndUnlockBlurRadius(blurUtils2.blurRadiusOfRatio(((Float) animatedValue).floatValue()));
                        }
                    });
                    ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$keyguardStateCallback$1$onKeyguardFadingAwayChanged$1$2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(@Nullable Animator animator2) {
                            NotificationShadeDepthController.this.keyguardAnimator = null;
                            NotificationShadeDepthController.scheduleUpdate$default(NotificationShadeDepthController.this, null, 1, null);
                        }
                    });
                    ofFloat.start();
                    Unit unit = Unit.INSTANCE;
                    notificationShadeDepthController.keyguardAnimator = ofFloat;
                }
            }

            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public void onKeyguardShowingChanged() {
                KeyguardStateController keyguardStateController2;
                Animator animator;
                Animator animator2;
                keyguardStateController2 = NotificationShadeDepthController.this.keyguardStateController;
                if (keyguardStateController2.isShowing()) {
                    animator = NotificationShadeDepthController.this.keyguardAnimator;
                    if (animator != null) {
                        animator.cancel();
                    }
                    animator2 = NotificationShadeDepthController.this.notificationAnimator;
                    if (animator2 == null) {
                        return;
                    }
                    animator2.cancel();
                }
            }
        };
        this.keyguardStateCallback = r3;
        ?? r4 = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$statusBarStateCallback$1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                boolean z;
                float f;
                int i2;
                NotificationShadeDepthController notificationShadeDepthController = NotificationShadeDepthController.this;
                float shadeExpansion = notificationShadeDepthController.getShadeExpansion();
                z = NotificationShadeDepthController.this.prevTracking;
                f = NotificationShadeDepthController.this.prevShadeVelocity;
                i2 = NotificationShadeDepthController.this.prevShadeDirection;
                notificationShadeDepthController.updateShadeAnimationBlur(shadeExpansion, z, f, i2);
                NotificationShadeDepthController.scheduleUpdate$default(NotificationShadeDepthController.this, null, 1, null);
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onDozingChanged(boolean z) {
                if (z) {
                    NotificationShadeDepthController.this.getShadeAnimation().finishIfRunning();
                    NotificationShadeDepthController.this.getBrightnessMirrorSpring().finishIfRunning();
                }
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onDozeAmountChanged(float f, float f2) {
                NotificationShadeDepthController notificationShadeDepthController = NotificationShadeDepthController.this;
                notificationShadeDepthController.setWakeAndUnlockBlurRadius(notificationShadeDepthController.blurUtils.blurRadiusOfRatio(f2));
                NotificationShadeDepthController.scheduleUpdate$default(NotificationShadeDepthController.this, null, 1, null);
            }
        };
        this.statusBarStateCallback = r4;
        String name = NotificationShadeDepthController.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
        keyguardStateController.addCallback(r3);
        statusBarStateController.addCallback(r4);
        notificationShadeWindowController.setScrimsVisibilityListener(new Consumer<Integer>() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController.1
            @Override // java.util.function.Consumer
            public final void accept(Integer num) {
                NotificationShadeDepthController.this.setScrimsVisible(num != null && num.intValue() == 2);
            }
        });
        this.shadeAnimation.setStiffness(200.0f);
        this.shadeAnimation.setDampingRatio(1.0f);
    }

    /* compiled from: NotificationShadeDepthController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @NotNull
    public final View getRoot() {
        View view = this.root;
        if (view != null) {
            return view;
        }
        Intrinsics.throwUninitializedPropertyAccessException("root");
        throw null;
    }

    public final void setRoot(@NotNull View view) {
        Intrinsics.checkNotNullParameter(view, "<set-?>");
        this.root = view;
    }

    public final float getShadeExpansion() {
        return this.shadeExpansion;
    }

    public final void setPanelPullDownMinFraction(float f) {
        this.panelPullDownMinFraction = f;
    }

    @NotNull
    public final DepthAnimation getShadeAnimation() {
        return this.shadeAnimation;
    }

    @NotNull
    public final DepthAnimation getBrightnessMirrorSpring() {
        return this.brightnessMirrorSpring;
    }

    public final void setBrightnessMirrorVisible(boolean z) {
        this.brightnessMirrorVisible = z;
        DepthAnimation.animateTo$default(this.brightnessMirrorSpring, z ? (int) this.blurUtils.blurRadiusOfRatio(1.0f) : 0, null, 2, null);
    }

    public final float getQsPanelExpansion() {
        return this.qsPanelExpansion;
    }

    public final void setQsPanelExpansion(float f) {
        if (Float.isNaN(f)) {
            Log.w("DepthController", "Invalid qs expansion");
            return;
        }
        if (this.qsPanelExpansion == f) {
            return;
        }
        this.qsPanelExpansion = f;
        scheduleUpdate$default(this, null, 1, null);
    }

    public final float getTransitionToFullShadeProgress() {
        return this.transitionToFullShadeProgress;
    }

    public final void setTransitionToFullShadeProgress(float f) {
        if (this.transitionToFullShadeProgress == f) {
            return;
        }
        this.transitionToFullShadeProgress = f;
        scheduleUpdate$default(this, null, 1, null);
    }

    public final boolean getBlursDisabledForAppLaunch() {
        return this.blursDisabledForAppLaunch;
    }

    public final void setBlursDisabledForAppLaunch(boolean z) {
        if (this.blursDisabledForAppLaunch == z) {
            return;
        }
        this.blursDisabledForAppLaunch = z;
        boolean z2 = true;
        scheduleUpdate$default(this, null, 1, null);
        if (this.shadeExpansion == 0.0f) {
            if (this.shadeAnimation.getRadius() != 0.0f) {
                z2 = false;
            }
            if (z2) {
                return;
            }
        }
        if (!z) {
            return;
        }
        DepthAnimation.animateTo$default(this.shadeAnimation, 0, null, 2, null);
        this.shadeAnimation.finishIfRunning();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setScrimsVisible(boolean z) {
        if (this.scrimsVisible == z) {
            return;
        }
        this.scrimsVisible = z;
        scheduleUpdate$default(this, null, 1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setWakeAndUnlockBlurRadius(float f) {
        if (this.wakeAndUnlockBlurRadius == f) {
            return;
        }
        this.wakeAndUnlockBlurRadius = f;
        scheduleUpdate$default(this, null, 1, null);
    }

    public final void addListener(@NotNull DepthListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.add(listener);
    }

    public final void removeListener(@NotNull DepthListener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.remove(listener);
    }

    @Override // com.android.systemui.statusbar.phone.PanelExpansionListener
    public void onPanelExpansionChanged(float f, boolean z) {
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        float f2 = this.panelPullDownMinFraction;
        float f3 = 1.0f;
        float saturate = MathUtils.saturate((f - f2) / (1.0f - f2));
        if ((this.shadeExpansion == saturate) && this.prevTracking == z) {
            this.prevTimestamp = elapsedRealtimeNanos;
            return;
        }
        long j = this.prevTimestamp;
        if (j < 0) {
            this.prevTimestamp = elapsedRealtimeNanos;
        } else {
            f3 = MathUtils.constrain((float) ((elapsedRealtimeNanos - j) / 1.0E9d), 1.0E-5f, 1.0f);
        }
        float f4 = saturate - this.shadeExpansion;
        int signum = (int) Math.signum(f4);
        float constrain = MathUtils.constrain((f4 * 100.0f) / f3, -3000.0f, 3000.0f);
        updateShadeAnimationBlur(saturate, z, constrain, signum);
        this.prevShadeDirection = signum;
        this.prevShadeVelocity = constrain;
        this.shadeExpansion = saturate;
        this.prevTracking = z;
        this.prevTimestamp = elapsedRealtimeNanos;
        scheduleUpdate$default(this, null, 1, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateShadeAnimationBlur(float f, boolean z, float f2, int i) {
        if (!shouldApplyShadeBlur()) {
            animateBlur(false, 0.0f);
            this.isClosed = true;
            this.isOpen = false;
        } else if (f > 0.0f) {
            if (this.isClosed) {
                animateBlur(true, f2);
                this.isClosed = false;
            }
            if (z && !this.isBlurred) {
                animateBlur(true, 0.0f);
            }
            if (!z && i < 0 && this.isBlurred) {
                animateBlur(false, f2);
            }
            if (f == 1.0f) {
                if (this.isOpen) {
                    return;
                }
                this.isOpen = true;
                if (this.isBlurred) {
                    return;
                }
                animateBlur(true, f2);
                return;
            }
            this.isOpen = false;
        } else if (this.isClosed) {
        } else {
            this.isClosed = true;
            if (!this.isBlurred) {
                return;
            }
            animateBlur(false, f2);
        }
    }

    private final void animateBlur(boolean z, float f) {
        this.isBlurred = z;
        float f2 = (!z || !shouldApplyShadeBlur()) ? 0.0f : 1.0f;
        this.shadeAnimation.setStartVelocity(f);
        DepthAnimation.animateTo$default(this.shadeAnimation, (int) this.blurUtils.blurRadiusOfRatio(f2), null, 2, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void scheduleUpdate$default(NotificationShadeDepthController notificationShadeDepthController, View view, int i, Object obj) {
        if ((i & 1) != 0) {
            view = null;
        }
        notificationShadeDepthController.scheduleUpdate(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void scheduleUpdate(View view) {
        if (this.updateScheduled) {
            return;
        }
        this.updateScheduled = true;
        this.blurRoot = view;
        this.choreographer.postFrameCallback(this.updateBlurCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean shouldApplyShadeBlur() {
        int state = this.statusBarStateController.getState();
        return (state == 0 || state == 2) && !this.keyguardStateController.isKeyguardFadingAway();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(@NotNull FileDescriptor fd, @NotNull PrintWriter pw, @NotNull String[] args) {
        Intrinsics.checkNotNullParameter(fd, "fd");
        Intrinsics.checkNotNullParameter(pw, "pw");
        Intrinsics.checkNotNullParameter(args, "args");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(pw, "  ");
        indentingPrintWriter.println("StatusBarWindowBlurController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("shadeExpansion: ", Float.valueOf(getShadeExpansion())));
        indentingPrintWriter.println(Intrinsics.stringPlus("shouldApplyShaeBlur: ", Boolean.valueOf(shouldApplyShadeBlur())));
        indentingPrintWriter.println(Intrinsics.stringPlus("shadeAnimation: ", Float.valueOf(getShadeAnimation().getRadius())));
        indentingPrintWriter.println(Intrinsics.stringPlus("brightnessMirrorRadius: ", Float.valueOf(getBrightnessMirrorSpring().getRadius())));
        indentingPrintWriter.println(Intrinsics.stringPlus("wakeAndUnlockBlur: ", Float.valueOf(this.wakeAndUnlockBlurRadius)));
        indentingPrintWriter.println(Intrinsics.stringPlus("blursDisabledForAppLaunch: ", Boolean.valueOf(getBlursDisabledForAppLaunch())));
        indentingPrintWriter.println(Intrinsics.stringPlus("qsPanelExpansion: ", Float.valueOf(getQsPanelExpansion())));
        indentingPrintWriter.println(Intrinsics.stringPlus("transitionToFullShadeProgress: ", Float.valueOf(getTransitionToFullShadeProgress())));
        indentingPrintWriter.println(Intrinsics.stringPlus("lastAppliedBlur: ", Integer.valueOf(this.lastAppliedBlur)));
    }

    /* compiled from: NotificationShadeDepthController.kt */
    /* loaded from: classes.dex */
    public final class DepthAnimation {
        private int pendingRadius = -1;
        private float radius;
        @NotNull
        private SpringAnimation springAnimation;
        final /* synthetic */ NotificationShadeDepthController this$0;
        @Nullable
        private View view;

        public DepthAnimation(final NotificationShadeDepthController this$0) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            this.this$0 = this$0;
            SpringAnimation springAnimation = new SpringAnimation(this, new FloatPropertyCompat<DepthAnimation>() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController$DepthAnimation$springAnimation$1
                /* JADX INFO: Access modifiers changed from: package-private */
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super("blurRadius");
                }

                @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
                public void setValue(@Nullable NotificationShadeDepthController.DepthAnimation depthAnimation, float f) {
                    View view;
                    NotificationShadeDepthController.DepthAnimation.this.setRadius(f);
                    NotificationShadeDepthController notificationShadeDepthController = this$0;
                    view = NotificationShadeDepthController.DepthAnimation.this.view;
                    notificationShadeDepthController.scheduleUpdate(view);
                }

                @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
                public float getValue(@Nullable NotificationShadeDepthController.DepthAnimation depthAnimation) {
                    return NotificationShadeDepthController.DepthAnimation.this.getRadius();
                }
            });
            this.springAnimation = springAnimation;
            springAnimation.setSpring(new SpringForce(0.0f));
            this.springAnimation.getSpring().setDampingRatio(1.0f);
            this.springAnimation.getSpring().setStiffness(10000.0f);
            this.springAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: com.android.systemui.statusbar.NotificationShadeDepthController.DepthAnimation.1
                @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    DepthAnimation.this.pendingRadius = -1;
                }
            });
        }

        public final float getRadius() {
            return this.radius;
        }

        public final void setRadius(float f) {
            this.radius = f;
        }

        public final float getRatio() {
            return this.this$0.blurUtils.ratioOfBlurRadius(this.radius);
        }

        public static /* synthetic */ void animateTo$default(DepthAnimation depthAnimation, int i, View view, int i2, Object obj) {
            if ((i2 & 2) != 0) {
                view = null;
            }
            depthAnimation.animateTo(i, view);
        }

        public final void animateTo(int i, @Nullable View view) {
            if (this.pendingRadius != i || !Intrinsics.areEqual(this.view, view)) {
                this.view = view;
                this.pendingRadius = i;
                this.springAnimation.animateToFinalPosition(i);
            }
        }

        public final void finishIfRunning() {
            if (this.springAnimation.isRunning()) {
                this.springAnimation.skipToEnd();
            }
        }

        public final void setStiffness(float f) {
            this.springAnimation.getSpring().setStiffness(f);
        }

        public final void setDampingRatio(float f) {
            this.springAnimation.getSpring().setDampingRatio(f);
        }

        public final void setStartVelocity(float f) {
            this.springAnimation.setStartVelocity(f);
        }
    }
}
