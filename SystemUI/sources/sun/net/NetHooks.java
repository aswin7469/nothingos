package sun.net;

import java.net.InetAddress;
import java.p026io.FileDescriptor;
import java.p026io.IOException;

public final class NetHooks {
    public static void beforeTcpBind(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws IOException {
    }

    public static void beforeTcpConnect(FileDescriptor fileDescriptor, InetAddress inetAddress, int i) throws IOException {
    }
}
