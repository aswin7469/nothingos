package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import com.android.systemui.C1894R;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020\u00192\u0006\u0010(\u001a\u00020)H\u0002J\u000e\u0010*\u001a\u00020&2\u0006\u0010'\u001a\u00020\u0019J\b\u0010+\u001a\u00020&H\u0014J\u0010\u0010,\u001a\u00020&2\u0006\u0010-\u001a\u00020.H\u0002J\b\u0010/\u001a\u00020&H\u0002R\u000e\u0010\u0007\u001a\u00020\bX.¢\u0006\u0002\n\u0000R\u001c\u0010\t\u001a\u0004\u0018\u00010\nX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0004¢\u0006\u0002\n\u0000R0\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00190\u00162\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00190\u0016@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\u00020 X.¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$¨\u00060"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/row/ChannelEditorListView;", "Landroid/widget/LinearLayout;", "c", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "appControlRow", "Lcom/android/systemui/statusbar/notification/row/AppControlView;", "appIcon", "Landroid/graphics/drawable/Drawable;", "getAppIcon", "()Landroid/graphics/drawable/Drawable;", "setAppIcon", "(Landroid/graphics/drawable/Drawable;)V", "appName", "", "getAppName", "()Ljava/lang/String;", "setAppName", "(Ljava/lang/String;)V", "channelRows", "", "Lcom/android/systemui/statusbar/notification/row/ChannelRow;", "newValue", "Landroid/app/NotificationChannel;", "channels", "getChannels", "()Ljava/util/List;", "setChannels", "(Ljava/util/List;)V", "controller", "Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialogController;", "getController", "()Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialogController;", "setController", "(Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialogController;)V", "addChannelRow", "", "channel", "inflater", "Landroid/view/LayoutInflater;", "highlightChannel", "onFinishInflate", "updateAppControlRow", "enabled", "", "updateRows", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ChannelEditorListView.kt */
public final class ChannelEditorListView extends LinearLayout {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private AppControlView appControlRow;
    private Drawable appIcon;
    private String appName;
    private final List<ChannelRow> channelRows;
    private List<NotificationChannel> channels;
    public ChannelEditorDialogController controller;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ChannelEditorListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "c");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
        this.channels = new ArrayList();
        this.channelRows = new ArrayList();
    }

    public final ChannelEditorDialogController getController() {
        ChannelEditorDialogController channelEditorDialogController = this.controller;
        if (channelEditorDialogController != null) {
            return channelEditorDialogController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("controller");
        return null;
    }

    public final void setController(ChannelEditorDialogController channelEditorDialogController) {
        Intrinsics.checkNotNullParameter(channelEditorDialogController, "<set-?>");
        this.controller = channelEditorDialogController;
    }

    public final Drawable getAppIcon() {
        return this.appIcon;
    }

    public final void setAppIcon(Drawable drawable) {
        this.appIcon = drawable;
    }

    public final String getAppName() {
        return this.appName;
    }

    public final void setAppName(String str) {
        this.appName = str;
    }

    public final List<NotificationChannel> getChannels() {
        return this.channels;
    }

    public final void setChannels(List<NotificationChannel> list) {
        Intrinsics.checkNotNullParameter(list, "newValue");
        this.channels = list;
        updateRows();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(C1894R.C1898id.app_control);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.app_control)");
        this.appControlRow = (AppControlView) findViewById;
    }

    public final void highlightChannel(NotificationChannel notificationChannel) {
        Intrinsics.checkNotNullParameter(notificationChannel, "channel");
        Assert.isMainThread();
        for (ChannelRow next : this.channelRows) {
            if (Intrinsics.areEqual((Object) next.getChannel(), (Object) notificationChannel)) {
                next.playHighlight();
            }
        }
    }

    private final void updateRows() {
        boolean areAppNotificationsEnabled = getController().areAppNotificationsEnabled();
        AutoTransition autoTransition = new AutoTransition();
        autoTransition.setDuration(200);
        autoTransition.addListener(new ChannelEditorListView$updateRows$1(this));
        TransitionManager.beginDelayedTransition(this, autoTransition);
        for (ChannelRow removeView : this.channelRows) {
            removeView(removeView);
        }
        this.channelRows.clear();
        updateAppControlRow(areAppNotificationsEnabled);
        if (areAppNotificationsEnabled) {
            LayoutInflater from = LayoutInflater.from(getContext());
            for (NotificationChannel addChannelRow : this.channels) {
                Intrinsics.checkNotNullExpressionValue(from, "inflater");
                addChannelRow(addChannelRow, from);
            }
        }
    }

    private final void addChannelRow(NotificationChannel notificationChannel, LayoutInflater layoutInflater) {
        View inflate = layoutInflater.inflate(C1894R.layout.notif_half_shelf_row, (ViewGroup) null);
        if (inflate != null) {
            ChannelRow channelRow = (ChannelRow) inflate;
            channelRow.setController(getController());
            channelRow.setChannel(notificationChannel);
            this.channelRows.add(channelRow);
            addView(channelRow);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.statusbar.notification.row.ChannelRow");
    }

    private final void updateAppControlRow(boolean z) {
        AppControlView appControlView = this.appControlRow;
        AppControlView appControlView2 = null;
        if (appControlView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appControlRow");
            appControlView = null;
        }
        appControlView.getIconView().setImageDrawable(this.appIcon);
        AppControlView appControlView3 = this.appControlRow;
        if (appControlView3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appControlRow");
            appControlView3 = null;
        }
        appControlView3.getChannelName().setText(getContext().getResources().getString(C1894R.string.notification_channel_dialog_title, new Object[]{this.appName}));
        AppControlView appControlView4 = this.appControlRow;
        if (appControlView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appControlRow");
            appControlView4 = null;
        }
        appControlView4.getSwitch().setChecked(z);
        AppControlView appControlView5 = this.appControlRow;
        if (appControlView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("appControlRow");
        } else {
            appControlView2 = appControlView5;
        }
        appControlView2.getSwitch().setOnCheckedChangeListener(new ChannelEditorListView$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateAppControlRow$lambda-0  reason: not valid java name */
    public static final void m3143updateAppControlRow$lambda0(ChannelEditorListView channelEditorListView, CompoundButton compoundButton, boolean z) {
        Intrinsics.checkNotNullParameter(channelEditorListView, "this$0");
        channelEditorListView.getController().proposeSetAppNotificationsEnabled(z);
        channelEditorListView.updateRows();
    }
}
