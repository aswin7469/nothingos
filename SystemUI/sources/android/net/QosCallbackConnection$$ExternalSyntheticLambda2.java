package android.net;

import android.telephony.data.EpsBearerQosSessionAttributes;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QosCallbackConnection$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ QosCallbackConnection f$0;
    public final /* synthetic */ QosSession f$1;
    public final /* synthetic */ EpsBearerQosSessionAttributes f$2;

    public /* synthetic */ QosCallbackConnection$$ExternalSyntheticLambda2(QosCallbackConnection qosCallbackConnection, QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) {
        this.f$0 = qosCallbackConnection;
        this.f$1 = qosSession;
        this.f$2 = epsBearerQosSessionAttributes;
    }

    public final void run() {
        this.f$0.mo3324xd4332d35(this.f$1, this.f$2);
    }
}
