package kotlin.collections;

import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Sets.kt */
/* loaded from: classes2.dex */
public class SetsKt__SetsKt extends SetsKt__SetsJVMKt {
    @NotNull
    public static <T> Set<T> emptySet() {
        return EmptySet.INSTANCE;
    }

    @NotNull
    public static <T> Set<T> setOf(@NotNull T... elements) {
        Set<T> emptySet;
        Set<T> set;
        Intrinsics.checkNotNullParameter(elements, "elements");
        if (elements.length > 0) {
            set = ArraysKt___ArraysKt.toSet(elements);
            return set;
        }
        emptySet = emptySet();
        return emptySet;
    }

    @NotNull
    public static <T> Set<T> mutableSetOf(@NotNull T... elements) {
        int mapCapacity;
        Intrinsics.checkNotNullParameter(elements, "elements");
        mapCapacity = MapsKt__MapsJVMKt.mapCapacity(elements.length);
        return (Set) ArraysKt___ArraysKt.toCollection(elements, new LinkedHashSet(mapCapacity));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NotNull
    public static final <T> Set<T> optimizeReadOnlySet(@NotNull Set<? extends T> optimizeReadOnlySet) {
        Set<T> emptySet;
        Intrinsics.checkNotNullParameter(optimizeReadOnlySet, "$this$optimizeReadOnlySet");
        int size = optimizeReadOnlySet.size();
        if (size != 0) {
            return size != 1 ? optimizeReadOnlySet : SetsKt__SetsJVMKt.setOf(optimizeReadOnlySet.iterator().next());
        }
        emptySet = emptySet();
        return emptySet;
    }
}
