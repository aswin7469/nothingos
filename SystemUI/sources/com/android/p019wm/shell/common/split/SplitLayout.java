package com.android.p019wm.shell.common.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.Display;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.RoundedCorner;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.internal.policy.DockedDividerUtils;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.ShellTaskOrganizer;
import com.android.p019wm.shell.animation.Interpolators;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import com.android.p019wm.shell.common.InteractionJankMonitorUtils;
import com.android.p019wm.shell.common.split.SplitWindowManager;
import java.p026io.PrintWriter;

/* renamed from: com.android.wm.shell.common.split.SplitLayout */
public final class SplitLayout implements DisplayInsetsController.OnInsetsChangedListener {
    public static final int PARALLAX_ALIGN_CENTER = 2;
    public static final int PARALLAX_DISMISSING = 1;
    public static final int PARALLAX_NONE = 0;
    /* access modifiers changed from: private */
    public final Rect mBounds1;
    /* access modifiers changed from: private */
    public final Rect mBounds2;
    private Context mContext;
    /* access modifiers changed from: private */
    public final boolean mDimNonImeSide;
    private final DisplayImeController mDisplayImeController;
    private int mDividePosition;
    /* access modifiers changed from: private */
    public final Rect mDividerBounds;
    private final int mDividerInsets;
    private final int mDividerSize;
    DividerSnapAlgorithm mDividerSnapAlgorithm;
    private final int mDividerWindowWidth;
    private boolean mFreezeDividerWindow;
    private final ImePositionProcessor mImePositionProcessor;
    /* access modifiers changed from: private */
    public boolean mInitialized;
    private final InsetsState mInsetsState;
    private int mOrientation;
    /* access modifiers changed from: private */
    public final Rect mRootBounds;
    private int mRotation;
    /* access modifiers changed from: private */
    public final SplitLayoutHandler mSplitLayoutHandler;
    /* access modifiers changed from: private */
    public final SplitWindowManager mSplitWindowManager;
    private final ResizingEffectPolicy mSurfaceEffectPolicy;
    /* access modifiers changed from: private */
    public final ShellTaskOrganizer mTaskOrganizer;
    /* access modifiers changed from: private */
    public final Rect mTempRect = new Rect();
    /* access modifiers changed from: private */
    public final Rect mWinBounds1;
    /* access modifiers changed from: private */
    public final Rect mWinBounds2;
    private WindowContainerToken mWinToken1;
    private WindowContainerToken mWinToken2;

    /* renamed from: com.android.wm.shell.common.split.SplitLayout$SplitLayoutHandler */
    public interface SplitLayoutHandler {
        int getSplitItemPosition(WindowContainerToken windowContainerToken);

        void onDoubleTappedDivider() {
        }

        void onLayoutPositionChanging(SplitLayout splitLayout);

        void onLayoutSizeChanged(SplitLayout splitLayout);

        void onLayoutSizeChanging(SplitLayout splitLayout);

        void onSnappedToDismiss(boolean z);

        void setLayoutOffsetTarget(int i, int i2, SplitLayout splitLayout);
    }

    public static int reversePosition(int i) {
        if (i != 0) {
            return i != 1 ? -1 : 0;
        }
        return 1;
    }

    public SplitLayout(String str, Context context, Configuration configuration, SplitLayoutHandler splitLayoutHandler, SplitWindowManager.ParentContainerCallbacks parentContainerCallbacks, DisplayImeController displayImeController, ShellTaskOrganizer shellTaskOrganizer, int i) {
        Rect rect = new Rect();
        this.mRootBounds = rect;
        this.mDividerBounds = new Rect();
        this.mBounds1 = new Rect();
        this.mBounds2 = new Rect();
        this.mWinBounds1 = new Rect();
        this.mWinBounds2 = new Rect();
        this.mInsetsState = new InsetsState();
        this.mInitialized = false;
        this.mFreezeDividerWindow = false;
        this.mContext = context.createConfigurationContext(configuration);
        this.mOrientation = configuration.orientation;
        this.mRotation = configuration.windowConfiguration.getRotation();
        this.mSplitLayoutHandler = splitLayoutHandler;
        this.mDisplayImeController = displayImeController;
        this.mSplitWindowManager = new SplitWindowManager(str, this.mContext, configuration, parentContainerCallbacks);
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mImePositionProcessor = new ImePositionProcessor(this.mContext.getDisplayId());
        this.mSurfaceEffectPolicy = new ResizingEffectPolicy(i);
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(C3353R.dimen.split_divider_bar_width);
        this.mDividerSize = dimensionPixelSize;
        int dividerInsets = getDividerInsets(resources, context.getDisplay());
        this.mDividerInsets = dividerInsets;
        this.mDividerWindowWidth = dimensionPixelSize + (dividerInsets * 2);
        rect.set(configuration.windowConfiguration.getBounds());
        this.mDividerSnapAlgorithm = getSnapAlgorithm(this.mContext, rect, (Rect) null);
        resetDividerPosition();
        this.mDimNonImeSide = resources.getBoolean(C3353R.bool.config_dimNonImeAttachedSide);
    }

    private int getDividerInsets(Resources resources, Display display) {
        int dimensionPixelSize = resources.getDimensionPixelSize(17105200);
        int i = 0;
        RoundedCorner roundedCorner = display.getRoundedCorner(0);
        if (roundedCorner != null) {
            i = Math.max(0, roundedCorner.getRadius());
        }
        RoundedCorner roundedCorner2 = display.getRoundedCorner(1);
        if (roundedCorner2 != null) {
            i = Math.max(i, roundedCorner2.getRadius());
        }
        RoundedCorner roundedCorner3 = display.getRoundedCorner(2);
        if (roundedCorner3 != null) {
            i = Math.max(i, roundedCorner3.getRadius());
        }
        RoundedCorner roundedCorner4 = display.getRoundedCorner(3);
        if (roundedCorner4 != null) {
            i = Math.max(i, roundedCorner4.getRadius());
        }
        return Math.max(dimensionPixelSize, i);
    }

    public Rect getBounds1() {
        return new Rect(this.mBounds1);
    }

    public Rect getRefBounds1() {
        Rect bounds1 = getBounds1();
        bounds1.offset(-this.mRootBounds.left, -this.mRootBounds.top);
        return bounds1;
    }

    public Rect getBounds2() {
        return new Rect(this.mBounds2);
    }

    public Rect getRefBounds2() {
        Rect bounds2 = getBounds2();
        bounds2.offset(-this.mRootBounds.left, -this.mRootBounds.top);
        return bounds2;
    }

    public Rect getDividerBounds() {
        return new Rect(this.mDividerBounds);
    }

    public Rect getRefDividerBounds() {
        Rect dividerBounds = getDividerBounds();
        dividerBounds.offset(-this.mRootBounds.left, -this.mRootBounds.top);
        return dividerBounds;
    }

    public SurfaceControl getDividerLeash() {
        SplitWindowManager splitWindowManager = this.mSplitWindowManager;
        if (splitWindowManager == null) {
            return null;
        }
        return splitWindowManager.getSurfaceControl();
    }

    /* access modifiers changed from: package-private */
    public int getDividePosition() {
        return this.mDividePosition;
    }

    public float getDividerPositionAsFraction() {
        int i;
        float f;
        if (isLandscape()) {
            f = ((float) (this.mBounds1.right + this.mBounds2.left)) / 2.0f;
            i = this.mBounds2.right;
        } else {
            f = ((float) (this.mBounds1.bottom + this.mBounds2.top)) / 2.0f;
            i = this.mBounds2.bottom;
        }
        return Math.min(1.0f, Math.max(0.0f, f / ((float) i)));
    }

    public boolean updateConfiguration(Configuration configuration) {
        int i = configuration.orientation;
        if (this.mOrientation != i) {
            this.mContext = this.mContext.createConfigurationContext(configuration);
            this.mSplitWindowManager.setConfiguration(configuration);
            this.mOrientation = i;
        }
        int rotation = configuration.windowConfiguration.getRotation();
        Rect bounds = configuration.windowConfiguration.getBounds();
        if (this.mRotation == rotation && this.mRootBounds.equals(bounds)) {
            return false;
        }
        this.mTempRect.set(this.mRootBounds);
        this.mRootBounds.set(bounds);
        this.mRotation = rotation;
        this.mDividerSnapAlgorithm = getSnapAlgorithm(this.mContext, this.mRootBounds, (Rect) null);
        initDividerPosition(this.mTempRect);
        return true;
    }

    public void rotateTo(int i, Rect rect) {
        boolean z = (((i - this.mRotation) + 4) % 4) % 2 != 0;
        this.mRotation = i;
        Rect rect2 = new Rect(this.mRootBounds);
        if (z) {
            rect2.set(this.mRootBounds.top, this.mRootBounds.left, this.mRootBounds.bottom, this.mRootBounds.right);
        }
        this.mTempRect.set(this.mRootBounds);
        this.mRootBounds.set(rect2);
        this.mDividerSnapAlgorithm = getSnapAlgorithm(this.mContext, this.mRootBounds, rect);
        initDividerPosition(this.mTempRect);
    }

    private void initDividerPosition(Rect rect) {
        int i = this.mDividerSnapAlgorithm.calculateNonDismissingSnapTarget((int) (((float) (isLandscape() ? this.mRootBounds.width() : this.mRootBounds.height())) * (((float) this.mDividePosition) / ((float) (isLandscape(rect) ? rect.width() : rect.height()))))).position;
        this.mDividePosition = i;
        updateBounds(i);
    }

    private void updateBounds(int i) {
        int i2;
        this.mDividerBounds.set(this.mRootBounds);
        this.mBounds1.set(this.mRootBounds);
        this.mBounds2.set(this.mRootBounds);
        boolean isLandscape = isLandscape(this.mRootBounds);
        if (isLandscape) {
            i2 = i + this.mRootBounds.left;
            this.mDividerBounds.left = i2 - this.mDividerInsets;
            Rect rect = this.mDividerBounds;
            rect.right = rect.left + this.mDividerWindowWidth;
            this.mBounds1.right = i2;
            this.mBounds2.left = this.mBounds1.right + this.mDividerSize;
        } else {
            i2 = i + this.mRootBounds.top;
            this.mDividerBounds.top = i2 - this.mDividerInsets;
            Rect rect2 = this.mDividerBounds;
            rect2.bottom = rect2.top + this.mDividerWindowWidth;
            this.mBounds1.bottom = i2;
            this.mBounds2.top = this.mBounds1.bottom + this.mDividerSize;
        }
        DockedDividerUtils.sanitizeStackBounds(this.mBounds1, true);
        DockedDividerUtils.sanitizeStackBounds(this.mBounds2, false);
        this.mSurfaceEffectPolicy.applyDividerPosition(i2, isLandscape);
    }

    public void init() {
        if (!this.mInitialized) {
            this.mInitialized = true;
            this.mSplitWindowManager.init(this, this.mInsetsState);
            this.mDisplayImeController.addPositionProcessor(this.mImePositionProcessor);
        }
    }

    public void release(SurfaceControl.Transaction transaction) {
        if (this.mInitialized) {
            this.mInitialized = false;
            this.mSplitWindowManager.release(transaction);
            this.mDisplayImeController.removePositionProcessor(this.mImePositionProcessor);
            this.mImePositionProcessor.reset();
        }
    }

    public void release() {
        release((SurfaceControl.Transaction) null);
    }

    public void update(SurfaceControl.Transaction transaction) {
        if (this.mInitialized) {
            this.mSplitWindowManager.release(transaction);
            this.mImePositionProcessor.reset();
            this.mSplitWindowManager.init(this, this.mInsetsState);
        }
    }

    public void insetsChanged(InsetsState insetsState) {
        this.mInsetsState.set(insetsState);
        if (this.mInitialized && !this.mFreezeDividerWindow) {
            this.mSplitWindowManager.onInsetsChanged(insetsState);
        }
    }

    public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        if (!this.mInsetsState.equals(insetsState)) {
            insetsChanged(insetsState);
        }
    }

    public void setFreezeDividerWindow(boolean z) {
        this.mFreezeDividerWindow = z;
    }

    /* access modifiers changed from: package-private */
    public void updateDivideBounds(int i) {
        updateBounds(i);
        this.mSplitLayoutHandler.onLayoutSizeChanging(this);
    }

    /* access modifiers changed from: package-private */
    public void setDividePosition(int i, boolean z) {
        this.mDividePosition = i;
        updateBounds(i);
        if (z) {
            this.mSplitLayoutHandler.onLayoutSizeChanged(this);
        }
    }

    public void setDivideRatio(float f) {
        int i;
        int i2;
        if (isLandscape()) {
            i2 = this.mRootBounds.left;
            i = this.mRootBounds.width();
        } else {
            i2 = this.mRootBounds.top;
            i = this.mRootBounds.height();
        }
        setDividePosition(this.mDividerSnapAlgorithm.calculateNonDismissingSnapTarget(i2 + ((int) (((float) i) * f))).position, false);
    }

    public void resetDividerPosition() {
        int i = this.mDividerSnapAlgorithm.getMiddleTarget().position;
        this.mDividePosition = i;
        updateBounds(i);
        this.mWinToken1 = null;
        this.mWinToken2 = null;
        this.mWinBounds1.setEmpty();
        this.mWinBounds2.setEmpty();
    }

    public void snapToTarget(int i, DividerSnapAlgorithm.SnapTarget snapTarget) {
        int i2 = snapTarget.flag;
        if (i2 == 1) {
            flingDividePosition(i, snapTarget.position, new SplitLayout$$ExternalSyntheticLambda0(this));
        } else if (i2 != 2) {
            flingDividePosition(i, snapTarget.position, new SplitLayout$$ExternalSyntheticLambda2(this, snapTarget));
        } else {
            flingDividePosition(i, snapTarget.position, new SplitLayout$$ExternalSyntheticLambda1(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$snapToTarget$0$com-android-wm-shell-common-split-SplitLayout */
    public /* synthetic */ void mo49331xbdb1f654() {
        this.mSplitLayoutHandler.onSnappedToDismiss(false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$snapToTarget$1$com-android-wm-shell-common-split-SplitLayout */
    public /* synthetic */ void mo49332x332c1c95() {
        this.mSplitLayoutHandler.onSnappedToDismiss(true);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$snapToTarget$2$com-android-wm-shell-common-split-SplitLayout */
    public /* synthetic */ void mo49333xa8a642d6(DividerSnapAlgorithm.SnapTarget snapTarget) {
        setDividePosition(snapTarget.position, true);
    }

    /* access modifiers changed from: package-private */
    public void onDoubleTappedDivider() {
        this.mSplitLayoutHandler.onDoubleTappedDivider();
    }

    public DividerSnapAlgorithm.SnapTarget findSnapTarget(int i, float f, boolean z) {
        return this.mDividerSnapAlgorithm.calculateSnapTarget(i, f, z);
    }

    private DividerSnapAlgorithm getSnapAlgorithm(Context context, Rect rect, Rect rect2) {
        boolean isLandscape = isLandscape(rect);
        Resources resources = context.getResources();
        int width = rect.width();
        int height = rect.height();
        int i = this.mDividerSize;
        boolean z = !isLandscape;
        if (rect2 == null) {
            rect2 = getDisplayInsets(context);
        }
        return new DividerSnapAlgorithm(resources, width, height, i, z, rect2, isLandscape ? 1 : 2);
    }

    /* access modifiers changed from: package-private */
    public void flingDividePosition(int i, final int i2, final Runnable runnable) {
        if (i == i2) {
            this.mSplitLayoutHandler.onLayoutSizeChanged(this);
            return;
        }
        InteractionJankMonitorUtils.beginTracing(52, this.mSplitWindowManager.getDividerView(), "Divider fling");
        ValueAnimator duration = ValueAnimator.ofInt(new int[]{i, i2}).setDuration(250);
        duration.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        duration.addUpdateListener(new SplitLayout$$ExternalSyntheticLambda3(this));
        duration.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                Runnable runnable = runnable;
                if (runnable != null) {
                    runnable.run();
                }
                InteractionJankMonitorUtils.endTracing(52);
            }

            public void onAnimationCancel(Animator animator) {
                SplitLayout.this.setDividePosition(i2, true);
            }
        });
        duration.start();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$flingDividePosition$3$com-android-wm-shell-common-split-SplitLayout */
    public /* synthetic */ void mo49330xe5a2de65(ValueAnimator valueAnimator) {
        updateDivideBounds(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    private static Rect getDisplayInsets(Context context) {
        return ((WindowManager) context.getSystemService(WindowManager.class)).getMaximumWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout()).toRect();
    }

    /* access modifiers changed from: private */
    public static boolean isLandscape(Rect rect) {
        return rect.width() > rect.height();
    }

    public boolean isLandscape() {
        return isLandscape(this.mRootBounds);
    }

    public void applySurfaceChanges(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, SurfaceControl surfaceControl2, SurfaceControl surfaceControl3, SurfaceControl surfaceControl4, boolean z) {
        SurfaceControl dividerLeash = getDividerLeash();
        if (dividerLeash != null) {
            this.mTempRect.set(getRefDividerBounds());
            transaction.setPosition(dividerLeash, (float) this.mTempRect.left, (float) this.mTempRect.top);
            transaction.setLayer(dividerLeash, Integer.MAX_VALUE);
        }
        this.mTempRect.set(getRefBounds1());
        transaction.setPosition(surfaceControl, (float) this.mTempRect.left, (float) this.mTempRect.top).setWindowCrop(surfaceControl, this.mTempRect.width(), this.mTempRect.height());
        this.mTempRect.set(getRefBounds2());
        transaction.setPosition(surfaceControl2, (float) this.mTempRect.left, (float) this.mTempRect.top).setWindowCrop(surfaceControl2, this.mTempRect.width(), this.mTempRect.height());
        if (!this.mImePositionProcessor.adjustSurfaceLayoutForIme(transaction, dividerLeash, surfaceControl, surfaceControl2, surfaceControl3, surfaceControl4)) {
            this.mSurfaceEffectPolicy.adjustDimSurface(transaction, surfaceControl3, surfaceControl4);
            if (z) {
                this.mSurfaceEffectPolicy.adjustRootSurface(transaction, surfaceControl, surfaceControl2);
            }
        }
    }

    public void applyTaskChanges(WindowContainerTransaction windowContainerTransaction, ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        if (!this.mBounds1.equals(this.mWinBounds1) || !runningTaskInfo.token.equals(this.mWinToken1)) {
            windowContainerTransaction.setBounds(runningTaskInfo.token, this.mBounds1);
            windowContainerTransaction.setSmallestScreenWidthDp(runningTaskInfo.token, getSmallestWidthDp(this.mBounds1));
            this.mWinBounds1.set(this.mBounds1);
            this.mWinToken1 = runningTaskInfo.token;
        }
        if (!this.mBounds2.equals(this.mWinBounds2) || !runningTaskInfo2.token.equals(this.mWinToken2)) {
            windowContainerTransaction.setBounds(runningTaskInfo2.token, this.mBounds2);
            windowContainerTransaction.setSmallestScreenWidthDp(runningTaskInfo2.token, getSmallestWidthDp(this.mBounds2));
            this.mWinBounds2.set(this.mBounds2);
            this.mWinToken2 = runningTaskInfo2.token;
        }
    }

    private int getSmallestWidthDp(Rect rect) {
        this.mTempRect.set(rect);
        this.mTempRect.inset(getDisplayInsets(this.mContext));
        return (int) (((float) Math.min(this.mTempRect.width(), this.mTempRect.height())) / this.mContext.getResources().getDisplayMetrics().density);
    }

    public void applyLayoutOffsetTarget(WindowContainerTransaction windowContainerTransaction, int i, int i2, ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        if (i == 0 && i2 == 0) {
            windowContainerTransaction.setBounds(runningTaskInfo.token, this.mBounds1);
            windowContainerTransaction.setAppBounds(runningTaskInfo.token, (Rect) null);
            windowContainerTransaction.setScreenSizeDp(runningTaskInfo.token, 0, 0);
            windowContainerTransaction.setBounds(runningTaskInfo2.token, this.mBounds2);
            windowContainerTransaction.setAppBounds(runningTaskInfo2.token, (Rect) null);
            windowContainerTransaction.setScreenSizeDp(runningTaskInfo2.token, 0, 0);
            return;
        }
        this.mTempRect.set(runningTaskInfo.configuration.windowConfiguration.getBounds());
        this.mTempRect.offset(i, i2);
        windowContainerTransaction.setBounds(runningTaskInfo.token, this.mTempRect);
        this.mTempRect.set(runningTaskInfo.configuration.windowConfiguration.getAppBounds());
        this.mTempRect.offset(i, i2);
        windowContainerTransaction.setAppBounds(runningTaskInfo.token, this.mTempRect);
        windowContainerTransaction.setScreenSizeDp(runningTaskInfo.token, runningTaskInfo.configuration.screenWidthDp, runningTaskInfo.configuration.screenHeightDp);
        this.mTempRect.set(runningTaskInfo2.configuration.windowConfiguration.getBounds());
        this.mTempRect.offset(i, i2);
        windowContainerTransaction.setBounds(runningTaskInfo2.token, this.mTempRect);
        this.mTempRect.set(runningTaskInfo2.configuration.windowConfiguration.getAppBounds());
        this.mTempRect.offset(i, i2);
        windowContainerTransaction.setAppBounds(runningTaskInfo2.token, this.mTempRect);
        windowContainerTransaction.setScreenSizeDp(runningTaskInfo2.token, runningTaskInfo2.configuration.screenWidthDp, runningTaskInfo2.configuration.screenHeightDp);
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + "bounds1=" + this.mBounds1.toShortString());
        printWriter.println(str + "dividerBounds=" + this.mDividerBounds.toShortString());
        printWriter.println(str + "bounds2=" + this.mBounds2.toShortString());
    }

    /* renamed from: com.android.wm.shell.common.split.SplitLayout$ResizingEffectPolicy */
    private class ResizingEffectPolicy {
        final Rect mContentBounds = new Rect();
        float mDismissingDimValue = 0.0f;
        int mDismissingSide = -1;
        final Point mParallaxOffset = new Point();
        private final int mParallaxType;
        int mShrinkSide = -1;
        final Rect mSurfaceBounds = new Rect();

        ResizingEffectPolicy(int i) {
            this.mParallaxType = i;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x007c  */
        /* JADX WARNING: Removed duplicated region for block: B:25:0x009a  */
        /* JADX WARNING: Removed duplicated region for block: B:30:0x00ba  */
        /* JADX WARNING: Removed duplicated region for block: B:38:0x00f3  */
        /* JADX WARNING: Removed duplicated region for block: B:42:? A[RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void applyDividerPosition(int r10, boolean r11) {
            /*
                r9 = this;
                r0 = -1
                r9.mDismissingSide = r0
                android.graphics.Point r1 = r9.mParallaxOffset
                r2 = 0
                r1.set(r2, r2)
                r1 = 0
                r9.mDismissingDimValue = r1
                com.android.wm.shell.common.split.SplitLayout r3 = com.android.p019wm.shell.common.split.SplitLayout.this
                com.android.internal.policy.DividerSnapAlgorithm r3 = r3.mDividerSnapAlgorithm
                com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r3 = r3.getFirstSplitTarget()
                int r3 = r3.position
                r4 = 3
                r5 = 4
                r6 = 1
                r7 = 2
                if (r10 >= r3) goto L_0x0039
                if (r11 == 0) goto L_0x0020
                r3 = r6
                goto L_0x0021
            L_0x0020:
                r3 = r7
            L_0x0021:
                r9.mDismissingSide = r3
                com.android.wm.shell.common.split.SplitLayout r3 = com.android.p019wm.shell.common.split.SplitLayout.this
                com.android.internal.policy.DividerSnapAlgorithm r3 = r3.mDividerSnapAlgorithm
                com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r3 = r3.getDismissStartTarget()
                int r3 = r3.position
                com.android.wm.shell.common.split.SplitLayout r8 = com.android.p019wm.shell.common.split.SplitLayout.this
                com.android.internal.policy.DividerSnapAlgorithm r8 = r8.mDividerSnapAlgorithm
                com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r8 = r8.getFirstSplitTarget()
                int r8 = r8.position
            L_0x0037:
                int r3 = r3 - r8
                goto L_0x0062
            L_0x0039:
                com.android.wm.shell.common.split.SplitLayout r3 = com.android.p019wm.shell.common.split.SplitLayout.this
                com.android.internal.policy.DividerSnapAlgorithm r3 = r3.mDividerSnapAlgorithm
                com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r3 = r3.getLastSplitTarget()
                int r3 = r3.position
                if (r10 <= r3) goto L_0x0061
                if (r11 == 0) goto L_0x0049
                r3 = r4
                goto L_0x004a
            L_0x0049:
                r3 = r5
            L_0x004a:
                r9.mDismissingSide = r3
                com.android.wm.shell.common.split.SplitLayout r3 = com.android.p019wm.shell.common.split.SplitLayout.this
                com.android.internal.policy.DividerSnapAlgorithm r3 = r3.mDividerSnapAlgorithm
                com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r3 = r3.getLastSplitTarget()
                int r3 = r3.position
                com.android.wm.shell.common.split.SplitLayout r8 = com.android.p019wm.shell.common.split.SplitLayout.this
                com.android.internal.policy.DividerSnapAlgorithm r8 = r8.mDividerSnapAlgorithm
                com.android.internal.policy.DividerSnapAlgorithm$SnapTarget r8 = r8.getDismissEndTarget()
                int r8 = r8.position
                goto L_0x0037
            L_0x0061:
                r3 = r2
            L_0x0062:
                if (r11 == 0) goto L_0x006f
                com.android.wm.shell.common.split.SplitLayout r8 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r8 = r8.mWinBounds1
                int r8 = r8.right
                if (r10 >= r8) goto L_0x007a
                goto L_0x0079
            L_0x006f:
                com.android.wm.shell.common.split.SplitLayout r8 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r8 = r8.mWinBounds1
                int r8 = r8.bottom
                if (r10 >= r8) goto L_0x007a
            L_0x0079:
                r2 = r6
            L_0x007a:
                if (r2 == 0) goto L_0x009a
                if (r11 == 0) goto L_0x0080
                r2 = r6
                goto L_0x0081
            L_0x0080:
                r2 = r7
            L_0x0081:
                r9.mShrinkSide = r2
                android.graphics.Rect r2 = r9.mContentBounds
                com.android.wm.shell.common.split.SplitLayout r4 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r4 = r4.mWinBounds1
                r2.set(r4)
                android.graphics.Rect r2 = r9.mSurfaceBounds
                com.android.wm.shell.common.split.SplitLayout r4 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r4 = r4.mBounds1
                r2.set(r4)
                goto L_0x00b6
            L_0x009a:
                if (r11 == 0) goto L_0x009d
                goto L_0x009e
            L_0x009d:
                r4 = r5
            L_0x009e:
                r9.mShrinkSide = r4
                android.graphics.Rect r2 = r9.mContentBounds
                com.android.wm.shell.common.split.SplitLayout r4 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r4 = r4.mWinBounds2
                r2.set(r4)
                android.graphics.Rect r2 = r9.mSurfaceBounds
                com.android.wm.shell.common.split.SplitLayout r4 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r4 = r4.mBounds2
                r2.set(r4)
            L_0x00b6:
                int r2 = r9.mDismissingSide
                if (r2 == r0) goto L_0x00ef
                com.android.wm.shell.common.split.SplitLayout r0 = com.android.p019wm.shell.common.split.SplitLayout.this
                com.android.internal.policy.DividerSnapAlgorithm r0 = r0.mDividerSnapAlgorithm
                float r10 = r0.calculateDismissingFraction(r10)
                r0 = 1065353216(0x3f800000, float:1.0)
                float r10 = java.lang.Math.min((float) r10, (float) r0)
                float r10 = java.lang.Math.max((float) r1, (float) r10)
                android.view.animation.PathInterpolator r0 = com.android.p019wm.shell.animation.Interpolators.DIM_INTERPOLATOR
                float r0 = r0.getInterpolation(r10)
                r9.mDismissingDimValue = r0
                int r0 = r9.mParallaxType
                if (r0 != r6) goto L_0x00ef
                int r0 = r9.mDismissingSide
                float r10 = r9.calculateParallaxDismissingFraction(r10, r0)
                if (r11 == 0) goto L_0x00e8
                android.graphics.Point r0 = r9.mParallaxOffset
                float r1 = (float) r3
                float r10 = r10 * r1
                int r10 = (int) r10
                r0.x = r10
                goto L_0x00ef
            L_0x00e8:
                android.graphics.Point r0 = r9.mParallaxOffset
                float r1 = (float) r3
                float r10 = r10 * r1
                int r10 = (int) r10
                r0.y = r10
            L_0x00ef:
                int r10 = r9.mParallaxType
                if (r10 != r7) goto L_0x011a
                if (r11 == 0) goto L_0x0108
                android.graphics.Point r10 = r9.mParallaxOffset
                android.graphics.Rect r11 = r9.mSurfaceBounds
                int r11 = r11.width()
                android.graphics.Rect r9 = r9.mContentBounds
                int r9 = r9.width()
                int r11 = r11 - r9
                int r11 = r11 / r7
                r10.x = r11
                goto L_0x011a
            L_0x0108:
                android.graphics.Point r10 = r9.mParallaxOffset
                android.graphics.Rect r11 = r9.mSurfaceBounds
                int r11 = r11.height()
                android.graphics.Rect r9 = r9.mContentBounds
                int r9 = r9.height()
                int r11 = r11 - r9
                int r11 = r11 / r7
                r10.y = r11
            L_0x011a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.common.split.SplitLayout.ResizingEffectPolicy.applyDividerPosition(int, boolean):void");
        }

        private float calculateParallaxDismissingFraction(float f, int i) {
            float interpolation = Interpolators.SLOWDOWN_INTERPOLATOR.getInterpolation(f) / 3.5f;
            return i == 2 ? interpolation / 2.0f : interpolation;
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:21:0x0067 A[ADDED_TO_REGION] */
        /* JADX WARNING: Removed duplicated region for block: B:24:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void adjustRootSurface(android.view.SurfaceControl.Transaction r7, android.view.SurfaceControl r8, android.view.SurfaceControl r9) {
            /*
                r6 = this;
                int r0 = r6.mParallaxType
                r1 = 4
                r2 = 3
                r3 = 2
                r4 = 0
                r5 = 1
                if (r0 != r5) goto L_0x0034
                int r0 = r6.mDismissingSide
                if (r0 == r5) goto L_0x0024
                if (r0 == r3) goto L_0x0024
                if (r0 == r2) goto L_0x0014
                if (r0 == r1) goto L_0x0014
                goto L_0x0062
            L_0x0014:
                com.android.wm.shell.common.split.SplitLayout r8 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r8 = r8.mTempRect
                com.android.wm.shell.common.split.SplitLayout r0 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r0 = r0.mBounds2
                r8.set(r0)
                goto L_0x0050
            L_0x0024:
                com.android.wm.shell.common.split.SplitLayout r9 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r9 = r9.mTempRect
                com.android.wm.shell.common.split.SplitLayout r0 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r0 = r0.mBounds1
                r9.set(r0)
                goto L_0x0063
            L_0x0034:
                if (r0 != r3) goto L_0x0062
                int r0 = r6.mShrinkSide
                if (r0 == r5) goto L_0x0052
                if (r0 == r3) goto L_0x0052
                if (r0 == r2) goto L_0x0041
                if (r0 == r1) goto L_0x0041
                goto L_0x0062
            L_0x0041:
                com.android.wm.shell.common.split.SplitLayout r8 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r8 = r8.mTempRect
                com.android.wm.shell.common.split.SplitLayout r0 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r0 = r0.mBounds2
                r8.set(r0)
            L_0x0050:
                r8 = r9
                goto L_0x0063
            L_0x0052:
                com.android.wm.shell.common.split.SplitLayout r9 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r9 = r9.mTempRect
                com.android.wm.shell.common.split.SplitLayout r0 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r0 = r0.mBounds1
                r9.set(r0)
                goto L_0x0063
            L_0x0062:
                r8 = r4
            L_0x0063:
                int r9 = r6.mParallaxType
                if (r9 == 0) goto L_0x00a4
                if (r8 == 0) goto L_0x00a4
                com.android.wm.shell.common.split.SplitLayout r9 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r9 = r9.mTempRect
                int r9 = r9.left
                android.graphics.Point r0 = r6.mParallaxOffset
                int r0 = r0.x
                int r9 = r9 + r0
                float r9 = (float) r9
                com.android.wm.shell.common.split.SplitLayout r0 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r0 = r0.mTempRect
                int r0 = r0.top
                android.graphics.Point r1 = r6.mParallaxOffset
                int r1 = r1.y
                int r0 = r0 + r1
                float r0 = (float) r0
                r7.setPosition(r8, r9, r0)
                com.android.wm.shell.common.split.SplitLayout r9 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r9 = r9.mTempRect
                android.graphics.Point r0 = r6.mParallaxOffset
                int r0 = r0.x
                int r0 = -r0
                android.graphics.Point r1 = r6.mParallaxOffset
                int r1 = r1.y
                int r1 = -r1
                r9.offsetTo(r0, r1)
                com.android.wm.shell.common.split.SplitLayout r6 = com.android.p019wm.shell.common.split.SplitLayout.this
                android.graphics.Rect r6 = r6.mTempRect
                r7.setWindowCrop(r8, r6)
            L_0x00a4:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.common.split.SplitLayout.ResizingEffectPolicy.adjustRootSurface(android.view.SurfaceControl$Transaction, android.view.SurfaceControl, android.view.SurfaceControl):void");
        }

        /* access modifiers changed from: package-private */
        public void adjustDimSurface(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, SurfaceControl surfaceControl2) {
            int i = this.mDismissingSide;
            boolean z = true;
            if (!(i == 1 || i == 2)) {
                if (i == 3 || i == 4) {
                    surfaceControl = surfaceControl2;
                } else {
                    transaction.setAlpha(surfaceControl, 0.0f).hide(surfaceControl);
                    transaction.setAlpha(surfaceControl2, 0.0f).hide(surfaceControl2);
                    return;
                }
            }
            SurfaceControl.Transaction alpha = transaction.setAlpha(surfaceControl, this.mDismissingDimValue);
            if (this.mDismissingDimValue <= 0.001f) {
                z = false;
            }
            alpha.setVisibility(surfaceControl, z);
        }
    }

    /* renamed from: com.android.wm.shell.common.split.SplitLayout$ImePositionProcessor */
    private class ImePositionProcessor implements DisplayImeController.ImePositionProcessor {
        private static final float ADJUSTED_NONFOCUS_DIM = 0.3f;
        private static final float ADJUSTED_SPLIT_FRACTION_MAX = 0.7f;
        private float mDimValue1;
        private float mDimValue2;
        private final int mDisplayId;
        private int mEndImeTop;
        private boolean mHasImeFocus;
        private boolean mImeShown;
        private float mLastDim1;
        private float mLastDim2;
        private int mLastYOffset;
        private int mStartImeTop;
        private float mTargetDim1;
        private float mTargetDim2;
        private int mTargetYOffset;
        private int mYOffsetForIme;

        private float getProgressValue(float f, float f2, float f3) {
            return f + ((f2 - f) * f3);
        }

        private ImePositionProcessor(int i) {
            this.mDisplayId = i;
        }

        public int onImeStartPositioning(int i, int i2, int i3, boolean z, boolean z2, SurfaceControl.Transaction transaction) {
            boolean z3 = false;
            if (i != this.mDisplayId || !SplitLayout.this.mInitialized) {
                return 0;
            }
            int imeTargetPosition = getImeTargetPosition();
            boolean z4 = imeTargetPosition != -1;
            this.mHasImeFocus = z4;
            if (!z4) {
                return 0;
            }
            this.mStartImeTop = z ? i2 : i3;
            if (z) {
                i2 = i3;
            }
            this.mEndImeTop = i2;
            this.mImeShown = z;
            this.mLastDim1 = this.mDimValue1;
            float f = 0.3f;
            this.mTargetDim1 = (imeTargetPosition != 1 || !z || !SplitLayout.this.mDimNonImeSide) ? 0.0f : 0.3f;
            this.mLastDim2 = this.mDimValue2;
            if (imeTargetPosition != 0 || !this.mImeShown || !SplitLayout.this.mDimNonImeSide) {
                f = 0.0f;
            }
            this.mTargetDim2 = f;
            this.mLastYOffset = this.mYOffsetForIme;
            int i4 = (imeTargetPosition != 1 || z2 || SplitLayout.isLandscape(SplitLayout.this.mRootBounds) || !this.mImeShown) ? 0 : 1;
            int targetYOffset = i4 != 0 ? getTargetYOffset() : 0;
            this.mTargetYOffset = targetYOffset;
            if (targetYOffset != this.mLastYOffset) {
                if (targetYOffset == 0) {
                    SplitLayout.this.mSplitLayoutHandler.setLayoutOffsetTarget(0, 0, SplitLayout.this);
                } else {
                    SplitLayout.this.mSplitLayoutHandler.setLayoutOffsetTarget(0, this.mTargetYOffset - this.mLastYOffset, SplitLayout.this);
                }
            }
            SplitWindowManager access$1100 = SplitLayout.this.mSplitWindowManager;
            if (!this.mImeShown || !this.mHasImeFocus) {
                z3 = true;
            }
            access$1100.setInteractive(z3);
            return i4;
        }

        public void onImePositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
            if (i == this.mDisplayId && this.mHasImeFocus) {
                onProgress(getProgress(i2));
                SplitLayout.this.mSplitLayoutHandler.onLayoutPositionChanging(SplitLayout.this);
            }
        }

        public void onImeEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
            if (i == this.mDisplayId && this.mHasImeFocus && !z) {
                onProgress(1.0f);
                SplitLayout.this.mSplitLayoutHandler.onLayoutPositionChanging(SplitLayout.this);
            }
        }

        public void onImeControlTargetChanged(int i, boolean z) {
            if (i == this.mDisplayId && !z && this.mImeShown) {
                reset();
                SplitLayout.this.mSplitWindowManager.setInteractive(true);
                SplitLayout.this.mSplitLayoutHandler.setLayoutOffsetTarget(0, 0, SplitLayout.this);
                SplitLayout.this.mSplitLayoutHandler.onLayoutPositionChanging(SplitLayout.this);
            }
        }

        private int getTargetYOffset() {
            return -Math.min(Math.abs(this.mEndImeTop - this.mStartImeTop), (int) (((float) SplitLayout.this.mBounds1.height()) * 0.7f));
        }

        private int getImeTargetPosition() {
            return SplitLayout.this.mSplitLayoutHandler.getSplitItemPosition(SplitLayout.this.mTaskOrganizer.getImeTarget(this.mDisplayId));
        }

        private float getProgress(int i) {
            int i2 = this.mStartImeTop;
            return (((float) i) - ((float) i2)) / ((float) (this.mEndImeTop - i2));
        }

        private void onProgress(float f) {
            this.mDimValue1 = getProgressValue(this.mLastDim1, this.mTargetDim1, f);
            this.mDimValue2 = getProgressValue(this.mLastDim2, this.mTargetDim2, f);
            this.mYOffsetForIme = (int) getProgressValue((float) this.mLastYOffset, (float) this.mTargetYOffset, f);
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            this.mHasImeFocus = false;
            this.mImeShown = false;
            this.mTargetYOffset = 0;
            this.mLastYOffset = 0;
            this.mYOffsetForIme = 0;
            this.mTargetDim1 = 0.0f;
            this.mLastDim1 = 0.0f;
            this.mDimValue1 = 0.0f;
            this.mTargetDim2 = 0.0f;
            this.mLastDim2 = 0.0f;
            this.mDimValue2 = 0.0f;
        }

        /* access modifiers changed from: package-private */
        public boolean adjustSurfaceLayoutForIme(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, SurfaceControl surfaceControl2, SurfaceControl surfaceControl3, SurfaceControl surfaceControl4, SurfaceControl surfaceControl5) {
            boolean z;
            boolean z2 = false;
            boolean z3 = this.mDimValue1 > 0.001f || this.mDimValue2 > 0.001f;
            if (this.mYOffsetForIme != 0) {
                if (surfaceControl != null) {
                    SplitLayout.this.mTempRect.set(SplitLayout.this.mDividerBounds);
                    SplitLayout.this.mTempRect.offset(0, this.mYOffsetForIme);
                    transaction.setPosition(surfaceControl, (float) SplitLayout.this.mTempRect.left, (float) SplitLayout.this.mTempRect.top);
                }
                SplitLayout.this.mTempRect.set(SplitLayout.this.mBounds1);
                SplitLayout.this.mTempRect.offset(0, this.mYOffsetForIme);
                transaction.setPosition(surfaceControl2, (float) SplitLayout.this.mTempRect.left, (float) SplitLayout.this.mTempRect.top);
                SplitLayout.this.mTempRect.set(SplitLayout.this.mBounds2);
                SplitLayout.this.mTempRect.offset(0, this.mYOffsetForIme);
                transaction.setPosition(surfaceControl3, (float) SplitLayout.this.mTempRect.left, (float) SplitLayout.this.mTempRect.top);
                z = true;
            } else {
                z = false;
            }
            if (!z3) {
                return z;
            }
            transaction.setAlpha(surfaceControl4, this.mDimValue1).setVisibility(surfaceControl4, this.mDimValue1 > 0.001f);
            SurfaceControl.Transaction alpha = transaction.setAlpha(surfaceControl5, this.mDimValue2);
            if (this.mDimValue2 > 0.001f) {
                z2 = true;
            }
            alpha.setVisibility(surfaceControl5, z2);
            return true;
        }
    }
}
