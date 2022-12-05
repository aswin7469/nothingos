package com.android.wm.shell.common.magnetictarget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.PointF;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes2.dex */
public abstract class MagnetizedObject<T> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private static boolean hapticSettingObserverInitialized;
    private static boolean systemHapticsEnabled;
    @NotNull
    private final PhysicsAnimator<T> animator;
    @NotNull
    private final Context context;
    @NotNull
    private PhysicsAnimator.SpringConfig flungIntoTargetSpringConfig;
    public MagnetListener magnetListener;
    private boolean movedBeyondSlop;
    @Nullable
    private PhysicsAnimator.EndListener<T> physicsAnimatorEndListener;
    @Nullable
    private PhysicsAnimator.UpdateListener<T> physicsAnimatorUpdateListener;
    @NotNull
    private PhysicsAnimator.SpringConfig springConfig;
    @Nullable
    private MagneticTarget targetObjectIsStuckTo;
    private int touchSlop;
    @NotNull
    private final T underlyingObject;
    @NotNull
    private final VelocityTracker velocityTracker;
    @NotNull
    private final Vibrator vibrator;
    @NotNull
    private final FloatPropertyCompat<? super T> xProperty;
    @NotNull
    private final FloatPropertyCompat<? super T> yProperty;
    @NotNull
    private final int[] objectLocationOnScreen = new int[2];
    @NotNull
    private final ArrayList<MagneticTarget> associatedTargets = new ArrayList<>();
    @NotNull
    private PointF touchDown = new PointF();
    @NotNull
    private Function5<? super MagneticTarget, ? super Float, ? super Float, ? super Boolean, ? super Function0<Unit>, Unit> animateStuckToTarget = new MagnetizedObject$animateStuckToTarget$1(this);
    private boolean flingToTargetEnabled = true;
    private float flingToTargetWidthPercent = 3.0f;
    private float flingToTargetMinVelocity = 4000.0f;
    private float flingUnstuckFromTargetMinVelocity = 4000.0f;
    private float stickToTargetMaxXVelocity = 2000.0f;
    private boolean hapticsEnabled = true;

    /* compiled from: MagnetizedObject.kt */
    /* loaded from: classes2.dex */
    public interface MagnetListener {
        void onReleasedInTarget(@NotNull MagneticTarget magneticTarget);

        void onStuckToTarget(@NotNull MagneticTarget magneticTarget);

        void onUnstuckFromTarget(@NotNull MagneticTarget magneticTarget, float f, float f2, boolean z);
    }

    public abstract float getHeight(@NotNull T t);

    public abstract void getLocationOnScreen(@NotNull T t, @NotNull int[] iArr);

    public abstract float getWidth(@NotNull T t);

    public MagnetizedObject(@NotNull Context context, @NotNull T underlyingObject, @NotNull FloatPropertyCompat<? super T> xProperty, @NotNull FloatPropertyCompat<? super T> yProperty) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(underlyingObject, "underlyingObject");
        Intrinsics.checkNotNullParameter(xProperty, "xProperty");
        Intrinsics.checkNotNullParameter(yProperty, "yProperty");
        this.context = context;
        this.underlyingObject = underlyingObject;
        this.xProperty = xProperty;
        this.yProperty = yProperty;
        this.animator = PhysicsAnimator.Companion.getInstance(underlyingObject);
        VelocityTracker obtain = VelocityTracker.obtain();
        Intrinsics.checkNotNullExpressionValue(obtain, "obtain()");
        this.velocityTracker = obtain;
        Object systemService = context.getSystemService("vibrator");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.os.Vibrator");
        this.vibrator = (Vibrator) systemService;
        PhysicsAnimator.SpringConfig springConfig = new PhysicsAnimator.SpringConfig(1500.0f, 1.0f);
        this.springConfig = springConfig;
        this.flungIntoTargetSpringConfig = springConfig;
        Companion.initHapticSettingObserver(context);
    }

    @NotNull
    public final T getUnderlyingObject() {
        return this.underlyingObject;
    }

    public final boolean getObjectStuckToTarget() {
        return this.targetObjectIsStuckTo != null;
    }

    @NotNull
    public final MagnetListener getMagnetListener() {
        MagnetListener magnetListener = this.magnetListener;
        if (magnetListener != null) {
            return magnetListener;
        }
        Intrinsics.throwUninitializedPropertyAccessException("magnetListener");
        throw null;
    }

    public final void setMagnetListener(@NotNull MagnetListener magnetListener) {
        Intrinsics.checkNotNullParameter(magnetListener, "<set-?>");
        this.magnetListener = magnetListener;
    }

    public final void setAnimateStuckToTarget(@NotNull Function5<? super MagneticTarget, ? super Float, ? super Float, ? super Boolean, ? super Function0<Unit>, Unit> function5) {
        Intrinsics.checkNotNullParameter(function5, "<set-?>");
        this.animateStuckToTarget = function5;
    }

    public final float getFlingToTargetWidthPercent() {
        return this.flingToTargetWidthPercent;
    }

    public final void setFlingToTargetWidthPercent(float f) {
        this.flingToTargetWidthPercent = f;
    }

    public final float getFlingToTargetMinVelocity() {
        return this.flingToTargetMinVelocity;
    }

    public final void setFlingToTargetMinVelocity(float f) {
        this.flingToTargetMinVelocity = f;
    }

    public final float getStickToTargetMaxXVelocity() {
        return this.stickToTargetMaxXVelocity;
    }

    public final void setStickToTargetMaxXVelocity(float f) {
        this.stickToTargetMaxXVelocity = f;
    }

    public final void setHapticsEnabled(boolean z) {
        this.hapticsEnabled = z;
    }

    public final void addTarget(@NotNull MagneticTarget target) {
        Intrinsics.checkNotNullParameter(target, "target");
        this.associatedTargets.add(target);
        target.updateLocationOnScreen();
    }

    @NotNull
    public final MagneticTarget addTarget(@NotNull View target, int i) {
        Intrinsics.checkNotNullParameter(target, "target");
        MagneticTarget magneticTarget = new MagneticTarget(target, i);
        addTarget(magneticTarget);
        return magneticTarget;
    }

    public final void clearAllTargets() {
        this.associatedTargets.clear();
    }

    public final boolean maybeConsumeMotionEvent(@NotNull MotionEvent ev) {
        T t;
        boolean z;
        Intrinsics.checkNotNullParameter(ev, "ev");
        if (this.associatedTargets.size() == 0) {
            return false;
        }
        T t2 = null;
        if (ev.getAction() == 0) {
            updateTargetViews$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
            this.velocityTracker.clear();
            this.targetObjectIsStuckTo = null;
            this.touchDown.set(ev.getRawX(), ev.getRawY());
            this.movedBeyondSlop = false;
        }
        addMovement(ev);
        if (!this.movedBeyondSlop) {
            if (((float) Math.hypot(ev.getRawX() - this.touchDown.x, ev.getRawY() - this.touchDown.y)) <= this.touchSlop) {
                return false;
            }
            this.movedBeyondSlop = true;
        }
        Iterator<T> it = this.associatedTargets.iterator();
        while (true) {
            if (!it.hasNext()) {
                t = null;
                break;
            }
            t = it.next();
            MagneticTarget magneticTarget = (MagneticTarget) t;
            if (((float) Math.hypot(ev.getRawX() - magneticTarget.getCenterOnScreen().x, ev.getRawY() - magneticTarget.getCenterOnScreen().y)) < magneticTarget.getMagneticFieldRadiusPx()) {
                z = true;
                continue;
            } else {
                z = false;
                continue;
            }
            if (z) {
                break;
            }
        }
        MagneticTarget magneticTarget2 = (MagneticTarget) t;
        boolean z2 = !getObjectStuckToTarget() && magneticTarget2 != null;
        boolean z3 = getObjectStuckToTarget() && magneticTarget2 != null && !Intrinsics.areEqual(this.targetObjectIsStuckTo, magneticTarget2);
        if (z2 || z3) {
            this.velocityTracker.computeCurrentVelocity(1000);
            float xVelocity = this.velocityTracker.getXVelocity();
            float yVelocity = this.velocityTracker.getYVelocity();
            if (z2 && Math.abs(xVelocity) > this.stickToTargetMaxXVelocity) {
                return false;
            }
            this.targetObjectIsStuckTo = magneticTarget2;
            cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
            MagnetListener magnetListener = getMagnetListener();
            Intrinsics.checkNotNull(magneticTarget2);
            magnetListener.onStuckToTarget(magneticTarget2);
            this.animateStuckToTarget.invoke(magneticTarget2, Float.valueOf(xVelocity), Float.valueOf(yVelocity), Boolean.FALSE, null);
            vibrateIfEnabled(5);
        } else if (magneticTarget2 == null && getObjectStuckToTarget()) {
            this.velocityTracker.computeCurrentVelocity(1000);
            cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
            MagnetListener magnetListener2 = getMagnetListener();
            MagneticTarget magneticTarget3 = this.targetObjectIsStuckTo;
            Intrinsics.checkNotNull(magneticTarget3);
            magnetListener2.onUnstuckFromTarget(magneticTarget3, this.velocityTracker.getXVelocity(), this.velocityTracker.getYVelocity(), false);
            this.targetObjectIsStuckTo = null;
            vibrateIfEnabled(2);
        }
        if (ev.getAction() == 1) {
            this.velocityTracker.computeCurrentVelocity(1000);
            float xVelocity2 = this.velocityTracker.getXVelocity();
            float yVelocity2 = this.velocityTracker.getYVelocity();
            cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
            if (!getObjectStuckToTarget()) {
                Iterator<T> it2 = this.associatedTargets.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    T next = it2.next();
                    if (isForcefulFlingTowardsTarget((MagneticTarget) next, ev.getRawX(), ev.getRawY(), xVelocity2, yVelocity2)) {
                        t2 = next;
                        break;
                    }
                }
                MagneticTarget magneticTarget4 = t2;
                if (magneticTarget4 == null) {
                    return false;
                }
                getMagnetListener().onStuckToTarget(magneticTarget4);
                this.targetObjectIsStuckTo = magneticTarget4;
                this.animateStuckToTarget.invoke(magneticTarget4, Float.valueOf(xVelocity2), Float.valueOf(yVelocity2), Boolean.TRUE, new MagnetizedObject$maybeConsumeMotionEvent$1(this, magneticTarget4));
                return true;
            }
            if ((-yVelocity2) > this.flingUnstuckFromTargetMinVelocity) {
                MagnetListener magnetListener3 = getMagnetListener();
                MagneticTarget magneticTarget5 = this.targetObjectIsStuckTo;
                Intrinsics.checkNotNull(magneticTarget5);
                magnetListener3.onUnstuckFromTarget(magneticTarget5, xVelocity2, yVelocity2, true);
            } else {
                MagnetListener magnetListener4 = getMagnetListener();
                MagneticTarget magneticTarget6 = this.targetObjectIsStuckTo;
                Intrinsics.checkNotNull(magneticTarget6);
                magnetListener4.onReleasedInTarget(magneticTarget6);
                vibrateIfEnabled(5);
            }
            this.targetObjectIsStuckTo = null;
            return true;
        }
        return getObjectStuckToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"MissingPermission"})
    public final void vibrateIfEnabled(int i) {
        if (!this.hapticsEnabled || !systemHapticsEnabled) {
            return;
        }
        this.vibrator.vibrate(VibrationEffect.createPredefined(i));
    }

    private final void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.velocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void animateStuckToTargetInternal(MagneticTarget magneticTarget, float f, float f2, boolean z, Function0<Unit> function0) {
        magneticTarget.updateLocationOnScreen();
        getLocationOnScreen(this.underlyingObject, this.objectLocationOnScreen);
        float width = (magneticTarget.getCenterOnScreen().x - (getWidth(this.underlyingObject) / 2.0f)) - this.objectLocationOnScreen[0];
        float height = (magneticTarget.getCenterOnScreen().y - (getHeight(this.underlyingObject) / 2.0f)) - this.objectLocationOnScreen[1];
        PhysicsAnimator.SpringConfig springConfig = z ? this.flungIntoTargetSpringConfig : this.springConfig;
        cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
        PhysicsAnimator<T> physicsAnimator = this.animator;
        FloatPropertyCompat<? super T> floatPropertyCompat = this.xProperty;
        PhysicsAnimator<T> spring = physicsAnimator.spring(floatPropertyCompat, floatPropertyCompat.getValue((T) this.underlyingObject) + width, f, springConfig);
        FloatPropertyCompat<? super T> floatPropertyCompat2 = this.yProperty;
        spring.spring(floatPropertyCompat2, floatPropertyCompat2.getValue((T) this.underlyingObject) + height, f2, springConfig);
        PhysicsAnimator.UpdateListener<T> updateListener = this.physicsAnimatorUpdateListener;
        if (updateListener != null) {
            PhysicsAnimator<T> physicsAnimator2 = this.animator;
            Intrinsics.checkNotNull(updateListener);
            physicsAnimator2.addUpdateListener(updateListener);
        }
        PhysicsAnimator.EndListener<T> endListener = this.physicsAnimatorEndListener;
        if (endListener != null) {
            PhysicsAnimator<T> physicsAnimator3 = this.animator;
            Intrinsics.checkNotNull(endListener);
            physicsAnimator3.addEndListener(endListener);
        }
        if (function0 != null) {
            this.animator.withEndActions(function0);
        }
        this.animator.start();
    }

    private final boolean isForcefulFlingTowardsTarget(MagneticTarget magneticTarget, float f, float f2, float f3, float f4) {
        if (!this.flingToTargetEnabled) {
            return false;
        }
        if (!(f2 >= magneticTarget.getCenterOnScreen().y ? f4 < this.flingToTargetMinVelocity : f4 > this.flingToTargetMinVelocity)) {
            return false;
        }
        if (!(f3 == 0.0f)) {
            float f5 = f4 / f3;
            f = (magneticTarget.getCenterOnScreen().y - (f2 - (f * f5))) / f5;
        }
        float width = (magneticTarget.getTargetView().getWidth() * this.flingToTargetWidthPercent) / 2;
        return f > magneticTarget.getCenterOnScreen().x - width && f < magneticTarget.getCenterOnScreen().x + width;
    }

    public final void cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
        this.animator.cancel(this.xProperty, this.yProperty);
    }

    public final void updateTargetViews$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
        for (MagneticTarget magneticTarget : this.associatedTargets) {
            magneticTarget.updateLocationOnScreen();
        }
        if (this.associatedTargets.size() > 0) {
            this.touchSlop = ViewConfiguration.get(this.associatedTargets.get(0).getTargetView().getContext()).getScaledTouchSlop();
        }
    }

    /* compiled from: MagnetizedObject.kt */
    /* loaded from: classes2.dex */
    public static final class MagneticTarget {
        private int magneticFieldRadiusPx;
        @NotNull
        private final View targetView;
        @NotNull
        private final PointF centerOnScreen = new PointF();
        @NotNull
        private final int[] tempLoc = new int[2];

        public MagneticTarget(@NotNull View targetView, int i) {
            Intrinsics.checkNotNullParameter(targetView, "targetView");
            this.targetView = targetView;
            this.magneticFieldRadiusPx = i;
        }

        @NotNull
        public final View getTargetView() {
            return this.targetView;
        }

        public final int getMagneticFieldRadiusPx() {
            return this.magneticFieldRadiusPx;
        }

        public final void setMagneticFieldRadiusPx(int i) {
            this.magneticFieldRadiusPx = i;
        }

        @NotNull
        public final PointF getCenterOnScreen() {
            return this.centerOnScreen;
        }

        public final void updateLocationOnScreen() {
            this.targetView.post(new Runnable() { // from class: com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget$updateLocationOnScreen$1
                @Override // java.lang.Runnable
                public final void run() {
                    int[] iArr;
                    int[] iArr2;
                    int[] iArr3;
                    View targetView = MagnetizedObject.MagneticTarget.this.getTargetView();
                    iArr = MagnetizedObject.MagneticTarget.this.tempLoc;
                    targetView.getLocationOnScreen(iArr);
                    PointF centerOnScreen = MagnetizedObject.MagneticTarget.this.getCenterOnScreen();
                    iArr2 = MagnetizedObject.MagneticTarget.this.tempLoc;
                    float width = (iArr2[0] + (MagnetizedObject.MagneticTarget.this.getTargetView().getWidth() / 2.0f)) - MagnetizedObject.MagneticTarget.this.getTargetView().getTranslationX();
                    iArr3 = MagnetizedObject.MagneticTarget.this.tempLoc;
                    centerOnScreen.set(width, (iArr3[1] + (MagnetizedObject.MagneticTarget.this.getTargetView().getHeight() / 2.0f)) - MagnetizedObject.MagneticTarget.this.getTargetView().getTranslationY());
                }
            });
        }
    }

    /* compiled from: MagnetizedObject.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void initHapticSettingObserver(final Context context) {
            if (MagnetizedObject.hapticSettingObserverInitialized) {
                return;
            }
            final Handler main = Handler.getMain();
            ContentObserver contentObserver = new ContentObserver(main) { // from class: com.android.wm.shell.common.magnetictarget.MagnetizedObject$Companion$initHapticSettingObserver$hapticSettingObserver$1
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    MagnetizedObject.Companion companion = MagnetizedObject.Companion;
                    boolean z2 = false;
                    if (Settings.System.getIntForUser(context.getContentResolver(), "haptic_feedback_enabled", 0, -2) != 0) {
                        z2 = true;
                    }
                    MagnetizedObject.systemHapticsEnabled = z2;
                }
            };
            context.getContentResolver().registerContentObserver(Settings.System.getUriFor("haptic_feedback_enabled"), true, contentObserver);
            contentObserver.onChange(false);
            MagnetizedObject.hapticSettingObserverInitialized = true;
        }
    }
}
