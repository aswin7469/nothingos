package com.nothing.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class NTImageUtil {
    public static Drawable setClipOutRect(Context context, Drawable drawable, Rect rect, Rect rect2, boolean z) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        if (z) {
            canvas.scale(-1.0f, 1.0f, (float) (intrinsicWidth / 2), (float) (intrinsicHeight / 2));
        }
        canvas.clipOutRect(rect);
        canvas.clipOutRect(rect2);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return new BitmapDrawable(context.getResources(), createBitmap);
    }
}
