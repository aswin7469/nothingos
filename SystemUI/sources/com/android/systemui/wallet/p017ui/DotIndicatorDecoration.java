package com.android.systemui.wallet.p017ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.MathUtils;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1894R;

/* renamed from: com.android.systemui.wallet.ui.DotIndicatorDecoration */
final class DotIndicatorDecoration extends RecyclerView.ItemDecoration {
    private WalletCardCarousel mCardCarousel;
    private final int mDotMargin;
    private final Paint mPaint = new Paint(1);
    private final int mSelectedColor;
    private final int mSelectedRadius;
    private final int mUnselectedColor;
    private final int mUnselectedRadius;

    DotIndicatorDecoration(Context context) {
        this.mUnselectedRadius = context.getResources().getDimensionPixelSize(C1894R.dimen.card_carousel_dot_unselected_radius);
        this.mSelectedRadius = context.getResources().getDimensionPixelSize(C1894R.dimen.card_carousel_dot_selected_radius);
        this.mDotMargin = context.getResources().getDimensionPixelSize(C1894R.dimen.card_carousel_dot_margin);
        this.mUnselectedColor = context.getColor(C1894R.C1895color.material_dynamic_neutral70);
        this.mSelectedColor = context.getColor(C1894R.C1895color.material_dynamic_neutral100);
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        if (recyclerView.getAdapter().getItemCount() > 1) {
            rect.bottom = view.getResources().getDimensionPixelSize(C1894R.dimen.card_carousel_dot_offset);
        }
    }

    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        this.mCardCarousel = (WalletCardCarousel) recyclerView;
        int itemCount = recyclerView.getAdapter().getItemCount();
        if (itemCount > 1) {
            canvas.save();
            float width = ((float) recyclerView.getWidth()) / 6.0f;
            float min = 1.0f - (Math.min(Math.abs(this.mCardCarousel.mEdgeToCenterDistance), width) / width);
            canvas.translate((((float) recyclerView.getWidth()) - ((float) (((this.mDotMargin * (itemCount - 1)) + ((this.mUnselectedRadius * 2) * (itemCount - 2))) + (this.mSelectedRadius * 2)))) / 2.0f, (float) (recyclerView.getHeight() - this.mDotMargin));
            for (int i = 0; i < itemCount; i++) {
                int i2 = isLayoutLtr() ? i : (itemCount - i) - 1;
                if (isSelectedItem(i2)) {
                    drawSelectedDot(canvas, min);
                } else if (isNextItemInScrollingDirection(i2)) {
                    drawFadingUnselectedDot(canvas, min);
                } else {
                    drawUnselectedDot(canvas);
                }
                canvas.translate((float) this.mDotMargin, 0.0f);
            }
            canvas.restore();
            this.mCardCarousel = null;
        }
    }

    private void drawSelectedDot(Canvas canvas, float f) {
        float f2 = f / 2.0f;
        this.mPaint.setColor(getTransitionAdjustedColor(ColorUtils.blendARGB(this.mSelectedColor, this.mUnselectedColor, f2)));
        float lerp = MathUtils.lerp(this.mSelectedRadius, this.mUnselectedRadius, f2);
        canvas.drawCircle(lerp, 0.0f, lerp, this.mPaint);
        canvas.translate(lerp * 2.0f, 0.0f);
    }

    private void drawFadingUnselectedDot(Canvas canvas, float f) {
        float f2 = f / 2.0f;
        this.mPaint.setColor(getTransitionAdjustedColor(ColorUtils.blendARGB(this.mUnselectedColor, this.mSelectedColor, f2)));
        float lerp = MathUtils.lerp(this.mUnselectedRadius, this.mSelectedColor, f2);
        canvas.drawCircle(lerp, 0.0f, lerp, this.mPaint);
        canvas.translate(lerp * 2.0f, 0.0f);
    }

    private void drawUnselectedDot(Canvas canvas) {
        this.mPaint.setColor(this.mUnselectedColor);
        int i = this.mUnselectedRadius;
        canvas.drawCircle((float) i, 0.0f, (float) i, this.mPaint);
        canvas.translate((float) (this.mUnselectedRadius * 2), 0.0f);
    }

    private int getTransitionAdjustedColor(int i) {
        return ColorUtils.setAlphaComponent(i, 255);
    }

    private boolean isSelectedItem(int i) {
        return this.mCardCarousel.mCenteredAdapterPosition == i;
    }

    private boolean isNextItemInScrollingDirection(int i) {
        if (isLayoutLtr()) {
            if ((this.mCardCarousel.mCenteredAdapterPosition + 1 != i || this.mCardCarousel.mEdgeToCenterDistance < 0.0f) && (this.mCardCarousel.mCenteredAdapterPosition - 1 != i || this.mCardCarousel.mEdgeToCenterDistance >= 0.0f)) {
                return false;
            }
            return true;
        } else if ((this.mCardCarousel.mCenteredAdapterPosition - 1 != i || this.mCardCarousel.mEdgeToCenterDistance < 0.0f) && (this.mCardCarousel.mCenteredAdapterPosition + 1 != i || this.mCardCarousel.mEdgeToCenterDistance >= 0.0f)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isLayoutLtr() {
        WalletCardCarousel walletCardCarousel = this.mCardCarousel;
        if (walletCardCarousel == null || walletCardCarousel.getLayoutDirection() == 0) {
            return true;
        }
        return false;
    }
}
