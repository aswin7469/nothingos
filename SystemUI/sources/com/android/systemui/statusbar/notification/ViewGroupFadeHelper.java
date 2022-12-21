package com.android.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

@Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/ViewGroupFadeHelper;", "", "()V", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ViewGroupFadeHelper.kt */
public final class ViewGroupFadeHelper {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final Function1<View, Boolean> visibilityIncluder = ViewGroupFadeHelper$Companion$visibilityIncluder$1.INSTANCE;

    @JvmStatic
    public static final void fadeOutAllChildrenExcept(ViewGroup viewGroup, View view, long j, Runnable runnable) {
        Companion.fadeOutAllChildrenExcept(viewGroup, view, j, runnable);
    }

    @JvmStatic
    public static final void reset(ViewGroup viewGroup) {
        Companion.reset(viewGroup);
    }

    @Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010#\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J*\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00052\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0007J2\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00050\u00112\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00052\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004H\u0002J\u0010\u0010\u0013\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0007R\u001a\u0010\u0003\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/ViewGroupFadeHelper$Companion;", "", "()V", "visibilityIncluder", "Lkotlin/Function1;", "Landroid/view/View;", "", "fadeOutAllChildrenExcept", "", "root", "Landroid/view/ViewGroup;", "excludedView", "duration", "", "endRunnable", "Ljava/lang/Runnable;", "gatherViews", "", "shouldInclude", "reset", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ViewGroupFadeHelper.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final void fadeOutAllChildrenExcept(ViewGroup viewGroup, View view, long j, Runnable runnable) {
            Intrinsics.checkNotNullParameter(viewGroup, "root");
            Intrinsics.checkNotNullParameter(view, "excludedView");
            Set<View> gatherViews = gatherViews(viewGroup, view, ViewGroupFadeHelper.visibilityIncluder);
            for (View next : gatherViews) {
                if (next.getHasOverlappingRendering() && next.getLayerType() == 0) {
                    next.setLayerType(2, (Paint) null);
                    next.setTag(C1893R.C1897id.view_group_fade_helper_hardware_layer, true);
                }
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            ofFloat.setDuration(j);
            ofFloat.setInterpolator(Interpolators.ALPHA_OUT);
            ofFloat.addUpdateListener(new ViewGroupFadeHelper$Companion$$ExternalSyntheticLambda0(viewGroup, gatherViews));
            ofFloat.addListener(new C2664xbb47cb27(runnable));
            ofFloat.start();
            viewGroup.setTag(C1893R.C1897id.view_group_fade_helper_modified_views, gatherViews);
            viewGroup.setTag(C1893R.C1897id.view_group_fade_helper_animator, ofFloat);
        }

        /* access modifiers changed from: private */
        /* renamed from: fadeOutAllChildrenExcept$lambda-1$lambda-0  reason: not valid java name */
        public static final void m3094fadeOutAllChildrenExcept$lambda1$lambda0(ViewGroup viewGroup, Set set, ValueAnimator valueAnimator) {
            Intrinsics.checkNotNullParameter(viewGroup, "$root");
            Intrinsics.checkNotNullParameter(set, "$viewsToFadeOut");
            Float f = (Float) viewGroup.getTag(C1893R.C1897id.view_group_fade_helper_previous_value_tag);
            Object animatedValue = valueAnimator.getAnimatedValue();
            if (animatedValue != null) {
                float floatValue = ((Float) animatedValue).floatValue();
                Iterator it = set.iterator();
                while (it.hasNext()) {
                    View view = (View) it.next();
                    if (!Intrinsics.areEqual(view.getAlpha(), f)) {
                        view.setTag(C1893R.C1897id.view_group_fade_helper_restore_tag, Float.valueOf(view.getAlpha()));
                    }
                    view.setAlpha(floatValue);
                }
                viewGroup.setTag(C1893R.C1897id.view_group_fade_helper_previous_value_tag, Float.valueOf(floatValue));
                return;
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
        }

        private final Set<View> gatherViews(ViewGroup viewGroup, View view, Function1<? super View, Boolean> function1) {
            Set<View> linkedHashSet = new LinkedHashSet<>();
            for (ViewGroup viewGroup2 = (ViewGroup) view.getParent(); viewGroup2 != null; viewGroup2 = (ViewGroup) viewGroup2.getParent()) {
                int childCount = viewGroup2.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = viewGroup2.getChildAt(i);
                    Intrinsics.checkNotNullExpressionValue(childAt, "child");
                    if (function1.invoke(childAt).booleanValue() && !Intrinsics.areEqual((Object) view, (Object) childAt)) {
                        linkedHashSet.add(childAt);
                    }
                }
                if (Intrinsics.areEqual((Object) viewGroup2, (Object) viewGroup)) {
                    break;
                }
                view = viewGroup2;
            }
            return linkedHashSet;
        }

        @JvmStatic
        public final void reset(ViewGroup viewGroup) {
            Intrinsics.checkNotNullParameter(viewGroup, "root");
            Set<View> asMutableSet = TypeIntrinsics.asMutableSet(viewGroup.getTag(C1893R.C1897id.view_group_fade_helper_modified_views));
            Animator animator = (Animator) viewGroup.getTag(C1893R.C1897id.view_group_fade_helper_animator);
            if (asMutableSet != null && animator != null) {
                animator.cancel();
                Float f = (Float) viewGroup.getTag(C1893R.C1897id.view_group_fade_helper_previous_value_tag);
                for (View view : asMutableSet) {
                    Float f2 = (Float) view.getTag(C1893R.C1897id.view_group_fade_helper_restore_tag);
                    if (f2 != null) {
                        if (Intrinsics.areEqual(f, view.getAlpha())) {
                            view.setAlpha(f2.floatValue());
                        }
                        if (Intrinsics.areEqual((Object) (Boolean) view.getTag(C1893R.C1897id.view_group_fade_helper_hardware_layer), (Object) true)) {
                            view.setLayerType(0, (Paint) null);
                            view.setTag(C1893R.C1897id.view_group_fade_helper_hardware_layer, (Object) null);
                        }
                        view.setTag(C1893R.C1897id.view_group_fade_helper_restore_tag, (Object) null);
                    }
                }
                viewGroup.setTag(C1893R.C1897id.view_group_fade_helper_modified_views, (Object) null);
                viewGroup.setTag(C1893R.C1897id.view_group_fade_helper_previous_value_tag, (Object) null);
                viewGroup.setTag(C1893R.C1897id.view_group_fade_helper_animator, (Object) null);
            }
        }
    }
}
