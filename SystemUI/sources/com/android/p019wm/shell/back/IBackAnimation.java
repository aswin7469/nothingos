package com.android.p019wm.shell.back;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.window.IOnBackInvokedCallback;

/* renamed from: com.android.wm.shell.back.IBackAnimation */
public interface IBackAnimation extends IInterface {

    /* renamed from: com.android.wm.shell.back.IBackAnimation$Default */
    public static class Default implements IBackAnimation {
        public IBinder asBinder() {
            return null;
        }

        public void clearBackToLauncherCallback() throws RemoteException {
        }

        public void onBackToLauncherAnimationFinished() throws RemoteException {
        }

        public void setBackToLauncherCallback(IOnBackInvokedCallback iOnBackInvokedCallback) throws RemoteException {
        }
    }

    void clearBackToLauncherCallback() throws RemoteException;

    void onBackToLauncherAnimationFinished() throws RemoteException;

    void setBackToLauncherCallback(IOnBackInvokedCallback iOnBackInvokedCallback) throws RemoteException;

    /* renamed from: com.android.wm.shell.back.IBackAnimation$Stub */
    public static abstract class Stub extends Binder implements IBackAnimation {
        private static final String DESCRIPTOR = "com.android.wm.shell.back.IBackAnimation";
        static final int TRANSACTION_clearBackToLauncherCallback = 2;
        static final int TRANSACTION_onBackToLauncherAnimationFinished = 3;
        static final int TRANSACTION_setBackToLauncherCallback = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBackAnimation asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IBackAnimation)) {
                return new Proxy(iBinder);
            }
            return (IBackAnimation) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                setBackToLauncherCallback(IOnBackInvokedCallback.Stub.asInterface(parcel.readStrongBinder()));
                parcel2.writeNoException();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                clearBackToLauncherCallback();
                parcel2.writeNoException();
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                onBackToLauncherAnimationFinished();
                parcel2.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.android.wm.shell.back.IBackAnimation$Stub$Proxy */
        private static class Proxy implements IBackAnimation {
            public static IBackAnimation sDefaultImpl;
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

            public void setBackToLauncherCallback(IOnBackInvokedCallback iOnBackInvokedCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iOnBackInvokedCallback != null ? iOnBackInvokedCallback.asBinder() : null);
                    if (this.mRemote.transact(1, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setBackToLauncherCallback(iOnBackInvokedCallback);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void clearBackToLauncherCallback() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearBackToLauncherCallback();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void onBackToLauncherAnimationFinished() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBackToLauncherAnimationFinished();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBackAnimation iBackAnimation) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iBackAnimation == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iBackAnimation;
                return true;
            }
        }

        public static IBackAnimation getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
