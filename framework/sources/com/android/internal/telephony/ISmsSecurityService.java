package com.android.internal.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.internal.telephony.ISmsSecurityAgent;
/* loaded from: classes4.dex */
public interface ISmsSecurityService extends IInterface {
    public static final String DESCRIPTOR = "com.android.internal.telephony.ISmsSecurityService";

    boolean register(ISmsSecurityAgent iSmsSecurityAgent) throws RemoteException;

    boolean sendResponse(SmsAuthorizationRequest smsAuthorizationRequest, boolean z) throws RemoteException;

    boolean unregister(ISmsSecurityAgent iSmsSecurityAgent) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements ISmsSecurityService {
        @Override // com.android.internal.telephony.ISmsSecurityService
        public boolean register(ISmsSecurityAgent agent) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ISmsSecurityService
        public boolean unregister(ISmsSecurityAgent agent) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.telephony.ISmsSecurityService
        public boolean sendResponse(SmsAuthorizationRequest request, boolean authorized) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements ISmsSecurityService {
        static final int TRANSACTION_register = 1;
        static final int TRANSACTION_sendResponse = 3;
        static final int TRANSACTION_unregister = 2;

        public Stub() {
            attachInterface(this, ISmsSecurityService.DESCRIPTOR);
        }

        public static ISmsSecurityService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(ISmsSecurityService.DESCRIPTOR);
            if (iin != null && (iin instanceof ISmsSecurityService)) {
                return (ISmsSecurityService) iin;
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
                    return "register";
                case 2:
                    return "unregister";
                case 3:
                    return "sendResponse";
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
            SmsAuthorizationRequest _arg0;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(ISmsSecurityService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(ISmsSecurityService.DESCRIPTOR);
                            ISmsSecurityAgent _arg02 = ISmsSecurityAgent.Stub.asInterface(data.readStrongBinder());
                            boolean register = register(_arg02);
                            reply.writeNoException();
                            reply.writeInt(register ? 1 : 0);
                            return true;
                        case 2:
                            data.enforceInterface(ISmsSecurityService.DESCRIPTOR);
                            ISmsSecurityAgent _arg03 = ISmsSecurityAgent.Stub.asInterface(data.readStrongBinder());
                            boolean unregister = unregister(_arg03);
                            reply.writeNoException();
                            reply.writeInt(unregister ? 1 : 0);
                            return true;
                        case 3:
                            data.enforceInterface(ISmsSecurityService.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = SmsAuthorizationRequest.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            boolean _arg1 = data.readInt() != 0;
                            boolean sendResponse = sendResponse(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeInt(sendResponse ? 1 : 0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements ISmsSecurityService {
            public static ISmsSecurityService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return ISmsSecurityService.DESCRIPTOR;
            }

            @Override // com.android.internal.telephony.ISmsSecurityService
            public boolean register(ISmsSecurityAgent agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmsSecurityService.DESCRIPTOR);
                    _data.writeStrongBinder(agent != null ? agent.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().register(agent);
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

            @Override // com.android.internal.telephony.ISmsSecurityService
            public boolean unregister(ISmsSecurityAgent agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmsSecurityService.DESCRIPTOR);
                    _data.writeStrongBinder(agent != null ? agent.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().unregister(agent);
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

            @Override // com.android.internal.telephony.ISmsSecurityService
            public boolean sendResponse(SmsAuthorizationRequest request, boolean authorized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(ISmsSecurityService.DESCRIPTOR);
                    boolean _result = true;
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(authorized ? 1 : 0);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().sendResponse(request, authorized);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ISmsSecurityService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ISmsSecurityService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
