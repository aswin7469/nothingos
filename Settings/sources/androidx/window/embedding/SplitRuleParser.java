package androidx.window.embedding;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import androidx.window.C0447R;
import androidx.window.core.ExperimentalWindowApi;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExperimentalWindowApi
/* compiled from: SplitRuleParser.kt */
public final class SplitRuleParser {
    @Nullable
    public final Set<EmbeddingRule> parseSplitRules$window_release(@NotNull Context context, int i) {
        Intrinsics.checkNotNullParameter(context, "context");
        return parseSplitXml(context, i);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0063, code lost:
        r6 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0079, code lost:
        r4 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00b0, code lost:
        r5 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.util.Set<androidx.window.embedding.EmbeddingRule> parseSplitXml(android.content.Context r9, int r10) {
        /*
            r8 = this;
            android.content.res.Resources r0 = r9.getResources()
            r1 = 0
            android.content.res.XmlResourceParser r10 = r0.getXml(r10)     // Catch:{ NotFoundException -> 0x00f4 }
            java.lang.String r0 = "resources.getXml(splitResourceId)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r10, r0)     // Catch:{ NotFoundException -> 0x00f4 }
            java.util.HashSet r0 = new java.util.HashSet
            r0.<init>()
            int r2 = r10.getDepth()
            int r3 = r10.next()
            r4 = r1
            r5 = r4
            r6 = r5
        L_0x001f:
            r7 = 1
            if (r3 == r7) goto L_0x00f3
            r7 = 3
            if (r3 != r7) goto L_0x002b
            int r3 = r10.getDepth()
            if (r3 <= r2) goto L_0x00f3
        L_0x002b:
            int r3 = r10.getEventType()
            r7 = 2
            if (r3 != r7) goto L_0x00ed
            java.lang.String r3 = r10.getName()
            java.lang.String r7 = "split-config"
            boolean r3 = kotlin.jvm.internal.Intrinsics.areEqual(r7, r3)
            if (r3 == 0) goto L_0x0041
            goto L_0x00ed
        L_0x0041:
            java.lang.String r3 = r10.getName()
            if (r3 == 0) goto L_0x00e7
            int r7 = r3.hashCode()
            switch(r7) {
                case 511422343: goto L_0x00b2;
                case 520447504: goto L_0x009e;
                case 1579230604: goto L_0x007c;
                case 1793077963: goto L_0x0066;
                case 2050988213: goto L_0x0050;
                default: goto L_0x004e;
            }
        L_0x004e:
            goto L_0x00e7
        L_0x0050:
            java.lang.String r7 = "SplitPlaceholderRule"
            boolean r3 = r3.equals(r7)
            if (r3 != 0) goto L_0x005a
            goto L_0x00e7
        L_0x005a:
            androidx.window.embedding.SplitPlaceholderRule r3 = r8.parseSplitPlaceholderRule(r9, r10)
            r0.add(r3)
            r4 = r1
            r5 = r4
        L_0x0063:
            r6 = r3
            goto L_0x00e7
        L_0x0066:
            java.lang.String r7 = "ActivityRule"
            boolean r3 = r3.equals(r7)
            if (r3 != 0) goto L_0x0070
            goto L_0x00e7
        L_0x0070:
            androidx.window.embedding.ActivityRule r3 = r8.parseSplitActivityRule(r9, r10)
            r0.add(r3)
            r5 = r1
            r6 = r5
        L_0x0079:
            r4 = r3
            goto L_0x00e7
        L_0x007c:
            java.lang.String r7 = "SplitPairFilter"
            boolean r3 = r3.equals(r7)
            if (r3 != 0) goto L_0x0085
            goto L_0x00e7
        L_0x0085:
            if (r5 == 0) goto L_0x0096
            androidx.window.embedding.SplitPairFilter r3 = r8.parseSplitPairFilter(r9, r10)
            r0.remove(r5)
            androidx.window.embedding.SplitPairRule r3 = r5.plus$window_release(r3)
            r0.add(r3)
            goto L_0x00b0
        L_0x0096:
            java.lang.IllegalArgumentException r8 = new java.lang.IllegalArgumentException
            java.lang.String r9 = "Found orphaned SplitPairFilter outside of SplitPairRule"
            r8.<init>(r9)
            throw r8
        L_0x009e:
            java.lang.String r7 = "SplitPairRule"
            boolean r3 = r3.equals(r7)
            if (r3 != 0) goto L_0x00a7
            goto L_0x00e7
        L_0x00a7:
            androidx.window.embedding.SplitPairRule r3 = r8.parseSplitPairRule(r9, r10)
            r0.add(r3)
            r4 = r1
            r6 = r4
        L_0x00b0:
            r5 = r3
            goto L_0x00e7
        L_0x00b2:
            java.lang.String r7 = "ActivityFilter"
            boolean r3 = r3.equals(r7)
            if (r3 != 0) goto L_0x00bb
            goto L_0x00e7
        L_0x00bb:
            if (r4 != 0) goto L_0x00c8
            if (r6 == 0) goto L_0x00c0
            goto L_0x00c8
        L_0x00c0:
            java.lang.IllegalArgumentException r8 = new java.lang.IllegalArgumentException
            java.lang.String r9 = "Found orphaned ActivityFilter"
            r8.<init>(r9)
            throw r8
        L_0x00c8:
            androidx.window.embedding.ActivityFilter r3 = r8.parseActivityFilter(r9, r10)
            if (r4 == 0) goto L_0x00d9
            r0.remove(r4)
            androidx.window.embedding.ActivityRule r3 = r4.plus$window_release(r3)
            r0.add(r3)
            goto L_0x0079
        L_0x00d9:
            if (r6 == 0) goto L_0x00e7
            r0.remove(r6)
            androidx.window.embedding.SplitPlaceholderRule r3 = r6.plus$window_release(r3)
            r0.add(r3)
            goto L_0x0063
        L_0x00e7:
            int r3 = r10.next()
            goto L_0x001f
        L_0x00ed:
            int r3 = r10.next()
            goto L_0x001f
        L_0x00f3:
            return r0
        L_0x00f4:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.embedding.SplitRuleParser.parseSplitXml(android.content.Context, int):java.util.Set");
    }

    private final SplitPairRule parseSplitPairRule(Context context, XmlResourceParser xmlResourceParser) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(xmlResourceParser, C0447R.styleable.SplitPairRule, 0, 0);
        float f = obtainStyledAttributes.getFloat(C0447R.styleable.SplitPairRule_splitRatio, 0.0f);
        int dimension = (int) obtainStyledAttributes.getDimension(C0447R.styleable.SplitPairRule_splitMinWidth, 0.0f);
        int dimension2 = (int) obtainStyledAttributes.getDimension(C0447R.styleable.SplitPairRule_splitMinSmallestWidth, 0.0f);
        int i = obtainStyledAttributes.getInt(C0447R.styleable.SplitPairRule_splitLayoutDirection, 3);
        return new SplitPairRule(SetsKt__SetsKt.emptySet(), obtainStyledAttributes.getInt(C0447R.styleable.SplitPairRule_finishPrimaryWithSecondary, 0), obtainStyledAttributes.getInt(C0447R.styleable.SplitPairRule_finishSecondaryWithPrimary, 1), obtainStyledAttributes.getBoolean(C0447R.styleable.SplitPairRule_clearTop, false), dimension, dimension2, f, i);
    }

    private final SplitPlaceholderRule parseSplitPlaceholderRule(Context context, XmlResourceParser xmlResourceParser) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(xmlResourceParser, C0447R.styleable.SplitPlaceholderRule, 0, 0);
        String string = obtainStyledAttributes.getString(C0447R.styleable.SplitPlaceholderRule_placeholderActivityName);
        boolean z = obtainStyledAttributes.getBoolean(C0447R.styleable.SplitPlaceholderRule_stickyPlaceholder, false);
        int i = obtainStyledAttributes.getInt(C0447R.styleable.SplitPlaceholderRule_finishPrimaryWithSecondary, 1);
        float f = obtainStyledAttributes.getFloat(C0447R.styleable.SplitPlaceholderRule_splitRatio, 0.0f);
        int dimension = (int) obtainStyledAttributes.getDimension(C0447R.styleable.SplitPlaceholderRule_splitMinWidth, 0.0f);
        int dimension2 = (int) obtainStyledAttributes.getDimension(C0447R.styleable.SplitPlaceholderRule_splitMinSmallestWidth, 0.0f);
        int i2 = obtainStyledAttributes.getInt(C0447R.styleable.SplitPlaceholderRule_splitLayoutDirection, 3);
        String packageName = context.getApplicationContext().getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "packageName");
        ComponentName buildClassName = buildClassName(packageName, string);
        Set emptySet = SetsKt__SetsKt.emptySet();
        Intent component = new Intent().setComponent(buildClassName);
        Intrinsics.checkNotNullExpressionValue(component, "Intent().setComponent(pl…eholderActivityClassName)");
        return new SplitPlaceholderRule(emptySet, component, z, i, dimension, dimension2, f, i2);
    }

    private final SplitPairFilter parseSplitPairFilter(Context context, XmlResourceParser xmlResourceParser) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(xmlResourceParser, C0447R.styleable.SplitPairFilter, 0, 0);
        String string = obtainStyledAttributes.getString(C0447R.styleable.SplitPairFilter_primaryActivityName);
        String string2 = obtainStyledAttributes.getString(C0447R.styleable.SplitPairFilter_secondaryActivityName);
        String string3 = obtainStyledAttributes.getString(C0447R.styleable.SplitPairFilter_secondaryActivityAction);
        String packageName = context.getApplicationContext().getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "packageName");
        return new SplitPairFilter(buildClassName(packageName, string), buildClassName(packageName, string2), string3);
    }

    private final ActivityRule parseSplitActivityRule(Context context, XmlResourceParser xmlResourceParser) {
        return new ActivityRule(SetsKt__SetsKt.emptySet(), context.getTheme().obtainStyledAttributes(xmlResourceParser, C0447R.styleable.ActivityRule, 0, 0).getBoolean(C0447R.styleable.ActivityRule_alwaysExpand, false));
    }

    private final ActivityFilter parseActivityFilter(Context context, XmlResourceParser xmlResourceParser) {
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(xmlResourceParser, C0447R.styleable.ActivityFilter, 0, 0);
        String string = obtainStyledAttributes.getString(C0447R.styleable.ActivityFilter_activityName);
        String string2 = obtainStyledAttributes.getString(C0447R.styleable.ActivityFilter_activityAction);
        String packageName = context.getApplicationContext().getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "packageName");
        return new ActivityFilter(buildClassName(packageName, string), string2);
    }

    private final ComponentName buildClassName(String str, CharSequence charSequence) {
        if (charSequence != null) {
            if (!(charSequence.length() == 0)) {
                String obj = charSequence.toString();
                if (obj.charAt(0) == '.') {
                    return new ComponentName(str, str + obj);
                }
                int indexOf$default = StringsKt__StringsKt.indexOf$default((CharSequence) obj, '/', 0, false, 6, (Object) null);
                if (indexOf$default > 0) {
                    str = obj.substring(0, indexOf$default);
                    Intrinsics.checkNotNullExpressionValue(str, "this as java.lang.String…ing(startIndex, endIndex)");
                    obj = obj.substring(indexOf$default + 1);
                    Intrinsics.checkNotNullExpressionValue(obj, "this as java.lang.String).substring(startIndex)");
                }
                if (Intrinsics.areEqual(obj, "*") || StringsKt__StringsKt.indexOf$default((CharSequence) obj, '.', 0, false, 6, (Object) null) >= 0) {
                    return new ComponentName(str, obj);
                }
                return new ComponentName(str, str + '.' + obj);
            }
        }
        throw new IllegalArgumentException("Activity name must not be null");
    }
}
