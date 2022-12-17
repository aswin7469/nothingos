package com.p007nt.facerecognition.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* renamed from: com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver */
public interface IFaceRecognitionServiceReceiver extends IInterface {
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
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver");
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver");
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
            parcel2.writeString("com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver");
            return true;
        }
    }
}
