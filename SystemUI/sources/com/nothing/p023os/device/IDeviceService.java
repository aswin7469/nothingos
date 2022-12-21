package com.nothing.p023os.device;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.nothing.os.device.IDeviceService */
public interface IDeviceService extends IInterface {

    /* renamed from: com.nothing.os.device.IDeviceService$Default */
    public static class Default implements IDeviceService {
        public IBinder asBinder() {
            return null;
        }

        public void getCommand(int i, Bundle bundle) throws RemoteException {
        }

        public void registerCallBack(IDeviceServiceCallBack iDeviceServiceCallBack) throws RemoteException {
        }

        public void sendCommand(int i, Bundle bundle) throws RemoteException {
        }

        public void setCommand(int i, Bundle bundle) throws RemoteException {
        }

        public void setModelIdAndDevice(String str, BluetoothDevice bluetoothDevice) throws RemoteException {
        }

        public void setModelIdAndDeviceConnected(String str, BluetoothDevice bluetoothDevice, boolean z) throws RemoteException {
        }

        public Bundle syncCommand(int i, Bundle bundle) throws RemoteException {
            return null;
        }

        public void unRegisterCallBack(IDeviceServiceCallBack iDeviceServiceCallBack) throws RemoteException {
        }
    }

    void getCommand(int i, Bundle bundle) throws RemoteException;

    void registerCallBack(IDeviceServiceCallBack iDeviceServiceCallBack) throws RemoteException;

    void sendCommand(int i, Bundle bundle) throws RemoteException;

    void setCommand(int i, Bundle bundle) throws RemoteException;

    void setModelIdAndDevice(String str, BluetoothDevice bluetoothDevice) throws RemoteException;

    void setModelIdAndDeviceConnected(String str, BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    Bundle syncCommand(int i, Bundle bundle) throws RemoteException;

    void unRegisterCallBack(IDeviceServiceCallBack iDeviceServiceCallBack) throws RemoteException;

    /* renamed from: com.nothing.os.device.IDeviceService$Stub */
    public static abstract class Stub extends Binder implements IDeviceService {
        private static final String DESCRIPTOR = "com.nothing.os.device.IDeviceService";
        static final int TRANSACTION_getCommand = 5;
        static final int TRANSACTION_registerCallBack = 6;
        static final int TRANSACTION_sendCommand = 3;
        static final int TRANSACTION_setCommand = 4;
        static final int TRANSACTION_setModelIdAndDevice = 2;
        static final int TRANSACTION_setModelIdAndDeviceConnected = 1;
        static final int TRANSACTION_syncCommand = 8;
        static final int TRANSACTION_unRegisterCallBack = 7;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDeviceService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IDeviceService)) {
                return new Proxy(iBinder);
            }
            return (IDeviceService) queryLocalInterface;
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: android.bluetooth.BluetoothDevice} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: android.bluetooth.BluetoothDevice} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v10, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v13, resolved type: android.os.Bundle} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v16, resolved type: android.os.Bundle} */
        /* JADX WARNING: type inference failed for: r3v0 */
        /* JADX WARNING: type inference failed for: r3v19 */
        /* JADX WARNING: type inference failed for: r3v20 */
        /* JADX WARNING: type inference failed for: r3v21 */
        /* JADX WARNING: type inference failed for: r3v22 */
        /* JADX WARNING: type inference failed for: r3v23 */
        /* JADX WARNING: type inference failed for: r3v24 */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTransact(int r5, android.os.Parcel r6, android.os.Parcel r7, int r8) throws android.os.RemoteException {
            /*
                r4 = this;
                r0 = 1598968902(0x5f4e5446, float:1.4867585E19)
                r1 = 1
                java.lang.String r2 = "com.nothing.os.device.IDeviceService"
                if (r5 == r0) goto L_0x00f8
                r0 = 0
                r3 = 0
                switch(r5) {
                    case 1: goto L_0x00d4;
                    case 2: goto L_0x00b7;
                    case 3: goto L_0x009a;
                    case 4: goto L_0x007d;
                    case 5: goto L_0x0060;
                    case 6: goto L_0x004e;
                    case 7: goto L_0x003c;
                    case 8: goto L_0x0012;
                    default: goto L_0x000d;
                }
            L_0x000d:
                boolean r4 = super.onTransact(r5, r6, r7, r8)
                return r4
            L_0x0012:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0028
                android.os.Parcelable$Creator r8 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r3 = r6
                android.os.Bundle r3 = (android.os.Bundle) r3
            L_0x0028:
                android.os.Bundle r4 = r4.syncCommand(r5, r3)
                r7.writeNoException()
                if (r4 == 0) goto L_0x0038
                r7.writeInt(r1)
                r4.writeToParcel(r7, r1)
                goto L_0x003b
            L_0x0038:
                r7.writeInt(r0)
            L_0x003b:
                return r1
            L_0x003c:
                r6.enforceInterface(r2)
                android.os.IBinder r5 = r6.readStrongBinder()
                com.nothing.os.device.IDeviceServiceCallBack r5 = com.nothing.p023os.device.IDeviceServiceCallBack.Stub.asInterface(r5)
                r4.unRegisterCallBack(r5)
                r7.writeNoException()
                return r1
            L_0x004e:
                r6.enforceInterface(r2)
                android.os.IBinder r5 = r6.readStrongBinder()
                com.nothing.os.device.IDeviceServiceCallBack r5 = com.nothing.p023os.device.IDeviceServiceCallBack.Stub.asInterface(r5)
                r4.registerCallBack(r5)
                r7.writeNoException()
                return r1
            L_0x0060:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0076
                android.os.Parcelable$Creator r8 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r3 = r6
                android.os.Bundle r3 = (android.os.Bundle) r3
            L_0x0076:
                r4.getCommand(r5, r3)
                r7.writeNoException()
                return r1
            L_0x007d:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x0093
                android.os.Parcelable$Creator r8 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r3 = r6
                android.os.Bundle r3 = (android.os.Bundle) r3
            L_0x0093:
                r4.setCommand(r5, r3)
                r7.writeNoException()
                return r1
            L_0x009a:
                r6.enforceInterface(r2)
                int r5 = r6.readInt()
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x00b0
                android.os.Parcelable$Creator r8 = android.os.Bundle.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r3 = r6
                android.os.Bundle r3 = (android.os.Bundle) r3
            L_0x00b0:
                r4.sendCommand(r5, r3)
                r7.writeNoException()
                return r1
            L_0x00b7:
                r6.enforceInterface(r2)
                java.lang.String r5 = r6.readString()
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x00cd
                android.os.Parcelable$Creator r8 = android.bluetooth.BluetoothDevice.CREATOR
                java.lang.Object r6 = r8.createFromParcel(r6)
                r3 = r6
                android.bluetooth.BluetoothDevice r3 = (android.bluetooth.BluetoothDevice) r3
            L_0x00cd:
                r4.setModelIdAndDevice(r5, r3)
                r7.writeNoException()
                return r1
            L_0x00d4:
                r6.enforceInterface(r2)
                java.lang.String r5 = r6.readString()
                int r8 = r6.readInt()
                if (r8 == 0) goto L_0x00ea
                android.os.Parcelable$Creator r8 = android.bluetooth.BluetoothDevice.CREATOR
                java.lang.Object r8 = r8.createFromParcel(r6)
                r3 = r8
                android.bluetooth.BluetoothDevice r3 = (android.bluetooth.BluetoothDevice) r3
            L_0x00ea:
                int r6 = r6.readInt()
                if (r6 == 0) goto L_0x00f1
                r0 = r1
            L_0x00f1:
                r4.setModelIdAndDeviceConnected(r5, r3, r0)
                r7.writeNoException()
                return r1
            L_0x00f8:
                r7.writeString(r2)
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nothing.p023os.device.IDeviceService.Stub.onTransact(int, android.os.Parcel, android.os.Parcel, int):boolean");
        }

        /* renamed from: com.nothing.os.device.IDeviceService$Stub$Proxy */
        private static class Proxy implements IDeviceService {
            public static IDeviceService sDefaultImpl;
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void setModelIdAndDeviceConnected(String str, BluetoothDevice bluetoothDevice, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bluetoothDevice != null) {
                        obtain.writeInt(1);
                        bluetoothDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    obtain.writeInt(z ? 1 : 0);
                    if (this.mRemote.transact(1, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setModelIdAndDeviceConnected(str, bluetoothDevice, z);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setModelIdAndDevice(String str, BluetoothDevice bluetoothDevice) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeString(str);
                    if (bluetoothDevice != null) {
                        obtain.writeInt(1);
                        bluetoothDevice.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(2, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setModelIdAndDevice(str, bluetoothDevice);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void sendCommand(int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(3, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendCommand(i, bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setCommand(int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(4, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCommand(i, bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getCommand(int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (this.mRemote.transact(5, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getCommand(i, bundle);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerCallBack(IDeviceServiceCallBack iDeviceServiceCallBack) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iDeviceServiceCallBack != null ? iDeviceServiceCallBack.asBinder() : null);
                    if (this.mRemote.transact(6, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerCallBack(iDeviceServiceCallBack);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unRegisterCallBack(IDeviceServiceCallBack iDeviceServiceCallBack) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeStrongBinder(iDeviceServiceCallBack != null ? iDeviceServiceCallBack.asBinder() : null);
                    if (this.mRemote.transact(7, obtain, obtain2, 0) || Stub.getDefaultImpl() == null) {
                        obtain2.readException();
                        obtain2.recycle();
                        obtain.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unRegisterCallBack(iDeviceServiceCallBack);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public Bundle syncCommand(int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (bundle != null) {
                        obtain.writeInt(1);
                        bundle.writeToParcel(obtain, 0);
                    } else {
                        obtain.writeInt(0);
                    }
                    if (!this.mRemote.transact(8, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().syncCommand(i, bundle);
                    }
                    obtain2.readException();
                    Bundle bundle2 = obtain2.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(obtain2) : null;
                    obtain2.recycle();
                    obtain.recycle();
                    return bundle2;
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IDeviceService iDeviceService) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            } else if (iDeviceService == null) {
                return false;
            } else {
                Proxy.sDefaultImpl = iDeviceService;
                return true;
            }
        }

        public static IDeviceService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
