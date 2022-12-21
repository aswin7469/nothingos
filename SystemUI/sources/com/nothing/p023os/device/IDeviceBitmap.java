package com.nothing.p023os.device;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.nothing.os.device.IDeviceBitmap */
public interface IDeviceBitmap extends IInterface {

    /* renamed from: com.nothing.os.device.IDeviceBitmap$Default */
    public static class Default implements IDeviceBitmap {
        public IBinder asBinder() {
            return null;
        }

        public Bitmap getCaseBitmap() throws RemoteException {
            return null;
        }

        public Bitmap getDefaultBitmap() throws RemoteException {
            return null;
        }

        public Bitmap getLeftBitmap() throws RemoteException {
            return null;
        }

        public Bitmap getRightBitmap() throws RemoteException {
            return null;
        }
    }

    Bitmap getCaseBitmap() throws RemoteException;

    Bitmap getDefaultBitmap() throws RemoteException;

    Bitmap getLeftBitmap() throws RemoteException;

    Bitmap getRightBitmap() throws RemoteException;

    /* renamed from: com.nothing.os.device.IDeviceBitmap$Stub */
    public static abstract class Stub extends Binder implements IDeviceBitmap {
        private static final String DESCRIPTOR = "com.nothing.os.device.IDeviceBitmap";
        static final int TRANSACTION_getCaseBitmap = 3;
        static final int TRANSACTION_getDefaultBitmap = 4;
        static final int TRANSACTION_getLeftBitmap = 1;
        static final int TRANSACTION_getRightBitmap = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDeviceBitmap asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IDeviceBitmap)) {
                return new Proxy(iBinder);
            }
            return (IDeviceBitmap) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                Bitmap leftBitmap = getLeftBitmap();
                parcel2.writeNoException();
                if (leftBitmap != null) {
                    parcel2.writeInt(1);
                    leftBitmap.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                Bitmap rightBitmap = getRightBitmap();
                parcel2.writeNoException();
                if (rightBitmap != null) {
                    parcel2.writeInt(1);
                    rightBitmap.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                Bitmap caseBitmap = getCaseBitmap();
                parcel2.writeNoException();
                if (caseBitmap != null) {
                    parcel2.writeInt(1);
                    caseBitmap.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(DESCRIPTOR);
                Bitmap defaultBitmap = getDefaultBitmap();
                parcel2.writeNoException();
                if (defaultBitmap != null) {
                    parcel2.writeInt(1);
                    defaultBitmap.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.nothing.os.device.IDeviceBitmap$Stub$Proxy */
        private static class Proxy implements IDeviceBitmap {
            public static IDeviceBitmap sDefaultImpl;
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

            public Bitmap getLeftBitmap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLeftBitmap();
                    }
                    obtain2.readException();
                    Bitmap bitmap = obtain2.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bitmap;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bitmap getRightBitmap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRightBitmap();
                    }
                    obtain2.readException();
                    Bitmap bitmap = obtain2.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bitmap;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bitmap getCaseBitmap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCaseBitmap();
                    }
                    obtain2.readException();
                    Bitmap bitmap = obtain2.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bitmap;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bitmap getDefaultBitmap() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDefaultBitmap();
                    }
                    obtain2.readException();
                    Bitmap bitmap = obtain2.readInt() != 0 ? (Bitmap) Bitmap.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bitmap;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IDeviceBitmap iDeviceBitmap) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iDeviceBitmap == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iDeviceBitmap;
                return true;
            }
        }

        public static IDeviceBitmap getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
