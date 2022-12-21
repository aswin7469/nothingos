package sun.nio.p033ch;

import java.nio.ByteBuffer;
import sun.misc.Cleaner;

/* renamed from: sun.nio.ch.IOVecWrapper */
class IOVecWrapper {
    private static final int BASE_OFFSET = 0;
    private static final int LEN_OFFSET;
    private static final int SIZE_IOVEC;
    static int addressSize;
    private static final ThreadLocal<IOVecWrapper> cached = new ThreadLocal<>();
    final long address;
    private final ByteBuffer[] buf;
    private final int[] position;
    private final int[] remaining;
    private final ByteBuffer[] shadow;
    private final int size;
    private final AllocatedNativeObject vecArray;

    /* renamed from: sun.nio.ch.IOVecWrapper$Deallocator */
    private static class Deallocator implements Runnable {
        private final AllocatedNativeObject obj;

        Deallocator(AllocatedNativeObject allocatedNativeObject) {
            this.obj = allocatedNativeObject;
        }

        public void run() {
            this.obj.free();
        }
    }

    static {
        int addressSize2 = Util.unsafe().addressSize();
        addressSize = addressSize2;
        LEN_OFFSET = addressSize2;
        SIZE_IOVEC = (short) (addressSize2 * 2);
    }

    private IOVecWrapper(int i) {
        this.size = i;
        this.buf = new ByteBuffer[i];
        this.position = new int[i];
        this.remaining = new int[i];
        this.shadow = new ByteBuffer[i];
        AllocatedNativeObject allocatedNativeObject = new AllocatedNativeObject(i * SIZE_IOVEC, false);
        this.vecArray = allocatedNativeObject;
        this.address = allocatedNativeObject.address();
    }

    static IOVecWrapper get(int i) {
        ThreadLocal<IOVecWrapper> threadLocal = cached;
        IOVecWrapper iOVecWrapper = threadLocal.get();
        if (iOVecWrapper != null && iOVecWrapper.size < i) {
            iOVecWrapper.vecArray.free();
            iOVecWrapper = null;
        }
        if (iOVecWrapper != null) {
            return iOVecWrapper;
        }
        IOVecWrapper iOVecWrapper2 = new IOVecWrapper(i);
        Cleaner.create(iOVecWrapper2, new Deallocator(iOVecWrapper2.vecArray));
        threadLocal.set(iOVecWrapper2);
        return iOVecWrapper2;
    }

    /* access modifiers changed from: package-private */
    public void setBuffer(int i, ByteBuffer byteBuffer, int i2, int i3) {
        this.buf[i] = byteBuffer;
        this.position[i] = i2;
        this.remaining[i] = i3;
    }

    /* access modifiers changed from: package-private */
    public void setShadow(int i, ByteBuffer byteBuffer) {
        this.shadow[i] = byteBuffer;
    }

    /* access modifiers changed from: package-private */
    public ByteBuffer getBuffer(int i) {
        return this.buf[i];
    }

    /* access modifiers changed from: package-private */
    public int getPosition(int i) {
        return this.position[i];
    }

    /* access modifiers changed from: package-private */
    public int getRemaining(int i) {
        return this.remaining[i];
    }

    /* access modifiers changed from: package-private */
    public ByteBuffer getShadow(int i) {
        return this.shadow[i];
    }

    /* access modifiers changed from: package-private */
    public void clearRefs(int i) {
        this.buf[i] = null;
        this.shadow[i] = null;
    }

    /* access modifiers changed from: package-private */
    public void putBase(int i, long j) {
        int i2 = (SIZE_IOVEC * i) + 0;
        if (addressSize == 4) {
            this.vecArray.putInt(i2, (int) j);
        } else {
            this.vecArray.putLong(i2, j);
        }
    }

    /* access modifiers changed from: package-private */
    public void putLen(int i, long j) {
        int i2 = (SIZE_IOVEC * i) + LEN_OFFSET;
        if (addressSize == 4) {
            this.vecArray.putInt(i2, (int) j);
        } else {
            this.vecArray.putLong(i2, j);
        }
    }
}
