package android.service.attention;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.service.attention.IAttentionCallback;
/* loaded from: classes2.dex */
public interface IAttentionService extends IInterface {
    public static final String DESCRIPTOR = "android.service.attention.IAttentionService";

    void cancelAttentionCheck(IAttentionCallback iAttentionCallback) throws RemoteException;

    void checkAttention(IAttentionCallback iAttentionCallback) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IAttentionService {
        @Override // android.service.attention.IAttentionService
        public void checkAttention(IAttentionCallback callback) throws RemoteException {
        }

        @Override // android.service.attention.IAttentionService
        public void cancelAttentionCheck(IAttentionCallback callback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IAttentionService {
        static final int TRANSACTION_cancelAttentionCheck = 2;
        static final int TRANSACTION_checkAttention = 1;

        public Stub() {
            attachInterface(this, IAttentionService.DESCRIPTOR);
        }

        public static IAttentionService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IAttentionService.DESCRIPTOR);
            if (iin != null && (iin instanceof IAttentionService)) {
                return (IAttentionService) iin;
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
                    return "checkAttention";
                case 2:
                    return "cancelAttentionCheck";
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
                    reply.writeString(IAttentionService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IAttentionService.DESCRIPTOR);
                            IAttentionCallback _arg0 = IAttentionCallback.Stub.asInterface(data.readStrongBinder());
                            checkAttention(_arg0);
                            return true;
                        case 2:
                            data.enforceInterface(IAttentionService.DESCRIPTOR);
                            IAttentionCallback _arg02 = IAttentionCallback.Stub.asInterface(data.readStrongBinder());
                            cancelAttentionCheck(_arg02);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IAttentionService {
            public static IAttentionService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IAttentionService.DESCRIPTOR;
            }

            @Override // android.service.attention.IAttentionService
            public void checkAttention(IAttentionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAttentionService.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().checkAttention(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.service.attention.IAttentionService
            public void cancelAttentionCheck(IAttentionCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IAttentionService.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelAttentionCheck(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IAttentionService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IAttentionService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}