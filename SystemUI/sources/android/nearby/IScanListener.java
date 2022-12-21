package android.nearby;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IScanListener extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.IScanListener";

    public static class Default implements IScanListener {
        public IBinder asBinder() {
            return null;
        }

        public void onDiscovered(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
        }

        public void onError() throws RemoteException {
        }

        public void onLost(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
        }

        public void onUpdated(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
        }
    }

    void onDiscovered(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException;

    void onError() throws RemoteException;

    void onLost(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException;

    void onUpdated(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException;

    public static abstract class Stub extends Binder implements IScanListener {
        static final int TRANSACTION_onDiscovered = 1;
        static final int TRANSACTION_onError = 4;
        static final int TRANSACTION_onLost = 3;
        static final int TRANSACTION_onUpdated = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onDiscovered";
            }
            if (i == 2) {
                return "onUpdated";
            }
            if (i == 3) {
                return "onLost";
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
            attachInterface(this, IScanListener.DESCRIPTOR);
        }

        public static IScanListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IScanListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IScanListener)) {
                return new Proxy(iBinder);
            }
            return (IScanListener) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IScanListener.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    parcel.enforceNoDataAvail();
                    onDiscovered((NearbyDeviceParcelable) parcel.readTypedObject(NearbyDeviceParcelable.CREATOR));
                } else if (i == 2) {
                    parcel.enforceNoDataAvail();
                    onUpdated((NearbyDeviceParcelable) parcel.readTypedObject(NearbyDeviceParcelable.CREATOR));
                } else if (i == 3) {
                    parcel.enforceNoDataAvail();
                    onLost((NearbyDeviceParcelable) parcel.readTypedObject(NearbyDeviceParcelable.CREATOR));
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onError();
                }
                return true;
            }
            parcel2.writeString(IScanListener.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IScanListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IScanListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onDiscovered(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScanListener.DESCRIPTOR);
                    obtain.writeTypedObject(nearbyDeviceParcelable, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onUpdated(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScanListener.DESCRIPTOR);
                    obtain.writeTypedObject(nearbyDeviceParcelable, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onLost(NearbyDeviceParcelable nearbyDeviceParcelable) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScanListener.DESCRIPTOR);
                    obtain.writeTypedObject(nearbyDeviceParcelable, 0);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onError() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScanListener.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
