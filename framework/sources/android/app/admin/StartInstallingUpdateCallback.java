package android.app.admin;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface StartInstallingUpdateCallback extends IInterface {
    public static final String DESCRIPTOR = "android.app.admin.StartInstallingUpdateCallback";

    void onStartInstallingUpdateError(int i, String str) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements StartInstallingUpdateCallback {
        @Override // android.app.admin.StartInstallingUpdateCallback
        public void onStartInstallingUpdateError(int errorCode, String errorMessage) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements StartInstallingUpdateCallback {
        static final int TRANSACTION_onStartInstallingUpdateError = 1;

        public Stub() {
            attachInterface(this, StartInstallingUpdateCallback.DESCRIPTOR);
        }

        public static StartInstallingUpdateCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(StartInstallingUpdateCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof StartInstallingUpdateCallback)) {
                return (StartInstallingUpdateCallback) iin;
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
                    return "onStartInstallingUpdateError";
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
                    reply.writeString(StartInstallingUpdateCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(StartInstallingUpdateCallback.DESCRIPTOR);
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            onStartInstallingUpdateError(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements StartInstallingUpdateCallback {
            public static StartInstallingUpdateCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return StartInstallingUpdateCallback.DESCRIPTOR;
            }

            @Override // android.app.admin.StartInstallingUpdateCallback
            public void onStartInstallingUpdateError(int errorCode, String errorMessage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(StartInstallingUpdateCallback.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    _data.writeString(errorMessage);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStartInstallingUpdateError(errorCode, errorMessage);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(StartInstallingUpdateCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static StartInstallingUpdateCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
