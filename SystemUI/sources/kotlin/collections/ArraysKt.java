package kotlin.collections;

import java.util.List;
import java.util.Set;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes2.dex */
public final class ArraysKt extends ArraysKt___ArraysKt {
    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> asList(@NotNull T[] tArr) {
        return ArraysKt___ArraysJvmKt.asList(tArr);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Sequence<T> asSequence(@NotNull T[] tArr) {
        return ArraysKt___ArraysKt.asSequence(tArr);
    }

    public static /* bridge */ /* synthetic */ <T> boolean contains(@NotNull T[] tArr, T t) {
        return ArraysKt___ArraysKt.contains(tArr, t);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ byte[] copyInto(@NotNull byte[] bArr, @NotNull byte[] bArr2, int i, int i2, int i3) {
        return ArraysKt___ArraysJvmKt.copyInto(bArr, bArr2, i, i2, i3);
    }

    public static /* bridge */ /* synthetic */ byte[] copyInto$default(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, Object obj) {
        return ArraysKt___ArraysJvmKt.copyInto$default(bArr, bArr2, i, i2, i3, i4, obj);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> filterNotNull(@NotNull T[] tArr) {
        return ArraysKt___ArraysKt.filterNotNull(tArr);
    }

    public static /* bridge */ /* synthetic */ char single(@NotNull char[] cArr) {
        return ArraysKt___ArraysKt.single(cArr);
    }

    @Nullable
    public static /* bridge */ /* synthetic */ <T> T singleOrNull(@NotNull T[] tArr) {
        return (T) ArraysKt___ArraysKt.singleOrNull(tArr);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> List<T> toMutableList(@NotNull T[] tArr) {
        return ArraysKt___ArraysKt.toMutableList(tArr);
    }

    @NotNull
    public static /* bridge */ /* synthetic */ <T> Set<T> toSet(@NotNull T[] tArr) {
        return ArraysKt___ArraysKt.toSet(tArr);
    }
}
