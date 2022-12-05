package com.android.internal.policy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.WindowConfiguration;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.Property;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.InputQueue;
import android.view.InsetsState;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.PendingInsetsController;
import android.view.SurfaceHolder;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewRootImpl;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowCallbacks;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import com.android.internal.R;
import com.android.internal.graphics.drawable.BackgroundBlurDrawable;
import com.android.internal.policy.PhoneWindow;
import com.android.internal.view.FloatingActionMode;
import com.android.internal.view.RootViewSurfaceTaker;
import com.android.internal.view.StandaloneActionMode;
import com.android.internal.view.menu.ContextMenuBuilder;
import com.android.internal.view.menu.MenuHelper;
import com.android.internal.widget.ActionBarContextView;
import com.android.internal.widget.BackgroundFallback;
import com.android.internal.widget.DecorCaptionView;
import com.android.internal.widget.FloatingToolbar;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes4.dex */
public class DecorView extends FrameLayout implements RootViewSurfaceTaker, WindowCallbacks {
    public static final int DECOR_SHADOW_FOCUSED_HEIGHT_IN_DIP = 20;
    public static final int DECOR_SHADOW_UNFOCUSED_HEIGHT_IN_DIP = 5;
    private static final int SCRIM_LIGHT = -419430401;
    private static final boolean SWEEP_OPEN_MENU = false;
    private static final String TAG = "DecorView";
    private float mAvailableWidth;
    private BackgroundBlurDrawable mBackgroundBlurDrawable;
    private final int mBarEnterExitDuration;
    private Drawable mCaptionBackgroundDrawable;
    private boolean mChanging;
    ViewGroup mContentRoot;
    private boolean mCrossWindowBlurEnabled;
    private Consumer<Boolean> mCrossWindowBlurEnabledListener;
    private DecorCaptionView mDecorCaptionView;
    private int mDownY;
    private boolean mDrawLegacyNavigationBarBackground;
    private ObjectAnimator mFadeAnim;
    private final int mFeatureId;
    private ActionMode mFloatingActionMode;
    private View mFloatingActionModeOriginatingView;
    private FloatingToolbar mFloatingToolbar;
    private ViewTreeObserver.OnPreDrawListener mFloatingToolbarPreDrawListener;
    final boolean mForceWindowDrawsBarBackgrounds;
    private final Interpolator mHideInterpolator;
    private boolean mIsInPictureInPictureMode;
    private BackgroundBlurDrawable mLastBackgroundBlurDrawable;
    private Drawable mLastOriginalBackgroundDrawable;
    private ViewOutlineProvider mLastOutlineProvider;
    private final Paint mLegacyNavigationBarBackgroundPaint;
    private Drawable mMenuBackground;
    private Drawable mOriginalBackgroundDrawable;
    private Drawable mPendingWindowBackground;
    ActionMode mPrimaryActionMode;
    private PopupWindow mPrimaryActionModePopup;
    private ActionBarContextView mPrimaryActionModeView;
    private final int mResizeShadowSize;
    private Drawable mResizingBackgroundDrawable;
    private final int mSemiTransparentBarColor;
    private final Interpolator mShowInterpolator;
    private Runnable mShowPrimaryActionModePopup;
    private View mStatusGuard;
    private Rect mTempRect;
    private Drawable mUserCaptionBackgroundDrawable;
    private boolean mWatchingForMenu;
    private PhoneWindow mWindow;
    private static boolean DEBUG_MEASURE = false;
    public static final ColorViewAttributes STATUS_BAR_COLOR_VIEW_ATTRIBUTES = new ColorViewAttributes(67108864, 48, 3, 5, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME, 16908335, 0);
    public static final ColorViewAttributes NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES = new ColorViewAttributes(134217728, 80, 5, 3, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME, 16908336, 1);
    private static final ViewOutlineProvider PIP_OUTLINE_PROVIDER = new ViewOutlineProvider() { // from class: com.android.internal.policy.DecorView.1
        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, 0, view.getWidth(), view.getHeight());
            outline.setAlpha(1.0f);
        }
    };
    private boolean mAllowUpdateElevation = false;
    private boolean mElevationAdjustedForStack = false;
    int mDefaultOpacity = -1;
    private final Rect mDrawingBounds = new Rect();
    private final Rect mBackgroundPadding = new Rect();
    private final Rect mFramePadding = new Rect();
    private final Rect mFrameOffsets = new Rect();
    private boolean mHasCaption = false;
    private final ColorViewState mStatusColorViewState = new ColorViewState(STATUS_BAR_COLOR_VIEW_ATTRIBUTES);
    private final ColorViewState mNavigationColorViewState = new ColorViewState(NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES);
    private final BackgroundFallback mBackgroundFallback = new BackgroundFallback();
    private int mLastTopInset = 0;
    private int mLastBottomInset = 0;
    private int mLastRightInset = 0;
    private int mLastLeftInset = 0;
    private boolean mLastHasTopStableInset = false;
    private boolean mLastHasBottomStableInset = false;
    private boolean mLastHasRightStableInset = false;
    private boolean mLastHasLeftStableInset = false;
    private int mLastWindowFlags = 0;
    private boolean mLastShouldAlwaysConsumeSystemBars = false;
    private int mRootScrollY = 0;
    private boolean mWindowResizeCallbacksAdded = false;
    private Drawable.Callback mLastBackgroundDrawableCb = null;
    private BackdropFrameRenderer mBackdropFrameRenderer = null;
    String mLogTag = TAG;
    private final Rect mFloatingInsets = new Rect();
    private boolean mApplyFloatingVerticalInsets = false;
    private boolean mApplyFloatingHorizontalInsets = false;
    private int mResizeMode = -1;
    private final Paint mVerticalResizeShadowPaint = new Paint();
    private final Paint mHorizontalResizeShadowPaint = new Paint();
    private Insets mBackgroundInsets = Insets.NONE;
    private Insets mLastBackgroundInsets = Insets.NONE;
    private PendingInsetsController mPendingInsetsController = new PendingInsetsController();
    private int mOriginalBackgroundBlurRadius = 0;
    private int mBackgroundBlurRadius = 0;
    private final ViewTreeObserver.OnPreDrawListener mBackgroundBlurOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.internal.policy.DecorView$$ExternalSyntheticLambda0
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public final boolean onPreDraw() {
            return DecorView.this.lambda$new$0$DecorView();
        }
    };

    public /* synthetic */ boolean lambda$new$0$DecorView() {
        updateBackgroundBlurCorners();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DecorView(Context context, int featureId, PhoneWindow window, WindowManager.LayoutParams params) {
        super(context);
        boolean z = false;
        Paint paint = new Paint();
        this.mLegacyNavigationBarBackgroundPaint = paint;
        this.mFeatureId = featureId;
        this.mShowInterpolator = AnimationUtils.loadInterpolator(context, 17563662);
        this.mHideInterpolator = AnimationUtils.loadInterpolator(context, 17563663);
        this.mBarEnterExitDuration = context.getResources().getInteger(R.integer.dock_enter_exit_duration);
        if (context.getResources().getBoolean(R.bool.config_forceWindowDrawsStatusBarBackground) && context.getApplicationInfo().targetSdkVersion >= 24) {
            z = true;
        }
        this.mForceWindowDrawsBarBackgrounds = z;
        this.mSemiTransparentBarColor = context.getResources().getColor(R.color.system_bar_background_semi_transparent, null);
        updateAvailableWidth();
        setWindow(window);
        updateLogTag(params);
        this.mResizeShadowSize = context.getResources().getDimensionPixelSize(R.dimen.resize_shadow_size);
        initResizingPaints();
        paint.setColor(-16777216);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackgroundFallback(Drawable fallbackDrawable) {
        this.mBackgroundFallback.setDrawable(fallbackDrawable);
        setWillNotDraw(getBackground() == null && !this.mBackgroundFallback.hasFallback());
    }

    public Drawable getBackgroundFallback() {
        return this.mBackgroundFallback.getDrawable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View getStatusBarBackgroundView() {
        return this.mStatusColorViewState.view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View getNavigationBarBackgroundView() {
        return this.mNavigationColorViewState.view;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean gatherTransparentRegion(Region region) {
        boolean statusOpaque = gatherTransparentRegion(this.mStatusColorViewState, region);
        boolean navOpaque = gatherTransparentRegion(this.mNavigationColorViewState, region);
        boolean decorOpaque = super.gatherTransparentRegion(region);
        return statusOpaque || navOpaque || decorOpaque;
    }

    boolean gatherTransparentRegion(ColorViewState colorViewState, Region region) {
        if (colorViewState.view != null && colorViewState.visible && isResizing()) {
            return colorViewState.view.gatherTransparentRegion(region);
        }
        return false;
    }

    @Override // android.view.View
    public void onDraw(Canvas c) {
        super.onDraw(c);
        this.mBackgroundFallback.draw(this, this.mContentRoot, c, this.mWindow.mContentParent, this.mStatusColorViewState.view, this.mNavigationColorViewState.view);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int action = event.getAction();
        boolean isDown = action == 0;
        if (isDown && event.getRepeatCount() == 0) {
            if (this.mWindow.mPanelChordingKey > 0 && this.mWindow.mPanelChordingKey != keyCode) {
                boolean handled = dispatchKeyShortcutEvent(event);
                if (handled) {
                    return true;
                }
            }
            if (this.mWindow.mPreparedPanel != null && this.mWindow.mPreparedPanel.isOpen) {
                PhoneWindow phoneWindow = this.mWindow;
                if (phoneWindow.performPanelShortcut(phoneWindow.mPreparedPanel, keyCode, event, 0)) {
                    return true;
                }
            }
        }
        if (!this.mWindow.isDestroyed()) {
            Window.Callback cb = this.mWindow.getCallback();
            boolean handled2 = (cb == null || this.mFeatureId >= 0) ? super.dispatchKeyEvent(event) : cb.dispatchKeyEvent(event);
            if (handled2) {
                return true;
            }
        }
        return isDown ? this.mWindow.onKeyDown(this.mFeatureId, event.getKeyCode(), event) : this.mWindow.onKeyUp(this.mFeatureId, event.getKeyCode(), event);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyShortcutEvent(KeyEvent ev) {
        if (this.mWindow.mPreparedPanel != null) {
            PhoneWindow phoneWindow = this.mWindow;
            boolean handled = phoneWindow.performPanelShortcut(phoneWindow.mPreparedPanel, ev.getKeyCode(), ev, 1);
            if (handled) {
                if (this.mWindow.mPreparedPanel != null) {
                    this.mWindow.mPreparedPanel.isHandled = true;
                }
                return true;
            }
        }
        Window.Callback cb = this.mWindow.getCallback();
        boolean handled2 = (cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchKeyShortcutEvent(ev) : cb.dispatchKeyShortcutEvent(ev);
        if (handled2) {
            return true;
        }
        PhoneWindow.PanelFeatureState st = this.mWindow.getPanelState(0, false);
        if (st != null && this.mWindow.mPreparedPanel == null) {
            this.mWindow.preparePanel(st, ev);
            boolean handled3 = this.mWindow.performPanelShortcut(st, ev.getKeyCode(), ev, 1);
            st.isPrepared = false;
            if (handled3) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Window.Callback cb = this.mWindow.getCallback();
        return (cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchTouchEvent(ev) : cb.dispatchTouchEvent(ev);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTrackballEvent(MotionEvent ev) {
        Window.Callback cb = this.mWindow.getCallback();
        return (cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchTrackballEvent(ev) : cb.dispatchTrackballEvent(ev);
    }

    @Override // android.view.View
    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        Window.Callback cb = this.mWindow.getCallback();
        return (cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchGenericMotionEvent(ev) : cb.dispatchGenericMotionEvent(ev);
    }

    public boolean superDispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == 4) {
            int action = event.getAction();
            ActionMode actionMode = this.mPrimaryActionMode;
            if (actionMode != null) {
                if (action == 1) {
                    actionMode.finish();
                }
                return true;
            }
        }
        if (super.dispatchKeyEvent(event)) {
            return true;
        }
        return getViewRootImpl() != null && getViewRootImpl().dispatchUnhandledKeyEvent(event);
    }

    public boolean superDispatchKeyShortcutEvent(KeyEvent event) {
        return super.dispatchKeyShortcutEvent(event);
    }

    public boolean superDispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    public boolean superDispatchTrackballEvent(MotionEvent event) {
        return super.dispatchTrackballEvent(event);
    }

    public boolean superDispatchGenericMotionEvent(MotionEvent event) {
        return super.dispatchGenericMotionEvent(event);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        return onInterceptTouchEvent(event);
    }

    private boolean isOutOfInnerBounds(int x, int y) {
        return x < 0 || y < 0 || x > getWidth() || y > getHeight();
    }

    private boolean isOutOfBounds(int x, int y) {
        return x < -5 || y < -5 || x > getWidth() + 5 || y > getHeight() + 5;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (this.mHasCaption && isShowingCaption() && action == 0) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (isOutOfInnerBounds(x, y)) {
                return true;
            }
        }
        int x2 = this.mFeatureId;
        if (x2 >= 0 && action == 0) {
            int x3 = (int) event.getX();
            int y2 = (int) event.getY();
            if (isOutOfBounds(x3, y2)) {
                this.mWindow.closePanel(this.mFeatureId);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override // android.view.View, android.view.accessibility.AccessibilityEventSource
    public void sendAccessibilityEvent(int eventType) {
        if (!AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            return;
        }
        int i = this.mFeatureId;
        if ((i == 0 || i == 6 || i == 2 || i == 5) && getChildCount() == 1) {
            getChildAt(0).sendAccessibilityEvent(eventType);
        } else {
            super.sendAccessibilityEvent(eventType);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        Window.Callback cb = this.mWindow.getCallback();
        if (cb != null && !this.mWindow.isDestroyed() && cb.dispatchPopulateAccessibilityEvent(event)) {
            return true;
        }
        return super.dispatchPopulateAccessibilityEventInternal(event);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (changed) {
            Rect drawingBounds = this.mDrawingBounds;
            getDrawingRect(drawingBounds);
            Drawable fg = getForeground();
            if (fg != null) {
                Rect frameOffsets = this.mFrameOffsets;
                drawingBounds.left += frameOffsets.left;
                drawingBounds.top += frameOffsets.top;
                drawingBounds.right -= frameOffsets.right;
                drawingBounds.bottom -= frameOffsets.bottom;
                fg.setBounds(drawingBounds);
                Rect framePadding = this.mFramePadding;
                drawingBounds.left += framePadding.left - frameOffsets.left;
                drawingBounds.top += framePadding.top - frameOffsets.top;
                drawingBounds.right -= framePadding.right - frameOffsets.right;
                drawingBounds.bottom -= framePadding.bottom - frameOffsets.bottom;
            }
            Drawable bg = super.getBackground();
            if (bg != null) {
                bg.setBounds(drawingBounds);
            }
        }
        return changed;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0127 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0130  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0187  */
    /* JADX WARN: Removed duplicated region for block: B:62:? A[RETURN, SYNTHETIC] */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureSpec2;
        int heightMeasureSpec2;
        boolean measure;
        TypedValue tv;
        int min;
        TypedValue tvh;
        int h;
        int w;
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        boolean isPortrait = getResources().getConfiguration().orientation == 1;
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        boolean fixedWidth = false;
        this.mApplyFloatingHorizontalInsets = false;
        if (widthMode == Integer.MIN_VALUE) {
            PhoneWindow phoneWindow = this.mWindow;
            TypedValue tvw = isPortrait ? phoneWindow.mFixedWidthMinor : phoneWindow.mFixedWidthMajor;
            if (tvw != null && tvw.type != 0) {
                if (tvw.type == 5) {
                    w = (int) tvw.getDimension(metrics);
                } else {
                    int w2 = tvw.type;
                    if (w2 == 6) {
                        w = (int) tvw.getFraction(metrics.widthPixels, metrics.widthPixels);
                    } else {
                        w = 0;
                    }
                }
                if (DEBUG_MEASURE) {
                    String str = this.mLogTag;
                    Log.d(str, "Fixed width: " + w);
                }
                int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
                if (w <= 0) {
                    widthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec((widthSize - this.mFloatingInsets.left) - this.mFloatingInsets.right, Integer.MIN_VALUE);
                    this.mApplyFloatingHorizontalInsets = true;
                } else {
                    widthMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(Math.min(w, widthSize), 1073741824);
                    fixedWidth = true;
                }
                this.mApplyFloatingVerticalInsets = false;
                if (heightMode == Integer.MIN_VALUE) {
                    if (isPortrait) {
                        tvh = this.mWindow.mFixedHeightMajor;
                    } else {
                        tvh = this.mWindow.mFixedHeightMinor;
                    }
                    if (tvh != null && tvh.type != 0) {
                        if (tvh.type == 5) {
                            h = (int) tvh.getDimension(metrics);
                        } else {
                            int h2 = tvh.type;
                            if (h2 == 6) {
                                h = (int) tvh.getFraction(metrics.heightPixels, metrics.heightPixels);
                            } else {
                                h = 0;
                            }
                        }
                        if (DEBUG_MEASURE) {
                            String str2 = this.mLogTag;
                            Log.d(str2, "Fixed height: " + h);
                        }
                        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
                        if (h > 0) {
                            heightMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(Math.min(h, heightSize), 1073741824);
                        } else if ((this.mWindow.getAttributes().flags & 256) == 0) {
                            heightMeasureSpec2 = View.MeasureSpec.makeMeasureSpec((heightSize - this.mFloatingInsets.top) - this.mFloatingInsets.bottom, Integer.MIN_VALUE);
                            this.mApplyFloatingVerticalInsets = true;
                        }
                        super.onMeasure(widthMeasureSpec2, heightMeasureSpec2);
                        int width = getMeasuredWidth();
                        measure = false;
                        int widthMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(width, 1073741824);
                        if (!fixedWidth && widthMode == Integer.MIN_VALUE) {
                            PhoneWindow phoneWindow2 = this.mWindow;
                            tv = !isPortrait ? phoneWindow2.mMinWidthMinor : phoneWindow2.mMinWidthMajor;
                            if (tv.type != 0) {
                                if (tv.type == 5) {
                                    min = (int) tv.getDimension(metrics);
                                } else {
                                    int min2 = tv.type;
                                    if (min2 == 6) {
                                        float f = this.mAvailableWidth;
                                        min = (int) tv.getFraction(f, f);
                                    } else {
                                        min = 0;
                                    }
                                }
                                if (DEBUG_MEASURE) {
                                    String str3 = this.mLogTag;
                                    Log.d(str3, "Adjust for min width: " + min + ", value::" + ((Object) tv.coerceToString()) + ", mAvailableWidth=" + this.mAvailableWidth);
                                }
                                if (width < min) {
                                    widthMeasureSpec3 = View.MeasureSpec.makeMeasureSpec(min, 1073741824);
                                    measure = true;
                                }
                            }
                        }
                        if (measure) {
                            super.onMeasure(widthMeasureSpec3, heightMeasureSpec2);
                            return;
                        }
                        return;
                    }
                }
                heightMeasureSpec2 = heightMeasureSpec;
                super.onMeasure(widthMeasureSpec2, heightMeasureSpec2);
                int width2 = getMeasuredWidth();
                measure = false;
                int widthMeasureSpec32 = View.MeasureSpec.makeMeasureSpec(width2, 1073741824);
                if (!fixedWidth) {
                    PhoneWindow phoneWindow22 = this.mWindow;
                    if (!isPortrait) {
                    }
                    if (tv.type != 0) {
                    }
                }
                if (measure) {
                }
            }
        }
        widthMeasureSpec2 = widthMeasureSpec;
        this.mApplyFloatingVerticalInsets = false;
        if (heightMode == Integer.MIN_VALUE) {
        }
        heightMeasureSpec2 = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec2, heightMeasureSpec2);
        int width22 = getMeasuredWidth();
        measure = false;
        int widthMeasureSpec322 = View.MeasureSpec.makeMeasureSpec(width22, 1073741824);
        if (!fixedWidth) {
        }
        if (measure) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mApplyFloatingVerticalInsets) {
            offsetTopAndBottom(this.mFloatingInsets.top);
        }
        if (this.mApplyFloatingHorizontalInsets) {
            offsetLeftAndRight(this.mFloatingInsets.left);
        }
        updateElevation();
        this.mAllowUpdateElevation = true;
        if (changed) {
            if (this.mResizeMode == 1 || this.mDrawLegacyNavigationBarBackground) {
                getViewRootImpl().requestInvalidateRootRenderNode();
            }
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Drawable drawable = this.mMenuBackground;
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View originalView) {
        return showContextMenuForChildInternal(originalView, Float.NaN, Float.NaN);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return showContextMenuForChildInternal(originalView, x, y);
    }

    private boolean showContextMenuForChildInternal(View originalView, float x, float y) {
        MenuHelper helper;
        if (this.mWindow.mContextMenuHelper != null) {
            this.mWindow.mContextMenuHelper.dismiss();
            this.mWindow.mContextMenuHelper = null;
        }
        PhoneWindow.PhoneWindowMenuCallback callback = this.mWindow.mContextMenuCallback;
        if (this.mWindow.mContextMenu == null) {
            this.mWindow.mContextMenu = new ContextMenuBuilder(getContext());
            this.mWindow.mContextMenu.setCallback(callback);
        } else {
            this.mWindow.mContextMenu.clearAll();
        }
        boolean isPopup = !Float.isNaN(x) && !Float.isNaN(y);
        if (isPopup) {
            helper = this.mWindow.mContextMenu.showPopup(getContext(), originalView, x, y);
        } else {
            helper = this.mWindow.mContextMenu.showDialog(originalView, originalView.getWindowToken());
        }
        if (helper != null) {
            callback.setShowDialogForSubmenu(!isPopup);
            helper.setPresenterCallback(callback);
        }
        this.mWindow.mContextMenuHelper = helper;
        return helper != null;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        return startActionModeForChild(originalView, callback, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View child, ActionMode.Callback callback, int type) {
        return startActionMode(child, callback, type);
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return startActionMode(callback, 0);
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        return startActionMode(this, callback, type);
    }

    private ActionMode startActionMode(View originatingView, ActionMode.Callback callback, int type) {
        ActionMode.Callback2 wrappedCallback = new ActionModeCallback2Wrapper(callback);
        ActionMode mode = null;
        if (this.mWindow.getCallback() != null && !this.mWindow.isDestroyed()) {
            try {
                mode = this.mWindow.getCallback().onWindowStartingActionMode(wrappedCallback, type);
            } catch (AbstractMethodError e) {
                if (type == 0) {
                    try {
                        mode = this.mWindow.getCallback().onWindowStartingActionMode(wrappedCallback);
                    } catch (AbstractMethodError e2) {
                    }
                }
            }
        }
        if (mode != null) {
            if (mode.getType() == 0) {
                cleanupPrimaryActionMode();
                this.mPrimaryActionMode = mode;
            } else if (mode.getType() == 1) {
                ActionMode actionMode = this.mFloatingActionMode;
                if (actionMode != null) {
                    actionMode.finish();
                }
                this.mFloatingActionMode = mode;
            }
        } else {
            mode = createActionMode(type, wrappedCallback, originatingView);
            if (mode != null && wrappedCallback.onCreateActionMode(mode, mode.getMenu())) {
                setHandledActionMode(mode);
            } else {
                mode = null;
            }
        }
        if (mode != null && this.mWindow.getCallback() != null && !this.mWindow.isDestroyed()) {
            try {
                this.mWindow.getCallback().onActionModeStarted(mode);
            } catch (AbstractMethodError e3) {
            }
        }
        return mode;
    }

    private void cleanupPrimaryActionMode() {
        ActionMode actionMode = this.mPrimaryActionMode;
        if (actionMode != null) {
            actionMode.finish();
            this.mPrimaryActionMode = null;
        }
        ActionBarContextView actionBarContextView = this.mPrimaryActionModeView;
        if (actionBarContextView != null) {
            actionBarContextView.killMode();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanupFloatingActionModeViews() {
        FloatingToolbar floatingToolbar = this.mFloatingToolbar;
        if (floatingToolbar != null) {
            floatingToolbar.dismiss();
            this.mFloatingToolbar = null;
        }
        View view = this.mFloatingActionModeOriginatingView;
        if (view != null) {
            if (this.mFloatingToolbarPreDrawListener != null) {
                view.getViewTreeObserver().removeOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
                this.mFloatingToolbarPreDrawListener = null;
            }
            this.mFloatingActionModeOriginatingView = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startChanging() {
        this.mChanging = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void finishChanging() {
        this.mChanging = false;
        drawableChanged();
    }

    public void setWindowBackground(Drawable drawable) {
        if (this.mWindow == null) {
            this.mPendingWindowBackground = drawable;
        } else if (this.mOriginalBackgroundDrawable != drawable) {
            this.mOriginalBackgroundDrawable = drawable;
            updateBackgroundDrawable();
            boolean z = false;
            if (drawable != null) {
                if (this.mWindow.isTranslucent() || this.mWindow.isShowingWallpaper()) {
                    z = true;
                }
                this.mResizingBackgroundDrawable = enforceNonTranslucentBackground(drawable, z);
            } else {
                Drawable drawable2 = this.mWindow.mBackgroundDrawable;
                Drawable drawable3 = this.mWindow.mBackgroundFallbackDrawable;
                if (this.mWindow.isTranslucent() || this.mWindow.isShowingWallpaper()) {
                    z = true;
                }
                this.mResizingBackgroundDrawable = getResizingBackgroundDrawable(drawable2, drawable3, z);
            }
            Drawable drawable4 = this.mResizingBackgroundDrawable;
            if (drawable4 != null) {
                drawable4.getPadding(this.mBackgroundPadding);
            } else {
                this.mBackgroundPadding.setEmpty();
            }
            if (!View.sBrokenWindowBackground) {
                drawableChanged();
            }
        }
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable background) {
        setWindowBackground(background);
    }

    public void setWindowFrame(Drawable drawable) {
        if (getForeground() != drawable) {
            setForeground(drawable);
            if (drawable != null) {
                drawable.getPadding(this.mFramePadding);
            } else {
                this.mFramePadding.setEmpty();
            }
            drawableChanged();
        }
    }

    @Override // android.view.View
    public void onWindowSystemUiVisibilityChanged(int visible) {
        updateColorViews(null, true);
        updateDecorCaptionStatus(getResources().getConfiguration());
        View view = this.mStatusGuard;
        if (view != null && view.getVisibility() == 0) {
            updateStatusGuardColor();
        }
    }

    @Override // android.view.View
    public void onSystemBarAppearanceChanged(int appearance) {
        updateColorViews(null, true);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        WindowManager.LayoutParams attrs = this.mWindow.getAttributes();
        this.mFloatingInsets.setEmpty();
        if ((attrs.flags & 256) == 0) {
            if (attrs.height == -2) {
                this.mFloatingInsets.top = insets.getSystemWindowInsetTop();
                this.mFloatingInsets.bottom = insets.getSystemWindowInsetBottom();
                insets = insets.inset(0, insets.getSystemWindowInsetTop(), 0, insets.getSystemWindowInsetBottom());
            }
            if (this.mWindow.getAttributes().width == -2) {
                this.mFloatingInsets.left = insets.getSystemWindowInsetTop();
                this.mFloatingInsets.right = insets.getSystemWindowInsetBottom();
                insets = insets.inset(insets.getSystemWindowInsetLeft(), 0, insets.getSystemWindowInsetRight(), 0);
            }
        }
        this.mFrameOffsets.set(insets.getSystemWindowInsetsAsRect());
        WindowInsets insets2 = updateStatusGuard(updateColorViews(insets, true));
        if (getForeground() != null) {
            drawableChanged();
        }
        return insets2;
    }

    @Override // android.view.ViewGroup
    public boolean isTransitionGroup() {
        return false;
    }

    public static boolean isNavBarToRightEdge(int bottomInset, int rightInset) {
        return bottomInset == 0 && rightInset > 0;
    }

    public static boolean isNavBarToLeftEdge(int bottomInset, int leftInset) {
        return bottomInset == 0 && leftInset > 0;
    }

    public static int getNavBarSize(int bottomInset, int rightInset, int leftInset) {
        return isNavBarToRightEdge(bottomInset, rightInset) ? rightInset : isNavBarToLeftEdge(bottomInset, leftInset) ? leftInset : bottomInset;
    }

    public static void getNavigationBarRect(int canvasWidth, int canvasHeight, Rect systemBarInsets, Rect outRect, float scale) {
        int bottomInset = (int) (systemBarInsets.bottom * scale);
        int leftInset = (int) (systemBarInsets.left * scale);
        int rightInset = (int) (systemBarInsets.right * scale);
        int size = getNavBarSize(bottomInset, rightInset, leftInset);
        if (isNavBarToRightEdge(bottomInset, rightInset)) {
            outRect.set(canvasWidth - size, 0, canvasWidth, canvasHeight);
        } else if (isNavBarToLeftEdge(bottomInset, leftInset)) {
            outRect.set(0, 0, size, canvasHeight);
        } else {
            outRect.set(0, canvasHeight - size, canvasWidth, canvasHeight);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x01c9, code lost:
        if (r15.isRequestedVisible(1) == false) goto L104;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x01e8, code lost:
        if (r1 == 0) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x021c, code lost:
        if (r15.isRequestedVisible(0) == false) goto L94;
     */
    /* JADX WARN: Removed duplicated region for block: B:103:0x01eb  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x01da  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x01fd  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x020f  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x0229 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x024c  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0253  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0259  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x025f  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0267  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x02ad  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02c1  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x02a9  */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0262  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x025c  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0256  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x024f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public WindowInsets updateColorViews(WindowInsets insets, boolean animate) {
        int systemBarsAppearance;
        boolean disallowAnimate;
        WindowManager.LayoutParams attrs;
        int statusBarSideInset;
        Insets systemInsets;
        int i;
        int i2;
        WindowManager.LayoutParams attrs2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        ViewGroup viewGroup;
        WindowInsets insets2;
        WindowManager.LayoutParams attrs3 = this.mWindow.getAttributes();
        int sysUiVisibility = attrs3.systemUiVisibility | getWindowSystemUiVisibility();
        WindowInsetsController controller = getWindowInsetsController();
        boolean isImeWindow = this.mWindow.getAttributes().type == 2011;
        if (!this.mWindow.mIsFloating || isImeWindow) {
            boolean disallowAnimate2 = (!isLaidOut()) | (((this.mLastWindowFlags ^ attrs3.flags) & Integer.MIN_VALUE) != 0);
            this.mLastWindowFlags = attrs3.flags;
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot != null) {
                systemBarsAppearance = viewRoot.mWindowAttributes.insetsFlags.appearance;
            } else {
                systemBarsAppearance = controller.getSystemBarsAppearance();
            }
            int appearance = systemBarsAppearance;
            if (insets == null) {
                disallowAnimate = disallowAnimate2;
            } else {
                boolean clearCompatInsets = InsetsState.clearCompatInsets(attrs3.type, attrs3.flags, getResources().getConfiguration().windowConfiguration.getWindowingMode());
                Insets stableBarInsets = insets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
                if (clearCompatInsets) {
                    systemInsets = Insets.NONE;
                } else {
                    systemInsets = Insets.min(insets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout()), stableBarInsets);
                }
                this.mLastTopInset = systemInsets.top;
                this.mLastBottomInset = systemInsets.bottom;
                this.mLastRightInset = systemInsets.right;
                this.mLastLeftInset = systemInsets.left;
                boolean hasTopStableInset = stableBarInsets.top != 0;
                boolean disallowAnimate3 = disallowAnimate2 | (hasTopStableInset != this.mLastHasTopStableInset);
                this.mLastHasTopStableInset = hasTopStableInset;
                boolean hasBottomStableInset = stableBarInsets.bottom != 0;
                boolean disallowAnimate4 = disallowAnimate3 | (hasBottomStableInset != this.mLastHasBottomStableInset);
                this.mLastHasBottomStableInset = hasBottomStableInset;
                boolean hasRightStableInset = stableBarInsets.right != 0;
                boolean disallowAnimate5 = disallowAnimate4 | (hasRightStableInset != this.mLastHasRightStableInset);
                this.mLastHasRightStableInset = hasRightStableInset;
                boolean hasLeftStableInset = stableBarInsets.left != 0;
                boolean disallowAnimate6 = disallowAnimate5 | (hasLeftStableInset != this.mLastHasLeftStableInset);
                this.mLastHasLeftStableInset = hasLeftStableInset;
                this.mLastShouldAlwaysConsumeSystemBars = insets.shouldAlwaysConsumeSystemBars();
                disallowAnimate = disallowAnimate6;
            }
            boolean navBarToRightEdge = isNavBarToRightEdge(this.mLastBottomInset, this.mLastRightInset);
            boolean navBarToLeftEdge = isNavBarToLeftEdge(this.mLastBottomInset, this.mLastLeftInset);
            int navBarSize = getNavBarSize(this.mLastBottomInset, this.mLastRightInset, this.mLastLeftInset);
            attrs = attrs3;
            updateColorViewInt(this.mNavigationColorViewState, calculateNavigationBarColor(appearance), this.mWindow.mNavigationBarDividerColor, navBarSize, navBarToRightEdge || navBarToLeftEdge, navBarToLeftEdge, 0, animate && !disallowAnimate, this.mForceWindowDrawsBarBackgrounds, controller);
            boolean oldDrawLegacy = this.mDrawLegacyNavigationBarBackground;
            boolean z = this.mNavigationColorViewState.visible && (this.mWindow.getAttributes().flags & Integer.MIN_VALUE) == 0;
            this.mDrawLegacyNavigationBarBackground = z;
            if (oldDrawLegacy != z && viewRoot != null) {
                viewRoot.requestInvalidateRootRenderNode();
            }
            boolean statusBarNeedsRightInset = navBarToRightEdge && this.mNavigationColorViewState.present;
            boolean statusBarNeedsLeftInset = navBarToLeftEdge && this.mNavigationColorViewState.present;
            if (statusBarNeedsRightInset) {
                statusBarSideInset = this.mLastRightInset;
            } else {
                statusBarSideInset = statusBarNeedsLeftInset ? this.mLastLeftInset : 0;
            }
            int statusBarColor = calculateStatusBarColor(appearance);
            updateColorViewInt(this.mStatusColorViewState, statusBarColor, 0, this.mLastTopInset, false, statusBarNeedsLeftInset, statusBarSideInset, animate && !disallowAnimate, this.mForceWindowDrawsBarBackgrounds, controller);
            if (this.mHasCaption) {
                this.mDecorCaptionView.getCaption().setBackgroundColor(statusBarColor);
                updateDecorCaptionShade();
            }
        } else {
            attrs = attrs3;
        }
        if ((sysUiVisibility & 2) != 0) {
            i = 1;
        } else {
            if (controller == null) {
                i = 1;
            } else {
                i = 1;
            }
            i2 = 0;
            int i8 = i2;
            boolean decorFitsSystemWindows = this.mWindow.mDecorFitsSystemWindows;
            if (!this.mForceWindowDrawsBarBackgrounds) {
                attrs2 = attrs;
                if ((attrs2.flags & Integer.MIN_VALUE) == 0) {
                    if ((sysUiVisibility & 512) == 0) {
                        if (decorFitsSystemWindows) {
                        }
                    }
                }
            } else {
                attrs2 = attrs;
            }
            if (this.mLastShouldAlwaysConsumeSystemBars || i8 == 0) {
                i3 = 0;
                i4 = i3;
                i5 = (!((attrs2.flags & Integer.MIN_VALUE) == 0 && (sysUiVisibility & 512) == 0 && decorFitsSystemWindows && i8 == 0) && i4 == 0) ? 0 : i;
                if ((sysUiVisibility & 4) == 0 || (attrs2.flags & 1024) != 0) {
                    i6 = 0;
                } else {
                    if (controller == null) {
                        i6 = 0;
                    } else {
                        i6 = 0;
                    }
                    i7 = i6;
                    int consumedTop = ((((sysUiVisibility & 1024) == 0 || !decorFitsSystemWindows || (attrs2.flags & 256) != 0 || (attrs2.flags & 65536) != 0 || !this.mForceWindowDrawsBarBackgrounds || this.mLastTopInset == 0) && (!this.mLastShouldAlwaysConsumeSystemBars || i7 == 0)) ? i6 : i) == 0 ? this.mLastTopInset : i6;
                    int consumedRight = i5 == 0 ? this.mLastRightInset : i6;
                    int consumedBottom = i5 == 0 ? this.mLastBottomInset : i6;
                    int consumedLeft = i5 == 0 ? this.mLastLeftInset : i6;
                    viewGroup = this.mContentRoot;
                    if (viewGroup != null) {
                        insets2 = insets;
                    } else if (!(viewGroup.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
                        insets2 = insets;
                    } else {
                        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) this.mContentRoot.getLayoutParams();
                        if (lp.topMargin == consumedTop && lp.rightMargin == consumedRight && lp.bottomMargin == consumedBottom && lp.leftMargin == consumedLeft) {
                            insets2 = insets;
                        } else {
                            lp.topMargin = consumedTop;
                            lp.rightMargin = consumedRight;
                            lp.bottomMargin = consumedBottom;
                            lp.leftMargin = consumedLeft;
                            this.mContentRoot.setLayoutParams(lp);
                            insets2 = insets;
                            if (insets2 == null) {
                                requestApplyInsets();
                            }
                        }
                        if (insets2 != null) {
                            insets2 = insets2.inset(consumedLeft, consumedTop, consumedRight, consumedBottom);
                        }
                    }
                    if (i4 == 0) {
                        this.mBackgroundInsets = Insets.of(this.mLastLeftInset, 0, this.mLastRightInset, this.mLastBottomInset);
                    } else {
                        this.mBackgroundInsets = Insets.NONE;
                    }
                    updateBackgroundDrawable();
                    return insets2;
                }
                i7 = i;
                int consumedTop2 = ((((sysUiVisibility & 1024) == 0 || !decorFitsSystemWindows || (attrs2.flags & 256) != 0 || (attrs2.flags & 65536) != 0 || !this.mForceWindowDrawsBarBackgrounds || this.mLastTopInset == 0) && (!this.mLastShouldAlwaysConsumeSystemBars || i7 == 0)) ? i6 : i) == 0 ? this.mLastTopInset : i6;
                if (i5 == 0) {
                }
                if (i5 == 0) {
                }
                if (i5 == 0) {
                }
                viewGroup = this.mContentRoot;
                if (viewGroup != null) {
                }
                if (i4 == 0) {
                }
                updateBackgroundDrawable();
                return insets2;
            }
            i3 = i;
            i4 = i3;
            i5 = (!((attrs2.flags & Integer.MIN_VALUE) == 0 && (sysUiVisibility & 512) == 0 && decorFitsSystemWindows && i8 == 0) && i4 == 0) ? 0 : i;
            if ((sysUiVisibility & 4) == 0) {
            }
            i6 = 0;
            i7 = i;
            int consumedTop22 = ((((sysUiVisibility & 1024) == 0 || !decorFitsSystemWindows || (attrs2.flags & 256) != 0 || (attrs2.flags & 65536) != 0 || !this.mForceWindowDrawsBarBackgrounds || this.mLastTopInset == 0) && (!this.mLastShouldAlwaysConsumeSystemBars || i7 == 0)) ? i6 : i) == 0 ? this.mLastTopInset : i6;
            if (i5 == 0) {
            }
            if (i5 == 0) {
            }
            if (i5 == 0) {
            }
            viewGroup = this.mContentRoot;
            if (viewGroup != null) {
            }
            if (i4 == 0) {
            }
            updateBackgroundDrawable();
            return insets2;
        }
        i2 = i;
        int i82 = i2;
        boolean decorFitsSystemWindows2 = this.mWindow.mDecorFitsSystemWindows;
        if (!this.mForceWindowDrawsBarBackgrounds) {
        }
        if (this.mLastShouldAlwaysConsumeSystemBars) {
        }
        i3 = 0;
        i4 = i3;
        i5 = (!((attrs2.flags & Integer.MIN_VALUE) == 0 && (sysUiVisibility & 512) == 0 && decorFitsSystemWindows2 && i82 == 0) && i4 == 0) ? 0 : i;
        if ((sysUiVisibility & 4) == 0) {
        }
        i6 = 0;
        i7 = i;
        int consumedTop222 = ((((sysUiVisibility & 1024) == 0 || !decorFitsSystemWindows2 || (attrs2.flags & 256) != 0 || (attrs2.flags & 65536) != 0 || !this.mForceWindowDrawsBarBackgrounds || this.mLastTopInset == 0) && (!this.mLastShouldAlwaysConsumeSystemBars || i7 == 0)) ? i6 : i) == 0 ? this.mLastTopInset : i6;
        if (i5 == 0) {
        }
        if (i5 == 0) {
        }
        if (i5 == 0) {
        }
        viewGroup = this.mContentRoot;
        if (viewGroup != null) {
        }
        if (i4 == 0) {
        }
        updateBackgroundDrawable();
        return insets2;
    }

    private void updateBackgroundDrawable() {
        if (this.mBackgroundInsets == null) {
            this.mBackgroundInsets = Insets.NONE;
        }
        if (this.mBackgroundInsets.equals(this.mLastBackgroundInsets) && this.mBackgroundBlurDrawable == this.mLastBackgroundBlurDrawable && this.mLastOriginalBackgroundDrawable == this.mOriginalBackgroundDrawable) {
            return;
        }
        Drawable destDrawable = this.mOriginalBackgroundDrawable;
        if (this.mBackgroundBlurDrawable != null) {
            destDrawable = new LayerDrawable(new Drawable[]{this.mBackgroundBlurDrawable, this.mOriginalBackgroundDrawable});
        }
        if (destDrawable != null && !this.mBackgroundInsets.equals(Insets.NONE)) {
            destDrawable = new InsetDrawable(destDrawable, this.mBackgroundInsets.left, this.mBackgroundInsets.top, this.mBackgroundInsets.right, this.mBackgroundInsets.bottom) { // from class: com.android.internal.policy.DecorView.2
                @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
                public boolean getPadding(Rect padding) {
                    return getDrawable().getPadding(padding);
                }
            };
        }
        super.setBackgroundDrawable(destDrawable);
        this.mLastBackgroundInsets = this.mBackgroundInsets;
        this.mLastBackgroundBlurDrawable = this.mBackgroundBlurDrawable;
        this.mLastOriginalBackgroundDrawable = this.mOriginalBackgroundDrawable;
    }

    private void updateBackgroundBlurCorners() {
        if (this.mBackgroundBlurDrawable == null) {
            return;
        }
        float cornerRadius = 0.0f;
        if (this.mBackgroundBlurRadius != 0 && this.mOriginalBackgroundDrawable != null) {
            Outline outline = new Outline();
            this.mOriginalBackgroundDrawable.getOutline(outline);
            cornerRadius = outline.mMode == 1 ? outline.getRadius() : 0.0f;
        }
        this.mBackgroundBlurDrawable.setCornerRadius(cornerRadius);
    }

    private void updateBackgroundBlurRadius() {
        if (getViewRootImpl() == null) {
            return;
        }
        int i = (!this.mCrossWindowBlurEnabled || !this.mWindow.isTranslucent()) ? 0 : this.mOriginalBackgroundBlurRadius;
        this.mBackgroundBlurRadius = i;
        if (this.mBackgroundBlurDrawable == null && i > 0) {
            this.mBackgroundBlurDrawable = getViewRootImpl().createBackgroundBlurDrawable();
            updateBackgroundDrawable();
        }
        BackgroundBlurDrawable backgroundBlurDrawable = this.mBackgroundBlurDrawable;
        if (backgroundBlurDrawable != null) {
            backgroundBlurDrawable.setBlurRadius(this.mBackgroundBlurRadius);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBackgroundBlurRadius(int blurRadius) {
        this.mOriginalBackgroundBlurRadius = blurRadius;
        if (blurRadius > 0) {
            if (this.mCrossWindowBlurEnabledListener == null) {
                this.mCrossWindowBlurEnabledListener = new Consumer() { // from class: com.android.internal.policy.DecorView$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        DecorView.this.lambda$setBackgroundBlurRadius$1$DecorView((Boolean) obj);
                    }
                };
                ((WindowManager) getContext().getSystemService(WindowManager.class)).addCrossWindowBlurEnabledListener(this.mCrossWindowBlurEnabledListener);
                getViewTreeObserver().addOnPreDrawListener(this.mBackgroundBlurOnPreDrawListener);
                return;
            }
            updateBackgroundBlurRadius();
        } else if (this.mCrossWindowBlurEnabledListener != null) {
            updateBackgroundBlurRadius();
            removeBackgroundBlurDrawable();
        }
    }

    public /* synthetic */ void lambda$setBackgroundBlurRadius$1$DecorView(Boolean enabled) {
        this.mCrossWindowBlurEnabled = enabled.booleanValue();
        updateBackgroundBlurRadius();
    }

    void removeBackgroundBlurDrawable() {
        if (this.mCrossWindowBlurEnabledListener != null) {
            ((WindowManager) getContext().getSystemService(WindowManager.class)).removeCrossWindowBlurEnabledListener(this.mCrossWindowBlurEnabledListener);
            this.mCrossWindowBlurEnabledListener = null;
        }
        getViewTreeObserver().removeOnPreDrawListener(this.mBackgroundBlurOnPreDrawListener);
        this.mBackgroundBlurDrawable = null;
        updateBackgroundDrawable();
    }

    @Override // android.view.View
    public Drawable getBackground() {
        return this.mOriginalBackgroundDrawable;
    }

    private int calculateStatusBarColor(int appearance) {
        return calculateBarColor(this.mWindow.getAttributes().flags, 67108864, this.mSemiTransparentBarColor, this.mWindow.mStatusBarColor, appearance, 8, this.mWindow.mEnsureStatusBarContrastWhenTransparent);
    }

    private int calculateNavigationBarColor(int appearance) {
        return calculateBarColor(this.mWindow.getAttributes().flags, 134217728, this.mSemiTransparentBarColor, this.mWindow.mNavigationBarColor, appearance, 16, this.mWindow.mEnsureNavigationBarContrastWhenTransparent && getContext().getResources().getBoolean(R.bool.config_navBarNeedsScrim));
    }

    public static int calculateBarColor(int flags, int translucentFlag, int semiTransparentBarColor, int barColor, int appearance, int lightAppearanceFlag, boolean scrimTransparent) {
        if ((flags & translucentFlag) != 0) {
            return semiTransparentBarColor;
        }
        if ((Integer.MIN_VALUE & flags) == 0) {
            return -16777216;
        }
        if (scrimTransparent && Color.alpha(barColor) == 0) {
            boolean light = (appearance & lightAppearanceFlag) != 0;
            return light ? SCRIM_LIGHT : semiTransparentBarColor;
        }
        return barColor;
    }

    private int getCurrentColor(ColorViewState state) {
        if (state.visible) {
            return state.color;
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x00f4, code lost:
        if (r6.leftMargin != r13) goto L71;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void updateColorViewInt(final ColorViewState state, int color, int dividerColor, int size, boolean verticalBar, boolean seascape, int sideMargin, boolean animate, boolean force, WindowInsetsController controller) {
        int resolvedGravity;
        int vis;
        boolean visibilityChanged;
        int leftMargin;
        state.present = state.attributes.isPresent(controller.isRequestedVisible(state.attributes.insetsType), this.mWindow.getAttributes().flags, force);
        boolean show = state.attributes.isVisible(state.present, color, this.mWindow.getAttributes().flags, force);
        boolean showView = show && !isResizing() && !this.mHasCaption && size > 0;
        boolean visibilityChanged2 = false;
        View view = state.view;
        int resolvedWidth = -1;
        int resolvedHeight = verticalBar ? -1 : size;
        if (verticalBar) {
            resolvedWidth = size;
        }
        if (verticalBar) {
            ColorViewAttributes colorViewAttributes = state.attributes;
            resolvedGravity = seascape ? colorViewAttributes.seascapeGravity : colorViewAttributes.horizontalGravity;
        } else {
            resolvedGravity = state.attributes.verticalGravity;
        }
        if (view == null) {
            if (showView) {
                View view2 = new View(this.mContext);
                view = view2;
                state.view = view2;
                setColor(view, color, dividerColor, verticalBar, seascape);
                view.setTransitionName(state.attributes.transitionName);
                view.setId(state.attributes.id);
                visibilityChanged2 = true;
                view.setVisibility(4);
                state.targetVisibility = 0;
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(resolvedWidth, resolvedHeight, resolvedGravity);
                if (seascape) {
                    lp.leftMargin = sideMargin;
                } else {
                    lp.rightMargin = sideMargin;
                }
                addView(view, lp);
                updateColorViewTranslations();
            }
        } else {
            int vis2 = showView ? 0 : 4;
            boolean visibilityChanged3 = state.targetVisibility != vis2;
            state.targetVisibility = vis2;
            FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) view.getLayoutParams();
            int rightMargin = seascape ? 0 : sideMargin;
            int leftMargin2 = seascape ? sideMargin : 0;
            if (lp2.height == resolvedHeight && lp2.width == resolvedWidth && lp2.gravity == resolvedGravity) {
                vis = rightMargin;
                if (lp2.rightMargin == vis) {
                    visibilityChanged = visibilityChanged3;
                    leftMargin = leftMargin2;
                } else {
                    visibilityChanged = visibilityChanged3;
                    leftMargin = leftMargin2;
                }
            } else {
                vis = rightMargin;
                visibilityChanged = visibilityChanged3;
                leftMargin = leftMargin2;
            }
            lp2.height = resolvedHeight;
            lp2.width = resolvedWidth;
            lp2.gravity = resolvedGravity;
            lp2.rightMargin = vis;
            lp2.leftMargin = leftMargin;
            view.setLayoutParams(lp2);
            if (showView) {
                setColor(view, color, dividerColor, verticalBar, seascape);
            }
            visibilityChanged2 = visibilityChanged;
        }
        if (visibilityChanged2) {
            view.animate().cancel();
            if (!animate || isResizing()) {
                int i = 0;
                view.setAlpha(1.0f);
                if (!showView) {
                    i = 4;
                }
                view.setVisibility(i);
            } else if (showView) {
                if (view.getVisibility() != 0) {
                    view.setVisibility(0);
                    view.setAlpha(0.0f);
                }
                view.animate().alpha(1.0f).setInterpolator(this.mShowInterpolator).setDuration(this.mBarEnterExitDuration);
            } else {
                view.animate().alpha(0.0f).setInterpolator(this.mHideInterpolator).setDuration(this.mBarEnterExitDuration).withEndAction(new Runnable() { // from class: com.android.internal.policy.DecorView.3
                    @Override // java.lang.Runnable
                    public void run() {
                        state.view.setAlpha(1.0f);
                        state.view.setVisibility(4);
                    }
                });
            }
        }
        state.visible = show;
        state.color = color;
    }

    private static void setColor(View v, int color, int dividerColor, boolean verticalBar, boolean seascape) {
        if (dividerColor != 0) {
            Pair<Boolean, Boolean> dir = (Pair) v.getTag();
            if (dir == null || ((Boolean) dir.first).booleanValue() != verticalBar || ((Boolean) dir.second).booleanValue() != seascape) {
                int size = Math.round(TypedValue.applyDimension(1, 1.0f, v.getContext().getResources().getDisplayMetrics()));
                v.setBackground(new LayerDrawable(new Drawable[]{new ColorDrawable(dividerColor), new InsetDrawable((Drawable) new ColorDrawable(color), (!verticalBar || seascape) ? 0 : size, !verticalBar ? size : 0, (!verticalBar || !seascape) ? 0 : size, 0)}));
                v.setTag(new Pair(Boolean.valueOf(verticalBar), Boolean.valueOf(seascape)));
                return;
            }
            LayerDrawable d = (LayerDrawable) v.getBackground();
            InsetDrawable inset = (InsetDrawable) d.getDrawable(1);
            ((ColorDrawable) inset.getDrawable()).setColor(color);
            ((ColorDrawable) d.getDrawable(0)).setColor(dividerColor);
            return;
        }
        v.setTag(null);
        v.setBackgroundColor(color);
    }

    private void updateColorViewTranslations() {
        int rootScrollY = this.mRootScrollY;
        float f = 0.0f;
        if (this.mStatusColorViewState.view != null) {
            this.mStatusColorViewState.view.setTranslationY(rootScrollY > 0 ? rootScrollY : 0.0f);
        }
        if (this.mNavigationColorViewState.view != null) {
            View view = this.mNavigationColorViewState.view;
            if (rootScrollY < 0) {
                f = rootScrollY;
            }
            view.setTranslationY(f);
        }
    }

    private WindowInsets updateStatusGuard(WindowInsets insets) {
        boolean showStatusGuard;
        int i;
        WindowInsets insets2 = insets;
        ActionBarContextView actionBarContextView = this.mPrimaryActionModeView;
        if (actionBarContextView == null) {
            showStatusGuard = false;
            i = 0;
        } else if (!(actionBarContextView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams)) {
            showStatusGuard = false;
            i = 0;
        } else {
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) this.mPrimaryActionModeView.getLayoutParams();
            boolean mlpChanged = false;
            if (this.mPrimaryActionModeView.isShown()) {
                if (this.mTempRect == null) {
                    this.mTempRect = new Rect();
                }
                Rect rect = this.mTempRect;
                WindowInsets innerInsets = this.mWindow.mContentParent.computeSystemWindowInsets(insets2, rect);
                int newTopMargin = innerInsets.getSystemWindowInsetTop();
                int newLeftMargin = innerInsets.getSystemWindowInsetLeft();
                int newRightMargin = innerInsets.getSystemWindowInsetRight();
                WindowInsets rootInsets = getRootWindowInsets();
                int newGuardLeftMargin = rootInsets.getSystemWindowInsetLeft();
                int newGuardRightMargin = rootInsets.getSystemWindowInsetRight();
                if (mlp.topMargin != newTopMargin || mlp.leftMargin != newLeftMargin || mlp.rightMargin != newRightMargin) {
                    mlpChanged = true;
                    mlp.topMargin = newTopMargin;
                    mlp.leftMargin = newLeftMargin;
                    mlp.rightMargin = newRightMargin;
                }
                if (newTopMargin > 0 && this.mStatusGuard == null) {
                    View view = new View(this.mContext);
                    this.mStatusGuard = view;
                    view.setVisibility(8);
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-1, mlp.topMargin, 51);
                    lp.leftMargin = newGuardLeftMargin;
                    lp.rightMargin = newGuardRightMargin;
                    addView(this.mStatusGuard, indexOfChild(this.mStatusColorViewState.view), lp);
                } else {
                    View view2 = this.mStatusGuard;
                    if (view2 != null) {
                        FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) view2.getLayoutParams();
                        if (lp2.height != mlp.topMargin || lp2.leftMargin != newGuardLeftMargin || lp2.rightMargin != newGuardRightMargin) {
                            lp2.height = mlp.topMargin;
                            lp2.leftMargin = newGuardLeftMargin;
                            lp2.rightMargin = newGuardRightMargin;
                            this.mStatusGuard.setLayoutParams(lp2);
                        }
                    }
                }
                View view3 = this.mStatusGuard;
                boolean z = true;
                boolean showStatusGuard2 = view3 != null;
                if (showStatusGuard2 && view3.getVisibility() != 0) {
                    updateStatusGuardColor();
                }
                if ((this.mWindow.getLocalFeaturesPrivate() & 1024) != 0) {
                    z = false;
                }
                boolean nonOverlay = z;
                if (nonOverlay && showStatusGuard2) {
                    insets2 = insets2.inset(0, insets.getSystemWindowInsetTop(), 0, 0);
                }
                showStatusGuard = showStatusGuard2;
                i = 0;
            } else {
                showStatusGuard = false;
                if (mlp.topMargin == 0 && mlp.leftMargin == 0 && mlp.rightMargin == 0) {
                    i = 0;
                } else {
                    mlpChanged = true;
                    i = 0;
                    mlp.topMargin = 0;
                }
            }
            if (mlpChanged) {
                this.mPrimaryActionModeView.setLayoutParams(mlp);
            }
        }
        View view4 = this.mStatusGuard;
        if (view4 != null) {
            view4.setVisibility(showStatusGuard ? i : 8);
        }
        return insets2;
    }

    private void updateStatusGuardColor() {
        int color;
        boolean lightStatusBar = (getWindowSystemUiVisibility() & 8192) != 0;
        View view = this.mStatusGuard;
        if (lightStatusBar) {
            color = this.mContext.getColor(R.color.decor_view_status_guard_light);
        } else {
            color = this.mContext.getColor(R.color.decor_view_status_guard);
        }
        view.setBackgroundColor(color);
    }

    public void updatePictureInPictureOutlineProvider(boolean isInPictureInPictureMode) {
        if (this.mIsInPictureInPictureMode == isInPictureInPictureMode) {
            return;
        }
        if (isInPictureInPictureMode) {
            Window.WindowControllerCallback callback = this.mWindow.getWindowControllerCallback();
            if (callback != null && callback.isTaskRoot()) {
                super.setOutlineProvider(PIP_OUTLINE_PROVIDER);
            }
        } else {
            ViewOutlineProvider outlineProvider = getOutlineProvider();
            ViewOutlineProvider viewOutlineProvider = this.mLastOutlineProvider;
            if (outlineProvider != viewOutlineProvider) {
                setOutlineProvider(viewOutlineProvider);
            }
        }
        this.mIsInPictureInPictureMode = isInPictureInPictureMode;
    }

    @Override // android.view.View
    public void setOutlineProvider(ViewOutlineProvider provider) {
        super.setOutlineProvider(provider);
        this.mLastOutlineProvider = provider;
    }

    private void drawableChanged() {
        if (this.mChanging) {
            return;
        }
        Rect framePadding = this.mFramePadding;
        if (framePadding == null) {
            framePadding = new Rect();
        }
        Rect backgroundPadding = this.mBackgroundPadding;
        if (backgroundPadding == null) {
            backgroundPadding = new Rect();
        }
        setPadding(framePadding.left + backgroundPadding.left, framePadding.top + backgroundPadding.top, framePadding.right + backgroundPadding.right, framePadding.bottom + backgroundPadding.bottom);
        requestLayout();
        invalidate();
        int opacity = -1;
        WindowConfiguration winConfig = getResources().getConfiguration().windowConfiguration;
        boolean renderShadowsInCompositor = this.mWindow.mRenderShadowsInCompositor;
        if (winConfig.hasWindowShadow() && !renderShadowsInCompositor) {
            opacity = -3;
        } else {
            Drawable bg = getBackground();
            Drawable fg = getForeground();
            if (bg != null) {
                if (fg == null) {
                    opacity = bg.getOpacity();
                } else if (framePadding.left <= 0 && framePadding.top <= 0 && framePadding.right <= 0 && framePadding.bottom <= 0) {
                    int fop = fg.getOpacity();
                    int bop = bg.getOpacity();
                    if (fop == -1 || bop == -1) {
                        opacity = -1;
                    } else if (fop == 0) {
                        opacity = bop;
                    } else if (bop == 0) {
                        opacity = fop;
                    } else {
                        opacity = Drawable.resolveOpacity(fop, bop);
                    }
                } else {
                    opacity = -3;
                }
            }
        }
        this.mDefaultOpacity = opacity;
        if (this.mFeatureId < 0) {
            this.mWindow.setDefaultWindowFormat(opacity);
        }
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (this.mWindow.hasFeature(0) && !hasWindowFocus && this.mWindow.mPanelChordingKey != 0) {
            this.mWindow.closePanel(0);
        }
        Window.Callback cb = this.mWindow.getCallback();
        if (cb != null && !this.mWindow.isDestroyed() && this.mFeatureId < 0) {
            cb.onWindowFocusChanged(hasWindowFocus);
        }
        ActionMode actionMode = this.mPrimaryActionMode;
        if (actionMode != null) {
            actionMode.onWindowFocusChanged(hasWindowFocus);
        }
        ActionMode actionMode2 = this.mFloatingActionMode;
        if (actionMode2 != null) {
            actionMode2.onWindowFocusChanged(hasWindowFocus);
        }
        updateElevation();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window.Callback cb = this.mWindow.getCallback();
        if (cb != null && !this.mWindow.isDestroyed() && this.mFeatureId < 0) {
            cb.onAttachedToWindow();
        }
        if (this.mFeatureId == -1) {
            this.mWindow.openPanelsAfterRestore();
        }
        if (!this.mWindowResizeCallbacksAdded) {
            getViewRootImpl().addWindowCallbacks(this);
            this.mWindowResizeCallbacksAdded = true;
        } else {
            BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
            if (backdropFrameRenderer != null) {
                backdropFrameRenderer.onConfigurationChange();
            }
        }
        updateBackgroundBlurRadius();
        this.mWindow.onViewRootImplSet(getViewRootImpl());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Window.Callback cb = this.mWindow.getCallback();
        if (cb != null && this.mFeatureId < 0) {
            cb.onDetachedFromWindow();
        }
        if (this.mWindow.mDecorContentParent != null) {
            this.mWindow.mDecorContentParent.dismissPopups();
        }
        if (this.mPrimaryActionModePopup != null) {
            removeCallbacks(this.mShowPrimaryActionModePopup);
            if (this.mPrimaryActionModePopup.isShowing()) {
                this.mPrimaryActionModePopup.dismiss();
            }
            this.mPrimaryActionModePopup = null;
        }
        FloatingToolbar floatingToolbar = this.mFloatingToolbar;
        if (floatingToolbar != null) {
            floatingToolbar.dismiss();
            this.mFloatingToolbar = null;
        }
        removeBackgroundBlurDrawable();
        PhoneWindow.PanelFeatureState st = this.mWindow.getPanelState(0, false);
        if (st != null && st.menu != null && this.mFeatureId < 0) {
            st.menu.close();
        }
        releaseThreadedRenderer();
        if (this.mWindowResizeCallbacksAdded) {
            getViewRootImpl().removeWindowCallbacks(this);
            this.mWindowResizeCallbacksAdded = false;
        }
        this.mPendingInsetsController.detach();
    }

    @Override // android.view.View
    public void onCloseSystemDialogs(String reason) {
        if (this.mFeatureId >= 0) {
            this.mWindow.closeAllPanels();
        }
    }

    @Override // com.android.internal.view.RootViewSurfaceTaker
    public SurfaceHolder.Callback2 willYouTakeTheSurface() {
        if (this.mFeatureId < 0) {
            return this.mWindow.mTakeSurfaceCallback;
        }
        return null;
    }

    @Override // com.android.internal.view.RootViewSurfaceTaker
    public InputQueue.Callback willYouTakeTheInputQueue() {
        if (this.mFeatureId < 0) {
            return this.mWindow.mTakeInputQueueCallback;
        }
        return null;
    }

    @Override // com.android.internal.view.RootViewSurfaceTaker
    public void setSurfaceType(int type) {
        this.mWindow.setType(type);
    }

    @Override // com.android.internal.view.RootViewSurfaceTaker
    public void setSurfaceFormat(int format) {
        this.mWindow.setFormat(format);
    }

    @Override // com.android.internal.view.RootViewSurfaceTaker
    public void setSurfaceKeepScreenOn(boolean keepOn) {
        if (keepOn) {
            this.mWindow.addFlags(128);
        } else {
            this.mWindow.clearFlags(128);
        }
    }

    @Override // com.android.internal.view.RootViewSurfaceTaker
    public void onRootViewScrollYChanged(int rootScrollY) {
        this.mRootScrollY = rootScrollY;
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        if (decorCaptionView != null) {
            decorCaptionView.onRootViewScrollYChanged(rootScrollY);
        }
        updateColorViewTranslations();
    }

    @Override // com.android.internal.view.RootViewSurfaceTaker
    public PendingInsetsController providePendingInsetsController() {
        return this.mPendingInsetsController;
    }

    private ActionMode createActionMode(int type, ActionMode.Callback2 callback, View originatingView) {
        switch (type) {
            case 1:
                return createFloatingActionMode(originatingView, callback);
            default:
                return createStandaloneActionMode(callback);
        }
    }

    private void setHandledActionMode(ActionMode mode) {
        if (mode.getType() == 0) {
            setHandledPrimaryActionMode(mode);
        } else if (mode.getType() == 1) {
            setHandledFloatingActionMode(mode);
        }
    }

    private ActionMode createStandaloneActionMode(ActionMode.Callback callback) {
        Context actionBarContext;
        endOnGoingFadeAnimation();
        cleanupPrimaryActionMode();
        ActionBarContextView actionBarContextView = this.mPrimaryActionModeView;
        boolean z = false;
        if (actionBarContextView == null || !actionBarContextView.isAttachedToWindow()) {
            if (this.mWindow.isFloating()) {
                TypedValue outValue = new TypedValue();
                Resources.Theme baseTheme = this.mContext.getTheme();
                baseTheme.resolveAttribute(16843825, outValue, true);
                if (outValue.resourceId != 0) {
                    Resources.Theme actionBarTheme = this.mContext.getResources().newTheme();
                    actionBarTheme.setTo(baseTheme);
                    actionBarTheme.applyStyle(outValue.resourceId, true);
                    actionBarContext = new ContextThemeWrapper(this.mContext, 0);
                    actionBarContext.getTheme().setTo(actionBarTheme);
                } else {
                    actionBarContext = this.mContext;
                }
                this.mPrimaryActionModeView = new ActionBarContextView(actionBarContext);
                PopupWindow popupWindow = new PopupWindow(actionBarContext, (AttributeSet) null, (int) R.attr.actionModePopupWindowStyle);
                this.mPrimaryActionModePopup = popupWindow;
                popupWindow.setWindowLayoutType(2);
                this.mPrimaryActionModePopup.setContentView(this.mPrimaryActionModeView);
                this.mPrimaryActionModePopup.setWidth(-1);
                actionBarContext.getTheme().resolveAttribute(16843499, outValue, true);
                int height = TypedValue.complexToDimensionPixelSize(outValue.data, actionBarContext.getResources().getDisplayMetrics());
                this.mPrimaryActionModeView.setContentHeight(height);
                this.mPrimaryActionModePopup.setHeight(-2);
                this.mShowPrimaryActionModePopup = new Runnable() { // from class: com.android.internal.policy.DecorView.4
                    @Override // java.lang.Runnable
                    public void run() {
                        DecorView.this.mPrimaryActionModePopup.showAtLocation(DecorView.this.mPrimaryActionModeView.getApplicationWindowToken(), 55, 0, 0);
                        DecorView.this.endOnGoingFadeAnimation();
                        if (!DecorView.this.shouldAnimatePrimaryActionModeView()) {
                            DecorView.this.mPrimaryActionModeView.setAlpha(1.0f);
                            DecorView.this.mPrimaryActionModeView.setVisibility(0);
                            return;
                        }
                        DecorView decorView = DecorView.this;
                        decorView.mFadeAnim = ObjectAnimator.ofFloat(decorView.mPrimaryActionModeView, (Property<ActionBarContextView, Float>) View.ALPHA, 0.0f, 1.0f);
                        DecorView.this.mFadeAnim.addListener(new AnimatorListenerAdapter() { // from class: com.android.internal.policy.DecorView.4.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationStart(Animator animation) {
                                DecorView.this.mPrimaryActionModeView.setVisibility(0);
                            }

                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animation) {
                                DecorView.this.mPrimaryActionModeView.setAlpha(1.0f);
                                DecorView.this.mFadeAnim = null;
                            }
                        });
                        DecorView.this.mFadeAnim.start();
                    }
                };
            } else {
                ViewStub stub = (ViewStub) findViewById(R.id.action_mode_bar_stub);
                if (stub != null) {
                    this.mPrimaryActionModeView = (ActionBarContextView) stub.inflate();
                    this.mPrimaryActionModePopup = null;
                }
            }
        }
        ActionBarContextView actionBarContextView2 = this.mPrimaryActionModeView;
        if (actionBarContextView2 != null) {
            actionBarContextView2.killMode();
            Context context = this.mPrimaryActionModeView.getContext();
            ActionBarContextView actionBarContextView3 = this.mPrimaryActionModeView;
            if (this.mPrimaryActionModePopup == null) {
                z = true;
            }
            ActionMode mode = new StandaloneActionMode(context, actionBarContextView3, callback, z);
            return mode;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void endOnGoingFadeAnimation() {
        ObjectAnimator objectAnimator = this.mFadeAnim;
        if (objectAnimator != null) {
            objectAnimator.end();
        }
    }

    private void setHandledPrimaryActionMode(ActionMode mode) {
        endOnGoingFadeAnimation();
        this.mPrimaryActionMode = mode;
        mode.invalidate();
        this.mPrimaryActionModeView.initForMode(this.mPrimaryActionMode);
        if (this.mPrimaryActionModePopup != null) {
            post(this.mShowPrimaryActionModePopup);
        } else if (shouldAnimatePrimaryActionModeView()) {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mPrimaryActionModeView, (Property<ActionBarContextView, Float>) View.ALPHA, 0.0f, 1.0f);
            this.mFadeAnim = ofFloat;
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.internal.policy.DecorView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animation) {
                    DecorView.this.mPrimaryActionModeView.setVisibility(0);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animation) {
                    DecorView.this.mPrimaryActionModeView.setAlpha(1.0f);
                    DecorView.this.mFadeAnim = null;
                }
            });
            this.mFadeAnim.start();
        } else {
            this.mPrimaryActionModeView.setAlpha(1.0f);
            this.mPrimaryActionModeView.setVisibility(0);
        }
        this.mPrimaryActionModeView.sendAccessibilityEvent(32);
    }

    boolean shouldAnimatePrimaryActionModeView() {
        return isLaidOut();
    }

    private ActionMode createFloatingActionMode(View originatingView, ActionMode.Callback2 callback) {
        ActionMode actionMode = this.mFloatingActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        cleanupFloatingActionModeViews();
        this.mFloatingToolbar = new FloatingToolbar(this.mWindow);
        final FloatingActionMode mode = new FloatingActionMode(this.mContext, callback, originatingView, this.mFloatingToolbar);
        this.mFloatingActionModeOriginatingView = originatingView;
        this.mFloatingToolbarPreDrawListener = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.internal.policy.DecorView.6
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                mode.updateViewLocationInWindow();
                return true;
            }
        };
        return mode;
    }

    private void setHandledFloatingActionMode(ActionMode mode) {
        this.mFloatingActionMode = mode;
        mode.invalidate();
        this.mFloatingActionModeOriginatingView.getViewTreeObserver().addOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
    }

    void enableCaption(boolean attachedAndVisible) {
        if (this.mHasCaption != attachedAndVisible) {
            this.mHasCaption = attachedAndVisible;
            if (getForeground() != null) {
                drawableChanged();
            }
            notifyCaptionHeightChanged();
        }
    }

    public void notifyCaptionHeightChanged() {
        getWindowInsetsController().setCaptionInsetsHeight(getCaptionInsetsHeight());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setWindow(PhoneWindow phoneWindow) {
        this.mWindow = phoneWindow;
        Context context = getContext();
        if (context instanceof DecorContext) {
            DecorContext decorContext = (DecorContext) context;
            decorContext.setPhoneWindow(this.mWindow);
        }
        if (this.mPendingWindowBackground != null) {
            Drawable background = this.mPendingWindowBackground;
            this.mPendingWindowBackground = null;
            setWindowBackground(background);
        }
    }

    @Override // android.view.View
    public Resources getResources() {
        return getContext().getResources();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateDecorCaptionStatus(newConfig);
        updateAvailableWidth();
        initializeElevation();
    }

    @Override // android.view.View
    public void onMovedToDisplay(int displayId, Configuration config) {
        super.onMovedToDisplay(displayId, config);
        getContext().updateDisplay(displayId);
    }

    private boolean isFillingScreen(Configuration config) {
        boolean isFullscreen = config.windowConfiguration.getWindowingMode() == 1;
        return isFullscreen && ((getWindowSystemUiVisibility() | getSystemUiVisibility()) & 4) != 0;
    }

    private void updateDecorCaptionStatus(Configuration config) {
        boolean displayWindowDecor = config.windowConfiguration.hasWindowDecorCaption() && !isFillingScreen(config);
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        if (decorCaptionView == null && displayWindowDecor) {
            LayoutInflater inflater = this.mWindow.getLayoutInflater();
            DecorCaptionView createDecorCaptionView = createDecorCaptionView(inflater);
            this.mDecorCaptionView = createDecorCaptionView;
            if (createDecorCaptionView != null) {
                if (createDecorCaptionView.getParent() == null) {
                    addView(this.mDecorCaptionView, 0, new ViewGroup.LayoutParams(-1, -1));
                }
                removeView(this.mContentRoot);
                this.mDecorCaptionView.addView(this.mContentRoot, new ViewGroup.MarginLayoutParams(-1, -1));
            }
        } else if (decorCaptionView != null) {
            decorCaptionView.onConfigurationChanged(displayWindowDecor);
            enableCaption(displayWindowDecor);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onResourcesLoaded(LayoutInflater inflater, int layoutResource) {
        if (this.mBackdropFrameRenderer != null) {
            loadBackgroundDrawablesIfNeeded();
            this.mBackdropFrameRenderer.onResourcesLoaded(this, this.mResizingBackgroundDrawable, this.mCaptionBackgroundDrawable, this.mUserCaptionBackgroundDrawable, getCurrentColor(this.mStatusColorViewState), getCurrentColor(this.mNavigationColorViewState));
        }
        this.mDecorCaptionView = createDecorCaptionView(inflater);
        View root = inflater.inflate(layoutResource, (ViewGroup) null);
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        if (decorCaptionView != null) {
            if (decorCaptionView.getParent() == null) {
                addView(this.mDecorCaptionView, new ViewGroup.LayoutParams(-1, -1));
            }
            this.mDecorCaptionView.addView(root, new ViewGroup.MarginLayoutParams(-1, -1));
        } else {
            addView(root, 0, new ViewGroup.LayoutParams(-1, -1));
        }
        this.mContentRoot = (ViewGroup) root;
        initializeElevation();
    }

    private void loadBackgroundDrawablesIfNeeded() {
        if (this.mResizingBackgroundDrawable == null) {
            Drawable resizingBackgroundDrawable = getResizingBackgroundDrawable(this.mWindow.mBackgroundDrawable, this.mWindow.mBackgroundFallbackDrawable, this.mWindow.isTranslucent() || this.mWindow.isShowingWallpaper());
            this.mResizingBackgroundDrawable = resizingBackgroundDrawable;
            if (resizingBackgroundDrawable == null) {
                String str = this.mLogTag;
                Log.w(str, "Failed to find background drawable for PhoneWindow=" + this.mWindow);
            }
        }
        if (this.mCaptionBackgroundDrawable == null) {
            this.mCaptionBackgroundDrawable = getContext().getDrawable(R.drawable.decor_caption_title_focused);
        }
        Drawable drawable = this.mResizingBackgroundDrawable;
        if (drawable != null) {
            this.mLastBackgroundDrawableCb = drawable.getCallback();
            this.mResizingBackgroundDrawable.setCallback(null);
        }
    }

    private DecorCaptionView createDecorCaptionView(LayoutInflater inflater) {
        DecorCaptionView decorCaptionView = null;
        boolean z = true;
        for (int i = getChildCount() - 1; i >= 0 && decorCaptionView == null; i--) {
            View view = getChildAt(i);
            if (view instanceof DecorCaptionView) {
                decorCaptionView = (DecorCaptionView) view;
                removeViewAt(i);
            }
        }
        WindowManager.LayoutParams attrs = this.mWindow.getAttributes();
        boolean isApplication = attrs.type == 1 || attrs.type == 2 || attrs.type == 4;
        WindowConfiguration winConfig = getResources().getConfiguration().windowConfiguration;
        if (!this.mWindow.isFloating() && isApplication && winConfig.hasWindowDecorCaption()) {
            if (decorCaptionView == null) {
                decorCaptionView = inflateDecorCaptionView(inflater);
            }
            decorCaptionView.setPhoneWindow(this.mWindow, true);
        } else {
            decorCaptionView = null;
        }
        if (decorCaptionView == null) {
            z = false;
        }
        enableCaption(z);
        return decorCaptionView;
    }

    private DecorCaptionView inflateDecorCaptionView(LayoutInflater inflater) {
        Context context = getContext();
        LayoutInflater inflater2 = LayoutInflater.from(context);
        DecorCaptionView view = (DecorCaptionView) inflater2.inflate(R.layout.decor_caption, (ViewGroup) null);
        setDecorCaptionShade(view);
        return view;
    }

    private void setDecorCaptionShade(DecorCaptionView view) {
        int shade = this.mWindow.getDecorCaptionShade();
        switch (shade) {
            case 1:
                setLightDecorCaptionShade(view);
                return;
            case 2:
                setDarkDecorCaptionShade(view);
                return;
            default:
                if ((getWindowSystemUiVisibility() & 8192) != 0) {
                    setDarkDecorCaptionShade(view);
                    return;
                } else {
                    setLightDecorCaptionShade(view);
                    return;
                }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateDecorCaptionShade() {
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        if (decorCaptionView != null) {
            setDecorCaptionShade(decorCaptionView);
        }
    }

    private void setLightDecorCaptionShade(DecorCaptionView view) {
        view.findViewById(R.id.maximize_window).setBackgroundResource(R.drawable.decor_maximize_button_light);
        view.findViewById(R.id.close_window).setBackgroundResource(R.drawable.decor_close_button_light);
    }

    private void setDarkDecorCaptionShade(DecorCaptionView view) {
        view.findViewById(R.id.maximize_window).setBackgroundResource(R.drawable.decor_maximize_button_dark);
        view.findViewById(R.id.close_window).setBackgroundResource(R.drawable.decor_close_button_dark);
    }

    public static Drawable getResizingBackgroundDrawable(Drawable backgroundDrawable, Drawable fallbackDrawable, boolean windowTranslucent) {
        if (backgroundDrawable != null) {
            return enforceNonTranslucentBackground(backgroundDrawable, windowTranslucent);
        }
        if (fallbackDrawable != null) {
            return enforceNonTranslucentBackground(fallbackDrawable, windowTranslucent);
        }
        return new ColorDrawable(-16777216);
    }

    private static Drawable enforceNonTranslucentBackground(Drawable drawable, boolean windowTranslucent) {
        if (!windowTranslucent && (drawable instanceof ColorDrawable)) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            int color = colorDrawable.getColor();
            if (Color.alpha(color) != 255) {
                ColorDrawable copy = (ColorDrawable) colorDrawable.getConstantState().newDrawable().mo3113mutate();
                copy.setColor(Color.argb(255, Color.red(color), Color.green(color), Color.blue(color)));
                return copy;
            }
        }
        return drawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clearContentView() {
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        if (decorCaptionView != null) {
            decorCaptionView.removeContentView();
            return;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View v = getChildAt(i);
            if (v != this.mStatusColorViewState.view && v != this.mNavigationColorViewState.view && v != this.mStatusGuard) {
                removeViewAt(i);
            }
        }
    }

    @Override // android.view.WindowCallbacks
    public void onWindowSizeIsChanging(Rect newBounds, boolean fullscreen, Rect systemInsets, Rect stableInsets) {
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer != null) {
            backdropFrameRenderer.setTargetRect(newBounds, fullscreen, systemInsets);
        }
    }

    @Override // android.view.WindowCallbacks
    public void onWindowDragResizeStart(Rect initialBounds, boolean fullscreen, Rect systemInsets, Rect stableInsets, int resizeMode) {
        if (this.mWindow.isDestroyed()) {
            releaseThreadedRenderer();
        } else if (this.mBackdropFrameRenderer != null) {
        } else {
            ThreadedRenderer renderer = getThreadedRenderer();
            if (renderer != null) {
                loadBackgroundDrawablesIfNeeded();
                WindowInsets rootInsets = getRootWindowInsets();
                this.mBackdropFrameRenderer = new BackdropFrameRenderer(this, renderer, initialBounds, this.mResizingBackgroundDrawable, this.mCaptionBackgroundDrawable, this.mUserCaptionBackgroundDrawable, getCurrentColor(this.mStatusColorViewState), getCurrentColor(this.mNavigationColorViewState), fullscreen, rootInsets.getInsets(WindowInsets.Type.systemBars()));
                updateElevation();
                updateColorViews(null, false);
            }
            this.mResizeMode = resizeMode;
            getViewRootImpl().requestInvalidateRootRenderNode();
        }
    }

    @Override // android.view.WindowCallbacks
    public void onWindowDragResizeEnd() {
        releaseThreadedRenderer();
        updateColorViews(null, false);
        this.mResizeMode = -1;
        getViewRootImpl().requestInvalidateRootRenderNode();
    }

    @Override // android.view.WindowCallbacks
    public boolean onContentDrawn(int offsetX, int offsetY, int sizeX, int sizeY) {
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer == null) {
            return false;
        }
        return backdropFrameRenderer.onContentDrawn(offsetX, offsetY, sizeX, sizeY);
    }

    @Override // android.view.WindowCallbacks
    public void onRequestDraw(boolean reportNextDraw) {
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer != null) {
            backdropFrameRenderer.onRequestDraw(reportNextDraw);
        } else if (reportNextDraw && isAttachedToWindow()) {
            getViewRootImpl().reportDrawFinish();
        }
    }

    @Override // android.view.WindowCallbacks
    public void onPostDraw(RecordingCanvas canvas) {
        drawResizingShadowIfNeeded(canvas);
        drawLegacyNavigationBarBackground(canvas);
    }

    private void initResizingPaints() {
        int startColor = this.mContext.getResources().getColor(R.color.resize_shadow_start_color, null);
        int endColor = this.mContext.getResources().getColor(R.color.resize_shadow_end_color, null);
        int middleColor = (startColor + endColor) / 2;
        this.mHorizontalResizeShadowPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, this.mResizeShadowSize, new int[]{startColor, middleColor, endColor}, new float[]{0.0f, 0.3f, 1.0f}, Shader.TileMode.CLAMP));
        this.mVerticalResizeShadowPaint.setShader(new LinearGradient(0.0f, 0.0f, this.mResizeShadowSize, 0.0f, new int[]{startColor, middleColor, endColor}, new float[]{0.0f, 0.3f, 1.0f}, Shader.TileMode.CLAMP));
    }

    private void drawResizingShadowIfNeeded(RecordingCanvas canvas) {
        if (this.mResizeMode != 1 || this.mWindow.mIsFloating || this.mWindow.isTranslucent() || this.mWindow.isShowingWallpaper()) {
            return;
        }
        canvas.save();
        canvas.translate(0.0f, getHeight() - this.mFrameOffsets.bottom);
        canvas.drawRect(0.0f, 0.0f, getWidth(), this.mResizeShadowSize, this.mHorizontalResizeShadowPaint);
        canvas.restore();
        canvas.save();
        canvas.translate(getWidth() - this.mFrameOffsets.right, 0.0f);
        canvas.drawRect(0.0f, 0.0f, this.mResizeShadowSize, getHeight(), this.mVerticalResizeShadowPaint);
        canvas.restore();
    }

    private void drawLegacyNavigationBarBackground(RecordingCanvas canvas) {
        View v;
        if (!this.mDrawLegacyNavigationBarBackground || (v = this.mNavigationColorViewState.view) == null) {
            return;
        }
        canvas.drawRect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom(), this.mLegacyNavigationBarBackgroundPaint);
    }

    private void releaseThreadedRenderer() {
        Drawable.Callback callback;
        Drawable drawable = this.mResizingBackgroundDrawable;
        if (drawable != null && (callback = this.mLastBackgroundDrawableCb) != null) {
            drawable.setCallback(callback);
            this.mLastBackgroundDrawableCb = null;
        }
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer != null) {
            backdropFrameRenderer.releaseRenderer();
            this.mBackdropFrameRenderer = null;
            updateElevation();
        }
    }

    private boolean isResizing() {
        return this.mBackdropFrameRenderer != null;
    }

    private void initializeElevation() {
        this.mAllowUpdateElevation = false;
        updateElevation();
    }

    private void updateElevation() {
        int windowingMode = getResources().getConfiguration().windowConfiguration.getWindowingMode();
        boolean renderShadowsInCompositor = this.mWindow.mRenderShadowsInCompositor;
        if (renderShadowsInCompositor) {
            return;
        }
        float elevation = 0.0f;
        boolean wasAdjustedForStack = this.mElevationAdjustedForStack;
        float f = 5.0f;
        if (windowingMode == 5 && !isResizing()) {
            if (hasWindowFocus()) {
                f = 20.0f;
            }
            float elevation2 = f;
            if (!this.mAllowUpdateElevation) {
                elevation2 = 20.0f;
            }
            elevation = dipToPx(elevation2);
            this.mElevationAdjustedForStack = true;
        } else if (windowingMode == 2) {
            elevation = dipToPx(5.0f);
            this.mElevationAdjustedForStack = true;
        } else {
            this.mElevationAdjustedForStack = false;
        }
        if ((wasAdjustedForStack || this.mElevationAdjustedForStack) && getElevation() != elevation) {
            if (!isResizing()) {
                this.mWindow.setElevation(elevation);
            } else {
                setElevation(elevation);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isShowingCaption() {
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        return decorCaptionView != null && decorCaptionView.isCaptionShowing();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getCaptionHeight() {
        if (isShowingCaption()) {
            return this.mDecorCaptionView.getCaptionHeight();
        }
        return 0;
    }

    public int getCaptionInsetsHeight() {
        if (!this.mWindow.isOverlayWithDecorCaptionEnabled()) {
            return 0;
        }
        return getCaptionHeight();
    }

    private float dipToPx(float dip) {
        return TypedValue.applyDimension(1, dip, getResources().getDisplayMetrics());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setUserCaptionBackgroundDrawable(Drawable drawable) {
        this.mUserCaptionBackgroundDrawable = drawable;
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer != null) {
            backdropFrameRenderer.setUserCaptionBackgroundDrawable(drawable);
        }
    }

    private static String getTitleSuffix(WindowManager.LayoutParams params) {
        if (params == null) {
            return "";
        }
        String[] split = params.getTitle().toString().split("\\.");
        if (split.length <= 0) {
            return "";
        }
        return split[split.length - 1];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLogTag(WindowManager.LayoutParams params) {
        this.mLogTag = "DecorView[" + getTitleSuffix(params) + "]";
    }

    private void updateAvailableWidth() {
        Resources res = getResources();
        this.mAvailableWidth = TypedValue.applyDimension(1, res.getConfiguration().screenWidthDp, res.getDisplayMetrics());
    }

    @Override // android.view.View
    public void requestKeyboardShortcuts(List<KeyboardShortcutGroup> list, int deviceId) {
        PhoneWindow.PanelFeatureState st = this.mWindow.getPanelState(0, false);
        Menu menu = st != null ? st.menu : null;
        if (!this.mWindow.isDestroyed() && this.mWindow.getCallback() != null) {
            this.mWindow.getCallback().onProvideKeyboardShortcuts(list, menu, deviceId);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchPointerCaptureChanged(boolean hasCapture) {
        super.dispatchPointerCaptureChanged(hasCapture);
        if (!this.mWindow.isDestroyed() && this.mWindow.getCallback() != null) {
            this.mWindow.getCallback().onPointerCaptureChanged(hasCapture);
        }
    }

    @Override // android.view.View
    public int getAccessibilityViewId() {
        return 2147483646;
    }

    @Override // android.view.View
    public WindowInsetsController getWindowInsetsController() {
        if (isAttachedToWindow()) {
            return super.getWindowInsetsController();
        }
        return this.mPendingInsetsController;
    }

    @Override // android.view.View
    public String toString() {
        return "DecorView@" + Integer.toHexString(hashCode()) + "[" + getTitleSuffix(this.mWindow.getAttributes()) + "]";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static class ColorViewState {
        final ColorViewAttributes attributes;
        int color;
        boolean visible;
        View view = null;
        int targetVisibility = 4;
        boolean present = false;

        ColorViewState(ColorViewAttributes attributes) {
            this.attributes = attributes;
        }
    }

    /* loaded from: classes4.dex */
    public static class ColorViewAttributes {
        final int horizontalGravity;
        final int id;
        final int insetsType;
        final int seascapeGravity;
        final String transitionName;
        final int translucentFlag;
        final int verticalGravity;

        private ColorViewAttributes(int translucentFlag, int verticalGravity, int horizontalGravity, int seascapeGravity, String transitionName, int id, int insetsType) {
            this.id = id;
            this.translucentFlag = translucentFlag;
            this.verticalGravity = verticalGravity;
            this.horizontalGravity = horizontalGravity;
            this.seascapeGravity = seascapeGravity;
            this.transitionName = transitionName;
            this.insetsType = insetsType;
        }

        public boolean isPresent(boolean requestedVisible, int windowFlags, boolean force) {
            return requestedVisible && ((Integer.MIN_VALUE & windowFlags) != 0 || force);
        }

        public boolean isVisible(boolean present, int color, int windowFlags, boolean force) {
            return present && ((-16777216) & color) != 0 && ((this.translucentFlag & windowFlags) == 0 || force);
        }

        public boolean isVisible(InsetsState state, int color, int windowFlags, boolean force) {
            boolean present = isPresent(state.getSource(this.insetsType).isVisible(), windowFlags, force);
            return isVisible(present, color, windowFlags, force);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public class ActionModeCallback2Wrapper extends ActionMode.Callback2 {
        private final ActionMode.Callback mWrapped;

        public ActionModeCallback2Wrapper(ActionMode.Callback wrapped) {
            this.mWrapped = wrapped;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onCreateActionMode(mode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            DecorView.this.requestFitSystemWindows();
            return this.mWrapped.onPrepareActionMode(mode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrapped.onActionItemClicked(mode, item);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode mode) {
            boolean isPrimary;
            this.mWrapped.onDestroyActionMode(mode);
            boolean isFloating = false;
            boolean isMncApp = DecorView.this.mContext.getApplicationInfo().targetSdkVersion >= 23;
            if (isMncApp) {
                isPrimary = mode == DecorView.this.mPrimaryActionMode;
                if (mode == DecorView.this.mFloatingActionMode) {
                    isFloating = true;
                }
                if (!isPrimary && mode.getType() == 0) {
                    Log.e(DecorView.this.mLogTag, "Destroying unexpected ActionMode instance of TYPE_PRIMARY; " + mode + " was not the current primary action mode! Expected " + DecorView.this.mPrimaryActionMode);
                }
                if (!isFloating && mode.getType() == 1) {
                    Log.e(DecorView.this.mLogTag, "Destroying unexpected ActionMode instance of TYPE_FLOATING; " + mode + " was not the current floating action mode! Expected " + DecorView.this.mFloatingActionMode);
                }
            } else {
                isPrimary = mode.getType() == 0;
                if (mode.getType() == 1) {
                    isFloating = true;
                }
            }
            if (isPrimary) {
                if (DecorView.this.mPrimaryActionModePopup != null) {
                    DecorView decorView = DecorView.this;
                    decorView.removeCallbacks(decorView.mShowPrimaryActionModePopup);
                }
                if (DecorView.this.mPrimaryActionModeView != null) {
                    DecorView.this.endOnGoingFadeAnimation();
                    final ActionBarContextView lastActionModeView = DecorView.this.mPrimaryActionModeView;
                    DecorView decorView2 = DecorView.this;
                    decorView2.mFadeAnim = ObjectAnimator.ofFloat(decorView2.mPrimaryActionModeView, (Property<ActionBarContextView, Float>) View.ALPHA, 1.0f, 0.0f);
                    DecorView.this.mFadeAnim.addListener(new Animator.AnimatorListener() { // from class: com.android.internal.policy.DecorView.ActionModeCallback2Wrapper.1
                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animation) {
                            if (lastActionModeView == DecorView.this.mPrimaryActionModeView) {
                                lastActionModeView.setVisibility(8);
                                if (DecorView.this.mPrimaryActionModePopup != null) {
                                    DecorView.this.mPrimaryActionModePopup.dismiss();
                                }
                                lastActionModeView.killMode();
                                DecorView.this.mFadeAnim = null;
                                DecorView.this.requestApplyInsets();
                            }
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override // android.animation.Animator.AnimatorListener
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    DecorView.this.mFadeAnim.start();
                }
                DecorView.this.mPrimaryActionMode = null;
            } else if (isFloating) {
                DecorView.this.cleanupFloatingActionModeViews();
                DecorView.this.mFloatingActionMode = null;
            }
            if (DecorView.this.mWindow.getCallback() != null && !DecorView.this.mWindow.isDestroyed()) {
                try {
                    DecorView.this.mWindow.getCallback().onActionModeFinished(mode);
                } catch (AbstractMethodError e) {
                }
            }
            DecorView.this.requestFitSystemWindows();
        }

        @Override // android.view.ActionMode.Callback2
        public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
            ActionMode.Callback callback = this.mWrapped;
            if (callback instanceof ActionMode.Callback2) {
                ((ActionMode.Callback2) callback).onGetContentRect(mode, view, outRect);
            } else {
                super.onGetContentRect(mode, view, outRect);
            }
        }
    }
}
