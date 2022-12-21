package android.p000os;

import android.app.PendingIntent;
import android.p000os.IPullAtomCallback;

/* renamed from: android.os.IStatsManagerService */
public interface IStatsManagerService extends IInterface {
    public static final String DESCRIPTOR = "android.os.IStatsManagerService";

    /* renamed from: android.os.IStatsManagerService$Default */
    public static class Default implements IStatsManagerService {
        public void addConfiguration(long j, byte[] bArr, String str) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }

        public byte[] getData(long j, String str) throws RemoteException {
            return null;
        }

        public byte[] getMetadata(String str) throws RemoteException {
            return null;
        }

        public long[] getRegisteredExperimentIds() throws RemoteException {
            return null;
        }

        public void registerPullAtomCallback(int i, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException {
        }

        public void removeActiveConfigsChangedOperation(String str) throws RemoteException {
        }

        public void removeConfiguration(long j, String str) throws RemoteException {
        }

        public void removeDataFetchOperation(long j, String str) throws RemoteException {
        }

        public long[] setActiveConfigsChangedOperation(PendingIntent pendingIntent, String str) throws RemoteException {
            return null;
        }

        public void setBroadcastSubscriber(long j, long j2, PendingIntent pendingIntent, String str) throws RemoteException {
        }

        public void setDataFetchOperation(long j, PendingIntent pendingIntent, String str) throws RemoteException {
        }

        public void unregisterPullAtomCallback(int i) throws RemoteException {
        }

        public void unsetBroadcastSubscriber(long j, long j2, String str) throws RemoteException {
        }
    }

    void addConfiguration(long j, byte[] bArr, String str) throws RemoteException;

    byte[] getData(long j, String str) throws RemoteException;

    byte[] getMetadata(String str) throws RemoteException;

    long[] getRegisteredExperimentIds() throws RemoteException;

    void registerPullAtomCallback(int i, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException;

    void removeActiveConfigsChangedOperation(String str) throws RemoteException;

    void removeConfiguration(long j, String str) throws RemoteException;

    void removeDataFetchOperation(long j, String str) throws RemoteException;

    long[] setActiveConfigsChangedOperation(PendingIntent pendingIntent, String str) throws RemoteException;

    void setBroadcastSubscriber(long j, long j2, PendingIntent pendingIntent, String str) throws RemoteException;

    void setDataFetchOperation(long j, PendingIntent pendingIntent, String str) throws RemoteException;

    void unregisterPullAtomCallback(int i) throws RemoteException;

    void unsetBroadcastSubscriber(long j, long j2, String str) throws RemoteException;

    /* renamed from: android.os.IStatsManagerService$Stub */
    public static abstract class Stub extends Binder implements IStatsManagerService {
        static final int TRANSACTION_addConfiguration = 10;
        static final int TRANSACTION_getData = 9;
        static final int TRANSACTION_getMetadata = 8;
        static final int TRANSACTION_getRegisteredExperimentIds = 7;
        static final int TRANSACTION_registerPullAtomCallback = 12;
        static final int TRANSACTION_removeActiveConfigsChangedOperation = 4;
        static final int TRANSACTION_removeConfiguration = 11;
        static final int TRANSACTION_removeDataFetchOperation = 2;
        static final int TRANSACTION_setActiveConfigsChangedOperation = 3;
        static final int TRANSACTION_setBroadcastSubscriber = 5;
        static final int TRANSACTION_setDataFetchOperation = 1;
        static final int TRANSACTION_unregisterPullAtomCallback = 13;
        static final int TRANSACTION_unsetBroadcastSubscriber = 6;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IStatsManagerService.DESCRIPTOR);
        }

        public static IStatsManagerService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IStatsManagerService.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStatsManagerService)) {
                return new Proxy(iBinder);
            }
            return (IStatsManagerService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IStatsManagerService.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        setDataFetchOperation(parcel.readLong(), (PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 2:
                        removeDataFetchOperation(parcel.readLong(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 3:
                        long[] activeConfigsChangedOperation = setActiveConfigsChangedOperation((PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeLongArray(activeConfigsChangedOperation);
                        break;
                    case 4:
                        removeActiveConfigsChangedOperation(parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 5:
                        setBroadcastSubscriber(parcel.readLong(), parcel.readLong(), (PendingIntent) parcel.readTypedObject(PendingIntent.CREATOR), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 6:
                        unsetBroadcastSubscriber(parcel.readLong(), parcel.readLong(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 7:
                        long[] registeredExperimentIds = getRegisteredExperimentIds();
                        parcel2.writeNoException();
                        parcel2.writeLongArray(registeredExperimentIds);
                        break;
                    case 8:
                        byte[] metadata = getMetadata(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeByteArray(metadata);
                        break;
                    case 9:
                        byte[] data = getData(parcel.readLong(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeByteArray(data);
                        break;
                    case 10:
                        addConfiguration(parcel.readLong(), parcel.createByteArray(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 11:
                        removeConfiguration(parcel.readLong(), parcel.readString());
                        parcel2.writeNoException();
                        break;
                    case 12:
                        registerPullAtomCallback(parcel.readInt(), parcel.readLong(), parcel.readLong(), parcel.createIntArray(), IPullAtomCallback.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 13:
                        unregisterPullAtomCallback(parcel.readInt());
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(IStatsManagerService.DESCRIPTOR);
            return true;
        }

        /* renamed from: android.os.IStatsManagerService$Stub$Proxy */
        private static class Proxy implements IStatsManagerService {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IStatsManagerService.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void setDataFetchOperation(long j, PendingIntent pendingIntent, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeTypedObject(pendingIntent, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeDataFetchOperation(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long[] setActiveConfigsChangedOperation(PendingIntent pendingIntent, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeTypedObject(pendingIntent, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createLongArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeActiveConfigsChangedOperation(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setBroadcastSubscriber(long j, long j2, PendingIntent pendingIntent, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeTypedObject(pendingIntent, 0);
                    obtain.writeString(str);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unsetBroadcastSubscriber(long j, long j2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeString(str);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long[] getRegisteredExperimentIds() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createLongArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getMetadata(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getData(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addConfiguration(long j, byte[] bArr, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeConfiguration(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerPullAtomCallback(int i, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeIntArray(iArr);
                    obtain.writeStrongInterface(iPullAtomCallback);
                    this.mRemote.transact(12, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void unregisterPullAtomCallback(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsManagerService.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
