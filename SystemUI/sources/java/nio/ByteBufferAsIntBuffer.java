package java.nio;

import libcore.p030io.Memory;

class ByteBufferAsIntBuffer extends IntBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: bb */
    protected final ByteBuffer f562bb;
    protected final int offset;
    private final ByteOrder order;

    ByteBufferAsIntBuffer(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, ByteOrder byteOrder) {
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

    public IntBuffer slice() {
        int position = position();
        int limit = limit();
        int i = position <= limit ? limit - position : 0;
        return new ByteBufferAsIntBuffer(this.f562bb, -1, 0, i, i, (position << 2) + this.offset, this.order);
    }

    public IntBuffer duplicate() {
        return new ByteBufferAsIntBuffer(this.f562bb, markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    public IntBuffer asReadOnlyBuffer() {
        return new ByteBufferAsIntBuffer(this.f562bb.asReadOnlyBuffer(), markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60915ix(int i) {
        return (i << 2) + this.offset;
    }

    public int get() {
        return get(nextGetIndex());
    }

    public int get(int i) {
        return this.f562bb.getIntUnchecked(mo60915ix(checkIndex(i)));
    }

    public IntBuffer get(int[] iArr, int i, int i2) {
        checkBounds(i, i2, iArr.length);
        if (i2 <= remaining()) {
            this.f562bb.getUnchecked(mo60915ix(this.position), iArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferUnderflowException();
    }

    public IntBuffer put(int i) {
        put(nextPutIndex(), i);
        return this;
    }

    public IntBuffer put(int i, int i2) {
        if (!this.isReadOnly) {
            this.f562bb.putIntUnchecked(mo60915ix(checkIndex(i)), i2);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public IntBuffer put(int[] iArr, int i, int i2) {
        checkBounds(i, i2, iArr.length);
        if (i2 <= remaining()) {
            this.f562bb.putUnchecked(mo60915ix(this.position), iArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferOverflowException();
    }

    public IntBuffer compact() {
        if (!this.isReadOnly) {
            int position = position();
            int limit = limit();
            int i = position <= limit ? limit - position : 0;
            ByteBuffer byteBuffer = this.f562bb;
            if (!(byteBuffer instanceof DirectByteBuffer)) {
                System.arraycopy((Object) byteBuffer.array(), mo60915ix(position), (Object) this.f562bb.array(), mo60915ix(0), i << 2);
            } else {
                Memory.memmove(this, mo60915ix(0), this, mo60915ix(position), (long) (i << 2));
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
