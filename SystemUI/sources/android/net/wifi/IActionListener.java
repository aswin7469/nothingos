package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IActionListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IActionListener";

    public static class Default implements IActionListener {
        public IBinder asBinder() {
            return null;
        }

        public void onFailure(int i) throws RemoteException {
        }

        public void onSuccess() throws RemoteException {
        }
    }

    void onFailure(int i) throws RemoteException;

    void onSuccess() throws RemoteException;

    public static abstract class Stub extends Binder implements IActionListener {
        static final int TRANSACTION_onFailure = 2;
        static final int TRANSACTION_onSuccess = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IActionListener.DESCRIPTOR);
        }

        public static IActionListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IActionListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IActionListener)) {
                return new Proxy(iBinder);
            }
            return (IActionListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IActionListener.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onSuccess();
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onFailure(parcel.readInt());
                }
                return true;
            }
            parcel2.writeString(IActionListener.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IActionListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IActionListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onSuccess() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IActionListener.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onFailure(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IActionListener.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
