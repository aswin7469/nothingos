package com.google.android.setupdesign.items;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import com.google.android.setupdesign.items.ItemHierarchy;
import com.google.android.setupdesign.items.ItemInflater;
import java.util.ArrayList;
import java.util.List;

public class ItemGroup extends AbstractItemHierarchy implements ItemInflater.ItemParent, ItemHierarchy.Observer {
    private static final String TAG = "ItemGroup";
    private final List<ItemHierarchy> children = new ArrayList();
    private int count = 0;
    private boolean dirty = false;
    private final SparseIntArray hierarchyStart = new SparseIntArray();

    private static int binarySearch(SparseIntArray sparseIntArray, int i) {
        int size = sparseIntArray.size() - 1;
        int i2 = 0;
        while (i2 <= size) {
            int i3 = (i2 + size) >>> 1;
            int valueAt = sparseIntArray.valueAt(i3);
            if (valueAt < i) {
                i2 = i3 + 1;
            } else if (valueAt <= i) {
                return sparseIntArray.keyAt(i3);
            } else {
                size = i3 - 1;
            }
        }
        return sparseIntArray.keyAt(i2 - 1);
    }

    private static <T> int identityIndexOf(List<T> list, T t) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (list.get(i) == t) {
                return i;
            }
        }
        return -1;
    }

    public ItemGroup() {
    }

    public ItemGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void addChild(ItemHierarchy itemHierarchy) {
        this.dirty = true;
        this.children.add(itemHierarchy);
        itemHierarchy.registerObserver(this);
        int count2 = itemHierarchy.getCount();
        if (count2 > 0) {
            notifyItemRangeInserted(getChildPosition(itemHierarchy), count2);
        }
    }

    public boolean removeChild(ItemHierarchy itemHierarchy) {
        int identityIndexOf = identityIndexOf(this.children, itemHierarchy);
        int childPosition = getChildPosition(identityIndexOf);
        this.dirty = true;
        if (identityIndexOf == -1) {
            return false;
        }
        int count2 = itemHierarchy.getCount();
        this.children.remove(identityIndexOf);
        itemHierarchy.unregisterObserver(this);
        if (count2 > 0) {
            notifyItemRangeRemoved(childPosition, count2);
        }
        return true;
    }

    public void clear() {
        if (!this.children.isEmpty()) {
            int count2 = getCount();
            for (ItemHierarchy unregisterObserver : this.children) {
                unregisterObserver.unregisterObserver(this);
            }
            this.dirty = true;
            this.children.clear();
            notifyItemRangeRemoved(0, count2);
        }
    }

    public int getCount() {
        updateDataIfNeeded();
        return this.count;
    }

    public IItem getItemAt(int i) {
        int itemIndex = getItemIndex(i);
        return this.children.get(itemIndex).getItemAt(i - this.hierarchyStart.get(itemIndex));
    }

    public void onChanged(ItemHierarchy itemHierarchy) {
        this.dirty = true;
        notifyChanged();
    }

    private int getChildPosition(ItemHierarchy itemHierarchy) {
        return getChildPosition(identityIndexOf(this.children, itemHierarchy));
    }

    private int getChildPosition(int i) {
        updateDataIfNeeded();
        if (i == -1) {
            return -1;
        }
        int size = this.children.size();
        int i2 = -1;
        while (i2 < 0 && i < size) {
            i2 = this.hierarchyStart.get(i, -1);
            i++;
        }
        return i2 < 0 ? getCount() : i2;
    }

    public void onItemRangeChanged(ItemHierarchy itemHierarchy, int i, int i2) {
        int childPosition = getChildPosition(itemHierarchy);
        if (childPosition >= 0) {
            notifyItemRangeChanged(childPosition + i, i2);
        } else {
            Log.e(TAG, "Unexpected child change " + itemHierarchy);
        }
    }

    public void onItemRangeInserted(ItemHierarchy itemHierarchy, int i, int i2) {
        this.dirty = true;
        int childPosition = getChildPosition(itemHierarchy);
        if (childPosition >= 0) {
            notifyItemRangeInserted(childPosition + i, i2);
        } else {
            Log.e(TAG, "Unexpected child insert " + itemHierarchy);
        }
    }

    public void onItemRangeMoved(ItemHierarchy itemHierarchy, int i, int i2, int i3) {
        this.dirty = true;
        int childPosition = getChildPosition(itemHierarchy);
        if (childPosition >= 0) {
            notifyItemRangeMoved(i + childPosition, childPosition + i2, i3);
        } else {
            Log.e(TAG, "Unexpected child move " + itemHierarchy);
        }
    }

    public void onItemRangeRemoved(ItemHierarchy itemHierarchy, int i, int i2) {
        this.dirty = true;
        int childPosition = getChildPosition(itemHierarchy);
        if (childPosition >= 0) {
            notifyItemRangeRemoved(childPosition + i, i2);
        } else {
            Log.e(TAG, "Unexpected child remove " + itemHierarchy);
        }
    }

    public ItemHierarchy findItemById(int i) {
        if (i == getId()) {
            return this;
        }
        for (ItemHierarchy findItemById : this.children) {
            ItemHierarchy findItemById2 = findItemById.findItemById(i);
            if (findItemById2 != null) {
                return findItemById2;
            }
        }
        return null;
    }

    private void updateDataIfNeeded() {
        if (this.dirty) {
            this.count = 0;
            this.hierarchyStart.clear();
            for (int i = 0; i < this.children.size(); i++) {
                ItemHierarchy itemHierarchy = this.children.get(i);
                if (itemHierarchy.getCount() > 0) {
                    this.hierarchyStart.put(i, this.count);
                }
                this.count += itemHierarchy.getCount();
            }
            this.dirty = false;
        }
    }

    private int getItemIndex(int i) {
        updateDataIfNeeded();
        if (i < 0 || i >= this.count) {
            throw new IndexOutOfBoundsException("size=" + this.count + "; index=" + i);
        }
        int binarySearch = binarySearch(this.hierarchyStart, i);
        if (binarySearch >= 0) {
            return binarySearch;
        }
        throw new IllegalStateException("Cannot have item start index < 0");
    }
}
