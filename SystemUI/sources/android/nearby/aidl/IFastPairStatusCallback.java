package android.nearby.aidl;

import android.nearby.FastPairDevice;
import android.nearby.PairStatusMetadata;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFastPairStatusCallback extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.aidl.IFastPairStatusCallback";

    public static class Default implements IFastPairStatusCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onPairUpdate(FastPairDevice fastPairDevice, PairStatusMetadata pairStatusMetadata) throws RemoteException {
        }
    }

    void onPairUpdate(FastPairDevice fastPairDevice, PairStatusMetadata pairStatusMetadata) throws RemoteException;

    public static abstract class Stub extends Binder implements IFastPairStatusCallback {
        static final int TRANSACTION_onPairUpdate = 1;

        public static String getDefaultTransactionName(int i) {
            if (i != 1) {
                return null;
            }
            return "onPairUpdate";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 0;
        }

        public Stub() {
            attachInterface(this, IFastPairStatusCallback.DESCRIPTOR);
        }

        public static IFastPairStatusCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFastPairStatusCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFastPairStatusCallback)) {
                return new Proxy(iBinder);
            }
            return (IFastPairStatusCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFastPairStatusCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IFastPairStatusCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceNoDataAvail();
                onPairUpdate((FastPairDevice) parcel.readTypedObject(FastPairDevice.CREATOR), (PairStatusMetadata) parcel.readTypedObject(PairStatusMetadata.CREATOR));
                parcel2.writeNoException();
                return true;
            }
        }

        private static class Proxy implements IFastPairStatusCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFastPairStatusCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onPairUpdate(FastPairDevice fastPairDevice, PairStatusMetadata pairStatusMetadata) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairStatusCallback.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairDevice, 0);
                    obtain.writeTypedObject(pairStatusMetadata, 0);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
