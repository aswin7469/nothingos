package com.android.p019wm.shell.common.magnetictarget;

import android.content.Context;
import android.graphics.PointF;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000¤\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0015\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u001d\b&\u0018\u0000 \u0001*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002:\u0006\u0001\u0001\u0001B5\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u0012\u000e\u0010\u0006\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u0007\u0012\u000e\u0010\b\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u0007¢\u0006\u0002\u0010\tJ\u0010\u0010c\u001a\u00020\u00102\u0006\u0010d\u001a\u00020eH\u0002J\u0016\u0010f\u001a\u00020\f2\u0006\u0010g\u001a\u00020h2\u0006\u0010i\u001a\u00020VJ\u000e\u0010f\u001a\u00020\u00102\u0006\u0010g\u001a\u00020\fJ:\u0010j\u001a\u00020\u00102\u0006\u0010g\u001a\u00020\f2\u0006\u0010k\u001a\u00020\r2\u0006\u0010l\u001a\u00020\r2\u0006\u0010m\u001a\u00020\u000e2\u0010\b\u0002\u0010n\u001a\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000fH\u0002J\r\u0010o\u001a\u00020\u0010H\u0000¢\u0006\u0002\bpJ\u0006\u0010q\u001a\u00020\u0010J\u0015\u0010r\u001a\u00020\r2\u0006\u0010\u0005\u001a\u00028\u0000H&¢\u0006\u0002\u0010sJ\u001d\u0010t\u001a\u00020\u00102\u0006\u0010\u0005\u001a\u00028\u00002\u0006\u0010u\u001a\u00020=H&¢\u0006\u0002\u0010vJ\u0015\u0010w\u001a\u00020\r2\u0006\u0010\u0005\u001a\u00028\u0000H&¢\u0006\u0002\u0010sJ0\u0010x\u001a\u00020\u000e2\u0006\u0010g\u001a\u00020\f2\u0006\u0010y\u001a\u00020\r2\u0006\u0010z\u001a\u00020\r2\u0006\u0010k\u001a\u00020\r2\u0006\u0010l\u001a\u00020\rH\u0002J\u000e\u0010{\u001a\u00020\u000e2\u0006\u0010|\u001a\u00020eJ\u000e\u0010}\u001a\u00020\u00102\u0006\u0010g\u001a\u00020\fJ\r\u0010~\u001a\u00020\u0010H\u0000¢\u0006\u0002\bJ\u0012\u0010\u0001\u001a\u00020\u00102\u0007\u0010\u0001\u001a\u00020VH\u0003RF\u0010\n\u001a.\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\u000e\u0012\f\u0012\n\u0012\u0004\u0012\u00020\u0010\u0018\u00010\u000f\u0012\u0004\u0012\u00020\u00100\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00000\u0016X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0017\u001a\u0012\u0012\u0004\u0012\u00020\f0\u0018j\b\u0012\u0004\u0012\u00020\f`\u0019X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u001a\u0010\u001c\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001a\u0010!\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001a\u0010&\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b'\u0010#\"\u0004\b(\u0010%R\u001a\u0010)\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b*\u0010#\"\u0004\b+\u0010%R\u001a\u0010,\u001a\u00020-X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101R\u001a\u00102\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u001e\"\u0004\b4\u0010 R\u001a\u00105\u001a\u000206X.¢\u0006\u000e\n\u0000\u001a\u0004\b7\u00108\"\u0004\b9\u0010:R\u000e\u0010;\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010<\u001a\u00020=X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010>\u001a\u00020\u000e8F¢\u0006\u0006\u001a\u0004\b?\u0010\u001eR\"\u0010@\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010AX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bB\u0010C\"\u0004\bD\u0010ER\"\u0010F\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010GX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bH\u0010I\"\u0004\bJ\u0010KR\u001a\u0010L\u001a\u00020-X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bM\u0010/\"\u0004\bN\u00101R\u001a\u0010O\u001a\u00020\rX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bP\u0010#\"\u0004\bQ\u0010%R\u0010\u0010R\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010S\u001a\u00020TX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010U\u001a\u00020VX\u000e¢\u0006\u0002\n\u0000R\u0013\u0010\u0005\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010Y\u001a\u0004\bW\u0010XR\u000e\u0010Z\u001a\u00020[X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\\\u001a\u00020]X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010^\u001a\u00020_X\u0004¢\u0006\u0002\n\u0000R\u0019\u0010\u0006\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u0007¢\u0006\b\n\u0000\u001a\u0004\b`\u0010aR\u0019\u0010\b\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u0007¢\u0006\b\n\u0000\u001a\u0004\bb\u0010a¨\u0006\u0001"}, mo64987d2 = {"Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject;", "T", "", "context", "Landroid/content/Context;", "underlyingObject", "xProperty", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "yProperty", "(Landroid/content/Context;Ljava/lang/Object;Landroidx/dynamicanimation/animation/FloatPropertyCompat;Landroidx/dynamicanimation/animation/FloatPropertyCompat;)V", "animateStuckToTarget", "Lkotlin/Function5;", "Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagneticTarget;", "", "", "Lkotlin/Function0;", "", "getAnimateStuckToTarget", "()Lkotlin/jvm/functions/Function5;", "setAnimateStuckToTarget", "(Lkotlin/jvm/functions/Function5;)V", "animator", "Lcom/android/wm/shell/animation/PhysicsAnimator;", "associatedTargets", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "getContext", "()Landroid/content/Context;", "flingToTargetEnabled", "getFlingToTargetEnabled", "()Z", "setFlingToTargetEnabled", "(Z)V", "flingToTargetMinVelocity", "getFlingToTargetMinVelocity", "()F", "setFlingToTargetMinVelocity", "(F)V", "flingToTargetWidthPercent", "getFlingToTargetWidthPercent", "setFlingToTargetWidthPercent", "flingUnstuckFromTargetMinVelocity", "getFlingUnstuckFromTargetMinVelocity", "setFlingUnstuckFromTargetMinVelocity", "flungIntoTargetSpringConfig", "Lcom/android/wm/shell/animation/PhysicsAnimator$SpringConfig;", "getFlungIntoTargetSpringConfig", "()Lcom/android/wm/shell/animation/PhysicsAnimator$SpringConfig;", "setFlungIntoTargetSpringConfig", "(Lcom/android/wm/shell/animation/PhysicsAnimator$SpringConfig;)V", "hapticsEnabled", "getHapticsEnabled", "setHapticsEnabled", "magnetListener", "Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagnetListener;", "getMagnetListener", "()Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagnetListener;", "setMagnetListener", "(Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagnetListener;)V", "movedBeyondSlop", "objectLocationOnScreen", "", "objectStuckToTarget", "getObjectStuckToTarget", "physicsAnimatorEndListener", "Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "getPhysicsAnimatorEndListener", "()Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "setPhysicsAnimatorEndListener", "(Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;)V", "physicsAnimatorUpdateListener", "Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;", "getPhysicsAnimatorUpdateListener", "()Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;", "setPhysicsAnimatorUpdateListener", "(Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;)V", "springConfig", "getSpringConfig", "setSpringConfig", "stickToTargetMaxXVelocity", "getStickToTargetMaxXVelocity", "setStickToTargetMaxXVelocity", "targetObjectIsStuckTo", "touchDown", "Landroid/graphics/PointF;", "touchSlop", "", "getUnderlyingObject", "()Ljava/lang/Object;", "Ljava/lang/Object;", "velocityTracker", "Landroid/view/VelocityTracker;", "vibrationAttributes", "Landroid/os/VibrationAttributes;", "vibrator", "Landroid/os/Vibrator;", "getXProperty", "()Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "getYProperty", "addMovement", "event", "Landroid/view/MotionEvent;", "addTarget", "target", "Landroid/view/View;", "magneticFieldRadiusPx", "animateStuckToTargetInternal", "velX", "velY", "flung", "after", "cancelAnimations", "cancelAnimations$WMShell_release", "clearAllTargets", "getHeight", "(Ljava/lang/Object;)F", "getLocationOnScreen", "loc", "(Ljava/lang/Object;[I)V", "getWidth", "isForcefulFlingTowardsTarget", "rawX", "rawY", "maybeConsumeMotionEvent", "ev", "removeTarget", "updateTargetViews", "updateTargetViews$WMShell_release", "vibrateIfEnabled", "effectId", "Companion", "MagnetListener", "MagneticTarget", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.common.magnetictarget.MagnetizedObject */
/* compiled from: MagnetizedObject.kt */
public abstract class MagnetizedObject<T> {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private Function5<? super MagneticTarget, ? super Float, ? super Float, ? super Boolean, ? super Function0<Unit>, Unit> animateStuckToTarget;
    private final PhysicsAnimator<T> animator;
    private final ArrayList<MagneticTarget> associatedTargets = new ArrayList<>();
    private final Context context;
    private boolean flingToTargetEnabled;
    private float flingToTargetMinVelocity;
    private float flingToTargetWidthPercent;
    private float flingUnstuckFromTargetMinVelocity;
    private PhysicsAnimator.SpringConfig flungIntoTargetSpringConfig;
    private boolean hapticsEnabled;
    public MagnetListener magnetListener;
    private boolean movedBeyondSlop;
    private final int[] objectLocationOnScreen = new int[2];
    private PhysicsAnimator.EndListener<T> physicsAnimatorEndListener;
    private PhysicsAnimator.UpdateListener<T> physicsAnimatorUpdateListener;
    private PhysicsAnimator.SpringConfig springConfig;
    private float stickToTargetMaxXVelocity;
    /* access modifiers changed from: private */
    public MagneticTarget targetObjectIsStuckTo;
    private PointF touchDown;
    private int touchSlop;
    private final T underlyingObject;
    private final VelocityTracker velocityTracker;
    private final VibrationAttributes vibrationAttributes;
    private final Vibrator vibrator;
    private final FloatPropertyCompat<? super T> xProperty;
    private final FloatPropertyCompat<? super T> yProperty;

    @Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J(\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\fH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\rÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagnetListener;", "", "onReleasedInTarget", "", "target", "Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagneticTarget;", "onStuckToTarget", "onUnstuckFromTarget", "velX", "", "velY", "wasFlungOut", "", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagnetListener */
    /* compiled from: MagnetizedObject.kt */
    public interface MagnetListener {
        void onReleasedInTarget(MagneticTarget magneticTarget);

        void onStuckToTarget(MagneticTarget magneticTarget);

        void onUnstuckFromTarget(MagneticTarget magneticTarget, float f, float f2, boolean z);
    }

    @JvmStatic
    public static final <T extends View> MagnetizedObject<T> magnetizeView(T t) {
        return Companion.magnetizeView(t);
    }

    public abstract float getHeight(T t);

    public abstract void getLocationOnScreen(T t, int[] iArr);

    public abstract float getWidth(T t);

    public MagnetizedObject(Context context2, T t, FloatPropertyCompat<? super T> floatPropertyCompat, FloatPropertyCompat<? super T> floatPropertyCompat2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(t, "underlyingObject");
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "xProperty");
        Intrinsics.checkNotNullParameter(floatPropertyCompat2, "yProperty");
        this.context = context2;
        this.underlyingObject = t;
        this.xProperty = floatPropertyCompat;
        this.yProperty = floatPropertyCompat2;
        this.animator = PhysicsAnimator.Companion.getInstance(t);
        VelocityTracker obtain = VelocityTracker.obtain();
        Intrinsics.checkNotNullExpressionValue(obtain, "obtain()");
        this.velocityTracker = obtain;
        Object systemService = context2.getSystemService("vibrator");
        if (systemService != null) {
            this.vibrator = (Vibrator) systemService;
            VibrationAttributes createForUsage = VibrationAttributes.createForUsage(18);
            Intrinsics.checkNotNullExpressionValue(createForUsage, "createForUsage(\n        …onAttributes.USAGE_TOUCH)");
            this.vibrationAttributes = createForUsage;
            this.touchDown = new PointF();
            this.animateStuckToTarget = new MagnetizedObject$animateStuckToTarget$1(this);
            this.flingToTargetEnabled = true;
            this.flingToTargetWidthPercent = 3.0f;
            this.flingToTargetMinVelocity = 4000.0f;
            this.flingUnstuckFromTargetMinVelocity = 4000.0f;
            this.stickToTargetMaxXVelocity = 2000.0f;
            this.hapticsEnabled = true;
            PhysicsAnimator.SpringConfig springConfig2 = new PhysicsAnimator.SpringConfig(1500.0f, 1.0f);
            this.springConfig = springConfig2;
            this.flungIntoTargetSpringConfig = springConfig2;
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.os.Vibrator");
    }

    public final Context getContext() {
        return this.context;
    }

    public final T getUnderlyingObject() {
        return this.underlyingObject;
    }

    public final FloatPropertyCompat<? super T> getXProperty() {
        return this.xProperty;
    }

    public final FloatPropertyCompat<? super T> getYProperty() {
        return this.yProperty;
    }

    public final boolean getObjectStuckToTarget() {
        return this.targetObjectIsStuckTo != null;
    }

    public final MagnetListener getMagnetListener() {
        MagnetListener magnetListener2 = this.magnetListener;
        if (magnetListener2 != null) {
            return magnetListener2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("magnetListener");
        return null;
    }

    public final void setMagnetListener(MagnetListener magnetListener2) {
        Intrinsics.checkNotNullParameter(magnetListener2, "<set-?>");
        this.magnetListener = magnetListener2;
    }

    public final PhysicsAnimator.UpdateListener<T> getPhysicsAnimatorUpdateListener() {
        return this.physicsAnimatorUpdateListener;
    }

    public final void setPhysicsAnimatorUpdateListener(PhysicsAnimator.UpdateListener<T> updateListener) {
        this.physicsAnimatorUpdateListener = updateListener;
    }

    public final PhysicsAnimator.EndListener<T> getPhysicsAnimatorEndListener() {
        return this.physicsAnimatorEndListener;
    }

    public final void setPhysicsAnimatorEndListener(PhysicsAnimator.EndListener<T> endListener) {
        this.physicsAnimatorEndListener = endListener;
    }

    public final Function5<MagneticTarget, Float, Float, Boolean, Function0<Unit>, Unit> getAnimateStuckToTarget() {
        return this.animateStuckToTarget;
    }

    public final void setAnimateStuckToTarget(Function5<? super MagneticTarget, ? super Float, ? super Float, ? super Boolean, ? super Function0<Unit>, Unit> function5) {
        Intrinsics.checkNotNullParameter(function5, "<set-?>");
        this.animateStuckToTarget = function5;
    }

    public final boolean getFlingToTargetEnabled() {
        return this.flingToTargetEnabled;
    }

    public final void setFlingToTargetEnabled(boolean z) {
        this.flingToTargetEnabled = z;
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

    public final float getFlingUnstuckFromTargetMinVelocity() {
        return this.flingUnstuckFromTargetMinVelocity;
    }

    public final void setFlingUnstuckFromTargetMinVelocity(float f) {
        this.flingUnstuckFromTargetMinVelocity = f;
    }

    public final float getStickToTargetMaxXVelocity() {
        return this.stickToTargetMaxXVelocity;
    }

    public final void setStickToTargetMaxXVelocity(float f) {
        this.stickToTargetMaxXVelocity = f;
    }

    public final boolean getHapticsEnabled() {
        return this.hapticsEnabled;
    }

    public final void setHapticsEnabled(boolean z) {
        this.hapticsEnabled = z;
    }

    public final PhysicsAnimator.SpringConfig getSpringConfig() {
        return this.springConfig;
    }

    public final void setSpringConfig(PhysicsAnimator.SpringConfig springConfig2) {
        Intrinsics.checkNotNullParameter(springConfig2, "<set-?>");
        this.springConfig = springConfig2;
    }

    public final PhysicsAnimator.SpringConfig getFlungIntoTargetSpringConfig() {
        return this.flungIntoTargetSpringConfig;
    }

    public final void setFlungIntoTargetSpringConfig(PhysicsAnimator.SpringConfig springConfig2) {
        Intrinsics.checkNotNullParameter(springConfig2, "<set-?>");
        this.flungIntoTargetSpringConfig = springConfig2;
    }

    public final void addTarget(MagneticTarget magneticTarget) {
        Intrinsics.checkNotNullParameter(magneticTarget, "target");
        this.associatedTargets.add(magneticTarget);
        magneticTarget.updateLocationOnScreen();
    }

    public final MagneticTarget addTarget(View view, int i) {
        Intrinsics.checkNotNullParameter(view, "target");
        MagneticTarget magneticTarget = new MagneticTarget(view, i);
        addTarget(magneticTarget);
        return magneticTarget;
    }

    public final void removeTarget(MagneticTarget magneticTarget) {
        Intrinsics.checkNotNullParameter(magneticTarget, "target");
        this.associatedTargets.remove((Object) magneticTarget);
    }

    public final void clearAllTargets() {
        this.associatedTargets.clear();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v2, resolved type: com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean maybeConsumeMotionEvent(android.view.MotionEvent r14) {
        /*
            r13 = this;
            java.lang.String r0 = "ev"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            java.util.ArrayList<com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget> r0 = r13.associatedTargets
            int r0 = r0.size()
            r1 = 0
            if (r0 != 0) goto L_0x000f
            return r1
        L_0x000f:
            int r0 = r14.getAction()
            r2 = 0
            if (r0 != 0) goto L_0x002f
            r13.updateTargetViews$WMShell_release()
            android.view.VelocityTracker r0 = r13.velocityTracker
            r0.clear()
            r13.targetObjectIsStuckTo = r2
            android.graphics.PointF r0 = r13.touchDown
            float r3 = r14.getRawX()
            float r4 = r14.getRawY()
            r0.set(r3, r4)
            r13.movedBeyondSlop = r1
        L_0x002f:
            r13.addMovement(r14)
            boolean r0 = r13.movedBeyondSlop
            r3 = 1
            if (r0 != 0) goto L_0x005b
            float r0 = r14.getRawX()
            android.graphics.PointF r4 = r13.touchDown
            float r4 = r4.x
            float r0 = r0 - r4
            double r4 = (double) r0
            float r0 = r14.getRawY()
            android.graphics.PointF r6 = r13.touchDown
            float r6 = r6.y
            float r0 = r0 - r6
            double r6 = (double) r0
            double r4 = java.lang.Math.hypot(r4, r6)
            float r0 = (float) r4
            int r4 = r13.touchSlop
            float r4 = (float) r4
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x005a
            r13.movedBeyondSlop = r3
            goto L_0x005b
        L_0x005a:
            return r1
        L_0x005b:
            java.util.ArrayList<com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget> r0 = r13.associatedTargets
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            java.util.Iterator r0 = r0.iterator()
        L_0x0063:
            boolean r4 = r0.hasNext()
            if (r4 == 0) goto L_0x009c
            java.lang.Object r4 = r0.next()
            r5 = r4
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget r5 = (com.android.p019wm.shell.common.magnetictarget.MagnetizedObject.MagneticTarget) r5
            float r6 = r14.getRawX()
            android.graphics.PointF r7 = r5.getCenterOnScreen()
            float r7 = r7.x
            float r6 = r6 - r7
            double r6 = (double) r6
            float r8 = r14.getRawY()
            android.graphics.PointF r9 = r5.getCenterOnScreen()
            float r9 = r9.y
            float r8 = r8 - r9
            double r8 = (double) r8
            double r6 = java.lang.Math.hypot(r6, r8)
            float r6 = (float) r6
            int r5 = r5.getMagneticFieldRadiusPx()
            float r5 = (float) r5
            int r5 = (r6 > r5 ? 1 : (r6 == r5 ? 0 : -1))
            if (r5 >= 0) goto L_0x0098
            r5 = r3
            goto L_0x0099
        L_0x0098:
            r5 = r1
        L_0x0099:
            if (r5 == 0) goto L_0x0063
            goto L_0x009d
        L_0x009c:
            r4 = r2
        L_0x009d:
            r6 = r4
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget r6 = (com.android.p019wm.shell.common.magnetictarget.MagnetizedObject.MagneticTarget) r6
            boolean r0 = r13.getObjectStuckToTarget()
            if (r0 != 0) goto L_0x00aa
            if (r6 == 0) goto L_0x00aa
            r0 = r3
            goto L_0x00ab
        L_0x00aa:
            r0 = r1
        L_0x00ab:
            boolean r4 = r13.getObjectStuckToTarget()
            if (r4 == 0) goto L_0x00bd
            if (r6 == 0) goto L_0x00bd
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget r4 = r13.targetObjectIsStuckTo
            boolean r4 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r4, (java.lang.Object) r6)
            if (r4 != 0) goto L_0x00bd
            r4 = r3
            goto L_0x00be
        L_0x00bd:
            r4 = r1
        L_0x00be:
            r11 = 5
            r12 = 1000(0x3e8, float:1.401E-42)
            if (r0 != 0) goto L_0x00f5
            if (r4 == 0) goto L_0x00c6
            goto L_0x00f5
        L_0x00c6:
            if (r6 != 0) goto L_0x0138
            boolean r0 = r13.getObjectStuckToTarget()
            if (r0 == 0) goto L_0x0138
            android.view.VelocityTracker r0 = r13.velocityTracker
            r0.computeCurrentVelocity(r12)
            r13.cancelAnimations$WMShell_release()
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagnetListener r0 = r13.getMagnetListener()
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget r4 = r13.targetObjectIsStuckTo
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)
            android.view.VelocityTracker r5 = r13.velocityTracker
            float r5 = r5.getXVelocity()
            android.view.VelocityTracker r6 = r13.velocityTracker
            float r6 = r6.getYVelocity()
            r0.onUnstuckFromTarget(r4, r5, r6, r1)
            r13.targetObjectIsStuckTo = r2
            r0 = 2
            r13.vibrateIfEnabled(r0)
            goto L_0x0138
        L_0x00f5:
            android.view.VelocityTracker r4 = r13.velocityTracker
            r4.computeCurrentVelocity(r12)
            android.view.VelocityTracker r4 = r13.velocityTracker
            float r4 = r4.getXVelocity()
            android.view.VelocityTracker r5 = r13.velocityTracker
            float r5 = r5.getYVelocity()
            if (r0 == 0) goto L_0x0113
            float r0 = java.lang.Math.abs((float) r4)
            float r7 = r13.stickToTargetMaxXVelocity
            int r0 = (r0 > r7 ? 1 : (r0 == r7 ? 0 : -1))
            if (r0 <= 0) goto L_0x0113
            return r1
        L_0x0113:
            r13.targetObjectIsStuckTo = r6
            r13.cancelAnimations$WMShell_release()
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagnetListener r0 = r13.getMagnetListener()
            kotlin.jvm.internal.Intrinsics.checkNotNull(r6)
            r0.onStuckToTarget(r6)
            kotlin.jvm.functions.Function5<? super com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget, ? super java.lang.Float, ? super java.lang.Float, ? super java.lang.Boolean, ? super kotlin.jvm.functions.Function0<kotlin.Unit>, kotlin.Unit> r0 = r13.animateStuckToTarget
            java.lang.Float r7 = java.lang.Float.valueOf((float) r4)
            java.lang.Float r8 = java.lang.Float.valueOf((float) r5)
            java.lang.Boolean r9 = java.lang.Boolean.valueOf((boolean) r1)
            r10 = 0
            r5 = r0
            r5.invoke(r6, r7, r8, r9, r10)
            r13.vibrateIfEnabled(r11)
        L_0x0138:
            int r0 = r14.getAction()
            if (r0 != r3) goto L_0x01cb
            android.view.VelocityTracker r0 = r13.velocityTracker
            r0.computeCurrentVelocity(r12)
            android.view.VelocityTracker r0 = r13.velocityTracker
            float r0 = r0.getXVelocity()
            android.view.VelocityTracker r4 = r13.velocityTracker
            float r10 = r4.getYVelocity()
            r13.cancelAnimations$WMShell_release()
            boolean r4 = r13.getObjectStuckToTarget()
            if (r4 == 0) goto L_0x017e
            float r14 = -r10
            float r1 = r13.flingUnstuckFromTargetMinVelocity
            int r14 = (r14 > r1 ? 1 : (r14 == r1 ? 0 : -1))
            if (r14 <= 0) goto L_0x016c
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagnetListener r14 = r13.getMagnetListener()
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget r1 = r13.targetObjectIsStuckTo
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            r14.onUnstuckFromTarget(r1, r0, r10, r3)
            goto L_0x017b
        L_0x016c:
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagnetListener r14 = r13.getMagnetListener()
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget r0 = r13.targetObjectIsStuckTo
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            r14.onReleasedInTarget(r0)
            r13.vibrateIfEnabled(r11)
        L_0x017b:
            r13.targetObjectIsStuckTo = r2
            return r3
        L_0x017e:
            java.util.ArrayList<com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget> r4 = r13.associatedTargets
            java.lang.Iterable r4 = (java.lang.Iterable) r4
            java.util.Iterator r11 = r4.iterator()
        L_0x0186:
            boolean r4 = r11.hasNext()
            if (r4 == 0) goto L_0x01a5
            java.lang.Object r12 = r11.next()
            r5 = r12
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget r5 = (com.android.p019wm.shell.common.magnetictarget.MagnetizedObject.MagneticTarget) r5
            float r6 = r14.getRawX()
            float r7 = r14.getRawY()
            r4 = r13
            r8 = r0
            r9 = r10
            boolean r4 = r4.isForcefulFlingTowardsTarget(r5, r6, r7, r8, r9)
            if (r4 == 0) goto L_0x0186
            r2 = r12
        L_0x01a5:
            r5 = r2
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget r5 = (com.android.p019wm.shell.common.magnetictarget.MagnetizedObject.MagneticTarget) r5
            if (r5 == 0) goto L_0x01ca
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagnetListener r14 = r13.getMagnetListener()
            r14.onStuckToTarget(r5)
            r13.targetObjectIsStuckTo = r5
            kotlin.jvm.functions.Function5<? super com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget, ? super java.lang.Float, ? super java.lang.Float, ? super java.lang.Boolean, ? super kotlin.jvm.functions.Function0<kotlin.Unit>, kotlin.Unit> r4 = r13.animateStuckToTarget
            java.lang.Float r6 = java.lang.Float.valueOf((float) r0)
            java.lang.Float r7 = java.lang.Float.valueOf((float) r10)
            java.lang.Boolean r8 = java.lang.Boolean.valueOf((boolean) r3)
            com.android.wm.shell.common.magnetictarget.MagnetizedObject$maybeConsumeMotionEvent$1 r9 = new com.android.wm.shell.common.magnetictarget.MagnetizedObject$maybeConsumeMotionEvent$1
            r9.<init>(r13, r5)
            r4.invoke(r5, r6, r7, r8, r9)
            return r3
        L_0x01ca:
            return r1
        L_0x01cb:
            boolean r13 = r13.getObjectStuckToTarget()
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.common.magnetictarget.MagnetizedObject.maybeConsumeMotionEvent(android.view.MotionEvent):boolean");
    }

    /* access modifiers changed from: private */
    public final void vibrateIfEnabled(int i) {
        if (this.hapticsEnabled) {
            this.vibrator.vibrate(VibrationEffect.createPredefined(i), this.vibrationAttributes);
        }
    }

    private final void addMovement(MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        this.velocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    static /* synthetic */ void animateStuckToTargetInternal$default(MagnetizedObject magnetizedObject, MagneticTarget magneticTarget, float f, float f2, boolean z, Function0 function0, int i, Object obj) {
        if (obj == null) {
            if ((i & 16) != 0) {
                function0 = null;
            }
            magnetizedObject.animateStuckToTargetInternal(magneticTarget, f, f2, z, function0);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: animateStuckToTargetInternal");
    }

    /* access modifiers changed from: private */
    public final void animateStuckToTargetInternal(MagneticTarget magneticTarget, float f, float f2, boolean z, Function0<Unit> function0) {
        magneticTarget.updateLocationOnScreen();
        getLocationOnScreen(this.underlyingObject, this.objectLocationOnScreen);
        float width = (magneticTarget.getCenterOnScreen().x - (getWidth(this.underlyingObject) / 2.0f)) - ((float) this.objectLocationOnScreen[0]);
        float height = (magneticTarget.getCenterOnScreen().y - (getHeight(this.underlyingObject) / 2.0f)) - ((float) this.objectLocationOnScreen[1]);
        PhysicsAnimator.SpringConfig springConfig2 = z ? this.flungIntoTargetSpringConfig : this.springConfig;
        cancelAnimations$WMShell_release();
        PhysicsAnimator<T> physicsAnimator = this.animator;
        FloatPropertyCompat floatPropertyCompat = this.xProperty;
        PhysicsAnimator<T> spring = physicsAnimator.spring(floatPropertyCompat, floatPropertyCompat.getValue(this.underlyingObject) + width, f, springConfig2);
        FloatPropertyCompat<? super T> floatPropertyCompat2 = this.yProperty;
        spring.spring(floatPropertyCompat2, floatPropertyCompat2.getValue(this.underlyingObject) + height, f2, springConfig2);
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
            this.animator.withEndActions((Function0<Unit>[]) new Function0[]{function0});
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
        float width = (((float) magneticTarget.getTargetView().getWidth()) * this.flingToTargetWidthPercent) / ((float) 2);
        if (f <= magneticTarget.getCenterOnScreen().x - width || f >= magneticTarget.getCenterOnScreen().x + width) {
            return false;
        }
        return true;
    }

    public final void cancelAnimations$WMShell_release() {
        this.animator.cancel(this.xProperty, this.yProperty);
    }

    public final void updateTargetViews$WMShell_release() {
        for (MagneticTarget updateLocationOnScreen : this.associatedTargets) {
            updateLocationOnScreen.updateLocationOnScreen();
        }
        if (this.associatedTargets.size() > 0) {
            this.touchSlop = ViewConfiguration.get(this.associatedTargets.get(0).getTargetView().getContext()).getScaledTouchSlop();
        }
    }

    @Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010\u0013\u001a\u00020\u0014R\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001a\u0010\u0004\u001a\u00020\u0005X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, mo64987d2 = {"Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagneticTarget;", "", "targetView", "Landroid/view/View;", "magneticFieldRadiusPx", "", "(Landroid/view/View;I)V", "centerOnScreen", "Landroid/graphics/PointF;", "getCenterOnScreen", "()Landroid/graphics/PointF;", "getMagneticFieldRadiusPx", "()I", "setMagneticFieldRadiusPx", "(I)V", "getTargetView", "()Landroid/view/View;", "tempLoc", "", "updateLocationOnScreen", "", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.common.magnetictarget.MagnetizedObject$MagneticTarget */
    /* compiled from: MagnetizedObject.kt */
    public static final class MagneticTarget {
        private final PointF centerOnScreen = new PointF();
        private int magneticFieldRadiusPx;
        private final View targetView;
        private final int[] tempLoc = new int[2];

        public MagneticTarget(View view, int i) {
            Intrinsics.checkNotNullParameter(view, "targetView");
            this.targetView = view;
            this.magneticFieldRadiusPx = i;
        }

        public final View getTargetView() {
            return this.targetView;
        }

        public final int getMagneticFieldRadiusPx() {
            return this.magneticFieldRadiusPx;
        }

        public final void setMagneticFieldRadiusPx(int i) {
            this.magneticFieldRadiusPx = i;
        }

        public final PointF getCenterOnScreen() {
            return this.centerOnScreen;
        }

        public final void updateLocationOnScreen() {
            this.targetView.post(new MagnetizedObject$MagneticTarget$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        /* renamed from: updateLocationOnScreen$lambda-0  reason: not valid java name */
        public static final void m3452updateLocationOnScreen$lambda0(MagneticTarget magneticTarget) {
            Intrinsics.checkNotNullParameter(magneticTarget, "this$0");
            magneticTarget.targetView.getLocationOnScreen(magneticTarget.tempLoc);
            magneticTarget.centerOnScreen.set((((float) magneticTarget.tempLoc[0]) + (((float) magneticTarget.targetView.getWidth()) / 2.0f)) - magneticTarget.targetView.getTranslationX(), (((float) magneticTarget.tempLoc[1]) + (((float) magneticTarget.targetView.getHeight()) / 2.0f)) - magneticTarget.targetView.getTranslationY());
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J%\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00050\u0004\"\b\b\u0001\u0010\u0005*\u00020\u00062\u0006\u0010\u0007\u001a\u0002H\u0005H\u0007¢\u0006\u0002\u0010\b¨\u0006\t"}, mo64987d2 = {"Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$Companion;", "", "()V", "magnetizeView", "Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject;", "T", "Landroid/view/View;", "view", "(Landroid/view/View;)Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject;", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.common.magnetictarget.MagnetizedObject$Companion */
    /* compiled from: MagnetizedObject.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final <T extends View> MagnetizedObject<T> magnetizeView(T t) {
            Intrinsics.checkNotNullParameter(t, "view");
            return new MagnetizedObject$Companion$magnetizeView$1(t, t.getContext(), DynamicAnimation.TRANSLATION_X, DynamicAnimation.TRANSLATION_Y);
        }
    }
}
