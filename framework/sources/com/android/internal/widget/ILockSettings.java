package com.android.internal.widget;

import android.app.PendingIntent;
import android.app.trust.IStrongAuthTracker;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.security.keystore.recovery.KeyChainProtectionParams;
import android.security.keystore.recovery.KeyChainSnapshot;
import android.security.keystore.recovery.RecoveryCertPath;
import android.security.keystore.recovery.WrappedApplicationKey;
import com.android.internal.widget.ICheckCredentialProgressCallback;
import java.util.List;
import java.util.Map;
/* loaded from: classes4.dex */
public interface ILockSettings extends IInterface {
    VerifyCredentialResponse checkCredential(LockscreenCredential lockscreenCredential, int i, ICheckCredentialProgressCallback iCheckCredentialProgressCallback) throws RemoteException;

    boolean checkVoldPassword(int i) throws RemoteException;

    void closeSession(String str) throws RemoteException;

    String generateKey(String str) throws RemoteException;

    String generateKeyWithMetadata(String str, byte[] bArr) throws RemoteException;

    boolean getBoolean(String str, boolean z, int i) throws RemoteException;

    int getCredentialType(int i) throws RemoteException;

    byte[] getHashFactor(LockscreenCredential lockscreenCredential, int i) throws RemoteException;

    String getKey(String str) throws RemoteException;

    KeyChainSnapshot getKeyChainSnapshot() throws RemoteException;

    long getLong(String str, long j, int i) throws RemoteException;

    String getPassword() throws RemoteException;

    int[] getRecoverySecretTypes() throws RemoteException;

    Map getRecoveryStatus() throws RemoteException;

    boolean getSeparateProfileChallengeEnabled(int i) throws RemoteException;

    String getString(String str, String str2, int i) throws RemoteException;

    int getStrongAuthForUser(int i) throws RemoteException;

    boolean hasPendingEscrowToken(int i) throws RemoteException;

    boolean hasSecureLockScreen() throws RemoteException;

    String importKey(String str, byte[] bArr) throws RemoteException;

    String importKeyWithMetadata(String str, byte[] bArr, byte[] bArr2) throws RemoteException;

    void initRecoveryServiceWithSigFile(String str, byte[] bArr, byte[] bArr2) throws RemoteException;

    Map recoverKeyChainSnapshot(String str, byte[] bArr, List<WrappedApplicationKey> list) throws RemoteException;

    void registerStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) throws RemoteException;

    void removeCachedUnifiedChallenge(int i) throws RemoteException;

    void removeGatekeeperPasswordHandle(long j) throws RemoteException;

    void removeKey(String str) throws RemoteException;

    void reportSuccessfulBiometricUnlock(boolean z, int i) throws RemoteException;

    void requireStrongAuth(int i, int i2) throws RemoteException;

    void resetKeyStore(int i) throws RemoteException;

    void sanitizePassword() throws RemoteException;

    void scheduleNonStrongBiometricIdleTimeout(int i) throws RemoteException;

    void setBoolean(String str, boolean z, int i) throws RemoteException;

    boolean setLockCredential(LockscreenCredential lockscreenCredential, LockscreenCredential lockscreenCredential2, int i) throws RemoteException;

    void setLong(String str, long j, int i) throws RemoteException;

    void setRecoverySecretTypes(int[] iArr) throws RemoteException;

    void setRecoveryStatus(String str, int i) throws RemoteException;

    void setSeparateProfileChallengeEnabled(int i, boolean z, LockscreenCredential lockscreenCredential) throws RemoteException;

    void setServerParams(byte[] bArr) throws RemoteException;

    void setSnapshotCreatedPendingIntent(PendingIntent pendingIntent) throws RemoteException;

    void setString(String str, String str2, int i) throws RemoteException;

    byte[] startRecoverySessionWithCertPath(String str, String str2, RecoveryCertPath recoveryCertPath, byte[] bArr, byte[] bArr2, List<KeyChainProtectionParams> list) throws RemoteException;

    void systemReady() throws RemoteException;

    boolean tryUnlockWithCachedUnifiedChallenge(int i) throws RemoteException;

    void unregisterStrongAuthTracker(IStrongAuthTracker iStrongAuthTracker) throws RemoteException;

    void updateEncryptionPassword(int i, byte[] bArr) throws RemoteException;

    void userPresent(int i) throws RemoteException;

    VerifyCredentialResponse verifyCredential(LockscreenCredential lockscreenCredential, int i, int i2) throws RemoteException;

    VerifyCredentialResponse verifyGatekeeperPasswordHandle(long j, long j2, int i) throws RemoteException;

    VerifyCredentialResponse verifyTiedProfileChallenge(LockscreenCredential lockscreenCredential, int i, int i2) throws RemoteException;

    /* loaded from: classes4.dex */
    public static class Default implements ILockSettings {
        @Override // com.android.internal.widget.ILockSettings
        public void setBoolean(String key, boolean value, int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void setLong(String key, long value, int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void setString(String key, String value, int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public boolean getBoolean(String key, boolean defaultValue, int userId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.widget.ILockSettings
        public long getLong(String key, long defaultValue, int userId) throws RemoteException {
            return 0L;
        }

        @Override // com.android.internal.widget.ILockSettings
        public String getString(String key, String defaultValue, int userId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public boolean setLockCredential(LockscreenCredential credential, LockscreenCredential savedCredential, int userId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void resetKeyStore(int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public VerifyCredentialResponse checkCredential(LockscreenCredential credential, int userId, ICheckCredentialProgressCallback progressCallback) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public VerifyCredentialResponse verifyCredential(LockscreenCredential credential, int userId, int flags) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public VerifyCredentialResponse verifyTiedProfileChallenge(LockscreenCredential credential, int userId, int flags) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public VerifyCredentialResponse verifyGatekeeperPasswordHandle(long gatekeeperPasswordHandle, long challenge, int userId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void removeGatekeeperPasswordHandle(long gatekeeperPasswordHandle) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public boolean checkVoldPassword(int userId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.widget.ILockSettings
        public int getCredentialType(int userId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.widget.ILockSettings
        public byte[] getHashFactor(LockscreenCredential currentCredential, int userId) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void setSeparateProfileChallengeEnabled(int userId, boolean enabled, LockscreenCredential managedUserPassword) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public boolean getSeparateProfileChallengeEnabled(int userId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void registerStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void unregisterStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void requireStrongAuth(int strongAuthReason, int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void reportSuccessfulBiometricUnlock(boolean isStrongBiometric, int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void scheduleNonStrongBiometricIdleTimeout(int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void systemReady() throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void userPresent(int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public int getStrongAuthForUser(int userId) throws RemoteException {
            return 0;
        }

        @Override // com.android.internal.widget.ILockSettings
        public boolean hasPendingEscrowToken(int userId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void initRecoveryServiceWithSigFile(String rootCertificateAlias, byte[] recoveryServiceCertFile, byte[] recoveryServiceSigFile) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public KeyChainSnapshot getKeyChainSnapshot() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public String generateKey(String alias) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public String generateKeyWithMetadata(String alias, byte[] metadata) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public String importKey(String alias, byte[] keyBytes) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public String importKeyWithMetadata(String alias, byte[] keyBytes, byte[] metadata) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public String getKey(String alias) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void removeKey(String alias) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void setSnapshotCreatedPendingIntent(PendingIntent intent) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void setServerParams(byte[] serverParams) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void setRecoveryStatus(String alias, int status) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public Map getRecoveryStatus() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public int[] getRecoverySecretTypes() throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public byte[] startRecoverySessionWithCertPath(String sessionId, String rootCertificateAlias, RecoveryCertPath verifierCertPath, byte[] vaultParams, byte[] vaultChallenge, List<KeyChainProtectionParams> secrets) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public Map recoverKeyChainSnapshot(String sessionId, byte[] recoveryKeyBlob, List<WrappedApplicationKey> applicationKeys) throws RemoteException {
            return null;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void closeSession(String sessionId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public boolean hasSecureLockScreen() throws RemoteException {
            return false;
        }

        @Override // com.android.internal.widget.ILockSettings
        public boolean tryUnlockWithCachedUnifiedChallenge(int userId) throws RemoteException {
            return false;
        }

        @Override // com.android.internal.widget.ILockSettings
        public void removeCachedUnifiedChallenge(int userId) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void updateEncryptionPassword(int type, byte[] password) throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public void sanitizePassword() throws RemoteException {
        }

        @Override // com.android.internal.widget.ILockSettings
        public String getPassword() throws RemoteException {
            return null;
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes4.dex */
    public static abstract class Stub extends Binder implements ILockSettings {
        public static final String DESCRIPTOR = "com.android.internal.widget.ILockSettings";
        static final int TRANSACTION_checkCredential = 9;
        static final int TRANSACTION_checkVoldPassword = 14;
        static final int TRANSACTION_closeSession = 44;
        static final int TRANSACTION_generateKey = 30;
        static final int TRANSACTION_generateKeyWithMetadata = 31;
        static final int TRANSACTION_getBoolean = 4;
        static final int TRANSACTION_getCredentialType = 15;
        static final int TRANSACTION_getHashFactor = 16;
        static final int TRANSACTION_getKey = 34;
        static final int TRANSACTION_getKeyChainSnapshot = 29;
        static final int TRANSACTION_getLong = 5;
        static final int TRANSACTION_getPassword = 50;
        static final int TRANSACTION_getRecoverySecretTypes = 41;
        static final int TRANSACTION_getRecoveryStatus = 39;
        static final int TRANSACTION_getSeparateProfileChallengeEnabled = 18;
        static final int TRANSACTION_getString = 6;
        static final int TRANSACTION_getStrongAuthForUser = 26;
        static final int TRANSACTION_hasPendingEscrowToken = 27;
        static final int TRANSACTION_hasSecureLockScreen = 45;
        static final int TRANSACTION_importKey = 32;
        static final int TRANSACTION_importKeyWithMetadata = 33;
        static final int TRANSACTION_initRecoveryServiceWithSigFile = 28;
        static final int TRANSACTION_recoverKeyChainSnapshot = 43;
        static final int TRANSACTION_registerStrongAuthTracker = 19;
        static final int TRANSACTION_removeCachedUnifiedChallenge = 47;
        static final int TRANSACTION_removeGatekeeperPasswordHandle = 13;
        static final int TRANSACTION_removeKey = 35;
        static final int TRANSACTION_reportSuccessfulBiometricUnlock = 22;
        static final int TRANSACTION_requireStrongAuth = 21;
        static final int TRANSACTION_resetKeyStore = 8;
        static final int TRANSACTION_sanitizePassword = 49;
        static final int TRANSACTION_scheduleNonStrongBiometricIdleTimeout = 23;
        static final int TRANSACTION_setBoolean = 1;
        static final int TRANSACTION_setLockCredential = 7;
        static final int TRANSACTION_setLong = 2;
        static final int TRANSACTION_setRecoverySecretTypes = 40;
        static final int TRANSACTION_setRecoveryStatus = 38;
        static final int TRANSACTION_setSeparateProfileChallengeEnabled = 17;
        static final int TRANSACTION_setServerParams = 37;
        static final int TRANSACTION_setSnapshotCreatedPendingIntent = 36;
        static final int TRANSACTION_setString = 3;
        static final int TRANSACTION_startRecoverySessionWithCertPath = 42;
        static final int TRANSACTION_systemReady = 24;
        static final int TRANSACTION_tryUnlockWithCachedUnifiedChallenge = 46;
        static final int TRANSACTION_unregisterStrongAuthTracker = 20;
        static final int TRANSACTION_updateEncryptionPassword = 48;
        static final int TRANSACTION_userPresent = 25;
        static final int TRANSACTION_verifyCredential = 10;
        static final int TRANSACTION_verifyGatekeeperPasswordHandle = 12;
        static final int TRANSACTION_verifyTiedProfileChallenge = 11;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILockSettings asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof ILockSettings)) {
                return (ILockSettings) iin;
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
                    return "setBoolean";
                case 2:
                    return "setLong";
                case 3:
                    return "setString";
                case 4:
                    return "getBoolean";
                case 5:
                    return "getLong";
                case 6:
                    return "getString";
                case 7:
                    return "setLockCredential";
                case 8:
                    return "resetKeyStore";
                case 9:
                    return "checkCredential";
                case 10:
                    return "verifyCredential";
                case 11:
                    return "verifyTiedProfileChallenge";
                case 12:
                    return "verifyGatekeeperPasswordHandle";
                case 13:
                    return "removeGatekeeperPasswordHandle";
                case 14:
                    return "checkVoldPassword";
                case 15:
                    return "getCredentialType";
                case 16:
                    return "getHashFactor";
                case 17:
                    return "setSeparateProfileChallengeEnabled";
                case 18:
                    return "getSeparateProfileChallengeEnabled";
                case 19:
                    return "registerStrongAuthTracker";
                case 20:
                    return "unregisterStrongAuthTracker";
                case 21:
                    return "requireStrongAuth";
                case 22:
                    return "reportSuccessfulBiometricUnlock";
                case 23:
                    return "scheduleNonStrongBiometricIdleTimeout";
                case 24:
                    return "systemReady";
                case 25:
                    return "userPresent";
                case 26:
                    return "getStrongAuthForUser";
                case 27:
                    return "hasPendingEscrowToken";
                case 28:
                    return "initRecoveryServiceWithSigFile";
                case 29:
                    return "getKeyChainSnapshot";
                case 30:
                    return "generateKey";
                case 31:
                    return "generateKeyWithMetadata";
                case 32:
                    return "importKey";
                case 33:
                    return "importKeyWithMetadata";
                case 34:
                    return "getKey";
                case 35:
                    return "removeKey";
                case 36:
                    return "setSnapshotCreatedPendingIntent";
                case 37:
                    return "setServerParams";
                case 38:
                    return "setRecoveryStatus";
                case 39:
                    return "getRecoveryStatus";
                case 40:
                    return "setRecoverySecretTypes";
                case 41:
                    return "getRecoverySecretTypes";
                case 42:
                    return "startRecoverySessionWithCertPath";
                case 43:
                    return "recoverKeyChainSnapshot";
                case 44:
                    return "closeSession";
                case 45:
                    return "hasSecureLockScreen";
                case 46:
                    return "tryUnlockWithCachedUnifiedChallenge";
                case 47:
                    return "removeCachedUnifiedChallenge";
                case 48:
                    return "updateEncryptionPassword";
                case 49:
                    return "sanitizePassword";
                case 50:
                    return "getPassword";
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
            LockscreenCredential _arg0;
            LockscreenCredential _arg1;
            LockscreenCredential _arg02;
            LockscreenCredential _arg03;
            LockscreenCredential _arg04;
            LockscreenCredential _arg05;
            LockscreenCredential _arg2;
            PendingIntent _arg06;
            RecoveryCertPath _arg22;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    boolean z = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg07 = data.readString();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg12 = z;
                            int _arg23 = data.readInt();
                            setBoolean(_arg07, _arg12, _arg23);
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg08 = data.readString();
                            long _arg13 = data.readLong();
                            int _arg24 = data.readInt();
                            setLong(_arg08, _arg13, _arg24);
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg09 = data.readString();
                            String _arg14 = data.readString();
                            int _arg25 = data.readInt();
                            setString(_arg09, _arg14, _arg25);
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg010 = data.readString();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg15 = z;
                            int _arg26 = data.readInt();
                            boolean z2 = getBoolean(_arg010, _arg15, _arg26);
                            reply.writeNoException();
                            reply.writeInt(z2 ? 1 : 0);
                            return true;
                        case 5:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg011 = data.readString();
                            long _arg16 = data.readLong();
                            int _arg27 = data.readInt();
                            long _result = getLong(_arg011, _arg16, _arg27);
                            reply.writeNoException();
                            reply.writeLong(_result);
                            return true;
                        case 6:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg012 = data.readString();
                            String _arg17 = data.readString();
                            int _arg28 = data.readInt();
                            String _result2 = getString(_arg012, _arg17, _arg28);
                            reply.writeNoException();
                            reply.writeString(_result2);
                            return true;
                        case 7:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = LockscreenCredential.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg1 = LockscreenCredential.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg1 = null;
                            }
                            int _arg29 = data.readInt();
                            boolean lockCredential = setLockCredential(_arg0, _arg1, _arg29);
                            reply.writeNoException();
                            reply.writeInt(lockCredential ? 1 : 0);
                            return true;
                        case 8:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg013 = data.readInt();
                            resetKeyStore(_arg013);
                            reply.writeNoException();
                            return true;
                        case 9:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg02 = LockscreenCredential.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg02 = null;
                            }
                            int _arg18 = data.readInt();
                            ICheckCredentialProgressCallback _arg210 = ICheckCredentialProgressCallback.Stub.asInterface(data.readStrongBinder());
                            VerifyCredentialResponse _result3 = checkCredential(_arg02, _arg18, _arg210);
                            reply.writeNoException();
                            if (_result3 != null) {
                                reply.writeInt(1);
                                _result3.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 10:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg03 = LockscreenCredential.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg03 = null;
                            }
                            int _arg19 = data.readInt();
                            int _arg211 = data.readInt();
                            VerifyCredentialResponse _result4 = verifyCredential(_arg03, _arg19, _arg211);
                            reply.writeNoException();
                            if (_result4 != null) {
                                reply.writeInt(1);
                                _result4.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 11:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg04 = LockscreenCredential.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg04 = null;
                            }
                            int _arg110 = data.readInt();
                            int _arg212 = data.readInt();
                            VerifyCredentialResponse _result5 = verifyTiedProfileChallenge(_arg04, _arg110, _arg212);
                            reply.writeNoException();
                            if (_result5 != null) {
                                reply.writeInt(1);
                                _result5.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 12:
                            data.enforceInterface(DESCRIPTOR);
                            long _arg014 = data.readLong();
                            long _arg111 = data.readLong();
                            int _arg213 = data.readInt();
                            VerifyCredentialResponse _result6 = verifyGatekeeperPasswordHandle(_arg014, _arg111, _arg213);
                            reply.writeNoException();
                            if (_result6 != null) {
                                reply.writeInt(1);
                                _result6.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 13:
                            data.enforceInterface(DESCRIPTOR);
                            long _arg015 = data.readLong();
                            removeGatekeeperPasswordHandle(_arg015);
                            reply.writeNoException();
                            return true;
                        case 14:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg016 = data.readInt();
                            boolean checkVoldPassword = checkVoldPassword(_arg016);
                            reply.writeNoException();
                            reply.writeInt(checkVoldPassword ? 1 : 0);
                            return true;
                        case 15:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg017 = data.readInt();
                            int _result7 = getCredentialType(_arg017);
                            reply.writeNoException();
                            reply.writeInt(_result7);
                            return true;
                        case 16:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg05 = LockscreenCredential.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg05 = null;
                            }
                            int _arg112 = data.readInt();
                            byte[] _result8 = getHashFactor(_arg05, _arg112);
                            reply.writeNoException();
                            reply.writeByteArray(_result8);
                            return true;
                        case 17:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg018 = data.readInt();
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg113 = z;
                            if (data.readInt() != 0) {
                                _arg2 = LockscreenCredential.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg2 = null;
                            }
                            setSeparateProfileChallengeEnabled(_arg018, _arg113, _arg2);
                            reply.writeNoException();
                            return true;
                        case 18:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg019 = data.readInt();
                            boolean separateProfileChallengeEnabled = getSeparateProfileChallengeEnabled(_arg019);
                            reply.writeNoException();
                            reply.writeInt(separateProfileChallengeEnabled ? 1 : 0);
                            return true;
                        case 19:
                            data.enforceInterface(DESCRIPTOR);
                            IStrongAuthTracker _arg020 = IStrongAuthTracker.Stub.asInterface(data.readStrongBinder());
                            registerStrongAuthTracker(_arg020);
                            reply.writeNoException();
                            return true;
                        case 20:
                            data.enforceInterface(DESCRIPTOR);
                            IStrongAuthTracker _arg021 = IStrongAuthTracker.Stub.asInterface(data.readStrongBinder());
                            unregisterStrongAuthTracker(_arg021);
                            reply.writeNoException();
                            return true;
                        case 21:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg022 = data.readInt();
                            int _arg114 = data.readInt();
                            requireStrongAuth(_arg022, _arg114);
                            reply.writeNoException();
                            return true;
                        case 22:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                z = true;
                            }
                            boolean _arg023 = z;
                            int _arg115 = data.readInt();
                            reportSuccessfulBiometricUnlock(_arg023, _arg115);
                            reply.writeNoException();
                            return true;
                        case 23:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg024 = data.readInt();
                            scheduleNonStrongBiometricIdleTimeout(_arg024);
                            reply.writeNoException();
                            return true;
                        case 24:
                            data.enforceInterface(DESCRIPTOR);
                            systemReady();
                            reply.writeNoException();
                            return true;
                        case 25:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg025 = data.readInt();
                            userPresent(_arg025);
                            reply.writeNoException();
                            return true;
                        case 26:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg026 = data.readInt();
                            int _result9 = getStrongAuthForUser(_arg026);
                            reply.writeNoException();
                            reply.writeInt(_result9);
                            return true;
                        case 27:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg027 = data.readInt();
                            boolean hasPendingEscrowToken = hasPendingEscrowToken(_arg027);
                            reply.writeNoException();
                            reply.writeInt(hasPendingEscrowToken ? 1 : 0);
                            return true;
                        case 28:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg028 = data.readString();
                            byte[] _arg116 = data.createByteArray();
                            byte[] _arg214 = data.createByteArray();
                            initRecoveryServiceWithSigFile(_arg028, _arg116, _arg214);
                            reply.writeNoException();
                            return true;
                        case 29:
                            data.enforceInterface(DESCRIPTOR);
                            KeyChainSnapshot _result10 = getKeyChainSnapshot();
                            reply.writeNoException();
                            if (_result10 != null) {
                                reply.writeInt(1);
                                _result10.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 30:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg029 = data.readString();
                            String _result11 = generateKey(_arg029);
                            reply.writeNoException();
                            reply.writeString(_result11);
                            return true;
                        case 31:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg030 = data.readString();
                            byte[] _arg117 = data.createByteArray();
                            String _result12 = generateKeyWithMetadata(_arg030, _arg117);
                            reply.writeNoException();
                            reply.writeString(_result12);
                            return true;
                        case 32:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg031 = data.readString();
                            byte[] _arg118 = data.createByteArray();
                            String _result13 = importKey(_arg031, _arg118);
                            reply.writeNoException();
                            reply.writeString(_result13);
                            return true;
                        case 33:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg032 = data.readString();
                            byte[] _arg119 = data.createByteArray();
                            byte[] _arg215 = data.createByteArray();
                            String _result14 = importKeyWithMetadata(_arg032, _arg119, _arg215);
                            reply.writeNoException();
                            reply.writeString(_result14);
                            return true;
                        case 34:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg033 = data.readString();
                            String _result15 = getKey(_arg033);
                            reply.writeNoException();
                            reply.writeString(_result15);
                            return true;
                        case 35:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg034 = data.readString();
                            removeKey(_arg034);
                            reply.writeNoException();
                            return true;
                        case 36:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg06 = PendingIntent.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg06 = null;
                            }
                            setSnapshotCreatedPendingIntent(_arg06);
                            reply.writeNoException();
                            return true;
                        case 37:
                            data.enforceInterface(DESCRIPTOR);
                            byte[] _arg035 = data.createByteArray();
                            setServerParams(_arg035);
                            reply.writeNoException();
                            return true;
                        case 38:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg036 = data.readString();
                            int _arg120 = data.readInt();
                            setRecoveryStatus(_arg036, _arg120);
                            reply.writeNoException();
                            return true;
                        case 39:
                            data.enforceInterface(DESCRIPTOR);
                            Map _result16 = getRecoveryStatus();
                            reply.writeNoException();
                            reply.writeMap(_result16);
                            return true;
                        case 40:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _arg037 = data.createIntArray();
                            setRecoverySecretTypes(_arg037);
                            reply.writeNoException();
                            return true;
                        case 41:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _result17 = getRecoverySecretTypes();
                            reply.writeNoException();
                            reply.writeIntArray(_result17);
                            return true;
                        case 42:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg038 = data.readString();
                            String _arg121 = data.readString();
                            if (data.readInt() != 0) {
                                _arg22 = RecoveryCertPath.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg22 = null;
                            }
                            byte[] _arg3 = data.createByteArray();
                            byte[] _arg4 = data.createByteArray();
                            List<KeyChainProtectionParams> _arg5 = data.createTypedArrayList(KeyChainProtectionParams.CREATOR);
                            byte[] _result18 = startRecoverySessionWithCertPath(_arg038, _arg121, _arg22, _arg3, _arg4, _arg5);
                            reply.writeNoException();
                            reply.writeByteArray(_result18);
                            return true;
                        case 43:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg039 = data.readString();
                            byte[] _arg122 = data.createByteArray();
                            List<WrappedApplicationKey> _arg216 = data.createTypedArrayList(WrappedApplicationKey.CREATOR);
                            Map _result19 = recoverKeyChainSnapshot(_arg039, _arg122, _arg216);
                            reply.writeNoException();
                            reply.writeMap(_result19);
                            return true;
                        case 44:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg040 = data.readString();
                            closeSession(_arg040);
                            reply.writeNoException();
                            return true;
                        case 45:
                            data.enforceInterface(DESCRIPTOR);
                            boolean hasSecureLockScreen = hasSecureLockScreen();
                            reply.writeNoException();
                            reply.writeInt(hasSecureLockScreen ? 1 : 0);
                            return true;
                        case 46:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg041 = data.readInt();
                            boolean tryUnlockWithCachedUnifiedChallenge = tryUnlockWithCachedUnifiedChallenge(_arg041);
                            reply.writeNoException();
                            reply.writeInt(tryUnlockWithCachedUnifiedChallenge ? 1 : 0);
                            return true;
                        case 47:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg042 = data.readInt();
                            removeCachedUnifiedChallenge(_arg042);
                            reply.writeNoException();
                            return true;
                        case 48:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg043 = data.readInt();
                            byte[] _arg123 = data.createByteArray();
                            updateEncryptionPassword(_arg043, _arg123);
                            reply.writeNoException();
                            return true;
                        case 49:
                            data.enforceInterface(DESCRIPTOR);
                            sanitizePassword();
                            reply.writeNoException();
                            return true;
                        case 50:
                            data.enforceInterface(DESCRIPTOR);
                            String _result20 = getPassword();
                            reply.writeNoException();
                            reply.writeString(_result20);
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes4.dex */
        public static class Proxy implements ILockSettings {
            public static ILockSettings sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            @Override // com.android.internal.widget.ILockSettings
            public void setBoolean(String key, boolean value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(value ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setBoolean(key, value, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void setLong(String key, long value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeLong(value);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setLong(key, value, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void setString(String key, String value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(value);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setString(key, value, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public boolean getBoolean(String key, boolean defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    boolean _result = true;
                    _data.writeInt(defaultValue ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getBoolean(key, defaultValue, userId);
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

            @Override // com.android.internal.widget.ILockSettings
            public long getLong(String key, long defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeLong(defaultValue);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getLong(key, defaultValue, userId);
                    }
                    _reply.readException();
                    long _result = _reply.readLong();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public String getString(String key, String defaultValue, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(defaultValue);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getString(key, defaultValue, userId);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public boolean setLockCredential(LockscreenCredential credential, LockscreenCredential savedCredential, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (credential != null) {
                        _data.writeInt(1);
                        credential.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (savedCredential != null) {
                        _data.writeInt(1);
                        savedCredential.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().setLockCredential(credential, savedCredential, userId);
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

            @Override // com.android.internal.widget.ILockSettings
            public void resetKeyStore(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetKeyStore(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public VerifyCredentialResponse checkCredential(LockscreenCredential credential, int userId, ICheckCredentialProgressCallback progressCallback) throws RemoteException {
                VerifyCredentialResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (credential != null) {
                        _data.writeInt(1);
                        credential.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeStrongBinder(progressCallback != null ? progressCallback.asBinder() : null);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkCredential(credential, userId, progressCallback);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VerifyCredentialResponse.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public VerifyCredentialResponse verifyCredential(LockscreenCredential credential, int userId, int flags) throws RemoteException {
                VerifyCredentialResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (credential != null) {
                        _data.writeInt(1);
                        credential.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().verifyCredential(credential, userId, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VerifyCredentialResponse.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public VerifyCredentialResponse verifyTiedProfileChallenge(LockscreenCredential credential, int userId, int flags) throws RemoteException {
                VerifyCredentialResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (credential != null) {
                        _data.writeInt(1);
                        credential.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().verifyTiedProfileChallenge(credential, userId, flags);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VerifyCredentialResponse.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public VerifyCredentialResponse verifyGatekeeperPasswordHandle(long gatekeeperPasswordHandle, long challenge, int userId) throws RemoteException {
                VerifyCredentialResponse _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(gatekeeperPasswordHandle);
                    _data.writeLong(challenge);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().verifyGatekeeperPasswordHandle(gatekeeperPasswordHandle, challenge, userId);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = VerifyCredentialResponse.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void removeGatekeeperPasswordHandle(long gatekeeperPasswordHandle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(gatekeeperPasswordHandle);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeGatekeeperPasswordHandle(gatekeeperPasswordHandle);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public boolean checkVoldPassword(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().checkVoldPassword(userId);
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

            @Override // com.android.internal.widget.ILockSettings
            public int getCredentialType(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getCredentialType(userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public byte[] getHashFactor(LockscreenCredential currentCredential, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (currentCredential != null) {
                        _data.writeInt(1);
                        currentCredential.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getHashFactor(currentCredential, userId);
                    }
                    _reply.readException();
                    byte[] _result = _reply.createByteArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void setSeparateProfileChallengeEnabled(int userId, boolean enabled, LockscreenCredential managedUserPassword) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(enabled ? 1 : 0);
                    if (managedUserPassword != null) {
                        _data.writeInt(1);
                        managedUserPassword.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSeparateProfileChallengeEnabled(userId, enabled, managedUserPassword);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public boolean getSeparateProfileChallengeEnabled(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getSeparateProfileChallengeEnabled(userId);
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

            @Override // com.android.internal.widget.ILockSettings
            public void registerStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(tracker != null ? tracker.asBinder() : null);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().registerStrongAuthTracker(tracker);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void unregisterStrongAuthTracker(IStrongAuthTracker tracker) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(tracker != null ? tracker.asBinder() : null);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unregisterStrongAuthTracker(tracker);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void requireStrongAuth(int strongAuthReason, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(strongAuthReason);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().requireStrongAuth(strongAuthReason, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void reportSuccessfulBiometricUnlock(boolean isStrongBiometric, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isStrongBiometric ? 1 : 0);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reportSuccessfulBiometricUnlock(isStrongBiometric, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void scheduleNonStrongBiometricIdleTimeout(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().scheduleNonStrongBiometricIdleTimeout(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void systemReady() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().systemReady();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void userPresent(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().userPresent(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public int getStrongAuthForUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getStrongAuthForUser(userId);
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public boolean hasPendingEscrowToken(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasPendingEscrowToken(userId);
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

            @Override // com.android.internal.widget.ILockSettings
            public void initRecoveryServiceWithSigFile(String rootCertificateAlias, byte[] recoveryServiceCertFile, byte[] recoveryServiceSigFile) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rootCertificateAlias);
                    _data.writeByteArray(recoveryServiceCertFile);
                    _data.writeByteArray(recoveryServiceSigFile);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initRecoveryServiceWithSigFile(rootCertificateAlias, recoveryServiceCertFile, recoveryServiceSigFile);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public KeyChainSnapshot getKeyChainSnapshot() throws RemoteException {
                KeyChainSnapshot _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKeyChainSnapshot();
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = KeyChainSnapshot.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public String generateKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().generateKey(alias);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public String generateKeyWithMetadata(String alias, byte[] metadata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(metadata);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().generateKeyWithMetadata(alias, metadata);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public String importKey(String alias, byte[] keyBytes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(keyBytes);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().importKey(alias, keyBytes);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public String importKeyWithMetadata(String alias, byte[] keyBytes, byte[] metadata) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeByteArray(keyBytes);
                    _data.writeByteArray(metadata);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().importKeyWithMetadata(alias, keyBytes, metadata);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public String getKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getKey(alias);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void removeKey(String alias) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeKey(alias);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void setSnapshotCreatedPendingIntent(PendingIntent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setSnapshotCreatedPendingIntent(intent);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void setServerParams(byte[] serverParams) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(serverParams);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setServerParams(serverParams);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void setRecoveryStatus(String alias, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(alias);
                    _data.writeInt(status);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRecoveryStatus(alias, status);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public Map getRecoveryStatus() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRecoveryStatus();
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void setRecoverySecretTypes(int[] secretTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(secretTypes);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setRecoverySecretTypes(secretTypes);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public int[] getRecoverySecretTypes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getRecoverySecretTypes();
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public byte[] startRecoverySessionWithCertPath(String sessionId, String rootCertificateAlias, RecoveryCertPath verifierCertPath, byte[] vaultParams, byte[] vaultChallenge, List<KeyChainProtectionParams> secrets) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(sessionId);
                        try {
                            _data.writeString(rootCertificateAlias);
                            if (verifierCertPath != null) {
                                _data.writeInt(1);
                                verifierCertPath.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeByteArray(vaultParams);
                                try {
                                    _data.writeByteArray(vaultChallenge);
                                    try {
                                        _data.writeTypedList(secrets);
                                        boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                                        if (!_status && Stub.getDefaultImpl() != null) {
                                            byte[] startRecoverySessionWithCertPath = Stub.getDefaultImpl().startRecoverySessionWithCertPath(sessionId, rootCertificateAlias, verifierCertPath, vaultParams, vaultChallenge, secrets);
                                            _reply.recycle();
                                            _data.recycle();
                                            return startRecoverySessionWithCertPath;
                                        }
                                        _reply.readException();
                                        byte[] _result = _reply.createByteArray();
                                        _reply.recycle();
                                        _data.recycle();
                                        return _result;
                                    } catch (Throwable th) {
                                        th = th;
                                        _reply.recycle();
                                        _data.recycle();
                                        throw th;
                                    }
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
                } catch (Throwable th6) {
                    th = th6;
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public Map recoverKeyChainSnapshot(String sessionId, byte[] recoveryKeyBlob, List<WrappedApplicationKey> applicationKeys) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    _data.writeByteArray(recoveryKeyBlob);
                    _data.writeTypedList(applicationKeys);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().recoverKeyChainSnapshot(sessionId, recoveryKeyBlob, applicationKeys);
                    }
                    _reply.readException();
                    ClassLoader cl = getClass().getClassLoader();
                    Map _result = _reply.readHashMap(cl);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void closeSession(String sessionId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sessionId);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().closeSession(sessionId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public boolean hasSecureLockScreen() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().hasSecureLockScreen();
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

            @Override // com.android.internal.widget.ILockSettings
            public boolean tryUnlockWithCachedUnifiedChallenge(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().tryUnlockWithCachedUnifiedChallenge(userId);
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

            @Override // com.android.internal.widget.ILockSettings
            public void removeCachedUnifiedChallenge(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().removeCachedUnifiedChallenge(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void updateEncryptionPassword(int type, byte[] password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeByteArray(password);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().updateEncryptionPassword(type, password);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public void sanitizePassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().sanitizePassword();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // com.android.internal.widget.ILockSettings
            public String getPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getPassword();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(ILockSettings impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static ILockSettings getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
