package androidx.recyclerview.widget;

import androidx.recyclerview.widget.AdapterHelper;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class OpReorderer {
    final Callback mCallback;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface Callback {
        AdapterHelper.UpdateOp obtainUpdateOp(int cmd, int startPosition, int itemCount, Object payload);

        void recycleUpdateOp(AdapterHelper.UpdateOp op);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public OpReorderer(Callback callback) {
        this.mCallback = callback;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void reorderOps(List<AdapterHelper.UpdateOp> ops) {
        while (true) {
            int lastMoveOutOfOrder = getLastMoveOutOfOrder(ops);
            if (lastMoveOutOfOrder != -1) {
                swapMoveOp(ops, lastMoveOutOfOrder, lastMoveOutOfOrder + 1);
            } else {
                return;
            }
        }
    }

    private void swapMoveOp(List<AdapterHelper.UpdateOp> list, int badMove, int next) {
        AdapterHelper.UpdateOp updateOp = list.get(badMove);
        AdapterHelper.UpdateOp updateOp2 = list.get(next);
        int i = updateOp2.cmd;
        if (i == 1) {
            swapMoveAdd(list, badMove, updateOp, next, updateOp2);
        } else if (i == 2) {
            swapMoveRemove(list, badMove, updateOp, next, updateOp2);
        } else if (i != 4) {
        } else {
            swapMoveUpdate(list, badMove, updateOp, next, updateOp2);
        }
    }

    void swapMoveRemove(List<AdapterHelper.UpdateOp> list, int movePos, AdapterHelper.UpdateOp moveOp, int removePos, AdapterHelper.UpdateOp removeOp) {
        boolean z;
        int i = moveOp.positionStart;
        int i2 = moveOp.itemCount;
        boolean z2 = false;
        if (i < i2) {
            if (removeOp.positionStart == i && removeOp.itemCount == i2 - i) {
                z = false;
                z2 = true;
            } else {
                z = false;
            }
        } else if (removeOp.positionStart == i2 + 1 && removeOp.itemCount == i - i2) {
            z = true;
            z2 = true;
        } else {
            z = true;
        }
        int i3 = removeOp.positionStart;
        if (i2 < i3) {
            removeOp.positionStart = i3 - 1;
        } else {
            int i4 = removeOp.itemCount;
            if (i2 < i3 + i4) {
                removeOp.itemCount = i4 - 1;
                moveOp.cmd = 2;
                moveOp.itemCount = 1;
                if (removeOp.itemCount != 0) {
                    return;
                }
                list.remove(removePos);
                this.mCallback.recycleUpdateOp(removeOp);
                return;
            }
        }
        int i5 = moveOp.positionStart;
        int i6 = removeOp.positionStart;
        AdapterHelper.UpdateOp updateOp = null;
        if (i5 <= i6) {
            removeOp.positionStart = i6 + 1;
        } else {
            int i7 = removeOp.itemCount;
            if (i5 < i6 + i7) {
                updateOp = this.mCallback.obtainUpdateOp(2, i5 + 1, (i6 + i7) - i5, null);
                removeOp.itemCount = moveOp.positionStart - removeOp.positionStart;
            }
        }
        if (z2) {
            list.set(movePos, removeOp);
            list.remove(removePos);
            this.mCallback.recycleUpdateOp(moveOp);
            return;
        }
        if (z) {
            if (updateOp != null) {
                int i8 = moveOp.positionStart;
                if (i8 > updateOp.positionStart) {
                    moveOp.positionStart = i8 - updateOp.itemCount;
                }
                int i9 = moveOp.itemCount;
                if (i9 > updateOp.positionStart) {
                    moveOp.itemCount = i9 - updateOp.itemCount;
                }
            }
            int i10 = moveOp.positionStart;
            if (i10 > removeOp.positionStart) {
                moveOp.positionStart = i10 - removeOp.itemCount;
            }
            int i11 = moveOp.itemCount;
            if (i11 > removeOp.positionStart) {
                moveOp.itemCount = i11 - removeOp.itemCount;
            }
        } else {
            if (updateOp != null) {
                int i12 = moveOp.positionStart;
                if (i12 >= updateOp.positionStart) {
                    moveOp.positionStart = i12 - updateOp.itemCount;
                }
                int i13 = moveOp.itemCount;
                if (i13 >= updateOp.positionStart) {
                    moveOp.itemCount = i13 - updateOp.itemCount;
                }
            }
            int i14 = moveOp.positionStart;
            if (i14 >= removeOp.positionStart) {
                moveOp.positionStart = i14 - removeOp.itemCount;
            }
            int i15 = moveOp.itemCount;
            if (i15 >= removeOp.positionStart) {
                moveOp.itemCount = i15 - removeOp.itemCount;
            }
        }
        list.set(movePos, removeOp);
        if (moveOp.positionStart != moveOp.itemCount) {
            list.set(removePos, moveOp);
        } else {
            list.remove(removePos);
        }
        if (updateOp == null) {
            return;
        }
        list.add(movePos, updateOp);
    }

    private void swapMoveAdd(List<AdapterHelper.UpdateOp> list, int move, AdapterHelper.UpdateOp moveOp, int add, AdapterHelper.UpdateOp addOp) {
        int i = moveOp.itemCount;
        int i2 = addOp.positionStart;
        int i3 = i < i2 ? -1 : 0;
        int i4 = moveOp.positionStart;
        if (i4 < i2) {
            i3++;
        }
        if (i2 <= i4) {
            moveOp.positionStart = i4 + addOp.itemCount;
        }
        int i5 = addOp.positionStart;
        if (i5 <= i) {
            moveOp.itemCount = i + addOp.itemCount;
        }
        addOp.positionStart = i5 + i3;
        list.set(move, addOp);
        list.set(add, moveOp);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x002b  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0027  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void swapMoveUpdate(List<AdapterHelper.UpdateOp> list, int move, AdapterHelper.UpdateOp moveOp, int update, AdapterHelper.UpdateOp updateOp) {
        AdapterHelper.UpdateOp obtainUpdateOp;
        int i;
        int i2;
        int i3 = moveOp.itemCount;
        int i4 = updateOp.positionStart;
        AdapterHelper.UpdateOp updateOp2 = null;
        if (i3 < i4) {
            updateOp.positionStart = i4 - 1;
        } else {
            int i5 = updateOp.itemCount;
            if (i3 < i4 + i5) {
                updateOp.itemCount = i5 - 1;
                obtainUpdateOp = this.mCallback.obtainUpdateOp(4, moveOp.positionStart, 1, updateOp.payload);
                i = moveOp.positionStart;
                i2 = updateOp.positionStart;
                if (i > i2) {
                    updateOp.positionStart = i2 + 1;
                } else {
                    int i6 = updateOp.itemCount;
                    if (i < i2 + i6) {
                        int i7 = (i2 + i6) - i;
                        updateOp2 = this.mCallback.obtainUpdateOp(4, i + 1, i7, updateOp.payload);
                        updateOp.itemCount -= i7;
                    }
                }
                list.set(update, moveOp);
                if (updateOp.itemCount <= 0) {
                    list.set(move, updateOp);
                } else {
                    list.remove(move);
                    this.mCallback.recycleUpdateOp(updateOp);
                }
                if (obtainUpdateOp != null) {
                    list.add(move, obtainUpdateOp);
                }
                if (updateOp2 != null) {
                    return;
                }
                list.add(move, updateOp2);
                return;
            }
        }
        obtainUpdateOp = null;
        i = moveOp.positionStart;
        i2 = updateOp.positionStart;
        if (i > i2) {
        }
        list.set(update, moveOp);
        if (updateOp.itemCount <= 0) {
        }
        if (obtainUpdateOp != null) {
        }
        if (updateOp2 != null) {
        }
    }

    private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> list) {
        boolean z = false;
        for (int size = list.size() - 1; size >= 0; size--) {
            if (list.get(size).cmd != 8) {
                z = true;
            } else if (z) {
                return size;
            }
        }
        return -1;
    }
}
