package java.nio.channels;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.p026io.IOException;

public interface MulticastChannel extends NetworkChannel {
    void close() throws IOException;

    MembershipKey join(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException;

    MembershipKey join(InetAddress inetAddress, NetworkInterface networkInterface, InetAddress inetAddress2) throws IOException;
}
