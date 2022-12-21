package android.p000os;

import android.p000os.IPullAtomResultReceiver;

/* renamed from: android.os.IPullAtomCallback */
public interface IPullAtomCallback extends IInterface {
    public static final String DESCRIPTOR = "android.os.IPullAtomCallback";

    /* renamed from: android.os.IPullAtomCallback$Default */
    public static class Default implements IPullAtomCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onPullAtom(int i, IPullAtomResultReceiver iPullAtomResultReceiver) throws RemoteException {
        }
    }

    void onPullAtom(int i, IPullAtomResultReceiver iPullAtomResultReceiver) throws RemoteException;

    /* renamed from: android.os.IPullAtomCallback$Stub */
    public static abstract class Stub extends Binder implements IPullAtomCallback {
        static final int TRANSACTION_onPullAtom = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IPullAtomCallback.DESCRIPTOR);
        }

        public static IPullAtomCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IPullAtomCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPullAtomCallback)) {
                return new Proxy(iBinder);
            }
            return (IPullAtomCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IPullAtomCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IPullAtomCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onPullAtom(parcel.readInt(), IPullAtomResultReceiver.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            }
        }

        /* renamed from: android.os.IPullAtomCallback$Stub$Proxy */
        private static class Proxy implements IPullAtomCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IPullAtomCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onPullAtom(int i, IPullAtomResultReceiver iPullAtomResultReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPullAtomCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iPullAtomResultReceiver);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
