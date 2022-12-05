package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IResetMemoryCallback extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IResetMemoryCallback";

    void onComplete(int i) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IResetMemoryCallback {
        @Override // com.android.internal.telephony.euicc.IResetMemoryCallback
        public void onComplete(int resultCode) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IResetMemoryCallback {
        static final int TRANSACTION_onComplete = 1;

        public Stub() {
            attachInterface(this, IResetMemoryCallback.DESCRIPTOR);
        }

        public static IResetMemoryCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IResetMemoryCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IResetMemoryCallback)) {
                return (IResetMemoryCallback) iin;
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
                    return "onComplete";
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
                    reply.writeString(IResetMemoryCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IResetMemoryCallback.DESCRIPTOR);
                            int _arg0 = data.readInt();
                            onComplete(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IResetMemoryCallback {
            public static IResetMemoryCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IResetMemoryCallback.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.euicc.IResetMemoryCallback
            public void onComplete(int resultCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IResetMemoryCallback.DESCRIPTOR);
                    _data.writeInt(resultCode);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onComplete(resultCode);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IResetMemoryCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IResetMemoryCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
