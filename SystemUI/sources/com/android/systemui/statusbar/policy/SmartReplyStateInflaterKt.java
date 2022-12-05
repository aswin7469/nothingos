package com.android.systemui.statusbar.policy;

import android.app.PendingIntent;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.NotificationUiAdjustment;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
public final class SmartReplyStateInflaterKt {
    private static final boolean DEBUG = Log.isLoggable("SmartReplyViewInflater", 3);

    public static final /* synthetic */ void access$executeWhenUnlocked(KeyguardDismissUtil keyguardDismissUtil, boolean z, Function0 function0) {
        executeWhenUnlocked(keyguardDismissUtil, z, function0);
    }

    public static final /* synthetic */ boolean access$getDEBUG$p() {
        return DEBUG;
    }

    public static final /* synthetic */ void access$startPendingIntentDismissingKeyguard(ActivityStarter activityStarter, PendingIntent pendingIntent, View view, Function0 function0) {
        startPendingIntentDismissingKeyguard(activityStarter, pendingIntent, view, function0);
    }

    public static final boolean shouldShowSmartReplyView(@NotNull NotificationEntry entry, @NotNull InflatedSmartReplyState smartReplyState) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        Intrinsics.checkNotNullParameter(smartReplyState, "smartReplyState");
        if (!(smartReplyState.getSmartReplies() == null && smartReplyState.getSmartActions() == null) && !entry.getSbn().getNotification().extras.getBoolean("android.remoteInputSpinner", false)) {
            return !entry.getSbn().getNotification().extras.getBoolean("android.hideSmartReplies", false);
        }
        return false;
    }

    public static final boolean areSuggestionsSimilar(@Nullable InflatedSmartReplyState inflatedSmartReplyState, @Nullable InflatedSmartReplyState inflatedSmartReplyState2) {
        if (inflatedSmartReplyState == inflatedSmartReplyState2) {
            return true;
        }
        return inflatedSmartReplyState != null && inflatedSmartReplyState2 != null && inflatedSmartReplyState.getHasPhishingAction() == inflatedSmartReplyState2.getHasPhishingAction() && Intrinsics.areEqual(inflatedSmartReplyState.getSmartRepliesList(), inflatedSmartReplyState2.getSmartRepliesList()) && Intrinsics.areEqual(inflatedSmartReplyState.getSuppressedActionIndices(), inflatedSmartReplyState2.getSuppressedActionIndices()) && !NotificationUiAdjustment.areDifferent(inflatedSmartReplyState.getSmartActionsList(), inflatedSmartReplyState2.getSmartActionsList());
    }

    public static final void executeWhenUnlocked(KeyguardDismissUtil keyguardDismissUtil, boolean z, final Function0<Boolean> function0) {
        keyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt$sam$com_android_systemui_plugins_ActivityStarter_OnDismissAction$0
            @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
            public final /* synthetic */ boolean onDismiss() {
                return ((Boolean) Function0.this.mo1951invoke()).booleanValue();
            }
        }, z, false);
    }

    public static final void startPendingIntentDismissingKeyguard(ActivityStarter activityStarter, PendingIntent pendingIntent, View view, final Function0<Unit> function0) {
        activityStarter.startPendingIntentDismissingKeyguard(pendingIntent, new Runnable() { // from class: com.android.systemui.statusbar.policy.SmartReplyStateInflaterKt$startPendingIntentDismissingKeyguard$1
            /* JADX WARN: Type inference failed for: r0v2, types: [R, java.lang.Object] */
            @Override // java.lang.Runnable
            /* renamed from: run */
            public final R mo1325run() {
                return function0.mo1951invoke();
            }
        }, view);
    }
}
