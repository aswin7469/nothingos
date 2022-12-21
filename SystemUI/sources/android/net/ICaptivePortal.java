package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ICaptivePortal extends IInterface {

    public static class Default implements ICaptivePortal {
        public void appRequest(int i) throws RemoteException {
        }

        public void appResponse(int i) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void appRequest(int i) throws RemoteException;

    void appResponse(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ICaptivePortal {
        public static final String DESCRIPTOR = "android.net.ICaptivePortal";
        static final int TRANSACTION_appRequest = 1;
        static final int TRANSACTION_appResponse = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "appRequest";
            }
            if (i != 2) {
                return null;
            }
            return "appResponse";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 1;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ICaptivePortal asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ICaptivePortal)) {
                return new Proxy(iBinder);
            }
            return (ICaptivePortal) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    appRequest(parcel.readInt());
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    appResponse(parcel.readInt());
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ICaptivePortal {
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

            public void appRequest(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void appResponse(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
