package com.android.wm.shell.animation;

import android.util.ArrayMap;
import android.util.Log;
import androidx.dynamicanimation.animation.AnimationHandler;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
public final class PhysicsAnimator<T> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static Function1<Object, ? extends PhysicsAnimator<?>> instanceConstructor = PhysicsAnimator$Companion$instanceConstructor$1.INSTANCE;
    @NotNull
    private Function1<? super Set<? extends FloatPropertyCompat<? super T>>, Unit> cancelAction;
    @Nullable
    private AnimationHandler customAnimationHandler;
    @NotNull
    private FlingConfig defaultFling;
    @NotNull
    private SpringConfig defaultSpring;
    @NotNull
    private final ArrayList<Function0<Unit>> endActions;
    @NotNull
    private final ArrayList<EndListener<T>> endListeners;
    @NotNull
    private final ArrayMap<FloatPropertyCompat<? super T>, FlingAnimation> flingAnimations;
    @NotNull
    private final ArrayMap<FloatPropertyCompat<? super T>, FlingConfig> flingConfigs;
    @NotNull
    private ArrayList<PhysicsAnimator<T>.InternalListener> internalListeners;
    @NotNull
    private final ArrayMap<FloatPropertyCompat<? super T>, SpringAnimation> springAnimations;
    @NotNull
    private final ArrayMap<FloatPropertyCompat<? super T>, SpringConfig> springConfigs;
    @NotNull
    private Function0<Unit> startAction;
    @NotNull
    private final ArrayList<UpdateListener<T>> updateListeners;
    @NotNull
    private final WeakReference<T> weakTarget;

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes2.dex */
    public interface EndListener<T> {
        void onAnimationEnd(T t, @NotNull FloatPropertyCompat<? super T> floatPropertyCompat, boolean z, boolean z2, float f, float f2, boolean z3);
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes2.dex */
    public interface UpdateListener<T> {
        void onAnimationUpdateForProperty(T t, @NotNull ArrayMap<FloatPropertyCompat<? super T>, AnimationUpdate> arrayMap);
    }

    public /* synthetic */ PhysicsAnimator(Object obj, DefaultConstructorMarker defaultConstructorMarker) {
        this(obj);
    }

    public static final float estimateFlingEndValue(float f, float f2, @NotNull FlingConfig flingConfig) {
        return Companion.estimateFlingEndValue(f, f2, flingConfig);
    }

    @NotNull
    public static final <T> PhysicsAnimator<T> getInstance(@NotNull T t) {
        return Companion.getInstance(t);
    }

    private final boolean isValidValue(float f) {
        return f < Float.MAX_VALUE && f > -3.4028235E38f;
    }

    @NotNull
    public final PhysicsAnimator<T> flingThenSpring(@NotNull FloatPropertyCompat<? super T> property, float f, @NotNull FlingConfig flingConfig, @NotNull SpringConfig springConfig) {
        Intrinsics.checkNotNullParameter(property, "property");
        Intrinsics.checkNotNullParameter(flingConfig, "flingConfig");
        Intrinsics.checkNotNullParameter(springConfig, "springConfig");
        return flingThenSpring$default(this, property, f, flingConfig, springConfig, false, 16, null);
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
        this.defaultSpring = PhysicsAnimatorKt.access$getGlobalDefaultSpring$p();
        this.defaultFling = PhysicsAnimatorKt.access$getGlobalDefaultFling$p();
        this.internalListeners = new ArrayList<>();
        this.startAction = new PhysicsAnimator$startAction$1(this);
        this.cancelAction = new PhysicsAnimator$cancelAction$1(this);
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes2.dex */
    public static final class AnimationUpdate {
        private final float value;
        private final float velocity;

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof AnimationUpdate)) {
                return false;
            }
            AnimationUpdate animationUpdate = (AnimationUpdate) obj;
            return Intrinsics.areEqual(Float.valueOf(this.value), Float.valueOf(animationUpdate.value)) && Intrinsics.areEqual(Float.valueOf(this.velocity), Float.valueOf(animationUpdate.velocity));
        }

        public int hashCode() {
            return (Float.hashCode(this.value) * 31) + Float.hashCode(this.velocity);
        }

        @NotNull
        public String toString() {
            return "AnimationUpdate(value=" + this.value + ", velocity=" + this.velocity + ')';
        }

        public AnimationUpdate(float f, float f2) {
            this.value = f;
            this.velocity = f2;
        }
    }

    @NotNull
    public final ArrayList<PhysicsAnimator<T>.InternalListener> getInternalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
        return this.internalListeners;
    }

    @NotNull
    public final PhysicsAnimator<T> spring(@NotNull FloatPropertyCompat<? super T> property, float f, float f2, float f3, float f4) {
        Intrinsics.checkNotNullParameter(property, "property");
        if (PhysicsAnimatorKt.access$getVerboseLogging$p()) {
            Log.d("PhysicsAnimator", "Springing " + Companion.getReadablePropertyName(property) + " to " + f + '.');
        }
        this.springConfigs.put(property, new SpringConfig(f3, f4, f2, f));
        return this;
    }

    public static /* synthetic */ PhysicsAnimator spring$default(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, float f, float f2, SpringConfig springConfig, int i, Object obj) {
        if ((i & 8) != 0) {
            springConfig = physicsAnimator.defaultSpring;
        }
        return physicsAnimator.spring(floatPropertyCompat, f, f2, springConfig);
    }

    @NotNull
    public final PhysicsAnimator<T> spring(@NotNull FloatPropertyCompat<? super T> property, float f, float f2, @NotNull SpringConfig config) {
        Intrinsics.checkNotNullParameter(property, "property");
        Intrinsics.checkNotNullParameter(config, "config");
        return spring(property, f, f2, config.getStiffness(), config.getDampingRatio$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell());
    }

    @NotNull
    public final PhysicsAnimator<T> spring(@NotNull FloatPropertyCompat<? super T> property, float f, @NotNull SpringConfig config) {
        Intrinsics.checkNotNullParameter(property, "property");
        Intrinsics.checkNotNullParameter(config, "config");
        return spring(property, f, 0.0f, config);
    }

    @NotNull
    public final PhysicsAnimator<T> spring(@NotNull FloatPropertyCompat<? super T> property, float f) {
        Intrinsics.checkNotNullParameter(property, "property");
        return spring$default(this, property, f, 0.0f, null, 8, null);
    }

    public static /* synthetic */ PhysicsAnimator flingThenSpring$default(PhysicsAnimator physicsAnimator, FloatPropertyCompat floatPropertyCompat, float f, FlingConfig flingConfig, SpringConfig springConfig, boolean z, int i, Object obj) {
        if ((i & 16) != 0) {
            z = false;
        }
        return physicsAnimator.flingThenSpring(floatPropertyCompat, f, flingConfig, springConfig, z);
    }

    @NotNull
    public final PhysicsAnimator<T> flingThenSpring(@NotNull FloatPropertyCompat<? super T> property, float f, @NotNull FlingConfig flingConfig, @NotNull SpringConfig springConfig, boolean z) {
        Intrinsics.checkNotNullParameter(property, "property");
        Intrinsics.checkNotNullParameter(flingConfig, "flingConfig");
        Intrinsics.checkNotNullParameter(springConfig, "springConfig");
        Object obj = (T) this.weakTarget.get();
        if (obj == null) {
            Log.w("PhysicsAnimator", "Trying to animate a GC-ed target.");
            return this;
        }
        FlingConfig copy$default = FlingConfig.copy$default(flingConfig, 0.0f, 0.0f, 0.0f, 0.0f, 15, null);
        SpringConfig copy$default2 = SpringConfig.copy$default(springConfig, 0.0f, 0.0f, 0.0f, 0.0f, 15, null);
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        float min = i < 0 ? flingConfig.getMin() : flingConfig.getMax();
        if (z && isValidValue(min)) {
            float value = property.getValue(obj) + (f / (flingConfig.getFriction$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() * 4.2f));
            float min2 = (flingConfig.getMin() + flingConfig.getMax()) / 2;
            if ((i < 0 && value > min2) || (f > 0.0f && value < min2)) {
                float min3 = value < min2 ? flingConfig.getMin() : flingConfig.getMax();
                if (isValidValue(min3)) {
                    return spring(property, min3, f, springConfig);
                }
            }
            float value2 = min - property.getValue(obj);
            float friction$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell = flingConfig.getFriction$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() * 4.2f * value2;
            if (value2 > 0.0f && f >= 0.0f) {
                f = Math.max(friction$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell, f);
            } else if (value2 < 0.0f && i <= 0) {
                f = Math.min(friction$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell, f);
            }
            copy$default.setStartVelocity$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(f);
            copy$default2.setFinalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(min);
        } else {
            copy$default.setStartVelocity$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(f);
        }
        this.flingConfigs.put(property, copy$default);
        this.springConfigs.put(property, copy$default2);
        return this;
    }

    @NotNull
    public final PhysicsAnimator<T> addUpdateListener(@NotNull UpdateListener<T> listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.updateListeners.add(listener);
        return this;
    }

    @NotNull
    public final PhysicsAnimator<T> addEndListener(@NotNull EndListener<T> listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.endListeners.add(listener);
        return this;
    }

    @NotNull
    public final PhysicsAnimator<T> withEndActions(@NotNull Function0<Unit>... endActions) {
        Intrinsics.checkNotNullParameter(endActions, "endActions");
        this.endActions.addAll(ArraysKt.filterNotNull(endActions));
        return this;
    }

    @NotNull
    public final PhysicsAnimator<T> withEndActions(@NotNull Runnable... endActions) {
        Intrinsics.checkNotNullParameter(endActions, "endActions");
        ArrayList<Function0<Unit>> arrayList = this.endActions;
        List<Runnable> filterNotNull = ArraysKt.filterNotNull(endActions);
        ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(filterNotNull, 10));
        for (Runnable runnable : filterNotNull) {
            arrayList2.add(new PhysicsAnimator$withEndActions$1$1(runnable));
        }
        arrayList.addAll(arrayList2);
        return this;
    }

    public final void setDefaultSpringConfig(@NotNull SpringConfig defaultSpring) {
        Intrinsics.checkNotNullParameter(defaultSpring, "defaultSpring");
        this.defaultSpring = defaultSpring;
    }

    public final void setCustomAnimationHandler(@NotNull AnimationHandler handler) {
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.customAnimationHandler = handler;
    }

    public final void start() {
        this.startAction.mo1951invoke();
    }

    public final void startInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
        T t = this.weakTarget.get();
        if (t == null) {
            Log.w("PhysicsAnimator", "Trying to animate a GC-ed object.");
            return;
        }
        ArrayList<Function0> arrayList = new ArrayList();
        for (final FloatPropertyCompat<? super T> floatPropertyCompat : getAnimatedProperties$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell()) {
            FlingConfig flingConfig = this.flingConfigs.get(floatPropertyCompat);
            final SpringConfig springConfig = this.springConfigs.get(floatPropertyCompat);
            float value = floatPropertyCompat.getValue(t);
            if (flingConfig != null) {
                arrayList.add(new PhysicsAnimator$startInternal$1(flingConfig, this, floatPropertyCompat, t, value));
            }
            if (springConfig != null) {
                if (flingConfig == null) {
                    SpringAnimation springAnimation = getSpringAnimation(floatPropertyCompat, t);
                    if (this.customAnimationHandler != null && !Intrinsics.areEqual(springAnimation.getAnimationHandler(), this.customAnimationHandler)) {
                        if (springAnimation.isRunning()) {
                            cancel(floatPropertyCompat);
                        }
                        AnimationHandler animationHandler = this.customAnimationHandler;
                        if (animationHandler == null) {
                            animationHandler = springAnimation.getAnimationHandler();
                            Intrinsics.checkNotNullExpressionValue(animationHandler, "springAnim.animationHandler");
                        }
                        springAnimation.setAnimationHandler(animationHandler);
                    }
                    springConfig.applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(springAnimation);
                    arrayList.add(new PhysicsAnimator$startInternal$2(springAnimation));
                } else {
                    final float min = flingConfig.getMin();
                    final float max = flingConfig.getMax();
                    this.endListeners.add(0, new EndListener<T>() { // from class: com.android.wm.shell.animation.PhysicsAnimator$startInternal$3
                        @Override // com.android.wm.shell.animation.PhysicsAnimator.EndListener
                        public void onAnimationEnd(T t2, @NotNull FloatPropertyCompat<? super T> property, boolean z, boolean z2, float f, float f2, boolean z3) {
                            float f3;
                            SpringAnimation springAnimation2;
                            AnimationHandler animationHandler2;
                            Intrinsics.checkNotNullParameter(property, "property");
                            if (!Intrinsics.areEqual(property, floatPropertyCompat) || !z || z2) {
                                return;
                            }
                            boolean z4 = true;
                            boolean z5 = Math.abs(f2) > 0.0f;
                            boolean z6 = !(min <= f && f <= max);
                            if (!z5 && !z6) {
                                return;
                            }
                            springConfig.setStartVelocity$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(f2);
                            float finalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell = springConfig.getFinalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
                            f3 = PhysicsAnimatorKt.UNSET;
                            if (finalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell != f3) {
                                z4 = false;
                            }
                            if (z4) {
                                if (z5) {
                                    springConfig.setFinalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(f2 < 0.0f ? min : max);
                                } else if (z6) {
                                    PhysicsAnimator.SpringConfig springConfig2 = springConfig;
                                    float f4 = min;
                                    if (f >= f4) {
                                        f4 = max;
                                    }
                                    springConfig2.setFinalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(f4);
                                }
                            }
                            springAnimation2 = this.getSpringAnimation(floatPropertyCompat, t2);
                            animationHandler2 = ((PhysicsAnimator) this).customAnimationHandler;
                            if (animationHandler2 == null) {
                                animationHandler2 = springAnimation2.getAnimationHandler();
                                Intrinsics.checkNotNullExpressionValue(animationHandler2, "springAnim.animationHandler");
                            }
                            springAnimation2.setAnimationHandler(animationHandler2);
                            springConfig.applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(springAnimation2);
                            springAnimation2.start();
                        }
                    });
                }
            }
        }
        this.internalListeners.add(new InternalListener(this, t, getAnimatedProperties$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(), new ArrayList(this.updateListeners), new ArrayList(this.endListeners), new ArrayList(this.endActions)));
        for (Function0 function0 : arrayList) {
            function0.mo1951invoke();
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

    /* JADX INFO: Access modifiers changed from: private */
    public final SpringAnimation getSpringAnimation(FloatPropertyCompat<? super T> floatPropertyCompat, T t) {
        ArrayMap<FloatPropertyCompat<? super T>, SpringAnimation> arrayMap = this.springAnimations;
        SpringAnimation springAnimation = arrayMap.get(floatPropertyCompat);
        if (springAnimation == null) {
            springAnimation = (SpringAnimation) configureDynamicAnimation(new SpringAnimation(t, floatPropertyCompat), floatPropertyCompat);
            arrayMap.put(floatPropertyCompat, springAnimation);
        }
        Intrinsics.checkNotNullExpressionValue(springAnimation, "springAnimations.getOrPut(\n                property,\n                { configureDynamicAnimation(SpringAnimation(target, property), property)\n                        as SpringAnimation })");
        return springAnimation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final FlingAnimation getFlingAnimation(FloatPropertyCompat<? super T> floatPropertyCompat, T t) {
        ArrayMap<FloatPropertyCompat<? super T>, FlingAnimation> arrayMap = this.flingAnimations;
        FlingAnimation flingAnimation = arrayMap.get(floatPropertyCompat);
        if (flingAnimation == null) {
            flingAnimation = (FlingAnimation) configureDynamicAnimation(new FlingAnimation(t, floatPropertyCompat), floatPropertyCompat);
            arrayMap.put(floatPropertyCompat, flingAnimation);
        }
        Intrinsics.checkNotNullExpressionValue(flingAnimation, "flingAnimations.getOrPut(\n                property,\n                { configureDynamicAnimation(FlingAnimation(target, property), property)\n                        as FlingAnimation })");
        return flingAnimation;
    }

    private final DynamicAnimation<?> configureDynamicAnimation(final DynamicAnimation<?> dynamicAnimation, final FloatPropertyCompat<? super T> floatPropertyCompat) {
        dynamicAnimation.addUpdateListener(new DynamicAnimation.OnAnimationUpdateListener(this) { // from class: com.android.wm.shell.animation.PhysicsAnimator$configureDynamicAnimation$1
            final /* synthetic */ PhysicsAnimator<T> this$0;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.this$0 = this;
            }

            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener
            public final void onAnimationUpdate(DynamicAnimation dynamicAnimation2, float f, float f2) {
                int size = this.this$0.getInternalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell().size();
                if (size > 0) {
                    int i = 0;
                    while (true) {
                        int i2 = i + 1;
                        ((PhysicsAnimator.InternalListener) this.this$0.getInternalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell().get(i)).onInternalAnimationUpdate$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(floatPropertyCompat, f, f2);
                        if (i2 >= size) {
                            return;
                        }
                        i = i2;
                    }
                }
            }
        });
        dynamicAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener(this) { // from class: com.android.wm.shell.animation.PhysicsAnimator$configureDynamicAnimation$2
            final /* synthetic */ PhysicsAnimator<T> this$0;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                this.this$0 = this;
            }

            /* compiled from: PhysicsAnimator.kt */
            /* renamed from: com.android.wm.shell.animation.PhysicsAnimator$configureDynamicAnimation$2$1  reason: invalid class name */
            /* loaded from: classes2.dex */
            static final class AnonymousClass1 extends Lambda implements Function1<PhysicsAnimator<T>.InternalListener, Boolean> {
                final /* synthetic */ DynamicAnimation<?> $anim;
                final /* synthetic */ boolean $canceled;
                final /* synthetic */ FloatPropertyCompat<? super T> $property;
                final /* synthetic */ float $value;
                final /* synthetic */ float $velocity;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                AnonymousClass1(FloatPropertyCompat<? super T> floatPropertyCompat, boolean z, float f, float f2, DynamicAnimation<?> dynamicAnimation) {
                    super(1);
                    this.$property = floatPropertyCompat;
                    this.$canceled = z;
                    this.$value = f;
                    this.$velocity = f2;
                    this.$anim = dynamicAnimation;
                }

                @Override // kotlin.jvm.functions.Function1
                /* renamed from: invoke */
                public /* bridge */ /* synthetic */ Boolean mo1949invoke(Object obj) {
                    return Boolean.valueOf(invoke((PhysicsAnimator.InternalListener) obj));
                }

                public final boolean invoke(@NotNull PhysicsAnimator<T>.InternalListener it) {
                    Intrinsics.checkNotNullParameter(it, "it");
                    return it.onInternalAnimationEnd$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(this.$property, this.$canceled, this.$value, this.$velocity, this.$anim instanceof FlingAnimation);
                }
            }

            @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
            public final void onAnimationEnd(DynamicAnimation dynamicAnimation2, boolean z, float f, float f2) {
                ArrayMap arrayMap;
                ArrayMap arrayMap2;
                ArrayMap arrayMap3;
                ArrayMap arrayMap4;
                CollectionsKt__MutableCollectionsKt.removeAll((List) this.this$0.getInternalListeners$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(), (Function1) new AnonymousClass1(floatPropertyCompat, z, f, f2, dynamicAnimation));
                arrayMap = ((PhysicsAnimator) this.this$0).springAnimations;
                if (Intrinsics.areEqual(arrayMap.get(floatPropertyCompat), dynamicAnimation)) {
                    arrayMap4 = ((PhysicsAnimator) this.this$0).springAnimations;
                    arrayMap4.remove(floatPropertyCompat);
                }
                arrayMap2 = ((PhysicsAnimator) this.this$0).flingAnimations;
                if (Intrinsics.areEqual(arrayMap2.get(floatPropertyCompat), dynamicAnimation)) {
                    arrayMap3 = ((PhysicsAnimator) this.this$0).flingAnimations;
                    arrayMap3.remove(floatPropertyCompat);
                }
            }
        });
        return dynamicAnimation;
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes2.dex */
    public final class InternalListener {
        @NotNull
        private List<? extends Function0<Unit>> endActions;
        @NotNull
        private List<? extends EndListener<T>> endListeners;
        private int numPropertiesAnimating;
        @NotNull
        private Set<? extends FloatPropertyCompat<? super T>> properties;
        private final T target;
        final /* synthetic */ PhysicsAnimator<T> this$0;
        @NotNull
        private final ArrayMap<FloatPropertyCompat<? super T>, AnimationUpdate> undispatchedUpdates = new ArrayMap<>();
        @NotNull
        private List<? extends UpdateListener<T>> updateListeners;

        public InternalListener(PhysicsAnimator this$0, @NotNull T t, @NotNull Set<? extends FloatPropertyCompat<? super T>> properties, @NotNull List<? extends UpdateListener<T>> updateListeners, @NotNull List<? extends EndListener<T>> endListeners, List<? extends Function0<Unit>> endActions) {
            Intrinsics.checkNotNullParameter(this$0, "this$0");
            Intrinsics.checkNotNullParameter(properties, "properties");
            Intrinsics.checkNotNullParameter(updateListeners, "updateListeners");
            Intrinsics.checkNotNullParameter(endListeners, "endListeners");
            Intrinsics.checkNotNullParameter(endActions, "endActions");
            this.this$0 = this$0;
            this.target = t;
            this.properties = properties;
            this.updateListeners = updateListeners;
            this.endListeners = endListeners;
            this.endActions = endActions;
            this.numPropertiesAnimating = properties.size();
        }

        public final void onInternalAnimationUpdate$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(@NotNull FloatPropertyCompat<? super T> property, float f, float f2) {
            Intrinsics.checkNotNullParameter(property, "property");
            if (!this.properties.contains(property)) {
                return;
            }
            this.undispatchedUpdates.put(property, new AnimationUpdate(f, f2));
            maybeDispatchUpdates();
        }

        public final boolean onInternalAnimationEnd$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(@NotNull FloatPropertyCompat<? super T> property, boolean z, float f, float f2, boolean z2) {
            Intrinsics.checkNotNullParameter(property, "property");
            if (!this.properties.contains(property)) {
                return false;
            }
            this.numPropertiesAnimating--;
            maybeDispatchUpdates();
            if (this.undispatchedUpdates.containsKey(property)) {
                Iterator<T> it = this.updateListeners.iterator();
                while (it.hasNext()) {
                    T t = this.target;
                    ArrayMap<FloatPropertyCompat<? super T>, AnimationUpdate> arrayMap = new ArrayMap<>();
                    arrayMap.put(property, this.undispatchedUpdates.get(property));
                    Unit unit = Unit.INSTANCE;
                    ((UpdateListener) it.next()).onAnimationUpdateForProperty(t, arrayMap);
                }
                this.undispatchedUpdates.remove(property);
            }
            boolean z3 = !this.this$0.arePropertiesAnimating(this.properties);
            List<? extends EndListener<T>> list = this.endListeners;
            PhysicsAnimator<T> physicsAnimator = this.this$0;
            Iterator<T> it2 = list.iterator();
            while (it2.hasNext()) {
                ((EndListener) it2.next()).onAnimationEnd(this.target, property, z2, z, f, f2, z3);
                if (physicsAnimator.isPropertyAnimating(property)) {
                    return false;
                }
            }
            if (z3 && !z) {
                Iterator<T> it3 = this.endActions.iterator();
                while (it3.hasNext()) {
                    ((Function0) it3.next()).mo1951invoke();
                }
            }
            return z3;
        }

        private final void maybeDispatchUpdates() {
            if (this.undispatchedUpdates.size() < this.numPropertiesAnimating || this.undispatchedUpdates.size() <= 0) {
                return;
            }
            Iterator<T> it = this.updateListeners.iterator();
            while (it.hasNext()) {
                ((UpdateListener) it.next()).onAnimationUpdateForProperty(this.target, new ArrayMap<>(this.undispatchedUpdates));
            }
            this.undispatchedUpdates.clear();
        }
    }

    public final boolean isRunning() {
        Set<FloatPropertyCompat<? super T>> keySet = this.springAnimations.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "springAnimations.keys");
        Set<FloatPropertyCompat<? super T>> keySet2 = this.flingAnimations.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet2, "flingAnimations.keys");
        return arePropertiesAnimating(CollectionsKt.union(keySet, keySet2));
    }

    public final boolean isPropertyAnimating(@NotNull FloatPropertyCompat<? super T> property) {
        Intrinsics.checkNotNullParameter(property, "property");
        SpringAnimation springAnimation = this.springAnimations.get(property);
        if (!(springAnimation == null ? false : springAnimation.isRunning())) {
            FlingAnimation flingAnimation = this.flingAnimations.get(property);
            if (!(flingAnimation == null ? false : flingAnimation.isRunning())) {
                return false;
            }
        }
        return true;
    }

    @NotNull
    public final Set<FloatPropertyCompat<? super T>> getAnimatedProperties$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
        Set<FloatPropertyCompat<? super T>> keySet = this.springConfigs.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "springConfigs.keys");
        Set<FloatPropertyCompat<? super T>> keySet2 = this.flingConfigs.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet2, "flingConfigs.keys");
        return CollectionsKt.union(keySet, keySet2);
    }

    public final void cancelInternal$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(@NotNull Set<? extends FloatPropertyCompat<? super T>> properties) {
        Intrinsics.checkNotNullParameter(properties, "properties");
        for (FloatPropertyCompat<? super T> floatPropertyCompat : properties) {
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
        function1.mo1949invoke(keySet);
        Function1<? super Set<? extends FloatPropertyCompat<? super T>>, Unit> function12 = this.cancelAction;
        Set<FloatPropertyCompat<? super T>> keySet2 = this.springAnimations.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet2, "springAnimations.keys");
        function12.mo1949invoke(keySet2);
    }

    public final void cancel(@NotNull FloatPropertyCompat<? super T>... properties) {
        Intrinsics.checkNotNullParameter(properties, "properties");
        this.cancelAction.mo1949invoke(ArraysKt.toSet(properties));
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes2.dex */
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

        @NotNull
        public final SpringConfig copy(float f, float f2, float f3, float f4) {
            return new SpringConfig(f, f2, f3, f4);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof SpringConfig)) {
                return false;
            }
            SpringConfig springConfig = (SpringConfig) obj;
            return Intrinsics.areEqual(Float.valueOf(this.stiffness), Float.valueOf(springConfig.stiffness)) && Intrinsics.areEqual(Float.valueOf(this.dampingRatio), Float.valueOf(springConfig.dampingRatio)) && Intrinsics.areEqual(Float.valueOf(this.startVelocity), Float.valueOf(springConfig.startVelocity)) && Intrinsics.areEqual(Float.valueOf(this.finalPosition), Float.valueOf(springConfig.finalPosition));
        }

        public int hashCode() {
            return (((((Float.hashCode(this.stiffness) * 31) + Float.hashCode(this.dampingRatio)) * 31) + Float.hashCode(this.startVelocity)) * 31) + Float.hashCode(this.finalPosition);
        }

        @NotNull
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

        public final float getDampingRatio$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
            return this.dampingRatio;
        }

        public final void setStartVelocity$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(float f) {
            this.startVelocity = f;
        }

        public /* synthetic */ SpringConfig(float f, float f2, float f3, float f4, int i, DefaultConstructorMarker defaultConstructorMarker) {
            this(f, f2, (i & 4) != 0 ? 0.0f : f3, (i & 8) != 0 ? PhysicsAnimatorKt.access$getUNSET$p() : f4);
        }

        public final float getFinalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
            return this.finalPosition;
        }

        public final void setFinalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(float f) {
            this.finalPosition = f;
        }

        public SpringConfig() {
            this(PhysicsAnimatorKt.access$getGlobalDefaultSpring$p().stiffness, PhysicsAnimatorKt.access$getGlobalDefaultSpring$p().dampingRatio);
        }

        public SpringConfig(float f, float f2) {
            this(f, f2, 0.0f, 0.0f, 8, null);
        }

        public final void applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(@NotNull SpringAnimation anim) {
            Intrinsics.checkNotNullParameter(anim, "anim");
            SpringForce spring = anim.getSpring();
            if (spring == null) {
                spring = new SpringForce();
            }
            spring.setStiffness(getStiffness());
            spring.setDampingRatio(getDampingRatio$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell());
            spring.setFinalPosition(getFinalPosition$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell());
            Unit unit = Unit.INSTANCE;
            anim.setSpring(spring);
            float f = this.startVelocity;
            if (!(f == 0.0f)) {
                anim.setStartVelocity(f);
            }
        }
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes2.dex */
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

        @NotNull
        public final FlingConfig copy(float f, float f2, float f3, float f4) {
            return new FlingConfig(f, f2, f3, f4);
        }

        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof FlingConfig)) {
                return false;
            }
            FlingConfig flingConfig = (FlingConfig) obj;
            return Intrinsics.areEqual(Float.valueOf(this.friction), Float.valueOf(flingConfig.friction)) && Intrinsics.areEqual(Float.valueOf(this.min), Float.valueOf(flingConfig.min)) && Intrinsics.areEqual(Float.valueOf(this.max), Float.valueOf(flingConfig.max)) && Intrinsics.areEqual(Float.valueOf(this.startVelocity), Float.valueOf(flingConfig.startVelocity));
        }

        public int hashCode() {
            return (((((Float.hashCode(this.friction) * 31) + Float.hashCode(this.min)) * 31) + Float.hashCode(this.max)) * 31) + Float.hashCode(this.startVelocity);
        }

        @NotNull
        public String toString() {
            return "FlingConfig(friction=" + this.friction + ", min=" + this.min + ", max=" + this.max + ", startVelocity=" + this.startVelocity + ')';
        }

        public FlingConfig(float f, float f2, float f3, float f4) {
            this.friction = f;
            this.min = f2;
            this.max = f3;
            this.startVelocity = f4;
        }

        public final float getFriction$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
            return this.friction;
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

        public final float getStartVelocity$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
            return this.startVelocity;
        }

        public final void setStartVelocity$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(float f) {
            this.startVelocity = f;
        }

        public FlingConfig() {
            this(PhysicsAnimatorKt.access$getGlobalDefaultFling$p().friction);
        }

        public FlingConfig(float f) {
            this(f, PhysicsAnimatorKt.access$getGlobalDefaultFling$p().min, PhysicsAnimatorKt.access$getGlobalDefaultFling$p().max);
        }

        public FlingConfig(float f, float f2, float f3) {
            this(f, f2, f3, 0.0f);
        }

        public final void applyToAnimation$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell(@NotNull FlingAnimation anim) {
            Intrinsics.checkNotNullParameter(anim, "anim");
            anim.setFriction(getFriction$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell());
            anim.setMinValue(getMin());
            anim.setMaxValue(getMax());
            anim.setStartVelocity(getStartVelocity$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell());
        }
    }

    /* compiled from: PhysicsAnimator.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @NotNull
        public final Function1<Object, PhysicsAnimator<?>> getInstanceConstructor$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
            return PhysicsAnimator.instanceConstructor;
        }

        @NotNull
        public final <T> PhysicsAnimator<T> getInstance(@NotNull T target) {
            Intrinsics.checkNotNullParameter(target, "target");
            if (!PhysicsAnimatorKt.getAnimators().containsKey(target)) {
                PhysicsAnimatorKt.getAnimators().put(target, getInstanceConstructor$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell().mo1949invoke(target));
            }
            PhysicsAnimator<?> physicsAnimator = PhysicsAnimatorKt.getAnimators().get(target);
            Objects.requireNonNull(physicsAnimator, "null cannot be cast to non-null type com.android.wm.shell.animation.PhysicsAnimator<T of com.android.wm.shell.animation.PhysicsAnimator.Companion.getInstance>");
            return (PhysicsAnimator<T>) physicsAnimator;
        }

        public final float estimateFlingEndValue(float f, float f2, @NotNull FlingConfig flingConfig) {
            Intrinsics.checkNotNullParameter(flingConfig, "flingConfig");
            return Math.min(flingConfig.getMax(), Math.max(flingConfig.getMin(), f + (f2 / (flingConfig.getFriction$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() * 4.2f))));
        }

        @NotNull
        public final String getReadablePropertyName(@NotNull FloatPropertyCompat<?> property) {
            Intrinsics.checkNotNullParameter(property, "property");
            return Intrinsics.areEqual(property, DynamicAnimation.TRANSLATION_X) ? "translationX" : Intrinsics.areEqual(property, DynamicAnimation.TRANSLATION_Y) ? "translationY" : Intrinsics.areEqual(property, DynamicAnimation.TRANSLATION_Z) ? "translationZ" : Intrinsics.areEqual(property, DynamicAnimation.SCALE_X) ? "scaleX" : Intrinsics.areEqual(property, DynamicAnimation.SCALE_Y) ? "scaleY" : Intrinsics.areEqual(property, DynamicAnimation.ROTATION) ? "rotation" : Intrinsics.areEqual(property, DynamicAnimation.ROTATION_X) ? "rotationX" : Intrinsics.areEqual(property, DynamicAnimation.ROTATION_Y) ? "rotationY" : Intrinsics.areEqual(property, DynamicAnimation.SCROLL_X) ? "scrollX" : Intrinsics.areEqual(property, DynamicAnimation.SCROLL_Y) ? "scrollY" : Intrinsics.areEqual(property, DynamicAnimation.ALPHA) ? "alpha" : "Custom FloatPropertyCompat instance";
        }
    }

    public final boolean arePropertiesAnimating(@NotNull Set<? extends FloatPropertyCompat<? super T>> properties) {
        Intrinsics.checkNotNullParameter(properties, "properties");
        if (!(properties instanceof Collection) || !properties.isEmpty()) {
            for (FloatPropertyCompat<? super T> floatPropertyCompat : properties) {
                if (isPropertyAnimating(floatPropertyCompat)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
