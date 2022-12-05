package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ChannelEditorListView.kt */
/* loaded from: classes.dex */
public final class AppControlView extends LinearLayout {
    public TextView channelName;
    public ImageView iconView;

    /* renamed from: switch  reason: not valid java name */
    public Switch f0switch;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AppControlView(@NotNull Context c, @NotNull AttributeSet attrs) {
        super(c, attrs);
        Intrinsics.checkNotNullParameter(c, "c");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
    }

    @NotNull
    public final ImageView getIconView() {
        ImageView imageView = this.iconView;
        if (imageView != null) {
            return imageView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("iconView");
        throw null;
    }

    public final void setIconView(@NotNull ImageView imageView) {
        Intrinsics.checkNotNullParameter(imageView, "<set-?>");
        this.iconView = imageView;
    }

    @NotNull
    public final TextView getChannelName() {
        TextView textView = this.channelName;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("channelName");
        throw null;
    }

    public final void setChannelName(@NotNull TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.channelName = textView;
    }

    @NotNull
    public final Switch getSwitch() {
        Switch r0 = this.f0switch;
        if (r0 != null) {
            return r0;
        }
        Intrinsics.throwUninitializedPropertyAccessException("switch");
        throw null;
    }

    public final void setSwitch(@NotNull Switch r2) {
        Intrinsics.checkNotNullParameter(r2, "<set-?>");
        this.f0switch = r2;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        View findViewById = findViewById(R$id.icon);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.icon)");
        setIconView((ImageView) findViewById);
        View findViewById2 = findViewById(R$id.app_name);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.app_name)");
        setChannelName((TextView) findViewById2);
        View findViewById3 = findViewById(R$id.toggle);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(R.id.toggle)");
        setSwitch((Switch) findViewById3);
        setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.AppControlView$onFinishInflate$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AppControlView.this.getSwitch().toggle();
            }
        });
    }
}
