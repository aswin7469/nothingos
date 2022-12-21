package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IScanResultsListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IScanResultsListener";

    public static class Default implements IScanResultsListener {
        public IBinder asBinder() {
            return null;
        }

        public void onScanResultsAvailable() throws RemoteException {
        }
    }

    void onScanResultsAvailable() throws RemoteException;

    public static abstract class Stub extends Binder implements IScanResultsListener {
        static final int TRANSACTION_onScanResultsAvailable = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IScanResultsListener.DESCRIPTOR);
        }

        public static IScanResultsListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IScanResultsListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IScanResultsListener)) {
                return new Proxy(iBinder);
            }
            return (IScanResultsListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IScanResultsListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IScanResultsListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onScanResultsAvailable();
                return true;
            }
        }

        private static class Proxy implements IScanResultsListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IScanResultsListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onScanResultsAvailable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IScanResultsListener.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
