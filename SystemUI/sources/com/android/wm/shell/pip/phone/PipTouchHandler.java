package com.android.wm.shell.pip.phone;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.provider.DeviceConfig;
import android.util.Log;
import android.util.Size;
import android.view.InputEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.pip.phone.PhonePipMenuController;
import com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.function.Function;
/* loaded from: classes2.dex */
public class PipTouchHandler {
    private final AccessibilityManager mAccessibilityManager;
    private int mBottomOffsetBufferPx;
    private final PipAccessibilityInteractionConnection mConnection;
    private final Context mContext;
    private int mDisplayRotation;
    private boolean mEnableResize;
    private int mExpandedShortestEdgeSize;
    private final FloatingContentCoordinator mFloatingContentCoordinator;
    private int mImeHeight;
    private int mImeOffset;
    private boolean mIsImeShowing;
    private boolean mIsShelfShowing;
    private final ShellExecutor mMainExecutor;
    private final PhonePipMenuController mMenuController;
    private float mMinimumSizePercent;
    private PipMotionHelper mMotionHelper;
    private int mMovementBoundsExtraOffsets;
    private boolean mMovementWithinDismiss;
    private final PipBoundsAlgorithm mPipBoundsAlgorithm;
    private final PipBoundsState mPipBoundsState;
    private final PipDismissTargetHandler mPipDismissTargetHandler;
    private PipResizeGestureHandler mPipResizeGestureHandler;
    private final PipTaskOrganizer mPipTaskOrganizer;
    private final PipUiEventLogger mPipUiEventLogger;
    private boolean mSendingHoverAccessibilityEvents;
    private int mShelfHeight;
    private float mStashVelocityThreshold;
    private final PipTouchState mTouchState;
    private boolean mShowPipMenuOnAnimationEnd = false;
    private boolean mEnableStash = true;
    private final Rect mInsetBounds = new Rect();
    private int mDeferResizeToNormalBoundsUntilRotation = -1;
    private int mMenuState = 0;
    private float mSavedSnapFraction = -1.0f;
    private final Rect mTmpBounds = new Rect();
    private PipTouchGesture mGesture = new DefaultPipTouchGesture();

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldShowResizeHandle() {
        return false;
    }

    /* loaded from: classes2.dex */
    private class PipMenuListener implements PhonePipMenuController.Listener {
        private PipMenuListener() {
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public void onPipMenuStateChangeStart(int i, boolean z, Runnable runnable) {
            PipTouchHandler.this.onPipMenuStateChangeStart(i, z, runnable);
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public void onPipMenuStateChangeFinish(int i) {
            PipTouchHandler.this.setMenuState(i);
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public void onPipExpand() {
            PipTouchHandler.this.mMotionHelper.expandLeavePip();
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public void onPipDismiss() {
            PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_TAP_TO_REMOVE);
            PipTouchHandler.this.mTouchState.removeDoubleTapTimeoutCallback();
            PipTouchHandler.this.mMotionHelper.dismissPip();
        }

        @Override // com.android.wm.shell.pip.phone.PhonePipMenuController.Listener
        public void onPipShowMenu() {
            PipTouchHandler.this.mMenuController.showMenu(2, PipTouchHandler.this.mPipBoundsState.getBounds(), true, PipTouchHandler.this.willResizeMenu(), PipTouchHandler.this.shouldShowResizeHandle());
        }
    }

    @SuppressLint({"InflateParams"})
    public PipTouchHandler(Context context, final PhonePipMenuController phonePipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipTaskOrganizer pipTaskOrganizer, PipMotionHelper pipMotionHelper, FloatingContentCoordinator floatingContentCoordinator, PipUiEventLogger pipUiEventLogger, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mAccessibilityManager = (AccessibilityManager) context.getSystemService(AccessibilityManager.class);
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipBoundsState = pipBoundsState;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        this.mMenuController = phonePipMenuController;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        phonePipMenuController.addListener(new PipMenuListener());
        this.mMotionHelper = pipMotionHelper;
        PipDismissTargetHandler pipDismissTargetHandler = new PipDismissTargetHandler(context, pipUiEventLogger, pipMotionHelper, shellExecutor);
        this.mPipDismissTargetHandler = pipDismissTargetHandler;
        this.mPipResizeGestureHandler = new PipResizeGestureHandler(context, pipBoundsAlgorithm, pipBoundsState, this.mMotionHelper, pipTaskOrganizer, pipDismissTargetHandler, new Function() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda7
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                Rect movementBounds;
                movementBounds = PipTouchHandler.this.getMovementBounds((Rect) obj);
                return movementBounds;
            }
        }, new Runnable() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PipTouchHandler.this.updateMovementBounds();
            }
        }, pipUiEventLogger, phonePipMenuController, shellExecutor);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        Runnable runnable = new Runnable() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                PipTouchHandler.this.lambda$new$0();
            }
        };
        Objects.requireNonNull(phonePipMenuController);
        this.mTouchState = new PipTouchState(viewConfiguration, runnable, new Runnable() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                PhonePipMenuController.this.hideMenu();
            }
        }, shellExecutor);
        this.mConnection = new PipAccessibilityInteractionConnection(context, pipBoundsState, this.mMotionHelper, pipTaskOrganizer, pipBoundsAlgorithm.getSnapAlgorithm(), new PipAccessibilityInteractionConnection.AccessibilityCallbacks() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda2
            @Override // com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection.AccessibilityCallbacks
            public final void onAccessibilityShowMenu() {
                PipTouchHandler.this.onAccessibilityShowMenu();
            }
        }, new Runnable() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PipTouchHandler.this.updateMovementBounds();
            }
        }, new Runnable() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                PipTouchHandler.this.animateToUnStashedState();
            }
        }, shellExecutor);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mPipBoundsState.isStashed()) {
            animateToUnStashedState();
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_UNSTASHED);
            this.mPipBoundsState.setStashed(0);
            return;
        }
        this.mMenuController.showMenuWithPossibleDelay(2, this.mPipBoundsState.getBounds(), true, willResizeMenu(), shouldShowResizeHandle());
    }

    public void init() {
        this.mEnableResize = this.mContext.getResources().getBoolean(R.bool.config_pipEnableResizeForMenu);
        reloadResources();
        this.mMotionHelper.init();
        this.mPipResizeGestureHandler.init();
        this.mPipDismissTargetHandler.init();
        this.mEnableStash = DeviceConfig.getBoolean("systemui", "pip_stashing", true);
        DeviceConfig.addOnPropertiesChangedListener("systemui", this.mMainExecutor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                PipTouchHandler.this.lambda$init$1(properties);
            }
        });
        this.mStashVelocityThreshold = DeviceConfig.getFloat("systemui", "pip_velocity_threshold", 18000.0f);
        DeviceConfig.addOnPropertiesChangedListener("systemui", this.mMainExecutor, new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                PipTouchHandler.this.lambda$init$2(properties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains("pip_stashing")) {
            this.mEnableStash = properties.getBoolean("pip_stashing", true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$2(DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains("pip_velocity_threshold")) {
            this.mStashVelocityThreshold = properties.getFloat("pip_velocity_threshold", 18000.0f);
        }
    }

    private void reloadResources() {
        Resources resources = this.mContext.getResources();
        this.mBottomOffsetBufferPx = resources.getDimensionPixelSize(R.dimen.pip_bottom_offset_buffer);
        this.mExpandedShortestEdgeSize = resources.getDimensionPixelSize(R.dimen.pip_expanded_shortest_edge_size);
        this.mImeOffset = resources.getDimensionPixelSize(R.dimen.pip_ime_offset);
        this.mMinimumSizePercent = resources.getFraction(R.fraction.config_pipShortestEdgePercent, 1, 1);
        this.mPipDismissTargetHandler.updateMagneticTargetSize();
    }

    public void setTouchEnabled(boolean z) {
        this.mTouchState.setAllowTouches(z);
    }

    public void showPictureInPictureMenu() {
        if (!this.mTouchState.isUserInteracting()) {
            this.mMenuController.showMenu(2, this.mPipBoundsState.getBounds(), false, willResizeMenu(), shouldShowResizeHandle());
        }
    }

    public void onActivityPinned() {
        this.mPipDismissTargetHandler.createOrUpdateDismissTarget();
        this.mShowPipMenuOnAnimationEnd = true;
        this.mPipResizeGestureHandler.onActivityPinned();
        this.mFloatingContentCoordinator.onContentAdded(this.mMotionHelper);
    }

    public void onActivityUnpinned(ComponentName componentName) {
        if (componentName == null) {
            this.mPipDismissTargetHandler.cleanUpDismissTarget();
            this.mFloatingContentCoordinator.onContentRemoved(this.mMotionHelper);
        }
        this.mPipResizeGestureHandler.onActivityUnpinned();
    }

    public void onPinnedStackAnimationEnded(int i) {
        this.mMotionHelper.synchronizePinnedStackBounds();
        updateMovementBounds();
        if (i == 2) {
            this.mPipResizeGestureHandler.setUserResizeBounds(this.mPipBoundsState.getBounds());
        }
        if (this.mShowPipMenuOnAnimationEnd) {
            this.mMenuController.showMenu(1, this.mPipBoundsState.getBounds(), true, false, shouldShowResizeHandle());
            this.mShowPipMenuOnAnimationEnd = false;
        }
    }

    public void onConfigurationChanged() {
        this.mPipResizeGestureHandler.onConfigurationChanged();
        this.mMotionHelper.synchronizePinnedStackBounds();
        reloadResources();
        this.mPipDismissTargetHandler.createOrUpdateDismissTarget();
    }

    public void onImeVisibilityChanged(boolean z, int i) {
        this.mIsImeShowing = z;
        this.mImeHeight = i;
    }

    public void onShelfVisibilityChanged(boolean z, int i) {
        this.mIsShelfShowing = z;
        this.mShelfHeight = i;
    }

    public void onSystemUiStateChanged(boolean z) {
        this.mPipResizeGestureHandler.onSystemUiStateChanged(z);
    }

    public void adjustBoundsForRotation(Rect rect, Rect rect2, Rect rect3) {
        Rect rect4 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(rect, rect3, rect4, 0);
        if ((this.mPipBoundsState.getMovementBounds().bottom - this.mMovementBoundsExtraOffsets) - this.mBottomOffsetBufferPx <= rect2.top) {
            rect.offsetTo(rect.left, rect4.bottom);
        }
    }

    public void onAspectRatioChanged() {
        this.mPipResizeGestureHandler.invalidateUserResizeBounds();
    }

    public void onMovementBoundsChanged(Rect rect, Rect rect2, Rect rect3, boolean z, boolean z2, int i) {
        int i2;
        if (this.mPipResizeGestureHandler.getUserResizeBounds().isEmpty()) {
            this.mPipResizeGestureHandler.setUserResizeBounds(rect2);
        }
        int i3 = 0;
        int i4 = this.mIsImeShowing ? this.mImeHeight : 0;
        boolean z3 = true;
        if (this.mDisplayRotation != i) {
            this.mTouchState.reset();
        }
        Rect rect4 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(rect2, rect, rect4, i4);
        if (this.mPipBoundsState.getMovementBounds().isEmpty()) {
            this.mPipBoundsAlgorithm.getMovementBounds(rect3, rect, this.mPipBoundsState.getMovementBounds(), 0);
        }
        float width = rect2.width() / rect2.height();
        Point point = new Point();
        this.mContext.getDisplay().getRealSize(point);
        Size sizeForAspectRatio = this.mPipBoundsAlgorithm.getSizeForAspectRatio(width, this.mExpandedShortestEdgeSize, point.x, point.y);
        this.mPipBoundsState.setExpandedBounds(new Rect(0, 0, sizeForAspectRatio.getWidth(), sizeForAspectRatio.getHeight()));
        Rect rect5 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(this.mPipBoundsState.getExpandedBounds(), rect, rect5, i4);
        if (this.mPipResizeGestureHandler.isUsingPinchToZoom()) {
            updatePinchResizeSizeConstraints(rect, rect2, width);
        } else {
            this.mPipResizeGestureHandler.updateMinSize(rect2.width(), rect2.height());
            this.mPipResizeGestureHandler.updateMaxSize(this.mPipBoundsState.getExpandedBounds().width(), this.mPipBoundsState.getExpandedBounds().height());
        }
        boolean z4 = this.mIsImeShowing;
        int max = Math.max(z4 ? this.mImeOffset : 0, (z4 || !this.mIsShelfShowing) ? 0 : this.mShelfHeight);
        if ((z || z2) && !this.mTouchState.isUserInteracting()) {
            if (this.mMenuState != 2 || !willResizeMenu()) {
                z3 = false;
            }
            Rect rect6 = new Rect();
            PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
            if (this.mIsImeShowing) {
                i3 = this.mImeHeight;
            }
            pipBoundsAlgorithm.getMovementBounds(rect3, rect, rect6, i3);
            int i5 = this.mPipBoundsState.getMovementBounds().bottom - this.mMovementBoundsExtraOffsets;
            int i6 = rect6.bottom;
            if (i6 >= rect6.top) {
                i6 -= max;
            }
            if (z3) {
                rect3.set(this.mPipBoundsState.getExpandedBounds());
                this.mPipBoundsAlgorithm.getSnapAlgorithm().applySnapFraction(rect3, rect6, this.mSavedSnapFraction);
            }
            if (i5 < i6) {
                int i7 = rect3.top;
                if (i7 > i5 - this.mBottomOffsetBufferPx) {
                    this.mMotionHelper.animateToOffset(rect3, i6 - i7);
                }
            } else if (i5 > i6 && (i2 = rect3.top) > i6 - this.mBottomOffsetBufferPx) {
                this.mMotionHelper.animateToOffset(rect3, i6 - i2);
            }
        }
        this.mPipBoundsState.setNormalMovementBounds(rect4);
        this.mPipBoundsState.setExpandedMovementBounds(rect5);
        this.mDisplayRotation = i;
        this.mInsetBounds.set(rect);
        updateMovementBounds();
        this.mMovementBoundsExtraOffsets = max;
        this.mConnection.onMovementBoundsChanged(rect2, this.mPipBoundsState.getExpandedBounds(), this.mPipBoundsState.getNormalMovementBounds(), this.mPipBoundsState.getExpandedMovementBounds());
        if (this.mDeferResizeToNormalBoundsUntilRotation == i) {
            this.mMotionHelper.animateToUnexpandedState(rect2, this.mSavedSnapFraction, this.mPipBoundsState.getNormalMovementBounds(), this.mPipBoundsState.getMovementBounds(), true);
            this.mSavedSnapFraction = -1.0f;
            this.mDeferResizeToNormalBoundsUntilRotation = -1;
        }
    }

    private void updatePinchResizeSizeConstraints(Rect rect, Rect rect2, float f) {
        int i;
        int i2;
        int i3;
        int i4;
        int min = Math.min(this.mPipBoundsState.getDisplayBounds().width(), this.mPipBoundsState.getDisplayBounds().height());
        int width = rect.left + (this.mPipBoundsState.getDisplayBounds().width() - rect.right);
        int height = rect.top + (this.mPipBoundsState.getDisplayBounds().height() - rect.bottom);
        if (f > 1.0f) {
            i2 = (int) Math.min(rect2.width(), min * this.mMinimumSizePercent);
            i = (int) (i2 / f);
            i4 = Math.max(rect2.width(), min - width);
            i3 = (int) (i4 / f);
        } else {
            int min2 = (int) Math.min(rect2.height(), min * this.mMinimumSizePercent);
            int max = Math.max(rect2.height(), min - height);
            i = min2;
            i2 = (int) (min2 * f);
            i3 = max;
            i4 = (int) (max * f);
        }
        this.mPipResizeGestureHandler.updateMinSize(i2, i);
        this.mPipResizeGestureHandler.updateMaxSize(i4, i3);
        this.mPipBoundsState.setMaxSize(i4, i3);
        this.mPipBoundsState.setMinSize(i2, i);
    }

    public void onRegistrationChanged(boolean z) {
        if (z) {
            this.mConnection.register(this.mAccessibilityManager);
        } else {
            this.mAccessibilityManager.setPictureInPictureActionReplacingConnection(null);
        }
        if (z || !this.mTouchState.isUserInteracting()) {
            return;
        }
        this.mPipDismissTargetHandler.cleanUpDismissTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAccessibilityShowMenu() {
        this.mMenuController.showMenu(2, this.mPipBoundsState.getBounds(), true, willResizeMenu(), shouldShowResizeHandle());
    }

    /* JADX WARN: Code restructure failed: missing block: B:65:0x00e7, code lost:
        if (r11.mGesture.onUp(r11.mTouchState) != false) goto L41;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean handleTouchEvent(InputEvent inputEvent) {
        if (!(inputEvent instanceof MotionEvent)) {
            return true;
        }
        MotionEvent motionEvent = (MotionEvent) inputEvent;
        if (!this.mPipBoundsState.isStashed() && this.mPipResizeGestureHandler.willStartResizeGesture(motionEvent)) {
            this.mTouchState.onTouchEvent(motionEvent);
            this.mTouchState.reset();
            return true;
        } else if (this.mPipResizeGestureHandler.hasOngoingGesture()) {
            this.mPipDismissTargetHandler.hideDismissTargetMaybe();
            return true;
        } else if ((motionEvent.getAction() == 0 || this.mTouchState.isUserInteracting()) && this.mPipDismissTargetHandler.maybeConsumeMotionEvent(motionEvent)) {
            if (motionEvent.getAction() == 0) {
                this.mTouchState.onTouchEvent(motionEvent);
            }
            this.mTouchState.addMovementToVelocityTracker(motionEvent);
            return true;
        } else {
            this.mTouchState.onTouchEvent(motionEvent);
            boolean z = false;
            boolean z2 = this.mMenuState != 0;
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mGesture.onDown(this.mTouchState);
            } else if (action == 1) {
                updateMovementBounds();
            } else if (action == 2) {
                if (!this.mGesture.onMove(this.mTouchState)) {
                    z2 = !this.mTouchState.isDragging();
                }
            } else {
                if (action != 3) {
                    if (action != 7) {
                        if (action != 9) {
                            if (action == 10) {
                                if (!this.mAccessibilityManager.isTouchExplorationEnabled()) {
                                    this.mTouchState.scheduleHoverExitTimeoutCallback();
                                }
                                if (!z2 && this.mSendingHoverAccessibilityEvents) {
                                    sendAccessibilityHoverEvent(256);
                                    this.mSendingHoverAccessibilityEvents = false;
                                }
                            }
                        } else if (!this.mAccessibilityManager.isTouchExplorationEnabled()) {
                            this.mTouchState.removeHoverExitTimeoutCallback();
                            this.mMenuController.showMenu(2, this.mPipBoundsState.getBounds(), false, false, shouldShowResizeHandle());
                        }
                    }
                    if (!z2 && !this.mSendingHoverAccessibilityEvents) {
                        sendAccessibilityHoverEvent(128);
                        this.mSendingHoverAccessibilityEvents = true;
                    }
                }
                if (!this.mTouchState.startedDragging() && !this.mTouchState.isDragging()) {
                    z = true;
                }
                this.mTouchState.reset();
                z2 = z;
            }
            if (z2 & (!this.mPipBoundsState.isStashed())) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                if (this.mTouchState.startedDragging()) {
                    obtain.setAction(3);
                    this.mMenuController.pokeMenu();
                }
                this.mMenuController.handlePointerEvent(obtain);
                obtain.recycle();
            }
            return true;
        }
    }

    private void sendAccessibilityHoverEvent(int i) {
        if (!this.mAccessibilityManager.isEnabled()) {
            return;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain(i);
        obtain.setImportantForAccessibility(true);
        obtain.setSourceNodeId(AccessibilityNodeInfo.ROOT_NODE_ID);
        obtain.setWindowId(-3);
        this.mAccessibilityManager.sendAccessibilityEvent(obtain);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPipMenuStateChangeStart(int i, boolean z, Runnable runnable) {
        int rotation;
        int i2 = this.mMenuState;
        if (i2 != i || z) {
            if (i == 2 && i2 != 2) {
                if (!z) {
                    return;
                }
                animateToNormalSize(runnable);
            } else if (i != 0 || i2 != 2) {
            } else {
                if (z && !this.mPipResizeGestureHandler.isResizing()) {
                    if (this.mDeferResizeToNormalBoundsUntilRotation == -1 && this.mDisplayRotation != (rotation = this.mContext.getDisplay().getRotation())) {
                        this.mDeferResizeToNormalBoundsUntilRotation = rotation;
                    }
                    if (this.mDeferResizeToNormalBoundsUntilRotation != -1) {
                        return;
                    }
                    animateToUnexpandedState(getUserResizeBounds());
                    return;
                }
                this.mSavedSnapFraction = -1.0f;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setMenuState(int i) {
        this.mMenuState = i;
        updateMovementBounds();
        onRegistrationChanged(i == 0);
        if (i == 0) {
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_HIDE_MENU);
        } else if (i != 2) {
        } else {
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_SHOW_MENU);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateToMaximizedState(Runnable runnable) {
        Rect rect = new Rect();
        int i = 0;
        Rect rect2 = new Rect(0, 0, this.mPipBoundsState.getMaxSize().x, this.mPipBoundsState.getMaxSize().y);
        PipBoundsAlgorithm pipBoundsAlgorithm = this.mPipBoundsAlgorithm;
        Rect rect3 = this.mInsetBounds;
        if (this.mIsImeShowing) {
            i = this.mImeHeight;
        }
        pipBoundsAlgorithm.getMovementBounds(rect2, rect3, rect, i);
        this.mSavedSnapFraction = this.mMotionHelper.animateToExpandedState(rect2, this.mPipBoundsState.getMovementBounds(), rect, runnable);
    }

    private void animateToNormalSize(Runnable runnable) {
        this.mPipResizeGestureHandler.setUserResizeBounds(this.mPipBoundsState.getBounds());
        Rect adjustNormalBoundsToFitMenu = this.mPipBoundsAlgorithm.adjustNormalBoundsToFitMenu(this.mPipBoundsState.getNormalBounds(), this.mMenuController.getEstimatedMinMenuSize());
        Rect rect = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(adjustNormalBoundsToFitMenu, this.mInsetBounds, rect, this.mIsImeShowing ? this.mImeHeight : 0);
        this.mSavedSnapFraction = this.mMotionHelper.animateToExpandedState(adjustNormalBoundsToFitMenu, this.mPipBoundsState.getMovementBounds(), rect, runnable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateToUnexpandedState(Rect rect) {
        Rect rect2 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(rect, this.mInsetBounds, rect2, this.mIsImeShowing ? this.mImeHeight : 0);
        this.mMotionHelper.animateToUnexpandedState(rect, this.mSavedSnapFraction, rect2, this.mPipBoundsState.getMovementBounds(), false);
        this.mSavedSnapFraction = -1.0f;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateToUnStashedState() {
        int width;
        int i;
        Rect bounds = this.mPipBoundsState.getBounds();
        boolean z = bounds.left < this.mPipBoundsState.getDisplayBounds().left;
        Rect rect = new Rect(0, bounds.top, 0, bounds.bottom);
        if (z) {
            width = this.mInsetBounds.left;
        } else {
            width = this.mInsetBounds.right - bounds.width();
        }
        rect.left = width;
        if (z) {
            i = this.mInsetBounds.left + bounds.width();
        } else {
            i = this.mInsetBounds.right;
        }
        rect.right = i;
        this.mMotionHelper.animateToUnStashedBounds(rect);
    }

    public PipMotionHelper getMotionHelper() {
        return this.mMotionHelper;
    }

    @VisibleForTesting
    public PipResizeGestureHandler getPipResizeGestureHandler() {
        return this.mPipResizeGestureHandler;
    }

    @VisibleForTesting
    public void setPipResizeGestureHandler(PipResizeGestureHandler pipResizeGestureHandler) {
        this.mPipResizeGestureHandler = pipResizeGestureHandler;
    }

    @VisibleForTesting
    public void setPipMotionHelper(PipMotionHelper pipMotionHelper) {
        this.mMotionHelper = pipMotionHelper;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getUserResizeBounds() {
        return this.mPipResizeGestureHandler.getUserResizeBounds();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class DefaultPipTouchGesture extends PipTouchGesture {
        private final PointF mDelta;
        private boolean mShouldHideMenuAfterFling;
        private final Point mStartPosition;

        private DefaultPipTouchGesture() {
            this.mStartPosition = new Point();
            this.mDelta = new PointF();
        }

        @Override // com.android.wm.shell.pip.phone.PipTouchGesture
        public void onDown(PipTouchState pipTouchState) {
            if (!pipTouchState.isUserInteracting()) {
                return;
            }
            Rect possiblyMotionBounds = PipTouchHandler.this.getPossiblyMotionBounds();
            this.mDelta.set(0.0f, 0.0f);
            this.mStartPosition.set(possiblyMotionBounds.left, possiblyMotionBounds.top);
            PipTouchHandler.this.mMovementWithinDismiss = pipTouchState.getDownTouchPosition().y >= ((float) PipTouchHandler.this.mPipBoundsState.getMovementBounds().bottom);
            PipTouchHandler.this.mMotionHelper.setSpringingToTouch(false);
            PipTouchHandler.this.mPipDismissTargetHandler.setTaskLeash(PipTouchHandler.this.mPipTaskOrganizer.getSurfaceControl());
            if (PipTouchHandler.this.mMenuState == 0 || PipTouchHandler.this.mPipBoundsState.isStashed()) {
                return;
            }
            PipTouchHandler.this.mMenuController.pokeMenu();
        }

        @Override // com.android.wm.shell.pip.phone.PipTouchGesture
        public boolean onMove(PipTouchState pipTouchState) {
            boolean z = false;
            if (!pipTouchState.isUserInteracting()) {
                return false;
            }
            if (pipTouchState.startedDragging()) {
                PipTouchHandler.this.mSavedSnapFraction = -1.0f;
                PipTouchHandler.this.mPipDismissTargetHandler.showDismissTargetMaybe();
            }
            if (!pipTouchState.isDragging()) {
                return false;
            }
            PointF lastTouchDelta = pipTouchState.getLastTouchDelta();
            Point point = this.mStartPosition;
            PointF pointF = this.mDelta;
            float f = pointF.x;
            float f2 = point.x + f;
            float f3 = pointF.y;
            float f4 = point.y + f3;
            float f5 = lastTouchDelta.x + f2;
            float f6 = lastTouchDelta.y + f4;
            pointF.x = f + (f5 - f2);
            pointF.y = f3 + (f6 - f4);
            PipTouchHandler.this.mTmpBounds.set(PipTouchHandler.this.getPossiblyMotionBounds());
            PipTouchHandler.this.mTmpBounds.offsetTo((int) f5, (int) f6);
            PipTouchHandler.this.mMotionHelper.movePip(PipTouchHandler.this.mTmpBounds, true);
            PointF lastTouchPosition = pipTouchState.getLastTouchPosition();
            if (PipTouchHandler.this.mMovementWithinDismiss) {
                PipTouchHandler pipTouchHandler = PipTouchHandler.this;
                if (lastTouchPosition.y >= pipTouchHandler.mPipBoundsState.getMovementBounds().bottom) {
                    z = true;
                }
                pipTouchHandler.mMovementWithinDismiss = z;
            }
            return true;
        }

        @Override // com.android.wm.shell.pip.phone.PipTouchGesture
        public boolean onUp(PipTouchState pipTouchState) {
            PipTouchHandler.this.mPipDismissTargetHandler.hideDismissTargetMaybe();
            PipTouchHandler.this.mPipDismissTargetHandler.setTaskLeash(null);
            if (!pipTouchState.isUserInteracting()) {
                return false;
            }
            PointF velocity = pipTouchState.getVelocity();
            if (pipTouchState.isDragging()) {
                if (PipTouchHandler.this.mMenuState != 0) {
                    PipTouchHandler.this.mMenuController.showMenu(PipTouchHandler.this.mMenuState, PipTouchHandler.this.mPipBoundsState.getBounds(), true, PipTouchHandler.this.willResizeMenu(), PipTouchHandler.this.shouldShowResizeHandle());
                }
                this.mShouldHideMenuAfterFling = PipTouchHandler.this.mMenuState == 0;
                PipTouchHandler.this.mTouchState.reset();
                if (!PipTouchHandler.this.mEnableStash || !shouldStash(velocity, PipTouchHandler.this.getPossiblyMotionBounds())) {
                    if (PipTouchHandler.this.mPipBoundsState.isStashed()) {
                        PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_UNSTASHED);
                        PipTouchHandler.this.mPipBoundsState.setStashed(0);
                    }
                    PipTouchHandler.this.mMotionHelper.flingToSnapTarget(velocity.x, velocity.y, new Runnable() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$DefaultPipTouchGesture$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            PipTouchHandler.DefaultPipTouchGesture.this.flingEndAction();
                        }
                    });
                } else {
                    PipTouchHandler.this.mMotionHelper.stashToEdge(velocity.x, velocity.y, new Runnable() { // from class: com.android.wm.shell.pip.phone.PipTouchHandler$DefaultPipTouchGesture$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            PipTouchHandler.DefaultPipTouchGesture.this.stashEndAction();
                        }
                    });
                }
            } else if (!PipTouchHandler.this.mTouchState.isDoubleTap() || PipTouchHandler.this.mPipBoundsState.isStashed() || PipTouchHandler.this.mMenuState == 2) {
                if (PipTouchHandler.this.mMenuState != 2) {
                    if (PipTouchHandler.this.mPipBoundsState.isStashed()) {
                        PipTouchHandler.this.animateToUnStashedState();
                        PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_UNSTASHED);
                        PipTouchHandler.this.mPipBoundsState.setStashed(0);
                        PipTouchHandler.this.mTouchState.removeDoubleTapTimeoutCallback();
                    } else if (PipTouchHandler.this.mTouchState.isWaitingForDoubleTap()) {
                        PipTouchHandler.this.mTouchState.scheduleDoubleTapTimeoutCallback();
                    } else {
                        PipTouchHandler.this.mMenuController.showMenu(2, PipTouchHandler.this.mPipBoundsState.getBounds(), true, PipTouchHandler.this.willResizeMenu(), PipTouchHandler.this.shouldShowResizeHandle());
                    }
                }
            } else if (PipTouchHandler.this.mPipResizeGestureHandler.isUsingPinchToZoom()) {
                boolean z = PipTouchHandler.this.mPipBoundsState.getBounds().width() < PipTouchHandler.this.mPipBoundsState.getMaxSize().x && PipTouchHandler.this.mPipBoundsState.getBounds().height() < PipTouchHandler.this.mPipBoundsState.getMaxSize().y;
                if (PipTouchHandler.this.mMenuController.isMenuVisible()) {
                    PipTouchHandler.this.mMenuController.hideMenu(0, false);
                }
                if (z) {
                    PipTouchHandler.this.mPipResizeGestureHandler.setUserResizeBounds(PipTouchHandler.this.mPipBoundsState.getBounds());
                    PipTouchHandler.this.animateToMaximizedState(null);
                } else {
                    PipTouchHandler pipTouchHandler = PipTouchHandler.this;
                    pipTouchHandler.animateToUnexpandedState(pipTouchHandler.getUserResizeBounds());
                }
            } else {
                PipTouchHandler.this.setTouchEnabled(false);
                PipTouchHandler.this.mMotionHelper.expandLeavePip();
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stashEndAction() {
            if (PipTouchHandler.this.mPipBoundsState.getBounds().left >= 0 || PipTouchHandler.this.mPipBoundsState.getStashedState() == 1) {
                if (PipTouchHandler.this.mPipBoundsState.getBounds().left >= 0 && PipTouchHandler.this.mPipBoundsState.getStashedState() != 2) {
                    PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_RIGHT);
                    PipTouchHandler.this.mPipBoundsState.setStashed(2);
                }
            } else {
                PipTouchHandler.this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_STASH_LEFT);
                PipTouchHandler.this.mPipBoundsState.setStashed(1);
            }
            PipTouchHandler.this.mMenuController.hideMenu();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void flingEndAction() {
            if (this.mShouldHideMenuAfterFling) {
                PipTouchHandler.this.mMenuController.hideMenu();
            }
        }

        private boolean shouldStash(PointF pointF, Rect rect) {
            boolean z = (pointF.x < (-PipTouchHandler.this.mStashVelocityThreshold) && PipTouchHandler.this.mPipBoundsState.getStashedState() != 2) || (pointF.x > PipTouchHandler.this.mStashVelocityThreshold && PipTouchHandler.this.mPipBoundsState.getStashedState() != 1);
            int width = rect.width() / 2;
            return z || (rect.right > PipTouchHandler.this.mPipBoundsState.getDisplayBounds().right + width || rect.left < PipTouchHandler.this.mPipBoundsState.getDisplayBounds().left - width);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateMovementBounds() {
        boolean z = false;
        this.mPipBoundsAlgorithm.getMovementBounds(this.mPipBoundsState.getBounds(), this.mInsetBounds, this.mPipBoundsState.getMovementBounds(), this.mIsImeShowing ? this.mImeHeight : 0);
        this.mMotionHelper.onMovementBoundsChanged();
        if (this.mMenuState == 2) {
            z = true;
        }
        this.mPipBoundsState.setMinEdgeSize((!z || !willResizeMenu()) ? this.mPipBoundsAlgorithm.getDefaultMinSize() : this.mExpandedShortestEdgeSize);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rect getMovementBounds(Rect rect) {
        Rect rect2 = new Rect();
        this.mPipBoundsAlgorithm.getMovementBounds(rect, this.mInsetBounds, rect2, this.mIsImeShowing ? this.mImeHeight : 0);
        return rect2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean willResizeMenu() {
        if (!this.mEnableResize) {
            return false;
        }
        Size estimatedMinMenuSize = this.mMenuController.getEstimatedMinMenuSize();
        if (estimatedMinMenuSize == null) {
            Log.wtf("PipTouchHandler", "Failed to get estimated menu size");
            return false;
        }
        Rect bounds = this.mPipBoundsState.getBounds();
        return bounds.width() < estimatedMinMenuSize.getWidth() || bounds.height() < estimatedMinMenuSize.getHeight();
    }

    Rect getPossiblyMotionBounds() {
        if (this.mPipBoundsState.getMotionBoundsState().isInMotion()) {
            return this.mPipBoundsState.getMotionBoundsState().getBoundsInMotion();
        }
        return this.mPipBoundsState.getBounds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setOhmOffset(int i) {
        this.mPipResizeGestureHandler.setOhmOffset(i);
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + "PipTouchHandler");
        printWriter.println(str2 + "mMenuState=" + this.mMenuState);
        printWriter.println(str2 + "mIsImeShowing=" + this.mIsImeShowing);
        printWriter.println(str2 + "mImeHeight=" + this.mImeHeight);
        printWriter.println(str2 + "mIsShelfShowing=" + this.mIsShelfShowing);
        printWriter.println(str2 + "mShelfHeight=" + this.mShelfHeight);
        printWriter.println(str2 + "mSavedSnapFraction=" + this.mSavedSnapFraction);
        printWriter.println(str2 + "mMovementBoundsExtraOffsets=" + this.mMovementBoundsExtraOffsets);
        this.mPipBoundsAlgorithm.dump(printWriter, str2);
        this.mTouchState.dump(printWriter, str2);
        PipResizeGestureHandler pipResizeGestureHandler = this.mPipResizeGestureHandler;
        if (pipResizeGestureHandler != null) {
            pipResizeGestureHandler.dump(printWriter, str2);
        }
    }
}
