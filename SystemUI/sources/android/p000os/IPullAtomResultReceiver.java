package android.p000os;

import android.util.StatsEventParcel;

/* renamed from: android.os.IPullAtomResultReceiver */
public interface IPullAtomResultReceiver extends IInterface {
    public static final String DESCRIPTOR = "android.os.IPullAtomResultReceiver";

    /* renamed from: android.os.IPullAtomResultReceiver$Default */
    public static class Default implements IPullAtomResultReceiver {
        public IBinder asBinder() {
            return null;
        }

        public void pullFinished(int i, boolean z, StatsEventParcel[] statsEventParcelArr) throws RemoteException {
        }
    }

    void pullFinished(int i, boolean z, StatsEventParcel[] statsEventParcelArr) throws RemoteException;

    /* renamed from: android.os.IPullAtomResultReceiver$Stub */
    public static abstract class Stub extends Binder implements IPullAtomResultReceiver {
        static final int TRANSACTION_pullFinished = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IPullAtomResultReceiver.DESCRIPTOR);
        }

        public static IPullAtomResultReceiver asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IPullAtomResultReceiver.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPullAtomResultReceiver)) {
                return new Proxy(iBinder);
            }
            return (IPullAtomResultReceiver) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IPullAtomResultReceiver.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IPullAtomResultReceiver.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                pullFinished(parcel.readInt(), parcel.readBoolean(), (StatsEventParcel[]) parcel.createTypedArray(StatsEventParcel.CREATOR));
                return true;
            }
        }

        /* renamed from: android.os.IPullAtomResultReceiver$Stub$Proxy */
        private static class Proxy implements IPullAtomResultReceiver {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IPullAtomResultReceiver.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void pullFinished(int i, boolean z, StatsEventParcel[] statsEventParcelArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPullAtomResultReceiver.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeTypedArray(statsEventParcelArr, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
