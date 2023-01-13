package java.nio;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public abstract class IntBuffer extends Buffer implements Comparable<IntBuffer> {

    /* renamed from: hb */
    final int[] f568hb;
    boolean isReadOnly;
    final int offset;

    private static boolean equals(int i, int i2) {
        return i == i2;
    }

    public abstract IntBuffer asReadOnlyBuffer();

    public abstract IntBuffer compact();

    public abstract IntBuffer duplicate();

    public abstract int get();

    public abstract int get(int i);

    public abstract boolean isDirect();

    public abstract ByteOrder order();

    public abstract IntBuffer put(int i);

    public abstract IntBuffer put(int i, int i2);

    public abstract IntBuffer slice();

    IntBuffer(int i, int i2, int i3, int i4, int[] iArr, int i5) {
        super(i, i2, i3, i4, 2);
        this.f568hb = iArr;
        this.offset = i5;
    }

    IntBuffer(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, (int[]) null, 0);
    }

    public static IntBuffer allocate(int i) {
        if (i >= 0) {
            return new HeapIntBuffer(i, i);
        }
        throw new IllegalArgumentException();
    }

    public static IntBuffer wrap(int[] iArr, int i, int i2) {
        try {
            return new HeapIntBuffer(iArr, i, i2);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static IntBuffer wrap(int[] iArr) {
        return wrap(iArr, 0, iArr.length);
    }

    public IntBuffer get(int[] iArr, int i, int i2) {
        checkBounds(i, i2, iArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                iArr[i] = get();
                i++;
            }
            return this;
        }
        throw new BufferUnderflowException();
    }

    public IntBuffer get(int[] iArr) {
        return get(iArr, 0, iArr.length);
    }

    public IntBuffer put(IntBuffer intBuffer) {
        if (intBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!isReadOnly()) {
            int remaining = intBuffer.remaining();
            if (remaining <= remaining()) {
                for (int i = 0; i < remaining; i++) {
                    put(intBuffer.get());
                }
                return this;
            }
            throw new BufferOverflowException();
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public IntBuffer put(int[] iArr, int i, int i2) {
        checkBounds(i, i2, iArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                put(iArr[i]);
                i++;
            }
            return this;
        }
        throw new BufferOverflowException();
    }

    public final IntBuffer put(int[] iArr) {
        return put(iArr, 0, iArr.length);
    }

    public final boolean hasArray() {
        return this.f568hb != null && !this.isReadOnly;
    }

    public final int[] array() {
        int[] iArr = this.f568hb;
        if (iArr == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return iArr;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final int arrayOffset() {
        if (this.f568hb == null) {
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
        if (!(obj instanceof IntBuffer)) {
            return false;
        }
        IntBuffer intBuffer = (IntBuffer) obj;
        if (remaining() != intBuffer.remaining()) {
            return false;
        }
        int position = position();
        int limit = limit() - 1;
        int limit2 = intBuffer.limit() - 1;
        while (limit >= position) {
            if (!equals(get(limit), intBuffer.get(limit2))) {
                return false;
            }
            limit--;
            limit2--;
        }
        return true;
    }

    public int compareTo(IntBuffer intBuffer) {
        int position = position() + Math.min(remaining(), intBuffer.remaining());
        int position2 = position();
        int position3 = intBuffer.position();
        while (position2 < position) {
            int compare = compare(get(position2), intBuffer.get(position3));
            if (compare != 0) {
                return compare;
            }
            position2++;
            position3++;
        }
        return remaining() - intBuffer.remaining();
    }

    private static int compare(int i, int i2) {
        return Integer.compare(i, i2);
    }
}
