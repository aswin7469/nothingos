package com.android.p019wm.shell.animation;

import android.app.StatsManager;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u0014\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001:\u00018B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\"\u0010\u0015\u001a\u00020\u0016\"\b\b\u0000\u0010\u0017*\u00020\u00012\u000e\u0010\u0018\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\u00170\u0019H\u0007JI\u0010\u0015\u001a\u00020\u0016\"\b\b\u0000\u0010\u0017*\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00170\n2\"\u0010\u0018\u001a\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0000\u0012\u0002H\u00170\u00190\u001b\"\n\u0012\u0006\b\u0000\u0012\u0002H\u00170\u0019H\u0007¢\u0006\u0002\u0010\u001cJ4\u0010\u001d\u001a\u00020\u0016\"\b\b\u0000\u0010\u0017*\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00170\n2\u0012\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u0002H\u0017\u0012\u0004\u0012\u00020\u000e0\u001fH\u0007J\u001e\u0010 \u001a\u00020\u0016\"\b\b\u0000\u0010\u0017*\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00170\nJ\"\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00170\u000b\"\u0004\b\u0000\u0010\u00172\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00170\nH\u0002JL\u0010\"\u001a0\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u0002H\u00170\u0019\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020%0$j\b\u0012\u0004\u0012\u00020%`&0#j\b\u0012\u0004\u0012\u0002H\u0017`'\"\b\b\u0000\u0010\u0017*\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00170\nJ\b\u0010(\u001a\u00020\u0016H\u0007J\u0010\u0010)\u001a\u00020\u00162\u0006\u0010*\u001a\u00020\u000eH\u0007J\u0010\u0010+\u001a\u00020\u00162\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\b\u0010,\u001a\u00020\u0016H\u0007J\u0010-\u001a\u00020\u0016\"\b\b\u0000\u0010\u0017*\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00170\n2\u000e\u0010.\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\u00170\u00192\u0016\u0010/\u001a\u0012\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020\u000e0\u001fj\u0002`022\u00101\u001a\u001a\u0012\u0016\b\u0001\u0012\u0012\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020\u000e0\u001fj\u0002`00\u001b\"\u0012\u0012\u0004\u0012\u00020%\u0012\u0004\u0012\u00020\u000e0\u001fj\u0002`0¢\u0006\u0002\u00102JJ\u0010-\u001a\u00020\u0016\"\b\b\u0000\u0010\u0017*\u00020\u00012\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00170\n2\u000e\u0010.\u001a\n\u0012\u0006\b\u0000\u0012\u0002H\u00170\u00192\u0006\u00103\u001a\u0002042\u0006\u00105\u001a\u0002042\n\u00106\u001a\u000207\"\u000204R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00010\u0004j\b\u0012\u0004\u0012\u00020\u0001`\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R:\u0010\b\u001a.\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b0\tj\u0016\u0012\b\u0012\u0006\u0012\u0002\b\u00030\n\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b`\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014¨\u00069"}, mo65043d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimatorTestUtils;", "", "()V", "allAnimatedObjects", "Ljava/util/HashSet;", "Lkotlin/collections/HashSet;", "animationThreadHandler", "Landroid/os/Handler;", "animatorTestHelpers", "Ljava/util/HashMap;", "Lcom/android/wm/shell/animation/PhysicsAnimator;", "Lcom/android/wm/shell/animation/PhysicsAnimatorTestUtils$AnimatorTestHelper;", "Lkotlin/collections/HashMap;", "startBlocksUntilAnimationsEnd", "", "timeoutMs", "", "getTimeoutMs", "()J", "setTimeoutMs", "(J)V", "blockUntilAnimationsEnd", "", "T", "properties", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "animator", "", "(Lcom/android/wm/shell/animation/PhysicsAnimator;[Landroidx/dynamicanimation/animation/FloatPropertyCompat;)V", "blockUntilFirstAnimationFrameWhereTrue", "predicate", "Lkotlin/Function1;", "clearAnimationUpdateFrames", "getAnimationTestHelper", "getAnimationUpdateFrames", "Landroid/util/ArrayMap;", "Ljava/util/ArrayList;", "Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "Lkotlin/collections/ArrayList;", "Lcom/android/wm/shell/animation/UpdateFramesPerProperty;", "prepareForTest", "setAllAnimationsBlock", "block", "setBlockTimeout", "tearDown", "verifyAnimationUpdateFrames", "property", "firstUpdateMatcher", "Lcom/android/wm/shell/animation/UpdateMatcher;", "additionalUpdateMatchers", "(Lcom/android/wm/shell/animation/PhysicsAnimator;Landroidx/dynamicanimation/animation/FloatPropertyCompat;Lkotlin/jvm/functions/Function1;[Lkotlin/jvm/functions/Function1;)V", "startValue", "", "firstTargetValue", "additionalTargetValues", "", "AnimatorTestHelper", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils */
/* compiled from: PhysicsAnimatorTestUtils.kt */
public final class PhysicsAnimatorTestUtils {
    public static final PhysicsAnimatorTestUtils INSTANCE = new PhysicsAnimatorTestUtils();
    /* access modifiers changed from: private */
    public static final HashSet<Object> allAnimatedObjects = new HashSet<>();
    /* access modifiers changed from: private */
    public static final Handler animationThreadHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public static final HashMap<PhysicsAnimator<?>, AnimatorTestHelper<?>> animatorTestHelpers = new HashMap<>();
    /* access modifiers changed from: private */
    public static boolean startBlocksUntilAnimationsEnd;
    private static long timeoutMs = StatsManager.DEFAULT_TIMEOUT_MILLIS;

    private PhysicsAnimatorTestUtils() {
    }

    public final long getTimeoutMs() {
        return timeoutMs;
    }

    public final void setTimeoutMs(long j) {
        timeoutMs = j;
    }

    @JvmStatic
    public static final void prepareForTest() {
        PhysicsAnimator.Companion.setInstanceConstructor$WMShell_release(new PhysicsAnimatorTestUtils$prepareForTest$1(PhysicsAnimator.Companion.getInstanceConstructor$WMShell_release()));
        timeoutMs = StatsManager.DEFAULT_TIMEOUT_MILLIS;
        startBlocksUntilAnimationsEnd = false;
        allAnimatedObjects.clear();
    }

    @JvmStatic
    public static final void tearDown() {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        animationThreadHandler.post(new PhysicsAnimatorTestUtils$$ExternalSyntheticLambda0(countDownLatch));
        countDownLatch.await();
        animatorTestHelpers.clear();
        PhysicsAnimatorKt.getAnimators().clear();
        allAnimatedObjects.clear();
    }

    /* access modifiers changed from: private */
    /* renamed from: tearDown$lambda-1  reason: not valid java name */
    public static final void m3400tearDown$lambda1(CountDownLatch countDownLatch) {
        Intrinsics.checkNotNullParameter(countDownLatch, "$latch");
        Set<PhysicsAnimator<?>> keySet = animatorTestHelpers.keySet();
        Intrinsics.checkNotNullExpressionValue(keySet, "animatorTestHelpers.keys");
        for (PhysicsAnimator cancel : keySet) {
            cancel.cancel();
        }
        countDownLatch.countDown();
    }

    @JvmStatic
    public static final void setBlockTimeout(long j) {
        timeoutMs = j;
    }

    @JvmStatic
    public static final void setAllAnimationsBlock(boolean z) {
        startBlocksUntilAnimationsEnd = z;
    }

    @JvmStatic
    public static final <T> void blockUntilAnimationsEnd(PhysicsAnimator<T> physicsAnimator, FloatPropertyCompat<? super T>... floatPropertyCompatArr) throws InterruptedException {
        Intrinsics.checkNotNullParameter(physicsAnimator, "animator");
        Intrinsics.checkNotNullParameter(floatPropertyCompatArr, "properties");
        HashSet hashSet = new HashSet();
        for (FloatPropertyCompat<? super T> floatPropertyCompat : floatPropertyCompatArr) {
            if (physicsAnimator.isPropertyAnimating(floatPropertyCompat)) {
                hashSet.add(floatPropertyCompat);
            }
        }
        if (hashSet.size() > 0) {
            CountDownLatch countDownLatch = new CountDownLatch(hashSet.size());
            INSTANCE.getAnimationTestHelper(physicsAnimator).addTestEndListener$WMShell_release(new PhysicsAnimatorTestUtils$blockUntilAnimationsEnd$1(hashSet, countDownLatch));
            countDownLatch.await(timeoutMs, TimeUnit.MILLISECONDS);
        }
    }

    @JvmStatic
    public static final <T> void blockUntilAnimationsEnd(FloatPropertyCompat<? super T> floatPropertyCompat) throws InterruptedException {
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "properties");
        Iterator<Object> it = allAnimatedObjects.iterator();
        while (it.hasNext()) {
            try {
                blockUntilAnimationsEnd(PhysicsAnimator.Companion.getInstance(it.next()), floatPropertyCompat);
            } catch (ClassCastException unused) {
            }
        }
    }

    @JvmStatic
    public static final <T> void blockUntilFirstAnimationFrameWhereTrue(PhysicsAnimator<T> physicsAnimator, Function1<? super T, Boolean> function1) throws InterruptedException {
        Intrinsics.checkNotNullParameter(physicsAnimator, "animator");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        if (physicsAnimator.isRunning()) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            INSTANCE.getAnimationTestHelper(physicsAnimator).addTestUpdateListener$WMShell_release(new C3367x4cc8474d(function1, countDownLatch));
            countDownLatch.await(timeoutMs, TimeUnit.MILLISECONDS);
        }
    }

    public final <T> void verifyAnimationUpdateFrames(PhysicsAnimator<T> physicsAnimator, FloatPropertyCompat<? super T> floatPropertyCompat, Function1<? super PhysicsAnimator.AnimationUpdate, Boolean> function1, Function1<? super PhysicsAnimator.AnimationUpdate, Boolean>... function1Arr) {
        Intrinsics.checkNotNullParameter(physicsAnimator, "animator");
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        Intrinsics.checkNotNullParameter(function1, "firstUpdateMatcher");
        Intrinsics.checkNotNullParameter(function1Arr, "additionalUpdateMatchers");
        ArrayMap<FloatPropertyCompat<? super T>, ArrayList<PhysicsAnimator.AnimationUpdate>> animationUpdateFrames = getAnimationUpdateFrames(physicsAnimator);
        if (animationUpdateFrames.containsKey(floatPropertyCompat)) {
            ArrayList<PhysicsAnimator.AnimationUpdate> arrayList = animationUpdateFrames.get(floatPropertyCompat);
            Intrinsics.checkNotNull(arrayList);
            ArrayList arrayList2 = new ArrayList(arrayList);
            ArrayDeque arrayDeque = new ArrayDeque(ArraysKt.toList((T[]) function1Arr));
            StringBuilder sb = new StringBuilder();
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                PhysicsAnimator.AnimationUpdate animationUpdate = (PhysicsAnimator.AnimationUpdate) it.next();
                Intrinsics.checkNotNullExpressionValue(animationUpdate, "update");
                if (!function1.invoke(animationUpdate).booleanValue()) {
                    sb.append(new StringBuilder().append((Object) animationUpdate).append(10).toString());
                } else if (arrayDeque.size() == 0) {
                    getAnimationUpdateFrames(physicsAnimator).remove(floatPropertyCompat);
                    return;
                } else {
                    sb.append(animationUpdate + "\t(satisfied matcher)\n");
                    Object pop = arrayDeque.pop();
                    Intrinsics.checkNotNullExpressionValue(pop, "matchers.pop()");
                    function1 = (Function1) pop;
                }
            }
            String readablePropertyName = PhysicsAnimator.Companion.getReadablePropertyName(floatPropertyCompat);
            getAnimationUpdateFrames(physicsAnimator).remove(floatPropertyCompat);
            throw new RuntimeException("Failed to verify animation frames for property " + readablePropertyName + ": Provided " + (function1Arr.length + 1) + " matchers, however " + (arrayDeque.size() + 1) + " remained unsatisfied.\n\nAll frames:\n" + sb);
        }
        throw new IllegalStateException("No frames for given target object and property.".toString());
    }

    public final <T> void verifyAnimationUpdateFrames(PhysicsAnimator<T> physicsAnimator, FloatPropertyCompat<? super T> floatPropertyCompat, float f, float f2, float... fArr) {
        Intrinsics.checkNotNullParameter(physicsAnimator, "animator");
        Intrinsics.checkNotNullParameter(floatPropertyCompat, "property");
        Intrinsics.checkNotNullParameter(fArr, "additionalTargetValues");
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(Float.valueOf(f2));
        arrayList2.addAll(ArraysKt.toList(fArr));
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            Float f3 = (Float) it.next();
            Intrinsics.checkNotNullExpressionValue(f3, "value");
            if (f3.floatValue() > f) {
                arrayList.add(new PhysicsAnimatorTestUtils$verifyAnimationUpdateFrames$1(f3));
            } else {
                arrayList.add(new PhysicsAnimatorTestUtils$verifyAnimationUpdateFrames$2(f3));
            }
            f = f3.floatValue();
        }
        Object obj = arrayList.get(0);
        Intrinsics.checkNotNullExpressionValue(obj, "matchers[0]");
        Object[] array = CollectionsKt.drop(arrayList, 0).toArray((T[]) new Function1[0]);
        Intrinsics.checkNotNull(array, "null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        Function1[] function1Arr = (Function1[]) array;
        verifyAnimationUpdateFrames(physicsAnimator, floatPropertyCompat, (Function1) obj, (Function1[]) Arrays.copyOf((T[]) function1Arr, function1Arr.length));
    }

    public final <T> ArrayMap<FloatPropertyCompat<? super T>, ArrayList<PhysicsAnimator.AnimationUpdate>> getAnimationUpdateFrames(PhysicsAnimator<T> physicsAnimator) {
        Intrinsics.checkNotNullParameter(physicsAnimator, "animator");
        AnimatorTestHelper animatorTestHelper = animatorTestHelpers.get(physicsAnimator);
        ArrayMap<FloatPropertyCompat<? super T>, ArrayList<PhysicsAnimator.AnimationUpdate>> updates$WMShell_release = animatorTestHelper != null ? animatorTestHelper.getUpdates$WMShell_release() : null;
        if (updates$WMShell_release != null) {
            return updates$WMShell_release;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.util.ArrayMap<androidx.dynamicanimation.animation.FloatPropertyCompat<in T of com.android.wm.shell.animation.PhysicsAnimatorTestUtils.getAnimationUpdateFrames>, java.util.ArrayList<com.android.wm.shell.animation.PhysicsAnimator.AnimationUpdate>{ kotlin.collections.TypeAliasesKt.ArrayList<com.android.wm.shell.animation.PhysicsAnimator.AnimationUpdate> }>{ com.android.wm.shell.animation.PhysicsAnimatorTestUtilsKt.UpdateFramesPerProperty<T of com.android.wm.shell.animation.PhysicsAnimatorTestUtils.getAnimationUpdateFrames> }");
    }

    public final <T> void clearAnimationUpdateFrames(PhysicsAnimator<T> physicsAnimator) {
        Intrinsics.checkNotNullParameter(physicsAnimator, "animator");
        AnimatorTestHelper animatorTestHelper = animatorTestHelpers.get(physicsAnimator);
        if (animatorTestHelper != null) {
            animatorTestHelper.clearUpdates$WMShell_release();
        }
    }

    private final <T> AnimatorTestHelper<T> getAnimationTestHelper(PhysicsAnimator<T> physicsAnimator) {
        AnimatorTestHelper<?> animatorTestHelper = animatorTestHelpers.get(physicsAnimator);
        if (animatorTestHelper != null) {
            return animatorTestHelper;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.wm.shell.animation.PhysicsAnimatorTestUtils.AnimatorTestHelper<T of com.android.wm.shell.animation.PhysicsAnimatorTestUtils.getAnimationTestHelper>");
    }

    @Metadata(mo65042d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\"\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004¢\u0006\u0002\u0010\u0005J\u001b\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u000fH\u0000¢\u0006\u0002\b\u0015J\u001b\u0010\u0016\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00028\u00000\u0011H\u0000¢\u0006\u0002\b\u0017J\u001e\u0010\u0018\u001a\u00020\u00132\u0014\u0010\u0019\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b0\u001aH\u0002J\r\u0010\u001b\u001a\u00020\u0013H\u0000¢\u0006\u0002\b\u001cJ;\u0010\u001d\u001a0\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\n0\tj\b\u0012\u0004\u0012\u00020\n`\u000b0\u0007j\b\u0012\u0004\u0012\u00028\u0000`\u001eH\u0000¢\u0006\u0002\b\u001fJ\b\u0010 \u001a\u00020\u0013H\u0002R2\u0010\u0006\u001a&\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\b\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\n0\tj\b\u0012\u0004\u0012\u00020\n`\u000b0\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R*\u0010\u000e\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000f0\tj\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u000f`\u000bX\u0004¢\u0006\u0002\n\u0000R*\u0010\u0010\u001a\u001e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u00110\tj\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00028\u00000\u0011`\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006!"}, mo65043d2 = {"Lcom/android/wm/shell/animation/PhysicsAnimatorTestUtils$AnimatorTestHelper;", "T", "", "animator", "Lcom/android/wm/shell/animation/PhysicsAnimator;", "(Lcom/android/wm/shell/animation/PhysicsAnimator;)V", "allUpdates", "Landroid/util/ArrayMap;", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "Ljava/util/ArrayList;", "Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "Lkotlin/collections/ArrayList;", "currentlyRunningStartInternal", "", "testEndListeners", "Lcom/android/wm/shell/animation/PhysicsAnimator$EndListener;", "testUpdateListeners", "Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;", "addTestEndListener", "", "listener", "addTestEndListener$WMShell_release", "addTestUpdateListener", "addTestUpdateListener$WMShell_release", "cancelForTest", "properties", "", "clearUpdates", "clearUpdates$WMShell_release", "getUpdates", "Lcom/android/wm/shell/animation/UpdateFramesPerProperty;", "getUpdates$WMShell_release", "startForTest", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$AnimatorTestHelper */
    /* compiled from: PhysicsAnimatorTestUtils.kt */
    public static final class AnimatorTestHelper<T> {
        /* access modifiers changed from: private */
        public final ArrayMap<FloatPropertyCompat<? super T>, ArrayList<PhysicsAnimator.AnimationUpdate>> allUpdates = new ArrayMap<>();
        private final PhysicsAnimator<T> animator;
        private boolean currentlyRunningStartInternal;
        /* access modifiers changed from: private */
        public final ArrayList<PhysicsAnimator.EndListener<T>> testEndListeners = new ArrayList<>();
        /* access modifiers changed from: private */
        public final ArrayList<PhysicsAnimator.UpdateListener<T>> testUpdateListeners = new ArrayList<>();

        public AnimatorTestHelper(PhysicsAnimator<T> physicsAnimator) {
            Intrinsics.checkNotNullParameter(physicsAnimator, "animator");
            this.animator = physicsAnimator;
            physicsAnimator.setStartAction$WMShell_release(new Function0<Unit>(this) {
                public final void invoke() {
                    ((AnimatorTestHelper) this.receiver).startForTest();
                }
            });
            physicsAnimator.setCancelAction$WMShell_release(new Function1<Set<? extends FloatPropertyCompat<? super T>>, Unit>(this) {
                public /* bridge */ /* synthetic */ Object invoke(Object obj) {
                    invoke((Set) obj);
                    return Unit.INSTANCE;
                }

                public final void invoke(Set<? extends FloatPropertyCompat<? super T>> set) {
                    Intrinsics.checkNotNullParameter(set, "p0");
                    ((AnimatorTestHelper) this.receiver).cancelForTest(set);
                }
            });
        }

        public final void addTestEndListener$WMShell_release(PhysicsAnimator.EndListener<T> endListener) {
            Intrinsics.checkNotNullParameter(endListener, "listener");
            this.testEndListeners.add(endListener);
        }

        public final void addTestUpdateListener$WMShell_release(PhysicsAnimator.UpdateListener<T> updateListener) {
            Intrinsics.checkNotNullParameter(updateListener, "listener");
            this.testUpdateListeners.add(updateListener);
        }

        public final ArrayMap<FloatPropertyCompat<? super T>, ArrayList<PhysicsAnimator.AnimationUpdate>> getUpdates$WMShell_release() {
            return this.allUpdates;
        }

        public final void clearUpdates$WMShell_release() {
            this.allUpdates.clear();
        }

        /* access modifiers changed from: private */
        public final void startForTest() {
            CountDownLatch countDownLatch = new CountDownLatch(PhysicsAnimatorTestUtils.startBlocksUntilAnimationsEnd ? 2 : 1);
            PhysicsAnimatorTestUtils.animationThreadHandler.post(new C3365x10869(this, countDownLatch));
            countDownLatch.await(PhysicsAnimatorTestUtils.INSTANCE.getTimeoutMs(), TimeUnit.MILLISECONDS);
        }

        /* access modifiers changed from: private */
        /* renamed from: startForTest$lambda-0  reason: not valid java name */
        public static final void m3403startForTest$lambda0(AnimatorTestHelper animatorTestHelper, CountDownLatch countDownLatch) {
            Intrinsics.checkNotNullParameter(animatorTestHelper, "this$0");
            Intrinsics.checkNotNullParameter(countDownLatch, "$unblockLatch");
            animatorTestHelper.animator.addUpdateListener(new PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$1(animatorTestHelper));
            animatorTestHelper.animator.addEndListener(new PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$2(animatorTestHelper, countDownLatch));
            animatorTestHelper.currentlyRunningStartInternal = true;
            animatorTestHelper.animator.startInternal$WMShell_release();
            animatorTestHelper.currentlyRunningStartInternal = false;
            countDownLatch.countDown();
        }

        /* access modifiers changed from: private */
        public final void cancelForTest(Set<? extends FloatPropertyCompat<? super T>> set) {
            if (this.currentlyRunningStartInternal) {
                this.animator.cancelInternal$WMShell_release(set);
                return;
            }
            CountDownLatch countDownLatch = new CountDownLatch(1);
            PhysicsAnimatorTestUtils.animationThreadHandler.post(new C3366x1086a(this, set, countDownLatch));
            countDownLatch.await(PhysicsAnimatorTestUtils.INSTANCE.getTimeoutMs(), TimeUnit.MILLISECONDS);
        }

        /* access modifiers changed from: private */
        /* renamed from: cancelForTest$lambda-1  reason: not valid java name */
        public static final void m3402cancelForTest$lambda1(AnimatorTestHelper animatorTestHelper, Set set, CountDownLatch countDownLatch) {
            Intrinsics.checkNotNullParameter(animatorTestHelper, "this$0");
            Intrinsics.checkNotNullParameter(set, "$properties");
            Intrinsics.checkNotNullParameter(countDownLatch, "$unblockLatch");
            animatorTestHelper.animator.cancelInternal$WMShell_release(set);
            countDownLatch.countDown();
        }
    }
}
