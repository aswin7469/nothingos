package com.android.systemui.controls.management;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.p012qs.PageIndicator;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0018\u0010\u000f\u001a\u00020\n2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\tH\u0014J\u0010\u0010\u0013\u001a\u00020\n2\u0006\u0010\u0014\u001a\u00020\u0015H\u0016R&\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000e¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ManagementPageIndicator;", "Lcom/android/systemui/qs/PageIndicator;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "visibilityListener", "Lkotlin/Function1;", "", "", "getVisibilityListener", "()Lkotlin/jvm/functions/Function1;", "setVisibilityListener", "(Lkotlin/jvm/functions/Function1;)V", "onVisibilityChanged", "changedView", "Landroid/view/View;", "visibility", "setLocation", "location", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ManagementPageIndicator.kt */
public final class ManagementPageIndicator extends PageIndicator {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private Function1<? super Integer, Unit> visibilityListener;

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
    public ManagementPageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
        this.visibilityListener = ManagementPageIndicator$visibilityListener$1.INSTANCE;
    }

    public void setLocation(float f) {
        if (getLayoutDirection() == 1) {
            super.setLocation(((float) (getChildCount() - 1)) - f);
        } else {
            super.setLocation(f);
        }
    }

    public final Function1<Integer, Unit> getVisibilityListener() {
        return this.visibilityListener;
    }

    public final void setVisibilityListener(Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        this.visibilityListener = function1;
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        Intrinsics.checkNotNullParameter(view, "changedView");
        super.onVisibilityChanged(view, i);
        if (Intrinsics.areEqual((Object) view, (Object) this)) {
            this.visibilityListener.invoke(Integer.valueOf(i));
        }
    }
}
