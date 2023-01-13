package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0014J\b\u0010\r\u001a\u00020\u000eH\u0014R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX.¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo65043d2 = {"Lcom/android/systemui/biometrics/UdfpsFpmOtherView;", "Lcom/android/systemui/biometrics/UdfpsAnimationView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "fingerprintDrawable", "Lcom/android/systemui/biometrics/UdfpsFpDrawable;", "fingerprintView", "Landroid/widget/ImageView;", "getDrawable", "Lcom/android/systemui/biometrics/UdfpsDrawable;", "onFinishInflate", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: UdfpsFpmOtherView.kt */
public final class UdfpsFpmOtherView extends UdfpsAnimationView {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final UdfpsFpDrawable fingerprintDrawable;
    private ImageView fingerprintView;

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
    public UdfpsFpmOtherView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        this.fingerprintDrawable = new UdfpsFpDrawable(context);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        View findViewById = findViewById(C1894R.C1898id.udfps_fpm_other_fp_view);
        Intrinsics.checkNotNull(findViewById);
        ImageView imageView = (ImageView) findViewById;
        this.fingerprintView = imageView;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("fingerprintView");
            imageView = null;
        }
        imageView.setImageDrawable(this.fingerprintDrawable);
    }

    /* access modifiers changed from: protected */
    public UdfpsDrawable getDrawable() {
        return this.fingerprintDrawable;
    }
}
