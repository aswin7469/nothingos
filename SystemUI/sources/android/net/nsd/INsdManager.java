package android.net.nsd;

import android.net.nsd.INsdManagerCallback;
import android.net.nsd.INsdServiceConnector;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import sun.security.util.SecurityConstants;

public interface INsdManager extends IInterface {

    public static class Default implements INsdManager {
        public IBinder asBinder() {
            return null;
        }

        public INsdServiceConnector connect(INsdManagerCallback iNsdManagerCallback) throws RemoteException {
            return null;
        }
    }

    INsdServiceConnector connect(INsdManagerCallback iNsdManagerCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements INsdManager {
        public static final String DESCRIPTOR = "android.net.nsd.INsdManager";
        static final int TRANSACTION_connect = 1;

        public static String getDefaultTransactionName(int i) {
            if (i != 1) {
                return null;
            }
            return SecurityConstants.SOCKET_CONNECT_ACTION;
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 0;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INsdManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INsdManager)) {
                return new Proxy(iBinder);
            }
            return (INsdManager) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                INsdManagerCallback asInterface = INsdManagerCallback.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                INsdServiceConnector connect = connect(asInterface);
                parcel2.writeNoException();
                parcel2.writeStrongInterface(connect);
                return true;
            }
        }

        private static class Proxy implements INsdManager {
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

            public INsdServiceConnector connect(INsdManagerCallback iNsdManagerCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongInterface(iNsdManagerCallback);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return INsdServiceConnector.Stub.asInterface(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
