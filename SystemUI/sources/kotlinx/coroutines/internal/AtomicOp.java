package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlinx.coroutines.DebugKt;
import org.jetbrains.annotations.Nullable;
/* compiled from: Atomic.kt */
/* loaded from: classes2.dex */
public abstract class AtomicOp<T> extends OpDescriptor {
    private static final AtomicReferenceFieldUpdater _consensus$FU = AtomicReferenceFieldUpdater.newUpdater(AtomicOp.class, Object.class, "_consensus");
    private volatile Object _consensus;

    public abstract void complete(T t, @Nullable Object obj);

    @Nullable
    public abstract Object prepare(T t);

    public AtomicOp() {
        Object obj;
        obj = AtomicKt.NO_DECISION;
        this._consensus = obj;
    }

    public final boolean tryDecide(@Nullable Object obj) {
        Object obj2;
        Object obj3;
        if (DebugKt.getASSERTIONS_ENABLED()) {
            obj3 = AtomicKt.NO_DECISION;
            if (!(obj != obj3)) {
                throw new AssertionError();
            }
        }
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _consensus$FU;
        obj2 = AtomicKt.NO_DECISION;
        return atomicReferenceFieldUpdater.compareAndSet(this, obj2, obj);
    }

    private final Object decide(Object obj) {
        return tryDecide(obj) ? obj : this._consensus;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlinx.coroutines.internal.OpDescriptor
    @Nullable
    public final Object perform(@Nullable Object obj) {
        Object obj2;
        Object obj3 = this._consensus;
        obj2 = AtomicKt.NO_DECISION;
        if (obj3 == obj2) {
            obj3 = decide(prepare(obj));
        }
        complete(obj, obj3);
        return obj3;
    }
}
