package com.android.keyguard;

import android.util.MathUtils;
import kotlin.Metadata;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0001H\n¢\u0006\u0004\b\u0006\u0010\u0007"}, mo64987d2 = {"<anonymous>", "", "tag", "", "startValue", "endValue", "invoke", "(Ljava/lang/String;Ljava/lang/Float;Ljava/lang/Float;)Ljava/lang/Float;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: FontInterpolator.kt */
final class FontInterpolator$lerp$newAxes$1 extends Lambda implements Function3<String, Float, Float, Float> {
    final /* synthetic */ float $progress;
    final /* synthetic */ FontInterpolator this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    FontInterpolator$lerp$newAxes$1(FontInterpolator fontInterpolator, float f) {
        super(3);
        this.this$0 = fontInterpolator;
        this.$progress = f;
    }

    public final Float invoke(String str, Float f, Float f2) {
        float f3;
        Intrinsics.checkNotNullParameter(str, "tag");
        if (Intrinsics.areEqual((Object) str, (Object) "wght")) {
            FontInterpolator fontInterpolator = this.this$0;
            float f4 = 400.0f;
            float floatValue = f != null ? f.floatValue() : 400.0f;
            if (f2 != null) {
                f4 = f2.floatValue();
            }
            f3 = fontInterpolator.adjustWeight(MathUtils.lerp(floatValue, f4, this.$progress));
        } else if (Intrinsics.areEqual((Object) str, (Object) "ital")) {
            FontInterpolator fontInterpolator2 = this.this$0;
            float f5 = 0.0f;
            float floatValue2 = f != null ? f.floatValue() : 0.0f;
            if (f2 != null) {
                f5 = f2.floatValue();
            }
            f3 = fontInterpolator2.adjustItalic(MathUtils.lerp(floatValue2, f5, this.$progress));
        } else {
            if ((f == null || f2 == null) ? false : true) {
                f3 = MathUtils.lerp(f.floatValue(), f2.floatValue(), this.$progress);
            } else {
                throw new IllegalArgumentException(("Unable to interpolate due to unknown default axes value : " + str).toString());
            }
        }
        return Float.valueOf(f3);
    }
}
