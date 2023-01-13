package java.nio;

class HeapLongBuffer extends LongBuffer {
    public boolean isDirect() {
        return false;
    }

    HeapLongBuffer(int i, int i2) {
        this(i, i2, false);
    }

    HeapLongBuffer(int i, int i2, boolean z) {
        super(-1, 0, i2, i, new long[i], 0);
        this.isReadOnly = z;
    }

    HeapLongBuffer(long[] jArr, int i, int i2) {
        this(jArr, i, i2, false);
    }

    HeapLongBuffer(long[] jArr, int i, int i2, boolean z) {
        super(-1, i, i + i2, jArr.length, jArr, 0);
        this.isReadOnly = z;
    }

    protected HeapLongBuffer(long[] jArr, int i, int i2, int i3, int i4, int i5) {
        this(jArr, i, i2, i3, i4, i5, false);
    }

    protected HeapLongBuffer(long[] jArr, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2, i3, i4, jArr, i5);
        this.isReadOnly = z;
    }

    public LongBuffer slice() {
        return new HeapLongBuffer(this.f569hb, -1, 0, remaining(), remaining(), this.offset + position(), this.isReadOnly);
    }

    public LongBuffer duplicate() {
        return new HeapLongBuffer(this.f569hb, markValue(), position(), limit(), capacity(), this.offset, this.isReadOnly);
    }

    public LongBuffer asReadOnlyBuffer() {
        return new HeapLongBuffer(this.f569hb, markValue(), position(), limit(), capacity(), this.offset, true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60970ix(int i) {
        return i + this.offset;
    }

    public long get() {
        return this.f569hb[mo60970ix(nextGetIndex())];
    }

    public long get(int i) {
        return this.f569hb[mo60970ix(checkIndex(i))];
    }

    public LongBuffer get(long[] jArr, int i, int i2) {
        checkBounds(i, i2, jArr.length);
        if (i2 <= remaining()) {
            System.arraycopy((Object) this.f569hb, mo60970ix(position()), (Object) jArr, i, i2);
            position(position() + i2);
            return this;
        }
        throw new BufferUnderflowException();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public LongBuffer put(long j) {
        if (!this.isReadOnly) {
            this.f569hb[mo60970ix(nextPutIndex())] = j;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public LongBuffer put(int i, long j) {
        if (!this.isReadOnly) {
            this.f569hb[mo60970ix(checkIndex(i))] = j;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public LongBuffer put(long[] jArr, int i, int i2) {
        if (!this.isReadOnly) {
            checkBounds(i, i2, jArr.length);
            if (i2 <= remaining()) {
                System.arraycopy((Object) jArr, i, (Object) this.f569hb, mo60970ix(position()), i2);
                position(position() + i2);
                return this;
            }
            throw new BufferOverflowException();
        }
        throw new ReadOnlyBufferException();
    }

    public LongBuffer put(LongBuffer longBuffer) {
        if (longBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!this.isReadOnly) {
            if (longBuffer instanceof HeapLongBuffer) {
                HeapLongBuffer heapLongBuffer = (HeapLongBuffer) longBuffer;
                int remaining = heapLongBuffer.remaining();
                if (remaining <= remaining()) {
                    System.arraycopy((Object) heapLongBuffer.f569hb, heapLongBuffer.mo60970ix(heapLongBuffer.position()), (Object) this.f569hb, mo60970ix(position()), remaining);
                    heapLongBuffer.position(heapLongBuffer.position() + remaining);
                    position(position() + remaining);
                } else {
                    throw new BufferOverflowException();
                }
            } else if (longBuffer.isDirect()) {
                int remaining2 = longBuffer.remaining();
                if (remaining2 <= remaining()) {
                    longBuffer.get(this.f569hb, mo60970ix(position()), remaining2);
                    position(position() + remaining2);
                } else {
                    throw new BufferOverflowException();
                }
            } else {
                super.put(longBuffer);
            }
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public LongBuffer compact() {
        if (!this.isReadOnly) {
            System.arraycopy((Object) this.f569hb, mo60970ix(position()), (Object) this.f569hb, mo60970ix(0), remaining());
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
