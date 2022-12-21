package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.systemui.C1893R;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0019\u001a\u00020\u001aH\u0014R\u001a\u0010\u0007\u001a\u00020\bX.¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u0014X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018¨\u0006\u001b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/row/AppControlView;", "Landroid/widget/LinearLayout;", "c", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "channelName", "Landroid/widget/TextView;", "getChannelName", "()Landroid/widget/TextView;", "setChannelName", "(Landroid/widget/TextView;)V", "iconView", "Landroid/widget/ImageView;", "getIconView", "()Landroid/widget/ImageView;", "setIconView", "(Landroid/widget/ImageView;)V", "switch", "Landroid/widget/Switch;", "getSwitch", "()Landroid/widget/Switch;", "setSwitch", "(Landroid/widget/Switch;)V", "onFinishInflate", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ChannelEditorListView.kt */
public final class AppControlView extends LinearLayout {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    public TextView channelName;
    public ImageView iconView;

    /* renamed from: switch  reason: not valid java name */
    public Switch f948switch;

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
    public AppControlView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "c");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
    }

    public final ImageView getIconView() {
        ImageView imageView = this.iconView;
        if (imageView != null) {
            return imageView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("iconView");
        return null;
    }

    public final void setIconView(ImageView imageView) {
        Intrinsics.checkNotNullParameter(imageView, "<set-?>");
        this.iconView = imageView;
    }

    public final TextView getChannelName() {
        TextView textView = this.channelName;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("channelName");
        return null;
    }

    public final void setChannelName(TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.channelName = textView;
    }

    public final Switch getSwitch() {
        Switch switchR = this.f948switch;
        if (switchR != null) {
            return switchR;
        }
        Intrinsics.throwUninitializedPropertyAccessException("switch");
        return null;
    }

    public final void setSwitch(Switch switchR) {
        Intrinsics.checkNotNullParameter(switchR, "<set-?>");
        this.f948switch = switchR;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        View findViewById = findViewById(C1893R.C1897id.icon);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.icon)");
        setIconView((ImageView) findViewById);
        View findViewById2 = findViewById(C1893R.C1897id.app_name);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.app_name)");
        setChannelName((TextView) findViewById2);
        View findViewById3 = findViewById(C1893R.C1897id.toggle);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "findViewById(R.id.toggle)");
        setSwitch((Switch) findViewById3);
        setOnClickListener(new AppControlView$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: onFinishInflate$lambda-0  reason: not valid java name */
    public static final void m3133onFinishInflate$lambda0(AppControlView appControlView, View view) {
        Intrinsics.checkNotNullParameter(appControlView, "this$0");
        appControlView.getSwitch().toggle();
    }
}
