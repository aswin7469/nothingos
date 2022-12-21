package sun.security.util;

import java.p026io.IOException;
import java.util.ArrayList;

class DerIndefLenConverter {
    private static final int CLASS_MASK = 192;
    private static final int FORM_MASK = 32;
    private static final int LEN_LONG = 128;
    private static final int LEN_MASK = 127;
    private static final int SKIP_EOC_BYTES = 2;
    private static final int TAG_MASK = 31;
    private byte[] data;
    private int dataPos;
    private int dataSize;
    private int index;
    private ArrayList<Object> ndefsList = new ArrayList<>();
    private byte[] newData;
    private int newDataPos;
    private int numOfTotalLenBytes = 0;
    private int unresolved = 0;

    private byte[] getLengthBytes(int i) {
        if (i < 128) {
            return new byte[]{(byte) i};
        } else if (i < 256) {
            return new byte[]{-127, (byte) i};
        } else if (i < 65536) {
            return new byte[]{-126, (byte) (i >> 8), (byte) i};
        } else if (i < 16777216) {
            return new byte[]{-125, (byte) (i >> 16), (byte) (i >> 8), (byte) i};
        } else {
            return new byte[]{-124, (byte) (i >> 24), (byte) (i >> 16), (byte) (i >> 8), (byte) i};
        }
    }

    private int getNumOfLenBytes(int i) {
        if (i < 128) {
            return 1;
        }
        if (i < 256) {
            return 2;
        }
        if (i < 65536) {
            return 3;
        }
        return i < 16777216 ? 4 : 5;
    }

    private boolean isEOC(int i) {
        return (i & 31) == 0 && (i & 32) == 0 && (i & 192) == 0;
    }

    static boolean isLongForm(int i) {
        return (i & 128) == 128;
    }

    DerIndefLenConverter() {
    }

    static boolean isIndefinite(int i) {
        return isLongForm(i) && (i & 127) == 0;
    }

    private void parseTag() throws IOException {
        int i = this.dataPos;
        if (i != this.dataSize) {
            if (isEOC(this.data[i]) && this.data[this.dataPos + 1] == 0) {
                int size = this.ndefsList.size() - 1;
                int i2 = 0;
                Object obj = null;
                while (size >= 0) {
                    obj = this.ndefsList.get(size);
                    if (obj instanceof Integer) {
                        break;
                    }
                    i2 += ((byte[]) obj).length - 3;
                    size--;
                }
                if (size >= 0) {
                    byte[] lengthBytes = getLengthBytes((this.dataPos - ((Integer) obj).intValue()) + i2);
                    this.ndefsList.set(size, lengthBytes);
                    this.unresolved--;
                    this.numOfTotalLenBytes += lengthBytes.length - 3;
                } else {
                    throw new IOException("EOC does not have matching indefinite-length tag");
                }
            }
            this.dataPos++;
        }
    }

    private void writeTag() {
        int i = this.dataPos;
        if (i != this.dataSize) {
            byte[] bArr = this.data;
            this.dataPos = i + 1;
            byte b = bArr[i];
            if (isEOC(b)) {
                byte[] bArr2 = this.data;
                int i2 = this.dataPos;
                if (bArr2[i2] == 0) {
                    this.dataPos = i2 + 1;
                    writeTag();
                    return;
                }
            }
            byte[] bArr3 = this.newData;
            int i3 = this.newDataPos;
            this.newDataPos = i3 + 1;
            bArr3[i3] = (byte) b;
        }
    }

    private int parseLength() throws IOException {
        int i = this.dataPos;
        if (i == this.dataSize) {
            return 0;
        }
        byte[] bArr = this.data;
        this.dataPos = i + 1;
        byte b = bArr[i] & 255;
        if (isIndefinite(b)) {
            this.ndefsList.add(new Integer(this.dataPos));
            this.unresolved++;
            return 0;
        } else if (!isLongForm(b)) {
            return b & Byte.MAX_VALUE;
        } else {
            byte b2 = b & Byte.MAX_VALUE;
            if (b2 > 4) {
                throw new IOException("Too much data");
            } else if (this.dataSize - this.dataPos >= b2 + 1) {
                int i2 = 0;
                for (int i3 = 0; i3 < b2; i3++) {
                    byte[] bArr2 = this.data;
                    int i4 = this.dataPos;
                    this.dataPos = i4 + 1;
                    i2 = (i2 << 8) + (bArr2[i4] & 255);
                }
                if (i2 >= 0) {
                    return i2;
                }
                throw new IOException("Invalid length bytes");
            } else {
                throw new IOException("Too little data");
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v5, resolved type: byte} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeLengthAndValue() throws java.p026io.IOException {
        /*
            r6 = this;
            int r0 = r6.dataPos
            int r1 = r6.dataSize
            if (r0 != r1) goto L_0x0007
            return
        L_0x0007:
            byte[] r1 = r6.data
            int r2 = r0 + 1
            r6.dataPos = r2
            byte r0 = r1[r0]
            r0 = r0 & 255(0xff, float:3.57E-43)
            boolean r1 = isIndefinite(r0)
            r2 = 0
            if (r1 == 0) goto L_0x0035
            java.util.ArrayList<java.lang.Object> r0 = r6.ndefsList
            int r1 = r6.index
            int r3 = r1 + 1
            r6.index = r3
            java.lang.Object r0 = r0.get(r1)
            byte[] r0 = (byte[]) r0
            byte[] r1 = r6.newData
            int r3 = r6.newDataPos
            int r4 = r0.length
            java.lang.System.arraycopy((java.lang.Object) r0, (int) r2, (java.lang.Object) r1, (int) r3, (int) r4)
            int r1 = r6.newDataPos
            int r0 = r0.length
            int r1 = r1 + r0
            r6.newDataPos = r1
            return
        L_0x0035:
            boolean r1 = isLongForm(r0)
            if (r1 == 0) goto L_0x005d
            r0 = r0 & 127(0x7f, float:1.78E-43)
            r1 = r2
        L_0x003e:
            if (r2 >= r0) goto L_0x0052
            int r1 = r1 << 8
            byte[] r3 = r6.data
            int r4 = r6.dataPos
            int r5 = r4 + 1
            r6.dataPos = r5
            byte r3 = r3[r4]
            r3 = r3 & 255(0xff, float:3.57E-43)
            int r1 = r1 + r3
            int r2 = r2 + 1
            goto L_0x003e
        L_0x0052:
            if (r1 < 0) goto L_0x0055
            goto L_0x005f
        L_0x0055:
            java.io.IOException r6 = new java.io.IOException
            java.lang.String r0 = "Invalid length bytes"
            r6.<init>((java.lang.String) r0)
            throw r6
        L_0x005d:
            r1 = r0 & 127(0x7f, float:1.78E-43)
        L_0x005f:
            r6.writeLength(r1)
            r6.writeValue(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.util.DerIndefLenConverter.writeLengthAndValue():void");
    }

    private void writeLength(int i) {
        if (i < 128) {
            byte[] bArr = this.newData;
            int i2 = this.newDataPos;
            this.newDataPos = i2 + 1;
            bArr[i2] = (byte) i;
        } else if (i < 256) {
            byte[] bArr2 = this.newData;
            int i3 = this.newDataPos;
            int i4 = i3 + 1;
            bArr2[i3] = -127;
            this.newDataPos = i4 + 1;
            bArr2[i4] = (byte) i;
        } else if (i < 65536) {
            byte[] bArr3 = this.newData;
            int i5 = this.newDataPos;
            int i6 = i5 + 1;
            bArr3[i5] = -126;
            int i7 = i6 + 1;
            bArr3[i6] = (byte) (i >> 8);
            this.newDataPos = i7 + 1;
            bArr3[i7] = (byte) i;
        } else if (i < 16777216) {
            byte[] bArr4 = this.newData;
            int i8 = this.newDataPos;
            int i9 = i8 + 1;
            bArr4[i8] = -125;
            int i10 = i9 + 1;
            bArr4[i9] = (byte) (i >> 16);
            int i11 = i10 + 1;
            bArr4[i10] = (byte) (i >> 8);
            this.newDataPos = i11 + 1;
            bArr4[i11] = (byte) i;
        } else {
            byte[] bArr5 = this.newData;
            int i12 = this.newDataPos;
            int i13 = i12 + 1;
            bArr5[i12] = -124;
            int i14 = i13 + 1;
            bArr5[i13] = (byte) (i >> 24);
            int i15 = i14 + 1;
            bArr5[i14] = (byte) (i >> 16);
            int i16 = i15 + 1;
            bArr5[i15] = (byte) (i >> 8);
            this.newDataPos = i16 + 1;
            bArr5[i16] = (byte) i;
        }
    }

    private void parseValue(int i) {
        this.dataPos += i;
    }

    private void writeValue(int i) {
        for (int i2 = 0; i2 < i; i2++) {
            byte[] bArr = this.newData;
            int i3 = this.newDataPos;
            this.newDataPos = i3 + 1;
            byte[] bArr2 = this.data;
            int i4 = this.dataPos;
            this.dataPos = i4 + 1;
            bArr[i3] = bArr2[i4];
        }
    }

    /* access modifiers changed from: package-private */
    public byte[] convert(byte[] bArr) throws IOException {
        int i;
        this.data = bArr;
        this.dataPos = 0;
        this.index = 0;
        this.dataSize = bArr.length;
        while (true) {
            if (this.dataPos >= this.dataSize) {
                i = 0;
                break;
            }
            parseTag();
            parseValue(parseLength());
            if (this.unresolved == 0) {
                int i2 = this.dataSize;
                int i3 = this.dataPos;
                i = i2 - i3;
                this.dataSize = i3;
                break;
            }
        }
        if (this.unresolved == 0) {
            this.newData = new byte[(this.dataSize + this.numOfTotalLenBytes + i)];
            this.dataPos = 0;
            this.newDataPos = 0;
            this.index = 0;
            while (true) {
                int i4 = this.dataPos;
                int i5 = this.dataSize;
                if (i4 < i5) {
                    writeTag();
                    writeLengthAndValue();
                } else {
                    System.arraycopy((Object) bArr, i5, (Object) this.newData, this.numOfTotalLenBytes + i5, i);
                    return this.newData;
                }
            }
        } else {
            throw new IOException("not all indef len BER resolved");
        }
    }
}
