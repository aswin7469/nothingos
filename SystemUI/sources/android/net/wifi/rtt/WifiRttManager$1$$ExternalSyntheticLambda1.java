package android.net.wifi.rtt;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class WifiRttManager$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ RangingResultCallback f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ WifiRttManager$1$$ExternalSyntheticLambda1(RangingResultCallback rangingResultCallback, int i) {
        this.f$0 = rangingResultCallback;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onRangingFailure(this.f$1);
    }
}
