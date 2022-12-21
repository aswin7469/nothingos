package android.net.netstats;

import android.net.DataUsageRequest;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IUsageCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.netstats.IUsageCallback";

    public static class Default implements IUsageCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onCallbackReleased(DataUsageRequest dataUsageRequest) throws RemoteException {
        }

        public void onThresholdReached(DataUsageRequest dataUsageRequest) throws RemoteException {
        }
    }

    void onCallbackReleased(DataUsageRequest dataUsageRequest) throws RemoteException;

    void onThresholdReached(DataUsageRequest dataUsageRequest) throws RemoteException;

    public static abstract class Stub extends Binder implements IUsageCallback {
        static final int TRANSACTION_onCallbackReleased = 2;
        static final int TRANSACTION_onThresholdReached = 1;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "onThresholdReached";
            }
            if (i != 2) {
                return null;
            }
            return "onCallbackReleased";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 1;
        }

        public Stub() {
            attachInterface(this, IUsageCallback.DESCRIPTOR);
        }

        public static IUsageCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IUsageCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IUsageCallback)) {
                return new Proxy(iBinder);
            }
            return (IUsageCallback) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IUsageCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    parcel.enforceNoDataAvail();
                    onThresholdReached((DataUsageRequest) parcel.readTypedObject(DataUsageRequest.CREATOR));
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceNoDataAvail();
                    onCallbackReleased((DataUsageRequest) parcel.readTypedObject(DataUsageRequest.CREATOR));
                }
                return true;
            }
            parcel2.writeString(IUsageCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IUsageCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IUsageCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onThresholdReached(DataUsageRequest dataUsageRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IUsageCallback.DESCRIPTOR);
                    obtain.writeTypedObject(dataUsageRequest, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onCallbackReleased(DataUsageRequest dataUsageRequest) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IUsageCallback.DESCRIPTOR);
                    obtain.writeTypedObject(dataUsageRequest, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
