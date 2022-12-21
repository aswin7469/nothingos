package android.net;

import android.os.CancellationSignal;
import java.p026io.FileDescriptor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DnsResolver$$ExternalSyntheticLambda2 implements CancellationSignal.OnCancelListener {
    public final /* synthetic */ DnsResolver f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ FileDescriptor f$2;

    public /* synthetic */ DnsResolver$$ExternalSyntheticLambda2(DnsResolver dnsResolver, Object obj, FileDescriptor fileDescriptor) {
        this.f$0 = dnsResolver;
        this.f$1 = obj;
        this.f$2 = fileDescriptor;
    }

    public final void onCancel() {
        this.f$0.m1876lambda$addCancellationSignal$10$androidnetDnsResolver(this.f$1, this.f$2);
    }
}
