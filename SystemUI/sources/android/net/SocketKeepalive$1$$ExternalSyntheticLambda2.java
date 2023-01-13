package android.net;

import android.net.SocketKeepalive;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class SocketKeepalive$1$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ SocketKeepalive.C00981 f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ SocketKeepalive.Callback f$2;

    public /* synthetic */ SocketKeepalive$1$$ExternalSyntheticLambda2(SocketKeepalive.C00981 r1, int i, SocketKeepalive.Callback callback) {
        this.f$0 = r1;
        this.f$1 = i;
        this.f$2 = callback;
    }

    public final void run() {
        this.f$0.m1939lambda$onStarted$0$androidnetSocketKeepalive$1(this.f$1, this.f$2);
    }
}
