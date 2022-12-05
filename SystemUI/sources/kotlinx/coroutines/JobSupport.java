package kotlinx.coroutines;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.ExceptionsKt__ExceptionsKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsJvmKt;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.internal.ConcurrentKt;
import kotlinx.coroutines.internal.LockFreeLinkedListKt;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;
import kotlinx.coroutines.internal.OpDescriptor;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.Symbol;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: JobSupport.kt */
/* loaded from: classes2.dex */
public class JobSupport implements Job, ChildJob, ParentJob {
    private static final AtomicReferenceFieldUpdater _state$FU = AtomicReferenceFieldUpdater.newUpdater(JobSupport.class, Object.class, "_state");
    private volatile Object _state;
    @Nullable
    public volatile ChildHandle parentHandle;

    protected void afterCompletionInternal(@Nullable Object obj, int i) {
    }

    public boolean getHandlesException$kotlinx_coroutines_core() {
        return true;
    }

    public boolean getOnCancelComplete$kotlinx_coroutines_core() {
        return false;
    }

    protected boolean handleJobException(@NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        return false;
    }

    protected boolean isScopedCoroutine() {
        return false;
    }

    protected void onCancelling(@Nullable Throwable th) {
    }

    protected void onCompletionInternal(@Nullable Object obj) {
    }

    public void onStartInternal$kotlinx_coroutines_core() {
    }

    public JobSupport(boolean z) {
        this._state = z ? JobSupportKt.EMPTY_ACTIVE : JobSupportKt.EMPTY_NEW;
    }

    @Override // kotlin.coroutines.CoroutineContext
    public <R> R fold(R r, @NotNull Function2<? super R, ? super CoroutineContext.Element, ? extends R> operation) {
        Intrinsics.checkParameterIsNotNull(operation, "operation");
        return (R) Job.DefaultImpls.fold(this, r, operation);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    @Nullable
    public <E extends CoroutineContext.Element> E get(@NotNull CoroutineContext.Key<E> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return (E) Job.DefaultImpls.get(this, key);
    }

    @Override // kotlin.coroutines.CoroutineContext
    @NotNull
    public CoroutineContext minusKey(@NotNull CoroutineContext.Key<?> key) {
        Intrinsics.checkParameterIsNotNull(key, "key");
        return Job.DefaultImpls.minusKey(this, key);
    }

    @Override // kotlin.coroutines.CoroutineContext
    @NotNull
    public CoroutineContext plus(@NotNull CoroutineContext context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        return Job.DefaultImpls.plus(this, context);
    }

    @Override // kotlin.coroutines.CoroutineContext.Element
    @NotNull
    public final CoroutineContext.Key<?> getKey() {
        return Job.Key;
    }

    private final boolean addLastAtomic(final Object obj, NodeList nodeList, final JobNode<?> jobNode) {
        int tryCondAddNext;
        LockFreeLinkedListNode.CondAddOp condAddOp = new LockFreeLinkedListNode.CondAddOp(jobNode) { // from class: kotlinx.coroutines.JobSupport$addLastAtomic$$inlined$addLastIf$1
            @Override // kotlinx.coroutines.internal.AtomicOp
            @Nullable
            public Object prepare(@NotNull LockFreeLinkedListNode affected) {
                Intrinsics.checkParameterIsNotNull(affected, "affected");
                if (this.getState$kotlinx_coroutines_core() == obj) {
                    return null;
                }
                return LockFreeLinkedListKt.getCONDITION_FALSE();
            }
        };
        do {
            Object prev = nodeList.getPrev();
            if (prev == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
            tryCondAddNext = ((LockFreeLinkedListNode) prev).tryCondAddNext(jobNode, nodeList, condAddOp);
            if (tryCondAddNext == 1) {
                return true;
            }
        } while (tryCondAddNext != 2);
        return false;
    }

    public final void initParentJobInternal$kotlinx_coroutines_core(@Nullable Job job) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!(this.parentHandle == null)) {
                throw new AssertionError();
            }
        }
        if (job == null) {
            this.parentHandle = NonDisposableHandle.INSTANCE;
            return;
        }
        job.start();
        ChildHandle attachChild = job.attachChild(this);
        this.parentHandle = attachChild;
        if (!isCompleted()) {
            return;
        }
        attachChild.dispose();
        this.parentHandle = NonDisposableHandle.INSTANCE;
    }

    private final boolean cancelMakeCompleting(Object obj) {
        int tryMakeCompleting;
        do {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete) || (((state$kotlinx_coroutines_core instanceof Finishing) && ((Finishing) state$kotlinx_coroutines_core).isCompleting) || (tryMakeCompleting = tryMakeCompleting(state$kotlinx_coroutines_core, new CompletedExceptionally(createCauseException(obj), false, 2, null), 0)) == 0)) {
                return false;
            }
            if (tryMakeCompleting == 1 || tryMakeCompleting == 2) {
                return true;
            }
        } while (tryMakeCompleting == 3);
        throw new IllegalStateException("unexpected result".toString());
    }

    private final boolean joinInternal() {
        Object state$kotlinx_coroutines_core;
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                return false;
            }
        } while (startInternal(state$kotlinx_coroutines_core) < 0);
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0085, code lost:
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final boolean makeCancelling(Object obj) {
        Throwable th = null;
        Throwable th2 = null;
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Finishing) {
                synchronized (state$kotlinx_coroutines_core) {
                    if (((Finishing) state$kotlinx_coroutines_core).isSealed()) {
                        return false;
                    }
                    boolean isCancelling = ((Finishing) state$kotlinx_coroutines_core).isCancelling();
                    if (obj != null || !isCancelling) {
                        if (th2 == null) {
                            th2 = createCauseException(obj);
                        }
                        ((Finishing) state$kotlinx_coroutines_core).addExceptionLocked(th2);
                    }
                    Throwable th3 = ((Finishing) state$kotlinx_coroutines_core).rootCause;
                    if (!isCancelling) {
                        th = th3;
                    }
                    if (th != null) {
                        notifyCancelling(((Finishing) state$kotlinx_coroutines_core).getList(), th);
                    }
                    return true;
                }
            } else if (!(state$kotlinx_coroutines_core instanceof Incomplete)) {
                return false;
            } else {
                if (th2 == null) {
                    th2 = createCauseException(obj);
                }
                Incomplete incomplete = (Incomplete) state$kotlinx_coroutines_core;
                if (incomplete.isActive()) {
                    if (tryMakeCancelling(incomplete, th2)) {
                        return true;
                    }
                } else {
                    int tryMakeCompleting = tryMakeCompleting(state$kotlinx_coroutines_core, new CompletedExceptionally(th2, false, 2, null), 0);
                    if (tryMakeCompleting == 0) {
                        throw new IllegalStateException(("Cannot happen in " + state$kotlinx_coroutines_core).toString());
                    } else if (tryMakeCompleting == 1 || tryMakeCompleting == 2) {
                        break;
                    } else if (tryMakeCompleting != 3) {
                        throw new IllegalStateException("unexpected result".toString());
                    }
                }
            }
        }
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final DisposableHandle invokeOnCompletion(boolean z, boolean z2, @NotNull Function1<? super Throwable, Unit> handler) {
        Throwable th;
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        Throwable th2 = null;
        JobNode<?> jobNode = null;
        while (true) {
            Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (state$kotlinx_coroutines_core instanceof Empty) {
                Empty empty = (Empty) state$kotlinx_coroutines_core;
                if (empty.isActive()) {
                    if (jobNode == null) {
                        jobNode = makeNode(handler, z);
                    }
                    if (_state$FU.compareAndSet(this, state$kotlinx_coroutines_core, jobNode)) {
                        return jobNode;
                    }
                } else {
                    promoteEmptyToNodeList(empty);
                }
            } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
                NodeList list = ((Incomplete) state$kotlinx_coroutines_core).getList();
                if (list != null) {
                    DisposableHandle disposableHandle = NonDisposableHandle.INSTANCE;
                    if (!z || !(state$kotlinx_coroutines_core instanceof Finishing)) {
                        th = null;
                    } else {
                        synchronized (state$kotlinx_coroutines_core) {
                            th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
                            if (th == null || ((handler instanceof ChildHandleNode) && !((Finishing) state$kotlinx_coroutines_core).isCompleting)) {
                                if (jobNode == null) {
                                    jobNode = makeNode(handler, z);
                                }
                                if (addLastAtomic(state$kotlinx_coroutines_core, list, jobNode)) {
                                    if (th == null) {
                                        return jobNode;
                                    }
                                    disposableHandle = jobNode;
                                }
                            }
                            Unit unit = Unit.INSTANCE;
                        }
                    }
                    if (th != null) {
                        if (z2) {
                            handler.mo1949invoke(th);
                        }
                        return disposableHandle;
                    }
                    if (jobNode == null) {
                        jobNode = makeNode(handler, z);
                    }
                    if (addLastAtomic(state$kotlinx_coroutines_core, list, jobNode)) {
                        return jobNode;
                    }
                } else if (state$kotlinx_coroutines_core == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.JobNode<*>");
                } else {
                    promoteSingleToNodeList((JobNode) state$kotlinx_coroutines_core);
                }
            } else {
                if (z2) {
                    if (!(state$kotlinx_coroutines_core instanceof CompletedExceptionally)) {
                        state$kotlinx_coroutines_core = null;
                    }
                    CompletedExceptionally completedExceptionally = (CompletedExceptionally) state$kotlinx_coroutines_core;
                    if (completedExceptionally != null) {
                        th2 = completedExceptionally.cause;
                    }
                    handler.mo1949invoke(th2);
                }
                return NonDisposableHandle.INSTANCE;
            }
        }
    }

    public final boolean makeCompletingOnce$kotlinx_coroutines_core(@Nullable Object obj, int i) {
        int tryMakeCompleting;
        do {
            tryMakeCompleting = tryMakeCompleting(getState$kotlinx_coroutines_core(), obj, i);
            if (tryMakeCompleting == 0) {
                throw new IllegalStateException("Job " + this + " is already complete or completing, but is being completed with " + obj, getExceptionOrNull(obj));
            } else if (tryMakeCompleting == 1) {
                return true;
            } else {
                if (tryMakeCompleting == 2) {
                    return false;
                }
            }
        } while (tryMakeCompleting == 3);
        throw new IllegalStateException("unexpected result".toString());
    }

    public final void removeNode$kotlinx_coroutines_core(@NotNull JobNode<?> node) {
        Object state$kotlinx_coroutines_core;
        AtomicReferenceFieldUpdater atomicReferenceFieldUpdater;
        Empty empty;
        Intrinsics.checkParameterIsNotNull(node, "node");
        do {
            state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
            if (!(state$kotlinx_coroutines_core instanceof JobNode)) {
                if (!(state$kotlinx_coroutines_core instanceof Incomplete) || ((Incomplete) state$kotlinx_coroutines_core).getList() == null) {
                    return;
                }
                node.remove();
                return;
            } else if (state$kotlinx_coroutines_core != node) {
                return;
            } else {
                atomicReferenceFieldUpdater = _state$FU;
                empty = JobSupportKt.EMPTY_ACTIVE;
            }
        } while (!atomicReferenceFieldUpdater.compareAndSet(this, state$kotlinx_coroutines_core, empty));
    }

    @Override // kotlinx.coroutines.Job
    public final boolean start() {
        int startInternal;
        do {
            startInternal = startInternal(getState$kotlinx_coroutines_core());
            if (startInternal == 0) {
                return false;
            }
        } while (startInternal != 1);
        return true;
    }

    @Override // kotlinx.coroutines.Job
    public boolean isActive() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        return (state$kotlinx_coroutines_core instanceof Incomplete) && ((Incomplete) state$kotlinx_coroutines_core).isActive();
    }

    public final boolean isCompleted() {
        return !(getState$kotlinx_coroutines_core() instanceof Incomplete);
    }

    @Nullable
    final /* synthetic */ Object joinSuspend(@NotNull Continuation<? super Unit> continuation) {
        Continuation intercepted;
        Object coroutine_suspended;
        intercepted = IntrinsicsKt__IntrinsicsJvmKt.intercepted(continuation);
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(intercepted, 1);
        CancellableContinuationKt.disposeOnCancellation(cancellableContinuationImpl, invokeOnCompletion(new ResumeOnCompletion(this, cancellableContinuationImpl)));
        Object result = cancellableContinuationImpl.getResult();
        coroutine_suspended = IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (result == coroutine_suspended) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    private final boolean tryFinalizeFinishingState(Finishing finishing, Object obj, int i) {
        boolean isCancelling;
        Throwable finalRootCause;
        boolean z = false;
        if (!(getState$kotlinx_coroutines_core() == finishing)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!(!finishing.isSealed())) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        if (!finishing.isCompleting) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj instanceof CompletedExceptionally) ? null : obj);
        Throwable th = completedExceptionally != null ? completedExceptionally.cause : null;
        synchronized (finishing) {
            isCancelling = finishing.isCancelling();
            List<Throwable> sealLocked = finishing.sealLocked(th);
            finalRootCause = getFinalRootCause(finishing, sealLocked);
            if (finalRootCause != null) {
                addSuppressedExceptions(finalRootCause, sealLocked);
            }
        }
        if (finalRootCause != null && finalRootCause != th) {
            obj = new CompletedExceptionally(finalRootCause, false, 2, null);
        }
        if (finalRootCause != null) {
            if (cancelParent(finalRootCause) || handleJobException(finalRootCause)) {
                z = true;
            }
            if (z) {
                if (obj == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.CompletedExceptionally");
                }
                ((CompletedExceptionally) obj).makeHandled();
            }
        }
        if (!isCancelling) {
            onCancelling(finalRootCause);
        }
        onCompletionInternal(obj);
        if (!_state$FU.compareAndSet(this, finishing, JobSupportKt.boxIncomplete(obj))) {
            throw new IllegalArgumentException(("Unexpected state: " + this._state + ", expected: " + finishing + ", update: " + obj).toString());
        }
        completeStateFinalization(finishing, obj, i);
        return true;
    }

    private final Throwable getFinalRootCause(Finishing finishing, List<? extends Throwable> list) {
        Object obj = null;
        if (!list.isEmpty()) {
            Iterator<T> it = list.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Object next = it.next();
                if (!(((Throwable) next) instanceof CancellationException)) {
                    obj = next;
                    break;
                }
            }
            Throwable th = (Throwable) obj;
            return th != null ? th : list.get(0);
        } else if (!finishing.isCancelling()) {
            return null;
        } else {
            return createJobCancellationException();
        }
    }

    private final void addSuppressedExceptions(Throwable th, List<? extends Throwable> list) {
        if (list.size() <= 1) {
            return;
        }
        Set identitySet = ConcurrentKt.identitySet(list.size());
        Throwable unwrap = StackTraceRecoveryKt.unwrap(th);
        for (Throwable th2 : list) {
            Throwable unwrap2 = StackTraceRecoveryKt.unwrap(th2);
            if (unwrap2 != th && unwrap2 != unwrap && !(unwrap2 instanceof CancellationException) && identitySet.add(unwrap2)) {
                ExceptionsKt__ExceptionsKt.addSuppressed(th, unwrap2);
            }
        }
    }

    private final boolean tryFinalizeSimpleState(Incomplete incomplete, Object obj, int i) {
        if (DebugKt.getASSERTIONS_ENABLED()) {
            if (!((incomplete instanceof Empty) || (incomplete instanceof JobNode))) {
                throw new AssertionError();
            }
        }
        if (!DebugKt.getASSERTIONS_ENABLED() || (!(obj instanceof CompletedExceptionally))) {
            if (!_state$FU.compareAndSet(this, incomplete, JobSupportKt.boxIncomplete(obj))) {
                return false;
            }
            onCancelling(null);
            onCompletionInternal(obj);
            completeStateFinalization(incomplete, obj, i);
            return true;
        }
        throw new AssertionError();
    }

    private final void completeStateFinalization(Incomplete incomplete, Object obj, int i) {
        ChildHandle childHandle = this.parentHandle;
        if (childHandle != null) {
            childHandle.dispose();
            this.parentHandle = NonDisposableHandle.INSTANCE;
        }
        Throwable th = null;
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj instanceof CompletedExceptionally) ? null : obj);
        if (completedExceptionally != null) {
            th = completedExceptionally.cause;
        }
        if (incomplete instanceof JobNode) {
            try {
                ((JobNode) incomplete).invoke(th);
            } catch (Throwable th2) {
                handleOnCompletionException$kotlinx_coroutines_core(new CompletionHandlerException("Exception in completion handler " + incomplete + " for " + this, th2));
            }
        } else {
            NodeList list = incomplete.getList();
            if (list != null) {
                notifyCompletion(list, th);
            }
        }
        afterCompletionInternal(obj, i);
    }

    private final void notifyCancelling(NodeList nodeList, Throwable th) {
        onCancelling(th);
        Object next = nodeList.getNext();
        if (next == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
        }
        CompletionHandlerException completionHandlerException = null;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual(lockFreeLinkedListNode, nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobCancellingNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(th);
                } catch (Throwable th2) {
                    if (completionHandlerException != null) {
                        ExceptionsKt__ExceptionsKt.addSuppressed(completionHandlerException, th2);
                    } else {
                        completionHandlerException = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th2);
                        Unit unit = Unit.INSTANCE;
                    }
                }
            }
        }
        if (completionHandlerException != null) {
            handleOnCompletionException$kotlinx_coroutines_core(completionHandlerException);
        }
        cancelParent(th);
    }

    private final boolean cancelParent(Throwable th) {
        if (isScopedCoroutine()) {
            return true;
        }
        boolean z = th instanceof CancellationException;
        ChildHandle childHandle = this.parentHandle;
        return (childHandle == null || childHandle == NonDisposableHandle.INSTANCE) ? z : childHandle.childCancelled(th) || z;
    }

    private final int startInternal(Object obj) {
        Empty empty;
        if (obj instanceof Empty) {
            if (((Empty) obj).isActive()) {
                return 0;
            }
            AtomicReferenceFieldUpdater atomicReferenceFieldUpdater = _state$FU;
            empty = JobSupportKt.EMPTY_ACTIVE;
            if (!atomicReferenceFieldUpdater.compareAndSet(this, obj, empty)) {
                return -1;
            }
            onStartInternal$kotlinx_coroutines_core();
            return 1;
        } else if (!(obj instanceof InactiveNodeList)) {
            return 0;
        } else {
            if (!_state$FU.compareAndSet(this, obj, ((InactiveNodeList) obj).getList())) {
                return -1;
            }
            onStartInternal$kotlinx_coroutines_core();
            return 1;
        }
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final CancellationException getCancellationException() {
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            Throwable th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
            if (th != null) {
                CancellationException cancellationException = toCancellationException(th, DebugStringsKt.getClassSimpleName(this) + " is cancelling");
                if (cancellationException != null) {
                    return cancellationException;
                }
            }
            throw new IllegalStateException(("Job is still new or active: " + this).toString());
        } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
            throw new IllegalStateException(("Job is still new or active: " + this).toString());
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            return toCancellationException$default(this, ((CompletedExceptionally) state$kotlinx_coroutines_core).cause, null, 1, null);
        } else {
            return new JobCancellationException(DebugStringsKt.getClassSimpleName(this) + " has completed normally", null, this);
        }
    }

    public static /* synthetic */ CancellationException toCancellationException$default(JobSupport jobSupport, Throwable th, String str, int i, Object obj) {
        if (obj == null) {
            if ((i & 1) != 0) {
                str = null;
            }
            return jobSupport.toCancellationException(th, str);
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: toCancellationException");
    }

    @NotNull
    protected final CancellationException toCancellationException(@NotNull Throwable toCancellationException, @Nullable String str) {
        Intrinsics.checkParameterIsNotNull(toCancellationException, "$this$toCancellationException");
        CancellationException cancellationException = (CancellationException) (!(toCancellationException instanceof CancellationException) ? null : toCancellationException);
        if (cancellationException == null) {
            if (str == null) {
                str = DebugStringsKt.getClassSimpleName(toCancellationException) + " was cancelled";
            }
            cancellationException = new JobCancellationException(str, toCancellationException, this);
        }
        return cancellationException;
    }

    @NotNull
    public final DisposableHandle invokeOnCompletion(@NotNull Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        return invokeOnCompletion(false, true, handler);
    }

    private final JobNode<?> makeNode(Function1<? super Throwable, Unit> function1, boolean z) {
        boolean z2 = true;
        JobCancellingNode jobCancellingNode = null;
        if (z) {
            if (function1 instanceof JobCancellingNode) {
                jobCancellingNode = function1;
            }
            JobCancellingNode jobCancellingNode2 = jobCancellingNode;
            if (jobCancellingNode2 == null) {
                return new InvokeOnCancelling(this, function1);
            }
            if (jobCancellingNode2.job != this) {
                z2 = false;
            }
            if (!z2) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            return jobCancellingNode2;
        }
        if (function1 instanceof JobNode) {
            jobCancellingNode = function1;
        }
        JobNode<?> jobNode = jobCancellingNode;
        if (jobNode == null) {
            return new InvokeOnCompletion(this, function1);
        }
        if (jobNode.job != this || (jobNode instanceof JobCancellingNode)) {
            z2 = false;
        }
        if (!z2) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        return jobNode;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v2, types: [kotlinx.coroutines.InactiveNodeList] */
    private final void promoteEmptyToNodeList(Empty empty) {
        NodeList nodeList = new NodeList();
        if (!empty.isActive()) {
            nodeList = new InactiveNodeList(nodeList);
        }
        _state$FU.compareAndSet(this, empty, nodeList);
    }

    private final void promoteSingleToNodeList(JobNode<?> jobNode) {
        jobNode.addOneIfEmpty(new NodeList());
        _state$FU.compareAndSet(this, jobNode, jobNode.getNextNode());
    }

    @Override // kotlinx.coroutines.Job
    @Nullable
    public final Object join(@NotNull Continuation<? super Unit> continuation) {
        if (!joinInternal()) {
            YieldKt.checkCompletion(continuation.getContext());
            return Unit.INSTANCE;
        }
        return joinSuspend(continuation);
    }

    @Override // kotlinx.coroutines.Job
    public void cancel(@Nullable CancellationException cancellationException) {
        cancelInternal(cancellationException);
    }

    public boolean cancelInternal(@Nullable Throwable th) {
        return cancelImpl$kotlinx_coroutines_core(th) && getHandlesException$kotlinx_coroutines_core();
    }

    @Override // kotlinx.coroutines.ChildJob
    public final void parentCancelled(@NotNull ParentJob parentJob) {
        Intrinsics.checkParameterIsNotNull(parentJob, "parentJob");
        cancelImpl$kotlinx_coroutines_core(parentJob);
    }

    public boolean childCancelled(@NotNull Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        if (cause instanceof CancellationException) {
            return true;
        }
        return cancelImpl$kotlinx_coroutines_core(cause) && getHandlesException$kotlinx_coroutines_core();
    }

    public final boolean cancelImpl$kotlinx_coroutines_core(@Nullable Object obj) {
        if (!getOnCancelComplete$kotlinx_coroutines_core() || !cancelMakeCompleting(obj)) {
            return makeCancelling(obj);
        }
        return true;
    }

    private final void notifyCompletion(@NotNull NodeList nodeList, Throwable th) {
        Object next = nodeList.getNext();
        if (next == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
        }
        CompletionHandlerException completionHandlerException = null;
        for (LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode) next; !Intrinsics.areEqual(lockFreeLinkedListNode, nodeList); lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode()) {
            if (lockFreeLinkedListNode instanceof JobNode) {
                JobNode jobNode = (JobNode) lockFreeLinkedListNode;
                try {
                    jobNode.invoke(th);
                } catch (Throwable th2) {
                    if (completionHandlerException != null) {
                        ExceptionsKt__ExceptionsKt.addSuppressed(completionHandlerException, th2);
                    } else {
                        completionHandlerException = new CompletionHandlerException("Exception in completion handler " + jobNode + " for " + this, th2);
                        Unit unit = Unit.INSTANCE;
                    }
                }
            }
        }
        if (completionHandlerException == null) {
            return;
        }
        handleOnCompletionException$kotlinx_coroutines_core(completionHandlerException);
    }

    private final JobCancellationException createJobCancellationException() {
        return new JobCancellationException("Job was cancelled", null, this);
    }

    @Override // kotlinx.coroutines.ParentJob
    @NotNull
    public CancellationException getChildJobCancellationCause() {
        Throwable th;
        Object state$kotlinx_coroutines_core = getState$kotlinx_coroutines_core();
        CancellationException cancellationException = null;
        if (state$kotlinx_coroutines_core instanceof Finishing) {
            th = ((Finishing) state$kotlinx_coroutines_core).rootCause;
        } else if (state$kotlinx_coroutines_core instanceof CompletedExceptionally) {
            th = ((CompletedExceptionally) state$kotlinx_coroutines_core).cause;
        } else if (state$kotlinx_coroutines_core instanceof Incomplete) {
            throw new IllegalStateException(("Cannot be cancelling child in this state: " + state$kotlinx_coroutines_core).toString());
        } else {
            th = null;
        }
        if (th instanceof CancellationException) {
            cancellationException = th;
        }
        CancellationException cancellationException2 = cancellationException;
        if (cancellationException2 != null) {
            return cancellationException2;
        }
        return new JobCancellationException("Parent job is " + stateString(state$kotlinx_coroutines_core), th, this);
    }

    private final Throwable createCauseException(Object obj) {
        if (obj != null ? obj instanceof Throwable : true) {
            return obj != null ? (Throwable) obj : createJobCancellationException();
        } else if (obj == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ParentJob");
        } else {
            return ((ParentJob) obj).getChildJobCancellationCause();
        }
    }

    private final NodeList getOrPromoteCancellingList(Incomplete incomplete) {
        NodeList list = incomplete.getList();
        if (list != null) {
            return list;
        }
        if (incomplete instanceof Empty) {
            return new NodeList();
        }
        if (incomplete instanceof JobNode) {
            promoteSingleToNodeList((JobNode) incomplete);
            return null;
        }
        throw new IllegalStateException(("State should have list: " + incomplete).toString());
    }

    private final boolean tryMakeCancelling(Incomplete incomplete, Throwable th) {
        if (!DebugKt.getASSERTIONS_ENABLED() || (!(incomplete instanceof Finishing))) {
            if (DebugKt.getASSERTIONS_ENABLED() && !incomplete.isActive()) {
                throw new AssertionError();
            }
            NodeList orPromoteCancellingList = getOrPromoteCancellingList(incomplete);
            if (orPromoteCancellingList == null) {
                return false;
            }
            if (!_state$FU.compareAndSet(this, incomplete, new Finishing(orPromoteCancellingList, false, th))) {
                return false;
            }
            notifyCancelling(orPromoteCancellingList, th);
            return true;
        }
        throw new AssertionError();
    }

    private final int tryMakeCompleting(Object obj, Object obj2, int i) {
        if (!(obj instanceof Incomplete)) {
            return 0;
        }
        if ((!(obj instanceof Empty) && !(obj instanceof JobNode)) || (obj instanceof ChildHandleNode) || (obj2 instanceof CompletedExceptionally)) {
            return tryMakeCompletingSlowPath((Incomplete) obj, obj2, i);
        }
        return !tryFinalizeSimpleState((Incomplete) obj, obj2, i) ? 3 : 1;
    }

    private final int tryMakeCompletingSlowPath(Incomplete incomplete, Object obj, int i) {
        NodeList orPromoteCancellingList = getOrPromoteCancellingList(incomplete);
        if (orPromoteCancellingList != null) {
            Throwable th = null;
            Finishing finishing = (Finishing) (!(incomplete instanceof Finishing) ? null : incomplete);
            if (finishing == null) {
                finishing = new Finishing(orPromoteCancellingList, false, null);
            }
            synchronized (finishing) {
                if (finishing.isCompleting) {
                    return 0;
                }
                finishing.isCompleting = true;
                if (finishing != incomplete && !_state$FU.compareAndSet(this, incomplete, finishing)) {
                    return 3;
                }
                if (!(!finishing.isSealed())) {
                    throw new IllegalArgumentException("Failed requirement.".toString());
                }
                boolean isCancelling = finishing.isCancelling();
                CompletedExceptionally completedExceptionally = (CompletedExceptionally) (!(obj instanceof CompletedExceptionally) ? null : obj);
                if (completedExceptionally != null) {
                    finishing.addExceptionLocked(completedExceptionally.cause);
                }
                Throwable th2 = finishing.rootCause;
                if (!isCancelling) {
                    th = th2;
                }
                Unit unit = Unit.INSTANCE;
                if (th != null) {
                    notifyCancelling(orPromoteCancellingList, th);
                }
                ChildHandleNode firstChild = firstChild(incomplete);
                if (firstChild != null && tryWaitForChild(finishing, firstChild, obj)) {
                    return 2;
                }
                return tryFinalizeFinishingState(finishing, obj, i) ? 1 : 3;
            }
        }
        return 3;
    }

    private final Throwable getExceptionOrNull(@Nullable Object obj) {
        if (!(obj instanceof CompletedExceptionally)) {
            obj = null;
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    private final ChildHandleNode firstChild(Incomplete incomplete) {
        ChildHandleNode childHandleNode = (ChildHandleNode) (!(incomplete instanceof ChildHandleNode) ? null : incomplete);
        if (childHandleNode != null) {
            return childHandleNode;
        }
        NodeList list = incomplete.getList();
        if (list == null) {
            return null;
        }
        return nextChild(list);
    }

    private final boolean tryWaitForChild(Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
        while (Job.DefaultImpls.invokeOnCompletion$default(childHandleNode.childJob, false, false, new ChildCompletion(this, finishing, childHandleNode, obj), 1, null) == NonDisposableHandle.INSTANCE) {
            childHandleNode = nextChild(childHandleNode);
            if (childHandleNode == null) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void continueCompleting(Finishing finishing, ChildHandleNode childHandleNode, Object obj) {
        if (!(getState$kotlinx_coroutines_core() == finishing)) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        }
        ChildHandleNode nextChild = nextChild(childHandleNode);
        if (nextChild != null && tryWaitForChild(finishing, nextChild, obj)) {
            return;
        }
        tryFinalizeFinishingState(finishing, obj, 0);
    }

    private final ChildHandleNode nextChild(@NotNull LockFreeLinkedListNode lockFreeLinkedListNode) {
        while (lockFreeLinkedListNode.isRemoved()) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getPrevNode();
        }
        while (true) {
            lockFreeLinkedListNode = lockFreeLinkedListNode.getNextNode();
            if (!lockFreeLinkedListNode.isRemoved()) {
                if (lockFreeLinkedListNode instanceof ChildHandleNode) {
                    return (ChildHandleNode) lockFreeLinkedListNode;
                }
                if (lockFreeLinkedListNode instanceof NodeList) {
                    return null;
                }
            }
        }
    }

    @Override // kotlinx.coroutines.Job
    @NotNull
    public final ChildHandle attachChild(@NotNull ChildJob child) {
        Intrinsics.checkParameterIsNotNull(child, "child");
        DisposableHandle invokeOnCompletion$default = Job.DefaultImpls.invokeOnCompletion$default(this, true, false, new ChildHandleNode(this, child), 2, null);
        if (invokeOnCompletion$default != null) {
            return (ChildHandle) invokeOnCompletion$default;
        }
        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.ChildHandle");
    }

    public void handleOnCompletionException$kotlinx_coroutines_core(@NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        throw exception;
    }

    @NotNull
    public String toString() {
        return toDebugString() + '@' + DebugStringsKt.getHexAddress(this);
    }

    @NotNull
    public final String toDebugString() {
        return nameString$kotlinx_coroutines_core() + '{' + stateString(getState$kotlinx_coroutines_core()) + '}';
    }

    @NotNull
    public String nameString$kotlinx_coroutines_core() {
        return DebugStringsKt.getClassSimpleName(this);
    }

    private final String stateString(Object obj) {
        if (!(obj instanceof Finishing)) {
            return obj instanceof Incomplete ? ((Incomplete) obj).isActive() ? "Active" : "New" : obj instanceof CompletedExceptionally ? "Cancelled" : "Completed";
        }
        Finishing finishing = (Finishing) obj;
        return finishing.isCancelling() ? "Cancelling" : finishing.isCompleting ? "Completing" : "Active";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JobSupport.kt */
    /* loaded from: classes2.dex */
    public static final class Finishing implements Incomplete {
        private volatile Object _exceptionsHolder;
        public volatile boolean isCompleting;
        @NotNull
        private final NodeList list;
        @Nullable
        public volatile Throwable rootCause;

        @Override // kotlinx.coroutines.Incomplete
        @NotNull
        public NodeList getList() {
            return this.list;
        }

        public Finishing(@NotNull NodeList list, boolean z, @Nullable Throwable th) {
            Intrinsics.checkParameterIsNotNull(list, "list");
            this.list = list;
            this.isCompleting = z;
            this.rootCause = th;
        }

        public final boolean isSealed() {
            Symbol symbol;
            Object obj = this._exceptionsHolder;
            symbol = JobSupportKt.SEALED;
            return obj == symbol;
        }

        public final boolean isCancelling() {
            return this.rootCause != null;
        }

        @Override // kotlinx.coroutines.Incomplete
        public boolean isActive() {
            return this.rootCause == null;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object] */
        @NotNull
        public final List<Throwable> sealLocked(@Nullable Throwable th) {
            ArrayList<Throwable> arrayList;
            Symbol symbol;
            ?? r0 = this._exceptionsHolder;
            if (r0 == 0) {
                arrayList = allocateList();
            } else if (r0 instanceof Throwable) {
                ArrayList<Throwable> allocateList = allocateList();
                allocateList.add(r0);
                arrayList = allocateList;
            } else if (!(r0 instanceof ArrayList)) {
                throw new IllegalStateException(("State is " + ((Object) r0)).toString());
            } else {
                arrayList = (ArrayList) r0;
            }
            Throwable th2 = this.rootCause;
            if (th2 != null) {
                arrayList.add(0, th2);
            }
            if (th != null && (!Intrinsics.areEqual(th, th2))) {
                arrayList.add(th);
            }
            symbol = JobSupportKt.SEALED;
            this._exceptionsHolder = symbol;
            return arrayList;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object] */
        public final void addExceptionLocked(@NotNull Throwable exception) {
            Intrinsics.checkParameterIsNotNull(exception, "exception");
            Throwable th = this.rootCause;
            if (th == null) {
                this.rootCause = exception;
            } else if (exception == th) {
            } else {
                ?? r0 = this._exceptionsHolder;
                if (r0 == 0) {
                    this._exceptionsHolder = exception;
                } else if (r0 instanceof Throwable) {
                    if (exception == r0) {
                        return;
                    }
                    ArrayList<Throwable> allocateList = allocateList();
                    allocateList.add(r0);
                    allocateList.add(exception);
                    this._exceptionsHolder = allocateList;
                } else if (r0 instanceof ArrayList) {
                    ((ArrayList) r0).add(exception);
                } else {
                    throw new IllegalStateException(("State is " + ((Object) r0)).toString());
                }
            }
        }

        private final ArrayList<Throwable> allocateList() {
            return new ArrayList<>(4);
        }

        @NotNull
        public String toString() {
            return "Finishing[cancelling=" + isCancelling() + ", completing=" + this.isCompleting + ", rootCause=" + this.rootCause + ", exceptions=" + this._exceptionsHolder + ", list=" + getList() + ']';
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* compiled from: JobSupport.kt */
    /* loaded from: classes2.dex */
    public static final class ChildCompletion extends JobNode<Job> {
        private final ChildHandleNode child;
        private final JobSupport parent;
        private final Object proposedUpdate;
        private final Finishing state;

        @Override // kotlin.jvm.functions.Function1
        /* renamed from: invoke */
        public /* bridge */ /* synthetic */ Unit mo1949invoke(Throwable th) {
            invoke2(th);
            return Unit.INSTANCE;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ChildCompletion(@NotNull JobSupport parent, @NotNull Finishing state, @NotNull ChildHandleNode child, @Nullable Object obj) {
            super(child.childJob);
            Intrinsics.checkParameterIsNotNull(parent, "parent");
            Intrinsics.checkParameterIsNotNull(state, "state");
            Intrinsics.checkParameterIsNotNull(child, "child");
            this.parent = parent;
            this.state = state;
            this.child = child;
            this.proposedUpdate = obj;
        }

        @Override // kotlinx.coroutines.CompletionHandlerBase
        /* renamed from: invoke  reason: avoid collision after fix types in other method */
        public void invoke2(@Nullable Throwable th) {
            this.parent.continueCompleting(this.state, this.child, this.proposedUpdate);
        }

        @Override // kotlinx.coroutines.internal.LockFreeLinkedListNode
        @NotNull
        public String toString() {
            return "ChildCompletion[" + this.child + ", " + this.proposedUpdate + ']';
        }
    }

    @Nullable
    public final Object getState$kotlinx_coroutines_core() {
        while (true) {
            Object obj = this._state;
            if (!(obj instanceof OpDescriptor)) {
                return obj;
            }
            ((OpDescriptor) obj).perform(this);
        }
    }
}
