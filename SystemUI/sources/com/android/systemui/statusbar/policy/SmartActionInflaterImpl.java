package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.view.View;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ@\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J(\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u001d"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/SmartActionInflaterImpl;", "Lcom/android/systemui/statusbar/policy/SmartActionInflater;", "constants", "Lcom/android/systemui/statusbar/policy/SmartReplyConstants;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "smartReplyController", "Lcom/android/systemui/statusbar/SmartReplyController;", "headsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "(Lcom/android/systemui/statusbar/policy/SmartReplyConstants;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/statusbar/SmartReplyController;Lcom/android/systemui/statusbar/policy/HeadsUpManager;)V", "inflateActionButton", "Landroid/widget/Button;", "parent", "Landroid/view/ViewGroup;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "smartActions", "Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartActions;", "actionIndex", "", "action", "Landroid/app/Notification$Action;", "delayOnClickListener", "", "packageContext", "Landroid/content/Context;", "onSmartActionClick", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SmartReplyStateInflater.kt */
public final class SmartActionInflaterImpl implements SmartActionInflater {
    private final ActivityStarter activityStarter;
    private final SmartReplyConstants constants;
    private final HeadsUpManager headsUpManager;
    /* access modifiers changed from: private */
    public final SmartReplyController smartReplyController;

    @Inject
    public SmartActionInflaterImpl(SmartReplyConstants smartReplyConstants, ActivityStarter activityStarter2, SmartReplyController smartReplyController2, HeadsUpManager headsUpManager2) {
        Intrinsics.checkNotNullParameter(smartReplyConstants, "constants");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(smartReplyController2, "smartReplyController");
        Intrinsics.checkNotNullParameter(headsUpManager2, "headsUpManager");
        this.constants = smartReplyConstants;
        this.activityStarter = activityStarter2;
        this.smartReplyController = smartReplyController2;
        this.headsUpManager = headsUpManager2;
    }

    /* JADX WARNING: type inference failed for: r14v5, types: [android.view.View$OnClickListener] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.widget.Button inflateActionButton(android.view.ViewGroup r8, com.android.systemui.statusbar.notification.collection.NotificationEntry r9, com.android.systemui.statusbar.policy.SmartReplyView.SmartActions r10, int r11, android.app.Notification.Action r12, boolean r13, android.content.Context r14) {
        /*
            r7 = this;
            java.lang.String r0 = "parent"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            java.lang.String r0 = "entry"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
            java.lang.String r0 = "smartActions"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r10, r0)
            java.lang.String r0 = "action"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            java.lang.String r0 = "packageContext"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            android.content.Context r0 = r8.getContext()
            android.view.LayoutInflater r0 = android.view.LayoutInflater.from(r0)
            r1 = 2131624455(0x7f0e0207, float:1.887609E38)
            r2 = 0
            android.view.View r8 = r0.inflate(r1, r8, r2)
            if (r8 == 0) goto L_0x0083
            android.widget.Button r8 = (android.widget.Button) r8
            java.lang.CharSequence r0 = r12.title
            r8.setText(r0)
            android.graphics.drawable.Icon r0 = r12.getIcon()
            android.graphics.drawable.Drawable r14 = r0.loadDrawable(r14)
            android.content.Context r0 = r8.getContext()
            android.content.res.Resources r0 = r0.getResources()
            r1 = 2131167021(0x7f07072d, float:1.7948304E38)
            int r0 = r0.getDimensionPixelSize(r1)
            r14.setBounds(r2, r2, r0, r0)
            r0 = 0
            r8.setCompoundDrawables(r14, r0, r0, r0)
            com.android.systemui.statusbar.policy.SmartActionInflaterImpl$$ExternalSyntheticLambda0 r14 = new com.android.systemui.statusbar.policy.SmartActionInflaterImpl$$ExternalSyntheticLambda0
            r1 = r14
            r2 = r7
            r3 = r9
            r4 = r10
            r5 = r11
            r6 = r12
            r1.<init>(r2, r3, r4, r5, r6)
            if (r13 == 0) goto L_0x006b
            com.android.systemui.statusbar.policy.DelayedOnClickListener r9 = new com.android.systemui.statusbar.policy.DelayedOnClickListener
            com.android.systemui.statusbar.policy.SmartReplyConstants r7 = r7.constants
            long r10 = r7.getOnClickInitDelay()
            r9.<init>(r14, r10)
            r14 = r9
            android.view.View$OnClickListener r14 = (android.view.View.OnClickListener) r14
        L_0x006b:
            r8.setOnClickListener(r14)
            android.view.ViewGroup$LayoutParams r7 = r8.getLayoutParams()
            if (r7 == 0) goto L_0x007b
            com.android.systemui.statusbar.policy.SmartReplyView$LayoutParams r7 = (com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams) r7
            com.android.systemui.statusbar.policy.SmartReplyView$SmartButtonType r9 = com.android.systemui.statusbar.policy.SmartReplyView.SmartButtonType.ACTION
            r7.mButtonType = r9
            return r8
        L_0x007b:
            java.lang.NullPointerException r7 = new java.lang.NullPointerException
            java.lang.String r8 = "null cannot be cast to non-null type com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams"
            r7.<init>(r8)
            throw r7
        L_0x0083:
            java.lang.NullPointerException r7 = new java.lang.NullPointerException
            java.lang.String r8 = "null cannot be cast to non-null type android.widget.Button"
            r7.<init>(r8)
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartActionInflaterImpl.inflateActionButton(android.view.ViewGroup, com.android.systemui.statusbar.notification.collection.NotificationEntry, com.android.systemui.statusbar.policy.SmartReplyView$SmartActions, int, android.app.Notification$Action, boolean, android.content.Context):android.widget.Button");
    }

    /* access modifiers changed from: private */
    /* renamed from: inflateActionButton$lambda-2$lambda-1  reason: not valid java name */
    public static final void m3241inflateActionButton$lambda2$lambda1(SmartActionInflaterImpl smartActionInflaterImpl, NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, int i, Notification.Action action, View view) {
        Intrinsics.checkNotNullParameter(smartActionInflaterImpl, "this$0");
        Intrinsics.checkNotNullParameter(notificationEntry, "$entry");
        Intrinsics.checkNotNullParameter(smartActions, "$smartActions");
        Intrinsics.checkNotNullParameter(action, "$action");
        smartActionInflaterImpl.onSmartActionClick(notificationEntry, smartActions, i, action);
    }

    private final void onSmartActionClick(NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, int i, Notification.Action action) {
        if (!smartActions.fromAssistant || 11 != action.getSemanticAction()) {
            ActivityStarter activityStarter2 = this.activityStarter;
            PendingIntent pendingIntent = action.actionIntent;
            Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
            SmartReplyStateInflaterKt.startPendingIntentDismissingKeyguard(activityStarter2, pendingIntent, notificationEntry.getRow(), new SmartActionInflaterImpl$onSmartActionClick$1(this, notificationEntry, i, action, smartActions));
            return;
        }
        notificationEntry.getRow().doSmartActionClick(((int) notificationEntry.getRow().getX()) / 2, ((int) notificationEntry.getRow().getY()) / 2, 11);
        this.smartReplyController.smartActionClicked(notificationEntry, i, action, smartActions.fromAssistant);
    }
}
