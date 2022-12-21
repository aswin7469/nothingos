package java.nio;

import libcore.p030io.Memory;

class ByteBufferAsShortBuffer extends ShortBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: bb */
    protected final ByteBuffer f566bb;
    protected final int offset;
    private final ByteOrder order;

    ByteBufferAsShortBuffer(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, ByteOrder byteOrder) {
        super(i, i2, i3, i4);
        ByteBuffer duplicate = byteBuffer.duplicate();
        this.f566bb = duplicate;
        this.isReadOnly = byteBuffer.isReadOnly;
        if (byteBuffer instanceof DirectByteBuffer) {
            this.address = byteBuffer.address + ((long) i5);
        }
        duplicate.order(byteOrder);
        this.order = byteOrder;
        this.offset = i5;
    }

    public ShortBuffer slice() {
        int position = position();
        int limit = limit();
        int i = position <= limit ? limit - position : 0;
        return new ByteBufferAsShortBuffer(this.f566bb, -1, 0, i, i, (position << 1) + this.offset, this.order);
    }

    public ShortBuffer duplicate() {
        return new ByteBufferAsShortBuffer(this.f566bb, markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    public ShortBuffer asReadOnlyBuffer() {
        return new ByteBufferAsShortBuffer(this.f566bb.asReadOnlyBuffer(), markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60883ix(int i) {
        return (i << 1) + this.offset;
    }

    public short get() {
        return get(nextGetIndex());
    }

    public short get(int i) {
        return this.f566bb.getShortUnchecked(mo60883ix(checkIndex(i)));
    }

    public ShortBuffer get(short[] sArr, int i, int i2) {
        checkBounds(i, i2, sArr.length);
        if (i2 <= remaining()) {
            this.f566bb.getUnchecked(mo60883ix(this.position), sArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferUnderflowException();
    }

    public ShortBuffer put(short s) {
        put(nextPutIndex(), s);
        return this;
    }

    public ShortBuffer put(int i, short s) {
        if (!this.isReadOnly) {
            this.f566bb.putShortUnchecked(mo60883ix(checkIndex(i)), s);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ShortBuffer put(short[] sArr, int i, int i2) {
        checkBounds(i, i2, sArr.length);
        if (i2 <= remaining()) {
            this.f566bb.putUnchecked(mo60883ix(this.position), sArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferOverflowException();
    }

    public ShortBuffer compact() {
        if (!this.isReadOnly) {
            int position = position();
            int limit = limit();
            int i = position <= limit ? limit - position : 0;
            ByteBuffer byteBuffer = this.f566bb;
            if (!(byteBuffer instanceof DirectByteBuffer)) {
                System.arraycopy((Object) byteBuffer.array(), mo60883ix(position), (Object) this.f566bb.array(), mo60883ix(0), i << 1);
            } else {
                Memory.memmove(this, mo60883ix(0), this, mo60883ix(position), (long) (i << 1));
            }
            position(i);
            limit(capacity());
            discardMark();
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public boolean isDirect() {
        return this.f566bb.isDirect();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public ByteOrder order() {
        return this.order;
    }
}
