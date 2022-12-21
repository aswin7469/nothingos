package android.p000os;

/* renamed from: android.os.IPendingIntentRef */
public interface IPendingIntentRef extends IInterface {
    public static final String DESCRIPTOR = "android.os.IPendingIntentRef";

    /* renamed from: android.os.IPendingIntentRef$Default */
    public static class Default implements IPendingIntentRef {
        public IBinder asBinder() {
            return null;
        }

        public void sendActiveConfigsChangedBroadcast(long[] jArr) throws RemoteException {
        }

        public void sendDataBroadcast(long j) throws RemoteException {
        }

        public void sendSubscriberBroadcast(long j, long j2, long j3, long j4, String[] strArr, StatsDimensionsValueParcel statsDimensionsValueParcel) throws RemoteException {
        }
    }

    void sendActiveConfigsChangedBroadcast(long[] jArr) throws RemoteException;

    void sendDataBroadcast(long j) throws RemoteException;

    void sendSubscriberBroadcast(long j, long j2, long j3, long j4, String[] strArr, StatsDimensionsValueParcel statsDimensionsValueParcel) throws RemoteException;

    /* renamed from: android.os.IPendingIntentRef$Stub */
    public static abstract class Stub extends Binder implements IPendingIntentRef {
        static final int TRANSACTION_sendActiveConfigsChangedBroadcast = 2;
        static final int TRANSACTION_sendDataBroadcast = 1;
        static final int TRANSACTION_sendSubscriberBroadcast = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IPendingIntentRef.DESCRIPTOR);
        }

        public static IPendingIntentRef asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IPendingIntentRef.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPendingIntentRef)) {
                return new Proxy(iBinder);
            }
            return (IPendingIntentRef) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            int i3 = i;
            Parcel parcel3 = parcel;
            if (i3 >= 1 && i3 <= 16777215) {
                parcel3.enforceInterface(IPendingIntentRef.DESCRIPTOR);
            }
            if (i3 != 1598968902) {
                if (i3 == 1) {
                    sendDataBroadcast(parcel.readLong());
                } else if (i3 == 2) {
                    sendActiveConfigsChangedBroadcast(parcel.createLongArray());
                } else if (i3 != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    sendSubscriberBroadcast(parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.readLong(), parcel.createStringArray(), (StatsDimensionsValueParcel) parcel3.readTypedObject(StatsDimensionsValueParcel.CREATOR));
                }
                return true;
            }
            parcel2.writeString(IPendingIntentRef.DESCRIPTOR);
            return true;
        }

        /* renamed from: android.os.IPendingIntentRef$Stub$Proxy */
        private static class Proxy implements IPendingIntentRef {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IPendingIntentRef.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void sendDataBroadcast(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPendingIntentRef.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendActiveConfigsChangedBroadcast(long[] jArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPendingIntentRef.DESCRIPTOR);
                    obtain.writeLongArray(jArr);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void sendSubscriberBroadcast(long j, long j2, long j3, long j4, String[] strArr, StatsDimensionsValueParcel statsDimensionsValueParcel) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPendingIntentRef.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeLong(j2);
                    obtain.writeLong(j3);
                    obtain.writeLong(j4);
                    obtain.writeStringArray(strArr);
                    obtain.writeTypedObject(statsDimensionsValueParcel, 0);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
