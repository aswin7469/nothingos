package javax.crypto.spec;

import java.security.spec.KeySpec;
import java.util.Arrays;

public class PBEKeySpec implements KeySpec {
    private int iterationCount = 0;
    private int keyLength = 0;
    private char[] password;
    private byte[] salt = null;

    public PBEKeySpec(char[] cArr) {
        if (cArr == null || cArr.length == 0) {
            this.password = new char[0];
        } else {
            this.password = (char[]) cArr.clone();
        }
    }

    public PBEKeySpec(char[] cArr, byte[] bArr, int i, int i2) {
        if (cArr == null || cArr.length == 0) {
            this.password = new char[0];
        } else {
            this.password = (char[]) cArr.clone();
        }
        if (bArr == null) {
            throw new NullPointerException("the salt parameter must be non-null");
        } else if (bArr.length != 0) {
            this.salt = (byte[]) bArr.clone();
            if (i <= 0) {
                throw new IllegalArgumentException("invalid iterationCount value");
            } else if (i2 > 0) {
                this.iterationCount = i;
                this.keyLength = i2;
            } else {
                throw new IllegalArgumentException("invalid keyLength value");
            }
        } else {
            throw new IllegalArgumentException("the salt parameter must not be empty");
        }
    }

    public PBEKeySpec(char[] cArr, byte[] bArr, int i) {
        if (cArr == null || cArr.length == 0) {
            this.password = new char[0];
        } else {
            this.password = (char[]) cArr.clone();
        }
        if (bArr == null) {
            throw new NullPointerException("the salt parameter must be non-null");
        } else if (bArr.length != 0) {
            this.salt = (byte[]) bArr.clone();
            if (i > 0) {
                this.iterationCount = i;
                return;
            }
            throw new IllegalArgumentException("invalid iterationCount value");
        } else {
            throw new IllegalArgumentException("the salt parameter must not be empty");
        }
    }

    public final void clearPassword() {
        char[] cArr = this.password;
        if (cArr != null) {
            Arrays.fill(cArr, ' ');
            this.password = null;
        }
    }

    public final char[] getPassword() {
        char[] cArr = this.password;
        if (cArr != null) {
            return (char[]) cArr.clone();
        }
        throw new IllegalStateException("password has been cleared");
    }

    public final byte[] getSalt() {
        byte[] bArr = this.salt;
        if (bArr != null) {
            return (byte[]) bArr.clone();
        }
        return null;
    }

    public final int getIterationCount() {
        return this.iterationCount;
    }

    public final int getKeyLength() {
        return this.keyLength;
    }
}
