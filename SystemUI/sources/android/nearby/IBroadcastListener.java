package android.nearby;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBroadcastListener extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.IBroadcastListener";

    public static class Default implements IBroadcastListener {
        public IBinder asBinder() {
            return null;
        }

        public void onStatusChanged(int i) throws RemoteException {
        }
    }

    void onStatusChanged(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IBroadcastListener {
        static final int TRANSACTION_onStatusChanged = 1;

        public static String getDefaultTransactionName(int i) {
            if (i != 1) {
                return null;
            }
            return "onStatusChanged";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 0;
        }

        public Stub() {
            attachInterface(this, IBroadcastListener.DESCRIPTOR);
        }

        public static IBroadcastListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IBroadcastListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IBroadcastListener)) {
                return new Proxy(iBinder);
            }
            return (IBroadcastListener) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IBroadcastListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IBroadcastListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                int readInt = parcel.readInt();
                parcel.enforceNoDataAvail();
                onStatusChanged(readInt);
                return true;
            }
        }

        private static class Proxy implements IBroadcastListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IBroadcastListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onStatusChanged(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBroadcastListener.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
