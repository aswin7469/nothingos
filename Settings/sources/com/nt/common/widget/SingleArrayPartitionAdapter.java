package com.nt.common.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.nt.common.widget.BasePartitionAdapter;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class SingleArrayPartitionAdapter<T> extends BasePartitionAdapter {
    protected List<T> mObjects;

    protected abstract void bindView(View view, Context context, int i, int i2, T t, int i3, int i4);

    protected abstract View newView(Context context, int i, int i2, T t, int i3, int i4, ViewGroup viewGroup);

    public SingleArrayPartitionAdapter(Context context, List<T> list, int... iArr) {
        super(context, (iArr == null || iArr.length <= 0) ? 10 : iArr.length);
        this.mObjects = list;
        addPartitions(iArr);
    }

    private void addPartitions(int[] iArr) {
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        setNotificationsEnabled(false);
        for (int i : iArr) {
            addPartition(false, true, i);
        }
        setNotificationsEnabled(true);
    }

    public int addPartition(boolean z, boolean z2, int i) {
        return addPartition(new BasePartitionAdapter.Partition(z, z2, i));
    }

    @Override // com.nt.common.widget.BasePartitionAdapter
    public int addPartition(BasePartitionAdapter.Partition partition) {
        return super.addPartition(partition);
    }

    protected int getDataPosition(int i, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < i; i4++) {
            i3 += this.mPartitions[i4].mItemCount;
        }
        int i5 = i3 + (i2 - this.mPartitions[i].mHeaderViewsCount);
        List<T> list = this.mObjects;
        if (list == null || i5 < list.size()) {
            return i5;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Different data source: ");
        sb.append(this.mObjects == null ? "mObjects null" : "mObjects.size: " + this.mObjects.size());
        sb.append(Arrays.toString(this.mPartitions));
        throw new IllegalStateException(sb.toString());
    }

    @Override // com.nt.common.widget.BasePartitionAdapter
    protected T getItem(int i, int i2) {
        if (this.mObjects == null) {
            return null;
        }
        return this.mObjects.get(getDataPosition(i, i2));
    }

    @Override // com.nt.common.widget.BasePartitionAdapter
    protected long getItemId(int i, int i2) {
        if (this.mObjects == null) {
            return 0L;
        }
        return getDataPosition(i, i2);
    }

    @Override // com.nt.common.widget.BasePartitionAdapter
    protected View getView(int i, int i2, int i3, int i4, View view, ViewGroup viewGroup) {
        if (this.mObjects == null) {
            throw new IllegalStateException("the list is null");
        }
        T t = this.mObjects.get(getDataPosition(i2, i3));
        View newView = view == null ? newView(this.mContext, i, i2, t, i3, i4, viewGroup) : view;
        bindView(newView, this.mContext, i, i2, t, i3, i4);
        return newView;
    }
}
