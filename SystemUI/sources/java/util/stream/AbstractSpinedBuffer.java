package java.util.stream;

abstract class AbstractSpinedBuffer {
    public static final int MAX_CHUNK_POWER = 30;
    public static final int MIN_CHUNK_POWER = 4;
    public static final int MIN_CHUNK_SIZE = 16;
    public static final int MIN_SPINE_SIZE = 8;
    protected int elementIndex;
    protected final int initialChunkPower;
    protected long[] priorElementCount;
    protected int spineIndex;

    public abstract void clear();

    protected AbstractSpinedBuffer() {
        this.initialChunkPower = 4;
    }

    protected AbstractSpinedBuffer(int i) {
        if (i >= 0) {
            this.initialChunkPower = Math.max(4, 32 - Integer.numberOfLeadingZeros(i - 1));
            return;
        }
        throw new IllegalArgumentException("Illegal Capacity: " + i);
    }

    public boolean isEmpty() {
        return this.spineIndex == 0 && this.elementIndex == 0;
    }

    public long count() {
        int i = this.spineIndex;
        if (i == 0) {
            return (long) this.elementIndex;
        }
        return this.priorElementCount[i] + ((long) this.elementIndex);
    }

    /* access modifiers changed from: protected */
    public int chunkSize(int i) {
        int i2;
        if (i == 0 || i == 1) {
            i2 = this.initialChunkPower;
        } else {
            i2 = Math.min((this.initialChunkPower + i) - 1, 30);
        }
        return 1 << i2;
    }
}
