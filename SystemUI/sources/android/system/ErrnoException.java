package android.system;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.SocketException;
import java.p026io.IOException;
import libcore.p030io.Libcore;

public final class ErrnoException extends Exception {
    public final int errno;
    private final String functionName;

    public ErrnoException(String str, int i) {
        this.functionName = str;
        this.errno = i;
    }

    public ErrnoException(String str, int i, Throwable th) {
        super(th);
        this.functionName = str;
        this.errno = i;
    }

    public String getMessage() {
        String errnoName = OsConstants.errnoName(this.errno);
        if (errnoName == null) {
            errnoName = "errno " + this.errno;
        }
        return this.functionName + " failed: " + errnoName + " (" + Libcore.f855os.strerror(this.errno) + NavigationBarInflaterView.KEY_CODE_END;
    }

    public IOException rethrowAsIOException() throws IOException {
        IOException iOException = new IOException(getMessage());
        iOException.initCause(this);
        throw iOException;
    }

    public SocketException rethrowAsSocketException() throws SocketException {
        throw new SocketException(getMessage(), this);
    }
}
