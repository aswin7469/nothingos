package android.bluetooth;

import android.content.AttributionSource;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IBluetoothVcp extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IBluetoothVcp";

    int getAbsoluteVolume(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    int getActiveProfile(int i, AttributionSource attributionSource) throws RemoteException;

    int getConnectionMode(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    int getConnectionState(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    boolean isMute(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    void setAbsoluteVolume(BluetoothDevice bluetoothDevice, int i, AttributionSource attributionSource) throws RemoteException;

    boolean setActiveProfile(BluetoothDevice bluetoothDevice, int i, int i2, AttributionSource attributionSource) throws RemoteException;

    void setMute(BluetoothDevice bluetoothDevice, boolean z, AttributionSource attributionSource) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetoothVcp {
        @Override // android.bluetooth.IBluetoothVcp
        public int getConnectionState(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothVcp
        public int getConnectionMode(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothVcp
        public void setAbsoluteVolume(BluetoothDevice device, int volume, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothVcp
        public int getAbsoluteVolume(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothVcp
        public void setMute(BluetoothDevice device, boolean enableMute, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothVcp
        public boolean isMute(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothVcp
        public boolean setActiveProfile(BluetoothDevice device, int audioType, int profile, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothVcp
        public int getActiveProfile(int audioType, AttributionSource attributionSource) throws RemoteException {
            return 0;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothVcp {
        static final int TRANSACTION_getAbsoluteVolume = 4;
        static final int TRANSACTION_getActiveProfile = 8;
        static final int TRANSACTION_getConnectionMode = 2;
        static final int TRANSACTION_getConnectionState = 1;
        static final int TRANSACTION_isMute = 6;
        static final int TRANSACTION_setAbsoluteVolume = 3;
        static final int TRANSACTION_setActiveProfile = 7;
        static final int TRANSACTION_setMute = 5;

        public Stub() {
            attachInterface(this, IBluetoothVcp.DESCRIPTOR);
        }

        public static IBluetoothVcp asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IBluetoothVcp.DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothVcp)) {
                return (IBluetoothVcp) iin;
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
                    return "getConnectionState";
                case 2:
                    return "getConnectionMode";
                case 3:
                    return "setAbsoluteVolume";
                case 4:
                    return "getAbsoluteVolume";
                case 5:
                    return "setMute";
                case 6:
                    return "isMute";
                case 7:
                    return "setActiveProfile";
                case 8:
                    return "getActiveProfile";
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
            BluetoothDevice _arg0;
            AttributionSource _arg1;
            BluetoothDevice _arg02;
            AttributionSource _arg12;
            BluetoothDevice _arg03;
            AttributionSource _arg2;
            BluetoothDevice _arg04;
            AttributionSource _arg13;
            BluetoothDevice _arg05;
            AttributionSource _arg22;
            BluetoothDevice _arg06;
            AttributionSource _arg14;
            BluetoothDevice _arg07;
            AttributionSource _arg3;
            AttributionSource _arg15;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IBluetoothVcp.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IBluetoothVcp.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg1 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            int _result = getConnectionState(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeInt(_result);
                            return true;
                        case 2:
                            data.enforceInterface(IBluetoothVcp.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg12 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            int _result2 = getConnectionMode(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeInt(_result2);
                            return true;
                        case 3:
                            data.enforceInterface(IBluetoothVcp.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            int _arg16 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg2 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            setAbsoluteVolume(_arg03, _arg16, _arg2);
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(IBluetoothVcp.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg13 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg13 = null;
                            }
                            int _result3 = getAbsoluteVolume(_arg04, _arg13);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 5:
                            data.enforceInterface(IBluetoothVcp.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            boolean _arg17 = data.readInt() != 0;
                            if (data.readInt() != 0) {
                                _arg22 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg22 = null;
                            }
                            setMute(_arg05, _arg17, _arg22);
                            reply.writeNoException();
                            return true;
                        case 6:
                            data.enforceInterface(IBluetoothVcp.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg06 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg06 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg14 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg14 = null;
                            }
                            boolean isMute = isMute(_arg06, _arg14);
                            reply.writeNoException();
                            reply.writeInt(isMute ? 1 : 0);
                            return true;
                        case 7:
                            data.enforceInterface(IBluetoothVcp.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg07 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg07 = null;
                            }
                            int _arg18 = data.readInt();
                            int _arg23 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg3 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg3 = null;
                            }
                            boolean activeProfile = setActiveProfile(_arg07, _arg18, _arg23, _arg3);
                            reply.writeNoException();
                            reply.writeInt(activeProfile ? 1 : 0);
                            return true;
                        case 8:
                            data.enforceInterface(IBluetoothVcp.DESCRIPTOR);
                            int _arg08 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg15 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg15 = null;
                            }
                            int _result4 = getActiveProfile(_arg08, _arg15);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetoothVcp {
            public static IBluetoothVcp sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IBluetoothVcp.DESCRIPTOR;
            }

            @Override // android.bluetooth.IBluetoothVcp
            public int getConnectionState(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothVcp.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConnectionState(device, attributionSource);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothVcp
            public int getConnectionMode(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothVcp.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConnectionMode(device, attributionSource);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothVcp
            public void setAbsoluteVolume(BluetoothDevice device, int volume, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothVcp.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(volume);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setAbsoluteVolume(device, volume, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothVcp
            public int getAbsoluteVolume(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothVcp.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAbsoluteVolume(device, attributionSource);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothVcp
            public void setMute(BluetoothDevice device, boolean enableMute, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothVcp.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enableMute ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setMute(device, enableMute, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothVcp
            public boolean isMute(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothVcp.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isMute(device, attributionSource);
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

            @Override // android.bluetooth.IBluetoothVcp
            public boolean setActiveProfile(BluetoothDevice device, int audioType, int profile, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothVcp.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(audioType);
                    _data.writeInt(profile);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setActiveProfile(device, audioType, profile, attributionSource);
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

            @Override // android.bluetooth.IBluetoothVcp
            public int getActiveProfile(int audioType, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothVcp.DESCRIPTOR);
                    _data.writeInt(audioType);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getActiveProfile(audioType, attributionSource);
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

        public static boolean setDefaultImpl(IBluetoothVcp impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetoothVcp getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
