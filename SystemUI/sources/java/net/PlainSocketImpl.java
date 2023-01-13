package java.net;

import android.system.ErrnoException;
import android.system.OsConstants;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import jdk.net.ExtendedSocketOptions;
import jdk.net.SocketFlow;
import libcore.p030io.AsynchronousCloseMonitor;
import libcore.p030io.IoBridge;
import libcore.p030io.IoUtils;
import libcore.p030io.Libcore;
import sun.net.ExtendedOptionsImpl;

class PlainSocketImpl extends AbstractPlainSocketImpl {
    PlainSocketImpl() {
        this.f557fd = new FileDescriptor();
    }

    /* access modifiers changed from: protected */
    public <T> void setOption(SocketOption<T> socketOption, T t) throws IOException {
        if (!socketOption.equals(ExtendedSocketOptions.SO_FLOW_SLA)) {
            super.setOption(socketOption, t);
        } else if (!isClosedOrPending()) {
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
        if (!isClosedOrPending()) {
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
            if (this.socket == null || !this.socket.isConnected()) {
                throw e;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void socketCreate(boolean z) throws IOException {
        this.f557fd.setInt$(IoBridge.socket(OsConstants.AF_INET6, z ? OsConstants.SOCK_STREAM : OsConstants.SOCK_DGRAM, 0).getInt$());
        IoUtils.setFdOwner(this.f557fd, this);
        if (this.serverSocket != null) {
            IoUtils.setBlocking(this.f557fd, false);
            IoBridge.setSocketOption(this.f557fd, 4, true);
        }
    }

    /* access modifiers changed from: package-private */
    public void socketConnect(InetAddress inetAddress, int i, int i2) throws IOException {
        if (this.f557fd == null || !this.f557fd.valid()) {
            throw new SocketException("Socket closed");
        }
        IoBridge.connect(this.f557fd, inetAddress, i, i2);
        this.address = inetAddress;
        this.port = i;
        if (this.localport == 0 && !isClosedOrPending()) {
            this.localport = IoBridge.getLocalInetSocketAddress(this.f557fd).getPort();
        }
    }

    /* access modifiers changed from: package-private */
    public void socketBind(InetAddress inetAddress, int i) throws IOException {
        if (this.f557fd == null || !this.f557fd.valid()) {
            throw new SocketException("Socket closed");
        }
        IoBridge.bind(this.f557fd, inetAddress, i);
        this.address = inetAddress;
        if (i == 0) {
            this.localport = IoBridge.getLocalInetSocketAddress(this.f557fd).getPort();
        } else {
            this.localport = i;
        }
    }

    /* access modifiers changed from: package-private */
    public void socketListen(int i) throws IOException {
        if (this.f557fd == null || !this.f557fd.valid()) {
            throw new SocketException("Socket closed");
        }
        try {
            Libcore.f855os.listen(this.f557fd, i);
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        }
    }

    /* access modifiers changed from: package-private */
    public void socketAccept(SocketImpl socketImpl) throws IOException {
        if (this.f557fd == null || !this.f557fd.valid()) {
            throw new SocketException("Socket closed");
        }
        if (this.timeout <= 0) {
            IoBridge.poll(this.f557fd, OsConstants.POLLIN | OsConstants.POLLERR, -1);
        } else {
            IoBridge.poll(this.f557fd, OsConstants.POLLIN | OsConstants.POLLERR, this.timeout);
        }
        InetSocketAddress inetSocketAddress = new InetSocketAddress();
        try {
            socketImpl.f557fd.setInt$(Libcore.f855os.accept(this.f557fd, inetSocketAddress).getInt$());
            IoUtils.setFdOwner(socketImpl.f557fd, socketImpl);
            socketImpl.address = inetSocketAddress.getAddress();
            socketImpl.port = inetSocketAddress.getPort();
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EAGAIN) {
                SocketTimeoutException socketTimeoutException = new SocketTimeoutException();
                socketTimeoutException.initCause(e);
                throw socketTimeoutException;
            } else if (e.errno == OsConstants.EINVAL || e.errno == OsConstants.EBADF) {
                throw new SocketException("Socket closed");
            } else {
                e.rethrowAsSocketException();
            }
        }
        socketImpl.localport = IoBridge.getLocalInetSocketAddress(socketImpl.f557fd).getPort();
    }

    /* access modifiers changed from: package-private */
    public int socketAvailable() throws IOException {
        return IoBridge.available(this.f557fd);
    }

    /* access modifiers changed from: package-private */
    public void socketClose0(boolean z) throws IOException {
        if (this.f557fd == null || !this.f557fd.valid()) {
            throw new SocketException("socket already closed");
        }
        FileDescriptor markerFD = z ? getMarkerFD() : null;
        if (!z || markerFD == null) {
            IoBridge.closeAndSignalBlockedThreads(this.f557fd);
            return;
        }
        try {
            Libcore.f855os.dup2(markerFD, this.f557fd.getInt$());
            Libcore.f855os.close(markerFD);
            AsynchronousCloseMonitor.signalBlockedThreads(this.f557fd);
        } catch (ErrnoException unused) {
        }
    }

    private FileDescriptor getMarkerFD() throws SocketException {
        FileDescriptor fileDescriptor = new FileDescriptor();
        FileDescriptor fileDescriptor2 = new FileDescriptor();
        try {
            Libcore.f855os.socketpair(OsConstants.AF_UNIX, OsConstants.SOCK_STREAM, 0, fileDescriptor, fileDescriptor2);
            Libcore.f855os.shutdown(fileDescriptor, OsConstants.SHUT_RDWR);
            Libcore.f855os.close(fileDescriptor2);
            return fileDescriptor;
        } catch (ErrnoException unused) {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void socketShutdown(int i) throws IOException {
        try {
            Libcore.f855os.shutdown(this.f557fd, i);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    /* access modifiers changed from: package-private */
    public void socketSetOption0(int i, Object obj) throws SocketException {
        if (i != 4102) {
            IoBridge.setSocketOption(this.f557fd, i, obj);
        }
    }

    /* access modifiers changed from: package-private */
    public Object socketGetOption(int i) throws SocketException {
        return IoBridge.getSocketOption(this.f557fd, i);
    }

    /* access modifiers changed from: package-private */
    public void socketSendUrgentData(int i) throws IOException {
        if (this.f557fd == null || !this.f557fd.valid()) {
            throw new SocketException("Socket closed");
        }
        try {
            Libcore.f855os.sendto(this.f557fd, new byte[]{(byte) i}, 0, 1, OsConstants.MSG_OOB, (InetAddress) null, 0);
        } catch (ErrnoException e) {
            throw e.rethrowAsSocketException();
        }
    }
}
