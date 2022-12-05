package android.bluetooth;

import android.bluetooth.le.ScanResult;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes.dex */
public interface IBleBroadcastAudioScanAssistCallback extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IBleBroadcastAudioScanAssistCallback";

    void onBleBroadcastAudioSourceAdded(BluetoothDevice bluetoothDevice, byte b, int i) throws RemoteException;

    void onBleBroadcastAudioSourceRemoved(BluetoothDevice bluetoothDevice, byte b, int i) throws RemoteException;

    void onBleBroadcastAudioSourceSelected(BluetoothDevice bluetoothDevice, int i, List<BleBroadcastSourceChannel> list) throws RemoteException;

    void onBleBroadcastAudioSourceUpdated(BluetoothDevice bluetoothDevice, byte b, int i) throws RemoteException;

    void onBleBroadcastPinUpdated(BluetoothDevice bluetoothDevice, byte b, int i) throws RemoteException;

    void onBleBroadcastSourceFound(ScanResult scanResult) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBleBroadcastAudioScanAssistCallback {
        @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
        public void onBleBroadcastSourceFound(ScanResult scanres) throws RemoteException {
        }

        @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
        public void onBleBroadcastAudioSourceSelected(BluetoothDevice device, int status, List<BleBroadcastSourceChannel> broadcastSourceChannels) throws RemoteException {
        }

        @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
        public void onBleBroadcastAudioSourceAdded(BluetoothDevice rcvr, byte srcId, int status) throws RemoteException {
        }

        @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
        public void onBleBroadcastAudioSourceUpdated(BluetoothDevice rcvr, byte srcId, int status) throws RemoteException {
        }

        @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
        public void onBleBroadcastPinUpdated(BluetoothDevice rcvr, byte srcId, int status) throws RemoteException {
        }

        @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
        public void onBleBroadcastAudioSourceRemoved(BluetoothDevice rcvr, byte srcId, int status) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBleBroadcastAudioScanAssistCallback {
        static final int TRANSACTION_onBleBroadcastAudioSourceAdded = 3;
        static final int TRANSACTION_onBleBroadcastAudioSourceRemoved = 6;
        static final int TRANSACTION_onBleBroadcastAudioSourceSelected = 2;
        static final int TRANSACTION_onBleBroadcastAudioSourceUpdated = 4;
        static final int TRANSACTION_onBleBroadcastPinUpdated = 5;
        static final int TRANSACTION_onBleBroadcastSourceFound = 1;

        public Stub() {
            attachInterface(this, IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
        }

        public static IBleBroadcastAudioScanAssistCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
            if (iin != null && (iin instanceof IBleBroadcastAudioScanAssistCallback)) {
                return (IBleBroadcastAudioScanAssistCallback) iin;
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
                    return "onBleBroadcastSourceFound";
                case 2:
                    return "onBleBroadcastAudioSourceSelected";
                case 3:
                    return "onBleBroadcastAudioSourceAdded";
                case 4:
                    return "onBleBroadcastAudioSourceUpdated";
                case 5:
                    return "onBleBroadcastPinUpdated";
                case 6:
                    return "onBleBroadcastAudioSourceRemoved";
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
            ScanResult _arg0;
            BluetoothDevice _arg02;
            BluetoothDevice _arg03;
            BluetoothDevice _arg04;
            BluetoothDevice _arg05;
            BluetoothDevice _arg06;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = ScanResult.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            onBleBroadcastSourceFound(_arg0);
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            int _arg1 = data.readInt();
                            List<BleBroadcastSourceChannel> _arg2 = data.createTypedArrayList(BleBroadcastSourceChannel.CREATOR);
                            onBleBroadcastAudioSourceSelected(_arg02, _arg1, _arg2);
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            byte _arg12 = data.readByte();
                            int _arg22 = data.readInt();
                            onBleBroadcastAudioSourceAdded(_arg03, _arg12, _arg22);
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            byte _arg13 = data.readByte();
                            int _arg23 = data.readInt();
                            onBleBroadcastAudioSourceUpdated(_arg04, _arg13, _arg23);
                            reply.writeNoException();
                            return true;
                        case 5:
                            data.enforceInterface(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            byte _arg14 = data.readByte();
                            int _arg24 = data.readInt();
                            onBleBroadcastPinUpdated(_arg05, _arg14, _arg24);
                            reply.writeNoException();
                            return true;
                        case 6:
                            data.enforceInterface(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg06 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg06 = null;
                            }
                            byte _arg15 = data.readByte();
                            int _arg25 = data.readInt();
                            onBleBroadcastAudioSourceRemoved(_arg06, _arg15, _arg25);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBleBroadcastAudioScanAssistCallback {
            public static IBleBroadcastAudioScanAssistCallback sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IBleBroadcastAudioScanAssistCallback.DESCRIPTOR;
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastSourceFound(ScanResult scanres) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                    if (scanres != null) {
                        _data.writeInt(1);
                        scanres.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBleBroadcastSourceFound(scanres);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastAudioSourceSelected(BluetoothDevice device, int status, List<BleBroadcastSourceChannel> broadcastSourceChannels) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(status);
                    _data.writeTypedList(broadcastSourceChannels);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBleBroadcastAudioSourceSelected(device, status, broadcastSourceChannels);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastAudioSourceAdded(BluetoothDevice rcvr, byte srcId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                    if (rcvr != null) {
                        _data.writeInt(1);
                        rcvr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByte(srcId);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBleBroadcastAudioSourceAdded(rcvr, srcId, status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastAudioSourceUpdated(BluetoothDevice rcvr, byte srcId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                    if (rcvr != null) {
                        _data.writeInt(1);
                        rcvr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByte(srcId);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBleBroadcastAudioSourceUpdated(rcvr, srcId, status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastPinUpdated(BluetoothDevice rcvr, byte srcId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                    if (rcvr != null) {
                        _data.writeInt(1);
                        rcvr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByte(srcId);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBleBroadcastPinUpdated(rcvr, srcId, status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBleBroadcastAudioScanAssistCallback
            public void onBleBroadcastAudioSourceRemoved(BluetoothDevice rcvr, byte srcId, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBleBroadcastAudioScanAssistCallback.DESCRIPTOR);
                    if (rcvr != null) {
                        _data.writeInt(1);
                        rcvr.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByte(srcId);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onBleBroadcastAudioSourceRemoved(rcvr, srcId, status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBleBroadcastAudioScanAssistCallback impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBleBroadcastAudioScanAssistCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
