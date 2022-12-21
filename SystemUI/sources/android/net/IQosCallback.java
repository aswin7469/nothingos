package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.data.EpsBearerQosSessionAttributes;
import android.telephony.data.NrQosSessionAttributes;

public interface IQosCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.IQosCallback";

    public static class Default implements IQosCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onError(int i) throws RemoteException {
        }

        public void onNrQosSessionAvailable(QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) throws RemoteException {
        }

        public void onQosEpsBearerSessionAvailable(QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) throws RemoteException {
        }

        public void onQosSessionLost(QosSession qosSession) throws RemoteException {
        }
    }

    void onError(int i) throws RemoteException;

    void onNrQosSessionAvailable(QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) throws RemoteException;

    void onQosEpsBearerSessionAvailable(QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) throws RemoteException;

    void onQosSessionLost(QosSession qosSession) throws RemoteException;

    public static abstract class Stub extends Binder implements IQosCallback {
        static final int TRANSACTION_onError = 4;
        static final int TRANSACTION_onNrQosSessionAvailable = 2;
        static final int TRANSACTION_onQosEpsBearerSessionAvailable = 1;
        static final int TRANSACTION_onQosSessionLost = 3;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onQosEpsBearerSessionAvailable";
            }
            if (i == 2) {
                return "onNrQosSessionAvailable";
            }
            if (i == 3) {
                return "onQosSessionLost";
            }
            if (i != 4) {
                return null;
            }
            return "onError";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 3;
        }

        public Stub() {
            attachInterface(this, IQosCallback.DESCRIPTOR);
        }

        public static IQosCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IQosCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IQosCallback)) {
                return new Proxy(iBinder);
            }
            return (IQosCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IQosCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onQosEpsBearerSessionAvailable((QosSession) parcel.readTypedObject(QosSession.CREATOR), (EpsBearerQosSessionAttributes) parcel.readTypedObject(EpsBearerQosSessionAttributes.CREATOR));
                } else if (i == 2) {
                    onNrQosSessionAvailable((QosSession) parcel.readTypedObject(QosSession.CREATOR), (NrQosSessionAttributes) parcel.readTypedObject(NrQosSessionAttributes.CREATOR));
                } else if (i == 3) {
                    onQosSessionLost((QosSession) parcel.readTypedObject(QosSession.CREATOR));
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onError(parcel.readInt());
                }
                return true;
            }
            parcel2.writeString(IQosCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IQosCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IQosCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onQosEpsBearerSessionAvailable(QosSession qosSession, EpsBearerQosSessionAttributes epsBearerQosSessionAttributes) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IQosCallback.DESCRIPTOR);
                    obtain.writeTypedObject(qosSession, 0);
                    obtain.writeTypedObject(epsBearerQosSessionAttributes, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onNrQosSessionAvailable(QosSession qosSession, NrQosSessionAttributes nrQosSessionAttributes) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IQosCallback.DESCRIPTOR);
                    obtain.writeTypedObject(qosSession, 0);
                    obtain.writeTypedObject(nrQosSessionAttributes, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onQosSessionLost(QosSession qosSession) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IQosCallback.DESCRIPTOR);
                    obtain.writeTypedObject(qosSession, 0);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onError(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IQosCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
