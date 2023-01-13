package android.net;

import android.net.SocketKeepalive;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SocketKeepalive$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ SocketKeepalive.C00981 f$0;
    public final /* synthetic */ SocketKeepalive.Callback f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ SocketKeepalive$1$$ExternalSyntheticLambda1(SocketKeepalive.C00981 r1, SocketKeepalive.Callback callback, int i) {
        this.f$0 = r1;
        this.f$1 = callback;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.m1938lambda$onError$2$androidnetSocketKeepalive$1(this.f$1, this.f$2);
    }
}
