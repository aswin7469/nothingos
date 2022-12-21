package java.util.concurrent;

public class ExecutorCompletionService<V> implements CompletionService<V> {
    private final AbstractExecutorService aes;
    private final BlockingQueue<Future<V>> completionQueue;
    private final Executor executor;

    private static class QueueingFuture<V> extends FutureTask<Void> {
        private final BlockingQueue<Future<V>> completionQueue;
        private final Future<V> task;

        QueueingFuture(RunnableFuture<V> runnableFuture, BlockingQueue<Future<V>> blockingQueue) {
            super(runnableFuture, null);
            this.task = runnableFuture;
            this.completionQueue = blockingQueue;
        }

        /* access modifiers changed from: protected */
        public void done() {
            this.completionQueue.add(this.task);
        }
    }

    private RunnableFuture<V> newTaskFor(Callable<V> callable) {
        AbstractExecutorService abstractExecutorService = this.aes;
        if (abstractExecutorService == null) {
            return new FutureTask(callable);
        }
        return abstractExecutorService.newTaskFor(callable);
    }

    private RunnableFuture<V> newTaskFor(Runnable runnable, V v) {
        AbstractExecutorService abstractExecutorService = this.aes;
        if (abstractExecutorService == null) {
            return new FutureTask(runnable, v);
        }
        return abstractExecutorService.newTaskFor(runnable, v);
    }

    public ExecutorCompletionService(Executor executor2) {
        executor2.getClass();
        this.executor = executor2;
        this.aes = executor2 instanceof AbstractExecutorService ? (AbstractExecutorService) executor2 : null;
        this.completionQueue = new LinkedBlockingQueue();
    }

    public ExecutorCompletionService(Executor executor2, BlockingQueue<Future<V>> blockingQueue) {
        AbstractExecutorService abstractExecutorService = null;
        if (executor2 == null || blockingQueue == null) {
            throw null;
        }
        this.executor = executor2;
        this.aes = executor2 instanceof AbstractExecutorService ? (AbstractExecutorService) executor2 : abstractExecutorService;
        this.completionQueue = blockingQueue;
    }

    public Future<V> submit(Callable<V> callable) {
        callable.getClass();
        RunnableFuture<V> newTaskFor = newTaskFor(callable);
        this.executor.execute(new QueueingFuture(newTaskFor, this.completionQueue));
        return newTaskFor;
    }

    public Future<V> submit(Runnable runnable, V v) {
        runnable.getClass();
        RunnableFuture newTaskFor = newTaskFor(runnable, v);
        this.executor.execute(new QueueingFuture(newTaskFor, this.completionQueue));
        return newTaskFor;
    }

    public Future<V> take() throws InterruptedException {
        return this.completionQueue.take();
    }

    public Future<V> poll() {
        return this.completionQueue.poll();
    }

    public Future<V> poll(long j, TimeUnit timeUnit) throws InterruptedException {
        return this.completionQueue.poll(j, timeUnit);
    }
}
