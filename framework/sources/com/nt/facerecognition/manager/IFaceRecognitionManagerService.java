package com.nt.facerecognition.manager;

import android.graphics.Rect;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.Surface;
import com.nt.facerecognition.manager.IFaceRecognitionClientActiveCallback;
import com.nt.facerecognition.manager.IFaceRecognitionServiceLockoutResetCallback;
import com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver;
import java.util.List;
/* loaded from: classes4.dex */
public interface IFaceRecognitionManagerService extends IInterface {
    public static final String DESCRIPTOR = "com.nt.facerecognition.manager.IFaceRecognitionManagerService";

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

    void removeClientActiveCallback(IFaceRecognitionClientActiveCallback iFaceRecognitionClientActiveCallback) throws RemoteException;

    void rename(int i, int i2, String str) throws RemoteException;

    void resetTimeout(byte[] bArr) throws RemoteException;

    void setActiveUser(int i) throws RemoteException;

    boolean warmUpHardwareDevice(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver) throws RemoteException;

    boolean warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver iFaceRecognitionServiceReceiver, Surface surface, int i, int i2) throws RemoteException;

    void warmUpOrStopHardwareDeviceByDisplayState(boolean z) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements IFaceRecognitionManagerService {
        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public boolean warmUpHardwareDevice(IFaceRecognitionServiceReceiver receiver) throws RemoteException {
            return false;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void authenticate(IBinder token, long sessionId, int userId, IFaceRecognitionServiceReceiver receiver, int flags, String opPackageName) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void cancelAuthentication(IBinder token, String opPackageName) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public boolean warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver receiver, Surface surface, int width, int height) throws RemoteException {
            return false;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void enroll(IBinder token, byte[] cryptoToken, int groupId, IFaceRecognitionServiceReceiver receiver, int flags, String opPackageName, Rect frameMarkRect) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void cancelEnrollment(IBinder token) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public boolean closeHardwareDevice() throws RemoteException {
            return false;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void remove(IBinder token, int faceId, int groupId, int userId, IFaceRecognitionServiceReceiver receiver) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void removeAllFaceMetadata(IBinder token, int userId, IFaceRecognitionServiceReceiver receiver) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void rename(int faceId, int groupId, String name) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public List<FaceMetadata> getEnrolledFaceMetadatas(int groupId, String opPackageName) throws RemoteException {
            return null;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public boolean isHardwareDetected(long deviceId, String opPackageName) throws RemoteException {
            return false;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public long preEnroll(IBinder token) throws RemoteException {
            return 0L;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public int postEnroll(IBinder token) throws RemoteException {
            return 0;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public boolean hasEnrolledFaceMetadatas(int groupId, String opPackageName) throws RemoteException {
            return false;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public long getAuthenticatorId(String opPackageName) throws RemoteException {
            return 0L;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void resetTimeout(byte[] cryptoToken) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void addLockoutResetCallback(IFaceRecognitionServiceLockoutResetCallback callback) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void setActiveUser(int uid) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void enumerate(IBinder token, int userId, IFaceRecognitionServiceReceiver receiver) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public boolean isClientActive() throws RemoteException {
            return false;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void addClientActiveCallback(IFaceRecognitionClientActiveCallback callback) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void removeClientActiveCallback(IFaceRecognitionClientActiveCallback callback) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public String getReportString(int msg) throws RemoteException {
            return null;
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
        public void warmUpOrStopHardwareDeviceByDisplayState(boolean warmUp) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
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
        static final int TRANSACTION_getReportString = 24;
        static final int TRANSACTION_hasEnrolledFaceMetadatas = 15;
        static final int TRANSACTION_isClientActive = 21;
        static final int TRANSACTION_isHardwareDetected = 12;
        static final int TRANSACTION_postEnroll = 14;
        static final int TRANSACTION_preEnroll = 13;
        static final int TRANSACTION_remove = 8;
        static final int TRANSACTION_removeAllFaceMetadata = 9;
        static final int TRANSACTION_removeClientActiveCallback = 23;
        static final int TRANSACTION_rename = 10;
        static final int TRANSACTION_resetTimeout = 17;
        static final int TRANSACTION_setActiveUser = 19;
        static final int TRANSACTION_warmUpHardwareDevice = 1;
        static final int TRANSACTION_warmUpHardwareDeviceForPreview = 4;
        static final int TRANSACTION_warmUpOrStopHardwareDeviceByDisplayState = 25;

        public Stub() {
            attachInterface(this, IFaceRecognitionManagerService.DESCRIPTOR);
        }

        public static IFaceRecognitionManagerService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IFaceRecognitionManagerService.DESCRIPTOR);
            if (iin != null && (iin instanceof IFaceRecognitionManagerService)) {
                return (IFaceRecognitionManagerService) iin;
            }
            return new Proxy(obj);
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "warmUpHardwareDevice";
                case 2:
                    return "authenticate";
                case 3:
                    return "cancelAuthentication";
                case 4:
                    return "warmUpHardwareDeviceForPreview";
                case 5:
                    return "enroll";
                case 6:
                    return "cancelEnrollment";
                case 7:
                    return "closeHardwareDevice";
                case 8:
                    return "remove";
                case 9:
                    return "removeAllFaceMetadata";
                case 10:
                    return "rename";
                case 11:
                    return "getEnrolledFaceMetadatas";
                case 12:
                    return "isHardwareDetected";
                case 13:
                    return "preEnroll";
                case 14:
                    return "postEnroll";
                case 15:
                    return "hasEnrolledFaceMetadatas";
                case 16:
                    return "getAuthenticatorId";
                case 17:
                    return "resetTimeout";
                case 18:
                    return "addLockoutResetCallback";
                case 19:
                    return "setActiveUser";
                case 20:
                    return "enumerate";
                case 21:
                    return "isClientActive";
                case 22:
                    return "addClientActiveCallback";
                case 23:
                    return "removeClientActiveCallback";
                case 24:
                    return "getReportString";
                case 25:
                    return "warmUpOrStopHardwareDeviceByDisplayState";
                default:
                    return null;
            }
        }

        @Override // android.os.Binder
        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        @Override // android.os.Binder
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Surface _arg1;
            Rect _arg6;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IFaceRecognitionManagerService.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IFaceRecognitionServiceReceiver _arg0 = IFaceRecognitionServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            boolean warmUpHardwareDevice = warmUpHardwareDevice(_arg0);
                            reply.writeNoException();
                            reply.writeInt(warmUpHardwareDevice ? 1 : 0);
                            return true;
                        case 2:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg02 = data.readStrongBinder();
                            long _arg12 = data.readLong();
                            int _arg2 = data.readInt();
                            IFaceRecognitionServiceReceiver _arg3 = IFaceRecognitionServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            int _arg4 = data.readInt();
                            String _arg5 = data.readString();
                            authenticate(_arg02, _arg12, _arg2, _arg3, _arg4, _arg5);
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg03 = data.readStrongBinder();
                            String _arg13 = data.readString();
                            cancelAuthentication(_arg03, _arg13);
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IFaceRecognitionServiceReceiver _arg04 = IFaceRecognitionServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            if (data.readInt() != 0) {
                                _arg1 = Surface.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            int _arg22 = data.readInt();
                            int _arg32 = data.readInt();
                            boolean warmUpHardwareDeviceForPreview = warmUpHardwareDeviceForPreview(_arg04, _arg1, _arg22, _arg32);
                            reply.writeNoException();
                            reply.writeInt(warmUpHardwareDeviceForPreview ? 1 : 0);
                            return true;
                        case 5:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg05 = data.readStrongBinder();
                            byte[] _arg14 = data.createByteArray();
                            int _arg23 = data.readInt();
                            IFaceRecognitionServiceReceiver _arg33 = IFaceRecognitionServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            int _arg42 = data.readInt();
                            String _arg52 = data.readString();
                            if (data.readInt() != 0) {
                                _arg6 = Rect.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg6 = null;
                            }
                            enroll(_arg05, _arg14, _arg23, _arg33, _arg42, _arg52, _arg6);
                            reply.writeNoException();
                            return true;
                        case 6:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg06 = data.readStrongBinder();
                            cancelEnrollment(_arg06);
                            reply.writeNoException();
                            return true;
                        case 7:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            boolean closeHardwareDevice = closeHardwareDevice();
                            reply.writeNoException();
                            reply.writeInt(closeHardwareDevice ? 1 : 0);
                            return true;
                        case 8:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg07 = data.readStrongBinder();
                            int _arg15 = data.readInt();
                            int _arg24 = data.readInt();
                            int _arg34 = data.readInt();
                            IFaceRecognitionServiceReceiver _arg43 = IFaceRecognitionServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            remove(_arg07, _arg15, _arg24, _arg34, _arg43);
                            reply.writeNoException();
                            return true;
                        case 9:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg08 = data.readStrongBinder();
                            int _arg16 = data.readInt();
                            IFaceRecognitionServiceReceiver _arg25 = IFaceRecognitionServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            removeAllFaceMetadata(_arg08, _arg16, _arg25);
                            reply.writeNoException();
                            return true;
                        case 10:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            int _arg09 = data.readInt();
                            int _arg17 = data.readInt();
                            String _arg26 = data.readString();
                            rename(_arg09, _arg17, _arg26);
                            reply.writeNoException();
                            return true;
                        case 11:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            int _arg010 = data.readInt();
                            String _arg18 = data.readString();
                            List<FaceMetadata> _result = getEnrolledFaceMetadatas(_arg010, _arg18);
                            reply.writeNoException();
                            reply.writeTypedList(_result);
                            return true;
                        case 12:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            long _arg011 = data.readLong();
                            String _arg19 = data.readString();
                            boolean isHardwareDetected = isHardwareDetected(_arg011, _arg19);
                            reply.writeNoException();
                            reply.writeInt(isHardwareDetected ? 1 : 0);
                            return true;
                        case 13:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg012 = data.readStrongBinder();
                            long _result2 = preEnroll(_arg012);
                            reply.writeNoException();
                            reply.writeLong(_result2);
                            return true;
                        case 14:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg013 = data.readStrongBinder();
                            int _result3 = postEnroll(_arg013);
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 15:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            int _arg014 = data.readInt();
                            String _arg110 = data.readString();
                            boolean hasEnrolledFaceMetadatas = hasEnrolledFaceMetadatas(_arg014, _arg110);
                            reply.writeNoException();
                            reply.writeInt(hasEnrolledFaceMetadatas ? 1 : 0);
                            return true;
                        case 16:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            String _arg015 = data.readString();
                            long _result4 = getAuthenticatorId(_arg015);
                            reply.writeNoException();
                            reply.writeLong(_result4);
                            return true;
                        case 17:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            byte[] _arg016 = data.createByteArray();
                            resetTimeout(_arg016);
                            reply.writeNoException();
                            return true;
                        case 18:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IFaceRecognitionServiceLockoutResetCallback _arg017 = IFaceRecognitionServiceLockoutResetCallback.Stub.asInterface(data.readStrongBinder());
                            addLockoutResetCallback(_arg017);
                            reply.writeNoException();
                            return true;
                        case 19:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            int _arg018 = data.readInt();
                            setActiveUser(_arg018);
                            reply.writeNoException();
                            return true;
                        case 20:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IBinder _arg019 = data.readStrongBinder();
                            int _arg111 = data.readInt();
                            IFaceRecognitionServiceReceiver _arg27 = IFaceRecognitionServiceReceiver.Stub.asInterface(data.readStrongBinder());
                            enumerate(_arg019, _arg111, _arg27);
                            reply.writeNoException();
                            return true;
                        case 21:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            boolean isClientActive = isClientActive();
                            reply.writeNoException();
                            reply.writeInt(isClientActive ? 1 : 0);
                            return true;
                        case 22:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IFaceRecognitionClientActiveCallback _arg020 = IFaceRecognitionClientActiveCallback.Stub.asInterface(data.readStrongBinder());
                            addClientActiveCallback(_arg020);
                            reply.writeNoException();
                            return true;
                        case 23:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            IFaceRecognitionClientActiveCallback _arg021 = IFaceRecognitionClientActiveCallback.Stub.asInterface(data.readStrongBinder());
                            removeClientActiveCallback(_arg021);
                            reply.writeNoException();
                            return true;
                        case 24:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            int _arg022 = data.readInt();
                            String _result5 = getReportString(_arg022);
                            reply.writeNoException();
                            reply.writeString(_result5);
                            return true;
                        case 25:
                            data.enforceInterface(IFaceRecognitionManagerService.DESCRIPTOR);
                            boolean _arg023 = data.readInt() != 0;
                            warmUpOrStopHardwareDeviceByDisplayState(_arg023);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IFaceRecognitionManagerService {
            public static IFaceRecognitionManagerService sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IFaceRecognitionManagerService.DESCRIPTOR;
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public boolean warmUpHardwareDevice(IFaceRecognitionServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().warmUpHardwareDevice(receiver);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void authenticate(IBinder token, long sessionId, int userId, IFaceRecognitionServiceReceiver receiver, int flags, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(token);
                    try {
                        _data.writeLong(sessionId);
                        try {
                            _data.writeInt(userId);
                            _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                            try {
                                _data.writeInt(flags);
                                _data.writeString(opPackageName);
                                boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    Stub.getDefaultImpl().authenticate(token, sessionId, userId, receiver, flags, opPackageName);
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void cancelAuthentication(IBinder token, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPackageName);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelAuthentication(token, opPackageName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public boolean warmUpHardwareDeviceForPreview(IFaceRecognitionServiceReceiver receiver, Surface surface, int width, int height) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _result = true;
                    if (surface != null) {
                        _data.writeInt(1);
                        surface.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(width);
                    _data.writeInt(height);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().warmUpHardwareDeviceForPreview(receiver, surface, width, height);
                    }
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void enroll(IBinder token, byte[] cryptoToken, int groupId, IFaceRecognitionServiceReceiver receiver, int flags, String opPackageName, Rect frameMarkRect) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                } catch (Throwable th) {
                    th = th;
                }
                try {
                    _data.writeStrongBinder(token);
                    try {
                        _data.writeByteArray(cryptoToken);
                        try {
                            _data.writeInt(groupId);
                            _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                            try {
                                _data.writeInt(flags);
                                _data.writeString(opPackageName);
                                if (frameMarkRect != null) {
                                    _data.writeInt(1);
                                    frameMarkRect.writeToParcel(_data, 0);
                                } else {
                                    _data.writeInt(0);
                                }
                                boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    Stub.getDefaultImpl().enroll(token, cryptoToken, groupId, receiver, flags, opPackageName, frameMarkRect);
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void cancelEnrollment(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelEnrollment(token);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public boolean closeHardwareDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().closeHardwareDevice();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void remove(IBinder token, int faceId, int groupId, int userId, IFaceRecognitionServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(faceId);
                    _data.writeInt(groupId);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().remove(token, faceId, groupId, userId, receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void removeAllFaceMetadata(IBinder token, int userId, IFaceRecognitionServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeAllFaceMetadata(token, userId, receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void rename(int faceId, int groupId, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeInt(faceId);
                    _data.writeInt(groupId);
                    _data.writeString(name);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().rename(faceId, groupId, name);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public List<FaceMetadata> getEnrolledFaceMetadatas(int groupId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeString(opPackageName);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getEnrolledFaceMetadatas(groupId, opPackageName);
                    }
                    _reply.readException();
                    List<FaceMetadata> _result = _reply.createTypedArrayList(FaceMetadata.CREATOR);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public boolean isHardwareDetected(long deviceId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeString(opPackageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHardwareDetected(deviceId, opPackageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public long preEnroll(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().preEnroll(token);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public int postEnroll(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().postEnroll(token);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public boolean hasEnrolledFaceMetadatas(int groupId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeInt(groupId);
                    _data.writeString(opPackageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasEnrolledFaceMetadatas(groupId, opPackageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public long getAuthenticatorId(String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeString(opPackageName);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAuthenticatorId(opPackageName);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void resetTimeout(byte[] cryptoToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeByteArray(cryptoToken);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetTimeout(cryptoToken);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void addLockoutResetCallback(IFaceRecognitionServiceLockoutResetCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addLockoutResetCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void setActiveUser(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeInt(uid);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setActiveUser(uid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void enumerate(IBinder token, int userId, IFaceRecognitionServiceReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().enumerate(token, userId, receiver);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public boolean isClientActive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isClientActive();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        z = true;
                    }
                    boolean _status2 = z;
                    return _status2;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void addClientActiveCallback(IFaceRecognitionClientActiveCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addClientActiveCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void removeClientActiveCallback(IFaceRecognitionClientActiveCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeClientActiveCallback(callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public String getReportString(int msg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeInt(msg);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getReportString(msg);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionManagerService
            public void warmUpOrStopHardwareDeviceByDisplayState(boolean warmUp) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionManagerService.DESCRIPTOR);
                    _data.writeInt(warmUp ? 1 : 0);
                    boolean _status = this.mRemote.transact(25, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().warmUpOrStopHardwareDeviceByDisplayState(warmUp);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFaceRecognitionManagerService impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFaceRecognitionManagerService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
