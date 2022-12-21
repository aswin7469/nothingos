package sun.nio.p033ch;

/* renamed from: sun.nio.ch.PollArrayWrapper */
public class PollArrayWrapper extends AbstractPollArrayWrapper {
    int interruptFD;

    private static native void interrupt(int i);

    private native int poll0(long j, int i, long j2);

    /* access modifiers changed from: package-private */
    public void release(int i) {
    }

    PollArrayWrapper(int i) {
        this.pollArray = new AllocatedNativeObject((i + 1) * 8, false);
        this.pollArrayAddress = this.pollArray.address();
        this.totalChannels = 1;
    }

    /* access modifiers changed from: package-private */
    public void initInterrupt(int i, int i2) {
        this.interruptFD = i2;
        putDescriptor(0, i);
        putEventOps(0, Net.POLLIN);
        putReventOps(0, 0);
    }

    /* access modifiers changed from: package-private */
    public void free() {
        this.pollArray.free();
    }

    /* access modifiers changed from: package-private */
    public void addEntry(SelChImpl selChImpl) {
        putDescriptor(this.totalChannels, IOUtil.fdVal(selChImpl.getFD()));
        putEventOps(this.totalChannels, 0);
        putReventOps(this.totalChannels, 0);
        this.totalChannels++;
    }

    static void replaceEntry(PollArrayWrapper pollArrayWrapper, int i, PollArrayWrapper pollArrayWrapper2, int i2) {
        pollArrayWrapper2.putDescriptor(i2, pollArrayWrapper.getDescriptor(i));
        pollArrayWrapper2.putEventOps(i2, pollArrayWrapper.getEventOps(i));
        pollArrayWrapper2.putReventOps(i2, pollArrayWrapper.getReventOps(i));
    }

    /* access modifiers changed from: package-private */
    public void grow(int i) {
        PollArrayWrapper pollArrayWrapper = new PollArrayWrapper(i);
        for (int i2 = 0; i2 < this.totalChannels; i2++) {
            replaceEntry(this, i2, pollArrayWrapper, i2);
        }
        this.pollArray.free();
        this.pollArray = pollArrayWrapper.pollArray;
        this.pollArrayAddress = this.pollArray.address();
    }

    /* access modifiers changed from: package-private */
    public int poll(int i, int i2, long j) {
        return poll0(this.pollArrayAddress + ((long) (i2 * 8)), i, j);
    }

    public void interrupt() {
        interrupt(this.interruptFD);
    }
}
