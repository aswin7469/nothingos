package nothing.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IDeviceStateService extends IInterface {
    public static final String DESCRIPTOR = "nothing.os.IDeviceStateService";

    boolean isCtsRunning() throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IDeviceStateService {
        @Override // nothing.os.IDeviceStateService
        public boolean isCtsRunning() throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements IDeviceStateService {
        static final int TRANSACTION_isCtsRunning = 1;

        public Stub() {
            attachInterface(this, IDeviceStateService.DESCRIPTOR);
        }

        public static IDeviceStateService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IDeviceStateService.DESCRIPTOR);
            if (iin != null && (iin instanceof IDeviceStateService)) {
                return (IDeviceStateService) iin;
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
                    return "isCtsRunning";
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
                    reply.writeString(IDeviceStateService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IDeviceStateService.DESCRIPTOR);
                            boolean isCtsRunning = isCtsRunning();
                            reply.writeNoException();
                            reply.writeInt(isCtsRunning ? 1 : 0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IDeviceStateService {
            public static IDeviceStateService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IDeviceStateService.DESCRIPTOR;
            }

            @Override // nothing.os.IDeviceStateService
            public boolean isCtsRunning() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IDeviceStateService.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCtsRunning();
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
        }

        public static boolean setDefaultImpl(IDeviceStateService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IDeviceStateService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
