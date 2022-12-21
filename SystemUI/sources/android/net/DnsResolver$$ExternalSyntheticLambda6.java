package android.net;

import android.net.DnsResolver;
import android.system.ErrnoException;
import android.system.OsConstants;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DnsResolver$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ DnsResolver.Callback f$0;

    public /* synthetic */ DnsResolver$$ExternalSyntheticLambda6(DnsResolver.Callback callback) {
        this.f$0 = callback;
    }

    public final void run() {
        this.f$0.onError(new DnsResolver.DnsException(1, new ErrnoException("resNetworkQuery", OsConstants.ENONET)));
    }
}
