package java.nio;

import libcore.p030io.Memory;

class ByteBufferAsFloatBuffer extends FloatBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    /* renamed from: bb */
    protected final ByteBuffer f563bb;
    protected final int offset;
    private final ByteOrder order;

    ByteBufferAsFloatBuffer(ByteBuffer byteBuffer, int i, int i2, int i3, int i4, int i5, ByteOrder byteOrder) {
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

    public FloatBuffer slice() {
        int position = position();
        int limit = limit();
        int i = position <= limit ? limit - position : 0;
        return new ByteBufferAsFloatBuffer(this.f563bb, -1, 0, i, i, (position << 2) + this.offset, this.order);
    }

    public FloatBuffer duplicate() {
        return new ByteBufferAsFloatBuffer(this.f563bb, markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    public FloatBuffer asReadOnlyBuffer() {
        return new ByteBufferAsFloatBuffer(this.f563bb.asReadOnlyBuffer(), markValue(), position(), limit(), capacity(), this.offset, this.order);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60847ix(int i) {
        return (i << 2) + this.offset;
    }

    public float get() {
        return get(nextGetIndex());
    }

    public float get(int i) {
        return this.f563bb.getFloatUnchecked(mo60847ix(checkIndex(i)));
    }

    public FloatBuffer get(float[] fArr, int i, int i2) {
        checkBounds(i, i2, fArr.length);
        if (i2 <= remaining()) {
            this.f563bb.getUnchecked(mo60847ix(this.position), fArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferUnderflowException();
    }

    public FloatBuffer put(float f) {
        put(nextPutIndex(), f);
        return this;
    }

    public FloatBuffer put(int i, float f) {
        if (!this.isReadOnly) {
            this.f563bb.putFloatUnchecked(mo60847ix(checkIndex(i)), f);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public FloatBuffer put(float[] fArr, int i, int i2) {
        checkBounds(i, i2, fArr.length);
        if (i2 <= remaining()) {
            this.f563bb.putUnchecked(mo60847ix(this.position), fArr, i, i2);
            this.position += i2;
            return this;
        }
        throw new BufferOverflowException();
    }

    public FloatBuffer compact() {
        if (!this.isReadOnly) {
            int position = position();
            int limit = limit();
            int i = position <= limit ? limit - position : 0;
            ByteBuffer byteBuffer = this.f563bb;
            if (!(byteBuffer instanceof DirectByteBuffer)) {
                System.arraycopy((Object) byteBuffer.array(), mo60847ix(position), (Object) this.f563bb.array(), mo60847ix(0), i << 2);
            } else {
                Memory.memmove(this, mo60847ix(0), this, mo60847ix(position), (long) (i << 2));
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
