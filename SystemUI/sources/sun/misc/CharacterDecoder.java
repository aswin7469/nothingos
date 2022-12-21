package sun.misc;

import java.nio.ByteBuffer;
import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.PushbackInputStream;

public abstract class CharacterDecoder {
    /* access modifiers changed from: protected */
    public abstract int bytesPerAtom();

    /* access modifiers changed from: protected */
    public abstract int bytesPerLine();

    /* access modifiers changed from: protected */
    public void decodeBufferPrefix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
    }

    /* access modifiers changed from: protected */
    public void decodeBufferSuffix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
    }

    /* access modifiers changed from: protected */
    public void decodeLineSuffix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
    }

    /* access modifiers changed from: protected */
    public int decodeLinePrefix(PushbackInputStream pushbackInputStream, OutputStream outputStream) throws IOException {
        return bytesPerLine();
    }

    /* access modifiers changed from: protected */
    public void decodeAtom(PushbackInputStream pushbackInputStream, OutputStream outputStream, int i) throws IOException {
        throw new CEStreamExhausted();
    }

    /* access modifiers changed from: protected */
    public int readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        int i3 = 0;
        while (i3 < i2) {
            int read = inputStream.read();
            if (read != -1) {
                bArr[i3 + i] = (byte) read;
                i3++;
            } else if (i3 == 0) {
                return -1;
            } else {
                return i3;
            }
        }
        return i2;
    }

    public void decodeBuffer(InputStream inputStream, OutputStream outputStream) throws IOException {
        PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
        decodeBufferPrefix(pushbackInputStream, outputStream);
        while (true) {
            try {
                int decodeLinePrefix = decodeLinePrefix(pushbackInputStream, outputStream);
                int i = 0;
                while (bytesPerAtom() + i < decodeLinePrefix) {
                    decodeAtom(pushbackInputStream, outputStream, bytesPerAtom());
                    bytesPerAtom();
                    i += bytesPerAtom();
                }
                if (bytesPerAtom() + i == decodeLinePrefix) {
                    decodeAtom(pushbackInputStream, outputStream, bytesPerAtom());
                    bytesPerAtom();
                } else {
                    decodeAtom(pushbackInputStream, outputStream, decodeLinePrefix - i);
                }
                decodeLineSuffix(pushbackInputStream, outputStream);
            } catch (CEStreamExhausted unused) {
                decodeBufferSuffix(pushbackInputStream, outputStream);
                return;
            }
        }
    }

    public byte[] decodeBuffer(String str) throws IOException {
        byte[] bArr = new byte[str.length()];
        str.getBytes(0, str.length(), bArr, 0);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeBuffer(byteArrayInputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public byte[] decodeBuffer(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decodeBuffer(inputStream, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public ByteBuffer decodeBufferToByteBuffer(String str) throws IOException {
        return ByteBuffer.wrap(decodeBuffer(str));
    }

    public ByteBuffer decodeBufferToByteBuffer(InputStream inputStream) throws IOException {
        return ByteBuffer.wrap(decodeBuffer(inputStream));
    }
}
