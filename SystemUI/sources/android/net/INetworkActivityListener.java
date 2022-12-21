package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkActivityListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.INetworkActivityListener";

    public static class Default implements INetworkActivityListener {
        public IBinder asBinder() {
            return null;
        }

        public void onNetworkActive() throws RemoteException {
        }
    }

    void onNetworkActive() throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkActivityListener {
        static final int TRANSACTION_onNetworkActive = 1;

        public static String getDefaultTransactionName(int i) {
            if (i != 1) {
                return null;
            }
            return "onNetworkActive";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 0;
        }

        public Stub() {
            attachInterface(this, INetworkActivityListener.DESCRIPTOR);
        }

        public static INetworkActivityListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkActivityListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkActivityListener)) {
                return new Proxy(iBinder);
            }
            return (INetworkActivityListener) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkActivityListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(INetworkActivityListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onNetworkActive();
                return true;
            }
        }

        private static class Proxy implements INetworkActivityListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkActivityListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onNetworkActive() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkActivityListener.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
