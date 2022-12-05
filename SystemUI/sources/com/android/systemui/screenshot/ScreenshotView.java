package com.android.systemui.screenshot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Looper;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MathUtils;
import android.view.Choreographer;
import android.view.DisplayCutout;
import android.view.GestureDetector;
import android.view.InputEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScrollCaptureResponse;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScreenshotView;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.shared.system.InputChannelCompat$InputEventListener;
import com.android.systemui.shared.system.InputMonitorCompat;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ScreenshotView extends FrameLayout implements ViewTreeObserver.OnComputeInternalInsetsListener {
    private static final String TAG = LogConfig.logTag(ScreenshotView.class);
    private final Interpolator mAccelerateInterpolator;
    private final AccessibilityManager mAccessibilityManager;
    private HorizontalScrollView mActionsContainer;
    private ImageView mActionsContainerBackground;
    private LinearLayout mActionsView;
    private ImageView mBackgroundProtection;
    private ScreenshotViewCallback mCallbacks;
    private final float mCornerSizeX;
    private boolean mDirectionLTR;
    private Animator mDismissAnimation;
    private FrameLayout mDismissButton;
    private final float mDismissDeltaY;
    private final DisplayMetrics mDisplayMetrics;
    private ScreenshotActionChip mEditChip;
    private final Interpolator mFastOutSlowIn;
    private InputMonitorCompat mInputMonitor;
    private int mNavMode;
    private boolean mOrientationPortrait;
    private PendingInteraction mPendingInteraction;
    private boolean mPendingSharedTransition;
    private ScreenshotActionChip mQuickShareChip;
    private final Resources mResources;
    private ImageView mScreenshotFlash;
    private ImageView mScreenshotPreview;
    private View mScreenshotPreviewBorder;
    private ScreenshotSelectorView mScreenshotSelectorView;
    private View mScreenshotStatic;
    private ScreenshotActionChip mScrollChip;
    private ImageView mScrollablePreview;
    private ImageView mScrollingScrim;
    private ScreenshotActionChip mShareChip;
    private boolean mShowScrollablePreview;
    private final ArrayList<ScreenshotActionChip> mSmartChips;
    private int mStaticLeftMargin;
    private GestureDetector mSwipeDetector;
    private SwipeDismissHandler mSwipeDismissHandler;
    private View mTransitionView;
    private UiEventLogger mUiEventLogger;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public enum PendingInteraction {
        PREVIEW,
        EDIT,
        SHARE,
        QUICK_SHARE
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface ScreenshotViewCallback {
        void onDismiss();

        void onTouchOutside();

        void onUserInteraction();
    }

    public ScreenshotView(Context context) {
        this(context, null);
    }

    public ScreenshotView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScreenshotView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ScreenshotView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mAccelerateInterpolator = new AccelerateInterpolator();
        this.mSmartChips = new ArrayList<>();
        Resources resources = ((FrameLayout) this).mContext.getResources();
        this.mResources = resources;
        this.mCornerSizeX = resources.getDimensionPixelSize(R$dimen.global_screenshot_x_scale);
        this.mDismissDeltaY = resources.getDimensionPixelSize(R$dimen.screenshot_dismissal_height_delta);
        this.mFastOutSlowIn = AnimationUtils.loadInterpolator(((FrameLayout) this).mContext, 17563661);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = displayMetrics;
        ((FrameLayout) this).mContext.getDisplay().getRealMetrics(displayMetrics);
        this.mAccessibilityManager = AccessibilityManager.getInstance(((FrameLayout) this).mContext);
        GestureDetector gestureDetector = new GestureDetector(((FrameLayout) this).mContext, new GestureDetector.SimpleOnGestureListener() { // from class: com.android.systemui.screenshot.ScreenshotView.1
            final Rect mActionsRect = new Rect();

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                ScreenshotView.this.mActionsContainer.getBoundsOnScreen(this.mActionsRect);
                return !this.mActionsRect.contains((int) motionEvent2.getRawX(), (int) motionEvent2.getRawY()) || !ScreenshotView.this.mActionsContainer.canScrollHorizontally((int) f);
            }
        });
        this.mSwipeDetector = gestureDetector;
        gestureDetector.setIsLongpressEnabled(false);
        this.mSwipeDismissHandler = new SwipeDismissHandler();
        addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.screenshot.ScreenshotView.2
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                ScreenshotView.this.startInputListening();
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                ScreenshotView.this.stopInputListening();
            }
        });
    }

    public void hideScrollChip() {
        this.mScrollChip.setVisibility(8);
    }

    public void showScrollChip(final String str, final Runnable runnable) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_IMPRESSION, 0, str);
        this.mScrollChip.setVisibility(0);
        this.mScrollChip.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda19
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView.this.lambda$showScrollChip$0(str, runnable, view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showScrollChip$0(String str, Runnable runnable, View view) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_LONG_SCREENSHOT_REQUESTED, 0, str);
        runnable.run();
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(getTouchRegion(true));
    }

    private Region getTouchRegion(boolean z) {
        Region region = new Region();
        Rect rect = new Rect();
        this.mScreenshotPreview.getBoundsOnScreen(rect);
        rect.inset((int) dpToPx(-12.0f), (int) dpToPx(-12.0f));
        region.op(rect, Region.Op.UNION);
        this.mActionsContainerBackground.getBoundsOnScreen(rect);
        rect.inset((int) dpToPx(-12.0f), (int) dpToPx(-12.0f));
        region.op(rect, Region.Op.UNION);
        this.mDismissButton.getBoundsOnScreen(rect);
        region.op(rect, Region.Op.UNION);
        if (z && this.mScrollingScrim.getVisibility() == 0) {
            this.mScrollingScrim.getBoundsOnScreen(rect);
            region.op(rect, Region.Op.UNION);
        }
        if (QuickStepContract.isGesturalMode(this.mNavMode)) {
            Insets insets = ((WindowManager) ((FrameLayout) this).mContext.getSystemService(WindowManager.class)).getCurrentWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.systemGestures());
            Rect rect2 = new Rect(0, 0, insets.left, this.mDisplayMetrics.heightPixels);
            region.op(rect2, Region.Op.UNION);
            DisplayMetrics displayMetrics = this.mDisplayMetrics;
            int i = displayMetrics.widthPixels;
            rect2.set(i - insets.right, 0, i, displayMetrics.heightPixels);
            region.op(rect2, Region.Op.UNION);
        }
        return region;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startInputListening() {
        stopInputListening();
        InputMonitorCompat inputMonitorCompat = new InputMonitorCompat("Screenshot", 0);
        this.mInputMonitor = inputMonitorCompat;
        inputMonitorCompat.getInputReceiver(Looper.getMainLooper(), Choreographer.getInstance(), new InputChannelCompat$InputEventListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda20
            @Override // com.android.systemui.shared.system.InputChannelCompat$InputEventListener
            public final void onInputEvent(InputEvent inputEvent) {
                ScreenshotView.this.lambda$startInputListening$1(inputEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startInputListening$1(InputEvent inputEvent) {
        if (inputEvent instanceof MotionEvent) {
            MotionEvent motionEvent = (MotionEvent) inputEvent;
            if (motionEvent.getActionMasked() != 0 || getTouchRegion(false).contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                return;
            }
            this.mCallbacks.onTouchOutside();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopInputListening() {
        InputMonitorCompat inputMonitorCompat = this.mInputMonitor;
        if (inputMonitorCompat != null) {
            inputMonitorCompat.dispose();
            this.mInputMonitor = null;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!getTouchRegion(false).contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            this.mSwipeDismissHandler.onTouch(this, motionEvent);
        }
        return this.mSwipeDetector.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        ImageView imageView = (ImageView) findViewById(R$id.screenshot_scrolling_scrim);
        Objects.requireNonNull(imageView);
        this.mScrollingScrim = imageView;
        View findViewById = findViewById(R$id.global_screenshot_static);
        Objects.requireNonNull(findViewById);
        this.mScreenshotStatic = findViewById;
        ImageView imageView2 = (ImageView) findViewById(R$id.global_screenshot_preview);
        Objects.requireNonNull(imageView2);
        this.mScreenshotPreview = imageView2;
        View findViewById2 = findViewById(R$id.screenshot_transition_view);
        Objects.requireNonNull(findViewById2);
        this.mTransitionView = findViewById2;
        View findViewById3 = findViewById(R$id.global_screenshot_preview_border);
        Objects.requireNonNull(findViewById3);
        this.mScreenshotPreviewBorder = findViewById3;
        this.mScreenshotPreview.setClipToOutline(true);
        ImageView imageView3 = (ImageView) findViewById(R$id.global_screenshot_actions_container_background);
        Objects.requireNonNull(imageView3);
        this.mActionsContainerBackground = imageView3;
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R$id.global_screenshot_actions_container);
        Objects.requireNonNull(horizontalScrollView);
        this.mActionsContainer = horizontalScrollView;
        LinearLayout linearLayout = (LinearLayout) findViewById(R$id.global_screenshot_actions);
        Objects.requireNonNull(linearLayout);
        this.mActionsView = linearLayout;
        ImageView imageView4 = (ImageView) findViewById(R$id.global_screenshot_actions_background);
        Objects.requireNonNull(imageView4);
        this.mBackgroundProtection = imageView4;
        FrameLayout frameLayout = (FrameLayout) findViewById(R$id.global_screenshot_dismiss_button);
        Objects.requireNonNull(frameLayout);
        this.mDismissButton = frameLayout;
        ImageView imageView5 = (ImageView) findViewById(R$id.screenshot_scrollable_preview);
        Objects.requireNonNull(imageView5);
        this.mScrollablePreview = imageView5;
        ImageView imageView6 = (ImageView) findViewById(R$id.global_screenshot_flash);
        Objects.requireNonNull(imageView6);
        this.mScreenshotFlash = imageView6;
        ScreenshotSelectorView screenshotSelectorView = (ScreenshotSelectorView) findViewById(R$id.global_screenshot_selector);
        Objects.requireNonNull(screenshotSelectorView);
        this.mScreenshotSelectorView = screenshotSelectorView;
        ScreenshotActionChip screenshotActionChip = (ScreenshotActionChip) this.mActionsContainer.findViewById(R$id.screenshot_share_chip);
        Objects.requireNonNull(screenshotActionChip);
        this.mShareChip = screenshotActionChip;
        ScreenshotActionChip screenshotActionChip2 = (ScreenshotActionChip) this.mActionsContainer.findViewById(R$id.screenshot_edit_chip);
        Objects.requireNonNull(screenshotActionChip2);
        this.mEditChip = screenshotActionChip2;
        ScreenshotActionChip screenshotActionChip3 = (ScreenshotActionChip) this.mActionsContainer.findViewById(R$id.screenshot_scroll_chip);
        Objects.requireNonNull(screenshotActionChip3);
        this.mScrollChip = screenshotActionChip3;
        int dpToPx = (int) dpToPx(12.0f);
        this.mScreenshotPreview.setTouchDelegate(new TouchDelegate(new Rect(dpToPx, dpToPx, dpToPx, dpToPx), this.mScreenshotPreview));
        this.mActionsContainerBackground.setTouchDelegate(new TouchDelegate(new Rect(dpToPx, dpToPx, dpToPx, dpToPx), this.mActionsContainerBackground));
        setFocusable(true);
        this.mScreenshotSelectorView.setFocusable(true);
        this.mScreenshotSelectorView.setFocusableInTouchMode(true);
        boolean z = false;
        this.mActionsContainer.setScrollX(0);
        this.mNavMode = getResources().getInteger(17694868);
        this.mOrientationPortrait = getResources().getConfiguration().orientation == 1;
        if (getResources().getConfiguration().getLayoutDirection() == 0) {
            z = true;
        }
        this.mDirectionLTR = z;
        setFocusableInTouchMode(true);
        requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View getTransitionView() {
        return this.mTransitionView;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getStaticLeftMargin() {
        return this.mStaticLeftMargin;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void init(UiEventLogger uiEventLogger, ScreenshotViewCallback screenshotViewCallback) {
        this.mUiEventLogger = uiEventLogger;
        this.mCallbacks = screenshotViewCallback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void takePartialScreenshot(Consumer<Rect> consumer) {
        this.mScreenshotSelectorView.setOnScreenshotSelected(consumer);
        this.mScreenshotSelectorView.setVisibility(0);
        this.mScreenshotSelectorView.requestFocus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setScreenshot(Bitmap bitmap, Insets insets) {
        this.mScreenshotPreview.setImageDrawable(createScreenDrawable(this.mResources, bitmap, insets));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateInsets(WindowInsets windowInsets) {
        boolean z = true;
        if (((FrameLayout) this).mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        this.mOrientationPortrait = z;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mScreenshotStatic.getLayoutParams();
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        Insets insets = windowInsets.getInsets(WindowInsets.Type.navigationBars());
        if (displayCutout == null) {
            layoutParams.setMargins(0, 0, 0, insets.bottom);
        } else {
            Insets waterfallInsets = displayCutout.getWaterfallInsets();
            if (this.mOrientationPortrait) {
                layoutParams.setMargins(waterfallInsets.left, Math.max(displayCutout.getSafeInsetTop(), waterfallInsets.top), waterfallInsets.right, Math.max(displayCutout.getSafeInsetBottom(), Math.max(insets.bottom, waterfallInsets.bottom)));
            } else {
                layoutParams.setMargins(Math.max(displayCutout.getSafeInsetLeft(), waterfallInsets.left), waterfallInsets.top, Math.max(displayCutout.getSafeInsetRight(), waterfallInsets.right), Math.max(insets.bottom, waterfallInsets.bottom));
            }
        }
        this.mStaticLeftMargin = layoutParams.leftMargin;
        this.mScreenshotStatic.setLayoutParams(layoutParams);
        this.mScreenshotStatic.requestLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateOrientation(WindowInsets windowInsets) {
        boolean z = true;
        if (((FrameLayout) this).mContext.getResources().getConfiguration().orientation != 1) {
            z = false;
        }
        this.mOrientationPortrait = z;
        updateInsets(windowInsets);
        int dimensionPixelSize = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.global_screenshot_x_scale);
        ViewGroup.LayoutParams layoutParams = this.mScreenshotPreview.getLayoutParams();
        if (this.mOrientationPortrait) {
            layoutParams.width = dimensionPixelSize;
            layoutParams.height = -2;
            this.mScreenshotPreview.setScaleType(ImageView.ScaleType.FIT_START);
        } else {
            layoutParams.width = -2;
            layoutParams.height = dimensionPixelSize;
            this.mScreenshotPreview.setScaleType(ImageView.ScaleType.FIT_END);
        }
        this.mScreenshotPreview.setLayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimatorSet createScreenshotDropInAnimation(Rect rect, boolean z) {
        Rect rect2 = new Rect();
        this.mScreenshotPreview.getHitRect(rect2);
        float width = this.mCornerSizeX / (this.mOrientationPortrait ? rect.width() : rect.height());
        final float f = 1.0f / width;
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(133L);
        ofFloat.setInterpolator(this.mFastOutSlowIn);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$createScreenshotDropInAnimation$2(valueAnimator);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat2.setDuration(217L);
        ofFloat2.setInterpolator(this.mFastOutSlowIn);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$createScreenshotDropInAnimation$3(valueAnimator);
            }
        });
        final PointF pointF = new PointF(rect.centerX(), rect.centerY());
        final PointF pointF2 = new PointF(rect2.exactCenterX(), rect2.exactCenterY());
        int[] locationOnScreen = this.mScreenshotPreview.getLocationOnScreen();
        pointF.offset(rect2.left - locationOnScreen[0], rect2.top - locationOnScreen[1]);
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat3.setDuration(500L);
        ofFloat3.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScreenshotView.this.mScreenshotPreview.setScaleX(f);
                ScreenshotView.this.mScreenshotPreview.setScaleY(f);
                ScreenshotView.this.mScreenshotPreview.setVisibility(0);
                if (ScreenshotView.this.mAccessibilityManager.isEnabled()) {
                    ScreenshotView.this.mDismissButton.setAlpha(0.0f);
                    ScreenshotView.this.mDismissButton.setVisibility(0);
                }
            }
        });
        ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda9
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$createScreenshotDropInAnimation$4(r2, f, r4, pointF, pointF2, r7, valueAnimator);
            }
        });
        this.mScreenshotFlash.setAlpha(0.0f);
        this.mScreenshotFlash.setVisibility(0);
        ValueAnimator ofFloat4 = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat4.setDuration(100L);
        ofFloat4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$createScreenshotDropInAnimation$5(valueAnimator);
            }
        });
        if (z) {
            animatorSet.play(ofFloat2).after(ofFloat);
            animatorSet.play(ofFloat2).with(ofFloat3);
        } else {
            animatorSet.play(ofFloat3);
        }
        animatorSet.play(ofFloat4).after(ofFloat3);
        animatorSet.addListener(new AnonymousClass4(pointF2, rect, width));
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotDropInAnimation$2(ValueAnimator valueAnimator) {
        this.mScreenshotFlash.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotDropInAnimation$3(ValueAnimator valueAnimator) {
        this.mScreenshotFlash.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotDropInAnimation$4(float f, float f2, float f3, PointF pointF, PointF pointF2, float f4, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        if (animatedFraction < f) {
            float lerp = MathUtils.lerp(f2, 1.0f, this.mFastOutSlowIn.getInterpolation(animatedFraction / f));
            this.mScreenshotPreview.setScaleX(lerp);
            this.mScreenshotPreview.setScaleY(lerp);
        } else {
            this.mScreenshotPreview.setScaleX(1.0f);
            this.mScreenshotPreview.setScaleY(1.0f);
        }
        if (animatedFraction < f3) {
            float lerp2 = MathUtils.lerp(pointF.x, pointF2.x, this.mFastOutSlowIn.getInterpolation(animatedFraction / f3));
            ImageView imageView = this.mScreenshotPreview;
            imageView.setX(lerp2 - (imageView.getWidth() / 2.0f));
        } else {
            ImageView imageView2 = this.mScreenshotPreview;
            imageView2.setX(pointF2.x - (imageView2.getWidth() / 2.0f));
        }
        float lerp3 = MathUtils.lerp(pointF.y, pointF2.y, this.mFastOutSlowIn.getInterpolation(animatedFraction));
        ImageView imageView3 = this.mScreenshotPreview;
        imageView3.setY(lerp3 - (imageView3.getHeight() / 2.0f));
        if (animatedFraction >= f4) {
            this.mDismissButton.setAlpha((animatedFraction - f4) / (1.0f - f4));
            float x = this.mScreenshotPreview.getX();
            float y = this.mScreenshotPreview.getY();
            FrameLayout frameLayout = this.mDismissButton;
            frameLayout.setY(y - (frameLayout.getHeight() / 2.0f));
            if (this.mDirectionLTR) {
                this.mDismissButton.setX((x + this.mScreenshotPreview.getWidth()) - (this.mDismissButton.getWidth() / 2.0f));
                return;
            }
            FrameLayout frameLayout2 = this.mDismissButton;
            frameLayout2.setX(x - (frameLayout2.getWidth() / 2.0f));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotDropInAnimation$5(ValueAnimator valueAnimator) {
        this.mScreenshotPreviewBorder.setAlpha(valueAnimator.getAnimatedFraction());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.screenshot.ScreenshotView$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 extends AnimatorListenerAdapter {
        final /* synthetic */ Rect val$bounds;
        final /* synthetic */ float val$cornerScale;
        final /* synthetic */ PointF val$finalPos;

        AnonymousClass4(PointF pointF, Rect rect, float f) {
            this.val$finalPos = pointF;
            this.val$bounds = rect;
            this.val$cornerScale = f;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            float width;
            ScreenshotView.this.mDismissButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$4$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ScreenshotView.AnonymousClass4.this.lambda$onAnimationEnd$0(view);
                }
            });
            ScreenshotView.this.mDismissButton.setAlpha(1.0f);
            float width2 = ScreenshotView.this.mDismissButton.getWidth() / 2.0f;
            if (ScreenshotView.this.mDirectionLTR) {
                width = (this.val$finalPos.x - width2) + ((this.val$bounds.width() * this.val$cornerScale) / 2.0f);
            } else {
                width = (this.val$finalPos.x - width2) - ((this.val$bounds.width() * this.val$cornerScale) / 2.0f);
            }
            ScreenshotView.this.mDismissButton.setX(width);
            ScreenshotView.this.mDismissButton.setY((this.val$finalPos.y - width2) - ((this.val$bounds.height() * this.val$cornerScale) / 2.0f));
            ScreenshotView.this.mScreenshotPreview.setScaleX(1.0f);
            ScreenshotView.this.mScreenshotPreview.setScaleY(1.0f);
            ScreenshotView.this.mScreenshotPreview.setX(this.val$finalPos.x - (ScreenshotView.this.mScreenshotPreview.getWidth() / 2.0f));
            ScreenshotView.this.mScreenshotPreview.setY(this.val$finalPos.y - (ScreenshotView.this.mScreenshotPreview.getHeight() / 2.0f));
            ScreenshotView.this.requestLayout();
            ScreenshotView.this.createScreenshotActionsShadeAnimation().start();
            ScreenshotView screenshotView = ScreenshotView.this;
            screenshotView.setOnTouchListener(screenshotView.mSwipeDismissHandler);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0(View view) {
            ScreenshotView.this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_EXPLICIT_DISMISSAL);
            ScreenshotView.this.animateDismissal();
        }
    }

    ValueAnimator createScreenshotActionsShadeAnimation() {
        try {
            ActivityManager.getService().resumeAppSwitches();
        } catch (RemoteException unused) {
        }
        final ArrayList arrayList = new ArrayList();
        this.mShareChip.setContentDescription(((FrameLayout) this).mContext.getString(17041377));
        this.mShareChip.setIcon(Icon.createWithResource(((FrameLayout) this).mContext, R$drawable.ic_screenshot_share), true);
        this.mShareChip.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda14
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView.this.lambda$createScreenshotActionsShadeAnimation$6(view);
            }
        });
        arrayList.add(this.mShareChip);
        this.mEditChip.setContentDescription(((FrameLayout) this).mContext.getString(R$string.screenshot_edit_label));
        this.mEditChip.setIcon(Icon.createWithResource(((FrameLayout) this).mContext, R$drawable.ic_screenshot_edit), true);
        this.mEditChip.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda13
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView.this.lambda$createScreenshotActionsShadeAnimation$7(view);
            }
        });
        arrayList.add(this.mEditChip);
        this.mScreenshotPreview.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda12
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView.this.lambda$createScreenshotActionsShadeAnimation$8(view);
            }
        });
        this.mScrollChip.setText(((FrameLayout) this).mContext.getString(R$string.screenshot_scroll_label));
        this.mScrollChip.setIcon(Icon.createWithResource(((FrameLayout) this).mContext, R$drawable.ic_screenshot_scroll), true);
        arrayList.add(this.mScrollChip);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mActionsView.getChildAt(0).getLayoutParams();
        layoutParams.setMarginEnd(0);
        this.mActionsView.getChildAt(0).setLayoutParams(layoutParams);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setDuration(400L);
        this.mActionsContainer.setAlpha(0.0f);
        this.mActionsContainerBackground.setAlpha(0.0f);
        this.mActionsContainer.setVisibility(0);
        this.mActionsContainerBackground.setVisibility(0);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda11
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$createScreenshotActionsShadeAnimation$9(r2, arrayList, valueAnimator);
            }
        });
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotActionsShadeAnimation$6(View view) {
        this.mShareChip.setIsPending(true);
        this.mEditChip.setIsPending(false);
        ScreenshotActionChip screenshotActionChip = this.mQuickShareChip;
        if (screenshotActionChip != null) {
            screenshotActionChip.setIsPending(false);
        }
        this.mPendingInteraction = PendingInteraction.SHARE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotActionsShadeAnimation$7(View view) {
        this.mEditChip.setIsPending(true);
        this.mShareChip.setIsPending(false);
        ScreenshotActionChip screenshotActionChip = this.mQuickShareChip;
        if (screenshotActionChip != null) {
            screenshotActionChip.setIsPending(false);
        }
        this.mPendingInteraction = PendingInteraction.EDIT;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotActionsShadeAnimation$8(View view) {
        this.mShareChip.setIsPending(false);
        this.mEditChip.setIsPending(false);
        ScreenshotActionChip screenshotActionChip = this.mQuickShareChip;
        if (screenshotActionChip != null) {
            screenshotActionChip.setIsPending(false);
        }
        this.mPendingInteraction = PendingInteraction.PREVIEW;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotActionsShadeAnimation$9(float f, ArrayList arrayList, ValueAnimator valueAnimator) {
        HorizontalScrollView horizontalScrollView;
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.mBackgroundProtection.setAlpha(animatedFraction);
        float f2 = animatedFraction < f ? animatedFraction / f : 1.0f;
        this.mActionsContainer.setAlpha(f2);
        this.mActionsContainerBackground.setAlpha(f2);
        float f3 = (0.3f * animatedFraction) + 0.7f;
        this.mActionsContainer.setScaleX(f3);
        this.mActionsContainerBackground.setScaleX(f3);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ScreenshotActionChip screenshotActionChip = (ScreenshotActionChip) it.next();
            screenshotActionChip.setAlpha(animatedFraction);
            screenshotActionChip.setScaleX(1.0f / f3);
        }
        HorizontalScrollView horizontalScrollView2 = this.mActionsContainer;
        horizontalScrollView2.setScrollX(this.mDirectionLTR ? 0 : horizontalScrollView2.getWidth());
        float f4 = 0.0f;
        this.mActionsContainer.setPivotX(this.mDirectionLTR ? 0.0f : horizontalScrollView.getWidth());
        ImageView imageView = this.mActionsContainerBackground;
        if (!this.mDirectionLTR) {
            f4 = imageView.getWidth();
        }
        imageView.setPivotX(f4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setChipIntents(final ScreenshotController.SavedImageData savedImageData) {
        this.mShareChip.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda18
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView.this.lambda$setChipIntents$10(savedImageData, view);
            }
        });
        this.mEditChip.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda16
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView.this.lambda$setChipIntents$11(savedImageData, view);
            }
        });
        this.mScreenshotPreview.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda17
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotView.this.lambda$setChipIntents$12(savedImageData, view);
            }
        });
        ScreenshotActionChip screenshotActionChip = this.mQuickShareChip;
        if (screenshotActionChip != null) {
            screenshotActionChip.setPendingIntent(savedImageData.quickShareAction.actionIntent, new Runnable() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda22
                @Override // java.lang.Runnable
                public final void run() {
                    ScreenshotView.this.lambda$setChipIntents$13();
                }
            });
        }
        PendingInteraction pendingInteraction = this.mPendingInteraction;
        if (pendingInteraction != null) {
            int i = AnonymousClass9.$SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction[pendingInteraction.ordinal()];
            if (i == 1) {
                this.mScreenshotPreview.callOnClick();
                return;
            } else if (i == 2) {
                this.mShareChip.callOnClick();
                return;
            } else if (i == 3) {
                this.mEditChip.callOnClick();
                return;
            } else if (i != 4) {
                return;
            } else {
                this.mQuickShareChip.callOnClick();
                return;
            }
        }
        LayoutInflater from = LayoutInflater.from(((FrameLayout) this).mContext);
        for (Notification.Action action : savedImageData.smartActions) {
            ScreenshotActionChip screenshotActionChip2 = (ScreenshotActionChip) from.inflate(R$layout.global_screenshot_action_chip, (ViewGroup) this.mActionsView, false);
            screenshotActionChip2.setText(action.title);
            screenshotActionChip2.setIcon(action.getIcon(), false);
            screenshotActionChip2.setPendingIntent(action.actionIntent, new Runnable() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda21
                @Override // java.lang.Runnable
                public final void run() {
                    ScreenshotView.this.lambda$setChipIntents$14();
                }
            });
            screenshotActionChip2.setAlpha(1.0f);
            this.mActionsView.addView(screenshotActionChip2);
            this.mSmartChips.add(screenshotActionChip2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$10(ScreenshotController.SavedImageData savedImageData, View view) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SHARE_TAPPED);
        startSharedTransition(savedImageData.shareTransition.get());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$11(ScreenshotController.SavedImageData savedImageData, View view) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_EDIT_TAPPED);
        startSharedTransition(savedImageData.editTransition.get());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$12(ScreenshotController.SavedImageData savedImageData, View view) {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_PREVIEW_TAPPED);
        startSharedTransition(savedImageData.editTransition.get());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$13() {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SMART_ACTION_TAPPED);
        animateDismissal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.screenshot.ScreenshotView$9  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction;

        static {
            int[] iArr = new int[PendingInteraction.values().length];
            $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction = iArr;
            try {
                iArr[PendingInteraction.PREVIEW.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction[PendingInteraction.SHARE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction[PendingInteraction.EDIT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$screenshot$ScreenshotView$PendingInteraction[PendingInteraction.QUICK_SHARE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setChipIntents$14() {
        this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SMART_ACTION_TAPPED);
        animateDismissal();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addQuickShareChip(Notification.Action action) {
        if (this.mPendingInteraction == null) {
            ScreenshotActionChip screenshotActionChip = (ScreenshotActionChip) LayoutInflater.from(((FrameLayout) this).mContext).inflate(R$layout.global_screenshot_action_chip, (ViewGroup) this.mActionsView, false);
            this.mQuickShareChip = screenshotActionChip;
            screenshotActionChip.setText(action.title);
            this.mQuickShareChip.setIcon(action.getIcon(), false);
            this.mQuickShareChip.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda15
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ScreenshotView.this.lambda$addQuickShareChip$15(view);
                }
            });
            this.mQuickShareChip.setAlpha(1.0f);
            this.mActionsView.addView(this.mQuickShareChip);
            this.mSmartChips.add(this.mQuickShareChip);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addQuickShareChip$15(View view) {
        this.mShareChip.setIsPending(false);
        this.mEditChip.setIsPending(false);
        this.mQuickShareChip.setIsPending(true);
        this.mPendingInteraction = PendingInteraction.QUICK_SHARE;
    }

    private Rect scrollableAreaOnScreen(ScrollCaptureResponse scrollCaptureResponse) {
        Rect rect = new Rect(scrollCaptureResponse.getBoundsInWindow());
        Rect windowBounds = scrollCaptureResponse.getWindowBounds();
        rect.offset(windowBounds.left, windowBounds.top);
        DisplayMetrics displayMetrics = this.mDisplayMetrics;
        rect.intersect(new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels));
        return rect;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startLongScreenshotTransition(final Rect rect, final Runnable runnable, ScrollCaptureController.LongScreenshot longScreenshot) {
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$startLongScreenshotTransition$16(valueAnimator);
            }
        });
        if (this.mShowScrollablePreview) {
            this.mScrollablePreview.setImageBitmap(longScreenshot.toBitmap());
            final float x = this.mScrollablePreview.getX();
            final float y = this.mScrollablePreview.getY();
            int[] locationOnScreen = this.mScrollablePreview.getLocationOnScreen();
            rect.offset(((int) x) - locationOnScreen[0], ((int) y) - locationOnScreen[1]);
            this.mScrollablePreview.setPivotX(0.0f);
            this.mScrollablePreview.setPivotY(0.0f);
            this.mScrollablePreview.setAlpha(1.0f);
            float width = this.mScrollablePreview.getWidth() / longScreenshot.getWidth();
            Matrix matrix = new Matrix();
            matrix.setScale(width, width);
            matrix.postTranslate(longScreenshot.getLeft() * width, longScreenshot.getTop() * width);
            this.mScrollablePreview.setImageMatrix(matrix);
            final float width2 = rect.width() / this.mScrollablePreview.getWidth();
            ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda10
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ScreenshotView.this.lambda$startLongScreenshotTransition$17(width2, x, rect, y, valueAnimator);
                }
            });
            ValueAnimator ofFloat3 = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ScreenshotView.this.lambda$startLongScreenshotTransition$18(valueAnimator);
                }
            });
            animatorSet.play(ofFloat2).with(ofFloat).before(ofFloat3);
            ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    runnable.run();
                }
            });
        } else {
            animatorSet.play(ofFloat);
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    runnable.run();
                }
            });
        }
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.7
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ScreenshotView.this.mCallbacks.onDismiss();
            }
        });
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startLongScreenshotTransition$16(ValueAnimator valueAnimator) {
        this.mScrollingScrim.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startLongScreenshotTransition$17(float f, float f2, Rect rect, float f3, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        float lerp = MathUtils.lerp(1.0f, f, animatedFraction);
        this.mScrollablePreview.setScaleX(lerp);
        this.mScrollablePreview.setScaleY(lerp);
        this.mScrollablePreview.setX(MathUtils.lerp(f2, rect.left, animatedFraction));
        this.mScrollablePreview.setY(MathUtils.lerp(f3, rect.top, animatedFraction));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startLongScreenshotTransition$18(ValueAnimator valueAnimator) {
        this.mScrollablePreview.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void prepareScrollingTransition(ScrollCaptureResponse scrollCaptureResponse, Bitmap bitmap, Bitmap bitmap2, boolean z) {
        this.mShowScrollablePreview = z == this.mOrientationPortrait;
        this.mScrollingScrim.setImageBitmap(bitmap2);
        this.mScrollingScrim.setVisibility(0);
        if (this.mShowScrollablePreview) {
            Rect scrollableAreaOnScreen = scrollableAreaOnScreen(scrollCaptureResponse);
            float width = this.mCornerSizeX / (this.mOrientationPortrait ? bitmap.getWidth() : bitmap.getHeight());
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mScrollablePreview.getLayoutParams();
            ((ViewGroup.MarginLayoutParams) layoutParams).width = (int) (scrollableAreaOnScreen.width() * width);
            ((ViewGroup.MarginLayoutParams) layoutParams).height = (int) (scrollableAreaOnScreen.height() * width);
            Matrix matrix = new Matrix();
            matrix.setScale(width, width);
            matrix.postTranslate((-scrollableAreaOnScreen.left) * width, (-scrollableAreaOnScreen.top) * width);
            this.mScrollablePreview.setTranslationX((this.mDirectionLTR ? scrollableAreaOnScreen.left : scrollableAreaOnScreen.right - getWidth()) * width);
            this.mScrollablePreview.setTranslationY(width * scrollableAreaOnScreen.top);
            this.mScrollablePreview.setImageMatrix(matrix);
            this.mScrollablePreview.setImageBitmap(bitmap);
            this.mScrollablePreview.setVisibility(0);
        }
        this.mDismissButton.setVisibility(8);
        this.mActionsContainer.setVisibility(8);
        this.mBackgroundProtection.setVisibility(8);
        this.mActionsContainerBackground.setVisibility(4);
        this.mScreenshotPreviewBorder.setVisibility(4);
        this.mScreenshotPreview.setVisibility(4);
        this.mScrollingScrim.setImageTintBlendMode(BlendMode.SRC_ATOP);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 0.3f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$prepareScrollingTransition$19(valueAnimator);
            }
        });
        ofFloat.setDuration(200L);
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareScrollingTransition$19(ValueAnimator valueAnimator) {
        this.mScrollingScrim.setImageTintList(ColorStateList.valueOf(Color.argb(((Float) valueAnimator.getAnimatedValue()).floatValue(), 0.0f, 0.0f, 0.0f)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void restoreNonScrollingUi() {
        this.mScrollChip.setVisibility(8);
        this.mScrollablePreview.setVisibility(8);
        this.mScrollingScrim.setVisibility(8);
        if (this.mAccessibilityManager.isEnabled()) {
            this.mDismissButton.setVisibility(0);
        }
        this.mActionsContainer.setVisibility(0);
        this.mBackgroundProtection.setVisibility(0);
        this.mActionsContainerBackground.setVisibility(0);
        this.mScreenshotPreviewBorder.setVisibility(0);
        this.mScreenshotPreview.setVisibility(0);
        this.mCallbacks.onUserInteraction();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isDismissing() {
        Animator animator = this.mDismissAnimation;
        return animator != null && animator.isRunning();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPendingSharedTransition() {
        return this.mPendingSharedTransition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void animateDismissal() {
        animateDismissal(createScreenshotTranslateDismissAnimation());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateDismissal(Animator animator) {
        this.mDismissAnimation = animator;
        animator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotView.8
            private boolean mCancelled = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator2) {
                super.onAnimationCancel(animator2);
                this.mCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator2) {
                super.onAnimationEnd(animator2);
                if (!this.mCancelled) {
                    ScreenshotView.this.mCallbacks.onDismiss();
                }
            }
        });
        this.mDismissAnimation.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reset() {
        Animator animator = this.mDismissAnimation;
        if (animator != null && animator.isRunning()) {
            this.mDismissAnimation.cancel();
        }
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this);
        this.mScreenshotPreview.setImageDrawable(null);
        this.mScreenshotPreview.setVisibility(4);
        this.mScreenshotPreviewBorder.setAlpha(0.0f);
        this.mPendingSharedTransition = false;
        this.mActionsContainerBackground.setVisibility(8);
        this.mActionsContainer.setVisibility(8);
        this.mBackgroundProtection.setAlpha(0.0f);
        this.mDismissButton.setVisibility(8);
        this.mScrollingScrim.setVisibility(8);
        this.mScrollablePreview.setVisibility(8);
        this.mScreenshotStatic.setTranslationX(0.0f);
        this.mScreenshotPreview.setContentDescription(((FrameLayout) this).mContext.getResources().getString(R$string.screenshot_preview_description));
        this.mScreenshotPreview.setOnClickListener(null);
        this.mShareChip.setOnClickListener(null);
        this.mScrollingScrim.setVisibility(8);
        this.mEditChip.setOnClickListener(null);
        this.mShareChip.setIsPending(false);
        this.mEditChip.setIsPending(false);
        this.mPendingInteraction = null;
        Iterator<ScreenshotActionChip> it = this.mSmartChips.iterator();
        while (it.hasNext()) {
            this.mActionsView.removeView(it.next());
        }
        this.mSmartChips.clear();
        this.mQuickShareChip = null;
        setAlpha(1.0f);
        this.mScreenshotSelectorView.stop();
    }

    private void startSharedTransition(ScreenshotController.SavedImageData.ActionTransition actionTransition) {
        try {
            this.mPendingSharedTransition = true;
            actionTransition.action.actionIntent.send();
            createScreenshotFadeDismissAnimation().start();
        } catch (PendingIntent.CanceledException e) {
            this.mPendingSharedTransition = false;
            Runnable runnable = actionTransition.onCancelRunnable;
            if (runnable != null) {
                runnable.run();
            }
            Log.e(TAG, "Intent cancelled", e);
        }
        if (this.mPendingSharedTransition) {
            ScreenshotActionChip screenshotActionChip = this.mShareChip;
            if (screenshotActionChip != null) {
                screenshotActionChip.setOnClickListener(null);
            }
            ScreenshotActionChip screenshotActionChip2 = this.mEditChip;
            if (screenshotActionChip2 != null) {
                screenshotActionChip2.setOnClickListener(null);
            }
            ImageView imageView = this.mScreenshotPreview;
            if (imageView == null) {
                return;
            }
            imageView.setOnClickListener(null);
        }
    }

    private AnimatorSet createScreenshotTranslateDismissAnimation() {
        final float x;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.setStartDelay(50L);
        ofFloat.setDuration(350L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$createScreenshotTranslateDismissAnimation$20(valueAnimator);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat2.setInterpolator(this.mAccelerateInterpolator);
        ofFloat2.setDuration(350L);
        if (this.mDirectionLTR) {
            x = (this.mScreenshotPreviewBorder.getX() + this.mScreenshotPreviewBorder.getWidth()) * (-1.0f);
        } else {
            x = this.mDisplayMetrics.widthPixels - this.mScreenshotPreviewBorder.getX();
        }
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$createScreenshotTranslateDismissAnimation$21(x, valueAnimator);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofFloat2).with(ofFloat);
        return animatorSet;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotTranslateDismissAnimation$20(ValueAnimator valueAnimator) {
        setAlpha(1.0f - valueAnimator.getAnimatedFraction());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotTranslateDismissAnimation$21(float f, ValueAnimator valueAnimator) {
        this.mScreenshotStatic.setTranslationX(MathUtils.lerp(0.0f, f, valueAnimator.getAnimatedFraction()));
    }

    ValueAnimator createScreenshotFadeDismissAnimation() {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenshotView.this.lambda$createScreenshotFadeDismissAnimation$22(valueAnimator);
            }
        });
        ofFloat.setDuration(600L);
        return ofFloat;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createScreenshotFadeDismissAnimation$22(ValueAnimator valueAnimator) {
        float animatedFraction = 1.0f - valueAnimator.getAnimatedFraction();
        this.mDismissButton.setAlpha(animatedFraction);
        this.mActionsContainerBackground.setAlpha(animatedFraction);
        this.mActionsContainer.setAlpha(animatedFraction);
        this.mBackgroundProtection.setAlpha(animatedFraction);
        this.mScreenshotPreviewBorder.setAlpha(animatedFraction);
    }

    private static Drawable createScreenDrawable(Resources resources, Bitmap bitmap, Insets insets) {
        int width = (bitmap.getWidth() - insets.left) - insets.right;
        int height = (bitmap.getHeight() - insets.top) - insets.bottom;
        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
        if (height == 0 || width == 0 || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            Log.e(TAG, "Can't create inset drawable, using 0 insets bitmap and insets create degenerate region: " + bitmap.getWidth() + "x" + bitmap.getHeight() + " " + bitmapDrawable);
            return bitmapDrawable;
        }
        float f = width;
        float f2 = height;
        InsetDrawable insetDrawable = new InsetDrawable(bitmapDrawable, (insets.left * (-1.0f)) / f, (insets.top * (-1.0f)) / f2, (insets.right * (-1.0f)) / f, (insets.bottom * (-1.0f)) / f2);
        return (insets.left < 0 || insets.top < 0 || insets.right < 0 || insets.bottom < 0) ? new LayerDrawable(new Drawable[]{new ColorDrawable(-16777216), insetDrawable}) : insetDrawable;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float dpToPx(float f) {
        return (f * this.mDisplayMetrics.densityDpi) / 160.0f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class SwipeDismissHandler implements View.OnTouchListener {
        private int mDirectionX;
        private final GestureDetector mGestureDetector;
        private float mPreviousX;
        private float mStartX;

        SwipeDismissHandler() {
            this.mGestureDetector = new GestureDetector(((FrameLayout) ScreenshotView.this).mContext, new SwipeDismissGestureListener());
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            boolean onTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
            ScreenshotView.this.mCallbacks.onUserInteraction();
            if (motionEvent.getActionMasked() == 0) {
                float rawX = motionEvent.getRawX();
                this.mStartX = rawX;
                this.mPreviousX = rawX;
                return true;
            } else if (motionEvent.getActionMasked() != 1) {
                return onTouchEvent;
            } else {
                if (!isPastDismissThreshold() || (ScreenshotView.this.mDismissAnimation != null && ScreenshotView.this.mDismissAnimation.isRunning())) {
                    if (ScreenshotView.this.mDismissAnimation == null || !ScreenshotView.this.mDismissAnimation.isRunning()) {
                        createSwipeReturnAnimation().start();
                    }
                } else {
                    ScreenshotView.this.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SWIPE_DISMISSED);
                    ScreenshotView.this.animateDismissal(createSwipeDismissAnimation());
                }
                return true;
            }
        }

        /* loaded from: classes.dex */
        class SwipeDismissGestureListener extends GestureDetector.SimpleOnGestureListener {
            SwipeDismissGestureListener() {
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                ScreenshotView.this.mScreenshotStatic.setTranslationX(motionEvent2.getRawX() - SwipeDismissHandler.this.mStartX);
                SwipeDismissHandler.this.mDirectionX = motionEvent2.getRawX() < SwipeDismissHandler.this.mPreviousX ? -1 : 1;
                SwipeDismissHandler.this.mPreviousX = motionEvent2.getRawX();
                return true;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                if (ScreenshotView.this.mScreenshotStatic.getTranslationX() * f > 0.0f) {
                    if (ScreenshotView.this.mDismissAnimation != null && ScreenshotView.this.mDismissAnimation.isRunning()) {
                        return false;
                    }
                    SwipeDismissHandler swipeDismissHandler = SwipeDismissHandler.this;
                    ScreenshotView.this.animateDismissal(swipeDismissHandler.createSwipeDismissAnimation(f / 1000.0f));
                    return true;
                }
                return false;
            }
        }

        private boolean isPastDismissThreshold() {
            float translationX = ScreenshotView.this.mScreenshotStatic.getTranslationX();
            return ((float) this.mDirectionX) * translationX > 0.0f && Math.abs(translationX) >= ScreenshotView.this.dpToPx(20.0f);
        }

        private ValueAnimator createSwipeDismissAnimation() {
            return createSwipeDismissAnimation(1.0f);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public ValueAnimator createSwipeDismissAnimation(float f) {
            float min = Math.min(3.0f, Math.max(1.0f, f));
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            final float translationX = ScreenshotView.this.mScreenshotStatic.getTranslationX();
            final float right = translationX < 0.0f ? ScreenshotView.this.mActionsContainerBackground.getRight() * (-1) : ScreenshotView.this.mDisplayMetrics.widthPixels;
            float abs = Math.abs(right - translationX);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$SwipeDismissHandler$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ScreenshotView.SwipeDismissHandler.this.lambda$createSwipeDismissAnimation$0(translationX, right, valueAnimator);
                }
            });
            ofFloat.setDuration(abs / Math.abs(min));
            return ofFloat;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createSwipeDismissAnimation$0(float f, float f2, ValueAnimator valueAnimator) {
            ScreenshotView.this.mScreenshotStatic.setTranslationX(MathUtils.lerp(f, f2, valueAnimator.getAnimatedFraction()));
            ScreenshotView.this.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
        }

        private ValueAnimator createSwipeReturnAnimation() {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            final float translationX = ScreenshotView.this.mScreenshotStatic.getTranslationX();
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.screenshot.ScreenshotView$SwipeDismissHandler$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ScreenshotView.SwipeDismissHandler.this.lambda$createSwipeReturnAnimation$1(translationX, r3, valueAnimator);
                }
            });
            return ofFloat;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$createSwipeReturnAnimation$1(float f, float f2, ValueAnimator valueAnimator) {
            ScreenshotView.this.mScreenshotStatic.setTranslationX(MathUtils.lerp(f, f2, valueAnimator.getAnimatedFraction()));
        }
    }
}
