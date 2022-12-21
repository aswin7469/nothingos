package androidx.leanback.widget;

import android.graphics.drawable.Drawable;
import android.view.View;

final class ForegroundHelper {
    static boolean supportsForeground() {
        return true;
    }

    static Drawable getForeground(View view) {
        return view.getForeground();
    }

    static void setForeground(View view, Drawable drawable) {
        view.setForeground(drawable);
    }

    private ForegroundHelper() {
    }
}
