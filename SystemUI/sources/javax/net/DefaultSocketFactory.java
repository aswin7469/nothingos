package javax.net;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.p026io.IOException;

/* compiled from: SocketFactory */
class DefaultSocketFactory extends SocketFactory {
    DefaultSocketFactory() {
    }

    public Socket createSocket() {
        return new Socket();
    }

    public Socket createSocket(String str, int i) throws IOException, UnknownHostException {
        return new Socket(str, i);
    }

    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return new Socket(inetAddress, i);
    }

    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException, UnknownHostException {
        return new Socket(str, i, inetAddress, i2);
    }

    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return new Socket(inetAddress, i, inetAddress2, i2);
    }
}
