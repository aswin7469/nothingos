package java.net;

import java.p026io.IOException;

class SocketSecrets {
    SocketSecrets() {
    }

    private static <T> void setOption(Object obj, SocketOption<T> socketOption, T t) throws IOException {
        SocketImpl socketImpl;
        if (obj instanceof Socket) {
            socketImpl = ((Socket) obj).getImpl();
        } else if (obj instanceof ServerSocket) {
            socketImpl = ((ServerSocket) obj).getImpl();
        } else {
            throw new IllegalArgumentException();
        }
        socketImpl.setOption(socketOption, t);
    }

    private static <T> T getOption(Object obj, SocketOption<T> socketOption) throws IOException {
        SocketImpl socketImpl;
        if (obj instanceof Socket) {
            socketImpl = ((Socket) obj).getImpl();
        } else if (obj instanceof ServerSocket) {
            socketImpl = ((ServerSocket) obj).getImpl();
        } else {
            throw new IllegalArgumentException();
        }
        return socketImpl.getOption(socketOption);
    }

    private static <T> void setOption(DatagramSocket datagramSocket, SocketOption<T> socketOption, T t) throws IOException {
        datagramSocket.getImpl().setOption(socketOption, t);
    }

    private static <T> T getOption(DatagramSocket datagramSocket, SocketOption<T> socketOption) throws IOException {
        return datagramSocket.getImpl().getOption(socketOption);
    }
}
