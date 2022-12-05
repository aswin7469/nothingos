package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
/* loaded from: classes2.dex */
public class LibraryLoader {
    @SuppressLint({"StaticFieldLeak"})
    private static Context sAppContext;

    private static Context getContext() {
        if (sAppContext == null) {
            try {
                sAppContext = (Context) Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                throw new IllegalStateException("LibraryLoader not initialized. Call LibraryLoader.initialize() before using library classes.", e);
            }
        }
        return sAppContext;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void loadLibrary() {
        try {
            System.loadLibrary("pl_droidsonroids_gif");
        } catch (UnsatisfiedLinkError unused) {
            ReLinker.loadLibrary(getContext());
        }
    }
}
