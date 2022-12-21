package com.android.launcher3.icons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;

public interface BitmapRenderer {
    public static final boolean USE_HARDWARE_BITMAP = true;

    void draw(Canvas canvas);

    static Bitmap createSoftwareBitmap(int i, int i2, BitmapRenderer bitmapRenderer) {
        GraphicsUtils.noteNewBitmapCreated();
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        bitmapRenderer.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    static Bitmap createHardwareBitmap(int i, int i2, BitmapRenderer bitmapRenderer) {
        if (!USE_HARDWARE_BITMAP) {
            return createSoftwareBitmap(i, i2, bitmapRenderer);
        }
        GraphicsUtils.noteNewBitmapCreated();
        Picture picture = new Picture();
        bitmapRenderer.draw(picture.beginRecording(i, i2));
        picture.endRecording();
        return Bitmap.createBitmap(picture);
    }

    static Bitmap createBitmap(Bitmap bitmap, int i, int i2, int i3, int i4) {
        if (bitmap.getConfig() == Bitmap.Config.HARDWARE) {
            return createHardwareBitmap(i3, i4, new BitmapRenderer$$ExternalSyntheticLambda0(bitmap, i, i2, i3, i4));
        }
        GraphicsUtils.noteNewBitmapCreated();
        return Bitmap.createBitmap(bitmap, i, i2, i3, i4);
    }
}
