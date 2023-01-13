package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import com.google.android.setupdesign.C3963R;
import com.google.android.setupdesign.items.ItemHierarchy;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class AbstractItemHierarchy implements ItemHierarchy {
    private static final String TAG = "AbstractItemHierarchy";

    /* renamed from: id */
    private int f456id = -1;
    private final ArrayList<ItemHierarchy.Observer> observers = new ArrayList<>();

    public AbstractItemHierarchy() {
    }

    public AbstractItemHierarchy(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C3963R.styleable.SudAbstractItem);
        this.f456id = obtainStyledAttributes.getResourceId(C3963R.styleable.SudAbstractItem_android_id, -1);
        obtainStyledAttributes.recycle();
    }

    public void setId(int i) {
        this.f456id = i;
    }

    public int getId() {
        return this.f456id;
    }

    public int getViewId() {
        return getId();
    }

    public void registerObserver(ItemHierarchy.Observer observer) {
        this.observers.add(observer);
    }

    public void unregisterObserver(ItemHierarchy.Observer observer) {
        this.observers.remove((Object) observer);
    }

    public void notifyChanged() {
        Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
        while (it.hasNext()) {
            it.next().onChanged(this);
        }
    }

    public void notifyItemRangeChanged(int i, int i2) {
        if (i < 0) {
            Log.w(TAG, "notifyItemRangeChanged: Invalid position=" + i);
        } else if (i2 < 0) {
            Log.w(TAG, "notifyItemRangeChanged: Invalid itemCount=" + i2);
        } else {
            Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onItemRangeChanged(this, i, i2);
            }
        }
    }

    public void notifyItemRangeInserted(int i, int i2) {
        if (i < 0) {
            Log.w(TAG, "notifyItemRangeInserted: Invalid position=" + i);
        } else if (i2 < 0) {
            Log.w(TAG, "notifyItemRangeInserted: Invalid itemCount=" + i2);
        } else {
            Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onItemRangeInserted(this, i, i2);
            }
        }
    }

    public void notifyItemRangeMoved(int i, int i2, int i3) {
        if (i < 0) {
            Log.w(TAG, "notifyItemRangeMoved: Invalid fromPosition=" + i);
        } else if (i2 < 0) {
            Log.w(TAG, "notifyItemRangeMoved: Invalid toPosition=" + i2);
        } else if (i3 < 0) {
            Log.w(TAG, "notifyItemRangeMoved: Invalid itemCount=" + i3);
        } else {
            Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onItemRangeMoved(this, i, i2, i3);
            }
        }
    }

    public void notifyItemRangeRemoved(int i, int i2) {
        if (i < 0) {
            Log.w(TAG, "notifyItemRangeInserted: Invalid position=" + i);
        } else if (i2 < 0) {
            Log.w(TAG, "notifyItemRangeInserted: Invalid itemCount=" + i2);
        } else {
            Iterator<ItemHierarchy.Observer> it = this.observers.iterator();
            while (it.hasNext()) {
                it.next().onItemRangeRemoved(this, i, i2);
            }
        }
    }
}
