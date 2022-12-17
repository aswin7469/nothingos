package androidx.window.layout;

import androidx.window.extensions.layout.WindowLayoutInfo;
import androidx.window.layout.ExtensionWindowLayoutInfoBackend;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* renamed from: androidx.window.layout.ExtensionWindowLayoutInfoBackend$registerLayoutChangeCallback$1$2$disposableToken$1 */
/* compiled from: ExtensionWindowLayoutInfoBackend.kt */
final class C0457xa47eee96 extends Lambda implements Function1<WindowLayoutInfo, Unit> {
    final /* synthetic */ ExtensionWindowLayoutInfoBackend.MulticastConsumer $consumer;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    C0457xa47eee96(ExtensionWindowLayoutInfoBackend.MulticastConsumer multicastConsumer) {
        super(1);
        this.$consumer = multicastConsumer;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((WindowLayoutInfo) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull WindowLayoutInfo windowLayoutInfo) {
        Intrinsics.checkNotNullParameter(windowLayoutInfo, "value");
        this.$consumer.accept(windowLayoutInfo);
    }
}
