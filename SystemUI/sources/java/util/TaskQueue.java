package java.util;

/* compiled from: Timer */
class TaskQueue {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private TimerTask[] queue = new TimerTask[128];
    private int size = 0;

    TaskQueue() {
    }

    /* access modifiers changed from: package-private */
    public int size() {
        return this.size;
    }

    /* access modifiers changed from: package-private */
    public void add(TimerTask timerTask) {
        int i = this.size + 1;
        TimerTask[] timerTaskArr = this.queue;
        if (i == timerTaskArr.length) {
            this.queue = (TimerTask[]) Arrays.copyOf((T[]) timerTaskArr, timerTaskArr.length * 2);
        }
        TimerTask[] timerTaskArr2 = this.queue;
        int i2 = this.size + 1;
        this.size = i2;
        timerTaskArr2[i2] = timerTask;
        fixUp(i2);
    }

    /* access modifiers changed from: package-private */
    public TimerTask getMin() {
        return this.queue[1];
    }

    /* access modifiers changed from: package-private */
    public TimerTask get(int i) {
        return this.queue[i];
    }

    /* access modifiers changed from: package-private */
    public void removeMin() {
        TimerTask[] timerTaskArr = this.queue;
        int i = this.size;
        timerTaskArr[1] = timerTaskArr[i];
        this.size = i - 1;
        timerTaskArr[i] = null;
        fixDown(1);
    }

    /* access modifiers changed from: package-private */
    public void quickRemove(int i) {
        TimerTask[] timerTaskArr = this.queue;
        int i2 = this.size;
        timerTaskArr[i] = timerTaskArr[i2];
        this.size = i2 - 1;
        timerTaskArr[i2] = null;
    }

    /* access modifiers changed from: package-private */
    public void rescheduleMin(long j) {
        this.queue[1].nextExecutionTime = j;
        fixDown(1);
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        for (int i = 1; i <= this.size; i++) {
            this.queue[i] = null;
        }
        this.size = 0;
    }

    private void fixUp(int i) {
        while (i > 1) {
            int i2 = i >> 1;
            if (this.queue[i2].nextExecutionTime > this.queue[i].nextExecutionTime) {
                TimerTask[] timerTaskArr = this.queue;
                TimerTask timerTask = timerTaskArr[i2];
                timerTaskArr[i2] = timerTaskArr[i];
                timerTaskArr[i] = timerTask;
                i = i2;
            } else {
                return;
            }
        }
    }

    private void fixDown(int i) {
        while (true) {
            int i2 = i << 1;
            int i3 = this.size;
            if (i2 <= i3 && i2 > 0) {
                if (i2 < i3) {
                    int i4 = i2 + 1;
                    if (this.queue[i2].nextExecutionTime > this.queue[i4].nextExecutionTime) {
                        i2 = i4;
                    }
                }
                if (this.queue[i].nextExecutionTime > this.queue[i2].nextExecutionTime) {
                    TimerTask[] timerTaskArr = this.queue;
                    TimerTask timerTask = timerTaskArr[i2];
                    timerTaskArr[i2] = timerTaskArr[i];
                    timerTaskArr[i] = timerTask;
                    i = i2;
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void heapify() {
        for (int i = this.size / 2; i >= 1; i--) {
            fixDown(i);
        }
    }
}
