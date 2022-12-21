package java.nio;

class StringCharBuffer extends CharBuffer {
    CharSequence str;

    public boolean isDirect() {
        return false;
    }

    public final boolean isReadOnly() {
        return true;
    }

    StringCharBuffer(CharSequence charSequence, int i, int i2) {
        super(-1, i, i2, charSequence.length());
        int length = charSequence.length();
        if (i < 0 || i > length || i2 < i || i2 > length) {
            throw new IndexOutOfBoundsException();
        }
        this.str = charSequence;
    }

    public CharBuffer slice() {
        return new StringCharBuffer(this.str, -1, 0, remaining(), remaining(), this.offset + position());
    }

    private StringCharBuffer(CharSequence charSequence, int i, int i2, int i3, int i4, int i5) {
        super(i, i2, i3, i4, (char[]) null, i5);
        this.str = charSequence;
    }

    public CharBuffer duplicate() {
        return new StringCharBuffer(this.str, markValue(), position(), limit(), capacity(), this.offset);
    }

    public CharBuffer asReadOnlyBuffer() {
        return duplicate();
    }

    public final char get() {
        return this.str.charAt(nextGetIndex() + this.offset);
    }

    public final char get(int i) {
        return this.str.charAt(checkIndex(i) + this.offset);
    }

    /* access modifiers changed from: package-private */
    public char getUnchecked(int i) {
        return this.str.charAt(i + this.offset);
    }

    public final CharBuffer put(char c) {
        throw new ReadOnlyBufferException();
    }

    public final CharBuffer put(int i, char c) {
        throw new ReadOnlyBufferException();
    }

    public final CharBuffer compact() {
        throw new ReadOnlyBufferException();
    }

    /* access modifiers changed from: package-private */
    public final String toString(int i, int i2) {
        return this.str.toString().substring(i + this.offset, i2 + this.offset);
    }

    public final CharBuffer subSequence(int i, int i2) {
        try {
            int position = position();
            return new StringCharBuffer(this.str, -1, position + checkIndex(i, position), position + checkIndex(i2, position), capacity(), this.offset);
        } catch (IllegalArgumentException unused) {
            throw new IndexOutOfBoundsException();
        }
    }

    public ByteOrder order() {
        return ByteOrder.nativeOrder();
    }
}
