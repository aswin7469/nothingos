package com.android.systemui.util.service;

import android.util.Log;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.service.ObservableServiceConnection;
import com.android.systemui.util.service.Observer;
import com.android.systemui.util.time.SystemClock;
import javax.inject.Inject;
import javax.inject.Named;

public class PersistentConnectionManager<T> {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "PersistentConnManager";
    private final int mBaseReconnectDelayMs;
    private final Runnable mConnectRunnable = new Runnable() {
        public void run() {
            Runnable unused = PersistentConnectionManager.this.mCurrentReconnectCancelable = null;
            PersistentConnectionManager.this.mConnection.bind();
        }
    };
    /* access modifiers changed from: private */
    public final ObservableServiceConnection<T> mConnection;
    private final ObservableServiceConnection.Callback mConnectionCallback = new ObservableServiceConnection.Callback() {
        private long mStartTime;

        public void onConnected(ObservableServiceConnection observableServiceConnection, Object obj) {
            this.mStartTime = PersistentConnectionManager.this.mSystemClock.currentTimeMillis();
        }

        public void onDisconnected(ObservableServiceConnection observableServiceConnection, int i) {
            if (PersistentConnectionManager.this.mSystemClock.currentTimeMillis() - this.mStartTime > ((long) PersistentConnectionManager.this.mMinConnectionDuration)) {
                PersistentConnectionManager.this.mo47132xf835ab20();
            } else {
                PersistentConnectionManager.this.scheduleConnectionAttempt();
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable mCurrentReconnectCancelable;
    private final DelayableExecutor mMainExecutor;
    private final int mMaxReconnectAttempts;
    /* access modifiers changed from: private */
    public final int mMinConnectionDuration;
    private final Observer mObserver;
    private final Observer.Callback mObserverCallback = new PersistentConnectionManager$$ExternalSyntheticLambda0(this);
    private int mReconnectAttempts = 0;
    /* access modifiers changed from: private */
    public final SystemClock mSystemClock;

    @Inject
    public PersistentConnectionManager(SystemClock systemClock, DelayableExecutor delayableExecutor, @Named("service_connection") ObservableServiceConnection<T> observableServiceConnection, @Named("max_reconnect_attempts") int i, @Named("base_reconnect_attempts") int i2, @Named("min_connection_duration_ms") int i3, @Named("observer") Observer observer) {
        this.mSystemClock = systemClock;
        this.mMainExecutor = delayableExecutor;
        this.mConnection = observableServiceConnection;
        this.mObserver = observer;
        this.mMaxReconnectAttempts = i;
        this.mBaseReconnectDelayMs = i2;
        this.mMinConnectionDuration = i3;
    }

    public void start() {
        this.mConnection.addCallback(this.mConnectionCallback);
        this.mObserver.addCallback(this.mObserverCallback);
        mo47132xf835ab20();
    }

    public void stop() {
        this.mConnection.removeCallback(this.mConnectionCallback);
        this.mObserver.removeCallback(this.mObserverCallback);
        this.mConnection.unbind();
    }

    /* access modifiers changed from: private */
    /* renamed from: initiateConnectionAttempt */
    public void mo47132xf835ab20() {
        this.mReconnectAttempts = 0;
        this.mConnection.bind();
    }

    /* access modifiers changed from: private */
    public void scheduleConnectionAttempt() {
        Runnable runnable = this.mCurrentReconnectCancelable;
        if (runnable != null) {
            runnable.run();
            this.mCurrentReconnectCancelable = null;
        }
        int i = this.mReconnectAttempts;
        if (i < this.mMaxReconnectAttempts) {
            long scalb = (long) Math.scalb((float) this.mBaseReconnectDelayMs, i);
            if (DEBUG) {
                Log.d(TAG, "scheduling connection attempt in " + scalb + "milliseconds");
            }
            this.mCurrentReconnectCancelable = this.mMainExecutor.executeDelayed(this.mConnectRunnable, scalb);
            this.mReconnectAttempts++;
        } else if (DEBUG) {
            Log.d(TAG, "exceeded max connection attempts.");
        }
    }
}
