package kotlin.comparisons;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _ComparisonsJvm.kt */
/* loaded from: classes2.dex */
public class ComparisonsKt___ComparisonsJvmKt extends ComparisonsKt__ComparisonsKt {
    public static float maxOf(float f, @NotNull float... other) {
        Intrinsics.checkNotNullParameter(other, "other");
        for (float f2 : other) {
            f = Math.max(f, f2);
        }
        return f;
    }
}
