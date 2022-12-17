package com.nothing.p005os.device;

import android.bluetooth.BluetoothDevice;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.nothing.os.device.IDeviceService */
public interface IDeviceService extends IInterface {
    void getCommand(int i, Bundle bundle) throws RemoteException;

    void registerCallBack(IDeviceServiceCallBack iDeviceServiceCallBack) throws RemoteException;

    void sendCommand(int i, Bundle bundle) throws RemoteException;

    void setCommand(int i, Bundle bundle) throws RemoteException;

    void setModelIdAndDeviceConnected(String str, BluetoothDevice bluetoothDevice, boolean z) throws RemoteException;

    void unRegisterCallBack(IDeviceServiceCallBack iDeviceServiceCallBack) throws RemoteException;

    /* renamed from: com.nothing.os.device.IDeviceService$Stub */
    public static abstract class Stub extends Binder implements IDeviceService {
        public static IDeviceService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.nothing.os.device.IDeviceService");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IDeviceService)) {
                return new Proxy(iBinder);
            }
            return (IDeviceService) queryLocalInterface;
        }

        /* renamed from: com.nothing.os.device.IDeviceService$Stub$Proxy */
        private static class Proxy implements IDeviceService {
            public static IDeviceService sDefaultImpl;
            private IBinder mRemote;

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
                    obtain.writeInterfaceToken("com.nothing.os.device.IDeviceService");
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

            public void sendCommand(int i, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nothing.os.device.IDeviceService");
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
                    obtain.writeInterfaceToken("com.nothing.os.device.IDeviceService");
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
                    obtain.writeInterfaceToken("com.nothing.os.device.IDeviceService");
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
                    obtain.writeInterfaceToken("com.nothing.os.device.IDeviceService");
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
                    obtain.writeInterfaceToken("com.nothing.os.device.IDeviceService");
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
        }

        public static IDeviceService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
