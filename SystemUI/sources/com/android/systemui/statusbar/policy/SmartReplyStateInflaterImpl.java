package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.InflatedSmartReplyState;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
public final class SmartReplyStateInflaterImpl implements SmartReplyStateInflater {
    @NotNull
    private final ActivityManagerWrapper activityManagerWrapper;
    @NotNull
    private final SmartReplyConstants constants;
    @NotNull
    private final DevicePolicyManagerWrapper devicePolicyManagerWrapper;
    @NotNull
    private final PackageManagerWrapper packageManagerWrapper;
    @NotNull
    private final SmartActionInflater smartActionsInflater;
    @NotNull
    private final SmartReplyInflater smartRepliesInflater;

    public SmartReplyStateInflaterImpl(@NotNull SmartReplyConstants constants, @NotNull ActivityManagerWrapper activityManagerWrapper, @NotNull PackageManagerWrapper packageManagerWrapper, @NotNull DevicePolicyManagerWrapper devicePolicyManagerWrapper, @NotNull SmartReplyInflater smartRepliesInflater, @NotNull SmartActionInflater smartActionsInflater) {
        Intrinsics.checkNotNullParameter(constants, "constants");
        Intrinsics.checkNotNullParameter(activityManagerWrapper, "activityManagerWrapper");
        Intrinsics.checkNotNullParameter(packageManagerWrapper, "packageManagerWrapper");
        Intrinsics.checkNotNullParameter(devicePolicyManagerWrapper, "devicePolicyManagerWrapper");
        Intrinsics.checkNotNullParameter(smartRepliesInflater, "smartRepliesInflater");
        Intrinsics.checkNotNullParameter(smartActionsInflater, "smartActionsInflater");
        this.constants = constants;
        this.activityManagerWrapper = activityManagerWrapper;
        this.packageManagerWrapper = packageManagerWrapper;
        this.devicePolicyManagerWrapper = devicePolicyManagerWrapper;
        this.smartRepliesInflater = smartRepliesInflater;
        this.smartActionsInflater = smartActionsInflater;
    }

    @Override // com.android.systemui.statusbar.policy.SmartReplyStateInflater
    @NotNull
    public InflatedSmartReplyState inflateSmartReplyState(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        return chooseSmartRepliesAndActions(entry);
    }

    @Override // com.android.systemui.statusbar.policy.SmartReplyStateInflater
    @NotNull
    public InflatedSmartReplyViewHolder inflateSmartReplyViewHolder(@NotNull Context sysuiContext, @NotNull Context notifPackageContext, @NotNull NotificationEntry entry, @Nullable InflatedSmartReplyState inflatedSmartReplyState, @NotNull InflatedSmartReplyState newSmartReplyState) {
        Sequence asSequence;
        Sequence mapIndexed;
        Sequence plus;
        List list;
        Sequence asSequence2;
        Sequence filter;
        Intrinsics.checkNotNullParameter(sysuiContext, "sysuiContext");
        Intrinsics.checkNotNullParameter(notifPackageContext, "notifPackageContext");
        Intrinsics.checkNotNullParameter(entry, "entry");
        Intrinsics.checkNotNullParameter(newSmartReplyState, "newSmartReplyState");
        Sequence sequence = null;
        if (!SmartReplyStateInflaterKt.shouldShowSmartReplyView(entry, newSmartReplyState)) {
            return new InflatedSmartReplyViewHolder(null, null);
        }
        boolean z = !SmartReplyStateInflaterKt.areSuggestionsSimilar(inflatedSmartReplyState, newSmartReplyState);
        SmartReplyView inflate = SmartReplyView.inflate(sysuiContext, this.constants);
        SmartReplyView.SmartReplies smartReplies = newSmartReplyState.getSmartReplies();
        inflate.setSmartRepliesGeneratedByAssistant(smartReplies == null ? false : smartReplies.fromAssistant);
        if (smartReplies == null) {
            mapIndexed = null;
        } else {
            List<CharSequence> list2 = smartReplies.choices;
            Intrinsics.checkNotNullExpressionValue(list2, "smartReplies.choices");
            asSequence = CollectionsKt___CollectionsKt.asSequence(list2);
            mapIndexed = SequencesKt___SequencesKt.mapIndexed(asSequence, new SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartReplyButtons$1$1(this, inflate, entry, smartReplies, z));
        }
        if (mapIndexed == null) {
            mapIndexed = SequencesKt__SequencesKt.emptySequence();
        }
        Sequence sequence2 = mapIndexed;
        SmartReplyView.SmartActions smartActions = newSmartReplyState.getSmartActions();
        if (smartActions != null) {
            ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(notifPackageContext, sysuiContext.getTheme());
            List<Notification.Action> list3 = smartActions.actions;
            Intrinsics.checkNotNullExpressionValue(list3, "smartActions.actions");
            asSequence2 = CollectionsKt___CollectionsKt.asSequence(list3);
            filter = SequencesKt___SequencesKt.filter(asSequence2, SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1.INSTANCE);
            sequence = SequencesKt___SequencesKt.mapIndexed(filter, new SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$2(this, inflate, entry, smartActions, z, contextThemeWrapper));
        }
        if (sequence == null) {
            sequence = SequencesKt__SequencesKt.emptySequence();
        }
        plus = SequencesKt___SequencesKt.plus(sequence2, sequence);
        list = SequencesKt___SequencesKt.toList(plus);
        return new InflatedSmartReplyViewHolder(inflate, list);
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00e3  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0110  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0145  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x00b0  */
    @NotNull
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final InflatedSmartReplyState chooseSmartRepliesAndActions(@NotNull NotificationEntry entry) {
        SmartReplyView.SmartReplies smartReplies;
        SmartReplyView.SmartActions smartActions;
        List<Notification.Action> list;
        boolean z;
        boolean z2;
        List<CharSequence> smartReplies2;
        List<Notification.Action> smartActions2;
        Object obj;
        PendingIntent pendingIntent;
        List asList;
        Intrinsics.checkNotNullParameter(entry, "entry");
        Notification notification = entry.getSbn().getNotification();
        Pair<RemoteInput, Notification.Action> findRemoteInputActionPair = notification.findRemoteInputActionPair(false);
        Pair<RemoteInput, Notification.Action> findRemoteInputActionPair2 = notification.findRemoteInputActionPair(true);
        InflatedSmartReplyState.SuppressedActions suppressedActions = null;
        if (!this.constants.isEnabled()) {
            if (SmartReplyStateInflaterKt.access$getDEBUG$p()) {
                Log.d("SmartReplyViewInflater", Intrinsics.stringPlus("Smart suggestions not enabled, not adding suggestions for ", entry.getSbn().getKey()));
            }
            return new InflatedSmartReplyState(null, null, null, false);
        }
        boolean z3 = !this.constants.requiresTargetingP() || entry.targetSdk >= 28;
        List<Notification.Action> appGeneratedSmartActions = notification.getContextualActions();
        if (z3 && findRemoteInputActionPair != null && (pendingIntent = ((Notification.Action) findRemoteInputActionPair.second).actionIntent) != null) {
            CharSequence[] choices = ((RemoteInput) findRemoteInputActionPair.first).getChoices();
            if (Intrinsics.areEqual(choices == null ? null : Boolean.valueOf(!(choices.length == 0)), Boolean.TRUE)) {
                CharSequence[] choices2 = ((RemoteInput) findRemoteInputActionPair.first).getChoices();
                Intrinsics.checkNotNullExpressionValue(choices2, "pair.first.choices");
                asList = ArraysKt___ArraysJvmKt.asList(choices2);
                smartReplies = new SmartReplyView.SmartReplies(asList, (RemoteInput) findRemoteInputActionPair.first, pendingIntent, false);
                Intrinsics.checkNotNullExpressionValue(appGeneratedSmartActions, "appGeneratedSmartActions");
                smartActions = !(appGeneratedSmartActions.isEmpty() ^ true) ? new SmartReplyView.SmartActions(appGeneratedSmartActions, false) : null;
                if (smartReplies == null && smartActions == null) {
                    smartReplies2 = entry.getSmartReplies();
                    Intrinsics.checkNotNullExpressionValue(smartReplies2, "entry.smartReplies");
                    smartActions2 = entry.getSmartActions();
                    Intrinsics.checkNotNullExpressionValue(smartActions2, "entry.smartActions");
                    if ((!smartReplies2.isEmpty()) && findRemoteInputActionPair2 != null && ((Notification.Action) findRemoteInputActionPair2.second).getAllowGeneratedReplies()) {
                        obj = findRemoteInputActionPair2.second;
                        if (((Notification.Action) obj).actionIntent != null) {
                            smartReplies = new SmartReplyView.SmartReplies(smartReplies2, (RemoteInput) findRemoteInputActionPair2.first, ((Notification.Action) obj).actionIntent, true);
                        }
                    }
                    if ((!smartActions2.isEmpty()) && notification.getAllowSystemGeneratedContextualActions()) {
                        if (this.activityManagerWrapper.isLockTaskKioskModeActive()) {
                            smartActions2 = filterAllowlistedLockTaskApps(smartActions2);
                        }
                        smartActions = new SmartReplyView.SmartActions(smartActions2, true);
                    }
                }
                list = smartActions != null ? null : smartActions.actions;
                if (list != null && !list.isEmpty()) {
                    for (Notification.Action action : list) {
                        if (!action.isContextual() || action.getSemanticAction() != 12) {
                            z = false;
                            continue;
                        } else {
                            z = true;
                            continue;
                        }
                        if (z) {
                            z2 = true;
                            break;
                        }
                    }
                }
                z2 = false;
                if (z2) {
                    Notification.Action[] actionArr = notification.actions;
                    Intrinsics.checkNotNullExpressionValue(actionArr, "notification.actions");
                    ArrayList arrayList = new ArrayList();
                    int length = actionArr.length;
                    int i = 0;
                    int i2 = 0;
                    while (i < length) {
                        int i3 = i2 + 1;
                        RemoteInput[] remoteInputs = actionArr[i].getRemoteInputs();
                        Integer valueOf = Intrinsics.areEqual(remoteInputs == null ? null : Boolean.valueOf(!(remoteInputs.length == 0)), Boolean.TRUE) ? Integer.valueOf(i2) : null;
                        if (valueOf != null) {
                            arrayList.add(valueOf);
                        }
                        i++;
                        i2 = i3;
                    }
                    suppressedActions = new InflatedSmartReplyState.SuppressedActions(arrayList);
                }
                return new InflatedSmartReplyState(smartReplies, smartActions, suppressedActions, z2);
            }
        }
        smartReplies = null;
        Intrinsics.checkNotNullExpressionValue(appGeneratedSmartActions, "appGeneratedSmartActions");
        if (!(appGeneratedSmartActions.isEmpty() ^ true)) {
        }
        if (smartReplies == null) {
            smartReplies2 = entry.getSmartReplies();
            Intrinsics.checkNotNullExpressionValue(smartReplies2, "entry.smartReplies");
            smartActions2 = entry.getSmartActions();
            Intrinsics.checkNotNullExpressionValue(smartActions2, "entry.smartActions");
            if (!smartReplies2.isEmpty()) {
                obj = findRemoteInputActionPair2.second;
                if (((Notification.Action) obj).actionIntent != null) {
                }
            }
            if (!smartActions2.isEmpty()) {
                if (this.activityManagerWrapper.isLockTaskKioskModeActive()) {
                }
                smartActions = new SmartReplyView.SmartActions(smartActions2, true);
            }
        }
        if (smartActions != null) {
        }
        if (list != null) {
            while (r12.hasNext()) {
            }
        }
        z2 = false;
        if (z2) {
        }
        return new InflatedSmartReplyState(smartReplies, smartActions, suppressedActions, z2);
    }

    private final List<Notification.Action> filterAllowlistedLockTaskApps(List<? extends Notification.Action> list) {
        Intent intent;
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            PendingIntent pendingIntent = ((Notification.Action) obj).actionIntent;
            boolean z = false;
            ResolveInfo resolveInfo = null;
            if (pendingIntent != null && (intent = pendingIntent.getIntent()) != null) {
                resolveInfo = this.packageManagerWrapper.resolveActivity(intent, 0);
            }
            if (resolveInfo != null) {
                z = this.devicePolicyManagerWrapper.isLockTaskPermitted(resolveInfo.activityInfo.packageName);
            }
            if (z) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }
}
