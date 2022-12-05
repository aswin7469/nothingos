package kotlin.math;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MathJVM.kt */
/* loaded from: classes2.dex */
public class MathKt__MathJVMKt extends MathKt__MathHKt {
    public static int roundToInt(float f) {
        if (!Float.isNaN(f)) {
            return Math.round(f);
        }
        throw new IllegalArgumentException("Cannot round NaN value.");
    }
}
