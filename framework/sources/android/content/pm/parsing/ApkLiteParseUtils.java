package android.content.pm.parsing;

import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.VerifierInfo;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.res.ApkAssets;
import android.content.res.XmlResourceParser;
import android.os.Trace;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.Slog;
import com.android.internal.util.ArrayUtils;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import libcore.io.IoUtils;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ApkLiteParseUtils {
    public static final String APK_FILE_EXTENSION = ".apk";
    private static final int PARSE_DEFAULT_INSTALL_LOCATION = -1;
    private static final String TAG = "PackageParsing";
    private static final Comparator<String> sSplitNameComparator = new SplitNameComparator();

    public static ParseResult<PackageLite> parsePackageLite(ParseInput input, File packageFile, int flags) {
        if (packageFile.isDirectory()) {
            return parseClusterPackageLite(input, packageFile, flags);
        }
        return parseMonolithicPackageLite(input, packageFile, flags);
    }

    public static ParseResult<PackageLite> parseMonolithicPackageLite(ParseInput input, File packageFile, int flags) {
        Trace.traceBegin(262144L, "parseApkLite");
        try {
            ParseResult<ApkLite> result = parseApkLite(input, packageFile, flags);
            if (result.isError()) {
                return input.error(result);
            }
            ApkLite baseApk = result.getResult();
            String packagePath = packageFile.getAbsolutePath();
            return input.success(new PackageLite(packagePath, baseApk.getPath(), baseApk, null, null, null, null, null, null, baseApk.getTargetSdkVersion()));
        } finally {
            Trace.traceEnd(262144L);
        }
    }

    public static ParseResult<PackageLite> parseClusterPackageLite(ParseInput input, File packageDir, int flags) {
        File[] files = packageDir.listFiles();
        if (ArrayUtils.isEmpty(files)) {
            return input.error(-100, "No packages found in split");
        }
        int i = 0;
        if (files.length == 1 && files[0].isDirectory()) {
            return parseClusterPackageLite(input, files[0], flags);
        }
        String packageName = null;
        int versionCode = 0;
        ArrayMap<String, ApkLite> apks = new ArrayMap<>();
        long j = 262144;
        Trace.traceBegin(262144L, "parseApkLite");
        try {
            int length = files.length;
            while (i < length) {
                File file = files[i];
                if (isApkFile(file)) {
                    ParseResult<ApkLite> result = parseApkLite(input, file, flags);
                    if (result.isError()) {
                        ParseResult<PackageLite> error = input.error(result);
                        Trace.traceEnd(j);
                        return error;
                    }
                    ApkLite lite = result.getResult();
                    if (packageName == null) {
                        packageName = lite.getPackageName();
                        versionCode = lite.getVersionCode();
                    } else if (!packageName.equals(lite.getPackageName())) {
                        return input.error(-101, "Inconsistent package " + lite.getPackageName() + " in " + file + "; expected " + packageName);
                    } else if (versionCode != lite.getVersionCode()) {
                        return input.error(-101, "Inconsistent version " + lite.getVersionCode() + " in " + file + "; expected " + versionCode);
                    }
                    if (apks.put(lite.getSplitName(), lite) != null) {
                        return input.error(-101, "Split name " + lite.getSplitName() + " defined more than once; most recent was " + file);
                    }
                }
                i++;
                j = 262144;
            }
            Trace.traceEnd(262144L);
            ApkLite baseApk = apks.remove(null);
            return composePackageLiteFromApks(input, packageDir, baseApk, apks);
        } finally {
            Trace.traceEnd(262144L);
        }
    }

    public static ParseResult<PackageLite> composePackageLiteFromApks(ParseInput input, File packageDir, ApkLite baseApk, ArrayMap<String, ApkLite> splitApks) {
        return composePackageLiteFromApks(input, packageDir, baseApk, splitApks, false);
    }

    public static ParseResult<PackageLite> composePackageLiteFromApks(ParseInput input, File packageDir, ApkLite baseApk, ArrayMap<String, ApkLite> splitApks, boolean apkRenamed) {
        String[] splitNames;
        boolean[] isFeatureSplits;
        String[] usesSplitNames;
        String[] configForSplits;
        String[] splitCodePaths;
        int[] splitRevisionCodes;
        if (baseApk == null) {
            return input.error(-101, "Missing base APK in " + packageDir);
        }
        int size = ArrayUtils.size(splitApks);
        if (size <= 0) {
            splitNames = null;
            isFeatureSplits = null;
            usesSplitNames = null;
            configForSplits = null;
            splitCodePaths = null;
            splitRevisionCodes = null;
        } else {
            String[] splitNames2 = new String[size];
            boolean[] isFeatureSplits2 = new boolean[size];
            String[] usesSplitNames2 = new String[size];
            String[] configForSplits2 = new String[size];
            String[] splitCodePaths2 = new String[size];
            int[] splitRevisionCodes2 = new int[size];
            String[] splitNames3 = (String[]) splitApks.keySet().toArray(splitNames2);
            Arrays.sort(splitNames3, sSplitNameComparator);
            for (int i = 0; i < size; i++) {
                ApkLite apk = splitApks.get(splitNames3[i]);
                usesSplitNames2[i] = apk.getUsesSplitName();
                isFeatureSplits2[i] = apk.isFeatureSplit();
                configForSplits2[i] = apk.getConfigForSplit();
                splitCodePaths2[i] = apkRenamed ? new File(packageDir, splitNameToFileName(apk)).getAbsolutePath() : apk.getPath();
                splitRevisionCodes2[i] = apk.getRevisionCode();
            }
            splitNames = splitNames3;
            isFeatureSplits = isFeatureSplits2;
            usesSplitNames = usesSplitNames2;
            configForSplits = configForSplits2;
            splitCodePaths = splitCodePaths2;
            splitRevisionCodes = splitRevisionCodes2;
        }
        String codePath = packageDir.getAbsolutePath();
        String baseCodePath = apkRenamed ? new File(packageDir, splitNameToFileName(baseApk)).getAbsolutePath() : baseApk.getPath();
        return input.success(new PackageLite(codePath, baseCodePath, baseApk, splitNames, isFeatureSplits, usesSplitNames, configForSplits, splitCodePaths, splitRevisionCodes, baseApk.getTargetSdkVersion()));
    }

    public static String splitNameToFileName(ApkLite apk) {
        String fileName;
        Objects.requireNonNull(apk);
        if (apk.getSplitName() == null) {
            fileName = "base";
        } else {
            fileName = "split_" + apk.getSplitName();
        }
        return fileName + ".apk";
    }

    public static ParseResult<ApkLite> parseApkLite(ParseInput input, File apkFile, int flags) {
        return parseApkLiteInner(input, apkFile, null, null, flags);
    }

    public static ParseResult<ApkLite> parseApkLite(ParseInput input, FileDescriptor fd, String debugPathName, int flags) {
        return parseApkLiteInner(input, null, fd, debugPathName, flags);
    }

    private static ParseResult<ApkLite> parseApkLiteInner(ParseInput input, File apkFile, FileDescriptor fd, String debugPathName, int flags) {
        Exception e;
        PackageParser.SigningDetails signingDetails;
        String apkPath = fd != null ? debugPathName : apkFile.getAbsolutePath();
        XmlResourceParser parser = null;
        ApkAssets apkAssets = null;
        boolean z = false;
        try {
            try {
                try {
                    ApkAssets apkAssets2 = fd != null ? ApkAssets.loadFromFd(fd, debugPathName, 0, null) : ApkAssets.loadFromPath(apkPath);
                    try {
                        XmlResourceParser parser2 = apkAssets2.openXml("AndroidManifest.xml");
                        try {
                            if ((flags & 32) != 0) {
                                if ((flags & 16) != 0) {
                                    z = true;
                                }
                                boolean skipVerify = z;
                                Trace.traceBegin(262144L, "collectCertificates");
                                try {
                                    ParseResult<PackageParser.SigningDetails> result = ParsingPackageUtils.getSigningDetails(input, apkFile.getAbsolutePath(), skipVerify, false, PackageParser.SigningDetails.UNKNOWN, 0);
                                    if (result.isError()) {
                                        ParseResult<ApkLite> error = input.error(result);
                                        IoUtils.closeQuietly(parser2);
                                        if (apkAssets2 != null) {
                                            try {
                                                apkAssets2.close();
                                            } catch (Throwable th) {
                                            }
                                        }
                                        return error;
                                    }
                                    PackageParser.SigningDetails signingDetails2 = result.getResult();
                                    Trace.traceEnd(262144L);
                                    signingDetails = signingDetails2;
                                } finally {
                                    Trace.traceEnd(262144L);
                                }
                            } else {
                                signingDetails = PackageParser.SigningDetails.UNKNOWN;
                            }
                            ParseResult<ApkLite> parseApkLite = parseApkLite(input, apkPath, parser2, signingDetails);
                            IoUtils.closeQuietly(parser2);
                            if (apkAssets2 != null) {
                                try {
                                    apkAssets2.close();
                                } catch (Throwable th2) {
                                }
                            }
                            return parseApkLite;
                        } catch (IOException | RuntimeException | XmlPullParserException e2) {
                            e = e2;
                            apkAssets = apkAssets2;
                            parser = parser2;
                            Exception e3 = e;
                            Slog.w("PackageParsing", "Failed to parse " + apkPath, e3);
                            ParseResult<ApkLite> error2 = input.error(-102, "Failed to parse " + apkPath, e3);
                            IoUtils.closeQuietly(parser);
                            if (apkAssets != null) {
                                try {
                                    apkAssets.close();
                                } catch (Throwable th3) {
                                }
                            }
                            return error2;
                        } catch (Throwable th4) {
                            e = th4;
                            apkAssets = apkAssets2;
                            parser = parser2;
                            IoUtils.closeQuietly(parser);
                            if (apkAssets != null) {
                                try {
                                    apkAssets.close();
                                } catch (Throwable th5) {
                                }
                            }
                            throw e;
                        }
                    } catch (IOException | RuntimeException | XmlPullParserException e4) {
                        e = e4;
                        apkAssets = apkAssets2;
                    } catch (Throwable th6) {
                        e = th6;
                        apkAssets = apkAssets2;
                    }
                } catch (IOException | RuntimeException | XmlPullParserException e5) {
                    e = e5;
                }
            } catch (IOException e6) {
                ParseResult<ApkLite> error3 = input.error(-100, "Failed to parse " + apkPath, e6);
                IoUtils.closeQuietly((AutoCloseable) null);
                if (0 != 0) {
                    try {
                        apkAssets.close();
                    } catch (Throwable th7) {
                    }
                }
                return error3;
            }
        } catch (Throwable th8) {
            e = th8;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0289, code lost:
        if (android.content.pm.PackageParser.checkRequiredSystemProperties(r15, r14) != false) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x028b, code lost:
        android.util.Slog.i("PackageParsing", "Skipping target and overlay pair " + r8 + " and " + r60 + ": overlay ignored due to required system property: " + r15 + " with value: " + r14);
        r0 = null;
        r3 = false;
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x031a, code lost:
        return r59.success(new android.content.pm.parsing.ApkLite(r60, r4.first, r4.second, r40, r42, r53, r41, r35, r36, r37, r6, r12, r62, r38, r47, r48, r49, r50, r52, r51, r39, r0, r3, r5, r46, r45, r54));
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x02c4, code lost:
        r0 = r8;
        r3 = r17;
        r5 = r18;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ParseResult<ApkLite> parseApkLite(ParseInput input, String codePath, XmlResourceParser parser, PackageParser.SigningDetails signingDetails) throws IOException, XmlPullParserException {
        List<VerifierInfo> verifiers;
        List<VerifierInfo> verifiers2;
        String minCode;
        int targetVer;
        String targetCode;
        XmlResourceParser xmlResourceParser = parser;
        ParseResult<Pair<String, String>> result = parsePackageSplitNames(input, xmlResourceParser);
        if (result.isError()) {
            return input.error(result);
        }
        Pair<String, String> packageSplit = result.getResult();
        int installLocation = xmlResourceParser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "installLocation", -1);
        int versionCode = xmlResourceParser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "versionCode", 0);
        int versionCodeMajor = xmlResourceParser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "versionCodeMajor", 0);
        int revisionCode = xmlResourceParser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "revisionCode", 0);
        boolean coreApp = xmlResourceParser.getAttributeBooleanValue(null, "coreApp", false);
        boolean isolatedSplits = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "isolatedSplits", false);
        boolean isFeatureSplit = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "isFeatureSplit", false);
        boolean isSplitRequired = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "isSplitRequired", false);
        String configForSplit = xmlResourceParser.getAttributeValue(null, "configForSplit");
        boolean overlayIsStatic = false;
        int overlayPriority = 0;
        boolean z = true;
        int searchDepth = parser.getDepth() + 1;
        List<VerifierInfo> verifiers3 = new ArrayList<>();
        int targetSdkVersion = 0;
        int minSdkVersion = 1;
        boolean debuggable = false;
        boolean profilableByShell = false;
        boolean multiArch = false;
        boolean use32bitAbi = false;
        boolean extractNativeLibs = true;
        boolean useEmbeddedDex = false;
        String usesSplitName = null;
        String targetPackage = null;
        int rollbackDataPolicy = 0;
        String requiredSystemPropertyName = null;
        String requiredSystemPropertyValue = null;
        while (true) {
            int type = parser.next();
            if (type == z) {
                verifiers = verifiers3;
                break;
            }
            int i = 3;
            if (type == 3 && parser.getDepth() < searchDepth) {
                verifiers = verifiers3;
                break;
            }
            if (type == 3) {
                verifiers2 = verifiers3;
            } else if (type == 4) {
                verifiers2 = verifiers3;
            } else if (parser.getDepth() != searchDepth) {
                verifiers2 = verifiers3;
            } else if ("package-verifier".equals(parser.getName())) {
                VerifierInfo verifier = parseVerifier(parser);
                if (verifier == null) {
                    verifiers2 = verifiers3;
                } else {
                    verifiers2 = verifiers3;
                    verifiers2.add(verifier);
                }
            } else {
                verifiers2 = verifiers3;
                if ("application".equals(parser.getName())) {
                    debuggable = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "debuggable", false);
                    multiArch = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "multiArch", false);
                    use32bitAbi = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "use32bitAbi", false);
                    extractNativeLibs = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "extractNativeLibs", z);
                    useEmbeddedDex = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "useEmbeddedDex", false);
                    rollbackDataPolicy = xmlResourceParser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "rollbackDataPolicy", 0);
                    int innerDepth = parser.getDepth();
                    boolean profilableByShell2 = profilableByShell;
                    while (true) {
                        int innerType = parser.next();
                        if (innerType == z || (innerType == i && parser.getDepth() <= innerDepth)) {
                            break;
                        } else if (innerType == i || innerType == 4 || parser.getDepth() != innerDepth + 1 || !"profileable".equals(parser.getName())) {
                            i = 3;
                            z = true;
                        } else {
                            profilableByShell2 = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "shell", profilableByShell2);
                            i = 3;
                            z = true;
                        }
                    }
                    profilableByShell = profilableByShell2;
                    verifiers3 = verifiers2;
                    z = true;
                } else if ("overlay".equals(parser.getName())) {
                    requiredSystemPropertyName = xmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "requiredSystemPropertyName");
                    requiredSystemPropertyValue = xmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "requiredSystemPropertyValue");
                    targetPackage = xmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "targetPackage");
                    overlayIsStatic = xmlResourceParser.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "isStatic", false);
                    overlayPriority = xmlResourceParser.getAttributeIntValue("http://schemas.android.com/apk/res/android", "priority", 0);
                    verifiers3 = verifiers2;
                    z = true;
                } else if ("uses-split".equals(parser.getName())) {
                    if (usesSplitName != null) {
                        Slog.w("PackageParsing", "Only one <uses-split> permitted. Ignoring others.");
                    } else {
                        usesSplitName = xmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
                        if (usesSplitName == null) {
                            return input.error(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "<uses-split> tag requires 'android:name' attribute");
                        }
                        verifiers3 = verifiers2;
                        z = true;
                    }
                } else if ("uses-sdk".equals(parser.getName())) {
                    String minSdkVersionString = xmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "minSdkVersion");
                    String targetSdkVersionString = xmlResourceParser.getAttributeValue("http://schemas.android.com/apk/res/android", "targetSdkVersion");
                    int minVer = 1;
                    String minCode2 = null;
                    int targetVer2 = 0;
                    String targetCode2 = null;
                    if (!TextUtils.isEmpty(minSdkVersionString)) {
                        try {
                            minVer = Integer.parseInt(minSdkVersionString);
                        } catch (NumberFormatException e) {
                            minCode2 = minSdkVersionString;
                        }
                    }
                    if (!TextUtils.isEmpty(targetSdkVersionString)) {
                        try {
                            targetVer2 = Integer.parseInt(targetSdkVersionString);
                        } catch (NumberFormatException e2) {
                            targetCode2 = targetSdkVersionString;
                            if (minCode2 == null) {
                                minCode2 = targetCode2;
                            }
                        }
                        minCode = minCode2;
                        targetVer = targetVer2;
                        targetCode = targetCode2;
                    } else {
                        String targetCode3 = minCode2;
                        minCode = minCode2;
                        targetVer = minVer;
                        targetCode = targetCode3;
                    }
                    int searchDepth2 = searchDepth;
                    ParseResult<Integer> targetResult = ParsingPackageUtils.computeTargetSdkVersion(targetVer, targetCode, ParsingPackageUtils.SDK_CODENAMES, input);
                    if (targetResult.isError()) {
                        return input.error(targetResult);
                    }
                    targetSdkVersion = targetResult.getResult().intValue();
                    ParseResult<Integer> minResult = ParsingPackageUtils.computeMinSdkVersion(minVer, minCode, ParsingPackageUtils.SDK_VERSION, ParsingPackageUtils.SDK_CODENAMES, input);
                    if (minResult.isError()) {
                        return input.error(minResult);
                    }
                    minSdkVersion = minResult.getResult().intValue();
                    xmlResourceParser = parser;
                    searchDepth = searchDepth2;
                    z = true;
                    verifiers3 = verifiers2;
                }
            }
            xmlResourceParser = parser;
            searchDepth = searchDepth;
            z = true;
            verifiers3 = verifiers2;
        }
    }

    public static ParseResult<Pair<String, String>> parsePackageSplitNames(ParseInput input, XmlResourceParser parser) throws IOException, XmlPullParserException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            return input.error(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No start tag found");
        }
        if (!parser.getName().equals("manifest")) {
            return input.error(PackageManager.INSTALL_PARSE_FAILED_MANIFEST_MALFORMED, "No <manifest> tag");
        }
        String packageName = parser.getAttributeValue(null, "package");
        if (!"android".equals(packageName)) {
            ParseResult<?> nameResult = ParsingPackageUtils.validateName(input, packageName, true, true);
            if (nameResult.isError()) {
                return input.error(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest package: " + nameResult.getErrorMessage());
            }
        }
        String splitName = parser.getAttributeValue(null, "split");
        if (splitName != null) {
            if (splitName.length() == 0) {
                splitName = null;
            } else {
                ParseResult<?> nameResult2 = ParsingPackageUtils.validateName(input, splitName, false, false);
                if (nameResult2.isError()) {
                    return input.error(PackageManager.INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME, "Invalid manifest split: " + nameResult2.getErrorMessage());
                }
            }
        }
        return input.success(Pair.create(packageName.intern(), splitName != null ? splitName.intern() : splitName));
    }

    public static VerifierInfo parseVerifier(AttributeSet attrs) {
        String packageName = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
        String encodedPublicKey = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "publicKey");
        if (packageName == null || packageName.length() == 0) {
            Slog.i("PackageParsing", "verifier package name was null; skipping");
            return null;
        }
        PublicKey publicKey = PackageParser.parsePublicKey(encodedPublicKey);
        if (publicKey == null) {
            Slog.i("PackageParsing", "Unable to parse verifier public key for " + packageName);
            return null;
        }
        return new VerifierInfo(packageName, publicKey);
    }

    /* loaded from: classes.dex */
    private static class SplitNameComparator implements Comparator<String> {
        private SplitNameComparator() {
        }

        @Override // java.util.Comparator
        public int compare(String lhs, String rhs) {
            if (lhs == null) {
                return -1;
            }
            if (rhs == null) {
                return 1;
            }
            return lhs.compareTo(rhs);
        }
    }

    public static boolean isApkFile(File file) {
        return isApkPath(file.getName());
    }

    public static boolean isApkPath(String path) {
        return path.endsWith(".apk");
    }
}
