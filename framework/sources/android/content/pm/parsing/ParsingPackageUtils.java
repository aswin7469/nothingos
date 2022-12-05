package android.content.pm.parsing;

import android.app.ActivityThread;
import android.app.ResourcesManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.FeatureGroupInfo;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.Signature;
import android.content.pm.parsing.component.ComponentParseUtils;
import android.content.pm.parsing.component.ParsedActivity;
import android.content.pm.parsing.component.ParsedActivityUtils;
import android.content.pm.parsing.component.ParsedAttribution;
import android.content.pm.parsing.component.ParsedAttributionUtils;
import android.content.pm.parsing.component.ParsedComponent;
import android.content.pm.parsing.component.ParsedInstrumentation;
import android.content.pm.parsing.component.ParsedInstrumentationUtils;
import android.content.pm.parsing.component.ParsedIntentInfo;
import android.content.pm.parsing.component.ParsedIntentInfoUtils;
import android.content.pm.parsing.component.ParsedMainComponent;
import android.content.pm.parsing.component.ParsedPermission;
import android.content.pm.parsing.component.ParsedPermissionGroup;
import android.content.pm.parsing.component.ParsedPermissionUtils;
import android.content.pm.parsing.component.ParsedProcess;
import android.content.pm.parsing.component.ParsedProcessUtils;
import android.content.pm.parsing.component.ParsedProvider;
import android.content.pm.parsing.component.ParsedProviderUtils;
import android.content.pm.parsing.component.ParsedService;
import android.content.pm.parsing.component.ParsedServiceUtils;
import android.content.pm.parsing.component.ParsedUsesPermission;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.pm.parsing.result.ParseTypeImpl;
import android.content.pm.split.DefaultSplitAssetLoader;
import android.content.pm.split.SplitAssetDependencyLoader;
import android.content.pm.split.SplitAssetLoader;
import android.content.pm.split.SplitDependencyLoader;
import android.content.res.ApkAssets;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.IncidentManager;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserHandle;
import android.os.ext.SdkExtensions;
import android.permission.PermissionManager;
import android.provider.SettingsStringUtil;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.util.apk.ApkSignatureVerifier;
import com.android.internal.R;
import com.android.internal.os.ClassLoaderFactory;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;
import nothing.os.DeviceStateManager;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ParsingPackageUtils {
    public static final String ANDROID_MANIFEST_FILENAME = "AndroidManifest.xml";
    public static final boolean DEBUG_BACKUP = false;
    public static final boolean DEBUG_JAR = false;
    public static final float DEFAULT_PRE_O_MAX_ASPECT_RATIO = 1.86f;
    private static final int MAX_FILE_NAME_SIZE = 223;
    public static final String METADATA_ACTIVITY_LAUNCH_MODE = "android.activity.launch_mode";
    public static final String METADATA_ACTIVITY_WINDOW_LAYOUT_AFFINITY = "android.activity_window_layout_affinity";
    public static final String METADATA_MAX_ASPECT_RATIO = "android.max_aspect";
    public static final String METADATA_SUPPORTS_SIZE_CHANGES = "android.supports_size_changes";
    public static final String MNT_EXPAND = "/mnt/expand/";
    public static final int PARSE_CHATTY = Integer.MIN_VALUE;
    public static final int PARSE_COLLECT_CERTIFICATES = 32;
    public static final int PARSE_DEFAULT_INSTALL_LOCATION = -1;
    public static final int PARSE_DEFAULT_TARGET_SANDBOX = 1;
    public static final int PARSE_ENFORCE_CODE = 64;
    public static final int PARSE_EXTERNAL_STORAGE = 8;
    public static final int PARSE_IGNORE_PROCESSES = 2;
    public static final int PARSE_IS_SYSTEM_DIR = 16;
    public static final int PARSE_MUST_BE_APK = 1;
    public static final boolean RIGID_PARSER = false;
    private static final String TAG = "PackageParsing";
    public static final String TAG_ADOPT_PERMISSIONS = "adopt-permissions";
    public static final String TAG_APPLICATION = "application";
    public static final String TAG_ATTRIBUTION = "attribution";
    public static final String TAG_COMPATIBLE_SCREENS = "compatible-screens";
    public static final String TAG_EAT_COMMENT = "eat-comment";
    public static final String TAG_FEATURE_GROUP = "feature-group";
    public static final String TAG_INSTRUMENTATION = "instrumentation";
    public static final String TAG_KEY_SETS = "key-sets";
    public static final String TAG_MANIFEST = "manifest";
    public static final String TAG_ORIGINAL_PACKAGE = "original-package";
    public static final String TAG_OVERLAY = "overlay";
    public static final String TAG_PACKAGE = "package";
    public static final String TAG_PACKAGE_VERIFIER = "package-verifier";
    public static final String TAG_PERMISSION = "permission";
    public static final String TAG_PERMISSION_GROUP = "permission-group";
    public static final String TAG_PERMISSION_TREE = "permission-tree";
    public static final String TAG_PROFILEABLE = "profileable";
    public static final String TAG_PROTECTED_BROADCAST = "protected-broadcast";
    public static final String TAG_QUERIES = "queries";
    public static final String TAG_RESTRICT_UPDATE = "restrict-update";
    public static final String TAG_SUPPORTS_INPUT = "supports-input";
    public static final String TAG_SUPPORT_SCREENS = "supports-screens";
    public static final String TAG_USES_CONFIGURATION = "uses-configuration";
    public static final String TAG_USES_FEATURE = "uses-feature";
    public static final String TAG_USES_GL_TEXTURE = "uses-gl-texture";
    public static final String TAG_USES_PERMISSION = "uses-permission";
    public static final String TAG_USES_PERMISSION_SDK_23 = "uses-permission-sdk-23";
    public static final String TAG_USES_PERMISSION_SDK_M = "uses-permission-sdk-m";
    public static final String TAG_USES_SDK = "uses-sdk";
    public static final String TAG_USES_SPLIT = "uses-split";
    private Callback mCallback;
    private DisplayMetrics mDisplayMetrics;
    private boolean mOnlyCoreApps;
    private String[] mSeparateProcesses;
    private List<PermissionManager.SplitPermissionInfo> mSplitPermissionInfos;
    public static final int SDK_VERSION = Build.VERSION.SDK_INT;
    public static final String[] SDK_CODENAMES = Build.VERSION.ACTIVE_CODENAMES;
    public static boolean sCompatibilityModeEnabled = true;
    public static boolean sUseRoundIcon = false;

    /* loaded from: classes.dex */
    public interface Callback {
        boolean hasFeature(String str);

        ParsingPackage startParsingPackage(String str, String str2, String str3, TypedArray typedArray, boolean z);
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface ParseFlags {
    }

    public static ParseResult<ParsingPackage> parseDefaultOneTime(File file, int parseFlags, List<PermissionManager.SplitPermissionInfo> splitPermissions, boolean collectCertificates) {
        ParseInput input = ParseTypeImpl.forDefaultParsing().reset();
        return parseDefault(input, file, parseFlags, splitPermissions, collectCertificates);
    }

    public static ParseResult<ParsingPackage> parseDefault(ParseInput input, File file, int parseFlags, List<PermissionManager.SplitPermissionInfo> splitPermissions, boolean collectCertificates) {
        ParsingPackageUtils parser = new ParsingPackageUtils(false, null, null, splitPermissions, new Callback() { // from class: android.content.pm.parsing.ParsingPackageUtils.1
            @Override // android.content.pm.parsing.ParsingPackageUtils.Callback
            public boolean hasFeature(String feature) {
                return false;
            }

            @Override // android.content.pm.parsing.ParsingPackageUtils.Callback
            public ParsingPackage startParsingPackage(String packageName, String baseApkPath, String path, TypedArray manifestArray, boolean isCoreApp) {
                return new ParsingPackageImpl(packageName, baseApkPath, path, manifestArray);
            }
        });
        try {
            ParseResult<ParsingPackage> result = parser.parsePackage(input, file, parseFlags);
            if (result.isError()) {
                return result;
            }
            try {
                ParsingPackage pkg = result.getResult();
                if (collectCertificates) {
                    pkg.mo984setSigningDetails(getSigningDetails(pkg, false));
                }
                pkg.hideAsParsed();
                return input.success(pkg);
            } catch (PackageParser.PackageParserException e) {
                return input.error(-102, "Error collecting package certificates", e);
            }
        } catch (PackageParser.PackageParserException e2) {
            return input.error(-102, "Error parsing package", e2);
        }
    }

    public ParsingPackageUtils(boolean onlyCoreApps, String[] separateProcesses, DisplayMetrics displayMetrics, List<PermissionManager.SplitPermissionInfo> splitPermissions, Callback callback) {
        this.mOnlyCoreApps = onlyCoreApps;
        this.mSeparateProcesses = separateProcesses;
        this.mDisplayMetrics = displayMetrics;
        this.mSplitPermissionInfos = splitPermissions;
        this.mCallback = callback;
    }

    public ParseResult<ParsingPackage> parsePackage(ParseInput input, File packageFile, int flags) throws PackageParser.PackageParserException {
        if (packageFile.isDirectory()) {
            return parseClusterPackage(input, packageFile, flags);
        }
        return parseMonolithicPackage(input, packageFile, flags);
    }

    private ParseResult<ParsingPackage> parseClusterPackage(ParseInput input, File packageDir, int flags) {
        SplitAssetLoader assetLoader;
        SparseArray<int[]> splitDependencies;
        SplitAssetLoader assetLoader2;
        ParseResult<PackageLite> liteResult = ApkLiteParseUtils.parseClusterPackageLite(input, packageDir, 0);
        if (liteResult.isError()) {
            return input.error(liteResult);
        }
        PackageLite lite = liteResult.getResult();
        if (this.mOnlyCoreApps && !lite.isCoreApp()) {
            return input.error(PackageManager.INSTALL_PARSE_FAILED_ONLY_COREAPP_ALLOWED, "Not a coreApp: " + packageDir);
        }
        if (!lite.isIsolatedSplits() || ArrayUtils.isEmpty(lite.getSplitNames())) {
            assetLoader = new DefaultSplitAssetLoader(lite, flags);
            splitDependencies = null;
        } else {
            try {
                SparseArray<int[]> splitDependencies2 = SplitAssetDependencyLoader.createDependenciesFromPackage(lite);
                assetLoader = new SplitAssetDependencyLoader(lite, splitDependencies2, flags);
                splitDependencies = splitDependencies2;
            } catch (SplitDependencyLoader.IllegalDependencyException e) {
                return input.error(-101, e.getMessage());
            }
        }
        try {
            File baseApk = new File(lite.getBaseApkPath());
            ParseResult<ParsingPackage> result = parseBaseApk(input, baseApk, lite.getPath(), assetLoader, flags);
            if (!result.isError()) {
                ParsingPackage pkg = result.getResult();
                if (!ArrayUtils.isEmpty(lite.getSplitNames())) {
                    pkg.mo895asSplit(lite.getSplitNames(), lite.getSplitApkPaths(), lite.getSplitRevisionCodes(), splitDependencies);
                    int num = lite.getSplitNames().length;
                    int i = 0;
                    while (i < num) {
                        AssetManager splitAssets = assetLoader.getSplitAssetManager(i);
                        int i2 = i;
                        SparseArray<int[]> splitDependencies3 = splitDependencies;
                        assetLoader2 = assetLoader;
                        try {
                            try {
                                parseSplitApk(input, pkg, i2, splitAssets, flags);
                                i = i2 + 1;
                                splitDependencies = splitDependencies3;
                                assetLoader = assetLoader2;
                            } catch (PackageParser.PackageParserException e2) {
                                e = e2;
                            }
                        } catch (Throwable th) {
                            e = th;
                            IoUtils.closeQuietly(assetLoader2);
                            throw e;
                        }
                    }
                    assetLoader2 = assetLoader;
                } else {
                    assetLoader2 = assetLoader;
                }
                pkg.mo1002setUse32BitAbi(lite.isUse32bitAbi());
                ParseResult<ParsingPackage> success = input.success(pkg);
                IoUtils.closeQuietly(assetLoader2);
                return success;
            }
            try {
                ParseResult<ParsingPackage> error = input.error(result);
                IoUtils.closeQuietly(assetLoader);
                return error;
            } catch (PackageParser.PackageParserException e3) {
                e = e3;
                assetLoader2 = assetLoader;
            } catch (Throwable th2) {
                e = th2;
                assetLoader2 = assetLoader;
                IoUtils.closeQuietly(assetLoader2);
                throw e;
            }
        } catch (PackageParser.PackageParserException e4) {
            e = e4;
            assetLoader2 = assetLoader;
        } catch (Throwable th3) {
            e = th3;
            assetLoader2 = assetLoader;
        }
        ParseResult<ParsingPackage> error2 = input.error(-102, "Failed to load assets: " + lite.getBaseApkPath(), e);
        IoUtils.closeQuietly(assetLoader2);
        return error2;
    }

    private ParseResult<ParsingPackage> parseMonolithicPackage(ParseInput input, File apkFile, int flags) throws PackageParser.PackageParserException {
        ParseResult<PackageLite> liteResult = ApkLiteParseUtils.parseMonolithicPackageLite(input, apkFile, flags);
        if (liteResult.isError()) {
            return input.error(liteResult);
        }
        PackageLite lite = liteResult.getResult();
        if (this.mOnlyCoreApps && !lite.isCoreApp()) {
            return input.error(PackageManager.INSTALL_PARSE_FAILED_ONLY_COREAPP_ALLOWED, "Not a coreApp: " + apkFile);
        }
        SplitAssetLoader assetLoader = new DefaultSplitAssetLoader(lite, flags);
        try {
            ParseResult<ParsingPackage> result = parseBaseApk(input, apkFile, apkFile.getCanonicalPath(), assetLoader, flags);
            return result.isError() ? input.error(result) : input.success(result.getResult().mo1002setUse32BitAbi(lite.isUse32bitAbi()));
        } catch (IOException e) {
            return input.error(-102, "Failed to get path: " + apkFile, e);
        } finally {
            IoUtils.closeQuietly(assetLoader);
        }
    }

    private ParseResult<ParsingPackage> parseBaseApk(ParseInput input, File apkFile, String codePath, SplitAssetLoader assetLoader, int flags) throws PackageParser.PackageParserException {
        String volumeUuid;
        XmlResourceParser parser;
        ParseResult<ParsingPackage> result;
        String apkPath = apkFile.getAbsolutePath();
        if (apkPath.startsWith("/mnt/expand/")) {
            int end = apkPath.indexOf(47, "/mnt/expand/".length());
            String volumeUuid2 = apkPath.substring("/mnt/expand/".length(), end);
            volumeUuid = volumeUuid2;
        } else {
            volumeUuid = null;
        }
        AssetManager assets = assetLoader.getBaseAssetManager();
        int cookie = assets.findCookieForPath(apkPath);
        if (cookie == 0) {
            return input.error(-101, "Failed adding asset path: " + apkPath);
        }
        try {
            parser = assets.openXmlResourceParser(cookie, "AndroidManifest.xml");
        } catch (Exception e) {
            e = e;
        }
        try {
            try {
            } catch (Throwable th) {
                th = th;
            }
            try {
                Resources res = new Resources(assets, this.mDisplayMetrics, null);
                ParseResult<ParsingPackage> result2 = parseBaseApk(input, apkPath, codePath, res, parser, flags);
                if (result2.isError()) {
                    ParseResult<ParsingPackage> error = input.error(result2.getErrorCode(), apkPath + " (at " + parser.getPositionDescription() + "): " + result2.getErrorMessage());
                    if (parser != null) {
                        parser.close();
                    }
                    return error;
                }
                ParsingPackage pkg = result2.getResult();
                if (assets.containsAllocatedTable()) {
                    ParseResult<?> deferResult = input.deferError("Targeting R+ (version 30 and above) requires the resources.arsc of installed APKs to be stored uncompressed and aligned on a 4-byte boundary", ParseInput.DeferredError.RESOURCES_ARSC_COMPRESSED);
                    if (deferResult.isError()) {
                        ParseResult<ParsingPackage> error2 = input.error(PackageManager.INSTALL_PARSE_FAILED_RESOURCES_ARSC_COMPRESSED, deferResult.getErrorMessage());
                        if (parser != null) {
                            parser.close();
                        }
                        return error2;
                    }
                }
                ApkAssets apkAssets = assetLoader.getBaseApkAssets();
                boolean definesOverlayable = false;
                try {
                    definesOverlayable = apkAssets.definesOverlayable();
                } catch (IOException e2) {
                }
                if (definesOverlayable) {
                    SparseArray<String> packageNames = assets.getAssignedPackageIdentifiers();
                    int size = packageNames.size();
                    int index = 0;
                    while (index < size) {
                        String packageName = packageNames.valueAt(index);
                        SparseArray<String> packageNames2 = packageNames;
                        Map<String, String> overlayableToActor = assets.getOverlayableMap(packageName);
                        if (overlayableToActor == null || overlayableToActor.isEmpty()) {
                            result = result2;
                        } else {
                            for (String overlayable : overlayableToActor.keySet()) {
                                pkg.addOverlayable(overlayable, overlayableToActor.get(overlayable));
                                result2 = result2;
                                overlayableToActor = overlayableToActor;
                            }
                            result = result2;
                        }
                        index++;
                        packageNames = packageNames2;
                        result2 = result;
                    }
                }
                pkg.mo1009setVolumeUuid(volumeUuid);
                if ((flags & 32) != 0) {
                    pkg.mo984setSigningDetails(getSigningDetails(pkg, false));
                } else {
                    pkg.mo984setSigningDetails(PackageParser.SigningDetails.UNKNOWN);
                }
                ParseResult<ParsingPackage> success = input.success(pkg);
                if (parser != null) {
                    parser.close();
                }
                return success;
            } catch (Throwable th2) {
                th = th2;
                Throwable th3 = th;
                if (parser != null) {
                    parser.close();
                }
                throw th3;
            }
        } catch (Exception e3) {
            e = e3;
            return input.error(-102, "Failed to read manifest from " + apkPath, e);
        }
    }

    private ParseResult<ParsingPackage> parseSplitApk(ParseInput input, ParsingPackage pkg, int splitIndex, AssetManager assets, int flags) {
        String apkPath = pkg.getSplitCodePaths()[splitIndex];
        int cookie = assets.findCookieForPath(apkPath);
        if (cookie == 0) {
            return input.error(-101, "Failed adding asset path: " + apkPath);
        }
        try {
            XmlResourceParser parser = assets.openXmlResourceParser(cookie, "AndroidManifest.xml");
            Resources res = new Resources(assets, this.mDisplayMetrics, null);
            ParseResult<ParsingPackage> parseResult = parseSplitApk(input, pkg, res, parser, flags, splitIndex);
            if (parseResult.isError()) {
                int errorCode = parseResult.getErrorCode();
                ParseResult<ParsingPackage> error = input.error(errorCode, apkPath + " (at " + parser.getPositionDescription() + "): " + parseResult.getErrorMessage());
                if (parser != null) {
                    parser.close();
                }
                return error;
            }
            if (parser != null) {
                parser.close();
            }
            return parseResult;
        } catch (Exception e) {
            return input.error(-102, "Failed to read manifest from " + apkPath, e);
        }
    }

    private ParseResult<ParsingPackage> parseBaseApk(ParseInput input, String apkPath, String codePath, Resources res, XmlResourceParser parser, int flags) throws XmlPullParserException, IOException {
        ParseResult<Pair<String, String>> packageSplitResult = ApkLiteParseUtils.parsePackageSplitNames(input, parser);
        if (packageSplitResult.isError()) {
            return input.error(packageSplitResult);
        }
        Pair<String, String> packageSplit = packageSplitResult.getResult();
        String pkgName = packageSplit.first;
        String splitName = packageSplit.second;
        if (!TextUtils.isEmpty(splitName)) {
            return input.error(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Expected base APK, but found split " + splitName);
        }
        TypedArray manifestArray = res.obtainAttributes(parser, R.styleable.AndroidManifest);
        try {
            boolean isCoreApp = parser.getAttributeBooleanValue(null, "coreApp", false);
            ParsingPackage pkg = this.mCallback.startParsingPackage(pkgName, apkPath, codePath, manifestArray, isCoreApp);
            try {
                ParseResult<ParsingPackage> result = parseBaseApkTags(input, pkg, manifestArray, res, parser, flags);
                if (!result.isError()) {
                    ParseResult<ParsingPackage> success = input.success(pkg);
                    manifestArray.recycle();
                    return success;
                }
                manifestArray.recycle();
                return result;
            } catch (Throwable th) {
                th = th;
                manifestArray.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private ParseResult<ParsingPackage> parseSplitApk(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser, int flags, int splitIndex) throws XmlPullParserException, IOException, PackageParser.PackageParserException {
        ParseResult result;
        PackageParser.parsePackageSplitNames(parser, parser);
        boolean foundApp = false;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (outerDepth + 1 >= parser.getDepth() && type == 2) {
                    String tagName = parser.getName();
                    if ("application".equals(tagName)) {
                        if (foundApp) {
                            Slog.w("PackageParsing", "<manifest> has more than one <application>");
                            result = input.success(null);
                        } else {
                            foundApp = true;
                            result = parseSplitApplication(input, pkg, res, parser, flags, splitIndex);
                        }
                    } else {
                        result = ParsingUtils.unknownTag("<manifest>", pkg, parser, input);
                    }
                    if (result.isError()) {
                        return input.error(result);
                    }
                }
            } else {
                if (!foundApp) {
                    ParseResult<?> deferResult = input.deferError("<manifest> does not contain an <application>", ParseInput.DeferredError.MISSING_APP_TAG);
                    if (deferResult.isError()) {
                        return input.error(deferResult);
                    }
                }
                return input.success(pkg);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x00a4, code lost:
        if (r13.equals("provider") != false) goto L26;
     */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0174  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ParseResult<ParsingPackage> parseSplitApplication(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser, int flags, int splitIndex) throws XmlPullParserException, IOException {
        TypedArray sa;
        ParseResult activityResult;
        int i;
        ParseResult result;
        TypedArray sa2;
        ParsedMainComponent mainComponent;
        ParsingPackage parsingPackage = pkg;
        Resources resources = res;
        XmlResourceParser xmlResourceParser = parser;
        TypedArray sa3 = resources.obtainAttributes(xmlResourceParser, R.styleable.AndroidManifestApplication);
        int i2 = 1;
        try {
            parsingPackage.mo986setSplitHasCode(splitIndex, sa3.getBoolean(7, true));
            String classLoaderName = sa3.getString(46);
            if (classLoaderName != null) {
                try {
                    if (!ClassLoaderFactory.isValidClassLoaderName(classLoaderName)) {
                        ParseResult<ParsingPackage> error = input.error("Invalid class loader name: " + classLoaderName);
                        sa3.recycle();
                        return error;
                    }
                } catch (Throwable th) {
                    th = th;
                    sa = sa3;
                    sa.recycle();
                    throw th;
                }
            }
            parsingPackage.mo985setSplitClassLoaderName(splitIndex, classLoaderName);
            sa3.recycle();
            int depth = parser.getDepth();
            while (true) {
                int type = parser.next();
                if (type != i2) {
                    int i3 = 3;
                    if (type != 3 || parser.getDepth() > depth) {
                        if (type == 2) {
                            ParsedMainComponent mainComponent2 = null;
                            String tagName = parser.getName();
                            boolean isActivity = false;
                            switch (tagName.hashCode()) {
                                case -1655966961:
                                    if (tagName.equals("activity")) {
                                        i3 = 0;
                                        break;
                                    }
                                    i3 = -1;
                                    break;
                                case -987494927:
                                    break;
                                case -808719889:
                                    if (tagName.equals(IncidentManager.URI_PARAM_RECEIVER_CLASS)) {
                                        i3 = i2;
                                        break;
                                    }
                                    i3 = -1;
                                    break;
                                case 790287890:
                                    if (tagName.equals("activity-alias")) {
                                        i3 = 4;
                                        break;
                                    }
                                    i3 = -1;
                                    break;
                                case 1984153269:
                                    if (tagName.equals("service")) {
                                        i3 = 2;
                                        break;
                                    }
                                    i3 = -1;
                                    break;
                                default:
                                    i3 = -1;
                                    break;
                            }
                            switch (i3) {
                                case 0:
                                    isActivity = true;
                                    activityResult = ParsedActivityUtils.parseActivityOrReceiver(this.mSeparateProcesses, pkg, res, parser, flags, sUseRoundIcon, input);
                                    if (activityResult.isSuccess()) {
                                        ParsedActivity activity = activityResult.getResult();
                                        if (isActivity) {
                                            parsingPackage.mo865addActivity(activity);
                                        } else {
                                            parsingPackage.mo884addReceiver(activity);
                                        }
                                        mainComponent2 = activity;
                                    }
                                    i = i2;
                                    result = activityResult;
                                    sa2 = sa3;
                                    mainComponent = mainComponent2;
                                    break;
                                case 1:
                                    activityResult = ParsedActivityUtils.parseActivityOrReceiver(this.mSeparateProcesses, pkg, res, parser, flags, sUseRoundIcon, input);
                                    if (activityResult.isSuccess()) {
                                    }
                                    i = i2;
                                    result = activityResult;
                                    sa2 = sa3;
                                    mainComponent = mainComponent2;
                                    break;
                                case 2:
                                    ParseResult serviceResult = ParsedServiceUtils.parseService(this.mSeparateProcesses, pkg, res, parser, flags, sUseRoundIcon, input);
                                    if (serviceResult.isSuccess()) {
                                        ParsedService service = serviceResult.getResult();
                                        parsingPackage.mo886addService(service);
                                        mainComponent2 = service;
                                    }
                                    i = i2;
                                    result = serviceResult;
                                    sa2 = sa3;
                                    mainComponent = mainComponent2;
                                    break;
                                case 3:
                                    ParseResult providerResult = ParsedProviderUtils.parseProvider(this.mSeparateProcesses, pkg, res, parser, flags, sUseRoundIcon, input);
                                    if (providerResult.isSuccess()) {
                                        ParsedProvider provider = providerResult.getResult();
                                        parsingPackage.mo880addProvider(provider);
                                        mainComponent2 = provider;
                                    }
                                    i = i2;
                                    result = providerResult;
                                    sa2 = sa3;
                                    mainComponent = mainComponent2;
                                    break;
                                case 4:
                                    ParseResult activityResult2 = ParsedActivityUtils.parseActivityAlias(parsingPackage, resources, xmlResourceParser, sUseRoundIcon, input);
                                    if (activityResult2.isSuccess()) {
                                        ParsedActivity activity2 = activityResult2.getResult();
                                        parsingPackage.mo865addActivity(activity2);
                                        mainComponent2 = activity2;
                                    }
                                    i = i2;
                                    result = activityResult2;
                                    sa2 = sa3;
                                    mainComponent = mainComponent2;
                                    break;
                                default:
                                    i = i2;
                                    sa2 = sa3;
                                    mainComponent = null;
                                    result = parseSplitBaseAppChildTags(input, tagName, pkg, res, parser);
                                    break;
                            }
                            if (result.isError()) {
                                return input.error(result);
                            }
                            if (mainComponent != null && mainComponent.getSplitName() == null) {
                                mainComponent.setSplitName(pkg.getSplitNames()[splitIndex]);
                            }
                            parsingPackage = pkg;
                            resources = res;
                            xmlResourceParser = parser;
                            i2 = i;
                            sa3 = sa2;
                        }
                    }
                }
            }
            return input.success(pkg);
        } catch (Throwable th2) {
            th = th2;
            sa = sa3;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private ParseResult parseSplitBaseAppChildTags(ParseInput input, String tag, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws IOException, XmlPullParserException {
        char c;
        switch (tag.hashCode()) {
            case -1608941274:
                if (tag.equals("uses-native-library")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -1356765254:
                if (tag.equals("uses-library")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -1115949454:
                if (tag.equals("meta-data")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -993141291:
                if (tag.equals("property")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 8960125:
                if (tag.equals("uses-static-library")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1964930885:
                if (tag.equals("uses-package")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                ParseResult<PackageManager.Property> metaDataResult = parseMetaData(pkg, null, res, parser, "<meta-data>", input);
                if (metaDataResult.isSuccess() && metaDataResult.getResult() != null) {
                    pkg.mo946setMetaData(metaDataResult.getResult().toBundle(pkg.getMetaData()));
                }
                return metaDataResult;
            case 1:
                ParseResult<PackageManager.Property> propertyResult = parseMetaData(pkg, null, res, parser, "<property>", input);
                if (propertyResult.isSuccess()) {
                    pkg.mo878addProperty(propertyResult.getResult());
                }
                return propertyResult;
            case 2:
                return parseUsesStaticLibrary(input, pkg, res, parser);
            case 3:
                return parseUsesLibrary(input, pkg, res, parser);
            case 4:
                return parseUsesNativeLibrary(input, pkg, res, parser);
            case 5:
                return input.success(null);
            default:
                return ParsingUtils.unknownTag("<application>", pkg, parser, input);
        }
    }

    private ParseResult<ParsingPackage> parseBaseApkTags(ParseInput input, ParsingPackage pkg, TypedArray sa, Resources res, XmlResourceParser parser, int flags) throws XmlPullParserException, IOException {
        ParseResult result;
        ParseResult<ParsingPackage> sharedUserResult = parseSharedUser(input, pkg, sa);
        if (sharedUserResult.isError()) {
            return sharedUserResult;
        }
        pkg.mo936setInstallLocation(anInteger(-1, 4, sa)).mo995setTargetSandboxVersion(anInteger(1, 7, sa)).mo925setExternalStorage((flags & 8) != 0);
        int depth = parser.getDepth();
        boolean foundApp = false;
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= depth)) {
                break;
            } else if (type == 2) {
                String tagName = parser.getName();
                if ("application".equals(tagName)) {
                    if (foundApp) {
                        Slog.w("PackageParsing", "<manifest> has more than one <application>");
                        result = input.success(null);
                    } else {
                        foundApp = true;
                        result = parseBaseApplication(input, pkg, res, parser, flags);
                    }
                } else {
                    result = parseBaseApkTag(tagName, input, pkg, res, parser, flags);
                }
                if (result.isError()) {
                    return input.error(result);
                }
            }
        }
        if (!foundApp && ArrayUtils.size(pkg.getInstrumentations()) == 0) {
            ParseResult<?> deferResult = input.deferError("<manifest> does not contain an <application> or <instrumentation>", ParseInput.DeferredError.MISSING_APP_TAG);
            if (deferResult.isError()) {
                return input.error(deferResult);
            }
        }
        if (!ParsedAttribution.isCombinationValid(pkg.getAttributions())) {
            return input.error(-101, "Combination <attribution> tags are not valid");
        }
        if (ParsedPermissionUtils.declareDuplicatePermission(pkg)) {
            return input.error(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Declare duplicate permissions with different protection levels or group.");
        }
        convertNewPermissions(pkg);
        convertSplitPermissions(pkg);
        if (pkg.getTargetSdkVersion() < 4 || (!pkg.isSupportsSmallScreens() && !pkg.isSupportsNormalScreens() && !pkg.isSupportsLargeScreens() && !pkg.isSupportsExtraLargeScreens() && !pkg.isResizeable() && !pkg.isAnyDensity())) {
            adjustPackageToBeUnresizeableAndUnpipable(pkg);
        }
        return input.success(pkg);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private ParseResult parseBaseApkTag(String tag, ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser, int flags) throws IOException, XmlPullParserException {
        char c;
        switch (tag.hashCode()) {
            case -1773650763:
                if (tag.equals("uses-configuration")) {
                    c = '\n';
                    break;
                }
                c = 65535;
                break;
            case -1667688228:
                if (tag.equals("permission-tree")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1108197302:
                if (tag.equals("original-package")) {
                    c = 17;
                    break;
                }
                c = 65535;
                break;
            case -1091287984:
                if (tag.equals("overlay")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -998269702:
                if (tag.equals("restrict-update")) {
                    c = 23;
                    break;
                }
                c = 65535;
                break;
            case -979207434:
                if (tag.equals("feature")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -517618225:
                if (tag.equals("permission")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -309882753:
                if (tag.equals("attribution")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case -266709319:
                if (tag.equals("uses-sdk")) {
                    c = '\r';
                    break;
                }
                c = 65535;
                break;
            case -170723071:
                if (tag.equals("permission-group")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case -129269526:
                if (tag.equals("eat-comment")) {
                    c = 22;
                    break;
                }
                c = 65535;
                break;
            case 119109844:
                if (tag.equals("uses-gl-texture")) {
                    c = 19;
                    break;
                }
                c = 65535;
                break;
            case 349565761:
                if (tag.equals("supports-input")) {
                    c = 21;
                    break;
                }
                c = 65535;
                break;
            case 454915839:
                if (tag.equals("key-sets")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 544550766:
                if (tag.equals("instrumentation")) {
                    c = 16;
                    break;
                }
                c = 65535;
                break;
            case 599862896:
                if (tag.equals("uses-permission")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 632228327:
                if (tag.equals("adopt-permissions")) {
                    c = 18;
                    break;
                }
                c = 65535;
                break;
            case 655087462:
                if (tag.equals("queries")) {
                    c = 24;
                    break;
                }
                c = 65535;
                break;
            case 896788286:
                if (tag.equals("supports-screens")) {
                    c = 14;
                    break;
                }
                c = 65535;
                break;
            case 1343942321:
                if (tag.equals("uses-permission-sdk-23")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 1439495522:
                if (tag.equals("protected-broadcast")) {
                    c = 15;
                    break;
                }
                c = 65535;
                break;
            case 1682371816:
                if (tag.equals("feature-group")) {
                    c = '\f';
                    break;
                }
                c = 65535;
                break;
            case 1705921021:
                if (tag.equals("uses-permission-sdk-m")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            case 1792785909:
                if (tag.equals("uses-feature")) {
                    c = 11;
                    break;
                }
                c = 65535;
                break;
            case 1818228622:
                if (tag.equals("compatible-screens")) {
                    c = 20;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return parseOverlay(input, pkg, res, parser);
            case 1:
                return parseKeySets(input, pkg, res, parser);
            case 2:
            case 3:
                return parseAttribution(input, pkg, res, parser);
            case 4:
                return parsePermissionGroup(input, pkg, res, parser);
            case 5:
                return parsePermission(input, pkg, res, parser);
            case 6:
                return parsePermissionTree(input, pkg, res, parser);
            case 7:
            case '\b':
            case '\t':
                return parseUsesPermission(input, pkg, res, parser);
            case '\n':
                return parseUsesConfiguration(input, pkg, res, parser);
            case 11:
                return parseUsesFeature(input, pkg, res, parser);
            case '\f':
                return parseFeatureGroup(input, pkg, res, parser);
            case '\r':
                return parseUsesSdk(input, pkg, res, parser);
            case 14:
                return parseSupportScreens(input, pkg, res, parser);
            case 15:
                return parseProtectedBroadcast(input, pkg, res, parser);
            case 16:
                return parseInstrumentation(input, pkg, res, parser);
            case 17:
                return parseOriginalPackage(input, pkg, res, parser);
            case 18:
                return parseAdoptPermissions(input, pkg, res, parser);
            case 19:
            case 20:
            case 21:
            case 22:
                XmlUtils.skipCurrentTag(parser);
                return input.success(pkg);
            case 23:
                return parseRestrictUpdateHash(flags, input, pkg, res, parser);
            case 24:
                return parseQueries(input, pkg, res, parser);
            default:
                return ParsingUtils.unknownTag("<manifest>", pkg, parser, input);
        }
    }

    private static ParseResult<ParsingPackage> parseSharedUser(ParseInput input, ParsingPackage pkg, TypedArray sa) {
        String str = nonConfigString(0, 0, sa);
        if (TextUtils.isEmpty(str)) {
            return input.success(pkg);
        }
        if (!"android".equals(pkg.getPackageName())) {
            ParseResult<?> nameResult = validateName(input, str, true, true);
            if (nameResult.isError()) {
                return input.error(PackageManager.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID, "<manifest> specifies bad sharedUserId name \"" + str + "\": " + nameResult.getErrorMessage());
            }
        }
        return input.success(pkg.mo982setSharedUserId(str.intern()).mo983setSharedUserLabel(resId(3, sa)));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:10:0x0222, code lost:
        r0 = r22.getPackageName();
        r5 = r7.keySet();
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0234, code lost:
        if (r5.removeAll(r9.keySet()) == false) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x024e, code lost:
        return r21.error("Package" + r0 + " AndroidManifest.xml 'key-set' and 'public-key' names must be distinct.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x024f, code lost:
        r6 = r9.entrySet().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x025b, code lost:
        if (r6.hasNext() == false) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x025d, code lost:
        r14 = r6.next();
        r15 = r14.getKey();
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0275, code lost:
        if (r14.getValue().size() != 0) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x029d, code lost:
        if (r10.contains(r15) == false) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x02c1, code lost:
        r3 = r14.getValue().iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x02cf, code lost:
        if (r3.hasNext() == false) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x02d1, code lost:
        r4 = r3.next();
        r22.mo872addKeySet(r15, r7.get(r4));
        r3 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x029f, code lost:
        android.util.Slog.w("PackageParsing", "Package" + r0 + " AndroidManifest.xml 'key-set' " + r15 + " contained improper 'public-key' tags. Not including in package's defined key-sets.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0277, code lost:
        android.util.Slog.w("PackageParsing", "Package" + r0 + " AndroidManifest.xml 'key-set' " + r15 + " has no valid associated 'public-key'. Not including in package's defined key-sets.");
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x02f9, code lost:
        if (r22.getKeySetMapping().keySet().containsAll(r8) == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x02fb, code lost:
        r22.mo1001setUpgradeKeySets(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0302, code lost:
        return r21.success(r22);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x031b, code lost:
        return r21.error("Package" + r0 + " AndroidManifest.xml does not define all 'upgrade-key-set's .");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ParseResult<ParsingPackage> parseKeySets(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        int outerDepth;
        int currentKeySetDepth;
        char c;
        TypedArray sa;
        String publicKeyName;
        String encodedKey;
        int outerDepth2 = parser.getDepth();
        ArrayMap<String, PublicKey> publicKeys = new ArrayMap<>();
        ArraySet<String> upgradeKeySets = new ArraySet<>();
        ArrayMap<String, ArraySet<String>> definedKeySets = new ArrayMap<>();
        ArraySet<String> improperKeySets = new ArraySet<>();
        String currentKeySet = null;
        int currentKeySetDepth2 = -1;
        while (true) {
            int type = parser.next();
            if (type != 1 && (type != 3 || parser.getDepth() > outerDepth2)) {
                if (type != 3) {
                    String tagName = parser.getName();
                    switch (tagName.hashCode()) {
                        case -1369233085:
                            if (tagName.equals("upgrade-key-set")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case -816609292:
                            if (tagName.equals("key-set")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case 1903323387:
                            if (tagName.equals("public-key")) {
                                c = 1;
                                break;
                            }
                            c = 65535;
                            break;
                        default:
                            c = 65535;
                            break;
                    }
                    switch (c) {
                        case 0:
                            outerDepth = outerDepth2;
                            if (currentKeySet != null) {
                                return input.error("Improperly nested 'key-set' tag at " + parser.getPositionDescription());
                            }
                            sa = res.obtainAttributes(parser, R.styleable.AndroidManifestKeySet);
                            try {
                                String keysetName = sa.getNonResourceString(0);
                                definedKeySets.put(keysetName, new ArraySet<>());
                                currentKeySet = keysetName;
                                int currentKeySetDepth3 = parser.getDepth();
                                sa.recycle();
                                currentKeySetDepth2 = currentKeySetDepth3;
                                outerDepth2 = outerDepth;
                                break;
                            } finally {
                            }
                        case 1:
                            if (currentKeySet != null) {
                                sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPublicKey);
                                try {
                                    publicKeyName = nonResString(0, sa);
                                    outerDepth = outerDepth2;
                                    try {
                                        encodedKey = nonResString(1, sa);
                                        if (encodedKey == null) {
                                            try {
                                                if (publicKeys.get(publicKeyName) == null) {
                                                    StringBuilder sb = new StringBuilder();
                                                    try {
                                                        sb.append("'public-key' ");
                                                        sb.append(publicKeyName);
                                                        sb.append(" must define a public-key value on first use at ");
                                                        sb.append(parser.getPositionDescription());
                                                        return input.error(sb.toString());
                                                    } catch (Throwable th) {
                                                        th = th;
                                                    }
                                                }
                                            } catch (Throwable th2) {
                                                th = th2;
                                            }
                                        }
                                        currentKeySetDepth = currentKeySetDepth2;
                                    } catch (Throwable th3) {
                                        th = th3;
                                    }
                                } catch (Throwable th4) {
                                    th = th4;
                                }
                                if (encodedKey != null) {
                                    try {
                                        PublicKey currentKey = PackageParser.parsePublicKey(encodedKey);
                                        if (currentKey == null) {
                                            StringBuilder sb2 = new StringBuilder();
                                            try {
                                                sb2.append("No recognized valid key in 'public-key' tag at ");
                                                sb2.append(parser.getPositionDescription());
                                                sb2.append(" key-set ");
                                                sb2.append(currentKeySet);
                                                sb2.append(" will not be added to the package's defined key-sets.");
                                                Slog.w("PackageParsing", sb2.toString());
                                                improperKeySets.add(currentKeySet);
                                                XmlUtils.skipCurrentTag(parser);
                                                currentKeySetDepth2 = currentKeySetDepth;
                                                outerDepth2 = outerDepth;
                                                break;
                                            } catch (Throwable th5) {
                                                th = th5;
                                            }
                                        } else {
                                            if (publicKeys.get(publicKeyName) != null && !publicKeys.get(publicKeyName).equals(currentKey)) {
                                                return input.error("Value of 'public-key' " + publicKeyName + " conflicts with previously defined value at " + parser.getPositionDescription());
                                            }
                                            publicKeys.put(publicKeyName, currentKey);
                                        }
                                        th = th5;
                                    } catch (Throwable th6) {
                                        th = th6;
                                    }
                                    throw th;
                                }
                                definedKeySets.get(currentKeySet).add(publicKeyName);
                                XmlUtils.skipCurrentTag(parser);
                                currentKeySetDepth2 = currentKeySetDepth;
                                outerDepth2 = outerDepth;
                                break;
                            } else {
                                return input.error("Improperly nested 'key-set' tag at " + parser.getPositionDescription());
                            }
                        case 2:
                            sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUpgradeKeySet);
                            try {
                                String name = sa.getNonResourceString(0);
                                upgradeKeySets.add(name);
                                XmlUtils.skipCurrentTag(parser);
                                sa.recycle();
                                outerDepth = outerDepth2;
                                currentKeySetDepth = currentKeySetDepth2;
                                currentKeySetDepth2 = currentKeySetDepth;
                                outerDepth2 = outerDepth;
                                break;
                            } finally {
                            }
                        default:
                            outerDepth = outerDepth2;
                            currentKeySetDepth = currentKeySetDepth2;
                            ParseResult result = ParsingUtils.unknownTag("<key-sets>", pkg, parser, input);
                            if (result.isError()) {
                                return input.error(result);
                            }
                            currentKeySetDepth2 = currentKeySetDepth;
                            outerDepth2 = outerDepth;
                            break;
                    }
                } else if (parser.getDepth() == currentKeySetDepth2) {
                    currentKeySet = null;
                    currentKeySetDepth2 = -1;
                } else {
                    outerDepth = outerDepth2;
                    currentKeySetDepth = currentKeySetDepth2;
                    currentKeySetDepth2 = currentKeySetDepth;
                    outerDepth2 = outerDepth;
                }
            }
        }
    }

    private static ParseResult<ParsingPackage> parseAttribution(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws IOException, XmlPullParserException {
        ParseResult<ParsedAttribution> result = ParsedAttributionUtils.parseAttribution(res, parser, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.mo867addAttribution(result.getResult()));
    }

    private static ParseResult<ParsingPackage> parsePermissionGroup(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        ParseResult<ParsedPermissionGroup> result = ParsedPermissionUtils.parsePermissionGroup(pkg, res, parser, sUseRoundIcon, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.mo876addPermissionGroup(result.getResult()));
    }

    private static ParseResult<ParsingPackage> parsePermission(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        ParseResult<ParsedPermission> result = ParsedPermissionUtils.parsePermission(pkg, res, parser, sUseRoundIcon, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.mo875addPermission(result.getResult()));
    }

    private static ParseResult<ParsingPackage> parsePermissionTree(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        ParseResult<ParsedPermission> result = ParsedPermissionUtils.parsePermissionTree(pkg, res, parser, sUseRoundIcon, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.mo875addPermission(result.getResult()));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private ParseResult<ParsingPackage> parseUsesPermission(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws IOException, XmlPullParserException {
        int type;
        int maxSdkVersion;
        int i;
        int outerDepth;
        char c;
        ParseResult<?> result;
        ParseInput parseInput = input;
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesPermission);
        try {
            String name = sa.getNonResourceString(0);
            int maxSdkVersion2 = 0;
            int i2 = 1;
            TypedValue val = sa.peekValue(1);
            if (val != null && val.type >= 16 && val.type <= 31) {
                maxSdkVersion2 = val.data;
            }
            ArraySet<String> requiredFeatures = new ArraySet<>();
            String feature = sa.getNonConfigurationString(2, 0);
            if (feature != null) {
                requiredFeatures.add(feature);
            }
            ArraySet<String> requiredNotFeatures = new ArraySet<>();
            int i3 = 3;
            String feature2 = sa.getNonConfigurationString(3, 0);
            if (feature2 != null) {
                requiredNotFeatures.add(feature2);
            }
            int usesPermissionFlags = sa.getInt(4, 0);
            int outerDepth2 = parser.getDepth();
            while (true) {
                int type2 = parser.next();
                if (type2 != i2) {
                    type = type2;
                    if (type == i3) {
                        outerDepth = outerDepth2;
                        if (parser.getDepth() > outerDepth) {
                        }
                    } else {
                        outerDepth = outerDepth2;
                    }
                    if (type == i3) {
                        outerDepth2 = outerDepth;
                        i2 = 1;
                        i3 = 3;
                    } else if (type == 4) {
                        outerDepth2 = outerDepth;
                        i2 = 1;
                    } else {
                        String name2 = parser.getName();
                        switch (name2.hashCode()) {
                            case 874138830:
                                if (name2.equals("required-not-feature")) {
                                    c = 1;
                                    break;
                                }
                                c = 65535;
                                break;
                            case 1693350600:
                                if (name2.equals("required-feature")) {
                                    c = 0;
                                    break;
                                }
                                c = 65535;
                                break;
                            default:
                                c = 65535;
                                break;
                        }
                        switch (c) {
                            case 0:
                                result = parseRequiredFeature(parseInput, res, parser);
                                if (result.isSuccess()) {
                                    requiredFeatures.add(result.getResult());
                                    break;
                                }
                                break;
                            case 1:
                                result = parseRequiredNotFeature(parseInput, res, parser);
                                if (result.isSuccess()) {
                                    requiredNotFeatures.add(result.getResult());
                                    break;
                                }
                                break;
                            default:
                                result = ParsingUtils.unknownTag("<uses-permission>", pkg, parser, parseInput);
                                break;
                        }
                        if (result.isError()) {
                            return parseInput.error(result);
                        }
                        outerDepth2 = outerDepth;
                        i2 = 1;
                        i3 = 3;
                    }
                } else {
                    type = type2;
                }
            }
            ParseResult<ParsingPackage> success = input.success(pkg);
            if (name == null) {
                return success;
            }
            if (maxSdkVersion2 != 0 && maxSdkVersion2 < Build.VERSION.RESOURCES_SDK_INT) {
                return success;
            }
            if (this.mCallback != null) {
                int i4 = requiredFeatures.size() - 1;
                while (i4 >= 0) {
                    int type3 = type;
                    if (!this.mCallback.hasFeature(requiredFeatures.valueAt(i4))) {
                        return success;
                    }
                    i4--;
                    type = type3;
                }
                for (int i5 = requiredNotFeatures.size() - 1; i5 >= 0; i5--) {
                    if (this.mCallback.hasFeature(requiredNotFeatures.valueAt(i5))) {
                        return success;
                    }
                }
            }
            List<ParsedUsesPermission> usesPermissions = pkg.getUsesPermissions();
            int size = usesPermissions.size();
            int i6 = 0;
            while (true) {
                if (i6 < size) {
                    ParsedUsesPermission usesPermission = usesPermissions.get(i6);
                    List<ParsedUsesPermission> usesPermissions2 = usesPermissions;
                    if (Objects.equals(usesPermission.name, name)) {
                        maxSdkVersion = usesPermissionFlags;
                        if (usesPermission.usesPermissionFlags != maxSdkVersion) {
                            return parseInput.error("Conflicting uses-permissions flags: " + name + " in package: " + pkg.getPackageName() + " at: " + parser.getPositionDescription());
                        }
                        Slog.w("PackageParsing", "Ignoring duplicate uses-permissions/uses-permissions-sdk-m: " + name + " in package: " + pkg.getPackageName() + " at: " + parser.getPositionDescription());
                        i = 1;
                    } else {
                        i6++;
                        parseInput = input;
                        usesPermissions = usesPermissions2;
                        usesPermissionFlags = usesPermissionFlags;
                        maxSdkVersion2 = maxSdkVersion2;
                    }
                } else {
                    maxSdkVersion = usesPermissionFlags;
                    i = 0;
                }
            }
            if (i == 0) {
                pkg.mo891addUsesPermission(new ParsedUsesPermission(name, maxSdkVersion));
            }
            return success;
        } finally {
            sa.recycle();
        }
    }

    private ParseResult<String> parseRequiredFeature(ParseInput input, Resources res, AttributeSet attrs) {
        ParseResult<String> success;
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestRequiredFeature);
        try {
            String featureName = sa.getString(0);
            if (TextUtils.isEmpty(featureName)) {
                success = input.error("Feature name is missing from <required-feature> tag.");
            } else {
                success = input.success(featureName);
            }
            return success;
        } finally {
            sa.recycle();
        }
    }

    private ParseResult<String> parseRequiredNotFeature(ParseInput input, Resources res, AttributeSet attrs) {
        ParseResult<String> success;
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestRequiredNotFeature);
        try {
            String featureName = sa.getString(0);
            if (TextUtils.isEmpty(featureName)) {
                success = input.error("Feature name is missing from <required-not-feature> tag.");
            } else {
                success = input.success(featureName);
            }
            return success;
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseUsesConfiguration(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        ConfigurationInfo cPref = new ConfigurationInfo();
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesConfiguration);
        try {
            cPref.reqTouchScreen = sa.getInt(0, 0);
            cPref.reqKeyboardType = sa.getInt(1, 0);
            if (sa.getBoolean(2, false)) {
                cPref.reqInputFeatures = 1 | cPref.reqInputFeatures;
            }
            cPref.reqNavigation = sa.getInt(3, 0);
            if (sa.getBoolean(4, false)) {
                cPref.reqInputFeatures |= 2;
            }
            pkg.mo868addConfigPreference(cPref);
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseUsesFeature(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        FeatureInfo fi = parseFeatureInfo(res, parser);
        pkg.mo885addReqFeature(fi);
        if (fi.name == null) {
            ConfigurationInfo cPref = new ConfigurationInfo();
            cPref.reqGlEsVersion = fi.reqGlEsVersion;
            pkg.mo868addConfigPreference(cPref);
        }
        return input.success(pkg);
    }

    private static FeatureInfo parseFeatureInfo(Resources res, AttributeSet attrs) {
        FeatureInfo fi = new FeatureInfo();
        TypedArray sa = res.obtainAttributes(attrs, R.styleable.AndroidManifestUsesFeature);
        try {
            fi.name = sa.getNonResourceString(0);
            fi.version = sa.getInt(3, 0);
            if (fi.name == null) {
                fi.reqGlEsVersion = sa.getInt(1, 0);
            }
            if (sa.getBoolean(2, true)) {
                fi.flags |= 1;
            }
            return fi;
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseFeatureGroup(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws IOException, XmlPullParserException {
        FeatureGroupInfo group = new FeatureGroupInfo();
        ArrayList<FeatureInfo> features = null;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= depth)) {
                break;
            } else if (type == 2) {
                String innerTagName = parser.getName();
                if (innerTagName.equals("uses-feature")) {
                    FeatureInfo featureInfo = parseFeatureInfo(res, parser);
                    featureInfo.flags = 1 | featureInfo.flags;
                    features = ArrayUtils.add(features, featureInfo);
                } else {
                    Slog.w("PackageParsing", "Unknown element under <feature-group>: " + innerTagName + " at " + pkg.getBaseApkPath() + " " + parser.getPositionDescription());
                }
            }
        }
        if (features != null) {
            group.features = new FeatureInfo[features.size()];
            group.features = (FeatureInfo[]) features.toArray(group.features);
        }
        pkg.mo869addFeatureGroup(group);
        return input.success(pkg);
    }

    private static ParseResult<ParsingPackage> parseUsesSdk(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws IOException, XmlPullParserException {
        ParseResult result;
        int i = SDK_VERSION;
        if (i > 0) {
            TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesSdk);
            int minVers = 1;
            String minCode = null;
            int targetVers = 0;
            String targetCode = null;
            try {
                TypedValue val = sa.peekValue(0);
                if (val != null) {
                    if (val.type != 3 || val.string == null) {
                        minVers = val.data;
                    } else {
                        minCode = val.string.toString();
                    }
                }
                TypedValue val2 = sa.peekValue(1);
                if (val2 == null) {
                    targetVers = minVers;
                    targetCode = minCode;
                } else if (val2.type != 3 || val2.string == null) {
                    targetVers = val2.data;
                } else {
                    targetCode = val2.string.toString();
                    if (minCode == null) {
                        minCode = targetCode;
                    }
                }
                String[] strArr = SDK_CODENAMES;
                ParseResult<Integer> targetSdkVersionResult = computeTargetSdkVersion(targetVers, targetCode, strArr, input);
                if (targetSdkVersionResult.isError()) {
                    return input.error(targetSdkVersionResult);
                }
                int targetSdkVersion = targetSdkVersionResult.getResult().intValue();
                ParseResult<?> deferResult = input.enableDeferredError(pkg.getPackageName(), targetSdkVersion);
                if (deferResult.isError()) {
                    return input.error(deferResult);
                }
                ParseResult<Integer> minSdkVersionResult = computeMinSdkVersion(minVers, minCode, i, strArr, input);
                if (minSdkVersionResult.isError()) {
                    return input.error(minSdkVersionResult);
                }
                int minSdkVersion = minSdkVersionResult.getResult().intValue();
                pkg.mo949setMinSdkVersion(minSdkVersion).mo996setTargetSdkVersion(targetSdkVersion);
                int innerDepth = parser.getDepth();
                SparseIntArray minExtensionVersions = null;
                while (true) {
                    ParseResult<Integer> minSdkVersionResult2 = minSdkVersionResult;
                    int type = parser.next();
                    int minVers2 = minVers;
                    if (type == 1) {
                        break;
                    }
                    if (type == 3 && parser.getDepth() <= innerDepth) {
                        break;
                    }
                    if (type != 3 && type != 4) {
                        if (parser.getName().equals("extension-sdk")) {
                            SparseIntArray minExtensionVersions2 = minExtensionVersions == null ? new SparseIntArray() : minExtensionVersions;
                            result = parseExtensionSdk(input, res, parser, minExtensionVersions2);
                            XmlUtils.skipCurrentTag(parser);
                            minExtensionVersions = minExtensionVersions2;
                        } else {
                            result = ParsingUtils.unknownTag("<uses-sdk>", pkg, parser, input);
                        }
                        if (result.isError()) {
                            return input.error(result);
                        }
                        minSdkVersionResult = minSdkVersionResult2;
                        minVers = minVers2;
                    }
                    minSdkVersionResult = minSdkVersionResult2;
                    minVers = minVers2;
                }
                pkg.mo948setMinExtensionVersions(exactSizedCopyOfSparseArray(minExtensionVersions));
            } finally {
                sa.recycle();
            }
        }
        return input.success(pkg);
    }

    private static SparseIntArray exactSizedCopyOfSparseArray(SparseIntArray input) {
        if (input == null) {
            return null;
        }
        SparseIntArray output = new SparseIntArray(input.size());
        for (int i = 0; i < input.size(); i++) {
            output.put(input.keyAt(i), input.valueAt(i));
        }
        return output;
    }

    private static ParseResult<SparseIntArray> parseExtensionSdk(ParseInput input, Resources res, XmlResourceParser parser, SparseIntArray minExtensionVersions) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestExtensionSdk);
        try {
            int sdkVersion = sa.getInt(0, -1);
            int minVersion = sa.getInt(1, -1);
            sa.recycle();
            if (sdkVersion < 0) {
                return input.error(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<extension-sdk> must specify an sdkVersion >= 0");
            }
            if (minVersion < 0) {
                return input.error(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<extension-sdk> must specify minExtensionVersion >= 0");
            }
            try {
                int version = SdkExtensions.getExtensionVersion(sdkVersion);
                if (version < minVersion) {
                    return input.error(-12, "Package requires " + sdkVersion + " extension version " + minVersion + " which exceeds device version " + version);
                }
                minExtensionVersions.put(sdkVersion, minVersion);
                return input.success(minExtensionVersions);
            } catch (RuntimeException e) {
                return input.error(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Specified sdkVersion " + sdkVersion + " is not valid");
            }
        } catch (Throwable th) {
            sa.recycle();
            throw th;
        }
    }

    public static ParseResult<Integer> computeMinSdkVersion(int minVers, String minCode, int platformSdkVersion, String[] platformSdkCodenames, ParseInput input) {
        if (minCode == null) {
            if (minVers <= platformSdkVersion) {
                return input.success(Integer.valueOf(minVers));
            }
            return input.error(-12, "Requires newer sdk version #" + minVers + " (current version is #" + platformSdkVersion + ")");
        } else if (matchTargetCode(platformSdkCodenames, minCode)) {
            return input.success(10000);
        } else {
            if (platformSdkCodenames.length > 0) {
                return input.error(-12, "Requires development platform " + minCode + " (current platform is any of " + Arrays.toString(platformSdkCodenames) + ")");
            }
            return input.error(-12, "Requires development platform " + minCode + " but this is a release platform.");
        }
    }

    public static ParseResult<Integer> computeTargetSdkVersion(int targetVers, String targetCode, String[] platformSdkCodenames, ParseInput input) {
        if (targetCode == null) {
            return input.success(Integer.valueOf(targetVers));
        }
        if (matchTargetCode(platformSdkCodenames, targetCode)) {
            return input.success(10000);
        }
        if (platformSdkCodenames.length > 0) {
            return input.error(-12, "Requires development platform " + targetCode + " (current platform is any of " + Arrays.toString(platformSdkCodenames) + ")");
        }
        return input.error(-12, "Requires development platform " + targetCode + " but this is a release platform.");
    }

    private static boolean matchTargetCode(String[] codeNames, String targetCode) {
        String targetCodeName;
        int targetCodeIdx = targetCode.indexOf(46);
        if (targetCodeIdx == -1) {
            targetCodeName = targetCode;
        } else {
            targetCodeName = targetCode.substring(0, targetCodeIdx);
        }
        return ArrayUtils.contains(codeNames, targetCodeName);
    }

    private static ParseResult<ParsingPackage> parseRestrictUpdateHash(int flags, ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        if ((flags & 16) != 0) {
            TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestRestrictUpdate);
            try {
                String hash = sa.getNonConfigurationString(0, 0);
                if (hash != null) {
                    int hashLength = hash.length();
                    byte[] hashBytes = new byte[hashLength / 2];
                    for (int i = 0; i < hashLength; i += 2) {
                        hashBytes[i / 2] = (byte) ((Character.digit(hash.charAt(i), 16) << 4) + Character.digit(hash.charAt(i + 1), 16));
                    }
                    pkg.mo979setRestrictUpdateHash(hashBytes);
                } else {
                    pkg.mo979setRestrictUpdateHash(null);
                }
            } finally {
                sa.recycle();
            }
        }
        return input.success(pkg);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x01c4, code lost:
        return r20.success(r21);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ParseResult<ParsingPackage> parseQueries(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws IOException, XmlPullParserException {
        int i;
        String dataType;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= depth)) {
                break;
            } else if (type == 2) {
                if (parser.getName().equals("intent")) {
                    ParseResult<ParsedIntentInfo> result = ParsedIntentInfoUtils.parseIntentInfo(null, pkg, res, parser, true, true, input);
                    if (result.isError()) {
                        return input.error(result);
                    }
                    ParsedIntentInfo intentInfo = result.getResult();
                    Uri data = null;
                    String dataType2 = null;
                    String host = null;
                    int numActions = intentInfo.countActions();
                    int numSchemes = intentInfo.countDataSchemes();
                    int numTypes = intentInfo.countDataTypes();
                    int numHosts = intentInfo.getHosts().length;
                    if (numSchemes == 0 && numTypes == 0 && numActions == 0) {
                        return input.error("intent tags must contain either an action or data.");
                    }
                    if (numActions > 1) {
                        return input.error("intent tag may have at most one action.");
                    }
                    if (numTypes > 1) {
                        return input.error("intent tag may have at most one data type.");
                    }
                    if (numSchemes > 1) {
                        return input.error("intent tag may have at most one data scheme.");
                    }
                    if (numHosts > 1) {
                        return input.error("intent tag may have at most one data host.");
                    }
                    Intent intent = new Intent();
                    int max = intentInfo.countCategories();
                    int i2 = 0;
                    while (i2 < max) {
                        intent.addCategory(intentInfo.getCategory(i2));
                        i2++;
                        data = data;
                        dataType2 = dataType2;
                    }
                    Uri data2 = data;
                    String dataType3 = dataType2;
                    if (numHosts != 1) {
                        i = 0;
                    } else {
                        i = 0;
                        host = intentInfo.getHosts()[0];
                    }
                    Uri data3 = numSchemes == 1 ? new Uri.Builder().scheme(intentInfo.getDataScheme(i)).authority(host).path(IntentFilter.WILDCARD_PATH).build() : data2;
                    if (numTypes == 1) {
                        dataType = intentInfo.getDataType(0);
                        if (!dataType.contains("/")) {
                            dataType = dataType + IntentFilter.WILDCARD_PATH;
                        }
                        if (data3 == null) {
                            data3 = new Uri.Builder().scheme("content").authority("*").path(IntentFilter.WILDCARD_PATH).build();
                        }
                    } else {
                        dataType = dataType3;
                    }
                    intent.setDataAndType(data3, dataType);
                    if (numActions == 1) {
                        intent.setAction(intentInfo.getAction(0));
                    }
                    pkg.mo881addQueriesIntent(intent);
                } else if (parser.getName().equals("package")) {
                    String packageName = res.obtainAttributes(parser, R.styleable.AndroidManifestQueriesPackage).getNonConfigurationString(0, 0);
                    if (TextUtils.isEmpty(packageName)) {
                        return input.error("Package name is missing from package tag.");
                    }
                    pkg.mo882addQueriesPackage(packageName.intern());
                } else if (parser.getName().equals("provider")) {
                    TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestQueriesProvider);
                    try {
                        String authorities = sa.getNonConfigurationString(0, 0);
                        if (TextUtils.isEmpty(authorities)) {
                            return input.error(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "Authority missing from provider tag.");
                        }
                        StringTokenizer authoritiesTokenizer = new StringTokenizer(authorities, ";");
                        while (authoritiesTokenizer.hasMoreElements()) {
                            pkg.mo883addQueriesProvider(authoritiesTokenizer.nextToken());
                        }
                    } finally {
                        sa.recycle();
                    }
                } else {
                    continue;
                }
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0462, code lost:
        if (android.text.TextUtils.isEmpty(r29.getStaticSharedLibName()) == false) goto L153;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x0464, code lost:
        r1 = generateAppDetailsHiddenActivity(r28, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x046c, code lost:
        if (r1.isError() == false) goto L152;
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x0472, code lost:
        return r28.error(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x0473, code lost:
        r29.mo865addActivity(r1.getResult());
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x047c, code lost:
        if (r0 == 0) goto L155;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x047e, code lost:
        r29.mo1011sortActivities();
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0481, code lost:
        if (r17 == 0) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0483, code lost:
        r29.mo1012sortReceivers();
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0486, code lost:
        if (r18 == 0) goto L159;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0488, code lost:
        r29.mo1013sortServices();
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x048b, code lost:
        setMaxAspectRatio(r29);
        setMinAspectRatio(r29);
        setSupportsSizeChanges(r29);
        r29.mo933setHasDomainUrls(hasDomainURLs(r29));
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x049f, code lost:
        return r28.success(r29);
     */
    /* JADX WARN: Removed duplicated region for block: B:202:0x0415  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private ParseResult<ParsingPackage> parseBaseApplication(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser, int flags) throws XmlPullParserException, IOException {
        String taskAffinity;
        char c;
        char c2;
        char c3;
        char c4;
        char c5;
        int depth;
        ParseResult activityResult;
        ParseResult result;
        boolean z;
        String tagName = pkg.getPackageName();
        int targetSdk = pkg.getTargetSdkVersion();
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestApplication);
        try {
            if (sa == null) {
                ParseResult<ParsingPackage> error = input.error("<application> does not contain any attributes");
                sa.recycle();
                return error;
            }
            try {
                String name = sa.getNonConfigurationString(3, 0);
                if (name != null) {
                    String packageName = pkg.getPackageName();
                    String outInfoName = ParsingUtils.buildClassName(packageName, name);
                    if (PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(outInfoName)) {
                        ParseResult<ParsingPackage> error2 = input.error("<application> invalid android:name");
                        sa.recycle();
                        return error2;
                    } else if (outInfoName == null) {
                        ParseResult<ParsingPackage> error3 = input.error("Empty class name in package " + packageName);
                        sa.recycle();
                        return error3;
                    } else {
                        pkg.mo915setClassName(outInfoName);
                    }
                }
                TypedValue labelValue = sa.peekValue(1);
                if (labelValue != null) {
                    pkg.mo939setLabelRes(labelValue.resourceId);
                    if (labelValue.resourceId == 0) {
                        pkg.mo953setNonLocalizedLabel(labelValue.coerceToString());
                    }
                }
                parseBaseAppBasicFlags(pkg, sa);
                String manageSpaceActivity = nonConfigString(1024, 4, sa);
                if (manageSpaceActivity != null) {
                    String manageSpaceActivityName = ParsingUtils.buildClassName(tagName, manageSpaceActivity);
                    if (manageSpaceActivityName == null) {
                        ParseResult<ParsingPackage> error4 = input.error("Empty class name in package " + tagName);
                        sa.recycle();
                        return error4;
                    }
                    pkg.mo943setManageSpaceActivityName(manageSpaceActivityName);
                }
                if (pkg.isAllowBackup()) {
                    String backupAgent = nonConfigString(1024, 16, sa);
                    if (backupAgent != null) {
                        String backupAgentName = ParsingUtils.buildClassName(tagName, backupAgent);
                        if (backupAgentName == null) {
                            ParseResult<ParsingPackage> error5 = input.error("Empty class name in package " + tagName);
                            sa.recycle();
                            return error5;
                        }
                        pkg.mo907setBackupAgentName(backupAgentName).mo938setKillAfterRestore(bool(true, 18, sa)).mo978setRestoreAnyVersion(bool(false, 21, sa)).mo929setFullBackupOnly(bool(false, 32, sa)).mo908setBackupInForeground(bool(false, 40, sa));
                    }
                    TypedValue v = sa.peekValue(35);
                    if (v != null) {
                        int fullBackupContent = v.resourceId;
                        if (v.resourceId == 0) {
                            fullBackupContent = v.data == 0 ? -1 : 0;
                        }
                        pkg.mo928setFullBackupContent(fullBackupContent);
                    }
                }
                if (sa.getBoolean(8, false)) {
                    String requiredFeature = sa.getNonResourceString(45);
                    if (requiredFeature != null && !this.mCallback.hasFeature(requiredFeature)) {
                        z = false;
                        pkg.mo962setPersistent(z);
                    }
                    z = true;
                    pkg.mo962setPersistent(z);
                }
                if (sa.hasValueOrEmpty(37)) {
                    pkg.mo976setResizeableActivity(Boolean.valueOf(sa.getBoolean(37, true)));
                } else {
                    pkg.mo977setResizeableActivityViaSdkVersion(targetSdk >= 24);
                }
                if (targetSdk >= 8) {
                    taskAffinity = sa.getNonConfigurationString(12, 1024);
                } else {
                    String taskAffinity2 = sa.getNonResourceString(12);
                    taskAffinity = taskAffinity2;
                }
                ParseResult<String> taskAffinityResult = ComponentParseUtils.buildTaskAffinityName(tagName, tagName, taskAffinity, input);
                if (taskAffinityResult.isError()) {
                    ParseResult<ParsingPackage> error6 = input.error(taskAffinityResult);
                    sa.recycle();
                    return error6;
                }
                pkg.mo997setTaskAffinity(taskAffinityResult.getResult());
                String factory = sa.getNonResourceString(48);
                if (factory != null) {
                    String appComponentFactory = ParsingUtils.buildClassName(tagName, factory);
                    if (appComponentFactory == null) {
                        ParseResult<ParsingPackage> error7 = input.error("Empty class name in package " + tagName);
                        sa.recycle();
                        return error7;
                    }
                    pkg.mo905setAppComponentFactory(appComponentFactory);
                }
                ParseResult<String> processNameResult = ComponentParseUtils.buildProcessName(tagName, null, targetSdk >= 8 ? sa.getNonConfigurationString(11, 1024) : sa.getNonResourceString(11), flags, this.mSeparateProcesses, input);
                if (processNameResult.isError()) {
                    ParseResult<ParsingPackage> error8 = input.error(processNameResult);
                    sa.recycle();
                    return error8;
                }
                String processName = processNameResult.getResult();
                pkg.mo964setProcessName(processName);
                if (pkg.isCantSaveState() && processName != null && !processName.equals(tagName)) {
                    ParseResult<ParsingPackage> error9 = input.error("cantSaveState applications can not use custom processes");
                    sa.recycle();
                    return error9;
                }
                String classLoaderName = pkg.getClassLoaderName();
                if (classLoaderName != null && !ClassLoaderFactory.isValidClassLoaderName(classLoaderName)) {
                    ParseResult<ParsingPackage> error10 = input.error("Invalid class loader name: " + classLoaderName);
                    sa.recycle();
                    return error10;
                }
                char c6 = 65535;
                pkg.mo931setGwpAsanMode(sa.getInt(62, -1));
                pkg.mo945setMemtagMode(sa.getInt(64, -1));
                if (sa.hasValue(65)) {
                    pkg.mo951setNativeHeapZeroInitialized(Boolean.valueOf(sa.getBoolean(65, false)).booleanValue() ? 1 : 0);
                }
                if (sa.hasValue(67)) {
                    pkg.mo971setRequestRawExternalStorageAccess(Boolean.valueOf(sa.getBoolean(67, false)));
                }
                if (sa.hasValue(68)) {
                    c = 0;
                    pkg.mo969setRequestForegroundServiceExemption(sa.getBoolean(68, false));
                } else {
                    c = 0;
                }
                sa.recycle();
                int i = 0;
                int depth2 = parser.getDepth();
                int i2 = 0;
                int i3 = 0;
                while (true) {
                    int type = parser.next();
                    if (type != 1 && (type != 3 || parser.getDepth() > depth2)) {
                        if (type == 2) {
                            String pkgName = tagName;
                            String tagName2 = parser.getName();
                            boolean isActivity = false;
                            switch (tagName2.hashCode()) {
                                case -1655966961:
                                    if (tagName2.equals("activity")) {
                                        c2 = c;
                                        break;
                                    }
                                    c2 = c6;
                                    break;
                                case -987494927:
                                    if (tagName2.equals("provider")) {
                                        c2 = 3;
                                        break;
                                    }
                                    c2 = c6;
                                    break;
                                case -808719889:
                                    if (tagName2.equals(IncidentManager.URI_PARAM_RECEIVER_CLASS)) {
                                        c2 = 1;
                                        break;
                                    }
                                    c2 = c6;
                                    break;
                                case 790287890:
                                    if (tagName2.equals("activity-alias")) {
                                        c2 = 4;
                                        break;
                                    }
                                    c2 = c6;
                                    break;
                                case 1984153269:
                                    if (tagName2.equals("service")) {
                                        c2 = 2;
                                        break;
                                    }
                                    c2 = c6;
                                    break;
                                default:
                                    c2 = c6;
                                    break;
                            }
                            switch (c2) {
                                case 0:
                                    c3 = 1;
                                    c4 = c;
                                    c5 = c6;
                                    depth = depth2;
                                    isActivity = true;
                                    activityResult = ParsedActivityUtils.parseActivityOrReceiver(this.mSeparateProcesses, pkg, res, parser, flags, sUseRoundIcon, input);
                                    if (activityResult.isSuccess()) {
                                        ParsedActivity activity = activityResult.getResult();
                                        if (isActivity) {
                                            i |= activity.getOrder() != 0 ? c3 : c4;
                                            pkg.mo865addActivity(activity);
                                        } else {
                                            i2 |= activity.getOrder() != 0 ? c3 : c4;
                                            pkg.mo884addReceiver(activity);
                                        }
                                    }
                                    result = activityResult;
                                    break;
                                case 1:
                                    c3 = 1;
                                    c4 = c;
                                    c5 = c6;
                                    depth = depth2;
                                    activityResult = ParsedActivityUtils.parseActivityOrReceiver(this.mSeparateProcesses, pkg, res, parser, flags, sUseRoundIcon, input);
                                    if (activityResult.isSuccess()) {
                                    }
                                    result = activityResult;
                                    break;
                                case 2:
                                    c4 = c;
                                    c5 = c6;
                                    depth = depth2;
                                    ParseResult serviceResult = ParsedServiceUtils.parseService(this.mSeparateProcesses, pkg, res, parser, flags, sUseRoundIcon, input);
                                    if (serviceResult.isSuccess()) {
                                        ParsedService service = serviceResult.getResult();
                                        i3 |= service.getOrder() != 0 ? (char) 1 : c4;
                                        pkg.mo886addService(service);
                                    }
                                    result = serviceResult;
                                    break;
                                case 3:
                                    c5 = c6;
                                    c4 = c;
                                    depth = depth2;
                                    ParseResult providerResult = ParsedProviderUtils.parseProvider(this.mSeparateProcesses, pkg, res, parser, flags, sUseRoundIcon, input);
                                    if (providerResult.isSuccess()) {
                                        pkg.mo880addProvider(providerResult.getResult());
                                    }
                                    result = providerResult;
                                    break;
                                case 4:
                                    ParseResult activityResult2 = ParsedActivityUtils.parseActivityAlias(pkg, res, parser, sUseRoundIcon, input);
                                    if (activityResult2.isSuccess()) {
                                        ParsedActivity activity2 = activityResult2.getResult();
                                        i |= activity2.getOrder() != 0 ? (char) 1 : c;
                                        pkg.mo865addActivity(activity2);
                                    }
                                    result = activityResult2;
                                    c4 = c;
                                    c5 = c6;
                                    depth = depth2;
                                    break;
                                default:
                                    c4 = c;
                                    c5 = c6;
                                    depth = depth2;
                                    result = parseBaseAppChildTag(input, tagName2, pkg, res, parser, flags);
                                    break;
                            }
                            if (result.isError()) {
                                return input.error(result);
                            }
                            tagName = pkgName;
                            c = c4;
                            depth2 = depth;
                            c6 = c5;
                        }
                    }
                }
            } catch (Throwable th) {
                th = th;
                sa.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private void parseBaseAppBasicFlags(ParsingPackage pkg, TypedArray sa) {
        boolean z;
        boolean z2;
        boolean z3;
        int targetSdk = pkg.getTargetSdkVersion();
        boolean z4 = true;
        ParsingPackage attributionsAreUserVisible = pkg.mo899setAllowBackup(bool(true, 17, sa)).mo900setAllowClearUserData(bool(true, 5, sa)).mo901setAllowClearUserDataOnFailedRestore(bool(true, 54, sa)).mo902setAllowNativeHeapPointerTagging(bool(true, 59, sa)).mo924setEnabled(bool(true, 9, sa)).mo926setExtractNativeLibs(bool(true, 34, sa)).mo932setHasCode(bool(true, 7, sa)).mo903setAllowTaskReparenting(bool(false, 14, sa)).mo912setCantSaveState(bool(false, 47, sa)).mo918setCrossProfile(bool(false, 58, sa)).mo920setDebuggable(bool(false, 10, sa)).mo921setDefaultToDeviceProtectedStorage(bool(false, 38, sa)).mo923setDirectBootAware(bool(false, 39, sa)).mo927setForceQueryable(bool(false, 57, sa)).mo930setGame(bool(false, 31, sa)).mo934setHasFragileUserData(bool(false, 50, sa)).mo940setLargeHeap(bool(false, 24, sa)).mo950setMultiArch(bool(false, 33, sa)).mo963setPreserveLegacyExternalStorage(bool(false, 61, sa)).mo973setRequiredForAllUsers(bool(false, 27, sa)).mo993setSupportsRtl(bool(false, 26, sa)).mo998setTestOnly(bool(false, 15, sa)).mo1003setUseEmbeddedDex(bool(false, 53, sa)).mo1005setUsesNonSdkApi(bool(false, 49, sa)).mo1008setVmSafeMode(bool(false, 20, sa)).mo906setAutoRevokePermissions(anInt(60, sa)).setAttributionsAreUserVisible(bool(false, 69, sa));
        if (targetSdk >= 29) {
            z = true;
        } else {
            z = false;
        }
        ParsingPackage mo898setAllowAudioPlaybackCapture = attributionsAreUserVisible.mo898setAllowAudioPlaybackCapture(bool(z, 55, sa));
        if (targetSdk >= 14) {
            z2 = true;
        } else {
            z2 = false;
        }
        ParsingPackage mo910setBaseHardwareAccelerated = mo898setAllowAudioPlaybackCapture.mo910setBaseHardwareAccelerated(bool(z2, 23, sa));
        if (targetSdk < 29) {
            z3 = true;
        } else {
            z3 = false;
        }
        ParsingPackage mo970setRequestLegacyExternalStorage = mo910setBaseHardwareAccelerated.mo970setRequestLegacyExternalStorage(bool(z3, 56, sa));
        if (targetSdk >= 28) {
            z4 = false;
        }
        mo970setRequestLegacyExternalStorage.mo1004setUsesCleartextTraffic(bool(z4, 36, sa)).mo1000setUiOptions(anInt(25, sa)).mo913setCategory(anInt(-1, 43, sa)).mo944setMaxAspectRatio(aFloat(44, sa)).mo947setMinAspectRatio(aFloat(51, sa)).mo909setBanner(resId(30, sa)).mo922setDescriptionRes(resId(13, sa)).mo935setIconRes(resId(2, sa)).mo942setLogo(resId(22, sa)).mo952setNetworkSecurityConfigRes(resId(41, sa)).mo981setRoundIconRes(resId(42, sa)).mo999setTheme(resId(0, sa)).mo919setDataExtractionRules(resId(66, sa)).mo914setClassLoaderName(string(46, sa)).mo972setRequiredAccountType(string(29, sa)).mo980setRestrictedAccountType(string(28, sa)).mo1010setZygotePreloadName(string(52, sa)).mo961setPermission(nonConfigString(0, 6, sa));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private ParseResult parseBaseAppChildTag(ParseInput input, String tag, ParsingPackage pkg, Resources res, XmlResourceParser parser, int flags) throws IOException, XmlPullParserException {
        char c;
        switch (tag.hashCode()) {
            case -1608941274:
                if (tag.equals("uses-native-library")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case -1356765254:
                if (tag.equals("uses-library")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case -1115949454:
                if (tag.equals("meta-data")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case -1094759587:
                if (tag.equals("processes")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case -1056667556:
                if (tag.equals("static-library")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case -993141291:
                if (tag.equals("property")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 8960125:
                if (tag.equals("uses-static-library")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 166208699:
                if (tag.equals("library")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 178070147:
                if (tag.equals("profileable")) {
                    c = '\t';
                    break;
                }
                c = 65535;
                break;
            case 1964930885:
                if (tag.equals("uses-package")) {
                    c = '\b';
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                ParseResult<PackageManager.Property> metaDataResult = parseMetaData(pkg, null, res, parser, "<meta-data>", input);
                if (metaDataResult.isSuccess() && metaDataResult.getResult() != null) {
                    pkg.mo946setMetaData(metaDataResult.getResult().toBundle(pkg.getMetaData()));
                }
                return metaDataResult;
            case 1:
                ParseResult<PackageManager.Property> propertyResult = parseMetaData(pkg, null, res, parser, "<property>", input);
                if (propertyResult.isSuccess()) {
                    pkg.mo878addProperty(propertyResult.getResult());
                }
                return propertyResult;
            case 2:
                return parseStaticLibrary(pkg, res, parser, input);
            case 3:
                return parseLibrary(pkg, res, parser, input);
            case 4:
                return parseUsesStaticLibrary(input, pkg, res, parser);
            case 5:
                return parseUsesLibrary(input, pkg, res, parser);
            case 6:
                return parseUsesNativeLibrary(input, pkg, res, parser);
            case 7:
                return parseProcesses(input, pkg, res, parser, this.mSeparateProcesses, flags);
            case '\b':
                return input.success(null);
            case '\t':
                return parseProfileable(input, pkg, res, parser);
            default:
                return ParsingUtils.unknownTag("<application>", pkg, parser, input);
        }
    }

    private static ParseResult<ParsingPackage> parseStaticLibrary(ParsingPackage pkg, Resources res, XmlResourceParser parser, ParseInput input) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestStaticLibrary);
        try {
            String lname = sa.getNonResourceString(0);
            int version = sa.getInt(1, -1);
            int versionMajor = sa.getInt(2, 0);
            if (lname != null && version >= 0) {
                if (pkg.getSharedUserId() != null) {
                    return input.error(PackageManager.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID, "sharedUserId not allowed in static shared library");
                }
                if (pkg.getStaticSharedLibName() == null) {
                    return input.success(pkg.mo987setStaticSharedLibName(lname.intern()).mo988setStaticSharedLibVersion(PackageInfo.composeLongVersionCode(versionMajor, version)).mo989setStaticSharedLibrary(true));
                }
                return input.error("Multiple static-shared libs for package " + pkg.getPackageName());
            }
            return input.error("Bad static-library declaration name: " + lname + " version: " + version);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseLibrary(ParsingPackage pkg, Resources res, XmlResourceParser parser, ParseInput input) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestLibrary);
        try {
            String lname = sa.getNonResourceString(0);
            if (lname != null) {
                String lname2 = lname.intern();
                if (!ArrayUtils.contains(pkg.getLibraryNames(), lname2)) {
                    pkg.mo873addLibraryName(lname2);
                }
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseUsesStaticLibrary(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesStaticLibrary);
        try {
            String lname = sa.getNonResourceString(0);
            int version = sa.getInt(1, -1);
            String certSha256Digest = sa.getNonResourceString(2);
            if (lname != null && version >= 0 && certSha256Digest != null) {
                List<String> usesStaticLibraries = pkg.getUsesStaticLibraries();
                if (usesStaticLibraries.contains(lname)) {
                    return input.error("Depending on multiple versions of static library " + lname);
                }
                String lname2 = lname.intern();
                String certSha256Digest2 = certSha256Digest.replace(SettingsStringUtil.DELIMITER, "").toLowerCase();
                String[] additionalCertSha256Digests = EmptyArray.STRING;
                if (pkg.getTargetSdkVersion() >= 27) {
                    ParseResult<String[]> certResult = parseAdditionalCertificates(input, res, parser);
                    if (certResult.isError()) {
                        return input.error((ParseResult<?>) certResult);
                    }
                    additionalCertSha256Digests = certResult.getResult();
                }
                String[] certSha256Digests = new String[additionalCertSha256Digests.length + 1];
                certSha256Digests[0] = certSha256Digest2;
                System.arraycopy(additionalCertSha256Digests, 0, certSha256Digests, 1, additionalCertSha256Digests.length);
                return input.success(pkg.mo892addUsesStaticLibrary(lname2).mo894addUsesStaticLibraryVersion(version).mo893addUsesStaticLibraryCertDigests(certSha256Digests));
            }
            return input.error("Bad uses-static-library declaration name: " + lname + " version: " + version + " certDigest" + certSha256Digest);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseUsesLibrary(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesLibrary);
        try {
            String lname = sa.getNonResourceString(0);
            boolean req = sa.getBoolean(1, true);
            if (lname != null) {
                String lname2 = lname.intern();
                if (req) {
                    pkg.mo887addUsesLibrary(lname2).mo896removeUsesOptionalLibrary(lname2);
                } else if (!ArrayUtils.contains(pkg.getUsesLibraries(), lname2)) {
                    pkg.mo889addUsesOptionalLibrary(lname2);
                }
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseUsesNativeLibrary(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestUsesNativeLibrary);
        try {
            String lname = sa.getNonResourceString(0);
            boolean req = sa.getBoolean(1, true);
            if (lname != null) {
                if (req) {
                    pkg.mo888addUsesNativeLibrary(lname).mo897removeUsesOptionalNativeLibrary(lname);
                } else if (!ArrayUtils.contains(pkg.getUsesNativeLibraries(), lname)) {
                    pkg.mo890addUsesOptionalNativeLibrary(lname);
                }
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseProcesses(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser, String[] separateProcesses, int flags) throws IOException, XmlPullParserException {
        ParseResult<ArrayMap<String, ParsedProcess>> result = ParsedProcessUtils.parseProcesses(separateProcesses, pkg, res, parser, flags, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.mo965setProcesses(result.getResult()));
    }

    private static ParseResult<ParsingPackage> parseProfileable(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        boolean z;
        ParsingPackage newPkg;
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestProfileable);
        try {
            boolean z2 = false;
            if (!pkg.isProfileableByShell() && !bool(false, 1, sa)) {
                z = false;
                newPkg = pkg.mo967setProfileableByShell(z);
                if (newPkg.isProfileable() && bool(true, 0, sa)) {
                    z2 = true;
                }
                return input.success(newPkg.mo966setProfileable(z2));
            }
            z = true;
            newPkg = pkg.mo967setProfileableByShell(z);
            if (newPkg.isProfileable()) {
                z2 = true;
            }
            return input.success(newPkg.mo966setProfileable(z2));
        } finally {
            sa.recycle();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0075, code lost:
        return r8.success(r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ParseResult<String[]> parseAdditionalCertificates(ParseInput input, Resources resources, XmlResourceParser parser) throws XmlPullParserException, IOException {
        String[] certSha256Digests = EmptyArray.STRING;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= depth)) {
                break;
            } else if (type == 2) {
                String nodeName = parser.getName();
                if (nodeName.equals("additional-certificate")) {
                    TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestAdditionalCertificate);
                    try {
                        String certSha256Digest = sa.getNonResourceString(0);
                        if (TextUtils.isEmpty(certSha256Digest)) {
                            return input.error("Bad additional-certificate declaration with empty certDigest:" + certSha256Digest);
                        }
                        certSha256Digests = (String[]) ArrayUtils.appendElement(String.class, certSha256Digests, certSha256Digest.replace(SettingsStringUtil.DELIMITER, "").toLowerCase());
                    } finally {
                        sa.recycle();
                    }
                } else {
                    continue;
                }
            }
        }
    }

    private static ParseResult<ParsedActivity> generateAppDetailsHiddenActivity(ParseInput input, ParsingPackage pkg) {
        String packageName = pkg.getPackageName();
        ParseResult<String> result = ComponentParseUtils.buildTaskAffinityName(packageName, packageName, ":app_details", input);
        if (result.isError()) {
            return input.error(result);
        }
        String taskAffinity = result.getResult();
        return input.success(ParsedActivity.makeAppDetailsActivity(packageName, pkg.getProcessName(), pkg.getUiOptions(), taskAffinity, pkg.isBaseHardwareAccelerated()));
    }

    private static boolean hasDomainURLs(ParsingPackage pkg) {
        List<ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            ParsedActivity activity = activities.get(index);
            List<ParsedIntentInfo> filters = activity.getIntents();
            int filtersSize = filters.size();
            for (int filtersIndex = 0; filtersIndex < filtersSize; filtersIndex++) {
                ParsedIntentInfo aii = filters.get(filtersIndex);
                if (aii.hasAction("android.intent.action.VIEW") && aii.hasAction("android.intent.action.VIEW") && (aii.hasDataScheme(IntentFilter.SCHEME_HTTP) || aii.hasDataScheme(IntentFilter.SCHEME_HTTPS))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void setMaxAspectRatio(ParsingPackage pkg) {
        float activityAspectRatio;
        float maxAspectRatio = pkg.getTargetSdkVersion() < 26 ? 1.86f : 0.0f;
        float packageMaxAspectRatio = pkg.getMaxAspectRatio();
        if (packageMaxAspectRatio != 0.0f) {
            maxAspectRatio = packageMaxAspectRatio;
        } else {
            Bundle appMetaData = pkg.getMetaData();
            if (appMetaData != null && appMetaData.containsKey("android.max_aspect")) {
                maxAspectRatio = appMetaData.getFloat("android.max_aspect", maxAspectRatio);
            }
        }
        List<ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            ParsedActivity activity = activities.get(index);
            if (activity.getMaxAspectRatio() == null) {
                if (activity.getMetaData() != null) {
                    activityAspectRatio = activity.getMetaData().getFloat("android.max_aspect", maxAspectRatio);
                } else {
                    activityAspectRatio = maxAspectRatio;
                }
                activity.setMaxAspectRatio(activity.getResizeMode(), activityAspectRatio);
            }
        }
    }

    public static void setMaxAspectRatio(ParsingPackageRead pkg, boolean forceFull) {
        if (DeviceStateManager.isCtsRunningStatic()) {
            return;
        }
        float defaultAspectRatio = forceFull ? 3.0f : 1.86f;
        float maxAspectRatio = pkg.getTargetSdkVersion() < 26 ? defaultAspectRatio : 0.0f;
        float packageMaxAspectRatio = pkg.getMaxAspectRatio();
        if (packageMaxAspectRatio != 0.0f) {
            maxAspectRatio = packageMaxAspectRatio;
        } else {
            Bundle appMetaData = pkg.getMetaData();
            if (appMetaData != null && appMetaData.containsKey("android.max_aspect")) {
                maxAspectRatio = appMetaData.getFloat("android.max_aspect", maxAspectRatio);
            }
        }
        List<ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            ParsedActivity activity = activities.get(index);
            float activityAspectRatio = activity.getMetaData() != null ? activity.getMetaData().getFloat("android.max_aspect", maxAspectRatio) : maxAspectRatio;
            activity.setMaxAspectRatio(activity.getResizeMode(), activityAspectRatio);
        }
    }

    private void setMinAspectRatio(ParsingPackage pkg) {
        float minAspectRatio = pkg.getMinAspectRatio();
        List<ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            ParsedActivity activity = activities.get(index);
            if (activity.getMinAspectRatio() == null) {
                activity.setMinAspectRatio(activity.getResizeMode(), minAspectRatio);
            }
        }
    }

    private void setSupportsSizeChanges(ParsingPackage pkg) {
        Bundle appMetaData = pkg.getMetaData();
        boolean supportsSizeChanges = appMetaData != null && appMetaData.getBoolean("android.supports_size_changes", false);
        List<ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            ParsedActivity activity = activities.get(index);
            if (supportsSizeChanges || (activity.getMetaData() != null && activity.getMetaData().getBoolean("android.supports_size_changes", false))) {
                activity.setSupportsSizeChanges(true);
            }
        }
    }

    private static ParseResult<ParsingPackage> parseOverlay(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestResourceOverlay);
        try {
            String target = sa.getString(1);
            int priority = anInt(0, 0, sa);
            if (target == null) {
                return input.error("<overlay> does not specify a target package");
            }
            if (priority < 0 || priority > 9999) {
                return input.error("<overlay> priority must be between 0 and 9999");
            }
            String propName = sa.getString(5);
            String propValue = sa.getString(6);
            if (PackageParser.checkRequiredSystemProperties(propName, propValue)) {
                return input.success(pkg.mo954setOverlay(true).mo958setOverlayTarget(target).mo957setOverlayPriority(priority).mo959setOverlayTargetName(sa.getString(3)).mo955setOverlayCategory(sa.getString(2)).mo956setOverlayIsStatic(bool(false, 4, sa)));
            }
            String message = "Skipping target and overlay pair " + target + " and " + pkg.getBaseApkPath() + ": overlay ignored due to required system property: " + propName + " with value: " + propValue;
            Slog.i("PackageParsing", message);
            return input.skip(message);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseProtectedBroadcast(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestProtectedBroadcast);
        try {
            String name = nonResString(0, sa);
            if (name != null) {
                pkg.mo879addProtectedBroadcast(name);
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseSupportScreens(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestSupportsScreens);
        try {
            int requiresSmallestWidthDp = anInt(0, 6, sa);
            int compatibleWidthLimitDp = anInt(0, 7, sa);
            int largestWidthLimitDp = anInt(0, 8, sa);
            return input.success(pkg.mo994setSupportsSmallScreens(anInt(1, 1, sa)).mo992setSupportsNormalScreens(anInt(1, 2, sa)).mo991setSupportsLargeScreens(anInt(1, 3, sa)).mo990setSupportsExtraLargeScreens(anInt(1, 5, sa)).mo975setResizeable(anInt(1, 4, sa)).mo904setAnyDensity(anInt(1, 0, sa)).mo974setRequiresSmallestWidthDp(requiresSmallestWidthDp).mo916setCompatibleWidthLimitDp(compatibleWidthLimitDp).mo941setLargestWidthLimitDp(largestWidthLimitDp));
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseInstrumentation(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) throws XmlPullParserException, IOException {
        ParseResult<ParsedInstrumentation> result = ParsedInstrumentationUtils.parseInstrumentation(pkg, res, parser, sUseRoundIcon, input);
        if (result.isError()) {
            return input.error(result);
        }
        return input.success(pkg.mo871addInstrumentation(result.getResult()));
    }

    private static ParseResult<ParsingPackage> parseOriginalPackage(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestOriginalPackage);
        try {
            String orig = sa.getNonConfigurationString(0, 0);
            if (!pkg.getPackageName().equals(orig)) {
                if (pkg.getOriginalPackages().isEmpty()) {
                    pkg.mo968setRealPackage(pkg.getPackageName());
                }
                pkg.mo874addOriginalPackage(orig);
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsingPackage> parseAdoptPermissions(ParseInput input, ParsingPackage pkg, Resources res, XmlResourceParser parser) {
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestOriginalPackage);
        try {
            String name = nonConfigString(0, 0, sa);
            if (name != null) {
                pkg.mo866addAdoptPermission(name);
            }
            return input.success(pkg);
        } finally {
            sa.recycle();
        }
    }

    private static void convertNewPermissions(ParsingPackage pkg) {
        int NP = PackageParser.NEW_PERMISSIONS.length;
        StringBuilder newPermsMsg = null;
        for (int ip = 0; ip < NP; ip++) {
            PackageParser.NewPermissionInfo npi = PackageParser.NEW_PERMISSIONS[ip];
            if (pkg.getTargetSdkVersion() >= npi.sdkVersion) {
                break;
            }
            if (!pkg.getRequestedPermissions().contains(npi.name)) {
                if (newPermsMsg == null) {
                    newPermsMsg = new StringBuilder(128);
                    newPermsMsg.append(pkg.getPackageName());
                    newPermsMsg.append(": compat added ");
                } else {
                    newPermsMsg.append(' ');
                }
                newPermsMsg.append(npi.name);
                pkg.mo891addUsesPermission(new ParsedUsesPermission(npi.name, 0)).mo870addImplicitPermission(npi.name);
            }
        }
        if (newPermsMsg != null) {
            Slog.i("PackageParsing", newPermsMsg.toString());
        }
    }

    private void convertSplitPermissions(ParsingPackage pkg) {
        int listSize = this.mSplitPermissionInfos.size();
        for (int is = 0; is < listSize; is++) {
            PermissionManager.SplitPermissionInfo spi = this.mSplitPermissionInfos.get(is);
            List<String> requestedPermissions = pkg.getRequestedPermissions();
            if (pkg.getTargetSdkVersion() < spi.getTargetSdk() && requestedPermissions.contains(spi.getSplitPermission())) {
                List<String> newPerms = spi.getNewPermissions();
                for (int in = 0; in < newPerms.size(); in++) {
                    String perm = newPerms.get(in);
                    if (!requestedPermissions.contains(perm)) {
                        pkg.mo891addUsesPermission(new ParsedUsesPermission(perm, 0)).mo870addImplicitPermission(perm);
                    }
                }
            }
        }
    }

    private static void adjustPackageToBeUnresizeableAndUnpipable(ParsingPackage pkg) {
        List<ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int index = 0; index < activitiesSize; index++) {
            ParsedActivity activity = activities.get(index);
            activity.setResizeMode(0).setFlags(activity.getFlags() & (-4194305));
        }
    }

    public static String validateName(String name, boolean requireSeparator, boolean requireFilename) {
        int N = name.length();
        boolean hasSep = false;
        boolean front = true;
        for (int i = 0; i < N; i++) {
            char c = name.charAt(i);
            if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                front = false;
            } else if (front || ((c < '0' || c > '9') && c != '_')) {
                if (c != '.') {
                    return "bad character '" + c + "'";
                }
                hasSep = true;
                front = true;
            }
        }
        if (requireFilename) {
            if (!FileUtils.isValidExtFilename(name)) {
                return "Invalid filename";
            }
            if (N > 223) {
                return "the length of the name is greater than 223";
            }
        }
        if (hasSep || !requireSeparator) {
            return null;
        }
        return "must have at least one '.' separator";
    }

    public static ParseResult validateName(ParseInput input, String name, boolean requireSeparator, boolean requireFilename) {
        String errorMessage = validateName(name, requireSeparator, requireFilename);
        if (errorMessage != null) {
            return input.error(errorMessage);
        }
        return input.success(null);
    }

    public static ParseResult<PackageManager.Property> parseMetaData(ParsingPackage pkg, ParsedComponent component, Resources res, XmlResourceParser parser, String tagName, ParseInput input) {
        PackageManager.Property property;
        TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestMetaData);
        boolean z = false;
        try {
            String name = TextUtils.safeIntern(nonConfigString(0, 0, sa));
            if (name == null) {
                return input.error(tagName + " requires an android:name attribute");
            }
            String packageName = pkg.getPackageName();
            String stringValue = null;
            String className = component != null ? component.getName() : null;
            TypedValue v = sa.peekValue(2);
            if (v == null || v.resourceId == 0) {
                TypedValue v2 = sa.peekValue(1);
                if (v2 == null) {
                    return input.error(tagName + " requires an android:value or android:resource attribute");
                } else if (v2.type == 3) {
                    CharSequence cs = v2.coerceToString();
                    if (cs != null) {
                        stringValue = cs.toString();
                    }
                    property = new PackageManager.Property(name, stringValue, packageName, className);
                } else if (v2.type == 18) {
                    if (v2.data != 0) {
                        z = true;
                    }
                    property = new PackageManager.Property(name, z, packageName, className);
                } else if (v2.type >= 16 && v2.type <= 31) {
                    property = new PackageManager.Property(name, v2.data, false, packageName, className);
                } else if (v2.type == 4) {
                    property = new PackageManager.Property(name, v2.getFloat(), packageName, className);
                } else {
                    Slog.w("PackageParsing", tagName + " only supports string, integer, float, color, boolean, and resource reference types: " + parser.getName() + " at " + pkg.getBaseApkPath() + " " + parser.getPositionDescription());
                    property = null;
                }
            } else {
                property = new PackageManager.Property(name, v.resourceId, true, packageName, className);
            }
            return input.success(property);
        } finally {
            sa.recycle();
        }
    }

    public static PackageParser.SigningDetails getSigningDetails(ParsingPackageRead pkg, boolean skipVerify) throws PackageParser.PackageParserException {
        PackageParser.SigningDetails signingDetails = PackageParser.SigningDetails.UNKNOWN;
        ParseInput input = ParseTypeImpl.forDefaultParsing().reset();
        Trace.traceBegin(262144L, "collectCertificates");
        try {
            ParseResult<PackageParser.SigningDetails> result = getSigningDetails(input, pkg.getBaseApkPath(), skipVerify, pkg.isStaticSharedLibrary(), signingDetails, pkg.getTargetSdkVersion());
            if (result.isError()) {
                throw new PackageParser.PackageParserException(result.getErrorCode(), result.getErrorMessage(), result.getException());
            }
            PackageParser.SigningDetails signingDetails2 = result.getResult();
            String[] splitCodePaths = pkg.getSplitCodePaths();
            if (!ArrayUtils.isEmpty(splitCodePaths)) {
                PackageParser.SigningDetails signingDetails3 = signingDetails2;
                for (String str : splitCodePaths) {
                    try {
                        ParseResult<PackageParser.SigningDetails> result2 = getSigningDetails(input, str, skipVerify, pkg.isStaticSharedLibrary(), signingDetails3, pkg.getTargetSdkVersion());
                        if (result2.isError()) {
                            throw new PackageParser.PackageParserException(result2.getErrorCode(), result2.getErrorMessage(), result2.getException());
                        }
                        signingDetails3 = result2.getResult();
                    } catch (Throwable th) {
                        th = th;
                        Trace.traceEnd(262144L);
                        throw th;
                    }
                }
                signingDetails2 = signingDetails3;
            }
            Trace.traceEnd(262144L);
            return signingDetails2;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static ParseResult<PackageParser.SigningDetails> getSigningDetails(ParseInput input, String baseCodePath, boolean skipVerify, boolean isStaticSharedLibrary, PackageParser.SigningDetails existingSigningDetails, int targetSdk) {
        PackageParser.SigningDetails verified;
        int minSignatureScheme = ApkSignatureVerifier.getMinimumSignatureSchemeVersionForTargetSdk(targetSdk);
        if (isStaticSharedLibrary) {
            minSignatureScheme = 2;
        }
        try {
            if (skipVerify) {
                verified = ApkSignatureVerifier.unsafeGetCertsWithoutVerification(baseCodePath, 1);
            } else {
                verified = ApkSignatureVerifier.verify(baseCodePath, minSignatureScheme);
            }
            if (existingSigningDetails == PackageParser.SigningDetails.UNKNOWN) {
                return input.success(verified);
            }
            if (!Signature.areExactMatch(existingSigningDetails.signatures, verified.signatures)) {
                return input.error(PackageManager.INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES, baseCodePath + " has mismatched certificates");
            }
            return input.success(existingSigningDetails);
        } catch (PackageParser.PackageParserException e) {
            return input.error(-103, "Failed collecting certificates for " + baseCodePath, e);
        }
    }

    public static void readConfigUseRoundIcon(Resources r) {
        if (r != null) {
            sUseRoundIcon = r.getBoolean(R.bool.config_useRoundIcon);
            return;
        }
        try {
            ApplicationInfo androidAppInfo = ActivityThread.getPackageManager().getApplicationInfo("android", 0, UserHandle.myUserId());
            Resources systemResources = Resources.getSystem();
            Resources overlayableRes = ResourcesManager.getInstance().getResources(null, null, null, androidAppInfo.resourceDirs, androidAppInfo.overlayPaths, androidAppInfo.sharedLibraryFiles, null, null, systemResources.getCompatibilityInfo(), systemResources.getClassLoader(), null);
            sUseRoundIcon = overlayableRes.getBoolean(R.bool.config_useRoundIcon);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static boolean bool(boolean defaultValue, int attribute, TypedArray sa) {
        return sa.getBoolean(attribute, defaultValue);
    }

    private static float aFloat(float defaultValue, int attribute, TypedArray sa) {
        return sa.getFloat(attribute, defaultValue);
    }

    private static float aFloat(int attribute, TypedArray sa) {
        return sa.getFloat(attribute, 0.0f);
    }

    private static int anInt(int defaultValue, int attribute, TypedArray sa) {
        return sa.getInt(attribute, defaultValue);
    }

    private static int anInteger(int defaultValue, int attribute, TypedArray sa) {
        return sa.getInteger(attribute, defaultValue);
    }

    private static int anInt(int attribute, TypedArray sa) {
        return sa.getInt(attribute, 0);
    }

    private static int resId(int attribute, TypedArray sa) {
        return sa.getResourceId(attribute, 0);
    }

    private static String string(int attribute, TypedArray sa) {
        return sa.getString(attribute);
    }

    private static String nonConfigString(int allowedChangingConfigs, int attribute, TypedArray sa) {
        return sa.getNonConfigurationString(attribute, allowedChangingConfigs);
    }

    private static String nonResString(int index, TypedArray sa) {
        return sa.getNonResourceString(index);
    }

    public static void writeKeySetMapping(Parcel dest, Map<String, ArraySet<PublicKey>> keySetMapping) {
        if (keySetMapping == null) {
            dest.writeInt(-1);
            return;
        }
        int N = keySetMapping.size();
        dest.writeInt(N);
        for (String key : keySetMapping.keySet()) {
            dest.writeString(key);
            ArraySet<PublicKey> keys = keySetMapping.get(key);
            if (keys == null) {
                dest.writeInt(-1);
            } else {
                int M = keys.size();
                dest.writeInt(M);
                for (int j = 0; j < M; j++) {
                    dest.writeSerializable(keys.valueAt(j));
                }
            }
        }
    }

    public static ArrayMap<String, ArraySet<PublicKey>> readKeySetMapping(Parcel in) {
        int N = in.readInt();
        if (N == -1) {
            return null;
        }
        ArrayMap<String, ArraySet<PublicKey>> keySetMapping = new ArrayMap<>();
        for (int i = 0; i < N; i++) {
            String key = in.readString();
            int M = in.readInt();
            if (M == -1) {
                keySetMapping.put(key, null);
            } else {
                ArraySet<PublicKey> keys = new ArraySet<>(M);
                for (int j = 0; j < M; j++) {
                    PublicKey pk = (PublicKey) in.readSerializable();
                    keys.add(pk);
                }
                keySetMapping.put(key, keys);
            }
        }
        return keySetMapping;
    }
}
