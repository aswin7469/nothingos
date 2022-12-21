package sun.security.util;

import android.net.connectivity.com.android.net.module.util.NetworkStackConstants;
import java.util.Comparator;

public class ByteArrayTagOrder implements Comparator<byte[]> {
    public final int compare(byte[] bArr, byte[] bArr2) {
        return (bArr[0] | NetworkStackConstants.TCPHDR_URG) - (bArr2[0] | NetworkStackConstants.TCPHDR_URG);
    }
}
