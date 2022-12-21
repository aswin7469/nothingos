package com.android.permission.jarjar.com.android.internal.infra;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAndroidFuture extends IInterface {
    public static final String DESCRIPTOR = "com.android.permission.jarjar.com.android.internal.infra.IAndroidFuture";

    public static class Default implements IAndroidFuture {
        public IBinder asBinder() {
            return null;
        }

        public void complete(AndroidFuture androidFuture) throws RemoteException {
        }
    }

    void complete(AndroidFuture androidFuture) throws RemoteException;

    public static abstract class Stub extends Binder implements IAndroidFuture {
        static final int TRANSACTION_complete = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IAndroidFuture.DESCRIPTOR);
        }

        public static IAndroidFuture asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IAndroidFuture.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IAndroidFuture)) {
                return new Proxy(iBinder);
            }
            return (IAndroidFuture) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IAndroidFuture.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IAndroidFuture.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                complete((AndroidFuture) parcel.readTypedObject(AndroidFuture.CREATOR));
                return true;
            }
        }

        private static class Proxy implements IAndroidFuture {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IAndroidFuture.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void complete(AndroidFuture androidFuture) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IAndroidFuture.DESCRIPTOR);
                    obtain.writeTypedObject(androidFuture, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
