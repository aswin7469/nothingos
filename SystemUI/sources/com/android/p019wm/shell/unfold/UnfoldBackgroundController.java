package com.android.p019wm.shell.unfold;

import android.content.Context;
import android.graphics.Color;
import android.view.SurfaceControl;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.RootTaskDisplayAreaOrganizer;

/* renamed from: com.android.wm.shell.unfold.UnfoldBackgroundController */
public class UnfoldBackgroundController {
    private static final int BACKGROUND_LAYER_Z_INDEX = -1;
    private final float[] mBackgroundColor;
    private SurfaceControl mBackgroundLayer;
    private final RootTaskDisplayAreaOrganizer mRootTaskDisplayAreaOrganizer;

    public UnfoldBackgroundController(Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer) {
        this.mRootTaskDisplayAreaOrganizer = rootTaskDisplayAreaOrganizer;
        this.mBackgroundColor = getBackgroundColor(context);
    }

    public void ensureBackground(SurfaceControl.Transaction transaction) {
        if (this.mBackgroundLayer == null) {
            SurfaceControl.Builder colorLayer = new SurfaceControl.Builder().setName("app-unfold-background").setCallsite("AppUnfoldTransitionController").setColorLayer();
            this.mRootTaskDisplayAreaOrganizer.attachToDisplayArea(0, colorLayer);
            SurfaceControl build = colorLayer.build();
            this.mBackgroundLayer = build;
            transaction.setColor(build, this.mBackgroundColor).show(this.mBackgroundLayer).setLayer(this.mBackgroundLayer, -1);
        }
    }

    public void removeBackground(SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mBackgroundLayer;
        if (surfaceControl != null) {
            if (surfaceControl.isValid()) {
                transaction.remove(this.mBackgroundLayer);
            }
            this.mBackgroundLayer = null;
        }
    }

    private float[] getBackgroundColor(Context context) {
        int color = context.getResources().getColor(C3353R.C3354color.unfold_transition_background);
        return new float[]{((float) Color.red(color)) / 255.0f, ((float) Color.green(color)) / 255.0f, ((float) Color.blue(color)) / 255.0f};
    }
}
