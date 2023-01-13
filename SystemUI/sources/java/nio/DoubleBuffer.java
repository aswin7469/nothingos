package java.nio;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public abstract class DoubleBuffer extends Buffer implements Comparable<DoubleBuffer> {

    /* renamed from: hb */
    final double[] f566hb;
    boolean isReadOnly;
    final int offset;

    public abstract DoubleBuffer asReadOnlyBuffer();

    public abstract DoubleBuffer compact();

    public abstract DoubleBuffer duplicate();

    public abstract double get();

    public abstract double get(int i);

    public abstract boolean isDirect();

    public abstract ByteOrder order();

    public abstract DoubleBuffer put(double d);

    public abstract DoubleBuffer put(int i, double d);

    public abstract DoubleBuffer slice();

    DoubleBuffer(int i, int i2, int i3, int i4, double[] dArr, int i5) {
        super(i, i2, i3, i4, 3);
        this.f566hb = dArr;
        this.offset = i5;
    }

    DoubleBuffer(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, (double[]) null, 0);
    }

    public static DoubleBuffer allocate(int i) {
        if (i >= 0) {
            return new HeapDoubleBuffer(i, i);
        }
        throw new IllegalArgumentException();
    }

    public static DoubleBuffer wrap(double[] dArr, int i, int i2) {
        try {
            return new HeapDoubleBuffer(dArr, i, i2);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static DoubleBuffer wrap(double[] dArr) {
        return wrap(dArr, 0, dArr.length);
    }

    public DoubleBuffer get(double[] dArr, int i, int i2) {
        checkBounds(i, i2, dArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                dArr[i] = get();
                i++;
            }
            return this;
        }
        throw new BufferUnderflowException();
    }

    public DoubleBuffer get(double[] dArr) {
        return get(dArr, 0, dArr.length);
    }

    public DoubleBuffer put(DoubleBuffer doubleBuffer) {
        if (doubleBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!isReadOnly()) {
            int remaining = doubleBuffer.remaining();
            if (remaining <= remaining()) {
                for (int i = 0; i < remaining; i++) {
                    put(doubleBuffer.get());
                }
                return this;
            }
            throw new BufferOverflowException();
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public DoubleBuffer put(double[] dArr, int i, int i2) {
        checkBounds(i, i2, dArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                put(dArr[i]);
                i++;
            }
            return this;
        }
        throw new BufferOverflowException();
    }

    public final DoubleBuffer put(double[] dArr) {
        return put(dArr, 0, dArr.length);
    }

    public final boolean hasArray() {
        return this.f566hb != null && !this.isReadOnly;
    }

    public final double[] array() {
        double[] dArr = this.f566hb;
        if (dArr == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return dArr;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final int arrayOffset() {
        if (this.f566hb == null) {
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
        if (!(obj instanceof DoubleBuffer)) {
            return false;
        }
        DoubleBuffer doubleBuffer = (DoubleBuffer) obj;
        if (remaining() != doubleBuffer.remaining()) {
            return false;
        }
        int position = position();
        int limit = limit() - 1;
        int limit2 = doubleBuffer.limit() - 1;
        while (limit >= position) {
            if (!equals(get(limit), doubleBuffer.get(limit2))) {
                return false;
            }
            limit--;
            limit2--;
        }
        return true;
    }

    private static boolean equals(double d, double d2) {
        return d == d2 || (Double.isNaN(d) && Double.isNaN(d2));
    }

    public int compareTo(DoubleBuffer doubleBuffer) {
        int position = position() + Math.min(remaining(), doubleBuffer.remaining());
        int position2 = position();
        int position3 = doubleBuffer.position();
        while (position2 < position) {
            int compare = compare(get(position2), doubleBuffer.get(position3));
            if (compare != 0) {
                return compare;
            }
            position2++;
            position3++;
        }
        return remaining() - doubleBuffer.remaining();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        if (java.lang.Double.isNaN(r6) != false) goto L_0x0010;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int compare(double r4, double r6) {
        /*
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            r1 = -1
            if (r0 >= 0) goto L_0x0006
            goto L_0x001f
        L_0x0006:
            int r0 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            r2 = 1
            if (r0 <= 0) goto L_0x000d
        L_0x000b:
            r1 = r2
            goto L_0x001f
        L_0x000d:
            r3 = 0
            if (r0 != 0) goto L_0x0012
        L_0x0010:
            r1 = r3
            goto L_0x001f
        L_0x0012:
            boolean r4 = java.lang.Double.isNaN(r4)
            if (r4 == 0) goto L_0x001f
            boolean r4 = java.lang.Double.isNaN(r6)
            if (r4 == 0) goto L_0x000b
            goto L_0x0010
        L_0x001f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.DoubleBuffer.compare(double, double):int");
    }
}
