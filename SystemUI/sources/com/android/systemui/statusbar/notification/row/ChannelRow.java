package com.android.systemui.statusbar.notification.row;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010!\u001a\u00020\"H\u0014J\u0006\u0010#\u001a\u00020\"J\b\u0010$\u001a\u00020\"H\u0002J\b\u0010%\u001a\u00020\"H\u0002R(\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\b@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX.¢\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u00020\u0012X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0014\"\u0004\b\u0015\u0010\u0016R\u001a\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X.¢\u0006\u0002\n\u0000¨\u0006&"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/row/ChannelRow;", "Landroid/widget/LinearLayout;", "c", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "newValue", "Landroid/app/NotificationChannel;", "channel", "getChannel", "()Landroid/app/NotificationChannel;", "setChannel", "(Landroid/app/NotificationChannel;)V", "channelDescription", "Landroid/widget/TextView;", "channelName", "controller", "Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialogController;", "getController", "()Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialogController;", "setController", "(Lcom/android/systemui/statusbar/notification/row/ChannelEditorDialogController;)V", "gentle", "", "getGentle", "()Z", "setGentle", "(Z)V", "highlightColor", "", "switch", "Landroid/widget/Switch;", "onFinishInflate", "", "playHighlight", "updateImportance", "updateViews", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ChannelEditorListView.kt */
public final class ChannelRow extends LinearLayout {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private NotificationChannel channel;
    private TextView channelDescription;
    private TextView channelName;
    public ChannelEditorDialogController controller;
    private boolean gentle;
    private final int highlightColor;

    /* renamed from: switch  reason: not valid java name */
    private Switch f949switch;

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
    public ChannelRow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "c");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
        this.highlightColor = Utils.getColorAttrDefaultColor(getContext(), 16843820);
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

    public final boolean getGentle() {
        return this.gentle;
    }

    public final void setGentle(boolean z) {
        this.gentle = z;
    }

    public final NotificationChannel getChannel() {
        return this.channel;
    }

    public final void setChannel(NotificationChannel notificationChannel) {
        this.channel = notificationChannel;
        updateImportance();
        updateViews();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(C1893R.C1897id.channel_name);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.channel_name)");
        this.channelName = (TextView) findViewById;
        View findViewById2 = findViewById(C1893R.C1897id.channel_description);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.channel_description)");
        this.channelDescription = (TextView) findViewById2;
        View findViewById3 = findViewById(C1893R.C1897id.toggle);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(R.id.toggle)");
        Switch switchR = (Switch) findViewById3;
        this.f949switch = switchR;
        if (switchR == null) {
            Intrinsics.throwUninitializedPropertyAccessException("switch");
            switchR = null;
        }
        switchR.setOnCheckedChangeListener(new ChannelRow$$ExternalSyntheticLambda1(this));
        setOnClickListener(new ChannelRow$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onFinishInflate$lambda-1  reason: not valid java name */
    public static final void m3142onFinishInflate$lambda1(ChannelRow channelRow, CompoundButton compoundButton, boolean z) {
        Intrinsics.checkNotNullParameter(channelRow, "this$0");
        NotificationChannel notificationChannel = channelRow.channel;
        if (notificationChannel != null) {
            channelRow.getController().proposeEditForChannel(notificationChannel, z ? notificationChannel.getImportance() : 0);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onFinishInflate$lambda-2  reason: not valid java name */
    public static final void m3143onFinishInflate$lambda2(ChannelRow channelRow, View view) {
        Intrinsics.checkNotNullParameter(channelRow, "this$0");
        Switch switchR = channelRow.f949switch;
        if (switchR == null) {
            Intrinsics.throwUninitializedPropertyAccessException("switch");
            switchR = null;
        }
        switchR.toggle();
    }

    public final void playHighlight() {
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), new Object[]{0, Integer.valueOf(this.highlightColor)});
        ofObject.setDuration(200);
        ofObject.addUpdateListener(new ChannelRow$$ExternalSyntheticLambda0(this));
        ofObject.setRepeatMode(2);
        ofObject.setRepeatCount(5);
        ofObject.start();
    }

    /* access modifiers changed from: private */
    /* renamed from: playHighlight$lambda-3  reason: not valid java name */
    public static final void m3144playHighlight$lambda3(ChannelRow channelRow, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(channelRow, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            channelRow.setBackgroundColor(((Integer) animatedValue).intValue());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x007d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void updateViews() {
        /*
            r6 = this;
            android.app.NotificationChannel r0 = r6.channel
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            android.widget.TextView r1 = r6.channelName
            r2 = 0
            if (r1 != 0) goto L_0x0010
            java.lang.String r1 = "channelName"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
            r1 = r2
        L_0x0010:
            java.lang.CharSequence r3 = r0.getName()
            if (r3 != 0) goto L_0x001a
            java.lang.String r3 = ""
            java.lang.CharSequence r3 = (java.lang.CharSequence) r3
        L_0x001a:
            r1.setText(r3)
            java.lang.String r1 = r0.getGroup()
            java.lang.String r3 = "channelDescription"
            if (r1 == 0) goto L_0x0038
            android.widget.TextView r4 = r6.channelDescription
            if (r4 != 0) goto L_0x002d
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
            r4 = r2
        L_0x002d:
            com.android.systemui.statusbar.notification.row.ChannelEditorDialogController r5 = r6.getController()
            java.lang.CharSequence r1 = r5.groupNameForId(r1)
            r4.setText(r1)
        L_0x0038:
            java.lang.String r1 = r0.getGroup()
            r4 = 0
            if (r1 == 0) goto L_0x005e
            android.widget.TextView r1 = r6.channelDescription
            if (r1 != 0) goto L_0x0047
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
            r1 = r2
        L_0x0047:
            java.lang.CharSequence r1 = r1.getText()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x0052
            goto L_0x005e
        L_0x0052:
            android.widget.TextView r1 = r6.channelDescription
            if (r1 != 0) goto L_0x005a
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
            r1 = r2
        L_0x005a:
            r1.setVisibility(r4)
            goto L_0x006b
        L_0x005e:
            android.widget.TextView r1 = r6.channelDescription
            if (r1 != 0) goto L_0x0066
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r3)
            r1 = r2
        L_0x0066:
            r3 = 8
            r1.setVisibility(r3)
        L_0x006b:
            android.widget.Switch r6 = r6.f949switch
            if (r6 != 0) goto L_0x0076
            java.lang.String r6 = "switch"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r6)
            goto L_0x0077
        L_0x0076:
            r2 = r6
        L_0x0077:
            int r6 = r0.getImportance()
            if (r6 == 0) goto L_0x007e
            r4 = 1
        L_0x007e:
            r2.setChecked(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.row.ChannelRow.updateViews():void");
    }

    private final void updateImportance() {
        NotificationChannel notificationChannel = this.channel;
        boolean z = false;
        int importance = notificationChannel != null ? notificationChannel.getImportance() : 0;
        if (importance != -1000 && importance < 3) {
            z = true;
        }
        this.gentle = z;
    }
}
