package java.nio;

class HeapCharBuffer extends CharBuffer {
    public boolean isDirect() {
        return false;
    }

    HeapCharBuffer(int i, int i2) {
        this(i, i2, false);
    }

    HeapCharBuffer(int i, int i2, boolean z) {
        super(-1, 0, i2, i, new char[i], 0);
        this.isReadOnly = z;
    }

    HeapCharBuffer(char[] cArr, int i, int i2) {
        this(cArr, i, i2, false);
    }

    HeapCharBuffer(char[] cArr, int i, int i2, boolean z) {
        super(-1, i, i + i2, cArr.length, cArr, 0);
        this.isReadOnly = z;
    }

    protected HeapCharBuffer(char[] cArr, int i, int i2, int i3, int i4, int i5) {
        this(cArr, i, i2, i3, i4, i5, false);
    }

    protected HeapCharBuffer(char[] cArr, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2, i3, i4, cArr, i5);
        this.isReadOnly = z;
    }

    public CharBuffer slice() {
        return new HeapCharBuffer(this.f567hb, -1, 0, remaining(), remaining(), this.offset + position(), this.isReadOnly);
    }

    public CharBuffer duplicate() {
        return new HeapCharBuffer(this.f567hb, markValue(), position(), limit(), capacity(), this.offset, this.isReadOnly);
    }

    public CharBuffer asReadOnlyBuffer() {
        return new HeapCharBuffer(this.f567hb, markValue(), position(), limit(), capacity(), this.offset, true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60909ix(int i) {
        return i + this.offset;
    }

    public char get() {
        return this.f567hb[mo60909ix(nextGetIndex())];
    }

    public char get(int i) {
        return this.f567hb[mo60909ix(checkIndex(i))];
    }

    /* access modifiers changed from: package-private */
    public char getUnchecked(int i) {
        return this.f567hb[mo60909ix(i)];
    }

    public CharBuffer get(char[] cArr, int i, int i2) {
        checkBounds(i, i2, cArr.length);
        if (i2 <= remaining()) {
            System.arraycopy((Object) this.f567hb, mo60909ix(position()), (Object) cArr, i, i2);
            position(position() + i2);
            return this;
        }
        throw new BufferUnderflowException();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public CharBuffer put(char c) {
        if (!this.isReadOnly) {
            this.f567hb[mo60909ix(nextPutIndex())] = c;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public CharBuffer put(int i, char c) {
        if (!this.isReadOnly) {
            this.f567hb[mo60909ix(checkIndex(i))] = c;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public CharBuffer put(char[] cArr, int i, int i2) {
        if (!this.isReadOnly) {
            checkBounds(i, i2, cArr.length);
            if (i2 <= remaining()) {
                System.arraycopy((Object) cArr, i, (Object) this.f567hb, mo60909ix(position()), i2);
                position(position() + i2);
                return this;
            }
            throw new BufferOverflowException();
        }
        throw new ReadOnlyBufferException();
    }

    public CharBuffer put(CharBuffer charBuffer) {
        if (charBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!this.isReadOnly) {
            if (charBuffer instanceof HeapCharBuffer) {
                HeapCharBuffer heapCharBuffer = (HeapCharBuffer) charBuffer;
                int remaining = heapCharBuffer.remaining();
                if (remaining <= remaining()) {
                    System.arraycopy((Object) heapCharBuffer.f567hb, heapCharBuffer.mo60909ix(heapCharBuffer.position()), (Object) this.f567hb, mo60909ix(position()), remaining);
                    heapCharBuffer.position(heapCharBuffer.position() + remaining);
                    position(position() + remaining);
                } else {
                    throw new BufferOverflowException();
                }
            } else if (charBuffer.isDirect()) {
                int remaining2 = charBuffer.remaining();
                if (remaining2 <= remaining()) {
                    charBuffer.get(this.f567hb, mo60909ix(position()), remaining2);
                    position(position() + remaining2);
                } else {
                    throw new BufferOverflowException();
                }
            } else {
                super.put(charBuffer);
            }
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public CharBuffer compact() {
        if (!this.isReadOnly) {
            System.arraycopy((Object) this.f567hb, mo60909ix(position()), (Object) this.f567hb, mo60909ix(0), remaining());
            position(remaining());
            limit(capacity());
            discardMark();
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public String toString(int i, int i2) {
        try {
            return new String(this.f567hb, this.offset + i, i2 - i);
        } catch (StringIndexOutOfBoundsException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public CharBuffer subSequence(int i, int i2) {
        if (i < 0 || i2 > length() || i > i2) {
            throw new IndexOutOfBoundsException();
        }
        int position = position();
        return new HeapCharBuffer(this.f567hb, -1, position + i, position + i2, capacity(), this.offset, this.isReadOnly);
    }

    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
