package com.google.android.setupcompat.portal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.setupcompat.portal.IPortalProgressCallback;

public interface IPortalProgressService extends IInterface {

    public static class Default implements IPortalProgressService {
        public IBinder asBinder() {
            return null;
        }

        public void onAllowMobileData(boolean z) throws RemoteException {
        }

        public Bundle onGetRemainingValues() throws RemoteException {
            return null;
        }

        public void onPortalSessionStart() throws RemoteException {
        }

        public void onSetCallback(IPortalProgressCallback iPortalProgressCallback) throws RemoteException {
        }

        public void onSuspend() throws RemoteException {
        }
    }

    void onAllowMobileData(boolean z) throws RemoteException;

    Bundle onGetRemainingValues() throws RemoteException;

    void onPortalSessionStart() throws RemoteException;

    void onSetCallback(IPortalProgressCallback iPortalProgressCallback) throws RemoteException;

    void onSuspend() throws RemoteException;

    public static abstract class Stub extends Binder implements IPortalProgressService {
        private static final String DESCRIPTOR = "com.google.android.setupcompat.portal.IPortalProgressService";
        static final int TRANSACTION_onAllowMobileData = 4;
        static final int TRANSACTION_onGetRemainingValues = 5;
        static final int TRANSACTION_onPortalSessionStart = 1;
        static final int TRANSACTION_onSetCallback = 2;
        static final int TRANSACTION_onSuspend = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPortalProgressService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPortalProgressService)) {
                return new Proxy(iBinder);
            }
            return (IPortalProgressService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onPortalSessionStart();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onSetCallback(IPortalProgressCallback.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            } else if (i != 3) {
                boolean z = false;
                if (i == 4) {
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    onAllowMobileData(z);
                    return true;
                } else if (i == 5) {
                    parcel.enforceInterface(DESCRIPTOR);
                    Bundle onGetRemainingValues = onGetRemainingValues();
                    parcel2.writeNoException();
                    if (onGetRemainingValues != null) {
                        parcel2.writeInt(1);
                        onGetRemainingValues.writeToParcel(parcel2, 1);
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
            } else {
                parcel.enforceInterface(DESCRIPTOR);
                onSuspend();
                return true;
            }
        }

        private static class Proxy implements IPortalProgressService {
            public static IPortalProgressService sDefaultImpl;
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

            public void onPortalSessionStart() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onPortalSessionStart();
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onSetCallback(IPortalProgressCallback iPortalProgressCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iPortalProgressCallback != null ? iPortalProgressCallback.asBinder() : null);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onSetCallback(iPortalProgressCallback);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onSuspend() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onSuspend();
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onAllowMobileData(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(4, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onAllowMobileData(z);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public Bundle onGetRemainingValues() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().onGetRemainingValues();
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

        public static boolean setDefaultImpl(IPortalProgressService iPortalProgressService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iPortalProgressService == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iPortalProgressService;
                return true;
            }
        }

        public static IPortalProgressService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
