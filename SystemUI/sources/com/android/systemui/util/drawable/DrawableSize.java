package com.android.systemui.util.drawable;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimatedRotateDrawable;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.util.Log;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, mo64987d2 = {"Lcom/android/systemui/util/drawable/DrawableSize;", "", "()V", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DrawableSize.kt */
public final class DrawableSize {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String TAG = "SysUiDrawableSize";

    @JvmStatic
    public static final Drawable downscaleToSize(Resources resources, Drawable drawable, int i, int i2) {
        return Companion.downscaleToSize(resources, drawable, i, i2);
    }

    @Metadata(mo64986d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J,\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u000b2\b\b\u0001\u0010\f\u001a\u00020\u000bH\u0007J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u0006H\u0002J\u0010\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\t\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0010"}, mo64987d2 = {"Lcom/android/systemui/util/drawable/DrawableSize$Companion;", "", "()V", "TAG", "", "downscaleToSize", "Landroid/graphics/drawable/Drawable;", "res", "Landroid/content/res/Resources;", "drawable", "maxWidth", "", "maxHeight", "isAnimated", "", "isSimpleBitmap", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: DrawableSize.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        private final boolean isSimpleBitmap(Drawable drawable) {
            return !drawable.isStateful() && !isAnimated(drawable);
        }

        private final boolean isAnimated(Drawable drawable) {
            if ((drawable instanceof Animatable) || (drawable instanceof Animatable2) || (drawable instanceof AnimatedImageDrawable) || (drawable instanceof AnimatedRotateDrawable) || (drawable instanceof AnimatedStateListDrawable) || (drawable instanceof AnimatedVectorDrawable)) {
                return true;
            }
            return false;
        }

        @JvmStatic
        public final Drawable downscaleToSize(Resources resources, Drawable drawable, int i, int i2) {
            int i3;
            int i4;
            Bitmap bitmap;
            Bitmap bitmap2;
            Bitmap bitmap3;
            Intrinsics.checkNotNullParameter(resources, "res");
            Intrinsics.checkNotNullParameter(drawable, "drawable");
            Trace.beginSection("DrawableSize#downscaleToSize");
            Bitmap.Config config = null;
            BitmapDrawable bitmapDrawable = drawable instanceof BitmapDrawable ? (BitmapDrawable) drawable : null;
            if (bitmapDrawable == null || (bitmap3 = bitmapDrawable.getBitmap()) == null) {
                i3 = drawable.getIntrinsicWidth();
            } else {
                i3 = bitmap3.getWidth();
            }
            BitmapDrawable bitmapDrawable2 = drawable instanceof BitmapDrawable ? (BitmapDrawable) drawable : null;
            if (bitmapDrawable2 == null || (bitmap2 = bitmapDrawable2.getBitmap()) == null) {
                i4 = drawable.getIntrinsicHeight();
            } else {
                i4 = bitmap2.getHeight();
            }
            if (i3 <= 0 || i4 <= 0) {
                Trace.endSection();
                return drawable;
            } else if (i3 >= i || i4 >= i2) {
                try {
                    if (!DrawableSize.Companion.isSimpleBitmap(drawable)) {
                        return drawable;
                    }
                    float f = (float) i3;
                    float f2 = (float) i4;
                    float min = Math.min(((float) i2) / f2, ((float) i) / f);
                    int i5 = (int) (f * min);
                    int i6 = (int) (f2 * min);
                    if (i5 > 0) {
                        if (i6 > 0) {
                            if (Log.isLoggable(DrawableSize.TAG, 3)) {
                                Log.d(DrawableSize.TAG, "Resizing large drawable (" + drawable.getClass().getSimpleName() + ") from " + i3 + " x " + i4 + " to " + i5 + " x " + i6);
                            }
                            BitmapDrawable bitmapDrawable3 = drawable instanceof BitmapDrawable ? (BitmapDrawable) drawable : null;
                            if (!(bitmapDrawable3 == null || (bitmap = bitmapDrawable3.getBitmap()) == null)) {
                                config = bitmap.getConfig();
                            }
                            if (config == null) {
                                config = Bitmap.Config.ARGB_8888;
                            }
                            Bitmap createBitmap = Bitmap.createBitmap(i5, i6, config);
                            Canvas canvas = new Canvas(createBitmap);
                            Rect bounds = drawable.getBounds();
                            drawable.setBounds(0, 0, i5, i6);
                            drawable.draw(canvas);
                            drawable.setBounds(bounds);
                            Drawable bitmapDrawable4 = new BitmapDrawable(resources, createBitmap);
                            Trace.endSection();
                            return bitmapDrawable4;
                        }
                    }
                    Log.w(DrawableSize.TAG, "Attempted to resize " + drawable.getClass().getSimpleName() + " from " + i3 + " x " + i4 + " to invalid " + i5 + " x " + i6 + '.');
                    Trace.endSection();
                    return drawable;
                } finally {
                    Trace.endSection();
                }
            } else {
                if (Log.isLoggable(DrawableSize.TAG, 3)) {
                    Log.d(DrawableSize.TAG, "Not resizing " + i3 + " x " + i4 + " to " + i + " x " + i2);
                }
                Trace.endSection();
                return drawable;
            }
        }
    }
}
