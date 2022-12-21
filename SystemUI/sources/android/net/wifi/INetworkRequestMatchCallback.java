package android.net.wifi;

import android.net.wifi.INetworkRequestUserSelectionCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface INetworkRequestMatchCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.INetworkRequestMatchCallback";

    public static class Default implements INetworkRequestMatchCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onAbort() throws RemoteException {
        }

        public void onMatch(List<ScanResult> list) throws RemoteException {
        }

        public void onUserSelectionCallbackRegistration(INetworkRequestUserSelectionCallback iNetworkRequestUserSelectionCallback) throws RemoteException {
        }

        public void onUserSelectionConnectFailure(WifiConfiguration wifiConfiguration) throws RemoteException {
        }

        public void onUserSelectionConnectSuccess(WifiConfiguration wifiConfiguration) throws RemoteException {
        }
    }

    void onAbort() throws RemoteException;

    void onMatch(List<ScanResult> list) throws RemoteException;

    void onUserSelectionCallbackRegistration(INetworkRequestUserSelectionCallback iNetworkRequestUserSelectionCallback) throws RemoteException;

    void onUserSelectionConnectFailure(WifiConfiguration wifiConfiguration) throws RemoteException;

    void onUserSelectionConnectSuccess(WifiConfiguration wifiConfiguration) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkRequestMatchCallback {
        static final int TRANSACTION_onAbort = 2;
        static final int TRANSACTION_onMatch = 3;
        static final int TRANSACTION_onUserSelectionCallbackRegistration = 1;
        static final int TRANSACTION_onUserSelectionConnectFailure = 5;
        static final int TRANSACTION_onUserSelectionConnectSuccess = 4;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, INetworkRequestMatchCallback.DESCRIPTOR);
        }

        public static INetworkRequestMatchCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkRequestMatchCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkRequestMatchCallback)) {
                return new Proxy(iBinder);
            }
            return (INetworkRequestMatchCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkRequestMatchCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onUserSelectionCallbackRegistration(INetworkRequestUserSelectionCallback.Stub.asInterface(parcel.readStrongBinder()));
                } else if (i == 2) {
                    onAbort();
                } else if (i == 3) {
                    onMatch(parcel.createTypedArrayList(ScanResult.CREATOR));
                } else if (i == 4) {
                    onUserSelectionConnectSuccess((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR));
                } else if (i != 5) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onUserSelectionConnectFailure((WifiConfiguration) parcel.readTypedObject(WifiConfiguration.CREATOR));
                }
                return true;
            }
            parcel2.writeString(INetworkRequestMatchCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkRequestMatchCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkRequestMatchCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onUserSelectionCallbackRegistration(INetworkRequestUserSelectionCallback iNetworkRequestUserSelectionCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkRequestMatchCallback.DESCRIPTOR);
                    obtain.writeStrongInterface(iNetworkRequestUserSelectionCallback);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAbort() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkRequestMatchCallback.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMatch(List<ScanResult> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkRequestMatchCallback.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onUserSelectionConnectSuccess(WifiConfiguration wifiConfiguration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkRequestMatchCallback.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onUserSelectionConnectFailure(WifiConfiguration wifiConfiguration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkRequestMatchCallback.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConfiguration, 0);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
