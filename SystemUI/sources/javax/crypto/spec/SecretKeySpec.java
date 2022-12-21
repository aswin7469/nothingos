package javax.crypto.spec;

import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Locale;
import javax.crypto.SecretKey;

public class SecretKeySpec implements KeySpec, SecretKey {
    private static final long serialVersionUID = 6577238317307289933L;
    private String algorithm;
    private byte[] key;

    public String getFormat() {
        return "RAW";
    }

    public SecretKeySpec(byte[] bArr, String str) {
        if (bArr == null || str == null) {
            throw new IllegalArgumentException("Missing argument");
        } else if (bArr.length != 0) {
            this.key = (byte[]) bArr.clone();
            this.algorithm = str;
        } else {
            throw new IllegalArgumentException("Empty key");
        }
    }

    public SecretKeySpec(byte[] bArr, int i, int i2, String str) {
        if (bArr == null || str == null) {
            throw new IllegalArgumentException("Missing argument");
        } else if (bArr.length == 0) {
            throw new IllegalArgumentException("Empty key");
        } else if (bArr.length - i < i2) {
            throw new IllegalArgumentException("Invalid offset/length combination");
        } else if (i2 >= 0) {
            byte[] bArr2 = new byte[i2];
            this.key = bArr2;
            System.arraycopy((Object) bArr, i, (Object) bArr2, 0, i2);
            this.algorithm = str;
        } else {
            throw new ArrayIndexOutOfBoundsException("len is negative");
        }
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public byte[] getEncoded() {
        return (byte[]) this.key.clone();
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int i2 = 1;
        while (true) {
            byte[] bArr = this.key;
            if (i2 >= bArr.length) {
                break;
            }
            i += bArr[i2] * i2;
            i2++;
        }
        if (this.algorithm.equalsIgnoreCase("TripleDES")) {
            hashCode = -1335250348;
        } else {
            hashCode = this.algorithm.toLowerCase(Locale.ENGLISH).hashCode();
        }
        return hashCode ^ i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SecretKey)) {
            return false;
        }
        SecretKey secretKey = (SecretKey) obj;
        String algorithm2 = secretKey.getAlgorithm();
        if (!algorithm2.equalsIgnoreCase(this.algorithm) && ((!algorithm2.equalsIgnoreCase("DESede") || !this.algorithm.equalsIgnoreCase("TripleDES")) && (!algorithm2.equalsIgnoreCase("TripleDES") || !this.algorithm.equalsIgnoreCase("DESede")))) {
            return false;
        }
        return MessageDigest.isEqual(this.key, secretKey.getEncoded());
    }
}
