package android.net;

import android.net.INetworkStatsSession;
import android.net.netstats.IUsageCallback;
import android.net.netstats.provider.INetworkStatsProvider;
import android.net.netstats.provider.INetworkStatsProviderCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkStatsService extends IInterface {

    public static class Default implements INetworkStatsService {
        public void advisePersistThreshold(long j) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }

        public void forceUpdate() throws RemoteException {
        }

        public NetworkStats getDataLayerSnapshotForUid(int i) throws RemoteException {
            return null;
        }

        public long getIfaceStats(String str, int i) throws RemoteException {
            return 0;
        }

        public String[] getMobileIfaces() throws RemoteException {
            return null;
        }

        public long getTotalStats(int i) throws RemoteException {
            return 0;
        }

        public long getUidStats(int i, int i2) throws RemoteException {
            return 0;
        }

        public NetworkStats getUidStatsForTransport(int i) throws RemoteException {
            return null;
        }

        public void incrementOperationCount(int i, int i2, int i3) throws RemoteException {
        }

        public void noteUidForeground(int i, boolean z) throws RemoteException {
        }

        public void notifyNetworkStatus(Network[] networkArr, NetworkStateSnapshot[] networkStateSnapshotArr, String str, UnderlyingNetworkInfo[] underlyingNetworkInfoArr) throws RemoteException {
        }

        public INetworkStatsSession openSession() throws RemoteException {
            return null;
        }

        public INetworkStatsSession openSessionForUsageStats(int i, String str) throws RemoteException {
            return null;
        }

        public INetworkStatsProviderCallback registerNetworkStatsProvider(String str, INetworkStatsProvider iNetworkStatsProvider) throws RemoteException {
            return null;
        }

        public DataUsageRequest registerUsageCallback(String str, DataUsageRequest dataUsageRequest, IUsageCallback iUsageCallback) throws RemoteException {
            return null;
        }

        public void setStatsProviderWarningAndLimitAsync(String str, long j, long j2) throws RemoteException {
        }

        public void unregisterUsageRequest(DataUsageRequest dataUsageRequest) throws RemoteException {
        }
    }

    void advisePersistThreshold(long j) throws RemoteException;

    void forceUpdate() throws RemoteException;

    NetworkStats getDataLayerSnapshotForUid(int i) throws RemoteException;

    long getIfaceStats(String str, int i) throws RemoteException;

    String[] getMobileIfaces() throws RemoteException;

    long getTotalStats(int i) throws RemoteException;

    long getUidStats(int i, int i2) throws RemoteException;

    NetworkStats getUidStatsForTransport(int i) throws RemoteException;

    void incrementOperationCount(int i, int i2, int i3) throws RemoteException;

    void noteUidForeground(int i, boolean z) throws RemoteException;

    void notifyNetworkStatus(Network[] networkArr, NetworkStateSnapshot[] networkStateSnapshotArr, String str, UnderlyingNetworkInfo[] underlyingNetworkInfoArr) throws RemoteException;

    INetworkStatsSession openSession() throws RemoteException;

    INetworkStatsSession openSessionForUsageStats(int i, String str) throws RemoteException;

    INetworkStatsProviderCallback registerNetworkStatsProvider(String str, INetworkStatsProvider iNetworkStatsProvider) throws RemoteException;

    DataUsageRequest registerUsageCallback(String str, DataUsageRequest dataUsageRequest, IUsageCallback iUsageCallback) throws RemoteException;

    void setStatsProviderWarningAndLimitAsync(String str, long j, long j2) throws RemoteException;

    void unregisterUsageRequest(DataUsageRequest dataUsageRequest) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkStatsService {
        public static final String DESCRIPTOR = "android.net.INetworkStatsService";
        static final int TRANSACTION_advisePersistThreshold = 16;
        static final int TRANSACTION_forceUpdate = 8;
        static final int TRANSACTION_getDataLayerSnapshotForUid = 3;
        static final int TRANSACTION_getIfaceStats = 12;
        static final int TRANSACTION_getMobileIfaces = 5;
        static final int TRANSACTION_getTotalStats = 13;
        static final int TRANSACTION_getUidStats = 11;
        static final int TRANSACTION_getUidStatsForTransport = 4;
        static final int TRANSACTION_incrementOperationCount = 6;
        static final int TRANSACTION_noteUidForeground = 15;
        static final int TRANSACTION_notifyNetworkStatus = 7;
        static final int TRANSACTION_openSession = 1;
        static final int TRANSACTION_openSessionForUsageStats = 2;
        static final int TRANSACTION_registerNetworkStatsProvider = 14;
        static final int TRANSACTION_registerUsageCallback = 9;
        static final int TRANSACTION_setStatsProviderWarningAndLimitAsync = 17;
        static final int TRANSACTION_unregisterUsageRequest = 10;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "openSession";
                case 2:
                    return "openSessionForUsageStats";
                case 3:
                    return "getDataLayerSnapshotForUid";
                case 4:
                    return "getUidStatsForTransport";
                case 5:
                    return "getMobileIfaces";
                case 6:
                    return "incrementOperationCount";
                case 7:
                    return "notifyNetworkStatus";
                case 8:
                    return "forceUpdate";
                case 9:
                    return "registerUsageCallback";
                case 10:
                    return "unregisterUsageRequest";
                case 11:
                    return "getUidStats";
                case 12:
                    return "getIfaceStats";
                case 13:
                    return "getTotalStats";
                case 14:
                    return "registerNetworkStatsProvider";
                case 15:
                    return "noteUidForeground";
                case 16:
                    return "advisePersistThreshold";
                case 17:
                    return "setStatsProviderWarningAndLimitAsync";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 16;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkStatsService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkStatsService)) {
                return new Proxy(iBinder);
            }
            return (INetworkStatsService) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        INetworkStatsSession openSession = openSession();
                        parcel2.writeNoException();
                        parcel2.writeStrongInterface(openSession);
                        break;
                    case 2:
                        int readInt = parcel.readInt();
                        String readString = parcel.readString();
                        parcel.enforceNoDataAvail();
                        INetworkStatsSession openSessionForUsageStats = openSessionForUsageStats(readInt, readString);
                        parcel2.writeNoException();
                        parcel2.writeStrongInterface(openSessionForUsageStats);
                        break;
                    case 3:
                        int readInt2 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        NetworkStats dataLayerSnapshotForUid = getDataLayerSnapshotForUid(readInt2);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(dataLayerSnapshotForUid, 1);
                        break;
                    case 4:
                        int readInt3 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        NetworkStats uidStatsForTransport = getUidStatsForTransport(readInt3);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(uidStatsForTransport, 1);
                        break;
                    case 5:
                        String[] mobileIfaces = getMobileIfaces();
                        parcel2.writeNoException();
                        parcel2.writeStringArray(mobileIfaces);
                        break;
                    case 6:
                        int readInt4 = parcel.readInt();
                        int readInt5 = parcel.readInt();
                        int readInt6 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        incrementOperationCount(readInt4, readInt5, readInt6);
                        parcel2.writeNoException();
                        break;
                    case 7:
                        parcel.enforceNoDataAvail();
                        notifyNetworkStatus((Network[]) parcel.createTypedArray(Network.CREATOR), (NetworkStateSnapshot[]) parcel.createTypedArray(NetworkStateSnapshot.CREATOR), parcel.readString(), (UnderlyingNetworkInfo[]) parcel.createTypedArray(UnderlyingNetworkInfo.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 8:
                        forceUpdate();
                        parcel2.writeNoException();
                        break;
                    case 9:
                        IUsageCallback asInterface = IUsageCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        DataUsageRequest registerUsageCallback = registerUsageCallback(parcel.readString(), (DataUsageRequest) parcel.readTypedObject(DataUsageRequest.CREATOR), asInterface);
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(registerUsageCallback, 1);
                        break;
                    case 10:
                        parcel.enforceNoDataAvail();
                        unregisterUsageRequest((DataUsageRequest) parcel.readTypedObject(DataUsageRequest.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 11:
                        int readInt7 = parcel.readInt();
                        int readInt8 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        long uidStats = getUidStats(readInt7, readInt8);
                        parcel2.writeNoException();
                        parcel2.writeLong(uidStats);
                        break;
                    case 12:
                        String readString2 = parcel.readString();
                        int readInt9 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        long ifaceStats = getIfaceStats(readString2, readInt9);
                        parcel2.writeNoException();
                        parcel2.writeLong(ifaceStats);
                        break;
                    case 13:
                        int readInt10 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        long totalStats = getTotalStats(readInt10);
                        parcel2.writeNoException();
                        parcel2.writeLong(totalStats);
                        break;
                    case 14:
                        String readString3 = parcel.readString();
                        INetworkStatsProvider asInterface2 = INetworkStatsProvider.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        INetworkStatsProviderCallback registerNetworkStatsProvider = registerNetworkStatsProvider(readString3, asInterface2);
                        parcel2.writeNoException();
                        parcel2.writeStrongInterface(registerNetworkStatsProvider);
                        break;
                    case 15:
                        int readInt11 = parcel.readInt();
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        noteUidForeground(readInt11, readBoolean);
                        parcel2.writeNoException();
                        break;
                    case 16:
                        long readLong = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        advisePersistThreshold(readLong);
                        parcel2.writeNoException();
                        break;
                    case 17:
                        String readString4 = parcel.readString();
                        long readLong2 = parcel.readLong();
                        long readLong3 = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        setStatsProviderWarningAndLimitAsync(readString4, readLong2, readLong3);
                        parcel2.writeNoException();
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkStatsService {
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

            public INetworkStatsSession openSession() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return INetworkStatsSession.Stub.asInterface(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public INetworkStatsSession openSessionForUsageStats(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return INetworkStatsSession.Stub.asInterface(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStats getDataLayerSnapshotForUid(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStats) obtain2.readTypedObject(NetworkStats.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStats getUidStatsForTransport(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStats) obtain2.readTypedObject(NetworkStats.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String[] getMobileIfaces() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void incrementOperationCount(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notifyNetworkStatus(Network[] networkArr, NetworkStateSnapshot[] networkStateSnapshotArr, String str, UnderlyingNetworkInfo[] underlyingNetworkInfoArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedArray(networkArr, 0);
                    obtain.writeTypedArray(networkStateSnapshotArr, 0);
                    obtain.writeString(str);
                    obtain.writeTypedArray(underlyingNetworkInfoArr, 0);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void forceUpdate() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public DataUsageRequest registerUsageCallback(String str, DataUsageRequest dataUsageRequest, IUsageCallback iUsageCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(dataUsageRequest, 0);
                    obtain.writeStrongInterface(iUsageCallback);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return (DataUsageRequest) obtain2.readTypedObject(DataUsageRequest.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterUsageRequest(DataUsageRequest dataUsageRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(dataUsageRequest, 0);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getUidStats(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getIfaceStats(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getTotalStats(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public INetworkStatsProviderCallback registerNetworkStatsProvider(String str, INetworkStatsProvider iNetworkStatsProvider) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeStrongInterface(iNetworkStatsProvider);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return INetworkStatsProviderCallback.Stub.asInterface(obtain2.readStrongBinder());
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteUidForeground(int i, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void advisePersistThreshold(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setStatsProviderWarningAndLimitAsync(String str, long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
