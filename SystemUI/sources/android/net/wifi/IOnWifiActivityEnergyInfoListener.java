package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.connectivity.WifiActivityEnergyInfo;

public interface IOnWifiActivityEnergyInfoListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IOnWifiActivityEnergyInfoListener";

    public static class Default implements IOnWifiActivityEnergyInfoListener {
        public IBinder asBinder() {
            return null;
        }

        public void onWifiActivityEnergyInfo(WifiActivityEnergyInfo wifiActivityEnergyInfo) throws RemoteException {
        }
    }

    void onWifiActivityEnergyInfo(WifiActivityEnergyInfo wifiActivityEnergyInfo) throws RemoteException;

    public static abstract class Stub extends Binder implements IOnWifiActivityEnergyInfoListener {
        static final int TRANSACTION_onWifiActivityEnergyInfo = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IOnWifiActivityEnergyInfoListener.DESCRIPTOR);
        }

        public static IOnWifiActivityEnergyInfoListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOnWifiActivityEnergyInfoListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOnWifiActivityEnergyInfoListener)) {
                return new Proxy(iBinder);
            }
            return (IOnWifiActivityEnergyInfoListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IOnWifiActivityEnergyInfoListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IOnWifiActivityEnergyInfoListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onWifiActivityEnergyInfo((WifiActivityEnergyInfo) parcel.readTypedObject(WifiActivityEnergyInfo.CREATOR));
                return true;
            }
        }

        private static class Proxy implements IOnWifiActivityEnergyInfoListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IOnWifiActivityEnergyInfoListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onWifiActivityEnergyInfo(WifiActivityEnergyInfo wifiActivityEnergyInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOnWifiActivityEnergyInfoListener.DESCRIPTOR);
                    obtain.writeTypedObject(wifiActivityEnergyInfo, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
