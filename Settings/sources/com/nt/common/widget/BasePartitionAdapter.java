package com.nt.common.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public abstract class BasePartitionAdapter extends AbsBasePartitionAdapter {
    protected boolean mCacheValid;
    protected final Context mContext;
    protected int mCount;
    protected int mItemCounts;
    private boolean mNotificationNeeded;
    private boolean mNotificationsEnabled = true;
    protected int mPartitionCount;
    protected Partition[] mPartitions;

    /* loaded from: classes2.dex */
    public class PartitionFixedViewInfo {
        public Object data;
        public boolean isSelectable;
        public View view;
    }

    protected abstract void bindHeaderView(View view, Context context, int i, int i2);

    protected boolean canSelect(int i, int i2) {
        return true;
    }

    protected abstract Object getItem(int i, int i2);

    protected abstract long getItemId(int i, int i2);

    protected int getItemViewType(int i, int i2) {
        return 1;
    }

    public int getItemViewTypeCount() {
        return 1;
    }

    protected abstract View getView(int i, int i2, int i3, int i4, View view, ViewGroup viewGroup);

    protected boolean isEnabled(int i, int i2) {
        return true;
    }

    protected abstract View newHeaderView(Context context, int i, int i2, ViewGroup viewGroup);

    public BasePartitionAdapter(Context context, int i) {
        this.mContext = context;
        this.mPartitions = new Partition[i];
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int addPartition(Partition partition) {
        int i = this.mPartitionCount;
        Partition[] partitionArr = this.mPartitions;
        if (i >= partitionArr.length) {
            Partition[] partitionArr2 = new Partition[i + 10];
            System.arraycopy(partitionArr, 0, partitionArr2, 0, i);
            this.mPartitions = partitionArr2;
        }
        Partition[] partitionArr3 = this.mPartitions;
        int i2 = this.mPartitionCount;
        this.mPartitionCount = i2 + 1;
        partitionArr3[i2] = partition;
        invalidate();
        notifyDataSetChanged();
        return this.mPartitionCount - 1;
    }

    protected void invalidate() {
        this.mCacheValid = false;
    }

    protected void ensureCacheValid() {
        if (!this.mCacheValid) {
            this.mCount = 0;
            this.mItemCounts = 0;
            for (int i = 0; i < this.mPartitionCount; i++) {
                Partition[] partitionArr = this.mPartitions;
                partitionArr[i].mHeaderViewsCount = partitionArr[i].mHeaderViewInfos.size();
                Partition[] partitionArr2 = this.mPartitions;
                partitionArr2[i].mFooterViewsCount = partitionArr2[i].mFooterViewInfos.size();
                Partition[] partitionArr3 = this.mPartitions;
                partitionArr3[i].mCount = partitionArr3[i].mHeaderViewsCount + partitionArr3[i].mItemCount + partitionArr3[i].mFooterViewsCount;
                int i2 = partitionArr3[i].mCount;
                if (partitionArr3[i].mHasHeader && (i2 != 0 || partitionArr3[i].mShowIfEmpty)) {
                    i2++;
                }
                partitionArr3[i].mSize = i2;
                this.mCount += i2;
                this.mItemCounts += partitionArr3[i].mItemCount;
            }
            this.mCacheValid = true;
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        ensureCacheValid();
        return this.mCount;
    }

    protected boolean isHeaderView(int i, int i2) {
        return i2 >= 0 && i2 < this.mPartitions[i].mHeaderViewsCount;
    }

    protected boolean isFooterView(int i, int i2) {
        Partition[] partitionArr = this.mPartitions;
        return i2 >= partitionArr[i].mCount - partitionArr[i].mFooterViewsCount;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return getItemViewTypeCount() + 1;
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int i) {
        ensureCacheValid();
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mPartitionCount) {
            Partition[] partitionArr = this.mPartitions;
            int i4 = partitionArr[i2].mSize + i3;
            if (i >= i3 && i < i4) {
                int i5 = i - i3;
                if (partitionArr[i2].mHasHeader) {
                    i5--;
                }
                if (i5 == -1) {
                    return 0;
                }
                if (!isHeaderView(i2, i5) && !isFooterView(i2, i5)) {
                    return getItemViewType(i2, i);
                }
                return -2;
            }
            i2++;
            i3 = i4;
        }
        throw new ArrayIndexOutOfBoundsException(i);
    }

    protected int getItemBackgroundType(int i, int i2) {
        if (i2 == -1) {
            return 0;
        }
        if (i2 == 0 && this.mPartitions[i].mCount == 1) {
            return 4;
        }
        if (i2 == 0) {
            return 1;
        }
        return i2 == this.mPartitions[i].mCount - 1 ? 3 : 2;
    }

    private boolean areAllPartitionFixedViewsSelectable(ArrayList<PartitionFixedViewInfo> arrayList) {
        Iterator<PartitionFixedViewInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            if (!it.next().isSelectable) {
                return false;
            }
        }
        return true;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        for (int i = 0; i < this.mPartitionCount; i++) {
            Partition[] partitionArr = this.mPartitions;
            if (partitionArr[i].mHasHeader || !areAllPartitionFixedViewsSelectable(partitionArr[i].mHeaderViewInfos) || !areAllPartitionFixedViewsSelectable(this.mPartitions[i].mFooterViewInfos)) {
                return false;
            }
        }
        return true;
    }

    @Override // android.widget.BaseAdapter, android.widget.ListAdapter
    public boolean isEnabled(int i) {
        ensureCacheValid();
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mPartitionCount) {
            Partition[] partitionArr = this.mPartitions;
            int i4 = partitionArr[i2].mSize + i3;
            if (i >= i3 && i < i4) {
                int i5 = i - i3;
                if (partitionArr[i2].mHasHeader) {
                    i5--;
                }
                if (i5 == -1) {
                    return false;
                }
                if (isHeaderView(i2, i5)) {
                    return this.mPartitions[i2].mHeaderViewInfos.get(i5).isSelectable;
                }
                if (isFooterView(i2, i5)) {
                    Partition[] partitionArr2 = this.mPartitions;
                    return partitionArr2[i2].mFooterViewInfos.get(i5 - (partitionArr2[i2].mCount - partitionArr2[i2].mFooterViewsCount)).isSelectable;
                } else if (canSelect(i2, i5)) {
                    return isEnabled(i2, i5);
                } else {
                    return false;
                }
            }
            i2++;
            i3 = i4;
        }
        return false;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        ensureCacheValid();
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mPartitionCount) {
            Partition[] partitionArr = this.mPartitions;
            int i4 = partitionArr[i2].mSize + i3;
            if (i >= i3 && i < i4) {
                int i5 = i - i3;
                if (partitionArr[i2].mHasHeader) {
                    i5--;
                }
                if (i5 == -1) {
                    return null;
                }
                if (isHeaderView(i2, i5)) {
                    return this.mPartitions[i2].mHeaderViewInfos.get(i5).data;
                }
                if (isFooterView(i2, i5)) {
                    Partition[] partitionArr2 = this.mPartitions;
                    return partitionArr2[i2].mFooterViewInfos.get(i5 - (partitionArr2[i2].mCount - partitionArr2[i2].mFooterViewsCount)).data;
                }
                return getItem(i2, i5);
            }
            i2++;
            i3 = i4;
        }
        return null;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        ensureCacheValid();
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mPartitionCount) {
            Partition[] partitionArr = this.mPartitions;
            int i4 = partitionArr[i2].mSize + i3;
            if (i >= i3 && i < i4) {
                int i5 = i - i3;
                if (partitionArr[i2].mHasHeader) {
                    i5--;
                }
                if (i5 == -1) {
                    return 0L;
                }
                if (!isHeaderView(i2, i5) && !isFooterView(i2, i5)) {
                    return getItemId(i2, i5);
                }
                return -1L;
            }
            i2++;
            i3 = i4;
        }
        return 0L;
    }

    public void setNotificationsEnabled(boolean z) {
        this.mNotificationsEnabled = z;
        if (!z || !this.mNotificationNeeded) {
            return;
        }
        notifyDataSetChanged();
    }

    @Override // android.widget.BaseAdapter
    public void notifyDataSetChanged() {
        if (this.mNotificationsEnabled) {
            this.mNotificationNeeded = false;
            super.notifyDataSetChanged();
            return;
        }
        this.mNotificationNeeded = true;
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view2;
        ensureCacheValid();
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mPartitionCount) {
            Partition[] partitionArr = this.mPartitions;
            int i4 = partitionArr[i2].mSize + i3;
            if (i >= i3 && i < i4) {
                int i5 = i - i3;
                if (partitionArr[i2].mHasHeader) {
                    i5--;
                }
                int i6 = i5;
                int itemBackgroundType = getItemBackgroundType(i2, i6);
                if (i6 == -1) {
                    view2 = getHeaderView(i, i2, view, viewGroup);
                } else if (isHeaderView(i2, i6)) {
                    view2 = this.mPartitions[i2].mHeaderViewInfos.get(i6).view;
                } else if (isFooterView(i2, i6)) {
                    Partition[] partitionArr2 = this.mPartitions;
                    view2 = partitionArr2[i2].mFooterViewInfos.get(i6 - (partitionArr2[i2].mCount - partitionArr2[i2].mFooterViewsCount)).view;
                } else {
                    view2 = getView(i, i2, i6, itemBackgroundType, view, viewGroup);
                }
                if (view2 != null) {
                    return view2;
                }
                throw new NullPointerException("View should not be null, partition: " + i2 + " position: " + i);
            }
            i2++;
            i3 = i4;
        }
        throw new ArrayIndexOutOfBoundsException(i);
    }

    protected View getHeaderView(int i, int i2, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = newHeaderView(this.mContext, i, i2, viewGroup);
        }
        bindHeaderView(view, this.mContext, i, i2);
        return view;
    }

    /* loaded from: classes2.dex */
    public static class Partition {
        int mCount;
        int mFooterViewsCount;
        boolean mHasHeader;
        int mHeaderViewsCount;
        int mItemCount;
        boolean mShowIfEmpty;
        int mSize;
        ArrayList<PartitionFixedViewInfo> mHeaderViewInfos = new ArrayList<>();
        ArrayList<PartitionFixedViewInfo> mFooterViewInfos = new ArrayList<>();

        public Partition(boolean z, boolean z2, int i) {
            this.mShowIfEmpty = z;
            this.mHasHeader = z2;
            this.mItemCount = i;
        }

        public String toString() {
            return "\n Partition: mShowIfEmpty: " + this.mShowIfEmpty + ",mHasHeader: " + this.mHasHeader + ",mSize: " + this.mSize + ",mCount: " + this.mCount + ",mItemCount: " + this.mItemCount + ",mHeaderViewsCount: " + this.mHeaderViewsCount + ",mFooterViewsCount: " + this.mFooterViewsCount;
        }
    }
}
