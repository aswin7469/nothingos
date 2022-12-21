package android.net.netstats.provider;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkStatsProvider extends IInterface {
    public static final String DESCRIPTOR = "android.net.netstats.provider.INetworkStatsProvider";

    public static class Default implements INetworkStatsProvider {
        public IBinder asBinder() {
            return null;
        }

        public void onRequestStatsUpdate(int i) throws RemoteException {
        }

        public void onSetAlert(long j) throws RemoteException {
        }

        public void onSetWarningAndLimit(String str, long j, long j2) throws RemoteException {
        }
    }

    void onRequestStatsUpdate(int i) throws RemoteException;

    void onSetAlert(long j) throws RemoteException;

    void onSetWarningAndLimit(String str, long j, long j2) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkStatsProvider {
        static final int TRANSACTION_onRequestStatsUpdate = 1;
        static final int TRANSACTION_onSetAlert = 2;
        static final int TRANSACTION_onSetWarningAndLimit = 3;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onRequestStatsUpdate";
            }
            if (i == 2) {
                return "onSetAlert";
            }
            if (i != 3) {
                return null;
            }
            return "onSetWarningAndLimit";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 2;
        }

        public Stub() {
            attachInterface(this, INetworkStatsProvider.DESCRIPTOR);
        }

        public static INetworkStatsProvider asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INetworkStatsProvider.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkStatsProvider)) {
                return new Proxy(iBinder);
            }
            return (INetworkStatsProvider) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INetworkStatsProvider.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    int readInt = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    onRequestStatsUpdate(readInt);
                } else if (i == 2) {
                    long readLong = parcel.readLong();
                    parcel.enforceNoDataAvail();
                    onSetAlert(readLong);
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    String readString = parcel.readString();
                    long readLong2 = parcel.readLong();
                    long readLong3 = parcel.readLong();
                    parcel.enforceNoDataAvail();
                    onSetWarningAndLimit(readString, readLong2, readLong3);
                }
                return true;
            }
            parcel2.writeString(INetworkStatsProvider.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkStatsProvider {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INetworkStatsProvider.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onRequestStatsUpdate(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStatsProvider.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSetAlert(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStatsProvider.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSetWarningAndLimit(String str, long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INetworkStatsProvider.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
