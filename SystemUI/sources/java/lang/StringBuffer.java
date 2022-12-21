package java.lang;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.util.Arrays;

public final class StringBuffer extends AbstractStringBuilder implements Serializable, CharSequence {
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("value", char[].class), new ObjectStreamField("count", Integer.TYPE), new ObjectStreamField("shared", Boolean.TYPE)};
    static final long serialVersionUID = 3388685877147921107L;
    private transient char[] toStringCache;

    public StringBuffer() {
        super(16);
    }

    public StringBuffer(int i) {
        super(i);
    }

    public StringBuffer(String str) {
        super(str.length() + 16);
        append(str);
    }

    public StringBuffer(CharSequence charSequence) {
        this(charSequence.length() + 16);
        append(charSequence);
    }

    public synchronized int length() {
        return this.count;
    }

    public synchronized int capacity() {
        return this.value.length;
    }

    public synchronized void ensureCapacity(int i) {
        super.ensureCapacity(i);
    }

    public synchronized void trimToSize() {
        super.trimToSize();
    }

    public synchronized void setLength(int i) {
        this.toStringCache = null;
        super.setLength(i);
    }

    public synchronized char charAt(int i) {
        if (i >= 0) {
            if (i < this.count) {
            }
        }
        throw new StringIndexOutOfBoundsException(i);
        return this.value[i];
    }

    public synchronized int codePointAt(int i) {
        return super.codePointAt(i);
    }

    public synchronized int codePointBefore(int i) {
        return super.codePointBefore(i);
    }

    public synchronized int codePointCount(int i, int i2) {
        return super.codePointCount(i, i2);
    }

    public synchronized int offsetByCodePoints(int i, int i2) {
        return super.offsetByCodePoints(i, i2);
    }

    public synchronized void getChars(int i, int i2, char[] cArr, int i3) {
        super.getChars(i, i2, cArr, i3);
    }

    public synchronized void setCharAt(int i, char c) {
        if (i >= 0) {
            if (i < this.count) {
                this.toStringCache = null;
                this.value[i] = c;
            }
        }
        throw new StringIndexOutOfBoundsException(i);
    }

    public synchronized StringBuffer append(Object obj) {
        this.toStringCache = null;
        super.append(String.valueOf(obj));
        return this;
    }

    public synchronized StringBuffer append(String str) {
        this.toStringCache = null;
        super.append(str);
        return this;
    }

    public synchronized StringBuffer append(StringBuffer stringBuffer) {
        this.toStringCache = null;
        super.append(stringBuffer);
        return this;
    }

    /* access modifiers changed from: package-private */
    public synchronized StringBuffer append(AbstractStringBuilder abstractStringBuilder) {
        this.toStringCache = null;
        super.append(abstractStringBuilder);
        return this;
    }

    public synchronized StringBuffer append(CharSequence charSequence) {
        this.toStringCache = null;
        super.append(charSequence);
        return this;
    }

    public synchronized StringBuffer append(CharSequence charSequence, int i, int i2) {
        this.toStringCache = null;
        super.append(charSequence, i, i2);
        return this;
    }

    public synchronized StringBuffer append(char[] cArr) {
        this.toStringCache = null;
        super.append(cArr);
        return this;
    }

    public synchronized StringBuffer append(char[] cArr, int i, int i2) {
        this.toStringCache = null;
        super.append(cArr, i, i2);
        return this;
    }

    public synchronized StringBuffer append(boolean z) {
        this.toStringCache = null;
        super.append(z);
        return this;
    }

    public synchronized StringBuffer append(char c) {
        this.toStringCache = null;
        super.append(c);
        return this;
    }

    public synchronized StringBuffer append(int i) {
        this.toStringCache = null;
        super.append(i);
        return this;
    }

    public synchronized StringBuffer appendCodePoint(int i) {
        this.toStringCache = null;
        super.appendCodePoint(i);
        return this;
    }

    public synchronized StringBuffer append(long j) {
        this.toStringCache = null;
        super.append(j);
        return this;
    }

    public synchronized StringBuffer append(float f) {
        this.toStringCache = null;
        super.append(f);
        return this;
    }

    public synchronized StringBuffer append(double d) {
        this.toStringCache = null;
        super.append(d);
        return this;
    }

    public synchronized StringBuffer delete(int i, int i2) {
        this.toStringCache = null;
        super.delete(i, i2);
        return this;
    }

    public synchronized StringBuffer deleteCharAt(int i) {
        this.toStringCache = null;
        super.deleteCharAt(i);
        return this;
    }

    public synchronized StringBuffer replace(int i, int i2, String str) {
        this.toStringCache = null;
        super.replace(i, i2, str);
        return this;
    }

    public synchronized String substring(int i) {
        return substring(i, this.count);
    }

    public synchronized CharSequence subSequence(int i, int i2) {
        return super.substring(i, i2);
    }

    public synchronized String substring(int i, int i2) {
        return super.substring(i, i2);
    }

    public synchronized StringBuffer insert(int i, char[] cArr, int i2, int i3) {
        this.toStringCache = null;
        super.insert(i, cArr, i2, i3);
        return this;
    }

    public synchronized StringBuffer insert(int i, Object obj) {
        this.toStringCache = null;
        super.insert(i, String.valueOf(obj));
        return this;
    }

    public synchronized StringBuffer insert(int i, String str) {
        this.toStringCache = null;
        super.insert(i, str);
        return this;
    }

    public synchronized StringBuffer insert(int i, char[] cArr) {
        this.toStringCache = null;
        super.insert(i, cArr);
        return this;
    }

    public StringBuffer insert(int i, CharSequence charSequence) {
        super.insert(i, charSequence);
        return this;
    }

    public synchronized StringBuffer insert(int i, CharSequence charSequence, int i2, int i3) {
        this.toStringCache = null;
        super.insert(i, charSequence, i2, i3);
        return this;
    }

    public StringBuffer insert(int i, boolean z) {
        super.insert(i, z);
        return this;
    }

    public synchronized StringBuffer insert(int i, char c) {
        this.toStringCache = null;
        super.insert(i, c);
        return this;
    }

    public StringBuffer insert(int i, int i2) {
        super.insert(i, i2);
        return this;
    }

    public StringBuffer insert(int i, long j) {
        super.insert(i, j);
        return this;
    }

    public StringBuffer insert(int i, float f) {
        super.insert(i, f);
        return this;
    }

    public StringBuffer insert(int i, double d) {
        super.insert(i, d);
        return this;
    }

    public int indexOf(String str) {
        return super.indexOf(str);
    }

    public synchronized int indexOf(String str, int i) {
        return super.indexOf(str, i);
    }

    public int lastIndexOf(String str) {
        return lastIndexOf(str, this.count);
    }

    public synchronized int lastIndexOf(String str, int i) {
        return super.lastIndexOf(str, i);
    }

    public synchronized StringBuffer reverse() {
        this.toStringCache = null;
        super.reverse();
        return this;
    }

    public synchronized String toString() {
        if (this.toStringCache == null) {
            this.toStringCache = Arrays.copyOfRange(this.value, 0, this.count);
        }
        return new String(this.toStringCache, 0, this.count);
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ObjectOutputStream.PutField putFields = objectOutputStream.putFields();
        putFields.put("value", (Object) this.value);
        putFields.put("count", this.count);
        putFields.put("shared", false);
        objectOutputStream.writeFields();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField readFields = objectInputStream.readFields();
        this.value = (char[]) readFields.get("value", (Object) null);
        this.count = readFields.get("count", 0);
    }
}
