package sun.nio.p035fs;

import sun.misc.Cleaner;
import sun.misc.Unsafe;

/* renamed from: sun.nio.fs.NativeBuffer */
class NativeBuffer {
    /* access modifiers changed from: private */
    public static final Unsafe unsafe = Unsafe.getUnsafe();
    private final long address;
    private final Cleaner cleaner;
    private Object owner;
    private final int size;

    /* renamed from: sun.nio.fs.NativeBuffer$Deallocator */
    private static class Deallocator implements Runnable {
        private final long address;

        Deallocator(long j) {
            this.address = j;
        }

        public void run() {
            NativeBuffer.unsafe.freeMemory(this.address);
        }
    }

    NativeBuffer(int i) {
        long allocateMemory = unsafe.allocateMemory((long) i);
        this.address = allocateMemory;
        this.size = i;
        this.cleaner = Cleaner.create(this, new Deallocator(allocateMemory));
    }

    /* access modifiers changed from: package-private */
    public void release() {
        NativeBuffers.releaseNativeBuffer(this);
    }

    /* access modifiers changed from: package-private */
    public long address() {
        return this.address;
    }

    /* access modifiers changed from: package-private */
    public int size() {
        return this.size;
    }

    /* access modifiers changed from: package-private */
    public Cleaner cleaner() {
        return this.cleaner;
    }

    /* access modifiers changed from: package-private */
    public void setOwner(Object obj) {
        this.owner = obj;
    }

    /* access modifiers changed from: package-private */
    public Object owner() {
        return this.owner;
    }
}
