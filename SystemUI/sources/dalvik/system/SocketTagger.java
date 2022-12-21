package dalvik.system;

import android.annotation.SystemApi;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.p026io.FileDescriptor;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public abstract class SocketTagger {
    private static SocketTagger tagger = new SocketTagger() {
        public void tag(FileDescriptor fileDescriptor) throws SocketException {
        }

        public void untag(FileDescriptor fileDescriptor) throws SocketException {
        }
    };

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public abstract void tag(FileDescriptor fileDescriptor) throws SocketException;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public abstract void untag(FileDescriptor fileDescriptor) throws SocketException;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final void tag(Socket socket) throws SocketException {
        if (!socket.isClosed()) {
            tag(socket.getFileDescriptor$());
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final void untag(Socket socket) throws SocketException {
        if (!socket.isClosed()) {
            untag(socket.getFileDescriptor$());
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final void tag(DatagramSocket datagramSocket) throws SocketException {
        if (!datagramSocket.isClosed()) {
            tag(datagramSocket.getFileDescriptor$());
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final void untag(DatagramSocket datagramSocket) throws SocketException {
        if (!datagramSocket.isClosed()) {
            untag(datagramSocket.getFileDescriptor$());
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static synchronized void set(SocketTagger socketTagger) {
        synchronized (SocketTagger.class) {
            if (socketTagger != null) {
                tagger = socketTagger;
            } else {
                throw new NullPointerException("tagger == null");
            }
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static synchronized SocketTagger get() {
        SocketTagger socketTagger;
        synchronized (SocketTagger.class) {
            socketTagger = tagger;
        }
        return socketTagger;
    }
}
