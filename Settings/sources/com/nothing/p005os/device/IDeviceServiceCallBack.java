package com.nothing.p005os.device;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.nothing.os.device.IDeviceServiceCallBack */
public interface IDeviceServiceCallBack extends IInterface {
    void onFail(int i, int i2) throws RemoteException;

    void onSuccess(int i, Bundle bundle) throws RemoteException;

    /* renamed from: com.nothing.os.device.IDeviceServiceCallBack$Stub */
    public static abstract class Stub extends Binder implements IDeviceServiceCallBack {
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.nothing.os.device.IDeviceServiceCallBack");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.nothing.os.device.IDeviceServiceCallBack");
                onSuccess(parcel.readInt(), parcel.readInt() != 0 ? (Bundle) Bundle.CREATOR.createFromParcel(parcel) : null);
                parcel2.writeNoException();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface("com.nothing.os.device.IDeviceServiceCallBack");
                onFail(parcel.readInt(), parcel.readInt());
                parcel2.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString("com.nothing.os.device.IDeviceServiceCallBack");
                return true;
            }
        }
    }
}
