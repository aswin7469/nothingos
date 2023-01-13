package com.android.systemui.p012qs.tileimpl;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\u001a\u001c\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\n\u0010\u0005\u001a\u00020\u0006\"\u00020\u0007H\u0002\u001a\u000e\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"TAG", "", "colorValuesHolder", "Landroid/animation/PropertyValuesHolder;", "name", "values", "", "", "constrainSquishiness", "", "squish", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.tileimpl.QSTileViewImplKt */
/* compiled from: QSTileViewImpl.kt */
public final class QSTileViewImplKt {
    private static final String TAG = "QSTileViewImpl";

    public static final float constrainSquishiness(float f) {
        return (f * 0.9f) + 0.1f;
    }

    /* access modifiers changed from: private */
    public static final PropertyValuesHolder colorValuesHolder(String str, int... iArr) {
        PropertyValuesHolder ofInt = PropertyValuesHolder.ofInt(str, Arrays.copyOf(iArr, iArr.length));
        ofInt.setEvaluator(ArgbEvaluator.getInstance());
        Intrinsics.checkNotNullExpressionValue(ofInt, "ofInt(name, *values).app…ator.getInstance())\n    }");
        return ofInt;
    }
}
