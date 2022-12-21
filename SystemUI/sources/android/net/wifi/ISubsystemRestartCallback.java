package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISubsystemRestartCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.ISubsystemRestartCallback";

    public static class Default implements ISubsystemRestartCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onSubsystemRestarted() throws RemoteException {
        }

        public void onSubsystemRestarting() throws RemoteException {
        }
    }

    void onSubsystemRestarted() throws RemoteException;

    void onSubsystemRestarting() throws RemoteException;

    public static abstract class Stub extends Binder implements ISubsystemRestartCallback {
        static final int TRANSACTION_onSubsystemRestarted = 2;
        static final int TRANSACTION_onSubsystemRestarting = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ISubsystemRestartCallback.DESCRIPTOR);
        }

        public static ISubsystemRestartCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISubsystemRestartCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISubsystemRestartCallback)) {
                return new Proxy(iBinder);
            }
            return (ISubsystemRestartCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ISubsystemRestartCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onSubsystemRestarting();
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onSubsystemRestarted();
                }
                return true;
            }
            parcel2.writeString(ISubsystemRestartCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ISubsystemRestartCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ISubsystemRestartCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onSubsystemRestarting() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISubsystemRestartCallback.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSubsystemRestarted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISubsystemRestartCallback.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
