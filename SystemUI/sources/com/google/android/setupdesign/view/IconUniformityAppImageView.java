package com.google.android.setupdesign.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.google.android.setupdesign.C3963R;
import com.google.android.setupdesign.view.IconUniformityAppImageViewBindable;
import com.google.android.setupdesign.widget.CardBackgroundDrawable;

public class IconUniformityAppImageView extends ImageView implements IconUniformityAppImageViewBindable {
    private static final Float APPS_ICON_RADIUS_MULTIPLIER = Float.valueOf(0.2f);
    private static final Float LEGACY_SIZE_SCALE_FACTOR;
    private static final Float LEGACY_SIZE_SCALE_MARGIN_FACTOR;
    private static final boolean ON_L_PLUS = true;
    private int backdropColorResId = 0;
    private final GradientDrawable backdropDrawable = new GradientDrawable();
    private CardBackgroundDrawable cardBackgroundDrawable;

    static {
        Float valueOf = Float.valueOf(0.75f);
        LEGACY_SIZE_SCALE_FACTOR = valueOf;
        LEGACY_SIZE_SCALE_MARGIN_FACTOR = Float.valueOf((1.0f - valueOf.floatValue()) / 2.0f);
    }

    public IconUniformityAppImageView(Context context) {
        super(context);
    }

    public IconUniformityAppImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public IconUniformityAppImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public IconUniformityAppImageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.backdropColorResId = C3963R.C3964color.sud_uniformity_backdrop_color;
        this.backdropDrawable.setColor(ContextCompat.getColor(getContext(), this.backdropColorResId));
    }

    public void bindView(IconUniformityAppImageViewBindable.IconUniformityAppImageViewData iconUniformityAppImageViewData) {
        setLegacyTransformationMatrix((float) iconUniformityAppImageViewData.icon.getMinimumWidth(), (float) iconUniformityAppImageViewData.icon.getMinimumHeight(), (float) getLayoutParams().width, (float) getLayoutParams().height);
        final float floatValue = ((float) getLayoutParams().height) * APPS_ICON_RADIUS_MULTIPLIER.floatValue();
        if (ON_L_PLUS) {
            setBackgroundColor(ContextCompat.getColor(getContext(), this.backdropColorResId));
            this.backdropDrawable.setCornerRadius(floatValue);
            setElevation(getContext().getResources().getDimension(C3963R.dimen.sud_icon_uniformity_elevation));
            setClipToOutline(true);
            setOutlineProvider(new ViewOutlineProvider() {
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, IconUniformityAppImageView.this.getLayoutParams().width, IconUniformityAppImageView.this.getLayoutParams().height, floatValue);
                }
            });
        } else {
            CardBackgroundDrawable cardBackgroundDrawable2 = new CardBackgroundDrawable(ContextCompat.getColor(getContext(), this.backdropColorResId), floatValue, 0.0f);
            this.cardBackgroundDrawable = cardBackgroundDrawable2;
            cardBackgroundDrawable2.setBounds(0, 0, getLayoutParams().width, getLayoutParams().height);
        }
        setImageDrawable(iconUniformityAppImageViewData.icon);
    }

    public void onRecycle() {
        setImageDrawable((Drawable) null);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        CardBackgroundDrawable cardBackgroundDrawable2;
        super.onDraw(canvas);
        if (!ON_L_PLUS && (cardBackgroundDrawable2 = this.cardBackgroundDrawable) != null) {
            cardBackgroundDrawable2.draw(canvas);
        }
        super.onDraw(canvas);
    }

    private void setLegacyTransformationMatrix(float f, float f2, float f3, float f4) {
        Matrix matrix = new Matrix();
        Float f5 = LEGACY_SIZE_SCALE_MARGIN_FACTOR;
        float floatValue = f5.floatValue() * f4;
        float floatValue2 = f5.floatValue() * f3;
        matrix.setRectToRect(new RectF(0.0f, 0.0f, f, f2), new RectF(floatValue2, floatValue, f3 - floatValue2, f4 - floatValue), Matrix.ScaleToFit.FILL);
        setScaleType(ImageView.ScaleType.MATRIX);
        setImageMatrix(matrix);
    }
}
