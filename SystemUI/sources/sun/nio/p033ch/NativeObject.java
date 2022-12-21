package sun.nio.p033ch;

import java.nio.ByteOrder;
import sun.misc.Unsafe;

/* renamed from: sun.nio.ch.NativeObject */
class NativeObject {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static ByteOrder byteOrder = null;
    private static int pageSize = -1;
    protected static final Unsafe unsafe = Unsafe.getUnsafe();
    private final long address;
    protected long allocationAddress;

    NativeObject(long j) {
        this.allocationAddress = j;
        this.address = j;
    }

    NativeObject(long j, long j2) {
        this.allocationAddress = j;
        this.address = j + j2;
    }

    protected NativeObject(int i, boolean z) {
        if (!z) {
            long allocateMemory = unsafe.allocateMemory((long) i);
            this.allocationAddress = allocateMemory;
            this.address = allocateMemory;
            return;
        }
        int pageSize2 = pageSize();
        long allocateMemory2 = unsafe.allocateMemory((long) (i + pageSize2));
        this.allocationAddress = allocateMemory2;
        this.address = (((long) pageSize2) + allocateMemory2) - (((long) (pageSize2 - 1)) & allocateMemory2);
    }

    /* access modifiers changed from: package-private */
    public long address() {
        return this.address;
    }

    /* access modifiers changed from: package-private */
    public long allocationAddress() {
        return this.allocationAddress;
    }

    /* access modifiers changed from: package-private */
    public NativeObject subObject(int i) {
        return new NativeObject(((long) i) + this.address);
    }

    /* access modifiers changed from: package-private */
    public NativeObject getObject(int i) {
        long j;
        int addressSize = addressSize();
        if (addressSize == 4) {
            j = (long) (unsafe.getInt(((long) i) + this.address) & -1);
        } else if (addressSize == 8) {
            j = unsafe.getLong(((long) i) + this.address);
        } else {
            throw new InternalError("Address size not supported");
        }
        return new NativeObject(j);
    }

    /* access modifiers changed from: package-private */
    public void putObject(int i, NativeObject nativeObject) {
        int addressSize = addressSize();
        if (addressSize == 4) {
            putInt(i, (int) (nativeObject.address & -1));
        } else if (addressSize == 8) {
            putLong(i, nativeObject.address);
        } else {
            throw new InternalError("Address size not supported");
        }
    }

    /* access modifiers changed from: package-private */
    public final byte getByte(int i) {
        return unsafe.getByte(((long) i) + this.address);
    }

    /* access modifiers changed from: package-private */
    public final void putByte(int i, byte b) {
        unsafe.putByte(((long) i) + this.address, b);
    }

    /* access modifiers changed from: package-private */
    public final short getShort(int i) {
        return unsafe.getShort(((long) i) + this.address);
    }

    /* access modifiers changed from: package-private */
    public final void putShort(int i, short s) {
        unsafe.putShort(((long) i) + this.address, s);
    }

    /* access modifiers changed from: package-private */
    public final char getChar(int i) {
        return unsafe.getChar(((long) i) + this.address);
    }

    /* access modifiers changed from: package-private */
    public final void putChar(int i, char c) {
        unsafe.putChar(((long) i) + this.address, c);
    }

    /* access modifiers changed from: package-private */
    public final int getInt(int i) {
        return unsafe.getInt(((long) i) + this.address);
    }

    /* access modifiers changed from: package-private */
    public final void putInt(int i, int i2) {
        unsafe.putInt(((long) i) + this.address, i2);
    }

    /* access modifiers changed from: package-private */
    public final long getLong(int i) {
        return unsafe.getLong(((long) i) + this.address);
    }

    /* access modifiers changed from: package-private */
    public final void putLong(int i, long j) {
        unsafe.putLong(((long) i) + this.address, j);
    }

    /* access modifiers changed from: package-private */
    public final float getFloat(int i) {
        return unsafe.getFloat(((long) i) + this.address);
    }

    /* access modifiers changed from: package-private */
    public final void putFloat(int i, float f) {
        unsafe.putFloat(((long) i) + this.address, f);
    }

    /* access modifiers changed from: package-private */
    public final double getDouble(int i) {
        return unsafe.getDouble(((long) i) + this.address);
    }

    /* access modifiers changed from: package-private */
    public final void putDouble(int i, double d) {
        unsafe.putDouble(((long) i) + this.address, d);
    }

    static int addressSize() {
        return unsafe.addressSize();
    }

    static ByteOrder byteOrder() {
        ByteOrder byteOrder2 = byteOrder;
        if (byteOrder2 != null) {
            return byteOrder2;
        }
        Unsafe unsafe2 = unsafe;
        long allocateMemory = unsafe2.allocateMemory(8);
        try {
            unsafe2.putLong(allocateMemory, 72623859790382856L);
            byte b = unsafe2.getByte(allocateMemory);
            if (b == 1) {
                byteOrder = ByteOrder.BIG_ENDIAN;
            } else if (b == 8) {
                byteOrder = ByteOrder.LITTLE_ENDIAN;
            }
            unsafe2.freeMemory(allocateMemory);
            return byteOrder;
        } catch (Throwable th) {
            unsafe.freeMemory(allocateMemory);
            throw th;
        }
    }

    static int pageSize() {
        if (pageSize == -1) {
            pageSize = unsafe.pageSize();
        }
        return pageSize;
    }
}
