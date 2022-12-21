package java.util;

public abstract class TimerTask implements Runnable {
    static final int CANCELLED = 3;
    static final int EXECUTED = 2;
    static final int SCHEDULED = 1;
    static final int VIRGIN = 0;
    final Object lock = new Object();
    long nextExecutionTime;
    long period = 0;
    int state = 0;

    public abstract void run();

    protected TimerTask() {
    }

    public boolean cancel() {
        boolean z;
        synchronized (this.lock) {
            z = true;
            if (this.state != 1) {
                z = false;
            }
            this.state = 3;
        }
        return z;
    }

    public long scheduledExecutionTime() {
        long j;
        synchronized (this.lock) {
            long j2 = this.period;
            if (j2 < 0) {
                j = this.nextExecutionTime + j2;
            } else {
                j = this.nextExecutionTime - j2;
            }
        }
        return j;
    }
}
