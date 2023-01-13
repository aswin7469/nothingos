package java.nio;

import java.p026io.IOException;
import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public abstract class CharBuffer extends Buffer implements Comparable<CharBuffer>, Appendable, CharSequence, Readable {

    /* renamed from: hb */
    final char[] f565hb;
    boolean isReadOnly;
    final int offset;

    private static boolean equals(char c, char c2) {
        return c == c2;
    }

    public abstract CharBuffer asReadOnlyBuffer();

    public abstract CharBuffer compact();

    public abstract CharBuffer duplicate();

    public abstract char get();

    public abstract char get(int i);

    /* access modifiers changed from: package-private */
    public abstract char getUnchecked(int i);

    public abstract boolean isDirect();

    public abstract ByteOrder order();

    public abstract CharBuffer put(char c);

    public abstract CharBuffer put(int i, char c);

    public abstract CharBuffer slice();

    public abstract CharBuffer subSequence(int i, int i2);

    /* access modifiers changed from: package-private */
    public abstract String toString(int i, int i2);

    CharBuffer(int i, int i2, int i3, int i4, char[] cArr, int i5) {
        super(i, i2, i3, i4, 1);
        this.f565hb = cArr;
        this.offset = i5;
    }

    CharBuffer(int i, int i2, int i3, int i4) {
        this(i, i2, i3, i4, (char[]) null, 0);
    }

    public static CharBuffer allocate(int i) {
        if (i >= 0) {
            return new HeapCharBuffer(i, i);
        }
        throw new IllegalArgumentException();
    }

    public static CharBuffer wrap(char[] cArr, int i, int i2) {
        try {
            return new HeapCharBuffer(cArr, i, i2);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static CharBuffer wrap(char[] cArr) {
        return wrap(cArr, 0, cArr.length);
    }

    public int read(CharBuffer charBuffer) throws IOException {
        int remaining = charBuffer.remaining();
        int remaining2 = remaining();
        if (remaining2 == 0) {
            return -1;
        }
        int min = Math.min(remaining2, remaining);
        int limit = limit();
        if (remaining < remaining2) {
            limit(position() + min);
        }
        if (min > 0) {
            try {
                charBuffer.put(this);
            } catch (Throwable th) {
                limit(limit);
                throw th;
            }
        }
        limit(limit);
        return min;
    }

    public static CharBuffer wrap(CharSequence charSequence, int i, int i2) {
        try {
            return new StringCharBuffer(charSequence, i, i2);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public static CharBuffer wrap(CharSequence charSequence) {
        return wrap(charSequence, 0, charSequence.length());
    }

    public CharBuffer get(char[] cArr, int i, int i2) {
        checkBounds(i, i2, cArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                cArr[i] = get();
                i++;
            }
            return this;
        }
        throw new BufferUnderflowException();
    }

    public CharBuffer get(char[] cArr) {
        return get(cArr, 0, cArr.length);
    }

    public CharBuffer put(CharBuffer charBuffer) {
        if (charBuffer == this) {
            throw new IllegalArgumentException();
        } else if (!isReadOnly()) {
            int remaining = charBuffer.remaining();
            if (remaining <= remaining()) {
                for (int i = 0; i < remaining; i++) {
                    put(charBuffer.get());
                }
                return this;
            }
            throw new BufferOverflowException();
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public CharBuffer put(char[] cArr, int i, int i2) {
        checkBounds(i, i2, cArr.length);
        if (i2 <= remaining()) {
            int i3 = i2 + i;
            while (i < i3) {
                put(cArr[i]);
                i++;
            }
            return this;
        }
        throw new BufferOverflowException();
    }

    public final CharBuffer put(char[] cArr) {
        return put(cArr, 0, cArr.length);
    }

    public CharBuffer put(String str, int i, int i2) {
        int i3 = i2 - i;
        checkBounds(i, i3, str.length());
        if (i == i2) {
            return this;
        }
        if (isReadOnly()) {
            throw new ReadOnlyBufferException();
        } else if (i3 <= remaining()) {
            while (i < i2) {
                put(str.charAt(i));
                i++;
            }
            return this;
        } else {
            throw new BufferOverflowException();
        }
    }

    public final CharBuffer put(String str) {
        return put(str, 0, str.length());
    }

    public final boolean hasArray() {
        return this.f565hb != null && !this.isReadOnly;
    }

    public final char[] array() {
        char[] cArr = this.f565hb;
        if (cArr == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return cArr;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public final int arrayOffset() {
        if (this.f565hb == null) {
            throw new UnsupportedOperationException();
        } else if (!this.isReadOnly) {
            return this.offset;
        } else {
            throw new ReadOnlyBufferException();
        }
    }

    public Buffer position(int i) {
        return super.position(i);
    }

    public Buffer limit(int i) {
        return super.limit(i);
    }

    public Buffer mark() {
        return super.mark();
    }

    public Buffer reset() {
        return super.reset();
    }

    public Buffer clear() {
        return super.clear();
    }

    public Buffer flip() {
        return super.flip();
    }

    public Buffer rewind() {
        return super.rewind();
    }

    public int hashCode() {
        int position = position();
        int i = 1;
        for (int limit = limit() - 1; limit >= position; limit--) {
            i = (i * 31) + get(limit);
        }
        return i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CharBuffer)) {
            return false;
        }
        CharBuffer charBuffer = (CharBuffer) obj;
        if (remaining() != charBuffer.remaining()) {
            return false;
        }
        int position = position();
        int limit = limit() - 1;
        int limit2 = charBuffer.limit() - 1;
        while (limit >= position) {
            if (!equals(get(limit), charBuffer.get(limit2))) {
                return false;
            }
            limit--;
            limit2--;
        }
        return true;
    }

    public int compareTo(CharBuffer charBuffer) {
        int position = position() + Math.min(remaining(), charBuffer.remaining());
        int position2 = position();
        int position3 = charBuffer.position();
        while (position2 < position) {
            int compare = compare(get(position2), charBuffer.get(position3));
            if (compare != 0) {
                return compare;
            }
            position2++;
            position3++;
        }
        return remaining() - charBuffer.remaining();
    }

    private static int compare(char c, char c2) {
        return Character.compare(c, c2);
    }

    public String toString() {
        return toString(position(), limit());
    }

    public final int length() {
        return remaining();
    }

    public final char charAt(int i) {
        return get(position() + checkIndex(i, 1));
    }

    public CharBuffer append(CharSequence charSequence) {
        if (charSequence == null) {
            return put("null");
        }
        return put(charSequence.toString());
    }

    public CharBuffer append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        return put(charSequence.subSequence(i, i2).toString());
    }

    public CharBuffer append(char c) {
        return put(c);
    }

    public IntStream chars() {
        return StreamSupport.intStream(new CharBuffer$$ExternalSyntheticLambda0(this), 16464, false);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$chars$0$java-nio-CharBuffer  reason: not valid java name */
    public /* synthetic */ Spliterator.OfInt m3722lambda$chars$0$javanioCharBuffer() {
        return new CharBufferSpliterator(this);
    }
}
