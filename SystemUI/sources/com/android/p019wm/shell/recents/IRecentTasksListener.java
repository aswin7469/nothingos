package com.android.p019wm.shell.recents;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.android.wm.shell.recents.IRecentTasksListener */
public interface IRecentTasksListener extends IInterface {

    /* renamed from: com.android.wm.shell.recents.IRecentTasksListener$Default */
    public static class Default implements IRecentTasksListener {
        public IBinder asBinder() {
            return null;
        }

        public void onRecentTasksChanged() throws RemoteException {
        }
    }

    void onRecentTasksChanged() throws RemoteException;

    /* renamed from: com.android.wm.shell.recents.IRecentTasksListener$Stub */
    public static abstract class Stub extends Binder implements IRecentTasksListener {
        private static final String DESCRIPTOR = "com.android.wm.shell.recents.IRecentTasksListener";
        static final int TRANSACTION_onRecentTasksChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRecentTasksListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IRecentTasksListener)) {
                return new Proxy(iBinder);
            }
            return (IRecentTasksListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onRecentTasksChanged();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.android.wm.shell.recents.IRecentTasksListener$Stub$Proxy */
        private static class Proxy implements IRecentTasksListener {
            public static IRecentTasksListener sDefaultImpl;
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

            public void onRecentTasksChanged() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onRecentTasksChanged();
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IRecentTasksListener iRecentTasksListener) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iRecentTasksListener == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iRecentTasksListener;
                return true;
            }
        }

        public static IRecentTasksListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
