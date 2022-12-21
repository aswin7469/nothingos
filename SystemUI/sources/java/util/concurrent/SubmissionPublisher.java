package java.util.concurrent;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Flow;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class SubmissionPublisher<T> implements Flow.Publisher<T>, AutoCloseable {
    private static final Executor ASYNC_POOL = (ForkJoinPool.getCommonPoolParallelism() > 1 ? ForkJoinPool.commonPool() : new ThreadPerTaskExecutor());
    static final int BUFFER_CAPACITY_LIMIT = 1073741824;
    static final int INITIAL_CAPACITY = 32;
    BufferedSubscription<T> clients;
    volatile boolean closed;
    volatile Throwable closedException;
    final Executor executor;
    final int maxBufferCapacity;
    final BiConsumer<? super Flow.Subscriber<? super T>, ? super Throwable> onNextHandler;
    Thread owner;
    boolean subscribed;

    static final int roundCapacity(int i) {
        int i2 = i - 1;
        int i3 = i2 | (i2 >>> 1);
        int i4 = i3 | (i3 >>> 2);
        int i5 = i4 | (i4 >>> 4);
        int i6 = i5 | (i5 >>> 8);
        int i7 = i6 | (i6 >>> 16);
        if (i7 <= 0) {
            return 1;
        }
        if (i7 >= 1073741824) {
            return 1073741824;
        }
        return 1 + i7;
    }

    private static final class ThreadPerTaskExecutor implements Executor {
        ThreadPerTaskExecutor() {
        }

        public void execute(Runnable runnable) {
            new Thread(runnable).start();
        }
    }

    public SubmissionPublisher(Executor executor2, int i, BiConsumer<? super Flow.Subscriber<? super T>, ? super Throwable> biConsumer) {
        executor2.getClass();
        if (i > 0) {
            this.executor = executor2;
            this.onNextHandler = biConsumer;
            this.maxBufferCapacity = roundCapacity(i);
            return;
        }
        throw new IllegalArgumentException("capacity must be positive");
    }

    public SubmissionPublisher(Executor executor2, int i) {
        this(executor2, i, (BiConsumer) null);
    }

    public SubmissionPublisher() {
        this(ASYNC_POOL, Flow.defaultBufferSize(), (BiConsumer) null);
    }

    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        subscriber.getClass();
        int i = this.maxBufferCapacity;
        int i2 = 32;
        if (i < 32) {
            i2 = i;
        }
        BufferedSubscription bufferedSubscription = new BufferedSubscription(subscriber, this.executor, this.onNextHandler, new Object[i2], i);
        synchronized (this) {
            if (!this.subscribed) {
                this.subscribed = true;
                this.owner = Thread.currentThread();
            }
            BufferedSubscription<T> bufferedSubscription2 = this.clients;
            BufferedSubscription<T> bufferedSubscription3 = null;
            while (true) {
                if (bufferedSubscription2 == null) {
                    bufferedSubscription.onSubscribe();
                    Throwable th = this.closedException;
                    if (th != null) {
                        bufferedSubscription.onError(th);
                    } else if (this.closed) {
                        bufferedSubscription.onComplete();
                    } else if (bufferedSubscription3 == null) {
                        this.clients = bufferedSubscription;
                    } else {
                        bufferedSubscription3.next = bufferedSubscription;
                    }
                } else {
                    BufferedSubscription<T> bufferedSubscription4 = bufferedSubscription2.next;
                    if (bufferedSubscription2.isClosed()) {
                        bufferedSubscription2.next = null;
                        if (bufferedSubscription3 == null) {
                            this.clients = bufferedSubscription4;
                        } else {
                            bufferedSubscription3.next = bufferedSubscription4;
                        }
                    } else if (subscriber.equals(bufferedSubscription2.subscriber)) {
                        bufferedSubscription2.onError(new IllegalStateException("Duplicate subscribe"));
                        break;
                    } else {
                        bufferedSubscription3 = bufferedSubscription2;
                    }
                    bufferedSubscription2 = bufferedSubscription4;
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:36:0x0062 A[LOOP:0: B:14:0x0026->B:36:0x0062, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0045 A[EDGE_INSN: B:41:0x0045->B:28:0x0045 ?: BREAK  , SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int doOffer(T r15, long r16, java.util.function.BiPredicate<java.util.concurrent.Flow.Subscriber<? super T>, ? super T> r18) {
        /*
            r14 = this;
            r9 = r14
            r15.getClass()
            monitor-enter(r14)
            java.lang.Thread r0 = java.lang.Thread.currentThread()     // Catch:{ all -> 0x0066 }
            java.util.concurrent.SubmissionPublisher$BufferedSubscription<T> r1 = r9.clients     // Catch:{ all -> 0x0066 }
            java.lang.Thread r2 = r9.owner     // Catch:{ all -> 0x0066 }
            r3 = 1
            r10 = 0
            if (r2 == r0) goto L_0x0013
            r0 = r3
            goto L_0x0014
        L_0x0013:
            r0 = r10
        L_0x0014:
            r4 = 0
            if (r0 == 0) goto L_0x001b
            if (r2 == 0) goto L_0x001b
            r9.owner = r4     // Catch:{ all -> 0x0066 }
        L_0x001b:
            if (r1 != 0) goto L_0x0022
            boolean r0 = r9.closed     // Catch:{ all -> 0x0066 }
            r12 = r10
            r10 = r0
            goto L_0x0056
        L_0x0022:
            r5 = r4
            r6 = r5
            r2 = r10
            r7 = r2
        L_0x0026:
            java.util.concurrent.SubmissionPublisher$BufferedSubscription<T> r8 = r1.next     // Catch:{ all -> 0x0066 }
            r11 = r15
            int r12 = r1.offer(r15, r0)     // Catch:{ all -> 0x0066 }
            if (r12 != 0) goto L_0x0039
            r1.nextRetry = r4     // Catch:{ all -> 0x0066 }
            if (r5 != 0) goto L_0x0035
            r6 = r1
            goto L_0x0037
        L_0x0035:
            r5.nextRetry = r1     // Catch:{ all -> 0x0066 }
        L_0x0037:
            r5 = r1
            goto L_0x0041
        L_0x0039:
            if (r12 >= 0) goto L_0x003e
            r12 = r2
            r13 = r3
            goto L_0x0043
        L_0x003e:
            if (r12 <= r2) goto L_0x0041
            goto L_0x0042
        L_0x0041:
            r12 = r2
        L_0x0042:
            r13 = r7
        L_0x0043:
            if (r8 != 0) goto L_0x0062
            if (r6 != 0) goto L_0x0049
            if (r13 == 0) goto L_0x0056
        L_0x0049:
            r1 = r14
            r2 = r15
            r3 = r16
            r5 = r18
            r7 = r12
            r8 = r13
            int r0 = r1.retryOffer(r2, r3, r5, r6, r7, r8)     // Catch:{ all -> 0x0066 }
            r12 = r0
        L_0x0056:
            monitor-exit(r14)     // Catch:{ all -> 0x0066 }
            if (r10 != 0) goto L_0x005a
            return r12
        L_0x005a:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Closed"
            r0.<init>((java.lang.String) r1)
            throw r0
        L_0x0062:
            r1 = r8
            r2 = r12
            r7 = r13
            goto L_0x0026
        L_0x0066:
            r0 = move-exception
            monitor-exit(r14)     // Catch:{ all -> 0x0066 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.doOffer(java.lang.Object, long, java.util.function.BiPredicate):int");
    }

    private int retryOffer(T t, long j, BiPredicate<Flow.Subscriber<? super T>, ? super T> biPredicate, BufferedSubscription<T> bufferedSubscription, int i, boolean z) {
        while (bufferedSubscription != null) {
            BufferedSubscription<T> bufferedSubscription2 = bufferedSubscription.nextRetry;
            bufferedSubscription.nextRetry = null;
            if (j > 0) {
                bufferedSubscription.awaitSpace(j);
            }
            int retryOffer = bufferedSubscription.retryOffer(t);
            if (retryOffer == 0 && biPredicate != null && biPredicate.test(bufferedSubscription.subscriber, t)) {
                retryOffer = bufferedSubscription.retryOffer(t);
            }
            if (retryOffer == 0) {
                i = i >= 0 ? -1 : i - 1;
            } else if (retryOffer < 0) {
                z = true;
            } else if (i >= 0 && retryOffer > i) {
                i = retryOffer;
            }
            bufferedSubscription = bufferedSubscription2;
        }
        if (z) {
            cleanAndCount();
        }
        return i;
    }

    private int cleanAndCount() {
        BufferedSubscription<T> bufferedSubscription = this.clients;
        int i = 0;
        BufferedSubscription<T> bufferedSubscription2 = null;
        while (bufferedSubscription != null) {
            BufferedSubscription<T> bufferedSubscription3 = bufferedSubscription.next;
            if (bufferedSubscription.isClosed()) {
                bufferedSubscription.next = null;
                if (bufferedSubscription2 == null) {
                    this.clients = bufferedSubscription3;
                } else {
                    bufferedSubscription2.next = bufferedSubscription3;
                }
            } else {
                i++;
                bufferedSubscription2 = bufferedSubscription;
            }
            bufferedSubscription = bufferedSubscription3;
        }
        return i;
    }

    public int submit(T t) {
        return doOffer(t, Long.MAX_VALUE, (BiPredicate) null);
    }

    public int offer(T t, BiPredicate<Flow.Subscriber<? super T>, ? super T> biPredicate) {
        return doOffer(t, 0, biPredicate);
    }

    public int offer(T t, long j, TimeUnit timeUnit, BiPredicate<Flow.Subscriber<? super T>, ? super T> biPredicate) {
        long nanos = timeUnit.toNanos(j);
        if (nanos == Long.MAX_VALUE) {
            nanos--;
        }
        return doOffer(t, nanos, biPredicate);
    }

    public void close() {
        BufferedSubscription<T> bufferedSubscription;
        if (!this.closed) {
            synchronized (this) {
                bufferedSubscription = this.clients;
                this.clients = null;
                this.owner = null;
                this.closed = true;
            }
            while (bufferedSubscription != null) {
                BufferedSubscription<T> bufferedSubscription2 = bufferedSubscription.next;
                bufferedSubscription.next = null;
                bufferedSubscription.onComplete();
                bufferedSubscription = bufferedSubscription2;
            }
        }
    }

    public void closeExceptionally(Throwable th) {
        BufferedSubscription<T> bufferedSubscription;
        th.getClass();
        if (!this.closed) {
            synchronized (this) {
                bufferedSubscription = this.clients;
                if (!this.closed) {
                    this.closedException = th;
                    this.clients = null;
                    this.owner = null;
                    this.closed = true;
                }
            }
            while (bufferedSubscription != null) {
                BufferedSubscription<T> bufferedSubscription2 = bufferedSubscription.next;
                bufferedSubscription.next = null;
                bufferedSubscription.onError(th);
                bufferedSubscription = bufferedSubscription2;
            }
        }
    }

    public boolean isClosed() {
        return this.closed;
    }

    public Throwable getClosedException() {
        return this.closedException;
    }

    public boolean hasSubscribers() {
        boolean z;
        synchronized (this) {
            BufferedSubscription<T> bufferedSubscription = this.clients;
            while (true) {
                if (bufferedSubscription == null) {
                    z = false;
                    break;
                }
                BufferedSubscription<T> bufferedSubscription2 = bufferedSubscription.next;
                if (!bufferedSubscription.isClosed()) {
                    z = true;
                    break;
                }
                bufferedSubscription.next = null;
                this.clients = bufferedSubscription2;
                bufferedSubscription = bufferedSubscription2;
            }
        }
        return z;
    }

    public int getNumberOfSubscribers() {
        int cleanAndCount;
        synchronized (this) {
            cleanAndCount = cleanAndCount();
        }
        return cleanAndCount;
    }

    public Executor getExecutor() {
        return this.executor;
    }

    public int getMaxBufferCapacity() {
        return this.maxBufferCapacity;
    }

    public List<Flow.Subscriber<? super T>> getSubscribers() {
        ArrayList arrayList = new ArrayList();
        synchronized (this) {
            BufferedSubscription<T> bufferedSubscription = this.clients;
            BufferedSubscription<T> bufferedSubscription2 = null;
            while (bufferedSubscription != null) {
                BufferedSubscription<T> bufferedSubscription3 = bufferedSubscription.next;
                if (bufferedSubscription.isClosed()) {
                    bufferedSubscription.next = null;
                    if (bufferedSubscription2 == null) {
                        this.clients = bufferedSubscription3;
                    } else {
                        bufferedSubscription2.next = bufferedSubscription3;
                    }
                } else {
                    arrayList.add(bufferedSubscription.subscriber);
                    bufferedSubscription2 = bufferedSubscription;
                }
                bufferedSubscription = bufferedSubscription3;
            }
        }
        return arrayList;
    }

    public boolean isSubscribed(Flow.Subscriber<? super T> subscriber) {
        subscriber.getClass();
        if (this.closed) {
            return false;
        }
        synchronized (this) {
            BufferedSubscription<T> bufferedSubscription = this.clients;
            BufferedSubscription<T> bufferedSubscription2 = null;
            while (bufferedSubscription != null) {
                BufferedSubscription<T> bufferedSubscription3 = bufferedSubscription.next;
                if (bufferedSubscription.isClosed()) {
                    bufferedSubscription.next = null;
                    if (bufferedSubscription2 == null) {
                        this.clients = bufferedSubscription3;
                    } else {
                        bufferedSubscription2.next = bufferedSubscription3;
                    }
                } else if (subscriber.equals(bufferedSubscription.subscriber)) {
                    return true;
                } else {
                    bufferedSubscription2 = bufferedSubscription;
                }
                bufferedSubscription = bufferedSubscription3;
            }
            return false;
        }
    }

    public long estimateMinimumDemand() {
        long j;
        boolean z;
        synchronized (this) {
            BufferedSubscription<T> bufferedSubscription = this.clients;
            j = Long.MAX_VALUE;
            z = false;
            BufferedSubscription<T> bufferedSubscription2 = null;
            while (bufferedSubscription != null) {
                BufferedSubscription<T> bufferedSubscription3 = bufferedSubscription.next;
                int estimateLag = bufferedSubscription.estimateLag();
                if (estimateLag < 0) {
                    bufferedSubscription.next = null;
                    if (bufferedSubscription2 == null) {
                        this.clients = bufferedSubscription3;
                    } else {
                        bufferedSubscription2.next = bufferedSubscription3;
                    }
                } else {
                    long j2 = bufferedSubscription.demand - ((long) estimateLag);
                    if (j2 < j) {
                        j = j2;
                    }
                    z = true;
                    bufferedSubscription2 = bufferedSubscription;
                }
                bufferedSubscription = bufferedSubscription3;
            }
        }
        if (z) {
            return j;
        }
        return 0;
    }

    public int estimateMaximumLag() {
        int i;
        synchronized (this) {
            BufferedSubscription<T> bufferedSubscription = this.clients;
            i = 0;
            BufferedSubscription<T> bufferedSubscription2 = null;
            while (bufferedSubscription != null) {
                BufferedSubscription<T> bufferedSubscription3 = bufferedSubscription.next;
                int estimateLag = bufferedSubscription.estimateLag();
                if (estimateLag < 0) {
                    bufferedSubscription.next = null;
                    if (bufferedSubscription2 == null) {
                        this.clients = bufferedSubscription3;
                    } else {
                        bufferedSubscription2.next = bufferedSubscription3;
                    }
                } else {
                    if (estimateLag > i) {
                        i = estimateLag;
                    }
                    bufferedSubscription2 = bufferedSubscription;
                }
                bufferedSubscription = bufferedSubscription3;
            }
        }
        return i;
    }

    public CompletableFuture<Void> consume(Consumer<? super T> consumer) {
        consumer.getClass();
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        subscribe(new ConsumerSubscriber(completableFuture, consumer));
        return completableFuture;
    }

    static final class ConsumerSubscriber<T> implements Flow.Subscriber<T> {
        final Consumer<? super T> consumer;
        final CompletableFuture<Void> status;
        Flow.Subscription subscription;

        ConsumerSubscriber(CompletableFuture<Void> completableFuture, Consumer<? super T> consumer2) {
            this.status = completableFuture;
            this.consumer = consumer2;
        }

        public final void onSubscribe(Flow.Subscription subscription2) {
            this.subscription = subscription2;
            this.status.whenComplete(new SubmissionPublisher$ConsumerSubscriber$$ExternalSyntheticLambda0(subscription2));
            if (!this.status.isDone()) {
                subscription2.request(Long.MAX_VALUE);
            }
        }

        public final void onError(Throwable th) {
            this.status.completeExceptionally(th);
        }

        public final void onComplete() {
            this.status.complete(null);
        }

        public final void onNext(T t) {
            try {
                this.consumer.accept(t);
            } catch (Throwable th) {
                this.subscription.cancel();
                this.status.completeExceptionally(th);
            }
        }
    }

    static final class ConsumerTask<T> extends ForkJoinTask<Void> implements Runnable, CompletableFuture.AsynchronousCompletionTask {
        final BufferedSubscription<T> consumer;

        public final Void getRawResult() {
            return null;
        }

        public final void setRawResult(Void voidR) {
        }

        ConsumerTask(BufferedSubscription<T> bufferedSubscription) {
            this.consumer = bufferedSubscription;
        }

        public final boolean exec() {
            this.consumer.consume();
            return false;
        }

        public final void run() {
            this.consumer.consume();
        }
    }

    static final class BufferedSubscription<T> implements Flow.Subscription, ForkJoinPool.ManagedBlocker {
        static final int ACTIVE = 2;
        static final int CLOSED = 1;
        static final int COMPLETE = 16;
        static final VarHandle CTL;
        static final VarHandle DEMAND;
        static final int ERROR = 8;
        static final long INTERRUPTED = -1;
        static final int OPEN = 64;

        /* renamed from: QA */
        static final VarHandle f760QA;
        static final int REQS = 4;
        static final int RUN = 32;
        Object[] array;
        volatile int ctl;
        volatile long demand;
        Executor executor;
        int head;
        final int maxCapacity;
        BufferedSubscription<T> next;
        BufferedSubscription<T> nextRetry;
        final BiConsumer<? super Flow.Subscriber<? super T>, ? super Throwable> onNextHandler;
        Throwable pendingError;
        final Flow.Subscriber<? super T> subscriber;
        int tail;
        long timeout;
        Thread waiter;
        volatile int waiting;

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.casDemand(long, long):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final boolean casDemand(long r1, long r3) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.casDemand(long, long):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.casDemand(long, long):boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.getAndBitwiseOrCtl(int):int, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final int getAndBitwiseOrCtl(int r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.getAndBitwiseOrCtl(int):int, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.getAndBitwiseOrCtl(int):int");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.growAndOffer(java.lang.Object, java.lang.Object[], int):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final boolean growAndOffer(T r1, java.lang.Object[] r2, int r3) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.growAndOffer(java.lang.Object, java.lang.Object[], int):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.growAndOffer(java.lang.Object, java.lang.Object[], int):boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.isReleasable():boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        public final boolean isReleasable() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.isReleasable():boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.isReleasable():boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.offer(java.lang.Object, boolean):int, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final int offer(T r1, boolean r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.offer(java.lang.Object, boolean):int, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.offer(java.lang.Object, boolean):int");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.retryOffer(java.lang.Object):int, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final int retryOffer(T r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.retryOffer(java.lang.Object):int, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.retryOffer(java.lang.Object):int");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.subtractDemand(int):long, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final long subtractDemand(int r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.subtractDemand(int):long, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.subtractDemand(int):long");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.takeItems(java.util.concurrent.Flow$Subscriber, long, int):int, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final int takeItems(java.util.concurrent.Flow.Subscriber<? super T> r1, long r2, int r4) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.takeItems(java.util.concurrent.Flow$Subscriber, long, int):int, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.takeItems(java.util.concurrent.Flow$Subscriber, long, int):int");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.weakCasCtl(int, int):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:58)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 5 more
            */
        final boolean weakCasCtl(int r1, int r2) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.SubmissionPublisher.BufferedSubscription.weakCasCtl(int, int):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.SubmissionPublisher.BufferedSubscription.weakCasCtl(int, int):boolean");
        }

        BufferedSubscription(Flow.Subscriber<? super T> subscriber2, Executor executor2, BiConsumer<? super Flow.Subscriber<? super T>, ? super Throwable> biConsumer, Object[] objArr, int i) {
            this.subscriber = subscriber2;
            this.executor = executor2;
            this.onNextHandler = biConsumer;
            this.array = objArr;
            this.maxCapacity = i;
        }

        /* access modifiers changed from: package-private */
        public final boolean isClosed() {
            return (this.ctl & 1) != 0;
        }

        /* access modifiers changed from: package-private */
        public final int estimateLag() {
            int i = this.ctl;
            int i2 = this.tail - this.head;
            if ((i & 1) != 0) {
                return -1;
            }
            if (i2 < 0) {
                return 0;
            }
            return i2;
        }

        /* access modifiers changed from: package-private */
        public final int startOnOffer(int i) {
            int i2 = this.ctl;
            if ((i2 & 6) == 4) {
                i2 = getAndBitwiseOrCtl(34);
                if ((i2 & 33) == 0) {
                    tryStart();
                    return i;
                }
            }
            if ((i2 & 1) != 0) {
                return -1;
            }
            return i;
        }

        /* access modifiers changed from: package-private */
        public final void tryStart() {
            try {
                ConsumerTask consumerTask = new ConsumerTask(this);
                Executor executor2 = this.executor;
                if (executor2 != null) {
                    executor2.execute(consumerTask);
                }
            } catch (Error | RuntimeException e) {
                getAndBitwiseOrCtl(9);
                throw e;
            }
        }

        /* access modifiers changed from: package-private */
        public final void startOnSignal(int i) {
            if ((this.ctl & i) != i && (getAndBitwiseOrCtl(i) & 33) == 0) {
                tryStart();
            }
        }

        /* access modifiers changed from: package-private */
        public final void onSubscribe() {
            startOnSignal(34);
        }

        /* access modifiers changed from: package-private */
        public final void onComplete() {
            startOnSignal(50);
        }

        /* access modifiers changed from: package-private */
        public final void onError(Throwable th) {
            if (th != null) {
                this.pendingError = th;
            }
            int andBitwiseOrCtl = getAndBitwiseOrCtl(42);
            if ((andBitwiseOrCtl & 1) != 0) {
                return;
            }
            if ((andBitwiseOrCtl & 32) == 0) {
                tryStart();
                return;
            }
            Object[] objArr = this.array;
            if (objArr != null) {
                Arrays.fill(objArr, (Object) null);
            }
        }

        public final void cancel() {
            onError((Throwable) null);
        }

        public final void request(long j) {
            long j2;
            long j3;
            if (j > 0) {
                do {
                    j2 = this.demand;
                    j3 = j2 + j;
                    if (j3 < j2) {
                        j3 = Long.MAX_VALUE;
                    }
                } while (!casDemand(j2, j3));
                startOnSignal(38);
                return;
            }
            onError(new IllegalArgumentException("non-positive subscription request"));
        }

        /* access modifiers changed from: package-private */
        public final void consume() {
            Flow.Subscriber<? super T> subscriber2 = this.subscriber;
            if (subscriber2 != null) {
                subscribeOnOpen(subscriber2);
                long j = this.demand;
                int i = this.head;
                int i2 = this.tail;
                while (true) {
                    int i3 = this.ctl;
                    if ((i3 & 8) != 0) {
                        closeOnError(subscriber2, (Throwable) null);
                        return;
                    }
                    int takeItems = takeItems(subscriber2, j, i);
                    if (takeItems > 0) {
                        i += takeItems;
                        this.head = i;
                        j = subtractDemand(takeItems);
                    } else {
                        j = this.demand;
                        int i4 = (j > 0 ? 1 : (j == 0 ? 0 : -1));
                        if (i4 == 0 && (i3 & 4) != 0) {
                            weakCasCtl(i3, i3 & -5);
                        } else if (i4 == 0 || (i3 & 4) != 0) {
                            int i5 = this.tail;
                            if (i2 == i5) {
                                boolean z = i5 == i;
                                if (z && (i3 & 16) != 0) {
                                    closeOnComplete(subscriber2);
                                    return;
                                } else if (z || i4 == 0) {
                                    int i6 = (i3 & 2) != 0 ? 2 : 32;
                                    if (weakCasCtl(i3, (~i6) & i3) && i6 == 32) {
                                        return;
                                    }
                                }
                            }
                            i2 = i5;
                        } else {
                            weakCasCtl(i3, i3 | 4);
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final boolean consumeNext(Flow.Subscriber<? super T> subscriber2, Object obj) {
            if (subscriber2 == null) {
                return true;
            }
            try {
                subscriber2.onNext(obj);
                return true;
            } catch (Throwable th) {
                handleOnNext(subscriber2, th);
                return false;
            }
        }

        /* access modifiers changed from: package-private */
        public final void handleOnNext(Flow.Subscriber<? super T> subscriber2, Throwable th) {
            try {
                BiConsumer<? super Flow.Subscriber<? super T>, ? super Throwable> biConsumer = this.onNextHandler;
                if (biConsumer != null) {
                    biConsumer.accept(subscriber2, th);
                }
            } catch (Throwable unused) {
            }
            closeOnError(subscriber2, th);
        }

        /* access modifiers changed from: package-private */
        public final void subscribeOnOpen(Flow.Subscriber<? super T> subscriber2) {
            if ((this.ctl & 64) == 0 && (getAndBitwiseOrCtl(64) & 64) == 0) {
                consumeSubscribe(subscriber2);
            }
        }

        /* access modifiers changed from: package-private */
        public final void consumeSubscribe(Flow.Subscriber<? super T> subscriber2) {
            if (subscriber2 != null) {
                try {
                    subscriber2.onSubscribe(this);
                } catch (Throwable th) {
                    closeOnError(subscriber2, th);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final void closeOnComplete(Flow.Subscriber<? super T> subscriber2) {
            if ((1 & getAndBitwiseOrCtl(1)) == 0) {
                consumeComplete(subscriber2);
            }
        }

        /* access modifiers changed from: package-private */
        public final void consumeComplete(Flow.Subscriber<? super T> subscriber2) {
            if (subscriber2 != null) {
                try {
                    subscriber2.onComplete();
                } catch (Throwable unused) {
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final void closeOnError(Flow.Subscriber<? super T> subscriber2, Throwable th) {
            if ((getAndBitwiseOrCtl(9) & 1) == 0) {
                if (th == null) {
                    th = this.pendingError;
                }
                this.pendingError = null;
                this.executor = null;
                signalWaiter();
                consumeError(subscriber2, th);
            }
        }

        /* access modifiers changed from: package-private */
        public final void consumeError(Flow.Subscriber<? super T> subscriber2, Throwable th) {
            if (th != null && subscriber2 != null) {
                try {
                    subscriber2.onError(th);
                } catch (Throwable unused) {
                }
            }
        }

        /* access modifiers changed from: package-private */
        public final void signalWaiter() {
            this.waiting = 0;
            Thread thread = this.waiter;
            if (thread != null) {
                LockSupport.unpark(thread);
            }
        }

        /* access modifiers changed from: package-private */
        public final void awaitSpace(long j) {
            if (!isReleasable()) {
                ForkJoinPool.helpAsyncBlocker(this.executor, this);
                if (!isReleasable()) {
                    this.timeout = j;
                    try {
                        ForkJoinPool.managedBlock(this);
                    } catch (InterruptedException unused) {
                        this.timeout = -1;
                    }
                    if (this.timeout == -1) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        public final boolean block() {
            long j = this.timeout;
            boolean z = j < Long.MAX_VALUE;
            long nanoTime = z ? System.nanoTime() + j : 0;
            while (!isReleasable()) {
                if (Thread.interrupted()) {
                    this.timeout = -1;
                    if (z) {
                        break;
                    }
                } else {
                    if (z) {
                        j = nanoTime - System.nanoTime();
                        if (j <= 0) {
                            break;
                        }
                    }
                    if (this.waiter == null) {
                        this.waiter = Thread.currentThread();
                    } else if (this.waiting == 0) {
                        this.waiting = 1;
                    } else if (z) {
                        LockSupport.parkNanos(this, j);
                    } else {
                        LockSupport.park(this);
                    }
                }
            }
            this.waiter = null;
            this.waiting = 0;
            return true;
        }

        static {
            Class<BufferedSubscription> cls = BufferedSubscription.class;
            try {
                MethodHandles.Lookup lookup = MethodHandles.lookup();
                CTL = lookup.findVarHandle(cls, "ctl", Integer.TYPE);
                DEMAND = lookup.findVarHandle(cls, "demand", Long.TYPE);
                f760QA = MethodHandles.arrayElementVarHandle(Object[].class);
                Class<LockSupport> cls2 = LockSupport.class;
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError((Throwable) e);
            }
        }
    }
}
