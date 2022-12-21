package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOnWifiUsabilityStatsListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IOnWifiUsabilityStatsListener";

    public static class Default implements IOnWifiUsabilityStatsListener {
        public IBinder asBinder() {
            return null;
        }

        public void onWifiUsabilityStats(int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry) throws RemoteException {
        }
    }

    void onWifiUsabilityStats(int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry) throws RemoteException;

    public static abstract class Stub extends Binder implements IOnWifiUsabilityStatsListener {
        static final int TRANSACTION_onWifiUsabilityStats = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IOnWifiUsabilityStatsListener.DESCRIPTOR);
        }

        public static IOnWifiUsabilityStatsListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOnWifiUsabilityStatsListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOnWifiUsabilityStatsListener)) {
                return new Proxy(iBinder);
            }
            return (IOnWifiUsabilityStatsListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IOnWifiUsabilityStatsListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IOnWifiUsabilityStatsListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onWifiUsabilityStats(parcel.readInt(), parcel.readBoolean(), (WifiUsabilityStatsEntry) parcel.readTypedObject(WifiUsabilityStatsEntry.CREATOR));
                return true;
            }
        }

        private static class Proxy implements IOnWifiUsabilityStatsListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IOnWifiUsabilityStatsListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onWifiUsabilityStats(int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOnWifiUsabilityStatsListener.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeBoolean(z);
                    obtain.writeTypedObject(wifiUsabilityStatsEntry, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
