package android.net.wifi;

import android.net.wifi.ISoftApCallback;
import android.os.Parcel;
import java.util.List;
import java.util.function.BiConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ISoftApCallback$Stub$Proxy$$ExternalSyntheticLambda1 implements BiConsumer {
    public final /* synthetic */ Parcel f$0;

    public /* synthetic */ ISoftApCallback$Stub$Proxy$$ExternalSyntheticLambda1(Parcel parcel) {
        this.f$0 = parcel;
    }

    public final void accept(Object obj, Object obj2) {
        ISoftApCallback.Stub.Proxy.lambda$onConnectedClientsOrInfoChanged$1(this.f$0, (String) obj, (List) obj2);
    }
}
