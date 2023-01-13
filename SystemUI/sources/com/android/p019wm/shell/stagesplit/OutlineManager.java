package com.android.p019wm.shell.stagesplit;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.IWindow;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import android.widget.FrameLayout;
import com.android.p019wm.shell.C3353R;

/* renamed from: com.android.wm.shell.stagesplit.OutlineManager */
class OutlineManager extends WindowlessWindowManager {
    private static final String WINDOW_NAME = "SplitOutlineLayer";
    private final Context mContext;
    private final int mExpandedTaskBarHeight;
    private SurfaceControl mHostLeash;
    private final InsetsState mInsetsState = new InsetsState();
    private final Rect mLastOutlineBounds = new Rect();
    private SurfaceControl mLeash;
    private OutlineView mOutlineView;
    private final Rect mRootBounds = new Rect();
    private final Rect mTempRect = new Rect();
    private SurfaceControlViewHost mViewHost;

    OutlineManager(Context context, Configuration configuration) {
        super(configuration, (SurfaceControl) null, (IBinder) null);
        Context createWindowContext = context.createWindowContext(context.getDisplay(), 2038, (Bundle) null);
        this.mContext = createWindowContext;
        this.mExpandedTaskBarHeight = createWindowContext.getResources().getDimensionPixelSize(17105561);
    }

    /* access modifiers changed from: protected */
    public void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        builder.setParent(this.mHostLeash);
    }

    /* access modifiers changed from: package-private */
    public void inflate(SurfaceControl surfaceControl, Rect rect) {
        if (this.mLeash == null && this.mViewHost == null) {
            this.mHostLeash = surfaceControl;
            this.mRootBounds.set(rect);
            Context context = this.mContext;
            this.mViewHost = new SurfaceControlViewHost(context, context.getDisplay(), this);
            FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.mContext).inflate(C3353R.layout.split_outline, (ViewGroup) null);
            this.mOutlineView = (OutlineView) frameLayout.findViewById(C3353R.C3356id.split_outline);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(0, 0, 2038, 24, -3);
            layoutParams.width = this.mRootBounds.width();
            layoutParams.height = this.mRootBounds.height();
            layoutParams.token = new Binder();
            layoutParams.setTitle(WINDOW_NAME);
            layoutParams.privateFlags |= 536870976;
            this.mViewHost.setView(frameLayout, layoutParams);
            this.mLeash = getSurfaceControl(this.mViewHost.getWindowToken());
            drawOutline();
        }
    }

    /* access modifiers changed from: package-private */
    public void release() {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mViewHost = null;
        }
        this.mRootBounds.setEmpty();
        this.mLastOutlineBounds.setEmpty();
        this.mOutlineView = null;
        this.mHostLeash = null;
        this.mLeash = null;
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl getOutlineLeash() {
        return this.mLeash;
    }

    /* access modifiers changed from: package-private */
    public void setVisibility(boolean z) {
        OutlineView outlineView = this.mOutlineView;
        if (outlineView != null) {
            outlineView.setVisibility(z ? 0 : 4);
        }
    }

    /* access modifiers changed from: package-private */
    public void setRootBounds(Rect rect) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null && surfaceControlViewHost.getView() != null && !this.mRootBounds.equals(rect)) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mViewHost.getView().getLayoutParams();
            layoutParams.width = rect.width();
            layoutParams.height = rect.height();
            this.mViewHost.relayout(layoutParams);
            this.mRootBounds.set(rect);
            drawOutline();
        }
    }

    /* access modifiers changed from: package-private */
    public void onInsetsChanged(InsetsState insetsState) {
        if (!this.mInsetsState.equals(insetsState)) {
            this.mInsetsState.set(insetsState);
            drawOutline();
        }
    }

    private void computeOutlineBounds(Rect rect, InsetsState insetsState, Rect rect2) {
        rect2.set(rect);
        InsetsSource source = insetsState.getSource(21);
        if (source.getFrame().height() >= this.mExpandedTaskBarHeight) {
            rect2.inset(source.calculateVisibleInsets(rect2));
        }
        rect2.offset(-rect.left, -rect.top);
    }

    /* access modifiers changed from: package-private */
    public void drawOutline() {
        if (this.mOutlineView != null) {
            computeOutlineBounds(this.mRootBounds, this.mInsetsState, this.mTempRect);
            if (!this.mTempRect.equals(this.mLastOutlineBounds)) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mOutlineView.getLayoutParams();
                marginLayoutParams.leftMargin = this.mTempRect.left;
                marginLayoutParams.topMargin = this.mTempRect.top;
                marginLayoutParams.width = this.mTempRect.width();
                marginLayoutParams.height = this.mTempRect.height();
                this.mOutlineView.setLayoutParams(marginLayoutParams);
                this.mLastOutlineBounds.set(this.mTempRect);
            }
        }
    }
}
