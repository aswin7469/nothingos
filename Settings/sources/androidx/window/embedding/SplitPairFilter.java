package androidx.window.embedding;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import androidx.window.core.ExperimentalWindowApi;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExperimentalWindowApi
/* compiled from: SplitPairFilter.kt */
public final class SplitPairFilter {
    @NotNull
    private final ComponentName primaryActivityName;
    @Nullable
    private final String secondaryActivityIntentAction;
    @NotNull
    private final ComponentName secondaryActivityName;

    /* JADX WARNING: Removed duplicated region for block: B:13:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0075  */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0128  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SplitPairFilter(@org.jetbrains.annotations.NotNull android.content.ComponentName r13, @org.jetbrains.annotations.NotNull android.content.ComponentName r14, @org.jetbrains.annotations.Nullable java.lang.String r15) {
        /*
            r12 = this;
            java.lang.String r0 = "primaryActivityName"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            java.lang.String r0 = "secondaryActivityName"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            r12.<init>()
            r12.primaryActivityName = r13
            r12.secondaryActivityName = r14
            r12.secondaryActivityIntentAction = r15
            java.lang.String r12 = r13.getPackageName()
            java.lang.String r15 = "primaryActivityName.packageName"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r12, r15)
            java.lang.String r13 = r13.getClassName()
            java.lang.String r15 = "primaryActivityName.className"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r13, r15)
            java.lang.String r15 = r14.getPackageName()
            java.lang.String r0 = "secondaryActivityName.packageName"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r15, r0)
            java.lang.String r14 = r14.getClassName()
            java.lang.String r0 = "secondaryActivityName.className"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r14, r0)
            int r0 = r12.length()
            r7 = 1
            r8 = 0
            if (r0 != 0) goto L_0x0047
            r0 = r7
            goto L_0x0048
        L_0x0047:
            r0 = r8
        L_0x0048:
            if (r0 != 0) goto L_0x0057
            int r0 = r15.length()
            if (r0 != 0) goto L_0x0052
            r0 = r7
            goto L_0x0053
        L_0x0052:
            r0 = r8
        L_0x0053:
            if (r0 != 0) goto L_0x0057
            r0 = r7
            goto L_0x0058
        L_0x0057:
            r0 = r8
        L_0x0058:
            if (r0 == 0) goto L_0x0128
            int r0 = r13.length()
            if (r0 != 0) goto L_0x0062
            r0 = r7
            goto L_0x0063
        L_0x0062:
            r0 = r8
        L_0x0063:
            if (r0 != 0) goto L_0x0072
            int r0 = r14.length()
            if (r0 != 0) goto L_0x006d
            r0 = r7
            goto L_0x006e
        L_0x006d:
            r0 = r8
        L_0x006e:
            if (r0 != 0) goto L_0x0072
            r0 = r7
            goto L_0x0073
        L_0x0072:
            r0 = r8
        L_0x0073:
            if (r0 == 0) goto L_0x011c
            java.lang.String r9 = "*"
            r10 = 2
            r11 = 0
            boolean r0 = kotlin.text.StringsKt__StringsKt.contains$default(r12, r9, r8, r10, r11)
            if (r0 == 0) goto L_0x0094
            r3 = 0
            r4 = 0
            r5 = 6
            r6 = 0
            java.lang.String r2 = "*"
            r1 = r12
            int r0 = kotlin.text.StringsKt__StringsKt.indexOf$default((java.lang.CharSequence) r1, (java.lang.String) r2, (int) r3, (boolean) r4, (int) r5, (java.lang.Object) r6)
            int r12 = r12.length()
            int r12 = r12 - r7
            if (r0 != r12) goto L_0x0092
            goto L_0x0094
        L_0x0092:
            r12 = r8
            goto L_0x0095
        L_0x0094:
            r12 = r7
        L_0x0095:
            java.lang.String r6 = "Wildcard in package name is only allowed at the end."
            if (r12 == 0) goto L_0x0112
            boolean r12 = kotlin.text.StringsKt__StringsKt.contains$default(r13, r9, r8, r10, r11)
            if (r12 == 0) goto L_0x00b4
            r2 = 0
            r3 = 0
            r4 = 6
            r5 = 0
            java.lang.String r1 = "*"
            r0 = r13
            int r12 = kotlin.text.StringsKt__StringsKt.indexOf$default((java.lang.CharSequence) r0, (java.lang.String) r1, (int) r2, (boolean) r3, (int) r4, (java.lang.Object) r5)
            int r13 = r13.length()
            int r13 = r13 - r7
            if (r12 != r13) goto L_0x00b2
            goto L_0x00b4
        L_0x00b2:
            r12 = r8
            goto L_0x00b5
        L_0x00b4:
            r12 = r7
        L_0x00b5:
            java.lang.String r13 = "Wildcard in class name is only allowed at the end."
            if (r12 == 0) goto L_0x0108
            boolean r12 = kotlin.text.StringsKt__StringsKt.contains$default(r15, r9, r8, r10, r11)
            if (r12 == 0) goto L_0x00d4
            r2 = 0
            r3 = 0
            r4 = 6
            r5 = 0
            java.lang.String r1 = "*"
            r0 = r15
            int r12 = kotlin.text.StringsKt__StringsKt.indexOf$default((java.lang.CharSequence) r0, (java.lang.String) r1, (int) r2, (boolean) r3, (int) r4, (java.lang.Object) r5)
            int r15 = r15.length()
            int r15 = r15 - r7
            if (r12 != r15) goto L_0x00d2
            goto L_0x00d4
        L_0x00d2:
            r12 = r8
            goto L_0x00d5
        L_0x00d4:
            r12 = r7
        L_0x00d5:
            if (r12 == 0) goto L_0x00fe
            boolean r12 = kotlin.text.StringsKt__StringsKt.contains$default(r14, r9, r8, r10, r11)
            if (r12 == 0) goto L_0x00f1
            r3 = 0
            r4 = 0
            r5 = 6
            r6 = 0
            java.lang.String r2 = "*"
            r1 = r14
            int r12 = kotlin.text.StringsKt__StringsKt.indexOf$default((java.lang.CharSequence) r1, (java.lang.String) r2, (int) r3, (boolean) r4, (int) r5, (java.lang.Object) r6)
            int r14 = r14.length()
            int r14 = r14 - r7
            if (r12 != r14) goto L_0x00f0
            goto L_0x00f1
        L_0x00f0:
            r7 = r8
        L_0x00f1:
            if (r7 == 0) goto L_0x00f4
            return
        L_0x00f4:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x00fe:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = r6.toString()
            r12.<init>(r13)
            throw r12
        L_0x0108:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x0112:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = r6.toString()
            r12.<init>(r13)
            throw r12
        L_0x011c:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = "Activity class name must not be empty."
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        L_0x0128:
            java.lang.IllegalArgumentException r12 = new java.lang.IllegalArgumentException
            java.lang.String r13 = "Package name must not be empty"
            java.lang.String r13 = r13.toString()
            r12.<init>(r13)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.embedding.SplitPairFilter.<init>(android.content.ComponentName, android.content.ComponentName, java.lang.String):void");
    }

    @NotNull
    public final ComponentName getPrimaryActivityName() {
        return this.primaryActivityName;
    }

    @NotNull
    public final ComponentName getSecondaryActivityName() {
        return this.secondaryActivityName;
    }

    @Nullable
    public final String getSecondaryActivityIntentAction() {
        return this.secondaryActivityIntentAction;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0041, code lost:
        if (matchesActivityIntentPair(r6, r7) != false) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean matchesActivityPair(@org.jetbrains.annotations.NotNull android.app.Activity r6, @org.jetbrains.annotations.NotNull android.app.Activity r7) {
        /*
            r5 = this;
            java.lang.String r0 = "primaryActivity"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            java.lang.String r0 = "secondaryActivity"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            androidx.window.embedding.MatcherUtils r0 = androidx.window.embedding.MatcherUtils.INSTANCE
            android.content.ComponentName r1 = r6.getComponentName()
            android.content.ComponentName r2 = r5.primaryActivityName
            boolean r1 = r0.areComponentsMatching$window_release(r1, r2)
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x002a
            android.content.ComponentName r1 = r7.getComponentName()
            android.content.ComponentName r4 = r5.secondaryActivityName
            boolean r0 = r0.areComponentsMatching$window_release(r1, r4)
            if (r0 == 0) goto L_0x002a
            r0 = r2
            goto L_0x002b
        L_0x002a:
            r0 = r3
        L_0x002b:
            android.content.Intent r1 = r7.getIntent()
            if (r1 == 0) goto L_0x0046
            if (r0 == 0) goto L_0x0044
            android.content.Intent r7 = r7.getIntent()
            java.lang.String r0 = "secondaryActivity.intent"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r7, r0)
            boolean r5 = r5.matchesActivityIntentPair(r6, r7)
            if (r5 == 0) goto L_0x0044
            goto L_0x0045
        L_0x0044:
            r2 = r3
        L_0x0045:
            r0 = r2
        L_0x0046:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.window.embedding.SplitPairFilter.matchesActivityPair(android.app.Activity, android.app.Activity):boolean");
    }

    public final boolean matchesActivityIntentPair(@NotNull Activity activity, @NotNull Intent intent) {
        Intrinsics.checkNotNullParameter(activity, "primaryActivity");
        Intrinsics.checkNotNullParameter(intent, "secondaryActivityIntent");
        ComponentName componentName = activity.getComponentName();
        MatcherUtils matcherUtils = MatcherUtils.INSTANCE;
        if (!matcherUtils.areComponentsMatching$window_release(componentName, this.primaryActivityName) || !matcherUtils.areComponentsMatching$window_release(intent.getComponent(), this.secondaryActivityName)) {
            return false;
        }
        String str = this.secondaryActivityIntentAction;
        if (str == null || Intrinsics.areEqual(str, intent.getAction())) {
            return true;
        }
        return false;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SplitPairFilter)) {
            return false;
        }
        SplitPairFilter splitPairFilter = (SplitPairFilter) obj;
        return Intrinsics.areEqual(this.primaryActivityName, splitPairFilter.primaryActivityName) && Intrinsics.areEqual(this.secondaryActivityName, splitPairFilter.secondaryActivityName) && Intrinsics.areEqual(this.secondaryActivityIntentAction, splitPairFilter.secondaryActivityIntentAction);
    }

    public int hashCode() {
        int hashCode = ((this.primaryActivityName.hashCode() * 31) + this.secondaryActivityName.hashCode()) * 31;
        String str = this.secondaryActivityIntentAction;
        return hashCode + (str != null ? str.hashCode() : 0);
    }

    @NotNull
    public String toString() {
        return "SplitPairFilter{primaryActivityName=" + this.primaryActivityName + ", secondaryActivityName=" + this.secondaryActivityName + ", secondaryActivityAction=" + this.secondaryActivityIntentAction + '}';
    }
}
