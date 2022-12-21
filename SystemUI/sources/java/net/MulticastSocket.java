package java.net;

import java.p026io.IOException;
import java.util.Enumeration;
import java.util.Set;

public class MulticastSocket extends DatagramSocket {
    private static Set<SocketOption<?>> options = null;
    private static boolean optionsSet = false;
    private InetAddress infAddress;
    private Object infLock;
    private boolean interfaceSet;
    private Object ttlLock;

    public MulticastSocket() throws IOException {
        this((SocketAddress) new InetSocketAddress(0));
    }

    public MulticastSocket(int i) throws IOException {
        this((SocketAddress) new InetSocketAddress(i));
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MulticastSocket(SocketAddress socketAddress) throws IOException {
        super((SocketAddress) null);
        SocketAddress socketAddress2 = null;
        this.ttlLock = new Object();
        this.infLock = new Object();
        this.infAddress = null;
        setReuseAddress(true);
        if (socketAddress != null) {
            try {
                bind(socketAddress);
            } finally {
                if (!isBound()) {
                    close();
                }
            }
        }
    }

    @Deprecated
    public void setTTL(byte b) throws IOException {
        if (!isClosed()) {
            getImpl().setTTL(b);
            return;
        }
        throw new SocketException("Socket is closed");
    }

    public void setTimeToLive(int i) throws IOException {
        if (i < 0 || i > 255) {
            throw new IllegalArgumentException("ttl out of range");
        } else if (!isClosed()) {
            getImpl().setTimeToLive(i);
        } else {
            throw new SocketException("Socket is closed");
        }
    }

    @Deprecated
    public byte getTTL() throws IOException {
        if (!isClosed()) {
            return getImpl().getTTL();
        }
        throw new SocketException("Socket is closed");
    }

    public int getTimeToLive() throws IOException {
        if (!isClosed()) {
            return getImpl().getTimeToLive();
        }
        throw new SocketException("Socket is closed");
    }

    public void joinGroup(InetAddress inetAddress) throws IOException {
        if (!isClosed()) {
            checkAddress(inetAddress, "joinGroup");
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkMulticast(inetAddress);
            }
            if (inetAddress.isMulticastAddress()) {
                NetworkInterface networkInterface = NetworkInterface.getDefault();
                if (!this.interfaceSet && networkInterface != null) {
                    setNetworkInterface(networkInterface);
                }
                getImpl().join(inetAddress);
                return;
            }
            throw new SocketException("Not a multicast address");
        }
        throw new SocketException("Socket is closed");
    }

    public void leaveGroup(InetAddress inetAddress) throws IOException {
        if (!isClosed()) {
            checkAddress(inetAddress, "leaveGroup");
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkMulticast(inetAddress);
            }
            if (inetAddress.isMulticastAddress()) {
                getImpl().leave(inetAddress);
                return;
            }
            throw new SocketException("Not a multicast address");
        }
        throw new SocketException("Socket is closed");
    }

    public void joinGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        } else if (!this.oldImpl) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            checkAddress(inetSocketAddress.getAddress(), "joinGroup");
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkMulticast(inetSocketAddress.getAddress());
            }
            if (inetSocketAddress.getAddress().isMulticastAddress()) {
                getImpl().joinGroup(socketAddress, networkInterface);
                return;
            }
            throw new SocketException("Not a multicast address");
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void leaveGroup(SocketAddress socketAddress, NetworkInterface networkInterface) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket is closed");
        } else if (socketAddress == null || !(socketAddress instanceof InetSocketAddress)) {
            throw new IllegalArgumentException("Unsupported address type");
        } else if (!this.oldImpl) {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            checkAddress(inetSocketAddress.getAddress(), "leaveGroup");
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                securityManager.checkMulticast(inetSocketAddress.getAddress());
            }
            if (inetSocketAddress.getAddress().isMulticastAddress()) {
                getImpl().leaveGroup(socketAddress, networkInterface);
                return;
            }
            throw new SocketException("Not a multicast address");
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public void setInterface(InetAddress inetAddress) throws SocketException {
        if (!isClosed()) {
            checkAddress(inetAddress, "setInterface");
            synchronized (this.infLock) {
                getImpl().setOption(16, inetAddress);
                this.infAddress = inetAddress;
                this.interfaceSet = true;
            }
            return;
        }
        throw new SocketException("Socket is closed");
    }

    public InetAddress getInterface() throws SocketException {
        if (!isClosed()) {
            synchronized (this.infLock) {
                InetAddress inetAddress = (InetAddress) getImpl().getOption(16);
                InetAddress inetAddress2 = this.infAddress;
                if (inetAddress2 == null) {
                    return inetAddress;
                }
                if (inetAddress.equals(inetAddress2)) {
                    return inetAddress;
                }
                try {
                    Enumeration<InetAddress> inetAddresses = NetworkInterface.getByInetAddress(inetAddress).getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        if (inetAddresses.nextElement().equals(this.infAddress)) {
                            InetAddress inetAddress3 = this.infAddress;
                            return inetAddress3;
                        }
                    }
                    this.infAddress = null;
                    return inetAddress;
                } catch (Exception unused) {
                    return inetAddress;
                }
            }
        } else {
            throw new SocketException("Socket is closed");
        }
    }

    public void setNetworkInterface(NetworkInterface networkInterface) throws SocketException {
        synchronized (this.infLock) {
            getImpl().setOption(31, networkInterface);
            this.infAddress = null;
            this.interfaceSet = true;
        }
    }

    public NetworkInterface getNetworkInterface() throws SocketException {
        Integer num = (Integer) getImpl().getOption(31);
        if (num.intValue() != 0) {
            return NetworkInterface.getByIndex(num.intValue());
        }
        InetAddress[] inetAddressArr = {InetAddress.anyLocalAddress()};
        return new NetworkInterface(inetAddressArr[0].getHostName(), 0, inetAddressArr);
    }

    public void setLoopbackMode(boolean z) throws SocketException {
        getImpl().setOption(18, Boolean.valueOf(z));
    }

    public boolean getLoopbackMode() throws SocketException {
        return ((Boolean) getImpl().getOption(18)).booleanValue();
    }

    @Deprecated
    public void send(DatagramPacket datagramPacket, byte b) throws IOException {
        if (!isClosed()) {
            checkAddress(datagramPacket.getAddress(), "send");
            synchronized (this.ttlLock) {
                synchronized (datagramPacket) {
                    if (this.connectState == 0) {
                        SecurityManager securityManager = System.getSecurityManager();
                        if (securityManager != null) {
                            if (datagramPacket.getAddress().isMulticastAddress()) {
                                securityManager.checkMulticast(datagramPacket.getAddress(), b);
                            } else {
                                securityManager.checkConnect(datagramPacket.getAddress().getHostAddress(), datagramPacket.getPort());
                            }
                        }
                    } else {
                        InetAddress address = datagramPacket.getAddress();
                        if (address == null) {
                            datagramPacket.setAddress(this.connectedAddress);
                            datagramPacket.setPort(this.connectedPort);
                        } else if (!address.equals(this.connectedAddress) || datagramPacket.getPort() != this.connectedPort) {
                            throw new SecurityException("connected address and packet address differ");
                        }
                    }
                    byte ttl = getTTL();
                    if (b != ttl) {
                        try {
                            getImpl().setTTL(b);
                        } catch (Throwable th) {
                            if (b != ttl) {
                                getImpl().setTTL(ttl);
                            }
                            throw th;
                        }
                    }
                    getImpl().send(datagramPacket);
                    if (b != ttl) {
                        getImpl().setTTL(ttl);
                    }
                }
            }
            return;
        }
        throw new SocketException("Socket is closed");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(7:8|9|10|11|12|13|14) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x001a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Set<java.net.SocketOption<?>> supportedOptions() {
        /*
            r2 = this;
            java.lang.Class<java.net.MulticastSocket> r0 = java.net.MulticastSocket.class
            monitor-enter(r0)
            boolean r1 = optionsSet     // Catch:{ all -> 0x0027 }
            if (r1 == 0) goto L_0x000b
            java.util.Set<java.net.SocketOption<?>> r2 = options     // Catch:{ all -> 0x0027 }
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return r2
        L_0x000b:
            java.net.DatagramSocketImpl r2 = r2.getImpl()     // Catch:{ SocketException -> 0x001a }
            java.util.Set r2 = r2.supportedOptions()     // Catch:{ SocketException -> 0x001a }
            java.util.Set r2 = java.util.Collections.unmodifiableSet(r2)     // Catch:{ SocketException -> 0x001a }
            options = r2     // Catch:{ SocketException -> 0x001a }
            goto L_0x0020
        L_0x001a:
            java.util.Set r2 = java.util.Collections.emptySet()     // Catch:{ all -> 0x0027 }
            options = r2     // Catch:{ all -> 0x0027 }
        L_0x0020:
            r2 = 1
            optionsSet = r2     // Catch:{ all -> 0x0027 }
            java.util.Set<java.net.SocketOption<?>> r2 = options     // Catch:{ all -> 0x0027 }
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            return r2
        L_0x0027:
            r2 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0027 }
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.net.MulticastSocket.supportedOptions():java.util.Set");
    }
}
