package android.net;

import android.os.CancellationSignal;
import java.p026io.FileDescriptor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DnsResolver$$ExternalSyntheticLambda9 implements CancellationSignal.OnCancelListener {
    public final /* synthetic */ DnsResolver f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ boolean f$2;
    public final /* synthetic */ FileDescriptor f$3;
    public final /* synthetic */ boolean f$4;
    public final /* synthetic */ FileDescriptor f$5;

    public /* synthetic */ DnsResolver$$ExternalSyntheticLambda9(DnsResolver dnsResolver, Object obj, boolean z, FileDescriptor fileDescriptor, boolean z2, FileDescriptor fileDescriptor2) {
        this.f$0 = dnsResolver;
        this.f$1 = obj;
        this.f$2 = z;
        this.f$3 = fileDescriptor;
        this.f$4 = z2;
        this.f$5 = fileDescriptor2;
    }

    public final void onCancel() {
        this.f$0.m1877lambda$query$6$androidnetDnsResolver(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
