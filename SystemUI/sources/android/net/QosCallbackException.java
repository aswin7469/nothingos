package android.net;

import android.annotation.SystemApi;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class QosCallbackException extends Exception {
    public static final int EX_TYPE_FILTER_NETWORK_RELEASED = 1;
    public static final int EX_TYPE_FILTER_NONE = 0;
    public static final int EX_TYPE_FILTER_NOT_SUPPORTED = 3;
    public static final int EX_TYPE_FILTER_SOCKET_LOCAL_ADDRESS_CHANGED = 4;
    public static final int EX_TYPE_FILTER_SOCKET_NOT_BOUND = 2;
    private static final String TAG = "QosCallbackException";

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExceptionType {
    }

    static QosCallbackException createException(int i) {
        if (i == 1) {
            return new QosCallbackException((Throwable) new NetworkReleasedException());
        }
        if (i == 2) {
            return new QosCallbackException((Throwable) new SocketNotBoundException());
        }
        if (i == 3) {
            return new QosCallbackException((Throwable) new UnsupportedOperationException("This device does not support the specified filter"));
        }
        if (i == 4) {
            return new QosCallbackException((Throwable) new SocketLocalAddressChangedException());
        }
        Log.wtf(TAG, "create: No case setup for exception type: '" + i + "'");
        return new QosCallbackException((Throwable) new RuntimeException("Unknown exception code: " + i));
    }

    public QosCallbackException(String str) {
        super(str);
    }

    public QosCallbackException(Throwable th) {
        super(th);
    }
}
