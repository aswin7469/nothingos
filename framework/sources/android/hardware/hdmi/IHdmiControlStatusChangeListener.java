package android.hardware.hdmi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IHdmiControlStatusChangeListener extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.hdmi.IHdmiControlStatusChangeListener";

    void onStatusChange(int i, boolean z) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IHdmiControlStatusChangeListener {
        @Override // android.hardware.hdmi.IHdmiControlStatusChangeListener
        public void onStatusChange(int isCecEnabled, boolean isCecAvailable) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IHdmiControlStatusChangeListener {
        static final int TRANSACTION_onStatusChange = 1;

        public Stub() {
            attachInterface(this, IHdmiControlStatusChangeListener.DESCRIPTOR);
        }

        public static IHdmiControlStatusChangeListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IHdmiControlStatusChangeListener.DESCRIPTOR);
            if (iin != null && (iin instanceof IHdmiControlStatusChangeListener)) {
                return (IHdmiControlStatusChangeListener) iin;
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
                    return "onStatusChange";
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
                    reply.writeString(IHdmiControlStatusChangeListener.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IHdmiControlStatusChangeListener.DESCRIPTOR);
                            int _arg0 = data.readInt();
                            boolean _arg1 = data.readInt() != 0;
                            onStatusChange(_arg0, _arg1);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IHdmiControlStatusChangeListener {
            public static IHdmiControlStatusChangeListener sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IHdmiControlStatusChangeListener.DESCRIPTOR;
            }

            @Override // android.hardware.hdmi.IHdmiControlStatusChangeListener
            public void onStatusChange(int isCecEnabled, boolean isCecAvailable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IHdmiControlStatusChangeListener.DESCRIPTOR);
                    _data.writeInt(isCecEnabled);
                    _data.writeInt(isCecAvailable ? 1 : 0);
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onStatusChange(isCecEnabled, isCecAvailable);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IHdmiControlStatusChangeListener impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IHdmiControlStatusChangeListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
