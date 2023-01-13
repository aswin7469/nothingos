package com.android.p019wm.shell.bubbles;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.launcher3.icons.BitmapInfo;
import com.android.launcher3.icons.C1693R;
import com.android.launcher3.icons.ShadowGenerator;
import com.android.p019wm.shell.C3353R;

/* renamed from: com.android.wm.shell.bubbles.BubbleBadgeIconFactory */
public class BubbleBadgeIconFactory extends BaseIconFactory {
    public BubbleBadgeIconFactory(Context context) {
        super(context, context.getResources().getConfiguration().densityDpi, context.getResources().getDimensionPixelSize(C3353R.dimen.bubble_badge_size));
    }

    /* access modifiers changed from: package-private */
    public BitmapInfo getBadgeBitmap(Drawable drawable, boolean z) {
        ShadowGenerator shadowGenerator = new ShadowGenerator(this.mIconBitmapSize);
        Bitmap createIconBitmap = createIconBitmap(drawable, 1.0f, this.mIconBitmapSize);
        if (drawable instanceof AdaptiveIconDrawable) {
            createIconBitmap = Bitmap.createScaledBitmap(getCircleBitmap((AdaptiveIconDrawable) drawable, drawable.getIntrinsicWidth()), this.mIconBitmapSize, this.mIconBitmapSize, true);
        }
        if (z) {
            int color = this.mContext.getResources().getColor(C1693R.C1694color.important_conversation, (Resources.Theme) null);
            Bitmap createBitmap = Bitmap.createBitmap(createIconBitmap.getWidth(), createIconBitmap.getHeight(), createIconBitmap.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setAntiAlias(true);
            canvas.drawCircle((float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2), (float) (canvas.getWidth() / 2), paint);
            int dimensionPixelSize = (int) ((float) this.mContext.getResources().getDimensionPixelSize(17105253));
            int i = dimensionPixelSize * 2;
            float f = (float) dimensionPixelSize;
            canvas.drawBitmap(Bitmap.createScaledBitmap(createIconBitmap, canvas.getWidth() - i, canvas.getHeight() - i, true), f, f, (Paint) null);
            shadowGenerator.recreateIcon(Bitmap.createBitmap(createBitmap), canvas);
            return createIconBitmap(createBitmap);
        }
        Canvas canvas2 = new Canvas();
        canvas2.setBitmap(createIconBitmap);
        shadowGenerator.recreateIcon(Bitmap.createBitmap(createIconBitmap), canvas2);
        return createIconBitmap(createIconBitmap);
    }

    private Bitmap getCircleBitmap(AdaptiveIconDrawable adaptiveIconDrawable, int i) {
        Drawable foreground = adaptiveIconDrawable.getForeground();
        Drawable background = adaptiveIconDrawable.getBackground();
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(createBitmap);
        Path path = new Path();
        float f = ((float) i) / 2.0f;
        path.addCircle(f, f, f, Path.Direction.CW);
        canvas.clipPath(path);
        background.setBounds(0, 0, i, i);
        background.draw(canvas);
        int i2 = i / 5;
        int i3 = -i2;
        int i4 = i + i2;
        foreground.setBounds(i3, i3, i4, i4);
        foreground.draw(canvas);
        canvas.setBitmap((Bitmap) null);
        return createBitmap;
    }
}
