package com.android.systemui.accessibility.floatingmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.ViewPropertyAnimator;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.internal.accessibility.dialog.AccessibilityTarget;
import com.android.systemui.C1893R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AccessibilityFloatingMenuView extends FrameLayout implements RecyclerView.OnItemTouchListener {
    private static final int ANIMATION_DURATION_MS = 600;
    private static final int ANIMATION_START_OFFSET = 600;
    private static final float ANIMATION_TO_X_VALUE = 0.5f;
    private static final int FADE_EFFECT_DURATION_MS = 3000;
    private static final int FADE_OUT_DURATION_MS = 1000;
    private static final int INDEX_MENU_ITEM = 0;
    private static final int MIN_WINDOW_Y = 0;
    private static final int SNAP_TO_LOCATION_DURATION_MS = 150;
    private final AccessibilityTargetAdapter mAdapter;
    /* access modifiers changed from: private */
    public int mAlignment;
    final WindowManager.LayoutParams mCurrentLayoutParams;
    private int mDisplayHeight;
    private final Rect mDisplayInsetsRect;
    private int mDisplayWidth;
    private int mDownX;
    private int mDownY;
    final ValueAnimator mDragAnimator;
    private final ValueAnimator mFadeOutAnimator;
    private float mFadeOutValue;
    private int mIconHeight;
    private int mIconWidth;
    private final Rect mImeInsetsRect;
    private int mInset;
    private boolean mIsDownInEnlargedTouchArea;
    private boolean mIsDragging;
    private boolean mIsFadeEffectEnabled;
    private boolean mIsShowing;
    private final Configuration mLastConfiguration;
    private final RecyclerView mListView;
    private int mMargin;
    /* access modifiers changed from: private */
    public Optional<OnDragEndListener> mOnDragEndListener;
    private int mPadding;
    /* access modifiers changed from: private */
    public final Position mPosition;
    private float mRadius;
    /* access modifiers changed from: private */
    public int mRadiusType;
    private int mRelativeToPointerDownX;
    private int mRelativeToPointerDownY;
    int mShapeType;
    /* access modifiers changed from: private */
    public int mSizeType;
    private float mSquareScaledTouchSlop;
    /* access modifiers changed from: private */
    public final List<AccessibilityTarget> mTargets;
    private int mTemporaryShapeType;
    private final Handler mUiHandler;
    private final WindowManager mWindowManager;

    @Retention(RetentionPolicy.SOURCE)
    @interface Alignment {
        public static final int LEFT = 0;
        public static final int RIGHT = 1;
    }

    interface OnDragEndListener {
        void onDragEnd(Position position);
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface RadiusType {
        public static final int LEFT_HALF_OVAL = 0;
        public static final int OVAL = 1;
        public static final int RIGHT_HALF_OVAL = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface ShapeType {
        public static final int HALF_OVAL = 1;
        public static final int OVAL = 0;
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface SizeType {
        public static final int LARGE = 1;
        public static final int SMALL = 0;
    }

    private float[] createRadii(float f, int i) {
        if (i == 0) {
            return new float[]{f, f, 0.0f, 0.0f, 0.0f, 0.0f, f, f};
        } else if (i == 2) {
            return new float[]{0.0f, 0.0f, f, f, f, f, 0.0f, 0.0f};
        } else {
            return new float[]{f, f, f, f, f, f, f, f};
        }
    }

    private int getLargeSizeResIdWith(int i) {
        return i > 1 ? C1893R.dimen.accessibility_floating_menu_large_multiple_radius : C1893R.dimen.accessibility_floating_menu_large_single_radius;
    }

    private int getSmallSizeResIdWith(int i) {
        return i > 1 ? C1893R.dimen.accessibility_floating_menu_small_multiple_radius : C1893R.dimen.accessibility_floating_menu_small_single_radius;
    }

    private boolean isMovingTowardsScreenEdge(int i, int i2, int i3) {
        if (i != 1 || i2 <= i3) {
            return i == 0 && i3 > i2;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public int transformToAlignment(float f) {
        return f < 0.5f ? 0 : 1;
    }

    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public AccessibilityFloatingMenuView(Context context, Position position) {
        this(context, position, new RecyclerView(context));
    }

    AccessibilityFloatingMenuView(Context context, Position position, RecyclerView recyclerView) {
        super(context);
        this.mIsDragging = false;
        this.mSizeType = 0;
        this.mShapeType = 0;
        this.mDisplayInsetsRect = new Rect();
        this.mImeInsetsRect = new Rect();
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
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{1.0f, this.mFadeOutValue});
        this.mFadeOutAnimator = ofFloat;
        ofFloat.setDuration(1000);
        ofFloat.addUpdateListener(new AccessibilityFloatingMenuView$$ExternalSyntheticLambda1(this));
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mDragAnimator = ofFloat2;
        ofFloat2.setDuration(150);
        ofFloat2.setInterpolator(new OvershootInterpolator());
        ofFloat2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                AccessibilityFloatingMenuView.this.mPosition.update(AccessibilityFloatingMenuView.this.transformCurrentPercentageXToEdge(), AccessibilityFloatingMenuView.this.calculateCurrentPercentageY());
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = AccessibilityFloatingMenuView.this;
                int unused = accessibilityFloatingMenuView.mAlignment = accessibilityFloatingMenuView.transformToAlignment(accessibilityFloatingMenuView.mPosition.getPercentageX());
                AccessibilityFloatingMenuView accessibilityFloatingMenuView2 = AccessibilityFloatingMenuView.this;
                accessibilityFloatingMenuView2.updateLocationWith(accessibilityFloatingMenuView2.mPosition);
                AccessibilityFloatingMenuView accessibilityFloatingMenuView3 = AccessibilityFloatingMenuView.this;
                accessibilityFloatingMenuView3.updateInsetWith(accessibilityFloatingMenuView3.getResources().getConfiguration().uiMode, AccessibilityFloatingMenuView.this.mAlignment);
                AccessibilityFloatingMenuView accessibilityFloatingMenuView4 = AccessibilityFloatingMenuView.this;
                int unused2 = accessibilityFloatingMenuView4.mRadiusType = accessibilityFloatingMenuView4.mAlignment == 1 ? 0 : 2;
                AccessibilityFloatingMenuView accessibilityFloatingMenuView5 = AccessibilityFloatingMenuView.this;
                accessibilityFloatingMenuView5.updateRadiusWith(accessibilityFloatingMenuView5.mSizeType, AccessibilityFloatingMenuView.this.mRadiusType, AccessibilityFloatingMenuView.this.mTargets.size());
                AccessibilityFloatingMenuView.this.fadeOut();
                AccessibilityFloatingMenuView.this.mOnDragEndListener.ifPresent(new AccessibilityFloatingMenuView$1$$ExternalSyntheticLambda0(this));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onAnimationEnd$0$com-android-systemui-accessibility-floatingmenu-AccessibilityFloatingMenuView$1 */
            public /* synthetic */ void mo30067x15544f97(OnDragEndListener onDragEndListener) {
                onDragEndListener.onDragEnd(AccessibilityFloatingMenuView.this.mPosition);
            }
        });
        initListView();
        updateStrokeWith(getResources().getConfiguration().uiMode, this.mAlignment);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-accessibility-floatingmenu-AccessibilityFloatingMenuView */
    public /* synthetic */ void mo30051xa8eaac1a(ValueAnimator valueAnimator) {
        setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0018, code lost:
        if (r6 != 3) goto L_0x00c2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onInterceptTouchEvent(androidx.recyclerview.widget.RecyclerView r5, android.view.MotionEvent r6) {
        /*
            r4 = this;
            float r5 = r6.getRawX()
            int r5 = (int) r5
            float r0 = r6.getRawY()
            int r0 = (int) r0
            int r6 = r6.getAction()
            r1 = 0
            if (r6 == 0) goto L_0x009f
            r2 = 2
            r3 = 1
            if (r6 == r3) goto L_0x006c
            if (r6 == r2) goto L_0x001c
            r5 = 3
            if (r6 == r5) goto L_0x006c
            goto L_0x00c2
        L_0x001c:
            boolean r6 = r4.mIsDragging
            if (r6 != 0) goto L_0x002a
            int r6 = r4.mDownX
            int r2 = r4.mDownY
            boolean r6 = r4.hasExceededTouchSlop(r6, r2, r5, r0)
            if (r6 == 0) goto L_0x00c2
        L_0x002a:
            boolean r6 = r4.mIsDragging
            if (r6 != 0) goto L_0x0038
            r4.mIsDragging = r3
            float r6 = r4.mRadius
            r4.setRadius(r6, r3)
            r4.setInset(r1, r1)
        L_0x0038:
            int r6 = r4.mAlignment
            int r2 = r4.mDownX
            boolean r6 = r4.isMovingTowardsScreenEdge(r6, r5, r2)
            r4.mTemporaryShapeType = r6
            int r6 = r4.mRelativeToPointerDownX
            int r5 = r5 + r6
            int r6 = r4.mRelativeToPointerDownY
            int r0 = r0 + r6
            android.view.WindowManager$LayoutParams r6 = r4.mCurrentLayoutParams
            int r2 = r4.getMinWindowX()
            int r3 = r4.getMaxWindowX()
            int r5 = android.util.MathUtils.constrain(r5, r2, r3)
            r6.x = r5
            android.view.WindowManager$LayoutParams r5 = r4.mCurrentLayoutParams
            int r6 = r4.getMaxWindowY()
            int r6 = android.util.MathUtils.constrain(r0, r1, r6)
            r5.y = r6
            android.view.WindowManager r5 = r4.mWindowManager
            android.view.WindowManager$LayoutParams r6 = r4.mCurrentLayoutParams
            r5.updateViewLayout(r4, r6)
            goto L_0x00c2
        L_0x006c:
            boolean r5 = r4.mIsDragging
            if (r5 == 0) goto L_0x0091
            r4.mIsDragging = r1
            int r5 = r4.getMinWindowX()
            int r6 = r4.getMaxWindowX()
            android.view.WindowManager$LayoutParams r0 = r4.mCurrentLayoutParams
            int r0 = r0.x
            int r1 = r5 + r6
            int r1 = r1 / r2
            if (r0 <= r1) goto L_0x0084
            r5 = r6
        L_0x0084:
            android.view.WindowManager$LayoutParams r6 = r4.mCurrentLayoutParams
            int r6 = r6.y
            r4.snapToLocation(r5, r6)
            int r5 = r4.mTemporaryShapeType
            r4.setShapeType(r5)
            return r3
        L_0x0091:
            boolean r5 = r4.isOvalShape()
            if (r5 != 0) goto L_0x009b
            r4.setShapeType(r1)
            return r3
        L_0x009b:
            r4.fadeOut()
            goto L_0x00c2
        L_0x009f:
            r4.fadeIn()
            r4.mDownX = r5
            r4.mDownY = r0
            android.view.WindowManager$LayoutParams r5 = r4.mCurrentLayoutParams
            int r5 = r5.x
            int r6 = r4.mDownX
            int r5 = r5 - r6
            r4.mRelativeToPointerDownX = r5
            android.view.WindowManager$LayoutParams r5 = r4.mCurrentLayoutParams
            int r5 = r5.y
            int r6 = r4.mDownY
            int r5 = r5 - r6
            r4.mRelativeToPointerDownY = r5
            androidx.recyclerview.widget.RecyclerView r4 = r4.mListView
            android.view.ViewPropertyAnimator r4 = r4.animate()
            r5 = 0
            r4.translationX(r5)
        L_0x00c2:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView.onInterceptTouchEvent(androidx.recyclerview.widget.RecyclerView, android.view.MotionEvent):boolean");
    }

    /* access modifiers changed from: package-private */
    public void show() {
        if (!isShowing()) {
            this.mIsShowing = true;
            this.mWindowManager.addView(this, this.mCurrentLayoutParams);
            setOnApplyWindowInsetsListener(new AccessibilityFloatingMenuView$$ExternalSyntheticLambda2(this));
            setSystemGestureExclusion();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$show$1$com-android-systemui-accessibility-floatingmenu-AccessibilityFloatingMenuView */
    public /* synthetic */ WindowInsets mo30054xebc882d2(View view, WindowInsets windowInsets) {
        return onWindowInsetsApplied(windowInsets);
    }

    /* access modifiers changed from: package-private */
    public void hide() {
        if (isShowing()) {
            this.mIsShowing = false;
            this.mWindowManager.removeView(this);
            setOnApplyWindowInsetsListener((View.OnApplyWindowInsetsListener) null);
            setSystemGestureExclusion();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isShowing() {
        return this.mIsShowing;
    }

    /* access modifiers changed from: package-private */
    public boolean isOvalShape() {
        return this.mShapeType == 0;
    }

    /* access modifiers changed from: package-private */
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

    /* access modifiers changed from: package-private */
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

    /* access modifiers changed from: package-private */
    public void setShapeType(int i) {
        AccessibilityFloatingMenuView$$ExternalSyntheticLambda3 accessibilityFloatingMenuView$$ExternalSyntheticLambda3;
        fadeIn();
        this.mShapeType = i;
        updateOffsetWith(i, this.mAlignment);
        if (i == 0) {
            accessibilityFloatingMenuView$$ExternalSyntheticLambda3 = null;
        } else {
            accessibilityFloatingMenuView$$ExternalSyntheticLambda3 = new AccessibilityFloatingMenuView$$ExternalSyntheticLambda3(this);
        }
        setOnTouchListener(accessibilityFloatingMenuView$$ExternalSyntheticLambda3);
        fadeOut();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setShapeType$2$com-android-systemui-accessibility-floatingmenu-AccessibilityFloatingMenuView */
    public /* synthetic */ boolean mo30052xab37714f(View view, MotionEvent motionEvent) {
        return onTouched(motionEvent);
    }

    public void setOnDragEndListener(OnDragEndListener onDragEndListener) {
        this.mOnDragEndListener = Optional.ofNullable(onDragEndListener);
    }

    /* access modifiers changed from: package-private */
    public void startTranslateXAnimation() {
        fadeIn();
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, this.mAlignment == 1 ? 0.5f : -0.5f, 1, 0.0f, 1, 0.0f);
        translateAnimation.setDuration(600);
        translateAnimation.setRepeatMode(2);
        translateAnimation.setInterpolator(new OvershootInterpolator());
        translateAnimation.setRepeatCount(-1);
        translateAnimation.setStartOffset(600);
        this.mListView.startAnimation(translateAnimation);
    }

    /* access modifiers changed from: package-private */
    public void stopTranslateXAnimation() {
        this.mListView.clearAnimation();
        fadeOut();
    }

    /* access modifiers changed from: package-private */
    public Rect getWindowLocationOnScreen() {
        int i = this.mCurrentLayoutParams.x;
        int i2 = this.mCurrentLayoutParams.y;
        return new Rect(i, i2, getWindowWidth() + i, getWindowHeight() + i2);
    }

    /* access modifiers changed from: package-private */
    public void updateOpacityWith(boolean z, float f) {
        this.mIsFadeEffectEnabled = z;
        this.mFadeOutValue = f;
        this.mFadeOutAnimator.cancel();
        float f2 = 1.0f;
        this.mFadeOutAnimator.setFloatValues(new float[]{1.0f, this.mFadeOutValue});
        if (this.mIsFadeEffectEnabled) {
            f2 = this.mFadeOutValue;
        }
        setAlpha(f2);
    }

    /* access modifiers changed from: package-private */
    public void onEnabledFeaturesChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    /* access modifiers changed from: package-private */
    public void fadeIn() {
        if (this.mIsFadeEffectEnabled) {
            this.mFadeOutAnimator.cancel();
            this.mUiHandler.removeCallbacksAndMessages((Object) null);
            this.mUiHandler.post(new AccessibilityFloatingMenuView$$ExternalSyntheticLambda5(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fadeIn$3$com-android-systemui-accessibility-floatingmenu-AccessibilityFloatingMenuView */
    public /* synthetic */ void mo30049x833039d8() {
        setAlpha(1.0f);
    }

    /* access modifiers changed from: package-private */
    public void fadeOut() {
        if (this.mIsFadeEffectEnabled) {
            this.mUiHandler.postDelayed(new AccessibilityFloatingMenuView$$ExternalSyntheticLambda4(this), 3000);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fadeOut$4$com-android-systemui-accessibility-floatingmenu-AccessibilityFloatingMenuView */
    public /* synthetic */ void mo30050x7f450d30() {
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
        motionEvent.setLocation((float) (x - i), (float) (y - i));
        return this.mListView.dispatchTouchEvent(motionEvent);
    }

    private WindowInsets onWindowInsetsApplied(WindowInsets windowInsets) {
        WindowMetrics currentWindowMetrics = this.mWindowManager.getCurrentWindowMetrics();
        if (!getDisplayInsets(currentWindowMetrics).toRect().equals(this.mDisplayInsetsRect)) {
            updateDisplaySizeWith(currentWindowMetrics);
            updateLocationWith(this.mPosition);
        }
        Rect rect = currentWindowMetrics.getWindowInsets().getInsets(WindowInsets.Type.ime()).toRect();
        if (!rect.equals(this.mImeInsetsRect)) {
            if (isImeVisible(rect)) {
                this.mImeInsetsRect.set(rect);
            } else {
                this.mImeInsetsRect.setEmpty();
            }
            updateLocationWith(this.mPosition);
        }
        return windowInsets;
    }

    private boolean isImeVisible(Rect rect) {
        return (rect.left == 0 && rect.top == 0 && rect.right == 0 && rect.bottom == 0) ? false : true;
    }

    private boolean hasExceededTouchSlop(int i, int i2, int i3, int i4) {
        return MathUtils.sq((float) (i3 - i)) + MathUtils.sq((float) (i4 - i2)) > this.mSquareScaledTouchSlop;
    }

    private void setRadius(float f, int i) {
        getMenuGradientDrawable().setCornerRadii(createRadii(f, i));
    }

    private Handler createUiHandler() {
        return new Handler((Looper) Objects.requireNonNull(Looper.myLooper(), "looper must not be null"));
    }

    private void updateDimensions() {
        Resources resources = getResources();
        updateDisplaySizeWith(this.mWindowManager.getCurrentWindowMetrics());
        this.mMargin = resources.getDimensionPixelSize(C1893R.dimen.accessibility_floating_menu_margin);
        this.mInset = resources.getDimensionPixelSize(C1893R.dimen.accessibility_floating_menu_stroke_inset);
        this.mSquareScaledTouchSlop = MathUtils.sq((float) ViewConfiguration.get(getContext()).getScaledTouchSlop());
        updateItemViewDimensionsWith(this.mSizeType);
    }

    private void updateDisplaySizeWith(WindowMetrics windowMetrics) {
        Rect bounds = windowMetrics.getBounds();
        Insets displayInsets = getDisplayInsets(windowMetrics);
        this.mDisplayInsetsRect.set(displayInsets.toRect());
        bounds.inset(displayInsets);
        this.mDisplayWidth = bounds.width();
        this.mDisplayHeight = bounds.height();
    }

    private void updateItemViewDimensionsWith(int i) {
        Resources resources = getResources();
        this.mPadding = resources.getDimensionPixelSize(i == 0 ? C1893R.dimen.accessibility_floating_menu_small_padding : C1893R.dimen.accessibility_floating_menu_large_padding);
        int dimensionPixelSize = resources.getDimensionPixelSize(i == 0 ? C1893R.dimen.accessibility_floating_menu_small_width_height : C1893R.dimen.accessibility_floating_menu_large_width_height);
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
        Drawable drawable = getContext().getDrawable(C1893R.C1895drawable.accessibility_floating_menu_background);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        this.mListView.setLayoutParams(new FrameLayout.LayoutParams(-2, -2));
        this.mListView.setBackground(new InstantInsetLayerDrawable(new Drawable[]{drawable}));
        this.mListView.setAdapter(this.mAdapter);
        this.mListView.setLayoutManager(linearLayoutManager);
        this.mListView.addOnItemTouchListener(this);
        this.mListView.animate().setInterpolator(new OvershootInterpolator());
        this.mListView.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this.mListView) {
            public AccessibilityDelegateCompat getItemDelegate() {
                return new ItemDelegateCompat(this, AccessibilityFloatingMenuView.this);
            }
        });
        updateListViewWith(this.mLastConfiguration);
        addView(this.mListView);
    }

    private void updateListViewWith(Configuration configuration) {
        updateMarginWith(configuration);
        this.mListView.setElevation((float) getResources().getDimensionPixelSize(C1893R.dimen.accessibility_floating_menu_elevation));
    }

    private WindowManager.LayoutParams createDefaultLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2024, 520, -3);
        layoutParams.receiveInsetsIgnoringZOrder = true;
        layoutParams.privateFlags |= 2097152;
        layoutParams.windowAnimations = 16973827;
        layoutParams.gravity = 8388659;
        layoutParams.x = this.mAlignment == 1 ? getMaxWindowX() : getMinWindowX();
        layoutParams.y = Math.max(0, ((int) (this.mPosition.getPercentageY() * ((float) getMaxWindowY()))) - getInterval());
        updateAccessibilityTitle(layoutParams);
        return layoutParams;
    }

    /* access modifiers changed from: protected */
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

    /* access modifiers changed from: package-private */
    public void snapToLocation(int i, int i2) {
        this.mDragAnimator.cancel();
        this.mDragAnimator.removeAllUpdateListeners();
        this.mDragAnimator.addUpdateListener(new AccessibilityFloatingMenuView$$ExternalSyntheticLambda0(this, i, i2));
        this.mDragAnimator.start();
    }

    /* access modifiers changed from: private */
    /* renamed from: onDragAnimationUpdate */
    public void mo30055xb4652553(ValueAnimator valueAnimator, int i, int i2) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float f = 1.0f - floatValue;
        this.mCurrentLayoutParams.x = (int) ((((float) this.mCurrentLayoutParams.x) * f) + (((float) i) * floatValue));
        this.mCurrentLayoutParams.y = (int) ((f * ((float) this.mCurrentLayoutParams.y)) + (floatValue * ((float) i2)));
        this.mWindowManager.updateViewLayout(this, this.mCurrentLayoutParams);
    }

    private int getMinWindowX() {
        return -getMarginStartEndWith(this.mLastConfiguration);
    }

    private int getMaxWindowX() {
        return (this.mDisplayWidth - getMarginStartEndWith(this.mLastConfiguration)) - getLayoutWidth();
    }

    private int getMaxWindowY() {
        return this.mDisplayHeight - getWindowHeight();
    }

    private InstantInsetLayerDrawable getMenuLayerDrawable() {
        return (InstantInsetLayerDrawable) this.mListView.getBackground();
    }

    private GradientDrawable getMenuGradientDrawable() {
        return (GradientDrawable) getMenuLayerDrawable().getDrawable(0);
    }

    private Insets getDisplayInsets(WindowMetrics windowMetrics) {
        return windowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
    }

    /* access modifiers changed from: private */
    public void updateLocationWith(Position position) {
        int transformToAlignment = transformToAlignment(position.getPercentageX());
        this.mCurrentLayoutParams.x = transformToAlignment == 1 ? getMaxWindowX() : getMinWindowX();
        this.mCurrentLayoutParams.y = Math.max(0, ((int) (position.getPercentageY() * ((float) getMaxWindowY()))) - getInterval());
        this.mWindowManager.updateViewLayout(this, this.mCurrentLayoutParams);
    }

    private int getInterval() {
        int i = this.mDisplayHeight - this.mImeInsetsRect.bottom;
        int percentageY = ((int) (this.mPosition.getPercentageY() * ((float) getMaxWindowY()))) + getWindowHeight();
        if (percentageY > i) {
            return percentageY - i;
        }
        return 0;
    }

    private void updateMarginWith(Configuration configuration) {
        int marginStartEndWith = getMarginStartEndWith(configuration);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mListView.getLayoutParams();
        int i = this.mMargin;
        layoutParams.setMargins(marginStartEndWith, i, marginStartEndWith, i);
        this.mListView.setLayoutParams(layoutParams);
    }

    private void updateOffsetWith(int i, int i2) {
        float layoutWidth = ((float) getLayoutWidth()) / 2.0f;
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
        getMenuGradientDrawable().setColor(getResources().getColor(C1893R.C1894color.accessibility_floating_menu_background));
    }

    private void updateStrokeWith(int i, int i2) {
        updateInsetWith(i, i2);
        int i3 = 0;
        boolean z = (i & 48) == 32;
        Resources resources = getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(C1893R.dimen.accessibility_floating_menu_stroke_width);
        if (z) {
            i3 = dimensionPixelSize;
        }
        getMenuGradientDrawable().setStroke(i3, resources.getColor(C1893R.C1894color.accessibility_floating_menu_stroke_dark));
    }

    /* access modifiers changed from: private */
    public void updateRadiusWith(int i, int i2, int i3) {
        float dimensionPixelSize = (float) getResources().getDimensionPixelSize(getRadiusResId(i, i3));
        this.mRadius = dimensionPixelSize;
        setRadius(dimensionPixelSize, i2);
    }

    /* access modifiers changed from: private */
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
        layoutParams.accessibilityTitle = getResources().getString(17039584);
    }

    private void setInset(int i, int i2) {
        InstantInsetLayerDrawable menuLayerDrawable = getMenuLayerDrawable();
        if (menuLayerDrawable.getLayerInsetLeft(0) != i || menuLayerDrawable.getLayerInsetRight(0) != i2) {
            menuLayerDrawable.setLayerInset(0, i, 0, i2, 0);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean hasExceededMaxLayoutHeight() {
        return calculateActualLayoutHeight() > getMaxLayoutHeight();
    }

    /* access modifiers changed from: private */
    public float transformCurrentPercentageXToEdge() {
        return ((double) calculateCurrentPercentageX()) < 0.5d ? 0.0f : 1.0f;
    }

    private float calculateCurrentPercentageX() {
        return ((float) this.mCurrentLayoutParams.x) / ((float) getMaxWindowX());
    }

    /* access modifiers changed from: private */
    public float calculateCurrentPercentageY() {
        return ((float) this.mCurrentLayoutParams.y) / ((float) getMaxWindowY());
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

    /* access modifiers changed from: package-private */
    public Rect getAvailableBounds() {
        return new Rect(0, 0, this.mDisplayWidth - getWindowWidth(), this.mDisplayHeight - getWindowHeight());
    }

    private int getMaxLayoutHeight() {
        return this.mDisplayHeight - (this.mMargin * 2);
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
        return Math.min(this.mDisplayHeight, (this.mMargin * 2) + getLayoutHeight());
    }

    private void setSystemGestureExclusion() {
        post(new AccessibilityFloatingMenuView$$ExternalSyntheticLambda6(this, new Rect(0, 0, getWindowWidth(), getWindowHeight())));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setSystemGestureExclusion$6$com-android-systemui-accessibility-floatingmenu-AccessibilityFloatingMenuView */
    public /* synthetic */ void mo30053x330bb596(Rect rect) {
        List list;
        if (this.mIsShowing) {
            list = Collections.singletonList(rect);
        } else {
            list = Collections.emptyList();
        }
        setSystemGestureExclusionRects(list);
    }
}
