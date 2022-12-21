package java.p026io;

import java.nio.ByteOrder;
import kotlin.UShort;
import libcore.p030io.Memory;
import sun.security.util.DerValue;

/* renamed from: java.io.DataInputStream */
public class DataInputStream extends FilterInputStream implements DataInput {
    private byte[] bytearr = new byte[80];
    private char[] chararr = new char[80];
    private char[] lineBuffer;
    private byte[] readBuffer = new byte[8];

    public DataInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public final int read(byte[] bArr) throws IOException {
        return this.f521in.read(bArr, 0, bArr.length);
    }

    public final int read(byte[] bArr, int i, int i2) throws IOException {
        return this.f521in.read(bArr, i, i2);
    }

    public final void readFully(byte[] bArr) throws IOException {
        readFully(bArr, 0, bArr.length);
    }

    public final void readFully(byte[] bArr, int i, int i2) throws IOException {
        if (i2 >= 0) {
            int i3 = 0;
            while (i3 < i2) {
                int read = this.f521in.read(bArr, i + i3, i2 - i3);
                if (read >= 0) {
                    i3 += read;
                } else {
                    throw new EOFException();
                }
            }
            return;
        }
        throw new IndexOutOfBoundsException();
    }

    public final int skipBytes(int i) throws IOException {
        int i2 = 0;
        while (i2 < i) {
            int skip = (int) this.f521in.skip((long) (i - i2));
            if (skip <= 0) {
                break;
            }
            i2 += skip;
        }
        return i2;
    }

    public final boolean readBoolean() throws IOException {
        int read = this.f521in.read();
        if (read >= 0) {
            return read != 0;
        }
        throw new EOFException();
    }

    public final byte readByte() throws IOException {
        int read = this.f521in.read();
        if (read >= 0) {
            return (byte) read;
        }
        throw new EOFException();
    }

    public final int readUnsignedByte() throws IOException {
        int read = this.f521in.read();
        if (read >= 0) {
            return read;
        }
        throw new EOFException();
    }

    public final short readShort() throws IOException {
        readFully(this.readBuffer, 0, 2);
        return Memory.peekShort(this.readBuffer, 0, ByteOrder.BIG_ENDIAN);
    }

    public final int readUnsignedShort() throws IOException {
        readFully(this.readBuffer, 0, 2);
        return Memory.peekShort(this.readBuffer, 0, ByteOrder.BIG_ENDIAN) & UShort.MAX_VALUE;
    }

    public final char readChar() throws IOException {
        readFully(this.readBuffer, 0, 2);
        return (char) Memory.peekShort(this.readBuffer, 0, ByteOrder.BIG_ENDIAN);
    }

    public final int readInt() throws IOException {
        readFully(this.readBuffer, 0, 4);
        return Memory.peekInt(this.readBuffer, 0, ByteOrder.BIG_ENDIAN);
    }

    public final long readLong() throws IOException {
        readFully(this.readBuffer, 0, 8);
        byte[] bArr = this.readBuffer;
        return (((long) bArr[0]) << 56) + (((long) (bArr[1] & 255)) << 48) + (((long) (bArr[2] & 255)) << 40) + (((long) (bArr[3] & 255)) << 32) + (((long) (bArr[4] & 255)) << 24) + ((long) ((bArr[5] & 255) << 16)) + ((long) ((bArr[6] & 255) << 8)) + ((long) ((bArr[7] & 255) << 0));
    }

    public final float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    public final double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Deprecated
    public final String readLine() throws IOException {
        int read;
        char[] cArr = this.lineBuffer;
        if (cArr == null) {
            cArr = new char[128];
            this.lineBuffer = cArr;
        }
        int length = cArr.length;
        int i = 0;
        while (true) {
            read = this.f521in.read();
            if (read == -1 || read == 10) {
                break;
            } else if (read != 13) {
                length--;
                if (length < 0) {
                    int i2 = i + 128;
                    char[] cArr2 = new char[i2];
                    System.arraycopy((Object) this.lineBuffer, 0, (Object) cArr2, 0, i);
                    this.lineBuffer = cArr2;
                    char[] cArr3 = cArr2;
                    length = (i2 - i) - 1;
                    cArr = cArr3;
                }
                cArr[i] = (char) read;
                i++;
            } else {
                int read2 = this.f521in.read();
                if (read2 != 10 && read2 != -1) {
                    if (!(this.f521in instanceof PushbackInputStream)) {
                        this.f521in = new PushbackInputStream(this.f521in);
                    }
                    ((PushbackInputStream) this.f521in).unread(read2);
                }
            }
        }
        if (read == -1 && i == 0) {
            return null;
        }
        return String.copyValueOf(cArr, 0, i);
    }

    public final String readUTF() throws IOException {
        return readUTF(this);
    }

    public static final String readUTF(DataInput dataInput) throws IOException {
        char[] cArr;
        byte[] bArr;
        int i;
        int i2;
        int i3;
        int readUnsignedShort = dataInput.readUnsignedShort();
        if (dataInput instanceof DataInputStream) {
            DataInputStream dataInputStream = (DataInputStream) dataInput;
            if (dataInputStream.bytearr.length < readUnsignedShort) {
                int i4 = readUnsignedShort * 2;
                dataInputStream.bytearr = new byte[i4];
                dataInputStream.chararr = new char[i4];
            }
            cArr = dataInputStream.chararr;
            bArr = dataInputStream.bytearr;
        } else {
            bArr = new byte[readUnsignedShort];
            cArr = new char[readUnsignedShort];
        }
        dataInput.readFully(bArr, 0, readUnsignedShort);
        int i5 = 0;
        int i6 = 0;
        while (i < readUnsignedShort) {
            byte b = bArr[i] & 255;
            if (b > Byte.MAX_VALUE) {
                break;
            }
            i5 = i + 1;
            cArr[i2] = (char) b;
            i6 = i2 + 1;
        }
        while (i < readUnsignedShort) {
            byte b2 = bArr[i] & 255;
            switch (b2 >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    i++;
                    cArr[i2] = (char) b2;
                    i2++;
                    continue;
                case 12:
                case 13:
                    i += 2;
                    if (i <= readUnsignedShort) {
                        byte b3 = bArr[i - 1];
                        if ((b3 & DerValue.TAG_PRIVATE) == 128) {
                            i3 = i2 + 1;
                            cArr[i2] = (char) (((b2 & 31) << 6) | (b3 & 63));
                            break;
                        } else {
                            throw new UTFDataFormatException("malformed input around byte " + i);
                        }
                    } else {
                        throw new UTFDataFormatException("malformed input: partial character at end");
                    }
                case 14:
                    i += 3;
                    if (i <= readUnsignedShort) {
                        byte b4 = bArr[i - 2];
                        int i7 = i - 1;
                        byte b5 = bArr[i7];
                        if ((b4 & DerValue.TAG_PRIVATE) == 128 && (b5 & DerValue.TAG_PRIVATE) == 128) {
                            i3 = i2 + 1;
                            cArr[i2] = (char) (((b2 & 15) << 12) | ((b4 & 63) << 6) | ((b5 & 63) << 0));
                            break;
                        } else {
                            throw new UTFDataFormatException("malformed input around byte " + i7);
                        }
                    } else {
                        throw new UTFDataFormatException("malformed input: partial character at end");
                    }
                default:
                    throw new UTFDataFormatException("malformed input around byte " + i);
            }
            i2 = i3;
        }
        return new String(cArr, 0, i2);
    }
}
