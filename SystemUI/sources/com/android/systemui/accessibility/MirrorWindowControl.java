package com.android.systemui.accessibility;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import com.android.systemui.C1894R;

public abstract class MirrorWindowControl {
    private static final boolean DBG = (Log.isLoggable(TAG, 3) | false);
    private static final String TAG = "MirrorWindowControl";
    protected final Context mContext;
    private final Point mControlPosition = new Point();
    protected View mControlsView;
    private final Rect mDraggableBound = new Rect();
    protected MirrorWindowDelegate mMirrorWindowDelegate;
    final Point mTmpPoint = new Point();
    private final WindowManager mWindowManager;

    public interface MirrorWindowDelegate {
        void move(int i, int i2);
    }

    /* access modifiers changed from: package-private */
    public abstract String getWindowTitle();

    /* access modifiers changed from: package-private */
    public abstract View onCreateView(LayoutInflater layoutInflater, Point point);

    MirrorWindowControl(Context context) {
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService("window");
    }

    public void setWindowDelegate(MirrorWindowDelegate mirrorWindowDelegate) {
        this.mMirrorWindowDelegate = mirrorWindowDelegate;
    }

    public final void showControl() {
        if (this.mControlsView != null) {
            Log.w(TAG, "control view is visible");
            return;
        }
        Point point = this.mTmpPoint;
        this.mControlsView = onCreateView(LayoutInflater.from(this.mContext), point);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.magnification_controls_size);
        layoutParams.width = point.x <= 0 ? dimensionPixelSize : point.x;
        if (point.y > 0) {
            dimensionPixelSize = point.y;
        }
        layoutParams.height = dimensionPixelSize;
        setDefaultParams(layoutParams);
        setDefaultPosition(layoutParams);
        this.mWindowManager.addView(this.mControlsView, layoutParams);
        updateDraggableBound(layoutParams.width, layoutParams.height);
    }

    private void setDefaultParams(WindowManager.LayoutParams layoutParams) {
        layoutParams.gravity = 51;
        layoutParams.flags = 40;
        layoutParams.type = 2039;
        layoutParams.format = 1;
        layoutParams.setTitle(getWindowTitle());
    }

    private void setDefaultPosition(WindowManager.LayoutParams layoutParams) {
        Point point = this.mTmpPoint;
        this.mContext.getDisplay().getSize(point);
        layoutParams.x = point.x - layoutParams.width;
        layoutParams.y = point.y - layoutParams.height;
        this.mControlPosition.set(layoutParams.x, layoutParams.y);
    }

    public final void destroyControl() {
        View view = this.mControlsView;
        if (view != null) {
            this.mWindowManager.removeView(view);
            this.mControlsView = null;
        }
    }

    public void move(int i, int i2) {
        if (this.mControlsView == null) {
            Log.w(TAG, "control view is not available yet or destroyed");
            return;
        }
        this.mTmpPoint.set(this.mControlPosition.x, this.mControlPosition.y);
        this.mTmpPoint.offset(i, i2);
        setPosition(this.mTmpPoint);
    }

    private void setPosition(Point point) {
        constrainFrameToDraggableBound(point);
        if (!point.equals(this.mControlPosition)) {
            this.mControlPosition.set(point.x, point.y);
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mControlsView.getLayoutParams();
            layoutParams.x = this.mControlPosition.x;
            layoutParams.y = this.mControlPosition.y;
            this.mWindowManager.updateViewLayout(this.mControlsView, layoutParams);
        }
    }

    private void constrainFrameToDraggableBound(Point point) {
        point.x = MathUtils.constrain(point.x, this.mDraggableBound.left, this.mDraggableBound.right);
        point.y = MathUtils.constrain(point.y, this.mDraggableBound.top, this.mDraggableBound.bottom);
    }

    private void updateDraggableBound(int i, int i2) {
        Point point = this.mTmpPoint;
        this.mContext.getDisplay().getSize(point);
        this.mDraggableBound.set(0, 0, point.x - i, point.y - i2);
        if (DBG) {
            Log.d(TAG, "updateDraggableBound :" + this.mDraggableBound);
        }
    }
}
