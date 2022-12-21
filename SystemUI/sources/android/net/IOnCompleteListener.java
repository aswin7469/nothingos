package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOnCompleteListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.IOnCompleteListener";

    public static class Default implements IOnCompleteListener {
        public IBinder asBinder() {
            return null;
        }

        public void onComplete() throws RemoteException {
        }
    }

    void onComplete() throws RemoteException;

    public static abstract class Stub extends Binder implements IOnCompleteListener {
        static final int TRANSACTION_onComplete = 1;

        public static String getDefaultTransactionName(int i) {
            if (i != 1) {
                return null;
            }
            return "onComplete";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 0;
        }

        public Stub() {
            attachInterface(this, IOnCompleteListener.DESCRIPTOR);
        }

        public static IOnCompleteListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOnCompleteListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOnCompleteListener)) {
                return new Proxy(iBinder);
            }
            return (IOnCompleteListener) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IOnCompleteListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IOnCompleteListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onComplete();
                return true;
            }
        }

        private static class Proxy implements IOnCompleteListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IOnCompleteListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onComplete() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOnCompleteListener.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
