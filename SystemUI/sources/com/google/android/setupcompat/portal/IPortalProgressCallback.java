package com.google.android.setupcompat.portal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPortalProgressCallback extends IInterface {

    public static class Default implements IPortalProgressCallback {
        public IBinder asBinder() {
            return null;
        }

        public Bundle setComplete(int i, int i2, int[] iArr) throws RemoteException {
            return null;
        }

        public Bundle setFailure(int i, int i2, int[] iArr) throws RemoteException {
            return null;
        }

        public Bundle setPendingReason(int i, int i2, int[] iArr, int i3) throws RemoteException {
            return null;
        }

        public Bundle setProgressCount(int i, int i2, int i3) throws RemoteException {
            return null;
        }

        public Bundle setProgressPercentage(int i) throws RemoteException {
            return null;
        }

        public Bundle setSummary(String str) throws RemoteException {
            return null;
        }
    }

    Bundle setComplete(int i, int i2, int[] iArr) throws RemoteException;

    Bundle setFailure(int i, int i2, int[] iArr) throws RemoteException;

    Bundle setPendingReason(int i, int i2, int[] iArr, int i3) throws RemoteException;

    Bundle setProgressCount(int i, int i2, int i3) throws RemoteException;

    Bundle setProgressPercentage(int i) throws RemoteException;

    Bundle setSummary(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IPortalProgressCallback {
        private static final String DESCRIPTOR = "com.google.android.setupcompat.portal.IPortalProgressCallback";
        static final int TRANSACTION_setComplete = 5;
        static final int TRANSACTION_setFailure = 6;
        static final int TRANSACTION_setPendingReason = 4;
        static final int TRANSACTION_setProgressCount = 1;
        static final int TRANSACTION_setProgressPercentage = 2;
        static final int TRANSACTION_setSummary = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPortalProgressCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPortalProgressCallback)) {
                return new Proxy(iBinder);
            }
            return (IPortalProgressCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle progressCount = setProgressCount(parcel.readInt(), parcel.readInt(), parcel.readInt());
                        parcel2.writeNoException();
                        if (progressCount != null) {
                            parcel2.writeInt(1);
                            progressCount.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle progressPercentage = setProgressPercentage(parcel.readInt());
                        parcel2.writeNoException();
                        if (progressPercentage != null) {
                            parcel2.writeInt(1);
                            progressPercentage.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle summary = setSummary(parcel.readString());
                        parcel2.writeNoException();
                        if (summary != null) {
                            parcel2.writeInt(1);
                            summary.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle pendingReason = setPendingReason(parcel.readInt(), parcel.readInt(), parcel.createIntArray(), parcel.readInt());
                        parcel2.writeNoException();
                        if (pendingReason != null) {
                            parcel2.writeInt(1);
                            pendingReason.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle complete = setComplete(parcel.readInt(), parcel.readInt(), parcel.createIntArray());
                        parcel2.writeNoException();
                        if (complete != null) {
                            parcel2.writeInt(1);
                            complete.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(DESCRIPTOR);
                        Bundle failure = setFailure(parcel.readInt(), parcel.readInt(), parcel.createIntArray());
                        parcel2.writeNoException();
                        if (failure != null) {
                            parcel2.writeInt(1);
                            failure.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IPortalProgressCallback {
            public static IPortalProgressCallback sDefaultImpl;
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

            public Bundle setProgressCount(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setProgressCount(i, i2, i3);
                    }
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle setProgressPercentage(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setProgressPercentage(i);
                    }
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle setSummary(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(3, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setSummary(str);
                    }
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle setPendingReason(int i, int i2, int[] iArr, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeIntArray(iArr);
                    obtain.writeInt(i3);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setPendingReason(i, i2, iArr, i3);
                    }
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle setComplete(int i, int i2, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setComplete(i, i2, iArr);
                    }
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle setFailure(int i, int i2, int[] iArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeIntArray(iArr);
                    if (!this.mRemote.transact(6, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setFailure(i, i2, iArr);
                    }
                    obtain2.readException();
                    Bundle bundle = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IPortalProgressCallback iPortalProgressCallback) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iPortalProgressCallback == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iPortalProgressCallback;
                return true;
            }
        }

        public static IPortalProgressCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
