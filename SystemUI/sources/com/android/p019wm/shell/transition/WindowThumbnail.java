package com.android.p019wm.shell.transition;

import android.graphics.ColorSpace;
import android.graphics.GraphicBuffer;
import android.hardware.HardwareBuffer;
import android.view.SurfaceControl;
import android.view.SurfaceSession;

/* renamed from: com.android.wm.shell.transition.WindowThumbnail */
class WindowThumbnail {
    private SurfaceControl mSurfaceControl;

    private WindowThumbnail() {
    }

    static WindowThumbnail createAndAttach(SurfaceSession surfaceSession, SurfaceControl surfaceControl, HardwareBuffer hardwareBuffer, SurfaceControl.Transaction transaction) {
        WindowThumbnail windowThumbnail = new WindowThumbnail();
        windowThumbnail.mSurfaceControl = new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl).setName("WindowThumanil : " + surfaceControl.toString()).setCallsite("WindowThumanil").setFormat(-3).build();
        transaction.setBuffer(windowThumbnail.mSurfaceControl, GraphicBuffer.createFromHardwareBuffer(hardwareBuffer));
        transaction.setColorSpace(windowThumbnail.mSurfaceControl, ColorSpace.get(ColorSpace.Named.SRGB));
        transaction.setLayer(windowThumbnail.mSurfaceControl, Integer.MAX_VALUE);
        transaction.show(windowThumbnail.mSurfaceControl);
        transaction.apply();
        return windowThumbnail;
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl getSurface() {
        return this.mSurfaceControl;
    }

    /* access modifiers changed from: package-private */
    public void destroy(SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
            transaction.apply();
            this.mSurfaceControl.release();
            this.mSurfaceControl = null;
        }
    }
}
