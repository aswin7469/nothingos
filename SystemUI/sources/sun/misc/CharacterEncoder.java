package sun.misc;

import java.nio.ByteBuffer;
import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.PrintStream;

public abstract class CharacterEncoder {
    protected PrintStream pStream;

    /* access modifiers changed from: protected */
    public abstract int bytesPerAtom();

    /* access modifiers changed from: protected */
    public abstract int bytesPerLine();

    /* access modifiers changed from: protected */
    public abstract void encodeAtom(OutputStream outputStream, byte[] bArr, int i, int i2) throws IOException;

    /* access modifiers changed from: protected */
    public void encodeBufferSuffix(OutputStream outputStream) throws IOException {
    }

    /* access modifiers changed from: protected */
    public void encodeLinePrefix(OutputStream outputStream, int i) throws IOException {
    }

    /* access modifiers changed from: protected */
    public void encodeBufferPrefix(OutputStream outputStream) throws IOException {
        this.pStream = new PrintStream(outputStream);
    }

    /* access modifiers changed from: protected */
    public void encodeLineSuffix(OutputStream outputStream) throws IOException {
        this.pStream.println();
    }

    /* access modifiers changed from: protected */
    public int readFully(InputStream inputStream, byte[] bArr) throws IOException {
        for (int i = 0; i < bArr.length; i++) {
            int read = inputStream.read();
            if (read == -1) {
                return i;
            }
            bArr[i] = (byte) read;
        }
        return bArr.length;
    }

    public void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[bytesPerLine()];
        encodeBufferPrefix(outputStream);
        while (true) {
            int readFully = readFully(inputStream, bArr);
            if (readFully == 0) {
                break;
            }
            encodeLinePrefix(outputStream, readFully);
            int i = 0;
            while (i < readFully) {
                if (bytesPerAtom() + i <= readFully) {
                    encodeAtom(outputStream, bArr, i, bytesPerAtom());
                } else {
                    encodeAtom(outputStream, bArr, i, readFully - i);
                }
                i += bytesPerAtom();
            }
            if (readFully < bytesPerLine()) {
                break;
            }
            encodeLineSuffix(outputStream);
        }
        encodeBufferSuffix(outputStream);
    }

    public void encode(byte[] bArr, OutputStream outputStream) throws IOException {
        encode((InputStream) new ByteArrayInputStream(bArr), outputStream);
    }

    public String encode(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encode((InputStream) new ByteArrayInputStream(bArr), (OutputStream) byteArrayOutputStream);
            return byteArrayOutputStream.toString("8859_1");
        } catch (Exception unused) {
            throw new Error("CharacterEncoder.encode internal error");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x0023  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private byte[] getBytes(java.nio.ByteBuffer r3) {
        /*
            r2 = this;
            boolean r2 = r3.hasArray()
            if (r2 == 0) goto L_0x0020
            byte[] r2 = r3.array()
            int r0 = r2.length
            int r1 = r3.capacity()
            if (r0 != r1) goto L_0x0020
            int r0 = r2.length
            int r1 = r3.remaining()
            if (r0 != r1) goto L_0x0020
            int r0 = r3.limit()
            r3.position((int) r0)
            goto L_0x0021
        L_0x0020:
            r2 = 0
        L_0x0021:
            if (r2 != 0) goto L_0x002c
            int r2 = r3.remaining()
            byte[] r2 = new byte[r2]
            r3.get((byte[]) r2)
        L_0x002c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.CharacterEncoder.getBytes(java.nio.ByteBuffer):byte[]");
    }

    public void encode(ByteBuffer byteBuffer, OutputStream outputStream) throws IOException {
        encode(getBytes(byteBuffer), outputStream);
    }

    public String encode(ByteBuffer byteBuffer) {
        return encode(getBytes(byteBuffer));
    }

    public void encodeBuffer(InputStream inputStream, OutputStream outputStream) throws IOException {
        int readFully;
        byte[] bArr = new byte[bytesPerLine()];
        encodeBufferPrefix(outputStream);
        do {
            readFully = readFully(inputStream, bArr);
            if (readFully == 0) {
                break;
            }
            encodeLinePrefix(outputStream, readFully);
            int i = 0;
            while (i < readFully) {
                if (bytesPerAtom() + i <= readFully) {
                    encodeAtom(outputStream, bArr, i, bytesPerAtom());
                } else {
                    encodeAtom(outputStream, bArr, i, readFully - i);
                }
                i += bytesPerAtom();
            }
            encodeLineSuffix(outputStream);
        } while (readFully >= bytesPerLine());
        encodeBufferSuffix(outputStream);
    }

    public void encodeBuffer(byte[] bArr, OutputStream outputStream) throws IOException {
        encodeBuffer((InputStream) new ByteArrayInputStream(bArr), outputStream);
    }

    public String encodeBuffer(byte[] bArr) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            encodeBuffer((InputStream) new ByteArrayInputStream(bArr), (OutputStream) byteArrayOutputStream);
            return byteArrayOutputStream.toString();
        } catch (Exception unused) {
            throw new Error("CharacterEncoder.encodeBuffer internal error");
        }
    }

    public void encodeBuffer(ByteBuffer byteBuffer, OutputStream outputStream) throws IOException {
        encodeBuffer(getBytes(byteBuffer), outputStream);
    }

    public String encodeBuffer(ByteBuffer byteBuffer) {
        return encodeBuffer(getBytes(byteBuffer));
    }
}
