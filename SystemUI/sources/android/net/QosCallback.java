package android.net;

import android.annotation.SystemApi;

@SystemApi
public abstract class QosCallback {

    public static class QosCallbackRegistrationException extends RuntimeException {
    }

    public void onError(QosCallbackException qosCallbackException) {
    }

    public void onQosSessionAvailable(QosSession qosSession, QosSessionAttributes qosSessionAttributes) {
    }

    public void onQosSessionLost(QosSession qosSession) {
    }
}
