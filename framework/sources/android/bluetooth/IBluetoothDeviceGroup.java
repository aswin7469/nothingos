package android.bluetooth;

import android.bluetooth.IBluetoothGroupCallback;
import android.content.AttributionSource;
import android.media.MediaMetrics;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes.dex */
public interface IBluetoothDeviceGroup extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IBluetoothDeviceGroup";

    void connect(int i, BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    void disconnect(int i, BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    DeviceGroup getDeviceGroup(int i, boolean z, AttributionSource attributionSource) throws RemoteException;

    List<DeviceGroup> getDiscoveredGroups(boolean z, AttributionSource attributionSource) throws RemoteException;

    void getExclusiveAccessStatus(int i, int i2, List<BluetoothDevice> list, AttributionSource attributionSource) throws RemoteException;

    int getRemoteDeviceGroupId(BluetoothDevice bluetoothDevice, ParcelUuid parcelUuid, boolean z, AttributionSource attributionSource) throws RemoteException;

    boolean isGroupDiscoveryInProgress(int i, AttributionSource attributionSource) throws RemoteException;

    void registerGroupClientApp(ParcelUuid parcelUuid, IBluetoothGroupCallback iBluetoothGroupCallback, AttributionSource attributionSource) throws RemoteException;

    void setExclusiveAccess(int i, int i2, List<BluetoothDevice> list, int i3, AttributionSource attributionSource) throws RemoteException;

    void startGroupDiscovery(int i, int i2, AttributionSource attributionSource) throws RemoteException;

    void stopGroupDiscovery(int i, int i2, AttributionSource attributionSource) throws RemoteException;

    void unregisterGroupClientApp(int i, AttributionSource attributionSource) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetoothDeviceGroup {
        @Override // android.bluetooth.IBluetoothDeviceGroup
        public void connect(int appId, BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public void disconnect(int appId, BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public void registerGroupClientApp(ParcelUuid uuid, IBluetoothGroupCallback callback, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public void unregisterGroupClientApp(int appId, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public void startGroupDiscovery(int appId, int groupId, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public void stopGroupDiscovery(int appId, int groupId, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public List<DeviceGroup> getDiscoveredGroups(boolean mPublicAddr, AttributionSource attributionSource) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public DeviceGroup getDeviceGroup(int groupId, boolean mPublicAddr, AttributionSource attributionSource) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public int getRemoteDeviceGroupId(BluetoothDevice device, ParcelUuid uuid, boolean mPublicAddr, AttributionSource attributionSource) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public boolean isGroupDiscoveryInProgress(int groupId, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public void setExclusiveAccess(int appId, int groupId, List<BluetoothDevice> devices, int value, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothDeviceGroup
        public void getExclusiveAccessStatus(int appId, int groupId, List<BluetoothDevice> devices, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothDeviceGroup {
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_disconnect = 2;
        static final int TRANSACTION_getDeviceGroup = 8;
        static final int TRANSACTION_getDiscoveredGroups = 7;
        static final int TRANSACTION_getExclusiveAccessStatus = 12;
        static final int TRANSACTION_getRemoteDeviceGroupId = 9;
        static final int TRANSACTION_isGroupDiscoveryInProgress = 10;
        static final int TRANSACTION_registerGroupClientApp = 3;
        static final int TRANSACTION_setExclusiveAccess = 11;
        static final int TRANSACTION_startGroupDiscovery = 5;
        static final int TRANSACTION_stopGroupDiscovery = 6;
        static final int TRANSACTION_unregisterGroupClientApp = 4;

        public Stub() {
            attachInterface(this, IBluetoothDeviceGroup.DESCRIPTOR);
        }

        public static IBluetoothDeviceGroup asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IBluetoothDeviceGroup.DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothDeviceGroup)) {
                return (IBluetoothDeviceGroup) iin;
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
                    return MediaMetrics.Value.CONNECT;
                case 2:
                    return MediaMetrics.Value.DISCONNECT;
                case 3:
                    return "registerGroupClientApp";
                case 4:
                    return "unregisterGroupClientApp";
                case 5:
                    return "startGroupDiscovery";
                case 6:
                    return "stopGroupDiscovery";
                case 7:
                    return "getDiscoveredGroups";
                case 8:
                    return "getDeviceGroup";
                case 9:
                    return "getRemoteDeviceGroupId";
                case 10:
                    return "isGroupDiscoveryInProgress";
                case 11:
                    return "setExclusiveAccess";
                case 12:
                    return "getExclusiveAccessStatus";
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
            BluetoothDevice _arg1;
            AttributionSource _arg2;
            BluetoothDevice _arg12;
            AttributionSource _arg22;
            ParcelUuid _arg0;
            AttributionSource _arg23;
            AttributionSource _arg13;
            AttributionSource _arg24;
            AttributionSource _arg25;
            AttributionSource _arg14;
            boolean _arg15;
            AttributionSource _arg26;
            BluetoothDevice _arg02;
            ParcelUuid _arg16;
            AttributionSource _arg3;
            AttributionSource _arg17;
            AttributionSource _arg4;
            AttributionSource _arg32;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IBluetoothDeviceGroup.DESCRIPTOR);
                    return true;
                default:
                    boolean _arg27 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg03 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg1 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg2 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            connect(_arg03, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg04 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg12 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg22 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg22 = null;
                            }
                            disconnect(_arg04, _arg12, _arg22);
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = ParcelUuid.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            IBluetoothGroupCallback _arg18 = IBluetoothGroupCallback.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg23 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg23 = null;
                            }
                            registerGroupClientApp(_arg0, _arg18, _arg23);
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg05 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg13 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg13 = null;
                            }
                            unregisterGroupClientApp(_arg05, _arg13);
                            reply.writeNoException();
                            return true;
                        case 5:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg06 = data.readInt();
                            int _arg19 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg24 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg24 = null;
                            }
                            startGroupDiscovery(_arg06, _arg19, _arg24);
                            reply.writeNoException();
                            return true;
                        case 6:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg07 = data.readInt();
                            int _arg110 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg25 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg25 = null;
                            }
                            stopGroupDiscovery(_arg07, _arg110, _arg25);
                            reply.writeNoException();
                            return true;
                        case 7:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg27 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg14 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg14 = null;
                            }
                            List<DeviceGroup> _result = getDiscoveredGroups(_arg27, _arg14);
                            reply.writeNoException();
                            reply.writeTypedList(_result);
                            return true;
                        case 8:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg08 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg15 = true;
                            } else {
                                _arg15 = false;
                            }
                            if (data.readInt() != 0) {
                                _arg26 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg26 = null;
                            }
                            DeviceGroup _result2 = getDeviceGroup(_arg08, _arg15, _arg26);
                            reply.writeNoException();
                            if (_result2 != null) {
                                reply.writeInt(1);
                                _result2.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 9:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg16 = ParcelUuid.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg16 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg27 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg3 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg3 = null;
                            }
                            int _result3 = getRemoteDeviceGroupId(_arg02, _arg16, _arg27, _arg3);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 10:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg09 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg17 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg17 = null;
                            }
                            boolean isGroupDiscoveryInProgress = isGroupDiscoveryInProgress(_arg09, _arg17);
                            reply.writeNoException();
                            reply.writeInt(isGroupDiscoveryInProgress ? 1 : 0);
                            return true;
                        case 11:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg010 = data.readInt();
                            int _arg111 = data.readInt();
                            List<BluetoothDevice> _arg28 = data.createTypedArrayList(BluetoothDevice.CREATOR);
                            int _arg33 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg4 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg4 = null;
                            }
                            setExclusiveAccess(_arg010, _arg111, _arg28, _arg33, _arg4);
                            reply.writeNoException();
                            return true;
                        case 12:
                            data.enforceInterface(IBluetoothDeviceGroup.DESCRIPTOR);
                            int _arg011 = data.readInt();
                            int _arg112 = data.readInt();
                            List<BluetoothDevice> _arg29 = data.createTypedArrayList(BluetoothDevice.CREATOR);
                            if (data.readInt() != 0) {
                                _arg32 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg32 = null;
                            }
                            getExclusiveAccessStatus(_arg011, _arg112, _arg29, _arg32);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetoothDeviceGroup {
            public static IBluetoothDeviceGroup sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IBluetoothDeviceGroup.DESCRIPTOR;
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public void connect(int appId, BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(appId);
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
                        Stub.getDefaultImpl().connect(appId, device, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public void disconnect(int appId, BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(appId);
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
                        Stub.getDefaultImpl().disconnect(appId, device, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public void registerGroupClientApp(ParcelUuid uuid, IBluetoothGroupCallback callback, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerGroupClientApp(uuid, callback, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public void unregisterGroupClientApp(int appId, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(appId);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterGroupClientApp(appId, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public void startGroupDiscovery(int appId, int groupId, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(groupId);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startGroupDiscovery(appId, groupId, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public void stopGroupDiscovery(int appId, int groupId, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(groupId);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().stopGroupDiscovery(appId, groupId, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public List<DeviceGroup> getDiscoveredGroups(boolean mPublicAddr, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(mPublicAddr ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDiscoveredGroups(mPublicAddr, attributionSource);
                    }
                    _reply.readException();
                    List<DeviceGroup> _result = _reply.createTypedArrayList(DeviceGroup.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public DeviceGroup getDeviceGroup(int groupId, boolean mPublicAddr, AttributionSource attributionSource) throws RemoteException {
                DeviceGroup _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeInt(mPublicAddr ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDeviceGroup(groupId, mPublicAddr, attributionSource);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = DeviceGroup.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public int getRemoteDeviceGroupId(BluetoothDevice device, ParcelUuid uuid, boolean mPublicAddr, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mPublicAddr ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRemoteDeviceGroupId(device, uuid, mPublicAddr, attributionSource);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public boolean isGroupDiscoveryInProgress(int groupId, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(groupId);
                    boolean _result = true;
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isGroupDiscoveryInProgress(groupId, attributionSource);
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

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public void setExclusiveAccess(int appId, int groupId, List<BluetoothDevice> devices, int value, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(groupId);
                    _data.writeTypedList(devices);
                    _data.writeInt(value);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setExclusiveAccess(appId, groupId, devices, value, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothDeviceGroup
            public void getExclusiveAccessStatus(int appId, int groupId, List<BluetoothDevice> devices, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothDeviceGroup.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(groupId);
                    _data.writeTypedList(devices);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().getExclusiveAccessStatus(appId, groupId, devices, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetoothDeviceGroup impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetoothDeviceGroup getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
