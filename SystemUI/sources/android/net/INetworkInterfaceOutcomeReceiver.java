package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkInterfaceOutcomeReceiver extends IInterface {
    public static final String DESCRIPTOR = "android.net.INetworkInterfaceOutcomeReceiver";

    public static class Default implements INetworkInterfaceOutcomeReceiver {
        public IBinder asBinder() {
            return null;
        }

        public void onError(EthernetNetworkManagementException ethernetNetworkManagementException) throws RemoteException {
        }

        public void onResult(String str) throws RemoteException {
        }
    }

    void onError(EthernetNetworkManagementException ethernetNetworkManagementException) throws RemoteException;

    void onResult(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkInterfaceOutcomeReceiver {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onResult = 1;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onResult";
            }
            if (i != 2) {
                return null;
            }
            return "onError";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 1;
        }

        public Stub() {
            attachInterface(this, INetworkInterfaceOutcomeReceiver.DESCRIPTOR);
        }

        public static INetworkInterfaceOutcomeReceiver asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkInterfaceOutcomeReceiver.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkInterfaceOutcomeReceiver)) {
                return new Proxy(iBinder);
            }
            return (INetworkInterfaceOutcomeReceiver) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkInterfaceOutcomeReceiver.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    String readString = parcel.readString();
                    parcel.enforceNoDataAvail();
                    onResult(readString);
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceNoDataAvail();
                    onError((EthernetNetworkManagementException) parcel.readTypedObject(EthernetNetworkManagementException.CREATOR));
                }
                return true;
            }
            parcel2.writeString(INetworkInterfaceOutcomeReceiver.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkInterfaceOutcomeReceiver {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkInterfaceOutcomeReceiver.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onResult(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkInterfaceOutcomeReceiver.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onError(EthernetNetworkManagementException ethernetNetworkManagementException) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkInterfaceOutcomeReceiver.DESCRIPTOR);
                    obtain.writeTypedObject(ethernetNetworkManagementException, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
