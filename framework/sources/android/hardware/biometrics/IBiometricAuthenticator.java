package android.hardware.biometrics;

import android.hardware.biometrics.IBiometricSensorReceiver;
import android.hardware.biometrics.IInvalidationCallback;
import android.hardware.biometrics.ITestSession;
import android.hardware.biometrics.ITestSessionCallback;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IBiometricAuthenticator extends IInterface {
    public static final String DESCRIPTOR = "android.hardware.biometrics.IBiometricAuthenticator";

    void cancelAuthenticationFromService(IBinder iBinder, String str, long j) throws RemoteException;

    ITestSession createTestSession(ITestSessionCallback iTestSessionCallback, String str) throws RemoteException;

    byte[] dumpSensorServiceStateProto(boolean z) throws RemoteException;

    long getAuthenticatorId(int i) throws RemoteException;

    int getLockoutModeForUser(int i) throws RemoteException;

    SensorPropertiesInternal getSensorProperties(String str) throws RemoteException;

    boolean hasEnrolledTemplates(int i, String str) throws RemoteException;

    void invalidateAuthenticatorId(int i, IInvalidationCallback iInvalidationCallback) throws RemoteException;

    boolean isHardwareDetected(String str) throws RemoteException;

    void prepareForAuthentication(boolean z, IBinder iBinder, long j, int i, IBiometricSensorReceiver iBiometricSensorReceiver, String str, long j2, int i2, boolean z2) throws RemoteException;

    void resetLockout(IBinder iBinder, String str, int i, byte[] bArr) throws RemoteException;

    void startPreparedClient(int i) throws RemoteException;

    /* loaded from: classes.dex */
    public static class Default implements IBiometricAuthenticator {
        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public ITestSession createTestSession(ITestSessionCallback callback, String opPackageName) throws RemoteException {
            return null;
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public SensorPropertiesInternal getSensorProperties(String opPackageName) throws RemoteException {
            return null;
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public byte[] dumpSensorServiceStateProto(boolean clearSchedulerBuffer) throws RemoteException {
            return null;
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public void prepareForAuthentication(boolean requireConfirmation, IBinder token, long operationId, int userId, IBiometricSensorReceiver sensorReceiver, String opPackageName, long requestId, int cookie, boolean allowBackgroundAuthentication) throws RemoteException {
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public void startPreparedClient(int cookie) throws RemoteException {
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public void cancelAuthenticationFromService(IBinder token, String opPackageName, long requestId) throws RemoteException {
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public boolean isHardwareDetected(String opPackageName) throws RemoteException {
            return false;
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public boolean hasEnrolledTemplates(int userId, String opPackageName) throws RemoteException {
            return false;
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public int getLockoutModeForUser(int userId) throws RemoteException {
            return 0;
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public void invalidateAuthenticatorId(int userId, IInvalidationCallback callback) throws RemoteException {
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public long getAuthenticatorId(int callingUserId) throws RemoteException {
            return 0L;
        }

        @Override // android.hardware.biometrics.IBiometricAuthenticator
        public void resetLockout(IBinder token, String opPackageName, int userId, byte[] hardwareAuthToken) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IBiometricAuthenticator {
        static final int TRANSACTION_cancelAuthenticationFromService = 6;
        static final int TRANSACTION_createTestSession = 1;
        static final int TRANSACTION_dumpSensorServiceStateProto = 3;
        static final int TRANSACTION_getAuthenticatorId = 11;
        static final int TRANSACTION_getLockoutModeForUser = 9;
        static final int TRANSACTION_getSensorProperties = 2;
        static final int TRANSACTION_hasEnrolledTemplates = 8;
        static final int TRANSACTION_invalidateAuthenticatorId = 10;
        static final int TRANSACTION_isHardwareDetected = 7;
        static final int TRANSACTION_prepareForAuthentication = 4;
        static final int TRANSACTION_resetLockout = 12;
        static final int TRANSACTION_startPreparedClient = 5;

        public Stub() {
            attachInterface(this, IBiometricAuthenticator.DESCRIPTOR);
        }

        public static IBiometricAuthenticator asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(IBiometricAuthenticator.DESCRIPTOR);
            if (iin != null && (iin instanceof IBiometricAuthenticator)) {
                return (IBiometricAuthenticator) iin;
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
                    return "createTestSession";
                case 2:
                    return "getSensorProperties";
                case 3:
                    return "dumpSensorServiceStateProto";
                case 4:
                    return "prepareForAuthentication";
                case 5:
                    return "startPreparedClient";
                case 6:
                    return "cancelAuthenticationFromService";
                case 7:
                    return "isHardwareDetected";
                case 8:
                    return "hasEnrolledTemplates";
                case 9:
                    return "getLockoutModeForUser";
                case 10:
                    return "invalidateAuthenticatorId";
                case 11:
                    return "getAuthenticatorId";
                case 12:
                    return "resetLockout";
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
            boolean z;
            boolean _arg0;
            boolean _arg8;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(IBiometricAuthenticator.DESCRIPTOR);
                    return true;
                default:
                    switch (code) {
                        case 1:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            ITestSessionCallback _arg02 = ITestSessionCallback.Stub.asInterface(data.readStrongBinder());
                            String _arg1 = data.readString();
                            ITestSession _result = createTestSession(_arg02, _arg1);
                            reply.writeNoException();
                            reply.writeStrongBinder(_result != null ? _result.asBinder() : null);
                            return true;
                        case 2:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            String _arg03 = data.readString();
                            SensorPropertiesInternal _result2 = getSensorProperties(_arg03);
                            reply.writeNoException();
                            if (_result2 != null) {
                                reply.writeInt(1);
                                _result2.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 3:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            if (data.readInt() == 0) {
                                z = false;
                            } else {
                                z = true;
                            }
                            boolean _arg04 = z;
                            byte[] _result3 = dumpSensorServiceStateProto(_arg04);
                            reply.writeNoException();
                            reply.writeByteArray(_result3);
                            return true;
                        case 4:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = true;
                            } else {
                                _arg0 = false;
                            }
                            IBinder _arg12 = data.readStrongBinder();
                            long _arg2 = data.readLong();
                            int _arg3 = data.readInt();
                            IBiometricSensorReceiver _arg4 = IBiometricSensorReceiver.Stub.asInterface(data.readStrongBinder());
                            String _arg5 = data.readString();
                            long _arg6 = data.readLong();
                            int _arg7 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg8 = true;
                            } else {
                                _arg8 = false;
                            }
                            prepareForAuthentication(_arg0, _arg12, _arg2, _arg3, _arg4, _arg5, _arg6, _arg7, _arg8);
                            reply.writeNoException();
                            return true;
                        case 5:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            int _arg05 = data.readInt();
                            startPreparedClient(_arg05);
                            reply.writeNoException();
                            return true;
                        case 6:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            IBinder _arg06 = data.readStrongBinder();
                            String _arg13 = data.readString();
                            long _arg22 = data.readLong();
                            cancelAuthenticationFromService(_arg06, _arg13, _arg22);
                            reply.writeNoException();
                            return true;
                        case 7:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            String _arg07 = data.readString();
                            boolean isHardwareDetected = isHardwareDetected(_arg07);
                            reply.writeNoException();
                            reply.writeInt(isHardwareDetected ? 1 : 0);
                            return true;
                        case 8:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            int _arg08 = data.readInt();
                            String _arg14 = data.readString();
                            boolean hasEnrolledTemplates = hasEnrolledTemplates(_arg08, _arg14);
                            reply.writeNoException();
                            reply.writeInt(hasEnrolledTemplates ? 1 : 0);
                            return true;
                        case 9:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            int _arg09 = data.readInt();
                            int _result4 = getLockoutModeForUser(_arg09);
                            reply.writeNoException();
                            reply.writeInt(_result4);
                            return true;
                        case 10:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            int _arg010 = data.readInt();
                            IInvalidationCallback _arg15 = IInvalidationCallback.Stub.asInterface(data.readStrongBinder());
                            invalidateAuthenticatorId(_arg010, _arg15);
                            reply.writeNoException();
                            return true;
                        case 11:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            int _arg011 = data.readInt();
                            long _result5 = getAuthenticatorId(_arg011);
                            reply.writeNoException();
                            reply.writeLong(_result5);
                            return true;
                        case 12:
                            data.enforceInterface(IBiometricAuthenticator.DESCRIPTOR);
                            IBinder _arg012 = data.readStrongBinder();
                            String _arg16 = data.readString();
                            int _arg23 = data.readInt();
                            byte[] _arg32 = data.createByteArray();
                            resetLockout(_arg012, _arg16, _arg23, _arg32);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public static class Proxy implements IBiometricAuthenticator {
            public static IBiometricAuthenticator sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return IBiometricAuthenticator.DESCRIPTOR;
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public ITestSession createTestSession(ITestSessionCallback callback, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(opPackageName);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createTestSession(callback, opPackageName);
                    }
                    _reply.readException();
                    ITestSession _result = ITestSession.Stub.asInterface(_reply.readStrongBinder());
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public SensorPropertiesInternal getSensorProperties(String opPackageName) throws RemoteException {
                SensorPropertiesInternal _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeString(opPackageName);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSensorProperties(opPackageName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = SensorPropertiesInternal.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public byte[] dumpSensorServiceStateProto(boolean clearSchedulerBuffer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeInt(clearSchedulerBuffer ? 1 : 0);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().dumpSensorServiceStateProto(clearSchedulerBuffer);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public void prepareForAuthentication(boolean requireConfirmation, IBinder token, long operationId, int userId, IBiometricSensorReceiver sensorReceiver, String opPackageName, long requestId, int cookie, boolean allowBackgroundAuthentication) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(requireConfirmation ? 1 : 0);
                    _data.writeStrongBinder(token);
                    _data.writeLong(operationId);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(sensorReceiver != null ? sensorReceiver.asBinder() : null);
                    _data.writeString(opPackageName);
                    _data.writeLong(requestId);
                    _data.writeInt(cookie);
                    if (!allowBackgroundAuthentication) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().prepareForAuthentication(requireConfirmation, token, operationId, userId, sensorReceiver, opPackageName, requestId, cookie, allowBackgroundAuthentication);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public void startPreparedClient(int cookie) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeInt(cookie);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startPreparedClient(cookie);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public void cancelAuthenticationFromService(IBinder token, String opPackageName, long requestId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPackageName);
                    _data.writeLong(requestId);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().cancelAuthenticationFromService(token, opPackageName, requestId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public boolean isHardwareDetected(String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeString(opPackageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isHardwareDetected(opPackageName);
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

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public boolean hasEnrolledTemplates(int userId, String opPackageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeString(opPackageName);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasEnrolledTemplates(userId, opPackageName);
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

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public int getLockoutModeForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLockoutModeForUser(userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public void invalidateAuthenticatorId(int userId, IInvalidationCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().invalidateAuthenticatorId(userId, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public long getAuthenticatorId(int callingUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeInt(callingUserId);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getAuthenticatorId(callingUserId);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.hardware.biometrics.IBiometricAuthenticator
            public void resetLockout(IBinder token, String opPackageName, int userId, byte[] hardwareAuthToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(IBiometricAuthenticator.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(opPackageName);
                    _data.writeInt(userId);
                    _data.writeByteArray(hardwareAuthToken);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetLockout(token, opPackageName, userId, hardwareAuthToken);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IBiometricAuthenticator impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IBiometricAuthenticator getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
