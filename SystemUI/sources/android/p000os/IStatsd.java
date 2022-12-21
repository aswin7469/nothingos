package android.p000os;

import android.p000os.IPendingIntentRef;
import android.p000os.IPullAtomCallback;
import android.util.PropertyParcel;

/* renamed from: android.os.IStatsd */
public interface IStatsd extends IInterface {
    public static final String DESCRIPTOR = "android.os.IStatsd";
    public static final int FLAG_REQUIRE_LOW_LATENCY_MONITOR = 4;
    public static final int FLAG_REQUIRE_STAGING = 1;
    public static final int FLAG_ROLLBACK_ENABLED = 2;

    /* renamed from: android.os.IStatsd$Default */
    public static class Default implements IStatsd {
        public void addConfiguration(long j, byte[] bArr, int i) throws RemoteException {
        }

        public void allPullersFromBootRegistered() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }

        public void bootCompleted() throws RemoteException {
        }

        public byte[] getData(long j, int i) throws RemoteException {
            return null;
        }

        public byte[] getMetadata() throws RemoteException {
            return null;
        }

        public long[] getRegisteredExperimentIds() throws RemoteException {
            return null;
        }

        public void informAlarmForSubscriberTriggeringFired() throws RemoteException {
        }

        public void informAllUidData(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
        }

        public void informAnomalyAlarmFired() throws RemoteException {
        }

        public void informDeviceShutdown() throws RemoteException {
        }

        public void informOnePackage(String str, int i, long j, String str2, String str3, byte[] bArr) throws RemoteException {
        }

        public void informOnePackageRemoved(String str, int i) throws RemoteException {
        }

        public void informPollAlarmFired() throws RemoteException {
        }

        public void registerNativePullAtomCallback(int i, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException {
        }

        public void registerPullAtomCallback(int i, int i2, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException {
        }

        public void removeActiveConfigsChangedOperation(int i) throws RemoteException {
        }

        public void removeConfiguration(long j, int i) throws RemoteException {
        }

        public void removeDataFetchOperation(long j, int i) throws RemoteException {
        }

        public long[] setActiveConfigsChangedOperation(IPendingIntentRef iPendingIntentRef, int i) throws RemoteException {
            return null;
        }

        public void setBroadcastSubscriber(long j, long j2, IPendingIntentRef iPendingIntentRef, int i) throws RemoteException {
        }

        public void setDataFetchOperation(long j, IPendingIntentRef iPendingIntentRef, int i) throws RemoteException {
        }

        public void statsCompanionReady() throws RemoteException {
        }

        public void systemRunning() throws RemoteException {
        }

        public void unregisterNativePullAtomCallback(int i) throws RemoteException {
        }

        public void unregisterPullAtomCallback(int i, int i2) throws RemoteException {
        }

        public void unsetBroadcastSubscriber(long j, long j2, int i) throws RemoteException {
        }

        public void updateProperties(PropertyParcel[] propertyParcelArr) throws RemoteException {
        }
    }

    void addConfiguration(long j, byte[] bArr, int i) throws RemoteException;

    void allPullersFromBootRegistered() throws RemoteException;

    void bootCompleted() throws RemoteException;

    byte[] getData(long j, int i) throws RemoteException;

    byte[] getMetadata() throws RemoteException;

    long[] getRegisteredExperimentIds() throws RemoteException;

    void informAlarmForSubscriberTriggeringFired() throws RemoteException;

    void informAllUidData(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void informAnomalyAlarmFired() throws RemoteException;

    void informDeviceShutdown() throws RemoteException;

    void informOnePackage(String str, int i, long j, String str2, String str3, byte[] bArr) throws RemoteException;

    void informOnePackageRemoved(String str, int i) throws RemoteException;

    void informPollAlarmFired() throws RemoteException;

    void registerNativePullAtomCallback(int i, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException;

    void registerPullAtomCallback(int i, int i2, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException;

    void removeActiveConfigsChangedOperation(int i) throws RemoteException;

    void removeConfiguration(long j, int i) throws RemoteException;

    void removeDataFetchOperation(long j, int i) throws RemoteException;

    long[] setActiveConfigsChangedOperation(IPendingIntentRef iPendingIntentRef, int i) throws RemoteException;

    void setBroadcastSubscriber(long j, long j2, IPendingIntentRef iPendingIntentRef, int i) throws RemoteException;

    void setDataFetchOperation(long j, IPendingIntentRef iPendingIntentRef, int i) throws RemoteException;

    void statsCompanionReady() throws RemoteException;

    void systemRunning() throws RemoteException;

    void unregisterNativePullAtomCallback(int i) throws RemoteException;

    void unregisterPullAtomCallback(int i, int i2) throws RemoteException;

    void unsetBroadcastSubscriber(long j, long j2, int i) throws RemoteException;

    void updateProperties(PropertyParcel[] propertyParcelArr) throws RemoteException;

    /* renamed from: android.os.IStatsd$Stub */
    public static abstract class Stub extends Binder implements IStatsd {
        static final int TRANSACTION_addConfiguration = 13;
        static final int TRANSACTION_allPullersFromBootRegistered = 21;
        static final int TRANSACTION_bootCompleted = 2;
        static final int TRANSACTION_getData = 11;
        static final int TRANSACTION_getMetadata = 12;
        static final int TRANSACTION_getRegisteredExperimentIds = 26;
        static final int TRANSACTION_informAlarmForSubscriberTriggeringFired = 6;
        static final int TRANSACTION_informAllUidData = 8;
        static final int TRANSACTION_informAnomalyAlarmFired = 4;
        static final int TRANSACTION_informDeviceShutdown = 7;
        static final int TRANSACTION_informOnePackage = 9;
        static final int TRANSACTION_informOnePackageRemoved = 10;
        static final int TRANSACTION_informPollAlarmFired = 5;
        static final int TRANSACTION_registerNativePullAtomCallback = 23;
        static final int TRANSACTION_registerPullAtomCallback = 22;
        static final int TRANSACTION_removeActiveConfigsChangedOperation = 17;
        static final int TRANSACTION_removeConfiguration = 18;
        static final int TRANSACTION_removeDataFetchOperation = 15;
        static final int TRANSACTION_setActiveConfigsChangedOperation = 16;
        static final int TRANSACTION_setBroadcastSubscriber = 19;
        static final int TRANSACTION_setDataFetchOperation = 14;
        static final int TRANSACTION_statsCompanionReady = 3;
        static final int TRANSACTION_systemRunning = 1;
        static final int TRANSACTION_unregisterNativePullAtomCallback = 25;
        static final int TRANSACTION_unregisterPullAtomCallback = 24;
        static final int TRANSACTION_unsetBroadcastSubscriber = 20;
        static final int TRANSACTION_updateProperties = 27;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IStatsd.DESCRIPTOR);
        }

        public static IStatsd asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IStatsd.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStatsd)) {
                return new Proxy(iBinder);
            }
            return (IStatsd) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IStatsd.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        systemRunning();
                        break;
                    case 2:
                        bootCompleted();
                        break;
                    case 3:
                        statsCompanionReady();
                        parcel2.writeNoException();
                        break;
                    case 4:
                        informAnomalyAlarmFired();
                        parcel2.writeNoException();
                        break;
                    case 5:
                        informPollAlarmFired();
                        parcel2.writeNoException();
                        break;
                    case 6:
                        informAlarmForSubscriberTriggeringFired();
                        parcel2.writeNoException();
                        break;
                    case 7:
                        informDeviceShutdown();
                        parcel2.writeNoException();
                        break;
                    case 8:
                        informAllUidData((ParcelFileDescriptor) parcel.readTypedObject(ParcelFileDescriptor.CREATOR));
                        break;
                    case 9:
                        informOnePackage(parcel.readString(), parcel.readInt(), parcel.readLong(), parcel.readString(), parcel.readString(), parcel.createByteArray());
                        break;
                    case 10:
                        informOnePackageRemoved(parcel.readString(), parcel.readInt());
                        break;
                    case 11:
                        byte[] data = getData(parcel.readLong(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeByteArray(data);
                        break;
                    case 12:
                        byte[] metadata = getMetadata();
                        parcel2.writeNoException();
                        parcel2.writeByteArray(metadata);
                        break;
                    case 13:
                        addConfiguration(parcel.readLong(), parcel.createByteArray(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 14:
                        setDataFetchOperation(parcel.readLong(), IPendingIntentRef.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 15:
                        removeDataFetchOperation(parcel.readLong(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 16:
                        long[] activeConfigsChangedOperation = setActiveConfigsChangedOperation(IPendingIntentRef.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeLongArray(activeConfigsChangedOperation);
                        break;
                    case 17:
                        removeActiveConfigsChangedOperation(parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 18:
                        removeConfiguration(parcel.readLong(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 19:
                        setBroadcastSubscriber(parcel.readLong(), parcel.readLong(), IPendingIntentRef.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 20:
                        unsetBroadcastSubscriber(parcel.readLong(), parcel.readLong(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 21:
                        allPullersFromBootRegistered();
                        break;
                    case 22:
                        registerPullAtomCallback(parcel.readInt(), parcel.readInt(), parcel.readLong(), parcel.readLong(), parcel.createIntArray(), IPullAtomCallback.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 23:
                        registerNativePullAtomCallback(parcel.readInt(), parcel.readLong(), parcel.readLong(), parcel.createIntArray(), IPullAtomCallback.Stub.asInterface(parcel.readStrongBinder()));
                        break;
                    case 24:
                        unregisterPullAtomCallback(parcel.readInt(), parcel.readInt());
                        break;
                    case 25:
                        unregisterNativePullAtomCallback(parcel.readInt());
                        break;
                    case 26:
                        long[] registeredExperimentIds = getRegisteredExperimentIds();
                        parcel2.writeNoException();
                        parcel2.writeLongArray(registeredExperimentIds);
                        break;
                    case 27:
                        updateProperties((PropertyParcel[]) parcel.createTypedArray(PropertyParcel.CREATOR));
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(IStatsd.DESCRIPTOR);
            return true;
        }

        /* renamed from: android.os.IStatsd$Stub$Proxy */
        private static class Proxy implements IStatsd {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IStatsd.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void systemRunning() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void bootCompleted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void statsCompanionReady() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void informAnomalyAlarmFired() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void informPollAlarmFired() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void informAlarmForSubscriberTriggeringFired() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void informDeviceShutdown() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void informAllUidData(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeTypedObject(parcelFileDescriptor, 0);
                    this.mRemote.transact(8, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void informOnePackage(String str, int i, long j, String str2, String str3, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(9, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void informOnePackageRemoved(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public byte[] getData(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public byte[] getMetadata() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createByteArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addConfiguration(long j, byte[] bArr, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDataFetchOperation(long j, IPendingIntentRef iPendingIntentRef, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeStrongInterface(iPendingIntentRef);
                    obtain.writeInt(i);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeDataFetchOperation(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long[] setActiveConfigsChangedOperation(IPendingIntentRef iPendingIntentRef, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeStrongInterface(iPendingIntentRef);
                    obtain.writeInt(i);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createLongArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeActiveConfigsChangedOperation(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeConfiguration(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setBroadcastSubscriber(long j, long j2, IPendingIntentRef iPendingIntentRef, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeStrongInterface(iPendingIntentRef);
                    obtain.writeInt(i);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unsetBroadcastSubscriber(long j, long j2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeInt(i);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void allPullersFromBootRegistered() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(21, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void registerPullAtomCallback(int i, int i2, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeIntArray(iArr);
                    obtain.writeStrongInterface(iPullAtomCallback);
                    this.mRemote.transact(22, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void registerNativePullAtomCallback(int i, long j, long j2, int[] iArr, IPullAtomCallback iPullAtomCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeIntArray(iArr);
                    obtain.writeStrongInterface(iPullAtomCallback);
                    this.mRemote.transact(23, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void unregisterPullAtomCallback(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(24, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void unregisterNativePullAtomCallback(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(25, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public long[] getRegisteredExperimentIds() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createLongArray();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void updateProperties(PropertyParcel[] propertyParcelArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStatsd.DESCRIPTOR);
                    obtain.writeTypedArray(propertyParcelArr, 0);
                    this.mRemote.transact(27, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
