package com.android.systemui.statusbar.policy;

import android.app.PendingIntent;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.NotificationUiAdjustment;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000F\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001a\u0010\u0004\u001a\u00020\u00012\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u001a\u0016\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0006\u001a\"\u0010\f\u001a\u00020\r*\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00012\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00010\u0011H\u0002\u001a,\u0010\u0012\u001a\u00020\r*\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\b\u0010\u0016\u001a\u0004\u0018\u00010\u00172\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\r0\u0011H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo65043d2 = {"DEBUG", "", "TAG", "", "areSuggestionsSimilar", "left", "Lcom/android/systemui/statusbar/policy/InflatedSmartReplyState;", "right", "shouldShowSmartReplyView", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "smartReplyState", "executeWhenUnlocked", "", "Lcom/android/systemui/statusbar/phone/KeyguardDismissUtil;", "requiresShadeOpen", "onDismissAction", "Lkotlin/Function0;", "startPendingIntentDismissingKeyguard", "Lcom/android/systemui/plugins/ActivityStarter;", "intent", "Landroid/app/PendingIntent;", "associatedView", "Landroid/view/View;", "runnable", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyStateInflaterKt {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "SmartReplyViewInflater";

    public static final boolean shouldShowSmartReplyView(NotificationEntry notificationEntry, InflatedSmartReplyState inflatedSmartReplyState) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        Intrinsics.checkNotNullParameter(inflatedSmartReplyState, "smartReplyState");
        if ((inflatedSmartReplyState.getSmartReplies() != null || inflatedSmartReplyState.getSmartActions() != null) && !notificationEntry.getSbn().getNotification().extras.getBoolean("android.remoteInputSpinner", false)) {
            return !notificationEntry.getSbn().getNotification().extras.getBoolean("android.hideSmartReplies", false);
        }
        return false;
    }

    public static final boolean areSuggestionsSimilar(InflatedSmartReplyState inflatedSmartReplyState, InflatedSmartReplyState inflatedSmartReplyState2) {
        if (inflatedSmartReplyState == inflatedSmartReplyState2) {
            return true;
        }
        if (inflatedSmartReplyState == null || inflatedSmartReplyState2 == null || inflatedSmartReplyState.getHasPhishingAction() != inflatedSmartReplyState2.getHasPhishingAction() || !Intrinsics.areEqual((Object) inflatedSmartReplyState.getSmartRepliesList(), (Object) inflatedSmartReplyState2.getSmartRepliesList()) || !Intrinsics.areEqual((Object) inflatedSmartReplyState.getSuppressedActionIndices(), (Object) inflatedSmartReplyState2.getSuppressedActionIndices()) || NotificationUiAdjustment.areDifferent(inflatedSmartReplyState.getSmartActionsList(), inflatedSmartReplyState2.getSmartActionsList())) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public static final void executeWhenUnlocked(KeyguardDismissUtil keyguardDismissUtil, boolean z, Function0<Boolean> function0) {
        keyguardDismissUtil.executeWhenUnlocked(new SmartReplyStateInflaterKt$$ExternalSyntheticLambda0(function0), z, false);
    }

    /* access modifiers changed from: private */
    /* renamed from: executeWhenUnlocked$lambda-0  reason: not valid java name */
    public static final boolean m3250executeWhenUnlocked$lambda0(Function0 function0) {
        Intrinsics.checkNotNullParameter(function0, "$tmp0");
        return ((Boolean) function0.invoke()).booleanValue();
    }

    /* access modifiers changed from: private */
    public static final void startPendingIntentDismissingKeyguard(ActivityStarter activityStarter, PendingIntent pendingIntent, View view, Function0<Unit> function0) {
        activityStarter.startPendingIntentDismissingKeyguard(pendingIntent, (Runnable) new SmartReplyStateInflaterKt$$ExternalSyntheticLambda1(function0), view);
    }
}
