package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkStatsSession extends IInterface {

    public static class Default implements INetworkStatsSession {
        public IBinder asBinder() {
            return null;
        }

        public void close() throws RemoteException {
        }

        public NetworkStats getDeviceSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException {
            return null;
        }

        public NetworkStatsHistory getHistoryForNetwork(NetworkTemplate networkTemplate, int i) throws RemoteException {
            return null;
        }

        public NetworkStatsHistory getHistoryForUid(NetworkTemplate networkTemplate, int i, int i2, int i3, int i4) throws RemoteException {
            return null;
        }

        public NetworkStatsHistory getHistoryIntervalForNetwork(NetworkTemplate networkTemplate, int i, long j, long j2) throws RemoteException {
            return null;
        }

        public NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate networkTemplate, int i, int i2, int i3, int i4, long j, long j2) throws RemoteException {
            return null;
        }

        public int[] getRelevantUids() throws RemoteException {
            return null;
        }

        public NetworkStats getSummaryForAllUid(NetworkTemplate networkTemplate, long j, long j2, boolean z) throws RemoteException {
            return null;
        }

        public NetworkStats getSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException {
            return null;
        }

        public NetworkStats getTaggedSummaryForAllUid(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException {
            return null;
        }
    }

    void close() throws RemoteException;

    NetworkStats getDeviceSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException;

    NetworkStatsHistory getHistoryForNetwork(NetworkTemplate networkTemplate, int i) throws RemoteException;

    NetworkStatsHistory getHistoryForUid(NetworkTemplate networkTemplate, int i, int i2, int i3, int i4) throws RemoteException;

    NetworkStatsHistory getHistoryIntervalForNetwork(NetworkTemplate networkTemplate, int i, long j, long j2) throws RemoteException;

    NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate networkTemplate, int i, int i2, int i3, int i4, long j, long j2) throws RemoteException;

    int[] getRelevantUids() throws RemoteException;

    NetworkStats getSummaryForAllUid(NetworkTemplate networkTemplate, long j, long j2, boolean z) throws RemoteException;

    NetworkStats getSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException;

    NetworkStats getTaggedSummaryForAllUid(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException;

    public static abstract class Stub extends Binder implements INetworkStatsSession {
        public static final String DESCRIPTOR = "android.net.INetworkStatsSession";
        static final int TRANSACTION_close = 10;
        static final int TRANSACTION_getDeviceSummaryForNetwork = 1;
        static final int TRANSACTION_getHistoryForNetwork = 3;
        static final int TRANSACTION_getHistoryForUid = 7;
        static final int TRANSACTION_getHistoryIntervalForNetwork = 4;
        static final int TRANSACTION_getHistoryIntervalForUid = 8;
        static final int TRANSACTION_getRelevantUids = 9;
        static final int TRANSACTION_getSummaryForAllUid = 5;
        static final int TRANSACTION_getSummaryForNetwork = 2;
        static final int TRANSACTION_getTaggedSummaryForAllUid = 6;

        public static String getDefaultTransactionName(int i) {
            switch (i) {
                case 1:
                    return "getDeviceSummaryForNetwork";
                case 2:
                    return "getSummaryForNetwork";
                case 3:
                    return "getHistoryForNetwork";
                case 4:
                    return "getHistoryIntervalForNetwork";
                case 5:
                    return "getSummaryForAllUid";
                case 6:
                    return "getTaggedSummaryForAllUid";
                case 7:
                    return "getHistoryForUid";
                case 8:
                    return "getHistoryIntervalForUid";
                case 9:
                    return "getRelevantUids";
                case 10:
                    return "close";
                default:
                    return null;
            }
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 9;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkStatsSession asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INetworkStatsSession)) {
                return new Proxy(iBinder);
            }
            return (INetworkStatsSession) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = i;
            Parcel parcel3 = parcel;
            Parcel parcel4 = parcel2;
            if (i3 >= 1 && i3 <= 16777215) {
                parcel3.enforceInterface(DESCRIPTOR);
            }
            if (i3 != 1598968902) {
                switch (i3) {
                    case 1:
                        long readLong = parcel.readLong();
                        long readLong2 = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        NetworkStats deviceSummaryForNetwork = getDeviceSummaryForNetwork((NetworkTemplate) parcel3.readTypedObject(NetworkTemplate.CREATOR), readLong, readLong2);
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(deviceSummaryForNetwork, 1);
                        break;
                    case 2:
                        long readLong3 = parcel.readLong();
                        long readLong4 = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        NetworkStats summaryForNetwork = getSummaryForNetwork((NetworkTemplate) parcel3.readTypedObject(NetworkTemplate.CREATOR), readLong3, readLong4);
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(summaryForNetwork, 1);
                        break;
                    case 3:
                        int readInt = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        NetworkStatsHistory historyForNetwork = getHistoryForNetwork((NetworkTemplate) parcel3.readTypedObject(NetworkTemplate.CREATOR), readInt);
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(historyForNetwork, 1);
                        break;
                    case 4:
                        int readInt2 = parcel.readInt();
                        long readLong5 = parcel.readLong();
                        long readLong6 = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        NetworkStatsHistory historyIntervalForNetwork = getHistoryIntervalForNetwork((NetworkTemplate) parcel3.readTypedObject(NetworkTemplate.CREATOR), readInt2, readLong5, readLong6);
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(historyIntervalForNetwork, 1);
                        break;
                    case 5:
                        long readLong7 = parcel.readLong();
                        long readLong8 = parcel.readLong();
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        NetworkStats summaryForAllUid = getSummaryForAllUid((NetworkTemplate) parcel3.readTypedObject(NetworkTemplate.CREATOR), readLong7, readLong8, readBoolean);
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(summaryForAllUid, 1);
                        break;
                    case 6:
                        long readLong9 = parcel.readLong();
                        long readLong10 = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        NetworkStats taggedSummaryForAllUid = getTaggedSummaryForAllUid((NetworkTemplate) parcel3.readTypedObject(NetworkTemplate.CREATOR), readLong9, readLong10);
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(taggedSummaryForAllUid, 1);
                        break;
                    case 7:
                        int readInt3 = parcel.readInt();
                        int readInt4 = parcel.readInt();
                        int readInt5 = parcel.readInt();
                        int readInt6 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        NetworkStatsHistory historyForUid = getHistoryForUid((NetworkTemplate) parcel3.readTypedObject(NetworkTemplate.CREATOR), readInt3, readInt4, readInt5, readInt6);
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(historyForUid, 1);
                        break;
                    case 8:
                        int readInt7 = parcel.readInt();
                        int readInt8 = parcel.readInt();
                        int readInt9 = parcel.readInt();
                        int readInt10 = parcel.readInt();
                        long readLong11 = parcel.readLong();
                        long readLong12 = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        NetworkStatsHistory historyIntervalForUid = getHistoryIntervalForUid((NetworkTemplate) parcel3.readTypedObject(NetworkTemplate.CREATOR), readInt7, readInt8, readInt9, readInt10, readLong11, readLong12);
                        parcel2.writeNoException();
                        parcel4.writeTypedObject(historyIntervalForUid, 1);
                        break;
                    case 9:
                        int[] relevantUids = getRelevantUids();
                        parcel2.writeNoException();
                        parcel4.writeIntArray(relevantUids);
                        break;
                    case 10:
                        close();
                        parcel2.writeNoException();
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel4.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INetworkStatsSession {
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

            public NetworkStats getDeviceSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkTemplate, 0);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStats) obtain2.readTypedObject(NetworkStats.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStats getSummaryForNetwork(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkTemplate, 0);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStats) obtain2.readTypedObject(NetworkStats.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStatsHistory getHistoryForNetwork(NetworkTemplate networkTemplate, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkTemplate, 0);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStatsHistory) obtain2.readTypedObject(NetworkStatsHistory.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStatsHistory getHistoryIntervalForNetwork(NetworkTemplate networkTemplate, int i, long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkTemplate, 0);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStatsHistory) obtain2.readTypedObject(NetworkStatsHistory.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStats getSummaryForAllUid(NetworkTemplate networkTemplate, long j, long j2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkTemplate, 0);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStats) obtain2.readTypedObject(NetworkStats.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStats getTaggedSummaryForAllUid(NetworkTemplate networkTemplate, long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkTemplate, 0);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStats) obtain2.readTypedObject(NetworkStats.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStatsHistory getHistoryForUid(NetworkTemplate networkTemplate, int i, int i2, int i3, int i4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkTemplate, 0);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStatsHistory) obtain2.readTypedObject(NetworkStatsHistory.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public NetworkStatsHistory getHistoryIntervalForUid(NetworkTemplate networkTemplate, int i, int i2, int i3, int i4, long j, long j2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(networkTemplate, 0);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return (NetworkStatsHistory) obtain2.readTypedObject(NetworkStatsHistory.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int[] getRelevantUids() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createIntArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void close() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
