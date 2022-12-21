package androidx.leanback.widget;

import android.util.Log;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ArrayObjectAdapter extends ObjectAdapter {
    /* access modifiers changed from: private */
    public static final Boolean DEBUG = false;
    private static final String TAG = "ArrayObjectAdapter";
    private final List<Object> mItems = new ArrayList();
    ListUpdateCallback mListUpdateCallback;
    final List<Object> mOldItems = new ArrayList();
    private List mUnmodifiableItems;

    public boolean isImmediateNotifySupported() {
        return true;
    }

    public ArrayObjectAdapter(PresenterSelector presenterSelector) {
        super(presenterSelector);
    }

    public ArrayObjectAdapter(Presenter presenter) {
        super(presenter);
    }

    public ArrayObjectAdapter() {
    }

    public int size() {
        return this.mItems.size();
    }

    public Object get(int i) {
        return this.mItems.get(i);
    }

    public int indexOf(Object obj) {
        return this.mItems.indexOf(obj);
    }

    public void notifyArrayItemRangeChanged(int i, int i2) {
        notifyItemRangeChanged(i, i2);
    }

    public void add(Object obj) {
        add(this.mItems.size(), obj);
    }

    public void add(int i, Object obj) {
        this.mItems.add(i, obj);
        notifyItemRangeInserted(i, 1);
    }

    public void addAll(int i, Collection collection) {
        int size = collection.size();
        if (size != 0) {
            this.mItems.addAll(i, collection);
            notifyItemRangeInserted(i, size);
        }
    }

    public boolean remove(Object obj) {
        int indexOf = this.mItems.indexOf(obj);
        if (indexOf >= 0) {
            this.mItems.remove(indexOf);
            notifyItemRangeRemoved(indexOf, 1);
        }
        if (indexOf >= 0) {
            return true;
        }
        return false;
    }

    public void move(int i, int i2) {
        if (i != i2) {
            this.mItems.add(i2, this.mItems.remove(i));
            notifyItemMoved(i, i2);
        }
    }

    public void replace(int i, Object obj) {
        this.mItems.set(i, obj);
        notifyItemRangeChanged(i, 1);
    }

    public int removeItems(int i, int i2) {
        int min = Math.min(i2, this.mItems.size() - i);
        if (min <= 0) {
            return 0;
        }
        for (int i3 = 0; i3 < min; i3++) {
            this.mItems.remove(i);
        }
        notifyItemRangeRemoved(i, min);
        return min;
    }

    public void clear() {
        int size = this.mItems.size();
        if (size != 0) {
            this.mItems.clear();
            notifyItemRangeRemoved(0, size);
        }
    }

    public <E> List<E> unmodifiableList() {
        if (this.mUnmodifiableItems == null) {
            this.mUnmodifiableItems = Collections.unmodifiableList(this.mItems);
        }
        return this.mUnmodifiableItems;
    }

    public void setItems(final List list, final DiffCallback diffCallback) {
        if (diffCallback == null) {
            this.mItems.clear();
            this.mItems.addAll(list);
            notifyChanged();
            return;
        }
        this.mOldItems.clear();
        this.mOldItems.addAll(this.mItems);
        DiffUtil.DiffResult calculateDiff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            public int getOldListSize() {
                return ArrayObjectAdapter.this.mOldItems.size();
            }

            public int getNewListSize() {
                return list.size();
            }

            public boolean areItemsTheSame(int i, int i2) {
                return diffCallback.areItemsTheSame(ArrayObjectAdapter.this.mOldItems.get(i), list.get(i2));
            }

            public boolean areContentsTheSame(int i, int i2) {
                return diffCallback.areContentsTheSame(ArrayObjectAdapter.this.mOldItems.get(i), list.get(i2));
            }

            public Object getChangePayload(int i, int i2) {
                return diffCallback.getChangePayload(ArrayObjectAdapter.this.mOldItems.get(i), list.get(i2));
            }
        });
        this.mItems.clear();
        this.mItems.addAll(list);
        if (this.mListUpdateCallback == null) {
            this.mListUpdateCallback = new ListUpdateCallback() {
                public void onInserted(int i, int i2) {
                    if (ArrayObjectAdapter.DEBUG.booleanValue()) {
                        Log.d(ArrayObjectAdapter.TAG, "onInserted");
                    }
                    ArrayObjectAdapter.this.notifyItemRangeInserted(i, i2);
                }

                public void onRemoved(int i, int i2) {
                    if (ArrayObjectAdapter.DEBUG.booleanValue()) {
                        Log.d(ArrayObjectAdapter.TAG, "onRemoved");
                    }
                    ArrayObjectAdapter.this.notifyItemRangeRemoved(i, i2);
                }

                public void onMoved(int i, int i2) {
                    if (ArrayObjectAdapter.DEBUG.booleanValue()) {
                        Log.d(ArrayObjectAdapter.TAG, "onMoved");
                    }
                    ArrayObjectAdapter.this.notifyItemMoved(i, i2);
                }

                public void onChanged(int i, int i2, Object obj) {
                    if (ArrayObjectAdapter.DEBUG.booleanValue()) {
                        Log.d(ArrayObjectAdapter.TAG, "onChanged");
                    }
                    ArrayObjectAdapter.this.notifyItemRangeChanged(i, i2, obj);
                }
            };
        }
        calculateDiff.dispatchUpdatesTo(this.mListUpdateCallback);
        this.mOldItems.clear();
    }
}
