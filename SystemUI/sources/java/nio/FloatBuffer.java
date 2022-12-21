package java.nio;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public abstract class FloatBuffer extends Buffer implements Comparable<FloatBuffer> {

    /* renamed from: hb */
    final float[] f569hb;
    boolean isReadOnly;
    final int offset;

    public abstract FloatBuffer asReadOnlyBuffer();

    public abstract FloatBuffer compact();

    public abstract FloatBuffer duplicate();

    public abstract float get();

    public abstract float get(int i);

    public abstract boolean isDirect();

    public abstract ByteOrder order();

    public abstract FloatBuffer put(float f);

    public abstract FloatBuffer put(int i, float f);

    public abstract FloatBuffer slice();

    FloatBuffer(int i, int i2, int i3, int i4, float[] fArr, int i5) {
        super(i, i2, i3, i4, 2);
        this.f569hb = fArr;
        this.offset = i5;
    }

    FloatBuffer(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, (float[]) null, 0);
    }

    public static FloatBuffer allocate(int i) {
        if (i >= 0) {
            return new HeapFloatBuffer(i, i);
        }
        throw new IllegalArgumentException();
    }

    public static FloatBuffer wrap(float[] fArr, int i, int i2) {
        try {
            return new HeapFloatBuffer(fArr, i, i2);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static FloatBuffer wrap(float[] fArr) {
        return wrap(fArr, 0, fArr.length);
    }

    public FloatBuffer get(float[] fArr, int i, int i2) {
        checkBounds(i, i2, fArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                fArr[i] = get();
                i++;
            }
            return this;
        }
        throw new BufferUnderflowException();
    }

    public FloatBuffer get(float[] fArr) {
        return get(fArr, 0, fArr.length);
    }

    public FloatBuffer put(FloatBuffer floatBuffer) {
        if (floatBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!isReadOnly()) {
            int remaining = floatBuffer.remaining();
            if (remaining <= remaining()) {
                for (int i = 0; i < remaining; i++) {
                    put(floatBuffer.get());
                }
                return this;
            }
            throw new BufferOverflowException();
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public FloatBuffer put(float[] fArr, int i, int i2) {
        checkBounds(i, i2, fArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                put(fArr[i]);
                i++;
            }
            return this;
        }
        throw new BufferOverflowException();
    }

    public final FloatBuffer put(float[] fArr) {
        return put(fArr, 0, fArr.length);
    }

    public final boolean hasArray() {
        return this.f569hb != null && !this.isReadOnly;
    }

    public final float[] array() {
        float[] fArr = this.f569hb;
        if (fArr == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return fArr;
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
        if (!(obj instanceof FloatBuffer)) {
            return false;
        }
        FloatBuffer floatBuffer = (FloatBuffer) obj;
        if (remaining() != floatBuffer.remaining()) {
            return false;
        }
        int position = position();
        int limit = limit() - 1;
        int limit2 = floatBuffer.limit() - 1;
        while (limit >= position) {
            if (!equals(get(limit), floatBuffer.get(limit2))) {
                return false;
            }
            limit--;
            limit2--;
        }
        return true;
    }

    private static boolean equals(float f, float f2) {
        return f == f2 || (Float.isNaN(f) && Float.isNaN(f2));
    }

    public int compareTo(FloatBuffer floatBuffer) {
        int position = position() + Math.min(remaining(), floatBuffer.remaining());
        int position2 = position();
        int position3 = floatBuffer.position();
        while (position2 < position) {
            int compare = compare(get(position2), floatBuffer.get(position3));
            if (compare != 0) {
                return compare;
            }
            position2++;
            position3++;
        }
        return remaining() - floatBuffer.remaining();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001c, code lost:
        if (java.lang.Float.isNaN(r5) != false) goto L_0x0010;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int compare(float r4, float r5) {
        /*
            int r0 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            r1 = -1
            if (r0 >= 0) goto L_0x0006
            goto L_0x001f
        L_0x0006:
            int r0 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
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
            boolean r4 = java.lang.Float.isNaN(r4)
            if (r4 == 0) goto L_0x001f
            boolean r4 = java.lang.Float.isNaN(r5)
            if (r4 == 0) goto L_0x000b
            goto L_0x0010
        L_0x001f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.FloatBuffer.compare(float, float):int");
    }
}
