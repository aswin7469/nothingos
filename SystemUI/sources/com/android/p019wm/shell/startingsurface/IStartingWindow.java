package com.android.p019wm.shell.startingsurface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.p019wm.shell.startingsurface.IStartingWindowListener;

/* renamed from: com.android.wm.shell.startingsurface.IStartingWindow */
public interface IStartingWindow extends IInterface {

    /* renamed from: com.android.wm.shell.startingsurface.IStartingWindow$Default */
    public static class Default implements IStartingWindow {
        public IBinder asBinder() {
            return null;
        }

        public void setStartingWindowListener(IStartingWindowListener iStartingWindowListener) throws RemoteException {
        }
    }

    void setStartingWindowListener(IStartingWindowListener iStartingWindowListener) throws RemoteException;

    /* renamed from: com.android.wm.shell.startingsurface.IStartingWindow$Stub */
    public static abstract class Stub extends Binder implements IStartingWindow {
        private static final String DESCRIPTOR = "com.android.wm.shell.startingsurface.IStartingWindow";
        static final int TRANSACTION_setStartingWindowListener = 44;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStartingWindow asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IStartingWindow)) {
                return new Proxy(iBinder);
            }
            return (IStartingWindow) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 44) {
                parcel.enforceInterface(DESCRIPTOR);
                setStartingWindowListener(IStartingWindowListener.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.android.wm.shell.startingsurface.IStartingWindow$Stub$Proxy */
        private static class Proxy implements IStartingWindow {
            public static IStartingWindow sDefaultImpl;
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

            public void setStartingWindowListener(IStartingWindowListener iStartingWindowListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iStartingWindowListener != null ? iStartingWindowListener.asBinder() : null);
                    if (this.mRemote.transact(44, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().setStartingWindowListener(iStartingWindowListener);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IStartingWindow iStartingWindow) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iStartingWindow == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iStartingWindow;
                return true;
            }
        }

        public static IStartingWindow getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
