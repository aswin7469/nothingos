package com.android.launcher3.icons;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public class IconNormalizer {
    private final Bitmap mBitmap;
    private final Canvas mCanvas;
    private boolean mEnableShapeDetection;
    private final float[] mLeftBorder;
    private final int mMaxSize;
    private final Paint mPaintMaskShape;
    private final Paint mPaintMaskShapeOutline;
    private final byte[] mPixels;
    private final float[] mRightBorder;
    private final Rect mBounds = new Rect();
    private final RectF mAdaptiveIconBounds = new RectF();
    private final Path mShapePath = new Path();
    private final Matrix mMatrix = new Matrix();
    private float mAdaptiveIconScale = 0.0f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public IconNormalizer(Context context, int i, boolean z) {
        int i2 = i * 2;
        this.mMaxSize = i2;
        Bitmap createBitmap = Bitmap.createBitmap(i2, i2, Bitmap.Config.ALPHA_8);
        this.mBitmap = createBitmap;
        this.mCanvas = new Canvas(createBitmap);
        this.mPixels = new byte[i2 * i2];
        this.mLeftBorder = new float[i2];
        this.mRightBorder = new float[i2];
        Paint paint = new Paint();
        this.mPaintMaskShape = paint;
        paint.setColor(-65536);
        paint.setStyle(Paint.Style.FILL);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.XOR));
        Paint paint2 = new Paint();
        this.mPaintMaskShapeOutline = paint2;
        paint2.setStrokeWidth(context.getResources().getDisplayMetrics().density * 2.0f);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(-16777216);
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mEnableShapeDetection = z;
    }

    private static float getScale(float f, float f2, float f3) {
        float f4 = f / f2;
        float f5 = f4 < 0.7853982f ? 0.6597222f : ((1.0f - f4) * 0.040449437f) + 0.6510417f;
        float f6 = f / f3;
        if (f6 > f5) {
            return (float) Math.sqrt(f5 / f6);
        }
        return 1.0f;
    }

    @TargetApi(26)
    public static float normalizeAdaptiveIcon(Drawable drawable, int i, RectF rectF) {
        Rect rect = new Rect(drawable.getBounds());
        drawable.setBounds(0, 0, i, i);
        Path iconMask = ((AdaptiveIconDrawable) drawable).getIconMask();
        Region region = new Region();
        region.setPath(iconMask, new Region(0, 0, i, i));
        Rect bounds = region.getBounds();
        int area = GraphicsUtils.getArea(region);
        if (rectF != null) {
            float f = i;
            rectF.set(bounds.left / f, bounds.top / f, 1.0f - (bounds.right / f), 1.0f - (bounds.bottom / f));
        }
        drawable.setBounds(rect);
        float f2 = area;
        return getScale(f2, f2, i * i);
    }

    private boolean isShape(Path path) {
        if (Math.abs((this.mBounds.width() / this.mBounds.height()) - 1.0f) > 0.05f) {
            return false;
        }
        this.mMatrix.reset();
        this.mMatrix.setScale(this.mBounds.width(), this.mBounds.height());
        Matrix matrix = this.mMatrix;
        Rect rect = this.mBounds;
        matrix.postTranslate(rect.left, rect.top);
        path.transform(this.mMatrix, this.mShapePath);
        this.mCanvas.drawPath(this.mShapePath, this.mPaintMaskShape);
        this.mCanvas.drawPath(this.mShapePath, this.mPaintMaskShapeOutline);
        return isTransparentBitmap();
    }

    private boolean isTransparentBitmap() {
        Rect rect;
        ByteBuffer wrap = ByteBuffer.wrap(this.mPixels);
        wrap.rewind();
        this.mBitmap.copyPixelsToBuffer(wrap);
        Rect rect2 = this.mBounds;
        int i = rect2.top;
        int i2 = this.mMaxSize;
        int i3 = i * i2;
        int i4 = i2 - rect2.right;
        int i5 = 0;
        while (true) {
            rect = this.mBounds;
            if (i >= rect.bottom) {
                break;
            }
            int i6 = rect.left;
            int i7 = i3 + i6;
            while (i6 < this.mBounds.right) {
                if ((this.mPixels[i7] & 255) > 40) {
                    i5++;
                }
                i7++;
                i6++;
            }
            i3 = i7 + i4;
            i++;
        }
        return ((float) i5) / ((float) (rect.width() * this.mBounds.height())) < 0.005f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:79:0x0050, code lost:
        if (r4 <= r16.mMaxSize) goto L80;
     */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00d8 A[Catch: all -> 0x012f, TryCatch #0 {, blocks: (B:4:0x0009, B:6:0x000e, B:8:0x0012, B:10:0x0018, B:12:0x0024, B:13:0x0029, B:17:0x002d, B:21:0x003a, B:24:0x005c, B:28:0x0089, B:35:0x0098, B:38:0x009f, B:42:0x00b0, B:44:0x00ba, B:51:0x00c9, B:53:0x00d8, B:57:0x00ec, B:58:0x00e3, B:61:0x00ef, B:63:0x00fb, B:66:0x010f, B:68:0x0113, B:70:0x0116, B:71:0x011f, B:76:0x0040, B:78:0x004e, B:81:0x0056, B:83:0x005a, B:84:0x0052), top: B:3:0x0009 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00fb A[Catch: all -> 0x012f, TryCatch #0 {, blocks: (B:4:0x0009, B:6:0x000e, B:8:0x0012, B:10:0x0018, B:12:0x0024, B:13:0x0029, B:17:0x002d, B:21:0x003a, B:24:0x005c, B:28:0x0089, B:35:0x0098, B:38:0x009f, B:42:0x00b0, B:44:0x00ba, B:51:0x00c9, B:53:0x00d8, B:57:0x00ec, B:58:0x00e3, B:61:0x00ef, B:63:0x00fb, B:66:0x010f, B:68:0x0113, B:70:0x0116, B:71:0x011f, B:76:0x0040, B:78:0x004e, B:81:0x0056, B:83:0x005a, B:84:0x0052), top: B:3:0x0009 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized float getScale(Drawable drawable, RectF rectF, Path path, boolean[] zArr) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (BaseIconFactory.ATLEAST_OREO && (drawable instanceof AdaptiveIconDrawable)) {
            if (this.mAdaptiveIconScale == 0.0f) {
                this.mAdaptiveIconScale = normalizeAdaptiveIcon(drawable, this.mMaxSize, this.mAdaptiveIconBounds);
            }
            if (rectF != null) {
                rectF.set(this.mAdaptiveIconBounds);
            }
            return this.mAdaptiveIconScale;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth > 0 && intrinsicHeight > 0) {
            int i7 = this.mMaxSize;
            if (intrinsicWidth > i7 || intrinsicHeight > i7) {
                int max = Math.max(intrinsicWidth, intrinsicHeight);
                int i8 = this.mMaxSize;
                intrinsicWidth = (intrinsicWidth * i8) / max;
                intrinsicHeight = (i8 * intrinsicHeight) / max;
            }
            int i9 = 0;
            this.mBitmap.eraseColor(0);
            drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
            drawable.draw(this.mCanvas);
            ByteBuffer wrap = ByteBuffer.wrap(this.mPixels);
            wrap.rewind();
            this.mBitmap.copyPixelsToBuffer(wrap);
            int i10 = this.mMaxSize;
            i = i10 + 1;
            int i11 = i10 - intrinsicWidth;
            i2 = 0;
            int i12 = 0;
            i3 = -1;
            i4 = -1;
            i5 = -1;
            while (i2 < intrinsicHeight) {
                int i13 = -1;
                int i14 = -1;
                for (int i15 = i9; i15 < intrinsicWidth; i15++) {
                    if ((this.mPixels[i12] & 255) > 40) {
                        if (i13 == -1) {
                            i13 = i15;
                        }
                        i14 = i15;
                    }
                    i12++;
                }
                i12 += i11;
                this.mLeftBorder[i2] = i13;
                this.mRightBorder[i2] = i14;
                if (i13 != -1) {
                    if (i3 == -1) {
                        i3 = i2;
                    }
                    int min = Math.min(i, i13);
                    i4 = Math.max(i4, i14);
                    i = min;
                    i5 = i2;
                }
                i2++;
                i9 = 0;
            }
            if (i3 != -1 && i4 != -1) {
                convertToConvexArray(this.mLeftBorder, 1, i3, i5);
                convertToConvexArray(this.mRightBorder, -1, i3, i5);
                float f = 0.0f;
                for (i6 = 0; i6 < intrinsicHeight; i6++) {
                    float[] fArr = this.mLeftBorder;
                    if (fArr[i6] > -1.0f) {
                        f += (this.mRightBorder[i6] - fArr[i6]) + 1.0f;
                    }
                }
                Rect rect = this.mBounds;
                rect.left = i;
                rect.right = i4;
                rect.top = i3;
                rect.bottom = i5;
                if (rectF != null) {
                    float f2 = intrinsicWidth;
                    float f3 = intrinsicHeight;
                    rectF.set(i / f2, i3 / f3, 1.0f - (i4 / f2), 1.0f - (i5 / f3));
                }
                if (zArr != null && this.mEnableShapeDetection && zArr.length > 0) {
                    zArr[0] = isShape(path);
                }
                return getScale(f, ((i5 + 1) - i3) * ((i4 + 1) - i), intrinsicWidth * intrinsicHeight);
            }
            return 1.0f;
        }
        intrinsicWidth = this.mMaxSize;
        if (intrinsicHeight <= 0 || intrinsicHeight > this.mMaxSize) {
            intrinsicHeight = this.mMaxSize;
        }
        int i92 = 0;
        this.mBitmap.eraseColor(0);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(this.mCanvas);
        ByteBuffer wrap2 = ByteBuffer.wrap(this.mPixels);
        wrap2.rewind();
        this.mBitmap.copyPixelsToBuffer(wrap2);
        int i102 = this.mMaxSize;
        i = i102 + 1;
        int i112 = i102 - intrinsicWidth;
        i2 = 0;
        int i122 = 0;
        i3 = -1;
        i4 = -1;
        i5 = -1;
        while (i2 < intrinsicHeight) {
        }
        if (i3 != -1) {
            convertToConvexArray(this.mLeftBorder, 1, i3, i5);
            convertToConvexArray(this.mRightBorder, -1, i3, i5);
            float f4 = 0.0f;
            while (i6 < intrinsicHeight) {
            }
            Rect rect2 = this.mBounds;
            rect2.left = i;
            rect2.right = i4;
            rect2.top = i3;
            rect2.bottom = i5;
            if (rectF != null) {
            }
            if (zArr != null) {
                zArr[0] = isShape(path);
            }
            return getScale(f4, ((i5 + 1) - i3) * ((i4 + 1) - i), intrinsicWidth * intrinsicHeight);
        }
        return 1.0f;
    }

    private static void convertToConvexArray(float[] fArr, int i, int i2, int i3) {
        float[] fArr2 = new float[fArr.length - 1];
        int i4 = -1;
        float f = Float.MAX_VALUE;
        for (int i5 = i2 + 1; i5 <= i3; i5++) {
            if (fArr[i5] > -1.0f) {
                if (f == Float.MAX_VALUE) {
                    i4 = i2;
                } else {
                    float f2 = ((fArr[i5] - fArr[i4]) / (i5 - i4)) - f;
                    float f3 = i;
                    if (f2 * f3 < 0.0f) {
                        while (i4 > i2) {
                            i4--;
                            if ((((fArr[i5] - fArr[i4]) / (i5 - i4)) - fArr2[i4]) * f3 >= 0.0f) {
                                break;
                            }
                        }
                    }
                }
                f = (fArr[i5] - fArr[i4]) / (i5 - i4);
                for (int i6 = i4; i6 < i5; i6++) {
                    fArr2[i6] = f;
                    fArr[i6] = fArr[i4] + ((i6 - i4) * f);
                }
                i4 = i5;
            }
        }
    }

    public static int getNormalizedCircleSize(int i) {
        return (int) Math.round(Math.sqrt((((i * i) * 0.6597222f) * 4.0f) / 3.141592653589793d));
    }
}
