package android.net;

import android.os.ConditionVariable;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NetworkAgent$$ExternalSyntheticLambda7 implements Runnable {
    public final /* synthetic */ ConditionVariable f$0;

    public /* synthetic */ NetworkAgent$$ExternalSyntheticLambda7(ConditionVariable conditionVariable) {
        this.f$0 = conditionVariable;
    }

    public final void run() {
        this.f$0.open();
    }
}
