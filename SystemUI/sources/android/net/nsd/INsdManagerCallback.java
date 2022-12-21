package android.net.nsd;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INsdManagerCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.nsd.INsdManagerCallback";

    public static class Default implements INsdManagerCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onDiscoverServicesFailed(int i, int i2) throws RemoteException {
        }

        public void onDiscoverServicesStarted(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
        }

        public void onRegisterServiceFailed(int i, int i2) throws RemoteException {
        }

        public void onRegisterServiceSucceeded(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
        }

        public void onResolveServiceFailed(int i, int i2) throws RemoteException {
        }

        public void onResolveServiceSucceeded(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
        }

        public void onServiceFound(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
        }

        public void onServiceLost(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
        }

        public void onStopDiscoveryFailed(int i, int i2) throws RemoteException {
        }

        public void onStopDiscoverySucceeded(int i) throws RemoteException {
        }

        public void onUnregisterServiceFailed(int i, int i2) throws RemoteException {
        }

        public void onUnregisterServiceSucceeded(int i) throws RemoteException {
        }
    }

    void onDiscoverServicesFailed(int i, int i2) throws RemoteException;

    void onDiscoverServicesStarted(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException;

    void onRegisterServiceFailed(int i, int i2) throws RemoteException;

    void onRegisterServiceSucceeded(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException;

    void onResolveServiceFailed(int i, int i2) throws RemoteException;

    void onResolveServiceSucceeded(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException;

    void onServiceFound(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException;

    void onServiceLost(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException;

    void onStopDiscoveryFailed(int i, int i2) throws RemoteException;

    void onStopDiscoverySucceeded(int i) throws RemoteException;

    void onUnregisterServiceFailed(int i, int i2) throws RemoteException;

    void onUnregisterServiceSucceeded(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements INsdManagerCallback {
        static final int TRANSACTION_onDiscoverServicesFailed = 2;
        static final int TRANSACTION_onDiscoverServicesStarted = 1;
        static final int TRANSACTION_onRegisterServiceFailed = 7;
        static final int TRANSACTION_onRegisterServiceSucceeded = 8;
        static final int TRANSACTION_onResolveServiceFailed = 11;
        static final int TRANSACTION_onResolveServiceSucceeded = 12;
        static final int TRANSACTION_onServiceFound = 3;
        static final int TRANSACTION_onServiceLost = 4;
        static final int TRANSACTION_onStopDiscoveryFailed = 5;
        static final int TRANSACTION_onStopDiscoverySucceeded = 6;
        static final int TRANSACTION_onUnregisterServiceFailed = 9;
        static final int TRANSACTION_onUnregisterServiceSucceeded = 10;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "onDiscoverServicesStarted";
                case 2:
                    return "onDiscoverServicesFailed";
                case 3:
                    return "onServiceFound";
                case 4:
                    return "onServiceLost";
                case 5:
                    return "onStopDiscoveryFailed";
                case 6:
                    return "onStopDiscoverySucceeded";
                case 7:
                    return "onRegisterServiceFailed";
                case 8:
                    return "onRegisterServiceSucceeded";
                case 9:
                    return "onUnregisterServiceFailed";
                case 10:
                    return "onUnregisterServiceSucceeded";
                case 11:
                    return "onResolveServiceFailed";
                case 12:
                    return "onResolveServiceSucceeded";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 11;
        }

        public Stub() {
            attachInterface(this, INsdManagerCallback.DESCRIPTOR);
        }

        public static INsdManagerCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INsdManagerCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INsdManagerCallback)) {
                return new Proxy(iBinder);
            }
            return (INsdManagerCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INsdManagerCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceNoDataAvail();
                        onDiscoverServicesStarted(parcel.readInt(), (NsdServiceInfo) parcel.readTypedObject(NsdServiceInfo.CREATOR));
                        break;
                    case 2:
                        int readInt = parcel.readInt();
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onDiscoverServicesFailed(readInt, readInt2);
                        break;
                    case 3:
                        parcel.enforceNoDataAvail();
                        onServiceFound(parcel.readInt(), (NsdServiceInfo) parcel.readTypedObject(NsdServiceInfo.CREATOR));
                        break;
                    case 4:
                        parcel.enforceNoDataAvail();
                        onServiceLost(parcel.readInt(), (NsdServiceInfo) parcel.readTypedObject(NsdServiceInfo.CREATOR));
                        break;
                    case 5:
                        int readInt3 = parcel.readInt();
                        int readInt4 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onStopDiscoveryFailed(readInt3, readInt4);
                        break;
                    case 6:
                        int readInt5 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onStopDiscoverySucceeded(readInt5);
                        break;
                    case 7:
                        int readInt6 = parcel.readInt();
                        int readInt7 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onRegisterServiceFailed(readInt6, readInt7);
                        break;
                    case 8:
                        parcel.enforceNoDataAvail();
                        onRegisterServiceSucceeded(parcel.readInt(), (NsdServiceInfo) parcel.readTypedObject(NsdServiceInfo.CREATOR));
                        break;
                    case 9:
                        int readInt8 = parcel.readInt();
                        int readInt9 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onUnregisterServiceFailed(readInt8, readInt9);
                        break;
                    case 10:
                        int readInt10 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onUnregisterServiceSucceeded(readInt10);
                        break;
                    case 11:
                        int readInt11 = parcel.readInt();
                        int readInt12 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onResolveServiceFailed(readInt11, readInt12);
                        break;
                    case 12:
                        parcel.enforceNoDataAvail();
                        onResolveServiceSucceeded(parcel.readInt(), (NsdServiceInfo) parcel.readTypedObject(NsdServiceInfo.CREATOR));
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(INsdManagerCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INsdManagerCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INsdManagerCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onDiscoverServicesStarted(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nsdServiceInfo, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onDiscoverServicesFailed(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onServiceFound(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nsdServiceInfo, 0);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onServiceLost(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nsdServiceInfo, 0);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStopDiscoveryFailed(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStopDiscoverySucceeded(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRegisterServiceFailed(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRegisterServiceSucceeded(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nsdServiceInfo, 0);
                    this.mRemote.transact(8, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onUnregisterServiceFailed(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(9, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onUnregisterServiceSucceeded(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onResolveServiceFailed(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(11, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onResolveServiceSucceeded(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdManagerCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nsdServiceInfo, 0);
                    this.mRemote.transact(12, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
