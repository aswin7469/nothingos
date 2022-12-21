package com.android.p019wm.shell.animation;

import android.util.ArrayMap;
import android.util.Log;
import androidx.constraintlayout.motion.widget.Key;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.FrameCallbackScheduler;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u001f\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 i*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u0007hijklmnB\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00028\u0000¢\u0006\u0002\u0010\u0004J\u001a\u00104\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\f\u00105\u001a\b\u0012\u0004\u0012\u00028\u00000\u001aJ\u001a\u00106\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\f\u00105\u001a\b\u0012\u0004\u0012\u00028\u00000.J\u001c\u00107\u001a\u0002082\u0014\u00109\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b0\u0007J\u0006\u0010:\u001a\u00020\tJ/\u0010:\u001a\u00020\t2\"\u00109\u001a\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b0;\"\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b¢\u0006\u0002\u0010<J#\u0010=\u001a\u00020\t2\u0014\u00109\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b0\u0007H\u0000¢\u0006\u0002\b>J\b\u0010?\u001a\u00020\tH\u0002J(\u0010@\u001a\u0006\u0012\u0002\b\u00030A2\n\u0010B\u001a\u0006\u0012\u0002\b\u00030A2\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\bH\u0002J.\u0010D\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010E\u001a\u00020F2\b\b\u0002\u0010G\u001a\u00020\u0011JB\u0010D\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010E\u001a\u00020F2\b\b\u0002\u0010H\u001a\u00020F2\b\b\u0002\u0010I\u001a\u00020F2\b\b\u0002\u0010J\u001a\u00020FJ@\u0010K\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010E\u001a\u00020F2\u0006\u0010L\u001a\u00020\u00112\u0006\u0010M\u001a\u00020\u00132\b\b\u0002\u0010N\u001a\u000208H\u0007J\u001b\u0010O\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b0\u0007H\u0000¢\u0006\u0002\bPJ%\u0010Q\u001a\u00020\u001d2\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010\u0003\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010RJ%\u0010S\u001a\u00020&2\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010\u0003\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010TJ\u0016\u0010U\u001a\u0002082\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\bJ\u0006\u0010V\u001a\u000208J\u0010\u0010W\u001a\u0002082\u0006\u0010X\u001a\u00020FH\u0002J\u000e\u0010Y\u001a\u00020\t2\u0006\u0010Z\u001a\u00020\u000fJ\u000e\u0010[\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0011J\u000e\u0010\\\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u0013J$\u0010]\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010^\u001a\u00020FJ.\u0010]\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010^\u001a\u00020F2\b\b\u0002\u0010G\u001a\u00020\u0013J6\u0010]\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010^\u001a\u00020F2\u0006\u0010E\u001a\u00020F2\b\b\u0002\u0010G\u001a\u00020\u0013JB\u0010]\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u000e\u0010C\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b2\u0006\u0010^\u001a\u00020F2\b\b\u0002\u0010E\u001a\u00020F2\b\b\u0002\u0010_\u001a\u00020F2\b\b\u0002\u0010`\u001a\u00020FJ\u0006\u0010a\u001a\u00020\tJ\r\u0010b\u001a\u00020\tH\u0000¢\u0006\u0002\bcJA\u0010d\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002.\u0010\u0014\u001a\u0018\u0012\u0014\b\u0001\u0012\u0010\u0012\u0004\u0012\u00020\t\u0018\u00010\u0016j\u0004\u0018\u0001`\u00170;\"\u0010\u0012\u0004\u0012\u00020\t\u0018\u00010\u0016j\u0004\u0018\u0001`\u0017¢\u0006\u0002\u0010eJ)\u0010d\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\u0016\u0010\u0014\u001a\f\u0012\b\b\u0001\u0012\u0004\u0018\u00010f0;\"\u0004\u0018\u00010f¢\u0006\u0002\u0010gR4\u0010\u0005\u001a\u001c\u0012\u0012\u0012\u0010\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b0\u0007\u0012\u0004\u0012\u00020\t0\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R2\u0010\u0014\u001a&\u0012\u000e\u0012\f\u0012\u0004\u0012\u00020\t0\u0016j\u0002`\u00170\u0015j\u0012\u0012\u000e\u0012\f\u0012\u0004\u0012\u00020\t0\u0016j\u0002`\u0017`\u0018X\u0004¢\u0006\u0002\n\u0000R*\u0010\u0019\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u001a0\u0015j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u001a`\u0018X\u0004¢\u0006\u0002\n\u0000R\"\u0010\u001b\u001a\u0016\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b\u0012\u0004\u0012\u00020\u001d0\u001cX\u0004¢\u0006\u0002\n\u0000R\"\u0010\u001e\u001a\u0016\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b\u0012\u0004\u0012\u00020\u00110\u001cX\u0004¢\u0006\u0002\n\u0000R>\u0010\u001f\u001a&\u0012\u000e\u0012\f0 R\b\u0012\u0004\u0012\u00028\u00000\u00000\u0015j\u0012\u0012\u000e\u0012\f0 R\b\u0012\u0004\u0012\u00028\u00000\u0000`\u0018X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\"\u0010%\u001a\u0016\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b\u0012\u0004\u0012\u00020&0\u001cX\u0004¢\u0006\u0002\n\u0000R\"\u0010'\u001a\u0016\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b\u0012\u0004\u0012\u00020\u00130\u001cX\u0004¢\u0006\u0002\n\u0000R \u0010(\u001a\b\u0012\u0004\u0012\u00020\t0\u0016X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R*\u0010-\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000.0\u0015j\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000.`\u0018X\u0004¢\u0006\u0002\n\u0000R\u001f\u0010/\u001a\u0010\u0012\f\u0012\n 1*\u0004\u0018\u00018\u00008\u000000¢\u0006\b\n\u0000\u001a\u0004\b2\u00103¨\u0006o"}, mo64987d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimator;", "T", "", "target", "(Ljava/lang/Object;)V", "cancelAction", "Lkotlin/Function1;", "", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "", "getCancelAction$WMShell_release", "()Lkotlin/jvm/functions/Function1;", "setCancelAction$WMShell_release", "(Lkotlin/jvm/functions/Function1;)V", "customScheduler", "Landroidx/dynamicanimation/animation/FrameCallbackScheduler;", "defaultFling", "Lcom/android/wm/shell/animation/PhysicsAnimator$FlingConfig;", "defaultSpring", "Lcom/android/wm/shell/animation/PhysicsAnimator$SpringConfig;", "endActions", "Ljava/util/ArrayList;", "Lkotlin/Function0;", "Lcom/android/wm/shell/animation/EndAction;", "Lkotlin/collections/ArrayList;", "endListeners", "Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "flingAnimations", "Landroid/util/ArrayMap;", "Landroidx/dynamicanimation/animation/FlingAnimation;", "flingConfigs", "internalListeners", "Lcom/android/wm/shell/animation/PhysicsAnimator$InternalListener;", "getInternalListeners$WMShell_release", "()Ljava/util/ArrayList;", "setInternalListeners$WMShell_release", "(Ljava/util/ArrayList;)V", "springAnimations", "Landroidx/dynamicanimation/animation/SpringAnimation;", "springConfigs", "startAction", "getStartAction$WMShell_release", "()Lkotlin/jvm/functions/Function0;", "setStartAction$WMShell_release", "(Lkotlin/jvm/functions/Function0;)V", "updateListeners", "Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;", "weakTarget", "Ljava/lang/ref/WeakReference;", "kotlin.jvm.PlatformType", "getWeakTarget", "()Ljava/lang/ref/WeakReference;", "addEndListener", "listener", "addUpdateListener", "arePropertiesAnimating", "", "properties", "cancel", "", "([Landroidx/dynamicanimation/animation/FloatPropertyCompat;)V", "cancelInternal", "cancelInternal$WMShell_release", "clearAnimator", "configureDynamicAnimation", "Landroidx/dynamicanimation/animation/DynamicAnimation;", "anim", "property", "fling", "startVelocity", "", "config", "friction", "min", "max", "flingThenSpring", "flingConfig", "springConfig", "flingMustReachMinOrMax", "getAnimatedProperties", "getAnimatedProperties$WMShell_release", "getFlingAnimation", "(Landroidx/dynamicanimation/animation/FloatPropertyCompat;Ljava/lang/Object;)Landroidx/dynamicanimation/animation/FlingAnimation;", "getSpringAnimation", "(Landroidx/dynamicanimation/animation/FloatPropertyCompat;Ljava/lang/Object;)Landroidx/dynamicanimation/animation/SpringAnimation;", "isPropertyAnimating", "isRunning", "isValidValue", "value", "setCustomScheduler", "scheduler", "setDefaultFlingConfig", "setDefaultSpringConfig", "spring", "toPosition", "stiffness", "dampingRatio", "start", "startInternal", "startInternal$WMShell_release", "withEndActions", "([Lkotlin/jvm/functions/Function0;)Lcom/android/wm/shell/animation/PhysicsAnimator;", "Ljava/lang/Runnable;", "([Ljava/lang/Runnable;)Lcom/android/wm/shell/animation/PhysicsAnimator;", "AnimationUpdate", "Companion", "EndListener", "FlingConfig", "InternalListener", "SpringConfig", "UpdateListener", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimator */
/* compiled from: PhysicsAnimator.kt */
public final class PhysicsAnimator<T> {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static Function1<Object, ? extends PhysicsAnimator<?>> instanceConstructor = PhysicsAnimator$Companion$instanceConstructor$1.INSTANCE;
    private Function1<? super Set<? extends FloatPropertyCompat<? super T>>, Unit> cancelAction;
    /* access modifiers changed from: private */
    public FrameCallbackScheduler customScheduler;
    private FlingConfig defaultFling;
    private SpringConfig defaultSpring;
    private final ArrayList<Function0<Unit>> endActions;
    private final ArrayList<EndListener<T>> endListeners;
    private final ArrayMap<FloatPropertyCompat<? super T>, FlingAnimation> flingAnimations;
    private final ArrayMap<FloatPropertyCompat<? super T>, FlingConfig> flingConfigs;
    private ArrayList<PhysicsAnimator<T>.InternalListener> internalListeners;
    private final ArrayMap<FloatPropertyCompat<? super T>, SpringAnimation> springAnimations;
    private final ArrayMap<FloatPropertyCompat<? super T>, SpringConfig> springConfigs;
    private Function0<Unit> startAction;
    private final ArrayList<UpdateListener<T>> updateListeners;
    private final WeakReference<T> weakTarget;

    @Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\bf\u0018\u0000*\u0004\b\u0001\u0010\u00012\u00020\u0002JM\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00028\u00012\u000e\u0010\u0006\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00010\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\tH&¢\u0006\u0002\u0010\u000fø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0010À\u0006\u0001"}, mo64987d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "T", "", "onAnimationEnd", "", "target", "property", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "wasFling", "", "canceled", "finalValue", "", "finalVelocity", "allRelevantPropertyAnimsEnded", "(Ljava/lang/Object;Landroidx/dynamicanimation/animation/FloatPropertyCompat;ZZFFZ)V", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$EndListener */
    /* compiled from: PhysicsAnimator.kt */
    public interface EndListener<T> {
        void onAnimationEnd(T t, FloatPropertyCompat<? super T> floatPropertyCompat, boolean z, boolean z2, float f, float f2, boolean z3);
    }

    @Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000*\u0004\b\u0001\u0010\u00012\u00020\u0002J;\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00028\u00012$\u0010\u0006\u001a \u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00010\b\u0012\u0004\u0012\u00020\t0\u0007j\b\u0012\u0004\u0012\u00028\u0001`\nH&¢\u0006\u0002\u0010\u000bø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\fÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;", "T", "", "onAnimationUpdateForProperty", "", "target", "values", "Landroid/util/ArrayMap;", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "Lcom/android/wm/shell/animation/UpdateMap;", "(Ljava/lang/Object;Landroid/util/ArrayMap;)V", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$UpdateListener */
    /* compiled from: PhysicsAnimator.kt */
    public interface UpdateListener<T> {
        void onAnimationUpdateForProperty(T t, ArrayMap<FloatPropertyCompat<? super T>, AnimationUpdate> arrayMap);
    }

    public /* synthetic */ PhysicsAnimator(Object obj, DefaultConstructorMarker defaultConstructorMarker) {
        this(obj);
    }

    @JvmStatic
    public static final float estimateFlingEndValue(float f, float f2, FlingConfig flingConfig) {
        return Companion.estimateFlingEndValue(f, f2, flingConfig);
    }

    @JvmStatic
    public static final <T> PhysicsAnimator<T> getInstance(T t) {
        return Companion.getInstance(t);
    }

    @JvmStatic
    public static final String getReadablePropertyName(FloatPropertyCompat<?> floatPropertyCompat) {
        return Companion.getReadablePropertyName(floatPropertyCompat);
    }

    private final boolean isValidValue(float f) {
        return f < Float.MAX_VALUE && f > -3.4028235E38f;
    }

    @JvmStatic
    public static final void setVerboseLogging(boolean z) {
        Companion.setVerboseLogging(z);
    }

    public final PhysicsAnimator<T> flingThenSpring(FloatPropertyCompat<? super T> floatPropertyCompat, float f, FlingConfig flingConfig, SpringConfig springConfig) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        Intrinsics.checkNotNullParameter(flingConfig, "flingConfig");
        Intrinsics.checkNotNullParameter(springConfig, "springConfig");
        return flingThenSpring$default(this, floatPropertyCompat, f, flingConfig, springConfig, false, 16, (Object) null);
    }

    private PhysicsAnimator(T t) {
        this.weakTarget = new WeakReference<>(t);
        this.springAnimations = new ArrayMap<>();
        this.flingAnimations = new ArrayMap<>();
        this.springConfigs = new ArrayMap<>();
        this.flingConfigs = new ArrayMap<>();
        this.updateListeners = new ArrayList<>();
        this.endListeners = new ArrayList<>();
        this.endActions = new ArrayList<>();
        this.defaultSpring = PhysicsAnimatorKt.globalDefaultSpring;
        this.defaultFling = PhysicsAnimatorKt.globalDefaultFling;
        this.internalListeners = new ArrayList<>();
        this.startAction = new PhysicsAnimator$startAction$1(this);
        this.cancelAction = new PhysicsAnimator$cancelAction$1(this);
    }

    public final WeakReference<T> getWeakTarget() {
        return this.weakTarget;
    }

    @Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "", "value", "", "velocity", "(FF)V", "getValue", "()F", "getVelocity", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$AnimationUpdate */
    /* compiled from: PhysicsAnimator.kt */
    public static final class AnimationUpdate {
        private final float value;
        private final float velocity;

        public static /* synthetic */ AnimationUpdate copy$default(AnimationUpdate animationUpdate, float f, float f2, int i, Object obj) {
            if ((i & 1) != 0) {
                f = animationUpdate.value;
            }
            if ((i & 2) != 0) {
                f2 = animationUpdate.velocity;
            }
            return animationUpdate.copy(f, f2);
        }

        public final float component1() {
            return this.value;
        }

        public final float component2() {
            return this.velocity;
        }

        public final AnimationUpdate copy(float f, float f2) {
            return new AnimationUpdate(f, f2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AnimationUpdate)) {
                return false;
            }
            AnimationUpdate animationUpdate = (AnimationUpdate) obj;
            return Intrinsics.areEqual((Object) Float.valueOf(this.value), (Object) Float.valueOf(animationUpdate.value)) && Intrinsics.areEqual((Object) Float.valueOf(this.velocity), (Object) Float.valueOf(animationUpdate.velocity));
        }

        public int hashCode() {
            return (Float.hashCode(this.value) * 31) + Float.hashCode(this.velocity);
        }

        public String toString() {
            return "AnimationUpdate(value=" + this.value + ", velocity=" + this.velocity + ')';
        }

        public AnimationUpdate(float f, float f2) {
            this.value = f;
            this.velocity = f2;
        }

        public final float getValue() {
            return this.value;
        }

        public final float getVelocity() {
            return this.velocity;
        }
    }

    public final ArrayList<PhysicsAnimator<T>.InternalListener> getInternalListeners$WMShell_release() {
        return this.internalListeners;
    }

    public final void setInternalListeners$WMShell_release(ArrayList<PhysicsAnimator<T>.InternalListener> arrayList) {
        Intrinsics.checkNotNullParameter(arrayList, "<set-?>");
        this.internalListeners = arrayList;
    }

    public final Function0<Unit> getStartAction$WMShell_release() {
        return this.startAction;
    }

    public final void setStartAction$WMShell_release(Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(function0, "<set-?>");
        this.startAction = function0;
    }

    public final Function1<Set<? extends FloatPropertyCompat<? super T>>, Unit> getCancelAction$WMShell_release() {
        return this.cancelAction;
    }

    public final void setCancelAction$WMShell_release(Function1<? super Set<? extends FloatPropertyCompat<? super T>>, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        this.cancelAction = function1;
    }

    public static /* synthetic */ PhysicsAnimator spring$default(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, float f, float f2, float f3, float f4, int i, Object obj) {
        if ((i & 4) != 0) {
            f2 = 0.0f;
        }
        float f5 = f2;
        if ((i & 8) != 0) {
            f3 = physicsAnimator.defaultSpring.getStiffness();
        }
        float f6 = f3;
        if ((i & 16) != 0) {
            f4 = physicsAnimator.defaultSpring.getDampingRatio$WMShell_release();
        }
        return physicsAnimator.spring(floatPropertyCompat, f, f5, f6, f4);
    }

    public final PhysicsAnimator<T> spring(FloatPropertyCompat<? super T> floatPropertyCompat, float f, float f2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        if (PhysicsAnimatorKt.verboseLogging) {
            Log.d("PhysicsAnimator", "Springing " + Companion.getReadablePropertyName(floatPropertyCompat) + " to " + f + '.');
        }
        this.springConfigs.put(floatPropertyCompat, new SpringConfig(f3, f4, f2, f));
        return this;
    }

    public static /* synthetic */ PhysicsAnimator spring$default(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, float f, float f2, SpringConfig springConfig, int i, Object obj) {
        if ((i & 8) != 0) {
            springConfig = physicsAnimator.defaultSpring;
        }
        return physicsAnimator.spring(floatPropertyCompat, f, f2, springConfig);
    }

    public final PhysicsAnimator<T> spring(FloatPropertyCompat<? super T> floatPropertyCompat, float f, float f2, SpringConfig springConfig) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        Intrinsics.checkNotNullParameter(springConfig, "config");
        return spring(floatPropertyCompat, f, f2, springConfig.getStiffness(), springConfig.getDampingRatio$WMShell_release());
    }

    public static /* synthetic */ PhysicsAnimator spring$default(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, float f, SpringConfig springConfig, int i, Object obj) {
        if ((i & 4) != 0) {
            springConfig = physicsAnimator.defaultSpring;
        }
        return physicsAnimator.spring(floatPropertyCompat, f, springConfig);
    }

    public final PhysicsAnimator<T> spring(FloatPropertyCompat<? super T> floatPropertyCompat, float f, SpringConfig springConfig) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        Intrinsics.checkNotNullParameter(springConfig, "config");
        return spring(floatPropertyCompat, f, 0.0f, springConfig);
    }

    public final PhysicsAnimator<T> spring(FloatPropertyCompat<? super T> floatPropertyCompat, float f) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        return spring$default(this, floatPropertyCompat, f, 0.0f, (SpringConfig) null, 8, (Object) null);
    }

    public static /* synthetic */ PhysicsAnimator fling$default(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, float f, float f2, float f3, float f4, int i, Object obj) {
        if ((i & 4) != 0) {
            f2 = physicsAnimator.defaultFling.getFriction$WMShell_release();
        }
        float f5 = f2;
        if ((i & 8) != 0) {
            f3 = physicsAnimator.defaultFling.getMin();
        }
        float f6 = f3;
        if ((i & 16) != 0) {
            f4 = physicsAnimator.defaultFling.getMax();
        }
        return physicsAnimator.fling(floatPropertyCompat, f, f5, f6, f4);
    }

    public final PhysicsAnimator<T> fling(FloatPropertyCompat<? super T> floatPropertyCompat, float f, float f2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        if (PhysicsAnimatorKt.verboseLogging) {
            Log.d("PhysicsAnimator", "Flinging " + Companion.getReadablePropertyName(floatPropertyCompat) + " with velocity " + f + '.');
        }
        this.flingConfigs.put(floatPropertyCompat, new FlingConfig(f2, f3, f4, f));
        return this;
    }

    public static /* synthetic */ PhysicsAnimator fling$default(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, float f, FlingConfig flingConfig, int i, Object obj) {
        if ((i & 4) != 0) {
            flingConfig = physicsAnimator.defaultFling;
        }
        return physicsAnimator.fling(floatPropertyCompat, f, flingConfig);
    }

    public final PhysicsAnimator<T> fling(FloatPropertyCompat<? super T> floatPropertyCompat, float f, FlingConfig flingConfig) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        Intrinsics.checkNotNullParameter(flingConfig, "config");
        return fling(floatPropertyCompat, f, flingConfig.getFriction$WMShell_release(), flingConfig.getMin(), flingConfig.getMax());
    }

    public static /* synthetic */ PhysicsAnimator flingThenSpring$default(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, float f, FlingConfig flingConfig, SpringConfig springConfig, boolean z, int i, Object obj) {
        if ((i & 16) != 0) {
            z = false;
        }
        return physicsAnimator.flingThenSpring(floatPropertyCompat, f, flingConfig, springConfig, z);
    }

    public final PhysicsAnimator<T> flingThenSpring(FloatPropertyCompat<? super T> floatPropertyCompat, float f, FlingConfig flingConfig, SpringConfig springConfig, boolean z) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        Intrinsics.checkNotNullParameter(flingConfig, "flingConfig");
        Intrinsics.checkNotNullParameter(springConfig, "springConfig");
        T t = this.weakTarget.get();
        if (t == null) {
            Log.w("PhysicsAnimator", "Trying to animate a GC-ed target.");
            return this;
        }
        FlingConfig copy$default = FlingConfig.copy$default(flingConfig, 0.0f, 0.0f, 0.0f, 0.0f, 15, (Object) null);
        SpringConfig copy$default2 = SpringConfig.copy$default(springConfig, 0.0f, 0.0f, 0.0f, 0.0f, 15, (Object) null);
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        float min = i < 0 ? flingConfig.getMin() : flingConfig.getMax();
        if (!z || !isValidValue(min)) {
            copy$default.setStartVelocity$WMShell_release(f);
        } else {
            float value = floatPropertyCompat.getValue(t) + (f / (flingConfig.getFriction$WMShell_release() * 4.2f));
            float min2 = (flingConfig.getMin() + flingConfig.getMax()) / ((float) 2);
            if ((i < 0 && value > min2) || (f > 0.0f && value < min2)) {
                float min3 = value < min2 ? flingConfig.getMin() : flingConfig.getMax();
                if (isValidValue(min3)) {
                    return spring(floatPropertyCompat, min3, f, springConfig);
                }
            }
            float value2 = min - floatPropertyCompat.getValue(t);
            float friction$WMShell_release = flingConfig.getFriction$WMShell_release() * 4.2f * value2;
            if (value2 > 0.0f && f >= 0.0f) {
                f = Math.max(friction$WMShell_release, f);
            } else if (value2 < 0.0f && i <= 0) {
                f = Math.min(friction$WMShell_release, f);
            }
            copy$default.setStartVelocity$WMShell_release(f);
            copy$default2.setFinalPosition$WMShell_release(min);
        }
        this.flingConfigs.put(floatPropertyCompat, copy$default);
        this.springConfigs.put(floatPropertyCompat, copy$default2);
        return this;
    }

    public final PhysicsAnimator<T> addUpdateListener(UpdateListener<T> updateListener) {
        Intrinsics.checkNotNullParameter(updateListener, "listener");
        this.updateListeners.add(updateListener);
        return this;
    }

    public final PhysicsAnimator<T> addEndListener(EndListener<T> endListener) {
        Intrinsics.checkNotNullParameter(endListener, "listener");
        this.endListeners.add(endListener);
        return this;
    }

    public final PhysicsAnimator<T> withEndActions(Function0<Unit>... function0Arr) {
        Intrinsics.checkNotNullParameter(function0Arr, "endActions");
        this.endActions.addAll(ArraysKt.filterNotNull(function0Arr));
        return this;
    }

    public final PhysicsAnimator<T> withEndActions(Runnable... runnableArr) {
        Intrinsics.checkNotNullParameter(runnableArr, "endActions");
        ArrayList<Function0<Unit>> arrayList = this.endActions;
        Iterable<Runnable> filterNotNull = ArraysKt.filterNotNull(runnableArr);
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(filterNotNull, 10));
        for (Runnable physicsAnimator$withEndActions$1$1 : filterNotNull) {
            arrayList2.add(new PhysicsAnimator$withEndActions$1$1(physicsAnimator$withEndActions$1$1));
        }
        arrayList.addAll((List) arrayList2);
        return this;
    }

    public final void setDefaultSpringConfig(SpringConfig springConfig) {
        Intrinsics.checkNotNullParameter(springConfig, "defaultSpring");
        this.defaultSpring = springConfig;
    }

    public final void setDefaultFlingConfig(FlingConfig flingConfig) {
        Intrinsics.checkNotNullParameter(flingConfig, "defaultFling");
        this.defaultFling = flingConfig;
    }

    public final void setCustomScheduler(FrameCallbackScheduler frameCallbackScheduler) {
        Intrinsics.checkNotNullParameter(frameCallbackScheduler, "scheduler");
        this.customScheduler = frameCallbackScheduler;
    }

    public final void start() {
        this.startAction.invoke();
    }

    public final void startInternal$WMShell_release() {
        T t = this.weakTarget.get();
        if (t == null) {
            Log.w("PhysicsAnimator", "Trying to animate a GC-ed object.");
            return;
        }
        ArrayList<Function0> arrayList = new ArrayList<>();
        for (FloatPropertyCompat floatPropertyCompat : getAnimatedProperties$WMShell_release()) {
            FlingConfig flingConfig = this.flingConfigs.get(floatPropertyCompat);
            SpringConfig springConfig = this.springConfigs.get(floatPropertyCompat);
            float value = floatPropertyCompat.getValue(t);
            if (flingConfig != null) {
                arrayList.add(new PhysicsAnimator$startInternal$1(flingConfig, this, floatPropertyCompat, t, value));
            }
            if (springConfig != null) {
                if (flingConfig == null) {
                    SpringAnimation springAnimation = getSpringAnimation(floatPropertyCompat, t);
                    if (this.customScheduler != null && !Intrinsics.areEqual((Object) springAnimation.getScheduler(), (Object) this.customScheduler)) {
                        if (springAnimation.isRunning()) {
                            cancel(floatPropertyCompat);
                        }
                        FrameCallbackScheduler frameCallbackScheduler = this.customScheduler;
                        if (frameCallbackScheduler == null) {
                            frameCallbackScheduler = springAnimation.getScheduler();
                            Intrinsics.checkNotNullExpressionValue(frameCallbackScheduler, "springAnim.scheduler");
                        }
                        springAnimation.setScheduler(frameCallbackScheduler);
                    }
                    springConfig.applyToAnimation$WMShell_release(springAnimation);
                    arrayList.add(new PhysicsAnimator$startInternal$2(springAnimation));
                } else {
                    this.endListeners.add(0, new PhysicsAnimator$startInternal$3(floatPropertyCompat, flingConfig.getMin(), flingConfig.getMax(), springConfig, this));
                }
            }
        }
        this.internalListeners.add(new InternalListener(this, t, getAnimatedProperties$WMShell_release(), new ArrayList(this.updateListeners), new ArrayList(this.endListeners), new ArrayList(this.endActions)));
        for (Function0 invoke : arrayList) {
            invoke.invoke();
        }
        clearAnimator();
    }

    private final void clearAnimator() {
        this.springConfigs.clear();
        this.flingConfigs.clear();
        this.updateListeners.clear();
        this.endListeners.clear();
        this.endActions.clear();
    }

    /* access modifiers changed from: private */
    public final SpringAnimation getSpringAnimation(FloatPropertyCompat<? super T> floatPropertyCompat, T t) {
        Map map = this.springAnimations;
        Object obj = map.get(floatPropertyCompat);
        if (obj == null) {
            obj = (SpringAnimation) configureDynamicAnimation(new SpringAnimation(t, floatPropertyCompat), floatPropertyCompat);
            map.put(floatPropertyCompat, obj);
        }
        Intrinsics.checkNotNullExpressionValue(obj, "springAnimations.getOrPu…    as SpringAnimation })");
        return (SpringAnimation) obj;
    }

    /* access modifiers changed from: private */
    public final FlingAnimation getFlingAnimation(FloatPropertyCompat<? super T> floatPropertyCompat, T t) {
        Map map = this.flingAnimations;
        Object obj = map.get(floatPropertyCompat);
        if (obj == null) {
            obj = (FlingAnimation) configureDynamicAnimation(new FlingAnimation(t, floatPropertyCompat), floatPropertyCompat);
            map.put(floatPropertyCompat, obj);
        }
        Intrinsics.checkNotNullExpressionValue(obj, "flingAnimations.getOrPut…     as FlingAnimation })");
        return (FlingAnimation) obj;
    }

    private final DynamicAnimation<?> configureDynamicAnimation(DynamicAnimation<?> dynamicAnimation, FloatPropertyCompat<? super T> floatPropertyCompat) {
        dynamicAnimation.addUpdateListener(new PhysicsAnimator$$ExternalSyntheticLambda0(this, floatPropertyCompat));
        dynamicAnimation.addEndListener(new PhysicsAnimator$$ExternalSyntheticLambda1(this, floatPropertyCompat, dynamicAnimation));
        return dynamicAnimation;
    }

    /* access modifiers changed from: private */
    /* renamed from: configureDynamicAnimation$lambda-4  reason: not valid java name */
    public static final void m3394configureDynamicAnimation$lambda4(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, DynamicAnimation dynamicAnimation, float f, float f2) {
        Intrinsics.checkNotNullParameter(physicsAnimator, "this$0");
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "$property");
        int size = physicsAnimator.internalListeners.size();
        for (int i = 0; i < size; i++) {
            physicsAnimator.internalListeners.get(i).onInternalAnimationUpdate$WMShell_release(floatPropertyCompat, f, f2);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: configureDynamicAnimation$lambda-5  reason: not valid java name */
    public static final void m3395configureDynamicAnimation$lambda5(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, DynamicAnimation dynamicAnimation, DynamicAnimation dynamicAnimation2, boolean z, float f, float f2) {
        Intrinsics.checkNotNullParameter(physicsAnimator, "this$0");
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "$property");
        Intrinsics.checkNotNullParameter(dynamicAnimation, "$anim");
        CollectionsKt.removeAll(physicsAnimator.internalListeners, new PhysicsAnimator$configureDynamicAnimation$2$1(floatPropertyCompat, z, f, f2, dynamicAnimation));
        if (Intrinsics.areEqual((Object) physicsAnimator.springAnimations.get(floatPropertyCompat), (Object) dynamicAnimation)) {
            physicsAnimator.springAnimations.remove(floatPropertyCompat);
        }
        if (Intrinsics.areEqual((Object) physicsAnimator.flingAnimations.get(floatPropertyCompat), (Object) dynamicAnimation)) {
            physicsAnimator.flingAnimations.remove(floatPropertyCompat);
        }
    }

    @Metadata(mo64986d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\b\b\u0004\u0018\u00002\u00020\u0001Bc\u0012\u0006\u0010\u0002\u001a\u00028\u0000\u0012\u0014\u0010\u0003\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u00050\u0004\u0012\u0012\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\b0\u0007\u0012\u0012\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\n0\u0007\u0012\u0016\u0010\u000b\u001a\u0012\u0012\u000e\u0012\f\u0012\u0004\u0012\u00020\r0\fj\u0002`\u000e0\u0007¢\u0006\u0002\u0010\u000fJ\b\u0010\u0016\u001a\u00020\rH\u0002J=\u0010\u0017\u001a\u00020\u00182\u000e\u0010\u0019\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u00052\u0006\u0010\u001a\u001a\u00020\u00182\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u0018H\u0000¢\u0006\u0002\b\u001fJ-\u0010 \u001a\u00020\r2\u000e\u0010\u0019\u001a\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u00052\u0006\u0010!\u001a\u00020\u001c2\u0006\u0010\"\u001a\u00020\u001cH\u0000¢\u0006\u0002\b#R\u001e\u0010\u000b\u001a\u0012\u0012\u000e\u0012\f\u0012\u0004\u0012\u00020\r0\fj\u0002`\u000e0\u0007X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\n0\u0007X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\u0003\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u00050\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u00028\u0000X\u0004¢\u0006\u0004\n\u0002\u0010\u0012R\"\u0010\u0013\u001a\u0016\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u0005\u0012\u0004\u0012\u00020\u00150\u0014X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0006\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\b0\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006$"}, mo64987d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimator$InternalListener;", "", "target", "properties", "", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "updateListeners", "", "Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;", "endListeners", "Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "endActions", "Lkotlin/Function0;", "", "Lcom/android/wm/shell/animation/EndAction;", "(Lcom/android/wm/shell/animation/PhysicsAnimator;Ljava/lang/Object;Ljava/util/Set;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "numPropertiesAnimating", "", "Ljava/lang/Object;", "undispatchedUpdates", "Landroid/util/ArrayMap;", "Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "maybeDispatchUpdates", "onInternalAnimationEnd", "", "property", "canceled", "finalValue", "", "finalVelocity", "isFling", "onInternalAnimationEnd$WMShell_release", "onInternalAnimationUpdate", "value", "velocity", "onInternalAnimationUpdate$WMShell_release", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$InternalListener */
    /* compiled from: PhysicsAnimator.kt */
    public final class InternalListener {
        private List<? extends Function0<Unit>> endActions;
        private List<? extends EndListener<T>> endListeners;
        private int numPropertiesAnimating;
        private Set<? extends FloatPropertyCompat<? super T>> properties;
        private final T target;
        final /* synthetic */ PhysicsAnimator<T> this$0;
        private final ArrayMap<FloatPropertyCompat<? super T>, AnimationUpdate> undispatchedUpdates = new ArrayMap<>();
        private List<? extends UpdateListener<T>> updateListeners;

        public InternalListener(PhysicsAnimator physicsAnimator, T t, Set<? extends FloatPropertyCompat<? super T>> set, List<? extends UpdateListener<T>> list, List<? extends EndListener<T>> list2, List<? extends Function0<Unit>> list3) {
            Intrinsics.checkNotNullParameter(set, "properties");
            Intrinsics.checkNotNullParameter(list, "updateListeners");
            Intrinsics.checkNotNullParameter(list2, "endListeners");
            Intrinsics.checkNotNullParameter(list3, "endActions");
            this.this$0 = physicsAnimator;
            this.target = t;
            this.properties = set;
            this.updateListeners = list;
            this.endListeners = list2;
            this.endActions = list3;
            this.numPropertiesAnimating = set.size();
        }

        public final void onInternalAnimationUpdate$WMShell_release(FloatPropertyCompat<? super T> floatPropertyCompat, float f, float f2) {
            Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
            if (this.properties.contains(floatPropertyCompat)) {
                this.undispatchedUpdates.put(floatPropertyCompat, new AnimationUpdate(f, f2));
                maybeDispatchUpdates();
            }
        }

        public final boolean onInternalAnimationEnd$WMShell_release(FloatPropertyCompat<? super T> floatPropertyCompat, boolean z, float f, float f2, boolean z2) {
            FloatPropertyCompat<? super T> floatPropertyCompat2 = floatPropertyCompat;
            Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
            if (!this.properties.contains(floatPropertyCompat)) {
                return false;
            }
            this.numPropertiesAnimating--;
            maybeDispatchUpdates();
            if (this.undispatchedUpdates.containsKey(floatPropertyCompat)) {
                for (UpdateListener onAnimationUpdateForProperty : this.updateListeners) {
                    T t = this.target;
                    ArrayMap arrayMap = new ArrayMap();
                    arrayMap.put(floatPropertyCompat, this.undispatchedUpdates.get(floatPropertyCompat));
                    Unit unit = Unit.INSTANCE;
                    onAnimationUpdateForProperty.onAnimationUpdateForProperty(t, arrayMap);
                }
                this.undispatchedUpdates.remove(floatPropertyCompat);
            }
            boolean z3 = !this.this$0.arePropertiesAnimating(this.properties);
            PhysicsAnimator<T> physicsAnimator = this.this$0;
            for (EndListener onAnimationEnd : this.endListeners) {
                onAnimationEnd.onAnimationEnd(this.target, floatPropertyCompat, z2, z, f, f2, z3);
                if (physicsAnimator.isPropertyAnimating(floatPropertyCompat)) {
                    return false;
                }
            }
            if (z3 && !z) {
                for (Function0 invoke : this.endActions) {
                    invoke.invoke();
                }
            }
            return z3;
        }

        private final void maybeDispatchUpdates() {
            if (this.undispatchedUpdates.size() >= this.numPropertiesAnimating && this.undispatchedUpdates.size() > 0) {
                for (UpdateListener onAnimationUpdateForProperty : this.updateListeners) {
                    onAnimationUpdateForProperty.onAnimationUpdateForProperty(this.target, new ArrayMap(this.undispatchedUpdates));
                }
                this.undispatchedUpdates.clear();
            }
        }
    }

    public final boolean isRunning() {
        Set<FloatPropertyCompat<? super T>> keySet = this.springAnimations.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "springAnimations.keys");
        Set<FloatPropertyCompat<? super T>> keySet2 = this.flingAnimations.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet2, "flingAnimations.keys");
        return arePropertiesAnimating(CollectionsKt.union(keySet, keySet2));
    }

    public final boolean isPropertyAnimating(FloatPropertyCompat<? super T> floatPropertyCompat) {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        SpringAnimation springAnimation = this.springAnimations.get(floatPropertyCompat);
        if (!(springAnimation != null ? springAnimation.isRunning() : false)) {
            FlingAnimation flingAnimation = this.flingAnimations.get(floatPropertyCompat);
            if (flingAnimation != null ? flingAnimation.isRunning() : false) {
                return true;
            }
            return false;
        }
        return true;
    }

    public final boolean arePropertiesAnimating(Set<? extends FloatPropertyCompat<? super T>> set) {
        Intrinsics.checkNotNullParameter(set, "properties");
        Iterable<FloatPropertyCompat> iterable = set;
        if ((iterable instanceof Collection) && ((Collection) iterable).isEmpty()) {
            return false;
        }
        for (FloatPropertyCompat isPropertyAnimating : iterable) {
            if (isPropertyAnimating(isPropertyAnimating)) {
                return true;
            }
        }
        return false;
    }

    public final Set<FloatPropertyCompat<? super T>> getAnimatedProperties$WMShell_release() {
        Set<FloatPropertyCompat<? super T>> keySet = this.springConfigs.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "springConfigs.keys");
        Set<FloatPropertyCompat<? super T>> keySet2 = this.flingConfigs.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet2, "flingConfigs.keys");
        return CollectionsKt.union(keySet, keySet2);
    }

    public final void cancelInternal$WMShell_release(Set<? extends FloatPropertyCompat<? super T>> set) {
        Intrinsics.checkNotNullParameter(set, "properties");
        for (FloatPropertyCompat floatPropertyCompat : set) {
            FlingAnimation flingAnimation = this.flingAnimations.get(floatPropertyCompat);
            if (flingAnimation != null) {
                flingAnimation.cancel();
            }
            SpringAnimation springAnimation = this.springAnimations.get(floatPropertyCompat);
            if (springAnimation != null) {
                springAnimation.cancel();
            }
        }
    }

    public final void cancel() {
        Function1<? super Set<? extends FloatPropertyCompat<? super T>>, Unit> function1 = this.cancelAction;
        Set<FloatPropertyCompat<? super T>> keySet = this.flingAnimations.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "flingAnimations.keys");
        function1.invoke(keySet);
        Function1<? super Set<? extends FloatPropertyCompat<? super T>>, Unit> function12 = this.cancelAction;
        Set<FloatPropertyCompat<? super T>> keySet2 = this.springAnimations.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet2, "springAnimations.keys");
        function12.invoke(keySet2);
    }

    public final void cancel(FloatPropertyCompat<? super T>... floatPropertyCompatArr) {
        Intrinsics.checkNotNullParameter(floatPropertyCompatArr, "properties");
        this.cancelAction.invoke(ArraysKt.toSet((T[]) floatPropertyCompatArr));
    }

    @Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u0017\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004¢\u0006\u0002\u0010\u0006B+\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\b\b\u0002\u0010\u0007\u001a\u00020\u0004\u0012\b\b\u0002\u0010\b\u001a\u00020\u0004¢\u0006\u0002\u0010\tJ\u0015\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0000¢\u0006\u0002\b\u0018J\t\u0010\u0019\u001a\u00020\u0004HÆ\u0003J\u000e\u0010\u001a\u001a\u00020\u0004HÀ\u0003¢\u0006\u0002\b\u001bJ\u000e\u0010\u001c\u001a\u00020\u0004HÀ\u0003¢\u0006\u0002\b\u001dJ\u000e\u0010\u001e\u001a\u00020\u0004HÀ\u0003¢\u0006\u0002\b\u001fJ1\u0010 \u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u00042\b\b\u0002\u0010\b\u001a\u00020\u0004HÆ\u0001J\u0013\u0010!\u001a\u00020\"2\b\u0010#\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010$\u001a\u00020%HÖ\u0001J\t\u0010&\u001a\u00020'HÖ\u0001R\u001a\u0010\u0005\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001a\u0010\b\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000b\"\u0004\b\u000f\u0010\rR\u001a\u0010\u0007\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u000b\"\u0004\b\u0011\u0010\rR\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000b\"\u0004\b\u0013\u0010\r¨\u0006("}, mo64987d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimator$SpringConfig;", "", "()V", "stiffness", "", "dampingRatio", "(FF)V", "startVelocity", "finalPosition", "(FFFF)V", "getDampingRatio$WMShell_release", "()F", "setDampingRatio$WMShell_release", "(F)V", "getFinalPosition$WMShell_release", "setFinalPosition$WMShell_release", "getStartVelocity$WMShell_release", "setStartVelocity$WMShell_release", "getStiffness", "setStiffness", "applyToAnimation", "", "anim", "Landroidx/dynamicanimation/animation/SpringAnimation;", "applyToAnimation$WMShell_release", "component1", "component2", "component2$WMShell_release", "component3", "component3$WMShell_release", "component4", "component4$WMShell_release", "copy", "equals", "", "other", "hashCode", "", "toString", "", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$SpringConfig */
    /* compiled from: PhysicsAnimator.kt */
    public static final class SpringConfig {
        private float dampingRatio;
        private float finalPosition;
        private float startVelocity;
        private float stiffness;

        public static /* synthetic */ SpringConfig copy$default(SpringConfig springConfig, float f, float f2, float f3, float f4, int i, Object obj) {
            if ((i & 1) != 0) {
                f = springConfig.stiffness;
            }
            if ((i & 2) != 0) {
                f2 = springConfig.dampingRatio;
            }
            if ((i & 4) != 0) {
                f3 = springConfig.startVelocity;
            }
            if ((i & 8) != 0) {
                f4 = springConfig.finalPosition;
            }
            return springConfig.copy(f, f2, f3, f4);
        }

        public final float component1() {
            return this.stiffness;
        }

        public final float component2$WMShell_release() {
            return this.dampingRatio;
        }

        public final float component3$WMShell_release() {
            return this.startVelocity;
        }

        public final float component4$WMShell_release() {
            return this.finalPosition;
        }

        public final SpringConfig copy(float f, float f2, float f3, float f4) {
            return new SpringConfig(f, f2, f3, f4);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SpringConfig)) {
                return false;
            }
            SpringConfig springConfig = (SpringConfig) obj;
            return Intrinsics.areEqual((Object) Float.valueOf(this.stiffness), (Object) Float.valueOf(springConfig.stiffness)) && Intrinsics.areEqual((Object) Float.valueOf(this.dampingRatio), (Object) Float.valueOf(springConfig.dampingRatio)) && Intrinsics.areEqual((Object) Float.valueOf(this.startVelocity), (Object) Float.valueOf(springConfig.startVelocity)) && Intrinsics.areEqual((Object) Float.valueOf(this.finalPosition), (Object) Float.valueOf(springConfig.finalPosition));
        }

        public int hashCode() {
            return (((((Float.hashCode(this.stiffness) * 31) + Float.hashCode(this.dampingRatio)) * 31) + Float.hashCode(this.startVelocity)) * 31) + Float.hashCode(this.finalPosition);
        }

        public String toString() {
            return "SpringConfig(stiffness=" + this.stiffness + ", dampingRatio=" + this.dampingRatio + ", startVelocity=" + this.startVelocity + ", finalPosition=" + this.finalPosition + ')';
        }

        public SpringConfig(float f, float f2, float f3, float f4) {
            this.stiffness = f;
            this.dampingRatio = f2;
            this.startVelocity = f3;
            this.finalPosition = f4;
        }

        public final float getStiffness() {
            return this.stiffness;
        }

        public final void setStiffness(float f) {
            this.stiffness = f;
        }

        public final float getDampingRatio$WMShell_release() {
            return this.dampingRatio;
        }

        public final void setDampingRatio$WMShell_release(float f) {
            this.dampingRatio = f;
        }

        public final float getStartVelocity$WMShell_release() {
            return this.startVelocity;
        }

        public final void setStartVelocity$WMShell_release(float f) {
            this.startVelocity = f;
        }

        /* JADX INFO: this call moved to the top of the method (can break code semantics) */
        public /* synthetic */ SpringConfig(float f, float f2, float f3, float f4, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(f, f2, (i & 4) != 0 ? 0.0f : f3, (i & 8) != 0 ? PhysicsAnimatorKt.UNSET : f4);
        }

        public final float getFinalPosition$WMShell_release() {
            return this.finalPosition;
        }

        public final void setFinalPosition$WMShell_release(float f) {
            this.finalPosition = f;
        }

        public SpringConfig() {
            this(PhysicsAnimatorKt.globalDefaultSpring.stiffness, PhysicsAnimatorKt.globalDefaultSpring.dampingRatio);
        }

        public SpringConfig(float f, float f2) {
            this(f, f2, 0.0f, 0.0f, 8, (DefaultConstructorMarker) null);
        }

        public final void applyToAnimation$WMShell_release(SpringAnimation springAnimation) {
            Intrinsics.checkNotNullParameter(springAnimation, "anim");
            SpringForce spring = springAnimation.getSpring();
            if (spring == null) {
                spring = new SpringForce();
            }
            spring.setStiffness(this.stiffness);
            spring.setDampingRatio(this.dampingRatio);
            spring.setFinalPosition(this.finalPosition);
            springAnimation.setSpring(spring);
            float f = this.startVelocity;
            if (!(f == 0.0f)) {
                springAnimation.setStartVelocity(f);
            }
        }
    }

    @Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005B\u001f\b\u0016\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0004\u0012\u0006\u0010\u0007\u001a\u00020\u0004¢\u0006\u0002\u0010\bB'\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0004\u0012\u0006\u0010\u0007\u001a\u00020\u0004\u0012\u0006\u0010\t\u001a\u00020\u0004¢\u0006\u0002\u0010\nJ\u0015\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0000¢\u0006\u0002\b\u0018J\u000e\u0010\u0019\u001a\u00020\u0004HÀ\u0003¢\u0006\u0002\b\u001aJ\t\u0010\u001b\u001a\u00020\u0004HÆ\u0003J\t\u0010\u001c\u001a\u00020\u0004HÆ\u0003J\u000e\u0010\u001d\u001a\u00020\u0004HÀ\u0003¢\u0006\u0002\b\u001eJ1\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0006\u001a\u00020\u00042\b\b\u0002\u0010\u0007\u001a\u00020\u00042\b\b\u0002\u0010\t\u001a\u00020\u0004HÆ\u0001J\u0013\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010#\u001a\u00020$HÖ\u0001J\t\u0010%\u001a\u00020&HÖ\u0001R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u0005R\u001a\u0010\u0007\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\f\"\u0004\b\u000f\u0010\u0005R\u001a\u0010\u0006\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\f\"\u0004\b\u0011\u0010\u0005R\u001a\u0010\t\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\f\"\u0004\b\u0013\u0010\u0005¨\u0006'"}, mo64987d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimator$FlingConfig;", "", "()V", "friction", "", "(F)V", "min", "max", "(FFF)V", "startVelocity", "(FFFF)V", "getFriction$WMShell_release", "()F", "setFriction$WMShell_release", "getMax", "setMax", "getMin", "setMin", "getStartVelocity$WMShell_release", "setStartVelocity$WMShell_release", "applyToAnimation", "", "anim", "Landroidx/dynamicanimation/animation/FlingAnimation;", "applyToAnimation$WMShell_release", "component1", "component1$WMShell_release", "component2", "component3", "component4", "component4$WMShell_release", "copy", "equals", "", "other", "hashCode", "", "toString", "", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$FlingConfig */
    /* compiled from: PhysicsAnimator.kt */
    public static final class FlingConfig {
        private float friction;
        private float max;
        private float min;
        private float startVelocity;

        public static /* synthetic */ FlingConfig copy$default(FlingConfig flingConfig, float f, float f2, float f3, float f4, int i, Object obj) {
            if ((i & 1) != 0) {
                f = flingConfig.friction;
            }
            if ((i & 2) != 0) {
                f2 = flingConfig.min;
            }
            if ((i & 4) != 0) {
                f3 = flingConfig.max;
            }
            if ((i & 8) != 0) {
                f4 = flingConfig.startVelocity;
            }
            return flingConfig.copy(f, f2, f3, f4);
        }

        public final float component1$WMShell_release() {
            return this.friction;
        }

        public final float component2() {
            return this.min;
        }

        public final float component3() {
            return this.max;
        }

        public final float component4$WMShell_release() {
            return this.startVelocity;
        }

        public final FlingConfig copy(float f, float f2, float f3, float f4) {
            return new FlingConfig(f, f2, f3, f4);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FlingConfig)) {
                return false;
            }
            FlingConfig flingConfig = (FlingConfig) obj;
            return Intrinsics.areEqual((Object) Float.valueOf(this.friction), (Object) Float.valueOf(flingConfig.friction)) && Intrinsics.areEqual((Object) Float.valueOf(this.min), (Object) Float.valueOf(flingConfig.min)) && Intrinsics.areEqual((Object) Float.valueOf(this.max), (Object) Float.valueOf(flingConfig.max)) && Intrinsics.areEqual((Object) Float.valueOf(this.startVelocity), (Object) Float.valueOf(flingConfig.startVelocity));
        }

        public int hashCode() {
            return (((((Float.hashCode(this.friction) * 31) + Float.hashCode(this.min)) * 31) + Float.hashCode(this.max)) * 31) + Float.hashCode(this.startVelocity);
        }

        public String toString() {
            return "FlingConfig(friction=" + this.friction + ", min=" + this.min + ", max=" + this.max + ", startVelocity=" + this.startVelocity + ')';
        }

        public FlingConfig(float f, float f2, float f3, float f4) {
            this.friction = f;
            this.min = f2;
            this.max = f3;
            this.startVelocity = f4;
        }

        public final float getFriction$WMShell_release() {
            return this.friction;
        }

        public final void setFriction$WMShell_release(float f) {
            this.friction = f;
        }

        public final float getMin() {
            return this.min;
        }

        public final void setMin(float f) {
            this.min = f;
        }

        public final float getMax() {
            return this.max;
        }

        public final void setMax(float f) {
            this.max = f;
        }

        public final float getStartVelocity$WMShell_release() {
            return this.startVelocity;
        }

        public final void setStartVelocity$WMShell_release(float f) {
            this.startVelocity = f;
        }

        public FlingConfig() {
            this(PhysicsAnimatorKt.globalDefaultFling.friction);
        }

        public FlingConfig(float f) {
            this(f, PhysicsAnimatorKt.globalDefaultFling.min, PhysicsAnimatorKt.globalDefaultFling.max);
        }

        public FlingConfig(float f, float f2, float f3) {
            this(f, f2, f3, 0.0f);
        }

        public final void applyToAnimation$WMShell_release(FlingAnimation flingAnimation) {
            Intrinsics.checkNotNullParameter(flingAnimation, "anim");
            flingAnimation.setFriction(this.friction);
            flingAnimation.setMinValue(this.min);
            flingAnimation.setMaxValue(this.max);
            flingAnimation.setStartVelocity(this.startVelocity);
        }
    }

    @Metadata(mo64986d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007J%\u0010\u0010\u001a\b\u0012\u0004\u0012\u0002H\u00110\u0005\"\b\b\u0001\u0010\u0011*\u00020\u00012\u0006\u0010\u0012\u001a\u0002H\u0011H\u0007¢\u0006\u0002\u0010\u0013J\u0014\u0010\u0014\u001a\u00020\u00152\n\u0010\u0016\u001a\u0006\u0012\u0002\b\u00030\u0017H\u0007J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0007R*\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u0001\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00050\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\t¨\u0006\u001c"}, mo64987d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimator$Companion;", "", "()V", "instanceConstructor", "Lkotlin/Function1;", "Lcom/android/wm/shell/animation/PhysicsAnimator;", "getInstanceConstructor$WMShell_release", "()Lkotlin/jvm/functions/Function1;", "setInstanceConstructor$WMShell_release", "(Lkotlin/jvm/functions/Function1;)V", "estimateFlingEndValue", "", "startValue", "startVelocity", "flingConfig", "Lcom/android/wm/shell/animation/PhysicsAnimator$FlingConfig;", "getInstance", "T", "target", "(Ljava/lang/Object;)Lcom/android/wm/shell/animation/PhysicsAnimator;", "getReadablePropertyName", "", "property", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "setVerboseLogging", "", "debug", "", "WMShell_release"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$Companion */
    /* compiled from: PhysicsAnimator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final Function1<Object, PhysicsAnimator<?>> getInstanceConstructor$WMShell_release() {
            return PhysicsAnimator.instanceConstructor;
        }

        public final void setInstanceConstructor$WMShell_release(Function1<Object, ? extends PhysicsAnimator<?>> function1) {
            Intrinsics.checkNotNullParameter(function1, "<set-?>");
            PhysicsAnimator.instanceConstructor = function1;
        }

        @JvmStatic
        public final <T> PhysicsAnimator<T> getInstance(T t) {
            Intrinsics.checkNotNullParameter(t, "target");
            if (!PhysicsAnimatorKt.getAnimators().containsKey(t)) {
                PhysicsAnimatorKt.getAnimators().put(t, getInstanceConstructor$WMShell_release().invoke(t));
            }
            PhysicsAnimator<?> physicsAnimator = PhysicsAnimatorKt.getAnimators().get(t);
            if (physicsAnimator != null) {
                return physicsAnimator;
            }
            throw new NullPointerException("null cannot be cast to non-null type com.android.wm.shell.animation.PhysicsAnimator<T of com.android.wm.shell.animation.PhysicsAnimator.Companion.getInstance>");
        }

        @JvmStatic
        public final void setVerboseLogging(boolean z) {
            PhysicsAnimatorKt.verboseLogging = z;
        }

        @JvmStatic
        public final float estimateFlingEndValue(float f, float f2, FlingConfig flingConfig) {
            Intrinsics.checkNotNullParameter(flingConfig, "flingConfig");
            return Math.min(flingConfig.getMax(), Math.max(flingConfig.getMin(), f + (f2 / (flingConfig.getFriction$WMShell_release() * 4.2f))));
        }

        @JvmStatic
        public final String getReadablePropertyName(FloatPropertyCompat<?> floatPropertyCompat) {
            Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.TRANSLATION_X)) {
                return Key.TRANSLATION_X;
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.TRANSLATION_Y)) {
                return Key.TRANSLATION_Y;
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.TRANSLATION_Z)) {
                return Key.TRANSLATION_Z;
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.SCALE_X)) {
                return Key.SCALE_X;
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.SCALE_Y)) {
                return Key.SCALE_Y;
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.ROTATION)) {
                return Key.ROTATION;
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.ROTATION_X)) {
                return Key.ROTATION_X;
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.ROTATION_Y)) {
                return Key.ROTATION_Y;
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.SCROLL_X)) {
                return "scrollX";
            }
            if (Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.SCROLL_Y)) {
                return "scrollY";
            }
            return Intrinsics.areEqual((Object) floatPropertyCompat, (Object) DynamicAnimation.ALPHA) ? Key.ALPHA : "Custom FloatPropertyCompat instance";
        }
    }
}
