package kotlinx.coroutines.sync;

import kotlin.Metadata;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;

@Metadata(mo64986d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\u0010\u0004\u001a\u00060\u0005j\u0002`\u0006H\u0016¨\u0006\u0007¸\u0006\u0000"}, mo64987d2 = {"kotlinx/coroutines/internal/LockFreeLinkedListNode$makeCondAddOp$1", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode$CondAddOp;", "prepare", "", "affected", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "Lkotlinx/coroutines/internal/Node;", "kotlinx-coroutines-core"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
/* renamed from: kotlinx.coroutines.sync.MutexImpl$lockSuspend$lambda-6$lambda-5$$inlined$addLastIf$1  reason: invalid class name */
/* compiled from: LockFreeLinkedList.kt */
public final class MutexImpl$lockSuspend$lambda6$lambda5$$inlined$addLastIf$1 extends LockFreeLinkedListNode.CondAddOp {
    final /* synthetic */ LockFreeLinkedListNode $node;
    final /* synthetic */ Object $state$inlined;
    final /* synthetic */ MutexImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public MutexImpl$lockSuspend$lambda6$lambda5$$inlined$addLastIf$1(LockFreeLinkedListNode lockFreeLinkedListNode, MutexImpl mutexImpl, Object obj) {
        super(lockFreeLinkedListNode);
        this.$node = lockFreeLinkedListNode;
        this.this$0 = mutexImpl;
        this.$state$inlined = obj;
    }

    public Object prepare(LockFreeLinkedListNode lockFreeLinkedListNode) {
        if (this.this$0._state == this.$state$inlined) {
            return null;
        }
        return LockFreeLinkedListKt.getCONDITION_FALSE();
    }
}
