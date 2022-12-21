package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITrafficStateCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.ITrafficStateCallback";

    public static class Default implements ITrafficStateCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onStateChanged(int i) throws RemoteException {
        }
    }

    void onStateChanged(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ITrafficStateCallback {
        static final int TRANSACTION_onStateChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ITrafficStateCallback.DESCRIPTOR);
        }

        public static ITrafficStateCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ITrafficStateCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ITrafficStateCallback)) {
                return new Proxy(iBinder);
            }
            return (ITrafficStateCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ITrafficStateCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(ITrafficStateCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onStateChanged(parcel.readInt());
                return true;
            }
        }

        private static class Proxy implements ITrafficStateCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ITrafficStateCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onStateChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITrafficStateCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
