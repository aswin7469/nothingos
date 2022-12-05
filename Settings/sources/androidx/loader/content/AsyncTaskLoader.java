package androidx.loader.content;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateUtils;
import androidx.core.os.OperationCanceledException;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public abstract class AsyncTaskLoader<D> extends Loader<D> {
    private volatile AsyncTaskLoader<D>.LoadTask mCancellingTask;
    private Executor mExecutor;
    private Handler mHandler;
    private long mLastLoadCompleteTime = -10000;
    private volatile AsyncTaskLoader<D>.LoadTask mTask;
    private long mUpdateThrottle;

    public void cancelLoadInBackground() {
    }

    /* renamed from: loadInBackground */
    public abstract D mo611loadInBackground();

    public void onCanceled(D d) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public final class LoadTask extends ModernAsyncTask<D> implements Runnable {
        boolean waiting;

        LoadTask() {
        }

        @Override // androidx.loader.content.ModernAsyncTask
        protected D doInBackground() {
            try {
                return (D) AsyncTaskLoader.this.onLoadInBackground();
            } catch (OperationCanceledException e) {
                if (!this.isCancelled()) {
                    throw e;
                }
                return null;
            }
        }

        @Override // androidx.loader.content.ModernAsyncTask
        protected void onPostExecute(D d) {
            AsyncTaskLoader.this.dispatchOnLoadComplete(this, d);
        }

        @Override // androidx.loader.content.ModernAsyncTask
        protected void onCancelled(D d) {
            AsyncTaskLoader.this.dispatchOnCancelled(this, d);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.waiting = false;
            AsyncTaskLoader.this.executePendingTask();
        }
    }

    public AsyncTaskLoader(Context context) {
        super(context);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.loader.content.Loader
    public void onForceLoad() {
        super.onForceLoad();
        cancelLoad();
        this.mTask = new LoadTask();
        executePendingTask();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.loader.content.Loader
    public boolean onCancelLoad() {
        if (this.mTask != null) {
            if (!isStarted()) {
                onContentChanged();
            }
            if (this.mCancellingTask != null) {
                if (this.mTask.waiting) {
                    this.mTask.waiting = false;
                    this.mHandler.removeCallbacks(this.mTask);
                }
                this.mTask = null;
                return false;
            } else if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks(this.mTask);
                this.mTask = null;
                return false;
            } else {
                boolean cancel = this.mTask.cancel(false);
                if (cancel) {
                    this.mCancellingTask = this.mTask;
                    cancelLoadInBackground();
                }
                this.mTask = null;
                return cancel;
            }
        }
        return false;
    }

    void executePendingTask() {
        if (this.mCancellingTask != null || this.mTask == null) {
            return;
        }
        if (this.mTask.waiting) {
            this.mTask.waiting = false;
            this.mHandler.removeCallbacks(this.mTask);
        }
        if (this.mUpdateThrottle > 0 && SystemClock.uptimeMillis() < this.mLastLoadCompleteTime + this.mUpdateThrottle) {
            this.mTask.waiting = true;
            this.mHandler.postAtTime(this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
            return;
        }
        if (this.mExecutor == null) {
            this.mExecutor = getExecutor();
        }
        this.mTask.executeOnExecutor(this.mExecutor);
    }

    void dispatchOnCancelled(AsyncTaskLoader<D>.LoadTask loadTask, D d) {
        onCanceled(d);
        if (this.mCancellingTask == loadTask) {
            rollbackContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mCancellingTask = null;
            deliverCancellation();
            executePendingTask();
        }
    }

    void dispatchOnLoadComplete(AsyncTaskLoader<D>.LoadTask loadTask, D d) {
        if (this.mTask != loadTask) {
            dispatchOnCancelled(loadTask, d);
        } else if (isAbandoned()) {
            onCanceled(d);
        } else {
            commitContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mTask = null;
            deliverResult(d);
        }
    }

    protected D onLoadInBackground() {
        return mo611loadInBackground();
    }

    protected Executor getExecutor() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    @Override // androidx.loader.content.Loader
    @Deprecated
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        TimeUnit timeUnit;
        String str2;
        super.dump(str, fileDescriptor, printWriter, strArr);
        if (this.mTask != null) {
            printWriter.print(str);
            printWriter.print("mTask=");
            printWriter.print(this.mTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mTask.waiting);
        }
        if (this.mCancellingTask != null) {
            printWriter.print(str);
            printWriter.print("mCancellingTask=");
            printWriter.print(this.mCancellingTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mCancellingTask.waiting);
        }
        if (this.mUpdateThrottle != 0) {
            printWriter.print(str);
            printWriter.print("mUpdateThrottle=");
            printWriter.print(DateUtils.formatElapsedTime(TimeUnit.MILLISECONDS.toSeconds(this.mUpdateThrottle)));
            printWriter.print(" mLastLoadCompleteTime=");
            if (this.mLastLoadCompleteTime == -10000) {
                str2 = "--";
            } else {
                str2 = "-" + DateUtils.formatElapsedTime(timeUnit.toSeconds(SystemClock.uptimeMillis() - this.mLastLoadCompleteTime));
            }
            printWriter.print(str2);
            printWriter.println();
        }
    }
}
