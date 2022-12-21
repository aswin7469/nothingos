package java.nio;

class HeapDoubleBuffer extends DoubleBuffer {
    public boolean isDirect() {
        return false;
    }

    HeapDoubleBuffer(int i, int i2) {
        this(i, i2, false);
    }

    HeapDoubleBuffer(double[] dArr, int i, int i2) {
        this(dArr, i, i2, false);
    }

    protected HeapDoubleBuffer(double[] dArr, int i, int i2, int i3, int i4, int i5) {
        this(dArr, i, i2, i3, i4, i5, false);
    }

    HeapDoubleBuffer(int i, int i2, boolean z) {
        super(-1, 0, i2, i, new double[i], 0);
        this.isReadOnly = z;
    }

    HeapDoubleBuffer(double[] dArr, int i, int i2, boolean z) {
        super(-1, i, i + i2, dArr.length, dArr, 0);
        this.isReadOnly = z;
    }

    protected HeapDoubleBuffer(double[] dArr, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2, i3, i4, dArr, i5);
        this.isReadOnly = z;
    }

    public DoubleBuffer slice() {
        return new HeapDoubleBuffer(this.f568hb, -1, 0, remaining(), remaining(), this.offset + position(), this.isReadOnly);
    }

    public DoubleBuffer duplicate() {
        return new HeapDoubleBuffer(this.f568hb, markValue(), position(), limit(), capacity(), this.offset, this.isReadOnly);
    }

    public DoubleBuffer asReadOnlyBuffer() {
        return new HeapDoubleBuffer(this.f568hb, markValue(), position(), limit(), capacity(), this.offset, true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60910ix(int i) {
        return i + this.offset;
    }

    public double get() {
        return this.f568hb[mo60910ix(nextGetIndex())];
    }

    public double get(int i) {
        return this.f568hb[mo60910ix(checkIndex(i))];
    }

    public DoubleBuffer get(double[] dArr, int i, int i2) {
        checkBounds(i, i2, dArr.length);
        if (i2 <= remaining()) {
            System.arraycopy((Object) this.f568hb, mo60910ix(position()), (Object) dArr, i, i2);
            position(position() + i2);
            return this;
        }
        throw new BufferUnderflowException();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public DoubleBuffer put(double d) {
        if (!this.isReadOnly) {
            this.f568hb[mo60910ix(nextPutIndex())] = d;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public DoubleBuffer put(int i, double d) {
        if (!this.isReadOnly) {
            this.f568hb[mo60910ix(checkIndex(i))] = d;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public DoubleBuffer put(double[] dArr, int i, int i2) {
        if (!this.isReadOnly) {
            checkBounds(i, i2, dArr.length);
            if (i2 <= remaining()) {
                System.arraycopy((Object) dArr, i, (Object) this.f568hb, mo60910ix(position()), i2);
                position(position() + i2);
                return this;
            }
            throw new BufferOverflowException();
        }
        throw new ReadOnlyBufferException();
    }

    public DoubleBuffer put(DoubleBuffer doubleBuffer) {
        if (doubleBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!this.isReadOnly) {
            if (doubleBuffer instanceof HeapDoubleBuffer) {
                HeapDoubleBuffer heapDoubleBuffer = (HeapDoubleBuffer) doubleBuffer;
                int remaining = heapDoubleBuffer.remaining();
                if (remaining <= remaining()) {
                    System.arraycopy((Object) heapDoubleBuffer.f568hb, heapDoubleBuffer.mo60910ix(heapDoubleBuffer.position()), (Object) this.f568hb, mo60910ix(position()), remaining);
                    heapDoubleBuffer.position(heapDoubleBuffer.position() + remaining);
                    position(position() + remaining);
                } else {
                    throw new BufferOverflowException();
                }
            } else if (doubleBuffer.isDirect()) {
                int remaining2 = doubleBuffer.remaining();
                if (remaining2 <= remaining()) {
                    doubleBuffer.get(this.f568hb, mo60910ix(position()), remaining2);
                    position(position() + remaining2);
                } else {
                    throw new BufferOverflowException();
                }
            } else {
                super.put(doubleBuffer);
            }
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public DoubleBuffer compact() {
        if (!this.isReadOnly) {
            System.arraycopy((Object) this.f568hb, mo60910ix(position()), (Object) this.f568hb, mo60910ix(0), remaining());
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
