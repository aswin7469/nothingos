package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWifiAwareMacAddressProvider extends IInterface {

    public static class Default implements IWifiAwareMacAddressProvider {
        public IBinder asBinder() {
            return null;
        }

        public void macAddress(MacAddrMapping[] macAddrMappingArr) throws RemoteException {
        }
    }

    void macAddress(MacAddrMapping[] macAddrMappingArr) throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiAwareMacAddressProvider {
        public static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareMacAddressProvider";
        static final int TRANSACTION_macAddress = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiAwareMacAddressProvider asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiAwareMacAddressProvider)) {
                return new Proxy(iBinder);
            }
            return (IWifiAwareMacAddressProvider) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                macAddress((MacAddrMapping[]) parcel.createTypedArray(MacAddrMapping.CREATOR));
                return true;
            }
        }

        private static class Proxy implements IWifiAwareMacAddressProvider {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void macAddress(MacAddrMapping[] macAddrMappingArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedArray(macAddrMappingArr, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
