package android.content.pm.dex;

import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.parsing.ApkLiteParseUtils;
import android.content.pm.parsing.PackageLite;
import android.os.SystemProperties;
import android.util.ArrayMap;
import android.util.JsonReader;
import android.util.Log;
import android.util.jar.StrictJarFile;
import com.android.internal.security.VerityUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
/* loaded from: classes.dex */
public class DexMetadataHelper {
    private static final String DEX_METADATA_FILE_EXTENSION = ".dm";
    private static final String PROPERTY_DM_FSVERITY_REQUIRED = "pm.dexopt.dm.require_fsverity";
    private static final String PROPERTY_DM_JSON_MANIFEST_REQUIRED = "pm.dexopt.dm.require_manifest";
    public static final String TAG = "DexMetadataHelper";
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);

    private DexMetadataHelper() {
    }

    public static boolean isDexMetadataFile(File file) {
        return isDexMetadataPath(file.getName());
    }

    private static boolean isDexMetadataPath(String path) {
        return path.endsWith(".dm");
    }

    public static boolean isFsVerityRequired() {
        return VerityUtils.isFsVeritySupported() && SystemProperties.getBoolean(PROPERTY_DM_FSVERITY_REQUIRED, false);
    }

    public static long getPackageDexMetadataSize(PackageLite pkg) {
        long sizeBytes = 0;
        Collection<String> dexMetadataList = getPackageDexMetadata(pkg).values();
        for (String dexMetadata : dexMetadataList) {
            sizeBytes += new File(dexMetadata).length();
        }
        return sizeBytes;
    }

    public static File findDexMetadataForFile(File targetFile) {
        String dexMetadataPath = buildDexMetadataPathForFile(targetFile);
        File dexMetadataFile = new File(dexMetadataPath);
        if (dexMetadataFile.exists()) {
            return dexMetadataFile;
        }
        return null;
    }

    private static Map<String, String> getPackageDexMetadata(PackageLite pkg) {
        return buildPackageApkToDexMetadataMap(pkg.getAllApkPaths());
    }

    public static Map<String, String> buildPackageApkToDexMetadataMap(List<String> codePaths) {
        ArrayMap<String, String> result = new ArrayMap<>();
        for (int i = codePaths.size() - 1; i >= 0; i--) {
            String codePath = codePaths.get(i);
            String dexMetadataPath = buildDexMetadataPathForFile(new File(codePath));
            if (Files.exists(Paths.get(dexMetadataPath, new String[0]), new LinkOption[0])) {
                result.put(codePath, dexMetadataPath);
            }
        }
        return result;
    }

    public static String buildDexMetadataPathForApk(String codePath) {
        if (!ApkLiteParseUtils.isApkPath(codePath)) {
            throw new IllegalStateException("Corrupted package. Code path is not an apk " + codePath);
        }
        return codePath.substring(0, codePath.length() - ".apk".length()) + ".dm";
    }

    private static String buildDexMetadataPathForFile(File targetFile) {
        if (ApkLiteParseUtils.isApkFile(targetFile)) {
            return buildDexMetadataPathForApk(targetFile.getPath());
        }
        return targetFile.getPath() + ".dm";
    }

    public static void validateDexMetadataFile(String dmaPath, String packageName, long versionCode) throws PackageParser.PackageParserException {
        validateDexMetadataFile(dmaPath, packageName, versionCode, SystemProperties.getBoolean(PROPERTY_DM_JSON_MANIFEST_REQUIRED, false));
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0064 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static void validateDexMetadataFile(String dmaPath, String packageName, long versionCode, boolean requireManifest) throws PackageParser.PackageParserException {
        IOException e;
        IOException e2;
        StrictJarFile jarFile = null;
        if (DEBUG) {
            Log.v(TAG, "validateDexMetadataFile: " + dmaPath + ", " + packageName + ", " + versionCode);
        }
        try {
            try {
                StrictJarFile jarFile2 = new StrictJarFile(dmaPath, false, false);
                try {
                    validateDexMetadataManifest(dmaPath, jarFile2, packageName, versionCode, requireManifest);
                    try {
                        jarFile2.close();
                    } catch (IOException e3) {
                    }
                } catch (IOException e4) {
                    e2 = e4;
                    throw new PackageParser.PackageParserException(PackageManager.INSTALL_FAILED_BAD_DEX_METADATA, "Error opening " + dmaPath, e2);
                }
            } catch (Throwable th) {
                e = th;
                if (0 != 0) {
                    try {
                        jarFile.close();
                    } catch (IOException e5) {
                    }
                }
                throw e;
            }
        } catch (IOException e6) {
            e2 = e6;
        } catch (Throwable th2) {
            e = th2;
            if (0 != 0) {
            }
            throw e;
        }
    }

    private static void validateDexMetadataManifest(String dmaPath, StrictJarFile jarFile, String packageName, long versionCode, boolean requireManifest) throws IOException, PackageParser.PackageParserException {
        if (!requireManifest) {
            if (DEBUG) {
                Log.v(TAG, "validateDexMetadataManifest: " + dmaPath + " manifest.json check skipped");
                return;
            }
            return;
        }
        ZipEntry zipEntry = jarFile.findEntry("manifest.json");
        if (zipEntry == null) {
            throw new PackageParser.PackageParserException(PackageManager.INSTALL_FAILED_BAD_DEX_METADATA, "Missing manifest.json in " + dmaPath);
        }
        InputStream inputStream = jarFile.getInputStream(zipEntry);
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            String jsonPackageName = null;
            long jsonVersionCode = -1;
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("packageName")) {
                    jsonPackageName = reader.nextString();
                } else if (name.equals("versionCode")) {
                    jsonVersionCode = reader.nextLong();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            if (jsonPackageName == null || jsonVersionCode == -1) {
                throw new PackageParser.PackageParserException(PackageManager.INSTALL_FAILED_BAD_DEX_METADATA, "manifest.json in " + dmaPath + " is missing 'packageName' and/or 'versionCode'");
            } else if (!jsonPackageName.equals(packageName)) {
                throw new PackageParser.PackageParserException(PackageManager.INSTALL_FAILED_BAD_DEX_METADATA, "manifest.json in " + dmaPath + " has invalid packageName: " + jsonPackageName + ", expected: " + packageName);
            } else if (versionCode != jsonVersionCode) {
                throw new PackageParser.PackageParserException(PackageManager.INSTALL_FAILED_BAD_DEX_METADATA, "manifest.json in " + dmaPath + " has invalid versionCode: " + jsonVersionCode + ", expected: " + versionCode);
            } else if (DEBUG) {
                Log.v(TAG, "validateDexMetadataManifest: " + dmaPath + ", " + packageName + ", " + versionCode + ": successful");
            }
        } catch (UnsupportedEncodingException e) {
            throw new PackageParser.PackageParserException(PackageManager.INSTALL_FAILED_BAD_DEX_METADATA, "Error opening manifest.json in " + dmaPath, e);
        }
    }

    public static void validateDexPaths(String[] paths) {
        ArrayList<String> apks = new ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            if (ApkLiteParseUtils.isApkPath(paths[i])) {
                apks.add(paths[i]);
            }
        }
        ArrayList<String> unmatchedDmFiles = new ArrayList<>();
        for (String dmPath : paths) {
            if (isDexMetadataPath(dmPath)) {
                boolean valid = false;
                int j = apks.size() - 1;
                while (true) {
                    if (j >= 0) {
                        if (dmPath.equals(buildDexMetadataPathForFile(new File(apks.get(j))))) {
                            valid = true;
                            break;
                        } else {
                            j--;
                        }
                    } else {
                        break;
                    }
                }
                if (!valid) {
                    unmatchedDmFiles.add(dmPath);
                }
            }
        }
        if (!unmatchedDmFiles.isEmpty()) {
            throw new IllegalStateException("Unmatched .dm files: " + unmatchedDmFiles);
        }
    }
}
