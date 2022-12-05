package com.android.systemui.accessibility.floatingmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.internal.accessibility.dialog.AccessibilityTarget;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class AccessibilityFloatingMenuView extends FrameLayout implements RecyclerView.OnItemTouchListener {
    private final AccessibilityTargetAdapter mAdapter;
    private int mAlignment;
    @VisibleForTesting
    final WindowManager.LayoutParams mCurrentLayoutParams;
    private int mDownX;
    private int mDownY;
    @VisibleForTesting
    final ValueAnimator mDragAnimator;
    private final ValueAnimator mFadeOutAnimator;
    private float mFadeOutValue;
    private int mIconHeight;
    private int mIconWidth;
    private boolean mImeVisibility;
    private int mInset;
    private boolean mIsDownInEnlargedTouchArea;
    private boolean mIsDragging;
    private boolean mIsFadeEffectEnabled;
    private boolean mIsShowing;
    private final Configuration mLastConfiguration;
    private final RecyclerView mListView;
    private int mMargin;
    private Optional<OnDragEndListener> mOnDragEndListener;
    private int mPadding;
    private final Position mPosition;
    private float mRadius;
    private int mRadiusType;
    private int mRelativeToPointerDownX;
    private int mRelativeToPointerDownY;
    private int mScreenHeight;
    private int mScreenWidth;
    @VisibleForTesting
    int mShapeType;
    private int mSizeType;
    private float mSquareScaledTouchSlop;
    private final List<AccessibilityTarget> mTargets;
    private int mTemporaryShapeType;
    private final Handler mUiHandler;
    private final WindowManager mWindowManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnDragEndListener {
        void onDragEnd(Position position);
    }

    private float[] createRadii(float f, int i) {
        return i == 0 ? new float[]{f, f, 0.0f, 0.0f, 0.0f, 0.0f, f, f} : i == 2 ? new float[]{0.0f, 0.0f, f, f, f, f, 0.0f, 0.0f} : new float[]{f, f, f, f, f, f, f, f};
    }

    private boolean isMovingTowardsScreenEdge(int i, int i2, int i3) {
        if (i != 1 || i2 <= i3) {
            return i == 0 && i3 > i2;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int transformToAlignment(float f) {
        return f < 0.5f ? 0 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public AccessibilityFloatingMenuView(Context context, Position position) {
        this(context, position, new RecyclerView(context));
    }

    @VisibleForTesting
    AccessibilityFloatingMenuView(Context context, Position position, RecyclerView recyclerView) {
        super(context);
        this.mIsDragging = false;
        this.mSizeType = 0;
        this.mShapeType = 0;
        this.mOnDragEndListener = Optional.empty();
        ArrayList arrayList = new ArrayList();
        this.mTargets = arrayList;
        this.mListView = recyclerView;
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mLastConfiguration = new Configuration(getResources().getConfiguration());
        this.mAdapter = new AccessibilityTargetAdapter(arrayList);
        this.mUiHandler = createUiHandler();
        this.mPosition = position;
        int transformToAlignment = transformToAlignment(position.getPercentageX());
        this.mAlignment = transformToAlignment;
        this.mRadiusType = transformToAlignment == 1 ? 0 : 2;
        updateDimensions();
        this.mCurrentLayoutParams = createDefaultLayoutParams();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, this.mFadeOutValue);
        this.mFadeOutAnimator = ofFloat;
        ofFloat.setDuration(1000L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AccessibilityFloatingMenuView.this.lambda$new$0(valueAnimator);
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mDragAnimator = ofFloat2;
        ofFloat2.setDuration(150L);
        ofFloat2.setInterpolator(new OvershootInterpolator());
        ofFloat2.addListener(new AnonymousClass1());
        initListView();
        updateStrokeWith(getResources().getConfiguration().uiMode, this.mAlignment);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ValueAnimator valueAnimator) {
        setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends AnimatorListenerAdapter {
        AnonymousClass1() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            AccessibilityFloatingMenuView.this.mPosition.update(AccessibilityFloatingMenuView.this.transformCurrentPercentageXToEdge(), AccessibilityFloatingMenuView.this.calculateCurrentPercentageY());
            AccessibilityFloatingMenuView accessibilityFloatingMenuView = AccessibilityFloatingMenuView.this;
            accessibilityFloatingMenuView.mAlignment = accessibilityFloatingMenuView.transformToAlignment(accessibilityFloatingMenuView.mPosition.getPercentageX());
            AccessibilityFloatingMenuView accessibilityFloatingMenuView2 = AccessibilityFloatingMenuView.this;
            accessibilityFloatingMenuView2.updateLocationWith(accessibilityFloatingMenuView2.mPosition);
            AccessibilityFloatingMenuView accessibilityFloatingMenuView3 = AccessibilityFloatingMenuView.this;
            accessibilityFloatingMenuView3.updateInsetWith(accessibilityFloatingMenuView3.getResources().getConfiguration().uiMode, AccessibilityFloatingMenuView.this.mAlignment);
            AccessibilityFloatingMenuView accessibilityFloatingMenuView4 = AccessibilityFloatingMenuView.this;
            accessibilityFloatingMenuView4.mRadiusType = accessibilityFloatingMenuView4.mAlignment == 1 ? 0 : 2;
            AccessibilityFloatingMenuView accessibilityFloatingMenuView5 = AccessibilityFloatingMenuView.this;
            accessibilityFloatingMenuView5.updateRadiusWith(accessibilityFloatingMenuView5.mSizeType, AccessibilityFloatingMenuView.this.mRadiusType, AccessibilityFloatingMenuView.this.mTargets.size());
            AccessibilityFloatingMenuView.this.fadeOut();
            AccessibilityFloatingMenuView.this.mOnDragEndListener.ifPresent(new Consumer() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AccessibilityFloatingMenuView.AnonymousClass1.this.lambda$onAnimationEnd$0((AccessibilityFloatingMenuView.OnDragEndListener) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAnimationEnd$0(OnDragEndListener onDragEndListener) {
            onDragEndListener.onDragEnd(AccessibilityFloatingMenuView.this.mPosition);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0018, code lost:
        if (r7 != 3) goto L8;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        int rawX = (int) motionEvent.getRawX();
        int rawY = (int) motionEvent.getRawY();
        int action = motionEvent.getAction();
        if (action == 0) {
            fadeIn();
            this.mDownX = rawX;
            this.mDownY = rawY;
            WindowManager.LayoutParams layoutParams = this.mCurrentLayoutParams;
            this.mRelativeToPointerDownX = layoutParams.x - rawX;
            this.mRelativeToPointerDownY = layoutParams.y - rawY;
            this.mListView.animate().translationX(0.0f);
        } else {
            if (action != 1) {
                if (action == 2) {
                    if (this.mIsDragging || hasExceededTouchSlop(this.mDownX, this.mDownY, rawX, rawY)) {
                        if (!this.mIsDragging) {
                            this.mIsDragging = true;
                            setRadius(this.mRadius, 1);
                            setInset(0, 0);
                        }
                        this.mTemporaryShapeType = isMovingTowardsScreenEdge(this.mAlignment, rawX, this.mDownX) ? 1 : 0;
                        int i = rawX + this.mRelativeToPointerDownX;
                        int i2 = rawY + this.mRelativeToPointerDownY;
                        this.mCurrentLayoutParams.x = MathUtils.constrain(i, getMinWindowX(), getMaxWindowX());
                        this.mCurrentLayoutParams.y = MathUtils.constrain(i2, 0, getMaxWindowY());
                        this.mWindowManager.updateViewLayout(this, this.mCurrentLayoutParams);
                    }
                }
            }
            if (this.mIsDragging) {
                this.mIsDragging = false;
                int minWindowX = getMinWindowX();
                int maxWindowX = getMaxWindowX();
                WindowManager.LayoutParams layoutParams2 = this.mCurrentLayoutParams;
                if (layoutParams2.x > (minWindowX + maxWindowX) / 2) {
                    minWindowX = maxWindowX;
                }
                snapToLocation(minWindowX, layoutParams2.y);
                setShapeType(this.mTemporaryShapeType);
                return true;
            } else if (!isOvalShape()) {
                setShapeType(0);
                return true;
            } else {
                fadeOut();
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void show() {
        if (isShowing()) {
            return;
        }
        this.mIsShowing = true;
        this.mWindowManager.addView(this, this.mCurrentLayoutParams);
        setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda2
            @Override // android.view.View.OnApplyWindowInsetsListener
            public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                WindowInsets lambda$show$1;
                lambda$show$1 = AccessibilityFloatingMenuView.this.lambda$show$1(view, windowInsets);
                return lambda$show$1;
            }
        });
        setSystemGestureExclusion();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ WindowInsets lambda$show$1(View view, WindowInsets windowInsets) {
        return onWindowInsetsApplied(windowInsets);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hide() {
        if (!isShowing()) {
            return;
        }
        this.mIsShowing = false;
        this.mWindowManager.removeView(this);
        setOnApplyWindowInsetsListener(null);
        setSystemGestureExclusion();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isShowing() {
        return this.mIsShowing;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isOvalShape() {
        return this.mShapeType == 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTargetsChanged(List<AccessibilityTarget> list) {
        fadeIn();
        this.mTargets.clear();
        this.mTargets.addAll(list);
        onEnabledFeaturesChanged();
        updateRadiusWith(this.mSizeType, this.mRadiusType, this.mTargets.size());
        updateScrollModeWith(hasExceededMaxLayoutHeight());
        setSystemGestureExclusion();
        fadeOut();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setSizeType(int i) {
        fadeIn();
        this.mSizeType = i;
        updateItemViewWith(i);
        updateRadiusWith(i, this.mRadiusType, this.mTargets.size());
        updateLocationWith(this.mPosition);
        updateScrollModeWith(hasExceededMaxLayoutHeight());
        updateOffsetWith(this.mShapeType, this.mAlignment);
        setSystemGestureExclusion();
        fadeOut();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setShapeType(int i) {
        fadeIn();
        this.mShapeType = i;
        updateOffsetWith(i, this.mAlignment);
        setOnTouchListener(i == 0 ? null : new View.OnTouchListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda3
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                boolean lambda$setShapeType$2;
                lambda$setShapeType$2 = AccessibilityFloatingMenuView.this.lambda$setShapeType$2(view, motionEvent);
                return lambda$setShapeType$2;
            }
        });
        fadeOut();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$setShapeType$2(View view, MotionEvent motionEvent) {
        return onTouched(motionEvent);
    }

    public void setOnDragEndListener(OnDragEndListener onDragEndListener) {
        this.mOnDragEndListener = Optional.ofNullable(onDragEndListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void startTranslateXAnimation() {
        fadeIn();
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, this.mAlignment == 1 ? 0.5f : -0.5f, 1, 0.0f, 1, 0.0f);
        translateAnimation.setDuration(600L);
        translateAnimation.setRepeatMode(2);
        translateAnimation.setInterpolator(new OvershootInterpolator());
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setStartOffset(600L);
        this.mListView.startAnimation(translateAnimation);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void stopTranslateXAnimation() {
        this.mListView.clearAnimation();
        fadeOut();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getWindowLocationOnScreen() {
        WindowManager.LayoutParams layoutParams = this.mCurrentLayoutParams;
        int i = layoutParams.x;
        int i2 = layoutParams.y;
        return new Rect(i, i2, getWindowWidth() + i, getWindowHeight() + i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateOpacityWith(boolean z, float f) {
        this.mIsFadeEffectEnabled = z;
        this.mFadeOutValue = f;
        this.mFadeOutAnimator.cancel();
        float f2 = 1.0f;
        this.mFadeOutAnimator.setFloatValues(1.0f, this.mFadeOutValue);
        if (this.mIsFadeEffectEnabled) {
            f2 = this.mFadeOutValue;
        }
        setAlpha(f2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onEnabledFeaturesChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void fadeIn() {
        if (!this.mIsFadeEffectEnabled) {
            return;
        }
        this.mFadeOutAnimator.cancel();
        this.mUiHandler.removeCallbacksAndMessages(null);
        this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                AccessibilityFloatingMenuView.this.lambda$fadeIn$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeIn$3() {
        setAlpha(1.0f);
    }

    @VisibleForTesting
    void fadeOut() {
        if (!this.mIsFadeEffectEnabled) {
            return;
        }
        this.mUiHandler.postDelayed(new Runnable() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                AccessibilityFloatingMenuView.this.lambda$fadeOut$4();
            }
        }, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeOut$4() {
        this.mFadeOutAnimator.start();
    }

    private boolean onTouched(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int marginStartEndWith = getMarginStartEndWith(this.mLastConfiguration);
        Rect rect = new Rect(marginStartEndWith, this.mMargin, getLayoutWidth() + marginStartEndWith, this.mMargin + getLayoutHeight());
        if (action == 0 && rect.contains(x, y)) {
            this.mIsDownInEnlargedTouchArea = true;
        }
        if (!this.mIsDownInEnlargedTouchArea) {
            return false;
        }
        if (action == 1 || action == 3) {
            this.mIsDownInEnlargedTouchArea = false;
        }
        int i = this.mMargin;
        motionEvent.setLocation(x - i, y - i);
        return this.mListView.dispatchTouchEvent(motionEvent);
    }

    private WindowInsets onWindowInsetsApplied(WindowInsets windowInsets) {
        boolean isVisible = windowInsets.isVisible(WindowInsets.Type.ime());
        if (isVisible != this.mImeVisibility) {
            this.mImeVisibility = isVisible;
            updateLocationWith(this.mPosition);
        }
        return windowInsets;
    }

    private boolean hasExceededTouchSlop(int i, int i2, int i3, int i4) {
        return MathUtils.sq((float) (i3 - i)) + MathUtils.sq((float) (i4 - i2)) > this.mSquareScaledTouchSlop;
    }

    private void setRadius(float f, int i) {
        getMenuGradientDrawable().setCornerRadii(createRadii(f, i));
    }

    private Handler createUiHandler() {
        Looper myLooper = Looper.myLooper();
        Objects.requireNonNull(myLooper, "looper must not be null");
        return new Handler(myLooper);
    }

    private void updateDimensions() {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        this.mScreenWidth = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
        this.mMargin = resources.getDimensionPixelSize(R$dimen.accessibility_floating_menu_margin);
        this.mInset = resources.getDimensionPixelSize(R$dimen.accessibility_floating_menu_stroke_inset);
        this.mSquareScaledTouchSlop = MathUtils.sq(ViewConfiguration.get(getContext()).getScaledTouchSlop());
        updateItemViewDimensionsWith(this.mSizeType);
    }

    private void updateItemViewDimensionsWith(int i) {
        int i2;
        int i3;
        Resources resources = getResources();
        if (i == 0) {
            i2 = R$dimen.accessibility_floating_menu_small_padding;
        } else {
            i2 = R$dimen.accessibility_floating_menu_large_padding;
        }
        this.mPadding = resources.getDimensionPixelSize(i2);
        if (i == 0) {
            i3 = R$dimen.accessibility_floating_menu_small_width_height;
        } else {
            i3 = R$dimen.accessibility_floating_menu_large_width_height;
        }
        int dimensionPixelSize = resources.getDimensionPixelSize(i3);
        this.mIconWidth = dimensionPixelSize;
        this.mIconHeight = dimensionPixelSize;
    }

    private void updateItemViewWith(int i) {
        updateItemViewDimensionsWith(i);
        this.mAdapter.setItemPadding(this.mPadding);
        this.mAdapter.setIconWidthHeight(this.mIconWidth);
        this.mAdapter.notifyDataSetChanged();
    }

    private void initListView() {
        Drawable drawable = getContext().getDrawable(R$drawable.accessibility_floating_menu_background);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.mListView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        this.mListView.setBackground(new InstantInsetLayerDrawable(new Drawable[]{drawable}));
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setLayoutManager(linearLayoutManager);
        this.mListView.addOnItemTouchListener(this);
        this.mListView.animate().setInterpolator(new OvershootInterpolator());
        this.mListView.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this.mListView) { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.2
            @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
            public AccessibilityDelegateCompat getItemDelegate() {
                return new ItemDelegateCompat(this, AccessibilityFloatingMenuView.this);
            }
        });
        updateListViewWith(this.mLastConfiguration);
        addView(this.mListView);
    }

    private void updateListViewWith(Configuration configuration) {
        updateMarginWith(configuration);
        this.mListView.setElevation(getResources().getDimensionPixelSize(R$dimen.accessibility_floating_menu_elevation));
    }

    private WindowManager.LayoutParams createDefaultLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2024, 520, -3);
        layoutParams.receiveInsetsIgnoringZOrder = true;
        layoutParams.windowAnimations = 16973827;
        layoutParams.gravity = 8388659;
        layoutParams.x = this.mAlignment == 1 ? getMaxWindowX() : getMinWindowX();
        layoutParams.y = Math.max(0, ((int) (this.mPosition.getPercentageY() * getMaxWindowY())) - getInterval());
        updateAccessibilityTitle(layoutParams);
        return layoutParams;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mLastConfiguration.setTo(configuration);
        if ((configuration.diff(this.mLastConfiguration) & 4) != 0) {
            updateAccessibilityTitle(this.mCurrentLayoutParams);
        }
        updateDimensions();
        updateListViewWith(configuration);
        updateItemViewWith(this.mSizeType);
        updateColor();
        updateStrokeWith(configuration.uiMode, this.mAlignment);
        updateLocationWith(this.mPosition);
        updateRadiusWith(this.mSizeType, this.mRadiusType, this.mTargets.size());
        updateScrollModeWith(hasExceededMaxLayoutHeight());
        setSystemGestureExclusion();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public void snapToLocation(final int i, final int i2) {
        this.mDragAnimator.cancel();
        this.mDragAnimator.removeAllUpdateListeners();
        this.mDragAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                AccessibilityFloatingMenuView.this.lambda$snapToLocation$5(i, i2, valueAnimator);
            }
        });
        this.mDragAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: onDragAnimationUpdate */
    public void lambda$snapToLocation$5(ValueAnimator valueAnimator, int i, int i2) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float f = 1.0f - floatValue;
        WindowManager.LayoutParams layoutParams = this.mCurrentLayoutParams;
        layoutParams.x = (int) ((layoutParams.x * f) + (i * floatValue));
        layoutParams.y = (int) ((f * layoutParams.y) + (floatValue * i2));
        this.mWindowManager.updateViewLayout(this, layoutParams);
    }

    private int getMinWindowX() {
        return -getMarginStartEndWith(this.mLastConfiguration);
    }

    private int getMaxWindowX() {
        return (this.mScreenWidth - getMarginStartEndWith(this.mLastConfiguration)) - getLayoutWidth();
    }

    private int getMaxWindowY() {
        return this.mScreenHeight - getWindowHeight();
    }

    private InstantInsetLayerDrawable getMenuLayerDrawable() {
        return (InstantInsetLayerDrawable) this.mListView.getBackground();
    }

    private GradientDrawable getMenuGradientDrawable() {
        return (GradientDrawable) getMenuLayerDrawable().getDrawable(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLocationWith(Position position) {
        int transformToAlignment = transformToAlignment(position.getPercentageX());
        this.mCurrentLayoutParams.x = transformToAlignment == 1 ? getMaxWindowX() : getMinWindowX();
        this.mCurrentLayoutParams.y = Math.max(0, ((int) (position.getPercentageY() * getMaxWindowY())) - getInterval());
        this.mWindowManager.updateViewLayout(this, this.mCurrentLayoutParams);
    }

    private int getInterval() {
        if (!this.mImeVisibility) {
            return 0;
        }
        int i = this.mScreenHeight - this.mWindowManager.getCurrentWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.ime() | WindowInsets.Type.navigationBars()).bottom;
        int windowHeight = this.mCurrentLayoutParams.y + getWindowHeight();
        if (windowHeight <= i) {
            return 0;
        }
        return windowHeight - i;
    }

    private void updateMarginWith(Configuration configuration) {
        int marginStartEndWith = getMarginStartEndWith(configuration);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mListView.getLayoutParams();
        int i = this.mMargin;
        layoutParams.setMargins(marginStartEndWith, i, marginStartEndWith, i);
        this.mListView.setLayoutParams(layoutParams);
    }

    private void updateOffsetWith(int i, int i2) {
        float layoutWidth = getLayoutWidth() / 2.0f;
        if (i == 0) {
            layoutWidth = 0.0f;
        }
        ViewPropertyAnimator animate = this.mListView.animate();
        if (i2 != 1) {
            layoutWidth = -layoutWidth;
        }
        animate.translationX(layoutWidth);
    }

    private void updateScrollModeWith(boolean z) {
        this.mListView.setOverScrollMode(z ? 0 : 2);
    }

    private void updateColor() {
        getMenuGradientDrawable().setColor(getResources().getColor(R$color.accessibility_floating_menu_background));
    }

    private void updateStrokeWith(int i, int i2) {
        updateInsetWith(i, i2);
        int i3 = 0;
        boolean z = (i & 48) == 32;
        Resources resources = getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R$dimen.accessibility_floating_menu_stroke_width);
        if (z) {
            i3 = dimensionPixelSize;
        }
        getMenuGradientDrawable().setStroke(i3, resources.getColor(R$color.accessibility_floating_menu_stroke_dark));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRadiusWith(int i, int i2, int i3) {
        float dimensionPixelSize = getResources().getDimensionPixelSize(getRadiusResId(i, i3));
        this.mRadius = dimensionPixelSize;
        setRadius(dimensionPixelSize, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateInsetWith(int i, int i2) {
        int i3 = 0;
        int i4 = (i & 48) == 32 ? this.mInset : 0;
        int i5 = i2 == 0 ? i4 : 0;
        if (i2 == 1) {
            i3 = i4;
        }
        setInset(i5, i3);
    }

    private void updateAccessibilityTitle(WindowManager.LayoutParams layoutParams) {
        layoutParams.accessibilityTitle = getResources().getString(17039577);
    }

    private void setInset(int i, int i2) {
        InstantInsetLayerDrawable menuLayerDrawable = getMenuLayerDrawable();
        if (menuLayerDrawable.getLayerInsetLeft(0) == i && menuLayerDrawable.getLayerInsetRight(0) == i2) {
            return;
        }
        menuLayerDrawable.setLayerInset(0, i, 0, i2, 0);
    }

    @VisibleForTesting
    boolean hasExceededMaxLayoutHeight() {
        return calculateActualLayoutHeight() > getMaxLayoutHeight();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float transformCurrentPercentageXToEdge() {
        return ((double) calculateCurrentPercentageX()) < 0.5d ? 0.0f : 1.0f;
    }

    private float calculateCurrentPercentageX() {
        return this.mCurrentLayoutParams.x / getMaxWindowX();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public float calculateCurrentPercentageY() {
        return this.mCurrentLayoutParams.y / getMaxWindowY();
    }

    private int calculateActualLayoutHeight() {
        return ((this.mPadding + this.mIconHeight) * this.mTargets.size()) + this.mPadding;
    }

    private int getMarginStartEndWith(Configuration configuration) {
        if (configuration == null || configuration.orientation != 1) {
            return 0;
        }
        return this.mMargin;
    }

    private int getRadiusResId(int i, int i2) {
        if (i == 0) {
            return getSmallSizeResIdWith(i2);
        }
        return getLargeSizeResIdWith(i2);
    }

    private int getSmallSizeResIdWith(int i) {
        if (i > 1) {
            return R$dimen.accessibility_floating_menu_small_multiple_radius;
        }
        return R$dimen.accessibility_floating_menu_small_single_radius;
    }

    private int getLargeSizeResIdWith(int i) {
        if (i > 1) {
            return R$dimen.accessibility_floating_menu_large_multiple_radius;
        }
        return R$dimen.accessibility_floating_menu_large_single_radius;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @VisibleForTesting
    public Rect getAvailableBounds() {
        return new Rect(0, 0, this.mScreenWidth - getWindowWidth(), this.mScreenHeight - getWindowHeight());
    }

    private int getMaxLayoutHeight() {
        return this.mScreenHeight - (this.mMargin * 2);
    }

    private int getLayoutWidth() {
        return (this.mPadding * 2) + this.mIconWidth;
    }

    private int getLayoutHeight() {
        return Math.min(getMaxLayoutHeight(), calculateActualLayoutHeight());
    }

    private int getWindowWidth() {
        return (getMarginStartEndWith(this.mLastConfiguration) * 2) + getLayoutWidth();
    }

    private int getWindowHeight() {
        return Math.min(this.mScreenHeight, (this.mMargin * 2) + getLayoutHeight());
    }

    private void setSystemGestureExclusion() {
        final Rect rect = new Rect(0, 0, getWindowWidth(), getWindowHeight());
        post(new Runnable() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                AccessibilityFloatingMenuView.this.lambda$setSystemGestureExclusion$6(rect);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setSystemGestureExclusion$6(Rect rect) {
        List emptyList;
        if (this.mIsShowing) {
            emptyList = Collections.singletonList(rect);
        } else {
            emptyList = Collections.emptyList();
        }
        setSystemGestureExclusionRects(emptyList);
    }
}
