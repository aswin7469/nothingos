package javax.net.ssl;

import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.p026io.IOException;

/* compiled from: SSLSocketFactory */
class DefaultSSLSocketFactory extends SSLSocketFactory {
    private Exception reason;

    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    DefaultSSLSocketFactory(Exception exc) {
        this.reason = exc;
    }

    private Socket throwException() throws SocketException {
        throw ((SocketException) new SocketException(this.reason.toString()).initCause(this.reason));
    }

    public Socket createSocket() throws IOException {
        return throwException();
    }

    public Socket createSocket(String str, int i) throws IOException {
        return throwException();
    }

    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        return throwException();
    }

    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return throwException();
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        return throwException();
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return throwException();
    }
}
