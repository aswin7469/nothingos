package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOnWifiDriverCountryCodeChangedListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IOnWifiDriverCountryCodeChangedListener";

    public static class Default implements IOnWifiDriverCountryCodeChangedListener {
        public IBinder asBinder() {
            return null;
        }

        public void onDriverCountryCodeChanged(String str) throws RemoteException {
        }
    }

    void onDriverCountryCodeChanged(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IOnWifiDriverCountryCodeChangedListener {
        static final int TRANSACTION_onDriverCountryCodeChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IOnWifiDriverCountryCodeChangedListener.DESCRIPTOR);
        }

        public static IOnWifiDriverCountryCodeChangedListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOnWifiDriverCountryCodeChangedListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOnWifiDriverCountryCodeChangedListener)) {
                return new Proxy(iBinder);
            }
            return (IOnWifiDriverCountryCodeChangedListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IOnWifiDriverCountryCodeChangedListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IOnWifiDriverCountryCodeChangedListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onDriverCountryCodeChanged(parcel.readString());
                return true;
            }
        }

        private static class Proxy implements IOnWifiDriverCountryCodeChangedListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IOnWifiDriverCountryCodeChangedListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onDriverCountryCodeChanged(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOnWifiDriverCountryCodeChangedListener.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
