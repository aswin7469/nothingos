package com.p025nt.facerecognition.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback */
public interface IFaceRecognitionServiceLockoutResetCallback extends IInterface {
    public static final String DESCRIPTOR = "com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback";

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback$Default */
    public static class Default implements IFaceRecognitionServiceLockoutResetCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onLockoutReset(long j, IRemoteCallback iRemoteCallback) throws RemoteException {
        }
    }

    void onLockoutReset(long j, IRemoteCallback iRemoteCallback) throws RemoteException;

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback$Stub */
    public static abstract class Stub extends Binder implements IFaceRecognitionServiceLockoutResetCallback {
        static final int TRANSACTION_onLockoutReset = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
        }

        public static IFaceRecognitionServiceLockoutResetCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFaceRecognitionServiceLockoutResetCallback)) {
                return new Proxy(iBinder);
            }
            return (IFaceRecognitionServiceLockoutResetCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                long readLong = parcel.readLong();
                IRemoteCallback asInterface = IRemoteCallback.Stub.asInterface(parcel.readStrongBinder());
                parcel.enforceNoDataAvail();
                onLockoutReset(readLong, asInterface);
                return true;
            }
        }

        /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback$Stub$Proxy */
        private static class Proxy implements IFaceRecognitionServiceLockoutResetCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onLockoutReset(long j, IRemoteCallback iRemoteCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceLockoutResetCallback.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeStrongInterface(iRemoteCallback);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
