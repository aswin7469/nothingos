package libcore.p030io;

import android.annotation.SystemApi;
import java.p026io.ByteArrayOutputStream;
import java.p026io.EOFException;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.Reader;
import java.p026io.StringWriter;
import java.util.concurrent.atomic.AtomicReference;
import libcore.util.ArrayUtils;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
/* renamed from: libcore.io.Streams */
public final class Streams {
    private static AtomicReference<byte[]> skipBuffer = new AtomicReference<>();

    private Streams() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int readSingleByte(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[1];
        if (inputStream.read(bArr, 0, 1) != -1) {
            return bArr[0] & 255;
        }
        return -1;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void writeSingleByte(OutputStream outputStream, int i) throws IOException {
        outputStream.write(new byte[]{(byte) (i & 255)});
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void readFully(InputStream inputStream, byte[] bArr) throws IOException {
        readFully(inputStream, bArr, 0, bArr.length);
    }

    public static void readFully(InputStream inputStream, byte[] bArr, int i, int i2) throws IOException {
        if (i2 != 0) {
            if (inputStream == null) {
                throw new NullPointerException("in == null");
            } else if (bArr != null) {
                ArrayUtils.throwsIfOutOfBounds(bArr.length, i, i2);
                while (i2 > 0) {
                    int read = inputStream.read(bArr, i, i2);
                    if (read >= 0) {
                        i += read;
                        i2 -= read;
                    } else {
                        throw new EOFException();
                    }
                }
            } else {
                throw new NullPointerException("dst == null");
            }
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static byte[] readFully(InputStream inputStream) throws IOException {
        try {
            return readFullyNoClose(inputStream);
        } finally {
            inputStream.close();
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static byte[] readFullyNoClose(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter stringWriter = new StringWriter();
            char[] cArr = new char[1024];
            while (true) {
                int read = reader.read(cArr);
                if (read == -1) {
                    return stringWriter.toString();
                }
                stringWriter.write(cArr, 0, read);
            }
        } finally {
            reader.close();
        }
    }

    public static void skipAll(InputStream inputStream) throws IOException {
        do {
            inputStream.skip(Long.MAX_VALUE);
        } while (inputStream.read() != -1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0015, code lost:
        r3 = (int) java.lang.Math.min(r8 - r1, (long) r0.length);
     */
    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.MODULE_LIBRARIES)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static long skipByReading(java.p026io.InputStream r7, long r8) throws java.p026io.IOException {
        /*
            java.util.concurrent.atomic.AtomicReference<byte[]> r0 = skipBuffer
            r1 = 0
            java.lang.Object r0 = r0.getAndSet(r1)
            byte[] r0 = (byte[]) r0
            if (r0 != 0) goto L_0x000f
            r0 = 4096(0x1000, float:5.74E-42)
            byte[] r0 = new byte[r0]
        L_0x000f:
            r1 = 0
        L_0x0011:
            int r3 = (r1 > r8 ? 1 : (r1 == r8 ? 0 : -1))
            if (r3 >= 0) goto L_0x002b
            long r3 = r8 - r1
            int r5 = r0.length
            long r5 = (long) r5
            long r3 = java.lang.Math.min((long) r3, (long) r5)
            int r3 = (int) r3
            r4 = 0
            int r4 = r7.read(r0, r4, r3)
            r5 = -1
            if (r4 != r5) goto L_0x0027
            goto L_0x002b
        L_0x0027:
            long r5 = (long) r4
            long r1 = r1 + r5
            if (r4 >= r3) goto L_0x0011
        L_0x002b:
            java.util.concurrent.atomic.AtomicReference<byte[]> r7 = skipBuffer
            r7.set(r0)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: libcore.p030io.Streams.skipByReading(java.io.InputStream, long):long");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static int copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        int i = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return i;
            }
            i += read;
            outputStream.write(bArr, 0, read);
        }
    }

    public static String readAsciiLine(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder(80);
        while (true) {
            int read = inputStream.read();
            if (read == -1) {
                throw new EOFException();
            } else if (read == 10) {
                int length = sb.length();
                if (length > 0) {
                    int i = length - 1;
                    if (sb.charAt(i) == 13) {
                        sb.setLength(i);
                    }
                }
                return sb.toString();
            } else {
                sb.append((char) read);
            }
        }
    }
}
