package com.android.systemui.statusbar.notification.row;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.R$id;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ChannelEditorListView.kt */
/* loaded from: classes.dex */
public final class ChannelRow extends LinearLayout {
    @Nullable
    private NotificationChannel channel;
    private TextView channelDescription;
    private TextView channelName;
    public ChannelEditorDialogController controller;
    private boolean gentle;
    private final int highlightColor = Utils.getColorAttrDefaultColor(getContext(), 16843820);

    /* renamed from: switch  reason: not valid java name */
    private Switch f1switch;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ChannelRow(@NotNull Context c, @NotNull AttributeSet attrs) {
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

    @Nullable
    public final NotificationChannel getChannel() {
        return this.channel;
    }

    public final void setChannel(@Nullable NotificationChannel notificationChannel) {
        this.channel = notificationChannel;
        updateImportance();
        updateViews();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(R$id.channel_name);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.channel_name)");
        this.channelName = (TextView) findViewById;
        View findViewById2 = findViewById(R$id.channel_description);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.channel_description)");
        this.channelDescription = (TextView) findViewById2;
        View findViewById3 = findViewById(R$id.toggle);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(R.id.toggle)");
        Switch r0 = (Switch) findViewById3;
        this.f1switch = r0;
        if (r0 != null) {
            r0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelRow$onFinishInflate$1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    NotificationChannel channel = ChannelRow.this.getChannel();
                    if (channel == null) {
                        return;
                    }
                    ChannelRow.this.getController().proposeEditForChannel(channel, z ? channel.getImportance() : 0);
                }
            });
            setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelRow$onFinishInflate$2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    Switch r02;
                    r02 = ChannelRow.this.f1switch;
                    if (r02 != null) {
                        r02.toggle();
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("switch");
                        throw null;
                    }
                }
            });
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("switch");
        throw null;
    }

    public final void playHighlight() {
        ValueAnimator ofObject = ValueAnimator.ofObject(new ArgbEvaluator(), 0, Integer.valueOf(this.highlightColor));
        ofObject.setDuration(200L);
        ofObject.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.notification.row.ChannelRow$playHighlight$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ChannelRow channelRow = ChannelRow.this;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                channelRow.setBackgroundColor(((Integer) animatedValue).intValue());
            }
        });
        ofObject.setRepeatMode(2);
        ofObject.setRepeatCount(5);
        ofObject.start();
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x006b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final void updateViews() {
        Switch r6;
        NotificationChannel notificationChannel = this.channel;
        if (notificationChannel == null) {
            return;
        }
        TextView textView = this.channelName;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("channelName");
            throw null;
        }
        CharSequence name = notificationChannel.getName();
        if (name == null) {
            name = "";
        }
        textView.setText(name);
        String group = notificationChannel.getGroup();
        if (group != null) {
            TextView textView2 = this.channelDescription;
            if (textView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("channelDescription");
                throw null;
            }
            textView2.setText(getController().groupNameForId(group));
        }
        boolean z = false;
        if (notificationChannel.getGroup() != null) {
            TextView textView3 = this.channelDescription;
            if (textView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("channelDescription");
                throw null;
            } else if (!TextUtils.isEmpty(textView3.getText())) {
                TextView textView4 = this.channelDescription;
                if (textView4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("channelDescription");
                    throw null;
                }
                textView4.setVisibility(0);
                r6 = this.f1switch;
                if (r6 != null) {
                    Intrinsics.throwUninitializedPropertyAccessException("switch");
                    throw null;
                }
                if (notificationChannel.getImportance() != 0) {
                    z = true;
                }
                r6.setChecked(z);
                return;
            }
        }
        TextView textView5 = this.channelDescription;
        if (textView5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("channelDescription");
            throw null;
        }
        textView5.setVisibility(8);
        r6 = this.f1switch;
        if (r6 != null) {
        }
    }

    private final void updateImportance() {
        NotificationChannel notificationChannel = this.channel;
        boolean z = false;
        int importance = notificationChannel == null ? 0 : notificationChannel.getImportance();
        if (importance != -1000 && importance < 3) {
            z = true;
        }
        this.gentle = z;
    }
}
