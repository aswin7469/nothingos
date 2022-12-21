package java.nio;

import libcore.p030io.Memory;

class ByteBufferAsDoubleBuffer extends DoubleBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: bb */
    protected final ByteBuffer f562bb;
    protected final int offset;
    private final ByteOrder order;

    ByteBufferAsDoubleBuffer(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, ByteOrder byteOrder) {
        super(i, i2, i3, i4);
        ByteBuffer duplicate = byteBuffer.duplicate();
        this.f562bb = duplicate;
        this.isReadOnly = byteBuffer.isReadOnly;
        if (byteBuffer instanceof DirectByteBuffer) {
            this.address = byteBuffer.address + ((long) i5);
        }
        duplicate.order(byteOrder);
        this.order = byteOrder;
        this.offset = i5;
    }

    public DoubleBuffer slice() {
        int position = position();
        int limit = limit();
        int i = position <= limit ? limit - position : 0;
        return new ByteBufferAsDoubleBuffer(this.f562bb, -1, 0, i, i, (position << 3) + this.offset, this.order);
    }

    public DoubleBuffer duplicate() {
        return new ByteBufferAsDoubleBuffer(this.f562bb, markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    public DoubleBuffer asReadOnlyBuffer() {
        return new ByteBufferAsDoubleBuffer(this.f562bb.asReadOnlyBuffer(), markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60835ix(int i) {
        return (i << 3) + this.offset;
    }

    public double get() {
        return get(nextGetIndex());
    }

    public double get(int i) {
        return this.f562bb.getDoubleUnchecked(mo60835ix(checkIndex(i)));
    }

    public DoubleBuffer get(double[] dArr, int i, int i2) {
        checkBounds(i, i2, dArr.length);
        if (i2 <= remaining()) {
            this.f562bb.getUnchecked(mo60835ix(this.position), dArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferUnderflowException();
    }

    public DoubleBuffer put(double d) {
        put(nextPutIndex(), d);
        return this;
    }

    public DoubleBuffer put(int i, double d) {
        if (!this.isReadOnly) {
            this.f562bb.putDoubleUnchecked(mo60835ix(checkIndex(i)), d);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public DoubleBuffer put(double[] dArr, int i, int i2) {
        checkBounds(i, i2, dArr.length);
        if (i2 <= remaining()) {
            this.f562bb.putUnchecked(mo60835ix(this.position), dArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferOverflowException();
    }

    public DoubleBuffer compact() {
        if (!this.isReadOnly) {
            int position = position();
            int limit = limit();
            int i = position <= limit ? limit - position : 0;
            ByteBuffer byteBuffer = this.f562bb;
            if (!(byteBuffer instanceof DirectByteBuffer)) {
                System.arraycopy((Object) byteBuffer.array(), mo60835ix(position), (Object) this.f562bb.array(), mo60835ix(0), i << 3);
            } else {
                Memory.memmove(this, mo60835ix(0), this, mo60835ix(position), (long) (i << 3));
            }
            position(i);
            limit(capacity());
            discardMark();
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public boolean isDirect() {
        return this.f562bb.isDirect();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public ByteOrder order() {
        return this.order;
    }
}
