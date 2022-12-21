package androidx.core.net;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import java.p026io.FileDescriptor;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;

class DatagramSocketWrapper extends Socket {
    DatagramSocketWrapper(DatagramSocket datagramSocket, FileDescriptor fileDescriptor) throws SocketException {
        super((SocketImpl) new DatagramSocketImplWrapper(datagramSocket, fileDescriptor));
    }

    private static class DatagramSocketImplWrapper extends SocketImpl {
        DatagramSocketImplWrapper(DatagramSocket datagramSocket, FileDescriptor fileDescriptor) {
            this.localport = datagramSocket.getLocalPort();
            this.f559fd = fileDescriptor;
        }

        /* access modifiers changed from: protected */
        public void accept(SocketImpl socketImpl) throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public int available() throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public void bind(InetAddress inetAddress, int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public void close() throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public void connect(String str, int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public void connect(InetAddress inetAddress, int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public void create(boolean z) throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public InputStream getInputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public void listen(int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public void connect(SocketAddress socketAddress, int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        /* access modifiers changed from: protected */
        public void sendUrgentData(int i) throws IOException {
            throw new UnsupportedOperationException();
        }

        public Object getOption(int i) throws SocketException {
            throw new UnsupportedOperationException();
        }

        public void setOption(int i, Object obj) throws SocketException {
            throw new UnsupportedOperationException();
        }
    }
}
