package androidx.leanback.widget;

import androidx.collection.CircularArray;
import androidx.collection.CircularIntArray;
import androidx.leanback.widget.Grid;
/* loaded from: classes.dex */
abstract class StaggeredGrid extends Grid {
    protected Object mPendingItem;
    protected int mPendingItemSize;
    protected CircularArray<Location> mLocations = new CircularArray<>(64);
    protected int mFirstIndex = -1;

    protected abstract boolean appendVisibleItemsWithoutCache(int toLimit, boolean oneColumnMode);

    protected abstract boolean prependVisibleItemsWithoutCache(int toLimit, boolean oneColumnMode);

    /* loaded from: classes.dex */
    public static class Location extends Grid.Location {
        public int offset;
        public int size;

        public Location(int row, int offset, int size) {
            super(row);
            this.offset = offset;
            this.size = size;
        }
    }

    public final int getFirstIndex() {
        return this.mFirstIndex;
    }

    public final int getLastIndex() {
        return (this.mFirstIndex + this.mLocations.size()) - 1;
    }

    @Override // androidx.leanback.widget.Grid
    /* renamed from: getLocation  reason: collision with other method in class */
    public final Location mo120getLocation(int index) {
        int i = index - this.mFirstIndex;
        if (i < 0 || i >= this.mLocations.size()) {
            return null;
        }
        return this.mLocations.get(i);
    }

    @Override // androidx.leanback.widget.Grid
    protected final boolean prependVisibleItems(int toLimit, boolean oneColumnMode) {
        boolean prependVisibleItemsWithoutCache;
        if (this.mProvider.getCount() == 0) {
            return false;
        }
        if (!oneColumnMode && checkPrependOverLimit(toLimit)) {
            return false;
        }
        try {
            if (!prependVisbleItemsWithCache(toLimit, oneColumnMode)) {
                prependVisibleItemsWithoutCache = prependVisibleItemsWithoutCache(toLimit, oneColumnMode);
                this.mTmpItem[0] = null;
            } else {
                prependVisibleItemsWithoutCache = true;
                this.mTmpItem[0] = null;
            }
            this.mPendingItem = null;
            return prependVisibleItemsWithoutCache;
        } catch (Throwable th) {
            this.mTmpItem[0] = null;
            this.mPendingItem = null;
            throw th;
        }
    }

    protected final boolean prependVisbleItemsWithCache(int toLimit, boolean oneColumnMode) {
        int i;
        int i2;
        int i3;
        if (this.mLocations.size() == 0) {
            return false;
        }
        int i4 = this.mFirstVisibleIndex;
        if (i4 >= 0) {
            i = this.mProvider.getEdge(i4);
            i3 = mo120getLocation(this.mFirstVisibleIndex).offset;
            i2 = this.mFirstVisibleIndex - 1;
        } else {
            i = Integer.MAX_VALUE;
            int i5 = this.mStartIndex;
            i2 = i5 != -1 ? i5 : 0;
            if (i2 > getLastIndex() || i2 < getFirstIndex() - 1) {
                this.mLocations.clear();
                return false;
            } else if (i2 < getFirstIndex()) {
                return false;
            } else {
                i3 = 0;
            }
        }
        int max = Math.max(this.mProvider.getMinIndex(), this.mFirstIndex);
        while (i2 >= max) {
            Location mo120getLocation = mo120getLocation(i2);
            int i6 = mo120getLocation.row;
            int createItem = this.mProvider.createItem(i2, false, this.mTmpItem, false);
            if (createItem != mo120getLocation.size) {
                this.mLocations.removeFromStart((i2 + 1) - this.mFirstIndex);
                this.mFirstIndex = this.mFirstVisibleIndex;
                this.mPendingItem = this.mTmpItem[0];
                this.mPendingItemSize = createItem;
                return false;
            }
            this.mFirstVisibleIndex = i2;
            if (this.mLastVisibleIndex < 0) {
                this.mLastVisibleIndex = i2;
            }
            this.mProvider.addItem(this.mTmpItem[0], i2, createItem, i6, i - i3);
            if (!oneColumnMode && checkPrependOverLimit(toLimit)) {
                return true;
            }
            i = this.mProvider.getEdge(i2);
            i3 = mo120getLocation.offset;
            if (i6 == 0 && oneColumnMode) {
                return true;
            }
            i2--;
        }
        return false;
    }

    private int calculateOffsetAfterLastItem(int row) {
        boolean z;
        int lastIndex = getLastIndex();
        while (true) {
            if (lastIndex < this.mFirstIndex) {
                z = false;
                break;
            } else if (mo120getLocation(lastIndex).row == row) {
                z = true;
                break;
            } else {
                lastIndex--;
            }
        }
        if (!z) {
            lastIndex = getLastIndex();
        }
        int i = isReversedFlow() ? (-mo120getLocation(lastIndex).size) - this.mSpacing : mo120getLocation(lastIndex).size + this.mSpacing;
        for (int i2 = lastIndex + 1; i2 <= getLastIndex(); i2++) {
            i -= mo120getLocation(i2).offset;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int prependVisibleItemToRow(int itemIndex, int rowIndex, int edge) {
        int i = this.mFirstVisibleIndex;
        if (i >= 0 && (i != getFirstIndex() || this.mFirstVisibleIndex != itemIndex + 1)) {
            throw new IllegalStateException();
        }
        int i2 = this.mFirstIndex;
        Location mo120getLocation = i2 >= 0 ? mo120getLocation(i2) : null;
        int edge2 = this.mProvider.getEdge(this.mFirstIndex);
        Location location = new Location(rowIndex, 0, 0);
        this.mLocations.addFirst(location);
        Object obj = this.mPendingItem;
        if (obj != null) {
            location.size = this.mPendingItemSize;
            this.mPendingItem = null;
        } else {
            location.size = this.mProvider.createItem(itemIndex, false, this.mTmpItem, false);
            obj = this.mTmpItem[0];
        }
        Object obj2 = obj;
        this.mFirstVisibleIndex = itemIndex;
        this.mFirstIndex = itemIndex;
        if (this.mLastVisibleIndex < 0) {
            this.mLastVisibleIndex = itemIndex;
        }
        int i3 = !this.mReversedFlow ? edge - location.size : edge + location.size;
        if (mo120getLocation != null) {
            mo120getLocation.offset = edge2 - i3;
        }
        this.mProvider.addItem(obj2, itemIndex, location.size, rowIndex, i3);
        return location.size;
    }

    @Override // androidx.leanback.widget.Grid
    protected final boolean appendVisibleItems(int toLimit, boolean oneColumnMode) {
        boolean appendVisibleItemsWithoutCache;
        if (this.mProvider.getCount() == 0) {
            return false;
        }
        if (!oneColumnMode && checkAppendOverLimit(toLimit)) {
            return false;
        }
        try {
            if (!appendVisbleItemsWithCache(toLimit, oneColumnMode)) {
                appendVisibleItemsWithoutCache = appendVisibleItemsWithoutCache(toLimit, oneColumnMode);
                this.mTmpItem[0] = null;
            } else {
                appendVisibleItemsWithoutCache = true;
                this.mTmpItem[0] = null;
            }
            this.mPendingItem = null;
            return appendVisibleItemsWithoutCache;
        } catch (Throwable th) {
            this.mTmpItem[0] = null;
            this.mPendingItem = null;
            throw th;
        }
    }

    protected final boolean appendVisbleItemsWithCache(int toLimit, boolean oneColumnMode) {
        int i;
        int i2;
        int i3;
        if (this.mLocations.size() == 0) {
            return false;
        }
        int count = this.mProvider.getCount();
        int i4 = this.mLastVisibleIndex;
        if (i4 >= 0) {
            i = i4 + 1;
            i2 = this.mProvider.getEdge(i4);
        } else {
            int i5 = this.mStartIndex;
            i = i5 != -1 ? i5 : 0;
            if (i > getLastIndex() + 1 || i < getFirstIndex()) {
                this.mLocations.clear();
                return false;
            } else if (i > getLastIndex()) {
                return false;
            } else {
                i2 = Integer.MAX_VALUE;
            }
        }
        int lastIndex = getLastIndex();
        int i6 = i;
        while (i6 < count && i6 <= lastIndex) {
            Location mo120getLocation = mo120getLocation(i6);
            if (i2 != Integer.MAX_VALUE) {
                i2 += mo120getLocation.offset;
            }
            int i7 = mo120getLocation.row;
            int createItem = this.mProvider.createItem(i6, true, this.mTmpItem, false);
            if (createItem != mo120getLocation.size) {
                mo120getLocation.size = createItem;
                this.mLocations.removeFromEnd(lastIndex - i6);
                i3 = i6;
            } else {
                i3 = lastIndex;
            }
            this.mLastVisibleIndex = i6;
            if (this.mFirstVisibleIndex < 0) {
                this.mFirstVisibleIndex = i6;
            }
            this.mProvider.addItem(this.mTmpItem[0], i6, createItem, i7, i2);
            if (!oneColumnMode && checkAppendOverLimit(toLimit)) {
                return true;
            }
            if (i2 == Integer.MAX_VALUE) {
                i2 = this.mProvider.getEdge(i6);
            }
            if (i7 == this.mNumRows - 1 && oneColumnMode) {
                return true;
            }
            i6++;
            lastIndex = i3;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final int appendVisibleItemToRow(int itemIndex, int rowIndex, int location) {
        int edge;
        int i = this.mLastVisibleIndex;
        if (i >= 0 && (i != getLastIndex() || this.mLastVisibleIndex != itemIndex - 1)) {
            throw new IllegalStateException();
        }
        int i2 = this.mLastVisibleIndex;
        if (i2 < 0) {
            edge = (this.mLocations.size() <= 0 || itemIndex != getLastIndex() + 1) ? 0 : calculateOffsetAfterLastItem(rowIndex);
        } else {
            edge = location - this.mProvider.getEdge(i2);
        }
        Location location2 = new Location(rowIndex, edge, 0);
        this.mLocations.addLast(location2);
        Object obj = this.mPendingItem;
        if (obj != null) {
            location2.size = this.mPendingItemSize;
            this.mPendingItem = null;
        } else {
            location2.size = this.mProvider.createItem(itemIndex, true, this.mTmpItem, false);
            obj = this.mTmpItem[0];
        }
        Object obj2 = obj;
        if (this.mLocations.size() == 1) {
            this.mLastVisibleIndex = itemIndex;
            this.mFirstVisibleIndex = itemIndex;
            this.mFirstIndex = itemIndex;
        } else {
            int i3 = this.mLastVisibleIndex;
            if (i3 < 0) {
                this.mLastVisibleIndex = itemIndex;
                this.mFirstVisibleIndex = itemIndex;
            } else {
                this.mLastVisibleIndex = i3 + 1;
            }
        }
        this.mProvider.addItem(obj2, itemIndex, location2.size, rowIndex, location);
        return location2.size;
    }

    @Override // androidx.leanback.widget.Grid
    public final CircularIntArray[] getItemPositionsInRows(int startPos, int endPos) {
        for (int i = 0; i < this.mNumRows; i++) {
            this.mTmpItemPositionsInRows[i].clear();
        }
        if (startPos >= 0) {
            while (startPos <= endPos) {
                CircularIntArray circularIntArray = this.mTmpItemPositionsInRows[mo120getLocation(startPos).row];
                if (circularIntArray.size() > 0 && circularIntArray.getLast() == startPos - 1) {
                    circularIntArray.popLast();
                    circularIntArray.addLast(startPos);
                } else {
                    circularIntArray.addLast(startPos);
                    circularIntArray.addLast(startPos);
                }
                startPos++;
            }
        }
        return this.mTmpItemPositionsInRows;
    }

    @Override // androidx.leanback.widget.Grid
    public void invalidateItemsAfter(int index) {
        super.invalidateItemsAfter(index);
        this.mLocations.removeFromEnd((getLastIndex() - index) + 1);
        if (this.mLocations.size() == 0) {
            this.mFirstIndex = -1;
        }
    }
}
