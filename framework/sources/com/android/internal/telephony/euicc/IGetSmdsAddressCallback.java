package com.android.internal.telephony.euicc;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IGetSmdsAddressCallback extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.euicc.IGetSmdsAddressCallback";

    void onComplete(int i, String str) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IGetSmdsAddressCallback {
        @Override // com.android.internal.telephony.euicc.IGetSmdsAddressCallback
        public void onComplete(int resultCode, String address) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IGetSmdsAddressCallback {
        static final int TRANSACTION_onComplete = 1;

        public Stub() {
            attachInterface(this, IGetSmdsAddressCallback.DESCRIPTOR);
        }

        public static IGetSmdsAddressCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IGetSmdsAddressCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IGetSmdsAddressCallback)) {
                return (IGetSmdsAddressCallback) iin;
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
                    reply.writeString(IGetSmdsAddressCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IGetSmdsAddressCallback.DESCRIPTOR);
                            int _arg0 = data.readInt();
                            String _arg1 = data.readString();
                            onComplete(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IGetSmdsAddressCallback {
            public static IGetSmdsAddressCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IGetSmdsAddressCallback.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.euicc.IGetSmdsAddressCallback
            public void onComplete(int resultCode, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IGetSmdsAddressCallback.DESCRIPTOR);
                    _data.writeInt(resultCode);
                    _data.writeString(address);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onComplete(resultCode, address);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IGetSmdsAddressCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IGetSmdsAddressCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
