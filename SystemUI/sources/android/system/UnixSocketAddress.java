package android.system;

import android.annotation.SystemApi;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class UnixSocketAddress extends SocketAddress {
    private static final int NAMED_PATH_LENGTH = OsConstants.UNIX_PATH_MAX;
    private static final byte[] UNNAMED_PATH = new byte[0];
    private final byte[] sun_path;

    private UnixSocketAddress(byte[] bArr) {
        if (bArr == null) {
            throw new IllegalArgumentException("sun_path must not be null");
        } else if (bArr.length > NAMED_PATH_LENGTH) {
            throw new IllegalArgumentException("sun_path exceeds the maximum length");
        } else if (bArr.length == 0) {
            this.sun_path = UNNAMED_PATH;
        } else {
            byte[] bArr2 = new byte[bArr.length];
            this.sun_path = bArr2;
            System.arraycopy((Object) bArr, 0, (Object) bArr2, 0, bArr.length);
        }
    }

    public static UnixSocketAddress createAbstract(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] bArr = new byte[(bytes.length + 1)];
        System.arraycopy((Object) bytes, 0, (Object) bArr, 1, bytes.length);
        return new UnixSocketAddress(bArr);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static UnixSocketAddress createFileSystem(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        byte[] bArr = new byte[(bytes.length + 1)];
        System.arraycopy((Object) bytes, 0, (Object) bArr, 0, bytes.length);
        return new UnixSocketAddress(bArr);
    }

    public static UnixSocketAddress createUnnamed() {
        return new UnixSocketAddress(UNNAMED_PATH);
    }

    public byte[] getSunPath() {
        byte[] bArr = this.sun_path;
        if (bArr.length == 0) {
            return bArr;
        }
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy((Object) bArr, 0, (Object) bArr2, 0, bArr.length);
        return bArr2;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return Arrays.equals(this.sun_path, ((UnixSocketAddress) obj).sun_path);
    }

    public int hashCode() {
        return Arrays.hashCode(this.sun_path);
    }

    public String toString() {
        return "UnixSocketAddress[sun_path=" + Arrays.toString(this.sun_path) + ']';
    }
}
