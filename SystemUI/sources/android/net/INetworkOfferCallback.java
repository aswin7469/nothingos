package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkOfferCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.INetworkOfferCallback";

    public static class Default implements INetworkOfferCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onNetworkNeeded(NetworkRequest networkRequest) throws RemoteException {
        }

        public void onNetworkUnneeded(NetworkRequest networkRequest) throws RemoteException {
        }
    }

    void onNetworkNeeded(NetworkRequest networkRequest) throws RemoteException;

    void onNetworkUnneeded(NetworkRequest networkRequest) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkOfferCallback {
        static final int TRANSACTION_onNetworkNeeded = 1;
        static final int TRANSACTION_onNetworkUnneeded = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onNetworkNeeded";
            }
            if (i != 2) {
                return null;
            }
            return "onNetworkUnneeded";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 1;
        }

        public Stub() {
            attachInterface(this, INetworkOfferCallback.DESCRIPTOR);
        }

        public static INetworkOfferCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkOfferCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkOfferCallback)) {
                return new Proxy(iBinder);
            }
            return (INetworkOfferCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkOfferCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onNetworkNeeded((NetworkRequest) parcel.readTypedObject(NetworkRequest.CREATOR));
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onNetworkUnneeded((NetworkRequest) parcel.readTypedObject(NetworkRequest.CREATOR));
                }
                return true;
            }
            parcel2.writeString(INetworkOfferCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkOfferCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkOfferCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onNetworkNeeded(NetworkRequest networkRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkOfferCallback.DESCRIPTOR);
                    obtain.writeTypedObject(networkRequest, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onNetworkUnneeded(NetworkRequest networkRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkOfferCallback.DESCRIPTOR);
                    obtain.writeTypedObject(networkRequest, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
