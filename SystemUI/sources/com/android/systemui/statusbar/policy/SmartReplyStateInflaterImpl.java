package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B7\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u000e\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012J\u001c\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00150\u00142\f\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00150\u0014H\u0002J\u0010\u0010\u0017\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0016J2\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001b2\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u001d\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u001e\u001a\u00020\u0010H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/SmartReplyStateInflaterImpl;", "Lcom/android/systemui/statusbar/policy/SmartReplyStateInflater;", "constants", "Lcom/android/systemui/statusbar/policy/SmartReplyConstants;", "activityManagerWrapper", "Lcom/android/systemui/shared/system/ActivityManagerWrapper;", "packageManagerWrapper", "Lcom/android/systemui/shared/system/PackageManagerWrapper;", "devicePolicyManagerWrapper", "Lcom/android/systemui/shared/system/DevicePolicyManagerWrapper;", "smartRepliesInflater", "Lcom/android/systemui/statusbar/policy/SmartReplyInflater;", "smartActionsInflater", "Lcom/android/systemui/statusbar/policy/SmartActionInflater;", "(Lcom/android/systemui/statusbar/policy/SmartReplyConstants;Lcom/android/systemui/shared/system/ActivityManagerWrapper;Lcom/android/systemui/shared/system/PackageManagerWrapper;Lcom/android/systemui/shared/system/DevicePolicyManagerWrapper;Lcom/android/systemui/statusbar/policy/SmartReplyInflater;Lcom/android/systemui/statusbar/policy/SmartActionInflater;)V", "chooseSmartRepliesAndActions", "Lcom/android/systemui/statusbar/policy/InflatedSmartReplyState;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "filterAllowlistedLockTaskApps", "", "Landroid/app/Notification$Action;", "actions", "inflateSmartReplyState", "inflateSmartReplyViewHolder", "Lcom/android/systemui/statusbar/policy/InflatedSmartReplyViewHolder;", "sysuiContext", "Landroid/content/Context;", "notifPackageContext", "existingSmartReplyState", "newSmartReplyState", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyStateInflaterImpl implements SmartReplyStateInflater {
    private final ActivityManagerWrapper activityManagerWrapper;
    private final SmartReplyConstants constants;
    private final DevicePolicyManagerWrapper devicePolicyManagerWrapper;
    private final PackageManagerWrapper packageManagerWrapper;
    /* access modifiers changed from: private */
    public final SmartActionInflater smartActionsInflater;
    /* access modifiers changed from: private */
    public final SmartReplyInflater smartRepliesInflater;

    @Inject
    public SmartReplyStateInflaterImpl(SmartReplyConstants smartReplyConstants, ActivityManagerWrapper activityManagerWrapper2, PackageManagerWrapper packageManagerWrapper2, DevicePolicyManagerWrapper devicePolicyManagerWrapper2, SmartReplyInflater smartReplyInflater, SmartActionInflater smartActionInflater) {
        Intrinsics.checkNotNullParameter(smartReplyConstants, "constants");
        Intrinsics.checkNotNullParameter(activityManagerWrapper2, "activityManagerWrapper");
        Intrinsics.checkNotNullParameter(packageManagerWrapper2, "packageManagerWrapper");
        Intrinsics.checkNotNullParameter(devicePolicyManagerWrapper2, "devicePolicyManagerWrapper");
        Intrinsics.checkNotNullParameter(smartReplyInflater, "smartRepliesInflater");
        Intrinsics.checkNotNullParameter(smartActionInflater, "smartActionsInflater");
        this.constants = smartReplyConstants;
        this.activityManagerWrapper = activityManagerWrapper2;
        this.packageManagerWrapper = packageManagerWrapper2;
        this.devicePolicyManagerWrapper = devicePolicyManagerWrapper2;
        this.smartRepliesInflater = smartReplyInflater;
        this.smartActionsInflater = smartActionInflater;
    }

    public InflatedSmartReplyState inflateSmartReplyState(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        return chooseSmartRepliesAndActions(notificationEntry);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x005a, code lost:
        if (r0 == null) goto L_0x005c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0095, code lost:
        if (r9 == null) goto L_0x0097;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder inflateSmartReplyViewHolder(android.content.Context r10, android.content.Context r11, com.android.systemui.statusbar.notification.collection.NotificationEntry r12, com.android.systemui.statusbar.policy.InflatedSmartReplyState r13, com.android.systemui.statusbar.policy.InflatedSmartReplyState r14) {
        /*
            r9 = this;
            java.lang.String r0 = "sysuiContext"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
            java.lang.String r0 = "notifPackageContext"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r11, r0)
            java.lang.String r0 = "entry"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            java.lang.String r0 = "newSmartReplyState"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            boolean r0 = com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt.shouldShowSmartReplyView(r12, r14)
            if (r0 != 0) goto L_0x0022
            com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder r9 = new com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder
            r10 = 0
            r9.<init>(r10, r10)
            return r9
        L_0x0022:
            boolean r13 = com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt.areSuggestionsSimilar(r13, r14)
            r13 = r13 ^ 1
            com.android.systemui.statusbar.policy.SmartReplyConstants r0 = r9.constants
            com.android.systemui.statusbar.policy.SmartReplyView r7 = com.android.systemui.statusbar.policy.SmartReplyView.inflate(r10, r0)
            com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies r4 = r14.getSmartReplies()
            if (r4 == 0) goto L_0x0037
            boolean r0 = r4.fromAssistant
            goto L_0x0038
        L_0x0037:
            r0 = 0
        L_0x0038:
            r7.setSmartRepliesGeneratedByAssistant(r0)
            if (r4 == 0) goto L_0x005c
            java.util.List<java.lang.CharSequence> r0 = r4.choices
            java.lang.String r1 = "smartReplies.choices"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r1)
            java.lang.Iterable r0 = (java.lang.Iterable) r0
            kotlin.sequences.Sequence r6 = kotlin.collections.CollectionsKt.asSequence(r0)
            com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1 r8 = new com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1
            r0 = r8
            r1 = r9
            r2 = r7
            r3 = r12
            r5 = r13
            r0.<init>(r1, r2, r3, r4, r5)
            kotlin.jvm.functions.Function2 r8 = (kotlin.jvm.functions.Function2) r8
            kotlin.sequences.Sequence r0 = kotlin.sequences.SequencesKt.mapIndexed(r6, r8)
            if (r0 != 0) goto L_0x0060
        L_0x005c:
            kotlin.sequences.Sequence r0 = kotlin.sequences.SequencesKt.emptySequence()
        L_0x0060:
            r8 = r0
            com.android.systemui.statusbar.policy.SmartReplyView$SmartActions r4 = r14.getSmartActions()
            if (r4 == 0) goto L_0x0097
            android.view.ContextThemeWrapper r6 = new android.view.ContextThemeWrapper
            android.content.res.Resources$Theme r10 = r10.getTheme()
            r6.<init>(r11, r10)
            java.util.List<android.app.Notification$Action> r10 = r4.actions
            java.lang.String r11 = "smartActions.actions"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r10, r11)
            java.lang.Iterable r10 = (java.lang.Iterable) r10
            kotlin.sequences.Sequence r10 = kotlin.collections.CollectionsKt.asSequence(r10)
            com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 r11 = com.android.systemui.statusbar.policy.C3186xa9afa0f0.INSTANCE
            kotlin.jvm.functions.Function1 r11 = (kotlin.jvm.functions.Function1) r11
            kotlin.sequences.Sequence r10 = kotlin.sequences.SequencesKt.filter(r10, r11)
            com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2 r11 = new com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2
            r0 = r11
            r1 = r9
            r2 = r7
            r3 = r12
            r5 = r13
            r0.<init>(r1, r2, r3, r4, r5, r6)
            kotlin.jvm.functions.Function2 r11 = (kotlin.jvm.functions.Function2) r11
            kotlin.sequences.Sequence r9 = kotlin.sequences.SequencesKt.mapIndexed(r10, r11)
            if (r9 != 0) goto L_0x009b
        L_0x0097:
            kotlin.sequences.Sequence r9 = kotlin.sequences.SequencesKt.emptySequence()
        L_0x009b:
            com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder r10 = new com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder
            kotlin.sequences.Sequence r9 = kotlin.sequences.SequencesKt.plus(r8, r9)
            java.util.List r9 = kotlin.sequences.SequencesKt.toList(r9)
            r10.<init>(r7, r9)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl.inflateSmartReplyViewHolder(android.content.Context, android.content.Context, com.android.systemui.statusbar.notification.collection.NotificationEntry, com.android.systemui.statusbar.policy.InflatedSmartReplyState, com.android.systemui.statusbar.policy.InflatedSmartReplyState):com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder");
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x01a1 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x008c  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b9  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00bf  */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x011b  */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0144  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x0162  */
    /* JADX WARNING: Removed duplicated region for block: B:86:0x0196  */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x019b  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x019e  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x015f A[EDGE_INSN: B:94:0x015f->B:71:0x015f ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.android.systemui.statusbar.policy.InflatedSmartReplyState chooseSmartRepliesAndActions(com.android.systemui.statusbar.notification.collection.NotificationEntry r13) {
        /*
            r12 = this;
            java.lang.String r0 = "entry"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            android.service.notification.StatusBarNotification r0 = r13.getSbn()
            android.app.Notification r0 = r0.getNotification()
            r1 = 0
            android.util.Pair r2 = r0.findRemoteInputActionPair(r1)
            r3 = 1
            android.util.Pair r4 = r0.findRemoteInputActionPair(r3)
            com.android.systemui.statusbar.policy.SmartReplyConstants r5 = r12.constants
            boolean r5 = r5.isEnabled()
            r6 = 0
            if (r5 != 0) goto L_0x0048
            boolean r12 = com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt.DEBUG
            if (r12 == 0) goto L_0x0042
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r0 = "Smart suggestions not enabled, not adding suggestions for "
            r12.<init>((java.lang.String) r0)
            android.service.notification.StatusBarNotification r13 = r13.getSbn()
            java.lang.String r13 = r13.getKey()
            java.lang.StringBuilder r12 = r12.append((java.lang.String) r13)
            java.lang.String r12 = r12.toString()
            java.lang.String r13 = "SmartReplyViewInflater"
            android.util.Log.d(r13, r12)
        L_0x0042:
            com.android.systemui.statusbar.policy.InflatedSmartReplyState r12 = new com.android.systemui.statusbar.policy.InflatedSmartReplyState
            r12.<init>(r6, r6, r6, r1)
            return r12
        L_0x0048:
            com.android.systemui.statusbar.policy.SmartReplyConstants r5 = r12.constants
            boolean r5 = r5.requiresTargetingP()
            if (r5 == 0) goto L_0x0059
            int r5 = r13.targetSdk
            r7 = 28
            if (r5 < r7) goto L_0x0057
            goto L_0x0059
        L_0x0057:
            r5 = r1
            goto L_0x005a
        L_0x0059:
            r5 = r3
        L_0x005a:
            java.util.List r7 = r0.getContextualActions()
            if (r5 == 0) goto L_0x00a9
            if (r2 == 0) goto L_0x00a9
            java.lang.Object r5 = r2.second
            android.app.Notification$Action r5 = (android.app.Notification.Action) r5
            android.app.PendingIntent r5 = r5.actionIntent
            if (r5 == 0) goto L_0x00a9
            java.lang.String r8 = "actionIntent"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r8)
            java.lang.Object r8 = r2.first
            android.app.RemoteInput r8 = (android.app.RemoteInput) r8
            java.lang.CharSequence[] r8 = r8.getChoices()
            if (r8 == 0) goto L_0x0089
            java.lang.String r9 = "choices"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r8, r9)
            int r8 = r8.length
            if (r8 != 0) goto L_0x0083
            r8 = r3
            goto L_0x0084
        L_0x0083:
            r8 = r1
        L_0x0084:
            r8 = r8 ^ r3
            if (r8 != r3) goto L_0x0089
            r8 = r3
            goto L_0x008a
        L_0x0089:
            r8 = r1
        L_0x008a:
            if (r8 == 0) goto L_0x00a9
            com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies r8 = new com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies
            java.lang.Object r9 = r2.first
            android.app.RemoteInput r9 = (android.app.RemoteInput) r9
            java.lang.CharSequence[] r9 = r9.getChoices()
            java.lang.String r10 = "pair.first.choices"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r9, r10)
            java.lang.Object[] r9 = (java.lang.Object[]) r9
            java.util.List r9 = kotlin.collections.ArraysKt.asList((T[]) r9)
            java.lang.Object r2 = r2.first
            android.app.RemoteInput r2 = (android.app.RemoteInput) r2
            r8.<init>(r9, r2, r5, r1)
            goto L_0x00aa
        L_0x00a9:
            r8 = r6
        L_0x00aa:
            java.lang.String r2 = "appGeneratedSmartActions"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r7, r2)
            r2 = r7
            java.util.Collection r2 = (java.util.Collection) r2
            boolean r2 = r2.isEmpty()
            r2 = r2 ^ r3
            if (r2 == 0) goto L_0x00bf
            com.android.systemui.statusbar.policy.SmartReplyView$SmartActions r2 = new com.android.systemui.statusbar.policy.SmartReplyView$SmartActions
            r2.<init>(r7, r1)
            goto L_0x00c0
        L_0x00bf:
            r2 = r6
        L_0x00c0:
            if (r8 != 0) goto L_0x0124
            if (r2 != 0) goto L_0x0124
            java.util.List r5 = r13.getSmartReplies()
            java.lang.String r7 = "entry.smartReplies"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r7)
            java.util.List r13 = r13.getSmartActions()
            java.lang.String r7 = "entry.smartActions"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r13, r7)
            r7 = r5
            java.util.Collection r7 = (java.util.Collection) r7
            boolean r7 = r7.isEmpty()
            r7 = r7 ^ r3
            if (r7 == 0) goto L_0x0103
            if (r4 == 0) goto L_0x0103
            java.lang.Object r7 = r4.second
            android.app.Notification$Action r7 = (android.app.Notification.Action) r7
            boolean r7 = r7.getAllowGeneratedReplies()
            if (r7 == 0) goto L_0x0103
            java.lang.Object r7 = r4.second
            android.app.Notification$Action r7 = (android.app.Notification.Action) r7
            android.app.PendingIntent r7 = r7.actionIntent
            if (r7 == 0) goto L_0x0103
            com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies r8 = new com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies
            java.lang.Object r7 = r4.first
            android.app.RemoteInput r7 = (android.app.RemoteInput) r7
            java.lang.Object r4 = r4.second
            android.app.Notification$Action r4 = (android.app.Notification.Action) r4
            android.app.PendingIntent r4 = r4.actionIntent
            r8.<init>(r5, r7, r4, r3)
        L_0x0103:
            r4 = r13
            java.util.Collection r4 = (java.util.Collection) r4
            boolean r4 = r4.isEmpty()
            r4 = r4 ^ r3
            if (r4 == 0) goto L_0x0124
            boolean r4 = r0.getAllowSystemGeneratedContextualActions()
            if (r4 == 0) goto L_0x0124
            com.android.systemui.shared.system.ActivityManagerWrapper r2 = r12.activityManagerWrapper
            boolean r2 = r2.isLockTaskKioskModeActive()
            if (r2 == 0) goto L_0x011f
            java.util.List r13 = r12.filterAllowlistedLockTaskApps(r13)
        L_0x011f:
            com.android.systemui.statusbar.policy.SmartReplyView$SmartActions r2 = new com.android.systemui.statusbar.policy.SmartReplyView$SmartActions
            r2.<init>(r13, r3)
        L_0x0124:
            if (r2 == 0) goto L_0x015f
            java.util.List<android.app.Notification$Action> r12 = r2.actions
            if (r12 == 0) goto L_0x015f
            java.lang.Iterable r12 = (java.lang.Iterable) r12
            boolean r13 = r12 instanceof java.util.Collection
            if (r13 == 0) goto L_0x013a
            r13 = r12
            java.util.Collection r13 = (java.util.Collection) r13
            boolean r13 = r13.isEmpty()
            if (r13 == 0) goto L_0x013a
            goto L_0x015f
        L_0x013a:
            java.util.Iterator r12 = r12.iterator()
        L_0x013e:
            boolean r13 = r12.hasNext()
            if (r13 == 0) goto L_0x015f
            java.lang.Object r13 = r12.next()
            android.app.Notification$Action r13 = (android.app.Notification.Action) r13
            boolean r4 = r13.isContextual()
            if (r4 == 0) goto L_0x015a
            int r13 = r13.getSemanticAction()
            r4 = 12
            if (r13 != r4) goto L_0x015a
            r13 = r3
            goto L_0x015b
        L_0x015a:
            r13 = r1
        L_0x015b:
            if (r13 == 0) goto L_0x013e
            r12 = r3
            goto L_0x0160
        L_0x015f:
            r12 = r1
        L_0x0160:
            if (r12 == 0) goto L_0x01ac
            android.app.Notification$Action[] r13 = r0.actions
            java.lang.String r0 = "notification.actions"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r13, r0)
            java.lang.Object[] r13 = (java.lang.Object[]) r13
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.Collection r0 = (java.util.Collection) r0
            int r4 = r13.length
            r5 = r1
            r7 = r5
        L_0x0175:
            if (r5 >= r4) goto L_0x01a5
            r9 = r13[r5]
            int r10 = r7 + 1
            android.app.Notification$Action r9 = (android.app.Notification.Action) r9
            android.app.RemoteInput[] r9 = r9.getRemoteInputs()
            if (r9 == 0) goto L_0x0193
            java.lang.String r11 = "remoteInputs"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r9, r11)
            int r9 = r9.length
            if (r9 != 0) goto L_0x018d
            r9 = r3
            goto L_0x018e
        L_0x018d:
            r9 = r1
        L_0x018e:
            r9 = r9 ^ r3
            if (r9 != r3) goto L_0x0193
            r9 = r3
            goto L_0x0194
        L_0x0193:
            r9 = r1
        L_0x0194:
            if (r9 == 0) goto L_0x019b
            java.lang.Integer r7 = java.lang.Integer.valueOf((int) r7)
            goto L_0x019c
        L_0x019b:
            r7 = r6
        L_0x019c:
            if (r7 == 0) goto L_0x01a1
            r0.add(r7)
        L_0x01a1:
            int r5 = r5 + 1
            r7 = r10
            goto L_0x0175
        L_0x01a5:
            java.util.List r0 = (java.util.List) r0
            com.android.systemui.statusbar.policy.InflatedSmartReplyState$SuppressedActions r6 = new com.android.systemui.statusbar.policy.InflatedSmartReplyState$SuppressedActions
            r6.<init>(r0)
        L_0x01ac:
            com.android.systemui.statusbar.policy.InflatedSmartReplyState r13 = new com.android.systemui.statusbar.policy.InflatedSmartReplyState
            r13.<init>(r8, r2, r6, r12)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl.chooseSmartRepliesAndActions(com.android.systemui.statusbar.notification.collection.NotificationEntry):com.android.systemui.statusbar.policy.InflatedSmartReplyState");
    }

    private final List<Notification.Action> filterAllowlistedLockTaskApps(List<? extends Notification.Action> list) {
        Intent intent;
        ResolveInfo resolveActivity;
        Collection arrayList = new ArrayList();
        for (Object next : list) {
            PendingIntent pendingIntent = ((Notification.Action) next).actionIntent;
            boolean z = false;
            if (!(pendingIntent == null || (intent = pendingIntent.getIntent()) == null || (resolveActivity = this.packageManagerWrapper.resolveActivity(intent, 0)) == null)) {
                z = this.devicePolicyManagerWrapper.isLockTaskPermitted(resolveActivity.activityInfo.packageName);
            }
            if (z) {
                arrayList.add(next);
            }
        }
        return (List) arrayList;
    }
}
