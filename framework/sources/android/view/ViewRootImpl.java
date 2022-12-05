package android.view;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.ActivityManager;
import android.app.ResourcesManager;
import android.app.WindowConfiguration;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BLASTBufferQueue;
import android.graphics.Canvas;
import android.graphics.FrameInfo;
import android.graphics.HardwareRenderer;
import android.graphics.HardwareRendererObserver;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.media.TtmlUtils;
import android.media.audio.common.AudioFormat;
import android.net.TrafficStats;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.sysprop.DisplayProperties;
import android.telecom.Logging.Session;
import android.util.AndroidRuntimeException;
import android.util.ArraySet;
import android.util.BoostFramework;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.LongArray;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.TypedValue;
import android.util.imetracing.ImeTracing;
import android.util.proto.ProtoOutputStream;
import android.view.ActionMode;
import android.view.Choreographer;
import android.view.IWindow;
import android.view.InputDevice;
import android.view.InputQueue;
import android.view.KeyCharacterMap;
import android.view.ScrollCaptureResponse;
import android.view.Surface;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeIdManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.IAccessibilityEmbeddedConnection;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.contentcapture.ContentCaptureManager;
import android.view.contentcapture.ContentCaptureSession;
import android.view.contentcapture.MainContentCaptureSession;
import android.view.inputmethod.InputMethodManager;
import android.widget.Scroller;
import android.window.ClientWindowFrames;
import com.android.internal.R;
import com.android.internal.graphics.drawable.BackgroundBlurDrawable;
import com.android.internal.inputmethod.InputMethodDebug;
import com.android.internal.os.IResultReceiver;
import com.android.internal.os.SomeArgs;
import com.android.internal.policy.DecorView;
import com.android.internal.policy.PhoneFallbackEventHandler;
import com.android.internal.util.Preconditions;
import com.android.internal.view.BaseSurfaceHolder;
import com.android.internal.view.RootViewSurfaceTaker;
import com.android.internal.view.SurfaceCallbackHelper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
/* loaded from: classes3.dex */
public final class ViewRootImpl implements ViewParent, View.AttachInfo.Callbacks, ThreadedRenderer.DrawCallbacks, AttachedSurfaceControl {
    private static final int CONTENT_CAPTURE_ENABLED_FALSE = 2;
    private static final int CONTENT_CAPTURE_ENABLED_NOT_CHECKED = 0;
    private static final int CONTENT_CAPTURE_ENABLED_TRUE = 1;
    private static final boolean ENABLE_INPUT_LATENCY_TRACKING = false;
    private static final int MAX_QUEUED_INPUT_EVENT_POOL_SIZE = 10;
    static final int MAX_TRACKBALL_DELAY = 250;
    private static final int MSG_CHECK_FOCUS = 13;
    private static final int MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST = 21;
    private static final int MSG_CLOSE_SYSTEM_DIALOGS = 14;
    private static final int MSG_DIE = 3;
    private static final int MSG_DISPATCH_APP_VISIBILITY = 8;
    private static final int MSG_DISPATCH_DRAG_EVENT = 15;
    private static final int MSG_DISPATCH_DRAG_LOCATION_EVENT = 16;
    private static final int MSG_DISPATCH_GET_NEW_SURFACE = 9;
    private static final int MSG_DISPATCH_INPUT_EVENT = 7;
    private static final int MSG_DISPATCH_KEY_FROM_AUTOFILL = 12;
    private static final int MSG_DISPATCH_KEY_FROM_IME = 11;
    private static final int MSG_DISPATCH_SYSTEM_UI_VISIBILITY = 17;
    private static final int MSG_DISPATCH_WINDOW_SHOWN = 25;
    private static final int MSG_DRAW_FINISHED = 29;
    private static final int MSG_HIDE_INSETS = 35;
    private static final int MSG_INSETS_CHANGED = 30;
    private static final int MSG_INSETS_CONTROL_CHANGED = 31;
    private static final int MSG_INVALIDATE = 1;
    private static final int MSG_INVALIDATE_RECT = 2;
    private static final int MSG_INVALIDATE_WORLD = 22;
    private static final int MSG_LOCATION_IN_PARENT_DISPLAY_CHANGED = 33;
    private static final int MSG_POINTER_CAPTURE_CHANGED = 28;
    private static final int MSG_PROCESS_INPUT_EVENTS = 19;
    private static final int MSG_REQUEST_KEYBOARD_SHORTCUTS = 26;
    private static final int MSG_REQUEST_SCROLL_CAPTURE = 36;
    private static final int MSG_RESIZED = 4;
    private static final int MSG_RESIZED_REPORT = 5;
    private static final int MSG_SHOW_INSETS = 34;
    private static final int MSG_SYNTHESIZE_INPUT_EVENT = 24;
    private static final int MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED = 32;
    private static final int MSG_UPDATE_CONFIGURATION = 18;
    private static final int MSG_UPDATE_POINTER_ICON = 27;
    private static final int MSG_WINDOW_FOCUS_CHANGED = 6;
    private static final int MSG_WINDOW_MOVED = 23;
    private static final boolean MT_RENDERER_AVAILABLE = true;
    private static final String PROPERTY_PROFILE_RENDERING = "viewroot.profile_rendering";
    private static final int SCROLL_CAPTURE_REQUEST_TIMEOUT_MILLIS = 2500;
    private static final String TAG = "ViewRootImpl";
    private static boolean sAlwaysAssignFocus;
    private boolean DEBUG_LOCAL_FOCUS_FOR_GAME;
    private IAccessibilityEmbeddedConnection mAccessibilityEmbeddedConnection;
    View mAccessibilityFocusedHost;
    AccessibilityNodeInfo mAccessibilityFocusedVirtualView;
    final AccessibilityInteractionConnectionManager mAccessibilityInteractionConnectionManager;
    AccessibilityInteractionController mAccessibilityInteractionController;
    final AccessibilityManager mAccessibilityManager;
    private ActivityConfigCallback mActivityConfigCallback;
    private boolean mActivityRelaunched;
    boolean mAdded;
    boolean mAddedTouchMode;
    private int mAppIsGameMode;
    private boolean mAppVisibilityChanged;
    boolean mAppVisible;
    boolean mApplyInsetsRequested;
    final View.AttachInfo mAttachInfo;
    AudioManager mAudioManager;
    final String mBasePackageName;
    private BLASTBufferQueue mBlastBufferQueue;
    private final BackgroundBlurDrawable.Aggregator mBlurRegionAggregator;
    private SurfaceControl mBoundsLayer;
    private int mCanvasOffsetX;
    private int mCanvasOffsetY;
    final Choreographer mChoreographer;
    int mClientWindowLayoutFlags;
    final SystemUiVisibilityInfo mCompatibleVisibilityInfo;
    final ConsumeBatchedInputImmediatelyRunnable mConsumeBatchedInputImmediatelyRunnable;
    boolean mConsumeBatchedInputImmediatelyScheduled;
    boolean mConsumeBatchedInputScheduled;
    final ConsumeBatchedInputRunnable mConsumedBatchedInputRunnable;
    int mContentCaptureEnabled;
    public final Context mContext;
    int mCurScrollY;
    View mCurrentDragView;
    private PointerIcon mCustomPointerIcon;
    private final int mDensity;
    private Rect mDirty;
    int mDispatchedSystemBarAppearance;
    int mDispatchedSystemUiVisibility;
    Display mDisplay;
    private final DisplayManager.DisplayListener mDisplayListener;
    final DisplayManager mDisplayManager;
    ClipDescription mDragDescription;
    final PointF mDragPoint;
    private boolean mDragResizing;
    boolean mDrawingAllowed;
    int mDrawsNeededToReport;
    FallbackEventHandler mFallbackEventHandler;
    private boolean mFastScrollSoundEffectsEnabled;
    boolean mFirst;
    InputStage mFirstInputStage;
    InputStage mFirstPostImeInputStage;
    private boolean mForceDecorViewVisibility;
    private boolean mForceDisableBLAST;
    private boolean mForceNextConfigUpdate;
    boolean mForceNextWindowRelayout;
    private int mFpsNumFrames;
    private long mFpsPrevTime;
    private long mFpsStartTime;
    boolean mFullRedrawNeeded;
    private final GestureExclusionTracker mGestureExclusionTracker;
    boolean mHadWindowFocus;
    final ViewRootHandler mHandler;
    boolean mHandlingLayoutInLayoutRequest;
    HardwareRendererObserver mHardwareRendererObserver;
    int mHardwareXOffset;
    int mHardwareYOffset;
    boolean mHaveMoveEvent;
    int mHeight;
    final HighContrastTextManager mHighContrastTextManager;
    private final ImeFocusController mImeFocusController;
    private boolean mInLayout;
    private final InputEventCompatProcessor mInputCompatProcessor;
    private final InputEventAssigner mInputEventAssigner;
    protected final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
    private WindowInputEventReceiver mInputEventReceiver;
    InputQueue mInputQueue;
    InputQueue.Callback mInputQueueCallback;
    private final InsetsController mInsetsController;
    final InvalidateOnAnimationRunnable mInvalidateOnAnimationRunnable;
    private boolean mInvalidateRootRequested;
    boolean mIsAmbientMode;
    public boolean mIsAnimating;
    boolean mIsCreating;
    boolean mIsDrawing;
    private boolean mIsForWebviewOverlay;
    boolean mIsInTraversal;
    private boolean mIsSurfaceOpaque;
    private final Configuration mLastConfigurationFromResources;
    final ViewTreeObserver.InternalInsetsInfo mLastGivenInsets;
    boolean mLastInCompatMode;
    private final MergedConfiguration mLastReportedMergedConfiguration;
    private int mLastRotation;
    WeakReference<View> mLastScrolledFocus;
    private final Point mLastSurfaceSize;
    int mLastSystemUiVisibility;
    final PointF mLastTouchPoint;
    int mLastTouchSource;
    private WindowInsets mLastWindowInsets;
    private int mLastWindowMode;
    boolean mLayoutRequested;
    ArrayList<View> mLayoutRequesters;
    final IBinder mLeashToken;
    volatile Object mLocalDragState;
    final WindowLeaked mLocation;
    boolean mLostWindowFocus;
    private boolean mNeedsRendererSetup;
    boolean mNewSurfaceNeeded;
    private boolean mNextDrawUseBlastSync;
    private final int mNoncompatDensity;
    int mOrigWindowType;
    boolean mPausedForTransition;
    boolean mPendingAlwaysConsumeSystemBars;
    final Rect mPendingBackDropFrame;
    int mPendingInputEventCount;
    QueuedInputEvent mPendingInputEventHead;
    String mPendingInputEventQueueLengthCounterName;
    QueuedInputEvent mPendingInputEventTail;
    private final MergedConfiguration mPendingMergedConfiguration;
    private ArrayList<LayoutTransition> mPendingTransitions;
    boolean mPerformContentCapture;
    boolean mPointerCapture;
    private int mPointerIconType;
    final Region mPreviousTransparentRegion;
    boolean mProcessInputEventsScheduled;
    private boolean mProfile;
    private boolean mProfileRendering;
    private QueuedInputEvent mQueuedInputEventPool;
    private int mQueuedInputEventPoolSize;
    private boolean mRemoved;
    private Choreographer.FrameCallback mRenderProfiler;
    private boolean mRenderProfilingEnabled;
    boolean mReportNextDraw;
    private boolean mRequestedTraverseWhilePaused;
    private int mResizeMode;
    private HashSet<ScrollCaptureCallback> mRootScrollCaptureCallbacks;
    private final SurfaceControl.Transaction mRtBLASTSyncTransaction;
    private long mScrollCaptureRequestTimeout;
    boolean mScrollMayChange;
    int mScrollY;
    Scroller mScroller;
    SendWindowContentChangedAccessibilityEvent mSendWindowContentChangedAccessibilityEvent;
    int mSoftInputMode;
    boolean mStopped;
    public final Surface mSurface;
    private final ArrayList<SurfaceChangedCallback> mSurfaceChangedCallbacks;
    private final SurfaceControl.Transaction mSurfaceChangedTransaction;
    private final SurfaceControl mSurfaceControl;
    BaseSurfaceHolder mSurfaceHolder;
    SurfaceHolder.Callback2 mSurfaceHolderCallback;
    private int mSurfaceSequenceId;
    private final SurfaceSession mSurfaceSession;
    private final Point mSurfaceSize;
    InputStage mSyntheticInputStage;
    private String mTag;
    final int mTargetSdkVersion;
    private final Rect mTempBoundsRect;
    private final InsetsSourceControl[] mTempControls;
    HashSet<View> mTempHashSet;
    private final InsetsState mTempInsets;
    final Rect mTempRect;
    final Thread mThread;
    private final ClientWindowFrames mTmpFrames;
    final int[] mTmpLocation;
    final TypedValue mTmpValue;
    private final SurfaceControl.Transaction mTransaction;
    CompatibilityInfo.Translator mTranslator;
    final Region mTransparentRegion;
    int mTraversalBarrier;
    final TraversalRunnable mTraversalRunnable;
    public boolean mTraversalScheduled;
    private int mTypesHiddenByFlags;
    boolean mUnbufferedInputDispatch;
    int mUnbufferedInputSource;
    private final UnhandledKeyManager mUnhandledKeyManager;
    boolean mUpcomingInTouchMode;
    boolean mUpcomingWindowFocus;
    private boolean mUseBLASTAdapter;
    private boolean mUseMTRenderer;
    private boolean mUseMinRefreshRate;
    View mView;
    final ViewConfiguration mViewConfiguration;
    protected final ViewFrameInfo mViewFrameInfo;
    private int mViewLayoutDirectionInitial;
    int mViewVisibility;
    final Rect mVisRect;
    public int mVoteMinRefreshRateNum;
    private boolean mWaitForBlastSyncComplete;
    int mWidth;
    boolean mWillDrawSoon;
    private boolean mWillMove;
    private boolean mWillResize;
    final Rect mWinFrame;
    final W mWindow;
    public final WindowManager.LayoutParams mWindowAttributes;
    boolean mWindowAttributesChanged;
    final ArrayList<WindowCallbacks> mWindowCallbacks;
    CountDownLatch mWindowDrawCountDown;
    boolean mWindowFocusChanged;
    private float[] mWindowModeOffset;
    final IWindowSession mWindowSession;
    private static boolean DBG = false;
    private static boolean LOCAL_LOGV = false;
    private static boolean DEBUG_DRAW = false;
    private static boolean DEBUG_LAYOUT = false;
    private static boolean DEBUG_DIALOG = false;
    private static boolean DEBUG_INPUT_RESIZE = false;
    private static boolean DEBUG_ORIENTATION = false;
    private static boolean DEBUG_TRACKBALL = false;
    private static boolean DEBUG_IMF = false;
    private static boolean DEBUG_CONFIGURATION = false;
    private static boolean DEBUG_FPS = false;
    private static boolean DEBUG_INPUT_STAGES = false;
    private static boolean DEBUG_KEEP_SCREEN_ON = false;
    private static boolean DEBUG_CONTENT_CAPTURE = false;
    private static boolean DEBUG_SCROLL_CAPTURE = false;
    private static boolean DEBUG_BLAST = false;
    static final ThreadLocal<HandlerActionQueue> sRunQueues = new ThreadLocal<>();
    static final ArrayList<Runnable> sFirstDrawHandlers = new ArrayList<>();
    static boolean sFirstDrawComplete = false;
    private static final ArrayList<ConfigChangedCallback> sConfigCallbacks = new ArrayList<>();
    private static boolean sCompatibilityDone = false;
    static final Interpolator mResizeInterpolator = new AccelerateDecelerateInterpolator();

    /* loaded from: classes3.dex */
    public interface ActivityConfigCallback {
        void onConfigurationChanged(Configuration configuration, int i);
    }

    /* loaded from: classes3.dex */
    public interface ConfigChangedCallback {
        void onConfigurationChanged(Configuration configuration);
    }

    /* loaded from: classes3.dex */
    public interface SurfaceChangedCallback {
        void surfaceCreated(SurfaceControl.Transaction transaction);

        void surfaceDestroyed();

        void surfaceReplaced(SurfaceControl.Transaction transaction);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FrameInfo getUpdatedFrameInfo() {
        FrameInfo frameInfo = this.mChoreographer.mFrameInfo;
        this.mViewFrameInfo.populateFrameInfo(frameInfo);
        this.mViewFrameInfo.reset();
        this.mInputEventAssigner.notifyFrameProcessed();
        return frameInfo;
    }

    public ImeFocusController getImeFocusController() {
        return this.mImeFocusController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class SystemUiVisibilityInfo {
        int globalVisibility;
        int localChanges;
        int localValue;

        SystemUiVisibilityInfo() {
        }
    }

    public ViewRootImpl(Context context, Display display) {
        this(context, display, WindowManagerGlobal.getWindowSession(), false);
    }

    public ViewRootImpl(Context context, Display display, IWindowSession session) {
        this(context, display, session, false);
    }

    public ViewRootImpl(Context context, Display display, IWindowSession session, boolean useSfChoreographer) {
        InputEventCompatProcessor compatProcessor;
        this.mWindowCallbacks = new ArrayList<>();
        this.mTmpLocation = new int[2];
        this.mTmpValue = new TypedValue();
        this.mWindowAttributes = new WindowManager.LayoutParams();
        this.mAppVisible = true;
        boolean z = false;
        this.mForceDecorViewVisibility = false;
        this.mOrigWindowType = -1;
        this.mStopped = false;
        this.mIsAmbientMode = false;
        this.mPausedForTransition = false;
        this.mLastInCompatMode = false;
        this.mViewFrameInfo = new ViewFrameInfo();
        this.mInputEventAssigner = new InputEventAssigner();
        this.mSurfaceSize = new Point();
        this.mLastSurfaceSize = new Point();
        this.mTempBoundsRect = new Rect();
        this.mContentCaptureEnabled = 0;
        this.mUnbufferedInputSource = 0;
        this.mPendingInputEventQueueLengthCounterName = "pq";
        this.mUnhandledKeyManager = new UnhandledKeyManager();
        this.mWindowAttributesChanged = false;
        this.mVoteMinRefreshRateNum = 0;
        this.mUseMinRefreshRate = false;
        this.mSurface = new Surface();
        this.mSurfaceControl = new SurfaceControl();
        this.mSurfaceChangedTransaction = new SurfaceControl.Transaction();
        this.mSurfaceSession = new SurfaceSession();
        this.mTransaction = new SurfaceControl.Transaction();
        this.mTmpFrames = new ClientWindowFrames();
        this.mPendingBackDropFrame = new Rect();
        this.mTempInsets = new InsetsState();
        this.mTempControls = new InsetsSourceControl[22];
        this.mLastGivenInsets = new ViewTreeObserver.InternalInsetsInfo();
        this.mTypesHiddenByFlags = 0;
        this.mLastConfigurationFromResources = new Configuration();
        this.mLastReportedMergedConfiguration = new MergedConfiguration();
        this.mPendingMergedConfiguration = new MergedConfiguration();
        this.mDragPoint = new PointF();
        this.mLastTouchPoint = new PointF();
        this.mFpsStartTime = -1L;
        this.mFpsPrevTime = -1L;
        this.mPointerIconType = 1;
        this.mCustomPointerIcon = null;
        AccessibilityInteractionConnectionManager accessibilityInteractionConnectionManager = new AccessibilityInteractionConnectionManager();
        this.mAccessibilityInteractionConnectionManager = accessibilityInteractionConnectionManager;
        this.mInLayout = false;
        this.mLayoutRequesters = new ArrayList<>();
        this.mHandlingLayoutInLayoutRequest = false;
        this.mInputEventConsistencyVerifier = InputEventConsistencyVerifier.isInstrumentationEnabled() ? new InputEventConsistencyVerifier(this, 0) : null;
        this.mBlurRegionAggregator = new BackgroundBlurDrawable.Aggregator(this);
        this.mGestureExclusionTracker = new GestureExclusionTracker();
        this.mRtBLASTSyncTransaction = new SurfaceControl.Transaction();
        this.mNextDrawUseBlastSync = false;
        this.mWaitForBlastSyncComplete = false;
        this.mRequestedTraverseWhilePaused = false;
        this.mScrollCaptureRequestTimeout = 2500L;
        this.mSurfaceSequenceId = 0;
        this.mTag = TAG;
        this.mHaveMoveEvent = false;
        this.mProfile = false;
        this.mDisplayListener = new DisplayManager.DisplayListener() { // from class: android.view.ViewRootImpl.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int displayId) {
                int oldDisplayState;
                int newDisplayState;
                if (ViewRootImpl.this.mView != null && ViewRootImpl.this.mDisplay.getDisplayId() == displayId && (oldDisplayState = ViewRootImpl.this.mAttachInfo.mDisplayState) != (newDisplayState = ViewRootImpl.this.mDisplay.getState())) {
                    ViewRootImpl.this.mAttachInfo.mDisplayState = newDisplayState;
                    ViewRootImpl.this.pokeDrawLockIfNeeded();
                    if (oldDisplayState != 0) {
                        int oldScreenState = toViewScreenState(oldDisplayState);
                        int newScreenState = toViewScreenState(newDisplayState);
                        if (oldScreenState != newScreenState) {
                            ViewRootImpl.this.mView.dispatchScreenStateChanged(newScreenState);
                        }
                        if (oldDisplayState == 1) {
                            ViewRootImpl.this.mFullRedrawNeeded = true;
                            ViewRootImpl.this.scheduleTraversals();
                        }
                    }
                }
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int displayId) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int displayId) {
            }

            private int toViewScreenState(int displayState) {
                if (displayState != 1) {
                    return 1;
                }
                return 0;
            }
        };
        this.mSurfaceChangedCallbacks = new ArrayList<>();
        this.mDrawsNeededToReport = 0;
        ViewRootHandler viewRootHandler = new ViewRootHandler();
        this.mHandler = viewRootHandler;
        this.mTraversalRunnable = new TraversalRunnable();
        this.mConsumedBatchedInputRunnable = new ConsumeBatchedInputRunnable();
        this.mConsumeBatchedInputImmediatelyRunnable = new ConsumeBatchedInputImmediatelyRunnable();
        this.mInvalidateOnAnimationRunnable = new InvalidateOnAnimationRunnable();
        this.mWindowModeOffset = new float[4];
        this.mLastRotation = -1;
        this.mLastWindowMode = 0;
        this.mAppIsGameMode = 0;
        this.DEBUG_LOCAL_FOCUS_FOR_GAME = true;
        this.mContext = context;
        this.mWindowSession = session;
        this.mDisplay = display;
        this.mBasePackageName = context.getBasePackageName();
        this.mThread = Thread.currentThread();
        WindowLeaked windowLeaked = new WindowLeaked(null);
        this.mLocation = windowLeaked;
        windowLeaked.fillInStackTrace();
        this.mWidth = -1;
        this.mHeight = -1;
        this.mDirty = new Rect();
        this.mTempRect = new Rect();
        this.mVisRect = new Rect();
        this.mWinFrame = new Rect();
        W w = new W(this);
        this.mWindow = w;
        this.mLeashToken = new Binder();
        this.mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        this.mViewVisibility = 8;
        this.mTransparentRegion = new Region();
        this.mPreviousTransparentRegion = new Region();
        this.mFirst = true;
        this.mPerformContentCapture = true;
        this.mAdded = false;
        this.mAttachInfo = new View.AttachInfo(session, w, display, this, viewRootHandler, this, context);
        this.mCompatibleVisibilityInfo = new SystemUiVisibilityInfo();
        AccessibilityManager accessibilityManager = AccessibilityManager.getInstance(context);
        this.mAccessibilityManager = accessibilityManager;
        accessibilityManager.addAccessibilityStateChangeListener(accessibilityInteractionConnectionManager, viewRootHandler);
        HighContrastTextManager highContrastTextManager = new HighContrastTextManager();
        this.mHighContrastTextManager = highContrastTextManager;
        accessibilityManager.addHighTextContrastStateChangeListener(highContrastTextManager, viewRootHandler);
        this.mViewConfiguration = ViewConfiguration.get(context);
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
        this.mNoncompatDensity = context.getResources().getDisplayMetrics().noncompatDensityDpi;
        this.mFallbackEventHandler = new PhoneFallbackEventHandler(context);
        this.mChoreographer = useSfChoreographer ? Choreographer.getSfInstance() : Choreographer.getInstance();
        this.mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        this.mInsetsController = new InsetsController(new ViewRootInsetsControllerHost(this));
        String processorOverrideName = context.getResources().getString(R.string.config_inputEventCompatProcessorOverrideClassName);
        if (processorOverrideName.isEmpty()) {
            compatProcessor = new InputEventCompatProcessor(context);
        } else {
            compatProcessor = null;
            try {
                this.mInputCompatProcessor = (InputEventCompatProcessor) Class.forName(processorOverrideName).getConstructor(Context.class).newInstance(context);
            } catch (Exception e) {
                Log.e(TAG, "Unable to create the InputEventCompatProcessor. ", e);
            } finally {
                this.mInputCompatProcessor = compatProcessor;
            }
        }
        if (!sCompatibilityDone) {
            sAlwaysAssignFocus = this.mTargetSdkVersion < 28 ? true : z;
            sCompatibilityDone = true;
        }
        loadSystemProperties();
        this.mImeFocusController = new ImeFocusController(this);
        AudioManager audioManager = (AudioManager) this.mContext.getSystemService(AudioManager.class);
        this.mFastScrollSoundEffectsEnabled = audioManager.areNavigationRepeatSoundEffectsEnabled();
        this.mScrollCaptureRequestTimeout = 2500L;
    }

    public static void addFirstDrawHandler(Runnable callback) {
        ArrayList<Runnable> arrayList = sFirstDrawHandlers;
        synchronized (arrayList) {
            if (!sFirstDrawComplete) {
                arrayList.add(callback);
            }
        }
    }

    public static void addConfigCallback(ConfigChangedCallback callback) {
        ArrayList<ConfigChangedCallback> arrayList = sConfigCallbacks;
        synchronized (arrayList) {
            arrayList.add(callback);
        }
    }

    public void setActivityConfigCallback(ActivityConfigCallback callback) {
        this.mActivityConfigCallback = callback;
    }

    public void setOnContentApplyWindowInsetsListener(Window.OnContentApplyWindowInsetsListener listener) {
        this.mAttachInfo.mContentOnApplyWindowInsetsListener = listener;
        if (!this.mFirst) {
            requestFitSystemWindows();
        }
    }

    public void addWindowCallbacks(WindowCallbacks callback) {
        synchronized (this.mWindowCallbacks) {
            this.mWindowCallbacks.add(callback);
        }
    }

    public void removeWindowCallbacks(WindowCallbacks callback) {
        synchronized (this.mWindowCallbacks) {
            this.mWindowCallbacks.remove(callback);
        }
    }

    public void reportDrawFinish() {
        CountDownLatch countDownLatch = this.mWindowDrawCountDown;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public void profile() {
        this.mProfile = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isInTouchMode() {
        IWindowSession windowSession = WindowManagerGlobal.peekWindowSession();
        if (windowSession != null) {
            try {
                return windowSession.getInTouchMode();
            } catch (RemoteException e) {
                return false;
            }
        }
        return false;
    }

    public void notifyChildRebuilt() {
        if (this.mView instanceof RootViewSurfaceTaker) {
            SurfaceHolder.Callback2 callback2 = this.mSurfaceHolderCallback;
            if (callback2 != null) {
                this.mSurfaceHolder.removeCallback(callback2);
            }
            SurfaceHolder.Callback2 willYouTakeTheSurface = ((RootViewSurfaceTaker) this.mView).willYouTakeTheSurface();
            this.mSurfaceHolderCallback = willYouTakeTheSurface;
            if (willYouTakeTheSurface != null) {
                TakenSurfaceHolder takenSurfaceHolder = new TakenSurfaceHolder();
                this.mSurfaceHolder = takenSurfaceHolder;
                takenSurfaceHolder.setFormat(0);
                this.mSurfaceHolder.addCallback(this.mSurfaceHolderCallback);
            } else {
                this.mSurfaceHolder = null;
            }
            InputQueue.Callback willYouTakeTheInputQueue = ((RootViewSurfaceTaker) this.mView).willYouTakeTheInputQueue();
            this.mInputQueueCallback = willYouTakeTheInputQueue;
            if (willYouTakeTheInputQueue != null) {
                willYouTakeTheInputQueue.onInputQueueCreated(this.mInputQueue);
            }
        }
    }

    public static void computeWindowBounds(WindowManager.LayoutParams attrs, InsetsState state, Rect displayFrame, Rect outBounds) {
        int typesToFit = attrs.getFitInsetsTypes();
        int sidesToFit = attrs.getFitInsetsSides();
        ArraySet<Integer> types = InsetsState.toInternalType(typesToFit);
        int bottom = 0;
        Insets insets = Insets.of(0, 0, 0, 0);
        for (int i = types.size() - 1; i >= 0; i--) {
            InsetsSource source = state.peekSource(types.valueAt(i).intValue());
            if (source != null) {
                insets = Insets.max(insets, source.calculateInsets(displayFrame, attrs.isFitInsetsIgnoringVisibility()));
            }
        }
        int i2 = sidesToFit & 1;
        int left = i2 != 0 ? insets.left : 0;
        int top = (sidesToFit & 2) != 0 ? insets.top : 0;
        int right = (sidesToFit & 4) != 0 ? insets.right : 0;
        if ((sidesToFit & 8) != 0) {
            bottom = insets.bottom;
        }
        outBounds.set(displayFrame.left + left, displayFrame.top + top, displayFrame.right - right, displayFrame.bottom - bottom);
    }

    private Configuration getConfiguration() {
        return this.mContext.getResources().getConfiguration();
    }

    public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView) {
        setView(view, attrs, panelParentView, UserHandle.myUserId());
    }

    /* JADX WARN: Removed duplicated region for block: B:149:0x0477 A[Catch: all -> 0x0487, TRY_ENTER, TryCatch #5 {all -> 0x0487, blocks: (B:15:0x004a, B:17:0x0051, B:19:0x0057, B:21:0x005d, B:22:0x0065, B:24:0x0072, B:26:0x007d, B:27:0x008e, B:29:0x0093, B:30:0x0096, B:32:0x00ab, B:35:0x00b7, B:37:0x00bb, B:39:0x00c0, B:41:0x00c5, B:42:0x00d6, B:44:0x00da, B:45:0x00f0, B:47:0x00f6, B:48:0x00fe, B:51:0x0111, B:54:0x011e, B:56:0x0122, B:57:0x012a, B:59:0x0138, B:60:0x0141, B:63:0x014c, B:65:0x0154, B:67:0x015c, B:75:0x01be, B:76:0x01c1, B:79:0x01ca, B:81:0x0204, B:83:0x021e, B:84:0x022f, B:85:0x0232, B:86:0x0365, B:87:0x0379, B:89:0x0236, B:90:0x0253, B:91:0x0254, B:92:0x0271, B:93:0x0272, B:94:0x028f, B:95:0x0290, B:96:0x02ad, B:97:0x02ae, B:99:0x02b0, B:100:0x02d9, B:101:0x02da, B:102:0x02fe, B:103:0x02ff, B:104:0x031c, B:105:0x031d, B:106:0x0346, B:107:0x0347, B:108:0x0364, B:109:0x037a, B:111:0x037e, B:112:0x0381, B:114:0x0385, B:117:0x0392, B:119:0x0396, B:120:0x03a2, B:121:0x03ad, B:124:0x03b7, B:127:0x03be, B:129:0x03c8, B:130:0x03cd, B:132:0x03d3, B:133:0x03d7, B:134:0x0480, B:149:0x0477, B:150:0x047b, B:165:0x011c, B:171:0x0485), top: B:3:0x0005 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setView(View view, WindowManager.LayoutParams attrs, View panelParentView, int userId) {
        boolean restore;
        InputChannel inputChannel;
        PendingInsetsController pendingInsetsController;
        synchronized (this) {
            try {
                try {
                    if (this.mView == null) {
                        this.mView = view;
                        this.mAttachInfo.mDisplayState = this.mDisplay.getState();
                        this.mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mHandler);
                        this.mViewLayoutDirectionInitial = this.mView.getRawLayoutDirection();
                        this.mFallbackEventHandler.setView(view);
                        try {
                            this.mWindowAttributes.copyFrom(attrs);
                            if (this.mWindowAttributes.packageName == null) {
                                this.mWindowAttributes.packageName = this.mBasePackageName;
                            }
                            this.mWindowAttributes.privateFlags |= 33554432;
                            WindowManager.LayoutParams attrs2 = this.mWindowAttributes;
                            setTag();
                            if (DEBUG_KEEP_SCREEN_ON && (this.mClientWindowLayoutFlags & 128) != 0 && (attrs2.flags & 128) == 0) {
                                Slog.d(this.mTag, "setView: FLAG_KEEP_SCREEN_ON changed from true to false!");
                            }
                            this.mClientWindowLayoutFlags = attrs2.flags;
                            setAccessibilityFocus(null, null);
                            boolean z = false;
                            if (view instanceof RootViewSurfaceTaker) {
                                SurfaceHolder.Callback2 willYouTakeTheSurface = ((RootViewSurfaceTaker) view).willYouTakeTheSurface();
                                this.mSurfaceHolderCallback = willYouTakeTheSurface;
                                if (willYouTakeTheSurface != null) {
                                    TakenSurfaceHolder takenSurfaceHolder = new TakenSurfaceHolder();
                                    this.mSurfaceHolder = takenSurfaceHolder;
                                    takenSurfaceHolder.setFormat(0);
                                    this.mSurfaceHolder.addCallback(this.mSurfaceHolderCallback);
                                }
                            }
                            if (!attrs2.hasManualSurfaceInsets) {
                                attrs2.setSurfaceInsets(view, false, true);
                            }
                            CompatibilityInfo compatibilityInfo = this.mDisplay.getDisplayAdjustments().getCompatibilityInfo();
                            this.mTranslator = compatibilityInfo.getTranslator();
                            if (this.mSurfaceHolder == null) {
                                enableHardwareAcceleration(attrs2);
                                boolean useMTRenderer = this.mAttachInfo.mThreadedRenderer != null;
                                if (this.mUseMTRenderer != useMTRenderer) {
                                    endDragResizing();
                                    this.mUseMTRenderer = useMTRenderer;
                                }
                            }
                            CompatibilityInfo.Translator translator = this.mTranslator;
                            if (translator != null) {
                                this.mSurface.setCompatibilityTranslator(translator);
                                attrs2.backup();
                                this.mTranslator.translateWindowLayout(attrs2);
                                restore = true;
                            } else {
                                restore = false;
                            }
                            boolean restore2 = DEBUG_LAYOUT;
                            if (restore2) {
                                Log.d(this.mTag, "WindowLayout in setView:" + attrs2);
                            }
                            if (!compatibilityInfo.supportsScreen()) {
                                attrs2.privateFlags |= 128;
                                this.mLastInCompatMode = true;
                            }
                            this.mSoftInputMode = attrs2.softInputMode;
                            this.mWindowAttributesChanged = true;
                            this.mAttachInfo.mRootView = view;
                            this.mAttachInfo.mScalingRequired = this.mTranslator != null;
                            View.AttachInfo attachInfo = this.mAttachInfo;
                            CompatibilityInfo.Translator translator2 = this.mTranslator;
                            attachInfo.mApplicationScale = translator2 == null ? 1.0f : translator2.applicationScale;
                            if (panelParentView != null) {
                                this.mAttachInfo.mPanelParentWindowToken = panelParentView.getApplicationWindowToken();
                            }
                            this.mAdded = true;
                            requestLayout();
                            if ((this.mWindowAttributes.inputFeatures & 2) == 0) {
                                InputChannel inputChannel2 = new InputChannel();
                                inputChannel = inputChannel2;
                            } else {
                                inputChannel = null;
                            }
                            this.mForceDecorViewVisibility = (this.mWindowAttributes.privateFlags & 16384) != 0;
                            View view2 = this.mView;
                            if ((view2 instanceof RootViewSurfaceTaker) && (pendingInsetsController = ((RootViewSurfaceTaker) view2).providePendingInsetsController()) != null) {
                                pendingInsetsController.replayAndAttach(this.mInsetsController);
                            }
                            try {
                                this.mOrigWindowType = this.mWindowAttributes.type;
                                this.mAttachInfo.mRecomputeGlobalAttributes = true;
                                collectViewAttributes();
                                adjustLayoutParamsForCompatibility(this.mWindowAttributes);
                                controlInsetsForCompatibility(this.mWindowAttributes);
                                InputChannel inputChannel3 = inputChannel;
                                try {
                                    int res = this.mWindowSession.addToDisplayAsUser(this.mWindow, this.mWindowAttributes, getHostVisibility(), this.mDisplay.getDisplayId(), userId, this.mInsetsController.getRequestedVisibility(), inputChannel3, this.mTempInsets, this.mTempControls);
                                    CompatibilityInfo.Translator translator3 = this.mTranslator;
                                    if (translator3 != null) {
                                        try {
                                            translator3.translateInsetsStateInScreenToAppWindow(this.mTempInsets);
                                            this.mTranslator.translateSourceControlsInScreenToAppWindow(this.mTempControls);
                                        } catch (RemoteException e) {
                                            e = e;
                                            try {
                                                this.mAdded = false;
                                                this.mView = null;
                                                this.mAttachInfo.mRootView = null;
                                                this.mFallbackEventHandler.setView(null);
                                                unscheduleTraversals();
                                                setAccessibilityFocus(null, null);
                                                throw new RuntimeException("Adding window failed", e);
                                            } catch (Throwable th) {
                                                e = th;
                                                if (restore) {
                                                    attrs2.restore();
                                                }
                                                throw e;
                                            }
                                        } catch (Throwable th2) {
                                            e = th2;
                                            if (restore) {
                                            }
                                            throw e;
                                        }
                                    }
                                    if (restore) {
                                        attrs2.restore();
                                    }
                                    this.mAttachInfo.mAlwaysConsumeSystemBars = (res & 4) != 0;
                                    this.mPendingAlwaysConsumeSystemBars = this.mAttachInfo.mAlwaysConsumeSystemBars;
                                    this.mInsetsController.onStateChanged(this.mTempInsets);
                                    this.mInsetsController.onControlsChanged(this.mTempControls);
                                    computeWindowBounds(this.mWindowAttributes, this.mInsetsController.getState(), getConfiguration().windowConfiguration.getBounds(), this.mTmpFrames.frame);
                                    setFrame(this.mTmpFrames.frame);
                                    if (DEBUG_LAYOUT) {
                                        Log.v(this.mTag, "Added window " + this.mWindow);
                                    }
                                    if (res < 0) {
                                        this.mAttachInfo.mRootView = null;
                                        this.mAdded = false;
                                        this.mFallbackEventHandler.setView(null);
                                        unscheduleTraversals();
                                        setAccessibilityFocus(null, null);
                                        switch (res) {
                                            case -11:
                                                throw new WindowManager.BadTokenException("Unable to add Window " + this.mWindow + " -- requested userId is not valid");
                                            case -10:
                                                throw new WindowManager.InvalidDisplayException("Unable to add window " + this.mWindow + " -- the specified window type " + this.mWindowAttributes.type + " is not valid");
                                            case -9:
                                                throw new WindowManager.InvalidDisplayException("Unable to add window " + this.mWindow + " -- the specified display can not be found");
                                            case -8:
                                                throw new WindowManager.BadTokenException("Unable to add window " + this.mWindow + " -- permission denied for window type " + this.mWindowAttributes.type);
                                            case -7:
                                                throw new WindowManager.BadTokenException("Unable to add window " + this.mWindow + " -- another window of type " + this.mWindowAttributes.type + " already exists");
                                            case -6:
                                                return;
                                            case -5:
                                                throw new WindowManager.BadTokenException("Unable to add window -- window " + this.mWindow + " has already been added");
                                            case -4:
                                                throw new WindowManager.BadTokenException("Unable to add window -- app for token " + attrs2.token + " is exiting");
                                            case -3:
                                                throw new WindowManager.BadTokenException("Unable to add window -- token " + attrs2.token + " is not for an application");
                                            case -2:
                                            case -1:
                                                throw new WindowManager.BadTokenException("Unable to add window -- token " + attrs2.token + " is not valid; is your activity running?");
                                            default:
                                                throw new RuntimeException("Unable to add window -- unknown error code " + res);
                                        }
                                    }
                                    if ((res & 8) != 0) {
                                        this.mUseBLASTAdapter = true;
                                    }
                                    if (view instanceof RootViewSurfaceTaker) {
                                        this.mInputQueueCallback = ((RootViewSurfaceTaker) view).willYouTakeTheInputQueue();
                                    }
                                    if (inputChannel3 != null) {
                                        if (this.mInputQueueCallback != null) {
                                            InputQueue inputQueue = new InputQueue();
                                            this.mInputQueue = inputQueue;
                                            this.mInputQueueCallback.onInputQueueCreated(inputQueue);
                                        }
                                        this.mInputEventReceiver = new WindowInputEventReceiver(inputChannel3, Looper.myLooper());
                                    }
                                    view.assignParent(this);
                                    this.mAddedTouchMode = (res & 1) != 0;
                                    if ((res & 2) != 0) {
                                        z = true;
                                    }
                                    this.mAppVisible = z;
                                    if (this.mAccessibilityManager.isEnabled()) {
                                        this.mAccessibilityInteractionConnectionManager.ensureConnection();
                                    }
                                    if (view.getImportantForAccessibility() == 0) {
                                        view.setImportantForAccessibility(1);
                                    }
                                    CharSequence counterSuffix = attrs2.getTitle();
                                    SyntheticInputStage syntheticInputStage = new SyntheticInputStage();
                                    this.mSyntheticInputStage = syntheticInputStage;
                                    InputStage viewPostImeStage = new ViewPostImeInputStage(syntheticInputStage);
                                    InputStage nativePostImeStage = new NativePostImeInputStage(viewPostImeStage, "aq:native-post-ime:" + ((Object) counterSuffix));
                                    InputStage earlyPostImeStage = new EarlyPostImeInputStage(nativePostImeStage);
                                    InputStage imeStage = new ImeInputStage(earlyPostImeStage, "aq:ime:" + ((Object) counterSuffix));
                                    InputStage viewPreImeStage = new ViewPreImeInputStage(imeStage);
                                    InputStage nativePreImeStage = new NativePreImeInputStage(viewPreImeStage, "aq:native-pre-ime:" + ((Object) counterSuffix));
                                    this.mFirstInputStage = nativePreImeStage;
                                    this.mFirstPostImeInputStage = earlyPostImeStage;
                                    this.mPendingInputEventQueueLengthCounterName = "aq:pending:" + ((Object) counterSuffix);
                                } catch (RemoteException e2) {
                                    e = e2;
                                } catch (Throwable th3) {
                                    e = th3;
                                }
                            } catch (RemoteException e3) {
                                e = e3;
                            } catch (Throwable th4) {
                                e = th4;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            throw th;
                        }
                    }
                } catch (Throwable th6) {
                    th = th6;
                }
            } catch (Throwable th7) {
                th = th7;
            }
        }
    }

    private void setTag() {
        String[] split = this.mWindowAttributes.getTitle().toString().split("\\.");
        if (split.length > 0) {
            this.mTag = "ViewRootImpl[" + split[split.length - 1] + "]";
        }
    }

    public int getWindowFlags() {
        return this.mWindowAttributes.flags;
    }

    public int getDisplayId() {
        return this.mDisplay.getDisplayId();
    }

    public CharSequence getTitle() {
        return this.mWindowAttributes.getTitle();
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void destroyHardwareResources() {
        ThreadedRenderer renderer = this.mAttachInfo.mThreadedRenderer;
        if (renderer != null) {
            if (Looper.myLooper() != this.mAttachInfo.mHandler.getLooper()) {
                this.mAttachInfo.mHandler.postAtFrontOfQueue(new Runnable() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda8
                    @Override // java.lang.Runnable
                    public final void run() {
                        ViewRootImpl.this.destroyHardwareResources();
                    }
                });
                return;
            }
            renderer.destroyHardwareResources(this.mView);
            renderer.destroy();
        }
    }

    public void detachFunctor(long functor) {
    }

    public static void invokeFunctor(long functor, boolean waitForCompletion) {
    }

    public void registerAnimatingRenderNode(RenderNode animator) {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.registerAnimatingRenderNode(animator);
            return;
        }
        if (this.mAttachInfo.mPendingAnimatingRenderNodes == null) {
            this.mAttachInfo.mPendingAnimatingRenderNodes = new ArrayList();
        }
        this.mAttachInfo.mPendingAnimatingRenderNodes.add(animator);
    }

    public void registerVectorDrawableAnimator(NativeVectorDrawableAnimator animator) {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.registerVectorDrawableAnimator(animator);
        }
    }

    public void registerRtFrameCallback(final HardwareRenderer.FrameDrawingCallback callback) {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.registerRtFrameCallback(new HardwareRenderer.FrameDrawingCallback() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda3
                @Override // android.graphics.HardwareRenderer.FrameDrawingCallback
                public final void onFrameDraw(long j) {
                    ViewRootImpl.lambda$registerRtFrameCallback$0(HardwareRenderer.FrameDrawingCallback.this, j);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$registerRtFrameCallback$0(HardwareRenderer.FrameDrawingCallback callback, long frame) {
        try {
            callback.onFrameDraw(frame);
        } catch (Exception e) {
            Log.e(TAG, "Exception while executing onFrameDraw", e);
        }
    }

    private void addASurfaceTransactionCallback() {
        HardwareRenderer.ASurfaceTransactionCallback callback = new HardwareRenderer.ASurfaceTransactionCallback() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda1
            @Override // android.graphics.HardwareRenderer.ASurfaceTransactionCallback
            public final boolean onMergeTransaction(long j, long j2, long j3) {
                return ViewRootImpl.this.lambda$addASurfaceTransactionCallback$1$ViewRootImpl(j, j2, j3);
            }
        };
        this.mAttachInfo.mThreadedRenderer.setASurfaceTransactionCallback(callback);
    }

    public /* synthetic */ boolean lambda$addASurfaceTransactionCallback$1$ViewRootImpl(long nativeTransactionObj, long nativeSurfaceControlObj, long frameNr) {
        BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
        if (bLASTBufferQueue == null) {
            return false;
        }
        bLASTBufferQueue.mergeWithNextTransaction(nativeTransactionObj, frameNr);
        return true;
    }

    private void addPrepareSurfaceControlForWebviewCallback() {
        HardwareRenderer.PrepareSurfaceControlForWebviewCallback callback = new HardwareRenderer.PrepareSurfaceControlForWebviewCallback() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda6
            @Override // android.graphics.HardwareRenderer.PrepareSurfaceControlForWebviewCallback
            public final void prepare() {
                ViewRootImpl.this.lambda$addPrepareSurfaceControlForWebviewCallback$2$ViewRootImpl();
            }
        };
        this.mAttachInfo.mThreadedRenderer.setPrepareSurfaceControlForWebviewCallback(callback);
    }

    public /* synthetic */ void lambda$addPrepareSurfaceControlForWebviewCallback$2$ViewRootImpl() {
        if (this.mIsForWebviewOverlay) {
            return;
        }
        synchronized (this) {
            this.mIsForWebviewOverlay = true;
        }
        this.mTransaction.setOpaque(this.mSurfaceControl, false).apply();
    }

    private void enableHardwareAcceleration(WindowManager.LayoutParams attrs) {
        boolean translucent = false;
        this.mAttachInfo.mHardwareAccelerated = false;
        this.mAttachInfo.mHardwareAccelerationRequested = false;
        if (this.mTranslator != null) {
            return;
        }
        boolean hardwareAccelerated = (attrs.flags & 16777216) != 0;
        if (hardwareAccelerated) {
            boolean forceHwAccelerated = (attrs.privateFlags & 2) != 0;
            if (ThreadedRenderer.sRendererEnabled || forceHwAccelerated) {
                if (this.mAttachInfo.mThreadedRenderer != null) {
                    this.mAttachInfo.mThreadedRenderer.destroy();
                }
                Rect insets = attrs.surfaceInsets;
                boolean hasSurfaceInsets = (insets.left == 0 && insets.right == 0 && insets.top == 0 && insets.bottom == 0) ? false : true;
                if (attrs.format != -1 || hasSurfaceInsets) {
                    translucent = true;
                }
                this.mAttachInfo.mThreadedRenderer = ThreadedRenderer.create(this.mContext, translucent, attrs.getTitle().toString());
                updateColorModeIfNeeded(attrs.getColorMode());
                updateForceDarkMode();
                if (this.mAttachInfo.mThreadedRenderer != null) {
                    View.AttachInfo attachInfo = this.mAttachInfo;
                    attachInfo.mHardwareAccelerationRequested = true;
                    attachInfo.mHardwareAccelerated = true;
                    if (this.mHardwareRendererObserver != null) {
                        this.mAttachInfo.mThreadedRenderer.addObserver(this.mHardwareRendererObserver);
                    }
                    if (HardwareRenderer.isWebViewOverlaysEnabled()) {
                        addPrepareSurfaceControlForWebviewCallback();
                        addASurfaceTransactionCallback();
                    }
                    this.mAttachInfo.mThreadedRenderer.setSurfaceControl(this.mSurfaceControl);
                }
            }
        }
    }

    private int getNightMode() {
        return getConfiguration().uiMode & 48;
    }

    private void updateForceDarkMode() {
        if (this.mAttachInfo.mThreadedRenderer == null) {
            return;
        }
        boolean z = true;
        boolean useAutoDark = getNightMode() == 32;
        if (useAutoDark) {
            boolean forceDarkAllowedDefault = SystemProperties.getBoolean(ThreadedRenderer.DEBUG_FORCE_DARK, false);
            TypedArray a = this.mContext.obtainStyledAttributes(R.styleable.Theme);
            if (!a.getBoolean(279, true) || !a.getBoolean(278, forceDarkAllowedDefault)) {
                z = false;
            }
            useAutoDark = z;
            a.recycle();
        }
        if (this.mAttachInfo.mThreadedRenderer.setForceDark(useAutoDark)) {
            invalidateWorld(this.mView);
        }
    }

    public View getView() {
        return this.mView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final WindowLeaked getLocation() {
        return this.mLocation;
    }

    public void setLayoutParams(WindowManager.LayoutParams attrs, boolean newView) {
        int oldInsetLeft;
        synchronized (this) {
            int oldInsetLeft2 = this.mWindowAttributes.surfaceInsets.left;
            int oldInsetTop = this.mWindowAttributes.surfaceInsets.top;
            int oldInsetRight = this.mWindowAttributes.surfaceInsets.right;
            int oldInsetBottom = this.mWindowAttributes.surfaceInsets.bottom;
            int oldSoftInputMode = this.mWindowAttributes.softInputMode;
            boolean oldHasManualSurfaceInsets = this.mWindowAttributes.hasManualSurfaceInsets;
            if (DEBUG_KEEP_SCREEN_ON && (this.mClientWindowLayoutFlags & 128) != 0 && (attrs.flags & 128) == 0) {
                Slog.d(this.mTag, "setLayoutParams: FLAG_KEEP_SCREEN_ON from true to false!");
            }
            this.mClientWindowLayoutFlags = attrs.flags;
            int compatibleWindowFlag = this.mWindowAttributes.privateFlags & 128;
            int systemUiVisibility = this.mWindowAttributes.systemUiVisibility;
            int subtreeSystemUiVisibility = this.mWindowAttributes.subtreeSystemUiVisibility;
            int appearance = this.mWindowAttributes.insetsFlags.appearance;
            int behavior = this.mWindowAttributes.insetsFlags.behavior;
            int appearanceAndBehaviorPrivateFlags = this.mWindowAttributes.privateFlags & AudioFormat.DTS_HD;
            int changes = this.mWindowAttributes.copyFrom(attrs);
            if ((524288 & changes) != 0) {
                this.mAttachInfo.mRecomputeGlobalAttributes = true;
            }
            if ((changes & 1) != 0) {
                this.mAttachInfo.mNeedsUpdateLightCenter = true;
            }
            if (this.mWindowAttributes.packageName == null) {
                this.mWindowAttributes.packageName = this.mBasePackageName;
            }
            this.mWindowAttributes.systemUiVisibility = systemUiVisibility;
            this.mWindowAttributes.subtreeSystemUiVisibility = subtreeSystemUiVisibility;
            this.mWindowAttributes.insetsFlags.appearance = appearance;
            this.mWindowAttributes.insetsFlags.behavior = behavior;
            this.mWindowAttributes.privateFlags |= compatibleWindowFlag | appearanceAndBehaviorPrivateFlags | 33554432;
            if (this.mWindowAttributes.preservePreviousSurfaceInsets) {
                this.mWindowAttributes.surfaceInsets.set(oldInsetLeft2, oldInsetTop, oldInsetRight, oldInsetBottom);
                this.mWindowAttributes.hasManualSurfaceInsets = oldHasManualSurfaceInsets;
            } else if (this.mWindowAttributes.surfaceInsets.left != oldInsetLeft2 || this.mWindowAttributes.surfaceInsets.top != oldInsetTop || this.mWindowAttributes.surfaceInsets.right != oldInsetRight || this.mWindowAttributes.surfaceInsets.bottom != oldInsetBottom) {
                this.mNeedsRendererSetup = true;
            }
            applyKeepScreenOnFlag(this.mWindowAttributes);
            if (newView) {
                this.mSoftInputMode = attrs.softInputMode;
                requestLayout();
            }
            if ((attrs.softInputMode & 240) == 0) {
                WindowManager.LayoutParams layoutParams = this.mWindowAttributes;
                oldInsetLeft = oldSoftInputMode;
                layoutParams.softInputMode = (oldInsetLeft & 240) | (layoutParams.softInputMode & TrafficStats.TAG_SYSTEM_IMPERSONATION_RANGE_END);
            } else {
                oldInsetLeft = oldSoftInputMode;
            }
            if (this.mWindowAttributes.softInputMode != oldInsetLeft) {
                requestFitSystemWindows();
            }
            this.mWindowAttributesChanged = true;
            scheduleTraversals();
        }
    }

    void handleAppVisibility(boolean visible) {
        if (this.mAppVisible != visible) {
            this.mAppVisible = visible;
            this.mAppVisibilityChanged = true;
            if (this.DEBUG_LOCAL_FOCUS_FOR_GAME && this.mAppIsGameMode == 1 && !this.mUpcomingWindowFocus && visible && WindowConfiguration.isNtPinWindow(this.mLastWindowMode)) {
                windowFocusChanged(false, false);
                windowFocusChanged(true, false);
            }
            scheduleTraversals();
            if (!this.mAppVisible) {
                WindowManagerGlobal.trimForeground();
            }
        }
    }

    void handleGetNewSurface() {
        this.mNewSurfaceNeeded = true;
        this.mFullRedrawNeeded = true;
        scheduleTraversals();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleResized(int msg, SomeArgs args) {
        if (!this.mAdded) {
            return;
        }
        ClientWindowFrames frames = (ClientWindowFrames) args.arg1;
        MergedConfiguration mergedConfiguration = (MergedConfiguration) args.arg2;
        boolean z = false;
        boolean forceNextWindowRelayout = args.argi1 != 0;
        int displayId = args.argi3;
        Rect backdropFrame = frames.backdropFrame;
        boolean frameChanged = !this.mWinFrame.equals(frames.frame);
        boolean backdropFrameChanged = !this.mPendingBackDropFrame.equals(backdropFrame);
        boolean configChanged = !this.mLastReportedMergedConfiguration.equals(mergedConfiguration);
        boolean displayChanged = this.mDisplay.getDisplayId() != displayId;
        if (msg == 4 && !frameChanged && !backdropFrameChanged && !configChanged && !displayChanged && !forceNextWindowRelayout) {
            return;
        }
        if (configChanged) {
            performConfigurationChange(mergedConfiguration, false, displayChanged ? displayId : -1);
        } else if (displayChanged) {
            onMovedToDisplay(displayId, this.mLastConfigurationFromResources);
        }
        setFrame(frames.frame);
        this.mTmpFrames.displayFrame.set(frames.displayFrame);
        this.mPendingBackDropFrame.set(backdropFrame);
        this.mForceNextWindowRelayout = forceNextWindowRelayout;
        if (args.argi2 != 0) {
            z = true;
        }
        this.mPendingAlwaysConsumeSystemBars = z;
        if (msg == 5 && !this.mNextDrawUseBlastSync) {
            reportNextDraw();
        }
        View view = this.mView;
        if (view != null && (frameChanged || configChanged)) {
            forceLayout(view);
        }
        requestLayout();
    }

    public void onMovedToDisplay(int displayId, Configuration config) {
        if (this.mDisplay.getDisplayId() == displayId) {
            return;
        }
        updateInternalDisplay(displayId, this.mView.getResources());
        this.mImeFocusController.onMovedToDisplay();
        this.mAttachInfo.mDisplayState = this.mDisplay.getState();
        this.mView.dispatchMovedToDisplay(this.mDisplay, config);
    }

    private void updateInternalDisplay(int displayId, Resources resources) {
        Display preferredDisplay = ResourcesManager.getInstance().getAdjustedDisplay(displayId, resources);
        if (preferredDisplay == null) {
            Slog.w(TAG, "Cannot get desired display with Id: " + displayId);
            this.mDisplay = ResourcesManager.getInstance().getAdjustedDisplay(0, resources);
        } else {
            this.mDisplay = preferredDisplay;
        }
        this.mContext.updateDisplay(this.mDisplay.getDisplayId());
    }

    void pokeDrawLockIfNeeded() {
        int displayState = this.mAttachInfo.mDisplayState;
        if (this.mView == null || !this.mAdded || !this.mTraversalScheduled) {
            return;
        }
        if (displayState == 3 || displayState == 4) {
            try {
                this.mWindowSession.pokeDrawLock(this.mWindow);
            } catch (RemoteException e) {
            }
        }
    }

    @Override // android.view.ViewParent
    public void requestFitSystemWindows() {
        checkThread();
        this.mApplyInsetsRequested = true;
        scheduleTraversals();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyInsetsChanged() {
        this.mApplyInsetsRequested = true;
        if (this.mWillMove || this.mWillResize) {
            return;
        }
        requestLayout();
        if (View.sForceLayoutWhenInsetsChanged && this.mView != null && (this.mWindowAttributes.softInputMode & 240) == 16) {
            forceLayout(this.mView);
        }
        if (!this.mIsInTraversal) {
            scheduleTraversals();
        }
    }

    @Override // android.view.ViewParent
    public void requestLayout() {
        if (!this.mHandlingLayoutInLayoutRequest) {
            checkThread();
            this.mLayoutRequested = true;
            scheduleTraversals();
        }
    }

    @Override // android.view.ViewParent
    public boolean isLayoutRequested() {
        return this.mLayoutRequested;
    }

    @Override // android.view.ViewParent
    public void onDescendantInvalidated(View child, View descendant) {
        if ((descendant.mPrivateFlags & 64) != 0) {
            this.mIsAnimating = true;
        }
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void invalidate() {
        this.mDirty.set(0, 0, this.mWidth, this.mHeight);
        if (!this.mWillDrawSoon) {
            scheduleTraversals();
        }
    }

    void invalidateWorld(View view) {
        view.invalidate();
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                invalidateWorld(parent.getChildAt(i));
            }
        }
    }

    @Override // android.view.ViewParent
    public void invalidateChild(View child, Rect dirty) {
        invalidateChildInParent(null, dirty);
    }

    @Override // android.view.ViewParent
    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        checkThread();
        if (DEBUG_DRAW) {
            String str = this.mTag;
            Log.v(str, "Invalidate child: " + dirty);
        }
        if (dirty == null) {
            invalidate();
            return null;
        } else if (dirty.isEmpty() && !this.mIsAnimating) {
            return null;
        } else {
            if (this.mCurScrollY != 0 || this.mTranslator != null) {
                this.mTempRect.set(dirty);
                dirty = this.mTempRect;
                int i = this.mCurScrollY;
                if (i != 0) {
                    dirty.offset(0, -i);
                }
                CompatibilityInfo.Translator translator = this.mTranslator;
                if (translator != null) {
                    translator.translateRectInAppWindowToScreen(dirty);
                }
                if (this.mAttachInfo.mScalingRequired) {
                    dirty.inset(-1, -1);
                }
            }
            invalidateRectOnScreen(dirty);
            return null;
        }
    }

    private void invalidateRectOnScreen(Rect dirty) {
        Rect localDirty = this.mDirty;
        localDirty.union(dirty.left, dirty.top, dirty.right, dirty.bottom);
        float appScale = this.mAttachInfo.mApplicationScale;
        boolean intersected = localDirty.intersect(0, 0, (int) ((this.mWidth * appScale) + 0.5f), (int) ((this.mHeight * appScale) + 0.5f));
        if (!intersected) {
            localDirty.setEmpty();
        }
        if (!this.mWillDrawSoon) {
            if (intersected || this.mIsAnimating) {
                scheduleTraversals();
            }
        }
    }

    public void setIsAmbientMode(boolean ambient) {
        this.mIsAmbientMode = ambient;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindowStopped(boolean stopped) {
        checkThread();
        if (this.mStopped != stopped) {
            this.mStopped = stopped;
            ThreadedRenderer renderer = this.mAttachInfo.mThreadedRenderer;
            if (renderer != null) {
                if (DEBUG_DRAW) {
                    String str = this.mTag;
                    Log.d(str, "WindowStopped on " + ((Object) getTitle()) + " set to " + this.mStopped);
                }
                renderer.setStopped(this.mStopped);
            }
            if (!this.mStopped) {
                this.mNewSurfaceNeeded = true;
                scheduleTraversals();
                return;
            }
            if (renderer != null) {
                renderer.destroyHardwareResources(this.mView);
            }
            if (this.mSurface.isValid()) {
                if (this.mSurfaceHolder != null) {
                    notifyHolderSurfaceDestroyed();
                }
                notifySurfaceDestroyed();
            }
            destroySurface();
        }
    }

    public void addSurfaceChangedCallback(SurfaceChangedCallback c) {
        this.mSurfaceChangedCallbacks.add(c);
    }

    public void removeSurfaceChangedCallback(SurfaceChangedCallback c) {
        this.mSurfaceChangedCallbacks.remove(c);
    }

    private void notifySurfaceCreated() {
        for (int i = 0; i < this.mSurfaceChangedCallbacks.size(); i++) {
            this.mSurfaceChangedCallbacks.get(i).surfaceCreated(this.mSurfaceChangedTransaction);
        }
    }

    private void notifySurfaceReplaced() {
        for (int i = 0; i < this.mSurfaceChangedCallbacks.size(); i++) {
            this.mSurfaceChangedCallbacks.get(i).surfaceReplaced(this.mSurfaceChangedTransaction);
        }
    }

    private void notifySurfaceDestroyed() {
        for (int i = 0; i < this.mSurfaceChangedCallbacks.size(); i++) {
            this.mSurfaceChangedCallbacks.get(i).surfaceDestroyed();
        }
    }

    public SurfaceControl getBoundsLayer() {
        if (this.mBoundsLayer == null) {
            SurfaceControl.Builder containerLayer = new SurfaceControl.Builder(this.mSurfaceSession).setContainerLayer();
            this.mBoundsLayer = containerLayer.setName("Bounds for - " + getTitle().toString()).setParent(getSurfaceControl()).setCallsite("ViewRootImpl.getBoundsLayer").build();
            setBoundsLayerCrop(this.mTransaction);
            this.mTransaction.show(this.mBoundsLayer).apply();
        }
        return this.mBoundsLayer;
    }

    Surface getOrCreateBLASTSurface() {
        if (!this.mSurfaceControl.isValid()) {
            return null;
        }
        Surface ret = null;
        BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
        if (bLASTBufferQueue == null) {
            BLASTBufferQueue bLASTBufferQueue2 = new BLASTBufferQueue(this.mTag, this.mSurfaceControl, this.mSurfaceSize.x, this.mSurfaceSize.y, this.mWindowAttributes.format);
            this.mBlastBufferQueue = bLASTBufferQueue2;
            ret = bLASTBufferQueue2.createSurface();
        } else {
            bLASTBufferQueue.update(this.mSurfaceControl, this.mSurfaceSize.x, this.mSurfaceSize.y, this.mWindowAttributes.format);
        }
        BoostFramework.ScrollOptimizer.setBLASTBufferQueue(this.mBlastBufferQueue);
        return ret;
    }

    private void setBoundsLayerCrop(SurfaceControl.Transaction t) {
        this.mTempBoundsRect.set(0, 0, this.mSurfaceSize.x, this.mSurfaceSize.y);
        this.mTempBoundsRect.inset(this.mWindowAttributes.surfaceInsets.left, this.mWindowAttributes.surfaceInsets.top, this.mWindowAttributes.surfaceInsets.right, this.mWindowAttributes.surfaceInsets.bottom);
        t.setWindowCrop(this.mBoundsLayer, this.mTempBoundsRect);
    }

    private boolean updateBoundsLayer(SurfaceControl.Transaction t) {
        if (this.mBoundsLayer != null) {
            setBoundsLayerCrop(t);
            return true;
        }
        return false;
    }

    private void prepareSurfaces() {
        SurfaceControl.Transaction t = this.mTransaction;
        SurfaceControl sc = getSurfaceControl();
        if (sc.isValid() && updateBoundsLayer(t)) {
            lambda$applyTransactionOnDraw$9$ViewRootImpl(t, this.mSurface.getNextFrameNumber());
        }
    }

    private void destroySurface() {
        SurfaceControl surfaceControl = this.mBoundsLayer;
        if (surfaceControl != null) {
            surfaceControl.release();
            this.mBoundsLayer = null;
        }
        this.mSurface.release();
        this.mSurfaceControl.release();
        BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
        if (bLASTBufferQueue != null) {
            bLASTBufferQueue.destroy();
            this.mBlastBufferQueue = null;
        }
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.setSurfaceControl(null);
        }
    }

    public void setPausedForTransition(boolean paused) {
        this.mPausedForTransition = paused;
    }

    @Override // android.view.ViewParent
    public ViewParent getParent() {
        return null;
    }

    @Override // android.view.ViewParent
    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        if (child != this.mView) {
            throw new RuntimeException("child is not mine, honest!");
        }
        return r.intersect(0, 0, this.mWidth, this.mHeight);
    }

    @Override // android.view.ViewParent
    public void bringChildToFront(View child) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getHostVisibility() {
        if (this.mAppVisible || this.mForceDecorViewVisibility) {
            return this.mView.getVisibility();
        }
        return 8;
    }

    public void requestTransitionStart(LayoutTransition transition) {
        ArrayList<LayoutTransition> arrayList = this.mPendingTransitions;
        if (arrayList == null || !arrayList.contains(transition)) {
            if (this.mPendingTransitions == null) {
                this.mPendingTransitions = new ArrayList<>();
            }
            this.mPendingTransitions.add(transition);
        }
    }

    void notifyRendererOfFramePending() {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.notifyFramePending();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void scheduleTraversals() {
        if (!this.mTraversalScheduled) {
            this.mTraversalScheduled = true;
            this.mTraversalBarrier = this.mHandler.getLooper().getQueue().postSyncBarrier();
            this.mChoreographer.postCallback(3, this.mTraversalRunnable, null);
            notifyRendererOfFramePending();
            pokeDrawLockIfNeeded();
        }
    }

    void unscheduleTraversals() {
        if (this.mTraversalScheduled) {
            this.mTraversalScheduled = false;
            this.mHandler.getLooper().getQueue().removeSyncBarrier(this.mTraversalBarrier);
            this.mChoreographer.removeCallbacks(3, this.mTraversalRunnable, null);
        }
    }

    void doTraversal() {
        if (this.mTraversalScheduled) {
            this.mTraversalScheduled = false;
            this.mHandler.getLooper().getQueue().removeSyncBarrier(this.mTraversalBarrier);
            if (this.mProfile) {
                Debug.startMethodTracing("ViewAncestor");
            }
            performTraversals();
            if (this.mProfile) {
                Debug.stopMethodTracing();
                this.mProfile = false;
            }
        }
    }

    private void applyKeepScreenOnFlag(WindowManager.LayoutParams params) {
        if (this.mAttachInfo.mKeepScreenOn) {
            params.flags |= 128;
        } else {
            params.flags = (params.flags & (-129)) | (this.mClientWindowLayoutFlags & 128);
        }
    }

    private boolean collectViewAttributes() {
        if (this.mAttachInfo.mRecomputeGlobalAttributes) {
            this.mAttachInfo.mRecomputeGlobalAttributes = false;
            boolean oldScreenOn = this.mAttachInfo.mKeepScreenOn;
            this.mAttachInfo.mKeepScreenOn = false;
            this.mAttachInfo.mSystemUiVisibility = 0;
            this.mAttachInfo.mHasSystemUiListeners = false;
            this.mView.dispatchCollectViewAttributes(this.mAttachInfo, 0);
            this.mAttachInfo.mSystemUiVisibility &= ~this.mAttachInfo.mDisabledSystemUiVisibility;
            WindowManager.LayoutParams params = this.mWindowAttributes;
            this.mAttachInfo.mSystemUiVisibility |= getImpliedSystemUiVisibility(params);
            SystemUiVisibilityInfo systemUiVisibilityInfo = this.mCompatibleVisibilityInfo;
            systemUiVisibilityInfo.globalVisibility = (systemUiVisibilityInfo.globalVisibility & (-2)) | (this.mAttachInfo.mSystemUiVisibility & 1);
            dispatchDispatchSystemUiVisibilityChanged(this.mCompatibleVisibilityInfo);
            if (this.mAttachInfo.mKeepScreenOn != oldScreenOn || this.mAttachInfo.mSystemUiVisibility != params.subtreeSystemUiVisibility || this.mAttachInfo.mHasSystemUiListeners != params.hasSystemUiListeners) {
                applyKeepScreenOnFlag(params);
                params.subtreeSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                params.hasSystemUiListeners = this.mAttachInfo.mHasSystemUiListeners;
                this.mView.dispatchWindowSystemUiVisiblityChanged(this.mAttachInfo.mSystemUiVisibility);
                return true;
            }
        }
        return false;
    }

    private int getImpliedSystemUiVisibility(WindowManager.LayoutParams params) {
        int vis = 0;
        if ((params.flags & 67108864) != 0) {
            vis = 0 | 1280;
        }
        if ((params.flags & 134217728) != 0) {
            return vis | 768;
        }
        return vis;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateCompatSysUiVisibility(int type, boolean visible, boolean hasControl) {
        int systemUiFlag;
        boolean wasVisible = true;
        if (type != 0 && type != 1) {
            return;
        }
        SystemUiVisibilityInfo info = this.mCompatibleVisibilityInfo;
        if (type == 0) {
            systemUiFlag = 4;
        } else {
            systemUiFlag = 2;
        }
        if ((info.globalVisibility & systemUiFlag) != 0) {
            wasVisible = false;
        }
        if (visible) {
            info.globalVisibility &= ~systemUiFlag;
            if (!wasVisible && hasControl) {
                info.localChanges |= systemUiFlag;
            }
        } else {
            info.globalVisibility |= systemUiFlag;
            info.localChanges &= ~systemUiFlag;
        }
        dispatchDispatchSystemUiVisibilityChanged(info);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearLowProfileModeIfNeeded(int showTypes, boolean fromIme) {
        SystemUiVisibilityInfo info = this.mCompatibleVisibilityInfo;
        if ((WindowInsets.Type.systemBars() & showTypes) != 0 && !fromIme && (info.globalVisibility & 1) != 0) {
            info.globalVisibility &= -2;
            info.localChanges |= 1;
            dispatchDispatchSystemUiVisibilityChanged(info);
        }
    }

    private void dispatchDispatchSystemUiVisibilityChanged(SystemUiVisibilityInfo args) {
        if (this.mDispatchedSystemUiVisibility != args.globalVisibility) {
            this.mHandler.removeMessages(17);
            ViewRootHandler viewRootHandler = this.mHandler;
            viewRootHandler.sendMessage(viewRootHandler.obtainMessage(17, args));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDispatchSystemUiVisibilityChanged(SystemUiVisibilityInfo args) {
        if (this.mView == null) {
            return;
        }
        if (args.localChanges != 0) {
            this.mView.updateLocalSystemUiVisibility(args.localValue, args.localChanges);
            args.localChanges = 0;
        }
        int visibility = args.globalVisibility & 7;
        if (this.mDispatchedSystemUiVisibility != visibility) {
            this.mDispatchedSystemUiVisibility = visibility;
            this.mView.dispatchSystemUiVisibilityChanged(visibility);
        }
    }

    public static void adjustLayoutParamsForCompatibility(WindowManager.LayoutParams inOutParams) {
        int sysUiVis = inOutParams.systemUiVisibility | inOutParams.subtreeSystemUiVisibility;
        int flags = inOutParams.flags;
        int type = inOutParams.type;
        int adjust = inOutParams.softInputMode & 240;
        if ((inOutParams.privateFlags & 67108864) == 0) {
            inOutParams.insetsFlags.appearance = 0;
            if ((sysUiVis & 1) != 0) {
                inOutParams.insetsFlags.appearance |= 4;
            }
            if ((sysUiVis & 8192) != 0) {
                inOutParams.insetsFlags.appearance |= 8;
            }
            if ((sysUiVis & 16) != 0) {
                inOutParams.insetsFlags.appearance |= 16;
            }
        }
        if ((inOutParams.privateFlags & 134217728) == 0) {
            if ((sysUiVis & 4096) != 0 || (flags & 1024) != 0) {
                inOutParams.insetsFlags.behavior = 2;
            } else {
                inOutParams.insetsFlags.behavior = 1;
            }
        }
        inOutParams.privateFlags &= -1073741825;
        if ((inOutParams.privateFlags & 268435456) != 0) {
            return;
        }
        int types = inOutParams.getFitInsetsTypes();
        boolean ignoreVis = inOutParams.isFitInsetsIgnoringVisibility();
        if ((sysUiVis & 1024) != 0 || (flags & 256) != 0 || (67108864 & flags) != 0) {
            types &= ~WindowInsets.Type.statusBars();
        }
        if ((sysUiVis & 512) != 0 || (flags & 134217728) != 0) {
            types &= ~WindowInsets.Type.systemBars();
        }
        if (type == 2005 || type == 2003) {
            ignoreVis = true;
        } else if ((WindowInsets.Type.systemBars() & types) == WindowInsets.Type.systemBars()) {
            if (adjust == 16) {
                types |= WindowInsets.Type.ime();
            } else {
                inOutParams.privateFlags |= 1073741824;
            }
        }
        inOutParams.setFitInsetsTypes(types);
        inOutParams.setFitInsetsIgnoringVisibility(ignoreVis);
        inOutParams.privateFlags &= -268435457;
    }

    private void controlInsetsForCompatibility(WindowManager.LayoutParams params) {
        int sysUiVis = params.systemUiVisibility | params.subtreeSystemUiVisibility;
        int flags = params.flags;
        boolean navIsHiddenByFlags = false;
        boolean matchParent = params.width == -1 && params.height == -1;
        boolean nonAttachedAppWindow = params.type >= 1 && params.type <= 99;
        boolean statusWasHiddenByFlags = (this.mTypesHiddenByFlags & WindowInsets.Type.statusBars()) != 0;
        boolean statusIsHiddenByFlags = (sysUiVis & 4) != 0 || ((flags & 1024) != 0 && matchParent && nonAttachedAppWindow);
        boolean navWasHiddenByFlags = (this.mTypesHiddenByFlags & WindowInsets.Type.navigationBars()) != 0;
        if ((sysUiVis & 2) != 0) {
            navIsHiddenByFlags = true;
        }
        int typesToHide = 0;
        int typesToShow = 0;
        if (statusIsHiddenByFlags && !statusWasHiddenByFlags) {
            typesToHide = 0 | WindowInsets.Type.statusBars();
        } else if (!statusIsHiddenByFlags && statusWasHiddenByFlags) {
            typesToShow = 0 | WindowInsets.Type.statusBars();
        }
        if (navIsHiddenByFlags && !navWasHiddenByFlags) {
            typesToHide |= WindowInsets.Type.navigationBars();
        } else if (!navIsHiddenByFlags && navWasHiddenByFlags) {
            typesToShow |= WindowInsets.Type.navigationBars();
        }
        if (typesToHide != 0) {
            getInsetsController().hide(typesToHide);
        }
        if (typesToShow != 0) {
            getInsetsController().show(typesToShow);
        }
        int i = this.mTypesHiddenByFlags | typesToHide;
        this.mTypesHiddenByFlags = i;
        this.mTypesHiddenByFlags = i & (~typesToShow);
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x01b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean measureHierarchy(View host, WindowManager.LayoutParams lp, Resources res, int desiredWindowWidth, int desiredWindowHeight) {
        boolean windowSizeMayChange;
        boolean goodMeasure;
        boolean goodMeasure2;
        boolean windowSizeMayChange2;
        if (DEBUG_ORIENTATION || DEBUG_LAYOUT) {
            Log.v(this.mTag, "Measuring " + host + " in display " + desiredWindowWidth + "x" + desiredWindowHeight + Session.TRUNCATE_STRING);
        }
        if (lp.width != -2) {
            windowSizeMayChange = false;
            goodMeasure = false;
        } else {
            DisplayMetrics packageMetrics = res.getDisplayMetrics();
            res.getValue(R.dimen.config_prefDialogWidth, this.mTmpValue, true);
            int baseSize = 0;
            if (this.mTmpValue.type == 5) {
                baseSize = (int) this.mTmpValue.getDimension(packageMetrics);
            }
            if (DEBUG_DIALOG) {
                Log.v(this.mTag, "Window " + this.mView + ": baseSize=" + baseSize + ", desiredWindowWidth=" + desiredWindowWidth);
            }
            if (baseSize == 0 || desiredWindowWidth <= baseSize) {
                windowSizeMayChange = false;
                goodMeasure = false;
            } else {
                int childWidthMeasureSpec = getRootMeasureSpec(baseSize, lp.width);
                int childHeightMeasureSpec = getRootMeasureSpec(desiredWindowHeight, lp.height);
                performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                if (DEBUG_DIALOG) {
                    String str = this.mTag;
                    windowSizeMayChange = false;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Window ");
                    goodMeasure = false;
                    sb.append(this.mView);
                    sb.append(": measured (");
                    sb.append(host.getMeasuredWidth());
                    sb.append(",");
                    sb.append(host.getMeasuredHeight());
                    sb.append(") from width spec: ");
                    sb.append(View.MeasureSpec.toString(childWidthMeasureSpec));
                    sb.append(" and height spec: ");
                    sb.append(View.MeasureSpec.toString(childHeightMeasureSpec));
                    Log.v(str, sb.toString());
                } else {
                    windowSizeMayChange = false;
                    goodMeasure = false;
                }
                if ((host.getMeasuredWidthAndState() & 16777216) == 0) {
                    goodMeasure2 = true;
                } else {
                    int baseSize2 = (baseSize + desiredWindowWidth) / 2;
                    if (DEBUG_DIALOG) {
                        Log.v(this.mTag, "Window " + this.mView + ": next baseSize=" + baseSize2);
                    }
                    performMeasure(getRootMeasureSpec(baseSize2, lp.width), childHeightMeasureSpec);
                    if (DEBUG_DIALOG) {
                        Log.v(this.mTag, "Window " + this.mView + ": measured (" + host.getMeasuredWidth() + "," + host.getMeasuredHeight() + ")");
                    }
                    if ((host.getMeasuredWidthAndState() & 16777216) == 0) {
                        if (DEBUG_DIALOG) {
                            Log.v(this.mTag, "Good!");
                        }
                        goodMeasure2 = true;
                    }
                }
                if (!goodMeasure2) {
                    performMeasure(getRootMeasureSpec(desiredWindowWidth, lp.width), getRootMeasureSpec(desiredWindowHeight, lp.height));
                    if (this.mWidth != host.getMeasuredWidth() || this.mHeight != host.getMeasuredHeight()) {
                        windowSizeMayChange2 = true;
                        if (DBG) {
                            System.out.println("======================================");
                            System.out.println("performTraversals -- after measure");
                            host.debug();
                        }
                        return windowSizeMayChange2;
                    }
                }
                windowSizeMayChange2 = windowSizeMayChange;
                if (DBG) {
                }
                return windowSizeMayChange2;
            }
        }
        goodMeasure2 = goodMeasure;
        if (!goodMeasure2) {
        }
        windowSizeMayChange2 = windowSizeMayChange;
        if (DBG) {
        }
        return windowSizeMayChange2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void transformMatrixToGlobal(Matrix m) {
        m.preTranslate(this.mAttachInfo.mWindowLeft, this.mAttachInfo.mWindowTop);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void transformMatrixToLocal(Matrix m) {
        m.postTranslate(-this.mAttachInfo.mWindowLeft, -this.mAttachInfo.mWindowTop);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowInsets getWindowInsets(boolean forceConstruct) {
        if (this.mLastWindowInsets == null || forceConstruct) {
            Configuration config = getConfiguration();
            this.mLastWindowInsets = this.mInsetsController.calculateInsets(config.isScreenRound(), this.mAttachInfo.mAlwaysConsumeSystemBars, this.mWindowAttributes.type, config.windowConfiguration.getWindowingMode(), this.mWindowAttributes.softInputMode, this.mWindowAttributes.flags, this.mWindowAttributes.systemUiVisibility | this.mWindowAttributes.subtreeSystemUiVisibility);
            this.mAttachInfo.mContentInsets.set(this.mLastWindowInsets.getSystemWindowInsets().toRect());
            this.mAttachInfo.mStableInsets.set(this.mLastWindowInsets.getStableInsets().toRect());
            this.mAttachInfo.mVisibleInsets.set(this.mInsetsController.calculateVisibleInsets(this.mWindowAttributes.softInputMode));
        }
        return this.mLastWindowInsets;
    }

    public void dispatchApplyInsets(View host) {
        Trace.traceBegin(8L, "dispatchApplyInsets");
        this.mApplyInsetsRequested = false;
        WindowInsets insets = getWindowInsets(true);
        if (!shouldDispatchCutout()) {
            insets = insets.consumeDisplayCutout();
        }
        host.dispatchApplyWindowInsets(insets);
        this.mAttachInfo.delayNotifyContentCaptureInsetsEvent(insets.getInsets(WindowInsets.Type.all()));
        Trace.traceEnd(8L);
    }

    private boolean updateCaptionInsets() {
        View view = this.mView;
        if (!(view instanceof DecorView)) {
            return false;
        }
        int captionInsetsHeight = ((DecorView) view).getCaptionInsetsHeight();
        Rect captionFrame = new Rect();
        if (captionInsetsHeight != 0) {
            captionFrame.set(this.mWinFrame.left, this.mWinFrame.top, this.mWinFrame.right, this.mWinFrame.top + captionInsetsHeight);
        }
        if (this.mAttachInfo.mCaptionInsets.equals(captionFrame)) {
            return false;
        }
        this.mAttachInfo.mCaptionInsets.set(captionFrame);
        return true;
    }

    private boolean shouldDispatchCutout() {
        return this.mWindowAttributes.layoutInDisplayCutoutMode == 3 || this.mWindowAttributes.layoutInDisplayCutoutMode == 1;
    }

    public InsetsController getInsetsController() {
        return this.mInsetsController;
    }

    private static boolean shouldUseDisplaySize(WindowManager.LayoutParams lp) {
        return lp.type == 2041 || lp.type == 2011 || lp.type == 2020;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int dipToPx(int dip) {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        return (int) ((displayMetrics.density * dip) + 0.5f);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(177:15|(2:738|(1:742))(1:19)|20|(1:737)(1:28)|29|(4:31|(1:33)(1:735)|(1:35)(1:734)|(167:37|38|(3:40|(1:42)(1:732)|43)(1:733)|44|(5:46|(1:48)(2:718|(1:723)(1:722))|49|(1:51)|52)(2:724|(3:728|(1:730)|731))|(3:54|(2:(1:57)(1:59)|58)|(1:63))|64|(1:66)|67|(1:717)(1:73)|74|(4:76|(1:78)(2:708|(3:712|(1:714)(1:715)|80))|79|80)(1:716)|81|(1:83)|84|(1:86)|87|(2:692|(6:694|(3:696|(2:698|699)(1:701)|700)|702|(1:704)|705|(1:707)))|91|(1:691)(2:97|(146:99|(1:101)|(1:688)(142:104|(1:687)(4:108|(2:110|(1:114))(1:686)|678|(2:680|(1:682))(1:685))|115|116|(1:677)(1:120)|121|(1:676)(1:125)|126|(1:128)(1:675)|129|(1:131)(1:674)|(4:133|(1:137)|138|(1:140))(1:673)|141|(1:672)(2:146|(1:148)(27:671|(1:392)|(1:391)(1:251)|252|(1:390)(1:256)|257|(4:259|(4:261|(1:263)|264|(2:266|(1:268)))(1:388)|269|(1:271))(1:389)|(1:273)(1:(1:385)(1:(1:387)))|(1:275)|(2:277|(4:281|(1:283)(1:288)|284|285))|289|(2:291|(4:300|(1:302)|303|(2:305|(2:307|(1:309))(2:310|(1:312))))(2:295|(1:299)))|(1:383)(1:316)|317|(1:381)(1:320)|(1:380)(1:324)|(1:326)(1:(1:379)(1:378))|(3:369|(1:371)(1:374)|(1:373))|329|(1:331)|332|(1:335)|(3:337|(4:341|(2:344|342)|345|346)|347)(1:(1:354)(3:355|(4:359|(2:362|360)|363|364)|(1:368)))|348|(1:350)|351|352))|149|(1:151)(1:670)|152|153|154|(8:650|651|652|653|654|655|656|657)(1:156)|157|158|(3:633|634|(4:636|637|638|639)(1:644))(1:160)|161|162|(94:166|167|168|(1:170)(1:622)|171|(1:173)(1:621)|174|(1:620)(1:177)|178|(5:613|614|(1:616)|617|(1:619))|180|(1:182)(1:612)|183|184|185|(1:187)|437|(8:595|596|597|598|599|600|601|602)(1:439)|440|441|(6:443|(1:445)|446|(1:448)(1:451)|449|450)|452|453|454|(5:456|457|458|459|460)(1:589)|461|(1:463)(1:585)|464|(1:584)(1:468)|469|(1:583)(1:473)|474|475|(1:582)(1:478)|479|(1:481)|(2:483|484)|485|(1:487)|(2:578|579)|(4:494|495|496|(5:529|530|531|(3:534|535|(1:537))|533))(1:(8:548|(1:550)|551|(1:553)|554|(1:556)|557|(1:559))(1:(3:569|570|571)))|498|499|(1:(8:(1:503)(1:524)|504|(1:523)(1:508)|509|(1:511)(1:522)|512|513|514)(1:525))(1:526)|515|(1:(1:518)(1:519))|520|192|(1:194)|195|(1:436)|199|(6:201|(1:203)|204|(2:206|(3:208|(1:210)|211))|(2:423|(3:425|(4:427|(1:429)|430|431)(1:433)|432)(1:434))(1:216)|(4:218|219|220|221))(1:435)|226|(1:237)|238|(4:393|(1:395)(1:422)|396|(44:404|(1:406)(1:421)|407|(1:409)|410|(1:412)|(1:420)(3:414|(1:416)(1:419)|417)|418|242|(0)|392|(1:248)|391|252|(1:254)|390|257|(0)(0)|(0)(0)|(0)|(0)|289|(0)|(1:314)|383|317|(0)|381|(1:322)|380|(0)(0)|(0)|369|(0)(0)|(0)|329|(0)|332|(1:335)|(0)(0)|348|(0)|351|352))|241|242|(0)|392|(0)|391|252|(0)|390|257|(0)(0)|(0)(0)|(0)|(0)|289|(0)|(0)|383|317|(0)|381|(0)|380|(0)(0)|(0)|369|(0)(0)|(0)|329|(0)|332|(0)|(0)(0)|348|(0)|351|352)|626|627|628|629|167|168|(0)(0)|171|(0)(0)|174|(0)|620|178|(0)|180|(0)(0)|183|184|185|(0)|437|(0)(0)|440|441|(0)|452|453|454|(0)(0)|461|(0)(0)|464|(1:466)|584|469|(1:471)|583|474|475|(0)|580|582|479|(0)|(0)|485|(0)|(1:489)|578|579|(0)(0)|498|499|(0)(0)|515|(0)|520|192|(0)|195|(1:197)|436|199|(0)(0)|226|(2:228|237)|238|(0)|393|(0)(0)|396|(1:398)|404|(0)(0)|407|(0)|410|(0)|(0)(0)|418|242|(0)|392|(0)|391|252|(0)|390|257|(0)(0)|(0)(0)|(0)|(0)|289|(0)|(0)|383|317|(0)|381|(0)|380|(0)(0)|(0)|369|(0)(0)|(0)|329|(0)|332|(0)|(0)(0)|348|(0)|351|352)|684|116|(1:118)|677|121|(1:123)|676|126|(0)(0)|129|(0)(0)|(0)(0)|141|(0)|672|149|(0)(0)|152|153|154|(0)(0)|157|158|(0)(0)|161|162|(113:166|167|168|(0)(0)|171|(0)(0)|174|(0)|620|178|(0)|180|(0)(0)|183|184|185|(0)|437|(0)(0)|440|441|(0)|452|453|454|(0)(0)|461|(0)(0)|464|(0)|584|469|(0)|583|474|475|(0)|580|582|479|(0)|(0)|485|(0)|(0)|578|579|(0)(0)|498|499|(0)(0)|515|(0)|520|192|(0)|195|(0)|436|199|(0)(0)|226|(0)|238|(0)|393|(0)(0)|396|(0)|404|(0)(0)|407|(0)|410|(0)|(0)(0)|418|242|(0)|392|(0)|391|252|(0)|390|257|(0)(0)|(0)(0)|(0)|(0)|289|(0)|(0)|383|317|(0)|381|(0)|380|(0)(0)|(0)|369|(0)(0)|(0)|329|(0)|332|(0)|(0)(0)|348|(0)|351|352)|626|627|628|629|167|168|(0)(0)|171|(0)(0)|174|(0)|620|178|(0)|180|(0)(0)|183|184|185|(0)|437|(0)(0)|440|441|(0)|452|453|454|(0)(0)|461|(0)(0)|464|(0)|584|469|(0)|583|474|475|(0)|580|582|479|(0)|(0)|485|(0)|(0)|578|579|(0)(0)|498|499|(0)(0)|515|(0)|520|192|(0)|195|(0)|436|199|(0)(0)|226|(0)|238|(0)|393|(0)(0)|396|(0)|404|(0)(0)|407|(0)|410|(0)|(0)(0)|418|242|(0)|392|(0)|391|252|(0)|390|257|(0)(0)|(0)(0)|(0)|(0)|289|(0)|(0)|383|317|(0)|381|(0)|380|(0)(0)|(0)|369|(0)(0)|(0)|329|(0)|332|(0)|(0)(0)|348|(0)|351|352)(1:689))|690|(0)|(0)|688|684|116|(0)|677|121|(0)|676|126|(0)(0)|129|(0)(0)|(0)(0)|141|(0)|672|149|(0)(0)|152|153|154|(0)(0)|157|158|(0)(0)|161|162|(0)|626|627|628|629|167|168|(0)(0)|171|(0)(0)|174|(0)|620|178|(0)|180|(0)(0)|183|184|185|(0)|437|(0)(0)|440|441|(0)|452|453|454|(0)(0)|461|(0)(0)|464|(0)|584|469|(0)|583|474|475|(0)|580|582|479|(0)|(0)|485|(0)|(0)|578|579|(0)(0)|498|499|(0)(0)|515|(0)|520|192|(0)|195|(0)|436|199|(0)(0)|226|(0)|238|(0)|393|(0)(0)|396|(0)|404|(0)(0)|407|(0)|410|(0)|(0)(0)|418|242|(0)|392|(0)|391|252|(0)|390|257|(0)(0)|(0)(0)|(0)|(0)|289|(0)|(0)|383|317|(0)|381|(0)|380|(0)(0)|(0)|369|(0)(0)|(0)|329|(0)|332|(0)|(0)(0)|348|(0)|351|352))|736|38|(0)(0)|44|(0)(0)|(0)|64|(0)|67|(2:69|71)|717|74|(0)(0)|81|(0)|84|(0)|87|(1:89)|692|(0)|91|(1:93)|691|690|(0)|(0)|688|684|116|(0)|677|121|(0)|676|126|(0)(0)|129|(0)(0)|(0)(0)|141|(0)|672|149|(0)(0)|152|153|154|(0)(0)|157|158|(0)(0)|161|162|(0)|626|627|628|629|167|168|(0)(0)|171|(0)(0)|174|(0)|620|178|(0)|180|(0)(0)|183|184|185|(0)|437|(0)(0)|440|441|(0)|452|453|454|(0)(0)|461|(0)(0)|464|(0)|584|469|(0)|583|474|475|(0)|580|582|479|(0)|(0)|485|(0)|(0)|578|579|(0)(0)|498|499|(0)(0)|515|(0)|520|192|(0)|195|(0)|436|199|(0)(0)|226|(0)|238|(0)|393|(0)(0)|396|(0)|404|(0)(0)|407|(0)|410|(0)|(0)(0)|418|242|(0)|392|(0)|391|252|(0)|390|257|(0)(0)|(0)(0)|(0)|(0)|289|(0)|(0)|383|317|(0)|381|(0)|380|(0)(0)|(0)|369|(0)(0)|(0)|329|(0)|332|(0)|(0)(0)|348|(0)|351|352) */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0798, code lost:
        r1 = r0;
        r3 = r17;
        r2 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:592:0x07a0, code lost:
        r3 = false;
        r1 = r0;
        r2 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:594:0x07ab, code lost:
        r1 = r0;
        r3 = r17;
        r2 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:611:0x07b7, code lost:
        r38 = r20;
        r1 = r0;
        r3 = r17;
        r2 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:625:0x07c6, code lost:
        r38 = r20;
        r3 = r17;
        r1 = r28;
        r2 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:632:0x07d6, code lost:
        r38 = r20;
        r36 = r14;
        r3 = r17;
        r1 = r28;
        r2 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:649:0x07e8, code lost:
        r38 = r20;
        r36 = r14;
        r3 = r17;
        r1 = r28;
        r2 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:669:0x07fc, code lost:
        r38 = r20;
        r33 = false;
        r36 = r14;
        r3 = r17;
        r1 = r28;
        r2 = r29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:683:0x02fb, code lost:
        if (r23.height() != r50.mHeight) goto L115;
     */
    /* JADX WARN: Removed duplicated region for block: B:101:0x02b9  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02bd A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:118:0x0310  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0326  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x033d  */
    /* JADX WARN: Removed duplicated region for block: B:131:0x034f  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x035f  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x0398 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:151:0x03d1  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x0463  */
    /* JADX WARN: Removed duplicated region for block: B:160:0x04af  */
    /* JADX WARN: Removed duplicated region for block: B:164:0x04b5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:170:0x04d0  */
    /* JADX WARN: Removed duplicated region for block: B:173:0x04d8  */
    /* JADX WARN: Removed duplicated region for block: B:176:0x04df A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:182:0x0517  */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0523 A[Catch: RemoteException -> 0x0502, TRY_ENTER, TRY_LEAVE, TryCatch #1 {RemoteException -> 0x0502, blocks: (B:614:0x04ea, B:616:0x04ee, B:617:0x04f5, B:619:0x04fe, B:187:0x0523), top: B:613:0x04ea }] */
    /* JADX WARN: Removed duplicated region for block: B:194:0x0818  */
    /* JADX WARN: Removed duplicated region for block: B:197:0x084c  */
    /* JADX WARN: Removed duplicated region for block: B:201:0x0864  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x091b  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x094b A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:244:0x0a4a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:248:0x0a55  */
    /* JADX WARN: Removed duplicated region for block: B:254:0x0a61  */
    /* JADX WARN: Removed duplicated region for block: B:259:0x0a6e  */
    /* JADX WARN: Removed duplicated region for block: B:273:0x0afa  */
    /* JADX WARN: Removed duplicated region for block: B:275:0x0b0b  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0b19  */
    /* JADX WARN: Removed duplicated region for block: B:291:0x0b82  */
    /* JADX WARN: Removed duplicated region for block: B:314:0x0c18  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x0c27 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:322:0x0c2e  */
    /* JADX WARN: Removed duplicated region for block: B:326:0x0c37  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0c49 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:331:0x0c75  */
    /* JADX WARN: Removed duplicated region for block: B:334:0x0c82 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:337:0x0c88  */
    /* JADX WARN: Removed duplicated region for block: B:350:0x0cf0  */
    /* JADX WARN: Removed duplicated region for block: B:353:0x0cb2  */
    /* JADX WARN: Removed duplicated region for block: B:371:0x0c53  */
    /* JADX WARN: Removed duplicated region for block: B:373:0x0c58  */
    /* JADX WARN: Removed duplicated region for block: B:374:0x0c55  */
    /* JADX WARN: Removed duplicated region for block: B:375:0x0c3c  */
    /* JADX WARN: Removed duplicated region for block: B:384:0x0afe  */
    /* JADX WARN: Removed duplicated region for block: B:389:0x0af4  */
    /* JADX WARN: Removed duplicated region for block: B:395:0x0951  */
    /* JADX WARN: Removed duplicated region for block: B:398:0x095a  */
    /* JADX WARN: Removed duplicated region for block: B:406:0x0989  */
    /* JADX WARN: Removed duplicated region for block: B:409:0x09e8  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a4  */
    /* JADX WARN: Removed duplicated region for block: B:412:0x09fe  */
    /* JADX WARN: Removed duplicated region for block: B:414:0x0a10  */
    /* JADX WARN: Removed duplicated region for block: B:420:0x0a3b  */
    /* JADX WARN: Removed duplicated region for block: B:421:0x09ce  */
    /* JADX WARN: Removed duplicated region for block: B:422:0x0953  */
    /* JADX WARN: Removed duplicated region for block: B:435:0x0913  */
    /* JADX WARN: Removed duplicated region for block: B:439:0x0580  */
    /* JADX WARN: Removed duplicated region for block: B:443:0x058e A[Catch: RemoteException -> 0x0556, TRY_ENTER, TryCatch #22 {RemoteException -> 0x0556, blocks: (B:602:0x0537, B:443:0x058e, B:445:0x0592, B:446:0x05ae, B:449:0x05bc, B:460:0x05d7, B:466:0x0605, B:471:0x0614, B:481:0x063a, B:483:0x0642, B:489:0x0655, B:491:0x065d, B:495:0x0670, B:530:0x067d, B:506:0x0727, B:548:0x06b5, B:550:0x06b9, B:551:0x06bc, B:553:0x06c7, B:554:0x06cd, B:556:0x06d1, B:557:0x06d4, B:559:0x06da, B:565:0x06ec, B:567:0x06f2, B:569:0x06fa, B:571:0x06fd, B:574:0x0708), top: B:601:0x0537, inners: #17 }] */
    /* JADX WARN: Removed duplicated region for block: B:456:0x05ce  */
    /* JADX WARN: Removed duplicated region for block: B:463:0x05f7  */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0605 A[Catch: RemoteException -> 0x0556, TRY_ENTER, TryCatch #22 {RemoteException -> 0x0556, blocks: (B:602:0x0537, B:443:0x058e, B:445:0x0592, B:446:0x05ae, B:449:0x05bc, B:460:0x05d7, B:466:0x0605, B:471:0x0614, B:481:0x063a, B:483:0x0642, B:489:0x0655, B:491:0x065d, B:495:0x0670, B:530:0x067d, B:506:0x0727, B:548:0x06b5, B:550:0x06b9, B:551:0x06bc, B:553:0x06c7, B:554:0x06cd, B:556:0x06d1, B:557:0x06d4, B:559:0x06da, B:565:0x06ec, B:567:0x06f2, B:569:0x06fa, B:571:0x06fd, B:574:0x0708), top: B:601:0x0537, inners: #17 }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:471:0x0614 A[Catch: RemoteException -> 0x0556, TRY_LEAVE, TryCatch #22 {RemoteException -> 0x0556, blocks: (B:602:0x0537, B:443:0x058e, B:445:0x0592, B:446:0x05ae, B:449:0x05bc, B:460:0x05d7, B:466:0x0605, B:471:0x0614, B:481:0x063a, B:483:0x0642, B:489:0x0655, B:491:0x065d, B:495:0x0670, B:530:0x067d, B:506:0x0727, B:548:0x06b5, B:550:0x06b9, B:551:0x06bc, B:553:0x06c7, B:554:0x06cd, B:556:0x06d1, B:557:0x06d4, B:559:0x06da, B:565:0x06ec, B:567:0x06f2, B:569:0x06fa, B:571:0x06fd, B:574:0x0708), top: B:601:0x0537, inners: #17 }] */
    /* JADX WARN: Removed duplicated region for block: B:477:0x0629 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:481:0x063a A[Catch: RemoteException -> 0x0556, TRY_ENTER, TryCatch #22 {RemoteException -> 0x0556, blocks: (B:602:0x0537, B:443:0x058e, B:445:0x0592, B:446:0x05ae, B:449:0x05bc, B:460:0x05d7, B:466:0x0605, B:471:0x0614, B:481:0x063a, B:483:0x0642, B:489:0x0655, B:491:0x065d, B:495:0x0670, B:530:0x067d, B:506:0x0727, B:548:0x06b5, B:550:0x06b9, B:551:0x06bc, B:553:0x06c7, B:554:0x06cd, B:556:0x06d1, B:557:0x06d4, B:559:0x06da, B:565:0x06ec, B:567:0x06f2, B:569:0x06fa, B:571:0x06fd, B:574:0x0708), top: B:601:0x0537, inners: #17 }] */
    /* JADX WARN: Removed duplicated region for block: B:483:0x0642 A[Catch: RemoteException -> 0x0556, TRY_LEAVE, TryCatch #22 {RemoteException -> 0x0556, blocks: (B:602:0x0537, B:443:0x058e, B:445:0x0592, B:446:0x05ae, B:449:0x05bc, B:460:0x05d7, B:466:0x0605, B:471:0x0614, B:481:0x063a, B:483:0x0642, B:489:0x0655, B:491:0x065d, B:495:0x0670, B:530:0x067d, B:506:0x0727, B:548:0x06b5, B:550:0x06b9, B:551:0x06bc, B:553:0x06c7, B:554:0x06cd, B:556:0x06d1, B:557:0x06d4, B:559:0x06da, B:565:0x06ec, B:567:0x06f2, B:569:0x06fa, B:571:0x06fd, B:574:0x0708), top: B:601:0x0537, inners: #17 }] */
    /* JADX WARN: Removed duplicated region for block: B:487:0x0650  */
    /* JADX WARN: Removed duplicated region for block: B:489:0x0655 A[Catch: RemoteException -> 0x0556, TRY_ENTER, TryCatch #22 {RemoteException -> 0x0556, blocks: (B:602:0x0537, B:443:0x058e, B:445:0x0592, B:446:0x05ae, B:449:0x05bc, B:460:0x05d7, B:466:0x0605, B:471:0x0614, B:481:0x063a, B:483:0x0642, B:489:0x0655, B:491:0x065d, B:495:0x0670, B:530:0x067d, B:506:0x0727, B:548:0x06b5, B:550:0x06b9, B:551:0x06bc, B:553:0x06c7, B:554:0x06cd, B:556:0x06d1, B:557:0x06d4, B:559:0x06da, B:565:0x06ec, B:567:0x06f2, B:569:0x06fa, B:571:0x06fd, B:574:0x0708), top: B:601:0x0537, inners: #17 }] */
    /* JADX WARN: Removed duplicated region for block: B:494:0x066f  */
    /* JADX WARN: Removed duplicated region for block: B:501:0x0710  */
    /* JADX WARN: Removed duplicated region for block: B:517:0x077a  */
    /* JADX WARN: Removed duplicated region for block: B:526:0x076e  */
    /* JADX WARN: Removed duplicated region for block: B:547:0x06b3  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:585:0x05f9  */
    /* JADX WARN: Removed duplicated region for block: B:589:0x05ed  */
    /* JADX WARN: Removed duplicated region for block: B:595:0x052c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:612:0x0519  */
    /* JADX WARN: Removed duplicated region for block: B:613:0x04ea A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:621:0x04da  */
    /* JADX WARN: Removed duplicated region for block: B:622:0x04d2  */
    /* JADX WARN: Removed duplicated region for block: B:633:0x046d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:650:0x03eb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:66:0x0192  */
    /* JADX WARN: Removed duplicated region for block: B:670:0x03dc  */
    /* JADX WARN: Removed duplicated region for block: B:673:0x038e  */
    /* JADX WARN: Removed duplicated region for block: B:674:0x0359  */
    /* JADX WARN: Removed duplicated region for block: B:675:0x033f  */
    /* JADX WARN: Removed duplicated region for block: B:694:0x024c  */
    /* JADX WARN: Removed duplicated region for block: B:716:0x021f  */
    /* JADX WARN: Removed duplicated region for block: B:724:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:733:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x022a  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0232  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void performTraversals() {
        boolean z;
        boolean supportsScreen;
        boolean z2;
        WindowManager.LayoutParams params;
        int desiredWindowWidth;
        int desiredWindowHeight;
        boolean layoutRequested;
        Rect frame;
        int i;
        int desiredWindowHeight2;
        int desiredWindowWidth2;
        int resizeMode;
        int desiredWindowWidth3;
        int desiredWindowHeight3;
        boolean windowSizeMayChange;
        int desiredWindowWidth4;
        int desiredWindowHeight4;
        boolean windowShouldResize;
        boolean computesInternalInsets;
        boolean isViewVisible;
        boolean surfaceSizeChanged;
        boolean surfaceCreated;
        boolean surfaceReplaced;
        boolean windowAttributesChanged;
        int relayoutResult;
        WindowManager.LayoutParams params2;
        boolean insetsPending;
        boolean updatedConfiguration;
        Rect frame2;
        boolean viewVisibilityChanged;
        boolean wasReportNextDraw;
        boolean wasReportNextDraw2;
        int relayoutResult2;
        boolean didLayout;
        boolean triggerGlobalLayoutListener;
        boolean changedVisibility;
        boolean regainedFocus;
        boolean cancelDraw;
        boolean isToast;
        Region touchableRegion;
        Rect visibleInsets;
        Rect visibleInsets2;
        BaseSurfaceHolder baseSurfaceHolder;
        boolean surfaceSizeChanged2;
        boolean dispatchApplyInsets;
        boolean insetsPending2;
        boolean hwInitialized;
        boolean hwInitialized2;
        int relayoutResult3;
        boolean insetsPending3;
        boolean dispatchApplyInsets2;
        boolean surfaceSizeChanged3;
        ThreadedRenderer threadedRenderer;
        boolean focusChangedDueToTouchMode;
        boolean measureAgain;
        int relayoutResult4;
        boolean updatedConfiguration2;
        int relayoutResult5;
        boolean dragResizing;
        String str;
        StringBuilder sb;
        boolean alwaysConsumeSystemBarsChanged;
        boolean dragResizing2;
        int i2;
        int desiredWindowHeight5;
        int desiredWindowWidth5;
        View host = this.mView;
        if (DBG) {
            System.out.println("======================================");
            System.out.println("performTraversals");
            host.debug();
        }
        if (host == null || !this.mAdded) {
            return;
        }
        if (this.mWaitForBlastSyncComplete) {
            if (DEBUG_BLAST) {
                Log.w(this.mTag, "Can't perform draw while waiting for a transaction complete");
            }
            this.mRequestedTraverseWhilePaused = true;
            return;
        }
        this.mIsInTraversal = true;
        this.mWillDrawSoon = true;
        boolean windowSizeMayChange2 = false;
        WindowManager.LayoutParams lp = this.mWindowAttributes;
        if (this.mVoteMinRefreshRateNum > 0 && lp.preferredDisplayModeId == 0) {
            lp.preferredDisplayModeId = this.mDisplay.getModeIdByRefreshRate(60.0f);
            this.mUseMinRefreshRate = true;
            this.mWindowAttributesChanged = true;
        } else if (this.mVoteMinRefreshRateNum <= 0 && this.mUseMinRefreshRate) {
            lp.preferredDisplayModeId = 0;
            this.mVoteMinRefreshRateNum = 0;
            this.mUseMinRefreshRate = false;
            this.mWindowAttributesChanged = true;
        }
        int viewVisibility = getHostVisibility();
        boolean z3 = this.mFirst;
        boolean viewVisibilityChanged2 = !z3 && (this.mViewVisibility != viewVisibility || this.mNewSurfaceNeeded || this.mAppVisibilityChanged);
        this.mAppVisibilityChanged = false;
        if (!z3) {
            if ((this.mViewVisibility == 0) != (viewVisibility == 0)) {
                z = true;
                boolean viewUserVisibilityChanged = z;
                CompatibilityInfo compatibilityInfo = this.mDisplay.getDisplayAdjustments().getCompatibilityInfo();
                supportsScreen = compatibilityInfo.supportsScreen();
                z2 = this.mLastInCompatMode;
                if (supportsScreen != z2) {
                    this.mFullRedrawNeeded = true;
                    this.mLayoutRequested = true;
                    if (z2) {
                        lp.privateFlags &= -129;
                        this.mLastInCompatMode = false;
                    } else {
                        lp.privateFlags |= 128;
                        this.mLastInCompatMode = true;
                    }
                    params = lp;
                } else {
                    params = null;
                }
                Rect frame3 = this.mWinFrame;
                if (!this.mFirst) {
                    this.mFullRedrawNeeded = true;
                    this.mLayoutRequested = true;
                    Configuration config = getConfiguration();
                    if (shouldUseDisplaySize(lp)) {
                        Point size = new Point();
                        this.mDisplay.getRealSize(size);
                        desiredWindowWidth = size.x;
                        desiredWindowHeight = size.y;
                    } else if (lp.width == -2 || lp.height == -2) {
                        desiredWindowWidth = dipToPx(config.screenWidthDp);
                        desiredWindowHeight = dipToPx(config.screenHeightDp);
                    } else {
                        desiredWindowWidth = frame3.width();
                        desiredWindowHeight = frame3.height();
                    }
                    this.mAttachInfo.mUse32BitDrawingCache = true;
                    this.mAttachInfo.mWindowVisibility = viewVisibility;
                    this.mAttachInfo.mRecomputeGlobalAttributes = false;
                    this.mLastConfigurationFromResources.setTo(config);
                    this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                    if (this.mViewLayoutDirectionInitial == 2) {
                        host.setLayoutDirection(config.getLayoutDirection());
                    }
                    host.dispatchAttachedToWindow(this.mAttachInfo, 0);
                    this.mAttachInfo.mTreeObserver.dispatchOnWindowAttachedChange(true);
                    dispatchApplyInsets(host);
                } else {
                    desiredWindowWidth = frame3.width();
                    desiredWindowHeight = frame3.height();
                    if (desiredWindowWidth != this.mWidth || desiredWindowHeight != this.mHeight) {
                        if (DEBUG_ORIENTATION) {
                            Log.v(this.mTag, "View " + host + " resized to: " + frame3);
                        }
                        this.mFullRedrawNeeded = true;
                        this.mLayoutRequested = true;
                        windowSizeMayChange2 = true;
                    }
                }
                if (viewVisibilityChanged2) {
                    this.mAttachInfo.mWindowVisibility = viewVisibility;
                    host.dispatchWindowVisibilityChanged(viewVisibility);
                    if (viewUserVisibilityChanged) {
                        host.dispatchVisibilityAggregated(viewVisibility == 0);
                    }
                    if (viewVisibility != 0 || this.mNewSurfaceNeeded) {
                        endDragResizing();
                        destroyHardwareResources();
                    }
                }
                if (this.mAttachInfo.mWindowVisibility != 0) {
                    host.clearAccessibilityFocus();
                }
                getRunQueue().executeActions(this.mAttachInfo.mHandler);
                layoutRequested = !this.mLayoutRequested && (!this.mStopped || this.mReportNextDraw);
                if (!layoutRequested) {
                    Resources res = this.mView.getContext().getResources();
                    if (this.mFirst) {
                        this.mAttachInfo.mInTouchMode = !this.mAddedTouchMode;
                        ensureTouchModeLocally(this.mAddedTouchMode);
                        i2 = -2;
                    } else {
                        i2 = -2;
                        if (lp.width == -2 || lp.height == -2) {
                            windowSizeMayChange2 = true;
                            if (shouldUseDisplaySize(lp)) {
                                Point size2 = new Point();
                                this.mDisplay.getRealSize(size2);
                                int desiredWindowWidth6 = size2.x;
                                desiredWindowHeight5 = size2.y;
                                desiredWindowWidth5 = desiredWindowWidth6;
                            } else {
                                Configuration config2 = res.getConfiguration();
                                int desiredWindowWidth7 = dipToPx(config2.screenWidthDp);
                                desiredWindowHeight5 = dipToPx(config2.screenHeightDp);
                                desiredWindowWidth5 = desiredWindowWidth7;
                            }
                            i = i2;
                            desiredWindowWidth2 = desiredWindowWidth5;
                            frame = frame3;
                            desiredWindowHeight2 = desiredWindowHeight5;
                            windowSizeMayChange2 |= measureHierarchy(host, lp, res, desiredWindowWidth2, desiredWindowHeight2);
                        }
                    }
                    desiredWindowHeight5 = desiredWindowHeight;
                    desiredWindowWidth5 = desiredWindowWidth;
                    i = i2;
                    desiredWindowWidth2 = desiredWindowWidth5;
                    frame = frame3;
                    desiredWindowHeight2 = desiredWindowHeight5;
                    windowSizeMayChange2 |= measureHierarchy(host, lp, res, desiredWindowWidth2, desiredWindowHeight2);
                } else {
                    frame = frame3;
                    i = -2;
                    desiredWindowHeight2 = desiredWindowHeight;
                    desiredWindowWidth2 = desiredWindowWidth;
                }
                if (collectViewAttributes()) {
                    params = lp;
                }
                if (this.mAttachInfo.mForceReportNewAttributes) {
                    this.mAttachInfo.mForceReportNewAttributes = false;
                    params = lp;
                }
                if (!this.mFirst || this.mAttachInfo.mViewVisibilityChanged) {
                    this.mAttachInfo.mViewVisibilityChanged = false;
                    resizeMode = this.mSoftInputMode & 240;
                    if (resizeMode == 0) {
                        int N = this.mAttachInfo.mScrollContainers.size();
                        for (int i3 = 0; i3 < N; i3++) {
                            if (this.mAttachInfo.mScrollContainers.get(i3).isShown()) {
                                resizeMode = 16;
                            }
                        }
                        if (resizeMode == 0) {
                            resizeMode = 32;
                        }
                        if ((lp.softInputMode & 240) != resizeMode) {
                            lp.softInputMode = (lp.softInputMode & TrafficStats.TAG_SYSTEM_IMPERSONATION_RANGE_END) | resizeMode;
                            params = lp;
                        }
                    }
                }
                if (this.mApplyInsetsRequested || this.mWillMove || this.mWillResize) {
                    desiredWindowWidth3 = desiredWindowWidth2;
                    desiredWindowHeight3 = desiredWindowHeight2;
                } else {
                    dispatchApplyInsets(host);
                    if (this.mLayoutRequested) {
                        desiredWindowWidth3 = desiredWindowWidth2;
                        desiredWindowHeight3 = desiredWindowHeight2;
                        windowSizeMayChange = windowSizeMayChange2 | measureHierarchy(host, lp, this.mView.getContext().getResources(), desiredWindowWidth2, desiredWindowHeight2);
                        if (layoutRequested) {
                            this.mLayoutRequested = false;
                        }
                        if (!layoutRequested && windowSizeMayChange) {
                            if (this.mWidth == host.getMeasuredWidth() && this.mHeight == host.getMeasuredHeight()) {
                                if (lp.width == i) {
                                    desiredWindowWidth4 = desiredWindowWidth3;
                                    if (frame.width() < desiredWindowWidth4 && frame.width() != this.mWidth) {
                                        desiredWindowHeight4 = desiredWindowHeight3;
                                    }
                                } else {
                                    desiredWindowWidth4 = desiredWindowWidth3;
                                }
                                if (lp.height == i) {
                                    desiredWindowHeight4 = desiredWindowHeight3;
                                    if (frame.height() < desiredWindowHeight4) {
                                    }
                                } else {
                                    desiredWindowHeight4 = desiredWindowHeight3;
                                }
                            } else {
                                desiredWindowWidth4 = desiredWindowWidth3;
                                desiredWindowHeight4 = desiredWindowHeight3;
                            }
                            windowShouldResize = true;
                            boolean windowShouldResize2 = windowShouldResize | (!this.mDragResizing && this.mResizeMode == 0) | this.mActivityRelaunched;
                            computesInternalInsets = !this.mAttachInfo.mTreeObserver.hasComputeInternalInsetsListeners() || this.mAttachInfo.mHasNonEmptyGivenInternalInsets;
                            int surfaceGenerationId = this.mSurface.getGenerationId();
                            isViewVisible = viewVisibility != 0;
                            boolean windowRelayoutWasForced = this.mForceNextWindowRelayout;
                            surfaceSizeChanged = false;
                            surfaceCreated = false;
                            boolean surfaceDestroyed = false;
                            surfaceReplaced = false;
                            windowAttributesChanged = this.mWindowAttributesChanged;
                            if (!windowAttributesChanged) {
                                relayoutResult = 0;
                                this.mWindowAttributesChanged = false;
                                params2 = lp;
                            } else {
                                relayoutResult = 0;
                                params2 = params;
                            }
                            if (params2 == null) {
                                insetsPending = false;
                                if ((host.mPrivateFlags & 512) != 0 && !PixelFormat.formatHasAlpha(params2.format)) {
                                    params2.format = -3;
                                }
                                adjustLayoutParamsForCompatibility(params2);
                                controlInsetsForCompatibility(params2);
                                updatedConfiguration = false;
                                if (this.mDispatchedSystemBarAppearance != params2.insetsFlags.appearance) {
                                    int i4 = params2.insetsFlags.appearance;
                                    this.mDispatchedSystemBarAppearance = i4;
                                    this.mView.onSystemBarAppearanceChanged(i4);
                                }
                            } else {
                                insetsPending = false;
                                updatedConfiguration = false;
                            }
                            boolean updatedConfiguration3 = this.mReportNextDraw;
                            if (!this.mFirst || windowShouldResize2 || viewVisibilityChanged2 || params2 != null) {
                                frame2 = frame;
                            } else if (!this.mForceNextWindowRelayout) {
                                maybeHandleWindowMove(frame);
                                wasReportNextDraw = updatedConfiguration3;
                                viewVisibilityChanged = viewVisibilityChanged2;
                                relayoutResult2 = relayoutResult;
                                wasReportNextDraw2 = updatedConfiguration;
                                if (!surfaceSizeChanged || surfaceReplaced || surfaceCreated || windowAttributesChanged) {
                                    prepareSurfaces();
                                }
                                didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                if (didLayout) {
                                    performLayout(lp, this.mWidth, this.mHeight);
                                    if ((host.mPrivateFlags & 512) != 0) {
                                        host.getLocationInWindow(this.mTmpLocation);
                                        Region region = this.mTransparentRegion;
                                        int[] iArr = this.mTmpLocation;
                                        region.set(iArr[0], iArr[1], (iArr[0] + host.mRight) - host.mLeft, (this.mTmpLocation[1] + host.mBottom) - host.mTop);
                                        host.gatherTransparentRegion(this.mTransparentRegion);
                                        CompatibilityInfo.Translator translator = this.mTranslator;
                                        if (translator != null) {
                                            translator.translateRegionInWindowToScreen(this.mTransparentRegion);
                                        }
                                        if (!this.mTransparentRegion.equals(this.mPreviousTransparentRegion)) {
                                            this.mPreviousTransparentRegion.set(this.mTransparentRegion);
                                            this.mFullRedrawNeeded = true;
                                            SurfaceControl sc = getSurfaceControl();
                                            if (sc.isValid()) {
                                                this.mTransaction.setTransparentRegionHint(sc, this.mTransparentRegion).apply();
                                            }
                                        }
                                    }
                                    if (DBG) {
                                        System.out.println("======================================");
                                        System.out.println("performTraversals -- after setFrame");
                                        host.debug();
                                    }
                                }
                                if (surfaceCreated) {
                                    notifySurfaceCreated();
                                } else if (surfaceReplaced) {
                                    notifySurfaceReplaced();
                                } else if (surfaceDestroyed) {
                                    notifySurfaceDestroyed();
                                }
                                if (triggerGlobalLayoutListener) {
                                    this.mAttachInfo.mRecomputeGlobalAttributes = false;
                                    this.mAttachInfo.mTreeObserver.dispatchOnGlobalLayout();
                                }
                                if (computesInternalInsets) {
                                    ViewTreeObserver.InternalInsetsInfo insets = this.mAttachInfo.mGivenInternalInsets;
                                    insets.reset();
                                    this.mAttachInfo.mTreeObserver.dispatchOnComputeInternalInsets(insets);
                                    this.mAttachInfo.mHasNonEmptyGivenInternalInsets = !insets.isEmpty();
                                    if (insetsPending || !this.mLastGivenInsets.equals(insets)) {
                                        this.mLastGivenInsets.set(insets);
                                        CompatibilityInfo.Translator translator2 = this.mTranslator;
                                        if (translator2 != null) {
                                            Rect contentInsets = translator2.getTranslatedContentInsets(insets.contentInsets);
                                            Rect visibleInsets3 = this.mTranslator.getTranslatedVisibleInsets(insets.visibleInsets);
                                            touchableRegion = this.mTranslator.getTranslatedTouchableArea(insets.touchableRegion);
                                            visibleInsets = visibleInsets3;
                                            visibleInsets2 = contentInsets;
                                        } else {
                                            Rect contentInsets2 = insets.contentInsets;
                                            Rect visibleInsets4 = insets.visibleInsets;
                                            touchableRegion = insets.touchableRegion;
                                            visibleInsets = visibleInsets4;
                                            visibleInsets2 = contentInsets2;
                                        }
                                        try {
                                            this.mWindowSession.setInsets(this.mWindow, insets.mTouchableInsets, visibleInsets2, visibleInsets, touchableRegion);
                                        } catch (RemoteException e) {
                                        }
                                    }
                                }
                                if (this.mFirst) {
                                    if (sAlwaysAssignFocus || !isInTouchMode()) {
                                        if (DEBUG_INPUT_RESIZE) {
                                            Log.v(this.mTag, "First: mView.hasFocus()=" + this.mView.hasFocus());
                                        }
                                        View view = this.mView;
                                        if (view != null) {
                                            if (!view.hasFocus()) {
                                                this.mView.restoreDefaultFocus();
                                                if (DEBUG_INPUT_RESIZE) {
                                                    Log.v(this.mTag, "First: requested focused view=" + this.mView.findFocus());
                                                }
                                            } else if (DEBUG_INPUT_RESIZE) {
                                                Log.v(this.mTag, "First: existing focused view=" + this.mView.findFocus());
                                            }
                                        }
                                    } else {
                                        View focused = this.mView.findFocus();
                                        if ((focused instanceof ViewGroup) && ((ViewGroup) focused).getDescendantFocusability() == 262144) {
                                            focused.restoreDefaultFocus();
                                        }
                                    }
                                }
                                changedVisibility = (!viewVisibilityChanged || this.mFirst) && isViewVisible;
                                boolean hasWindowFocus = !this.mAttachInfo.mHasWindowFocus && isViewVisible;
                                regainedFocus = !hasWindowFocus && this.mLostWindowFocus;
                                if (regainedFocus) {
                                    this.mLostWindowFocus = false;
                                    cancelDraw = true;
                                } else if (hasWindowFocus || !this.mHadWindowFocus) {
                                    cancelDraw = true;
                                } else {
                                    cancelDraw = true;
                                    this.mLostWindowFocus = true;
                                }
                                if (!changedVisibility || regainedFocus) {
                                    isToast = this.mWindowAttributes.type == 2005 ? cancelDraw : false;
                                    if (!isToast) {
                                        host.sendAccessibilityEvent(32);
                                    }
                                }
                                this.mFirst = false;
                                this.mWillDrawSoon = false;
                                this.mNewSurfaceNeeded = false;
                                this.mActivityRelaunched = false;
                                this.mViewVisibility = viewVisibility;
                                this.mHadWindowFocus = hasWindowFocus;
                                this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                if ((relayoutResult2 & 2) != 0) {
                                    reportNextDraw();
                                }
                                if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw() && isViewVisible) {
                                    cancelDraw = false;
                                }
                                if (!cancelDraw) {
                                    ArrayList<LayoutTransition> arrayList = this.mPendingTransitions;
                                    if (arrayList != null && arrayList.size() > 0) {
                                        for (int i5 = 0; i5 < this.mPendingTransitions.size(); i5++) {
                                            this.mPendingTransitions.get(i5).startChangingAnimations();
                                        }
                                        this.mPendingTransitions.clear();
                                    }
                                    performDraw();
                                } else if (isViewVisible) {
                                    scheduleTraversals();
                                } else {
                                    ArrayList<LayoutTransition> arrayList2 = this.mPendingTransitions;
                                    if (arrayList2 != null && arrayList2.size() > 0) {
                                        for (int i6 = 0; i6 < this.mPendingTransitions.size(); i6++) {
                                            this.mPendingTransitions.get(i6).endChangingAnimations();
                                        }
                                        this.mPendingTransitions.clear();
                                    }
                                    if (!wasReportNextDraw && this.mReportNextDraw) {
                                        this.mReportNextDraw = false;
                                        pendingDrawFinished();
                                    }
                                }
                                if (this.mAttachInfo.mContentCaptureEvents != null) {
                                    notifyContentCatpureEvents();
                                }
                                this.mIsInTraversal = false;
                            } else {
                                frame2 = frame;
                            }
                            wasReportNextDraw = updatedConfiguration3;
                            this.mForceNextWindowRelayout = false;
                            baseSurfaceHolder = this.mSurfaceHolder;
                            if (baseSurfaceHolder == null) {
                                baseSurfaceHolder.mSurfaceLock.lock();
                                surfaceSizeChanged2 = false;
                                this.mDrawingAllowed = true;
                            } else {
                                surfaceSizeChanged2 = false;
                            }
                            dispatchApplyInsets = false;
                            boolean hadSurface = this.mSurface.isValid();
                            if (!DEBUG_LAYOUT) {
                                try {
                                    String str2 = this.mTag;
                                    hwInitialized = false;
                                    try {
                                        StringBuilder sb2 = new StringBuilder();
                                        try {
                                            sb2.append("host=w:");
                                            sb2.append(host.getMeasuredWidth());
                                            sb2.append(", h:");
                                            sb2.append(host.getMeasuredHeight());
                                            sb2.append(", params=");
                                            sb2.append(params2);
                                            Log.i(str2, sb2.toString());
                                        } catch (RemoteException e2) {
                                            insetsPending2 = computesInternalInsets;
                                            viewVisibilityChanged = viewVisibilityChanged2;
                                            hwInitialized2 = surfaceSizeChanged2;
                                            relayoutResult3 = relayoutResult;
                                            insetsPending3 = updatedConfiguration;
                                            dispatchApplyInsets2 = dispatchApplyInsets;
                                            if (DEBUG_ORIENTATION) {
                                            }
                                            this.mAttachInfo.mWindowLeft = frame2.left;
                                            this.mAttachInfo.mWindowTop = frame2.top;
                                            if (this.mWidth == frame2.width()) {
                                            }
                                            this.mWidth = frame2.width();
                                            this.mHeight = frame2.height();
                                            if (this.mSurfaceHolder != null) {
                                            }
                                            threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                                            if (threadedRenderer != null) {
                                            }
                                            if (this.mStopped) {
                                            }
                                            focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                                            if (!focusChangedDueToTouchMode) {
                                            }
                                            int childWidthMeasureSpec = getRootMeasureSpec(this.mWidth, lp.width);
                                            int childHeightMeasureSpec = getRootMeasureSpec(this.mHeight, lp.height);
                                            if (DEBUG_LAYOUT) {
                                            }
                                            performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                                            int width = host.getMeasuredWidth();
                                            int height = host.getMeasuredHeight();
                                            measureAgain = false;
                                            relayoutResult4 = relayoutResult3;
                                            updatedConfiguration2 = insetsPending3;
                                            if (lp.horizontalWeight > 0.0f) {
                                            }
                                            if (lp.verticalWeight > 0.0f) {
                                            }
                                            if (measureAgain) {
                                            }
                                            layoutRequested = true;
                                            surfaceSizeChanged = surfaceSizeChanged3;
                                            relayoutResult2 = relayoutResult4;
                                            insetsPending = insetsPending2;
                                            wasReportNextDraw2 = updatedConfiguration2;
                                            if (!surfaceSizeChanged) {
                                            }
                                            prepareSurfaces();
                                            didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                            triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                            if (didLayout) {
                                            }
                                            if (surfaceCreated) {
                                            }
                                            if (triggerGlobalLayoutListener) {
                                            }
                                            if (computesInternalInsets) {
                                            }
                                            if (this.mFirst) {
                                            }
                                            if (!viewVisibilityChanged) {
                                            }
                                            if (!this.mAttachInfo.mHasWindowFocus) {
                                            }
                                            if (!hasWindowFocus) {
                                            }
                                            if (regainedFocus) {
                                            }
                                            if (!changedVisibility) {
                                            }
                                            if (this.mWindowAttributes.type == 2005) {
                                            }
                                            if (!isToast) {
                                            }
                                            this.mFirst = false;
                                            this.mWillDrawSoon = false;
                                            this.mNewSurfaceNeeded = false;
                                            this.mActivityRelaunched = false;
                                            this.mViewVisibility = viewVisibility;
                                            this.mHadWindowFocus = hasWindowFocus;
                                            this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                            if ((relayoutResult2 & 2) != 0) {
                                            }
                                            if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                            }
                                            if (!cancelDraw) {
                                            }
                                            if (this.mAttachInfo.mContentCaptureEvents != null) {
                                            }
                                            this.mIsInTraversal = false;
                                        }
                                    } catch (RemoteException e3) {
                                        insetsPending2 = computesInternalInsets;
                                        viewVisibilityChanged = viewVisibilityChanged2;
                                        hwInitialized2 = surfaceSizeChanged2;
                                        relayoutResult3 = relayoutResult;
                                        insetsPending3 = updatedConfiguration;
                                    }
                                } catch (RemoteException e4) {
                                    hwInitialized = false;
                                    insetsPending2 = computesInternalInsets;
                                    viewVisibilityChanged = viewVisibilityChanged2;
                                    hwInitialized2 = surfaceSizeChanged2;
                                    relayoutResult3 = relayoutResult;
                                    insetsPending3 = updatedConfiguration;
                                }
                            } else {
                                hwInitialized = false;
                            }
                            if (this.mAttachInfo.mThreadedRenderer == null) {
                                try {
                                    if (this.mAttachInfo.mThreadedRenderer.pause()) {
                                        try {
                                            this.mDirty.set(0, 0, this.mWidth, this.mHeight);
                                        } catch (RemoteException e5) {
                                            insetsPending2 = computesInternalInsets;
                                            viewVisibilityChanged = viewVisibilityChanged2;
                                            hwInitialized2 = surfaceSizeChanged2;
                                            relayoutResult3 = relayoutResult;
                                            insetsPending3 = updatedConfiguration;
                                            dispatchApplyInsets2 = dispatchApplyInsets;
                                            if (DEBUG_ORIENTATION) {
                                            }
                                            this.mAttachInfo.mWindowLeft = frame2.left;
                                            this.mAttachInfo.mWindowTop = frame2.top;
                                            if (this.mWidth == frame2.width()) {
                                            }
                                            this.mWidth = frame2.width();
                                            this.mHeight = frame2.height();
                                            if (this.mSurfaceHolder != null) {
                                            }
                                            threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                                            if (threadedRenderer != null) {
                                            }
                                            if (this.mStopped) {
                                            }
                                            focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                                            if (!focusChangedDueToTouchMode) {
                                            }
                                            int childWidthMeasureSpec2 = getRootMeasureSpec(this.mWidth, lp.width);
                                            int childHeightMeasureSpec2 = getRootMeasureSpec(this.mHeight, lp.height);
                                            if (DEBUG_LAYOUT) {
                                            }
                                            performMeasure(childWidthMeasureSpec2, childHeightMeasureSpec2);
                                            int width2 = host.getMeasuredWidth();
                                            int height2 = host.getMeasuredHeight();
                                            measureAgain = false;
                                            relayoutResult4 = relayoutResult3;
                                            updatedConfiguration2 = insetsPending3;
                                            if (lp.horizontalWeight > 0.0f) {
                                            }
                                            if (lp.verticalWeight > 0.0f) {
                                            }
                                            if (measureAgain) {
                                            }
                                            layoutRequested = true;
                                            surfaceSizeChanged = surfaceSizeChanged3;
                                            relayoutResult2 = relayoutResult4;
                                            insetsPending = insetsPending2;
                                            wasReportNextDraw2 = updatedConfiguration2;
                                            if (!surfaceSizeChanged) {
                                            }
                                            prepareSurfaces();
                                            didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                            triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                            if (didLayout) {
                                            }
                                            if (surfaceCreated) {
                                            }
                                            if (triggerGlobalLayoutListener) {
                                            }
                                            if (computesInternalInsets) {
                                            }
                                            if (this.mFirst) {
                                            }
                                            if (!viewVisibilityChanged) {
                                            }
                                            if (!this.mAttachInfo.mHasWindowFocus) {
                                            }
                                            if (!hasWindowFocus) {
                                            }
                                            if (regainedFocus) {
                                            }
                                            if (!changedVisibility) {
                                            }
                                            if (this.mWindowAttributes.type == 2005) {
                                            }
                                            if (!isToast) {
                                            }
                                            this.mFirst = false;
                                            this.mWillDrawSoon = false;
                                            this.mNewSurfaceNeeded = false;
                                            this.mActivityRelaunched = false;
                                            this.mViewVisibility = viewVisibility;
                                            this.mHadWindowFocus = hasWindowFocus;
                                            this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                            if ((relayoutResult2 & 2) != 0) {
                                            }
                                            if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                            }
                                            if (!cancelDraw) {
                                            }
                                            if (this.mAttachInfo.mContentCaptureEvents != null) {
                                            }
                                            this.mIsInTraversal = false;
                                        }
                                    }
                                } catch (RemoteException e6) {
                                    insetsPending2 = computesInternalInsets;
                                    viewVisibilityChanged = viewVisibilityChanged2;
                                    hwInitialized2 = surfaceSizeChanged2;
                                    relayoutResult3 = relayoutResult;
                                    insetsPending3 = updatedConfiguration;
                                }
                            }
                            if (!this.mFirst && !viewVisibilityChanged2) {
                                viewVisibilityChanged = viewVisibilityChanged2;
                                relayoutResult5 = relayoutWindow(params2, viewVisibility, computesInternalInsets);
                                boolean freeformResizing = (relayoutResult5 & 16) == 0;
                                boolean dockedResizing = (relayoutResult5 & 8) == 0;
                                dragResizing = !freeformResizing || dockedResizing;
                                if ((relayoutResult5 & 128) != 0) {
                                    try {
                                        if (DEBUG_BLAST) {
                                            Log.d(this.mTag, "Relayout called with blastSync");
                                        }
                                        reportNextDraw();
                                        if (isHardwareEnabled()) {
                                            this.mNextDrawUseBlastSync = true;
                                        }
                                    } catch (RemoteException e7) {
                                        insetsPending2 = computesInternalInsets;
                                        relayoutResult3 = relayoutResult5;
                                        hwInitialized2 = surfaceSizeChanged2;
                                        insetsPending3 = updatedConfiguration;
                                        dispatchApplyInsets2 = dispatchApplyInsets;
                                        if (DEBUG_ORIENTATION) {
                                        }
                                        this.mAttachInfo.mWindowLeft = frame2.left;
                                        this.mAttachInfo.mWindowTop = frame2.top;
                                        if (this.mWidth == frame2.width()) {
                                        }
                                        this.mWidth = frame2.width();
                                        this.mHeight = frame2.height();
                                        if (this.mSurfaceHolder != null) {
                                        }
                                        threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                                        if (threadedRenderer != null) {
                                        }
                                        if (this.mStopped) {
                                        }
                                        focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                                        if (!focusChangedDueToTouchMode) {
                                        }
                                        int childWidthMeasureSpec22 = getRootMeasureSpec(this.mWidth, lp.width);
                                        int childHeightMeasureSpec22 = getRootMeasureSpec(this.mHeight, lp.height);
                                        if (DEBUG_LAYOUT) {
                                        }
                                        performMeasure(childWidthMeasureSpec22, childHeightMeasureSpec22);
                                        int width22 = host.getMeasuredWidth();
                                        int height22 = host.getMeasuredHeight();
                                        measureAgain = false;
                                        relayoutResult4 = relayoutResult3;
                                        updatedConfiguration2 = insetsPending3;
                                        if (lp.horizontalWeight > 0.0f) {
                                        }
                                        if (lp.verticalWeight > 0.0f) {
                                        }
                                        if (measureAgain) {
                                        }
                                        layoutRequested = true;
                                        surfaceSizeChanged = surfaceSizeChanged3;
                                        relayoutResult2 = relayoutResult4;
                                        insetsPending = insetsPending2;
                                        wasReportNextDraw2 = updatedConfiguration2;
                                        if (!surfaceSizeChanged) {
                                        }
                                        prepareSurfaces();
                                        didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                        triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                        if (didLayout) {
                                        }
                                        if (surfaceCreated) {
                                        }
                                        if (triggerGlobalLayoutListener) {
                                        }
                                        if (computesInternalInsets) {
                                        }
                                        if (this.mFirst) {
                                        }
                                        if (!viewVisibilityChanged) {
                                        }
                                        if (!this.mAttachInfo.mHasWindowFocus) {
                                        }
                                        if (!hasWindowFocus) {
                                        }
                                        if (regainedFocus) {
                                        }
                                        if (!changedVisibility) {
                                        }
                                        if (this.mWindowAttributes.type == 2005) {
                                        }
                                        if (!isToast) {
                                        }
                                        this.mFirst = false;
                                        this.mWillDrawSoon = false;
                                        this.mNewSurfaceNeeded = false;
                                        this.mActivityRelaunched = false;
                                        this.mViewVisibility = viewVisibility;
                                        this.mHadWindowFocus = hasWindowFocus;
                                        this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                        if ((relayoutResult2 & 2) != 0) {
                                        }
                                        if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                        }
                                        if (!cancelDraw) {
                                        }
                                        if (this.mAttachInfo.mContentCaptureEvents != null) {
                                        }
                                        this.mIsInTraversal = false;
                                    }
                                }
                                boolean surfaceControlChanged = (relayoutResult5 & 4) != 4;
                                if (this.mSurfaceControl.isValid()) {
                                    updateOpacity(this.mWindowAttributes, dragResizing, surfaceControlChanged);
                                }
                                if (!DEBUG_LAYOUT) {
                                    try {
                                        str = this.mTag;
                                        try {
                                            sb = new StringBuilder();
                                            insetsPending2 = computesInternalInsets;
                                        } catch (RemoteException e8) {
                                            insetsPending2 = computesInternalInsets;
                                            relayoutResult3 = relayoutResult5;
                                            hwInitialized2 = surfaceSizeChanged2;
                                            insetsPending3 = updatedConfiguration;
                                            dispatchApplyInsets2 = dispatchApplyInsets;
                                            if (DEBUG_ORIENTATION) {
                                            }
                                            this.mAttachInfo.mWindowLeft = frame2.left;
                                            this.mAttachInfo.mWindowTop = frame2.top;
                                            if (this.mWidth == frame2.width()) {
                                            }
                                            this.mWidth = frame2.width();
                                            this.mHeight = frame2.height();
                                            if (this.mSurfaceHolder != null) {
                                            }
                                            threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                                            if (threadedRenderer != null) {
                                            }
                                            if (this.mStopped) {
                                            }
                                            focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                                            if (!focusChangedDueToTouchMode) {
                                            }
                                            int childWidthMeasureSpec222 = getRootMeasureSpec(this.mWidth, lp.width);
                                            int childHeightMeasureSpec222 = getRootMeasureSpec(this.mHeight, lp.height);
                                            if (DEBUG_LAYOUT) {
                                            }
                                            performMeasure(childWidthMeasureSpec222, childHeightMeasureSpec222);
                                            int width222 = host.getMeasuredWidth();
                                            int height222 = host.getMeasuredHeight();
                                            measureAgain = false;
                                            relayoutResult4 = relayoutResult3;
                                            updatedConfiguration2 = insetsPending3;
                                            if (lp.horizontalWeight > 0.0f) {
                                            }
                                            if (lp.verticalWeight > 0.0f) {
                                            }
                                            if (measureAgain) {
                                            }
                                            layoutRequested = true;
                                            surfaceSizeChanged = surfaceSizeChanged3;
                                            relayoutResult2 = relayoutResult4;
                                            insetsPending = insetsPending2;
                                            wasReportNextDraw2 = updatedConfiguration2;
                                            if (!surfaceSizeChanged) {
                                            }
                                            prepareSurfaces();
                                            didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                            triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                            if (didLayout) {
                                            }
                                            if (surfaceCreated) {
                                            }
                                            if (triggerGlobalLayoutListener) {
                                            }
                                            if (computesInternalInsets) {
                                            }
                                            if (this.mFirst) {
                                            }
                                            if (!viewVisibilityChanged) {
                                            }
                                            if (!this.mAttachInfo.mHasWindowFocus) {
                                            }
                                            if (!hasWindowFocus) {
                                            }
                                            if (regainedFocus) {
                                            }
                                            if (!changedVisibility) {
                                            }
                                            if (this.mWindowAttributes.type == 2005) {
                                            }
                                            if (!isToast) {
                                            }
                                            this.mFirst = false;
                                            this.mWillDrawSoon = false;
                                            this.mNewSurfaceNeeded = false;
                                            this.mActivityRelaunched = false;
                                            this.mViewVisibility = viewVisibility;
                                            this.mHadWindowFocus = hasWindowFocus;
                                            this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                            if ((relayoutResult2 & 2) != 0) {
                                            }
                                            if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                            }
                                            if (!cancelDraw) {
                                            }
                                            if (this.mAttachInfo.mContentCaptureEvents != null) {
                                            }
                                            this.mIsInTraversal = false;
                                        }
                                    } catch (RemoteException e9) {
                                        insetsPending2 = computesInternalInsets;
                                        relayoutResult3 = relayoutResult5;
                                        hwInitialized2 = surfaceSizeChanged2;
                                        insetsPending3 = updatedConfiguration;
                                    }
                                    try {
                                        sb.append("relayout: frame=");
                                        sb.append(frame2.toShortString());
                                        sb.append(" surface=");
                                        sb.append(this.mSurface);
                                        Log.v(str, sb.toString());
                                    } catch (RemoteException e10) {
                                        relayoutResult3 = relayoutResult5;
                                        hwInitialized2 = surfaceSizeChanged2;
                                        insetsPending3 = updatedConfiguration;
                                        dispatchApplyInsets2 = dispatchApplyInsets;
                                        if (DEBUG_ORIENTATION) {
                                        }
                                        this.mAttachInfo.mWindowLeft = frame2.left;
                                        this.mAttachInfo.mWindowTop = frame2.top;
                                        if (this.mWidth == frame2.width()) {
                                        }
                                        this.mWidth = frame2.width();
                                        this.mHeight = frame2.height();
                                        if (this.mSurfaceHolder != null) {
                                        }
                                        threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                                        if (threadedRenderer != null) {
                                        }
                                        if (this.mStopped) {
                                        }
                                        focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                                        if (!focusChangedDueToTouchMode) {
                                        }
                                        int childWidthMeasureSpec2222 = getRootMeasureSpec(this.mWidth, lp.width);
                                        int childHeightMeasureSpec2222 = getRootMeasureSpec(this.mHeight, lp.height);
                                        if (DEBUG_LAYOUT) {
                                        }
                                        performMeasure(childWidthMeasureSpec2222, childHeightMeasureSpec2222);
                                        int width2222 = host.getMeasuredWidth();
                                        int height2222 = host.getMeasuredHeight();
                                        measureAgain = false;
                                        relayoutResult4 = relayoutResult3;
                                        updatedConfiguration2 = insetsPending3;
                                        if (lp.horizontalWeight > 0.0f) {
                                        }
                                        if (lp.verticalWeight > 0.0f) {
                                        }
                                        if (measureAgain) {
                                        }
                                        layoutRequested = true;
                                        surfaceSizeChanged = surfaceSizeChanged3;
                                        relayoutResult2 = relayoutResult4;
                                        insetsPending = insetsPending2;
                                        wasReportNextDraw2 = updatedConfiguration2;
                                        if (!surfaceSizeChanged) {
                                        }
                                        prepareSurfaces();
                                        didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                        triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                        if (didLayout) {
                                        }
                                        if (surfaceCreated) {
                                        }
                                        if (triggerGlobalLayoutListener) {
                                        }
                                        if (computesInternalInsets) {
                                        }
                                        if (this.mFirst) {
                                        }
                                        if (!viewVisibilityChanged) {
                                        }
                                        if (!this.mAttachInfo.mHasWindowFocus) {
                                        }
                                        if (!hasWindowFocus) {
                                        }
                                        if (regainedFocus) {
                                        }
                                        if (!changedVisibility) {
                                        }
                                        if (this.mWindowAttributes.type == 2005) {
                                        }
                                        if (!isToast) {
                                        }
                                        this.mFirst = false;
                                        this.mWillDrawSoon = false;
                                        this.mNewSurfaceNeeded = false;
                                        this.mActivityRelaunched = false;
                                        this.mViewVisibility = viewVisibility;
                                        this.mHadWindowFocus = hasWindowFocus;
                                        this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                        if ((relayoutResult2 & 2) != 0) {
                                        }
                                        if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                        }
                                        if (!cancelDraw) {
                                        }
                                        if (this.mAttachInfo.mContentCaptureEvents != null) {
                                        }
                                        this.mIsInTraversal = false;
                                    }
                                } else {
                                    insetsPending2 = computesInternalInsets;
                                }
                                if (!this.mPendingMergedConfiguration.equals(this.mLastReportedMergedConfiguration)) {
                                    if (DEBUG_CONFIGURATION) {
                                        Log.v(this.mTag, "Visible with new config: " + this.mPendingMergedConfiguration.getMergedConfiguration());
                                    }
                                    performConfigurationChange(new MergedConfiguration(this.mPendingMergedConfiguration), !this.mFirst, -1);
                                    updatedConfiguration = true;
                                }
                                if (this.mLastSurfaceSize.equals(this.mSurfaceSize)) {
                                    try {
                                        surfaceSizeChanged2 = true;
                                        this.mLastSurfaceSize.set(this.mSurfaceSize.x, this.mSurfaceSize.y);
                                    } catch (RemoteException e11) {
                                        relayoutResult3 = relayoutResult5;
                                        hwInitialized2 = true;
                                        insetsPending3 = updatedConfiguration;
                                        dispatchApplyInsets2 = dispatchApplyInsets;
                                        if (DEBUG_ORIENTATION) {
                                        }
                                        this.mAttachInfo.mWindowLeft = frame2.left;
                                        this.mAttachInfo.mWindowTop = frame2.top;
                                        if (this.mWidth == frame2.width()) {
                                        }
                                        this.mWidth = frame2.width();
                                        this.mHeight = frame2.height();
                                        if (this.mSurfaceHolder != null) {
                                        }
                                        threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                                        if (threadedRenderer != null) {
                                        }
                                        if (this.mStopped) {
                                        }
                                        focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                                        if (!focusChangedDueToTouchMode) {
                                        }
                                        int childWidthMeasureSpec22222 = getRootMeasureSpec(this.mWidth, lp.width);
                                        int childHeightMeasureSpec22222 = getRootMeasureSpec(this.mHeight, lp.height);
                                        if (DEBUG_LAYOUT) {
                                        }
                                        performMeasure(childWidthMeasureSpec22222, childHeightMeasureSpec22222);
                                        int width22222 = host.getMeasuredWidth();
                                        int height22222 = host.getMeasuredHeight();
                                        measureAgain = false;
                                        relayoutResult4 = relayoutResult3;
                                        updatedConfiguration2 = insetsPending3;
                                        if (lp.horizontalWeight > 0.0f) {
                                        }
                                        if (lp.verticalWeight > 0.0f) {
                                        }
                                        if (measureAgain) {
                                        }
                                        layoutRequested = true;
                                        surfaceSizeChanged = surfaceSizeChanged3;
                                        relayoutResult2 = relayoutResult4;
                                        insetsPending = insetsPending2;
                                        wasReportNextDraw2 = updatedConfiguration2;
                                        if (!surfaceSizeChanged) {
                                        }
                                        prepareSurfaces();
                                        didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                        triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                        if (didLayout) {
                                        }
                                        if (surfaceCreated) {
                                        }
                                        if (triggerGlobalLayoutListener) {
                                        }
                                        if (computesInternalInsets) {
                                        }
                                        if (this.mFirst) {
                                        }
                                        if (!viewVisibilityChanged) {
                                        }
                                        if (!this.mAttachInfo.mHasWindowFocus) {
                                        }
                                        if (!hasWindowFocus) {
                                        }
                                        if (regainedFocus) {
                                        }
                                        if (!changedVisibility) {
                                        }
                                        if (this.mWindowAttributes.type == 2005) {
                                        }
                                        if (!isToast) {
                                        }
                                        this.mFirst = false;
                                        this.mWillDrawSoon = false;
                                        this.mNewSurfaceNeeded = false;
                                        this.mActivityRelaunched = false;
                                        this.mViewVisibility = viewVisibility;
                                        this.mHadWindowFocus = hasWindowFocus;
                                        this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                        if ((relayoutResult2 & 2) != 0) {
                                        }
                                        if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                        }
                                        if (!cancelDraw) {
                                        }
                                        if (this.mAttachInfo.mContentCaptureEvents != null) {
                                        }
                                        this.mIsInTraversal = false;
                                    }
                                } else {
                                    surfaceSizeChanged2 = false;
                                }
                                alwaysConsumeSystemBarsChanged = this.mPendingAlwaysConsumeSystemBars == this.mAttachInfo.mAlwaysConsumeSystemBars;
                                updateColorModeIfNeeded(lp.getColorMode());
                                surfaceCreated = hadSurface && this.mSurface.isValid();
                                surfaceDestroyed = !hadSurface && !this.mSurface.isValid();
                                surfaceReplaced = (surfaceGenerationId == this.mSurface.getGenerationId() || surfaceControlChanged) && this.mSurface.isValid();
                                if (surfaceReplaced) {
                                    this.mSurfaceSequenceId++;
                                }
                                if (alwaysConsumeSystemBarsChanged) {
                                    this.mAttachInfo.mAlwaysConsumeSystemBars = this.mPendingAlwaysConsumeSystemBars;
                                    dispatchApplyInsets = true;
                                }
                                if (updateCaptionInsets()) {
                                    dispatchApplyInsets = true;
                                }
                                if (!dispatchApplyInsets || this.mLastSystemUiVisibility != this.mAttachInfo.mSystemUiVisibility || this.mApplyInsetsRequested) {
                                    this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                                    dispatchApplyInsets(host);
                                    dispatchApplyInsets = true;
                                }
                                if (!surfaceCreated) {
                                    this.mFullRedrawNeeded = true;
                                    this.mPreviousTransparentRegion.setEmpty();
                                    try {
                                        if (this.mAttachInfo.mThreadedRenderer != null) {
                                            try {
                                                boolean hwInitialized3 = this.mAttachInfo.mThreadedRenderer.initialize(this.mSurface);
                                                if (hwInitialized3) {
                                                    try {
                                                        if ((host.mPrivateFlags & 512) == 0) {
                                                            this.mAttachInfo.mThreadedRenderer.allocateBuffers();
                                                        }
                                                    } catch (Surface.OutOfResourcesException e12) {
                                                        e = e12;
                                                        handleOutOfResourcesException(e);
                                                        return;
                                                    }
                                                }
                                                hwInitialized = hwInitialized3;
                                            } catch (Surface.OutOfResourcesException e13) {
                                                e = e13;
                                            }
                                        }
                                    } catch (RemoteException e14) {
                                        hwInitialized = true;
                                        relayoutResult3 = relayoutResult5;
                                        hwInitialized2 = surfaceSizeChanged2;
                                        insetsPending3 = updatedConfiguration;
                                        dispatchApplyInsets2 = dispatchApplyInsets;
                                        if (DEBUG_ORIENTATION) {
                                        }
                                        this.mAttachInfo.mWindowLeft = frame2.left;
                                        this.mAttachInfo.mWindowTop = frame2.top;
                                        if (this.mWidth == frame2.width()) {
                                        }
                                        this.mWidth = frame2.width();
                                        this.mHeight = frame2.height();
                                        if (this.mSurfaceHolder != null) {
                                        }
                                        threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                                        if (threadedRenderer != null) {
                                        }
                                        if (this.mStopped) {
                                        }
                                        focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                                        if (!focusChangedDueToTouchMode) {
                                        }
                                        int childWidthMeasureSpec222222 = getRootMeasureSpec(this.mWidth, lp.width);
                                        int childHeightMeasureSpec222222 = getRootMeasureSpec(this.mHeight, lp.height);
                                        if (DEBUG_LAYOUT) {
                                        }
                                        performMeasure(childWidthMeasureSpec222222, childHeightMeasureSpec222222);
                                        int width222222 = host.getMeasuredWidth();
                                        int height222222 = host.getMeasuredHeight();
                                        measureAgain = false;
                                        relayoutResult4 = relayoutResult3;
                                        updatedConfiguration2 = insetsPending3;
                                        if (lp.horizontalWeight > 0.0f) {
                                        }
                                        if (lp.verticalWeight > 0.0f) {
                                        }
                                        if (measureAgain) {
                                        }
                                        layoutRequested = true;
                                        surfaceSizeChanged = surfaceSizeChanged3;
                                        relayoutResult2 = relayoutResult4;
                                        insetsPending = insetsPending2;
                                        wasReportNextDraw2 = updatedConfiguration2;
                                        if (!surfaceSizeChanged) {
                                        }
                                        prepareSurfaces();
                                        didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                        triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                        if (didLayout) {
                                        }
                                        if (surfaceCreated) {
                                        }
                                        if (triggerGlobalLayoutListener) {
                                        }
                                        if (computesInternalInsets) {
                                        }
                                        if (this.mFirst) {
                                        }
                                        if (!viewVisibilityChanged) {
                                        }
                                        if (!this.mAttachInfo.mHasWindowFocus) {
                                        }
                                        if (!hasWindowFocus) {
                                        }
                                        if (regainedFocus) {
                                        }
                                        if (!changedVisibility) {
                                        }
                                        if (this.mWindowAttributes.type == 2005) {
                                        }
                                        if (!isToast) {
                                        }
                                        this.mFirst = false;
                                        this.mWillDrawSoon = false;
                                        this.mNewSurfaceNeeded = false;
                                        this.mActivityRelaunched = false;
                                        this.mViewVisibility = viewVisibility;
                                        this.mHadWindowFocus = hasWindowFocus;
                                        this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                        if ((relayoutResult2 & 2) != 0) {
                                        }
                                        if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                        }
                                        if (!cancelDraw) {
                                        }
                                        if (this.mAttachInfo.mContentCaptureEvents != null) {
                                        }
                                        this.mIsInTraversal = false;
                                    }
                                } else if (surfaceDestroyed) {
                                    WeakReference<View> weakReference = this.mLastScrolledFocus;
                                    if (weakReference != null) {
                                        weakReference.clear();
                                    }
                                    this.mCurScrollY = 0;
                                    this.mScrollY = 0;
                                    View view2 = this.mView;
                                    if (view2 instanceof RootViewSurfaceTaker) {
                                        ((RootViewSurfaceTaker) view2).onRootViewScrollYChanged(0);
                                    }
                                    Scroller scroller = this.mScroller;
                                    if (scroller != null) {
                                        scroller.abortAnimation();
                                    }
                                    if (isHardwareEnabled()) {
                                        this.mAttachInfo.mThreadedRenderer.destroy();
                                    }
                                } else if ((surfaceReplaced || surfaceSizeChanged2 || windowRelayoutWasForced) && this.mSurfaceHolder == null && this.mAttachInfo.mThreadedRenderer != null && this.mSurface.isValid()) {
                                    this.mFullRedrawNeeded = true;
                                    try {
                                        this.mAttachInfo.mThreadedRenderer.updateSurface(this.mSurface);
                                    } catch (Surface.OutOfResourcesException e15) {
                                        handleOutOfResourcesException(e15);
                                        return;
                                    }
                                }
                                if (this.mDragResizing != dragResizing) {
                                    dragResizing2 = dragResizing;
                                } else if (dragResizing) {
                                    this.mResizeMode = freeformResizing ? 0 : 1;
                                    boolean backdropSizeMatchesFrame = this.mWinFrame.width() == this.mPendingBackDropFrame.width() && this.mWinFrame.height() == this.mPendingBackDropFrame.height();
                                    dragResizing2 = dragResizing;
                                    startDragResizing(this.mPendingBackDropFrame, !backdropSizeMatchesFrame, this.mAttachInfo.mContentInsets, this.mAttachInfo.mStableInsets, this.mResizeMode);
                                } else {
                                    dragResizing2 = dragResizing;
                                    endDragResizing();
                                }
                                if (!this.mUseMTRenderer) {
                                    if (dragResizing2) {
                                        this.mCanvasOffsetX = this.mWinFrame.left;
                                        this.mCanvasOffsetY = this.mWinFrame.top;
                                    } else {
                                        this.mCanvasOffsetY = 0;
                                        this.mCanvasOffsetX = 0;
                                    }
                                }
                                relayoutResult3 = relayoutResult5;
                                hwInitialized2 = surfaceSizeChanged2;
                                insetsPending3 = updatedConfiguration;
                                dispatchApplyInsets2 = dispatchApplyInsets;
                                if (DEBUG_ORIENTATION) {
                                    Log.v(TAG, "Relayout returned: frame=" + frame2 + ", surface=" + this.mSurface);
                                }
                                this.mAttachInfo.mWindowLeft = frame2.left;
                                this.mAttachInfo.mWindowTop = frame2.top;
                                if (this.mWidth == frame2.width() || this.mHeight != frame2.height()) {
                                    this.mWidth = frame2.width();
                                    this.mHeight = frame2.height();
                                }
                                if (this.mSurfaceHolder != null) {
                                    if (this.mSurface.isValid()) {
                                        this.mSurfaceHolder.mSurface = this.mSurface;
                                    }
                                    this.mSurfaceHolder.setSurfaceFrameSize(this.mWidth, this.mHeight);
                                    this.mSurfaceHolder.mSurfaceLock.unlock();
                                    if (surfaceCreated) {
                                        this.mSurfaceHolder.ungetCallbacks();
                                        this.mIsCreating = true;
                                        SurfaceHolder.Callback[] callbacks = this.mSurfaceHolder.getCallbacks();
                                        if (callbacks != null) {
                                            for (SurfaceHolder.Callback c : callbacks) {
                                                c.surfaceCreated(this.mSurfaceHolder);
                                            }
                                        }
                                    }
                                    if (!surfaceCreated && !surfaceReplaced && !hwInitialized2 && !windowAttributesChanged) {
                                        surfaceSizeChanged3 = hwInitialized2;
                                    } else if (this.mSurface.isValid()) {
                                        SurfaceHolder.Callback[] callbacks2 = this.mSurfaceHolder.getCallbacks();
                                        if (callbacks2 != null) {
                                            int i7 = 0;
                                            for (int length = callbacks2.length; i7 < length; length = length) {
                                                SurfaceHolder.Callback c2 = callbacks2[i7];
                                                boolean surfaceSizeChanged4 = hwInitialized2;
                                                c2.surfaceChanged(this.mSurfaceHolder, lp.format, this.mWidth, this.mHeight);
                                                i7++;
                                                callbacks2 = callbacks2;
                                                hwInitialized2 = surfaceSizeChanged4;
                                            }
                                            surfaceSizeChanged3 = hwInitialized2;
                                        } else {
                                            surfaceSizeChanged3 = hwInitialized2;
                                        }
                                        this.mIsCreating = false;
                                    } else {
                                        surfaceSizeChanged3 = hwInitialized2;
                                    }
                                    if (surfaceDestroyed) {
                                        notifyHolderSurfaceDestroyed();
                                        this.mSurfaceHolder.mSurfaceLock.lock();
                                        try {
                                            this.mSurfaceHolder.mSurface = new Surface();
                                        } finally {
                                            this.mSurfaceHolder.mSurfaceLock.unlock();
                                        }
                                    }
                                } else {
                                    surfaceSizeChanged3 = hwInitialized2;
                                }
                                threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                                if (threadedRenderer != null && threadedRenderer.isEnabled() && (hwInitialized || this.mWidth != threadedRenderer.getWidth() || this.mHeight != threadedRenderer.getHeight() || this.mNeedsRendererSetup)) {
                                    threadedRenderer.setup(this.mWidth, this.mHeight, this.mAttachInfo, this.mWindowAttributes.surfaceInsets);
                                    this.mNeedsRendererSetup = false;
                                }
                                if (this.mStopped || wasReportNextDraw) {
                                    focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                                    if (!focusChangedDueToTouchMode || this.mWidth != host.getMeasuredWidth() || this.mHeight != host.getMeasuredHeight() || dispatchApplyInsets2 || insetsPending3) {
                                        int childWidthMeasureSpec2222222 = getRootMeasureSpec(this.mWidth, lp.width);
                                        int childHeightMeasureSpec2222222 = getRootMeasureSpec(this.mHeight, lp.height);
                                        if (DEBUG_LAYOUT) {
                                            Log.v(this.mTag, "Ooops, something changed!  mWidth=" + this.mWidth + " measuredWidth=" + host.getMeasuredWidth() + " mHeight=" + this.mHeight + " measuredHeight=" + host.getMeasuredHeight() + " dispatchApplyInsets=" + dispatchApplyInsets2);
                                        }
                                        performMeasure(childWidthMeasureSpec2222222, childHeightMeasureSpec2222222);
                                        int width2222222 = host.getMeasuredWidth();
                                        int height2222222 = host.getMeasuredHeight();
                                        measureAgain = false;
                                        relayoutResult4 = relayoutResult3;
                                        updatedConfiguration2 = insetsPending3;
                                        if (lp.horizontalWeight > 0.0f) {
                                            width2222222 += (int) ((this.mWidth - width2222222) * lp.horizontalWeight);
                                            childWidthMeasureSpec2222222 = View.MeasureSpec.makeMeasureSpec(width2222222, 1073741824);
                                            measureAgain = true;
                                        }
                                        if (lp.verticalWeight > 0.0f) {
                                            height2222222 += (int) ((this.mHeight - height2222222) * lp.verticalWeight);
                                            childHeightMeasureSpec2222222 = View.MeasureSpec.makeMeasureSpec(height2222222, 1073741824);
                                            measureAgain = true;
                                        }
                                        if (measureAgain) {
                                            if (DEBUG_LAYOUT) {
                                                Log.v(this.mTag, "And hey let's measure once more: width=" + width2222222 + " height=" + height2222222);
                                            }
                                            performMeasure(childWidthMeasureSpec2222222, childHeightMeasureSpec2222222);
                                        }
                                        layoutRequested = true;
                                        surfaceSizeChanged = surfaceSizeChanged3;
                                        relayoutResult2 = relayoutResult4;
                                        insetsPending = insetsPending2;
                                        wasReportNextDraw2 = updatedConfiguration2;
                                        if (!surfaceSizeChanged) {
                                        }
                                        prepareSurfaces();
                                        didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                        triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                        if (didLayout) {
                                        }
                                        if (surfaceCreated) {
                                        }
                                        if (triggerGlobalLayoutListener) {
                                        }
                                        if (computesInternalInsets) {
                                        }
                                        if (this.mFirst) {
                                        }
                                        if (!viewVisibilityChanged) {
                                        }
                                        if (!this.mAttachInfo.mHasWindowFocus) {
                                        }
                                        if (!hasWindowFocus) {
                                        }
                                        if (regainedFocus) {
                                        }
                                        if (!changedVisibility) {
                                        }
                                        if (this.mWindowAttributes.type == 2005) {
                                        }
                                        if (!isToast) {
                                        }
                                        this.mFirst = false;
                                        this.mWillDrawSoon = false;
                                        this.mNewSurfaceNeeded = false;
                                        this.mActivityRelaunched = false;
                                        this.mViewVisibility = viewVisibility;
                                        this.mHadWindowFocus = hasWindowFocus;
                                        this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                        if ((relayoutResult2 & 2) != 0) {
                                        }
                                        if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                            cancelDraw = false;
                                        }
                                        if (!cancelDraw) {
                                        }
                                        if (this.mAttachInfo.mContentCaptureEvents != null) {
                                        }
                                        this.mIsInTraversal = false;
                                    }
                                }
                                relayoutResult4 = relayoutResult3;
                                updatedConfiguration2 = insetsPending3;
                                surfaceSizeChanged = surfaceSizeChanged3;
                                relayoutResult2 = relayoutResult4;
                                insetsPending = insetsPending2;
                                wasReportNextDraw2 = updatedConfiguration2;
                                if (!surfaceSizeChanged) {
                                }
                                prepareSurfaces();
                                didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                                triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                                if (didLayout) {
                                }
                                if (surfaceCreated) {
                                }
                                if (triggerGlobalLayoutListener) {
                                }
                                if (computesInternalInsets) {
                                }
                                if (this.mFirst) {
                                }
                                if (!viewVisibilityChanged) {
                                }
                                if (!this.mAttachInfo.mHasWindowFocus) {
                                }
                                if (!hasWindowFocus) {
                                }
                                if (regainedFocus) {
                                }
                                if (!changedVisibility) {
                                }
                                if (this.mWindowAttributes.type == 2005) {
                                }
                                if (!isToast) {
                                }
                                this.mFirst = false;
                                this.mWillDrawSoon = false;
                                this.mNewSurfaceNeeded = false;
                                this.mActivityRelaunched = false;
                                this.mViewVisibility = viewVisibility;
                                this.mHadWindowFocus = hasWindowFocus;
                                this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                                if ((relayoutResult2 & 2) != 0) {
                                }
                                if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                                }
                                if (!cancelDraw) {
                                }
                                if (this.mAttachInfo.mContentCaptureEvents != null) {
                                }
                                this.mIsInTraversal = false;
                            }
                            viewVisibilityChanged = viewVisibilityChanged2;
                            this.mViewFrameInfo.flags |= 1;
                            relayoutResult5 = relayoutWindow(params2, viewVisibility, computesInternalInsets);
                            boolean freeformResizing2 = (relayoutResult5 & 16) == 0;
                            boolean dockedResizing2 = (relayoutResult5 & 8) == 0;
                            dragResizing = !freeformResizing2 || dockedResizing2;
                            if ((relayoutResult5 & 128) != 0) {
                            }
                            boolean surfaceControlChanged2 = (relayoutResult5 & 4) != 4;
                            if (this.mSurfaceControl.isValid()) {
                            }
                            if (!DEBUG_LAYOUT) {
                            }
                            if (!this.mPendingMergedConfiguration.equals(this.mLastReportedMergedConfiguration)) {
                            }
                            if (this.mLastSurfaceSize.equals(this.mSurfaceSize)) {
                            }
                            alwaysConsumeSystemBarsChanged = this.mPendingAlwaysConsumeSystemBars == this.mAttachInfo.mAlwaysConsumeSystemBars;
                            updateColorModeIfNeeded(lp.getColorMode());
                            surfaceCreated = hadSurface && this.mSurface.isValid();
                            surfaceDestroyed = !hadSurface && !this.mSurface.isValid();
                            surfaceReplaced = (surfaceGenerationId == this.mSurface.getGenerationId() || surfaceControlChanged2) && this.mSurface.isValid();
                            if (surfaceReplaced) {
                            }
                            if (alwaysConsumeSystemBarsChanged) {
                            }
                            if (updateCaptionInsets()) {
                            }
                            if (!dispatchApplyInsets) {
                            }
                            this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                            dispatchApplyInsets(host);
                            dispatchApplyInsets = true;
                            if (!surfaceCreated) {
                            }
                            if (this.mDragResizing != dragResizing) {
                            }
                            if (!this.mUseMTRenderer) {
                            }
                            relayoutResult3 = relayoutResult5;
                            hwInitialized2 = surfaceSizeChanged2;
                            insetsPending3 = updatedConfiguration;
                            dispatchApplyInsets2 = dispatchApplyInsets;
                            if (DEBUG_ORIENTATION) {
                            }
                            this.mAttachInfo.mWindowLeft = frame2.left;
                            this.mAttachInfo.mWindowTop = frame2.top;
                            if (this.mWidth == frame2.width()) {
                            }
                            this.mWidth = frame2.width();
                            this.mHeight = frame2.height();
                            if (this.mSurfaceHolder != null) {
                            }
                            threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                            if (threadedRenderer != null) {
                                threadedRenderer.setup(this.mWidth, this.mHeight, this.mAttachInfo, this.mWindowAttributes.surfaceInsets);
                                this.mNeedsRendererSetup = false;
                            }
                            if (this.mStopped) {
                            }
                            focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                            if (!focusChangedDueToTouchMode) {
                            }
                            int childWidthMeasureSpec22222222 = getRootMeasureSpec(this.mWidth, lp.width);
                            int childHeightMeasureSpec22222222 = getRootMeasureSpec(this.mHeight, lp.height);
                            if (DEBUG_LAYOUT) {
                            }
                            performMeasure(childWidthMeasureSpec22222222, childHeightMeasureSpec22222222);
                            int width22222222 = host.getMeasuredWidth();
                            int height22222222 = host.getMeasuredHeight();
                            measureAgain = false;
                            relayoutResult4 = relayoutResult3;
                            updatedConfiguration2 = insetsPending3;
                            if (lp.horizontalWeight > 0.0f) {
                            }
                            if (lp.verticalWeight > 0.0f) {
                            }
                            if (measureAgain) {
                            }
                            layoutRequested = true;
                            surfaceSizeChanged = surfaceSizeChanged3;
                            relayoutResult2 = relayoutResult4;
                            insetsPending = insetsPending2;
                            wasReportNextDraw2 = updatedConfiguration2;
                            if (!surfaceSizeChanged) {
                            }
                            prepareSurfaces();
                            didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                            triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                            if (didLayout) {
                            }
                            if (surfaceCreated) {
                            }
                            if (triggerGlobalLayoutListener) {
                            }
                            if (computesInternalInsets) {
                            }
                            if (this.mFirst) {
                            }
                            if (!viewVisibilityChanged) {
                            }
                            if (!this.mAttachInfo.mHasWindowFocus) {
                            }
                            if (!hasWindowFocus) {
                            }
                            if (regainedFocus) {
                            }
                            if (!changedVisibility) {
                            }
                            if (this.mWindowAttributes.type == 2005) {
                            }
                            if (!isToast) {
                            }
                            this.mFirst = false;
                            this.mWillDrawSoon = false;
                            this.mNewSurfaceNeeded = false;
                            this.mActivityRelaunched = false;
                            this.mViewVisibility = viewVisibility;
                            this.mHadWindowFocus = hasWindowFocus;
                            this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                            if ((relayoutResult2 & 2) != 0) {
                            }
                            if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                            }
                            if (!cancelDraw) {
                            }
                            if (this.mAttachInfo.mContentCaptureEvents != null) {
                            }
                            this.mIsInTraversal = false;
                        }
                        desiredWindowWidth4 = desiredWindowWidth3;
                        desiredWindowHeight4 = desiredWindowHeight3;
                        windowShouldResize = false;
                        boolean windowShouldResize22 = windowShouldResize | (!this.mDragResizing && this.mResizeMode == 0) | this.mActivityRelaunched;
                        computesInternalInsets = !this.mAttachInfo.mTreeObserver.hasComputeInternalInsetsListeners() || this.mAttachInfo.mHasNonEmptyGivenInternalInsets;
                        int surfaceGenerationId2 = this.mSurface.getGenerationId();
                        isViewVisible = viewVisibility != 0;
                        boolean windowRelayoutWasForced2 = this.mForceNextWindowRelayout;
                        surfaceSizeChanged = false;
                        surfaceCreated = false;
                        boolean surfaceDestroyed2 = false;
                        surfaceReplaced = false;
                        windowAttributesChanged = this.mWindowAttributesChanged;
                        if (!windowAttributesChanged) {
                        }
                        if (params2 == null) {
                        }
                        boolean updatedConfiguration32 = this.mReportNextDraw;
                        if (!this.mFirst) {
                        }
                        frame2 = frame;
                        wasReportNextDraw = updatedConfiguration32;
                        this.mForceNextWindowRelayout = false;
                        baseSurfaceHolder = this.mSurfaceHolder;
                        if (baseSurfaceHolder == null) {
                        }
                        dispatchApplyInsets = false;
                        boolean hadSurface2 = this.mSurface.isValid();
                        if (!DEBUG_LAYOUT) {
                        }
                        if (this.mAttachInfo.mThreadedRenderer == null) {
                        }
                        if (!this.mFirst) {
                            viewVisibilityChanged = viewVisibilityChanged2;
                            relayoutResult5 = relayoutWindow(params2, viewVisibility, computesInternalInsets);
                            boolean freeformResizing22 = (relayoutResult5 & 16) == 0;
                            boolean dockedResizing22 = (relayoutResult5 & 8) == 0;
                            dragResizing = !freeformResizing22 || dockedResizing22;
                            if ((relayoutResult5 & 128) != 0) {
                            }
                            boolean surfaceControlChanged22 = (relayoutResult5 & 4) != 4;
                            if (this.mSurfaceControl.isValid()) {
                            }
                            if (!DEBUG_LAYOUT) {
                            }
                            if (!this.mPendingMergedConfiguration.equals(this.mLastReportedMergedConfiguration)) {
                            }
                            if (this.mLastSurfaceSize.equals(this.mSurfaceSize)) {
                            }
                            alwaysConsumeSystemBarsChanged = this.mPendingAlwaysConsumeSystemBars == this.mAttachInfo.mAlwaysConsumeSystemBars;
                            updateColorModeIfNeeded(lp.getColorMode());
                            surfaceCreated = hadSurface2 && this.mSurface.isValid();
                            surfaceDestroyed2 = !hadSurface2 && !this.mSurface.isValid();
                            surfaceReplaced = (surfaceGenerationId2 == this.mSurface.getGenerationId() || surfaceControlChanged22) && this.mSurface.isValid();
                            if (surfaceReplaced) {
                            }
                            if (alwaysConsumeSystemBarsChanged) {
                            }
                            if (updateCaptionInsets()) {
                            }
                            if (!dispatchApplyInsets) {
                            }
                            this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                            dispatchApplyInsets(host);
                            dispatchApplyInsets = true;
                            if (!surfaceCreated) {
                            }
                            if (this.mDragResizing != dragResizing) {
                            }
                            if (!this.mUseMTRenderer) {
                            }
                            relayoutResult3 = relayoutResult5;
                            hwInitialized2 = surfaceSizeChanged2;
                            insetsPending3 = updatedConfiguration;
                            dispatchApplyInsets2 = dispatchApplyInsets;
                            if (DEBUG_ORIENTATION) {
                            }
                            this.mAttachInfo.mWindowLeft = frame2.left;
                            this.mAttachInfo.mWindowTop = frame2.top;
                            if (this.mWidth == frame2.width()) {
                            }
                            this.mWidth = frame2.width();
                            this.mHeight = frame2.height();
                            if (this.mSurfaceHolder != null) {
                            }
                            threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                            if (threadedRenderer != null) {
                            }
                            if (this.mStopped) {
                            }
                            focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                            if (!focusChangedDueToTouchMode) {
                            }
                            int childWidthMeasureSpec222222222 = getRootMeasureSpec(this.mWidth, lp.width);
                            int childHeightMeasureSpec222222222 = getRootMeasureSpec(this.mHeight, lp.height);
                            if (DEBUG_LAYOUT) {
                            }
                            performMeasure(childWidthMeasureSpec222222222, childHeightMeasureSpec222222222);
                            int width222222222 = host.getMeasuredWidth();
                            int height222222222 = host.getMeasuredHeight();
                            measureAgain = false;
                            relayoutResult4 = relayoutResult3;
                            updatedConfiguration2 = insetsPending3;
                            if (lp.horizontalWeight > 0.0f) {
                            }
                            if (lp.verticalWeight > 0.0f) {
                            }
                            if (measureAgain) {
                            }
                            layoutRequested = true;
                            surfaceSizeChanged = surfaceSizeChanged3;
                            relayoutResult2 = relayoutResult4;
                            insetsPending = insetsPending2;
                            wasReportNextDraw2 = updatedConfiguration2;
                            if (!surfaceSizeChanged) {
                            }
                            prepareSurfaces();
                            didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                            triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                            if (didLayout) {
                            }
                            if (surfaceCreated) {
                            }
                            if (triggerGlobalLayoutListener) {
                            }
                            if (computesInternalInsets) {
                            }
                            if (this.mFirst) {
                            }
                            if (!viewVisibilityChanged) {
                            }
                            if (!this.mAttachInfo.mHasWindowFocus) {
                            }
                            if (!hasWindowFocus) {
                            }
                            if (regainedFocus) {
                            }
                            if (!changedVisibility) {
                            }
                            if (this.mWindowAttributes.type == 2005) {
                            }
                            if (!isToast) {
                            }
                            this.mFirst = false;
                            this.mWillDrawSoon = false;
                            this.mNewSurfaceNeeded = false;
                            this.mActivityRelaunched = false;
                            this.mViewVisibility = viewVisibility;
                            this.mHadWindowFocus = hasWindowFocus;
                            this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                            if ((relayoutResult2 & 2) != 0) {
                            }
                            if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                            }
                            if (!cancelDraw) {
                            }
                            if (this.mAttachInfo.mContentCaptureEvents != null) {
                            }
                            this.mIsInTraversal = false;
                        }
                        viewVisibilityChanged = viewVisibilityChanged2;
                        this.mViewFrameInfo.flags |= 1;
                        relayoutResult5 = relayoutWindow(params2, viewVisibility, computesInternalInsets);
                        boolean freeformResizing222 = (relayoutResult5 & 16) == 0;
                        boolean dockedResizing222 = (relayoutResult5 & 8) == 0;
                        dragResizing = !freeformResizing222 || dockedResizing222;
                        if ((relayoutResult5 & 128) != 0) {
                        }
                        boolean surfaceControlChanged222 = (relayoutResult5 & 4) != 4;
                        if (this.mSurfaceControl.isValid()) {
                        }
                        if (!DEBUG_LAYOUT) {
                        }
                        if (!this.mPendingMergedConfiguration.equals(this.mLastReportedMergedConfiguration)) {
                        }
                        if (this.mLastSurfaceSize.equals(this.mSurfaceSize)) {
                        }
                        alwaysConsumeSystemBarsChanged = this.mPendingAlwaysConsumeSystemBars == this.mAttachInfo.mAlwaysConsumeSystemBars;
                        updateColorModeIfNeeded(lp.getColorMode());
                        surfaceCreated = hadSurface2 && this.mSurface.isValid();
                        surfaceDestroyed2 = !hadSurface2 && !this.mSurface.isValid();
                        surfaceReplaced = (surfaceGenerationId2 == this.mSurface.getGenerationId() || surfaceControlChanged222) && this.mSurface.isValid();
                        if (surfaceReplaced) {
                        }
                        if (alwaysConsumeSystemBarsChanged) {
                        }
                        if (updateCaptionInsets()) {
                        }
                        if (!dispatchApplyInsets) {
                        }
                        this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                        dispatchApplyInsets(host);
                        dispatchApplyInsets = true;
                        if (!surfaceCreated) {
                        }
                        if (this.mDragResizing != dragResizing) {
                        }
                        if (!this.mUseMTRenderer) {
                        }
                        relayoutResult3 = relayoutResult5;
                        hwInitialized2 = surfaceSizeChanged2;
                        insetsPending3 = updatedConfiguration;
                        dispatchApplyInsets2 = dispatchApplyInsets;
                        if (DEBUG_ORIENTATION) {
                        }
                        this.mAttachInfo.mWindowLeft = frame2.left;
                        this.mAttachInfo.mWindowTop = frame2.top;
                        if (this.mWidth == frame2.width()) {
                        }
                        this.mWidth = frame2.width();
                        this.mHeight = frame2.height();
                        if (this.mSurfaceHolder != null) {
                        }
                        threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                        if (threadedRenderer != null) {
                        }
                        if (this.mStopped) {
                        }
                        focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                        if (!focusChangedDueToTouchMode) {
                        }
                        int childWidthMeasureSpec2222222222 = getRootMeasureSpec(this.mWidth, lp.width);
                        int childHeightMeasureSpec2222222222 = getRootMeasureSpec(this.mHeight, lp.height);
                        if (DEBUG_LAYOUT) {
                        }
                        performMeasure(childWidthMeasureSpec2222222222, childHeightMeasureSpec2222222222);
                        int width2222222222 = host.getMeasuredWidth();
                        int height2222222222 = host.getMeasuredHeight();
                        measureAgain = false;
                        relayoutResult4 = relayoutResult3;
                        updatedConfiguration2 = insetsPending3;
                        if (lp.horizontalWeight > 0.0f) {
                        }
                        if (lp.verticalWeight > 0.0f) {
                        }
                        if (measureAgain) {
                        }
                        layoutRequested = true;
                        surfaceSizeChanged = surfaceSizeChanged3;
                        relayoutResult2 = relayoutResult4;
                        insetsPending = insetsPending2;
                        wasReportNextDraw2 = updatedConfiguration2;
                        if (!surfaceSizeChanged) {
                        }
                        prepareSurfaces();
                        didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                        triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                        if (didLayout) {
                        }
                        if (surfaceCreated) {
                        }
                        if (triggerGlobalLayoutListener) {
                        }
                        if (computesInternalInsets) {
                        }
                        if (this.mFirst) {
                        }
                        if (!viewVisibilityChanged) {
                        }
                        if (!this.mAttachInfo.mHasWindowFocus) {
                        }
                        if (!hasWindowFocus) {
                        }
                        if (regainedFocus) {
                        }
                        if (!changedVisibility) {
                        }
                        if (this.mWindowAttributes.type == 2005) {
                        }
                        if (!isToast) {
                        }
                        this.mFirst = false;
                        this.mWillDrawSoon = false;
                        this.mNewSurfaceNeeded = false;
                        this.mActivityRelaunched = false;
                        this.mViewVisibility = viewVisibility;
                        this.mHadWindowFocus = hasWindowFocus;
                        this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                        if ((relayoutResult2 & 2) != 0) {
                        }
                        if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                        }
                        if (!cancelDraw) {
                        }
                        if (this.mAttachInfo.mContentCaptureEvents != null) {
                        }
                        this.mIsInTraversal = false;
                    }
                    desiredWindowWidth3 = desiredWindowWidth2;
                    desiredWindowHeight3 = desiredWindowHeight2;
                }
                windowSizeMayChange = windowSizeMayChange2;
                if (layoutRequested) {
                }
                if (!layoutRequested) {
                }
                desiredWindowWidth4 = desiredWindowWidth3;
                desiredWindowHeight4 = desiredWindowHeight3;
                windowShouldResize = false;
                boolean windowShouldResize222 = windowShouldResize | (!this.mDragResizing && this.mResizeMode == 0) | this.mActivityRelaunched;
                computesInternalInsets = !this.mAttachInfo.mTreeObserver.hasComputeInternalInsetsListeners() || this.mAttachInfo.mHasNonEmptyGivenInternalInsets;
                int surfaceGenerationId22 = this.mSurface.getGenerationId();
                isViewVisible = viewVisibility != 0;
                boolean windowRelayoutWasForced22 = this.mForceNextWindowRelayout;
                surfaceSizeChanged = false;
                surfaceCreated = false;
                boolean surfaceDestroyed22 = false;
                surfaceReplaced = false;
                windowAttributesChanged = this.mWindowAttributesChanged;
                if (!windowAttributesChanged) {
                }
                if (params2 == null) {
                }
                boolean updatedConfiguration322 = this.mReportNextDraw;
                if (!this.mFirst) {
                }
                frame2 = frame;
                wasReportNextDraw = updatedConfiguration322;
                this.mForceNextWindowRelayout = false;
                baseSurfaceHolder = this.mSurfaceHolder;
                if (baseSurfaceHolder == null) {
                }
                dispatchApplyInsets = false;
                boolean hadSurface22 = this.mSurface.isValid();
                if (!DEBUG_LAYOUT) {
                }
                if (this.mAttachInfo.mThreadedRenderer == null) {
                }
                if (!this.mFirst) {
                }
                viewVisibilityChanged = viewVisibilityChanged2;
                this.mViewFrameInfo.flags |= 1;
                relayoutResult5 = relayoutWindow(params2, viewVisibility, computesInternalInsets);
                boolean freeformResizing2222 = (relayoutResult5 & 16) == 0;
                boolean dockedResizing2222 = (relayoutResult5 & 8) == 0;
                dragResizing = !freeformResizing2222 || dockedResizing2222;
                if ((relayoutResult5 & 128) != 0) {
                }
                boolean surfaceControlChanged2222 = (relayoutResult5 & 4) != 4;
                if (this.mSurfaceControl.isValid()) {
                }
                if (!DEBUG_LAYOUT) {
                }
                if (!this.mPendingMergedConfiguration.equals(this.mLastReportedMergedConfiguration)) {
                }
                if (this.mLastSurfaceSize.equals(this.mSurfaceSize)) {
                }
                alwaysConsumeSystemBarsChanged = this.mPendingAlwaysConsumeSystemBars == this.mAttachInfo.mAlwaysConsumeSystemBars;
                updateColorModeIfNeeded(lp.getColorMode());
                surfaceCreated = hadSurface22 && this.mSurface.isValid();
                surfaceDestroyed22 = !hadSurface22 && !this.mSurface.isValid();
                surfaceReplaced = (surfaceGenerationId22 == this.mSurface.getGenerationId() || surfaceControlChanged2222) && this.mSurface.isValid();
                if (surfaceReplaced) {
                }
                if (alwaysConsumeSystemBarsChanged) {
                }
                if (updateCaptionInsets()) {
                }
                if (!dispatchApplyInsets) {
                }
                this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                dispatchApplyInsets(host);
                dispatchApplyInsets = true;
                if (!surfaceCreated) {
                }
                if (this.mDragResizing != dragResizing) {
                }
                if (!this.mUseMTRenderer) {
                }
                relayoutResult3 = relayoutResult5;
                hwInitialized2 = surfaceSizeChanged2;
                insetsPending3 = updatedConfiguration;
                dispatchApplyInsets2 = dispatchApplyInsets;
                if (DEBUG_ORIENTATION) {
                }
                this.mAttachInfo.mWindowLeft = frame2.left;
                this.mAttachInfo.mWindowTop = frame2.top;
                if (this.mWidth == frame2.width()) {
                }
                this.mWidth = frame2.width();
                this.mHeight = frame2.height();
                if (this.mSurfaceHolder != null) {
                }
                threadedRenderer = this.mAttachInfo.mThreadedRenderer;
                if (threadedRenderer != null) {
                }
                if (this.mStopped) {
                }
                focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
                if (!focusChangedDueToTouchMode) {
                }
                int childWidthMeasureSpec22222222222 = getRootMeasureSpec(this.mWidth, lp.width);
                int childHeightMeasureSpec22222222222 = getRootMeasureSpec(this.mHeight, lp.height);
                if (DEBUG_LAYOUT) {
                }
                performMeasure(childWidthMeasureSpec22222222222, childHeightMeasureSpec22222222222);
                int width22222222222 = host.getMeasuredWidth();
                int height22222222222 = host.getMeasuredHeight();
                measureAgain = false;
                relayoutResult4 = relayoutResult3;
                updatedConfiguration2 = insetsPending3;
                if (lp.horizontalWeight > 0.0f) {
                }
                if (lp.verticalWeight > 0.0f) {
                }
                if (measureAgain) {
                }
                layoutRequested = true;
                surfaceSizeChanged = surfaceSizeChanged3;
                relayoutResult2 = relayoutResult4;
                insetsPending = insetsPending2;
                wasReportNextDraw2 = updatedConfiguration2;
                if (!surfaceSizeChanged) {
                }
                prepareSurfaces();
                didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
                triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
                if (didLayout) {
                }
                if (surfaceCreated) {
                }
                if (triggerGlobalLayoutListener) {
                }
                if (computesInternalInsets) {
                }
                if (this.mFirst) {
                }
                if (!viewVisibilityChanged) {
                }
                if (!this.mAttachInfo.mHasWindowFocus) {
                }
                if (!hasWindowFocus) {
                }
                if (regainedFocus) {
                }
                if (!changedVisibility) {
                }
                if (this.mWindowAttributes.type == 2005) {
                }
                if (!isToast) {
                }
                this.mFirst = false;
                this.mWillDrawSoon = false;
                this.mNewSurfaceNeeded = false;
                this.mActivityRelaunched = false;
                this.mViewVisibility = viewVisibility;
                this.mHadWindowFocus = hasWindowFocus;
                this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
                if ((relayoutResult2 & 2) != 0) {
                }
                if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
                }
                if (!cancelDraw) {
                }
                if (this.mAttachInfo.mContentCaptureEvents != null) {
                }
                this.mIsInTraversal = false;
            }
        }
        z = false;
        boolean viewUserVisibilityChanged2 = z;
        CompatibilityInfo compatibilityInfo2 = this.mDisplay.getDisplayAdjustments().getCompatibilityInfo();
        supportsScreen = compatibilityInfo2.supportsScreen();
        z2 = this.mLastInCompatMode;
        if (supportsScreen != z2) {
        }
        Rect frame32 = this.mWinFrame;
        if (!this.mFirst) {
        }
        if (viewVisibilityChanged2) {
        }
        if (this.mAttachInfo.mWindowVisibility != 0) {
        }
        getRunQueue().executeActions(this.mAttachInfo.mHandler);
        layoutRequested = !this.mLayoutRequested && (!this.mStopped || this.mReportNextDraw);
        if (!layoutRequested) {
        }
        if (collectViewAttributes()) {
        }
        if (this.mAttachInfo.mForceReportNewAttributes) {
        }
        if (!this.mFirst) {
        }
        this.mAttachInfo.mViewVisibilityChanged = false;
        resizeMode = this.mSoftInputMode & 240;
        if (resizeMode == 0) {
        }
        if (this.mApplyInsetsRequested) {
        }
        desiredWindowWidth3 = desiredWindowWidth2;
        desiredWindowHeight3 = desiredWindowHeight2;
        windowSizeMayChange = windowSizeMayChange2;
        if (layoutRequested) {
        }
        if (!layoutRequested) {
        }
        desiredWindowWidth4 = desiredWindowWidth3;
        desiredWindowHeight4 = desiredWindowHeight3;
        windowShouldResize = false;
        boolean windowShouldResize2222 = windowShouldResize | (!this.mDragResizing && this.mResizeMode == 0) | this.mActivityRelaunched;
        computesInternalInsets = !this.mAttachInfo.mTreeObserver.hasComputeInternalInsetsListeners() || this.mAttachInfo.mHasNonEmptyGivenInternalInsets;
        int surfaceGenerationId222 = this.mSurface.getGenerationId();
        isViewVisible = viewVisibility != 0;
        boolean windowRelayoutWasForced222 = this.mForceNextWindowRelayout;
        surfaceSizeChanged = false;
        surfaceCreated = false;
        boolean surfaceDestroyed222 = false;
        surfaceReplaced = false;
        windowAttributesChanged = this.mWindowAttributesChanged;
        if (!windowAttributesChanged) {
        }
        if (params2 == null) {
        }
        boolean updatedConfiguration3222 = this.mReportNextDraw;
        if (!this.mFirst) {
        }
        frame2 = frame;
        wasReportNextDraw = updatedConfiguration3222;
        this.mForceNextWindowRelayout = false;
        baseSurfaceHolder = this.mSurfaceHolder;
        if (baseSurfaceHolder == null) {
        }
        dispatchApplyInsets = false;
        boolean hadSurface222 = this.mSurface.isValid();
        if (!DEBUG_LAYOUT) {
        }
        if (this.mAttachInfo.mThreadedRenderer == null) {
        }
        if (!this.mFirst) {
        }
        viewVisibilityChanged = viewVisibilityChanged2;
        this.mViewFrameInfo.flags |= 1;
        relayoutResult5 = relayoutWindow(params2, viewVisibility, computesInternalInsets);
        boolean freeformResizing22222 = (relayoutResult5 & 16) == 0;
        boolean dockedResizing22222 = (relayoutResult5 & 8) == 0;
        dragResizing = !freeformResizing22222 || dockedResizing22222;
        if ((relayoutResult5 & 128) != 0) {
        }
        boolean surfaceControlChanged22222 = (relayoutResult5 & 4) != 4;
        if (this.mSurfaceControl.isValid()) {
        }
        if (!DEBUG_LAYOUT) {
        }
        if (!this.mPendingMergedConfiguration.equals(this.mLastReportedMergedConfiguration)) {
        }
        if (this.mLastSurfaceSize.equals(this.mSurfaceSize)) {
        }
        alwaysConsumeSystemBarsChanged = this.mPendingAlwaysConsumeSystemBars == this.mAttachInfo.mAlwaysConsumeSystemBars;
        updateColorModeIfNeeded(lp.getColorMode());
        surfaceCreated = hadSurface222 && this.mSurface.isValid();
        surfaceDestroyed222 = !hadSurface222 && !this.mSurface.isValid();
        surfaceReplaced = (surfaceGenerationId222 == this.mSurface.getGenerationId() || surfaceControlChanged22222) && this.mSurface.isValid();
        if (surfaceReplaced) {
        }
        if (alwaysConsumeSystemBarsChanged) {
        }
        if (updateCaptionInsets()) {
        }
        if (!dispatchApplyInsets) {
        }
        this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
        dispatchApplyInsets(host);
        dispatchApplyInsets = true;
        if (!surfaceCreated) {
        }
        if (this.mDragResizing != dragResizing) {
        }
        if (!this.mUseMTRenderer) {
        }
        relayoutResult3 = relayoutResult5;
        hwInitialized2 = surfaceSizeChanged2;
        insetsPending3 = updatedConfiguration;
        dispatchApplyInsets2 = dispatchApplyInsets;
        if (DEBUG_ORIENTATION) {
        }
        this.mAttachInfo.mWindowLeft = frame2.left;
        this.mAttachInfo.mWindowTop = frame2.top;
        if (this.mWidth == frame2.width()) {
        }
        this.mWidth = frame2.width();
        this.mHeight = frame2.height();
        if (this.mSurfaceHolder != null) {
        }
        threadedRenderer = this.mAttachInfo.mThreadedRenderer;
        if (threadedRenderer != null) {
        }
        if (this.mStopped) {
        }
        focusChangedDueToTouchMode = ensureTouchModeLocally((relayoutResult3 & 1) != 0);
        if (!focusChangedDueToTouchMode) {
        }
        int childWidthMeasureSpec222222222222 = getRootMeasureSpec(this.mWidth, lp.width);
        int childHeightMeasureSpec222222222222 = getRootMeasureSpec(this.mHeight, lp.height);
        if (DEBUG_LAYOUT) {
        }
        performMeasure(childWidthMeasureSpec222222222222, childHeightMeasureSpec222222222222);
        int width222222222222 = host.getMeasuredWidth();
        int height222222222222 = host.getMeasuredHeight();
        measureAgain = false;
        relayoutResult4 = relayoutResult3;
        updatedConfiguration2 = insetsPending3;
        if (lp.horizontalWeight > 0.0f) {
        }
        if (lp.verticalWeight > 0.0f) {
        }
        if (measureAgain) {
        }
        layoutRequested = true;
        surfaceSizeChanged = surfaceSizeChanged3;
        relayoutResult2 = relayoutResult4;
        insetsPending = insetsPending2;
        wasReportNextDraw2 = updatedConfiguration2;
        if (!surfaceSizeChanged) {
        }
        prepareSurfaces();
        didLayout = !layoutRequested && (!this.mStopped || wasReportNextDraw);
        triggerGlobalLayoutListener = !didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
        if (didLayout) {
        }
        if (surfaceCreated) {
        }
        if (triggerGlobalLayoutListener) {
        }
        if (computesInternalInsets) {
        }
        if (this.mFirst) {
        }
        if (!viewVisibilityChanged) {
        }
        if (!this.mAttachInfo.mHasWindowFocus) {
        }
        if (!hasWindowFocus) {
        }
        if (regainedFocus) {
        }
        if (!changedVisibility) {
        }
        if (this.mWindowAttributes.type == 2005) {
        }
        if (!isToast) {
        }
        this.mFirst = false;
        this.mWillDrawSoon = false;
        this.mNewSurfaceNeeded = false;
        this.mActivityRelaunched = false;
        this.mViewVisibility = viewVisibility;
        this.mHadWindowFocus = hasWindowFocus;
        this.mImeFocusController.onTraversal(hasWindowFocus, this.mWindowAttributes);
        if ((relayoutResult2 & 2) != 0) {
        }
        if (!this.mAttachInfo.mTreeObserver.dispatchOnPreDraw()) {
        }
        if (!cancelDraw) {
        }
        if (this.mAttachInfo.mContentCaptureEvents != null) {
        }
        this.mIsInTraversal = false;
    }

    private void notifyContentCatpureEvents() {
        Trace.traceBegin(8L, "notifyContentCaptureEvents");
        try {
            MainContentCaptureSession mainSession = this.mAttachInfo.mContentCaptureManager.getMainContentCaptureSession();
            for (int i = 0; i < this.mAttachInfo.mContentCaptureEvents.size(); i++) {
                int sessionId = this.mAttachInfo.mContentCaptureEvents.keyAt(i);
                mainSession.notifyViewTreeEvent(sessionId, true);
                ArrayList<Object> events = this.mAttachInfo.mContentCaptureEvents.valueAt(i);
                for (int j = 0; j < events.size(); j++) {
                    Object event = events.get(j);
                    if (event instanceof AutofillId) {
                        mainSession.notifyViewDisappeared(sessionId, (AutofillId) event);
                    } else if (event instanceof View) {
                        View view = (View) event;
                        ContentCaptureSession session = view.getContentCaptureSession();
                        if (session == null) {
                            String str = this.mTag;
                            Log.w(str, "no content capture session on view: " + view);
                        } else {
                            int actualId = session.getId();
                            if (actualId != sessionId) {
                                String str2 = this.mTag;
                                Log.w(str2, "content capture session mismatch for view (" + view + "): was " + sessionId + " before, it's " + actualId + " now");
                            } else {
                                ViewStructure structure = session.newViewStructure(view);
                                view.onProvideContentCaptureStructure(structure, 0);
                                session.notifyViewAppeared(structure);
                            }
                        }
                    } else if (event instanceof Insets) {
                        mainSession.notifyViewInsetsChanged(sessionId, (Insets) event);
                    } else {
                        String str3 = this.mTag;
                        Log.w(str3, "invalid content capture event: " + event);
                    }
                }
                mainSession.notifyViewTreeEvent(sessionId, false);
            }
            this.mAttachInfo.mContentCaptureEvents = null;
        } finally {
            Trace.traceEnd(8L);
        }
    }

    private void notifyHolderSurfaceDestroyed() {
        this.mSurfaceHolder.ungetCallbacks();
        SurfaceHolder.Callback[] callbacks = this.mSurfaceHolder.getCallbacks();
        if (callbacks != null) {
            for (SurfaceHolder.Callback c : callbacks) {
                c.surfaceDestroyed(this.mSurfaceHolder);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeHandleWindowMove(Rect frame) {
        boolean windowMoved = (this.mAttachInfo.mWindowLeft == frame.left && this.mAttachInfo.mWindowTop == frame.top) ? false : true;
        if (windowMoved) {
            this.mAttachInfo.mWindowLeft = frame.left;
            this.mAttachInfo.mWindowTop = frame.top;
        }
        if (windowMoved || this.mAttachInfo.mNeedsUpdateLightCenter) {
            if (this.mAttachInfo.mThreadedRenderer != null) {
                this.mAttachInfo.mThreadedRenderer.setLightCenter(this.mAttachInfo);
            }
            this.mAttachInfo.mNeedsUpdateLightCenter = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWindowFocusChanged() {
        boolean z;
        synchronized (this) {
            if (!this.mWindowFocusChanged) {
                return;
            }
            this.mWindowFocusChanged = false;
            boolean hasWindowFocus = this.mUpcomingWindowFocus;
            boolean inTouchMode = this.mUpcomingInTouchMode;
            if (hasWindowFocus) {
                InsetsController insetsController = this.mInsetsController;
                if (getFocusedViewOrNull() == null) {
                    z = false;
                } else {
                    z = true;
                }
                insetsController.onWindowFocusGained(z);
            } else {
                this.mInsetsController.onWindowFocusLost();
            }
            if (this.mAdded) {
                profileRendering(hasWindowFocus);
                if (hasWindowFocus) {
                    ensureTouchModeLocally(inTouchMode);
                    if (this.mAttachInfo.mThreadedRenderer != null && this.mSurface.isValid()) {
                        this.mFullRedrawNeeded = true;
                        try {
                            Rect surfaceInsets = this.mWindowAttributes.surfaceInsets;
                            this.mAttachInfo.mThreadedRenderer.initializeIfNeeded(this.mWidth, this.mHeight, this.mAttachInfo, this.mSurface, surfaceInsets);
                        } catch (Surface.OutOfResourcesException e) {
                            Log.e(this.mTag, "OutOfResourcesException locking surface", e);
                            try {
                                if (!this.mWindowSession.outOfMemory(this.mWindow)) {
                                    Slog.w(this.mTag, "No processes killed for memory; killing self");
                                    Process.killProcess(Process.myPid());
                                }
                            } catch (RemoteException e2) {
                            }
                            ViewRootHandler viewRootHandler = this.mHandler;
                            viewRootHandler.sendMessageDelayed(viewRootHandler.obtainMessage(6), 500L);
                            return;
                        }
                    }
                }
                this.mAttachInfo.mHasWindowFocus = hasWindowFocus;
                this.mImeFocusController.updateImeFocusable(this.mWindowAttributes, true);
                this.mImeFocusController.onPreWindowFocus(hasWindowFocus, this.mWindowAttributes);
                if (this.mView != null) {
                    this.mAttachInfo.mKeyDispatchState.reset();
                    this.mView.dispatchWindowFocusChanged(hasWindowFocus);
                    this.mAttachInfo.mTreeObserver.dispatchOnWindowFocusChange(hasWindowFocus);
                    if (this.mAttachInfo.mTooltipHost != null) {
                        this.mAttachInfo.mTooltipHost.hideTooltip();
                    }
                }
                this.mImeFocusController.onPostWindowFocus(getFocusedViewOrNull(), hasWindowFocus, this.mWindowAttributes);
                if (hasWindowFocus) {
                    this.mWindowAttributes.softInputMode &= TrafficStats.TAG_NETWORK_STACK_RANGE_END;
                    ((WindowManager.LayoutParams) this.mView.getLayoutParams()).softInputMode &= TrafficStats.TAG_NETWORK_STACK_RANGE_END;
                    fireAccessibilityFocusEventIfHasFocusedNode();
                } else if (this.mPointerCapture) {
                    handlePointerCaptureChanged(false);
                }
            }
            this.mFirstInputStage.onWindowFocusChanged(hasWindowFocus);
            if (hasWindowFocus) {
                handleContentCaptureFlush();
            }
        }
    }

    private void fireAccessibilityFocusEventIfHasFocusedNode() {
        View focusedView;
        if (!AccessibilityManager.getInstance(this.mContext).isEnabled() || (focusedView = this.mView.findFocus()) == null) {
            return;
        }
        AccessibilityNodeProvider provider = focusedView.getAccessibilityNodeProvider();
        if (provider == null) {
            focusedView.sendAccessibilityEvent(8);
            return;
        }
        AccessibilityNodeInfo focusedNode = findFocusedVirtualNode(provider);
        if (focusedNode != null) {
            int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(focusedNode.getSourceNodeId());
            AccessibilityEvent event = AccessibilityEvent.obtain(8);
            event.setSource(focusedView, virtualId);
            event.setPackageName(focusedNode.getPackageName());
            event.setChecked(focusedNode.isChecked());
            event.setContentDescription(focusedNode.getContentDescription());
            event.setPassword(focusedNode.isPassword());
            event.getText().add(focusedNode.getText());
            event.setEnabled(focusedNode.isEnabled());
            focusedView.getParent().requestSendAccessibilityEvent(focusedView, event);
            focusedNode.recycle();
        }
    }

    private AccessibilityNodeInfo findFocusedVirtualNode(AccessibilityNodeProvider provider) {
        AccessibilityNodeInfo focusedNode = provider.findFocus(1);
        if (focusedNode != null) {
            return focusedNode;
        }
        if (!this.mContext.isAutofillCompatibilityEnabled()) {
            return null;
        }
        AccessibilityNodeInfo current = provider.createAccessibilityNodeInfo(-1);
        if (current.isFocused()) {
            return current;
        }
        Queue<AccessibilityNodeInfo> fringe = new LinkedList<>();
        fringe.offer(current);
        while (!fringe.isEmpty()) {
            AccessibilityNodeInfo current2 = fringe.poll();
            LongArray childNodeIds = current2.getChildNodeIds();
            if (childNodeIds != null && childNodeIds.size() > 0) {
                int childCount = childNodeIds.size();
                for (int i = 0; i < childCount; i++) {
                    int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(childNodeIds.get(i));
                    AccessibilityNodeInfo child = provider.createAccessibilityNodeInfo(virtualId);
                    if (child != null) {
                        if (child.isFocused()) {
                            return child;
                        }
                        fringe.offer(child);
                    }
                }
                current2.recycle();
            }
        }
        return null;
    }

    private void handleOutOfResourcesException(Surface.OutOfResourcesException e) {
        Log.e(this.mTag, "OutOfResourcesException initializing HW surface", e);
        try {
            if (!this.mWindowSession.outOfMemory(this.mWindow) && Process.myUid() != 1000) {
                Slog.w(this.mTag, "No processes killed for memory; killing self");
                Process.killProcess(Process.myPid());
            }
        } catch (RemoteException e2) {
        }
        this.mLayoutRequested = true;
    }

    private void performMeasure(int childWidthMeasureSpec, int childHeightMeasureSpec) {
        if (this.mView == null) {
            return;
        }
        Trace.traceBegin(8L, "measure");
        try {
            this.mView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        } finally {
            Trace.traceEnd(8L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isInLayout() {
        return this.mInLayout;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean requestLayoutDuringLayout(View view) {
        if (view.mParent == null || view.mAttachInfo == null) {
            return true;
        }
        if (!this.mLayoutRequesters.contains(view)) {
            this.mLayoutRequesters.add(view);
        }
        return !this.mHandlingLayoutInLayoutRequest;
    }

    private void performLayout(WindowManager.LayoutParams lp, int desiredWindowWidth, int desiredWindowHeight) {
        ArrayList<View> validLayoutRequesters;
        this.mScrollMayChange = true;
        this.mInLayout = true;
        View host = this.mView;
        if (host == null) {
            return;
        }
        if (DEBUG_ORIENTATION || DEBUG_LAYOUT) {
            String str = this.mTag;
            Log.v(str, "Laying out " + host + " to (" + host.getMeasuredWidth() + ", " + host.getMeasuredHeight() + ")");
        }
        Trace.traceBegin(8L, TtmlUtils.TAG_LAYOUT);
        try {
            host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
            this.mInLayout = false;
            int numViewsRequestingLayout = this.mLayoutRequesters.size();
            if (numViewsRequestingLayout > 0 && (validLayoutRequesters = getValidLayoutRequesters(this.mLayoutRequesters, false)) != null) {
                this.mHandlingLayoutInLayoutRequest = true;
                int numValidRequests = validLayoutRequesters.size();
                for (int i = 0; i < numValidRequests; i++) {
                    View view = validLayoutRequesters.get(i);
                    Log.w("View", "requestLayout() improperly called by " + view + " during layout: running second layout pass");
                    view.requestLayout();
                }
                measureHierarchy(host, lp, this.mView.getContext().getResources(), desiredWindowWidth, desiredWindowHeight);
                this.mInLayout = true;
                host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
                this.mHandlingLayoutInLayoutRequest = false;
                final ArrayList<View> validLayoutRequesters2 = getValidLayoutRequesters(this.mLayoutRequesters, true);
                if (validLayoutRequesters2 != null) {
                    getRunQueue().post(new Runnable() { // from class: android.view.ViewRootImpl.2
                        @Override // java.lang.Runnable
                        public void run() {
                            int numValidRequests2 = validLayoutRequesters2.size();
                            for (int i2 = 0; i2 < numValidRequests2; i2++) {
                                View view2 = (View) validLayoutRequesters2.get(i2);
                                Log.w("View", "requestLayout() improperly called by " + view2 + " during second layout pass: posting in next frame");
                                view2.requestLayout();
                            }
                        }
                    });
                }
            }
            Trace.traceEnd(8L);
            this.mInLayout = false;
        } catch (Throwable th) {
            Trace.traceEnd(8L);
            throw th;
        }
    }

    private ArrayList<View> getValidLayoutRequesters(ArrayList<View> layoutRequesters, boolean secondLayoutRequests) {
        int numViewsRequestingLayout = layoutRequesters.size();
        ArrayList<View> validLayoutRequesters = null;
        for (int i = 0; i < numViewsRequestingLayout; i++) {
            View view = layoutRequesters.get(i);
            if (view != null && view.mAttachInfo != null && view.mParent != null && (secondLayoutRequests || (view.mPrivateFlags & 4096) == 4096)) {
                boolean gone = false;
                View parent = view;
                while (true) {
                    if (parent == null) {
                        break;
                    } else if ((parent.mViewFlags & 12) == 8) {
                        gone = true;
                        break;
                    } else if (parent.mParent instanceof View) {
                        parent = (View) parent.mParent;
                    } else {
                        parent = null;
                    }
                }
                if (!gone) {
                    if (validLayoutRequesters == null) {
                        validLayoutRequesters = new ArrayList<>();
                    }
                    validLayoutRequesters.add(view);
                }
            }
        }
        if (!secondLayoutRequests) {
            for (int i2 = 0; i2 < numViewsRequestingLayout; i2++) {
                View view2 = layoutRequesters.get(i2);
                while (view2 != null && (view2.mPrivateFlags & 4096) != 0) {
                    view2.mPrivateFlags &= -4097;
                    if (view2.mParent instanceof View) {
                        view2 = (View) view2.mParent;
                    } else {
                        view2 = null;
                    }
                }
            }
        }
        layoutRequesters.clear();
        return validLayoutRequesters;
    }

    @Override // android.view.ViewParent
    public void requestTransparentRegion(View child) {
        checkThread();
        View view = this.mView;
        if (view != child) {
            return;
        }
        if ((view.mPrivateFlags & 512) == 0) {
            this.mView.mPrivateFlags |= 512;
            this.mWindowAttributesChanged = true;
        }
        requestLayout();
    }

    private static int getRootMeasureSpec(int windowSize, int rootDimension) {
        switch (rootDimension) {
            case -2:
                int measureSpec = View.MeasureSpec.makeMeasureSpec(windowSize, Integer.MIN_VALUE);
                return measureSpec;
            case -1:
                int measureSpec2 = View.MeasureSpec.makeMeasureSpec(windowSize, 1073741824);
                return measureSpec2;
            default:
                int measureSpec3 = View.MeasureSpec.makeMeasureSpec(rootDimension, 1073741824);
                return measureSpec3;
        }
    }

    @Override // android.view.ThreadedRenderer.DrawCallbacks
    public void onPreDraw(RecordingCanvas canvas) {
        if (this.mCurScrollY != 0 && this.mHardwareYOffset != 0 && this.mAttachInfo.mThreadedRenderer.isOpaque()) {
            canvas.drawColor(-16777216);
        }
        canvas.translate(-this.mHardwareXOffset, -this.mHardwareYOffset);
    }

    @Override // android.view.ThreadedRenderer.DrawCallbacks
    public void onPostDraw(RecordingCanvas canvas) {
        drawAccessibilityFocusedDrawableIfNeeded(canvas);
        if (this.mUseMTRenderer) {
            for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                this.mWindowCallbacks.get(i).onPostDraw(canvas);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void outputDisplayList(View view) {
        view.mRenderNode.output();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void profileRendering(boolean enabled) {
        if (this.mProfileRendering) {
            this.mRenderProfilingEnabled = enabled;
            Choreographer.FrameCallback frameCallback = this.mRenderProfiler;
            if (frameCallback != null) {
                this.mChoreographer.removeFrameCallback(frameCallback);
            }
            if (this.mRenderProfilingEnabled) {
                if (this.mRenderProfiler == null) {
                    this.mRenderProfiler = new Choreographer.FrameCallback() { // from class: android.view.ViewRootImpl.3
                        @Override // android.view.Choreographer.FrameCallback
                        public void doFrame(long frameTimeNanos) {
                            ViewRootImpl.this.mDirty.set(0, 0, ViewRootImpl.this.mWidth, ViewRootImpl.this.mHeight);
                            ViewRootImpl.this.scheduleTraversals();
                            if (ViewRootImpl.this.mRenderProfilingEnabled) {
                                ViewRootImpl.this.mChoreographer.postFrameCallback(ViewRootImpl.this.mRenderProfiler);
                            }
                        }
                    };
                }
                this.mChoreographer.postFrameCallback(this.mRenderProfiler);
                return;
            }
            this.mRenderProfiler = null;
        }
    }

    private void trackFPS() {
        long nowTime = System.currentTimeMillis();
        if (this.mFpsStartTime < 0) {
            this.mFpsPrevTime = nowTime;
            this.mFpsStartTime = nowTime;
            this.mFpsNumFrames = 0;
            return;
        }
        this.mFpsNumFrames++;
        String thisHash = Integer.toHexString(System.identityHashCode(this));
        long frameTime = nowTime - this.mFpsPrevTime;
        long totalTime = nowTime - this.mFpsStartTime;
        Log.v(this.mTag, "0x" + thisHash + "\tFrame time:\t" + frameTime);
        this.mFpsPrevTime = nowTime;
        if (totalTime > 1000) {
            float fps = (this.mFpsNumFrames * 1000.0f) / ((float) totalTime);
            Log.v(this.mTag, "0x" + thisHash + "\tFPS:\t" + fps);
            this.mFpsStartTime = nowTime;
            this.mFpsNumFrames = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void drawPending() {
        this.mDrawsNeededToReport++;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void pendingDrawFinished() {
        int i = this.mDrawsNeededToReport;
        if (i == 0) {
            throw new RuntimeException("Unbalanced drawPending/pendingDrawFinished calls");
        }
        int i2 = i - 1;
        this.mDrawsNeededToReport = i2;
        if (i2 == 0) {
            reportDrawFinished();
        } else if (DEBUG_BLAST) {
            Log.d(this.mTag, "pendingDrawFinished. Waiting on draw reported mDrawsNeededToReport=" + this.mDrawsNeededToReport);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postDrawFinished() {
        this.mHandler.sendEmptyMessage(29);
    }

    private void reportDrawFinished() {
        try {
            try {
                if (DEBUG_BLAST) {
                    Log.d(this.mTag, "reportDrawFinished");
                }
                this.mDrawsNeededToReport = 0;
                this.mWindowSession.finishDrawing(this.mWindow, this.mSurfaceChangedTransaction);
            } catch (RemoteException e) {
                Log.e(this.mTag, "Unable to report draw finished", e);
                this.mSurfaceChangedTransaction.apply();
            }
        } finally {
            this.mSurfaceChangedTransaction.clear();
        }
    }

    private HardwareRenderer.FrameCompleteCallback createFrameCompleteCallback(final Handler handler, final boolean reportNextDraw, final ArrayList<Runnable> commitCallbacks) {
        return new HardwareRenderer.FrameCompleteCallback() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda2
            @Override // android.graphics.HardwareRenderer.FrameCompleteCallback
            public final void onFrameComplete(long j) {
                ViewRootImpl.this.lambda$createFrameCompleteCallback$4$ViewRootImpl(handler, reportNextDraw, commitCallbacks, j);
            }
        };
    }

    public /* synthetic */ void lambda$createFrameCompleteCallback$4$ViewRootImpl(Handler handler, final boolean reportNextDraw, final ArrayList commitCallbacks, long frameNr) {
        if (DEBUG_BLAST) {
            String str = this.mTag;
            Log.d(str, "Received frameCompleteCallback frameNum=" + frameNr);
        }
        handler.postAtFrontOfQueue(new Runnable() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                ViewRootImpl.this.lambda$createFrameCompleteCallback$3$ViewRootImpl(reportNextDraw, commitCallbacks);
            }
        });
    }

    public /* synthetic */ void lambda$createFrameCompleteCallback$3$ViewRootImpl(boolean reportNextDraw, ArrayList commitCallbacks) {
        if (this.mNextDrawUseBlastSync) {
            this.mSurfaceChangedTransaction.merge(this.mRtBLASTSyncTransaction);
        }
        if (reportNextDraw) {
            pendingDrawFinished();
        }
        if (commitCallbacks != null) {
            for (int i = 0; i < commitCallbacks.size(); i++) {
                ((Runnable) commitCallbacks.get(i)).run();
            }
        }
    }

    public boolean isHardwareEnabled() {
        return this.mAttachInfo.mThreadedRenderer != null && this.mAttachInfo.mThreadedRenderer.isEnabled();
    }

    private boolean addFrameCompleteCallbackIfNeeded() {
        int i = 0;
        if (!isHardwareEnabled()) {
            return false;
        }
        ArrayList<Runnable> commitCallbacks = this.mAttachInfo.mTreeObserver.captureFrameCommitCallbacks();
        boolean needFrameCompleteCallback = this.mNextDrawUseBlastSync || this.mReportNextDraw || (commitCallbacks != null && commitCallbacks.size() > 0);
        if (!needFrameCompleteCallback) {
            return false;
        }
        if (DEBUG_BLAST) {
            String str = this.mTag;
            StringBuilder sb = new StringBuilder();
            sb.append("Creating frameCompleteCallback mNextDrawUseBlastSync=");
            sb.append(this.mNextDrawUseBlastSync);
            sb.append(" mReportNextDraw=");
            sb.append(this.mReportNextDraw);
            sb.append(" commitCallbacks size=");
            if (commitCallbacks != null) {
                i = commitCallbacks.size();
            }
            sb.append(i);
            Log.d(str, sb.toString());
        }
        this.mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(createFrameCompleteCallback(this.mAttachInfo.mHandler, this.mReportNextDraw, commitCallbacks));
        return true;
    }

    private void addFrameCallbackIfNeeded() {
        final boolean nextDrawUseBlastSync = this.mNextDrawUseBlastSync;
        final boolean reportNextDraw = this.mReportNextDraw;
        final boolean hasBlurUpdates = this.mBlurRegionAggregator.hasUpdates();
        final boolean needsCallbackForBlur = hasBlurUpdates || this.mBlurRegionAggregator.hasRegions();
        if (!nextDrawUseBlastSync && !reportNextDraw && !needsCallbackForBlur) {
            return;
        }
        if (DEBUG_BLAST) {
            String str = this.mTag;
            Log.d(str, "Creating frameDrawingCallback nextDrawUseBlastSync=" + nextDrawUseBlastSync + " reportNextDraw=" + reportNextDraw + " hasBlurUpdates=" + hasBlurUpdates);
        }
        this.mWaitForBlastSyncComplete = nextDrawUseBlastSync;
        final BackgroundBlurDrawable.BlurRegion[] blurRegionsForFrame = needsCallbackForBlur ? this.mBlurRegionAggregator.getBlurRegionsCopyForRT() : null;
        HardwareRenderer.FrameDrawingCallback frameDrawingCallback = new HardwareRenderer.FrameDrawingCallback() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda5
            @Override // android.graphics.HardwareRenderer.FrameDrawingCallback
            public final void onFrameDraw(long j) {
                ViewRootImpl.this.lambda$addFrameCallbackIfNeeded$7$ViewRootImpl(nextDrawUseBlastSync, needsCallbackForBlur, blurRegionsForFrame, hasBlurUpdates, reportNextDraw, j);
            }
        };
        registerRtFrameCallback(frameDrawingCallback);
    }

    public /* synthetic */ void lambda$addFrameCallbackIfNeeded$7$ViewRootImpl(boolean nextDrawUseBlastSync, boolean needsCallbackForBlur, BackgroundBlurDrawable.BlurRegion[] blurRegionsForFrame, boolean hasBlurUpdates, boolean reportNextDraw, final long frame) {
        if (DEBUG_BLAST) {
            String str = this.mTag;
            Log.d(str, "Received frameDrawingCallback frameNum=" + frame + ". Creating transactionCompleteCallback=" + nextDrawUseBlastSync);
        }
        if (needsCallbackForBlur) {
            this.mBlurRegionAggregator.dispatchBlurTransactionIfNeeded(frame, blurRegionsForFrame, hasBlurUpdates);
        }
        BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
        if (bLASTBufferQueue == null) {
            return;
        }
        if (nextDrawUseBlastSync) {
            bLASTBufferQueue.setNextTransaction(this.mRtBLASTSyncTransaction);
            this.mBlastBufferQueue.setTransactionCompleteCallback(frame, new BLASTBufferQueue.TransactionCompleteCallback() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda0
                @Override // android.graphics.BLASTBufferQueue.TransactionCompleteCallback
                public final void onTransactionComplete(long j) {
                    ViewRootImpl.this.lambda$addFrameCallbackIfNeeded$6$ViewRootImpl(frame, j);
                }
            });
        } else if (reportNextDraw) {
            bLASTBufferQueue.flushShadowQueue();
        }
    }

    public /* synthetic */ void lambda$addFrameCallbackIfNeeded$6$ViewRootImpl(long frame, long frameNumber) {
        if (DEBUG_BLAST) {
            String str = this.mTag;
            Log.d(str, "Received transactionCompleteCallback frameNum=" + frame);
        }
        this.mHandler.postAtFrontOfQueue(new Runnable() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                ViewRootImpl.this.lambda$addFrameCallbackIfNeeded$5$ViewRootImpl();
            }
        });
    }

    public /* synthetic */ void lambda$addFrameCallbackIfNeeded$5$ViewRootImpl() {
        this.mNextDrawUseBlastSync = false;
        this.mWaitForBlastSyncComplete = false;
        if (DEBUG_BLAST) {
            String str = this.mTag;
            Log.d(str, "Scheduling a traversal=" + this.mRequestedTraverseWhilePaused + " due to a previous skipped traversal.");
        }
        if (this.mRequestedTraverseWhilePaused) {
            this.mRequestedTraverseWhilePaused = false;
            scheduleTraversals();
        }
    }

    private void performDraw() {
        if ((this.mAttachInfo.mDisplayState == 1 && !this.mReportNextDraw) || this.mView == null) {
            return;
        }
        boolean fullRedrawNeeded = this.mFullRedrawNeeded || this.mReportNextDraw || this.mNextDrawUseBlastSync;
        this.mFullRedrawNeeded = false;
        this.mIsDrawing = true;
        Trace.traceBegin(8L, "draw");
        boolean usingAsyncReport = addFrameCompleteCallbackIfNeeded();
        addFrameCallbackIfNeeded();
        try {
            boolean canUseAsync = draw(fullRedrawNeeded);
            if (usingAsyncReport && !canUseAsync) {
                this.mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(null);
                usingAsyncReport = false;
            }
            this.mIsDrawing = false;
            Trace.traceEnd(8L);
            if (this.mAttachInfo.mPendingAnimatingRenderNodes != null) {
                int count = this.mAttachInfo.mPendingAnimatingRenderNodes.size();
                for (int i = 0; i < count; i++) {
                    this.mAttachInfo.mPendingAnimatingRenderNodes.get(i).endAllAnimators();
                }
                this.mAttachInfo.mPendingAnimatingRenderNodes.clear();
            }
            if (this.mReportNextDraw) {
                this.mReportNextDraw = false;
                CountDownLatch countDownLatch = this.mWindowDrawCountDown;
                if (countDownLatch != null) {
                    try {
                        countDownLatch.await();
                    } catch (InterruptedException e) {
                        Log.e(this.mTag, "Window redraw count down interrupted!");
                    }
                    this.mWindowDrawCountDown = null;
                }
                if (this.mAttachInfo.mThreadedRenderer != null) {
                    this.mAttachInfo.mThreadedRenderer.setStopped(this.mStopped);
                }
                if (LOCAL_LOGV) {
                    String str = this.mTag;
                    Log.v(str, "FINISHED DRAWING: " + ((Object) this.mWindowAttributes.getTitle()));
                }
                if (this.mSurfaceHolder != null && this.mSurface.isValid()) {
                    SurfaceCallbackHelper sch = new SurfaceCallbackHelper(new Runnable() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda10
                        @Override // java.lang.Runnable
                        public final void run() {
                            ViewRootImpl.this.postDrawFinished();
                        }
                    });
                    SurfaceHolder.Callback[] callbacks = this.mSurfaceHolder.getCallbacks();
                    sch.dispatchSurfaceRedrawNeededAsync(this.mSurfaceHolder, callbacks);
                } else if (!usingAsyncReport) {
                    if (this.mAttachInfo.mThreadedRenderer != null) {
                        this.mAttachInfo.mThreadedRenderer.fence();
                    }
                    pendingDrawFinished();
                }
            }
            if (this.mPerformContentCapture) {
                performContentCaptureInitialReport();
            }
        } catch (Throwable th) {
            this.mIsDrawing = false;
            Trace.traceEnd(8L);
            throw th;
        }
    }

    private boolean isContentCaptureEnabled() {
        int i = 1;
        switch (this.mContentCaptureEnabled) {
            case 0:
                boolean reallyEnabled = isContentCaptureReallyEnabled();
                if (!reallyEnabled) {
                    i = 2;
                }
                this.mContentCaptureEnabled = i;
                return reallyEnabled;
            case 1:
                return true;
            case 2:
                return false;
            default:
                Log.w(TAG, "isContentCaptureEnabled(): invalid state " + this.mContentCaptureEnabled);
                return false;
        }
    }

    private boolean isContentCaptureReallyEnabled() {
        ContentCaptureManager ccm;
        return (this.mContext.getContentCaptureOptions() == null || (ccm = this.mAttachInfo.getContentCaptureManager(this.mContext)) == null || !ccm.isContentCaptureEnabled()) ? false : true;
    }

    private void performContentCaptureInitialReport() {
        this.mPerformContentCapture = false;
        View rootView = this.mView;
        if (DEBUG_CONTENT_CAPTURE) {
            String str = this.mTag;
            Log.v(str, "performContentCaptureInitialReport() on " + rootView);
        }
        if (Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "dispatchContentCapture() for " + getClass().getSimpleName());
        }
        try {
            if (!isContentCaptureEnabled()) {
                return;
            }
            rootView.dispatchInitialProvideContentCaptureStructure();
        } finally {
            Trace.traceEnd(8L);
        }
    }

    private void handleContentCaptureFlush() {
        if (DEBUG_CONTENT_CAPTURE) {
            Log.v(this.mTag, "handleContentCaptureFlush()");
        }
        if (Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "flushContentCapture for " + getClass().getSimpleName());
        }
        try {
            if (!isContentCaptureEnabled()) {
                return;
            }
            ContentCaptureManager ccm = this.mAttachInfo.mContentCaptureManager;
            if (ccm == null) {
                Log.w(TAG, "No ContentCapture on AttachInfo");
            } else {
                ccm.flush(2);
            }
        } finally {
            Trace.traceEnd(8L);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:100:0x021c  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x02a5  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01b8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean draw(boolean fullRedrawNeeded) {
        int curScrollY;
        boolean fullRedrawNeeded2;
        int xOffset;
        boolean accessibilityFocusDirty;
        int xOffset2;
        Drawable drawable;
        Scroller scroller;
        Surface surface = this.mSurface;
        if (!surface.isValid()) {
            return false;
        }
        if (DEBUG_FPS) {
            trackFPS();
        }
        if (!sFirstDrawComplete) {
            ArrayList<Runnable> arrayList = sFirstDrawHandlers;
            synchronized (arrayList) {
                sFirstDrawComplete = true;
                int count = arrayList.size();
                for (int i = 0; i < count; i++) {
                    this.mHandler.post(sFirstDrawHandlers.get(i));
                }
            }
        }
        Rect rect = null;
        scrollToRectOrFocus(null, false);
        if (this.mAttachInfo.mViewScrollChanged) {
            this.mAttachInfo.mViewScrollChanged = false;
            this.mAttachInfo.mTreeObserver.dispatchOnScrollChanged();
        }
        Scroller scroller2 = this.mScroller;
        boolean animating = scroller2 != null && scroller2.computeScrollOffset();
        if (animating) {
            curScrollY = this.mScroller.getCurrY();
        } else {
            int curScrollY2 = this.mScrollY;
            curScrollY = curScrollY2;
        }
        if (this.mCurScrollY == curScrollY) {
            fullRedrawNeeded2 = fullRedrawNeeded;
        } else {
            this.mCurScrollY = curScrollY;
            View view = this.mView;
            if (view instanceof RootViewSurfaceTaker) {
                ((RootViewSurfaceTaker) view).onRootViewScrollYChanged(curScrollY);
            }
            fullRedrawNeeded2 = true;
        }
        float appScale = this.mAttachInfo.mApplicationScale;
        boolean scalingRequired = this.mAttachInfo.mScalingRequired;
        Rect dirty = this.mDirty;
        if (this.mSurfaceHolder != null) {
            dirty.setEmpty();
            if (animating && (scroller = this.mScroller) != null) {
                scroller.abortAnimation();
            }
            return false;
        }
        if (fullRedrawNeeded2) {
            dirty.set(0, 0, (int) ((this.mWidth * appScale) + 0.5f), (int) ((this.mHeight * appScale) + 0.5f));
        }
        if (DEBUG_ORIENTATION || DEBUG_DRAW) {
            Log.v(this.mTag, "Draw " + this.mView + "/" + ((Object) this.mWindowAttributes.getTitle()) + ": dirty={" + dirty.left + "," + dirty.top + "," + dirty.right + "," + dirty.bottom + "} surface=" + surface + " surface.isValid()=" + surface.isValid() + ", appScale:" + appScale + ", width=" + this.mWidth + ", height=" + this.mHeight);
        }
        this.mAttachInfo.mTreeObserver.dispatchOnDraw();
        int xOffset3 = -this.mCanvasOffsetX;
        int yOffset = (-this.mCanvasOffsetY) + curScrollY;
        WindowManager.LayoutParams params = this.mWindowAttributes;
        if (params != null) {
            rect = params.surfaceInsets;
        }
        Rect surfaceInsets = rect;
        if (surfaceInsets == null) {
            xOffset = xOffset3;
        } else {
            int xOffset4 = xOffset3 - surfaceInsets.left;
            yOffset -= surfaceInsets.top;
            dirty.offset(surfaceInsets.left, surfaceInsets.top);
            xOffset = xOffset4;
        }
        Drawable drawable2 = this.mAttachInfo.mAccessibilityFocusDrawable;
        if (drawable2 != null) {
            Rect bounds = this.mAttachInfo.mTmpInvalRect;
            boolean hasFocus = getAccessibilityFocusedRect(bounds);
            if (!hasFocus) {
                bounds.setEmpty();
            }
            if (!bounds.equals(drawable2.getBounds())) {
                accessibilityFocusDirty = true;
                this.mAttachInfo.mDrawingTime = this.mChoreographer.getFrameTimeNanos() / TimeUtils.NANOS_PER_MS;
                boolean useAsyncReport = false;
                if (dirty.isEmpty() || this.mIsAnimating || accessibilityFocusDirty || this.mNextDrawUseBlastSync) {
                    if (isHardwareEnabled()) {
                        int yOffset2 = yOffset;
                        if (this.mAttachInfo.mThreadedRenderer == null) {
                            xOffset2 = xOffset;
                        } else if (this.mAttachInfo.mThreadedRenderer.isEnabled()) {
                            xOffset2 = xOffset;
                        } else if (!this.mAttachInfo.mThreadedRenderer.isRequested()) {
                            xOffset2 = xOffset;
                        } else if (this.mSurface.isValid()) {
                            try {
                            } catch (Surface.OutOfResourcesException e) {
                                e = e;
                            }
                            try {
                                this.mAttachInfo.mThreadedRenderer.initializeIfNeeded(this.mWidth, this.mHeight, this.mAttachInfo, this.mSurface, surfaceInsets);
                                this.mFullRedrawNeeded = true;
                                scheduleTraversals();
                                return false;
                            } catch (Surface.OutOfResourcesException e2) {
                                e = e2;
                                handleOutOfResourcesException(e);
                                return false;
                            }
                        } else {
                            xOffset2 = xOffset;
                        }
                        if (!drawSoftware(surface, this.mAttachInfo, xOffset2, yOffset2, scalingRequired, dirty, surfaceInsets)) {
                            return false;
                        }
                    } else {
                        boolean invalidateRoot = accessibilityFocusDirty || this.mInvalidateRootRequested;
                        this.mInvalidateRootRequested = false;
                        this.mIsAnimating = false;
                        if (this.mHardwareYOffset != yOffset || this.mHardwareXOffset != xOffset) {
                            this.mHardwareYOffset = yOffset;
                            this.mHardwareXOffset = xOffset;
                            invalidateRoot = true;
                        }
                        if (invalidateRoot) {
                            this.mAttachInfo.mThreadedRenderer.invalidateRoot();
                        }
                        dirty.setEmpty();
                        boolean updated = updateContentDrawBounds();
                        boolean invalidateRoot2 = this.mReportNextDraw;
                        if (!invalidateRoot2) {
                            drawable = drawable2;
                        } else {
                            drawable = drawable2;
                            this.mAttachInfo.mThreadedRenderer.setStopped(false);
                        }
                        if (updated) {
                            requestDrawWindow();
                        }
                        useAsyncReport = true;
                        this.mAttachInfo.mThreadedRenderer.draw(this.mView, this.mAttachInfo, this);
                    }
                }
                if (animating) {
                    this.mFullRedrawNeeded = true;
                    scheduleTraversals();
                }
                return useAsyncReport;
            }
        }
        accessibilityFocusDirty = false;
        this.mAttachInfo.mDrawingTime = this.mChoreographer.getFrameTimeNanos() / TimeUtils.NANOS_PER_MS;
        boolean useAsyncReport2 = false;
        if (dirty.isEmpty()) {
        }
        if (isHardwareEnabled()) {
        }
        if (animating) {
        }
        return useAsyncReport2;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0099 A[Catch: all -> 0x0127, TryCatch #2 {all -> 0x0127, blocks: (B:9:0x0041, B:11:0x0045, B:13:0x0075, B:19:0x0088, B:21:0x0099, B:22:0x00d9, B:24:0x00e4, B:26:0x00e9, B:27:0x00ed, B:43:0x0082, B:44:0x0049), top: B:8:0x0041 }] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00e4 A[Catch: all -> 0x0127, TryCatch #2 {all -> 0x0127, blocks: (B:9:0x0041, B:11:0x0045, B:13:0x0075, B:19:0x0088, B:21:0x0099, B:22:0x00d9, B:24:0x00e4, B:26:0x00e9, B:27:0x00ed, B:43:0x0082, B:44:0x0049), top: B:8:0x0041 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x00e9 A[Catch: all -> 0x0127, TryCatch #2 {all -> 0x0127, blocks: (B:9:0x0041, B:11:0x0045, B:13:0x0075, B:19:0x0088, B:21:0x0099, B:22:0x00d9, B:24:0x00e4, B:26:0x00e9, B:27:0x00ed, B:43:0x0082, B:44:0x0049), top: B:8:0x0041 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:36:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00ec  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean drawSoftware(Surface surface, View.AttachInfo attachInfo, int xoff, int yoff, boolean scalingRequired, Rect dirty, Rect surfaceInsets) {
        int dirtyYOffset;
        int dirtyYOffset2;
        boolean z;
        boolean z2;
        CompatibilityInfo.Translator translator;
        if (surfaceInsets != null) {
            int dirtyXOffset = xoff + surfaceInsets.left;
            int dirtyYOffset3 = yoff + surfaceInsets.top;
            dirtyYOffset = dirtyYOffset3;
            dirtyYOffset2 = dirtyXOffset;
        } else {
            dirtyYOffset = yoff;
            dirtyYOffset2 = xoff;
        }
        int dirtyXOffset2 = -dirtyYOffset2;
        try {
            try {
                dirty.offset(dirtyXOffset2, -dirtyYOffset);
                int i = dirty.left;
                int i2 = dirty.top;
                int i3 = dirty.right;
                int i4 = dirty.bottom;
                Canvas canvas = this.mSurface.lockCanvas(dirty);
                int left = this.mDensity;
                canvas.setDensity(left);
                try {
                    if (DEBUG_ORIENTATION || DEBUG_DRAW) {
                        Log.v(this.mTag, "Surface " + surface + " drawing to bitmap w=" + canvas.getWidth() + ", h=" + canvas.getHeight());
                    }
                    try {
                        if (canvas.isOpaque() && yoff == 0 && xoff == 0) {
                            z2 = false;
                            dirty.setEmpty();
                            this.mIsAnimating = z2;
                            this.mView.mPrivateFlags |= 32;
                            if (DEBUG_DRAW) {
                                Context cxt = this.mView.getContext();
                                Log.i(this.mTag, "Drawing: package:" + cxt.getPackageName() + ", metrics=" + cxt.getResources().getDisplayMetrics() + ", compatibilityInfo=" + cxt.getResources().getCompatibilityInfo());
                            }
                            canvas.translate(-xoff, -yoff);
                            translator = this.mTranslator;
                            if (translator != null) {
                                translator.translateCanvas(canvas);
                            }
                            canvas.setScreenDensity(!scalingRequired ? this.mNoncompatDensity : 0);
                            this.mView.draw(canvas);
                            drawAccessibilityFocusedDrawableIfNeeded(canvas);
                            surface.unlockCanvasAndPost(canvas);
                            if (LOCAL_LOGV) {
                                return true;
                            }
                            Log.v(this.mTag, "Surface " + surface + " unlockCanvasAndPost");
                            return true;
                        }
                        surface.unlockCanvasAndPost(canvas);
                        if (LOCAL_LOGV) {
                        }
                    } catch (IllegalArgumentException e) {
                        z = true;
                        Log.e(this.mTag, "Could not unlock surface", e);
                        this.mLayoutRequested = z;
                        return false;
                    }
                    z2 = false;
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    dirty.setEmpty();
                    this.mIsAnimating = z2;
                    this.mView.mPrivateFlags |= 32;
                    if (DEBUG_DRAW) {
                    }
                    canvas.translate(-xoff, -yoff);
                    translator = this.mTranslator;
                    if (translator != null) {
                    }
                    canvas.setScreenDensity(!scalingRequired ? this.mNoncompatDensity : 0);
                    this.mView.draw(canvas);
                    drawAccessibilityFocusedDrawableIfNeeded(canvas);
                } catch (Throwable e2) {
                    try {
                        surface.unlockCanvasAndPost(canvas);
                        if (LOCAL_LOGV) {
                            Log.v(this.mTag, "Surface " + surface + " unlockCanvasAndPost");
                        }
                        throw e2;
                    } catch (IllegalArgumentException e3) {
                        Log.e(this.mTag, "Could not unlock surface", e3);
                        z = true;
                        this.mLayoutRequested = z;
                        return false;
                    }
                }
            } finally {
                dirty.offset(dirtyYOffset2, dirtyYOffset);
            }
        } catch (Surface.OutOfResourcesException e4) {
            handleOutOfResourcesException(e4);
            dirty.offset(dirtyYOffset2, dirtyYOffset);
            return false;
        } catch (IllegalArgumentException e5) {
            Log.e(this.mTag, "Could not lock surface", e5);
            this.mLayoutRequested = true;
            dirty.offset(dirtyYOffset2, dirtyYOffset);
            return false;
        }
    }

    private void drawAccessibilityFocusedDrawableIfNeeded(Canvas canvas) {
        Rect bounds = this.mAttachInfo.mTmpInvalRect;
        if (getAccessibilityFocusedRect(bounds)) {
            Drawable drawable = getAccessibilityFocusedDrawable();
            if (drawable != null) {
                drawable.setBounds(bounds);
                drawable.draw(canvas);
            }
        } else if (this.mAttachInfo.mAccessibilityFocusDrawable != null) {
            this.mAttachInfo.mAccessibilityFocusDrawable.setBounds(0, 0, 0, 0);
        }
    }

    private boolean getAccessibilityFocusedRect(Rect bounds) {
        View host;
        AccessibilityManager manager = AccessibilityManager.getInstance(this.mView.mContext);
        if (!manager.isEnabled() || !manager.isTouchExplorationEnabled() || (host = this.mAccessibilityFocusedHost) == null || host.mAttachInfo == null) {
            return false;
        }
        AccessibilityNodeProvider provider = host.getAccessibilityNodeProvider();
        if (provider == null) {
            host.getBoundsOnScreen(bounds, true);
        } else {
            AccessibilityNodeInfo accessibilityNodeInfo = this.mAccessibilityFocusedVirtualView;
            if (accessibilityNodeInfo == null) {
                return false;
            }
            accessibilityNodeInfo.getBoundsInScreen(bounds);
        }
        View.AttachInfo attachInfo = this.mAttachInfo;
        bounds.offset(0, attachInfo.mViewRootImpl.mScrollY);
        bounds.offset(-attachInfo.mWindowLeft, -attachInfo.mWindowTop);
        if (!bounds.intersect(0, 0, attachInfo.mViewRootImpl.mWidth, attachInfo.mViewRootImpl.mHeight)) {
            bounds.setEmpty();
        }
        return !bounds.isEmpty();
    }

    private Drawable getAccessibilityFocusedDrawable() {
        if (this.mAttachInfo.mAccessibilityFocusDrawable == null) {
            TypedValue value = new TypedValue();
            boolean resolved = this.mView.mContext.getTheme().resolveAttribute(R.attr.accessibilityFocusedDrawable, value, true);
            if (resolved) {
                this.mAttachInfo.mAccessibilityFocusDrawable = this.mView.mContext.getDrawable(value.resourceId);
            }
        }
        if (this.mAttachInfo.mAccessibilityFocusDrawable instanceof GradientDrawable) {
            GradientDrawable drawable = (GradientDrawable) this.mAttachInfo.mAccessibilityFocusDrawable;
            drawable.setStroke(this.mAccessibilityManager.getAccessibilityFocusStrokeWidth(), this.mAccessibilityManager.getAccessibilityFocusColor());
        }
        return this.mAttachInfo.mAccessibilityFocusDrawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateSystemGestureExclusionRectsForView(View view) {
        this.mGestureExclusionTracker.updateRectsForView(view);
        this.mHandler.sendEmptyMessage(32);
    }

    void systemGestureExclusionChanged() {
        List<Rect> rectsForWindowManager = this.mGestureExclusionTracker.computeChangedRects();
        if (rectsForWindowManager != null && this.mView != null) {
            try {
                this.mWindowSession.reportSystemGestureExclusionChanged(this.mWindow, rectsForWindowManager);
                this.mAttachInfo.mTreeObserver.dispatchOnSystemGestureExclusionRectsChanged(rectsForWindowManager);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    void updateLocationInParentDisplay(int x, int y) {
        View.AttachInfo attachInfo = this.mAttachInfo;
        if (attachInfo != null && !attachInfo.mLocationInParentDisplay.equals(x, y)) {
            this.mAttachInfo.mLocationInParentDisplay.set(x, y);
        }
    }

    public void setRootSystemGestureExclusionRects(List<Rect> rects) {
        this.mGestureExclusionTracker.setRootSystemGestureExclusionRects(rects);
        this.mHandler.sendEmptyMessage(32);
    }

    public List<Rect> getRootSystemGestureExclusionRects() {
        return this.mGestureExclusionTracker.getRootSystemGestureExclusionRects();
    }

    public void requestInvalidateRootRenderNode() {
        this.mInvalidateRootRequested = true;
    }

    boolean scrollToRectOrFocus(Rect rectangle, boolean immediate) {
        int scrollY;
        Rect ci = this.mAttachInfo.mContentInsets;
        Rect vi = this.mAttachInfo.mVisibleInsets;
        int scrollY2 = 0;
        boolean handled = false;
        if (vi.left > ci.left || vi.top > ci.top || vi.right > ci.right || vi.bottom > ci.bottom) {
            scrollY2 = this.mScrollY;
            View focus = this.mView.findFocus();
            if (focus == null) {
                return false;
            }
            WeakReference<View> weakReference = this.mLastScrolledFocus;
            View lastScrolledFocus = weakReference != null ? weakReference.get() : null;
            if (focus != lastScrolledFocus) {
                rectangle = null;
            }
            if (DEBUG_INPUT_RESIZE) {
                String str = this.mTag;
                Log.v(str, "Eval scroll: focus=" + focus + " rectangle=" + rectangle + " ci=" + ci + " vi=" + vi);
            }
            if (focus == lastScrolledFocus && !this.mScrollMayChange && rectangle == null) {
                if (DEBUG_INPUT_RESIZE) {
                    String str2 = this.mTag;
                    Log.v(str2, "Keeping scroll y=" + this.mScrollY + " vi=" + vi.toShortString());
                }
            } else {
                this.mLastScrolledFocus = new WeakReference<>(focus);
                this.mScrollMayChange = false;
                if (DEBUG_INPUT_RESIZE) {
                    Log.v(this.mTag, "Need to scroll?");
                }
                if (focus.getGlobalVisibleRect(this.mVisRect, null)) {
                    if (DEBUG_INPUT_RESIZE) {
                        String str3 = this.mTag;
                        Log.v(str3, "Root w=" + this.mView.getWidth() + " h=" + this.mView.getHeight() + " ci=" + ci.toShortString() + " vi=" + vi.toShortString());
                    }
                    if (rectangle == null) {
                        focus.getFocusedRect(this.mTempRect);
                        if (DEBUG_INPUT_RESIZE) {
                            String str4 = this.mTag;
                            Log.v(str4, "Focus " + focus + ": focusRect=" + this.mTempRect.toShortString());
                        }
                        View view = this.mView;
                        if (view instanceof ViewGroup) {
                            ((ViewGroup) view).offsetDescendantRectToMyCoords(focus, this.mTempRect);
                        }
                        if (DEBUG_INPUT_RESIZE) {
                            String str5 = this.mTag;
                            Log.v(str5, "Focus in window: focusRect=" + this.mTempRect.toShortString() + " visRect=" + this.mVisRect.toShortString());
                        }
                    } else {
                        this.mTempRect.set(rectangle);
                        if (DEBUG_INPUT_RESIZE) {
                            String str6 = this.mTag;
                            Log.v(str6, "Request scroll to rect: " + this.mTempRect.toShortString() + " visRect=" + this.mVisRect.toShortString());
                        }
                    }
                    if (this.mTempRect.intersect(this.mVisRect)) {
                        if (DEBUG_INPUT_RESIZE) {
                            String str7 = this.mTag;
                            Log.v(str7, "Focus window visible rect: " + this.mTempRect.toShortString());
                        }
                        if (this.mTempRect.height() > (this.mView.getHeight() - vi.top) - vi.bottom) {
                            if (DEBUG_INPUT_RESIZE) {
                                String str8 = this.mTag;
                                Log.v(str8, "Too tall; leaving scrollY=" + scrollY2);
                            }
                        } else {
                            if (this.mTempRect.top < vi.top) {
                                scrollY = this.mTempRect.top - vi.top;
                                if (DEBUG_INPUT_RESIZE) {
                                    String str9 = this.mTag;
                                    Log.v(str9, "Top covered; scrollY=" + scrollY);
                                }
                            } else if (this.mTempRect.bottom > this.mView.getHeight() - vi.bottom) {
                                scrollY = this.mTempRect.bottom - (this.mView.getHeight() - vi.bottom);
                                if (DEBUG_INPUT_RESIZE) {
                                    String str10 = this.mTag;
                                    Log.v(str10, "Bottom covered; scrollY=" + scrollY);
                                }
                            } else {
                                scrollY2 = 0;
                            }
                            scrollY2 = scrollY;
                        }
                        handled = true;
                    }
                }
            }
        }
        if (scrollY2 != this.mScrollY) {
            if (DEBUG_INPUT_RESIZE) {
                String str11 = this.mTag;
                Log.v(str11, "Pan scroll changed: old=" + this.mScrollY + " , new=" + scrollY2);
            }
            if (!immediate) {
                if (this.mScroller == null) {
                    this.mScroller = new Scroller(this.mView.getContext());
                }
                Scroller scroller = this.mScroller;
                int i = this.mScrollY;
                scroller.startScroll(0, i, 0, scrollY2 - i);
            } else {
                Scroller scroller2 = this.mScroller;
                if (scroller2 != null) {
                    scroller2.abortAnimation();
                }
            }
            this.mScrollY = scrollY2;
        }
        return handled;
    }

    public View getAccessibilityFocusedHost() {
        return this.mAccessibilityFocusedHost;
    }

    public AccessibilityNodeInfo getAccessibilityFocusedVirtualView() {
        return this.mAccessibilityFocusedVirtualView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAccessibilityFocus(View view, AccessibilityNodeInfo node) {
        if (this.mAccessibilityFocusedVirtualView != null) {
            AccessibilityNodeInfo focusNode = this.mAccessibilityFocusedVirtualView;
            View focusHost = this.mAccessibilityFocusedHost;
            this.mAccessibilityFocusedHost = null;
            this.mAccessibilityFocusedVirtualView = null;
            focusHost.clearAccessibilityFocusNoCallbacks(64);
            AccessibilityNodeProvider provider = focusHost.getAccessibilityNodeProvider();
            if (provider != null) {
                focusNode.getBoundsInParent(this.mTempRect);
                focusHost.invalidate(this.mTempRect);
                int virtualNodeId = AccessibilityNodeInfo.getVirtualDescendantId(focusNode.getSourceNodeId());
                provider.performAction(virtualNodeId, 128, null);
            }
            focusNode.recycle();
        }
        View view2 = this.mAccessibilityFocusedHost;
        if (view2 != null && view2 != view) {
            view2.clearAccessibilityFocusNoCallbacks(64);
        }
        this.mAccessibilityFocusedHost = view;
        this.mAccessibilityFocusedVirtualView = node;
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.invalidateRoot();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasPointerCapture() {
        return this.mPointerCapture;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void requestPointerCapture(boolean enabled) {
        if (this.mPointerCapture == enabled) {
            return;
        }
        IBinder inputToken = getInputToken();
        if (inputToken == null) {
            Log.e(this.mTag, "No input channel to request Pointer Capture.");
        } else {
            InputManager.getInstance().requestPointerCapture(inputToken, enabled);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePointerCaptureChanged(boolean hasCapture) {
        if (this.mPointerCapture == hasCapture) {
            return;
        }
        this.mPointerCapture = hasCapture;
        View view = this.mView;
        if (view != null) {
            view.dispatchPointerCaptureChanged(hasCapture);
        }
    }

    private void updateColorModeIfNeeded(int colorMode) {
        if (this.mAttachInfo.mThreadedRenderer == null) {
            return;
        }
        if (!getConfiguration().isScreenWideColorGamut()) {
            colorMode = 0;
        }
        this.mAttachInfo.mThreadedRenderer.setColorMode(colorMode);
    }

    @Override // android.view.ViewParent
    public void requestChildFocus(View child, View focused) {
        if (DEBUG_INPUT_RESIZE) {
            String str = this.mTag;
            Log.v(str, "Request child focus: focus now " + focused);
        }
        checkThread();
        scheduleTraversals();
    }

    @Override // android.view.ViewParent
    public void clearChildFocus(View child) {
        if (DEBUG_INPUT_RESIZE) {
            Log.v(this.mTag, "Clearing child focus");
        }
        checkThread();
        scheduleTraversals();
    }

    @Override // android.view.ViewParent
    public ViewParent getParentForAccessibility() {
        return null;
    }

    @Override // android.view.ViewParent
    public void focusableViewAvailable(View v) {
        checkThread();
        View view = this.mView;
        if (view != null) {
            if (!view.hasFocus()) {
                if (sAlwaysAssignFocus || !this.mAttachInfo.mInTouchMode) {
                    v.requestFocus();
                    return;
                }
                return;
            }
            View focused = this.mView.findFocus();
            if (focused instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) focused;
                if (group.getDescendantFocusability() == 262144 && isViewDescendantOf(v, focused)) {
                    v.requestFocus();
                }
            }
        }
    }

    @Override // android.view.ViewParent
    public void recomputeViewAttributes(View child) {
        checkThread();
        if (this.mView == child) {
            this.mAttachInfo.mRecomputeGlobalAttributes = true;
            if (!this.mWillDrawSoon) {
                scheduleTraversals();
            }
        }
    }

    void dispatchDetachedFromWindow() {
        InputQueue inputQueue;
        this.mInsetsController.onWindowFocusLost();
        this.mFirstInputStage.onDetachedFromWindow();
        View view = this.mView;
        if (view != null && view.mAttachInfo != null) {
            this.mAttachInfo.mTreeObserver.dispatchOnWindowAttachedChange(false);
            this.mView.dispatchDetachedFromWindow();
        }
        this.mAccessibilityInteractionConnectionManager.ensureNoConnection();
        this.mAccessibilityManager.removeAccessibilityStateChangeListener(this.mAccessibilityInteractionConnectionManager);
        this.mAccessibilityManager.removeHighTextContrastStateChangeListener(this.mHighContrastTextManager);
        removeSendWindowContentChangedCallback();
        destroyHardwareRenderer();
        setAccessibilityFocus(null, null);
        this.mInsetsController.cancelExistingAnimations();
        this.mView.assignParent(null);
        this.mView = null;
        this.mAttachInfo.mRootView = null;
        destroySurface();
        InputQueue.Callback callback = this.mInputQueueCallback;
        if (callback != null && (inputQueue = this.mInputQueue) != null) {
            callback.onInputQueueDestroyed(inputQueue);
            this.mInputQueue.dispose();
            this.mInputQueueCallback = null;
            this.mInputQueue = null;
        }
        try {
            this.mWindowSession.remove(this.mWindow);
        } catch (RemoteException e) {
        }
        WindowInputEventReceiver windowInputEventReceiver = this.mInputEventReceiver;
        if (windowInputEventReceiver != null) {
            windowInputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
        unscheduleTraversals();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performConfigurationChange(MergedConfiguration mergedConfiguration, boolean force, int newDisplayId) {
        Configuration globalConfig;
        if (mergedConfiguration != null) {
            Configuration globalConfig2 = mergedConfiguration.getGlobalConfiguration();
            Configuration overrideConfig = mergedConfiguration.getOverrideConfiguration();
            if (DEBUG_CONFIGURATION) {
                Log.v(this.mTag, "Applying new config to window " + ((Object) this.mWindowAttributes.getTitle()) + ", globalConfig: " + globalConfig2 + ", overrideConfig: " + overrideConfig);
            }
            CompatibilityInfo ci = this.mDisplay.getDisplayAdjustments().getCompatibilityInfo();
            if (ci.equals(CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO)) {
                globalConfig = globalConfig2;
            } else {
                Configuration globalConfig3 = new Configuration(globalConfig2);
                ci.applyToConfiguration(this.mNoncompatDensity, globalConfig3);
                globalConfig = globalConfig3;
            }
            ArrayList<ConfigChangedCallback> arrayList = sConfigCallbacks;
            synchronized (arrayList) {
                try {
                    for (int i = arrayList.size() - 1; i >= 0; i--) {
                        sConfigCallbacks.get(i).onConfigurationChanged(globalConfig);
                    }
                } catch (Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            break;
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                    throw th;
                }
            }
            this.mLastReportedMergedConfiguration.setConfiguration(globalConfig, overrideConfig);
            this.mForceNextConfigUpdate = force;
            ActivityConfigCallback activityConfigCallback = this.mActivityConfigCallback;
            if (activityConfigCallback != null) {
                activityConfigCallback.onConfigurationChanged(overrideConfig, newDisplayId);
            } else {
                updateConfiguration(newDisplayId);
            }
            this.mForceNextConfigUpdate = false;
            int windowMode = overrideConfig.windowConfiguration.getWindowingMode();
            int rotation = overrideConfig.windowConfiguration.getRotation();
            int appWidth = overrideConfig.windowConfiguration.getAppBounds() != null ? overrideConfig.windowConfiguration.getAppBounds().width() : 0;
            if (appWidth != 0) {
                if (this.mLastRotation != rotation || this.mLastWindowMode != windowMode) {
                    this.mLastRotation = rotation;
                    if (this.mLastWindowMode != windowMode) {
                        this.mLastWindowMode = windowMode;
                        if (this.DEBUG_LOCAL_FOCUS_FOR_GAME) {
                            boolean isPinWindow = WindowConfiguration.isNtPinWindow(windowMode);
                            if (this.mAppIsGameMode == 0 && isPinWindow) {
                                int appHeight = overrideConfig.windowConfiguration.getAppBounds() != null ? overrideConfig.windowConfiguration.getAppBounds().height() : 0;
                                this.mAppIsGameMode = (appHeight == 0 || (((float) Math.max(appHeight, appWidth)) * 1.0f) / ((float) Math.min(appHeight, appWidth)) <= 2.0f) ? -1 : 1;
                            }
                            if (this.mAppIsGameMode == 1) {
                                if (isPinWindow) {
                                    this.mWindowAttributes.flags |= 268435456;
                                } else {
                                    this.mWindowAttributes.flags &= -268435457;
                                }
                            }
                            if (isPinWindow && this.mAppIsGameMode == 1 && !this.mUpcomingWindowFocus && this.mAppVisible) {
                                windowFocusChanged(false, false);
                                windowFocusChanged(true, false);
                            }
                        }
                    }
                    Log.i(TAG, "performConfigurationChange mLastWindowMode=" + this.mLastWindowMode + ",mLastRotation=" + this.mLastRotation + ",mAppIsGameMode=" + this.mAppIsGameMode);
                    if (WindowConfiguration.isNtWindowMode(this.mLastWindowMode)) {
                        try {
                            this.mWindowSession.getWindowModeTouchOffset(this.mWindow, this.mWindowModeOffset);
                            return;
                        } catch (RemoteException e) {
                            Slog.e(this.mTag, "Unable get Window Mode Touch Offset");
                            return;
                        }
                    }
                    float[] fArr = this.mWindowModeOffset;
                    fArr[1] = 0.0f;
                    fArr[0] = 0.0f;
                    fArr[3] = 1.0f;
                    fArr[2] = 1.0f;
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("No merged config provided.");
    }

    public void updateConfiguration(int newDisplayId) {
        View view = this.mView;
        if (view == null) {
            return;
        }
        Resources localResources = view.getResources();
        Configuration config = localResources.getConfiguration();
        if (newDisplayId != -1) {
            onMovedToDisplay(newDisplayId, config);
        }
        if (this.mForceNextConfigUpdate || this.mLastConfigurationFromResources.diff(config) != 0) {
            updateInternalDisplay(this.mDisplay.getDisplayId(), localResources);
            int lastLayoutDirection = this.mLastConfigurationFromResources.getLayoutDirection();
            int currentLayoutDirection = config.getLayoutDirection();
            this.mLastConfigurationFromResources.setTo(config);
            if (lastLayoutDirection != currentLayoutDirection && this.mViewLayoutDirectionInitial == 2) {
                this.mView.setLayoutDirection(currentLayoutDirection);
            }
            this.mView.dispatchConfigurationChanged(config);
            this.mForceNextWindowRelayout = true;
            requestLayout();
        }
        updateForceDarkMode();
    }

    public static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        return (theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent);
    }

    private static void forceLayout(View view) {
        view.forceLayout();
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                forceLayout(group.getChildAt(i));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class ViewRootHandler extends Handler {
        ViewRootHandler() {
        }

        @Override // android.os.Handler
        public String getMessageName(Message message) {
            switch (message.what) {
                case 1:
                    return "MSG_INVALIDATE";
                case 2:
                    return "MSG_INVALIDATE_RECT";
                case 3:
                    return "MSG_DIE";
                case 4:
                    return "MSG_RESIZED";
                case 5:
                    return "MSG_RESIZED_REPORT";
                case 6:
                    return "MSG_WINDOW_FOCUS_CHANGED";
                case 7:
                    return "MSG_DISPATCH_INPUT_EVENT";
                case 8:
                    return "MSG_DISPATCH_APP_VISIBILITY";
                case 9:
                    return "MSG_DISPATCH_GET_NEW_SURFACE";
                case 10:
                case 20:
                case 22:
                case 26:
                default:
                    return super.getMessageName(message);
                case 11:
                    return "MSG_DISPATCH_KEY_FROM_IME";
                case 12:
                    return "MSG_DISPATCH_KEY_FROM_AUTOFILL";
                case 13:
                    return "MSG_CHECK_FOCUS";
                case 14:
                    return "MSG_CLOSE_SYSTEM_DIALOGS";
                case 15:
                    return "MSG_DISPATCH_DRAG_EVENT";
                case 16:
                    return "MSG_DISPATCH_DRAG_LOCATION_EVENT";
                case 17:
                    return "MSG_DISPATCH_SYSTEM_UI_VISIBILITY";
                case 18:
                    return "MSG_UPDATE_CONFIGURATION";
                case 19:
                    return "MSG_PROCESS_INPUT_EVENTS";
                case 21:
                    return "MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST";
                case 23:
                    return "MSG_WINDOW_MOVED";
                case 24:
                    return "MSG_SYNTHESIZE_INPUT_EVENT";
                case 25:
                    return "MSG_DISPATCH_WINDOW_SHOWN";
                case 27:
                    return "MSG_UPDATE_POINTER_ICON";
                case 28:
                    return "MSG_POINTER_CAPTURE_CHANGED";
                case 29:
                    return "MSG_DRAW_FINISHED";
                case 30:
                    return "MSG_INSETS_CHANGED";
                case 31:
                    return "MSG_INSETS_CONTROL_CHANGED";
                case 32:
                    return "MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED";
                case 33:
                    return "MSG_LOCATION_IN_PARENT_DISPLAY_CHANGED";
                case 34:
                    return "MSG_SHOW_INSETS";
                case 35:
                    return "MSG_HIDE_INSETS";
            }
        }

        @Override // android.os.Handler
        public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
            if (msg.what == 26 && msg.obj == null) {
                throw new NullPointerException("Attempted to call MSG_REQUEST_KEYBOARD_SHORTCUTS with null receiver:");
            }
            return super.sendMessageAtTime(msg, uptimeMillis);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (Trace.isTagEnabled(8L)) {
                Trace.traceBegin(8L, getMessageName(msg));
            }
            try {
                handleMessageImpl(msg);
            } finally {
                Trace.traceEnd(8L);
            }
        }

        private void handleMessageImpl(Message msg) {
            boolean z = false;
            boolean z2 = false;
            boolean z3 = false;
            boolean z4 = false;
            boolean z5 = false;
            boolean z6 = true;
            switch (msg.what) {
                case 1:
                    ((View) msg.obj).invalidate();
                    return;
                case 2:
                    View.AttachInfo.InvalidateInfo info = (View.AttachInfo.InvalidateInfo) msg.obj;
                    info.target.invalidate(info.left, info.top, info.right, info.bottom);
                    info.recycle();
                    return;
                case 3:
                    ViewRootImpl.this.doDie();
                    return;
                case 4:
                case 5:
                    ViewRootImpl.this.mWillMove = false;
                    ViewRootImpl.this.mWillResize = false;
                    SomeArgs args = (SomeArgs) msg.obj;
                    ViewRootImpl.this.handleResized(msg.what, args);
                    args.recycle();
                    return;
                case 6:
                    ViewRootImpl.this.handleWindowFocusChanged();
                    return;
                case 7:
                    SomeArgs args2 = (SomeArgs) msg.obj;
                    InputEventReceiver receiver = (InputEventReceiver) args2.arg2;
                    ViewRootImpl.this.enqueueInputEvent((InputEvent) args2.arg1, receiver, 0, true);
                    args2.recycle();
                    return;
                case 8:
                    ViewRootImpl viewRootImpl = ViewRootImpl.this;
                    if (msg.arg1 != 0) {
                        z = true;
                    }
                    viewRootImpl.handleAppVisibility(z);
                    return;
                case 9:
                    ViewRootImpl.this.handleGetNewSurface();
                    return;
                case 10:
                case 20:
                default:
                    return;
                case 11:
                    if (ViewRootImpl.LOCAL_LOGV) {
                        Log.v(ViewRootImpl.TAG, "Dispatching key " + msg.obj + " from IME to " + ViewRootImpl.this.mView);
                    }
                    KeyEvent event = (KeyEvent) msg.obj;
                    if ((event.getFlags() & 8) != 0) {
                        event = KeyEvent.changeFlags(event, event.getFlags() & (-9));
                    }
                    ViewRootImpl.this.enqueueInputEvent(event, null, 1, true);
                    return;
                case 12:
                    if (ViewRootImpl.LOCAL_LOGV) {
                        Log.v(ViewRootImpl.TAG, "Dispatching key " + msg.obj + " from Autofill to " + ViewRootImpl.this.mView);
                    }
                    ViewRootImpl.this.enqueueInputEvent((KeyEvent) msg.obj, null, 0, true);
                    return;
                case 13:
                    ViewRootImpl.this.getImeFocusController().checkFocus(false, true);
                    return;
                case 14:
                    if (ViewRootImpl.this.mView != null) {
                        ViewRootImpl.this.mView.onCloseSystemDialogs((String) msg.obj);
                        return;
                    }
                    return;
                case 15:
                case 16:
                    DragEvent event2 = (DragEvent) msg.obj;
                    event2.mLocalState = ViewRootImpl.this.mLocalDragState;
                    ViewRootImpl.this.handleDragEvent(event2);
                    return;
                case 17:
                    ViewRootImpl.this.handleDispatchSystemUiVisibilityChanged((SystemUiVisibilityInfo) msg.obj);
                    return;
                case 18:
                    Configuration config = (Configuration) msg.obj;
                    if (config.isOtherSeqNewer(ViewRootImpl.this.mLastReportedMergedConfiguration.getMergedConfiguration())) {
                        config = ViewRootImpl.this.mLastReportedMergedConfiguration.getGlobalConfiguration();
                    }
                    ViewRootImpl.this.mPendingMergedConfiguration.setConfiguration(config, ViewRootImpl.this.mLastReportedMergedConfiguration.getOverrideConfiguration());
                    ViewRootImpl.this.performConfigurationChange(new MergedConfiguration(ViewRootImpl.this.mPendingMergedConfiguration), false, -1);
                    return;
                case 19:
                    ViewRootImpl.this.mProcessInputEventsScheduled = false;
                    ViewRootImpl.this.doProcessInputEvents();
                    return;
                case 21:
                    ViewRootImpl.this.setAccessibilityFocus(null, null);
                    return;
                case 22:
                    if (ViewRootImpl.this.mView != null) {
                        ViewRootImpl viewRootImpl2 = ViewRootImpl.this;
                        viewRootImpl2.invalidateWorld(viewRootImpl2.mView);
                        return;
                    }
                    return;
                case 23:
                    ViewRootImpl.this.mWillMove = false;
                    if (ViewRootImpl.this.mAdded) {
                        int w = ViewRootImpl.this.mWinFrame.width();
                        int h = ViewRootImpl.this.mWinFrame.height();
                        int l = msg.arg1;
                        int t = msg.arg2;
                        ViewRootImpl.this.mTmpFrames.frame.left = l;
                        ViewRootImpl.this.mTmpFrames.frame.right = l + w;
                        ViewRootImpl.this.mTmpFrames.frame.top = t;
                        ViewRootImpl.this.mTmpFrames.frame.bottom = t + h;
                        ViewRootImpl viewRootImpl3 = ViewRootImpl.this;
                        viewRootImpl3.setFrame(viewRootImpl3.mTmpFrames.frame);
                        ViewRootImpl.this.mPendingBackDropFrame.set(ViewRootImpl.this.mWinFrame);
                        ViewRootImpl viewRootImpl4 = ViewRootImpl.this;
                        viewRootImpl4.maybeHandleWindowMove(viewRootImpl4.mWinFrame);
                        return;
                    }
                    return;
                case 24:
                    ViewRootImpl.this.enqueueInputEvent((InputEvent) msg.obj, null, 32, true);
                    return;
                case 25:
                    ViewRootImpl.this.handleDispatchWindowShown();
                    return;
                case 26:
                    IResultReceiver receiver2 = (IResultReceiver) msg.obj;
                    int deviceId = msg.arg1;
                    ViewRootImpl.this.handleRequestKeyboardShortcuts(receiver2, deviceId);
                    return;
                case 27:
                    ViewRootImpl.this.resetPointerIcon((MotionEvent) msg.obj);
                    return;
                case 28:
                    if (msg.arg1 != 0) {
                        z5 = true;
                    }
                    boolean hasCapture = z5;
                    ViewRootImpl.this.handlePointerCaptureChanged(hasCapture);
                    return;
                case 29:
                    ViewRootImpl.this.pendingDrawFinished();
                    return;
                case 30:
                    SomeArgs args3 = (SomeArgs) msg.obj;
                    ViewRootImpl.this.mWillMove = args3.argi1 == 1;
                    ViewRootImpl viewRootImpl5 = ViewRootImpl.this;
                    if (args3.argi2 == 1) {
                        z4 = true;
                    }
                    viewRootImpl5.mWillResize = z4;
                    ViewRootImpl.this.mInsetsController.onStateChanged((InsetsState) args3.arg1);
                    args3.recycle();
                    return;
                case 31:
                    SomeArgs args4 = (SomeArgs) msg.obj;
                    ViewRootImpl.this.mWillMove = args4.argi1 == 1;
                    ViewRootImpl viewRootImpl6 = ViewRootImpl.this;
                    if (args4.argi2 != 1) {
                        z6 = false;
                    }
                    viewRootImpl6.mWillResize = z6;
                    ViewRootImpl.this.mInsetsController.onStateChanged((InsetsState) args4.arg1);
                    InsetsSourceControl[] controls = (InsetsSourceControl[]) args4.arg2;
                    if (ViewRootImpl.this.mAdded) {
                        ViewRootImpl.this.mInsetsController.onControlsChanged(controls);
                    } else if (controls != null) {
                        for (InsetsSourceControl control : controls) {
                            if (control != null) {
                                control.release(InsetsAnimationThreadControlRunner$$ExternalSyntheticLambda2.INSTANCE);
                            }
                        }
                    }
                    args4.recycle();
                    return;
                case 32:
                    ViewRootImpl.this.systemGestureExclusionChanged();
                    return;
                case 33:
                    ViewRootImpl.this.updateLocationInParentDisplay(msg.arg1, msg.arg2);
                    return;
                case 34:
                    if (ViewRootImpl.this.mView == null) {
                        Object[] objArr = new Object[2];
                        objArr[0] = Integer.valueOf(msg.arg1);
                        objArr[1] = Boolean.valueOf(msg.arg2 == 1);
                        Log.e(ViewRootImpl.TAG, String.format("Calling showInsets(%d,%b) on window that no longer has views.", objArr));
                    }
                    ViewRootImpl.this.clearLowProfileModeIfNeeded(msg.arg1, msg.arg2 == 1);
                    InsetsController insetsController = ViewRootImpl.this.mInsetsController;
                    int i = msg.arg1;
                    if (msg.arg2 == 1) {
                        z3 = true;
                    }
                    insetsController.show(i, z3);
                    return;
                case 35:
                    InsetsController insetsController2 = ViewRootImpl.this.mInsetsController;
                    int i2 = msg.arg1;
                    if (msg.arg2 == 1) {
                        z2 = true;
                    }
                    insetsController2.hide(i2, z2);
                    return;
                case 36:
                    ViewRootImpl.this.handleScrollCaptureRequest((IScrollCaptureResponseListener) msg.obj);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean ensureTouchMode(boolean inTouchMode) {
        if (DBG) {
            Log.d("touchmode", "ensureTouchMode(" + inTouchMode + "), current touch mode is " + this.mAttachInfo.mInTouchMode);
        }
        if (this.mAttachInfo.mInTouchMode == inTouchMode) {
            return false;
        }
        try {
            this.mWindowSession.setInTouchMode(inTouchMode);
            return ensureTouchModeLocally(inTouchMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean ensureTouchModeLocally(boolean inTouchMode) {
        if (DBG) {
            Log.d("touchmode", "ensureTouchModeLocally(" + inTouchMode + "), current touch mode is " + this.mAttachInfo.mInTouchMode);
        }
        if (this.mAttachInfo.mInTouchMode == inTouchMode) {
            return false;
        }
        this.mAttachInfo.mInTouchMode = inTouchMode;
        this.mAttachInfo.mTreeObserver.dispatchOnTouchModeChanged(inTouchMode);
        return inTouchMode ? enterTouchMode() : leaveTouchMode();
    }

    private boolean enterTouchMode() {
        View focused;
        View view = this.mView;
        if (view == null || !view.hasFocus() || (focused = this.mView.findFocus()) == null || focused.isFocusableInTouchMode()) {
            return false;
        }
        ViewGroup ancestorToTakeFocus = findAncestorToTakeFocusInTouchMode(focused);
        if (ancestorToTakeFocus != null) {
            return ancestorToTakeFocus.requestFocus();
        }
        focused.clearFocusInternal(null, true, false);
        return true;
    }

    private static ViewGroup findAncestorToTakeFocusInTouchMode(View focused) {
        ViewParent parent = focused.getParent();
        while (parent instanceof ViewGroup) {
            ViewGroup vgParent = (ViewGroup) parent;
            if (vgParent.getDescendantFocusability() == 262144 && vgParent.isFocusableInTouchMode()) {
                return vgParent;
            }
            if (vgParent.isRootNamespace()) {
                return null;
            }
            parent = vgParent.getParent();
        }
        return null;
    }

    private boolean leaveTouchMode() {
        View view = this.mView;
        if (view != null) {
            if (view.hasFocus()) {
                View focusedView = this.mView.findFocus();
                if (!(focusedView instanceof ViewGroup) || ((ViewGroup) focusedView).getDescendantFocusability() != 262144) {
                    return false;
                }
            }
            return this.mView.restoreDefaultFocus();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public abstract class InputStage {
        protected static final int FINISH_HANDLED = 1;
        protected static final int FINISH_NOT_HANDLED = 2;
        protected static final int FORWARD = 0;
        private final InputStage mNext;
        private String mTracePrefix;

        public InputStage(InputStage next) {
            this.mNext = next;
        }

        public final void deliver(QueuedInputEvent q) {
            if ((q.mFlags & 4) != 0) {
                forward(q);
            } else if (shouldDropInputEvent(q)) {
                finish(q, false);
            } else {
                traceEvent(q, 8L);
                try {
                    int result = onProcess(q);
                    Trace.traceEnd(8L);
                    apply(q, result);
                } catch (Throwable th) {
                    Trace.traceEnd(8L);
                    throw th;
                }
            }
        }

        protected void finish(QueuedInputEvent q, boolean handled) {
            q.mFlags |= 4;
            if (handled) {
                q.mFlags |= 8;
            }
            forward(q);
        }

        protected void forward(QueuedInputEvent q) {
            onDeliverToNext(q);
        }

        protected void apply(QueuedInputEvent q, int result) {
            if (result == 0) {
                forward(q);
            } else if (result == 1) {
                finish(q, true);
            } else if (result == 2) {
                finish(q, false);
            } else {
                throw new IllegalArgumentException("Invalid result: " + result);
            }
        }

        protected int onProcess(QueuedInputEvent q) {
            return 0;
        }

        protected void onDeliverToNext(QueuedInputEvent q) {
            if (ViewRootImpl.DEBUG_INPUT_STAGES) {
                String str = ViewRootImpl.this.mTag;
                Log.v(str, "Done with " + getClass().getSimpleName() + ". " + q);
            }
            InputStage inputStage = this.mNext;
            if (inputStage == null) {
                ViewRootImpl.this.finishInputEvent(q);
            } else {
                inputStage.deliver(q);
            }
        }

        protected void onWindowFocusChanged(boolean hasWindowFocus) {
            InputStage inputStage = this.mNext;
            if (inputStage != null) {
                inputStage.onWindowFocusChanged(hasWindowFocus);
            }
        }

        protected void onDetachedFromWindow() {
            InputStage inputStage = this.mNext;
            if (inputStage != null) {
                inputStage.onDetachedFromWindow();
            }
        }

        protected boolean shouldDropInputEvent(QueuedInputEvent q) {
            String reason;
            if (ViewRootImpl.this.mView == null || !ViewRootImpl.this.mAdded) {
                String str = ViewRootImpl.this.mTag;
                Slog.w(str, "Dropping event due to root view being removed: " + q.mEvent);
                return true;
            }
            if (!ViewRootImpl.this.mAttachInfo.mHasWindowFocus && !q.mEvent.isFromSource(2) && !ViewRootImpl.this.isAutofillUiShowing()) {
                reason = "no window focus";
            } else if (ViewRootImpl.this.mStopped) {
                reason = "window is stopped";
            } else if (ViewRootImpl.this.mIsAmbientMode && !q.mEvent.isFromSource(1)) {
                reason = "non-button event in ambient mode";
            } else if (!ViewRootImpl.this.mPausedForTransition || isBack(q.mEvent)) {
                return false;
            } else {
                reason = "paused for transition";
            }
            if (!ViewRootImpl.isTerminalInputEvent(q.mEvent)) {
                String str2 = ViewRootImpl.this.mTag;
                Slog.w(str2, "Dropping event (" + reason + "):" + q.mEvent);
                return true;
            }
            q.mEvent.cancel();
            String str3 = ViewRootImpl.this.mTag;
            Slog.w(str3, "Cancelling event (" + reason + "):" + q.mEvent);
            return false;
        }

        void dump(String prefix, PrintWriter writer) {
            InputStage inputStage = this.mNext;
            if (inputStage != null) {
                inputStage.dump(prefix, writer);
            }
        }

        private boolean isBack(InputEvent event) {
            return (event instanceof KeyEvent) && ((KeyEvent) event).getKeyCode() == 4;
        }

        private void traceEvent(QueuedInputEvent q, long traceTag) {
            if (!Trace.isTagEnabled(traceTag)) {
                return;
            }
            if (this.mTracePrefix == null) {
                this.mTracePrefix = getClass().getSimpleName();
            }
            Trace.traceBegin(traceTag, this.mTracePrefix + " id=0x" + Integer.toHexString(q.mEvent.getId()));
        }
    }

    /* loaded from: classes3.dex */
    abstract class AsyncInputStage extends InputStage {
        protected static final int DEFER = 3;
        private QueuedInputEvent mQueueHead;
        private int mQueueLength;
        private QueuedInputEvent mQueueTail;
        private final String mTraceCounter;

        public AsyncInputStage(InputStage next, String traceCounter) {
            super(next);
            this.mTraceCounter = traceCounter;
        }

        protected void defer(QueuedInputEvent q) {
            q.mFlags |= 2;
            enqueue(q);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected void forward(QueuedInputEvent q) {
            q.mFlags &= -3;
            QueuedInputEvent curr = this.mQueueHead;
            if (curr == null) {
                super.forward(q);
                return;
            }
            int deviceId = q.mEvent.getDeviceId();
            QueuedInputEvent prev = null;
            boolean blocked = false;
            while (curr != null && curr != q) {
                if (!blocked && deviceId == curr.mEvent.getDeviceId()) {
                    blocked = true;
                }
                prev = curr;
                curr = curr.mNext;
            }
            if (blocked) {
                if (curr == null) {
                    enqueue(q);
                    return;
                }
                return;
            }
            if (curr != null) {
                curr = curr.mNext;
                dequeue(q, prev);
            }
            super.forward(q);
            while (curr != null) {
                if (deviceId == curr.mEvent.getDeviceId()) {
                    if ((curr.mFlags & 2) == 0) {
                        QueuedInputEvent next = curr.mNext;
                        dequeue(curr, prev);
                        super.forward(curr);
                        curr = next;
                    } else {
                        return;
                    }
                } else {
                    prev = curr;
                    curr = curr.mNext;
                }
            }
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected void apply(QueuedInputEvent q, int result) {
            if (result == 3) {
                defer(q);
            } else {
                super.apply(q, result);
            }
        }

        private void enqueue(QueuedInputEvent q) {
            QueuedInputEvent queuedInputEvent = this.mQueueTail;
            if (queuedInputEvent == null) {
                this.mQueueHead = q;
                this.mQueueTail = q;
            } else {
                queuedInputEvent.mNext = q;
                this.mQueueTail = q;
            }
            int i = this.mQueueLength + 1;
            this.mQueueLength = i;
            Trace.traceCounter(4L, this.mTraceCounter, i);
        }

        private void dequeue(QueuedInputEvent q, QueuedInputEvent prev) {
            if (prev == null) {
                this.mQueueHead = q.mNext;
            } else {
                prev.mNext = q.mNext;
            }
            if (this.mQueueTail == q) {
                this.mQueueTail = prev;
            }
            q.mNext = null;
            int i = this.mQueueLength - 1;
            this.mQueueLength = i;
            Trace.traceCounter(4L, this.mTraceCounter, i);
        }

        @Override // android.view.ViewRootImpl.InputStage
        void dump(String prefix, PrintWriter writer) {
            writer.print(prefix);
            writer.print(getClass().getName());
            writer.print(": mQueueLength=");
            writer.println(this.mQueueLength);
            super.dump(prefix, writer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class NativePreImeInputStage extends AsyncInputStage implements InputQueue.FinishedInputEventCallback {
        public NativePreImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected int onProcess(QueuedInputEvent q) {
            if (ViewRootImpl.this.mInputQueue != null && (q.mEvent instanceof KeyEvent)) {
                ViewRootImpl.this.mInputQueue.sendInputEvent(q.mEvent, q, true, this);
                return 3;
            }
            return 0;
        }

        @Override // android.view.InputQueue.FinishedInputEventCallback
        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
            } else {
                forward(q);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class ViewPreImeInputStage extends InputStage {
        public ViewPreImeInputStage(InputStage next) {
            super(next);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            return 0;
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = (KeyEvent) q.mEvent;
            if (ViewRootImpl.this.mView.dispatchKeyEventPreIme(event)) {
                return 1;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class ImeInputStage extends AsyncInputStage implements InputMethodManager.FinishedInputEventCallback {
        public ImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected int onProcess(QueuedInputEvent q) {
            int result = ViewRootImpl.this.mImeFocusController.onProcessImeInputStage(q, q.mEvent, ViewRootImpl.this.mWindowAttributes, this);
            switch (result) {
                case -1:
                    return 3;
                case 0:
                    return 0;
                case 1:
                    return 1;
                default:
                    throw new IllegalStateException("Unexpected result=" + result);
            }
        }

        @Override // android.view.inputmethod.InputMethodManager.FinishedInputEventCallback
        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
            } else {
                forward(q);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class EarlyPostImeInputStage extends InputStage {
        public EarlyPostImeInputStage(InputStage next) {
            super(next);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            if (q.mEvent instanceof MotionEvent) {
                return processMotionEvent(q);
            }
            return 0;
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = (KeyEvent) q.mEvent;
            if (ViewRootImpl.this.mAttachInfo.mTooltipHost != null) {
                ViewRootImpl.this.mAttachInfo.mTooltipHost.handleTooltipKey(event);
            }
            if (ViewRootImpl.this.checkForLeavingTouchModeAndConsume(event)) {
                return 1;
            }
            ViewRootImpl.this.mFallbackEventHandler.preDispatchKeyEvent(event);
            return 0;
        }

        private int processMotionEvent(QueuedInputEvent q) {
            MotionEvent event = (MotionEvent) q.mEvent;
            if (event.isFromSource(2)) {
                return processPointerEvent(q);
            }
            int action = event.getActionMasked();
            if ((action == 0 || action == 8) && event.isFromSource(8)) {
                ViewRootImpl.this.ensureTouchMode(false);
            }
            return 0;
        }

        private int processPointerEvent(QueuedInputEvent q) {
            AutofillManager afm;
            MotionEvent event = (MotionEvent) q.mEvent;
            if (ViewRootImpl.this.mTranslator != null) {
                ViewRootImpl.this.mTranslator.translateEventInScreenToAppWindow(event);
            }
            int action = event.getAction();
            if (action == 0 || action == 8) {
                ViewRootImpl.this.ensureTouchMode(true);
            }
            if (action == 0 && (afm = ViewRootImpl.this.getAutofillManager()) != null) {
                afm.requestHideFillUi();
            }
            if (action == 0 && ViewRootImpl.this.mAttachInfo.mTooltipHost != null) {
                ViewRootImpl.this.mAttachInfo.mTooltipHost.hideTooltip();
            }
            if (ViewRootImpl.this.mCurScrollY != 0) {
                event.offsetLocation(0.0f, ViewRootImpl.this.mCurScrollY);
            }
            if (event.isTouchEvent()) {
                ViewRootImpl.this.mLastTouchPoint.x = event.getRawX();
                ViewRootImpl.this.mLastTouchPoint.y = event.getRawY();
                ViewRootImpl.this.mLastTouchSource = event.getSource();
                return 0;
            }
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class NativePostImeInputStage extends AsyncInputStage implements InputQueue.FinishedInputEventCallback {
        public NativePostImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected int onProcess(QueuedInputEvent q) {
            if (ViewRootImpl.this.mInputQueue != null) {
                ViewRootImpl.this.mInputQueue.sendInputEvent(q.mEvent, q, false, this);
                return 3;
            }
            return 0;
        }

        @Override // android.view.InputQueue.FinishedInputEventCallback
        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
            } else {
                forward(q);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class ViewPostImeInputStage extends InputStage {
        public ViewPostImeInputStage(InputStage next) {
            super(next);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            int source = q.mEvent.getSource();
            if ((source & 2) != 0) {
                return processPointerEvent(q);
            }
            if ((source & 4) != 0) {
                return processTrackballEvent(q);
            }
            return processGenericMotionEvent(q);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected void onDeliverToNext(QueuedInputEvent q) {
            if (ViewRootImpl.this.mUnbufferedInputDispatch && (q.mEvent instanceof MotionEvent) && ((MotionEvent) q.mEvent).isTouchEvent() && ViewRootImpl.isTerminalInputEvent(q.mEvent)) {
                ViewRootImpl.this.mUnbufferedInputDispatch = false;
                ViewRootImpl.this.scheduleConsumeBatchedInput();
            }
            super.onDeliverToNext(q);
        }

        private boolean performFocusNavigation(KeyEvent event) {
            int direction = 0;
            switch (event.getKeyCode()) {
                case 19:
                    if (event.hasNoModifiers()) {
                        direction = 33;
                        break;
                    }
                    break;
                case 20:
                    if (event.hasNoModifiers()) {
                        direction = 130;
                        break;
                    }
                    break;
                case 21:
                    if (event.hasNoModifiers()) {
                        direction = 17;
                        break;
                    }
                    break;
                case 22:
                    if (event.hasNoModifiers()) {
                        direction = 66;
                        break;
                    }
                    break;
                case 61:
                    if (event.hasNoModifiers()) {
                        direction = 2;
                        break;
                    } else if (event.hasModifiers(1)) {
                        direction = 1;
                        break;
                    }
                    break;
            }
            boolean isFastScrolling = false;
            if (direction != 0) {
                View focused = ViewRootImpl.this.mView.findFocus();
                if (focused != null) {
                    View v = focused.focusSearch(direction);
                    if (v != null && v != focused) {
                        focused.getFocusedRect(ViewRootImpl.this.mTempRect);
                        if (ViewRootImpl.this.mView instanceof ViewGroup) {
                            ((ViewGroup) ViewRootImpl.this.mView).offsetDescendantRectToMyCoords(focused, ViewRootImpl.this.mTempRect);
                            ((ViewGroup) ViewRootImpl.this.mView).offsetRectIntoDescendantCoords(v, ViewRootImpl.this.mTempRect);
                        }
                        if (v.requestFocus(direction, ViewRootImpl.this.mTempRect)) {
                            if (event.getRepeatCount() > 0) {
                                isFastScrolling = true;
                            }
                            ViewRootImpl.this.playSoundEffect(SoundEffectConstants.getConstantForFocusDirection(direction, isFastScrolling));
                            return true;
                        }
                    }
                    if (ViewRootImpl.this.mView.dispatchUnhandledMove(focused, direction)) {
                        return true;
                    }
                } else if (ViewRootImpl.this.mView.restoreDefaultFocus()) {
                    return true;
                }
            }
            return false;
        }

        private boolean performKeyboardGroupNavigation(int direction) {
            View cluster;
            View focused = ViewRootImpl.this.mView.findFocus();
            if (focused == null && ViewRootImpl.this.mView.restoreDefaultFocus()) {
                return true;
            }
            if (focused == null) {
                cluster = ViewRootImpl.this.keyboardNavigationClusterSearch(null, direction);
            } else {
                cluster = focused.keyboardNavigationClusterSearch(null, direction);
            }
            int realDirection = direction;
            if (direction == 2 || direction == 1) {
                realDirection = 130;
            }
            if (cluster != null && cluster.isRootNamespace()) {
                if (!cluster.restoreFocusNotInCluster()) {
                    cluster = ViewRootImpl.this.keyboardNavigationClusterSearch(null, direction);
                } else {
                    ViewRootImpl.this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
                    return true;
                }
            }
            if (cluster != null && cluster.restoreFocusInCluster(realDirection)) {
                ViewRootImpl.this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
                return true;
            }
            return false;
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = (KeyEvent) q.mEvent;
            if (!ViewRootImpl.this.mUnhandledKeyManager.preViewDispatch(event) && !ViewRootImpl.this.mView.dispatchKeyEvent(event)) {
                if (shouldDropInputEvent(q)) {
                    return 2;
                }
                if (ViewRootImpl.this.mUnhandledKeyManager.dispatch(ViewRootImpl.this.mView, event)) {
                    return 1;
                }
                int groupNavigationDirection = 0;
                if (event.getAction() == 0 && event.getKeyCode() == 61) {
                    if (KeyEvent.metaStateHasModifiers(event.getMetaState(), 65536)) {
                        groupNavigationDirection = 2;
                    } else if (KeyEvent.metaStateHasModifiers(event.getMetaState(), 65537)) {
                        groupNavigationDirection = 1;
                    }
                }
                if (event.getAction() == 0 && !KeyEvent.metaStateHasNoModifiers(event.getMetaState()) && event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(event.getKeyCode()) && groupNavigationDirection == 0) {
                    if (ViewRootImpl.this.mView.dispatchKeyShortcutEvent(event)) {
                        return 1;
                    }
                    if (shouldDropInputEvent(q)) {
                        return 2;
                    }
                }
                if (ViewRootImpl.this.mFallbackEventHandler.dispatchKeyEvent(event)) {
                    return 1;
                }
                if (shouldDropInputEvent(q)) {
                    return 2;
                }
                if (event.getAction() != 0) {
                    return 0;
                }
                return groupNavigationDirection != 0 ? performKeyboardGroupNavigation(groupNavigationDirection) ? 1 : 0 : performFocusNavigation(event) ? 1 : 0;
            }
            return 1;
        }

        private int processPointerEvent(QueuedInputEvent q) {
            MotionEvent event = (MotionEvent) q.mEvent;
            ViewRootImpl.this.mAttachInfo.mUnbufferedDispatchRequested = false;
            ViewRootImpl.this.mAttachInfo.mHandlingPointerEvent = true;
            boolean dispatchPointerEvent = ViewRootImpl.this.mView.dispatchPointerEvent(event);
            int action = event.getActionMasked();
            if (action == 2) {
                ViewRootImpl.this.mHaveMoveEvent = true;
            } else if (action == 1) {
                ViewRootImpl.this.mHaveMoveEvent = false;
            }
            maybeUpdatePointerIcon(event);
            ViewRootImpl.this.maybeUpdateTooltip(event);
            ViewRootImpl.this.mAttachInfo.mHandlingPointerEvent = false;
            if (ViewRootImpl.this.mAttachInfo.mUnbufferedDispatchRequested && !ViewRootImpl.this.mUnbufferedInputDispatch) {
                ViewRootImpl.this.mUnbufferedInputDispatch = true;
                if (ViewRootImpl.this.mConsumeBatchedInputScheduled) {
                    ViewRootImpl.this.scheduleConsumeBatchedInputImmediately();
                }
            }
            return dispatchPointerEvent ? 1 : 0;
        }

        private void maybeUpdatePointerIcon(MotionEvent event) {
            if (event.getPointerCount() == 1 && event.isFromSource(8194)) {
                if (event.getActionMasked() == 9 || event.getActionMasked() == 10) {
                    ViewRootImpl.this.mPointerIconType = 1;
                }
                if (event.getActionMasked() != 10 && !ViewRootImpl.this.updatePointerIcon(event) && event.getActionMasked() == 7) {
                    ViewRootImpl.this.mPointerIconType = 1;
                }
            }
        }

        private int processTrackballEvent(QueuedInputEvent q) {
            MotionEvent event = (MotionEvent) q.mEvent;
            return ((!event.isFromSource(InputDevice.SOURCE_MOUSE_RELATIVE) || (ViewRootImpl.this.hasPointerCapture() && !ViewRootImpl.this.mView.dispatchCapturedPointerEvent(event))) && !ViewRootImpl.this.mView.dispatchTrackballEvent(event)) ? 0 : 1;
        }

        private int processGenericMotionEvent(QueuedInputEvent q) {
            MotionEvent event = (MotionEvent) q.mEvent;
            return ((!event.isFromSource(InputDevice.SOURCE_TOUCHPAD) || !ViewRootImpl.this.hasPointerCapture() || !ViewRootImpl.this.mView.dispatchCapturedPointerEvent(event)) && !ViewRootImpl.this.mView.dispatchGenericMotionEvent(event)) ? 0 : 1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetPointerIcon(MotionEvent event) {
        this.mPointerIconType = 1;
        updatePointerIcon(event);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean updatePointerIcon(MotionEvent event) {
        float x = event.getX(0);
        float y = event.getY(0);
        View view = this.mView;
        if (view == null) {
            Slog.d(this.mTag, "updatePointerIcon called after view was removed");
            return false;
        } else if (x >= 0.0f && x < view.getWidth() && y >= 0.0f && y < this.mView.getHeight()) {
            PointerIcon pointerIcon = this.mView.onResolvePointerIcon(event, 0);
            int pointerType = pointerIcon != null ? pointerIcon.getType() : 1000;
            if (this.mPointerIconType != pointerType) {
                this.mPointerIconType = pointerType;
                this.mCustomPointerIcon = null;
                if (pointerType != -1) {
                    InputManager.getInstance().setPointerIconType(pointerType);
                    return true;
                }
            }
            if (this.mPointerIconType == -1 && !pointerIcon.equals(this.mCustomPointerIcon)) {
                this.mCustomPointerIcon = pointerIcon;
                InputManager.getInstance().setCustomPointerIcon(this.mCustomPointerIcon);
            }
            return true;
        } else {
            Slog.d(this.mTag, "updatePointerIcon called with position out of bounds");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeUpdateTooltip(MotionEvent event) {
        if (event.getPointerCount() != 1) {
            return;
        }
        int action = event.getActionMasked();
        if (action != 9 && action != 7 && action != 10) {
            return;
        }
        AccessibilityManager manager = AccessibilityManager.getInstance(this.mContext);
        if (manager.isEnabled() && manager.isTouchExplorationEnabled()) {
            return;
        }
        View view = this.mView;
        if (view == null) {
            Slog.d(this.mTag, "maybeUpdateTooltip called after view was removed");
        } else {
            view.dispatchTooltipHoverEvent(event);
        }
    }

    private View getFocusedViewOrNull() {
        View view = this.mView;
        if (view != null) {
            return view.findFocus();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class SyntheticInputStage extends InputStage {
        private final SyntheticJoystickHandler mJoystick;
        private final SyntheticKeyboardHandler mKeyboard;
        private final SyntheticTouchNavigationHandler mTouchNavigation;
        private final SyntheticTrackballHandler mTrackball;

        public SyntheticInputStage() {
            super(null);
            this.mTrackball = new SyntheticTrackballHandler();
            this.mJoystick = new SyntheticJoystickHandler();
            this.mTouchNavigation = new SyntheticTouchNavigationHandler();
            this.mKeyboard = new SyntheticKeyboardHandler();
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected int onProcess(QueuedInputEvent q) {
            q.mFlags |= 16;
            if (q.mEvent instanceof MotionEvent) {
                MotionEvent event = (MotionEvent) q.mEvent;
                int source = event.getSource();
                if ((source & 4) != 0) {
                    this.mTrackball.process(event);
                    return 1;
                } else if ((source & 16) != 0) {
                    this.mJoystick.process(event);
                    return 1;
                } else if ((source & 2097152) == 2097152) {
                    this.mTouchNavigation.process(event);
                    return 1;
                } else {
                    return 0;
                }
            } else if ((q.mFlags & 32) != 0) {
                this.mKeyboard.process((KeyEvent) q.mEvent);
                return 1;
            } else {
                return 0;
            }
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected void onDeliverToNext(QueuedInputEvent q) {
            if ((q.mFlags & 16) == 0 && (q.mEvent instanceof MotionEvent)) {
                MotionEvent event = (MotionEvent) q.mEvent;
                int source = event.getSource();
                if ((source & 4) != 0) {
                    this.mTrackball.cancel();
                } else if ((source & 16) == 0) {
                    if ((source & 2097152) == 2097152) {
                        this.mTouchNavigation.cancel(event);
                    }
                } else {
                    this.mJoystick.cancel();
                }
            }
            super.onDeliverToNext(q);
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected void onWindowFocusChanged(boolean hasWindowFocus) {
            if (hasWindowFocus) {
                return;
            }
            this.mJoystick.cancel();
        }

        @Override // android.view.ViewRootImpl.InputStage
        protected void onDetachedFromWindow() {
            this.mJoystick.cancel();
        }
    }

    /* loaded from: classes3.dex */
    final class SyntheticTrackballHandler {
        private long mLastTime;
        private final TrackballAxis mX = new TrackballAxis();
        private final TrackballAxis mY = new TrackballAxis();

        SyntheticTrackballHandler() {
        }

        public void process(MotionEvent event) {
            long curTime;
            int keycode;
            float accel;
            String str;
            int keycode2;
            long curTime2;
            String str2;
            long curTime3 = SystemClock.uptimeMillis();
            if (this.mLastTime + 250 < curTime3) {
                this.mX.reset(0);
                this.mY.reset(0);
                this.mLastTime = curTime3;
            }
            int action = event.getAction();
            int metaState = event.getMetaState();
            switch (action) {
                case 0:
                    curTime = curTime3;
                    this.mX.reset(2);
                    this.mY.reset(2);
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime, curTime, 0, 23, 0, metaState, -1, 0, 1024, 257));
                    break;
                case 1:
                    this.mX.reset(2);
                    this.mY.reset(2);
                    curTime = curTime3;
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime3, curTime3, 1, 23, 0, metaState, -1, 0, 1024, 257));
                    break;
                default:
                    curTime = curTime3;
                    break;
            }
            if (ViewRootImpl.DEBUG_TRACKBALL) {
                Log.v(ViewRootImpl.this.mTag, "TB X=" + this.mX.position + " step=" + this.mX.step + " dir=" + this.mX.dir + " acc=" + this.mX.acceleration + " move=" + event.getX() + " / Y=" + this.mY.position + " step=" + this.mY.step + " dir=" + this.mY.dir + " acc=" + this.mY.acceleration + " move=" + event.getY());
            }
            float xOff = this.mX.collect(event.getX(), event.getEventTime(), "X");
            float yOff = this.mY.collect(event.getY(), event.getEventTime(), "Y");
            int movement = 0;
            if (xOff > yOff) {
                movement = this.mX.generate();
                if (movement == 0) {
                    keycode = 0;
                    accel = 1.0f;
                } else {
                    int keycode3 = movement > 0 ? 22 : 21;
                    float accel2 = this.mX.acceleration;
                    this.mY.reset(2);
                    keycode = keycode3;
                    accel = accel2;
                }
            } else if (yOff <= 0.0f) {
                keycode = 0;
                accel = 1.0f;
            } else {
                movement = this.mY.generate();
                if (movement == 0) {
                    keycode = 0;
                    accel = 1.0f;
                } else {
                    int keycode4 = movement > 0 ? 20 : 19;
                    float accel3 = this.mY.acceleration;
                    this.mX.reset(2);
                    keycode = keycode4;
                    accel = accel3;
                }
            }
            if (keycode != 0) {
                if (movement < 0) {
                    movement = -movement;
                }
                int accelMovement = (int) (movement * accel);
                if (ViewRootImpl.DEBUG_TRACKBALL) {
                    Log.v(ViewRootImpl.this.mTag, "Move: movement=" + movement + " accelMovement=" + accelMovement + " accel=" + accel);
                }
                if (accelMovement > movement) {
                    if (ViewRootImpl.DEBUG_TRACKBALL) {
                        Log.v(ViewRootImpl.this.mTag, "Delivering fake DPAD: " + keycode);
                    }
                    int movement2 = movement - 1;
                    int repeatCount = accelMovement - movement2;
                    str = "Delivering fake DPAD: ";
                    keycode2 = keycode;
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime, curTime, 2, keycode, repeatCount, metaState, -1, 0, 1024, 257));
                    curTime2 = curTime;
                    movement = movement2;
                } else {
                    str = "Delivering fake DPAD: ";
                    keycode2 = keycode;
                    curTime2 = curTime;
                }
                while (movement > 0) {
                    if (ViewRootImpl.DEBUG_TRACKBALL) {
                        String str3 = ViewRootImpl.this.mTag;
                        StringBuilder sb = new StringBuilder();
                        str2 = str;
                        sb.append(str2);
                        sb.append(keycode2);
                        Log.v(str3, sb.toString());
                    } else {
                        str2 = str;
                    }
                    long curTime4 = SystemClock.uptimeMillis();
                    int i = keycode2;
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime4, curTime4, 0, i, 0, metaState, -1, 0, 1024, 257));
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime4, curTime4, 1, i, 0, metaState, -1, 0, 1024, 257));
                    movement--;
                    curTime2 = curTime4;
                    str = str2;
                    keycode2 = keycode2;
                }
                this.mLastTime = curTime2;
            }
        }

        public void cancel() {
            this.mLastTime = -2147483648L;
            if (ViewRootImpl.this.mView != null && ViewRootImpl.this.mAdded) {
                ViewRootImpl.this.ensureTouchMode(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class TrackballAxis {
        static final float ACCEL_MOVE_SCALING_FACTOR = 0.025f;
        static final long FAST_MOVE_TIME = 150;
        static final float FIRST_MOVEMENT_THRESHOLD = 0.5f;
        static final float MAX_ACCELERATION = 20.0f;
        static final float SECOND_CUMULATIVE_MOVEMENT_THRESHOLD = 2.0f;
        static final float SUBSEQUENT_INCREMENTAL_MOVEMENT_THRESHOLD = 1.0f;
        int dir;
        int nonAccelMovement;
        float position;
        int step;
        float acceleration = 1.0f;
        long lastMoveTime = 0;

        TrackballAxis() {
        }

        void reset(int _step) {
            this.position = 0.0f;
            this.acceleration = 1.0f;
            this.lastMoveTime = 0L;
            this.step = _step;
            this.dir = 0;
        }

        float collect(float off, long time, String axis) {
            long normTime;
            if (off > 0.0f) {
                normTime = off * 150.0f;
                if (this.dir < 0) {
                    if (ViewRootImpl.DEBUG_TRACKBALL) {
                        Log.v(ViewRootImpl.TAG, axis + " reversed to positive!");
                    }
                    this.position = 0.0f;
                    this.step = 0;
                    this.acceleration = 1.0f;
                    this.lastMoveTime = 0L;
                }
                this.dir = 1;
            } else if (off < 0.0f) {
                normTime = (-off) * 150.0f;
                if (this.dir > 0) {
                    if (ViewRootImpl.DEBUG_TRACKBALL) {
                        Log.v(ViewRootImpl.TAG, axis + " reversed to negative!");
                    }
                    this.position = 0.0f;
                    this.step = 0;
                    this.acceleration = 1.0f;
                    this.lastMoveTime = 0L;
                }
                this.dir = -1;
            } else {
                normTime = 0;
            }
            if (normTime > 0) {
                long delta = time - this.lastMoveTime;
                this.lastMoveTime = time;
                float acc = this.acceleration;
                if (delta < normTime) {
                    float scale = ((float) (normTime - delta)) * ACCEL_MOVE_SCALING_FACTOR;
                    if (scale > 1.0f) {
                        acc *= scale;
                    }
                    if (ViewRootImpl.DEBUG_TRACKBALL) {
                        Log.v(ViewRootImpl.TAG, axis + " accelerate: off=" + off + " normTime=" + normTime + " delta=" + delta + " scale=" + scale + " acc=" + acc);
                    }
                    float f = MAX_ACCELERATION;
                    if (acc < MAX_ACCELERATION) {
                        f = acc;
                    }
                    this.acceleration = f;
                } else {
                    float scale2 = ((float) (delta - normTime)) * ACCEL_MOVE_SCALING_FACTOR;
                    if (scale2 > 1.0f) {
                        acc /= scale2;
                    }
                    if (ViewRootImpl.DEBUG_TRACKBALL) {
                        Log.v(ViewRootImpl.TAG, axis + " deccelerate: off=" + off + " normTime=" + normTime + " delta=" + delta + " scale=" + scale2 + " acc=" + acc);
                    }
                    this.acceleration = acc > 1.0f ? acc : 1.0f;
                }
            }
            float f2 = this.position + off;
            this.position = f2;
            return Math.abs(f2);
        }

        int generate() {
            int movement = 0;
            this.nonAccelMovement = 0;
            while (true) {
                float f = this.position;
                int dir = f >= 0.0f ? 1 : -1;
                switch (this.step) {
                    case 0:
                        if (Math.abs(f) < 0.5f) {
                            return movement;
                        }
                        movement += dir;
                        this.nonAccelMovement += dir;
                        this.step = 1;
                        break;
                    case 1:
                        if (Math.abs(f) < SECOND_CUMULATIVE_MOVEMENT_THRESHOLD) {
                            return movement;
                        }
                        movement += dir;
                        this.nonAccelMovement += dir;
                        this.position -= dir * SECOND_CUMULATIVE_MOVEMENT_THRESHOLD;
                        this.step = 2;
                        break;
                    default:
                        if (Math.abs(f) < 1.0f) {
                            return movement;
                        }
                        movement += dir;
                        this.position -= dir * 1.0f;
                        float acc = this.acceleration * 1.1f;
                        this.acceleration = acc < MAX_ACCELERATION ? acc : this.acceleration;
                        break;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class SyntheticJoystickHandler extends Handler {
        private static final int MSG_ENQUEUE_X_AXIS_KEY_REPEAT = 1;
        private static final int MSG_ENQUEUE_Y_AXIS_KEY_REPEAT = 2;
        private final JoystickAxesState mJoystickAxesState = new JoystickAxesState();
        private final SparseArray<KeyEvent> mDeviceKeyEvents = new SparseArray<>();

        public SyntheticJoystickHandler() {
            super(true);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                case 2:
                    if (ViewRootImpl.this.mAttachInfo.mHasWindowFocus) {
                        KeyEvent oldEvent = (KeyEvent) msg.obj;
                        KeyEvent e = KeyEvent.changeTimeRepeat(oldEvent, SystemClock.uptimeMillis(), oldEvent.getRepeatCount() + 1);
                        ViewRootImpl.this.enqueueInputEvent(e);
                        Message m = obtainMessage(msg.what, e);
                        m.setAsynchronous(true);
                        sendMessageDelayed(m, ViewConfiguration.getKeyRepeatDelay());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void process(MotionEvent event) {
            switch (event.getActionMasked()) {
                case 2:
                    update(event);
                    return;
                case 3:
                    cancel();
                    return;
                default:
                    String str = ViewRootImpl.this.mTag;
                    Log.w(str, "Unexpected action: " + event.getActionMasked());
                    return;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancel() {
            removeMessages(1);
            removeMessages(2);
            for (int i = 0; i < this.mDeviceKeyEvents.size(); i++) {
                KeyEvent keyEvent = this.mDeviceKeyEvents.valueAt(i);
                if (keyEvent != null) {
                    ViewRootImpl.this.enqueueInputEvent(KeyEvent.changeTimeRepeat(keyEvent, SystemClock.uptimeMillis(), 0));
                }
            }
            this.mDeviceKeyEvents.clear();
            this.mJoystickAxesState.resetState();
        }

        private void update(MotionEvent event) {
            int historySize = event.getHistorySize();
            for (int h = 0; h < historySize; h++) {
                long time = event.getHistoricalEventTime(h);
                this.mJoystickAxesState.updateStateForAxis(event, time, 0, event.getHistoricalAxisValue(0, 0, h));
                this.mJoystickAxesState.updateStateForAxis(event, time, 1, event.getHistoricalAxisValue(1, 0, h));
                this.mJoystickAxesState.updateStateForAxis(event, time, 15, event.getHistoricalAxisValue(15, 0, h));
                this.mJoystickAxesState.updateStateForAxis(event, time, 16, event.getHistoricalAxisValue(16, 0, h));
            }
            long time2 = event.getEventTime();
            this.mJoystickAxesState.updateStateForAxis(event, time2, 0, event.getAxisValue(0));
            this.mJoystickAxesState.updateStateForAxis(event, time2, 1, event.getAxisValue(1));
            this.mJoystickAxesState.updateStateForAxis(event, time2, 15, event.getAxisValue(15));
            this.mJoystickAxesState.updateStateForAxis(event, time2, 16, event.getAxisValue(16));
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public final class JoystickAxesState {
            private static final int STATE_DOWN_OR_RIGHT = 1;
            private static final int STATE_NEUTRAL = 0;
            private static final int STATE_UP_OR_LEFT = -1;
            final int[] mAxisStatesHat = {0, 0};
            final int[] mAxisStatesStick = {0, 0};

            JoystickAxesState() {
            }

            void resetState() {
                int[] iArr = this.mAxisStatesHat;
                iArr[0] = 0;
                iArr[1] = 0;
                int[] iArr2 = this.mAxisStatesStick;
                iArr2[0] = 0;
                iArr2[1] = 0;
            }

            void updateStateForAxis(MotionEvent event, long time, int axis, float value) {
                int axisStateIndex;
                int repeatMessage;
                int currentState;
                int keyCode;
                if (isXAxis(axis)) {
                    axisStateIndex = 0;
                    repeatMessage = 1;
                } else if (!isYAxis(axis)) {
                    String str = ViewRootImpl.this.mTag;
                    Log.e(str, "Unexpected axis " + axis + " in updateStateForAxis!");
                    return;
                } else {
                    axisStateIndex = 1;
                    repeatMessage = 2;
                }
                int newState = joystickAxisValueToState(value);
                if (axis == 0 || axis == 1) {
                    currentState = this.mAxisStatesStick[axisStateIndex];
                } else {
                    currentState = this.mAxisStatesHat[axisStateIndex];
                }
                if (currentState == newState) {
                    return;
                }
                int metaState = event.getMetaState();
                int deviceId = event.getDeviceId();
                int source = event.getSource();
                if (currentState == 1 || currentState == -1) {
                    int keyCode2 = joystickAxisAndStateToKeycode(axis, currentState);
                    if (keyCode2 != 0) {
                        ViewRootImpl.this.enqueueInputEvent(new KeyEvent(time, time, 1, keyCode2, 0, metaState, deviceId, 0, 1024, source));
                        deviceId = deviceId;
                        SyntheticJoystickHandler.this.mDeviceKeyEvents.put(deviceId, null);
                    }
                    SyntheticJoystickHandler.this.removeMessages(repeatMessage);
                }
                if ((newState == 1 || newState == -1) && (keyCode = joystickAxisAndStateToKeycode(axis, newState)) != 0) {
                    int deviceId2 = deviceId;
                    KeyEvent keyEvent = new KeyEvent(time, time, 0, keyCode, 0, metaState, deviceId2, 0, 1024, source);
                    ViewRootImpl.this.enqueueInputEvent(keyEvent);
                    Message m = SyntheticJoystickHandler.this.obtainMessage(repeatMessage, keyEvent);
                    m.setAsynchronous(true);
                    SyntheticJoystickHandler.this.sendMessageDelayed(m, ViewConfiguration.getKeyRepeatTimeout());
                    SyntheticJoystickHandler.this.mDeviceKeyEvents.put(deviceId2, new KeyEvent(time, time, 1, keyCode, 0, metaState, deviceId2, 0, 1056, source));
                }
                if (axis == 0 || axis == 1) {
                    this.mAxisStatesStick[axisStateIndex] = newState;
                } else {
                    this.mAxisStatesHat[axisStateIndex] = newState;
                }
            }

            private boolean isXAxis(int axis) {
                return axis == 0 || axis == 15;
            }

            private boolean isYAxis(int axis) {
                return axis == 1 || axis == 16;
            }

            private int joystickAxisAndStateToKeycode(int axis, int state) {
                if (isXAxis(axis) && state == -1) {
                    return 21;
                }
                if (isXAxis(axis) && state == 1) {
                    return 22;
                }
                if (isYAxis(axis) && state == -1) {
                    return 19;
                }
                if (!isYAxis(axis) || state != 1) {
                    String str = ViewRootImpl.this.mTag;
                    Log.e(str, "Unknown axis " + axis + " or direction " + state);
                    return 0;
                }
                return 20;
            }

            private int joystickAxisValueToState(float value) {
                if (value >= 0.5f) {
                    return 1;
                }
                if (value <= -0.5f) {
                    return -1;
                }
                return 0;
            }
        }
    }

    /* loaded from: classes3.dex */
    final class SyntheticTouchNavigationHandler extends Handler {
        private static final float DEFAULT_HEIGHT_MILLIMETERS = 48.0f;
        private static final float DEFAULT_WIDTH_MILLIMETERS = 48.0f;
        private static final float FLING_TICK_DECAY = 0.8f;
        private static final boolean LOCAL_DEBUG = false;
        private static final String LOCAL_TAG = "SyntheticTouchNavigationHandler";
        private static final float MAX_FLING_VELOCITY_TICKS_PER_SECOND = 20.0f;
        private static final float MIN_FLING_VELOCITY_TICKS_PER_SECOND = 6.0f;
        private static final int TICK_DISTANCE_MILLIMETERS = 12;
        private float mAccumulatedX;
        private float mAccumulatedY;
        private float mConfigMaxFlingVelocity;
        private float mConfigMinFlingVelocity;
        private float mConfigTickDistance;
        private boolean mConsumedMovement;
        private boolean mCurrentDeviceSupported;
        private int mCurrentSource;
        private float mFlingVelocity;
        private boolean mFlinging;
        private float mLastX;
        private float mLastY;
        private long mPendingKeyDownTime;
        private int mPendingKeyMetaState;
        private int mPendingKeyRepeatCount;
        private float mStartX;
        private float mStartY;
        private VelocityTracker mVelocityTracker;
        private int mCurrentDeviceId = -1;
        private int mActivePointerId = -1;
        private int mPendingKeyCode = 0;
        private final Runnable mFlingRunnable = new Runnable() { // from class: android.view.ViewRootImpl.SyntheticTouchNavigationHandler.1
            @Override // java.lang.Runnable
            public void run() {
                long time = SystemClock.uptimeMillis();
                SyntheticTouchNavigationHandler syntheticTouchNavigationHandler = SyntheticTouchNavigationHandler.this;
                syntheticTouchNavigationHandler.sendKeyDownOrRepeat(time, syntheticTouchNavigationHandler.mPendingKeyCode, SyntheticTouchNavigationHandler.this.mPendingKeyMetaState);
                SyntheticTouchNavigationHandler.access$3832(SyntheticTouchNavigationHandler.this, 0.8f);
                if (!SyntheticTouchNavigationHandler.this.postFling(time)) {
                    SyntheticTouchNavigationHandler.this.mFlinging = false;
                    SyntheticTouchNavigationHandler.this.finishKeys(time);
                }
            }
        };

        static /* synthetic */ float access$3832(SyntheticTouchNavigationHandler x0, float x1) {
            float f = x0.mFlingVelocity * x1;
            x0.mFlingVelocity = f;
            return f;
        }

        public SyntheticTouchNavigationHandler() {
            super(true);
        }

        public void process(MotionEvent event) {
            long time = event.getEventTime();
            int deviceId = event.getDeviceId();
            int source = event.getSource();
            if (this.mCurrentDeviceId != deviceId || this.mCurrentSource != source) {
                finishKeys(time);
                finishTracking(time);
                this.mCurrentDeviceId = deviceId;
                this.mCurrentSource = source;
                this.mCurrentDeviceSupported = false;
                InputDevice device = event.getDevice();
                if (device != null) {
                    InputDevice.MotionRange xRange = device.getMotionRange(0);
                    InputDevice.MotionRange yRange = device.getMotionRange(1);
                    if (xRange != null && yRange != null) {
                        this.mCurrentDeviceSupported = true;
                        float xRes = xRange.getResolution();
                        if (xRes <= 0.0f) {
                            xRes = xRange.getRange() / 48.0f;
                        }
                        float yRes = yRange.getResolution();
                        if (yRes <= 0.0f) {
                            yRes = yRange.getRange() / 48.0f;
                        }
                        float nominalRes = (xRes + yRes) * 0.5f;
                        float f = 12.0f * nominalRes;
                        this.mConfigTickDistance = f;
                        this.mConfigMinFlingVelocity = f * MIN_FLING_VELOCITY_TICKS_PER_SECOND;
                        this.mConfigMaxFlingVelocity = f * MAX_FLING_VELOCITY_TICKS_PER_SECOND;
                    }
                }
            }
            if (!this.mCurrentDeviceSupported) {
                return;
            }
            int action = event.getActionMasked();
            switch (action) {
                case 0:
                    boolean caughtFling = this.mFlinging;
                    finishKeys(time);
                    finishTracking(time);
                    this.mActivePointerId = event.getPointerId(0);
                    VelocityTracker obtain = VelocityTracker.obtain();
                    this.mVelocityTracker = obtain;
                    obtain.addMovement(event);
                    this.mStartX = event.getX();
                    float y = event.getY();
                    this.mStartY = y;
                    this.mLastX = this.mStartX;
                    this.mLastY = y;
                    this.mAccumulatedX = 0.0f;
                    this.mAccumulatedY = 0.0f;
                    this.mConsumedMovement = caughtFling;
                    return;
                case 1:
                case 2:
                    int i = this.mActivePointerId;
                    if (i >= 0) {
                        int index = event.findPointerIndex(i);
                        if (index < 0) {
                            finishKeys(time);
                            finishTracking(time);
                            return;
                        }
                        this.mVelocityTracker.addMovement(event);
                        float x = event.getX(index);
                        float y2 = event.getY(index);
                        this.mAccumulatedX += x - this.mLastX;
                        this.mAccumulatedY += y2 - this.mLastY;
                        this.mLastX = x;
                        this.mLastY = y2;
                        int metaState = event.getMetaState();
                        consumeAccumulatedMovement(time, metaState);
                        if (action == 1) {
                            if (this.mConsumedMovement && this.mPendingKeyCode != 0) {
                                this.mVelocityTracker.computeCurrentVelocity(1000, this.mConfigMaxFlingVelocity);
                                float vx = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
                                float vy = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                                if (!startFling(time, vx, vy)) {
                                    finishKeys(time);
                                }
                            }
                            finishTracking(time);
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    finishKeys(time);
                    finishTracking(time);
                    return;
                default:
                    return;
            }
        }

        public void cancel(MotionEvent event) {
            if (this.mCurrentDeviceId == event.getDeviceId() && this.mCurrentSource == event.getSource()) {
                long time = event.getEventTime();
                finishKeys(time);
                finishTracking(time);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void finishKeys(long time) {
            cancelFling();
            sendKeyUp(time);
        }

        private void finishTracking(long time) {
            if (this.mActivePointerId >= 0) {
                this.mActivePointerId = -1;
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
        }

        private void consumeAccumulatedMovement(long time, int metaState) {
            float absX = Math.abs(this.mAccumulatedX);
            float absY = Math.abs(this.mAccumulatedY);
            if (absX >= absY) {
                if (absX >= this.mConfigTickDistance) {
                    this.mAccumulatedX = consumeAccumulatedMovement(time, metaState, this.mAccumulatedX, 21, 22);
                    this.mAccumulatedY = 0.0f;
                    this.mConsumedMovement = true;
                }
            } else if (absY >= this.mConfigTickDistance) {
                this.mAccumulatedY = consumeAccumulatedMovement(time, metaState, this.mAccumulatedY, 19, 20);
                this.mAccumulatedX = 0.0f;
                this.mConsumedMovement = true;
            }
        }

        private float consumeAccumulatedMovement(long time, int metaState, float accumulator, int negativeKeyCode, int positiveKeyCode) {
            while (accumulator <= (-this.mConfigTickDistance)) {
                sendKeyDownOrRepeat(time, negativeKeyCode, metaState);
                accumulator += this.mConfigTickDistance;
            }
            while (accumulator >= this.mConfigTickDistance) {
                sendKeyDownOrRepeat(time, positiveKeyCode, metaState);
                accumulator -= this.mConfigTickDistance;
            }
            return accumulator;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void sendKeyDownOrRepeat(long time, int keyCode, int metaState) {
            if (this.mPendingKeyCode == keyCode) {
                this.mPendingKeyRepeatCount++;
            } else {
                sendKeyUp(time);
                this.mPendingKeyDownTime = time;
                this.mPendingKeyCode = keyCode;
                this.mPendingKeyRepeatCount = 0;
            }
            this.mPendingKeyMetaState = metaState;
            ViewRootImpl.this.enqueueInputEvent(new KeyEvent(this.mPendingKeyDownTime, time, 0, this.mPendingKeyCode, this.mPendingKeyRepeatCount, this.mPendingKeyMetaState, this.mCurrentDeviceId, 1024, this.mCurrentSource));
        }

        private void sendKeyUp(long time) {
            if (this.mPendingKeyCode != 0) {
                ViewRootImpl.this.enqueueInputEvent(new KeyEvent(this.mPendingKeyDownTime, time, 1, this.mPendingKeyCode, 0, this.mPendingKeyMetaState, this.mCurrentDeviceId, 0, 1024, this.mCurrentSource));
                this.mPendingKeyCode = 0;
            }
        }

        private boolean startFling(long time, float vx, float vy) {
            switch (this.mPendingKeyCode) {
                case 19:
                    if ((-vy) >= this.mConfigMinFlingVelocity && Math.abs(vx) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = -vy;
                        break;
                    } else {
                        return false;
                    }
                case 20:
                    if (vy >= this.mConfigMinFlingVelocity && Math.abs(vx) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = vy;
                        break;
                    } else {
                        return false;
                    }
                case 21:
                    if ((-vx) >= this.mConfigMinFlingVelocity && Math.abs(vy) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = -vx;
                        break;
                    } else {
                        return false;
                    }
                    break;
                case 22:
                    if (vx >= this.mConfigMinFlingVelocity && Math.abs(vy) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = vx;
                        break;
                    } else {
                        return false;
                    }
            }
            boolean postFling = postFling(time);
            this.mFlinging = postFling;
            return postFling;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean postFling(long time) {
            float f = this.mFlingVelocity;
            if (f >= this.mConfigMinFlingVelocity) {
                long delay = (this.mConfigTickDistance / f) * 1000.0f;
                postAtTime(this.mFlingRunnable, time + delay);
                return true;
            }
            return false;
        }

        private void cancelFling() {
            if (this.mFlinging) {
                removeCallbacks(this.mFlingRunnable);
                this.mFlinging = false;
            }
        }
    }

    /* loaded from: classes3.dex */
    final class SyntheticKeyboardHandler {
        SyntheticKeyboardHandler() {
        }

        public void process(KeyEvent event) {
            if ((event.getFlags() & 1024) != 0) {
                return;
            }
            KeyCharacterMap kcm = event.getKeyCharacterMap();
            int keyCode = event.getKeyCode();
            int metaState = event.getMetaState();
            KeyCharacterMap.FallbackAction fallbackAction = kcm.getFallbackAction(keyCode, metaState);
            if (fallbackAction != null) {
                int flags = event.getFlags() | 1024;
                KeyEvent fallbackEvent = KeyEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), fallbackAction.keyCode, event.getRepeatCount(), fallbackAction.metaState, event.getDeviceId(), event.getScanCode(), flags, event.getSource(), null);
                fallbackAction.recycle();
                ViewRootImpl.this.enqueueInputEvent(fallbackEvent);
            }
        }
    }

    private static boolean isNavigationKey(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 61:
            case 62:
            case 66:
            case 92:
            case 93:
            case 122:
            case 123:
                return true;
            default:
                return false;
        }
    }

    private static boolean isTypingKey(KeyEvent keyEvent) {
        return keyEvent.getUnicodeChar() > 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkForLeavingTouchModeAndConsume(KeyEvent event) {
        if (!this.mAttachInfo.mInTouchMode) {
            return false;
        }
        int action = event.getAction();
        if ((action != 0 && action != 2) || (event.getFlags() & 4) != 0) {
            return false;
        }
        if (isNavigationKey(event)) {
            return ensureTouchMode(false);
        }
        if (!isTypingKey(event)) {
            return false;
        }
        ensureTouchMode(false);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setLocalDragState(Object obj) {
        this.mLocalDragState = obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDragEvent(DragEvent event) {
        if (this.mView != null && this.mAdded) {
            int what = event.mAction;
            if (what == 1) {
                this.mCurrentDragView = null;
                this.mDragDescription = event.mClipDescription;
            } else {
                if (what == 4) {
                    this.mDragDescription = null;
                }
                event.mClipDescription = this.mDragDescription;
            }
            if (what == 6) {
                if (View.sCascadedDragDrop) {
                    this.mView.dispatchDragEnterExitInPreN(event);
                }
                setDragFocus(null, event);
            } else {
                if (what == 2 || what == 3) {
                    this.mDragPoint.set(event.mX, event.mY);
                    CompatibilityInfo.Translator translator = this.mTranslator;
                    if (translator != null) {
                        translator.translatePointInScreenToAppWindow(this.mDragPoint);
                    }
                    int i = this.mCurScrollY;
                    if (i != 0) {
                        this.mDragPoint.offset(0.0f, i);
                    }
                    event.mX = this.mDragPoint.x;
                    event.mY = this.mDragPoint.y;
                }
                View prevDragView = this.mCurrentDragView;
                if (what == 3 && event.mClipData != null) {
                    event.mClipData.prepareToEnterProcess(this.mView.getContext().getAttributionSource());
                }
                boolean result = this.mView.dispatchDragEvent(event);
                if (what == 2 && !event.mEventHandlerWasCalled) {
                    setDragFocus(null, event);
                }
                if (prevDragView != this.mCurrentDragView) {
                    if (prevDragView != null) {
                        try {
                            this.mWindowSession.dragRecipientExited(this.mWindow);
                        } catch (RemoteException e) {
                            Slog.e(this.mTag, "Unable to note drag target change");
                        }
                    }
                    if (this.mCurrentDragView != null) {
                        this.mWindowSession.dragRecipientEntered(this.mWindow);
                    }
                }
                if (what == 3) {
                    try {
                        String str = this.mTag;
                        Log.i(str, "Reporting drop result: " + result);
                        this.mWindowSession.reportDropResult(this.mWindow, result);
                    } catch (RemoteException e2) {
                        Log.e(this.mTag, "Unable to report drop result");
                    }
                }
                if (what == 4) {
                    this.mCurrentDragView = null;
                    setLocalDragState(null);
                    this.mAttachInfo.mDragToken = null;
                    if (this.mAttachInfo.mDragSurface != null) {
                        this.mAttachInfo.mDragSurface.release();
                        this.mAttachInfo.mDragSurface = null;
                    }
                }
            }
        }
        event.recycle();
    }

    public void onWindowTitleChanged() {
        this.mAttachInfo.mForceReportNewAttributes = true;
    }

    public void handleDispatchWindowShown() {
        this.mAttachInfo.mTreeObserver.dispatchOnWindowShown();
    }

    public void handleRequestKeyboardShortcuts(IResultReceiver receiver, int deviceId) {
        Bundle data = new Bundle();
        ArrayList<KeyboardShortcutGroup> list = new ArrayList<>();
        View view = this.mView;
        if (view != null) {
            view.requestKeyboardShortcuts(list, deviceId);
        }
        data.putParcelableArrayList(WindowManager.PARCEL_KEY_SHORTCUTS_ARRAY, list);
        try {
            receiver.send(0, data);
        } catch (RemoteException e) {
        }
    }

    public void getLastTouchPoint(Point outLocation) {
        outLocation.x = (int) this.mLastTouchPoint.x;
        outLocation.y = (int) this.mLastTouchPoint.y;
    }

    public int getLastTouchSource() {
        return this.mLastTouchSource;
    }

    public void setDragFocus(View newDragTarget, DragEvent event) {
        if (this.mCurrentDragView != newDragTarget && !View.sCascadedDragDrop) {
            float tx = event.mX;
            float ty = event.mY;
            int action = event.mAction;
            ClipData td = event.mClipData;
            event.mX = 0.0f;
            event.mY = 0.0f;
            event.mClipData = null;
            if (this.mCurrentDragView != null) {
                event.mAction = 6;
                this.mCurrentDragView.callDragEventHandler(event);
            }
            if (newDragTarget != null) {
                event.mAction = 5;
                newDragTarget.callDragEventHandler(event);
            }
            event.mAction = action;
            event.mX = tx;
            event.mY = ty;
            event.mClipData = td;
        }
        this.mCurrentDragView = newDragTarget;
    }

    private AudioManager getAudioManager() {
        View view = this.mView;
        if (view == null) {
            throw new IllegalStateException("getAudioManager called when there is no mView");
        }
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) view.getContext().getSystemService("audio");
        }
        return this.mAudioManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public AutofillManager getAutofillManager() {
        View view = this.mView;
        if (view instanceof ViewGroup) {
            ViewGroup decorView = (ViewGroup) view;
            if (decorView.getChildCount() > 0) {
                return (AutofillManager) decorView.getChildAt(0).getContext().getSystemService(AutofillManager.class);
            }
            return null;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAutofillUiShowing() {
        AutofillManager afm = getAutofillManager();
        if (afm == null) {
            return false;
        }
        return afm.isAutofillUiShowing();
    }

    public AccessibilityInteractionController getAccessibilityInteractionController() {
        if (this.mView == null) {
            throw new IllegalStateException("getAccessibilityInteractionController called when there is no mView");
        }
        if (this.mAccessibilityInteractionController == null) {
            this.mAccessibilityInteractionController = new AccessibilityInteractionController(this);
        }
        return this.mAccessibilityInteractionController;
    }

    private int relayoutWindow(WindowManager.LayoutParams params, int viewVisibility, boolean insetsPending) throws RemoteException {
        boolean restore;
        long frameNumber;
        float appScale = this.mAttachInfo.mApplicationScale;
        if (params != null && this.mTranslator != null) {
            params.backup();
            this.mTranslator.translateWindowLayout(params);
            restore = true;
        } else {
            restore = false;
        }
        if (params != null) {
            if (DBG) {
                Log.d(this.mTag, "WindowLayout in layoutWindow:" + params);
            }
            if (this.mOrigWindowType != params.type && this.mTargetSdkVersion < 14) {
                Slog.w(this.mTag, "Window type can not be changed after the window is added; ignoring change of " + this.mView);
                params.type = this.mOrigWindowType;
            }
        }
        if (!this.mSurface.isValid()) {
            frameNumber = -1;
        } else {
            long frameNumber2 = this.mSurface.getNextFrameNumber();
            frameNumber = frameNumber2;
        }
        int relayoutResult = this.mWindowSession.relayout(this.mWindow, params, (int) ((this.mView.getMeasuredWidth() * appScale) + 0.5f), (int) ((this.mView.getMeasuredHeight() * appScale) + 0.5f), viewVisibility, insetsPending ? 1 : 0, frameNumber, this.mTmpFrames, this.mPendingMergedConfiguration, this.mSurfaceControl, this.mTempInsets, this.mTempControls, this.mSurfaceSize);
        this.mPendingBackDropFrame.set(this.mTmpFrames.backdropFrame);
        if (this.mSurfaceControl.isValid()) {
            if (!useBLAST()) {
                this.mSurface.copyFrom(this.mSurfaceControl);
            } else {
                Surface blastSurface = getOrCreateBLASTSurface();
                if (blastSurface != null) {
                    this.mSurface.transferFrom(blastSurface);
                }
            }
            if (this.mAttachInfo.mThreadedRenderer != null) {
                if (HardwareRenderer.isWebViewOverlaysEnabled()) {
                    addPrepareSurfaceControlForWebviewCallback();
                    addASurfaceTransactionCallback();
                }
                this.mAttachInfo.mThreadedRenderer.setSurfaceControl(this.mSurfaceControl);
            }
        } else {
            destroySurface();
        }
        this.mPendingAlwaysConsumeSystemBars = (relayoutResult & 64) != 0;
        if (restore) {
            params.restore();
        }
        CompatibilityInfo.Translator translator = this.mTranslator;
        if (translator != null) {
            translator.translateRectInScreenToAppWindow(this.mTmpFrames.frame);
            this.mTranslator.translateInsetsStateInScreenToAppWindow(this.mTempInsets);
            this.mTranslator.translateSourceControlsInScreenToAppWindow(this.mTempControls);
        }
        setFrame(this.mTmpFrames.frame);
        this.mWillMove = false;
        this.mWillResize = false;
        this.mInsetsController.onStateChanged(this.mTempInsets);
        this.mInsetsController.onControlsChanged(this.mTempControls);
        return relayoutResult;
    }

    private void updateOpacity(WindowManager.LayoutParams params, boolean dragResizing, boolean forceUpdate) {
        boolean opaque = false;
        if (!PixelFormat.formatHasAlpha(params.format) && params.surfaceInsets.left == 0 && params.surfaceInsets.top == 0 && params.surfaceInsets.right == 0 && params.surfaceInsets.bottom == 0 && !dragResizing) {
            opaque = true;
        }
        if (!forceUpdate && this.mIsSurfaceOpaque == opaque) {
            return;
        }
        synchronized (this) {
            if (this.mIsForWebviewOverlay) {
                this.mIsSurfaceOpaque = false;
                return;
            }
            this.mTransaction.setOpaque(this.mSurfaceControl, opaque).apply();
            this.mIsSurfaceOpaque = opaque;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFrame(Rect frame) {
        this.mWinFrame.set(frame);
        this.mInsetsController.onFrameChanged(frame);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getDisplayFrame(Rect outFrame) {
        outFrame.set(this.mTmpFrames.displayFrame);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void getWindowVisibleDisplayFrame(Rect outFrame) {
        outFrame.set(this.mTmpFrames.displayFrame);
        Rect insets = this.mAttachInfo.mVisibleInsets;
        outFrame.left += insets.left;
        outFrame.top += insets.top;
        outFrame.right -= insets.right;
        outFrame.bottom -= insets.bottom;
    }

    @Override // android.view.View.AttachInfo.Callbacks
    public void playSoundEffect(int effectId) {
        checkThread();
        try {
            AudioManager audioManager = getAudioManager();
            if (this.mFastScrollSoundEffectsEnabled && SoundEffectConstants.isNavigationRepeat(effectId)) {
                audioManager.playSoundEffect(SoundEffectConstants.nextNavigationRepeatSoundEffectId());
                return;
            }
            switch (effectId) {
                case 0:
                    audioManager.playSoundEffect(0);
                    return;
                case 1:
                case 5:
                    audioManager.playSoundEffect(3);
                    return;
                case 2:
                case 6:
                    audioManager.playSoundEffect(1);
                    return;
                case 3:
                case 7:
                    audioManager.playSoundEffect(4);
                    return;
                case 4:
                case 8:
                    audioManager.playSoundEffect(2);
                    return;
                default:
                    throw new IllegalArgumentException("unknown effect id " + effectId + " not defined in " + SoundEffectConstants.class.getCanonicalName());
            }
        } catch (IllegalStateException e) {
            String str = this.mTag;
            Log.e(str, "FATAL EXCEPTION when attempting to play sound effect: " + e);
            e.printStackTrace();
        }
    }

    @Override // android.view.View.AttachInfo.Callbacks
    public boolean performHapticFeedback(int effectId, boolean always) {
        try {
            return this.mWindowSession.performHapticFeedback(effectId, always);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Override // android.view.ViewParent
    public View focusSearch(View focused, int direction) {
        checkThread();
        if (!(this.mView instanceof ViewGroup)) {
            return null;
        }
        return FocusFinder.getInstance().findNextFocus((ViewGroup) this.mView, focused, direction);
    }

    @Override // android.view.ViewParent
    public View keyboardNavigationClusterSearch(View currentCluster, int direction) {
        checkThread();
        return FocusFinder.getInstance().findNextKeyboardNavigationCluster(this.mView, currentCluster, direction);
    }

    public void debug() {
        this.mView.debug();
    }

    public void dumpDebug(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, Objects.toString(this.mView));
        proto.write(1120986464258L, this.mDisplay.getDisplayId());
        proto.write(1133871366147L, this.mAppVisible);
        proto.write(1120986464261L, this.mHeight);
        proto.write(1120986464260L, this.mWidth);
        proto.write(1133871366150L, this.mIsAnimating);
        this.mVisRect.dumpDebug(proto, 1146756268039L);
        proto.write(1133871366152L, this.mIsDrawing);
        proto.write(1133871366153L, this.mAdded);
        this.mWinFrame.dumpDebug(proto, 1146756268042L);
        proto.write(1138166333452L, Objects.toString(this.mLastWindowInsets));
        proto.write(1138166333453L, InputMethodDebug.softInputModeToString(this.mSoftInputMode));
        proto.write(1120986464270L, this.mScrollY);
        proto.write(1120986464271L, this.mCurScrollY);
        proto.write(1133871366160L, this.mRemoved);
        this.mWindowAttributes.dumpDebug(proto, 1146756268049L);
        proto.end(token);
        this.mInsetsController.dumpDebug(proto, 1146756268036L);
        this.mImeFocusController.dumpDebug(proto, 1146756268039L);
    }

    public void dump(String prefix, PrintWriter writer) {
        String innerPrefix = prefix + "  ";
        writer.println(prefix + "ViewRoot:");
        writer.println(innerPrefix + "mAdded=" + this.mAdded);
        writer.println(innerPrefix + "mRemoved=" + this.mRemoved);
        writer.println(innerPrefix + "mStopped=" + this.mStopped);
        writer.println(innerPrefix + "mPausedForTransition=" + this.mPausedForTransition);
        writer.println(innerPrefix + "mConsumeBatchedInputScheduled=" + this.mConsumeBatchedInputScheduled);
        writer.println(innerPrefix + "mConsumeBatchedInputImmediatelyScheduled=" + this.mConsumeBatchedInputImmediatelyScheduled);
        writer.println(innerPrefix + "mPendingInputEventCount=" + this.mPendingInputEventCount);
        writer.println(innerPrefix + "mProcessInputEventsScheduled=" + this.mProcessInputEventsScheduled);
        writer.println(innerPrefix + "mTraversalScheduled=" + this.mTraversalScheduled);
        if (this.mTraversalScheduled) {
            writer.println(innerPrefix + " (barrier=" + this.mTraversalBarrier + ")");
        }
        writer.println(innerPrefix + "mIsAmbientMode=" + this.mIsAmbientMode);
        writer.println(innerPrefix + "mUnbufferedInputSource=" + Integer.toHexString(this.mUnbufferedInputSource));
        if (this.mAttachInfo != null) {
            writer.print(innerPrefix + "mAttachInfo= ");
            this.mAttachInfo.dump(innerPrefix, writer);
        } else {
            writer.println(innerPrefix + "mAttachInfo=<null>");
        }
        this.mFirstInputStage.dump(innerPrefix, writer);
        WindowInputEventReceiver windowInputEventReceiver = this.mInputEventReceiver;
        if (windowInputEventReceiver != null) {
            windowInputEventReceiver.dump(innerPrefix, writer);
        }
        this.mChoreographer.dump(prefix, writer);
        this.mInsetsController.dump(prefix, writer);
        writer.println(prefix + "View Hierarchy:");
        dumpViewHierarchy(innerPrefix, writer, this.mView);
    }

    private void dumpViewHierarchy(String prefix, PrintWriter writer, View view) {
        ViewGroup grp;
        int N;
        writer.print(prefix);
        if (view == null) {
            writer.println("null");
            return;
        }
        writer.println(view.toString());
        if (!(view instanceof ViewGroup) || (N = (grp = (ViewGroup) view).getChildCount()) <= 0) {
            return;
        }
        String prefix2 = prefix + "  ";
        for (int i = 0; i < N; i++) {
            dumpViewHierarchy(prefix2, writer, grp.getChildAt(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class GfxInfo {
        public long renderNodeMemoryAllocated;
        public long renderNodeMemoryUsage;
        public int viewCount;

        /* JADX INFO: Access modifiers changed from: package-private */
        public void add(GfxInfo other) {
            this.viewCount += other.viewCount;
            this.renderNodeMemoryUsage += other.renderNodeMemoryUsage;
            this.renderNodeMemoryAllocated += other.renderNodeMemoryAllocated;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public GfxInfo getGfxInfo() {
        GfxInfo info = new GfxInfo();
        View view = this.mView;
        if (view != null) {
            appendGfxInfo(view, info);
        }
        return info;
    }

    private static void computeRenderNodeUsage(RenderNode node, GfxInfo info) {
        if (node == null) {
            return;
        }
        info.renderNodeMemoryUsage += node.computeApproximateMemoryUsage();
        info.renderNodeMemoryAllocated += node.computeApproximateMemoryAllocated();
    }

    private static void appendGfxInfo(View view, GfxInfo info) {
        info.viewCount++;
        computeRenderNodeUsage(view.mRenderNode, info);
        computeRenderNodeUsage(view.mBackgroundRenderNode, info);
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                appendGfxInfo(group.getChildAt(i), info);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean die(boolean immediate) {
        if (immediate && !this.mIsInTraversal) {
            doDie();
            return false;
        }
        if (!this.mIsDrawing) {
            destroyHardwareRenderer();
        } else {
            String str = this.mTag;
            Log.e(str, "Attempting to destroy the window while drawing!\n  window=" + this + ", title=" + ((Object) this.mWindowAttributes.getTitle()));
        }
        this.mHandler.sendEmptyMessage(3);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void doDie() {
        checkThread();
        if (LOCAL_LOGV) {
            Log.v(this.mTag, "DIE in " + this + " of " + this.mSurface);
        }
        synchronized (this) {
            if (this.mRemoved) {
                return;
            }
            boolean viewVisibilityChanged = true;
            this.mRemoved = true;
            if (this.mAdded) {
                dispatchDetachedFromWindow();
            }
            if (this.mAdded && !this.mFirst) {
                destroyHardwareRenderer();
                View view = this.mView;
                if (view != null) {
                    int viewVisibility = view.getVisibility();
                    if (this.mViewVisibility == viewVisibility) {
                        viewVisibilityChanged = false;
                    }
                    if (this.mWindowAttributesChanged || viewVisibilityChanged) {
                        try {
                            if ((relayoutWindow(this.mWindowAttributes, viewVisibility, false) & 2) != 0) {
                                this.mWindowSession.finishDrawing(this.mWindow, null);
                            }
                        } catch (RemoteException e) {
                        }
                    }
                    destroySurface();
                }
            }
            this.mInsetsController.onControlsChanged(null);
            this.mAdded = false;
            WindowManagerGlobal.getInstance().doRemoveView(this);
        }
    }

    public void requestUpdateConfiguration(Configuration config) {
        Message msg = this.mHandler.obtainMessage(18, config);
        this.mHandler.sendMessage(msg);
    }

    public void loadSystemProperties() {
        this.mHandler.post(new Runnable() { // from class: android.view.ViewRootImpl.4
            @Override // java.lang.Runnable
            public void run() {
                ViewRootImpl.this.mProfileRendering = SystemProperties.getBoolean(ViewRootImpl.PROPERTY_PROFILE_RENDERING, false);
                ViewRootImpl viewRootImpl = ViewRootImpl.this;
                viewRootImpl.profileRendering(viewRootImpl.mAttachInfo.mHasWindowFocus);
                if (ViewRootImpl.this.mAttachInfo.mThreadedRenderer != null && ViewRootImpl.this.mAttachInfo.mThreadedRenderer.loadSystemProperties()) {
                    ViewRootImpl.this.invalidate();
                }
                boolean layout = DisplayProperties.debug_layout().orElse(false).booleanValue();
                if (layout != ViewRootImpl.this.mAttachInfo.mDebugLayout) {
                    ViewRootImpl.this.mAttachInfo.mDebugLayout = layout;
                    if (!ViewRootImpl.this.mHandler.hasMessages(22)) {
                        ViewRootImpl.this.mHandler.sendEmptyMessageDelayed(22, 200L);
                    }
                }
            }
        });
    }

    private void destroyHardwareRenderer() {
        ThreadedRenderer hardwareRenderer = this.mAttachInfo.mThreadedRenderer;
        if (hardwareRenderer != null) {
            HardwareRendererObserver hardwareRendererObserver = this.mHardwareRendererObserver;
            if (hardwareRendererObserver != null) {
                hardwareRenderer.removeObserver(hardwareRendererObserver);
            }
            View view = this.mView;
            if (view != null) {
                hardwareRenderer.destroyHardwareResources(view);
            }
            hardwareRenderer.destroy();
            hardwareRenderer.setRequested(false);
            this.mAttachInfo.mThreadedRenderer = null;
            this.mAttachInfo.mHardwareAccelerated = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchResized(ClientWindowFrames frames, boolean reportDraw, MergedConfiguration mergedConfiguration, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId) {
        Rect frame = frames.frame;
        Rect backDropFrame = frames.backdropFrame;
        if (DEBUG_LAYOUT) {
            Log.v(this.mTag, "Resizing " + this + ": frame=" + frame.toShortString() + " reportDraw=" + reportDraw + " backDropFrame=" + backDropFrame);
        }
        boolean sameProcessCall = true;
        if (this.mDragResizing && this.mUseMTRenderer) {
            boolean fullscreen = frame.equals(backDropFrame);
            synchronized (this.mWindowCallbacks) {
                for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                    this.mWindowCallbacks.get(i).onWindowSizeIsChanging(backDropFrame, fullscreen, this.mAttachInfo.mVisibleInsets, this.mAttachInfo.mStableInsets);
                }
            }
        }
        Message msg = this.mHandler.obtainMessage(reportDraw ? 5 : 4);
        CompatibilityInfo.Translator translator = this.mTranslator;
        if (translator != null) {
            translator.translateRectInScreenToAppWindow(frame);
        }
        SomeArgs args = SomeArgs.obtain();
        if (Binder.getCallingPid() != Process.myPid()) {
            sameProcessCall = false;
        }
        args.arg1 = sameProcessCall ? new ClientWindowFrames(frames) : frames;
        args.arg2 = (!sameProcessCall || mergedConfiguration == null) ? mergedConfiguration : new MergedConfiguration(mergedConfiguration);
        args.argi1 = forceLayout ? 1 : 0;
        args.argi2 = alwaysConsumeSystemBars ? 1 : 0;
        args.argi3 = displayId;
        msg.obj = args;
        this.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchInsetsChanged(InsetsState insetsState, boolean willMove, boolean willResize) {
        if (Binder.getCallingPid() == Process.myPid()) {
            insetsState = new InsetsState(insetsState, true);
        }
        CompatibilityInfo.Translator translator = this.mTranslator;
        if (translator != null) {
            translator.translateInsetsStateInScreenToAppWindow(insetsState);
        }
        if (insetsState != null && insetsState.getSource(19).isVisible()) {
            ImeTracing.getInstance().triggerClientDump("ViewRootImpl#dispatchInsetsChanged", getInsetsController().getHost().getInputMethodManager(), null);
        }
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = insetsState;
        args.argi1 = willMove ? 1 : 0;
        args.argi2 = willResize ? 1 : 0;
        this.mHandler.obtainMessage(30, args).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchInsetsControlChanged(InsetsState insetsState, InsetsSourceControl[] activeControls, boolean willMove, boolean willResize) {
        if (Binder.getCallingPid() == Process.myPid()) {
            insetsState = new InsetsState(insetsState, true);
            if (activeControls != null) {
                for (int i = activeControls.length - 1; i >= 0; i--) {
                    activeControls[i] = new InsetsSourceControl(activeControls[i]);
                }
            }
        }
        CompatibilityInfo.Translator translator = this.mTranslator;
        if (translator != null) {
            translator.translateInsetsStateInScreenToAppWindow(insetsState);
            this.mTranslator.translateSourceControlsInScreenToAppWindow(activeControls);
        }
        if (insetsState != null && insetsState.getSource(19).isVisible()) {
            ImeTracing.getInstance().triggerClientDump("ViewRootImpl#dispatchInsetsControlChanged", getInsetsController().getHost().getInputMethodManager(), null);
        }
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = insetsState;
        args.arg2 = activeControls;
        args.argi1 = willMove ? 1 : 0;
        args.argi2 = willResize ? 1 : 0;
        this.mHandler.obtainMessage(31, args).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showInsets(int types, boolean fromIme) {
        this.mHandler.obtainMessage(34, types, fromIme ? 1 : 0).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideInsets(int types, boolean fromIme) {
        this.mHandler.obtainMessage(35, types, fromIme ? 1 : 0).sendToTarget();
    }

    public void dispatchMoved(int newX, int newY) {
        if (DEBUG_LAYOUT) {
            String str = this.mTag;
            Log.v(str, "Window moved " + this + ": newX=" + newX + " newY=" + newY);
        }
        if (this.mTranslator != null) {
            PointF point = new PointF(newX, newY);
            this.mTranslator.translatePointInScreenToAppWindow(point);
            newX = (int) (point.x + 0.5d);
            newY = (int) (point.y + 0.5d);
        }
        Message msg = this.mHandler.obtainMessage(23, newX, newY);
        this.mHandler.sendMessage(msg);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class QueuedInputEvent {
        public static final int FLAG_DEFERRED = 2;
        public static final int FLAG_DELIVER_POST_IME = 1;
        public static final int FLAG_FINISHED = 4;
        public static final int FLAG_FINISHED_HANDLED = 8;
        public static final int FLAG_MODIFIED_FOR_COMPATIBILITY = 64;
        public static final int FLAG_RESYNTHESIZED = 16;
        public static final int FLAG_UNHANDLED = 32;
        public InputEvent mEvent;
        public int mFlags;
        public QueuedInputEvent mNext;
        public InputEventReceiver mReceiver;

        private QueuedInputEvent() {
        }

        public boolean shouldSkipIme() {
            if ((this.mFlags & 1) != 0) {
                return true;
            }
            InputEvent inputEvent = this.mEvent;
            return (inputEvent instanceof MotionEvent) && (inputEvent.isFromSource(2) || this.mEvent.isFromSource(4194304));
        }

        public boolean shouldSendToSynthesizer() {
            if ((this.mFlags & 32) != 0) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("QueuedInputEvent{flags=");
            boolean hasPrevious = flagToString("DELIVER_POST_IME", 1, false, sb);
            if (!flagToString("UNHANDLED", 32, flagToString("RESYNTHESIZED", 16, flagToString("FINISHED_HANDLED", 8, flagToString("FINISHED", 4, flagToString("DEFERRED", 2, hasPrevious, sb), sb), sb), sb), sb)) {
                sb.append("0");
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(", hasNextQueuedEvent=");
            String str = "true";
            sb2.append(this.mEvent != null ? str : "false");
            sb.append(sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append(", hasInputEventReceiver=");
            if (this.mReceiver == null) {
                str = "false";
            }
            sb3.append(str);
            sb.append(sb3.toString());
            sb.append(", mEvent=" + this.mEvent + "}");
            return sb.toString();
        }

        private boolean flagToString(String name, int flag, boolean hasPrevious, StringBuilder sb) {
            if ((this.mFlags & flag) != 0) {
                if (hasPrevious) {
                    sb.append("|");
                }
                sb.append(name);
                return true;
            }
            return hasPrevious;
        }
    }

    private QueuedInputEvent obtainQueuedInputEvent(InputEvent event, InputEventReceiver receiver, int flags) {
        QueuedInputEvent q = this.mQueuedInputEventPool;
        if (q != null) {
            this.mQueuedInputEventPoolSize--;
            this.mQueuedInputEventPool = q.mNext;
            q.mNext = null;
        } else {
            q = new QueuedInputEvent();
        }
        q.mEvent = event;
        q.mReceiver = receiver;
        q.mFlags = flags;
        return q;
    }

    private void recycleQueuedInputEvent(QueuedInputEvent q) {
        q.mEvent = null;
        q.mReceiver = null;
        int i = this.mQueuedInputEventPoolSize;
        if (i < 10) {
            this.mQueuedInputEventPoolSize = i + 1;
            q.mNext = this.mQueuedInputEventPool;
            this.mQueuedInputEventPool = q;
        }
    }

    void enqueueInputEvent(InputEvent event) {
        enqueueInputEvent(event, null, 0, false);
    }

    void enqueueInputEvent(InputEvent event, InputEventReceiver receiver, int flags, boolean processImmediately) {
        QueuedInputEvent q = obtainQueuedInputEvent(event, receiver, flags);
        if (event instanceof MotionEvent) {
            MotionEvent me = (MotionEvent) event;
            if (me.getAction() == 3) {
                EventLog.writeEvent((int) EventLogTags.VIEW_ENQUEUE_INPUT_EVENT, "Motion - Cancel", getTitle());
            }
        } else if (event instanceof KeyEvent) {
            KeyEvent ke = (KeyEvent) event;
            if (ke.isCanceled()) {
                EventLog.writeEvent((int) EventLogTags.VIEW_ENQUEUE_INPUT_EVENT, "Key - Cancel", getTitle());
            }
        }
        QueuedInputEvent last = this.mPendingInputEventTail;
        if (last == null) {
            this.mPendingInputEventHead = q;
            this.mPendingInputEventTail = q;
        } else {
            last.mNext = q;
            this.mPendingInputEventTail = q;
        }
        int i = this.mPendingInputEventCount + 1;
        this.mPendingInputEventCount = i;
        Trace.traceCounter(4L, this.mPendingInputEventQueueLengthCounterName, i);
        if (processImmediately) {
            doProcessInputEvents();
        } else {
            scheduleProcessInputEvents();
        }
    }

    private void scheduleProcessInputEvents() {
        if (!this.mProcessInputEventsScheduled) {
            this.mProcessInputEventsScheduled = true;
            Message msg = this.mHandler.obtainMessage(19);
            msg.setAsynchronous(true);
            this.mHandler.sendMessage(msg);
        }
    }

    void doProcessInputEvents() {
        BoostFramework.ScrollOptimizer.setBLASTBufferQueue(this.mBlastBufferQueue);
        while (this.mPendingInputEventHead != null) {
            QueuedInputEvent q = this.mPendingInputEventHead;
            QueuedInputEvent queuedInputEvent = q.mNext;
            this.mPendingInputEventHead = queuedInputEvent;
            if (queuedInputEvent == null) {
                this.mPendingInputEventTail = null;
            }
            q.mNext = null;
            int i = this.mPendingInputEventCount - 1;
            this.mPendingInputEventCount = i;
            Trace.traceCounter(4L, this.mPendingInputEventQueueLengthCounterName, i);
            this.mViewFrameInfo.setInputEvent(this.mInputEventAssigner.processEvent(q.mEvent));
            deliverInputEvent(q);
        }
        if (this.mProcessInputEventsScheduled) {
            this.mProcessInputEventsScheduled = false;
            this.mHandler.removeMessages(19);
        }
    }

    private void deliverInputEvent(QueuedInputEvent q) {
        InputStage stage;
        Trace.asyncTraceBegin(8L, "deliverInputEvent", q.mEvent.getId());
        if (Trace.isTagEnabled(8L)) {
            Trace.traceBegin(8L, "deliverInputEvent src=0x" + Integer.toHexString(q.mEvent.getSource()) + " eventTimeNano=" + q.mEvent.getEventTimeNano() + " id=0x" + Integer.toHexString(q.mEvent.getId()));
        }
        try {
            if (this.mInputEventConsistencyVerifier != null) {
                Trace.traceBegin(8L, "verifyEventConsistency");
                this.mInputEventConsistencyVerifier.onInputEvent(q.mEvent, 0);
                Trace.traceEnd(8L);
            }
            if (q.shouldSendToSynthesizer()) {
                stage = this.mSyntheticInputStage;
            } else {
                stage = q.shouldSkipIme() ? this.mFirstPostImeInputStage : this.mFirstInputStage;
            }
            if (q.mEvent instanceof KeyEvent) {
                Trace.traceBegin(8L, "preDispatchToUnhandledKeyManager");
                this.mUnhandledKeyManager.preDispatch((KeyEvent) q.mEvent);
                Trace.traceEnd(8L);
            }
            if (stage != null) {
                handleWindowFocusChanged();
                stage.deliver(q);
            } else {
                finishInputEvent(q);
            }
        } finally {
            Trace.traceEnd(8L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishInputEvent(QueuedInputEvent q) {
        Trace.asyncTraceEnd(8L, "deliverInputEvent", q.mEvent.getId());
        if (q.mReceiver != null) {
            boolean modified = true;
            boolean handled = (q.mFlags & 8) != 0;
            if ((q.mFlags & 64) == 0) {
                modified = false;
            }
            if (modified) {
                Trace.traceBegin(8L, "processInputEventBeforeFinish");
                try {
                    InputEvent processedEvent = this.mInputCompatProcessor.processInputEventBeforeFinish(q.mEvent);
                    if (processedEvent != null) {
                        q.mReceiver.finishInputEvent(processedEvent, handled);
                    }
                } finally {
                    Trace.traceEnd(8L);
                }
            } else {
                q.mReceiver.finishInputEvent(q.mEvent, handled);
            }
        } else {
            q.mEvent.recycleIfNeededAfterDispatch();
        }
        recycleQueuedInputEvent(q);
    }

    static boolean isTerminalInputEvent(InputEvent event) {
        if (event instanceof KeyEvent) {
            KeyEvent keyEvent = (KeyEvent) event;
            return keyEvent.getAction() == 1;
        }
        MotionEvent motionEvent = (MotionEvent) event;
        int action = motionEvent.getAction();
        return action == 1 || action == 3 || action == 10;
    }

    void scheduleConsumeBatchedInput() {
        if (!this.mConsumeBatchedInputScheduled && !this.mConsumeBatchedInputImmediatelyScheduled) {
            this.mConsumeBatchedInputScheduled = true;
            this.mChoreographer.postCallback(0, this.mConsumedBatchedInputRunnable, null);
        }
    }

    void unscheduleConsumeBatchedInput() {
        if (this.mConsumeBatchedInputScheduled) {
            this.mConsumeBatchedInputScheduled = false;
            this.mChoreographer.removeCallbacks(0, this.mConsumedBatchedInputRunnable, null);
        }
    }

    void scheduleConsumeBatchedInputImmediately() {
        if (!this.mConsumeBatchedInputImmediatelyScheduled) {
            unscheduleConsumeBatchedInput();
            this.mConsumeBatchedInputImmediatelyScheduled = true;
            this.mHandler.post(this.mConsumeBatchedInputImmediatelyRunnable);
        }
    }

    boolean doConsumeBatchedInput(long frameTimeNanos) {
        boolean consumedBatches;
        WindowInputEventReceiver windowInputEventReceiver = this.mInputEventReceiver;
        if (windowInputEventReceiver != null) {
            consumedBatches = windowInputEventReceiver.consumeBatchedInputEvents(frameTimeNanos);
        } else {
            consumedBatches = false;
        }
        doProcessInputEvents();
        return consumedBatches;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class TraversalRunnable implements Runnable {
        TraversalRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewRootImpl.this.doTraversal();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class WindowInputEventReceiver extends InputEventReceiver {
        public WindowInputEventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        @Override // android.view.InputEventReceiver
        public void onInputEvent(InputEvent event) {
            Trace.traceBegin(8L, "processInputEventForCompatibility");
            if (event instanceof MotionEvent) {
                MotionEvent motionEvent = (MotionEvent) event;
                if (WindowConfiguration.isNtWindowMode(ViewRootImpl.this.mLastWindowMode)) {
                    motionEvent.setRawDisplayOffset(ViewRootImpl.this.mWindowModeOffset[0], ViewRootImpl.this.mWindowModeOffset[1]);
                    motionEvent.setRawDisplayScale(ViewRootImpl.this.mWindowModeOffset[2], ViewRootImpl.this.mWindowModeOffset[3]);
                } else {
                    motionEvent.setRawDisplayOffset(0.0f, 0.0f);
                    motionEvent.setRawDisplayScale(1.0f, 1.0f);
                }
            }
            try {
                List<InputEvent> processedEvents = ViewRootImpl.this.mInputCompatProcessor.processInputEventForCompatibility(event);
                if (processedEvents != null) {
                    if (processedEvents.isEmpty()) {
                        finishInputEvent(event, true);
                        return;
                    }
                    for (int i = 0; i < processedEvents.size(); i++) {
                        ViewRootImpl.this.enqueueInputEvent(processedEvents.get(i), this, 64, true);
                    }
                    return;
                }
                ViewRootImpl.this.enqueueInputEvent(event, this, 0, true);
            } finally {
                Trace.traceEnd(8L);
            }
        }

        @Override // android.view.InputEventReceiver
        public void onBatchedInputEventPending(int source) {
            boolean unbuffered = ViewRootImpl.this.mUnbufferedInputDispatch || (ViewRootImpl.this.mUnbufferedInputSource & source) != 0;
            if (unbuffered) {
                if (ViewRootImpl.this.mConsumeBatchedInputScheduled) {
                    ViewRootImpl.this.unscheduleConsumeBatchedInput();
                }
                consumeBatchedInputEvents(-1L);
                return;
            }
            ViewRootImpl.this.scheduleConsumeBatchedInput();
        }

        @Override // android.view.InputEventReceiver
        public void onFocusEvent(boolean hasFocus, boolean inTouchMode) {
            ViewRootImpl.this.windowFocusChanged(hasFocus, inTouchMode);
        }

        @Override // android.view.InputEventReceiver
        public void onPointerCaptureEvent(boolean pointerCaptureEnabled) {
            ViewRootImpl.this.dispatchPointerCaptureChanged(pointerCaptureEnabled);
        }

        @Override // android.view.InputEventReceiver
        public void onDragEvent(boolean isExiting, float x, float y) {
            DragEvent event = DragEvent.obtain(isExiting ? 6 : 2, x, y, 0.0f, 0.0f, null, null, null, null, null, false);
            ViewRootImpl.this.dispatchDragEvent(event);
        }

        @Override // android.view.InputEventReceiver
        public void dispose() {
            ViewRootImpl.this.unscheduleConsumeBatchedInput();
            super.dispose();
        }
    }

    /* loaded from: classes3.dex */
    final class InputMetricsListener implements HardwareRendererObserver.OnFrameMetricsAvailableListener {
        public long[] data = new long[22];

        InputMetricsListener() {
        }

        @Override // android.graphics.HardwareRendererObserver.OnFrameMetricsAvailableListener
        public void onFrameMetricsAvailable(int dropCountSinceLastInvocation) {
            long[] jArr = this.data;
            int inputEventId = (int) jArr[4];
            if (inputEventId == 0) {
                return;
            }
            long presentTime = jArr[21];
            if (presentTime <= 0) {
                return;
            }
            long gpuCompletedTime = jArr[19];
            if (ViewRootImpl.this.mInputEventReceiver == null) {
                return;
            }
            if (gpuCompletedTime < presentTime) {
                ViewRootImpl.this.mInputEventReceiver.reportTimeline(inputEventId, gpuCompletedTime, presentTime);
                return;
            }
            double discrepancyMs = (gpuCompletedTime - presentTime) * 1.0E-6d;
            long vsyncId = this.data[1];
            Log.w(ViewRootImpl.TAG, "Not reporting timeline because gpuCompletedTime is " + discrepancyMs + "ms ahead of presentTime. FRAME_TIMELINE_VSYNC_ID=" + vsyncId + ", INPUT_EVENT_ID=" + inputEventId);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class ConsumeBatchedInputRunnable implements Runnable {
        ConsumeBatchedInputRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewRootImpl.this.mConsumeBatchedInputScheduled = false;
            ViewRootImpl viewRootImpl = ViewRootImpl.this;
            if (viewRootImpl.doConsumeBatchedInput(viewRootImpl.mChoreographer.getFrameTimeNanos())) {
                ViewRootImpl.this.scheduleConsumeBatchedInput();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class ConsumeBatchedInputImmediatelyRunnable implements Runnable {
        ConsumeBatchedInputImmediatelyRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ViewRootImpl.this.mConsumeBatchedInputImmediatelyScheduled = false;
            ViewRootImpl.this.doConsumeBatchedInput(-1L);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class InvalidateOnAnimationRunnable implements Runnable {
        private boolean mPosted;
        private View.AttachInfo.InvalidateInfo[] mTempViewRects;
        private View[] mTempViews;
        private final ArrayList<View> mViews = new ArrayList<>();
        private final ArrayList<View.AttachInfo.InvalidateInfo> mViewRects = new ArrayList<>();

        InvalidateOnAnimationRunnable() {
        }

        public void addView(View view) {
            synchronized (this) {
                this.mViews.add(view);
                postIfNeededLocked();
            }
        }

        public void addViewRect(View.AttachInfo.InvalidateInfo info) {
            synchronized (this) {
                this.mViewRects.add(info);
                postIfNeededLocked();
            }
        }

        public void removeView(View view) {
            synchronized (this) {
                this.mViews.remove(view);
                int i = this.mViewRects.size();
                while (true) {
                    int i2 = i - 1;
                    if (i <= 0) {
                        break;
                    }
                    View.AttachInfo.InvalidateInfo info = this.mViewRects.get(i2);
                    if (info.target == view) {
                        this.mViewRects.remove(i2);
                        info.recycle();
                    }
                    i = i2;
                }
                if (this.mPosted && this.mViews.isEmpty() && this.mViewRects.isEmpty()) {
                    ViewRootImpl.this.mChoreographer.removeCallbacks(1, this, null);
                    this.mPosted = false;
                }
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            int viewCount;
            int viewRectCount;
            synchronized (this) {
                this.mPosted = false;
                viewCount = this.mViews.size();
                if (viewCount != 0) {
                    ArrayList<View> arrayList = this.mViews;
                    View[] viewArr = this.mTempViews;
                    if (viewArr == null) {
                        viewArr = new View[viewCount];
                    }
                    this.mTempViews = (View[]) arrayList.toArray(viewArr);
                    this.mViews.clear();
                }
                viewRectCount = this.mViewRects.size();
                if (viewRectCount != 0) {
                    ArrayList<View.AttachInfo.InvalidateInfo> arrayList2 = this.mViewRects;
                    View.AttachInfo.InvalidateInfo[] invalidateInfoArr = this.mTempViewRects;
                    if (invalidateInfoArr == null) {
                        invalidateInfoArr = new View.AttachInfo.InvalidateInfo[viewRectCount];
                    }
                    this.mTempViewRects = (View.AttachInfo.InvalidateInfo[]) arrayList2.toArray(invalidateInfoArr);
                    this.mViewRects.clear();
                }
            }
            for (int i = 0; i < viewCount; i++) {
                this.mTempViews[i].invalidate();
                this.mTempViews[i] = null;
            }
            for (int i2 = 0; i2 < viewRectCount; i2++) {
                View.AttachInfo.InvalidateInfo info = this.mTempViewRects[i2];
                info.target.invalidate(info.left, info.top, info.right, info.bottom);
                info.recycle();
            }
        }

        private void postIfNeededLocked() {
            if (!this.mPosted) {
                ViewRootImpl.this.mChoreographer.postCallback(1, this, null);
                this.mPosted = true;
            }
        }
    }

    public void dispatchInvalidateDelayed(View view, long delayMilliseconds) {
        Message msg = this.mHandler.obtainMessage(1, view);
        this.mHandler.sendMessageDelayed(msg, delayMilliseconds);
    }

    public void dispatchInvalidateRectDelayed(View.AttachInfo.InvalidateInfo info, long delayMilliseconds) {
        Message msg = this.mHandler.obtainMessage(2, info);
        this.mHandler.sendMessageDelayed(msg, delayMilliseconds);
    }

    public void dispatchInvalidateOnAnimation(View view) {
        this.mInvalidateOnAnimationRunnable.addView(view);
    }

    public void dispatchInvalidateRectOnAnimation(View.AttachInfo.InvalidateInfo info) {
        this.mInvalidateOnAnimationRunnable.addViewRect(info);
    }

    public void cancelInvalidate(View view) {
        this.mHandler.removeMessages(1, view);
        this.mHandler.removeMessages(2, view);
        this.mInvalidateOnAnimationRunnable.removeView(view);
    }

    public void dispatchInputEvent(InputEvent event) {
        dispatchInputEvent(event, null);
    }

    public void dispatchInputEvent(InputEvent event, InputEventReceiver receiver) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = event;
        args.arg2 = receiver;
        Message msg = this.mHandler.obtainMessage(7, args);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void synthesizeInputEvent(InputEvent event) {
        Message msg = this.mHandler.obtainMessage(24, event);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void dispatchKeyFromIme(KeyEvent event) {
        Message msg = this.mHandler.obtainMessage(11, event);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void dispatchKeyFromAutofill(KeyEvent event) {
        Message msg = this.mHandler.obtainMessage(12, event);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void dispatchUnhandledInputEvent(InputEvent event) {
        if (event instanceof MotionEvent) {
            event = MotionEvent.obtain((MotionEvent) event);
        }
        synthesizeInputEvent(event);
    }

    public void dispatchAppVisibility(boolean visible) {
        Message msg = this.mHandler.obtainMessage(8);
        msg.arg1 = visible ? 1 : 0;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchGetNewSurface() {
        Message msg = this.mHandler.obtainMessage(9);
        this.mHandler.sendMessage(msg);
    }

    public void dispatchLocationInParentDisplayChanged(Point offset) {
        Message msg = this.mHandler.obtainMessage(33, offset.x, offset.y);
        this.mHandler.sendMessage(msg);
    }

    public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) {
        synchronized (this) {
            this.mWindowFocusChanged = true;
            this.mUpcomingWindowFocus = hasFocus;
            this.mUpcomingInTouchMode = inTouchMode;
        }
        Message msg = Message.obtain();
        msg.what = 6;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchWindowShown() {
        this.mHandler.sendEmptyMessage(25);
    }

    public void dispatchCloseSystemDialogs(String reason) {
        Message msg = Message.obtain();
        msg.what = 14;
        msg.obj = reason;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchDragEvent(DragEvent event) {
        int what;
        if (event.getAction() == 2) {
            what = 16;
            this.mHandler.removeMessages(16);
        } else {
            what = 15;
        }
        Message msg = this.mHandler.obtainMessage(what, event);
        this.mHandler.sendMessage(msg);
    }

    public void updatePointerIcon(float x, float y) {
        this.mHandler.removeMessages(27);
        long now = SystemClock.uptimeMillis();
        MotionEvent event = MotionEvent.obtain(0L, now, 7, x, y, 0);
        Message msg = this.mHandler.obtainMessage(27, event);
        this.mHandler.sendMessage(msg);
    }

    public void dispatchCheckFocus() {
        if (!this.mHandler.hasMessages(13)) {
            this.mHandler.sendEmptyMessage(13);
        }
    }

    public void dispatchRequestKeyboardShortcuts(IResultReceiver receiver, int deviceId) {
        this.mHandler.obtainMessage(26, deviceId, 0, receiver).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchPointerCaptureChanged(boolean on) {
        this.mHandler.removeMessages(28);
        Message msg = this.mHandler.obtainMessage(28);
        msg.arg1 = on ? 1 : 0;
        this.mHandler.sendMessage(msg);
    }

    private void postSendWindowContentChangedCallback(View source, int changeType) {
        if (this.mSendWindowContentChangedAccessibilityEvent == null) {
            this.mSendWindowContentChangedAccessibilityEvent = new SendWindowContentChangedAccessibilityEvent();
        }
        this.mSendWindowContentChangedAccessibilityEvent.runOrPost(source, changeType);
    }

    private void removeSendWindowContentChangedCallback() {
        SendWindowContentChangedAccessibilityEvent sendWindowContentChangedAccessibilityEvent = this.mSendWindowContentChangedAccessibilityEvent;
        if (sendWindowContentChangedAccessibilityEvent != null) {
            this.mHandler.removeCallbacks(sendWindowContentChangedAccessibilityEvent);
        }
    }

    @Override // android.view.ViewParent
    public boolean showContextMenuForChild(View originalView) {
        return false;
    }

    @Override // android.view.ViewParent
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return false;
    }

    @Override // android.view.ViewParent
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        return null;
    }

    @Override // android.view.ViewParent
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback, int type) {
        return null;
    }

    @Override // android.view.ViewParent
    public void createContextMenu(ContextMenu menu) {
    }

    @Override // android.view.ViewParent
    public void childDrawableStateChanged(View child) {
    }

    @Override // android.view.ViewParent
    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        AccessibilityNodeProvider provider;
        SendWindowContentChangedAccessibilityEvent sendWindowContentChangedAccessibilityEvent;
        if (this.mView == null || this.mStopped || this.mPausedForTransition) {
            return false;
        }
        if (event.getEventType() != 2048 && (sendWindowContentChangedAccessibilityEvent = this.mSendWindowContentChangedAccessibilityEvent) != null && sendWindowContentChangedAccessibilityEvent.mSource != null) {
            this.mSendWindowContentChangedAccessibilityEvent.removeCallbacksAndRun();
        }
        int eventType = event.getEventType();
        View source = getSourceForAccessibilityEvent(event);
        switch (eventType) {
            case 2048:
                handleWindowContentChangedEvent(event);
                break;
            case 32768:
                if (source != null && (provider = source.getAccessibilityNodeProvider()) != null) {
                    int virtualNodeId = AccessibilityNodeInfo.getVirtualDescendantId(event.getSourceNodeId());
                    AccessibilityNodeInfo node = provider.createAccessibilityNodeInfo(virtualNodeId);
                    setAccessibilityFocus(source, node);
                    break;
                }
                break;
            case 65536:
                if (source != null && source.getAccessibilityNodeProvider() != null) {
                    setAccessibilityFocus(null, null);
                    break;
                }
                break;
        }
        this.mAccessibilityManager.sendAccessibilityEvent(event);
        return true;
    }

    private View getSourceForAccessibilityEvent(AccessibilityEvent event) {
        long sourceNodeId = event.getSourceNodeId();
        int accessibilityViewId = AccessibilityNodeInfo.getAccessibilityViewId(sourceNodeId);
        return AccessibilityNodeIdManager.getInstance().findView(accessibilityViewId);
    }

    private void handleWindowContentChangedEvent(AccessibilityEvent event) {
        View focusedHost = this.mAccessibilityFocusedHost;
        if (focusedHost == null || this.mAccessibilityFocusedVirtualView == null) {
            return;
        }
        AccessibilityNodeProvider provider = focusedHost.getAccessibilityNodeProvider();
        if (provider == null) {
            this.mAccessibilityFocusedHost = null;
            this.mAccessibilityFocusedVirtualView = null;
            focusedHost.clearAccessibilityFocusNoCallbacks(0);
            return;
        }
        int changes = event.getContentChangeTypes();
        if ((changes & 1) == 0 && changes != 0) {
            return;
        }
        long eventSourceNodeId = event.getSourceNodeId();
        int changedViewId = AccessibilityNodeInfo.getAccessibilityViewId(eventSourceNodeId);
        boolean hostInSubtree = false;
        View root = this.mAccessibilityFocusedHost;
        while (root != null && !hostInSubtree) {
            if (changedViewId == root.getAccessibilityViewId()) {
                hostInSubtree = true;
            } else {
                ViewParent parent = root.getParent();
                if (parent instanceof View) {
                    root = (View) parent;
                } else {
                    root = null;
                }
            }
        }
        if (!hostInSubtree) {
            return;
        }
        long focusedSourceNodeId = this.mAccessibilityFocusedVirtualView.getSourceNodeId();
        int focusedChildId = AccessibilityNodeInfo.getVirtualDescendantId(focusedSourceNodeId);
        Rect oldBounds = this.mTempRect;
        this.mAccessibilityFocusedVirtualView.getBoundsInScreen(oldBounds);
        AccessibilityNodeInfo createAccessibilityNodeInfo = provider.createAccessibilityNodeInfo(focusedChildId);
        this.mAccessibilityFocusedVirtualView = createAccessibilityNodeInfo;
        if (createAccessibilityNodeInfo == null) {
            this.mAccessibilityFocusedHost = null;
            focusedHost.clearAccessibilityFocusNoCallbacks(0);
            provider.performAction(focusedChildId, AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
            invalidateRectOnScreen(oldBounds);
            return;
        }
        Rect newBounds = createAccessibilityNodeInfo.getBoundsInScreen();
        if (!oldBounds.equals(newBounds)) {
            oldBounds.union(newBounds);
            invalidateRectOnScreen(oldBounds);
        }
    }

    @Override // android.view.ViewParent
    public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
        postSendWindowContentChangedCallback((View) Preconditions.checkNotNull(source), changeType);
    }

    @Override // android.view.ViewParent
    public boolean canResolveLayoutDirection() {
        return true;
    }

    @Override // android.view.ViewParent
    public boolean isLayoutDirectionResolved() {
        return true;
    }

    @Override // android.view.ViewParent
    public int getLayoutDirection() {
        return 0;
    }

    @Override // android.view.ViewParent
    public boolean canResolveTextDirection() {
        return true;
    }

    @Override // android.view.ViewParent
    public boolean isTextDirectionResolved() {
        return true;
    }

    @Override // android.view.ViewParent
    public int getTextDirection() {
        return 1;
    }

    @Override // android.view.ViewParent
    public boolean canResolveTextAlignment() {
        return true;
    }

    @Override // android.view.ViewParent
    public boolean isTextAlignmentResolved() {
        return true;
    }

    @Override // android.view.ViewParent
    public int getTextAlignment() {
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public View getCommonPredecessor(View first, View second) {
        if (this.mTempHashSet == null) {
            this.mTempHashSet = new HashSet<>();
        }
        HashSet<View> seen = this.mTempHashSet;
        seen.clear();
        View firstCurrent = first;
        while (firstCurrent != null) {
            seen.add(firstCurrent);
            ViewParent firstCurrentParent = firstCurrent.mParent;
            if (firstCurrentParent instanceof View) {
                firstCurrent = (View) firstCurrentParent;
            } else {
                firstCurrent = null;
            }
        }
        View secondCurrent = second;
        while (secondCurrent != null) {
            if (seen.contains(secondCurrent)) {
                seen.clear();
                return secondCurrent;
            }
            ViewParent secondCurrentParent = secondCurrent.mParent;
            if (secondCurrentParent instanceof View) {
                secondCurrent = (View) secondCurrentParent;
            } else {
                secondCurrent = null;
            }
        }
        seen.clear();
        return null;
    }

    void checkThread() {
        if (this.mThread != Thread.currentThread()) {
            throw new CalledFromWrongThreadException("Only the original thread that created a view hierarchy can touch its views.");
        }
    }

    @Override // android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    @Override // android.view.ViewParent
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        if (rectangle == null) {
            return scrollToRectOrFocus(null, immediate);
        }
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        boolean scrolled = scrollToRectOrFocus(rectangle, immediate);
        this.mTempRect.set(rectangle);
        this.mTempRect.offset(0, -this.mCurScrollY);
        this.mTempRect.offset(this.mAttachInfo.mWindowLeft, this.mAttachInfo.mWindowTop);
        try {
            this.mWindowSession.onRectangleOnScreenRequested(this.mWindow, this.mTempRect);
        } catch (RemoteException e) {
        }
        return scrolled;
    }

    @Override // android.view.ViewParent
    public void childHasTransientStateChanged(View child, boolean hasTransientState) {
    }

    @Override // android.view.ViewParent
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

    @Override // android.view.ViewParent
    public void onStopNestedScroll(View target) {
    }

    @Override // android.view.ViewParent
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }

    @Override // android.view.ViewParent
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    @Override // android.view.ViewParent
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    }

    @Override // android.view.ViewParent
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override // android.view.ViewParent
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override // android.view.ViewParent
    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        return false;
    }

    public void addScrollCaptureCallback(ScrollCaptureCallback callback) {
        if (this.mRootScrollCaptureCallbacks == null) {
            this.mRootScrollCaptureCallbacks = new HashSet<>();
        }
        this.mRootScrollCaptureCallbacks.add(callback);
    }

    public void removeScrollCaptureCallback(ScrollCaptureCallback callback) {
        HashSet<ScrollCaptureCallback> hashSet = this.mRootScrollCaptureCallbacks;
        if (hashSet != null) {
            hashSet.remove(callback);
            if (this.mRootScrollCaptureCallbacks.isEmpty()) {
                this.mRootScrollCaptureCallbacks = null;
            }
        }
    }

    public void dispatchScrollCaptureRequest(IScrollCaptureResponseListener listener) {
        this.mHandler.obtainMessage(36, listener).sendToTarget();
    }

    private void collectRootScrollCaptureTargets(ScrollCaptureSearchResults results) {
        HashSet<ScrollCaptureCallback> hashSet = this.mRootScrollCaptureCallbacks;
        if (hashSet == null) {
            return;
        }
        Iterator<ScrollCaptureCallback> it = hashSet.iterator();
        while (it.hasNext()) {
            ScrollCaptureCallback cb = it.next();
            Point offset = new Point(this.mView.getLeft(), this.mView.getTop());
            Rect rect = new Rect(0, 0, this.mView.getWidth(), this.mView.getHeight());
            results.addTarget(new ScrollCaptureTarget(this.mView, rect, offset, cb));
        }
    }

    public void setScrollCaptureRequestTimeout(int timeMillis) {
        this.mScrollCaptureRequestTimeout = timeMillis;
    }

    public long getScrollCaptureRequestTimeout() {
        return this.mScrollCaptureRequestTimeout;
    }

    public void handleScrollCaptureRequest(final IScrollCaptureResponseListener listener) {
        final ScrollCaptureSearchResults results = new ScrollCaptureSearchResults(this.mContext.getMainExecutor());
        collectRootScrollCaptureTargets(results);
        View rootView = getView();
        if (rootView != null) {
            Point point = new Point();
            Rect rect = new Rect(0, 0, rootView.getWidth(), rootView.getHeight());
            getChildVisibleRect(rootView, rect, point);
            Objects.requireNonNull(results);
            rootView.dispatchScrollCaptureSearch(rect, point, new Consumer() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda13
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ScrollCaptureSearchResults.this.addTarget((ScrollCaptureTarget) obj);
                }
            });
        }
        Runnable onComplete = new Runnable() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ViewRootImpl.this.lambda$handleScrollCaptureRequest$8$ViewRootImpl(listener, results);
            }
        };
        results.setOnCompleteListener(onComplete);
        if (!results.isComplete()) {
            ViewRootHandler viewRootHandler = this.mHandler;
            Objects.requireNonNull(results);
            viewRootHandler.postDelayed(new Runnable() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ScrollCaptureSearchResults.this.finish();
                }
            }, getScrollCaptureRequestTimeout());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: dispatchScrollCaptureSearchResponse */
    public void lambda$handleScrollCaptureRequest$8$ViewRootImpl(IScrollCaptureResponseListener listener, ScrollCaptureSearchResults results) {
        ScrollCaptureTarget selectedTarget = results.getTopResult();
        ScrollCaptureResponse.Builder response = new ScrollCaptureResponse.Builder();
        response.setWindowTitle(getTitle().toString());
        response.setPackageName(this.mContext.getPackageName());
        StringWriter writer = new StringWriter();
        IndentingPrintWriter pw = new IndentingPrintWriter(writer);
        results.dump(pw);
        pw.flush();
        response.addMessage(writer.toString());
        if (selectedTarget == null) {
            response.setDescription("No scrollable targets found in window");
            try {
                listener.onScrollCaptureResponse(response.build());
                return;
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to send scroll capture search result", e);
                return;
            }
        }
        response.setDescription("Connected");
        Rect boundsInWindow = new Rect();
        View containingView = selectedTarget.getContainingView();
        containingView.getLocationInWindow(this.mAttachInfo.mTmpLocation);
        boundsInWindow.set(selectedTarget.getScrollBounds());
        boundsInWindow.offset(this.mAttachInfo.mTmpLocation[0], this.mAttachInfo.mTmpLocation[1]);
        response.setBoundsInWindow(boundsInWindow);
        Rect boundsOnScreen = new Rect();
        this.mView.getLocationOnScreen(this.mAttachInfo.mTmpLocation);
        boundsOnScreen.set(0, 0, this.mView.getWidth(), this.mView.getHeight());
        boundsOnScreen.offset(this.mAttachInfo.mTmpLocation[0], this.mAttachInfo.mTmpLocation[1]);
        response.setWindowBounds(boundsOnScreen);
        ScrollCaptureConnection connection = new ScrollCaptureConnection(this.mView.getContext().getMainExecutor(), selectedTarget);
        response.setConnection(connection);
        try {
            listener.onScrollCaptureResponse(response.build());
        } catch (RemoteException e2) {
            if (DEBUG_SCROLL_CAPTURE) {
                Log.w(TAG, "Failed to send scroll capture search response.", e2);
            }
            connection.close();
        }
    }

    private void reportNextDraw() {
        if (!this.mReportNextDraw) {
            drawPending();
        }
        this.mReportNextDraw = true;
    }

    public void setReportNextDraw() {
        reportNextDraw();
        invalidate();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void changeCanvasOpacity(boolean opaque) {
        String str = this.mTag;
        Log.d(str, "changeCanvasOpacity: opaque=" + opaque);
        boolean opaque2 = opaque & ((this.mView.mPrivateFlags & 512) == 0);
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.setOpaque(opaque2);
        }
    }

    public boolean dispatchUnhandledKeyEvent(KeyEvent event) {
        return this.mUnhandledKeyManager.dispatch(this.mView, event);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public class TakenSurfaceHolder extends BaseSurfaceHolder {
        TakenSurfaceHolder() {
        }

        @Override // com.android.internal.view.BaseSurfaceHolder
        public boolean onAllowLockCanvas() {
            return ViewRootImpl.this.mDrawingAllowed;
        }

        @Override // com.android.internal.view.BaseSurfaceHolder
        public void onRelayoutContainer() {
        }

        @Override // com.android.internal.view.BaseSurfaceHolder, android.view.SurfaceHolder
        public void setFormat(int format) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceFormat(format);
        }

        @Override // com.android.internal.view.BaseSurfaceHolder, android.view.SurfaceHolder
        public void setType(int type) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceType(type);
        }

        @Override // com.android.internal.view.BaseSurfaceHolder
        public void onUpdateSurface() {
            throw new IllegalStateException("Shouldn't be here");
        }

        @Override // android.view.SurfaceHolder
        public boolean isCreating() {
            return ViewRootImpl.this.mIsCreating;
        }

        @Override // com.android.internal.view.BaseSurfaceHolder, android.view.SurfaceHolder
        public void setFixedSize(int width, int height) {
            throw new UnsupportedOperationException("Currently only support sizing from layout");
        }

        @Override // android.view.SurfaceHolder
        public void setKeepScreenOn(boolean screenOn) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceKeepScreenOn(screenOn);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static class W extends IWindow.Stub {
        private final WeakReference<ViewRootImpl> mViewAncestor;
        private final IWindowSession mWindowSession;

        W(ViewRootImpl viewAncestor) {
            this.mViewAncestor = new WeakReference<>(viewAncestor);
            this.mWindowSession = viewAncestor.mWindowSession;
        }

        @Override // android.view.IWindow
        public void resized(ClientWindowFrames frames, boolean reportDraw, MergedConfiguration mergedConfiguration, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchResized(frames, reportDraw, mergedConfiguration, forceLayout, alwaysConsumeSystemBars, displayId);
            }
        }

        @Override // android.view.IWindow
        public void locationInParentDisplayChanged(Point offset) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchLocationInParentDisplayChanged(offset);
            }
        }

        @Override // android.view.IWindow
        public void insetsChanged(InsetsState insetsState, boolean willMove, boolean willResize) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchInsetsChanged(insetsState, willMove, willResize);
            }
        }

        @Override // android.view.IWindow
        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] activeControls, boolean willMove, boolean willResize) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchInsetsControlChanged(insetsState, activeControls, willMove, willResize);
            }
        }

        @Override // android.view.IWindow
        public void showInsets(int types, boolean fromIme) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (fromIme) {
                ImeTracing.getInstance().triggerClientDump("ViewRootImpl.W#showInsets", viewAncestor.getInsetsController().getHost().getInputMethodManager(), null);
            }
            if (viewAncestor != null) {
                viewAncestor.showInsets(types, fromIme);
            }
        }

        @Override // android.view.IWindow
        public void hideInsets(int types, boolean fromIme) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (fromIme) {
                ImeTracing.getInstance().triggerClientDump("ViewRootImpl.W#hideInsets", viewAncestor.getInsetsController().getHost().getInputMethodManager(), null);
            }
            if (viewAncestor != null) {
                viewAncestor.hideInsets(types, fromIme);
            }
        }

        @Override // android.view.IWindow
        public void moved(int newX, int newY) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchMoved(newX, newY);
            }
        }

        @Override // android.view.IWindow
        public void dispatchAppVisibility(boolean visible) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchAppVisibility(visible);
            }
        }

        @Override // android.view.IWindow
        public void dispatchGetNewSurface() {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchGetNewSurface();
            }
        }

        @Override // android.view.IWindow
        public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.windowFocusChanged(hasFocus, inTouchMode);
            }
        }

        private static int checkCallingPermission(String permission) {
            try {
                return ActivityManager.getService().checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid());
            } catch (RemoteException e) {
                return -1;
            }
        }

        @Override // android.view.IWindow
        public void executeCommand(String command, String parameters, ParcelFileDescriptor out) {
            View view;
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null && (view = viewAncestor.mView) != null) {
                if (checkCallingPermission(Manifest.permission.DUMP) != 0) {
                    throw new SecurityException("Insufficient permissions to invoke executeCommand() from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid());
                }
                OutputStream clientStream = null;
                try {
                    try {
                        try {
                            clientStream = new ParcelFileDescriptor.AutoCloseOutputStream(out);
                            ViewDebug.dispatchCommand(view, command, parameters, clientStream);
                            clientStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            if (clientStream != null) {
                                clientStream.close();
                            }
                        }
                    } catch (Throwable th) {
                        if (clientStream != null) {
                            try {
                                clientStream.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        }

        @Override // android.view.IWindow
        public void closeSystemDialogs(String reason) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchCloseSystemDialogs(reason);
            }
        }

        @Override // android.view.IWindow
        public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, float zoom, boolean sync) {
            if (sync) {
                try {
                    this.mWindowSession.wallpaperOffsetsComplete(asBinder());
                } catch (RemoteException e) {
                }
            }
        }

        @Override // android.view.IWindow
        public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) {
            if (sync) {
                try {
                    this.mWindowSession.wallpaperCommandComplete(asBinder(), null);
                } catch (RemoteException e) {
                }
            }
        }

        @Override // android.view.IWindow
        public void dispatchDragEvent(DragEvent event) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchDragEvent(event);
            }
        }

        @Override // android.view.IWindow
        public void updatePointerIcon(float x, float y) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.updatePointerIcon(x, y);
            }
        }

        @Override // android.view.IWindow
        public void dispatchWindowShown() {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchWindowShown();
            }
        }

        @Override // android.view.IWindow
        public void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchRequestKeyboardShortcuts(receiver, deviceId);
            }
        }

        @Override // android.view.IWindow
        public void requestScrollCapture(IScrollCaptureResponseListener listener) {
            ViewRootImpl viewAncestor = this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchScrollCaptureRequest(listener);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static final class CalledFromWrongThreadException extends AndroidRuntimeException {
        public CalledFromWrongThreadException(String msg) {
            super(msg);
        }
    }

    static HandlerActionQueue getRunQueue() {
        ThreadLocal<HandlerActionQueue> threadLocal = sRunQueues;
        HandlerActionQueue rq = threadLocal.get();
        if (rq != null) {
            return rq;
        }
        HandlerActionQueue rq2 = new HandlerActionQueue();
        threadLocal.set(rq2);
        return rq2;
    }

    private void startDragResizing(Rect initialBounds, boolean fullscreen, Rect systemInsets, Rect stableInsets, int resizeMode) {
        if (!this.mDragResizing) {
            this.mDragResizing = true;
            if (this.mUseMTRenderer) {
                for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                    this.mWindowCallbacks.get(i).onWindowDragResizeStart(initialBounds, fullscreen, systemInsets, stableInsets, resizeMode);
                }
            }
            this.mFullRedrawNeeded = true;
        }
    }

    private void endDragResizing() {
        if (this.mDragResizing) {
            this.mDragResizing = false;
            if (this.mUseMTRenderer) {
                for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                    this.mWindowCallbacks.get(i).onWindowDragResizeEnd();
                }
            }
            this.mFullRedrawNeeded = true;
        }
    }

    private boolean updateContentDrawBounds() {
        boolean updated = false;
        boolean z = true;
        if (this.mUseMTRenderer) {
            for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                updated |= this.mWindowCallbacks.get(i).onContentDrawn(this.mWindowAttributes.surfaceInsets.left, this.mWindowAttributes.surfaceInsets.top, this.mWidth, this.mHeight);
            }
        }
        if (!this.mDragResizing || !this.mReportNextDraw) {
            z = false;
        }
        return updated | z;
    }

    private void requestDrawWindow() {
        if (!this.mUseMTRenderer) {
            return;
        }
        this.mWindowDrawCountDown = new CountDownLatch(this.mWindowCallbacks.size());
        for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
            this.mWindowCallbacks.get(i).onRequestDraw(this.mReportNextDraw);
        }
    }

    public void reportActivityRelaunched() {
        this.mActivityRelaunched = true;
    }

    public SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }

    public IBinder getInputToken() {
        WindowInputEventReceiver windowInputEventReceiver = this.mInputEventReceiver;
        if (windowInputEventReceiver == null) {
            return null;
        }
        return windowInputEventReceiver.getToken();
    }

    public IBinder getWindowToken() {
        return this.mAttachInfo.mWindowToken;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class AccessibilityInteractionConnectionManager implements AccessibilityManager.AccessibilityStateChangeListener {
        AccessibilityInteractionConnectionManager() {
        }

        @Override // android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener
        public void onAccessibilityStateChanged(boolean enabled) {
            if (enabled) {
                ensureConnection();
                if (ViewRootImpl.this.mAttachInfo.mHasWindowFocus && ViewRootImpl.this.mView != null) {
                    ViewRootImpl.this.mView.sendAccessibilityEvent(32);
                    View focusedView = ViewRootImpl.this.mView.findFocus();
                    if (focusedView != null && focusedView != ViewRootImpl.this.mView) {
                        focusedView.sendAccessibilityEvent(8);
                    }
                }
                if (ViewRootImpl.this.mAttachInfo.mLeashedParentToken != null) {
                    ViewRootImpl.this.mAccessibilityManager.associateEmbeddedHierarchy(ViewRootImpl.this.mAttachInfo.mLeashedParentToken, ViewRootImpl.this.mLeashToken);
                    return;
                }
                return;
            }
            ensureNoConnection();
            ViewRootImpl.this.mHandler.obtainMessage(21).sendToTarget();
        }

        public void ensureConnection() {
            boolean registered = ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId != -1;
            if (!registered) {
                ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId = ViewRootImpl.this.mAccessibilityManager.addAccessibilityInteractionConnection(ViewRootImpl.this.mWindow, ViewRootImpl.this.mLeashToken, ViewRootImpl.this.mContext.getPackageName(), new AccessibilityInteractionConnection(ViewRootImpl.this));
            }
        }

        public void ensureNoConnection() {
            boolean registered = ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId != -1;
            if (registered) {
                ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId = -1;
                ViewRootImpl.this.mAccessibilityManager.removeAccessibilityInteractionConnection(ViewRootImpl.this.mWindow);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public final class HighContrastTextManager implements AccessibilityManager.HighTextContrastChangeListener {
        HighContrastTextManager() {
            ThreadedRenderer.setHighContrastText(ViewRootImpl.this.mAccessibilityManager.isHighTextContrastEnabled());
        }

        @Override // android.view.accessibility.AccessibilityManager.HighTextContrastChangeListener
        public void onHighTextContrastStateChanged(boolean enabled) {
            ThreadedRenderer.setHighContrastText(enabled);
            ViewRootImpl.this.destroyHardwareResources();
            ViewRootImpl.this.invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes3.dex */
    public static final class AccessibilityInteractionConnection extends IAccessibilityInteractionConnection.Stub {
        private final WeakReference<ViewRootImpl> mViewRootImpl;

        AccessibilityInteractionConnection(ViewRootImpl viewRootImpl) {
            this.mViewRootImpl = new WeakReference<>(viewRootImpl);
        }

        @Override // android.view.accessibility.IAccessibilityInteractionConnection
        public void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec, Bundle args) {
            ViewRootImpl viewRootImpl = this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfoByAccessibilityIdClientThread(accessibilityNodeId, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec, args);
                return;
            }
            try {
                callback.setFindAccessibilityNodeInfosResult(null, interactionId);
            } catch (RemoteException e) {
            }
        }

        @Override // android.view.accessibility.IAccessibilityInteractionConnection
        public void performAccessibilityAction(long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) {
            ViewRootImpl viewRootImpl = this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().performAccessibilityActionClientThread(accessibilityNodeId, action, arguments, interactionId, callback, flags, interrogatingPid, interrogatingTid);
                return;
            }
            try {
                callback.setPerformAccessibilityActionResult(false, interactionId);
            } catch (RemoteException e) {
            }
        }

        @Override // android.view.accessibility.IAccessibilityInteractionConnection
        public void findAccessibilityNodeInfosByViewId(long accessibilityNodeId, String viewId, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByViewIdClientThread(accessibilityNodeId, viewId, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
                return;
            }
            try {
                callback.setFindAccessibilityNodeInfoResult(null, interactionId);
            } catch (RemoteException e) {
            }
        }

        @Override // android.view.accessibility.IAccessibilityInteractionConnection
        public void findAccessibilityNodeInfosByText(long accessibilityNodeId, String text, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByTextClientThread(accessibilityNodeId, text, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
                return;
            }
            try {
                callback.setFindAccessibilityNodeInfosResult(null, interactionId);
            } catch (RemoteException e) {
            }
        }

        @Override // android.view.accessibility.IAccessibilityInteractionConnection
        public void findFocus(long accessibilityNodeId, int focusType, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().findFocusClientThread(accessibilityNodeId, focusType, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
                return;
            }
            try {
                callback.setFindAccessibilityNodeInfoResult(null, interactionId);
            } catch (RemoteException e) {
            }
        }

        @Override // android.view.accessibility.IAccessibilityInteractionConnection
        public void focusSearch(long accessibilityNodeId, int direction, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().focusSearchClientThread(accessibilityNodeId, direction, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
                return;
            }
            try {
                callback.setFindAccessibilityNodeInfoResult(null, interactionId);
            } catch (RemoteException e) {
            }
        }

        @Override // android.view.accessibility.IAccessibilityInteractionConnection
        public void clearAccessibilityFocus() {
            ViewRootImpl viewRootImpl = this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().clearAccessibilityFocusClientThread();
            }
        }

        @Override // android.view.accessibility.IAccessibilityInteractionConnection
        public void notifyOutsideTouch() {
            ViewRootImpl viewRootImpl = this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().notifyOutsideTouchClientThread();
            }
        }
    }

    public IAccessibilityEmbeddedConnection getAccessibilityEmbeddedConnection() {
        if (this.mAccessibilityEmbeddedConnection == null) {
            this.mAccessibilityEmbeddedConnection = new AccessibilityEmbeddedConnection(this);
        }
        return this.mAccessibilityEmbeddedConnection;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SendWindowContentChangedAccessibilityEvent implements Runnable {
        private int mChangeTypes;
        public long mLastEventTimeMillis;
        public StackTraceElement[] mOrigin;
        public View mSource;

        private SendWindowContentChangedAccessibilityEvent() {
            this.mChangeTypes = 0;
        }

        @Override // java.lang.Runnable
        public void run() {
            View source = this.mSource;
            this.mSource = null;
            if (source == null) {
                Log.e(ViewRootImpl.TAG, "Accessibility content change has no source");
                return;
            }
            if (AccessibilityManager.getInstance(ViewRootImpl.this.mContext).isEnabled()) {
                this.mLastEventTimeMillis = SystemClock.uptimeMillis();
                AccessibilityEvent event = AccessibilityEvent.obtain();
                event.setEventType(2048);
                event.setContentChangeTypes(this.mChangeTypes);
                source.sendAccessibilityEventUnchecked(event);
            } else {
                this.mLastEventTimeMillis = 0L;
            }
            source.resetSubtreeAccessibilityStateChanged();
            this.mChangeTypes = 0;
        }

        public void runOrPost(View source, int changeType) {
            if (ViewRootImpl.this.mHandler.getLooper() != Looper.myLooper()) {
                CalledFromWrongThreadException e = new CalledFromWrongThreadException("Only the original thread that created a view hierarchy can touch its views.");
                Log.e(ViewRootImpl.TAG, "Accessibility content change on non-UI thread. Future Android versions will throw an exception.", e);
                ViewRootImpl.this.mHandler.removeCallbacks(this);
                if (this.mSource != null) {
                    run();
                }
            }
            View view = this.mSource;
            if (view != null) {
                View predecessor = ViewRootImpl.this.getCommonPredecessor(view, source);
                if (predecessor != null) {
                    predecessor = predecessor.getSelfOrParentImportantForA11y();
                }
                this.mSource = predecessor != null ? predecessor : source;
                this.mChangeTypes |= changeType;
                return;
            }
            this.mSource = source;
            this.mChangeTypes = changeType;
            long timeSinceLastMillis = SystemClock.uptimeMillis() - this.mLastEventTimeMillis;
            long minEventIntevalMillis = ViewConfiguration.getSendRecurringAccessibilityEventsInterval();
            if (timeSinceLastMillis >= minEventIntevalMillis) {
                removeCallbacksAndRun();
            } else {
                ViewRootImpl.this.mHandler.postDelayed(this, minEventIntevalMillis - timeSinceLastMillis);
            }
        }

        public void removeCallbacksAndRun() {
            ViewRootImpl.this.mHandler.removeCallbacks(this);
            run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class UnhandledKeyManager {
        private final SparseArray<WeakReference<View>> mCapturedKeys;
        private WeakReference<View> mCurrentReceiver;
        private boolean mDispatched;

        private UnhandledKeyManager() {
            this.mDispatched = true;
            this.mCapturedKeys = new SparseArray<>();
            this.mCurrentReceiver = null;
        }

        boolean dispatch(View root, KeyEvent event) {
            if (this.mDispatched) {
                return false;
            }
            try {
                Trace.traceBegin(8L, "UnhandledKeyEvent dispatch");
                this.mDispatched = true;
                View consumer = root.dispatchUnhandledKeyEvent(event);
                if (event.getAction() == 0) {
                    int keycode = event.getKeyCode();
                    if (consumer != null && !KeyEvent.isModifierKey(keycode)) {
                        this.mCapturedKeys.put(keycode, new WeakReference<>(consumer));
                    }
                }
                return consumer != null;
            } finally {
                Trace.traceEnd(8L);
            }
        }

        void preDispatch(KeyEvent event) {
            int idx;
            this.mCurrentReceiver = null;
            if (event.getAction() == 1 && (idx = this.mCapturedKeys.indexOfKey(event.getKeyCode())) >= 0) {
                this.mCurrentReceiver = this.mCapturedKeys.valueAt(idx);
                this.mCapturedKeys.removeAt(idx);
            }
        }

        boolean preViewDispatch(KeyEvent event) {
            this.mDispatched = false;
            if (this.mCurrentReceiver == null) {
                this.mCurrentReceiver = this.mCapturedKeys.get(event.getKeyCode());
            }
            WeakReference<View> weakReference = this.mCurrentReceiver;
            if (weakReference == null) {
                return false;
            }
            View target = weakReference.get();
            if (event.getAction() == 1) {
                this.mCurrentReceiver = null;
            }
            if (target != null && target.isAttachedToWindow()) {
                target.onUnhandledKeyEvent(event);
            }
            return true;
        }
    }

    public void dispatchBlurRegions(float[][] regionCopy, long frameNumber) {
        BLASTBufferQueue bLASTBufferQueue;
        SurfaceControl surfaceControl = getSurfaceControl();
        if (!surfaceControl.isValid()) {
            return;
        }
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        transaction.setBlurRegions(surfaceControl, regionCopy);
        if (useBLAST() && (bLASTBufferQueue = this.mBlastBufferQueue) != null) {
            bLASTBufferQueue.mergeWithNextTransaction(transaction, frameNumber);
        }
    }

    public BackgroundBlurDrawable createBackgroundBlurDrawable() {
        return this.mBlurRegionAggregator.createBackgroundBlurDrawable(this.mContext);
    }

    @Override // android.view.ViewParent
    public void onDescendantUnbufferedRequested() {
        this.mUnbufferedInputSource = this.mView.mUnbufferedInputSource;
    }

    void forceDisableBLAST() {
        this.mForceDisableBLAST = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean useBLAST() {
        return this.mUseBLASTAdapter && !this.mForceDisableBLAST;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSurfaceSequenceId() {
        return this.mSurfaceSequenceId;
    }

    /* renamed from: mergeWithNextTransaction */
    public void lambda$applyTransactionOnDraw$9$ViewRootImpl(SurfaceControl.Transaction t, long frameNumber) {
        BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
        if (bLASTBufferQueue != null) {
            bLASTBufferQueue.mergeWithNextTransaction(t, frameNumber);
        } else {
            t.apply();
        }
    }

    @Override // android.view.AttachedSurfaceControl
    public SurfaceControl.Transaction buildReparentTransaction(SurfaceControl child) {
        if (this.mSurfaceControl.isValid()) {
            return new SurfaceControl.Transaction().reparent(child, this.mSurfaceControl);
        }
        return null;
    }

    @Override // android.view.AttachedSurfaceControl
    public boolean applyTransactionOnDraw(final SurfaceControl.Transaction t) {
        registerRtFrameCallback(new HardwareRenderer.FrameDrawingCallback() { // from class: android.view.ViewRootImpl$$ExternalSyntheticLambda4
            @Override // android.graphics.HardwareRenderer.FrameDrawingCallback
            public final void onFrameDraw(long j) {
                ViewRootImpl.this.lambda$applyTransactionOnDraw$9$ViewRootImpl(t, j);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSurfaceTransformHint() {
        return this.mSurfaceControl.getTransformHint();
    }

    public int getWindowingMode() {
        return this.mLastWindowMode;
    }
}
