package sun.nio.p033ch;

import java.nio.channels.spi.AsynchronousChannelProvider;
import java.p026io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import sun.nio.p033ch.Port;

/* renamed from: sun.nio.ch.EPollPort */
final class EPollPort extends Port {
    private static final int ENOENT = 2;
    private static final int MAX_EPOLL_EVENTS = 512;
    /* access modifiers changed from: private */
    public final Event EXECUTE_TASK_OR_SHUTDOWN;
    /* access modifiers changed from: private */
    public final Event NEED_TO_POLL;
    /* access modifiers changed from: private */
    public final long address;
    private boolean closed;
    /* access modifiers changed from: private */
    public final int epfd;
    /* access modifiers changed from: private */
    public final ArrayBlockingQueue<Event> queue;
    /* access modifiers changed from: private */

    /* renamed from: sp */
    public final int[] f880sp;
    /* access modifiers changed from: private */
    public final AtomicInteger wakeupCount = new AtomicInteger();

    private static native void close0(int i);

    /* access modifiers changed from: private */
    public static native void drain1(int i) throws IOException;

    private static native void interrupt(int i) throws IOException;

    private static native void socketpair(int[] iArr) throws IOException;

    /* renamed from: sun.nio.ch.EPollPort$Event */
    static class Event {
        final Port.PollableChannel channel;
        final int events;

        Event(Port.PollableChannel pollableChannel, int i) {
            this.channel = pollableChannel;
            this.events = i;
        }

        /* access modifiers changed from: package-private */
        public Port.PollableChannel channel() {
            return this.channel;
        }

        /* access modifiers changed from: package-private */
        public int events() {
            return this.events;
        }
    }

    EPollPort(AsynchronousChannelProvider asynchronousChannelProvider, ThreadPool threadPool) throws IOException {
        super(asynchronousChannelProvider, threadPool);
        Event event = new Event((Port.PollableChannel) null, 0);
        this.NEED_TO_POLL = event;
        this.EXECUTE_TASK_OR_SHUTDOWN = new Event((Port.PollableChannel) null, 0);
        int epollCreate = EPoll.epollCreate();
        this.epfd = epollCreate;
        int[] iArr = new int[2];
        try {
            socketpair(iArr);
            EPoll.epollCtl(epollCreate, 1, iArr[0], Net.POLLIN);
            this.f880sp = iArr;
            this.address = EPoll.allocatePollArray(512);
            ArrayBlockingQueue<Event> arrayBlockingQueue = new ArrayBlockingQueue<>(512);
            this.queue = arrayBlockingQueue;
            arrayBlockingQueue.offer(event);
        } catch (IOException e) {
            close0(this.epfd);
            throw e;
        }
    }

    /* access modifiers changed from: package-private */
    public EPollPort start() {
        startThreads(new EventHandlerTask());
        return this;
    }

    /* access modifiers changed from: private */
    public void implClose() {
        synchronized (this) {
            if (!this.closed) {
                this.closed = true;
                EPoll.freePollArray(this.address);
                close0(this.f880sp[0]);
                close0(this.f880sp[1]);
                close0(this.epfd);
            }
        }
    }

    private void wakeup() {
        if (this.wakeupCount.incrementAndGet() == 1) {
            try {
                interrupt(this.f880sp[1]);
            } catch (IOException e) {
                throw new AssertionError((Object) e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void executeOnHandlerTask(Runnable runnable) {
        synchronized (this) {
            if (!this.closed) {
                offerTask(runnable);
                wakeup();
            } else {
                throw new RejectedExecutionException();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void shutdownHandlerTasks() {
        int threadCount = threadCount();
        if (threadCount == 0) {
            implClose();
            return;
        }
        while (true) {
            int i = threadCount - 1;
            if (threadCount > 0) {
                wakeup();
                threadCount = i;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void startPoll(int i, int i2) {
        int i3 = i2 | 1073741824;
        int epollCtl = EPoll.epollCtl(this.epfd, 3, i, i3);
        if (epollCtl == 2) {
            epollCtl = EPoll.epollCtl(this.epfd, 1, i, i3);
        }
        if (epollCtl != 0) {
            throw new AssertionError();
        }
    }

    /* renamed from: sun.nio.ch.EPollPort$EventHandlerTask */
    private class EventHandlerTask implements Runnable {
        private EventHandlerTask() {
        }

        private Event poll() throws IOException {
            while (true) {
                try {
                    int epollWait = EPoll.epollWait(EPollPort.this.epfd, EPollPort.this.address, 512);
                    EPollPort.this.fdToChannelLock.readLock().lock();
                    while (true) {
                        int i = epollWait - 1;
                        if (epollWait <= 0) {
                            break;
                        }
                        long event = EPoll.getEvent(EPollPort.this.address, i);
                        int descriptor = EPoll.getDescriptor(event);
                        if (descriptor == EPollPort.this.f880sp[0]) {
                            if (EPollPort.this.wakeupCount.decrementAndGet() == 0) {
                                EPollPort.drain1(EPollPort.this.f880sp[0]);
                            }
                            if (i > 0) {
                                EPollPort.this.queue.offer(EPollPort.this.EXECUTE_TASK_OR_SHUTDOWN);
                            } else {
                                Event r0 = EPollPort.this.EXECUTE_TASK_OR_SHUTDOWN;
                                EPollPort.this.fdToChannelLock.readLock().unlock();
                                EPollPort.this.queue.offer(EPollPort.this.NEED_TO_POLL);
                                return r0;
                            }
                        } else {
                            Port.PollableChannel pollableChannel = (Port.PollableChannel) EPollPort.this.fdToChannel.get(Integer.valueOf(descriptor));
                            if (pollableChannel != null) {
                                Event event2 = new Event(pollableChannel, EPoll.getEvents(event));
                                if (i > 0) {
                                    EPollPort.this.queue.offer(event2);
                                } else {
                                    EPollPort.this.fdToChannelLock.readLock().unlock();
                                    EPollPort.this.queue.offer(EPollPort.this.NEED_TO_POLL);
                                    return event2;
                                }
                            } else {
                                continue;
                            }
                        }
                        epollWait = i;
                    }
                    EPollPort.this.fdToChannelLock.readLock().unlock();
                } catch (Throwable th) {
                    EPollPort.this.queue.offer(EPollPort.this.NEED_TO_POLL);
                    throw th;
                }
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
            r4.channel().onEvent(r4.events(), r3);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r6 = this;
                sun.nio.ch.Invoker$GroupAndInvokeCount r0 = sun.nio.p033ch.Invoker.getGroupAndInvokeCount()
                r1 = 0
                r2 = 1
                if (r0 == 0) goto L_0x000a
                r3 = r2
                goto L_0x000b
            L_0x000a:
                r3 = r1
            L_0x000b:
                r4 = r1
            L_0x000c:
                if (r3 == 0) goto L_0x0016
                r0.resetInvokeCount()     // Catch:{ all -> 0x0012 }
                goto L_0x0016
            L_0x0012:
                r0 = move-exception
                r1 = r4
                goto L_0x0088
            L_0x0016:
                sun.nio.ch.EPollPort r4 = sun.nio.p033ch.EPollPort.this     // Catch:{ InterruptedException -> 0x000b }
                java.util.concurrent.ArrayBlockingQueue r4 = r4.queue     // Catch:{ InterruptedException -> 0x000b }
                java.lang.Object r4 = r4.take()     // Catch:{ InterruptedException -> 0x000b }
                sun.nio.ch.EPollPort$Event r4 = (sun.nio.p033ch.EPollPort.Event) r4     // Catch:{ InterruptedException -> 0x000b }
                sun.nio.ch.EPollPort r5 = sun.nio.p033ch.EPollPort.this     // Catch:{ InterruptedException -> 0x000b }
                sun.nio.ch.EPollPort$Event r5 = r5.NEED_TO_POLL     // Catch:{ InterruptedException -> 0x000b }
                if (r4 != r5) goto L_0x0049
                sun.nio.ch.EPollPort$Event r4 = r6.poll()     // Catch:{ IOException -> 0x002f }
                goto L_0x0049
            L_0x002f:
                r4 = move-exception
                r4.printStackTrace()     // Catch:{ InterruptedException -> 0x000b }
                sun.nio.ch.EPollPort r0 = sun.nio.p033ch.EPollPort.this
                int r0 = r0.threadExit(r6, r1)
                if (r0 != 0) goto L_0x0048
                sun.nio.ch.EPollPort r0 = sun.nio.p033ch.EPollPort.this
                boolean r0 = r0.isShutdown()
                if (r0 == 0) goto L_0x0048
                sun.nio.ch.EPollPort r6 = sun.nio.p033ch.EPollPort.this
                r6.implClose()
            L_0x0048:
                return
            L_0x0049:
                sun.nio.ch.EPollPort r5 = sun.nio.p033ch.EPollPort.this     // Catch:{ all -> 0x0087 }
                sun.nio.ch.EPollPort$Event r5 = r5.EXECUTE_TASK_OR_SHUTDOWN     // Catch:{ all -> 0x0087 }
                if (r4 != r5) goto L_0x0074
                sun.nio.ch.EPollPort r4 = sun.nio.p033ch.EPollPort.this     // Catch:{ all -> 0x0087 }
                java.lang.Runnable r4 = r4.pollTask()     // Catch:{ all -> 0x0087 }
                if (r4 != 0) goto L_0x006f
                sun.nio.ch.EPollPort r0 = sun.nio.p033ch.EPollPort.this
                int r0 = r0.threadExit(r6, r1)
                if (r0 != 0) goto L_0x006e
                sun.nio.ch.EPollPort r0 = sun.nio.p033ch.EPollPort.this
                boolean r0 = r0.isShutdown()
                if (r0 == 0) goto L_0x006e
                sun.nio.ch.EPollPort r6 = sun.nio.p033ch.EPollPort.this
                r6.implClose()
            L_0x006e:
                return
            L_0x006f:
                r4.run()     // Catch:{ all -> 0x0084 }
                r4 = r2
                goto L_0x000c
            L_0x0074:
                sun.nio.ch.Port$PollableChannel r5 = r4.channel()     // Catch:{ Error -> 0x0082, RuntimeException -> 0x0080 }
                int r4 = r4.events()     // Catch:{ Error -> 0x0082, RuntimeException -> 0x0080 }
                r5.onEvent(r4, r3)     // Catch:{ Error -> 0x0082, RuntimeException -> 0x0080 }
                goto L_0x000b
            L_0x0080:
                r0 = move-exception
                throw r0     // Catch:{ all -> 0x0084 }
            L_0x0082:
                r0 = move-exception
                throw r0     // Catch:{ all -> 0x0084 }
            L_0x0084:
                r0 = move-exception
                r1 = r2
                goto L_0x0088
            L_0x0087:
                r0 = move-exception
            L_0x0088:
                sun.nio.ch.EPollPort r2 = sun.nio.p033ch.EPollPort.this
                int r1 = r2.threadExit(r6, r1)
                if (r1 != 0) goto L_0x009d
                sun.nio.ch.EPollPort r1 = sun.nio.p033ch.EPollPort.this
                boolean r1 = r1.isShutdown()
                if (r1 == 0) goto L_0x009d
                sun.nio.ch.EPollPort r6 = sun.nio.p033ch.EPollPort.this
                r6.implClose()
            L_0x009d:
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: sun.nio.p033ch.EPollPort.EventHandlerTask.run():void");
        }
    }
}
