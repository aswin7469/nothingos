package sun.security.util;

import java.p026io.ByteArrayOutputStream;
import java.p026io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.HashMap;

public class ManifestDigester {
    public static final String MF_MAIN_ATTRS = "Manifest-Main-Attributes";
    private HashMap<String, Entry> entries = new HashMap<>();
    private byte[] rawBytes;

    static class Position {
        int endOfFirstLine;
        int endOfSection;
        int startOfNext;

        Position() {
        }
    }

    private boolean findSection(int i, Position position) {
        int length = this.rawBytes.length;
        position.endOfFirstLine = -1;
        int i2 = i;
        boolean z = true;
        while (i < length) {
            byte b = this.rawBytes[i];
            if (b != 10) {
                if (b != 13) {
                    z = false;
                    i++;
                } else {
                    if (position.endOfFirstLine == -1) {
                        position.endOfFirstLine = i - 1;
                    }
                    if (i < length) {
                        int i3 = i + 1;
                        if (this.rawBytes[i3] == 10) {
                            i = i3;
                        }
                    }
                }
            }
            if (position.endOfFirstLine == -1) {
                position.endOfFirstLine = i - 1;
            }
            if (z || i == length - 1) {
                if (i == length - 1) {
                    position.endOfSection = i;
                } else {
                    position.endOfSection = i2;
                }
                position.startOfNext = i + 1;
                return true;
            }
            i2 = i;
            z = true;
            i++;
        }
        return false;
    }

    public ManifestDigester(byte[] bArr) {
        this.rawBytes = bArr;
        new ByteArrayOutputStream();
        Position position = new Position();
        if (findSection(0, position)) {
            this.entries.put(MF_MAIN_ATTRS, new Entry(0, position.endOfSection + 1, position.startOfNext, this.rawBytes));
            for (int i = position.startOfNext; findSection(i, position); i = position.startOfNext) {
                int i2 = (position.endOfFirstLine - i) + 1;
                int i3 = (position.endOfSection - i) + 1;
                int i4 = position.startOfNext - i;
                if (i2 > 6 && isNameAttr(bArr, i)) {
                    StringBuilder sb = new StringBuilder(i3);
                    try {
                        sb.append(new String(bArr, i + 6, i2 - 6, "UTF8"));
                        int i5 = i2 + i;
                        int i6 = i5 - i < i3 ? bArr[i5] == 13 ? i5 + 2 : i5 + 1 : i5;
                        while (i6 - i < i3) {
                            int i7 = i6 + 1;
                            if (bArr[i6] != 32) {
                                break;
                            }
                            i6 = i7;
                            while (true) {
                                if (i6 - i >= i3) {
                                    break;
                                }
                                int i8 = i6 + 1;
                                if (bArr[i6] == 10) {
                                    i6 = i8;
                                    break;
                                }
                                i6 = i8;
                            }
                            if (bArr[i6 - 1] == 10) {
                                sb.append(new String(bArr, i7, bArr[i6 + -2] == 13 ? (i6 - i7) - 2 : (i6 - i7) - 1, "UTF8"));
                            } else {
                                return;
                            }
                        }
                        this.entries.put(sb.toString(), new Entry(i, i3, i4, this.rawBytes));
                    } catch (UnsupportedEncodingException unused) {
                        throw new IllegalStateException("UTF8 not available on platform");
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0022, code lost:
        r1 = r2[r3 + 3];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r1 = r2[r3 + 1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0016, code lost:
        r1 = r2[r3 + 2];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean isNameAttr(byte[] r2, int r3) {
        /*
            r1 = this;
            byte r1 = r2[r3]
            r0 = 78
            if (r1 == r0) goto L_0x000a
            r0 = 110(0x6e, float:1.54E-43)
            if (r1 != r0) goto L_0x0040
        L_0x000a:
            int r1 = r3 + 1
            byte r1 = r2[r1]
            r0 = 97
            if (r1 == r0) goto L_0x0016
            r0 = 65
            if (r1 != r0) goto L_0x0040
        L_0x0016:
            int r1 = r3 + 2
            byte r1 = r2[r1]
            r0 = 109(0x6d, float:1.53E-43)
            if (r1 == r0) goto L_0x0022
            r0 = 77
            if (r1 != r0) goto L_0x0040
        L_0x0022:
            int r1 = r3 + 3
            byte r1 = r2[r1]
            r0 = 101(0x65, float:1.42E-43)
            if (r1 == r0) goto L_0x002e
            r0 = 69
            if (r1 != r0) goto L_0x0040
        L_0x002e:
            int r1 = r3 + 4
            byte r1 = r2[r1]
            r0 = 58
            if (r1 != r0) goto L_0x0040
            int r3 = r3 + 5
            byte r1 = r2[r3]
            r2 = 32
            if (r1 != r2) goto L_0x0040
            r1 = 1
            goto L_0x0041
        L_0x0040:
            r1 = 0
        L_0x0041:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.ManifestDigester.isNameAttr(byte[], int):boolean");
    }

    public static class Entry {
        int length;
        int lengthWithBlankLine;
        int offset;
        boolean oldStyle;
        byte[] rawBytes;

        public Entry(int i, int i2, int i3, byte[] bArr) {
            this.offset = i;
            this.length = i2;
            this.lengthWithBlankLine = i3;
            this.rawBytes = bArr;
        }

        public byte[] digest(MessageDigest messageDigest) {
            messageDigest.reset();
            if (this.oldStyle) {
                doOldStyle(messageDigest, this.rawBytes, this.offset, this.lengthWithBlankLine);
            } else {
                messageDigest.update(this.rawBytes, this.offset, this.lengthWithBlankLine);
            }
            return messageDigest.digest();
        }

        private void doOldStyle(MessageDigest messageDigest, byte[] bArr, int i, int i2) {
            int i3 = i2 + i;
            byte b = -1;
            int i4 = i;
            while (i < i3) {
                if (bArr[i] == 13 && b == 32) {
                    messageDigest.update(bArr, i4, (i - i4) - 1);
                    i4 = i;
                }
                b = bArr[i];
                i++;
            }
            messageDigest.update(bArr, i4, i - i4);
        }

        public byte[] digestWorkaround(MessageDigest messageDigest) {
            messageDigest.reset();
            messageDigest.update(this.rawBytes, this.offset, this.length);
            return messageDigest.digest();
        }
    }

    public Entry get(String str, boolean z) {
        Entry entry = this.entries.get(str);
        if (entry != null) {
            entry.oldStyle = z;
        }
        return entry;
    }

    public byte[] manifestDigest(MessageDigest messageDigest) {
        messageDigest.reset();
        byte[] bArr = this.rawBytes;
        messageDigest.update(bArr, 0, bArr.length);
        return messageDigest.digest();
    }
}
