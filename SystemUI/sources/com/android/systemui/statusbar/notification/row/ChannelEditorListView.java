package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ChannelEditorListView.kt */
/* loaded from: classes.dex */
public final class ChannelEditorListView extends LinearLayout {
    private AppControlView appControlRow;
    @Nullable
    private Drawable appIcon;
    @Nullable
    private String appName;
    public ChannelEditorDialogController controller;
    @NotNull
    private List<NotificationChannel> channels = new ArrayList();
    @NotNull
    private final List<ChannelRow> channelRows = new ArrayList();

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelEditorListView(@NotNull Context c, @NotNull AttributeSet attrs) {
        super(c, attrs);
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
    }

    @NotNull
    public final ChannelEditorDialogController getController() {
        ChannelEditorDialogController channelEditorDialogController = this.controller;
        if (channelEditorDialogController != null) {
            return channelEditorDialogController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("controller");
        throw null;
    }

    public final void setController(@NotNull ChannelEditorDialogController channelEditorDialogController) {
        Intrinsics.checkNotNullParameter(channelEditorDialogController, "<set-?>");
        this.controller = channelEditorDialogController;
    }

    public final void setAppIcon(@Nullable Drawable drawable) {
        this.appIcon = drawable;
    }

    public final void setAppName(@Nullable String str) {
        this.appName = str;
    }

    public final void setChannels(@NotNull List<NotificationChannel> newValue) {
        Intrinsics.checkNotNullParameter(newValue, "newValue");
        this.channels = newValue;
        updateRows();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(R$id.app_control);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.app_control)");
        this.appControlRow = (AppControlView) findViewById;
    }

    public final void highlightChannel(@NotNull NotificationChannel channel) {
        Intrinsics.checkNotNullParameter(channel, "channel");
        Assert.isMainThread();
        for (ChannelRow channelRow : this.channelRows) {
            if (Intrinsics.areEqual(channelRow.getChannel(), channel)) {
                channelRow.playHighlight();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateRows() {
        boolean areAppNotificationsEnabled = getController().areAppNotificationsEnabled();
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(200L);
        autoTransition.addListener(new Transition.TransitionListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorListView$updateRows$1
            @Override // android.transition.Transition.TransitionListener
            public void onTransitionCancel(@Nullable Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionPause(@Nullable Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionResume(@Nullable Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionStart(@Nullable Transition transition) {
            }

            @Override // android.transition.Transition.TransitionListener
            public void onTransitionEnd(@Nullable Transition transition) {
                ChannelEditorListView.this.notifySubtreeAccessibilityStateChangedIfNeeded();
            }
        });
        TransitionManager.beginDelayedTransition(this, autoTransition);
        for (ChannelRow channelRow : this.channelRows) {
            removeView(channelRow);
        }
        this.channelRows.clear();
        updateAppControlRow(areAppNotificationsEnabled);
        if (areAppNotificationsEnabled) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            for (NotificationChannel notificationChannel : this.channels) {
                Intrinsics.checkNotNullExpressionValue(inflater, "inflater");
                addChannelRow(notificationChannel, inflater);
            }
        }
    }

    private final void addChannelRow(NotificationChannel notificationChannel, LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(R$layout.notif_half_shelf_row, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.systemui.statusbar.notification.row.ChannelRow");
        ChannelRow channelRow = (ChannelRow) inflate;
        channelRow.setController(getController());
        channelRow.setChannel(notificationChannel);
        this.channelRows.add(channelRow);
        addView(channelRow);
    }

    private final void updateAppControlRow(boolean z) {
        AppControlView appControlView = this.appControlRow;
        if (appControlView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appControlRow");
            throw null;
        }
        appControlView.getIconView().setImageDrawable(this.appIcon);
        AppControlView appControlView2 = this.appControlRow;
        if (appControlView2 != null) {
            appControlView2.getChannelName().setText(getContext().getResources().getString(R$string.notification_channel_dialog_title, this.appName));
            AppControlView appControlView3 = this.appControlRow;
            if (appControlView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("appControlRow");
                throw null;
            }
            appControlView3.getSwitch().setChecked(z);
            AppControlView appControlView4 = this.appControlRow;
            if (appControlView4 != null) {
                appControlView4.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelEditorListView$updateAppControlRow$1
                    @Override // android.widget.CompoundButton.OnCheckedChangeListener
                    public final void onCheckedChanged(CompoundButton compoundButton, boolean z2) {
                        ChannelEditorListView.this.getController().proposeSetAppNotificationsEnabled(z2);
                        ChannelEditorListView.this.updateRows();
                    }
                });
                return;
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("appControlRow");
                throw null;
            }
        }
        Intrinsics.throwUninitializedPropertyAccessException("appControlRow");
        throw null;
    }
}
