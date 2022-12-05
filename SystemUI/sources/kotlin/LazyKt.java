package kotlin;

import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes2.dex */
public final class LazyKt extends LazyKt__LazyKt {
    @NotNull
    public static /* bridge */ /* synthetic */ <T> Lazy<T> lazy(@NotNull Function0<? extends T> function0) {
        return LazyKt__LazyJVMKt.lazy(function0);
    }
}
