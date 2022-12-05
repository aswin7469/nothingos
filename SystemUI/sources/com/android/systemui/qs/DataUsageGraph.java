package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.android.settingslib.Utils;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
/* loaded from: classes.dex */
public class DataUsageGraph extends View {
    private long mLimitLevel;
    private final int mMarkerWidth;
    private long mMaxLevel;
    private final int mOverlimitColor;
    private final int mTrackColor;
    private final int mUsageColor;
    private long mUsageLevel;
    private final int mWarningColor;
    private long mWarningLevel;
    private final RectF mTmpRect = new RectF();
    private final Paint mTmpPaint = new Paint();

    public DataUsageGraph(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Resources resources = context.getResources();
        this.mTrackColor = Utils.getColorStateListDefaultColor(context, R$color.data_usage_graph_track);
        this.mWarningColor = Utils.getColorStateListDefaultColor(context, R$color.data_usage_graph_warning);
        this.mUsageColor = Utils.getColorAccentDefaultColor(context);
        this.mOverlimitColor = Utils.getColorErrorDefaultColor(context);
        this.mMarkerWidth = resources.getDimensionPixelSize(R$dimen.data_usage_graph_marker_width);
    }

    public void setLevels(long j, long j2, long j3) {
        this.mLimitLevel = Math.max(0L, j);
        this.mWarningLevel = Math.max(0L, j2);
        this.mUsageLevel = Math.max(0L, j3);
        this.mMaxLevel = Math.max(Math.max(Math.max(this.mLimitLevel, this.mWarningLevel), this.mUsageLevel), 1L);
        postInvalidate();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        RectF rectF = this.mTmpRect;
        Paint paint = this.mTmpPaint;
        int width = getWidth();
        int height = getHeight();
        long j = this.mLimitLevel;
        boolean z = j > 0 && this.mUsageLevel > j;
        float f = width;
        long j2 = this.mMaxLevel;
        float f2 = (((float) this.mUsageLevel) / ((float) j2)) * f;
        if (z) {
            f2 = Math.min(Math.max(((((float) j) / ((float) j2)) * f) - (i / 2), this.mMarkerWidth), width - (this.mMarkerWidth * 2));
            rectF.set(this.mMarkerWidth + f2, 0.0f, f, height);
            paint.setColor(this.mOverlimitColor);
            canvas.drawRect(rectF, paint);
        } else {
            rectF.set(0.0f, 0.0f, f, height);
            paint.setColor(this.mTrackColor);
            canvas.drawRect(rectF, paint);
        }
        float f3 = height;
        rectF.set(0.0f, 0.0f, f2, f3);
        paint.setColor(this.mUsageColor);
        canvas.drawRect(rectF, paint);
        float min = Math.min(Math.max((f * (((float) this.mWarningLevel) / ((float) this.mMaxLevel))) - (this.mMarkerWidth / 2), 0.0f), width - this.mMarkerWidth);
        rectF.set(min, 0.0f, this.mMarkerWidth + min, f3);
        paint.setColor(this.mWarningColor);
        canvas.drawRect(rectF, paint);
    }
}
