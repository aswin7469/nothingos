package android.nearby.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFastPairAntispoofKeyDeviceMetadataCallback extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.aidl.IFastPairAntispoofKeyDeviceMetadataCallback";

    public static class Default implements IFastPairAntispoofKeyDeviceMetadataCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onError(int i, String str) throws RemoteException {
        }

        public void onFastPairAntispoofKeyDeviceMetadataReceived(FastPairAntispoofKeyDeviceMetadataParcel fastPairAntispoofKeyDeviceMetadataParcel) throws RemoteException {
        }
    }

    void onError(int i, String str) throws RemoteException;

    void onFastPairAntispoofKeyDeviceMetadataReceived(FastPairAntispoofKeyDeviceMetadataParcel fastPairAntispoofKeyDeviceMetadataParcel) throws RemoteException;

    public static abstract class Stub extends Binder implements IFastPairAntispoofKeyDeviceMetadataCallback {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onFastPairAntispoofKeyDeviceMetadataReceived = 1;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onFastPairAntispoofKeyDeviceMetadataReceived";
            }
            if (i != 2) {
                return null;
            }
            return "onError";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 1;
        }

        public Stub() {
            attachInterface(this, IFastPairAntispoofKeyDeviceMetadataCallback.DESCRIPTOR);
        }

        public static IFastPairAntispoofKeyDeviceMetadataCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFastPairAntispoofKeyDeviceMetadataCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFastPairAntispoofKeyDeviceMetadataCallback)) {
                return new Proxy(iBinder);
            }
            return (IFastPairAntispoofKeyDeviceMetadataCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFastPairAntispoofKeyDeviceMetadataCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    parcel.enforceNoDataAvail();
                    onFastPairAntispoofKeyDeviceMetadataReceived((FastPairAntispoofKeyDeviceMetadataParcel) parcel.readTypedObject(FastPairAntispoofKeyDeviceMetadataParcel.CREATOR));
                    parcel2.writeNoException();
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    int readInt = parcel.readInt();
                    String readString = parcel.readString();
                    parcel.enforceNoDataAvail();
                    onError(readInt, readString);
                    parcel2.writeNoException();
                }
                return true;
            }
            parcel2.writeString(IFastPairAntispoofKeyDeviceMetadataCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IFastPairAntispoofKeyDeviceMetadataCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFastPairAntispoofKeyDeviceMetadataCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onFastPairAntispoofKeyDeviceMetadataReceived(FastPairAntispoofKeyDeviceMetadataParcel fastPairAntispoofKeyDeviceMetadataParcel) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairAntispoofKeyDeviceMetadataCallback.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairAntispoofKeyDeviceMetadataParcel, 0);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onError(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairAntispoofKeyDeviceMetadataCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
