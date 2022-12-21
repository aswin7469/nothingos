package android.app.role;

import android.app.role.IOnRoleHoldersChangedListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteCallback;
import android.os.RemoteException;
import java.util.List;

public interface IRoleManager extends IInterface {
    public static final String DESCRIPTOR = "android.app.role.IRoleManager";

    public static class Default implements IRoleManager {
        public void addOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener iOnRoleHoldersChangedListener, int i) throws RemoteException {
        }

        public void addRoleHolderAsUser(String str, String str2, int i, int i2, RemoteCallback remoteCallback) throws RemoteException {
        }

        public boolean addRoleHolderFromController(String str, String str2) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }

        public void clearRoleHoldersAsUser(String str, int i, int i2, RemoteCallback remoteCallback) throws RemoteException {
        }

        public String getBrowserRoleHolder(int i) throws RemoteException {
            return null;
        }

        public List<String> getHeldRolesFromController(String str) throws RemoteException {
            return null;
        }

        public List<String> getRoleHoldersAsUser(String str, int i) throws RemoteException {
            return null;
        }

        public String getSmsRoleHolder(int i) throws RemoteException {
            return null;
        }

        public boolean isBypassingRoleQualification() throws RemoteException {
            return false;
        }

        public boolean isRoleAvailable(String str) throws RemoteException {
            return false;
        }

        public boolean isRoleHeld(String str, String str2) throws RemoteException {
            return false;
        }

        public void removeOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener iOnRoleHoldersChangedListener, int i) throws RemoteException {
        }

        public void removeRoleHolderAsUser(String str, String str2, int i, int i2, RemoteCallback remoteCallback) throws RemoteException {
        }

        public boolean removeRoleHolderFromController(String str, String str2) throws RemoteException {
            return false;
        }

        public boolean setBrowserRoleHolder(String str, int i) throws RemoteException {
            return false;
        }

        public void setBypassingRoleQualification(boolean z) throws RemoteException {
        }

        public void setRoleNamesFromController(List<String> list) throws RemoteException {
        }
    }

    void addOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener iOnRoleHoldersChangedListener, int i) throws RemoteException;

    void addRoleHolderAsUser(String str, String str2, int i, int i2, RemoteCallback remoteCallback) throws RemoteException;

    boolean addRoleHolderFromController(String str, String str2) throws RemoteException;

    void clearRoleHoldersAsUser(String str, int i, int i2, RemoteCallback remoteCallback) throws RemoteException;

    String getBrowserRoleHolder(int i) throws RemoteException;

    List<String> getHeldRolesFromController(String str) throws RemoteException;

    List<String> getRoleHoldersAsUser(String str, int i) throws RemoteException;

    String getSmsRoleHolder(int i) throws RemoteException;

    boolean isBypassingRoleQualification() throws RemoteException;

    boolean isRoleAvailable(String str) throws RemoteException;

    boolean isRoleHeld(String str, String str2) throws RemoteException;

    void removeOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener iOnRoleHoldersChangedListener, int i) throws RemoteException;

    void removeRoleHolderAsUser(String str, String str2, int i, int i2, RemoteCallback remoteCallback) throws RemoteException;

    boolean removeRoleHolderFromController(String str, String str2) throws RemoteException;

    boolean setBrowserRoleHolder(String str, int i) throws RemoteException;

    void setBypassingRoleQualification(boolean z) throws RemoteException;

    void setRoleNamesFromController(List<String> list) throws RemoteException;

    public static abstract class Stub extends Binder implements IRoleManager {
        static final int TRANSACTION_addOnRoleHoldersChangedListenerAsUser = 7;
        static final int TRANSACTION_addRoleHolderAsUser = 4;
        static final int TRANSACTION_addRoleHolderFromController = 12;
        static final int TRANSACTION_clearRoleHoldersAsUser = 6;
        static final int TRANSACTION_getBrowserRoleHolder = 15;
        static final int TRANSACTION_getHeldRolesFromController = 14;
        static final int TRANSACTION_getRoleHoldersAsUser = 3;
        static final int TRANSACTION_getSmsRoleHolder = 17;
        static final int TRANSACTION_isBypassingRoleQualification = 9;
        static final int TRANSACTION_isRoleAvailable = 1;
        static final int TRANSACTION_isRoleHeld = 2;
        static final int TRANSACTION_removeOnRoleHoldersChangedListenerAsUser = 8;
        static final int TRANSACTION_removeRoleHolderAsUser = 5;
        static final int TRANSACTION_removeRoleHolderFromController = 13;
        static final int TRANSACTION_setBrowserRoleHolder = 16;
        static final int TRANSACTION_setBypassingRoleQualification = 10;
        static final int TRANSACTION_setRoleNamesFromController = 11;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IRoleManager.DESCRIPTOR);
        }

        public static IRoleManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IRoleManager.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IRoleManager)) {
                return new Proxy(iBinder);
            }
            return (IRoleManager) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IRoleManager.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        boolean isRoleAvailable = isRoleAvailable(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isRoleAvailable);
                        break;
                    case 2:
                        boolean isRoleHeld = isRoleHeld(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isRoleHeld);
                        break;
                    case 3:
                        List<String> roleHoldersAsUser = getRoleHoldersAsUser(parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeStringList(roleHoldersAsUser);
                        break;
                    case 4:
                        addRoleHolderAsUser(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 5:
                        removeRoleHolderAsUser(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 6:
                        clearRoleHoldersAsUser(parcel.readString(), parcel.readInt(), parcel.readInt(), (RemoteCallback) parcel.readTypedObject(RemoteCallback.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 7:
                        addOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 8:
                        removeOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 9:
                        boolean isBypassingRoleQualification = isBypassingRoleQualification();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isBypassingRoleQualification);
                        break;
                    case 10:
                        setBypassingRoleQualification(parcel.readBoolean());
                        parcel2.writeNoException();
                        break;
                    case 11:
                        setRoleNamesFromController(parcel.createStringArrayList());
                        parcel2.writeNoException();
                        break;
                    case 12:
                        boolean addRoleHolderFromController = addRoleHolderFromController(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(addRoleHolderFromController);
                        break;
                    case 13:
                        boolean removeRoleHolderFromController = removeRoleHolderFromController(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(removeRoleHolderFromController);
                        break;
                    case 14:
                        List<String> heldRolesFromController = getHeldRolesFromController(parcel.readString());
                        parcel2.writeNoException();
                        parcel2.writeStringList(heldRolesFromController);
                        break;
                    case 15:
                        String browserRoleHolder = getBrowserRoleHolder(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeString(browserRoleHolder);
                        break;
                    case 16:
                        boolean browserRoleHolder2 = setBrowserRoleHolder(parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeBoolean(browserRoleHolder2);
                        break;
                    case 17:
                        String smsRoleHolder = getSmsRoleHolder(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeString(smsRoleHolder);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(IRoleManager.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IRoleManager {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IRoleManager.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean isRoleAvailable(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isRoleHeld(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getRoleHoldersAsUser(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArrayList();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addRoleHolderAsUser(String str, String str2, int i, int i2, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeRoleHolderAsUser(String str, String str2, int i, int i2, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearRoleHoldersAsUser(String str, int i, int i2, RemoteCallback remoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeTypedObject(remoteCallback, 0);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener iOnRoleHoldersChangedListener, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnRoleHoldersChangedListener);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeOnRoleHoldersChangedListenerAsUser(IOnRoleHoldersChangedListener iOnRoleHoldersChangedListener, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnRoleHoldersChangedListener);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isBypassingRoleQualification() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setBypassingRoleQualification(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setRoleNamesFromController(List<String> list) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeStringList(list);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean addRoleHolderFromController(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean removeRoleHolderFromController(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<String> getHeldRolesFromController(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createStringArrayList();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getBrowserRoleHolder(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean setBrowserRoleHolder(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getSmsRoleHolder(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IRoleManager.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
