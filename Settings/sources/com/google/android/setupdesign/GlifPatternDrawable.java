package com.google.android.setupdesign;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import java.lang.ref.SoftReference;
/* loaded from: classes2.dex */
public class GlifPatternDrawable extends Drawable {
    @SuppressLint({"InlinedApi"})
    private static final int[] ATTRS_PRIMARY_COLOR = {16843827};
    private static SoftReference<Bitmap> bitmapCache;
    private static int[] patternLightness;
    private static Path[] patternPaths;
    private int color;
    private final Paint tempPaint = new Paint(1);

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public static void invalidatePattern() {
        bitmapCache = null;
    }

    public GlifPatternDrawable(int i) {
        setColor(i);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0035, code lost:
        if (r6 < 1152.0f) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x002b, code lost:
        if (r5 < 2049.0f) goto L9;
     */
    @Override // android.graphics.drawable.Drawable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        int width = bounds.width();
        int height = bounds.height();
        SoftReference<Bitmap> softReference = bitmapCache;
        Bitmap bitmap = null;
        Bitmap bitmap2 = softReference != null ? softReference.get() : null;
        if (bitmap2 != null) {
            int width2 = bitmap2.getWidth();
            int height2 = bitmap2.getHeight();
            if (width > width2) {
            }
            if (height > height2) {
            }
        }
        bitmap = bitmap2;
        if (bitmap == null) {
            this.tempPaint.reset();
            bitmap = createBitmapCache(width, height);
            bitmapCache = new SoftReference<>(bitmap);
            this.tempPaint.reset();
        }
        canvas.save();
        canvas.clipRect(bounds);
        scaleCanvasToBounds(canvas, bitmap, bounds);
        canvas.drawColor(-16777216);
        this.tempPaint.setColor(-1);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.tempPaint);
        canvas.drawColor(this.color);
        canvas.restore();
    }

    public Bitmap createBitmapCache(int i, int i2) {
        float min = Math.min(1.5f, Math.max(i / 1366.0f, i2 / 768.0f));
        Bitmap createBitmap = Bitmap.createBitmap((int) (1366.0f * min), (int) (768.0f * min), Bitmap.Config.ALPHA_8);
        renderOnCanvas(new Canvas(createBitmap), min);
        return createBitmap;
    }

    private void renderOnCanvas(Canvas canvas, float f) {
        canvas.save();
        canvas.scale(f, f);
        this.tempPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        if (patternPaths == null) {
            Path[] pathArr = new Path[7];
            patternPaths = pathArr;
            patternLightness = new int[]{10, 40, 51, 66, 91, 112, 130};
            Path path = new Path();
            pathArr[0] = path;
            path.moveTo(1029.4f, 357.5f);
            path.lineTo(1366.0f, 759.1f);
            path.lineTo(1366.0f, 0.0f);
            path.lineTo(1137.7f, 0.0f);
            path.close();
            Path[] pathArr2 = patternPaths;
            Path path2 = new Path();
            pathArr2[1] = path2;
            path2.moveTo(1138.1f, 0.0f);
            path2.rLineTo(-144.8f, 768.0f);
            path2.rLineTo(372.7f, 0.0f);
            path2.rLineTo(0.0f, -524.0f);
            path2.cubicTo(1290.7f, 121.6f, 1219.2f, 41.1f, 1178.7f, 0.0f);
            path2.close();
            Path[] pathArr3 = patternPaths;
            Path path3 = new Path();
            pathArr3[2] = path3;
            path3.moveTo(949.8f, 768.0f);
            path3.rCubicTo(92.6f, -170.6f, 213.0f, -440.3f, 269.4f, -768.0f);
            path3.lineTo(585.0f, 0.0f);
            path3.rLineTo(2.1f, 766.0f);
            path3.close();
            Path[] pathArr4 = patternPaths;
            Path path4 = new Path();
            pathArr4[3] = path4;
            path4.moveTo(471.1f, 768.0f);
            path4.rMoveTo(704.5f, 0.0f);
            path4.cubicTo(1123.6f, 563.3f, 1027.4f, 275.2f, 856.2f, 0.0f);
            path4.lineTo(476.4f, 0.0f);
            path4.rLineTo(-5.3f, 768.0f);
            path4.close();
            Path[] pathArr5 = patternPaths;
            Path path5 = new Path();
            pathArr5[4] = path5;
            path5.moveTo(323.1f, 768.0f);
            path5.moveTo(777.5f, 768.0f);
            path5.cubicTo(661.9f, 348.8f, 427.2f, 21.4f, 401.2f, 25.4f);
            path5.lineTo(323.1f, 768.0f);
            path5.close();
            Path[] pathArr6 = patternPaths;
            Path path6 = new Path();
            pathArr6[5] = path6;
            path6.moveTo(178.44286f, 766.8571f);
            path6.lineTo(308.7f, 768.0f);
            path6.cubicTo(381.7f, 604.6f, 481.6f, 344.3f, 562.2f, 0.0f);
            path6.lineTo(0.0f, 0.0f);
            path6.close();
            Path[] pathArr7 = patternPaths;
            Path path7 = new Path();
            pathArr7[6] = path7;
            path7.moveTo(146.0f, 0.0f);
            path7.lineTo(0.0f, 0.0f);
            path7.lineTo(0.0f, 768.0f);
            path7.lineTo(394.2f, 768.0f);
            path7.cubicTo(327.7f, 475.3f, 228.5f, 201.0f, 146.0f, 0.0f);
            path7.close();
        }
        for (int i = 0; i < 7; i++) {
            this.tempPaint.setColor(patternLightness[i] << 24);
            canvas.drawPath(patternPaths[i], this.tempPaint);
        }
        canvas.restore();
        this.tempPaint.reset();
    }

    public void scaleCanvasToBounds(Canvas canvas, Bitmap bitmap, Rect rect) {
        int width = bitmap.getWidth();
        float f = width;
        float width2 = rect.width() / f;
        float height = bitmap.getHeight();
        float height2 = rect.height() / height;
        canvas.scale(width2, height2);
        if (height2 > width2) {
            canvas.scale(height2 / width2, 1.0f, f * 0.146f, 0.0f);
        } else if (width2 <= height2) {
        } else {
            canvas.scale(1.0f, width2 / height2, 0.0f, height * 0.228f);
        }
    }

    public void setColor(int i) {
        this.color = Color.argb(204, Color.red(i), Color.green(i), Color.blue(i));
        invalidateSelf();
    }
}
