package androidx.leanback.widget;

import androidx.leanback.widget.StaggeredGrid;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class StaggeredGridDefault extends StaggeredGrid {
    int getRowMax(int rowIndex) {
        int i;
        StaggeredGrid.Location mo120getLocation;
        int i2 = this.mFirstVisibleIndex;
        if (i2 < 0) {
            return Integer.MIN_VALUE;
        }
        if (this.mReversedFlow) {
            int edge = this.mProvider.getEdge(i2);
            if (mo120getLocation(this.mFirstVisibleIndex).row == rowIndex) {
                return edge;
            }
            int i3 = this.mFirstVisibleIndex;
            do {
                i3++;
                if (i3 <= getLastIndex()) {
                    mo120getLocation = mo120getLocation(i3);
                    edge += mo120getLocation.offset;
                }
            } while (mo120getLocation.row != rowIndex);
            return edge;
        }
        int edge2 = this.mProvider.getEdge(this.mLastVisibleIndex);
        StaggeredGrid.Location mo120getLocation2 = mo120getLocation(this.mLastVisibleIndex);
        if (mo120getLocation2.row == rowIndex) {
            i = mo120getLocation2.size;
        } else {
            int i4 = this.mLastVisibleIndex;
            while (true) {
                i4--;
                if (i4 < getFirstIndex()) {
                    break;
                }
                edge2 -= mo120getLocation2.offset;
                mo120getLocation2 = mo120getLocation(i4);
                if (mo120getLocation2.row == rowIndex) {
                    i = mo120getLocation2.size;
                    break;
                }
            }
        }
        return edge2 + i;
        return Integer.MIN_VALUE;
    }

    int getRowMin(int rowIndex) {
        StaggeredGrid.Location mo120getLocation;
        int i;
        int i2 = this.mFirstVisibleIndex;
        if (i2 < 0) {
            return Integer.MAX_VALUE;
        }
        if (this.mReversedFlow) {
            int edge = this.mProvider.getEdge(this.mLastVisibleIndex);
            StaggeredGrid.Location mo120getLocation2 = mo120getLocation(this.mLastVisibleIndex);
            if (mo120getLocation2.row == rowIndex) {
                i = mo120getLocation2.size;
            } else {
                int i3 = this.mLastVisibleIndex;
                while (true) {
                    i3--;
                    if (i3 < getFirstIndex()) {
                        break;
                    }
                    edge -= mo120getLocation2.offset;
                    mo120getLocation2 = mo120getLocation(i3);
                    if (mo120getLocation2.row == rowIndex) {
                        i = mo120getLocation2.size;
                        break;
                    }
                }
            }
            return edge - i;
        }
        int edge2 = this.mProvider.getEdge(i2);
        if (mo120getLocation(this.mFirstVisibleIndex).row == rowIndex) {
            return edge2;
        }
        int i4 = this.mFirstVisibleIndex;
        do {
            i4++;
            if (i4 <= getLastIndex()) {
                mo120getLocation = mo120getLocation(i4);
                edge2 += mo120getLocation.offset;
            }
        } while (mo120getLocation.row != rowIndex);
        return edge2;
        return Integer.MAX_VALUE;
    }

    @Override // androidx.leanback.widget.Grid
    public int findRowMax(boolean findLarge, int indexLimit, int[] indices) {
        int i;
        int edge = this.mProvider.getEdge(indexLimit);
        StaggeredGrid.Location mo120getLocation = mo120getLocation(indexLimit);
        int i2 = mo120getLocation.row;
        if (this.mReversedFlow) {
            i = i2;
            int i3 = i;
            int i4 = 1;
            int i5 = edge;
            for (int i6 = indexLimit + 1; i4 < this.mNumRows && i6 <= this.mLastVisibleIndex; i6++) {
                StaggeredGrid.Location mo120getLocation2 = mo120getLocation(i6);
                i5 += mo120getLocation2.offset;
                int i7 = mo120getLocation2.row;
                if (i7 != i3) {
                    i4++;
                    if (!findLarge ? i5 >= edge : i5 <= edge) {
                        i3 = i7;
                    } else {
                        edge = i5;
                        indexLimit = i6;
                        i = i7;
                        i3 = i;
                    }
                }
            }
        } else {
            int i8 = 1;
            int i9 = i2;
            StaggeredGrid.Location location = mo120getLocation;
            int i10 = edge;
            edge = this.mProvider.getSize(indexLimit) + edge;
            i = i9;
            for (int i11 = indexLimit - 1; i8 < this.mNumRows && i11 >= this.mFirstVisibleIndex; i11--) {
                i10 -= location.offset;
                location = mo120getLocation(i11);
                int i12 = location.row;
                if (i12 != i9) {
                    i8++;
                    int size = this.mProvider.getSize(i11) + i10;
                    if (!findLarge ? size >= edge : size <= edge) {
                        i9 = i12;
                    } else {
                        edge = size;
                        indexLimit = i11;
                        i = i12;
                        i9 = i;
                    }
                }
            }
        }
        if (indices != null) {
            indices[0] = i;
            indices[1] = indexLimit;
        }
        return edge;
    }

    @Override // androidx.leanback.widget.Grid
    public int findRowMin(boolean findLarge, int indexLimit, int[] indices) {
        int i;
        int edge = this.mProvider.getEdge(indexLimit);
        StaggeredGrid.Location mo120getLocation = mo120getLocation(indexLimit);
        int i2 = mo120getLocation.row;
        if (this.mReversedFlow) {
            int i3 = 1;
            i = edge - this.mProvider.getSize(indexLimit);
            int i4 = i2;
            for (int i5 = indexLimit - 1; i3 < this.mNumRows && i5 >= this.mFirstVisibleIndex; i5--) {
                edge -= mo120getLocation.offset;
                mo120getLocation = mo120getLocation(i5);
                int i6 = mo120getLocation.row;
                if (i6 != i4) {
                    i3++;
                    int size = edge - this.mProvider.getSize(i5);
                    if (!findLarge ? size >= i : size <= i) {
                        i4 = i6;
                    } else {
                        i = size;
                        indexLimit = i5;
                        i2 = i6;
                        i4 = i2;
                    }
                }
            }
        } else {
            int i7 = i2;
            int i8 = i7;
            int i9 = 1;
            int i10 = edge;
            for (int i11 = indexLimit + 1; i9 < this.mNumRows && i11 <= this.mLastVisibleIndex; i11++) {
                StaggeredGrid.Location mo120getLocation2 = mo120getLocation(i11);
                i10 += mo120getLocation2.offset;
                int i12 = mo120getLocation2.row;
                if (i12 != i8) {
                    i9++;
                    if (!findLarge ? i10 >= edge : i10 <= edge) {
                        i8 = i12;
                    } else {
                        edge = i10;
                        indexLimit = i11;
                        i7 = i12;
                        i8 = i7;
                    }
                }
            }
            i = edge;
            i2 = i7;
        }
        if (indices != null) {
            indices[0] = i2;
            indices[1] = indexLimit;
        }
        return i;
    }

    private int findRowEdgeLimitSearchIndex(boolean append) {
        boolean z = false;
        if (append) {
            for (int i = this.mLastVisibleIndex; i >= this.mFirstVisibleIndex; i--) {
                int i2 = mo120getLocation(i).row;
                if (i2 == 0) {
                    z = true;
                } else if (z && i2 == this.mNumRows - 1) {
                    return i;
                }
            }
            return -1;
        }
        for (int i3 = this.mFirstVisibleIndex; i3 <= this.mLastVisibleIndex; i3++) {
            int i4 = mo120getLocation(i3).row;
            if (i4 == this.mNumRows - 1) {
                z = true;
            } else if (z && i4 == 0) {
                return i3;
            }
        }
        return -1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x014c, code lost:
        return r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0134, code lost:
        return true;
     */
    @Override // androidx.leanback.widget.StaggeredGrid
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean appendVisibleItemsWithoutCache(int toLimit, boolean oneColumnMode) {
        int i;
        int i2;
        boolean z;
        int i3;
        int i4;
        int i5;
        int count = this.mProvider.getCount();
        int i6 = this.mLastVisibleIndex;
        if (i6 >= 0) {
            if (i6 < getLastIndex()) {
                return false;
            }
            int i7 = this.mLastVisibleIndex;
            i = i7 + 1;
            i2 = mo120getLocation(i7).row;
            int findRowEdgeLimitSearchIndex = findRowEdgeLimitSearchIndex(true);
            if (findRowEdgeLimitSearchIndex < 0) {
                i3 = Integer.MIN_VALUE;
                for (int i8 = 0; i8 < this.mNumRows; i8++) {
                    i3 = this.mReversedFlow ? getRowMin(i8) : getRowMax(i8);
                    if (i3 != Integer.MIN_VALUE) {
                        break;
                    }
                }
            } else {
                i3 = this.mReversedFlow ? findRowMin(false, findRowEdgeLimitSearchIndex, null) : findRowMax(true, findRowEdgeLimitSearchIndex, null);
            }
            if (!this.mReversedFlow ? getRowMax(i2) >= i3 : getRowMin(i2) <= i3) {
                i2++;
                if (i2 == this.mNumRows) {
                    i3 = this.mReversedFlow ? findRowMin(false, null) : findRowMax(true, null);
                    i2 = 0;
                }
            }
            z = true;
        } else {
            int i9 = this.mStartIndex;
            i = i9 != -1 ? i9 : 0;
            i2 = (this.mLocations.size() > 0 ? mo120getLocation(getLastIndex()).row + 1 : i) % this.mNumRows;
            z = false;
            i3 = 0;
        }
        boolean z2 = false;
        loop1: while (true) {
            if (i2 < this.mNumRows) {
                if (i == count || (!oneColumnMode && checkAppendOverLimit(toLimit))) {
                    break;
                }
                int rowMin = this.mReversedFlow ? getRowMin(i2) : getRowMax(i2);
                if (rowMin == Integer.MAX_VALUE || rowMin == Integer.MIN_VALUE) {
                    if (i2 == 0) {
                        rowMin = this.mReversedFlow ? getRowMin(this.mNumRows - 1) : getRowMax(this.mNumRows - 1);
                        if (rowMin != Integer.MAX_VALUE && rowMin != Integer.MIN_VALUE) {
                            if (this.mReversedFlow) {
                                i5 = this.mSpacing;
                                i4 = -i5;
                                rowMin += i4;
                            } else {
                                i4 = this.mSpacing;
                                rowMin += i4;
                            }
                        }
                    } else {
                        rowMin = this.mReversedFlow ? getRowMax(i2 - 1) : getRowMin(i2 - 1);
                    }
                } else if (this.mReversedFlow) {
                    i5 = this.mSpacing;
                    i4 = -i5;
                    rowMin += i4;
                } else {
                    i4 = this.mSpacing;
                    rowMin += i4;
                }
                int i10 = i + 1;
                int appendVisibleItemToRow = appendVisibleItemToRow(i, i2, rowMin);
                if (z) {
                    while (true) {
                        if (!this.mReversedFlow) {
                            if (rowMin + appendVisibleItemToRow >= i3) {
                                break;
                            }
                            if (i10 == count) {
                                break loop1;
                            }
                            break loop1;
                        }
                        if (rowMin - appendVisibleItemToRow <= i3) {
                            break;
                        }
                        if (i10 == count || (!oneColumnMode && checkAppendOverLimit(toLimit))) {
                            break loop1;
                        }
                        rowMin += this.mReversedFlow ? (-appendVisibleItemToRow) - this.mSpacing : appendVisibleItemToRow + this.mSpacing;
                        int i11 = i10 + 1;
                        int appendVisibleItemToRow2 = appendVisibleItemToRow(i10, i2, rowMin);
                        i10 = i11;
                        appendVisibleItemToRow = appendVisibleItemToRow2;
                    }
                } else {
                    z = true;
                    i3 = this.mReversedFlow ? getRowMin(i2) : getRowMax(i2);
                }
                i = i10;
                i2++;
                z2 = true;
            } else if (oneColumnMode) {
                return z2;
            } else {
                i3 = this.mReversedFlow ? findRowMin(false, null) : findRowMax(true, null);
                i2 = 0;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:41:0x0146, code lost:
        return r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x012e, code lost:
        return true;
     */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00ff A[LOOP:2: B:54:0x00ff->B:68:0x0123, LOOP_START, PHI: r5 r8 r9 
      PHI: (r5v12 int) = (r5v6 int), (r5v17 int) binds: [B:53:0x00fd, B:68:0x0123] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r8v19 int) = (r8v17 int), (r8v20 int) binds: [B:53:0x00fd, B:68:0x0123] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r9v8 int) = (r9v6 int), (r9v10 int) binds: [B:53:0x00fd, B:68:0x0123] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0131  */
    @Override // androidx.leanback.widget.StaggeredGrid
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected boolean prependVisibleItemsWithoutCache(int toLimit, boolean oneColumnMode) {
        int i;
        int i2;
        boolean z;
        int i3;
        int i4;
        int i5;
        int i6 = this.mFirstVisibleIndex;
        if (i6 >= 0) {
            if (i6 > getFirstIndex()) {
                return false;
            }
            int i7 = this.mFirstVisibleIndex;
            i = i7 - 1;
            i2 = mo120getLocation(i7).row;
            int findRowEdgeLimitSearchIndex = findRowEdgeLimitSearchIndex(false);
            if (findRowEdgeLimitSearchIndex < 0) {
                i2--;
                i3 = Integer.MAX_VALUE;
                for (int i8 = this.mNumRows - 1; i8 >= 0; i8--) {
                    i3 = this.mReversedFlow ? getRowMax(i8) : getRowMin(i8);
                    if (i3 != Integer.MAX_VALUE) {
                        break;
                    }
                }
            } else {
                i3 = this.mReversedFlow ? findRowMax(true, findRowEdgeLimitSearchIndex, null) : findRowMin(false, findRowEdgeLimitSearchIndex, null);
            }
            if (!this.mReversedFlow ? getRowMin(i2) <= i3 : getRowMax(i2) >= i3) {
                i2--;
                if (i2 < 0) {
                    i2 = this.mNumRows - 1;
                    i3 = this.mReversedFlow ? findRowMax(true, null) : findRowMin(false, null);
                }
            }
            z = true;
        } else {
            int i9 = this.mStartIndex;
            i = i9 != -1 ? i9 : 0;
            i2 = (this.mLocations.size() > 0 ? (mo120getLocation(getFirstIndex()).row + this.mNumRows) - 1 : i) % this.mNumRows;
            z = false;
            i3 = 0;
        }
        boolean z2 = false;
        loop1: while (true) {
            if (i2 >= 0) {
                if (i < 0 || (!oneColumnMode && checkPrependOverLimit(toLimit))) {
                    break;
                }
                int rowMax = this.mReversedFlow ? getRowMax(i2) : getRowMin(i2);
                if (rowMax == Integer.MAX_VALUE || rowMax == Integer.MIN_VALUE) {
                    if (i2 == this.mNumRows - 1) {
                        rowMax = this.mReversedFlow ? getRowMax(0) : getRowMin(0);
                        if (rowMax != Integer.MAX_VALUE && rowMax != Integer.MIN_VALUE) {
                            if (this.mReversedFlow) {
                                i5 = this.mSpacing;
                                rowMax += i5;
                            } else {
                                i4 = this.mSpacing;
                                i5 = -i4;
                                rowMax += i5;
                            }
                        }
                    } else {
                        rowMax = this.mReversedFlow ? getRowMin(i2 + 1) : getRowMax(i2 + 1);
                    }
                    int i10 = i - 1;
                    int prependVisibleItemToRow = prependVisibleItemToRow(i, i2, rowMax);
                    if (z) {
                        while (true) {
                            if (!this.mReversedFlow) {
                                if (rowMax - prependVisibleItemToRow <= i3) {
                                    break;
                                }
                                if (i10 < 0) {
                                    break loop1;
                                }
                                break loop1;
                            }
                            if (rowMax + prependVisibleItemToRow >= i3) {
                                break;
                            }
                            if (i10 < 0 || (!oneColumnMode && checkPrependOverLimit(toLimit))) {
                                break loop1;
                            }
                            rowMax += this.mReversedFlow ? prependVisibleItemToRow + this.mSpacing : (-prependVisibleItemToRow) - this.mSpacing;
                            int i11 = i10 - 1;
                            int prependVisibleItemToRow2 = prependVisibleItemToRow(i10, i2, rowMax);
                            i10 = i11;
                            prependVisibleItemToRow = prependVisibleItemToRow2;
                        }
                    } else {
                        z = true;
                        i3 = this.mReversedFlow ? getRowMax(i2) : getRowMin(i2);
                    }
                    i = i10;
                    i2--;
                    z2 = true;
                } else if (this.mReversedFlow) {
                    i5 = this.mSpacing;
                    rowMax += i5;
                    int i102 = i - 1;
                    int prependVisibleItemToRow3 = prependVisibleItemToRow(i, i2, rowMax);
                    if (z) {
                    }
                    i = i102;
                    i2--;
                    z2 = true;
                } else {
                    i4 = this.mSpacing;
                    i5 = -i4;
                    rowMax += i5;
                    int i1022 = i - 1;
                    int prependVisibleItemToRow32 = prependVisibleItemToRow(i, i2, rowMax);
                    if (z) {
                    }
                    i = i1022;
                    i2--;
                    z2 = true;
                }
            } else if (oneColumnMode) {
                return z2;
            } else {
                i3 = this.mReversedFlow ? findRowMax(true, null) : findRowMin(false, null);
                i2 = this.mNumRows - 1;
            }
        }
    }
}
