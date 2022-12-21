package android.net;

import android.net.DnsResolver;
import android.system.ErrnoException;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DnsResolver$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ DnsResolver.Callback f$0;
    public final /* synthetic */ ErrnoException f$1;

    public /* synthetic */ DnsResolver$$ExternalSyntheticLambda3(DnsResolver.Callback callback, ErrnoException errnoException) {
        this.f$0 = callback;
        this.f$1 = errnoException;
    }

    public final void run() {
        this.f$0.onError(new DnsResolver.DnsException(1, this.f$1));
    }
}
