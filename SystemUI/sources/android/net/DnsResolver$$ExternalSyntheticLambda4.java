package android.net;

import android.net.DnsResolver;
import android.os.CancellationSignal;
import android.os.MessageQueue;
import java.p026io.FileDescriptor;
import java.util.concurrent.Executor;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DnsResolver$$ExternalSyntheticLambda4 implements MessageQueue.OnFileDescriptorEventListener {
    public final /* synthetic */ MessageQueue f$0;
    public final /* synthetic */ Executor f$1;
    public final /* synthetic */ Object f$2;
    public final /* synthetic */ CancellationSignal f$3;
    public final /* synthetic */ DnsResolver.Callback f$4;

    public /* synthetic */ DnsResolver$$ExternalSyntheticLambda4(MessageQueue messageQueue, Executor executor, Object obj, CancellationSignal cancellationSignal, DnsResolver.Callback callback) {
        this.f$0 = messageQueue;
        this.f$1 = executor;
        this.f$2 = obj;
        this.f$3 = cancellationSignal;
        this.f$4 = callback;
    }

    public final int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i) {
        return DnsResolver.lambda$registerFDListener$9(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, fileDescriptor, i);
    }
}
