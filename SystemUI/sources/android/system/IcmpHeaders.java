package android.system;

public final class IcmpHeaders {
    private IcmpHeaders() {
    }

    public static byte[] createIcmpEchoHdr(boolean z, int i) {
        byte[] bArr = new byte[8];
        bArr[0] = (byte) (z ? OsConstants.ICMP_ECHO : OsConstants.ICMP6_ECHO_REQUEST);
        bArr[6] = (byte) (i >> 8);
        bArr[7] = (byte) i;
        return bArr;
    }
}
