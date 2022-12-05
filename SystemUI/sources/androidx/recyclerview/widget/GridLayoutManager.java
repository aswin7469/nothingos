package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
/* loaded from: classes.dex */
public class GridLayoutManager extends LinearLayoutManager {
    int[] mCachedBorders;
    View[] mSet;
    private boolean mUsingSpansToEstimateScrollBarDimensions;
    boolean mPendingSpanCountChange = false;
    int mSpanCount = -1;
    final SparseIntArray mPreLayoutSpanSizeCache = new SparseIntArray();
    final SparseIntArray mPreLayoutSpanIndexCache = new SparseIntArray();
    SpanSizeLookup mSpanSizeLookup = new DefaultSpanSizeLookup();
    final Rect mDecorInsets = new Rect();

    public GridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setSpanCount(RecyclerView.LayoutManager.getProperties(context, attrs, defStyleAttr, defStyleRes).spanCount);
    }

    public GridLayoutManager(Context context, int spanCount) {
        super(context);
        setSpanCount(spanCount);
    }

    public GridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        setSpanCount(spanCount);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public void setStackFromEnd(boolean stackFromEnd) {
        if (stackFromEnd) {
            throw new UnsupportedOperationException("GridLayoutManager does not support stack from end. Consider using reverse layout");
        }
        super.setStackFromEnd(false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getRowCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 0) {
            return this.mSpanCount;
        }
        if (state.getItemCount() >= 1) {
            return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int getColumnCountForAccessibility(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            return this.mSpanCount;
        }
        if (state.getItemCount() >= 1) {
            return getSpanGroupIndex(recycler, state, state.getItemCount() - 1) + 1;
        }
        return 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onInitializeAccessibilityNodeInfoForItem(RecyclerView.Recycler recycler, RecyclerView.State state, View host, AccessibilityNodeInfoCompat info) {
        ViewGroup.LayoutParams layoutParams = host.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.onInitializeAccessibilityNodeInfoForItem(host, info);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        int spanGroupIndex = getSpanGroupIndex(recycler, state, layoutParams2.getViewLayoutPosition());
        if (this.mOrientation == 0) {
            info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), spanGroupIndex, 1, false, false));
        } else {
            info.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(spanGroupIndex, 1, layoutParams2.getSpanIndex(), layoutParams2.getSpanSize(), false, false));
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            cachePreLayoutSpanMapping();
        }
        super.onLayoutChildren(recycler, state);
        clearPreLayoutSpanMappingCache();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        this.mPendingSpanCountChange = false;
    }

    private void clearPreLayoutSpanMappingCache() {
        this.mPreLayoutSpanSizeCache.clear();
        this.mPreLayoutSpanIndexCache.clear();
    }

    private void cachePreLayoutSpanMapping() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
            int viewLayoutPosition = layoutParams.getViewLayoutPosition();
            this.mPreLayoutSpanSizeCache.put(viewLayoutPosition, layoutParams.getSpanSize());
            this.mPreLayoutSpanIndexCache.put(viewLayoutPosition, layoutParams.getSpanIndex());
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsChanged(RecyclerView recyclerView) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsUpdated(RecyclerView recyclerView, int positionStart, int itemCount, Object payload) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onItemsMoved(RecyclerView recyclerView, int from, int to, int itemCount) {
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        this.mSpanSizeLookup.invalidateSpanGroupIndexCache();
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        if (this.mOrientation == 0) {
            return new LayoutParams(-2, -1);
        }
        return new LayoutParams(-1, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return new LayoutParams(c, attrs);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return new LayoutParams((ViewGroup.MarginLayoutParams) lp);
        }
        return new LayoutParams(lp);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        return lp instanceof LayoutParams;
    }

    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        this.mSpanSizeLookup = spanSizeLookup;
    }

    private void updateMeasurements() {
        int height;
        int paddingTop;
        if (getOrientation() == 1) {
            height = getWidth() - getPaddingRight();
            paddingTop = getPaddingLeft();
        } else {
            height = getHeight() - getPaddingBottom();
            paddingTop = getPaddingTop();
        }
        calculateItemBorders(height - paddingTop);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void setMeasuredDimension(Rect childrenBounds, int wSpec, int hSpec) {
        int chooseSize;
        int chooseSize2;
        if (this.mCachedBorders == null) {
            super.setMeasuredDimension(childrenBounds, wSpec, hSpec);
        }
        int paddingLeft = getPaddingLeft() + getPaddingRight();
        int paddingTop = getPaddingTop() + getPaddingBottom();
        if (this.mOrientation == 1) {
            chooseSize2 = RecyclerView.LayoutManager.chooseSize(hSpec, childrenBounds.height() + paddingTop, getMinimumHeight());
            int[] iArr = this.mCachedBorders;
            chooseSize = RecyclerView.LayoutManager.chooseSize(wSpec, iArr[iArr.length - 1] + paddingLeft, getMinimumWidth());
        } else {
            chooseSize = RecyclerView.LayoutManager.chooseSize(wSpec, childrenBounds.width() + paddingLeft, getMinimumWidth());
            int[] iArr2 = this.mCachedBorders;
            chooseSize2 = RecyclerView.LayoutManager.chooseSize(hSpec, iArr2[iArr2.length - 1] + paddingTop, getMinimumHeight());
        }
        setMeasuredDimension(chooseSize, chooseSize2);
    }

    private void calculateItemBorders(int totalSpace) {
        this.mCachedBorders = calculateItemBorders(this.mCachedBorders, this.mSpanCount, totalSpace);
    }

    static int[] calculateItemBorders(int[] cachedBorders, int spanCount, int totalSpace) {
        int i;
        if (cachedBorders == null || cachedBorders.length != spanCount + 1 || cachedBorders[cachedBorders.length - 1] != totalSpace) {
            cachedBorders = new int[spanCount + 1];
        }
        int i2 = 0;
        cachedBorders[0] = 0;
        int i3 = totalSpace / spanCount;
        int i4 = totalSpace % spanCount;
        int i5 = 0;
        for (int i6 = 1; i6 <= spanCount; i6++) {
            i2 += i4;
            if (i2 <= 0 || spanCount - i2 >= i4) {
                i = i3;
            } else {
                i = i3 + 1;
                i2 -= spanCount;
            }
            i5 += i;
            cachedBorders[i6] = i5;
        }
        return cachedBorders;
    }

    int getSpaceForSpanRange(int startSpan, int spanSize) {
        if (this.mOrientation == 1 && isLayoutRTL()) {
            int[] iArr = this.mCachedBorders;
            int i = this.mSpanCount;
            return iArr[i - startSpan] - iArr[(i - startSpan) - spanSize];
        }
        int[] iArr2 = this.mCachedBorders;
        return iArr2[spanSize + startSpan] - iArr2[startSpan];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.recyclerview.widget.LinearLayoutManager
    public void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int itemDirection) {
        super.onAnchorReady(recycler, state, anchorInfo, itemDirection);
        updateMeasurements();
        if (state.getItemCount() > 0 && !state.isPreLayout()) {
            ensureAnchorIsInCorrectSpan(recycler, state, anchorInfo, itemDirection);
        }
        ensureViewSet();
    }

    private void ensureViewSet() {
        View[] viewArr = this.mSet;
        if (viewArr == null || viewArr.length != this.mSpanCount) {
            this.mSet = new View[this.mSpanCount];
        }
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollHorizontallyBy(dx, recycler, state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        updateMeasurements();
        ensureViewSet();
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    private void ensureAnchorIsInCorrectSpan(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int itemDirection) {
        boolean z = itemDirection == 1;
        int spanIndex = getSpanIndex(recycler, state, anchorInfo.mPosition);
        if (z) {
            while (spanIndex > 0) {
                int i = anchorInfo.mPosition;
                if (i <= 0) {
                    return;
                }
                int i2 = i - 1;
                anchorInfo.mPosition = i2;
                spanIndex = getSpanIndex(recycler, state, i2);
            }
            return;
        }
        int itemCount = state.getItemCount() - 1;
        int i3 = anchorInfo.mPosition;
        while (i3 < itemCount) {
            int i4 = i3 + 1;
            int spanIndex2 = getSpanIndex(recycler, state, i4);
            if (spanIndex2 <= spanIndex) {
                break;
            }
            i3 = i4;
            spanIndex = spanIndex2;
        }
        anchorInfo.mPosition = i3;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    View findReferenceChild(RecyclerView.Recycler recycler, RecyclerView.State state, boolean layoutFromEnd, boolean traverseChildrenInReverseOrder) {
        int i;
        int childCount = getChildCount();
        int i2 = -1;
        int i3 = 1;
        if (traverseChildrenInReverseOrder) {
            i = getChildCount() - 1;
            i3 = -1;
        } else {
            i2 = childCount;
            i = 0;
        }
        int itemCount = state.getItemCount();
        ensureLayoutState();
        int startAfterPadding = this.mOrientationHelper.getStartAfterPadding();
        int endAfterPadding = this.mOrientationHelper.getEndAfterPadding();
        View view = null;
        View view2 = null;
        while (i != i2) {
            View childAt = getChildAt(i);
            int position = getPosition(childAt);
            if (position >= 0 && position < itemCount && getSpanIndex(recycler, state, position) == 0) {
                if (((RecyclerView.LayoutParams) childAt.getLayoutParams()).isItemRemoved()) {
                    if (view2 == null) {
                        view2 = childAt;
                    }
                } else if (this.mOrientationHelper.getDecoratedStart(childAt) < endAfterPadding && this.mOrientationHelper.getDecoratedEnd(childAt) >= startAfterPadding) {
                    return childAt;
                } else {
                    if (view == null) {
                        view = childAt;
                    }
                }
            }
            i += i3;
        }
        return view != null ? view : view2;
    }

    private int getSpanGroupIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int viewPosition) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanGroupIndex(viewPosition, this.mSpanCount);
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(viewPosition);
        if (convertPreLayoutPositionToPostLayout == -1) {
            Log.w("GridLayoutManager", "Cannot find span size for pre layout position. " + viewPosition);
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanGroupIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }

    private int getSpanIndex(RecyclerView.Recycler recycler, RecyclerView.State state, int pos) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getCachedSpanIndex(pos, this.mSpanCount);
        }
        int i = this.mPreLayoutSpanIndexCache.get(pos, -1);
        if (i != -1) {
            return i;
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(pos);
        if (convertPreLayoutPositionToPostLayout == -1) {
            Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + pos);
            return 0;
        }
        return this.mSpanSizeLookup.getCachedSpanIndex(convertPreLayoutPositionToPostLayout, this.mSpanCount);
    }

    private int getSpanSize(RecyclerView.Recycler recycler, RecyclerView.State state, int pos) {
        if (!state.isPreLayout()) {
            return this.mSpanSizeLookup.getSpanSize(pos);
        }
        int i = this.mPreLayoutSpanSizeCache.get(pos, -1);
        if (i != -1) {
            return i;
        }
        int convertPreLayoutPositionToPostLayout = recycler.convertPreLayoutPositionToPostLayout(pos);
        if (convertPreLayoutPositionToPostLayout == -1) {
            Log.w("GridLayoutManager", "Cannot find span size for pre layout position. It is not cached, not in the adapter. Pos:" + pos);
            return 1;
        }
        return this.mSpanSizeLookup.getSpanSize(convertPreLayoutPositionToPostLayout);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager
    void collectPrefetchPositionsForLayoutState(RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int i = this.mSpanCount;
        for (int i2 = 0; i2 < this.mSpanCount && layoutState.hasMore(state) && i > 0; i2++) {
            int i3 = layoutState.mCurrentPosition;
            layoutPrefetchRegistry.addPosition(i3, Math.max(0, layoutState.mScrollingOffset));
            i -= this.mSpanSizeLookup.getSpanSize(i3);
            layoutState.mCurrentPosition += layoutState.mItemDirection;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x009f, code lost:
        r21.mFinished = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00a1, code lost:
        return;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0 */
    /* JADX WARN: Type inference failed for: r5v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r5v19 */
    @Override // androidx.recyclerview.widget.LinearLayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void layoutChunk(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.LayoutState layoutState, LinearLayoutManager.LayoutChunkResult result) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int decoratedMeasurementInOther;
        int childMeasureSpec;
        int i8;
        View next;
        int modeInOther = this.mOrientationHelper.getModeInOther();
        ?? r5 = 0;
        Object[] objArr = modeInOther != 1073741824 ? 1 : null;
        int i9 = getChildCount() > 0 ? this.mCachedBorders[this.mSpanCount] : 0;
        if (objArr != null) {
            updateMeasurements();
        }
        boolean z = layoutState.mItemDirection == 1;
        int i10 = this.mSpanCount;
        if (!z) {
            i10 = getSpanIndex(recycler, state, layoutState.mCurrentPosition) + getSpanSize(recycler, state, layoutState.mCurrentPosition);
        }
        int i11 = 0;
        while (i11 < this.mSpanCount && layoutState.hasMore(state) && i10 > 0) {
            int i12 = layoutState.mCurrentPosition;
            int spanSize = getSpanSize(recycler, state, i12);
            if (spanSize > this.mSpanCount) {
                throw new IllegalArgumentException("Item at position " + i12 + " requires " + spanSize + " spans but GridLayoutManager has only " + this.mSpanCount + " spans.");
            }
            i10 -= spanSize;
            if (i10 < 0 || (next = layoutState.next(recycler)) == null) {
                break;
            }
            this.mSet[i11] = next;
            i11++;
        }
        float f = 0.0f;
        assignSpans(recycler, state, i11, z);
        int i13 = 0;
        int i14 = 0;
        while (i13 < i11) {
            View view = this.mSet[i13];
            if (layoutState.mScrapList == null) {
                if (z) {
                    addView(view);
                } else {
                    addView(view, r5);
                }
            } else if (z) {
                addDisappearingView(view);
            } else {
                int i15 = r5 == true ? 1 : 0;
                int i16 = r5 == true ? 1 : 0;
                addDisappearingView(view, i15);
            }
            calculateItemDecorationsForChild(view, this.mDecorInsets);
            measureChild(view, modeInOther, r5);
            int decoratedMeasurement = this.mOrientationHelper.getDecoratedMeasurement(view);
            if (decoratedMeasurement > i14) {
                i14 = decoratedMeasurement;
            }
            float decoratedMeasurementInOther2 = (this.mOrientationHelper.getDecoratedMeasurementInOther(view) * 1.0f) / ((LayoutParams) view.getLayoutParams()).mSpanSize;
            if (decoratedMeasurementInOther2 > f) {
                f = decoratedMeasurementInOther2;
            }
            i13++;
            r5 = 0;
        }
        if (objArr != null) {
            guessMeasurement(f, i9);
            i14 = 0;
            for (int i17 = 0; i17 < i11; i17++) {
                View view2 = this.mSet[i17];
                measureChild(view2, 1073741824, true);
                int decoratedMeasurement2 = this.mOrientationHelper.getDecoratedMeasurement(view2);
                if (decoratedMeasurement2 > i14) {
                    i14 = decoratedMeasurement2;
                }
            }
        }
        for (int i18 = 0; i18 < i11; i18++) {
            View view3 = this.mSet[i18];
            if (this.mOrientationHelper.getDecoratedMeasurement(view3) != i14) {
                LayoutParams layoutParams = (LayoutParams) view3.getLayoutParams();
                Rect rect = layoutParams.mDecorInsets;
                int i19 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                int i20 = rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                int spaceForSpanRange = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
                if (this.mOrientation == 1) {
                    i8 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, i20, ((ViewGroup.MarginLayoutParams) layoutParams).width, false);
                    childMeasureSpec = View.MeasureSpec.makeMeasureSpec(i14 - i19, 1073741824);
                } else {
                    int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i14 - i20, 1073741824);
                    childMeasureSpec = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, 1073741824, i19, ((ViewGroup.MarginLayoutParams) layoutParams).height, false);
                    i8 = makeMeasureSpec;
                }
                measureChildWithDecorationsAndMargin(view3, i8, childMeasureSpec, true);
            }
        }
        int i21 = 0;
        result.mConsumed = i14;
        if (this.mOrientation == 1) {
            if (layoutState.mLayoutDirection == -1) {
                i3 = layoutState.mOffset;
                i4 = i3 - i14;
            } else {
                int i22 = layoutState.mOffset;
                i4 = i22;
                i3 = i14 + i22;
            }
            i = 0;
            i2 = 0;
        } else if (layoutState.mLayoutDirection == -1) {
            int i23 = layoutState.mOffset;
            i2 = i23 - i14;
            i4 = 0;
            i = i23;
            i3 = 0;
        } else {
            int i24 = layoutState.mOffset;
            i = i14 + i24;
            i2 = i24;
            i3 = 0;
            i4 = 0;
        }
        while (i21 < i11) {
            View view4 = this.mSet[i21];
            LayoutParams layoutParams2 = (LayoutParams) view4.getLayoutParams();
            if (this.mOrientation == 1) {
                if (isLayoutRTL()) {
                    int paddingLeft = getPaddingLeft() + this.mCachedBorders[this.mSpanCount - layoutParams2.mSpanIndex];
                    decoratedMeasurementInOther = i3;
                    i6 = paddingLeft;
                    i7 = paddingLeft - this.mOrientationHelper.getDecoratedMeasurementInOther(view4);
                } else {
                    int paddingLeft2 = getPaddingLeft() + this.mCachedBorders[layoutParams2.mSpanIndex];
                    decoratedMeasurementInOther = i3;
                    i7 = paddingLeft2;
                    i6 = this.mOrientationHelper.getDecoratedMeasurementInOther(view4) + paddingLeft2;
                }
                i5 = i4;
            } else {
                int paddingTop = getPaddingTop() + this.mCachedBorders[layoutParams2.mSpanIndex];
                i5 = paddingTop;
                i6 = i;
                i7 = i2;
                decoratedMeasurementInOther = this.mOrientationHelper.getDecoratedMeasurementInOther(view4) + paddingTop;
            }
            layoutDecoratedWithMargins(view4, i7, i5, i6, decoratedMeasurementInOther);
            if (layoutParams2.isItemRemoved() || layoutParams2.isItemChanged()) {
                result.mIgnoreConsumed = true;
            }
            result.mFocusable |= view4.hasFocusable();
            i21++;
            i3 = decoratedMeasurementInOther;
            i = i6;
            i2 = i7;
            i4 = i5;
        }
        Arrays.fill(this.mSet, (Object) null);
    }

    private void measureChild(View view, int otherDirParentSpecMode, boolean alreadyMeasured) {
        int i;
        int i2;
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        Rect rect = layoutParams.mDecorInsets;
        int i3 = rect.top + rect.bottom + ((ViewGroup.MarginLayoutParams) layoutParams).topMargin + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        int i4 = rect.left + rect.right + ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        int spaceForSpanRange = getSpaceForSpanRange(layoutParams.mSpanIndex, layoutParams.mSpanSize);
        if (this.mOrientation == 1) {
            i2 = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, otherDirParentSpecMode, i4, ((ViewGroup.MarginLayoutParams) layoutParams).width, false);
            i = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), i3, ((ViewGroup.MarginLayoutParams) layoutParams).height, true);
        } else {
            int childMeasureSpec = RecyclerView.LayoutManager.getChildMeasureSpec(spaceForSpanRange, otherDirParentSpecMode, i3, ((ViewGroup.MarginLayoutParams) layoutParams).height, false);
            int childMeasureSpec2 = RecyclerView.LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getWidthMode(), i4, ((ViewGroup.MarginLayoutParams) layoutParams).width, true);
            i = childMeasureSpec;
            i2 = childMeasureSpec2;
        }
        measureChildWithDecorationsAndMargin(view, i2, i, alreadyMeasured);
    }

    private void guessMeasurement(float maxSizeInOther, int currentOtherDirSize) {
        calculateItemBorders(Math.max(Math.round(maxSizeInOther * this.mSpanCount), currentOtherDirSize));
    }

    private void measureChildWithDecorationsAndMargin(View child, int widthSpec, int heightSpec, boolean alreadyMeasured) {
        boolean shouldMeasureChild;
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
        if (alreadyMeasured) {
            shouldMeasureChild = shouldReMeasureChild(child, widthSpec, heightSpec, layoutParams);
        } else {
            shouldMeasureChild = shouldMeasureChild(child, widthSpec, heightSpec, layoutParams);
        }
        if (shouldMeasureChild) {
            child.measure(widthSpec, heightSpec);
        }
    }

    private void assignSpans(RecyclerView.Recycler recycler, RecyclerView.State state, int count, boolean layingOutInPrimaryDirection) {
        int i;
        int i2;
        int i3 = 0;
        int i4 = -1;
        if (layingOutInPrimaryDirection) {
            i2 = 1;
            i4 = count;
            i = 0;
        } else {
            i = count - 1;
            i2 = -1;
        }
        while (i != i4) {
            View view = this.mSet[i];
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            int spanSize = getSpanSize(recycler, state, getPosition(view));
            layoutParams.mSpanSize = spanSize;
            layoutParams.mSpanIndex = i3;
            i3 += spanSize;
            i += i2;
        }
    }

    public int getSpanCount() {
        return this.mSpanCount;
    }

    public void setSpanCount(int spanCount) {
        if (spanCount == this.mSpanCount) {
            return;
        }
        this.mPendingSpanCountChange = true;
        if (spanCount < 1) {
            throw new IllegalArgumentException("Span count should be at least 1. Provided " + spanCount);
        }
        this.mSpanCount = spanCount;
        this.mSpanSizeLookup.invalidateSpanIndexCache();
        requestLayout();
    }

    /* loaded from: classes.dex */
    public static abstract class SpanSizeLookup {
        final SparseIntArray mSpanIndexCache = new SparseIntArray();
        final SparseIntArray mSpanGroupIndexCache = new SparseIntArray();
        private boolean mCacheSpanIndices = false;
        private boolean mCacheSpanGroupIndices = false;

        public abstract int getSpanSize(int position);

        public void setSpanIndexCacheEnabled(boolean cacheSpanIndices) {
            if (!cacheSpanIndices) {
                this.mSpanGroupIndexCache.clear();
            }
            this.mCacheSpanIndices = cacheSpanIndices;
        }

        public void invalidateSpanIndexCache() {
            this.mSpanIndexCache.clear();
        }

        public void invalidateSpanGroupIndexCache() {
            this.mSpanGroupIndexCache.clear();
        }

        int getCachedSpanIndex(int position, int spanCount) {
            if (!this.mCacheSpanIndices) {
                return getSpanIndex(position, spanCount);
            }
            int i = this.mSpanIndexCache.get(position, -1);
            if (i != -1) {
                return i;
            }
            int spanIndex = getSpanIndex(position, spanCount);
            this.mSpanIndexCache.put(position, spanIndex);
            return spanIndex;
        }

        int getCachedSpanGroupIndex(int position, int spanCount) {
            if (!this.mCacheSpanGroupIndices) {
                return getSpanGroupIndex(position, spanCount);
            }
            int i = this.mSpanGroupIndexCache.get(position, -1);
            if (i != -1) {
                return i;
            }
            int spanGroupIndex = getSpanGroupIndex(position, spanCount);
            this.mSpanGroupIndexCache.put(position, spanGroupIndex);
            return spanGroupIndex;
        }

        /* JADX WARN: Removed duplicated region for block: B:12:0x0024  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0033  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x002b -> B:10:0x0030). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:15:0x002d -> B:10:0x0030). Please submit an issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:16:0x002f -> B:10:0x0030). Please submit an issue!!! */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public int getSpanIndex(int position, int spanCount) {
            int i;
            int i2;
            int spanSize = getSpanSize(position);
            if (spanSize == spanCount) {
                return 0;
            }
            if (!this.mCacheSpanIndices || (i = findFirstKeyLessThan(this.mSpanIndexCache, position)) < 0) {
                i = 0;
                i2 = 0;
                if (i >= position) {
                    int spanSize2 = getSpanSize(i);
                    i2 += spanSize2;
                    if (i2 == spanCount) {
                        i2 = 0;
                    } else if (i2 > spanCount) {
                        i2 = spanSize2;
                    }
                    i++;
                    if (i >= position) {
                        if (spanSize + i2 > spanCount) {
                            return 0;
                        }
                        return i2;
                    }
                }
            } else {
                i2 = this.mSpanIndexCache.get(i) + getSpanSize(i);
                i++;
                if (i >= position) {
                }
            }
        }

        static int findFirstKeyLessThan(SparseIntArray cache, int position) {
            int size = cache.size() - 1;
            int i = 0;
            while (i <= size) {
                int i2 = (i + size) >>> 1;
                if (cache.keyAt(i2) < position) {
                    i = i2 + 1;
                } else {
                    size = i2 - 1;
                }
            }
            int i3 = i - 1;
            if (i3 < 0 || i3 >= cache.size()) {
                return -1;
            }
            return cache.keyAt(i3);
        }

        public int getSpanGroupIndex(int adapterPosition, int spanCount) {
            int i;
            int i2;
            int i3;
            int findFirstKeyLessThan;
            if (!this.mCacheSpanGroupIndices || (findFirstKeyLessThan = findFirstKeyLessThan(this.mSpanGroupIndexCache, adapterPosition)) == -1) {
                i = 0;
                i2 = 0;
                i3 = 0;
            } else {
                i = this.mSpanGroupIndexCache.get(findFirstKeyLessThan);
                i2 = findFirstKeyLessThan + 1;
                i3 = getCachedSpanIndex(findFirstKeyLessThan, spanCount) + getSpanSize(findFirstKeyLessThan);
                if (i3 == spanCount) {
                    i++;
                    i3 = 0;
                }
            }
            int spanSize = getSpanSize(adapterPosition);
            while (i2 < adapterPosition) {
                int spanSize2 = getSpanSize(i2);
                i3 += spanSize2;
                if (i3 == spanCount) {
                    i++;
                    i3 = 0;
                } else if (i3 > spanCount) {
                    i++;
                    i3 = spanSize2;
                }
                i2++;
            }
            return i3 + spanSize > spanCount ? i + 1 : i;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:68:0x00d6, code lost:
        if (r13 == (r2 > r15)) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x00f6, code lost:
        if (r13 == r11) goto L51;
     */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0107  */
    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int childCount;
        int i;
        int i2;
        View view;
        View view2;
        int i3;
        int i4;
        boolean z;
        int i5;
        int i6;
        RecyclerView.Recycler recycler2 = recycler;
        RecyclerView.State state2 = state;
        View findContainingItemView = findContainingItemView(focused);
        View view3 = null;
        if (findContainingItemView == null) {
            return null;
        }
        LayoutParams layoutParams = (LayoutParams) findContainingItemView.getLayoutParams();
        int i7 = layoutParams.mSpanIndex;
        int i8 = layoutParams.mSpanSize + i7;
        if (super.onFocusSearchFailed(focused, focusDirection, recycler, state) == null) {
            return null;
        }
        if ((convertFocusDirectionToLayoutDirection(focusDirection) == 1) != this.mShouldReverseLayout) {
            i2 = getChildCount() - 1;
            childCount = -1;
            i = -1;
        } else {
            childCount = getChildCount();
            i = 1;
            i2 = 0;
        }
        boolean z2 = this.mOrientation == 1 && isLayoutRTL();
        int spanGroupIndex = getSpanGroupIndex(recycler2, state2, i2);
        int i9 = -1;
        int i10 = -1;
        int i11 = 0;
        int i12 = 0;
        int i13 = i2;
        View view4 = null;
        while (i13 != childCount) {
            int spanGroupIndex2 = getSpanGroupIndex(recycler2, state2, i13);
            View childAt = getChildAt(i13);
            if (childAt == findContainingItemView) {
                break;
            }
            if (!childAt.hasFocusable() || spanGroupIndex2 == spanGroupIndex) {
                LayoutParams layoutParams2 = (LayoutParams) childAt.getLayoutParams();
                int i14 = layoutParams2.mSpanIndex;
                view = findContainingItemView;
                int i15 = layoutParams2.mSpanSize + i14;
                if (childAt.hasFocusable() && i14 == i7 && i15 == i8) {
                    return childAt;
                }
                if ((!childAt.hasFocusable() || view3 != null) && (childAt.hasFocusable() || view4 != null)) {
                    view2 = view4;
                    int min = Math.min(i15, i8) - Math.max(i14, i7);
                    if (childAt.hasFocusable()) {
                        if (min <= i11) {
                            if (min == i11) {
                            }
                        }
                    } else if (view3 == null) {
                        i3 = i11;
                        i4 = childCount;
                        z = true;
                        if (isViewPartiallyVisible(childAt, false, true)) {
                            i5 = i12;
                            if (min > i5) {
                                i6 = i10;
                                if (z) {
                                    if (childAt.hasFocusable()) {
                                        i9 = layoutParams2.mSpanIndex;
                                        i10 = i6;
                                        i12 = i5;
                                        view4 = view2;
                                        view3 = childAt;
                                        i11 = Math.min(i15, i8) - Math.max(i14, i7);
                                    } else {
                                        int i16 = layoutParams2.mSpanIndex;
                                        i12 = Math.min(i15, i8) - Math.max(i14, i7);
                                        i10 = i16;
                                        i11 = i3;
                                        view4 = childAt;
                                    }
                                    i13 += i;
                                    recycler2 = recycler;
                                    state2 = state;
                                    findContainingItemView = view;
                                    childCount = i4;
                                }
                            } else {
                                if (min == i5) {
                                    i6 = i10;
                                    if (i14 <= i6) {
                                        z = false;
                                    }
                                } else {
                                    i6 = i10;
                                }
                                z = false;
                                if (z) {
                                }
                            }
                        }
                        i6 = i10;
                        i5 = i12;
                        z = false;
                        if (z) {
                        }
                    }
                    i3 = i11;
                    i4 = childCount;
                    i6 = i10;
                    i5 = i12;
                    z = false;
                    if (z) {
                    }
                } else {
                    view2 = view4;
                }
                i3 = i11;
                i4 = childCount;
                i6 = i10;
                i5 = i12;
                z = true;
                if (z) {
                }
            } else if (view3 != null) {
                break;
            } else {
                view = findContainingItemView;
                view2 = view4;
                i3 = i11;
                i4 = childCount;
                i6 = i10;
                i5 = i12;
            }
            i10 = i6;
            i12 = i5;
            i11 = i3;
            view4 = view2;
            i13 += i;
            recycler2 = recycler;
            state2 = state;
            findContainingItemView = view;
            childCount = i4;
        }
        return view3 != null ? view3 : view4;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public boolean supportsPredictiveItemAnimations() {
        return this.mPendingSavedState == null && !this.mPendingSpanCountChange;
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollRange(RecyclerView.State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return computeScrollRangeWithSpanInfo(state);
        }
        return super.computeHorizontalScrollRange(state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollRange(RecyclerView.State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return computeScrollRangeWithSpanInfo(state);
        }
        return super.computeVerticalScrollRange(state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return computeScrollOffsetWithSpanInfo(state);
        }
        return super.computeHorizontalScrollOffset(state);
    }

    @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public int computeVerticalScrollOffset(RecyclerView.State state) {
        if (this.mUsingSpansToEstimateScrollBarDimensions) {
            return computeScrollOffsetWithSpanInfo(state);
        }
        return super.computeVerticalScrollOffset(state);
    }

    private int computeScrollRangeWithSpanInfo(RecyclerView.State state) {
        if (getChildCount() != 0 && state.getItemCount() != 0) {
            ensureLayoutState();
            View findFirstVisibleChildClosestToStart = findFirstVisibleChildClosestToStart(!isSmoothScrollbarEnabled(), true);
            View findFirstVisibleChildClosestToEnd = findFirstVisibleChildClosestToEnd(!isSmoothScrollbarEnabled(), true);
            if (findFirstVisibleChildClosestToStart != null && findFirstVisibleChildClosestToEnd != null) {
                if (!isSmoothScrollbarEnabled()) {
                    return this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1;
                }
                int decoratedEnd = this.mOrientationHelper.getDecoratedEnd(findFirstVisibleChildClosestToEnd) - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart);
                int cachedSpanGroupIndex = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount);
                return (int) ((decoratedEnd / ((this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount) - cachedSpanGroupIndex) + 1)) * (this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1));
            }
        }
        return 0;
    }

    private int computeScrollOffsetWithSpanInfo(RecyclerView.State state) {
        int max;
        if (getChildCount() != 0 && state.getItemCount() != 0) {
            ensureLayoutState();
            boolean isSmoothScrollbarEnabled = isSmoothScrollbarEnabled();
            View findFirstVisibleChildClosestToStart = findFirstVisibleChildClosestToStart(!isSmoothScrollbarEnabled, true);
            View findFirstVisibleChildClosestToEnd = findFirstVisibleChildClosestToEnd(!isSmoothScrollbarEnabled, true);
            if (findFirstVisibleChildClosestToStart != null && findFirstVisibleChildClosestToEnd != null) {
                int cachedSpanGroupIndex = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount);
                int cachedSpanGroupIndex2 = this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount);
                int min = Math.min(cachedSpanGroupIndex, cachedSpanGroupIndex2);
                int max2 = Math.max(cachedSpanGroupIndex, cachedSpanGroupIndex2);
                int cachedSpanGroupIndex3 = this.mSpanSizeLookup.getCachedSpanGroupIndex(state.getItemCount() - 1, this.mSpanCount) + 1;
                if (this.mShouldReverseLayout) {
                    max = Math.max(0, (cachedSpanGroupIndex3 - max2) - 1);
                } else {
                    max = Math.max(0, min);
                }
                if (!isSmoothScrollbarEnabled) {
                    return max;
                }
                return Math.round((max * (Math.abs(this.mOrientationHelper.getDecoratedEnd(findFirstVisibleChildClosestToEnd) - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart)) / ((this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToEnd), this.mSpanCount) - this.mSpanSizeLookup.getCachedSpanGroupIndex(getPosition(findFirstVisibleChildClosestToStart), this.mSpanCount)) + 1))) + (this.mOrientationHelper.getStartAfterPadding() - this.mOrientationHelper.getDecoratedStart(findFirstVisibleChildClosestToStart)));
            }
        }
        return 0;
    }

    /* loaded from: classes.dex */
    public static final class DefaultSpanSizeLookup extends SpanSizeLookup {
        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanSize(int position) {
            return 1;
        }

        @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
        public int getSpanIndex(int position, int spanCount) {
            return position % spanCount;
        }
    }

    /* loaded from: classes.dex */
    public static class LayoutParams extends RecyclerView.LayoutParams {
        int mSpanIndex = -1;
        int mSpanSize = 0;

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

        public int getSpanIndex() {
            return this.mSpanIndex;
        }

        public int getSpanSize() {
            return this.mSpanSize;
        }
    }
}
