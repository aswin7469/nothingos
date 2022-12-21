package androidx.leanback.widget;

import androidx.leanback.widget.StaggeredGrid;

final class StaggeredGridDefault extends StaggeredGrid {
    StaggeredGridDefault() {
    }

    /* access modifiers changed from: package-private */
    public int getRowMax(int i) {
        int i2;
        StaggeredGrid.Location location;
        if (this.mFirstVisibleIndex < 0) {
            return Integer.MIN_VALUE;
        }
        if (this.mReversedFlow) {
            int edge = this.mProvider.getEdge(this.mFirstVisibleIndex);
            if (getLocation(this.mFirstVisibleIndex).row == i) {
                return edge;
            }
            int i3 = this.mFirstVisibleIndex;
            do {
                i3++;
                if (i3 <= getLastIndex()) {
                    location = getLocation(i3);
                    edge += location.offset;
                }
            } while (location.row != i);
            return edge;
        }
        int edge2 = this.mProvider.getEdge(this.mLastVisibleIndex);
        StaggeredGrid.Location location2 = getLocation(this.mLastVisibleIndex);
        if (location2.row != i) {
            int i4 = this.mLastVisibleIndex;
            while (true) {
                i4--;
                if (i4 < getFirstIndex()) {
                    break;
                }
                edge2 -= location2.offset;
                location2 = getLocation(i4);
                if (location2.row == i) {
                    i2 = location2.size;
                    break;
                }
            }
        } else {
            i2 = location2.size;
        }
        return edge2 + i2;
        return Integer.MIN_VALUE;
    }

    /* access modifiers changed from: package-private */
    public int getRowMin(int i) {
        StaggeredGrid.Location location;
        int i2;
        if (this.mFirstVisibleIndex < 0) {
            return Integer.MAX_VALUE;
        }
        if (this.mReversedFlow) {
            int edge = this.mProvider.getEdge(this.mLastVisibleIndex);
            StaggeredGrid.Location location2 = getLocation(this.mLastVisibleIndex);
            if (location2.row != i) {
                int i3 = this.mLastVisibleIndex;
                while (true) {
                    i3--;
                    if (i3 < getFirstIndex()) {
                        break;
                    }
                    edge -= location2.offset;
                    location2 = getLocation(i3);
                    if (location2.row == i) {
                        i2 = location2.size;
                        break;
                    }
                }
            } else {
                i2 = location2.size;
            }
            return edge - i2;
        }
        int edge2 = this.mProvider.getEdge(this.mFirstVisibleIndex);
        if (getLocation(this.mFirstVisibleIndex).row == i) {
            return edge2;
        }
        int i4 = this.mFirstVisibleIndex;
        do {
            i4++;
            if (i4 <= getLastIndex()) {
                location = getLocation(i4);
                edge2 += location.offset;
            }
        } while (location.row != i);
        return edge2;
        return Integer.MAX_VALUE;
    }

    public int findRowMax(boolean z, int i, int[] iArr) {
        int i2;
        int edge = this.mProvider.getEdge(i);
        StaggeredGrid.Location location = getLocation(i);
        int i3 = location.row;
        if (this.mReversedFlow) {
            i2 = i3;
            int i4 = i2;
            int i5 = 1;
            int i6 = i + 1;
            int i7 = edge;
            while (i5 < this.mNumRows && i6 <= this.mLastVisibleIndex) {
                StaggeredGrid.Location location2 = getLocation(i6);
                i7 += location2.offset;
                if (location2.row != i4) {
                    i4 = location2.row;
                    i5++;
                    if (z) {
                        if (i7 <= edge) {
                        }
                    } else if (i7 >= edge) {
                    }
                    edge = i7;
                    i = i6;
                    i2 = i4;
                }
                i6++;
            }
        } else {
            int i8 = 1;
            int i9 = i - 1;
            int i10 = i3;
            StaggeredGrid.Location location3 = location;
            int i11 = edge;
            edge = this.mProvider.getSize(i) + edge;
            i2 = i10;
            while (i8 < this.mNumRows && i9 >= this.mFirstVisibleIndex) {
                i11 -= location3.offset;
                location3 = getLocation(i9);
                if (location3.row != i10) {
                    i10 = location3.row;
                    i8++;
                    int size = this.mProvider.getSize(i9) + i11;
                    if (z) {
                        if (size <= edge) {
                        }
                    } else if (size >= edge) {
                    }
                    i2 = i10;
                    i = i9;
                    edge = size;
                }
                i9--;
            }
        }
        if (iArr != null) {
            iArr[0] = i2;
            iArr[1] = i;
        }
        return edge;
    }

    public int findRowMin(boolean z, int i, int[] iArr) {
        int i2;
        int edge = this.mProvider.getEdge(i);
        StaggeredGrid.Location location = getLocation(i);
        int i3 = location.row;
        if (this.mReversedFlow) {
            int i4 = 1;
            int i5 = i - 1;
            i2 = edge - this.mProvider.getSize(i);
            int i6 = i3;
            while (i4 < this.mNumRows && i5 >= this.mFirstVisibleIndex) {
                edge -= location.offset;
                location = getLocation(i5);
                if (location.row != i6) {
                    i6 = location.row;
                    i4++;
                    int size = edge - this.mProvider.getSize(i5);
                    if (z) {
                        if (size <= i2) {
                        }
                    } else if (size >= i2) {
                    }
                    i3 = i6;
                    i = i5;
                    i2 = size;
                }
                i5--;
            }
        } else {
            int i7 = i3;
            int i8 = i7;
            int i9 = 1;
            int i10 = i + 1;
            int i11 = edge;
            while (i9 < this.mNumRows && i10 <= this.mLastVisibleIndex) {
                StaggeredGrid.Location location2 = getLocation(i10);
                i11 += location2.offset;
                if (location2.row != i8) {
                    i8 = location2.row;
                    i9++;
                    if (z) {
                        if (i11 <= edge) {
                        }
                    } else if (i11 >= edge) {
                    }
                    edge = i11;
                    i = i10;
                    i7 = i8;
                }
                i10++;
            }
            i2 = edge;
            i3 = i7;
        }
        if (iArr != null) {
            iArr[0] = i3;
            iArr[1] = i;
        }
        return i2;
    }

    private int findRowEdgeLimitSearchIndex(boolean z) {
        boolean z2 = false;
        if (z) {
            for (int i = this.mLastVisibleIndex; i >= this.mFirstVisibleIndex; i--) {
                int i2 = getLocation(i).row;
                if (i2 == 0) {
                    z2 = true;
                } else if (z2 && i2 == this.mNumRows - 1) {
                    return i;
                }
            }
            return -1;
        }
        for (int i3 = this.mFirstVisibleIndex; i3 <= this.mLastVisibleIndex; i3++) {
            int i4 = getLocation(i3).row;
            if (i4 == this.mNumRows - 1) {
                z2 = true;
            } else if (z2 && i4 == 0) {
                return i3;
            }
        }
        return -1;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:81:0x0109 A[LOOP:2: B:81:0x0109->B:95:0x012d, LOOP_START, PHI: r1 r9 r10 
      PHI: (r1v10 int) = (r1v4 int), (r1v13 int) binds: [B:80:0x0107, B:95:0x012d] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r9v7 int) = (r9v5 int), (r9v8 int) binds: [B:80:0x0107, B:95:0x012d] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r10v4 int) = (r10v2 int), (r10v6 int) binds: [B:80:0x0107, B:95:0x012d] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:98:0x013b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean appendVisibleItemsWithoutCache(int r14, boolean r15) {
        /*
            r13 = this;
            androidx.leanback.widget.Grid$Provider r0 = r13.mProvider
            int r0 = r0.getCount()
            int r1 = r13.mLastVisibleIndex
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = 0
            r4 = 0
            r5 = 1
            if (r1 < 0) goto L_0x0078
            int r1 = r13.mLastVisibleIndex
            int r6 = r13.getLastIndex()
            if (r1 >= r6) goto L_0x0018
            return r4
        L_0x0018:
            int r1 = r13.mLastVisibleIndex
            int r1 = r1 + r5
            int r6 = r13.mLastVisibleIndex
            androidx.leanback.widget.StaggeredGrid$Location r6 = r13.getLocation((int) r6)
            int r6 = r6.row
            int r7 = r13.findRowEdgeLimitSearchIndex(r5)
            if (r7 >= 0) goto L_0x0042
            r8 = r2
            r7 = r4
        L_0x002b:
            int r9 = r13.mNumRows
            if (r7 >= r9) goto L_0x0050
            boolean r8 = r13.mReversedFlow
            if (r8 == 0) goto L_0x0038
            int r8 = r13.getRowMin(r7)
            goto L_0x003c
        L_0x0038:
            int r8 = r13.getRowMax(r7)
        L_0x003c:
            if (r8 == r2) goto L_0x003f
            goto L_0x0050
        L_0x003f:
            int r7 = r7 + 1
            goto L_0x002b
        L_0x0042:
            boolean r8 = r13.mReversedFlow
            if (r8 == 0) goto L_0x004b
            int r7 = r13.findRowMin(r4, r7, r3)
            goto L_0x004f
        L_0x004b:
            int r7 = r13.findRowMax(r5, r7, r3)
        L_0x004f:
            r8 = r7
        L_0x0050:
            boolean r7 = r13.mReversedFlow
            if (r7 == 0) goto L_0x005b
            int r7 = r13.getRowMin(r6)
            if (r7 > r8) goto L_0x0076
            goto L_0x0061
        L_0x005b:
            int r7 = r13.getRowMax(r6)
            if (r7 < r8) goto L_0x0076
        L_0x0061:
            int r6 = r6 + 1
            int r7 = r13.mNumRows
            if (r6 != r7) goto L_0x0076
            boolean r6 = r13.mReversedFlow
            if (r6 == 0) goto L_0x0070
            int r6 = r13.findRowMin(r4, r3)
            goto L_0x0074
        L_0x0070:
            int r6 = r13.findRowMax(r5, r3)
        L_0x0074:
            r8 = r6
            r6 = r4
        L_0x0076:
            r7 = r5
            goto L_0x009b
        L_0x0078:
            int r1 = r13.mStartIndex
            r6 = -1
            if (r1 == r6) goto L_0x0080
            int r1 = r13.mStartIndex
            goto L_0x0081
        L_0x0080:
            r1 = r4
        L_0x0081:
            androidx.collection.CircularArray r6 = r13.mLocations
            int r6 = r6.size()
            if (r6 <= 0) goto L_0x0095
            int r6 = r13.getLastIndex()
            androidx.leanback.widget.StaggeredGrid$Location r6 = r13.getLocation((int) r6)
            int r6 = r6.row
            int r6 = r6 + r5
            goto L_0x0096
        L_0x0095:
            r6 = r1
        L_0x0096:
            int r7 = r13.mNumRows
            int r6 = r6 % r7
            r7 = r4
            r8 = r7
        L_0x009b:
            r9 = r4
        L_0x009c:
            int r10 = r13.mNumRows
            if (r6 >= r10) goto L_0x0151
            if (r1 == r0) goto L_0x0150
            if (r15 != 0) goto L_0x00ac
            boolean r10 = r13.checkAppendOverLimit(r14)
            if (r10 == 0) goto L_0x00ac
            goto L_0x0150
        L_0x00ac:
            boolean r9 = r13.mReversedFlow
            if (r9 == 0) goto L_0x00b5
            int r9 = r13.getRowMin(r6)
            goto L_0x00b9
        L_0x00b5:
            int r9 = r13.getRowMax(r6)
        L_0x00b9:
            r10 = 2147483647(0x7fffffff, float:NaN)
            if (r9 == r10) goto L_0x00cd
            if (r9 != r2) goto L_0x00c1
            goto L_0x00cd
        L_0x00c1:
            boolean r10 = r13.mReversedFlow
            if (r10 == 0) goto L_0x00c9
            int r10 = r13.mSpacing
        L_0x00c7:
            int r10 = -r10
            goto L_0x00cb
        L_0x00c9:
            int r10 = r13.mSpacing
        L_0x00cb:
            int r9 = r9 + r10
            goto L_0x0101
        L_0x00cd:
            if (r6 != 0) goto L_0x00f0
            boolean r9 = r13.mReversedFlow
            if (r9 == 0) goto L_0x00db
            int r9 = r13.mNumRows
            int r9 = r9 - r5
            int r9 = r13.getRowMin(r9)
            goto L_0x00e2
        L_0x00db:
            int r9 = r13.mNumRows
            int r9 = r9 - r5
            int r9 = r13.getRowMax(r9)
        L_0x00e2:
            if (r9 == r10) goto L_0x0101
            if (r9 == r2) goto L_0x0101
            boolean r10 = r13.mReversedFlow
            if (r10 == 0) goto L_0x00ed
            int r10 = r13.mSpacing
            goto L_0x00c7
        L_0x00ed:
            int r10 = r13.mSpacing
            goto L_0x00cb
        L_0x00f0:
            boolean r9 = r13.mReversedFlow
            if (r9 == 0) goto L_0x00fb
            int r9 = r6 + -1
            int r9 = r13.getRowMax(r9)
            goto L_0x0101
        L_0x00fb:
            int r9 = r6 + -1
            int r9 = r13.getRowMin(r9)
        L_0x0101:
            int r10 = r1 + 1
            int r1 = r13.appendVisibleItemToRow(r1, r6, r9)
            if (r7 == 0) goto L_0x013b
        L_0x0109:
            boolean r11 = r13.mReversedFlow
            if (r11 == 0) goto L_0x0112
            int r11 = r9 - r1
            if (r11 <= r8) goto L_0x0139
            goto L_0x0116
        L_0x0112:
            int r11 = r9 + r1
            if (r11 >= r8) goto L_0x0139
        L_0x0116:
            if (r10 == r0) goto L_0x0138
            if (r15 != 0) goto L_0x0121
            boolean r11 = r13.checkAppendOverLimit(r14)
            if (r11 == 0) goto L_0x0121
            goto L_0x0138
        L_0x0121:
            boolean r11 = r13.mReversedFlow
            if (r11 == 0) goto L_0x012a
            int r1 = -r1
            int r11 = r13.mSpacing
            int r1 = r1 - r11
            goto L_0x012d
        L_0x012a:
            int r11 = r13.mSpacing
            int r1 = r1 + r11
        L_0x012d:
            int r9 = r9 + r1
            int r1 = r10 + 1
            int r10 = r13.appendVisibleItemToRow(r10, r6, r9)
            r12 = r10
            r10 = r1
            r1 = r12
            goto L_0x0109
        L_0x0138:
            return r5
        L_0x0139:
            r1 = r10
            goto L_0x014b
        L_0x013b:
            boolean r1 = r13.mReversedFlow
            if (r1 == 0) goto L_0x0144
            int r1 = r13.getRowMin(r6)
            goto L_0x0148
        L_0x0144:
            int r1 = r13.getRowMax(r6)
        L_0x0148:
            r8 = r1
            r7 = r5
            goto L_0x0139
        L_0x014b:
            int r6 = r6 + 1
            r9 = r5
            goto L_0x009c
        L_0x0150:
            return r9
        L_0x0151:
            if (r15 == 0) goto L_0x0154
            return r9
        L_0x0154:
            boolean r6 = r13.mReversedFlow
            if (r6 == 0) goto L_0x015d
            int r6 = r13.findRowMin(r4, r3)
            goto L_0x0161
        L_0x015d:
            int r6 = r13.findRowMax(r5, r3)
        L_0x0161:
            r8 = r6
            r6 = r4
            goto L_0x009c
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.StaggeredGridDefault.appendVisibleItemsWithoutCache(int, boolean):boolean");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0103 A[LOOP:2: B:80:0x0103->B:94:0x0127, LOOP_START, PHI: r0 r8 r9 
      PHI: (r0v10 int) = (r0v4 int), (r0v13 int) binds: [B:79:0x0101, B:94:0x0127] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r8v7 int) = (r8v5 int), (r8v8 int) binds: [B:79:0x0101, B:94:0x0127] A[DONT_GENERATE, DONT_INLINE]
      PHI: (r9v3 int) = (r9v1 int), (r9v5 int) binds: [B:79:0x0101, B:94:0x0127] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x0135  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean prependVisibleItemsWithoutCache(int r13, boolean r14) {
        /*
            r12 = this;
            int r0 = r12.mFirstVisibleIndex
            r1 = 2147483647(0x7fffffff, float:NaN)
            r2 = 0
            r3 = 0
            r4 = 1
            if (r0 < 0) goto L_0x0075
            int r0 = r12.mFirstVisibleIndex
            int r5 = r12.getFirstIndex()
            if (r0 <= r5) goto L_0x0013
            return r3
        L_0x0013:
            int r0 = r12.mFirstVisibleIndex
            int r0 = r0 - r4
            int r5 = r12.mFirstVisibleIndex
            androidx.leanback.widget.StaggeredGrid$Location r5 = r12.getLocation((int) r5)
            int r5 = r5.row
            int r6 = r12.findRowEdgeLimitSearchIndex(r3)
            if (r6 >= 0) goto L_0x003f
            int r5 = r5 + -1
            int r6 = r12.mNumRows
            int r6 = r6 - r4
            r7 = r1
        L_0x002a:
            if (r6 < 0) goto L_0x004d
            boolean r7 = r12.mReversedFlow
            if (r7 == 0) goto L_0x0035
            int r7 = r12.getRowMax(r6)
            goto L_0x0039
        L_0x0035:
            int r7 = r12.getRowMin(r6)
        L_0x0039:
            if (r7 == r1) goto L_0x003c
            goto L_0x004d
        L_0x003c:
            int r6 = r6 + -1
            goto L_0x002a
        L_0x003f:
            boolean r7 = r12.mReversedFlow
            if (r7 == 0) goto L_0x0048
            int r6 = r12.findRowMax(r4, r6, r2)
            goto L_0x004c
        L_0x0048:
            int r6 = r12.findRowMin(r3, r6, r2)
        L_0x004c:
            r7 = r6
        L_0x004d:
            boolean r6 = r12.mReversedFlow
            if (r6 == 0) goto L_0x0058
            int r6 = r12.getRowMax(r5)
            if (r6 < r7) goto L_0x0073
            goto L_0x005e
        L_0x0058:
            int r6 = r12.getRowMin(r5)
            if (r6 > r7) goto L_0x0073
        L_0x005e:
            int r5 = r5 + -1
            if (r5 >= 0) goto L_0x0073
            int r5 = r12.mNumRows
            int r5 = r5 - r4
            boolean r6 = r12.mReversedFlow
            if (r6 == 0) goto L_0x006e
            int r6 = r12.findRowMax(r4, r2)
            goto L_0x0072
        L_0x006e:
            int r6 = r12.findRowMin(r3, r2)
        L_0x0072:
            r7 = r6
        L_0x0073:
            r6 = r4
            goto L_0x009b
        L_0x0075:
            int r0 = r12.mStartIndex
            r5 = -1
            if (r0 == r5) goto L_0x007d
            int r0 = r12.mStartIndex
            goto L_0x007e
        L_0x007d:
            r0 = r3
        L_0x007e:
            androidx.collection.CircularArray r5 = r12.mLocations
            int r5 = r5.size()
            if (r5 <= 0) goto L_0x0095
            int r5 = r12.getFirstIndex()
            androidx.leanback.widget.StaggeredGrid$Location r5 = r12.getLocation((int) r5)
            int r5 = r5.row
            int r6 = r12.mNumRows
            int r5 = r5 + r6
            int r5 = r5 - r4
            goto L_0x0096
        L_0x0095:
            r5 = r0
        L_0x0096:
            int r6 = r12.mNumRows
            int r5 = r5 % r6
            r6 = r3
            r7 = r6
        L_0x009b:
            r8 = r3
        L_0x009c:
            if (r5 < 0) goto L_0x014b
            if (r0 < 0) goto L_0x014a
            if (r14 != 0) goto L_0x00aa
            boolean r9 = r12.checkPrependOverLimit(r13)
            if (r9 == 0) goto L_0x00aa
            goto L_0x014a
        L_0x00aa:
            boolean r8 = r12.mReversedFlow
            if (r8 == 0) goto L_0x00b3
            int r8 = r12.getRowMax(r5)
            goto L_0x00b7
        L_0x00b3:
            int r8 = r12.getRowMin(r5)
        L_0x00b7:
            r9 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r8 == r1) goto L_0x00ca
            if (r8 != r9) goto L_0x00be
            goto L_0x00ca
        L_0x00be:
            boolean r9 = r12.mReversedFlow
            if (r9 == 0) goto L_0x00c5
            int r9 = r12.mSpacing
            goto L_0x00c8
        L_0x00c5:
            int r9 = r12.mSpacing
        L_0x00c7:
            int r9 = -r9
        L_0x00c8:
            int r8 = r8 + r9
            goto L_0x00fb
        L_0x00ca:
            int r8 = r12.mNumRows
            int r8 = r8 - r4
            if (r5 != r8) goto L_0x00ea
            boolean r8 = r12.mReversedFlow
            if (r8 == 0) goto L_0x00d8
            int r8 = r12.getRowMax(r3)
            goto L_0x00dc
        L_0x00d8:
            int r8 = r12.getRowMin(r3)
        L_0x00dc:
            if (r8 == r1) goto L_0x00fb
            if (r8 == r9) goto L_0x00fb
            boolean r9 = r12.mReversedFlow
            if (r9 == 0) goto L_0x00e7
            int r9 = r12.mSpacing
            goto L_0x00c8
        L_0x00e7:
            int r9 = r12.mSpacing
            goto L_0x00c7
        L_0x00ea:
            boolean r8 = r12.mReversedFlow
            if (r8 == 0) goto L_0x00f5
            int r8 = r5 + 1
            int r8 = r12.getRowMin(r8)
            goto L_0x00fb
        L_0x00f5:
            int r8 = r5 + 1
            int r8 = r12.getRowMax(r8)
        L_0x00fb:
            int r9 = r0 + -1
            int r0 = r12.prependVisibleItemToRow(r0, r5, r8)
            if (r6 == 0) goto L_0x0135
        L_0x0103:
            boolean r10 = r12.mReversedFlow
            if (r10 == 0) goto L_0x010c
            int r10 = r8 + r0
            if (r10 >= r7) goto L_0x0133
            goto L_0x0110
        L_0x010c:
            int r10 = r8 - r0
            if (r10 <= r7) goto L_0x0133
        L_0x0110:
            if (r9 < 0) goto L_0x0132
            if (r14 != 0) goto L_0x011b
            boolean r10 = r12.checkPrependOverLimit(r13)
            if (r10 == 0) goto L_0x011b
            goto L_0x0132
        L_0x011b:
            boolean r10 = r12.mReversedFlow
            if (r10 == 0) goto L_0x0123
            int r10 = r12.mSpacing
            int r0 = r0 + r10
            goto L_0x0127
        L_0x0123:
            int r0 = -r0
            int r10 = r12.mSpacing
            int r0 = r0 - r10
        L_0x0127:
            int r8 = r8 + r0
            int r0 = r9 + -1
            int r9 = r12.prependVisibleItemToRow(r9, r5, r8)
            r11 = r9
            r9 = r0
            r0 = r11
            goto L_0x0103
        L_0x0132:
            return r4
        L_0x0133:
            r0 = r9
            goto L_0x0145
        L_0x0135:
            boolean r0 = r12.mReversedFlow
            if (r0 == 0) goto L_0x013e
            int r0 = r12.getRowMax(r5)
            goto L_0x0142
        L_0x013e:
            int r0 = r12.getRowMin(r5)
        L_0x0142:
            r7 = r0
            r6 = r4
            goto L_0x0133
        L_0x0145:
            int r5 = r5 + -1
            r8 = r4
            goto L_0x009c
        L_0x014a:
            return r8
        L_0x014b:
            if (r14 == 0) goto L_0x014e
            return r8
        L_0x014e:
            boolean r5 = r12.mReversedFlow
            if (r5 == 0) goto L_0x0157
            int r5 = r12.findRowMax(r4, r2)
            goto L_0x015b
        L_0x0157:
            int r5 = r12.findRowMin(r3, r2)
        L_0x015b:
            r7 = r5
            int r5 = r12.mNumRows
            int r5 = r5 - r4
            goto L_0x009c
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.StaggeredGridDefault.prependVisibleItemsWithoutCache(int, boolean):boolean");
    }
}
