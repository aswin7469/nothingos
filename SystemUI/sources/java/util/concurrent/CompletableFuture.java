package java.util.concurrent;

import androidx.slice.compat.SliceProviderCompat;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompletableFuture<T> implements Future<T>, CompletionStage<T> {
    static final int ASYNC = 1;
    private static final Executor ASYNC_POOL;
    static final int NESTED = -1;
    private static final VarHandle NEXT;
    static final AltResult NIL = new AltResult((Throwable) null);
    private static final VarHandle RESULT;
    private static final VarHandle STACK;
    static final int SYNC = 0;
    private static final boolean USE_COMMON_POOL;
    volatile Object result;
    volatile Completion stack;

    public interface AsynchronousCompletionTask {
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.cleanStack():void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final void cleanStack() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.cleanStack():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.cleanStack():void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeNull():boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final boolean completeNull() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeNull():boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.completeNull():boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeRelay(java.lang.Object):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final boolean completeRelay(java.lang.Object r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeRelay(java.lang.Object):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.completeRelay(java.lang.Object):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeThrowable(java.lang.Throwable):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final boolean completeThrowable(java.lang.Throwable r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeThrowable(java.lang.Throwable):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.completeThrowable(java.lang.Throwable):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeThrowable(java.lang.Throwable, java.lang.Object):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final boolean completeThrowable(java.lang.Throwable r1, java.lang.Object r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeThrowable(java.lang.Throwable, java.lang.Object):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.completeThrowable(java.lang.Throwable, java.lang.Object):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeValue(java.lang.Object):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final boolean completeValue(T r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.completeValue(java.lang.Object):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.completeValue(java.lang.Object):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.internalComplete(java.lang.Object):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final boolean internalComplete(java.lang.Object r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.internalComplete(java.lang.Object):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.internalComplete(java.lang.Object):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.orpush(java.util.concurrent.CompletableFuture, java.util.concurrent.CompletableFuture$BiCompletion):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final void orpush(java.util.concurrent.CompletableFuture<?> r1, java.util.concurrent.CompletableFuture.BiCompletion<?, ?, ?> r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.orpush(java.util.concurrent.CompletableFuture, java.util.concurrent.CompletableFuture$BiCompletion):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.orpush(java.util.concurrent.CompletableFuture, java.util.concurrent.CompletableFuture$BiCompletion):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.postComplete():void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final void postComplete() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.postComplete():void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.postComplete():void");
    }

    public CompletableFuture<T> toCompletableFuture() {
        return this;
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.tryPushStack(java.util.concurrent.CompletableFuture$Completion):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final boolean tryPushStack(java.util.concurrent.CompletableFuture.Completion r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.tryPushStack(java.util.concurrent.CompletableFuture$Completion):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.tryPushStack(java.util.concurrent.CompletableFuture$Completion):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.unipush(java.util.concurrent.CompletableFuture$Completion):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final void unipush(java.util.concurrent.CompletableFuture.Completion r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.CompletableFuture.unipush(java.util.concurrent.CompletableFuture$Completion):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.unipush(java.util.concurrent.CompletableFuture$Completion):void");
    }

    /* access modifiers changed from: package-private */
    public final void pushStack(Completion completion) {
        do {
        } while (!tryPushStack(completion));
    }

    static final class AltResult {

        /* renamed from: ex */
        final Throwable f715ex;

        AltResult(Throwable th) {
            this.f715ex = th;
        }
    }

    static {
        Executor executor;
        Class<CompletableFuture> cls = CompletableFuture.class;
        boolean z = true;
        if (ForkJoinPool.getCommonPoolParallelism() <= 1) {
            z = false;
        }
        USE_COMMON_POOL = z;
        if (z) {
            executor = ForkJoinPool.commonPool();
        } else {
            executor = new ThreadPerTaskExecutor();
        }
        ASYNC_POOL = executor;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            RESULT = lookup.findVarHandle(cls, SliceProviderCompat.EXTRA_RESULT, Object.class);
            STACK = lookup.findVarHandle(cls, "stack", Completion.class);
            NEXT = lookup.findVarHandle(Completion.class, "next", Completion.class);
            Class<LockSupport> cls2 = LockSupport.class;
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }

    /* access modifiers changed from: package-private */
    public final Object encodeValue(T t) {
        return t == null ? NIL : t;
    }

    static AltResult encodeThrowable(Throwable th) {
        if (!(th instanceof CompletionException)) {
            th = new CompletionException(th);
        }
        return new AltResult(th);
    }

    static Object encodeThrowable(Throwable th, Object obj) {
        if (!(th instanceof CompletionException)) {
            th = new CompletionException(th);
        } else if ((obj instanceof AltResult) && th == ((AltResult) obj).f715ex) {
            return obj;
        }
        return new AltResult(th);
    }

    /* access modifiers changed from: package-private */
    public Object encodeOutcome(T t, Throwable th) {
        if (th == null) {
            return t == null ? NIL : t;
        }
        return encodeThrowable(th);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = ((java.util.concurrent.CompletableFuture.AltResult) r2).f715ex;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Object encodeRelay(java.lang.Object r2) {
        /*
            boolean r0 = r2 instanceof java.util.concurrent.CompletableFuture.AltResult
            if (r0 == 0) goto L_0x0019
            r0 = r2
            java.util.concurrent.CompletableFuture$AltResult r0 = (java.util.concurrent.CompletableFuture.AltResult) r0
            java.lang.Throwable r0 = r0.f715ex
            if (r0 == 0) goto L_0x0019
            boolean r1 = r0 instanceof java.util.concurrent.CompletionException
            if (r1 != 0) goto L_0x0019
            java.util.concurrent.CompletableFuture$AltResult r2 = new java.util.concurrent.CompletableFuture$AltResult
            java.util.concurrent.CompletionException r1 = new java.util.concurrent.CompletionException
            r1.<init>((java.lang.Throwable) r0)
            r2.<init>(r1)
        L_0x0019:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.encodeRelay(java.lang.Object):java.lang.Object");
    }

    private static Object reportGet(Object obj) throws InterruptedException, ExecutionException {
        Throwable cause;
        if (obj == null) {
            throw new InterruptedException();
        } else if (!(obj instanceof AltResult)) {
            return obj;
        } else {
            Throwable th = ((AltResult) obj).f715ex;
            if (th == null) {
                return null;
            }
            if (!(th instanceof CancellationException)) {
                if ((th instanceof CompletionException) && (cause = th.getCause()) != null) {
                    th = cause;
                }
                throw new ExecutionException(th);
            }
            throw ((CancellationException) th);
        }
    }

    private static Object reportJoin(Object obj) {
        if (!(obj instanceof AltResult)) {
            return obj;
        }
        Throwable th = ((AltResult) obj).f715ex;
        if (th == null) {
            return null;
        }
        if (th instanceof CancellationException) {
            throw ((CancellationException) th);
        } else if (th instanceof CompletionException) {
            throw ((CompletionException) th);
        } else {
            throw new CompletionException(th);
        }
    }

    static final class ThreadPerTaskExecutor implements Executor {
        ThreadPerTaskExecutor() {
        }

        public void execute(Runnable runnable) {
            new Thread(runnable).start();
        }
    }

    static Executor screenExecutor(Executor executor) {
        if (!USE_COMMON_POOL && executor == ForkJoinPool.commonPool()) {
            return ASYNC_POOL;
        }
        executor.getClass();
        return executor;
    }

    static abstract class Completion extends ForkJoinTask<Void> implements Runnable, AsynchronousCompletionTask {
        volatile Completion next;

        public final Void getRawResult() {
            return null;
        }

        /* access modifiers changed from: package-private */
        public abstract boolean isLive();

        public final void setRawResult(Void voidR) {
        }

        /* access modifiers changed from: package-private */
        public abstract CompletableFuture<?> tryFire(int i);

        Completion() {
        }

        public final void run() {
            tryFire(1);
        }

        public final boolean exec() {
            tryFire(1);
            return false;
        }
    }

    static abstract class UniCompletion<T, V> extends Completion {
        CompletableFuture<V> dep;
        Executor executor;
        CompletableFuture<T> src;

        UniCompletion(Executor executor2, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2) {
            this.executor = executor2;
            this.dep = completableFuture;
            this.src = completableFuture2;
        }

        /* access modifiers changed from: package-private */
        public final boolean claim() {
            Executor executor2 = this.executor;
            if (compareAndSetForkJoinTaskTag(0, 1)) {
                if (executor2 == null) {
                    return true;
                }
                this.executor = null;
                executor2.execute(this);
            }
            return false;
        }

        /* access modifiers changed from: package-private */
        public final boolean isLive() {
            return this.dep != null;
        }
    }

    /* access modifiers changed from: package-private */
    public final CompletableFuture<T> postFire(CompletableFuture<?> completableFuture, int i) {
        if (!(completableFuture == null || completableFuture.stack == null)) {
            Object obj = completableFuture.result;
            if (obj == null) {
                completableFuture.cleanStack();
            }
            if (i >= 0 && !(obj == null && completableFuture.result == null)) {
                completableFuture.postComplete();
            }
        }
        if (this.result == null || this.stack == null) {
            return null;
        }
        if (i < 0) {
            return this;
        }
        postComplete();
        return null;
    }

    static final class UniApply<T, V> extends UniCompletion<T, V> {

        /* renamed from: fn */
        Function<? super T, ? extends V> f729fn;

        UniApply(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, Function<? super T, ? extends V> function) {
            super(executor, completableFuture, completableFuture2);
            this.f729fn = function;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<V> tryFire(int i) {
            Function<? super T, ? extends V> function;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2 = this.dep;
            if (completableFuture2 == null || (function = this.f729fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null) {
                return null;
            }
            if (completableFuture2.result == null) {
                if (obj instanceof AltResult) {
                    Throwable th = ((AltResult) obj).f715ex;
                    if (th != null) {
                        completableFuture2.completeThrowable(th, obj);
                    } else {
                        obj = null;
                    }
                }
                if (i <= 0) {
                    try {
                        if (!claim()) {
                            return null;
                        }
                    } catch (Throwable th2) {
                        completableFuture2.completeThrowable(th2);
                    }
                }
                completableFuture2.completeValue(function.apply(obj));
            }
            this.dep = null;
            this.src = null;
            this.f729fn = null;
            return completableFuture2.postFire(completableFuture, i);
        }
    }

    private <V> CompletableFuture<V> uniApplyStage(Executor executor, Function<? super T, ? extends V> function) {
        function.getClass();
        Object obj = this.result;
        if (obj != null) {
            return uniApplyNow(obj, executor, function);
        }
        CompletableFuture<V> newIncompleteFuture = newIncompleteFuture();
        unipush(new UniApply(executor, newIncompleteFuture, this, function));
        return newIncompleteFuture;
    }

    private <V> CompletableFuture<V> uniApplyNow(Object obj, Executor executor, Function<? super T, ? extends V> function) {
        CompletableFuture<V> newIncompleteFuture = newIncompleteFuture();
        if (obj instanceof AltResult) {
            Throwable th = ((AltResult) obj).f715ex;
            if (th != null) {
                newIncompleteFuture.result = encodeThrowable(th, obj);
                return newIncompleteFuture;
            }
            obj = null;
        }
        if (executor != null) {
            try {
                executor.execute(new UniApply((Executor) null, newIncompleteFuture, this, function));
            } catch (Throwable th2) {
                newIncompleteFuture.result = encodeThrowable(th2);
            }
        } else {
            newIncompleteFuture.result = newIncompleteFuture.encodeValue(function.apply(obj));
        }
        return newIncompleteFuture;
    }

    static final class UniAccept<T> extends UniCompletion<T, Void> {

        /* renamed from: fn */
        Consumer<? super T> f728fn;

        UniAccept(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, Consumer<? super T> consumer) {
            super(executor, completableFuture, completableFuture2);
            this.f728fn = consumer;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<Void> tryFire(int i) {
            Consumer<? super T> consumer;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2 = this.dep;
            if (completableFuture2 == null || (consumer = this.f728fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null) {
                return null;
            }
            if (completableFuture2.result == null) {
                if (obj instanceof AltResult) {
                    Throwable th = ((AltResult) obj).f715ex;
                    if (th != null) {
                        completableFuture2.completeThrowable(th, obj);
                    } else {
                        obj = null;
                    }
                }
                if (i <= 0) {
                    try {
                        if (!claim()) {
                            return null;
                        }
                    } catch (Throwable th2) {
                        completableFuture2.completeThrowable(th2);
                    }
                }
                consumer.accept(obj);
                completableFuture2.completeNull();
            }
            this.dep = null;
            this.src = null;
            this.f728fn = null;
            return completableFuture2.postFire(completableFuture, i);
        }
    }

    private CompletableFuture<Void> uniAcceptStage(Executor executor, Consumer<? super T> consumer) {
        consumer.getClass();
        Object obj = this.result;
        if (obj != null) {
            return uniAcceptNow(obj, executor, consumer);
        }
        CompletableFuture<Void> newIncompleteFuture = newIncompleteFuture();
        unipush(new UniAccept(executor, newIncompleteFuture, this, consumer));
        return newIncompleteFuture;
    }

    private CompletableFuture<Void> uniAcceptNow(Object obj, Executor executor, Consumer<? super T> consumer) {
        CompletableFuture<Void> newIncompleteFuture = newIncompleteFuture();
        if (obj instanceof AltResult) {
            Throwable th = ((AltResult) obj).f715ex;
            if (th != null) {
                newIncompleteFuture.result = encodeThrowable(th, obj);
                return newIncompleteFuture;
            }
            obj = null;
        }
        if (executor != null) {
            try {
                executor.execute(new UniAccept((Executor) null, newIncompleteFuture, this, consumer));
            } catch (Throwable th2) {
                newIncompleteFuture.result = encodeThrowable(th2);
            }
        } else {
            consumer.accept(obj);
            newIncompleteFuture.result = NIL;
        }
        return newIncompleteFuture;
    }

    static final class UniRun<T> extends UniCompletion<T, Void> {

        /* renamed from: fn */
        Runnable f733fn;

        UniRun(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, Runnable runnable) {
            super(executor, completableFuture, completableFuture2);
            this.f733fn = runnable;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<Void> tryFire(int i) {
            Runnable runnable;
            CompletableFuture completableFuture;
            Object obj;
            Throwable th;
            CompletableFuture completableFuture2 = this.dep;
            if (completableFuture2 == null || (runnable = this.f733fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null) {
                return null;
            }
            if (completableFuture2.result == null) {
                if (!(obj instanceof AltResult) || (th = ((AltResult) obj).f715ex) == null) {
                    if (i <= 0) {
                        try {
                            if (!claim()) {
                                return null;
                            }
                        } catch (Throwable th2) {
                            completableFuture2.completeThrowable(th2);
                        }
                    }
                    runnable.run();
                    completableFuture2.completeNull();
                } else {
                    completableFuture2.completeThrowable(th, obj);
                }
            }
            this.dep = null;
            this.src = null;
            this.f733fn = null;
            return completableFuture2.postFire(completableFuture, i);
        }
    }

    private CompletableFuture<Void> uniRunStage(Executor executor, Runnable runnable) {
        runnable.getClass();
        Object obj = this.result;
        if (obj != null) {
            return uniRunNow(obj, executor, runnable);
        }
        CompletableFuture<Void> newIncompleteFuture = newIncompleteFuture();
        unipush(new UniRun(executor, newIncompleteFuture, this, runnable));
        return newIncompleteFuture;
    }

    private CompletableFuture<Void> uniRunNow(Object obj, Executor executor, Runnable runnable) {
        Throwable th;
        CompletableFuture<Void> newIncompleteFuture = newIncompleteFuture();
        if ((obj instanceof AltResult) && (th = ((AltResult) obj).f715ex) != null) {
            newIncompleteFuture.result = encodeThrowable(th, obj);
        } else if (executor != null) {
            try {
                executor.execute(new UniRun((Executor) null, newIncompleteFuture, this, runnable));
            } catch (Throwable th2) {
                newIncompleteFuture.result = encodeThrowable(th2);
            }
        } else {
            runnable.run();
            newIncompleteFuture.result = NIL;
        }
        return newIncompleteFuture;
    }

    static final class UniWhenComplete<T> extends UniCompletion<T, T> {

        /* renamed from: fn */
        BiConsumer<? super T, ? super Throwable> f734fn;

        UniWhenComplete(Executor executor, CompletableFuture<T> completableFuture, CompletableFuture<T> completableFuture2, BiConsumer<? super T, ? super Throwable> biConsumer) {
            super(executor, completableFuture, completableFuture2);
            this.f734fn = biConsumer;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<T> tryFire(int i) {
            BiConsumer<? super T, ? super Throwable> biConsumer;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2 = this.dep;
            if (!(completableFuture2 == null || (biConsumer = this.f734fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null)) {
                if (completableFuture2.uniWhenComplete(obj, biConsumer, i > 0 ? null : this)) {
                    this.dep = null;
                    this.src = null;
                    this.f734fn = null;
                    return completableFuture2.postFire(completableFuture, i);
                }
            }
            return null;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: java.lang.Throwable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: java.lang.Object} */
    /* JADX WARNING: type inference failed for: r0v1 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x002d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean uniWhenComplete(java.lang.Object r3, java.util.function.BiConsumer<? super T, ? super java.lang.Throwable> r4, java.util.concurrent.CompletableFuture.UniWhenComplete<T> r5) {
        /*
            r2 = this;
            java.lang.Object r0 = r2.result
            r1 = 1
            if (r0 != 0) goto L_0x0036
            r0 = 0
            if (r5 == 0) goto L_0x0010
            boolean r5 = r5.claim()     // Catch:{ all -> 0x0028 }
            if (r5 != 0) goto L_0x0010
            r2 = 0
            return r2
        L_0x0010:
            boolean r5 = r3 instanceof java.util.concurrent.CompletableFuture.AltResult     // Catch:{ all -> 0x0028 }
            if (r5 == 0) goto L_0x001a
            r5 = r3
            java.util.concurrent.CompletableFuture$AltResult r5 = (java.util.concurrent.CompletableFuture.AltResult) r5     // Catch:{ all -> 0x0028 }
            java.lang.Throwable r5 = r5.f715ex     // Catch:{ all -> 0x0028 }
            goto L_0x001c
        L_0x001a:
            r5 = r0
            r0 = r3
        L_0x001c:
            r4.accept(r0, r5)     // Catch:{ all -> 0x0025 }
            if (r5 != 0) goto L_0x0033
            r2.internalComplete(r3)     // Catch:{ all -> 0x0025 }
            return r1
        L_0x0025:
            r4 = move-exception
            r0 = r5
            goto L_0x0029
        L_0x0028:
            r4 = move-exception
        L_0x0029:
            if (r0 != 0) goto L_0x002d
            r5 = r4
            goto L_0x0033
        L_0x002d:
            if (r0 == r4) goto L_0x0032
            r0.addSuppressed(r4)
        L_0x0032:
            r5 = r0
        L_0x0033:
            r2.completeThrowable(r5, r3)
        L_0x0036:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.CompletableFuture.uniWhenComplete(java.lang.Object, java.util.function.BiConsumer, java.util.concurrent.CompletableFuture$UniWhenComplete):boolean");
    }

    private CompletableFuture<T> uniWhenCompleteStage(Executor executor, BiConsumer<? super T, ? super Throwable> biConsumer) {
        biConsumer.getClass();
        CompletableFuture<T> newIncompleteFuture = newIncompleteFuture();
        Object obj = this.result;
        if (obj == null) {
            unipush(new UniWhenComplete(executor, newIncompleteFuture, this, biConsumer));
        } else if (executor == null) {
            newIncompleteFuture.uniWhenComplete(obj, biConsumer, (UniWhenComplete<T>) null);
        } else {
            try {
                executor.execute(new UniWhenComplete((Executor) null, newIncompleteFuture, this, biConsumer));
            } catch (Throwable th) {
                newIncompleteFuture.result = encodeThrowable(th);
            }
        }
        return newIncompleteFuture;
    }

    static final class UniHandle<T, V> extends UniCompletion<T, V> {

        /* renamed from: fn */
        BiFunction<? super T, Throwable, ? extends V> f732fn;

        UniHandle(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, BiFunction<? super T, Throwable, ? extends V> biFunction) {
            super(executor, completableFuture, completableFuture2);
            this.f732fn = biFunction;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<V> tryFire(int i) {
            BiFunction<? super T, Throwable, ? extends V> biFunction;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2 = this.dep;
            if (!(completableFuture2 == null || (biFunction = this.f732fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null)) {
                if (completableFuture2.uniHandle(obj, biFunction, i > 0 ? null : this)) {
                    this.dep = null;
                    this.src = null;
                    this.f732fn = null;
                    return completableFuture2.postFire(completableFuture, i);
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public final <S> boolean uniHandle(Object obj, BiFunction<? super S, Throwable, ? extends T> biFunction, UniHandle<S, T> uniHandle) {
        if (this.result != null) {
            return true;
        }
        if (uniHandle != null) {
            try {
                if (!uniHandle.claim()) {
                    return false;
                }
            } catch (Throwable th) {
                completeThrowable(th);
                return true;
            }
        }
        Throwable th2 = null;
        if (obj instanceof AltResult) {
            th2 = ((AltResult) obj).f715ex;
            obj = null;
        }
        completeValue(biFunction.apply(obj, th2));
        return true;
    }

    private <V> CompletableFuture<V> uniHandleStage(Executor executor, BiFunction<? super T, Throwable, ? extends V> biFunction) {
        biFunction.getClass();
        CompletableFuture<V> newIncompleteFuture = newIncompleteFuture();
        Object obj = this.result;
        if (obj == null) {
            unipush(new UniHandle(executor, newIncompleteFuture, this, biFunction));
        } else if (executor == null) {
            newIncompleteFuture.uniHandle(obj, biFunction, (UniHandle<S, V>) null);
        } else {
            try {
                executor.execute(new UniHandle((Executor) null, newIncompleteFuture, this, biFunction));
            } catch (Throwable th) {
                newIncompleteFuture.result = encodeThrowable(th);
            }
        }
        return newIncompleteFuture;
    }

    static final class UniExceptionally<T> extends UniCompletion<T, T> {

        /* renamed from: fn */
        Function<? super Throwable, ? extends T> f731fn;

        UniExceptionally(CompletableFuture<T> completableFuture, CompletableFuture<T> completableFuture2, Function<? super Throwable, ? extends T> function) {
            super((Executor) null, completableFuture, completableFuture2);
            this.f731fn = function;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<T> tryFire(int i) {
            Function<? super Throwable, ? extends T> function;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2 = this.dep;
            if (completableFuture2 == null || (function = this.f731fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null || !completableFuture2.uniExceptionally(obj, function, this)) {
                return null;
            }
            this.dep = null;
            this.src = null;
            this.f731fn = null;
            return completableFuture2.postFire(completableFuture, i);
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean uniExceptionally(Object obj, Function<? super Throwable, ? extends T> function, UniExceptionally<T> uniExceptionally) {
        Throwable th;
        if (this.result != null) {
            return true;
        }
        try {
            if (!(obj instanceof AltResult) || (th = ((AltResult) obj).f715ex) == null) {
                internalComplete(obj);
                return true;
            } else if (uniExceptionally != null && !uniExceptionally.claim()) {
                return false;
            } else {
                completeValue(function.apply(th));
                return true;
            }
        } catch (Throwable th2) {
            completeThrowable(th2);
            return true;
        }
    }

    private CompletableFuture<T> uniExceptionallyStage(Function<Throwable, ? extends T> function) {
        function.getClass();
        CompletableFuture<T> newIncompleteFuture = newIncompleteFuture();
        Object obj = this.result;
        if (obj == null) {
            unipush(new UniExceptionally(newIncompleteFuture, this, function));
        } else {
            newIncompleteFuture.uniExceptionally(obj, function, (UniExceptionally<T>) null);
        }
        return newIncompleteFuture;
    }

    static final class UniRelay<U, T extends U> extends UniCompletion<T, U> {
        UniRelay(CompletableFuture<U> completableFuture, CompletableFuture<T> completableFuture2) {
            super((Executor) null, completableFuture, completableFuture2);
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<U> tryFire(int i) {
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2 = this.dep;
            if (completableFuture2 == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null) {
                return null;
            }
            if (completableFuture2.result == null) {
                completableFuture2.completeRelay(obj);
            }
            this.src = null;
            this.dep = null;
            return completableFuture2.postFire(completableFuture, i);
        }
    }

    private static <U, T extends U> CompletableFuture<U> uniCopyStage(CompletableFuture<T> completableFuture) {
        CompletableFuture<U> newIncompleteFuture = completableFuture.newIncompleteFuture();
        Object obj = completableFuture.result;
        if (obj != null) {
            newIncompleteFuture.result = encodeRelay(obj);
        } else {
            completableFuture.unipush(new UniRelay(newIncompleteFuture, completableFuture));
        }
        return newIncompleteFuture;
    }

    private MinimalStage<T> uniAsMinimalStage() {
        Object obj = this.result;
        if (obj != null) {
            return new MinimalStage<>(encodeRelay(obj));
        }
        MinimalStage<T> minimalStage = new MinimalStage<>();
        unipush(new UniRelay(minimalStage, this));
        return minimalStage;
    }

    static final class UniCompose<T, V> extends UniCompletion<T, V> {

        /* renamed from: fn */
        Function<? super T, ? extends CompletionStage<V>> f730fn;

        UniCompose(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, Function<? super T, ? extends CompletionStage<V>> function) {
            super(executor, completableFuture, completableFuture2);
            this.f730fn = function;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<V> tryFire(int i) {
            Function<? super T, ? extends CompletionStage<V>> function;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2 = this.dep;
            if (completableFuture2 == null || (function = this.f730fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null) {
                return null;
            }
            if (completableFuture2.result == null) {
                if (obj instanceof AltResult) {
                    Throwable th = ((AltResult) obj).f715ex;
                    if (th != null) {
                        completableFuture2.completeThrowable(th, obj);
                    } else {
                        obj = null;
                    }
                }
                if (i <= 0) {
                    try {
                        if (!claim()) {
                            return null;
                        }
                    } catch (Throwable th2) {
                        completableFuture2.completeThrowable(th2);
                    }
                }
                CompletableFuture completableFuture3 = ((CompletionStage) function.apply(obj)).toCompletableFuture();
                Object obj2 = completableFuture3.result;
                if (obj2 != null) {
                    completableFuture2.completeRelay(obj2);
                } else {
                    completableFuture3.unipush(new UniRelay(completableFuture2, completableFuture3));
                    if (completableFuture2.result == null) {
                        return null;
                    }
                }
            }
            this.dep = null;
            this.src = null;
            this.f730fn = null;
            return completableFuture2.postFire(completableFuture, i);
        }
    }

    private <V> CompletableFuture<V> uniComposeStage(Executor executor, Function<? super T, ? extends CompletionStage<V>> function) {
        function.getClass();
        CompletableFuture<V> newIncompleteFuture = newIncompleteFuture();
        Object obj = this.result;
        if (obj == null) {
            unipush(new UniCompose(executor, newIncompleteFuture, this, function));
        } else if (executor == null) {
            if (obj instanceof AltResult) {
                Throwable th = ((AltResult) obj).f715ex;
                if (th != null) {
                    newIncompleteFuture.result = encodeThrowable(th, obj);
                    return newIncompleteFuture;
                }
                obj = null;
            }
            try {
                CompletableFuture completableFuture = ((CompletionStage) function.apply(obj)).toCompletableFuture();
                Object obj2 = completableFuture.result;
                if (obj2 != null) {
                    newIncompleteFuture.result = encodeRelay(obj2);
                } else {
                    completableFuture.unipush(new UniRelay(newIncompleteFuture, completableFuture));
                }
            } catch (Throwable th2) {
                newIncompleteFuture.result = encodeThrowable(th2);
            }
        } else {
            try {
                executor.execute(new UniCompose((Executor) null, newIncompleteFuture, this, function));
            } catch (Throwable th3) {
                newIncompleteFuture.result = encodeThrowable(th3);
            }
        }
        return newIncompleteFuture;
    }

    static abstract class BiCompletion<T, U, V> extends UniCompletion<T, V> {
        CompletableFuture<U> snd;

        BiCompletion(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3) {
            super(executor, completableFuture, completableFuture2);
            this.snd = completableFuture3;
        }
    }

    static final class CoCompletion extends Completion {
        BiCompletion<?, ?, ?> base;

        CoCompletion(BiCompletion<?, ?, ?> biCompletion) {
            this.base = biCompletion;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<?> tryFire(int i) {
            CompletableFuture<?> tryFire;
            BiCompletion<?, ?, ?> biCompletion = this.base;
            if (biCompletion == null || (tryFire = biCompletion.tryFire(i)) == null) {
                return null;
            }
            this.base = null;
            return tryFire;
        }

        /* access modifiers changed from: package-private */
        public final boolean isLive() {
            BiCompletion<?, ?, ?> biCompletion = this.base;
            return (biCompletion == null || biCompletion.dep == null) ? false : true;
        }
    }

    /* access modifiers changed from: package-private */
    public final void bipush(CompletableFuture<?> completableFuture, BiCompletion<?, ?, ?> biCompletion) {
        if (biCompletion != null) {
            while (this.result == null) {
                if (tryPushStack(biCompletion)) {
                    if (completableFuture.result == null) {
                        completableFuture.unipush(new CoCompletion(biCompletion));
                        return;
                    } else if (this.result != null) {
                        biCompletion.tryFire(0);
                        return;
                    } else {
                        return;
                    }
                }
            }
            completableFuture.unipush(biCompletion);
        }
    }

    /* access modifiers changed from: package-private */
    public final CompletableFuture<T> postFire(CompletableFuture<?> completableFuture, CompletableFuture<?> completableFuture2, int i) {
        if (!(completableFuture2 == null || completableFuture2.stack == null)) {
            Object obj = completableFuture2.result;
            if (obj == null) {
                completableFuture2.cleanStack();
            }
            if (i >= 0 && !(obj == null && completableFuture2.result == null)) {
                completableFuture2.postComplete();
            }
        }
        return postFire(completableFuture, i);
    }

    static final class BiApply<T, U, V> extends BiCompletion<T, U, V> {

        /* renamed from: fn */
        BiFunction<? super T, ? super U, ? extends V> f719fn;

        BiApply(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, BiFunction<? super T, ? super U, ? extends V> biFunction) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.f719fn = biFunction;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<V> tryFire(int i) {
            BiFunction<? super T, ? super U, ? extends V> biFunction;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2;
            Object obj2;
            CompletableFuture completableFuture3 = this.dep;
            if (!(completableFuture3 == null || (biFunction = this.f719fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null || (completableFuture2 = this.snd) == null || (obj2 = completableFuture2.result) == null)) {
                if (completableFuture3.biApply(obj, obj2, biFunction, i > 0 ? null : this)) {
                    this.dep = null;
                    this.src = null;
                    this.snd = null;
                    this.f719fn = null;
                    return completableFuture3.postFire(completableFuture, completableFuture2, i);
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public final <R, S> boolean biApply(Object obj, Object obj2, BiFunction<? super R, ? super S, ? extends T> biFunction, BiApply<R, S, T> biApply) {
        if (this.result != null) {
            return true;
        }
        if (obj instanceof AltResult) {
            Throwable th = ((AltResult) obj).f715ex;
            if (th != null) {
                completeThrowable(th, obj);
                return true;
            }
            obj = null;
        }
        if (obj2 instanceof AltResult) {
            Throwable th2 = ((AltResult) obj2).f715ex;
            if (th2 != null) {
                completeThrowable(th2, obj2);
                return true;
            }
            obj2 = null;
        }
        if (biApply != null) {
            try {
                if (!biApply.claim()) {
                    return false;
                }
            } catch (Throwable th3) {
                completeThrowable(th3);
                return true;
            }
        }
        completeValue(biFunction.apply(obj, obj2));
        return true;
    }

    private <U, V> CompletableFuture<V> biApplyStage(Executor executor, CompletionStage<U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        CompletableFuture<U> completableFuture;
        Object obj;
        if (biFunction == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw null;
        }
        CompletableFuture<V> newIncompleteFuture = newIncompleteFuture();
        Object obj2 = this.result;
        if (obj2 == null || (obj = completableFuture.result) == null) {
            bipush(completableFuture, new BiApply(executor, newIncompleteFuture, this, completableFuture, biFunction));
        } else if (executor == null) {
            newIncompleteFuture.biApply(obj2, obj, biFunction, (BiApply<R, S, V>) null);
        } else {
            try {
                executor.execute(new BiApply((Executor) null, newIncompleteFuture, this, completableFuture, biFunction));
            } catch (Throwable th) {
                newIncompleteFuture.result = encodeThrowable(th);
            }
        }
        return newIncompleteFuture;
    }

    static final class BiAccept<T, U> extends BiCompletion<T, U, Void> {

        /* renamed from: fn */
        BiConsumer<? super T, ? super U> f718fn;

        BiAccept(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, BiConsumer<? super T, ? super U> biConsumer) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.f718fn = biConsumer;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<Void> tryFire(int i) {
            BiConsumer<? super T, ? super U> biConsumer;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2;
            Object obj2;
            CompletableFuture completableFuture3 = this.dep;
            if (!(completableFuture3 == null || (biConsumer = this.f718fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null || (completableFuture2 = this.snd) == null || (obj2 = completableFuture2.result) == null)) {
                if (completableFuture3.biAccept(obj, obj2, biConsumer, i > 0 ? null : this)) {
                    this.dep = null;
                    this.src = null;
                    this.snd = null;
                    this.f718fn = null;
                    return completableFuture3.postFire(completableFuture, completableFuture2, i);
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public final <R, S> boolean biAccept(Object obj, Object obj2, BiConsumer<? super R, ? super S> biConsumer, BiAccept<R, S> biAccept) {
        if (this.result != null) {
            return true;
        }
        if (obj instanceof AltResult) {
            Throwable th = ((AltResult) obj).f715ex;
            if (th != null) {
                completeThrowable(th, obj);
                return true;
            }
            obj = null;
        }
        if (obj2 instanceof AltResult) {
            Throwable th2 = ((AltResult) obj2).f715ex;
            if (th2 != null) {
                completeThrowable(th2, obj2);
                return true;
            }
            obj2 = null;
        }
        if (biAccept != null) {
            try {
                if (!biAccept.claim()) {
                    return false;
                }
            } catch (Throwable th3) {
                completeThrowable(th3);
                return true;
            }
        }
        biConsumer.accept(obj, obj2);
        completeNull();
        return true;
    }

    private <U> CompletableFuture<Void> biAcceptStage(Executor executor, CompletionStage<U> completionStage, BiConsumer<? super T, ? super U> biConsumer) {
        CompletableFuture<U> completableFuture;
        Object obj;
        if (biConsumer == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw null;
        }
        CompletableFuture<Void> newIncompleteFuture = newIncompleteFuture();
        Object obj2 = this.result;
        if (obj2 == null || (obj = completableFuture.result) == null) {
            bipush(completableFuture, new BiAccept(executor, newIncompleteFuture, this, completableFuture, biConsumer));
        } else if (executor == null) {
            newIncompleteFuture.biAccept(obj2, obj, biConsumer, (BiAccept<R, S>) null);
        } else {
            try {
                executor.execute(new BiAccept((Executor) null, newIncompleteFuture, this, completableFuture, biConsumer));
            } catch (Throwable th) {
                newIncompleteFuture.result = encodeThrowable(th);
            }
        }
        return newIncompleteFuture;
    }

    static final class BiRun<T, U> extends BiCompletion<T, U, Void> {

        /* renamed from: fn */
        Runnable f720fn;

        BiRun(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, Runnable runnable) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.f720fn = runnable;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<Void> tryFire(int i) {
            Runnable runnable;
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2;
            Object obj2;
            CompletableFuture completableFuture3 = this.dep;
            if (!(completableFuture3 == null || (runnable = this.f720fn) == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null || (completableFuture2 = this.snd) == null || (obj2 = completableFuture2.result) == null)) {
                if (completableFuture3.biRun(obj, obj2, runnable, i > 0 ? null : this)) {
                    this.dep = null;
                    this.src = null;
                    this.snd = null;
                    this.f720fn = null;
                    return completableFuture3.postFire(completableFuture, completableFuture2, i);
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean biRun(Object obj, Object obj2, Runnable runnable, BiRun<?, ?> biRun) {
        Throwable th;
        if (this.result != null) {
            return true;
        }
        if (!(obj instanceof AltResult) || (th = ((AltResult) obj).f715ex) == null) {
            if (!(obj2 instanceof AltResult) || (th = ((AltResult) obj2).f715ex) == null) {
                if (biRun != null) {
                    try {
                        if (!biRun.claim()) {
                            return false;
                        }
                    } catch (Throwable th2) {
                        completeThrowable(th2);
                        return true;
                    }
                }
                runnable.run();
                completeNull();
                return true;
            }
            obj = obj2;
        }
        completeThrowable(th, obj);
        return true;
    }

    private CompletableFuture<Void> biRunStage(Executor executor, CompletionStage<?> completionStage, Runnable runnable) {
        CompletableFuture<?> completableFuture;
        Object obj;
        if (runnable == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw null;
        }
        CompletableFuture<Void> newIncompleteFuture = newIncompleteFuture();
        Object obj2 = this.result;
        if (obj2 == null || (obj = completableFuture.result) == null) {
            bipush(completableFuture, new BiRun(executor, newIncompleteFuture, this, completableFuture, runnable));
        } else if (executor == null) {
            newIncompleteFuture.biRun(obj2, obj, runnable, (BiRun<?, ?>) null);
        } else {
            try {
                executor.execute(new BiRun((Executor) null, newIncompleteFuture, this, completableFuture, runnable));
            } catch (Throwable th) {
                newIncompleteFuture.result = encodeThrowable(th);
            }
        }
        return newIncompleteFuture;
    }

    static final class BiRelay<T, U> extends BiCompletion<T, U, Void> {
        BiRelay(CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3) {
            super((Executor) null, completableFuture, completableFuture2, completableFuture3);
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<Void> tryFire(int i) {
            CompletableFuture completableFuture;
            Object obj;
            CompletableFuture completableFuture2;
            Object obj2;
            Throwable th;
            CompletableFuture completableFuture3 = this.dep;
            if (completableFuture3 == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null || (completableFuture2 = this.snd) == null || (obj2 = completableFuture2.result) == null) {
                return null;
            }
            if (completableFuture3.result == null) {
                if (!(obj instanceof AltResult) || (th = ((AltResult) obj).f715ex) == null) {
                    if (!(obj2 instanceof AltResult) || (th = ((AltResult) obj2).f715ex) == null) {
                        completableFuture3.completeNull();
                    } else {
                        obj = obj2;
                    }
                }
                completableFuture3.completeThrowable(th, obj);
            }
            this.src = null;
            this.snd = null;
            this.dep = null;
            return completableFuture3.postFire(completableFuture, completableFuture2, i);
        }
    }

    static CompletableFuture<Void> andTree(CompletableFuture<?>[] completableFutureArr, int i, int i2) {
        CompletableFuture<Void> completableFuture;
        CompletableFuture<Void> completableFuture2;
        Object obj;
        Throwable th;
        CompletableFuture<Void> completableFuture3 = new CompletableFuture<>();
        if (i > i2) {
            completableFuture3.result = NIL;
        } else {
            int i3 = (i + i2) >>> 1;
            if (i == i3) {
                completableFuture = completableFutureArr[i];
            } else {
                completableFuture = andTree(completableFutureArr, i, i3);
            }
            if (completableFuture != null) {
                if (i == i2) {
                    completableFuture2 = completableFuture;
                } else {
                    int i4 = i3 + 1;
                    if (i2 == i4) {
                        completableFuture2 = completableFutureArr[i2];
                    } else {
                        completableFuture2 = andTree(completableFutureArr, i4, i2);
                    }
                }
                if (completableFuture2 != null) {
                    Object obj2 = completableFuture.result;
                    if (obj2 == null || (obj = completableFuture2.result) == null) {
                        completableFuture.bipush(completableFuture2, new BiRelay(completableFuture3, completableFuture, completableFuture2));
                    } else {
                        if (!(obj2 instanceof AltResult) || (th = ((AltResult) obj2).f715ex) == null) {
                            if (!(obj instanceof AltResult) || (th = ((AltResult) obj).f715ex) == null) {
                                completableFuture3.result = NIL;
                            } else {
                                obj2 = obj;
                            }
                        }
                        completableFuture3.result = encodeThrowable(th, obj2);
                    }
                }
            }
            throw null;
        }
        return completableFuture3;
    }

    static final class OrApply<T, U extends T, V> extends BiCompletion<T, U, V> {

        /* renamed from: fn */
        Function<? super T, ? extends V> f725fn;

        OrApply(Executor executor, CompletableFuture<V> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, Function<? super T, ? extends V> function) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.f725fn = function;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<V> tryFire(int i) {
            Function<? super T, ? extends V> function;
            CompletableFuture completableFuture;
            CompletableFuture completableFuture2;
            Object obj;
            CompletableFuture completableFuture3 = this.dep;
            if (completableFuture3 == null || (function = this.f725fn) == null || (completableFuture = this.src) == null || (completableFuture2 = this.snd) == null || ((obj = completableFuture.result) == null && (obj = completableFuture2.result) == null)) {
                return null;
            }
            if (completableFuture3.result == null) {
                if (i <= 0) {
                    try {
                        if (!claim()) {
                            return null;
                        }
                    } catch (Throwable th) {
                        completableFuture3.completeThrowable(th);
                    }
                }
                if (obj instanceof AltResult) {
                    Throwable th2 = ((AltResult) obj).f715ex;
                    if (th2 != null) {
                        completableFuture3.completeThrowable(th2, obj);
                    } else {
                        obj = null;
                    }
                }
                completableFuture3.completeValue(function.apply(obj));
            }
            this.dep = null;
            this.src = null;
            this.snd = null;
            this.f725fn = null;
            return completableFuture3.postFire(completableFuture, completableFuture2, i);
        }
    }

    private <U extends T, V> CompletableFuture<V> orApplyStage(Executor executor, CompletionStage<U> completionStage, Function<? super T, ? extends V> function) {
        CompletableFuture<U> completableFuture;
        if (function == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw null;
        }
        Object obj = this.result;
        if (obj == null) {
            obj = completableFuture.result;
            if (obj != null) {
                this = completableFuture;
            } else {
                CompletableFuture<V> newIncompleteFuture = newIncompleteFuture();
                orpush(completableFuture, new OrApply(executor, newIncompleteFuture, this, completableFuture, function));
                return newIncompleteFuture;
            }
        }
        return this.uniApplyNow(obj, executor, function);
    }

    static final class OrAccept<T, U extends T> extends BiCompletion<T, U, Void> {

        /* renamed from: fn */
        Consumer<? super T> f724fn;

        OrAccept(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, Consumer<? super T> consumer) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.f724fn = consumer;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<Void> tryFire(int i) {
            Consumer<? super T> consumer;
            CompletableFuture completableFuture;
            CompletableFuture completableFuture2;
            Object obj;
            CompletableFuture completableFuture3 = this.dep;
            if (completableFuture3 == null || (consumer = this.f724fn) == null || (completableFuture = this.src) == null || (completableFuture2 = this.snd) == null || ((obj = completableFuture.result) == null && (obj = completableFuture2.result) == null)) {
                return null;
            }
            if (completableFuture3.result == null) {
                if (i <= 0) {
                    try {
                        if (!claim()) {
                            return null;
                        }
                    } catch (Throwable th) {
                        completableFuture3.completeThrowable(th);
                    }
                }
                if (obj instanceof AltResult) {
                    Throwable th2 = ((AltResult) obj).f715ex;
                    if (th2 != null) {
                        completableFuture3.completeThrowable(th2, obj);
                    } else {
                        obj = null;
                    }
                }
                consumer.accept(obj);
                completableFuture3.completeNull();
            }
            this.dep = null;
            this.src = null;
            this.snd = null;
            this.f724fn = null;
            return completableFuture3.postFire(completableFuture, completableFuture2, i);
        }
    }

    private <U extends T> CompletableFuture<Void> orAcceptStage(Executor executor, CompletionStage<U> completionStage, Consumer<? super T> consumer) {
        CompletableFuture<U> completableFuture;
        if (consumer == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw null;
        }
        Object obj = this.result;
        if (obj == null) {
            obj = completableFuture.result;
            if (obj != null) {
                this = completableFuture;
            } else {
                CompletableFuture<Void> newIncompleteFuture = newIncompleteFuture();
                orpush(completableFuture, new OrAccept(executor, newIncompleteFuture, this, completableFuture, consumer));
                return newIncompleteFuture;
            }
        }
        return this.uniAcceptNow(obj, executor, consumer);
    }

    static final class OrRun<T, U> extends BiCompletion<T, U, Void> {

        /* renamed from: fn */
        Runnable f726fn;

        OrRun(Executor executor, CompletableFuture<Void> completableFuture, CompletableFuture<T> completableFuture2, CompletableFuture<U> completableFuture3, Runnable runnable) {
            super(executor, completableFuture, completableFuture2, completableFuture3);
            this.f726fn = runnable;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<Void> tryFire(int i) {
            Runnable runnable;
            CompletableFuture completableFuture;
            CompletableFuture completableFuture2;
            Object obj;
            Throwable th;
            CompletableFuture completableFuture3 = this.dep;
            if (completableFuture3 == null || (runnable = this.f726fn) == null || (completableFuture = this.src) == null || (completableFuture2 = this.snd) == null || ((obj = completableFuture.result) == null && (obj = completableFuture2.result) == null)) {
                return null;
            }
            if (completableFuture3.result == null) {
                if (i <= 0) {
                    try {
                        if (!claim()) {
                            return null;
                        }
                    } catch (Throwable th2) {
                        completableFuture3.completeThrowable(th2);
                    }
                }
                if (!(obj instanceof AltResult) || (th = ((AltResult) obj).f715ex) == null) {
                    runnable.run();
                    completableFuture3.completeNull();
                } else {
                    completableFuture3.completeThrowable(th, obj);
                }
            }
            this.dep = null;
            this.src = null;
            this.snd = null;
            this.f726fn = null;
            return completableFuture3.postFire(completableFuture, completableFuture2, i);
        }
    }

    private CompletableFuture<Void> orRunStage(Executor executor, CompletionStage<?> completionStage, Runnable runnable) {
        CompletableFuture<?> completableFuture;
        if (runnable == null || (completableFuture = completionStage.toCompletableFuture()) == null) {
            throw null;
        }
        Object obj = this.result;
        if (obj == null) {
            obj = completableFuture.result;
            if (obj != null) {
                this = completableFuture;
            } else {
                CompletableFuture<Void> newIncompleteFuture = newIncompleteFuture();
                orpush(completableFuture, new OrRun(executor, newIncompleteFuture, this, completableFuture, runnable));
                return newIncompleteFuture;
            }
        }
        return this.uniRunNow(obj, executor, runnable);
    }

    static class AnyOf extends Completion {
        CompletableFuture<Object> dep;
        CompletableFuture<?> src;
        CompletableFuture<?>[] srcs;

        AnyOf(CompletableFuture<Object> completableFuture, CompletableFuture<?> completableFuture2, CompletableFuture<?>[] completableFutureArr) {
            this.dep = completableFuture;
            this.src = completableFuture2;
            this.srcs = completableFutureArr;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<Object> tryFire(int i) {
            CompletableFuture<?> completableFuture;
            Object obj;
            CompletableFuture<?>[] completableFutureArr;
            CompletableFuture<Object> completableFuture2 = this.dep;
            if (!(completableFuture2 == null || (completableFuture = this.src) == null || (obj = completableFuture.result) == null || (completableFutureArr = this.srcs) == null)) {
                this.dep = null;
                this.src = null;
                this.srcs = null;
                if (completableFuture2.completeRelay(obj)) {
                    for (CompletableFuture<?> completableFuture3 : completableFutureArr) {
                        if (completableFuture3 != completableFuture) {
                            completableFuture3.cleanStack();
                        }
                    }
                    if (i < 0) {
                        return completableFuture2;
                    }
                    completableFuture2.postComplete();
                }
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public final boolean isLive() {
            CompletableFuture<Object> completableFuture = this.dep;
            return completableFuture != null && completableFuture.result == null;
        }
    }

    static final class AsyncSupply<T> extends ForkJoinTask<Void> implements Runnable, AsynchronousCompletionTask {
        CompletableFuture<T> dep;

        /* renamed from: fn */
        Supplier<? extends T> f717fn;

        public final Void getRawResult() {
            return null;
        }

        public final void setRawResult(Void voidR) {
        }

        AsyncSupply(CompletableFuture<T> completableFuture, Supplier<? extends T> supplier) {
            this.dep = completableFuture;
            this.f717fn = supplier;
        }

        public final boolean exec() {
            run();
            return false;
        }

        public void run() {
            Supplier<? extends T> supplier;
            CompletableFuture<T> completableFuture = this.dep;
            if (completableFuture != null && (supplier = this.f717fn) != null) {
                this.dep = null;
                this.f717fn = null;
                if (completableFuture.result == null) {
                    try {
                        completableFuture.completeValue(supplier.get());
                    } catch (Throwable th) {
                        completableFuture.completeThrowable(th);
                    }
                }
                completableFuture.postComplete();
            }
        }
    }

    static <U> CompletableFuture<U> asyncSupplyStage(Executor executor, Supplier<U> supplier) {
        supplier.getClass();
        CompletableFuture<U> completableFuture = new CompletableFuture<>();
        executor.execute(new AsyncSupply(completableFuture, supplier));
        return completableFuture;
    }

    static final class AsyncRun extends ForkJoinTask<Void> implements Runnable, AsynchronousCompletionTask {
        CompletableFuture<Void> dep;

        /* renamed from: fn */
        Runnable f716fn;

        public final Void getRawResult() {
            return null;
        }

        public final void setRawResult(Void voidR) {
        }

        AsyncRun(CompletableFuture<Void> completableFuture, Runnable runnable) {
            this.dep = completableFuture;
            this.f716fn = runnable;
        }

        public final boolean exec() {
            run();
            return false;
        }

        public void run() {
            Runnable runnable;
            CompletableFuture<Void> completableFuture = this.dep;
            if (completableFuture != null && (runnable = this.f716fn) != null) {
                this.dep = null;
                this.f716fn = null;
                if (completableFuture.result == null) {
                    try {
                        runnable.run();
                        completableFuture.completeNull();
                    } catch (Throwable th) {
                        completableFuture.completeThrowable(th);
                    }
                }
                completableFuture.postComplete();
            }
        }
    }

    static CompletableFuture<Void> asyncRunStage(Executor executor, Runnable runnable) {
        runnable.getClass();
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        executor.execute(new AsyncRun(completableFuture, runnable));
        return completableFuture;
    }

    static final class Signaller extends Completion implements ForkJoinPool.ManagedBlocker {
        final long deadline;
        boolean interrupted;
        final boolean interruptible;
        long nanos;
        volatile Thread thread = Thread.currentThread();

        Signaller(boolean z, long j, long j2) {
            this.interruptible = z;
            this.nanos = j;
            this.deadline = j2;
        }

        /* access modifiers changed from: package-private */
        public final CompletableFuture<?> tryFire(int i) {
            Thread thread2 = this.thread;
            if (thread2 != null) {
                this.thread = null;
                LockSupport.unpark(thread2);
            }
            return null;
        }

        public boolean isReleasable() {
            if (Thread.interrupted()) {
                this.interrupted = true;
            }
            if (this.interrupted && this.interruptible) {
                return true;
            }
            long j = this.deadline;
            if (j != 0) {
                if (this.nanos <= 0) {
                    return true;
                }
                long nanoTime = j - System.nanoTime();
                this.nanos = nanoTime;
                if (nanoTime <= 0) {
                    return true;
                }
            }
            if (this.thread == null) {
                return true;
            }
            return false;
        }

        public boolean block() {
            while (!isReleasable()) {
                if (this.deadline == 0) {
                    LockSupport.park(this);
                } else {
                    LockSupport.parkNanos(this, this.nanos);
                }
            }
            return true;
        }

        /* access modifiers changed from: package-private */
        public final boolean isLive() {
            return this.thread != null;
        }
    }

    private Object waitingGet(boolean z) {
        Object obj;
        boolean z2 = false;
        Signaller signaller = null;
        while (true) {
            obj = this.result;
            if (obj == null) {
                if (signaller != null) {
                    if (z2) {
                        try {
                            ForkJoinPool.managedBlock(signaller);
                        } catch (InterruptedException unused) {
                            signaller.interrupted = true;
                        }
                        if (signaller.interrupted && z) {
                            break;
                        }
                    } else {
                        z2 = tryPushStack(signaller);
                    }
                } else {
                    signaller = new Signaller(z, 0, 0);
                    if (Thread.currentThread() instanceof ForkJoinWorkerThread) {
                        ForkJoinPool.helpAsyncBlocker(defaultExecutor(), signaller);
                    }
                }
            } else {
                break;
            }
        }
        if (signaller != null && z2) {
            signaller.thread = null;
            if (!z && signaller.interrupted) {
                Thread.currentThread().interrupt();
            }
            if (obj == null) {
                cleanStack();
            }
        }
        if (!(obj == null && (obj = this.result) == null)) {
            postComplete();
        }
        return obj;
    }

    private Object timedGet(long j) throws TimeoutException {
        Object obj;
        if (Thread.interrupted()) {
            return null;
        }
        if (j > 0) {
            long nanoTime = System.nanoTime() + j;
            if (nanoTime == 0) {
                nanoTime = 1;
            }
            boolean z = false;
            Signaller signaller = null;
            while (true) {
                obj = this.result;
                if (obj != null) {
                    break;
                } else if (signaller == null) {
                    Signaller signaller2 = new Signaller(true, j, nanoTime);
                    if (Thread.currentThread() instanceof ForkJoinWorkerThread) {
                        ForkJoinPool.helpAsyncBlocker(defaultExecutor(), signaller2);
                    }
                    signaller = signaller2;
                } else if (!z) {
                    z = tryPushStack(signaller);
                } else if (signaller.nanos <= 0) {
                    break;
                } else {
                    try {
                        ForkJoinPool.managedBlock(signaller);
                    } catch (InterruptedException unused) {
                        signaller.interrupted = true;
                    }
                    if (signaller.interrupted) {
                        break;
                    }
                }
            }
            if (signaller != null && z) {
                signaller.thread = null;
                if (obj == null) {
                    cleanStack();
                }
            }
            if (!(obj == null && (obj = this.result) == null)) {
                postComplete();
            }
            if (obj != null || (signaller != null && signaller.interrupted)) {
                return obj;
            }
        }
        throw new TimeoutException();
    }

    public CompletableFuture() {
    }

    CompletableFuture(Object obj) {
        this.result = obj;
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier) {
        return asyncSupplyStage(ASYNC_POOL, supplier);
    }

    public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor) {
        return asyncSupplyStage(screenExecutor(executor), supplier);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable) {
        return asyncRunStage(ASYNC_POOL, runnable);
    }

    public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor) {
        return asyncRunStage(screenExecutor(executor), runnable);
    }

    public static <U> CompletableFuture<U> completedFuture(U u) {
        if (u == null) {
            u = NIL;
        }
        return new CompletableFuture<>(u);
    }

    public boolean isDone() {
        return this.result != null;
    }

    public T get() throws InterruptedException, ExecutionException {
        Object obj = this.result;
        if (obj == null) {
            obj = waitingGet(true);
        }
        return reportGet(obj);
    }

    public T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        long nanos = timeUnit.toNanos(j);
        Object obj = this.result;
        if (obj == null) {
            obj = timedGet(nanos);
        }
        return reportGet(obj);
    }

    public T join() {
        Object obj = this.result;
        if (obj == null) {
            obj = waitingGet(false);
        }
        return reportJoin(obj);
    }

    public T getNow(T t) {
        Object obj = this.result;
        return obj == null ? t : reportJoin(obj);
    }

    public boolean complete(T t) {
        boolean completeValue = completeValue(t);
        postComplete();
        return completeValue;
    }

    public boolean completeExceptionally(Throwable th) {
        th.getClass();
        boolean internalComplete = internalComplete(new AltResult(th));
        postComplete();
        return internalComplete;
    }

    public <U> CompletableFuture<U> thenApply(Function<? super T, ? extends U> function) {
        return uniApplyStage((Executor) null, function);
    }

    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> function) {
        return uniApplyStage(defaultExecutor(), function);
    }

    public <U> CompletableFuture<U> thenApplyAsync(Function<? super T, ? extends U> function, Executor executor) {
        return uniApplyStage(screenExecutor(executor), function);
    }

    public CompletableFuture<Void> thenAccept(Consumer<? super T> consumer) {
        return uniAcceptStage((Executor) null, consumer);
    }

    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> consumer) {
        return uniAcceptStage(defaultExecutor(), consumer);
    }

    public CompletableFuture<Void> thenAcceptAsync(Consumer<? super T> consumer, Executor executor) {
        return uniAcceptStage(screenExecutor(executor), consumer);
    }

    public CompletableFuture<Void> thenRun(Runnable runnable) {
        return uniRunStage((Executor) null, runnable);
    }

    public CompletableFuture<Void> thenRunAsync(Runnable runnable) {
        return uniRunStage(defaultExecutor(), runnable);
    }

    public CompletableFuture<Void> thenRunAsync(Runnable runnable, Executor executor) {
        return uniRunStage(screenExecutor(executor), runnable);
    }

    public <U, V> CompletableFuture<V> thenCombine(CompletionStage<? extends U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        return biApplyStage((Executor) null, completionStage, biFunction);
    }

    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction) {
        return biApplyStage(defaultExecutor(), completionStage, biFunction);
    }

    public <U, V> CompletableFuture<V> thenCombineAsync(CompletionStage<? extends U> completionStage, BiFunction<? super T, ? super U, ? extends V> biFunction, Executor executor) {
        return biApplyStage(screenExecutor(executor), completionStage, biFunction);
    }

    public <U> CompletableFuture<Void> thenAcceptBoth(CompletionStage<? extends U> completionStage, BiConsumer<? super T, ? super U> biConsumer) {
        return biAcceptStage((Executor) null, completionStage, biConsumer);
    }

    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> completionStage, BiConsumer<? super T, ? super U> biConsumer) {
        return biAcceptStage(defaultExecutor(), completionStage, biConsumer);
    }

    public <U> CompletableFuture<Void> thenAcceptBothAsync(CompletionStage<? extends U> completionStage, BiConsumer<? super T, ? super U> biConsumer, Executor executor) {
        return biAcceptStage(screenExecutor(executor), completionStage, biConsumer);
    }

    public CompletableFuture<Void> runAfterBoth(CompletionStage<?> completionStage, Runnable runnable) {
        return biRunStage((Executor) null, completionStage, runnable);
    }

    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> completionStage, Runnable runnable) {
        return biRunStage(defaultExecutor(), completionStage, runnable);
    }

    public CompletableFuture<Void> runAfterBothAsync(CompletionStage<?> completionStage, Runnable runnable, Executor executor) {
        return biRunStage(screenExecutor(executor), completionStage, runnable);
    }

    public <U> CompletableFuture<U> applyToEither(CompletionStage<? extends T> completionStage, Function<? super T, U> function) {
        return orApplyStage((Executor) null, completionStage, function);
    }

    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> completionStage, Function<? super T, U> function) {
        return orApplyStage(defaultExecutor(), completionStage, function);
    }

    public <U> CompletableFuture<U> applyToEitherAsync(CompletionStage<? extends T> completionStage, Function<? super T, U> function, Executor executor) {
        return orApplyStage(screenExecutor(executor), completionStage, function);
    }

    public CompletableFuture<Void> acceptEither(CompletionStage<? extends T> completionStage, Consumer<? super T> consumer) {
        return orAcceptStage((Executor) null, completionStage, consumer);
    }

    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> completionStage, Consumer<? super T> consumer) {
        return orAcceptStage(defaultExecutor(), completionStage, consumer);
    }

    public CompletableFuture<Void> acceptEitherAsync(CompletionStage<? extends T> completionStage, Consumer<? super T> consumer, Executor executor) {
        return orAcceptStage(screenExecutor(executor), completionStage, consumer);
    }

    public CompletableFuture<Void> runAfterEither(CompletionStage<?> completionStage, Runnable runnable) {
        return orRunStage((Executor) null, completionStage, runnable);
    }

    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> completionStage, Runnable runnable) {
        return orRunStage(defaultExecutor(), completionStage, runnable);
    }

    public CompletableFuture<Void> runAfterEitherAsync(CompletionStage<?> completionStage, Runnable runnable, Executor executor) {
        return orRunStage(screenExecutor(executor), completionStage, runnable);
    }

    public <U> CompletableFuture<U> thenCompose(Function<? super T, ? extends CompletionStage<U>> function) {
        return uniComposeStage((Executor) null, function);
    }

    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> function) {
        return uniComposeStage(defaultExecutor(), function);
    }

    public <U> CompletableFuture<U> thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> function, Executor executor) {
        return uniComposeStage(screenExecutor(executor), function);
    }

    public CompletableFuture<T> whenComplete(BiConsumer<? super T, ? super Throwable> biConsumer) {
        return uniWhenCompleteStage((Executor) null, biConsumer);
    }

    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> biConsumer) {
        return uniWhenCompleteStage(defaultExecutor(), biConsumer);
    }

    public CompletableFuture<T> whenCompleteAsync(BiConsumer<? super T, ? super Throwable> biConsumer, Executor executor) {
        return uniWhenCompleteStage(screenExecutor(executor), biConsumer);
    }

    public <U> CompletableFuture<U> handle(BiFunction<? super T, Throwable, ? extends U> biFunction) {
        return uniHandleStage((Executor) null, biFunction);
    }

    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> biFunction) {
        return uniHandleStage(defaultExecutor(), biFunction);
    }

    public <U> CompletableFuture<U> handleAsync(BiFunction<? super T, Throwable, ? extends U> biFunction, Executor executor) {
        return uniHandleStage(screenExecutor(executor), biFunction);
    }

    public CompletableFuture<T> exceptionally(Function<Throwable, ? extends T> function) {
        return uniExceptionallyStage(function);
    }

    public static CompletableFuture<Void> allOf(CompletableFuture<?>... completableFutureArr) {
        return andTree(completableFutureArr, 0, completableFutureArr.length - 1);
    }

    public static CompletableFuture<Object> anyOf(CompletableFuture<?>... completableFutureArr) {
        int length = completableFutureArr.length;
        int i = 0;
        if (length > 1) {
            for (CompletableFuture<?> completableFuture : completableFutureArr) {
                Object obj = completableFuture.result;
                if (obj != null) {
                    return new CompletableFuture<>(encodeRelay(obj));
                }
            }
            CompletableFuture[] completableFutureArr2 = (CompletableFuture[]) completableFutureArr.clone();
            CompletableFuture<Object> completableFuture2 = new CompletableFuture<>();
            for (CompletableFuture completableFuture3 : completableFutureArr2) {
                completableFuture3.unipush(new AnyOf(completableFuture2, completableFuture3, completableFutureArr2));
            }
            if (completableFuture2.result != null) {
                int length2 = completableFutureArr2.length;
                while (i < length2) {
                    if (completableFutureArr2[i].result != null) {
                        while (true) {
                            i++;
                            if (i >= length2) {
                                break;
                            } else if (completableFutureArr2[i].result == null) {
                                completableFutureArr2[i].cleanStack();
                            }
                        }
                    }
                    i++;
                }
            }
            return completableFuture2;
        } else if (length == 0) {
            return new CompletableFuture<>();
        } else {
            return uniCopyStage(completableFutureArr[0]);
        }
    }

    public boolean cancel(boolean z) {
        boolean z2 = this.result == null && internalComplete(new AltResult(new CancellationException()));
        postComplete();
        if (z2 || isCancelled()) {
            return true;
        }
        return false;
    }

    public boolean isCancelled() {
        Object obj = this.result;
        return (obj instanceof AltResult) && (((AltResult) obj).f715ex instanceof CancellationException);
    }

    public boolean isCompletedExceptionally() {
        Object obj = this.result;
        return (obj instanceof AltResult) && obj != NIL;
    }

    public void obtrudeValue(T t) {
        if (t == null) {
            t = NIL;
        }
        this.result = t;
        postComplete();
    }

    public void obtrudeException(Throwable th) {
        th.getClass();
        this.result = new AltResult(th);
        postComplete();
    }

    public int getNumberOfDependents() {
        int i = 0;
        for (Completion completion = this.stack; completion != null; completion = completion.next) {
            i++;
        }
        return i;
    }

    public String toString() {
        String str;
        Object obj = this.result;
        int i = 0;
        for (Completion completion = this.stack; completion != null; completion = completion.next) {
            i++;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        if (obj != null) {
            if (obj instanceof AltResult) {
                AltResult altResult = (AltResult) obj;
                if (altResult.f715ex != null) {
                    str = "[Completed exceptionally: " + altResult.f715ex + NavigationBarInflaterView.SIZE_MOD_END;
                }
            }
            str = "[Completed normally]";
        } else if (i == 0) {
            str = "[Not completed]";
        } else {
            str = "[Not completed, " + i + " dependents]";
        }
        sb.append(str);
        return sb.toString();
    }

    public <U> CompletableFuture<U> newIncompleteFuture() {
        return new CompletableFuture<>();
    }

    public Executor defaultExecutor() {
        return ASYNC_POOL;
    }

    public CompletableFuture<T> copy() {
        return uniCopyStage(this);
    }

    public CompletionStage<T> minimalCompletionStage() {
        return uniAsMinimalStage();
    }

    public CompletableFuture<T> completeAsync(Supplier<? extends T> supplier, Executor executor) {
        if (supplier == null || executor == null) {
            throw null;
        }
        executor.execute(new AsyncSupply(this, supplier));
        return this;
    }

    public CompletableFuture<T> completeAsync(Supplier<? extends T> supplier) {
        return completeAsync(supplier, defaultExecutor());
    }

    public CompletableFuture<T> orTimeout(long j, TimeUnit timeUnit) {
        timeUnit.getClass();
        if (this.result == null) {
            whenComplete(new Canceller(Delayer.delay(new Timeout(this), j, timeUnit)));
        }
        return this;
    }

    public CompletableFuture<T> completeOnTimeout(T t, long j, TimeUnit timeUnit) {
        timeUnit.getClass();
        if (this.result == null) {
            whenComplete(new Canceller(Delayer.delay(new DelayedCompleter(this, t), j, timeUnit)));
        }
        return this;
    }

    public static Executor delayedExecutor(long j, TimeUnit timeUnit, Executor executor) {
        if (timeUnit != null && executor != null) {
            return new DelayedExecutor(j, timeUnit, executor);
        }
        throw null;
    }

    public static Executor delayedExecutor(long j, TimeUnit timeUnit) {
        timeUnit.getClass();
        return new DelayedExecutor(j, timeUnit, ASYNC_POOL);
    }

    public static <U> CompletionStage<U> completedStage(U u) {
        if (u == null) {
            u = NIL;
        }
        return new MinimalStage(u);
    }

    public static <U> CompletableFuture<U> failedFuture(Throwable th) {
        th.getClass();
        return new CompletableFuture<>(new AltResult(th));
    }

    public static <U> CompletionStage<U> failedStage(Throwable th) {
        th.getClass();
        return new MinimalStage(new AltResult(th));
    }

    static final class Delayer {
        static final ScheduledThreadPoolExecutor delayer;

        Delayer() {
        }

        static ScheduledFuture<?> delay(Runnable runnable, long j, TimeUnit timeUnit) {
            return delayer.schedule(runnable, j, timeUnit);
        }

        static final class DaemonThreadFactory implements ThreadFactory {
            DaemonThreadFactory() {
            }

            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setDaemon(true);
                thread.setName("CompletableFutureDelayScheduler");
                return thread;
            }
        }

        static {
            ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, (ThreadFactory) new DaemonThreadFactory());
            delayer = scheduledThreadPoolExecutor;
            scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
        }
    }

    static final class DelayedExecutor implements Executor {
        final long delay;
        final Executor executor;
        final TimeUnit unit;

        DelayedExecutor(long j, TimeUnit timeUnit, Executor executor2) {
            this.delay = j;
            this.unit = timeUnit;
            this.executor = executor2;
        }

        public void execute(Runnable runnable) {
            Delayer.delay(new TaskSubmitter(this.executor, runnable), this.delay, this.unit);
        }
    }

    static final class TaskSubmitter implements Runnable {
        final Runnable action;
        final Executor executor;

        TaskSubmitter(Executor executor2, Runnable runnable) {
            this.executor = executor2;
            this.action = runnable;
        }

        public void run() {
            this.executor.execute(this.action);
        }
    }

    static final class Timeout implements Runnable {

        /* renamed from: f */
        final CompletableFuture<?> f727f;

        Timeout(CompletableFuture<?> completableFuture) {
            this.f727f = completableFuture;
        }

        public void run() {
            CompletableFuture<?> completableFuture = this.f727f;
            if (completableFuture != null && !completableFuture.isDone()) {
                this.f727f.completeExceptionally(new TimeoutException());
            }
        }
    }

    static final class DelayedCompleter<U> implements Runnable {

        /* renamed from: f */
        final CompletableFuture<U> f722f;

        /* renamed from: u */
        final U f723u;

        DelayedCompleter(CompletableFuture<U> completableFuture, U u) {
            this.f722f = completableFuture;
            this.f723u = u;
        }

        public void run() {
            CompletableFuture<U> completableFuture = this.f722f;
            if (completableFuture != null) {
                completableFuture.complete(this.f723u);
            }
        }
    }

    static final class Canceller implements BiConsumer<Object, Throwable> {

        /* renamed from: f */
        final Future<?> f721f;

        Canceller(Future<?> future) {
            this.f721f = future;
        }

        public void accept(Object obj, Throwable th) {
            Future<?> future;
            if (th == null && (future = this.f721f) != null && !future.isDone()) {
                this.f721f.cancel(false);
            }
        }
    }

    static final class MinimalStage<T> extends CompletableFuture<T> {
        MinimalStage() {
        }

        MinimalStage(Object obj) {
            super(obj);
        }

        public <U> CompletableFuture<U> newIncompleteFuture() {
            return new MinimalStage();
        }

        public T get() {
            throw new UnsupportedOperationException();
        }

        public T get(long j, TimeUnit timeUnit) {
            throw new UnsupportedOperationException();
        }

        public T getNow(T t) {
            throw new UnsupportedOperationException();
        }

        public T join() {
            throw new UnsupportedOperationException();
        }

        public boolean complete(T t) {
            throw new UnsupportedOperationException();
        }

        public boolean completeExceptionally(Throwable th) {
            throw new UnsupportedOperationException();
        }

        public boolean cancel(boolean z) {
            throw new UnsupportedOperationException();
        }

        public void obtrudeValue(T t) {
            throw new UnsupportedOperationException();
        }

        public void obtrudeException(Throwable th) {
            throw new UnsupportedOperationException();
        }

        public boolean isDone() {
            throw new UnsupportedOperationException();
        }

        public boolean isCancelled() {
            throw new UnsupportedOperationException();
        }

        public boolean isCompletedExceptionally() {
            throw new UnsupportedOperationException();
        }

        public int getNumberOfDependents() {
            throw new UnsupportedOperationException();
        }

        public CompletableFuture<T> completeAsync(Supplier<? extends T> supplier, Executor executor) {
            throw new UnsupportedOperationException();
        }

        public CompletableFuture<T> completeAsync(Supplier<? extends T> supplier) {
            throw new UnsupportedOperationException();
        }

        public CompletableFuture<T> orTimeout(long j, TimeUnit timeUnit) {
            throw new UnsupportedOperationException();
        }

        public CompletableFuture<T> completeOnTimeout(T t, long j, TimeUnit timeUnit) {
            throw new UnsupportedOperationException();
        }

        public CompletableFuture<T> toCompletableFuture() {
            Object obj = this.result;
            if (obj != null) {
                return new CompletableFuture<>(encodeRelay(obj));
            }
            CompletableFuture<T> completableFuture = new CompletableFuture<>();
            unipush(new UniRelay(completableFuture, this));
            return completableFuture;
        }
    }
}
