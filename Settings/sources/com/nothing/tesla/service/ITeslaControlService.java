package com.nothing.tesla.service;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes2.dex */
public interface ITeslaControlService extends IInterface {
    void registerCallback(ICallback iCallback) throws RemoteException;

    void sendCmd(int i, String str) throws RemoteException;

    void syncStatus() throws RemoteException;

    void unregisterCallback(ICallback iCallback) throws RemoteException;

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements ITeslaControlService {
        public static ITeslaControlService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.nothing.tesla.service.ITeslaControlService");
            if (queryLocalInterface != null && (queryLocalInterface instanceof ITeslaControlService)) {
                return (ITeslaControlService) queryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements ITeslaControlService {
            public static ITeslaControlService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            @Override // com.nothing.tesla.service.ITeslaControlService
            public void syncStatus() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nothing.tesla.service.ITeslaControlService");
                    if (!this.mRemote.transact(1, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().syncStatus();
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nothing.tesla.service.ITeslaControlService
            public void sendCmd(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nothing.tesla.service.ITeslaControlService");
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    if (!this.mRemote.transact(2, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sendCmd(i, str);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nothing.tesla.service.ITeslaControlService
            public void registerCallback(ICallback iCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nothing.tesla.service.ITeslaControlService");
                    obtain.writeStrongBinder(iCallback != null ? iCallback.asBinder() : null);
                    if (!this.mRemote.transact(4, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerCallback(iCallback);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            @Override // com.nothing.tesla.service.ITeslaControlService
            public void unregisterCallback(ICallback iCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nothing.tesla.service.ITeslaControlService");
                    obtain.writeStrongBinder(iCallback != null ? iCallback.asBinder() : null);
                    if (!this.mRemote.transact(5, obtain, obtain2, 0) && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterCallback(iCallback);
                    } else {
                        obtain2.readException();
                    }
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public static ITeslaControlService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
