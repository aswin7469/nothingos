package java.nio;

import dalvik.system.VMRuntime;
import java.p026io.FileDescriptor;
import libcore.p030io.Memory;
import sun.misc.Cleaner;
import sun.nio.p033ch.DirectBuffer;

public class DirectByteBuffer extends MappedByteBuffer implements DirectBuffer {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    final Cleaner cleaner;
    final MemoryRef memoryRef;

    public final boolean isDirect() {
        return true;
    }

    static final class MemoryRef {
        long allocatedAddress;
        byte[] buffer;
        boolean isAccessible;
        boolean isFreed;
        final int offset;
        final Object originalBufferObject;

        MemoryRef(int i) {
            VMRuntime runtime = VMRuntime.getRuntime();
            byte[] bArr = (byte[]) runtime.newNonMovableArray(Byte.TYPE, i + 7);
            this.buffer = bArr;
            long addressOf = runtime.addressOf(bArr);
            this.allocatedAddress = addressOf;
            this.offset = (int) (((7 + addressOf) & -8) - addressOf);
            this.isAccessible = true;
            this.isFreed = false;
            this.originalBufferObject = null;
        }

        MemoryRef(long j, Object obj) {
            this.buffer = null;
            this.allocatedAddress = j;
            this.offset = 0;
            this.originalBufferObject = obj;
            this.isAccessible = true;
        }

        /* access modifiers changed from: package-private */
        public void free() {
            this.buffer = null;
            this.allocatedAddress = 0;
            this.isAccessible = false;
            this.isFreed = true;
        }
    }

    DirectByteBuffer(int i, MemoryRef memoryRef2) {
        super(-1, 0, i, i, memoryRef2.buffer, memoryRef2.offset);
        this.memoryRef = memoryRef2;
        this.address = memoryRef2.allocatedAddress + ((long) memoryRef2.offset);
        this.cleaner = null;
        this.isReadOnly = false;
    }

    private DirectByteBuffer(long j, int i) {
        super(-1, 0, i, i);
        this.memoryRef = new MemoryRef(j, this);
        this.address = j;
        this.cleaner = null;
    }

    public DirectByteBuffer(int i, long j, FileDescriptor fileDescriptor, Runnable runnable, boolean z) {
        super(-1, 0, i, i, fileDescriptor);
        this.isReadOnly = z;
        MemoryRef memoryRef2 = new MemoryRef(j, (Object) null);
        this.memoryRef = memoryRef2;
        this.address = j;
        this.cleaner = Cleaner.create(memoryRef2, runnable);
    }

    DirectByteBuffer(MemoryRef memoryRef2, int i, int i2, int i3, int i4, int i5) {
        this(memoryRef2, i, i2, i3, i4, i5, false);
    }

    DirectByteBuffer(MemoryRef memoryRef2, int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2, i3, i4, memoryRef2.buffer, i5);
        this.isReadOnly = z;
        this.memoryRef = memoryRef2;
        this.address = memoryRef2.allocatedAddress + ((long) i5);
        this.cleaner = null;
    }

    public final Object attachment() {
        return this.memoryRef;
    }

    public final Cleaner cleaner() {
        return this.cleaner;
    }

    public final ByteBuffer slice() {
        if (this.memoryRef.isAccessible) {
            int position = position();
            int limit = limit();
            int i = position <= limit ? limit - position : 0;
            return new DirectByteBuffer(this.memoryRef, -1, 0, i, i, position + this.offset, this.isReadOnly);
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public ByteBuffer slice(int i, int i2) {
        if (this.memoryRef.isAccessible) {
            int i3 = i <= i2 ? i2 - i : 0;
            return new DirectByteBuffer(this.memoryRef, -1, 0, i3, i3, i + this.offset, this.isReadOnly);
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final ByteBuffer duplicate() {
        if (!this.memoryRef.isFreed) {
            return new DirectByteBuffer(this.memoryRef, markValue(), position(), limit(), capacity(), this.offset, this.isReadOnly);
        }
        throw new IllegalStateException("buffer has been freed");
    }

    public final ByteBuffer asReadOnlyBuffer() {
        if (!this.memoryRef.isFreed) {
            return new DirectByteBuffer(this.memoryRef, markValue(), position(), limit(), capacity(), this.offset, true);
        }
        throw new IllegalStateException("buffer has been freed");
    }

    public final long address() {
        return this.address;
    }

    /* renamed from: ix */
    private long m1710ix(int i) {
        return this.address + ((long) i);
    }

    private byte get(long j) {
        return Memory.peekByte(j);
    }

    public final byte get() {
        if (this.memoryRef.isAccessible) {
            return get(m1710ix(nextGetIndex()));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final byte get(int i) {
        if (this.memoryRef.isAccessible) {
            return get(m1710ix(checkIndex(i)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public ByteBuffer get(byte[] bArr, int i, int i2) {
        if (this.memoryRef.isAccessible) {
            checkBounds(i, i2, bArr.length);
            int position = position();
            int limit = limit();
            if (i2 <= (position <= limit ? limit - position : 0)) {
                Memory.peekByteArray(m1710ix(position), bArr, i, i2);
                this.position = position + i2;
                return this;
            }
            throw new BufferUnderflowException();
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    private ByteBuffer put(long j, byte b) {
        Memory.pokeByte(j, b);
        return this;
    }

    public ByteBuffer put(ByteBuffer byteBuffer) {
        if (this.memoryRef.isAccessible) {
            return super.put(byteBuffer);
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final ByteBuffer put(byte b) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            put(m1710ix(nextPutIndex()), b);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final ByteBuffer put(int i, byte b) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            put(m1710ix(checkIndex(i)), b);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public ByteBuffer put(byte[] bArr, int i, int i2) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            checkBounds(i, i2, bArr.length);
            int position = position();
            int limit = limit();
            if (i2 <= (position <= limit ? limit - position : 0)) {
                Memory.pokeByteArray(m1710ix(position), bArr, i, i2);
                this.position = position + i2;
                return this;
            }
            throw new BufferOverflowException();
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final ByteBuffer compact() {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            int position = position();
            int limit = limit();
            int i = position <= limit ? limit - position : 0;
            System.arraycopy((Object) this.f558hb, this.position + this.offset, (Object) this.f558hb, this.offset, remaining());
            position(i);
            limit(capacity());
            discardMark();
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final boolean isReadOnly() {
        return this.isReadOnly;
    }

    /* access modifiers changed from: package-private */
    public final byte _get(int i) {
        return get(i);
    }

    /* access modifiers changed from: package-private */
    public final void _put(int i, byte b) {
        put(i, b);
    }

    public final char getChar() {
        if (this.memoryRef.isAccessible) {
            int i = this.position + 2;
            if (i <= limit()) {
                char peekShort = (char) Memory.peekShort(m1710ix(this.position), !this.nativeByteOrder);
                this.position = i;
                return peekShort;
            }
            throw new BufferUnderflowException();
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final char getChar(int i) {
        if (this.memoryRef.isAccessible) {
            checkIndex(i, 2);
            return (char) Memory.peekShort(m1710ix(i), !this.nativeByteOrder);
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public char getCharUnchecked(int i) {
        if (this.memoryRef.isAccessible) {
            return (char) Memory.peekShort(m1710ix(i), !this.nativeByteOrder);
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public void getUnchecked(int i, char[] cArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.peekCharArray(m1710ix(i), cArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    private ByteBuffer putChar(long j, char c) {
        Memory.pokeShort(j, (short) c, !this.nativeByteOrder);
        return this;
    }

    public final ByteBuffer putChar(char c) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putChar(m1710ix(nextPutIndex(2)), c);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final ByteBuffer putChar(int i, char c) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putChar(m1710ix(checkIndex(i, 2)), c);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    /* access modifiers changed from: package-private */
    public void putCharUnchecked(int i, char c) {
        if (this.memoryRef.isAccessible) {
            putChar(m1710ix(i), c);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public void putUnchecked(int i, char[] cArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.pokeCharArray(m1710ix(i), cArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final CharBuffer asCharBuffer() {
        if (!this.memoryRef.isFreed) {
            int position = position();
            int limit = limit();
            int i = (position <= limit ? limit - position : 0) >> 1;
            return new ByteBufferAsCharBuffer(this, -1, 0, i, i, position, order());
        }
        throw new IllegalStateException("buffer has been freed");
    }

    private short getShort(long j) {
        return Memory.peekShort(j, !this.nativeByteOrder);
    }

    public final short getShort() {
        if (this.memoryRef.isAccessible) {
            return getShort(m1710ix(nextGetIndex(2)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final short getShort(int i) {
        if (this.memoryRef.isAccessible) {
            return getShort(m1710ix(checkIndex(i, 2)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public short getShortUnchecked(int i) {
        if (this.memoryRef.isAccessible) {
            return getShort(m1710ix(i));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public void getUnchecked(int i, short[] sArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.peekShortArray(m1710ix(i), sArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    private ByteBuffer putShort(long j, short s) {
        Memory.pokeShort(j, s, !this.nativeByteOrder);
        return this;
    }

    public final ByteBuffer putShort(short s) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putShort(m1710ix(nextPutIndex(2)), s);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final ByteBuffer putShort(int i, short s) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putShort(m1710ix(checkIndex(i, 2)), s);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    /* access modifiers changed from: package-private */
    public void putShortUnchecked(int i, short s) {
        if (this.memoryRef.isAccessible) {
            putShort(m1710ix(i), s);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public void putUnchecked(int i, short[] sArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.pokeShortArray(m1710ix(i), sArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final ShortBuffer asShortBuffer() {
        if (!this.memoryRef.isFreed) {
            int position = position();
            int limit = limit();
            int i = (position <= limit ? limit - position : 0) >> 1;
            return new ByteBufferAsShortBuffer(this, -1, 0, i, i, position, order());
        }
        throw new IllegalStateException("buffer has been freed");
    }

    private int getInt(long j) {
        return Memory.peekInt(j, !this.nativeByteOrder);
    }

    public int getInt() {
        if (this.memoryRef.isAccessible) {
            return getInt(m1710ix(nextGetIndex(4)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public int getInt(int i) {
        if (this.memoryRef.isAccessible) {
            return getInt(m1710ix(checkIndex(i, 4)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final int getIntUnchecked(int i) {
        if (this.memoryRef.isAccessible) {
            return getInt(m1710ix(i));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final void getUnchecked(int i, int[] iArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.peekIntArray(m1710ix(i), iArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    private ByteBuffer putInt(long j, int i) {
        Memory.pokeInt(j, i, !this.nativeByteOrder);
        return this;
    }

    public final ByteBuffer putInt(int i) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putInt(m1710ix(nextPutIndex(4)), i);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final ByteBuffer putInt(int i, int i2) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putInt(m1710ix(checkIndex(i, 4)), i2);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    /* access modifiers changed from: package-private */
    public final void putIntUnchecked(int i, int i2) {
        if (this.memoryRef.isAccessible) {
            putInt(m1710ix(i), i2);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final void putUnchecked(int i, int[] iArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.pokeIntArray(m1710ix(i), iArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final IntBuffer asIntBuffer() {
        if (!this.memoryRef.isFreed) {
            int position = position();
            int limit = limit();
            int i = (position <= limit ? limit - position : 0) >> 2;
            return new ByteBufferAsIntBuffer(this, -1, 0, i, i, position, order());
        }
        throw new IllegalStateException("buffer has been freed");
    }

    private long getLong(long j) {
        return Memory.peekLong(j, !this.nativeByteOrder);
    }

    public final long getLong() {
        if (this.memoryRef.isAccessible) {
            return getLong(m1710ix(nextGetIndex(8)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final long getLong(int i) {
        if (this.memoryRef.isAccessible) {
            return getLong(m1710ix(checkIndex(i, 8)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final long getLongUnchecked(int i) {
        if (this.memoryRef.isAccessible) {
            return getLong(m1710ix(i));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final void getUnchecked(int i, long[] jArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.peekLongArray(m1710ix(i), jArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    private ByteBuffer putLong(long j, long j2) {
        Memory.pokeLong(j, j2, !this.nativeByteOrder);
        return this;
    }

    public final ByteBuffer putLong(long j) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putLong(m1710ix(nextPutIndex(8)), j);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final ByteBuffer putLong(int i, long j) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putLong(m1710ix(checkIndex(i, 8)), j);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    /* access modifiers changed from: package-private */
    public final void putLongUnchecked(int i, long j) {
        if (this.memoryRef.isAccessible) {
            putLong(m1710ix(i), j);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final void putUnchecked(int i, long[] jArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.pokeLongArray(m1710ix(i), jArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final LongBuffer asLongBuffer() {
        if (!this.memoryRef.isFreed) {
            int position = position();
            int limit = limit();
            int i = (position <= limit ? limit - position : 0) >> 3;
            return new ByteBufferAsLongBuffer(this, -1, 0, i, i, position, order());
        }
        throw new IllegalStateException("buffer has been freed");
    }

    private float getFloat(long j) {
        return Float.intBitsToFloat(Memory.peekInt(j, !this.nativeByteOrder));
    }

    public final float getFloat() {
        if (this.memoryRef.isAccessible) {
            return getFloat(m1710ix(nextGetIndex(4)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final float getFloat(int i) {
        if (this.memoryRef.isAccessible) {
            return getFloat(m1710ix(checkIndex(i, 4)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final float getFloatUnchecked(int i) {
        if (this.memoryRef.isAccessible) {
            return getFloat(m1710ix(i));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final void getUnchecked(int i, float[] fArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.peekFloatArray(m1710ix(i), fArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    private ByteBuffer putFloat(long j, float f) {
        Memory.pokeInt(j, Float.floatToRawIntBits(f), !this.nativeByteOrder);
        return this;
    }

    public final ByteBuffer putFloat(float f) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putFloat(m1710ix(nextPutIndex(4)), f);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final ByteBuffer putFloat(int i, float f) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putFloat(m1710ix(checkIndex(i, 4)), f);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    /* access modifiers changed from: package-private */
    public final void putFloatUnchecked(int i, float f) {
        if (this.memoryRef.isAccessible) {
            putFloat(m1710ix(i), f);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final void putUnchecked(int i, float[] fArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.pokeFloatArray(m1710ix(i), fArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final FloatBuffer asFloatBuffer() {
        if (!this.memoryRef.isFreed) {
            int position = position();
            int limit = limit();
            int i = (position <= limit ? limit - position : 0) >> 2;
            return new ByteBufferAsFloatBuffer(this, -1, 0, i, i, position, order());
        }
        throw new IllegalStateException("buffer has been freed");
    }

    private double getDouble(long j) {
        return Double.longBitsToDouble(Memory.peekLong(j, !this.nativeByteOrder));
    }

    public final double getDouble() {
        if (this.memoryRef.isAccessible) {
            return getDouble(m1710ix(nextGetIndex(8)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final double getDouble(int i) {
        if (this.memoryRef.isAccessible) {
            return getDouble(m1710ix(checkIndex(i, 8)));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final double getDoubleUnchecked(int i) {
        if (this.memoryRef.isAccessible) {
            return getDouble(m1710ix(i));
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final void getUnchecked(int i, double[] dArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.peekDoubleArray(m1710ix(i), dArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    private ByteBuffer putDouble(long j, double d) {
        Memory.pokeLong(j, Double.doubleToRawLongBits(d), !this.nativeByteOrder);
        return this;
    }

    public final ByteBuffer putDouble(double d) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putDouble(m1710ix(nextPutIndex(8)), d);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final ByteBuffer putDouble(int i, double d) {
        if (!this.memoryRef.isAccessible) {
            throw new IllegalStateException("buffer is inaccessible");
        } else if (!this.isReadOnly) {
            putDouble(m1710ix(checkIndex(i, 8)), d);
            return this;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    /* access modifiers changed from: package-private */
    public final void putDoubleUnchecked(int i, double d) {
        if (this.memoryRef.isAccessible) {
            putDouble(m1710ix(i), d);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    /* access modifiers changed from: package-private */
    public final void putUnchecked(int i, double[] dArr, int i2, int i3) {
        if (this.memoryRef.isAccessible) {
            Memory.pokeDoubleArray(m1710ix(i), dArr, i2, i3, !this.nativeByteOrder);
            return;
        }
        throw new IllegalStateException("buffer is inaccessible");
    }

    public final DoubleBuffer asDoubleBuffer() {
        if (!this.memoryRef.isFreed) {
            int position = position();
            int limit = limit();
            int i = (position <= limit ? limit - position : 0) >> 3;
            return new ByteBufferAsDoubleBuffer(this, -1, 0, i, i, position, order());
        }
        throw new IllegalStateException("buffer has been freed");
    }

    public final boolean isAccessible() {
        return this.memoryRef.isAccessible;
    }

    public final void setAccessible(boolean z) {
        this.memoryRef.isAccessible = z;
    }
}
