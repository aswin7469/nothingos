package com.android.systemui.statusbar;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.util.AttributeSet;
import com.android.systemui.C1893R;
import java.p026io.IOException;
import org.xmlpull.p032v1.XmlPullParser;
import org.xmlpull.p032v1.XmlPullParserException;

public class CastDrawable extends DrawableWrapper {
    private Drawable mFillDrawable;
    private int mHorizontalPadding;

    public CastDrawable() {
        super((Drawable) null);
    }

    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        setDrawable(resources.getDrawable(C1893R.C1895drawable.ic_cast, theme).mutate());
        this.mFillDrawable = resources.getDrawable(C1893R.C1895drawable.ic_cast_connected_fill, theme).mutate();
        this.mHorizontalPadding = resources.getDimensionPixelSize(C1893R.dimen.status_bar_horizontal_padding);
    }

    public boolean canApplyTheme() {
        return this.mFillDrawable.canApplyTheme() || super.canApplyTheme();
    }

    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        this.mFillDrawable.applyTheme(theme);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        this.mFillDrawable.setBounds(rect);
    }

    public boolean onLayoutDirectionChanged(int i) {
        this.mFillDrawable.setLayoutDirection(i);
        return super.onLayoutDirectionChanged(i);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.mFillDrawable.draw(canvas);
    }

    public boolean getPadding(Rect rect) {
        rect.left += this.mHorizontalPadding;
        rect.right += this.mHorizontalPadding;
        return true;
    }

    public void setAlpha(int i) {
        super.setAlpha(i);
        this.mFillDrawable.setAlpha(i);
    }

    public boolean setVisible(boolean z, boolean z2) {
        this.mFillDrawable.setVisible(z, z2);
        return super.setVisible(z, z2);
    }

    public Drawable mutate() {
        this.mFillDrawable.mutate();
        return super.mutate();
    }
}
