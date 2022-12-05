package androidx.leanback.widget;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import androidx.collection.LruCache;
import java.util.Map;
/* loaded from: classes.dex */
class ViewsStateBundle {
    private LruCache<String, SparseArray<Parcelable>> mChildStates;
    private int mSavePolicy = 0;
    private int mLimitNumber = 100;

    public void clear() {
        LruCache<String, SparseArray<Parcelable>> lruCache = this.mChildStates;
        if (lruCache != null) {
            lruCache.evictAll();
        }
    }

    public void remove(int id) {
        LruCache<String, SparseArray<Parcelable>> lruCache = this.mChildStates;
        if (lruCache == null || lruCache.size() == 0) {
            return;
        }
        this.mChildStates.remove(getSaveStatesKey(id));
    }

    public final Bundle saveAsBundle() {
        LruCache<String, SparseArray<Parcelable>> lruCache = this.mChildStates;
        if (lruCache == null || lruCache.size() == 0) {
            return null;
        }
        Map<String, SparseArray<Parcelable>> snapshot = this.mChildStates.snapshot();
        Bundle bundle = new Bundle();
        for (Map.Entry<String, SparseArray<Parcelable>> entry : snapshot.entrySet()) {
            bundle.putSparseParcelableArray(entry.getKey(), entry.getValue());
        }
        return bundle;
    }

    public final void loadFromBundle(Bundle savedBundle) {
        LruCache<String, SparseArray<Parcelable>> lruCache = this.mChildStates;
        if (lruCache == null || savedBundle == null) {
            return;
        }
        lruCache.evictAll();
        for (String str : savedBundle.keySet()) {
            this.mChildStates.put(str, savedBundle.getSparseParcelableArray(str));
        }
    }

    public final void loadView(View view, int id) {
        if (this.mChildStates != null) {
            SparseArray<Parcelable> remove = this.mChildStates.remove(getSaveStatesKey(id));
            if (remove == null) {
                return;
            }
            view.restoreHierarchyState(remove);
        }
    }

    protected final void saveViewUnchecked(View view, int id) {
        if (this.mChildStates != null) {
            String saveStatesKey = getSaveStatesKey(id);
            SparseArray<Parcelable> sparseArray = new SparseArray<>();
            view.saveHierarchyState(sparseArray);
            this.mChildStates.put(saveStatesKey, sparseArray);
        }
    }

    public final Bundle saveOnScreenView(Bundle bundle, View view, int id) {
        if (this.mSavePolicy != 0) {
            String saveStatesKey = getSaveStatesKey(id);
            SparseArray<Parcelable> sparseArray = new SparseArray<>();
            view.saveHierarchyState(sparseArray);
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray(saveStatesKey, sparseArray);
        }
        return bundle;
    }

    public final void saveOffscreenView(View view, int id) {
        int i = this.mSavePolicy;
        if (i == 1) {
            remove(id);
        } else if (i != 2 && i != 3) {
        } else {
            saveViewUnchecked(view, id);
        }
    }

    static String getSaveStatesKey(int id) {
        return Integer.toString(id);
    }
}
