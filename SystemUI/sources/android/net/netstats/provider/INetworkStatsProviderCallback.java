package android.net.netstats.provider;

import android.net.NetworkStats;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkStatsProviderCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.netstats.provider.INetworkStatsProviderCallback";

    public static class Default implements INetworkStatsProviderCallback {
        public IBinder asBinder() {
            return null;
        }

        public void notifyAlertReached() throws RemoteException {
        }

        public void notifyLimitReached() throws RemoteException {
        }

        public void notifyStatsUpdated(int i, NetworkStats networkStats, NetworkStats networkStats2) throws RemoteException {
        }

        public void notifyWarningReached() throws RemoteException {
        }

        public void unregister() throws RemoteException {
        }
    }

    void notifyAlertReached() throws RemoteException;

    void notifyLimitReached() throws RemoteException;

    void notifyStatsUpdated(int i, NetworkStats networkStats, NetworkStats networkStats2) throws RemoteException;

    void notifyWarningReached() throws RemoteException;

    void unregister() throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkStatsProviderCallback {
        static final int TRANSACTION_notifyAlertReached = 2;
        static final int TRANSACTION_notifyLimitReached = 4;
        static final int TRANSACTION_notifyStatsUpdated = 1;
        static final int TRANSACTION_notifyWarningReached = 3;
        static final int TRANSACTION_unregister = 5;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "notifyStatsUpdated";
            }
            if (i == 2) {
                return "notifyAlertReached";
            }
            if (i == 3) {
                return "notifyWarningReached";
            }
            if (i == 4) {
                return "notifyLimitReached";
            }
            if (i != 5) {
                return null;
            }
            return "unregister";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 4;
        }

        public Stub() {
            attachInterface(this, INetworkStatsProviderCallback.DESCRIPTOR);
        }

        public static INetworkStatsProviderCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkStatsProviderCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkStatsProviderCallback)) {
                return new Proxy(iBinder);
            }
            return (INetworkStatsProviderCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkStatsProviderCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    parcel.enforceNoDataAvail();
                    notifyStatsUpdated(parcel.readInt(), (NetworkStats) parcel.readTypedObject(NetworkStats.CREATOR), (NetworkStats) parcel.readTypedObject(NetworkStats.CREATOR));
                } else if (i == 2) {
                    notifyAlertReached();
                } else if (i == 3) {
                    notifyWarningReached();
                } else if (i == 4) {
                    notifyLimitReached();
                } else if (i != 5) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    unregister();
                }
                return true;
            }
            parcel2.writeString(INetworkStatsProviderCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkStatsProviderCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkStatsProviderCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void notifyStatsUpdated(int i, NetworkStats networkStats, NetworkStats networkStats2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStatsProviderCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(networkStats, 0);
                    obtain.writeTypedObject(networkStats2, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void notifyAlertReached() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStatsProviderCallback.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void notifyWarningReached() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStatsProviderCallback.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void notifyLimitReached() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStatsProviderCallback.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void unregister() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStatsProviderCallback.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
