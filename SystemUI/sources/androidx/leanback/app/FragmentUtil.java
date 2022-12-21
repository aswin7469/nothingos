package androidx.leanback.app;

import android.app.Fragment;
import android.content.Context;

class FragmentUtil {
    static Context getContext(Fragment fragment) {
        return fragment.getContext();
    }

    private FragmentUtil() {
    }
}
