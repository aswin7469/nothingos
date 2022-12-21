package androidx.leanback.widget;

import android.net.wifi.WifiEnterpriseConfig;
import androidx.collection.CircularArray;
import androidx.collection.CircularIntArray;
import androidx.leanback.widget.Grid;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.PrintWriter;

abstract class StaggeredGrid extends Grid {
    protected int mFirstIndex = -1;
    protected CircularArray<Location> mLocations = new CircularArray<>(64);
    protected Object mPendingItem;
    protected int mPendingItemSize;

    /* access modifiers changed from: protected */
    public abstract boolean appendVisibleItemsWithoutCache(int i, boolean z);

    /* access modifiers changed from: protected */
    public abstract boolean prependVisibleItemsWithoutCache(int i, boolean z);

    StaggeredGrid() {
    }

    public static class Location extends Grid.Location {
        public int offset;
        public int size;

        public Location(int i, int i2, int i3) {
            super(i);
            this.offset = i2;
            this.size = i3;
        }
    }

    public final int getFirstIndex() {
        return this.mFirstIndex;
    }

    public final int getLastIndex() {
        return (this.mFirstIndex + this.mLocations.size()) - 1;
    }

    public final int getSize() {
        return this.mLocations.size();
    }

    public final Location getLocation(int i) {
        int i2 = i - this.mFirstIndex;
        if (i2 < 0 || i2 >= this.mLocations.size()) {
            return null;
        }
        return this.mLocations.get(i2);
    }

    public final void debugPrint(PrintWriter printWriter) {
        int size = this.mLocations.size();
        for (int i = 0; i < size; i++) {
            printWriter.print("<" + (this.mFirstIndex + i) + NavigationBarInflaterView.BUTTON_SEPARATOR + this.mLocations.get(i).row + ">");
            printWriter.print(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            printWriter.println();
        }
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public final boolean prependVisibleItems(int i, boolean z) {
        if (this.mProvider.getCount() == 0) {
            return false;
        }
        if (!z && checkPrependOverLimit(i)) {
            return false;
        }
        try {
            if (prependVisbleItemsWithCache(i, z)) {
                this.mTmpItem[0] = null;
                this.mPendingItem = null;
                return true;
            }
            boolean prependVisibleItemsWithoutCache = prependVisibleItemsWithoutCache(i, z);
            this.mTmpItem[0] = null;
            this.mPendingItem = null;
            return prependVisibleItemsWithoutCache;
        } catch (Throwable th) {
            this.mTmpItem[0] = null;
            this.mPendingItem = null;
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public final boolean prependVisbleItemsWithCache(int i, boolean z) {
        int i2;
        int i3;
        int i4;
        if (this.mLocations.size() == 0) {
            return false;
        }
        if (this.mFirstVisibleIndex >= 0) {
            i4 = this.mProvider.getEdge(this.mFirstVisibleIndex);
            i3 = getLocation(this.mFirstVisibleIndex).offset;
            i2 = this.mFirstVisibleIndex - 1;
        } else {
            i2 = this.mStartIndex != -1 ? this.mStartIndex : 0;
            if (i2 > getLastIndex() || i2 < getFirstIndex() - 1) {
                this.mLocations.clear();
                return false;
            } else if (i2 < getFirstIndex()) {
                return false;
            } else {
                i4 = Integer.MAX_VALUE;
                i3 = 0;
            }
        }
        int max = Math.max(this.mProvider.getMinIndex(), this.mFirstIndex);
        while (i2 >= max) {
            Location location = getLocation(i2);
            int i5 = location.row;
            int createItem = this.mProvider.createItem(i2, false, this.mTmpItem, false);
            if (createItem != location.size) {
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
            this.mProvider.addItem(this.mTmpItem[0], i2, createItem, i5, i4 - i3);
            if (!z && checkPrependOverLimit(i)) {
                return true;
            }
            i4 = this.mProvider.getEdge(i2);
            i3 = location.offset;
            if (i5 == 0 && z) {
                return true;
            }
            i2--;
        }
        return false;
    }

    private int calculateOffsetAfterLastItem(int i) {
        boolean z;
        int i2;
        int lastIndex = getLastIndex();
        while (true) {
            if (lastIndex < this.mFirstIndex) {
                z = false;
                break;
            } else if (getLocation(lastIndex).row == i) {
                z = true;
                break;
            } else {
                lastIndex--;
            }
        }
        if (!z) {
            lastIndex = getLastIndex();
        }
        if (isReversedFlow()) {
            i2 = (-getLocation(lastIndex).size) - this.mSpacing;
        } else {
            i2 = getLocation(lastIndex).size + this.mSpacing;
        }
        for (int i3 = lastIndex + 1; i3 <= getLastIndex(); i3++) {
            i2 -= getLocation(i3).offset;
        }
        return i2;
    }

    /* access modifiers changed from: protected */
    public final int prependVisibleItemToRow(int i, int i2, int i3) {
        Object obj;
        if (this.mFirstVisibleIndex < 0 || (this.mFirstVisibleIndex == getFirstIndex() && this.mFirstVisibleIndex == i + 1)) {
            int i4 = this.mFirstIndex;
            Location location = i4 >= 0 ? getLocation(i4) : null;
            int edge = this.mProvider.getEdge(this.mFirstIndex);
            Location location2 = new Location(i2, 0, 0);
            this.mLocations.addFirst(location2);
            if (this.mPendingItem != null) {
                location2.size = this.mPendingItemSize;
                obj = this.mPendingItem;
                this.mPendingItem = null;
            } else {
                location2.size = this.mProvider.createItem(i, false, this.mTmpItem, false);
                obj = this.mTmpItem[0];
            }
            Object obj2 = obj;
            this.mFirstVisibleIndex = i;
            this.mFirstIndex = i;
            if (this.mLastVisibleIndex < 0) {
                this.mLastVisibleIndex = i;
            }
            int i5 = !this.mReversedFlow ? i3 - location2.size : i3 + location2.size;
            if (location != null) {
                location.offset = edge - i5;
            }
            this.mProvider.addItem(obj2, i, location2.size, i2, i5);
            return location2.size;
        }
        throw new IllegalStateException();
    }

    /* JADX INFO: finally extract failed */
    /* access modifiers changed from: protected */
    public final boolean appendVisibleItems(int i, boolean z) {
        if (this.mProvider.getCount() == 0) {
            return false;
        }
        if (!z && checkAppendOverLimit(i)) {
            return false;
        }
        try {
            if (appendVisbleItemsWithCache(i, z)) {
                this.mTmpItem[0] = null;
                this.mPendingItem = null;
                return true;
            }
            boolean appendVisibleItemsWithoutCache = appendVisibleItemsWithoutCache(i, z);
            this.mTmpItem[0] = null;
            this.mPendingItem = null;
            return appendVisibleItemsWithoutCache;
        } catch (Throwable th) {
            this.mTmpItem[0] = null;
            this.mPendingItem = null;
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public final boolean appendVisbleItemsWithCache(int i, boolean z) {
        int i2;
        int i3;
        if (this.mLocations.size() == 0) {
            return false;
        }
        int count = this.mProvider.getCount();
        if (this.mLastVisibleIndex >= 0) {
            i3 = this.mLastVisibleIndex + 1;
            i2 = this.mProvider.getEdge(this.mLastVisibleIndex);
        } else {
            i3 = this.mStartIndex != -1 ? this.mStartIndex : 0;
            if (i3 > getLastIndex() + 1 || i3 < getFirstIndex()) {
                this.mLocations.clear();
                return false;
            } else if (i3 > getLastIndex()) {
                return false;
            } else {
                i2 = Integer.MAX_VALUE;
            }
        }
        int lastIndex = getLastIndex();
        while (i3 < count && i3 <= lastIndex) {
            Location location = getLocation(i3);
            if (i2 != Integer.MAX_VALUE) {
                i2 += location.offset;
            }
            int i4 = location.row;
            int createItem = this.mProvider.createItem(i3, true, this.mTmpItem, false);
            if (createItem != location.size) {
                location.size = createItem;
                this.mLocations.removeFromEnd(lastIndex - i3);
                lastIndex = i3;
            }
            this.mLastVisibleIndex = i3;
            if (this.mFirstVisibleIndex < 0) {
                this.mFirstVisibleIndex = i3;
            }
            this.mProvider.addItem(this.mTmpItem[0], i3, createItem, i4, i2);
            if (!z && checkAppendOverLimit(i)) {
                return true;
            }
            if (i2 == Integer.MAX_VALUE) {
                i2 = this.mProvider.getEdge(i3);
            }
            if (i4 == this.mNumRows - 1 && z) {
                return true;
            }
            i3++;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public final int appendVisibleItemToRow(int i, int i2, int i3) {
        int i4;
        Object obj;
        if (this.mLastVisibleIndex < 0 || (this.mLastVisibleIndex == getLastIndex() && this.mLastVisibleIndex == i - 1)) {
            if (this.mLastVisibleIndex < 0) {
                i4 = (this.mLocations.size() <= 0 || i != getLastIndex() + 1) ? 0 : calculateOffsetAfterLastItem(i2);
            } else {
                i4 = i3 - this.mProvider.getEdge(this.mLastVisibleIndex);
            }
            Location location = new Location(i2, i4, 0);
            this.mLocations.addLast(location);
            if (this.mPendingItem != null) {
                location.size = this.mPendingItemSize;
                obj = this.mPendingItem;
                this.mPendingItem = null;
            } else {
                location.size = this.mProvider.createItem(i, true, this.mTmpItem, false);
                obj = this.mTmpItem[0];
            }
            Object obj2 = obj;
            if (this.mLocations.size() == 1) {
                this.mLastVisibleIndex = i;
                this.mFirstVisibleIndex = i;
                this.mFirstIndex = i;
            } else if (this.mLastVisibleIndex < 0) {
                this.mLastVisibleIndex = i;
                this.mFirstVisibleIndex = i;
            } else {
                this.mLastVisibleIndex++;
            }
            this.mProvider.addItem(obj2, i, location.size, i2, i3);
            return location.size;
        }
        throw new IllegalStateException();
    }

    public final CircularIntArray[] getItemPositionsInRows(int i, int i2) {
        for (int i3 = 0; i3 < this.mNumRows; i3++) {
            this.mTmpItemPositionsInRows[i3].clear();
        }
        if (i >= 0) {
            while (i <= i2) {
                CircularIntArray circularIntArray = this.mTmpItemPositionsInRows[getLocation(i).row];
                if (circularIntArray.size() <= 0 || circularIntArray.getLast() != i - 1) {
                    circularIntArray.addLast(i);
                    circularIntArray.addLast(i);
                } else {
                    circularIntArray.popLast();
                    circularIntArray.addLast(i);
                }
                i++;
            }
        }
        return this.mTmpItemPositionsInRows;
    }

    public void invalidateItemsAfter(int i) {
        super.invalidateItemsAfter(i);
        this.mLocations.removeFromEnd((getLastIndex() - i) + 1);
        if (this.mLocations.size() == 0) {
            this.mFirstIndex = -1;
        }
    }
}
