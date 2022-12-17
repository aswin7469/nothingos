package com.android.settings.network.helper;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import com.android.settingslib.utils.ThreadUtils;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

public class LifecycleCallbackConverter<T> extends LifecycleCallbackAdapter {
    private final AtomicLong mNumberOfActiveStatusChange = new AtomicLong();
    private final Consumer<T> mResultCallback;
    private final Thread mUiThread = Thread.currentThread();

    private static final boolean isActiveStatus(long j) {
        return (j & 1) != 0;
    }

    public /* bridge */ /* synthetic */ void close() {
        super.close();
    }

    public /* bridge */ /* synthetic */ Lifecycle getLifecycle() {
        return super.getLifecycle();
    }

    public /* bridge */ /* synthetic */ void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        super.onStateChanged(lifecycleOwner, event);
    }

    public LifecycleCallbackConverter(Lifecycle lifecycle, Consumer<T> consumer) {
        super(lifecycle);
        this.mResultCallback = consumer;
    }

    public void postResult(T t) {
        long j = this.mNumberOfActiveStatusChange.get();
        if (Thread.currentThread() == this.mUiThread) {
            lambda$postResultToUiThread$0(j, t);
        } else {
            postResultToUiThread(j, t);
        }
    }

    /* access modifiers changed from: protected */
    public void postResultToUiThread(long j, T t) {
        ThreadUtils.postOnMainThread(new LifecycleCallbackConverter$$ExternalSyntheticLambda0(this, j, t));
    }

    /* access modifiers changed from: protected */
    /* renamed from: dispatchExtResult */
    public void lambda$postResultToUiThread$0(long j, T t) {
        if (isActiveStatus(j) && j == this.mNumberOfActiveStatusChange.get() && getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            this.mResultCallback.accept(t);
        }
    }

    public boolean isCallbackActive() {
        return isActiveStatus(this.mNumberOfActiveStatusChange.get());
    }

    public void setCallbackActive(boolean z) {
        if (isCallbackActive() != z) {
            this.mNumberOfActiveStatusChange.getAndIncrement();
        }
    }
}
