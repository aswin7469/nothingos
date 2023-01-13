package com.android.systemui.util.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.android.systemui.dagger.qualifiers.Main;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Inject;

public class ObservableServiceConnection<T> implements ServiceConnection {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    public static final int DISCONNECT_REASON_BINDING_DIED = 3;
    public static final int DISCONNECT_REASON_DISCONNECTED = 2;
    public static final int DISCONNECT_REASON_NULL_BINDING = 1;
    public static final int DISCONNECT_REASON_UNBIND = 4;
    private static final String TAG = "ObservableSvcConn";
    private boolean mBoundCalled;
    private final ArrayList<WeakReference<Callback<T>>> mCallbacks;
    private final Context mContext;
    private final Executor mExecutor;
    private final int mFlags = 1;
    private Optional<Integer> mLastDisconnectReason;
    private T mProxy;
    private final Intent mServiceIntent;
    private final ServiceTransformer<T> mTransformer;

    public interface Callback<T> {
        void onConnected(ObservableServiceConnection<T> observableServiceConnection, T t);

        void onDisconnected(ObservableServiceConnection<T> observableServiceConnection, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DisconnectReason {
    }

    public interface ServiceTransformer<T> {
        T convert(IBinder iBinder);
    }

    @Inject
    public ObservableServiceConnection(Context context, Intent intent, @Main Executor executor, ServiceTransformer<T> serviceTransformer) {
        this.mContext = context;
        this.mServiceIntent = intent;
        this.mExecutor = executor;
        this.mTransformer = serviceTransformer;
        this.mCallbacks = new ArrayList<>();
        this.mLastDisconnectReason = Optional.empty();
    }

    public boolean bind() {
        boolean bindService = this.mContext.bindService(this.mServiceIntent, this.mFlags, this.mExecutor, this);
        this.mBoundCalled = true;
        if (DEBUG) {
            Log.d(TAG, "bind. bound:" + bindService);
        }
        return bindService;
    }

    public void unbind() {
        if (this.mBoundCalled) {
            this.mBoundCalled = false;
            this.mContext.unbindService(this);
            onDisconnected(4);
        }
    }

    public void addCallback(Callback<T> callback) {
        if (DEBUG) {
            Log.d(TAG, "addCallback:" + callback);
        }
        this.mExecutor.execute(new ObservableServiceConnection$$ExternalSyntheticLambda0(this, callback));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addCallback$0$com-android-systemui-util-service-ObservableServiceConnection */
    public /* synthetic */ void mo47115xbbde41de(Callback callback) {
        Iterator<WeakReference<Callback<T>>> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            if (it.next().get() == callback) {
                return;
            }
        }
        this.mCallbacks.add(new WeakReference(callback));
        T t = this.mProxy;
        if (t != null) {
            callback.onConnected(this, t);
        } else if (this.mLastDisconnectReason.isPresent()) {
            callback.onDisconnected(this, this.mLastDisconnectReason.get().intValue());
        }
    }

    public void removeCallback(Callback callback) {
        if (DEBUG) {
            Log.d(TAG, "removeCallback:" + callback);
        }
        this.mExecutor.execute(new ObservableServiceConnection$$ExternalSyntheticLambda3(this, callback));
    }

    static /* synthetic */ boolean lambda$removeCallback$1(Callback callback, WeakReference weakReference) {
        return weakReference.get() == callback;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$removeCallback$2$com-android-systemui-util-service-ObservableServiceConnection */
    public /* synthetic */ void mo47118x2dd4631d(Callback callback) {
        this.mCallbacks.removeIf(new ObservableServiceConnection$$ExternalSyntheticLambda1(callback));
    }

    private void onDisconnected(int i) {
        if (DEBUG) {
            Log.d(TAG, "onDisconnected:" + i);
        }
        if (this.mBoundCalled) {
            this.mLastDisconnectReason = Optional.m1751of(Integer.valueOf(i));
            unbind();
            this.mProxy = null;
            applyToCallbacksLocked(new ObservableServiceConnection$$ExternalSyntheticLambda4(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDisconnected$3$com-android-systemui-util-service-ObservableServiceConnection */
    public /* synthetic */ void mo47116x9aee602f(Callback callback) {
        callback.onDisconnected(this, this.mLastDisconnectReason.get().intValue());
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (DEBUG) {
            Log.d(TAG, "onServiceConnected");
        }
        this.mProxy = this.mTransformer.convert(iBinder);
        applyToCallbacksLocked(new ObservableServiceConnection$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onServiceConnected$4$com-android-systemui-util-service-ObservableServiceConnection */
    public /* synthetic */ void mo47117x7eb59809(Callback callback) {
        callback.onConnected(this, this.mProxy);
    }

    private void applyToCallbacksLocked(Consumer<Callback<T>> consumer) {
        Iterator<WeakReference<Callback<T>>> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            Callback callback = (Callback) it.next().get();
            if (callback != null) {
                consumer.accept(callback);
            } else {
                it.remove();
            }
        }
    }

    public void onServiceDisconnected(ComponentName componentName) {
        onDisconnected(2);
    }

    public void onBindingDied(ComponentName componentName) {
        onDisconnected(2);
    }

    public void onNullBinding(ComponentName componentName) {
        onDisconnected(1);
    }
}
