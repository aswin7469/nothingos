package androidx.window.embedding;

import androidx.window.embedding.EmbeddingInterfaceCompat;
import androidx.window.extensions.embedding.SplitInfo;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.NotNull;

/* compiled from: EmbeddingCompat.kt */
final class EmbeddingCompat$setEmbeddingCallback$1 extends Lambda implements Function1<List<?>, Unit> {
    final /* synthetic */ EmbeddingInterfaceCompat.EmbeddingCallbackInterface $embeddingCallback;
    final /* synthetic */ EmbeddingCompat this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    EmbeddingCompat$setEmbeddingCallback$1(EmbeddingInterfaceCompat.EmbeddingCallbackInterface embeddingCallbackInterface, EmbeddingCompat embeddingCompat) {
        super(1);
        this.$embeddingCallback = embeddingCallbackInterface;
        this.this$0 = embeddingCompat;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke((List<?>) (List) obj);
        return Unit.INSTANCE;
    }

    public final void invoke(@NotNull List<?> list) {
        Intrinsics.checkNotNullParameter(list, "values");
        ArrayList arrayList = new ArrayList();
        for (Object next : list) {
            if (next instanceof SplitInfo) {
                arrayList.add(next);
            }
        }
        this.$embeddingCallback.onSplitInfoChanged(this.this$0.adapter.translate((List<? extends SplitInfo>) arrayList));
    }
}
