package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.TaskInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Choreographer;
import android.view.Display;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.window.SplashScreenView;
import android.window.StartingWindowInfo;
import android.window.TaskSnapshot;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.startingsurface.StartingSurfaceDrawer;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public class StartingSurfaceDrawer {
    static final boolean DEBUG_SPLASH_SCREEN = StartingWindowController.DEBUG_SPLASH_SCREEN;
    static final String TAG = "StartingSurfaceDrawer";
    private Choreographer mChoreographer;
    private final Context mContext;
    private final DisplayManager mDisplayManager;
    private final ShellExecutor mSplashScreenExecutor;
    @VisibleForTesting
    final SplashscreenContentDrawer mSplashscreenContentDrawer;
    private final SparseArray<StartingWindowRecord> mStartingWindowRecords = new SparseArray<>();
    private final SparseArray<SurfaceControlViewHost> mAnimatedSplashScreenSurfaceHosts = new SparseArray<>(1);

    public StartingSurfaceDrawer(Context context, ShellExecutor shellExecutor, TransactionPool transactionPool) {
        this.mContext = context;
        this.mDisplayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mSplashScreenExecutor = shellExecutor;
        this.mSplashscreenContentDrawer = new SplashscreenContentDrawer(context, transactionPool);
        shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                StartingSurfaceDrawer.this.lambda$new$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mChoreographer = Choreographer.getInstance();
    }

    private Display getDisplay(int i) {
        return this.mDisplayManager.getDisplay(i);
    }

    int getSplashScreenTheme(int i, ActivityInfo activityInfo) {
        if (i != 0) {
            return i;
        }
        if (activityInfo.getThemeResource() == 0) {
            return 16974563;
        }
        return activityInfo.getThemeResource();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addSplashScreenStartingWindow(StartingWindowInfo startingWindowInfo, final IBinder iBinder, @StartingWindowInfo.StartingWindowType int i) {
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo.taskInfo;
        ActivityInfo activityInfo = startingWindowInfo.targetActivityInfo;
        if (activityInfo == null) {
            activityInfo = runningTaskInfo.topActivityInfo;
        }
        ActivityInfo activityInfo2 = activityInfo;
        if (activityInfo2 == null || activityInfo2.packageName == null) {
            return;
        }
        int i2 = runningTaskInfo.displayId;
        final int i3 = runningTaskInfo.taskId;
        int splashScreenTheme = getSplashScreenTheme(startingWindowInfo.splashScreenThemeResId, activityInfo2);
        boolean z = DEBUG_SPLASH_SCREEN;
        if (z) {
            Slog.d(TAG, "addSplashScreen " + activityInfo2.packageName + " theme=" + Integer.toHexString(splashScreenTheme) + " task=" + runningTaskInfo.taskId + " suggestType=" + i);
        }
        Display display = getDisplay(i2);
        if (display == null) {
            return;
        }
        Context createDisplayContext = i2 == 0 ? this.mContext : this.mContext.createDisplayContext(display);
        if (createDisplayContext == null) {
            return;
        }
        if (splashScreenTheme != createDisplayContext.getThemeResId()) {
            try {
                createDisplayContext = createDisplayContext.createPackageContextAsUser(activityInfo2.packageName, 4, UserHandle.of(runningTaskInfo.userId));
                createDisplayContext.setTheme(splashScreenTheme);
            } catch (PackageManager.NameNotFoundException e) {
                Slog.w(TAG, "Failed creating package context with package name " + activityInfo2.packageName + " for user " + runningTaskInfo.userId, e);
                return;
            }
        }
        Configuration configuration = runningTaskInfo.getConfiguration();
        if (configuration.diffPublicOnly(createDisplayContext.getResources().getConfiguration()) != 0) {
            if (z) {
                Slog.d(TAG, "addSplashScreen: creating context based on task Configuration " + configuration + " for splash screen");
            }
            Context createConfigurationContext = createDisplayContext.createConfigurationContext(configuration);
            createConfigurationContext.setTheme(splashScreenTheme);
            TypedArray obtainStyledAttributes = createConfigurationContext.obtainStyledAttributes(R.styleable.Window);
            int resourceId = obtainStyledAttributes.getResourceId(1, 0);
            if (resourceId != 0) {
                try {
                    if (createConfigurationContext.getDrawable(resourceId) != null) {
                        if (z) {
                            Slog.d(TAG, "addSplashScreen: apply overrideConfig" + configuration + " to starting window resId=" + resourceId);
                        }
                        createDisplayContext = createConfigurationContext;
                    }
                } catch (Resources.NotFoundException e2) {
                    Slog.w(TAG, "failed creating starting window for overrideConfig at taskId: " + i3, e2);
                    return;
                }
            }
            obtainStyledAttributes.recycle();
        }
        Context context = createDisplayContext;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(3);
        layoutParams.setFitInsetsSides(0);
        layoutParams.setFitInsetsTypes(0);
        layoutParams.format = -3;
        int i4 = 16843008;
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(R.styleable.Window);
        if (obtainStyledAttributes2.getBoolean(14, false)) {
            i4 = 17891584;
        }
        if (i != 4 || obtainStyledAttributes2.getBoolean(33, false)) {
            i4 |= Integer.MIN_VALUE;
        }
        layoutParams.layoutInDisplayCutoutMode = obtainStyledAttributes2.getInt(50, layoutParams.layoutInDisplayCutoutMode);
        layoutParams.windowAnimations = obtainStyledAttributes2.getResourceId(8, 0);
        obtainStyledAttributes2.recycle();
        if (i2 == 0 && startingWindowInfo.isKeyguardOccluded) {
            i4 |= 524288;
        }
        layoutParams.flags = 131096 | i4;
        layoutParams.token = iBinder;
        layoutParams.packageName = activityInfo2.packageName;
        int i5 = layoutParams.privateFlags | 16;
        layoutParams.privateFlags = i5;
        layoutParams.privateFlags = i5 | 536870912;
        if (!context.getResources().getCompatibilityInfo().supportsScreen()) {
            layoutParams.privateFlags |= 128;
        }
        layoutParams.setTitle("Splash Screen " + activityInfo2.packageName);
        final SplashScreenViewSupplier splashScreenViewSupplier = new SplashScreenViewSupplier();
        final FrameLayout frameLayout = new FrameLayout(this.mSplashscreenContentDrawer.createViewContextWrapper(context));
        frameLayout.setPadding(0, 0, 0, 0);
        frameLayout.setFitsSystemWindows(false);
        Runnable runnable = new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                StartingSurfaceDrawer.this.lambda$addSplashScreenStartingWindow$1(splashScreenViewSupplier, i3, iBinder, frameLayout);
            }
        };
        this.mSplashscreenContentDrawer.createContentView(context, i, activityInfo2, i3, new Consumer() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                StartingSurfaceDrawer.SplashScreenViewSupplier.this.setView((SplashScreenView) obj);
            }
        });
        try {
            if (!addWindow(i3, iBinder, frameLayout, (WindowManager) context.getSystemService(WindowManager.class), layoutParams, i)) {
                return;
            }
            this.mChoreographer.postCallback(2, runnable, null);
            this.mStartingWindowRecords.get(i3).mBGColor = splashScreenViewSupplier.mo1778get().getInitBackgroundColor();
        } catch (RuntimeException e3) {
            Slog.w(TAG, "failed creating starting window at taskId: " + i3, e3);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addSplashScreenStartingWindow$1(SplashScreenViewSupplier splashScreenViewSupplier, int i, IBinder iBinder, FrameLayout frameLayout) {
        Trace.traceBegin(32L, "addSplashScreenView");
        SplashScreenView mo1778get = splashScreenViewSupplier.mo1778get();
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null && iBinder == startingWindowRecord.mAppToken) {
            if (mo1778get != null) {
                try {
                    frameLayout.addView(mo1778get);
                } catch (RuntimeException e) {
                    String str = TAG;
                    Slog.w(str, "failed set content view to starting window at taskId: " + i, e);
                    mo1778get = null;
                }
            }
            startingWindowRecord.setSplashScreenView(mo1778get);
        }
        Trace.traceEnd(32L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getStartingWindowBackgroundColorForTask(int i) {
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord == null) {
            return 0;
        }
        return startingWindowRecord.mBGColor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class SplashScreenViewSupplier implements Supplier<SplashScreenView> {
        private boolean mIsViewSet;
        private SplashScreenView mView;

        private SplashScreenViewSupplier() {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void setView(SplashScreenView splashScreenView) {
            synchronized (this) {
                this.mView = splashScreenView;
                this.mIsViewSet = true;
                notify();
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.function.Supplier
        /* renamed from: get */
        public SplashScreenView mo1778get() {
            SplashScreenView splashScreenView;
            synchronized (this) {
                while (!this.mIsViewSet) {
                    try {
                        wait();
                    } catch (InterruptedException unused) {
                    }
                }
                splashScreenView = this.mView;
            }
            return splashScreenView;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int estimateTaskBackgroundColor(TaskInfo taskInfo) {
        ActivityInfo activityInfo = taskInfo.topActivityInfo;
        if (activityInfo == null) {
            return 0;
        }
        String str = activityInfo.packageName;
        int i = taskInfo.userId;
        try {
            Context createPackageContextAsUser = this.mContext.createPackageContextAsUser(str, 4, UserHandle.of(i));
            try {
                String splashScreenTheme = ActivityThread.getPackageManager().getSplashScreenTheme(str, i);
                int splashScreenTheme2 = getSplashScreenTheme(splashScreenTheme != null ? createPackageContextAsUser.getResources().getIdentifier(splashScreenTheme, null, null) : 0, activityInfo);
                if (splashScreenTheme2 != createPackageContextAsUser.getThemeResId()) {
                    createPackageContextAsUser.setTheme(splashScreenTheme2);
                }
                return this.mSplashscreenContentDrawer.estimateTaskBackgroundColor(createPackageContextAsUser);
            } catch (RemoteException | RuntimeException e) {
                String str2 = TAG;
                Slog.w(str2, "failed get starting window background color at taskId: " + taskInfo.taskId, e);
                return 0;
            }
        } catch (PackageManager.NameNotFoundException e2) {
            String str3 = TAG;
            Slog.w(str3, "Failed creating package context with package name " + str + " for user " + taskInfo.userId, e2);
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void makeTaskSnapshotWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder, TaskSnapshot taskSnapshot) {
        final int i = startingWindowInfo.taskInfo.taskId;
        lambda$makeTaskSnapshotWindow$2(i);
        TaskSnapshotWindow create = TaskSnapshotWindow.create(startingWindowInfo, iBinder, taskSnapshot, this.mSplashScreenExecutor, new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                StartingSurfaceDrawer.this.lambda$makeTaskSnapshotWindow$2(i);
            }
        });
        if (create == null) {
            return;
        }
        this.mStartingWindowRecords.put(i, new StartingWindowRecord(iBinder, null, create, 2));
    }

    public void removeStartingWindow(int i, SurfaceControl surfaceControl, Rect rect, boolean z) {
        if (DEBUG_SPLASH_SCREEN) {
            String str = TAG;
            Slog.d(str, "Task start finish, remove starting surface for task " + i);
        }
        removeWindowSynced(i, surfaceControl, rect, z);
    }

    public void copySplashScreenView(final int i) {
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        SplashScreenView.SplashScreenViewParcelable splashScreenViewParcelable = null;
        SplashScreenView splashScreenView = startingWindowRecord != null ? startingWindowRecord.mContentView : null;
        if (splashScreenView != null && splashScreenView.isCopyable()) {
            splashScreenViewParcelable = new SplashScreenView.SplashScreenViewParcelable(splashScreenView);
            splashScreenViewParcelable.setClientCallback(new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda0
                public final void onResult(Bundle bundle) {
                    StartingSurfaceDrawer.this.lambda$copySplashScreenView$4(i, bundle);
                }
            }));
            splashScreenView.onCopied();
            this.mAnimatedSplashScreenSurfaceHosts.append(i, splashScreenView.getSurfaceHost());
        }
        if (DEBUG_SPLASH_SCREEN) {
            String str = TAG;
            Slog.v(str, "Copying splash screen window view for task: " + i + " parcelable: " + splashScreenViewParcelable);
        }
        ActivityTaskManager.getInstance().onSplashScreenViewCopyFinished(i, splashScreenViewParcelable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$copySplashScreenView$4(final int i, Bundle bundle) {
        this.mSplashScreenExecutor.execute(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                StartingSurfaceDrawer.this.lambda$copySplashScreenView$3(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$copySplashScreenView$3(int i) {
        onAppSplashScreenViewRemoved(i, false);
    }

    public void onAppSplashScreenViewRemoved(int i) {
        onAppSplashScreenViewRemoved(i, true);
    }

    private void onAppSplashScreenViewRemoved(int i, boolean z) {
        final SurfaceControlViewHost surfaceControlViewHost = this.mAnimatedSplashScreenSurfaceHosts.get(i);
        if (surfaceControlViewHost == null) {
            return;
        }
        this.mAnimatedSplashScreenSurfaceHosts.remove(i);
        if (DEBUG_SPLASH_SCREEN) {
            String str = z ? "Server cleaned up" : "App removed";
            String str2 = TAG;
            Slog.v(str2, str + "the splash screen. Releasing SurfaceControlViewHost for task:" + i);
        }
        surfaceControlViewHost.getView().post(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                surfaceControlViewHost.release();
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0054  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean addWindow(int i, IBinder iBinder, View view, WindowManager windowManager, WindowManager.LayoutParams layoutParams, @StartingWindowInfo.StartingWindowType int i2) {
        boolean z = false;
        try {
            try {
                Trace.traceBegin(32L, "addRootView");
                windowManager.addView(view, layoutParams);
                Trace.traceEnd(32L);
            } catch (WindowManager.BadTokenException e) {
                String str = TAG;
                Slog.w(str, iBinder + " already running, starting window not displayed. " + e.getMessage());
                Trace.traceEnd(32L);
                if (view != null && view.getParent() == null) {
                    Slog.w(str, "view not successfully added to wm, removing view");
                }
            }
            if (view != null && view.getParent() == null) {
                Slog.w(TAG, "view not successfully added to wm, removing view");
                windowManager.removeViewImmediate(view);
                if (z) {
                }
                return z;
            }
            z = true;
            if (z) {
                lambda$makeTaskSnapshotWindow$2(i);
                saveSplashScreenRecord(iBinder, i, view, i2);
            }
            return z;
        } catch (Throwable th) {
            Trace.traceEnd(32L);
            if (view != null && view.getParent() == null) {
                Slog.w(TAG, "view not successfully added to wm, removing view");
                windowManager.removeViewImmediate(view);
            }
            throw th;
        }
    }

    private void saveSplashScreenRecord(IBinder iBinder, int i, View view, @StartingWindowInfo.StartingWindowType int i2) {
        this.mStartingWindowRecords.put(i, new StartingWindowRecord(iBinder, view, null, i2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: removeWindowNoAnimate */
    public void lambda$makeTaskSnapshotWindow$2(int i) {
        removeWindowSynced(i, null, null, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onImeDrawnOnTask(int i) {
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null && startingWindowRecord.mTaskSnapshotWindow != null && startingWindowRecord.mTaskSnapshotWindow.hasImeSurface()) {
            startingWindowRecord.mTaskSnapshotWindow.removeImmediately();
        }
        this.mStartingWindowRecords.remove(i);
    }

    protected void removeWindowSynced(final int i, SurfaceControl surfaceControl, Rect rect, boolean z) {
        final StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null) {
            if (startingWindowRecord.mDecorView != null) {
                if (DEBUG_SPLASH_SCREEN) {
                    String str = TAG;
                    Slog.v(str, "Removing splash screen window for task: " + i);
                }
                if (startingWindowRecord.mContentView == null) {
                    Slog.e(TAG, "Found empty splash screen, remove!");
                    removeWindowInner(startingWindowRecord.mDecorView, false);
                } else if (startingWindowRecord.mSuggestType == 4) {
                    removeWindowInner(startingWindowRecord.mDecorView, false);
                } else if (z) {
                    this.mSplashscreenContentDrawer.applyExitAnimation(startingWindowRecord.mContentView, surfaceControl, rect, new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda7
                        @Override // java.lang.Runnable
                        public final void run() {
                            StartingSurfaceDrawer.this.lambda$removeWindowSynced$5(startingWindowRecord);
                        }
                    });
                } else {
                    removeWindowInner(startingWindowRecord.mDecorView, true);
                }
                this.mStartingWindowRecords.remove(i);
            }
            if (startingWindowRecord.mTaskSnapshotWindow == null) {
                return;
            }
            startingWindowRecord.mTaskSnapshotWindow.scheduleRemove(new Runnable() { // from class: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    StartingSurfaceDrawer.this.lambda$removeWindowSynced$6(i);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeWindowSynced$5(StartingWindowRecord startingWindowRecord) {
        removeWindowInner(startingWindowRecord.mDecorView, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeWindowSynced$6(int i) {
        this.mStartingWindowRecords.remove(i);
    }

    private void removeWindowInner(View view, boolean z) {
        if (z) {
            view.setVisibility(8);
        }
        WindowManager windowManager = (WindowManager) view.getContext().getSystemService(WindowManager.class);
        if (windowManager != null) {
            windowManager.removeView(view);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class StartingWindowRecord {
        private final IBinder mAppToken;
        private int mBGColor;
        private SplashScreenView mContentView;
        private final View mDecorView;
        private boolean mSetSplashScreen;
        @StartingWindowInfo.StartingWindowType
        private int mSuggestType;
        private final TaskSnapshotWindow mTaskSnapshotWindow;

        StartingWindowRecord(IBinder iBinder, View view, TaskSnapshotWindow taskSnapshotWindow, @StartingWindowInfo.StartingWindowType int i) {
            this.mAppToken = iBinder;
            this.mDecorView = view;
            this.mTaskSnapshotWindow = taskSnapshotWindow;
            if (taskSnapshotWindow != null) {
                this.mBGColor = taskSnapshotWindow.getBackgroundColor();
            }
            this.mSuggestType = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setSplashScreenView(SplashScreenView splashScreenView) {
            if (this.mSetSplashScreen) {
                return;
            }
            this.mContentView = splashScreenView;
            this.mSetSplashScreen = true;
        }
    }
}
