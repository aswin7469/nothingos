package android.nearby.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFastPairEligibleAccountsCallback extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.aidl.IFastPairEligibleAccountsCallback";

    public static class Default implements IFastPairEligibleAccountsCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onError(int i, String str) throws RemoteException {
        }

        public void onFastPairEligibleAccountsReceived(FastPairEligibleAccountParcel[] fastPairEligibleAccountParcelArr) throws RemoteException {
        }
    }

    void onError(int i, String str) throws RemoteException;

    void onFastPairEligibleAccountsReceived(FastPairEligibleAccountParcel[] fastPairEligibleAccountParcelArr) throws RemoteException;

    public static abstract class Stub extends Binder implements IFastPairEligibleAccountsCallback {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onFastPairEligibleAccountsReceived = 1;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onFastPairEligibleAccountsReceived";
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
            attachInterface(this, IFastPairEligibleAccountsCallback.DESCRIPTOR);
        }

        public static IFastPairEligibleAccountsCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFastPairEligibleAccountsCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFastPairEligibleAccountsCallback)) {
                return new Proxy(iBinder);
            }
            return (IFastPairEligibleAccountsCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFastPairEligibleAccountsCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    parcel.enforceNoDataAvail();
                    onFastPairEligibleAccountsReceived((FastPairEligibleAccountParcel[]) parcel.createTypedArray(FastPairEligibleAccountParcel.CREATOR));
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
            parcel2.writeString(IFastPairEligibleAccountsCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IFastPairEligibleAccountsCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFastPairEligibleAccountsCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onFastPairEligibleAccountsReceived(FastPairEligibleAccountParcel[] fastPairEligibleAccountParcelArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairEligibleAccountsCallback.DESCRIPTOR);
                    obtain.writeTypedArray(fastPairEligibleAccountParcelArr, 0);
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
                    obtain.writeInterfaceToken(IFastPairEligibleAccountsCallback.DESCRIPTOR);
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
