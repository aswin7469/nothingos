package java.net;

import android.system.ErrnoException;
import android.system.OsConstants;
import android.system.StructGroupReq;
import java.p026io.IOException;
import jdk.net.ExtendedSocketOptions;
import jdk.net.SocketFlow;
import libcore.p030io.IoBridge;
import libcore.p030io.Libcore;
import libcore.util.EmptyArray;
import sun.net.ExtendedOptionsImpl;

class PlainDatagramSocketImpl extends AbstractPlainDatagramSocketImpl {
    PlainDatagramSocketImpl() {
    }

    /* access modifiers changed from: protected */
    public <T> void setOption(SocketOption<T> socketOption, T t) throws IOException {
        if (!socketOption.equals(ExtendedSocketOptions.SO_FLOW_SLA)) {
            super.setOption(socketOption, t);
        } else if (!isClosed()) {
            ExtendedOptionsImpl.checkSetOptionPermission(socketOption);
            ExtendedOptionsImpl.checkValueType(t, SocketFlow.class);
            ExtendedOptionsImpl.setFlowOption(getFileDescriptor(), (SocketFlow) t);
        } else {
            throw new SocketException("Socket closed");
        }
    }

    /* access modifiers changed from: protected */
    public <T> T getOption(SocketOption<T> socketOption) throws IOException {
        if (!socketOption.equals(ExtendedSocketOptions.SO_FLOW_SLA)) {
            return super.getOption(socketOption);
        }
        if (!isClosed()) {
            ExtendedOptionsImpl.checkGetOptionPermission(socketOption);
            T create = SocketFlow.create();
            ExtendedOptionsImpl.getFlowOption(getFileDescriptor(), create);
            return create;
        }
        throw new SocketException("Socket closed");
    }

    /* access modifiers changed from: protected */
    public void socketSetOption(int i, Object obj) throws SocketException {
        try {
            socketSetOption0(i, obj);
        } catch (SocketException e) {
            if (!this.connected) {
                throw e;
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void bind0(int i, InetAddress inetAddress) throws SocketException {
        if (!isClosed()) {
            IoBridge.bind(this.f556fd, inetAddress, i);
            if (i == 0) {
                this.localPort = IoBridge.getLocalInetSocketAddress(this.f556fd).getPort();
            } else {
                this.localPort = i;
            }
        } else {
            throw new SocketException("Socket closed");
        }
    }

    /* access modifiers changed from: protected */
    public void send(DatagramPacket datagramPacket) throws IOException {
        if (isClosed()) {
            throw new SocketException("Socket closed");
        } else if (datagramPacket.getData() == null || datagramPacket.getAddress() == null) {
            throw new NullPointerException("null buffer || null address");
        } else {
            IoBridge.sendto(this.f556fd, datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength(), 0, this.connected ? null : datagramPacket.getAddress(), this.connected ? 0 : datagramPacket.getPort());
        }
    }

    /* access modifiers changed from: protected */
    public synchronized int peek(InetAddress inetAddress) throws IOException {
        DatagramPacket datagramPacket;
        datagramPacket = new DatagramPacket(EmptyArray.BYTE, 0);
        doRecv(datagramPacket, OsConstants.MSG_PEEK);
        inetAddress.holder().address = datagramPacket.getAddress().holder().address;
        return datagramPacket.getPort();
    }

    /* access modifiers changed from: protected */
    public synchronized int peekData(DatagramPacket datagramPacket) throws IOException {
        doRecv(datagramPacket, OsConstants.MSG_PEEK);
        return datagramPacket.getPort();
    }

    /* access modifiers changed from: protected */
    public synchronized void receive0(DatagramPacket datagramPacket) throws IOException {
        doRecv(datagramPacket, 0);
    }

    private void doRecv(DatagramPacket datagramPacket, int i) throws IOException {
        if (!isClosed()) {
            if (this.timeout != 0) {
                IoBridge.poll(this.f556fd, OsConstants.POLLIN | OsConstants.POLLERR, this.timeout);
            }
            IoBridge.recvfrom(false, this.f556fd, datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.bufLength, i, datagramPacket, this.connected);
            return;
        }
        throw new SocketException("Socket closed");
    }

    /* access modifiers changed from: protected */
    public void setTimeToLive(int i) throws IOException {
        IoBridge.setSocketOption(this.f556fd, 17, Integer.valueOf(i));
    }

    /* access modifiers changed from: protected */
    public int getTimeToLive() throws IOException {
        return ((Integer) IoBridge.getSocketOption(this.f556fd, 17)).intValue();
    }

    /* access modifiers changed from: protected */
    public void setTTL(byte b) throws IOException {
        setTimeToLive(b & 255);
    }

    /* access modifiers changed from: protected */
    public byte getTTL() throws IOException {
        return (byte) getTimeToLive();
    }

    private static StructGroupReq makeGroupReq(InetAddress inetAddress, NetworkInterface networkInterface) {
        return new StructGroupReq(networkInterface != null ? networkInterface.getIndex() : 0, inetAddress);
    }

    /* access modifiers changed from: protected */
    public void join(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException {
        if (!isClosed()) {
            IoBridge.setSocketOption(this.f556fd, 19, makeGroupReq(inetAddress, networkInterface));
            return;
        }
        throw new SocketException("Socket closed");
    }

    /* access modifiers changed from: protected */
    public void leave(InetAddress inetAddress, NetworkInterface networkInterface) throws IOException {
        if (!isClosed()) {
            IoBridge.setSocketOption(this.f556fd, 20, makeGroupReq(inetAddress, networkInterface));
            return;
        }
        throw new SocketException("Socket closed");
    }

    /* access modifiers changed from: protected */
    public void datagramSocketCreate() throws SocketException {
        this.f556fd = IoBridge.socket(OsConstants.AF_INET6, OsConstants.SOCK_DGRAM, 0);
        IoBridge.setSocketOption(this.f556fd, 32, true);
        try {
            Libcore.f857os.setsockoptInt(this.f556fd, OsConstants.IPPROTO_IP, OsConstants.IP_MULTICAST_ALL, 0);
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        }
    }

    /* access modifiers changed from: protected */
    public void datagramSocketClose() {
        try {
            IoBridge.closeAndSignalBlockedThreads(this.f556fd);
        } catch (IOException unused) {
        }
    }

    /* access modifiers changed from: protected */
    public void socketSetOption0(int i, Object obj) throws SocketException {
        if (!isClosed()) {
            IoBridge.setSocketOption(this.f556fd, i, obj);
            return;
        }
        throw new SocketException("Socket closed");
    }

    /* access modifiers changed from: protected */
    public Object socketGetOption(int i) throws SocketException {
        if (!isClosed()) {
            return IoBridge.getSocketOption(this.f556fd, i);
        }
        throw new SocketException("Socket closed");
    }

    /* access modifiers changed from: protected */
    public void connect0(InetAddress inetAddress, int i) throws SocketException {
        if (!isClosed()) {
            IoBridge.connect(this.f556fd, inetAddress, i);
            return;
        }
        throw new SocketException("Socket closed");
    }

    /* access modifiers changed from: protected */
    public void disconnect0(int i) {
        if (!isClosed()) {
            InetAddress inetAddress = new InetAddress();
            inetAddress.holder().family = OsConstants.AF_UNSPEC;
            try {
                IoBridge.connect(this.f556fd, inetAddress, 0);
            } catch (SocketException unused) {
            }
        }
    }
}
