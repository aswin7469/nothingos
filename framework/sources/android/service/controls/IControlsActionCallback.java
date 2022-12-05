package android.service.controls;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes3.dex */
public interface IControlsActionCallback extends IInterface {
    public static final String DESCRIPTOR = "android.service.controls.IControlsActionCallback";

    void accept(IBinder iBinder, String str, int i) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IControlsActionCallback {
        @Override // android.service.controls.IControlsActionCallback
        public void accept(IBinder token, String controlId, int response) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IControlsActionCallback {
        static final int TRANSACTION_accept = 1;

        public Stub() {
            attachInterface(this, IControlsActionCallback.DESCRIPTOR);
        }

        public static IControlsActionCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IControlsActionCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IControlsActionCallback)) {
                return (IControlsActionCallback) iin;
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
                    return "accept";
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
                    reply.writeString(IControlsActionCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IControlsActionCallback.DESCRIPTOR);
                            IBinder _arg0 = data.readStrongBinder();
                            String _arg1 = data.readString();
                            int _arg2 = data.readInt();
                            accept(_arg0, _arg1, _arg2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IControlsActionCallback {
            public static IControlsActionCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IControlsActionCallback.DESCRIPTOR;
            }

            @Override // android.service.controls.IControlsActionCallback
            public void accept(IBinder token, String controlId, int response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IControlsActionCallback.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(controlId);
                    _data.writeInt(response);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().accept(token, controlId, response);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IControlsActionCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IControlsActionCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
