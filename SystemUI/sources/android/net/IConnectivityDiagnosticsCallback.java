package android.net;

import android.net.ConnectivityDiagnosticsManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IConnectivityDiagnosticsCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.IConnectivityDiagnosticsCallback";

    public static class Default implements IConnectivityDiagnosticsCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onConnectivityReportAvailable(ConnectivityDiagnosticsManager.ConnectivityReport connectivityReport) throws RemoteException {
        }

        public void onDataStallSuspected(ConnectivityDiagnosticsManager.DataStallReport dataStallReport) throws RemoteException {
        }

        public void onNetworkConnectivityReported(Network network, boolean z) throws RemoteException {
        }
    }

    void onConnectivityReportAvailable(ConnectivityDiagnosticsManager.ConnectivityReport connectivityReport) throws RemoteException;

    void onDataStallSuspected(ConnectivityDiagnosticsManager.DataStallReport dataStallReport) throws RemoteException;

    void onNetworkConnectivityReported(Network network, boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements IConnectivityDiagnosticsCallback {
        static final int TRANSACTION_onConnectivityReportAvailable = 1;
        static final int TRANSACTION_onDataStallSuspected = 2;
        static final int TRANSACTION_onNetworkConnectivityReported = 3;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onConnectivityReportAvailable";
            }
            if (i == 2) {
                return "onDataStallSuspected";
            }
            if (i != 3) {
                return null;
            }
            return "onNetworkConnectivityReported";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 2;
        }

        public Stub() {
            attachInterface(this, IConnectivityDiagnosticsCallback.DESCRIPTOR);
        }

        public static IConnectivityDiagnosticsCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IConnectivityDiagnosticsCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IConnectivityDiagnosticsCallback)) {
                return new Proxy(iBinder);
            }
            return (IConnectivityDiagnosticsCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IConnectivityDiagnosticsCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onConnectivityReportAvailable((ConnectivityDiagnosticsManager.ConnectivityReport) parcel.readTypedObject(ConnectivityDiagnosticsManager.ConnectivityReport.CREATOR));
                } else if (i == 2) {
                    onDataStallSuspected((ConnectivityDiagnosticsManager.DataStallReport) parcel.readTypedObject(ConnectivityDiagnosticsManager.DataStallReport.CREATOR));
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onNetworkConnectivityReported((Network) parcel.readTypedObject(Network.CREATOR), parcel.readBoolean());
                }
                return true;
            }
            parcel2.writeString(IConnectivityDiagnosticsCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IConnectivityDiagnosticsCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IConnectivityDiagnosticsCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onConnectivityReportAvailable(ConnectivityDiagnosticsManager.ConnectivityReport connectivityReport) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IConnectivityDiagnosticsCallback.DESCRIPTOR);
                    obtain.writeTypedObject(connectivityReport, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onDataStallSuspected(ConnectivityDiagnosticsManager.DataStallReport dataStallReport) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IConnectivityDiagnosticsCallback.DESCRIPTOR);
                    obtain.writeTypedObject(dataStallReport, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onNetworkConnectivityReported(Network network, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IConnectivityDiagnosticsCallback.DESCRIPTOR);
                    obtain.writeTypedObject(network, 0);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
