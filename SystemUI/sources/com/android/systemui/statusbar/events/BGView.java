package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.View;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J(\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000f\u001a\u00020\fH\u0016R\u0014\u0010\u0006\u001a\u00020\u00018VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u0010"}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/BGView;", "Landroid/view/View;", "Lcom/android/systemui/statusbar/events/BackgroundAnimatableView;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "view", "getView", "()Landroid/view/View;", "setBoundsForAnimation", "", "l", "", "t", "r", "b", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusEvent.kt */
public final class BGView extends View implements BackgroundAnimatableView {
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
    public BGView(Context context) {
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
