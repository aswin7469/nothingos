package java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractExecutorService implements ExecutorService {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* access modifiers changed from: protected */
    public <T> RunnableFuture<T> newTaskFor(Runnable runnable, T t) {
        return new FutureTask(runnable, t);
    }

    /* access modifiers changed from: protected */
    public <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask(callable);
    }

    public Future<?> submit(Runnable runnable) {
        runnable.getClass();
        RunnableFuture newTaskFor = newTaskFor(runnable, (Object) null);
        execute(newTaskFor);
        return newTaskFor;
    }

    public <T> Future<T> submit(Runnable runnable, T t) {
        runnable.getClass();
        RunnableFuture newTaskFor = newTaskFor(runnable, t);
        execute(newTaskFor);
        return newTaskFor;
    }

    public <T> Future<T> submit(Callable<T> callable) {
        callable.getClass();
        RunnableFuture<T> newTaskFor = newTaskFor(callable);
        execute(newTaskFor);
        return newTaskFor;
    }

    private <T> T doInvokeAny(Collection<? extends Callable<T>> collection, boolean z, long j) throws InterruptedException, ExecutionException, TimeoutException {
        long j2;
        ExecutionException e;
        collection.getClass();
        int size = collection.size();
        if (size != 0) {
            ArrayList arrayList = new ArrayList(size);
            ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(this);
            if (z) {
                try {
                    j2 = System.nanoTime() + j;
                } catch (ExecutionException e2) {
                    e = e2;
                } catch (RuntimeException e3) {
                    e = new ExecutionException((Throwable) e3);
                } catch (Throwable th) {
                    cancelAll(arrayList);
                    throw th;
                }
            } else {
                j2 = 0;
            }
            Iterator<? extends Callable<T>> it = collection.iterator();
            arrayList.add(executorCompletionService.submit((Callable) it.next()));
            int i = size - 1;
            int i2 = 1;
            e = null;
            while (true) {
                Future poll = executorCompletionService.poll();
                if (poll == null) {
                    if (i > 0) {
                        i--;
                        arrayList.add(executorCompletionService.submit((Callable) it.next()));
                        i2++;
                    } else if (i2 == 0) {
                        if (e == null) {
                            e = new ExecutionException();
                        }
                        throw e;
                    } else if (z) {
                        poll = executorCompletionService.poll(j, TimeUnit.NANOSECONDS);
                        if (poll != null) {
                            j = j2 - System.nanoTime();
                        } else {
                            throw new TimeoutException();
                        }
                    } else {
                        poll = executorCompletionService.take();
                    }
                }
                if (poll != null) {
                    i2--;
                    T t = poll.get();
                    cancelAll(arrayList);
                    return t;
                }
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws InterruptedException, ExecutionException {
        try {
            return doInvokeAny(collection, false, 0);
        } catch (TimeoutException unused) {
            return null;
        }
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return doInvokeAny(collection, true, timeUnit.toNanos(j));
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        collection.getClass();
        ArrayList arrayList = new ArrayList(collection.size());
        try {
            for (Callable newTaskFor : collection) {
                RunnableFuture newTaskFor2 = newTaskFor(newTaskFor);
                arrayList.add(newTaskFor2);
                execute(newTaskFor2);
            }
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                Future future = (Future) arrayList.get(i);
                if (!future.isDone()) {
                    try {
                        future.get();
                    } catch (CancellationException | ExecutionException unused) {
                    }
                }
            }
            return arrayList;
        } catch (Throwable th) {
            cancelAll(arrayList);
            throw th;
        }
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection, long j, TimeUnit timeUnit) throws InterruptedException {
        long j2;
        collection.getClass();
        long nanos = timeUnit.toNanos(j);
        long nanoTime = System.nanoTime() + nanos;
        ArrayList arrayList = new ArrayList(collection.size());
        try {
            for (Callable newTaskFor : collection) {
                arrayList.add(newTaskFor(newTaskFor));
            }
            int size = arrayList.size();
            int i = 0;
            for (int i2 = 0; i2 < size; i2++) {
                if (i2 == 0) {
                    j2 = nanos;
                } else {
                    j2 = nanoTime - System.nanoTime();
                }
                if (j2 <= 0) {
                    cancelAll(arrayList, i);
                    return arrayList;
                }
                execute((Runnable) arrayList.get(i2));
            }
            while (i < size) {
                Future future = (Future) arrayList.get(i);
                if (!future.isDone()) {
                    try {
                        future.get(nanoTime - System.nanoTime(), TimeUnit.NANOSECONDS);
                    } catch (CancellationException | ExecutionException unused) {
                    } catch (TimeoutException unused2) {
                    }
                }
                i++;
            }
            return arrayList;
        } catch (Throwable th) {
            cancelAll(arrayList);
            throw th;
        }
    }

    private static <T> void cancelAll(ArrayList<Future<T>> arrayList) {
        cancelAll(arrayList, 0);
    }

    private static <T> void cancelAll(ArrayList<Future<T>> arrayList, int i) {
        int size = arrayList.size();
        while (i < size) {
            arrayList.get(i).cancel(true);
            i++;
        }
    }
}
