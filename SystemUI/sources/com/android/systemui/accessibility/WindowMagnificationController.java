package com.android.systemui.accessibility;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.view.Choreographer;
import android.view.Display;
import android.view.IWindow;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.WindowMetrics;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IRemoteMagnificationAnimationCallback;
import androidx.core.math.MathUtils;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.C1893R;
import com.android.systemui.accessibility.MagnificationGestureDetector;
import com.android.systemui.accessibility.MirrorWindowControl;
import com.android.systemui.model.SysUiState;
import com.android.systemui.shared.system.WindowManagerWrapper;
import java.p026io.PrintWriter;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;

class WindowMagnificationController implements View.OnTouchListener, SurfaceHolder.Callback, MirrorWindowControl.MirrorWindowDelegate, MagnificationGestureDetector.OnGestureListener, ComponentCallbacks {
    /* access modifiers changed from: private */
    public static final Range<Float> A11Y_ACTION_SCALE_RANGE = new Range<>(Float.valueOf(2.0f), Float.valueOf(8.0f));
    private static final float A11Y_CHANGE_SCALE_DIFFERENCE = 1.0f;
    private static final float ANIMATION_BOUNCE_EFFECT_SCALE = 1.05f;
    private static final boolean DEBUG = (Log.isLoggable(TAG, 3) || Build.IS_DEBUGGABLE);
    private static final String TAG = "WindowMagnificationController";
    private static final int UPDATE_STATE_DESCRIPTION_DELAY_MS = 100;
    private final WindowMagnificationAnimationController mAnimationController;
    private int mBorderDragSize;
    private View mBottomDrag;
    private float mBounceEffectAnimationScale;
    private final int mBounceEffectDuration;
    private final Configuration mConfiguration;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final int mDisplayId;
    private View mDragView;
    private int mDragViewSize;
    private final MagnificationGestureDetector mGestureDetector;
    private final Handler mHandler;
    private View mLeftDrag;
    private Locale mLocale;
    private final Rect mMagnificationFrame = new Rect();
    private final Rect mMagnificationFrameBoundary = new Rect();
    private int mMagnificationFrameOffsetX = 0;
    private int mMagnificationFrameOffsetY = 0;
    private int mMinWindowSize;
    private SurfaceControl mMirrorSurface;
    private int mMirrorSurfaceMargin;
    private SurfaceView mMirrorSurfaceView;
    private final View.OnLayoutChangeListener mMirrorSurfaceViewLayoutChangeListener;
    private View mMirrorView;
    private final Rect mMirrorViewBounds = new Rect();
    private Choreographer.FrameCallback mMirrorViewGeometryVsyncCallback;
    private final View.OnLayoutChangeListener mMirrorViewLayoutChangeListener;
    private final Runnable mMirrorViewRunnable;
    private MirrorWindowControl mMirrorWindowControl;
    private int mOuterBorderSize;
    private boolean mOverlapWithGestureInsets;
    private NumberFormat mPercentFormat;
    private final Resources mResources;
    private View mRightDrag;
    int mRotation;
    /* access modifiers changed from: private */
    public float mScale;
    private final SfVsyncFrameCallbackProvider mSfVsyncFrameProvider;
    /* access modifiers changed from: private */
    public final Rect mSourceBounds = new Rect();
    private SysUiState mSysUiState;
    private int mSystemGestureTop = -1;
    private final Rect mTmpRect = new Rect();
    private View mTopDrag;
    private final SurfaceControl.Transaction mTransaction;
    private final Runnable mUpdateStateDescriptionRunnable;
    private Rect mWindowBounds;
    private final Runnable mWindowInsetChangeRunnable;
    /* access modifiers changed from: private */
    public final WindowMagnifierCallback mWindowMagnifierCallback;
    private final WindowManager mWm;

    public boolean onFinish(float f, float f2) {
        return false;
    }

    public void onLowMemory() {
    }

    public boolean onStart(float f, float f2) {
        return true;
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    WindowMagnificationController(Context context, Handler handler, WindowMagnificationAnimationController windowMagnificationAnimationController, SfVsyncFrameCallbackProvider sfVsyncFrameCallbackProvider, MirrorWindowControl mirrorWindowControl, SurfaceControl.Transaction transaction, WindowMagnifierCallback windowMagnifierCallback, SysUiState sysUiState) {
        this.mContext = context;
        this.mHandler = handler;
        this.mAnimationController = windowMagnificationAnimationController;
        windowMagnificationAnimationController.setWindowMagnificationController(this);
        this.mSfVsyncFrameProvider = sfVsyncFrameCallbackProvider;
        this.mWindowMagnifierCallback = windowMagnifierCallback;
        this.mSysUiState = sysUiState;
        this.mConfiguration = new Configuration(context.getResources().getConfiguration());
        Display display = context.getDisplay();
        this.mDisplayId = context.getDisplayId();
        this.mRotation = display.getRotation();
        WindowManager windowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mWm = windowManager;
        this.mWindowBounds = new Rect(windowManager.getCurrentWindowMetrics().getBounds());
        Resources resources = context.getResources();
        this.mResources = resources;
        this.mScale = (float) resources.getInteger(C1893R.integer.magnification_default_scale);
        this.mBounceEffectDuration = resources.getInteger(17694720);
        updateDimensions();
        Size defaultWindowSizeWithWindowBounds = getDefaultWindowSizeWithWindowBounds(this.mWindowBounds);
        setMagnificationFrame(defaultWindowSizeWithWindowBounds.getWidth(), defaultWindowSizeWithWindowBounds.getHeight(), this.mWindowBounds.width() / 2, this.mWindowBounds.height() / 2);
        computeBounceAnimationScale();
        this.mMirrorWindowControl = mirrorWindowControl;
        if (mirrorWindowControl != null) {
            mirrorWindowControl.setWindowDelegate(this);
        }
        this.mTransaction = transaction;
        this.mGestureDetector = new MagnificationGestureDetector(context, handler, this);
        this.mMirrorViewRunnable = new WindowMagnificationController$$ExternalSyntheticLambda0(this);
        this.mMirrorViewLayoutChangeListener = new WindowMagnificationController$$ExternalSyntheticLambda1(this);
        this.mMirrorSurfaceViewLayoutChangeListener = new WindowMagnificationController$$ExternalSyntheticLambda2(this);
        this.mMirrorViewGeometryVsyncCallback = new WindowMagnificationController$$ExternalSyntheticLambda3(this);
        this.mUpdateStateDescriptionRunnable = new WindowMagnificationController$$ExternalSyntheticLambda4(this);
        this.mWindowInsetChangeRunnable = new WindowMagnificationController$$ExternalSyntheticLambda5(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-accessibility-WindowMagnificationController */
    public /* synthetic */ void mo30005xf0fbb365() {
        if (this.mMirrorView != null) {
            Rect rect = new Rect(this.mMirrorViewBounds);
            this.mMirrorView.getBoundsOnScreen(this.mMirrorViewBounds);
            if (!(rect.width() == this.mMirrorViewBounds.width() && rect.height() == this.mMirrorViewBounds.height())) {
                this.mMirrorView.setSystemGestureExclusionRects(Collections.singletonList(new Rect(0, 0, this.mMirrorViewBounds.width(), this.mMirrorViewBounds.height())));
            }
            updateSystemUIStateIfNeeded();
            this.mWindowMagnifierCallback.onWindowMagnifierBoundsChanged(this.mDisplayId, this.mMirrorViewBounds);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-accessibility-WindowMagnificationController */
    public /* synthetic */ void mo30006x5b2b3b84(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (!this.mHandler.hasCallbacks(this.mMirrorViewRunnable)) {
            this.mHandler.post(this.mMirrorViewRunnable);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-accessibility-WindowMagnificationController */
    public /* synthetic */ void mo30007xc55ac3a3(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        applyTapExcludeRegion();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-accessibility-WindowMagnificationController */
    public /* synthetic */ void mo30008x2f8a4bc2(long j) {
        if (isWindowVisible() && this.mMirrorSurface != null && calculateSourceBounds(this.mMagnificationFrame, this.mScale)) {
            this.mTmpRect.set(0, 0, this.mMagnificationFrame.width(), this.mMagnificationFrame.height());
            this.mTransaction.setGeometry(this.mMirrorSurface, this.mSourceBounds, this.mTmpRect, 0).apply();
            if (!this.mAnimationController.isAnimating()) {
                this.mWindowMagnifierCallback.onSourceBoundsChanged(this.mDisplayId, this.mSourceBounds);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$4$com-android-systemui-accessibility-WindowMagnificationController */
    public /* synthetic */ void mo30009x99b9d3e1() {
        if (isWindowVisible()) {
            this.mMirrorView.setStateDescription(formatStateDescription(this.mScale));
        }
    }

    private void updateDimensions() {
        this.mMirrorSurfaceMargin = this.mResources.getDimensionPixelSize(C1893R.dimen.magnification_mirror_surface_margin);
        this.mBorderDragSize = this.mResources.getDimensionPixelSize(C1893R.dimen.magnification_border_drag_size);
        this.mDragViewSize = this.mResources.getDimensionPixelSize(C1893R.dimen.magnification_drag_view_size);
        this.mOuterBorderSize = this.mResources.getDimensionPixelSize(C1893R.dimen.magnification_outer_border_margin);
        this.mMinWindowSize = this.mResources.getDimensionPixelSize(17104911);
    }

    private void computeBounceAnimationScale() {
        float width = (float) (this.mMagnificationFrame.width() + (this.mMirrorSurfaceMargin * 2));
        this.mBounceEffectAnimationScale = Math.min(width / (width - ((float) (this.mOuterBorderSize * 2))), (float) ANIMATION_BOUNCE_EFFECT_SCALE);
    }

    private boolean updateSystemGestureInsetsTop() {
        WindowMetrics currentWindowMetrics = this.mWm.getCurrentWindowMetrics();
        Insets insets = currentWindowMetrics.getWindowInsets().getInsets(WindowInsets.Type.systemGestures());
        int i = insets.bottom != 0 ? currentWindowMetrics.getBounds().bottom - insets.bottom : -1;
        if (i == this.mSystemGestureTop) {
            return false;
        }
        this.mSystemGestureTop = i;
        return true;
    }

    /* access modifiers changed from: package-private */
    public void deleteWindowMagnification(IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mAnimationController.deleteWindowMagnification(iRemoteMagnificationAnimationCallback);
    }

    /* access modifiers changed from: package-private */
    public void deleteWindowMagnification() {
        if (isWindowVisible()) {
            SurfaceControl surfaceControl = this.mMirrorSurface;
            if (surfaceControl != null) {
                this.mTransaction.remove(surfaceControl).apply();
                this.mMirrorSurface = null;
            }
            SurfaceView surfaceView = this.mMirrorSurfaceView;
            if (surfaceView != null) {
                surfaceView.removeOnLayoutChangeListener(this.mMirrorSurfaceViewLayoutChangeListener);
            }
            if (this.mMirrorView != null) {
                this.mHandler.removeCallbacks(this.mMirrorViewRunnable);
                this.mMirrorView.removeOnLayoutChangeListener(this.mMirrorViewLayoutChangeListener);
                this.mWm.removeView(this.mMirrorView);
                this.mMirrorView = null;
            }
            MirrorWindowControl mirrorWindowControl = this.mMirrorWindowControl;
            if (mirrorWindowControl != null) {
                mirrorWindowControl.destroyControl();
            }
            this.mMirrorViewBounds.setEmpty();
            this.mSourceBounds.setEmpty();
            updateSystemUIStateIfNeeded();
            this.mContext.unregisterComponentCallbacks(this);
            this.mWindowMagnifierCallback.onSourceBoundsChanged(this.mDisplayId, new Rect());
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        int diff = configuration.diff(this.mConfiguration);
        this.mConfiguration.setTo(configuration);
        onConfigurationChanged(diff);
    }

    /* access modifiers changed from: package-private */
    public void onConfigurationChanged(int i) {
        boolean z;
        if (DEBUG) {
            Log.d(TAG, "onConfigurationChanged = " + Configuration.configurationDiffToString(i));
        }
        if (i != 0) {
            if ((i & 128) != 0) {
                onRotate();
            }
            if ((i & 4) != 0) {
                updateAccessibilityWindowTitleIfNeeded();
            }
            if ((i & 4096) != 0) {
                updateDimensions();
                computeBounceAnimationScale();
                z = true;
            } else {
                z = false;
            }
            if ((i & 1024) != 0) {
                z |= handleScreenSizeChanged();
            }
            if (isWindowVisible() && z) {
                deleteWindowMagnification();
                enableWindowMagnificationInternal(Float.NaN, Float.NaN, Float.NaN);
            }
        }
    }

    private boolean handleScreenSizeChanged() {
        Rect rect = new Rect(this.mWindowBounds);
        Rect bounds = this.mWm.getCurrentWindowMetrics().getBounds();
        if (!bounds.equals(rect)) {
            this.mWindowBounds.set(bounds);
            Size defaultWindowSizeWithWindowBounds = getDefaultWindowSizeWithWindowBounds(this.mWindowBounds);
            setMagnificationFrame(defaultWindowSizeWithWindowBounds.getWidth(), defaultWindowSizeWithWindowBounds.getHeight(), (int) ((getCenterX() * ((float) this.mWindowBounds.width())) / ((float) rect.width())), (int) ((getCenterY() * ((float) this.mWindowBounds.height())) / ((float) rect.height())));
            calculateMagnificationFrameBoundary();
            return true;
        } else if (!DEBUG) {
            return false;
        } else {
            Log.d(TAG, "handleScreenSizeChanged -- window bounds is not changed");
            return false;
        }
    }

    private void updateSystemUIStateIfNeeded() {
        updateSysUIState(false);
    }

    private void updateAccessibilityWindowTitleIfNeeded() {
        if (isWindowVisible()) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mMirrorView.getLayoutParams();
            layoutParams.accessibilityTitle = getAccessibilityWindowTitle();
            this.mWm.updateViewLayout(this.mMirrorView, layoutParams);
        }
    }

    private void onRotate() {
        Display display = this.mContext.getDisplay();
        int i = this.mRotation;
        int rotation = display.getRotation();
        this.mRotation = rotation;
        int degreeFromRotation = getDegreeFromRotation(rotation, i);
        if (degreeFromRotation == 0 || degreeFromRotation == 180) {
            Log.w(TAG, "onRotate -- rotate with the device. skip it");
            return;
        }
        Rect rect = new Rect(this.mWm.getCurrentWindowMetrics().getBounds());
        if (rect.width() == this.mWindowBounds.height() && rect.height() == this.mWindowBounds.width()) {
            this.mWindowBounds.set(rect);
            Matrix matrix = new Matrix();
            matrix.setRotate((float) degreeFromRotation);
            if (degreeFromRotation == 90) {
                matrix.postTranslate((float) this.mWindowBounds.width(), 0.0f);
            } else if (degreeFromRotation == 270) {
                matrix.postTranslate(0.0f, (float) this.mWindowBounds.height());
            }
            RectF rectF = new RectF(this.mMagnificationFrame);
            int i2 = this.mMirrorSurfaceMargin;
            rectF.inset((float) (-i2), (float) (-i2));
            matrix.mapRect(rectF);
            setWindowSizeAndCenter((int) rectF.width(), (int) rectF.height(), (float) ((int) rectF.centerX()), (float) ((int) rectF.centerY()));
            return;
        }
        Log.w(TAG, "onRotate -- unexpected window height/width");
    }

    private int getDegreeFromRotation(int i, int i2) {
        return (((i2 - i) + 4) % 4) * 90;
    }

    private void createMirrorWindow() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(this.mMagnificationFrame.width() + (this.mMirrorSurfaceMargin * 2), this.mMagnificationFrame.height() + (this.mMirrorSurfaceMargin * 2), 2039, 40, -2);
        layoutParams.gravity = 51;
        layoutParams.x = this.mMagnificationFrame.left - this.mMirrorSurfaceMargin;
        layoutParams.y = this.mMagnificationFrame.top - this.mMirrorSurfaceMargin;
        layoutParams.layoutInDisplayCutoutMode = 1;
        layoutParams.receiveInsetsIgnoringZOrder = true;
        layoutParams.setTitle(this.mContext.getString(C1893R.string.magnification_window_title));
        layoutParams.accessibilityTitle = getAccessibilityWindowTitle();
        View inflate = LayoutInflater.from(this.mContext).inflate(C1893R.layout.window_magnifier_view, (ViewGroup) null);
        this.mMirrorView = inflate;
        SurfaceView surfaceView = (SurfaceView) inflate.findViewById(C1893R.C1897id.surface_view);
        this.mMirrorSurfaceView = surfaceView;
        surfaceView.addOnLayoutChangeListener(this.mMirrorSurfaceViewLayoutChangeListener);
        this.mMirrorView.setSystemUiVisibility(5894);
        this.mMirrorView.addOnLayoutChangeListener(this.mMirrorViewLayoutChangeListener);
        this.mMirrorView.setAccessibilityDelegate(new MirrorWindowA11yDelegate());
        this.mMirrorView.setOnApplyWindowInsetsListener(new WindowMagnificationController$$ExternalSyntheticLambda6(this));
        this.mWm.addView(this.mMirrorView, layoutParams);
        SurfaceHolder holder = this.mMirrorSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setFormat(1);
        addDragTouchListeners();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createMirrorWindow$5$com-android-systemui-accessibility-WindowMagnificationController */
    public /* synthetic */ WindowInsets mo30004xceecd97b(View view, WindowInsets windowInsets) {
        if (!this.mHandler.hasCallbacks(this.mWindowInsetChangeRunnable)) {
            this.mHandler.post(this.mWindowInsetChangeRunnable);
        }
        return view.onApplyWindowInsets(windowInsets);
    }

    /* access modifiers changed from: private */
    public void onWindowInsetChanged() {
        if (updateSystemGestureInsetsTop()) {
            updateSystemUIStateIfNeeded();
        }
    }

    private void applyTapExcludeRegion() {
        Region calculateTapExclude = calculateTapExclude();
        try {
            WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(IWindow.Stub.asInterface(this.mMirrorView.getWindowToken()), calculateTapExclude);
        } catch (RemoteException unused) {
        }
    }

    private Region calculateTapExclude() {
        int i = this.mBorderDragSize;
        Region region = new Region(i, i, this.mMirrorView.getWidth() - this.mBorderDragSize, this.mMirrorView.getHeight() - this.mBorderDragSize);
        region.op(new Rect((this.mMirrorView.getWidth() - this.mDragViewSize) - this.mBorderDragSize, (this.mMirrorView.getHeight() - this.mDragViewSize) - this.mBorderDragSize, this.mMirrorView.getWidth(), this.mMirrorView.getHeight()), Region.Op.DIFFERENCE);
        return region;
    }

    private String getAccessibilityWindowTitle() {
        return this.mResources.getString(17039665);
    }

    private void showControls() {
        MirrorWindowControl mirrorWindowControl = this.mMirrorWindowControl;
        if (mirrorWindowControl != null) {
            mirrorWindowControl.showControl();
        }
    }

    public void setWindowSize(int i, int i2) {
        setWindowSizeAndCenter(i, i2, Float.NaN, Float.NaN);
    }

    /* access modifiers changed from: package-private */
    public void setWindowSizeAndCenter(int i, int i2, float f, float f2) {
        int clamp = MathUtils.clamp(i, this.mMinWindowSize, this.mWindowBounds.width());
        int clamp2 = MathUtils.clamp(i2, this.mMinWindowSize, this.mWindowBounds.height());
        if (Float.isNaN(f)) {
            f = (float) this.mMagnificationFrame.centerX();
        }
        if (Float.isNaN(f)) {
            f2 = (float) this.mMagnificationFrame.centerY();
        }
        int i3 = this.mMirrorSurfaceMargin;
        setMagnificationFrame(clamp - (i3 * 2), clamp2 - (i3 * 2), (int) f, (int) f2);
        calculateMagnificationFrameBoundary();
        updateMagnificationFramePosition(0, 0);
        modifyWindowMagnification(true);
    }

    private void setMagnificationFrame(int i, int i2, int i3, int i4) {
        int i5 = i3 - (i / 2);
        int i6 = i4 - (i2 / 2);
        this.mMagnificationFrame.set(i5, i6, i + i5, i2 + i6);
    }

    private Size getDefaultWindowSizeWithWindowBounds(Rect rect) {
        int min = Math.min(this.mResources.getDimensionPixelSize(C1893R.dimen.magnification_max_frame_size), Math.min(rect.width(), rect.height()) / 2) + (this.mMirrorSurfaceMargin * 2);
        return new Size(min, min);
    }

    private void createMirror() {
        SurfaceControl mirrorDisplay = WindowManagerWrapper.getInstance().mirrorDisplay(this.mDisplayId);
        this.mMirrorSurface = mirrorDisplay;
        if (mirrorDisplay.isValid()) {
            this.mTransaction.show(this.mMirrorSurface).reparent(this.mMirrorSurface, this.mMirrorSurfaceView.getSurfaceControl());
            modifyWindowMagnification(false);
        }
    }

    private void addDragTouchListeners() {
        this.mDragView = this.mMirrorView.findViewById(C1893R.C1897id.drag_handle);
        this.mLeftDrag = this.mMirrorView.findViewById(C1893R.C1897id.left_handle);
        this.mTopDrag = this.mMirrorView.findViewById(C1893R.C1897id.top_handle);
        this.mRightDrag = this.mMirrorView.findViewById(C1893R.C1897id.right_handle);
        this.mBottomDrag = this.mMirrorView.findViewById(C1893R.C1897id.bottom_handle);
        this.mDragView.setOnTouchListener(this);
        this.mLeftDrag.setOnTouchListener(this);
        this.mTopDrag.setOnTouchListener(this);
        this.mRightDrag.setOnTouchListener(this);
        this.mBottomDrag.setOnTouchListener(this);
    }

    private void modifyWindowMagnification(boolean z) {
        this.mSfVsyncFrameProvider.postFrameCallback(this.mMirrorViewGeometryVsyncCallback);
        updateMirrorViewLayout(z);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x007a  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0085  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateMirrorViewLayout(boolean r6) {
        /*
            r5 = this;
            boolean r0 = r5.isWindowVisible()
            if (r0 != 0) goto L_0x0007
            return
        L_0x0007:
            android.graphics.Rect r0 = r5.mWindowBounds
            int r0 = r0.width()
            android.view.View r1 = r5.mMirrorView
            int r1 = r1.getWidth()
            int r0 = r0 - r1
            android.graphics.Rect r1 = r5.mWindowBounds
            int r1 = r1.height()
            android.view.View r2 = r5.mMirrorView
            int r2 = r2.getHeight()
            int r1 = r1 - r2
            android.view.View r2 = r5.mMirrorView
            android.view.ViewGroup$LayoutParams r2 = r2.getLayoutParams()
            android.view.WindowManager$LayoutParams r2 = (android.view.WindowManager.LayoutParams) r2
            android.graphics.Rect r3 = r5.mMagnificationFrame
            int r3 = r3.left
            int r4 = r5.mMirrorSurfaceMargin
            int r3 = r3 - r4
            r2.x = r3
            android.graphics.Rect r3 = r5.mMagnificationFrame
            int r3 = r3.top
            int r4 = r5.mMirrorSurfaceMargin
            int r3 = r3 - r4
            r2.y = r3
            if (r6 == 0) goto L_0x0057
            android.graphics.Rect r6 = r5.mMagnificationFrame
            int r6 = r6.width()
            int r3 = r5.mMirrorSurfaceMargin
            int r3 = r3 * 2
            int r6 = r6 + r3
            r2.width = r6
            android.graphics.Rect r6 = r5.mMagnificationFrame
            int r6 = r6.height()
            int r3 = r5.mMirrorSurfaceMargin
            int r3 = r3 * 2
            int r6 = r6 + r3
            r2.height = r6
        L_0x0057:
            int r6 = r2.x
            r3 = 0
            if (r6 >= 0) goto L_0x0067
            int r6 = r2.x
            int r0 = r5.mOuterBorderSize
            int r0 = -r0
            int r6 = java.lang.Math.max((int) r6, (int) r0)
        L_0x0065:
            float r6 = (float) r6
            goto L_0x0076
        L_0x0067:
            int r6 = r2.x
            if (r6 <= r0) goto L_0x0075
            int r6 = r2.x
            int r6 = r6 - r0
            int r0 = r5.mOuterBorderSize
            int r6 = java.lang.Math.min((int) r6, (int) r0)
            goto L_0x0065
        L_0x0075:
            r6 = r3
        L_0x0076:
            int r0 = r2.y
            if (r0 >= 0) goto L_0x0085
            int r0 = r2.y
            int r1 = r5.mOuterBorderSize
            int r1 = -r1
            int r0 = java.lang.Math.max((int) r0, (int) r1)
        L_0x0083:
            float r3 = (float) r0
            goto L_0x0093
        L_0x0085:
            int r0 = r2.y
            if (r0 <= r1) goto L_0x0093
            int r0 = r2.y
            int r0 = r0 - r1
            int r1 = r5.mOuterBorderSize
            int r0 = java.lang.Math.min((int) r0, (int) r1)
            goto L_0x0083
        L_0x0093:
            android.view.View r0 = r5.mMirrorView
            r0.setTranslationX(r6)
            android.view.View r6 = r5.mMirrorView
            r6.setTranslationY(r3)
            android.view.WindowManager r6 = r5.mWm
            android.view.View r5 = r5.mMirrorView
            r6.updateViewLayout(r5, r2)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.accessibility.WindowMagnificationController.updateMirrorViewLayout(boolean):void");
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == this.mDragView || view == this.mLeftDrag || view == this.mTopDrag || view == this.mRightDrag || view == this.mBottomDrag) {
            return this.mGestureDetector.onTouch(motionEvent);
        }
        return false;
    }

    public void updateSysUIStateFlag() {
        updateSysUIState(true);
    }

    private boolean calculateSourceBounds(Rect rect, float f) {
        Rect rect2 = this.mTmpRect;
        rect2.set(this.mSourceBounds);
        int width = rect.width() / 2;
        int height = rect.height() / 2;
        int i = width - ((int) (((float) width) / f));
        int i2 = rect.right - i;
        int i3 = height - ((int) (((float) height) / f));
        this.mSourceBounds.set(rect.left + i, rect.top + i3, i2, rect.bottom - i3);
        this.mSourceBounds.offset(-this.mMagnificationFrameOffsetX, -this.mMagnificationFrameOffsetY);
        if (this.mSourceBounds.left < 0) {
            Rect rect3 = this.mSourceBounds;
            rect3.offsetTo(0, rect3.top);
        } else if (this.mSourceBounds.right > this.mWindowBounds.width()) {
            this.mSourceBounds.offsetTo(this.mWindowBounds.width() - this.mSourceBounds.width(), this.mSourceBounds.top);
        }
        if (this.mSourceBounds.top < 0) {
            Rect rect4 = this.mSourceBounds;
            rect4.offsetTo(rect4.left, 0);
        } else if (this.mSourceBounds.bottom > this.mWindowBounds.height()) {
            Rect rect5 = this.mSourceBounds;
            rect5.offsetTo(rect5.left, this.mWindowBounds.height() - this.mSourceBounds.height());
        }
        return !this.mSourceBounds.equals(rect2);
    }

    private void calculateMagnificationFrameBoundary() {
        int width = this.mMagnificationFrame.width() / 2;
        int height = this.mMagnificationFrame.height() / 2;
        float f = this.mScale;
        int i = width - ((int) (((float) width) / f));
        int max = Math.max(i - this.mMagnificationFrameOffsetX, 0);
        int max2 = Math.max(i + this.mMagnificationFrameOffsetX, 0);
        int i2 = height - ((int) (((float) height) / f));
        this.mMagnificationFrameBoundary.set(-max, -Math.max(i2 - this.mMagnificationFrameOffsetY, 0), this.mWindowBounds.width() + max2, this.mWindowBounds.height() + Math.max(i2 + this.mMagnificationFrameOffsetY, 0));
    }

    private boolean updateMagnificationFramePosition(int i, int i2) {
        this.mTmpRect.set(this.mMagnificationFrame);
        this.mTmpRect.offset(i, i2);
        if (this.mTmpRect.left < this.mMagnificationFrameBoundary.left) {
            this.mTmpRect.offsetTo(this.mMagnificationFrameBoundary.left, this.mTmpRect.top);
        } else if (this.mTmpRect.right > this.mMagnificationFrameBoundary.right) {
            int width = this.mMagnificationFrameBoundary.right - this.mMagnificationFrame.width();
            Rect rect = this.mTmpRect;
            rect.offsetTo(width, rect.top);
        }
        if (this.mTmpRect.top < this.mMagnificationFrameBoundary.top) {
            Rect rect2 = this.mTmpRect;
            rect2.offsetTo(rect2.left, this.mMagnificationFrameBoundary.top);
        } else if (this.mTmpRect.bottom > this.mMagnificationFrameBoundary.bottom) {
            int height = this.mMagnificationFrameBoundary.bottom - this.mMagnificationFrame.height();
            Rect rect3 = this.mTmpRect;
            rect3.offsetTo(rect3.left, height);
        }
        if (this.mTmpRect.equals(this.mMagnificationFrame)) {
            return false;
        }
        this.mMagnificationFrame.set(this.mTmpRect);
        return true;
    }

    private void updateSysUIState(boolean z) {
        boolean z2 = isWindowVisible() && this.mSystemGestureTop > 0 && this.mMirrorViewBounds.bottom > this.mSystemGestureTop;
        if (z || z2 != this.mOverlapWithGestureInsets) {
            this.mOverlapWithGestureInsets = z2;
            this.mSysUiState.setFlag(524288, z2).commitUpdate(this.mDisplayId);
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        createMirror();
    }

    public void move(int i, int i2) {
        moveWindowMagnifier((float) i, (float) i2);
        this.mWindowMagnifierCallback.onMove(this.mDisplayId);
    }

    public void enableWindowMagnification(float f, float f2, float f3, float f4, float f5, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        this.mAnimationController.enableWindowMagnification(f, f2, f3, f4, f5, iRemoteMagnificationAnimationCallback);
    }

    /* access modifiers changed from: package-private */
    public void enableWindowMagnificationInternal(float f, float f2, float f3) {
        enableWindowMagnificationInternal(f, f2, f3, Float.NaN, Float.NaN);
    }

    /* access modifiers changed from: package-private */
    public void enableWindowMagnificationInternal(float f, float f2, float f3, float f4, float f5) {
        int i;
        int i2;
        float f6;
        if (Float.compare(f, 1.0f) <= 0) {
            deleteWindowMagnification();
            return;
        }
        if (!isWindowVisible()) {
            onConfigurationChanged(this.mResources.getConfiguration());
            this.mContext.registerComponentCallbacks(this);
        }
        if (Float.isNaN(f4)) {
            i = this.mMagnificationFrameOffsetX;
        } else {
            i = (int) (((float) (this.mMagnificationFrame.width() / 2)) * f4);
        }
        this.mMagnificationFrameOffsetX = i;
        if (Float.isNaN(f5)) {
            i2 = this.mMagnificationFrameOffsetY;
        } else {
            i2 = (int) (((float) (this.mMagnificationFrame.height() / 2)) * f5);
        }
        this.mMagnificationFrameOffsetY = i2;
        float f7 = ((float) this.mMagnificationFrameOffsetX) + f2;
        float f8 = ((float) i2) + f3;
        float f9 = 0.0f;
        if (Float.isNaN(f2)) {
            f6 = 0.0f;
        } else {
            f6 = f7 - this.mMagnificationFrame.exactCenterX();
        }
        if (!Float.isNaN(f3)) {
            f9 = f8 - this.mMagnificationFrame.exactCenterY();
        }
        if (Float.isNaN(f)) {
            f = this.mScale;
        }
        this.mScale = f;
        calculateMagnificationFrameBoundary();
        updateMagnificationFramePosition((int) f6, (int) f9);
        if (!isWindowVisible()) {
            createMirrorWindow();
            showControls();
            return;
        }
        modifyWindowMagnification(false);
    }

    /* access modifiers changed from: package-private */
    public void setScale(float f) {
        if (!this.mAnimationController.isAnimating() && isWindowVisible() && this.mScale != f) {
            enableWindowMagnificationInternal(f, Float.NaN, Float.NaN);
            this.mHandler.removeCallbacks(this.mUpdateStateDescriptionRunnable);
            this.mHandler.postDelayed(this.mUpdateStateDescriptionRunnable, 100);
        }
    }

    /* access modifiers changed from: package-private */
    public void moveWindowMagnifier(float f, float f2) {
        if (!this.mAnimationController.isAnimating() && this.mMirrorSurfaceView != null && updateMagnificationFramePosition((int) f, (int) f2)) {
            modifyWindowMagnification(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void moveWindowMagnifierToPosition(float f, float f2, IRemoteMagnificationAnimationCallback iRemoteMagnificationAnimationCallback) {
        if (this.mMirrorSurfaceView != null) {
            this.mAnimationController.moveWindowMagnifierToPosition(f, f2, iRemoteMagnificationAnimationCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public float getScale() {
        if (isWindowVisible()) {
            return this.mScale;
        }
        return Float.NaN;
    }

    /* access modifiers changed from: package-private */
    public float getCenterX() {
        if (isWindowVisible()) {
            return this.mMagnificationFrame.exactCenterX();
        }
        return Float.NaN;
    }

    /* access modifiers changed from: package-private */
    public float getCenterY() {
        if (isWindowVisible()) {
            return this.mMagnificationFrame.exactCenterY();
        }
        return Float.NaN;
    }

    private boolean isWindowVisible() {
        return this.mMirrorView != null;
    }

    /* access modifiers changed from: private */
    public CharSequence formatStateDescription(float f) {
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            this.mPercentFormat = NumberFormat.getPercentInstance(locale);
        }
        return this.mPercentFormat.format((double) f);
    }

    public boolean onSingleTap() {
        animateBounceEffect();
        return true;
    }

    public boolean onDrag(float f, float f2) {
        move((int) f, (int) f2);
        return true;
    }

    private void animateBounceEffect() {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mMirrorView, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat(View.SCALE_X, new float[]{1.0f, this.mBounceEffectAnimationScale, 1.0f}), PropertyValuesHolder.ofFloat(View.SCALE_Y, new float[]{1.0f, this.mBounceEffectAnimationScale, 1.0f})});
        ofPropertyValuesHolder.setDuration((long) this.mBounceEffectDuration);
        ofPropertyValuesHolder.start();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("WindowMagnificationController (displayId=" + this.mDisplayId + "):");
        printWriter.println("      mOverlapWithGestureInsets:" + this.mOverlapWithGestureInsets);
        printWriter.println("      mScale:" + this.mScale);
        printWriter.println("      mWindowBounds:" + this.mWindowBounds);
        Object obj = "empty";
        printWriter.println("      mMirrorViewBounds:" + (isWindowVisible() ? this.mMirrorViewBounds : obj));
        printWriter.println("      mMagnificationFrameBoundary:" + (isWindowVisible() ? this.mMagnificationFrameBoundary : obj));
        printWriter.println("      mMagnificationFrame:" + (isWindowVisible() ? this.mMagnificationFrame : obj));
        StringBuilder sb = new StringBuilder("      mSourceBounds:");
        if (!this.mSourceBounds.isEmpty()) {
            obj = this.mSourceBounds;
        }
        printWriter.println(sb.append(obj).toString());
        printWriter.println("      mSystemGestureTop:" + this.mSystemGestureTop);
        printWriter.println("      mMagnificationFrameOffsetX:" + this.mMagnificationFrameOffsetX);
        printWriter.println("      mMagnificationFrameOffsetY:" + this.mMagnificationFrameOffsetY);
    }

    private class MirrorWindowA11yDelegate extends View.AccessibilityDelegate {
        private MirrorWindowA11yDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1893R.C1897id.accessibility_action_zoom_in, WindowMagnificationController.this.mContext.getString(C1893R.string.accessibility_control_zoom_in)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1893R.C1897id.accessibility_action_zoom_out, WindowMagnificationController.this.mContext.getString(C1893R.string.accessibility_control_zoom_out)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1893R.C1897id.accessibility_action_move_up, WindowMagnificationController.this.mContext.getString(C1893R.string.accessibility_control_move_up)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1893R.C1897id.accessibility_action_move_down, WindowMagnificationController.this.mContext.getString(C1893R.string.accessibility_control_move_down)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1893R.C1897id.accessibility_action_move_left, WindowMagnificationController.this.mContext.getString(C1893R.string.accessibility_control_move_left)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(C1893R.C1897id.accessibility_action_move_right, WindowMagnificationController.this.mContext.getString(C1893R.string.accessibility_control_move_right)));
            accessibilityNodeInfo.setContentDescription(WindowMagnificationController.this.mContext.getString(C1893R.string.magnification_window_title));
            WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
            accessibilityNodeInfo.setStateDescription(windowMagnificationController.formatStateDescription(windowMagnificationController.getScale()));
        }

        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (performA11yAction(i)) {
                return true;
            }
            return super.performAccessibilityAction(view, i, bundle);
        }

        private boolean performA11yAction(int i) {
            if (i == C1893R.C1897id.accessibility_action_zoom_in) {
                WindowMagnificationController.this.mWindowMagnifierCallback.onPerformScaleAction(WindowMagnificationController.this.mDisplayId, ((Float) WindowMagnificationController.A11Y_ACTION_SCALE_RANGE.clamp(Float.valueOf(WindowMagnificationController.this.mScale + 1.0f))).floatValue());
            } else if (i == C1893R.C1897id.accessibility_action_zoom_out) {
                WindowMagnificationController.this.mWindowMagnifierCallback.onPerformScaleAction(WindowMagnificationController.this.mDisplayId, ((Float) WindowMagnificationController.A11Y_ACTION_SCALE_RANGE.clamp(Float.valueOf(WindowMagnificationController.this.mScale - 1.0f))).floatValue());
            } else if (i == C1893R.C1897id.accessibility_action_move_up) {
                WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
                windowMagnificationController.move(0, -windowMagnificationController.mSourceBounds.height());
            } else if (i == C1893R.C1897id.accessibility_action_move_down) {
                WindowMagnificationController windowMagnificationController2 = WindowMagnificationController.this;
                windowMagnificationController2.move(0, windowMagnificationController2.mSourceBounds.height());
            } else if (i == C1893R.C1897id.accessibility_action_move_left) {
                WindowMagnificationController windowMagnificationController3 = WindowMagnificationController.this;
                windowMagnificationController3.move(-windowMagnificationController3.mSourceBounds.width(), 0);
            } else if (i != C1893R.C1897id.accessibility_action_move_right) {
                return false;
            } else {
                WindowMagnificationController windowMagnificationController4 = WindowMagnificationController.this;
                windowMagnificationController4.move(windowMagnificationController4.mSourceBounds.width(), 0);
            }
            WindowMagnificationController.this.mWindowMagnifierCallback.onAccessibilityActionPerformed(WindowMagnificationController.this.mDisplayId);
            return true;
        }
    }
}
