package sun.security.x509;

import java.p026io.ByteArrayInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.PublicKey;
import java.util.Arrays;
import sun.misc.HexDumpEncoder;
import sun.security.util.BitArray;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class X509Key implements PublicKey {
    private static final long serialVersionUID = -5359250853002055002L;
    protected AlgorithmId algid;
    private BitArray bitStringKey = null;
    protected byte[] encodedKey;
    @Deprecated
    protected byte[] key = null;
    @Deprecated
    private int unusedBits = 0;

    public String getFormat() {
        return "X.509";
    }

    public X509Key() {
    }

    private X509Key(AlgorithmId algorithmId, BitArray bitArray) throws InvalidKeyException {
        this.algid = algorithmId;
        setKey(bitArray);
        encode();
    }

    /* access modifiers changed from: protected */
    public void setKey(BitArray bitArray) {
        this.bitStringKey = (BitArray) bitArray.clone();
        this.key = bitArray.toByteArray();
        int length = bitArray.length() % 8;
        this.unusedBits = length == 0 ? 0 : 8 - length;
    }

    /* access modifiers changed from: protected */
    public BitArray getKey() {
        byte[] bArr = this.key;
        BitArray bitArray = new BitArray((bArr.length * 8) - this.unusedBits, bArr);
        this.bitStringKey = bitArray;
        return (BitArray) bitArray.clone();
    }

    public static PublicKey parse(DerValue derValue) throws IOException {
        if (derValue.tag == 48) {
            try {
                PublicKey buildX509Key = buildX509Key(AlgorithmId.parse(derValue.data.getDerValue()), derValue.data.getUnalignedBitString());
                if (derValue.data.available() == 0) {
                    return buildX509Key;
                }
                throw new IOException("excess subject key");
            } catch (InvalidKeyException e) {
                throw new IOException("subject key, " + e.getMessage(), e);
            }
        } else {
            throw new IOException("corrupt subject key");
        }
    }

    /* access modifiers changed from: protected */
    public void parseKeyBits() throws IOException, InvalidKeyException {
        encode();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|(1:20)(1:21)) */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2 = java.lang.ClassLoader.getSystemClassLoader();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0055, code lost:
        if (r2 != null) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0057, code lost:
        r2 = r2.loadClass(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005c, code lost:
        r2 = null;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0051 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.security.PublicKey buildX509Key(sun.security.x509.AlgorithmId r4, sun.security.util.BitArray r5) throws java.p026io.IOException, java.security.InvalidKeyException {
        /*
            java.lang.String r0 = "PublicKey.X.509."
            sun.security.util.DerOutputStream r1 = new sun.security.util.DerOutputStream
            r1.<init>()
            encode(r1, r4, r5)
            java.security.spec.X509EncodedKeySpec r2 = new java.security.spec.X509EncodedKeySpec
            byte[] r1 = r1.toByteArray()
            r2.<init>(r1)
            java.lang.String r1 = r4.getName()     // Catch:{ NoSuchAlgorithmException -> 0x002b, InvalidKeySpecException -> 0x0020 }
            java.security.KeyFactory r1 = java.security.KeyFactory.getInstance(r1)     // Catch:{ NoSuchAlgorithmException -> 0x002b, InvalidKeySpecException -> 0x0020 }
            java.security.PublicKey r4 = r1.generatePublic(r2)     // Catch:{ NoSuchAlgorithmException -> 0x002b, InvalidKeySpecException -> 0x0020 }
            return r4
        L_0x0020:
            r4 = move-exception
            java.security.InvalidKeyException r5 = new java.security.InvalidKeyException
            java.lang.String r0 = r4.getMessage()
            r5.<init>(r0, r4)
            throw r5
        L_0x002b:
            java.lang.String r1 = ""
            java.lang.String r2 = "SUN"
            java.security.Provider r2 = java.security.Security.getProvider(r2)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            if (r2 == 0) goto L_0x0078
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            r3.<init>((java.lang.String) r0)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            java.lang.String r0 = r4.getName()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            r3.append((java.lang.String) r0)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            java.lang.String r0 = r3.toString()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            java.lang.String r1 = r2.getProperty(r0)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            if (r1 == 0) goto L_0x0072
            r0 = 0
            java.lang.Class r2 = java.lang.Class.forName(r1)     // Catch:{ ClassNotFoundException -> 0x0051 }
            goto L_0x005d
        L_0x0051:
            java.lang.ClassLoader r2 = java.lang.ClassLoader.getSystemClassLoader()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            if (r2 == 0) goto L_0x005c
            java.lang.Class r2 = r2.loadClass(r1)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            goto L_0x005d
        L_0x005c:
            r2 = r0
        L_0x005d:
            if (r2 == 0) goto L_0x0063
            java.lang.Object r0 = r2.newInstance()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
        L_0x0063:
            boolean r2 = r0 instanceof sun.security.x509.X509Key     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            if (r2 == 0) goto L_0x0095
            sun.security.x509.X509Key r0 = (sun.security.x509.X509Key) r0     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            r0.algid = r4     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            r0.setKey(r5)     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            r0.parseKeyBits()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            return r0
        L_0x0072:
            java.lang.InstantiationException r0 = new java.lang.InstantiationException     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            r0.<init>()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            throw r0     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
        L_0x0078:
            java.lang.InstantiationException r0 = new java.lang.InstantiationException     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            r0.<init>()     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
            throw r0     // Catch:{ ClassNotFoundException | InstantiationException -> 0x0095, IllegalAccessException -> 0x007e }
        L_0x007e:
            java.io.IOException r4 = new java.io.IOException
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            r5.append((java.lang.String) r1)
            java.lang.String r0 = " [internal error]"
            r5.append((java.lang.String) r0)
            java.lang.String r5 = r5.toString()
            r4.<init>((java.lang.String) r5)
            throw r4
        L_0x0095:
            sun.security.x509.X509Key r0 = new sun.security.x509.X509Key
            r0.<init>(r4, r5)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.X509Key.buildX509Key(sun.security.x509.AlgorithmId, sun.security.util.BitArray):java.security.PublicKey");
    }

    public String getAlgorithm() {
        return this.algid.getName();
    }

    public AlgorithmId getAlgorithmId() {
        return this.algid;
    }

    public final void encode(DerOutputStream derOutputStream) throws IOException {
        encode(derOutputStream, this.algid, getKey());
    }

    public byte[] getEncoded() {
        try {
            return (byte[]) getEncodedInternal().clone();
        } catch (InvalidKeyException unused) {
            return null;
        }
    }

    public byte[] getEncodedInternal() throws InvalidKeyException {
        byte[] bArr = this.encodedKey;
        if (bArr == null) {
            try {
                DerOutputStream derOutputStream = new DerOutputStream();
                encode(derOutputStream);
                bArr = derOutputStream.toByteArray();
                this.encodedKey = bArr;
            } catch (IOException e) {
                throw new InvalidKeyException("IOException : " + e.getMessage());
            }
        }
        return bArr;
    }

    public byte[] encode() throws InvalidKeyException {
        return (byte[]) getEncodedInternal().clone();
    }

    public String toString() {
        HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
        return "algorithm = " + this.algid.toString() + ", unparsed keybits = \n" + hexDumpEncoder.encodeBuffer(this.key);
    }

    public void decode(InputStream inputStream) throws InvalidKeyException {
        try {
            DerValue derValue = new DerValue(inputStream);
            if (derValue.tag == 48) {
                this.algid = AlgorithmId.parse(derValue.data.getDerValue());
                setKey(derValue.data.getUnalignedBitString());
                parseKeyBits();
                if (derValue.data.available() != 0) {
                    throw new InvalidKeyException("excess key data");
                }
                return;
            }
            throw new InvalidKeyException("invalid key format");
        } catch (IOException e) {
            throw new InvalidKeyException("IOException: " + e.getMessage());
        }
    }

    public void decode(byte[] bArr) throws InvalidKeyException {
        decode((InputStream) new ByteArrayInputStream(bArr));
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.write(getEncoded());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        try {
            decode((InputStream) objectInputStream);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            throw new IOException("deserialized key is invalid: " + e.getMessage());
        }
    }

    public boolean equals(Object obj) {
        byte[] bArr;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Key)) {
            return false;
        }
        try {
            byte[] encodedInternal = getEncodedInternal();
            if (obj instanceof X509Key) {
                bArr = ((X509Key) obj).getEncodedInternal();
            } else {
                bArr = ((Key) obj).getEncoded();
            }
            return Arrays.equals(encodedInternal, bArr);
        } catch (InvalidKeyException unused) {
            return false;
        }
    }

    public int hashCode() {
        try {
            byte[] encodedInternal = getEncodedInternal();
            int length = encodedInternal.length;
            for (byte b : encodedInternal) {
                length += (b & 255) * 37;
            }
            return length;
        } catch (InvalidKeyException unused) {
            return 0;
        }
    }

    static void encode(DerOutputStream derOutputStream, AlgorithmId algorithmId, BitArray bitArray) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        algorithmId.encode(derOutputStream2);
        derOutputStream2.putUnalignedBitString(bitArray);
        derOutputStream.write((byte) 48, derOutputStream2);
    }
}
