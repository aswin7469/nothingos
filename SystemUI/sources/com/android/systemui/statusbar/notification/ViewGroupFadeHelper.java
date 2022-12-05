package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ViewGroupFadeHelper.kt */
/* loaded from: classes.dex */
public final class ViewGroupFadeHelper {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final Function1<View, Boolean> visibilityIncluder = ViewGroupFadeHelper$Companion$visibilityIncluder$1.INSTANCE;

    public static final void fadeOutAllChildrenExcept(@NotNull ViewGroup viewGroup, @NotNull View view, long j, @Nullable Runnable runnable) {
        Companion.fadeOutAllChildrenExcept(viewGroup, view, j, runnable);
    }

    public static final void reset(@NotNull ViewGroup viewGroup) {
        Companion.reset(viewGroup);
    }

    /* compiled from: ViewGroupFadeHelper.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final void fadeOutAllChildrenExcept(@NotNull final ViewGroup root, @NotNull View excludedView, long j, @Nullable final Runnable runnable) {
            Intrinsics.checkNotNullParameter(root, "root");
            Intrinsics.checkNotNullParameter(excludedView, "excludedView");
            final Set<View> gatherViews = gatherViews(root, excludedView, ViewGroupFadeHelper.visibilityIncluder);
            for (View view : gatherViews) {
                if (view.getHasOverlappingRendering() && view.getLayerType() == 0) {
                    view.setLayerType(2, null);
                    view.setTag(R$id.view_group_fade_helper_hardware_layer, Boolean.TRUE);
                }
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat.setDuration(j);
            ofFloat.setInterpolator(Interpolators.ALPHA_OUT);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Float f = (Float) root.getTag(R$id.view_group_fade_helper_previous_value_tag);
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    float floatValue = ((Float) animatedValue).floatValue();
                    for (View view2 : gatherViews) {
                        if (!Intrinsics.areEqual(view2.getAlpha(), f)) {
                            view2.setTag(R$id.view_group_fade_helper_restore_tag, Float.valueOf(view2.getAlpha()));
                        }
                        view2.setAlpha(floatValue);
                    }
                    root.setTag(R$id.view_group_fade_helper_previous_value_tag, Float.valueOf(floatValue));
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.notification.ViewGroupFadeHelper$Companion$fadeOutAllChildrenExcept$animator$1$2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(@Nullable Animator animator) {
                    Runnable runnable2 = runnable;
                    if (runnable2 == null) {
                        return;
                    }
                    runnable2.run();
                }
            });
            ofFloat.start();
            root.setTag(R$id.view_group_fade_helper_modified_views, gatherViews);
            root.setTag(R$id.view_group_fade_helper_animator, ofFloat);
        }

        private final Set<View> gatherViews(ViewGroup viewGroup, View view, Function1<? super View, Boolean> function1) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            ViewParent parent = view.getParent();
            ViewGroup viewGroup2 = view;
            while (true) {
                ViewGroup viewGroup3 = (ViewGroup) parent;
                View view2 = viewGroup2;
                ViewGroup viewGroup4 = viewGroup3;
                if (viewGroup4 == null) {
                    break;
                }
                int i = 0;
                int childCount = viewGroup4.getChildCount();
                if (childCount > 0) {
                    while (true) {
                        int i2 = i + 1;
                        View child = viewGroup4.getChildAt(i);
                        Intrinsics.checkNotNullExpressionValue(child, "child");
                        if (function1.mo1949invoke(child).booleanValue() && !Intrinsics.areEqual(view2, child)) {
                            linkedHashSet.add(child);
                        }
                        if (i2 >= childCount) {
                            break;
                        }
                        i = i2;
                    }
                }
                if (Intrinsics.areEqual(viewGroup4, viewGroup)) {
                    break;
                }
                parent = viewGroup4.getParent();
                viewGroup2 = viewGroup4;
            }
            return linkedHashSet;
        }

        public final void reset(@NotNull ViewGroup root) {
            Intrinsics.checkNotNullParameter(root, "root");
            Set<View> asMutableSet = TypeIntrinsics.asMutableSet(root.getTag(R$id.view_group_fade_helper_modified_views));
            Animator animator = (Animator) root.getTag(R$id.view_group_fade_helper_animator);
            if (asMutableSet == null || animator == null) {
                return;
            }
            animator.cancel();
            Float f = (Float) root.getTag(R$id.view_group_fade_helper_previous_value_tag);
            for (View view : asMutableSet) {
                int i = R$id.view_group_fade_helper_restore_tag;
                Float f2 = (Float) view.getTag(i);
                if (f2 != null) {
                    if (Intrinsics.areEqual(f, view.getAlpha())) {
                        view.setAlpha(f2.floatValue());
                    }
                    int i2 = R$id.view_group_fade_helper_hardware_layer;
                    if (Intrinsics.areEqual((Boolean) view.getTag(i2), Boolean.TRUE)) {
                        view.setLayerType(0, null);
                        view.setTag(i2, null);
                    }
                    view.setTag(i, null);
                }
            }
            root.setTag(R$id.view_group_fade_helper_modified_views, null);
            root.setTag(R$id.view_group_fade_helper_previous_value_tag, null);
            root.setTag(R$id.view_group_fade_helper_animator, null);
        }
    }
}
