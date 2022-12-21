package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.p026io.Serializable;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.xml.datatype.DatatypeConstants;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;
import sun.util.locale.LanguageTag;

public class AlgorithmId implements Serializable, DerEncoder {
    public static final ObjectIdentifier AES_oid;
    private static final int[] DH_PKIX_data;
    public static final ObjectIdentifier DH_PKIX_oid;
    private static final int[] DH_data;
    public static final ObjectIdentifier DH_oid;
    private static final int[] DSA_OIW_data;
    public static final ObjectIdentifier DSA_OIW_oid;
    private static final int[] DSA_PKIX_data;
    public static final ObjectIdentifier DSA_oid;
    public static final ObjectIdentifier ECDH_oid;
    public static final ObjectIdentifier EC_oid;
    public static final ObjectIdentifier MD2_oid;
    public static final ObjectIdentifier MD5_oid;
    private static final int[] RSAEncryption_data;
    public static final ObjectIdentifier RSAEncryption_oid;
    private static final int[] RSA_data;
    public static final ObjectIdentifier RSA_oid;
    public static final ObjectIdentifier SHA224_oid;
    public static final ObjectIdentifier SHA256_oid;
    public static final ObjectIdentifier SHA384_oid;
    public static final ObjectIdentifier SHA512_oid;
    public static final ObjectIdentifier SHA_oid;
    private static final int[] dsaWithSHA1_PKIX_data;
    private static int initOidTableVersion = -1;
    private static final int[] md2WithRSAEncryption_data;
    public static final ObjectIdentifier md2WithRSAEncryption_oid;
    private static final int[] md5WithRSAEncryption_data;
    public static final ObjectIdentifier md5WithRSAEncryption_oid;
    private static final Map<ObjectIdentifier, String> nameTable;
    private static final Map<String, ObjectIdentifier> oidTable = new HashMap(1);
    public static final ObjectIdentifier pbeWithMD5AndDES_oid;
    public static final ObjectIdentifier pbeWithMD5AndRC2_oid;
    public static final ObjectIdentifier pbeWithSHA1AndDES_oid;
    public static ObjectIdentifier pbeWithSHA1AndDESede_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 1, 3});
    public static ObjectIdentifier pbeWithSHA1AndRC2_40_oid = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 12, 1, 6});
    public static final ObjectIdentifier pbeWithSHA1AndRC2_oid;
    private static final long serialVersionUID = 7205873507486557157L;
    private static final int[] sha1WithDSA_OIW_data;
    public static final ObjectIdentifier sha1WithDSA_OIW_oid;
    public static final ObjectIdentifier sha1WithDSA_oid;
    public static final ObjectIdentifier sha1WithECDSA_oid;
    private static final int[] sha1WithRSAEncryption_OIW_data;
    public static final ObjectIdentifier sha1WithRSAEncryption_OIW_oid;
    private static final int[] sha1WithRSAEncryption_data;
    public static final ObjectIdentifier sha1WithRSAEncryption_oid;
    public static final ObjectIdentifier sha224WithDSA_oid;
    public static final ObjectIdentifier sha224WithECDSA_oid;
    private static final int[] sha224WithRSAEncryption_data;
    public static final ObjectIdentifier sha224WithRSAEncryption_oid;
    public static final ObjectIdentifier sha256WithDSA_oid;
    public static final ObjectIdentifier sha256WithECDSA_oid;
    private static final int[] sha256WithRSAEncryption_data;
    public static final ObjectIdentifier sha256WithRSAEncryption_oid;
    public static final ObjectIdentifier sha384WithECDSA_oid;
    private static final int[] sha384WithRSAEncryption_data;
    public static final ObjectIdentifier sha384WithRSAEncryption_oid;
    public static final ObjectIdentifier sha512WithECDSA_oid;
    private static final int[] sha512WithRSAEncryption_data;
    public static final ObjectIdentifier sha512WithRSAEncryption_oid;
    private static final int[] shaWithDSA_OIW_data;
    public static final ObjectIdentifier shaWithDSA_OIW_oid;
    public static final ObjectIdentifier specifiedWithECDSA_oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3);
    private AlgorithmParameters algParams;
    private ObjectIdentifier algid;
    private boolean constructedFromDer = true;
    protected DerValue params;

    @Deprecated
    public AlgorithmId() {
    }

    public AlgorithmId(ObjectIdentifier objectIdentifier) {
        this.algid = objectIdentifier;
    }

    public AlgorithmId(ObjectIdentifier objectIdentifier, AlgorithmParameters algorithmParameters) {
        this.algid = objectIdentifier;
        this.algParams = algorithmParameters;
    }

    private AlgorithmId(ObjectIdentifier objectIdentifier, DerValue derValue) throws IOException {
        this.algid = objectIdentifier;
        this.params = derValue;
        if (derValue != null) {
            decodeParams();
        }
    }

    /* access modifiers changed from: protected */
    public void decodeParams() throws IOException {
        try {
            AlgorithmParameters instance = AlgorithmParameters.getInstance(this.algid.toString());
            this.algParams = instance;
            instance.init(this.params.toByteArray());
        } catch (NoSuchAlgorithmException unused) {
            this.algParams = null;
        }
    }

    public final void encode(DerOutputStream derOutputStream) throws IOException {
        derEncode(derOutputStream);
    }

    public void derEncode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream.putOID(this.algid);
        if (!this.constructedFromDer) {
            AlgorithmParameters algorithmParameters = this.algParams;
            if (algorithmParameters != null) {
                this.params = new DerValue(algorithmParameters.getEncoded());
            } else {
                this.params = null;
            }
        }
        DerValue derValue = this.params;
        if (derValue == null) {
            derOutputStream.putNull();
        } else {
            derOutputStream.putDerValue(derValue);
        }
        derOutputStream2.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream2.toByteArray());
    }

    public final byte[] encode() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derEncode(derOutputStream);
        return derOutputStream.toByteArray();
    }

    public final ObjectIdentifier getOID() {
        return this.algid;
    }

    public String getName() {
        String str;
        String str2 = nameTable.get(this.algid);
        if (str2 != null) {
            return str2;
        }
        if (this.params != null && this.algid.equals((Object) specifiedWithECDSA_oid)) {
            try {
                makeSigAlg(parse(new DerValue(getEncodedParams())).getName(), "EC");
            } catch (IOException unused) {
            }
        }
        synchronized (oidTable) {
            reinitializeMappingTableLocked();
            str = nameTable.get(this.algid);
        }
        return str == null ? this.algid.toString() : str;
    }

    public AlgorithmParameters getParameters() {
        return this.algParams;
    }

    public byte[] getEncodedParams() throws IOException {
        DerValue derValue = this.params;
        if (derValue == null) {
            return null;
        }
        return derValue.toByteArray();
    }

    public boolean equals(AlgorithmId algorithmId) {
        DerValue derValue = this.params;
        boolean equals = derValue == null ? algorithmId.params == null : derValue.equals(algorithmId.params);
        if (!this.algid.equals((Object) algorithmId.algid) || !equals) {
            return false;
        }
        return true;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AlgorithmId) {
            return equals((AlgorithmId) obj);
        }
        if (obj instanceof ObjectIdentifier) {
            return equals((ObjectIdentifier) obj);
        }
        return false;
    }

    public final boolean equals(ObjectIdentifier objectIdentifier) {
        return this.algid.equals((Object) objectIdentifier);
    }

    public int hashCode() {
        return (this.algid.toString() + paramsToString()).hashCode();
    }

    /* access modifiers changed from: protected */
    public String paramsToString() {
        if (this.params == null) {
            return "";
        }
        AlgorithmParameters algorithmParameters = this.algParams;
        return algorithmParameters != null ? algorithmParameters.toString() : ", params unparsed";
    }

    public String toString() {
        return getName() + paramsToString();
    }

    public static AlgorithmId parse(DerValue derValue) throws IOException {
        if (derValue.tag == 48) {
            DerInputStream derInputStream = derValue.toDerInputStream();
            ObjectIdentifier oid = derInputStream.getOID();
            DerValue derValue2 = null;
            if (derInputStream.available() != 0) {
                DerValue derValue3 = derInputStream.getDerValue();
                if (derValue3.tag != 5) {
                    derValue2 = derValue3;
                } else if (derValue3.length() != 0) {
                    throw new IOException("invalid NULL");
                }
                if (derInputStream.available() != 0) {
                    throw new IOException("Invalid AlgorithmIdentifier: extra data");
                }
            }
            return new AlgorithmId(oid, derValue2);
        }
        throw new IOException("algid parse error, not a sequence");
    }

    @Deprecated
    public static AlgorithmId getAlgorithmId(String str) throws NoSuchAlgorithmException {
        return get(str);
    }

    public static AlgorithmId get(String str) throws NoSuchAlgorithmException {
        try {
            ObjectIdentifier algOID = algOID(str);
            if (algOID != null) {
                return new AlgorithmId(algOID);
            }
            throw new NoSuchAlgorithmException("unrecognized algorithm name: " + str);
        } catch (IOException unused) {
            throw new NoSuchAlgorithmException("Invalid ObjectIdentifier " + str);
        }
    }

    public static AlgorithmId get(AlgorithmParameters algorithmParameters) throws NoSuchAlgorithmException {
        String algorithm = algorithmParameters.getAlgorithm();
        try {
            ObjectIdentifier algOID = algOID(algorithm);
            if (algOID != null) {
                return new AlgorithmId(algOID, algorithmParameters);
            }
            throw new NoSuchAlgorithmException("unrecognized algorithm name: " + algorithm);
        } catch (IOException unused) {
            throw new NoSuchAlgorithmException("Invalid ObjectIdentifier " + algorithm);
        }
    }

    private static ObjectIdentifier algOID(String str) throws IOException {
        ObjectIdentifier objectIdentifier;
        if (str.indexOf(46) != -1) {
            if (str.startsWith("OID.")) {
                return new ObjectIdentifier(str.substring(4));
            }
            return new ObjectIdentifier(str);
        } else if (str.equalsIgnoreCase("MD5")) {
            return MD5_oid;
        } else {
            if (str.equalsIgnoreCase("MD2")) {
                return MD2_oid;
            }
            if (str.equalsIgnoreCase("SHA") || str.equalsIgnoreCase("SHA1") || str.equalsIgnoreCase("SHA-1")) {
                return SHA_oid;
            }
            if (str.equalsIgnoreCase("SHA-256") || str.equalsIgnoreCase("SHA256")) {
                return SHA256_oid;
            }
            if (str.equalsIgnoreCase("SHA-384") || str.equalsIgnoreCase("SHA384")) {
                return SHA384_oid;
            }
            if (str.equalsIgnoreCase("SHA-512") || str.equalsIgnoreCase("SHA512")) {
                return SHA512_oid;
            }
            if (str.equalsIgnoreCase("SHA-224") || str.equalsIgnoreCase("SHA224")) {
                return SHA224_oid;
            }
            if (str.equalsIgnoreCase("RSA")) {
                return RSAEncryption_oid;
            }
            if (str.equalsIgnoreCase("Diffie-Hellman") || str.equalsIgnoreCase("DH")) {
                return DH_oid;
            }
            if (str.equalsIgnoreCase("DSA")) {
                return DSA_oid;
            }
            if (str.equalsIgnoreCase("EC")) {
                return EC_oid;
            }
            if (str.equalsIgnoreCase("ECDH")) {
                return ECDH_oid;
            }
            if (str.equalsIgnoreCase("AES")) {
                return AES_oid;
            }
            if (str.equalsIgnoreCase("MD5withRSA") || str.equalsIgnoreCase("MD5/RSA")) {
                return md5WithRSAEncryption_oid;
            }
            if (str.equalsIgnoreCase("MD2withRSA") || str.equalsIgnoreCase("MD2/RSA")) {
                return md2WithRSAEncryption_oid;
            }
            if (str.equalsIgnoreCase("SHAwithDSA") || str.equalsIgnoreCase("SHA1withDSA") || str.equalsIgnoreCase("SHA/DSA") || str.equalsIgnoreCase("SHA1/DSA") || str.equalsIgnoreCase("DSAWithSHA1") || str.equalsIgnoreCase("DSS") || str.equalsIgnoreCase("SHA-1/DSA")) {
                return sha1WithDSA_oid;
            }
            if (str.equalsIgnoreCase("SHA224WithDSA")) {
                return sha224WithDSA_oid;
            }
            if (str.equalsIgnoreCase("SHA256WithDSA")) {
                return sha256WithDSA_oid;
            }
            if (str.equalsIgnoreCase("SHA1WithRSA") || str.equalsIgnoreCase("SHA1/RSA")) {
                return sha1WithRSAEncryption_oid;
            }
            if (str.equalsIgnoreCase("SHA1withECDSA") || str.equalsIgnoreCase("ECDSA")) {
                return sha1WithECDSA_oid;
            }
            if (str.equalsIgnoreCase("SHA224withECDSA")) {
                return sha224WithECDSA_oid;
            }
            if (str.equalsIgnoreCase("SHA256withECDSA")) {
                return sha256WithECDSA_oid;
            }
            if (str.equalsIgnoreCase("SHA384withECDSA")) {
                return sha384WithECDSA_oid;
            }
            if (str.equalsIgnoreCase("SHA512withECDSA")) {
                return sha512WithECDSA_oid;
            }
            Map<String, ObjectIdentifier> map = oidTable;
            synchronized (map) {
                reinitializeMappingTableLocked();
                objectIdentifier = map.get(str.toUpperCase(Locale.ENGLISH));
            }
            return objectIdentifier;
        }
    }

    private static void reinitializeMappingTableLocked() {
        String property;
        int version = Security.getVersion();
        if (initOidTableVersion != version) {
            Provider[] providers = Security.getProviders();
            for (int i = 0; i < providers.length; i++) {
                Enumeration<Object> keys = providers[i].keys();
                while (keys.hasMoreElements()) {
                    String str = (String) keys.nextElement();
                    String upperCase = str.toUpperCase(Locale.ENGLISH);
                    if (upperCase.startsWith("ALG.ALIAS")) {
                        int indexOf = upperCase.indexOf("OID.", 0);
                        ObjectIdentifier objectIdentifier = null;
                        if (indexOf != -1) {
                            int i2 = indexOf + 4;
                            if (i2 == str.length()) {
                                break;
                            }
                            String substring = str.substring(i2);
                            String property2 = providers[i].getProperty(str);
                            if (property2 != null) {
                                String upperCase2 = property2.toUpperCase(Locale.ENGLISH);
                                try {
                                    objectIdentifier = new ObjectIdentifier(substring);
                                } catch (IOException unused) {
                                }
                                if (objectIdentifier != null) {
                                    Map<String, ObjectIdentifier> map = oidTable;
                                    if (!map.containsKey(upperCase2)) {
                                        map.put(upperCase2, objectIdentifier);
                                    }
                                    Map<ObjectIdentifier, String> map2 = nameTable;
                                    if (!map2.containsKey(objectIdentifier)) {
                                        map2.put(objectIdentifier, upperCase2);
                                    }
                                }
                            }
                        } else {
                            try {
                                objectIdentifier = new ObjectIdentifier(str.substring(str.indexOf(46, 10) + 1));
                            } catch (IOException unused2) {
                            }
                            if (!(objectIdentifier == null || (property = providers[i].getProperty(str)) == null)) {
                                String upperCase3 = property.toUpperCase(Locale.ENGLISH);
                                Map<String, ObjectIdentifier> map3 = oidTable;
                                if (!map3.containsKey(upperCase3)) {
                                    map3.put(upperCase3, objectIdentifier);
                                }
                                Map<ObjectIdentifier, String> map4 = nameTable;
                                if (!map4.containsKey(objectIdentifier)) {
                                    map4.put(objectIdentifier, upperCase3);
                                }
                            }
                        }
                    }
                }
            }
            initOidTableVersion = version;
        }
    }

    private static ObjectIdentifier oid(int... iArr) {
        return ObjectIdentifier.newInternal(iArr);
    }

    static {
        HashMap hashMap = new HashMap();
        nameTable = hashMap;
        ObjectIdentifier newInternal = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 2});
        MD2_oid = newInternal;
        ObjectIdentifier newInternal2 = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 2, 5});
        MD5_oid = newInternal2;
        ObjectIdentifier newInternal3 = ObjectIdentifier.newInternal(new int[]{1, 3, 14, 3, 2, 26});
        SHA_oid = newInternal3;
        ObjectIdentifier newInternal4 = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 4});
        SHA224_oid = newInternal4;
        ObjectIdentifier newInternal5 = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 1});
        SHA256_oid = newInternal5;
        ObjectIdentifier newInternal6 = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 2});
        SHA384_oid = newInternal6;
        ObjectIdentifier newInternal7 = ObjectIdentifier.newInternal(new int[]{2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 2, 3});
        SHA512_oid = newInternal7;
        int[] iArr = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 3, 1};
        DH_data = iArr;
        int[] iArr2 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10046, 2, 1};
        DH_PKIX_data = iArr2;
        int[] iArr3 = {1, 3, 14, 3, 2, 12};
        DSA_OIW_data = iArr3;
        int[] iArr4 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10040, 4, 1};
        DSA_PKIX_data = iArr4;
        int[] iArr5 = {2, 5, 8, 1, 1};
        RSA_data = iArr5;
        int[] iArr6 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 1};
        RSAEncryption_data = iArr6;
        ObjectIdentifier oid = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 2, 1);
        EC_oid = oid;
        ObjectIdentifier oid2 = oid(1, 3, 132, 1, 12);
        ECDH_oid = oid2;
        ObjectIdentifier objectIdentifier = oid2;
        ObjectIdentifier objectIdentifier2 = oid;
        ObjectIdentifier oid3 = oid(2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 1);
        AES_oid = oid3;
        ObjectIdentifier objectIdentifier3 = oid3;
        int[] iArr7 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 2};
        md2WithRSAEncryption_data = iArr7;
        ObjectIdentifier objectIdentifier4 = newInternal7;
        int[] iArr8 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 4};
        md5WithRSAEncryption_data = iArr8;
        ObjectIdentifier objectIdentifier5 = newInternal6;
        int[] iArr9 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 5};
        sha1WithRSAEncryption_data = iArr9;
        ObjectIdentifier objectIdentifier6 = newInternal5;
        int[] iArr10 = {1, 3, 14, 3, 2, 29};
        sha1WithRSAEncryption_OIW_data = iArr10;
        ObjectIdentifier objectIdentifier7 = newInternal4;
        int[] iArr11 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 14};
        sha224WithRSAEncryption_data = iArr11;
        ObjectIdentifier objectIdentifier8 = newInternal3;
        int[] iArr12 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 11};
        sha256WithRSAEncryption_data = iArr12;
        ObjectIdentifier objectIdentifier9 = newInternal;
        int[] iArr13 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 12};
        sha384WithRSAEncryption_data = iArr13;
        HashMap hashMap2 = hashMap;
        int[] iArr14 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 1, 13};
        sha512WithRSAEncryption_data = iArr14;
        ObjectIdentifier objectIdentifier10 = newInternal2;
        int[] iArr15 = {1, 3, 14, 3, 2, 13};
        shaWithDSA_OIW_data = iArr15;
        int[] iArr16 = iArr15;
        int[] iArr17 = {1, 3, 14, 3, 2, 27};
        sha1WithDSA_OIW_data = iArr17;
        int[] iArr18 = iArr17;
        int[] iArr19 = {1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10040, 4, 3};
        dsaWithSHA1_PKIX_data = iArr19;
        int[] iArr20 = iArr19;
        ObjectIdentifier oid4 = oid(2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 3, 1);
        sha224WithDSA_oid = oid4;
        ObjectIdentifier oid5 = oid(2, 16, DatatypeConstants.MIN_TIMEZONE_OFFSET, 1, 101, 3, 4, 3, 2);
        sha256WithDSA_oid = oid5;
        ObjectIdentifier objectIdentifier11 = oid5;
        ObjectIdentifier objectIdentifier12 = oid4;
        ObjectIdentifier oid6 = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 1);
        sha1WithECDSA_oid = oid6;
        ObjectIdentifier objectIdentifier13 = oid6;
        ObjectIdentifier oid7 = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3, 1);
        sha224WithECDSA_oid = oid7;
        ObjectIdentifier objectIdentifier14 = oid7;
        ObjectIdentifier oid8 = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3, 2);
        sha256WithECDSA_oid = oid8;
        ObjectIdentifier objectIdentifier15 = oid8;
        ObjectIdentifier oid9 = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3, 3);
        sha384WithECDSA_oid = oid9;
        ObjectIdentifier objectIdentifier16 = oid9;
        ObjectIdentifier oid10 = oid(1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 10045, 4, 3, 4);
        sha512WithECDSA_oid = oid10;
        ObjectIdentifier objectIdentifier17 = oid10;
        ObjectIdentifier newInternal8 = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 3});
        pbeWithMD5AndDES_oid = newInternal8;
        ObjectIdentifier objectIdentifier18 = newInternal8;
        ObjectIdentifier newInternal9 = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 6});
        pbeWithMD5AndRC2_oid = newInternal9;
        ObjectIdentifier objectIdentifier19 = newInternal9;
        ObjectIdentifier newInternal10 = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 10});
        pbeWithSHA1AndDES_oid = newInternal10;
        ObjectIdentifier newInternal11 = ObjectIdentifier.newInternal(new int[]{1, 2, DatatypeConstants.MIN_TIMEZONE_OFFSET, 113549, 1, 5, 11});
        pbeWithSHA1AndRC2_oid = newInternal11;
        ObjectIdentifier objectIdentifier20 = newInternal10;
        ObjectIdentifier objectIdentifier21 = newInternal11;
        ObjectIdentifier newInternal12 = ObjectIdentifier.newInternal(iArr);
        DH_oid = newInternal12;
        ObjectIdentifier newInternal13 = ObjectIdentifier.newInternal(iArr2);
        DH_PKIX_oid = newInternal13;
        ObjectIdentifier newInternal14 = ObjectIdentifier.newInternal(iArr3);
        DSA_OIW_oid = newInternal14;
        ObjectIdentifier newInternal15 = ObjectIdentifier.newInternal(iArr4);
        DSA_oid = newInternal15;
        ObjectIdentifier newInternal16 = ObjectIdentifier.newInternal(iArr5);
        RSA_oid = newInternal16;
        ObjectIdentifier newInternal17 = ObjectIdentifier.newInternal(iArr6);
        RSAEncryption_oid = newInternal17;
        ObjectIdentifier newInternal18 = ObjectIdentifier.newInternal(iArr7);
        md2WithRSAEncryption_oid = newInternal18;
        ObjectIdentifier newInternal19 = ObjectIdentifier.newInternal(iArr8);
        md5WithRSAEncryption_oid = newInternal19;
        ObjectIdentifier newInternal20 = ObjectIdentifier.newInternal(iArr9);
        sha1WithRSAEncryption_oid = newInternal20;
        ObjectIdentifier newInternal21 = ObjectIdentifier.newInternal(iArr10);
        sha1WithRSAEncryption_OIW_oid = newInternal21;
        ObjectIdentifier newInternal22 = ObjectIdentifier.newInternal(iArr11);
        sha224WithRSAEncryption_oid = newInternal22;
        ObjectIdentifier newInternal23 = ObjectIdentifier.newInternal(iArr12);
        sha256WithRSAEncryption_oid = newInternal23;
        ObjectIdentifier newInternal24 = ObjectIdentifier.newInternal(iArr13);
        sha384WithRSAEncryption_oid = newInternal24;
        ObjectIdentifier newInternal25 = ObjectIdentifier.newInternal(iArr14);
        sha512WithRSAEncryption_oid = newInternal25;
        ObjectIdentifier newInternal26 = ObjectIdentifier.newInternal(iArr16);
        shaWithDSA_OIW_oid = newInternal26;
        ObjectIdentifier newInternal27 = ObjectIdentifier.newInternal(iArr18);
        sha1WithDSA_OIW_oid = newInternal27;
        ObjectIdentifier objectIdentifier22 = newInternal25;
        ObjectIdentifier newInternal28 = ObjectIdentifier.newInternal(iArr20);
        sha1WithDSA_oid = newInternal28;
        ObjectIdentifier objectIdentifier23 = newInternal23;
        HashMap hashMap3 = hashMap2;
        hashMap3.put(objectIdentifier10, "MD5");
        hashMap3.put(objectIdentifier9, "MD2");
        hashMap3.put(objectIdentifier8, "SHA-1");
        hashMap3.put(objectIdentifier7, "SHA-224");
        hashMap3.put(objectIdentifier6, "SHA-256");
        hashMap3.put(objectIdentifier5, "SHA-384");
        hashMap3.put(objectIdentifier4, "SHA-512");
        hashMap3.put(newInternal17, "RSA");
        hashMap3.put(newInternal16, "RSA");
        hashMap3.put(newInternal12, "Diffie-Hellman");
        hashMap3.put(newInternal13, "Diffie-Hellman");
        hashMap3.put(newInternal15, "DSA");
        hashMap3.put(newInternal14, "DSA");
        hashMap3.put(objectIdentifier2, "EC");
        hashMap3.put(objectIdentifier, "ECDH");
        hashMap3.put(objectIdentifier3, "AES");
        hashMap3.put(objectIdentifier13, "SHA1withECDSA");
        hashMap3.put(objectIdentifier14, "SHA224withECDSA");
        hashMap3.put(objectIdentifier15, "SHA256withECDSA");
        hashMap3.put(objectIdentifier16, "SHA384withECDSA");
        hashMap3.put(objectIdentifier17, "SHA512withECDSA");
        hashMap3.put(newInternal19, "MD5withRSA");
        hashMap3.put(newInternal18, "MD2withRSA");
        hashMap3.put(newInternal28, "SHA1withDSA");
        hashMap3.put(newInternal27, "SHA1withDSA");
        hashMap3.put(newInternal26, "SHA1withDSA");
        hashMap3.put(objectIdentifier12, "SHA224withDSA");
        hashMap3.put(objectIdentifier11, "SHA256withDSA");
        hashMap3.put(newInternal20, "SHA1withRSA");
        hashMap3.put(newInternal21, "SHA1withRSA");
        hashMap3.put(newInternal22, "SHA224withRSA");
        hashMap3.put(objectIdentifier23, "SHA256withRSA");
        hashMap3.put(newInternal24, "SHA384withRSA");
        hashMap3.put(objectIdentifier22, "SHA512withRSA");
        hashMap3.put(objectIdentifier18, "PBEWithMD5AndDES");
        hashMap3.put(objectIdentifier19, "PBEWithMD5AndRC2");
        hashMap3.put(objectIdentifier20, "PBEWithSHA1AndDES");
        hashMap3.put(objectIdentifier21, "PBEWithSHA1AndRC2");
        hashMap3.put(pbeWithSHA1AndDESede_oid, "PBEWithSHA1AndDESede");
        hashMap3.put(pbeWithSHA1AndRC2_40_oid, "PBEWithSHA1AndRC2_40");
    }

    public static String makeSigAlg(String str, String str2) {
        String replace = str.replace((CharSequence) LanguageTag.SEP, (CharSequence) "");
        if (str2.equalsIgnoreCase("EC")) {
            str2 = "ECDSA";
        }
        return replace + "with" + str2;
    }

    public static String getEncAlgFromSigAlg(String str) {
        String str2;
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        int indexOf = upperCase.indexOf("WITH");
        if (indexOf <= 0) {
            return null;
        }
        int i = indexOf + 4;
        int indexOf2 = upperCase.indexOf("AND", i);
        if (indexOf2 > 0) {
            str2 = upperCase.substring(i, indexOf2);
        } else {
            str2 = upperCase.substring(i);
        }
        return str2.equalsIgnoreCase("ECDSA") ? "EC" : str2;
    }

    public static String getDigAlgFromSigAlg(String str) {
        String upperCase = str.toUpperCase(Locale.ENGLISH);
        int indexOf = upperCase.indexOf("WITH");
        if (indexOf > 0) {
            return upperCase.substring(0, indexOf);
        }
        return null;
    }
}
