package java.p026io;

import java.util.Arrays;

/* renamed from: java.io.CharArrayWriter */
public class CharArrayWriter extends Writer {
    protected char[] buf;
    protected int count;

    public void close() {
    }

    public void flush() {
    }

    public CharArrayWriter() {
        this(32);
    }

    public CharArrayWriter(int i) {
        if (i >= 0) {
            this.buf = new char[i];
            return;
        }
        throw new IllegalArgumentException("Negative initial size: " + i);
    }

    public void write(int i) {
        synchronized (this.lock) {
            int i2 = this.count + 1;
            char[] cArr = this.buf;
            if (i2 > cArr.length) {
                this.buf = Arrays.copyOf(cArr, Math.max(cArr.length << 1, i2));
            }
            this.buf[this.count] = (char) i;
            this.count = i2;
        }
    }

    public void write(char[] cArr, int i, int i2) {
        int i3;
        if (i < 0 || i > cArr.length || i2 < 0 || (i3 = i + i2) > cArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        } else if (i2 != 0) {
            synchronized (this.lock) {
                int i4 = this.count + i2;
                char[] cArr2 = this.buf;
                if (i4 > cArr2.length) {
                    this.buf = Arrays.copyOf(cArr2, Math.max(cArr2.length << 1, i4));
                }
                System.arraycopy((Object) cArr, i, (Object) this.buf, this.count, i2);
                this.count = i4;
            }
        }
    }

    public void write(String str, int i, int i2) {
        synchronized (this.lock) {
            int i3 = this.count + i2;
            char[] cArr = this.buf;
            if (i3 > cArr.length) {
                this.buf = Arrays.copyOf(cArr, Math.max(cArr.length << 1, i3));
            }
            str.getChars(i, i2 + i, this.buf, this.count);
            this.count = i3;
        }
    }

    public void writeTo(Writer writer) throws IOException {
        synchronized (this.lock) {
            writer.write(this.buf, 0, this.count);
        }
    }

    public CharArrayWriter append(CharSequence charSequence) {
        String valueOf = String.valueOf((Object) charSequence);
        write(valueOf, 0, valueOf.length());
        return this;
    }

    public CharArrayWriter append(CharSequence charSequence, int i, int i2) {
        if (charSequence == null) {
            charSequence = "null";
        }
        return append(charSequence.subSequence(i, i2));
    }

    public CharArrayWriter append(char c) {
        write(c);
        return this;
    }

    public void reset() {
        this.count = 0;
    }

    public char[] toCharArray() {
        char[] copyOf;
        synchronized (this.lock) {
            copyOf = Arrays.copyOf(this.buf, this.count);
        }
        return copyOf;
    }

    public int size() {
        return this.count;
    }

    public String toString() {
        String str;
        synchronized (this.lock) {
            str = new String(this.buf, 0, this.count);
        }
        return str;
    }
}
