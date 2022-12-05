package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IPackageManagerExt extends IInterface {
    public static final String DESCRIPTOR = "android.content.pm.IPackageManagerExt";

    boolean isForceFullForUid(int i) throws RemoteException;

    boolean setForceFull(String str, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IPackageManagerExt {
        @Override // android.content.pm.IPackageManagerExt
        public boolean setForceFull(String packageName, boolean forceFull) throws RemoteException {
            return false;
        }

        @Override // android.content.pm.IPackageManagerExt
        public boolean isForceFullForUid(int uid) throws RemoteException {
            return false;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IPackageManagerExt {
        static final int TRANSACTION_isForceFullForUid = 2;
        static final int TRANSACTION_setForceFull = 1;

        public Stub() {
            attachInterface(this, IPackageManagerExt.DESCRIPTOR);
        }

        public static IPackageManagerExt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IPackageManagerExt.DESCRIPTOR);
            if (iin != null && (iin instanceof IPackageManagerExt)) {
                return (IPackageManagerExt) iin;
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
                    return "setForceFull";
                case 2:
                    return "isForceFullForUid";
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
                    reply.writeString(IPackageManagerExt.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IPackageManagerExt.DESCRIPTOR);
                            String _arg0 = data.readString();
                            boolean _arg1 = data.readInt() != 0;
                            boolean forceFull = setForceFull(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeInt(forceFull ? 1 : 0);
                            return true;
                        case 2:
                            data.enforceInterface(IPackageManagerExt.DESCRIPTOR);
                            int _arg02 = data.readInt();
                            boolean isForceFullForUid = isForceFullForUid(_arg02);
                            reply.writeNoException();
                            reply.writeInt(isForceFullForUid ? 1 : 0);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IPackageManagerExt {
            public static IPackageManagerExt sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IPackageManagerExt.DESCRIPTOR;
            }

            @Override // android.content.pm.IPackageManagerExt
            public boolean setForceFull(String packageName, boolean forceFull) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPackageManagerExt.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(forceFull ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setForceFull(packageName, forceFull);
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

            @Override // android.content.pm.IPackageManagerExt
            public boolean isForceFullForUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IPackageManagerExt.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isForceFullForUid(uid);
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

        public static boolean setDefaultImpl(IPackageManagerExt impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IPackageManagerExt getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
