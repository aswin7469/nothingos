package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
public final class SmartActionInflaterImpl implements SmartActionInflater {
    @NotNull
    private final ActivityStarter activityStarter;
    @NotNull
    private final SmartReplyConstants constants;
    @NotNull
    private final HeadsUpManager headsUpManager;
    @NotNull
    private final SmartReplyController smartReplyController;

    public SmartActionInflaterImpl(@NotNull SmartReplyConstants constants, @NotNull ActivityStarter activityStarter, @NotNull SmartReplyController smartReplyController, @NotNull HeadsUpManager headsUpManager) {
        Intrinsics.checkNotNullParameter(constants, "constants");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(smartReplyController, "smartReplyController");
        Intrinsics.checkNotNullParameter(headsUpManager, "headsUpManager");
        this.constants = constants;
        this.activityStarter = activityStarter;
        this.smartReplyController = smartReplyController;
        this.headsUpManager = headsUpManager;
    }

    @Override // com.android.systemui.statusbar.policy.SmartActionInflater
    @NotNull
    public Button inflateActionButton(@NotNull ViewGroup parent, @NotNull final NotificationEntry entry, @NotNull final SmartReplyView.SmartActions smartActions, final int i, @NotNull final Notification.Action action, boolean z, @NotNull Context packageContext) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(entry, "entry");
        Intrinsics.checkNotNullParameter(smartActions, "smartActions");
        Intrinsics.checkNotNullParameter(action, "action");
        Intrinsics.checkNotNullParameter(packageContext, "packageContext");
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R$layout.smart_action_button, parent, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.widget.Button");
        Button button = (Button) inflate;
        button.setText(action.title);
        Drawable loadDrawable = action.getIcon().loadDrawable(packageContext);
        int dimensionPixelSize = button.getContext().getResources().getDimensionPixelSize(R$dimen.smart_action_button_icon_size);
        loadDrawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
        button.setCompoundDrawables(loadDrawable, null, null, null);
        View.OnClickListener onClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.policy.SmartActionInflaterImpl$inflateActionButton$1$onClickListener$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SmartActionInflaterImpl.this.onSmartActionClick(entry, smartActions, i, action);
            }
        };
        if (z) {
            onClickListener = new DelayedOnClickListener(onClickListener, this.constants.getOnClickInitDelay());
        }
        button.setOnClickListener(onClickListener);
        ViewGroup.LayoutParams layoutParams = button.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type com.android.systemui.statusbar.policy.SmartReplyView.LayoutParams");
        ((SmartReplyView.LayoutParams) layoutParams).mButtonType = SmartReplyView.SmartButtonType.ACTION;
        return button;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onSmartActionClick(NotificationEntry notificationEntry, SmartReplyView.SmartActions smartActions, int i, Notification.Action action) {
        if (smartActions.fromAssistant && 11 == action.getSemanticAction()) {
            notificationEntry.getRow().doSmartActionClick(((int) notificationEntry.getRow().getX()) / 2, ((int) notificationEntry.getRow().getY()) / 2, 11);
            this.smartReplyController.smartActionClicked(notificationEntry, i, action, smartActions.fromAssistant);
            return;
        }
        ActivityStarter activityStarter = this.activityStarter;
        PendingIntent pendingIntent = action.actionIntent;
        Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
        SmartReplyStateInflaterKt.access$startPendingIntentDismissingKeyguard(activityStarter, pendingIntent, notificationEntry.getRow(), new SmartActionInflaterImpl$onSmartActionClick$1(this, notificationEntry, i, action, smartActions));
    }
}
