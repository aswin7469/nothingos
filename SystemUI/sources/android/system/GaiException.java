package android.system;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.UnknownHostException;
import libcore.p030io.Libcore;

public final class GaiException extends RuntimeException {
    public final int error;
    private final String functionName;

    public GaiException(String str, int i) {
        this.functionName = str;
        this.error = i;
    }

    public GaiException(String str, int i, Throwable th) {
        super(th);
        this.functionName = str;
        this.error = i;
    }

    public String getMessage() {
        String gaiName = OsConstants.gaiName(this.error);
        if (gaiName == null) {
            gaiName = "GAI_ error " + this.error;
        }
        return this.functionName + " failed: " + gaiName + " (" + Libcore.f855os.gai_strerror(this.error) + NavigationBarInflaterView.KEY_CODE_END;
    }

    public UnknownHostException rethrowAsUnknownHostException(String str) throws UnknownHostException {
        UnknownHostException unknownHostException = new UnknownHostException(str);
        unknownHostException.initCause(this);
        throw unknownHostException;
    }

    public UnknownHostException rethrowAsUnknownHostException() throws UnknownHostException {
        throw rethrowAsUnknownHostException(getMessage());
    }
}
