package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IIntResultListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.IIntResultListener";

    public static class Default implements IIntResultListener {
        public IBinder asBinder() {
            return null;
        }

        public void onResult(int i) throws RemoteException {
        }
    }

    void onResult(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IIntResultListener {
        static final int TRANSACTION_onResult = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IIntResultListener.DESCRIPTOR);
        }

        public static IIntResultListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IIntResultListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IIntResultListener)) {
                return new Proxy(iBinder);
            }
            return (IIntResultListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IIntResultListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IIntResultListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onResult(parcel.readInt());
                return true;
            }
        }

        private static class Proxy implements IIntResultListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IIntResultListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onResult(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IIntResultListener.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
