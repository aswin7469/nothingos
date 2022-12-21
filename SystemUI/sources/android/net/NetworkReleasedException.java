package android.net;

import android.annotation.SystemApi;

@SystemApi
public class NetworkReleasedException extends Exception {
    public NetworkReleasedException() {
        super("The network was released and is no longer available");
    }
}
