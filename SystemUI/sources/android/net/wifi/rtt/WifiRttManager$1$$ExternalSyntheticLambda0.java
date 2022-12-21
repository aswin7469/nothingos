package android.net.wifi.rtt;

import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiRttManager$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ RangingResultCallback f$0;
    public final /* synthetic */ List f$1;

    public /* synthetic */ WifiRttManager$1$$ExternalSyntheticLambda0(RangingResultCallback rangingResultCallback, List list) {
        this.f$0 = rangingResultCallback;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.onRangingResults(this.f$1);
    }
}
