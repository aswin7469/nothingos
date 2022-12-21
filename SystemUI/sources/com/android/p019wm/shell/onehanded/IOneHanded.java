package com.android.p019wm.shell.onehanded;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.android.wm.shell.onehanded.IOneHanded */
public interface IOneHanded extends IInterface {

    /* renamed from: com.android.wm.shell.onehanded.IOneHanded$Default */
    public static class Default implements IOneHanded {
        public IBinder asBinder() {
            return null;
        }

        public void startOneHanded() throws RemoteException {
        }

        public void stopOneHanded() throws RemoteException {
        }
    }

    void startOneHanded() throws RemoteException;

    void stopOneHanded() throws RemoteException;

    /* renamed from: com.android.wm.shell.onehanded.IOneHanded$Stub */
    public static abstract class Stub extends Binder implements IOneHanded {
        private static final String DESCRIPTOR = "com.android.wm.shell.onehanded.IOneHanded";
        static final int TRANSACTION_startOneHanded = 2;
        static final int TRANSACTION_stopOneHanded = 3;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOneHanded asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOneHanded)) {
                return new Proxy(iBinder);
            }
            return (IOneHanded) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                startOneHanded();
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                stopOneHanded();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.android.wm.shell.onehanded.IOneHanded$Stub$Proxy */
        private static class Proxy implements IOneHanded {
            public static IOneHanded sDefaultImpl;
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

            public void startOneHanded() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().startOneHanded();
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void stopOneHanded() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().stopOneHanded();
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IOneHanded iOneHanded) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iOneHanded == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iOneHanded;
                return true;
            }
        }

        public static IOneHanded getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
