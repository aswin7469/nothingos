package com.android.p019wm.shell.common.split;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.RoundedCorner;
import android.view.View;
import com.android.p019wm.shell.C3353R;

/* renamed from: com.android.wm.shell.common.split.DividerRoundedCorner */
public class DividerRoundedCorner extends View {
    private InvertedRoundedCornerDrawInfo mBottomLeftCorner;
    private InvertedRoundedCornerDrawInfo mBottomRightCorner;
    private final Paint mDividerBarBackground;
    /* access modifiers changed from: private */
    public final int mDividerWidth = getResources().getDimensionPixelSize(C3353R.dimen.split_divider_bar_width);
    private final Point mStartPos = new Point();
    private InvertedRoundedCornerDrawInfo mTopLeftCorner;
    private InvertedRoundedCornerDrawInfo mTopRightCorner;

    public boolean hasOverlappingRendering() {
        return false;
    }

    public DividerRoundedCorner(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mDividerBarBackground = paint;
        paint.setColor(getResources().getColor(C3353R.C3354color.split_divider_background, (Resources.Theme) null));
        paint.setFlags(1);
        paint.setStyle(Paint.Style.FILL);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mTopLeftCorner = new InvertedRoundedCornerDrawInfo(0);
        this.mTopRightCorner = new InvertedRoundedCornerDrawInfo(1);
        this.mBottomLeftCorner = new InvertedRoundedCornerDrawInfo(3);
        this.mBottomRightCorner = new InvertedRoundedCornerDrawInfo(2);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.save();
        this.mTopLeftCorner.calculateStartPos(this.mStartPos);
        canvas.translate((float) this.mStartPos.x, (float) this.mStartPos.y);
        canvas.drawPath(this.mTopLeftCorner.mPath, this.mDividerBarBackground);
        canvas.translate((float) (-this.mStartPos.x), (float) (-this.mStartPos.y));
        this.mTopRightCorner.calculateStartPos(this.mStartPos);
        canvas.translate((float) this.mStartPos.x, (float) this.mStartPos.y);
        canvas.drawPath(this.mTopRightCorner.mPath, this.mDividerBarBackground);
        canvas.translate((float) (-this.mStartPos.x), (float) (-this.mStartPos.y));
        this.mBottomLeftCorner.calculateStartPos(this.mStartPos);
        canvas.translate((float) this.mStartPos.x, (float) this.mStartPos.y);
        canvas.drawPath(this.mBottomLeftCorner.mPath, this.mDividerBarBackground);
        canvas.translate((float) (-this.mStartPos.x), (float) (-this.mStartPos.y));
        this.mBottomRightCorner.calculateStartPos(this.mStartPos);
        canvas.translate((float) this.mStartPos.x, (float) this.mStartPos.y);
        canvas.drawPath(this.mBottomRightCorner.mPath, this.mDividerBarBackground);
        canvas.restore();
    }

    /* access modifiers changed from: private */
    public boolean isLandscape() {
        return getResources().getConfiguration().orientation == 2;
    }

    /* renamed from: com.android.wm.shell.common.split.DividerRoundedCorner$InvertedRoundedCornerDrawInfo */
    private class InvertedRoundedCornerDrawInfo {
        private final int mCornerPosition;
        /* access modifiers changed from: private */
        public final Path mPath;
        private final int mRadius;

        InvertedRoundedCornerDrawInfo(int i) {
            int i2;
            Path path = new Path();
            this.mPath = path;
            this.mCornerPosition = i;
            RoundedCorner roundedCorner = DividerRoundedCorner.this.getDisplay().getRoundedCorner(i);
            if (roundedCorner == null) {
                i2 = 0;
            } else {
                i2 = roundedCorner.getRadius();
            }
            this.mRadius = i2;
            Path path2 = new Path();
            float f = 0.0f;
            path2.addRect(0.0f, 0.0f, (float) i2, (float) i2, Path.Direction.CW);
            Path path3 = new Path();
            path3.addCircle(isLeftCorner() ? (float) i2 : 0.0f, isTopCorner() ? (float) i2 : f, (float) i2, Path.Direction.CW);
            path.op(path2, path3, Path.Op.DIFFERENCE);
        }

        /* access modifiers changed from: private */
        public void calculateStartPos(Point point) {
            int i;
            int i2;
            int i3 = 0;
            if (DividerRoundedCorner.this.isLandscape()) {
                if (isLeftCorner()) {
                    i2 = (DividerRoundedCorner.this.getWidth() / 2) + (DividerRoundedCorner.this.mDividerWidth / 2);
                } else {
                    i2 = ((DividerRoundedCorner.this.getWidth() / 2) - (DividerRoundedCorner.this.mDividerWidth / 2)) - this.mRadius;
                }
                point.x = i2;
                if (!isTopCorner()) {
                    i3 = DividerRoundedCorner.this.getHeight() - this.mRadius;
                }
                point.y = i3;
                return;
            }
            if (!isLeftCorner()) {
                i3 = DividerRoundedCorner.this.getWidth() - this.mRadius;
            }
            point.x = i3;
            if (isTopCorner()) {
                i = (DividerRoundedCorner.this.getHeight() / 2) + (DividerRoundedCorner.this.mDividerWidth / 2);
            } else {
                i = ((DividerRoundedCorner.this.getHeight() / 2) - (DividerRoundedCorner.this.mDividerWidth / 2)) - this.mRadius;
            }
            point.y = i;
        }

        private boolean isLeftCorner() {
            int i = this.mCornerPosition;
            return i == 0 || i == 3;
        }

        private boolean isTopCorner() {
            int i = this.mCornerPosition;
            return i == 0 || i == 1;
        }
    }
}
