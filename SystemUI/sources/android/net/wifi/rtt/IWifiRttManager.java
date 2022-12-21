package android.net.wifi.rtt;

import android.net.wifi.rtt.IRttCallback;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.WorkSource;

public interface IWifiRttManager extends IInterface {

    public static class Default implements IWifiRttManager {
        public IBinder asBinder() {
            return null;
        }

        public void cancelRanging(WorkSource workSource) throws RemoteException {
        }

        public boolean isAvailable() throws RemoteException {
            return false;
        }

        public void startRanging(IBinder iBinder, String str, String str2, WorkSource workSource, RangingRequest rangingRequest, IRttCallback iRttCallback, Bundle bundle) throws RemoteException {
        }
    }

    void cancelRanging(WorkSource workSource) throws RemoteException;

    boolean isAvailable() throws RemoteException;

    void startRanging(IBinder iBinder, String str, String str2, WorkSource workSource, RangingRequest rangingRequest, IRttCallback iRttCallback, Bundle bundle) throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiRttManager {
        public static final String DESCRIPTOR = "android.net.wifi.rtt.IWifiRttManager";
        static final int TRANSACTION_cancelRanging = 3;
        static final int TRANSACTION_isAvailable = 1;
        static final int TRANSACTION_startRanging = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiRttManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiRttManager)) {
                return new Proxy(iBinder);
            }
            return (IWifiRttManager) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    boolean isAvailable = isAvailable();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(isAvailable);
                } else if (i == 2) {
                    startRanging(parcel.readStrongBinder(), parcel.readString(), parcel.readString(), (WorkSource) parcel.readTypedObject(WorkSource.CREATOR), (RangingRequest) parcel.readTypedObject(RangingRequest.CREATOR), IRttCallback.Stub.asInterface(parcel.readStrongBinder()), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                    parcel2.writeNoException();
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    cancelRanging((WorkSource) parcel.readTypedObject(WorkSource.CREATOR));
                    parcel2.writeNoException();
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IWifiRttManager {
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

            public boolean isAvailable() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startRanging(IBinder iBinder, String str, String str2, WorkSource workSource, RangingRequest rangingRequest, IRttCallback iRttCallback, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedObject(workSource, 0);
                    obtain.writeTypedObject(rangingRequest, 0);
                    obtain.writeStrongInterface(iRttCallback);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void cancelRanging(WorkSource workSource) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeTypedObject(workSource, 0);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
