package android.os;

import android.os.IVoldListener;
import android.os.IVoldMountCallback;
import android.os.IVoldTaskListener;
import android.os.incremental.IncrementalFileSystemControlParcel;
import android.provider.Telephony;
import java.io.FileDescriptor;
/* loaded from: classes2.dex */
public interface IVold extends IInterface {
    public static final int ENCRYPTION_FLAG_NO_UI = 4;
    public static final int ENCRYPTION_STATE_ERROR_CORRUPT = -4;
    public static final int ENCRYPTION_STATE_ERROR_INCOMPLETE = -2;
    public static final int ENCRYPTION_STATE_ERROR_INCONSISTENT = -3;
    public static final int ENCRYPTION_STATE_ERROR_UNKNOWN = -1;
    public static final int ENCRYPTION_STATE_NONE = 1;
    public static final int ENCRYPTION_STATE_OK = 0;
    public static final int FSTRIM_FLAG_DEEP_TRIM = 1;
    public static final int MOUNT_FLAG_PRIMARY = 1;
    public static final int MOUNT_FLAG_VISIBLE = 2;
    public static final int PARTITION_TYPE_MIXED = 2;
    public static final int PARTITION_TYPE_PRIVATE = 1;
    public static final int PARTITION_TYPE_PUBLIC = 0;
    public static final int PASSWORD_TYPE_DEFAULT = 1;
    public static final int PASSWORD_TYPE_PASSWORD = 0;
    public static final int PASSWORD_TYPE_PATTERN = 2;
    public static final int PASSWORD_TYPE_PIN = 3;
    public static final int REMOUNT_MODE_ANDROID_WRITABLE = 4;
    public static final int REMOUNT_MODE_DEFAULT = 1;
    public static final int REMOUNT_MODE_INSTALLER = 2;
    public static final int REMOUNT_MODE_NONE = 0;
    public static final int REMOUNT_MODE_PASS_THROUGH = 3;
    public static final int STORAGE_FLAG_CE = 2;
    public static final int STORAGE_FLAG_DE = 1;
    public static final int VOLUME_STATE_BAD_REMOVAL = 8;
    public static final int VOLUME_STATE_CHECKING = 1;
    public static final int VOLUME_STATE_EJECTING = 5;
    public static final int VOLUME_STATE_FORMATTING = 4;
    public static final int VOLUME_STATE_MOUNTED = 2;
    public static final int VOLUME_STATE_MOUNTED_READ_ONLY = 3;
    public static final int VOLUME_STATE_REMOVED = 7;
    public static final int VOLUME_STATE_UNMOUNTABLE = 6;
    public static final int VOLUME_STATE_UNMOUNTED = 0;
    public static final int VOLUME_TYPE_ASEC = 3;
    public static final int VOLUME_TYPE_EMULATED = 2;
    public static final int VOLUME_TYPE_OBB = 4;
    public static final int VOLUME_TYPE_PRIVATE = 1;
    public static final int VOLUME_TYPE_PUBLIC = 0;
    public static final int VOLUME_TYPE_STUB = 5;

    void abortChanges(String str, boolean z) throws RemoteException;

    void abortFuse() throws RemoteException;

    void abortIdleMaint(IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void addAppIds(String[] strArr, int[] iArr) throws RemoteException;

    void addSandboxIds(int[] iArr, String[] strArr) throws RemoteException;

    void addUserKeyAuth(int i, int i2, String str, String str2) throws RemoteException;

    void benchmark(String str, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void bindMount(String str, String str2) throws RemoteException;

    void clearUserKeyAuth(int i, int i2, String str, String str2) throws RemoteException;

    void commitChanges() throws RemoteException;

    String createObb(String str, String str2, int i) throws RemoteException;

    String createStubVolume(String str, String str2, String str3, String str4, String str5, int i) throws RemoteException;

    void createUserKey(int i, int i2, boolean z) throws RemoteException;

    void destroyDsuMetadataKey(String str) throws RemoteException;

    void destroyObb(String str) throws RemoteException;

    void destroySandboxForApp(String str, String str2, int i) throws RemoteException;

    void destroyStubVolume(String str) throws RemoteException;

    void destroyUserKey(int i) throws RemoteException;

    void destroyUserStorage(String str, int i, int i2) throws RemoteException;

    void earlyBootEnded() throws RemoteException;

    void encryptFstab(String str, String str2, boolean z, String str3) throws RemoteException;

    void ensureAppDirsCreated(String[] strArr, int i) throws RemoteException;

    void fbeEnable() throws RemoteException;

    void fdeChangePassword(int i, String str, String str2) throws RemoteException;

    void fdeCheckPassword(String str) throws RemoteException;

    void fdeClearPassword() throws RemoteException;

    int fdeComplete() throws RemoteException;

    void fdeEnable(int i, String str, int i2) throws RemoteException;

    String fdeGetField(String str) throws RemoteException;

    String fdeGetPassword() throws RemoteException;

    int fdeGetPasswordType() throws RemoteException;

    void fdeRestart() throws RemoteException;

    void fdeSetField(String str, String str2) throws RemoteException;

    void fdeVerifyPassword(String str) throws RemoteException;

    void fixateNewestUserKeyAuth(int i) throws RemoteException;

    void fixupAppDir(String str, int i) throws RemoteException;

    void forgetPartition(String str, String str2) throws RemoteException;

    void format(String str, String str2) throws RemoteException;

    void fstrim(int i, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    int[] getUnlockedUsers() throws RemoteException;

    boolean incFsEnabled() throws RemoteException;

    void initUser0() throws RemoteException;

    boolean isCheckpointing() throws RemoteException;

    boolean isConvertibleToFbe() throws RemoteException;

    void lockUserKey(int i) throws RemoteException;

    void markBootAttempt() throws RemoteException;

    void monitor() throws RemoteException;

    void mount(String str, int i, int i2, IVoldMountCallback iVoldMountCallback) throws RemoteException;

    FileDescriptor mountAppFuse(int i, int i2) throws RemoteException;

    void mountDefaultEncrypted() throws RemoteException;

    void mountFstab(String str, String str2) throws RemoteException;

    IncrementalFileSystemControlParcel mountIncFs(String str, String str2, int i, String str3) throws RemoteException;

    void moveStorage(String str, String str2, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    boolean needsCheckpoint() throws RemoteException;

    boolean needsRollback() throws RemoteException;

    void onSecureKeyguardStateChanged(boolean z) throws RemoteException;

    void onUserAdded(int i, int i2) throws RemoteException;

    void onUserRemoved(int i) throws RemoteException;

    void onUserStarted(int i) throws RemoteException;

    void onUserStopped(int i) throws RemoteException;

    FileDescriptor openAppFuseFile(int i, int i2, int i3, int i4) throws RemoteException;

    void partition(String str, int i, int i2) throws RemoteException;

    void prepareCheckpoint() throws RemoteException;

    void prepareSandboxForApp(String str, int i, String str2, int i2) throws RemoteException;

    void prepareUserStorage(String str, int i, int i2, int i3) throws RemoteException;

    void remountAppStorageDirs(int i, int i2, String[] strArr) throws RemoteException;

    void remountUid(int i, int i2) throws RemoteException;

    void reset() throws RemoteException;

    void resetCheckpoint() throws RemoteException;

    void restoreCheckpoint(String str) throws RemoteException;

    void restoreCheckpointPart(String str, int i) throws RemoteException;

    void runIdleMaint(IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void setIncFsMountOptions(IncrementalFileSystemControlParcel incrementalFileSystemControlParcel, boolean z, boolean z2, String str) throws RemoteException;

    void setListener(IVoldListener iVoldListener) throws RemoteException;

    void setStorageBindingSeed(byte[] bArr) throws RemoteException;

    void setupAppDir(String str, int i) throws RemoteException;

    void shutdown() throws RemoteException;

    void startCheckpoint(int i) throws RemoteException;

    boolean supportsBlockCheckpoint() throws RemoteException;

    boolean supportsCheckpoint() throws RemoteException;

    boolean supportsFileCheckpoint() throws RemoteException;

    void unlockUserKey(int i, int i2, String str, String str2) throws RemoteException;

    void unmount(String str) throws RemoteException;

    void unmountAppFuse(int i, int i2) throws RemoteException;

    void unmountAppStorageDirs(int i, int i2, String[] strArr) throws RemoteException;

    void unmountIncFs(String str) throws RemoteException;

    /* loaded from: classes2.dex */
    public static class Default implements IVold {
        @Override // android.os.IVold
        public void setListener(IVoldListener listener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void abortFuse() throws RemoteException {
        }

        @Override // android.os.IVold
        public void monitor() throws RemoteException {
        }

        @Override // android.os.IVold
        public void reset() throws RemoteException {
        }

        @Override // android.os.IVold
        public void shutdown() throws RemoteException {
        }

        @Override // android.os.IVold
        public void onUserAdded(int userId, int userSerial) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onUserRemoved(int userId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onUserStarted(int userId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onUserStopped(int userId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void addAppIds(String[] packageNames, int[] appIds) throws RemoteException {
        }

        @Override // android.os.IVold
        public void addSandboxIds(int[] appIds, String[] sandboxIds) throws RemoteException {
        }

        @Override // android.os.IVold
        public void onSecureKeyguardStateChanged(boolean isShowing) throws RemoteException {
        }

        @Override // android.os.IVold
        public void partition(String diskId, int partitionType, int ratio) throws RemoteException {
        }

        @Override // android.os.IVold
        public void forgetPartition(String partGuid, String fsUuid) throws RemoteException {
        }

        @Override // android.os.IVold
        public void mount(String volId, int mountFlags, int mountUserId, IVoldMountCallback callback) throws RemoteException {
        }

        @Override // android.os.IVold
        public void unmount(String volId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void format(String volId, String fsType) throws RemoteException {
        }

        @Override // android.os.IVold
        public void benchmark(String volId, IVoldTaskListener listener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void moveStorage(String fromVolId, String toVolId, IVoldTaskListener listener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void remountUid(int uid, int remountMode) throws RemoteException {
        }

        @Override // android.os.IVold
        public void remountAppStorageDirs(int uid, int pid, String[] packageNames) throws RemoteException {
        }

        @Override // android.os.IVold
        public void unmountAppStorageDirs(int uid, int pid, String[] packageNames) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setupAppDir(String path, int appUid) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fixupAppDir(String path, int appUid) throws RemoteException {
        }

        @Override // android.os.IVold
        public void ensureAppDirsCreated(String[] paths, int appUid) throws RemoteException {
        }

        @Override // android.os.IVold
        public String createObb(String sourcePath, String sourceKey, int ownerGid) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void destroyObb(String volId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fstrim(int fstrimFlags, IVoldTaskListener listener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void runIdleMaint(IVoldTaskListener listener) throws RemoteException {
        }

        @Override // android.os.IVold
        public void abortIdleMaint(IVoldTaskListener listener) throws RemoteException {
        }

        @Override // android.os.IVold
        public FileDescriptor mountAppFuse(int uid, int mountId) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void unmountAppFuse(int uid, int mountId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fdeCheckPassword(String password) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fdeRestart() throws RemoteException {
        }

        @Override // android.os.IVold
        public int fdeComplete() throws RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public void fdeEnable(int passwordType, String password, int encryptionFlags) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fdeChangePassword(int passwordType, String currentPassword, String password) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fdeVerifyPassword(String password) throws RemoteException {
        }

        @Override // android.os.IVold
        public String fdeGetField(String key) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void fdeSetField(String key, String value) throws RemoteException {
        }

        @Override // android.os.IVold
        public int fdeGetPasswordType() throws RemoteException {
            return 0;
        }

        @Override // android.os.IVold
        public String fdeGetPassword() throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void fdeClearPassword() throws RemoteException {
        }

        @Override // android.os.IVold
        public void fbeEnable() throws RemoteException {
        }

        @Override // android.os.IVold
        public void mountDefaultEncrypted() throws RemoteException {
        }

        @Override // android.os.IVold
        public void initUser0() throws RemoteException {
        }

        @Override // android.os.IVold
        public boolean isConvertibleToFbe() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void mountFstab(String blkDevice, String mountPoint) throws RemoteException {
        }

        @Override // android.os.IVold
        public void encryptFstab(String blkDevice, String mountPoint, boolean shouldFormat, String fsType) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setStorageBindingSeed(byte[] seed) throws RemoteException {
        }

        @Override // android.os.IVold
        public void createUserKey(int userId, int userSerial, boolean ephemeral) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroyUserKey(int userId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void addUserKeyAuth(int userId, int userSerial, String token, String secret) throws RemoteException {
        }

        @Override // android.os.IVold
        public void clearUserKeyAuth(int userId, int userSerial, String token, String secret) throws RemoteException {
        }

        @Override // android.os.IVold
        public void fixateNewestUserKeyAuth(int userId) throws RemoteException {
        }

        @Override // android.os.IVold
        public int[] getUnlockedUsers() throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void unlockUserKey(int userId, int userSerial, String token, String secret) throws RemoteException {
        }

        @Override // android.os.IVold
        public void lockUserKey(int userId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void prepareUserStorage(String uuid, int userId, int userSerial, int storageFlags) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroyUserStorage(String uuid, int userId, int storageFlags) throws RemoteException {
        }

        @Override // android.os.IVold
        public void prepareSandboxForApp(String packageName, int appId, String sandboxId, int userId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroySandboxForApp(String packageName, String sandboxId, int userId) throws RemoteException {
        }

        @Override // android.os.IVold
        public void startCheckpoint(int retry) throws RemoteException {
        }

        @Override // android.os.IVold
        public boolean needsCheckpoint() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean needsRollback() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean isCheckpointing() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void abortChanges(String device, boolean retry) throws RemoteException {
        }

        @Override // android.os.IVold
        public void commitChanges() throws RemoteException {
        }

        @Override // android.os.IVold
        public void prepareCheckpoint() throws RemoteException {
        }

        @Override // android.os.IVold
        public void restoreCheckpoint(String device) throws RemoteException {
        }

        @Override // android.os.IVold
        public void restoreCheckpointPart(String device, int count) throws RemoteException {
        }

        @Override // android.os.IVold
        public void markBootAttempt() throws RemoteException {
        }

        @Override // android.os.IVold
        public boolean supportsCheckpoint() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean supportsBlockCheckpoint() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public boolean supportsFileCheckpoint() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public void resetCheckpoint() throws RemoteException {
        }

        @Override // android.os.IVold
        public void earlyBootEnded() throws RemoteException {
        }

        @Override // android.os.IVold
        public String createStubVolume(String sourcePath, String mountPath, String fsType, String fsUuid, String fsLabel, int flags) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void destroyStubVolume(String volId) throws RemoteException {
        }

        @Override // android.os.IVold
        public FileDescriptor openAppFuseFile(int uid, int mountId, int fileId, int flags) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public boolean incFsEnabled() throws RemoteException {
            return false;
        }

        @Override // android.os.IVold
        public IncrementalFileSystemControlParcel mountIncFs(String backingPath, String targetDir, int flags, String sysfsName) throws RemoteException {
            return null;
        }

        @Override // android.os.IVold
        public void unmountIncFs(String dir) throws RemoteException {
        }

        @Override // android.os.IVold
        public void setIncFsMountOptions(IncrementalFileSystemControlParcel control, boolean enableReadLogs, boolean enableReadTimeouts, String sysfsName) throws RemoteException {
        }

        @Override // android.os.IVold
        public void bindMount(String sourceDir, String targetDir) throws RemoteException {
        }

        @Override // android.os.IVold
        public void destroyDsuMetadataKey(String dsuSlot) throws RemoteException {
        }

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class Stub extends Binder implements IVold {
        public static final String DESCRIPTOR = "android.os.IVold";
        static final int TRANSACTION_abortChanges = 67;
        static final int TRANSACTION_abortFuse = 2;
        static final int TRANSACTION_abortIdleMaint = 30;
        static final int TRANSACTION_addAppIds = 10;
        static final int TRANSACTION_addSandboxIds = 11;
        static final int TRANSACTION_addUserKeyAuth = 53;
        static final int TRANSACTION_benchmark = 18;
        static final int TRANSACTION_bindMount = 85;
        static final int TRANSACTION_clearUserKeyAuth = 54;
        static final int TRANSACTION_commitChanges = 68;
        static final int TRANSACTION_createObb = 26;
        static final int TRANSACTION_createStubVolume = 78;
        static final int TRANSACTION_createUserKey = 51;
        static final int TRANSACTION_destroyDsuMetadataKey = 86;
        static final int TRANSACTION_destroyObb = 27;
        static final int TRANSACTION_destroySandboxForApp = 62;
        static final int TRANSACTION_destroyStubVolume = 79;
        static final int TRANSACTION_destroyUserKey = 52;
        static final int TRANSACTION_destroyUserStorage = 60;
        static final int TRANSACTION_earlyBootEnded = 77;
        static final int TRANSACTION_encryptFstab = 49;
        static final int TRANSACTION_ensureAppDirsCreated = 25;
        static final int TRANSACTION_fbeEnable = 44;
        static final int TRANSACTION_fdeChangePassword = 37;
        static final int TRANSACTION_fdeCheckPassword = 33;
        static final int TRANSACTION_fdeClearPassword = 43;
        static final int TRANSACTION_fdeComplete = 35;
        static final int TRANSACTION_fdeEnable = 36;
        static final int TRANSACTION_fdeGetField = 39;
        static final int TRANSACTION_fdeGetPassword = 42;
        static final int TRANSACTION_fdeGetPasswordType = 41;
        static final int TRANSACTION_fdeRestart = 34;
        static final int TRANSACTION_fdeSetField = 40;
        static final int TRANSACTION_fdeVerifyPassword = 38;
        static final int TRANSACTION_fixateNewestUserKeyAuth = 55;
        static final int TRANSACTION_fixupAppDir = 24;
        static final int TRANSACTION_forgetPartition = 14;
        static final int TRANSACTION_format = 17;
        static final int TRANSACTION_fstrim = 28;
        static final int TRANSACTION_getUnlockedUsers = 56;
        static final int TRANSACTION_incFsEnabled = 81;
        static final int TRANSACTION_initUser0 = 46;
        static final int TRANSACTION_isCheckpointing = 66;
        static final int TRANSACTION_isConvertibleToFbe = 47;
        static final int TRANSACTION_lockUserKey = 58;
        static final int TRANSACTION_markBootAttempt = 72;
        static final int TRANSACTION_monitor = 3;
        static final int TRANSACTION_mount = 15;
        static final int TRANSACTION_mountAppFuse = 31;
        static final int TRANSACTION_mountDefaultEncrypted = 45;
        static final int TRANSACTION_mountFstab = 48;
        static final int TRANSACTION_mountIncFs = 82;
        static final int TRANSACTION_moveStorage = 19;
        static final int TRANSACTION_needsCheckpoint = 64;
        static final int TRANSACTION_needsRollback = 65;
        static final int TRANSACTION_onSecureKeyguardStateChanged = 12;
        static final int TRANSACTION_onUserAdded = 6;
        static final int TRANSACTION_onUserRemoved = 7;
        static final int TRANSACTION_onUserStarted = 8;
        static final int TRANSACTION_onUserStopped = 9;
        static final int TRANSACTION_openAppFuseFile = 80;
        static final int TRANSACTION_partition = 13;
        static final int TRANSACTION_prepareCheckpoint = 69;
        static final int TRANSACTION_prepareSandboxForApp = 61;
        static final int TRANSACTION_prepareUserStorage = 59;
        static final int TRANSACTION_remountAppStorageDirs = 21;
        static final int TRANSACTION_remountUid = 20;
        static final int TRANSACTION_reset = 4;
        static final int TRANSACTION_resetCheckpoint = 76;
        static final int TRANSACTION_restoreCheckpoint = 70;
        static final int TRANSACTION_restoreCheckpointPart = 71;
        static final int TRANSACTION_runIdleMaint = 29;
        static final int TRANSACTION_setIncFsMountOptions = 84;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_setStorageBindingSeed = 50;
        static final int TRANSACTION_setupAppDir = 23;
        static final int TRANSACTION_shutdown = 5;
        static final int TRANSACTION_startCheckpoint = 63;
        static final int TRANSACTION_supportsBlockCheckpoint = 74;
        static final int TRANSACTION_supportsCheckpoint = 73;
        static final int TRANSACTION_supportsFileCheckpoint = 75;
        static final int TRANSACTION_unlockUserKey = 57;
        static final int TRANSACTION_unmount = 16;
        static final int TRANSACTION_unmountAppFuse = 32;
        static final int TRANSACTION_unmountAppStorageDirs = 22;
        static final int TRANSACTION_unmountIncFs = 83;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVold asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin != null && (iin instanceof IVold)) {
                return (IVold) iin;
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
                    return "setListener";
                case 2:
                    return "abortFuse";
                case 3:
                    return "monitor";
                case 4:
                    return "reset";
                case 5:
                    return "shutdown";
                case 6:
                    return "onUserAdded";
                case 7:
                    return "onUserRemoved";
                case 8:
                    return "onUserStarted";
                case 9:
                    return "onUserStopped";
                case 10:
                    return "addAppIds";
                case 11:
                    return "addSandboxIds";
                case 12:
                    return "onSecureKeyguardStateChanged";
                case 13:
                    return "partition";
                case 14:
                    return "forgetPartition";
                case 15:
                    return "mount";
                case 16:
                    return "unmount";
                case 17:
                    return Telephony.CellBroadcasts.MESSAGE_FORMAT;
                case 18:
                    return "benchmark";
                case 19:
                    return "moveStorage";
                case 20:
                    return "remountUid";
                case 21:
                    return "remountAppStorageDirs";
                case 22:
                    return "unmountAppStorageDirs";
                case 23:
                    return "setupAppDir";
                case 24:
                    return "fixupAppDir";
                case 25:
                    return "ensureAppDirsCreated";
                case 26:
                    return "createObb";
                case 27:
                    return "destroyObb";
                case 28:
                    return "fstrim";
                case 29:
                    return "runIdleMaint";
                case 30:
                    return "abortIdleMaint";
                case 31:
                    return "mountAppFuse";
                case 32:
                    return "unmountAppFuse";
                case 33:
                    return "fdeCheckPassword";
                case 34:
                    return "fdeRestart";
                case 35:
                    return "fdeComplete";
                case 36:
                    return "fdeEnable";
                case 37:
                    return "fdeChangePassword";
                case 38:
                    return "fdeVerifyPassword";
                case 39:
                    return "fdeGetField";
                case 40:
                    return "fdeSetField";
                case 41:
                    return "fdeGetPasswordType";
                case 42:
                    return "fdeGetPassword";
                case 43:
                    return "fdeClearPassword";
                case 44:
                    return "fbeEnable";
                case 45:
                    return "mountDefaultEncrypted";
                case 46:
                    return "initUser0";
                case 47:
                    return "isConvertibleToFbe";
                case 48:
                    return "mountFstab";
                case 49:
                    return "encryptFstab";
                case 50:
                    return "setStorageBindingSeed";
                case 51:
                    return "createUserKey";
                case 52:
                    return "destroyUserKey";
                case 53:
                    return "addUserKeyAuth";
                case 54:
                    return "clearUserKeyAuth";
                case 55:
                    return "fixateNewestUserKeyAuth";
                case 56:
                    return "getUnlockedUsers";
                case 57:
                    return "unlockUserKey";
                case 58:
                    return "lockUserKey";
                case 59:
                    return "prepareUserStorage";
                case 60:
                    return "destroyUserStorage";
                case 61:
                    return "prepareSandboxForApp";
                case 62:
                    return "destroySandboxForApp";
                case 63:
                    return "startCheckpoint";
                case 64:
                    return "needsCheckpoint";
                case 65:
                    return "needsRollback";
                case 66:
                    return "isCheckpointing";
                case 67:
                    return "abortChanges";
                case 68:
                    return "commitChanges";
                case 69:
                    return "prepareCheckpoint";
                case 70:
                    return "restoreCheckpoint";
                case 71:
                    return "restoreCheckpointPart";
                case 72:
                    return "markBootAttempt";
                case 73:
                    return "supportsCheckpoint";
                case 74:
                    return "supportsBlockCheckpoint";
                case 75:
                    return "supportsFileCheckpoint";
                case 76:
                    return "resetCheckpoint";
                case 77:
                    return "earlyBootEnded";
                case 78:
                    return "createStubVolume";
                case 79:
                    return "destroyStubVolume";
                case 80:
                    return "openAppFuseFile";
                case 81:
                    return "incFsEnabled";
                case 82:
                    return "mountIncFs";
                case 83:
                    return "unmountIncFs";
                case 84:
                    return "setIncFsMountOptions";
                case 85:
                    return "bindMount";
                case 86:
                    return "destroyDsuMetadataKey";
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
            IncrementalFileSystemControlParcel _arg0;
            boolean _arg1;
            switch (code) {
                case IBinder.INTERFACE_TRANSACTION /* 1598968902 */:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    boolean _arg2 = false;
                    switch (code) {
                        case 1:
                            data.enforceInterface(DESCRIPTOR);
                            IVoldListener _arg02 = IVoldListener.Stub.asInterface(data.readStrongBinder());
                            setListener(_arg02);
                            reply.writeNoException();
                            return true;
                        case 2:
                            data.enforceInterface(DESCRIPTOR);
                            abortFuse();
                            reply.writeNoException();
                            return true;
                        case 3:
                            data.enforceInterface(DESCRIPTOR);
                            monitor();
                            reply.writeNoException();
                            return true;
                        case 4:
                            data.enforceInterface(DESCRIPTOR);
                            reset();
                            reply.writeNoException();
                            return true;
                        case 5:
                            data.enforceInterface(DESCRIPTOR);
                            shutdown();
                            reply.writeNoException();
                            return true;
                        case 6:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg03 = data.readInt();
                            int _arg12 = data.readInt();
                            onUserAdded(_arg03, _arg12);
                            reply.writeNoException();
                            return true;
                        case 7:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg04 = data.readInt();
                            onUserRemoved(_arg04);
                            reply.writeNoException();
                            return true;
                        case 8:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg05 = data.readInt();
                            onUserStarted(_arg05);
                            reply.writeNoException();
                            return true;
                        case 9:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg06 = data.readInt();
                            onUserStopped(_arg06);
                            reply.writeNoException();
                            return true;
                        case 10:
                            data.enforceInterface(DESCRIPTOR);
                            String[] _arg07 = data.createStringArray();
                            int[] _arg13 = data.createIntArray();
                            addAppIds(_arg07, _arg13);
                            reply.writeNoException();
                            return true;
                        case 11:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _arg08 = data.createIntArray();
                            String[] _arg14 = data.createStringArray();
                            addSandboxIds(_arg08, _arg14);
                            reply.writeNoException();
                            return true;
                        case 12:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg2 = true;
                            }
                            onSecureKeyguardStateChanged(_arg2);
                            reply.writeNoException();
                            return true;
                        case 13:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg09 = data.readString();
                            int _arg15 = data.readInt();
                            partition(_arg09, _arg15, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 14:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg010 = data.readString();
                            String _arg16 = data.readString();
                            forgetPartition(_arg010, _arg16);
                            reply.writeNoException();
                            return true;
                        case 15:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg011 = data.readString();
                            int _arg17 = data.readInt();
                            int _arg22 = data.readInt();
                            IVoldMountCallback _arg3 = IVoldMountCallback.Stub.asInterface(data.readStrongBinder());
                            mount(_arg011, _arg17, _arg22, _arg3);
                            reply.writeNoException();
                            return true;
                        case 16:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg012 = data.readString();
                            unmount(_arg012);
                            reply.writeNoException();
                            return true;
                        case 17:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg013 = data.readString();
                            String _arg18 = data.readString();
                            format(_arg013, _arg18);
                            reply.writeNoException();
                            return true;
                        case 18:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg014 = data.readString();
                            IVoldTaskListener _arg19 = IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                            benchmark(_arg014, _arg19);
                            reply.writeNoException();
                            return true;
                        case 19:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg015 = data.readString();
                            String _arg110 = data.readString();
                            moveStorage(_arg015, _arg110, IVoldTaskListener.Stub.asInterface(data.readStrongBinder()));
                            reply.writeNoException();
                            return true;
                        case 20:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg016 = data.readInt();
                            int _arg111 = data.readInt();
                            remountUid(_arg016, _arg111);
                            reply.writeNoException();
                            return true;
                        case 21:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg017 = data.readInt();
                            int _arg112 = data.readInt();
                            remountAppStorageDirs(_arg017, _arg112, data.createStringArray());
                            reply.writeNoException();
                            return true;
                        case 22:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg018 = data.readInt();
                            int _arg113 = data.readInt();
                            unmountAppStorageDirs(_arg018, _arg113, data.createStringArray());
                            reply.writeNoException();
                            return true;
                        case 23:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg019 = data.readString();
                            int _arg114 = data.readInt();
                            setupAppDir(_arg019, _arg114);
                            reply.writeNoException();
                            return true;
                        case 24:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg020 = data.readString();
                            int _arg115 = data.readInt();
                            fixupAppDir(_arg020, _arg115);
                            reply.writeNoException();
                            return true;
                        case 25:
                            data.enforceInterface(DESCRIPTOR);
                            String[] _arg021 = data.createStringArray();
                            int _arg116 = data.readInt();
                            ensureAppDirsCreated(_arg021, _arg116);
                            reply.writeNoException();
                            return true;
                        case 26:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg022 = data.readString();
                            String _arg117 = data.readString();
                            String _result = createObb(_arg022, _arg117, data.readInt());
                            reply.writeNoException();
                            reply.writeString(_result);
                            return true;
                        case 27:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg023 = data.readString();
                            destroyObb(_arg023);
                            reply.writeNoException();
                            return true;
                        case 28:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg024 = data.readInt();
                            IVoldTaskListener _arg118 = IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                            fstrim(_arg024, _arg118);
                            reply.writeNoException();
                            return true;
                        case 29:
                            data.enforceInterface(DESCRIPTOR);
                            IVoldTaskListener _arg025 = IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                            runIdleMaint(_arg025);
                            reply.writeNoException();
                            return true;
                        case 30:
                            data.enforceInterface(DESCRIPTOR);
                            IVoldTaskListener _arg026 = IVoldTaskListener.Stub.asInterface(data.readStrongBinder());
                            abortIdleMaint(_arg026);
                            reply.writeNoException();
                            return true;
                        case 31:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg027 = data.readInt();
                            int _arg119 = data.readInt();
                            FileDescriptor _result2 = mountAppFuse(_arg027, _arg119);
                            reply.writeNoException();
                            reply.writeRawFileDescriptor(_result2);
                            return true;
                        case 32:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg028 = data.readInt();
                            int _arg120 = data.readInt();
                            unmountAppFuse(_arg028, _arg120);
                            reply.writeNoException();
                            return true;
                        case 33:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg029 = data.readString();
                            fdeCheckPassword(_arg029);
                            reply.writeNoException();
                            return true;
                        case 34:
                            data.enforceInterface(DESCRIPTOR);
                            fdeRestart();
                            reply.writeNoException();
                            return true;
                        case 35:
                            data.enforceInterface(DESCRIPTOR);
                            int _result3 = fdeComplete();
                            reply.writeNoException();
                            reply.writeInt(_result3);
                            return true;
                        case 36:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg030 = data.readInt();
                            String _arg121 = data.readString();
                            fdeEnable(_arg030, _arg121, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 37:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg031 = data.readInt();
                            String _arg122 = data.readString();
                            fdeChangePassword(_arg031, _arg122, data.readString());
                            reply.writeNoException();
                            return true;
                        case 38:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg032 = data.readString();
                            fdeVerifyPassword(_arg032);
                            reply.writeNoException();
                            return true;
                        case 39:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg033 = data.readString();
                            String _result4 = fdeGetField(_arg033);
                            reply.writeNoException();
                            reply.writeString(_result4);
                            return true;
                        case 40:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg034 = data.readString();
                            String _arg123 = data.readString();
                            fdeSetField(_arg034, _arg123);
                            reply.writeNoException();
                            return true;
                        case 41:
                            data.enforceInterface(DESCRIPTOR);
                            int _result5 = fdeGetPasswordType();
                            reply.writeNoException();
                            reply.writeInt(_result5);
                            return true;
                        case 42:
                            data.enforceInterface(DESCRIPTOR);
                            String _result6 = fdeGetPassword();
                            reply.writeNoException();
                            reply.writeString(_result6);
                            return true;
                        case 43:
                            data.enforceInterface(DESCRIPTOR);
                            fdeClearPassword();
                            reply.writeNoException();
                            return true;
                        case 44:
                            data.enforceInterface(DESCRIPTOR);
                            fbeEnable();
                            reply.writeNoException();
                            return true;
                        case 45:
                            data.enforceInterface(DESCRIPTOR);
                            mountDefaultEncrypted();
                            reply.writeNoException();
                            return true;
                        case 46:
                            data.enforceInterface(DESCRIPTOR);
                            initUser0();
                            reply.writeNoException();
                            return true;
                        case 47:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isConvertibleToFbe = isConvertibleToFbe();
                            reply.writeNoException();
                            reply.writeInt(isConvertibleToFbe ? 1 : 0);
                            return true;
                        case 48:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg035 = data.readString();
                            String _arg124 = data.readString();
                            mountFstab(_arg035, _arg124);
                            reply.writeNoException();
                            return true;
                        case 49:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg036 = data.readString();
                            String _arg125 = data.readString();
                            if (data.readInt() != 0) {
                                _arg2 = true;
                            }
                            String _arg32 = data.readString();
                            encryptFstab(_arg036, _arg125, _arg2, _arg32);
                            reply.writeNoException();
                            return true;
                        case 50:
                            data.enforceInterface(DESCRIPTOR);
                            byte[] _arg037 = data.createByteArray();
                            setStorageBindingSeed(_arg037);
                            reply.writeNoException();
                            return true;
                        case 51:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg038 = data.readInt();
                            int _arg126 = data.readInt();
                            if (data.readInt() != 0) {
                                _arg2 = true;
                            }
                            createUserKey(_arg038, _arg126, _arg2);
                            reply.writeNoException();
                            return true;
                        case 52:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg039 = data.readInt();
                            destroyUserKey(_arg039);
                            reply.writeNoException();
                            return true;
                        case 53:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg040 = data.readInt();
                            int _arg127 = data.readInt();
                            String _arg23 = data.readString();
                            String _arg33 = data.readString();
                            addUserKeyAuth(_arg040, _arg127, _arg23, _arg33);
                            reply.writeNoException();
                            return true;
                        case 54:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg041 = data.readInt();
                            int _arg128 = data.readInt();
                            String _arg24 = data.readString();
                            String _arg34 = data.readString();
                            clearUserKeyAuth(_arg041, _arg128, _arg24, _arg34);
                            reply.writeNoException();
                            return true;
                        case 55:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg042 = data.readInt();
                            fixateNewestUserKeyAuth(_arg042);
                            reply.writeNoException();
                            return true;
                        case 56:
                            data.enforceInterface(DESCRIPTOR);
                            int[] _result7 = getUnlockedUsers();
                            reply.writeNoException();
                            reply.writeIntArray(_result7);
                            return true;
                        case 57:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg043 = data.readInt();
                            int _arg129 = data.readInt();
                            String _arg25 = data.readString();
                            String _arg35 = data.readString();
                            unlockUserKey(_arg043, _arg129, _arg25, _arg35);
                            reply.writeNoException();
                            return true;
                        case 58:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg044 = data.readInt();
                            lockUserKey(_arg044);
                            reply.writeNoException();
                            return true;
                        case 59:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg045 = data.readString();
                            int _arg130 = data.readInt();
                            int _arg26 = data.readInt();
                            int _arg36 = data.readInt();
                            prepareUserStorage(_arg045, _arg130, _arg26, _arg36);
                            reply.writeNoException();
                            return true;
                        case 60:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg046 = data.readString();
                            int _arg131 = data.readInt();
                            destroyUserStorage(_arg046, _arg131, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 61:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg047 = data.readString();
                            int _arg132 = data.readInt();
                            String _arg27 = data.readString();
                            int _arg37 = data.readInt();
                            prepareSandboxForApp(_arg047, _arg132, _arg27, _arg37);
                            reply.writeNoException();
                            return true;
                        case 62:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg048 = data.readString();
                            String _arg133 = data.readString();
                            destroySandboxForApp(_arg048, _arg133, data.readInt());
                            reply.writeNoException();
                            return true;
                        case 63:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg049 = data.readInt();
                            startCheckpoint(_arg049);
                            reply.writeNoException();
                            return true;
                        case 64:
                            data.enforceInterface(DESCRIPTOR);
                            boolean needsCheckpoint = needsCheckpoint();
                            reply.writeNoException();
                            reply.writeInt(needsCheckpoint ? 1 : 0);
                            return true;
                        case 65:
                            data.enforceInterface(DESCRIPTOR);
                            boolean needsRollback = needsRollback();
                            reply.writeNoException();
                            reply.writeInt(needsRollback ? 1 : 0);
                            return true;
                        case 66:
                            data.enforceInterface(DESCRIPTOR);
                            boolean isCheckpointing = isCheckpointing();
                            reply.writeNoException();
                            reply.writeInt(isCheckpointing ? 1 : 0);
                            return true;
                        case 67:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg050 = data.readString();
                            if (data.readInt() != 0) {
                                _arg2 = true;
                            }
                            abortChanges(_arg050, _arg2);
                            reply.writeNoException();
                            return true;
                        case 68:
                            data.enforceInterface(DESCRIPTOR);
                            commitChanges();
                            reply.writeNoException();
                            return true;
                        case 69:
                            data.enforceInterface(DESCRIPTOR);
                            prepareCheckpoint();
                            reply.writeNoException();
                            return true;
                        case 70:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg051 = data.readString();
                            restoreCheckpoint(_arg051);
                            reply.writeNoException();
                            return true;
                        case 71:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg052 = data.readString();
                            int _arg134 = data.readInt();
                            restoreCheckpointPart(_arg052, _arg134);
                            reply.writeNoException();
                            return true;
                        case 72:
                            data.enforceInterface(DESCRIPTOR);
                            markBootAttempt();
                            reply.writeNoException();
                            return true;
                        case 73:
                            data.enforceInterface(DESCRIPTOR);
                            boolean supportsCheckpoint = supportsCheckpoint();
                            reply.writeNoException();
                            reply.writeInt(supportsCheckpoint ? 1 : 0);
                            return true;
                        case 74:
                            data.enforceInterface(DESCRIPTOR);
                            boolean supportsBlockCheckpoint = supportsBlockCheckpoint();
                            reply.writeNoException();
                            reply.writeInt(supportsBlockCheckpoint ? 1 : 0);
                            return true;
                        case 75:
                            data.enforceInterface(DESCRIPTOR);
                            boolean supportsFileCheckpoint = supportsFileCheckpoint();
                            reply.writeNoException();
                            reply.writeInt(supportsFileCheckpoint ? 1 : 0);
                            return true;
                        case 76:
                            data.enforceInterface(DESCRIPTOR);
                            resetCheckpoint();
                            reply.writeNoException();
                            return true;
                        case 77:
                            data.enforceInterface(DESCRIPTOR);
                            earlyBootEnded();
                            reply.writeNoException();
                            return true;
                        case 78:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg053 = data.readString();
                            String _arg135 = data.readString();
                            String _arg28 = data.readString();
                            String _arg38 = data.readString();
                            String _arg4 = data.readString();
                            int _arg5 = data.readInt();
                            String _result8 = createStubVolume(_arg053, _arg135, _arg28, _arg38, _arg4, _arg5);
                            reply.writeNoException();
                            reply.writeString(_result8);
                            return true;
                        case 79:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg054 = data.readString();
                            destroyStubVolume(_arg054);
                            reply.writeNoException();
                            return true;
                        case 80:
                            data.enforceInterface(DESCRIPTOR);
                            int _arg055 = data.readInt();
                            int _arg136 = data.readInt();
                            int _arg29 = data.readInt();
                            int _arg39 = data.readInt();
                            FileDescriptor _result9 = openAppFuseFile(_arg055, _arg136, _arg29, _arg39);
                            reply.writeNoException();
                            reply.writeRawFileDescriptor(_result9);
                            return true;
                        case 81:
                            data.enforceInterface(DESCRIPTOR);
                            boolean incFsEnabled = incFsEnabled();
                            reply.writeNoException();
                            reply.writeInt(incFsEnabled ? 1 : 0);
                            return true;
                        case 82:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg056 = data.readString();
                            String _arg137 = data.readString();
                            int _arg210 = data.readInt();
                            String _arg310 = data.readString();
                            IncrementalFileSystemControlParcel _result10 = mountIncFs(_arg056, _arg137, _arg210, _arg310);
                            reply.writeNoException();
                            if (_result10 != null) {
                                reply.writeInt(1);
                                _result10.writeToParcel(reply, 1);
                            } else {
                                reply.writeInt(0);
                            }
                            return true;
                        case 83:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg057 = data.readString();
                            unmountIncFs(_arg057);
                            reply.writeNoException();
                            return true;
                        case 84:
                            data.enforceInterface(DESCRIPTOR);
                            if (data.readInt() != 0) {
                                _arg0 = IncrementalFileSystemControlParcel.CREATOR.mo3559createFromParcel(data);
                            } else {
                                _arg0 = null;
                            }
                            if (data.readInt() != 0) {
                                _arg1 = true;
                            } else {
                                _arg1 = false;
                            }
                            if (data.readInt() != 0) {
                                _arg2 = true;
                            }
                            String _arg311 = data.readString();
                            setIncFsMountOptions(_arg0, _arg1, _arg2, _arg311);
                            reply.writeNoException();
                            return true;
                        case 85:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg058 = data.readString();
                            String _arg138 = data.readString();
                            bindMount(_arg058, _arg138);
                            reply.writeNoException();
                            return true;
                        case 86:
                            data.enforceInterface(DESCRIPTOR);
                            String _arg059 = data.readString();
                            destroyDsuMetadataKey(_arg059);
                            reply.writeNoException();
                            return true;
                        default:
                            return super.onTransact(code, data, reply, flags);
                    }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes2.dex */
        public static class Proxy implements IVold {
            public static IVold sDefaultImpl;
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

            @Override // android.os.IVold
            public void setListener(IVoldListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(1, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setListener(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void abortFuse() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(2, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().abortFuse();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void monitor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(3, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().monitor();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void reset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(4, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().reset();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void shutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(5, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().shutdown();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserAdded(int userId, int userSerial) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    boolean _status = this.mRemote.transact(6, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onUserAdded(userId, userSerial);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserRemoved(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(7, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onUserRemoved(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserStarted(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(8, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onUserStarted(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onUserStopped(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(9, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onUserStopped(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void addAppIds(String[] packageNames, int[] appIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeIntArray(appIds);
                    boolean _status = this.mRemote.transact(10, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addAppIds(packageNames, appIds);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void addSandboxIds(int[] appIds, String[] sandboxIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(appIds);
                    _data.writeStringArray(sandboxIds);
                    boolean _status = this.mRemote.transact(11, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addSandboxIds(appIds, sandboxIds);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void onSecureKeyguardStateChanged(boolean isShowing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isShowing ? 1 : 0);
                    boolean _status = this.mRemote.transact(12, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().onSecureKeyguardStateChanged(isShowing);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void partition(String diskId, int partitionType, int ratio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeInt(partitionType);
                    _data.writeInt(ratio);
                    boolean _status = this.mRemote.transact(13, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().partition(diskId, partitionType, ratio);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void forgetPartition(String partGuid, String fsUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(partGuid);
                    _data.writeString(fsUuid);
                    boolean _status = this.mRemote.transact(14, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().forgetPartition(partGuid, fsUuid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void mount(String volId, int mountFlags, int mountUserId, IVoldMountCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeInt(mountFlags);
                    _data.writeInt(mountUserId);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    boolean _status = this.mRemote.transact(15, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().mount(volId, mountFlags, mountUserId, callback);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmount(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    boolean _status = this.mRemote.transact(16, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unmount(volId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void format(String volId, String fsType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(fsType);
                    boolean _status = this.mRemote.transact(17, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().format(volId, fsType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void benchmark(String volId, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(18, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().benchmark(volId, listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void moveStorage(String fromVolId, String toVolId, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fromVolId);
                    _data.writeString(toVolId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(19, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().moveStorage(fromVolId, toVolId, listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void remountUid(int uid, int remountMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(remountMode);
                    boolean _status = this.mRemote.transact(20, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().remountUid(uid, remountMode);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void remountAppStorageDirs(int uid, int pid, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeStringArray(packageNames);
                    boolean _status = this.mRemote.transact(21, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().remountAppStorageDirs(uid, pid, packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountAppStorageDirs(int uid, int pid, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeStringArray(packageNames);
                    boolean _status = this.mRemote.transact(22, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unmountAppStorageDirs(uid, pid, packageNames);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void setupAppDir(String path, int appUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(appUid);
                    boolean _status = this.mRemote.transact(23, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setupAppDir(path, appUid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fixupAppDir(String path, int appUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(appUid);
                    boolean _status = this.mRemote.transact(24, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fixupAppDir(path, appUid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void ensureAppDirsCreated(String[] paths, int appUid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(paths);
                    _data.writeInt(appUid);
                    boolean _status = this.mRemote.transact(25, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().ensureAppDirsCreated(paths, appUid);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public String createObb(String sourcePath, String sourceKey, int ownerGid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sourcePath);
                    _data.writeString(sourceKey);
                    _data.writeInt(ownerGid);
                    boolean _status = this.mRemote.transact(26, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().createObb(sourcePath, sourceKey, ownerGid);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyObb(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    boolean _status = this.mRemote.transact(27, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().destroyObb(volId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fstrim(int fstrimFlags, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fstrimFlags);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(28, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fstrim(fstrimFlags, listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void runIdleMaint(IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(29, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().runIdleMaint(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void abortIdleMaint(IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean _status = this.mRemote.transact(30, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().abortIdleMaint(listener);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public FileDescriptor mountAppFuse(int uid, int mountId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    boolean _status = this.mRemote.transact(31, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().mountAppFuse(uid, mountId);
                    }
                    _reply.readException();
                    FileDescriptor _result = _reply.readRawFileDescriptor();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountAppFuse(int uid, int mountId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    boolean _status = this.mRemote.transact(32, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unmountAppFuse(uid, mountId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fdeCheckPassword(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    boolean _status = this.mRemote.transact(33, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fdeCheckPassword(password);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fdeRestart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(34, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fdeRestart();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int fdeComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(35, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().fdeComplete();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fdeEnable(int passwordType, String password, int encryptionFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(passwordType);
                    _data.writeString(password);
                    _data.writeInt(encryptionFlags);
                    boolean _status = this.mRemote.transact(36, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fdeEnable(passwordType, password, encryptionFlags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fdeChangePassword(int passwordType, String currentPassword, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(passwordType);
                    _data.writeString(currentPassword);
                    _data.writeString(password);
                    boolean _status = this.mRemote.transact(37, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fdeChangePassword(passwordType, currentPassword, password);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fdeVerifyPassword(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    boolean _status = this.mRemote.transact(38, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fdeVerifyPassword(password);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public String fdeGetField(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    boolean _status = this.mRemote.transact(39, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().fdeGetField(key);
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fdeSetField(String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(value);
                    boolean _status = this.mRemote.transact(40, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fdeSetField(key, value);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int fdeGetPasswordType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(41, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().fdeGetPasswordType();
                    }
                    _reply.readException();
                    int _result = _reply.readInt();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public String fdeGetPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(42, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().fdeGetPassword();
                    }
                    _reply.readException();
                    String _result = _reply.readString();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fdeClearPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(43, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fdeClearPassword();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fbeEnable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(44, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fbeEnable();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void mountDefaultEncrypted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(45, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().mountDefaultEncrypted();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void initUser0() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(46, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().initUser0();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean isConvertibleToFbe() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(47, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isConvertibleToFbe();
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

            @Override // android.os.IVold
            public void mountFstab(String blkDevice, String mountPoint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(blkDevice);
                    _data.writeString(mountPoint);
                    boolean _status = this.mRemote.transact(48, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().mountFstab(blkDevice, mountPoint);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void encryptFstab(String blkDevice, String mountPoint, boolean shouldFormat, String fsType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(blkDevice);
                    _data.writeString(mountPoint);
                    _data.writeInt(shouldFormat ? 1 : 0);
                    _data.writeString(fsType);
                    boolean _status = this.mRemote.transact(49, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().encryptFstab(blkDevice, mountPoint, shouldFormat, fsType);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void setStorageBindingSeed(byte[] seed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(seed);
                    boolean _status = this.mRemote.transact(50, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setStorageBindingSeed(seed);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void createUserKey(int userId, int userSerial, boolean ephemeral) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeInt(ephemeral ? 1 : 0);
                    boolean _status = this.mRemote.transact(51, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().createUserKey(userId, userSerial, ephemeral);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyUserKey(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(52, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().destroyUserKey(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void addUserKeyAuth(int userId, int userSerial, String token, String secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeString(token);
                    _data.writeString(secret);
                    boolean _status = this.mRemote.transact(53, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().addUserKeyAuth(userId, userSerial, token, secret);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void clearUserKeyAuth(int userId, int userSerial, String token, String secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeString(token);
                    _data.writeString(secret);
                    boolean _status = this.mRemote.transact(54, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().clearUserKeyAuth(userId, userSerial, token, secret);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void fixateNewestUserKeyAuth(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(55, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().fixateNewestUserKeyAuth(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public int[] getUnlockedUsers() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(56, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().getUnlockedUsers();
                    }
                    _reply.readException();
                    int[] _result = _reply.createIntArray();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unlockUserKey(int userId, int userSerial, String token, String secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeString(token);
                    _data.writeString(secret);
                    boolean _status = this.mRemote.transact(57, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unlockUserKey(userId, userSerial, token, secret);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void lockUserKey(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(58, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().lockUserKey(userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareUserStorage(String uuid, int userId, int userSerial, int storageFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeInt(storageFlags);
                    boolean _status = this.mRemote.transact(59, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().prepareUserStorage(uuid, userId, userSerial, storageFlags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyUserStorage(String uuid, int userId, int storageFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(storageFlags);
                    boolean _status = this.mRemote.transact(60, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().destroyUserStorage(uuid, userId, storageFlags);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareSandboxForApp(String packageName, int appId, String sandboxId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(appId);
                    _data.writeString(sandboxId);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(61, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().prepareSandboxForApp(packageName, appId, sandboxId, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroySandboxForApp(String packageName, String sandboxId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(sandboxId);
                    _data.writeInt(userId);
                    boolean _status = this.mRemote.transact(62, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().destroySandboxForApp(packageName, sandboxId, userId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void startCheckpoint(int retry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(retry);
                    boolean _status = this.mRemote.transact(63, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().startCheckpoint(retry);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean needsCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(64, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().needsCheckpoint();
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

            @Override // android.os.IVold
            public boolean needsRollback() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(65, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().needsRollback();
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

            @Override // android.os.IVold
            public boolean isCheckpointing() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(66, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().isCheckpointing();
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

            @Override // android.os.IVold
            public void abortChanges(String device, boolean retry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(device);
                    _data.writeInt(retry ? 1 : 0);
                    boolean _status = this.mRemote.transact(67, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().abortChanges(device, retry);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void commitChanges() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(68, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().commitChanges();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void prepareCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(69, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().prepareCheckpoint();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void restoreCheckpoint(String device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(device);
                    boolean _status = this.mRemote.transact(70, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restoreCheckpoint(device);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void restoreCheckpointPart(String device, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(device);
                    _data.writeInt(count);
                    boolean _status = this.mRemote.transact(71, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().restoreCheckpointPart(device, count);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void markBootAttempt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(72, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().markBootAttempt();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean supportsCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(73, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supportsCheckpoint();
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

            @Override // android.os.IVold
            public boolean supportsBlockCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(74, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supportsBlockCheckpoint();
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

            @Override // android.os.IVold
            public boolean supportsFileCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(75, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().supportsFileCheckpoint();
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

            @Override // android.os.IVold
            public void resetCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(76, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().resetCheckpoint();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void earlyBootEnded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _status = this.mRemote.transact(77, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().earlyBootEnded();
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public String createStubVolume(String sourcePath, String mountPath, String fsType, String fsUuid, String fsLabel, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(sourcePath);
                        try {
                            _data.writeString(mountPath);
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
                }
                try {
                    _data.writeString(fsType);
                    try {
                        _data.writeString(fsUuid);
                        try {
                            _data.writeString(fsLabel);
                            try {
                                _data.writeInt(flags);
                                boolean _status = this.mRemote.transact(78, _data, _reply, 0);
                                if (!_status && Stub.getDefaultImpl() != null) {
                                    String createStubVolume = Stub.getDefaultImpl().createStubVolume(sourcePath, mountPath, fsType, fsUuid, fsLabel, flags);
                                    _reply.recycle();
                                    _data.recycle();
                                    return createStubVolume;
                                }
                                _reply.readException();
                                String _result = _reply.readString();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
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
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            @Override // android.os.IVold
            public void destroyStubVolume(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    boolean _status = this.mRemote.transact(79, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().destroyStubVolume(volId);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public FileDescriptor openAppFuseFile(int uid, int mountId, int fileId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    _data.writeInt(fileId);
                    _data.writeInt(flags);
                    boolean _status = this.mRemote.transact(80, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().openAppFuseFile(uid, mountId, fileId, flags);
                    }
                    _reply.readException();
                    FileDescriptor _result = _reply.readRawFileDescriptor();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public boolean incFsEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    boolean _status = this.mRemote.transact(81, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().incFsEnabled();
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

            @Override // android.os.IVold
            public IncrementalFileSystemControlParcel mountIncFs(String backingPath, String targetDir, int flags, String sysfsName) throws RemoteException {
                IncrementalFileSystemControlParcel _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(backingPath);
                    _data.writeString(targetDir);
                    _data.writeInt(flags);
                    _data.writeString(sysfsName);
                    boolean _status = this.mRemote.transact(82, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        return Stub.getDefaultImpl().mountIncFs(backingPath, targetDir, flags, sysfsName);
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = IncrementalFileSystemControlParcel.CREATOR.mo3559createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void unmountIncFs(String dir) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dir);
                    boolean _status = this.mRemote.transact(83, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().unmountIncFs(dir);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void setIncFsMountOptions(IncrementalFileSystemControlParcel control, boolean enableReadLogs, boolean enableReadTimeouts, String sysfsName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (control != null) {
                        _data.writeInt(1);
                        control.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(enableReadLogs ? 1 : 0);
                    if (!enableReadTimeouts) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(sysfsName);
                    boolean _status = this.mRemote.transact(84, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().setIncFsMountOptions(control, enableReadLogs, enableReadTimeouts, sysfsName);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void bindMount(String sourceDir, String targetDir) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sourceDir);
                    _data.writeString(targetDir);
                    boolean _status = this.mRemote.transact(85, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().bindMount(sourceDir, targetDir);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override // android.os.IVold
            public void destroyDsuMetadataKey(String dsuSlot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dsuSlot);
                    boolean _status = this.mRemote.transact(86, _data, _reply, 0);
                    if (!_status && Stub.getDefaultImpl() != null) {
                        Stub.getDefaultImpl().destroyDsuMetadataKey(dsuSlot);
                    } else {
                        _reply.readException();
                    }
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public static boolean setDefaultImpl(IVold impl) {
            if (Proxy.sDefaultImpl != null) {
                throw new IllegalStateException("setDefaultImpl() called twice");
            }
            if (impl != null) {
                Proxy.sDefaultImpl = impl;
                return true;
            }
            return false;
        }

        public static IVold getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }
}
