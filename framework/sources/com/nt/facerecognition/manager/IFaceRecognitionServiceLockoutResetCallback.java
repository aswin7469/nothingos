package com.nt.facerecognition.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IFaceRecognitionServiceLockoutResetCallback extends IInterface {
    public static final String DESCRIPTOR = "com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback";

    void onLockoutReset(long j, IRemoteCallback iRemoteCallback) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IFaceRecognitionServiceLockoutResetCallback {
        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback
        public void onLockoutReset(long deviceId, IRemoteCallback callback) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IFaceRecognitionServiceLockoutResetCallback {
        static final int TRANSACTION_onLockoutReset = 1;

        public Stub() {
            attachInterface(this, IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
        }

        public static IFaceRecognitionServiceLockoutResetCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IFaceRecognitionServiceLockoutResetCallback)) {
                return (IFaceRecognitionServiceLockoutResetCallback) iin;
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
                    return "onLockoutReset";
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
                    reply.writeString(IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
                            long _arg0 = data.readLong();
                            IRemoteCallback _arg1 = IRemoteCallback.Stub.asInterface(data.readStrongBinder());
                            onLockoutReset(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IFaceRecognitionServiceLockoutResetCallback {
            public static IFaceRecognitionServiceLockoutResetCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR;
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback
            public void onLockoutReset(long deviceId, IRemoteCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onLockoutReset(deviceId, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFaceRecognitionServiceLockoutResetCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFaceRecognitionServiceLockoutResetCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
