package java.nio;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.DirectByteBuffer;
import jdk.internal.misc.Unsafe;

public abstract class ByteBuffer extends Buffer implements Comparable<ByteBuffer> {
    boolean bigEndian;

    /* renamed from: hb */
    final byte[] f558hb;
    boolean isReadOnly;
    boolean nativeByteOrder;
    final int offset;

    private static boolean equals(byte b, byte b2) {
        return b == b2;
    }

    /* access modifiers changed from: package-private */
    public abstract byte _get(int i);

    /* access modifiers changed from: package-private */
    public abstract void _put(int i, byte b);

    public abstract CharBuffer asCharBuffer();

    public abstract DoubleBuffer asDoubleBuffer();

    public abstract FloatBuffer asFloatBuffer();

    public abstract IntBuffer asIntBuffer();

    public abstract LongBuffer asLongBuffer();

    public abstract ByteBuffer asReadOnlyBuffer();

    public abstract ShortBuffer asShortBuffer();

    public abstract ByteBuffer compact();

    public abstract ByteBuffer duplicate();

    public abstract byte get();

    public abstract byte get(int i);

    public abstract char getChar();

    public abstract char getChar(int i);

    /* access modifiers changed from: package-private */
    public abstract char getCharUnchecked(int i);

    public abstract double getDouble();

    public abstract double getDouble(int i);

    /* access modifiers changed from: package-private */
    public abstract double getDoubleUnchecked(int i);

    public abstract float getFloat();

    public abstract float getFloat(int i);

    /* access modifiers changed from: package-private */
    public abstract float getFloatUnchecked(int i);

    public abstract int getInt();

    public abstract int getInt(int i);

    /* access modifiers changed from: package-private */
    public abstract int getIntUnchecked(int i);

    public abstract long getLong();

    public abstract long getLong(int i);

    /* access modifiers changed from: package-private */
    public abstract long getLongUnchecked(int i);

    public abstract short getShort();

    public abstract short getShort(int i);

    /* access modifiers changed from: package-private */
    public abstract short getShortUnchecked(int i);

    /* access modifiers changed from: package-private */
    public abstract void getUnchecked(int i, char[] cArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void getUnchecked(int i, double[] dArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void getUnchecked(int i, float[] fArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void getUnchecked(int i, int[] iArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void getUnchecked(int i, long[] jArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void getUnchecked(int i, short[] sArr, int i2, int i3);

    public boolean isAccessible() {
        return true;
    }

    public abstract boolean isDirect();

    public abstract ByteBuffer put(byte b);

    public abstract ByteBuffer put(int i, byte b);

    public abstract ByteBuffer putChar(char c);

    public abstract ByteBuffer putChar(int i, char c);

    /* access modifiers changed from: package-private */
    public abstract void putCharUnchecked(int i, char c);

    public abstract ByteBuffer putDouble(double d);

    public abstract ByteBuffer putDouble(int i, double d);

    /* access modifiers changed from: package-private */
    public abstract void putDoubleUnchecked(int i, double d);

    public abstract ByteBuffer putFloat(float f);

    public abstract ByteBuffer putFloat(int i, float f);

    /* access modifiers changed from: package-private */
    public abstract void putFloatUnchecked(int i, float f);

    public abstract ByteBuffer putInt(int i);

    public abstract ByteBuffer putInt(int i, int i2);

    /* access modifiers changed from: package-private */
    public abstract void putIntUnchecked(int i, int i2);

    public abstract ByteBuffer putLong(int i, long j);

    public abstract ByteBuffer putLong(long j);

    /* access modifiers changed from: package-private */
    public abstract void putLongUnchecked(int i, long j);

    public abstract ByteBuffer putShort(int i, short s);

    public abstract ByteBuffer putShort(short s);

    /* access modifiers changed from: package-private */
    public abstract void putShortUnchecked(int i, short s);

    /* access modifiers changed from: package-private */
    public abstract void putUnchecked(int i, char[] cArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void putUnchecked(int i, double[] dArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void putUnchecked(int i, float[] fArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void putUnchecked(int i, int[] iArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void putUnchecked(int i, long[] jArr, int i2, int i3);

    /* access modifiers changed from: package-private */
    public abstract void putUnchecked(int i, short[] sArr, int i2, int i3);

    public abstract ByteBuffer slice();

    /* access modifiers changed from: package-private */
    public abstract ByteBuffer slice(int i, int i2);

    ByteBuffer(int i, int i2, int i3, int i4, byte[] bArr, int i5) {
        super(i, i2, i3, i4, 0);
        boolean z = true;
        this.bigEndian = true;
        this.nativeByteOrder = Bits.byteOrder() != ByteOrder.BIG_ENDIAN ? false : z;
        this.f558hb = bArr;
        this.offset = i5;
    }

    ByteBuffer(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, (byte[]) null, 0);
    }

    public static ByteBuffer allocateDirect(int i) {
        return new DirectByteBuffer(i, new DirectByteBuffer.MemoryRef(i));
    }

    public static ByteBuffer allocate(int i) {
        if (i >= 0) {
            return new HeapByteBuffer(i, i);
        }
        throw new IllegalArgumentException();
    }

    public static ByteBuffer wrap(byte[] bArr, int i, int i2) {
        try {
            return new HeapByteBuffer(bArr, i, i2);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static ByteBuffer wrap(byte[] bArr) {
        return wrap(bArr, 0, bArr.length);
    }

    public ByteBuffer get(byte[] bArr, int i, int i2) {
        checkBounds(i, i2, bArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                bArr[i] = get();
                i++;
            }
            return this;
        }
        throw new BufferUnderflowException();
    }

    public ByteBuffer get(byte[] bArr) {
        return get(bArr, 0, bArr.length);
    }

    /* JADX WARNING: type inference failed for: r1v10, types: [byte[]] */
    /* JADX WARNING: type inference failed for: r1v12, types: [byte[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.nio.ByteBuffer put(java.nio.ByteBuffer r9) {
        /*
            r8 = this;
            if (r9 == r8) goto L_0x0080
            boolean r0 = r8.isReadOnly()
            if (r0 != 0) goto L_0x007a
            int r0 = r9.remaining()
            int r1 = r8.remaining()
            if (r0 > r1) goto L_0x0074
            byte[] r1 = r8.f558hb
            if (r1 == 0) goto L_0x002e
            byte[] r1 = r9.f558hb
            if (r1 == 0) goto L_0x002e
            int r2 = r9.position()
            int r3 = r9.offset
            int r2 = r2 + r3
            byte[] r3 = r8.f558hb
            int r4 = r8.position()
            int r5 = r8.offset
            int r4 = r4 + r5
            java.lang.System.arraycopy((java.lang.Object) r1, (int) r2, (java.lang.Object) r3, (int) r4, (int) r0)
            goto L_0x0064
        L_0x002e:
            boolean r1 = r9.isDirect()
            if (r1 == 0) goto L_0x0036
            r4 = r9
            goto L_0x0039
        L_0x0036:
            byte[] r1 = r9.f558hb
            r4 = r1
        L_0x0039:
            int r1 = r9.position()
            boolean r2 = r9.isDirect()
            if (r2 != 0) goto L_0x0046
            int r2 = r9.offset
            int r1 = r1 + r2
        L_0x0046:
            r5 = r1
            boolean r1 = r8.isDirect()
            if (r1 == 0) goto L_0x004f
            r2 = r8
            goto L_0x0052
        L_0x004f:
            byte[] r1 = r8.f558hb
            r2 = r1
        L_0x0052:
            int r1 = r8.position()
            boolean r3 = r8.isDirect()
            if (r3 != 0) goto L_0x005f
            int r3 = r8.offset
            int r1 = r1 + r3
        L_0x005f:
            r3 = r1
            long r6 = (long) r0
            libcore.p030io.Memory.memmove(r2, r3, r4, r5, r6)
        L_0x0064:
            int r1 = r9.limit()
            r9.position((int) r1)
            int r9 = r8.position()
            int r9 = r9 + r0
            r8.position((int) r9)
            return r8
        L_0x0074:
            java.nio.BufferOverflowException r8 = new java.nio.BufferOverflowException
            r8.<init>()
            throw r8
        L_0x007a:
            java.nio.ReadOnlyBufferException r8 = new java.nio.ReadOnlyBufferException
            r8.<init>()
            throw r8
        L_0x0080:
            java.lang.IllegalArgumentException r8 = new java.lang.IllegalArgumentException
            r8.<init>()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.ByteBuffer.put(java.nio.ByteBuffer):java.nio.ByteBuffer");
    }

    public ByteBuffer put(byte[] bArr, int i, int i2) {
        checkBounds(i, i2, bArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                put(bArr[i]);
                i++;
            }
            return this;
        }
        throw new BufferOverflowException();
    }

    public final ByteBuffer put(byte[] bArr) {
        return put(bArr, 0, bArr.length);
    }

    public final boolean hasArray() {
        return this.f558hb != null && !this.isReadOnly;
    }

    public final byte[] array() {
        byte[] bArr = this.f558hb;
        if (bArr == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return bArr;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final int arrayOffset() {
        if (this.f558hb == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return this.offset;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public Buffer position(int i) {
        return super.position(i);
    }

    public Buffer limit(int i) {
        return super.limit(i);
    }

    public Buffer mark() {
        return super.mark();
    }

    public Buffer reset() {
        return super.reset();
    }

    public Buffer clear() {
        return super.clear();
    }

    public Buffer flip() {
        return super.flip();
    }

    public Buffer rewind() {
        return super.rewind();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getClass().getName());
        stringBuffer.append("[pos=");
        stringBuffer.append(position());
        stringBuffer.append(" lim=");
        stringBuffer.append(limit());
        stringBuffer.append(" cap=");
        stringBuffer.append(capacity());
        stringBuffer.append(NavigationBarInflaterView.SIZE_MOD_END);
        return stringBuffer.toString();
    }

    public int hashCode() {
        int position = position();
        int i = 1;
        for (int limit = limit() - 1; limit >= position; limit--) {
            i = (i * 31) + get(limit);
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ByteBuffer)) {
            return false;
        }
        ByteBuffer byteBuffer = (ByteBuffer) obj;
        if (remaining() != byteBuffer.remaining()) {
            return false;
        }
        int position = position();
        int limit = limit() - 1;
        int limit2 = byteBuffer.limit() - 1;
        while (limit >= position) {
            if (!equals(get(limit), byteBuffer.get(limit2))) {
                return false;
            }
            limit--;
            limit2--;
        }
        return true;
    }

    public int compareTo(ByteBuffer byteBuffer) {
        int position = position() + Math.min(remaining(), byteBuffer.remaining());
        int position2 = position();
        int position3 = byteBuffer.position();
        while (position2 < position) {
            int compare = compare(get(position2), byteBuffer.get(position3));
            if (compare != 0) {
                return compare;
            }
            position2++;
            position3++;
        }
        return remaining() - byteBuffer.remaining();
    }

    private static int compare(byte b, byte b2) {
        return Byte.compare(b, b2);
    }

    public final ByteOrder order() {
        return this.bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
    }

    public final ByteBuffer order(ByteOrder byteOrder) {
        boolean z = true;
        boolean z2 = byteOrder == ByteOrder.BIG_ENDIAN;
        this.bigEndian = z2;
        if (z2 != (Bits.byteOrder() == ByteOrder.BIG_ENDIAN)) {
            z = false;
        }
        this.nativeByteOrder = z;
        return this;
    }

    public final int alignmentOffset(int i, int i2) {
        if (i >= 0) {
            if (i2 >= 1) {
                int i3 = i2 - 1;
                if ((i2 & i3) == 0) {
                    if (i2 <= 8 || isDirect()) {
                        return (int) (((long) i3) & ((isDirect() ? this.address : (long) (Unsafe.getUnsafe().arrayBaseOffset(byte[].class) + this.offset)) + ((long) i)));
                    }
                    throw new UnsupportedOperationException("Unit size unsupported for non-direct buffers: " + i2);
                }
            }
            throw new IllegalArgumentException("Unit size not a power of two: " + i2);
        }
        throw new IllegalArgumentException("Index less than zero: " + i);
    }

    public final ByteBuffer alignedSlice(int i) {
        int position = position();
        int limit = limit();
        int alignmentOffset = alignmentOffset(position, i);
        int alignmentOffset2 = alignmentOffset(limit, i);
        int i2 = alignmentOffset > 0 ? (i - alignmentOffset) + position : position;
        int i3 = limit - alignmentOffset2;
        if (i2 > limit || i3 < position) {
            i3 = position;
        } else {
            position = i2;
        }
        return slice(position, i3);
    }

    public void setAccessible(boolean z) {
        throw new UnsupportedOperationException();
    }
}
