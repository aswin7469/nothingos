package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.util.ContrastColorUtil;
import com.android.wm.shell.R;
import com.android.wm.shell.animation.Interpolators;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ManageEducationView.kt */
/* loaded from: classes2.dex */
public final class ManageEducationView extends LinearLayout {
    private boolean isHiding;
    @NotNull
    private final String TAG = "Bubbles";
    private final long ANIMATE_DURATION = 200;
    private final long ANIMATE_DURATION_SHORT = 40;
    @NotNull
    private final Lazy manageView$delegate = LazyKt.lazy(new ManageEducationView$manageView$2(this));
    @NotNull
    private final Lazy manageButton$delegate = LazyKt.lazy(new ManageEducationView$manageButton$2(this));
    @NotNull
    private final Lazy gotItButton$delegate = LazyKt.lazy(new ManageEducationView$gotItButton$2(this));
    @NotNull
    private final Lazy titleTextView$delegate = LazyKt.lazy(new ManageEducationView$titleTextView$2(this));
    @NotNull
    private final Lazy descTextView$delegate = LazyKt.lazy(new ManageEducationView$descTextView$2(this));

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ManageEducationView(@NotNull Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        LayoutInflater.from(context).inflate(R.layout.bubbles_manage_button_education, this);
        setVisibility(8);
        setElevation(getResources().getDimensionPixelSize(R.dimen.bubble_elevation));
        setLayoutDirection(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final View getManageView() {
        return (View) this.manageView$delegate.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Button getManageButton() {
        return (Button) this.manageButton$delegate.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Button getGotItButton() {
        return (Button) this.gotItButton$delegate.getValue();
    }

    private final TextView getTitleTextView() {
        return (TextView) this.titleTextView$delegate.getValue();
    }

    private final TextView getDescTextView() {
        return (TextView) this.descTextView$delegate.getValue();
    }

    @Override // android.view.View
    public void setLayoutDirection(int i) {
        super.setLayoutDirection(i);
        setDrawableDirection();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
        setTextColor();
    }

    private final void setTextColor() {
        TypedArray obtainStyledAttributes = ((LinearLayout) this).mContext.obtainStyledAttributes(new int[]{16843829, 16842809});
        int color = obtainStyledAttributes.getColor(0, -16777216);
        int color2 = obtainStyledAttributes.getColor(1, -1);
        obtainStyledAttributes.recycle();
        int ensureTextContrast = ContrastColorUtil.ensureTextContrast(color2, color, true);
        getTitleTextView().setTextColor(ensureTextContrast);
        getDescTextView().setTextColor(ensureTextContrast);
    }

    private final void setDrawableDirection() {
        int i;
        View manageView = getManageView();
        if (getResources().getConfiguration().getLayoutDirection() == 1) {
            i = R.drawable.bubble_stack_user_education_bg_rtl;
        } else {
            i = R.drawable.bubble_stack_user_education_bg;
        }
        manageView.setBackgroundResource(i);
    }

    public final void show(@NotNull final BubbleExpandedView expandedView, @NotNull final Rect rect) {
        Intrinsics.checkNotNullParameter(expandedView, "expandedView");
        Intrinsics.checkNotNullParameter(rect, "rect");
        if (getVisibility() == 0) {
            return;
        }
        setAlpha(0.0f);
        setVisibility(0);
        post(new Runnable() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1
            @Override // java.lang.Runnable
            public final void run() {
                Button manageButton;
                Button gotItButton;
                View manageView;
                View manageView2;
                long j;
                BubbleExpandedView.this.getManageButtonBoundsOnScreen(rect);
                manageButton = this.getManageButton();
                final BubbleExpandedView bubbleExpandedView = BubbleExpandedView.this;
                final ManageEducationView manageEducationView = this;
                manageButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        BubbleExpandedView.this.findViewById(R.id.settings_button).performClick();
                        manageEducationView.hide(true);
                    }
                });
                gotItButton = this.getGotItButton();
                final ManageEducationView manageEducationView2 = this;
                gotItButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.2
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ManageEducationView.this.hide(true);
                    }
                });
                final ManageEducationView manageEducationView3 = this;
                manageEducationView3.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.bubbles.ManageEducationView$show$1.3
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        ManageEducationView.this.hide(true);
                    }
                });
                manageView = this.getManageView();
                Rect rect2 = rect;
                ManageEducationView manageEducationView4 = this;
                manageView.setTranslationX(0.0f);
                int dimensionPixelSize = manageView.getResources().getDimensionPixelSize(R.dimen.bubbles_manage_education_top_inset);
                int i = rect2.top;
                manageView2 = manageEducationView4.getManageView();
                manageView.setTranslationY((i - manageView2.getHeight()) + dimensionPixelSize);
                this.bringToFront();
                ViewPropertyAnimator animate = this.animate();
                j = this.ANIMATE_DURATION;
                animate.setDuration(j).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
            }
        });
        setShouldShow(false);
    }

    public final void hide(boolean z) {
        if (getVisibility() != 0 || this.isHiding) {
            return;
        }
        animate().withStartAction(new Runnable() { // from class: com.android.wm.shell.bubbles.ManageEducationView$hide$1
            @Override // java.lang.Runnable
            public final void run() {
                ManageEducationView.this.isHiding = true;
            }
        }).alpha(0.0f).setDuration(z ? this.ANIMATE_DURATION_SHORT : this.ANIMATE_DURATION).withEndAction(new Runnable() { // from class: com.android.wm.shell.bubbles.ManageEducationView$hide$2
            @Override // java.lang.Runnable
            public final void run() {
                ManageEducationView.this.isHiding = false;
                ManageEducationView.this.setVisibility(8);
            }
        });
    }

    private final void setShouldShow(boolean z) {
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean("HasSeenBubblesManageOnboarding", !z).apply();
    }
}
