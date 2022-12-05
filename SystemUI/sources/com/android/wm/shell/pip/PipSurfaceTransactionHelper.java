package com.android.wm.shell.pip;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.SurfaceControl;
import com.android.wm.shell.R;
/* loaded from: classes2.dex */
public class PipSurfaceTransactionHelper {
    private int mCornerRadius;
    private final Matrix mTmpTransform = new Matrix();
    private final float[] mTmpFloat9 = new float[9];
    private final RectF mTmpSourceRectF = new RectF();
    private final RectF mTmpDestinationRectF = new RectF();
    private final Rect mTmpDestinationRect = new Rect();

    /* loaded from: classes2.dex */
    public interface SurfaceControlTransactionFactory {
        SurfaceControl.Transaction getTransaction();
    }

    public void onDensityOrFontScaleChanged(Context context) {
        this.mCornerRadius = context.getResources().getDimensionPixelSize(R.dimen.pip_corner_radius);
    }

    public PipSurfaceTransactionHelper alpha(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f) {
        transaction.setAlpha(surfaceControl, f);
        return this;
    }

    public PipSurfaceTransactionHelper crop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect) {
        transaction.setWindowCrop(surfaceControl, rect.width(), rect.height()).setPosition(surfaceControl, rect.left, rect.top);
        return this;
    }

    public PipSurfaceTransactionHelper scale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        return scale(transaction, surfaceControl, rect, rect2, 0.0f);
    }

    public PipSurfaceTransactionHelper scale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, float f) {
        this.mTmpSourceRectF.set(rect);
        this.mTmpSourceRectF.offsetTo(0.0f, 0.0f);
        this.mTmpDestinationRectF.set(rect2);
        this.mTmpTransform.setRectToRect(this.mTmpSourceRectF, this.mTmpDestinationRectF, Matrix.ScaleToFit.FILL);
        this.mTmpTransform.postRotate(f, this.mTmpDestinationRectF.centerX(), this.mTmpDestinationRectF.centerY());
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9);
        return this;
    }

    public PipSurfaceTransactionHelper scaleAndCrop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3) {
        float height;
        int height2;
        this.mTmpSourceRectF.set(rect);
        this.mTmpDestinationRect.set(rect);
        this.mTmpDestinationRect.inset(rect3);
        if (rect.width() <= rect.height()) {
            height = rect2.width();
            height2 = rect.width();
        } else {
            height = rect2.height();
            height2 = rect.height();
        }
        float f = height / height2;
        this.mTmpTransform.setScale(f, f);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setWindowCrop(surfaceControl, this.mTmpDestinationRect).setPosition(surfaceControl, rect2.left - (rect3.left * f), rect2.top - (rect3.top * f));
        return this;
    }

    public PipSurfaceTransactionHelper rotateAndScaleWithCrop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2, Rect rect3, float f, float f2, float f3, boolean z, boolean z2) {
        float f4;
        int i;
        float f5;
        this.mTmpDestinationRect.set(rect);
        this.mTmpDestinationRect.inset(rect3);
        int width = this.mTmpDestinationRect.width();
        int height = this.mTmpDestinationRect.height();
        int width2 = rect2.width();
        int height2 = rect2.height();
        float f6 = width <= height ? width2 / width : height2 / height;
        Rect rect4 = this.mTmpDestinationRect;
        rect4.set(0, 0, width2, height2);
        rect4.scale(1.0f / f6);
        rect4.offset(rect3.left, rect3.top);
        if (z) {
            f4 = f2 - (rect3.left * f6);
            i = rect3.top;
        } else if (z2) {
            f4 = f2 - (rect3.top * f6);
            f5 = f3 + (rect3.left * f6);
            this.mTmpTransform.setScale(f6, f6);
            this.mTmpTransform.postRotate(f);
            this.mTmpTransform.postTranslate(f4, f5);
            transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setWindowCrop(surfaceControl, rect4);
            return this;
        } else {
            f4 = f2 + (rect3.top * f6);
            i = rect3.left;
        }
        f5 = f3 - (i * f6);
        this.mTmpTransform.setScale(f6, f6);
        this.mTmpTransform.postRotate(f);
        this.mTmpTransform.postTranslate(f4, f5);
        transaction.setMatrix(surfaceControl, this.mTmpTransform, this.mTmpFloat9).setWindowCrop(surfaceControl, rect4);
        return this;
    }

    public PipSurfaceTransactionHelper resetScale(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect) {
        transaction.setMatrix(surfaceControl, Matrix.IDENTITY_MATRIX, this.mTmpFloat9).setPosition(surfaceControl, rect.left, rect.top);
        return this;
    }

    public PipSurfaceTransactionHelper round(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, boolean z) {
        transaction.setCornerRadius(surfaceControl, z ? this.mCornerRadius : 0.0f);
        return this;
    }

    public PipSurfaceTransactionHelper round(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect, Rect rect2) {
        transaction.setCornerRadius(surfaceControl, this.mCornerRadius * ((float) (Math.hypot(rect.width(), rect.height()) / Math.hypot(rect2.width(), rect2.height()))));
        return this;
    }
}
