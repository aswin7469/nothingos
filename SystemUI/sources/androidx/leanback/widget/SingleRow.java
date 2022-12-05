package androidx.leanback.widget;

import androidx.collection.CircularIntArray;
import androidx.leanback.widget.Grid;
import androidx.recyclerview.widget.RecyclerView;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SingleRow extends Grid {
    private final Grid.Location mTmpLocation = new Grid.Location(0);

    /* JADX INFO: Access modifiers changed from: package-private */
    public SingleRow() {
        setNumRows(1);
    }

    @Override // androidx.leanback.widget.Grid
    /* renamed from: getLocation */
    public final Grid.Location mo120getLocation(int index) {
        return this.mTmpLocation;
    }

    int getStartIndexForAppend() {
        int i = this.mLastVisibleIndex;
        if (i >= 0) {
            return i + 1;
        }
        int i2 = this.mStartIndex;
        if (i2 == -1) {
            return 0;
        }
        return Math.min(i2, this.mProvider.getCount() - 1);
    }

    int getStartIndexForPrepend() {
        int i = this.mFirstVisibleIndex;
        if (i >= 0) {
            return i - 1;
        }
        int i2 = this.mStartIndex;
        if (i2 != -1) {
            return Math.min(i2, this.mProvider.getCount() - 1);
        }
        return this.mProvider.getCount() - 1;
    }

    @Override // androidx.leanback.widget.Grid
    protected final boolean prependVisibleItems(int toLimit, boolean oneColumnMode) {
        int i;
        if (this.mProvider.getCount() == 0) {
            return false;
        }
        if (!oneColumnMode && checkPrependOverLimit(toLimit)) {
            return false;
        }
        int minIndex = this.mProvider.getMinIndex();
        int startIndexForPrepend = getStartIndexForPrepend();
        boolean z = false;
        while (startIndexForPrepend >= minIndex) {
            int createItem = this.mProvider.createItem(startIndexForPrepend, false, this.mTmpItem, false);
            if (this.mFirstVisibleIndex < 0 || this.mLastVisibleIndex < 0) {
                i = this.mReversedFlow ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                this.mFirstVisibleIndex = startIndexForPrepend;
                this.mLastVisibleIndex = startIndexForPrepend;
            } else {
                if (this.mReversedFlow) {
                    i = this.mProvider.getEdge(startIndexForPrepend + 1) + this.mSpacing + createItem;
                } else {
                    i = (this.mProvider.getEdge(startIndexForPrepend + 1) - this.mSpacing) - createItem;
                }
                this.mFirstVisibleIndex = startIndexForPrepend;
            }
            this.mProvider.addItem(this.mTmpItem[0], startIndexForPrepend, createItem, 0, i);
            if (oneColumnMode || checkPrependOverLimit(toLimit)) {
                return true;
            }
            startIndexForPrepend--;
            z = true;
        }
        return z;
    }

    @Override // androidx.leanback.widget.Grid
    protected final boolean appendVisibleItems(int toLimit, boolean oneColumnMode) {
        int i;
        if (this.mProvider.getCount() == 0) {
            return false;
        }
        if (!oneColumnMode && checkAppendOverLimit(toLimit)) {
            return false;
        }
        int startIndexForAppend = getStartIndexForAppend();
        boolean z = false;
        while (startIndexForAppend < this.mProvider.getCount()) {
            int createItem = this.mProvider.createItem(startIndexForAppend, true, this.mTmpItem, false);
            if (this.mFirstVisibleIndex < 0 || this.mLastVisibleIndex < 0) {
                i = this.mReversedFlow ? Integer.MAX_VALUE : Integer.MIN_VALUE;
                this.mFirstVisibleIndex = startIndexForAppend;
                this.mLastVisibleIndex = startIndexForAppend;
            } else {
                if (this.mReversedFlow) {
                    int i2 = startIndexForAppend - 1;
                    i = (this.mProvider.getEdge(i2) - this.mProvider.getSize(i2)) - this.mSpacing;
                } else {
                    int i3 = startIndexForAppend - 1;
                    i = this.mProvider.getEdge(i3) + this.mProvider.getSize(i3) + this.mSpacing;
                }
                this.mLastVisibleIndex = startIndexForAppend;
            }
            this.mProvider.addItem(this.mTmpItem[0], startIndexForAppend, createItem, 0, i);
            if (oneColumnMode || checkAppendOverLimit(toLimit)) {
                return true;
            }
            startIndexForAppend++;
            z = true;
        }
        return z;
    }

    @Override // androidx.leanback.widget.Grid
    public void collectAdjacentPrefetchPositions(int fromLimit, int da, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
        int startIndexForPrepend;
        int i;
        if (!this.mReversedFlow ? da < 0 : da > 0) {
            if (getFirstVisibleIndex() == 0) {
                return;
            }
            startIndexForPrepend = getStartIndexForPrepend();
            int edge = this.mProvider.getEdge(this.mFirstVisibleIndex);
            boolean z = this.mReversedFlow;
            int i2 = this.mSpacing;
            if (!z) {
                i2 = -i2;
            }
            i = edge + i2;
        } else if (getLastVisibleIndex() == this.mProvider.getCount() - 1) {
            return;
        } else {
            startIndexForPrepend = getStartIndexForAppend();
            int size = this.mProvider.getSize(this.mLastVisibleIndex) + this.mSpacing;
            int edge2 = this.mProvider.getEdge(this.mLastVisibleIndex);
            if (this.mReversedFlow) {
                size = -size;
            }
            i = size + edge2;
        }
        layoutPrefetchRegistry.addPosition(startIndexForPrepend, Math.abs(i - fromLimit));
    }

    @Override // androidx.leanback.widget.Grid
    public final CircularIntArray[] getItemPositionsInRows(int startPos, int endPos) {
        this.mTmpItemPositionsInRows[0].clear();
        this.mTmpItemPositionsInRows[0].addLast(startPos);
        this.mTmpItemPositionsInRows[0].addLast(endPos);
        return this.mTmpItemPositionsInRows;
    }

    @Override // androidx.leanback.widget.Grid
    protected final int findRowMin(boolean findLarge, int indexLimit, int[] indices) {
        if (indices != null) {
            indices[0] = 0;
            indices[1] = indexLimit;
        }
        return this.mReversedFlow ? this.mProvider.getEdge(indexLimit) - this.mProvider.getSize(indexLimit) : this.mProvider.getEdge(indexLimit);
    }

    @Override // androidx.leanback.widget.Grid
    protected final int findRowMax(boolean findLarge, int indexLimit, int[] indices) {
        if (indices != null) {
            indices[0] = 0;
            indices[1] = indexLimit;
        }
        if (this.mReversedFlow) {
            return this.mProvider.getEdge(indexLimit);
        }
        return this.mProvider.getSize(indexLimit) + this.mProvider.getEdge(indexLimit);
    }
}
