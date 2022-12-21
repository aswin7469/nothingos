package sun.security.util;

import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.p026io.ByteArrayInputStream;
import java.p026io.DataInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.util.Date;
import sun.misc.IOUtils;

public class DerValue {
    public static final byte TAG_APPLICATION = 64;
    public static final byte TAG_CONTEXT = Byte.MIN_VALUE;
    public static final byte TAG_PRIVATE = -64;
    public static final byte TAG_UNIVERSAL = 0;
    public static final byte tag_BMPString = 30;
    public static final byte tag_BitString = 3;
    public static final byte tag_Boolean = 1;
    public static final byte tag_Enumerated = 10;
    public static final byte tag_GeneralString = 27;
    public static final byte tag_GeneralizedTime = 24;
    public static final byte tag_IA5String = 22;
    public static final byte tag_Integer = 2;
    public static final byte tag_Null = 5;
    public static final byte tag_ObjectId = 6;
    public static final byte tag_OctetString = 4;
    public static final byte tag_PrintableString = 19;
    public static final byte tag_Sequence = 48;
    public static final byte tag_SequenceOf = 48;
    public static final byte tag_Set = 49;
    public static final byte tag_SetOf = 49;
    public static final byte tag_T61String = 20;
    public static final byte tag_UTF8String = 12;
    public static final byte tag_UniversalString = 28;
    public static final byte tag_UtcTime = 23;
    protected DerInputBuffer buffer;
    public final DerInputStream data;
    private int length;
    private byte[] originalEncodedForm;
    public byte tag;

    public static byte createTag(byte b, boolean z, byte b2) {
        byte b3 = (byte) (b | b2);
        return z ? (byte) (b3 | NetworkStackConstants.TCPHDR_URG) : b3;
    }

    public static boolean isPrintableStringChar(char c) {
        if ((c < 'a' || c > 'z') && ((c < 'A' || c > 'Z') && !((c >= '0' && c <= '9') || c == ' ' || c == ':' || c == '=' || c == '?'))) {
            switch (c) {
                case '\'':
                case '(':
                case ')':
                    break;
                default:
                    switch (c) {
                        case '+':
                        case ',':
                        case '-':
                        case '.':
                        case '/':
                            break;
                        default:
                            return false;
                    }
            }
        }
        return true;
    }

    public boolean isUniversal() {
        return (this.tag & TAG_PRIVATE) == 0;
    }

    public boolean isApplication() {
        return (this.tag & TAG_PRIVATE) == 64;
    }

    public boolean isContextSpecific() {
        return (this.tag & TAG_PRIVATE) == 128;
    }

    public boolean isContextSpecific(byte b) {
        if (isContextSpecific() && (this.tag & 31) == b) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isPrivate() {
        return (this.tag & TAG_PRIVATE) == 192;
    }

    public boolean isConstructed() {
        return (this.tag & NetworkStackConstants.TCPHDR_URG) == 32;
    }

    public boolean isConstructed(byte b) {
        if (isConstructed() && (this.tag & 31) == b) {
            return true;
        }
        return false;
    }

    public DerValue(String str) throws IOException {
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= str.length()) {
                z = true;
                break;
            } else if (!isPrintableStringChar(str.charAt(i))) {
                break;
            } else {
                i++;
            }
        }
        this.data = init(z ? (byte) 19 : 12, str);
    }

    public DerValue(byte b, String str) throws IOException {
        this.data = init(b, str);
    }

    public DerValue(byte b, byte[] bArr) {
        this.tag = b;
        DerInputBuffer derInputBuffer = new DerInputBuffer((byte[]) bArr.clone());
        this.buffer = derInputBuffer;
        this.length = bArr.length;
        DerInputStream derInputStream = new DerInputStream(derInputBuffer);
        this.data = derInputStream;
        derInputStream.mark(Integer.MAX_VALUE);
    }

    DerValue(DerInputBuffer derInputBuffer, boolean z) throws IOException {
        int pos = derInputBuffer.getPos();
        this.tag = (byte) derInputBuffer.read();
        byte read = (byte) derInputBuffer.read();
        int length2 = DerInputStream.getLength(read, derInputBuffer);
        this.length = length2;
        if (length2 == -1) {
            DerInputBuffer dup = derInputBuffer.dup();
            int available = dup.available();
            byte[] bArr = new byte[(available + 2)];
            bArr[0] = this.tag;
            bArr[1] = read;
            DataInputStream dataInputStream = new DataInputStream(dup);
            dataInputStream.readFully(bArr, 2, available);
            dataInputStream.close();
            DerInputBuffer derInputBuffer2 = new DerInputBuffer(new DerIndefLenConverter().convert(bArr));
            if (this.tag == derInputBuffer2.read()) {
                this.length = DerInputStream.getLength(derInputBuffer2);
                DerInputBuffer dup2 = derInputBuffer2.dup();
                this.buffer = dup2;
                dup2.truncate(this.length);
                this.data = new DerInputStream(this.buffer);
                derInputBuffer.skip((long) (this.length + 2));
            } else {
                throw new IOException("Indefinite length encoding not supported");
            }
        } else {
            DerInputBuffer dup3 = derInputBuffer.dup();
            this.buffer = dup3;
            dup3.truncate(this.length);
            this.data = new DerInputStream(this.buffer);
            derInputBuffer.skip((long) this.length);
        }
        if (z) {
            this.originalEncodedForm = derInputBuffer.getSlice(pos, derInputBuffer.getPos() - pos);
        }
    }

    public DerValue(byte[] bArr) throws IOException {
        this.data = init(true, (InputStream) new ByteArrayInputStream(bArr));
    }

    public DerValue(byte[] bArr, int i, int i2) throws IOException {
        this.data = init(true, (InputStream) new ByteArrayInputStream(bArr, i, i2));
    }

    public DerValue(InputStream inputStream) throws IOException {
        this.data = init(false, inputStream);
    }

    private DerInputStream init(byte b, String str) throws IOException {
        String str2;
        this.tag = b;
        if (b != 12) {
            if (!(b == 22 || b == 27)) {
                if (b == 30) {
                    str2 = "UnicodeBigUnmarked";
                } else if (b != 19) {
                    if (b == 20) {
                        str2 = "ISO-8859-1";
                    } else {
                        throw new IllegalArgumentException("Unsupported DER string type");
                    }
                }
            }
            str2 = "ASCII";
        } else {
            str2 = "UTF8";
        }
        byte[] bytes = str.getBytes(str2);
        this.length = bytes.length;
        DerInputBuffer derInputBuffer = new DerInputBuffer(bytes);
        this.buffer = derInputBuffer;
        DerInputStream derInputStream = new DerInputStream(derInputBuffer);
        derInputStream.mark(Integer.MAX_VALUE);
        return derInputStream;
    }

    private DerInputStream init(boolean z, InputStream inputStream) throws IOException {
        this.tag = (byte) inputStream.read();
        byte read = (byte) inputStream.read();
        int length2 = DerInputStream.getLength(read, inputStream);
        this.length = length2;
        if (length2 == -1) {
            int available = inputStream.available();
            byte[] bArr = new byte[(available + 2)];
            bArr[0] = this.tag;
            bArr[1] = read;
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            dataInputStream.readFully(bArr, 2, available);
            dataInputStream.close();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new DerIndefLenConverter().convert(bArr));
            if (this.tag == byteArrayInputStream.read()) {
                this.length = DerInputStream.getLength(byteArrayInputStream);
                inputStream = byteArrayInputStream;
            } else {
                throw new IOException("Indefinite length encoding not supported");
            }
        }
        if (!z || inputStream.available() == this.length) {
            DerInputBuffer derInputBuffer = new DerInputBuffer(IOUtils.readFully(inputStream, this.length, true));
            this.buffer = derInputBuffer;
            return new DerInputStream(derInputBuffer);
        }
        throw new IOException("extra data given to DerValue constructor");
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.write(this.tag);
        derOutputStream.putLength(this.length);
        int i = this.length;
        if (i > 0) {
            byte[] bArr = new byte[i];
            synchronized (this.data) {
                this.buffer.reset();
                if (this.buffer.read(bArr) == this.length) {
                    derOutputStream.write(bArr);
                } else {
                    throw new IOException("short DER value read (encode)");
                }
            }
        }
    }

    public final DerInputStream getData() {
        return this.data;
    }

    public final byte getTag() {
        return this.tag;
    }

    public boolean getBoolean() throws IOException {
        if (this.tag != 1) {
            throw new IOException("DerValue.getBoolean, not a BOOLEAN " + this.tag);
        } else if (this.length != 1) {
            throw new IOException("DerValue.getBoolean, invalid length " + this.length);
        } else if (this.buffer.read() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public ObjectIdentifier getOID() throws IOException {
        if (this.tag == 6) {
            return new ObjectIdentifier(this.buffer);
        }
        throw new IOException("DerValue.getOID, not an OID " + this.tag);
    }

    private byte[] append(byte[] bArr, byte[] bArr2) {
        if (bArr == null) {
            return bArr2;
        }
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy((Object) bArr, 0, (Object) bArr3, 0, bArr.length);
        System.arraycopy((Object) bArr2, 0, (Object) bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public byte[] getOctetString() throws IOException {
        if (this.tag == 4 || isConstructed((byte) 4)) {
            int i = this.length;
            byte[] bArr = new byte[i];
            if (i == 0) {
                return bArr;
            }
            if (this.buffer.read(bArr) == this.length) {
                if (isConstructed()) {
                    DerInputStream derInputStream = new DerInputStream(bArr);
                    bArr = null;
                    while (derInputStream.available() != 0) {
                        bArr = append(bArr, derInputStream.getOctetString());
                    }
                }
                return bArr;
            }
            throw new IOException("short read on DerValue buffer");
        }
        throw new IOException("DerValue.getOctetString, not an Octet String: " + this.tag);
    }

    public int getInteger() throws IOException {
        if (this.tag == 2) {
            return this.buffer.getInteger(this.data.available());
        }
        throw new IOException("DerValue.getInteger, not an int " + this.tag);
    }

    public BigInteger getBigInteger() throws IOException {
        if (this.tag == 2) {
            return this.buffer.getBigInteger(this.data.available(), false);
        }
        throw new IOException("DerValue.getBigInteger, not an int " + this.tag);
    }

    public BigInteger getPositiveBigInteger() throws IOException {
        if (this.tag == 2) {
            return this.buffer.getBigInteger(this.data.available(), true);
        }
        throw new IOException("DerValue.getBigInteger, not an int " + this.tag);
    }

    public int getEnumerated() throws IOException {
        if (this.tag == 10) {
            return this.buffer.getInteger(this.data.available());
        }
        throw new IOException("DerValue.getEnumerated, incorrect tag: " + this.tag);
    }

    public byte[] getBitString() throws IOException {
        if (this.tag == 3) {
            return this.buffer.getBitString();
        }
        throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
    }

    public BitArray getUnalignedBitString() throws IOException {
        if (this.tag == 3) {
            return this.buffer.getUnalignedBitString();
        }
        throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
    }

    public String getAsString() throws IOException {
        byte b = this.tag;
        if (b == 12) {
            return getUTF8String();
        }
        if (b == 19) {
            return getPrintableString();
        }
        if (b == 20) {
            return getT61String();
        }
        if (b == 22) {
            return getIA5String();
        }
        if (b == 30) {
            return getBMPString();
        }
        if (b == 27) {
            return getGeneralString();
        }
        return null;
    }

    public byte[] getBitString(boolean z) throws IOException {
        if (z || this.tag == 3) {
            return this.buffer.getBitString();
        }
        throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
    }

    public BitArray getUnalignedBitString(boolean z) throws IOException {
        if (z || this.tag == 3) {
            return this.buffer.getUnalignedBitString();
        }
        throw new IOException("DerValue.getBitString, not a bit string " + this.tag);
    }

    public byte[] getDataBytes() throws IOException {
        byte[] bArr = new byte[this.length];
        synchronized (this.data) {
            this.data.reset();
            this.data.getBytes(bArr);
        }
        return bArr;
    }

    public String getPrintableString() throws IOException {
        if (this.tag == 19) {
            return new String(getDataBytes(), "ASCII");
        }
        throw new IOException("DerValue.getPrintableString, not a string " + this.tag);
    }

    public String getT61String() throws IOException {
        if (this.tag == 20) {
            return new String(getDataBytes(), "ISO-8859-1");
        }
        throw new IOException("DerValue.getT61String, not T61 " + this.tag);
    }

    public String getIA5String() throws IOException {
        if (this.tag == 22) {
            return new String(getDataBytes(), "ASCII");
        }
        throw new IOException("DerValue.getIA5String, not IA5 " + this.tag);
    }

    public String getBMPString() throws IOException {
        if (this.tag == 30) {
            return new String(getDataBytes(), "UnicodeBigUnmarked");
        }
        throw new IOException("DerValue.getBMPString, not BMP " + this.tag);
    }

    public String getUTF8String() throws IOException {
        if (this.tag == 12) {
            return new String(getDataBytes(), "UTF8");
        }
        throw new IOException("DerValue.getUTF8String, not UTF-8 " + this.tag);
    }

    public String getGeneralString() throws IOException {
        if (this.tag == 27) {
            return new String(getDataBytes(), "ASCII");
        }
        throw new IOException("DerValue.getGeneralString, not GeneralString " + this.tag);
    }

    public Date getUTCTime() throws IOException {
        if (this.tag == 23) {
            return this.buffer.getUTCTime(this.data.available());
        }
        throw new IOException("DerValue.getUTCTime, not a UtcTime: " + this.tag);
    }

    public Date getGeneralizedTime() throws IOException {
        if (this.tag == 24) {
            return this.buffer.getGeneralizedTime(this.data.available());
        }
        throw new IOException("DerValue.getGeneralizedTime, not a GeneralizedTime: " + this.tag);
    }

    public boolean equals(Object obj) {
        if (obj instanceof DerValue) {
            return equals((DerValue) obj);
        }
        return false;
    }

    public boolean equals(DerValue derValue) {
        if (this == derValue) {
            return true;
        }
        if (this.tag != derValue.tag) {
            return false;
        }
        DerInputStream derInputStream = this.data;
        if (derInputStream == derValue.data) {
            return true;
        }
        if (System.identityHashCode(derInputStream) > System.identityHashCode(derValue.data)) {
            return doEquals(this, derValue);
        }
        return doEquals(derValue, this);
    }

    private static boolean doEquals(DerValue derValue, DerValue derValue2) {
        boolean equals;
        synchronized (derValue.data) {
            synchronized (derValue2.data) {
                derValue.data.reset();
                derValue2.data.reset();
                equals = derValue.buffer.equals(derValue2.buffer);
            }
        }
        return equals;
    }

    public String toString() {
        try {
            String asString = getAsString();
            if (asString != null) {
                return "\"" + asString + "\"";
            }
            byte b = this.tag;
            if (b == 5) {
                return "[DerValue, null]";
            }
            if (b == 6) {
                return "OID." + getOID();
            }
            return "[DerValue, tag = " + this.tag + ", length = " + this.length + NavigationBarInflaterView.SIZE_MOD_END;
        } catch (IOException unused) {
            throw new IllegalArgumentException("misformatted DER value");
        }
    }

    public byte[] getOriginalEncodedForm() {
        byte[] bArr = this.originalEncodedForm;
        if (bArr != null) {
            return (byte[]) bArr.clone();
        }
        return null;
    }

    public byte[] toByteArray() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        encode(derOutputStream);
        this.data.reset();
        return derOutputStream.toByteArray();
    }

    public DerInputStream toDerInputStream() throws IOException {
        byte b = this.tag;
        if (b == 48 || b == 49) {
            return new DerInputStream(this.buffer);
        }
        throw new IOException("toDerInputStream rejects tag type " + this.tag);
    }

    public int length() {
        return this.length;
    }

    public void resetTag(byte b) {
        this.tag = b;
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
