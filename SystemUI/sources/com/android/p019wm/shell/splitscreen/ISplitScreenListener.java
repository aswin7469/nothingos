package com.android.p019wm.shell.splitscreen;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.android.wm.shell.splitscreen.ISplitScreenListener */
public interface ISplitScreenListener extends IInterface {

    /* renamed from: com.android.wm.shell.splitscreen.ISplitScreenListener$Default */
    public static class Default implements ISplitScreenListener {
        public IBinder asBinder() {
            return null;
        }

        public void onStagePositionChanged(int i, int i2) throws RemoteException {
        }

        public void onTaskStageChanged(int i, int i2, boolean z) throws RemoteException {
        }
    }

    void onStagePositionChanged(int i, int i2) throws RemoteException;

    void onTaskStageChanged(int i, int i2, boolean z) throws RemoteException;

    /* renamed from: com.android.wm.shell.splitscreen.ISplitScreenListener$Stub */
    public static abstract class Stub extends Binder implements ISplitScreenListener {
        private static final String DESCRIPTOR = "com.android.wm.shell.splitscreen.ISplitScreenListener";
        static final int TRANSACTION_onStagePositionChanged = 1;
        static final int TRANSACTION_onTaskStageChanged = 2;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISplitScreenListener asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof ISplitScreenListener)) {
                return new Proxy(iBinder);
            }
            return (ISplitScreenListener) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onStagePositionChanged(parcel.readInt(), parcel.readInt());
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onTaskStageChanged(parcel.readInt(), parcel.readInt(), parcel.readInt() != 0);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        /* renamed from: com.android.wm.shell.splitscreen.ISplitScreenListener$Stub$Proxy */
        private static class Proxy implements ISplitScreenListener {
            public static ISplitScreenListener sDefaultImpl;
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

            public void onStagePositionChanged(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onStagePositionChanged(i, i2);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onTaskStageChanged(int i, int i2, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onTaskStageChanged(i, i2, z);
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISplitScreenListener iSplitScreenListener) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iSplitScreenListener == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iSplitScreenListener;
                return true;
            }
        }

        public static ISplitScreenListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
