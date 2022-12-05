package androidx.asynclayoutinflater.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.util.Pools$SynchronizedPool;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
/* loaded from: classes.dex */
public final class AsyncLayoutInflater {
    LayoutInflater mInflater;
    private Handler.Callback mHandlerCallback = new Handler.Callback() { // from class: androidx.asynclayoutinflater.view.AsyncLayoutInflater.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message msg) {
            InflateRequest inflateRequest = (InflateRequest) msg.obj;
            if (inflateRequest.view == null) {
                inflateRequest.view = AsyncLayoutInflater.this.mInflater.inflate(inflateRequest.resid, inflateRequest.parent, false);
            }
            inflateRequest.callback.onInflateFinished(inflateRequest.view, inflateRequest.resid, inflateRequest.parent);
            AsyncLayoutInflater.this.mInflateThread.releaseRequest(inflateRequest);
            return true;
        }
    };
    Handler mHandler = new Handler(this.mHandlerCallback);
    InflateThread mInflateThread = InflateThread.getInstance();

    /* loaded from: classes.dex */
    public interface OnInflateFinishedListener {
        void onInflateFinished(View view, int resid, ViewGroup parent);
    }

    public AsyncLayoutInflater(Context context) {
        this.mInflater = new BasicInflater(context);
    }

    public void inflate(int resid, ViewGroup parent, OnInflateFinishedListener callback) {
        Objects.requireNonNull(callback, "callback argument may not be null!");
        InflateRequest obtainRequest = this.mInflateThread.obtainRequest();
        obtainRequest.inflater = this;
        obtainRequest.resid = resid;
        obtainRequest.parent = parent;
        obtainRequest.callback = callback;
        this.mInflateThread.enqueue(obtainRequest);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class InflateRequest {
        OnInflateFinishedListener callback;
        AsyncLayoutInflater inflater;
        ViewGroup parent;
        int resid;
        View view;

        InflateRequest() {
        }
    }

    /* loaded from: classes.dex */
    private static class BasicInflater extends LayoutInflater {
        private static final String[] sClassPrefixList = {"android.widget.", "android.webkit.", "android.app."};

        BasicInflater(Context context) {
            super(context);
        }

        @Override // android.view.LayoutInflater
        public LayoutInflater cloneInContext(Context newContext) {
            return new BasicInflater(newContext);
        }

        @Override // android.view.LayoutInflater
        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            View createView;
            for (String str : sClassPrefixList) {
                try {
                    createView = createView(name, str, attrs);
                } catch (ClassNotFoundException unused) {
                }
                if (createView != null) {
                    return createView;
                }
            }
            return super.onCreateView(name, attrs);
        }
    }

    /* loaded from: classes.dex */
    private static class InflateThread extends Thread {
        private static final InflateThread sInstance;
        private ArrayBlockingQueue<InflateRequest> mQueue = new ArrayBlockingQueue<>(10);
        private Pools$SynchronizedPool<InflateRequest> mRequestPool = new Pools$SynchronizedPool<>(10);

        private InflateThread() {
        }

        static {
            InflateThread inflateThread = new InflateThread();
            sInstance = inflateThread;
            inflateThread.start();
        }

        public static InflateThread getInstance() {
            return sInstance;
        }

        public void runInner() {
            try {
                InflateRequest take = this.mQueue.take();
                try {
                    take.view = take.inflater.mInflater.inflate(take.resid, take.parent, false);
                } catch (RuntimeException e) {
                    Log.w("AsyncLayoutInflater", "Failed to inflate resource in the background! Retrying on the UI thread", e);
                }
                Message.obtain(take.inflater.mHandler, 0, take).sendToTarget();
            } catch (InterruptedException e2) {
                Log.w("AsyncLayoutInflater", e2);
            }
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                runInner();
            }
        }

        public InflateRequest obtainRequest() {
            InflateRequest acquire = this.mRequestPool.acquire();
            return acquire == null ? new InflateRequest() : acquire;
        }

        public void releaseRequest(InflateRequest obj) {
            obj.callback = null;
            obj.inflater = null;
            obj.parent = null;
            obj.resid = 0;
            obj.view = null;
            this.mRequestPool.release(obj);
        }

        public void enqueue(InflateRequest request) {
            try {
                this.mQueue.put(request);
            } catch (InterruptedException e) {
                throw new RuntimeException("Failed to enqueue async inflate request", e);
            }
        }
    }
}
