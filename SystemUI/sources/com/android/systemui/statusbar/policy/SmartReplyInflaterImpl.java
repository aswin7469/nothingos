package com.android.systemui.statusbar.policy;

import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
public final class SmartReplyInflaterImpl implements SmartReplyInflater {
    @NotNull
    private final SmartReplyConstants constants;
    @NotNull
    private final Context context;
    @NotNull
    private final KeyguardDismissUtil keyguardDismissUtil;
    @NotNull
    private final NotificationRemoteInputManager remoteInputManager;
    @NotNull
    private final SmartReplyController smartReplyController;

    public SmartReplyInflaterImpl(@NotNull SmartReplyConstants constants, @NotNull KeyguardDismissUtil keyguardDismissUtil, @NotNull NotificationRemoteInputManager remoteInputManager, @NotNull SmartReplyController smartReplyController, @NotNull Context context) {
        Intrinsics.checkNotNullParameter(constants, "constants");
        Intrinsics.checkNotNullParameter(keyguardDismissUtil, "keyguardDismissUtil");
        Intrinsics.checkNotNullParameter(remoteInputManager, "remoteInputManager");
        Intrinsics.checkNotNullParameter(smartReplyController, "smartReplyController");
        Intrinsics.checkNotNullParameter(context, "context");
        this.constants = constants;
        this.keyguardDismissUtil = keyguardDismissUtil;
        this.remoteInputManager = remoteInputManager;
        this.smartReplyController = smartReplyController;
        this.context = context;
    }

    @Override // com.android.systemui.statusbar.policy.SmartReplyInflater
    @NotNull
    public Button inflateReplyButton(@NotNull final SmartReplyView parent, @NotNull final NotificationEntry entry, @NotNull final SmartReplyView.SmartReplies smartReplies, final int i, @NotNull final CharSequence choice, boolean z) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(entry, "entry");
        Intrinsics.checkNotNullParameter(smartReplies, "smartReplies");
        Intrinsics.checkNotNullParameter(choice, "choice");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R$layout.smart_reply_button, (ViewGroup) parent, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.Button");
        final Button button = (Button) inflate;
        button.setText(choice);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$inflateReplyButton$1$onClickListener$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SmartReplyInflaterImpl.this.onSmartReplyClick(entry, smartReplies, i, parent, button, choice);
            }
        };
        if (z) {
            onClickListener = new DelayedOnClickListener(onClickListener, this.constants.getOnClickInitDelay());
        }
        button.setOnClickListener(onClickListener);
        button.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.android.systemui.statusbar.policy.SmartReplyInflaterImpl$inflateReplyButton$1$1
            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(@NotNull View host, @NotNull AccessibilityNodeInfo info) {
                Intrinsics.checkNotNullParameter(host, "host");
                Intrinsics.checkNotNullParameter(info, "info");
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.addAction(new AccessibilityNodeInfo.AccessibilityAction(16, SmartReplyView.this.getResources().getString(R$string.accessibility_send_smart_reply)));
            }
        });
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams");
        ((SmartReplyView.LayoutParams) layoutParams).mButtonType = SmartReplyView.SmartButtonType.REPLY;
        return button;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onSmartReplyClick(NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, int i, SmartReplyView smartReplyView, Button button, CharSequence charSequence) {
        SmartReplyStateInflaterKt.access$executeWhenUnlocked(this.keyguardDismissUtil, !notificationEntry.isRowPinned(), new SmartReplyInflaterImpl$onSmartReplyClick$1(this, smartReplies, button, charSequence, i, notificationEntry, smartReplyView));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Intent createRemoteInputIntent(SmartReplyView.SmartReplies smartReplies, CharSequence charSequence) {
        Bundle bundle = new Bundle();
        bundle.putString(smartReplies.remoteInput.getResultKey(), charSequence.toString());
        Intent intent = new Intent().addFlags(268435456);
        RemoteInput.addResultsToIntent(new RemoteInput[]{smartReplies.remoteInput}, intent, bundle);
        RemoteInput.setResultsSource(intent, 1);
        Intrinsics.checkNotNullExpressionValue(intent, "intent");
        return intent;
    }
}
