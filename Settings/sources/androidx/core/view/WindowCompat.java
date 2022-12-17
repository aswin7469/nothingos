package androidx.core.view;

import android.view.Window;

public final class WindowCompat {
    public static void setDecorFitsSystemWindows(Window window, boolean z) {
        Api30Impl.setDecorFitsSystemWindows(window, z);
    }

    static class Api30Impl {
        static void setDecorFitsSystemWindows(Window window, boolean z) {
            window.setDecorFitsSystemWindows(z);
        }
    }
}
