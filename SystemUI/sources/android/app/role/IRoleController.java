package android.app.role;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;

public interface IRoleController extends IInterface {
    public static final String DESCRIPTOR = "android.app.role.IRoleController";

    public static class Default implements IRoleController {
        public IBinder asBinder() {
            return null;
        }

        public void grantDefaultRoles(RemoteCallback remoteCallback) throws RemoteException {
        }

        public void isApplicationQualifiedForRole(String str, String str2, RemoteCallback remoteCallback) throws RemoteException {
        }

        public void isApplicationVisibleForRole(String str, String str2, RemoteCallback remoteCallback) throws RemoteException {
        }

        public void isRoleVisible(String str, RemoteCallback remoteCallback) throws RemoteException {
        }

        public void onAddRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) throws RemoteException {
        }

        public void onClearRoleHolders(String str, int i, RemoteCallback remoteCallback) throws RemoteException {
        }

        public void onRemoveRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) throws RemoteException {
        }
    }

    void grantDefaultRoles(RemoteCallback remoteCallback) throws RemoteException;

    void isApplicationQualifiedForRole(String str, String str2, RemoteCallback remoteCallback) throws RemoteException;

    void isApplicationVisibleForRole(String str, String str2, RemoteCallback remoteCallback) throws RemoteException;

    void isRoleVisible(String str, RemoteCallback remoteCallback) throws RemoteException;

    void onAddRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) throws RemoteException;

    void onClearRoleHolders(String str, int i, RemoteCallback remoteCallback) throws RemoteException;

    void onRemoveRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements IRoleController {
        static final int TRANSACTION_grantDefaultRoles = 1;
        static final int TRANSACTION_isApplicationQualifiedForRole = 5;
        static final int TRANSACTION_isApplicationVisibleForRole = 6;
        static final int TRANSACTION_isRoleVisible = 7;
        static final int TRANSACTION_onAddRoleHolder = 2;
        static final int TRANSACTION_onClearRoleHolders = 4;
        static final int TRANSACTION_onRemoveRoleHolder = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IRoleController.DESCRIPTOR);
        }

        public static IRoleController asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IRoleController.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IRoleController)) {
                return new Proxy(iBinder);
            }
            return (IRoleController) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IRoleController.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        grantDefaultRoles((RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        break;
                    case 2:
                        onAddRoleHolder(parcel.readString(), parcel.readString(), parcel.readInt(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        break;
                    case 3:
                        onRemoveRoleHolder(parcel.readString(), parcel.readString(), parcel.readInt(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        break;
                    case 4:
                        onClearRoleHolders(parcel.readString(), parcel.readInt(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        break;
                    case 5:
                        isApplicationQualifiedForRole(parcel.readString(), parcel.readString(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        break;
                    case 6:
                        isApplicationVisibleForRole(parcel.readString(), parcel.readString(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        break;
                    case 7:
                        isRoleVisible(parcel.readString(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(IRoleController.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IRoleController {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IRoleController.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void grantDefaultRoles(RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleController.DESCRIPTOR);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAddRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleController.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRemoveRoleHolder(String str, String str2, int i, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleController.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onClearRoleHolders(String str, int i, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleController.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void isApplicationQualifiedForRole(String str, String str2, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleController.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void isApplicationVisibleForRole(String str, String str2, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleController.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void isRoleVisible(String str, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleController.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
