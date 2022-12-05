package android.bluetooth;

import android.content.AttributionSource;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IBluetoothBroadcast extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IBluetoothBroadcast";

    int GetBroadcastStatus(String str, AttributionSource attributionSource) throws RemoteException;

    byte[] GetEncryptionKey(String str, AttributionSource attributionSource) throws RemoteException;

    boolean SetBroadcast(boolean z, String str, AttributionSource attributionSource) throws RemoteException;

    boolean SetEncryption(boolean z, int i, boolean z2, String str, AttributionSource attributionSource) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetoothBroadcast {
        @Override // android.bluetooth.IBluetoothBroadcast
        public boolean SetBroadcast(boolean enable, String packageName, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothBroadcast
        public boolean SetEncryption(boolean enable, int enc_len, boolean use_existing, String packageName, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothBroadcast
        public byte[] GetEncryptionKey(String packageName, AttributionSource attributionSource) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothBroadcast
        public int GetBroadcastStatus(String packageName, AttributionSource attributionSource) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothBroadcast {
        static final int TRANSACTION_GetBroadcastStatus = 4;
        static final int TRANSACTION_GetEncryptionKey = 3;
        static final int TRANSACTION_SetBroadcast = 1;
        static final int TRANSACTION_SetEncryption = 2;

        public Stub() {
            attachInterface(this, IBluetoothBroadcast.DESCRIPTOR);
        }

        public static IBluetoothBroadcast asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IBluetoothBroadcast.DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothBroadcast)) {
                return (IBluetoothBroadcast) iin;
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
                    return "SetBroadcast";
                case 2:
                    return "SetEncryption";
                case 3:
                    return "GetEncryptionKey";
                case 4:
                    return "GetBroadcastStatus";
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
            AttributionSource _arg2;
            boolean _arg0;
            boolean _arg22;
            AttributionSource _arg4;
            AttributionSource _arg1;
            AttributionSource _arg12;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IBluetoothBroadcast.DESCRIPTOR);
                    return true;
                default:
                    boolean _arg02 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(IBluetoothBroadcast.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = true;
                            }
                            String _arg13 = data.readString();
                            if (data.readInt() != 0) {
                                _arg2 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            boolean SetBroadcast = SetBroadcast(_arg02, _arg13, _arg2);
                            reply.writeNoException();
                            reply.writeInt(SetBroadcast ? 1 : 0);
                            return true;
                        case 2:
                            data.enforceInterface(IBluetoothBroadcast.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = true;
                            } else {
                                _arg0 = false;
                            }
                            int _arg14 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg22 = true;
                            } else {
                                _arg22 = false;
                            }
                            String _arg3 = data.readString();
                            if (data.readInt() != 0) {
                                _arg4 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg4 = null;
                            }
                            boolean SetEncryption = SetEncryption(_arg0, _arg14, _arg22, _arg3, _arg4);
                            reply.writeNoException();
                            reply.writeInt(SetEncryption ? 1 : 0);
                            return true;
                        case 3:
                            data.enforceInterface(IBluetoothBroadcast.DESCRIPTOR);
                            String _arg03 = data.readString();
                            if (data.readInt() != 0) {
                                _arg1 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            byte[] _result = GetEncryptionKey(_arg03, _arg1);
                            reply.writeNoException();
                            reply.writeByteArray(_result);
                            return true;
                        case 4:
                            data.enforceInterface(IBluetoothBroadcast.DESCRIPTOR);
                            String _arg04 = data.readString();
                            if (data.readInt() != 0) {
                                _arg12 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            int _result2 = GetBroadcastStatus(_arg04, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetoothBroadcast {
            public static IBluetoothBroadcast sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IBluetoothBroadcast.DESCRIPTOR;
            }

            @Override // android.bluetooth.IBluetoothBroadcast
            public boolean SetBroadcast(boolean enable, String packageName, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothBroadcast.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(packageName);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().SetBroadcast(enable, packageName, attributionSource);
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

            @Override // android.bluetooth.IBluetoothBroadcast
            public boolean SetEncryption(boolean enable, int enc_len, boolean use_existing, String packageName, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothBroadcast.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    try {
                        _data.writeInt(enc_len);
                        _data.writeInt(use_existing ? 1 : 0);
                        try {
                            _data.writeString(packageName);
                            if (attributionSource != null) {
                                _data.writeInt(1);
                                attributionSource.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    boolean SetEncryption = Stub.getDefaultImpl().SetEncryption(enable, enc_len, use_existing, packageName, attributionSource);
                                    _reply.recycle();
                                    _data.recycle();
                                    return SetEncryption;
                                }
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th) {
                                th = th;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                }
            }

            @Override // android.bluetooth.IBluetoothBroadcast
            public byte[] GetEncryptionKey(String packageName, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothBroadcast.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().GetEncryptionKey(packageName, attributionSource);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothBroadcast
            public int GetBroadcastStatus(String packageName, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothBroadcast.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().GetBroadcastStatus(packageName, attributionSource);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetoothBroadcast impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetoothBroadcast getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
