package android.app.usage;

import android.app.usage.NetworkStatsManager;
import android.net.DataUsageRequest;

/* renamed from: android.app.usage.NetworkStatsManager$UsageCallbackWrapper$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0003xf2514dc4 implements Runnable {
    public final /* synthetic */ NetworkStatsManager.UsageCallback f$0;
    public final /* synthetic */ DataUsageRequest f$1;

    public /* synthetic */ C0003xf2514dc4(NetworkStatsManager.UsageCallback usageCallback, DataUsageRequest dataUsageRequest) {
        this.f$0 = usageCallback;
        this.f$1 = dataUsageRequest;
    }

    public final void run() {
        this.f$0.onThresholdReached(this.f$1.template);
    }
}
