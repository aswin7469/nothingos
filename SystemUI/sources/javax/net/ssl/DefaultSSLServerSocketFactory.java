package javax.net.ssl;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.p026io.IOException;

/* compiled from: SSLServerSocketFactory */
class DefaultSSLServerSocketFactory extends SSLServerSocketFactory {
    private final Exception reason;

    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    DefaultSSLServerSocketFactory(Exception exc) {
        this.reason = exc;
    }

    private ServerSocket throwException() throws SocketException {
        throw ((SocketException) new SocketException(this.reason.toString()).initCause(this.reason));
    }

    public ServerSocket createServerSocket() throws IOException {
        return throwException();
    }

    public ServerSocket createServerSocket(int i) throws IOException {
        return throwException();
    }

    public ServerSocket createServerSocket(int i, int i2) throws IOException {
        return throwException();
    }

    public ServerSocket createServerSocket(int i, int i2, InetAddress inetAddress) throws IOException {
        return throwException();
    }
}
