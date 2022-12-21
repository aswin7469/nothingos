package android.nearby.aidl;

import android.nearby.FastPairDevice;
import android.nearby.aidl.IFastPairStatusCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import sun.security.util.SecurityConstants;

public interface IFastPairUiService extends IInterface {
    public static final String DESCRIPTOR = "android.nearby.aidl.IFastPairUiService";

    public static class Default implements IFastPairUiService {
        public IBinder asBinder() {
            return null;
        }

        public void cancel(FastPairDevice fastPairDevice) throws RemoteException {
        }

        public void connect(FastPairDevice fastPairDevice) throws RemoteException {
        }

        public void registerCallback(IFastPairStatusCallback iFastPairStatusCallback) throws RemoteException {
        }

        public void unregisterCallback(IFastPairStatusCallback iFastPairStatusCallback) throws RemoteException {
        }
    }

    void cancel(FastPairDevice fastPairDevice) throws RemoteException;

    void connect(FastPairDevice fastPairDevice) throws RemoteException;

    void registerCallback(IFastPairStatusCallback iFastPairStatusCallback) throws RemoteException;

    void unregisterCallback(IFastPairStatusCallback iFastPairStatusCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements IFastPairUiService {
        static final int TRANSACTION_cancel = 4;
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_unregisterCallback = 2;

        public static String getDefaultTransactionName(int i) {
            if (i == 1) {
                return "registerCallback";
            }
            if (i == 2) {
                return "unregisterCallback";
            }
            if (i == 3) {
                return SecurityConstants.SOCKET_CONNECT_ACTION;
            }
            if (i != 4) {
                return null;
            }
            return "cancel";
        }

        public IBinder asBinder() {
            return this;
        }

        public int getMaxTransactionId() {
            return 3;
        }

        public Stub() {
            attachInterface(this, IFastPairUiService.DESCRIPTOR);
        }

        public static IFastPairUiService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFastPairUiService.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFastPairUiService)) {
                return new Proxy(iBinder);
            }
            return (IFastPairUiService) queryLocalInterface;
        }

        public String getTransactionName(int i) {
            return getDefaultTransactionName(i);
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFastPairUiService.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    IFastPairStatusCallback asInterface = IFastPairStatusCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    registerCallback(asInterface);
                    parcel2.writeNoException();
                } else if (i == 2) {
                    IFastPairStatusCallback asInterface2 = IFastPairStatusCallback.Stub.asInterface(parcel.readStrongBinder());
                    parcel.enforceNoDataAvail();
                    unregisterCallback(asInterface2);
                    parcel2.writeNoException();
                } else if (i == 3) {
                    parcel.enforceNoDataAvail();
                    connect((FastPairDevice) parcel.readTypedObject(FastPairDevice.CREATOR));
                    parcel2.writeNoException();
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    parcel.enforceNoDataAvail();
                    cancel((FastPairDevice) parcel.readTypedObject(FastPairDevice.CREATOR));
                    parcel2.writeNoException();
                }
                return true;
            }
            parcel2.writeString(IFastPairUiService.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IFastPairUiService {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFastPairUiService.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void registerCallback(IFastPairStatusCallback iFastPairStatusCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairUiService.DESCRIPTOR);
                    obtain.writeStrongInterface(iFastPairStatusCallback);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterCallback(IFastPairStatusCallback iFastPairStatusCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairUiService.DESCRIPTOR);
                    obtain.writeStrongInterface(iFastPairStatusCallback);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void connect(FastPairDevice fastPairDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairUiService.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairDevice, 0);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void cancel(FastPairDevice fastPairDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFastPairUiService.DESCRIPTOR);
                    obtain.writeTypedObject(fastPairDevice, 0);
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
