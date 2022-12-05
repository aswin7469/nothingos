package android.content.pm.parsing.component;

import android.app.ActivityTaskManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.parsing.ParsingPackage;
import android.content.pm.parsing.ParsingUtils;
import android.content.pm.parsing.result.ParseInput;
import android.content.pm.parsing.result.ParseResult;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.media.TtmlUtils;
import android.os.IncidentManager;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.R;
import com.android.internal.util.ArrayUtils;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class ParsedActivityUtils {
    public static final boolean LOG_UNSAFE_BROADCASTS = false;
    private static final int RECREATE_ON_CONFIG_CHANGES_MASK = 3;
    public static final Set<String> SAFE_BROADCASTS;
    private static final String TAG = "PackageParsing";

    static {
        ArraySet arraySet = new ArraySet();
        SAFE_BROADCASTS = arraySet;
        arraySet.add(Intent.ACTION_BOOT_COMPLETED);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0286  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x02b0 A[Catch: all -> 0x02b8, TRY_ENTER, TRY_LEAVE, TryCatch #5 {all -> 0x02b8, blocks: (B:45:0x0247, B:47:0x0258, B:49:0x025f, B:50:0x0266, B:52:0x026e, B:54:0x0275, B:24:0x02b0, B:30:0x02cc), top: B:44:0x0247 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x02ba A[Catch: all -> 0x0306, TRY_ENTER, TRY_LEAVE, TryCatch #3 {all -> 0x0306, blocks: (B:18:0x00e7, B:22:0x0299, B:27:0x02ba, B:21:0x0288), top: B:17:0x00e7 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x016f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static ParseResult<ParsedActivity> parseActivityOrReceiver(String[] separateProcesses, ParsingPackage pkg, Resources res, XmlResourceParser parser, int flags, boolean useRoundIcon, ParseInput input) throws XmlPullParserException, IOException {
        TypedArray sa;
        String packageName;
        ParseInput parseInput;
        ParsingPackage parsingPackage;
        ParseResult<String> affinityNameResult;
        String packageName2 = pkg.getPackageName();
        ParsedActivity activity = new ParsedActivity();
        boolean receiver = IncidentManager.URI_PARAM_RECEIVER_CLASS.equals(parser.getName());
        String tag = "<" + parser.getName() + ">";
        TypedArray sa2 = res.obtainAttributes(parser, R.styleable.AndroidManifestActivity);
        try {
            try {
                ParseResult<ParsedActivity> result = ParsedMainComponentUtils.parseMainComponent(activity, tag, separateProcesses, pkg, sa2, flags, useRoundIcon, input, 30, 17, 42, 5, 2, 1, 23, 3, 7, 44, 48, 57);
                if (result.isError()) {
                    sa2.recycle();
                    return result;
                }
                try {
                    try {
                        if (receiver) {
                            try {
                                if (pkg.isCantSaveState()) {
                                    packageName = packageName2;
                                    try {
                                        if (Objects.equals(activity.getProcessName(), packageName)) {
                                            try {
                                                ParseResult<ParsedActivity> error = input.error("Heavy-weight applications can not have receivers in main process");
                                                sa2.recycle();
                                                return error;
                                            } catch (Throwable th) {
                                                th = th;
                                                sa = sa2;
                                                sa.recycle();
                                                throw th;
                                            }
                                        }
                                        parseInput = input;
                                        activity.theme = sa2.getResourceId(0, 0);
                                        activity.uiOptions = sa2.getInt(26, pkg.getUiOptions());
                                        activity.flags |= ComponentParseUtils.flag(64, 19, pkg.isAllowTaskReparenting(), sa2) | ComponentParseUtils.flag(8, 18, sa2) | ComponentParseUtils.flag(4, 11, sa2) | ComponentParseUtils.flag(32, 13, sa2) | ComponentParseUtils.flag(256, 22, sa2) | ComponentParseUtils.flag(2, 10, sa2) | ComponentParseUtils.flag(2048, 24, sa2) | ComponentParseUtils.flag(1, 9, sa2) | ComponentParseUtils.flag(128, 21, sa2) | ComponentParseUtils.flag(1024, 39, sa2) | ComponentParseUtils.flag(1024, 29, sa2) | ComponentParseUtils.flag(16, 12, sa2) | ComponentParseUtils.flag(536870912, 60, sa2);
                                        if (receiver) {
                                            try {
                                                activity.flags |= ComponentParseUtils.flag(512, 25, pkg.isBaseHardwareAccelerated(), sa2) | ComponentParseUtils.flag(Integer.MIN_VALUE, 31, sa2) | ComponentParseUtils.flag(262144, 59, sa2) | ComponentParseUtils.flag(8192, 35, sa2) | ComponentParseUtils.flag(4096, 36, sa2) | ComponentParseUtils.flag(16384, 37, sa2) | ComponentParseUtils.flag(8388608, 51, sa2) | ComponentParseUtils.flag(4194304, 41, sa2) | ComponentParseUtils.flag(16777216, 52, sa2) | ComponentParseUtils.flag(33554432, 56, sa2);
                                                activity.privateFlags |= ComponentParseUtils.flag(1, 54, sa2) | ComponentParseUtils.flag(2, 58, true, sa2);
                                                activity.colorMode = sa2.getInt(49, 0);
                                                activity.documentLaunchMode = sa2.getInt(33, 0);
                                                activity.launchMode = sa2.getInt(14, 0);
                                                activity.lockTaskLaunchMode = sa2.getInt(38, 0);
                                                activity.maxRecents = sa2.getInt(34, ActivityTaskManager.getDefaultAppRecentsLimitStatic());
                                                activity.persistableMode = sa2.getInteger(32, 0);
                                                activity.requestedVrComponent = sa2.getString(43);
                                                activity.rotationAnimation = sa2.getInt(46, -1);
                                                activity.softInputMode = sa2.getInt(20, 0);
                                                activity.configChanges = getActivityConfigChanges(sa2.getInt(16, 0), sa2.getInt(47, 0));
                                                int screenOrientation = sa2.getInt(15, -1);
                                                parsingPackage = pkg;
                                                try {
                                                    int resizeMode = getActivityResizeMode(parsingPackage, sa2, screenOrientation);
                                                    activity.screenOrientation = screenOrientation;
                                                    activity.resizeMode = resizeMode;
                                                    if (sa2.hasValue(50) && sa2.getType(50) == 4) {
                                                        activity.setMaxAspectRatio(resizeMode, sa2.getFloat(50, 0.0f));
                                                    }
                                                    if (sa2.hasValue(53) && sa2.getType(53) == 4) {
                                                        activity.setMinAspectRatio(resizeMode, sa2.getFloat(53, 0.0f));
                                                    }
                                                } catch (Throwable th2) {
                                                    th = th2;
                                                    sa = sa2;
                                                    sa.recycle();
                                                    throw th;
                                                }
                                            } catch (Throwable th3) {
                                                th = th3;
                                            }
                                        } else {
                                            parsingPackage = pkg;
                                            activity.launchMode = 0;
                                            activity.configChanges = 0;
                                            activity.flags |= ComponentParseUtils.flag(1073741824, 28, sa2);
                                        }
                                        String taskAffinity = sa2.getNonConfigurationString(8, 1024);
                                        affinityNameResult = ComponentParseUtils.buildTaskAffinityName(packageName, pkg.getTaskAffinity(), taskAffinity, parseInput);
                                        if (!affinityNameResult.isError()) {
                                            ParseResult<ParsedActivity> error2 = parseInput.error(affinityNameResult);
                                            sa2.recycle();
                                            return error2;
                                        }
                                        activity.taskAffinity = affinityNameResult.getResult();
                                        boolean visibleToEphemeral = sa2.getBoolean(45, false);
                                        if (visibleToEphemeral) {
                                            activity.flags |= 1048576;
                                            parsingPackage.mo1007setVisibleToInstantApps(true);
                                        }
                                        sa = sa2;
                                        try {
                                            ParseResult<ParsedActivity> parseActivityOrAlias = parseActivityOrAlias(activity, pkg, tag, parser, res, sa2, receiver, false, visibleToEphemeral, input, 27, 4, 6);
                                            sa.recycle();
                                            return parseActivityOrAlias;
                                        } catch (Throwable th4) {
                                            th = th4;
                                            sa.recycle();
                                            throw th;
                                        }
                                    } catch (Throwable th5) {
                                        th = th5;
                                    }
                                }
                            } catch (Throwable th6) {
                                th = th6;
                                sa = sa2;
                            }
                        }
                        activity.theme = sa2.getResourceId(0, 0);
                        activity.uiOptions = sa2.getInt(26, pkg.getUiOptions());
                        activity.flags |= ComponentParseUtils.flag(64, 19, pkg.isAllowTaskReparenting(), sa2) | ComponentParseUtils.flag(8, 18, sa2) | ComponentParseUtils.flag(4, 11, sa2) | ComponentParseUtils.flag(32, 13, sa2) | ComponentParseUtils.flag(256, 22, sa2) | ComponentParseUtils.flag(2, 10, sa2) | ComponentParseUtils.flag(2048, 24, sa2) | ComponentParseUtils.flag(1, 9, sa2) | ComponentParseUtils.flag(128, 21, sa2) | ComponentParseUtils.flag(1024, 39, sa2) | ComponentParseUtils.flag(1024, 29, sa2) | ComponentParseUtils.flag(16, 12, sa2) | ComponentParseUtils.flag(536870912, 60, sa2);
                        if (receiver) {
                        }
                        String taskAffinity2 = sa2.getNonConfigurationString(8, 1024);
                        affinityNameResult = ComponentParseUtils.buildTaskAffinityName(packageName, pkg.getTaskAffinity(), taskAffinity2, parseInput);
                        if (!affinityNameResult.isError()) {
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        sa = sa2;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    sa = sa2;
                }
                parseInput = input;
                packageName = packageName2;
            } catch (Throwable th9) {
                th = th9;
                sa = sa2;
            }
        } catch (Throwable th10) {
            th = th10;
            sa = sa2;
        }
    }

    public static ParseResult<ParsedActivity> parseActivityAlias(ParsingPackage pkg, Resources res, XmlResourceParser parser, boolean useRoundIcon, ParseInput input) throws XmlPullParserException, IOException {
        TypedArray sa;
        ParsedActivity target;
        TypedArray sa2 = res.obtainAttributes(parser, R.styleable.AndroidManifestActivityAlias);
        try {
            String targetActivity = sa2.getNonConfigurationString(7, 1024);
            try {
                if (targetActivity == null) {
                    ParseResult<ParsedActivity> error = input.error("<activity-alias> does not specify android:targetActivity");
                    sa2.recycle();
                    return error;
                }
                String packageName = pkg.getPackageName();
                String targetActivity2 = ParsingUtils.buildClassName(packageName, targetActivity);
                if (targetActivity2 == null) {
                    ParseResult<ParsedActivity> error2 = input.error("Empty class name in package " + packageName);
                    sa2.recycle();
                    return error2;
                }
                List<ParsedActivity> activities = pkg.getActivities();
                int activitiesSize = ArrayUtils.size(activities);
                int i = 0;
                while (true) {
                    if (i >= activitiesSize) {
                        target = null;
                        break;
                    }
                    ParsedActivity t = activities.get(i);
                    if (targetActivity2.equals(t.getName())) {
                        target = t;
                        break;
                    }
                    i++;
                }
                if (target == null) {
                    ParseResult<ParsedActivity> error3 = input.error("<activity-alias> target activity " + targetActivity2 + " not found in manifest with activities = " + pkg.getActivities() + ", parsedActivities = " + activities);
                    sa2.recycle();
                    return error3;
                }
                ParsedActivity activity = ParsedActivity.makeAlias(targetActivity2, target);
                String tag = "<" + parser.getName() + ">";
                sa = sa2;
                try {
                    ParseResult<ParsedActivity> result = ParsedMainComponentUtils.parseMainComponent(activity, tag, null, pkg, sa2, 0, useRoundIcon, input, 10, 6, null, 4, 1, 0, 8, 2, null, 11, null, 12);
                    if (result.isError()) {
                        sa.recycle();
                        return result;
                    }
                    boolean visibleToEphemeral = (activity.getFlags() & 1048576) != 0;
                    ParseResult<ParsedActivity> parseActivityOrAlias = parseActivityOrAlias(activity, pkg, tag, parser, res, sa, false, true, visibleToEphemeral, input, 9, 3, 5);
                    sa.recycle();
                    return parseActivityOrAlias;
                } catch (Throwable th) {
                    th = th;
                    sa.recycle();
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                sa = sa2;
            }
        } catch (Throwable th3) {
            th = th3;
            sa = sa2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x019a, code lost:
        if (r28 != false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x019f, code lost:
        if (r21.launchMode == 4) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x01a3, code lost:
        if (r21.metaData == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x01ad, code lost:
        if (r21.metaData.containsKey(android.content.pm.parsing.ParsingPackageUtils.METADATA_ACTIVITY_LAUNCH_MODE) == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x01af, code lost:
        r2 = r21.metaData.getString(android.content.pm.parsing.ParsingPackageUtils.METADATA_ACTIVITY_LAUNCH_MODE);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x01b5, code lost:
        if (r2 == null) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x01be, code lost:
        if (r2.equals("singleInstancePerTask") == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x01c0, code lost:
        r21.launchMode = 4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x01c2, code lost:
        r2 = resolveActivityWindowLayout(r21, r30);
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x01cb, code lost:
        if (r2.isError() == false) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x01d1, code lost:
        return r30.error(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x01d2, code lost:
        r21.windowLayout = r2.getResult();
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x01da, code lost:
        if (r16 != false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x01e4, code lost:
        if (r21.getIntents().size() <= 0) goto L51;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x01e6, code lost:
        r6 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x01ea, code lost:
        r1 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x01eb, code lost:
        if (r1 == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x01ed, code lost:
        r3 = r30.deferError(r21.getName() + ": Targeting S+ (version 31 and above) requires that an explicit value for android:exported be defined when intent filters are present", android.content.pm.parsing.result.ParseInput.DeferredError.MISSING_EXPORTED_FLAG);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0217, code lost:
        if (r3.isError() == false) goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x021d, code lost:
        return r30.error(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x021e, code lost:
        r21.exported = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x01e8, code lost:
        r6 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0224, code lost:
        return r30.success(r21);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ParseResult<ParsedActivity> parseActivityOrAlias(ParsedActivity activity, ParsingPackage pkg, String tag, XmlResourceParser parser, Resources resources, TypedArray array, boolean isReceiver, boolean isAlias, boolean visibleToEphemeral, ParseInput input, int parentActivityNameAttr, int permissionAttr, int exportedAttr) throws IOException, XmlPullParserException {
        boolean z;
        int depth;
        String permission;
        boolean z2;
        ParseResult result;
        ParsedIntentInfo intent;
        ParsedIntentInfo intent2;
        String parentActivityName = array.getNonConfigurationString(parentActivityNameAttr, 1024);
        if (parentActivityName != null) {
            String packageName = pkg.getPackageName();
            String parentClassName = ParsingUtils.buildClassName(packageName, parentActivityName);
            if (parentClassName == null) {
                Log.e("PackageParsing", "Activity " + activity.getName() + " specified invalid parentActivityName " + parentActivityName);
            } else {
                activity.setParentActivity(parentClassName);
            }
        }
        boolean z3 = false;
        String permission2 = array.getNonConfigurationString(permissionAttr, 0);
        if (!isAlias) {
            activity.setPermission(permission2 != null ? permission2 : pkg.getPermission());
        } else {
            activity.setPermission(permission2);
        }
        boolean setExported = array.hasValue(exportedAttr);
        if (setExported) {
            activity.exported = array.getBoolean(exportedAttr, false);
        }
        int depth2 = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1) {
                z = z3;
                break;
            } else if (type == 3 && parser.getDepth() <= depth2) {
                z = z3;
                break;
            } else if (type == 2) {
                if (parser.getName().equals("intent-filter")) {
                    depth = depth2;
                    permission = permission2;
                    z2 = z3;
                    ParseResult intentResult = parseIntentFilter(pkg, activity, !isReceiver, visibleToEphemeral, resources, parser, input);
                    if (intentResult.isSuccess() && (intent2 = intentResult.getResult()) != null) {
                        activity.order = Math.max(intent2.getOrder(), activity.order);
                        activity.addIntent(intent2);
                    }
                    result = intentResult;
                } else {
                    depth = depth2;
                    permission = permission2;
                    z2 = z3;
                    if (parser.getName().equals("meta-data")) {
                        result = ParsedComponentUtils.addMetaData(activity, pkg, resources, parser, input);
                    } else if (parser.getName().equals("property")) {
                        result = ParsedComponentUtils.addProperty(activity, pkg, resources, parser, input);
                    } else if (!isReceiver && !isAlias && parser.getName().equals("preferred")) {
                        ParseResult intentResult2 = parseIntentFilter(pkg, activity, true, visibleToEphemeral, resources, parser, input);
                        if (intentResult2.isSuccess() && (intent = intentResult2.getResult()) != null) {
                            pkg.mo877addPreferredActivityFilter(activity.getClassName(), intent);
                        }
                        result = intentResult2;
                    } else if (!isReceiver && !isAlias && parser.getName().equals(TtmlUtils.TAG_LAYOUT)) {
                        ParseResult layoutResult = parseActivityWindowLayout(resources, parser, input);
                        if (layoutResult.isSuccess()) {
                            activity.windowLayout = layoutResult.getResult();
                        }
                        result = layoutResult;
                    } else {
                        result = ParsingUtils.unknownTag(tag, pkg, parser, input);
                    }
                }
                if (result.isError()) {
                    return input.error(result);
                }
                depth2 = depth;
                permission2 = permission;
                z3 = z2;
            }
        }
    }

    private static ParseResult<ParsedIntentInfo> parseIntentFilter(ParsingPackage pkg, ParsedActivity activity, boolean allowImplicitEphemeralVisibility, boolean visibleToEphemeral, Resources resources, XmlResourceParser parser, ParseInput input) throws IOException, XmlPullParserException {
        ParseResult<ParsedIntentInfo> result = ParsedMainComponentUtils.parseIntentFilter(activity, pkg, resources, parser, visibleToEphemeral, true, true, allowImplicitEphemeralVisibility, true, input);
        if (result.isError()) {
            return input.error(result);
        }
        ParsedIntentInfo intent = result.getResult();
        if (intent != null) {
            if (intent.isVisibleToInstantApp()) {
                activity.flags |= 1048576;
            }
            if (intent.isImplicitlyVisibleToInstantApp()) {
                activity.flags |= 2097152;
            }
        }
        return input.success(intent);
    }

    private static int getActivityResizeMode(ParsingPackage pkg, TypedArray sa, int screenOrientation) {
        Boolean resizeableActivity = pkg.getResizeableActivity();
        boolean z = true;
        if (sa.hasValue(40) || resizeableActivity != null) {
            if (resizeableActivity == null || !resizeableActivity.booleanValue()) {
                z = false;
            }
            if (!sa.getBoolean(40, z)) {
                return 0;
            }
            return 2;
        } else if (pkg.isResizeableActivityViaSdkVersion()) {
            return 1;
        } else {
            if (ActivityInfo.isFixedOrientationPortrait(screenOrientation)) {
                return 6;
            }
            if (ActivityInfo.isFixedOrientationLandscape(screenOrientation)) {
                return 5;
            }
            if (screenOrientation == 14) {
                return 7;
            }
            return 4;
        }
    }

    private static ParseResult<ActivityInfo.WindowLayout> parseActivityWindowLayout(Resources res, AttributeSet attrs, ParseInput input) {
        TypedArray sw = res.obtainAttributes(attrs, R.styleable.AndroidManifestLayout);
        int width = -1;
        float widthFraction = -1.0f;
        int height = -1;
        float heightFraction = -1.0f;
        try {
            int widthType = sw.getType(3);
            if (widthType == 6) {
                widthFraction = sw.getFraction(3, 1, 1, -1.0f);
            } else if (widthType == 5) {
                width = sw.getDimensionPixelSize(3, -1);
            }
            int heightType = sw.getType(4);
            if (heightType == 6) {
                heightFraction = sw.getFraction(4, 1, 1, -1.0f);
            } else if (heightType == 5) {
                height = sw.getDimensionPixelSize(4, -1);
            }
            int gravity = sw.getInt(0, 17);
            int minWidth = sw.getDimensionPixelSize(1, -1);
            int minHeight = sw.getDimensionPixelSize(2, -1);
            String windowLayoutAffinity = sw.getNonConfigurationString(5, 0);
            ActivityInfo.WindowLayout windowLayout = new ActivityInfo.WindowLayout(width, widthFraction, height, heightFraction, gravity, minWidth, minHeight, windowLayoutAffinity);
            try {
                ParseResult<ActivityInfo.WindowLayout> success = input.success(windowLayout);
                sw.recycle();
                return success;
            } catch (Throwable th) {
                th = th;
                sw.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static ParseResult<ActivityInfo.WindowLayout> resolveActivityWindowLayout(ParsedActivity activity, ParseInput input) {
        if (activity.metaData == null || !activity.metaData.containsKey("android.activity_window_layout_affinity")) {
            return input.success(activity.windowLayout);
        }
        if (activity.windowLayout != null && activity.windowLayout.windowLayoutAffinity != null) {
            return input.success(activity.windowLayout);
        }
        String windowLayoutAffinity = activity.metaData.getString("android.activity_window_layout_affinity");
        ActivityInfo.WindowLayout layout = activity.windowLayout;
        if (layout == null) {
            layout = new ActivityInfo.WindowLayout(-1, -1.0f, -1, -1.0f, 0, -1, -1, windowLayoutAffinity);
        } else {
            layout.windowLayoutAffinity = windowLayoutAffinity;
        }
        return input.success(layout);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int getActivityConfigChanges(int configChanges, int recreateOnConfigChanges) {
        return ((~recreateOnConfigChanges) & 3) | configChanges;
    }
}
