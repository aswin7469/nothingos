package com.android.systemui.statusbar.phone.userswitcher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0010\u001a\u00020\u0011H\u0014R\u001e\u0010\t\u001a\u00020\b2\u0006\u0010\u0007\u001a\u00020\b@BX.¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u001e\u0010\r\u001a\u00020\f2\u0006\u0010\u0007\u001a\u00020\f@BX.¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f¨\u0006\u0012"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherContainer;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "<set-?>", "Landroid/widget/ImageView;", "avatar", "getAvatar", "()Landroid/widget/ImageView;", "Landroid/widget/TextView;", "text", "getText", "()Landroid/widget/TextView;", "onFinishInflate", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarUserSwitcherContainer.kt */
public final class StatusBarUserSwitcherContainer extends LinearLayout {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private ImageView avatar;
    private TextView text;

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

    public StatusBarUserSwitcherContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final TextView getText() {
        TextView textView = this.text;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("text");
        return null;
    }

    public final ImageView getAvatar() {
        ImageView imageView = this.avatar;
        if (imageView != null) {
            return imageView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("avatar");
        return null;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(C1894R.C1898id.current_user_name);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.current_user_name)");
        this.text = (TextView) findViewById;
        View findViewById2 = findViewById(C1894R.C1898id.current_user_avatar);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.current_user_avatar)");
        this.avatar = (ImageView) findViewById2;
    }
}
