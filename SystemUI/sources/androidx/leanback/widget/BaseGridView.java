package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.leanback.R$styleable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
/* loaded from: classes.dex */
public abstract class BaseGridView extends RecyclerView {
    private boolean mAnimateChildLayout = true;
    private boolean mHasOverlappingRendering = true;
    int mInitialPrefetchItemCount = 4;
    final GridLayoutManager mLayoutManager;
    private OnKeyInterceptListener mOnKeyInterceptListener;
    private OnMotionInterceptListener mOnMotionInterceptListener;
    private OnTouchInterceptListener mOnTouchInterceptListener;
    private OnUnhandledKeyListener mOnUnhandledKeyListener;
    private int mPrivateFlag;
    private SmoothScrollByBehavior mSmoothScrollByBehavior;

    /* loaded from: classes.dex */
    public interface OnKeyInterceptListener {
        boolean onInterceptKeyEvent(KeyEvent event);
    }

    /* loaded from: classes.dex */
    public interface OnLayoutCompletedListener {
        void onLayoutCompleted(RecyclerView.State state);
    }

    /* loaded from: classes.dex */
    public interface OnMotionInterceptListener {
        boolean onInterceptMotionEvent(MotionEvent event);
    }

    /* loaded from: classes.dex */
    public interface OnTouchInterceptListener {
        boolean onInterceptTouchEvent(MotionEvent event);
    }

    /* loaded from: classes.dex */
    public interface OnUnhandledKeyListener {
        boolean onUnhandledKey(KeyEvent event);
    }

    /* loaded from: classes.dex */
    public interface SmoothScrollByBehavior {
        int configSmoothScrollByDuration(int dx, int dy);

        Interpolator configSmoothScrollByInterpolator(int dx, int dy);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BaseGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this);
        this.mLayoutManager = gridLayoutManager;
        setLayoutManager(gridLayoutManager);
        setPreserveFocusAfterLayout(false);
        setDescendantFocusability(262144);
        setHasFixedSize(true);
        setChildrenDrawingOrderEnabled(true);
        setWillNotDraw(true);
        setOverScrollMode(2);
        ((SimpleItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);
        super.addRecyclerListener(new RecyclerView.RecyclerListener() { // from class: androidx.leanback.widget.BaseGridView.1
            @Override // androidx.recyclerview.widget.RecyclerView.RecyclerListener
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                BaseGridView.this.mLayoutManager.onChildRecycled(holder);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"CustomViewStyleable"})
    public void initBaseGridViewAttributes(Context context, AttributeSet attrs) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R$styleable.lbBaseGridView);
        this.mLayoutManager.setFocusOutAllowed(obtainStyledAttributes.getBoolean(R$styleable.lbBaseGridView_focusOutFront, false), obtainStyledAttributes.getBoolean(R$styleable.lbBaseGridView_focusOutEnd, false));
        this.mLayoutManager.setFocusOutSideAllowed(obtainStyledAttributes.getBoolean(R$styleable.lbBaseGridView_focusOutSideStart, true), obtainStyledAttributes.getBoolean(R$styleable.lbBaseGridView_focusOutSideEnd, true));
        this.mLayoutManager.setVerticalSpacing(obtainStyledAttributes.getDimensionPixelSize(R$styleable.lbBaseGridView_android_verticalSpacing, obtainStyledAttributes.getDimensionPixelSize(R$styleable.lbBaseGridView_verticalMargin, 0)));
        this.mLayoutManager.setHorizontalSpacing(obtainStyledAttributes.getDimensionPixelSize(R$styleable.lbBaseGridView_android_horizontalSpacing, obtainStyledAttributes.getDimensionPixelSize(R$styleable.lbBaseGridView_horizontalMargin, 0)));
        int i = R$styleable.lbBaseGridView_android_gravity;
        if (obtainStyledAttributes.hasValue(i)) {
            setGravity(obtainStyledAttributes.getInt(i, 0));
        }
        obtainStyledAttributes.recycle();
    }

    public void setWindowAlignment(int windowAlignment) {
        this.mLayoutManager.setWindowAlignment(windowAlignment);
        requestLayout();
    }

    public int getVerticalSpacing() {
        return this.mLayoutManager.getVerticalSpacing();
    }

    public void setOnChildViewHolderSelectedListener(OnChildViewHolderSelectedListener listener) {
        this.mLayoutManager.setOnChildViewHolderSelectedListener(listener);
    }

    public void setSelectedPosition(int position) {
        this.mLayoutManager.setSelection(position, 0);
    }

    public void setSelectedPositionSmooth(int position) {
        this.mLayoutManager.setSelectionSmooth(position);
    }

    public int getSelectedPosition() {
        return this.mLayoutManager.getSelection();
    }

    public void setGravity(int gravity) {
        this.mLayoutManager.setGravity(gravity);
        requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if ((this.mPrivateFlag & 1) == 1) {
            return false;
        }
        return this.mLayoutManager.gridOnRequestFocusInDescendants(this, direction, previouslyFocusedRect);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public int getChildDrawingOrder(int childCount, int i) {
        return this.mLayoutManager.getChildDrawingOrder(this, childCount, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean isChildrenDrawingOrderEnabledInternal() {
        return isChildrenDrawingOrderEnabled();
    }

    @Override // android.view.View
    public View focusSearch(int direction) {
        if (isFocused()) {
            GridLayoutManager gridLayoutManager = this.mLayoutManager;
            View findViewByPosition = gridLayoutManager.findViewByPosition(gridLayoutManager.getSelection());
            if (findViewByPosition != null) {
                return focusSearch(findViewByPosition, direction);
            }
        }
        return super.focusSearch(direction);
    }

    @Override // android.view.View
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.mLayoutManager.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent event) {
        OnKeyInterceptListener onKeyInterceptListener = this.mOnKeyInterceptListener;
        if ((onKeyInterceptListener == null || !onKeyInterceptListener.onInterceptKeyEvent(event)) && !super.dispatchKeyEvent(event)) {
            OnUnhandledKeyListener onUnhandledKeyListener = this.mOnUnhandledKeyListener;
            return onUnhandledKeyListener != null && onUnhandledKeyListener.onUnhandledKey(event);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent event) {
        OnTouchInterceptListener onTouchInterceptListener = this.mOnTouchInterceptListener;
        if (onTouchInterceptListener == null || !onTouchInterceptListener.onInterceptTouchEvent(event)) {
            return super.dispatchTouchEvent(event);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected boolean dispatchGenericFocusedEvent(MotionEvent event) {
        OnMotionInterceptListener onMotionInterceptListener = this.mOnMotionInterceptListener;
        if (onMotionInterceptListener == null || !onMotionInterceptListener.onInterceptMotionEvent(event)) {
            return super.dispatchGenericFocusedEvent(event);
        }
        return true;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return this.mHasOverlappingRendering;
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int layoutDirection) {
        this.mLayoutManager.onRtlPropertiesChanged(layoutDirection);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void scrollToPosition(int position) {
        if (this.mLayoutManager.isSlidingChildViews()) {
            this.mLayoutManager.setSelectionWithSub(position, 0, 0);
        } else {
            super.scrollToPosition(position);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void smoothScrollToPosition(int position) {
        if (this.mLayoutManager.isSlidingChildViews()) {
            this.mLayoutManager.setSelectionWithSub(position, 0, 0);
        } else {
            super.smoothScrollToPosition(position);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void smoothScrollBy(int dx, int dy) {
        SmoothScrollByBehavior smoothScrollByBehavior = this.mSmoothScrollByBehavior;
        if (smoothScrollByBehavior != null) {
            smoothScrollBy(dx, dy, smoothScrollByBehavior.configSmoothScrollByInterpolator(dx, dy), this.mSmoothScrollByBehavior.configSmoothScrollByDuration(dx, dy));
        } else {
            smoothScrollBy(dx, dy, null, Integer.MIN_VALUE);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void smoothScrollBy(int dx, int dy, Interpolator interpolator) {
        SmoothScrollByBehavior smoothScrollByBehavior = this.mSmoothScrollByBehavior;
        if (smoothScrollByBehavior != null) {
            smoothScrollBy(dx, dy, interpolator, smoothScrollByBehavior.configSmoothScrollByDuration(dx, dy));
        } else {
            smoothScrollBy(dx, dy, interpolator, Integer.MIN_VALUE);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public void removeView(View view) {
        boolean z = view.hasFocus() && isFocusable();
        if (z) {
            this.mPrivateFlag = 1 | this.mPrivateFlag;
            requestFocus();
        }
        super.removeView(view);
        if (z) {
            this.mPrivateFlag ^= -2;
        }
    }

    @Override // android.view.ViewGroup
    public void removeViewAt(int index) {
        boolean hasFocus = getChildAt(index).hasFocus();
        if (hasFocus) {
            this.mPrivateFlag |= 1;
            requestFocus();
        }
        super.removeViewAt(index);
        if (hasFocus) {
            this.mPrivateFlag ^= -2;
        }
    }
}
