package android.hardware.usb;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface IUsbSerialReader extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.usb.IUsbSerialReader";

    String getSerial(String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IUsbSerialReader {
        @Override // android.hardware.usb.IUsbSerialReader
        public String getSerial(String packageName) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IUsbSerialReader {
        static final int TRANSACTION_getSerial = 1;

        public Stub() {
            attachInterface(this, IUsbSerialReader.DESCRIPTOR);
        }

        public static IUsbSerialReader asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IUsbSerialReader.DESCRIPTOR);
            if (iin != null && (iin instanceof IUsbSerialReader)) {
                return (IUsbSerialReader) iin;
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
                    return "getSerial";
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
                    reply.writeString(IUsbSerialReader.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IUsbSerialReader.DESCRIPTOR);
                            String _arg0 = data.readString();
                            String _result = getSerial(_arg0);
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IUsbSerialReader {
            public static IUsbSerialReader sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IUsbSerialReader.DESCRIPTOR;
            }

            @Override // android.hardware.usb.IUsbSerialReader
            public String getSerial(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IUsbSerialReader.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSerial(packageName);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IUsbSerialReader impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IUsbSerialReader getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
