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
import com.android.systemui.Prefs;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.recents.TriangleShape;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: TooltipManager.kt */
/* loaded from: classes.dex */
public final class TooltipManager {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final View arrowView;
    private final boolean below;
    private final View dismissView;
    @NotNull
    private final ViewGroup layout;
    private final int maxTimesShown;
    @NotNull
    private final String preferenceName;
    @NotNull
    private final Function1<Integer, Unit> preferenceStorer;
    private int shown;
    private final TextView textView;

    public TooltipManager(@NotNull Context context, @NotNull String preferenceName, int i, boolean z) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(preferenceName, "preferenceName");
        this.preferenceName = preferenceName;
        this.maxTimesShown = i;
        this.below = z;
        this.shown = Prefs.getInt(context, preferenceName, 0);
        View inflate = LayoutInflater.from(context).inflate(R$layout.controls_onboarding, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup = (ViewGroup) inflate;
        this.layout = viewGroup;
        this.preferenceStorer = new TooltipManager$preferenceStorer$1(context, this);
        viewGroup.setAlpha(0.0f);
        this.textView = (TextView) viewGroup.requireViewById(R$id.onboarding_text);
        View requireViewById = viewGroup.requireViewById(R$id.dismiss);
        requireViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.TooltipManager$dismissView$1$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TooltipManager.this.hide(true);
            }
        });
        Unit unit = Unit.INSTANCE;
        this.dismissView = requireViewById;
        View requireViewById2 = viewGroup.requireViewById(R$id.arrow);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843829, typedValue, true);
        int color = context.getResources().getColor(typedValue.resourceId, context.getTheme());
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(R$dimen.recents_onboarding_toast_arrow_corner_radius);
        ViewGroup.LayoutParams layoutParams = requireViewById2.getLayoutParams();
        ShapeDrawable shapeDrawable = new ShapeDrawable(TriangleShape.create(layoutParams.width, layoutParams.height, z));
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(color);
        paint.setPathEffect(new CornerPathEffect(dimensionPixelSize));
        requireViewById2.setBackground(shapeDrawable);
        this.arrowView = requireViewById2;
        if (!z) {
            viewGroup.removeView(requireViewById2);
            viewGroup.addView(requireViewById2);
            ViewGroup.LayoutParams layoutParams2 = requireViewById2.getLayoutParams();
            Objects.requireNonNull(layoutParams2, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams2;
            marginLayoutParams.bottomMargin = marginLayoutParams.topMargin;
            marginLayoutParams.topMargin = 0;
        }
    }

    public /* synthetic */ TooltipManager(Context context, String str, int i, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, str, (i2 & 4) != 0 ? 2 : i, (i2 & 8) != 0 ? true : z);
    }

    /* compiled from: TooltipManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @NotNull
    public final ViewGroup getLayout() {
        return this.layout;
    }

    public final void show(int i, final int i2, final int i3) {
        if (!shouldShow()) {
            return;
        }
        this.textView.setText(i);
        int i4 = this.shown + 1;
        this.shown = i4;
        this.preferenceStorer.mo1949invoke(Integer.valueOf(i4));
        this.layout.post(new Runnable() { // from class: com.android.systemui.controls.TooltipManager$show$1
            @Override // java.lang.Runnable
            public final void run() {
                boolean z;
                int[] iArr = new int[2];
                TooltipManager.this.getLayout().getLocationOnScreen(iArr);
                boolean z2 = false;
                TooltipManager.this.getLayout().setTranslationX((i2 - iArr[0]) - (TooltipManager.this.getLayout().getWidth() / 2));
                ViewGroup layout = TooltipManager.this.getLayout();
                float f = i3 - iArr[1];
                z = TooltipManager.this.below;
                layout.setTranslationY(f - (!z ? TooltipManager.this.getLayout().getHeight() : 0));
                if (TooltipManager.this.getLayout().getAlpha() == 0.0f) {
                    z2 = true;
                }
                if (z2) {
                    TooltipManager.this.getLayout().animate().alpha(1.0f).withLayer().setStartDelay(500L).setDuration(300L).setInterpolator(new DecelerateInterpolator()).start();
                }
            }
        });
    }

    public final void hide(final boolean z) {
        if (this.layout.getAlpha() == 0.0f) {
            return;
        }
        this.layout.post(new Runnable() { // from class: com.android.systemui.controls.TooltipManager$hide$1
            @Override // java.lang.Runnable
            public final void run() {
                if (z) {
                    this.getLayout().animate().alpha(0.0f).withLayer().setStartDelay(0L).setDuration(100L).setInterpolator(new AccelerateInterpolator()).start();
                    return;
                }
                this.getLayout().animate().cancel();
                this.getLayout().setAlpha(0.0f);
            }
        });
    }

    private final boolean shouldShow() {
        return this.shown < this.maxTimesShown;
    }
}
