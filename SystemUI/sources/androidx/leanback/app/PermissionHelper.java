package androidx.leanback.app;

import android.app.Fragment;

public class PermissionHelper {
    public static void requestPermissions(Fragment fragment, String[] strArr, int i) {
        fragment.requestPermissions(strArr, i);
    }

    private PermissionHelper() {
    }
}
