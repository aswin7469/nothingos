package android.content.pm.parsing.component;

import android.content.pm.PathPermission;
import android.content.pm.parsing.ParsingPackage;
import android.content.pm.parsing.ParsingUtils;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.PatternMatcher;
import android.util.Slog;
import com.android.internal.R;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ParsedProviderUtils {
    private static final String TAG = "PackageParsing";

    public static ParseResult<ParsedProvider> parseProvider(String[] separateProcesses, ParsingPackage pkg, Resources res, XmlResourceParser parser, int flags, boolean useRoundIcon, ParseInput input) throws IOException, XmlPullParserException {
        TypedArray sa;
        ParseInput parseInput;
        int targetSdkVersion = pkg.getTargetSdkVersion();
        String packageName = pkg.getPackageName();
        ParsedProvider provider = new ParsedProvider();
        String tag = parser.getName();
        TypedArray sa2 = res.obtainAttributes(parser, R.styleable.AndroidManifestProvider);
        try {
            try {
                ParseResult<ParsedProvider> result = ParsedMainComponentUtils.parseMainComponent(provider, tag, separateProcesses, pkg, sa2, flags, useRoundIcon, input, 17, 14, 18, 6, 1, 0, 15, 2, 8, 19, 21, 23);
                if (result.isError()) {
                    sa2.recycle();
                    return result;
                }
                sa = sa2;
                try {
                    String authority = sa.getNonConfigurationString(10, 0);
                    try {
                        try {
                            provider.exported = sa.getBoolean(7, targetSdkVersion < 17);
                            provider.syncable = sa.getBoolean(11, false);
                            String permission = sa.getNonConfigurationString(3, 0);
                            String readPermission = sa.getNonConfigurationString(4, 0);
                            if (readPermission == null) {
                                readPermission = permission;
                            }
                            if (readPermission == null) {
                                try {
                                    provider.setReadPermission(pkg.getPermission());
                                } catch (Throwable th) {
                                    th = th;
                                    sa.recycle();
                                    throw th;
                                }
                            } else {
                                provider.setReadPermission(readPermission);
                            }
                            String writePermission = sa.getNonConfigurationString(5, 0);
                            if (writePermission == null) {
                                writePermission = permission;
                            }
                            if (writePermission == null) {
                                provider.setWritePermission(pkg.getPermission());
                            } else {
                                provider.setWritePermission(writePermission);
                            }
                            provider.grantUriPermissions = sa.getBoolean(13, false);
                            provider.forceUriPermissions = sa.getBoolean(22, false);
                            provider.multiProcess = sa.getBoolean(9, false);
                            provider.initOrder = sa.getInt(12, 0);
                            provider.flags |= ComponentParseUtils.flag(1073741824, 16, sa);
                            boolean visibleToEphemeral = sa.getBoolean(20, false);
                            if (visibleToEphemeral) {
                                provider.flags |= 1048576;
                                try {
                                    pkg.mo1007setVisibleToInstantApps(true);
                                } catch (Throwable th2) {
                                    th = th2;
                                    sa.recycle();
                                    throw th;
                                }
                            }
                            sa.recycle();
                            if (!pkg.isCantSaveState()) {
                                parseInput = input;
                            } else if (Objects.equals(provider.getProcessName(), packageName)) {
                                return input.error("Heavy-weight applications can not have providers in main process");
                            } else {
                                parseInput = input;
                            }
                            if (authority == null) {
                                return parseInput.error("<provider> does not include authorities attribute");
                            }
                            if (authority.length() <= 0) {
                                return parseInput.error("<provider> has empty authorities attribute");
                            }
                            provider.setAuthority(authority);
                            return parseProviderTags(pkg, tag, res, parser, visibleToEphemeral, provider, input);
                        } catch (Throwable th3) {
                            th = th3;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                    }
                } catch (Throwable th5) {
                    th = th5;
                }
            } catch (Throwable th6) {
                th = th6;
                sa = sa2;
            }
        } catch (Throwable th7) {
            th = th7;
            sa = sa2;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x00fa, code lost:
        return r24.success(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x005e, code lost:
        if (r8.equals("meta-data") != false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ParseResult<ParsedProvider> parseProviderTags(ParsingPackage pkg, String tag, Resources res, XmlResourceParser parser, boolean visibleToEphemeral, ParsedProvider provider, ParseInput input) throws XmlPullParserException, IOException {
        ParseResult result;
        int depth = parser.getDepth();
        while (true) {
            int type = parser.next();
            char c = 1;
            if (type != 1 && (type != 3 || parser.getDepth() > depth)) {
                if (type == 2) {
                    String name = parser.getName();
                    switch (name.hashCode()) {
                        case -1814617695:
                            if (name.equals("grant-uri-permission")) {
                                c = 3;
                                break;
                            }
                            c = 65535;
                            break;
                        case -1115949454:
                            break;
                        case -1029793847:
                            if (name.equals("intent-filter")) {
                                c = 0;
                                break;
                            }
                            c = 65535;
                            break;
                        case -993141291:
                            if (name.equals("property")) {
                                c = 2;
                                break;
                            }
                            c = 65535;
                            break;
                        case 636171383:
                            if (name.equals("path-permission")) {
                                c = 4;
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
                            ParseResult intentResult = ParsedMainComponentUtils.parseIntentFilter(provider, pkg, res, parser, visibleToEphemeral, true, false, false, false, input);
                            result = intentResult;
                            if (intentResult.isSuccess()) {
                                ParsedIntentInfo intent = intentResult.getResult();
                                provider.order = Math.max(intent.getOrder(), provider.order);
                                provider.addIntent(intent);
                            }
                            break;
                        case 1:
                            ParseResult result2 = ParsedComponentUtils.addMetaData(provider, pkg, res, parser, input);
                            result = result2;
                            break;
                        case 2:
                            ParseResult result3 = ParsedComponentUtils.addProperty(provider, pkg, res, parser, input);
                            result = result3;
                            break;
                        case 3:
                            ParseResult result4 = parseGrantUriPermission(provider, pkg, res, parser, input);
                            result = result4;
                            break;
                        case 4:
                            ParseResult result5 = parsePathPermission(provider, pkg, res, parser, input);
                            result = result5;
                            break;
                        default:
                            result = ParsingUtils.unknownTag(tag, pkg, parser, input);
                            break;
                    }
                    if (result.isError()) {
                        return input.error(result);
                    }
                }
            }
        }
    }

    private static ParseResult<ParsedProvider> parseGrantUriPermission(ParsedProvider provider, ParsingPackage pkg, Resources resources, XmlResourceParser parser, ParseInput input) {
        TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestGrantUriPermission);
        try {
            String name = parser.getName();
            PatternMatcher pa = null;
            String str = sa.getNonConfigurationString(4, 0);
            if (str != null) {
                pa = new PatternMatcher(str, 3);
            } else {
                String str2 = sa.getNonConfigurationString(2, 0);
                if (str2 != null) {
                    pa = new PatternMatcher(str2, 2);
                } else {
                    String str3 = sa.getNonConfigurationString(1, 0);
                    if (str3 != null) {
                        pa = new PatternMatcher(str3, 1);
                    } else {
                        String str4 = sa.getNonConfigurationString(3, 0);
                        if (str4 != null) {
                            pa = new PatternMatcher(str4, 4);
                        } else {
                            String str5 = sa.getNonConfigurationString(0, 0);
                            if (str5 != null) {
                                pa = new PatternMatcher(str5, 0);
                            }
                        }
                    }
                }
            }
            if (pa != null) {
                if (provider.uriPermissionPatterns == null) {
                    provider.uriPermissionPatterns = new PatternMatcher[1];
                    provider.uriPermissionPatterns[0] = pa;
                } else {
                    int N = provider.uriPermissionPatterns.length;
                    PatternMatcher[] newp = new PatternMatcher[N + 1];
                    System.arraycopy(provider.uriPermissionPatterns, 0, newp, 0, N);
                    newp[N] = pa;
                    provider.uriPermissionPatterns = newp;
                }
                provider.grantUriPermissions = true;
            } else {
                Slog.w("PackageParsing", "Unknown element under <path-permission>: " + name + " at " + pkg.getBaseApkPath() + " " + parser.getPositionDescription());
            }
            return input.success(provider);
        } finally {
            sa.recycle();
        }
    }

    private static ParseResult<ParsedProvider> parsePathPermission(ParsedProvider provider, ParsingPackage pkg, Resources resources, XmlResourceParser parser, ParseInput input) {
        TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestPathPermission);
        try {
            String name = parser.getName();
            String permission = sa.getNonConfigurationString(0, 0);
            String readPermission = sa.getNonConfigurationString(1, 0);
            if (readPermission == null) {
                readPermission = permission;
            }
            String writePermission = sa.getNonConfigurationString(2, 0);
            if (writePermission == null) {
                writePermission = permission;
            }
            boolean havePerm = false;
            if (readPermission != null) {
                readPermission = readPermission.intern();
                havePerm = true;
            }
            if (writePermission != null) {
                writePermission = writePermission.intern();
                havePerm = true;
            }
            if (!havePerm) {
                Slog.w("PackageParsing", "No readPermission or writePermission for <path-permission>: " + name + " at " + pkg.getBaseApkPath() + " " + parser.getPositionDescription());
                return input.success(provider);
            }
            PathPermission pa = null;
            String path = sa.getNonConfigurationString(7, 0);
            if (path != null) {
                pa = new PathPermission(path, 3, readPermission, writePermission);
            } else {
                String path2 = sa.getNonConfigurationString(5, 0);
                if (path2 != null) {
                    pa = new PathPermission(path2, 2, readPermission, writePermission);
                } else {
                    String path3 = sa.getNonConfigurationString(4, 0);
                    if (path3 != null) {
                        pa = new PathPermission(path3, 1, readPermission, writePermission);
                    } else {
                        String path4 = sa.getNonConfigurationString(6, 0);
                        if (path4 != null) {
                            pa = new PathPermission(path4, 4, readPermission, writePermission);
                        } else {
                            String path5 = sa.getNonConfigurationString(3, 0);
                            if (path5 != null) {
                                pa = new PathPermission(path5, 0, readPermission, writePermission);
                            }
                        }
                    }
                }
            }
            if (pa != null) {
                if (provider.pathPermissions == null) {
                    provider.pathPermissions = new PathPermission[1];
                    provider.pathPermissions[0] = pa;
                } else {
                    int N = provider.pathPermissions.length;
                    PathPermission[] newp = new PathPermission[N + 1];
                    System.arraycopy(provider.pathPermissions, 0, newp, 0, N);
                    newp[N] = pa;
                    provider.pathPermissions = newp;
                }
            } else {
                Slog.w("PackageParsing", "No path, pathPrefix, or pathPattern for <path-permission>: " + name + " at " + pkg.getBaseApkPath() + " " + parser.getPositionDescription());
            }
            return input.success(provider);
        } finally {
            sa.recycle();
        }
    }
}
