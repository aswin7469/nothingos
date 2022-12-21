package com.p025nt.facerecognition.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.nt.facerecognition.manager.IFaceRecognitionClientActiveCallback */
public interface IFaceRecognitionClientActiveCallback extends IInterface {
    public static final String DESCRIPTOR = "com.nt.facerecognition.manager.IFaceRecognitionClientActiveCallback";

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionClientActiveCallback$Default */
    public static class Default implements IFaceRecognitionClientActiveCallback {
        public IBinder asBinder() {
            return null;
        }

        public void onClientActiveChanged(boolean z) throws RemoteException {
        }
    }

    void onClientActiveChanged(boolean z) throws RemoteException;

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionClientActiveCallback$Stub */
    public static abstract class Stub extends Binder implements IFaceRecognitionClientActiveCallback {
        static final int TRANSACTION_onClientActiveChanged = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IFaceRecognitionClientActiveCallback.DESCRIPTOR);
        }

        public static IFaceRecognitionClientActiveCallback asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFaceRecognitionClientActiveCallback.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFaceRecognitionClientActiveCallback)) {
                return new Proxy(iBinder);
            }
            return (IFaceRecognitionClientActiveCallback) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFaceRecognitionClientActiveCallback.DESCRIPTOR);
            }
            if (i == 1598968902) {
                parcel2.writeString(IFaceRecognitionClientActiveCallback.DESCRIPTOR);
                return true;
            } else if (i != 1) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                boolean readBoolean = parcel.readBoolean();
                parcel.enforceNoDataAvail();
                onClientActiveChanged(readBoolean);
                return true;
            }
        }

        /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionClientActiveCallback$Stub$Proxy */
        private static class Proxy implements IFaceRecognitionClientActiveCallback {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFaceRecognitionClientActiveCallback.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onClientActiveChanged(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionClientActiveCallback.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
