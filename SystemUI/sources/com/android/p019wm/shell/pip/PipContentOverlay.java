package com.android.p019wm.shell.pip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.window.TaskSnapshot;

/* renamed from: com.android.wm.shell.pip.PipContentOverlay */
public abstract class PipContentOverlay {
    protected SurfaceControl mLeash;

    public abstract void attach(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl);

    public abstract void onAnimationEnd(SurfaceControl.Transaction transaction, Rect rect);

    public abstract void onAnimationUpdate(SurfaceControl.Transaction transaction, float f);

    public void detach(SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl != null && surfaceControl.isValid()) {
            transaction.remove(this.mLeash);
            transaction.apply();
        }
    }

    /* renamed from: com.android.wm.shell.pip.PipContentOverlay$PipColorOverlay */
    public static final class PipColorOverlay extends PipContentOverlay {
        private final Context mContext;

        public void onAnimationEnd(SurfaceControl.Transaction transaction, Rect rect) {
        }

        public PipColorOverlay(Context context) {
            this.mContext = context;
            this.mLeash = new SurfaceControl.Builder(new SurfaceSession()).setCallsite("PipAnimation").setName(PipColorOverlay.class.getSimpleName()).setColorLayer().build();
        }

        public void attach(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
            transaction.show(this.mLeash);
            transaction.setLayer(this.mLeash, Integer.MAX_VALUE);
            transaction.setColor(this.mLeash, getContentOverlayColor(this.mContext));
            transaction.setAlpha(this.mLeash, 0.0f);
            transaction.reparent(this.mLeash, surfaceControl);
            transaction.apply();
        }

        public void onAnimationUpdate(SurfaceControl.Transaction transaction, float f) {
            transaction.setAlpha(this.mLeash, f < 0.5f ? 0.0f : (f - 0.5f) * 2.0f);
        }

        private float[] getContentOverlayColor(Context context) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16842801});
            try {
                int color = obtainStyledAttributes.getColor(0, 0);
                return new float[]{((float) Color.red(color)) / 255.0f, ((float) Color.green(color)) / 255.0f, ((float) Color.blue(color)) / 255.0f};
            } finally {
                obtainStyledAttributes.recycle();
            }
        }
    }

    /* renamed from: com.android.wm.shell.pip.PipContentOverlay$PipSnapshotOverlay */
    public static final class PipSnapshotOverlay extends PipContentOverlay {
        private final TaskSnapshot mSnapshot;
        private final Rect mSourceRectHint;
        private float mTaskSnapshotScaleX;
        private float mTaskSnapshotScaleY;

        public void onAnimationUpdate(SurfaceControl.Transaction transaction, float f) {
        }

        public PipSnapshotOverlay(TaskSnapshot taskSnapshot, Rect rect) {
            this.mSnapshot = taskSnapshot;
            this.mSourceRectHint = new Rect(rect);
            this.mLeash = new SurfaceControl.Builder(new SurfaceSession()).setCallsite("PipAnimation").setName(PipSnapshotOverlay.class.getSimpleName()).build();
        }

        public void attach(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
            this.mTaskSnapshotScaleX = ((float) this.mSnapshot.getTaskSize().x) / ((float) this.mSnapshot.getHardwareBuffer().getWidth());
            this.mTaskSnapshotScaleY = ((float) this.mSnapshot.getTaskSize().y) / ((float) this.mSnapshot.getHardwareBuffer().getHeight());
            transaction.show(this.mLeash);
            transaction.setLayer(this.mLeash, Integer.MAX_VALUE);
            transaction.setBuffer(this.mLeash, this.mSnapshot.getHardwareBuffer());
            transaction.setPosition(this.mLeash, (float) (-this.mSourceRectHint.left), (float) (-this.mSourceRectHint.top));
            transaction.setScale(this.mLeash, this.mTaskSnapshotScaleX, this.mTaskSnapshotScaleY);
            transaction.reparent(this.mLeash, surfaceControl);
            transaction.apply();
        }

        public void onAnimationEnd(SurfaceControl.Transaction transaction, Rect rect) {
            SurfaceControl.Transaction transaction2 = new SurfaceControl.Transaction();
            float max = Math.max((((float) rect.width()) / ((float) this.mSourceRectHint.width())) * this.mTaskSnapshotScaleX, (((float) rect.height()) / ((float) this.mSourceRectHint.height())) * this.mTaskSnapshotScaleY);
            transaction2.setScale(this.mLeash, max, max);
            float f = -max;
            transaction2.setPosition(this.mLeash, (((float) this.mSourceRectHint.left) * f) / this.mTaskSnapshotScaleX, (f * ((float) this.mSourceRectHint.top)) / this.mTaskSnapshotScaleY);
            transaction2.apply();
            transaction.remove(this.mLeash);
        }
    }
}
