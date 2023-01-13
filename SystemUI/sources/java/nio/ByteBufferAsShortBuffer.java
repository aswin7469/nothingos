package java.nio;

import libcore.p030io.Memory;

class ByteBufferAsShortBuffer extends ShortBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: bb */
    protected final ByteBuffer f564bb;
    protected final int offset;
    private final ByteOrder order;

    ByteBufferAsShortBuffer(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, ByteOrder byteOrder) {
        super(i, i2, i3, i4);
        ByteBuffer duplicate = byteBuffer.duplicate();
        this.f564bb = duplicate;
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
        return new ByteBufferAsShortBuffer(this.f564bb, -1, 0, i, i, (position << 1) + this.offset, this.order);
    }

    public ShortBuffer duplicate() {
        return new ByteBufferAsShortBuffer(this.f564bb, markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    public ShortBuffer asReadOnlyBuffer() {
        return new ByteBufferAsShortBuffer(this.f564bb.asReadOnlyBuffer(), markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60939ix(int i) {
        return (i << 1) + this.offset;
    }

    public short get() {
        return get(nextGetIndex());
    }

    public short get(int i) {
        return this.f564bb.getShortUnchecked(mo60939ix(checkIndex(i)));
    }

    public ShortBuffer get(short[] sArr, int i, int i2) {
        checkBounds(i, i2, sArr.length);
        if (i2 <= remaining()) {
            this.f564bb.getUnchecked(mo60939ix(this.position), sArr, i, i2);
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
            this.f564bb.putShortUnchecked(mo60939ix(checkIndex(i)), s);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ShortBuffer put(short[] sArr, int i, int i2) {
        checkBounds(i, i2, sArr.length);
        if (i2 <= remaining()) {
            this.f564bb.putUnchecked(mo60939ix(this.position), sArr, i, i2);
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
            ByteBuffer byteBuffer = this.f564bb;
            if (!(byteBuffer instanceof DirectByteBuffer)) {
                System.arraycopy((Object) byteBuffer.array(), mo60939ix(position), (Object) this.f564bb.array(), mo60939ix(0), i << 1);
            } else {
                Memory.memmove(this, mo60939ix(0), this, mo60939ix(position), (long) (i << 1));
            }
            position(i);
            limit(capacity());
            discardMark();
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public boolean isDirect() {
        return this.f564bb.isDirect();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public ByteOrder order() {
        return this.order;
    }
}
