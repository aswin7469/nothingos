package android.window;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOnBackInvokedCallback extends IInterface {

    public static class Default implements IOnBackInvokedCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onBackCancelled() throws RemoteException {
        }

        public void onBackInvoked() throws RemoteException {
        }

        public void onBackProgressed(BackEvent backEvent) throws RemoteException {
        }

        public void onBackStarted() throws RemoteException {
        }
    }

    void onBackCancelled() throws RemoteException;

    void onBackInvoked() throws RemoteException;

    void onBackProgressed(BackEvent backEvent) throws RemoteException;

    void onBackStarted() throws RemoteException;

    public static abstract class Stub extends Binder implements IOnBackInvokedCallback {
        private static final String DESCRIPTOR = "android.window.IOnBackInvokedCallback";
        static final int TRANSACTION_onBackCancelled = 3;
        static final int TRANSACTION_onBackInvoked = 4;
        static final int TRANSACTION_onBackProgressed = 2;
        static final int TRANSACTION_onBackStarted = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOnBackInvokedCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IOnBackInvokedCallback)) {
                return new Proxy(iBinder);
            }
            return (IOnBackInvokedCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface(DESCRIPTOR);
                onBackStarted();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(DESCRIPTOR);
                onBackProgressed(parcel.readInt() != 0 ? (BackEvent) BackEvent.CREATOR.createFromParcel(parcel) : null);
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(DESCRIPTOR);
                onBackCancelled();
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(DESCRIPTOR);
                onBackInvoked();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
        }

        private static class Proxy implements IOnBackInvokedCallback {
            public static IOnBackInvokedCallback sDefaultImpl;
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

            public void onBackStarted() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onBackStarted();
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onBackProgressed(BackEvent backEvent) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (backEvent != null) {
                        obtain.writeInt(1);
                        backEvent.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(2, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onBackProgressed(backEvent);
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onBackCancelled() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onBackCancelled();
                    }
                } finally {
                    obtain.recycle();
                }
            }

            public void onBackInvoked() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, obtain, (Parcel) null, 1) || Stub.getDefaultImpl() == null) {
                        obtain.recycle();
                    } else {
                        Stub.getDefaultImpl().onBackInvoked();
                    }
                } finally {
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IOnBackInvokedCallback iOnBackInvokedCallback) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iOnBackInvokedCallback == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iOnBackInvokedCallback;
                return true;
            }
        }

        public static IOnBackInvokedCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
