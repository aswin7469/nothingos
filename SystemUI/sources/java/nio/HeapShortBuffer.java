package java.nio;

class HeapShortBuffer extends ShortBuffer {
    public boolean isDirect() {
        return false;
    }

    HeapShortBuffer(int i, int i2) {
        this(i, i2, false);
    }

    HeapShortBuffer(int i, int i2, boolean z) {
        super(-1, 0, i2, i, new short[i], 0);
        this.isReadOnly = z;
    }

    HeapShortBuffer(short[] sArr, int i, int i2) {
        this(sArr, i, i2, false);
    }

    HeapShortBuffer(short[] sArr, int i, int i2, boolean z) {
        super(-1, i, i + i2, sArr.length, sArr, 0);
        this.isReadOnly = z;
    }

    protected HeapShortBuffer(short[] sArr, int i, int i2, int i3, int i4, int i5) {
        this(sArr, i, i2, i3, i4, i5, false);
    }

    protected HeapShortBuffer(short[] sArr, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2, i3, i4, sArr, i5);
        this.isReadOnly = z;
    }

    public ShortBuffer slice() {
        return new HeapShortBuffer(this.f573hb, -1, 0, remaining(), remaining(), this.offset + position(), this.isReadOnly);
    }

    public ShortBuffer duplicate() {
        return new HeapShortBuffer(this.f573hb, markValue(), position(), limit(), capacity(), this.offset, this.isReadOnly);
    }

    public ShortBuffer asReadOnlyBuffer() {
        return new HeapShortBuffer(this.f573hb, markValue(), position(), limit(), capacity(), this.offset, true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60916ix(int i) {
        return i + this.offset;
    }

    public short get() {
        return this.f573hb[mo60916ix(nextGetIndex())];
    }

    public short get(int i) {
        return this.f573hb[mo60916ix(checkIndex(i))];
    }

    public ShortBuffer get(short[] sArr, int i, int i2) {
        checkBounds(i, i2, sArr.length);
        if (i2 <= remaining()) {
            System.arraycopy((Object) this.f573hb, mo60916ix(position()), (Object) sArr, i, i2);
            position(position() + i2);
            return this;
        }
        throw new BufferUnderflowException();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public ShortBuffer put(short s) {
        if (!this.isReadOnly) {
            this.f573hb[mo60916ix(nextPutIndex())] = s;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ShortBuffer put(int i, short s) {
        if (!this.isReadOnly) {
            this.f573hb[mo60916ix(checkIndex(i))] = s;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ShortBuffer put(short[] sArr, int i, int i2) {
        if (!this.isReadOnly) {
            checkBounds(i, i2, sArr.length);
            if (i2 <= remaining()) {
                System.arraycopy((Object) sArr, i, (Object) this.f573hb, mo60916ix(position()), i2);
                position(position() + i2);
                return this;
            }
            throw new BufferOverflowException();
        }
        throw new ReadOnlyBufferException();
    }

    public ShortBuffer put(ShortBuffer shortBuffer) {
        if (shortBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!this.isReadOnly) {
            if (shortBuffer instanceof HeapShortBuffer) {
                HeapShortBuffer heapShortBuffer = (HeapShortBuffer) shortBuffer;
                int remaining = heapShortBuffer.remaining();
                if (remaining <= remaining()) {
                    System.arraycopy((Object) heapShortBuffer.f573hb, heapShortBuffer.mo60916ix(heapShortBuffer.position()), (Object) this.f573hb, mo60916ix(position()), remaining);
                    heapShortBuffer.position(heapShortBuffer.position() + remaining);
                    position(position() + remaining);
                } else {
                    throw new BufferOverflowException();
                }
            } else if (shortBuffer.isDirect()) {
                int remaining2 = shortBuffer.remaining();
                if (remaining2 <= remaining()) {
                    shortBuffer.get(this.f573hb, mo60916ix(position()), remaining2);
                    position(position() + remaining2);
                } else {
                    throw new BufferOverflowException();
                }
            } else {
                super.put(shortBuffer);
            }
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public ShortBuffer compact() {
        if (!this.isReadOnly) {
            System.arraycopy((Object) this.f573hb, mo60916ix(position()), (Object) this.f573hb, mo60916ix(0), remaining());
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
