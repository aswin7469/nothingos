package android.net.wifi;

import android.os.Parcel;
import java.util.function.Consumer;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiConfiguration$$ExternalSyntheticLambda11 implements Consumer {
    public final /* synthetic */ Parcel f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ WifiConfiguration$$ExternalSyntheticLambda11(Parcel parcel, int i) {
        this.f$0 = parcel;
        this.f$1 = i;
    }

    public final void accept(Object obj) {
        this.f$0.writeParcelable((SecurityParams) obj, this.f$1);
    }
}
