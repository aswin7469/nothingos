package android.net.wifi;

import android.os.Parcel;
import java.util.Map;
import java.util.function.IntConsumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ISoftApCallback$Stub$$ExternalSyntheticLambda0 implements IntConsumer {
    public final /* synthetic */ Parcel f$0;
    public final /* synthetic */ Map f$1;

    public /* synthetic */ ISoftApCallback$Stub$$ExternalSyntheticLambda0(Parcel parcel, Map map) {
        this.f$0 = parcel;
        this.f$1 = map;
    }

    public final void accept(int i) {
        this.f$1.put(this.f$0.readString(), (SoftApInfo) this.f$0.readTypedObject(SoftApInfo.CREATOR));
    }
}
