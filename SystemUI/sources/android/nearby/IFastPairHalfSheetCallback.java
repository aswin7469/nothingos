package android.nearby;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFastPairHalfSheetCallback extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.IFastPairHalfSheetCallback";

    public static class Default implements IFastPairHalfSheetCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onHalfSheetConnectionConfirm(Intent intent) throws RemoteException {
        }
    }

    void onHalfSheetConnectionConfirm(Intent intent) throws RemoteException;

    public static abstract class Stub extends Binder implements IFastPairHalfSheetCallback {
        static final int TRANSACTION_onHalfSheetConnectionConfirm = 1;

        public static String getDefaultTransactionName(int i) {
            if (i != 1) {
                return null;
            }
            return "onHalfSheetConnectionConfirm";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 0;
        }

        public Stub() {
            attachInterface(this, IFastPairHalfSheetCallback.DESCRIPTOR);
        }

        public static IFastPairHalfSheetCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFastPairHalfSheetCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFastPairHalfSheetCallback)) {
                return new Proxy(iBinder);
            }
            return (IFastPairHalfSheetCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFastPairHalfSheetCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IFastPairHalfSheetCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceNoDataAvail();
                onHalfSheetConnectionConfirm((Intent) parcel.readTypedObject(Intent.CREATOR));
                parcel2.writeNoException();
                return true;
            }
        }

        private static class Proxy implements IFastPairHalfSheetCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFastPairHalfSheetCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onHalfSheetConnectionConfirm(Intent intent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairHalfSheetCallback.DESCRIPTOR);
                    obtain.writeTypedObject(intent, 0);
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
