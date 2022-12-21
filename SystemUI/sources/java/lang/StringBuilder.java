package java.lang;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;

public final class StringBuilder extends AbstractStringBuilder implements Serializable, CharSequence {
    static final long serialVersionUID = 4383685877147921099L;

    public /* bridge */ /* synthetic */ int capacity() {
        return super.capacity();
    }

    public /* bridge */ /* synthetic */ char charAt(int i) {
        return super.charAt(i);
    }

    public /* bridge */ /* synthetic */ int codePointAt(int i) {
        return super.codePointAt(i);
    }

    public /* bridge */ /* synthetic */ int codePointBefore(int i) {
        return super.codePointBefore(i);
    }

    public /* bridge */ /* synthetic */ int codePointCount(int i, int i2) {
        return super.codePointCount(i, i2);
    }

    public /* bridge */ /* synthetic */ void ensureCapacity(int i) {
        super.ensureCapacity(i);
    }

    public /* bridge */ /* synthetic */ void getChars(int i, int i2, char[] cArr, int i3) {
        super.getChars(i, i2, cArr, i3);
    }

    public /* bridge */ /* synthetic */ int length() {
        return super.length();
    }

    public /* bridge */ /* synthetic */ int offsetByCodePoints(int i, int i2) {
        return super.offsetByCodePoints(i, i2);
    }

    public /* bridge */ /* synthetic */ void setCharAt(int i, char c) {
        super.setCharAt(i, c);
    }

    public /* bridge */ /* synthetic */ void setLength(int i) {
        super.setLength(i);
    }

    public /* bridge */ /* synthetic */ CharSequence subSequence(int i, int i2) {
        return super.subSequence(i, i2);
    }

    public /* bridge */ /* synthetic */ String substring(int i) {
        return super.substring(i);
    }

    public /* bridge */ /* synthetic */ String substring(int i, int i2) {
        return super.substring(i, i2);
    }

    public /* bridge */ /* synthetic */ void trimToSize() {
        super.trimToSize();
    }

    public StringBuilder() {
        super(16);
    }

    public StringBuilder(int i) {
        super(i);
    }

    public StringBuilder(String str) {
        super(str.length() + 16);
        append(str);
    }

    public StringBuilder(CharSequence charSequence) {
        this(charSequence.length() + 16);
        append(charSequence);
    }

    public StringBuilder append(Object obj) {
        append(String.valueOf(obj));
        return this;
    }

    public StringBuilder append(String str) {
        super.append(str);
        return this;
    }

    public StringBuilder append(StringBuffer stringBuffer) {
        super.append(stringBuffer);
        return this;
    }

    public StringBuilder append(CharSequence charSequence) {
        super.append(charSequence);
        return this;
    }

    public StringBuilder append(CharSequence charSequence, int i, int i2) {
        super.append(charSequence, i, i2);
        return this;
    }

    public StringBuilder append(char[] cArr) {
        super.append(cArr);
        return this;
    }

    public StringBuilder append(char[] cArr, int i, int i2) {
        super.append(cArr, i, i2);
        return this;
    }

    public StringBuilder append(boolean z) {
        super.append(z);
        return this;
    }

    public StringBuilder append(char c) {
        super.append(c);
        return this;
    }

    public StringBuilder append(int i) {
        super.append(i);
        return this;
    }

    public StringBuilder append(long j) {
        super.append(j);
        return this;
    }

    public StringBuilder append(float f) {
        super.append(f);
        return this;
    }

    public StringBuilder append(double d) {
        super.append(d);
        return this;
    }

    public StringBuilder appendCodePoint(int i) {
        super.appendCodePoint(i);
        return this;
    }

    public StringBuilder delete(int i, int i2) {
        super.delete(i, i2);
        return this;
    }

    public StringBuilder deleteCharAt(int i) {
        super.deleteCharAt(i);
        return this;
    }

    public StringBuilder replace(int i, int i2, String str) {
        super.replace(i, i2, str);
        return this;
    }

    public StringBuilder insert(int i, char[] cArr, int i2, int i3) {
        super.insert(i, cArr, i2, i3);
        return this;
    }

    public StringBuilder insert(int i, Object obj) {
        super.insert(i, obj);
        return this;
    }

    public StringBuilder insert(int i, String str) {
        super.insert(i, str);
        return this;
    }

    public StringBuilder insert(int i, char[] cArr) {
        super.insert(i, cArr);
        return this;
    }

    public StringBuilder insert(int i, CharSequence charSequence) {
        super.insert(i, charSequence);
        return this;
    }

    public StringBuilder insert(int i, CharSequence charSequence, int i2, int i3) {
        super.insert(i, charSequence, i2, i3);
        return this;
    }

    public StringBuilder insert(int i, boolean z) {
        super.insert(i, z);
        return this;
    }

    public StringBuilder insert(int i, char c) {
        super.insert(i, c);
        return this;
    }

    public StringBuilder insert(int i, int i2) {
        super.insert(i, i2);
        return this;
    }

    public StringBuilder insert(int i, long j) {
        super.insert(i, j);
        return this;
    }

    public StringBuilder insert(int i, float f) {
        super.insert(i, f);
        return this;
    }

    public StringBuilder insert(int i, double d) {
        super.insert(i, d);
        return this;
    }

    public int indexOf(String str) {
        return super.indexOf(str);
    }

    public int indexOf(String str, int i) {
        return super.indexOf(str, i);
    }

    public int lastIndexOf(String str) {
        return super.lastIndexOf(str);
    }

    public int lastIndexOf(String str, int i) {
        return super.lastIndexOf(str, i);
    }

    public StringBuilder reverse() {
        super.reverse();
        return this;
    }

    public String toString() {
        if (this.count == 0) {
            return "";
        }
        return new String(this.value, 0, this.count);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.count);
        objectOutputStream.writeObject(this.value);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.count = objectInputStream.readInt();
        this.value = (char[]) objectInputStream.readObject();
    }
}
