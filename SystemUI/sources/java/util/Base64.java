package java.util;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.p026io.FilterOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;

public class Base64 {
    private Base64() {
    }

    public static Encoder getEncoder() {
        return Encoder.RFC4648;
    }

    public static Encoder getUrlEncoder() {
        return Encoder.RFC4648_URLSAFE;
    }

    public static Encoder getMimeEncoder() {
        return Encoder.RFC2045;
    }

    public static Encoder getMimeEncoder(int i, byte[] bArr) {
        Objects.requireNonNull(bArr);
        int[] r0 = Decoder.fromBase64;
        int length = bArr.length;
        int i2 = 0;
        while (i2 < length) {
            byte b = bArr[i2];
            if (r0[b & 255] == -1) {
                i2++;
            } else {
                throw new IllegalArgumentException("Illegal base64 line separator character 0x" + Integer.toString(b, 16));
            }
        }
        if (i <= 0) {
            return Encoder.RFC4648;
        }
        return new Encoder(false, bArr, (i >> 2) << 2, true);
    }

    public static Decoder getDecoder() {
        return Decoder.RFC4648;
    }

    public static Decoder getUrlDecoder() {
        return Decoder.RFC4648_URLSAFE;
    }

    public static Decoder getMimeDecoder() {
        return Decoder.RFC2045;
    }

    public static class Encoder {
        private static final byte[] CRLF;
        private static final int MIMELINEMAX = 76;
        static final Encoder RFC2045;
        static final Encoder RFC4648 = new Encoder(false, (byte[]) null, -1, true);
        static final Encoder RFC4648_URLSAFE = new Encoder(true, (byte[]) null, -1, true);
        /* access modifiers changed from: private */
        public static final char[] toBase64 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        /* access modifiers changed from: private */
        public static final char[] toBase64URL = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        private final boolean doPadding;
        private final boolean isURL;
        private final int linemax;
        private final byte[] newline;

        private Encoder(boolean z, byte[] bArr, int i, boolean z2) {
            this.isURL = z;
            this.newline = bArr;
            this.linemax = i;
            this.doPadding = z2;
        }

        static {
            byte[] bArr = {13, 10};
            CRLF = bArr;
            RFC2045 = new Encoder(false, bArr, 76, true);
        }

        private final int outLength(int i) {
            int i2;
            if (this.doPadding) {
                i2 = ((i + 2) / 3) * 4;
            } else {
                int i3 = i % 3;
                i2 = ((i / 3) * 4) + (i3 == 0 ? 0 : i3 + 1);
            }
            int i4 = this.linemax;
            return i4 > 0 ? i2 + (((i2 - 1) / i4) * this.newline.length) : i2;
        }

        public byte[] encode(byte[] bArr) {
            int outLength = outLength(bArr.length);
            byte[] bArr2 = new byte[outLength];
            int encode0 = encode0(bArr, 0, bArr.length, bArr2);
            return encode0 != outLength ? Arrays.copyOf(bArr2, encode0) : bArr2;
        }

        public int encode(byte[] bArr, byte[] bArr2) {
            if (bArr2.length >= outLength(bArr.length)) {
                return encode0(bArr, 0, bArr.length, bArr2);
            }
            throw new IllegalArgumentException("Output byte array is too small for encoding all input bytes");
        }

        public String encodeToString(byte[] bArr) {
            byte[] encode = encode(bArr);
            return new String(encode, 0, 0, encode.length);
        }

        public ByteBuffer encode(ByteBuffer byteBuffer) {
            int i;
            int outLength = outLength(byteBuffer.remaining());
            byte[] bArr = new byte[outLength];
            if (byteBuffer.hasArray()) {
                i = encode0(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.arrayOffset() + byteBuffer.limit(), bArr);
                byteBuffer.position(byteBuffer.limit());
            } else {
                int remaining = byteBuffer.remaining();
                byte[] bArr2 = new byte[remaining];
                byteBuffer.get(bArr2);
                i = encode0(bArr2, 0, remaining, bArr);
            }
            if (i != outLength) {
                bArr = Arrays.copyOf(bArr, i);
            }
            return ByteBuffer.wrap(bArr);
        }

        public OutputStream wrap(OutputStream outputStream) {
            Objects.requireNonNull(outputStream);
            return new EncOutputStream(outputStream, this.isURL ? toBase64URL : toBase64, this.newline, this.linemax, this.doPadding);
        }

        public Encoder withoutPadding() {
            if (!this.doPadding) {
                return this;
            }
            return new Encoder(this.isURL, this.newline, this.linemax, false);
        }

        private int encode0(byte[] bArr, int i, int i2, byte[] bArr2) {
            char[] cArr = this.isURL ? toBase64URL : toBase64;
            int i3 = ((i2 - i) / 3) * 3;
            int i4 = i + i3;
            int i5 = this.linemax;
            if (i5 > 0 && i3 > (i5 / 4) * 3) {
                i3 = (i5 / 4) * 3;
            }
            int i6 = 0;
            while (i < i4) {
                int min = Math.min(i + i3, i4);
                int i7 = i;
                int i8 = i6;
                while (i7 < min) {
                    int i9 = i7 + 1;
                    int i10 = i9 + 1;
                    byte b = ((bArr[i7] & 255) << 16) | ((bArr[i9] & 255) << 8);
                    int i11 = i10 + 1;
                    byte b2 = b | (bArr[i10] & 255);
                    int i12 = i8 + 1;
                    bArr2[i8] = (byte) cArr[(b2 >>> 18) & 63];
                    int i13 = i12 + 1;
                    bArr2[i12] = (byte) cArr[(b2 >>> 12) & 63];
                    int i14 = i13 + 1;
                    bArr2[i13] = (byte) cArr[(b2 >>> 6) & 63];
                    i8 = i14 + 1;
                    bArr2[i14] = (byte) cArr[b2 & 63];
                    i7 = i11;
                }
                int i15 = ((min - i) / 3) * 4;
                i6 += i15;
                if (i15 == this.linemax && min < i2) {
                    byte[] bArr3 = this.newline;
                    int length = bArr3.length;
                    int i16 = 0;
                    while (i16 < length) {
                        bArr2[i6] = bArr3[i16];
                        i16++;
                        i6++;
                    }
                }
                i = min;
            }
            if (i >= i2) {
                return i6;
            }
            int i17 = i + 1;
            byte b3 = bArr[i] & 255;
            int i18 = i6 + 1;
            bArr2[i6] = (byte) cArr[b3 >> 2];
            if (i17 == i2) {
                int i19 = i18 + 1;
                bArr2[i18] = (byte) cArr[(b3 << 4) & 63];
                if (!this.doPadding) {
                    return i19;
                }
                int i20 = i19 + 1;
                bArr2[i19] = 61;
                int i21 = i20 + 1;
                bArr2[i20] = 61;
                return i21;
            }
            byte b4 = bArr[i17] & 255;
            int i22 = i18 + 1;
            bArr2[i18] = (byte) cArr[((b3 << 4) & 63) | (b4 >> 4)];
            int i23 = i22 + 1;
            bArr2[i22] = (byte) cArr[(b4 << 2) & 63];
            if (!this.doPadding) {
                return i23;
            }
            bArr2[i23] = 61;
            return i23 + 1;
        }
    }

    public static class Decoder {
        static final Decoder RFC2045 = new Decoder(false, true);
        static final Decoder RFC4648 = new Decoder(false, false);
        static final Decoder RFC4648_URLSAFE = new Decoder(true, false);
        /* access modifiers changed from: private */
        public static final int[] fromBase64;
        private static final int[] fromBase64URL;
        private final boolean isMIME;
        private final boolean isURL;

        private Decoder(boolean z, boolean z2) {
            this.isURL = z;
            this.isMIME = z2;
        }

        static {
            int[] iArr = new int[256];
            fromBase64 = iArr;
            Arrays.fill(iArr, -1);
            for (int i = 0; i < Encoder.toBase64.length; i++) {
                fromBase64[Encoder.toBase64[i]] = i;
            }
            fromBase64[61] = -2;
            int[] iArr2 = new int[256];
            fromBase64URL = iArr2;
            Arrays.fill(iArr2, -1);
            for (int i2 = 0; i2 < Encoder.toBase64URL.length; i2++) {
                fromBase64URL[Encoder.toBase64URL[i2]] = i2;
            }
            fromBase64URL[61] = -2;
        }

        public byte[] decode(byte[] bArr) {
            int outLength = outLength(bArr, 0, bArr.length);
            byte[] bArr2 = new byte[outLength];
            int decode0 = decode0(bArr, 0, bArr.length, bArr2);
            return decode0 != outLength ? Arrays.copyOf(bArr2, decode0) : bArr2;
        }

        public byte[] decode(String str) {
            return decode(str.getBytes(StandardCharsets.ISO_8859_1));
        }

        public int decode(byte[] bArr, byte[] bArr2) {
            if (bArr2.length >= outLength(bArr, 0, bArr.length)) {
                return decode0(bArr, 0, bArr.length, bArr2);
            }
            throw new IllegalArgumentException("Output byte array is too small for decoding all input bytes");
        }

        public ByteBuffer decode(ByteBuffer byteBuffer) {
            int i;
            int i2;
            byte[] bArr;
            int position = byteBuffer.position();
            try {
                if (byteBuffer.hasArray()) {
                    bArr = byteBuffer.array();
                    i2 = byteBuffer.arrayOffset() + byteBuffer.position();
                    i = byteBuffer.arrayOffset() + byteBuffer.limit();
                    byteBuffer.position(byteBuffer.limit());
                } else {
                    i = byteBuffer.remaining();
                    bArr = new byte[i];
                    byteBuffer.get(bArr);
                    i2 = 0;
                }
                byte[] bArr2 = new byte[outLength(bArr, i2, i)];
                return ByteBuffer.wrap(bArr2, 0, decode0(bArr, i2, i, bArr2));
            } catch (IllegalArgumentException e) {
                byteBuffer.position(position);
                throw e;
            }
        }

        public InputStream wrap(InputStream inputStream) {
            Objects.requireNonNull(inputStream);
            return new DecInputStream(inputStream, this.isURL ? fromBase64URL : fromBase64, this.isMIME);
        }

        private int outLength(byte[] bArr, int i, int i2) {
            int i3;
            int[] iArr = this.isURL ? fromBase64URL : fromBase64;
            int i4 = i2 - i;
            int i5 = 0;
            if (i4 == 0) {
                return 0;
            }
            if (i4 >= 2) {
                if (this.isMIME) {
                    int i6 = 0;
                    while (true) {
                        if (i >= i2) {
                            break;
                        }
                        int i7 = i + 1;
                        byte b = bArr[i] & 255;
                        if (b == 61) {
                            i4 -= (i2 - i7) + 1;
                            break;
                        }
                        if (iArr[b] == -1) {
                            i6++;
                        }
                        i = i7;
                    }
                    i4 -= i6;
                } else if (bArr[i2 - 1] == 61) {
                    i5 = bArr[i2 - 2] == 61 ? 2 : 1;
                }
                if (i5 == 0 && (i3 = i4 & 3) != 0) {
                    i5 = 4 - i3;
                }
                return (((i4 + 3) / 4) * 3) - i5;
            } else if (this.isMIME && iArr[0] == -1) {
                return 0;
            } else {
                throw new IllegalArgumentException("Input byte[] should at least have 2 bytes for base64 bytes");
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:14:0x002b, code lost:
            if (r11[r8] == 61) goto L_0x002f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
            if (r4 != 18) goto L_0x007a;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private int decode0(byte[] r11, int r12, int r13, byte[] r14) {
            /*
                r10 = this;
                boolean r0 = r10.isURL
                if (r0 == 0) goto L_0x0007
                int[] r0 = fromBase64URL
                goto L_0x0009
            L_0x0007:
                int[] r0 = fromBase64
            L_0x0009:
                r1 = 18
                r2 = 0
                r4 = r1
                r3 = r2
                r5 = r3
            L_0x000f:
                r6 = 6
                r7 = 16
                if (r12 >= r13) goto L_0x007a
                int r8 = r12 + 1
                byte r12 = r11[r12]
                r12 = r12 & 255(0xff, float:3.57E-43)
                r12 = r0[r12]
                if (r12 >= 0) goto L_0x005b
                r9 = -2
                if (r12 != r9) goto L_0x003a
                if (r4 != r6) goto L_0x002e
                if (r8 == r13) goto L_0x0032
                int r12 = r8 + 1
                byte r2 = r11[r8]
                r8 = 61
                if (r2 != r8) goto L_0x0032
                goto L_0x002f
            L_0x002e:
                r12 = r8
            L_0x002f:
                if (r4 == r1) goto L_0x0032
                goto L_0x007a
            L_0x0032:
                java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
                java.lang.String r11 = "Input byte array has wrong 4-byte ending unit"
                r10.<init>((java.lang.String) r11)
                throw r10
            L_0x003a:
                boolean r12 = r10.isMIME
                if (r12 == 0) goto L_0x003f
                goto L_0x0078
            L_0x003f:
                java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r12 = new java.lang.StringBuilder
                java.lang.String r13 = "Illegal base64 character "
                r12.<init>((java.lang.String) r13)
                int r8 = r8 + -1
                byte r11 = r11[r8]
                java.lang.String r11 = java.lang.Integer.toString(r11, r7)
                r12.append((java.lang.String) r11)
                java.lang.String r11 = r12.toString()
                r10.<init>((java.lang.String) r11)
                throw r10
            L_0x005b:
                int r12 = r12 << r4
                r12 = r12 | r3
                int r4 = r4 + -6
                if (r4 >= 0) goto L_0x0077
                int r3 = r5 + 1
                int r4 = r12 >> 16
                byte r4 = (byte) r4
                r14[r5] = r4
                int r4 = r3 + 1
                int r5 = r12 >> 8
                byte r5 = (byte) r5
                r14[r3] = r5
                int r5 = r4 + 1
                byte r12 = (byte) r12
                r14[r4] = r12
                r4 = r1
                r3 = r2
                goto L_0x0078
            L_0x0077:
                r3 = r12
            L_0x0078:
                r12 = r8
                goto L_0x000f
            L_0x007a:
                if (r4 != r6) goto L_0x0085
                int r1 = r5 + 1
                int r2 = r3 >> 16
                byte r2 = (byte) r2
                r14[r5] = r2
                r5 = r1
                goto L_0x009a
            L_0x0085:
                if (r4 != 0) goto L_0x0096
                int r1 = r5 + 1
                int r2 = r3 >> 16
                byte r2 = (byte) r2
                r14[r5] = r2
                int r5 = r1 + 1
                int r2 = r3 >> 8
                byte r2 = (byte) r2
                r14[r1] = r2
                goto L_0x009a
            L_0x0096:
                r14 = 12
                if (r4 == r14) goto L_0x00c0
            L_0x009a:
                if (r12 >= r13) goto L_0x00bf
                boolean r14 = r10.isMIME
                if (r14 == 0) goto L_0x00ab
                int r14 = r12 + 1
                byte r12 = r11[r12]
                r12 = r0[r12]
                if (r12 >= 0) goto L_0x00aa
                r12 = r14
                goto L_0x009a
            L_0x00aa:
                r12 = r14
            L_0x00ab:
                java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r11 = new java.lang.StringBuilder
                java.lang.String r13 = "Input byte array has incorrect ending byte at "
                r11.<init>((java.lang.String) r13)
                r11.append((int) r12)
                java.lang.String r11 = r11.toString()
                r10.<init>((java.lang.String) r11)
                throw r10
            L_0x00bf:
                return r5
            L_0x00c0:
                java.lang.IllegalArgumentException r10 = new java.lang.IllegalArgumentException
                java.lang.String r11 = "Last unit does not have enough valid bits"
                r10.<init>((java.lang.String) r11)
                throw r10
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Base64.Decoder.decode0(byte[], int, int, byte[]):int");
        }
    }

    private static class EncOutputStream extends FilterOutputStream {

        /* renamed from: b0 */
        private int f634b0;

        /* renamed from: b1 */
        private int f635b1;

        /* renamed from: b2 */
        private int f636b2;
        private final char[] base64;
        private boolean closed = false;
        private final boolean doPadding;
        private int leftover = 0;
        private final int linemax;
        private int linepos = 0;
        private final byte[] newline;

        EncOutputStream(OutputStream outputStream, char[] cArr, byte[] bArr, int i, boolean z) {
            super(outputStream);
            this.base64 = cArr;
            this.newline = bArr;
            this.linemax = i;
            this.doPadding = z;
        }

        public void write(int i) throws IOException {
            write(new byte[]{(byte) (i & 255)}, 0, 1);
        }

        private void checkNewline() throws IOException {
            if (this.linepos == this.linemax) {
                this.out.write(this.newline);
                this.linepos = 0;
            }
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            if (this.closed) {
                throw new IOException("Stream is closed");
            } else if (i < 0 || i2 < 0 || i2 > bArr.length - i) {
                throw new ArrayIndexOutOfBoundsException();
            } else if (i2 != 0) {
                int i3 = this.leftover;
                if (i3 != 0) {
                    if (i3 == 1) {
                        int i4 = i + 1;
                        this.f635b1 = bArr[i] & 255;
                        i2--;
                        if (i2 == 0) {
                            this.leftover = i3 + 1;
                            return;
                        }
                        i = i4;
                    }
                    this.f636b2 = bArr[i] & 255;
                    i2--;
                    checkNewline();
                    this.out.write((int) this.base64[this.f634b0 >> 2]);
                    this.out.write((int) this.base64[((this.f634b0 << 4) & 63) | (this.f635b1 >> 4)]);
                    this.out.write((int) this.base64[((this.f635b1 << 2) & 63) | (this.f636b2 >> 6)]);
                    this.out.write((int) this.base64[this.f636b2 & 63]);
                    this.linepos += 4;
                    i++;
                }
                int i5 = i2 / 3;
                this.leftover = i2 - (i5 * 3);
                while (true) {
                    int i6 = i5 - 1;
                    if (i5 <= 0) {
                        break;
                    }
                    checkNewline();
                    int i7 = i + 1;
                    int i8 = i7 + 1;
                    byte b = ((bArr[i] & 255) << 16) | ((bArr[i7] & 255) << 8) | (bArr[i8] & 255);
                    this.out.write((int) this.base64[(b >>> 18) & 63]);
                    this.out.write((int) this.base64[(b >>> 12) & 63]);
                    this.out.write((int) this.base64[(b >>> 6) & 63]);
                    this.out.write((int) this.base64[b & 63]);
                    this.linepos += 4;
                    i = i8 + 1;
                    i5 = i6;
                }
                int i9 = this.leftover;
                if (i9 == 1) {
                    this.f634b0 = bArr[i] & 255;
                } else if (i9 == 2) {
                    this.f634b0 = bArr[i] & 255;
                    this.f635b1 = bArr[i + 1] & 255;
                }
            }
        }

        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                int i = this.leftover;
                if (i == 1) {
                    checkNewline();
                    this.out.write((int) this.base64[this.f634b0 >> 2]);
                    this.out.write((int) this.base64[(this.f634b0 << 4) & 63]);
                    if (this.doPadding) {
                        this.out.write(61);
                        this.out.write(61);
                    }
                } else if (i == 2) {
                    checkNewline();
                    this.out.write((int) this.base64[this.f634b0 >> 2]);
                    this.out.write((int) this.base64[((this.f634b0 << 4) & 63) | (this.f635b1 >> 4)]);
                    this.out.write((int) this.base64[(this.f635b1 << 2) & 63]);
                    if (this.doPadding) {
                        this.out.write(61);
                    }
                }
                this.leftover = 0;
                this.out.close();
            }
        }
    }

    private static class DecInputStream extends InputStream {
        private final int[] base64;
        private int bits = 0;
        private boolean closed = false;
        private boolean eof = false;

        /* renamed from: is */
        private final InputStream f633is;
        private final boolean isMIME;
        private int nextin = 18;
        private int nextout = -8;
        private byte[] sbBuf = new byte[1];

        DecInputStream(InputStream inputStream, int[] iArr, boolean z) {
            this.f633is = inputStream;
            this.base64 = iArr;
            this.isMIME = z;
        }

        public int read() throws IOException {
            if (read(this.sbBuf, 0, 1) == -1) {
                return -1;
            }
            return this.sbBuf[0] & 255;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int i3;
            int i4;
            if (this.closed) {
                throw new IOException("Stream is closed");
            } else if (this.eof && this.nextout < 0) {
                return -1;
            } else {
                if (i < 0 || i2 < 0 || i2 > bArr.length - i) {
                    throw new IndexOutOfBoundsException();
                }
                if (this.nextout >= 0) {
                    int i5 = i;
                    while (i2 != 0) {
                        i3 = i5 + 1;
                        int i6 = this.bits;
                        int i7 = this.nextout;
                        bArr[i5] = (byte) (i6 >> i7);
                        i2--;
                        int i8 = i7 - 8;
                        this.nextout = i8;
                        if (i8 < 0) {
                            this.bits = 0;
                        } else {
                            i5 = i3;
                        }
                    }
                    return i5 - i;
                }
                i3 = i;
                while (true) {
                    if (i2 <= 0) {
                        break;
                    }
                    int read = this.f633is.read();
                    if (read == -1) {
                        this.eof = true;
                        int i9 = this.nextin;
                        if (i9 != 18) {
                            if (i9 != 12) {
                                int i10 = i3 + 1;
                                int i11 = this.bits;
                                bArr[i3] = (byte) (i11 >> 16);
                                int i12 = i2 - 1;
                                if (i9 == 0) {
                                    if (i12 == 0) {
                                        this.bits = i11 >> 8;
                                        this.nextout = 0;
                                    } else {
                                        i3 = i10 + 1;
                                        bArr[i10] = (byte) (i11 >> 8);
                                    }
                                }
                                i3 = i10;
                            } else {
                                throw new IOException("Base64 stream has one un-decoded dangling byte.");
                            }
                        }
                        if (i3 == i) {
                            return -1;
                        }
                        return i3 - i;
                    } else if (read == 61) {
                        int i13 = this.nextin;
                        if (i13 == 18 || i13 == 12 || (i13 == 6 && this.f633is.read() != 61)) {
                            throw new IOException("Illegal base64 ending sequence:" + this.nextin);
                        }
                        int i14 = i3 + 1;
                        int i15 = this.bits;
                        bArr[i3] = (byte) (i15 >> 16);
                        int i16 = i2 - 1;
                        if (this.nextin == 0) {
                            if (i16 == 0) {
                                this.bits = i15 >> 8;
                                this.nextout = 0;
                            } else {
                                bArr[i14] = (byte) (i15 >> 8);
                                i4 = i14 + 1;
                                this.eof = true;
                            }
                        }
                        i4 = i14;
                        this.eof = true;
                    } else {
                        int i17 = this.base64[read];
                        if (i17 != -1) {
                            int i18 = this.bits;
                            int i19 = this.nextin;
                            this.bits = (i17 << i19) | i18;
                            if (i19 == 0) {
                                this.nextin = 18;
                                this.nextout = 16;
                                while (true) {
                                    int i20 = this.nextout;
                                    if (i20 < 0) {
                                        this.bits = 0;
                                        break;
                                    }
                                    int i21 = i3 + 1;
                                    bArr[i3] = (byte) (this.bits >> i20);
                                    i2--;
                                    int i22 = i20 - 8;
                                    this.nextout = i22;
                                    if (i2 == 0 && i22 >= 0) {
                                        return i21 - i;
                                    }
                                    i3 = i21;
                                }
                            } else {
                                this.nextin = i19 - 6;
                            }
                        } else if (!this.isMIME) {
                            throw new IOException("Illegal base64 character " + Integer.toString(i17, 16));
                        }
                    }
                }
                return i3 - i;
            }
        }

        public int available() throws IOException {
            if (!this.closed) {
                return this.f633is.available();
            }
            throw new IOException("Stream is closed");
        }

        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                this.f633is.close();
            }
        }
    }
}
