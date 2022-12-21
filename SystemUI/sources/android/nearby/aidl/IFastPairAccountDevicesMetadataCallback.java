package android.nearby.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFastPairAccountDevicesMetadataCallback extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.aidl.IFastPairAccountDevicesMetadataCallback";

    public static class Default implements IFastPairAccountDevicesMetadataCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onError(int i, String str) throws RemoteException {
        }

        public void onFastPairAccountDevicesMetadataReceived(FastPairAccountKeyDeviceMetadataParcel[] fastPairAccountKeyDeviceMetadataParcelArr) throws RemoteException {
        }
    }

    void onError(int i, String str) throws RemoteException;

    void onFastPairAccountDevicesMetadataReceived(FastPairAccountKeyDeviceMetadataParcel[] fastPairAccountKeyDeviceMetadataParcelArr) throws RemoteException;

    public static abstract class Stub extends Binder implements IFastPairAccountDevicesMetadataCallback {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onFastPairAccountDevicesMetadataReceived = 1;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onFastPairAccountDevicesMetadataReceived";
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
            attachInterface(this, IFastPairAccountDevicesMetadataCallback.DESCRIPTOR);
        }

        public static IFastPairAccountDevicesMetadataCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFastPairAccountDevicesMetadataCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFastPairAccountDevicesMetadataCallback)) {
                return new Proxy(iBinder);
            }
            return (IFastPairAccountDevicesMetadataCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFastPairAccountDevicesMetadataCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    parcel.enforceNoDataAvail();
                    onFastPairAccountDevicesMetadataReceived((FastPairAccountKeyDeviceMetadataParcel[]) parcel.createTypedArray(FastPairAccountKeyDeviceMetadataParcel.CREATOR));
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
            parcel2.writeString(IFastPairAccountDevicesMetadataCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IFastPairAccountDevicesMetadataCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFastPairAccountDevicesMetadataCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onFastPairAccountDevicesMetadataReceived(FastPairAccountKeyDeviceMetadataParcel[] fastPairAccountKeyDeviceMetadataParcelArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairAccountDevicesMetadataCallback.DESCRIPTOR);
                    obtain.writeTypedArray(fastPairAccountKeyDeviceMetadataParcelArr, 0);
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
                    obtain.writeInterfaceToken(IFastPairAccountDevicesMetadataCallback.DESCRIPTOR);
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
