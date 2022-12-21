package android.net;

import android.annotation.SystemApi;

@SystemApi
public class SocketLocalAddressChangedException extends Exception {
    public SocketLocalAddressChangedException() {
        super("The local address of the socket changed");
    }
}
