package com.p007nt.facerecognition.manager;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;
import java.util.List;

/* renamed from: com.nt.facerecognition.manager.IFaceRecognitionManagerService */
public interface IFaceRecognitionManagerService extends IInterface {
    void cancelEnrollment(IBinder iBinder) throws RemoteException;

    boolean closeHardwareDevice() throws RemoteException;

    void enroll(IBinder iBinder, byte[] bArr, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, int i2, String str, Rect rect) throws RemoteException;

    List<FaceMetadata> getEnrolledFaceMetadatas(int i, String str) throws RemoteException;

    String getReportString(int i) throws RemoteException;

    void remove(IBinder iBinder, int i, int i2, int i3, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException;

    void removeAllFaceMetadata(IBinder iBinder, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException;

    void rename(int i, int i2, String str) throws RemoteException;

    boolean warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, Surface surface, int i, int i2) throws RemoteException;

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionManagerService$Stub */
    public static abstract class Stub extends Binder implements IFaceRecognitionManagerService {
        public static IFaceRecognitionManagerService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFaceRecognitionManagerService)) {
                return new Proxy(iBinder);
            }
            return (IFaceRecognitionManagerService) queryLocalInterface;
        }

        /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionManagerService$Stub$Proxy */
        private static class Proxy implements IFaceRecognitionManagerService {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, Surface surface, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    obtain.writeStrongInterface(iFaceRecognitionServiceReceiver);
                    obtain.writeTypedObject(surface, 0);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enroll(IBinder iBinder, byte[] bArr, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, int i2, String str, Rect rect) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeByteArray(bArr);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iFaceRecognitionServiceReceiver);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    obtain.writeTypedObject(rect, 0);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void cancelEnrollment(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean closeHardwareDevice() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void remove(IBinder iBinder, int i, int i2, int i3, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeStrongInterface(iFaceRecognitionServiceReceiver);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void removeAllFaceMetadata(IBinder iBinder, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iFaceRecognitionServiceReceiver);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void rename(int i, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public List<FaceMetadata> getEnrolledFaceMetadatas(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.createTypedArrayList(FaceMetadata.CREATOR);
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getReportString(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.nt.facerecognition.manager.IFaceRecognitionManagerService");
                    obtain.writeInt(i);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
