package com.android.systemui.navigationbar.gestural;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.DeviceConfig;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Choreographer;
import android.view.ISystemGestureExclusionListener;
import android.view.IWindowManager;
import android.view.InputEvent;
import android.view.InputMonitor;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import com.android.internal.policy.GestureNavigationSettingsObserver;
import com.android.internal.util.LatencyTracker;
import com.android.p019wm.shell.back.BackAnimation;
import com.android.systemui.C1894R;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.NavigationEdgeBackPlugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.InputChannelCompat;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.EdgeBackGestureHandlerProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.navigationbar.gestural.EdgeBackGestureHandlerEx;
import java.p026io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class EdgeBackGestureHandler extends CurrentUserTracker implements PluginListener<NavigationEdgeBackPlugin>, ProtoTraceable<SystemUiTraceProto> {
    static final boolean DEBUG_MISSING_GESTURE = false;
    static final String DEBUG_MISSING_GESTURE_TAG = "NoBackGesture";
    private static final int MAX_LONG_PRESS_TIMEOUT = SystemProperties.getInt("gestures.back_timeout", 250);
    private static final int MAX_NUM_LOGGED_GESTURES = 10;
    private static final int MAX_NUM_LOGGED_PREDICTIONS = 10;
    private static final String TAG = "EdgeBackGestureHandler";
    private boolean mAllowGesture;
    /* access modifiers changed from: private */
    public BackAnimation mBackAnimation;
    private final NavigationEdgeBackPlugin.BackCallback mBackCallback;
    private BackGestureTfClassifierProvider mBackGestureTfClassifierProvider;
    private float mBottomGestureHeight;
    /* access modifiers changed from: private */
    public final Context mContext;
    private boolean mDisabledForQuickstep;
    /* access modifiers changed from: private */
    public final int mDisplayId;
    private final Point mDisplaySize = new Point();
    /* access modifiers changed from: private */
    public final PointF mDownPoint = new PointF();
    private NavigationEdgeBackPlugin mEdgeBackPlugin;
    private int mEdgeWidthLeft;
    private int mEdgeWidthRight;
    private final PointF mEndPoint = new PointF();
    /* access modifiers changed from: private */
    public EdgeBackGestureHandlerEx mEx;
    /* access modifiers changed from: private */
    public final Region mExcludeRegion = new Region();
    /* access modifiers changed from: private */
    public final FalsingManager mFalsingManager;
    private final List<ComponentName> mGestureBlockingActivities = new ArrayList();
    /* access modifiers changed from: private */
    public boolean mGestureBlockingActivityRunning;
    private ISystemGestureExclusionListener mGestureExclusionListener = new ISystemGestureExclusionListener.Stub() {
        public void onSystemGestureExclusionChanged(int i, Region region, Region region2) {
            if (i == EdgeBackGestureHandler.this.mDisplayId) {
                EdgeBackGestureHandler.this.mMainExecutor.execute(new EdgeBackGestureHandler$1$$ExternalSyntheticLambda0(this, region, region2));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onSystemGestureExclusionChanged$0$com-android-systemui-navigationbar-gestural-EdgeBackGestureHandler$1 */
        public /* synthetic */ void mo35066x7bfe7a7e(Region region, Region region2) {
            EdgeBackGestureHandler.this.mExcludeRegion.set(region);
            Region access$300 = EdgeBackGestureHandler.this.mUnrestrictedExcludeRegion;
            if (region2 != null) {
                region = region2;
            }
            access$300.set(region);
        }
    };
    private LogArray mGestureLogInsideInsets;
    private LogArray mGestureLogOutsideInsets;
    private final GestureNavigationSettingsObserver mGestureNavigationSettingsObserver;
    /* access modifiers changed from: private */
    public boolean mInRejectedExclusion;
    private InputChannelCompat.InputEventReceiver mInputEventReceiver;
    private InputMonitor mInputMonitor;
    private boolean mIsAttached;
    private boolean mIsBackGestureAllowed;
    private boolean mIsEnabled;
    private boolean mIsGesturalModeEnabled;
    /* access modifiers changed from: private */
    public boolean mIsInPipMode;
    private boolean mIsNavBarShownTransiently;
    /* access modifiers changed from: private */
    public boolean mIsOnLeftEdge;
    private final LatencyTracker mLatencyTracker;
    private int mLeftInset;
    private boolean mLogGesture;
    private final int mLongPressTimeout;
    private int mMLEnableWidth;
    private float mMLModelThreshold;
    private float mMLResults;
    /* access modifiers changed from: private */
    public final Executor mMainExecutor;
    private final Rect mNavBarOverlayExcludedBounds = new Rect();
    private final NavigationModeController mNavigationModeController;
    private DeviceConfig.OnPropertiesChangedListener mOnPropertiesChangedListener = new DeviceConfig.OnPropertiesChangedListener() {
        public void onPropertiesChanged(DeviceConfig.Properties properties) {
            if (!"systemui".equals(properties.getNamespace())) {
                return;
            }
            if (properties.getKeyset().contains("back_gesture_ml_model_threshold") || properties.getKeyset().contains("use_back_gesture_ml_model") || properties.getKeyset().contains("back_gesture_ml_model_name")) {
                EdgeBackGestureHandler.this.updateMLModelState();
            }
        }
    };
    /* access modifiers changed from: private */
    public final OverviewProxyService mOverviewProxyService;
    /* access modifiers changed from: private */
    public String mPackageName;
    private final Rect mPipExcludedBounds = new Rect();
    private final PluginManager mPluginManager;
    private LogArray mPredictionLog;
    private final ProtoTracer mProtoTracer;
    private OverviewProxyService.OverviewProxyListener mQuickSwitchListener = new OverviewProxyService.OverviewProxyListener() {
        public void onPrioritizedRotation(int i) {
            int unused = EdgeBackGestureHandler.this.mStartingQuickstepRotation = i;
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            edgeBackGestureHandler.updateDisabledForQuickstep(edgeBackGestureHandler.mContext.getResources().getConfiguration());
        }
    };
    private int mRightInset;
    /* access modifiers changed from: private */
    public int mStartingQuickstepRotation = -1;
    private Runnable mStateChangeCallback;
    /* access modifiers changed from: private */
    public int mSysUiFlags;
    private final SysUiState mSysUiState;
    private final SysUiState.SysUiStateCallback mSysUiStateCallback;
    private TaskStackChangeListener mTaskStackListener = new TaskStackChangeListener() {
        public void onTaskStackChanged() {
            EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
            boolean unused = edgeBackGestureHandler.mGestureBlockingActivityRunning = edgeBackGestureHandler.isGestureBlockingActivityRunning();
        }

        public void onTaskCreated(int i, ComponentName componentName) {
            if (componentName != null) {
                String unused = EdgeBackGestureHandler.this.mPackageName = componentName.getPackageName();
            } else {
                String unused2 = EdgeBackGestureHandler.this.mPackageName = "_UNKNOWN";
            }
        }

        public void onActivityPinned(String str, int i, int i2, int i3) {
            boolean unused = EdgeBackGestureHandler.this.mIsInPipMode = true;
        }

        public void onActivityUnpinned() {
            boolean unused = EdgeBackGestureHandler.this.mIsInPipMode = false;
        }
    };
    private boolean mThresholdCrossed;
    private float mTouchSlop;
    /* access modifiers changed from: private */
    public final Region mUnrestrictedExcludeRegion = new Region();
    private boolean mUseMLModel;
    private final ViewConfiguration mViewConfiguration;
    private Map<String, Integer> mVocab;
    private final WindowManager mWindowManager;
    private final IWindowManager mWindowManagerService;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    EdgeBackGestureHandler(Context context, OverviewProxyService overviewProxyService, SysUiState sysUiState, PluginManager pluginManager, @Main Executor executor, BroadcastDispatcher broadcastDispatcher, ProtoTracer protoTracer, NavigationModeController navigationModeController, ViewConfiguration viewConfiguration, WindowManager windowManager, IWindowManager iWindowManager, FalsingManager falsingManager, LatencyTracker latencyTracker) {
        super(broadcastDispatcher);
        BroadcastDispatcher broadcastDispatcher2 = broadcastDispatcher;
        this.mThresholdCrossed = false;
        this.mAllowGesture = false;
        this.mLogGesture = false;
        this.mInRejectedExclusion = false;
        this.mPredictionLog = new LogArray(10);
        this.mGestureLogInsideInsets = new LogArray(10);
        this.mGestureLogOutsideInsets = new LogArray(10);
        this.mBackCallback = new NavigationEdgeBackPlugin.BackCallback() {
            public void triggerBack() {
                if (EdgeBackGestureHandler.this.mEx.getIfNeedInterceptBack()) {
                    Log.d(EdgeBackGestureHandler.TAG, "Need to intercept back because of Game mode");
                    EdgeBackGestureHandler.this.mEx.resetBackIntercept();
                    EdgeBackGestureHandler.this.mEx.setBackInterceptTime();
                    EdgeBackGestureHandler.this.mEx.showToast(EdgeBackGestureHandler.this.mMainExecutor, EdgeBackGestureHandler.this.mContext);
                    return;
                }
                EdgeBackGestureHandler.this.mEx.hideToast(EdgeBackGestureHandler.this.mMainExecutor);
                EdgeBackGestureHandler.this.mFalsingManager.isFalseTouch(16);
                int i = 1;
                if (EdgeBackGestureHandler.this.mBackAnimation == null) {
                    boolean unused = EdgeBackGestureHandler.this.sendEvent(0, 4);
                    boolean unused2 = EdgeBackGestureHandler.this.sendEvent(1, 4);
                }
                EdgeBackGestureHandler.this.mOverviewProxyService.notifyBackAction(true, (int) EdgeBackGestureHandler.this.mDownPoint.x, (int) EdgeBackGestureHandler.this.mDownPoint.y, false, !EdgeBackGestureHandler.this.mIsOnLeftEdge);
                EdgeBackGestureHandler edgeBackGestureHandler = EdgeBackGestureHandler.this;
                if (edgeBackGestureHandler.mInRejectedExclusion) {
                    i = 2;
                }
                edgeBackGestureHandler.logGesture(i);
            }

            public void cancelBack() {
                EdgeBackGestureHandler.this.logGesture(4);
                EdgeBackGestureHandler.this.mOverviewProxyService.notifyBackAction(false, (int) EdgeBackGestureHandler.this.mDownPoint.x, (int) EdgeBackGestureHandler.this.mDownPoint.y, false, !EdgeBackGestureHandler.this.mIsOnLeftEdge);
            }
        };
        this.mSysUiStateCallback = new SysUiState.SysUiStateCallback() {
            public void onSystemUiStateChanged(int i) {
                int unused = EdgeBackGestureHandler.this.mSysUiFlags = i;
            }
        };
        this.mContext = context;
        this.mDisplayId = context.getDisplayId();
        this.mMainExecutor = executor;
        this.mOverviewProxyService = overviewProxyService;
        this.mSysUiState = sysUiState;
        this.mPluginManager = pluginManager;
        this.mProtoTracer = protoTracer;
        this.mNavigationModeController = navigationModeController;
        this.mViewConfiguration = viewConfiguration;
        this.mWindowManager = windowManager;
        this.mWindowManagerService = iWindowManager;
        this.mFalsingManager = falsingManager;
        this.mLatencyTracker = latencyTracker;
        ComponentName unflattenFromString = ComponentName.unflattenFromString(context.getString(17040027));
        if (unflattenFromString != null) {
            String packageName = unflattenFromString.getPackageName();
            PackageManager packageManager = context.getPackageManager();
            try {
                Resources resourcesForApplication = packageManager.getResourcesForApplication(packageManager.getApplicationInfo(packageName, 9728));
                int identifier = resourcesForApplication.getIdentifier("gesture_blocking_activities", "array", packageName);
                if (identifier == 0) {
                    Log.e(TAG, "No resource found for gesture-blocking activities");
                } else {
                    for (String unflattenFromString2 : resourcesForApplication.getStringArray(identifier)) {
                        this.mGestureBlockingActivities.add(ComponentName.unflattenFromString(unflattenFromString2));
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Failed to add gesture blocking activities", e);
            }
        }
        this.mLongPressTimeout = Math.min(MAX_LONG_PRESS_TIMEOUT, ViewConfiguration.getLongPressTimeout());
        this.mGestureNavigationSettingsObserver = new GestureNavigationSettingsObserver(this.mContext.getMainThreadHandler(), this.mContext, new EdgeBackGestureHandler$$ExternalSyntheticLambda0(this));
        updateCurrentUserResources();
        this.mEx = (EdgeBackGestureHandlerEx) NTDependencyEx.get(EdgeBackGestureHandlerEx.class);
    }

    public void setStateChangeCallback(Runnable runnable) {
        this.mStateChangeCallback = runnable;
    }

    public void updateCurrentUserResources() {
        Resources resources = this.mNavigationModeController.getCurrentUserContext().getResources();
        this.mEdgeWidthLeft = this.mGestureNavigationSettingsObserver.getLeftSensitivity(resources);
        this.mEdgeWidthRight = this.mGestureNavigationSettingsObserver.getRightSensitivity(resources);
        this.mIsBackGestureAllowed = !this.mGestureNavigationSettingsObserver.areNavigationButtonForcedVisible();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        this.mBottomGestureHeight = TypedValue.applyDimension(1, DeviceConfig.getFloat("systemui", "back_gesture_bottom_height", resources.getDimension(17105357) / displayMetrics.density), displayMetrics);
        int applyDimension = (int) TypedValue.applyDimension(1, 12.0f, displayMetrics);
        this.mMLEnableWidth = applyDimension;
        int i = this.mEdgeWidthRight;
        if (applyDimension > i) {
            this.mMLEnableWidth = i;
        }
        int i2 = this.mMLEnableWidth;
        int i3 = this.mEdgeWidthLeft;
        if (i2 > i3) {
            this.mMLEnableWidth = i3;
        }
        this.mTouchSlop = ((float) this.mViewConfiguration.getScaledTouchSlop()) * DeviceConfig.getFloat("systemui", "back_gesture_slop_multiplier", 0.75f);
    }

    public void updateNavigationBarOverlayExcludeRegion(Rect rect) {
        this.mNavBarOverlayExcludedBounds.set(rect);
    }

    /* access modifiers changed from: private */
    public void onNavigationSettingsChanged() {
        boolean isHandlingGestures = isHandlingGestures();
        updateCurrentUserResources();
        if (this.mStateChangeCallback != null && isHandlingGestures != isHandlingGestures()) {
            this.mStateChangeCallback.run();
        }
    }

    public void onUserSwitched(int i) {
        updateIsEnabled();
        updateCurrentUserResources();
    }

    public void onNavBarAttached() {
        this.mIsAttached = true;
        this.mProtoTracer.add(this);
        this.mOverviewProxyService.addCallback(this.mQuickSwitchListener);
        this.mSysUiState.addCallback(this.mSysUiStateCallback);
        updateIsEnabled();
        startTracking();
    }

    public void onNavBarDetached() {
        this.mIsAttached = false;
        this.mProtoTracer.remove(this);
        this.mOverviewProxyService.removeCallback(this.mQuickSwitchListener);
        this.mSysUiState.removeCallback(this.mSysUiStateCallback);
        updateIsEnabled();
        stopTracking();
    }

    public void onNavigationModeChanged(int i) {
        this.mIsGesturalModeEnabled = QuickStepContract.isGesturalMode(i);
        updateIsEnabled();
        updateCurrentUserResources();
    }

    public void onNavBarTransientStateChanged(boolean z) {
        this.mIsNavBarShownTransiently = z;
    }

    private void disposeInputChannel() {
        InputChannelCompat.InputEventReceiver inputEventReceiver = this.mInputEventReceiver;
        if (inputEventReceiver != null) {
            inputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        InputMonitor inputMonitor = this.mInputMonitor;
        if (inputMonitor != null) {
            inputMonitor.dispose();
            this.mInputMonitor = null;
        }
    }

    private void updateIsEnabled() {
        boolean z = this.mIsAttached && this.mIsGesturalModeEnabled;
        if (z != this.mIsEnabled) {
            this.mIsEnabled = z;
            disposeInputChannel();
            NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
            if (navigationEdgeBackPlugin != null) {
                navigationEdgeBackPlugin.onDestroy();
                this.mEdgeBackPlugin = null;
            }
            if (!this.mIsEnabled) {
                this.mGestureNavigationSettingsObserver.unregister();
                this.mPluginManager.removePluginListener(this);
                TaskStackChangeListeners.getInstance().unregisterTaskStackListener(this.mTaskStackListener);
                DeviceConfig.removeOnPropertiesChangedListener(this.mOnPropertiesChangedListener);
                try {
                    this.mWindowManagerService.unregisterSystemGestureExclusionListener(this.mGestureExclusionListener, this.mDisplayId);
                } catch (RemoteException | IllegalArgumentException e) {
                    Log.e(TAG, "Failed to unregister window manager callbacks", e);
                }
            } else {
                this.mGestureNavigationSettingsObserver.register();
                updateDisplaySize();
                TaskStackChangeListeners.getInstance().registerTaskStackListener(this.mTaskStackListener);
                Executor executor = this.mMainExecutor;
                Objects.requireNonNull(executor);
                DeviceConfig.addOnPropertiesChangedListener("systemui", new EdgeBackGestureHandler$$ExternalSyntheticLambda1(executor), this.mOnPropertiesChangedListener);
                try {
                    this.mWindowManagerService.registerSystemGestureExclusionListener(this.mGestureExclusionListener, this.mDisplayId);
                } catch (RemoteException | IllegalArgumentException e2) {
                    Log.e(TAG, "Failed to register window manager callbacks", e2);
                }
                this.mInputMonitor = InputManager.getInstance().monitorGestureInput("edge-swipe", this.mDisplayId);
                this.mInputEventReceiver = new InputChannelCompat.InputEventReceiver(this.mInputMonitor.getInputChannel(), Looper.getMainLooper(), Choreographer.getInstance(), new EdgeBackGestureHandler$$ExternalSyntheticLambda2(this));
                setEdgeBackPlugin(new NavigationBarEdgePanel(this.mContext, this.mBackAnimation, this.mLatencyTracker));
                this.mPluginManager.addPluginListener(this, NavigationEdgeBackPlugin.class, false);
            }
            updateMLModelState();
        }
    }

    public void onPluginConnected(NavigationEdgeBackPlugin navigationEdgeBackPlugin, Context context) {
        setEdgeBackPlugin(navigationEdgeBackPlugin);
    }

    public void onPluginDisconnected(NavigationEdgeBackPlugin navigationEdgeBackPlugin) {
        setEdgeBackPlugin(new NavigationBarEdgePanel(this.mContext, this.mBackAnimation, this.mLatencyTracker));
    }

    private void setEdgeBackPlugin(NavigationEdgeBackPlugin navigationEdgeBackPlugin) {
        NavigationEdgeBackPlugin navigationEdgeBackPlugin2 = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin2 != null) {
            navigationEdgeBackPlugin2.onDestroy();
        }
        this.mEdgeBackPlugin = navigationEdgeBackPlugin;
        navigationEdgeBackPlugin.setBackCallback(this.mBackCallback);
        this.mEdgeBackPlugin.setLayoutParams(createLayoutParams());
        updateDisplaySize();
    }

    public boolean isHandlingGestures() {
        return this.mIsEnabled && this.mIsBackGestureAllowed;
    }

    public void setPipStashExclusionBounds(Rect rect) {
        this.mPipExcludedBounds.set(rect);
    }

    private WindowManager.LayoutParams createLayoutParams() {
        Resources resources = this.mContext.getResources();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(resources.getDimensionPixelSize(C1894R.dimen.navigation_edge_panel_width), resources.getDimensionPixelSize(C1894R.dimen.navigation_edge_panel_height), 2024, 280, -3);
        layoutParams.accessibilityTitle = this.mContext.getString(C1894R.string.nav_bar_edge_panel);
        layoutParams.windowAnimations = 0;
        layoutParams.privateFlags |= 2097168;
        layoutParams.setTitle(TAG + this.mContext.getDisplayId());
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTrustedOverlay();
        return layoutParams;
    }

    /* access modifiers changed from: private */
    public void onInputEvent(InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            onMotionEvent((MotionEvent) inputEvent);
        }
    }

    /* access modifiers changed from: private */
    public void updateMLModelState() {
        boolean z = this.mIsEnabled && DeviceConfig.getBoolean("systemui", "use_back_gesture_ml_model", false);
        if (z != this.mUseMLModel) {
            if (z) {
                this.mBackGestureTfClassifierProvider = SystemUIFactory.getInstance().createBackGestureTfClassifierProvider(this.mContext.getAssets(), DeviceConfig.getString("systemui", "back_gesture_ml_model_name", "backgesture"));
                this.mMLModelThreshold = DeviceConfig.getFloat("systemui", "back_gesture_ml_model_threshold", 0.9f);
                if (this.mBackGestureTfClassifierProvider.isActive()) {
                    Trace.beginSection("EdgeBackGestureHandler#loadVocab");
                    this.mVocab = this.mBackGestureTfClassifierProvider.loadVocab(this.mContext.getAssets());
                    Trace.endSection();
                    this.mUseMLModel = true;
                    return;
                }
            }
            this.mUseMLModel = false;
            BackGestureTfClassifierProvider backGestureTfClassifierProvider = this.mBackGestureTfClassifierProvider;
            if (backGestureTfClassifierProvider != null) {
                backGestureTfClassifierProvider.release();
                this.mBackGestureTfClassifierProvider = null;
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getBackGesturePredictionsCategory(int r10, int r11, int r12) {
        /*
            r9 = this;
            r0 = -1
            if (r12 != r0) goto L_0x0004
            return r0
        L_0x0004:
            double r1 = (double) r10
            android.graphics.Point r3 = r9.mDisplaySize
            int r3 = r3.x
            double r3 = (double) r3
            r5 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r3 = r3 / r5
            int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            r2 = 2
            r3 = 1
            if (r1 > 0) goto L_0x0015
            r1 = r3
            goto L_0x001c
        L_0x0015:
            android.graphics.Point r1 = r9.mDisplaySize
            int r1 = r1.x
            int r10 = r1 - r10
            r1 = r2
        L_0x001c:
            r4 = 5
            java.lang.Object[] r4 = new java.lang.Object[r4]
            long[] r5 = new long[r3]
            android.graphics.Point r6 = r9.mDisplaySize
            int r6 = r6.x
            long r6 = (long) r6
            r8 = 0
            r5[r8] = r6
            r4[r8] = r5
            long[] r5 = new long[r3]
            long r6 = (long) r10
            r5[r8] = r6
            r4[r3] = r5
            long[] r10 = new long[r3]
            long r5 = (long) r1
            r10[r8] = r5
            r4[r2] = r10
            long[] r10 = new long[r3]
            long r1 = (long) r12
            r10[r8] = r1
            r12 = 3
            r4[r12] = r10
            long[] r10 = new long[r3]
            long r11 = (long) r11
            r10[r8] = r11
            r11 = 4
            r4[r11] = r10
            com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider r10 = r9.mBackGestureTfClassifierProvider
            float r10 = r10.predict(r4)
            r9.mMLResults = r10
            r11 = -1082130432(0xffffffffbf800000, float:-1.0)
            int r11 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1))
            if (r11 != 0) goto L_0x0058
            return r0
        L_0x0058:
            float r9 = r9.mMLModelThreshold
            int r9 = (r10 > r9 ? 1 : (r10 == r9 ? 0 : -1))
            if (r9 < 0) goto L_0x005f
            goto L_0x0060
        L_0x005f:
            r3 = r8
        L_0x0060:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.getBackGesturePredictionsCategory(int, int, int):int");
    }

    private boolean isWithinInsets(int i, int i2) {
        if (((float) i2) >= ((float) this.mDisplaySize.y) - this.mBottomGestureHeight) {
            return false;
        }
        if (i <= (this.mEdgeWidthLeft + this.mLeftInset) * 2 || i >= this.mDisplaySize.x - ((this.mEdgeWidthRight + this.mRightInset) * 2)) {
            return true;
        }
        return false;
    }

    private boolean isWithinTouchRegion(int i, int i2) {
        int backGesturePredictionsCategory;
        if ((this.mIsInPipMode && this.mPipExcludedBounds.contains(i, i2)) || this.mNavBarOverlayExcludedBounds.contains(i, i2)) {
            return false;
        }
        Map<String, Integer> map = this.mVocab;
        int intValue = map != null ? map.getOrDefault(this.mPackageName, -1).intValue() : -1;
        boolean z = i < this.mEdgeWidthLeft + this.mLeftInset || i >= (this.mDisplaySize.x - this.mEdgeWidthRight) - this.mRightInset;
        if (z) {
            if (!(i < this.mMLEnableWidth + this.mLeftInset || i >= (this.mDisplaySize.x - this.mMLEnableWidth) - this.mRightInset) && this.mUseMLModel && (backGesturePredictionsCategory = getBackGesturePredictionsCategory(i, i2, intValue)) != -1) {
                z = backGesturePredictionsCategory == 1;
            }
        }
        this.mPredictionLog.log(String.format("Prediction [%d,%d,%d,%d,%f,%d]", Long.valueOf(System.currentTimeMillis()), Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(intValue), Float.valueOf(this.mMLResults), Integer.valueOf(z ? 1 : 0)));
        if (this.mIsNavBarShownTransiently) {
            this.mLogGesture = true;
            return z;
        } else if (this.mExcludeRegion.contains(i, i2)) {
            if (z) {
                this.mOverviewProxyService.notifyBackAction(false, -1, -1, false, !this.mIsOnLeftEdge);
                this.mEndPoint.x = -1.0f;
                this.mEndPoint.y = -1.0f;
                this.mLogGesture = true;
                logGesture(3);
            }
            return false;
        } else {
            this.mInRejectedExclusion = this.mUnrestrictedExcludeRegion.contains(i, i2);
            this.mLogGesture = true;
            return z;
        }
    }

    private void cancelGesture(MotionEvent motionEvent) {
        this.mAllowGesture = false;
        this.mLogGesture = false;
        this.mInRejectedExclusion = false;
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(3);
        this.mEdgeBackPlugin.onMotionEvent(obtain);
        obtain.recycle();
    }

    /* access modifiers changed from: private */
    public void logGesture(int i) {
        if (this.mLogGesture) {
            this.mLogGesture = false;
            SysUiStatsLog.write(224, i, (int) this.mDownPoint.y, this.mIsOnLeftEdge ? 1 : 2, (int) this.mDownPoint.x, (int) this.mDownPoint.y, (int) this.mEndPoint.x, (int) this.mEndPoint.y, this.mEdgeWidthLeft + this.mLeftInset, this.mDisplaySize.x - (this.mEdgeWidthRight + this.mRightInset), this.mUseMLModel ? this.mMLResults : -2.0f, (!this.mUseMLModel || !this.mVocab.containsKey(this.mPackageName) || this.mVocab.get(this.mPackageName).intValue() >= 100) ? "" : this.mPackageName);
        }
    }

    private void onMotionEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mInputEventReceiver.setBatchingEnabled(false);
            this.mIsOnLeftEdge = motionEvent.getX() <= ((float) (this.mEdgeWidthLeft + this.mLeftInset));
            this.mMLResults = 0.0f;
            this.mLogGesture = false;
            this.mInRejectedExclusion = false;
            boolean isWithinInsets = isWithinInsets((int) motionEvent.getX(), (int) motionEvent.getY());
            this.mAllowGesture = !this.mDisabledForQuickstep && this.mIsBackGestureAllowed && isWithinInsets && !this.mGestureBlockingActivityRunning && !QuickStepContract.isBackGestureDisabled(this.mSysUiFlags) && isWithinTouchRegion((int) motionEvent.getX(), (int) motionEvent.getY());
            boolean z = ((int) motionEvent.getX()) < this.mEdgeWidthLeft + this.mLeftInset || ((int) motionEvent.getX()) >= (this.mDisplaySize.x - this.mEdgeWidthRight) - this.mRightInset;
            if (!this.mAllowGesture && z) {
                Log.d(TAG, "mAllowGesture: " + this.mAllowGesture + " | mDisabledForQuickstep: " + this.mDisabledForQuickstep + ", mIsBackGestureAllowed: " + this.mIsBackGestureAllowed + ", mGestureBlockingActivityRunning: " + this.mGestureBlockingActivityRunning + ", isBackGestureDisabled by Flags: " + QuickStepContract.isBackGestureDisabled(this.mSysUiFlags) + ", isWithinTouchRegion: " + isWithinTouchRegion((int) motionEvent.getX(), (int) motionEvent.getY()));
                if (QuickStepContract.isBackGestureDisabled(this.mSysUiFlags)) {
                    StringJoiner stringJoiner = new StringJoiner("|");
                    String str = "";
                    stringJoiner.add((this.mSysUiFlags & 2) != 0 ? "navbar_hidden" : str);
                    stringJoiner.add((this.mSysUiFlags & 4) != 0 ? "notif_visible" : str);
                    if ((this.mSysUiFlags & 64) != 0) {
                        str = "keygrd_visible";
                    }
                    stringJoiner.add(str);
                    Log.d(TAG, "SysUiFlags: " + stringJoiner.toString());
                }
            }
            this.mEx.shouldInterceptBack();
            this.mEdgeBackPlugin.setTriggerBackSilently(this.mEx.getIfNeedInterceptBack());
            if (this.mAllowGesture) {
                this.mEdgeBackPlugin.setIsLeftPanel(this.mIsOnLeftEdge);
                this.mEdgeBackPlugin.onMotionEvent(motionEvent);
            }
            if (this.mLogGesture) {
                this.mDownPoint.set(motionEvent.getX(), motionEvent.getY());
                this.mEndPoint.set(-1.0f, -1.0f);
                this.mThresholdCrossed = false;
            }
            (isWithinInsets ? this.mGestureLogInsideInsets : this.mGestureLogOutsideInsets).log(String.format("Gesture [%d,alw=%B,%B,%B,%B,disp=%s,wl=%d,il=%d,wr=%d,ir=%d,excl=%s]", Long.valueOf(System.currentTimeMillis()), Boolean.valueOf(this.mAllowGesture), Boolean.valueOf(this.mIsOnLeftEdge), Boolean.valueOf(this.mIsBackGestureAllowed), Boolean.valueOf(QuickStepContract.isBackGestureDisabled(this.mSysUiFlags)), this.mDisplaySize, Integer.valueOf(this.mEdgeWidthLeft), Integer.valueOf(this.mLeftInset), Integer.valueOf(this.mEdgeWidthRight), Integer.valueOf(this.mRightInset), this.mExcludeRegion));
        } else if (this.mAllowGesture || this.mLogGesture) {
            if (!this.mThresholdCrossed) {
                this.mEndPoint.x = (float) ((int) motionEvent.getX());
                this.mEndPoint.y = (float) ((int) motionEvent.getY());
                if (actionMasked == 5) {
                    if (this.mAllowGesture) {
                        logGesture(6);
                        cancelGesture(motionEvent);
                    }
                    this.mLogGesture = false;
                    return;
                } else if (actionMasked == 2) {
                    if (motionEvent.getEventTime() - motionEvent.getDownTime() > ((long) this.mLongPressTimeout)) {
                        if (this.mAllowGesture) {
                            logGesture(7);
                            cancelGesture(motionEvent);
                        }
                        this.mLogGesture = false;
                        return;
                    }
                    float abs = Math.abs(motionEvent.getX() - this.mDownPoint.x);
                    float abs2 = Math.abs(motionEvent.getY() - this.mDownPoint.y);
                    if (abs2 > abs && abs2 > this.mTouchSlop) {
                        if (this.mAllowGesture) {
                            logGesture(8);
                            cancelGesture(motionEvent);
                        }
                        this.mLogGesture = false;
                        return;
                    } else if (abs > abs2 && abs > this.mTouchSlop) {
                        if (!this.mAllowGesture || this.mEx.getIfNeedInterceptBack()) {
                            logGesture(5);
                        } else {
                            this.mThresholdCrossed = true;
                            this.mInputMonitor.pilferPointers();
                            this.mInputEventReceiver.setBatchingEnabled(true);
                        }
                    }
                }
            }
            if (this.mAllowGesture) {
                this.mEdgeBackPlugin.onMotionEvent(motionEvent);
            }
        }
        this.mProtoTracer.scheduleFrameUpdate();
    }

    /* access modifiers changed from: private */
    public void updateDisabledForQuickstep(Configuration configuration) {
        int rotation = configuration.windowConfiguration.getRotation();
        int i = this.mStartingQuickstepRotation;
        this.mDisabledForQuickstep = i > -1 && i != rotation;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (this.mStartingQuickstepRotation > -1) {
            updateDisabledForQuickstep(configuration);
        }
        updateDisplaySize();
    }

    private void updateDisplaySize() {
        Rect bounds = this.mWindowManager.getMaximumWindowMetrics().getBounds();
        this.mDisplaySize.set(bounds.width(), bounds.height());
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.setDisplaySize(this.mDisplaySize);
        }
    }

    /* access modifiers changed from: private */
    public boolean sendEvent(int i, int i2) {
        long uptimeMillis = SystemClock.uptimeMillis();
        KeyEvent keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, i, i2, 0, 0, -1, 0, 72, 257);
        keyEvent.setDisplayId(this.mContext.getDisplay().getDisplayId());
        return InputManager.getInstance().injectInputEvent(keyEvent, 0);
    }

    public void setInsets(int i, int i2) {
        this.mLeftInset = i;
        this.mRightInset = i2;
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.setInsets(i, i2);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("EdgeBackGestureHandler:");
        printWriter.println("  mIsEnabled=" + this.mIsEnabled);
        printWriter.println("  mIsAttached=" + this.mIsAttached);
        printWriter.println("  mIsBackGestureAllowed=" + this.mIsBackGestureAllowed);
        printWriter.println("  mIsGesturalModeEnabled=" + this.mIsGesturalModeEnabled);
        printWriter.println("  mIsNavBarShownTransiently=" + this.mIsNavBarShownTransiently);
        printWriter.println("  mGestureBlockingActivityRunning=" + this.mGestureBlockingActivityRunning);
        printWriter.println("  mAllowGesture=" + this.mAllowGesture);
        printWriter.println("  mUseMLModel=" + this.mUseMLModel);
        printWriter.println("  mDisabledForQuickstep=" + this.mDisabledForQuickstep);
        printWriter.println("  mStartingQuickstepRotation=" + this.mStartingQuickstepRotation);
        printWriter.println("  mInRejectedExclusion=" + this.mInRejectedExclusion);
        printWriter.println("  mExcludeRegion=" + this.mExcludeRegion);
        printWriter.println("  mUnrestrictedExcludeRegion=" + this.mUnrestrictedExcludeRegion);
        printWriter.println("  mIsInPipMode=" + this.mIsInPipMode);
        printWriter.println("  mPipExcludedBounds=" + this.mPipExcludedBounds);
        printWriter.println("  mNavBarOverlayExcludedBounds=" + this.mNavBarOverlayExcludedBounds);
        printWriter.println("  mEdgeWidthLeft=" + this.mEdgeWidthLeft);
        printWriter.println("  mEdgeWidthRight=" + this.mEdgeWidthRight);
        printWriter.println("  mLeftInset=" + this.mLeftInset);
        printWriter.println("  mRightInset=" + this.mRightInset);
        printWriter.println("  mMLEnableWidth=" + this.mMLEnableWidth);
        printWriter.println("  mMLModelThreshold=" + this.mMLModelThreshold);
        printWriter.println("  mTouchSlop=" + this.mTouchSlop);
        printWriter.println("  mBottomGestureHeight=" + this.mBottomGestureHeight);
        printWriter.println("  mPredictionLog=" + String.join((CharSequence) "\n", (Iterable<? extends CharSequence>) this.mPredictionLog));
        printWriter.println("  mGestureLogInsideInsets=" + String.join((CharSequence) "\n", (Iterable<? extends CharSequence>) this.mGestureLogInsideInsets));
        printWriter.println("  mGestureLogOutsideInsets=" + String.join((CharSequence) "\n", (Iterable<? extends CharSequence>) this.mGestureLogOutsideInsets));
        printWriter.println("  mEdgeBackPlugin=" + this.mEdgeBackPlugin);
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.dump(printWriter);
        }
    }

    /* access modifiers changed from: private */
    public boolean isGestureBlockingActivityRunning() {
        ComponentName componentName;
        ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.getInstance().getRunningTask();
        if (runningTask == null) {
            componentName = null;
        } else {
            componentName = runningTask.topActivity;
        }
        if (componentName != null) {
            this.mPackageName = componentName.getPackageName();
        } else {
            this.mPackageName = "_UNKNOWN";
        }
        return componentName != null && this.mGestureBlockingActivities.contains(componentName);
    }

    public void writeToProto(SystemUiTraceProto systemUiTraceProto) {
        if (systemUiTraceProto.edgeBackGestureHandler == null) {
            systemUiTraceProto.edgeBackGestureHandler = new EdgeBackGestureHandlerProto();
        }
        systemUiTraceProto.edgeBackGestureHandler.allowGesture = this.mAllowGesture;
    }

    public void setBackAnimation(BackAnimation backAnimation) {
        this.mBackAnimation = backAnimation;
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = this.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null && (navigationEdgeBackPlugin instanceof NavigationBarEdgePanel)) {
            ((NavigationBarEdgePanel) navigationEdgeBackPlugin).setBackAnimation(backAnimation);
        }
    }

    public static class Factory {
        private final BroadcastDispatcher mBroadcastDispatcher;
        private final Executor mExecutor;
        private final FalsingManager mFalsingManager;
        private final LatencyTracker mLatencyTracker;
        private final NavigationModeController mNavigationModeController;
        private final OverviewProxyService mOverviewProxyService;
        private final PluginManager mPluginManager;
        private final ProtoTracer mProtoTracer;
        private final SysUiState mSysUiState;
        private final ViewConfiguration mViewConfiguration;
        private final WindowManager mWindowManager;
        private final IWindowManager mWindowManagerService;

        @Inject
        public Factory(OverviewProxyService overviewProxyService, SysUiState sysUiState, PluginManager pluginManager, @Main Executor executor, BroadcastDispatcher broadcastDispatcher, ProtoTracer protoTracer, NavigationModeController navigationModeController, ViewConfiguration viewConfiguration, WindowManager windowManager, IWindowManager iWindowManager, FalsingManager falsingManager, LatencyTracker latencyTracker) {
            this.mOverviewProxyService = overviewProxyService;
            this.mSysUiState = sysUiState;
            this.mPluginManager = pluginManager;
            this.mExecutor = executor;
            this.mBroadcastDispatcher = broadcastDispatcher;
            this.mProtoTracer = protoTracer;
            this.mNavigationModeController = navigationModeController;
            this.mViewConfiguration = viewConfiguration;
            this.mWindowManager = windowManager;
            this.mWindowManagerService = iWindowManager;
            this.mFalsingManager = falsingManager;
            this.mLatencyTracker = latencyTracker;
        }

        public EdgeBackGestureHandler create(Context context) {
            return new EdgeBackGestureHandler(context, this.mOverviewProxyService, this.mSysUiState, this.mPluginManager, this.mExecutor, this.mBroadcastDispatcher, this.mProtoTracer, this.mNavigationModeController, this.mViewConfiguration, this.mWindowManager, this.mWindowManagerService, this.mFalsingManager, this.mLatencyTracker);
        }
    }

    private static class LogArray extends ArrayDeque<String> {
        private final int mLength;

        LogArray(int i) {
            this.mLength = i;
        }

        /* access modifiers changed from: package-private */
        public void log(String str) {
            if (size() >= this.mLength) {
                removeFirst();
            }
            addLast(str);
        }
    }
}
