package com.android.launcher3.icons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class BitmapRenderer$$ExternalSyntheticLambda0 implements BitmapRenderer {
    public final /* synthetic */ Bitmap f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ BitmapRenderer$$ExternalSyntheticLambda0(Bitmap bitmap, int i, int i2, int i3, int i4) {
        this.f$0 = bitmap;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = i3;
        this.f$4 = i4;
    }

    public final void draw(Canvas canvas) {
        canvas.drawBitmap(this.f$0, new Rect(this.f$1, this.f$2, this.f$1 + this.f$3, this.f$2 + this.f$4), new RectF(0.0f, 0.0f, (float) this.f$3, (float) this.f$4), (Paint) null);
    }
}
