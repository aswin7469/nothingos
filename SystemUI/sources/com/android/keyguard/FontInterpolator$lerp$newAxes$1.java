package com.android.keyguard;

import android.util.MathUtils;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: FontInterpolator.kt */
/* loaded from: classes.dex */
public final class FontInterpolator$lerp$newAxes$1 extends Lambda implements Function3<String, Float, Float, Float> {
    final /* synthetic */ float $progress;
    final /* synthetic */ FontInterpolator this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public FontInterpolator$lerp$newAxes$1(FontInterpolator fontInterpolator, float f) {
        super(3);
        this.this$0 = fontInterpolator;
        this.$progress = f;
    }

    @Override // kotlin.jvm.functions.Function3
    public /* bridge */ /* synthetic */ Float invoke(String str, Float f, Float f2) {
        return Float.valueOf(invoke2(str, f, f2));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final float invoke2(@NotNull String tag, @Nullable Float f, @Nullable Float f2) {
        float adjustItalic;
        float adjustWeight;
        Intrinsics.checkNotNullParameter(tag, "tag");
        if (Intrinsics.areEqual(tag, "wght")) {
            FontInterpolator fontInterpolator = this.this$0;
            float f3 = 400.0f;
            float floatValue = f == null ? 400.0f : f.floatValue();
            if (f2 != null) {
                f3 = f2.floatValue();
            }
            adjustWeight = fontInterpolator.adjustWeight(MathUtils.lerp(floatValue, f3, this.$progress));
            return adjustWeight;
        } else if (!Intrinsics.areEqual(tag, "ital")) {
            if (!((f == null || f2 == null) ? false : true)) {
                throw new IllegalArgumentException(Intrinsics.stringPlus("Unable to interpolate due to unknown default axes value : ", tag).toString());
            }
            return MathUtils.lerp(f.floatValue(), f2.floatValue(), this.$progress);
        } else {
            FontInterpolator fontInterpolator2 = this.this$0;
            float f4 = 0.0f;
            float floatValue2 = f == null ? 0.0f : f.floatValue();
            if (f2 != null) {
                f4 = f2.floatValue();
            }
            adjustItalic = fontInterpolator2.adjustItalic(MathUtils.lerp(floatValue2, f4, this.$progress));
            return adjustItalic;
        }
    }
}
