package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWifiVerboseLoggingStatusChangedListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IWifiVerboseLoggingStatusChangedListener";

    public static class Default implements IWifiVerboseLoggingStatusChangedListener {
        public IBinder asBinder() {
            return null;
        }

        public void onStatusChanged(boolean z) throws RemoteException {
        }
    }

    void onStatusChanged(boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiVerboseLoggingStatusChangedListener {
        static final int TRANSACTION_onStatusChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IWifiVerboseLoggingStatusChangedListener.DESCRIPTOR);
        }

        public static IWifiVerboseLoggingStatusChangedListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IWifiVerboseLoggingStatusChangedListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiVerboseLoggingStatusChangedListener)) {
                return new Proxy(iBinder);
            }
            return (IWifiVerboseLoggingStatusChangedListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IWifiVerboseLoggingStatusChangedListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IWifiVerboseLoggingStatusChangedListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onStatusChanged(parcel.readBoolean());
                return true;
            }
        }

        private static class Proxy implements IWifiVerboseLoggingStatusChangedListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IWifiVerboseLoggingStatusChangedListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onStatusChanged(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IWifiVerboseLoggingStatusChangedListener.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
