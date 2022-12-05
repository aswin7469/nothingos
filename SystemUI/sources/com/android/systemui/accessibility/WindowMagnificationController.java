package com.android.systemui.accessibility;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.util.Range;
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
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.graphics.SfVsyncFrameCallbackProvider;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.accessibility.MagnificationGestureDetector;
import com.android.systemui.model.SysUiState;
import com.android.systemui.shared.system.WindowManagerWrapper;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class WindowMagnificationController implements View.OnTouchListener, SurfaceHolder.Callback, MagnificationGestureDetector.OnGestureListener {
    private static final Range<Float> A11Y_ACTION_SCALE_RANGE = new Range<>(Float.valueOf(2.0f), Float.valueOf(8.0f));
    private int mBorderDragSize;
    private View mBottomDrag;
    private float mBounceEffectAnimationScale;
    private final int mBounceEffectDuration;
    private final Context mContext;
    private final int mDisplayId;
    private View mDragView;
    private int mDragViewSize;
    private final MagnificationGestureDetector mGestureDetector;
    private final Handler mHandler;
    private View mLeftDrag;
    private Locale mLocale;
    private SurfaceControl mMirrorSurface;
    private int mMirrorSurfaceMargin;
    private SurfaceView mMirrorSurfaceView;
    private View mMirrorView;
    private int mOuterBorderSize;
    private boolean mOverlapWithGestureInsets;
    private NumberFormat mPercentFormat;
    private final Resources mResources;
    private View mRightDrag;
    @VisibleForTesting
    int mRotation;
    private float mScale;
    private final SfVsyncFrameCallbackProvider mSfVsyncFrameProvider;
    private SysUiState mSysUiState;
    private View mTopDrag;
    private final SurfaceControl.Transaction mTransaction;
    private Rect mWindowBounds;
    private final WindowMagnifierCallback mWindowMagnifierCallback;
    private final WindowManager mWm;
    private final Rect mMagnificationFrame = new Rect();
    private final Rect mTmpRect = new Rect();
    private final Rect mMirrorViewBounds = new Rect();
    private final Rect mSourceBounds = new Rect();
    private final Rect mMagnificationFrameBoundary = new Rect();
    private int mSystemGestureTop = -1;
    private final Runnable mMirrorViewRunnable = new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda6
        @Override // java.lang.Runnable
        public final void run() {
            WindowMagnificationController.this.lambda$new$0();
        }
    };
    private final View.OnLayoutChangeListener mMirrorViewLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda3
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            WindowMagnificationController.this.lambda$new$1(view, i, i2, i3, i4, i5, i6, i7, i8);
        }
    };
    private final View.OnLayoutChangeListener mMirrorSurfaceViewLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda2
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            WindowMagnificationController.this.lambda$new$2(view, i, i2, i3, i4, i5, i6, i7, i8);
        }
    };
    private Choreographer.FrameCallback mMirrorViewGeometryVsyncCallback = new Choreographer.FrameCallback() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda0
        @Override // android.view.Choreographer.FrameCallback
        public final void doFrame(long j) {
            WindowMagnificationController.this.lambda$new$3(j);
        }
    };
    private final Runnable mUpdateStateDescriptionRunnable = new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda5
        @Override // java.lang.Runnable
        public final void run() {
            WindowMagnificationController.this.lambda$new$4();
        }
    };
    private final Runnable mWindowInsetChangeRunnable = new Runnable() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda4
        @Override // java.lang.Runnable
        public final void run() {
            WindowMagnificationController.this.onWindowInsetChanged();
        }
    };

    private void showControls() {
    }

    @Override // com.android.systemui.accessibility.MagnificationGestureDetector.OnGestureListener
    public boolean onFinish(float f, float f2) {
        return false;
    }

    @Override // com.android.systemui.accessibility.MagnificationGestureDetector.OnGestureListener
    public boolean onStart(float f, float f2) {
        return true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public WindowMagnificationController(Context context, Handler handler, SfVsyncFrameCallbackProvider sfVsyncFrameCallbackProvider, MirrorWindowControl mirrorWindowControl, SurfaceControl.Transaction transaction, WindowMagnifierCallback windowMagnifierCallback, SysUiState sysUiState) {
        this.mContext = context;
        this.mHandler = handler;
        this.mSfVsyncFrameProvider = sfVsyncFrameCallbackProvider;
        this.mWindowMagnifierCallback = windowMagnifierCallback;
        this.mSysUiState = sysUiState;
        Display display = context.getDisplay();
        this.mDisplayId = context.getDisplayId();
        this.mRotation = display.getRotation();
        WindowManager windowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mWm = windowManager;
        this.mWindowBounds = windowManager.getCurrentWindowMetrics().getBounds();
        Resources resources = context.getResources();
        this.mResources = resources;
        this.mScale = resources.getInteger(R$integer.magnification_default_scale);
        this.mBounceEffectDuration = resources.getInteger(17694720);
        updateDimensions();
        setInitialStartBounds();
        computeBounceAnimationScale();
        this.mTransaction = transaction;
        this.mGestureDetector = new MagnificationGestureDetector(context, handler, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        if (this.mMirrorView != null) {
            Rect rect = new Rect(this.mMirrorViewBounds);
            this.mMirrorView.getBoundsOnScreen(this.mMirrorViewBounds);
            if (rect.width() != this.mMirrorViewBounds.width() || rect.height() != this.mMirrorViewBounds.height()) {
                this.mMirrorView.setSystemGestureExclusionRects(Collections.singletonList(new Rect(0, 0, this.mMirrorViewBounds.width(), this.mMirrorViewBounds.height())));
            }
            updateSystemUIStateIfNeeded();
            this.mWindowMagnifierCallback.onWindowMagnifierBoundsChanged(this.mDisplayId, this.mMirrorViewBounds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (!this.mHandler.hasCallbacks(this.mMirrorViewRunnable)) {
            this.mHandler.post(this.mMirrorViewRunnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        applyTapExcludeRegion();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(long j) {
        if (!isWindowVisible() || this.mMirrorSurface == null) {
            return;
        }
        calculateSourceBounds(this.mMagnificationFrame, this.mScale);
        this.mTmpRect.set(0, 0, this.mMagnificationFrame.width(), this.mMagnificationFrame.height());
        this.mTransaction.setGeometry(this.mMirrorSurface, this.mSourceBounds, this.mTmpRect, 0).apply();
        this.mWindowMagnifierCallback.onSourceBoundsChanged(this.mDisplayId, this.mSourceBounds);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4() {
        if (isWindowVisible()) {
            this.mMirrorView.setStateDescription(formatStateDescription(this.mScale));
        }
    }

    private void updateDimensions() {
        this.mMirrorSurfaceMargin = this.mResources.getDimensionPixelSize(R$dimen.magnification_mirror_surface_margin);
        this.mBorderDragSize = this.mResources.getDimensionPixelSize(R$dimen.magnification_border_drag_size);
        this.mDragViewSize = this.mResources.getDimensionPixelSize(R$dimen.magnification_drag_view_size);
        this.mOuterBorderSize = this.mResources.getDimensionPixelSize(R$dimen.magnification_outer_border_margin);
    }

    private void computeBounceAnimationScale() {
        float width = this.mMagnificationFrame.width() + (this.mMirrorSurfaceMargin * 2);
        this.mBounceEffectAnimationScale = Math.min(width / (width - (this.mOuterBorderSize * 2)), 1.05f);
    }

    private boolean updateSystemGestureInsetsTop() {
        WindowMetrics currentWindowMetrics = this.mWm.getCurrentWindowMetrics();
        Insets insets = currentWindowMetrics.getWindowInsets().getInsets(WindowInsets.Type.systemGestures());
        int i = insets.bottom != 0 ? currentWindowMetrics.getBounds().bottom - insets.bottom : -1;
        if (i != this.mSystemGestureTop) {
            this.mSystemGestureTop = i;
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void deleteWindowMagnification() {
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
        this.mMirrorViewBounds.setEmpty();
        updateSystemUIStateIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onConfigurationChanged(int i) {
        if ((i & 4096) != 0) {
            updateDimensions();
            computeBounceAnimationScale();
            if (!isWindowVisible()) {
                return;
            }
            deleteWindowMagnification();
            enableWindowMagnification(Float.NaN, Float.NaN, Float.NaN);
        } else if ((i & 128) != 0) {
            onRotate();
        } else if ((i & 4) == 0) {
        } else {
            updateAccessibilityWindowTitleIfNeeded();
        }
    }

    private void updateSystemUIStateIfNeeded() {
        updateSysUIState(false);
    }

    private void updateAccessibilityWindowTitleIfNeeded() {
        if (!isWindowVisible()) {
            return;
        }
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mMirrorView.getLayoutParams();
        layoutParams.accessibilityTitle = getAccessibilityWindowTitle();
        this.mWm.updateViewLayout(this.mMirrorView, layoutParams);
    }

    private void onRotate() {
        Display display = this.mContext.getDisplay();
        int i = this.mRotation;
        this.mWindowBounds = this.mWm.getCurrentWindowMetrics().getBounds();
        setMagnificationFrameBoundary();
        this.mRotation = display.getRotation();
        if (!isWindowVisible()) {
            return;
        }
        int degreeFromRotation = getDegreeFromRotation(this.mRotation, i);
        Matrix matrix = new Matrix();
        matrix.setRotate(degreeFromRotation);
        if (degreeFromRotation == 90) {
            matrix.postTranslate(this.mWindowBounds.width(), 0.0f);
        } else if (degreeFromRotation == 270) {
            matrix.postTranslate(0.0f, this.mWindowBounds.height());
        } else {
            Log.w("WindowMagnificationController", "Invalid rotation change. " + degreeFromRotation);
            return;
        }
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mMirrorView.getLayoutParams();
        Rect rect = this.mTmpRect;
        int i2 = layoutParams.x;
        int i3 = layoutParams.y;
        rect.set(i2, i3, layoutParams.width + i2, layoutParams.height + i3);
        RectF rectF = new RectF(this.mTmpRect);
        matrix.mapRect(rectF);
        float f = rectF.left;
        Rect rect2 = this.mTmpRect;
        moveWindowMagnifier(f - rect2.left, rectF.top - rect2.top);
    }

    private int getDegreeFromRotation(int i, int i2) {
        return (((i2 - i) + 4) % 4) * 90;
    }

    private void createMirrorWindow() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(this.mMagnificationFrame.width() + (this.mMirrorSurfaceMargin * 2), this.mMagnificationFrame.height() + (this.mMirrorSurfaceMargin * 2), 2039, 40, -2);
        layoutParams.gravity = 51;
        Rect rect = this.mMagnificationFrame;
        int i = rect.left;
        int i2 = this.mMirrorSurfaceMargin;
        layoutParams.x = i - i2;
        layoutParams.y = rect.top - i2;
        layoutParams.layoutInDisplayCutoutMode = 1;
        layoutParams.receiveInsetsIgnoringZOrder = true;
        layoutParams.setTitle(this.mContext.getString(R$string.magnification_window_title));
        layoutParams.accessibilityTitle = getAccessibilityWindowTitle();
        View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.window_magnifier_view, (ViewGroup) null);
        this.mMirrorView = inflate;
        SurfaceView surfaceView = (SurfaceView) inflate.findViewById(R$id.surface_view);
        this.mMirrorSurfaceView = surfaceView;
        surfaceView.addOnLayoutChangeListener(this.mMirrorSurfaceViewLayoutChangeListener);
        this.mMirrorView.setSystemUiVisibility(5894);
        this.mMirrorView.addOnLayoutChangeListener(this.mMirrorViewLayoutChangeListener);
        this.mMirrorView.setAccessibilityDelegate(new MirrorWindowA11yDelegate());
        this.mMirrorView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.systemui.accessibility.WindowMagnificationController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                WindowInsets lambda$createMirrorWindow$5;
                lambda$createMirrorWindow$5 = WindowMagnificationController.this.lambda$createMirrorWindow$5(view, windowInsets);
                return lambda$createMirrorWindow$5;
            }
        });
        this.mWm.addView(this.mMirrorView, layoutParams);
        SurfaceHolder holder = this.mMirrorSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setFormat(1);
        addDragTouchListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$createMirrorWindow$5(View view, WindowInsets windowInsets) {
        if (!this.mHandler.hasCallbacks(this.mWindowInsetChangeRunnable)) {
            this.mHandler.post(this.mWindowInsetChangeRunnable);
        }
        return view.onApplyWindowInsets(windowInsets);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        return this.mResources.getString(17039652);
    }

    private void setInitialStartBounds() {
        int min = (Math.min(this.mWindowBounds.width(), this.mWindowBounds.height()) / 2) + (this.mMirrorSurfaceMargin * 2);
        int i = min / 2;
        int width = (this.mWindowBounds.width() / 2) - i;
        int height = (this.mWindowBounds.height() / 2) - i;
        this.mMagnificationFrame.set(width, height, width + min, min + height);
    }

    private void createMirror() {
        SurfaceControl mirrorDisplay = WindowManagerWrapper.getInstance().mirrorDisplay(this.mDisplayId);
        this.mMirrorSurface = mirrorDisplay;
        if (!mirrorDisplay.isValid()) {
            return;
        }
        this.mTransaction.show(this.mMirrorSurface).reparent(this.mMirrorSurface, this.mMirrorSurfaceView.getSurfaceControl());
        modifyWindowMagnification(this.mTransaction);
    }

    private void addDragTouchListeners() {
        this.mDragView = this.mMirrorView.findViewById(R$id.drag_handle);
        this.mLeftDrag = this.mMirrorView.findViewById(R$id.left_handle);
        this.mTopDrag = this.mMirrorView.findViewById(R$id.top_handle);
        this.mRightDrag = this.mMirrorView.findViewById(R$id.right_handle);
        this.mBottomDrag = this.mMirrorView.findViewById(R$id.bottom_handle);
        this.mDragView.setOnTouchListener(this);
        this.mLeftDrag.setOnTouchListener(this);
        this.mTopDrag.setOnTouchListener(this);
        this.mRightDrag.setOnTouchListener(this);
        this.mBottomDrag.setOnTouchListener(this);
    }

    private void modifyWindowMagnification(SurfaceControl.Transaction transaction) {
        this.mSfVsyncFrameProvider.postFrameCallback(this.mMirrorViewGeometryVsyncCallback);
        updateMirrorViewLayout();
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x005b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateMirrorViewLayout() {
        float f;
        int min;
        int i;
        int min2;
        if (!isWindowVisible()) {
            return;
        }
        int width = this.mWindowBounds.width() - this.mMirrorView.getWidth();
        int height = this.mWindowBounds.height() - this.mMirrorView.getHeight();
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.mMirrorView.getLayoutParams();
        Rect rect = this.mMagnificationFrame;
        int i2 = rect.left;
        int i3 = this.mMirrorSurfaceMargin;
        int i4 = i2 - i3;
        layoutParams.x = i4;
        layoutParams.y = rect.top - i3;
        float f2 = 0.0f;
        if (i4 < 0) {
            min = Math.max(i4, -this.mOuterBorderSize);
        } else if (i4 > width) {
            min = Math.min(i4 - width, this.mOuterBorderSize);
        } else {
            f = 0.0f;
            i = layoutParams.y;
            if (i >= 0) {
                min2 = Math.max(i, -this.mOuterBorderSize);
            } else {
                if (i > height) {
                    min2 = Math.min(i - height, this.mOuterBorderSize);
                }
                this.mMirrorView.setTranslationX(f);
                this.mMirrorView.setTranslationY(f2);
                this.mWm.updateViewLayout(this.mMirrorView, layoutParams);
            }
            f2 = min2;
            this.mMirrorView.setTranslationX(f);
            this.mMirrorView.setTranslationY(f2);
            this.mWm.updateViewLayout(this.mMirrorView, layoutParams);
        }
        f = min;
        i = layoutParams.y;
        if (i >= 0) {
        }
        f2 = min2;
        this.mMirrorView.setTranslationX(f);
        this.mMirrorView.setTranslationY(f2);
        this.mWm.updateViewLayout(this.mMirrorView, layoutParams);
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view == this.mDragView || view == this.mLeftDrag || view == this.mTopDrag || view == this.mRightDrag || view == this.mBottomDrag) {
            return this.mGestureDetector.onTouch(motionEvent);
        }
        return false;
    }

    public void updateSysUIStateFlag() {
        updateSysUIState(true);
    }

    private void calculateSourceBounds(Rect rect, float f) {
        int width = rect.width() / 2;
        int height = rect.height() / 2;
        int i = width - ((int) (width / f));
        int i2 = rect.right - i;
        int i3 = height - ((int) (height / f));
        this.mSourceBounds.set(rect.left + i, rect.top + i3, i2, rect.bottom - i3);
    }

    private void setMagnificationFrameBoundary() {
        int width = this.mMagnificationFrame.width() / 2;
        int height = this.mMagnificationFrame.height() / 2;
        float f = this.mScale;
        int i = width - ((int) (width / f));
        int i2 = height - ((int) (height / f));
        this.mMagnificationFrameBoundary.set(-i, -i2, this.mWindowBounds.width() + i, this.mWindowBounds.height() + i2);
    }

    private boolean updateMagnificationFramePosition(int i, int i2) {
        this.mTmpRect.set(this.mMagnificationFrame);
        this.mTmpRect.offset(i, i2);
        Rect rect = this.mTmpRect;
        int i3 = rect.left;
        Rect rect2 = this.mMagnificationFrameBoundary;
        int i4 = rect2.left;
        if (i3 < i4) {
            rect.offsetTo(i4, rect.top);
        } else {
            int i5 = rect.right;
            int i6 = rect2.right;
            if (i5 > i6) {
                int width = i6 - this.mMagnificationFrame.width();
                Rect rect3 = this.mTmpRect;
                rect3.offsetTo(width, rect3.top);
            }
        }
        Rect rect4 = this.mTmpRect;
        int i7 = rect4.top;
        Rect rect5 = this.mMagnificationFrameBoundary;
        int i8 = rect5.top;
        if (i7 < i8) {
            rect4.offsetTo(rect4.left, i8);
        } else {
            int i9 = rect4.bottom;
            int i10 = rect5.bottom;
            if (i9 > i10) {
                int height = i10 - this.mMagnificationFrame.height();
                Rect rect6 = this.mTmpRect;
                rect6.offsetTo(rect6.left, height);
            }
        }
        if (!this.mTmpRect.equals(this.mMagnificationFrame)) {
            this.mMagnificationFrame.set(this.mTmpRect);
            return true;
        }
        return false;
    }

    private void updateSysUIState(boolean z) {
        int i;
        boolean z2 = isWindowVisible() && (i = this.mSystemGestureTop) > 0 && this.mMirrorViewBounds.bottom > i;
        if (z || z2 != this.mOverlapWithGestureInsets) {
            this.mOverlapWithGestureInsets = z2;
            this.mSysUiState.setFlag(524288, z2).commitUpdate(this.mDisplayId);
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        createMirror();
    }

    public void move(int i, int i2) {
        moveWindowMagnifier(i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void enableWindowMagnification(float f, float f2, float f3) {
        float f4 = 0.0f;
        float exactCenterX = Float.isNaN(f2) ? 0.0f : f2 - this.mMagnificationFrame.exactCenterX();
        if (!Float.isNaN(f3)) {
            f4 = f3 - this.mMagnificationFrame.exactCenterY();
        }
        if (Float.isNaN(f)) {
            f = this.mScale;
        }
        this.mScale = f;
        setMagnificationFrameBoundary();
        updateMagnificationFramePosition((int) exactCenterX, (int) f4);
        if (!isWindowVisible()) {
            createMirrorWindow();
            showControls();
            return;
        }
        modifyWindowMagnification(this.mTransaction);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setScale(float f) {
        if (!isWindowVisible() || this.mScale == f) {
            return;
        }
        enableWindowMagnification(f, Float.NaN, Float.NaN);
        this.mHandler.removeCallbacks(this.mUpdateStateDescriptionRunnable);
        this.mHandler.postDelayed(this.mUpdateStateDescriptionRunnable, 100L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void moveWindowMagnifier(float f, float f2) {
        if (this.mMirrorSurfaceView != null && updateMagnificationFramePosition((int) f, (int) f2)) {
            modifyWindowMagnification(this.mTransaction);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getScale() {
        if (isWindowVisible()) {
            return this.mScale;
        }
        return Float.NaN;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCenterX() {
        if (isWindowVisible()) {
            return this.mMagnificationFrame.exactCenterX();
        }
        return Float.NaN;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float getCenterY() {
        if (isWindowVisible()) {
            return this.mMagnificationFrame.exactCenterY();
        }
        return Float.NaN;
    }

    private boolean isWindowVisible() {
        return this.mMirrorView != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CharSequence formatStateDescription(float f) {
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            this.mPercentFormat = NumberFormat.getPercentInstance(locale);
        }
        return this.mPercentFormat.format(f);
    }

    @Override // com.android.systemui.accessibility.MagnificationGestureDetector.OnGestureListener
    public boolean onSingleTap() {
        animateBounceEffect();
        return true;
    }

    @Override // com.android.systemui.accessibility.MagnificationGestureDetector.OnGestureListener
    public boolean onDrag(float f, float f2) {
        moveWindowMagnifier(f, f2);
        return true;
    }

    private void animateBounceEffect() {
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mMirrorView, PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, this.mBounceEffectAnimationScale, 1.0f), PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, this.mBounceEffectAnimationScale, 1.0f));
        ofPropertyValuesHolder.setDuration(this.mBounceEffectDuration);
        ofPropertyValuesHolder.start();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("WindowMagnificationController (displayId=" + this.mDisplayId + "):");
        StringBuilder sb = new StringBuilder();
        sb.append("      mOverlapWithGestureInsets:");
        sb.append(this.mOverlapWithGestureInsets);
        printWriter.println(sb.toString());
        printWriter.println("      mScale:" + this.mScale);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("      mMirrorViewBounds:");
        sb2.append(isWindowVisible() ? this.mMirrorViewBounds : "empty");
        printWriter.println(sb2.toString());
        printWriter.println("      mSystemGestureTop:" + this.mSystemGestureTop);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MirrorWindowA11yDelegate extends View.AccessibilityDelegate {
        private MirrorWindowA11yDelegate() {
        }

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_zoom_in, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_zoom_in)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_zoom_out, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_zoom_out)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_up, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_move_up)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_down, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_move_down)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_left, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_move_left)));
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(R$id.accessibility_action_move_right, WindowMagnificationController.this.mContext.getString(R$string.accessibility_control_move_right)));
            accessibilityNodeInfo.setContentDescription(WindowMagnificationController.this.mContext.getString(R$string.magnification_window_title));
            WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
            accessibilityNodeInfo.setStateDescription(windowMagnificationController.formatStateDescription(windowMagnificationController.getScale()));
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (performA11yAction(i)) {
                return true;
            }
            return super.performAccessibilityAction(view, i, bundle);
        }

        private boolean performA11yAction(int i) {
            if (i == R$id.accessibility_action_zoom_in) {
                WindowMagnificationController.this.mWindowMagnifierCallback.onPerformScaleAction(WindowMagnificationController.this.mDisplayId, ((Float) WindowMagnificationController.A11Y_ACTION_SCALE_RANGE.clamp(Float.valueOf(WindowMagnificationController.this.mScale + 1.0f))).floatValue());
            } else if (i == R$id.accessibility_action_zoom_out) {
                WindowMagnificationController.this.mWindowMagnifierCallback.onPerformScaleAction(WindowMagnificationController.this.mDisplayId, ((Float) WindowMagnificationController.A11Y_ACTION_SCALE_RANGE.clamp(Float.valueOf(WindowMagnificationController.this.mScale - 1.0f))).floatValue());
            } else if (i == R$id.accessibility_action_move_up) {
                WindowMagnificationController windowMagnificationController = WindowMagnificationController.this;
                windowMagnificationController.move(0, -windowMagnificationController.mSourceBounds.height());
            } else if (i == R$id.accessibility_action_move_down) {
                WindowMagnificationController windowMagnificationController2 = WindowMagnificationController.this;
                windowMagnificationController2.move(0, windowMagnificationController2.mSourceBounds.height());
            } else if (i == R$id.accessibility_action_move_left) {
                WindowMagnificationController windowMagnificationController3 = WindowMagnificationController.this;
                windowMagnificationController3.move(-windowMagnificationController3.mSourceBounds.width(), 0);
            } else if (i != R$id.accessibility_action_move_right) {
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
