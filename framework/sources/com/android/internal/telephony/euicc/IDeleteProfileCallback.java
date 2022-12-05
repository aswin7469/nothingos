package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IDeleteProfileCallback extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IDeleteProfileCallback";

    void onComplete(int i) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IDeleteProfileCallback {
        @Override // com.android.internal.telephony.euicc.IDeleteProfileCallback
        public void onComplete(int resultCode) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IDeleteProfileCallback {
        static final int TRANSACTION_onComplete = 1;

        public Stub() {
            attachInterface(this, IDeleteProfileCallback.DESCRIPTOR);
        }

        public static IDeleteProfileCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IDeleteProfileCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IDeleteProfileCallback)) {
                return (IDeleteProfileCallback) iin;
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
                    reply.writeString(IDeleteProfileCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IDeleteProfileCallback.DESCRIPTOR);
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
        public static class Proxy implements IDeleteProfileCallback {
            public static IDeleteProfileCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IDeleteProfileCallback.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.euicc.IDeleteProfileCallback
            public void onComplete(int resultCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IDeleteProfileCallback.DESCRIPTOR);
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

        public static boolean setDefaultImpl(IDeleteProfileCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IDeleteProfileCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
