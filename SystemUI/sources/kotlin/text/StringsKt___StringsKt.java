package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: _Strings.kt */
/* loaded from: classes2.dex */
public class StringsKt___StringsKt extends StringsKt___StringsJvmKt {
    @NotNull
    public static final String drop(@NotNull String drop, int i) {
        int coerceAtMost;
        Intrinsics.checkNotNullParameter(drop, "$this$drop");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
        }
        coerceAtMost = RangesKt___RangesKt.coerceAtMost(i, drop.length());
        String substring = drop.substring(coerceAtMost);
        Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.String).substring(startIndex)");
        return substring;
    }
}
