package com.android.internal.inputmethod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.sysaac.haptic.i;
/* loaded from: classes4.dex */
public interface IInputContentUriToken extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.inputmethod.IInputContentUriToken";

    void release() throws RemoteException;

    void take() throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IInputContentUriToken {
        @Override // com.android.internal.inputmethod.IInputContentUriToken
        public void take() throws RemoteException {
        }

        @Override // com.android.internal.inputmethod.IInputContentUriToken
        public void release() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IInputContentUriToken {
        static final int TRANSACTION_release = 2;
        static final int TRANSACTION_take = 1;

        public Stub() {
            attachInterface(this, IInputContentUriToken.DESCRIPTOR);
        }

        public static IInputContentUriToken asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IInputContentUriToken.DESCRIPTOR);
            if (iin != null && (iin instanceof IInputContentUriToken)) {
                return (IInputContentUriToken) iin;
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
                    return "take";
                case 2:
                    return i.c;
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
                    reply.writeString(IInputContentUriToken.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IInputContentUriToken.DESCRIPTOR);
                            take();
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(IInputContentUriToken.DESCRIPTOR);
                            release();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IInputContentUriToken {
            public static IInputContentUriToken sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IInputContentUriToken.DESCRIPTOR;
            }

            @Override // com.android.internal.inputmethod.IInputContentUriToken
            public void take() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IInputContentUriToken.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().take();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.inputmethod.IInputContentUriToken
            public void release() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IInputContentUriToken.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().release();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInputContentUriToken impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInputContentUriToken getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
