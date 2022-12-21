package sun.security.pkcs;

import java.math.BigInteger;
import java.p026io.ByteArrayInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectStreamException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyRep;
import java.security.PrivateKey;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

public class PKCS8Key implements PrivateKey {
    private static final long serialVersionUID = -3836890099307167124L;
    public static final BigInteger version = BigInteger.ZERO;
    protected AlgorithmId algid;
    protected byte[] encodedKey;
    protected byte[] key;

    public String getFormat() {
        return "PKCS#8";
    }

    public PKCS8Key() {
    }

    private PKCS8Key(AlgorithmId algorithmId, byte[] bArr) throws InvalidKeyException {
        this.algid = algorithmId;
        this.key = bArr;
        encode();
    }

    public static PKCS8Key parse(DerValue derValue) throws IOException {
        PrivateKey parseKey = parseKey(derValue);
        if (parseKey instanceof PKCS8Key) {
            return (PKCS8Key) parseKey;
        }
        throw new IOException("Provider did not return PKCS8Key");
    }

    public static PrivateKey parseKey(DerValue derValue) throws IOException {
        if (derValue.tag == 48) {
            BigInteger bigInteger = derValue.data.getBigInteger();
            BigInteger bigInteger2 = version;
            if (bigInteger2.equals(bigInteger)) {
                try {
                    PrivateKey buildPKCS8Key = buildPKCS8Key(AlgorithmId.parse(derValue.data.getDerValue()), derValue.data.getOctetString());
                    if (derValue.data.available() == 0) {
                        return buildPKCS8Key;
                    }
                    throw new IOException("excess private key");
                } catch (InvalidKeyException unused) {
                    throw new IOException("corrupt private key");
                }
            } else {
                throw new IOException("version mismatch: (supported: " + Debug.toHexString(bigInteger2) + ", parsed: " + Debug.toHexString(bigInteger));
            }
        } else {
            throw new IOException("corrupt private key");
        }
    }

    /* access modifiers changed from: protected */
    public void parseKeyBits() throws IOException, InvalidKeyException {
        encode();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:14|15|(1:17)(1:18)) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r2 = java.lang.ClassLoader.getSystemClassLoader();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x004a, code lost:
        if (r2 != null) goto L_0x004c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004c, code lost:
        r2 = r2.loadClass(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0051, code lost:
        r2 = null;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0046 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.security.PrivateKey buildPKCS8Key(sun.security.x509.AlgorithmId r4, byte[] r5) throws java.p026io.IOException, java.security.InvalidKeyException {
        /*
            java.lang.String r0 = "PrivateKey.PKCS#8."
            sun.security.util.DerOutputStream r1 = new sun.security.util.DerOutputStream
            r1.<init>()
            encode(r1, r4, r5)
            java.security.spec.PKCS8EncodedKeySpec r2 = new java.security.spec.PKCS8EncodedKeySpec
            byte[] r1 = r1.toByteArray()
            r2.<init>(r1)
            java.lang.String r1 = r4.getName()     // Catch:{ NoSuchAlgorithmException | InvalidKeySpecException -> 0x0020 }
            java.security.KeyFactory r1 = java.security.KeyFactory.getInstance(r1)     // Catch:{ NoSuchAlgorithmException | InvalidKeySpecException -> 0x0020 }
            java.security.PrivateKey r4 = r1.generatePrivate(r2)     // Catch:{ NoSuchAlgorithmException | InvalidKeySpecException -> 0x0020 }
            return r4
        L_0x0020:
            java.lang.String r1 = ""
            java.lang.String r2 = "SUN"
            java.security.Provider r2 = java.security.Security.getProvider(r2)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            if (r2 == 0) goto L_0x006c
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            r3.<init>((java.lang.String) r0)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            java.lang.String r0 = r4.getName()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            r3.append((java.lang.String) r0)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            java.lang.String r0 = r3.toString()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            java.lang.String r1 = r2.getProperty(r0)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            if (r1 == 0) goto L_0x0066
            r0 = 0
            java.lang.Class r2 = java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException -> 0x0046 }
            goto L_0x0052
        L_0x0046:
            java.lang.ClassLoader r2 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            if (r2 == 0) goto L_0x0051
            java.lang.Class r2 = r2.loadClass(r1)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            goto L_0x0052
        L_0x0051:
            r2 = r0
        L_0x0052:
            if (r2 == 0) goto L_0x0058
            java.lang.Object r0 = r2.newInstance()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
        L_0x0058:
            boolean r2 = r0 instanceof sun.security.pkcs.PKCS8Key     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            if (r2 == 0) goto L_0x0089
            sun.security.pkcs.PKCS8Key r0 = (sun.security.pkcs.PKCS8Key) r0     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            r0.algid = r4     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            r0.key = r5     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            r0.parseKeyBits()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            return r0
        L_0x0066:
            java.lang.InstantiationException r0 = new java.lang.InstantiationException     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            r0.<init>()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            throw r0     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
        L_0x006c:
            java.lang.InstantiationException r0 = new java.lang.InstantiationException     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            r0.<init>()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
            throw r0     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0089, IllegalAccessException -> 0x0072 }
        L_0x0072:
            java.io.IOException r4 = new java.io.IOException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append((java.lang.String) r1)
            java.lang.String r0 = " [internal error]"
            r5.append((java.lang.String) r0)
            java.lang.String r5 = r5.toString()
            r4.<init>((java.lang.String) r5)
            throw r4
        L_0x0089:
            sun.security.pkcs.PKCS8Key r0 = new sun.security.pkcs.PKCS8Key
            r0.<init>()
            r0.algid = r4
            r0.key = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs.PKCS8Key.buildPKCS8Key(sun.security.x509.AlgorithmId, byte[]):java.security.PrivateKey");
    }

    public String getAlgorithm() {
        return this.algid.getName();
    }

    public AlgorithmId getAlgorithmId() {
        return this.algid;
    }

    public final void encode(DerOutputStream derOutputStream) throws IOException {
        encode(derOutputStream, this.algid, this.key);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0009, code lost:
        r0 = null;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized byte[] getEncoded() {
        /*
            r1 = this;
            monitor-enter(r1)
            byte[] r0 = r1.encode()     // Catch:{ InvalidKeyException -> 0x0009, all -> 0x0006 }
            goto L_0x000a
        L_0x0006:
            r0 = move-exception
            monitor-exit(r1)
            throw r0
        L_0x0009:
            r0 = 0
        L_0x000a:
            monitor-exit(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.pkcs.PKCS8Key.getEncoded():byte[]");
    }

    public byte[] encode() throws InvalidKeyException {
        if (this.encodedKey == null) {
            try {
                DerOutputStream derOutputStream = new DerOutputStream();
                encode(derOutputStream);
                this.encodedKey = derOutputStream.toByteArray();
            } catch (IOException e) {
                throw new InvalidKeyException("IOException : " + e.getMessage());
            }
        }
        return (byte[]) this.encodedKey.clone();
    }

    public void decode(InputStream inputStream) throws InvalidKeyException {
        try {
            DerValue derValue = new DerValue(inputStream);
            if (derValue.tag == 48) {
                BigInteger bigInteger = derValue.data.getBigInteger();
                BigInteger bigInteger2 = version;
                if (bigInteger.equals(bigInteger2)) {
                    this.algid = AlgorithmId.parse(derValue.data.getDerValue());
                    this.key = derValue.data.getOctetString();
                    parseKeyBits();
                    derValue.data.available();
                    return;
                }
                throw new IOException("version mismatch: (supported: " + Debug.toHexString(bigInteger2) + ", parsed: " + Debug.toHexString(bigInteger));
            }
            throw new InvalidKeyException("invalid key format");
        } catch (IOException e) {
            throw new InvalidKeyException("IOException : " + e.getMessage());
        }
    }

    public void decode(byte[] bArr) throws InvalidKeyException {
        decode((InputStream) new ByteArrayInputStream(bArr));
    }

    /* access modifiers changed from: protected */
    public Object writeReplace() throws ObjectStreamException {
        return new KeyRep(KeyRep.Type.PRIVATE, getAlgorithm(), getFormat(), getEncoded());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        try {
            decode((InputStream) objectInputStream);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new IOException("deserialized key is invalid: " + e.getMessage());
        }
    }

    static void encode(DerOutputStream derOutputStream, AlgorithmId algorithmId, byte[] bArr) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putInteger(version);
        algorithmId.encode(derOutputStream2);
        derOutputStream2.putOctetString(bArr);
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Key)) {
            return false;
        }
        byte[] bArr = this.encodedKey;
        if (bArr == null) {
            bArr = getEncoded();
        }
        byte[] encoded = ((Key) obj).getEncoded();
        if (bArr.length != encoded.length) {
            return false;
        }
        for (int i = 0; i < bArr.length; i++) {
            if (bArr[i] != encoded[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        byte[] encoded = getEncoded();
        int i = 0;
        for (int i2 = 1; i2 < encoded.length; i2++) {
            i += encoded[i2] * i2;
        }
        return i;
    }
}
