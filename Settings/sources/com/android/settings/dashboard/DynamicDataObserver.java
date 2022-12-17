package com.android.settings.dashboard;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import com.android.settingslib.utils.ThreadUtils;
import java.util.concurrent.CountDownLatch;

public abstract class DynamicDataObserver extends ContentObserver {
    private CountDownLatch mCountDownLatch = new CountDownLatch(1);
    private boolean mUpdateDelegated;
    private Runnable mUpdateRunnable;

    public abstract Uri getUri();

    public abstract void onDataChanged();

    protected DynamicDataObserver() {
        super(new Handler(Looper.getMainLooper()));
        onDataChanged();
    }

    public synchronized void updateUi() {
        this.mUpdateDelegated = true;
        Runnable runnable = this.mUpdateRunnable;
        if (runnable != null) {
            runnable.run();
        }
    }

    public CountDownLatch getCountDownLatch() {
        return this.mCountDownLatch;
    }

    public void onChange(boolean z) {
        onDataChanged();
    }

    /* access modifiers changed from: protected */
    public synchronized void post(Runnable runnable) {
        if (this.mUpdateDelegated) {
            ThreadUtils.postOnMainThread(runnable);
        } else {
            this.mUpdateRunnable = runnable;
            this.mCountDownLatch.countDown();
        }
    }
}
