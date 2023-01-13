package com.android.systemui.wallet.p017ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import androidx.cardview.widget.CardView;
import com.android.systemui.C1894R;

/* renamed from: com.android.systemui.wallet.ui.WalletCardView */
public class WalletCardView extends CardView {
    private final Paint mBorderPaint;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public WalletCardView(Context context) {
        this(context, (AttributeSet) null);
    }

    public WalletCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mBorderPaint = paint;
        paint.setColor(context.getColor(C1894R.C1895color.wallet_card_border));
        paint.setStrokeWidth(context.getResources().getDimension(C1894R.dimen.wallet_card_border_width));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        float radius = getRadius();
        canvas.drawRoundRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), radius, radius, this.mBorderPaint);
    }
}
