package java.nio;

class HeapFloatBuffer extends FloatBuffer {
    public boolean isDirect() {
        return false;
    }

    HeapFloatBuffer(int i, int i2) {
        this(i, i2, false);
    }

    HeapFloatBuffer(int i, int i2, boolean z) {
        super(-1, 0, i2, i, new float[i], 0);
        this.isReadOnly = z;
    }

    HeapFloatBuffer(float[] fArr, int i, int i2) {
        this(fArr, i, i2, false);
    }

    HeapFloatBuffer(float[] fArr, int i, int i2, boolean z) {
        super(-1, i, i + i2, fArr.length, fArr, 0);
        this.isReadOnly = z;
    }

    protected HeapFloatBuffer(float[] fArr, int i, int i2, int i3, int i4, int i5) {
        this(fArr, i, i2, i3, i4, i5, false);
    }

    protected HeapFloatBuffer(float[] fArr, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2, i3, i4, fArr, i5);
        this.isReadOnly = z;
    }

    public FloatBuffer slice() {
        return new HeapFloatBuffer(this.f569hb, -1, 0, remaining(), remaining(), this.offset + position(), this.isReadOnly);
    }

    public FloatBuffer duplicate() {
        return new HeapFloatBuffer(this.f569hb, markValue(), position(), limit(), capacity(), this.offset, this.isReadOnly);
    }

    public FloatBuffer asReadOnlyBuffer() {
        return new HeapFloatBuffer(this.f569hb, markValue(), position(), limit(), capacity(), this.offset, true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60911ix(int i) {
        return i + this.offset;
    }

    public float get() {
        return this.f569hb[mo60911ix(nextGetIndex())];
    }

    public float get(int i) {
        return this.f569hb[mo60911ix(checkIndex(i))];
    }

    public FloatBuffer get(float[] fArr, int i, int i2) {
        checkBounds(i, i2, fArr.length);
        if (i2 <= remaining()) {
            System.arraycopy((Object) this.f569hb, mo60911ix(position()), (Object) fArr, i, i2);
            position(position() + i2);
            return this;
        }
        throw new BufferUnderflowException();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public FloatBuffer put(float f) {
        if (!this.isReadOnly) {
            this.f569hb[mo60911ix(nextPutIndex())] = f;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public FloatBuffer put(int i, float f) {
        if (!this.isReadOnly) {
            this.f569hb[mo60911ix(checkIndex(i))] = f;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public FloatBuffer put(float[] fArr, int i, int i2) {
        if (!this.isReadOnly) {
            checkBounds(i, i2, fArr.length);
            if (i2 <= remaining()) {
                System.arraycopy((Object) fArr, i, (Object) this.f569hb, mo60911ix(position()), i2);
                position(position() + i2);
                return this;
            }
            throw new BufferOverflowException();
        }
        throw new ReadOnlyBufferException();
    }

    public FloatBuffer put(FloatBuffer floatBuffer) {
        if (floatBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!this.isReadOnly) {
            if (floatBuffer instanceof HeapFloatBuffer) {
                HeapFloatBuffer heapFloatBuffer = (HeapFloatBuffer) floatBuffer;
                int remaining = heapFloatBuffer.remaining();
                if (remaining <= remaining()) {
                    System.arraycopy((Object) heapFloatBuffer.f569hb, heapFloatBuffer.mo60911ix(heapFloatBuffer.position()), (Object) this.f569hb, mo60911ix(position()), remaining);
                    heapFloatBuffer.position(heapFloatBuffer.position() + remaining);
                    position(position() + remaining);
                } else {
                    throw new BufferOverflowException();
                }
            } else if (floatBuffer.isDirect()) {
                int remaining2 = floatBuffer.remaining();
                if (remaining2 <= remaining()) {
                    floatBuffer.get(this.f569hb, mo60911ix(position()), remaining2);
                    position(position() + remaining2);
                } else {
                    throw new BufferOverflowException();
                }
            } else {
                super.put(floatBuffer);
            }
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public FloatBuffer compact() {
        if (!this.isReadOnly) {
            System.arraycopy((Object) this.f569hb, mo60911ix(position()), (Object) this.f569hb, mo60911ix(0), remaining());
            position(remaining());
            limit(capacity());
            discardMark();
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
