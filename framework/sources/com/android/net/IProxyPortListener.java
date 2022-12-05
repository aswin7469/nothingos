package com.android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IProxyPortListener extends IInterface {
    public static final String DESCRIPTOR = "com.android.net.IProxyPortListener";

    void setProxyPort(int i) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IProxyPortListener {
        @Override // com.android.net.IProxyPortListener
        public void setProxyPort(int port) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IProxyPortListener {
        static final int TRANSACTION_setProxyPort = 1;

        public Stub() {
            attachInterface(this, IProxyPortListener.DESCRIPTOR);
        }

        public static IProxyPortListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IProxyPortListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IProxyPortListener)) {
                return (IProxyPortListener) iin;
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
                    return "setProxyPort";
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
                    reply.writeString(IProxyPortListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IProxyPortListener.DESCRIPTOR);
                            int _arg0 = data.readInt();
                            setProxyPort(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IProxyPortListener {
            public static IProxyPortListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IProxyPortListener.DESCRIPTOR;
            }

            @Override // com.android.net.IProxyPortListener
            public void setProxyPort(int port) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IProxyPortListener.DESCRIPTOR);
                    _data.writeInt(port);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setProxyPort(port);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IProxyPortListener impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IProxyPortListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
