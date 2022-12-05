package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.collection.CircularIntArray;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.leanback.widget.BaseGridView;
import androidx.leanback.widget.Grid;
import androidx.leanback.widget.ItemAlignmentFacet;
import androidx.leanback.widget.WindowAlignment;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class GridLayoutManager extends RecyclerView.LayoutManager {
    private static final Rect sTempRect = new Rect();
    static int[] sTwoInts = new int[2];
    final BaseGridView mBaseGridView;
    GridLinearSmoothScroller mCurrentSmoothScroller;
    int[] mDisappearingPositions;
    private int mExtraLayoutSpace;
    int mExtraLayoutSpaceInPreLayout;
    private FacetProviderAdapter mFacetProviderAdapter;
    private int mFixedRowSizeSecondary;
    Grid mGrid;
    private int mHorizontalSpacing;
    private int mMaxSizeSecondary;
    int mNumRows;
    PendingMoveSmoothScroller mPendingMoveSmoothScroller;
    int mPositionDeltaInPreLayout;
    private int mPrimaryScrollExtra;
    RecyclerView.Recycler mRecycler;
    private int[] mRowSizeSecondary;
    private int mRowSizeSecondaryRequested;
    private int mSaveContextLevel;
    int mScrollOffsetSecondary;
    private int mSizePrimary;
    private int mSpacingPrimary;
    private int mSpacingSecondary;
    RecyclerView.State mState;
    private int mVerticalSpacing;
    float mSmoothScrollSpeedFactor = 1.0f;
    int mMaxPendingMoves = 10;
    int mOrientation = 0;
    private OrientationHelper mOrientationHelper = OrientationHelper.createHorizontalHelper(this);
    final SparseIntArray mPositionToRowInPostLayout = new SparseIntArray();
    int mFlag = 221696;
    private OnChildSelectedListener mChildSelectedListener = null;
    private ArrayList<OnChildViewHolderSelectedListener> mChildViewHolderSelectedListeners = null;
    ArrayList<BaseGridView.OnLayoutCompletedListener> mOnLayoutCompletedListeners = null;
    OnChildLaidOutListener mChildLaidOutListener = null;
    int mFocusPosition = -1;
    int mSubFocusPosition = 0;
    private int mFocusPositionOffset = 0;
    private int mGravity = 8388659;
    private int mNumRowsRequested = 1;
    private int mFocusScrollStrategy = 0;
    final WindowAlignment mWindowAlignment = new WindowAlignment();
    private final ItemAlignment mItemAlignment = new ItemAlignment();
    private int[] mMeasuredDimension = new int[2];
    final ViewsStateBundle mChildrenStates = new ViewsStateBundle();
    private final Runnable mRequestLayoutRunnable = new Runnable() { // from class: androidx.leanback.widget.GridLayoutManager.1
        @Override // java.lang.Runnable
        public void run() {
            GridLayoutManager.this.requestLayout();
        }
    };
    private Grid.Provider mGridProvider = new Grid.Provider() { // from class: androidx.leanback.widget.GridLayoutManager.2
        @Override // androidx.leanback.widget.Grid.Provider
        public int getMinIndex() {
            return GridLayoutManager.this.mPositionDeltaInPreLayout;
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public int getCount() {
            return GridLayoutManager.this.mState.getItemCount() + GridLayoutManager.this.mPositionDeltaInPreLayout;
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public int createItem(int index, boolean append, Object[] item, boolean disappearingItem) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View viewForPosition = gridLayoutManager.getViewForPosition(index - gridLayoutManager.mPositionDeltaInPreLayout);
            if (!((LayoutParams) viewForPosition.getLayoutParams()).isItemRemoved()) {
                if (disappearingItem) {
                    if (append) {
                        GridLayoutManager.this.addDisappearingView(viewForPosition);
                    } else {
                        GridLayoutManager.this.addDisappearingView(viewForPosition, 0);
                    }
                } else if (append) {
                    GridLayoutManager.this.addView(viewForPosition);
                } else {
                    GridLayoutManager.this.addView(viewForPosition, 0);
                }
                int i = GridLayoutManager.this.mChildVisibility;
                if (i != -1) {
                    viewForPosition.setVisibility(i);
                }
                PendingMoveSmoothScroller pendingMoveSmoothScroller = GridLayoutManager.this.mPendingMoveSmoothScroller;
                if (pendingMoveSmoothScroller != null) {
                    pendingMoveSmoothScroller.consumePendingMovesBeforeLayout();
                }
                int subPositionByView = GridLayoutManager.this.getSubPositionByView(viewForPosition, viewForPosition.findFocus());
                GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
                int i2 = gridLayoutManager2.mFlag;
                if ((i2 & 3) != 1) {
                    if (index == gridLayoutManager2.mFocusPosition && subPositionByView == gridLayoutManager2.mSubFocusPosition && gridLayoutManager2.mPendingMoveSmoothScroller == null) {
                        gridLayoutManager2.dispatchChildSelected();
                    }
                } else if ((i2 & 4) == 0) {
                    if ((i2 & 16) == 0 && index == gridLayoutManager2.mFocusPosition && subPositionByView == gridLayoutManager2.mSubFocusPosition) {
                        gridLayoutManager2.dispatchChildSelected();
                    } else if ((i2 & 16) != 0 && index >= gridLayoutManager2.mFocusPosition && viewForPosition.hasFocusable()) {
                        GridLayoutManager gridLayoutManager3 = GridLayoutManager.this;
                        gridLayoutManager3.mFocusPosition = index;
                        gridLayoutManager3.mSubFocusPosition = subPositionByView;
                        gridLayoutManager3.mFlag &= -17;
                        gridLayoutManager3.dispatchChildSelected();
                    }
                }
                GridLayoutManager.this.measureChild(viewForPosition);
            }
            item[0] = viewForPosition;
            GridLayoutManager gridLayoutManager4 = GridLayoutManager.this;
            return gridLayoutManager4.mOrientation == 0 ? gridLayoutManager4.getDecoratedMeasuredWidthWithMargin(viewForPosition) : gridLayoutManager4.getDecoratedMeasuredHeightWithMargin(viewForPosition);
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public void addItem(Object item, int index, int length, int rowIndex, int edge) {
            int i;
            int i2;
            PendingMoveSmoothScroller pendingMoveSmoothScroller;
            View view = (View) item;
            if (edge == Integer.MIN_VALUE || edge == Integer.MAX_VALUE) {
                edge = !GridLayoutManager.this.mGrid.isReversedFlow() ? GridLayoutManager.this.mWindowAlignment.mainAxis().getPaddingMin() : GridLayoutManager.this.mWindowAlignment.mainAxis().getSize() - GridLayoutManager.this.mWindowAlignment.mainAxis().getPaddingMax();
            }
            if (!GridLayoutManager.this.mGrid.isReversedFlow()) {
                i2 = length + edge;
                i = edge;
            } else {
                i = edge - length;
                i2 = edge;
            }
            int rowStartSecondary = GridLayoutManager.this.getRowStartSecondary(rowIndex) + GridLayoutManager.this.mWindowAlignment.secondAxis().getPaddingMin();
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            int i3 = rowStartSecondary - gridLayoutManager.mScrollOffsetSecondary;
            gridLayoutManager.mChildrenStates.loadView(view, index);
            GridLayoutManager.this.layoutChild(rowIndex, view, i, i2, i3);
            if (!GridLayoutManager.this.mState.isPreLayout()) {
                GridLayoutManager.this.updateScrollLimits();
            }
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            if ((gridLayoutManager2.mFlag & 3) != 1 && (pendingMoveSmoothScroller = gridLayoutManager2.mPendingMoveSmoothScroller) != null) {
                pendingMoveSmoothScroller.consumePendingMovesAfterLayout();
            }
            GridLayoutManager gridLayoutManager3 = GridLayoutManager.this;
            if (gridLayoutManager3.mChildLaidOutListener != null) {
                RecyclerView.ViewHolder childViewHolder = gridLayoutManager3.mBaseGridView.getChildViewHolder(view);
                GridLayoutManager gridLayoutManager4 = GridLayoutManager.this;
                gridLayoutManager4.mChildLaidOutListener.onChildLaidOut(gridLayoutManager4.mBaseGridView, view, index, childViewHolder == null ? -1L : childViewHolder.getItemId());
            }
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public void removeItem(int index) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View findViewByPosition = gridLayoutManager.findViewByPosition(index - gridLayoutManager.mPositionDeltaInPreLayout);
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            if ((gridLayoutManager2.mFlag & 3) == 1) {
                gridLayoutManager2.detachAndScrapView(findViewByPosition, gridLayoutManager2.mRecycler);
            } else {
                gridLayoutManager2.removeAndRecycleView(findViewByPosition, gridLayoutManager2.mRecycler);
            }
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public int getEdge(int index) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            View findViewByPosition = gridLayoutManager.findViewByPosition(index - gridLayoutManager.mPositionDeltaInPreLayout);
            GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
            return (gridLayoutManager2.mFlag & 262144) != 0 ? gridLayoutManager2.getViewMax(findViewByPosition) : gridLayoutManager2.getViewMin(findViewByPosition);
        }

        @Override // androidx.leanback.widget.Grid.Provider
        public int getSize(int index) {
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            return gridLayoutManager.getViewPrimarySize(gridLayoutManager.findViewByPosition(index - gridLayoutManager.mPositionDeltaInPreLayout));
        }
    };
    int mChildVisibility = -1;

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean requestChildRectangleOnScreen(RecyclerView parent, View view, Rect rect, boolean immediate) {
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class LayoutParams extends RecyclerView.LayoutParams {
        private int[] mAlignMultiple;
        private int mAlignX;
        private int mAlignY;
        private ItemAlignmentFacet mAlignmentFacet;
        int mBottomInset;
        int mLeftInset;
        int mRightInset;
        int mTopInset;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(RecyclerView.LayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super((RecyclerView.LayoutParams) source);
        }

        int getAlignX() {
            return this.mAlignX;
        }

        int getAlignY() {
            return this.mAlignY;
        }

        int getOpticalLeft(View view) {
            return view.getLeft() + this.mLeftInset;
        }

        int getOpticalTop(View view) {
            return view.getTop() + this.mTopInset;
        }

        int getOpticalRight(View view) {
            return view.getRight() - this.mRightInset;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getOpticalWidth(View view) {
            return (view.getWidth() - this.mLeftInset) - this.mRightInset;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getOpticalHeight(View view) {
            return (view.getHeight() - this.mTopInset) - this.mBottomInset;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getOpticalLeftInset() {
            return this.mLeftInset;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getOpticalRightInset() {
            return this.mRightInset;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int getOpticalTopInset() {
            return this.mTopInset;
        }

        void setAlignX(int alignX) {
            this.mAlignX = alignX;
        }

        void setAlignY(int alignY) {
            this.mAlignY = alignY;
        }

        void setItemAlignmentFacet(ItemAlignmentFacet facet) {
            this.mAlignmentFacet = facet;
        }

        ItemAlignmentFacet getItemAlignmentFacet() {
            return this.mAlignmentFacet;
        }

        void calculateItemAlignments(int orientation, View view) {
            ItemAlignmentFacet.ItemAlignmentDef[] alignmentDefs = this.mAlignmentFacet.getAlignmentDefs();
            int[] iArr = this.mAlignMultiple;
            if (iArr == null || iArr.length != alignmentDefs.length) {
                this.mAlignMultiple = new int[alignmentDefs.length];
            }
            for (int i = 0; i < alignmentDefs.length; i++) {
                this.mAlignMultiple[i] = ItemAlignmentFacetHelper.getAlignmentPosition(view, alignmentDefs[i], orientation);
            }
            if (orientation == 0) {
                this.mAlignX = this.mAlignMultiple[0];
            } else {
                this.mAlignY = this.mAlignMultiple[0];
            }
        }

        int[] getAlignMultiple() {
            return this.mAlignMultiple;
        }

        void setOpticalInsets(int leftInset, int topInset, int rightInset, int bottomInset) {
            this.mLeftInset = leftInset;
            this.mTopInset = topInset;
            this.mRightInset = rightInset;
            this.mBottomInset = bottomInset;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public abstract class GridLinearSmoothScroller extends LinearSmoothScroller {
        boolean mSkipOnStopInternal;

        GridLinearSmoothScroller() {
            super(GridLayoutManager.this.mBaseGridView.getContext());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
        public void onStop() {
            super.onStop();
            if (!this.mSkipOnStopInternal) {
                onStopInternal();
            }
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            if (gridLayoutManager.mCurrentSmoothScroller == this) {
                gridLayoutManager.mCurrentSmoothScroller = null;
            }
            if (gridLayoutManager.mPendingMoveSmoothScroller == this) {
                gridLayoutManager.mPendingMoveSmoothScroller = null;
            }
        }

        protected void onStopInternal() {
            View findViewByPosition = findViewByPosition(getTargetPosition());
            if (findViewByPosition == null) {
                if (getTargetPosition() < 0) {
                    return;
                }
                GridLayoutManager.this.scrollToSelection(getTargetPosition(), 0, false, 0);
                return;
            }
            if (GridLayoutManager.this.mFocusPosition != getTargetPosition()) {
                GridLayoutManager.this.mFocusPosition = getTargetPosition();
            }
            if (GridLayoutManager.this.hasFocus()) {
                GridLayoutManager.this.mFlag |= 32;
                findViewByPosition.requestFocus();
                GridLayoutManager.this.mFlag &= -33;
            }
            GridLayoutManager.this.dispatchChildSelected();
            GridLayoutManager.this.dispatchChildSelectedAndPositioned();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
            return super.calculateSpeedPerPixel(displayMetrics) * GridLayoutManager.this.mSmoothScrollSpeedFactor;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearSmoothScroller
        public int calculateTimeForScrolling(int dx) {
            int calculateTimeForScrolling = super.calculateTimeForScrolling(dx);
            if (GridLayoutManager.this.mWindowAlignment.mainAxis().getSize() > 0) {
                float size = (30.0f / GridLayoutManager.this.mWindowAlignment.mainAxis().getSize()) * dx;
                return ((float) calculateTimeForScrolling) < size ? (int) size : calculateTimeForScrolling;
            }
            return calculateTimeForScrolling;
        }

        @Override // androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
        protected void onTargetFound(View targetView, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
            int i;
            int i2;
            if (GridLayoutManager.this.getScrollPosition(targetView, null, GridLayoutManager.sTwoInts)) {
                if (GridLayoutManager.this.mOrientation == 0) {
                    int[] iArr = GridLayoutManager.sTwoInts;
                    i2 = iArr[0];
                    i = iArr[1];
                } else {
                    int[] iArr2 = GridLayoutManager.sTwoInts;
                    int i3 = iArr2[1];
                    i = iArr2[0];
                    i2 = i3;
                }
                action.update(i2, i, calculateTimeForDeceleration((int) Math.sqrt((i2 * i2) + (i * i))), this.mDecelerateInterpolator);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class PendingMoveSmoothScroller extends GridLinearSmoothScroller {
        private int mPendingMoves;
        private final boolean mStaggeredGrid;

        PendingMoveSmoothScroller(int initialPendingMoves, boolean staggeredGrid) {
            super();
            this.mPendingMoves = initialPendingMoves;
            this.mStaggeredGrid = staggeredGrid;
            setTargetPosition(-2);
        }

        void increasePendingMoves() {
            int i = this.mPendingMoves;
            if (i < GridLayoutManager.this.mMaxPendingMoves) {
                this.mPendingMoves = i + 1;
            }
        }

        void decreasePendingMoves() {
            int i = this.mPendingMoves;
            if (i > (-GridLayoutManager.this.mMaxPendingMoves)) {
                this.mPendingMoves = i - 1;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:11:0x001f  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x0054  */
        /* JADX WARN: Removed duplicated region for block: B:32:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x0048 -> B:8:0x0012). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x001a -> B:9:0x001b). Please submit an issue!!! */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        void consumePendingMovesBeforeLayout() {
            int i;
            int i2;
            int i3;
            View findViewByPosition;
            if (this.mStaggeredGrid || (i = this.mPendingMoves) == 0) {
                return;
            }
            View view = null;
            if (i > 0) {
                GridLayoutManager gridLayoutManager = GridLayoutManager.this;
                i2 = gridLayoutManager.mFocusPosition;
                int i4 = gridLayoutManager.mNumRows;
                i2 += i4;
                if (this.mPendingMoves == 0 && (findViewByPosition = findViewByPosition(i2)) != null) {
                    if (GridLayoutManager.this.canScrollTo(findViewByPosition)) {
                        GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
                        gridLayoutManager2.mFocusPosition = i2;
                        gridLayoutManager2.mSubFocusPosition = 0;
                        int i5 = this.mPendingMoves;
                        if (i5 > 0) {
                            this.mPendingMoves = i5 - 1;
                        } else {
                            this.mPendingMoves = i5 + 1;
                        }
                        view = findViewByPosition;
                    }
                    if (this.mPendingMoves > 0) {
                        i4 = GridLayoutManager.this.mNumRows;
                        i2 += i4;
                        if (this.mPendingMoves == 0) {
                        }
                        if (view == null) {
                            return;
                        }
                    }
                    i3 = GridLayoutManager.this.mNumRows;
                    i2 -= i3;
                    if (this.mPendingMoves == 0) {
                    }
                    if (view == null) {
                    }
                } else if (view == null || !GridLayoutManager.this.hasFocus()) {
                } else {
                    GridLayoutManager.this.mFlag |= 32;
                    view.requestFocus();
                    GridLayoutManager.this.mFlag &= -33;
                }
            } else {
                GridLayoutManager gridLayoutManager3 = GridLayoutManager.this;
                i2 = gridLayoutManager3.mFocusPosition;
                i3 = gridLayoutManager3.mNumRows;
                i2 -= i3;
                if (this.mPendingMoves == 0) {
                }
                if (view == null) {
                }
            }
        }

        void consumePendingMovesAfterLayout() {
            int i;
            if (this.mStaggeredGrid && (i = this.mPendingMoves) != 0) {
                this.mPendingMoves = GridLayoutManager.this.processSelectionMoves(true, i);
            }
            int i2 = this.mPendingMoves;
            if (i2 == 0 || ((i2 > 0 && GridLayoutManager.this.hasCreatedLastItem()) || (this.mPendingMoves < 0 && GridLayoutManager.this.hasCreatedFirstItem()))) {
                setTargetPosition(GridLayoutManager.this.mFocusPosition);
                stop();
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
        public PointF computeScrollVectorForPosition(int targetPosition) {
            int i = this.mPendingMoves;
            if (i == 0) {
                return null;
            }
            GridLayoutManager gridLayoutManager = GridLayoutManager.this;
            int i2 = ((gridLayoutManager.mFlag & 262144) == 0 ? i >= 0 : i <= 0) ? 1 : -1;
            if (gridLayoutManager.mOrientation == 0) {
                return new PointF(i2, 0.0f);
            }
            return new PointF(0.0f, i2);
        }

        @Override // androidx.leanback.widget.GridLayoutManager.GridLinearSmoothScroller
        protected void onStopInternal() {
            super.onStopInternal();
            this.mPendingMoves = 0;
            View findViewByPosition = findViewByPosition(getTargetPosition());
            if (findViewByPosition != null) {
                GridLayoutManager.this.scrollToView(findViewByPosition, true);
            }
        }
    }

    String getTag() {
        return "GridLayoutManager:" + this.mBaseGridView.getId();
    }

    public GridLayoutManager(BaseGridView baseGridView) {
        this.mBaseGridView = baseGridView;
        setItemPrefetchEnabled(false);
    }

    public void setOrientation(int orientation) {
        if (orientation == 0 || orientation == 1) {
            this.mOrientation = orientation;
            this.mOrientationHelper = OrientationHelper.createOrientationHelper(this, orientation);
            this.mWindowAlignment.setOrientation(orientation);
            this.mItemAlignment.setOrientation(orientation);
            this.mFlag |= 256;
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        int i;
        boolean z = false;
        if (this.mOrientation == 0) {
            if (layoutDirection == 1) {
                i = 262144;
            }
            i = 0;
        } else {
            if (layoutDirection == 1) {
                i = 524288;
            }
            i = 0;
        }
        int i2 = this.mFlag;
        if ((786432 & i2) == i) {
            return;
        }
        int i3 = i | (i2 & (-786433));
        this.mFlag = i3;
        this.mFlag = i3 | 256;
        WindowAlignment.Axis axis = this.mWindowAlignment.horizontal;
        if (layoutDirection == 1) {
            z = true;
        }
        axis.setReversedFlow(z);
    }

    public void setWindowAlignment(int windowAlignment) {
        this.mWindowAlignment.mainAxis().setWindowAlignment(windowAlignment);
    }

    public void setFocusOutAllowed(boolean throughFront, boolean throughEnd) {
        int i = 0;
        int i2 = (throughFront ? 2048 : 0) | (this.mFlag & (-6145));
        if (throughEnd) {
            i = 4096;
        }
        this.mFlag = i2 | i;
    }

    public void setFocusOutSideAllowed(boolean throughStart, boolean throughEnd) {
        int i = 0;
        int i2 = (throughStart ? 8192 : 0) | (this.mFlag & (-24577));
        if (throughEnd) {
            i = 16384;
        }
        this.mFlag = i2 | i;
    }

    public void setNumRows(int numRows) {
        if (numRows < 0) {
            throw new IllegalArgumentException();
        }
        this.mNumRowsRequested = numRows;
    }

    public void setRowHeight(int height) {
        if (height >= 0 || height == -2) {
            this.mRowSizeSecondaryRequested = height;
            return;
        }
        throw new IllegalArgumentException("Invalid row height: " + height);
    }

    public void setVerticalSpacing(int space) {
        if (this.mOrientation == 1) {
            this.mVerticalSpacing = space;
            this.mSpacingPrimary = space;
            return;
        }
        this.mVerticalSpacing = space;
        this.mSpacingSecondary = space;
    }

    public void setHorizontalSpacing(int space) {
        if (this.mOrientation == 0) {
            this.mHorizontalSpacing = space;
            this.mSpacingPrimary = space;
            return;
        }
        this.mHorizontalSpacing = space;
        this.mSpacingSecondary = space;
    }

    public int getVerticalSpacing() {
        return this.mVerticalSpacing;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    protected boolean hasDoneFirstLayout() {
        return this.mGrid != null;
    }

    public void setOnChildViewHolderSelectedListener(OnChildViewHolderSelectedListener listener) {
        if (listener == null) {
            this.mChildViewHolderSelectedListeners = null;
            return;
        }
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        if (arrayList == null) {
            this.mChildViewHolderSelectedListeners = new ArrayList<>();
        } else {
            arrayList.clear();
        }
        this.mChildViewHolderSelectedListeners.add(listener);
    }

    boolean hasOnChildViewHolderSelectedListener() {
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        return arrayList != null && arrayList.size() > 0;
    }

    void fireOnChildViewHolderSelected(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        if (arrayList == null) {
            return;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            this.mChildViewHolderSelectedListeners.get(size).onChildViewHolderSelected(parent, child, position, subposition);
        }
    }

    void fireOnChildViewHolderSelectedAndPositioned(RecyclerView parent, RecyclerView.ViewHolder child, int position, int subposition) {
        ArrayList<OnChildViewHolderSelectedListener> arrayList = this.mChildViewHolderSelectedListeners;
        if (arrayList == null) {
            return;
        }
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            this.mChildViewHolderSelectedListeners.get(size).onChildViewHolderSelectedAndPositioned(parent, child, position, subposition);
        }
    }

    private int getAdapterPositionByView(View view) {
        LayoutParams layoutParams;
        if (view == null || (layoutParams = (LayoutParams) view.getLayoutParams()) == null || layoutParams.isItemRemoved()) {
            return -1;
        }
        return layoutParams.getAbsoluteAdapterPosition();
    }

    int getSubPositionByView(View view, View childView) {
        ItemAlignmentFacet itemAlignmentFacet;
        if (view != null && childView != null && (itemAlignmentFacet = ((LayoutParams) view.getLayoutParams()).getItemAlignmentFacet()) != null) {
            ItemAlignmentFacet.ItemAlignmentDef[] alignmentDefs = itemAlignmentFacet.getAlignmentDefs();
            if (alignmentDefs.length > 1) {
                while (childView != view) {
                    int id = childView.getId();
                    if (id != -1) {
                        for (int i = 1; i < alignmentDefs.length; i++) {
                            if (alignmentDefs[i].getItemAlignmentFocusViewId() == id) {
                                return i;
                            }
                        }
                        continue;
                    }
                    childView = (View) childView.getParent();
                }
            }
        }
        return 0;
    }

    private int getAdapterPositionByIndex(int index) {
        return getAdapterPositionByView(getChildAt(index));
    }

    void dispatchChildSelected() {
        if (this.mChildSelectedListener != null || hasOnChildViewHolderSelectedListener()) {
            int i = this.mFocusPosition;
            View findViewByPosition = i == -1 ? null : findViewByPosition(i);
            if (findViewByPosition != null) {
                RecyclerView.ViewHolder childViewHolder = this.mBaseGridView.getChildViewHolder(findViewByPosition);
                OnChildSelectedListener onChildSelectedListener = this.mChildSelectedListener;
                if (onChildSelectedListener != null) {
                    onChildSelectedListener.onChildSelected(this.mBaseGridView, findViewByPosition, this.mFocusPosition, childViewHolder == null ? -1L : childViewHolder.getItemId());
                }
                fireOnChildViewHolderSelected(this.mBaseGridView, childViewHolder, this.mFocusPosition, this.mSubFocusPosition);
            } else {
                OnChildSelectedListener onChildSelectedListener2 = this.mChildSelectedListener;
                if (onChildSelectedListener2 != null) {
                    onChildSelectedListener2.onChildSelected(this.mBaseGridView, null, -1, -1L);
                }
                fireOnChildViewHolderSelected(this.mBaseGridView, null, -1, 0);
            }
            if ((this.mFlag & 3) == 1 || this.mBaseGridView.isLayoutRequested()) {
                return;
            }
            int childCount = getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                if (getChildAt(i2).isLayoutRequested()) {
                    forceRequestLayout();
                    return;
                }
            }
        }
    }

    void dispatchChildSelectedAndPositioned() {
        if (!hasOnChildViewHolderSelectedListener()) {
            return;
        }
        int i = this.mFocusPosition;
        View findViewByPosition = i == -1 ? null : findViewByPosition(i);
        if (findViewByPosition != null) {
            fireOnChildViewHolderSelectedAndPositioned(this.mBaseGridView, this.mBaseGridView.getChildViewHolder(findViewByPosition), this.mFocusPosition, this.mSubFocusPosition);
            return;
        }
        OnChildSelectedListener onChildSelectedListener = this.mChildSelectedListener;
        if (onChildSelectedListener != null) {
            onChildSelectedListener.onChildSelected(this.mBaseGridView, null, -1, -1L);
        }
        fireOnChildViewHolderSelectedAndPositioned(this.mBaseGridView, null, -1, 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollHorizontally() {
        return this.mOrientation == 0 || this.mNumRows > 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean canScrollVertically() {
        return this.mOrientation == 1 || this.mNumRows > 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateLayoutParams(Context context, AttributeSet attrs) {
        return new LayoutParams(context, attrs);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) lp);
        }
        if (lp instanceof RecyclerView.LayoutParams) {
            return new LayoutParams((RecyclerView.LayoutParams) lp);
        }
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        }
        return new LayoutParams(lp);
    }

    protected View getViewForPosition(int position) {
        View viewForPosition = this.mRecycler.getViewForPosition(position);
        ((LayoutParams) viewForPosition.getLayoutParams()).setItemAlignmentFacet((ItemAlignmentFacet) getFacet(this.mBaseGridView.getChildViewHolder(viewForPosition), ItemAlignmentFacet.class));
        return viewForPosition;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int getOpticalLeft(View v) {
        return ((LayoutParams) v.getLayoutParams()).getOpticalLeft(v);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int getOpticalRight(View v) {
        return ((LayoutParams) v.getLayoutParams()).getOpticalRight(v);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getDecoratedLeft(View child) {
        return super.getDecoratedLeft(child) + ((LayoutParams) child.getLayoutParams()).mLeftInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getDecoratedTop(View child) {
        return super.getDecoratedTop(child) + ((LayoutParams) child.getLayoutParams()).mTopInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getDecoratedRight(View child) {
        return super.getDecoratedRight(child) - ((LayoutParams) child.getLayoutParams()).mRightInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getDecoratedBottom(View child) {
        return super.getDecoratedBottom(child) - ((LayoutParams) child.getLayoutParams()).mBottomInset;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void getDecoratedBoundsWithMargins(View view, Rect outBounds) {
        super.getDecoratedBoundsWithMargins(view, outBounds);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        outBounds.left += layoutParams.mLeftInset;
        outBounds.top += layoutParams.mTopInset;
        outBounds.right -= layoutParams.mRightInset;
        outBounds.bottom -= layoutParams.mBottomInset;
    }

    int getViewMin(View v) {
        return this.mOrientationHelper.getDecoratedStart(v);
    }

    int getViewMax(View v) {
        return this.mOrientationHelper.getDecoratedEnd(v);
    }

    int getViewPrimarySize(View view) {
        Rect rect = sTempRect;
        getDecoratedBoundsWithMargins(view, rect);
        return this.mOrientation == 0 ? rect.width() : rect.height();
    }

    private int getViewCenter(View view) {
        return this.mOrientation == 0 ? getViewCenterX(view) : getViewCenterY(view);
    }

    private int getViewCenterSecondary(View view) {
        return this.mOrientation == 0 ? getViewCenterY(view) : getViewCenterX(view);
    }

    private int getViewCenterX(View v) {
        LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
        return layoutParams.getOpticalLeft(v) + layoutParams.getAlignX();
    }

    private int getViewCenterY(View v) {
        LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
        return layoutParams.getOpticalTop(v) + layoutParams.getAlignY();
    }

    private void saveContext(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int i = this.mSaveContextLevel;
        if (i == 0) {
            this.mRecycler = recycler;
            this.mState = state;
            this.mPositionDeltaInPreLayout = 0;
            this.mExtraLayoutSpaceInPreLayout = 0;
        }
        this.mSaveContextLevel = i + 1;
    }

    private void leaveContext() {
        int i = this.mSaveContextLevel - 1;
        this.mSaveContextLevel = i;
        if (i == 0) {
            this.mRecycler = null;
            this.mState = null;
            this.mPositionDeltaInPreLayout = 0;
            this.mExtraLayoutSpaceInPreLayout = 0;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0074, code lost:
        if (((r5.mFlag & 262144) != 0) != r5.mGrid.isReversedFlow()) goto L29;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean layoutInit() {
        Grid grid;
        int itemCount = this.mState.getItemCount();
        boolean z = true;
        if (itemCount == 0) {
            this.mFocusPosition = -1;
            this.mSubFocusPosition = 0;
        } else {
            int i = this.mFocusPosition;
            if (i >= itemCount) {
                this.mFocusPosition = itemCount - 1;
                this.mSubFocusPosition = 0;
            } else if (i == -1 && itemCount > 0) {
                this.mFocusPosition = 0;
                this.mSubFocusPosition = 0;
            }
        }
        if (!this.mState.didStructureChange() && (grid = this.mGrid) != null && grid.getFirstVisibleIndex() >= 0 && (this.mFlag & 256) == 0 && this.mGrid.getNumRows() == this.mNumRows) {
            updateScrollController();
            updateSecondaryScrollLimits();
            this.mGrid.setSpacing(this.mSpacingPrimary);
            return true;
        }
        this.mFlag &= -257;
        Grid grid2 = this.mGrid;
        if (grid2 != null && this.mNumRows == grid2.getNumRows()) {
        }
        Grid createGrid = Grid.createGrid(this.mNumRows);
        this.mGrid = createGrid;
        createGrid.setProvider(this.mGridProvider);
        Grid grid3 = this.mGrid;
        if ((262144 & this.mFlag) == 0) {
            z = false;
        }
        grid3.setReversedFlow(z);
        initScrollController();
        updateSecondaryScrollLimits();
        this.mGrid.setSpacing(this.mSpacingPrimary);
        detachAndScrapAttachedViews(this.mRecycler);
        this.mGrid.resetVisibleIndex();
        this.mWindowAlignment.mainAxis().invalidateScrollMin();
        this.mWindowAlignment.mainAxis().invalidateScrollMax();
        return false;
    }

    private int getRowSizeSecondary(int rowIndex) {
        int i = this.mFixedRowSizeSecondary;
        if (i != 0) {
            return i;
        }
        int[] iArr = this.mRowSizeSecondary;
        if (iArr != null) {
            return iArr[rowIndex];
        }
        return 0;
    }

    int getRowStartSecondary(int rowIndex) {
        int i = 0;
        if ((this.mFlag & 524288) != 0) {
            for (int i2 = this.mNumRows - 1; i2 > rowIndex; i2--) {
                i += getRowSizeSecondary(i2) + this.mSpacingSecondary;
            }
            return i;
        }
        int i3 = 0;
        while (i < rowIndex) {
            i3 += getRowSizeSecondary(i) + this.mSpacingSecondary;
            i++;
        }
        return i3;
    }

    private int getSizeSecondary() {
        int i = (this.mFlag & 524288) != 0 ? 0 : this.mNumRows - 1;
        return getRowStartSecondary(i) + getRowSizeSecondary(i);
    }

    int getDecoratedMeasuredWidthWithMargin(View v) {
        LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
        return getDecoratedMeasuredWidth(v) + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
    }

    int getDecoratedMeasuredHeightWithMargin(View v) {
        LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
        return getDecoratedMeasuredHeight(v) + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
    }

    private void measureScrapChild(int position, int widthSpec, int heightSpec, int[] measuredDimension) {
        View viewForPosition = this.mRecycler.getViewForPosition(position);
        if (viewForPosition != null) {
            LayoutParams layoutParams = (LayoutParams) viewForPosition.getLayoutParams();
            Rect rect = sTempRect;
            calculateItemDecorationsForChild(viewForPosition, rect);
            viewForPosition.measure(ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight() + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + rect.left + rect.right, ((ViewGroup.MarginLayoutParams) layoutParams).width), ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom() + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + rect.top + rect.bottom, ((ViewGroup.MarginLayoutParams) layoutParams).height));
            measuredDimension[0] = getDecoratedMeasuredWidthWithMargin(viewForPosition);
            measuredDimension[1] = getDecoratedMeasuredHeightWithMargin(viewForPosition);
            this.mRecycler.recycleView(viewForPosition);
        }
    }

    private boolean processRowSizeSecondary(boolean measure) {
        int decoratedMeasuredWidthWithMargin;
        if (this.mFixedRowSizeSecondary != 0 || this.mRowSizeSecondary == null) {
            return false;
        }
        Grid grid = this.mGrid;
        CircularIntArray[] itemPositionsInRows = grid == null ? null : grid.getItemPositionsInRows();
        boolean z = false;
        int i = -1;
        for (int i2 = 0; i2 < this.mNumRows; i2++) {
            CircularIntArray circularIntArray = itemPositionsInRows == null ? null : itemPositionsInRows[i2];
            int size = circularIntArray == null ? 0 : circularIntArray.size();
            int i3 = -1;
            for (int i4 = 0; i4 < size; i4 += 2) {
                int i5 = circularIntArray.get(i4 + 1);
                for (int i6 = circularIntArray.get(i4); i6 <= i5; i6++) {
                    View findViewByPosition = findViewByPosition(i6 - this.mPositionDeltaInPreLayout);
                    if (findViewByPosition != null) {
                        if (measure) {
                            measureChild(findViewByPosition);
                        }
                        if (this.mOrientation == 0) {
                            decoratedMeasuredWidthWithMargin = getDecoratedMeasuredHeightWithMargin(findViewByPosition);
                        } else {
                            decoratedMeasuredWidthWithMargin = getDecoratedMeasuredWidthWithMargin(findViewByPosition);
                        }
                        if (decoratedMeasuredWidthWithMargin > i3) {
                            i3 = decoratedMeasuredWidthWithMargin;
                        }
                    }
                }
            }
            int itemCount = this.mState.getItemCount();
            if (!this.mBaseGridView.hasFixedSize() && measure && i3 < 0 && itemCount > 0) {
                if (i < 0) {
                    int i7 = this.mFocusPosition;
                    if (i7 < 0) {
                        i7 = 0;
                    } else if (i7 >= itemCount) {
                        i7 = itemCount - 1;
                    }
                    if (getChildCount() > 0) {
                        int layoutPosition = this.mBaseGridView.getChildViewHolder(getChildAt(0)).getLayoutPosition();
                        int layoutPosition2 = this.mBaseGridView.getChildViewHolder(getChildAt(getChildCount() - 1)).getLayoutPosition();
                        if (i7 >= layoutPosition && i7 <= layoutPosition2) {
                            i7 = i7 - layoutPosition <= layoutPosition2 - i7 ? layoutPosition - 1 : layoutPosition2 + 1;
                            if (i7 < 0 && layoutPosition2 < itemCount - 1) {
                                i7 = layoutPosition2 + 1;
                            } else if (i7 >= itemCount && layoutPosition > 0) {
                                i7 = layoutPosition - 1;
                            }
                        }
                    }
                    if (i7 >= 0 && i7 < itemCount) {
                        measureScrapChild(i7, View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0), this.mMeasuredDimension);
                        i = this.mOrientation == 0 ? this.mMeasuredDimension[1] : this.mMeasuredDimension[0];
                    }
                }
                if (i >= 0) {
                    i3 = i;
                }
            }
            if (i3 < 0) {
                i3 = 0;
            }
            int[] iArr = this.mRowSizeSecondary;
            if (iArr[i2] != i3) {
                iArr[i2] = i3;
                z = true;
            }
        }
        return z;
    }

    private void updateRowSecondarySizeRefresh() {
        int i = this.mFlag & (-1025);
        int i2 = 0;
        if (processRowSizeSecondary(false)) {
            i2 = 1024;
        }
        int i3 = i | i2;
        this.mFlag = i3;
        if ((i3 & 1024) != 0) {
            forceRequestLayout();
        }
    }

    private void forceRequestLayout() {
        ViewCompat.postOnAnimation(this.mBaseGridView, this.mRequestLayoutRunnable);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int size;
        int size2;
        int mode;
        int paddingLeft;
        int paddingRight;
        saveContext(recycler, state);
        if (this.mOrientation == 0) {
            size2 = View.MeasureSpec.getSize(widthSpec);
            size = View.MeasureSpec.getSize(heightSpec);
            mode = View.MeasureSpec.getMode(heightSpec);
            paddingLeft = getPaddingTop();
            paddingRight = getPaddingBottom();
        } else {
            size = View.MeasureSpec.getSize(widthSpec);
            size2 = View.MeasureSpec.getSize(heightSpec);
            mode = View.MeasureSpec.getMode(widthSpec);
            paddingLeft = getPaddingLeft();
            paddingRight = getPaddingRight();
        }
        int i = paddingLeft + paddingRight;
        this.mMaxSizeSecondary = size;
        int i2 = this.mRowSizeSecondaryRequested;
        if (i2 == -2) {
            int i3 = this.mNumRowsRequested;
            if (i3 == 0) {
                i3 = 1;
            }
            this.mNumRows = i3;
            this.mFixedRowSizeSecondary = 0;
            int[] iArr = this.mRowSizeSecondary;
            if (iArr == null || iArr.length != i3) {
                this.mRowSizeSecondary = new int[i3];
            }
            if (this.mState.isPreLayout()) {
                updatePositionDeltaInPreLayout();
            }
            processRowSizeSecondary(true);
            if (mode == Integer.MIN_VALUE) {
                size = Math.min(getSizeSecondary() + i, this.mMaxSizeSecondary);
            } else if (mode == 0) {
                size = getSizeSecondary() + i;
            } else if (mode == 1073741824) {
                size = this.mMaxSizeSecondary;
            } else {
                throw new IllegalStateException("wrong spec");
            }
        } else {
            if (mode != Integer.MIN_VALUE) {
                if (mode == 0) {
                    if (i2 == 0) {
                        i2 = size - i;
                    }
                    this.mFixedRowSizeSecondary = i2;
                    int i4 = this.mNumRowsRequested;
                    if (i4 == 0) {
                        i4 = 1;
                    }
                    this.mNumRows = i4;
                    size = (i2 * i4) + (this.mSpacingSecondary * (i4 - 1)) + i;
                } else if (mode != 1073741824) {
                    throw new IllegalStateException("wrong spec");
                }
            }
            int i5 = this.mNumRowsRequested;
            if (i5 == 0 && i2 == 0) {
                this.mNumRows = 1;
                this.mFixedRowSizeSecondary = size - i;
            } else if (i5 == 0) {
                this.mFixedRowSizeSecondary = i2;
                int i6 = this.mSpacingSecondary;
                this.mNumRows = (size + i6) / (i2 + i6);
            } else if (i2 == 0) {
                this.mNumRows = i5;
                this.mFixedRowSizeSecondary = ((size - i) - (this.mSpacingSecondary * (i5 - 1))) / i5;
            } else {
                this.mNumRows = i5;
                this.mFixedRowSizeSecondary = i2;
            }
            if (mode == Integer.MIN_VALUE) {
                int i7 = this.mFixedRowSizeSecondary;
                int i8 = this.mNumRows;
                int i9 = (i7 * i8) + (this.mSpacingSecondary * (i8 - 1)) + i;
                if (i9 < size) {
                    size = i9;
                }
            }
        }
        if (this.mOrientation == 0) {
            setMeasuredDimension(size2, size);
        } else {
            setMeasuredDimension(size, size2);
        }
        leaveContext();
    }

    void measureChild(View child) {
        int makeMeasureSpec;
        int i;
        int i2;
        LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
        Rect rect = sTempRect;
        calculateItemDecorationsForChild(child, rect);
        int i3 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + rect.left + rect.right;
        int i4 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + rect.top + rect.bottom;
        if (this.mRowSizeSecondaryRequested == -2) {
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        } else {
            makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mFixedRowSizeSecondary, 1073741824);
        }
        if (this.mOrientation == 0) {
            i2 = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), i3, ((ViewGroup.MarginLayoutParams) layoutParams).width);
            i = ViewGroup.getChildMeasureSpec(makeMeasureSpec, i4, ((ViewGroup.MarginLayoutParams) layoutParams).height);
        } else {
            int childMeasureSpec = ViewGroup.getChildMeasureSpec(View.MeasureSpec.makeMeasureSpec(0, 0), i4, ((ViewGroup.MarginLayoutParams) layoutParams).height);
            int childMeasureSpec2 = ViewGroup.getChildMeasureSpec(makeMeasureSpec, i3, ((ViewGroup.MarginLayoutParams) layoutParams).width);
            i = childMeasureSpec;
            i2 = childMeasureSpec2;
        }
        child.measure(i2, i);
    }

    <E> E getFacet(RecyclerView.ViewHolder vh, Class<? extends E> facetClass) {
        FacetProviderAdapter facetProviderAdapter;
        FacetProvider facetProvider;
        E e = vh instanceof FacetProvider ? (E) ((FacetProvider) vh).getFacet(facetClass) : null;
        return (e != null || (facetProviderAdapter = this.mFacetProviderAdapter) == null || (facetProvider = facetProviderAdapter.getFacetProvider(vh.getItemViewType())) == null) ? e : (E) facetProvider.getFacet(facetClass);
    }

    void layoutChild(int rowIndex, View v, int start, int end, int startSecondary) {
        int rowSizeSecondary;
        int i;
        int decoratedMeasuredHeightWithMargin = this.mOrientation == 0 ? getDecoratedMeasuredHeightWithMargin(v) : getDecoratedMeasuredWidthWithMargin(v);
        int i2 = this.mFixedRowSizeSecondary;
        if (i2 > 0) {
            decoratedMeasuredHeightWithMargin = Math.min(decoratedMeasuredHeightWithMargin, i2);
        }
        int i3 = this.mGravity;
        int i4 = i3 & 112;
        int absoluteGravity = (this.mFlag & 786432) != 0 ? Gravity.getAbsoluteGravity(i3 & 8388615, 1) : i3 & 7;
        int i5 = this.mOrientation;
        if ((i5 != 0 || i4 != 48) && (i5 != 1 || absoluteGravity != 3)) {
            if ((i5 == 0 && i4 == 80) || (i5 == 1 && absoluteGravity == 5)) {
                rowSizeSecondary = getRowSizeSecondary(rowIndex) - decoratedMeasuredHeightWithMargin;
            } else if ((i5 == 0 && i4 == 16) || (i5 == 1 && absoluteGravity == 1)) {
                rowSizeSecondary = (getRowSizeSecondary(rowIndex) - decoratedMeasuredHeightWithMargin) / 2;
            }
            startSecondary += rowSizeSecondary;
        }
        if (this.mOrientation == 0) {
            i = decoratedMeasuredHeightWithMargin + startSecondary;
        } else {
            int i6 = decoratedMeasuredHeightWithMargin + startSecondary;
            int i7 = startSecondary;
            startSecondary = start;
            start = i7;
            i = end;
            end = i6;
        }
        layoutDecoratedWithMargins(v, start, startSecondary, end, i);
        Rect rect = sTempRect;
        super.getDecoratedBoundsWithMargins(v, rect);
        ((LayoutParams) v.getLayoutParams()).setOpticalInsets(start - rect.left, startSecondary - rect.top, rect.right - end, rect.bottom - i);
        updateChildAlignments(v);
    }

    private void updateChildAlignments(View v) {
        LayoutParams layoutParams = (LayoutParams) v.getLayoutParams();
        if (layoutParams.getItemAlignmentFacet() == null) {
            layoutParams.setAlignX(this.mItemAlignment.horizontal.getAlignmentPosition(v));
            layoutParams.setAlignY(this.mItemAlignment.vertical.getAlignmentPosition(v));
            return;
        }
        layoutParams.calculateItemAlignments(this.mOrientation, v);
        if (this.mOrientation == 0) {
            layoutParams.setAlignY(this.mItemAlignment.vertical.getAlignmentPosition(v));
        } else {
            layoutParams.setAlignX(this.mItemAlignment.horizontal.getAlignmentPosition(v));
        }
    }

    private void removeInvisibleViewsAtEnd() {
        int i;
        int i2 = this.mFlag;
        if ((65600 & i2) == 65536) {
            Grid grid = this.mGrid;
            int i3 = this.mFocusPosition;
            if ((i2 & 262144) != 0) {
                i = -this.mExtraLayoutSpace;
            } else {
                i = this.mExtraLayoutSpace + this.mSizePrimary;
            }
            grid.removeInvisibleItemsAtEnd(i3, i);
        }
    }

    private void removeInvisibleViewsAtFront() {
        int i = this.mFlag;
        if ((65600 & i) == 65536) {
            this.mGrid.removeInvisibleItemsAtFront(this.mFocusPosition, (i & 262144) != 0 ? this.mSizePrimary + this.mExtraLayoutSpace : -this.mExtraLayoutSpace);
        }
    }

    private boolean appendOneColumnVisibleItems() {
        return this.mGrid.appendOneColumnVisibleItems();
    }

    int getSlideOutDistance() {
        int i;
        int left;
        int right;
        if (this.mOrientation == 1) {
            i = -getHeight();
            if (getChildCount() <= 0 || (left = getChildAt(0).getTop()) >= 0) {
                return i;
            }
        } else if ((this.mFlag & 262144) != 0) {
            int width = getWidth();
            return (getChildCount() <= 0 || (right = getChildAt(0).getRight()) <= width) ? width : right;
        } else {
            i = -getWidth();
            if (getChildCount() <= 0 || (left = getChildAt(0).getLeft()) >= 0) {
                return i;
            }
        }
        return i + left;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isSlidingChildViews() {
        return (this.mFlag & 64) != 0;
    }

    private boolean prependOneColumnVisibleItems() {
        return this.mGrid.prependOneColumnVisibleItems();
    }

    private void appendVisibleItems() {
        int i;
        Grid grid = this.mGrid;
        if ((this.mFlag & 262144) != 0) {
            i = (-this.mExtraLayoutSpace) - this.mExtraLayoutSpaceInPreLayout;
        } else {
            i = this.mSizePrimary + this.mExtraLayoutSpace + this.mExtraLayoutSpaceInPreLayout;
        }
        grid.appendVisibleItems(i);
    }

    private void prependVisibleItems() {
        int i;
        Grid grid = this.mGrid;
        if ((this.mFlag & 262144) != 0) {
            i = this.mSizePrimary + this.mExtraLayoutSpace + this.mExtraLayoutSpaceInPreLayout;
        } else {
            i = (-this.mExtraLayoutSpace) - this.mExtraLayoutSpaceInPreLayout;
        }
        grid.prependVisibleItems(i);
    }

    private void fastRelayout() {
        Grid.Location mo120getLocation;
        int decoratedMeasuredHeightWithMargin;
        int childCount = getChildCount();
        int firstVisibleIndex = this.mGrid.getFirstVisibleIndex();
        this.mFlag &= -9;
        boolean z = false;
        int i = 0;
        while (i < childCount) {
            View childAt = getChildAt(i);
            if (firstVisibleIndex == getAdapterPositionByView(childAt) && (mo120getLocation = this.mGrid.mo120getLocation(firstVisibleIndex)) != null) {
                int rowStartSecondary = (getRowStartSecondary(mo120getLocation.row) + this.mWindowAlignment.secondAxis().getPaddingMin()) - this.mScrollOffsetSecondary;
                int viewMin = getViewMin(childAt);
                int viewPrimarySize = getViewPrimarySize(childAt);
                if (((LayoutParams) childAt.getLayoutParams()).viewNeedsUpdate()) {
                    this.mFlag |= 8;
                    detachAndScrapView(childAt, this.mRecycler);
                    childAt = getViewForPosition(firstVisibleIndex);
                    addView(childAt, i);
                }
                View view = childAt;
                measureChild(view);
                if (this.mOrientation == 0) {
                    decoratedMeasuredHeightWithMargin = getDecoratedMeasuredWidthWithMargin(view);
                } else {
                    decoratedMeasuredHeightWithMargin = getDecoratedMeasuredHeightWithMargin(view);
                }
                layoutChild(mo120getLocation.row, view, viewMin, viewMin + decoratedMeasuredHeightWithMargin, rowStartSecondary);
                if (viewPrimarySize == decoratedMeasuredHeightWithMargin) {
                    i++;
                    firstVisibleIndex++;
                }
            }
            z = true;
        }
        if (z) {
            int lastVisibleIndex = this.mGrid.getLastVisibleIndex();
            for (int i2 = childCount - 1; i2 >= i; i2--) {
                detachAndScrapView(getChildAt(i2), this.mRecycler);
            }
            this.mGrid.invalidateItemsAfter(firstVisibleIndex);
            if ((this.mFlag & 65536) != 0) {
                appendVisibleItems();
                int i3 = this.mFocusPosition;
                if (i3 >= 0 && i3 <= lastVisibleIndex) {
                    while (this.mGrid.getLastVisibleIndex() < this.mFocusPosition) {
                        this.mGrid.appendOneColumnVisibleItems();
                    }
                }
            } else {
                while (this.mGrid.appendOneColumnVisibleItems() && this.mGrid.getLastVisibleIndex() < lastVisibleIndex) {
                }
            }
        }
        updateScrollLimits();
        updateSecondaryScrollLimits();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void removeAndRecycleAllViews(RecyclerView.Recycler recycler) {
        for (int childCount = getChildCount() - 1; childCount >= 0; childCount--) {
            removeAndRecycleViewAt(childCount, recycler);
        }
    }

    private void focusToViewInLayout(boolean hadFocus, boolean alignToView, int extraDelta, int extraDeltaSecondary) {
        View findViewByPosition = findViewByPosition(this.mFocusPosition);
        if (findViewByPosition != null && alignToView) {
            scrollToView(findViewByPosition, false, extraDelta, extraDeltaSecondary);
        }
        if (findViewByPosition != null && hadFocus && !findViewByPosition.hasFocus()) {
            findViewByPosition.requestFocus();
        } else if (hadFocus || this.mBaseGridView.hasFocus()) {
        } else {
            if (findViewByPosition != null && findViewByPosition.hasFocusable()) {
                this.mBaseGridView.focusableViewAvailable(findViewByPosition);
            } else {
                int childCount = getChildCount();
                int i = 0;
                while (true) {
                    if (i < childCount) {
                        findViewByPosition = getChildAt(i);
                        if (findViewByPosition != null && findViewByPosition.hasFocusable()) {
                            this.mBaseGridView.focusableViewAvailable(findViewByPosition);
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
            }
            if (!alignToView || findViewByPosition == null || !findViewByPosition.hasFocus()) {
                return;
            }
            scrollToView(findViewByPosition, false, extraDelta, extraDeltaSecondary);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutCompleted(RecyclerView.State state) {
        ArrayList<BaseGridView.OnLayoutCompletedListener> arrayList = this.mOnLayoutCompletedListeners;
        if (arrayList != null) {
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                this.mOnLayoutCompletedListeners.get(size).onLayoutCompleted(state);
            }
        }
    }

    void updatePositionToRowMapInPostLayout() {
        Grid.Location mo120getLocation;
        this.mPositionToRowInPostLayout.clear();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            int oldPosition = this.mBaseGridView.getChildViewHolder(getChildAt(i)).getOldPosition();
            if (oldPosition >= 0 && (mo120getLocation = this.mGrid.mo120getLocation(oldPosition)) != null) {
                this.mPositionToRowInPostLayout.put(oldPosition, mo120getLocation.row);
            }
        }
    }

    void fillScrapViewsInPostLayout() {
        List<RecyclerView.ViewHolder> scrapList = this.mRecycler.getScrapList();
        int size = scrapList.size();
        if (size == 0) {
            return;
        }
        int[] iArr = this.mDisappearingPositions;
        if (iArr == null || size > iArr.length) {
            int length = iArr == null ? 16 : iArr.length;
            while (length < size) {
                length <<= 1;
            }
            this.mDisappearingPositions = new int[length];
        }
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            int absoluteAdapterPosition = scrapList.get(i2).getAbsoluteAdapterPosition();
            if (absoluteAdapterPosition >= 0) {
                this.mDisappearingPositions[i] = absoluteAdapterPosition;
                i++;
            }
        }
        if (i > 0) {
            Arrays.sort(this.mDisappearingPositions, 0, i);
            this.mGrid.fillDisappearingItems(this.mDisappearingPositions, i, this.mPositionToRowInPostLayout);
        }
        this.mPositionToRowInPostLayout.clear();
    }

    void updatePositionDeltaInPreLayout() {
        if (getChildCount() > 0) {
            this.mPositionDeltaInPreLayout = this.mGrid.getFirstVisibleIndex() - ((LayoutParams) getChildAt(0).getLayoutParams()).getViewLayoutPosition();
        } else {
            this.mPositionDeltaInPreLayout = 0;
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: CFG modification limit reached, blocks count: 226
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:60)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:40)
        */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(androidx.recyclerview.widget.RecyclerView.Recycler r13, androidx.recyclerview.widget.RecyclerView.State r14) {
        /*
            Method dump skipped, instructions count: 518
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.GridLayoutManager.onLayoutChildren(androidx.recyclerview.widget.RecyclerView$Recycler, androidx.recyclerview.widget.RecyclerView$State):void");
    }

    private void offsetChildrenSecondary(int increment) {
        int childCount = getChildCount();
        int i = 0;
        if (this.mOrientation == 0) {
            while (i < childCount) {
                getChildAt(i).offsetTopAndBottom(increment);
                i++;
            }
            return;
        }
        while (i < childCount) {
            getChildAt(i).offsetLeftAndRight(increment);
            i++;
        }
    }

    private void offsetChildrenPrimary(int increment) {
        int childCount = getChildCount();
        int i = 0;
        if (this.mOrientation == 1) {
            while (i < childCount) {
                getChildAt(i).offsetTopAndBottom(increment);
                i++;
            }
            return;
        }
        while (i < childCount) {
            getChildAt(i).offsetLeftAndRight(increment);
            i++;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollDirectionSecondary;
        if ((this.mFlag & 512) == 0 || !hasDoneFirstLayout()) {
            return 0;
        }
        saveContext(recycler, state);
        this.mFlag = (this.mFlag & (-4)) | 2;
        if (this.mOrientation == 0) {
            scrollDirectionSecondary = scrollDirectionPrimary(dx);
        } else {
            scrollDirectionSecondary = scrollDirectionSecondary(dx);
        }
        leaveContext();
        this.mFlag &= -4;
        return scrollDirectionSecondary;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrollDirectionSecondary;
        if ((this.mFlag & 512) == 0 || !hasDoneFirstLayout()) {
            return 0;
        }
        this.mFlag = (this.mFlag & (-4)) | 2;
        saveContext(recycler, state);
        if (this.mOrientation == 1) {
            scrollDirectionSecondary = scrollDirectionPrimary(dy);
        } else {
            scrollDirectionSecondary = scrollDirectionSecondary(dy);
        }
        leaveContext();
        this.mFlag &= -4;
        return scrollDirectionSecondary;
    }

    private int scrollDirectionPrimary(int da) {
        int minScroll;
        int i = this.mFlag;
        boolean z = true;
        if ((i & 64) == 0 && (i & 3) != 1 && (da <= 0 ? !(da >= 0 || this.mWindowAlignment.mainAxis().isMinUnknown() || da >= (minScroll = this.mWindowAlignment.mainAxis().getMinScroll())) : !(this.mWindowAlignment.mainAxis().isMaxUnknown() || da <= (minScroll = this.mWindowAlignment.mainAxis().getMaxScroll())))) {
            da = minScroll;
        }
        if (da == 0) {
            return 0;
        }
        offsetChildrenPrimary(-da);
        if ((this.mFlag & 3) == 1) {
            updateScrollLimits();
            return da;
        }
        int childCount = getChildCount();
        if ((this.mFlag & 262144) == 0 ? da < 0 : da > 0) {
            prependVisibleItems();
        } else {
            appendVisibleItems();
        }
        boolean z2 = getChildCount() > childCount;
        int childCount2 = getChildCount();
        if ((262144 & this.mFlag) == 0 ? da < 0 : da > 0) {
            removeInvisibleViewsAtEnd();
        } else {
            removeInvisibleViewsAtFront();
        }
        if (getChildCount() >= childCount2) {
            z = false;
        }
        if (z2 | z) {
            updateRowSecondarySizeRefresh();
        }
        this.mBaseGridView.invalidate();
        updateScrollLimits();
        return da;
    }

    private int scrollDirectionSecondary(int dy) {
        if (dy == 0) {
            return 0;
        }
        offsetChildrenSecondary(-dy);
        this.mScrollOffsetSecondary += dy;
        updateSecondaryScrollLimits();
        this.mBaseGridView.invalidate();
        return dy;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void collectAdjacentPrefetchPositions(int dx, int dy, RecyclerView.State state, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i;
        try {
            saveContext(null, state);
            if (this.mOrientation != 0) {
                dx = dy;
            }
            if (getChildCount() != 0 && dx != 0) {
                if (dx < 0) {
                    i = -this.mExtraLayoutSpace;
                } else {
                    i = this.mSizePrimary + this.mExtraLayoutSpace;
                }
                this.mGrid.collectAdjacentPrefetchPositions(i, dx, layoutPrefetchRegistry);
            }
        } finally {
            leaveContext();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void collectInitialPrefetchPositions(int adapterItemCount, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i = this.mBaseGridView.mInitialPrefetchItemCount;
        if (adapterItemCount == 0 || i == 0) {
            return;
        }
        int max = Math.max(0, Math.min(this.mFocusPosition - ((i - 1) / 2), adapterItemCount - i));
        for (int i2 = max; i2 < adapterItemCount && i2 < max + i; i2++) {
            layoutPrefetchRegistry.addPosition(i2, 0);
        }
    }

    void updateScrollLimits() {
        int firstVisibleIndex;
        int lastVisibleIndex;
        int itemCount;
        int i;
        int i2;
        int i3;
        if (this.mState.getItemCount() == 0) {
            return;
        }
        if ((this.mFlag & 262144) == 0) {
            firstVisibleIndex = this.mGrid.getLastVisibleIndex();
            i = this.mState.getItemCount() - 1;
            lastVisibleIndex = this.mGrid.getFirstVisibleIndex();
            itemCount = 0;
        } else {
            firstVisibleIndex = this.mGrid.getFirstVisibleIndex();
            lastVisibleIndex = this.mGrid.getLastVisibleIndex();
            itemCount = this.mState.getItemCount() - 1;
            i = 0;
        }
        if (firstVisibleIndex < 0 || lastVisibleIndex < 0) {
            return;
        }
        boolean z = firstVisibleIndex == i;
        boolean z2 = lastVisibleIndex == itemCount;
        if (!z && this.mWindowAlignment.mainAxis().isMaxUnknown() && !z2 && this.mWindowAlignment.mainAxis().isMinUnknown()) {
            return;
        }
        int i4 = Integer.MAX_VALUE;
        if (z) {
            i4 = this.mGrid.findRowMax(true, sTwoInts);
            View findViewByPosition = findViewByPosition(sTwoInts[1]);
            i2 = getViewCenter(findViewByPosition);
            int[] alignMultiple = ((LayoutParams) findViewByPosition.getLayoutParams()).getAlignMultiple();
            if (alignMultiple != null && alignMultiple.length > 0) {
                i2 += alignMultiple[alignMultiple.length - 1] - alignMultiple[0];
            }
        } else {
            i2 = Integer.MAX_VALUE;
        }
        int i5 = Integer.MIN_VALUE;
        if (z2) {
            i5 = this.mGrid.findRowMin(false, sTwoInts);
            i3 = getViewCenter(findViewByPosition(sTwoInts[1]));
        } else {
            i3 = Integer.MIN_VALUE;
        }
        this.mWindowAlignment.mainAxis().updateMinMax(i5, i4, i3, i2);
    }

    private void updateSecondaryScrollLimits() {
        WindowAlignment.Axis secondAxis = this.mWindowAlignment.secondAxis();
        int paddingMin = secondAxis.getPaddingMin() - this.mScrollOffsetSecondary;
        int sizeSecondary = getSizeSecondary() + paddingMin;
        secondAxis.updateMinMax(paddingMin, sizeSecondary, paddingMin, sizeSecondary);
    }

    private void initScrollController() {
        this.mWindowAlignment.reset();
        this.mWindowAlignment.horizontal.setSize(getWidth());
        this.mWindowAlignment.vertical.setSize(getHeight());
        this.mWindowAlignment.horizontal.setPadding(getPaddingLeft(), getPaddingRight());
        this.mWindowAlignment.vertical.setPadding(getPaddingTop(), getPaddingBottom());
        this.mSizePrimary = this.mWindowAlignment.mainAxis().getSize();
        this.mScrollOffsetSecondary = 0;
    }

    private void updateScrollController() {
        this.mWindowAlignment.horizontal.setSize(getWidth());
        this.mWindowAlignment.vertical.setSize(getHeight());
        this.mWindowAlignment.horizontal.setPadding(getPaddingLeft(), getPaddingRight());
        this.mWindowAlignment.vertical.setPadding(getPaddingTop(), getPaddingBottom());
        this.mSizePrimary = this.mWindowAlignment.mainAxis().getSize();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void scrollToPosition(int position) {
        setSelection(position, 0, false, 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        setSelection(position, 0, true, 0);
    }

    public void setSelection(int position, int primaryScrollExtra) {
        setSelection(position, 0, false, primaryScrollExtra);
    }

    public void setSelectionSmooth(int position) {
        setSelection(position, 0, true, 0);
    }

    public void setSelectionWithSub(int position, int subposition, int primaryScrollExtra) {
        setSelection(position, subposition, false, primaryScrollExtra);
    }

    public int getSelection() {
        return this.mFocusPosition;
    }

    public void setSelection(int position, int subposition, boolean smooth, int primaryScrollExtra) {
        if ((this.mFocusPosition == position || position == -1) && subposition == this.mSubFocusPosition && primaryScrollExtra == this.mPrimaryScrollExtra) {
            return;
        }
        scrollToSelection(position, subposition, smooth, primaryScrollExtra);
    }

    void scrollToSelection(int position, int subposition, boolean smooth, int primaryScrollExtra) {
        this.mPrimaryScrollExtra = primaryScrollExtra;
        View findViewByPosition = findViewByPosition(position);
        boolean z = !isSmoothScrolling();
        if (z && !this.mBaseGridView.isLayoutRequested() && findViewByPosition != null && getAdapterPositionByView(findViewByPosition) == position) {
            this.mFlag |= 32;
            scrollToView(findViewByPosition, smooth);
            this.mFlag &= -33;
            return;
        }
        int i = this.mFlag;
        if ((i & 512) == 0 || (i & 64) != 0) {
            this.mFocusPosition = position;
            this.mSubFocusPosition = subposition;
            this.mFocusPositionOffset = Integer.MIN_VALUE;
        } else if (smooth && !this.mBaseGridView.isLayoutRequested()) {
            this.mFocusPosition = position;
            this.mSubFocusPosition = subposition;
            this.mFocusPositionOffset = Integer.MIN_VALUE;
            if (!hasDoneFirstLayout()) {
                Log.w(getTag(), "setSelectionSmooth should not be called before first layout pass");
                return;
            }
            int startPositionSmoothScroller = startPositionSmoothScroller(position);
            if (startPositionSmoothScroller == this.mFocusPosition) {
                return;
            }
            this.mFocusPosition = startPositionSmoothScroller;
            this.mSubFocusPosition = 0;
        } else {
            if (!z) {
                skipSmoothScrollerOnStopInternal();
                this.mBaseGridView.stopScroll();
            }
            if (!this.mBaseGridView.isLayoutRequested() && findViewByPosition != null && getAdapterPositionByView(findViewByPosition) == position) {
                this.mFlag |= 32;
                scrollToView(findViewByPosition, smooth);
                this.mFlag &= -33;
                return;
            }
            this.mFocusPosition = position;
            this.mSubFocusPosition = subposition;
            this.mFocusPositionOffset = Integer.MIN_VALUE;
            this.mFlag |= 256;
            requestLayout();
        }
    }

    int startPositionSmoothScroller(int position) {
        GridLinearSmoothScroller gridLinearSmoothScroller = new GridLinearSmoothScroller() { // from class: androidx.leanback.widget.GridLayoutManager.4
            @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
            public PointF computeScrollVectorForPosition(int targetPosition) {
                if (getChildCount() == 0) {
                    return null;
                }
                GridLayoutManager gridLayoutManager = GridLayoutManager.this;
                boolean z = false;
                int position2 = gridLayoutManager.getPosition(gridLayoutManager.getChildAt(0));
                GridLayoutManager gridLayoutManager2 = GridLayoutManager.this;
                int i = 1;
                if ((gridLayoutManager2.mFlag & 262144) == 0 ? targetPosition < position2 : targetPosition > position2) {
                    z = true;
                }
                if (z) {
                    i = -1;
                }
                if (gridLayoutManager2.mOrientation == 0) {
                    return new PointF(i, 0.0f);
                }
                return new PointF(0.0f, i);
            }
        };
        gridLinearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(gridLinearSmoothScroller);
        return gridLinearSmoothScroller.getTargetPosition();
    }

    void skipSmoothScrollerOnStopInternal() {
        GridLinearSmoothScroller gridLinearSmoothScroller = this.mCurrentSmoothScroller;
        if (gridLinearSmoothScroller != null) {
            gridLinearSmoothScroller.mSkipOnStopInternal = true;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void startSmoothScroll(RecyclerView.SmoothScroller smoothScroller) {
        skipSmoothScrollerOnStopInternal();
        super.startSmoothScroll(smoothScroller);
        if (smoothScroller.isRunning() && (smoothScroller instanceof GridLinearSmoothScroller)) {
            GridLinearSmoothScroller gridLinearSmoothScroller = (GridLinearSmoothScroller) smoothScroller;
            this.mCurrentSmoothScroller = gridLinearSmoothScroller;
            if (gridLinearSmoothScroller instanceof PendingMoveSmoothScroller) {
                this.mPendingMoveSmoothScroller = (PendingMoveSmoothScroller) gridLinearSmoothScroller;
                return;
            } else {
                this.mPendingMoveSmoothScroller = null;
                return;
            }
        }
        this.mCurrentSmoothScroller = null;
        this.mPendingMoveSmoothScroller = null;
    }

    void processPendingMovement(boolean forward) {
        if (forward) {
            if (hasCreatedLastItem()) {
                return;
            }
        } else if (hasCreatedFirstItem()) {
            return;
        }
        PendingMoveSmoothScroller pendingMoveSmoothScroller = this.mPendingMoveSmoothScroller;
        if (pendingMoveSmoothScroller != null) {
            if (forward) {
                pendingMoveSmoothScroller.increasePendingMoves();
                return;
            } else {
                pendingMoveSmoothScroller.decreasePendingMoves();
                return;
            }
        }
        boolean z = true;
        int i = forward ? 1 : -1;
        if (this.mNumRows <= 1) {
            z = false;
        }
        PendingMoveSmoothScroller pendingMoveSmoothScroller2 = new PendingMoveSmoothScroller(i, z);
        this.mFocusPositionOffset = 0;
        startSmoothScroll(pendingMoveSmoothScroller2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        Grid grid;
        int i;
        if (this.mFocusPosition != -1 && (grid = this.mGrid) != null && grid.getFirstVisibleIndex() >= 0 && (i = this.mFocusPositionOffset) != Integer.MIN_VALUE && positionStart <= this.mFocusPosition + i) {
            this.mFocusPositionOffset = i + itemCount;
        }
        this.mChildrenStates.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsChanged(RecyclerView recyclerView) {
        this.mFocusPositionOffset = 0;
        this.mChildrenStates.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        Grid grid;
        int i;
        int i2;
        int i3;
        if (this.mFocusPosition != -1 && (grid = this.mGrid) != null && grid.getFirstVisibleIndex() >= 0 && (i = this.mFocusPositionOffset) != Integer.MIN_VALUE && positionStart <= (i3 = (i2 = this.mFocusPosition) + i)) {
            if (positionStart + itemCount > i3) {
                int i4 = i + (positionStart - i3);
                this.mFocusPositionOffset = i4;
                this.mFocusPosition = i2 + i4;
                this.mFocusPositionOffset = Integer.MIN_VALUE;
            } else {
                this.mFocusPositionOffset = i - itemCount;
            }
        }
        this.mChildrenStates.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsMoved(RecyclerView recyclerView, int fromPosition, int toPosition, int itemCount) {
        int i;
        int i2 = this.mFocusPosition;
        if (i2 != -1 && (i = this.mFocusPositionOffset) != Integer.MIN_VALUE) {
            int i3 = i2 + i;
            if (fromPosition <= i3 && i3 < fromPosition + itemCount) {
                this.mFocusPositionOffset = i + (toPosition - fromPosition);
            } else if (fromPosition < i3 && toPosition > i3 - itemCount) {
                this.mFocusPositionOffset = i - itemCount;
            } else if (fromPosition > i3 && toPosition < i3) {
                this.mFocusPositionOffset = i + itemCount;
            }
        }
        this.mChildrenStates.clear();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount) {
        int i = itemCount + positionStart;
        while (positionStart < i) {
            this.mChildrenStates.remove(positionStart);
            positionStart++;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean onRequestChildFocus(RecyclerView parent, View child, View focused) {
        if ((this.mFlag & 32768) == 0 && getAdapterPositionByView(child) != -1 && (this.mFlag & 35) == 0) {
            scrollToView(child, focused, true);
        }
        return true;
    }

    private int getPrimaryAlignedScrollDistance(View view) {
        return this.mWindowAlignment.mainAxis().getScroll(getViewCenter(view));
    }

    private int getAdjustedPrimaryAlignedScrollDistance(int scrollPrimary, View view, View childView) {
        int subPositionByView = getSubPositionByView(view, childView);
        if (subPositionByView != 0) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            return scrollPrimary + (layoutParams.getAlignMultiple()[subPositionByView] - layoutParams.getAlignMultiple()[0]);
        }
        return scrollPrimary;
    }

    private int getSecondaryScrollDistance(View view) {
        return this.mWindowAlignment.secondAxis().getScroll(getViewCenterSecondary(view));
    }

    void scrollToView(View view, boolean smooth) {
        scrollToView(view, view == null ? null : view.findFocus(), smooth);
    }

    void scrollToView(View view, boolean smooth, int extraDelta, int extraDeltaSecondary) {
        scrollToView(view, view == null ? null : view.findFocus(), smooth, extraDelta, extraDeltaSecondary);
    }

    private void scrollToView(View view, View childView, boolean smooth) {
        scrollToView(view, childView, smooth, 0, 0);
    }

    private void scrollToView(View view, View childView, boolean smooth, int extraDelta, int extraDeltaSecondary) {
        if ((this.mFlag & 64) != 0) {
            return;
        }
        int adapterPositionByView = getAdapterPositionByView(view);
        int subPositionByView = getSubPositionByView(view, childView);
        if (adapterPositionByView != this.mFocusPosition || subPositionByView != this.mSubFocusPosition) {
            this.mFocusPosition = adapterPositionByView;
            this.mSubFocusPosition = subPositionByView;
            this.mFocusPositionOffset = 0;
            if ((this.mFlag & 3) != 1) {
                dispatchChildSelected();
            }
            if (this.mBaseGridView.isChildrenDrawingOrderEnabledInternal()) {
                this.mBaseGridView.invalidate();
            }
        }
        if (view == null) {
            return;
        }
        if (!view.hasFocus() && this.mBaseGridView.hasFocus()) {
            view.requestFocus();
        }
        if ((this.mFlag & 131072) == 0 && smooth) {
            return;
        }
        if (!getScrollPosition(view, childView, sTwoInts) && extraDelta == 0 && extraDeltaSecondary == 0) {
            return;
        }
        int[] iArr = sTwoInts;
        scrollGrid(iArr[0] + extraDelta, iArr[1] + extraDeltaSecondary, smooth);
    }

    boolean getScrollPosition(View view, View childView, int[] deltas) {
        int i = this.mFocusScrollStrategy;
        if (i != 1 && i != 2) {
            return getAlignedPosition(view, childView, deltas);
        }
        return getNoneAlignedPosition(view, deltas);
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x009f, code lost:
        if (r2 != null) goto L15;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00ba  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean getNoneAlignedPosition(View view, int[] deltas) {
        View view2;
        int i;
        int viewMax;
        int secondaryScrollDistance;
        int adapterPositionByView = getAdapterPositionByView(view);
        int viewMin = getViewMin(view);
        int viewMax2 = getViewMax(view);
        int paddingMin = this.mWindowAlignment.mainAxis().getPaddingMin();
        int clientSize = this.mWindowAlignment.mainAxis().getClientSize();
        int rowIndex = this.mGrid.getRowIndex(adapterPositionByView);
        View view3 = null;
        if (viewMin < paddingMin) {
            if (this.mFocusScrollStrategy == 2) {
                View view4 = view;
                while (true) {
                    if (!prependOneColumnVisibleItems()) {
                        view2 = null;
                        view3 = view4;
                        break;
                    }
                    Grid grid = this.mGrid;
                    CircularIntArray circularIntArray = grid.getItemPositionsInRows(grid.getFirstVisibleIndex(), adapterPositionByView)[rowIndex];
                    View findViewByPosition = findViewByPosition(circularIntArray.get(0));
                    if (viewMax2 - getViewMin(findViewByPosition) <= clientSize) {
                        view4 = findViewByPosition;
                    } else if (circularIntArray.size() > 2) {
                        view2 = null;
                        view3 = findViewByPosition(circularIntArray.get(2));
                    } else {
                        view2 = null;
                        view3 = findViewByPosition;
                    }
                }
            } else {
                view2 = null;
                view3 = view;
            }
        } else if (viewMax2 <= clientSize + paddingMin) {
            view2 = null;
        } else if (this.mFocusScrollStrategy == 2) {
            while (true) {
                Grid grid2 = this.mGrid;
                CircularIntArray circularIntArray2 = grid2.getItemPositionsInRows(adapterPositionByView, grid2.getLastVisibleIndex())[rowIndex];
                view2 = findViewByPosition(circularIntArray2.get(circularIntArray2.size() - 1));
                if (getViewMax(view2) - viewMin <= clientSize) {
                    if (!appendOneColumnVisibleItems()) {
                        break;
                    }
                } else {
                    view2 = null;
                    break;
                }
            }
        } else {
            view2 = view;
        }
        if (view3 != null) {
            viewMax = getViewMin(view3);
        } else if (view2 != null) {
            viewMax = getViewMax(view2);
            paddingMin += clientSize;
        } else {
            i = 0;
            if (view3 == null) {
                view = view3;
            } else if (view2 != null) {
                view = view2;
            }
            secondaryScrollDistance = getSecondaryScrollDistance(view);
            if (i != 0 && secondaryScrollDistance == 0) {
                return false;
            }
            deltas[0] = i;
            deltas[1] = secondaryScrollDistance;
            return true;
        }
        i = viewMax - paddingMin;
        if (view3 == null) {
        }
        secondaryScrollDistance = getSecondaryScrollDistance(view);
        if (i != 0) {
        }
        deltas[0] = i;
        deltas[1] = secondaryScrollDistance;
        return true;
    }

    private boolean getAlignedPosition(View view, View childView, int[] deltas) {
        int primaryAlignedScrollDistance = getPrimaryAlignedScrollDistance(view);
        if (childView != null) {
            primaryAlignedScrollDistance = getAdjustedPrimaryAlignedScrollDistance(primaryAlignedScrollDistance, view, childView);
        }
        int secondaryScrollDistance = getSecondaryScrollDistance(view);
        int i = primaryAlignedScrollDistance + this.mPrimaryScrollExtra;
        if (i != 0 || secondaryScrollDistance != 0) {
            deltas[0] = i;
            deltas[1] = secondaryScrollDistance;
            return true;
        }
        deltas[0] = 0;
        deltas[1] = 0;
        return false;
    }

    private void scrollGrid(int scrollPrimary, int scrollSecondary, boolean smooth) {
        if ((this.mFlag & 3) == 1) {
            scrollDirectionPrimary(scrollPrimary);
            scrollDirectionSecondary(scrollSecondary);
            return;
        }
        if (this.mOrientation != 0) {
            scrollSecondary = scrollPrimary;
            scrollPrimary = scrollSecondary;
        }
        if (smooth) {
            this.mBaseGridView.smoothScrollBy(scrollPrimary, scrollSecondary);
            return;
        }
        this.mBaseGridView.scrollBy(scrollPrimary, scrollSecondary);
        dispatchChildSelectedAndPositioned();
    }

    public boolean isScrollEnabled() {
        return (this.mFlag & 131072) != 0;
    }

    private int findImmediateChildIndex(View view) {
        BaseGridView baseGridView;
        View findContainingItemView;
        if (view == null || (baseGridView = this.mBaseGridView) == null || view == baseGridView || (findContainingItemView = findContainingItemView(view)) == null) {
            return -1;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) == findContainingItemView) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        if (gainFocus) {
            int i = this.mFocusPosition;
            while (true) {
                View findViewByPosition = findViewByPosition(i);
                if (findViewByPosition == null) {
                    return;
                }
                if (findViewByPosition.getVisibility() == 0 && findViewByPosition.hasFocusable()) {
                    findViewByPosition.requestFocus();
                    return;
                }
                i++;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00ca A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00cb  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View onInterceptFocusSearch(View focused, int direction) {
        View view;
        if ((this.mFlag & 32768) != 0) {
            return focused;
        }
        FocusFinder focusFinder = FocusFinder.getInstance();
        View view2 = null;
        if (direction == 2 || direction == 1) {
            if (canScrollVertically()) {
                view2 = focusFinder.findNextFocus(this.mBaseGridView, focused, direction == 2 ? 130 : 33);
            }
            if (canScrollHorizontally()) {
                view = focusFinder.findNextFocus(this.mBaseGridView, focused, (getLayoutDirection() == 1) ^ (direction == 2) ? 66 : 17);
            } else {
                view = view2;
            }
        } else {
            view = focusFinder.findNextFocus(this.mBaseGridView, focused, direction);
        }
        if (view != null) {
            return view;
        }
        if (this.mBaseGridView.getDescendantFocusability() == 393216) {
            return this.mBaseGridView.getParent().focusSearch(focused, direction);
        }
        int movement = getMovement(direction);
        boolean z = this.mBaseGridView.getScrollState() != 0;
        if (movement == 1) {
            if (z || (this.mFlag & 4096) == 0) {
                view = focused;
            }
            if ((this.mFlag & 131072) != 0 && !hasCreatedLastItem()) {
                processPendingMovement(true);
                view = focused;
            }
            if (view == null) {
                return view;
            }
            View focusSearch = this.mBaseGridView.getParent().focusSearch(focused, direction);
            return focusSearch != null ? focusSearch : focused != null ? focused : this.mBaseGridView;
        } else if (movement == 0) {
            if (z || (this.mFlag & 2048) == 0) {
                view = focused;
            }
            if ((this.mFlag & 131072) != 0 && !hasCreatedFirstItem()) {
                processPendingMovement(false);
                view = focused;
            }
            if (view == null) {
            }
        } else if (movement == 3) {
            if (view == null) {
            }
        } else if (view == null) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:43:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00b5  */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onAddFocusables(RecyclerView recyclerView, ArrayList<View> views, int direction, int focusableMode) {
        int i;
        View childAt;
        int i2;
        if ((this.mFlag & 32768) != 0) {
            return true;
        }
        if (recyclerView.hasFocus()) {
            if (this.mPendingMoveSmoothScroller != null) {
                return true;
            }
            int movement = getMovement(direction);
            int findImmediateChildIndex = findImmediateChildIndex(recyclerView.findFocus());
            int adapterPositionByIndex = getAdapterPositionByIndex(findImmediateChildIndex);
            View findViewByPosition = adapterPositionByIndex == -1 ? null : findViewByPosition(adapterPositionByIndex);
            if (findViewByPosition != null) {
                findViewByPosition.addFocusables(views, direction, focusableMode);
            }
            if (this.mGrid == null || getChildCount() == 0) {
                return true;
            }
            if ((movement == 3 || movement == 2) && this.mGrid.getNumRows() <= 1) {
                return true;
            }
            Grid grid = this.mGrid;
            int i3 = (grid == null || findViewByPosition == null) ? -1 : grid.mo120getLocation(adapterPositionByIndex).row;
            int size = views.size();
            int i4 = (movement == 1 || movement == 3) ? 1 : -1;
            int childCount = i4 > 0 ? getChildCount() - 1 : 0;
            if (findImmediateChildIndex == -1) {
                i = i4 > 0 ? 0 : getChildCount() - 1;
            } else {
                i = findImmediateChildIndex + i4;
            }
            int i5 = i;
            while (true) {
                if (i4 > 0) {
                    if (i5 > childCount) {
                        break;
                    }
                    childAt = getChildAt(i5);
                    if (childAt.getVisibility() == 0 && childAt.hasFocusable()) {
                        if (findViewByPosition != null) {
                            childAt.addFocusables(views, direction, focusableMode);
                            if (views.size() > size) {
                                break;
                            }
                        } else {
                            int adapterPositionByIndex2 = getAdapterPositionByIndex(i5);
                            Grid.Location mo120getLocation = this.mGrid.mo120getLocation(adapterPositionByIndex2);
                            if (mo120getLocation != null) {
                                if (movement == 1) {
                                    if (mo120getLocation.row == i3 && adapterPositionByIndex2 > adapterPositionByIndex) {
                                        childAt.addFocusables(views, direction, focusableMode);
                                        if (views.size() > size) {
                                            break;
                                        }
                                    }
                                } else if (movement == 0) {
                                    if (mo120getLocation.row == i3 && adapterPositionByIndex2 < adapterPositionByIndex) {
                                        childAt.addFocusables(views, direction, focusableMode);
                                        if (views.size() > size) {
                                            break;
                                        }
                                    }
                                } else if (movement == 3) {
                                    int i6 = mo120getLocation.row;
                                    if (i6 != i3) {
                                        if (i6 < i3) {
                                            break;
                                        }
                                        childAt.addFocusables(views, direction, focusableMode);
                                    }
                                } else if (movement == 2 && (i2 = mo120getLocation.row) != i3) {
                                    if (i2 > i3) {
                                        break;
                                    }
                                    childAt.addFocusables(views, direction, focusableMode);
                                }
                            }
                        }
                    }
                    i5 += i4;
                } else {
                    if (i5 < childCount) {
                        break;
                    }
                    childAt = getChildAt(i5);
                    if (childAt.getVisibility() == 0) {
                        if (findViewByPosition != null) {
                        }
                    }
                    i5 += i4;
                }
            }
        } else {
            int size2 = views.size();
            if (this.mFocusScrollStrategy != 0) {
                int paddingMin = this.mWindowAlignment.mainAxis().getPaddingMin();
                int clientSize = this.mWindowAlignment.mainAxis().getClientSize() + paddingMin;
                int childCount2 = getChildCount();
                for (int i7 = 0; i7 < childCount2; i7++) {
                    View childAt2 = getChildAt(i7);
                    if (childAt2.getVisibility() == 0 && getViewMin(childAt2) >= paddingMin && getViewMax(childAt2) <= clientSize) {
                        childAt2.addFocusables(views, direction, focusableMode);
                    }
                }
                if (views.size() == size2) {
                    int childCount3 = getChildCount();
                    for (int i8 = 0; i8 < childCount3; i8++) {
                        View childAt3 = getChildAt(i8);
                        if (childAt3.getVisibility() == 0) {
                            childAt3.addFocusables(views, direction, focusableMode);
                        }
                    }
                }
            } else {
                View findViewByPosition2 = findViewByPosition(this.mFocusPosition);
                if (findViewByPosition2 != null) {
                    findViewByPosition2.addFocusables(views, direction, focusableMode);
                }
            }
            if (views.size() == size2 && recyclerView.isFocusable()) {
                views.add(recyclerView);
            }
        }
        return true;
    }

    boolean hasCreatedLastItem() {
        int itemCount = getItemCount();
        return itemCount == 0 || this.mBaseGridView.findViewHolderForAdapterPosition(itemCount - 1) != null;
    }

    boolean hasCreatedFirstItem() {
        return getItemCount() == 0 || this.mBaseGridView.findViewHolderForAdapterPosition(0) != null;
    }

    boolean isItemFullyVisible(int pos) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = this.mBaseGridView.findViewHolderForAdapterPosition(pos);
        return findViewHolderForAdapterPosition != null && findViewHolderForAdapterPosition.itemView.getLeft() >= 0 && findViewHolderForAdapterPosition.itemView.getRight() <= this.mBaseGridView.getWidth() && findViewHolderForAdapterPosition.itemView.getTop() >= 0 && findViewHolderForAdapterPosition.itemView.getBottom() <= this.mBaseGridView.getHeight();
    }

    boolean canScrollTo(View view) {
        return view.getVisibility() == 0 && (!hasFocus() || view.hasFocusable());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean gridOnRequestFocusInDescendants(RecyclerView recyclerView, int direction, Rect previouslyFocusedRect) {
        int i = this.mFocusScrollStrategy;
        if (i != 1 && i != 2) {
            return gridOnRequestFocusInDescendantsAligned(direction, previouslyFocusedRect);
        }
        return gridOnRequestFocusInDescendantsUnaligned(direction, previouslyFocusedRect);
    }

    private boolean gridOnRequestFocusInDescendantsAligned(int direction, Rect previouslyFocusedRect) {
        View findViewByPosition = findViewByPosition(this.mFocusPosition);
        if (findViewByPosition != null) {
            return findViewByPosition.requestFocus(direction, previouslyFocusedRect);
        }
        return false;
    }

    private boolean gridOnRequestFocusInDescendantsUnaligned(int direction, Rect previouslyFocusedRect) {
        int i;
        int i2;
        int childCount = getChildCount();
        int i3 = -1;
        if ((direction & 2) != 0) {
            i3 = childCount;
            i = 0;
            i2 = 1;
        } else {
            i = childCount - 1;
            i2 = -1;
        }
        int paddingMin = this.mWindowAlignment.mainAxis().getPaddingMin();
        int clientSize = this.mWindowAlignment.mainAxis().getClientSize() + paddingMin;
        while (i != i3) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0 && getViewMin(childAt) >= paddingMin && getViewMax(childAt) <= clientSize && childAt.requestFocus(direction, previouslyFocusedRect)) {
                return true;
            }
            i += i2;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0035, code lost:
        if (r10 != 130) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x003d, code lost:
        if ((r9.mFlag & 524288) == 0) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0043, code lost:
        if ((r9.mFlag & 524288) == 0) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0018, code lost:
        if (r10 != 130) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:?, code lost:
        return 3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int getMovement(int direction) {
        int i = this.mOrientation;
        if (i != 0) {
            if (i == 1) {
                if (direction != 17) {
                    if (direction == 33) {
                        return 0;
                    }
                    if (direction != 66) {
                    }
                }
            }
            return 17;
        }
        if (direction != 17) {
            if (direction != 33) {
                if (direction == 66) {
                    if ((this.mFlag & 262144) != 0) {
                        return 0;
                    }
                }
            }
            return 2;
        } else if ((this.mFlag & 262144) == 0) {
            return 0;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getChildDrawingOrder(RecyclerView recyclerView, int childCount, int i) {
        int indexOfChild;
        View findViewByPosition = findViewByPosition(this.mFocusPosition);
        return (findViewByPosition != null && i >= (indexOfChild = recyclerView.indexOfChild(findViewByPosition))) ? i < childCount + (-1) ? ((indexOfChild + childCount) - 1) - i : indexOfChild : i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onAdapterChanged(RecyclerView.Adapter oldAdapter, RecyclerView.Adapter newAdapter) {
        if (oldAdapter != null) {
            discardLayoutInfo();
            this.mFocusPosition = -1;
            this.mFocusPositionOffset = 0;
            this.mChildrenStates.clear();
        }
        if (newAdapter instanceof FacetProviderAdapter) {
            this.mFacetProviderAdapter = (FacetProviderAdapter) newAdapter;
        } else {
            this.mFacetProviderAdapter = null;
        }
        super.onAdapterChanged(oldAdapter, newAdapter);
    }

    private void discardLayoutInfo() {
        this.mGrid = null;
        this.mRowSizeSecondary = null;
        this.mFlag &= -1025;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: classes.dex */
    public static final class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.leanback.widget.GridLayoutManager.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public SavedState mo116createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public SavedState[] mo117newArray(int size) {
                return new SavedState[size];
            }
        };
        Bundle childStates;
        int index;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(this.index);
            out.writeBundle(this.childStates);
        }

        SavedState(Parcel in) {
            this.childStates = Bundle.EMPTY;
            this.index = in.readInt();
            this.childStates = in.readBundle(GridLayoutManager.class.getClassLoader());
        }

        SavedState() {
            this.childStates = Bundle.EMPTY;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState();
        savedState.index = getSelection();
        Bundle saveAsBundle = this.mChildrenStates.saveAsBundle();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            int adapterPositionByView = getAdapterPositionByView(childAt);
            if (adapterPositionByView != -1) {
                saveAsBundle = this.mChildrenStates.saveOnScreenView(saveAsBundle, childAt, adapterPositionByView);
            }
        }
        savedState.childStates = saveAsBundle;
        return savedState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onChildRecycled(RecyclerView.ViewHolder holder) {
        int absoluteAdapterPosition = holder.getAbsoluteAdapterPosition();
        if (absoluteAdapterPosition != -1) {
            this.mChildrenStates.saveOffscreenView(holder.itemView, absoluteAdapterPosition);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            return;
        }
        SavedState savedState = (SavedState) state;
        this.mFocusPosition = savedState.index;
        this.mFocusPositionOffset = 0;
        this.mChildrenStates.loadFromBundle(savedState.childStates);
        this.mFlag |= 256;
        requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Grid grid;
        if (this.mOrientation == 0 && (grid = this.mGrid) != null) {
            return grid.getNumRows();
        }
        return super.getRowCountForAccessibility(recycler, state);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Grid grid;
        if (this.mOrientation == 1 && (grid = this.mGrid) != null) {
            return grid.getNumRows();
        }
        return super.getColumnCountForAccessibility(recycler, state);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View host, AccessibilityNodeInfoCompat info) {
        ViewGroup.LayoutParams layoutParams = host.getLayoutParams();
        if (this.mGrid == null || !(layoutParams instanceof LayoutParams)) {
            return;
        }
        int absoluteAdapterPosition = ((LayoutParams) layoutParams).getAbsoluteAdapterPosition();
        int rowIndex = absoluteAdapterPosition >= 0 ? this.mGrid.getRowIndex(absoluteAdapterPosition) : -1;
        if (rowIndex < 0) {
            return;
        }
        int numRows = absoluteAdapterPosition / this.mGrid.getNumRows();
        if (this.mOrientation == 0) {
            info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(rowIndex, 1, numRows, 1, false, false));
        } else {
            info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(numRows, 1, rowIndex, 1, false, false));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002c, code lost:
        if (r6 != false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x004c, code lost:
        r8 = 4096;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0037, code lost:
        if (r6 != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x004a, code lost:
        if (r8 == androidx.core.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN.getId()) goto L16;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean performAccessibilityAction(RecyclerView.Recycler recycler, RecyclerView.State state, int action, Bundle args) {
        if (!isScrollEnabled()) {
            return true;
        }
        saveContext(recycler, state);
        boolean z = (this.mFlag & 262144) != 0;
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.mOrientation == 0) {
                if (action != AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT.getId()) {
                    if (action == AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT.getId()) {
                    }
                }
            } else {
                if (action != AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_UP.getId()) {
                }
                action = 8192;
            }
        }
        int i = this.mFocusPosition;
        boolean z2 = i == 0 && action == 8192;
        boolean z3 = i == state.getItemCount() - 1 && action == 4096;
        if (z2 || z3) {
            sendTypeViewScrolledAccessibilityEvent();
        } else if (action == 4096) {
            processPendingMovement(true);
            processSelectionMoves(false, 1);
        } else if (action == 8192) {
            processPendingMovement(false);
            processSelectionMoves(false, -1);
        }
        leaveContext();
        return true;
    }

    private void sendTypeViewScrolledAccessibilityEvent() {
        AccessibilityEvent obtain = AccessibilityEvent.obtain(4096);
        this.mBaseGridView.onInitializeAccessibilityEvent(obtain);
        BaseGridView baseGridView = this.mBaseGridView;
        baseGridView.requestSendAccessibilityEvent(baseGridView, obtain);
    }

    int processSelectionMoves(boolean preventScroll, int moves) {
        Grid grid = this.mGrid;
        if (grid == null) {
            return moves;
        }
        int i = this.mFocusPosition;
        int rowIndex = i != -1 ? grid.getRowIndex(i) : -1;
        View view = null;
        int childCount = getChildCount();
        for (int i2 = 0; i2 < childCount && moves != 0; i2++) {
            int i3 = moves > 0 ? i2 : (childCount - 1) - i2;
            View childAt = getChildAt(i3);
            if (canScrollTo(childAt)) {
                int adapterPositionByIndex = getAdapterPositionByIndex(i3);
                int rowIndex2 = this.mGrid.getRowIndex(adapterPositionByIndex);
                if (rowIndex == -1) {
                    i = adapterPositionByIndex;
                    view = childAt;
                    rowIndex = rowIndex2;
                } else if (rowIndex2 == rowIndex && ((moves > 0 && adapterPositionByIndex > i) || (moves < 0 && adapterPositionByIndex < i))) {
                    moves = moves > 0 ? moves - 1 : moves + 1;
                    i = adapterPositionByIndex;
                    view = childAt;
                }
            }
        }
        if (view != null) {
            if (preventScroll) {
                if (hasFocus()) {
                    this.mFlag |= 32;
                    view.requestFocus();
                    this.mFlag &= -33;
                }
                this.mFocusPosition = i;
                this.mSubFocusPosition = 0;
            } else {
                scrollToView(view, true);
            }
        }
        return moves;
    }

    private void addA11yActionMovingBackward(AccessibilityNodeInfoCompat info, boolean reverseFlowPrimary) {
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat;
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.mOrientation == 0) {
                if (reverseFlowPrimary) {
                    accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT;
                } else {
                    accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT;
                }
                info.addAction(accessibilityActionCompat);
            } else {
                info.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_UP);
            }
        } else {
            info.addAction(8192);
        }
        info.setScrollable(true);
    }

    private void addA11yActionMovingForward(AccessibilityNodeInfoCompat info, boolean reverseFlowPrimary) {
        AccessibilityNodeInfoCompat.AccessibilityActionCompat accessibilityActionCompat;
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.mOrientation == 0) {
                if (reverseFlowPrimary) {
                    accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_LEFT;
                } else {
                    accessibilityActionCompat = AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_RIGHT;
                }
                info.addAction(accessibilityActionCompat);
            } else {
                info.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_SCROLL_DOWN);
            }
        } else {
            info.addAction(4096);
        }
        info.setScrollable(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfo(RecyclerView.Recycler recycler, RecyclerView.State state, AccessibilityNodeInfoCompat info) {
        saveContext(recycler, state);
        int itemCount = state.getItemCount();
        int i = this.mFlag;
        boolean z = (262144 & i) != 0;
        if ((i & 2048) == 0 || (itemCount > 1 && !isItemFullyVisible(0))) {
            addA11yActionMovingBackward(info, z);
        }
        if ((this.mFlag & 4096) == 0 || (itemCount > 1 && !isItemFullyVisible(itemCount - 1))) {
            addA11yActionMovingForward(info, z);
        }
        info.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(getRowCountForAccessibility(recycler, state), getColumnCountForAccessibility(recycler, state), isLayoutHierarchical(recycler, state), getSelectionModeForAccessibility(recycler, state)));
        leaveContext();
    }
}
