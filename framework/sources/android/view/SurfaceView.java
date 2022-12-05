package android.view;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.CompatibilityInfo;
import android.graphics.BLASTBufferQueue;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.HardwareRenderer;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityEmbeddedConnection;
import com.android.internal.view.SurfaceCallbackHelper;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes3.dex */
public class SurfaceView extends View implements ViewRootImpl.SurfaceChangedCallback {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_POSITION = false;
    private static final String TAG = "SurfaceView";
    private static final String[] sAppBlackList = {"android.graphics.cts", "com.android.chrome", "com.brave.browser", "com.opera.browser", "org.mozilla.firefox", "org.torproject.torbrowser", "com.sec.android.app.sbrowser", "com.kiwibrowser.browser", "com.microsoft.emmx", "com.hsv.freeadblockerbrowser", "com.vivaldi.browser", "org.torproject.torbrowser", "com.yandex.browser", "org.adblockplus.browser"};
    private boolean mAttachedToWindow;
    int mBackgroundColor;
    SurfaceControl mBackgroundControl;
    private BLASTBufferQueue mBlastBufferQueue;
    private SurfaceControl mBlastSurfaceControl;
    final ArrayList<SurfaceHolder.Callback> mCallbacks;
    boolean mClipSurfaceToBounds;
    float mCornerRadius;
    SurfaceControl mDeferredDestroySurfaceControl;
    private boolean mDisableBackgroundLayer;
    boolean mDrawFinished;
    private final ViewTreeObserver.OnPreDrawListener mDrawListener;
    boolean mDrawingStopped;
    int mFormat;
    private final SurfaceControl.Transaction mFrameCallbackTransaction;
    private boolean mGlobalListenersAdded;
    boolean mHaveFrame;
    boolean mIsCreating;
    private boolean mIsInBlackList;
    long mLastLockTime;
    int mLastSurfaceHeight;
    int mLastSurfaceWidth;
    boolean mLastWindowVisibility;
    final int[] mLocation;
    private int mParentSurfaceSequenceId;
    private int mPendingReportDraws;
    private SurfaceViewPositionUpdateListener mPositionListener;
    private Rect mRTLastReportedPosition;
    private Point mRTLastReportedSurfaceSize;
    private RemoteAccessibilityController mRemoteAccessibilityController;
    int mRequestedFormat;
    int mRequestedHeight;
    boolean mRequestedVisible;
    int mRequestedWidth;
    Paint mRoundedViewportPaint;
    private volatile boolean mRtHandlingPositionUpdates;
    private volatile boolean mRtReleaseSurfaces;
    private final SurfaceControl.Transaction mRtTransaction;
    final Rect mScreenRect;
    private final ViewTreeObserver.OnScrollChangedListener mScrollChangedListener;
    int mSubLayer;
    final Surface mSurface;
    float mSurfaceAlpha;
    SurfaceControl mSurfaceControl;
    final Object mSurfaceControlLock;
    boolean mSurfaceCreated;
    private int mSurfaceFlags;
    final Rect mSurfaceFrame;
    int mSurfaceHeight;
    private final SurfaceHolder mSurfaceHolder;
    final ReentrantLock mSurfaceLock;
    SurfaceControlViewHost.SurfacePackage mSurfacePackage;
    private final SurfaceSession mSurfaceSession;
    int mSurfaceWidth;
    private final Matrix mTmpMatrix;
    final Rect mTmpRect;
    private final SurfaceControl.Transaction mTmpTransaction;
    int mTransformHint;
    boolean mUseAlpha;
    private final boolean mUseBlastAdapter;
    private final boolean mUseBlastSync;
    private boolean mUseLowRefreshRate;
    boolean mViewVisibility;
    boolean mVisible;
    int mWindowSpaceLeft;
    int mWindowSpaceTop;
    boolean mWindowStopped;
    boolean mWindowVisibility;

    public /* synthetic */ boolean lambda$new$0$SurfaceView() {
        this.mHaveFrame = getWidth() > 0 && getHeight() > 0;
        updateSurface();
        return true;
    }

    private static boolean useBlastAdapter(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.Global.getInt(contentResolver, Settings.Global.DEVELOPMENT_USE_BLAST_ADAPTER_SV, 1) == 1;
    }

    public SurfaceView(Context context) {
        this(context, null);
    }

    public SurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr, defStyleRes, false);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, boolean disableBackgroundLayer) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCallbacks = new ArrayList<>();
        this.mLocation = new int[2];
        this.mSurfaceLock = new ReentrantLock();
        this.mSurface = new Surface();
        this.mDrawingStopped = true;
        this.mDrawFinished = false;
        this.mScreenRect = new Rect();
        this.mSurfaceSession = new SurfaceSession();
        this.mDisableBackgroundLayer = false;
        this.mSurfaceControlLock = new Object();
        this.mTmpRect = new Rect();
        this.mSubLayer = -2;
        this.mIsCreating = false;
        this.mRtHandlingPositionUpdates = false;
        this.mRtReleaseSurfaces = false;
        this.mScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() { // from class: android.view.SurfaceView$$ExternalSyntheticLambda3
            @Override // android.view.ViewTreeObserver.OnScrollChangedListener
            public final void onScrollChanged() {
                SurfaceView.this.updateSurface();
            }
        };
        this.mDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: android.view.SurfaceView$$ExternalSyntheticLambda2
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public final boolean onPreDraw() {
                return SurfaceView.this.lambda$new$0$SurfaceView();
            }
        };
        this.mRequestedVisible = false;
        this.mWindowVisibility = false;
        this.mLastWindowVisibility = false;
        this.mViewVisibility = false;
        this.mWindowStopped = false;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mRequestedFormat = 4;
        this.mUseAlpha = false;
        this.mSurfaceAlpha = 1.0f;
        this.mBackgroundColor = -16777216;
        this.mHaveFrame = false;
        this.mSurfaceCreated = false;
        this.mLastLockTime = 0L;
        this.mVisible = false;
        this.mWindowSpaceLeft = -1;
        this.mWindowSpaceTop = -1;
        this.mSurfaceWidth = -1;
        this.mSurfaceHeight = -1;
        this.mFormat = -1;
        this.mSurfaceFrame = new Rect();
        this.mLastSurfaceWidth = -1;
        this.mLastSurfaceHeight = -1;
        this.mTransformHint = 0;
        this.mSurfaceFlags = 4;
        this.mRtTransaction = new SurfaceControl.Transaction();
        this.mFrameCallbackTransaction = new SurfaceControl.Transaction();
        this.mTmpTransaction = new SurfaceControl.Transaction();
        this.mRemoteAccessibilityController = new RemoteAccessibilityController(this);
        this.mTmpMatrix = new Matrix();
        this.mUseBlastSync = true;
        this.mUseLowRefreshRate = false;
        this.mIsInBlackList = false;
        this.mRTLastReportedPosition = new Rect();
        this.mRTLastReportedSurfaceSize = new Point();
        this.mPositionListener = null;
        this.mSurfaceHolder = new AnonymousClass1();
        this.mUseBlastAdapter = useBlastAdapter(context);
        setWillNotDraw(true);
        this.mDisableBackgroundLayer = disableBackgroundLayer;
    }

    public SurfaceHolder getHolder() {
        return this.mSurfaceHolder;
    }

    private void updateRequestedVisibility() {
        this.mRequestedVisible = this.mViewVisibility && this.mWindowVisibility && !this.mWindowStopped;
    }

    private void setWindowStopped(boolean stopped) {
        this.mWindowStopped = stopped;
        updateRequestedVisibility();
        updateSurface();
    }

    private boolean isAppInBlackList(String name) {
        int i = 0;
        while (true) {
            String[] strArr = sAppBlackList;
            if (i < strArr.length) {
                String packageName = strArr[i];
                if (name.indexOf(packageName) <= -1) {
                    i++;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewRootImpl().addSurfaceChangedCallback(this);
        boolean z = false;
        this.mWindowStopped = false;
        if (getVisibility() == 0) {
            z = true;
        }
        this.mViewVisibility = z;
        updateRequestedVisibility();
        this.mAttachedToWindow = true;
        this.mParent.requestTransparentRegion(this);
        if (!this.mGlobalListenersAdded) {
            ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnScrollChangedListener(this.mScrollChangedListener);
            observer.addOnPreDrawListener(this.mDrawListener);
            this.mGlobalListenersAdded = true;
        }
        String name = getViewRootImpl().getTitle().toString();
        this.mIsInBlackList = isAppInBlackList(name);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mWindowVisibility = visibility == 0;
        updateRequestedVisibility();
        updateSurface();
    }

    @Override // android.view.View
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean newRequestedVisible = true;
        boolean z = visibility == 0;
        this.mViewVisibility = z;
        if (!this.mWindowVisibility || !z || this.mWindowStopped) {
            newRequestedVisible = false;
        }
        if (newRequestedVisible != this.mRequestedVisible) {
            requestLayout();
        }
        this.mRequestedVisible = newRequestedVisible;
        updateSurface();
    }

    public void setUseAlpha() {
        if (!this.mUseAlpha) {
            this.mUseAlpha = true;
            updateSurfaceAlpha();
        }
    }

    @Override // android.view.View
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        updateSurfaceAlpha();
    }

    private float getFixedAlpha() {
        float alpha = getAlpha();
        if (!this.mUseAlpha || (this.mSubLayer <= 0 && alpha != 0.0f)) {
            return 1.0f;
        }
        return alpha;
    }

    private void updateSurfaceAlpha() {
        ViewRootImpl viewRoot;
        final Surface parent;
        if (!this.mUseAlpha) {
            return;
        }
        float viewAlpha = getAlpha();
        if (this.mSubLayer < 0 && 0.0f < viewAlpha && viewAlpha < 1.0f) {
            Log.w(TAG, System.identityHashCode(this) + " updateSurfaceAlpha: translucent color is not supported for a surface placed z-below.");
        }
        if (!this.mHaveFrame || (viewRoot = getViewRootImpl()) == null || this.mSurfaceControl == null || (parent = viewRoot.mSurface) == null || !parent.isValid()) {
            return;
        }
        final float alpha = getFixedAlpha();
        if (alpha != this.mSurfaceAlpha) {
            if (isHardwareAccelerated()) {
                viewRoot.registerRtFrameCallback(new HardwareRenderer.FrameDrawingCallback() { // from class: android.view.SurfaceView$$ExternalSyntheticLambda1
                    @Override // android.graphics.HardwareRenderer.FrameDrawingCallback
                    public final void onFrameDraw(long j) {
                        SurfaceView.this.lambda$updateSurfaceAlpha$1$SurfaceView(parent, alpha, j);
                    }
                });
                damageInParent();
            } else {
                this.mTmpTransaction.setAlpha(this.mSurfaceControl, alpha).apply();
            }
            this.mSurfaceAlpha = alpha;
        }
    }

    public /* synthetic */ void lambda$updateSurfaceAlpha$1$SurfaceView(Surface parent, float alpha, long frame) {
        try {
            synchronized (this.mSurfaceControlLock) {
                if (!parent.isValid()) {
                    return;
                }
                SurfaceControl surfaceControl = this.mSurfaceControl;
                if (surfaceControl == null) {
                    return;
                }
                this.mFrameCallbackTransaction.setAlpha(surfaceControl, alpha);
                applyOrMergeTransaction(this.mFrameCallbackTransaction, frame);
            }
        } catch (Exception e) {
            Log.e(TAG, System.identityHashCode(this) + "updateSurfaceAlpha RT: Exception during surface transaction", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void performDrawFinished() {
        if (this.mDeferredDestroySurfaceControl != null) {
            synchronized (this.mSurfaceControlLock) {
                this.mTmpTransaction.remove(this.mDeferredDestroySurfaceControl).apply();
                this.mDeferredDestroySurfaceControl = null;
            }
        }
        if (this.mPendingReportDraws > 0) {
            this.mDrawFinished = true;
            if (this.mAttachedToWindow) {
                this.mParent.requestTransparentRegion(this);
                notifyDrawFinished();
                invalidate();
                return;
            }
            return;
        }
        Log.e(TAG, System.identityHashCode(this) + "finished drawing but no pending report draw (extra call to draw completion runnable?)");
    }

    void notifyDrawFinished() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.pendingDrawFinished();
        }
        this.mPendingReportDraws--;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.removeSurfaceChangedCallback(this);
        }
        this.mAttachedToWindow = false;
        if (this.mGlobalListenersAdded) {
            ViewTreeObserver observer = getViewTreeObserver();
            observer.removeOnScrollChangedListener(this.mScrollChangedListener);
            observer.removeOnPreDrawListener(this.mDrawListener);
            this.mGlobalListenersAdded = false;
        }
        while (this.mPendingReportDraws > 0) {
            notifyDrawFinished();
        }
        this.mRequestedVisible = false;
        updateSurface();
        tryReleaseSurfaces();
        SurfaceControlViewHost.SurfacePackage surfacePackage = this.mSurfacePackage;
        if (surfacePackage != null) {
            SurfaceControl sc = surfacePackage.getSurfaceControl();
            if (sc != null && sc.isValid()) {
                this.mTmpTransaction.reparent(sc, null).apply();
            }
            this.mSurfacePackage.release();
            this.mSurfacePackage = null;
        }
        this.mHaveFrame = false;
        super.onDetachedFromWindow();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int i = this.mRequestedWidth;
        if (i >= 0) {
            width = resolveSizeAndState(i, widthMeasureSpec, 0);
        } else {
            width = getDefaultSize(0, widthMeasureSpec);
        }
        int i2 = this.mRequestedHeight;
        if (i2 >= 0) {
            height = resolveSizeAndState(i2, heightMeasureSpec, 0);
        } else {
            height = getDefaultSize(0, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public boolean setFrame(int left, int top, int right, int bottom) {
        boolean result = super.setFrame(left, top, right, bottom);
        updateSurface();
        return result;
    }

    @Override // android.view.View
    public boolean gatherTransparentRegion(Region region) {
        if (isAboveParent() || !this.mDrawFinished) {
            return super.gatherTransparentRegion(region);
        }
        boolean opaque = true;
        if ((this.mPrivateFlags & 128) == 0) {
            opaque = super.gatherTransparentRegion(region);
        } else if (region != null) {
            int w = getWidth();
            int h = getHeight();
            if (w > 0 && h > 0) {
                getLocationInWindow(this.mLocation);
                int[] iArr = this.mLocation;
                int l = iArr[0];
                int t = iArr[1];
                region.op(l, t, l + w, t + h, Region.Op.UNION);
            }
        }
        if (PixelFormat.formatHasAlpha(this.mRequestedFormat)) {
            return false;
        }
        return opaque;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (this.mDrawFinished && !isAboveParent() && (this.mPrivateFlags & 128) == 0) {
            clearSurfaceViewPort(canvas);
        }
        super.draw(canvas);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void dispatchDraw(Canvas canvas) {
        if (this.mDrawFinished && !isAboveParent() && (this.mPrivateFlags & 128) == 128) {
            clearSurfaceViewPort(canvas);
        }
        super.dispatchDraw(canvas);
    }

    public void setEnableSurfaceClipping(boolean enabled) {
        this.mClipSurfaceToBounds = enabled;
        invalidate();
    }

    @Override // android.view.View
    public void setClipBounds(Rect clipBounds) {
        super.setClipBounds(clipBounds);
        if (!this.mClipSurfaceToBounds) {
            return;
        }
        if (this.mCornerRadius > 0.0f && !isAboveParent()) {
            invalidate();
        }
        if (this.mSurfaceControl != null) {
            if (this.mClipBounds != null) {
                this.mTmpRect.set(this.mClipBounds);
            } else {
                this.mTmpRect.set(0, 0, this.mSurfaceWidth, this.mSurfaceHeight);
            }
            SyncRtSurfaceTransactionApplier applier = new SyncRtSurfaceTransactionApplier(this);
            applier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(this.mSurfaceControl).withWindowCrop(this.mTmpRect).build());
        }
    }

    private void clearSurfaceViewPort(Canvas canvas) {
        if (this.mCornerRadius > 0.0f) {
            canvas.getClipBounds(this.mTmpRect);
            if (this.mClipSurfaceToBounds && this.mClipBounds != null) {
                this.mTmpRect.intersect(this.mClipBounds);
            }
            float f = this.mCornerRadius;
            canvas.punchHole(this.mTmpRect.left, this.mTmpRect.top, this.mTmpRect.right, this.mTmpRect.bottom, f, f);
            return;
        }
        canvas.punchHole(0.0f, 0.0f, getWidth(), getHeight(), 0.0f, 0.0f);
    }

    public void setCornerRadius(float cornerRadius) {
        this.mCornerRadius = cornerRadius;
        if (cornerRadius > 0.0f && this.mRoundedViewportPaint == null) {
            Paint paint = new Paint(1);
            this.mRoundedViewportPaint = paint;
            paint.setBlendMode(BlendMode.CLEAR);
            this.mRoundedViewportPaint.setColor(0);
        }
        invalidate();
    }

    public float getCornerRadius() {
        return this.mCornerRadius;
    }

    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        this.mSubLayer = isMediaOverlay ? -1 : -2;
    }

    public void setZOrderOnTop(boolean onTop) {
        boolean allowDynamicChange = getContext().getApplicationInfo().targetSdkVersion > 29;
        setZOrderedOnTop(onTop, allowDynamicChange);
    }

    public boolean isZOrderedOnTop() {
        return this.mSubLayer > 0;
    }

    public boolean setZOrderedOnTop(boolean onTop, boolean allowDynamicChange) {
        int subLayer;
        ViewRootImpl viewRoot;
        final Surface parent;
        if (onTop) {
            subLayer = 1;
        } else {
            subLayer = -2;
        }
        if (this.mSubLayer == subLayer) {
            return false;
        }
        this.mSubLayer = subLayer;
        if (!allowDynamicChange) {
            return false;
        }
        if (this.mSurfaceControl == null || (viewRoot = getViewRootImpl()) == null || (parent = viewRoot.mSurface) == null || !parent.isValid()) {
            return true;
        }
        viewRoot.registerRtFrameCallback(new HardwareRenderer.FrameDrawingCallback() { // from class: android.view.SurfaceView$$ExternalSyntheticLambda0
            @Override // android.graphics.HardwareRenderer.FrameDrawingCallback
            public final void onFrameDraw(long j) {
                SurfaceView.this.lambda$setZOrderedOnTop$2$SurfaceView(parent, j);
            }
        });
        invalidate();
        return true;
    }

    public /* synthetic */ void lambda$setZOrderedOnTop$2$SurfaceView(Surface parent, long frame) {
        try {
            synchronized (this.mSurfaceControlLock) {
                if (parent.isValid() && this.mSurfaceControl != null) {
                    updateRelativeZ(this.mFrameCallbackTransaction);
                    applyOrMergeTransaction(this.mFrameCallbackTransaction, frame);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, System.identityHashCode(this) + "setZOrderOnTop RT: Exception during surface transaction", e);
        }
    }

    public void setSecure(boolean isSecure) {
        if (isSecure) {
            this.mSurfaceFlags |= 128;
        } else {
            this.mSurfaceFlags &= -129;
        }
    }

    public void setProtected(boolean isProtected) {
        if (isProtected) {
            this.mSurfaceFlags |= 2048;
        } else {
            this.mSurfaceFlags &= -2049;
        }
    }

    private void updateOpaqueFlag() {
        if (!PixelFormat.formatHasAlpha(this.mRequestedFormat)) {
            this.mSurfaceFlags |= 1024;
        } else {
            this.mSurfaceFlags &= -1025;
        }
    }

    private void updateBackgroundVisibility(SurfaceControl.Transaction t) {
        SurfaceControl surfaceControl = this.mBackgroundControl;
        if (surfaceControl == null) {
            return;
        }
        if (this.mSubLayer < 0 && (this.mSurfaceFlags & 1024) != 0 && !this.mDisableBackgroundLayer) {
            t.show(surfaceControl);
        } else {
            t.hide(surfaceControl);
        }
    }

    private SurfaceControl.Transaction updateBackgroundColor(SurfaceControl.Transaction t) {
        float[] colorComponents = {Color.red(this.mBackgroundColor) / 255.0f, Color.green(this.mBackgroundColor) / 255.0f, Color.blue(this.mBackgroundColor) / 255.0f};
        t.setColor(this.mBackgroundControl, colorComponents);
        return t;
    }

    private void tryReleaseSurfaces() {
        this.mSurfaceAlpha = 1.0f;
        synchronized (this.mSurfaceControlLock) {
            this.mSurface.destroy();
            BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
            if (bLASTBufferQueue != null) {
                bLASTBufferQueue.destroy();
                this.mBlastBufferQueue = null;
            }
            if (this.mRtHandlingPositionUpdates) {
                this.mRtReleaseSurfaces = true;
                return;
            }
            releaseSurfaces(this.mTmpTransaction);
            this.mTmpTransaction.apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseSurfaces(SurfaceControl.Transaction transaction) {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null) {
            transaction.remove(surfaceControl);
            this.mSurfaceControl = null;
        }
        SurfaceControl surfaceControl2 = this.mBackgroundControl;
        if (surfaceControl2 != null) {
            transaction.remove(surfaceControl2);
            this.mBackgroundControl = null;
        }
        SurfaceControl surfaceControl3 = this.mBlastSurfaceControl;
        if (surfaceControl3 != null) {
            transaction.remove(surfaceControl3);
            this.mBlastSurfaceControl = null;
        }
    }

    private void replacePositionUpdateListener(int surfaceWidth, int surfaceHeight, SurfaceControl.Transaction geometryTransaction) {
        if (this.mPositionListener != null) {
            this.mRenderNode.removePositionUpdateListener(this.mPositionListener);
        }
        this.mPositionListener = new SurfaceViewPositionUpdateListener(surfaceWidth, surfaceHeight, geometryTransaction);
        this.mRenderNode.addPositionUpdateListener(this.mPositionListener);
    }

    private boolean performSurfaceTransaction(ViewRootImpl viewRoot, CompatibilityInfo.Translator translator, boolean creating, boolean sizeChanged, boolean hintChanged) {
        this.mSurfaceLock.lock();
        try {
            boolean z = true;
            this.mDrawingStopped = !this.mVisible;
            if (creating || this.mParentSurfaceSequenceId == viewRoot.getSurfaceSequenceId()) {
                updateRelativeZ(this.mTmpTransaction);
            }
            this.mParentSurfaceSequenceId = viewRoot.getSurfaceSequenceId();
            if (this.mViewVisibility) {
                this.mTmpTransaction.show(this.mSurfaceControl);
            } else {
                this.mTmpTransaction.hide(this.mSurfaceControl);
            }
            SurfaceControlViewHost.SurfacePackage surfacePackage = this.mSurfacePackage;
            if (surfacePackage != null) {
                reparentSurfacePackage(this.mTmpTransaction, surfacePackage);
            }
            updateBackgroundVisibility(this.mTmpTransaction);
            updateBackgroundColor(this.mTmpTransaction);
            if (this.mUseAlpha) {
                float alpha = getFixedAlpha();
                this.mTmpTransaction.setAlpha(this.mSurfaceControl, alpha);
                this.mSurfaceAlpha = alpha;
            }
            SurfaceControl.Transaction geometryTransaction = new SurfaceControl.Transaction();
            geometryTransaction.setCornerRadius(this.mSurfaceControl, this.mCornerRadius);
            if ((sizeChanged || hintChanged) && !creating) {
                setBufferSize(geometryTransaction);
            }
            if (sizeChanged || creating || !isHardwareAccelerated()) {
                onSetSurfacePositionAndScaleRT(geometryTransaction, this.mSurfaceControl, this.mScreenRect.left, this.mScreenRect.top, this.mScreenRect.width() / this.mSurfaceWidth, this.mScreenRect.height() / this.mSurfaceHeight);
                if (!this.mClipSurfaceToBounds || this.mClipBounds == null) {
                    geometryTransaction.setWindowCrop(this.mSurfaceControl, this.mSurfaceWidth, this.mSurfaceHeight);
                } else {
                    geometryTransaction.setWindowCrop(this.mSurfaceControl, this.mClipBounds);
                }
                boolean applyChangesOnRenderThread = sizeChanged && !creating && isHardwareAccelerated();
                if (isHardwareAccelerated()) {
                    replacePositionUpdateListener(this.mSurfaceWidth, this.mSurfaceHeight, applyChangesOnRenderThread ? geometryTransaction : null);
                }
            }
            this.mTmpTransaction.merge(geometryTransaction);
            this.mTmpTransaction.apply();
            updateEmbeddedAccessibilityMatrix();
            this.mSurfaceFrame.left = 0;
            this.mSurfaceFrame.top = 0;
            if (translator == null) {
                this.mSurfaceFrame.right = this.mSurfaceWidth;
                this.mSurfaceFrame.bottom = this.mSurfaceHeight;
            } else {
                float appInvertedScale = translator.applicationInvertedScale;
                this.mSurfaceFrame.right = (int) ((this.mSurfaceWidth * appInvertedScale) + 0.5f);
                this.mSurfaceFrame.bottom = (int) ((this.mSurfaceHeight * appInvertedScale) + 0.5f);
            }
            int surfaceWidth = this.mSurfaceFrame.right;
            int surfaceHeight = this.mSurfaceFrame.bottom;
            if (this.mLastSurfaceWidth == surfaceWidth && this.mLastSurfaceHeight == surfaceHeight) {
                z = false;
            }
            boolean realSizeChanged = z;
            this.mLastSurfaceWidth = surfaceWidth;
            this.mLastSurfaceHeight = surfaceHeight;
            return realSizeChanged;
        } finally {
            this.mSurfaceLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x020c, code lost:
        if (r17 != false) goto L101;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x026b, code lost:
        r4 = getSurfaceCallbacks();
     */
    /* JADX WARN: Removed duplicated region for block: B:100:0x0206  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x0288  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateSurface() {
        ViewRootImpl viewRoot;
        boolean redrawNeeded;
        if (this.mHaveFrame && (viewRoot = getViewRootImpl()) != null) {
            if (viewRoot.mSurface == null || !viewRoot.mSurface.isValid()) {
                notifySurfaceDestroyed();
                tryReleaseSurfaces();
                return;
            }
            CompatibilityInfo.Translator translator = viewRoot.mTranslator;
            if (translator != null) {
                this.mSurface.setCompatibilityTranslator(translator);
            }
            int myWidth = this.mRequestedWidth;
            if (myWidth <= 0) {
                myWidth = getWidth();
            }
            int myWidth2 = myWidth;
            int myHeight = this.mRequestedHeight;
            if (myHeight <= 0) {
                myHeight = getHeight();
            }
            int myHeight2 = myHeight;
            float alpha = getFixedAlpha();
            boolean formatChanged = this.mFormat != this.mRequestedFormat;
            boolean z = this.mVisible;
            boolean z2 = this.mRequestedVisible;
            boolean visibleChanged = z != z2;
            boolean alphaChanged = this.mSurfaceAlpha != alpha;
            boolean creating = (this.mSurfaceControl == null || formatChanged || visibleChanged) && z2;
            boolean sizeChanged = (this.mSurfaceWidth == myWidth2 && this.mSurfaceHeight == myHeight2) ? false : true;
            boolean windowVisibleChanged = this.mWindowVisibility != this.mLastWindowVisibility;
            getLocationInSurface(this.mLocation);
            int i = this.mWindowSpaceLeft;
            int[] iArr = this.mLocation;
            boolean positionChanged = (i == iArr[0] && this.mWindowSpaceTop == iArr[1]) ? false : true;
            boolean layoutSizeChanged = (getWidth() == this.mScreenRect.width() && getHeight() == this.mScreenRect.height()) ? false : true;
            boolean hintChanged = viewRoot.getSurfaceTransformHint() != this.mTransformHint && this.mRequestedVisible;
            if (!creating && !formatChanged && !sizeChanged && !visibleChanged && ((!this.mUseAlpha || !alphaChanged) && !windowVisibleChanged && !positionChanged && !layoutSizeChanged && !hintChanged)) {
                return;
            }
            getLocationInWindow(this.mLocation);
            try {
                this.mVisible = this.mRequestedVisible;
                int[] iArr2 = this.mLocation;
                this.mWindowSpaceLeft = iArr2[0];
                this.mWindowSpaceTop = iArr2[1];
                this.mSurfaceWidth = myWidth2;
                this.mSurfaceHeight = myHeight2;
                this.mFormat = this.mRequestedFormat;
                this.mLastWindowVisibility = this.mWindowVisibility;
                this.mTransformHint = viewRoot.getSurfaceTransformHint();
                this.mScreenRect.left = this.mWindowSpaceLeft;
                this.mScreenRect.top = this.mWindowSpaceTop;
                this.mScreenRect.right = this.mWindowSpaceLeft + getWidth();
                this.mScreenRect.bottom = this.mWindowSpaceTop + getHeight();
                if (!this.mIsInBlackList) {
                    try {
                        if (this.mVisible) {
                            Display dp = getDisplay();
                            int physicalHeight = dp.getHeight();
                            int viewHeight = this.mScreenRect.bottom - this.mScreenRect.top;
                            if (viewHeight > physicalHeight / 2 && !this.mUseLowRefreshRate) {
                                Log.i(TAG, "surfaceview vote min fps");
                                viewRoot.mVoteMinRefreshRateNum++;
                                this.mUseLowRefreshRate = true;
                                viewRoot.scheduleTraversals();
                            } else if (viewHeight <= physicalHeight / 2 && this.mUseLowRefreshRate) {
                                Log.i(TAG, "surfaceview vote none");
                                viewRoot.mVoteMinRefreshRateNum--;
                                this.mUseLowRefreshRate = false;
                                viewRoot.scheduleTraversals();
                            }
                        } else {
                            if (this.mUseLowRefreshRate) {
                                viewRoot.mVoteMinRefreshRateNum--;
                            }
                            this.mUseLowRefreshRate = false;
                        }
                    } catch (Exception e) {
                        ex = e;
                        Log.e(TAG, "Exception configuring surface", ex);
                        return;
                    }
                }
                if (translator != null) {
                    translator.translateRectInAppWindowToScreen(this.mScreenRect);
                }
                Rect surfaceInsets = viewRoot.mWindowAttributes.surfaceInsets;
                this.mScreenRect.offset(surfaceInsets.left, surfaceInsets.top);
                if (creating) {
                    updateOpaqueFlag();
                    String name = "SurfaceView[" + viewRoot.getTitle().toString() + "]";
                    if (this.mUseBlastAdapter) {
                        createBlastSurfaceControls(viewRoot, name);
                    } else {
                        this.mDeferredDestroySurfaceControl = createSurfaceControls(viewRoot, name);
                    }
                } else if (this.mSurfaceControl == null) {
                    return;
                }
                try {
                    boolean realSizeChanged = performSurfaceTransaction(viewRoot, translator, creating, sizeChanged, hintChanged);
                    try {
                        if (!sizeChanged && !creating && !hintChanged) {
                            try {
                                if (!this.mVisible || this.mDrawFinished) {
                                    redrawNeeded = false;
                                    SurfaceHolder.Callback[] callbacks = null;
                                    if (this.mSurfaceCreated) {
                                        if (!creating) {
                                            try {
                                                if (!this.mVisible) {
                                                }
                                            } catch (Throwable th) {
                                                th = th;
                                                this.mIsCreating = false;
                                                if (this.mSurfaceControl != null && !this.mSurfaceCreated) {
                                                    tryReleaseSurfaces();
                                                }
                                                throw th;
                                            }
                                        }
                                        this.mSurfaceCreated = false;
                                        notifySurfaceDestroyed();
                                    }
                                    copySurface(creating, sizeChanged);
                                    if (!this.mVisible && this.mSurface.isValid()) {
                                        if (!this.mSurfaceCreated && (creating || visibleChanged)) {
                                            this.mSurfaceCreated = true;
                                            this.mIsCreating = true;
                                            if (0 == 0) {
                                                callbacks = getSurfaceCallbacks();
                                            }
                                            int length = callbacks.length;
                                            int i2 = 0;
                                            while (i2 < length) {
                                                SurfaceHolder.Callback c = callbacks[i2];
                                                c.surfaceCreated(this.mSurfaceHolder);
                                                i2++;
                                                callbacks = callbacks;
                                            }
                                        }
                                        if (!creating && !formatChanged && !sizeChanged && !hintChanged && !visibleChanged && !realSizeChanged) {
                                            if (redrawNeeded) {
                                                if (callbacks == null) {
                                                    callbacks = getSurfaceCallbacks();
                                                }
                                                this.mPendingReportDraws++;
                                                viewRoot.drawPending();
                                                SurfaceCallbackHelper sch = new SurfaceCallbackHelper(new Runnable() { // from class: android.view.SurfaceView$$ExternalSyntheticLambda4
                                                    @Override // java.lang.Runnable
                                                    public final void run() {
                                                        SurfaceView.this.onDrawFinished();
                                                    }
                                                });
                                                sch.dispatchSurfaceRedrawNeededAsync(this.mSurfaceHolder, callbacks);
                                            }
                                        }
                                        int length2 = callbacks.length;
                                        int i3 = 0;
                                        while (i3 < length2) {
                                            SurfaceHolder.Callback c2 = callbacks[i3];
                                            int i4 = length2;
                                            boolean realSizeChanged2 = realSizeChanged;
                                            try {
                                                c2.surfaceChanged(this.mSurfaceHolder, this.mFormat, myWidth2, myHeight2);
                                                i3++;
                                                length2 = i4;
                                                realSizeChanged = realSizeChanged2;
                                            } catch (Throwable th2) {
                                                th = th2;
                                                this.mIsCreating = false;
                                                if (this.mSurfaceControl != null) {
                                                    tryReleaseSurfaces();
                                                }
                                                throw th;
                                            }
                                        }
                                        if (redrawNeeded) {
                                        }
                                    }
                                    this.mIsCreating = false;
                                    if (this.mSurfaceControl != null && !this.mSurfaceCreated) {
                                        tryReleaseSurfaces();
                                    }
                                    return;
                                }
                            } catch (Exception e2) {
                                ex = e2;
                                Log.e(TAG, "Exception configuring surface", ex);
                                return;
                            }
                        }
                        if (this.mSurfaceCreated) {
                        }
                        copySurface(creating, sizeChanged);
                        if (!this.mVisible) {
                        }
                        this.mIsCreating = false;
                        if (this.mSurfaceControl != null) {
                            tryReleaseSurfaces();
                        }
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                    }
                    redrawNeeded = true;
                    SurfaceHolder.Callback[] callbacks2 = null;
                } catch (Exception e3) {
                    ex = e3;
                }
            } catch (Exception e4) {
                ex = e4;
            }
        }
    }

    private void copySurface(boolean surfaceControlCreated, boolean bufferSizeChanged) {
        if (surfaceControlCreated) {
            if (this.mUseBlastAdapter) {
                this.mSurface.copyFrom(this.mBlastBufferQueue);
            } else {
                this.mSurface.copyFrom(this.mSurfaceControl);
            }
        }
        if (bufferSizeChanged && getContext().getApplicationInfo().targetSdkVersion < 26) {
            if (this.mUseBlastAdapter) {
                BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
                if (bLASTBufferQueue != null) {
                    this.mSurface.transferFrom(bLASTBufferQueue.createSurfaceWithHandle());
                    return;
                }
                return;
            }
            this.mSurface.createFrom(this.mSurfaceControl);
        }
    }

    private void setBufferSize(SurfaceControl.Transaction transaction) {
        if (this.mUseBlastAdapter) {
            this.mBlastSurfaceControl.setTransformHint(this.mTransformHint);
            BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
            if (bLASTBufferQueue != null) {
                bLASTBufferQueue.update(this.mBlastSurfaceControl, this.mSurfaceWidth, this.mSurfaceHeight, this.mFormat, transaction);
                return;
            }
            return;
        }
        transaction.setBufferSize(this.mSurfaceControl, this.mSurfaceWidth, this.mSurfaceHeight);
    }

    private SurfaceControl createSurfaceControls(ViewRootImpl viewRoot, String name) {
        SurfaceControl previousSurfaceControl = this.mSurfaceControl;
        this.mSurfaceControl = new SurfaceControl.Builder(this.mSurfaceSession).setName(name).setLocalOwnerView(this).setParent(viewRoot.getBoundsLayer()).setCallsite("SurfaceView.updateSurface").setBufferSize(this.mSurfaceWidth, this.mSurfaceHeight).setFlags(this.mSurfaceFlags).setFormat(this.mFormat).build();
        this.mBackgroundControl = createBackgroundControl(name);
        return previousSurfaceControl;
    }

    private SurfaceControl createBackgroundControl(String name) {
        SurfaceControl.Builder builder = new SurfaceControl.Builder(this.mSurfaceSession);
        return builder.setName("Background for " + name).setLocalOwnerView(this).setOpaque(true).setColorLayer().setParent(this.mSurfaceControl).setCallsite("SurfaceView.updateSurface").build();
    }

    private void createBlastSurfaceControls(ViewRootImpl viewRoot, String name) {
        if (this.mSurfaceControl == null) {
            this.mSurfaceControl = new SurfaceControl.Builder(this.mSurfaceSession).setName(name).setLocalOwnerView(this).setParent(viewRoot.getBoundsLayer()).setCallsite("SurfaceView.updateSurface").setContainerLayer().build();
        }
        SurfaceControl surfaceControl = this.mBlastSurfaceControl;
        boolean z = false;
        if (surfaceControl == null) {
            this.mBlastSurfaceControl = new SurfaceControl.Builder(this.mSurfaceSession).setName(name + "(BLAST)").setLocalOwnerView(this).setParent(this.mSurfaceControl).setFlags(this.mSurfaceFlags).setHidden(false).setBLASTLayer().setCallsite("SurfaceView.updateSurface").build();
        } else {
            SurfaceControl.Transaction opaque = this.mTmpTransaction.setOpaque(surfaceControl, (this.mSurfaceFlags & 1024) != 0);
            SurfaceControl surfaceControl2 = this.mBlastSurfaceControl;
            if ((this.mSurfaceFlags & 128) != 0) {
                z = true;
            }
            opaque.setSecure(surfaceControl2, z).show(this.mBlastSurfaceControl).apply();
        }
        if (this.mBackgroundControl == null) {
            this.mBackgroundControl = createBackgroundControl(name);
        }
        BLASTBufferQueue bLASTBufferQueue = this.mBlastBufferQueue;
        if (bLASTBufferQueue != null) {
            bLASTBufferQueue.destroy();
        }
        int surfaceTransformHint = viewRoot.getSurfaceTransformHint();
        this.mTransformHint = surfaceTransformHint;
        this.mBlastSurfaceControl.setTransformHint(surfaceTransformHint);
        this.mBlastBufferQueue = new BLASTBufferQueue(name, this.mBlastSurfaceControl, this.mSurfaceWidth, this.mSurfaceHeight, this.mFormat);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDrawFinished() {
        runOnUiThread(new Runnable() { // from class: android.view.SurfaceView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                SurfaceView.this.performDrawFinished();
            }
        });
    }

    protected void applyChildSurfaceTransaction_renderWorker(SurfaceControl.Transaction t, Surface viewRootSurface, long nextViewRootFrameNumber) {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onSetSurfacePositionAndScaleRT(SurfaceControl.Transaction transaction, SurfaceControl surface, int positionLeft, int positionTop, float postScaleX, float postScaleY) {
        transaction.setPosition(surface, positionLeft, positionTop);
        transaction.setMatrix(surface, postScaleX, 0.0f, 0.0f, postScaleY);
    }

    public void requestUpdateSurfacePositionAndScale() {
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl == null) {
            return;
        }
        onSetSurfacePositionAndScaleRT(this.mTmpTransaction, surfaceControl, this.mScreenRect.left, this.mScreenRect.top, this.mScreenRect.width() / this.mSurfaceWidth, this.mScreenRect.height() / this.mSurfaceHeight);
        this.mTmpTransaction.apply();
    }

    public Rect getSurfaceRenderPosition() {
        return this.mRTLastReportedPosition;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyOrMergeTransaction(SurfaceControl.Transaction t, long frameNumber) {
        ViewRootImpl viewRoot = getViewRootImpl();
        boolean useBLAST = viewRoot != null && useBLASTSync(viewRoot);
        if (useBLAST) {
            viewRoot.lambda$applyTransactionOnDraw$9$ViewRootImpl(t, frameNumber);
        } else {
            t.apply();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SurfaceViewPositionUpdateListener implements RenderNode.PositionUpdateListener {
        boolean mPendingTransaction;
        private final SurfaceControl.Transaction mPositionChangedTransaction;
        int mRtSurfaceHeight;
        int mRtSurfaceWidth;

        SurfaceViewPositionUpdateListener(int surfaceWidth, int surfaceHeight, SurfaceControl.Transaction t) {
            this.mRtSurfaceWidth = -1;
            this.mRtSurfaceHeight = -1;
            SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
            this.mPositionChangedTransaction = transaction;
            this.mPendingTransaction = false;
            this.mRtSurfaceWidth = surfaceWidth;
            this.mRtSurfaceHeight = surfaceHeight;
            if (t != null) {
                transaction.merge(t);
                this.mPendingTransaction = true;
            }
        }

        @Override // android.graphics.RenderNode.PositionUpdateListener
        public void positionChanged(long frameNumber, int left, int top, int right, int bottom) {
            if (SurfaceView.this.mSurfaceControl == null) {
                return;
            }
            synchronized (SurfaceView.this.mSurfaceControlLock) {
                SurfaceView.this.mRtHandlingPositionUpdates = true;
            }
            if (SurfaceView.this.mRTLastReportedPosition.left != left || SurfaceView.this.mRTLastReportedPosition.top != top || SurfaceView.this.mRTLastReportedPosition.right != right || SurfaceView.this.mRTLastReportedPosition.bottom != bottom || SurfaceView.this.mRTLastReportedSurfaceSize.x != this.mRtSurfaceWidth || SurfaceView.this.mRTLastReportedSurfaceSize.y != this.mRtSurfaceHeight || this.mPendingTransaction) {
                try {
                    SurfaceView.this.mRTLastReportedPosition.set(left, top, right, bottom);
                    SurfaceView.this.mRTLastReportedSurfaceSize.set(this.mRtSurfaceWidth, this.mRtSurfaceHeight);
                    SurfaceView surfaceView = SurfaceView.this;
                    surfaceView.onSetSurfacePositionAndScaleRT(this.mPositionChangedTransaction, surfaceView.mSurfaceControl, SurfaceView.this.mRTLastReportedPosition.left, SurfaceView.this.mRTLastReportedPosition.top, SurfaceView.this.mRTLastReportedPosition.width() / this.mRtSurfaceWidth, SurfaceView.this.mRTLastReportedPosition.height() / this.mRtSurfaceHeight);
                    if (SurfaceView.this.mViewVisibility) {
                        this.mPositionChangedTransaction.show(SurfaceView.this.mSurfaceControl);
                    }
                    SurfaceView surfaceView2 = SurfaceView.this;
                    surfaceView2.applyChildSurfaceTransaction_renderWorker(this.mPositionChangedTransaction, surfaceView2.getViewRootImpl().mSurface, frameNumber);
                    SurfaceView.this.applyOrMergeTransaction(this.mPositionChangedTransaction, frameNumber);
                    this.mPendingTransaction = false;
                } catch (Exception ex) {
                    Log.e(SurfaceView.TAG, "Exception from repositionChild", ex);
                }
            }
        }

        @Override // android.graphics.RenderNode.PositionUpdateListener
        public void applyStretch(long frameNumber, float width, float height, float vecX, float vecY, float maxStretchX, float maxStretchY, float childRelativeLeft, float childRelativeTop, float childRelativeRight, float childRelativeBottom) {
            SurfaceView.this.mRtTransaction.setStretchEffect(SurfaceView.this.mSurfaceControl, width, height, vecX, vecY, maxStretchX, maxStretchY, childRelativeLeft, childRelativeTop, childRelativeRight, childRelativeBottom);
            SurfaceView surfaceView = SurfaceView.this;
            surfaceView.applyOrMergeTransaction(surfaceView.mRtTransaction, frameNumber);
        }

        @Override // android.graphics.RenderNode.PositionUpdateListener
        public void positionLost(long frameNumber) {
            SurfaceView.this.mRTLastReportedPosition.setEmpty();
            SurfaceView.this.mRTLastReportedSurfaceSize.set(-1, -1);
            if (this.mPendingTransaction) {
                Log.w(SurfaceView.TAG, System.identityHashCode(SurfaceView.this) + "Pending transaction cleared.");
                this.mPositionChangedTransaction.clear();
                this.mPendingTransaction = false;
            }
            if (SurfaceView.this.mSurfaceControl == null) {
                return;
            }
            synchronized (SurfaceView.this.mSurfaceControlLock) {
                SurfaceView.this.mRtTransaction.hide(SurfaceView.this.mSurfaceControl);
                if (SurfaceView.this.mRtReleaseSurfaces) {
                    SurfaceView.this.mRtReleaseSurfaces = false;
                    SurfaceView surfaceView = SurfaceView.this;
                    surfaceView.releaseSurfaces(surfaceView.mRtTransaction);
                }
                SurfaceView surfaceView2 = SurfaceView.this;
                surfaceView2.applyOrMergeTransaction(surfaceView2.mRtTransaction, frameNumber);
                SurfaceView.this.mRtHandlingPositionUpdates = false;
            }
        }
    }

    private SurfaceHolder.Callback[] getSurfaceCallbacks() {
        SurfaceHolder.Callback[] callbacks;
        synchronized (this.mCallbacks) {
            callbacks = new SurfaceHolder.Callback[this.mCallbacks.size()];
            this.mCallbacks.toArray(callbacks);
        }
        return callbacks;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void runOnUiThread(Runnable runnable) {
        Handler handler = getHandler();
        if (handler != null && handler.getLooper() != Looper.myLooper()) {
            handler.post(runnable);
        } else {
            runnable.run();
        }
    }

    public boolean isFixedSize() {
        return (this.mRequestedWidth == -1 && this.mRequestedHeight == -1) ? false : true;
    }

    private boolean isAboveParent() {
        return this.mSubLayer >= 0;
    }

    public void setResizeBackgroundColor(int bgColor) {
        if (this.mBackgroundControl == null) {
            return;
        }
        this.mBackgroundColor = bgColor;
        updateBackgroundColor(this.mTmpTransaction).apply();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: android.view.SurfaceView$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 implements SurfaceHolder {
        private static final String LOG_TAG = "SurfaceHolder";

        AnonymousClass1() {
        }

        @Override // android.view.SurfaceHolder
        public boolean isCreating() {
            return SurfaceView.this.mIsCreating;
        }

        @Override // android.view.SurfaceHolder
        public void addCallback(SurfaceHolder.Callback callback) {
            synchronized (SurfaceView.this.mCallbacks) {
                if (!SurfaceView.this.mCallbacks.contains(callback)) {
                    SurfaceView.this.mCallbacks.add(callback);
                }
            }
        }

        @Override // android.view.SurfaceHolder
        public void removeCallback(SurfaceHolder.Callback callback) {
            synchronized (SurfaceView.this.mCallbacks) {
                SurfaceView.this.mCallbacks.remove(callback);
            }
        }

        @Override // android.view.SurfaceHolder
        public void setFixedSize(int width, int height) {
            if (SurfaceView.this.mRequestedWidth != width || SurfaceView.this.mRequestedHeight != height) {
                SurfaceView.this.mRequestedWidth = width;
                SurfaceView.this.mRequestedHeight = height;
                SurfaceView.this.requestLayout();
            }
        }

        @Override // android.view.SurfaceHolder
        public void setSizeFromLayout() {
            if (SurfaceView.this.mRequestedWidth != -1 || SurfaceView.this.mRequestedHeight != -1) {
                SurfaceView surfaceView = SurfaceView.this;
                surfaceView.mRequestedHeight = -1;
                surfaceView.mRequestedWidth = -1;
                SurfaceView.this.requestLayout();
            }
        }

        @Override // android.view.SurfaceHolder
        public void setFormat(int format) {
            if (format == -1) {
                format = 4;
            }
            SurfaceView.this.mRequestedFormat = format;
            if (SurfaceView.this.mSurfaceControl != null) {
                SurfaceView.this.updateSurface();
            }
        }

        @Override // android.view.SurfaceHolder
        @Deprecated
        public void setType(int type) {
        }

        public /* synthetic */ void lambda$setKeepScreenOn$0$SurfaceView$1(boolean screenOn) {
            SurfaceView.this.setKeepScreenOn(screenOn);
        }

        @Override // android.view.SurfaceHolder
        public void setKeepScreenOn(final boolean screenOn) {
            SurfaceView.this.runOnUiThread(new Runnable() { // from class: android.view.SurfaceView$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SurfaceView.AnonymousClass1.this.lambda$setKeepScreenOn$0$SurfaceView$1(screenOn);
                }
            });
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockCanvas() {
            return internalLockCanvas(null, false);
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockCanvas(Rect inOutDirty) {
            return internalLockCanvas(inOutDirty, false);
        }

        @Override // android.view.SurfaceHolder
        public Canvas lockHardwareCanvas() {
            return internalLockCanvas(null, true);
        }

        private Canvas internalLockCanvas(Rect dirty, boolean hardware) {
            SurfaceView.this.mSurfaceLock.lock();
            Canvas c = null;
            if (!SurfaceView.this.mDrawingStopped && SurfaceView.this.mSurfaceControl != null) {
                try {
                    if (hardware) {
                        c = SurfaceView.this.mSurface.lockHardwareCanvas();
                    } else {
                        c = SurfaceView.this.mSurface.lockCanvas(dirty);
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Exception locking surface", e);
                }
            }
            if (c != null) {
                SurfaceView.this.mLastLockTime = SystemClock.uptimeMillis();
                return c;
            }
            long now = SystemClock.uptimeMillis();
            long nextTime = SurfaceView.this.mLastLockTime + 100;
            if (nextTime > now) {
                try {
                    Thread.sleep(nextTime - now);
                } catch (InterruptedException e2) {
                }
                now = SystemClock.uptimeMillis();
            }
            SurfaceView.this.mLastLockTime = now;
            SurfaceView.this.mSurfaceLock.unlock();
            return null;
        }

        @Override // android.view.SurfaceHolder
        public void unlockCanvasAndPost(Canvas canvas) {
            SurfaceView.this.mSurface.unlockCanvasAndPost(canvas);
            SurfaceView.this.mSurfaceLock.unlock();
        }

        @Override // android.view.SurfaceHolder
        public Surface getSurface() {
            return SurfaceView.this.mSurface;
        }

        @Override // android.view.SurfaceHolder
        public Rect getSurfaceFrame() {
            return SurfaceView.this.mSurfaceFrame;
        }
    }

    public SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }

    public IBinder getHostToken() {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot == null) {
            return null;
        }
        return viewRoot.getInputToken();
    }

    @Override // android.view.ViewRootImpl.SurfaceChangedCallback
    public void surfaceCreated(SurfaceControl.Transaction t) {
        setWindowStopped(false);
    }

    @Override // android.view.ViewRootImpl.SurfaceChangedCallback
    public void surfaceDestroyed() {
        setWindowStopped(true);
        this.mRemoteAccessibilityController.disassosciateHierarchy();
    }

    @Override // android.view.ViewRootImpl.SurfaceChangedCallback
    public void surfaceReplaced(SurfaceControl.Transaction t) {
        if (this.mSurfaceControl != null && this.mBackgroundControl != null) {
            updateRelativeZ(t);
        }
    }

    private void updateRelativeZ(SurfaceControl.Transaction t) {
        ViewRootImpl viewRoot = getViewRootImpl();
        if (viewRoot == null) {
            return;
        }
        SurfaceControl viewRootControl = viewRoot.getSurfaceControl();
        t.setRelativeLayer(this.mBackgroundControl, viewRootControl, Integer.MIN_VALUE);
        t.setRelativeLayer(this.mSurfaceControl, viewRootControl, this.mSubLayer);
    }

    public void setChildSurfacePackage(SurfaceControlViewHost.SurfacePackage p) {
        SurfaceControlViewHost.SurfacePackage surfacePackage = this.mSurfacePackage;
        SurfaceControl lastSc = surfacePackage != null ? surfacePackage.getSurfaceControl() : null;
        SurfaceControl surfaceControl = this.mSurfaceControl;
        if (surfaceControl != null && lastSc != null) {
            this.mTmpTransaction.reparent(lastSc, null).apply();
            this.mSurfacePackage.release();
        } else if (surfaceControl != null) {
            reparentSurfacePackage(this.mTmpTransaction, p);
            this.mTmpTransaction.apply();
        }
        this.mSurfacePackage = p;
    }

    private void reparentSurfacePackage(SurfaceControl.Transaction t, SurfaceControlViewHost.SurfacePackage p) {
        SurfaceControl parent;
        SurfaceControl sc = p.getSurfaceControl();
        if (sc == null || !sc.isValid()) {
            return;
        }
        initEmbeddedHierarchyForAccessibility(p);
        if (this.mUseBlastAdapter) {
            parent = this.mBlastSurfaceControl;
        } else {
            parent = this.mSurfaceControl;
        }
        t.reparent(sc, parent).show(sc);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (!this.mRemoteAccessibilityController.connected()) {
            return;
        }
        info.addChild(this.mRemoteAccessibilityController.getLeashToken());
    }

    @Override // android.view.View
    public int getImportantForAccessibility() {
        int mode = super.getImportantForAccessibility();
        RemoteAccessibilityController remoteAccessibilityController = this.mRemoteAccessibilityController;
        if ((remoteAccessibilityController != null && !remoteAccessibilityController.connected()) || mode != 0) {
            return mode;
        }
        return 1;
    }

    private void initEmbeddedHierarchyForAccessibility(SurfaceControlViewHost.SurfacePackage p) {
        IAccessibilityEmbeddedConnection connection = p.getAccessibilityEmbeddedConnection();
        if (this.mRemoteAccessibilityController.alreadyAssociated(connection)) {
            return;
        }
        this.mRemoteAccessibilityController.assosciateHierarchy(connection, getViewRootImpl().mLeashToken, getAccessibilityViewId());
        updateEmbeddedAccessibilityMatrix();
    }

    private void notifySurfaceDestroyed() {
        if (this.mSurface.isValid()) {
            SurfaceHolder.Callback[] callbacks = getSurfaceCallbacks();
            for (SurfaceHolder.Callback c : callbacks) {
                c.surfaceDestroyed(this.mSurfaceHolder);
            }
            if (this.mSurface.isValid()) {
                this.mSurface.forceScopedDisconnect();
            }
        }
    }

    void updateEmbeddedAccessibilityMatrix() {
        if (!this.mRemoteAccessibilityController.connected()) {
            return;
        }
        getBoundsOnScreen(this.mTmpRect);
        this.mTmpMatrix.reset();
        this.mTmpMatrix.setTranslate(this.mTmpRect.left, this.mTmpRect.top);
        this.mTmpMatrix.postScale(this.mScreenRect.width() / this.mSurfaceWidth, this.mScreenRect.height() / this.mSurfaceHeight);
        this.mRemoteAccessibilityController.setScreenMatrix(this.mTmpMatrix);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ViewRootImpl viewRoot = getViewRootImpl();
        if (this.mSurfacePackage == null || viewRoot == null) {
            return;
        }
        try {
            viewRoot.mWindowSession.grantEmbeddedWindowFocus(viewRoot.mWindow, this.mSurfacePackage.getInputToken(), gainFocus);
        } catch (Exception e) {
            Log.e(TAG, System.identityHashCode(this) + "Exception requesting focus on embedded window", e);
        }
    }

    private boolean useBLASTSync(ViewRootImpl viewRoot) {
        return viewRoot.useBLAST() && this.mUseBlastAdapter;
    }
}
