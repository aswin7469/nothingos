package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkRequestUserSelectionCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.INetworkRequestUserSelectionCallback";

    public static class Default implements INetworkRequestUserSelectionCallback {
        public IBinder asBinder() {
            return null;
        }

        public void reject() throws RemoteException {
        }

        public void select(WifiConfiguration wifiConfiguration) throws RemoteException {
        }
    }

    void reject() throws RemoteException;

    void select(WifiConfiguration wifiConfiguration) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkRequestUserSelectionCallback {
        static final int TRANSACTION_reject = 2;
        static final int TRANSACTION_select = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, INetworkRequestUserSelectionCallback.DESCRIPTOR);
        }

        public static INetworkRequestUserSelectionCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkRequestUserSelectionCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkRequestUserSelectionCallback)) {
                return new Proxy(iBinder);
            }
            return (INetworkRequestUserSelectionCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkRequestUserSelectionCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    select((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR));
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    reject();
                }
                return true;
            }
            parcel2.writeString(INetworkRequestUserSelectionCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkRequestUserSelectionCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkRequestUserSelectionCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void select(WifiConfiguration wifiConfiguration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkRequestUserSelectionCallback.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void reject() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkRequestUserSelectionCallback.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
