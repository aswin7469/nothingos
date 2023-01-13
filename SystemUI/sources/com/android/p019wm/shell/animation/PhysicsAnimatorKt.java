package com.android.p019wm.shell.animation;

import android.view.View;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import java.util.WeakHashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000T\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XD¢\u0006\u0002\n\u0000\"$\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u0007\u0012\b\u0012\u0006\u0012\u0002\b\u00030\b0\u0006X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\"\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000\"%\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00120\b\"\b\b\u0000\u0010\u0012*\u00020\u0013*\u0002H\u00128F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015*\u0016\u0010\u0016\"\b\u0012\u0004\u0012\u00020\u00180\u00172\b\u0012\u0004\u0012\u00020\u00180\u0017*8\u0010\u0019\u001a\u0004\b\u0000\u0010\u0012\"\u0016\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u0002H\u00120\u001b\u0012\u0004\u0012\u00020\u001c0\u001a2\u0016\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u0002H\u00120\u001b\u0012\u0004\u0012\u00020\u001c0\u001a¨\u0006\u001d"}, mo65043d2 = {"FLING_FRICTION_SCALAR_MULTIPLIER", "", "TAG", "", "UNSET", "animators", "Ljava/util/WeakHashMap;", "", "Lcom/android/wm/shell/animation/PhysicsAnimator;", "getAnimators", "()Ljava/util/WeakHashMap;", "globalDefaultFling", "Lcom/android/wm/shell/animation/PhysicsAnimator$FlingConfig;", "globalDefaultSpring", "Lcom/android/wm/shell/animation/PhysicsAnimator$SpringConfig;", "verboseLogging", "", "physicsAnimator", "T", "Landroid/view/View;", "getPhysicsAnimator", "(Landroid/view/View;)Lcom/android/wm/shell/animation/PhysicsAnimator;", "EndAction", "Lkotlin/Function0;", "", "UpdateMap", "Landroid/util/ArrayMap;", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "WMShell_release"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorKt */
/* compiled from: PhysicsAnimator.kt */
public final class PhysicsAnimatorKt {
    private static final float FLING_FRICTION_SCALAR_MULTIPLIER = 4.2f;
    private static final String TAG = "PhysicsAnimator";
    /* access modifiers changed from: private */
    public static final float UNSET = -3.4028235E38f;
    private static final WeakHashMap<Object, PhysicsAnimator<?>> animators = new WeakHashMap<>();
    /* access modifiers changed from: private */
    public static final PhysicsAnimator.FlingConfig globalDefaultFling = new PhysicsAnimator.FlingConfig(1.0f, -3.4028235E38f, Float.MAX_VALUE);
    /* access modifiers changed from: private */
    public static final PhysicsAnimator.SpringConfig globalDefaultSpring = new PhysicsAnimator.SpringConfig(1500.0f, 0.5f);
    /* access modifiers changed from: private */
    public static boolean verboseLogging;

    public static final <T extends View> PhysicsAnimator<T> getPhysicsAnimator(T t) {
        Intrinsics.checkNotNullParameter(t, "<this>");
        return PhysicsAnimator.Companion.getInstance(t);
    }

    public static final WeakHashMap<Object, PhysicsAnimator<?>> getAnimators() {
        return animators;
    }
}
