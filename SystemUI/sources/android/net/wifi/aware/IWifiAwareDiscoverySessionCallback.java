package android.net.wifi.aware;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWifiAwareDiscoverySessionCallback extends IInterface {

    public static class Default implements IWifiAwareDiscoverySessionCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onMatch(int i, byte[] bArr, byte[] bArr2, int i2, byte[] bArr3) throws RemoteException {
        }

        public void onMatchExpired(int i) throws RemoteException {
        }

        public void onMatchWithDistance(int i, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws RemoteException {
        }

        public void onMessageReceived(int i, byte[] bArr) throws RemoteException {
        }

        public void onMessageSendFail(int i, int i2) throws RemoteException {
        }

        public void onMessageSendSuccess(int i) throws RemoteException {
        }

        public void onSessionConfigFail(int i) throws RemoteException {
        }

        public void onSessionConfigSuccess() throws RemoteException {
        }

        public void onSessionStarted(int i) throws RemoteException {
        }

        public void onSessionTerminated(int i) throws RemoteException {
        }
    }

    void onMatch(int i, byte[] bArr, byte[] bArr2, int i2, byte[] bArr3) throws RemoteException;

    void onMatchExpired(int i) throws RemoteException;

    void onMatchWithDistance(int i, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws RemoteException;

    void onMessageReceived(int i, byte[] bArr) throws RemoteException;

    void onMessageSendFail(int i, int i2) throws RemoteException;

    void onMessageSendSuccess(int i) throws RemoteException;

    void onSessionConfigFail(int i) throws RemoteException;

    void onSessionConfigSuccess() throws RemoteException;

    void onSessionStarted(int i) throws RemoteException;

    void onSessionTerminated(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IWifiAwareDiscoverySessionCallback {
        public static final String DESCRIPTOR = "android.net.wifi.aware.IWifiAwareDiscoverySessionCallback";
        static final int TRANSACTION_onMatch = 5;
        static final int TRANSACTION_onMatchExpired = 10;
        static final int TRANSACTION_onMatchWithDistance = 6;
        static final int TRANSACTION_onMessageReceived = 9;
        static final int TRANSACTION_onMessageSendFail = 8;
        static final int TRANSACTION_onMessageSendSuccess = 7;
        static final int TRANSACTION_onSessionConfigFail = 3;
        static final int TRANSACTION_onSessionConfigSuccess = 2;
        static final int TRANSACTION_onSessionStarted = 1;
        static final int TRANSACTION_onSessionTerminated = 4;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWifiAwareDiscoverySessionCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IWifiAwareDiscoverySessionCallback)) {
                return new Proxy(iBinder);
            }
            return (IWifiAwareDiscoverySessionCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        onSessionStarted(parcel.readInt());
                        break;
                    case 2:
                        onSessionConfigSuccess();
                        break;
                    case 3:
                        onSessionConfigFail(parcel.readInt());
                        break;
                    case 4:
                        onSessionTerminated(parcel.readInt());
                        break;
                    case 5:
                        onMatch(parcel.readInt(), parcel.createByteArray(), parcel.createByteArray(), parcel.readInt(), parcel.createByteArray());
                        break;
                    case 6:
                        onMatchWithDistance(parcel.readInt(), parcel.createByteArray(), parcel.createByteArray(), parcel.readInt(), parcel.readInt(), parcel.createByteArray());
                        break;
                    case 7:
                        onMessageSendSuccess(parcel.readInt());
                        break;
                    case 8:
                        onMessageSendFail(parcel.readInt(), parcel.readInt());
                        break;
                    case 9:
                        onMessageReceived(parcel.readInt(), parcel.createByteArray());
                        break;
                    case 10:
                        onMatchExpired(parcel.readInt());
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IWifiAwareDiscoverySessionCallback {
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

            public void onSessionStarted(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSessionConfigSuccess() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSessionConfigFail(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSessionTerminated(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMatch(int i, byte[] bArr, byte[] bArr2, int i2, byte[] bArr3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    obtain.writeInt(i2);
                    obtain.writeByteArray(bArr3);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMatchWithDistance(int i, byte[] bArr, byte[] bArr2, int i2, int i3, byte[] bArr3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    obtain.writeByteArray(bArr2);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeByteArray(bArr3);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMessageSendSuccess(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMessageSendFail(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(8, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMessageReceived(int i, byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(9, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onMatchExpired(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(10, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
