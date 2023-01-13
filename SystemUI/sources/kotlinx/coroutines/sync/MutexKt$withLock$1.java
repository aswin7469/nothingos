package kotlinx.coroutines.sync;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function0;

@Metadata(mo65044k = 3, mo65045mv = {1, 5, 1}, mo65047xi = 48)
@DebugMetadata(mo65296c = "kotlinx.coroutines.sync.MutexKt", mo65297f = "Mutex.kt", mo65298i = {}, mo65299l = {114}, mo65300m = "withLock", mo65301n = {}, mo65302s = {})
/* compiled from: Mutex.kt */
final class MutexKt$withLock$1<T> extends ContinuationImpl {
    Object L$0;
    Object L$1;
    Object L$2;
    int label;
    /* synthetic */ Object result;

    MutexKt$withLock$1(Continuation<? super MutexKt$withLock$1> continuation) {
        super(continuation);
    }

    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return MutexKt.withLock((Mutex) null, (Object) null, (Function0) null, this);
    }
}
