package android.nearby;

import android.nearby.IBroadcastListener;
import android.nearby.IScanListener;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INearbyManager extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.INearbyManager";

    public static class Default implements INearbyManager {
        public IBinder asBinder() {
            return null;
        }

        public int registerScanListener(ScanRequest scanRequest, IScanListener iScanListener, String str, String str2) throws RemoteException {
            return 0;
        }

        public void startBroadcast(BroadcastRequestParcelable broadcastRequestParcelable, IBroadcastListener iBroadcastListener, String str, String str2) throws RemoteException {
        }

        public void stopBroadcast(IBroadcastListener iBroadcastListener, String str, String str2) throws RemoteException {
        }

        public void unregisterScanListener(IScanListener iScanListener, String str, String str2) throws RemoteException {
        }
    }

    int registerScanListener(ScanRequest scanRequest, IScanListener iScanListener, String str, String str2) throws RemoteException;

    void startBroadcast(BroadcastRequestParcelable broadcastRequestParcelable, IBroadcastListener iBroadcastListener, String str, String str2) throws RemoteException;

    void stopBroadcast(IBroadcastListener iBroadcastListener, String str, String str2) throws RemoteException;

    void unregisterScanListener(IScanListener iScanListener, String str, String str2) throws RemoteException;

    public static abstract class Stub extends Binder implements INearbyManager {
        static final int TRANSACTION_registerScanListener = 1;
        static final int TRANSACTION_startBroadcast = 3;
        static final int TRANSACTION_stopBroadcast = 4;
        static final int TRANSACTION_unregisterScanListener = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "registerScanListener";
            }
            if (i == 2) {
                return "unregisterScanListener";
            }
            if (i == 3) {
                return "startBroadcast";
            }
            if (i != 4) {
                return null;
            }
            return "stopBroadcast";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 3;
        }

        public Stub() {
            attachInterface(this, INearbyManager.DESCRIPTOR);
        }

        public static INearbyManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INearbyManager.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INearbyManager)) {
                return new Proxy(iBinder);
            }
            return (INearbyManager) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INearbyManager.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    IScanListener asInterface = IScanListener.Stub.asInterface(parcel.readStrongBinder());
                    String readString = parcel.readString();
                    String readString2 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    int registerScanListener = registerScanListener((ScanRequest) parcel.readTypedObject(ScanRequest.CREATOR), asInterface, readString, readString2);
                    parcel2.writeNoException();
                    parcel2.writeInt(registerScanListener);
                } else if (i == 2) {
                    IScanListener asInterface2 = IScanListener.Stub.asInterface(parcel.readStrongBinder());
                    String readString3 = parcel.readString();
                    String readString4 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    unregisterScanListener(asInterface2, readString3, readString4);
                    parcel2.writeNoException();
                } else if (i == 3) {
                    IBroadcastListener asInterface3 = IBroadcastListener.Stub.asInterface(parcel.readStrongBinder());
                    String readString5 = parcel.readString();
                    String readString6 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    startBroadcast((BroadcastRequestParcelable) parcel.readTypedObject(BroadcastRequestParcelable.CREATOR), asInterface3, readString5, readString6);
                    parcel2.writeNoException();
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    IBroadcastListener asInterface4 = IBroadcastListener.Stub.asInterface(parcel.readStrongBinder());
                    String readString7 = parcel.readString();
                    String readString8 = parcel.readString();
                    parcel.enforceNoDataAvail();
                    stopBroadcast(asInterface4, readString7, readString8);
                    parcel2.writeNoException();
                }
                return true;
            }
            parcel2.writeString(INearbyManager.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements INearbyManager {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INearbyManager.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public int registerScanListener(ScanRequest scanRequest, IScanListener iScanListener, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INearbyManager.DESCRIPTOR);
                    obtain.writeTypedObject(scanRequest, 0);
                    obtain.writeStrongInterface(iScanListener);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterScanListener(IScanListener iScanListener, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INearbyManager.DESCRIPTOR);
                    obtain.writeStrongInterface(iScanListener);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startBroadcast(BroadcastRequestParcelable broadcastRequestParcelable, IBroadcastListener iBroadcastListener, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INearbyManager.DESCRIPTOR);
                    obtain.writeTypedObject(broadcastRequestParcelable, 0);
                    obtain.writeStrongInterface(iBroadcastListener);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void stopBroadcast(IBroadcastListener iBroadcastListener, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INearbyManager.DESCRIPTOR);
                    obtain.writeStrongInterface(iBroadcastListener);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
