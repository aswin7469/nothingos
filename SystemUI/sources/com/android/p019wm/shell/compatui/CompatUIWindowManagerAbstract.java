package com.android.p019wm.shell.compatui;

import android.app.TaskInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.IWindow;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.SyncTransactionQueue;

/* renamed from: com.android.wm.shell.compatui.CompatUIWindowManagerAbstract */
public abstract class CompatUIWindowManagerAbstract extends WindowlessWindowManager {
    protected Context mContext;
    private final int mDisplayId = this.mContext.getDisplayId();
    private DisplayLayout mDisplayLayout;
    protected SurfaceControl mLeash;
    private final Rect mStableBounds;
    private final SyncTransactionQueue mSyncQueue;
    private Configuration mTaskConfig;
    protected final int mTaskId;
    private ShellTaskOrganizer.TaskListener mTaskListener;
    protected SurfaceControlViewHost mViewHost;

    /* access modifiers changed from: protected */
    public abstract View createLayout();

    /* access modifiers changed from: protected */
    public abstract boolean eligibleToShowLayout();

    /* access modifiers changed from: protected */
    public abstract View getLayout();

    /* access modifiers changed from: protected */
    public abstract int getZOrder();

    /* access modifiers changed from: protected */
    public abstract void removeLayout();

    /* access modifiers changed from: protected */
    public abstract void updateSurfacePosition();

    protected CompatUIWindowManagerAbstract(Context context, TaskInfo taskInfo, SyncTransactionQueue syncTransactionQueue, ShellTaskOrganizer.TaskListener taskListener, DisplayLayout displayLayout) {
        super(taskInfo.configuration, (SurfaceControl) null, (IBinder) null);
        this.mContext = context;
        this.mSyncQueue = syncTransactionQueue;
        this.mTaskConfig = taskInfo.configuration;
        this.mTaskId = taskInfo.taskId;
        this.mTaskListener = taskListener;
        this.mDisplayLayout = displayLayout;
        Rect rect = new Rect();
        this.mStableBounds = rect;
        this.mDisplayLayout.getStableBounds(rect);
    }

    public boolean createLayout(boolean z) {
        if (!eligibleToShowLayout()) {
            return false;
        }
        if (!z || getLayout() != null) {
            return true;
        }
        if (this.mViewHost == null) {
            SurfaceControlViewHost createSurfaceViewHost = createSurfaceViewHost();
            this.mViewHost = createSurfaceViewHost;
            createSurfaceViewHost.setView(createLayout(), getWindowLayoutParams());
            updateSurfacePosition();
            return true;
        }
        throw new IllegalStateException("A UI has already been created with this window manager.");
    }

    public void setConfiguration(Configuration configuration) {
        CompatUIWindowManagerAbstract.super.setConfiguration(configuration);
        this.mContext = this.mContext.createConfigurationContext(configuration);
    }

    /* access modifiers changed from: protected */
    public void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
        String simpleName = getClass().getSimpleName();
        SurfaceControl.Builder callsite = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName(simpleName + "Leash").setHidden(false).setCallsite(simpleName + "#attachToParentSurface");
        attachToParentSurface(callsite);
        SurfaceControl build = callsite.build();
        this.mLeash = build;
        builder.setParent(build);
        initSurface(this.mLeash);
    }

    private void initSurface(SurfaceControl surfaceControl) {
        this.mSyncQueue.runInSync(new CompatUIWindowManagerAbstract$$ExternalSyntheticLambda1(this, surfaceControl, getZOrder()));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initSurface$0$com-android-wm-shell-compatui-CompatUIWindowManagerAbstract */
    public /* synthetic */ void mo49431xe1721304(SurfaceControl surfaceControl, int i, SurfaceControl.Transaction transaction) {
        if (surfaceControl == null || !surfaceControl.isValid()) {
            Log.w(getTag(), "The leash has been released.");
        } else {
            transaction.setLayer(surfaceControl, i);
        }
    }

    public boolean updateCompatInfo(TaskInfo taskInfo, ShellTaskOrganizer.TaskListener taskListener, boolean z) {
        Configuration configuration = this.mTaskConfig;
        ShellTaskOrganizer.TaskListener taskListener2 = this.mTaskListener;
        Configuration configuration2 = taskInfo.configuration;
        this.mTaskConfig = configuration2;
        this.mTaskListener = taskListener;
        setConfiguration(configuration2);
        boolean z2 = false;
        if (!eligibleToShowLayout()) {
            release();
            return false;
        }
        View layout = getLayout();
        if (layout == null || taskListener2 != taskListener) {
            release();
            return createLayout(z);
        }
        boolean z3 = !this.mTaskConfig.windowConfiguration.getBounds().equals(configuration.windowConfiguration.getBounds());
        if (this.mTaskConfig.getLayoutDirection() != configuration.getLayoutDirection()) {
            z2 = true;
        }
        if (z3 || z2) {
            onParentBoundsChanged();
        }
        if (layout != null && z2) {
            layout.setLayoutDirection(this.mTaskConfig.getLayoutDirection());
        }
        return true;
    }

    public void updateVisibility(boolean z) {
        View layout = getLayout();
        if (layout == null) {
            createLayout(z);
            return;
        }
        int i = (!z || !eligibleToShowLayout()) ? 8 : 0;
        if (layout.getVisibility() != i) {
            layout.setVisibility(i);
        }
    }

    public void updateDisplayLayout(DisplayLayout displayLayout) {
        Rect rect = this.mStableBounds;
        Rect rect2 = new Rect();
        displayLayout.getStableBounds(rect2);
        this.mDisplayLayout = displayLayout;
        if (!rect.equals(rect2)) {
            this.mStableBounds.set(rect2);
            onParentBoundsChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public void attachToParentSurface(SurfaceControl.Builder builder) {
        this.mTaskListener.attachChildSurfaceToTask(this.mTaskId, builder);
    }

    public int getDisplayId() {
        return this.mDisplayId;
    }

    public int getTaskId() {
        return this.mTaskId;
    }

    public void release() {
        View layout = getLayout();
        if (layout != null) {
            layout.setVisibility(8);
        }
        removeLayout();
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mViewHost = null;
        }
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl != null) {
            this.mSyncQueue.runInSync(new CompatUIWindowManagerAbstract$$ExternalSyntheticLambda2(surfaceControl));
            this.mLeash = null;
        }
    }

    /* access modifiers changed from: package-private */
    public void relayout() {
        relayout(getWindowLayoutParams());
    }

    /* access modifiers changed from: protected */
    public void relayout(WindowManager.LayoutParams layoutParams) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.relayout(layoutParams);
            updateSurfacePosition();
        }
    }

    /* access modifiers changed from: protected */
    public void onParentBoundsChanged() {
        updateSurfacePosition();
    }

    /* access modifiers changed from: protected */
    public void updateSurfacePosition(int i, int i2) {
        if (this.mLeash != null) {
            this.mSyncQueue.runInSync(new CompatUIWindowManagerAbstract$$ExternalSyntheticLambda0(this, i, i2));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateSurfacePosition$2$com-android-wm-shell-compatui-CompatUIWindowManagerAbstract */
    public /* synthetic */ void mo49432xb3cca3b6(int i, int i2, SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mLeash;
        if (surfaceControl == null || !surfaceControl.isValid()) {
            Log.w(getTag(), "The leash has been released.");
        } else {
            transaction.setPosition(this.mLeash, (float) i, (float) i2);
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutDirection() {
        return this.mContext.getResources().getConfiguration().getLayoutDirection();
    }

    /* access modifiers changed from: protected */
    public Rect getTaskBounds() {
        return this.mTaskConfig.windowConfiguration.getBounds();
    }

    /* access modifiers changed from: protected */
    public Rect getTaskStableBounds() {
        Rect rect = new Rect(this.mStableBounds);
        rect.intersect(getTaskBounds());
        return rect;
    }

    public SurfaceControlViewHost createSurfaceViewHost() {
        Context context = this.mContext;
        return new SurfaceControlViewHost(context, context.getDisplay(), this);
    }

    /* access modifiers changed from: protected */
    public WindowManager.LayoutParams getWindowLayoutParams() {
        View layout = getLayout();
        if (layout == null) {
            return new WindowManager.LayoutParams();
        }
        layout.measure(0, 0);
        return getWindowLayoutParams(layout.getMeasuredWidth(), layout.getMeasuredHeight());
    }

    /* access modifiers changed from: protected */
    public WindowManager.LayoutParams getWindowLayoutParams(int i, int i2) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, 2038, 40, -3);
        layoutParams.token = new Binder();
        layoutParams.setTitle(getClass().getSimpleName() + this.mTaskId);
        layoutParams.privateFlags |= 536870976;
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public final String getTag() {
        return getClass().getSimpleName();
    }
}
