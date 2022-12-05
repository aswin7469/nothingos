package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: StringNumberConversionsJVM.kt */
/* loaded from: classes2.dex */
public class StringsKt__StringNumberConversionsJVMKt extends StringsKt__StringBuilderKt {
    @Nullable
    public static Float toFloatOrNull(@NotNull String toFloatOrNull) {
        Intrinsics.checkNotNullParameter(toFloatOrNull, "$this$toFloatOrNull");
        try {
            if (!ScreenFloatValueRegEx.value.matches(toFloatOrNull)) {
                return null;
            }
            return Float.valueOf(Float.parseFloat(toFloatOrNull));
        } catch (NumberFormatException unused) {
            return null;
        }
    }
}
