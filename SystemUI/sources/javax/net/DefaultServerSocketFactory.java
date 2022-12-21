package javax.net;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.p026io.IOException;

/* compiled from: ServerSocketFactory */
class DefaultServerSocketFactory extends ServerSocketFactory {
    DefaultServerSocketFactory() {
    }

    public ServerSocket createServerSocket() throws IOException {
        return new ServerSocket();
    }

    public ServerSocket createServerSocket(int i) throws IOException {
        return new ServerSocket(i);
    }

    public ServerSocket createServerSocket(int i, int i2) throws IOException {
        return new ServerSocket(i, i2);
    }

    public ServerSocket createServerSocket(int i, int i2, InetAddress inetAddress) throws IOException {
        return new ServerSocket(i, i2, inetAddress);
    }
}
