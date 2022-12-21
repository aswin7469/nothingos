package android.app;

import android.app.StatsManager;
import android.p000os.IPullAtomResultReceiver;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class StatsManager$PullAtomCallbackInternal$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ StatsManager.PullAtomCallbackInternal f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ IPullAtomResultReceiver f$2;

    public /* synthetic */ StatsManager$PullAtomCallbackInternal$$ExternalSyntheticLambda0(StatsManager.PullAtomCallbackInternal pullAtomCallbackInternal, int i, IPullAtomResultReceiver iPullAtomResultReceiver) {
        this.f$0 = pullAtomCallbackInternal;
        this.f$1 = i;
        this.f$2 = iPullAtomResultReceiver;
    }

    public final void run() {
        this.f$0.mo20x46f7e6de(this.f$1, this.f$2);
    }
}
