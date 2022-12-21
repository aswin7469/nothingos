package java.nio;

import libcore.p030io.Memory;

final class HeapByteBuffer extends ByteBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    public boolean isDirect() {
        return false;
    }

    HeapByteBuffer(int i, int i2) {
        this(i, i2, false);
    }

    private HeapByteBuffer(int i, int i2, boolean z) {
        super(-1, 0, i2, i, new byte[i], 0);
        this.isReadOnly = z;
    }

    HeapByteBuffer(byte[] bArr, int i, int i2) {
        this(bArr, i, i2, false);
    }

    private HeapByteBuffer(byte[] bArr, int i, int i2, boolean z) {
        super(-1, i, i + i2, bArr.length, bArr, 0);
        this.isReadOnly = z;
    }

    private HeapByteBuffer(byte[] bArr, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2, i3, i4, bArr, i5);
        this.isReadOnly = z;
    }

    public ByteBuffer slice() {
        return new HeapByteBuffer(this.f560hb, -1, 0, remaining(), remaining(), this.offset + position(), this.isReadOnly);
    }

    /* access modifiers changed from: package-private */
    public ByteBuffer slice(int i, int i2) {
        int i3 = i2 - i;
        return new HeapByteBuffer(this.f560hb, -1, 0, i3, i3, i + this.offset, this.isReadOnly);
    }

    public ByteBuffer duplicate() {
        return new HeapByteBuffer(this.f560hb, markValue(), position(), limit(), capacity(), this.offset, this.isReadOnly);
    }

    public ByteBuffer asReadOnlyBuffer() {
        return new HeapByteBuffer(this.f560hb, markValue(), position(), limit(), capacity(), this.offset, true);
    }

    /* access modifiers changed from: protected */
    /* renamed from: ix */
    public int mo60908ix(int i) {
        return i + this.offset;
    }

    public byte get() {
        return this.f560hb[mo60908ix(nextGetIndex())];
    }

    public byte get(int i) {
        return this.f560hb[mo60908ix(checkIndex(i))];
    }

    public ByteBuffer get(byte[] bArr, int i, int i2) {
        checkBounds(i, i2, bArr.length);
        if (i2 <= remaining()) {
            System.arraycopy((Object) this.f560hb, mo60908ix(position()), (Object) bArr, i, i2);
            position(position() + i2);
            return this;
        }
        throw new BufferUnderflowException();
    }

    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    public ByteBuffer put(byte b) {
        if (!this.isReadOnly) {
            this.f560hb[mo60908ix(nextPutIndex())] = b;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer put(int i, byte b) {
        if (!this.isReadOnly) {
            this.f560hb[mo60908ix(checkIndex(i))] = b;
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer put(byte[] bArr, int i, int i2) {
        if (!this.isReadOnly) {
            checkBounds(i, i2, bArr.length);
            if (i2 <= remaining()) {
                System.arraycopy((Object) bArr, i, (Object) this.f560hb, mo60908ix(position()), i2);
                position(position() + i2);
                return this;
            }
            throw new BufferOverflowException();
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer compact() {
        if (!this.isReadOnly) {
            System.arraycopy((Object) this.f560hb, mo60908ix(position()), (Object) this.f560hb, mo60908ix(0), remaining());
            position(remaining());
            limit(capacity());
            discardMark();
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public byte _get(int i) {
        return this.f560hb[i];
    }

    /* access modifiers changed from: package-private */
    public void _put(int i, byte b) {
        if (!this.isReadOnly) {
            this.f560hb[i] = b;
            return;
        }
        throw new ReadOnlyBufferException();
    }

    public char getChar() {
        return Bits.getChar(this, mo60908ix(nextGetIndex(2)), this.bigEndian);
    }

    public char getChar(int i) {
        return Bits.getChar(this, mo60908ix(checkIndex(i, 2)), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public char getCharUnchecked(int i) {
        return Bits.getChar(this, mo60908ix(i), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void getUnchecked(int i, char[] cArr, int i2, int i3) {
        Memory.unsafeBulkGet(cArr, i2, i3 * 2, this.f560hb, mo60908ix(i), 2, !this.nativeByteOrder);
    }

    public ByteBuffer putChar(char c) {
        if (!this.isReadOnly) {
            Bits.putChar(this, mo60908ix(nextPutIndex(2)), c, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer putChar(int i, char c) {
        if (!this.isReadOnly) {
            Bits.putChar(this, mo60908ix(checkIndex(i, 2)), c, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public void putCharUnchecked(int i, char c) {
        Bits.putChar(this, mo60908ix(i), c, this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void putUnchecked(int i, char[] cArr, int i2, int i3) {
        Memory.unsafeBulkPut(this.f560hb, mo60908ix(i), i3 * 2, cArr, i2, 2, !this.nativeByteOrder);
    }

    public CharBuffer asCharBuffer() {
        int remaining = remaining() >> 1;
        return new ByteBufferAsCharBuffer(this, -1, 0, remaining, remaining, position(), order());
    }

    public short getShort() {
        return Bits.getShort(this, mo60908ix(nextGetIndex(2)), this.bigEndian);
    }

    public short getShort(int i) {
        return Bits.getShort(this, mo60908ix(checkIndex(i, 2)), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public short getShortUnchecked(int i) {
        return Bits.getShort(this, mo60908ix(i), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void getUnchecked(int i, short[] sArr, int i2, int i3) {
        Memory.unsafeBulkGet(sArr, i2, i3 * 2, this.f560hb, mo60908ix(i), 2, !this.nativeByteOrder);
    }

    public ByteBuffer putShort(short s) {
        if (!this.isReadOnly) {
            Bits.putShort(this, mo60908ix(nextPutIndex(2)), s, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer putShort(int i, short s) {
        if (!this.isReadOnly) {
            Bits.putShort(this, mo60908ix(checkIndex(i, 2)), s, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public void putShortUnchecked(int i, short s) {
        Bits.putShort(this, mo60908ix(i), s, this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void putUnchecked(int i, short[] sArr, int i2, int i3) {
        Memory.unsafeBulkPut(this.f560hb, mo60908ix(i), i3 * 2, sArr, i2, 2, !this.nativeByteOrder);
    }

    public ShortBuffer asShortBuffer() {
        int remaining = remaining() >> 1;
        return new ByteBufferAsShortBuffer(this, -1, 0, remaining, remaining, position(), order());
    }

    public int getInt() {
        return Bits.getInt(this, mo60908ix(nextGetIndex(4)), this.bigEndian);
    }

    public int getInt(int i) {
        return Bits.getInt(this, mo60908ix(checkIndex(i, 4)), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public int getIntUnchecked(int i) {
        return Bits.getInt(this, mo60908ix(i), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void getUnchecked(int i, int[] iArr, int i2, int i3) {
        Memory.unsafeBulkGet(iArr, i2, i3 * 4, this.f560hb, mo60908ix(i), 4, !this.nativeByteOrder);
    }

    public ByteBuffer putInt(int i) {
        if (!this.isReadOnly) {
            Bits.putInt(this, mo60908ix(nextPutIndex(4)), i, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer putInt(int i, int i2) {
        if (!this.isReadOnly) {
            Bits.putInt(this, mo60908ix(checkIndex(i, 4)), i2, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public void putIntUnchecked(int i, int i2) {
        Bits.putInt(this, mo60908ix(i), i2, this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void putUnchecked(int i, int[] iArr, int i2, int i3) {
        Memory.unsafeBulkPut(this.f560hb, mo60908ix(i), i3 * 4, iArr, i2, 4, !this.nativeByteOrder);
    }

    public IntBuffer asIntBuffer() {
        int remaining = remaining() >> 2;
        return new ByteBufferAsIntBuffer(this, -1, 0, remaining, remaining, position(), order());
    }

    public long getLong() {
        return Bits.getLong(this, mo60908ix(nextGetIndex(8)), this.bigEndian);
    }

    public long getLong(int i) {
        return Bits.getLong(this, mo60908ix(checkIndex(i, 8)), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public long getLongUnchecked(int i) {
        return Bits.getLong(this, mo60908ix(i), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void getUnchecked(int i, long[] jArr, int i2, int i3) {
        Memory.unsafeBulkGet(jArr, i2, i3 * 8, this.f560hb, mo60908ix(i), 8, !this.nativeByteOrder);
    }

    public ByteBuffer putLong(long j) {
        if (!this.isReadOnly) {
            Bits.putLong(this, mo60908ix(nextPutIndex(8)), j, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer putLong(int i, long j) {
        if (!this.isReadOnly) {
            Bits.putLong(this, mo60908ix(checkIndex(i, 8)), j, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public void putLongUnchecked(int i, long j) {
        Bits.putLong(this, mo60908ix(i), j, this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void putUnchecked(int i, long[] jArr, int i2, int i3) {
        Memory.unsafeBulkPut(this.f560hb, mo60908ix(i), i3 * 8, jArr, i2, 8, !this.nativeByteOrder);
    }

    public LongBuffer asLongBuffer() {
        int remaining = remaining() >> 3;
        return new ByteBufferAsLongBuffer(this, -1, 0, remaining, remaining, position(), order());
    }

    public float getFloat() {
        return Bits.getFloat(this, mo60908ix(nextGetIndex(4)), this.bigEndian);
    }

    public float getFloat(int i) {
        return Bits.getFloat(this, mo60908ix(checkIndex(i, 4)), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public float getFloatUnchecked(int i) {
        return Bits.getFloat(this, mo60908ix(i), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void getUnchecked(int i, float[] fArr, int i2, int i3) {
        Memory.unsafeBulkGet(fArr, i2, i3 * 4, this.f560hb, mo60908ix(i), 4, !this.nativeByteOrder);
    }

    public ByteBuffer putFloat(float f) {
        if (!this.isReadOnly) {
            Bits.putFloat(this, mo60908ix(nextPutIndex(4)), f, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer putFloat(int i, float f) {
        if (!this.isReadOnly) {
            Bits.putFloat(this, mo60908ix(checkIndex(i, 4)), f, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public void putFloatUnchecked(int i, float f) {
        Bits.putFloat(this, mo60908ix(i), f, this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void putUnchecked(int i, float[] fArr, int i2, int i3) {
        Memory.unsafeBulkPut(this.f560hb, mo60908ix(i), i3 * 4, fArr, i2, 4, !this.nativeByteOrder);
    }

    public FloatBuffer asFloatBuffer() {
        int remaining = remaining() >> 2;
        return new ByteBufferAsFloatBuffer(this, -1, 0, remaining, remaining, position(), order());
    }

    public double getDouble() {
        return Bits.getDouble(this, mo60908ix(nextGetIndex(8)), this.bigEndian);
    }

    public double getDouble(int i) {
        return Bits.getDouble(this, mo60908ix(checkIndex(i, 8)), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public double getDoubleUnchecked(int i) {
        return Bits.getDouble(this, mo60908ix(i), this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void getUnchecked(int i, double[] dArr, int i2, int i3) {
        Memory.unsafeBulkGet(dArr, i2, i3 * 8, this.f560hb, mo60908ix(i), 8, !this.nativeByteOrder);
    }

    public ByteBuffer putDouble(double d) {
        if (!this.isReadOnly) {
            Bits.putDouble(this, mo60908ix(nextPutIndex(8)), d, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    public ByteBuffer putDouble(int i, double d) {
        if (!this.isReadOnly) {
            Bits.putDouble(this, mo60908ix(checkIndex(i, 8)), d, this.bigEndian);
            return this;
        }
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public void putDoubleUnchecked(int i, double d) {
        Bits.putDouble(this, mo60908ix(i), d, this.bigEndian);
    }

    /* access modifiers changed from: package-private */
    public void putUnchecked(int i, double[] dArr, int i2, int i3) {
        Memory.unsafeBulkPut(this.f560hb, mo60908ix(i), i3 * 8, dArr, i2, 8, !this.nativeByteOrder);
    }

    public DoubleBuffer asDoubleBuffer() {
        int remaining = remaining() >> 3;
        return new ByteBufferAsDoubleBuffer(this, -1, 0, remaining, remaining, position(), order());
    }
}
