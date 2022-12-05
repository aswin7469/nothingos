package android.telephony.ims;

import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.telephony.ims.RegistrationManager;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes3.dex */
public interface RegistrationManager {
    public static final Map<Integer, Integer> IMS_REG_TO_ACCESS_TYPE_MAP = new HashMap<Integer, Integer>() { // from class: android.telephony.ims.RegistrationManager.1
        {
            put(-1, -1);
            put(0, 1);
            put(3, 1);
            put(1, 2);
            put(2, 2);
        }
    };
    public static final int REGISTRATION_STATE_NOT_REGISTERED = 0;
    public static final int REGISTRATION_STATE_REGISTERED = 2;
    public static final int REGISTRATION_STATE_REGISTERING = 1;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    public @interface ImsRegistrationState {
    }

    void getRegistrationState(Executor executor, Consumer<Integer> consumer);

    void getRegistrationTransportType(Executor executor, Consumer<Integer> consumer);

    void registerImsRegistrationCallback(Executor executor, RegistrationCallback registrationCallback) throws ImsException;

    void unregisterImsRegistrationCallback(RegistrationCallback registrationCallback);

    static String registrationStateToString(int value) {
        switch (value) {
            case 0:
                return "REGISTRATION_STATE_NOT_REGISTERED";
            case 1:
                return "REGISTRATION_STATE_REGISTERING";
            case 2:
                return "REGISTRATION_STATE_REGISTERED";
            default:
                return Integer.toString(value);
        }
    }

    static int getAccessType(int regtech) {
        Map<Integer, Integer> map = IMS_REG_TO_ACCESS_TYPE_MAP;
        if (!map.containsKey(Integer.valueOf(regtech))) {
            Log.w("RegistrationManager", "getAccessType - invalid regType returned: " + regtech);
            return -1;
        }
        return map.get(Integer.valueOf(regtech)).intValue();
    }

    /* loaded from: classes3.dex */
    public static class RegistrationCallback {
        private final RegistrationBinder mBinder = new RegistrationBinder(this);

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class RegistrationBinder extends IImsRegistrationCallback.Stub {
            private Bundle mBundle = new Bundle();
            private Executor mExecutor;
            private final RegistrationCallback mLocalCallback;

            RegistrationBinder(RegistrationCallback localCallback) {
                this.mLocalCallback = localCallback;
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onRegistered(final ImsRegistrationAttributes attr) {
                if (this.mLocalCallback == null) {
                    return;
                }
                long callingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.RegistrationManager$RegistrationCallback$RegistrationBinder$$ExternalSyntheticLambda2
                        @Override // java.lang.Runnable
                        public final void run() {
                            RegistrationManager.RegistrationCallback.RegistrationBinder.this.lambda$onRegistered$0$RegistrationManager$RegistrationCallback$RegistrationBinder(attr);
                        }
                    });
                } finally {
                    restoreCallingIdentity(callingIdentity);
                }
            }

            public /* synthetic */ void lambda$onRegistered$0$RegistrationManager$RegistrationCallback$RegistrationBinder(ImsRegistrationAttributes attr) {
                this.mLocalCallback.onRegistered(attr);
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onRegistering(final ImsRegistrationAttributes attr) {
                if (this.mLocalCallback == null) {
                    return;
                }
                long callingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.RegistrationManager$RegistrationCallback$RegistrationBinder$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            RegistrationManager.RegistrationCallback.RegistrationBinder.this.lambda$onRegistering$1$RegistrationManager$RegistrationCallback$RegistrationBinder(attr);
                        }
                    });
                } finally {
                    restoreCallingIdentity(callingIdentity);
                }
            }

            public /* synthetic */ void lambda$onRegistering$1$RegistrationManager$RegistrationCallback$RegistrationBinder(ImsRegistrationAttributes attr) {
                this.mLocalCallback.onRegistering(attr);
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onDeregistered(final ImsReasonInfo info) {
                if (this.mLocalCallback == null) {
                    return;
                }
                long callingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.RegistrationManager$RegistrationCallback$RegistrationBinder$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            RegistrationManager.RegistrationCallback.RegistrationBinder.this.lambda$onDeregistered$2$RegistrationManager$RegistrationCallback$RegistrationBinder(info);
                        }
                    });
                } finally {
                    restoreCallingIdentity(callingIdentity);
                }
            }

            public /* synthetic */ void lambda$onDeregistered$2$RegistrationManager$RegistrationCallback$RegistrationBinder(ImsReasonInfo info) {
                this.mLocalCallback.onUnregistered(info);
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onTechnologyChangeFailed(final int imsRadioTech, final ImsReasonInfo info) {
                if (this.mLocalCallback == null) {
                    return;
                }
                long callingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.RegistrationManager$RegistrationCallback$RegistrationBinder$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            RegistrationManager.RegistrationCallback.RegistrationBinder.this.lambda$onTechnologyChangeFailed$3$RegistrationManager$RegistrationCallback$RegistrationBinder(imsRadioTech, info);
                        }
                    });
                } finally {
                    restoreCallingIdentity(callingIdentity);
                }
            }

            public /* synthetic */ void lambda$onTechnologyChangeFailed$3$RegistrationManager$RegistrationCallback$RegistrationBinder(int imsRadioTech, ImsReasonInfo info) {
                this.mLocalCallback.onTechnologyChangeFailed(RegistrationManager.getAccessType(imsRadioTech), info);
            }

            @Override // android.telephony.ims.aidl.IImsRegistrationCallback
            public void onSubscriberAssociatedUriChanged(final Uri[] uris) {
                if (this.mLocalCallback == null) {
                    return;
                }
                long callingIdentity = Binder.clearCallingIdentity();
                try {
                    this.mExecutor.execute(new Runnable() { // from class: android.telephony.ims.RegistrationManager$RegistrationCallback$RegistrationBinder$$ExternalSyntheticLambda4
                        @Override // java.lang.Runnable
                        public final void run() {
                            RegistrationManager.RegistrationCallback.RegistrationBinder.this.lambda$onSubscriberAssociatedUriChanged$4$RegistrationManager$RegistrationCallback$RegistrationBinder(uris);
                        }
                    });
                } finally {
                    restoreCallingIdentity(callingIdentity);
                }
            }

            public /* synthetic */ void lambda$onSubscriberAssociatedUriChanged$4$RegistrationManager$RegistrationCallback$RegistrationBinder(Uri[] uris) {
                this.mLocalCallback.onSubscriberAssociatedUriChanged(uris);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void setExecutor(Executor executor) {
                this.mExecutor = executor;
            }
        }

        @Deprecated
        public void onRegistered(int imsTransportType) {
        }

        public void onRegistered(ImsRegistrationAttributes attributes) {
            onRegistered(attributes.getTransportType());
        }

        public void onRegistering(int imsTransportType) {
        }

        public void onRegistering(ImsRegistrationAttributes attributes) {
            onRegistering(attributes.getTransportType());
        }

        public void onUnregistered(ImsReasonInfo info) {
        }

        public void onTechnologyChangeFailed(int imsTransportType, ImsReasonInfo info) {
        }

        public void onSubscriberAssociatedUriChanged(Uri[] uris) {
        }

        public final IImsRegistrationCallback getBinder() {
            return this.mBinder;
        }

        public void setExecutor(Executor executor) {
            this.mBinder.setExecutor(executor);
        }
    }
}
