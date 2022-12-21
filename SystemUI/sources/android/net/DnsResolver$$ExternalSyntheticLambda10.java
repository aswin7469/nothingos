package android.net;

import android.net.DnsResolver;
import android.os.CancellationSignal;
import java.p026io.FileDescriptor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DnsResolver$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ CancellationSignal f$1;
    public final /* synthetic */ FileDescriptor f$2;
    public final /* synthetic */ DnsResolver.Callback f$3;

    public /* synthetic */ DnsResolver$$ExternalSyntheticLambda10(Object obj, CancellationSignal cancellationSignal, FileDescriptor fileDescriptor, DnsResolver.Callback callback) {
        this.f$0 = obj;
        this.f$1 = cancellationSignal;
        this.f$2 = fileDescriptor;
        this.f$3 = callback;
    }

    public final void run() {
        DnsResolver.lambda$registerFDListener$8(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
