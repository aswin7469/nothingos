package android.net.wifi.hotspot2;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IProvisioningCallback extends IInterface {

    public static class Default implements IProvisioningCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onProvisioningComplete() throws RemoteException {
        }

        public void onProvisioningFailure(int i) throws RemoteException {
        }

        public void onProvisioningStatus(int i) throws RemoteException {
        }
    }

    void onProvisioningComplete() throws RemoteException;

    void onProvisioningFailure(int i) throws RemoteException;

    void onProvisioningStatus(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IProvisioningCallback {
        public static final String DESCRIPTOR = "android.net.wifi.hotspot2.IProvisioningCallback";
        static final int TRANSACTION_onProvisioningComplete = 3;
        static final int TRANSACTION_onProvisioningFailure = 1;
        static final int TRANSACTION_onProvisioningStatus = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IProvisioningCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IProvisioningCallback)) {
                return new Proxy(iBinder);
            }
            return (IProvisioningCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onProvisioningFailure(parcel.readInt());
                } else if (i == 2) {
                    onProvisioningStatus(parcel.readInt());
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onProvisioningComplete();
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IProvisioningCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onProvisioningFailure(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onProvisioningStatus(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onProvisioningComplete() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
