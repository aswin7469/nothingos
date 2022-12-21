package android.net;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class QosCallbackConnection$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ QosCallbackConnection f$0;
    public final /* synthetic */ QosSession f$1;

    public /* synthetic */ QosCallbackConnection$$ExternalSyntheticLambda1(QosCallbackConnection qosCallbackConnection, QosSession qosSession) {
        this.f$0 = qosCallbackConnection;
        this.f$1 = qosSession;
    }

    public final void run() {
        this.f$0.m1930lambda$onQosSessionLost$2$androidnetQosCallbackConnection(this.f$1);
    }
}
