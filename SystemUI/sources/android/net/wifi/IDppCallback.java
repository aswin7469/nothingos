package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDppCallback extends IInterface {
    public static final String DESCRIPTOR = "android.net.wifi.IDppCallback";

    public static class Default implements IDppCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onBootstrapUriGenerated(String str) throws RemoteException {
        }

        public void onFailure(int i, String str, String str2, int[] iArr) throws RemoteException {
        }

        public void onProgress(int i) throws RemoteException {
        }

        public void onSuccess(int i) throws RemoteException {
        }

        public void onSuccessConfigReceived(int i) throws RemoteException {
        }
    }

    void onBootstrapUriGenerated(String str) throws RemoteException;

    void onFailure(int i, String str, String str2, int[] iArr) throws RemoteException;

    void onProgress(int i) throws RemoteException;

    void onSuccess(int i) throws RemoteException;

    void onSuccessConfigReceived(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements IDppCallback {
        static final int TRANSACTION_onBootstrapUriGenerated = 5;
        static final int TRANSACTION_onFailure = 3;
        static final int TRANSACTION_onProgress = 4;
        static final int TRANSACTION_onSuccess = 2;
        static final int TRANSACTION_onSuccessConfigReceived = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IDppCallback.DESCRIPTOR);
        }

        public static IDppCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IDppCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IDppCallback)) {
                return new Proxy(iBinder);
            }
            return (IDppCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IDppCallback.DESCRIPTOR);
            }
            if (i != 1598968902) {
                if (i == 1) {
                    onSuccessConfigReceived(parcel.readInt());
                } else if (i == 2) {
                    onSuccess(parcel.readInt());
                } else if (i == 3) {
                    onFailure(parcel.readInt(), parcel.readString(), parcel.readString(), parcel.createIntArray());
                } else if (i == 4) {
                    onProgress(parcel.readInt());
                } else if (i != 5) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    onBootstrapUriGenerated(parcel.readString());
                }
                return true;
            }
            parcel2.writeString(IDppCallback.DESCRIPTOR);
            return true;
        }

        private static class Proxy implements IDppCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IDppCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onSuccessConfigReceived(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDppCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onSuccess(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDppCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onFailure(int i, String str, String str2, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDppCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeIntArray(iArr);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onProgress(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDppCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onBootstrapUriGenerated(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDppCallback.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
