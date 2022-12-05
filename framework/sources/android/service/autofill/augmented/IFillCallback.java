package android.service.autofill.augmented;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.autofill.Dataset;
import java.util.List;
/* loaded from: classes2.dex */
public interface IFillCallback extends IInterface {
    public static final String DESCRIPTOR = "android.service.autofill.augmented.IFillCallback";

    void cancel() throws RemoteException;

    boolean isCompleted() throws RemoteException;

    void onCancellable(ICancellationSignal iCancellationSignal) throws RemoteException;

    void onSuccess(List<Dataset> list, Bundle bundle, boolean z) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IFillCallback {
        @Override // android.service.autofill.augmented.IFillCallback
        public void onCancellable(ICancellationSignal cancellation) throws RemoteException {
        }

        @Override // android.service.autofill.augmented.IFillCallback
        public void onSuccess(List<Dataset> inlineSuggestionsData, Bundle clientState, boolean showingFillWindow) throws RemoteException {
        }

        @Override // android.service.autofill.augmented.IFillCallback
        public boolean isCompleted() throws RemoteException {
            return false;
        }

        @Override // android.service.autofill.augmented.IFillCallback
        public void cancel() throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IFillCallback {
        static final int TRANSACTION_cancel = 4;
        static final int TRANSACTION_isCompleted = 3;
        static final int TRANSACTION_onCancellable = 1;
        static final int TRANSACTION_onSuccess = 2;

        public Stub() {
            attachInterface(this, IFillCallback.DESCRIPTOR);
        }

        public static IFillCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IFillCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IFillCallback)) {
                return (IFillCallback) iin;
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
                    return "onCancellable";
                case 2:
                    return "onSuccess";
                case 3:
                    return "isCompleted";
                case 4:
                    return "cancel";
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
            Bundle _arg1;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IFillCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IFillCallback.DESCRIPTOR);
                            ICancellationSignal _arg0 = ICancellationSignal.Stub.asInterface(data.readStrongBinder());
                            onCancellable(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(IFillCallback.DESCRIPTOR);
                            List<Dataset> _arg02 = data.createTypedArrayList(Dataset.CREATOR);
                            if (data.readInt() != 0) {
                                _arg1 = Bundle.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            boolean _arg2 = data.readInt() != 0;
                            onSuccess(_arg02, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(IFillCallback.DESCRIPTOR);
                            boolean isCompleted = isCompleted();
                            reply.writeNoException();
                            reply.writeInt(isCompleted ? 1 : 0);
                            return true;
                        case 4:
                            data.enforceInterface(IFillCallback.DESCRIPTOR);
                            cancel();
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IFillCallback {
            public static IFillCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IFillCallback.DESCRIPTOR;
            }

            @Override // android.service.autofill.augmented.IFillCallback
            public void onCancellable(ICancellationSignal cancellation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFillCallback.DESCRIPTOR);
                    _data.writeStrongBinder(cancellation != null ? cancellation.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onCancellable(cancellation);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.autofill.augmented.IFillCallback
            public void onSuccess(List<Dataset> inlineSuggestionsData, Bundle clientState, boolean showingFillWindow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFillCallback.DESCRIPTOR);
                    _data.writeTypedList(inlineSuggestionsData);
                    int i = 1;
                    if (clientState != null) {
                        _data.writeInt(1);
                        clientState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!showingFillWindow) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSuccess(inlineSuggestionsData, clientState, showingFillWindow);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.autofill.augmented.IFillCallback
            public boolean isCompleted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFillCallback.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCompleted();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.service.autofill.augmented.IFillCallback
            public void cancel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFillCallback.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancel();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFillCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFillCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
