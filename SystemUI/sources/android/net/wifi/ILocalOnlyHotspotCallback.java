package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ILocalOnlyHotspotCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.ILocalOnlyHotspotCallback";

    public static class Default implements ILocalOnlyHotspotCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onHotspotFailed(int i) throws RemoteException {
        }

        public void onHotspotStarted(SoftApConfiguration softApConfiguration) throws RemoteException {
        }

        public void onHotspotStopped() throws RemoteException {
        }
    }

    void onHotspotFailed(int i) throws RemoteException;

    void onHotspotStarted(SoftApConfiguration softApConfiguration) throws RemoteException;

    void onHotspotStopped() throws RemoteException;

    public static abstract class Stub extends Binder implements ILocalOnlyHotspotCallback {
        static final int TRANSACTION_onHotspotFailed = 3;
        static final int TRANSACTION_onHotspotStarted = 1;
        static final int TRANSACTION_onHotspotStopped = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ILocalOnlyHotspotCallback.DESCRIPTOR);
        }

        public static ILocalOnlyHotspotCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ILocalOnlyHotspotCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ILocalOnlyHotspotCallback)) {
                return new Proxy(iBinder);
            }
            return (ILocalOnlyHotspotCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ILocalOnlyHotspotCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onHotspotStarted((SoftApConfiguration) parcel.readTypedObject(SoftApConfiguration.CREATOR));
                } else if (i == 2) {
                    onHotspotStopped();
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onHotspotFailed(parcel.readInt());
                }
                return true;
            }
            parcel2.writeString(ILocalOnlyHotspotCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ILocalOnlyHotspotCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ILocalOnlyHotspotCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onHotspotStarted(SoftApConfiguration softApConfiguration) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ILocalOnlyHotspotCallback.DESCRIPTOR);
                    obtain.writeTypedObject(softApConfiguration, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onHotspotStopped() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ILocalOnlyHotspotCallback.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onHotspotFailed(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ILocalOnlyHotspotCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
