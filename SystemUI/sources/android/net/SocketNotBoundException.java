package android.net;

import android.annotation.SystemApi;

@SystemApi
public class SocketNotBoundException extends Exception {
    public SocketNotBoundException() {
        super("The socket is unbound");
    }
}
