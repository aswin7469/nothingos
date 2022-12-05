package com.android.internal.inputmethod;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.inputmethod.InputMethodSubtype;
import java.util.List;
/* loaded from: classes4.dex */
public interface IInputMethodSubtypeListResultCallback extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.inputmethod.IInputMethodSubtypeListResultCallback";

    void onError(ThrowableHolder throwableHolder) throws RemoteException;

    void onResult(List<InputMethodSubtype> list) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IInputMethodSubtypeListResultCallback {
        @Override // com.android.internal.inputmethod.IInputMethodSubtypeListResultCallback
        public void onResult(List<InputMethodSubtype> result) throws RemoteException {
        }

        @Override // com.android.internal.inputmethod.IInputMethodSubtypeListResultCallback
        public void onError(ThrowableHolder exception) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IInputMethodSubtypeListResultCallback {
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onResult = 1;

        public Stub() {
            attachInterface(this, IInputMethodSubtypeListResultCallback.DESCRIPTOR);
        }

        public static IInputMethodSubtypeListResultCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IInputMethodSubtypeListResultCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IInputMethodSubtypeListResultCallback)) {
                return (IInputMethodSubtypeListResultCallback) iin;
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
                    return "onResult";
                case 2:
                    return "onError";
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
            ThrowableHolder _arg0;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IInputMethodSubtypeListResultCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IInputMethodSubtypeListResultCallback.DESCRIPTOR);
                            List<InputMethodSubtype> _arg02 = data.createTypedArrayList(InputMethodSubtype.CREATOR);
                            onResult(_arg02);
                            return true;
                        case 2:
                            data.enforceInterface(IInputMethodSubtypeListResultCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = ThrowableHolder.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            onError(_arg0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IInputMethodSubtypeListResultCallback {
            public static IInputMethodSubtypeListResultCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IInputMethodSubtypeListResultCallback.DESCRIPTOR;
            }

            @Override // com.android.internal.inputmethod.IInputMethodSubtypeListResultCallback
            public void onResult(List<InputMethodSubtype> result) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IInputMethodSubtypeListResultCallback.DESCRIPTOR);
                    _data.writeTypedList(result);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onResult(result);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.android.internal.inputmethod.IInputMethodSubtypeListResultCallback
            public void onError(ThrowableHolder exception) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IInputMethodSubtypeListResultCallback.DESCRIPTOR);
                    if (exception != null) {
                        _data.writeInt(1);
                        exception.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onError(exception);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInputMethodSubtypeListResultCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInputMethodSubtypeListResultCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}