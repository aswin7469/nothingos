package com.android.systemui.statusbar.notification;

import android.graphics.Bitmap;
import androidx.palette.graphics.Palette;

public class MediaNotificationProcessor {
    private static final float BLACK_MAX_LIGHTNESS = 0.08f;
    private static final float POPULATION_FRACTION_FOR_WHITE_OR_BLACK = 2.5f;
    private static final int RESIZE_BITMAP_AREA = 22500;
    private static final float WHITE_MIN_LIGHTNESS = 0.9f;

    private MediaNotificationProcessor() {
    }

    public static Palette.Swatch findBackgroundSwatch(Bitmap bitmap) {
        return findBackgroundSwatch(generateArtworkPaletteBuilder(bitmap).generate());
    }

    public static Palette.Swatch findBackgroundSwatch(Palette palette) {
        Palette.Swatch dominantSwatch = palette.getDominantSwatch();
        if (dominantSwatch == null) {
            return new Palette.Swatch(-1, 100);
        }
        if (!isWhiteOrBlack(dominantSwatch.getHsl())) {
            return dominantSwatch;
        }
        float f = -1.0f;
        Palette.Swatch swatch = null;
        for (Palette.Swatch next : palette.getSwatches()) {
            if (next != dominantSwatch && ((float) next.getPopulation()) > f && !isWhiteOrBlack(next.getHsl())) {
                f = (float) next.getPopulation();
                swatch = next;
            }
        }
        return (swatch != null && ((float) dominantSwatch.getPopulation()) / f <= POPULATION_FRACTION_FOR_WHITE_OR_BLACK) ? swatch : dominantSwatch;
    }

    public static Palette.Builder generateArtworkPaletteBuilder(Bitmap bitmap) {
        return Palette.from(bitmap).setRegion(0, 0, bitmap.getWidth() / 2, bitmap.getHeight()).clearFilters().resizeBitmapArea(RESIZE_BITMAP_AREA);
    }

    private static boolean isWhiteOrBlack(float[] fArr) {
        return isBlack(fArr) || isWhite(fArr);
    }

    private static boolean isBlack(float[] fArr) {
        return fArr[2] <= BLACK_MAX_LIGHTNESS;
    }

    private static boolean isWhite(float[] fArr) {
        return fArr[2] >= 0.9f;
    }
}
