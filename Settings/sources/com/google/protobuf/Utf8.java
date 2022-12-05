package com.google.protobuf;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class Utf8 {
    private static final Processor processor;

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int i) {
        if (i > -12) {
            return -1;
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int i, int i2) {
        if (i > -12 || i2 > -65) {
            return -1;
        }
        return i ^ (i2 << 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(int i, int i2, int i3) {
        if (i > -12 || i2 > -65 || i3 > -65) {
            return -1;
        }
        return (i ^ (i2 << 8)) ^ (i3 << 16);
    }

    static {
        Processor safeProcessor;
        if (UnsafeProcessor.isAvailable() && !Android.isOnAndroidDevice()) {
            safeProcessor = new UnsafeProcessor();
        } else {
            safeProcessor = new SafeProcessor();
        }
        processor = safeProcessor;
    }

    public static boolean isValidUtf8(byte[] bArr) {
        return processor.isValidUtf8(bArr, 0, bArr.length);
    }

    public static boolean isValidUtf8(byte[] bArr, int i, int i2) {
        return processor.isValidUtf8(bArr, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int incompleteStateFor(byte[] bArr, int i, int i2) {
        byte b = bArr[i - 1];
        int i3 = i2 - i;
        if (i3 != 0) {
            if (i3 == 1) {
                return incompleteStateFor(b, bArr[i]);
            }
            if (i3 == 2) {
                return incompleteStateFor(b, bArr[i], bArr[i + 1]);
            }
            throw new AssertionError();
        }
        return incompleteStateFor(b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class UnpairedSurrogateException extends IllegalArgumentException {
        UnpairedSurrogateException(int i, int i2) {
            super("Unpaired surrogate at index " + i + " of " + i2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int encodedLength(CharSequence charSequence) {
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
        throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (i2 + 4294967296L));
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
                    if (Character.codePointAt(charSequence, i) < 65536) {
                        throw new UnpairedSurrogateException(i, length);
                    }
                    i++;
                }
            }
            i++;
        }
        return i2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int encode(CharSequence charSequence, byte[] bArr, int i, int i2) {
        return processor.encodeUtf8(charSequence, bArr, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String decodeUtf8(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException {
        return processor.decodeUtf8(bArr, i, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static abstract class Processor {
        abstract String decodeUtf8(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

        abstract int encodeUtf8(CharSequence charSequence, byte[] bArr, int i, int i2);

        abstract int partialIsValidUtf8(int i, byte[] bArr, int i2, int i3);

        Processor() {
        }

        final boolean isValidUtf8(byte[] bArr, int i, int i2) {
            return partialIsValidUtf8(0, bArr, i, i2) == 0;
        }
    }

    /* loaded from: classes2.dex */
    static final class SafeProcessor extends Processor {
        SafeProcessor() {
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0015, code lost:
            if (r7[r8] > (-65)) goto L12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:30:0x0046, code lost:
            if (r7[r8] > (-65)) goto L31;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x007f, code lost:
            if (r7[r8] > (-65)) goto L51;
         */
        @Override // com.google.protobuf.Utf8.Processor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        int partialIsValidUtf8(int i, byte[] bArr, int i2, int i3) {
            int i4;
            if (i != 0) {
                if (i2 >= i3) {
                    return i;
                }
                byte b = (byte) i;
                if (b < -32) {
                    if (b >= -62) {
                        i4 = i2 + 1;
                    }
                    return -1;
                } else if (b < -16) {
                    byte b2 = (byte) (~(i >> 8));
                    if (b2 == 0) {
                        int i5 = i2 + 1;
                        byte b3 = bArr[i2];
                        if (i5 >= i3) {
                            return Utf8.incompleteStateFor(b, b3);
                        }
                        i2 = i5;
                        b2 = b3;
                    }
                    if (b2 <= -65 && ((b != -32 || b2 >= -96) && (b != -19 || b2 < -96))) {
                        i4 = i2 + 1;
                    }
                    return -1;
                } else {
                    byte b4 = (byte) (~(i >> 8));
                    byte b5 = 0;
                    if (b4 == 0) {
                        int i6 = i2 + 1;
                        b4 = bArr[i2];
                        if (i6 >= i3) {
                            return Utf8.incompleteStateFor(b, b4);
                        }
                        i2 = i6;
                    } else {
                        b5 = (byte) (i >> 16);
                    }
                    if (b5 == 0) {
                        int i7 = i2 + 1;
                        b5 = bArr[i2];
                        if (i7 >= i3) {
                            return Utf8.incompleteStateFor(b, b4, b5);
                        }
                        i2 = i7;
                    }
                    if (b4 <= -65 && (((b << 28) + (b4 + 112)) >> 30) == 0 && b5 <= -65) {
                        i4 = i2 + 1;
                    }
                    return -1;
                }
                i2 = i4;
            }
            return partialIsValidUtf8(bArr, i2, i3);
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException {
            if ((i | i2 | ((bArr.length - i) - i2)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)));
            }
            int i3 = i + i2;
            char[] cArr = new char[i2];
            int i4 = 0;
            while (i < i3) {
                byte b = bArr[i];
                if (!DecodeUtil.isOneByte(b)) {
                    break;
                }
                i++;
                DecodeUtil.handleOneByte(b, cArr, i4);
                i4++;
            }
            int i5 = i4;
            while (i < i3) {
                int i6 = i + 1;
                byte b2 = bArr[i];
                if (DecodeUtil.isOneByte(b2)) {
                    int i7 = i5 + 1;
                    DecodeUtil.handleOneByte(b2, cArr, i5);
                    while (i6 < i3) {
                        byte b3 = bArr[i6];
                        if (!DecodeUtil.isOneByte(b3)) {
                            break;
                        }
                        i6++;
                        DecodeUtil.handleOneByte(b3, cArr, i7);
                        i7++;
                    }
                    i = i6;
                    i5 = i7;
                } else if (DecodeUtil.isTwoBytes(b2)) {
                    if (i6 >= i3) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    DecodeUtil.handleTwoBytes(b2, bArr[i6], cArr, i5);
                    i = i6 + 1;
                    i5++;
                } else if (DecodeUtil.isThreeBytes(b2)) {
                    if (i6 >= i3 - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    int i8 = i6 + 1;
                    DecodeUtil.handleThreeBytes(b2, bArr[i6], bArr[i8], cArr, i5);
                    i = i8 + 1;
                    i5++;
                } else if (i6 >= i3 - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    int i9 = i6 + 1;
                    byte b4 = bArr[i6];
                    int i10 = i9 + 1;
                    DecodeUtil.handleFourBytes(b2, b4, bArr[i9], bArr[i10], cArr, i5);
                    i = i10 + 1;
                    i5 = i5 + 1 + 1;
                }
            }
            return new String(cArr, 0, i5);
        }

        /* JADX WARN: Code restructure failed: missing block: B:12:0x001d, code lost:
            return r9 + r6;
         */
        @Override // com.google.protobuf.Utf8.Processor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        int encodeUtf8(CharSequence charSequence, byte[] bArr, int i, int i2) {
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
            int i8 = i + i7;
            while (i7 < length) {
                char charAt2 = charSequence.charAt(i7);
                if (charAt2 >= 128 || i8 >= i6) {
                    if (charAt2 < 2048 && i8 <= i6 - 2) {
                        int i9 = i8 + 1;
                        bArr[i8] = (byte) ((charAt2 >>> 6) | 960);
                        i8 = i9 + 1;
                        bArr[i9] = (byte) ((charAt2 & '?') | 128);
                    } else if ((charAt2 >= 55296 && 57343 >= charAt2) || i8 > i6 - 3) {
                        if (i8 <= i6 - 4) {
                            int i10 = i7 + 1;
                            if (i10 != charSequence.length()) {
                                char charAt3 = charSequence.charAt(i10);
                                if (Character.isSurrogatePair(charAt2, charAt3)) {
                                    int codePoint = Character.toCodePoint(charAt2, charAt3);
                                    int i11 = i8 + 1;
                                    bArr[i8] = (byte) ((codePoint >>> 18) | 240);
                                    int i12 = i11 + 1;
                                    bArr[i11] = (byte) (((codePoint >>> 12) & 63) | 128);
                                    int i13 = i12 + 1;
                                    bArr[i12] = (byte) (((codePoint >>> 6) & 63) | 128);
                                    i8 = i13 + 1;
                                    bArr[i13] = (byte) ((codePoint & 63) | 128);
                                    i7 = i10;
                                } else {
                                    i7 = i10;
                                }
                            }
                            throw new UnpairedSurrogateException(i7 - 1, length);
                        } else if (55296 <= charAt2 && charAt2 <= 57343 && ((i4 = i7 + 1) == charSequence.length() || !Character.isSurrogatePair(charAt2, charSequence.charAt(i4)))) {
                            throw new UnpairedSurrogateException(i7, length);
                        } else {
                            throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + i8);
                        }
                    } else {
                        int i14 = i8 + 1;
                        bArr[i8] = (byte) ((charAt2 >>> '\f') | 480);
                        int i15 = i14 + 1;
                        bArr[i14] = (byte) (((charAt2 >>> 6) & 63) | 128);
                        i3 = i15 + 1;
                        bArr[i15] = (byte) ((charAt2 & '?') | 128);
                    }
                    i7++;
                } else {
                    i3 = i8 + 1;
                    bArr[i8] = (byte) charAt2;
                }
                i8 = i3;
                i7++;
            }
            return i8;
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
                    } else if (b >= -16) {
                        if (i3 >= i2 - 2) {
                            return Utf8.incompleteStateFor(bArr, i3, i2);
                        }
                        int i4 = i3 + 1;
                        byte b2 = bArr[i3];
                        if (b2 <= -65 && (((b << 28) + (b2 + 112)) >> 30) == 0) {
                            int i5 = i4 + 1;
                            if (bArr[i4] <= -65) {
                                i3 = i5 + 1;
                                if (bArr[i5] > -65) {
                                }
                            }
                        }
                        return -1;
                    } else if (i3 >= i2 - 1) {
                        return Utf8.incompleteStateFor(bArr, i3, i2);
                    } else {
                        int i6 = i3 + 1;
                        byte b3 = bArr[i3];
                        if (b3 <= -65 && ((b != -32 || b3 >= -96) && (b != -19 || b3 < -96))) {
                            i = i6 + 1;
                            if (bArr[i6] > -65) {
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

    /* loaded from: classes2.dex */
    static final class UnsafeProcessor extends Processor {
        UnsafeProcessor() {
        }

        static boolean isAvailable() {
            return UnsafeUtil.hasUnsafeArrayOperations() && UnsafeUtil.hasUnsafeByteBufferOperations();
        }

        /* JADX WARN: Code restructure failed: missing block: B:34:0x0059, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r12, r1) > (-65)) goto L36;
         */
        /* JADX WARN: Code restructure failed: missing block: B:55:0x009e, code lost:
            if (com.google.protobuf.UnsafeUtil.getByte(r12, r1) > (-65)) goto L56;
         */
        @Override // com.google.protobuf.Utf8.Processor
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        int partialIsValidUtf8(int i, byte[] bArr, int i2, int i3) {
            long j;
            byte b = 0;
            if ((i2 | i3 | (bArr.length - i3)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("Array length=%d, index=%d, limit=%d", Integer.valueOf(bArr.length), Integer.valueOf(i2), Integer.valueOf(i3)));
            }
            long j2 = i2;
            long j3 = i3;
            if (i != 0) {
                if (j2 >= j3) {
                    return i;
                }
                byte b2 = (byte) i;
                if (b2 < -32) {
                    if (b2 >= -62) {
                        long j4 = 1 + j2;
                        if (UnsafeUtil.getByte(bArr, j2) <= -65) {
                            j2 = j4;
                        }
                    }
                    return -1;
                }
                if (b2 < -16) {
                    byte b3 = (byte) (~(i >> 8));
                    if (b3 == 0) {
                        long j5 = j2 + 1;
                        b3 = UnsafeUtil.getByte(bArr, j2);
                        if (j5 >= j3) {
                            return Utf8.incompleteStateFor(b2, b3);
                        }
                        j2 = j5;
                    }
                    if (b3 <= -65 && ((b2 != -32 || b3 >= -96) && (b2 != -19 || b3 < -96))) {
                        j = j2 + 1;
                    }
                    return -1;
                }
                byte b4 = (byte) (~(i >> 8));
                if (b4 == 0) {
                    long j6 = j2 + 1;
                    b4 = UnsafeUtil.getByte(bArr, j2);
                    if (j6 >= j3) {
                        return Utf8.incompleteStateFor(b2, b4);
                    }
                    j2 = j6;
                } else {
                    b = (byte) (i >> 16);
                }
                if (b == 0) {
                    long j7 = j2 + 1;
                    b = UnsafeUtil.getByte(bArr, j2);
                    if (j7 >= j3) {
                        return Utf8.incompleteStateFor(b2, b4, b);
                    }
                    j2 = j7;
                }
                if (b4 <= -65 && (((b2 << 28) + (b4 + 112)) >> 30) == 0 && b <= -65) {
                    j = j2 + 1;
                }
                return -1;
                j2 = j;
            }
            return partialIsValidUtf8(bArr, j2, (int) (j3 - j2));
        }

        @Override // com.google.protobuf.Utf8.Processor
        String decodeUtf8(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException {
            if ((i | i2 | ((bArr.length - i) - i2)) < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("buffer length=%d, index=%d, size=%d", Integer.valueOf(bArr.length), Integer.valueOf(i), Integer.valueOf(i2)));
            }
            int i3 = i + i2;
            char[] cArr = new char[i2];
            int i4 = 0;
            while (i < i3) {
                byte b = UnsafeUtil.getByte(bArr, i);
                if (!DecodeUtil.isOneByte(b)) {
                    break;
                }
                i++;
                DecodeUtil.handleOneByte(b, cArr, i4);
                i4++;
            }
            int i5 = i4;
            while (i < i3) {
                int i6 = i + 1;
                byte b2 = UnsafeUtil.getByte(bArr, i);
                if (DecodeUtil.isOneByte(b2)) {
                    int i7 = i5 + 1;
                    DecodeUtil.handleOneByte(b2, cArr, i5);
                    while (i6 < i3) {
                        byte b3 = UnsafeUtil.getByte(bArr, i6);
                        if (!DecodeUtil.isOneByte(b3)) {
                            break;
                        }
                        i6++;
                        DecodeUtil.handleOneByte(b3, cArr, i7);
                        i7++;
                    }
                    i = i6;
                    i5 = i7;
                } else if (DecodeUtil.isTwoBytes(b2)) {
                    if (i6 >= i3) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    DecodeUtil.handleTwoBytes(b2, UnsafeUtil.getByte(bArr, i6), cArr, i5);
                    i = i6 + 1;
                    i5++;
                } else if (DecodeUtil.isThreeBytes(b2)) {
                    if (i6 >= i3 - 1) {
                        throw InvalidProtocolBufferException.invalidUtf8();
                    }
                    int i8 = i6 + 1;
                    DecodeUtil.handleThreeBytes(b2, UnsafeUtil.getByte(bArr, i6), UnsafeUtil.getByte(bArr, i8), cArr, i5);
                    i = i8 + 1;
                    i5++;
                } else if (i6 >= i3 - 2) {
                    throw InvalidProtocolBufferException.invalidUtf8();
                } else {
                    int i9 = i6 + 1;
                    int i10 = i9 + 1;
                    DecodeUtil.handleFourBytes(b2, UnsafeUtil.getByte(bArr, i6), UnsafeUtil.getByte(bArr, i9), UnsafeUtil.getByte(bArr, i10), cArr, i5);
                    i = i10 + 1;
                    i5 = i5 + 1 + 1;
                }
            }
            return new String(cArr, 0, i5);
        }

        @Override // com.google.protobuf.Utf8.Processor
        int encodeUtf8(CharSequence charSequence, byte[] bArr, int i, int i2) {
            char c;
            long j;
            long j2;
            long j3;
            char c2;
            int i3;
            char charAt;
            long j4 = i;
            long j5 = i2 + j4;
            int length = charSequence.length();
            if (length > i2 || bArr.length - i2 < i) {
                throw new ArrayIndexOutOfBoundsException("Failed writing " + charSequence.charAt(length - 1) + " at index " + (i + i2));
            }
            int i4 = 0;
            while (true) {
                c = 128;
                j = 1;
                if (i4 >= length || (charAt = charSequence.charAt(i4)) >= 128) {
                    break;
                }
                UnsafeUtil.putByte(bArr, j4, (byte) charAt);
                i4++;
                j4 = 1 + j4;
            }
            if (i4 == length) {
                return (int) j4;
            }
            while (i4 < length) {
                char charAt2 = charSequence.charAt(i4);
                if (charAt2 < c && j4 < j5) {
                    long j6 = j4 + j;
                    UnsafeUtil.putByte(bArr, j4, (byte) charAt2);
                    j3 = j;
                    j2 = j6;
                    c2 = c;
                } else if (charAt2 < 2048 && j4 <= j5 - 2) {
                    long j7 = j4 + j;
                    UnsafeUtil.putByte(bArr, j4, (byte) ((charAt2 >>> 6) | 960));
                    long j8 = j7 + j;
                    UnsafeUtil.putByte(bArr, j7, (byte) ((charAt2 & '?') | 128));
                    long j9 = j;
                    c2 = 128;
                    j2 = j8;
                    j3 = j9;
                } else if ((charAt2 >= 55296 && 57343 >= charAt2) || j4 > j5 - 3) {
                    if (j4 <= j5 - 4) {
                        int i5 = i4 + 1;
                        if (i5 != length) {
                            char charAt3 = charSequence.charAt(i5);
                            if (Character.isSurrogatePair(charAt2, charAt3)) {
                                int codePoint = Character.toCodePoint(charAt2, charAt3);
                                long j10 = j4 + 1;
                                UnsafeUtil.putByte(bArr, j4, (byte) ((codePoint >>> 18) | 240));
                                long j11 = j10 + 1;
                                c2 = 128;
                                UnsafeUtil.putByte(bArr, j10, (byte) (((codePoint >>> 12) & 63) | 128));
                                long j12 = j11 + 1;
                                UnsafeUtil.putByte(bArr, j11, (byte) (((codePoint >>> 6) & 63) | 128));
                                j3 = 1;
                                j2 = j12 + 1;
                                UnsafeUtil.putByte(bArr, j12, (byte) ((codePoint & 63) | 128));
                                i4 = i5;
                            } else {
                                i4 = i5;
                            }
                        }
                        throw new UnpairedSurrogateException(i4 - 1, length);
                    } else if (55296 <= charAt2 && charAt2 <= 57343 && ((i3 = i4 + 1) == length || !Character.isSurrogatePair(charAt2, charSequence.charAt(i3)))) {
                        throw new UnpairedSurrogateException(i4, length);
                    } else {
                        throw new ArrayIndexOutOfBoundsException("Failed writing " + charAt2 + " at index " + j4);
                    }
                } else {
                    long j13 = j4 + j;
                    UnsafeUtil.putByte(bArr, j4, (byte) ((charAt2 >>> '\f') | 480));
                    long j14 = j13 + j;
                    UnsafeUtil.putByte(bArr, j13, (byte) (((charAt2 >>> 6) & 63) | 128));
                    UnsafeUtil.putByte(bArr, j14, (byte) ((charAt2 & '?') | 128));
                    j2 = j14 + 1;
                    j3 = 1;
                    c2 = 128;
                }
                i4++;
                c = c2;
                long j15 = j3;
                j4 = j2;
                j = j15;
            }
            return (int) j4;
        }

        private static int unsafeEstimateConsecutiveAscii(byte[] bArr, long j, int i) {
            int i2 = 0;
            if (i < 16) {
                return 0;
            }
            while (i2 < i) {
                long j2 = 1 + j;
                if (UnsafeUtil.getByte(bArr, j) < 0) {
                    return i2;
                }
                i2++;
                j = j2;
            }
            return i;
        }

        /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
            return -1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x008e, code lost:
            return -1;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private static int partialIsValidUtf8(byte[] bArr, long j, int i) {
            long j2;
            int unsafeEstimateConsecutiveAscii = unsafeEstimateConsecutiveAscii(bArr, j, i);
            int i2 = i - unsafeEstimateConsecutiveAscii;
            long j3 = j + unsafeEstimateConsecutiveAscii;
            while (true) {
                byte b = 0;
                while (true) {
                    if (i2 <= 0) {
                        break;
                    }
                    long j4 = j3 + 1;
                    b = UnsafeUtil.getByte(bArr, j3);
                    if (b < 0) {
                        j3 = j4;
                        break;
                    }
                    i2--;
                    j3 = j4;
                }
                if (i2 == 0) {
                    return 0;
                }
                int i3 = i2 - 1;
                if (b >= -32) {
                    if (b >= -16) {
                        if (i3 < 3) {
                            return unsafeIncompleteStateFor(bArr, b, j3, i3);
                        }
                        i2 = i3 - 3;
                        long j5 = j3 + 1;
                        byte b2 = UnsafeUtil.getByte(bArr, j3);
                        if (b2 > -65 || (((b << 28) + (b2 + 112)) >> 30) != 0) {
                            break;
                        }
                        long j6 = j5 + 1;
                        if (UnsafeUtil.getByte(bArr, j5) > -65) {
                            break;
                        }
                        j2 = 1 + j6;
                        if (UnsafeUtil.getByte(bArr, j6) > -65) {
                            break;
                        }
                    } else if (i3 < 2) {
                        return unsafeIncompleteStateFor(bArr, b, j3, i3);
                    } else {
                        i2 = i3 - 2;
                        long j7 = j3 + 1;
                        byte b3 = UnsafeUtil.getByte(bArr, j3);
                        if (b3 > -65 || ((b == -32 && b3 < -96) || (b == -19 && b3 >= -96))) {
                            break;
                        }
                        j2 = 1 + j7;
                        if (UnsafeUtil.getByte(bArr, j7) > -65) {
                            break;
                        }
                    }
                } else if (i3 != 0) {
                    i2 = i3 - 1;
                    if (b < -62) {
                        break;
                    }
                    j2 = 1 + j3;
                    if (UnsafeUtil.getByte(bArr, j3) > -65) {
                        break;
                    }
                } else {
                    return b;
                }
                j3 = j2;
            }
        }

        private static int unsafeIncompleteStateFor(byte[] bArr, int i, long j, int i2) {
            if (i2 != 0) {
                if (i2 == 1) {
                    return Utf8.incompleteStateFor(i, UnsafeUtil.getByte(bArr, j));
                }
                if (i2 == 2) {
                    return Utf8.incompleteStateFor(i, UnsafeUtil.getByte(bArr, j), UnsafeUtil.getByte(bArr, j + 1));
                }
                throw new AssertionError();
            }
            return Utf8.incompleteStateFor(i);
        }
    }

    /* loaded from: classes2.dex */
    private static class DecodeUtil {
        private static char highSurrogate(int i) {
            return (char) ((i >>> 10) + 55232);
        }

        private static boolean isNotTrailingByte(byte b) {
            return b > -65;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isOneByte(byte b) {
            return b >= 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isThreeBytes(byte b) {
            return b < -16;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isTwoBytes(byte b) {
            return b < -32;
        }

        private static char lowSurrogate(int i) {
            return (char) ((i & 1023) + 56320);
        }

        private static int trailingByteValue(byte b) {
            return b & 63;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleOneByte(byte b, char[] cArr, int i) {
            cArr[i] = (char) b;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleTwoBytes(byte b, byte b2, char[] cArr, int i) throws InvalidProtocolBufferException {
            if (b < -62 || isNotTrailingByte(b2)) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            cArr[i] = (char) (((b & 31) << 6) | trailingByteValue(b2));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleThreeBytes(byte b, byte b2, byte b3, char[] cArr, int i) throws InvalidProtocolBufferException {
            if (isNotTrailingByte(b2) || ((b == -32 && b2 < -96) || ((b == -19 && b2 >= -96) || isNotTrailingByte(b3)))) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            cArr[i] = (char) (((b & 15) << 12) | (trailingByteValue(b2) << 6) | trailingByteValue(b3));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void handleFourBytes(byte b, byte b2, byte b3, byte b4, char[] cArr, int i) throws InvalidProtocolBufferException {
            if (isNotTrailingByte(b2) || (((b << 28) + (b2 + 112)) >> 30) != 0 || isNotTrailingByte(b3) || isNotTrailingByte(b4)) {
                throw InvalidProtocolBufferException.invalidUtf8();
            }
            int trailingByteValue = ((b & 7) << 18) | (trailingByteValue(b2) << 12) | (trailingByteValue(b3) << 6) | trailingByteValue(b4);
            cArr[i] = highSurrogate(trailingByteValue);
            cArr[i + 1] = lowSurrogate(trailingByteValue);
        }
    }
}
