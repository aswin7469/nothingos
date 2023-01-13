package com.android.settingslib.widget;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.DrawableWrapper;
import android.os.RemoteException;
import android.util.PathParser;
import android.view.WindowManagerGlobal;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AdaptiveOutlineDrawable extends DrawableWrapper {
    private static final float ADVANCED_ICON_CENTER = 50.0f;
    private static final float ADVANCED_ICON_RADIUS = 48.0f;
    public static final int ICON_TYPE_ADVANCED = 1;
    public static final int ICON_TYPE_DEFAULT = 0;
    private Bitmap mBitmap;
    private int mInsetPx;
    Paint mOutlinePaint;
    private Path mPath;
    private int mStrokeWidth;
    private int mType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AdaptiveOutlineIconType {
    }

    public AdaptiveOutlineDrawable(Resources resources, Bitmap bitmap) {
        super(new AdaptiveIconShapeDrawable(resources));
        init(resources, bitmap, 0);
    }

    public AdaptiveOutlineDrawable(Resources resources, Bitmap bitmap, int i) {
        super(new AdaptiveIconShapeDrawable(resources));
        init(resources, bitmap, i);
    }

    private void init(Resources resources, Bitmap bitmap, int i) {
        this.mType = i;
        getDrawable().setTint(-1);
        this.mPath = new Path(PathParser.createPathFromPathData(resources.getString(17039987)));
        this.mStrokeWidth = resources.getDimensionPixelSize(C1861R.dimen.adaptive_outline_stroke);
        Paint paint = new Paint();
        this.mOutlinePaint = paint;
        paint.setColor(getColor(resources, i));
        this.mOutlinePaint.setStyle(Paint.Style.STROKE);
        this.mOutlinePaint.setStrokeWidth((float) this.mStrokeWidth);
        this.mOutlinePaint.setAntiAlias(true);
        this.mInsetPx = getDimensionPixelSize(resources, i);
        this.mBitmap = bitmap;
    }

    private int getColor(Resources resources, int i) {
        int i2;
        if (i != 1) {
            i2 = C1861R.C1862color.bt_outline_color;
        } else {
            i2 = C1861R.C1862color.advanced_outline_color;
        }
        return resources.getColor(i2, (Resources.Theme) null);
    }

    private int getDimensionPixelSize(Resources resources, int i) {
        int i2;
        if (i != 1) {
            i2 = C1861R.dimen.dashboard_tile_foreground_image_inset;
        } else {
            i2 = C1861R.dimen.advanced_dashboard_tile_foreground_image_inset;
        }
        return resources.getDimensionPixelSize(i2);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect bounds = getBounds();
        int save = canvas.save();
        canvas.scale(((float) (bounds.right - bounds.left)) / 100.0f, ((float) (bounds.bottom - bounds.top)) / 100.0f);
        if (this.mType == 0) {
            canvas.drawPath(this.mPath, this.mOutlinePaint);
        } else {
            canvas.drawCircle(50.0f, 50.0f, 48.0f, this.mOutlinePaint);
        }
        canvas.restoreToCount(save);
        canvas.drawBitmap(this.mBitmap, (float) (bounds.left + this.mInsetPx), (float) (bounds.top + this.mInsetPx), (Paint) null);
    }

    private static int getDefaultDisplayDensity(int i) {
        try {
            return WindowManagerGlobal.getWindowManagerService().getInitialDisplayDensity(i);
        } catch (RemoteException unused) {
            return -1;
        }
    }

    public int getIntrinsicHeight() {
        return this.mBitmap.getHeight() + (this.mInsetPx * 2);
    }

    public int getIntrinsicWidth() {
        return this.mBitmap.getWidth() + (this.mInsetPx * 2);
    }
}
