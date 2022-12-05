package android.hardware.fingerprint;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IFingerprintStateListener extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.fingerprint.IFingerprintStateListener";

    void onEnrollmentsChanged(int i, int i2, boolean z) throws RemoteException;

    void onStateChanged(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IFingerprintStateListener {
        @Override // android.hardware.fingerprint.IFingerprintStateListener
        public void onStateChanged(int newState) throws RemoteException {
        }

        @Override // android.hardware.fingerprint.IFingerprintStateListener
        public void onEnrollmentsChanged(int userId, int sensorId, boolean hasEnrollments) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IFingerprintStateListener {
        static final int TRANSACTION_onEnrollmentsChanged = 2;
        static final int TRANSACTION_onStateChanged = 1;

        public Stub() {
            attachInterface(this, IFingerprintStateListener.DESCRIPTOR);
        }

        public static IFingerprintStateListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IFingerprintStateListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IFingerprintStateListener)) {
                return (IFingerprintStateListener) iin;
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
                    return "onStateChanged";
                case 2:
                    return "onEnrollmentsChanged";
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
                    reply.writeString(IFingerprintStateListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IFingerprintStateListener.DESCRIPTOR);
                            int _arg0 = data.readInt();
                            onStateChanged(_arg0);
                            return true;
                        case 2:
                            data.enforceInterface(IFingerprintStateListener.DESCRIPTOR);
                            int _arg02 = data.readInt();
                            int _arg1 = data.readInt();
                            boolean _arg2 = data.readInt() != 0;
                            onEnrollmentsChanged(_arg02, _arg1, _arg2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IFingerprintStateListener {
            public static IFingerprintStateListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IFingerprintStateListener.DESCRIPTOR;
            }

            @Override // android.hardware.fingerprint.IFingerprintStateListener
            public void onStateChanged(int newState) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFingerprintStateListener.DESCRIPTOR);
                    _data.writeInt(newState);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStateChanged(newState);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // android.hardware.fingerprint.IFingerprintStateListener
            public void onEnrollmentsChanged(int userId, int sensorId, boolean hasEnrollments) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFingerprintStateListener.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(sensorId);
                    _data.writeInt(hasEnrollments ? 1 : 0);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onEnrollmentsChanged(userId, sensorId, hasEnrollments);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFingerprintStateListener impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFingerprintStateListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
