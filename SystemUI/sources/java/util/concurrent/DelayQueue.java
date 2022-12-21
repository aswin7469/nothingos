package java.util.concurrent;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DelayQueue<E extends Delayed> extends AbstractQueue<E> implements BlockingQueue<E> {
    private final Condition available;
    private Thread leader;
    private final transient ReentrantLock lock;

    /* renamed from: q */
    private final PriorityQueue<E> f751q = new PriorityQueue<>();

    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    public DelayQueue() {
        ReentrantLock reentrantLock = new ReentrantLock();
        this.lock = reentrantLock;
        this.available = reentrantLock.newCondition();
    }

    public DelayQueue(Collection<? extends E> collection) {
        ReentrantLock reentrantLock = new ReentrantLock();
        this.lock = reentrantLock;
        this.available = reentrantLock.newCondition();
        addAll(collection);
    }

    public boolean add(E e) {
        return offer(e);
    }

    /* JADX INFO: finally extract failed */
    public boolean offer(E e) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            this.f751q.offer(e);
            if (this.f751q.peek() == e) {
                this.leader = null;
                this.available.signal();
            }
            reentrantLock.unlock();
            return true;
        } catch (Throwable th) {
            reentrantLock.unlock();
            throw th;
        }
    }

    public void put(E e) {
        offer(e);
    }

    public boolean offer(E e, long j, TimeUnit timeUnit) {
        return offer(e);
    }

    public E poll() {
        E e;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Delayed delayed = (Delayed) this.f751q.peek();
            if (delayed != null) {
                if (delayed.getDelay(TimeUnit.NANOSECONDS) <= 0) {
                    e = (Delayed) this.f751q.poll();
                    return e;
                }
            }
            e = null;
            return e;
        } finally {
            reentrantLock.unlock();
        }
    }

    public E take() throws InterruptedException {
        Thread currentThread;
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                Delayed delayed = (Delayed) this.f751q.peek();
                if (delayed == null) {
                    this.available.await();
                } else {
                    long delay = delayed.getDelay(TimeUnit.NANOSECONDS);
                    if (delay <= 0) {
                        break;
                    } else if (this.leader != null) {
                        this.available.await();
                    } else {
                        currentThread = Thread.currentThread();
                        this.leader = currentThread;
                        this.available.awaitNanos(delay);
                        if (this.leader == currentThread) {
                            this.leader = null;
                        }
                    }
                }
            } catch (Throwable th) {
                if (this.leader == null && this.f751q.peek() != null) {
                    this.available.signal();
                }
                reentrantLock.unlock();
                throw th;
            }
        }
        E e = (Delayed) this.f751q.poll();
        if (this.leader == null && this.f751q.peek() != null) {
            this.available.signal();
        }
        reentrantLock.unlock();
        return e;
    }

    public E poll(long j, TimeUnit timeUnit) throws InterruptedException {
        Thread currentThread;
        long nanos = timeUnit.toNanos(j);
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lockInterruptibly();
        while (true) {
            try {
                Delayed delayed = (Delayed) this.f751q.peek();
                if (delayed != null) {
                    long delay = delayed.getDelay(TimeUnit.NANOSECONDS);
                    if (delay <= 0) {
                        E e = (Delayed) this.f751q.poll();
                        if (this.leader == null && this.f751q.peek() != null) {
                            this.available.signal();
                        }
                        reentrantLock.unlock();
                        return e;
                    } else if (nanos <= 0) {
                        if (this.leader == null && this.f751q.peek() != null) {
                            this.available.signal();
                        }
                        reentrantLock.unlock();
                        return null;
                    } else {
                        if (nanos >= delay) {
                            if (this.leader == null) {
                                currentThread = Thread.currentThread();
                                this.leader = currentThread;
                                nanos -= delay - this.available.awaitNanos(delay);
                                if (this.leader == currentThread) {
                                    this.leader = null;
                                }
                            }
                        }
                        nanos = this.available.awaitNanos(nanos);
                    }
                } else if (nanos <= 0) {
                    if (this.leader == null && this.f751q.peek() != null) {
                        this.available.signal();
                    }
                    reentrantLock.unlock();
                    return null;
                } else {
                    nanos = this.available.awaitNanos(nanos);
                }
            } catch (Throwable th) {
                if (this.leader == null && this.f751q.peek() != null) {
                    this.available.signal();
                }
                reentrantLock.unlock();
                throw th;
            }
        }
    }

    public E peek() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return (Delayed) this.f751q.peek();
        } finally {
            reentrantLock.unlock();
        }
    }

    public int size() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.f751q.size();
        } finally {
            reentrantLock.unlock();
        }
    }

    public int drainTo(Collection<? super E> collection) {
        return drainTo(collection, Integer.MAX_VALUE);
    }

    public int drainTo(Collection<? super E> collection, int i) {
        Objects.requireNonNull(collection);
        if (collection != this) {
            int i2 = 0;
            if (i <= 0) {
                return 0;
            }
            ReentrantLock reentrantLock = this.lock;
            reentrantLock.lock();
            while (i2 < i) {
                try {
                    Delayed delayed = (Delayed) this.f751q.peek();
                    if (delayed == null || delayed.getDelay(TimeUnit.NANOSECONDS) > 0) {
                        break;
                    }
                    collection.add(delayed);
                    this.f751q.poll();
                    i2++;
                } catch (Throwable th) {
                    reentrantLock.unlock();
                    throw th;
                }
            }
            reentrantLock.unlock();
            return i2;
        }
        throw new IllegalArgumentException();
    }

    public void clear() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            this.f751q.clear();
        } finally {
            reentrantLock.unlock();
        }
    }

    public Object[] toArray() {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.f751q.toArray();
        } finally {
            reentrantLock.unlock();
        }
    }

    public <T> T[] toArray(T[] tArr) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.f751q.toArray(tArr);
        } finally {
            reentrantLock.unlock();
        }
    }

    public boolean remove(Object obj) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            return this.f751q.remove(obj);
        } finally {
            reentrantLock.unlock();
        }
    }

    /* access modifiers changed from: package-private */
    public void removeEQ(Object obj) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            Iterator<E> it = this.f751q.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (obj == it.next()) {
                        it.remove();
                        break;
                    }
                } else {
                    break;
                }
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public Iterator<E> iterator() {
        return new Itr(toArray());
    }

    private class Itr implements Iterator<E> {
        final Object[] array;
        int cursor;
        int lastRet = -1;

        Itr(Object[] objArr) {
            this.array = objArr;
        }

        public boolean hasNext() {
            return this.cursor < this.array.length;
        }

        public E next() {
            int i = this.cursor;
            E[] eArr = this.array;
            if (i < eArr.length) {
                this.cursor = i + 1;
                this.lastRet = i;
                return (Delayed) eArr[i];
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            int i = this.lastRet;
            if (i >= 0) {
                DelayQueue.this.removeEQ(this.array[i]);
                this.lastRet = -1;
                return;
            }
            throw new IllegalStateException();
        }
    }
}
