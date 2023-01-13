package android.net;

import android.net.SocketKeepalive;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SocketKeepalive$1$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ SocketKeepalive.C00981 f$0;
    public final /* synthetic */ SocketKeepalive.Callback f$1;

    public /* synthetic */ SocketKeepalive$1$$ExternalSyntheticLambda3(SocketKeepalive.C00981 r1, SocketKeepalive.Callback callback) {
        this.f$0 = r1;
        this.f$1 = callback;
    }

    public final void run() {
        this.f$0.m1937lambda$onDataReceived$3$androidnetSocketKeepalive$1(this.f$1);
    }
}
