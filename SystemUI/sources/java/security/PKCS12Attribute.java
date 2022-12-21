package java.security;

import java.math.BigInteger;
import java.p026io.IOException;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.regex.Pattern;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public final class PKCS12Attribute implements KeyStore.Entry.Attribute {
    private static final Pattern COLON_SEPARATED_HEX_PAIRS = Pattern.compile("^[0-9a-fA-F]{2}(:[0-9a-fA-F]{2})+$");
    private final byte[] encoded;
    private int hashValue = -1;
    private String name;
    private String value;

    public PKCS12Attribute(String str, String str2) {
        String[] strArr;
        if (str == null || str2 == null) {
            throw null;
        }
        try {
            ObjectIdentifier objectIdentifier = new ObjectIdentifier(str);
            this.name = str;
            int length = str2.length();
            if (length > 1 && str2.charAt(0) == '[') {
                int i = length - 1;
                if (str2.charAt(i) == ']') {
                    strArr = str2.substring(1, i).split(", ");
                    this.value = str2;
                    this.encoded = encode(objectIdentifier, strArr);
                }
            }
            strArr = new String[]{str2};
            this.value = str2;
            try {
                this.encoded = encode(objectIdentifier, strArr);
            } catch (IOException e) {
                throw new IllegalArgumentException("Incorrect format: value", e);
            }
        } catch (IOException e2) {
            throw new IllegalArgumentException("Incorrect format: name", e2);
        }
    }

    public PKCS12Attribute(byte[] bArr) {
        bArr.getClass();
        this.encoded = (byte[]) bArr.clone();
        try {
            parse(bArr);
        } catch (IOException e) {
            throw new IllegalArgumentException("Incorrect format: encoded", e);
        }
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public byte[] getEncoded() {
        return (byte[]) this.encoded.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PKCS12Attribute)) {
            return false;
        }
        return Arrays.equals(this.encoded, ((PKCS12Attribute) obj).encoded);
    }

    public int hashCode() {
        int i = this.hashValue;
        if (i != -1) {
            return i;
        }
        int hashCode = Arrays.hashCode(this.encoded);
        this.hashValue = hashCode;
        return hashCode;
    }

    public String toString() {
        return this.name + "=" + this.value;
    }

    private byte[] encode(ObjectIdentifier objectIdentifier, String[] strArr) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOID(objectIdentifier);
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (String str : strArr) {
            if (COLON_SEPARATED_HEX_PAIRS.matcher(str).matches()) {
                byte[] byteArray = new BigInteger(str.replace((CharSequence) ":", (CharSequence) ""), 16).toByteArray();
                if (byteArray[0] == 0) {
                    byteArray = Arrays.copyOfRange(byteArray, 1, byteArray.length);
                }
                derOutputStream2.putOctetString(byteArray);
            } else {
                derOutputStream2.putUTF8String(str);
            }
        }
        derOutputStream.write((byte) 49, derOutputStream2);
        DerOutputStream derOutputStream3 = new DerOutputStream();
        derOutputStream3.write((byte) 48, derOutputStream);
        return derOutputStream3.toByteArray();
    }

    private void parse(byte[] bArr) throws IOException {
        String str;
        DerValue[] sequence = new DerInputStream(bArr).getSequence(2);
        if (sequence.length == 2) {
            ObjectIdentifier oid = sequence[0].getOID();
            DerValue[] set = new DerInputStream(sequence[1].toByteArray()).getSet(1);
            int length = set.length;
            String[] strArr = new String[length];
            for (int i = 0; i < set.length; i++) {
                if (set[i].tag == 4) {
                    strArr[i] = Debug.toString(set[i].getOctetString());
                } else {
                    String asString = set[i].getAsString();
                    if (asString != null) {
                        strArr[i] = asString;
                    } else if (set[i].tag == 6) {
                        strArr[i] = set[i].getOID().toString();
                    } else if (set[i].tag == 24) {
                        strArr[i] = set[i].getGeneralizedTime().toString();
                    } else if (set[i].tag == 23) {
                        strArr[i] = set[i].getUTCTime().toString();
                    } else if (set[i].tag == 2) {
                        strArr[i] = set[i].getBigInteger().toString();
                    } else if (set[i].tag == 1) {
                        strArr[i] = String.valueOf(set[i].getBoolean());
                    } else {
                        strArr[i] = Debug.toString(set[i].getDataBytes());
                    }
                }
            }
            this.name = oid.toString();
            if (length == 1) {
                str = strArr[0];
            } else {
                str = Arrays.toString((Object[]) strArr);
            }
            this.value = str;
            return;
        }
        throw new IOException("Invalid length for PKCS12Attribute");
    }
}
