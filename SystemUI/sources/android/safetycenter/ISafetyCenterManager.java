package android.safetycenter;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.safetycenter.IOnSafetyCenterDataChangedListener;
import android.safetycenter.config.SafetyCenterConfig;

public interface ISafetyCenterManager extends IInterface {
    public static final String DESCRIPTOR = "android.safetycenter.ISafetyCenterManager";

    public static class Default implements ISafetyCenterManager {
        public void addOnSafetyCenterDataChangedListener(IOnSafetyCenterDataChangedListener iOnSafetyCenterDataChangedListener, int i) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }

        public void clearAllSafetySourceDataForTests() throws RemoteException {
        }

        public void clearSafetyCenterConfigForTests() throws RemoteException {
        }

        public void dismissSafetyCenterIssue(String str, int i) throws RemoteException {
        }

        public void executeSafetyCenterIssueAction(String str, String str2, int i) throws RemoteException {
        }

        public SafetyCenterConfig getSafetyCenterConfig() throws RemoteException {
            return null;
        }

        public SafetyCenterData getSafetyCenterData(int i) throws RemoteException {
            return null;
        }

        public SafetySourceData getSafetySourceData(String str, String str2, int i) throws RemoteException {
            return null;
        }

        public boolean isSafetyCenterEnabled() throws RemoteException {
            return false;
        }

        public void refreshSafetySources(int i, int i2) throws RemoteException {
        }

        public void removeOnSafetyCenterDataChangedListener(IOnSafetyCenterDataChangedListener iOnSafetyCenterDataChangedListener, int i) throws RemoteException {
        }

        public void reportSafetySourceError(String str, SafetySourceErrorDetails safetySourceErrorDetails, String str2, int i) throws RemoteException {
        }

        public void setSafetyCenterConfigForTests(SafetyCenterConfig safetyCenterConfig) throws RemoteException {
        }

        public void setSafetySourceData(String str, SafetySourceData safetySourceData, SafetyEvent safetyEvent, String str2, int i) throws RemoteException {
        }
    }

    void addOnSafetyCenterDataChangedListener(IOnSafetyCenterDataChangedListener iOnSafetyCenterDataChangedListener, int i) throws RemoteException;

    void clearAllSafetySourceDataForTests() throws RemoteException;

    void clearSafetyCenterConfigForTests() throws RemoteException;

    void dismissSafetyCenterIssue(String str, int i) throws RemoteException;

    void executeSafetyCenterIssueAction(String str, String str2, int i) throws RemoteException;

    SafetyCenterConfig getSafetyCenterConfig() throws RemoteException;

    SafetyCenterData getSafetyCenterData(int i) throws RemoteException;

    SafetySourceData getSafetySourceData(String str, String str2, int i) throws RemoteException;

    boolean isSafetyCenterEnabled() throws RemoteException;

    void refreshSafetySources(int i, int i2) throws RemoteException;

    void removeOnSafetyCenterDataChangedListener(IOnSafetyCenterDataChangedListener iOnSafetyCenterDataChangedListener, int i) throws RemoteException;

    void reportSafetySourceError(String str, SafetySourceErrorDetails safetySourceErrorDetails, String str2, int i) throws RemoteException;

    void setSafetyCenterConfigForTests(SafetyCenterConfig safetyCenterConfig) throws RemoteException;

    void setSafetySourceData(String str, SafetySourceData safetySourceData, SafetyEvent safetyEvent, String str2, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ISafetyCenterManager {
        static final int TRANSACTION_addOnSafetyCenterDataChangedListener = 8;
        static final int TRANSACTION_clearAllSafetySourceDataForTests = 12;
        static final int TRANSACTION_clearSafetyCenterConfigForTests = 14;
        static final int TRANSACTION_dismissSafetyCenterIssue = 10;
        static final int TRANSACTION_executeSafetyCenterIssueAction = 11;
        static final int TRANSACTION_getSafetyCenterConfig = 6;
        static final int TRANSACTION_getSafetyCenterData = 7;
        static final int TRANSACTION_getSafetySourceData = 3;
        static final int TRANSACTION_isSafetyCenterEnabled = 1;
        static final int TRANSACTION_refreshSafetySources = 5;
        static final int TRANSACTION_removeOnSafetyCenterDataChangedListener = 9;
        static final int TRANSACTION_reportSafetySourceError = 4;
        static final int TRANSACTION_setSafetyCenterConfigForTests = 13;
        static final int TRANSACTION_setSafetySourceData = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, ISafetyCenterManager.DESCRIPTOR);
        }

        public static ISafetyCenterManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(ISafetyCenterManager.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISafetyCenterManager)) {
                return new Proxy(iBinder);
            }
            return (ISafetyCenterManager) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(ISafetyCenterManager.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        boolean isSafetyCenterEnabled = isSafetyCenterEnabled();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isSafetyCenterEnabled);
                        break;
                    case 2:
                        setSafetySourceData(parcel.readString(), (SafetySourceData) parcel.readTypedObject(SafetySourceData.CREATOR), (SafetyEvent) parcel.readTypedObject(SafetyEvent.CREATOR), parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 3:
                        SafetySourceData safetySourceData = getSafetySourceData(parcel.readString(), parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(safetySourceData, 1);
                        break;
                    case 4:
                        reportSafetySourceError(parcel.readString(), (SafetySourceErrorDetails) parcel.readTypedObject(SafetySourceErrorDetails.CREATOR), parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 5:
                        refreshSafetySources(parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 6:
                        SafetyCenterConfig safetyCenterConfig = getSafetyCenterConfig();
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(safetyCenterConfig, 1);
                        break;
                    case 7:
                        SafetyCenterData safetyCenterData = getSafetyCenterData(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(safetyCenterData, 1);
                        break;
                    case 8:
                        addOnSafetyCenterDataChangedListener(IOnSafetyCenterDataChangedListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 9:
                        removeOnSafetyCenterDataChangedListener(IOnSafetyCenterDataChangedListener.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 10:
                        dismissSafetyCenterIssue(parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 11:
                        executeSafetyCenterIssueAction(parcel.readString(), parcel.readString(), parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 12:
                        clearAllSafetySourceDataForTests();
                        parcel2.writeNoException();
                        break;
                    case 13:
                        setSafetyCenterConfigForTests((SafetyCenterConfig) parcel.readTypedObject(SafetyCenterConfig.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 14:
                        clearSafetyCenterConfigForTests();
                        parcel2.writeNoException();
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(ISafetyCenterManager.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements ISafetyCenterManager {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return ISafetyCenterManager.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean isSafetyCenterEnabled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSafetySourceData(String str, SafetySourceData safetySourceData, SafetyEvent safetyEvent, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(safetySourceData, 0);
                    obtain.writeTypedObject(safetyEvent, 0);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SafetySourceData getSafetySourceData(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return (SafetySourceData) obtain2.readTypedObject(SafetySourceData.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void reportSafetySourceError(String str, SafetySourceErrorDetails safetySourceErrorDetails, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeTypedObject(safetySourceErrorDetails, 0);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void refreshSafetySources(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SafetyCenterConfig getSafetyCenterConfig() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return (SafetyCenterConfig) obtain2.readTypedObject(SafetyCenterConfig.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public SafetyCenterData getSafetyCenterData(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return (SafetyCenterData) obtain2.readTypedObject(SafetyCenterData.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addOnSafetyCenterDataChangedListener(IOnSafetyCenterDataChangedListener iOnSafetyCenterDataChangedListener, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnSafetyCenterDataChangedListener);
                    obtain.writeInt(i);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeOnSafetyCenterDataChangedListener(IOnSafetyCenterDataChangedListener iOnSafetyCenterDataChangedListener, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeStrongInterface(iOnSafetyCenterDataChangedListener);
                    obtain.writeInt(i);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void dismissSafetyCenterIssue(String str, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void executeSafetyCenterIssueAction(String str, String str2, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeInt(i);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearAllSafetySourceDataForTests() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setSafetyCenterConfigForTests(SafetyCenterConfig safetyCenterConfig) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    obtain.writeTypedObject(safetyCenterConfig, 0);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearSafetyCenterConfigForTests() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(ISafetyCenterManager.DESCRIPTOR);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
