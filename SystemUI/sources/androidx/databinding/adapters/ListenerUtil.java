package androidx.databinding.adapters;

import android.util.SparseArray;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class ListenerUtil {
    private static final SparseArray<WeakHashMap<View, WeakReference<?>>> sListeners = new SparseArray<>();

    public static <T> T trackListener(View view, T t, int i) {
        T tag = view.getTag(i);
        view.setTag(i, t);
        return tag;
    }

    public static <T> T getListener(View view, int i) {
        return view.getTag(i);
    }
}
