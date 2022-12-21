package com.android.p019wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.ContextImpl;
import android.app.WindowConfiguration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.GraphicBuffer;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.HardwareBuffer;
import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Trace;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.view.IWindowSession;
import android.view.InputChannel;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.SurfaceControl;
import android.view.SurfaceSession;
import android.view.ViewRootImpl;
import android.view.WindowInsets;
import android.view.WindowLayout;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.window.ClientWindowFrames;
import android.window.StartingWindowInfo;
import android.window.TaskSnapshot;
import com.android.internal.policy.DecorView;
import com.android.internal.protolog.common.ProtoLog;
import com.android.internal.view.BaseIWindow;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.systemui.shared.system.QuickStepContract;
import java.lang.ref.WeakReference;

/* renamed from: com.android.wm.shell.startingsurface.TaskSnapshotWindow */
public class TaskSnapshotWindow {
    private static final long DELAY_REMOVAL_TIME_GENERAL = 100;
    static final int FLAG_INHERIT_EXCLUDES = 830922808;
    private static final long MAX_DELAY_REMOVAL_TIME_IME_VISIBLE = 600;
    private static final String TAG = "ShellStartingWindow";
    private static final String TITLE_FORMAT = "SnapshotStartingWindow for taskId=%s";
    private final int mActivityType;
    private final Paint mBackgroundPaint;
    private final Runnable mClearWindowHandler;
    private final Rect mFrame = new Rect();
    /* access modifiers changed from: private */
    public boolean mHasDrawn;
    private final boolean mHasImeSurface;
    /* access modifiers changed from: private */
    public final int mOrientationOnCreation;
    private final Runnable mScheduledRunnable;
    private final IWindowSession mSession;
    private boolean mSizeMismatch;
    private TaskSnapshot mSnapshot;
    private final Matrix mSnapshotMatrix;
    /* access modifiers changed from: private */
    public final ShellExecutor mSplashScreenExecutor;
    private final int mStatusBarColor;
    private final SurfaceControl mSurfaceControl;
    private final SystemBarBackgroundPainter mSystemBarBackgroundPainter;
    private final Rect mSystemBarInsets = new Rect();
    private final Rect mTaskBounds;
    private final CharSequence mTitle;
    private final RectF mTmpDstFrame = new RectF();
    private final float[] mTmpFloat9;
    private final RectF mTmpSnapshotSize = new RectF();
    private final SurfaceControl.Transaction mTransaction;
    private final Window mWindow;

    static TaskSnapshotWindow create(StartingWindowInfo startingWindowInfo, IBinder iBinder, TaskSnapshot taskSnapshot, ShellExecutor shellExecutor, Runnable runnable) {
        ActivityManager.TaskDescription taskDescription;
        StartingWindowInfo startingWindowInfo2 = startingWindowInfo;
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo2.taskInfo;
        int i = runningTaskInfo.taskId;
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "create taskSnapshot surface for task: %d", new Object[]{Integer.valueOf(i)});
        WindowManager.LayoutParams layoutParams = startingWindowInfo2.topOpaqueWindowLayoutParams;
        WindowManager.LayoutParams layoutParams2 = startingWindowInfo2.mainWindowLayoutParams;
        InsetsState insetsState = startingWindowInfo2.topOpaqueWindowInsetsState;
        if (layoutParams == null || layoutParams2 == null || insetsState == null) {
            Slog.w("ShellStartingWindow", "unable to create taskSnapshot surface for task: " + i);
            return null;
        }
        WindowManager.LayoutParams layoutParams3 = new WindowManager.LayoutParams();
        int i2 = layoutParams.insetsFlags.appearance;
        int i3 = layoutParams.flags;
        int i4 = layoutParams.privateFlags;
        layoutParams3.packageName = layoutParams2.packageName;
        layoutParams3.windowAnimations = layoutParams2.windowAnimations;
        layoutParams3.dimAmount = layoutParams2.dimAmount;
        layoutParams3.type = 3;
        layoutParams3.format = taskSnapshot.getHardwareBuffer().getFormat();
        layoutParams3.flags = (-830922809 & i3) | 8 | 16;
        layoutParams3.privateFlags = (131072 & i4) | NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_OVERRIDE | QuickStepContract.SYSUI_STATE_VOICE_INTERACTION_WINDOW_SHOWING;
        layoutParams3.token = iBinder;
        layoutParams3.width = -1;
        layoutParams3.height = -1;
        layoutParams3.insetsFlags.appearance = i2;
        layoutParams3.insetsFlags.behavior = layoutParams.insetsFlags.behavior;
        layoutParams3.layoutInDisplayCutoutMode = layoutParams.layoutInDisplayCutoutMode;
        layoutParams3.setFitInsetsTypes(layoutParams.getFitInsetsTypes());
        layoutParams3.setFitInsetsSides(layoutParams.getFitInsetsSides());
        layoutParams3.setFitInsetsIgnoringVisibility(layoutParams.isFitInsetsIgnoringVisibility());
        layoutParams3.setTitle(String.format(TITLE_FORMAT, Integer.valueOf(i)));
        Point taskSize = taskSnapshot.getTaskSize();
        Rect rect = new Rect(0, 0, taskSize.x, taskSize.y);
        int orientation = taskSnapshot.getOrientation();
        int i5 = runningTaskInfo.topActivityType;
        int i6 = runningTaskInfo.displayId;
        IWindowSession windowSession = WindowManagerGlobal.getWindowSession();
        SurfaceControl surfaceControl = new SurfaceControl();
        ClientWindowFrames clientWindowFrames = new ClientWindowFrames();
        WindowLayout windowLayout = new WindowLayout();
        Rect rect2 = new Rect();
        InsetsSourceControl[] insetsSourceControlArr = new InsetsSourceControl[0];
        MergedConfiguration mergedConfiguration = new MergedConfiguration();
        if (runningTaskInfo.taskDescription != null) {
            taskDescription = runningTaskInfo.taskDescription;
        } else {
            taskDescription = new ActivityManager.TaskDescription();
            taskDescription.setBackgroundColor(-1);
        }
        Rect rect3 = rect2;
        ClientWindowFrames clientWindowFrames2 = clientWindowFrames;
        int i7 = i3;
        CharSequence title = layoutParams3.getTitle();
        int i8 = i2;
        ActivityManager.TaskDescription taskDescription2 = taskDescription;
        WindowManager.LayoutParams layoutParams4 = layoutParams3;
        String str = "ShellStartingWindow";
        TaskSnapshotWindow taskSnapshotWindow = new TaskSnapshotWindow(surfaceControl, taskSnapshot, title, taskDescription2, i8, i7, i4, rect, orientation, i5, insetsState, runnable, shellExecutor);
        Window window = taskSnapshotWindow.mWindow;
        InsetsState insetsState2 = new InsetsState();
        InputChannel inputChannel = new InputChannel();
        try {
            Trace.traceBegin(32, "TaskSnapshot#addToDisplay");
            int addToDisplay = windowSession.addToDisplay(window, layoutParams4, 8, i6, startingWindowInfo2.requestedVisibilities, inputChannel, insetsState2, insetsSourceControlArr);
            Trace.traceEnd(32);
            if (addToDisplay < 0) {
                Slog.w(str, "Failed to add snapshot starting window res=" + addToDisplay);
                return null;
            }
        } catch (RemoteException unused) {
            taskSnapshotWindow.clearWindowSynced();
        }
        window.setOuter(taskSnapshotWindow);
        try {
            Trace.traceBegin(32, "TaskSnapshot#relayout");
            if (ViewRootImpl.LOCAL_LAYOUT) {
                if (!surfaceControl.isValid()) {
                    windowSession.updateVisibility(window, layoutParams4, 0, mergedConfiguration, surfaceControl, insetsState2, insetsSourceControlArr);
                }
                Rect rect4 = rect3;
                insetsState2.getDisplayCutoutSafe(rect4);
                WindowConfiguration windowConfiguration = mergedConfiguration.getMergedConfiguration().windowConfiguration;
                windowLayout.computeFrames(layoutParams4, insetsState2, rect4, windowConfiguration.getBounds(), windowConfiguration.getWindowingMode(), -1, -1, startingWindowInfo2.requestedVisibilities, (Rect) null, 1.0f, clientWindowFrames2);
                windowSession.updateLayout(window, layoutParams4, 0, clientWindowFrames2, -1, -1);
            } else {
                windowSession.relayout(window, layoutParams4, -1, -1, 0, 0, clientWindowFrames2, mergedConfiguration, surfaceControl, insetsState2, insetsSourceControlArr, new Bundle());
            }
            Trace.traceEnd(32);
        } catch (RemoteException unused2) {
            taskSnapshotWindow.clearWindowSynced();
        }
        ClientWindowFrames clientWindowFrames3 = clientWindowFrames2;
        taskSnapshotWindow.setFrames(clientWindowFrames3.frame, getSystemBarInsets(clientWindowFrames3.frame, insetsState));
        taskSnapshotWindow.drawSnapshot();
        return taskSnapshotWindow;
    }

    public TaskSnapshotWindow(SurfaceControl surfaceControl, TaskSnapshot taskSnapshot, CharSequence charSequence, ActivityManager.TaskDescription taskDescription, int i, int i2, int i3, Rect rect, int i4, int i5, InsetsState insetsState, Runnable runnable, ShellExecutor shellExecutor) {
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        this.mSnapshotMatrix = new Matrix();
        this.mTmpFloat9 = new float[9];
        this.mScheduledRunnable = new TaskSnapshotWindow$$ExternalSyntheticLambda0(this);
        this.mSplashScreenExecutor = shellExecutor;
        IWindowSession windowSession = WindowManagerGlobal.getWindowSession();
        this.mSession = windowSession;
        Window window = new Window();
        this.mWindow = window;
        window.setSession(windowSession);
        this.mSurfaceControl = surfaceControl;
        this.mSnapshot = taskSnapshot;
        this.mTitle = charSequence;
        int backgroundColor = taskDescription.getBackgroundColor();
        paint.setColor(backgroundColor == 0 ? -1 : backgroundColor);
        this.mTaskBounds = rect;
        this.mSystemBarBackgroundPainter = new SystemBarBackgroundPainter(i2, i3, i, taskDescription, 1.0f, insetsState);
        this.mStatusBarColor = taskDescription.getStatusBarColor();
        this.mOrientationOnCreation = i4;
        this.mActivityType = i5;
        this.mTransaction = new SurfaceControl.Transaction();
        this.mClearWindowHandler = runnable;
        this.mHasImeSurface = taskSnapshot.hasImeSurface();
    }

    /* access modifiers changed from: package-private */
    public int getBackgroundColor() {
        return this.mBackgroundPaint.getColor();
    }

    /* access modifiers changed from: package-private */
    public boolean hasImeSurface() {
        return this.mHasImeSurface;
    }

    public void drawStatusBarBackground(Canvas canvas, Rect rect) {
        SystemBarBackgroundPainter systemBarBackgroundPainter = this.mSystemBarBackgroundPainter;
        systemBarBackgroundPainter.drawStatusBarBackground(canvas, rect, systemBarBackgroundPainter.getStatusBarColorViewHeight());
    }

    public void drawNavigationBarBackground(Canvas canvas) {
        this.mSystemBarBackgroundPainter.drawNavigationBarBackground(canvas);
    }

    /* access modifiers changed from: package-private */
    public void scheduleRemove(boolean z) {
        if (this.mActivityType == 2) {
            removeImmediately();
            return;
        }
        this.mSplashScreenExecutor.removeCallbacks(this.mScheduledRunnable);
        long j = (!this.mHasImeSurface || !z) ? 100 : MAX_DELAY_REMOVAL_TIME_IME_VISIBLE;
        this.mSplashScreenExecutor.executeDelayed(this.mScheduledRunnable, j);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "Defer removing snapshot surface in %d", new Object[]{Long.valueOf(j)});
    }

    /* access modifiers changed from: package-private */
    public void removeImmediately() {
        this.mSplashScreenExecutor.removeCallbacks(this.mScheduledRunnable);
        try {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "Removing taskSnapshot surface, mHasDrawn=%b", new Object[]{Boolean.valueOf(this.mHasDrawn)});
            this.mSession.remove(this.mWindow);
        } catch (RemoteException unused) {
        }
    }

    public void setFrames(Rect rect, Rect rect2) {
        this.mFrame.set(rect);
        this.mSystemBarInsets.set(rect2);
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        this.mSizeMismatch = (this.mFrame.width() == hardwareBuffer.getWidth() && this.mFrame.height() == hardwareBuffer.getHeight()) ? false : true;
        this.mSystemBarBackgroundPainter.setInsets(rect2);
    }

    static Rect getSystemBarInsets(Rect rect, InsetsState insetsState) {
        return insetsState.calculateInsets(rect, WindowInsets.Type.systemBars(), false).toRect();
    }

    private void drawSnapshot() {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "Drawing snapshot surface sizeMismatch=%b", new Object[]{Boolean.valueOf(this.mSizeMismatch)});
        if (this.mSizeMismatch) {
            drawSizeMismatchSnapshot();
        } else {
            drawSizeMatchSnapshot();
        }
        this.mHasDrawn = true;
        reportDrawn();
        if (this.mSnapshot.getHardwareBuffer() != null) {
            this.mSnapshot.getHardwareBuffer().close();
        }
        this.mSnapshot = null;
        this.mSurfaceControl.release();
    }

    private void drawSizeMatchSnapshot() {
        this.mTransaction.setBuffer(this.mSurfaceControl, this.mSnapshot.getHardwareBuffer()).setColorSpace(this.mSurfaceControl, this.mSnapshot.getColorSpace()).apply();
    }

    private void drawSizeMismatchSnapshot() {
        Rect rect;
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        SurfaceSession surfaceSession = new SurfaceSession();
        boolean z = Math.abs((((float) hardwareBuffer.getWidth()) / ((float) hardwareBuffer.getHeight())) - (((float) this.mFrame.width()) / ((float) this.mFrame.height()))) > 0.01f;
        SurfaceControl build = new SurfaceControl.Builder(surfaceSession).setName(this.mTitle + " - task-snapshot-surface").setBLASTLayer().setFormat(hardwareBuffer.getFormat()).setParent(this.mSurfaceControl).setCallsite("TaskSnapshotWindow.drawSizeMismatchSnapshot").build();
        this.mTransaction.show(build);
        if (z) {
            Rect calculateSnapshotCrop = calculateSnapshotCrop();
            rect = calculateSnapshotFrame(calculateSnapshotCrop);
            this.mTransaction.setWindowCrop(build, calculateSnapshotCrop);
            this.mTransaction.setPosition(build, (float) rect.left, (float) rect.top);
            this.mTmpSnapshotSize.set(calculateSnapshotCrop);
            this.mTmpDstFrame.set(rect);
        } else {
            this.mTmpSnapshotSize.set(0.0f, 0.0f, (float) hardwareBuffer.getWidth(), (float) hardwareBuffer.getHeight());
            this.mTmpDstFrame.set(this.mFrame);
            this.mTmpDstFrame.offsetTo(0.0f, 0.0f);
            rect = null;
        }
        this.mSnapshotMatrix.setRectToRect(this.mTmpSnapshotSize, this.mTmpDstFrame, Matrix.ScaleToFit.FILL);
        this.mTransaction.setMatrix(build, this.mSnapshotMatrix, this.mTmpFloat9);
        this.mTransaction.setColorSpace(build, this.mSnapshot.getColorSpace());
        this.mTransaction.setBuffer(build, this.mSnapshot.getHardwareBuffer());
        if (z) {
            GraphicBuffer create = GraphicBuffer.create(this.mFrame.width(), this.mFrame.height(), 1, 2336);
            Canvas lockCanvas = create.lockCanvas();
            drawBackgroundAndBars(lockCanvas, rect);
            create.unlockCanvasAndPost(lockCanvas);
            this.mTransaction.setBuffer(this.mSurfaceControl, HardwareBuffer.createFromGraphicBuffer(create));
        }
        this.mTransaction.apply();
        build.release();
    }

    public Rect calculateSnapshotCrop() {
        Rect rect = new Rect();
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        int i = 0;
        rect.set(0, 0, hardwareBuffer.getWidth(), hardwareBuffer.getHeight());
        Rect contentInsets = this.mSnapshot.getContentInsets();
        float width = ((float) hardwareBuffer.getWidth()) / ((float) this.mSnapshot.getTaskSize().x);
        float height = ((float) hardwareBuffer.getHeight()) / ((float) this.mSnapshot.getTaskSize().y);
        boolean z = this.mTaskBounds.top == 0 && this.mFrame.top == 0;
        int i2 = (int) (((float) contentInsets.left) * width);
        if (!z) {
            i = (int) (((float) contentInsets.top) * height);
        }
        rect.inset(i2, i, (int) (((float) contentInsets.right) * width), (int) (((float) contentInsets.bottom) * height));
        return rect;
    }

    public Rect calculateSnapshotFrame(Rect rect) {
        HardwareBuffer hardwareBuffer = this.mSnapshot.getHardwareBuffer();
        Rect rect2 = new Rect(0, 0, (int) ((((float) rect.width()) / (((float) hardwareBuffer.getWidth()) / ((float) this.mSnapshot.getTaskSize().x))) + 0.5f), (int) ((((float) rect.height()) / (((float) hardwareBuffer.getHeight()) / ((float) this.mSnapshot.getTaskSize().y))) + 0.5f));
        rect2.offset(this.mSystemBarInsets.left, 0);
        return rect2;
    }

    public void drawBackgroundAndBars(Canvas canvas, Rect rect) {
        int i;
        Rect rect2 = rect;
        int statusBarColorViewHeight = this.mSystemBarBackgroundPainter.getStatusBarColorViewHeight();
        boolean z = true;
        boolean z2 = canvas.getWidth() > rect2.right;
        if (canvas.getHeight() <= rect2.bottom) {
            z = false;
        }
        if (z2) {
            float f = (float) rect2.right;
            float f2 = Color.alpha(this.mStatusBarColor) == 255 ? (float) statusBarColorViewHeight : 0.0f;
            float width = (float) canvas.getWidth();
            if (z) {
                i = rect2.bottom;
            } else {
                i = canvas.getHeight();
            }
            canvas.drawRect(f, f2, width, (float) i, this.mBackgroundPaint);
        }
        if (z) {
            canvas.drawRect(0.0f, (float) rect2.bottom, (float) canvas.getWidth(), (float) canvas.getHeight(), this.mBackgroundPaint);
        }
        this.mSystemBarBackgroundPainter.drawDecors(canvas, rect2);
    }

    /* access modifiers changed from: private */
    public void clearWindowSynced() {
        this.mSplashScreenExecutor.executeDelayed(this.mClearWindowHandler, 0);
    }

    /* access modifiers changed from: private */
    public void reportDrawn() {
        try {
            this.mSession.finishDrawing(this.mWindow, (SurfaceControl.Transaction) null, Integer.MAX_VALUE);
        } catch (RemoteException unused) {
            clearWindowSynced();
        }
    }

    /* renamed from: com.android.wm.shell.startingsurface.TaskSnapshotWindow$Window */
    static class Window extends BaseIWindow {
        private WeakReference<TaskSnapshotWindow> mOuter;

        Window() {
        }

        public void setOuter(TaskSnapshotWindow taskSnapshotWindow) {
            this.mOuter = new WeakReference<>(taskSnapshotWindow);
        }

        public void resized(ClientWindowFrames clientWindowFrames, boolean z, MergedConfiguration mergedConfiguration, InsetsState insetsState, boolean z2, boolean z3, int i, int i2, int i3) {
            TaskSnapshotWindow taskSnapshotWindow = this.mOuter.get();
            if (taskSnapshotWindow != null) {
                taskSnapshotWindow.mSplashScreenExecutor.execute(new TaskSnapshotWindow$Window$$ExternalSyntheticLambda0(mergedConfiguration, taskSnapshotWindow, z));
            }
        }

        static /* synthetic */ void lambda$resized$0(MergedConfiguration mergedConfiguration, TaskSnapshotWindow taskSnapshotWindow, boolean z) {
            if (mergedConfiguration != null && taskSnapshotWindow.mOrientationOnCreation != mergedConfiguration.getMergedConfiguration().orientation) {
                taskSnapshotWindow.clearWindowSynced();
            } else if (z && taskSnapshotWindow.mHasDrawn) {
                taskSnapshotWindow.reportDrawn();
            }
        }
    }

    /* renamed from: com.android.wm.shell.startingsurface.TaskSnapshotWindow$SystemBarBackgroundPainter */
    static class SystemBarBackgroundPainter {
        private final InsetsState mInsetsState;
        private final int mNavigationBarColor;
        private final Paint mNavigationBarPaint;
        private final float mScale;
        private final int mStatusBarColor;
        private final Paint mStatusBarPaint;
        private final Rect mSystemBarInsets = new Rect();
        private final int mWindowFlags;
        private final int mWindowPrivateFlags;

        SystemBarBackgroundPainter(int i, int i2, int i3, ActivityManager.TaskDescription taskDescription, float f, InsetsState insetsState) {
            Paint paint = new Paint();
            this.mStatusBarPaint = paint;
            Paint paint2 = new Paint();
            this.mNavigationBarPaint = paint2;
            this.mWindowFlags = i;
            this.mWindowPrivateFlags = i2;
            this.mScale = f;
            ContextImpl systemUiContext = ActivityThread.currentActivityThread().getSystemUiContext();
            int color = systemUiContext.getColor(17171095);
            int calculateBarColor = DecorView.calculateBarColor(i, 67108864, color, taskDescription.getStatusBarColor(), i3, 8, taskDescription.getEnsureStatusBarContrastWhenTransparent());
            this.mStatusBarColor = calculateBarColor;
            int calculateBarColor2 = DecorView.calculateBarColor(i, 134217728, color, taskDescription.getNavigationBarColor(), i3, 16, taskDescription.getEnsureNavigationBarContrastWhenTransparent() && systemUiContext.getResources().getBoolean(17891710));
            this.mNavigationBarColor = calculateBarColor2;
            paint.setColor(calculateBarColor);
            paint2.setColor(calculateBarColor2);
            this.mInsetsState = insetsState;
        }

        /* access modifiers changed from: package-private */
        public void setInsets(Rect rect) {
            this.mSystemBarInsets.set(rect);
        }

        /* access modifiers changed from: package-private */
        public int getStatusBarColorViewHeight() {
            if (DecorView.STATUS_BAR_COLOR_VIEW_ATTRIBUTES.isVisible(this.mInsetsState, this.mStatusBarColor, this.mWindowFlags, (this.mWindowPrivateFlags & 131072) != 0)) {
                return (int) (((float) this.mSystemBarInsets.top) * this.mScale);
            }
            return 0;
        }

        private boolean isNavigationBarColorViewVisible() {
            return DecorView.NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES.isVisible(this.mInsetsState, this.mNavigationBarColor, this.mWindowFlags, (this.mWindowPrivateFlags & 131072) != 0);
        }

        /* access modifiers changed from: package-private */
        public void drawDecors(Canvas canvas, Rect rect) {
            drawStatusBarBackground(canvas, rect, getStatusBarColorViewHeight());
            drawNavigationBarBackground(canvas);
        }

        /* access modifiers changed from: package-private */
        public void drawStatusBarBackground(Canvas canvas, Rect rect, int i) {
            if (i > 0 && Color.alpha(this.mStatusBarColor) != 0) {
                if (rect == null || canvas.getWidth() > rect.right) {
                    canvas.drawRect((float) (rect != null ? rect.right : 0), 0.0f, (float) (canvas.getWidth() - ((int) (((float) this.mSystemBarInsets.right) * this.mScale))), (float) i, this.mStatusBarPaint);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void drawNavigationBarBackground(Canvas canvas) {
            Rect rect = new Rect();
            DecorView.getNavigationBarRect(canvas.getWidth(), canvas.getHeight(), this.mSystemBarInsets, rect, this.mScale);
            if (isNavigationBarColorViewVisible() && Color.alpha(this.mNavigationBarColor) != 0 && !rect.isEmpty()) {
                canvas.drawRect(rect, this.mNavigationBarPaint);
            }
        }
    }
}
