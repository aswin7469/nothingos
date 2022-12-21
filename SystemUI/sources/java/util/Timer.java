package java.util;

import java.util.concurrent.atomic.AtomicInteger;
import kotlin.time.DurationKt;

public class Timer {
    private static final AtomicInteger nextSerialNumber = new AtomicInteger(0);
    /* access modifiers changed from: private */
    public final TaskQueue queue;
    /* access modifiers changed from: private */
    public final TimerThread thread;
    private final Object threadReaper;

    private static int serialNumber() {
        return nextSerialNumber.getAndIncrement();
    }

    public Timer() {
        this("Timer-" + serialNumber());
    }

    public Timer(boolean isDaemon) {
        this("Timer-" + serialNumber(), isDaemon);
    }

    public Timer(String name) {
        TaskQueue taskQueue = new TaskQueue();
        this.queue = taskQueue;
        TimerThread timerThread = new TimerThread(taskQueue);
        this.thread = timerThread;
        this.threadReaper = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() throws Throwable {
                synchronized (Timer.this.queue) {
                    Timer.this.thread.newTasksMayBeScheduled = false;
                    Timer.this.queue.notify();
                }
            }
        };
        timerThread.setName(name);
        timerThread.start();
    }

    public Timer(String name, boolean isDaemon) {
        TaskQueue taskQueue = new TaskQueue();
        this.queue = taskQueue;
        TimerThread timerThread = new TimerThread(taskQueue);
        this.thread = timerThread;
        this.threadReaper = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() throws Throwable {
                synchronized (Timer.this.queue) {
                    Timer.this.thread.newTasksMayBeScheduled = false;
                    Timer.this.queue.notify();
                }
            }
        };
        timerThread.setName(name);
        timerThread.setDaemon(isDaemon);
        timerThread.start();
    }

    public void schedule(TimerTask task, long delay) {
        if (delay >= 0) {
            sched(task, System.currentTimeMillis() + delay, 0);
            return;
        }
        throw new IllegalArgumentException("Negative delay.");
    }

    public void schedule(TimerTask task, Date time) {
        sched(task, time.getTime(), 0);
    }

    public void schedule(TimerTask task, long delay, long period) {
        if (delay < 0) {
            throw new IllegalArgumentException("Negative delay.");
        } else if (period > 0) {
            sched(task, System.currentTimeMillis() + delay, -period);
        } else {
            throw new IllegalArgumentException("Non-positive period.");
        }
    }

    public void schedule(TimerTask task, Date firstTime, long period) {
        if (period > 0) {
            sched(task, firstTime.getTime(), -period);
            return;
        }
        throw new IllegalArgumentException("Non-positive period.");
    }

    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        if (delay < 0) {
            throw new IllegalArgumentException("Negative delay.");
        } else if (period > 0) {
            sched(task, System.currentTimeMillis() + delay, period);
        } else {
            throw new IllegalArgumentException("Non-positive period.");
        }
    }

    public void scheduleAtFixedRate(TimerTask task, Date firstTime, long period) {
        if (period > 0) {
            sched(task, firstTime.getTime(), period);
            return;
        }
        throw new IllegalArgumentException("Non-positive period.");
    }

    private void sched(TimerTask task, long time, long period) {
        if (time >= 0) {
            if (Math.abs(period) > DurationKt.MAX_MILLIS) {
                period >>= 1;
            }
            long j = period;
            synchronized (this.queue) {
                if (this.thread.newTasksMayBeScheduled) {
                    synchronized (task.lock) {
                        if (task.state == 0) {
                            task.nextExecutionTime = time;
                            task.period = j;
                            task.state = 1;
                        } else {
                            throw new IllegalStateException("Task already scheduled or cancelled");
                        }
                    }
                    this.queue.add(task);
                    if (this.queue.getMin() == task) {
                        this.queue.notify();
                    }
                } else {
                    throw new IllegalStateException("Timer already cancelled.");
                }
            }
            return;
        }
        throw new IllegalArgumentException("Illegal execution time.");
    }

    public void cancel() {
        synchronized (this.queue) {
            this.thread.newTasksMayBeScheduled = false;
            this.queue.clear();
            this.queue.notify();
        }
    }

    public int purge() {
        int i = 0;
        synchronized (this.queue) {
            for (int size = this.queue.size(); size > 0; size--) {
                if (this.queue.get(size).state == 3) {
                    this.queue.quickRemove(size);
                    i++;
                }
            }
            if (i != 0) {
                this.queue.heapify();
            }
        }
        return i;
    }
}
