package android.content.res.iconhelper;

import android.app.ActivityThread;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Binder;
import android.os.Build;
import android.os.UserHandle;
/* loaded from: classes.dex */
public class NtIconHelper {
    public static final int ADAPTIVEICON_FOREGROUND_SIZE_40 = 67;
    public static final int ADAPTIVEICON_FOREGROUND_SIZE_74 = 124;
    public static final int ADAPTIVEICON_STANDARD_SIZE = 182;
    private static final int CIRCLE_SHAPE_ICON = 0;
    private static final int ERROR_SHAPE_ICON = -1;
    private static final int IRREGULAR_SHAPE_ICON = 2;
    private static final int RECTANGLE_SHAPE_ICON = 1;

    private NtIconHelper() {
    }

    public static Drawable makeThemeIcon(Resources res, Drawable srcDrawable, ApplicationInfo appInfo) {
        try {
            if ((srcDrawable instanceof BitmapDrawable) && ActivityThread.currentApplication() != null) {
                if (UserHandle.getAppId(Binder.getCallingUid()) == ActivityThread.currentApplication().getPackageManager().getPackageUid(appInfo.packageName, 0)) {
                    return srcDrawable;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 26 && (srcDrawable instanceof AdaptiveIconDrawable)) {
            return srcDrawable;
        }
        if (srcDrawable instanceof VectorDrawable) {
            return srcDrawable;
        }
        return !(srcDrawable instanceof BitmapDrawable) ? srcDrawable : makeStandardStyleIcon(appInfo.packageName, res, srcDrawable);
    }

    public static Drawable makeStandardStyleIcon(String name, Resources res, Drawable srcDrawable) {
        int foregroundSize;
        int backgroundColor;
        try {
            Bitmap withoutTransparentBitmap = cropTransparentSpace(((BitmapDrawable) srcDrawable).getBitmap(), 0.7f);
            int shapeType = getIconShape(withoutTransparentBitmap);
            if (1 != shapeType) {
                withoutTransparentBitmap = cropTransparentSpace(((BitmapDrawable) srcDrawable).getBitmap(), 0.1f);
            }
            if (1 != shapeType && 2 != shapeType) {
                foregroundSize = 124;
                backgroundColor = 0;
                Drawable foregroundDrawable = new BitmapDrawable(res, addTransparentSpace(res, withoutTransparentBitmap, foregroundSize));
                return new AdaptiveIconDrawable(new ColorDrawable(backgroundColor), foregroundDrawable);
            }
            foregroundSize = 67;
            backgroundColor = -1;
            Drawable foregroundDrawable2 = new BitmapDrawable(res, addTransparentSpace(res, withoutTransparentBitmap, foregroundSize));
            return new AdaptiveIconDrawable(new ColorDrawable(backgroundColor), foregroundDrawable2);
        } catch (Exception e) {
            e.printStackTrace();
            return srcDrawable;
        }
    }

    private static int getIconShape(Bitmap bitmap) {
        if (bitmap == null || bitmap.getWidth() < 1 || bitmap.getHeight() < 1) {
            return -1;
        }
        int[] topPixels = new int[bitmap.getWidth()];
        int[] leftPixels = new int[bitmap.getHeight()];
        int[] rightPixels = new int[bitmap.getHeight()];
        int[] bottomPixels = new int[bitmap.getWidth()];
        bitmap.getPixels(topPixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth() - 1, 1);
        bitmap.getPixels(bottomPixels, 0, bitmap.getWidth(), 0, bitmap.getHeight() - 1, bitmap.getWidth() - 1, 1);
        Bitmap rotationBitmap = adjustPhotoRotation(bitmap, 90);
        rotationBitmap.getPixels(leftPixels, 0, rotationBitmap.getWidth(), 0, 0, rotationBitmap.getWidth() - 1, 1);
        rotationBitmap.getPixels(rightPixels, 0, rotationBitmap.getWidth(), 0, rotationBitmap.getHeight() - 1, rotationBitmap.getWidth() - 1, 1);
        int topCount = getValidCount(topPixels);
        int leftCount = getValidCount(leftPixels);
        int rightCount = getValidCount(rightPixels);
        int bottomCount = getValidCount(bottomPixels);
        if (topCount > topPixels.length / 2 && leftCount > leftPixels.length / 2 && rightCount > rightPixels.length / 2 && bottomCount > bottomPixels.length / 2) {
            return 1;
        }
        if (topCount <= topPixels.length / 2 && leftCount <= leftPixels.length / 2 && rightCount <= rightPixels.length / 2) {
            if (bottomCount <= bottomPixels.length / 2) {
                int s = (int) ((bitmap.getWidth() / 2) * 3.1415925f * (bitmap.getWidth() / 2));
                if (isAllowToAllocateInt(bitmap.getWidth(), bitmap.getHeight())) {
                    return 2;
                }
                int[] imgThePixels = new int[bitmap.getWidth() * bitmap.getHeight()];
                bitmap.getPixels(imgThePixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
                int validCount = getValidCount(imgThePixels);
                int offset = imgThePixels.length / 15;
                return (s - offset >= validCount || validCount >= s + offset) ? 2 : 0;
            }
        }
        return 2;
    }

    private static Bitmap adjustPhotoRotation(Bitmap bm, int orientationDegree) {
        float targetX;
        float targetY;
        Matrix m = new Matrix();
        m.setRotate(orientationDegree, bm.getWidth() / 2.0f, bm.getHeight() / 2.0f);
        if (orientationDegree == 90) {
            targetX = bm.getHeight();
            targetY = 0.0f;
        } else {
            targetX = bm.getHeight();
            targetY = bm.getWidth();
        }
        float[] values = new float[9];
        m.getValues(values);
        float x1 = values[2];
        float y1 = values[5];
        m.postTranslate(targetX - x1, targetY - y1);
        Bitmap bm1 = Bitmap.createBitmap(bm.getHeight(), bm.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bm1);
        canvas.drawBitmap(bm, m, paint);
        return bm1;
    }

    private static int getValidCount(int[] source) {
        int count = 0;
        for (int i : source) {
            if (Color.alpha(i) > 10) {
                count++;
            }
        }
        return count;
    }

    private static boolean isAllowToAllocateInt(int width, int height) {
        return ((float) ((width * height) * 4)) / 1048576.0f > 200.0f;
    }

    public static Bitmap cropTransparentSpace(Bitmap originBitmap, float scale) {
        int top = 0;
        int left = 0;
        int right = 0;
        int bottom = 0;
        for (int h = 0; h < originBitmap.getHeight(); h++) {
            boolean hasValidPix = false;
            int w = 0;
            while (true) {
                if (w < originBitmap.getWidth()) {
                    if (Color.alpha(originBitmap.getPixel(w, h)) <= scale * 255.0f) {
                        w++;
                    } else {
                        hasValidPix = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (hasValidPix) {
                break;
            }
            top++;
        }
        for (int w2 = 0; w2 < originBitmap.getWidth(); w2++) {
            boolean hasValidPix2 = false;
            int h2 = 0;
            while (true) {
                if (h2 < originBitmap.getHeight()) {
                    if (Color.alpha(originBitmap.getPixel(w2, h2)) <= scale * 255.0f) {
                        h2++;
                    } else {
                        hasValidPix2 = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (hasValidPix2) {
                break;
            }
            left++;
        }
        int w3 = originBitmap.getWidth();
        for (int w4 = w3 - 1; w4 >= 0; w4--) {
            boolean hasValidPix3 = false;
            int h3 = 0;
            while (true) {
                if (h3 < originBitmap.getHeight()) {
                    if (Color.alpha(originBitmap.getPixel(w4, h3)) <= scale * 255.0f) {
                        h3++;
                    } else {
                        hasValidPix3 = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (hasValidPix3) {
                break;
            }
            right++;
        }
        int w5 = originBitmap.getHeight();
        for (int h4 = w5 - 1; h4 >= 0; h4--) {
            boolean hasValidPix4 = false;
            int w6 = 0;
            while (true) {
                if (w6 < originBitmap.getWidth()) {
                    if (Color.alpha(originBitmap.getPixel(w6, h4)) <= scale * 255.0f) {
                        w6++;
                    } else {
                        hasValidPix4 = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (hasValidPix4) {
                break;
            }
            bottom++;
        }
        int h5 = originBitmap.getHeight();
        int cropHeight = (h5 - bottom) - top;
        int cropWidth = (originBitmap.getWidth() - left) - right;
        if (cropHeight <= 0 || cropWidth <= 0) {
            return originBitmap;
        }
        return Bitmap.createBitmap(originBitmap, left, top, cropWidth, cropHeight);
    }

    private static Bitmap addTransparentSpace(Resources resources, Bitmap originBitmap, int sizeDp) {
        int zoomHeight;
        int zoomWidth;
        float density = resources.getDisplayMetrics().density;
        int densitySize = (int) (182.0f * density);
        int originWidth = originBitmap.getWidth();
        int originHeight = originBitmap.getHeight();
        if (originWidth >= originHeight) {
            zoomWidth = (int) (sizeDp * density);
            zoomHeight = (int) (((originHeight * 1.0f) / originWidth) * zoomWidth);
        } else {
            zoomHeight = (int) (sizeDp * density);
            zoomWidth = (int) (((originWidth * 1.0f) / originHeight) * zoomHeight);
        }
        Bitmap zoomBitmap = zoomBitmap(originBitmap, zoomWidth, zoomHeight);
        Bitmap bitmap = Bitmap.createBitmap(densitySize, densitySize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        if (zoomBitmap != null && zoomBitmap.getDensity() != bitmap.getDensity()) {
            zoomBitmap.setDensity(bitmap.getDensity());
        }
        canvas.drawBitmap(zoomBitmap, (densitySize - (zoomBitmap.getWidth() * 1.0f)) / 2.0f, (densitySize - (zoomBitmap.getHeight() * 1.0f)) / 2.0f, paint);
        return bitmap;
    }

    private static BitmapDrawable transformVectorDrawableToBitmapDrawable(VectorDrawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return new BitmapDrawable(bitmap);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        try {
            return Bitmap.createScaledBitmap(bitmap, w, h, true);
        } catch (OutOfMemoryError er) {
            er.printStackTrace();
            return bitmap;
        }
    }
}
