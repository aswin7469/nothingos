package sun.misc;

public class BASE64Decoder extends CharacterDecoder {
    private static final char[] pem_array = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final byte[] pem_convert_array = new byte[256];
    byte[] decode_buffer = new byte[4];

    /* access modifiers changed from: protected */
    public int bytesPerAtom() {
        return 4;
    }

    /* access modifiers changed from: protected */
    public int bytesPerLine() {
        return 72;
    }

    static {
        int i = 0;
        for (int i2 = 0; i2 < 255; i2++) {
            pem_convert_array[i2] = -1;
        }
        while (true) {
            char[] cArr = pem_array;
            if (i < cArr.length) {
                pem_convert_array[cArr[i]] = (byte) i;
                i++;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x006f  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00b2  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void decodeAtom(java.p026io.PushbackInputStream r9, java.p026io.OutputStream r10, int r11) throws java.p026io.IOException {
        /*
            r8 = this;
            r0 = 2
            if (r11 < r0) goto L_0x00cb
        L_0x0003:
            int r1 = r9.read()
            r2 = -1
            if (r1 == r2) goto L_0x00c5
            r3 = 10
            if (r1 == r3) goto L_0x0003
            r3 = 13
            if (r1 == r3) goto L_0x0003
            byte[] r3 = r8.decode_buffer
            byte r1 = (byte) r1
            r4 = 0
            r3[r4] = r1
            int r1 = r11 + -1
            r5 = 1
            int r9 = r8.readFully(r9, r3, r5, r1)
            if (r9 == r2) goto L_0x00bf
            r9 = 61
            r1 = 3
            if (r11 <= r1) goto L_0x002d
            byte[] r3 = r8.decode_buffer
            byte r3 = r3[r1]
            if (r3 != r9) goto L_0x002d
            r11 = r1
        L_0x002d:
            if (r11 <= r0) goto L_0x0036
            byte[] r3 = r8.decode_buffer
            byte r3 = r3[r0]
            if (r3 != r9) goto L_0x0036
            r11 = r0
        L_0x0036:
            r9 = 4
            if (r11 == r0) goto L_0x0059
            if (r11 == r1) goto L_0x004b
            if (r11 == r9) goto L_0x0041
            r8 = r2
            r3 = r8
            r5 = r3
            goto L_0x006d
        L_0x0041:
            byte[] r2 = pem_convert_array
            byte[] r3 = r8.decode_buffer
            byte r3 = r3[r1]
            r3 = r3 & 255(0xff, float:3.57E-43)
            byte r2 = r2[r3]
        L_0x004b:
            byte[] r3 = pem_convert_array
            byte[] r6 = r8.decode_buffer
            byte r6 = r6[r0]
            r6 = r6 & 255(0xff, float:3.57E-43)
            byte r3 = r3[r6]
            r7 = r3
            r3 = r2
            r2 = r7
            goto L_0x005a
        L_0x0059:
            r3 = r2
        L_0x005a:
            byte[] r6 = pem_convert_array
            byte[] r8 = r8.decode_buffer
            byte r5 = r8[r5]
            r5 = r5 & 255(0xff, float:3.57E-43)
            byte r5 = r6[r5]
            byte r8 = r8[r4]
            r8 = r8 & 255(0xff, float:3.57E-43)
            byte r8 = r6[r8]
            r7 = r2
            r2 = r8
            r8 = r7
        L_0x006d:
            if (r11 == r0) goto L_0x00b2
            if (r11 == r1) goto L_0x0099
            if (r11 == r9) goto L_0x0074
            goto L_0x00be
        L_0x0074:
            int r11 = r2 << 2
            r11 = r11 & 252(0xfc, float:3.53E-43)
            int r0 = r5 >>> 4
            r0 = r0 & r1
            r11 = r11 | r0
            byte r11 = (byte) r11
            r10.write((int) r11)
            int r9 = r5 << 4
            r9 = r9 & 240(0xf0, float:3.36E-43)
            int r11 = r8 >>> 2
            r11 = r11 & 15
            r9 = r9 | r11
            byte r9 = (byte) r9
            r10.write((int) r9)
            int r8 = r8 << 6
            r8 = r8 & 192(0xc0, float:2.69E-43)
            r9 = r3 & 63
            r8 = r8 | r9
            byte r8 = (byte) r8
            r10.write((int) r8)
            goto L_0x00be
        L_0x0099:
            int r11 = r2 << 2
            r11 = r11 & 252(0xfc, float:3.53E-43)
            int r2 = r5 >>> 4
            r1 = r1 & r2
            r11 = r11 | r1
            byte r11 = (byte) r11
            r10.write((int) r11)
            int r9 = r5 << 4
            r9 = r9 & 240(0xf0, float:3.36E-43)
            int r8 = r8 >>> r0
            r8 = r8 & 15
            r8 = r8 | r9
            byte r8 = (byte) r8
            r10.write((int) r8)
            goto L_0x00be
        L_0x00b2:
            int r8 = r2 << 2
            r8 = r8 & 252(0xfc, float:3.53E-43)
            int r9 = r5 >>> 4
            r9 = r9 & r1
            r8 = r8 | r9
            byte r8 = (byte) r8
            r10.write((int) r8)
        L_0x00be:
            return
        L_0x00bf:
            sun.misc.CEStreamExhausted r8 = new sun.misc.CEStreamExhausted
            r8.<init>()
            throw r8
        L_0x00c5:
            sun.misc.CEStreamExhausted r8 = new sun.misc.CEStreamExhausted
            r8.<init>()
            throw r8
        L_0x00cb:
            sun.misc.CEFormatException r8 = new sun.misc.CEFormatException
            java.lang.String r9 = "BASE64Decoder: Not enough bytes for an atom."
            r8.<init>(r9)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.BASE64Decoder.decodeAtom(java.io.PushbackInputStream, java.io.OutputStream, int):void");
    }
}
