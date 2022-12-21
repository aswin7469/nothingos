package sun.security.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.Arrays;

public final class ObjectIdentifier implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = 8697030238860181294L;
    private int componentLen = -1;
    private Object components = null;
    private transient boolean componentsCalculated = false;
    private byte[] encoding = null;
    private volatile transient String stringForm;

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.encoding == null) {
            init((int[]) this.components, this.componentLen);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        if (!this.componentsCalculated) {
            int[] intArray = toIntArray();
            if (intArray != null) {
                this.components = intArray;
                this.componentLen = intArray.length;
            } else {
                this.components = HugeOidNotSupportedByOldJDK.theOne;
            }
            this.componentsCalculated = true;
        }
        objectOutputStream.defaultWriteObject();
    }

    static class HugeOidNotSupportedByOldJDK implements Serializable {
        private static final long serialVersionUID = 1;
        static HugeOidNotSupportedByOldJDK theOne = new HugeOidNotSupportedByOldJDK();

        HugeOidNotSupportedByOldJDK() {
        }
    }

    public ObjectIdentifier(String str) throws IOException {
        int indexOf;
        int i;
        String str2;
        int i2;
        int i3;
        byte[] bArr = new byte[str.length()];
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        do {
            try {
                indexOf = str.indexOf(46, i4);
                if (indexOf == -1) {
                    str2 = str.substring(i4);
                    i = str.length() - i4;
                } else {
                    str2 = str.substring(i4, indexOf);
                    i = indexOf - i4;
                }
                if (i > 9) {
                    BigInteger bigInteger = new BigInteger(str2);
                    if (i5 == 0) {
                        checkFirstComponent(bigInteger);
                        i3 = bigInteger.intValue();
                    } else {
                        if (i5 == 1) {
                            checkSecondComponent(i6, bigInteger);
                            bigInteger = bigInteger.add(BigInteger.valueOf((long) (i6 * 40)));
                        } else {
                            checkOtherComponent(i5, bigInteger);
                        }
                        i2 = pack7Oid(bigInteger, bArr, i7);
                        i7 += i2;
                        i4 = indexOf + 1;
                        i5++;
                    }
                } else {
                    i3 = Integer.parseInt(str2);
                    if (i5 == 0) {
                        checkFirstComponent(i3);
                    } else {
                        if (i5 == 1) {
                            checkSecondComponent(i6, i3);
                            i3 += i6 * 40;
                        } else {
                            checkOtherComponent(i5, i3);
                        }
                        i2 = pack7Oid(i3, bArr, i7);
                        i7 += i2;
                        i4 = indexOf + 1;
                        i5++;
                    }
                }
                i6 = i3;
                i4 = indexOf + 1;
                i5++;
            } catch (IOException e) {
                throw e;
            } catch (Exception e2) {
                throw new IOException("ObjectIdentifier() -- Invalid format: " + e2.toString(), e2);
            }
        } while (indexOf != -1);
        checkCount(i5);
        byte[] bArr2 = new byte[i7];
        this.encoding = bArr2;
        System.arraycopy((Object) bArr, 0, (Object) bArr2, 0, i7);
        this.stringForm = str;
    }

    public ObjectIdentifier(int[] iArr) throws IOException {
        checkCount(iArr.length);
        checkFirstComponent(iArr[0]);
        checkSecondComponent(iArr[0], iArr[1]);
        for (int i = 2; i < iArr.length; i++) {
            checkOtherComponent(i, iArr[i]);
        }
        init(iArr, iArr.length);
    }

    public ObjectIdentifier(DerInputStream derInputStream) throws IOException {
        byte b = (byte) derInputStream.getByte();
        if (b == 6) {
            int length = derInputStream.getLength();
            if (length <= derInputStream.available()) {
                byte[] bArr = new byte[length];
                this.encoding = bArr;
                derInputStream.getBytes(bArr);
                check(this.encoding);
                return;
            }
            throw new IOException("ObjectIdentifier() -- length exceedsdata available.  Length: " + length + ", Available: " + derInputStream.available());
        }
        throw new IOException("ObjectIdentifier() -- data isn't an object ID (tag = " + b + NavigationBarInflaterView.KEY_CODE_END);
    }

    ObjectIdentifier(DerInputBuffer derInputBuffer) throws IOException {
        DerInputStream derInputStream = new DerInputStream(derInputBuffer);
        byte[] bArr = new byte[derInputStream.available()];
        this.encoding = bArr;
        derInputStream.getBytes(bArr);
        check(this.encoding);
    }

    private void init(int[] iArr, int i) {
        int i2;
        byte[] bArr = new byte[((i * 5) + 1)];
        int i3 = iArr[1];
        int i4 = iArr[0];
        if (i3 < Integer.MAX_VALUE - (i4 * 40)) {
            i2 = pack7Oid((i4 * 40) + i3, bArr, 0);
        } else {
            i2 = pack7Oid(BigInteger.valueOf((long) i3).add(BigInteger.valueOf((long) (iArr[0] * 40))), bArr, 0);
        }
        int i5 = i2 + 0;
        for (int i6 = 2; i6 < i; i6++) {
            i5 += pack7Oid(iArr[i6], bArr, i5);
        }
        byte[] bArr2 = new byte[i5];
        this.encoding = bArr2;
        System.arraycopy((Object) bArr, 0, (Object) bArr2, 0, i5);
    }

    public static ObjectIdentifier newInternal(int[] iArr) {
        try {
            return new ObjectIdentifier(iArr);
        } catch (IOException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    /* access modifiers changed from: package-private */
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.write((byte) 6, this.encoding);
    }

    @Deprecated
    public boolean equals(ObjectIdentifier objectIdentifier) {
        return equals((Object) objectIdentifier);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ObjectIdentifier)) {
            return false;
        }
        return Arrays.equals(this.encoding, ((ObjectIdentifier) obj).encoding);
    }

    public int hashCode() {
        return Arrays.hashCode(this.encoding);
    }

    public int[] toIntArray() {
        int i;
        int length = this.encoding.length;
        int[] iArr = new int[20];
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            if ((this.encoding[i4] & 128) == 0) {
                int i5 = (i4 - i3) + 1;
                if (i5 > 4) {
                    BigInteger bigInteger = new BigInteger(pack(this.encoding, i3, i5, 7, 8));
                    if (i3 == 0) {
                        int i6 = i2 + 1;
                        iArr[i2] = 2;
                        BigInteger subtract = bigInteger.subtract(BigInteger.valueOf(80));
                        if (subtract.compareTo(BigInteger.valueOf(2147483647L)) == 1) {
                            return null;
                        }
                        i = i6 + 1;
                        iArr[i6] = subtract.intValue();
                    } else if (bigInteger.compareTo(BigInteger.valueOf(2147483647L)) == 1) {
                        return null;
                    } else {
                        i = i2 + 1;
                        iArr[i2] = bigInteger.intValue();
                    }
                    i2 = i;
                } else {
                    int i7 = 0;
                    for (int i8 = i3; i8 <= i4; i8++) {
                        i7 = (i7 << 7) | (this.encoding[i8] & Byte.MAX_VALUE);
                    }
                    if (i3 != 0) {
                        iArr[i2] = i7;
                        i2++;
                    } else if (i7 < 80) {
                        int i9 = i2 + 1;
                        iArr[i2] = i7 / 40;
                        i2 = i9 + 1;
                        iArr[i9] = i7 % 40;
                    } else {
                        int i10 = i2 + 1;
                        iArr[i2] = 2;
                        i2 = i10 + 1;
                        iArr[i10] = i7 - 80;
                    }
                }
                i3 = i4 + 1;
            }
            if (i2 >= iArr.length) {
                iArr = Arrays.copyOf(iArr, i2 + 10);
            }
        }
        return Arrays.copyOf(iArr, i2);
    }

    public String toString() {
        String str = this.stringForm;
        if (str != null) {
            return str;
        }
        int length = this.encoding.length;
        StringBuffer stringBuffer = new StringBuffer(length * 4);
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if ((this.encoding[i2] & 128) == 0) {
                if (i != 0) {
                    stringBuffer.append('.');
                }
                int i3 = (i2 - i) + 1;
                if (i3 > 4) {
                    BigInteger bigInteger = new BigInteger(pack(this.encoding, i, i3, 7, 8));
                    if (i == 0) {
                        stringBuffer.append("2.");
                        stringBuffer.append((Object) bigInteger.subtract(BigInteger.valueOf(80)));
                    } else {
                        stringBuffer.append((Object) bigInteger);
                    }
                } else {
                    byte b = 0;
                    for (int i4 = i; i4 <= i2; i4++) {
                        b = (b << 7) | (this.encoding[i4] & Byte.MAX_VALUE);
                    }
                    if (i != 0) {
                        stringBuffer.append((int) b);
                    } else if (b < 80) {
                        stringBuffer.append(b / 40);
                        stringBuffer.append('.');
                        stringBuffer.append(b % 40);
                    } else {
                        stringBuffer.append("2.");
                        stringBuffer.append(b - 80);
                    }
                }
                i = i2 + 1;
            }
        }
        String stringBuffer2 = stringBuffer.toString();
        this.stringForm = stringBuffer2;
        return stringBuffer2;
    }

    private static byte[] pack(byte[] bArr, int i, int i2, int i3, int i4) {
        if (i3 == i4) {
            return (byte[]) bArr.clone();
        }
        int i5 = i2 * i3;
        int i6 = ((i5 + i4) - 1) / i4;
        byte[] bArr2 = new byte[i6];
        int i7 = (i6 * i4) - i5;
        int i8 = 0;
        while (i8 < i5) {
            int i9 = i3 - (i8 % i3);
            int i10 = i4 - (i7 % i4);
            int i11 = i9 > i10 ? i10 : i9;
            int i12 = i7 / i4;
            bArr2[i12] = (byte) (((((bArr[(i8 / i3) + i] + 256) >> (i9 - i11)) & ((1 << i11) - 1)) << (i10 - i11)) | bArr2[i12]);
            i8 += i11;
            i7 += i11;
        }
        return bArr2;
    }

    private static int pack7Oid(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        byte[] pack = pack(bArr, i, i2, 8, 7);
        int length = pack.length - 1;
        for (int length2 = pack.length - 2; length2 >= 0; length2--) {
            byte b = pack[length2];
            if (b != 0) {
                length = length2;
            }
            pack[length2] = (byte) (b | 128);
        }
        System.arraycopy((Object) pack, length, (Object) bArr2, i3, pack.length - length);
        return pack.length - length;
    }

    private static int pack8(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        byte[] pack = pack(bArr, i, i2, 7, 8);
        int length = pack.length - 1;
        for (int length2 = pack.length - 2; length2 >= 0; length2--) {
            if (pack[length2] != 0) {
                length = length2;
            }
        }
        System.arraycopy((Object) pack, length, (Object) bArr2, i3, pack.length - length);
        return pack.length - length;
    }

    private static int pack7Oid(int i, byte[] bArr, int i2) {
        return pack7Oid(new byte[]{(byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i}, 0, 4, bArr, i2);
    }

    private static int pack7Oid(BigInteger bigInteger, byte[] bArr, int i) {
        byte[] byteArray = bigInteger.toByteArray();
        return pack7Oid(byteArray, 0, byteArray.length, bArr, i);
    }

    private static void check(byte[] bArr) throws IOException {
        int length = bArr.length;
        if (length < 1 || (bArr[length - 1] & 128) != 0) {
            throw new IOException("ObjectIdentifier() -- Invalid DER encoding, not ended");
        }
        for (int i = 0; i < length; i++) {
            if (bArr[i] == Byte.MIN_VALUE && (i == 0 || (bArr[i - 1] & 128) == 0)) {
                throw new IOException("ObjectIdentifier() -- Invalid DER encoding, useless extra octet detected");
            }
        }
    }

    private static void checkCount(int i) throws IOException {
        if (i < 2) {
            throw new IOException("ObjectIdentifier() -- Must be at least two oid components ");
        }
    }

    private static void checkFirstComponent(int i) throws IOException {
        if (i < 0 || i > 2) {
            throw new IOException("ObjectIdentifier() -- First oid component is invalid ");
        }
    }

    private static void checkFirstComponent(BigInteger bigInteger) throws IOException {
        if (bigInteger.signum() == -1 || bigInteger.compareTo(BigInteger.valueOf(2)) == 1) {
            throw new IOException("ObjectIdentifier() -- First oid component is invalid ");
        }
    }

    private static void checkSecondComponent(int i, int i2) throws IOException {
        if (i2 < 0 || (i != 2 && i2 > 39)) {
            throw new IOException("ObjectIdentifier() -- Second oid component is invalid ");
        }
    }

    private static void checkSecondComponent(int i, BigInteger bigInteger) throws IOException {
        if (bigInteger.signum() == -1 || (i != 2 && bigInteger.compareTo(BigInteger.valueOf(39)) == 1)) {
            throw new IOException("ObjectIdentifier() -- Second oid component is invalid ");
        }
    }

    private static void checkOtherComponent(int i, int i2) throws IOException {
        if (i2 < 0) {
            throw new IOException("ObjectIdentifier() -- oid component #" + (i + 1) + " must be non-negative ");
        }
    }

    private static void checkOtherComponent(int i, BigInteger bigInteger) throws IOException {
        if (bigInteger.signum() == -1) {
            throw new IOException("ObjectIdentifier() -- oid component #" + (i + 1) + " must be non-negative ");
        }
    }
}
