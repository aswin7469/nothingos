package android.p000os;

/* renamed from: android.os.IStatsCompanionService */
public interface IStatsCompanionService extends IInterface {

    /* renamed from: android.os.IStatsCompanionService$Default */
    public static class Default implements IStatsCompanionService {
        public IBinder asBinder() {
            return null;
        }

        public void cancelAlarmForSubscriberTriggering() throws RemoteException {
        }

        public void cancelAnomalyAlarm() throws RemoteException {
        }

        public void cancelPullingAlarm() throws RemoteException {
        }

        public boolean checkPermission(String str, int i, int i2) throws RemoteException {
            return false;
        }

        public void setAlarmForSubscriberTriggering(long j) throws RemoteException {
        }

        public void setAnomalyAlarm(long j) throws RemoteException {
        }

        public void setPullingAlarm(long j) throws RemoteException {
        }

        public void statsdReady() throws RemoteException {
        }
    }

    void cancelAlarmForSubscriberTriggering() throws RemoteException;

    void cancelAnomalyAlarm() throws RemoteException;

    void cancelPullingAlarm() throws RemoteException;

    boolean checkPermission(String str, int i, int i2) throws RemoteException;

    void setAlarmForSubscriberTriggering(long j) throws RemoteException;

    void setAnomalyAlarm(long j) throws RemoteException;

    void setPullingAlarm(long j) throws RemoteException;

    void statsdReady() throws RemoteException;

    /* renamed from: android.os.IStatsCompanionService$Stub */
    public static abstract class Stub extends Binder implements IStatsCompanionService {
        public static final String DESCRIPTOR = "android.os.IStatsCompanionService";
        static final int TRANSACTION_cancelAlarmForSubscriberTriggering = 7;
        static final int TRANSACTION_cancelAnomalyAlarm = 3;
        static final int TRANSACTION_cancelPullingAlarm = 5;
        static final int TRANSACTION_checkPermission = 8;
        static final int TRANSACTION_setAlarmForSubscriberTriggering = 6;
        static final int TRANSACTION_setAnomalyAlarm = 2;
        static final int TRANSACTION_setPullingAlarm = 4;
        static final int TRANSACTION_statsdReady = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStatsCompanionService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStatsCompanionService)) {
                return new Proxy(iBinder);
            }
            return (IStatsCompanionService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        statsdReady();
                        break;
                    case 2:
                        setAnomalyAlarm(parcel.readLong());
                        break;
                    case 3:
                        cancelAnomalyAlarm();
                        break;
                    case 4:
                        setPullingAlarm(parcel.readLong());
                        break;
                    case 5:
                        cancelPullingAlarm();
                        break;
                    case 6:
                        setAlarmForSubscriberTriggering(parcel.readLong());
                        break;
                    case 7:
                        cancelAlarmForSubscriberTriggering();
                        break;
                    case 8:
                        boolean checkPermission = checkPermission(parcel.readString(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(checkPermission);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        /* renamed from: android.os.IStatsCompanionService$Stub$Proxy */
        private static class Proxy implements IStatsCompanionService {
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

            public void statsdReady() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void setAnomalyAlarm(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void cancelAnomalyAlarm() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void setPullingAlarm(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void cancelPullingAlarm() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void setAlarmForSubscriberTriggering(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void cancelAlarmForSubscriberTriggering() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public boolean checkPermission(String str, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
