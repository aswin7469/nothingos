package android.telephony.ims.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.telephony.ims.RcsContactUceCapability;
import java.util.List;
/* loaded from: classes3.dex */
public interface IRcsUceControllerCallback extends IInterface {
    public static final String DESCRIPTOR = "android.telephony.ims.aidl.IRcsUceControllerCallback";

    void onCapabilitiesReceived(List<RcsContactUceCapability> list) throws RemoteException;

    void onComplete() throws RemoteException;

    void onError(int i, long j) throws RemoteException;

    /* loaded from: classes3.dex */
    public static class Default implements IRcsUceControllerCallback {
        @Override // android.telephony.ims.aidl.IRcsUceControllerCallback
        public void onCapabilitiesReceived(List<RcsContactUceCapability> contactCapabilities) throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IRcsUceControllerCallback
        public void onComplete() throws RemoteException {
        }

        @Override // android.telephony.ims.aidl.IRcsUceControllerCallback
        public void onError(int errorCode, long retryAfterMilliseconds) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes3.dex */
    public static abstract class Stub extends Binder implements IRcsUceControllerCallback {
        static final int TRANSACTION_onCapabilitiesReceived = 1;
        static final int TRANSACTION_onComplete = 2;
        static final int TRANSACTION_onError = 3;

        public Stub() {
            attachInterface(this, IRcsUceControllerCallback.DESCRIPTOR);
        }

        public static IRcsUceControllerCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IRcsUceControllerCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IRcsUceControllerCallback)) {
                return (IRcsUceControllerCallback) iin;
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
                    return "onCapabilitiesReceived";
                case 2:
                    return "onComplete";
                case 3:
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
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IRcsUceControllerCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IRcsUceControllerCallback.DESCRIPTOR);
                            List<RcsContactUceCapability> _arg0 = data.createTypedArrayList(RcsContactUceCapability.CREATOR);
                            onCapabilitiesReceived(_arg0);
                            return true;
                        case 2:
                            data.enforceInterface(IRcsUceControllerCallback.DESCRIPTOR);
                            onComplete();
                            return true;
                        case 3:
                            data.enforceInterface(IRcsUceControllerCallback.DESCRIPTOR);
                            int _arg02 = data.readInt();
                            long _arg1 = data.readLong();
                            onError(_arg02, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes3.dex */
        public static class Proxy implements IRcsUceControllerCallback {
            public static IRcsUceControllerCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IRcsUceControllerCallback.DESCRIPTOR;
            }

            @Override // android.telephony.ims.aidl.IRcsUceControllerCallback
            public void onCapabilitiesReceived(List<RcsContactUceCapability> contactCapabilities) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRcsUceControllerCallback.DESCRIPTOR);
                    _data.writeTypedList(contactCapabilities);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onCapabilitiesReceived(contactCapabilities);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IRcsUceControllerCallback
            public void onComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRcsUceControllerCallback.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onComplete();
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.telephony.ims.aidl.IRcsUceControllerCallback
            public void onError(int errorCode, long retryAfterMilliseconds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IRcsUceControllerCallback.DESCRIPTOR);
                    _data.writeInt(errorCode);
                    _data.writeLong(retryAfterMilliseconds);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onError(errorCode, retryAfterMilliseconds);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IRcsUceControllerCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IRcsUceControllerCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
