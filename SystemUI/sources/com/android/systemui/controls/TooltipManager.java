package com.android.systemui.controls;

import android.content.Context;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.android.systemui.C1893R;
import com.android.systemui.Prefs;
import com.android.systemui.recents.TriangleShape;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u0000 \"2\u00020\u0001:\u0001\"B)\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010\u001b\u001a\u00020\u00152\b\b\u0002\u0010\u001c\u001a\u00020\tJ\b\u0010\u001d\u001a\u00020\tH\u0002J\u001e\u0010\u001e\u001a\u00020\u00152\u0006\u0010\u001f\u001a\u00020\u00072\u0006\u0010 \u001a\u00020\u00072\u0006\u0010!\u001a\u00020\u0007R\u0016\u0010\u000b\u001a\n \r*\u0004\u0018\u00010\f0\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n \r*\u0004\u0018\u00010\f0\fX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u000f\u001a\u00020\u0010¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u001d\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00150\u0014¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0018\u001a\u00020\u0007X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0019\u001a\n \r*\u0004\u0018\u00010\u001a0\u001aX\u0004¢\u0006\u0002\n\u0000¨\u0006#"}, mo64987d2 = {"Lcom/android/systemui/controls/TooltipManager;", "", "context", "Landroid/content/Context;", "preferenceName", "", "maxTimesShown", "", "below", "", "(Landroid/content/Context;Ljava/lang/String;IZ)V", "arrowView", "Landroid/view/View;", "kotlin.jvm.PlatformType", "dismissView", "layout", "Landroid/view/ViewGroup;", "getLayout", "()Landroid/view/ViewGroup;", "preferenceStorer", "Lkotlin/Function1;", "", "getPreferenceStorer", "()Lkotlin/jvm/functions/Function1;", "shown", "textView", "Landroid/widget/TextView;", "hide", "animate", "shouldShow", "show", "stringRes", "x", "y", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: TooltipManager.kt */
public final class TooltipManager {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final long HIDE_DURATION_MS = 100;
    private static final long SHOW_DELAY_MS = 500;
    private static final long SHOW_DURATION_MS = 300;
    private final View arrowView;
    private final boolean below;
    private final View dismissView;
    private final ViewGroup layout;
    private final int maxTimesShown;
    /* access modifiers changed from: private */
    public final String preferenceName;
    private final Function1<Integer, Unit> preferenceStorer;
    private int shown;
    private final TextView textView;

    public TooltipManager(Context context, String str, int i, boolean z) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(str, "preferenceName");
        this.preferenceName = str;
        this.maxTimesShown = i;
        this.below = z;
        this.shown = Prefs.getInt(context, str, 0);
        View inflate = LayoutInflater.from(context).inflate(C1893R.layout.controls_onboarding, (ViewGroup) null);
        if (inflate != null) {
            ViewGroup viewGroup = (ViewGroup) inflate;
            this.layout = viewGroup;
            this.preferenceStorer = new TooltipManager$preferenceStorer$1(context, this);
            viewGroup.setAlpha(0.0f);
            this.textView = (TextView) viewGroup.requireViewById(C1893R.C1897id.onboarding_text);
            View requireViewById = viewGroup.requireViewById(C1893R.C1897id.dismiss);
            requireViewById.setOnClickListener(new TooltipManager$$ExternalSyntheticLambda2(this));
            this.dismissView = requireViewById;
            View requireViewById2 = viewGroup.requireViewById(C1893R.C1897id.arrow);
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16843829, typedValue, true);
            int color = context.getResources().getColor(typedValue.resourceId, context.getTheme());
            int dimensionPixelSize = context.getResources().getDimensionPixelSize(C1893R.dimen.recents_onboarding_toast_arrow_corner_radius);
            ViewGroup.LayoutParams layoutParams = requireViewById2.getLayoutParams();
            ShapeDrawable shapeDrawable = new ShapeDrawable(TriangleShape.create((float) layoutParams.width, (float) layoutParams.height, z));
            Paint paint = shapeDrawable.getPaint();
            paint.setColor(color);
            paint.setPathEffect(new CornerPathEffect((float) dimensionPixelSize));
            requireViewById2.setBackground(shapeDrawable);
            this.arrowView = requireViewById2;
            if (!z) {
                viewGroup.removeView(requireViewById2);
                viewGroup.addView(requireViewById2);
                ViewGroup.LayoutParams layoutParams2 = requireViewById2.getLayoutParams();
                if (layoutParams2 != null) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams2;
                    marginLayoutParams.bottomMargin = marginLayoutParams.topMargin;
                    marginLayoutParams.topMargin = 0;
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ TooltipManager(Context context, String str, int i, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, str, (i2 & 4) != 0 ? 2 : i, (i2 & 8) != 0 ? true : z);
    }

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/controls/TooltipManager$Companion;", "", "()V", "HIDE_DURATION_MS", "", "SHOW_DELAY_MS", "SHOW_DURATION_MS", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: TooltipManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final ViewGroup getLayout() {
        return this.layout;
    }

    public final Function1<Integer, Unit> getPreferenceStorer() {
        return this.preferenceStorer;
    }

    /* access modifiers changed from: private */
    /* renamed from: dismissView$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2599dismissView$lambda1$lambda0(TooltipManager tooltipManager, View view) {
        Intrinsics.checkNotNullParameter(tooltipManager, "this$0");
        tooltipManager.hide(true);
    }

    public final void show(int i, int i2, int i3) {
        if (shouldShow()) {
            this.textView.setText(i);
            int i4 = this.shown + 1;
            this.shown = i4;
            this.preferenceStorer.invoke(Integer.valueOf(i4));
            this.layout.post(new TooltipManager$$ExternalSyntheticLambda0(this, i2, i3));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-4  reason: not valid java name */
    public static final void m2601show$lambda4(TooltipManager tooltipManager, int i, int i2) {
        Intrinsics.checkNotNullParameter(tooltipManager, "this$0");
        int[] iArr = new int[2];
        tooltipManager.layout.getLocationOnScreen(iArr);
        ViewGroup viewGroup = tooltipManager.layout;
        boolean z = false;
        viewGroup.setTranslationX((float) ((i - iArr[0]) - (viewGroup.getWidth() / 2)));
        ViewGroup viewGroup2 = tooltipManager.layout;
        viewGroup2.setTranslationY(((float) (i2 - iArr[1])) - ((float) (!tooltipManager.below ? viewGroup2.getHeight() : 0)));
        if (tooltipManager.layout.getAlpha() == 0.0f) {
            z = true;
        }
        if (z) {
            tooltipManager.layout.animate().alpha(1.0f).withLayer().setStartDelay(500).setDuration(300).setInterpolator(new DecelerateInterpolator()).start();
        }
    }

    public static /* synthetic */ void hide$default(TooltipManager tooltipManager, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        tooltipManager.hide(z);
    }

    public final void hide(boolean z) {
        if (!(this.layout.getAlpha() == 0.0f)) {
            this.layout.post(new TooltipManager$$ExternalSyntheticLambda1(z, this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: hide$lambda-5  reason: not valid java name */
    public static final void m2600hide$lambda5(boolean z, TooltipManager tooltipManager) {
        Intrinsics.checkNotNullParameter(tooltipManager, "this$0");
        if (z) {
            tooltipManager.layout.animate().alpha(0.0f).withLayer().setStartDelay(0).setDuration(100).setInterpolator(new AccelerateInterpolator()).start();
            return;
        }
        tooltipManager.layout.animate().cancel();
        tooltipManager.layout.setAlpha(0.0f);
    }

    private final boolean shouldShow() {
        return this.shown < this.maxTimesShown;
    }
}
