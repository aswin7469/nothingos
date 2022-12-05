package android.hardware.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IUdfpsOverlayControllerCallback extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.fingerprint.IUdfpsOverlayControllerCallback";

    void onUserCanceled() throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IUdfpsOverlayControllerCallback {
        @Override // android.hardware.fingerprint.IUdfpsOverlayControllerCallback
        public void onUserCanceled() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IUdfpsOverlayControllerCallback {
        static final int TRANSACTION_onUserCanceled = 1;

        public Stub() {
            attachInterface(this, IUdfpsOverlayControllerCallback.DESCRIPTOR);
        }

        public static IUdfpsOverlayControllerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IUdfpsOverlayControllerCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IUdfpsOverlayControllerCallback)) {
                return (IUdfpsOverlayControllerCallback) iin;
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
                    return "onUserCanceled";
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
                    reply.writeString(IUdfpsOverlayControllerCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IUdfpsOverlayControllerCallback.DESCRIPTOR);
                            onUserCanceled();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IUdfpsOverlayControllerCallback {
            public static IUdfpsOverlayControllerCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IUdfpsOverlayControllerCallback.DESCRIPTOR;
            }

            @Override // android.hardware.fingerprint.IUdfpsOverlayControllerCallback
            public void onUserCanceled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IUdfpsOverlayControllerCallback.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onUserCanceled();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IUdfpsOverlayControllerCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IUdfpsOverlayControllerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
