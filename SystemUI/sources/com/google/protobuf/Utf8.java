package com.google.protobuf;

import android.net.wifi.WifiManager;
import java.nio.ByteBuffer;

final class Utf8 {
    private static final long ASCII_MASK_LONG = -9187201950435737472L;
    public static final int COMPLETE = 0;
    public static final int MALFORMED = -1;
    static final int MAX_BYTES_PER_CHAR = 3;
    private static final int UNSAFE_COUNT_ASCII_THRESHOLD = 16;
    private static final Processor processor = (UnsafeProcessor.isAvailable() ? new UnsafeProcessor() : new SafeProcessor());

    /* access modifiers changed from: private */
    public static int incompleteStateFor(int i) {
        if (i > -12) {
            return -1;
        }
        return i;
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(int i, int i2) {
        if (i > -12 || i2 > -65) {
            return -1;
        }
        return i ^ (i2 << 8);
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(int i, int i2, int i3) {
        if (i > -12 || i2 > -65 || i3 > -65) {
            return -1;
        }
        return (i ^ (i2 << 8)) ^ (i3 << 16);
    }

    public static boolean isValidUtf8(byte[] bArr) {
        return processor.isValidUtf8(bArr, 0, bArr.length);
    }

    public static boolean isValidUtf8(byte[] bArr, int i, int i2) {
        return processor.isValidUtf8(bArr, i, i2);
    }

    public static int partialIsValidUtf8(int i, byte[] bArr, int i2, int i3) {
        return processor.partialIsValidUtf8(i, bArr, i2, i3);
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(byte[] bArr, int i, int i2) {
        byte b = bArr[i - 1];
        int i3 = i2 - i;
        if (i3 == 0) {
            return incompleteStateFor(b);
        }
        if (i3 == 1) {
            return incompleteStateFor(b, bArr[i]);
        }
        if (i3 == 2) {
            return incompleteStateFor((int) b, (int) bArr[i], (int) bArr[i + 1]);
        }
        throw new AssertionError();
    }

    /* access modifiers changed from: private */
    public static int incompleteStateFor(ByteBuffer byteBuffer, int i, int i2, int i3) {
        if (i3 == 0) {
            return incompleteStateFor(i);
        }
        if (i3 == 1) {
            return incompleteStateFor(i, byteBuffer.get(i2));
        }
        if (i3 == 2) {
            return incompleteStateFor(i, (int) byteBuffer.get(i2), (int) byteBuffer.get(i2 + 1));
        }
        throw new AssertionError();
    }

    static class UnpairedSurrogateException extends IllegalArgumentException {
        UnpairedSurrogateException(int i, int i2) {
            super("Unpaired surrogate at index " + i + " of " + i2);
        }
    }

    static int encodedLength(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        while (i < length && charSequence.charAt(i) < 128) {
            i++;
        }
        int i2 = length;
        while (true) {
            if (i < length) {
                char charAt = charSequence.charAt(i);
                if (charAt >= 2048) {
                    i2 += encodedLengthGeneral(charSequence, i);
                    break;
                }
                i2 += (127 - charAt) >>> 31;
                i++;
            } else {
                break;
            }
        }
        if (i2 >= length) {
            return i2;
        }
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (((long) i2) + WifiManager.WIFI_FEATURE_P2P_RAND_MAC));
    }

    private static int encodedLengthGeneral(CharSequence charSequence, int i) {
        int length = charSequence.length();
        int i2 = 0;
        while (i < length) {
            char charAt = charSequence.charAt(i);
            if (charAt < 2048) {
                i2 += (127 - charAt) >>> 31;
            } else {
                i2 += 2;
                if (55296 <= charAt && charAt <= 57343) {
                    if (Character.codePointAt(charSequence, i) >= 65536) {
                        i++;
                    } else {
                        throw new UnpairedSurrogateException(i, length);
                    }
                }
            }
            i++;
        }
        return i2;
    }

    static int encode(CharSequence charSequence, byte[] bArr, int i, int i2) {
        return processor.encodeUtf8(charSequence, bArr, i, i2);
    }

    static boolean isValidUtf8(ByteBuffer byteBuffer) {
        return processor.isValidUtf8(byteBuffer, byteBuffer.position(), byteBuffer.remaining());
    }

    static int partialIsValidUtf8(int i, ByteBuffer byteBuffer, int i2, int i3) {
        return processor.partialIsValidUtf8(i, byteBuffer, i2, i3);
    }

    static void encodeUtf8(CharSequence charSequence, ByteBuffer byteBuffer) {
        processor.encodeUtf8(charSequence, byteBuffer);
    }

    /* access modifiers changed from: private */
    public static int estimateConsecutiveAscii(ByteBuffer byteBuffer, int i, int i2) {
        int i3 = i2 - 7;
        int i4 = i;
        while (i4 < i3 && (byteBuffer.getLong(i4) & ASCII_MASK_LONG) == 0) {
            i4 += 8;
        }
        return i4 - i;
    }

    static abstract class Processor {
        /* access modifiers changed from: package-private */
        public abstract int encodeUtf8(CharSequence charSequence, byte[] bArr, int i, int i2);

        /* access modifiers changed from: package-private */
        public abstract void encodeUtf8Direct(CharSequence charSequence, ByteBuffer byteBuffer);

        /* access modifiers changed from: package-private */
        public abstract int partialIsValidUtf8(int i, byte[] bArr, int i2, int i3);

        /* access modifiers changed from: package-private */
        public abstract int partialIsValidUtf8Direct(int i, ByteBuffer byteBuffer, int i2, int i3);

        Processor() {
        }

        /* access modifiers changed from: package-private */
        public final boolean isValidUtf8(byte[] bArr, int i, int i2) {
            return partialIsValidUtf8(0, bArr, i, i2) == 0;
        }

        /* access modifiers changed from: package-private */
        public final boolean isValidUtf8(ByteBuffer byteBuffer, int i, int i2) {
            return partialIsValidUtf8(0, byteBuffer, i, i2) == 0;
        }

        /* access modifiers changed from: package-private */
        public final int partialIsValidUtf8(int i, ByteBuffer byteBuffer, int i2, int i3) {
            if (byteBuffer.hasArray()) {
                int arrayOffset = byteBuffer.arrayOffset();
                return partialIsValidUtf8(i, byteBuffer.array(), i2 + arrayOffset, arrayOffset + i3);
            } else if (byteBuffer.isDirect()) {
                return partialIsValidUtf8Direct(i, byteBuffer, i2, i3);
            } else {
                return partialIsValidUtf8Default(i, byteBuffer, i2, i3);
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x004c, code lost:
            if (r7.get(r8) > -65) goto L_0x004e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x008f, code lost:
            if (r7.get(r6) > -65) goto L_0x0091;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0017, code lost:
            if (r7.get(r8) > -65) goto L_0x001d;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final int partialIsValidUtf8Default(int r6, java.nio.ByteBuffer r7, int r8, int r9) {
            /*
                r5 = this;
                if (r6 == 0) goto L_0x0092
                if (r8 < r9) goto L_0x0005
                return r6
            L_0x0005:
                byte r5 = (byte) r6
                r0 = -32
                r1 = -1
                r2 = -65
                if (r5 >= r0) goto L_0x001e
                r6 = -62
                if (r5 < r6) goto L_0x001d
                int r5 = r8 + 1
                byte r6 = r7.get((int) r8)
                if (r6 <= r2) goto L_0x001a
                goto L_0x001d
            L_0x001a:
                r8 = r5
                goto L_0x0092
            L_0x001d:
                return r1
            L_0x001e:
                r3 = -16
                if (r5 >= r3) goto L_0x004f
                int r6 = r6 >> 8
                int r6 = ~r6
                byte r6 = (byte) r6
                if (r6 != 0) goto L_0x0038
                int r6 = r8 + 1
                byte r8 = r7.get((int) r8)
                if (r6 < r9) goto L_0x0035
                int r5 = com.google.protobuf.Utf8.incompleteStateFor(r5, r8)
                return r5
            L_0x0035:
                r4 = r8
                r8 = r6
                r6 = r4
            L_0x0038:
                if (r6 > r2) goto L_0x004e
                r3 = -96
                if (r5 != r0) goto L_0x0040
                if (r6 < r3) goto L_0x004e
            L_0x0040:
                r0 = -19
                if (r5 != r0) goto L_0x0046
                if (r6 >= r3) goto L_0x004e
            L_0x0046:
                int r5 = r8 + 1
                byte r6 = r7.get((int) r8)
                if (r6 <= r2) goto L_0x001a
            L_0x004e:
                return r1
            L_0x004f:
                int r0 = r6 >> 8
                int r0 = ~r0
                byte r0 = (byte) r0
                if (r0 != 0) goto L_0x0064
                int r6 = r8 + 1
                byte r0 = r7.get((int) r8)
                if (r6 < r9) goto L_0x0062
                int r5 = com.google.protobuf.Utf8.incompleteStateFor(r5, r0)
                return r5
            L_0x0062:
                r8 = 0
                goto L_0x006a
            L_0x0064:
                int r6 = r6 >> 16
                byte r6 = (byte) r6
                r4 = r8
                r8 = r6
                r6 = r4
            L_0x006a:
                if (r8 != 0) goto L_0x007c
                int r8 = r6 + 1
                byte r6 = r7.get((int) r6)
                if (r8 < r9) goto L_0x0079
                int r5 = com.google.protobuf.Utf8.incompleteStateFor((int) r5, (int) r0, (int) r6)
                return r5
            L_0x0079:
                r4 = r8
                r8 = r6
                r6 = r4
            L_0x007c:
                if (r0 > r2) goto L_0x0091
                int r5 = r5 << 28
                int r0 = r0 + 112
                int r5 = r5 + r0
                int r5 = r5 >> 30
                if (r5 != 0) goto L_0x0091
                if (r8 > r2) goto L_0x0091
                int r8 = r6 + 1
                byte r5 = r7.get((int) r6)
                if (r5 <= r2) goto L_0x0092
            L_0x0091:
                return r1
            L_0x0092:
                int r5 = partialIsValidUtf8(r7, r8, r9)
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.Processor.partialIsValidUtf8Default(int, java.nio.ByteBuffer, int, int):int");
        }

        private static int partialIsValidUtf8(ByteBuffer byteBuffer, int i, int i2) {
            int access$200 = i + Utf8.estimateConsecutiveAscii(byteBuffer, i, i2);
            while (access$200 < i2) {
                int i3 = access$200 + 1;
                byte b = byteBuffer.get(access$200);
                if (b < 0) {
                    if (b < -32) {
                        if (i3 >= i2) {
                            return b;
                        }
                        if (b < -62 || byteBuffer.get(i3) > -65) {
                            return -1;
                        }
                        i3++;
                    } else if (b < -16) {
                        if (i3 >= i2 - 1) {
                            return Utf8.incompleteStateFor(byteBuffer, b, i3, i2 - i3);
                        }
                        int i4 = i3 + 1;
                        byte b2 = byteBuffer.get(i3);
                        if (b2 > -65 || ((b == -32 && b2 < -96) || ((b == -19 && b2 >= -96) || byteBuffer.get(i4) > -65))) {
                            return -1;
                        }
                        access$200 = i4 + 1;
                    } else if (i3 >= i2 - 2) {
                        return Utf8.incompleteStateFor(byteBuffer, b, i3, i2 - i3);
                    } else {
                        int i5 = i3 + 1;
                        byte b3 = byteBuffer.get(i3);
                        if (b3 <= -65 && (((b << 28) + (b3 + 112)) >> 30) == 0) {
                            int i6 = i5 + 1;
                            if (byteBuffer.get(i5) <= -65) {
                                i3 = i6 + 1;
                                if (byteBuffer.get(i6) > -65) {
                                }
                            }
                        }
                        return -1;
                    }
                }
                access$200 = i3;
            }
            return 0;
        }

        /* access modifiers changed from: package-private */
        public final void encodeUtf8(CharSequence charSequence, ByteBuffer byteBuffer) {
            if (byteBuffer.hasArray()) {
                int arrayOffset = byteBuffer.arrayOffset();
                byteBuffer.position(Utf8.encode(charSequence, byteBuffer.array(), byteBuffer.position() + arrayOffset, byteBuffer.remaining()) - arrayOffset);
            } else if (byteBuffer.isDirect()) {
                encodeUtf8Direct(charSequence, byteBuffer);
            } else {
                encodeUtf8Default(charSequence, byteBuffer);
            }
        }

        /* access modifiers changed from: package-private */
        public final void encodeUtf8Default(CharSequence charSequence, ByteBuffer byteBuffer) {
            int i;
            int length = charSequence.length();
            int position = byteBuffer.position();
            int i2 = 0;
            while (i2 < length) {
                try {
                    char charAt = charSequence.charAt(i2);
                    if (charAt >= 128) {
                        break;
                    }
                    byteBuffer.put(position + i2, (byte) charAt);
                    i2++;
                } catch (IndexOutOfBoundsException unused) {
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(i2) + " at index " + (byteBuffer.position() + Math.max(i2, (position - byteBuffer.position()) + 1)));
                }
            }
            if (i2 == length) {
                byteBuffer.position(position + i2);
                return;
            }
            position += i2;
            while (i2 < length) {
                char charAt2 = charSequence.charAt(i2);
                if (charAt2 < 128) {
                    byteBuffer.put(position, (byte) charAt2);
                } else if (charAt2 < 2048) {
                    i = position + 1;
                    try {
                        byteBuffer.put(position, (byte) ((charAt2 >>> 6) | 192));
                        byteBuffer.put(i, (byte) ((charAt2 & '?') | 128));
                        position = i;
                    } catch (IndexOutOfBoundsException unused2) {
                        position = i;
                        throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(i2) + " at index " + (byteBuffer.position() + Math.max(i2, (position - byteBuffer.position()) + 1)));
                    }
                } else if (charAt2 < 55296 || 57343 < charAt2) {
                    i = position + 1;
                    byteBuffer.put(position, (byte) ((charAt2 >>> 12) | 224));
                    position = i + 1;
                    byteBuffer.put(i, (byte) (((charAt2 >>> 6) & 63) | 128));
                    byteBuffer.put(position, (byte) ((charAt2 & '?') | 128));
                } else {
                    int i3 = i2 + 1;
                    if (i3 != length) {
                        try {
                            char charAt3 = charSequence.charAt(i3);
                            if (Character.isSurrogatePair(charAt2, charAt3)) {
                                int codePoint = Character.toCodePoint(charAt2, charAt3);
                                int i4 = position + 1;
                                try {
                                    byteBuffer.put(position, (byte) ((codePoint >>> 18) | 240));
                                    position = i4 + 1;
                                    byteBuffer.put(i4, (byte) (((codePoint >>> 12) & 63) | 128));
                                    i4 = position + 1;
                                    byteBuffer.put(position, (byte) (((codePoint >>> 6) & 63) | 128));
                                    byteBuffer.put(i4, (byte) ((codePoint & 63) | 128));
                                    position = i4;
                                    i2 = i3;
                                } catch (IndexOutOfBoundsException unused3) {
                                    position = i4;
                                    i2 = i3;
                                    throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(i2) + " at index " + (byteBuffer.position() + Math.max(i2, (position - byteBuffer.position()) + 1)));
                                }
                            } else {
                                i2 = i3;
                            }
                        } catch (IndexOutOfBoundsException unused4) {
                            i2 = i3;
                            throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(i2) + " at index " + (byteBuffer.position() + Math.max(i2, (position - byteBuffer.position()) + 1)));
                        }
                    }
                    throw new UnpairedSurrogateException(i2, length);
                }
                i2++;
                position++;
            }
            byteBuffer.position(position);
        }
    }

    static final class SafeProcessor extends Processor {
        SafeProcessor() {
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0046, code lost:
            if (r7[r8] > -65) goto L_0x0048;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:49:0x0083, code lost:
            if (r7[r6] > -65) goto L_0x0085;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0015, code lost:
            if (r7[r8] > -65) goto L_0x001b;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int partialIsValidUtf8(int r6, byte[] r7, int r8, int r9) {
            /*
                r5 = this;
                if (r6 == 0) goto L_0x0086
                if (r8 < r9) goto L_0x0005
                return r6
            L_0x0005:
                byte r5 = (byte) r6
                r0 = -32
                r1 = -1
                r2 = -65
                if (r5 >= r0) goto L_0x001c
                r6 = -62
                if (r5 < r6) goto L_0x001b
                int r5 = r8 + 1
                byte r6 = r7[r8]
                if (r6 <= r2) goto L_0x0018
                goto L_0x001b
            L_0x0018:
                r8 = r5
                goto L_0x0086
            L_0x001b:
                return r1
            L_0x001c:
                r3 = -16
                if (r5 >= r3) goto L_0x0049
                int r6 = r6 >> 8
                int r6 = ~r6
                byte r6 = (byte) r6
                if (r6 != 0) goto L_0x0034
                int r6 = r8 + 1
                byte r8 = r7[r8]
                if (r6 < r9) goto L_0x0031
                int r5 = com.google.protobuf.Utf8.incompleteStateFor(r5, r8)
                return r5
            L_0x0031:
                r4 = r8
                r8 = r6
                r6 = r4
            L_0x0034:
                if (r6 > r2) goto L_0x0048
                r3 = -96
                if (r5 != r0) goto L_0x003c
                if (r6 < r3) goto L_0x0048
            L_0x003c:
                r0 = -19
                if (r5 != r0) goto L_0x0042
                if (r6 >= r3) goto L_0x0048
            L_0x0042:
                int r5 = r8 + 1
                byte r6 = r7[r8]
                if (r6 <= r2) goto L_0x0018
            L_0x0048:
                return r1
            L_0x0049:
                int r0 = r6 >> 8
                int r0 = ~r0
                byte r0 = (byte) r0
                if (r0 != 0) goto L_0x005c
                int r6 = r8 + 1
                byte r0 = r7[r8]
                if (r6 < r9) goto L_0x005a
                int r5 = com.google.protobuf.Utf8.incompleteStateFor(r5, r0)
                return r5
            L_0x005a:
                r8 = 0
                goto L_0x0062
            L_0x005c:
                int r6 = r6 >> 16
                byte r6 = (byte) r6
                r4 = r8
                r8 = r6
                r6 = r4
            L_0x0062:
                if (r8 != 0) goto L_0x0072
                int r8 = r6 + 1
                byte r6 = r7[r6]
                if (r8 < r9) goto L_0x006f
                int r5 = com.google.protobuf.Utf8.incompleteStateFor((int) r5, (int) r0, (int) r6)
                return r5
            L_0x006f:
                r4 = r8
                r8 = r6
                r6 = r4
            L_0x0072:
                if (r0 > r2) goto L_0x0085
                int r5 = r5 << 28
                int r0 = r0 + 112
                int r5 = r5 + r0
                int r5 = r5 >> 30
                if (r5 != 0) goto L_0x0085
                if (r8 > r2) goto L_0x0085
                int r8 = r6 + 1
                byte r5 = r7[r6]
                if (r5 <= r2) goto L_0x0086
            L_0x0085:
                return r1
            L_0x0086:
                int r5 = partialIsValidUtf8(r7, r8, r9)
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.SafeProcessor.partialIsValidUtf8(int, byte[], int, int):int");
        }

        /* access modifiers changed from: package-private */
        public int partialIsValidUtf8Direct(int i, ByteBuffer byteBuffer, int i2, int i3) {
            return partialIsValidUtf8Default(i, byteBuffer, i2, i3);
        }

        /* access modifiers changed from: package-private */
        public int encodeUtf8(CharSequence charSequence, byte[] bArr, int i, int i2) {
            int i3;
            int i4;
            int i5;
            char charAt;
            int length = charSequence.length();
            int i6 = i2 + i;
            int i7 = 0;
            while (i7 < length && (i5 = i7 + i) < i6 && (charAt = charSequence.charAt(i7)) < 128) {
                bArr[i5] = (byte) charAt;
                i7++;
            }
            if (i7 == length) {
                return i + length;
            }
            int i8 = i + i7;
            while (i7 < length) {
                char charAt2 = charSequence.charAt(i7);
                if (charAt2 < 128 && i8 < i6) {
                    i4 = i8 + 1;
                    bArr[i8] = (byte) charAt2;
                } else if (charAt2 < 2048 && i8 <= i6 - 2) {
                    int i9 = i8 + 1;
                    bArr[i8] = (byte) ((charAt2 >>> 6) | 960);
                    i8 = i9 + 1;
                    bArr[i9] = (byte) ((charAt2 & '?') | 128);
                    i7++;
                } else if ((charAt2 < 55296 || 57343 < charAt2) && i8 <= i6 - 3) {
                    int i10 = i8 + 1;
                    bArr[i8] = (byte) ((charAt2 >>> 12) | 480);
                    int i11 = i10 + 1;
                    bArr[i10] = (byte) (((charAt2 >>> 6) & 63) | 128);
                    i4 = i11 + 1;
                    bArr[i11] = (byte) ((charAt2 & '?') | 128);
                } else if (i8 <= i6 - 4) {
                    int i12 = i7 + 1;
                    if (i12 != charSequence.length()) {
                        char charAt3 = charSequence.charAt(i12);
                        if (Character.isSurrogatePair(charAt2, charAt3)) {
                            int codePoint = Character.toCodePoint(charAt2, charAt3);
                            int i13 = i8 + 1;
                            bArr[i8] = (byte) ((codePoint >>> 18) | 240);
                            int i14 = i13 + 1;
                            bArr[i13] = (byte) (((codePoint >>> 12) & 63) | 128);
                            int i15 = i14 + 1;
                            bArr[i14] = (byte) (((codePoint >>> 6) & 63) | 128);
                            i8 = i15 + 1;
                            bArr[i15] = (byte) ((codePoint & 63) | 128);
                            i7 = i12;
                            i7++;
                        } else {
                            i7 = i12;
                        }
                    }
                    throw new UnpairedSurrogateException(i7 - 1, length);
                } else if (55296 > charAt2 || charAt2 > 57343 || ((i3 = i7 + 1) != charSequence.length() && Character.isSurrogatePair(charAt2, charSequence.charAt(i3)))) {
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + i8);
                } else {
                    throw new UnpairedSurrogateException(i7, length);
                }
                i8 = i4;
                i7++;
            }
            return i8;
        }

        /* access modifiers changed from: package-private */
        public void encodeUtf8Direct(CharSequence charSequence, ByteBuffer byteBuffer) {
            encodeUtf8Default(charSequence, byteBuffer);
        }

        private static int partialIsValidUtf8(byte[] bArr, int i, int i2) {
            while (i < i2 && bArr[i] >= 0) {
                i++;
            }
            if (i >= i2) {
                return 0;
            }
            return partialIsValidUtf8NonAscii(bArr, i, i2);
        }

        private static int partialIsValidUtf8NonAscii(byte[] bArr, int i, int i2) {
            while (i < i2) {
                int i3 = i + 1;
                byte b = bArr[i];
                if (b < 0) {
                    if (b < -32) {
                        if (i3 >= i2) {
                            return b;
                        }
                        if (b >= -62) {
                            i = i3 + 1;
                            if (bArr[i3] > -65) {
                            }
                        }
                        return -1;
                    } else if (b < -16) {
                        if (i3 >= i2 - 1) {
                            return Utf8.incompleteStateFor(bArr, i3, i2);
                        }
                        int i4 = i3 + 1;
                        byte b2 = bArr[i3];
                        if (b2 <= -65 && ((b != -32 || b2 >= -96) && (b != -19 || b2 < -96))) {
                            i = i4 + 1;
                            if (bArr[i4] > -65) {
                            }
                        }
                        return -1;
                    } else if (i3 >= i2 - 2) {
                        return Utf8.incompleteStateFor(bArr, i3, i2);
                    } else {
                        int i5 = i3 + 1;
                        byte b3 = bArr[i3];
                        if (b3 <= -65 && (((b << 28) + (b3 + 112)) >> 30) == 0) {
                            int i6 = i5 + 1;
                            if (bArr[i5] <= -65) {
                                i3 = i6 + 1;
                                if (bArr[i6] > -65) {
                                }
                            }
                        }
                        return -1;
                    }
                }
                i = i3;
            }
            return 0;
        }
    }

    static final class UnsafeProcessor extends Processor {
        UnsafeProcessor() {
        }

        static boolean isAvailable() {
            return UnsafeUtil.hasUnsafeArrayOperations() && UnsafeUtil.hasUnsafeByteBufferOperations();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0063, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r12, r1) > -65) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a8, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r12, r1) > -65) goto L_0x00aa;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int partialIsValidUtf8(int r11, byte[] r12, int r13, int r14) {
            /*
                r10 = this;
                r10 = r13 | r14
                int r0 = r12.length
                int r0 = r0 - r14
                r10 = r10 | r0
                r0 = 0
                if (r10 < 0) goto L_0x00b2
                long r1 = com.google.protobuf.UnsafeUtil.getArrayBaseOffset()
                long r3 = (long) r13
                long r1 = r1 + r3
                long r3 = com.google.protobuf.UnsafeUtil.getArrayBaseOffset()
                long r13 = (long) r14
                long r3 = r3 + r13
                if (r11 == 0) goto L_0x00ab
                int r10 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r10 < 0) goto L_0x001b
                return r11
            L_0x001b:
                byte r10 = (byte) r11
                r13 = -32
                r14 = -1
                r5 = -65
                r6 = 1
                if (r10 >= r13) goto L_0x0035
                r11 = -62
                if (r10 < r11) goto L_0x0034
                long r6 = r6 + r1
                byte r10 = com.google.protobuf.UnsafeUtil.getByte(r12, r1)
                if (r10 <= r5) goto L_0x0031
                goto L_0x0034
            L_0x0031:
                r1 = r6
                goto L_0x00ab
            L_0x0034:
                return r14
            L_0x0035:
                r8 = -16
                if (r10 >= r8) goto L_0x0069
                int r11 = r11 >> 8
                int r11 = ~r11
                byte r11 = (byte) r11
                if (r11 != 0) goto L_0x004f
                long r8 = r1 + r6
                byte r11 = com.google.protobuf.UnsafeUtil.getByte(r12, r1)
                int r0 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
                if (r0 < 0) goto L_0x004e
                int r10 = com.google.protobuf.Utf8.incompleteStateFor(r10, r11)
                return r10
            L_0x004e:
                r1 = r8
            L_0x004f:
                if (r11 > r5) goto L_0x0068
                r0 = -96
                if (r10 != r13) goto L_0x0057
                if (r11 < r0) goto L_0x0068
            L_0x0057:
                r13 = -19
                if (r10 != r13) goto L_0x005d
                if (r11 >= r0) goto L_0x0068
            L_0x005d:
                long r10 = r1 + r6
                byte r13 = com.google.protobuf.UnsafeUtil.getByte(r12, r1)
                if (r13 <= r5) goto L_0x0066
                goto L_0x0068
            L_0x0066:
                r1 = r10
                goto L_0x00ab
            L_0x0068:
                return r14
            L_0x0069:
                int r13 = r11 >> 8
                int r13 = ~r13
                byte r13 = (byte) r13
                if (r13 != 0) goto L_0x0080
                long r8 = r1 + r6
                byte r13 = com.google.protobuf.UnsafeUtil.getByte(r12, r1)
                int r11 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
                if (r11 < 0) goto L_0x007e
                int r10 = com.google.protobuf.Utf8.incompleteStateFor(r10, r13)
                return r10
            L_0x007e:
                r1 = r8
                goto L_0x0083
            L_0x0080:
                int r11 = r11 >> 16
                byte r0 = (byte) r11
            L_0x0083:
                if (r0 != 0) goto L_0x0095
                long r8 = r1 + r6
                byte r0 = com.google.protobuf.UnsafeUtil.getByte(r12, r1)
                int r11 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1))
                if (r11 < 0) goto L_0x0094
                int r10 = com.google.protobuf.Utf8.incompleteStateFor((int) r10, (int) r13, (int) r0)
                return r10
            L_0x0094:
                r1 = r8
            L_0x0095:
                if (r13 > r5) goto L_0x00aa
                int r10 = r10 << 28
                int r13 = r13 + 112
                int r10 = r10 + r13
                int r10 = r10 >> 30
                if (r10 != 0) goto L_0x00aa
                if (r0 > r5) goto L_0x00aa
                long r10 = r1 + r6
                byte r13 = com.google.protobuf.UnsafeUtil.getByte(r12, r1)
                if (r13 <= r5) goto L_0x0066
            L_0x00aa:
                return r14
            L_0x00ab:
                long r3 = r3 - r1
                int r10 = (int) r3
                int r10 = partialIsValidUtf8(r12, r1, r10)
                return r10
            L_0x00b2:
                java.lang.ArrayIndexOutOfBoundsException r10 = new java.lang.ArrayIndexOutOfBoundsException
                r11 = 3
                java.lang.Object[] r11 = new java.lang.Object[r11]
                int r12 = r12.length
                java.lang.Integer r12 = java.lang.Integer.valueOf((int) r12)
                r11[r0] = r12
                r12 = 1
                java.lang.Integer r13 = java.lang.Integer.valueOf((int) r13)
                r11[r12] = r13
                r12 = 2
                java.lang.Integer r13 = java.lang.Integer.valueOf((int) r14)
                r11[r12] = r13
                java.lang.String r12 = "Array length=%d, index=%d, limit=%d"
                java.lang.String r11 = java.lang.String.format(r12, r11)
                r10.<init>((java.lang.String) r11)
                throw r10
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.partialIsValidUtf8(int, byte[], int, int):int");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x0063, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r1) > -65) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a8, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r1) > -65) goto L_0x00aa;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int partialIsValidUtf8Direct(int r10, java.nio.ByteBuffer r11, int r12, int r13) {
            /*
                r9 = this;
                r9 = r12 | r13
                int r0 = r11.limit()
                int r0 = r0 - r13
                r9 = r9 | r0
                r0 = 0
                if (r9 < 0) goto L_0x00b2
                long r1 = com.google.protobuf.UnsafeUtil.addressOffset(r11)
                long r3 = (long) r12
                long r1 = r1 + r3
                int r13 = r13 - r12
                long r11 = (long) r13
                long r11 = r11 + r1
                if (r10 == 0) goto L_0x00ab
                int r9 = (r1 > r11 ? 1 : (r1 == r11 ? 0 : -1))
                if (r9 < 0) goto L_0x001b
                return r10
            L_0x001b:
                byte r9 = (byte) r10
                r13 = -32
                r3 = -1
                r4 = -65
                r5 = 1
                if (r9 >= r13) goto L_0x0035
                r10 = -62
                if (r9 < r10) goto L_0x0034
                long r5 = r5 + r1
                byte r9 = com.google.protobuf.UnsafeUtil.getByte(r1)
                if (r9 <= r4) goto L_0x0031
                goto L_0x0034
            L_0x0031:
                r1 = r5
                goto L_0x00ab
            L_0x0034:
                return r3
            L_0x0035:
                r7 = -16
                if (r9 >= r7) goto L_0x0069
                int r10 = r10 >> 8
                int r10 = ~r10
                byte r10 = (byte) r10
                if (r10 != 0) goto L_0x004f
                long r7 = r1 + r5
                byte r10 = com.google.protobuf.UnsafeUtil.getByte(r1)
                int r0 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
                if (r0 < 0) goto L_0x004e
                int r9 = com.google.protobuf.Utf8.incompleteStateFor(r9, r10)
                return r9
            L_0x004e:
                r1 = r7
            L_0x004f:
                if (r10 > r4) goto L_0x0068
                r0 = -96
                if (r9 != r13) goto L_0x0057
                if (r10 < r0) goto L_0x0068
            L_0x0057:
                r13 = -19
                if (r9 != r13) goto L_0x005d
                if (r10 >= r0) goto L_0x0068
            L_0x005d:
                long r9 = r1 + r5
                byte r13 = com.google.protobuf.UnsafeUtil.getByte(r1)
                if (r13 <= r4) goto L_0x0066
                goto L_0x0068
            L_0x0066:
                r1 = r9
                goto L_0x00ab
            L_0x0068:
                return r3
            L_0x0069:
                int r13 = r10 >> 8
                int r13 = ~r13
                byte r13 = (byte) r13
                if (r13 != 0) goto L_0x0080
                long r7 = r1 + r5
                byte r13 = com.google.protobuf.UnsafeUtil.getByte(r1)
                int r10 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
                if (r10 < 0) goto L_0x007e
                int r9 = com.google.protobuf.Utf8.incompleteStateFor(r9, r13)
                return r9
            L_0x007e:
                r1 = r7
                goto L_0x0083
            L_0x0080:
                int r10 = r10 >> 16
                byte r0 = (byte) r10
            L_0x0083:
                if (r0 != 0) goto L_0x0095
                long r7 = r1 + r5
                byte r0 = com.google.protobuf.UnsafeUtil.getByte(r1)
                int r10 = (r7 > r11 ? 1 : (r7 == r11 ? 0 : -1))
                if (r10 < 0) goto L_0x0094
                int r9 = com.google.protobuf.Utf8.incompleteStateFor((int) r9, (int) r13, (int) r0)
                return r9
            L_0x0094:
                r1 = r7
            L_0x0095:
                if (r13 > r4) goto L_0x00aa
                int r9 = r9 << 28
                int r13 = r13 + 112
                int r9 = r9 + r13
                int r9 = r9 >> 30
                if (r9 != 0) goto L_0x00aa
                if (r0 > r4) goto L_0x00aa
                long r9 = r1 + r5
                byte r13 = com.google.protobuf.UnsafeUtil.getByte(r1)
                if (r13 <= r4) goto L_0x0066
            L_0x00aa:
                return r3
            L_0x00ab:
                long r11 = r11 - r1
                int r9 = (int) r11
                int r9 = partialIsValidUtf8(r1, r9)
                return r9
            L_0x00b2:
                java.lang.ArrayIndexOutOfBoundsException r9 = new java.lang.ArrayIndexOutOfBoundsException
                r10 = 3
                java.lang.Object[] r10 = new java.lang.Object[r10]
                int r11 = r11.limit()
                java.lang.Integer r11 = java.lang.Integer.valueOf((int) r11)
                r10[r0] = r11
                r11 = 1
                java.lang.Integer r12 = java.lang.Integer.valueOf((int) r12)
                r10[r11] = r12
                r11 = 2
                java.lang.Integer r12 = java.lang.Integer.valueOf((int) r13)
                r10[r11] = r12
                java.lang.String r11 = "buffer limit=%d, index=%d, limit=%d"
                java.lang.String r10 = java.lang.String.format(r11, r10)
                r9.<init>((java.lang.String) r10)
                throw r9
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.partialIsValidUtf8Direct(int, java.nio.ByteBuffer, int, int):int");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:11:0x0036  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x003d A[LOOP:1: B:14:0x003d->B:38:0x0106, LOOP_START, PHI: r2 r3 r4 r11 
          PHI: (r2v4 int) = (r2v3 int), (r2v6 int) binds: [B:10:0x0034, B:38:0x0106] A[DONT_GENERATE, DONT_INLINE]
          PHI: (r3v2 char) = (r3v1 char), (r3v3 char) binds: [B:10:0x0034, B:38:0x0106] A[DONT_GENERATE, DONT_INLINE]
          PHI: (r4v4 long) = (r4v3 long), (r4v6 long) binds: [B:10:0x0034, B:38:0x0106] A[DONT_GENERATE, DONT_INLINE]
          PHI: (r11v3 long) = (r11v2 long), (r11v5 long) binds: [B:10:0x0034, B:38:0x0106] A[DONT_GENERATE, DONT_INLINE]] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public int encodeUtf8(java.lang.CharSequence r21, byte[] r22, int r23, int r24) {
            /*
                r20 = this;
                r0 = r21
                r1 = r22
                r2 = r23
                r3 = r24
                long r4 = com.google.protobuf.UnsafeUtil.getArrayBaseOffset()
                long r6 = (long) r2
                long r4 = r4 + r6
                long r6 = (long) r3
                long r6 = r6 + r4
                int r8 = r21.length()
                java.lang.String r9 = " at index "
                java.lang.String r10 = "Failed writing "
                if (r8 > r3) goto L_0x0152
                int r11 = r1.length
                int r11 = r11 - r3
                if (r11 < r2) goto L_0x0152
                r2 = 0
            L_0x001f:
                r3 = 128(0x80, float:1.794E-43)
                r11 = 1
                if (r2 >= r8) goto L_0x0034
                char r13 = r0.charAt(r2)
                if (r13 >= r3) goto L_0x0034
                long r11 = r11 + r4
                byte r3 = (byte) r13
                com.google.protobuf.UnsafeUtil.putByte(r1, r4, r3)
                int r2 = r2 + 1
                r4 = r11
                goto L_0x001f
            L_0x0034:
                if (r2 != r8) goto L_0x003d
                long r0 = com.google.protobuf.UnsafeUtil.getArrayBaseOffset()
            L_0x003a:
                long r4 = r4 - r0
                int r0 = (int) r4
                return r0
            L_0x003d:
                if (r2 >= r8) goto L_0x014c
                char r13 = r0.charAt(r2)
                if (r13 >= r3) goto L_0x0054
                int r14 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r14 >= 0) goto L_0x0054
                long r14 = r4 + r11
                byte r13 = (byte) r13
                com.google.protobuf.UnsafeUtil.putByte(r1, r4, r13)
                r4 = r11
                r12 = r14
                r11 = r3
                goto L_0x0106
            L_0x0054:
                r14 = 2048(0x800, float:2.87E-42)
                if (r13 >= r14) goto L_0x007e
                r14 = 2
                long r14 = r6 - r14
                int r14 = (r4 > r14 ? 1 : (r4 == r14 ? 0 : -1))
                if (r14 > 0) goto L_0x007e
                long r14 = r4 + r11
                int r3 = r13 >>> 6
                r3 = r3 | 960(0x3c0, float:1.345E-42)
                byte r3 = (byte) r3
                com.google.protobuf.UnsafeUtil.putByte(r1, r4, r3)
                long r3 = r14 + r11
                r5 = r13 & 63
                r13 = 128(0x80, float:1.794E-43)
                r5 = r5 | r13
                byte r5 = (byte) r5
                com.google.protobuf.UnsafeUtil.putByte(r1, r14, r5)
                r18 = r11
                r11 = 128(0x80, float:1.794E-43)
                r12 = r3
                r4 = r18
                goto L_0x0106
            L_0x007e:
                r3 = 57343(0xdfff, float:8.0355E-41)
                r14 = 55296(0xd800, float:7.7486E-41)
                if (r13 < r14) goto L_0x0088
                if (r3 >= r13) goto L_0x00b9
            L_0x0088:
                r15 = 3
                long r15 = r6 - r15
                int r15 = (r4 > r15 ? 1 : (r4 == r15 ? 0 : -1))
                if (r15 > 0) goto L_0x00b9
                long r14 = r4 + r11
                int r3 = r13 >>> 12
                r3 = r3 | 480(0x1e0, float:6.73E-43)
                byte r3 = (byte) r3
                com.google.protobuf.UnsafeUtil.putByte(r1, r4, r3)
                long r3 = r14 + r11
                int r5 = r13 >>> 6
                r5 = r5 & 63
                r11 = 128(0x80, float:1.794E-43)
                r5 = r5 | r11
                byte r5 = (byte) r5
                com.google.protobuf.UnsafeUtil.putByte(r1, r14, r5)
                r14 = 1
                long r16 = r3 + r14
                r5 = r13 & 63
                r5 = r5 | r11
                byte r5 = (byte) r5
                com.google.protobuf.UnsafeUtil.putByte(r1, r3, r5)
                r12 = r16
                r4 = 1
                r11 = 128(0x80, float:1.794E-43)
                goto L_0x0106
            L_0x00b9:
                r11 = 4
                long r11 = r6 - r11
                int r11 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1))
                if (r11 > 0) goto L_0x0119
                int r3 = r2 + 1
                if (r3 == r8) goto L_0x0111
                char r2 = r0.charAt(r3)
                boolean r11 = java.lang.Character.isSurrogatePair(r13, r2)
                if (r11 == 0) goto L_0x0110
                int r2 = java.lang.Character.toCodePoint(r13, r2)
                r11 = 1
                long r13 = r4 + r11
                int r15 = r2 >>> 18
                r15 = r15 | 240(0xf0, float:3.36E-43)
                byte r15 = (byte) r15
                com.google.protobuf.UnsafeUtil.putByte(r1, r4, r15)
                long r4 = r13 + r11
                int r15 = r2 >>> 12
                r15 = r15 & 63
                r11 = 128(0x80, float:1.794E-43)
                r12 = r15 | 128(0x80, float:1.794E-43)
                byte r12 = (byte) r12
                com.google.protobuf.UnsafeUtil.putByte(r1, r13, r12)
                r12 = 1
                long r14 = r4 + r12
                int r16 = r2 >>> 6
                r12 = r16 & 63
                r12 = r12 | r11
                byte r12 = (byte) r12
                com.google.protobuf.UnsafeUtil.putByte(r1, r4, r12)
                r4 = 1
                long r12 = r14 + r4
                r2 = r2 & 63
                r2 = r2 | r11
                byte r2 = (byte) r2
                com.google.protobuf.UnsafeUtil.putByte(r1, r14, r2)
                r2 = r3
            L_0x0106:
                int r2 = r2 + 1
                r3 = r11
                r18 = r4
                r4 = r12
                r11 = r18
                goto L_0x003d
            L_0x0110:
                r2 = r3
            L_0x0111:
                com.google.protobuf.Utf8$UnpairedSurrogateException r0 = new com.google.protobuf.Utf8$UnpairedSurrogateException
                int r2 = r2 + -1
                r0.<init>(r2, r8)
                throw r0
            L_0x0119:
                if (r14 > r13) goto L_0x0131
                if (r13 > r3) goto L_0x0131
                int r1 = r2 + 1
                if (r1 == r8) goto L_0x012b
                char r0 = r0.charAt(r1)
                boolean r0 = java.lang.Character.isSurrogatePair(r13, r0)
                if (r0 != 0) goto L_0x0131
            L_0x012b:
                com.google.protobuf.Utf8$UnpairedSurrogateException r0 = new com.google.protobuf.Utf8$UnpairedSurrogateException
                r0.<init>(r2, r8)
                throw r0
            L_0x0131:
                java.lang.ArrayIndexOutOfBoundsException r0 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>((java.lang.String) r10)
                java.lang.StringBuilder r1 = r1.append((char) r13)
                java.lang.StringBuilder r1 = r1.append((java.lang.String) r9)
                java.lang.StringBuilder r1 = r1.append((long) r4)
                java.lang.String r1 = r1.toString()
                r0.<init>((java.lang.String) r1)
                throw r0
            L_0x014c:
                long r0 = com.google.protobuf.UnsafeUtil.getArrayBaseOffset()
                goto L_0x003a
            L_0x0152:
                java.lang.ArrayIndexOutOfBoundsException r1 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>((java.lang.String) r10)
                int r8 = r8 + -1
                char r0 = r0.charAt(r8)
                java.lang.StringBuilder r0 = r4.append((char) r0)
                java.lang.StringBuilder r0 = r0.append((java.lang.String) r9)
                int r2 = r2 + r3
                java.lang.StringBuilder r0 = r0.append((int) r2)
                java.lang.String r0 = r0.toString()
                r1.<init>((java.lang.String) r0)
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.encodeUtf8(java.lang.CharSequence, byte[], int, int):int");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:11:0x0042 A[LOOP:1: B:11:0x0042->B:36:0x0107, LOOP_START, PHI: r2 r4 r6 r9 r12 
          PHI: (r2v2 long) = (r2v0 long), (r2v3 long) binds: [B:8:0x003a, B:36:0x0107] A[DONT_GENERATE, DONT_INLINE]
          PHI: (r4v4 long) = (r4v3 long), (r4v6 long) binds: [B:8:0x003a, B:36:0x0107] A[DONT_GENERATE, DONT_INLINE]
          PHI: (r6v3 long) = (r6v2 long), (r6v4 long) binds: [B:8:0x003a, B:36:0x0107] A[DONT_GENERATE, DONT_INLINE]
          PHI: (r9v4 int) = (r9v3 int), (r9v6 int) binds: [B:8:0x003a, B:36:0x0107] A[DONT_GENERATE, DONT_INLINE]
          PHI: (r12v1 char) = (r12v0 char), (r12v2 char) binds: [B:8:0x003a, B:36:0x0107] A[DONT_GENERATE, DONT_INLINE]] */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x003c  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void encodeUtf8Direct(java.lang.CharSequence r21, java.nio.ByteBuffer r22) {
            /*
                r20 = this;
                r0 = r21
                r1 = r22
                long r2 = com.google.protobuf.UnsafeUtil.addressOffset(r22)
                int r4 = r22.position()
                long r4 = (long) r4
                long r4 = r4 + r2
                int r6 = r22.limit()
                long r6 = (long) r6
                long r6 = r6 + r2
                int r8 = r21.length()
                long r9 = (long) r8
                long r11 = r6 - r4
                int r9 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
                java.lang.String r10 = " at index "
                java.lang.String r11 = "Failed writing "
                if (r9 > 0) goto L_0x015a
                r9 = 0
            L_0x0024:
                r12 = 128(0x80, float:1.794E-43)
                r13 = 1
                if (r9 >= r8) goto L_0x003a
                char r15 = r0.charAt(r9)
                if (r15 >= r12) goto L_0x003a
                long r12 = r4 + r13
                byte r14 = (byte) r15
                com.google.protobuf.UnsafeUtil.putByte(r4, r14)
                int r9 = r9 + 1
                r4 = r12
                goto L_0x0024
            L_0x003a:
                if (r9 != r8) goto L_0x0042
                long r4 = r4 - r2
                int r0 = (int) r4
                r1.position((int) r0)
                return
            L_0x0042:
                if (r9 >= r8) goto L_0x014f
                char r15 = r0.charAt(r9)
                if (r15 >= r12) goto L_0x005e
                int r16 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
                if (r16 >= 0) goto L_0x005e
                long r16 = r4 + r13
                byte r15 = (byte) r15
                com.google.protobuf.UnsafeUtil.putByte(r4, r15)
                r18 = r6
                r1 = r9
                r9 = r12
                r4 = r16
                r16 = r2
                goto L_0x0107
            L_0x005e:
                r12 = 2048(0x800, float:2.87E-42)
                if (r15 >= r12) goto L_0x0089
                r16 = 2
                long r16 = r6 - r16
                int r12 = (r4 > r16 ? 1 : (r4 == r16 ? 0 : -1))
                if (r12 > 0) goto L_0x0089
                r16 = r2
                long r1 = r4 + r13
                int r3 = r15 >>> 6
                r3 = r3 | 960(0x3c0, float:1.345E-42)
                byte r3 = (byte) r3
                com.google.protobuf.UnsafeUtil.putByte(r4, r3)
                long r3 = r1 + r13
                r5 = r15 & 63
                r12 = 128(0x80, float:1.794E-43)
                r5 = r5 | r12
                byte r5 = (byte) r5
                com.google.protobuf.UnsafeUtil.putByte(r1, r5)
                r4 = r3
            L_0x0082:
                r18 = r6
                r1 = r9
                r9 = 128(0x80, float:1.794E-43)
                goto L_0x0107
            L_0x0089:
                r16 = r2
                r1 = 57343(0xdfff, float:8.0355E-41)
                r2 = 55296(0xd800, float:7.7486E-41)
                if (r15 < r2) goto L_0x0095
                if (r1 >= r15) goto L_0x00bf
            L_0x0095:
                r18 = 3
                long r18 = r6 - r18
                int r3 = (r4 > r18 ? 1 : (r4 == r18 ? 0 : -1))
                if (r3 > 0) goto L_0x00bf
                long r1 = r4 + r13
                int r3 = r15 >>> 12
                r3 = r3 | 480(0x1e0, float:6.73E-43)
                byte r3 = (byte) r3
                com.google.protobuf.UnsafeUtil.putByte(r4, r3)
                long r3 = r1 + r13
                int r5 = r15 >>> 6
                r5 = r5 & 63
                r12 = 128(0x80, float:1.794E-43)
                r5 = r5 | r12
                byte r5 = (byte) r5
                com.google.protobuf.UnsafeUtil.putByte(r1, r5)
                long r1 = r3 + r13
                r5 = r15 & 63
                r5 = r5 | r12
                byte r5 = (byte) r5
                com.google.protobuf.UnsafeUtil.putByte(r3, r5)
                r4 = r1
                goto L_0x0082
            L_0x00bf:
                r18 = 4
                long r18 = r6 - r18
                int r3 = (r4 > r18 ? 1 : (r4 == r18 ? 0 : -1))
                if (r3 > 0) goto L_0x011c
                int r1 = r9 + 1
                if (r1 == r8) goto L_0x0114
                char r2 = r0.charAt(r1)
                boolean r3 = java.lang.Character.isSurrogatePair(r15, r2)
                if (r3 == 0) goto L_0x0113
                int r2 = java.lang.Character.toCodePoint(r15, r2)
                r18 = r6
                long r6 = r4 + r13
                int r3 = r2 >>> 18
                r3 = r3 | 240(0xf0, float:3.36E-43)
                byte r3 = (byte) r3
                com.google.protobuf.UnsafeUtil.putByte(r4, r3)
                long r3 = r6 + r13
                int r5 = r2 >>> 12
                r5 = r5 & 63
                r9 = 128(0x80, float:1.794E-43)
                r5 = r5 | r9
                byte r5 = (byte) r5
                com.google.protobuf.UnsafeUtil.putByte(r6, r5)
                long r5 = r3 + r13
                int r7 = r2 >>> 6
                r7 = r7 & 63
                r7 = r7 | r9
                byte r7 = (byte) r7
                com.google.protobuf.UnsafeUtil.putByte(r3, r7)
                long r3 = r5 + r13
                r2 = r2 & 63
                r2 = r2 | r9
                byte r2 = (byte) r2
                com.google.protobuf.UnsafeUtil.putByte(r5, r2)
                r4 = r3
            L_0x0107:
                int r1 = r1 + 1
                r12 = r9
                r2 = r16
                r6 = r18
                r9 = r1
                r1 = r22
                goto L_0x0042
            L_0x0113:
                r9 = r1
            L_0x0114:
                com.google.protobuf.Utf8$UnpairedSurrogateException r0 = new com.google.protobuf.Utf8$UnpairedSurrogateException
                int r9 = r9 + -1
                r0.<init>(r9, r8)
                throw r0
            L_0x011c:
                if (r2 > r15) goto L_0x0134
                if (r15 > r1) goto L_0x0134
                int r1 = r9 + 1
                if (r1 == r8) goto L_0x012e
                char r0 = r0.charAt(r1)
                boolean r0 = java.lang.Character.isSurrogatePair(r15, r0)
                if (r0 != 0) goto L_0x0134
            L_0x012e:
                com.google.protobuf.Utf8$UnpairedSurrogateException r0 = new com.google.protobuf.Utf8$UnpairedSurrogateException
                r0.<init>(r9, r8)
                throw r0
            L_0x0134:
                java.lang.ArrayIndexOutOfBoundsException r0 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>((java.lang.String) r11)
                java.lang.StringBuilder r1 = r1.append((char) r15)
                java.lang.StringBuilder r1 = r1.append((java.lang.String) r10)
                java.lang.StringBuilder r1 = r1.append((long) r4)
                java.lang.String r1 = r1.toString()
                r0.<init>((java.lang.String) r1)
                throw r0
            L_0x014f:
                r16 = r2
                long r4 = r4 - r16
                int r0 = (int) r4
                r1 = r22
                r1.position((int) r0)
                return
            L_0x015a:
                java.lang.ArrayIndexOutOfBoundsException r2 = new java.lang.ArrayIndexOutOfBoundsException
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>((java.lang.String) r11)
                int r8 = r8 + -1
                char r0 = r0.charAt(r8)
                java.lang.StringBuilder r0 = r3.append((char) r0)
                java.lang.StringBuilder r0 = r0.append((java.lang.String) r10)
                int r1 = r22.limit()
                java.lang.StringBuilder r0 = r0.append((int) r1)
                java.lang.String r0 = r0.toString()
                r2.<init>((java.lang.String) r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.encodeUtf8Direct(java.lang.CharSequence, java.nio.ByteBuffer):void");
        }

        private static int unsafeEstimateConsecutiveAscii(byte[] bArr, long j, int i) {
            if (i < 16) {
                return 0;
            }
            int i2 = ((int) j) & 7;
            int i3 = i2;
            while (i3 > 0) {
                long j2 = 1 + j;
                if (UnsafeUtil.getByte(bArr, j) < 0) {
                    return i2 - i3;
                }
                i3--;
                j = j2;
            }
            int i4 = i - i2;
            while (i4 >= 8 && (UnsafeUtil.getLong(bArr, j) & Utf8.ASCII_MASK_LONG) == 0) {
                j += 8;
                i4 -= 8;
            }
            return i - i4;
        }

        private static int unsafeEstimateConsecutiveAscii(long j, int i) {
            if (i < 16) {
                return 0;
            }
            int i2 = ((int) j) & 7;
            int i3 = i2;
            while (i3 > 0) {
                long j2 = 1 + j;
                if (UnsafeUtil.getByte(j) < 0) {
                    return i2 - i3;
                }
                i3--;
                j = j2;
            }
            int i4 = i - i2;
            while (i4 >= 8 && (UnsafeUtil.getLong(j) & Utf8.ASCII_MASK_LONG) == 0) {
                j += 8;
                i4 -= 8;
            }
            return i - i4;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0039, code lost:
            return -1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0063, code lost:
            return -1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static int partialIsValidUtf8(byte[] r8, long r9, int r11) {
            /*
                int r0 = unsafeEstimateConsecutiveAscii(r8, r9, r11)
                int r11 = r11 - r0
                long r0 = (long) r0
                long r9 = r9 + r0
            L_0x0007:
                r0 = 0
                r1 = r0
            L_0x0009:
                r2 = 1
                if (r11 <= 0) goto L_0x001a
                long r4 = r9 + r2
                byte r1 = com.google.protobuf.UnsafeUtil.getByte(r8, r9)
                if (r1 < 0) goto L_0x0019
                int r11 = r11 + -1
                r9 = r4
                goto L_0x0009
            L_0x0019:
                r9 = r4
            L_0x001a:
                if (r11 != 0) goto L_0x001d
                return r0
            L_0x001d:
                int r11 = r11 + -1
                r0 = -32
                r4 = -65
                r5 = -1
                if (r1 >= r0) goto L_0x003a
                if (r11 != 0) goto L_0x0029
                return r1
            L_0x0029:
                int r11 = r11 + -1
                r0 = -62
                if (r1 < r0) goto L_0x0039
                long r2 = r2 + r9
                byte r9 = com.google.protobuf.UnsafeUtil.getByte(r8, r9)
                if (r9 <= r4) goto L_0x0037
                goto L_0x0039
            L_0x0037:
                r9 = r2
                goto L_0x0007
            L_0x0039:
                return r5
            L_0x003a:
                r6 = -16
                if (r1 >= r6) goto L_0x0064
                r6 = 2
                if (r11 >= r6) goto L_0x0046
                int r8 = unsafeIncompleteStateFor(r8, r1, r9, r11)
                return r8
            L_0x0046:
                int r11 = r11 + -2
                long r6 = r9 + r2
                byte r9 = com.google.protobuf.UnsafeUtil.getByte(r8, r9)
                if (r9 > r4) goto L_0x0063
                r10 = -96
                if (r1 != r0) goto L_0x0056
                if (r9 < r10) goto L_0x0063
            L_0x0056:
                r0 = -19
                if (r1 != r0) goto L_0x005c
                if (r9 >= r10) goto L_0x0063
            L_0x005c:
                long r2 = r2 + r6
                byte r9 = com.google.protobuf.UnsafeUtil.getByte(r8, r6)
                if (r9 <= r4) goto L_0x0037
            L_0x0063:
                return r5
            L_0x0064:
                r0 = 3
                if (r11 >= r0) goto L_0x006c
                int r8 = unsafeIncompleteStateFor(r8, r1, r9, r11)
                return r8
            L_0x006c:
                int r11 = r11 + -3
                long r6 = r9 + r2
                byte r9 = com.google.protobuf.UnsafeUtil.getByte(r8, r9)
                if (r9 > r4) goto L_0x008e
                int r10 = r1 << 28
                int r9 = r9 + 112
                int r10 = r10 + r9
                int r9 = r10 >> 30
                if (r9 != 0) goto L_0x008e
                long r9 = r6 + r2
                byte r0 = com.google.protobuf.UnsafeUtil.getByte(r8, r6)
                if (r0 > r4) goto L_0x008e
                long r2 = r2 + r9
                byte r9 = com.google.protobuf.UnsafeUtil.getByte(r8, r9)
                if (r9 <= r4) goto L_0x0037
            L_0x008e:
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.partialIsValidUtf8(byte[], long, int):int");
        }

        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0039, code lost:
            return -1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x0063, code lost:
            return -1;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private static int partialIsValidUtf8(long r8, int r10) {
            /*
                int r0 = unsafeEstimateConsecutiveAscii(r8, r10)
                long r1 = (long) r0
                long r8 = r8 + r1
                int r10 = r10 - r0
            L_0x0007:
                r0 = 0
                r1 = r0
            L_0x0009:
                r2 = 1
                if (r10 <= 0) goto L_0x001a
                long r4 = r8 + r2
                byte r1 = com.google.protobuf.UnsafeUtil.getByte(r8)
                if (r1 < 0) goto L_0x0019
                int r10 = r10 + -1
                r8 = r4
                goto L_0x0009
            L_0x0019:
                r8 = r4
            L_0x001a:
                if (r10 != 0) goto L_0x001d
                return r0
            L_0x001d:
                int r10 = r10 + -1
                r0 = -32
                r4 = -65
                r5 = -1
                if (r1 >= r0) goto L_0x003a
                if (r10 != 0) goto L_0x0029
                return r1
            L_0x0029:
                int r10 = r10 + -1
                r0 = -62
                if (r1 < r0) goto L_0x0039
                long r2 = r2 + r8
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r8)
                if (r8 <= r4) goto L_0x0037
                goto L_0x0039
            L_0x0037:
                r8 = r2
                goto L_0x0007
            L_0x0039:
                return r5
            L_0x003a:
                r6 = -16
                if (r1 >= r6) goto L_0x0064
                r6 = 2
                if (r10 >= r6) goto L_0x0046
                int r8 = unsafeIncompleteStateFor(r8, r1, r10)
                return r8
            L_0x0046:
                int r10 = r10 + -2
                long r6 = r8 + r2
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r8)
                if (r8 > r4) goto L_0x0063
                r9 = -96
                if (r1 != r0) goto L_0x0056
                if (r8 < r9) goto L_0x0063
            L_0x0056:
                r0 = -19
                if (r1 != r0) goto L_0x005c
                if (r8 >= r9) goto L_0x0063
            L_0x005c:
                long r2 = r2 + r6
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r6)
                if (r8 <= r4) goto L_0x0037
            L_0x0063:
                return r5
            L_0x0064:
                r0 = 3
                if (r10 >= r0) goto L_0x006c
                int r8 = unsafeIncompleteStateFor(r8, r1, r10)
                return r8
            L_0x006c:
                int r10 = r10 + -3
                long r6 = r8 + r2
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r8)
                if (r8 > r4) goto L_0x008e
                int r9 = r1 << 28
                int r8 = r8 + 112
                int r9 = r9 + r8
                int r8 = r9 >> 30
                if (r8 != 0) goto L_0x008e
                long r8 = r6 + r2
                byte r0 = com.google.protobuf.UnsafeUtil.getByte(r6)
                if (r0 > r4) goto L_0x008e
                long r2 = r2 + r8
                byte r8 = com.google.protobuf.UnsafeUtil.getByte(r8)
                if (r8 <= r4) goto L_0x0037
            L_0x008e:
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.Utf8.UnsafeProcessor.partialIsValidUtf8(long, int):int");
        }

        private static int unsafeIncompleteStateFor(byte[] bArr, int i, long j, int i2) {
            if (i2 == 0) {
                return Utf8.incompleteStateFor(i);
            }
            if (i2 == 1) {
                return Utf8.incompleteStateFor(i, UnsafeUtil.getByte(bArr, j));
            }
            if (i2 == 2) {
                return Utf8.incompleteStateFor(i, (int) UnsafeUtil.getByte(bArr, j), (int) UnsafeUtil.getByte(bArr, j + 1));
            }
            throw new AssertionError();
        }

        private static int unsafeIncompleteStateFor(long j, int i, int i2) {
            if (i2 == 0) {
                return Utf8.incompleteStateFor(i);
            }
            if (i2 == 1) {
                return Utf8.incompleteStateFor(i, UnsafeUtil.getByte(j));
            }
            if (i2 == 2) {
                return Utf8.incompleteStateFor(i, (int) UnsafeUtil.getByte(j), (int) UnsafeUtil.getByte(j + 1));
            }
            throw new AssertionError();
        }
    }

    private Utf8() {
    }
}
