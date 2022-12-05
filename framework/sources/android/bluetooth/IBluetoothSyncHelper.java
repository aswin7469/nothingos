package android.bluetooth;

import android.bluetooth.IBleBroadcastAudioScanAssistCallback;
import android.bluetooth.le.ScanResult;
import android.content.AttributionSource;
import android.media.MediaMetrics;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;
/* loaded from: classes.dex */
public interface IBluetoothSyncHelper extends IInterface {
    public static final String DESCRIPTOR = "android.bluetooth.IBluetoothSyncHelper";

    boolean addBroadcastSource(BluetoothDevice bluetoothDevice, BleBroadcastSourceInfo bleBroadcastSourceInfo, boolean z, AttributionSource attributionSource) throws RemoteException;

    boolean connect(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    boolean disconnect(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    List<BleBroadcastSourceInfo> getAllBroadcastSourceInformation(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    List<BluetoothDevice> getConnectedDevices(AttributionSource attributionSource) throws RemoteException;

    int getConnectionPolicy(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    int getConnectionState(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr, AttributionSource attributionSource) throws RemoteException;

    void registerAppCallback(BluetoothDevice bluetoothDevice, IBleBroadcastAudioScanAssistCallback iBleBroadcastAudioScanAssistCallback, AttributionSource attributionSource) throws RemoteException;

    boolean removeBroadcastSource(BluetoothDevice bluetoothDevice, byte b, boolean z, AttributionSource attributionSource) throws RemoteException;

    boolean searchforLeAudioBroadcasters(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    boolean selectBroadcastSource(BluetoothDevice bluetoothDevice, ScanResult scanResult, boolean z, AttributionSource attributionSource) throws RemoteException;

    boolean setBroadcastCode(BluetoothDevice bluetoothDevice, BleBroadcastSourceInfo bleBroadcastSourceInfo, boolean z, AttributionSource attributionSource) throws RemoteException;

    boolean setConnectionPolicy(BluetoothDevice bluetoothDevice, int i, AttributionSource attributionSource) throws RemoteException;

    boolean startScanOffload(BluetoothDevice bluetoothDevice, boolean z, AttributionSource attributionSource) throws RemoteException;

    boolean stopScanOffload(BluetoothDevice bluetoothDevice, boolean z, AttributionSource attributionSource) throws RemoteException;

    boolean stopSearchforLeAudioBroadcasters(BluetoothDevice bluetoothDevice, AttributionSource attributionSource) throws RemoteException;

    void unregisterAppCallback(BluetoothDevice bluetoothDevice, IBleBroadcastAudioScanAssistCallback iBleBroadcastAudioScanAssistCallback, AttributionSource attributionSource) throws RemoteException;

    boolean updateBroadcastSource(BluetoothDevice bluetoothDevice, BleBroadcastSourceInfo bleBroadcastSourceInfo, boolean z, AttributionSource attributionSource) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBluetoothSyncHelper {
        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean connect(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean disconnect(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public List<BluetoothDevice> getConnectedDevices(AttributionSource attributionSource) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states, AttributionSource attributionSource) throws RemoteException {
            return null;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public int getConnectionState(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean setConnectionPolicy(BluetoothDevice device, int connectionPolicy, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public int getConnectionPolicy(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return 0;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean startScanOffload(BluetoothDevice device, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean stopScanOffload(BluetoothDevice device, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public void registerAppCallback(BluetoothDevice device, IBleBroadcastAudioScanAssistCallback cb, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public void unregisterAppCallback(BluetoothDevice device, IBleBroadcastAudioScanAssistCallback cb, AttributionSource attributionSource) throws RemoteException {
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean searchforLeAudioBroadcasters(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean stopSearchforLeAudioBroadcasters(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean addBroadcastSource(BluetoothDevice device, BleBroadcastSourceInfo srcInfo, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean selectBroadcastSource(BluetoothDevice device, ScanResult scanRes, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean updateBroadcastSource(BluetoothDevice device, BleBroadcastSourceInfo srcInfo, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean setBroadcastCode(BluetoothDevice device, BleBroadcastSourceInfo srcInfo, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public boolean removeBroadcastSource(BluetoothDevice device, byte SourceId, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
            return false;
        }

        @Override // android.bluetooth.IBluetoothSyncHelper
        public List<BleBroadcastSourceInfo> getAllBroadcastSourceInformation(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBluetoothSyncHelper {
        static final int TRANSACTION_addBroadcastSource = 14;
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_disconnect = 2;
        static final int TRANSACTION_getAllBroadcastSourceInformation = 19;
        static final int TRANSACTION_getConnectedDevices = 3;
        static final int TRANSACTION_getConnectionPolicy = 7;
        static final int TRANSACTION_getConnectionState = 5;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 4;
        static final int TRANSACTION_registerAppCallback = 10;
        static final int TRANSACTION_removeBroadcastSource = 18;
        static final int TRANSACTION_searchforLeAudioBroadcasters = 12;
        static final int TRANSACTION_selectBroadcastSource = 15;
        static final int TRANSACTION_setBroadcastCode = 17;
        static final int TRANSACTION_setConnectionPolicy = 6;
        static final int TRANSACTION_startScanOffload = 8;
        static final int TRANSACTION_stopScanOffload = 9;
        static final int TRANSACTION_stopSearchforLeAudioBroadcasters = 13;
        static final int TRANSACTION_unregisterAppCallback = 11;
        static final int TRANSACTION_updateBroadcastSource = 16;

        public Stub() {
            attachInterface(this, IBluetoothSyncHelper.DESCRIPTOR);
        }

        public static IBluetoothSyncHelper asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IBluetoothSyncHelper.DESCRIPTOR);
            if (iin != null && (iin instanceof IBluetoothSyncHelper)) {
                return (IBluetoothSyncHelper) iin;
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
                    return "getConnectedDevices";
                case 4:
                    return "getDevicesMatchingConnectionStates";
                case 5:
                    return "getConnectionState";
                case 6:
                    return "setConnectionPolicy";
                case 7:
                    return "getConnectionPolicy";
                case 8:
                    return "startScanOffload";
                case 9:
                    return "stopScanOffload";
                case 10:
                    return "registerAppCallback";
                case 11:
                    return "unregisterAppCallback";
                case 12:
                    return "searchforLeAudioBroadcasters";
                case 13:
                    return "stopSearchforLeAudioBroadcasters";
                case 14:
                    return "addBroadcastSource";
                case 15:
                    return "selectBroadcastSource";
                case 16:
                    return "updateBroadcastSource";
                case 17:
                    return "setBroadcastCode";
                case 18:
                    return "removeBroadcastSource";
                case 19:
                    return "getAllBroadcastSourceInformation";
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
            AttributionSource _arg03;
            AttributionSource _arg13;
            BluetoothDevice _arg04;
            AttributionSource _arg14;
            BluetoothDevice _arg05;
            AttributionSource _arg2;
            BluetoothDevice _arg06;
            AttributionSource _arg15;
            BluetoothDevice _arg07;
            AttributionSource _arg22;
            BluetoothDevice _arg08;
            AttributionSource _arg23;
            BluetoothDevice _arg09;
            AttributionSource _arg24;
            BluetoothDevice _arg010;
            AttributionSource _arg25;
            BluetoothDevice _arg011;
            AttributionSource _arg16;
            BluetoothDevice _arg012;
            AttributionSource _arg17;
            BluetoothDevice _arg013;
            BleBroadcastSourceInfo _arg18;
            AttributionSource _arg3;
            BluetoothDevice _arg014;
            ScanResult _arg19;
            AttributionSource _arg32;
            BluetoothDevice _arg015;
            BleBroadcastSourceInfo _arg110;
            AttributionSource _arg33;
            BluetoothDevice _arg016;
            BleBroadcastSourceInfo _arg111;
            AttributionSource _arg34;
            BluetoothDevice _arg017;
            AttributionSource _arg35;
            BluetoothDevice _arg018;
            AttributionSource _arg112;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IBluetoothSyncHelper.DESCRIPTOR);
                    return true;
                default:
                    boolean _arg26 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
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
                            boolean connect = connect(_arg0, _arg1);
                            reply.writeNoException();
                            reply.writeInt(connect ? 1 : 0);
                            return true;
                        case 2:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
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
                            boolean disconnect = disconnect(_arg02, _arg12);
                            reply.writeNoException();
                            reply.writeInt(disconnect ? 1 : 0);
                            return true;
                        case 3:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            List<BluetoothDevice> _result = getConnectedDevices(_arg03);
                            reply.writeNoException();
                            reply.writeTypedList(_result);
                            return true;
                        case 4:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            int[] _arg019 = data.createIntArray();
                            if (data.readInt() != 0) {
                                _arg13 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg13 = null;
                            }
                            List<BluetoothDevice> _result2 = getDevicesMatchingConnectionStates(_arg019, _arg13);
                            reply.writeNoException();
                            reply.writeTypedList(_result2);
                            return true;
                        case 5:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg14 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg14 = null;
                            }
                            int _result3 = getConnectionState(_arg04, _arg14);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 6:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            int _arg113 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg2 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            boolean connectionPolicy = setConnectionPolicy(_arg05, _arg113, _arg2);
                            reply.writeNoException();
                            reply.writeInt(connectionPolicy ? 1 : 0);
                            return true;
                        case 7:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg06 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg06 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg15 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg15 = null;
                            }
                            int _result4 = getConnectionPolicy(_arg06, _arg15);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 8:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg07 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg07 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg26 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg22 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg22 = null;
                            }
                            boolean startScanOffload = startScanOffload(_arg07, _arg26, _arg22);
                            reply.writeNoException();
                            reply.writeInt(startScanOffload ? 1 : 0);
                            return true;
                        case 9:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg08 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg08 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg26 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg23 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg23 = null;
                            }
                            boolean stopScanOffload = stopScanOffload(_arg08, _arg26, _arg23);
                            reply.writeNoException();
                            reply.writeInt(stopScanOffload ? 1 : 0);
                            return true;
                        case 10:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg09 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg09 = null;
                            }
                            IBleBroadcastAudioScanAssistCallback _arg114 = IBleBroadcastAudioScanAssistCallback.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg24 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg24 = null;
                            }
                            registerAppCallback(_arg09, _arg114, _arg24);
                            reply.writeNoException();
                            return true;
                        case 11:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg010 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg010 = null;
                            }
                            IBleBroadcastAudioScanAssistCallback _arg115 = IBleBroadcastAudioScanAssistCallback.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg25 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg25 = null;
                            }
                            unregisterAppCallback(_arg010, _arg115, _arg25);
                            reply.writeNoException();
                            return true;
                        case 12:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg011 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg011 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg16 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg16 = null;
                            }
                            boolean searchforLeAudioBroadcasters = searchforLeAudioBroadcasters(_arg011, _arg16);
                            reply.writeNoException();
                            reply.writeInt(searchforLeAudioBroadcasters ? 1 : 0);
                            return true;
                        case 13:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg012 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg012 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg17 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg17 = null;
                            }
                            boolean stopSearchforLeAudioBroadcasters = stopSearchforLeAudioBroadcasters(_arg012, _arg17);
                            reply.writeNoException();
                            reply.writeInt(stopSearchforLeAudioBroadcasters ? 1 : 0);
                            return true;
                        case 14:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg013 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg013 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg18 = BleBroadcastSourceInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg18 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg26 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg3 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg3 = null;
                            }
                            boolean addBroadcastSource = addBroadcastSource(_arg013, _arg18, _arg26, _arg3);
                            reply.writeNoException();
                            reply.writeInt(addBroadcastSource ? 1 : 0);
                            return true;
                        case 15:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg014 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg014 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg19 = ScanResult.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg19 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg26 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg32 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg32 = null;
                            }
                            boolean selectBroadcastSource = selectBroadcastSource(_arg014, _arg19, _arg26, _arg32);
                            reply.writeNoException();
                            reply.writeInt(selectBroadcastSource ? 1 : 0);
                            return true;
                        case 16:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg015 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg015 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg110 = BleBroadcastSourceInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg110 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg26 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg33 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg33 = null;
                            }
                            boolean updateBroadcastSource = updateBroadcastSource(_arg015, _arg110, _arg26, _arg33);
                            reply.writeNoException();
                            reply.writeInt(updateBroadcastSource ? 1 : 0);
                            return true;
                        case 17:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg016 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg016 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg111 = BleBroadcastSourceInfo.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg111 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg26 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg34 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg34 = null;
                            }
                            boolean broadcastCode = setBroadcastCode(_arg016, _arg111, _arg26, _arg34);
                            reply.writeNoException();
                            reply.writeInt(broadcastCode ? 1 : 0);
                            return true;
                        case 18:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg017 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg017 = null;
                            }
                            byte _arg116 = data.readByte();
                            if (data.readInt() != 0) {
                                _arg26 = true;
                            }
                            if (data.readInt() != 0) {
                                _arg35 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg35 = null;
                            }
                            boolean removeBroadcastSource = removeBroadcastSource(_arg017, _arg116, _arg26, _arg35);
                            reply.writeNoException();
                            reply.writeInt(removeBroadcastSource ? 1 : 0);
                            return true;
                        case 19:
                            data.enforceInterface(IBluetoothSyncHelper.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg018 = BluetoothDevice.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg018 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg112 = AttributionSource.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg112 = null;
                            }
                            List<BleBroadcastSourceInfo> _result5 = getAllBroadcastSourceInformation(_arg018, _arg112);
                            reply.writeNoException();
                            reply.writeTypedList(_result5);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBluetoothSyncHelper {
            public static IBluetoothSyncHelper sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IBluetoothSyncHelper.DESCRIPTOR;
            }

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean connect(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().connect(device, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean disconnect(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().disconnect(device, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public List<BluetoothDevice> getConnectedDevices(AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConnectedDevices(attributionSource);
                    }
                    _reply.readException();
                    List<BluetoothDevice> _result = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothSyncHelper
            public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    _data.writeIntArray(states);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getDevicesMatchingConnectionStates(states, attributionSource);
                    }
                    _reply.readException();
                    List<BluetoothDevice> _result = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothSyncHelper
            public int getConnectionState(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean setConnectionPolicy(BluetoothDevice device, int connectionPolicy, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(connectionPolicy);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setConnectionPolicy(device, connectionPolicy, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public int getConnectionPolicy(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getConnectionPolicy(device, attributionSource);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean startScanOffload(BluetoothDevice device, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(groupOp ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().startScanOffload(device, groupOp, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean stopScanOffload(BluetoothDevice device, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(groupOp ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopScanOffload(device, groupOp, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public void registerAppCallback(BluetoothDevice device, IBleBroadcastAudioScanAssistCallback cb, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerAppCallback(device, cb, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothSyncHelper
            public void unregisterAppCallback(BluetoothDevice device, IBleBroadcastAudioScanAssistCallback cb, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterAppCallback(device, cb, attributionSource);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean searchforLeAudioBroadcasters(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().searchforLeAudioBroadcasters(device, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean stopSearchforLeAudioBroadcasters(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().stopSearchforLeAudioBroadcasters(device, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean addBroadcastSource(BluetoothDevice device, BleBroadcastSourceInfo srcInfo, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (srcInfo != null) {
                        _data.writeInt(1);
                        srcInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(groupOp ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().addBroadcastSource(device, srcInfo, groupOp, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean selectBroadcastSource(BluetoothDevice device, ScanResult scanRes, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (scanRes != null) {
                        _data.writeInt(1);
                        scanRes.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(groupOp ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().selectBroadcastSource(device, scanRes, groupOp, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean updateBroadcastSource(BluetoothDevice device, BleBroadcastSourceInfo srcInfo, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (srcInfo != null) {
                        _data.writeInt(1);
                        srcInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(groupOp ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().updateBroadcastSource(device, srcInfo, groupOp, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean setBroadcastCode(BluetoothDevice device, BleBroadcastSourceInfo srcInfo, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (srcInfo != null) {
                        _data.writeInt(1);
                        srcInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(groupOp ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setBroadcastCode(device, srcInfo, groupOp, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public boolean removeBroadcastSource(BluetoothDevice device, byte SourceId, boolean groupOp, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByte(SourceId);
                    _data.writeInt(groupOp ? 1 : 0);
                    if (attributionSource != null) {
                        _data.writeInt(1);
                        attributionSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().removeBroadcastSource(device, SourceId, groupOp, attributionSource);
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

            @Override // android.bluetooth.IBluetoothSyncHelper
            public List<BleBroadcastSourceInfo> getAllBroadcastSourceInformation(BluetoothDevice device, AttributionSource attributionSource) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBluetoothSyncHelper.DESCRIPTOR);
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
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAllBroadcastSourceInformation(device, attributionSource);
                    }
                    _reply.readException();
                    List<BleBroadcastSourceInfo> _result = _reply.createTypedArrayList(BleBroadcastSourceInfo.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBluetoothSyncHelper impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBluetoothSyncHelper getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
