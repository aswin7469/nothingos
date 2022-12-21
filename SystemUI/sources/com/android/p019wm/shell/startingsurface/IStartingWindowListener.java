package com.android.p019wm.shell.startingsurface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.android.wm.shell.startingsurface.IStartingWindowListener */
public interface IStartingWindowListener extends IInterface {

    /* renamed from: com.android.wm.shell.startingsurface.IStartingWindowListener$Default */
    public static class Default implements IStartingWindowListener {
        public IBinder asBinder() {
            return null;
        }

        public void onTaskLaunching(int i, int i2, int i3) throws RemoteException {
        }
    }

    void onTaskLaunching(int i, int i2, int i3) throws RemoteException;

    /* renamed from: com.android.wm.shell.startingsurface.IStartingWindowListener$Stub */
    public static abstract class Stub extends Binder implements IStartingWindowListener {
        private static final String DESCRIPTOR = "com.android.wm.shell.startingsurface.IStartingWindowListener";
        static final int TRANSACTION_onTaskLaunching = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStartingWindowListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStartingWindowListener)) {
                return new Proxy(iBinder);
            }
            return (IStartingWindowListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onTaskLaunching(parcel.readInt(), parcel.readInt(), parcel.readInt());
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.android.wm.shell.startingsurface.IStartingWindowListener$Stub$Proxy */
        private static class Proxy implements IStartingWindowListener {
            public static IStartingWindowListener sDefaultImpl;
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

            public void onTaskLaunching(int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskLaunching(i, i2, i3);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IStartingWindowListener iStartingWindowListener) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iStartingWindowListener == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iStartingWindowListener;
                return true;
            }
        }

        public static IStartingWindowListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
