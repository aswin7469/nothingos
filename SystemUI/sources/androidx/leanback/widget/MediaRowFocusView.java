package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.leanback.R$color;
/* loaded from: classes.dex */
class MediaRowFocusView extends View {
    private final Paint mPaint;
    private final RectF mRoundRectF = new RectF();
    private int mRoundRectRadius;

    public MediaRowFocusView(Context context) {
        super(context);
        this.mPaint = createPaint(context);
    }

    public MediaRowFocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mPaint = createPaint(context);
    }

    public MediaRowFocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mPaint = createPaint(context);
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight() / 2;
        this.mRoundRectRadius = height;
        int height2 = ((height * 2) - getHeight()) / 2;
        this.mRoundRectF.set(0.0f, -height2, getWidth(), getHeight() + height2);
        RectF rectF = this.mRoundRectF;
        int i = this.mRoundRectRadius;
        canvas.drawRoundRect(rectF, i, i, this.mPaint);
    }

    private Paint createPaint(Context context) {
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(R$color.lb_playback_media_row_highlight_color));
        return paint;
    }
}
