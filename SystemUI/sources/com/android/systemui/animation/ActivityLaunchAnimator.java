package com.android.systemui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.app.PendingIntent;
import android.app.TaskInfo;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.util.MathUtils;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.systemui.animation.ActivityLaunchAnimator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.math.MathKt__MathJVMKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ActivityLaunchAnimator.kt */
/* loaded from: classes.dex */
public final class ActivityLaunchAnimator {
    private final Interpolator animationInterpolator;
    private final Interpolator animationInterpolatorX;
    @NotNull
    private final Callback callback;
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    public static final PathInterpolator CONTENT_FADE_OUT_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.2f, 1.0f);
    @NotNull
    private static final PathInterpolator WINDOW_FADE_IN_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.6f, 1.0f);
    @NotNull
    private static final PathInterpolator NAV_FADE_IN_INTERPOLATOR = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
    @NotNull
    private static final PathInterpolator NAV_FADE_OUT_INTERPOLATOR = new PathInterpolator(0.2f, 0.0f, 1.0f, 1.0f);
    @NotNull
    private static final PorterDuffXfermode SRC_MODE = new PorterDuffXfermode(PorterDuff.Mode.SRC);
    private final IPackageManager packageManager = AppGlobals.getPackageManager();
    @NotNull
    private final float[] cornerRadii = new float[8];

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        int getBackgroundColor(@NotNull TaskInfo taskInfo);

        void hideKeyguardWithAnimation(@NotNull IRemoteAnimationRunner iRemoteAnimationRunner);

        boolean isOnKeyguard();

        void setBlursDisabledForAppLaunch(boolean z);
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public interface PendingIntentStarter {
        int startPendingIntent(@Nullable RemoteAnimationAdapter remoteAnimationAdapter) throws PendingIntent.CanceledException;
    }

    public static final float getProgress(float f, long j, long j2) {
        return Companion.getProgress(f, j, j2);
    }

    public ActivityLaunchAnimator(@NotNull Callback callback, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        Intrinsics.checkNotNullParameter(context, "context");
        this.callback = callback;
        this.animationInterpolator = AnimationUtils.loadInterpolator(context, R$interpolator.launch_animation_interpolator_y);
        this.animationInterpolatorX = AnimationUtils.loadInterpolator(context, R$interpolator.launch_animation_interpolator_x);
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final float getProgress(float f, long j, long j2) {
            return MathUtils.constrain(((f * ((float) 500)) - ((float) j)) / ((float) j2), 0.0f, 1.0f);
        }
    }

    public final void startIntentWithAnimation(@Nullable Controller controller, boolean z, @Nullable String str, @NotNull Function1<? super RemoteAnimationAdapter, Integer> intentStarter) {
        Intrinsics.checkNotNullParameter(intentStarter, "intentStarter");
        boolean z2 = false;
        RemoteAnimationAdapter remoteAnimationAdapter = null;
        if (controller == null || !z) {
            Log.d("ActivityLaunchAnimator", "Starting intent with no animation");
            intentStarter.mo1949invoke(null);
            if (controller == null) {
                return;
            }
            callOnIntentStartedOnMainThread(controller, false);
            return;
        }
        Log.d("ActivityLaunchAnimator", "Starting intent with a launch animation");
        IRemoteAnimationRunner runner = new Runner(this, controller);
        boolean isOnKeyguard = this.callback.isOnKeyguard();
        if (!isOnKeyguard) {
            remoteAnimationAdapter = new RemoteAnimationAdapter(runner, 500L, 350L);
        }
        if (str != null && remoteAnimationAdapter != null) {
            try {
                ActivityTaskManager.getService().registerRemoteAnimationForNextActivityStart(str, remoteAnimationAdapter);
            } catch (RemoteException e) {
                Log.w("ActivityLaunchAnimator", "Unable to register the remote animation", e);
            }
        }
        int intValue = intentStarter.mo1949invoke(remoteAnimationAdapter).intValue();
        if (intValue == 2 || intValue == 0 || (intValue == 3 && isOnKeyguard)) {
            z2 = true;
        }
        Log.d("ActivityLaunchAnimator", "launchResult=" + intValue + " willAnimate=" + z2 + " isOnKeyguard=" + isOnKeyguard);
        callOnIntentStartedOnMainThread(controller, z2);
        if (!z2) {
            return;
        }
        runner.postTimeout$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib();
        if (!isOnKeyguard) {
            return;
        }
        this.callback.hideKeyguardWithAnimation(runner);
    }

    private final void callOnIntentStartedOnMainThread(final Controller controller, final boolean z) {
        if (!Intrinsics.areEqual(Looper.myLooper(), Looper.getMainLooper())) {
            controller.getLaunchContainer().getContext().getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$callOnIntentStartedOnMainThread$1
                @Override // java.lang.Runnable
                public final void run() {
                    ActivityLaunchAnimator.Controller.this.onIntentStarted(z);
                }
            });
        } else {
            controller.onIntentStarted(z);
        }
    }

    public final void startPendingIntentWithAnimation(@Nullable Controller controller, boolean z, @Nullable String str, @NotNull PendingIntentStarter intentStarter) throws PendingIntent.CanceledException {
        Intrinsics.checkNotNullParameter(intentStarter, "intentStarter");
        startIntentWithAnimation(controller, z, str, new ActivityLaunchAnimator$startPendingIntentWithAnimation$1(intentStarter));
    }

    @VisibleForTesting
    @NotNull
    public final Runner createRunner(@NotNull Controller controller) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        return new Runner(this, controller);
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public interface Controller {
        @NotNull
        public static final Companion Companion = Companion.$$INSTANCE;

        /* compiled from: ActivityLaunchAnimator.kt */
        /* loaded from: classes.dex */
        public static final class DefaultImpls {
            public static void onIntentStarted(@NotNull Controller controller, boolean z) {
                Intrinsics.checkNotNullParameter(controller, "this");
            }

            public static void onLaunchAnimationCancelled(@NotNull Controller controller) {
                Intrinsics.checkNotNullParameter(controller, "this");
            }
        }

        @Nullable
        static Controller fromView(@NotNull View view, @Nullable Integer num) {
            return Companion.fromView(view, num);
        }

        @NotNull
        State createAnimatorState();

        @NotNull
        ViewGroup getLaunchContainer();

        void onIntentStarted(boolean z);

        void onLaunchAnimationCancelled();

        void onLaunchAnimationEnd(boolean z);

        void onLaunchAnimationProgress(@NotNull State state, float f, float f2);

        void onLaunchAnimationStart(boolean z);

        void setLaunchContainer(@NotNull ViewGroup viewGroup);

        /* compiled from: ActivityLaunchAnimator.kt */
        /* loaded from: classes.dex */
        public static final class Companion {
            static final /* synthetic */ Companion $$INSTANCE = new Companion();

            private Companion() {
            }

            @Nullable
            public final Controller fromView(@NotNull View view, @Nullable Integer num) {
                Intrinsics.checkNotNullParameter(view, "view");
                if (!(view.getParent() instanceof ViewGroup)) {
                    Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + view + " is not attached to a ViewGroup", new Exception());
                    return null;
                }
                return new GhostedViewLaunchAnimatorController(view, num);
            }
        }
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public static class State {
        private int bottom;
        private float bottomCornerRadius;
        private int left;
        private int right;
        private final int startBottom;
        private final int startLeft;
        private final int startRight;
        private final int startTop;
        private int top;
        private float topCornerRadius;
        private final int startWidth = getWidth();
        private final int startHeight = getHeight();
        private final float startCenterX = getCenterX();
        private final float startCenterY = getCenterY();
        private boolean visible = true;

        public State(int i, int i2, int i3, int i4, float f, float f2) {
            this.top = i;
            this.bottom = i2;
            this.left = i3;
            this.right = i4;
            this.topCornerRadius = f;
            this.bottomCornerRadius = f2;
            this.startTop = i;
            this.startBottom = i2;
            this.startLeft = i3;
            this.startRight = i4;
        }

        public final int getTop() {
            return this.top;
        }

        public final void setTop(int i) {
            this.top = i;
        }

        public final int getBottom() {
            return this.bottom;
        }

        public final void setBottom(int i) {
            this.bottom = i;
        }

        public final int getLeft() {
            return this.left;
        }

        public final void setLeft(int i) {
            this.left = i;
        }

        public final int getRight() {
            return this.right;
        }

        public final void setRight(int i) {
            this.right = i;
        }

        public final float getTopCornerRadius() {
            return this.topCornerRadius;
        }

        public final void setTopCornerRadius(float f) {
            this.topCornerRadius = f;
        }

        public final float getBottomCornerRadius() {
            return this.bottomCornerRadius;
        }

        public final void setBottomCornerRadius(float f) {
            this.bottomCornerRadius = f;
        }

        public final float getStartCenterX() {
            return this.startCenterX;
        }

        public final float getStartCenterY() {
            return this.startCenterY;
        }

        public final int getWidth() {
            return this.right - this.left;
        }

        public final int getHeight() {
            return this.bottom - this.top;
        }

        public int getTopChange() {
            return this.top - this.startTop;
        }

        public int getBottomChange() {
            return this.bottom - this.startBottom;
        }

        public final int getLeftChange() {
            return this.left - this.startLeft;
        }

        public final int getRightChange() {
            return this.right - this.startRight;
        }

        public final float getWidthRatio() {
            return getWidth() / this.startWidth;
        }

        public final float getHeightRatio() {
            return getHeight() / this.startHeight;
        }

        public final float getCenterX() {
            return this.left + (getWidth() / 2.0f);
        }

        public final float getCenterY() {
            return this.top + (getHeight() / 2.0f);
        }

        public final boolean getVisible() {
            return this.visible;
        }

        public final void setVisible(boolean z) {
            this.visible = z;
        }
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class Runner extends IRemoteAnimationRunner.Stub {
        @Nullable
        private ValueAnimator animator;
        private boolean cancelled;
        private final Context context;
        @NotNull
        private final Controller controller;
        @NotNull
        private final ViewGroup launchContainer;
        final /* synthetic */ ActivityLaunchAnimator this$0;
        private boolean timedOut;
        @NotNull
        private final SyncRtSurfaceTransactionApplier transactionApplier;
        @NotNull
        private final Matrix matrix = new Matrix();
        @NotNull
        private final Matrix invertMatrix = new Matrix();
        @NotNull
        private Rect windowCrop = new Rect();
        @NotNull
        private RectF windowCropF = new RectF();
        @NotNull
        private Runnable onTimeout = new Runnable() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$onTimeout$1
            @Override // java.lang.Runnable
            public final void run() {
                ActivityLaunchAnimator.Runner.this.onAnimationTimedOut();
            }
        };

        public Runner(@NotNull ActivityLaunchAnimator this$0, Controller controller) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(controller, "controller");
            this.this$0 = this$0;
            this.controller = controller;
            ViewGroup launchContainer = controller.getLaunchContainer();
            this.launchContainer = launchContainer;
            this.context = launchContainer.getContext();
            this.transactionApplier = new SyncRtSurfaceTransactionApplier(launchContainer);
        }

        public final void postTimeout$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib() {
            this.launchContainer.postDelayed(this.onTimeout, 1000L);
        }

        private final void removeTimeout() {
            this.launchContainer.removeCallbacks(this.onTimeout);
        }

        public void onAnimationStart(int i, @Nullable final RemoteAnimationTarget[] remoteAnimationTargetArr, @Nullable RemoteAnimationTarget[] remoteAnimationTargetArr2, @Nullable final RemoteAnimationTarget[] remoteAnimationTargetArr3, @Nullable final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            removeTimeout();
            if (this.timedOut) {
                if (iRemoteAnimationFinishedCallback == null) {
                    return;
                }
                invoke(iRemoteAnimationFinishedCallback);
            } else if (this.cancelled) {
            } else {
                this.context.getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$onAnimationStart$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ActivityLaunchAnimator.Runner.this.startAnimation(remoteAnimationTargetArr, remoteAnimationTargetArr3, iRemoteAnimationFinishedCallback);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void startAnimation(RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            RemoteAnimationTarget remoteAnimationTarget;
            final RemoteAnimationTarget remoteAnimationTarget2;
            Log.d("ActivityLaunchAnimator", "Remote animation started");
            if (remoteAnimationTargetArr != null) {
                for (RemoteAnimationTarget remoteAnimationTarget3 : remoteAnimationTargetArr) {
                    if (remoteAnimationTarget3.mode == 0) {
                        remoteAnimationTarget = remoteAnimationTarget3;
                        break;
                    }
                }
            }
            remoteAnimationTarget = null;
            if (remoteAnimationTarget == null) {
                Log.d("ActivityLaunchAnimator", "Aborting the animation as no window is opening");
                removeTimeout();
                if (iRemoteAnimationFinishedCallback != null) {
                    invoke(iRemoteAnimationFinishedCallback);
                }
                this.controller.onLaunchAnimationCancelled();
                return;
            }
            if (remoteAnimationTargetArr2 != null) {
                for (RemoteAnimationTarget remoteAnimationTarget4 : remoteAnimationTargetArr2) {
                    if (remoteAnimationTarget4.windowType == 2019) {
                        remoteAnimationTarget2 = remoteAnimationTarget4;
                        break;
                    }
                }
            }
            remoteAnimationTarget2 = null;
            final State createAnimatorState = this.controller.createAnimatorState();
            final int top = createAnimatorState.getTop();
            final int bottom = createAnimatorState.getBottom();
            final int left = createAnimatorState.getLeft();
            final int right = createAnimatorState.getRight();
            final float f = (left + right) / 2.0f;
            final int i = right - left;
            final float topCornerRadius = createAnimatorState.getTopCornerRadius();
            final float bottomCornerRadius = createAnimatorState.getBottomCornerRadius();
            Rect rect = remoteAnimationTarget.screenSpaceBounds;
            final int i2 = rect.top;
            final int i3 = rect.bottom;
            int i4 = rect.left;
            int i5 = rect.right;
            final float f2 = (i4 + i5) / 2.0f;
            final int i6 = i5 - i4;
            int[] locationOnScreen = this.launchContainer.getLocationOnScreen();
            final boolean z = i2 <= locationOnScreen[1] && i3 >= locationOnScreen[1] + this.launchContainer.getHeight() && i4 <= locationOnScreen[0] && i5 >= locationOnScreen[0] + this.launchContainer.getWidth();
            final float windowCornerRadius = z ? ScreenDecorationsUtils.getWindowCornerRadius(this.context.getResources()) : 0.0f;
            Callback callback = this.this$0.callback;
            ActivityManager.RunningTaskInfo runningTaskInfo = remoteAnimationTarget.taskInfo;
            Intrinsics.checkNotNullExpressionValue(runningTaskInfo, "window.taskInfo");
            int backgroundColor = callback.getBackgroundColor(runningTaskInfo);
            final GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setColor(backgroundColor);
            gradientDrawable.setAlpha(0);
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.animator = ofFloat;
            ofFloat.setDuration(500L);
            ofFloat.setInterpolator(Interpolators.LINEAR);
            final ViewGroupOverlay overlay = this.launchContainer.getOverlay();
            final ActivityLaunchAnimator activityLaunchAnimator = this.this$0;
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$startAnimation$1
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(@Nullable Animator animator, boolean z2) {
                    ActivityLaunchAnimator.Controller controller;
                    Log.d("ActivityLaunchAnimator", "Animation started");
                    ActivityLaunchAnimator.this.callback.setBlursDisabledForAppLaunch(true);
                    controller = this.controller;
                    controller.onLaunchAnimationStart(z);
                    overlay.add(gradientDrawable);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(@Nullable Animator animator) {
                    ActivityLaunchAnimator.Controller controller;
                    Log.d("ActivityLaunchAnimator", "Animation ended");
                    ActivityLaunchAnimator.this.callback.setBlursDisabledForAppLaunch(false);
                    IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback2 = iRemoteAnimationFinishedCallback;
                    if (iRemoteAnimationFinishedCallback2 != null) {
                        this.invoke(iRemoteAnimationFinishedCallback2);
                    }
                    controller = this.controller;
                    controller.onLaunchAnimationEnd(z);
                    overlay.remove(gradientDrawable);
                }
            });
            final ActivityLaunchAnimator activityLaunchAnimator2 = this.this$0;
            final RemoteAnimationTarget remoteAnimationTarget5 = remoteAnimationTarget;
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$startAnimation$2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    boolean z2;
                    Interpolator interpolator;
                    Interpolator interpolator2;
                    float lerp;
                    float lerp2;
                    int roundToInt;
                    float lerp3;
                    int roundToInt2;
                    int roundToInt3;
                    int roundToInt4;
                    ActivityLaunchAnimator.Controller controller;
                    z2 = ActivityLaunchAnimator.Runner.this.cancelled;
                    if (z2) {
                        return;
                    }
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    interpolator = activityLaunchAnimator2.animationInterpolator;
                    float interpolation = interpolator.getInterpolation(animatedFraction);
                    interpolator2 = activityLaunchAnimator2.animationInterpolatorX;
                    float lerp4 = MathUtils.lerp(f, f2, interpolator2.getInterpolation(animatedFraction));
                    lerp = ActivityLaunchAnimator.Runner.this.lerp(i, i6, interpolation);
                    float f3 = lerp / 2;
                    ActivityLaunchAnimator.State state = createAnimatorState;
                    lerp2 = ActivityLaunchAnimator.Runner.this.lerp(top, i2, interpolation);
                    roundToInt = MathKt__MathJVMKt.roundToInt(lerp2);
                    state.setTop(roundToInt);
                    ActivityLaunchAnimator.State state2 = createAnimatorState;
                    lerp3 = ActivityLaunchAnimator.Runner.this.lerp(bottom, i3, interpolation);
                    roundToInt2 = MathKt__MathJVMKt.roundToInt(lerp3);
                    state2.setBottom(roundToInt2);
                    ActivityLaunchAnimator.State state3 = createAnimatorState;
                    roundToInt3 = MathKt__MathJVMKt.roundToInt(lerp4 - f3);
                    state3.setLeft(roundToInt3);
                    ActivityLaunchAnimator.State state4 = createAnimatorState;
                    roundToInt4 = MathKt__MathJVMKt.roundToInt(lerp4 + f3);
                    state4.setRight(roundToInt4);
                    createAnimatorState.setTopCornerRadius(MathUtils.lerp(topCornerRadius, windowCornerRadius, interpolation));
                    createAnimatorState.setBottomCornerRadius(MathUtils.lerp(bottomCornerRadius, windowCornerRadius, interpolation));
                    createAnimatorState.setVisible(ActivityLaunchAnimator.Companion.getProgress(animatedFraction, 0L, 150L) < 1.0f);
                    ActivityLaunchAnimator.Runner.this.applyStateToWindow(remoteAnimationTarget5, createAnimatorState);
                    ActivityLaunchAnimator.Runner.this.applyStateToWindowBackgroundLayer(gradientDrawable, createAnimatorState, animatedFraction);
                    RemoteAnimationTarget remoteAnimationTarget6 = remoteAnimationTarget2;
                    if (remoteAnimationTarget6 != null) {
                        ActivityLaunchAnimator.Runner.this.applyStateToNavigationBar(remoteAnimationTarget6, createAnimatorState, animatedFraction);
                    }
                    if (createAnimatorState.getTop() != top && createAnimatorState.getLeft() != left && createAnimatorState.getBottom() != bottom && createAnimatorState.getRight() != right) {
                        ActivityLaunchAnimator.State state5 = createAnimatorState;
                        state5.setTop(state5.getTop() + 1);
                        ActivityLaunchAnimator.State state6 = createAnimatorState;
                        state6.setLeft(state6.getLeft() + 1);
                        ActivityLaunchAnimator.State state7 = createAnimatorState;
                        state7.setRight(state7.getRight() - 1);
                        ActivityLaunchAnimator.State state8 = createAnimatorState;
                        state8.setBottom(state8.getBottom() - 1);
                    }
                    controller = ActivityLaunchAnimator.Runner.this.controller;
                    controller.onLaunchAnimationProgress(createAnimatorState, interpolation, animatedFraction);
                }
            });
            ofFloat.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void applyStateToWindow(RemoteAnimationTarget remoteAnimationTarget, State state) {
            int i;
            Rect rect = remoteAnimationTarget.screenSpaceBounds;
            int i2 = rect.left;
            int i3 = rect.right;
            float f = (i2 + i3) / 2.0f;
            int i4 = rect.top;
            float f2 = rect.bottom - i4;
            float max = Math.max(state.getWidth() / (i3 - i2), state.getHeight() / f2);
            this.matrix.reset();
            this.matrix.setScale(max, max, f, (i4 + i) / 2.0f);
            this.matrix.postTranslate(state.getCenterX() - f, (state.getTop() - rect.top) + (((f2 * max) - f2) / 2.0f));
            float left = state.getLeft() - rect.left;
            float top = state.getTop() - rect.top;
            this.windowCropF.set(left, top, state.getWidth() + left, state.getHeight() + top);
            this.matrix.invert(this.invertMatrix);
            this.invertMatrix.mapRect(this.windowCropF);
            this.windowCrop.set(MathKt.roundToInt(this.windowCropF.left), MathKt.roundToInt(this.windowCropF.top), MathKt.roundToInt(this.windowCropF.right), MathKt.roundToInt(this.windowCropF.bottom));
            this.transactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget.leash).withAlpha(1.0f).withMatrix(this.matrix).withWindowCrop(this.windowCrop).withLayer(remoteAnimationTarget.prefixOrderIndex).withCornerRadius(Math.max(state.getTopCornerRadius(), state.getBottomCornerRadius()) / max).withVisibility(true).build()});
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void applyStateToWindowBackgroundLayer(GradientDrawable gradientDrawable, State state, float f) {
            gradientDrawable.setBounds(state.getLeft(), state.getTop(), state.getRight(), state.getBottom());
            this.this$0.cornerRadii[0] = state.getTopCornerRadius();
            this.this$0.cornerRadii[1] = state.getTopCornerRadius();
            this.this$0.cornerRadii[2] = state.getTopCornerRadius();
            this.this$0.cornerRadii[3] = state.getTopCornerRadius();
            this.this$0.cornerRadii[4] = state.getBottomCornerRadius();
            this.this$0.cornerRadii[5] = state.getBottomCornerRadius();
            this.this$0.cornerRadii[6] = state.getBottomCornerRadius();
            this.this$0.cornerRadii[7] = state.getBottomCornerRadius();
            gradientDrawable.setCornerRadii(this.this$0.cornerRadii);
            Companion companion = ActivityLaunchAnimator.Companion;
            float progress = companion.getProgress(f, 0L, 150L);
            if (progress < 1.0f) {
                gradientDrawable.setAlpha(MathKt.roundToInt(ActivityLaunchAnimator.CONTENT_FADE_OUT_INTERPOLATOR.getInterpolation(progress) * 255));
                gradientDrawable.setXfermode(null);
                return;
            }
            gradientDrawable.setAlpha(MathKt.roundToInt((1 - ActivityLaunchAnimator.WINDOW_FADE_IN_INTERPOLATOR.getInterpolation(companion.getProgress(f, 150L, 183L))) * 255));
            gradientDrawable.setXfermode(ActivityLaunchAnimator.SRC_MODE);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void applyStateToNavigationBar(RemoteAnimationTarget remoteAnimationTarget, State state, float f) {
            Companion companion = ActivityLaunchAnimator.Companion;
            float progress = companion.getProgress(f, 234L, 133L);
            SyncRtSurfaceTransactionApplier.SurfaceParams.Builder builder = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget.leash);
            if (progress <= 0.0f) {
                builder.withAlpha(1.0f - ActivityLaunchAnimator.NAV_FADE_OUT_INTERPOLATOR.getInterpolation(companion.getProgress(f, 0L, 133L)));
            } else {
                this.matrix.reset();
                this.matrix.setTranslate(0.0f, state.getTop() - remoteAnimationTarget.sourceContainerBounds.top);
                this.windowCrop.set(state.getLeft(), 0, state.getRight(), state.getHeight());
                builder.withAlpha(ActivityLaunchAnimator.NAV_FADE_IN_INTERPOLATOR.getInterpolation(progress)).withMatrix(this.matrix).withWindowCrop(this.windowCrop).withVisibility(true);
            }
            this.transactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{builder.build()});
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void onAnimationTimedOut() {
            if (this.cancelled) {
                return;
            }
            Log.d("ActivityLaunchAnimator", "Remote animation timed out");
            this.timedOut = true;
            this.controller.onLaunchAnimationCancelled();
        }

        public void onAnimationCancelled() {
            if (this.timedOut) {
                return;
            }
            Log.d("ActivityLaunchAnimator", "Remote animation was cancelled");
            this.cancelled = true;
            removeTimeout();
            this.context.getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$onAnimationCancelled$1
                @Override // java.lang.Runnable
                public final void run() {
                    ValueAnimator valueAnimator;
                    ActivityLaunchAnimator.Controller controller;
                    valueAnimator = ActivityLaunchAnimator.Runner.this.animator;
                    if (valueAnimator != null) {
                        valueAnimator.cancel();
                    }
                    controller = ActivityLaunchAnimator.Runner.this.controller;
                    controller.onLaunchAnimationCancelled();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void invoke(IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            try {
                iRemoteAnimationFinishedCallback.onAnimationFinished();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final float lerp(int i, int i2, float f) {
            return MathUtils.lerp(i, i2, f);
        }
    }
}
