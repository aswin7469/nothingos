package android.app.role;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOnRoleHoldersChangedListener extends IInterface {
    public static final String DESCRIPTOR = "android.app.role.IOnRoleHoldersChangedListener";

    public static class Default implements IOnRoleHoldersChangedListener {
        public IBinder asBinder() {
            return null;
        }

        public void onRoleHoldersChanged(String str, int i) throws RemoteException {
        }
    }

    void onRoleHoldersChanged(String str, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IOnRoleHoldersChangedListener {
        static final int TRANSACTION_onRoleHoldersChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IOnRoleHoldersChangedListener.DESCRIPTOR);
        }

        public static IOnRoleHoldersChangedListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IOnRoleHoldersChangedListener.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOnRoleHoldersChangedListener)) {
                return new Proxy(iBinder);
            }
            return (IOnRoleHoldersChangedListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IOnRoleHoldersChangedListener.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IOnRoleHoldersChangedListener.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                onRoleHoldersChanged(parcel.readString(), parcel.readInt());
                return true;
            }
        }

        private static class Proxy implements IOnRoleHoldersChangedListener {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IOnRoleHoldersChangedListener.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onRoleHoldersChanged(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IOnRoleHoldersChangedListener.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
