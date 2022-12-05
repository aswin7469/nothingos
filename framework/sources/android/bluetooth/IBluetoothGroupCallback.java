package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes.dex */
public interface IBluetoothGroupCallback extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IBluetoothGroupCallback";

    void onConnectionStateChanged(int i, BluetoothDevice bluetoothDevice) throws RemoteException;

    void onExclusiveAccessAvailable(int i, BluetoothDevice bluetoothDevice) throws RemoteException;

    void onExclusiveAccessChanged(int i, int i2, int i3, List<BluetoothDevice> list) throws RemoteException;

    void onExclusiveAccessStatusFetched(int i, int i2) throws RemoteException;

    void onGroupClientAppRegistered(int i, int i2) throws RemoteException;

    void onGroupClientAppUnregistered(int i) throws RemoteException;

    void onGroupDeviceFound(int i, BluetoothDevice bluetoothDevice) throws RemoteException;

    void onGroupDiscoveryStatusChanged(int i, int i2, int i3) throws RemoteException;

    void onNewGroupFound(int i, BluetoothDevice bluetoothDevice, ParcelUuid parcelUuid) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetoothGroupCallback {
        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onConnectionStateChanged(int state, BluetoothDevice device) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onGroupClientAppRegistered(int status, int appId) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onGroupClientAppUnregistered(int status) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onNewGroupFound(int groupId, BluetoothDevice device, ParcelUuid uuid) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onGroupDiscoveryStatusChanged(int groupId, int status, int reason) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onGroupDeviceFound(int groupId, BluetoothDevice groupDevice) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onExclusiveAccessChanged(int groupId, int accessValue, int accessStatus, List<BluetoothDevice> devices) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onExclusiveAccessAvailable(int groupId, BluetoothDevice device) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothGroupCallback
        public void onExclusiveAccessStatusFetched(int groupId, int accessValue) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothGroupCallback {
        static final int TRANSACTION_onConnectionStateChanged = 1;
        static final int TRANSACTION_onExclusiveAccessAvailable = 8;
        static final int TRANSACTION_onExclusiveAccessChanged = 7;
        static final int TRANSACTION_onExclusiveAccessStatusFetched = 9;
        static final int TRANSACTION_onGroupClientAppRegistered = 2;
        static final int TRANSACTION_onGroupClientAppUnregistered = 3;
        static final int TRANSACTION_onGroupDeviceFound = 6;
        static final int TRANSACTION_onGroupDiscoveryStatusChanged = 5;
        static final int TRANSACTION_onNewGroupFound = 4;

        public Stub() {
            attachInterface(this, IBluetoothGroupCallback.DESCRIPTOR);
        }

        public static IBluetoothGroupCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IBluetoothGroupCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothGroupCallback)) {
                return (IBluetoothGroupCallback) iin;
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
                    return "onConnectionStateChanged";
                case 2:
                    return "onGroupClientAppRegistered";
                case 3:
                    return "onGroupClientAppUnregistered";
                case 4:
                    return "onNewGroupFound";
                case 5:
                    return "onGroupDiscoveryStatusChanged";
                case 6:
                    return "onGroupDeviceFound";
                case 7:
                    return "onExclusiveAccessChanged";
                case 8:
                    return "onExclusiveAccessAvailable";
                case 9:
                    return "onExclusiveAccessStatusFetched";
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
            BluetoothDevice _arg12;
            ParcelUuid _arg2;
            BluetoothDevice _arg13;
            BluetoothDevice _arg14;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IBluetoothGroupCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg0 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg1 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            onConnectionStateChanged(_arg0, _arg1);
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg02 = data.readInt();
                            int _arg15 = data.readInt();
                            onGroupClientAppRegistered(_arg02, _arg15);
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg03 = data.readInt();
                            onGroupClientAppUnregistered(_arg03);
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg04 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg12 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg12 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg2 = ParcelUuid.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            onNewGroupFound(_arg04, _arg12, _arg2);
                            reply.writeNoException();
                            return true;
                        case 5:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg05 = data.readInt();
                            int _arg16 = data.readInt();
                            int _arg22 = data.readInt();
                            onGroupDiscoveryStatusChanged(_arg05, _arg16, _arg22);
                            reply.writeNoException();
                            return true;
                        case 6:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg06 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg13 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg13 = null;
                            }
                            onGroupDeviceFound(_arg06, _arg13);
                            reply.writeNoException();
                            return true;
                        case 7:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg07 = data.readInt();
                            int _arg17 = data.readInt();
                            int _arg23 = data.readInt();
                            List<BluetoothDevice> _arg3 = data.createTypedArrayList(BluetoothDevice.CREATOR);
                            onExclusiveAccessChanged(_arg07, _arg17, _arg23, _arg3);
                            reply.writeNoException();
                            return true;
                        case 8:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg08 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg14 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg14 = null;
                            }
                            onExclusiveAccessAvailable(_arg08, _arg14);
                            reply.writeNoException();
                            return true;
                        case 9:
                            data.enforceInterface(IBluetoothGroupCallback.DESCRIPTOR);
                            int _arg09 = data.readInt();
                            int _arg18 = data.readInt();
                            onExclusiveAccessStatusFetched(_arg09, _arg18);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetoothGroupCallback {
            public static IBluetoothGroupCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IBluetoothGroupCallback.DESCRIPTOR;
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onConnectionStateChanged(int state, BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(state);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onConnectionStateChanged(state, device);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onGroupClientAppRegistered(int status, int appId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(appId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onGroupClientAppRegistered(status, appId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onGroupClientAppUnregistered(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onGroupClientAppUnregistered(status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onNewGroupFound(int groupId, BluetoothDevice device, ParcelUuid uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(groupId);
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
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onNewGroupFound(groupId, device, uuid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onGroupDiscoveryStatusChanged(int groupId, int status, int reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeInt(status);
                    _data.writeInt(reason);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onGroupDiscoveryStatusChanged(groupId, status, reason);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onGroupDeviceFound(int groupId, BluetoothDevice groupDevice) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(groupId);
                    if (groupDevice != null) {
                        _data.writeInt(1);
                        groupDevice.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onGroupDeviceFound(groupId, groupDevice);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onExclusiveAccessChanged(int groupId, int accessValue, int accessStatus, List<BluetoothDevice> devices) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeInt(accessValue);
                    _data.writeInt(accessStatus);
                    _data.writeTypedList(devices);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onExclusiveAccessChanged(groupId, accessValue, accessStatus, devices);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onExclusiveAccessAvailable(int groupId, BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(groupId);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onExclusiveAccessAvailable(groupId, device);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothGroupCallback
            public void onExclusiveAccessStatusFetched(int groupId, int accessValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothGroupCallback.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeInt(accessValue);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onExclusiveAccessStatusFetched(groupId, accessValue);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetoothGroupCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetoothGroupCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
