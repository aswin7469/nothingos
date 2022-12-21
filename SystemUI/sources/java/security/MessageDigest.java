package java.security;

import java.nio.ByteBuffer;
import java.util.Objects;
import sun.security.jca.Providers;
import sun.security.pkcs.PKCS9Attribute;

public abstract class MessageDigest extends MessageDigestSpi {
    private static final int INITIAL = 0;
    private static final int IN_PROGRESS = 1;
    /* access modifiers changed from: private */
    public String algorithm;
    /* access modifiers changed from: private */
    public Provider provider;
    /* access modifiers changed from: private */
    public int state = 0;

    protected MessageDigest(String str) {
        this.algorithm = str;
    }

    public static MessageDigest getInstance(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        Objects.requireNonNull(str, "null algorithm name");
        try {
            String str2 = null;
            Object[] impl = Security.getImpl(str, PKCS9Attribute.MESSAGE_DIGEST_STR, (String) null);
            Object obj = impl[0];
            if (obj instanceof MessageDigest) {
                messageDigest = (MessageDigest) obj;
            } else {
                messageDigest = new Delegate((MessageDigestSpi) obj, str);
            }
            messageDigest.provider = (Provider) impl[1];
            return messageDigest;
        } catch (NoSuchProviderException unused) {
            throw new NoSuchAlgorithmException(str + " not found");
        }
    }

    public static MessageDigest getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        Objects.requireNonNull(str, "null algorithm name");
        if (str2 == null || str2.isEmpty()) {
            throw new IllegalArgumentException("missing provider");
        }
        Providers.checkBouncyCastleDeprecation(str2, PKCS9Attribute.MESSAGE_DIGEST_STR, str);
        Object[] impl = Security.getImpl(str, PKCS9Attribute.MESSAGE_DIGEST_STR, str2);
        Object obj = impl[0];
        if (obj instanceof MessageDigest) {
            MessageDigest messageDigest = (MessageDigest) obj;
            messageDigest.provider = (Provider) impl[1];
            return messageDigest;
        }
        Delegate delegate = new Delegate((MessageDigestSpi) obj, str);
        delegate.provider = (Provider) impl[1];
        return delegate;
    }

    public static MessageDigest getInstance(String str, Provider provider2) throws NoSuchAlgorithmException {
        Objects.requireNonNull(str, "null algorithm name");
        if (provider2 != null) {
            Providers.checkBouncyCastleDeprecation(provider2, PKCS9Attribute.MESSAGE_DIGEST_STR, str);
            Object[] impl = Security.getImpl(str, PKCS9Attribute.MESSAGE_DIGEST_STR, provider2);
            Object obj = impl[0];
            if (obj instanceof MessageDigest) {
                MessageDigest messageDigest = (MessageDigest) obj;
                messageDigest.provider = (Provider) impl[1];
                return messageDigest;
            }
            Delegate delegate = new Delegate((MessageDigestSpi) obj, str);
            delegate.provider = (Provider) impl[1];
            return delegate;
        }
        throw new IllegalArgumentException("missing provider");
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public void update(byte b) {
        engineUpdate(b);
        this.state = 1;
    }

    public void update(byte[] bArr, int i, int i2) {
        if (bArr == null) {
            throw new IllegalArgumentException("No input buffer given");
        } else if (bArr.length - i >= i2) {
            engineUpdate(bArr, i, i2);
            this.state = 1;
        } else {
            throw new IllegalArgumentException("Input buffer too short");
        }
    }

    public void update(byte[] bArr) {
        engineUpdate(bArr, 0, bArr.length);
        this.state = 1;
    }

    public final void update(ByteBuffer byteBuffer) {
        byteBuffer.getClass();
        engineUpdate(byteBuffer);
        this.state = 1;
    }

    public byte[] digest() {
        byte[] engineDigest = engineDigest();
        this.state = 0;
        return engineDigest;
    }

    public int digest(byte[] bArr, int i, int i2) throws DigestException {
        if (bArr == null) {
            throw new IllegalArgumentException("No output buffer given");
        } else if (bArr.length - i >= i2) {
            int engineDigest = engineDigest(bArr, i, i2);
            this.state = 0;
            return engineDigest;
        } else {
            throw new IllegalArgumentException("Output buffer too small for specified offset and length");
        }
    }

    public byte[] digest(byte[] bArr) {
        update(bArr);
        return digest();
    }

    private String getProviderName() {
        Provider provider2 = this.provider;
        return provider2 == null ? "(no provider)" : provider2.getName();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.algorithm);
        sb.append(" Message Digest from ");
        sb.append(getProviderName());
        sb.append(", ");
        int i = this.state;
        if (i == 0) {
            sb.append("<initialized>");
        } else if (i == 1) {
            sb.append("<in progress>");
        }
        return sb.toString();
    }

    public static boolean isEqual(byte[] bArr, byte[] bArr2) {
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null || bArr2 == null) {
            return false;
        }
        int length = bArr.length;
        int length2 = bArr2.length;
        if (length2 != 0) {
            byte b = (length - length2) | 0;
            for (int i = 0; i < length; i++) {
                b |= bArr2[((i - length2) >>> 31) * i] ^ bArr[i];
            }
            if (b == 0) {
                return true;
            }
            return false;
        } else if (length == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void reset() {
        engineReset();
        this.state = 0;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public final int getDigestLength() {
        int engineGetDigestLength = engineGetDigestLength();
        if (engineGetDigestLength == 0) {
            try {
                return ((MessageDigest) clone()).digest().length;
            } catch (CloneNotSupportedException unused) {
            }
        }
        return engineGetDigestLength;
    }

    public Object clone() throws CloneNotSupportedException {
        if (this instanceof Cloneable) {
            return super.clone();
        }
        throw new CloneNotSupportedException();
    }

    static class Delegate extends MessageDigest {
        private MessageDigestSpi digestSpi;

        public Delegate(MessageDigestSpi messageDigestSpi, String str) {
            super(str);
            this.digestSpi = messageDigestSpi;
        }

        public Object clone() throws CloneNotSupportedException {
            MessageDigestSpi messageDigestSpi = this.digestSpi;
            if (messageDigestSpi instanceof Cloneable) {
                Delegate delegate = new Delegate((MessageDigestSpi) messageDigestSpi.clone(), this.algorithm);
                delegate.provider = this.provider;
                delegate.state = this.state;
                return delegate;
            }
            throw new CloneNotSupportedException();
        }

        /* access modifiers changed from: protected */
        public int engineGetDigestLength() {
            return this.digestSpi.engineGetDigestLength();
        }

        /* access modifiers changed from: protected */
        public void engineUpdate(byte b) {
            this.digestSpi.engineUpdate(b);
        }

        /* access modifiers changed from: protected */
        public void engineUpdate(byte[] bArr, int i, int i2) {
            this.digestSpi.engineUpdate(bArr, i, i2);
        }

        /* access modifiers changed from: protected */
        public void engineUpdate(ByteBuffer byteBuffer) {
            this.digestSpi.engineUpdate(byteBuffer);
        }

        /* access modifiers changed from: protected */
        public byte[] engineDigest() {
            return this.digestSpi.engineDigest();
        }

        /* access modifiers changed from: protected */
        public int engineDigest(byte[] bArr, int i, int i2) throws DigestException {
            return this.digestSpi.engineDigest(bArr, i, i2);
        }

        /* access modifiers changed from: protected */
        public void engineReset() {
            this.digestSpi.engineReset();
        }
    }
}
