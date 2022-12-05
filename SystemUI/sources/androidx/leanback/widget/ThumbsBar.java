package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.leanback.R$dimen;
/* loaded from: classes.dex */
public class ThumbsBar extends LinearLayout {
    final SparseArray<Bitmap> mBitmaps;
    int mHeroThumbHeightInPixel;
    int mHeroThumbWidthInPixel;
    private boolean mIsUserSets;
    int mMeasuredMarginInPixel;
    int mNumOfThumbs;
    int mThumbHeightInPixel;
    int mThumbWidthInPixel;

    public ThumbsBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThumbsBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mNumOfThumbs = -1;
        this.mBitmaps = new SparseArray<>();
        this.mIsUserSets = false;
        this.mThumbWidthInPixel = context.getResources().getDimensionPixelSize(R$dimen.lb_playback_transport_thumbs_width);
        this.mThumbHeightInPixel = context.getResources().getDimensionPixelSize(R$dimen.lb_playback_transport_thumbs_height);
        this.mHeroThumbHeightInPixel = context.getResources().getDimensionPixelSize(R$dimen.lb_playback_transport_hero_thumbs_width);
        this.mHeroThumbWidthInPixel = context.getResources().getDimensionPixelSize(R$dimen.lb_playback_transport_hero_thumbs_height);
        this.mMeasuredMarginInPixel = context.getResources().getDimensionPixelSize(R$dimen.lb_playback_transport_thumbs_margin);
    }

    public int getHeroIndex() {
        return getChildCount() / 2;
    }

    private void setNumberOfThumbsInternal() {
        while (getChildCount() > this.mNumOfThumbs) {
            removeView(getChildAt(getChildCount() - 1));
        }
        while (getChildCount() < this.mNumOfThumbs) {
            addView(createThumbView(this), new LinearLayout.LayoutParams(this.mThumbWidthInPixel, this.mThumbHeightInPixel));
        }
        int heroIndex = getHeroIndex();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            if (heroIndex == i) {
                layoutParams.width = this.mHeroThumbWidthInPixel;
                layoutParams.height = this.mHeroThumbHeightInPixel;
            } else {
                layoutParams.width = this.mThumbWidthInPixel;
                layoutParams.height = this.mThumbHeightInPixel;
            }
            childAt.setLayoutParams(layoutParams);
        }
    }

    private static int roundUp(int num, int divisor) {
        return ((num + divisor) - 1) / divisor;
    }

    private int calculateNumOfThumbs(int widthInPixel) {
        int roundUp = roundUp(widthInPixel - this.mHeroThumbWidthInPixel, this.mThumbWidthInPixel + this.mMeasuredMarginInPixel);
        if (roundUp < 2) {
            roundUp = 2;
        } else if ((roundUp & 1) != 0) {
            roundUp++;
        }
        return roundUp + 1;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int calculateNumOfThumbs;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        if (this.mIsUserSets || this.mNumOfThumbs == (calculateNumOfThumbs = calculateNumOfThumbs(measuredWidth))) {
            return;
        }
        this.mNumOfThumbs = calculateNumOfThumbs;
        setNumberOfThumbsInternal();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int heroIndex = getHeroIndex();
        View childAt = getChildAt(heroIndex);
        int width = (getWidth() / 2) - (childAt.getMeasuredWidth() / 2);
        int width2 = (getWidth() / 2) + (childAt.getMeasuredWidth() / 2);
        childAt.layout(width, getPaddingTop(), width2, getPaddingTop() + childAt.getMeasuredHeight());
        int paddingTop = getPaddingTop() + (childAt.getMeasuredHeight() / 2);
        for (int i = heroIndex - 1; i >= 0; i--) {
            int i2 = width - this.mMeasuredMarginInPixel;
            View childAt2 = getChildAt(i);
            childAt2.layout(i2 - childAt2.getMeasuredWidth(), paddingTop - (childAt2.getMeasuredHeight() / 2), i2, (childAt2.getMeasuredHeight() / 2) + paddingTop);
            width = i2 - childAt2.getMeasuredWidth();
        }
        while (true) {
            heroIndex++;
            if (heroIndex < this.mNumOfThumbs) {
                int i3 = width2 + this.mMeasuredMarginInPixel;
                View childAt3 = getChildAt(heroIndex);
                childAt3.layout(i3, paddingTop - (childAt3.getMeasuredHeight() / 2), childAt3.getMeasuredWidth() + i3, (childAt3.getMeasuredHeight() / 2) + paddingTop);
                width2 = i3 + childAt3.getMeasuredWidth();
            } else {
                return;
            }
        }
    }

    protected View createThumbView(ViewGroup parent) {
        return new ImageView(parent.getContext());
    }
}
