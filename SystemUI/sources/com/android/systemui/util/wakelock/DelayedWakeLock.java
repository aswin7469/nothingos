package com.android.systemui.util.wakelock;

import android.content.Context;
import android.os.Handler;
import javax.inject.Inject;

public class DelayedWakeLock implements WakeLock {
    private static final long RELEASE_DELAY_MS = 100;
    private static final String TO_STRING_PREFIX = "[DelayedWakeLock] ";
    private final Handler mHandler;
    private final WakeLock mInner;

    public DelayedWakeLock(Handler handler, WakeLock wakeLock) {
        this.mHandler = handler;
        this.mInner = wakeLock;
    }

    public void acquire(String str) {
        this.mInner.acquire(str);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$release$0$com-android-systemui-util-wakelock-DelayedWakeLock */
    public /* synthetic */ void mo47191x7da1638c(String str) {
        this.mInner.release(str);
    }

    public void release(String str) {
        this.mHandler.postDelayed(new DelayedWakeLock$$ExternalSyntheticLambda0(this, str), 100);
    }

    public Runnable wrap(Runnable runnable) {
        return WakeLock.wrapImpl(this, runnable);
    }

    public String toString() {
        return TO_STRING_PREFIX + this.mInner;
    }

    public static class Builder {
        private final Context mContext;
        private Handler mHandler;
        private String mTag;

        @Inject
        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTag(String str) {
            this.mTag = str;
            return this;
        }

        public Builder setHandler(Handler handler) {
            this.mHandler = handler;
            return this;
        }

        public DelayedWakeLock build() {
            return new DelayedWakeLock(this.mHandler, WakeLock.createPartial(this.mContext, this.mTag));
        }
    }
}
