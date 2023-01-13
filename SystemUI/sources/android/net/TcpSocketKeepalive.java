package android.net;

import android.net.SocketKeepalive;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.util.concurrent.Executor;

final class TcpSocketKeepalive extends SocketKeepalive {
    TcpSocketKeepalive(IConnectivityManager iConnectivityManager, Network network, ParcelFileDescriptor parcelFileDescriptor, Executor executor, SocketKeepalive.Callback callback) {
        super(iConnectivityManager, network, parcelFileDescriptor, executor, callback);
    }

    /* access modifiers changed from: package-private */
    public void startImpl(int i) {
        this.mExecutor.execute(new TcpSocketKeepalive$$ExternalSyntheticLambda0(this, i));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startImpl$0$android-net-TcpSocketKeepalive  reason: not valid java name */
    public /* synthetic */ void m1942lambda$startImpl$0$androidnetTcpSocketKeepalive(int i) {
        try {
            this.mService.startTcpKeepalive(this.mNetwork, this.mPfd, i, this.mCallback);
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error starting packet keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* access modifiers changed from: package-private */
    public void stopImpl() {
        this.mExecutor.execute(new TcpSocketKeepalive$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$stopImpl$1$android-net-TcpSocketKeepalive  reason: not valid java name */
    public /* synthetic */ void m1943lambda$stopImpl$1$androidnetTcpSocketKeepalive() {
        try {
            if (this.mSlot != null) {
                this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
            }
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error stopping packet keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }
}
