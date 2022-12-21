package com.nothing.proxy;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INothingService extends IInterface {
    public static final String DESCRIPTOR = "com.nothing.proxy.INothingService";

    public static class Default implements INothingService {
        public IBinder asBinder() {
            return null;
        }

        public void deviceGoToSleep(long j) throws RemoteException {
        }
    }

    void deviceGoToSleep(long j) throws RemoteException;

    public static abstract class Stub extends Binder implements INothingService {
        static final int TRANSACTION_deviceGoToSleep = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, INothingService.DESCRIPTOR);
        }

        public static INothingService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(INothingService.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof INothingService)) {
                return new Proxy(iBinder);
            }
            return (INothingService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(INothingService.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(INothingService.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                long readLong = parcel.readLong();
                parcel.enforceNoDataAvail();
                deviceGoToSleep(readLong);
                parcel2.writeNoException();
                return true;
            }
        }

        private static class Proxy implements INothingService {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return INothingService.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void deviceGoToSleep(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(INothingService.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
