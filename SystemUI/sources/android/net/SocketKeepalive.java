package android.net;

import android.annotation.SystemApi;
import android.net.ISocketKeepaliveCallback;
import android.os.Binder;
import android.os.ParcelFileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.IOException;
import java.util.concurrent.Executor;

public abstract class SocketKeepalive implements AutoCloseable {
    public static final int BINDER_DIED = -10;
    public static final int DATA_RECEIVED = -2;
    public static final int ERROR_HARDWARE_ERROR = -31;
    public static final int ERROR_INSUFFICIENT_RESOURCES = -32;
    public static final int ERROR_INVALID_INTERVAL = -24;
    public static final int ERROR_INVALID_IP_ADDRESS = -21;
    public static final int ERROR_INVALID_LENGTH = -23;
    public static final int ERROR_INVALID_NETWORK = -20;
    public static final int ERROR_INVALID_PORT = -22;
    public static final int ERROR_INVALID_SOCKET = -25;
    @SystemApi
    public static final int ERROR_NO_SUCH_SLOT = -33;
    public static final int ERROR_SOCKET_NOT_IDLE = -26;
    public static final int ERROR_STOP_REASON_UNINITIALIZED = -27;
    public static final int ERROR_UNSUPPORTED = -30;
    public static final int MAX_INTERVAL_SEC = 3600;
    public static final int MIN_INTERVAL_SEC = 10;
    public static final int NO_KEEPALIVE = -1;
    @SystemApi
    public static final int SUCCESS = 0;
    static final String TAG = "SocketKeepalive";
    final ISocketKeepaliveCallback mCallback;
    final Executor mExecutor;
    final Network mNetwork;
    final ParcelFileDescriptor mPfd;
    final IConnectivityManager mService;
    Integer mSlot;

    public static class Callback {
        public void onDataReceived() {
        }

        public void onError(int i) {
        }

        public void onStarted() {
        }

        public void onStopped() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorCode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface KeepaliveEvent {
    }

    /* access modifiers changed from: package-private */
    public abstract void startImpl(int i);

    /* access modifiers changed from: package-private */
    public abstract void stopImpl();

    public static class ErrorCodeException extends Exception {
        public final int error;

        public ErrorCodeException(int i, Throwable th) {
            super(th);
            this.error = i;
        }

        public ErrorCodeException(int i) {
            this.error = i;
        }
    }

    public static class InvalidSocketException extends ErrorCodeException {
        public InvalidSocketException(int i, Throwable th) {
            super(i, th);
        }

        public InvalidSocketException(int i) {
            super(i);
        }
    }

    SocketKeepalive(IConnectivityManager iConnectivityManager, Network network, ParcelFileDescriptor parcelFileDescriptor, final Executor executor, final Callback callback) {
        this.mService = iConnectivityManager;
        this.mNetwork = network;
        this.mPfd = parcelFileDescriptor;
        this.mExecutor = executor;
        this.mCallback = new ISocketKeepaliveCallback.Stub() {
            public void onStarted(int i) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    SocketKeepalive.this.mExecutor.execute(new SocketKeepalive$1$$ExternalSyntheticLambda2(this, i, callback));
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onStarted$0$android-net-SocketKeepalive$1  reason: not valid java name */
            public /* synthetic */ void m1933lambda$onStarted$0$androidnetSocketKeepalive$1(int i, Callback callback) {
                SocketKeepalive.this.mSlot = Integer.valueOf(i);
                callback.onStarted();
            }

            public void onStopped() {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    executor.execute(new SocketKeepalive$1$$ExternalSyntheticLambda0(this, callback));
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onStopped$1$android-net-SocketKeepalive$1  reason: not valid java name */
            public /* synthetic */ void m1934lambda$onStopped$1$androidnetSocketKeepalive$1(Callback callback) {
                SocketKeepalive.this.mSlot = null;
                callback.onStopped();
            }

            public void onError(int i) {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    executor.execute(new SocketKeepalive$1$$ExternalSyntheticLambda1(this, callback, i));
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onError$2$android-net-SocketKeepalive$1  reason: not valid java name */
            public /* synthetic */ void m1932lambda$onError$2$androidnetSocketKeepalive$1(Callback callback, int i) {
                SocketKeepalive.this.mSlot = null;
                callback.onError(i);
            }

            public void onDataReceived() {
                long clearCallingIdentity = Binder.clearCallingIdentity();
                try {
                    executor.execute(new SocketKeepalive$1$$ExternalSyntheticLambda3(this, callback));
                } finally {
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                }
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onDataReceived$3$android-net-SocketKeepalive$1  reason: not valid java name */
            public /* synthetic */ void m1931lambda$onDataReceived$3$androidnetSocketKeepalive$1(Callback callback) {
                SocketKeepalive.this.mSlot = null;
                callback.onDataReceived();
            }
        };
    }

    public final void start(int i) {
        startImpl(i);
    }

    public final void stop() {
        stopImpl();
    }

    public final void close() {
        stop();
        try {
            this.mPfd.close();
        } catch (IOException unused) {
        }
    }
}
