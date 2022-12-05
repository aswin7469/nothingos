package androidx.leanback.widget;

import android.util.SparseIntArray;
import androidx.collection.CircularIntArray;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class Grid {
    protected int mNumRows;
    protected Provider mProvider;
    protected boolean mReversedFlow;
    protected int mSpacing;
    protected CircularIntArray[] mTmpItemPositionsInRows;
    Object[] mTmpItem = new Object[1];
    protected int mFirstVisibleIndex = -1;
    protected int mLastVisibleIndex = -1;
    protected int mStartIndex = -1;

    /* loaded from: classes.dex */
    public interface Provider {
        void addItem(Object item, int index, int length, int rowIndex, int edge);

        int createItem(int index, boolean append, Object[] item, boolean disappearingItem);

        int getCount();

        int getEdge(int index);

        int getMinIndex();

        int getSize(int index);

        void removeItem(int index);
    }

    protected abstract boolean appendVisibleItems(int toLimit, boolean oneColumnMode);

    public void collectAdjacentPrefetchPositions(int fromLimit, int da, RecyclerView.LayoutManager.LayoutPrefetchRegistry layoutPrefetchRegistry) {
    }

    protected abstract int findRowMax(boolean findLarge, int indexLimit, int[] indices);

    protected abstract int findRowMin(boolean findLarge, int indexLimit, int[] rowIndex);

    public abstract CircularIntArray[] getItemPositionsInRows(int startPos, int endPos);

    /* renamed from: getLocation */
    public abstract Location mo120getLocation(int index);

    protected abstract boolean prependVisibleItems(int toLimit, boolean oneColumnMode);

    /* loaded from: classes.dex */
    public static class Location {
        public int row;

        public Location(int row) {
            this.row = row;
        }
    }

    public static Grid createGrid(int rows) {
        if (rows == 1) {
            return new SingleRow();
        }
        StaggeredGridDefault staggeredGridDefault = new StaggeredGridDefault();
        staggeredGridDefault.setNumRows(rows);
        return staggeredGridDefault;
    }

    public final void setSpacing(int spacing) {
        this.mSpacing = spacing;
    }

    public final void setReversedFlow(boolean reversedFlow) {
        this.mReversedFlow = reversedFlow;
    }

    public boolean isReversedFlow() {
        return this.mReversedFlow;
    }

    public void setProvider(Provider provider) {
        this.mProvider = provider;
    }

    public void setStart(int startIndex) {
        this.mStartIndex = startIndex;
    }

    public int getNumRows() {
        return this.mNumRows;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setNumRows(int numRows) {
        if (numRows <= 0) {
            throw new IllegalArgumentException();
        }
        if (this.mNumRows == numRows) {
            return;
        }
        this.mNumRows = numRows;
        this.mTmpItemPositionsInRows = new CircularIntArray[numRows];
        for (int i = 0; i < this.mNumRows; i++) {
            this.mTmpItemPositionsInRows[i] = new CircularIntArray();
        }
    }

    public final int getFirstVisibleIndex() {
        return this.mFirstVisibleIndex;
    }

    public final int getLastVisibleIndex() {
        return this.mLastVisibleIndex;
    }

    public void resetVisibleIndex() {
        this.mLastVisibleIndex = -1;
        this.mFirstVisibleIndex = -1;
    }

    public void invalidateItemsAfter(int index) {
        int i;
        if (index >= 0 && (i = this.mLastVisibleIndex) >= 0) {
            if (i >= index) {
                this.mLastVisibleIndex = index - 1;
            }
            resetVisibleIndexIfEmpty();
            if (getFirstVisibleIndex() >= 0) {
                return;
            }
            setStart(index);
        }
    }

    public final int getRowIndex(int index) {
        Location mo120getLocation = mo120getLocation(index);
        if (mo120getLocation == null) {
            return -1;
        }
        return mo120getLocation.row;
    }

    public final int findRowMin(boolean findLarge, int[] indices) {
        return findRowMin(findLarge, this.mReversedFlow ? this.mLastVisibleIndex : this.mFirstVisibleIndex, indices);
    }

    public final int findRowMax(boolean findLarge, int[] indices) {
        return findRowMax(findLarge, this.mReversedFlow ? this.mFirstVisibleIndex : this.mLastVisibleIndex, indices);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean checkAppendOverLimit(int toLimit) {
        if (this.mLastVisibleIndex < 0) {
            return false;
        }
        if (this.mReversedFlow) {
            if (findRowMin(true, null) > toLimit + this.mSpacing) {
                return false;
            }
        } else if (findRowMax(false, null) < toLimit - this.mSpacing) {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean checkPrependOverLimit(int toLimit) {
        if (this.mLastVisibleIndex < 0) {
            return false;
        }
        if (this.mReversedFlow) {
            if (findRowMax(false, null) < toLimit - this.mSpacing) {
                return false;
            }
        } else if (findRowMin(true, null) > toLimit + this.mSpacing) {
            return false;
        }
        return true;
    }

    public final CircularIntArray[] getItemPositionsInRows() {
        return getItemPositionsInRows(getFirstVisibleIndex(), getLastVisibleIndex());
    }

    public final boolean prependOneColumnVisibleItems() {
        return prependVisibleItems(this.mReversedFlow ? Integer.MIN_VALUE : Integer.MAX_VALUE, true);
    }

    public final void prependVisibleItems(int toLimit) {
        prependVisibleItems(toLimit, false);
    }

    public boolean appendOneColumnVisibleItems() {
        return appendVisibleItems(this.mReversedFlow ? Integer.MAX_VALUE : Integer.MIN_VALUE, true);
    }

    public final void appendVisibleItems(int toLimit) {
        appendVisibleItems(toLimit, false);
    }

    public void removeInvisibleItemsAtEnd(int aboveIndex, int toLimit) {
        while (true) {
            int i = this.mLastVisibleIndex;
            if (i < this.mFirstVisibleIndex || i <= aboveIndex) {
                break;
            }
            boolean z = false;
            if (this.mReversedFlow ? this.mProvider.getEdge(i) <= toLimit : this.mProvider.getEdge(i) >= toLimit) {
                z = true;
            }
            if (!z) {
                break;
            }
            this.mProvider.removeItem(this.mLastVisibleIndex);
            this.mLastVisibleIndex--;
        }
        resetVisibleIndexIfEmpty();
    }

    public void removeInvisibleItemsAtFront(int belowIndex, int toLimit) {
        while (true) {
            int i = this.mLastVisibleIndex;
            int i2 = this.mFirstVisibleIndex;
            if (i < i2 || i2 >= belowIndex) {
                break;
            }
            int size = this.mProvider.getSize(i2);
            boolean z = false;
            if (this.mReversedFlow ? this.mProvider.getEdge(this.mFirstVisibleIndex) - size >= toLimit : this.mProvider.getEdge(this.mFirstVisibleIndex) + size <= toLimit) {
                z = true;
            }
            if (!z) {
                break;
            }
            this.mProvider.removeItem(this.mFirstVisibleIndex);
            this.mFirstVisibleIndex++;
        }
        resetVisibleIndexIfEmpty();
    }

    private void resetVisibleIndexIfEmpty() {
        if (this.mLastVisibleIndex < this.mFirstVisibleIndex) {
            resetVisibleIndex();
        }
    }

    public void fillDisappearingItems(int[] positions, int positionsLength, SparseIntArray positionToRow) {
        int edge;
        int edge2;
        int lastVisibleIndex = getLastVisibleIndex();
        int binarySearch = lastVisibleIndex >= 0 ? Arrays.binarySearch(positions, 0, positionsLength, lastVisibleIndex) : 0;
        if (binarySearch < 0) {
            if (this.mReversedFlow) {
                edge2 = (this.mProvider.getEdge(lastVisibleIndex) - this.mProvider.getSize(lastVisibleIndex)) - this.mSpacing;
            } else {
                edge2 = this.mProvider.getEdge(lastVisibleIndex) + this.mProvider.getSize(lastVisibleIndex) + this.mSpacing;
            }
            int i = edge2;
            for (int i2 = (-binarySearch) - 1; i2 < positionsLength; i2++) {
                int i3 = positions[i2];
                int i4 = positionToRow.get(i3);
                int i5 = i4 < 0 ? 0 : i4;
                int createItem = this.mProvider.createItem(i3, true, this.mTmpItem, true);
                this.mProvider.addItem(this.mTmpItem[0], i3, createItem, i5, i);
                if (this.mReversedFlow) {
                    i = (i - createItem) - this.mSpacing;
                } else {
                    i = i + createItem + this.mSpacing;
                }
            }
        }
        int firstVisibleIndex = getFirstVisibleIndex();
        int binarySearch2 = firstVisibleIndex >= 0 ? Arrays.binarySearch(positions, 0, positionsLength, firstVisibleIndex) : 0;
        if (binarySearch2 < 0) {
            if (this.mReversedFlow) {
                edge = this.mProvider.getEdge(firstVisibleIndex);
            } else {
                edge = this.mProvider.getEdge(firstVisibleIndex);
            }
            for (int i6 = (-binarySearch2) - 2; i6 >= 0; i6--) {
                int i7 = positions[i6];
                int i8 = positionToRow.get(i7);
                int i9 = i8 < 0 ? 0 : i8;
                int createItem2 = this.mProvider.createItem(i7, false, this.mTmpItem, true);
                if (this.mReversedFlow) {
                    edge = edge + this.mSpacing + createItem2;
                } else {
                    edge = (edge - this.mSpacing) - createItem2;
                }
                this.mProvider.addItem(this.mTmpItem[0], i7, createItem2, i9, edge);
            }
        }
    }
}
