package androidx.core.p004os;

import android.os.Handler;
import android.os.Looper;

/* renamed from: androidx.core.os.HandlerCompat */
public final class HandlerCompat {
    private static final String TAG = "HandlerCompat";

    public static Handler createAsync(Looper looper) {
        return Api28Impl.createAsync(looper);
    }

    public static Handler createAsync(Looper looper, Handler.Callback callback) {
        return Api28Impl.createAsync(looper, callback);
    }

    public static boolean postDelayed(Handler handler, Runnable runnable, Object obj, long j) {
        return Api28Impl.postDelayed(handler, runnable, obj, j);
    }

    public static boolean hasCallbacks(Handler handler, Runnable runnable) {
        return Api29Impl.hasCallbacks(handler, runnable);
    }

    private HandlerCompat() {
    }

    /* renamed from: androidx.core.os.HandlerCompat$Api29Impl */
    private static class Api29Impl {
        private Api29Impl() {
        }

        public static boolean hasCallbacks(Handler handler, Runnable runnable) {
            return handler.hasCallbacks(runnable);
        }
    }

    /* renamed from: androidx.core.os.HandlerCompat$Api28Impl */
    private static class Api28Impl {
        private Api28Impl() {
        }

        public static Handler createAsync(Looper looper) {
            return Handler.createAsync(looper);
        }

        public static Handler createAsync(Looper looper, Handler.Callback callback) {
            return Handler.createAsync(looper, callback);
        }

        public static boolean postDelayed(Handler handler, Runnable runnable, Object obj, long j) {
            return handler.postDelayed(runnable, obj, j);
        }
    }
}
