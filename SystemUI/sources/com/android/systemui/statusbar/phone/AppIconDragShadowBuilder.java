package com.android.systemui.statusbar.phone;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

class AppIconDragShadowBuilder extends View.DragShadowBuilder {
    private static final int ICON_SCALE = 2;
    final Drawable mDrawable;
    final int mIconSize;

    public AppIconDragShadowBuilder(ImageView imageView) {
        this.mDrawable = imageView.getDrawable();
        this.mIconSize = imageView.getHeight() * 2;
    }

    public void onProvideShadowMetrics(Point point, Point point2) {
        int i = this.mIconSize;
        point.set(i, i);
        int i2 = this.mIconSize;
        point2.set(i2 / 2, (i2 * 2) / 3);
    }

    public void onDrawShadow(Canvas canvas) {
        Rect copyBounds = this.mDrawable.copyBounds();
        Drawable drawable = this.mDrawable;
        int i = this.mIconSize;
        drawable.setBounds(0, 0, i, i);
        this.mDrawable.draw(canvas);
        this.mDrawable.setBounds(copyBounds);
    }
}
