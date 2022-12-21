package android.net.nsd;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INsdServiceConnector extends IInterface {
    public static final String DESCRIPTOR = "android.net.nsd.INsdServiceConnector";

    public static class Default implements INsdServiceConnector {
        public IBinder asBinder() {
            return null;
        }

        public void discoverServices(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
        }

        public void registerService(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
        }

        public void resolveService(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
        }

        public void startDaemon() throws RemoteException {
        }

        public void stopDiscovery(int i) throws RemoteException {
        }

        public void unregisterService(int i) throws RemoteException {
        }
    }

    void discoverServices(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException;

    void registerService(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException;

    void resolveService(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException;

    void startDaemon() throws RemoteException;

    void stopDiscovery(int i) throws RemoteException;

    void unregisterService(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements INsdServiceConnector {
        static final int TRANSACTION_discoverServices = 3;
        static final int TRANSACTION_registerService = 1;
        static final int TRANSACTION_resolveService = 5;
        static final int TRANSACTION_startDaemon = 6;
        static final int TRANSACTION_stopDiscovery = 4;
        static final int TRANSACTION_unregisterService = 2;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "registerService";
                case 2:
                    return "unregisterService";
                case 3:
                    return "discoverServices";
                case 4:
                    return "stopDiscovery";
                case 5:
                    return "resolveService";
                case 6:
                    return "startDaemon";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 5;
        }

        public Stub() {
            attachInterface(this, INsdServiceConnector.DESCRIPTOR);
        }

        public static INsdServiceConnector asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INsdServiceConnector.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INsdServiceConnector)) {
                return new Proxy(iBinder);
            }
            return (INsdServiceConnector) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INsdServiceConnector.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceNoDataAvail();
                        registerService(parcel.readInt(), (NsdServiceInfo) parcel.readTypedObject(NsdServiceInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 2:
                        int readInt = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        unregisterService(readInt);
                        parcel2.writeNoException();
                        break;
                    case 3:
                        parcel.enforceNoDataAvail();
                        discoverServices(parcel.readInt(), (NsdServiceInfo) parcel.readTypedObject(NsdServiceInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 4:
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        stopDiscovery(readInt2);
                        parcel2.writeNoException();
                        break;
                    case 5:
                        parcel.enforceNoDataAvail();
                        resolveService(parcel.readInt(), (NsdServiceInfo) parcel.readTypedObject(NsdServiceInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 6:
                        startDaemon();
                        parcel2.writeNoException();
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(INsdServiceConnector.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INsdServiceConnector {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INsdServiceConnector.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void registerService(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdServiceConnector.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nsdServiceInfo, 0);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterService(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdServiceConnector.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void discoverServices(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdServiceConnector.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nsdServiceInfo, 0);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopDiscovery(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdServiceConnector.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void resolveService(int i, NsdServiceInfo nsdServiceInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdServiceConnector.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(nsdServiceInfo, 0);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startDaemon() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INsdServiceConnector.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
