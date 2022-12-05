package com.nt.settings.glyphs.utils;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/* loaded from: classes2.dex */
public class ThreadUtils {
    private static volatile Handler sMainThreadHandler;
    private static volatile ExecutorService sThreadExecutor;

    public static Handler getUiThreadHandler() {
        if (sMainThreadHandler == null) {
            sMainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return sMainThreadHandler;
    }

    public static Future postOnBackgroundThread(Runnable runnable) {
        return getThreadExecutor().submit(runnable);
    }

    public static void postOnMainThread(Runnable runnable) {
        getUiThreadHandler().post(runnable);
    }

    private static synchronized ExecutorService getThreadExecutor() {
        ExecutorService executorService;
        synchronized (ThreadUtils.class) {
            if (sThreadExecutor == null) {
                sThreadExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            }
            executorService = sThreadExecutor;
        }
        return executorService;
    }
}
