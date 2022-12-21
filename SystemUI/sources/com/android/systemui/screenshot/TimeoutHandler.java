package com.android.systemui.screenshot;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.accessibility.AccessibilityManager;
import javax.inject.Inject;

public class TimeoutHandler extends Handler {
    private static final int DEFAULT_TIMEOUT_MILLIS = 6000;
    private static final int MESSAGE_CORNER_TIMEOUT = 2;
    private static final String TAG = "TimeoutHandler";
    private final Context mContext;
    private int mDefaultTimeout = 6000;
    private Runnable mOnTimeout;

    static /* synthetic */ void lambda$new$0() {
    }

    @Inject
    public TimeoutHandler(Context context) {
        super(Looper.getMainLooper());
        this.mContext = context;
        this.mOnTimeout = new TimeoutHandler$$ExternalSyntheticLambda0();
    }

    public void setOnTimeoutRunnable(Runnable runnable) {
        this.mOnTimeout = runnable;
    }

    public void handleMessage(Message message) {
        if (message.what == 2) {
            this.mOnTimeout.run();
        }
    }

    public void setDefaultTimeoutMillis(int i) {
        this.mDefaultTimeout = i;
    }

    /* access modifiers changed from: package-private */
    public int getDefaultTimeoutMillis() {
        return this.mDefaultTimeout;
    }

    public void cancelTimeout() {
        removeMessages(2);
    }

    public void resetTimeout() {
        cancelTimeout();
        sendMessageDelayed(obtainMessage(2), (long) ((AccessibilityManager) this.mContext.getSystemService("accessibility")).getRecommendedTimeoutMillis(this.mDefaultTimeout, 4));
    }
}
