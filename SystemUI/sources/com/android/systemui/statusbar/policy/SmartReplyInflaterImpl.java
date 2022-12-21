package com.android.systemui.statusbar.policy;

import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.SmartReplyView;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0018\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002J8\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J8\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001f\u001a\u00020\u00162\u0006\u0010 \u001a\u00020\u00142\u0006\u0010\u0011\u001a\u00020\u0012H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006!"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/SmartReplyInflaterImpl;", "Lcom/android/systemui/statusbar/policy/SmartReplyInflater;", "constants", "Lcom/android/systemui/statusbar/policy/SmartReplyConstants;", "keyguardDismissUtil", "Lcom/android/systemui/statusbar/phone/KeyguardDismissUtil;", "remoteInputManager", "Lcom/android/systemui/statusbar/NotificationRemoteInputManager;", "smartReplyController", "Lcom/android/systemui/statusbar/SmartReplyController;", "context", "Landroid/content/Context;", "(Lcom/android/systemui/statusbar/policy/SmartReplyConstants;Lcom/android/systemui/statusbar/phone/KeyguardDismissUtil;Lcom/android/systemui/statusbar/NotificationRemoteInputManager;Lcom/android/systemui/statusbar/SmartReplyController;Landroid/content/Context;)V", "createRemoteInputIntent", "Landroid/content/Intent;", "smartReplies", "Lcom/android/systemui/statusbar/policy/SmartReplyView$SmartReplies;", "choice", "", "inflateReplyButton", "Landroid/widget/Button;", "parent", "Lcom/android/systemui/statusbar/policy/SmartReplyView;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "replyIndex", "", "delayOnClickListener", "", "onSmartReplyClick", "", "smartReplyView", "button", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SmartReplyStateInflater.kt */
public final class SmartReplyInflaterImpl implements SmartReplyInflater {
    /* access modifiers changed from: private */
    public final SmartReplyConstants constants;
    /* access modifiers changed from: private */
    public final Context context;
    private final KeyguardDismissUtil keyguardDismissUtil;
    /* access modifiers changed from: private */
    public final NotificationRemoteInputManager remoteInputManager;
    /* access modifiers changed from: private */
    public final SmartReplyController smartReplyController;

    @Inject
    public SmartReplyInflaterImpl(SmartReplyConstants smartReplyConstants, KeyguardDismissUtil keyguardDismissUtil2, NotificationRemoteInputManager notificationRemoteInputManager, SmartReplyController smartReplyController2, Context context2) {
        Intrinsics.checkNotNullParameter(smartReplyConstants, "constants");
        Intrinsics.checkNotNullParameter(keyguardDismissUtil2, "keyguardDismissUtil");
        Intrinsics.checkNotNullParameter(notificationRemoteInputManager, "remoteInputManager");
        Intrinsics.checkNotNullParameter(smartReplyController2, "smartReplyController");
        Intrinsics.checkNotNullParameter(context2, "context");
        this.constants = smartReplyConstants;
        this.keyguardDismissUtil = keyguardDismissUtil2;
        this.remoteInputManager = notificationRemoteInputManager;
        this.smartReplyController = smartReplyController2;
        this.context = context2;
    }

    /* JADX WARNING: type inference failed for: r10v3, types: [android.view.View$OnClickListener] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.widget.Button inflateReplyButton(com.android.systemui.statusbar.policy.SmartReplyView r12, com.android.systemui.statusbar.notification.collection.NotificationEntry r13, com.android.systemui.statusbar.policy.SmartReplyView.SmartReplies r14, int r15, java.lang.CharSequence r16, boolean r17) {
        /*
            r11 = this;
            r8 = r12
            r7 = r16
            java.lang.String r0 = "parent"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r12, r0)
            java.lang.String r0 = "entry"
            r2 = r13
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r13, r0)
            java.lang.String r0 = "smartReplies"
            r3 = r14
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r14, r0)
            java.lang.String r0 = "choice"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            android.content.Context r0 = r12.getContext()
            android.view.LayoutInflater r0 = android.view.LayoutInflater.from(r0)
            r1 = r8
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            r4 = 0
            r5 = 2131624456(0x7f0e0208, float:1.8876092E38)
            android.view.View r0 = r0.inflate(r5, r1, r4)
            if (r0 == 0) goto L_0x0075
            r9 = r0
            android.widget.Button r9 = (android.widget.Button) r9
            r9.setText(r7)
            com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$$ExternalSyntheticLambda0 r10 = new com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$$ExternalSyntheticLambda0
            r0 = r10
            r1 = r11
            r2 = r13
            r3 = r14
            r4 = r15
            r5 = r12
            r6 = r9
            r7 = r16
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            if (r17 == 0) goto L_0x0053
            com.android.systemui.statusbar.policy.DelayedOnClickListener r0 = new com.android.systemui.statusbar.policy.DelayedOnClickListener
            r1 = r11
            com.android.systemui.statusbar.policy.SmartReplyConstants r1 = r1.constants
            long r1 = r1.getOnClickInitDelay()
            r0.<init>(r10, r1)
            r10 = r0
            android.view.View$OnClickListener r10 = (android.view.View.OnClickListener) r10
        L_0x0053:
            r9.setOnClickListener(r10)
            com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$inflateReplyButton$1$1 r0 = new com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$inflateReplyButton$1$1
            r0.<init>(r12)
            android.view.View$AccessibilityDelegate r0 = (android.view.View.AccessibilityDelegate) r0
            r9.setAccessibilityDelegate(r0)
            android.view.ViewGroup$LayoutParams r0 = r9.getLayoutParams()
            if (r0 == 0) goto L_0x006d
            com.android.systemui.statusbar.policy.SmartReplyView$LayoutParams r0 = (com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams) r0
            com.android.systemui.statusbar.policy.SmartReplyView$SmartButtonType r1 = com.android.systemui.statusbar.policy.SmartReplyView.SmartButtonType.REPLY
            r0.mButtonType = r1
            return r9
        L_0x006d:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "null cannot be cast to non-null type com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams"
            r0.<init>(r1)
            throw r0
        L_0x0075:
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "null cannot be cast to non-null type android.widget.Button"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartReplyInflaterImpl.inflateReplyButton(com.android.systemui.statusbar.policy.SmartReplyView, com.android.systemui.statusbar.notification.collection.NotificationEntry, com.android.systemui.statusbar.policy.SmartReplyView$SmartReplies, int, java.lang.CharSequence, boolean):android.widget.Button");
    }

    /* access modifiers changed from: private */
    /* renamed from: inflateReplyButton$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3243inflateReplyButton$lambda1$lambda0(SmartReplyInflaterImpl smartReplyInflaterImpl, NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, int i, SmartReplyView smartReplyView, Button button, CharSequence charSequence, View view) {
        Intrinsics.checkNotNullParameter(smartReplyInflaterImpl, "this$0");
        Intrinsics.checkNotNullParameter(notificationEntry, "$entry");
        Intrinsics.checkNotNullParameter(smartReplies, "$smartReplies");
        Intrinsics.checkNotNullParameter(smartReplyView, "$parent");
        Intrinsics.checkNotNullParameter(button, "$this_apply");
        Intrinsics.checkNotNullParameter(charSequence, "$choice");
        smartReplyInflaterImpl.onSmartReplyClick(notificationEntry, smartReplies, i, smartReplyView, button, charSequence);
    }

    private final void onSmartReplyClick(NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, int i, SmartReplyView smartReplyView, Button button, CharSequence charSequence) {
        SmartReplyStateInflaterKt.executeWhenUnlocked(this.keyguardDismissUtil, !notificationEntry.isRowPinned(), new SmartReplyInflaterImpl$onSmartReplyClick$1(this, smartReplies, button, charSequence, i, notificationEntry, smartReplyView));
    }

    /* access modifiers changed from: private */
    public final Intent createRemoteInputIntent(SmartReplyView.SmartReplies smartReplies, CharSequence charSequence) {
        Bundle bundle = new Bundle();
        bundle.putString(smartReplies.remoteInput.getResultKey(), charSequence.toString());
        Intent addFlags = new Intent().addFlags(268435456);
        RemoteInput.addResultsToIntent(new RemoteInput[]{smartReplies.remoteInput}, addFlags, bundle);
        RemoteInput.setResultsSource(addFlags, 1);
        Intrinsics.checkNotNullExpressionValue(addFlags, "intent");
        return addFlags;
    }
}
