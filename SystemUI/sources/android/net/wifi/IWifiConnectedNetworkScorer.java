package android.net.wifi;

import android.net.wifi.IScoreUpdateObserver;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWifiConnectedNetworkScorer extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IWifiConnectedNetworkScorer";

    public static class Default implements IWifiConnectedNetworkScorer {
        public IBinder asBinder() {
            return null;
        }

        public void onSetScoreUpdateObserver(IScoreUpdateObserver iScoreUpdateObserver) throws RemoteException {
        }

        public void onStart(WifiConnectedSessionInfo wifiConnectedSessionInfo) throws RemoteException {
        }

        public void onStop(int i) throws RemoteException {
        }
    }

    void onSetScoreUpdateObserver(IScoreUpdateObserver iScoreUpdateObserver) throws RemoteException;

    void onStart(WifiConnectedSessionInfo wifiConnectedSessionInfo) throws RemoteException;

    void onStop(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiConnectedNetworkScorer {
        static final int TRANSACTION_onSetScoreUpdateObserver = 3;
        static final int TRANSACTION_onStart = 1;
        static final int TRANSACTION_onStop = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IWifiConnectedNetworkScorer.DESCRIPTOR);
        }

        public static IWifiConnectedNetworkScorer asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IWifiConnectedNetworkScorer.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiConnectedNetworkScorer)) {
                return new Proxy(iBinder);
            }
            return (IWifiConnectedNetworkScorer) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IWifiConnectedNetworkScorer.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onStart((WifiConnectedSessionInfo) parcel.readTypedObject(WifiConnectedSessionInfo.CREATOR));
                } else if (i == 2) {
                    onStop(parcel.readInt());
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onSetScoreUpdateObserver(IScoreUpdateObserver.Stub.asInterface(parcel.readStrongBinder()));
                }
                return true;
            }
            parcel2.writeString(IWifiConnectedNetworkScorer.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IWifiConnectedNetworkScorer {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IWifiConnectedNetworkScorer.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onStart(WifiConnectedSessionInfo wifiConnectedSessionInfo) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IWifiConnectedNetworkScorer.DESCRIPTOR);
                    obtain.writeTypedObject(wifiConnectedSessionInfo, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onStop(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IWifiConnectedNetworkScorer.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSetScoreUpdateObserver(IScoreUpdateObserver iScoreUpdateObserver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IWifiConnectedNetworkScorer.DESCRIPTOR);
                    obtain.writeStrongInterface(iScoreUpdateObserver);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
