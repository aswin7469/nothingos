package android.net;

import android.net.SocketKeepalive;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.net.InetAddress;
import java.util.concurrent.Executor;

public final class NattSocketKeepalive extends SocketKeepalive {
    public static final int NATT_PORT = 4500;
    private final InetAddress mDestination;
    private final int mResourceId;
    private final InetAddress mSource;

    NattSocketKeepalive(IConnectivityManager iConnectivityManager, Network network, ParcelFileDescriptor parcelFileDescriptor, int i, InetAddress inetAddress, InetAddress inetAddress2, Executor executor, SocketKeepalive.Callback callback) {
        super(iConnectivityManager, network, parcelFileDescriptor, executor, callback);
        this.mSource = inetAddress;
        this.mDestination = inetAddress2;
        this.mResourceId = i;
    }

    /* access modifiers changed from: package-private */
    public void startImpl(int i) {
        this.mExecutor.execute(new NattSocketKeepalive$$ExternalSyntheticLambda1(this, i));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startImpl$0$android-net-NattSocketKeepalive  reason: not valid java name */
    public /* synthetic */ void m1889lambda$startImpl$0$androidnetNattSocketKeepalive(int i) {
        try {
            this.mService.startNattKeepaliveWithFd(this.mNetwork, this.mPfd, this.mResourceId, i, this.mCallback, this.mSource.getHostAddress(), this.mDestination.getHostAddress());
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error starting socket keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }

    /* access modifiers changed from: package-private */
    public void stopImpl() {
        this.mExecutor.execute(new NattSocketKeepalive$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$stopImpl$1$android-net-NattSocketKeepalive  reason: not valid java name */
    public /* synthetic */ void m1890lambda$stopImpl$1$androidnetNattSocketKeepalive() {
        try {
            if (this.mSlot != null) {
                this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
            }
        } catch (RemoteException e) {
            Log.e("SocketKeepalive", "Error stopping socket keepalive: ", e);
            throw e.rethrowFromSystemServer();
        }
    }
}
