package com.android.systemui.controls.ui;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: CornerDrawable.kt */
/* loaded from: classes.dex */
public final class CornerDrawable extends DrawableWrapper {
    private final float cornerRadius;
    @NotNull
    private final Path path = new Path();
    @NotNull
    private final Drawable wrapped;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CornerDrawable(@NotNull Drawable wrapped, float f) {
        super(wrapped);
        Intrinsics.checkNotNullParameter(wrapped, "wrapped");
        this.wrapped = wrapped;
        this.cornerRadius = f;
        updatePath(new RectF(getBounds()));
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public void draw(@NotNull Canvas canvas) {
        Intrinsics.checkNotNullParameter(canvas, "canvas");
        canvas.clipPath(this.path);
        super.draw(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(int i, int i2, int i3, int i4) {
        updatePath(new RectF(i, i2, i3, i4));
        super.setBounds(i, i2, i3, i4);
    }

    @Override // android.graphics.drawable.Drawable
    public void setBounds(@NotNull Rect r) {
        Intrinsics.checkNotNullParameter(r, "r");
        updatePath(new RectF(r));
        super.setBounds(r);
    }

    private final void updatePath(RectF rectF) {
        this.path.reset();
        Path path = this.path;
        float f = this.cornerRadius;
        path.addRoundRect(rectF, f, f, Path.Direction.CW);
    }
}
