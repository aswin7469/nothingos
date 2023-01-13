package com.android.systemui.controls.p010ui;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u0015H\u0016J(\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0014\u001a\u00020\u00172\u0006\u0010\u0019\u001a\u00020\u0017H\u0016J\u0010\u0010\u001a\u001a\u00020\u00102\u0006\u0010\u0014\u001a\u00020\u001bH\u0002R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\n¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u001c"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/CornerDrawable;", "Landroid/graphics/drawable/DrawableWrapper;", "wrapped", "Landroid/graphics/drawable/Drawable;", "cornerRadius", "", "(Landroid/graphics/drawable/Drawable;F)V", "getCornerRadius", "()F", "path", "Landroid/graphics/Path;", "getPath", "()Landroid/graphics/Path;", "getWrapped", "()Landroid/graphics/drawable/Drawable;", "draw", "", "canvas", "Landroid/graphics/Canvas;", "setBounds", "r", "Landroid/graphics/Rect;", "l", "", "t", "b", "updatePath", "Landroid/graphics/RectF;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.CornerDrawable */
/* compiled from: CornerDrawable.kt */
public final class CornerDrawable extends DrawableWrapper {
    private final float cornerRadius;
    private final Path path = new Path();
    private final Drawable wrapped;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public CornerDrawable(Drawable drawable, float f) {
        super(drawable);
        Intrinsics.checkNotNullParameter(drawable, "wrapped");
        this.wrapped = drawable;
        this.cornerRadius = f;
        updatePath(new RectF(getBounds()));
    }

    public final float getCornerRadius() {
        return this.cornerRadius;
    }

    public final Drawable getWrapped() {
        return this.wrapped;
    }

    public final Path getPath() {
        return this.path;
    }

    public void draw(Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        canvas.clipPath(this.path);
        super.draw(canvas);
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        updatePath(new RectF((float) i, (float) i2, (float) i3, (float) i4));
        super.setBounds(i, i2, i3, i4);
    }

    public void setBounds(Rect rect) {
        Intrinsics.checkNotNullParameter(rect, "r");
        updatePath(new RectF(rect));
        super.setBounds(rect);
    }

    private final void updatePath(RectF rectF) {
        this.path.reset();
        Path path2 = this.path;
        float f = this.cornerRadius;
        path2.addRoundRect(rectF, f, f, Path.Direction.CW);
    }
}
