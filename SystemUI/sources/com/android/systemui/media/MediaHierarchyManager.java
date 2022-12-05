package com.android.systemui.media;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.ViewOverlay;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.Objects;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaHierarchyManager.kt */
/* loaded from: classes.dex */
public final class MediaHierarchyManager {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private boolean animationPending;
    private float animationStartAlpha;
    private float animationStartCrossFadeProgress;
    private ValueAnimator animator;
    @NotNull
    private final KeyguardBypassController bypassController;
    private boolean collapsingShadeFromQS;
    @NotNull
    private final Context context;
    private int distanceForFullShadeTransition;
    private boolean dozeAnimationRunning;
    private float fullShadeTransitionProgress;
    private boolean fullyAwake;
    private boolean goingToSleep;
    private boolean isCrossFadeAnimatorRunning;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    @NotNull
    private final MediaCarouselController mediaCarouselController;
    @NotNull
    private final NotificationLockscreenUserManager notifLockscreenUserManager;
    private boolean qsExpanded;
    private float qsExpansion;
    @Nullable
    private ViewGroupOverlay rootOverlay;
    @Nullable
    private View rootView;
    @NotNull
    private final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    @NotNull
    private final SysuiStatusBarStateController statusBarStateController;
    private int statusbarState;
    @NotNull
    private Rect currentBounds = new Rect();
    @NotNull
    private Rect animationStartBounds = new Rect();
    private int crossFadeAnimationStartLocation = -1;
    private int crossFadeAnimationEndLocation = -1;
    @NotNull
    private Rect targetBounds = new Rect();
    @NotNull
    private final MediaHost[] mediaHosts = new MediaHost[3];
    private int previousLocation = -1;
    private int desiredLocation = -1;
    private int currentAttachmentLocation = -1;
    @NotNull
    private final Runnable startAnimation = new Runnable() { // from class: com.android.systemui.media.MediaHierarchyManager$startAnimation$1
        @Override // java.lang.Runnable
        public final void run() {
            ValueAnimator valueAnimator;
            valueAnimator = MediaHierarchyManager.this.animator;
            valueAnimator.start();
        }
    };
    private float animationCrossFadeProgress = 1.0f;
    private float carouselAlpha = 1.0f;

    /* JADX INFO: Access modifiers changed from: private */
    public final float calculateAlphaFromCrossFade(float f, boolean z) {
        if (f <= 0.5f) {
            return 1.0f - (f / 0.5f);
        }
        if (!z) {
            return (f - 0.5f) / 0.5f;
        }
        return 1.0f;
    }

    public MediaHierarchyManager(@NotNull Context context, @NotNull SysuiStatusBarStateController statusBarStateController, @NotNull KeyguardStateController keyguardStateController, @NotNull KeyguardBypassController bypassController, @NotNull MediaCarouselController mediaCarouselController, @NotNull NotificationLockscreenUserManager notifLockscreenUserManager, @NotNull ConfigurationController configurationController, @NotNull WakefulnessLifecycle wakefulnessLifecycle, @NotNull StatusBarKeyguardViewManager statusBarKeyguardViewManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(bypassController, "bypassController");
        Intrinsics.checkNotNullParameter(mediaCarouselController, "mediaCarouselController");
        Intrinsics.checkNotNullParameter(notifLockscreenUserManager, "notifLockscreenUserManager");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(statusBarKeyguardViewManager, "statusBarKeyguardViewManager");
        this.context = context;
        this.statusBarStateController = statusBarStateController;
        this.keyguardStateController = keyguardStateController;
        this.bypassController = bypassController;
        this.mediaCarouselController = mediaCarouselController;
        this.notifLockscreenUserManager = notifLockscreenUserManager;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.statusbarState = statusBarStateController.getState();
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.media.MediaHierarchyManager$animator$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                boolean z;
                float f;
                float lerp;
                Rect rect;
                Rect rect2;
                Rect rect3;
                Rect rect4;
                float f2;
                float f3;
                float f4;
                MediaHierarchyManager.this.updateTargetState();
                float animatedFraction = ofFloat.getAnimatedFraction();
                z = MediaHierarchyManager.this.isCrossFadeAnimatorRunning;
                if (!z) {
                    f = MediaHierarchyManager.this.animationStartAlpha;
                    lerp = MathUtils.lerp(f, 1.0f, ofFloat.getAnimatedFraction());
                } else {
                    MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                    f2 = mediaHierarchyManager.animationStartCrossFadeProgress;
                    mediaHierarchyManager.animationCrossFadeProgress = MathUtils.lerp(f2, 1.0f, ofFloat.getAnimatedFraction());
                    f3 = MediaHierarchyManager.this.animationCrossFadeProgress;
                    animatedFraction = f3 < 0.5f ? 0.0f : 1.0f;
                    MediaHierarchyManager mediaHierarchyManager2 = MediaHierarchyManager.this;
                    f4 = mediaHierarchyManager2.animationCrossFadeProgress;
                    lerp = mediaHierarchyManager2.calculateAlphaFromCrossFade(f4, false);
                }
                float f5 = lerp;
                MediaHierarchyManager mediaHierarchyManager3 = MediaHierarchyManager.this;
                rect = mediaHierarchyManager3.animationStartBounds;
                rect2 = MediaHierarchyManager.this.targetBounds;
                rect3 = MediaHierarchyManager.this.currentBounds;
                mediaHierarchyManager3.interpolateBounds(rect, rect2, animatedFraction, rect3);
                MediaHierarchyManager mediaHierarchyManager4 = MediaHierarchyManager.this;
                rect4 = mediaHierarchyManager4.currentBounds;
                MediaHierarchyManager.applyState$default(mediaHierarchyManager4, rect4, f5, false, 4, null);
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.media.MediaHierarchyManager$animator$1$2
            private boolean cancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(@Nullable Animator animator) {
                View view;
                Runnable runnable;
                this.cancelled = true;
                MediaHierarchyManager.this.animationPending = false;
                view = MediaHierarchyManager.this.rootView;
                if (view == null) {
                    return;
                }
                runnable = MediaHierarchyManager.this.startAnimation;
                view.removeCallbacks(runnable);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@Nullable Animator animator) {
                MediaHierarchyManager.this.isCrossFadeAnimatorRunning = false;
                if (!this.cancelled) {
                    MediaHierarchyManager.this.applyTargetStateIfNotAnimating();
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(@Nullable Animator animator) {
                this.cancelled = false;
                MediaHierarchyManager.this.animationPending = false;
            }
        });
        Unit unit = Unit.INSTANCE;
        this.animator = ofFloat;
        updateConfiguration();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.media.MediaHierarchyManager.1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onDensityOrFontScaleChanged() {
                MediaHierarchyManager.this.updateConfiguration();
            }
        });
        statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.media.MediaHierarchyManager.2
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStatePreChange(int i, int i2) {
                MediaHierarchyManager.this.statusbarState = i2;
                MediaHierarchyManager.updateDesiredLocation$default(MediaHierarchyManager.this, false, false, 3, null);
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                MediaHierarchyManager.this.updateTargetState();
                if (i == 2 && MediaHierarchyManager.this.isLockScreenShadeVisibleToUser()) {
                    MediaHierarchyManager.this.mediaCarouselController.logSmartspaceImpression(MediaHierarchyManager.this.getQsExpanded());
                }
                MediaHierarchyManager.this.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(MediaHierarchyManager.this.isVisibleToUser());
            }

            /* JADX WARN: Code restructure failed: missing block: B:8:0x0017, code lost:
                if ((r3 == 1.0f) == false) goto L9;
             */
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public void onDozeAmountChanged(float f, float f2) {
                MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                boolean z = true;
                if (!(f == 0.0f)) {
                }
                z = false;
                mediaHierarchyManager.setDozeAnimationRunning(z);
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onDozingChanged(boolean z) {
                if (!z) {
                    MediaHierarchyManager.this.setDozeAnimationRunning(false);
                    if (MediaHierarchyManager.this.isLockScreenVisibleToUser()) {
                        MediaHierarchyManager.this.mediaCarouselController.logSmartspaceImpression(MediaHierarchyManager.this.getQsExpanded());
                    }
                } else {
                    MediaHierarchyManager.updateDesiredLocation$default(MediaHierarchyManager.this, false, false, 3, null);
                    MediaHierarchyManager.this.setQsExpanded(false);
                    MediaHierarchyManager.this.closeGuts();
                }
                MediaHierarchyManager.this.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(MediaHierarchyManager.this.isVisibleToUser());
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onExpandedChanged(boolean z) {
                if (MediaHierarchyManager.this.isHomeScreenShadeVisibleToUser()) {
                    MediaHierarchyManager.this.mediaCarouselController.logSmartspaceImpression(MediaHierarchyManager.this.getQsExpanded());
                }
                MediaHierarchyManager.this.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(MediaHierarchyManager.this.isVisibleToUser());
            }
        });
        wakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.media.MediaHierarchyManager.3
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedGoingToSleep() {
                MediaHierarchyManager.this.setGoingToSleep(false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedGoingToSleep() {
                MediaHierarchyManager.this.setGoingToSleep(true);
                MediaHierarchyManager.this.setFullyAwake(false);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedWakingUp() {
                MediaHierarchyManager.this.setGoingToSleep(false);
                MediaHierarchyManager.this.setFullyAwake(true);
            }

            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onStartedWakingUp() {
                MediaHierarchyManager.this.setGoingToSleep(false);
            }
        });
        mediaCarouselController.setUpdateUserVisibility(new AnonymousClass4());
    }

    private final ViewGroup getMediaFrame() {
        return this.mediaCarouselController.getMediaFrame();
    }

    private final boolean getHasActiveMedia() {
        MediaHost mediaHost = this.mediaHosts[1];
        return Intrinsics.areEqual(mediaHost == null ? null : Boolean.valueOf(mediaHost.getVisible()), Boolean.TRUE);
    }

    public final void setQsExpansion(float f) {
        if (!(this.qsExpansion == f)) {
            this.qsExpansion = f;
            updateDesiredLocation$default(this, false, false, 3, null);
            if (getQSTransformationProgress() < 0.0f) {
                return;
            }
            updateTargetState();
            applyTargetStateIfNotAnimating();
        }
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        if (this.qsExpanded != z) {
            this.qsExpanded = z;
            this.mediaCarouselController.getMediaCarouselScrollHandler().setQsExpanded(z);
        }
        if (z && (isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser())) {
            this.mediaCarouselController.logSmartspaceImpression(z);
        }
        this.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(isVisibleToUser());
    }

    private final void setFullShadeTransitionProgress(float f) {
        if (this.fullShadeTransitionProgress == f) {
            return;
        }
        this.fullShadeTransitionProgress = f;
        if (this.bypassController.getBypassEnabled() || this.statusbarState != 1) {
            return;
        }
        updateDesiredLocation$default(this, isCurrentlyFading(), false, 2, null);
        if (f < 0.0f) {
            return;
        }
        updateTargetState();
        setCarouselAlpha(calculateAlphaFromCrossFade(this.fullShadeTransitionProgress, true));
        applyTargetStateIfNotAnimating();
    }

    private final boolean isTransitioningToFullShade() {
        return !((this.fullShadeTransitionProgress > 0.0f ? 1 : (this.fullShadeTransitionProgress == 0.0f ? 0 : -1)) == 0) && !this.bypassController.getBypassEnabled() && this.statusbarState == 1;
    }

    public final void setTransitionToFullShadeAmount(float f) {
        setFullShadeTransitionProgress(MathUtils.saturate(f / this.distanceForFullShadeTransition));
    }

    public final void setCollapsingShadeFromQS(boolean z) {
        if (this.collapsingShadeFromQS != z) {
            this.collapsingShadeFromQS = z;
            updateDesiredLocation$default(this, true, false, 2, null);
        }
    }

    private final boolean getBlockLocationChanges() {
        return this.goingToSleep || this.dozeAnimationRunning;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setGoingToSleep(boolean z) {
        if (this.goingToSleep != z) {
            this.goingToSleep = z;
            if (z) {
                return;
            }
            updateDesiredLocation$default(this, false, false, 3, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setFullyAwake(boolean z) {
        if (this.fullyAwake != z) {
            this.fullyAwake = z;
            if (!z) {
                return;
            }
            updateDesiredLocation$default(this, true, false, 2, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setDozeAnimationRunning(boolean z) {
        if (this.dozeAnimationRunning != z) {
            this.dozeAnimationRunning = z;
            if (z) {
                return;
            }
            updateDesiredLocation$default(this, false, false, 3, null);
        }
    }

    private final void setCarouselAlpha(float f) {
        if (this.carouselAlpha == f) {
            return;
        }
        this.carouselAlpha = f;
        CrossFadeHelper.fadeIn(getMediaFrame(), f);
    }

    /* compiled from: MediaHierarchyManager.kt */
    /* renamed from: com.android.systemui.media.MediaHierarchyManager$4  reason: invalid class name */
    /* loaded from: classes.dex */
    static final class AnonymousClass4 extends Lambda implements Function0<Unit> {
        AnonymousClass4() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1951invoke() {
            mo1951invoke();
            return Unit.INSTANCE;
        }

        @Override // kotlin.jvm.functions.Function0
        /* renamed from: invoke  reason: collision with other method in class */
        public final void mo1951invoke() {
            MediaHierarchyManager.this.mediaCarouselController.getMediaCarouselScrollHandler().setVisibleToUser(MediaHierarchyManager.this.isVisibleToUser());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateConfiguration() {
        this.distanceForFullShadeTransition = this.context.getResources().getDimensionPixelSize(R$dimen.lockscreen_shade_media_transition_distance);
    }

    @NotNull
    public final UniqueObjectHostView register(@NotNull MediaHost mediaObject) {
        Intrinsics.checkNotNullParameter(mediaObject, "mediaObject");
        UniqueObjectHostView createUniqueObjectHost = createUniqueObjectHost();
        mediaObject.setHostView(createUniqueObjectHost);
        mediaObject.addVisibilityChangeListener(new MediaHierarchyManager$register$1(mediaObject, this));
        this.mediaHosts[mediaObject.getLocation()] = mediaObject;
        if (mediaObject.getLocation() == this.desiredLocation) {
            this.desiredLocation = -1;
        }
        if (mediaObject.getLocation() == this.currentAttachmentLocation) {
            this.currentAttachmentLocation = -1;
        }
        updateDesiredLocation$default(this, false, false, 3, null);
        return createUniqueObjectHost;
    }

    public final void closeGuts() {
        MediaCarouselController.closeGuts$default(this.mediaCarouselController, false, 1, null);
    }

    private final UniqueObjectHostView createUniqueObjectHost() {
        final UniqueObjectHostView uniqueObjectHostView = new UniqueObjectHostView(this.context);
        uniqueObjectHostView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.media.MediaHierarchyManager$createUniqueObjectHost$1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(@Nullable View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(@Nullable View view) {
                ViewGroupOverlay viewGroupOverlay;
                View view2;
                viewGroupOverlay = MediaHierarchyManager.this.rootOverlay;
                if (viewGroupOverlay == null) {
                    MediaHierarchyManager.this.rootView = uniqueObjectHostView.getViewRootImpl().getView();
                    MediaHierarchyManager mediaHierarchyManager = MediaHierarchyManager.this;
                    view2 = mediaHierarchyManager.rootView;
                    Intrinsics.checkNotNull(view2);
                    ViewOverlay overlay = view2.getOverlay();
                    Objects.requireNonNull(overlay, "null cannot be cast to non-null type android.view.ViewGroupOverlay");
                    mediaHierarchyManager.rootOverlay = (ViewGroupOverlay) overlay;
                }
                uniqueObjectHostView.removeOnAttachStateChangeListener(this);
            }
        });
        return uniqueObjectHostView;
    }

    static /* synthetic */ void updateDesiredLocation$default(MediaHierarchyManager mediaHierarchyManager, boolean z, boolean z2, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        if ((i & 2) != 0) {
            z2 = false;
        }
        mediaHierarchyManager.updateDesiredLocation(z, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateDesiredLocation(boolean z, boolean z2) {
        int i;
        int calculateLocation = calculateLocation();
        int i2 = this.desiredLocation;
        if (calculateLocation != i2 || z2) {
            boolean z3 = false;
            if (i2 >= 0 && calculateLocation != i2) {
                this.previousLocation = i2;
            } else if (z2) {
                boolean z4 = !this.bypassController.getBypassEnabled() && ((i = this.statusbarState) == 1 || i == 3);
                if (calculateLocation == 0 && this.previousLocation == 2 && !z4) {
                    this.previousLocation = 1;
                }
            }
            boolean z5 = this.desiredLocation == -1;
            this.desiredLocation = calculateLocation;
            boolean z6 = !z && shouldAnimateTransition(calculateLocation, this.previousLocation);
            Pair<Long, Long> animationParams = getAnimationParams(this.previousLocation, calculateLocation);
            long longValue = animationParams.component1().longValue();
            long longValue2 = animationParams.component2().longValue();
            MediaHost host = getHost(calculateLocation);
            if (calculateTransformationType() == 1) {
                z3 = true;
            }
            if (!z3 || isCurrentlyInGuidedTransformation() || !z6) {
                this.mediaCarouselController.onDesiredLocationChanged(calculateLocation, host, z6, longValue, longValue2);
            }
            performTransitionToNewLocation(z5, z6);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:35:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void performTransitionToNewLocation(boolean z, boolean z2) {
        int i;
        View view;
        if (this.previousLocation < 0 || z) {
            cancelAnimationAndApplyDesiredState();
            return;
        }
        MediaHost host = getHost(this.desiredLocation);
        MediaHost host2 = getHost(this.previousLocation);
        if (host == null || host2 == null) {
            cancelAnimationAndApplyDesiredState();
            return;
        }
        updateTargetState();
        if (isCurrentlyInGuidedTransformation()) {
            applyTargetStateIfNotAnimating();
        } else if (z2) {
            boolean z3 = this.isCrossFadeAnimatorRunning;
            float f = this.animationCrossFadeProgress;
            this.animator.cancel();
            if (this.currentAttachmentLocation != this.previousLocation || !host2.getHostView().isAttachedToWindow()) {
                this.animationStartBounds.set(this.currentBounds);
            } else {
                this.animationStartBounds.set(host2.getCurrentBounds());
            }
            boolean z4 = calculateTransformationType() == 1;
            int i2 = this.previousLocation;
            if (z3) {
                if (this.currentAttachmentLocation != this.crossFadeAnimationEndLocation) {
                    i = this.crossFadeAnimationStartLocation;
                    if (i == this.desiredLocation) {
                        f = 1.0f - f;
                    } else {
                        z4 = true;
                    }
                    this.isCrossFadeAnimatorRunning = z4;
                    this.crossFadeAnimationStartLocation = i;
                    int i3 = this.desiredLocation;
                    this.crossFadeAnimationEndLocation = i3;
                    this.animationStartAlpha = this.carouselAlpha;
                    this.animationStartCrossFadeProgress = f;
                    adjustAnimatorForTransition(i3, i2);
                    if (!this.animationPending) {
                    }
                } else {
                    if (z4) {
                        f = 1.0f - f;
                        i = i2;
                        this.isCrossFadeAnimatorRunning = z4;
                        this.crossFadeAnimationStartLocation = i;
                        int i32 = this.desiredLocation;
                        this.crossFadeAnimationEndLocation = i32;
                        this.animationStartAlpha = this.carouselAlpha;
                        this.animationStartCrossFadeProgress = f;
                        adjustAnimatorForTransition(i32, i2);
                        if (!this.animationPending || (view = this.rootView) == null) {
                        }
                        this.animationPending = true;
                        view.postOnAnimation(this.startAnimation);
                        return;
                    }
                    f = 0.0f;
                    i = i2;
                    this.isCrossFadeAnimatorRunning = z4;
                    this.crossFadeAnimationStartLocation = i;
                    int i322 = this.desiredLocation;
                    this.crossFadeAnimationEndLocation = i322;
                    this.animationStartAlpha = this.carouselAlpha;
                    this.animationStartCrossFadeProgress = f;
                    adjustAnimatorForTransition(i322, i2);
                    if (!this.animationPending) {
                    }
                }
            } else {
                if (z4) {
                    f = (1.0f - this.carouselAlpha) / 2.0f;
                    i = i2;
                    this.isCrossFadeAnimatorRunning = z4;
                    this.crossFadeAnimationStartLocation = i;
                    int i3222 = this.desiredLocation;
                    this.crossFadeAnimationEndLocation = i3222;
                    this.animationStartAlpha = this.carouselAlpha;
                    this.animationStartCrossFadeProgress = f;
                    adjustAnimatorForTransition(i3222, i2);
                    if (!this.animationPending) {
                    }
                }
                f = 0.0f;
                i = i2;
                this.isCrossFadeAnimatorRunning = z4;
                this.crossFadeAnimationStartLocation = i;
                int i32222 = this.desiredLocation;
                this.crossFadeAnimationEndLocation = i32222;
                this.animationStartAlpha = this.carouselAlpha;
                this.animationStartCrossFadeProgress = f;
                adjustAnimatorForTransition(i32222, i2);
                if (!this.animationPending) {
                }
            }
        } else {
            cancelAnimationAndApplyDesiredState();
        }
    }

    private final boolean shouldAnimateTransition(int i, int i2) {
        if (isCurrentlyInGuidedTransformation()) {
            return false;
        }
        if (i2 == 2 && this.desiredLocation == 1 && this.statusbarState == 0) {
            return false;
        }
        if (i == 1 && i2 == 2 && (this.statusBarStateController.leaveOpenOnKeyguardHide() || this.statusbarState == 2)) {
            return true;
        }
        if (this.statusbarState == 1 && (i == 2 || i2 == 2)) {
            return false;
        }
        return MediaHierarchyManagerKt.isShownNotFaded(getMediaFrame()) || this.animator.isRunning() || this.animationPending;
    }

    private final void adjustAnimatorForTransition(int i, int i2) {
        Pair<Long, Long> animationParams = getAnimationParams(i2, i);
        long longValue = animationParams.component1().longValue();
        long longValue2 = animationParams.component2().longValue();
        ValueAnimator valueAnimator = this.animator;
        valueAnimator.setDuration(longValue);
        valueAnimator.setStartDelay(longValue2);
    }

    private final Pair<Long, Long> getAnimationParams(int i, int i2) {
        long j;
        long j2 = 0;
        if (i == 2 && i2 == 1) {
            if (this.statusbarState == 0 && this.keyguardStateController.isKeyguardFadingAway()) {
                j2 = this.keyguardStateController.getKeyguardFadingAwayDelay();
            }
            j = 224;
        } else {
            j = (i == 1 && i2 == 2) ? 464L : 200L;
        }
        return TuplesKt.to(Long.valueOf(j), Long.valueOf(j2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void applyTargetStateIfNotAnimating() {
        if (!this.animator.isRunning()) {
            applyState$default(this, this.targetBounds, this.carouselAlpha, false, 4, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateTargetState() {
        if (isCurrentlyInGuidedTransformation() && !isCurrentlyFading()) {
            float transformationProgress = getTransformationProgress();
            MediaHost host = getHost(this.desiredLocation);
            Intrinsics.checkNotNull(host);
            MediaHost host2 = getHost(this.previousLocation);
            Intrinsics.checkNotNull(host2);
            if (!host.getVisible()) {
                host = host2;
            } else if (!host2.getVisible()) {
                host2 = host;
            }
            this.targetBounds = interpolateBounds$default(this, host2.getCurrentBounds(), host.getCurrentBounds(), transformationProgress, null, 8, null);
            return;
        }
        MediaHost host3 = getHost(this.desiredLocation);
        Rect currentBounds = host3 == null ? null : host3.getCurrentBounds();
        if (currentBounds == null) {
            return;
        }
        this.targetBounds.set(currentBounds);
    }

    static /* synthetic */ Rect interpolateBounds$default(MediaHierarchyManager mediaHierarchyManager, Rect rect, Rect rect2, float f, Rect rect3, int i, Object obj) {
        if ((i & 8) != 0) {
            rect3 = null;
        }
        return mediaHierarchyManager.interpolateBounds(rect, rect2, f, rect3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Rect interpolateBounds(Rect rect, Rect rect2, float f, Rect rect3) {
        int lerp = (int) MathUtils.lerp(rect.left, rect2.left, f);
        int lerp2 = (int) MathUtils.lerp(rect.top, rect2.top, f);
        int lerp3 = (int) MathUtils.lerp(rect.right, rect2.right, f);
        int lerp4 = (int) MathUtils.lerp(rect.bottom, rect2.bottom, f);
        if (rect3 == null) {
            rect3 = new Rect();
        }
        rect3.set(lerp, lerp2, lerp3, lerp4);
        return rect3;
    }

    private final boolean isCurrentlyInGuidedTransformation() {
        return getTransformationProgress() >= 0.0f;
    }

    public final int calculateTransformationType() {
        if (isTransitioningToFullShade()) {
            return 1;
        }
        int i = this.previousLocation;
        if ((i == 2 && this.desiredLocation == 0) || (i == 0 && this.desiredLocation == 2)) {
            return 1;
        }
        return (i == 2 && this.desiredLocation == 1) ? 1 : 0;
    }

    private final float getTransformationProgress() {
        float qSTransformationProgress = getQSTransformationProgress();
        if (this.statusbarState == 1 || qSTransformationProgress < 0.0f) {
            if (!isTransitioningToFullShade()) {
                return -1.0f;
            }
            return this.fullShadeTransitionProgress;
        }
        return qSTransformationProgress;
    }

    private final float getQSTransformationProgress() {
        MediaHost host = getHost(this.desiredLocation);
        MediaHost host2 = getHost(this.previousLocation);
        if (getHasActiveMedia()) {
            Integer num = null;
            Integer valueOf = host == null ? null : Integer.valueOf(host.getLocation());
            if (valueOf == null || valueOf.intValue() != 0) {
                return -1.0f;
            }
            if (host2 != null) {
                num = Integer.valueOf(host2.getLocation());
            }
            if (num == null || num.intValue() != 1) {
                return -1.0f;
            }
            if (!host2.getVisible() && this.statusbarState == 1) {
                return -1.0f;
            }
            return this.qsExpansion;
        }
        return -1.0f;
    }

    private final MediaHost getHost(int i) {
        if (i < 0) {
            return null;
        }
        return this.mediaHosts[i];
    }

    private final void cancelAnimationAndApplyDesiredState() {
        this.animator.cancel();
        MediaHost host = getHost(this.desiredLocation);
        if (host == null) {
            return;
        }
        applyState(host.getCurrentBounds(), 1.0f, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void applyState$default(MediaHierarchyManager mediaHierarchyManager, Rect rect, float f, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        mediaHierarchyManager.applyState(rect, f, z);
    }

    private final void applyState(Rect rect, float f, boolean z) {
        this.currentBounds.set(rect);
        float f2 = 1.0f;
        if (!isCurrentlyFading()) {
            f = 1.0f;
        }
        setCarouselAlpha(f);
        boolean z2 = !isCurrentlyInGuidedTransformation() || isCurrentlyFading();
        int i = z2 ? -1 : this.previousLocation;
        if (!z2) {
            f2 = getTransformationProgress();
        }
        this.mediaCarouselController.setCurrentState(i, resolveLocationForFading(), f2, z);
        updateHostAttachment();
        if (this.currentAttachmentLocation == -1000) {
            ViewGroup mediaFrame = getMediaFrame();
            Rect rect2 = this.currentBounds;
            mediaFrame.setLeftTopRightBottom(rect2.left, rect2.top, rect2.right, rect2.bottom);
        }
    }

    private final void updateHostAttachment() {
        UniqueObjectHostView hostView;
        int resolveLocationForFading = resolveLocationForFading();
        boolean z = true;
        boolean z2 = !isCurrentlyFading();
        if (this.isCrossFadeAnimatorRunning) {
            MediaHost host = getHost(resolveLocationForFading);
            Boolean bool = null;
            if (Intrinsics.areEqual(host == null ? null : Boolean.valueOf(host.getVisible()), Boolean.TRUE)) {
                MediaHost host2 = getHost(resolveLocationForFading);
                if (host2 != null && (hostView = host2.getHostView()) != null) {
                    bool = Boolean.valueOf(hostView.isShown());
                }
                if (Intrinsics.areEqual(bool, Boolean.FALSE) && resolveLocationForFading != this.desiredLocation) {
                    z2 = true;
                }
            }
        }
        if (!isTransitionRunning() || this.rootOverlay == null || !z2) {
            z = false;
        }
        if (z) {
            resolveLocationForFading = -1000;
        }
        int i = resolveLocationForFading;
        if (this.currentAttachmentLocation != i) {
            this.currentAttachmentLocation = i;
            ViewGroup viewGroup = (ViewGroup) getMediaFrame().getParent();
            if (viewGroup != null) {
                viewGroup.removeView(getMediaFrame());
            }
            if (z) {
                ViewGroupOverlay viewGroupOverlay = this.rootOverlay;
                Intrinsics.checkNotNull(viewGroupOverlay);
                viewGroupOverlay.add(getMediaFrame());
            } else {
                MediaHost host3 = getHost(i);
                Intrinsics.checkNotNull(host3);
                UniqueObjectHostView hostView2 = host3.getHostView();
                hostView2.addView(getMediaFrame());
                int paddingLeft = hostView2.getPaddingLeft();
                int paddingTop = hostView2.getPaddingTop();
                getMediaFrame().setLeftTopRightBottom(paddingLeft, paddingTop, this.currentBounds.width() + paddingLeft, this.currentBounds.height() + paddingTop);
            }
            if (!this.isCrossFadeAnimatorRunning) {
                return;
            }
            MediaCarouselController.onDesiredLocationChanged$default(this.mediaCarouselController, i, getHost(i), false, 0L, 0L, 24, null);
        }
    }

    private final int resolveLocationForFading() {
        if (this.isCrossFadeAnimatorRunning) {
            if (this.animationCrossFadeProgress > 0.5d || this.previousLocation == -1) {
                return this.crossFadeAnimationEndLocation;
            }
            return this.crossFadeAnimationStartLocation;
        }
        return this.desiredLocation;
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0015, code lost:
        if ((getTransformationProgress() == 1.0f) != false) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final boolean isTransitionRunning() {
        if (isCurrentlyInGuidedTransformation()) {
        }
        if (!this.animator.isRunning() && !this.animationPending) {
            return false;
        }
        return true;
    }

    private final int calculateLocation() {
        int i;
        if (getBlockLocationChanges()) {
            return this.desiredLocation;
        }
        int i2 = 1;
        boolean z = !this.bypassController.getBypassEnabled() && ((i = this.statusbarState) == 1 || i == 3);
        boolean shouldShowLockscreenNotifications = this.notifLockscreenUserManager.shouldShowLockscreenNotifications();
        float f = this.qsExpansion;
        if ((f > 0.0f && !z) || ((f > 0.4f && z) || !getHasActiveMedia())) {
            i2 = 0;
        } else if ((!z || !isTransformingToFullShadeAndInQQS()) && z && shouldShowLockscreenNotifications) {
            i2 = 2;
        }
        if (i2 == 2) {
            MediaHost host = getHost(i2);
            if (!Intrinsics.areEqual(host == null ? null : Boolean.valueOf(host.getVisible()), Boolean.TRUE) && !this.statusBarStateController.isDozing()) {
                return 0;
            }
        }
        if (i2 == 2 && this.desiredLocation == 0 && this.collapsingShadeFromQS) {
            return 0;
        }
        if (i2 != 2 && this.desiredLocation == 2 && !this.fullyAwake) {
            return 2;
        }
        return i2;
    }

    private final boolean isTransformingToFullShadeAndInQQS() {
        return isTransitioningToFullShade() && this.fullShadeTransitionProgress > 0.5f;
    }

    private final boolean isCurrentlyFading() {
        if (isTransitioningToFullShade()) {
            return true;
        }
        return this.isCrossFadeAnimatorRunning;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isVisibleToUser() {
        return isLockScreenVisibleToUser() || isLockScreenShadeVisibleToUser() || isHomeScreenShadeVisibleToUser();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isLockScreenVisibleToUser() {
        return !this.statusBarStateController.isDozing() && !this.statusBarKeyguardViewManager.isBouncerShowing() && this.statusBarStateController.getState() == 1 && this.notifLockscreenUserManager.shouldShowLockscreenNotifications() && this.statusBarStateController.isExpanded() && !this.qsExpanded;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isLockScreenShadeVisibleToUser() {
        if (!this.statusBarStateController.isDozing() && !this.statusBarKeyguardViewManager.isBouncerShowing()) {
            if (this.statusBarStateController.getState() == 2) {
                return true;
            }
            if (this.statusBarStateController.getState() == 1 && this.qsExpanded) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isHomeScreenShadeVisibleToUser() {
        return !this.statusBarStateController.isDozing() && this.statusBarStateController.getState() == 0 && this.statusBarStateController.isExpanded();
    }

    /* compiled from: MediaHierarchyManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
