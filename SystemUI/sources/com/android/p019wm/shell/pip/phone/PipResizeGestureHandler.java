package com.android.p019wm.shell.pip.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.input.InputManager;
import android.os.Looper;
import android.provider.DeviceConfig;
import android.view.BatchedInputEventReceiver;
import android.view.Choreographer;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InputMonitor;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.android.internal.policy.TaskResizingAlgorithm;
import com.android.p019wm.shell.C3343R;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.pip.PipBoundsAlgorithm;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import com.android.p019wm.shell.pip.PipUiEventLogger;
import java.p026io.PrintWriter;
import java.util.function.Consumer;
import java.util.function.Function;

/* renamed from: com.android.wm.shell.pip.phone.PipResizeGestureHandler */
public class PipResizeGestureHandler {
    private static final float PINCH_RESIZE_AUTO_MAX_RATIO = 0.9f;
    private static final int PINCH_RESIZE_SNAP_DURATION = 250;
    private static final String TAG = "PipResizeGestureHandler";
    private boolean mAllowGesture;
    private float mAngle = 0.0f;
    private final Context mContext;
    private int mCtrlType;
    private int mDelta;
    private final Rect mDisplayBounds = new Rect();
    private final int mDisplayId;
    private final Rect mDownBounds = new Rect();
    private final PointF mDownPoint = new PointF();
    private final PointF mDownSecondPoint = new PointF();
    private final Rect mDragCornerSize = new Rect();
    private boolean mEnableDragCornerResize;
    /* access modifiers changed from: private */
    public boolean mEnablePinchResize;
    int mFirstIndex = -1;
    private InputEventReceiver mInputEventReceiver;
    private InputMonitor mInputMonitor;
    private boolean mIsAttached;
    private boolean mIsEnabled;
    private boolean mIsSysUiStateValid;
    private final PointF mLastPoint = new PointF();
    private final Rect mLastResizeBounds = new Rect();
    private final PointF mLastSecondPoint = new PointF();
    private final ShellExecutor mMainExecutor;
    private final Point mMaxSize = new Point();
    private final Point mMinSize = new Point();
    private final PipMotionHelper mMotionHelper;
    private final Function<Rect, Rect> mMovementBoundsSupplier;
    private int mOhmOffset;
    private boolean mOngoingPinchToResize = false;
    private final PhonePipMenuController mPhonePipMenuController;
    private final PipPinchResizingAlgorithm mPinchResizingAlgorithm;
    private final PipBoundsAlgorithm mPipBoundsAlgorithm;
    private final PipBoundsState mPipBoundsState;
    private final PipDismissTargetHandler mPipDismissTargetHandler;
    private final PipTaskOrganizer mPipTaskOrganizer;
    private final PipUiEventLogger mPipUiEventLogger;
    int mSecondIndex = -1;
    private boolean mThresholdCrossed;
    private final Rect mTmpBottomLeftCorner = new Rect();
    private final Rect mTmpBottomRightCorner = new Rect();
    private final Region mTmpRegion = new Region();
    private final Rect mTmpTopLeftCorner = new Rect();
    private final Rect mTmpTopRightCorner = new Rect();
    private float mTouchSlop;
    private final Runnable mUpdateMovementBoundsRunnable;
    private final Rect mUserResizeBounds = new Rect();

    public PipResizeGestureHandler(Context context, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMotionHelper pipMotionHelper, PipTaskOrganizer pipTaskOrganizer, PipDismissTargetHandler pipDismissTargetHandler, Function<Rect, Rect> function, Runnable runnable, PipUiEventLogger pipUiEventLogger, PhonePipMenuController phonePipMenuController, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mDisplayId = context.getDisplayId();
        this.mMainExecutor = shellExecutor;
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipBoundsState = pipBoundsState;
        this.mMotionHelper = pipMotionHelper;
        this.mPipTaskOrganizer = pipTaskOrganizer;
        this.mPipDismissTargetHandler = pipDismissTargetHandler;
        this.mMovementBoundsSupplier = function;
        this.mUpdateMovementBoundsRunnable = runnable;
        this.mPhonePipMenuController = phonePipMenuController;
        this.mPipUiEventLogger = pipUiEventLogger;
        this.mPinchResizingAlgorithm = new PipPinchResizingAlgorithm();
    }

    public void init() {
        this.mContext.getDisplay().getRealSize(this.mMaxSize);
        reloadResources();
        this.mEnablePinchResize = DeviceConfig.getBoolean("systemui", "pip_pinch_resize", true);
        DeviceConfig.addOnPropertiesChangedListener("systemui", this.mMainExecutor, new DeviceConfig.OnPropertiesChangedListener() {
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("pip_pinch_resize")) {
                    boolean unused = PipResizeGestureHandler.this.mEnablePinchResize = properties.getBoolean("pip_pinch_resize", true);
                }
            }
        });
    }

    public void onConfigurationChanged() {
        reloadResources();
    }

    public void onSystemUiStateChanged(boolean z) {
        this.mIsSysUiStateValid = z;
    }

    private void reloadResources() {
        Resources resources = this.mContext.getResources();
        this.mDelta = resources.getDimensionPixelSize(C3343R.dimen.pip_resize_edge_size);
        this.mEnableDragCornerResize = resources.getBoolean(C3343R.bool.config_pipEnableDragCornerResize);
        this.mTouchSlop = (float) ViewConfiguration.get(this.mContext).getScaledTouchSlop();
    }

    private void resetDragCorners() {
        Rect rect = this.mDragCornerSize;
        int i = this.mDelta;
        rect.set(0, 0, i, i);
        this.mTmpTopLeftCorner.set(this.mDragCornerSize);
        this.mTmpTopRightCorner.set(this.mDragCornerSize);
        this.mTmpBottomLeftCorner.set(this.mDragCornerSize);
        this.mTmpBottomRightCorner.set(this.mDragCornerSize);
    }

    private void disposeInputChannel() {
        InputEventReceiver inputEventReceiver = this.mInputEventReceiver;
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

    /* access modifiers changed from: package-private */
    public void onActivityPinned() {
        this.mIsAttached = true;
        updateIsEnabled();
    }

    /* access modifiers changed from: package-private */
    public void onActivityUnpinned() {
        this.mIsAttached = false;
        this.mUserResizeBounds.setEmpty();
        updateIsEnabled();
    }

    private void updateIsEnabled() {
        boolean z = this.mIsAttached;
        if (z != this.mIsEnabled) {
            this.mIsEnabled = z;
            disposeInputChannel();
            if (this.mIsEnabled) {
                this.mInputMonitor = InputManager.getInstance().monitorGestureInput("pip-resize", this.mDisplayId);
                try {
                    this.mMainExecutor.executeBlocking(new PipResizeGestureHandler$$ExternalSyntheticLambda0(this));
                } catch (InterruptedException e) {
                    throw new RuntimeException("Failed to create input event receiver", e);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateIsEnabled$0$com-android-wm-shell-pip-phone-PipResizeGestureHandler */
    public /* synthetic */ void mo50457xbc679b54() {
        this.mInputEventReceiver = new PipResizeInputEventReceiver(this.mInputMonitor.getInputChannel(), Looper.myLooper());
    }

    /* access modifiers changed from: package-private */
    public void onInputEvent(InputEvent inputEvent) {
        if ((this.mEnableDragCornerResize || this.mEnablePinchResize) && !this.mPipBoundsState.isStashed() && (inputEvent instanceof MotionEvent)) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            int actionMasked = motionEvent.getActionMasked();
            Rect bounds = this.mPipBoundsState.getBounds();
            if ((actionMasked == 1 || actionMasked == 3) && !bounds.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY()) && this.mPhonePipMenuController.isMenuVisible()) {
                this.mPhonePipMenuController.hideMenu();
            }
            if (this.mEnablePinchResize && this.mOngoingPinchToResize) {
                onPinchResize(motionEvent);
            } else if (this.mEnableDragCornerResize) {
                onDragCornerResize(motionEvent);
            }
        }
    }

    public boolean hasOngoingGesture() {
        return this.mCtrlType != 0 || this.mOngoingPinchToResize;
    }

    public boolean isWithinDragResizeRegion(int i, int i2) {
        Rect bounds;
        if (!this.mEnableDragCornerResize || (bounds = this.mPipBoundsState.getBounds()) == null) {
            return false;
        }
        resetDragCorners();
        this.mTmpTopLeftCorner.offset(bounds.left - (this.mDelta / 2), bounds.top - (this.mDelta / 2));
        this.mTmpTopRightCorner.offset(bounds.right - (this.mDelta / 2), bounds.top - (this.mDelta / 2));
        this.mTmpBottomLeftCorner.offset(bounds.left - (this.mDelta / 2), bounds.bottom - (this.mDelta / 2));
        this.mTmpBottomRightCorner.offset(bounds.right - (this.mDelta / 2), bounds.bottom - (this.mDelta / 2));
        this.mTmpRegion.setEmpty();
        this.mTmpRegion.op(this.mTmpTopLeftCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpTopRightCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpBottomLeftCorner, Region.Op.UNION);
        this.mTmpRegion.op(this.mTmpBottomRightCorner, Region.Op.UNION);
        return this.mTmpRegion.contains(i, i2);
    }

    public boolean isUsingPinchToZoom() {
        return this.mEnablePinchResize;
    }

    public boolean isResizing() {
        return this.mAllowGesture;
    }

    public boolean willStartResizeGesture(MotionEvent motionEvent) {
        if (!isInValidSysUiState()) {
            return false;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            return isWithinDragResizeRegion((int) motionEvent.getRawX(), (int) motionEvent.getRawY());
        }
        if (actionMasked != 5 || !this.mEnablePinchResize || motionEvent.getPointerCount() != 2) {
            return false;
        }
        onPinchResize(motionEvent);
        boolean z = this.mAllowGesture;
        this.mOngoingPinchToResize = z;
        return z;
    }

    private void setCtrlType(int i, int i2) {
        Rect bounds = this.mPipBoundsState.getBounds();
        Rect apply = this.mMovementBoundsSupplier.apply(bounds);
        this.mDisplayBounds.set(apply.left, apply.top, apply.right + bounds.width(), apply.bottom + bounds.height());
        if (!(!this.mTmpTopLeftCorner.contains(i, i2) || bounds.top == this.mDisplayBounds.top || bounds.left == this.mDisplayBounds.left)) {
            this.mCtrlType = this.mCtrlType | 1 | 4;
        }
        if (!(!this.mTmpTopRightCorner.contains(i, i2) || bounds.top == this.mDisplayBounds.top || bounds.right == this.mDisplayBounds.right)) {
            this.mCtrlType = this.mCtrlType | 2 | 4;
        }
        if (!(!this.mTmpBottomRightCorner.contains(i, i2) || bounds.bottom == this.mDisplayBounds.bottom || bounds.right == this.mDisplayBounds.right)) {
            this.mCtrlType = this.mCtrlType | 2 | 8;
        }
        if (this.mTmpBottomLeftCorner.contains(i, i2) && bounds.bottom != this.mDisplayBounds.bottom && bounds.left != this.mDisplayBounds.left) {
            this.mCtrlType = this.mCtrlType | 1 | 8;
        }
    }

    private boolean isInValidSysUiState() {
        return this.mIsSysUiStateValid;
    }

    /* access modifiers changed from: package-private */
    public void onPinchResize(MotionEvent motionEvent) {
        int i;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            this.mFirstIndex = -1;
            this.mSecondIndex = -1;
            this.mAllowGesture = false;
            finishResize();
        }
        if (motionEvent.getPointerCount() == 2) {
            Rect bounds = this.mPipBoundsState.getBounds();
            if (actionMasked == 5 && this.mFirstIndex == -1 && this.mSecondIndex == -1 && bounds.contains((int) motionEvent.getRawX(0), (int) motionEvent.getRawY(0)) && bounds.contains((int) motionEvent.getRawX(1), (int) motionEvent.getRawY(1))) {
                this.mAllowGesture = true;
                this.mFirstIndex = 0;
                this.mSecondIndex = 1;
                this.mDownPoint.set(motionEvent.getRawX(0), motionEvent.getRawY(this.mFirstIndex));
                this.mDownSecondPoint.set(motionEvent.getRawX(this.mSecondIndex), motionEvent.getRawY(this.mSecondIndex));
                this.mDownBounds.set(bounds);
                this.mLastPoint.set(this.mDownPoint);
                PointF pointF = this.mLastSecondPoint;
                pointF.set(pointF);
                this.mLastResizeBounds.set(this.mDownBounds);
            }
            if (actionMasked == 2 && (i = this.mFirstIndex) != -1 && this.mSecondIndex != -1) {
                float rawX = motionEvent.getRawX(i);
                float rawY = motionEvent.getRawY(this.mFirstIndex);
                float rawX2 = motionEvent.getRawX(this.mSecondIndex);
                float rawY2 = motionEvent.getRawY(this.mSecondIndex);
                this.mLastPoint.set(rawX, rawY);
                this.mLastSecondPoint.set(rawX2, rawY2);
                if (!this.mThresholdCrossed && (distanceBetween(this.mDownSecondPoint, this.mLastSecondPoint) > this.mTouchSlop || distanceBetween(this.mDownPoint, this.mLastPoint) > this.mTouchSlop)) {
                    pilferPointers();
                    this.mThresholdCrossed = true;
                    this.mDownPoint.set(this.mLastPoint);
                    this.mDownSecondPoint.set(this.mLastSecondPoint);
                    if (this.mPhonePipMenuController.isMenuVisible()) {
                        this.mPhonePipMenuController.hideMenu();
                    }
                }
                if (this.mThresholdCrossed) {
                    float calculateBoundsAndAngle = this.mPinchResizingAlgorithm.calculateBoundsAndAngle(this.mDownPoint, this.mDownSecondPoint, this.mLastPoint, this.mLastSecondPoint, this.mMinSize, this.mMaxSize, this.mDownBounds, this.mLastResizeBounds);
                    this.mAngle = calculateBoundsAndAngle;
                    this.mPipTaskOrganizer.scheduleUserResizePip(this.mDownBounds, this.mLastResizeBounds, calculateBoundsAndAngle, (Consumer<Rect>) null);
                    this.mPipBoundsState.setHasUserResizedPip(true);
                }
            }
        }
    }

    private void onDragCornerResize(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        float x = motionEvent.getX();
        float y = motionEvent.getY() - ((float) this.mOhmOffset);
        boolean z = false;
        if (actionMasked == 0) {
            this.mLastResizeBounds.setEmpty();
            if (isInValidSysUiState() && isWithinDragResizeRegion((int) x, (int) y)) {
                z = true;
            }
            this.mAllowGesture = z;
            if (z) {
                setCtrlType((int) x, (int) y);
                this.mDownPoint.set(x, y);
                this.mDownBounds.set(this.mPipBoundsState.getBounds());
            }
        } else if (this.mAllowGesture) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    if (!this.mThresholdCrossed && Math.hypot((double) (x - this.mDownPoint.x), (double) (y - this.mDownPoint.y)) > ((double) this.mTouchSlop)) {
                        this.mThresholdCrossed = true;
                        this.mDownPoint.set(x, y);
                        this.mInputMonitor.pilferPointers();
                    }
                    if (this.mThresholdCrossed) {
                        if (this.mPhonePipMenuController.isMenuVisible()) {
                            this.mPhonePipMenuController.hideMenu(0, false);
                        }
                        this.mLastResizeBounds.set(TaskResizingAlgorithm.resizeDrag(x, y, this.mDownPoint.x, this.mDownPoint.y, this.mPipBoundsState.getBounds(), this.mCtrlType, this.mMinSize.x, this.mMinSize.y, this.mMaxSize, true, this.mDownBounds.width() > this.mDownBounds.height()));
                        this.mPipBoundsAlgorithm.transformBoundsToAspectRatio(this.mLastResizeBounds, this.mPipBoundsState.getAspectRatio(), false, true);
                        this.mPipTaskOrganizer.scheduleUserResizePip(this.mDownBounds, this.mLastResizeBounds, (Consumer<Rect>) null);
                        this.mPipBoundsState.setHasUserResizedPip(true);
                        return;
                    }
                    return;
                } else if (actionMasked != 3) {
                    if (actionMasked == 5) {
                        this.mAllowGesture = false;
                        return;
                    }
                    return;
                }
            }
            finishResize();
        }
    }

    private void finishResize() {
        if (!this.mLastResizeBounds.isEmpty()) {
            PipResizeGestureHandler$$ExternalSyntheticLambda1 pipResizeGestureHandler$$ExternalSyntheticLambda1 = new PipResizeGestureHandler$$ExternalSyntheticLambda1(this);
            if (this.mOngoingPinchToResize) {
                Rect rect = new Rect(this.mLastResizeBounds);
                if (((float) this.mLastResizeBounds.width()) >= ((float) this.mMaxSize.x) * 0.9f || ((float) this.mLastResizeBounds.height()) >= ((float) this.mMaxSize.y) * 0.9f) {
                    resizeRectAboutCenter(this.mLastResizeBounds, this.mMaxSize.x, this.mMaxSize.y);
                }
                int i = this.mLastResizeBounds.left;
                Rect movementBounds = this.mPipBoundsAlgorithm.getMovementBounds(this.mLastResizeBounds);
                int i2 = Math.abs(i - movementBounds.left) < Math.abs(movementBounds.right - i) ? movementBounds.left : movementBounds.right;
                Rect rect2 = this.mLastResizeBounds;
                rect2.offsetTo(i2, rect2.top);
                this.mPipBoundsAlgorithm.applySnapFraction(this.mLastResizeBounds, this.mPipBoundsAlgorithm.getSnapFraction(this.mLastResizeBounds, movementBounds));
                this.mPipTaskOrganizer.scheduleAnimateResizePip(rect, this.mLastResizeBounds, 250, this.mAngle, pipResizeGestureHandler$$ExternalSyntheticLambda1);
            } else {
                this.mPipTaskOrganizer.scheduleFinishResizePip(this.mLastResizeBounds, 7, pipResizeGestureHandler$$ExternalSyntheticLambda1);
            }
            this.mPipDismissTargetHandler.setMagneticFieldRadiusPercent((((float) this.mLastResizeBounds.width()) / ((float) this.mMinSize.x)) / 2.0f);
            this.mPipUiEventLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_RESIZE);
            return;
        }
        resetState();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$finishResize$1$com-android-wm-shell-pip-phone-PipResizeGestureHandler */
    public /* synthetic */ void mo50456x522235c0(Rect rect) {
        this.mUserResizeBounds.set(this.mLastResizeBounds);
        this.mMotionHelper.synchronizePinnedStackBounds();
        this.mUpdateMovementBoundsRunnable.run();
        resetState();
    }

    private void resetState() {
        this.mCtrlType = 0;
        this.mAngle = 0.0f;
        this.mOngoingPinchToResize = false;
        this.mAllowGesture = false;
        this.mThresholdCrossed = false;
    }

    /* access modifiers changed from: package-private */
    public void setUserResizeBounds(Rect rect) {
        this.mUserResizeBounds.set(rect);
    }

    /* access modifiers changed from: package-private */
    public void invalidateUserResizeBounds() {
        this.mUserResizeBounds.setEmpty();
    }

    /* access modifiers changed from: package-private */
    public Rect getUserResizeBounds() {
        return this.mUserResizeBounds;
    }

    /* access modifiers changed from: package-private */
    public Rect getLastResizeBounds() {
        return this.mLastResizeBounds;
    }

    /* access modifiers changed from: package-private */
    public void pilferPointers() {
        this.mInputMonitor.pilferPointers();
    }

    public void updateMaxSize(int i, int i2) {
        this.mMaxSize.set(i, i2);
    }

    public void updateMinSize(int i, int i2) {
        this.mMinSize.set(i, i2);
    }

    /* access modifiers changed from: package-private */
    public void setOhmOffset(int i) {
        this.mOhmOffset = i;
    }

    private float distanceBetween(PointF pointF, PointF pointF2) {
        return (float) Math.hypot((double) (pointF2.x - pointF.x), (double) (pointF2.y - pointF.y));
    }

    private void resizeRectAboutCenter(Rect rect, int i, int i2) {
        int centerX = rect.centerX() - (i / 2);
        int centerY = rect.centerY() - (i2 / 2);
        rect.set(centerX, centerY, i + centerX, i2 + centerY);
    }

    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        printWriter.println(str2 + "mAllowGesture=" + this.mAllowGesture);
        printWriter.println(str2 + "mIsAttached=" + this.mIsAttached);
        printWriter.println(str2 + "mIsEnabled=" + this.mIsEnabled);
        printWriter.println(str2 + "mEnablePinchResize=" + this.mEnablePinchResize);
        printWriter.println(str2 + "mThresholdCrossed=" + this.mThresholdCrossed);
        printWriter.println(str2 + "mOhmOffset=" + this.mOhmOffset);
    }

    /* renamed from: com.android.wm.shell.pip.phone.PipResizeGestureHandler$PipResizeInputEventReceiver */
    class PipResizeInputEventReceiver extends BatchedInputEventReceiver {
        PipResizeInputEventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper, Choreographer.getSfInstance());
        }

        public void onInputEvent(InputEvent inputEvent) {
            PipResizeGestureHandler.this.onInputEvent(inputEvent);
            finishInputEvent(inputEvent, true);
        }
    }
}
