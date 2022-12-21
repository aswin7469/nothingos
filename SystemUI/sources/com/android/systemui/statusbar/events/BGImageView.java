package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J(\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\rH\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\t¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/statusbar/events/BGImageView;", "Landroid/widget/ImageView;", "Lcom/android/systemui/statusbar/events/BackgroundAnimatableView;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "view", "Landroid/view/View;", "getView", "()Landroid/view/View;", "setBoundsForAnimation", "", "l", "", "t", "r", "b", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusEvent.kt */
public final class BGImageView extends ImageView implements BackgroundAnimatableView {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();

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
    public BGImageView(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public View getView() {
        return this;
    }

    public void setBoundsForAnimation(int i, int i2, int i3, int i4) {
        setLeftTopRightBottom(i, i2, i3, i4);
    }
}
