package com.android.p019wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.TaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.Trace;
import android.os.UserHandle;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Choreographer;
import android.view.Display;
import android.view.SurfaceControlViewHost;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.widget.FrameLayout;
import android.window.SplashScreenView;
import android.window.StartingWindowInfo;
import android.window.StartingWindowRemovalInfo;
import android.window.TaskSnapshot;
import com.android.internal.R;
import com.android.internal.protolog.common.ProtoLog;
import com.android.internal.util.ContrastColorUtil;
import com.android.launcher3.icons.IconProvider;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.common.TransactionPool;
import com.android.p019wm.shell.common.annotations.ShellSplashscreenThread;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.startingsurface.StartingSurface;
import com.nothing.NothingExManager;
import java.util.Objects;
import java.util.function.Supplier;

@ShellSplashscreenThread
/* renamed from: com.android.wm.shell.startingsurface.StartingSurfaceDrawer */
public class StartingSurfaceDrawer {
    private static final boolean DBG = Build.IS_DEBUGGABLE;
    private static final int LIGHT_BARS_MASK = 24;
    static final long MAX_ANIMATION_DURATION = 500;
    static final long MINIMAL_ANIMATION_DURATION = 400;
    private static final String TAG = "ShellStartingWindow";
    static final long TIME_WINDOW_DURATION = 100;
    private final SparseArray<SurfaceControlViewHost> mAnimatedSplashScreenSurfaceHosts = new SparseArray<>(1);
    private Choreographer mChoreographer;
    private final Context mContext;
    private final DisplayManager mDisplayManager;
    private final NothingExManager mNothingManager;
    private final ShellExecutor mSplashScreenExecutor;
    final SplashscreenContentDrawer mSplashscreenContentDrawer;
    final SparseArray<StartingWindowRecord> mStartingWindowRecords = new SparseArray<>();
    private StartingSurface.SysuiProxy mSysuiProxy;
    private final StartingWindowRemovalInfo mTmpRemovalInfo = new StartingWindowRemovalInfo();
    private final WindowManagerGlobal mWindowManagerGlobal;

    public StartingSurfaceDrawer(Context context, ShellExecutor shellExecutor, IconProvider iconProvider, TransactionPool transactionPool) {
        this.mContext = context;
        DisplayManager displayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
        this.mDisplayManager = displayManager;
        this.mSplashScreenExecutor = shellExecutor;
        this.mSplashscreenContentDrawer = new SplashscreenContentDrawer(context, iconProvider, transactionPool);
        shellExecutor.execute(new StartingSurfaceDrawer$$ExternalSyntheticLambda6(this));
        this.mWindowManagerGlobal = WindowManagerGlobal.getInstance();
        displayManager.getDisplay(0);
        this.mNothingManager = (NothingExManager) context.getSystemService("nothing_ex_service");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-wm-shell-startingsurface-StartingSurfaceDrawer */
    public /* synthetic */ void mo51177xd9c02b69() {
        this.mChoreographer = Choreographer.getInstance();
    }

    private Display getDisplay(int i) {
        return this.mDisplayManager.getDisplay(i);
    }

    /* access modifiers changed from: package-private */
    public int getSplashScreenTheme(int i, ActivityInfo activityInfo) {
        if (i != 0) {
            return i;
        }
        if (activityInfo.getThemeResource() != 0) {
            return activityInfo.getThemeResource();
        }
        return 16974563;
    }

    /* access modifiers changed from: package-private */
    public void setSysuiProxy(StartingSurface.SysuiProxy sysuiProxy) {
        this.mSysuiProxy = sysuiProxy;
    }

    /* access modifiers changed from: package-private */
    public void addSplashScreenStartingWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder, int i) {
        ActivityInfo activityInfo;
        SplashScreenViewSupplier splashScreenViewSupplier;
        StartingWindowInfo startingWindowInfo2 = startingWindowInfo;
        int i2 = i;
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo2.taskInfo;
        if (startingWindowInfo2.targetActivityInfo != null) {
            activityInfo = startingWindowInfo2.targetActivityInfo;
        } else {
            activityInfo = runningTaskInfo.topActivityInfo;
        }
        ActivityInfo activityInfo2 = activityInfo;
        if (activityInfo2 != null && activityInfo2.packageName != null) {
            int i3 = runningTaskInfo.displayId;
            int i4 = runningTaskInfo.taskId;
            int splashScreenTheme = getSplashScreenTheme(startingWindowInfo2.splashScreenThemeResId, activityInfo2);
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "addSplashScreen for package: %s with theme: %s for task: %d, suggestType: %d", new Object[]{activityInfo2.packageName, Integer.toHexString(splashScreenTheme), Integer.valueOf(i4), Integer.valueOf(i)});
            if (DBG) {
                Slog.d("ShellStartingWindow", "[IconPack] StartingSurfaceDrawer addSplashScreenStartingWindow windowInfo.splashScreenThemeResId:" + Integer.toHexString(startingWindowInfo2.splashScreenThemeResId) + ", theme:" + Integer.toHexString(splashScreenTheme));
            }
            Display display = getDisplay(i3);
            if (display != null) {
                Context createDisplayContext = i3 == 0 ? this.mContext : this.mContext.createDisplayContext(display);
                if (createDisplayContext != null) {
                    if (splashScreenTheme != createDisplayContext.getThemeResId()) {
                        try {
                            createDisplayContext = createDisplayContext.createPackageContextAsUser(activityInfo2.packageName, 4, UserHandle.of(runningTaskInfo.userId));
                            createDisplayContext.setTheme(splashScreenTheme);
                        } catch (PackageManager.NameNotFoundException e) {
                            Slog.w("ShellStartingWindow", "Failed creating package context with package name " + activityInfo2.packageName + " for user " + runningTaskInfo.userId, e);
                            return;
                        }
                    }
                    Configuration configuration = runningTaskInfo.getConfiguration();
                    if (configuration.diffPublicOnly(createDisplayContext.getResources().getConfiguration()) != 0) {
                        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "addSplashScreen: creating context based on task Configuration %s", new Object[]{configuration});
                        Context createConfigurationContext = createDisplayContext.createConfigurationContext(configuration);
                        createConfigurationContext.setTheme(splashScreenTheme);
                        TypedArray obtainStyledAttributes = createConfigurationContext.obtainStyledAttributes(R.styleable.Window);
                        int resourceId = obtainStyledAttributes.getResourceId(1, 0);
                        if (resourceId != 0) {
                            try {
                                if (createConfigurationContext.getDrawable(resourceId) != null) {
                                    ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "addSplashScreen: apply overrideConfig %s", new Object[]{configuration});
                                    createDisplayContext = createConfigurationContext;
                                }
                            } catch (Resources.NotFoundException e2) {
                                Slog.w("ShellStartingWindow", "failed creating starting window for overrideConfig at taskId: " + i4, e2);
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
                    TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(R.styleable.Window);
                    int i5 = obtainStyledAttributes2.getBoolean(14, false) ? 17891584 : 16843008;
                    if (i2 != 4 || obtainStyledAttributes2.getBoolean(33, false)) {
                        i5 |= Integer.MIN_VALUE;
                    }
                    layoutParams.layoutInDisplayCutoutMode = obtainStyledAttributes2.getInt(50, layoutParams.layoutInDisplayCutoutMode);
                    layoutParams.windowAnimations = obtainStyledAttributes2.getResourceId(8, 0);
                    obtainStyledAttributes2.recycle();
                    if (i3 == 0 && startingWindowInfo2.isKeyguardOccluded) {
                        i5 |= 524288;
                    }
                    layoutParams.flags = 131096 | i5;
                    layoutParams.token = iBinder;
                    layoutParams.packageName = activityInfo2.packageName;
                    layoutParams.privateFlags |= 16;
                    if (!context.getResources().getCompatibilityInfo().supportsScreen()) {
                        layoutParams.privateFlags |= 128;
                    }
                    layoutParams.setTitle("Splash Screen " + activityInfo2.packageName);
                    SplashScreenViewSupplier splashScreenViewSupplier2 = new SplashScreenViewSupplier();
                    FrameLayout frameLayout = new FrameLayout(this.mSplashscreenContentDrawer.createViewContextWrapper(context));
                    frameLayout.setPadding(0, 0, 0, 0);
                    frameLayout.setFitsSystemWindows(false);
                    FrameLayout frameLayout2 = frameLayout;
                    SplashScreenViewSupplier splashScreenViewSupplier3 = splashScreenViewSupplier2;
                    Display display2 = display;
                    String str = "ShellStartingWindow";
                    StartingSurfaceDrawer$$ExternalSyntheticLambda0 startingSurfaceDrawer$$ExternalSyntheticLambda0 = new StartingSurfaceDrawer$$ExternalSyntheticLambda0(this, splashScreenViewSupplier2, i4, iBinder, frameLayout2);
                    StartingSurface.SysuiProxy sysuiProxy = this.mSysuiProxy;
                    if (sysuiProxy != null) {
                        sysuiProxy.requestTopUi(true, str);
                    }
                    Drawable icon = this.mNothingManager.getIcon(new ComponentName(activityInfo2.packageName, ""));
                    if (icon != null) {
                        SplashscreenContentDrawer splashscreenContentDrawer = this.mSplashscreenContentDrawer;
                        Objects.requireNonNull(splashScreenViewSupplier3);
                        splashScreenViewSupplier = splashScreenViewSupplier3;
                        StartingSurfaceDrawer$$ExternalSyntheticLambda1 startingSurfaceDrawer$$ExternalSyntheticLambda1 = new StartingSurfaceDrawer$$ExternalSyntheticLambda1(splashScreenViewSupplier);
                        Objects.requireNonNull(splashScreenViewSupplier);
                        splashscreenContentDrawer.createContentView(context, i, startingWindowInfo, startingSurfaceDrawer$$ExternalSyntheticLambda1, new StartingSurfaceDrawer$$ExternalSyntheticLambda2(splashScreenViewSupplier), icon);
                    } else {
                        splashScreenViewSupplier = splashScreenViewSupplier3;
                        SplashscreenContentDrawer splashscreenContentDrawer2 = this.mSplashscreenContentDrawer;
                        Objects.requireNonNull(splashScreenViewSupplier);
                        StartingSurfaceDrawer$$ExternalSyntheticLambda1 startingSurfaceDrawer$$ExternalSyntheticLambda12 = new StartingSurfaceDrawer$$ExternalSyntheticLambda1(splashScreenViewSupplier);
                        Objects.requireNonNull(splashScreenViewSupplier);
                        splashscreenContentDrawer2.createContentView(context, i, startingWindowInfo, startingSurfaceDrawer$$ExternalSyntheticLambda12, new StartingSurfaceDrawer$$ExternalSyntheticLambda2(splashScreenViewSupplier));
                    }
                    try {
                        if (addWindow(i4, iBinder, frameLayout2, display2, layoutParams, i)) {
                            this.mChoreographer.postCallback(2, startingSurfaceDrawer$$ExternalSyntheticLambda0, (Object) null);
                            StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i4);
                            startingWindowRecord.parseAppSystemBarColor(context);
                            final SplashScreenView splashScreenView = splashScreenViewSupplier.get();
                            if (i2 != 4) {
                                splashScreenView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                                    public void onViewDetachedFromWindow(View view) {
                                    }

                                    public void onViewAttachedToWindow(View view) {
                                        splashScreenView.getWindowInsetsController().setSystemBarsAppearance(ContrastColorUtil.isColorLight(splashScreenView.getInitBackgroundColor()) ? 24 : 0, 24);
                                    }
                                });
                            }
                            int unused = startingWindowRecord.mBGColor = splashScreenView.getInitBackgroundColor();
                            return;
                        }
                        SplashScreenView splashScreenView2 = splashScreenViewSupplier.get();
                        if (splashScreenView2.getSurfaceHost() != null) {
                            SplashScreenView.releaseIconHost(splashScreenView2.getSurfaceHost());
                        }
                    } catch (RuntimeException e3) {
                        Slog.w(str, "failed creating starting window at taskId: " + i4, e3);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addSplashScreenStartingWindow$1$com-android-wm-shell-startingsurface-StartingSurfaceDrawer */
    public /* synthetic */ void mo51173xac683a44(SplashScreenViewSupplier splashScreenViewSupplier, int i, IBinder iBinder, FrameLayout frameLayout) {
        Trace.traceBegin(32, "addSplashScreenView");
        SplashScreenView splashScreenView = splashScreenViewSupplier.get();
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null && iBinder == startingWindowRecord.mAppToken) {
            if (splashScreenView != null) {
                try {
                    frameLayout.addView(splashScreenView);
                } catch (RuntimeException e) {
                    Slog.w("ShellStartingWindow", "failed set content view to starting window at taskId: " + i, e);
                    splashScreenView = null;
                }
            }
            startingWindowRecord.setSplashScreenView(splashScreenView);
        }
        Trace.traceEnd(32);
    }

    /* access modifiers changed from: package-private */
    public int getStartingWindowBackgroundColorForTask(int i) {
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord == null) {
            return 0;
        }
        return startingWindowRecord.mBGColor;
    }

    /* renamed from: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$SplashScreenViewSupplier */
    private static class SplashScreenViewSupplier implements Supplier<SplashScreenView> {
        private boolean mIsViewSet;
        private Runnable mUiThreadInitTask;
        private SplashScreenView mView;

        private SplashScreenViewSupplier() {
        }

        /* access modifiers changed from: package-private */
        public void setView(SplashScreenView splashScreenView) {
            synchronized (this) {
                this.mView = splashScreenView;
                this.mIsViewSet = true;
                notify();
            }
        }

        /* access modifiers changed from: package-private */
        public void setUiThreadInitTask(Runnable runnable) {
            synchronized (this) {
                this.mUiThreadInitTask = runnable;
            }
        }

        /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
        /* JADX WARNING: Missing exception handler attribute for start block: B:1:0x0001 */
        /* JADX WARNING: Removed duplicated region for block: B:1:0x0001 A[LOOP:0: B:1:0x0001->B:16:0x0001, LOOP_START, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public android.window.SplashScreenView get() {
            /*
                r1 = this;
                monitor-enter(r1)
            L_0x0001:
                boolean r0 = r1.mIsViewSet     // Catch:{ all -> 0x0017 }
                if (r0 != 0) goto L_0x0009
                r1.wait()     // Catch:{ InterruptedException -> 0x0001 }
                goto L_0x0001
            L_0x0009:
                java.lang.Runnable r0 = r1.mUiThreadInitTask     // Catch:{ all -> 0x0017 }
                if (r0 == 0) goto L_0x0013
                r0.run()     // Catch:{ all -> 0x0017 }
                r0 = 0
                r1.mUiThreadInitTask = r0     // Catch:{ all -> 0x0017 }
            L_0x0013:
                android.window.SplashScreenView r0 = r1.mView     // Catch:{ all -> 0x0017 }
                monitor-exit(r1)     // Catch:{ all -> 0x0017 }
                return r0
            L_0x0017:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x0017 }
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.startingsurface.StartingSurfaceDrawer.SplashScreenViewSupplier.get():android.window.SplashScreenView");
        }
    }

    /* access modifiers changed from: package-private */
    public int estimateTaskBackgroundColor(TaskInfo taskInfo) {
        if (taskInfo.topActivityInfo == null) {
            return 0;
        }
        ActivityInfo activityInfo = taskInfo.topActivityInfo;
        String str = activityInfo.packageName;
        int i = taskInfo.userId;
        try {
            Context createPackageContextAsUser = this.mContext.createPackageContextAsUser(str, 4, UserHandle.of(i));
            try {
                String splashScreenTheme = ActivityThread.getPackageManager().getSplashScreenTheme(str, i);
                int splashScreenTheme2 = getSplashScreenTheme(splashScreenTheme != null ? createPackageContextAsUser.getResources().getIdentifier(splashScreenTheme, (String) null, (String) null) : 0, activityInfo);
                if (splashScreenTheme2 != createPackageContextAsUser.getThemeResId()) {
                    createPackageContextAsUser.setTheme(splashScreenTheme2);
                }
                return this.mSplashscreenContentDrawer.estimateTaskBackgroundColor(createPackageContextAsUser);
            } catch (RemoteException | RuntimeException e) {
                Slog.w("ShellStartingWindow", "failed get starting window background color at taskId: " + taskInfo.taskId, e);
                return 0;
            }
        } catch (PackageManager.NameNotFoundException e2) {
            Slog.w("ShellStartingWindow", "Failed creating package context with package name " + str + " for user " + taskInfo.userId, e2);
            return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public void makeTaskSnapshotWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder, TaskSnapshot taskSnapshot) {
        int i = startingWindowInfo.taskInfo.taskId;
        mo51176x9a1aa546(i);
        TaskSnapshotWindow create = TaskSnapshotWindow.create(startingWindowInfo, iBinder, taskSnapshot, this.mSplashScreenExecutor, new StartingSurfaceDrawer$$ExternalSyntheticLambda7(this, i));
        if (create != null) {
            this.mStartingWindowRecords.put(i, new StartingWindowRecord(iBinder, (View) null, create, 2));
        }
    }

    public void removeStartingWindow(StartingWindowRemovalInfo startingWindowRemovalInfo) {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "Task start finish, remove starting surface for task: %d", new Object[]{Integer.valueOf(startingWindowRemovalInfo.taskId)});
        removeWindowSynced(startingWindowRemovalInfo, false);
    }

    public void clearAllWindows() {
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "Clear all starting windows immediately", new Object[0]);
        int size = this.mStartingWindowRecords.size();
        int[] iArr = new int[size];
        int i = size - 1;
        for (int i2 = i; i2 >= 0; i2--) {
            iArr[i2] = this.mStartingWindowRecords.keyAt(i2);
        }
        while (i >= 0) {
            mo51176x9a1aa546(iArr[i]);
            i--;
        }
    }

    public void copySplashScreenView(int i) {
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        SplashScreenView.SplashScreenViewParcelable splashScreenViewParcelable = null;
        SplashScreenView access$300 = startingWindowRecord != null ? startingWindowRecord.mContentView : null;
        if (access$300 != null && access$300.isCopyable()) {
            splashScreenViewParcelable = new SplashScreenView.SplashScreenViewParcelable(access$300);
            splashScreenViewParcelable.setClientCallback(new RemoteCallback(new StartingSurfaceDrawer$$ExternalSyntheticLambda5(this, i)));
            access$300.onCopied();
            this.mAnimatedSplashScreenSurfaceHosts.append(i, access$300.getSurfaceHost());
        }
        ShellProtoLogGroup shellProtoLogGroup = ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW;
        Object[] objArr = new Object[2];
        boolean z = false;
        objArr[0] = Integer.valueOf(i);
        if (splashScreenViewParcelable != null) {
            z = true;
        }
        objArr[1] = Boolean.valueOf(z);
        ProtoLog.v(shellProtoLogGroup, "Copying splash screen window view for task: %d with parcelable %b", objArr);
        ActivityTaskManager.getInstance().onSplashScreenViewCopyFinished(i, splashScreenViewParcelable);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$copySplashScreenView$4$com-android-wm-shell-startingsurface-StartingSurfaceDrawer */
    public /* synthetic */ void mo51175xa34a6c3e(int i, Bundle bundle) {
        this.mSplashScreenExecutor.execute(new StartingSurfaceDrawer$$ExternalSyntheticLambda3(this, i));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$copySplashScreenView$3$com-android-wm-shell-startingsurface-StartingSurfaceDrawer */
    public /* synthetic */ void mo51174x9d46a0df(int i) {
        onAppSplashScreenViewRemoved(i, false);
    }

    public void onAppSplashScreenViewRemoved(int i) {
        onAppSplashScreenViewRemoved(i, true);
    }

    private void onAppSplashScreenViewRemoved(int i, boolean z) {
        SurfaceControlViewHost surfaceControlViewHost = this.mAnimatedSplashScreenSurfaceHosts.get(i);
        if (surfaceControlViewHost != null) {
            this.mAnimatedSplashScreenSurfaceHosts.remove(i);
            ShellProtoLogGroup shellProtoLogGroup = ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW;
            Object[] objArr = new Object[2];
            objArr[0] = z ? "Server cleaned up" : "App removed";
            objArr[1] = Integer.valueOf(i);
            ProtoLog.v(shellProtoLogGroup, "%s the splash screen. Releasing SurfaceControlViewHost for task: %d", objArr);
            SplashScreenView.releaseIconHost(surfaceControlViewHost);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0064, code lost:
        if (r19.getParent() != null) goto L_0x0067;
     */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0069  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean addWindow(int r17, android.os.IBinder r18, android.view.View r19, android.view.Display r20, android.view.WindowManager.LayoutParams r21, int r22) {
        /*
            r16 = this;
            r1 = r16
            r2 = r18
            r9 = r19
            java.lang.String r10 = "view not successfully added to wm, removing view"
            java.lang.String r11 = "ShellStartingWindow"
            android.content.Context r0 = r19.getContext()
            r12 = 0
            r13 = 32
            r15 = 1
            java.lang.String r3 = "addRootView"
            android.os.Trace.traceBegin(r13, r3)     // Catch:{ BadTokenException -> 0x003e }
            android.view.WindowManagerGlobal r3 = r1.mWindowManagerGlobal     // Catch:{ BadTokenException -> 0x003e }
            r7 = 0
            int r8 = r0.getUserId()     // Catch:{ BadTokenException -> 0x003e }
            r4 = r19
            r5 = r21
            r6 = r20
            r3.addView(r4, r5, r6, r7, r8)     // Catch:{ BadTokenException -> 0x003e }
            android.os.Trace.traceEnd(r13)
            android.view.ViewParent r0 = r19.getParent()
            if (r0 != 0) goto L_0x003a
        L_0x0031:
            android.util.Slog.w(r11, r10)
            android.view.WindowManagerGlobal r0 = r1.mWindowManagerGlobal
            r0.removeView(r9, r15)
            goto L_0x0067
        L_0x003a:
            r12 = r15
            goto L_0x0067
        L_0x003c:
            r0 = move-exception
            goto L_0x0074
        L_0x003e:
            r0 = move-exception
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x003c }
            r3.<init>()     // Catch:{ all -> 0x003c }
            java.lang.StringBuilder r3 = r3.append((java.lang.Object) r2)     // Catch:{ all -> 0x003c }
            java.lang.String r4 = " already running, starting window not displayed. "
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)     // Catch:{ all -> 0x003c }
            java.lang.String r0 = r0.getMessage()     // Catch:{ all -> 0x003c }
            java.lang.StringBuilder r0 = r3.append((java.lang.String) r0)     // Catch:{ all -> 0x003c }
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x003c }
            android.util.Slog.w(r11, r0)     // Catch:{ all -> 0x003c }
            android.os.Trace.traceEnd(r13)
            android.view.ViewParent r0 = r19.getParent()
            if (r0 != 0) goto L_0x0067
            goto L_0x0031
        L_0x0067:
            if (r12 == 0) goto L_0x0073
            r16.mo51176x9a1aa546(r17)
            r3 = r17
            r4 = r22
            r1.saveSplashScreenRecord(r2, r3, r9, r4)
        L_0x0073:
            return r12
        L_0x0074:
            android.os.Trace.traceEnd(r13)
            android.view.ViewParent r2 = r19.getParent()
            if (r2 != 0) goto L_0x0085
            android.util.Slog.w(r11, r10)
            android.view.WindowManagerGlobal r1 = r1.mWindowManagerGlobal
            r1.removeView(r9, r15)
        L_0x0085:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.startingsurface.StartingSurfaceDrawer.addWindow(int, android.os.IBinder, android.view.View, android.view.Display, android.view.WindowManager$LayoutParams, int):boolean");
    }

    /* access modifiers changed from: package-private */
    public void saveSplashScreenRecord(IBinder iBinder, int i, View view, int i2) {
        this.mStartingWindowRecords.put(i, new StartingWindowRecord(iBinder, view, (TaskSnapshotWindow) null, i2));
    }

    /* access modifiers changed from: private */
    /* renamed from: removeWindowNoAnimate */
    public void mo51176x9a1aa546(int i) {
        this.mTmpRemovalInfo.taskId = i;
        removeWindowSynced(this.mTmpRemovalInfo, true);
    }

    /* access modifiers changed from: package-private */
    public void onImeDrawnOnTask(int i) {
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null && startingWindowRecord.mTaskSnapshotWindow != null && startingWindowRecord.mTaskSnapshotWindow.hasImeSurface()) {
            mo51176x9a1aa546(i);
        }
    }

    /* access modifiers changed from: protected */
    public void removeWindowSynced(StartingWindowRemovalInfo startingWindowRemovalInfo, boolean z) {
        int i = startingWindowRemovalInfo.taskId;
        StartingWindowRecord startingWindowRecord = this.mStartingWindowRecords.get(i);
        if (startingWindowRecord != null) {
            if (startingWindowRecord.mDecorView != null) {
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "Removing splash screen window for task: %d", new Object[]{Integer.valueOf(i)});
                if (startingWindowRecord.mContentView != null) {
                    startingWindowRecord.clearSystemBarColor();
                    if (z || startingWindowRecord.mSuggestType == 4) {
                        removeWindowInner(startingWindowRecord.mDecorView, false);
                    } else if (startingWindowRemovalInfo.playRevealAnimation) {
                        this.mSplashscreenContentDrawer.applyExitAnimation(startingWindowRecord.mContentView, startingWindowRemovalInfo.windowAnimationLeash, startingWindowRemovalInfo.mainFrame, new StartingSurfaceDrawer$$ExternalSyntheticLambda4(this, startingWindowRecord), startingWindowRecord.mCreateTime);
                    } else {
                        removeWindowInner(startingWindowRecord.mDecorView, true);
                    }
                } else {
                    Slog.e("ShellStartingWindow", "Found empty splash screen, remove!");
                    removeWindowInner(startingWindowRecord.mDecorView, false);
                }
            }
            if (startingWindowRecord.mTaskSnapshotWindow != null) {
                ProtoLog.v(ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW, "Removing task snapshot window for %d", new Object[]{Integer.valueOf(i)});
                if (z) {
                    startingWindowRecord.mTaskSnapshotWindow.removeImmediately();
                } else {
                    startingWindowRecord.mTaskSnapshotWindow.scheduleRemove(startingWindowRemovalInfo.deferRemoveForIme);
                }
            }
            this.mStartingWindowRecords.remove(i);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeWindowSynced$5$com-android-wm-shell-startingsurface-StartingSurfaceDrawer */
    public /* synthetic */ void mo51178x431af55c(StartingWindowRecord startingWindowRecord) {
        removeWindowInner(startingWindowRecord.mDecorView, true);
    }

    private void removeWindowInner(View view, boolean z) {
        StartingSurface.SysuiProxy sysuiProxy = this.mSysuiProxy;
        if (sysuiProxy != null) {
            sysuiProxy.requestTopUi(false, "ShellStartingWindow");
        }
        if (z) {
            view.setVisibility(8);
        }
        this.mWindowManagerGlobal.removeView(view, false);
    }

    /* renamed from: com.android.wm.shell.startingsurface.StartingSurfaceDrawer$StartingWindowRecord */
    private static class StartingWindowRecord {
        /* access modifiers changed from: private */
        public final IBinder mAppToken;
        /* access modifiers changed from: private */
        public int mBGColor;
        /* access modifiers changed from: private */
        public SplashScreenView mContentView;
        /* access modifiers changed from: private */
        public final long mCreateTime;
        /* access modifiers changed from: private */
        public final View mDecorView;
        private boolean mDrawsSystemBarBackgrounds;
        private boolean mSetSplashScreen;
        /* access modifiers changed from: private */
        public int mSuggestType;
        private int mSystemBarAppearance;
        /* access modifiers changed from: private */
        public final TaskSnapshotWindow mTaskSnapshotWindow;

        StartingWindowRecord(IBinder iBinder, View view, TaskSnapshotWindow taskSnapshotWindow, int i) {
            this.mAppToken = iBinder;
            this.mDecorView = view;
            this.mTaskSnapshotWindow = taskSnapshotWindow;
            if (taskSnapshotWindow != null) {
                this.mBGColor = taskSnapshotWindow.getBackgroundColor();
            }
            this.mSuggestType = i;
            this.mCreateTime = SystemClock.uptimeMillis();
        }

        /* access modifiers changed from: private */
        public void setSplashScreenView(SplashScreenView splashScreenView) {
            if (!this.mSetSplashScreen) {
                this.mContentView = splashScreenView;
                this.mSetSplashScreen = true;
            }
        }

        /* access modifiers changed from: private */
        public void parseAppSystemBarColor(Context context) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.styleable.Window);
            this.mDrawsSystemBarBackgrounds = obtainStyledAttributes.getBoolean(33, false);
            if (obtainStyledAttributes.getBoolean(45, false)) {
                this.mSystemBarAppearance |= 8;
            }
            if (obtainStyledAttributes.getBoolean(48, false)) {
                this.mSystemBarAppearance |= 16;
            }
            obtainStyledAttributes.recycle();
        }

        /* access modifiers changed from: private */
        public void clearSystemBarColor() {
            View view = this.mDecorView;
            if (view != null) {
                if (view.getLayoutParams() instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mDecorView.getLayoutParams();
                    if (this.mDrawsSystemBarBackgrounds) {
                        layoutParams.flags |= Integer.MIN_VALUE;
                    } else {
                        layoutParams.flags &= Integer.MAX_VALUE;
                    }
                    this.mDecorView.setLayoutParams(layoutParams);
                }
                if (this.mDecorView.getWindowInsetsController() != null) {
                    this.mDecorView.getWindowInsetsController().setSystemBarsAppearance(this.mSystemBarAppearance, 24);
                }
            }
        }
    }
}
