package com.nothing.tesla.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ICallback extends IInterface {
    void callback(int i, int i2, String str) throws RemoteException;

    void dataSyncCallback(String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ICallback {
        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.nothing.tesla.service.ICallback");
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                parcel.enforceInterface("com.nothing.tesla.service.ICallback");
                callback(parcel.readInt(), parcel.readInt(), parcel.readString());
                parcel2.writeNoException();
                return true;
            } else if (i != 2) {
                if (i == 1598968902) {
                    parcel2.writeString("com.nothing.tesla.service.ICallback");
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceInterface("com.nothing.tesla.service.ICallback");
                dataSyncCallback(parcel.readString());
                parcel2.writeNoException();
                return true;
            }
        }
    }
}
