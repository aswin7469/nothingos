package android.app;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import com.android.internal.util.ExponentiallyBucketedHistogram;
import java.util.Iterator;
import java.util.LinkedList;
/* loaded from: classes.dex */
public class QueuedWork {
    private static final boolean DEBUG = false;
    private static final long DELAY = 100;
    private static final long MAX_WAIT_TIME_MILLIS = 512;
    private static final String LOG_TAG = QueuedWork.class.getSimpleName();
    private static final Object sLock = new Object();
    private static Object sProcessingWork = new Object();
    private static final LinkedList<Runnable> sFinishers = new LinkedList<>();
    private static Handler sHandler = null;
    private static LinkedList<Runnable> sWork = new LinkedList<>();
    private static boolean sCanDelay = true;
    private static final ExponentiallyBucketedHistogram mWaitTimes = new ExponentiallyBucketedHistogram(16);
    private static int mNumWaits = 0;

    private static Handler getHandler() {
        Handler handler;
        synchronized (sLock) {
            if (sHandler == null) {
                HandlerThread handlerThread = new HandlerThread("queued-work-looper", -2);
                handlerThread.start();
                sHandler = new QueuedWorkHandler(handlerThread.getLooper());
            }
            handler = sHandler;
        }
        return handler;
    }

    public static void addFinisher(Runnable finisher) {
        synchronized (sLock) {
            sFinishers.add(finisher);
        }
    }

    public static void removeFinisher(Runnable finisher) {
        synchronized (sLock) {
            sFinishers.remove(finisher);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r5v0, types: [int, boolean] */
    public static void waitToFinish() {
        ?? r5;
        Object obj;
        Runnable finisher;
        long startTime = System.currentTimeMillis();
        Handler handler = getHandler();
        synchronized (sLock) {
            r5 = 1;
            if (handler.hasMessages(r5 == true ? 1 : 0)) {
                handler.removeMessages(r5);
            }
            boolean z = false;
        }
        StrictMode.ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            processPendingWork();
            while (true) {
                try {
                    obj = sLock;
                    synchronized (obj) {
                        finisher = sFinishers.poll();
                    }
                    if (finisher == null) {
                        break;
                    }
                    finisher.run();
                } finally {
                    sCanDelay = r5;
                }
            }
            synchronized (obj) {
                long waitTime = System.currentTimeMillis() - startTime;
                if (waitTime > 0 || 0 != 0) {
                    ExponentiallyBucketedHistogram exponentiallyBucketedHistogram = mWaitTimes;
                    exponentiallyBucketedHistogram.add(Long.valueOf(waitTime).intValue());
                    int i = mNumWaits + r5;
                    mNumWaits = i;
                    if (i % 1024 == 0 || waitTime > 512) {
                        exponentiallyBucketedHistogram.log(LOG_TAG, "waited: ");
                    }
                }
            }
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public static void queue(Runnable work, boolean shouldDelay) {
        Handler handler = getHandler();
        synchronized (sLock) {
            sWork.add(work);
            if (shouldDelay && sCanDelay) {
                handler.sendEmptyMessageDelayed(1, DELAY);
            } else {
                handler.sendEmptyMessage(1);
            }
        }
    }

    public static boolean hasPendingWork() {
        boolean z;
        synchronized (sLock) {
            z = !sWork.isEmpty();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void processPendingWork() {
        LinkedList<Runnable> work;
        synchronized (sProcessingWork) {
            synchronized (sLock) {
                work = sWork;
                sWork = new LinkedList<>();
                getHandler().removeMessages(1);
            }
            if (work.size() > 0) {
                Iterator<Runnable> it = work.iterator();
                while (it.hasNext()) {
                    Runnable w = it.next();
                    w.run();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class QueuedWorkHandler extends Handler {
        static final int MSG_RUN = 1;

        QueuedWorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                QueuedWork.processPendingWork();
            }
        }
    }
}
