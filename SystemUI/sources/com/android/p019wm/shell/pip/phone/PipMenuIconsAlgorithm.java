package com.android.p019wm.shell.pip.phone;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/* renamed from: com.android.wm.shell.pip.phone.PipMenuIconsAlgorithm */
public class PipMenuIconsAlgorithm {
    private static final String TAG = "PipMenuIconsAlgorithm";
    protected View mDismissButton;
    protected View mDragHandle;
    protected View mEnterSplitButton;
    protected View mSettingsButton;
    protected ViewGroup mTopEndContainer;
    protected ViewGroup mViewRoot;

    public void onBoundsChanged(Rect rect) {
    }

    protected PipMenuIconsAlgorithm(Context context) {
    }

    public void bindViews(ViewGroup viewGroup, ViewGroup viewGroup2, View view, View view2, View view3, View view4) {
        this.mViewRoot = viewGroup;
        this.mTopEndContainer = viewGroup2;
        this.mDragHandle = view;
        this.mEnterSplitButton = view2;
        this.mSettingsButton = view3;
        this.mDismissButton = view4;
    }

    protected static void setLayoutGravity(View view, int i) {
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            layoutParams.gravity = i;
            view.setLayoutParams(layoutParams);
        }
    }
}
