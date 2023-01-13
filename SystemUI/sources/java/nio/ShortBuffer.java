package java.nio;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public abstract class ShortBuffer extends Buffer implements Comparable<ShortBuffer> {

    /* renamed from: hb */
    final short[] f571hb;
    boolean isReadOnly;
    final int offset;

    private static boolean equals(short s, short s2) {
        return s == s2;
    }

    public abstract ShortBuffer asReadOnlyBuffer();

    public abstract ShortBuffer compact();

    public abstract ShortBuffer duplicate();

    public abstract short get();

    public abstract short get(int i);

    public abstract boolean isDirect();

    public abstract ByteOrder order();

    public abstract ShortBuffer put(int i, short s);

    public abstract ShortBuffer put(short s);

    public abstract ShortBuffer slice();

    ShortBuffer(int i, int i2, int i3, int i4, short[] sArr, int i5) {
        super(i, i2, i3, i4, 1);
        this.f571hb = sArr;
        this.offset = i5;
    }

    ShortBuffer(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, (short[]) null, 0);
    }

    public static ShortBuffer allocate(int i) {
        if (i >= 0) {
            return new HeapShortBuffer(i, i);
        }
        throw new IllegalArgumentException();
    }

    public static ShortBuffer wrap(short[] sArr, int i, int i2) {
        try {
            return new HeapShortBuffer(sArr, i, i2);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static ShortBuffer wrap(short[] sArr) {
        return wrap(sArr, 0, sArr.length);
    }

    public ShortBuffer get(short[] sArr, int i, int i2) {
        checkBounds(i, i2, sArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                sArr[i] = get();
                i++;
            }
            return this;
        }
        throw new BufferUnderflowException();
    }

    public ShortBuffer get(short[] sArr) {
        return get(sArr, 0, sArr.length);
    }

    public ShortBuffer put(ShortBuffer shortBuffer) {
        if (shortBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!isReadOnly()) {
            int remaining = shortBuffer.remaining();
            if (remaining <= remaining()) {
                for (int i = 0; i < remaining; i++) {
                    put(shortBuffer.get());
                }
                return this;
            }
            throw new BufferOverflowException();
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public ShortBuffer put(short[] sArr, int i, int i2) {
        checkBounds(i, i2, sArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                put(sArr[i]);
                i++;
            }
            return this;
        }
        throw new BufferOverflowException();
    }

    public final ShortBuffer put(short[] sArr) {
        return put(sArr, 0, sArr.length);
    }

    public final boolean hasArray() {
        return this.f571hb != null && !this.isReadOnly;
    }

    public final short[] array() {
        short[] sArr = this.f571hb;
        if (sArr == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return sArr;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final int arrayOffset() {
        if (this.f571hb == null) {
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
        if (!(obj instanceof ShortBuffer)) {
            return false;
        }
        ShortBuffer shortBuffer = (ShortBuffer) obj;
        if (remaining() != shortBuffer.remaining()) {
            return false;
        }
        int position = position();
        int limit = limit() - 1;
        int limit2 = shortBuffer.limit() - 1;
        while (limit >= position) {
            if (!equals(get(limit), shortBuffer.get(limit2))) {
                return false;
            }
            limit--;
            limit2--;
        }
        return true;
    }

    public int compareTo(ShortBuffer shortBuffer) {
        int position = position() + Math.min(remaining(), shortBuffer.remaining());
        int position2 = position();
        int position3 = shortBuffer.position();
        while (position2 < position) {
            int compare = compare(get(position2), shortBuffer.get(position3));
            if (compare != 0) {
                return compare;
            }
            position2++;
            position3++;
        }
        return remaining() - shortBuffer.remaining();
    }

    private static int compare(short s, short s2) {
        return Short.compare(s, s2);
    }
}
