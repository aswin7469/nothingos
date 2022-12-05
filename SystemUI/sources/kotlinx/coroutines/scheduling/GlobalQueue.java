package kotlinx.coroutines.scheduling;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeTaskQueue;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: Tasks.kt */
/* loaded from: classes2.dex */
public class GlobalQueue extends LockFreeTaskQueue<Task> {
    public GlobalQueue() {
        super(false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x008e, code lost:
        r7 = r9;
     */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Task removeFirstWithModeOrNull(@NotNull TaskMode mode) {
        Object obj;
        Intrinsics.checkParameterIsNotNull(mode, "mode");
        while (true) {
            LockFreeTaskQueueCore lockFreeTaskQueueCore = (LockFreeTaskQueueCore) this._cur$internal;
            while (true) {
                long j = lockFreeTaskQueueCore._state$internal;
                obj = null;
                if ((1152921504606846976L & j) != 0) {
                    obj = LockFreeTaskQueueCore.REMOVE_FROZEN;
                    break;
                }
                LockFreeTaskQueueCore.Companion companion = LockFreeTaskQueueCore.Companion;
                boolean z = false;
                int i = (int) ((1073741823 & j) >> 0);
                if ((((int) ((1152921503533105152L & j) >> 30)) & lockFreeTaskQueueCore.mask) == (lockFreeTaskQueueCore.mask & i)) {
                    break;
                }
                Object obj2 = lockFreeTaskQueueCore.array$internal.get(lockFreeTaskQueueCore.mask & i);
                if (obj2 == null) {
                    if (lockFreeTaskQueueCore.singleConsumer) {
                        break;
                    }
                } else if (obj2 instanceof LockFreeTaskQueueCore.Placeholder) {
                    break;
                } else {
                    if (((Task) obj2).getMode() == mode) {
                        z = true;
                    }
                    if (z) {
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
            }
            if (obj == LockFreeTaskQueueCore.REMOVE_FROZEN) {
                LockFreeTaskQueue._cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
            } else {
                return (Task) obj;
            }
        }
    }
}
