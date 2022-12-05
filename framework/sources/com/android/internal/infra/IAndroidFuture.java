package com.android.internal.infra;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IAndroidFuture extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.infra.IAndroidFuture";

    void complete(AndroidFuture androidFuture) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IAndroidFuture {
        @Override // com.android.internal.infra.IAndroidFuture
        public void complete(AndroidFuture resultContainer) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IAndroidFuture {
        static final int TRANSACTION_complete = 1;

        public Stub() {
            attachInterface(this, IAndroidFuture.DESCRIPTOR);
        }

        public static IAndroidFuture asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAndroidFuture.DESCRIPTOR);
            if (iin != null && (iin instanceof IAndroidFuture)) {
                return (IAndroidFuture) iin;
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
                    return "complete";
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
            AndroidFuture _arg0;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IAndroidFuture.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IAndroidFuture.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = AndroidFuture.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            complete(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IAndroidFuture {
            public static IAndroidFuture sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAndroidFuture.DESCRIPTOR;
            }

            @Override // com.android.internal.infra.IAndroidFuture
            public void complete(AndroidFuture resultContainer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAndroidFuture.DESCRIPTOR);
                    if (resultContainer != null) {
                        _data.writeInt(1);
                        resultContainer.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().complete(resultContainer);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAndroidFuture impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAndroidFuture getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
