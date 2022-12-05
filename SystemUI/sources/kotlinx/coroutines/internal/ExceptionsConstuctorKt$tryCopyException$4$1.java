package kotlinx.coroutines.internal;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ExceptionsConstuctor.kt */
/* loaded from: classes2.dex */
final class ExceptionsConstuctorKt$tryCopyException$4$1 extends Lambda implements Function1 {
    public static final ExceptionsConstuctorKt$tryCopyException$4$1 INSTANCE = new ExceptionsConstuctorKt$tryCopyException$4$1();

    ExceptionsConstuctorKt$tryCopyException$4$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    @Nullable
    /* renamed from: invoke */
    public final Void mo1949invoke(@NotNull Throwable it) {
        Intrinsics.checkParameterIsNotNull(it, "it");
        return null;
    }
}
