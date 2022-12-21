package androidx.leanback.widget;

import android.view.View;

final class ShadowHelper {
    static boolean supportsDynamicShadow() {
        return true;
    }

    private ShadowHelper() {
    }

    static Object addDynamicShadow(View view, float f, float f2, int i) {
        return ShadowHelperApi21.addDynamicShadow(view, f, f2, i);
    }

    static void setShadowFocusLevel(Object obj, float f) {
        ShadowHelperApi21.setShadowFocusLevel(obj, f);
    }
}
