package java.nio;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public abstract class LongBuffer extends Buffer implements Comparable<LongBuffer> {

    /* renamed from: hb */
    final long[] f569hb;
    boolean isReadOnly;
    final int offset;

    private static boolean equals(long j, long j2) {
        return j == j2;
    }

    public abstract LongBuffer asReadOnlyBuffer();

    public abstract LongBuffer compact();

    public abstract LongBuffer duplicate();

    public abstract long get();

    public abstract long get(int i);

    public abstract boolean isDirect();

    public abstract ByteOrder order();

    public abstract LongBuffer put(int i, long j);

    public abstract LongBuffer put(long j);

    public abstract LongBuffer slice();

    LongBuffer(int i, int i2, int i3, int i4, long[] jArr, int i5) {
        super(i, i2, i3, i4, 3);
        this.f569hb = jArr;
        this.offset = i5;
    }

    LongBuffer(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, (long[]) null, 0);
    }

    public static LongBuffer allocate(int i) {
        if (i >= 0) {
            return new HeapLongBuffer(i, i);
        }
        throw new IllegalArgumentException();
    }

    public static LongBuffer wrap(long[] jArr, int i, int i2) {
        try {
            return new HeapLongBuffer(jArr, i, i2);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static LongBuffer wrap(long[] jArr) {
        return wrap(jArr, 0, jArr.length);
    }

    public LongBuffer get(long[] jArr, int i, int i2) {
        checkBounds(i, i2, jArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                jArr[i] = get();
                i++;
            }
            return this;
        }
        throw new BufferUnderflowException();
    }

    public LongBuffer get(long[] jArr) {
        return get(jArr, 0, jArr.length);
    }

    public LongBuffer put(LongBuffer longBuffer) {
        if (longBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!isReadOnly()) {
            int remaining = longBuffer.remaining();
            if (remaining <= remaining()) {
                for (int i = 0; i < remaining; i++) {
                    put(longBuffer.get());
                }
                return this;
            }
            throw new BufferOverflowException();
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public LongBuffer put(long[] jArr, int i, int i2) {
        checkBounds(i, i2, jArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                put(jArr[i]);
                i++;
            }
            return this;
        }
        throw new BufferOverflowException();
    }

    public final LongBuffer put(long[] jArr) {
        return put(jArr, 0, jArr.length);
    }

    public final boolean hasArray() {
        return this.f569hb != null && !this.isReadOnly;
    }

    public final long[] array() {
        long[] jArr = this.f569hb;
        if (jArr == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return jArr;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final int arrayOffset() {
        if (this.f569hb == null) {
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
            i = (i * 31) + ((int) get(limit));
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof LongBuffer)) {
            return false;
        }
        LongBuffer longBuffer = (LongBuffer) obj;
        if (remaining() != longBuffer.remaining()) {
            return false;
        }
        int position = position();
        int limit = limit() - 1;
        int limit2 = longBuffer.limit() - 1;
        while (limit >= position) {
            if (!equals(get(limit), longBuffer.get(limit2))) {
                return false;
            }
            limit--;
            limit2--;
        }
        return true;
    }

    public int compareTo(LongBuffer longBuffer) {
        int position = position() + Math.min(remaining(), longBuffer.remaining());
        int position2 = position();
        int position3 = longBuffer.position();
        while (position2 < position) {
            int compare = compare(get(position2), longBuffer.get(position3));
            if (compare != 0) {
                return compare;
            }
            position2++;
            position3++;
        }
        return remaining() - longBuffer.remaining();
    }

    private static int compare(long j, long j2) {
        return Long.compare(j, j2);
    }
}
