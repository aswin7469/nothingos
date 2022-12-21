package android.net.wifi;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Messenger;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWifiScanner extends IInterface {

    public static class Default implements IWifiScanner {
        public IBinder asBinder() {
            return null;
        }

        public Bundle getAvailableChannels(int i, String str, String str2) throws RemoteException {
            return null;
        }

        public Messenger getMessenger() throws RemoteException {
            return null;
        }

        public boolean isScanning() throws RemoteException {
            return false;
        }
    }

    Bundle getAvailableChannels(int i, String str, String str2) throws RemoteException;

    Messenger getMessenger() throws RemoteException;

    boolean isScanning() throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiScanner {
        public static final String DESCRIPTOR = "android.net.wifi.IWifiScanner";
        static final int TRANSACTION_getAvailableChannels = 2;
        static final int TRANSACTION_getMessenger = 1;
        static final int TRANSACTION_isScanning = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiScanner asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiScanner)) {
                return new Proxy(iBinder);
            }
            return (IWifiScanner) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    Messenger messenger = getMessenger();
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(messenger, 1);
                } else if (i == 2) {
                    Bundle availableChannels = getAvailableChannels(parcel.readInt(), parcel.readString(), parcel.readString());
                    parcel2.writeNoException();
                    parcel2.writeTypedObject(availableChannels, 1);
                } else if (i != 3) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    boolean isScanning = isScanning();
                    parcel2.writeNoException();
                    parcel2.writeBoolean(isScanning);
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IWifiScanner {
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

            public Messenger getMessenger() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Messenger) obtain2.readTypedObject(Messenger.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle getAvailableChannels(int i, String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return (Bundle) obtain2.readTypedObject(Bundle.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isScanning() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
