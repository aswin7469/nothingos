package com.p025nt.facerecognition.manager;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;
import com.p025nt.facerecognition.manager.IFaceRecognitionClientActiveCallback;
import com.p025nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback;
import com.p025nt.facerecognition.manager.IFaceRecognitionServiceReceiver;
import java.util.List;

/* renamed from: com.nt.facerecognition.manager.IFaceRecognitionManagerService */
public interface IFaceRecognitionManagerService extends IInterface {
    public static final String DESCRIPTOR = "com.nt.facerecognition.manager.IFaceRecognitionManagerService";

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionManagerService$Default */
    public static class Default implements IFaceRecognitionManagerService {
        public void addClientActiveCallback(IFaceRecognitionClientActiveCallback iFaceRecognitionClientActiveCallback) throws RemoteException {
        }

        public void addLockoutResetCallback(IFaceRecognitionServiceLockoutResetCallback iFaceRecognitionServiceLockoutResetCallback) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }

        public void authenticate(IBinder iBinder, long j, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, int i2, String str) throws RemoteException {
        }

        public void cancelAuthentication(IBinder iBinder, String str) throws RemoteException {
        }

        public void cancelEnrollment(IBinder iBinder) throws RemoteException {
        }

        public boolean closeHardwareDevice() throws RemoteException {
            return false;
        }

        public void enroll(IBinder iBinder, byte[] bArr, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, int i2, String str, Rect rect) throws RemoteException {
        }

        public void enumerate(IBinder iBinder, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException {
        }

        public long getAuthenticatorId(String str) throws RemoteException {
            return 0;
        }

        public List<FaceMetadata> getEnrolledFaceMetadatas(int i, String str) throws RemoteException {
            return null;
        }

        public String getReportString(int i) throws RemoteException {
            return null;
        }

        public boolean hasEnrolledFaceMetadatas(int i, String str) throws RemoteException {
            return false;
        }

        public boolean isClientActive() throws RemoteException {
            return false;
        }

        public boolean isHardwareDetected(long j, String str) throws RemoteException {
            return false;
        }

        public int postEnroll(IBinder iBinder) throws RemoteException {
            return 0;
        }

        public long preEnroll(IBinder iBinder) throws RemoteException {
            return 0;
        }

        public void remove(IBinder iBinder, int i, int i2, int i3, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException {
        }

        public void removeAllFaceMetadata(IBinder iBinder, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException {
        }

        public void rename(int i, int i2, String str) throws RemoteException {
        }

        public void resetTimeout(byte[] bArr) throws RemoteException {
        }

        public void setActiveUser(int i) throws RemoteException {
        }

        public boolean warmUpHardwareDevice(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException {
            return false;
        }

        public boolean warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, Surface surface, int i, int i2) throws RemoteException {
            return false;
        }

        public void warmUpOrStopHardwareDeviceByDisplayState(boolean z) throws RemoteException {
        }
    }

    void addClientActiveCallback(IFaceRecognitionClientActiveCallback iFaceRecognitionClientActiveCallback) throws RemoteException;

    void addLockoutResetCallback(IFaceRecognitionServiceLockoutResetCallback iFaceRecognitionServiceLockoutResetCallback) throws RemoteException;

    void authenticate(IBinder iBinder, long j, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, int i2, String str) throws RemoteException;

    void cancelAuthentication(IBinder iBinder, String str) throws RemoteException;

    void cancelEnrollment(IBinder iBinder) throws RemoteException;

    boolean closeHardwareDevice() throws RemoteException;

    void enroll(IBinder iBinder, byte[] bArr, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, int i2, String str, Rect rect) throws RemoteException;

    void enumerate(IBinder iBinder, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException;

    long getAuthenticatorId(String str) throws RemoteException;

    List<FaceMetadata> getEnrolledFaceMetadatas(int i, String str) throws RemoteException;

    String getReportString(int i) throws RemoteException;

    boolean hasEnrolledFaceMetadatas(int i, String str) throws RemoteException;

    boolean isClientActive() throws RemoteException;

    boolean isHardwareDetected(long j, String str) throws RemoteException;

    int postEnroll(IBinder iBinder) throws RemoteException;

    long preEnroll(IBinder iBinder) throws RemoteException;

    void remove(IBinder iBinder, int i, int i2, int i3, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException;

    void removeAllFaceMetadata(IBinder iBinder, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException;

    void rename(int i, int i2, String str) throws RemoteException;

    void resetTimeout(byte[] bArr) throws RemoteException;

    void setActiveUser(int i) throws RemoteException;

    boolean warmUpHardwareDevice(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException;

    boolean warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, Surface surface, int i, int i2) throws RemoteException;

    void warmUpOrStopHardwareDeviceByDisplayState(boolean z) throws RemoteException;

    /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionManagerService$Stub */
    public static abstract class Stub extends Binder implements IFaceRecognitionManagerService {
        static final int TRANSACTION_addClientActiveCallback = 22;
        static final int TRANSACTION_addLockoutResetCallback = 18;
        static final int TRANSACTION_authenticate = 2;
        static final int TRANSACTION_cancelAuthentication = 3;
        static final int TRANSACTION_cancelEnrollment = 6;
        static final int TRANSACTION_closeHardwareDevice = 7;
        static final int TRANSACTION_enroll = 5;
        static final int TRANSACTION_enumerate = 20;
        static final int TRANSACTION_getAuthenticatorId = 16;
        static final int TRANSACTION_getEnrolledFaceMetadatas = 11;
        static final int TRANSACTION_getReportString = 23;
        static final int TRANSACTION_hasEnrolledFaceMetadatas = 15;
        static final int TRANSACTION_isClientActive = 21;
        static final int TRANSACTION_isHardwareDetected = 12;
        static final int TRANSACTION_postEnroll = 14;
        static final int TRANSACTION_preEnroll = 13;
        static final int TRANSACTION_remove = 8;
        static final int TRANSACTION_removeAllFaceMetadata = 9;
        static final int TRANSACTION_rename = 10;
        static final int TRANSACTION_resetTimeout = 17;
        static final int TRANSACTION_setActiveUser = 19;
        static final int TRANSACTION_warmUpHardwareDevice = 1;
        static final int TRANSACTION_warmUpHardwareDeviceForPreview = 4;
        static final int TRANSACTION_warmUpOrStopHardwareDeviceByDisplayState = 24;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, IFaceRecognitionManagerService.DESCRIPTOR);
        }

        public static IFaceRecognitionManagerService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(IFaceRecognitionManagerService.DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IFaceRecognitionManagerService)) {
                return new Proxy(iBinder);
            }
            return (IFaceRecognitionManagerService) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
            }
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        IFaceRecognitionServiceReceiver asInterface = IFaceRecognitionServiceReceiver.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        boolean warmUpHardwareDevice = warmUpHardwareDevice(asInterface);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(warmUpHardwareDevice);
                        break;
                    case 2:
                        IBinder readStrongBinder = parcel.readStrongBinder();
                        long readLong = parcel.readLong();
                        int readInt = parcel.readInt();
                        IFaceRecognitionServiceReceiver asInterface2 = IFaceRecognitionServiceReceiver.Stub.asInterface(parcel.readStrongBinder());
                        int readInt2 = parcel.readInt();
                        String readString = parcel.readString();
                        parcel.enforceNoDataAvail();
                        authenticate(readStrongBinder, readLong, readInt, asInterface2, readInt2, readString);
                        parcel2.writeNoException();
                        break;
                    case 3:
                        IBinder readStrongBinder2 = parcel.readStrongBinder();
                        String readString2 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        cancelAuthentication(readStrongBinder2, readString2);
                        parcel2.writeNoException();
                        break;
                    case 4:
                        int readInt3 = parcel.readInt();
                        int readInt4 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        boolean warmUpHardwareDeviceForPreview = warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver.Stub.asInterface(parcel.readStrongBinder()), (Surface) parcel.readTypedObject(Surface.CREATOR), readInt3, readInt4);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(warmUpHardwareDeviceForPreview);
                        break;
                    case 5:
                        parcel.enforceNoDataAvail();
                        enroll(parcel.readStrongBinder(), parcel.createByteArray(), parcel.readInt(), IFaceRecognitionServiceReceiver.Stub.asInterface(parcel.readStrongBinder()), parcel.readInt(), parcel.readString(), (Rect) parcel.readTypedObject(Rect.CREATOR));
                        parcel2.writeNoException();
                        break;
                    case 6:
                        IBinder readStrongBinder3 = parcel.readStrongBinder();
                        parcel.enforceNoDataAvail();
                        cancelEnrollment(readStrongBinder3);
                        parcel2.writeNoException();
                        break;
                    case 7:
                        boolean closeHardwareDevice = closeHardwareDevice();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(closeHardwareDevice);
                        break;
                    case 8:
                        IBinder readStrongBinder4 = parcel.readStrongBinder();
                        int readInt5 = parcel.readInt();
                        int readInt6 = parcel.readInt();
                        int readInt7 = parcel.readInt();
                        IFaceRecognitionServiceReceiver asInterface3 = IFaceRecognitionServiceReceiver.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        remove(readStrongBinder4, readInt5, readInt6, readInt7, asInterface3);
                        parcel2.writeNoException();
                        break;
                    case 9:
                        IBinder readStrongBinder5 = parcel.readStrongBinder();
                        int readInt8 = parcel.readInt();
                        IFaceRecognitionServiceReceiver asInterface4 = IFaceRecognitionServiceReceiver.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        removeAllFaceMetadata(readStrongBinder5, readInt8, asInterface4);
                        parcel2.writeNoException();
                        break;
                    case 10:
                        int readInt9 = parcel.readInt();
                        int readInt10 = parcel.readInt();
                        String readString3 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        rename(readInt9, readInt10, readString3);
                        parcel2.writeNoException();
                        break;
                    case 11:
                        int readInt11 = parcel.readInt();
                        String readString4 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        List<FaceMetadata> enrolledFaceMetadatas = getEnrolledFaceMetadatas(readInt11, readString4);
                        parcel2.writeNoException();
                        parcel2.writeTypedList(enrolledFaceMetadatas);
                        break;
                    case 12:
                        long readLong2 = parcel.readLong();
                        String readString5 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        boolean isHardwareDetected = isHardwareDetected(readLong2, readString5);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isHardwareDetected);
                        break;
                    case 13:
                        IBinder readStrongBinder6 = parcel.readStrongBinder();
                        parcel.enforceNoDataAvail();
                        long preEnroll = preEnroll(readStrongBinder6);
                        parcel2.writeNoException();
                        parcel2.writeLong(preEnroll);
                        break;
                    case 14:
                        IBinder readStrongBinder7 = parcel.readStrongBinder();
                        parcel.enforceNoDataAvail();
                        int postEnroll = postEnroll(readStrongBinder7);
                        parcel2.writeNoException();
                        parcel2.writeInt(postEnroll);
                        break;
                    case 15:
                        int readInt12 = parcel.readInt();
                        String readString6 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        boolean hasEnrolledFaceMetadatas = hasEnrolledFaceMetadatas(readInt12, readString6);
                        parcel2.writeNoException();
                        parcel2.writeBoolean(hasEnrolledFaceMetadatas);
                        break;
                    case 16:
                        String readString7 = parcel.readString();
                        parcel.enforceNoDataAvail();
                        long authenticatorId = getAuthenticatorId(readString7);
                        parcel2.writeNoException();
                        parcel2.writeLong(authenticatorId);
                        break;
                    case 17:
                        byte[] createByteArray = parcel.createByteArray();
                        parcel.enforceNoDataAvail();
                        resetTimeout(createByteArray);
                        parcel2.writeNoException();
                        break;
                    case 18:
                        IFaceRecognitionServiceLockoutResetCallback asInterface5 = IFaceRecognitionServiceLockoutResetCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        addLockoutResetCallback(asInterface5);
                        parcel2.writeNoException();
                        break;
                    case 19:
                        int readInt13 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        setActiveUser(readInt13);
                        parcel2.writeNoException();
                        break;
                    case 20:
                        IBinder readStrongBinder8 = parcel.readStrongBinder();
                        int readInt14 = parcel.readInt();
                        IFaceRecognitionServiceReceiver asInterface6 = IFaceRecognitionServiceReceiver.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        enumerate(readStrongBinder8, readInt14, asInterface6);
                        parcel2.writeNoException();
                        break;
                    case 21:
                        boolean isClientActive = isClientActive();
                        parcel2.writeNoException();
                        parcel2.writeBoolean(isClientActive);
                        break;
                    case 22:
                        IFaceRecognitionClientActiveCallback asInterface7 = IFaceRecognitionClientActiveCallback.Stub.asInterface(parcel.readStrongBinder());
                        parcel.enforceNoDataAvail();
                        addClientActiveCallback(asInterface7);
                        parcel2.writeNoException();
                        break;
                    case 23:
                        int readInt15 = parcel.readInt();
                        parcel.enforceNoDataAvail();
                        String reportString = getReportString(readInt15);
                        parcel2.writeNoException();
                        parcel2.writeString(reportString);
                        break;
                    case 24:
                        boolean readBoolean = parcel.readBoolean();
                        parcel.enforceNoDataAvail();
                        warmUpOrStopHardwareDeviceByDisplayState(readBoolean);
                        break;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
                return true;
            }
            parcel2.writeString(IFaceRecognitionManagerService.DESCRIPTOR);
            return true;
        }

        /* renamed from: com.nt.facerecognition.manager.IFaceRecognitionManagerService$Stub$Proxy */
        private static class Proxy implements IFaceRecognitionManagerService {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return IFaceRecognitionManagerService.DESCRIPTOR;
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public boolean warmUpHardwareDevice(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeStrongInterface(iFaceRecognitionServiceReceiver);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void authenticate(IBinder iBinder, long j, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, int i2, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeLong(j);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iFaceRecognitionServiceReceiver);
                    obtain.writeInt(i2);
                    obtain.writeString(str);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void cancelAuthentication(IBinder iBinder, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeString(str);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, Surface surface, int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
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
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
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
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
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
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
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
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
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
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
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
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
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
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
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

            public boolean isHardwareDetected(long j, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeLong(j);
                    obtain.writeString(str);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long preEnroll(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int postEnroll(IBinder iBinder) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean hasEnrolledFaceMetadatas(int i, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public long getAuthenticatorId(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeString(str);
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readLong();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void resetTimeout(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addLockoutResetCallback(IFaceRecognitionServiceLockoutResetCallback iFaceRecognitionServiceLockoutResetCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeStrongInterface(iFaceRecognitionServiceLockoutResetCallback);
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setActiveUser(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void enumerate(IBinder iBinder, int i, IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeInt(i);
                    obtain.writeStrongInterface(iFaceRecognitionServiceReceiver);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public boolean isClientActive() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readBoolean();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addClientActiveCallback(IFaceRecognitionClientActiveCallback iFaceRecognitionClientActiveCallback) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeStrongInterface(iFaceRecognitionClientActiveCallback);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getReportString(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void warmUpOrStopHardwareDeviceByDisplayState(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    obtain.writeBoolean(z);
                    this.mRemote.transact(24, obtain, (Parcel) null, 1);
                } finally {
                    obtain.recycle();
                }
            }
        }
    }
}
