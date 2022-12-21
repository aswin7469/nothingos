package sun.security.util;

import java.math.BigInteger;
import java.p026io.DataInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.util.Date;
import java.util.Vector;

public class DerInputStream {
    DerInputBuffer buffer;
    public byte tag;

    public DerInputStream(byte[] bArr) throws IOException {
        init(bArr, 0, bArr.length, true);
    }

    public DerInputStream(byte[] bArr, int i, int i2) throws IOException {
        init(bArr, i, i2, true);
    }

    public DerInputStream(byte[] bArr, int i, int i2, boolean z) throws IOException {
        init(bArr, i, i2, z);
    }

    private void init(byte[] bArr, int i, int i2, boolean z) throws IOException {
        if (i + 2 > bArr.length || i + i2 > bArr.length) {
            throw new IOException("Encoding bytes too short");
        }
        if (!DerIndefLenConverter.isIndefinite(bArr[i + 1])) {
            this.buffer = new DerInputBuffer(bArr, i, i2);
        } else if (z) {
            byte[] bArr2 = new byte[i2];
            System.arraycopy((Object) bArr, i, (Object) bArr2, 0, i2);
            this.buffer = new DerInputBuffer(new DerIndefLenConverter().convert(bArr2));
        } else {
            throw new IOException("Indefinite length BER encoding found");
        }
        this.buffer.mark(Integer.MAX_VALUE);
    }

    DerInputStream(DerInputBuffer derInputBuffer) {
        this.buffer = derInputBuffer;
        derInputBuffer.mark(Integer.MAX_VALUE);
    }

    public DerInputStream subStream(int i, boolean z) throws IOException {
        DerInputBuffer dup = this.buffer.dup();
        dup.truncate(i);
        if (z) {
            this.buffer.skip((long) i);
        }
        return new DerInputStream(dup);
    }

    public byte[] toByteArray() {
        return this.buffer.toByteArray();
    }

    public int getInteger() throws IOException {
        if (this.buffer.read() == 2) {
            DerInputBuffer derInputBuffer = this.buffer;
            return derInputBuffer.getInteger(getLength(derInputBuffer));
        }
        throw new IOException("DER input, Integer tag error");
    }

    public BigInteger getBigInteger() throws IOException {
        if (this.buffer.read() == 2) {
            DerInputBuffer derInputBuffer = this.buffer;
            return derInputBuffer.getBigInteger(getLength(derInputBuffer), false);
        }
        throw new IOException("DER input, Integer tag error");
    }

    public BigInteger getPositiveBigInteger() throws IOException {
        if (this.buffer.read() == 2) {
            DerInputBuffer derInputBuffer = this.buffer;
            return derInputBuffer.getBigInteger(getLength(derInputBuffer), true);
        }
        throw new IOException("DER input, Integer tag error");
    }

    public int getEnumerated() throws IOException {
        if (this.buffer.read() == 10) {
            DerInputBuffer derInputBuffer = this.buffer;
            return derInputBuffer.getInteger(getLength(derInputBuffer));
        }
        throw new IOException("DER input, Enumerated tag error");
    }

    public byte[] getBitString() throws IOException {
        if (this.buffer.read() == 3) {
            DerInputBuffer derInputBuffer = this.buffer;
            return derInputBuffer.getBitString(getLength(derInputBuffer));
        }
        throw new IOException("DER input not an bit string");
    }

    public BitArray getUnalignedBitString() throws IOException {
        if (this.buffer.read() == 3) {
            int length = getLength(this.buffer) - 1;
            int read = this.buffer.read();
            if (read >= 0) {
                int i = (length * 8) - read;
                if (i >= 0) {
                    byte[] bArr = new byte[length];
                    if (length == 0 || this.buffer.read(bArr) == length) {
                        return new BitArray(i, bArr);
                    }
                    throw new IOException("Short read of DER bit string");
                }
                throw new IOException("Valid bits of bit string invalid");
            }
            throw new IOException("Unused bits of bit string invalid");
        }
        throw new IOException("DER input not a bit string");
    }

    public byte[] getOctetString() throws IOException {
        if (this.buffer.read() == 4) {
            int length = getLength(this.buffer);
            byte[] bArr = new byte[length];
            if (length == 0 || this.buffer.read(bArr) == length) {
                return bArr;
            }
            throw new IOException("Short read of DER octet string");
        }
        throw new IOException("DER input not an octet string");
    }

    public void getBytes(byte[] bArr) throws IOException {
        if (bArr.length != 0 && this.buffer.read(bArr) != bArr.length) {
            throw new IOException("Short read of DER octet string");
        }
    }

    public void getNull() throws IOException {
        if (this.buffer.read() != 5 || this.buffer.read() != 0) {
            throw new IOException("getNull, bad data");
        }
    }

    public ObjectIdentifier getOID() throws IOException {
        return new ObjectIdentifier(this);
    }

    public DerValue[] getSequence(int i, boolean z) throws IOException {
        byte read = (byte) this.buffer.read();
        this.tag = read;
        if (read == 48) {
            return readVector(i, z);
        }
        throw new IOException("Sequence tag error");
    }

    public DerValue[] getSequence(int i) throws IOException {
        return getSequence(i, false);
    }

    public DerValue[] getSet(int i) throws IOException {
        byte read = (byte) this.buffer.read();
        this.tag = read;
        if (read == 49) {
            return readVector(i);
        }
        throw new IOException("Set tag error");
    }

    public DerValue[] getSet(int i, boolean z) throws IOException {
        return getSet(i, z, false);
    }

    public DerValue[] getSet(int i, boolean z, boolean z2) throws IOException {
        byte read = (byte) this.buffer.read();
        this.tag = read;
        if (z || read == 49) {
            return readVector(i, z2);
        }
        throw new IOException("Set tag error");
    }

    /* access modifiers changed from: protected */
    public DerValue[] readVector(int i) throws IOException {
        return readVector(i, false);
    }

    /* access modifiers changed from: protected */
    public DerValue[] readVector(int i, boolean z) throws IOException {
        byte read = (byte) this.buffer.read();
        int length = getLength(read, this.buffer);
        if (length == -1) {
            int available = this.buffer.available();
            byte[] bArr = new byte[(available + 2)];
            bArr[0] = this.tag;
            bArr[1] = read;
            DataInputStream dataInputStream = new DataInputStream(this.buffer);
            dataInputStream.readFully(bArr, 2, available);
            dataInputStream.close();
            DerInputBuffer derInputBuffer = new DerInputBuffer(new DerIndefLenConverter().convert(bArr));
            this.buffer = derInputBuffer;
            if (this.tag == derInputBuffer.read()) {
                length = getLength(this.buffer);
            } else {
                throw new IOException("Indefinite length encoding not supported");
            }
        }
        if (length == 0) {
            return new DerValue[0];
        }
        if (this.buffer.available() != length) {
            this = subStream(length, true);
        }
        Vector vector = new Vector(i);
        do {
            vector.addElement(new DerValue(this.buffer, z));
        } while (this.available() > 0);
        if (this.available() == 0) {
            int size = vector.size();
            DerValue[] derValueArr = new DerValue[size];
            for (int i2 = 0; i2 < size; i2++) {
                derValueArr[i2] = (DerValue) vector.elementAt(i2);
            }
            return derValueArr;
        }
        throw new IOException("Extra data at end of vector");
    }

    public DerValue getDerValue() throws IOException {
        return new DerValue((InputStream) this.buffer);
    }

    public String getUTF8String() throws IOException {
        return readString((byte) 12, "UTF-8", "UTF8");
    }

    public String getPrintableString() throws IOException {
        return readString((byte) 19, "Printable", "ASCII");
    }

    public String getT61String() throws IOException {
        return readString((byte) 20, "T61", "ISO-8859-1");
    }

    public String getIA5String() throws IOException {
        return readString((byte) 22, "IA5", "ASCII");
    }

    public String getBMPString() throws IOException {
        return readString((byte) 30, "BMP", "UnicodeBigUnmarked");
    }

    public String getGeneralString() throws IOException {
        return readString((byte) 27, "General", "ASCII");
    }

    private String readString(byte b, String str, String str2) throws IOException {
        if (this.buffer.read() == b) {
            int length = getLength(this.buffer);
            byte[] bArr = new byte[length];
            if (length == 0 || this.buffer.read(bArr) == length) {
                return new String(bArr, str2);
            }
            throw new IOException("Short read of DER " + str + " string");
        }
        throw new IOException("DER input not a " + str + " string");
    }

    public Date getUTCTime() throws IOException {
        if (this.buffer.read() == 23) {
            DerInputBuffer derInputBuffer = this.buffer;
            return derInputBuffer.getUTCTime(getLength(derInputBuffer));
        }
        throw new IOException("DER input, UTCtime tag invalid ");
    }

    public Date getGeneralizedTime() throws IOException {
        if (this.buffer.read() == 24) {
            DerInputBuffer derInputBuffer = this.buffer;
            return derInputBuffer.getGeneralizedTime(getLength(derInputBuffer));
        }
        throw new IOException("DER input, GeneralizedTime tag invalid ");
    }

    /* access modifiers changed from: package-private */
    public int getByte() throws IOException {
        return this.buffer.read() & 255;
    }

    public int peekByte() throws IOException {
        return this.buffer.peek();
    }

    /* access modifiers changed from: package-private */
    public int getLength() throws IOException {
        return getLength(this.buffer);
    }

    static int getLength(InputStream inputStream) throws IOException {
        return getLength(inputStream.read(), inputStream);
    }

    static int getLength(int i, InputStream inputStream) throws IOException {
        if (i == -1) {
            throw new IOException("Short read of DER length");
        } else if ((i & 128) == 0) {
            return i;
        } else {
            int i2 = i & 127;
            if (i2 == 0) {
                return -1;
            }
            if (i2 < 0 || i2 > 4) {
                StringBuilder sb = new StringBuilder("DerInputStream.getLength(): lengthTag=");
                sb.append(i2);
                sb.append(", ");
                sb.append(i2 < 0 ? "incorrect DER encoding." : "too big.");
                throw new IOException(sb.toString());
            }
            int read = inputStream.read() & 255;
            int i3 = i2 - 1;
            if (read != 0) {
                while (true) {
                    int i4 = i3 - 1;
                    if (i3 <= 0) {
                        break;
                    }
                    read = (inputStream.read() & 255) + (read << 8);
                    i3 = i4;
                }
                if (read < 0) {
                    throw new IOException("DerInputStream.getLength(): Invalid length bytes");
                } else if (read > 127) {
                    return read;
                } else {
                    throw new IOException("DerInputStream.getLength(): Should use short form for length");
                }
            } else {
                throw new IOException("DerInputStream.getLength(): Redundant length bytes found");
            }
        }
    }

    public void mark(int i) {
        this.buffer.mark(i);
    }

    public void reset() {
        this.buffer.reset();
    }

    public int available() {
        return this.buffer.available();
    }
}
