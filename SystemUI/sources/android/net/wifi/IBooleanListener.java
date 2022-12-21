package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBooleanListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IBooleanListener";

    public static class Default implements IBooleanListener {
        public IBinder asBinder() {
            return null;
        }

        public void onResult(boolean z) throws RemoteException {
        }
    }

    void onResult(boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements IBooleanListener {
        static final int TRANSACTION_onResult = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IBooleanListener.DESCRIPTOR);
        }

        public static IBooleanListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IBooleanListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IBooleanListener)) {
                return new Proxy(iBinder);
            }
            return (IBooleanListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IBooleanListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IBooleanListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onResult(parcel.readBoolean());
                return true;
            }
        }

        private static class Proxy implements IBooleanListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IBooleanListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onResult(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IBooleanListener.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
