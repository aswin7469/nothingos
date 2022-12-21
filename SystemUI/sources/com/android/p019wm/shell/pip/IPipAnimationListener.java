package com.android.p019wm.shell.pip;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.android.wm.shell.pip.IPipAnimationListener */
public interface IPipAnimationListener extends IInterface {

    /* renamed from: com.android.wm.shell.pip.IPipAnimationListener$Default */
    public static class Default implements IPipAnimationListener {
        public IBinder asBinder() {
            return null;
        }

        public void onExpandPip() throws RemoteException {
        }

        public void onPipAnimationStarted() throws RemoteException {
        }

        public void onPipResourceDimensionsChanged(int i, int i2) throws RemoteException {
        }
    }

    void onExpandPip() throws RemoteException;

    void onPipAnimationStarted() throws RemoteException;

    void onPipResourceDimensionsChanged(int i, int i2) throws RemoteException;

    /* renamed from: com.android.wm.shell.pip.IPipAnimationListener$Stub */
    public static abstract class Stub extends Binder implements IPipAnimationListener {
        private static final String DESCRIPTOR = "com.android.wm.shell.pip.IPipAnimationListener";
        static final int TRANSACTION_onExpandPip = 3;
        static final int TRANSACTION_onPipAnimationStarted = 1;
        static final int TRANSACTION_onPipResourceDimensionsChanged = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPipAnimationListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IPipAnimationListener)) {
                return new Proxy(iBinder);
            }
            return (IPipAnimationListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onPipAnimationStarted();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onPipResourceDimensionsChanged(parcel.readInt(), parcel.readInt());
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                onExpandPip();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.android.wm.shell.pip.IPipAnimationListener$Stub$Proxy */
        private static class Proxy implements IPipAnimationListener {
            public static IPipAnimationListener sDefaultImpl;
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

            public void onPipAnimationStarted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onPipAnimationStarted();
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onPipResourceDimensionsChanged(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onPipResourceDimensionsChanged(i, i2);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onExpandPip() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onExpandPip();
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IPipAnimationListener iPipAnimationListener) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iPipAnimationListener == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iPipAnimationListener;
                return true;
            }
        }

        public static IPipAnimationListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
