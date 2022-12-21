package android.net;

import android.telephony.data.NrQosSessionAttributes;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QosCallbackConnection$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ QosCallbackConnection f$0;
    public final /* synthetic */ QosSession f$1;
    public final /* synthetic */ NrQosSessionAttributes f$2;

    public /* synthetic */ QosCallbackConnection$$ExternalSyntheticLambda3(QosCallbackConnection qosCallbackConnection, QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) {
        this.f$0 = qosCallbackConnection;
        this.f$1 = qosSession;
        this.f$2 = nrQosSessionAttributes;
    }

    public final void run() {
        this.f$0.mo3323xb3f519b(this.f$1, this.f$2);
    }
}
