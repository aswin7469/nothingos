package com.android.systemui.media;

import android.util.MathUtils;
import kotlin.Metadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "x", "sign", "invoke", "(FF)Ljava/lang/Float;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: SquigglyProgress.kt */
final class SquigglyProgress$draw$computeAmplitude$1 extends Lambda implements Function2<Float, Float, Float> {
    final /* synthetic */ float $transitionLength;
    final /* synthetic */ float $waveEnd;
    final /* synthetic */ SquigglyProgress this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    SquigglyProgress$draw$computeAmplitude$1(SquigglyProgress squigglyProgress, float f, float f2) {
        super(2);
        this.this$0 = squigglyProgress;
        this.$waveEnd = f;
        this.$transitionLength = f2;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return invoke(((Number) obj).floatValue(), ((Number) obj2).floatValue());
    }

    public final Float invoke(float f, float f2) {
        float access$getHeightFraction$p = f2 * this.this$0.heightFraction * this.this$0.getLineAmplitude();
        float f3 = this.$waveEnd;
        return Float.valueOf(access$getHeightFraction$p * MathUtils.lerpInvSat(f3, f3 - this.$transitionLength, f));
    }
}
