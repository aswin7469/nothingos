package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.ThreadedRenderer;
/* loaded from: classes.dex */
public interface ITransientNotification extends IInterface {
    void hide() throws RemoteException;

    void show(IBinder iBinder) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements ITransientNotification {
        @Override // android.app.ITransientNotification
        public void show(IBinder windowToken) throws RemoteException {
        }

        @Override // android.app.ITransientNotification
        public void hide() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ITransientNotification {
        public static final String DESCRIPTOR = "android.app.ITransientNotification";
        static final int TRANSACTION_hide = 2;
        static final int TRANSACTION_show = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITransientNotification asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ITransientNotification)) {
                return (ITransientNotification) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return ThreadedRenderer.OVERDRAW_PROPERTY_SHOW;
                case 2:
                    return "hide";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(DESCRIPTOR);
                            IBinder _arg0 = data.readStrongBinder();
                            show(_arg0);
                            return true;
                        case 2:
                            data.enforceInterface(DESCRIPTOR);
                            hide();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements ITransientNotification {
            public static ITransientNotification sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // android.app.ITransientNotification
            public void show(IBinder windowToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().show(windowToken);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.app.ITransientNotification
            public void hide() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().hide();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ITransientNotification impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ITransientNotification getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
