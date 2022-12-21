package com.qti.extphone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDepersoResCallback extends IInterface {
    public static final String DESCRIPTOR = "com.qti.extphone.IDepersoResCallback";

    public static class Default implements IDepersoResCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onDepersoResult(int i, int i2) throws RemoteException {
        }
    }

    void onDepersoResult(int i, int i2) throws RemoteException;

    public static abstract class Stub extends Binder implements IDepersoResCallback {
        static final int TRANSACTION_onDepersoResult = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IDepersoResCallback.DESCRIPTOR);
        }

        public static IDepersoResCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IDepersoResCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IDepersoResCallback)) {
                return new Proxy(iBinder);
            }
            return (IDepersoResCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IDepersoResCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IDepersoResCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                int readInt = parcel.readInt();
                int readInt2 = parcel.readInt();
                parcel.enforceNoDataAvail();
                onDepersoResult(readInt, readInt2);
                return true;
            }
        }

        private static class Proxy implements IDepersoResCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IDepersoResCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onDepersoResult(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IDepersoResCallback.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
