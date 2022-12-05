package okio.internal;

import kotlin.Unit;
import org.jetbrains.annotations.NotNull;
/* compiled from: ByteString.kt */
/* loaded from: classes2.dex */
public final class ByteStringKt {
    @NotNull
    private static final char[] HEX_DIGIT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @NotNull
    public static final char[] getHEX_DIGIT_CHARS() {
        return HEX_DIGIT_CHARS;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:174:0x0220 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:240:0x0047 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:267:0x0083 A[EDGE_INSN: B:267:0x0083->B:268:0x0083 ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00df A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0173 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final int codePointIndexToCharIndex(byte[] bArr, int i) {
        boolean z;
        boolean z2;
        int i2;
        boolean z3;
        boolean z4;
        boolean z5;
        int length = bArr.length;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        loop0: while (i3 < length) {
            byte b = bArr[i3];
            if (b >= 0) {
                int i6 = i5 + 1;
                if (i5 == i) {
                    return i4;
                }
                if (b != 10 && b != 13) {
                    if (!(b >= 0 && b <= 31)) {
                        if (!(Byte.MAX_VALUE <= b && b <= 159)) {
                            z2 = false;
                            if (z2) {
                                return -1;
                            }
                        }
                    }
                    z2 = true;
                    if (z2) {
                    }
                }
                if (b == 65533) {
                    return -1;
                }
                i4 += b < 65536 ? 1 : 2;
                i3++;
                while (true) {
                    i5 = i6;
                    if (i3 < length && bArr[i3] >= 0) {
                        int i7 = i3 + 1;
                        byte b2 = bArr[i3];
                        i6 = i5 + 1;
                        if (i5 == i) {
                            return i4;
                        }
                        if (b2 != 10 && b2 != 13) {
                            if (!(b2 >= 0 && b2 <= 31)) {
                                if (!(Byte.MAX_VALUE <= b2 && b2 <= 159)) {
                                    z = false;
                                    if (z) {
                                        break loop0;
                                    }
                                }
                            }
                            z = true;
                            if (z) {
                            }
                        }
                        if (b2 == 65533) {
                            break loop0;
                        }
                        i4 += b2 < 65536 ? 1 : 2;
                        i3 = i7;
                    }
                }
                return -1;
            }
            if ((b >> 5) == -2) {
                int i8 = i3 + 1;
                if (length <= i8) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                byte b3 = bArr[i3];
                byte b4 = bArr[i8];
                if (!((b4 & 192) == 128)) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                int i9 = (b4 ^ 3968) ^ (b3 << 6);
                if (i9 < 128) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                i2 = i5 + 1;
                if (i5 == i) {
                    return i4;
                }
                if (i9 != 10 && i9 != 13) {
                    if (!(i9 >= 0 && i9 <= 31)) {
                        if (!(127 <= i9 && i9 <= 159)) {
                            z5 = false;
                            if (z5) {
                                return -1;
                            }
                        }
                    }
                    z5 = true;
                    if (z5) {
                    }
                }
                if (i9 == 65533) {
                    return -1;
                }
                i4 += i9 < 65536 ? 1 : 2;
                Unit unit = Unit.INSTANCE;
                i3 += 2;
            } else if ((b >> 4) == -2) {
                int i10 = i3 + 2;
                if (length <= i10) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                byte b5 = bArr[i3];
                byte b6 = bArr[i3 + 1];
                if (!((b6 & 192) == 128)) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                byte b7 = bArr[i10];
                if (!((b7 & 192) == 128)) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                int i11 = ((b7 ^ (-123008)) ^ (b6 << 6)) ^ (b5 << 12);
                if (i11 < 2048) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                if (55296 <= i11 && i11 <= 57343) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                i2 = i5 + 1;
                if (i5 == i) {
                    return i4;
                }
                if (i11 != 10 && i11 != 13) {
                    if (!(i11 >= 0 && i11 <= 31)) {
                        if (!(127 <= i11 && i11 <= 159)) {
                            z4 = false;
                            if (z4) {
                                return -1;
                            }
                        }
                    }
                    z4 = true;
                    if (z4) {
                    }
                }
                if (i11 == 65533) {
                    return -1;
                }
                i4 += i11 < 65536 ? 1 : 2;
                Unit unit2 = Unit.INSTANCE;
                i3 += 3;
            } else if ((b >> 3) != -2) {
                if (i5 != i) {
                    return -1;
                }
                return i4;
            } else {
                int i12 = i3 + 3;
                if (length <= i12) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                byte b8 = bArr[i3];
                byte b9 = bArr[i3 + 1];
                if (!((b9 & 192) == 128)) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                byte b10 = bArr[i3 + 2];
                if (!((b10 & 192) == 128)) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                byte b11 = bArr[i12];
                if (!((b11 & 192) == 128)) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                int i13 = (((b11 ^ 3678080) ^ (b10 << 6)) ^ (b9 << 12)) ^ (b8 << 18);
                if (i13 > 1114111) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                }
                if (55296 <= i13 && i13 <= 57343) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                } else if (i13 < 65536) {
                    if (i5 != i) {
                        return -1;
                    }
                    return i4;
                } else {
                    i2 = i5 + 1;
                    if (i5 == i) {
                        return i4;
                    }
                    if (i13 != 10 && i13 != 13) {
                        if (!(i13 >= 0 && i13 <= 31)) {
                            if (!(127 <= i13 && i13 <= 159)) {
                                z3 = false;
                                if (z3) {
                                    return -1;
                                }
                            }
                        }
                        z3 = true;
                        if (z3) {
                        }
                    }
                    if (i13 == 65533) {
                        return -1;
                    }
                    i4 += i13 < 65536 ? 1 : 2;
                    Unit unit3 = Unit.INSTANCE;
                    i3 += 4;
                }
            }
            i5 = i2;
        }
        return i4;
    }
}
