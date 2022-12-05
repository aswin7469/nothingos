package com.android.server;

import android.accounts.GrantCredentialsPermissionActivity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.media.MediaMetrics;
import android.nfc.cardemulation.CardEmulation;
import android.os.Build;
import android.os.CarrierAssociatedAppEntry;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Process;
import android.os.SystemProperties;
import android.os.incremental.IncrementalManager;
import android.os.storage.StorageManager;
import android.permission.PermissionManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.provider.SettingsStringUtil;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimingsTraceLog;
import android.util.Xml;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes4.dex */
public class SystemConfig {
    private static final int ALLOW_ALL = -1;
    private static final int ALLOW_APP_CONFIGS = 8;
    private static final int ALLOW_ASSOCIATIONS = 128;
    private static final int ALLOW_FEATURES = 1;
    private static final int ALLOW_HIDDENAPI_WHITELISTING = 64;
    private static final int ALLOW_IMPLICIT_BROADCASTS = 512;
    private static final int ALLOW_LIBS = 2;
    private static final int ALLOW_OEM_PERMISSIONS = 32;
    private static final int ALLOW_OVERRIDE_APP_RESTRICTIONS = 256;
    private static final int ALLOW_PERMISSIONS = 4;
    private static final int ALLOW_PRIVAPP_PERMISSIONS = 16;
    private static final int ALLOW_VENDOR_APEX = 1024;
    private static final String NO_RIL_PROPERTY = "ro.radio.noril";
    private static final String SKU_PROPERTY = "ro.boot.product.hardware.sku";
    static final String TAG = "SystemConfig";
    private static final String VENDOR_SKU_PROPERTY = "ro.boot.product.vendor.sku";
    static SystemConfig sInstance;
    private String mModulesInstallerPackageName;
    private String mOverlayConfigSignaturePackage;
    int[] mGlobalGids = EmptyArray.INT;
    final SparseArray<ArraySet<String>> mSystemPermissions = new SparseArray<>();
    final ArrayList<PermissionManager.SplitPermissionInfo> mSplitPermissions = new ArrayList<>();
    final ArrayMap<String, SharedLibraryEntry> mSharedLibraries = new ArrayMap<>();
    final ArrayMap<String, FeatureInfo> mAvailableFeatures = new ArrayMap<>();
    final ArraySet<String> mUnavailableFeatures = new ArraySet<>();
    final ArrayMap<String, PermissionEntry> mPermissions = new ArrayMap<>();
    final ArraySet<String> mAllowInPowerSaveExceptIdle = new ArraySet<>();
    final ArraySet<String> mAllowInPowerSave = new ArraySet<>();
    final ArraySet<String> mAllowInDataUsageSave = new ArraySet<>();
    final ArraySet<String> mAllowUnthrottledLocation = new ArraySet<>();
    final ArrayMap<String, ArraySet<String>> mAllowIgnoreLocationSettings = new ArrayMap<>();
    final ArraySet<String> mAllowImplicitBroadcasts = new ArraySet<>();
    final ArraySet<String> mLinkedApps = new ArraySet<>();
    final ArraySet<ComponentName> mDefaultVrComponents = new ArraySet<>();
    final ArraySet<ComponentName> mBackupTransportWhitelist = new ArraySet<>();
    final ArrayMap<String, ArrayMap<String, Boolean>> mPackageComponentEnabledState = new ArrayMap<>();
    final ArraySet<String> mHiddenApiPackageWhitelist = new ArraySet<>();
    final ArraySet<String> mDisabledUntilUsedPreinstalledCarrierApps = new ArraySet<>();
    final ArrayMap<String, List<CarrierAssociatedAppEntry>> mDisabledUntilUsedPreinstalledCarrierAssociatedApps = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mPrivAppPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mPrivAppDenyPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mVendorPrivAppPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mVendorPrivAppDenyPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mProductPrivAppPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mProductPrivAppDenyPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mSystemExtPrivAppPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mSystemExtPrivAppDenyPermissions = new ArrayMap<>();
    final ArrayMap<String, ArrayMap<String, Boolean>> mOemPermissions = new ArrayMap<>();
    final ArrayMap<String, ArraySet<String>> mAllowedAssociations = new ArrayMap<>();
    private final ArraySet<String> mBugreportWhitelistedPackages = new ArraySet<>();
    private final ArraySet<String> mAppDataIsolationWhitelistedApps = new ArraySet<>();
    private ArrayMap<String, Set<String>> mPackageToUserTypeWhitelist = new ArrayMap<>();
    private ArrayMap<String, Set<String>> mPackageToUserTypeBlacklist = new ArrayMap<>();
    private final ArraySet<String> mRollbackWhitelistedPackages = new ArraySet<>();
    private final ArraySet<String> mWhitelistedStagedInstallers = new ArraySet<>();
    private final ArrayMap<String, String> mAllowedVendorApexes = new ArrayMap<>();
    private Map<String, Map<String, String>> mNamedActors = null;

    /* loaded from: classes4.dex */
    public static final class SharedLibraryEntry {
        public final String[] dependencies;
        public final String filename;
        public final boolean isNative;
        public final String name;

        SharedLibraryEntry(String name, String filename, String[] dependencies) {
            this(name, filename, dependencies, false);
        }

        SharedLibraryEntry(String name, String filename, String[] dependencies, boolean isNative) {
            this.name = name;
            this.filename = filename;
            this.dependencies = dependencies;
            this.isNative = isNative;
        }
    }

    /* loaded from: classes4.dex */
    public static final class PermissionEntry {
        public int[] gids;
        public final String name;
        public boolean perUser;

        PermissionEntry(String name, boolean perUser) {
            this.name = name;
            this.perUser = perUser;
        }
    }

    public static SystemConfig getInstance() {
        SystemConfig systemConfig;
        if (!isSystemProcess()) {
            Slog.wtf(TAG, "SystemConfig is being accessed by a process other than system_server.");
        }
        synchronized (SystemConfig.class) {
            if (sInstance == null) {
                sInstance = new SystemConfig();
            }
            systemConfig = sInstance;
        }
        return systemConfig;
    }

    public int[] getGlobalGids() {
        return this.mGlobalGids;
    }

    public SparseArray<ArraySet<String>> getSystemPermissions() {
        return this.mSystemPermissions;
    }

    public ArrayList<PermissionManager.SplitPermissionInfo> getSplitPermissions() {
        return this.mSplitPermissions;
    }

    public ArrayMap<String, SharedLibraryEntry> getSharedLibraries() {
        return this.mSharedLibraries;
    }

    public ArrayMap<String, FeatureInfo> getAvailableFeatures() {
        return this.mAvailableFeatures;
    }

    public ArrayMap<String, PermissionEntry> getPermissions() {
        return this.mPermissions;
    }

    public ArraySet<String> getAllowImplicitBroadcasts() {
        return this.mAllowImplicitBroadcasts;
    }

    public ArraySet<String> getAllowInPowerSaveExceptIdle() {
        return this.mAllowInPowerSaveExceptIdle;
    }

    public ArraySet<String> getAllowInPowerSave() {
        return this.mAllowInPowerSave;
    }

    public ArraySet<String> getAllowInDataUsageSave() {
        return this.mAllowInDataUsageSave;
    }

    public ArraySet<String> getAllowUnthrottledLocation() {
        return this.mAllowUnthrottledLocation;
    }

    public ArrayMap<String, ArraySet<String>> getAllowIgnoreLocationSettings() {
        return this.mAllowIgnoreLocationSettings;
    }

    public ArraySet<String> getLinkedApps() {
        return this.mLinkedApps;
    }

    public ArraySet<String> getHiddenApiWhitelistedApps() {
        return this.mHiddenApiPackageWhitelist;
    }

    public ArraySet<ComponentName> getDefaultVrComponents() {
        return this.mDefaultVrComponents;
    }

    public ArraySet<ComponentName> getBackupTransportWhitelist() {
        return this.mBackupTransportWhitelist;
    }

    public ArrayMap<String, Boolean> getComponentsEnabledStates(String packageName) {
        return this.mPackageComponentEnabledState.get(packageName);
    }

    public ArraySet<String> getDisabledUntilUsedPreinstalledCarrierApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierApps;
    }

    public ArrayMap<String, List<CarrierAssociatedAppEntry>> getDisabledUntilUsedPreinstalledCarrierAssociatedApps() {
        return this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps;
    }

    public ArraySet<String> getPrivAppPermissions(String packageName) {
        return this.mPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getPrivAppDenyPermissions(String packageName) {
        return this.mPrivAppDenyPermissions.get(packageName);
    }

    public ArraySet<String> getVendorPrivAppPermissions(String packageName) {
        return this.mVendorPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getVendorPrivAppDenyPermissions(String packageName) {
        return this.mVendorPrivAppDenyPermissions.get(packageName);
    }

    public ArraySet<String> getProductPrivAppPermissions(String packageName) {
        return this.mProductPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getProductPrivAppDenyPermissions(String packageName) {
        return this.mProductPrivAppDenyPermissions.get(packageName);
    }

    public ArraySet<String> getSystemExtPrivAppPermissions(String packageName) {
        return this.mSystemExtPrivAppPermissions.get(packageName);
    }

    public ArraySet<String> getSystemExtPrivAppDenyPermissions(String packageName) {
        return this.mSystemExtPrivAppDenyPermissions.get(packageName);
    }

    public Map<String, Boolean> getOemPermissions(String packageName) {
        Map<String, Boolean> oemPermissions = this.mOemPermissions.get(packageName);
        if (oemPermissions != null) {
            return oemPermissions;
        }
        return Collections.emptyMap();
    }

    public ArrayMap<String, ArraySet<String>> getAllowedAssociations() {
        return this.mAllowedAssociations;
    }

    public ArraySet<String> getBugreportWhitelistedPackages() {
        return this.mBugreportWhitelistedPackages;
    }

    public Set<String> getRollbackWhitelistedPackages() {
        return this.mRollbackWhitelistedPackages;
    }

    public Set<String> getWhitelistedStagedInstallers() {
        return this.mWhitelistedStagedInstallers;
    }

    public Map<String, String> getAllowedVendorApexes() {
        return this.mAllowedVendorApexes;
    }

    public String getModulesInstallerPackageName() {
        return this.mModulesInstallerPackageName;
    }

    public ArraySet<String> getAppDataIsolationWhitelistedApps() {
        return this.mAppDataIsolationWhitelistedApps;
    }

    public ArrayMap<String, Set<String>> getAndClearPackageToUserTypeWhitelist() {
        ArrayMap<String, Set<String>> r = this.mPackageToUserTypeWhitelist;
        this.mPackageToUserTypeWhitelist = new ArrayMap<>(0);
        return r;
    }

    public ArrayMap<String, Set<String>> getAndClearPackageToUserTypeBlacklist() {
        ArrayMap<String, Set<String>> r = this.mPackageToUserTypeBlacklist;
        this.mPackageToUserTypeBlacklist = new ArrayMap<>(0);
        return r;
    }

    public Map<String, Map<String, String>> getNamedActors() {
        Map<String, Map<String, String>> map = this.mNamedActors;
        return map != null ? map : Collections.emptyMap();
    }

    public String getOverlayConfigSignaturePackage() {
        if (TextUtils.isEmpty(this.mOverlayConfigSignaturePackage)) {
            return null;
        }
        return this.mOverlayConfigSignaturePackage;
    }

    public SystemConfig(boolean readPermissions) {
        if (readPermissions) {
            Slog.w(TAG, "Constructing a test SystemConfig");
            readAllPermissions();
            return;
        }
        Slog.w(TAG, "Constructing an empty test SystemConfig");
    }

    SystemConfig() {
        TimingsTraceLog log = new TimingsTraceLog(TAG, 524288L);
        log.traceBegin("readAllPermissions");
        try {
            readAllPermissions();
            readPublicNativeLibrariesList();
        } finally {
            log.traceEnd();
        }
    }

    private void readAllPermissions() {
        char c;
        readPermissions(Environment.buildPath(Environment.getRootDirectory(), "etc", "sysconfig"), -1);
        readPermissions(Environment.buildPath(Environment.getRootDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS), -1);
        int vendorPermissionFlag = 1171;
        if (Build.VERSION.DEVICE_INITIAL_SDK_INT <= 27) {
            vendorPermissionFlag = 1171 | 12;
        }
        readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", "sysconfig"), vendorPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS), vendorPermissionFlag);
        String vendorSkuProperty = SystemProperties.get(VENDOR_SKU_PROPERTY, "");
        char c2 = 2;
        int i = 0;
        if (!vendorSkuProperty.isEmpty()) {
            String vendorSkuDir = "sku_" + vendorSkuProperty;
            readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", "sysconfig", vendorSkuDir), vendorPermissionFlag);
            readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS, vendorSkuDir), vendorPermissionFlag);
        }
        boolean noRilSupport = SystemProperties.getBoolean(NO_RIL_PROPERTY, false);
        if (noRilSupport) {
            readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", "sysconfig", "noRil"), vendorPermissionFlag);
            readPermissions(Environment.buildPath(Environment.getVendorDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS, "noRil"), vendorPermissionFlag);
        }
        int odmPermissionFlag = vendorPermissionFlag;
        readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", "sysconfig"), odmPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS), odmPermissionFlag);
        String skuProperty = SystemProperties.get(SKU_PROPERTY, "");
        if (!skuProperty.isEmpty()) {
            String skuDir = "sku_" + skuProperty;
            readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", "sysconfig", skuDir), odmPermissionFlag);
            readPermissions(Environment.buildPath(Environment.getOdmDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS, skuDir), odmPermissionFlag);
        }
        readPermissions(Environment.buildPath(Environment.getOemDirectory(), "etc", "sysconfig"), 1185);
        readPermissions(Environment.buildPath(Environment.getOemDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS), 1185);
        int productPermissionFlag = 2015;
        if (Build.VERSION.DEVICE_INITIAL_SDK_INT <= 30) {
            productPermissionFlag = -1;
        }
        readPermissions(Environment.buildPath(Environment.getProductDirectory(), "etc", "sysconfig"), productPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getProductDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS), productPermissionFlag);
        readPermissions(Environment.buildPath(Environment.getSystemExtDirectory(), "etc", "sysconfig"), -1);
        readPermissions(Environment.buildPath(Environment.getSystemExtDirectory(), "etc", DeviceConfig.NAMESPACE_PERMISSIONS), -1);
        if (!isSystemProcess()) {
            return;
        }
        File[] listFilesOrEmpty = FileUtils.listFilesOrEmpty(Environment.getApexDirectory());
        int length = listFilesOrEmpty.length;
        while (i < length) {
            File f = listFilesOrEmpty[i];
            if (f.isFile()) {
                c = c2;
            } else if (!f.getPath().contains("@")) {
                c = 2;
                readPermissions(Environment.buildPath(f, "etc", DeviceConfig.NAMESPACE_PERMISSIONS), 2);
            } else {
                c = 2;
            }
            i++;
            c2 = c;
        }
    }

    public void readPermissions(File libraryDir, int permissionFlag) {
        File[] listFiles;
        if (!libraryDir.exists() || !libraryDir.isDirectory()) {
            if (permissionFlag == -1) {
                Slog.w(TAG, "No directory " + libraryDir + ", skipping");
            }
        } else if (!libraryDir.canRead()) {
            Slog.w(TAG, "Directory " + libraryDir + " cannot be read");
        } else {
            File platformFile = null;
            for (File f : libraryDir.listFiles()) {
                if (f.isFile()) {
                    if (f.getPath().endsWith("etc/permissions/platform.xml")) {
                        platformFile = f;
                    } else if (!f.getPath().endsWith(".xml")) {
                        Slog.i(TAG, "Non-xml file " + f + " in " + libraryDir + " directory, ignoring");
                    } else if (!f.canRead()) {
                        Slog.w(TAG, "Permissions library file " + f + " cannot be read");
                    } else {
                        readPermissionsFromXml(f, permissionFlag);
                    }
                }
            }
            if (platformFile != null) {
                readPermissionsFromXml(platformFile, permissionFlag);
            }
        }
    }

    private void logNotAllowedInPartition(String name, File permFile, XmlPullParser parser) {
        Slog.w(TAG, "<" + name + "> not allowed in partition of " + permFile + " at " + parser.getPositionDescription());
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x003d, code lost:
        if (r12 != 2) goto L540;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0049, code lost:
        if (r10.getName().equals(android.provider.DeviceConfig.NAMESPACE_PERMISSIONS) != false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x009b, code lost:
        if (r34 != (-1)) goto L521;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x009d, code lost:
        r15 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00a2, code lost:
        if ((r34 & 2) == 0) goto L520;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00a4, code lost:
        r16 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00ab, code lost:
        if ((r34 & 1) == 0) goto L519;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00ad, code lost:
        r17 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00b4, code lost:
        if ((r34 & 4) == 0) goto L518;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00b6, code lost:
        r18 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00bd, code lost:
        if ((r34 & 8) == 0) goto L517;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00bf, code lost:
        r19 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00c6, code lost:
        if ((r34 & 16) == 0) goto L516;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00c8, code lost:
        r20 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00cf, code lost:
        if ((r34 & 32) == 0) goto L515;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d1, code lost:
        r21 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x00d8, code lost:
        if ((r34 & 64) == 0) goto L514;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00da, code lost:
        r22 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00e1, code lost:
        if ((r34 & 128) == 0) goto L513;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00e3, code lost:
        r11 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00e8, code lost:
        if ((r34 & 256) == 0) goto L512;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ea, code lost:
        r13 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00ef, code lost:
        if ((r34 & 512) == 0) goto L511;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00f1, code lost:
        r8 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00f6, code lost:
        if ((r34 & 1024) == 0) goto L510;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x00f8, code lost:
        r9 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:510:0x00fa, code lost:
        r9 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x00f3, code lost:
        r8 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:512:0x00ec, code lost:
        r13 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x00e5, code lost:
        r11 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x00dd, code lost:
        r22 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x00d4, code lost:
        r21 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:516:0x00cb, code lost:
        r20 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x00c2, code lost:
        r19 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:518:0x00b9, code lost:
        r18 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x00b0, code lost:
        r17 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:520:0x00a7, code lost:
        r16 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x009f, code lost:
        r15 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0055, code lost:
        if (r10.getName().equals("config") == false) goto L526;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x007f, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("Unexpected start tag in " + r33 + ": found " + r10.getName() + ", expected 'permissions' or 'config'");
     */
    /* JADX WARN: Code restructure failed: missing block: B:528:0x0088, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x0089, code lost:
        r3 = r0;
        r25 = r4;
        r24 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x0da7, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, r25, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0db6, code lost:
        libcore.io.IoUtils.closeQuietly(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:533:0x0092, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x0093, code lost:
        r3 = r0;
        r24 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x0db2, code lost:
        android.util.Slog.w(com.android.server.SystemConfig.TAG, r4, r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x0080, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:537:0x0081, code lost:
        r3 = r0;
        r24 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0e1e, code lost:
        libcore.io.IoUtils.closeQuietly(r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:539:0x0e21, code lost:
        throw r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x0d8c, code lost:
        throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
     */
    /* JADX WARN: Not initialized variable reg: 25, insn: 0x0d92: MOVE  (r4 I:??[OBJECT, ARRAY]) = (r25 I:??[OBJECT, ARRAY]), block:B:545:0x0d91 */
    /* JADX WARN: Removed duplicated region for block: B:188:0x0722 A[Catch: IOException -> 0x0d8d, XmlPullParserException -> 0x0d90, all -> 0x0e1c, TryCatch #8 {all -> 0x0e1c, blocks: (B:66:0x0d42, B:71:0x02b3, B:73:0x02bf, B:75:0x02e3, B:78:0x030b, B:80:0x0314, B:81:0x0311, B:531:0x0da7, B:535:0x0db2, B:83:0x031c, B:85:0x0329, B:87:0x0353, B:89:0x0357, B:91:0x035a, B:92:0x0361, B:95:0x0366, B:96:0x034c, B:97:0x0363, B:98:0x036c, B:100:0x0372, B:101:0x039a, B:102:0x0395, B:104:0x03a2, B:106:0x03a8, B:108:0x03fc, B:109:0x03cb, B:111:0x03d3, B:113:0x03da, B:114:0x03f8, B:115:0x03f9, B:116:0x0402, B:118:0x041f, B:119:0x04cd, B:120:0x0445, B:122:0x044b, B:123:0x0470, B:125:0x0476, B:126:0x049b, B:128:0x04a3, B:130:0x04a7, B:131:0x04ae, B:133:0x04b8, B:134:0x04ca, B:135:0x04c4, B:138:0x04d3, B:139:0x0507, B:141:0x0508, B:142:0x052b, B:143:0x052c, B:144:0x0536, B:146:0x053c, B:147:0x0564, B:148:0x055f, B:149:0x056a, B:151:0x0570, B:152:0x0598, B:153:0x0593, B:155:0x05a0, B:157:0x05a8, B:158:0x05d2, B:160:0x05da, B:161:0x0604, B:163:0x0618, B:164:0x0623, B:165:0x0647, B:166:0x0644, B:168:0x064f, B:170:0x0655, B:172:0x0681, B:173:0x0678, B:174:0x067e, B:176:0x0689, B:177:0x068f, B:180:0x069c, B:182:0x06bd, B:186:0x06e2, B:188:0x0722, B:191:0x072c, B:193:0x0736, B:194:0x073e, B:196:0x0748, B:198:0x0753, B:200:0x0759, B:202:0x0785, B:203:0x077c, B:204:0x0782, B:206:0x078d, B:210:0x079c, B:219:0x07a9, B:212:0x07db, B:214:0x07e5, B:215:0x07f0, B:217:0x0821, B:224:0x07b1, B:225:0x07f9, B:226:0x081e, B:228:0x0829, B:230:0x0831, B:232:0x0892, B:233:0x0856, B:235:0x085c, B:236:0x0889, B:237:0x088f, B:238:0x0898, B:240:0x08a0, B:242:0x08ac, B:244:0x0904, B:246:0x08d1, B:247:0x08f6, B:248:0x0901, B:250:0x090c, B:252:0x0912, B:254:0x093e, B:255:0x0935, B:256:0x093b, B:258:0x0946, B:260:0x094e, B:262:0x097c, B:263:0x0973, B:264:0x0979, B:266:0x0984, B:268:0x0990, B:270:0x09e9, B:271:0x09b3, B:273:0x09bd, B:276:0x09c5, B:277:0x09d1, B:279:0x09d9, B:282:0x09e2, B:283:0x09e6, B:285:0x09f1, B:287:0x09f7, B:289:0x0a23, B:290:0x0a1a, B:291:0x0a20, B:293:0x0a2b, B:295:0x0a31, B:297:0x0a5d, B:298:0x0a54, B:299:0x0a5a, B:301:0x0a65, B:303:0x0a6b, B:305:0x0a97, B:306:0x0a8e, B:307:0x0a94, B:309:0x0a9f, B:311:0x0aa5, B:313:0x0ad1, B:314:0x0ac8, B:315:0x0ace, B:317:0x0ad9, B:319:0x0adf, B:321:0x0b0d, B:322:0x0b04, B:323:0x0b0a, B:326:0x0b17, B:330:0x0b38, B:332:0x0b65, B:334:0x0b5d, B:335:0x0b27, B:336:0x0b61, B:339:0x0b6f, B:341:0x0b81, B:343:0x0be5, B:345:0x0ba6, B:346:0x0bcb, B:348:0x0bcf, B:349:0x0bd9, B:350:0x0bd3, B:351:0x0be2, B:354:0x0bed, B:355:0x0bf2, B:358:0x0bff, B:360:0x0c05, B:361:0x0c2c, B:363:0x0c34, B:364:0x0c5d, B:366:0x0c63, B:367:0x0c94, B:369:0x0ca3, B:370:0x0cae, B:371:0x0cb6, B:372:0x0cb3, B:375:0x0cc0, B:377:0x0cc6, B:378:0x0ced, B:379:0x0cf7, B:382:0x0d01, B:384:0x0d09, B:386:0x0d3e, B:387:0x0d16, B:388:0x0d3b, B:540:0x0d7d, B:541:0x0d8c), top: B:5:0x002a }] */
    /* JADX WARN: Removed duplicated region for block: B:190:0x072a  */
    /* JADX WARN: Removed duplicated region for block: B:488:0x0dc0  */
    /* JADX WARN: Removed duplicated region for block: B:491:0x0dd3  */
    /* JADX WARN: Removed duplicated region for block: B:494:0x0dde  */
    /* JADX WARN: Removed duplicated region for block: B:497:0x0def  */
    /* JADX WARN: Removed duplicated region for block: B:500:0x0e00  */
    /* JADX WARN: Removed duplicated region for block: B:504:0x0e11 A[LOOP:2: B:502:0x0e0b->B:504:0x0e11, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:507:0x0de4  */
    /* JADX WARN: Removed duplicated region for block: B:508:0x0dcc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void readPermissionsFromXml(File permFile, int permissionFlag) {
        Throwable th;
        FileReader permReader;
        XmlPullParserException e;
        IOException e2;
        int i;
        XmlPullParser parser;
        int type;
        String str;
        boolean allowAll;
        boolean allowLibs;
        boolean allowFeatures;
        boolean allowPermissions;
        boolean allowAppConfigs;
        boolean allowPrivappPermissions;
        boolean allowOemPermissions;
        boolean allowApiWhitelisting;
        boolean allowAssociations;
        boolean allowOverrideAppRestrictions;
        boolean allowImplicitBroadcasts;
        boolean allowVendorApex;
        char c;
        boolean allowed;
        boolean vendor;
        String str2 = "Got exception parsing permissions.";
        try {
            FileReader permReader2 = new FileReader(permFile);
            Slog.i(TAG, "Reading permissions from " + permFile);
            boolean lowRam = ActivityManager.isLowRamDeviceStatic();
            try {
                try {
                    parser = Xml.newPullParser();
                    parser.setInput(permReader2);
                    while (true) {
                        int next = parser.next();
                        type = next;
                        if (next == 2 || type == 1) {
                            try {
                                break;
                            } catch (IOException e3) {
                                e2 = e3;
                            } catch (XmlPullParserException e4) {
                                e = e4;
                                str2 = str;
                            }
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (IOException e5) {
                String str3 = str2;
                permReader = permReader2;
                e2 = e5;
            } catch (XmlPullParserException e6) {
                permReader = permReader2;
                e = e6;
            } catch (Throwable th3) {
                FileReader permReader3 = permReader2;
                th = th3;
            }
            if (StorageManager.isFileEncryptedNativeOnly()) {
                i = 0;
                addFeature(PackageManager.FEATURE_FILE_BASED_ENCRYPTION, 0);
                addFeature(PackageManager.FEATURE_SECURELY_REMOVES_USERS, 0);
            } else {
                i = 0;
            }
            if (StorageManager.hasAdoptable()) {
                addFeature(PackageManager.FEATURE_ADOPTABLE_STORAGE, i);
            }
            if (ActivityManager.isLowRamDeviceStatic()) {
                addFeature(PackageManager.FEATURE_RAM_LOW, i);
            } else {
                addFeature(PackageManager.FEATURE_RAM_NORMAL, i);
            }
            int incrementalVersion = IncrementalManager.getVersion();
            if (incrementalVersion > 0) {
                addFeature(PackageManager.FEATURE_INCREMENTAL_DELIVERY, incrementalVersion);
            }
            addFeature(PackageManager.FEATURE_APP_ENUMERATION, 0);
            if (Build.VERSION.DEVICE_INITIAL_SDK_INT >= 29) {
                addFeature(PackageManager.FEATURE_IPSEC_TUNNELS, 0);
            }
            Iterator<String> it = this.mUnavailableFeatures.iterator();
            while (it.hasNext()) {
                String featureName = it.next();
                removeFeature(featureName);
            }
            return;
            while (true) {
                XmlUtils.nextElement(parser);
                if (parser.getEventType() == 1) {
                    IoUtils.closeQuietly(permReader2);
                } else {
                    String name = parser.getName();
                    if (name == null) {
                        XmlUtils.skipCurrentTag(parser);
                    } else {
                        switch (name.hashCode()) {
                            case -2040330235:
                                if (name.equals("allow-unthrottled-location")) {
                                    c = '\n';
                                    break;
                                }
                                c = 65535;
                                break;
                            case -1882490007:
                                if (name.equals("allow-in-power-save")) {
                                    c = '\b';
                                    break;
                                }
                                c = 65535;
                                break;
                            case -1554938271:
                                if (name.equals("named-actor")) {
                                    c = 26;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -1461465444:
                                if (name.equals("component-override")) {
                                    c = 15;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -1005864890:
                                if (name.equals("disabled-until-used-preinstalled-carrier-app")) {
                                    c = 18;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -980620291:
                                if (name.equals("allow-association")) {
                                    c = 22;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -979207434:
                                if (name.equals("feature")) {
                                    c = 5;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -828905863:
                                if (name.equals("unavailable-feature")) {
                                    c = 6;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -642819164:
                                if (name.equals("allow-in-power-save-except-idle")) {
                                    c = 7;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -560717308:
                                if (name.equals("allow-ignore-location-settings")) {
                                    c = 11;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -517618225:
                                if (name.equals("permission")) {
                                    c = 1;
                                    break;
                                }
                                c = 65535;
                                break;
                            case -150068154:
                                if (name.equals("install-in-user-type")) {
                                    c = 25;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 98629247:
                                if (name.equals("group")) {
                                    c = 0;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 166208699:
                                if (name.equals("library")) {
                                    c = 4;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 180165796:
                                if (name.equals("hidden-api-whitelisted-app")) {
                                    c = 21;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 347247519:
                                if (name.equals("backup-transport-whitelisted-service")) {
                                    c = 16;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 414198242:
                                if (name.equals("allowed-vendor-apex")) {
                                    c = 30;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 802332808:
                                if (name.equals("allow-in-data-usage-save")) {
                                    c = '\t';
                                    break;
                                }
                                c = 65535;
                                break;
                            case 953292141:
                                if (name.equals("assign-permission")) {
                                    c = 2;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 968751633:
                                if (name.equals("rollback-whitelisted-app")) {
                                    c = 28;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1044015374:
                                if (name.equals("oem-permissions")) {
                                    c = 20;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1046683496:
                                if (name.equals("whitelisted-staged-installer")) {
                                    c = 29;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1121420326:
                                if (name.equals("app-link")) {
                                    c = '\r';
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1269564002:
                                if (name.equals("split-permission")) {
                                    c = 3;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1347585732:
                                if (name.equals("app-data-isolation-whitelisted-app")) {
                                    c = 23;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1567330472:
                                if (name.equals("default-enabled-vr-app")) {
                                    c = 14;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1633270165:
                                if (name.equals("disabled-until-used-preinstalled-carrier-associated-app")) {
                                    c = 17;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1723146313:
                                if (name.equals("privapp-permissions")) {
                                    c = 19;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1723586945:
                                if (name.equals("bugreport-whitelisted")) {
                                    c = 24;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1793277898:
                                if (name.equals("overlay-config-signature")) {
                                    c = 27;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1954925533:
                                if (name.equals("allow-implicit-broadcast")) {
                                    c = '\f';
                                    break;
                                }
                                c = 65535;
                                break;
                            default:
                                c = 65535;
                                break;
                        }
                        int type2 = type;
                        FileReader permReader4 = permReader2;
                        String str4 = str2;
                        boolean lowRam2 = lowRam;
                        boolean allowOverrideAppRestrictions2 = allowOverrideAppRestrictions;
                        boolean allowImplicitBroadcasts2 = allowImplicitBroadcasts;
                        boolean allowAssociations2 = allowAssociations;
                        switch (c) {
                            case 0:
                                if (allowAll) {
                                    String gidStr = parser.getAttributeValue(null, "gid");
                                    if (gidStr != null) {
                                        int gid = Process.getGidForName(gidStr);
                                        this.mGlobalGids = ArrayUtils.appendInt(this.mGlobalGids, gid);
                                    } else {
                                        Slog.w(TAG, "<" + name + "> without gid in " + permFile + " at " + parser.getPositionDescription());
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 1:
                                if (!allowPermissions) {
                                    logNotAllowedInPartition(name, permFile, parser);
                                    XmlUtils.skipCurrentTag(parser);
                                    break;
                                } else {
                                    String perm = parser.getAttributeValue(null, "name");
                                    if (perm != null) {
                                        readPermission(parser, perm.intern());
                                        break;
                                    } else {
                                        Slog.w(TAG, "<" + name + "> without name in " + permFile + " at " + parser.getPositionDescription());
                                        XmlUtils.skipCurrentTag(parser);
                                        break;
                                    }
                                }
                            case 2:
                                if (allowPermissions) {
                                    String perm2 = parser.getAttributeValue(null, "name");
                                    if (perm2 == null) {
                                        Slog.w(TAG, "<" + name + "> without name in " + permFile + " at " + parser.getPositionDescription());
                                        XmlUtils.skipCurrentTag(parser);
                                        break;
                                    } else {
                                        String uidStr = parser.getAttributeValue(null, GrantCredentialsPermissionActivity.EXTRAS_REQUESTING_UID);
                                        if (uidStr == null) {
                                            Slog.w(TAG, "<" + name + "> without uid in " + permFile + " at " + parser.getPositionDescription());
                                            XmlUtils.skipCurrentTag(parser);
                                            break;
                                        } else {
                                            int uid = Process.getUidForName(uidStr);
                                            if (uid < 0) {
                                                Slog.w(TAG, "<" + name + "> with unknown uid \"" + uidStr + "  in " + permFile + " at " + parser.getPositionDescription());
                                                XmlUtils.skipCurrentTag(parser);
                                                break;
                                            } else {
                                                String perm3 = perm2.intern();
                                                ArraySet<String> perms = this.mSystemPermissions.get(uid);
                                                if (perms == null) {
                                                    perms = new ArraySet<>();
                                                    this.mSystemPermissions.put(uid, perms);
                                                }
                                                perms.add(perm3);
                                            }
                                        }
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 3:
                                if (!allowPermissions) {
                                    logNotAllowedInPartition(name, permFile, parser);
                                    XmlUtils.skipCurrentTag(parser);
                                    break;
                                } else {
                                    readSplitPermission(parser, permFile);
                                    break;
                                }
                            case 4:
                                if (allowLibs) {
                                    String lname = parser.getAttributeValue(null, "name");
                                    String lfile = parser.getAttributeValue(null, ContentResolver.SCHEME_FILE);
                                    String ldependency = parser.getAttributeValue(null, "dependency");
                                    if (lname == null) {
                                        Slog.w(TAG, "<" + name + "> without name in " + permFile + " at " + parser.getPositionDescription());
                                    } else if (lfile == null) {
                                        Slog.w(TAG, "<" + name + "> without file in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        SharedLibraryEntry entry = new SharedLibraryEntry(lname, lfile, ldependency == null ? new String[0] : ldependency.split(SettingsStringUtil.DELIMITER));
                                        this.mSharedLibraries.put(lname, entry);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 5:
                                if (allowFeatures) {
                                    String fname = parser.getAttributeValue(null, "name");
                                    int fversion = XmlUtils.readIntAttribute(parser, "version", 0);
                                    if (!lowRam2) {
                                        allowed = true;
                                    } else {
                                        String notLowRam = parser.getAttributeValue(null, "notLowRam");
                                        allowed = !"true".equals(notLowRam);
                                    }
                                    if (fname == null) {
                                        Slog.w(TAG, "<" + name + "> without name in " + permFile + " at " + parser.getPositionDescription());
                                    } else if (allowed) {
                                        addFeature(fname, fversion);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 6:
                                if (allowFeatures) {
                                    String fname2 = parser.getAttributeValue(null, "name");
                                    if (fname2 == null) {
                                        Slog.w(TAG, "<" + name + "> without name in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mUnavailableFeatures.add(fname2);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 7:
                                if (allowOverrideAppRestrictions2) {
                                    String pkgname = parser.getAttributeValue(null, "package");
                                    if (pkgname == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mAllowInPowerSaveExceptIdle.add(pkgname);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case '\b':
                                if (allowOverrideAppRestrictions2) {
                                    String pkgname2 = parser.getAttributeValue(null, "package");
                                    if (pkgname2 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mAllowInPowerSave.add(pkgname2);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case '\t':
                                if (allowOverrideAppRestrictions2) {
                                    String pkgname3 = parser.getAttributeValue(null, "package");
                                    if (pkgname3 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mAllowInDataUsageSave.add(pkgname3);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case '\n':
                                if (allowOverrideAppRestrictions2) {
                                    String pkgname4 = parser.getAttributeValue(null, "package");
                                    if (pkgname4 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mAllowUnthrottledLocation.add(pkgname4);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 11:
                                if (allowOverrideAppRestrictions2) {
                                    String pkgname5 = parser.getAttributeValue(null, "package");
                                    String attributionTag = parser.getAttributeValue(null, "attributionTag");
                                    if (pkgname5 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        ArraySet<String> tags = this.mAllowIgnoreLocationSettings.get(pkgname5);
                                        if (tags == null || !tags.isEmpty()) {
                                            if (tags == null) {
                                                tags = new ArraySet<>(1);
                                                this.mAllowIgnoreLocationSettings.put(pkgname5, tags);
                                            }
                                            if (!"*".equals(attributionTag)) {
                                                if ("null".equals(attributionTag)) {
                                                    attributionTag = null;
                                                }
                                                tags.add(attributionTag);
                                            }
                                        }
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case '\f':
                                if (allowImplicitBroadcasts2) {
                                    String action = parser.getAttributeValue(null, "action");
                                    if (action == null) {
                                        Slog.w(TAG, "<" + name + "> without action in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mAllowImplicitBroadcasts.add(action);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case '\r':
                                if (allowAppConfigs) {
                                    String pkgname6 = parser.getAttributeValue(null, "package");
                                    if (pkgname6 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mLinkedApps.add(pkgname6);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 14:
                                if (allowAppConfigs) {
                                    String pkgname7 = parser.getAttributeValue(null, "package");
                                    String clsname = parser.getAttributeValue(null, "class");
                                    if (pkgname7 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else if (clsname == null) {
                                        Slog.w(TAG, "<" + name + "> without class in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mDefaultVrComponents.add(new ComponentName(pkgname7, clsname));
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 15:
                                readComponentOverrides(parser, permFile);
                                break;
                            case 16:
                                if (allowFeatures) {
                                    String serviceName = parser.getAttributeValue(null, "service");
                                    if (serviceName == null) {
                                        Slog.w(TAG, "<" + name + "> without service in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        ComponentName cn = ComponentName.unflattenFromString(serviceName);
                                        if (cn == null) {
                                            Slog.w(TAG, "<" + name + "> with invalid service name " + serviceName + " in " + permFile + " at " + parser.getPositionDescription());
                                        } else {
                                            this.mBackupTransportWhitelist.add(cn);
                                        }
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 17:
                                if (allowAppConfigs) {
                                    String pkgname8 = parser.getAttributeValue(null, "package");
                                    String carrierPkgname = parser.getAttributeValue(null, "carrierAppPackage");
                                    if (pkgname8 != null && carrierPkgname != null) {
                                        int addedInSdk = -1;
                                        String addedInSdkStr = parser.getAttributeValue(null, "addedInSdk");
                                        if (!TextUtils.isEmpty(addedInSdkStr)) {
                                            try {
                                                addedInSdk = Integer.parseInt(addedInSdkStr);
                                            } catch (NumberFormatException e7) {
                                                Slog.w(TAG, "<" + name + "> addedInSdk not an integer in " + permFile + " at " + parser.getPositionDescription());
                                                XmlUtils.skipCurrentTag(parser);
                                                type = type2;
                                                permReader2 = permReader4;
                                                str2 = str4;
                                                lowRam = lowRam2;
                                                allowOverrideAppRestrictions = allowOverrideAppRestrictions2;
                                                allowImplicitBroadcasts = allowImplicitBroadcasts2;
                                                allowAssociations = allowAssociations2;
                                            }
                                        }
                                        List<CarrierAssociatedAppEntry> associatedPkgs = this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.get(carrierPkgname);
                                        if (associatedPkgs == null) {
                                            associatedPkgs = new ArrayList();
                                            this.mDisabledUntilUsedPreinstalledCarrierAssociatedApps.put(carrierPkgname, associatedPkgs);
                                        }
                                        associatedPkgs.add(new CarrierAssociatedAppEntry(pkgname8, addedInSdk));
                                    }
                                    Slog.w(TAG, "<" + name + "> without package or carrierAppPackage in " + permFile + " at " + parser.getPositionDescription());
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                            case 18:
                                if (allowAppConfigs) {
                                    String pkgname9 = parser.getAttributeValue(null, "package");
                                    if (pkgname9 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mDisabledUntilUsedPreinstalledCarrierApps.add(pkgname9);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 19:
                                if (!allowPrivappPermissions) {
                                    logNotAllowedInPartition(name, permFile, parser);
                                    XmlUtils.skipCurrentTag(parser);
                                    break;
                                } else {
                                    if (!permFile.toPath().startsWith(Environment.getVendorDirectory().toPath() + "/")) {
                                        if (!permFile.toPath().startsWith(Environment.getOdmDirectory().toPath() + "/")) {
                                            vendor = false;
                                            boolean product = permFile.toPath().startsWith(Environment.getProductDirectory().toPath() + "/");
                                            boolean systemExt = permFile.toPath().startsWith(Environment.getSystemExtDirectory().toPath() + "/");
                                            if (!vendor) {
                                                readPrivAppPermissions(parser, this.mVendorPrivAppPermissions, this.mVendorPrivAppDenyPermissions);
                                            } else if (product) {
                                                readPrivAppPermissions(parser, this.mProductPrivAppPermissions, this.mProductPrivAppDenyPermissions);
                                            } else if (systemExt) {
                                                readPrivAppPermissions(parser, this.mSystemExtPrivAppPermissions, this.mSystemExtPrivAppDenyPermissions);
                                            } else {
                                                readPrivAppPermissions(parser, this.mPrivAppPermissions, this.mPrivAppDenyPermissions);
                                            }
                                            break;
                                        }
                                    }
                                    vendor = true;
                                    boolean product2 = permFile.toPath().startsWith(Environment.getProductDirectory().toPath() + "/");
                                    boolean systemExt2 = permFile.toPath().startsWith(Environment.getSystemExtDirectory().toPath() + "/");
                                    if (!vendor) {
                                    }
                                }
                            case 20:
                                if (!allowOemPermissions) {
                                    logNotAllowedInPartition(name, permFile, parser);
                                    XmlUtils.skipCurrentTag(parser);
                                    break;
                                } else {
                                    readOemPermissions(parser);
                                    break;
                                }
                            case 21:
                                if (allowApiWhitelisting) {
                                    String pkgname10 = parser.getAttributeValue(null, "package");
                                    if (pkgname10 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mHiddenApiPackageWhitelist.add(pkgname10);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 22:
                                if (allowAssociations2) {
                                    String target = parser.getAttributeValue(null, "target");
                                    if (target == null) {
                                        Slog.w(TAG, "<" + name + "> without target in " + permFile + " at " + parser.getPositionDescription());
                                        XmlUtils.skipCurrentTag(parser);
                                        break;
                                    } else {
                                        String allowed2 = parser.getAttributeValue(null, "allowed");
                                        if (allowed2 == null) {
                                            Slog.w(TAG, "<" + name + "> without allowed in " + permFile + " at " + parser.getPositionDescription());
                                            XmlUtils.skipCurrentTag(parser);
                                            break;
                                        } else {
                                            String target2 = target.intern();
                                            String allowed3 = allowed2.intern();
                                            ArraySet<String> associations = this.mAllowedAssociations.get(target2);
                                            if (associations == null) {
                                                associations = new ArraySet<>();
                                                this.mAllowedAssociations.put(target2, associations);
                                            }
                                            Slog.i(TAG, "Adding association: " + target2 + " <- " + allowed3);
                                            associations.add(allowed3);
                                        }
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 23:
                                String pkgname11 = parser.getAttributeValue(null, "package");
                                if (pkgname11 == null) {
                                    Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                } else {
                                    this.mAppDataIsolationWhitelistedApps.add(pkgname11);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 24:
                                String pkgname12 = parser.getAttributeValue(null, "package");
                                if (pkgname12 == null) {
                                    Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                } else {
                                    this.mBugreportWhitelistedPackages.add(pkgname12);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 25:
                                readInstallInUserType(parser, this.mPackageToUserTypeWhitelist, this.mPackageToUserTypeBlacklist);
                                break;
                            case 26:
                                String namespace = TextUtils.safeIntern(parser.getAttributeValue(null, Settings.EXTRA_NAMESPACE));
                                String actorName = parser.getAttributeValue(null, "name");
                                String pkgName = TextUtils.safeIntern(parser.getAttributeValue(null, "package"));
                                if (TextUtils.isEmpty(namespace)) {
                                    Slog.wtf(TAG, "<" + name + "> without namespace in " + permFile + " at " + parser.getPositionDescription());
                                } else if (TextUtils.isEmpty(actorName)) {
                                    Slog.wtf(TAG, "<" + name + "> without actor name in " + permFile + " at " + parser.getPositionDescription());
                                } else if (TextUtils.isEmpty(pkgName)) {
                                    Slog.wtf(TAG, "<" + name + "> without package name in " + permFile + " at " + parser.getPositionDescription());
                                } else if ("android".equalsIgnoreCase(namespace)) {
                                    throw new IllegalStateException("Defining " + actorName + " as " + pkgName + " for the android namespace is not allowed");
                                } else {
                                    if (this.mNamedActors == null) {
                                        this.mNamedActors = new ArrayMap();
                                    }
                                    Map<String, String> nameToPkgMap = this.mNamedActors.get(namespace);
                                    if (nameToPkgMap == null) {
                                        nameToPkgMap = new ArrayMap();
                                        this.mNamedActors.put(namespace, nameToPkgMap);
                                    } else if (nameToPkgMap.containsKey(actorName)) {
                                        String existing = nameToPkgMap.get(actorName);
                                        throw new IllegalStateException("Duplicate actor definition for " + namespace + "/" + actorName + "; defined as both " + existing + " and " + pkgName);
                                    }
                                    nameToPkgMap.put(actorName, pkgName);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 27:
                                if (allowAll) {
                                    String pkgName2 = parser.getAttributeValue(null, "package");
                                    if (pkgName2 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else if (!TextUtils.isEmpty(this.mOverlayConfigSignaturePackage)) {
                                        throw new IllegalStateException("Reference signature package defined as both " + this.mOverlayConfigSignaturePackage + " and " + pkgName2);
                                    } else {
                                        this.mOverlayConfigSignaturePackage = pkgName2.intern();
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 28:
                                String pkgname13 = parser.getAttributeValue(null, "package");
                                if (pkgname13 == null) {
                                    Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                } else {
                                    this.mRollbackWhitelistedPackages.add(pkgname13);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 29:
                                if (allowAppConfigs) {
                                    String pkgname14 = parser.getAttributeValue(null, "package");
                                    boolean isModulesInstaller = XmlUtils.readBooleanAttribute(parser, "isModulesInstaller", false);
                                    if (pkgname14 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    } else {
                                        this.mWhitelistedStagedInstallers.add(pkgname14);
                                    }
                                    if (isModulesInstaller) {
                                        if (this.mModulesInstallerPackageName != null) {
                                            throw new IllegalStateException("Multiple modules installers");
                                        }
                                        this.mModulesInstallerPackageName = pkgname14;
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            case 30:
                                if (allowVendorApex) {
                                    String pkgName3 = parser.getAttributeValue(null, "package");
                                    String installerPkgName = parser.getAttributeValue(null, "installerPackage");
                                    if (pkgName3 == null) {
                                        Slog.w(TAG, "<" + name + "> without package in " + permFile + " at " + parser.getPositionDescription());
                                    }
                                    if (installerPkgName == null) {
                                        Slog.w(TAG, "<" + name + "> without installerPackage in " + permFile + " at " + parser.getPositionDescription());
                                    }
                                    if (pkgName3 != null && installerPkgName != null) {
                                        this.mAllowedVendorApexes.put(pkgName3, installerPkgName);
                                    }
                                } else {
                                    logNotAllowedInPartition(name, permFile, parser);
                                }
                                XmlUtils.skipCurrentTag(parser);
                                break;
                            default:
                                Slog.w(TAG, "Tag " + name + " is unknown in " + permFile + " at " + parser.getPositionDescription());
                                XmlUtils.skipCurrentTag(parser);
                                break;
                        }
                        type = type2;
                        permReader2 = permReader4;
                        str2 = str4;
                        lowRam = lowRam2;
                        allowOverrideAppRestrictions = allowOverrideAppRestrictions2;
                        allowImplicitBroadcasts = allowImplicitBroadcasts2;
                        allowAssociations = allowAssociations2;
                    }
                }
            }
        } catch (FileNotFoundException e8) {
            Slog.w(TAG, "Couldn't find or open permissions file " + permFile);
        }
    }

    private void addFeature(String name, int version) {
        FeatureInfo fi = this.mAvailableFeatures.get(name);
        if (fi == null) {
            FeatureInfo fi2 = new FeatureInfo();
            fi2.name = name;
            fi2.version = version;
            this.mAvailableFeatures.put(name, fi2);
            return;
        }
        fi.version = Math.max(fi.version, version);
    }

    private void removeFeature(String name) {
        if (this.mAvailableFeatures.remove(name) != null) {
            Slog.d(TAG, "Removed unavailable feature " + name);
        }
    }

    void readPermission(XmlPullParser parser, String name) throws IOException, XmlPullParserException {
        if (this.mPermissions.containsKey(name)) {
            throw new IllegalStateException("Duplicate permission definition for " + name);
        }
        boolean perUser = XmlUtils.readBooleanAttribute(parser, "perUser", false);
        PermissionEntry perm = new PermissionEntry(name, perUser);
        this.mPermissions.put(name, perm);
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        String tagName = parser.getName();
                        if ("group".equals(tagName)) {
                            String gidStr = parser.getAttributeValue(null, "gid");
                            if (gidStr != null) {
                                int gid = Process.getGidForName(gidStr);
                                perm.gids = ArrayUtils.appendInt(perm.gids, gid);
                            } else {
                                Slog.w(TAG, "<group> without gid at " + parser.getPositionDescription());
                            }
                        }
                        XmlUtils.skipCurrentTag(parser);
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    private void readPrivAppPermissions(XmlPullParser parser, ArrayMap<String, ArraySet<String>> grantMap, ArrayMap<String, ArraySet<String>> denyMap) throws IOException, XmlPullParserException {
        String packageName = parser.getAttributeValue(null, "package");
        if (TextUtils.isEmpty(packageName)) {
            Slog.w(TAG, "package is required for <privapp-permissions> in " + parser.getPositionDescription());
            return;
        }
        ArraySet<String> permissions = grantMap.get(packageName);
        if (permissions == null) {
            permissions = new ArraySet<>();
        }
        ArraySet<String> denyPermissions = denyMap.get(packageName);
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            String name = parser.getName();
            if ("permission".equals(name)) {
                String permName = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(permName)) {
                    Slog.w(TAG, "name is required for <permission> in " + parser.getPositionDescription());
                } else {
                    permissions.add(permName);
                }
            } else if ("deny-permission".equals(name)) {
                String permName2 = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(permName2)) {
                    Slog.w(TAG, "name is required for <deny-permission> in " + parser.getPositionDescription());
                } else {
                    if (denyPermissions == null) {
                        denyPermissions = new ArraySet<>();
                    }
                    denyPermissions.add(permName2);
                }
            }
        }
        grantMap.put(packageName, permissions);
        if (denyPermissions != null) {
            denyMap.put(packageName, denyPermissions);
        }
    }

    private void readInstallInUserType(XmlPullParser parser, Map<String, Set<String>> doInstallMap, Map<String, Set<String>> nonInstallMap) throws IOException, XmlPullParserException {
        String packageName = parser.getAttributeValue(null, "package");
        if (TextUtils.isEmpty(packageName)) {
            Slog.w(TAG, "package is required for <install-in-user-type> in " + parser.getPositionDescription());
            return;
        }
        Set<String> userTypesYes = doInstallMap.get(packageName);
        Set<String> userTypesNo = nonInstallMap.get(packageName);
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            String name = parser.getName();
            if ("install-in".equals(name)) {
                String userType = parser.getAttributeValue(null, "user-type");
                if (TextUtils.isEmpty(userType)) {
                    Slog.w(TAG, "user-type is required for <install-in-user-type> in " + parser.getPositionDescription());
                } else {
                    if (userTypesYes == null) {
                        userTypesYes = new ArraySet<>();
                        doInstallMap.put(packageName, userTypesYes);
                    }
                    userTypesYes.add(userType);
                }
            } else if ("do-not-install-in".equals(name)) {
                String userType2 = parser.getAttributeValue(null, "user-type");
                if (TextUtils.isEmpty(userType2)) {
                    Slog.w(TAG, "user-type is required for <install-in-user-type> in " + parser.getPositionDescription());
                } else {
                    if (userTypesNo == null) {
                        userTypesNo = new ArraySet();
                        nonInstallMap.put(packageName, userTypesNo);
                    }
                    userTypesNo.add(userType2);
                }
            } else {
                Slog.w(TAG, "unrecognized tag in <install-in-user-type> in " + parser.getPositionDescription());
            }
        }
    }

    void readOemPermissions(XmlPullParser parser) throws IOException, XmlPullParserException {
        String packageName = parser.getAttributeValue(null, "package");
        if (TextUtils.isEmpty(packageName)) {
            Slog.w(TAG, "package is required for <oem-permissions> in " + parser.getPositionDescription());
            return;
        }
        ArrayMap<String, Boolean> permissions = this.mOemPermissions.get(packageName);
        if (permissions == null) {
            permissions = new ArrayMap<>();
        }
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            String name = parser.getName();
            if ("permission".equals(name)) {
                String permName = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(permName)) {
                    Slog.w(TAG, "name is required for <permission> in " + parser.getPositionDescription());
                } else {
                    permissions.put(permName, Boolean.TRUE);
                }
            } else if ("deny-permission".equals(name)) {
                String permName2 = parser.getAttributeValue(null, "name");
                if (TextUtils.isEmpty(permName2)) {
                    Slog.w(TAG, "name is required for <deny-permission> in " + parser.getPositionDescription());
                } else {
                    permissions.put(permName2, Boolean.FALSE);
                }
            }
        }
        this.mOemPermissions.put(packageName, permissions);
    }

    private void readSplitPermission(XmlPullParser parser, File permFile) throws IOException, XmlPullParserException {
        String splitPerm = parser.getAttributeValue(null, "name");
        if (splitPerm != null) {
            String targetSdkStr = parser.getAttributeValue(null, "targetSdk");
            int targetSdk = 10001;
            if (!TextUtils.isEmpty(targetSdkStr)) {
                try {
                    targetSdk = Integer.parseInt(targetSdkStr);
                } catch (NumberFormatException e) {
                    Slog.w(TAG, "<split-permission> targetSdk not an integer in " + permFile + " at " + parser.getPositionDescription());
                    XmlUtils.skipCurrentTag(parser);
                    return;
                }
            }
            int depth = parser.getDepth();
            List<String> newPermissions = new ArrayList<>();
            while (XmlUtils.nextElementWithin(parser, depth)) {
                String name = parser.getName();
                if ("new-permission".equals(name)) {
                    String newName = parser.getAttributeValue(null, "name");
                    if (TextUtils.isEmpty(newName)) {
                        Slog.w(TAG, "name is required for <new-permission> in " + parser.getPositionDescription());
                    } else {
                        newPermissions.add(newName);
                    }
                } else {
                    XmlUtils.skipCurrentTag(parser);
                }
            }
            if (!newPermissions.isEmpty()) {
                this.mSplitPermissions.add(new PermissionManager.SplitPermissionInfo(splitPerm, newPermissions, targetSdk));
                return;
            }
            return;
        }
        Slog.w(TAG, "<split-permission> without name in " + permFile + " at " + parser.getPositionDescription());
        XmlUtils.skipCurrentTag(parser);
    }

    private void readComponentOverrides(XmlPullParser parser, File permFile) throws IOException, XmlPullParserException {
        String pkgname = parser.getAttributeValue(null, "package");
        if (pkgname == null) {
            Slog.w(TAG, "<component-override> without package in " + permFile + " at " + parser.getPositionDescription());
            return;
        }
        String pkgname2 = pkgname.intern();
        int depth = parser.getDepth();
        while (XmlUtils.nextElementWithin(parser, depth)) {
            if (CardEmulation.EXTRA_SERVICE_COMPONENT.equals(parser.getName())) {
                String clsname = parser.getAttributeValue(null, "class");
                String enabled = parser.getAttributeValue(null, "enabled");
                if (clsname == null) {
                    Slog.w(TAG, "<component> without class in " + permFile + " at " + parser.getPositionDescription());
                    return;
                } else if (enabled == null) {
                    Slog.w(TAG, "<component> without enabled in " + permFile + " at " + parser.getPositionDescription());
                    return;
                } else {
                    if (clsname.startsWith(MediaMetrics.SEPARATOR)) {
                        clsname = pkgname2 + clsname;
                    }
                    String clsname2 = clsname.intern();
                    ArrayMap<String, Boolean> componentEnabledStates = this.mPackageComponentEnabledState.get(pkgname2);
                    if (componentEnabledStates == null) {
                        componentEnabledStates = new ArrayMap<>();
                        this.mPackageComponentEnabledState.put(pkgname2, componentEnabledStates);
                    }
                    componentEnabledStates.put(clsname2, Boolean.valueOf(!"false".equals(enabled)));
                }
            }
        }
    }

    private void readPublicNativeLibrariesList() {
        readPublicLibrariesListFile(new File("/vendor/etc/public.libraries.txt"));
        String[] dirs = {"/system/etc", "/system_ext/etc", "/product/etc"};
        for (String dir : dirs) {
            File[] files = new File(dir).listFiles();
            if (files == null) {
                Slog.w(TAG, "Public libraries file folder missing: " + dir);
            } else {
                for (File f : files) {
                    String name = f.getName();
                    if (name.startsWith("public.libraries-") && name.endsWith(".txt")) {
                        readPublicLibrariesListFile(f);
                    }
                }
            }
        }
    }

    private void readPublicLibrariesListFile(File listFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(listFile));
            while (true) {
                String line = br.readLine();
                if (line != null) {
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        String soname = line.trim().split(" ")[0];
                        SharedLibraryEntry entry = new SharedLibraryEntry(soname, soname, new String[0], true);
                        this.mSharedLibraries.put(entry.name, entry);
                    }
                } else {
                    br.close();
                    return;
                }
            }
        } catch (IOException e) {
            Slog.w(TAG, "Failed to read public libraries file " + listFile, e);
        }
    }

    private static boolean isSystemProcess() {
        return Process.myUid() == 1000;
    }
}
