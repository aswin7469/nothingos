package android.net;

import android.net.IQosCallback;
import android.telephony.data.EpsBearerQosSessionAttributes;
import android.telephony.data.NrQosSessionAttributes;
import java.util.Objects;
import java.util.concurrent.Executor;

class QosCallbackConnection extends IQosCallback.Stub {
    private volatile QosCallback mCallback;
    private final ConnectivityManager mConnectivityManager;
    private final Executor mExecutor;

    public QosCallback getCallback() {
        return this.mCallback;
    }

    QosCallbackConnection(ConnectivityManager connectivityManager, QosCallback qosCallback, Executor executor) {
        this.mConnectivityManager = (ConnectivityManager) Objects.requireNonNull(connectivityManager, "connectivityManager must be non-null");
        this.mCallback = (QosCallback) Objects.requireNonNull(qosCallback, "callback must be non-null");
        this.mExecutor = (Executor) Objects.requireNonNull(executor, "executor must be non-null");
    }

    public void onQosEpsBearerSessionAvailable(QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) {
        this.mExecutor.execute(new QosCallbackConnection$$ExternalSyntheticLambda2(this, qosSession, epsBearerQosSessionAttributes));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onQosEpsBearerSessionAvailable$0$android-net-QosCallbackConnection */
    public /* synthetic */ void mo3324xd4332d35(QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) {
        QosCallback qosCallback = this.mCallback;
        if (qosCallback != null) {
            qosCallback.onQosSessionAvailable(qosSession, epsBearerQosSessionAttributes);
        }
    }

    public void onNrQosSessionAvailable(QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) {
        this.mExecutor.execute(new QosCallbackConnection$$ExternalSyntheticLambda3(this, qosSession, nrQosSessionAttributes));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onNrQosSessionAvailable$1$android-net-QosCallbackConnection */
    public /* synthetic */ void mo3323xb3f519b(QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) {
        QosCallback qosCallback = this.mCallback;
        if (qosCallback != null) {
            qosCallback.onQosSessionAvailable(qosSession, nrQosSessionAttributes);
        }
    }

    public void onQosSessionLost(QosSession qosSession) {
        this.mExecutor.execute(new QosCallbackConnection$$ExternalSyntheticLambda1(this, qosSession));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onQosSessionLost$2$android-net-QosCallbackConnection  reason: not valid java name */
    public /* synthetic */ void m1930lambda$onQosSessionLost$2$androidnetQosCallbackConnection(QosSession qosSession) {
        QosCallback qosCallback = this.mCallback;
        if (qosCallback != null) {
            qosCallback.onQosSessionLost(qosSession);
        }
    }

    public void onError(int i) {
        this.mExecutor.execute(new QosCallbackConnection$$ExternalSyntheticLambda0(this, i));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onError$3$android-net-QosCallbackConnection  reason: not valid java name */
    public /* synthetic */ void m1929lambda$onError$3$androidnetQosCallbackConnection(int i) {
        QosCallback qosCallback = this.mCallback;
        if (qosCallback != null) {
            stopReceivingMessages();
            this.mConnectivityManager.unregisterQosCallback(qosCallback);
            qosCallback.onError(QosCallbackException.createException(i));
        }
    }

    /* access modifiers changed from: package-private */
    public void stopReceivingMessages() {
        this.mCallback = null;
    }
}
