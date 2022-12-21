package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IPnoScanResultsCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IPnoScanResultsCallback";

    public static class Default implements IPnoScanResultsCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onRegisterFailed(int i) throws RemoteException {
        }

        public void onRegisterSuccess() throws RemoteException {
        }

        public void onRemoved(int i) throws RemoteException {
        }

        public void onScanResultsAvailable(List<ScanResult> list) throws RemoteException {
        }
    }

    void onRegisterFailed(int i) throws RemoteException;

    void onRegisterSuccess() throws RemoteException;

    void onRemoved(int i) throws RemoteException;

    void onScanResultsAvailable(List<ScanResult> list) throws RemoteException;

    public static abstract class Stub extends Binder implements IPnoScanResultsCallback {
        static final int TRANSACTION_onRegisterFailed = 3;
        static final int TRANSACTION_onRegisterSuccess = 2;
        static final int TRANSACTION_onRemoved = 4;
        static final int TRANSACTION_onScanResultsAvailable = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IPnoScanResultsCallback.DESCRIPTOR);
        }

        public static IPnoScanResultsCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IPnoScanResultsCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPnoScanResultsCallback)) {
                return new Proxy(iBinder);
            }
            return (IPnoScanResultsCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IPnoScanResultsCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onScanResultsAvailable(parcel.createTypedArrayList(ScanResult.CREATOR));
                } else if (i == 2) {
                    onRegisterSuccess();
                } else if (i == 3) {
                    onRegisterFailed(parcel.readInt());
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onRemoved(parcel.readInt());
                }
                return true;
            }
            parcel2.writeString(IPnoScanResultsCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IPnoScanResultsCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IPnoScanResultsCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onScanResultsAvailable(List<ScanResult> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPnoScanResultsCallback.DESCRIPTOR);
                    obtain.writeTypedList(list);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRegisterSuccess() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPnoScanResultsCallback.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRegisterFailed(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPnoScanResultsCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRemoved(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IPnoScanResultsCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
