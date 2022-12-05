package android.content.pm.parsing;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureGroupInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.parsing.component.ParsedActivity;
import android.content.pm.parsing.component.ParsedAttribution;
import android.content.pm.parsing.component.ParsedComponent;
import android.content.pm.parsing.component.ParsedInstrumentation;
import android.content.pm.parsing.component.ParsedIntentInfo;
import android.content.pm.parsing.component.ParsedMainComponent;
import android.content.pm.parsing.component.ParsedPermission;
import android.content.pm.parsing.component.ParsedPermissionGroup;
import android.content.pm.parsing.component.ParsedProcess;
import android.content.pm.parsing.component.ParsedProvider;
import android.content.pm.parsing.component.ParsedService;
import android.content.pm.parsing.component.ParsedUsesPermission;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.proto.ProtoStream;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.CollectionUtils;
import com.android.internal.util.Parcelling;
import java.security.PublicKey;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
/* loaded from: classes.dex */
public class ParsingPackageImpl implements ParsingPackage, Parcelable {
    private static final String TAG = "PackageImpl";
    protected List<ParsedActivity> activities;
    protected List<String> adoptPermissions;
    private Boolean anyDensity;
    private String appComponentFactory;
    private List<ParsedAttribution> attributions;
    private int autoRevokePermissions;
    private String backupAgentName;
    private int banner;
    private int baseRevisionCode;
    private int category;
    private String classLoaderName;
    private String className;
    private int compatibleWidthLimitDp;
    private int compileSdkVersion;
    private String compileSdkVersionCodeName;
    private List<ConfigurationInfo> configPreferences;
    private int dataExtractionRules;
    private int descriptionRes;
    private List<FeatureGroupInfo> featureGroups;
    private int fullBackupContent;
    private int gwpAsanMode;
    private int iconRes;
    private List<String> implicitPermissions;
    private int installLocation;
    protected List<ParsedInstrumentation> instrumentations;
    private Map<String, ArraySet<PublicKey>> keySetMapping;
    private int labelRes;
    private int largestWidthLimitDp;
    private List<String> libraryNames;
    private int logo;
    protected String mBaseApkPath;
    private long mBooleans;
    private long mLongVersionCode;
    protected String mPath;
    private Map<String, PackageManager.Property> mProperties;
    private UUID mStorageUuid;
    private String manageSpaceActivityName;
    private float maxAspectRatio;
    private int memtagMode;
    private Bundle metaData;
    private ArraySet<String> mimeGroups;
    private float minAspectRatio;
    private SparseIntArray minExtensionVersions;
    private int minSdkVersion;
    private int nativeHeapZeroInitialized;
    private int networkSecurityConfigRes;
    private CharSequence nonLocalizedLabel;
    protected List<String> originalPackages;
    private String overlayCategory;
    private int overlayPriority;
    private String overlayTarget;
    private String overlayTargetName;
    private Map<String, String> overlayables;
    protected String packageName;
    private String permission;
    protected List<ParsedPermissionGroup> permissionGroups;
    protected List<ParsedPermission> permissions;
    private List<Pair<String, ParsedIntentInfo>> preferredActivityFilters;
    private String processName;
    private Map<String, ParsedProcess> processes;
    protected List<String> protectedBroadcasts;
    protected List<ParsedProvider> providers;
    private List<Intent> queriesIntents;
    private List<String> queriesPackages;
    private Set<String> queriesProviders;
    private String realPackage;
    protected List<ParsedActivity> receivers;
    private List<FeatureInfo> reqFeatures;
    private Boolean requestRawExternalStorageAccess;
    @Deprecated
    protected List<String> requestedPermissions;
    private String requiredAccountType;
    private int requiresSmallestWidthDp;
    private Boolean resizeable;
    private Boolean resizeableActivity;
    private byte[] restrictUpdateHash;
    private String restrictedAccountType;
    private int roundIconRes;
    protected List<ParsedService> services;
    private String sharedUserId;
    private int sharedUserLabel;
    private PackageParser.SigningDetails signingDetails;
    private String[] splitClassLoaderNames;
    protected String[] splitCodePaths;
    private SparseArray<int[]> splitDependencies;
    private int[] splitFlags;
    private String[] splitNames;
    private int[] splitRevisionCodes;
    private String staticSharedLibName;
    private long staticSharedLibVersion;
    private Boolean supportsExtraLargeScreens;
    private Boolean supportsLargeScreens;
    private Boolean supportsNormalScreens;
    private Boolean supportsSmallScreens;
    private int targetSandboxVersion;
    private int targetSdkVersion;
    private String taskAffinity;
    private int theme;
    private int uiOptions;
    private Set<String> upgradeKeySets;
    protected List<String> usesLibraries;
    protected List<String> usesNativeLibraries;
    protected List<String> usesOptionalLibraries;
    protected List<String> usesOptionalNativeLibraries;
    private List<ParsedUsesPermission> usesPermissions;
    private List<String> usesStaticLibraries;
    private String[][] usesStaticLibrariesCertDigests;
    private long[] usesStaticLibrariesVersions;
    protected int versionCode;
    protected int versionCodeMajor;
    private String versionName;
    protected String volumeUuid;
    private String zygotePreloadName;
    public static Parcelling.BuiltIn.ForBoolean sForBoolean = (Parcelling.BuiltIn.ForBoolean) Parcelling.Cache.getOrCreate(Parcelling.BuiltIn.ForBoolean.class);
    public static Parcelling.BuiltIn.ForInternedString sForInternedString = (Parcelling.BuiltIn.ForInternedString) Parcelling.Cache.getOrCreate(Parcelling.BuiltIn.ForInternedString.class);
    public static Parcelling.BuiltIn.ForInternedStringArray sForInternedStringArray = (Parcelling.BuiltIn.ForInternedStringArray) Parcelling.Cache.getOrCreate(Parcelling.BuiltIn.ForInternedStringArray.class);
    public static Parcelling.BuiltIn.ForInternedStringList sForInternedStringList = (Parcelling.BuiltIn.ForInternedStringList) Parcelling.Cache.getOrCreate(Parcelling.BuiltIn.ForInternedStringList.class);
    public static Parcelling.BuiltIn.ForInternedStringValueMap sForInternedStringValueMap = (Parcelling.BuiltIn.ForInternedStringValueMap) Parcelling.Cache.getOrCreate(Parcelling.BuiltIn.ForInternedStringValueMap.class);
    public static Parcelling.BuiltIn.ForStringSet sForStringSet = (Parcelling.BuiltIn.ForStringSet) Parcelling.Cache.getOrCreate(Parcelling.BuiltIn.ForStringSet.class);
    public static Parcelling.BuiltIn.ForInternedStringSet sForInternedStringSet = (Parcelling.BuiltIn.ForInternedStringSet) Parcelling.Cache.getOrCreate(Parcelling.BuiltIn.ForInternedStringSet.class);
    protected static ParsedIntentInfo.StringPairListParceler sForIntentInfoPairs = (ParsedIntentInfo.StringPairListParceler) Parcelling.Cache.getOrCreate(ParsedIntentInfo.StringPairListParceler.class);
    private static final Comparator<ParsedMainComponent> ORDER_COMPARATOR = ParsingPackageImpl$$ExternalSyntheticLambda0.INSTANCE;
    public static final Parcelable.Creator<ParsingPackageImpl> CREATOR = new Parcelable.Creator<ParsingPackageImpl>() { // from class: android.content.pm.parsing.ParsingPackageImpl.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ParsingPackageImpl mo3559createFromParcel(Parcel source) {
            return new ParsingPackageImpl(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ParsingPackageImpl[] mo3560newArray(int size) {
            return new ParsingPackageImpl[size];
        }
    };

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: asSplit */
    public /* bridge */ /* synthetic */ ParsingPackage mo895asSplit(String[] strArr, String[] strArr2, int[] iArr, SparseArray sparseArray) {
        return mo895asSplit(strArr, strArr2, iArr, (SparseArray<int[]>) sparseArray);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setProcesses */
    public /* bridge */ /* synthetic */ ParsingPackage mo965setProcesses(Map map) {
        return mo965setProcesses((Map<String, ParsedProcess>) map);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setUpgradeKeySets */
    public /* bridge */ /* synthetic */ ParsingPackage mo1001setUpgradeKeySets(Set set) {
        return mo1001setUpgradeKeySets((Set<String>) set);
    }

    /* loaded from: classes.dex */
    protected static class Booleans {
        private static final long ALLOW_AUDIO_PLAYBACK_CAPTURE = 2147483648L;
        private static final long ALLOW_BACKUP = 4;
        private static final long ALLOW_CLEAR_USER_DATA = 2048;
        private static final long ALLOW_CLEAR_USER_DATA_ON_FAILED_RESTORE = 1073741824;
        private static final long ALLOW_NATIVE_HEAP_POINTER_TAGGING = 68719476736L;
        private static final long ALLOW_TASK_REPARENTING = 1024;
        private static final long ATTRIBUTIONS_ARE_USER_VISIBLE = 140737488355328L;
        private static final long BACKUP_IN_FOREGROUND = 16777216;
        private static final long BASE_HARDWARE_ACCELERATED = 2;
        private static final long CANT_SAVE_STATE = 34359738368L;
        private static final long CROSS_PROFILE = 8796093022208L;
        private static final long DEBUGGABLE = 128;
        private static final long DEFAULT_TO_DEVICE_PROTECTED_STORAGE = 67108864;
        private static final long DIRECT_BOOT_AWARE = 134217728;
        private static final long DISALLOW_PROFILING = 35184372088832L;
        private static final long ENABLED = 17592186044416L;
        private static final long EXTERNAL_STORAGE = 1;
        private static final long EXTRACT_NATIVE_LIBS = 131072;
        private static final long FORCE_QUERYABLE = 4398046511104L;
        private static final long FULL_BACKUP_ONLY = 32;
        private static final long GAME = 262144;
        private static final long HAS_CODE = 512;
        private static final long HAS_DOMAIN_URLS = 4194304;
        private static final long HAS_FRAGILE_USER_DATA = 17179869184L;
        private static final long ISOLATED_SPLIT_LOADING = 2097152;
        private static final long KILL_AFTER_RESTORE = 8;
        private static final long LARGE_HEAP = 4096;
        private static final long MULTI_ARCH = 65536;
        private static final long OVERLAY = 1048576;
        private static final long OVERLAY_IS_STATIC = 549755813888L;
        private static final long PARTIALLY_DIRECT_BOOT_AWARE = 268435456;
        private static final long PERSISTENT = 64;
        private static final long PRESERVE_LEGACY_EXTERNAL_STORAGE = 137438953472L;
        private static final long PROFILEABLE_BY_SHELL = 8388608;
        private static final long REQUEST_FOREGROUND_SERVICE_EXEMPTION = 70368744177664L;
        private static final long REQUEST_LEGACY_EXTERNAL_STORAGE = 4294967296L;
        private static final long REQUIRED_FOR_ALL_USERS = 274877906944L;
        private static final long RESIZEABLE_ACTIVITY_VIA_SDK_VERSION = 536870912;
        private static final long RESTORE_ANY_VERSION = 16;
        private static final long STATIC_SHARED_LIBRARY = 524288;
        private static final long SUPPORTS_RTL = 16384;
        private static final long TEST_ONLY = 32768;
        private static final long USES_CLEARTEXT_TRAFFIC = 8192;
        private static final long USES_NON_SDK_API = 8589934592L;
        private static final long USE_32_BIT_ABI = 1099511627776L;
        private static final long USE_EMBEDDED_DEX = 33554432;
        private static final long VISIBLE_TO_INSTANT_APPS = 2199023255552L;
        private static final long VM_SAFE_MODE = 256;

        /* loaded from: classes.dex */
        public @interface Values {
        }

        protected Booleans() {
        }
    }

    private ParsingPackageImpl setBoolean(long flag, boolean value) {
        if (value) {
            this.mBooleans |= flag;
        } else {
            this.mBooleans &= ~flag;
        }
        return this;
    }

    private boolean getBoolean(long flag) {
        return (this.mBooleans & flag) != 0;
    }

    public ParsingPackageImpl(String packageName, String baseApkPath, String path, TypedArray manifestArray) {
        this.overlayables = Collections.emptyMap();
        this.libraryNames = Collections.emptyList();
        this.usesLibraries = Collections.emptyList();
        this.usesOptionalLibraries = Collections.emptyList();
        this.usesNativeLibraries = Collections.emptyList();
        this.usesOptionalNativeLibraries = Collections.emptyList();
        this.usesStaticLibraries = Collections.emptyList();
        this.configPreferences = Collections.emptyList();
        this.reqFeatures = Collections.emptyList();
        this.featureGroups = Collections.emptyList();
        this.originalPackages = Collections.emptyList();
        this.adoptPermissions = Collections.emptyList();
        this.requestedPermissions = Collections.emptyList();
        this.usesPermissions = Collections.emptyList();
        this.implicitPermissions = Collections.emptyList();
        this.upgradeKeySets = Collections.emptySet();
        this.keySetMapping = Collections.emptyMap();
        this.protectedBroadcasts = Collections.emptyList();
        this.activities = Collections.emptyList();
        this.receivers = Collections.emptyList();
        this.services = Collections.emptyList();
        this.providers = Collections.emptyList();
        this.attributions = Collections.emptyList();
        this.permissions = Collections.emptyList();
        this.permissionGroups = Collections.emptyList();
        this.instrumentations = Collections.emptyList();
        this.preferredActivityFilters = Collections.emptyList();
        this.processes = Collections.emptyMap();
        this.mProperties = Collections.emptyMap();
        this.queriesIntents = Collections.emptyList();
        this.queriesPackages = Collections.emptyList();
        this.queriesProviders = Collections.emptySet();
        this.installLocation = -1;
        this.minSdkVersion = 1;
        this.targetSdkVersion = 0;
        this.mBooleans = 17592186044416L;
        this.packageName = TextUtils.safeIntern(packageName);
        this.mBaseApkPath = baseApkPath;
        this.mPath = path;
        if (manifestArray != null) {
            this.versionCode = manifestArray.getInteger(1, 0);
            this.versionCodeMajor = manifestArray.getInteger(11, 0);
            mo911setBaseRevisionCode(manifestArray.getInteger(5, 0));
            mo1006setVersionName(manifestArray.getNonConfigurationString(2, 0));
            mo917setCompileSdkVersion(manifestArray.getInteger(9, 0));
            setCompileSdkVersionCodename(manifestArray.getNonConfigurationString(10, 0));
            mo937setIsolatedSplitLoading(manifestArray.getBoolean(6, false));
        }
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isSupportsSmallScreens() {
        Boolean bool = this.supportsSmallScreens;
        if (bool == null) {
            return this.targetSdkVersion >= 4;
        }
        return bool.booleanValue();
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isSupportsNormalScreens() {
        Boolean bool = this.supportsNormalScreens;
        return bool == null || bool.booleanValue();
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isSupportsLargeScreens() {
        Boolean bool = this.supportsLargeScreens;
        if (bool == null) {
            return this.targetSdkVersion >= 4;
        }
        return bool.booleanValue();
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isSupportsExtraLargeScreens() {
        Boolean bool = this.supportsExtraLargeScreens;
        if (bool == null) {
            return this.targetSdkVersion >= 9;
        }
        return bool.booleanValue();
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isResizeable() {
        Boolean bool = this.resizeable;
        if (bool == null) {
            return this.targetSdkVersion >= 4;
        }
        return bool.booleanValue();
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isAnyDensity() {
        Boolean bool = this.anyDensity;
        if (bool == null) {
            return this.targetSdkVersion >= 4;
        }
        return bool.booleanValue();
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: sortActivities  reason: collision with other method in class */
    public ParsingPackageImpl mo1011sortActivities() {
        Collections.sort(this.activities, ORDER_COMPARATOR);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: sortReceivers  reason: collision with other method in class */
    public ParsingPackageImpl mo1012sortReceivers() {
        Collections.sort(this.receivers, ORDER_COMPARATOR);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: sortServices  reason: collision with other method in class */
    public ParsingPackageImpl mo1013sortServices() {
        Collections.sort(this.services, ORDER_COMPARATOR);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    public Object hideAsParsed() {
        assignDerivedFields();
        return this;
    }

    private void assignDerivedFields() {
        this.mStorageUuid = StorageManager.convert(this.volumeUuid);
        this.mLongVersionCode = PackageInfo.composeLongVersionCode(this.versionCodeMajor, this.versionCode);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addConfigPreference  reason: collision with other method in class */
    public ParsingPackageImpl mo868addConfigPreference(ConfigurationInfo configPreference) {
        this.configPreferences = CollectionUtils.add(this.configPreferences, configPreference);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addReqFeature  reason: collision with other method in class */
    public ParsingPackageImpl mo885addReqFeature(FeatureInfo reqFeature) {
        this.reqFeatures = CollectionUtils.add(this.reqFeatures, reqFeature);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addFeatureGroup  reason: collision with other method in class */
    public ParsingPackageImpl mo869addFeatureGroup(FeatureGroupInfo featureGroup) {
        this.featureGroups = CollectionUtils.add(this.featureGroups, featureGroup);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addProperty  reason: collision with other method in class */
    public ParsingPackageImpl mo878addProperty(PackageManager.Property property) {
        if (property == null) {
            return this;
        }
        this.mProperties = CollectionUtils.add(this.mProperties, property.getName(), property);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addProtectedBroadcast  reason: collision with other method in class */
    public ParsingPackageImpl mo879addProtectedBroadcast(String protectedBroadcast) {
        if (!this.protectedBroadcasts.contains(protectedBroadcast)) {
            this.protectedBroadcasts = CollectionUtils.add(this.protectedBroadcasts, TextUtils.safeIntern(protectedBroadcast));
        }
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addInstrumentation  reason: collision with other method in class */
    public ParsingPackageImpl mo871addInstrumentation(ParsedInstrumentation instrumentation) {
        this.instrumentations = CollectionUtils.add(this.instrumentations, instrumentation);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addOriginalPackage  reason: collision with other method in class */
    public ParsingPackageImpl mo874addOriginalPackage(String originalPackage) {
        this.originalPackages = CollectionUtils.add(this.originalPackages, originalPackage);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    public ParsingPackage addOverlayable(String overlayableName, String actorName) {
        this.overlayables = CollectionUtils.add(this.overlayables, overlayableName, TextUtils.safeIntern(actorName));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addAdoptPermission  reason: collision with other method in class */
    public ParsingPackageImpl mo866addAdoptPermission(String adoptPermission) {
        this.adoptPermissions = CollectionUtils.add(this.adoptPermissions, TextUtils.safeIntern(adoptPermission));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addPermission  reason: collision with other method in class */
    public ParsingPackageImpl mo875addPermission(ParsedPermission permission) {
        this.permissions = CollectionUtils.add(this.permissions, permission);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addPermissionGroup  reason: collision with other method in class */
    public ParsingPackageImpl mo876addPermissionGroup(ParsedPermissionGroup permissionGroup) {
        this.permissionGroups = CollectionUtils.add(this.permissionGroups, permissionGroup);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addUsesPermission  reason: collision with other method in class */
    public ParsingPackageImpl mo891addUsesPermission(ParsedUsesPermission permission) {
        this.usesPermissions = CollectionUtils.add(this.usesPermissions, permission);
        this.requestedPermissions = CollectionUtils.add(this.requestedPermissions, permission.name);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addImplicitPermission  reason: collision with other method in class */
    public ParsingPackageImpl mo870addImplicitPermission(String permission) {
        this.implicitPermissions = CollectionUtils.add(this.implicitPermissions, TextUtils.safeIntern(permission));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addKeySet  reason: collision with other method in class */
    public ParsingPackageImpl mo872addKeySet(String keySetName, PublicKey publicKey) {
        ArraySet<PublicKey> publicKeys = this.keySetMapping.get(keySetName);
        if (publicKeys == null) {
            publicKeys = new ArraySet<>();
        }
        publicKeys.add(publicKey);
        this.keySetMapping = CollectionUtils.add(this.keySetMapping, keySetName, publicKeys);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addActivity  reason: collision with other method in class */
    public ParsingPackageImpl mo865addActivity(ParsedActivity parsedActivity) {
        this.activities = CollectionUtils.add(this.activities, parsedActivity);
        addMimeGroupsFromComponent(parsedActivity);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addReceiver  reason: collision with other method in class */
    public ParsingPackageImpl mo884addReceiver(ParsedActivity parsedReceiver) {
        this.receivers = CollectionUtils.add(this.receivers, parsedReceiver);
        addMimeGroupsFromComponent(parsedReceiver);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addService  reason: collision with other method in class */
    public ParsingPackageImpl mo886addService(ParsedService parsedService) {
        this.services = CollectionUtils.add(this.services, parsedService);
        addMimeGroupsFromComponent(parsedService);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addProvider  reason: collision with other method in class */
    public ParsingPackageImpl mo880addProvider(ParsedProvider parsedProvider) {
        this.providers = CollectionUtils.add(this.providers, parsedProvider);
        addMimeGroupsFromComponent(parsedProvider);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addAttribution  reason: collision with other method in class */
    public ParsingPackageImpl mo867addAttribution(ParsedAttribution attribution) {
        this.attributions = CollectionUtils.add(this.attributions, attribution);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addLibraryName  reason: collision with other method in class */
    public ParsingPackageImpl mo873addLibraryName(String libraryName) {
        this.libraryNames = CollectionUtils.add(this.libraryNames, TextUtils.safeIntern(libraryName));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addUsesOptionalLibrary  reason: collision with other method in class */
    public ParsingPackageImpl mo889addUsesOptionalLibrary(String libraryName) {
        this.usesOptionalLibraries = CollectionUtils.add(this.usesOptionalLibraries, TextUtils.safeIntern(libraryName));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addUsesLibrary  reason: collision with other method in class */
    public ParsingPackageImpl mo887addUsesLibrary(String libraryName) {
        this.usesLibraries = CollectionUtils.add(this.usesLibraries, TextUtils.safeIntern(libraryName));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: removeUsesOptionalLibrary  reason: collision with other method in class */
    public ParsingPackageImpl mo896removeUsesOptionalLibrary(String libraryName) {
        this.usesOptionalLibraries = CollectionUtils.remove(this.usesOptionalLibraries, libraryName);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addUsesOptionalNativeLibrary  reason: collision with other method in class */
    public final ParsingPackageImpl mo890addUsesOptionalNativeLibrary(String libraryName) {
        this.usesOptionalNativeLibraries = CollectionUtils.add(this.usesOptionalNativeLibraries, TextUtils.safeIntern(libraryName));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addUsesNativeLibrary  reason: collision with other method in class */
    public final ParsingPackageImpl mo888addUsesNativeLibrary(String libraryName) {
        this.usesNativeLibraries = CollectionUtils.add(this.usesNativeLibraries, TextUtils.safeIntern(libraryName));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: removeUsesOptionalNativeLibrary  reason: collision with other method in class */
    public ParsingPackageImpl mo897removeUsesOptionalNativeLibrary(String libraryName) {
        this.usesOptionalNativeLibraries = CollectionUtils.remove(this.usesOptionalNativeLibraries, libraryName);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addUsesStaticLibrary  reason: collision with other method in class */
    public ParsingPackageImpl mo892addUsesStaticLibrary(String libraryName) {
        this.usesStaticLibraries = CollectionUtils.add(this.usesStaticLibraries, TextUtils.safeIntern(libraryName));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addUsesStaticLibraryVersion  reason: collision with other method in class */
    public ParsingPackageImpl mo894addUsesStaticLibraryVersion(long version) {
        this.usesStaticLibrariesVersions = ArrayUtils.appendLong(this.usesStaticLibrariesVersions, version, true);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addUsesStaticLibraryCertDigests  reason: collision with other method in class */
    public ParsingPackageImpl mo893addUsesStaticLibraryCertDigests(String[] certSha256Digests) {
        this.usesStaticLibrariesCertDigests = (String[][]) ArrayUtils.appendElement(String[].class, this.usesStaticLibrariesCertDigests, certSha256Digests, true);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addPreferredActivityFilter  reason: collision with other method in class */
    public ParsingPackageImpl mo877addPreferredActivityFilter(String className, ParsedIntentInfo intentInfo) {
        this.preferredActivityFilters = CollectionUtils.add(this.preferredActivityFilters, Pair.create(className, intentInfo));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addQueriesIntent  reason: collision with other method in class */
    public ParsingPackageImpl mo881addQueriesIntent(Intent intent) {
        this.queriesIntents = CollectionUtils.add(this.queriesIntents, intent);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addQueriesPackage  reason: collision with other method in class */
    public ParsingPackageImpl mo882addQueriesPackage(String packageName) {
        this.queriesPackages = CollectionUtils.add(this.queriesPackages, TextUtils.safeIntern(packageName));
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: addQueriesProvider  reason: collision with other method in class */
    public ParsingPackageImpl mo883addQueriesProvider(String authority) {
        this.queriesProviders = CollectionUtils.add(this.queriesProviders, authority);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSupportsSmallScreens  reason: collision with other method in class */
    public ParsingPackageImpl mo994setSupportsSmallScreens(int supportsSmallScreens) {
        boolean z = true;
        if (supportsSmallScreens == 1) {
            return this;
        }
        if (supportsSmallScreens >= 0) {
            z = false;
        }
        this.supportsSmallScreens = Boolean.valueOf(z);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSupportsNormalScreens  reason: collision with other method in class */
    public ParsingPackageImpl mo992setSupportsNormalScreens(int supportsNormalScreens) {
        boolean z = true;
        if (supportsNormalScreens == 1) {
            return this;
        }
        if (supportsNormalScreens >= 0) {
            z = false;
        }
        this.supportsNormalScreens = Boolean.valueOf(z);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSupportsLargeScreens  reason: collision with other method in class */
    public ParsingPackageImpl mo991setSupportsLargeScreens(int supportsLargeScreens) {
        boolean z = true;
        if (supportsLargeScreens == 1) {
            return this;
        }
        if (supportsLargeScreens >= 0) {
            z = false;
        }
        this.supportsLargeScreens = Boolean.valueOf(z);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSupportsExtraLargeScreens  reason: collision with other method in class */
    public ParsingPackageImpl mo990setSupportsExtraLargeScreens(int supportsExtraLargeScreens) {
        boolean z = true;
        if (supportsExtraLargeScreens == 1) {
            return this;
        }
        if (supportsExtraLargeScreens >= 0) {
            z = false;
        }
        this.supportsExtraLargeScreens = Boolean.valueOf(z);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setResizeable  reason: collision with other method in class */
    public ParsingPackageImpl mo975setResizeable(int resizeable) {
        boolean z = true;
        if (resizeable == 1) {
            return this;
        }
        if (resizeable >= 0) {
            z = false;
        }
        this.resizeable = Boolean.valueOf(z);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAnyDensity  reason: collision with other method in class */
    public ParsingPackageImpl mo904setAnyDensity(int anyDensity) {
        boolean z = true;
        if (anyDensity == 1) {
            return this;
        }
        if (anyDensity >= 0) {
            z = false;
        }
        this.anyDensity = Boolean.valueOf(z);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: asSplit  reason: collision with other method in class */
    public ParsingPackageImpl mo895asSplit(String[] splitNames, String[] splitCodePaths, int[] splitRevisionCodes, SparseArray<int[]> splitDependencies) {
        this.splitNames = splitNames;
        this.splitCodePaths = splitCodePaths;
        this.splitRevisionCodes = splitRevisionCodes;
        this.splitDependencies = splitDependencies;
        int count = splitNames.length;
        this.splitFlags = new int[count];
        this.splitClassLoaderNames = new String[count];
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSplitHasCode  reason: collision with other method in class */
    public ParsingPackageImpl mo986setSplitHasCode(int splitIndex, boolean splitHasCode) {
        int i;
        int[] iArr = this.splitFlags;
        if (splitHasCode) {
            i = iArr[splitIndex] | 4;
        } else {
            i = iArr[splitIndex] & (-5);
        }
        iArr[splitIndex] = i;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSplitClassLoaderName  reason: collision with other method in class */
    public ParsingPackageImpl mo985setSplitClassLoaderName(int splitIndex, String classLoaderName) {
        this.splitClassLoaderNames[splitIndex] = classLoaderName;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRequiredAccountType  reason: collision with other method in class */
    public ParsingPackageImpl mo972setRequiredAccountType(String requiredAccountType) {
        this.requiredAccountType = TextUtils.nullIfEmpty(requiredAccountType);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setOverlayTarget  reason: collision with other method in class */
    public ParsingPackageImpl mo958setOverlayTarget(String overlayTarget) {
        this.overlayTarget = TextUtils.safeIntern(overlayTarget);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setVolumeUuid  reason: collision with other method in class */
    public ParsingPackageImpl mo1009setVolumeUuid(String volumeUuid) {
        this.volumeUuid = TextUtils.safeIntern(volumeUuid);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setStaticSharedLibName  reason: collision with other method in class */
    public ParsingPackageImpl mo987setStaticSharedLibName(String staticSharedLibName) {
        this.staticSharedLibName = TextUtils.safeIntern(staticSharedLibName);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSharedUserId  reason: collision with other method in class */
    public ParsingPackageImpl mo982setSharedUserId(String sharedUserId) {
        this.sharedUserId = TextUtils.safeIntern(sharedUserId);
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setNonLocalizedLabel  reason: collision with other method in class */
    public ParsingPackageImpl mo953setNonLocalizedLabel(CharSequence value) {
        this.nonLocalizedLabel = value == null ? null : value.toString().trim();
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getProcessName() {
        String str = this.processName;
        return str != null ? str : this.packageName;
    }

    public String toString() {
        return "Package{" + Integer.toHexString(System.identityHashCode(this)) + " " + this.packageName + "}";
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    @Deprecated
    public ApplicationInfo toAppInfoWithoutState() {
        ApplicationInfo appInfo = toAppInfoWithoutStateWithoutFlags();
        appInfo.flags = PackageInfoWithoutStateUtils.appInfoFlags(this);
        appInfo.privateFlags = PackageInfoWithoutStateUtils.appInfoPrivateFlags(this);
        appInfo.privateFlagsExt = PackageInfoWithoutStateUtils.appInfoPrivateFlagsExt(this);
        return appInfo;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public ApplicationInfo toAppInfoWithoutStateWithoutFlags() {
        int i;
        ApplicationInfo appInfo = new ApplicationInfo();
        appInfo.appComponentFactory = this.appComponentFactory;
        appInfo.backupAgentName = this.backupAgentName;
        appInfo.banner = this.banner;
        appInfo.category = this.category;
        appInfo.classLoaderName = this.classLoaderName;
        appInfo.className = this.className;
        appInfo.compatibleWidthLimitDp = this.compatibleWidthLimitDp;
        appInfo.compileSdkVersion = this.compileSdkVersion;
        appInfo.compileSdkVersionCodename = this.compileSdkVersionCodeName;
        appInfo.crossProfile = isCrossProfile();
        appInfo.descriptionRes = this.descriptionRes;
        appInfo.enabled = getBoolean(17592186044416L);
        appInfo.fullBackupContent = this.fullBackupContent;
        appInfo.dataExtractionRulesRes = this.dataExtractionRules;
        if (!ParsingPackageUtils.sUseRoundIcon || (i = this.roundIconRes) == 0) {
            i = this.iconRes;
        }
        appInfo.icon = i;
        appInfo.iconRes = this.iconRes;
        appInfo.roundIconRes = this.roundIconRes;
        appInfo.installLocation = this.installLocation;
        appInfo.labelRes = this.labelRes;
        appInfo.largestWidthLimitDp = this.largestWidthLimitDp;
        appInfo.logo = this.logo;
        appInfo.manageSpaceActivityName = this.manageSpaceActivityName;
        appInfo.maxAspectRatio = this.maxAspectRatio;
        appInfo.metaData = this.metaData;
        appInfo.minAspectRatio = this.minAspectRatio;
        appInfo.minSdkVersion = this.minSdkVersion;
        appInfo.name = this.className;
        appInfo.networkSecurityConfigRes = this.networkSecurityConfigRes;
        appInfo.nonLocalizedLabel = this.nonLocalizedLabel;
        appInfo.packageName = this.packageName;
        appInfo.permission = this.permission;
        appInfo.processName = getProcessName();
        appInfo.requiresSmallestWidthDp = this.requiresSmallestWidthDp;
        appInfo.splitClassLoaderNames = this.splitClassLoaderNames;
        appInfo.splitDependencies = this.splitDependencies;
        appInfo.splitNames = this.splitNames;
        appInfo.storageUuid = this.mStorageUuid;
        appInfo.targetSandboxVersion = this.targetSandboxVersion;
        appInfo.targetSdkVersion = this.targetSdkVersion;
        appInfo.taskAffinity = this.taskAffinity;
        appInfo.theme = this.theme;
        appInfo.uiOptions = this.uiOptions;
        appInfo.volumeUuid = this.volumeUuid;
        appInfo.zygotePreloadName = this.zygotePreloadName;
        appInfo.setGwpAsanMode(this.gwpAsanMode);
        appInfo.setMemtagMode(this.memtagMode);
        appInfo.setNativeHeapZeroInitialized(this.nativeHeapZeroInitialized);
        appInfo.setRequestRawExternalStorageAccess(this.requestRawExternalStorageAccess);
        appInfo.setBaseCodePath(this.mBaseApkPath);
        appInfo.setBaseResourcePath(this.mBaseApkPath);
        appInfo.setCodePath(this.mPath);
        appInfo.setResourcePath(this.mPath);
        appInfo.setSplitCodePaths(this.splitCodePaths);
        appInfo.setSplitResourcePaths(this.splitCodePaths);
        appInfo.setVersionCode(this.mLongVersionCode);
        return appInfo;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        sForBoolean.parcel(this.supportsSmallScreens, dest, flags);
        sForBoolean.parcel(this.supportsNormalScreens, dest, flags);
        sForBoolean.parcel(this.supportsLargeScreens, dest, flags);
        sForBoolean.parcel(this.supportsExtraLargeScreens, dest, flags);
        sForBoolean.parcel(this.resizeable, dest, flags);
        sForBoolean.parcel(this.anyDensity, dest, flags);
        dest.writeInt(this.versionCode);
        dest.writeInt(this.versionCodeMajor);
        dest.writeInt(this.baseRevisionCode);
        sForInternedString.parcel(this.versionName, dest, flags);
        dest.writeInt(this.compileSdkVersion);
        dest.writeString(this.compileSdkVersionCodeName);
        sForInternedString.parcel(this.packageName, dest, flags);
        dest.writeString(this.realPackage);
        dest.writeString(this.mBaseApkPath);
        dest.writeString(this.restrictedAccountType);
        dest.writeString(this.requiredAccountType);
        sForInternedString.parcel(this.overlayTarget, dest, flags);
        dest.writeString(this.overlayTargetName);
        dest.writeString(this.overlayCategory);
        dest.writeInt(this.overlayPriority);
        sForInternedStringValueMap.parcel(this.overlayables, dest, flags);
        sForInternedString.parcel(this.staticSharedLibName, dest, flags);
        dest.writeLong(this.staticSharedLibVersion);
        sForInternedStringList.parcel(this.libraryNames, dest, flags);
        sForInternedStringList.parcel(this.usesLibraries, dest, flags);
        sForInternedStringList.parcel(this.usesOptionalLibraries, dest, flags);
        sForInternedStringList.parcel(this.usesNativeLibraries, dest, flags);
        sForInternedStringList.parcel(this.usesOptionalNativeLibraries, dest, flags);
        sForInternedStringList.parcel(this.usesStaticLibraries, dest, flags);
        dest.writeLongArray(this.usesStaticLibrariesVersions);
        String[][] strArr = this.usesStaticLibrariesCertDigests;
        if (strArr == null) {
            dest.writeInt(-1);
        } else {
            dest.writeInt(strArr.length);
            int index = 0;
            while (true) {
                String[][] strArr2 = this.usesStaticLibrariesCertDigests;
                if (index >= strArr2.length) {
                    break;
                }
                dest.writeStringArray(strArr2[index]);
                index++;
            }
        }
        sForInternedString.parcel(this.sharedUserId, dest, flags);
        dest.writeInt(this.sharedUserLabel);
        dest.writeTypedList(this.configPreferences);
        dest.writeTypedList(this.reqFeatures);
        dest.writeTypedList(this.featureGroups);
        dest.writeByteArray(this.restrictUpdateHash);
        dest.writeStringList(this.originalPackages);
        sForInternedStringList.parcel(this.adoptPermissions, dest, flags);
        sForInternedStringList.parcel(this.requestedPermissions, dest, flags);
        dest.writeTypedList(this.usesPermissions);
        sForInternedStringList.parcel(this.implicitPermissions, dest, flags);
        sForStringSet.parcel(this.upgradeKeySets, dest, flags);
        ParsingPackageUtils.writeKeySetMapping(dest, this.keySetMapping);
        sForInternedStringList.parcel(this.protectedBroadcasts, dest, flags);
        dest.writeTypedList(this.activities);
        dest.writeTypedList(this.receivers);
        dest.writeTypedList(this.services);
        dest.writeTypedList(this.providers);
        dest.writeTypedList(this.attributions);
        dest.writeTypedList(this.permissions);
        dest.writeTypedList(this.permissionGroups);
        dest.writeTypedList(this.instrumentations);
        sForIntentInfoPairs.parcel(this.preferredActivityFilters, dest, flags);
        dest.writeMap(this.processes);
        dest.writeBundle(this.metaData);
        sForInternedString.parcel(this.volumeUuid, dest, flags);
        dest.writeParcelable(this.signingDetails, flags);
        dest.writeString(this.mPath);
        dest.writeTypedList(this.queriesIntents, flags);
        sForInternedStringList.parcel(this.queriesPackages, dest, flags);
        sForInternedStringSet.parcel(this.queriesProviders, dest, flags);
        dest.writeString(this.appComponentFactory);
        dest.writeString(this.backupAgentName);
        dest.writeInt(this.banner);
        dest.writeInt(this.category);
        dest.writeString(this.classLoaderName);
        dest.writeString(this.className);
        dest.writeInt(this.compatibleWidthLimitDp);
        dest.writeInt(this.descriptionRes);
        dest.writeInt(this.fullBackupContent);
        dest.writeInt(this.dataExtractionRules);
        dest.writeInt(this.iconRes);
        dest.writeInt(this.installLocation);
        dest.writeInt(this.labelRes);
        dest.writeInt(this.largestWidthLimitDp);
        dest.writeInt(this.logo);
        dest.writeString(this.manageSpaceActivityName);
        dest.writeFloat(this.maxAspectRatio);
        dest.writeFloat(this.minAspectRatio);
        dest.writeInt(this.minSdkVersion);
        dest.writeInt(this.networkSecurityConfigRes);
        dest.writeCharSequence(this.nonLocalizedLabel);
        dest.writeString(this.permission);
        dest.writeString(this.processName);
        dest.writeInt(this.requiresSmallestWidthDp);
        dest.writeInt(this.roundIconRes);
        dest.writeInt(this.targetSandboxVersion);
        dest.writeInt(this.targetSdkVersion);
        dest.writeString(this.taskAffinity);
        dest.writeInt(this.theme);
        dest.writeInt(this.uiOptions);
        dest.writeString(this.zygotePreloadName);
        dest.writeStringArray(this.splitClassLoaderNames);
        dest.writeStringArray(this.splitCodePaths);
        dest.writeSparseArray(this.splitDependencies);
        dest.writeIntArray(this.splitFlags);
        dest.writeStringArray(this.splitNames);
        dest.writeIntArray(this.splitRevisionCodes);
        sForBoolean.parcel(this.resizeableActivity, dest, flags);
        dest.writeInt(this.autoRevokePermissions);
        dest.writeArraySet(this.mimeGroups);
        dest.writeInt(this.gwpAsanMode);
        dest.writeSparseIntArray(this.minExtensionVersions);
        dest.writeLong(this.mBooleans);
        dest.writeMap(this.mProperties);
        dest.writeInt(this.memtagMode);
        dest.writeInt(this.nativeHeapZeroInitialized);
        sForBoolean.parcel(this.requestRawExternalStorageAccess, dest, flags);
    }

    public ParsingPackageImpl(Parcel in) {
        this.overlayables = Collections.emptyMap();
        this.libraryNames = Collections.emptyList();
        this.usesLibraries = Collections.emptyList();
        this.usesOptionalLibraries = Collections.emptyList();
        this.usesNativeLibraries = Collections.emptyList();
        this.usesOptionalNativeLibraries = Collections.emptyList();
        this.usesStaticLibraries = Collections.emptyList();
        this.configPreferences = Collections.emptyList();
        this.reqFeatures = Collections.emptyList();
        this.featureGroups = Collections.emptyList();
        this.originalPackages = Collections.emptyList();
        this.adoptPermissions = Collections.emptyList();
        this.requestedPermissions = Collections.emptyList();
        this.usesPermissions = Collections.emptyList();
        this.implicitPermissions = Collections.emptyList();
        this.upgradeKeySets = Collections.emptySet();
        this.keySetMapping = Collections.emptyMap();
        this.protectedBroadcasts = Collections.emptyList();
        this.activities = Collections.emptyList();
        this.receivers = Collections.emptyList();
        this.services = Collections.emptyList();
        this.providers = Collections.emptyList();
        this.attributions = Collections.emptyList();
        this.permissions = Collections.emptyList();
        this.permissionGroups = Collections.emptyList();
        this.instrumentations = Collections.emptyList();
        this.preferredActivityFilters = Collections.emptyList();
        this.processes = Collections.emptyMap();
        this.mProperties = Collections.emptyMap();
        this.queriesIntents = Collections.emptyList();
        this.queriesPackages = Collections.emptyList();
        this.queriesProviders = Collections.emptySet();
        this.installLocation = -1;
        this.minSdkVersion = 1;
        this.targetSdkVersion = 0;
        this.mBooleans = 17592186044416L;
        ClassLoader boot = Object.class.getClassLoader();
        this.supportsSmallScreens = sForBoolean.mo3468unparcel(in);
        this.supportsNormalScreens = sForBoolean.mo3468unparcel(in);
        this.supportsLargeScreens = sForBoolean.mo3468unparcel(in);
        this.supportsExtraLargeScreens = sForBoolean.mo3468unparcel(in);
        this.resizeable = sForBoolean.mo3468unparcel(in);
        this.anyDensity = sForBoolean.mo3468unparcel(in);
        this.versionCode = in.readInt();
        this.versionCodeMajor = in.readInt();
        this.baseRevisionCode = in.readInt();
        this.versionName = sForInternedString.mo3468unparcel(in);
        this.compileSdkVersion = in.readInt();
        this.compileSdkVersionCodeName = in.readString();
        this.packageName = sForInternedString.mo3468unparcel(in);
        this.realPackage = in.readString();
        this.mBaseApkPath = in.readString();
        this.restrictedAccountType = in.readString();
        this.requiredAccountType = in.readString();
        this.overlayTarget = sForInternedString.mo3468unparcel(in);
        this.overlayTargetName = in.readString();
        this.overlayCategory = in.readString();
        this.overlayPriority = in.readInt();
        this.overlayables = sForInternedStringValueMap.mo3468unparcel(in);
        this.staticSharedLibName = sForInternedString.mo3468unparcel(in);
        this.staticSharedLibVersion = in.readLong();
        this.libraryNames = sForInternedStringList.mo3468unparcel(in);
        this.usesLibraries = sForInternedStringList.mo3468unparcel(in);
        this.usesOptionalLibraries = sForInternedStringList.mo3468unparcel(in);
        this.usesNativeLibraries = sForInternedStringList.mo3468unparcel(in);
        this.usesOptionalNativeLibraries = sForInternedStringList.mo3468unparcel(in);
        this.usesStaticLibraries = sForInternedStringList.mo3468unparcel(in);
        this.usesStaticLibrariesVersions = in.createLongArray();
        int digestsSize = in.readInt();
        if (digestsSize >= 0) {
            this.usesStaticLibrariesCertDigests = new String[digestsSize];
            for (int index = 0; index < digestsSize; index++) {
                this.usesStaticLibrariesCertDigests[index] = sForInternedStringArray.mo3468unparcel(in);
            }
        }
        this.sharedUserId = sForInternedString.mo3468unparcel(in);
        this.sharedUserLabel = in.readInt();
        this.configPreferences = in.createTypedArrayList(ConfigurationInfo.CREATOR);
        this.reqFeatures = in.createTypedArrayList(FeatureInfo.CREATOR);
        this.featureGroups = in.createTypedArrayList(FeatureGroupInfo.CREATOR);
        this.restrictUpdateHash = in.createByteArray();
        this.originalPackages = in.createStringArrayList();
        this.adoptPermissions = sForInternedStringList.mo3468unparcel(in);
        this.requestedPermissions = sForInternedStringList.mo3468unparcel(in);
        this.usesPermissions = in.createTypedArrayList(ParsedUsesPermission.CREATOR);
        this.implicitPermissions = sForInternedStringList.mo3468unparcel(in);
        this.upgradeKeySets = sForStringSet.mo3468unparcel(in);
        this.keySetMapping = ParsingPackageUtils.readKeySetMapping(in);
        this.protectedBroadcasts = sForInternedStringList.mo3468unparcel(in);
        this.activities = in.createTypedArrayList(ParsedActivity.CREATOR);
        this.receivers = in.createTypedArrayList(ParsedActivity.CREATOR);
        this.services = in.createTypedArrayList(ParsedService.CREATOR);
        this.providers = in.createTypedArrayList(ParsedProvider.CREATOR);
        this.attributions = in.createTypedArrayList(ParsedAttribution.CREATOR);
        this.permissions = in.createTypedArrayList(ParsedPermission.CREATOR);
        this.permissionGroups = in.createTypedArrayList(ParsedPermissionGroup.CREATOR);
        this.instrumentations = in.createTypedArrayList(ParsedInstrumentation.CREATOR);
        this.preferredActivityFilters = sForIntentInfoPairs.mo3468unparcel(in);
        this.processes = in.readHashMap(boot);
        this.metaData = in.readBundle(boot);
        this.volumeUuid = sForInternedString.mo3468unparcel(in);
        this.signingDetails = (PackageParser.SigningDetails) in.readParcelable(boot);
        this.mPath = in.readString();
        this.queriesIntents = in.createTypedArrayList(Intent.CREATOR);
        this.queriesPackages = sForInternedStringList.mo3468unparcel(in);
        this.queriesProviders = sForInternedStringSet.mo3468unparcel(in);
        this.appComponentFactory = in.readString();
        this.backupAgentName = in.readString();
        this.banner = in.readInt();
        this.category = in.readInt();
        this.classLoaderName = in.readString();
        this.className = in.readString();
        this.compatibleWidthLimitDp = in.readInt();
        this.descriptionRes = in.readInt();
        this.fullBackupContent = in.readInt();
        this.dataExtractionRules = in.readInt();
        this.iconRes = in.readInt();
        this.installLocation = in.readInt();
        this.labelRes = in.readInt();
        this.largestWidthLimitDp = in.readInt();
        this.logo = in.readInt();
        this.manageSpaceActivityName = in.readString();
        this.maxAspectRatio = in.readFloat();
        this.minAspectRatio = in.readFloat();
        this.minSdkVersion = in.readInt();
        this.networkSecurityConfigRes = in.readInt();
        this.nonLocalizedLabel = in.readCharSequence();
        this.permission = in.readString();
        this.processName = in.readString();
        this.requiresSmallestWidthDp = in.readInt();
        this.roundIconRes = in.readInt();
        this.targetSandboxVersion = in.readInt();
        this.targetSdkVersion = in.readInt();
        this.taskAffinity = in.readString();
        this.theme = in.readInt();
        this.uiOptions = in.readInt();
        this.zygotePreloadName = in.readString();
        this.splitClassLoaderNames = in.createStringArray();
        this.splitCodePaths = in.createStringArray();
        this.splitDependencies = in.readSparseArray(boot);
        this.splitFlags = in.createIntArray();
        this.splitNames = in.createStringArray();
        this.splitRevisionCodes = in.createIntArray();
        this.resizeableActivity = sForBoolean.mo3468unparcel(in);
        this.autoRevokePermissions = in.readInt();
        this.mimeGroups = in.readArraySet(boot);
        this.gwpAsanMode = in.readInt();
        this.minExtensionVersions = in.readSparseIntArray();
        this.mBooleans = in.readLong();
        this.mProperties = in.readHashMap(boot);
        this.memtagMode = in.readInt();
        this.nativeHeapZeroInitialized = in.readInt();
        this.requestRawExternalStorageAccess = sForBoolean.mo3468unparcel(in);
        assignDerivedFields();
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getVersionCode() {
        return this.versionCode;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getVersionCodeMajor() {
        return this.versionCodeMajor;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getBaseRevisionCode() {
        return this.baseRevisionCode;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getVersionName() {
        return this.versionName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getCompileSdkVersion() {
        return this.compileSdkVersion;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getCompileSdkVersionCodeName() {
        return this.compileSdkVersionCodeName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getPackageName() {
        return this.packageName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getRealPackage() {
        return this.realPackage;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getBaseApkPath() {
        return this.mBaseApkPath;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isRequiredForAllUsers() {
        return getBoolean(274877906944L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getRestrictedAccountType() {
        return this.restrictedAccountType;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getRequiredAccountType() {
        return this.requiredAccountType;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getOverlayTarget() {
        return this.overlayTarget;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getOverlayTargetName() {
        return this.overlayTargetName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getOverlayCategory() {
        return this.overlayCategory;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getOverlayPriority() {
        return this.overlayPriority;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isOverlayIsStatic() {
        return getBoolean(549755813888L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Map<String, String> getOverlayables() {
        return this.overlayables;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getStaticSharedLibName() {
        return this.staticSharedLibName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public long getStaticSharedLibVersion() {
        return this.staticSharedLibVersion;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getLibraryNames() {
        return this.libraryNames;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getUsesLibraries() {
        return this.usesLibraries;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getUsesOptionalLibraries() {
        return this.usesOptionalLibraries;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getUsesNativeLibraries() {
        return this.usesNativeLibraries;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getUsesOptionalNativeLibraries() {
        return this.usesOptionalNativeLibraries;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getUsesStaticLibraries() {
        return this.usesStaticLibraries;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public long[] getUsesStaticLibrariesVersions() {
        return this.usesStaticLibrariesVersions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String[][] getUsesStaticLibrariesCertDigests() {
        return this.usesStaticLibrariesCertDigests;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getSharedUserId() {
        return this.sharedUserId;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getSharedUserLabel() {
        return this.sharedUserLabel;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ConfigurationInfo> getConfigPreferences() {
        return this.configPreferences;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<FeatureInfo> getReqFeatures() {
        return this.reqFeatures;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<FeatureGroupInfo> getFeatureGroups() {
        return this.featureGroups;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public byte[] getRestrictUpdateHash() {
        return this.restrictUpdateHash;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getOriginalPackages() {
        return this.originalPackages;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getAdoptPermissions() {
        return this.adoptPermissions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    @Deprecated
    public List<String> getRequestedPermissions() {
        return this.requestedPermissions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedUsesPermission> getUsesPermissions() {
        return this.usesPermissions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getImplicitPermissions() {
        return this.implicitPermissions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Map<String, PackageManager.Property> getProperties() {
        return this.mProperties;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Set<String> getUpgradeKeySets() {
        return this.upgradeKeySets;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Map<String, ArraySet<PublicKey>> getKeySetMapping() {
        return this.keySetMapping;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getProtectedBroadcasts() {
        return this.protectedBroadcasts;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedActivity> getActivities() {
        return this.activities;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedActivity> getReceivers() {
        return this.receivers;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedService> getServices() {
        return this.services;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedProvider> getProviders() {
        return this.providers;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedAttribution> getAttributions() {
        return this.attributions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedPermission> getPermissions() {
        return this.permissions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedPermissionGroup> getPermissionGroups() {
        return this.permissionGroups;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<ParsedInstrumentation> getInstrumentations() {
        return this.instrumentations;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<Pair<String, ParsedIntentInfo>> getPreferredActivityFilters() {
        return this.preferredActivityFilters;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Map<String, ParsedProcess> getProcesses() {
        return this.processes;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Bundle getMetaData() {
        return this.metaData;
    }

    private void addMimeGroupsFromComponent(ParsedComponent component) {
        for (int i = component.getIntents().size() - 1; i >= 0; i--) {
            IntentFilter filter = component.getIntents().get(i);
            for (int groupIndex = filter.countMimeGroups() - 1; groupIndex >= 0; groupIndex--) {
                this.mimeGroups = ArrayUtils.add(this.mimeGroups, filter.getMimeGroup(groupIndex));
            }
        }
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Set<String> getMimeGroups() {
        return this.mimeGroups;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getVolumeUuid() {
        return this.volumeUuid;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public PackageParser.SigningDetails getSigningDetails() {
        return this.signingDetails;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getPath() {
        return this.mPath;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isUse32BitAbi() {
        return getBoolean(1099511627776L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isVisibleToInstantApps() {
        return getBoolean(ProtoStream.FIELD_COUNT_REPEATED);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isForceQueryable() {
        return getBoolean(4398046511104L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<Intent> getQueriesIntents() {
        return this.queriesIntents;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public List<String> getQueriesPackages() {
        return this.queriesPackages;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Set<String> getQueriesProviders() {
        return this.queriesProviders;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String[] getSplitClassLoaderNames() {
        return this.splitClassLoaderNames;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String[] getSplitCodePaths() {
        return this.splitCodePaths;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public SparseArray<int[]> getSplitDependencies() {
        return this.splitDependencies;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int[] getSplitFlags() {
        return this.splitFlags;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String[] getSplitNames() {
        return this.splitNames;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int[] getSplitRevisionCodes() {
        return this.splitRevisionCodes;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getAppComponentFactory() {
        return this.appComponentFactory;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getBackupAgentName() {
        return this.backupAgentName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getBanner() {
        return this.banner;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getCategory() {
        return this.category;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getClassLoaderName() {
        return this.classLoaderName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getClassName() {
        return this.className;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getCompatibleWidthLimitDp() {
        return this.compatibleWidthLimitDp;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getDescriptionRes() {
        return this.descriptionRes;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isEnabled() {
        return getBoolean(17592186044416L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isCrossProfile() {
        return getBoolean(8796093022208L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getFullBackupContent() {
        return this.fullBackupContent;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getDataExtractionRules() {
        return this.dataExtractionRules;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getIconRes() {
        return this.iconRes;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getInstallLocation() {
        return this.installLocation;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getLabelRes() {
        return this.labelRes;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getLargestWidthLimitDp() {
        return this.largestWidthLimitDp;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getLogo() {
        return this.logo;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getManageSpaceActivityName() {
        return this.manageSpaceActivityName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public float getMaxAspectRatio() {
        return this.maxAspectRatio;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public float getMinAspectRatio() {
        return this.minAspectRatio;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public SparseIntArray getMinExtensionVersions() {
        return this.minExtensionVersions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getMinSdkVersion() {
        return this.minSdkVersion;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getNetworkSecurityConfigRes() {
        return this.networkSecurityConfigRes;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public CharSequence getNonLocalizedLabel() {
        return this.nonLocalizedLabel;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getPermission() {
        return this.permission;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getRequiresSmallestWidthDp() {
        return this.requiresSmallestWidthDp;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getRoundIconRes() {
        return this.roundIconRes;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getTargetSandboxVersion() {
        return this.targetSandboxVersion;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getTargetSdkVersion() {
        return this.targetSdkVersion;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getTaskAffinity() {
        return this.taskAffinity;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getTheme() {
        return this.theme;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getUiOptions() {
        return this.uiOptions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public String getZygotePreloadName() {
        return this.zygotePreloadName;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isExternalStorage() {
        return getBoolean(1L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isBaseHardwareAccelerated() {
        return getBoolean(2L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isAllowBackup() {
        return getBoolean(4L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isKillAfterRestore() {
        return getBoolean(8L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isRestoreAnyVersion() {
        return getBoolean(16L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isFullBackupOnly() {
        return getBoolean(32L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isPersistent() {
        return getBoolean(64L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isDebuggable() {
        return getBoolean(128L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isVmSafeMode() {
        return getBoolean(256L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isHasCode() {
        return getBoolean(512L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isAllowTaskReparenting() {
        return getBoolean(1024L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isAllowClearUserData() {
        return getBoolean(2048L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isLargeHeap() {
        return getBoolean(4096L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isUsesCleartextTraffic() {
        return getBoolean(8192L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isSupportsRtl() {
        return getBoolean(16384L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isTestOnly() {
        return getBoolean(32768L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isMultiArch() {
        return getBoolean(65536L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isExtractNativeLibs() {
        return getBoolean(131072L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isGame() {
        return getBoolean(262144L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Boolean getResizeableActivity() {
        return this.resizeableActivity;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isStaticSharedLibrary() {
        return getBoolean(524288L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isOverlay() {
        return getBoolean(1048576L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isIsolatedSplitLoading() {
        return getBoolean(2097152L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isHasDomainUrls() {
        return getBoolean(4194304L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isProfileableByShell() {
        return isProfileable() && getBoolean(8388608L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isProfileable() {
        return !getBoolean(35184372088832L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isBackupInForeground() {
        return getBoolean(16777216L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isUseEmbeddedDex() {
        return getBoolean(33554432L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isDefaultToDeviceProtectedStorage() {
        return getBoolean(67108864L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isDirectBootAware() {
        return getBoolean(134217728L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getGwpAsanMode() {
        return this.gwpAsanMode;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getMemtagMode() {
        return this.memtagMode;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getNativeHeapZeroInitialized() {
        return this.nativeHeapZeroInitialized;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public Boolean hasRequestRawExternalStorageAccess() {
        return this.requestRawExternalStorageAccess;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isPartiallyDirectBootAware() {
        return getBoolean(268435456L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isResizeableActivityViaSdkVersion() {
        return getBoolean(536870912L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isAllowClearUserDataOnFailedRestore() {
        return getBoolean(1073741824L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isAllowAudioPlaybackCapture() {
        return getBoolean(2147483648L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isRequestLegacyExternalStorage() {
        return getBoolean(ProtoStream.FIELD_TYPE_DOUBLE);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isUsesNonSdkApi() {
        return getBoolean(ProtoStream.FIELD_TYPE_FLOAT);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isHasFragileUserData() {
        return getBoolean(ProtoStream.FIELD_TYPE_UINT64);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isCantSaveState() {
        return getBoolean(ProtoStream.FIELD_TYPE_BOOL);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean isAllowNativeHeapPointerTagging() {
        return getBoolean(ProtoStream.FIELD_TYPE_SFIXED64);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public int getAutoRevokePermissions() {
        return this.autoRevokePermissions;
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean hasPreserveLegacyExternalStorage() {
        return getBoolean(137438953472L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean hasRequestForegroundServiceExemption() {
        return getBoolean(70368744177664L);
    }

    @Override // android.content.pm.parsing.ParsingPackageRead
    public boolean areAttributionsUserVisible() {
        return getBoolean(140737488355328L);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setBaseRevisionCode  reason: collision with other method in class */
    public ParsingPackageImpl mo911setBaseRevisionCode(int value) {
        this.baseRevisionCode = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setCompileSdkVersion  reason: collision with other method in class */
    public ParsingPackageImpl mo917setCompileSdkVersion(int value) {
        this.compileSdkVersion = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRequiredForAllUsers  reason: collision with other method in class */
    public ParsingPackageImpl mo973setRequiredForAllUsers(boolean value) {
        return setBoolean(274877906944L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setOverlayPriority  reason: collision with other method in class */
    public ParsingPackageImpl mo957setOverlayPriority(int value) {
        this.overlayPriority = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setOverlayIsStatic  reason: collision with other method in class */
    public ParsingPackageImpl mo956setOverlayIsStatic(boolean value) {
        return setBoolean(549755813888L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setStaticSharedLibVersion  reason: collision with other method in class */
    public ParsingPackageImpl mo988setStaticSharedLibVersion(long value) {
        this.staticSharedLibVersion = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSharedUserLabel  reason: collision with other method in class */
    public ParsingPackageImpl mo983setSharedUserLabel(int value) {
        this.sharedUserLabel = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRestrictUpdateHash  reason: collision with other method in class */
    public ParsingPackageImpl mo979setRestrictUpdateHash(byte... value) {
        this.restrictUpdateHash = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setUpgradeKeySets  reason: collision with other method in class */
    public ParsingPackageImpl mo1001setUpgradeKeySets(Set<String> value) {
        this.upgradeKeySets = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setProcesses  reason: collision with other method in class */
    public ParsingPackageImpl mo965setProcesses(Map<String, ParsedProcess> value) {
        this.processes = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setMetaData  reason: collision with other method in class */
    public ParsingPackageImpl mo946setMetaData(Bundle value) {
        this.metaData = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSigningDetails  reason: collision with other method in class */
    public ParsingPackageImpl mo984setSigningDetails(PackageParser.SigningDetails value) {
        this.signingDetails = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setUse32BitAbi  reason: collision with other method in class */
    public ParsingPackageImpl mo1002setUse32BitAbi(boolean value) {
        return setBoolean(1099511627776L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setVisibleToInstantApps  reason: collision with other method in class */
    public ParsingPackageImpl mo1007setVisibleToInstantApps(boolean value) {
        return setBoolean(ProtoStream.FIELD_COUNT_REPEATED, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setForceQueryable  reason: collision with other method in class */
    public ParsingPackageImpl mo927setForceQueryable(boolean value) {
        return setBoolean(4398046511104L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setBanner  reason: collision with other method in class */
    public ParsingPackageImpl mo909setBanner(int value) {
        this.banner = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setCategory  reason: collision with other method in class */
    public ParsingPackageImpl mo913setCategory(int value) {
        this.category = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setCompatibleWidthLimitDp  reason: collision with other method in class */
    public ParsingPackageImpl mo916setCompatibleWidthLimitDp(int value) {
        this.compatibleWidthLimitDp = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setDescriptionRes  reason: collision with other method in class */
    public ParsingPackageImpl mo922setDescriptionRes(int value) {
        this.descriptionRes = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setEnabled  reason: collision with other method in class */
    public ParsingPackageImpl mo924setEnabled(boolean value) {
        return setBoolean(17592186044416L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setCrossProfile  reason: collision with other method in class */
    public ParsingPackageImpl mo918setCrossProfile(boolean value) {
        return setBoolean(8796093022208L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setFullBackupContent  reason: collision with other method in class */
    public ParsingPackageImpl mo928setFullBackupContent(int value) {
        this.fullBackupContent = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setDataExtractionRules  reason: collision with other method in class */
    public ParsingPackageImpl mo919setDataExtractionRules(int value) {
        this.dataExtractionRules = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setIconRes  reason: collision with other method in class */
    public ParsingPackageImpl mo935setIconRes(int value) {
        this.iconRes = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setInstallLocation  reason: collision with other method in class */
    public ParsingPackageImpl mo936setInstallLocation(int value) {
        this.installLocation = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setLabelRes  reason: collision with other method in class */
    public ParsingPackageImpl mo939setLabelRes(int value) {
        this.labelRes = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setLargestWidthLimitDp  reason: collision with other method in class */
    public ParsingPackageImpl mo941setLargestWidthLimitDp(int value) {
        this.largestWidthLimitDp = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setLogo  reason: collision with other method in class */
    public ParsingPackageImpl mo942setLogo(int value) {
        this.logo = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setMaxAspectRatio  reason: collision with other method in class */
    public ParsingPackageImpl mo944setMaxAspectRatio(float value) {
        this.maxAspectRatio = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setMinAspectRatio  reason: collision with other method in class */
    public ParsingPackageImpl mo947setMinAspectRatio(float value) {
        this.minAspectRatio = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setMinExtensionVersions  reason: collision with other method in class */
    public ParsingPackageImpl mo948setMinExtensionVersions(SparseIntArray value) {
        this.minExtensionVersions = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setMinSdkVersion  reason: collision with other method in class */
    public ParsingPackageImpl mo949setMinSdkVersion(int value) {
        this.minSdkVersion = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setNetworkSecurityConfigRes  reason: collision with other method in class */
    public ParsingPackageImpl mo952setNetworkSecurityConfigRes(int value) {
        this.networkSecurityConfigRes = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRequiresSmallestWidthDp  reason: collision with other method in class */
    public ParsingPackageImpl mo974setRequiresSmallestWidthDp(int value) {
        this.requiresSmallestWidthDp = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRoundIconRes  reason: collision with other method in class */
    public ParsingPackageImpl mo981setRoundIconRes(int value) {
        this.roundIconRes = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setTargetSandboxVersion  reason: collision with other method in class */
    public ParsingPackageImpl mo995setTargetSandboxVersion(int value) {
        this.targetSandboxVersion = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setTargetSdkVersion  reason: collision with other method in class */
    public ParsingPackageImpl mo996setTargetSdkVersion(int value) {
        this.targetSdkVersion = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setTheme  reason: collision with other method in class */
    public ParsingPackageImpl mo999setTheme(int value) {
        this.theme = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRequestForegroundServiceExemption  reason: collision with other method in class */
    public ParsingPackageImpl mo969setRequestForegroundServiceExemption(boolean value) {
        return setBoolean(70368744177664L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setUiOptions  reason: collision with other method in class */
    public ParsingPackageImpl mo1000setUiOptions(int value) {
        this.uiOptions = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setExternalStorage  reason: collision with other method in class */
    public ParsingPackageImpl mo925setExternalStorage(boolean value) {
        return setBoolean(1L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setBaseHardwareAccelerated  reason: collision with other method in class */
    public ParsingPackageImpl mo910setBaseHardwareAccelerated(boolean value) {
        return setBoolean(2L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAllowBackup  reason: collision with other method in class */
    public ParsingPackageImpl mo899setAllowBackup(boolean value) {
        return setBoolean(4L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setKillAfterRestore  reason: collision with other method in class */
    public ParsingPackageImpl mo938setKillAfterRestore(boolean value) {
        return setBoolean(8L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRestoreAnyVersion  reason: collision with other method in class */
    public ParsingPackageImpl mo978setRestoreAnyVersion(boolean value) {
        return setBoolean(16L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setFullBackupOnly  reason: collision with other method in class */
    public ParsingPackageImpl mo929setFullBackupOnly(boolean value) {
        return setBoolean(32L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setPersistent  reason: collision with other method in class */
    public ParsingPackageImpl mo962setPersistent(boolean value) {
        return setBoolean(64L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setDebuggable  reason: collision with other method in class */
    public ParsingPackageImpl mo920setDebuggable(boolean value) {
        return setBoolean(128L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setVmSafeMode  reason: collision with other method in class */
    public ParsingPackageImpl mo1008setVmSafeMode(boolean value) {
        return setBoolean(256L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setHasCode  reason: collision with other method in class */
    public ParsingPackageImpl mo932setHasCode(boolean value) {
        return setBoolean(512L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAllowTaskReparenting  reason: collision with other method in class */
    public ParsingPackageImpl mo903setAllowTaskReparenting(boolean value) {
        return setBoolean(1024L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAllowClearUserData  reason: collision with other method in class */
    public ParsingPackageImpl mo900setAllowClearUserData(boolean value) {
        return setBoolean(2048L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setLargeHeap  reason: collision with other method in class */
    public ParsingPackageImpl mo940setLargeHeap(boolean value) {
        return setBoolean(4096L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setUsesCleartextTraffic  reason: collision with other method in class */
    public ParsingPackageImpl mo1004setUsesCleartextTraffic(boolean value) {
        return setBoolean(8192L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setSupportsRtl  reason: collision with other method in class */
    public ParsingPackageImpl mo993setSupportsRtl(boolean value) {
        return setBoolean(16384L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setTestOnly  reason: collision with other method in class */
    public ParsingPackageImpl mo998setTestOnly(boolean value) {
        return setBoolean(32768L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setMultiArch  reason: collision with other method in class */
    public ParsingPackageImpl mo950setMultiArch(boolean value) {
        return setBoolean(65536L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setExtractNativeLibs  reason: collision with other method in class */
    public ParsingPackageImpl mo926setExtractNativeLibs(boolean value) {
        return setBoolean(131072L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setGame  reason: collision with other method in class */
    public ParsingPackageImpl mo930setGame(boolean value) {
        return setBoolean(262144L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setResizeableActivity  reason: collision with other method in class */
    public ParsingPackageImpl mo976setResizeableActivity(Boolean value) {
        this.resizeableActivity = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setStaticSharedLibrary  reason: collision with other method in class */
    public ParsingPackageImpl mo989setStaticSharedLibrary(boolean value) {
        return setBoolean(524288L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setOverlay  reason: collision with other method in class */
    public ParsingPackageImpl mo954setOverlay(boolean value) {
        return setBoolean(1048576L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setIsolatedSplitLoading  reason: collision with other method in class */
    public ParsingPackageImpl mo937setIsolatedSplitLoading(boolean value) {
        return setBoolean(2097152L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setHasDomainUrls  reason: collision with other method in class */
    public ParsingPackageImpl mo933setHasDomainUrls(boolean value) {
        return setBoolean(4194304L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setProfileableByShell  reason: collision with other method in class */
    public ParsingPackageImpl mo967setProfileableByShell(boolean value) {
        return setBoolean(8388608L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setProfileable  reason: collision with other method in class */
    public ParsingPackageImpl mo966setProfileable(boolean value) {
        return setBoolean(35184372088832L, !value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setBackupInForeground  reason: collision with other method in class */
    public ParsingPackageImpl mo908setBackupInForeground(boolean value) {
        return setBoolean(16777216L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setUseEmbeddedDex  reason: collision with other method in class */
    public ParsingPackageImpl mo1003setUseEmbeddedDex(boolean value) {
        return setBoolean(33554432L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setDefaultToDeviceProtectedStorage  reason: collision with other method in class */
    public ParsingPackageImpl mo921setDefaultToDeviceProtectedStorage(boolean value) {
        return setBoolean(67108864L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setDirectBootAware  reason: collision with other method in class */
    public ParsingPackageImpl mo923setDirectBootAware(boolean value) {
        return setBoolean(134217728L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setGwpAsanMode  reason: collision with other method in class */
    public ParsingPackageImpl mo931setGwpAsanMode(int value) {
        this.gwpAsanMode = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setMemtagMode  reason: collision with other method in class */
    public ParsingPackageImpl mo945setMemtagMode(int value) {
        this.memtagMode = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setNativeHeapZeroInitialized  reason: collision with other method in class */
    public ParsingPackageImpl mo951setNativeHeapZeroInitialized(int value) {
        this.nativeHeapZeroInitialized = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRequestRawExternalStorageAccess  reason: collision with other method in class */
    public ParsingPackageImpl mo971setRequestRawExternalStorageAccess(Boolean value) {
        this.requestRawExternalStorageAccess = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setPartiallyDirectBootAware  reason: collision with other method in class */
    public ParsingPackageImpl mo960setPartiallyDirectBootAware(boolean value) {
        return setBoolean(268435456L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setResizeableActivityViaSdkVersion  reason: collision with other method in class */
    public ParsingPackageImpl mo977setResizeableActivityViaSdkVersion(boolean value) {
        return setBoolean(536870912L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAllowClearUserDataOnFailedRestore  reason: collision with other method in class */
    public ParsingPackageImpl mo901setAllowClearUserDataOnFailedRestore(boolean value) {
        return setBoolean(1073741824L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAllowAudioPlaybackCapture  reason: collision with other method in class */
    public ParsingPackageImpl mo898setAllowAudioPlaybackCapture(boolean value) {
        return setBoolean(2147483648L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRequestLegacyExternalStorage  reason: collision with other method in class */
    public ParsingPackageImpl mo970setRequestLegacyExternalStorage(boolean value) {
        return setBoolean(ProtoStream.FIELD_TYPE_DOUBLE, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setUsesNonSdkApi  reason: collision with other method in class */
    public ParsingPackageImpl mo1005setUsesNonSdkApi(boolean value) {
        return setBoolean(ProtoStream.FIELD_TYPE_FLOAT, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setHasFragileUserData  reason: collision with other method in class */
    public ParsingPackageImpl mo934setHasFragileUserData(boolean value) {
        return setBoolean(ProtoStream.FIELD_TYPE_UINT64, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setCantSaveState  reason: collision with other method in class */
    public ParsingPackageImpl mo912setCantSaveState(boolean value) {
        return setBoolean(ProtoStream.FIELD_TYPE_BOOL, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAllowNativeHeapPointerTagging  reason: collision with other method in class */
    public ParsingPackageImpl mo902setAllowNativeHeapPointerTagging(boolean value) {
        return setBoolean(ProtoStream.FIELD_TYPE_SFIXED64, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAutoRevokePermissions  reason: collision with other method in class */
    public ParsingPackageImpl mo906setAutoRevokePermissions(int value) {
        this.autoRevokePermissions = value;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setPreserveLegacyExternalStorage  reason: collision with other method in class */
    public ParsingPackageImpl mo963setPreserveLegacyExternalStorage(boolean value) {
        return setBoolean(137438953472L, value);
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setVersionName  reason: collision with other method in class */
    public ParsingPackageImpl mo1006setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    public ParsingPackage setCompileSdkVersionCodename(String compileSdkVersionCodename) {
        this.compileSdkVersionCodeName = compileSdkVersionCodename;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setProcessName  reason: collision with other method in class */
    public ParsingPackageImpl mo964setProcessName(String processName) {
        this.processName = processName;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRealPackage  reason: collision with other method in class */
    public ParsingPackageImpl mo968setRealPackage(String realPackage) {
        this.realPackage = realPackage;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setRestrictedAccountType  reason: collision with other method in class */
    public ParsingPackageImpl mo980setRestrictedAccountType(String restrictedAccountType) {
        this.restrictedAccountType = restrictedAccountType;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setOverlayTargetName  reason: collision with other method in class */
    public ParsingPackageImpl mo959setOverlayTargetName(String overlayTargetName) {
        this.overlayTargetName = overlayTargetName;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setOverlayCategory  reason: collision with other method in class */
    public ParsingPackageImpl mo955setOverlayCategory(String overlayCategory) {
        this.overlayCategory = overlayCategory;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setAppComponentFactory  reason: collision with other method in class */
    public ParsingPackageImpl mo905setAppComponentFactory(String appComponentFactory) {
        this.appComponentFactory = appComponentFactory;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setBackupAgentName  reason: collision with other method in class */
    public ParsingPackageImpl mo907setBackupAgentName(String backupAgentName) {
        this.backupAgentName = backupAgentName;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setClassLoaderName  reason: collision with other method in class */
    public ParsingPackageImpl mo914setClassLoaderName(String classLoaderName) {
        this.classLoaderName = classLoaderName;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setClassName  reason: collision with other method in class */
    public ParsingPackageImpl mo915setClassName(String className) {
        this.className = className == null ? null : className.trim();
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setManageSpaceActivityName  reason: collision with other method in class */
    public ParsingPackageImpl mo943setManageSpaceActivityName(String manageSpaceActivityName) {
        this.manageSpaceActivityName = manageSpaceActivityName;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setPermission  reason: collision with other method in class */
    public ParsingPackageImpl mo961setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setTaskAffinity  reason: collision with other method in class */
    public ParsingPackageImpl mo997setTaskAffinity(String taskAffinity) {
        this.taskAffinity = taskAffinity;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    /* renamed from: setZygotePreloadName  reason: collision with other method in class */
    public ParsingPackageImpl mo1010setZygotePreloadName(String zygotePreloadName) {
        this.zygotePreloadName = zygotePreloadName;
        return this;
    }

    @Override // android.content.pm.parsing.ParsingPackage
    public ParsingPackage setAttributionsAreUserVisible(boolean attributionsAreUserVisible) {
        setBoolean(140737488355328L, attributionsAreUserVisible);
        return this;
    }
}
