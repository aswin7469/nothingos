package java.nio;

import libcore.p030io.Memory;

class ByteBufferAsLongBuffer extends LongBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: bb */
    protected final ByteBuffer f563bb;
    protected final int offset;
    private final ByteOrder order;

    ByteBufferAsLongBuffer(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, ByteOrder byteOrder) {
        super(i, i2, i3, i4);
        ByteBuffer duplicate = byteBuffer.duplicate();
        this.f563bb = duplicate;
        this.isReadOnly = byteBuffer.isReadOnly;
        if (byteBuffer instanceof DirectByteBuffer) {
            this.address = byteBuffer.address + ((long) i5);
        }
        duplicate.order(byteOrder);
        this.order = byteOrder;
        this.offset = i5;
    }

    public LongBuffer slice() {
        int position = position();
        int limit = limit();
        int i = position <= limit ? limit - position : 0;
        return new ByteBufferAsLongBuffer(this.f563bb, -1, 0, i, i, (position << 3) + this.offset, this.order);
    }

    public LongBuffer duplicate() {
        return new ByteBufferAsLongBuffer(this.f563bb, markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    public LongBuffer asReadOnlyBuffer() {
        return new ByteBufferAsLongBuffer(this.f563bb.asReadOnlyBuffer(), markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60927ix(int i) {
        return (i << 3) + this.offset;
    }

    public long get() {
        return get(nextGetIndex());
    }

    public long get(int i) {
        return this.f563bb.getLongUnchecked(mo60927ix(checkIndex(i)));
    }

    public LongBuffer get(long[] jArr, int i, int i2) {
        checkBounds(i, i2, jArr.length);
        if (i2 <= remaining()) {
            this.f563bb.getUnchecked(mo60927ix(this.position), jArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferUnderflowException();
    }

    public LongBuffer put(long j) {
        put(nextPutIndex(), j);
        return this;
    }

    public LongBuffer put(int i, long j) {
        if (!this.isReadOnly) {
            this.f563bb.putLongUnchecked(mo60927ix(checkIndex(i)), j);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public LongBuffer put(long[] jArr, int i, int i2) {
        checkBounds(i, i2, jArr.length);
        if (i2 <= remaining()) {
            this.f563bb.putUnchecked(mo60927ix(this.position), jArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferOverflowException();
    }

    public LongBuffer compact() {
        if (!this.isReadOnly) {
            int position = position();
            int limit = limit();
            int i = position <= limit ? limit - position : 0;
            ByteBuffer byteBuffer = this.f563bb;
            if (!(byteBuffer instanceof DirectByteBuffer)) {
                System.arraycopy((Object) byteBuffer.array(), mo60927ix(position), (Object) this.f563bb.array(), mo60927ix(0), i << 3);
            } else {
                Memory.memmove(this, mo60927ix(0), this, mo60927ix(position), (long) (i << 3));
            }
            position(i);
            limit(capacity());
            discardMark();
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public boolean isDirect() {
        return this.f563bb.isDirect();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public ByteOrder order() {
        return this.order;
    }
}
