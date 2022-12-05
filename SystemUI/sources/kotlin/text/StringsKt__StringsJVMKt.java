package kotlin.text;

import java.util.Collection;
import java.util.Iterator;
import kotlin.collections.IntIterator;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: StringsJVM.kt */
/* loaded from: classes2.dex */
public class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt {
    public static /* synthetic */ String replace$default(String str, String str2, String str3, boolean z, int i, Object obj) {
        if ((i & 4) != 0) {
            z = false;
        }
        return replace(str, str2, str3, z);
    }

    @NotNull
    public static final String replace(@NotNull String replace, @NotNull String oldValue, @NotNull String newValue, boolean z) {
        int coerceAtLeast;
        Intrinsics.checkNotNullParameter(replace, "$this$replace");
        Intrinsics.checkNotNullParameter(oldValue, "oldValue");
        Intrinsics.checkNotNullParameter(newValue, "newValue");
        int i = 0;
        int indexOf = StringsKt__StringsKt.indexOf(replace, oldValue, 0, z);
        if (indexOf < 0) {
            return replace;
        }
        int length = oldValue.length();
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(length, 1);
        int length2 = (replace.length() - length) + newValue.length();
        if (length2 < 0) {
            throw new OutOfMemoryError();
        }
        StringBuilder sb = new StringBuilder(length2);
        do {
            sb.append((CharSequence) replace, i, indexOf);
            sb.append(newValue);
            i = indexOf + length;
            if (indexOf >= replace.length()) {
                break;
            }
            indexOf = StringsKt__StringsKt.indexOf(replace, oldValue, indexOf + coerceAtLeast, z);
        } while (indexOf > 0);
        sb.append((CharSequence) replace, i, replace.length());
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "stringBuilder.append(this, i, length).toString()");
        return sb2;
    }

    public static /* synthetic */ boolean startsWith$default(String str, String str2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return startsWith(str, str2, z);
    }

    public static final boolean startsWith(@NotNull String startsWith, @NotNull String prefix, boolean z) {
        Intrinsics.checkNotNullParameter(startsWith, "$this$startsWith");
        Intrinsics.checkNotNullParameter(prefix, "prefix");
        if (!z) {
            return startsWith.startsWith(prefix);
        }
        return regionMatches(startsWith, 0, prefix, 0, prefix.length(), z);
    }

    public static /* synthetic */ boolean endsWith$default(String str, String str2, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return endsWith(str, str2, z);
    }

    public static final boolean endsWith(@NotNull String endsWith, @NotNull String suffix, boolean z) {
        Intrinsics.checkNotNullParameter(endsWith, "$this$endsWith");
        Intrinsics.checkNotNullParameter(suffix, "suffix");
        if (!z) {
            return endsWith.endsWith(suffix);
        }
        return regionMatches(endsWith, endsWith.length() - suffix.length(), suffix, 0, suffix.length(), true);
    }

    public static final boolean isBlank(@NotNull CharSequence isBlank) {
        boolean z;
        Intrinsics.checkNotNullParameter(isBlank, "$this$isBlank");
        if (isBlank.length() != 0) {
            IntRange indices = StringsKt__StringsKt.getIndices(isBlank);
            if (!(indices instanceof Collection) || !((Collection) indices).isEmpty()) {
                Iterator<Integer> it = indices.iterator();
                while (it.hasNext()) {
                    if (!CharsKt__CharJVMKt.isWhitespace(isBlank.charAt(((IntIterator) it).nextInt()))) {
                        z = false;
                        break;
                    }
                }
            }
            z = true;
            if (!z) {
                return false;
            }
        }
        return true;
    }

    public static final boolean regionMatches(@NotNull String regionMatches, int i, @NotNull String other, int i2, int i3, boolean z) {
        Intrinsics.checkNotNullParameter(regionMatches, "$this$regionMatches");
        Intrinsics.checkNotNullParameter(other, "other");
        if (!z) {
            return regionMatches.regionMatches(i, other, i2, i3);
        }
        return regionMatches.regionMatches(z, i, other, i2, i3);
    }
}
