package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITetheredInterfaceCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.ITetheredInterfaceCallback";

    public static class Default implements ITetheredInterfaceCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onAvailable(String str) throws RemoteException {
        }

        public void onUnavailable() throws RemoteException {
        }
    }

    void onAvailable(String str) throws RemoteException;

    void onUnavailable() throws RemoteException;

    public static abstract class Stub extends Binder implements ITetheredInterfaceCallback {
        static final int TRANSACTION_onAvailable = 1;
        static final int TRANSACTION_onUnavailable = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onAvailable";
            }
            if (i != 2) {
                return null;
            }
            return "onUnavailable";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 1;
        }

        public Stub() {
            attachInterface(this, ITetheredInterfaceCallback.DESCRIPTOR);
        }

        public static ITetheredInterfaceCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ITetheredInterfaceCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ITetheredInterfaceCallback)) {
                return new Proxy(iBinder);
            }
            return (ITetheredInterfaceCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ITetheredInterfaceCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    String readString = parcel.readString();
                    parcel.enforceNoDataAvail();
                    onAvailable(readString);
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onUnavailable();
                }
                return true;
            }
            parcel2.writeString(ITetheredInterfaceCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ITetheredInterfaceCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ITetheredInterfaceCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onAvailable(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheredInterfaceCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onUnavailable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ITetheredInterfaceCallback.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
