package android.nfc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface INfcControllerAlwaysOnListener extends IInterface {
    public static final String DESCRIPTOR = "android.nfc.INfcControllerAlwaysOnListener";

    void onControllerAlwaysOnChanged(boolean z) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements INfcControllerAlwaysOnListener {
        @Override // android.nfc.INfcControllerAlwaysOnListener
        public void onControllerAlwaysOnChanged(boolean isEnabled) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements INfcControllerAlwaysOnListener {
        static final int TRANSACTION_onControllerAlwaysOnChanged = 1;

        public Stub() {
            attachInterface(this, INfcControllerAlwaysOnListener.DESCRIPTOR);
        }

        public static INfcControllerAlwaysOnListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(INfcControllerAlwaysOnListener.DESCRIPTOR);
            if (iin != null && (iin instanceof INfcControllerAlwaysOnListener)) {
                return (INfcControllerAlwaysOnListener) iin;
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
                    return "onControllerAlwaysOnChanged";
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
                    reply.writeString(INfcControllerAlwaysOnListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(INfcControllerAlwaysOnListener.DESCRIPTOR);
                            boolean _arg0 = data.readInt() != 0;
                            onControllerAlwaysOnChanged(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements INfcControllerAlwaysOnListener {
            public static INfcControllerAlwaysOnListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return INfcControllerAlwaysOnListener.DESCRIPTOR;
            }

            @Override // android.nfc.INfcControllerAlwaysOnListener
            public void onControllerAlwaysOnChanged(boolean isEnabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(INfcControllerAlwaysOnListener.DESCRIPTOR);
                    _data.writeInt(isEnabled ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onControllerAlwaysOnChanged(isEnabled);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(INfcControllerAlwaysOnListener impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static INfcControllerAlwaysOnListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
