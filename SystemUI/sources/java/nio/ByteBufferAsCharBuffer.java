package java.nio;

import libcore.p030io.Memory;

class ByteBufferAsCharBuffer extends CharBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: bb */
    protected final ByteBuffer f561bb;
    protected final int offset;
    private final ByteOrder order;

    ByteBufferAsCharBuffer(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, ByteOrder byteOrder) {
        super(i, i2, i3, i4);
        ByteBuffer duplicate = byteBuffer.duplicate();
        this.f561bb = duplicate;
        this.isReadOnly = byteBuffer.isReadOnly;
        if (byteBuffer instanceof DirectByteBuffer) {
            this.address = byteBuffer.address + ((long) i5);
        }
        duplicate.order(byteOrder);
        this.order = byteOrder;
        this.offset = i5;
    }

    public CharBuffer slice() {
        int position = position();
        int limit = limit();
        int i = position <= limit ? limit - position : 0;
        return new ByteBufferAsCharBuffer(this.f561bb, -1, 0, i, i, (position << 1) + this.offset, this.order);
    }

    public CharBuffer duplicate() {
        return new ByteBufferAsCharBuffer(this.f561bb, markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    public CharBuffer asReadOnlyBuffer() {
        return new ByteBufferAsCharBuffer(this.f561bb.asReadOnlyBuffer(), markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60822ix(int i) {
        return (i << 1) + this.offset;
    }

    public char get() {
        return get(nextGetIndex());
    }

    public char get(int i) {
        return this.f561bb.getCharUnchecked(mo60822ix(checkIndex(i)));
    }

    public CharBuffer get(char[] cArr, int i, int i2) {
        checkBounds(i, i2, cArr.length);
        if (i2 <= remaining()) {
            this.f561bb.getUnchecked(mo60822ix(this.position), cArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferUnderflowException();
    }

    /* access modifiers changed from: package-private */
    public char getUnchecked(int i) {
        return this.f561bb.getCharUnchecked(mo60822ix(i));
    }

    public CharBuffer put(char c) {
        put(nextPutIndex(), c);
        return this;
    }

    public CharBuffer put(int i, char c) {
        if (!this.isReadOnly) {
            this.f561bb.putCharUnchecked(mo60822ix(checkIndex(i)), c);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public CharBuffer put(char[] cArr, int i, int i2) {
        checkBounds(i, i2, cArr.length);
        if (i2 <= remaining()) {
            this.f561bb.putUnchecked(mo60822ix(this.position), cArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferOverflowException();
    }

    public CharBuffer compact() {
        if (!this.isReadOnly) {
            int position = position();
            int limit = limit();
            int i = position <= limit ? limit - position : 0;
            ByteBuffer byteBuffer = this.f561bb;
            if (!(byteBuffer instanceof DirectByteBuffer)) {
                System.arraycopy((Object) byteBuffer.array(), mo60822ix(position), (Object) this.f561bb.array(), mo60822ix(0), i << 1);
            } else {
                Memory.memmove(this, mo60822ix(0), this, mo60822ix(position), (long) (i << 1));
            }
            position(i);
            limit(capacity());
            discardMark();
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public boolean isDirect() {
        return this.f561bb.isDirect();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public String toString(int i, int i2) {
        if (i2 > limit() || i > i2) {
            throw new IndexOutOfBoundsException();
        }
        try {
            char[] cArr = new char[(i2 - i)];
            CharBuffer wrap = CharBuffer.wrap(cArr);
            CharBuffer duplicate = duplicate();
            duplicate.position(i);
            duplicate.limit(i2);
            wrap.put(duplicate);
            return new String(cArr);
        } catch (StringIndexOutOfBoundsException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public CharBuffer subSequence(int i, int i2) {
        int position = position();
        int limit = limit();
        if (position > limit) {
            position = limit;
        }
        int i3 = limit - position;
        if (i >= 0 && i2 <= i3 && i <= i2) {
            return new ByteBufferAsCharBuffer(this.f561bb, -1, position + i, position + i2, capacity(), this.offset, this.order);
        }
        throw new IndexOutOfBoundsException();
    }

    public ByteOrder order() {
        return this.order;
    }
}
