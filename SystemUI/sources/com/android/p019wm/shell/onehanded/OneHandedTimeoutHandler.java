package com.android.p019wm.shell.onehanded;

import com.android.p019wm.shell.common.ShellExecutor;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/* renamed from: com.android.wm.shell.onehanded.OneHandedTimeoutHandler */
public class OneHandedTimeoutHandler {
    private static final String TAG = "OneHandedTimeoutHandler";
    private List<TimeoutListener> mListeners = new ArrayList();
    private final ShellExecutor mMainExecutor;
    private int mTimeout = 8;
    private long mTimeoutMs = TimeUnit.SECONDS.toMillis((long) this.mTimeout);
    private final Runnable mTimeoutRunnable = new OneHandedTimeoutHandler$$ExternalSyntheticLambda0(this);

    /* renamed from: com.android.wm.shell.onehanded.OneHandedTimeoutHandler$TimeoutListener */
    public interface TimeoutListener {
        void onTimeout(int i);
    }

    public int getTimeout() {
        return this.mTimeout;
    }

    public OneHandedTimeoutHandler(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    public void setTimeout(int i) {
        this.mTimeout = i;
        this.mTimeoutMs = TimeUnit.SECONDS.toMillis((long) this.mTimeout);
        resetTimer();
    }

    public void removeTimer() {
        this.mMainExecutor.removeCallbacks(this.mTimeoutRunnable);
    }

    public void resetTimer() {
        removeTimer();
        int i = this.mTimeout;
        if (i != 0 && i != 0) {
            this.mMainExecutor.executeDelayed(this.mTimeoutRunnable, this.mTimeoutMs);
        }
    }

    public void registerTimeoutListener(TimeoutListener timeoutListener) {
        this.mListeners.add(timeoutListener);
    }

    /* access modifiers changed from: package-private */
    public boolean hasScheduledTimeout() {
        return this.mMainExecutor.hasCallback(this.mTimeoutRunnable);
    }

    /* access modifiers changed from: private */
    public void onStop() {
        for (int size = this.mListeners.size() - 1; size >= 0; size--) {
            this.mListeners.get(size).onTimeout(this.mTimeout);
        }
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        printWriter.print("  sTimeout=");
        printWriter.println(this.mTimeout);
        printWriter.print("  sListeners=");
        printWriter.println((Object) this.mListeners);
    }
}
