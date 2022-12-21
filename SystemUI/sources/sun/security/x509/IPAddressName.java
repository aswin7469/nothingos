package sun.security.x509;

import java.net.InetAddress;
import java.p026io.IOException;
import java.util.Arrays;
import sun.misc.HexDumpEncoder;
import sun.security.util.BitArray;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class IPAddressName implements GeneralNameInterface {
    private static final int MASKSIZE = 16;
    private byte[] address;
    private boolean isIPv4;
    private String name;

    public int getType() {
        return 7;
    }

    public IPAddressName(DerValue derValue) throws IOException {
        this(derValue.getOctetString());
    }

    public IPAddressName(byte[] bArr) throws IOException {
        if (bArr.length == 4 || bArr.length == 8) {
            this.isIPv4 = true;
        } else if (bArr.length == 16 || bArr.length == 32) {
            this.isIPv4 = false;
        } else {
            throw new IOException("Invalid IPAddressName");
        }
        this.address = bArr;
    }

    public IPAddressName(String str) throws IOException {
        if (str == null || str.length() == 0) {
            throw new IOException("IPAddress cannot be null or empty");
        } else if (str.charAt(str.length() - 1) == '/') {
            throw new IOException("Invalid IPAddress: " + str);
        } else if (str.indexOf(58) >= 0) {
            parseIPv6(str);
            this.isIPv4 = false;
        } else if (str.indexOf(46) >= 0) {
            parseIPv4(str);
            this.isIPv4 = true;
        } else {
            throw new IOException("Invalid IPAddress: " + str);
        }
    }

    private void parseIPv4(String str) throws IOException {
        int indexOf = str.indexOf(47);
        if (indexOf == -1) {
            this.address = InetAddress.getByName(str).getAddress();
            return;
        }
        this.address = new byte[8];
        byte[] address2 = InetAddress.getByName(str.substring(indexOf + 1)).getAddress();
        System.arraycopy((Object) InetAddress.getByName(str.substring(0, indexOf)).getAddress(), 0, (Object) this.address, 0, 4);
        System.arraycopy((Object) address2, 0, (Object) this.address, 4, 4);
    }

    private void parseIPv6(String str) throws IOException {
        int indexOf = str.indexOf(47);
        if (indexOf == -1) {
            this.address = InetAddress.getByName(str).getAddress();
            return;
        }
        this.address = new byte[32];
        System.arraycopy((Object) InetAddress.getByName(str.substring(0, indexOf)).getAddress(), 0, (Object) this.address, 0, 16);
        int parseInt = Integer.parseInt(str.substring(indexOf + 1));
        if (parseInt < 0 || parseInt > 128) {
            throw new IOException("IPv6Address prefix length (" + parseInt + ") in out of valid range [0,128]");
        }
        BitArray bitArray = new BitArray(128);
        for (int i = 0; i < parseInt; i++) {
            bitArray.set(i, true);
        }
        byte[] byteArray = bitArray.toByteArray();
        for (int i2 = 0; i2 < 16; i2++) {
            this.address[i2 + 16] = byteArray[i2];
        }
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOctetString(this.address);
    }

    public String toString() {
        try {
            return "IPAddress: " + getName();
        } catch (IOException unused) {
            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
            return "IPAddress: " + hexDumpEncoder.encodeBuffer(this.address);
        }
    }

    public String getName() throws IOException {
        String str = this.name;
        if (str != null) {
            return str;
        }
        int i = 0;
        if (this.isIPv4) {
            byte[] bArr = new byte[4];
            System.arraycopy((Object) this.address, 0, (Object) bArr, 0, 4);
            this.name = InetAddress.getByAddress(bArr).getHostAddress();
            byte[] bArr2 = this.address;
            if (bArr2.length == 8) {
                byte[] bArr3 = new byte[4];
                System.arraycopy((Object) bArr2, 4, (Object) bArr3, 0, 4);
                this.name += "/" + InetAddress.getByAddress(bArr3).getHostAddress();
            }
        } else {
            byte[] bArr4 = new byte[16];
            System.arraycopy((Object) this.address, 0, (Object) bArr4, 0, 16);
            this.name = InetAddress.getByAddress(bArr4).getHostAddress();
            if (this.address.length == 32) {
                byte[] bArr5 = new byte[16];
                for (int i2 = 16; i2 < 32; i2++) {
                    bArr5[i2 - 16] = this.address[i2];
                }
                BitArray bitArray = new BitArray(128, bArr5);
                while (i < 128 && bitArray.get(i)) {
                    i++;
                }
                this.name += "/" + i;
                while (i < 128) {
                    if (!bitArray.get(i)) {
                        i++;
                    } else {
                        throw new IOException("Invalid IPv6 subdomain - set bit " + i + " not contiguous");
                    }
                }
            }
        }
        return this.name;
    }

    public byte[] getBytes() {
        return (byte[]) this.address.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof IPAddressName)) {
            return false;
        }
        byte[] bArr = ((IPAddressName) obj).address;
        int length = bArr.length;
        byte[] bArr2 = this.address;
        if (length != bArr2.length) {
            return false;
        }
        if (bArr2.length != 8 && bArr2.length != 32) {
            return Arrays.equals(bArr, bArr2);
        }
        int length2 = bArr2.length / 2;
        for (int i = 0; i < length2; i++) {
            byte[] bArr3 = this.address;
            int i2 = i + length2;
            if (((byte) (bArr3[i2] & bArr3[i])) != ((byte) (bArr[i] & bArr[i2]))) {
                return false;
            }
        }
        while (true) {
            byte[] bArr4 = this.address;
            if (length2 >= bArr4.length) {
                return true;
            }
            if (bArr4[length2] != bArr[length2]) {
                return false;
            }
            length2++;
        }
    }

    public int hashCode() {
        int i = 0;
        int i2 = 0;
        while (true) {
            byte[] bArr = this.address;
            if (i >= bArr.length) {
                return i2;
            }
            i2 += bArr[i] * i;
            i++;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:42:0x008b, code lost:
        if (r11 != false) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0093, code lost:
        if (r8 != false) goto L_0x0095;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00be, code lost:
        if (r3 == r2) goto L_0x0097;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00d7, code lost:
        if (r3 == r2) goto L_0x0095;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int constrains(sun.security.x509.GeneralNameInterface r17) throws java.lang.UnsupportedOperationException {
        /*
            r16 = this;
            r0 = r16
            r1 = -1
            if (r17 != 0) goto L_0x0007
            goto L_0x00da
        L_0x0007:
            int r2 = r17.getType()
            r3 = 7
            if (r2 == r3) goto L_0x0010
            goto L_0x00da
        L_0x0010:
            r1 = r17
            sun.security.x509.IPAddressName r1 = (sun.security.x509.IPAddressName) r1
            boolean r2 = r1.equals(r0)
            if (r2 == 0) goto L_0x001d
        L_0x001a:
            r1 = 0
            goto L_0x00da
        L_0x001d:
            byte[] r1 = r1.address
            int r2 = r1.length
            r4 = 4
            if (r2 != r4) goto L_0x002b
            byte[] r2 = r0.address
            int r2 = r2.length
            if (r2 != r4) goto L_0x002b
        L_0x0028:
            r1 = 3
            goto L_0x00da
        L_0x002b:
            int r2 = r1.length
            r4 = 8
            r6 = 2
            r7 = 1
            if (r2 != r4) goto L_0x0037
            byte[] r2 = r0.address
            int r2 = r2.length
            if (r2 == r4) goto L_0x0041
        L_0x0037:
            int r2 = r1.length
            r8 = 32
            if (r2 != r8) goto L_0x0099
            byte[] r2 = r0.address
            int r2 = r2.length
            if (r2 != r8) goto L_0x0099
        L_0x0041:
            byte[] r2 = r0.address
            int r2 = r2.length
            int r2 = r2 / r6
            r10 = r7
            r11 = r10
            r4 = 0
            r8 = 0
            r9 = 0
        L_0x004a:
            if (r4 >= r2) goto L_0x0083
            byte[] r12 = r0.address
            byte r13 = r12[r4]
            int r14 = r4 + r2
            byte r12 = r12[r14]
            r15 = r13 & r12
            byte r15 = (byte) r15
            if (r15 == r13) goto L_0x005a
            r8 = r7
        L_0x005a:
            byte r15 = r1[r4]
            byte r14 = r1[r14]
            r3 = r15 & r14
            byte r3 = (byte) r3
            if (r3 == r15) goto L_0x0064
            r9 = r7
        L_0x0064:
            r3 = r12 & r14
            byte r3 = (byte) r3
            if (r3 != r12) goto L_0x0071
            r3 = r13 & r12
            byte r3 = (byte) r3
            r5 = r15 & r12
            byte r5 = (byte) r5
            if (r3 == r5) goto L_0x0072
        L_0x0071:
            r10 = 0
        L_0x0072:
            r3 = r14 & r12
            byte r3 = (byte) r3
            if (r3 != r14) goto L_0x007f
            r3 = r15 & r14
            byte r3 = (byte) r3
            r5 = r13 & r14
            byte r5 = (byte) r5
            if (r3 == r5) goto L_0x0080
        L_0x007f:
            r11 = 0
        L_0x0080:
            int r4 = r4 + 1
            goto L_0x004a
        L_0x0083:
            if (r8 != 0) goto L_0x008e
            if (r9 == 0) goto L_0x0088
            goto L_0x008e
        L_0x0088:
            if (r10 == 0) goto L_0x008b
            goto L_0x0097
        L_0x008b:
            if (r11 == 0) goto L_0x0028
            goto L_0x0095
        L_0x008e:
            if (r8 == 0) goto L_0x0093
            if (r9 == 0) goto L_0x0093
            goto L_0x001a
        L_0x0093:
            if (r8 == 0) goto L_0x0097
        L_0x0095:
            r1 = r6
            goto L_0x00da
        L_0x0097:
            r1 = r7
            goto L_0x00da
        L_0x0099:
            int r2 = r1.length
            if (r2 == r4) goto L_0x00c1
            int r2 = r1.length
            if (r2 != r8) goto L_0x00a0
            goto L_0x00c1
        L_0x00a0:
            byte[] r2 = r0.address
            int r3 = r2.length
            if (r3 == r4) goto L_0x00a8
            int r3 = r2.length
            if (r3 != r8) goto L_0x0028
        L_0x00a8:
            int r2 = r2.length
            int r2 = r2 / r6
            r3 = 0
        L_0x00ab:
            if (r3 >= r2) goto L_0x00be
            byte r4 = r1[r3]
            byte[] r5 = r0.address
            int r6 = r3 + r2
            byte r6 = r5[r6]
            r4 = r4 & r6
            byte r5 = r5[r3]
            if (r4 == r5) goto L_0x00bb
            goto L_0x00be
        L_0x00bb:
            int r3 = r3 + 1
            goto L_0x00ab
        L_0x00be:
            if (r3 != r2) goto L_0x0028
            goto L_0x0097
        L_0x00c1:
            int r2 = r1.length
            int r2 = r2 / r6
            r3 = 0
        L_0x00c4:
            if (r3 >= r2) goto L_0x00d7
            byte[] r4 = r0.address
            byte r4 = r4[r3]
            int r5 = r3 + r2
            byte r5 = r1[r5]
            r4 = r4 & r5
            byte r5 = r1[r3]
            if (r4 == r5) goto L_0x00d4
            goto L_0x00d7
        L_0x00d4:
            int r3 = r3 + 1
            goto L_0x00c4
        L_0x00d7:
            if (r3 != r2) goto L_0x0028
            goto L_0x0095
        L_0x00da:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.IPAddressName.constrains(sun.security.x509.GeneralNameInterface):int");
    }

    public int subtreeDepth() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("subtreeDepth() not defined for IPAddressName");
    }
}
