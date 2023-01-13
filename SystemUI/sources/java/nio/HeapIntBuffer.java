package java.nio;

class HeapIntBuffer extends IntBuffer {
    public boolean isDirect() {
        return false;
    }

    HeapIntBuffer(int i, int i2) {
        this(i, i2, false);
    }

    HeapIntBuffer(int i, int i2, boolean z) {
        super(-1, 0, i2, i, new int[i], 0);
        this.isReadOnly = z;
    }

    HeapIntBuffer(int[] iArr, int i, int i2) {
        this(iArr, i, i2, false);
    }

    HeapIntBuffer(int[] iArr, int i, int i2, boolean z) {
        super(-1, i, i + i2, iArr.length, iArr, 0);
        this.isReadOnly = z;
    }

    protected HeapIntBuffer(int[] iArr, int i, int i2, int i3, int i4, int i5) {
        this(iArr, i, i2, i3, i4, i5, false);
    }

    protected HeapIntBuffer(int[] iArr, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2, i3, i4, iArr, i5);
        this.isReadOnly = z;
    }

    public IntBuffer slice() {
        return new HeapIntBuffer(this.f568hb, -1, 0, remaining(), remaining(), this.offset + position(), this.isReadOnly);
    }

    public IntBuffer duplicate() {
        return new HeapIntBuffer(this.f568hb, markValue(), position(), limit(), capacity(), this.offset, this.isReadOnly);
    }

    public IntBuffer asReadOnlyBuffer() {
        return new HeapIntBuffer(this.f568hb, markValue(), position(), limit(), capacity(), this.offset, true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60968ix(int i) {
        return i + this.offset;
    }

    public int get() {
        return this.f568hb[mo60968ix(nextGetIndex())];
    }

    public int get(int i) {
        return this.f568hb[mo60968ix(checkIndex(i))];
    }

    public IntBuffer get(int[] iArr, int i, int i2) {
        checkBounds(i, i2, iArr.length);
        if (i2 <= remaining()) {
            System.arraycopy((Object) this.f568hb, mo60968ix(position()), (Object) iArr, i, i2);
            position(position() + i2);
            return this;
        }
        throw new BufferUnderflowException();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public IntBuffer put(int i) {
        if (!this.isReadOnly) {
            this.f568hb[mo60968ix(nextPutIndex())] = i;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public IntBuffer put(int i, int i2) {
        if (!this.isReadOnly) {
            this.f568hb[mo60968ix(checkIndex(i))] = i2;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public IntBuffer put(int[] iArr, int i, int i2) {
        if (!this.isReadOnly) {
            checkBounds(i, i2, iArr.length);
            if (i2 <= remaining()) {
                System.arraycopy((Object) iArr, i, (Object) this.f568hb, mo60968ix(position()), i2);
                position(position() + i2);
                return this;
            }
            throw new BufferOverflowException();
        }
        throw new ReadOnlyBufferException();
    }

    public IntBuffer put(IntBuffer intBuffer) {
        if (intBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!this.isReadOnly) {
            if (intBuffer instanceof HeapIntBuffer) {
                HeapIntBuffer heapIntBuffer = (HeapIntBuffer) intBuffer;
                int remaining = heapIntBuffer.remaining();
                if (remaining <= remaining()) {
                    System.arraycopy((Object) heapIntBuffer.f568hb, heapIntBuffer.mo60968ix(heapIntBuffer.position()), (Object) this.f568hb, mo60968ix(position()), remaining);
                    heapIntBuffer.position(heapIntBuffer.position() + remaining);
                    position(position() + remaining);
                } else {
                    throw new BufferOverflowException();
                }
            } else if (intBuffer.isDirect()) {
                int remaining2 = intBuffer.remaining();
                if (remaining2 <= remaining()) {
                    intBuffer.get(this.f568hb, mo60968ix(position()), remaining2);
                    position(position() + remaining2);
                } else {
                    throw new BufferOverflowException();
                }
            } else {
                super.put(intBuffer);
            }
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public IntBuffer compact() {
        if (!this.isReadOnly) {
            System.arraycopy((Object) this.f568hb, mo60968ix(position()), (Object) this.f568hb, mo60968ix(0), remaining());
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
