package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISuggestionConnectionStatusListener extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.ISuggestionConnectionStatusListener";

    public static class Default implements ISuggestionConnectionStatusListener {
        public IBinder asBinder() {
            return null;
        }

        public void onConnectionStatus(WifiNetworkSuggestion wifiNetworkSuggestion, int i) throws RemoteException {
        }
    }

    void onConnectionStatus(WifiNetworkSuggestion wifiNetworkSuggestion, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ISuggestionConnectionStatusListener {
        static final int TRANSACTION_onConnectionStatus = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ISuggestionConnectionStatusListener.DESCRIPTOR);
        }

        public static ISuggestionConnectionStatusListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISuggestionConnectionStatusListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISuggestionConnectionStatusListener)) {
                return new Proxy(iBinder);
            }
            return (ISuggestionConnectionStatusListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ISuggestionConnectionStatusListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(ISuggestionConnectionStatusListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onConnectionStatus((WifiNetworkSuggestion) parcel.readTypedObject(WifiNetworkSuggestion.CREATOR), parcel.readInt());
                return true;
            }
        }

        private static class Proxy implements ISuggestionConnectionStatusListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ISuggestionConnectionStatusListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onConnectionStatus(WifiNetworkSuggestion wifiNetworkSuggestion, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISuggestionConnectionStatusListener.DESCRIPTOR);
                    obtain.writeTypedObject(wifiNetworkSuggestion, 0);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
