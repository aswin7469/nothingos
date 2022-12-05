package kotlinx.coroutines.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: LockFreeTaskQueue.kt */
/* loaded from: classes2.dex */
public class LockFreeTaskQueue<E> {
    public static final /* synthetic */ AtomicReferenceFieldUpdater _cur$FU$internal = AtomicReferenceFieldUpdater.newUpdater(LockFreeTaskQueue.class, Object.class, "_cur$internal");
    public volatile /* synthetic */ Object _cur$internal;

    public LockFreeTaskQueue(boolean z) {
        this._cur$internal = new LockFreeTaskQueueCore(8, z);
    }

    public final int getSize() {
        return ((LockFreeTaskQueueCore) this._cur$internal).getSize();
    }

    public final void close() {
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            if (lockFreeTaskQueueCore.close()) {
                return;
            }
            _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
        }
    }

    public final boolean addLast(@NotNull E element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            int addLast = lockFreeTaskQueueCore.addLast(element);
            if (addLast != 0) {
                if (addLast == 1) {
                    _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
                } else if (addLast == 2) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x007c, code lost:
        r7 = (E) r9;
     */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final E removeFirstOrNull() {
        E e;
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            while (true) {
                long j = lockFreeTaskQueueCore._state$internal;
                e = null;
                if ((1152921504606846976L & j) != 0) {
                    e = (E) LockFreeTaskQueueCore.REMOVE_FROZEN;
                    break;
                }
                LockFreeTaskQueueCore.Companion companion = LockFreeTaskQueueCore.Companion;
                int i = (int) ((1073741823 & j) >> 0);
                if ((((int) ((1152921503533105152L & j) >> 30)) & lockFreeTaskQueueCore.mask) == (lockFreeTaskQueueCore.mask & i)) {
                    break;
                }
                Object obj = lockFreeTaskQueueCore.array$internal.get(lockFreeTaskQueueCore.mask & i);
                if (obj == null) {
                    if (lockFreeTaskQueueCore.singleConsumer) {
                        break;
                    }
                } else if (!(obj instanceof LockFreeTaskQueueCore.Placeholder)) {
                    int i2 = (i + 1) & 1073741823;
                    if (LockFreeTaskQueueCore._state$FU$internal.compareAndSet(lockFreeTaskQueueCore, j, companion.updateHead(j, i2))) {
                        lockFreeTaskQueueCore.array$internal.set(lockFreeTaskQueueCore.mask & i, null);
                        break;
                    } else if (lockFreeTaskQueueCore.singleConsumer) {
                        LockFreeTaskQueueCore lockFreeTaskQueueCore2 = lockFreeTaskQueueCore;
                        do {
                            lockFreeTaskQueueCore2 = lockFreeTaskQueueCore2.removeSlowPath(i, i2);
                        } while (lockFreeTaskQueueCore2 != null);
                        break;
                    }
                } else {
                    break;
                }
            }
            if (e != LockFreeTaskQueueCore.REMOVE_FROZEN) {
                return e;
            }
            _cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
        }
    }
}
