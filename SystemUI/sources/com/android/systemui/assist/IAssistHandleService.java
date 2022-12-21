package com.android.systemui.assist;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAssistHandleService extends IInterface {

    public static class Default implements IAssistHandleService {
        public IBinder asBinder() {
            return null;
        }

        public void requestAssistHandles() throws RemoteException {
        }
    }

    void requestAssistHandles() throws RemoteException;

    public static abstract class Stub extends Binder implements IAssistHandleService {
        private static final String DESCRIPTOR = "com.android.systemui.assist.IAssistHandleService";
        static final int TRANSACTION_requestAssistHandles = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAssistHandleService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IAssistHandleService)) {
                return new Proxy(iBinder);
            }
            return (IAssistHandleService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                requestAssistHandles();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IAssistHandleService {
            public static IAssistHandleService sDefaultImpl;
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

            public void requestAssistHandles() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().requestAssistHandles();
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAssistHandleService iAssistHandleService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iAssistHandleService == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iAssistHandleService;
                return true;
            }
        }

        public static IAssistHandleService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
