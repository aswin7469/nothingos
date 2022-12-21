package java.p026io;

/* renamed from: java.io.DataOutputStream */
public class DataOutputStream extends FilterOutputStream implements DataOutput {
    private byte[] bytearr = null;
    private byte[] writeBuffer = new byte[8];
    protected int written;

    public DataOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    private void incCount(int i) {
        int i2 = this.written + i;
        if (i2 < 0) {
            i2 = Integer.MAX_VALUE;
        }
        this.written = i2;
    }

    public synchronized void write(int i) throws IOException {
        this.out.write(i);
        incCount(1);
    }

    public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
        incCount(i2);
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public final void writeBoolean(boolean z) throws IOException {
        this.out.write(z ? 1 : 0);
        incCount(1);
    }

    public final void writeByte(int i) throws IOException {
        this.out.write(i);
        incCount(1);
    }

    public final void writeShort(int i) throws IOException {
        this.out.write((i >>> 8) & 255);
        this.out.write((i >>> 0) & 255);
        incCount(2);
    }

    public final void writeChar(int i) throws IOException {
        this.out.write((i >>> 8) & 255);
        this.out.write((i >>> 0) & 255);
        incCount(2);
    }

    public final void writeInt(int i) throws IOException {
        this.out.write((i >>> 24) & 255);
        this.out.write((i >>> 16) & 255);
        this.out.write((i >>> 8) & 255);
        this.out.write((i >>> 0) & 255);
        incCount(4);
    }

    public final void writeLong(long j) throws IOException {
        byte[] bArr = this.writeBuffer;
        bArr[0] = (byte) ((int) (j >>> 56));
        bArr[1] = (byte) ((int) (j >>> 48));
        bArr[2] = (byte) ((int) (j >>> 40));
        bArr[3] = (byte) ((int) (j >>> 32));
        bArr[4] = (byte) ((int) (j >>> 24));
        bArr[5] = (byte) ((int) (j >>> 16));
        bArr[6] = (byte) ((int) (j >>> 8));
        bArr[7] = (byte) ((int) (j >>> 0));
        this.out.write(this.writeBuffer, 0, 8);
        incCount(8);
    }

    public final void writeFloat(float f) throws IOException {
        writeInt(Float.floatToIntBits(f));
    }

    public final void writeDouble(double d) throws IOException {
        writeLong(Double.doubleToLongBits(d));
    }

    public final void writeBytes(String str) throws IOException {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            this.out.write((int) (byte) str.charAt(i));
        }
        incCount(length);
    }

    public final void writeChars(String str) throws IOException {
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            this.out.write((charAt >>> 8) & 255);
            this.out.write((charAt >>> 0) & 255);
        }
        incCount(length * 2);
    }

    public final void writeUTF(String str) throws IOException {
        writeUTF(str, this);
    }

    static int writeUTF(String str, DataOutput dataOutput) throws IOException {
        byte[] bArr;
        int i;
        int i2;
        int length = str.length();
        int i3 = 0;
        for (int i4 = 0; i4 < length; i4++) {
            char charAt = str.charAt(i4);
            i3 = (charAt < 1 || charAt > 127) ? charAt > 2047 ? i3 + 3 : i3 + 2 : i3 + 1;
        }
        if (i3 <= 65535) {
            if (dataOutput instanceof DataOutputStream) {
                DataOutputStream dataOutputStream = (DataOutputStream) dataOutput;
                byte[] bArr2 = dataOutputStream.bytearr;
                if (bArr2 == null || bArr2.length < i3 + 2) {
                    dataOutputStream.bytearr = new byte[((i3 * 2) + 2)];
                }
                bArr = dataOutputStream.bytearr;
            } else {
                bArr = new byte[(i3 + 2)];
            }
            bArr[0] = (byte) ((i3 >>> 8) & 255);
            bArr[1] = (byte) ((i3 >>> 0) & 255);
            int i5 = 0;
            int i6 = 2;
            while (i5 < length) {
                char charAt2 = str.charAt(i5);
                if (charAt2 < 1 || charAt2 > 127) {
                    break;
                }
                bArr[i] = (byte) charAt2;
                i5++;
                i6 = i + 1;
            }
            while (i5 < length) {
                char charAt3 = str.charAt(i5);
                if (charAt3 >= 1 && charAt3 <= 127) {
                    i2 = i + 1;
                    bArr[i] = (byte) charAt3;
                } else if (charAt3 > 2047) {
                    int i7 = i + 1;
                    bArr[i] = (byte) (((charAt3 >> 12) & 15) | 224);
                    int i8 = i7 + 1;
                    bArr[i7] = (byte) (((charAt3 >> 6) & 63) | 128);
                    i2 = i8 + 1;
                    bArr[i8] = (byte) (((charAt3 >> 0) & 63) | 128);
                } else {
                    int i9 = i + 1;
                    bArr[i] = (byte) (((charAt3 >> 6) & 31) | 192);
                    i = i9 + 1;
                    bArr[i9] = (byte) (((charAt3 >> 0) & 63) | 128);
                    i5++;
                }
                i = i2;
                i5++;
            }
            int i10 = i3 + 2;
            dataOutput.write(bArr, 0, i10);
            return i10;
        }
        throw new UTFDataFormatException("encoded string too long: " + i3 + " bytes");
    }

    public final int size() {
        return this.written;
    }
}
