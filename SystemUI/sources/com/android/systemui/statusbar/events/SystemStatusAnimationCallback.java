package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.animation.ValueAnimator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SystemStatusAnimationScheduler.kt */
/* loaded from: classes.dex */
public interface SystemStatusAnimationCallback {
    @Nullable
    default Animator onHidePersistentDot() {
        return null;
    }

    default void onSystemChromeAnimationEnd() {
    }

    default void onSystemChromeAnimationStart() {
    }

    default void onSystemChromeAnimationUpdate(@NotNull ValueAnimator animator) {
        Intrinsics.checkNotNullParameter(animator, "animator");
    }

    @Nullable
    default Animator onSystemStatusAnimationTransitionToPersistentDot(@Nullable String str) {
        return null;
    }
}
