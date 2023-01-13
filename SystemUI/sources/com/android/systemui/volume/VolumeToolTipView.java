package com.android.systemui.volume;

import android.content.Context;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.android.systemui.C1894R;
import com.android.systemui.recents.TriangleShape;

public class VolumeToolTipView extends LinearLayout {
    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public VolumeToolTipView(Context context) {
        super(context);
    }

    public VolumeToolTipView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public VolumeToolTipView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public VolumeToolTipView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        drawArrow();
    }

    private void drawArrow() {
        View findViewById = findViewById(C1894R.C1898id.arrow);
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        ShapeDrawable shapeDrawable = new ShapeDrawable(TriangleShape.createHorizontal((float) layoutParams.width, (float) layoutParams.height, !isLandscape()));
        Paint paint = shapeDrawable.getPaint();
        TypedValue typedValue = new TypedValue();
        getContext().getTheme().resolveAttribute(16843829, typedValue, true);
        paint.setColor(ContextCompat.getColor(getContext(), typedValue.resourceId));
        paint.setPathEffect(new CornerPathEffect(getResources().getDimension(C1894R.dimen.volume_tool_tip_arrow_corner_radius)));
        findViewById.setBackground(shapeDrawable);
    }

    private boolean isLandscape() {
        return getContext().getResources().getConfiguration().orientation == 2;
    }
}
