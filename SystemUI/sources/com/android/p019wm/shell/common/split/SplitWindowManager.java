package com.android.p019wm.shell.common.split;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Binder;
import android.os.IBinder;
import android.view.IWindow;
import android.view.InsetsState;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import com.android.p019wm.shell.C3343R;

/* renamed from: com.android.wm.shell.common.split.SplitWindowManager */
public final class SplitWindowManager extends WindowlessWindowManager {
    private static final String TAG = "SplitWindowManager";
    private Context mContext;
    private DividerView mDividerView;
    private SurfaceControl mLeash;
    private final ParentContainerCallbacks mParentContainerCallbacks;
    private SurfaceControl.Transaction mSyncTransaction = null;
    private SurfaceControlViewHost mViewHost;
    private final String mWindowName;

    /* renamed from: com.android.wm.shell.common.split.SplitWindowManager$ParentContainerCallbacks */
    public interface ParentContainerCallbacks {
        void attachToParentSurface(SurfaceControl.Builder builder);

        void onLeashReady(SurfaceControl surfaceControl);
    }

    public SplitWindowManager(String str, Context context, Configuration configuration, ParentContainerCallbacks parentContainerCallbacks) {
        super(configuration, (SurfaceControl) null, (IBinder) null);
        this.mContext = context.createConfigurationContext(configuration);
        this.mParentContainerCallbacks = parentContainerCallbacks;
        this.mWindowName = str;
    }

    /* access modifiers changed from: package-private */
    public void setTouchRegion(Rect rect) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            setTouchRegion(surfaceControlViewHost.getWindowToken().asBinder(), new Region(rect));
        }
    }

    public SurfaceControl getSurfaceControl(IWindow iWindow) {
        return SplitWindowManager.super.getSurfaceControl(iWindow);
    }

    public void setConfiguration(Configuration configuration) {
        SplitWindowManager.super.setConfiguration(configuration);
        this.mContext = this.mContext.createConfigurationContext(configuration);
    }

    /* access modifiers changed from: protected */
    public void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        SurfaceControl.Builder callsite = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName(TAG).setHidden(true).setCallsite("SplitWindowManager#attachToParentSurface");
        this.mParentContainerCallbacks.attachToParentSurface(callsite);
        SurfaceControl build = callsite.build();
        this.mLeash = build;
        this.mParentContainerCallbacks.onLeashReady(build);
        builder.setParent(this.mLeash);
    }

    /* access modifiers changed from: package-private */
    public void init(SplitLayout splitLayout, InsetsState insetsState) {
        if (this.mDividerView == null && this.mViewHost == null) {
            Context context = this.mContext;
            this.mViewHost = new SurfaceControlViewHost(context, context.getDisplay(), this);
            this.mDividerView = (DividerView) LayoutInflater.from(this.mContext).inflate(C3343R.layout.split_divider, (ViewGroup) null);
            Rect dividerBounds = splitLayout.getDividerBounds();
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(dividerBounds.width(), dividerBounds.height(), 2034, 545521704, -3);
            layoutParams.token = new Binder();
            layoutParams.setTitle(this.mWindowName);
            layoutParams.privateFlags |= 536870976;
            this.mViewHost.setView(this.mDividerView, layoutParams);
            this.mDividerView.setup(splitLayout, this, this.mViewHost, insetsState);
            return;
        }
        throw new UnsupportedOperationException("Try to inflate divider view again without release first");
    }

    /* access modifiers changed from: package-private */
    public void release(SurfaceControl.Transaction transaction) {
        if (this.mDividerView != null) {
            this.mDividerView = null;
        }
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            this.mSyncTransaction = transaction;
            surfaceControlViewHost.release();
            this.mSyncTransaction = null;
            this.mViewHost = null;
        }
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl != null) {
            if (transaction == null) {
                new SurfaceControl.Transaction().remove(this.mLeash).apply();
            } else {
                transaction.remove(surfaceControl);
            }
            this.mLeash = null;
        }
    }

    /* access modifiers changed from: protected */
    public void removeSurface(SurfaceControl surfaceControl) {
        SurfaceControl.Transaction transaction = this.mSyncTransaction;
        if (transaction != null) {
            transaction.remove(surfaceControl);
        } else {
            SplitWindowManager.super.removeSurface(surfaceControl);
        }
    }

    /* access modifiers changed from: package-private */
    public void setInteractive(boolean z) {
        DividerView dividerView = this.mDividerView;
        if (dividerView != null) {
            dividerView.setInteractive(z);
        }
    }

    /* access modifiers changed from: package-private */
    public View getDividerView() {
        return this.mDividerView;
    }

    /* access modifiers changed from: package-private */
    public SurfaceControl getSurfaceControl() {
        return this.mLeash;
    }

    /* access modifiers changed from: package-private */
    public void onInsetsChanged(InsetsState insetsState) {
        DividerView dividerView = this.mDividerView;
        if (dividerView != null) {
            dividerView.onInsetsChanged(insetsState, true);
        }
    }
}
