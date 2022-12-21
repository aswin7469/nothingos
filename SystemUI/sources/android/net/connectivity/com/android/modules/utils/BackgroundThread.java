package android.net.connectivity.com.android.modules.utils;

import android.os.Handler;
import android.os.HandlerThread;
import java.util.concurrent.Executor;

public final class BackgroundThread extends HandlerThread {
    private static Handler sHandler;
    private static HandlerExecutor sHandlerExecutor;
    private static BackgroundThread sInstance;
    private static final Object sLock = new Object();

    private BackgroundThread() {
        super(BackgroundThread.class.getName(), 10);
    }

    private static void ensureThreadLocked() {
        if (sInstance == null) {
            BackgroundThread backgroundThread = new BackgroundThread();
            sInstance = backgroundThread;
            backgroundThread.start();
            Handler handler = new Handler(sInstance.getLooper());
            sHandler = handler;
            sHandlerExecutor = new HandlerExecutor(handler);
        }
    }

    public static BackgroundThread get() {
        BackgroundThread backgroundThread;
        synchronized (sLock) {
            ensureThreadLocked();
            backgroundThread = sInstance;
        }
        return backgroundThread;
    }

    public static Handler getHandler() {
        Handler handler;
        synchronized (sLock) {
            ensureThreadLocked();
            handler = sHandler;
        }
        return handler;
    }

    public static Executor getExecutor() {
        HandlerExecutor handlerExecutor;
        synchronized (sLock) {
            ensureThreadLocked();
            handlerExecutor = sHandlerExecutor;
        }
        return handlerExecutor;
    }
}
