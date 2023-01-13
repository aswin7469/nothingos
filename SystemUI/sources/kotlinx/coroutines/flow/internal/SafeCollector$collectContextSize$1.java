package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\n"}, mo65043d2 = {"<anonymous>", "", "T", "count", "<anonymous parameter 1>", "Lkotlin/coroutines/CoroutineContext$Element;"}, mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: SafeCollector.kt */
final class SafeCollector$collectContextSize$1 extends Lambda implements Function2<Integer, CoroutineContext.Element, Integer> {
    public static final SafeCollector$collectContextSize$1 INSTANCE = new SafeCollector$collectContextSize$1();

    SafeCollector$collectContextSize$1() {
        super(2);
    }

    public final int invoke(int i, CoroutineContext.Element element) {
        return i + 1;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return Integer.valueOf(invoke(((Number) obj).intValue(), (CoroutineContext.Element) obj2));
    }
}
