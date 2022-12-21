package sun.nio.p033ch;

/* renamed from: sun.nio.ch.NativeThreadSet */
class NativeThreadSet {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private long[] elts;
    private int used = 0;
    private boolean waitingToEmpty;

    NativeThreadSet(int i) {
        this.elts = new long[i];
    }

    /* access modifiers changed from: package-private */
    public int add() {
        long current = NativeThread.current();
        if (current == 0) {
            current = -1;
        }
        synchronized (this) {
            int i = this.used;
            long[] jArr = this.elts;
            int i2 = 0;
            if (i >= jArr.length) {
                int length = jArr.length;
                long[] jArr2 = new long[(length * 2)];
                System.arraycopy((Object) jArr, 0, (Object) jArr2, 0, length);
                this.elts = jArr2;
                i2 = length;
            }
            while (true) {
                long[] jArr3 = this.elts;
                if (i2 >= jArr3.length) {
                    return -1;
                }
                if (jArr3[i2] == 0) {
                    jArr3[i2] = current;
                    this.used++;
                    return i2;
                }
                i2++;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void remove(int i) {
        synchronized (this) {
            this.elts[i] = 0;
            int i2 = this.used - 1;
            this.used = i2;
            if (i2 == 0 && this.waitingToEmpty) {
                notifyAll();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void signalAndWait() {
        boolean z = false;
        while (true) {
            int i = this.used;
            if (i > 0) {
                int length = this.elts.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    long j = this.elts[i2];
                    if (j != 0) {
                        if (j != -1) {
                            NativeThread.signal(j);
                        }
                        i--;
                        if (i == 0) {
                            break;
                        }
                    }
                    i2++;
                }
                this.waitingToEmpty = true;
                try {
                    wait(50);
                    this.waitingToEmpty = false;
                } catch (InterruptedException unused) {
                    this.waitingToEmpty = false;
                    z = true;
                } catch (Throwable th) {
                    this.waitingToEmpty = false;
                    throw th;
                }
            } else if (z) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
