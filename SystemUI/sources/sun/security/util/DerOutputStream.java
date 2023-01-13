package sun.security.util;

import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import java.math.BigInteger;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DerOutputStream extends ByteArrayOutputStream implements DerEncoder {
    private static ByteArrayLexOrder lexOrder = new ByteArrayLexOrder();
    private static ByteArrayTagOrder tagOrder = new ByteArrayTagOrder();

    public DerOutputStream(int i) {
        super(i);
    }

    public DerOutputStream() {
    }

    public void write(byte b, byte[] bArr) throws IOException {
        write(b);
        putLength(bArr.length);
        write(bArr, 0, bArr.length);
    }

    public void write(byte b, DerOutputStream derOutputStream) throws IOException {
        write(b);
        putLength(derOutputStream.count);
        write(derOutputStream.buf, 0, derOutputStream.count);
    }

    public void writeImplicit(byte b, DerOutputStream derOutputStream) throws IOException {
        write(b);
        write(derOutputStream.buf, 1, derOutputStream.count - 1);
    }

    public void putDerValue(DerValue derValue) throws IOException {
        derValue.encode(this);
    }

    public void putBoolean(boolean z) throws IOException {
        write(1);
        putLength(1);
        if (z) {
            write(255);
        } else {
            write(0);
        }
    }

    public void putEnumerated(int i) throws IOException {
        write(10);
        putIntegerContents(i);
    }

    public void putInteger(BigInteger bigInteger) throws IOException {
        write(2);
        byte[] byteArray = bigInteger.toByteArray();
        putLength(byteArray.length);
        write(byteArray, 0, byteArray.length);
    }

    public void putInteger(Integer num) throws IOException {
        putInteger(num.intValue());
    }

    public void putInteger(int i) throws IOException {
        write(2);
        putIntegerContents(i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x0056 A[LOOP:2: B:19:0x0054->B:20:0x0056, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void putIntegerContents(int r8) throws java.p026io.IOException {
        /*
            r7 = this;
            r0 = 4
            byte[] r1 = new byte[r0]
            r2 = r8 & 255(0xff, float:3.57E-43)
            byte r2 = (byte) r2
            r3 = 3
            r1[r3] = r2
            r2 = 65280(0xff00, float:9.1477E-41)
            r2 = r2 & r8
            int r2 = r2 >>> 8
            byte r2 = (byte) r2
            r4 = 2
            r1[r4] = r2
            r2 = 16711680(0xff0000, float:2.3418052E-38)
            r2 = r2 & r8
            int r2 = r2 >>> 16
            byte r2 = (byte) r2
            r4 = 1
            r1[r4] = r2
            r2 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r8 = r8 & r2
            int r8 = r8 >>> 24
            byte r8 = (byte) r8
            r2 = 0
            r1[r2] = r8
            r4 = 128(0x80, float:1.794E-43)
            r5 = -1
            if (r8 != r5) goto L_0x003b
            r8 = r2
        L_0x002b:
            if (r2 >= r3) goto L_0x004e
            byte r6 = r1[r2]
            if (r6 != r5) goto L_0x004e
            int r2 = r2 + 1
            byte r6 = r1[r2]
            r6 = r6 & r4
            if (r6 != r4) goto L_0x004e
            int r8 = r8 + 1
            goto L_0x002b
        L_0x003b:
            if (r8 != 0) goto L_0x004f
            r8 = r2
        L_0x003e:
            if (r2 >= r3) goto L_0x004e
            byte r5 = r1[r2]
            if (r5 != 0) goto L_0x004e
            int r2 = r2 + 1
            byte r5 = r1[r2]
            r5 = r5 & r4
            if (r5 != 0) goto L_0x004e
            int r8 = r8 + 1
            goto L_0x003e
        L_0x004e:
            r2 = r8
        L_0x004f:
            int r8 = 4 - r2
            r7.putLength(r8)
        L_0x0054:
            if (r2 >= r0) goto L_0x005e
            byte r8 = r1[r2]
            r7.write(r8)
            int r2 = r2 + 1
            goto L_0x0054
        L_0x005e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.DerOutputStream.putIntegerContents(int):void");
    }

    public void putBitString(byte[] bArr) throws IOException {
        write(3);
        putLength(bArr.length + 1);
        write(0);
        write(bArr);
    }

    public void putUnalignedBitString(BitArray bitArray) throws IOException {
        byte[] byteArray = bitArray.toByteArray();
        write(3);
        putLength(byteArray.length + 1);
        write((byteArray.length * 8) - bitArray.length());
        write(byteArray);
    }

    public void putTruncatedUnalignedBitString(BitArray bitArray) throws IOException {
        putUnalignedBitString(bitArray.truncate());
    }

    public void putOctetString(byte[] bArr) throws IOException {
        write((byte) 4, bArr);
    }

    public void putNull() throws IOException {
        write(5);
        putLength(0);
    }

    public void putOID(ObjectIdentifier objectIdentifier) throws IOException {
        objectIdentifier.encode(this);
    }

    public void putSequence(DerValue[] derValueArr) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        for (DerValue encode : derValueArr) {
            encode.encode(derOutputStream);
        }
        write((byte) 48, derOutputStream);
    }

    public void putSet(DerValue[] derValueArr) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        for (DerValue encode : derValueArr) {
            encode.encode(derOutputStream);
        }
        write((byte) 49, derOutputStream);
    }

    public void putOrderedSetOf(byte b, DerEncoder[] derEncoderArr) throws IOException {
        putOrderedSet(b, derEncoderArr, lexOrder);
    }

    public void putOrderedSet(byte b, DerEncoder[] derEncoderArr) throws IOException {
        putOrderedSet(b, derEncoderArr, tagOrder);
    }

    private void putOrderedSet(byte b, DerEncoder[] derEncoderArr, Comparator<byte[]> comparator) throws IOException {
        int length = derEncoderArr.length;
        DerOutputStream[] derOutputStreamArr = new DerOutputStream[length];
        for (int i = 0; i < derEncoderArr.length; i++) {
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStreamArr[i] = derOutputStream;
            derEncoderArr[i].derEncode(derOutputStream);
        }
        byte[][] bArr = new byte[length][];
        for (int i2 = 0; i2 < length; i2++) {
            bArr[i2] = derOutputStreamArr[i2].toByteArray();
        }
        Arrays.sort(bArr, comparator);
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (int i3 = 0; i3 < length; i3++) {
            derOutputStream2.write(bArr[i3]);
        }
        write(b, derOutputStream2);
    }

    public void putUTF8String(String str) throws IOException {
        writeString(str, (byte) 12, "UTF8");
    }

    public void putPrintableString(String str) throws IOException {
        writeString(str, (byte) 19, "ASCII");
    }

    public void putT61String(String str) throws IOException {
        writeString(str, (byte) 20, "ISO-8859-1");
    }

    public void putIA5String(String str) throws IOException {
        writeString(str, (byte) 22, "ASCII");
    }

    public void putBMPString(String str) throws IOException {
        writeString(str, (byte) 30, "UnicodeBigUnmarked");
    }

    public void putGeneralString(String str) throws IOException {
        writeString(str, (byte) 27, "ASCII");
    }

    private void writeString(String str, byte b, String str2) throws IOException {
        byte[] bytes = str.getBytes(str2);
        write(b);
        putLength(bytes.length);
        write(bytes);
    }

    public void putUTCTime(Date date) throws IOException {
        putTime(date, (byte) 23);
    }

    public void putGeneralizedTime(Date date) throws IOException {
        putTime(date, (byte) 24);
    }

    private void putTime(Date date, byte b) throws IOException {
        String str;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        if (b == 23) {
            str = "yyMMddHHmmss'Z'";
        } else {
            b = 24;
            str = "yyyyMMddHHmmss'Z'";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(str, Locale.f698US);
        simpleDateFormat.setTimeZone(timeZone);
        byte[] bytes = simpleDateFormat.format(date).getBytes("ISO-8859-1");
        write(b);
        putLength(bytes.length);
        write(bytes);
    }

    public void putLength(int i) throws IOException {
        if (i < 128) {
            write((byte) i);
        } else if (i < 256) {
            write(-127);
            write((byte) i);
        } else if (i < 65536) {
            write(-126);
            write((byte) (i >> 8));
            write((byte) i);
        } else if (i < 16777216) {
            write(-125);
            write((byte) (i >> 16));
            write((byte) (i >> 8));
            write((byte) i);
        } else {
            write(-124);
            write((byte) (i >> 24));
            write((byte) (i >> 16));
            write((byte) (i >> 8));
            write((byte) i);
        }
    }

    public void putTag(byte b, boolean z, byte b2) {
        byte b3 = (byte) (b | b2);
        if (z) {
            b3 = (byte) (b3 | NetworkStackConstants.TCPHDR_URG);
        }
        write(b3);
    }

    public void derEncode(OutputStream outputStream) throws IOException {
        outputStream.write(toByteArray());
    }
}
