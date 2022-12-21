package com.p025nt.facerecognition.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver */
public interface IFaceRecognitionServiceReceiver extends IInterface {
    public static final String DESCRIPTOR = "com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver";

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver$Default */
    public static class Default implements IFaceRecognitionServiceReceiver {
        public IBinder asBinder() {
            return null;
        }

        public void onAcquired(long j, int i, int i2) throws RemoteException {
        }

        public void onAuthenticationFaceDetected(long j, boolean z) throws RemoteException {
        }

        public void onAuthenticationFailed(long j, int i) throws RemoteException {
        }

        public void onAuthenticationSucceeded(long j, FaceMetadata faceMetadata, int i) throws RemoteException {
        }

        public void onAuthenticationTimeout(long j) throws RemoteException {
        }

        public void onEnrollResult(long j, int i, int i2, int i3) throws RemoteException {
        }

        public void onEnumerated(long j, int i, int i2, int i3) throws RemoteException {
        }

        public void onError(long j, int i, int i2) throws RemoteException {
        }

        public void onRemoved(long j, int i, int i2, int i3) throws RemoteException {
        }

        public void onScreenBrightnessOverrided(int i, float f) throws RemoteException {
        }

        public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) throws RemoteException {
        }
    }

    void onAcquired(long j, int i, int i2) throws RemoteException;

    void onAuthenticationFaceDetected(long j, boolean z) throws RemoteException;

    void onAuthenticationFailed(long j, int i) throws RemoteException;

    void onAuthenticationSucceeded(long j, FaceMetadata faceMetadata, int i) throws RemoteException;

    void onAuthenticationTimeout(long j) throws RemoteException;

    void onEnrollResult(long j, int i, int i2, int i3) throws RemoteException;

    void onEnumerated(long j, int i, int i2, int i3) throws RemoteException;

    void onError(long j, int i, int i2) throws RemoteException;

    void onRemoved(long j, int i, int i2, int i3) throws RemoteException;

    void onScreenBrightnessOverrided(int i, float f) throws RemoteException;

    void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) throws RemoteException;

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver$Stub */
    public static abstract class Stub extends Binder implements IFaceRecognitionServiceReceiver {
        static final int TRANSACTION_onAcquired = 3;
        static final int TRANSACTION_onAuthenticationFaceDetected = 7;
        static final int TRANSACTION_onAuthenticationFailed = 5;
        static final int TRANSACTION_onAuthenticationSucceeded = 4;
        static final int TRANSACTION_onAuthenticationTimeout = 6;
        static final int TRANSACTION_onEnrollResult = 2;
        static final int TRANSACTION_onEnumerated = 10;
        static final int TRANSACTION_onError = 8;
        static final int TRANSACTION_onRemoved = 9;
        static final int TRANSACTION_onScreenBrightnessOverrided = 11;
        static final int TRANSACTION_onWarmUpHardwareDeviceResult = 1;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IFaceRecognitionServiceReceiver.DESCRIPTOR);
        }

        public static IFaceRecognitionServiceReceiver asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFaceRecognitionServiceReceiver)) {
                return new Proxy(iBinder);
            }
            return (IFaceRecognitionServiceReceiver) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceNoDataAvail();
                        onWarmUpHardwareDeviceResult((CameraPreviewProperty) parcel.readTypedObject(CameraPreviewProperty.CREATOR));
                        break;
                    case 2:
                        long readLong = parcel.readLong();
                        int readInt = parcel.readInt();
                        int readInt2 = parcel.readInt();
                        int readInt3 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onEnrollResult(readLong, readInt, readInt2, readInt3);
                        break;
                    case 3:
                        long readLong2 = parcel.readLong();
                        int readInt4 = parcel.readInt();
                        int readInt5 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onAcquired(readLong2, readInt4, readInt5);
                        break;
                    case 4:
                        int readInt6 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onAuthenticationSucceeded(parcel.readLong(), (FaceMetadata) parcel.readTypedObject(FaceMetadata.CREATOR), readInt6);
                        break;
                    case 5:
                        long readLong3 = parcel.readLong();
                        int readInt7 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onAuthenticationFailed(readLong3, readInt7);
                        break;
                    case 6:
                        long readLong4 = parcel.readLong();
                        parcel.enforceNoDataAvail();
                        onAuthenticationTimeout(readLong4);
                        break;
                    case 7:
                        long readLong5 = parcel.readLong();
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        onAuthenticationFaceDetected(readLong5, readBoolean);
                        break;
                    case 8:
                        long readLong6 = parcel.readLong();
                        int readInt8 = parcel.readInt();
                        int readInt9 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onError(readLong6, readInt8, readInt9);
                        break;
                    case 9:
                        long readLong7 = parcel.readLong();
                        int readInt10 = parcel.readInt();
                        int readInt11 = parcel.readInt();
                        int readInt12 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onRemoved(readLong7, readInt10, readInt11, readInt12);
                        break;
                    case 10:
                        long readLong8 = parcel.readLong();
                        int readInt13 = parcel.readInt();
                        int readInt14 = parcel.readInt();
                        int readInt15 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        onEnumerated(readLong8, readInt13, readInt14, readInt15);
                        break;
                    case 11:
                        int readInt16 = parcel.readInt();
                        float readFloat = parcel.readFloat();
                        parcel.enforceNoDataAvail();
                        onScreenBrightnessOverrided(readInt16, readFloat);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(IFaceRecognitionServiceReceiver.DESCRIPTOR);
            return true;
        }

        /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver$Stub$Proxy */
        private static class Proxy implements IFaceRecognitionServiceReceiver {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFaceRecognitionServiceReceiver.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeTypedObject(cameraPreviewProperty, 0);
                    this.mRemote.transact(1, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onEnrollResult(long j, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(2, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAcquired(long j, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(3, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAuthenticationSucceeded(long j, FaceMetadata faceMetadata, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeTypedObject(faceMetadata, 0);
                    obtain.writeInt(i);
                    this.mRemote.transact(4, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAuthenticationFailed(long j, int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    this.mRemote.transact(5, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAuthenticationTimeout(long j) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    this.mRemote.transact(6, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onAuthenticationFaceDetected(long j, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(7, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onError(long j, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(8, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onRemoved(long j, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(9, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onEnumerated(long j, int i, int i2, int i3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    this.mRemote.transact(10, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public void onScreenBrightnessOverrided(int i, float f) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeFloat(f);
                    this.mRemote.transact(11, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
