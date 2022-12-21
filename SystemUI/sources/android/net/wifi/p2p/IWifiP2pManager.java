package android.net.wifi.p2p;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWifiP2pManager extends IInterface {

    public static class Default implements IWifiP2pManager {
        public IBinder asBinder() {
            return null;
        }

        public void checkConfigureWifiDisplayPermission() throws RemoteException {
        }

        public void close(IBinder iBinder) throws RemoteException {
        }

        public Messenger getMessenger(IBinder iBinder, String str, Bundle bundle) throws RemoteException {
            return null;
        }

        public Messenger getP2pStateMachineMessenger() throws RemoteException {
            return null;
        }

        public long getSupportedFeatures() throws RemoteException {
            return 0;
        }

        public void setMiracastMode(int i) throws RemoteException {
        }
    }

    void checkConfigureWifiDisplayPermission() throws RemoteException;

    void close(IBinder iBinder) throws RemoteException;

    Messenger getMessenger(IBinder iBinder, String str, Bundle bundle) throws RemoteException;

    Messenger getP2pStateMachineMessenger() throws RemoteException;

    long getSupportedFeatures() throws RemoteException;

    void setMiracastMode(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiP2pManager {
        public static final String DESCRIPTOR = "android.net.wifi.p2p.IWifiP2pManager";
        static final int TRANSACTION_checkConfigureWifiDisplayPermission = 5;
        static final int TRANSACTION_close = 3;
        static final int TRANSACTION_getMessenger = 1;
        static final int TRANSACTION_getP2pStateMachineMessenger = 2;
        static final int TRANSACTION_getSupportedFeatures = 6;
        static final int TRANSACTION_setMiracastMode = 4;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiP2pManager asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiP2pManager)) {
                return new Proxy(iBinder);
            }
            return (IWifiP2pManager) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        Messenger messenger = getMessenger(parcel.readStrongBinder(), parcel.readString(), (Bundle) parcel.readTypedObject(Bundle.CREATOR));
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(messenger, 1);
                        break;
                    case 2:
                        Messenger p2pStateMachineMessenger = getP2pStateMachineMessenger();
                        parcel2.writeNoException();
                        parcel2.writeTypedObject(p2pStateMachineMessenger, 1);
                        break;
                    case 3:
                        close(parcel.readStrongBinder());
                        break;
                    case 4:
                        setMiracastMode(parcel.readInt());
                        parcel2.writeNoException();
                        break;
                    case 5:
                        checkConfigureWifiDisplayPermission();
                        parcel2.writeNoException();
                        break;
                    case 6:
                        long supportedFeatures = getSupportedFeatures();
                        parcel2.writeNoException();
                        parcel2.writeLong(supportedFeatures);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IWifiP2pManager {
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

            public Messenger getMessenger(IBinder iBinder, String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    obtain.writeTypedObject(bundle, 0);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Messenger) obtain2.readTypedObject(Messenger.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Messenger getP2pStateMachineMessenger() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Messenger) obtain2.readTypedObject(Messenger.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void close(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void setMiracastMode(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void checkConfigureWifiDisplayPermission() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getSupportedFeatures() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
