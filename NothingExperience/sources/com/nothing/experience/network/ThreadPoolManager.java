package com.nothing.experience.network;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager {
    private static ThreadPoolManager httpInstance = new ThreadPoolManager();
    private static ThreadPoolManager insertInstance = new ThreadPoolManager();
    private static ThreadPoolManager queryInstance = new ThreadPoolManager();
    /* access modifiers changed from: private */
    public LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
        public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
            try {
                ThreadPoolManager.this.queue.put(runnable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable runnable = new Runnable() {
        public void run() {
            while (true) {
                Runnable runnable = null;
                try {
                    runnable = (Runnable) ThreadPoolManager.this.queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (runnable != null) {
                    ThreadPoolManager.this.threadPoolExecutor.execute(runnable);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public ThreadPoolExecutor threadPoolExecutor;

    private ThreadPoolManager() {
        ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(4, 20, 15, TimeUnit.SECONDS, new ArrayBlockingQueue(4), this.rejectedExecutionHandler);
        this.threadPoolExecutor = threadPoolExecutor2;
        threadPoolExecutor2.execute(this.runnable);
    }

    public static void release() {
        ThreadPoolManager threadPoolManager = httpInstance;
        if (threadPoolManager != null) {
            threadPoolManager.queue.clear();
        }
        ThreadPoolManager threadPoolManager2 = queryInstance;
        if (threadPoolManager2 != null) {
            threadPoolManager2.queue.clear();
        }
        ThreadPoolManager threadPoolManager3 = insertInstance;
        if (threadPoolManager3 != null) {
            threadPoolManager3.queue.clear();
        }
    }

    public static ThreadPoolManager getHttpInstance() {
        return httpInstance;
    }

    public static ThreadPoolManager getInsertInstance() {
        return insertInstance;
    }

    public static ThreadPoolManager getQueryInstance() {
        return queryInstance;
    }

    public void execute(Runnable runnable2) {
        if (runnable2 != null) {
            try {
                this.queue.put(runnable2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
