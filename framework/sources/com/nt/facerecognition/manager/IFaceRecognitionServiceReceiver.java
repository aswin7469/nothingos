package com.nt.facerecognition.manager;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes4.dex */
public interface IFaceRecognitionServiceReceiver extends IInterface {
    public static final String DESCRIPTOR = "com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver";

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

    /* loaded from: classes4.dex */
    public static class Default implements IFaceRecognitionServiceReceiver {
        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onEnrollResult(long deviceId, int faceId, int groupId, int remaining) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAcquired(long deviceId, int acquiredInfo, int vendorCode) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAuthenticationSucceeded(long deviceId, FaceMetadata fp, int userId) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAuthenticationFailed(long deviceId, int error) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAuthenticationTimeout(long deviceId) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onAuthenticationFaceDetected(long deviceId, boolean detected) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onError(long deviceId, int error, int vendorCode) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onRemoved(long deviceId, int faceId, int groupId, int remaining) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onEnumerated(long deviceId, int faceId, int groupId, int remaining) throws RemoteException {
        }

        @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
        public void onScreenBrightnessOverrided(int screenbrightnessvalue, float ambientLux) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
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

        public Stub() {
            attachInterface(this, IFaceRecognitionServiceReceiver.DESCRIPTOR);
        }

        public static IFaceRecognitionServiceReceiver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
            if (iin != null && (iin instanceof IFaceRecognitionServiceReceiver)) {
                return (IFaceRecognitionServiceReceiver) iin;
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
                    return "onWarmUpHardwareDeviceResult";
                case 2:
                    return "onEnrollResult";
                case 3:
                    return "onAcquired";
                case 4:
                    return "onAuthenticationSucceeded";
                case 5:
                    return "onAuthenticationFailed";
                case 6:
                    return "onAuthenticationTimeout";
                case 7:
                    return "onAuthenticationFaceDetected";
                case 8:
                    return "onError";
                case 9:
                    return "onRemoved";
                case 10:
                    return "onEnumerated";
                case 11:
                    return "onScreenBrightnessOverrided";
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
            CameraPreviewProperty _arg0;
            FaceMetadata _arg1;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = CameraPreviewProperty.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            onWarmUpHardwareDeviceResult(_arg0);
                            return true;
                        case 2:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg02 = data.readLong();
                            int _arg12 = data.readInt();
                            int _arg2 = data.readInt();
                            int _arg3 = data.readInt();
                            onEnrollResult(_arg02, _arg12, _arg2, _arg3);
                            return true;
                        case 3:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg03 = data.readLong();
                            int _arg13 = data.readInt();
                            int _arg22 = data.readInt();
                            onAcquired(_arg03, _arg13, _arg22);
                            return true;
                        case 4:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg04 = data.readLong();
                            if (data.readInt() != 0) {
                                _arg1 = FaceMetadata.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            int _arg23 = data.readInt();
                            onAuthenticationSucceeded(_arg04, _arg1, _arg23);
                            return true;
                        case 5:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg05 = data.readLong();
                            int _arg14 = data.readInt();
                            onAuthenticationFailed(_arg05, _arg14);
                            return true;
                        case 6:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg06 = data.readLong();
                            onAuthenticationTimeout(_arg06);
                            return true;
                        case 7:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg07 = data.readLong();
                            boolean _arg15 = data.readInt() != 0;
                            onAuthenticationFaceDetected(_arg07, _arg15);
                            return true;
                        case 8:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg08 = data.readLong();
                            int _arg16 = data.readInt();
                            int _arg24 = data.readInt();
                            onError(_arg08, _arg16, _arg24);
                            return true;
                        case 9:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg09 = data.readLong();
                            int _arg17 = data.readInt();
                            int _arg25 = data.readInt();
                            int _arg32 = data.readInt();
                            onRemoved(_arg09, _arg17, _arg25, _arg32);
                            return true;
                        case 10:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            long _arg010 = data.readLong();
                            int _arg18 = data.readInt();
                            int _arg26 = data.readInt();
                            int _arg33 = data.readInt();
                            onEnumerated(_arg010, _arg18, _arg26, _arg33);
                            return true;
                        case 11:
                            data.enforceInterface(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                            int _arg011 = data.readInt();
                            float _arg19 = data.readFloat();
                            onScreenBrightnessOverrided(_arg011, _arg19);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements IFaceRecognitionServiceReceiver {
            public static IFaceRecognitionServiceReceiver sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IFaceRecognitionServiceReceiver.DESCRIPTOR;
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onWarmUpHardwareDeviceResult(CameraPreviewProperty cameraPreviewProperty) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    if (cameraPreviewProperty != null) {
                        _data.writeInt(1);
                        cameraPreviewProperty.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(1, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onWarmUpHardwareDeviceResult(cameraPreviewProperty);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onEnrollResult(long deviceId, int faceId, int groupId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(groupId);
                    _data.writeInt(remaining);
                    boolean _status = this.mRemote.transact(2, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onEnrollResult(deviceId, faceId, groupId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onAcquired(long deviceId, int acquiredInfo, int vendorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(acquiredInfo);
                    _data.writeInt(vendorCode);
                    boolean _status = this.mRemote.transact(3, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAcquired(deviceId, acquiredInfo, vendorCode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onAuthenticationSucceeded(long deviceId, FaceMetadata fp, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    if (fp != null) {
                        _data.writeInt(1);
                        fp.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(4, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAuthenticationSucceeded(deviceId, fp, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onAuthenticationFailed(long deviceId, int error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(error);
                    boolean _status = this.mRemote.transact(5, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAuthenticationFailed(deviceId, error);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onAuthenticationTimeout(long deviceId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    boolean _status = this.mRemote.transact(6, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAuthenticationTimeout(deviceId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onAuthenticationFaceDetected(long deviceId, boolean detected) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(detected ? 1 : 0);
                    boolean _status = this.mRemote.transact(7, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onAuthenticationFaceDetected(deviceId, detected);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onError(long deviceId, int error, int vendorCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(error);
                    _data.writeInt(vendorCode);
                    boolean _status = this.mRemote.transact(8, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onError(deviceId, error, vendorCode);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onRemoved(long deviceId, int faceId, int groupId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(groupId);
                    _data.writeInt(remaining);
                    boolean _status = this.mRemote.transact(9, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onRemoved(deviceId, faceId, groupId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onEnumerated(long deviceId, int faceId, int groupId, int remaining) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeLong(deviceId);
                    _data.writeInt(faceId);
                    _data.writeInt(groupId);
                    _data.writeInt(remaining);
                    boolean _status = this.mRemote.transact(10, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onEnumerated(deviceId, faceId, groupId, remaining);
                    }
                } finally {
                    _data.recycle();
                }
            }

            @Override // com.nt.facerecognition.manager.IFaceRecognitionServiceReceiver
            public void onScreenBrightnessOverrided(int screenbrightnessvalue, float ambientLux) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IFaceRecognitionServiceReceiver.DESCRIPTOR);
                    _data.writeInt(screenbrightnessvalue);
                    _data.writeFloat(ambientLux);
                    boolean _status = this.mRemote.transact(11, _data, null, 1);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onScreenBrightnessOverrided(screenbrightnessvalue, ambientLux);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IFaceRecognitionServiceReceiver impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IFaceRecognitionServiceReceiver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
