package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Debug;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import com.android.systemui.statusbar.phone.PanelViewController;
import com.nothing.systemui.util.NTLogUtil;

public abstract class PanelView extends FrameLayout {
    public static final boolean DEBUG = false;
    public static final String TAG = "PanelView";
    protected CentralSurfaces mCentralSurfaces;
    protected HeadsUpManagerPhone mHeadsUpManager;
    protected KeyguardBottomAreaView mKeyguardBottomArea;
    private OnConfigurationChangedListener mOnConfigurationChangedListener;
    private PanelViewController.TouchHandler mTouchHandler;

    interface OnConfigurationChangedListener {
        void onConfigurationChanged(Configuration configuration);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public PanelView(Context context) {
        super(context);
    }

    public PanelView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PanelView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PanelView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setOnTouchListener(PanelViewController.TouchHandler touchHandler) {
        super.setOnTouchListener(touchHandler);
        this.mTouchHandler = touchHandler;
    }

    public void setOnConfigurationChangedListener(OnConfigurationChangedListener onConfigurationChangedListener) {
        this.mOnConfigurationChangedListener = onConfigurationChangedListener;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mTouchHandler.onInterceptTouchEvent(motionEvent);
    }

    public void dispatchConfigurationChanged(Configuration configuration) {
        super.dispatchConfigurationChanged(configuration);
        this.mOnConfigurationChangedListener.onConfigurationChanged(configuration);
    }

    public void setAlpha(float f) {
        NTLogUtil.m1686d(TAG, "setAlpha stacks: alpha: " + f + ", cb: " + Debug.getCallers(6));
        super.setAlpha(f);
    }
}
