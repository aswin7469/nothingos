package sun.nio.p033ch;

/* renamed from: sun.nio.ch.AbstractPollArrayWrapper */
public abstract class AbstractPollArrayWrapper {
    static final short EVENT_OFFSET = 4;
    static final short FD_OFFSET = 0;
    static final short REVENT_OFFSET = 6;
    static final short SIZE_POLLFD = 8;
    protected AllocatedNativeObject pollArray;
    protected long pollArrayAddress;
    protected int totalChannels = 0;

    /* access modifiers changed from: package-private */
    public int getEventOps(int i) {
        return this.pollArray.getShort((i * 8) + 4);
    }

    /* access modifiers changed from: package-private */
    public int getReventOps(int i) {
        return this.pollArray.getShort((i * 8) + 6);
    }

    /* access modifiers changed from: package-private */
    public int getDescriptor(int i) {
        return this.pollArray.getInt((i * 8) + 0);
    }

    /* access modifiers changed from: package-private */
    public void putEventOps(int i, int i2) {
        this.pollArray.putShort((i * 8) + 4, (short) i2);
    }

    /* access modifiers changed from: package-private */
    public void putReventOps(int i, int i2) {
        this.pollArray.putShort((i * 8) + 6, (short) i2);
    }

    /* access modifiers changed from: package-private */
    public void putDescriptor(int i, int i2) {
        this.pollArray.putInt((i * 8) + 0, i2);
    }
}
