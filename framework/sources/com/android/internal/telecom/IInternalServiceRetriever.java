package com.android.internal.telecom;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telecom.IDeviceIdleControllerAdapter;
/* loaded from: classes4.dex */
public interface IInternalServiceRetriever extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telecom.IInternalServiceRetriever";

    IDeviceIdleControllerAdapter getDeviceIdleController() throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IInternalServiceRetriever {
        @Override // com.android.internal.telecom.IInternalServiceRetriever
        public IDeviceIdleControllerAdapter getDeviceIdleController() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IInternalServiceRetriever {
        static final int TRANSACTION_getDeviceIdleController = 1;

        public Stub() {
            attachInterface(this, IInternalServiceRetriever.DESCRIPTOR);
        }

        public static IInternalServiceRetriever asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IInternalServiceRetriever.DESCRIPTOR);
            if (iin != null && (iin instanceof IInternalServiceRetriever)) {
                return (IInternalServiceRetriever) iin;
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
                    return "getDeviceIdleController";
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
                    reply.writeString(IInternalServiceRetriever.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IInternalServiceRetriever.DESCRIPTOR);
                            IDeviceIdleControllerAdapter _result = getDeviceIdleController();
                            reply.writeNoException();
                            reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IInternalServiceRetriever {
            public static IInternalServiceRetriever sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IInternalServiceRetriever.DESCRIPTOR;
            }

            @Override // com.android.internal.telecom.IInternalServiceRetriever
            public IDeviceIdleControllerAdapter getDeviceIdleController() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IInternalServiceRetriever.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceIdleController();
                    }
                    _reply.readException();
                    IDeviceIdleControllerAdapter _result = IDeviceIdleControllerAdapter.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IInternalServiceRetriever impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IInternalServiceRetriever getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
