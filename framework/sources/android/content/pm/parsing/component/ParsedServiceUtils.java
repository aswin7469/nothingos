package android.content.pm.parsing.component;

import android.content.pm.parsing.ParsingPackage;
import android.content.pm.parsing.ParsingUtils;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import com.android.internal.R;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ParsedServiceUtils {
    /* JADX WARN: Code restructure failed: missing block: B:44:0x024b, code lost:
        if (r2 != false) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0255, code lost:
        if (r14.getIntents().size() <= 0) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0258, code lost:
        r19 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x025a, code lost:
        r3 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x025c, code lost:
        if (r3 == 0) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x025e, code lost:
        r4 = r15.deferError(r14.getName() + ": Targeting S+ (version 31 and above) requires that an explicit value for android:exported be defined when intent filters are present", android.content.pm.parsing.result.ParseInput.DeferredError.MISSING_EXPORTED_FLAG);
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0288, code lost:
        if (r4.isError() == false) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x028e, code lost:
        return r15.error(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x028f, code lost:
        r14.exported = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0295, code lost:
        return r15.success(r14);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ParseResult<ParsedService> parseService(String[] separateProcesses, ParsingPackage pkg, Resources res, XmlResourceParser parser, int flags, boolean useRoundIcon, ParseInput input) throws XmlPullParserException, IOException {
        TypedArray sa;
        int i;
        ParsedService service;
        int i2;
        ParsingPackage parsingPackage;
        ParseInput parseInput;
        String packageName;
        ParseInput parseInput2;
        int i3;
        ParseInput parseInput3;
        String packageName2;
        int i4;
        int i5;
        ParsingPackage parsingPackage2;
        ParseResult parseResult;
        String tag;
        String packageName3 = pkg.getPackageName();
        ParsedService service2 = new ParsedService();
        String tag2 = parser.getName();
        TypedArray sa2 = res.obtainAttributes(parser, R.styleable.AndroidManifestService);
        try {
            String tag3 = tag2;
            try {
                ParseResult<ParsedService> result = ParsedMainComponentUtils.parseMainComponent(service2, tag2, separateProcesses, pkg, sa2, flags, useRoundIcon, input, 12, 7, 13, 4, 1, 0, 8, 2, 6, 15, 17, 20);
                if (result.isError()) {
                    sa2.recycle();
                    return result;
                }
                sa = sa2;
                try {
                    boolean setExported = sa.hasValue(5);
                    i = 0;
                    if (setExported) {
                        try {
                            service = service2;
                        } catch (Throwable th) {
                            th = th;
                            sa.recycle();
                            throw th;
                        }
                        try {
                            service.exported = sa.getBoolean(5, false);
                        } catch (Throwable th2) {
                            th = th2;
                            sa.recycle();
                            throw th;
                        }
                    } else {
                        service = service2;
                    }
                    i2 = 3;
                } catch (Throwable th3) {
                    th = th3;
                }
                try {
                    String permission = sa.getNonConfigurationString(3, 0);
                    service.setPermission(permission != null ? permission : pkg.getPermission());
                    service.foregroundServiceType = sa.getInt(19, 0);
                    int i6 = 1;
                    int i7 = 2;
                    service.flags |= ComponentParseUtils.flag(1, 9, sa) | ComponentParseUtils.flag(2, 10, sa) | ComponentParseUtils.flag(4, 14, sa) | ComponentParseUtils.flag(8, 18, sa) | ComponentParseUtils.flag(1073741824, 11, sa);
                    boolean visibleToEphemeral = sa.getBoolean(16, false);
                    if (visibleToEphemeral) {
                        service.flags |= 1048576;
                        parsingPackage = pkg;
                        try {
                            parsingPackage.mo1007setVisibleToInstantApps(true);
                        } catch (Throwable th4) {
                            th = th4;
                            sa.recycle();
                            throw th;
                        }
                    } else {
                        parsingPackage = pkg;
                    }
                    sa.recycle();
                    if (pkg.isCantSaveState()) {
                        packageName = packageName3;
                        if (Objects.equals(service.getProcessName(), packageName)) {
                            return input.error("Heavy-weight applications can not have services in main process");
                        }
                        parseInput = input;
                    } else {
                        parseInput = input;
                        packageName = packageName3;
                    }
                    int depth = parser.getDepth();
                    while (true) {
                        int type = parser.next();
                        if (type == i6) {
                            parseInput2 = parseInput;
                            i3 = i6;
                        } else if (type == i2 && parser.getDepth() <= depth) {
                            parseInput2 = parseInput;
                            i3 = i6;
                        } else if (type == i7) {
                            String name = parser.getName();
                            int i8 = -1;
                            switch (name.hashCode()) {
                                case -1115949454:
                                    if (name.equals("meta-data")) {
                                        i8 = i6;
                                        break;
                                    }
                                    break;
                                case -1029793847:
                                    if (name.equals("intent-filter")) {
                                        i8 = i;
                                        break;
                                    }
                                    break;
                                case -993141291:
                                    if (name.equals("property")) {
                                        i8 = i7;
                                        break;
                                    }
                                    break;
                            }
                            switch (i8) {
                                case 0:
                                    parseInput3 = parseInput;
                                    packageName2 = packageName;
                                    i4 = i7;
                                    i5 = i6;
                                    ParseResult intentResult = ParsedMainComponentUtils.parseIntentFilter(service, pkg, res, parser, visibleToEphemeral, true, false, false, false, input);
                                    if (intentResult.isSuccess()) {
                                        ParsedIntentInfo intent = intentResult.getResult();
                                        service.order = Math.max(intent.getOrder(), service.order);
                                        service.addIntent(intent);
                                    }
                                    parsingPackage2 = pkg;
                                    parseResult = intentResult;
                                    tag = tag3;
                                    break;
                                case 1:
                                    ParseResult parseResult2 = ParsedComponentUtils.addMetaData(service, parsingPackage, res, parser, parseInput);
                                    parseInput3 = parseInput;
                                    packageName2 = packageName;
                                    i4 = i7;
                                    i5 = i6;
                                    tag = tag3;
                                    parseResult = parseResult2;
                                    parsingPackage2 = parsingPackage;
                                    break;
                                case 2:
                                    ParseResult parseResult3 = ParsedComponentUtils.addProperty(service, parsingPackage, res, parser, parseInput);
                                    parseInput3 = parseInput;
                                    packageName2 = packageName;
                                    i4 = i7;
                                    i5 = i6;
                                    tag = tag3;
                                    parseResult = parseResult3;
                                    parsingPackage2 = parsingPackage;
                                    break;
                                default:
                                    parseInput3 = parseInput;
                                    packageName2 = packageName;
                                    i4 = i7;
                                    i5 = i6;
                                    parsingPackage2 = pkg;
                                    tag = tag3;
                                    parseResult = ParsingUtils.unknownTag(tag, parsingPackage2, parser, parseInput3);
                                    break;
                            }
                            if (parseResult.isError()) {
                                return parseInput3.error(parseResult);
                            }
                            parsingPackage = parsingPackage2;
                            tag3 = tag;
                            parseInput = parseInput3;
                            packageName = packageName2;
                            i7 = i4;
                            i6 = i5;
                            i = 0;
                            i2 = 3;
                        }
                    }
                } catch (Throwable th5) {
                    th = th5;
                    sa.recycle();
                    throw th;
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
}
