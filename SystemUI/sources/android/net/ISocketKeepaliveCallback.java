package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISocketKeepaliveCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.ISocketKeepaliveCallback";

    public static class Default implements ISocketKeepaliveCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onDataReceived() throws RemoteException {
        }

        public void onError(int i) throws RemoteException {
        }

        public void onStarted(int i) throws RemoteException {
        }

        public void onStopped() throws RemoteException {
        }
    }

    void onDataReceived() throws RemoteException;

    void onError(int i) throws RemoteException;

    void onStarted(int i) throws RemoteException;

    void onStopped() throws RemoteException;

    public static abstract class Stub extends Binder implements ISocketKeepaliveCallback {
        static final int TRANSACTION_onDataReceived = 4;
        static final int TRANSACTION_onError = 3;
        static final int TRANSACTION_onStarted = 1;
        static final int TRANSACTION_onStopped = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onStarted";
            }
            if (i == 2) {
                return "onStopped";
            }
            if (i == 3) {
                return "onError";
            }
            if (i != 4) {
                return null;
            }
            return "onDataReceived";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 3;
        }

        public Stub() {
            attachInterface(this, ISocketKeepaliveCallback.DESCRIPTOR);
        }

        public static ISocketKeepaliveCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISocketKeepaliveCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISocketKeepaliveCallback)) {
                return new Proxy(iBinder);
            }
            return (ISocketKeepaliveCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ISocketKeepaliveCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onStarted(parcel.readInt());
                } else if (i == 2) {
                    onStopped();
                } else if (i == 3) {
                    onError(parcel.readInt());
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onDataReceived();
                }
                return true;
            }
            parcel2.writeString(ISocketKeepaliveCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ISocketKeepaliveCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ISocketKeepaliveCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onStarted(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISocketKeepaliveCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStopped() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISocketKeepaliveCallback.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onError(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISocketKeepaliveCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onDataReceived() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISocketKeepaliveCallback.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
