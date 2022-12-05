package com.android.wm.shell.animation;

import android.view.View;
import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.WeakHashMap;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes2.dex */
public final class PhysicsAnimatorKt {
    private static boolean verboseLogging;
    @NotNull
    private static final WeakHashMap<Object, PhysicsAnimator<?>> animators = new WeakHashMap<>();
    @NotNull
    private static final PhysicsAnimator.SpringConfig globalDefaultSpring = new PhysicsAnimator.SpringConfig(1500.0f, 0.5f);
    private static final float UNSET = -3.4028235E38f;
    @NotNull
    private static final PhysicsAnimator.FlingConfig globalDefaultFling = new PhysicsAnimator.FlingConfig(1.0f, UNSET, Float.MAX_VALUE);

    public static final /* synthetic */ PhysicsAnimator.FlingConfig access$getGlobalDefaultFling$p() {
        return globalDefaultFling;
    }

    public static final /* synthetic */ PhysicsAnimator.SpringConfig access$getGlobalDefaultSpring$p() {
        return globalDefaultSpring;
    }

    public static final /* synthetic */ float access$getUNSET$p() {
        return UNSET;
    }

    public static final /* synthetic */ boolean access$getVerboseLogging$p() {
        return verboseLogging;
    }

    @NotNull
    public static final <T extends View> PhysicsAnimator<T> getPhysicsAnimator(@NotNull T t) {
        Intrinsics.checkNotNullParameter(t, "<this>");
        return PhysicsAnimator.Companion.getInstance(t);
    }

    @NotNull
    public static final WeakHashMap<Object, PhysicsAnimator<?>> getAnimators() {
        return animators;
    }
}
