package sun.security.x509;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.Principal;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class X500Name implements GeneralNameInterface, Principal {
    private static final int[] DNQUALIFIER_DATA;
    public static final ObjectIdentifier DNQUALIFIER_OID;
    private static final int[] DOMAIN_COMPONENT_DATA;
    public static final ObjectIdentifier DOMAIN_COMPONENT_OID;
    private static final int[] GENERATIONQUALIFIER_DATA;
    public static final ObjectIdentifier GENERATIONQUALIFIER_OID;
    private static final int[] GIVENNAME_DATA;
    public static final ObjectIdentifier GIVENNAME_OID;
    private static final int[] INITIALS_DATA;
    public static final ObjectIdentifier INITIALS_OID;
    private static final int[] SERIALNUMBER_DATA;
    public static final ObjectIdentifier SERIALNUMBER_OID;
    private static final int[] SURNAME_DATA;
    public static final ObjectIdentifier SURNAME_OID;
    private static final int[] commonName_data;
    public static final ObjectIdentifier commonName_oid;
    private static final int[] countryName_data;
    public static final ObjectIdentifier countryName_oid;
    private static final Map<ObjectIdentifier, ObjectIdentifier> internedOIDs = new HashMap();
    private static final int[] ipAddress_data;
    public static final ObjectIdentifier ipAddress_oid;
    private static final int[] localityName_data;
    public static final ObjectIdentifier localityName_oid;
    private static final int[] orgName_data;
    public static final ObjectIdentifier orgName_oid;
    private static final int[] orgUnitName_data;
    public static final ObjectIdentifier orgUnitName_oid;
    private static final Constructor<X500Principal> principalConstructor;
    private static final Field principalField;
    private static final int[] stateName_data;
    public static final ObjectIdentifier stateName_oid;
    private static final int[] streetAddress_data;
    public static final ObjectIdentifier streetAddress_oid;
    private static final int[] title_data;
    public static final ObjectIdentifier title_oid;
    private static final int[] userid_data;
    public static final ObjectIdentifier userid_oid;
    private volatile List<AVA> allAvaList;
    private String canonicalDn;

    /* renamed from: dn */
    private String f936dn;
    private byte[] encoded;
    private RDN[] names;
    private volatile List<RDN> rdnList;
    private String rfc1779Dn;
    private String rfc2253Dn;
    private X500Principal x500Principal;

    public int getType() {
        return 4;
    }

    public X500Name(String str) throws IOException {
        this(str, (Map<String, String>) Collections.emptyMap());
    }

    public X500Name(String str, Map<String, String> map) throws IOException {
        parseDN(str, map);
    }

    public X500Name(String str, String str2) throws IOException {
        if (str == null) {
            throw new NullPointerException("Name must not be null");
        } else if (str2.equalsIgnoreCase(X500Principal.RFC2253)) {
            parseRFC2253DN(str);
        } else if (str2.equalsIgnoreCase("DEFAULT")) {
            parseDN(str, Collections.emptyMap());
        } else {
            throw new IOException("Unsupported format " + str2);
        }
    }

    public X500Name(String str, String str2, String str3, String str4) throws IOException {
        RDN[] rdnArr = new RDN[4];
        this.names = rdnArr;
        rdnArr[3] = new RDN(1);
        this.names[3].assertion[0] = new AVA(commonName_oid, new DerValue(str));
        this.names[2] = new RDN(1);
        this.names[2].assertion[0] = new AVA(orgUnitName_oid, new DerValue(str2));
        this.names[1] = new RDN(1);
        this.names[1].assertion[0] = new AVA(orgName_oid, new DerValue(str3));
        this.names[0] = new RDN(1);
        this.names[0].assertion[0] = new AVA(countryName_oid, new DerValue(str4));
    }

    public X500Name(String str, String str2, String str3, String str4, String str5, String str6) throws IOException {
        RDN[] rdnArr = new RDN[6];
        this.names = rdnArr;
        rdnArr[5] = new RDN(1);
        this.names[5].assertion[0] = new AVA(commonName_oid, new DerValue(str));
        this.names[4] = new RDN(1);
        this.names[4].assertion[0] = new AVA(orgUnitName_oid, new DerValue(str2));
        this.names[3] = new RDN(1);
        this.names[3].assertion[0] = new AVA(orgName_oid, new DerValue(str3));
        this.names[2] = new RDN(1);
        this.names[2].assertion[0] = new AVA(localityName_oid, new DerValue(str4));
        this.names[1] = new RDN(1);
        this.names[1].assertion[0] = new AVA(stateName_oid, new DerValue(str5));
        this.names[0] = new RDN(1);
        this.names[0].assertion[0] = new AVA(countryName_oid, new DerValue(str6));
    }

    public X500Name(RDN[] rdnArr) throws IOException {
        int i = 0;
        if (rdnArr == null) {
            this.names = new RDN[0];
            return;
        }
        this.names = (RDN[]) rdnArr.clone();
        while (true) {
            RDN[] rdnArr2 = this.names;
            if (i >= rdnArr2.length) {
                return;
            }
            if (rdnArr2[i] != null) {
                i++;
            } else {
                throw new IOException("Cannot create an X500Name");
            }
        }
    }

    public X500Name(DerValue derValue) throws IOException {
        this(derValue.toDerInputStream());
    }

    public X500Name(DerInputStream derInputStream) throws IOException {
        parseDER(derInputStream);
    }

    public X500Name(byte[] bArr) throws IOException {
        parseDER(new DerInputStream(bArr));
    }

    public List<RDN> rdns() {
        List<RDN> list = this.rdnList;
        if (list != null) {
            return list;
        }
        List<RDN> unmodifiableList = Collections.unmodifiableList(Arrays.asList(this.names));
        this.rdnList = unmodifiableList;
        return unmodifiableList;
    }

    public int size() {
        return this.names.length;
    }

    public List<AVA> allAvas() {
        List<AVA> list = this.allAvaList;
        if (list != null) {
            return list;
        }
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (true) {
            RDN[] rdnArr = this.names;
            if (i < rdnArr.length) {
                arrayList.addAll(rdnArr[i].avas());
                i++;
            } else {
                List<AVA> unmodifiableList = Collections.unmodifiableList(arrayList);
                this.allAvaList = unmodifiableList;
                return unmodifiableList;
            }
        }
    }

    public int avaSize() {
        return allAvas().size();
    }

    public boolean isEmpty() {
        for (RDN rdn : this.names) {
            if (rdn.assertion.length != 0) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        return getRFC2253CanonicalName().hashCode();
    }

    public boolean equals(Object obj) {
        String str;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X500Name)) {
            return false;
        }
        X500Name x500Name = (X500Name) obj;
        String str2 = this.canonicalDn;
        if (str2 != null && (str = x500Name.canonicalDn) != null) {
            return str2.equals(str);
        }
        int length = this.names.length;
        if (length != x500Name.names.length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (this.names[i].assertion.length != x500Name.names[i].assertion.length) {
                return false;
            }
        }
        return getRFC2253CanonicalName().equals(x500Name.getRFC2253CanonicalName());
    }

    private String getString(DerValue derValue) throws IOException {
        if (derValue == null) {
            return null;
        }
        String asString = derValue.getAsString();
        if (asString != null) {
            return asString;
        }
        throw new IOException("not a DER string encoding, " + derValue.tag);
    }

    public String getCountry() throws IOException {
        return getString(findAttribute(countryName_oid));
    }

    public String getOrganization() throws IOException {
        return getString(findAttribute(orgName_oid));
    }

    public String getOrganizationalUnit() throws IOException {
        return getString(findAttribute(orgUnitName_oid));
    }

    public String getCommonName() throws IOException {
        return getString(findAttribute(commonName_oid));
    }

    public String getLocality() throws IOException {
        return getString(findAttribute(localityName_oid));
    }

    public String getState() throws IOException {
        return getString(findAttribute(stateName_oid));
    }

    public String getDomain() throws IOException {
        return getString(findAttribute(DOMAIN_COMPONENT_OID));
    }

    public String getDNQualifier() throws IOException {
        return getString(findAttribute(DNQUALIFIER_OID));
    }

    public String getSurname() throws IOException {
        return getString(findAttribute(SURNAME_OID));
    }

    public String getGivenName() throws IOException {
        return getString(findAttribute(GIVENNAME_OID));
    }

    public String getInitials() throws IOException {
        return getString(findAttribute(INITIALS_OID));
    }

    public String getGeneration() throws IOException {
        return getString(findAttribute(GENERATIONQUALIFIER_OID));
    }

    public String getIP() throws IOException {
        return getString(findAttribute(ipAddress_oid));
    }

    public String toString() {
        if (this.f936dn == null) {
            generateDN();
        }
        return this.f936dn;
    }

    public String getRFC1779Name() {
        return getRFC1779Name(Collections.emptyMap());
    }

    public String getRFC1779Name(Map<String, String> map) throws IllegalArgumentException {
        if (!map.isEmpty()) {
            return generateRFC1779DN(map);
        }
        String str = this.rfc1779Dn;
        if (str != null) {
            return str;
        }
        String generateRFC1779DN = generateRFC1779DN(map);
        this.rfc1779Dn = generateRFC1779DN;
        return generateRFC1779DN;
    }

    public String getRFC2253Name() {
        return getRFC2253Name(Collections.emptyMap());
    }

    public String getRFC2253Name(Map<String, String> map) {
        if (!map.isEmpty()) {
            return generateRFC2253DN(map);
        }
        String str = this.rfc2253Dn;
        if (str != null) {
            return str;
        }
        String generateRFC2253DN = generateRFC2253DN(map);
        this.rfc2253Dn = generateRFC2253DN;
        return generateRFC2253DN;
    }

    private String generateRFC2253DN(Map<String, String> map) {
        if (this.names.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(48);
        for (int length = this.names.length - 1; length >= 0; length--) {
            if (length < this.names.length - 1) {
                sb.append(',');
            }
            sb.append(this.names[length].toRFC2253String(map));
        }
        return sb.toString();
    }

    public String getRFC2253CanonicalName() {
        String str = this.canonicalDn;
        if (str != null) {
            return str;
        }
        if (this.names.length == 0) {
            this.canonicalDn = "";
            return "";
        }
        StringBuilder sb = new StringBuilder(48);
        for (int length = this.names.length - 1; length >= 0; length--) {
            if (length < this.names.length - 1) {
                sb.append(',');
            }
            sb.append(this.names[length].toRFC2253String(true));
        }
        String sb2 = sb.toString();
        this.canonicalDn = sb2;
        return sb2;
    }

    public String getName() {
        return toString();
    }

    private DerValue findAttribute(ObjectIdentifier objectIdentifier) {
        if (this.names == null) {
            return null;
        }
        int i = 0;
        while (true) {
            RDN[] rdnArr = this.names;
            if (i >= rdnArr.length) {
                return null;
            }
            DerValue findAttribute = rdnArr[i].findAttribute(objectIdentifier);
            if (findAttribute != null) {
                return findAttribute;
            }
            i++;
        }
    }

    public DerValue findMostSpecificAttribute(ObjectIdentifier objectIdentifier) {
        RDN[] rdnArr = this.names;
        if (rdnArr == null) {
            return null;
        }
        for (int length = rdnArr.length - 1; length >= 0; length--) {
            DerValue findAttribute = this.names[length].findAttribute(objectIdentifier);
            if (findAttribute != null) {
                return findAttribute;
            }
        }
        return null;
    }

    private void parseDER(DerInputStream derInputStream) throws IOException {
        DerValue[] derValueArr;
        byte[] byteArray = derInputStream.toByteArray();
        try {
            derValueArr = derInputStream.getSequence(5);
        } catch (IOException unused) {
            derValueArr = byteArray == null ? null : new DerInputStream(new DerValue((byte) 48, byteArray).toByteArray()).getSequence(5);
        }
        if (derValueArr == null) {
            this.names = new RDN[0];
            return;
        }
        this.names = new RDN[derValueArr.length];
        for (int i = 0; i < derValueArr.length; i++) {
            this.names[i] = new RDN(derValueArr[i]);
        }
    }

    @Deprecated
    public void emit(DerOutputStream derOutputStream) throws IOException {
        encode(derOutputStream);
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        int i = 0;
        while (true) {
            RDN[] rdnArr = this.names;
            if (i < rdnArr.length) {
                rdnArr[i].encode(derOutputStream2);
                i++;
            } else {
                derOutputStream.write((byte) 48, derOutputStream2);
                return;
            }
        }
    }

    public byte[] getEncodedInternal() throws IOException {
        if (this.encoded == null) {
            DerOutputStream derOutputStream = new DerOutputStream();
            DerOutputStream derOutputStream2 = new DerOutputStream();
            int i = 0;
            while (true) {
                RDN[] rdnArr = this.names;
                if (i >= rdnArr.length) {
                    break;
                }
                rdnArr[i].encode(derOutputStream2);
                i++;
            }
            derOutputStream.write((byte) 48, derOutputStream2);
            this.encoded = derOutputStream.toByteArray();
        }
        return this.encoded;
    }

    public byte[] getEncoded() throws IOException {
        return (byte[]) getEncodedInternal().clone();
    }

    private void parseDN(String str, Map<String, String> map) throws IOException {
        if (str == null || str.length() == 0) {
            this.names = new RDN[0];
            return;
        }
        checkNoNewLinesNorTabsAtBeginningOfDN(str);
        ArrayList arrayList = new ArrayList();
        int indexOf = str.indexOf(44);
        int indexOf2 = str.indexOf(59);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (indexOf >= 0 || indexOf2 >= 0) {
                if (indexOf2 >= 0) {
                    indexOf = indexOf < 0 ? indexOf2 : Math.min(indexOf, indexOf2);
                }
                i2 += countQuotes(str, i3, indexOf);
                if (indexOf >= 0 && i2 != 1 && !escaped(indexOf, i3, str)) {
                    arrayList.add(new RDN(str.substring(i, indexOf), map));
                    i = indexOf + 1;
                    i2 = 0;
                }
                i3 = indexOf + 1;
                indexOf = str.indexOf(44, i3);
                indexOf2 = str.indexOf(59, i3);
            } else {
                arrayList.add(new RDN(str.substring(i), map));
                Collections.reverse(arrayList);
                this.names = (RDN[]) arrayList.toArray(new RDN[arrayList.size()]);
                return;
            }
        }
    }

    private void checkNoNewLinesNorTabsAtBeginningOfDN(String str) {
        int i = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt == ' ') {
                i++;
            } else if (charAt == 9 || charAt == 10) {
                throw new IllegalArgumentException("DN cannot start with newline nor tab");
            } else {
                return;
            }
        }
    }

    private void parseRFC2253DN(String str) throws IOException {
        int i = 0;
        if (str.length() == 0) {
            this.names = new RDN[0];
            return;
        }
        ArrayList arrayList = new ArrayList();
        int indexOf = str.indexOf(44);
        int i2 = 0;
        while (indexOf >= 0) {
            if (indexOf > 0 && !escaped(indexOf, i2, str)) {
                arrayList.add(new RDN(str.substring(i, indexOf), X500Principal.RFC2253));
                i = indexOf + 1;
            }
            i2 = indexOf + 1;
            indexOf = str.indexOf(44, i2);
        }
        arrayList.add(new RDN(str.substring(i), X500Principal.RFC2253));
        Collections.reverse(arrayList);
        this.names = (RDN[]) arrayList.toArray(new RDN[arrayList.size()]);
    }

    static int countQuotes(String str, int i, int i2) {
        int i3 = 0;
        int i4 = 0;
        while (i < i2) {
            if (str.charAt(i) == '\"' && i4 % 2 == 0) {
                i3++;
            }
            i4 = str.charAt(i) == '\\' ? i4 + 1 : 0;
            i++;
        }
        return i3;
    }

    private static boolean escaped(int i, int i2, String str) {
        if (i == 1 && str.charAt(i - 1) == '\\') {
            return true;
        }
        if (i > 1 && str.charAt(i - 1) == '\\' && str.charAt(i - 2) != '\\') {
            return true;
        }
        if (i <= 1 || str.charAt(i - 1) != '\\' || str.charAt(i - 2) != '\\') {
            return false;
        }
        int i3 = 0;
        for (int i4 = i - 1; i4 >= i2; i4--) {
            if (str.charAt(i4) == '\\') {
                i3++;
            }
        }
        if (i3 % 2 != 0) {
            return true;
        }
        return false;
    }

    private void generateDN() {
        RDN[] rdnArr = this.names;
        if (rdnArr.length == 1) {
            this.f936dn = rdnArr[0].toString();
            return;
        }
        StringBuilder sb = new StringBuilder(48);
        RDN[] rdnArr2 = this.names;
        if (rdnArr2 != null) {
            for (int length = rdnArr2.length - 1; length >= 0; length--) {
                if (length != this.names.length - 1) {
                    sb.append(", ");
                }
                sb.append(this.names[length].toString());
            }
        }
        this.f936dn = sb.toString();
    }

    private String generateRFC1779DN(Map<String, String> map) {
        RDN[] rdnArr = this.names;
        if (rdnArr.length == 1) {
            return rdnArr[0].toRFC1779String(map);
        }
        StringBuilder sb = new StringBuilder(48);
        RDN[] rdnArr2 = this.names;
        if (rdnArr2 != null) {
            for (int length = rdnArr2.length - 1; length >= 0; length--) {
                if (length != this.names.length - 1) {
                    sb.append(", ");
                }
                sb.append(this.names[length].toRFC1779String(map));
            }
        }
        return sb.toString();
    }

    static ObjectIdentifier intern(ObjectIdentifier objectIdentifier) {
        ObjectIdentifier putIfAbsent = internedOIDs.putIfAbsent(objectIdentifier, objectIdentifier);
        return putIfAbsent == null ? objectIdentifier : putIfAbsent;
    }

    static {
        int[] iArr = {2, 5, 4, 3};
        commonName_data = iArr;
        int[] iArr2 = {2, 5, 4, 4};
        SURNAME_DATA = iArr2;
        int[] iArr3 = {2, 5, 4, 5};
        SERIALNUMBER_DATA = iArr3;
        int[] iArr4 = {2, 5, 4, 6};
        countryName_data = iArr4;
        int[] iArr5 = {2, 5, 4, 7};
        localityName_data = iArr5;
        int[] iArr6 = {2, 5, 4, 8};
        stateName_data = iArr6;
        int[] iArr7 = {2, 5, 4, 9};
        streetAddress_data = iArr7;
        int[] iArr8 = {2, 5, 4, 10};
        orgName_data = iArr8;
        int[] iArr9 = {2, 5, 4, 11};
        orgUnitName_data = iArr9;
        int[] iArr10 = {2, 5, 4, 12};
        title_data = iArr10;
        int[] iArr11 = {2, 5, 4, 42};
        GIVENNAME_DATA = iArr11;
        int[] iArr12 = {2, 5, 4, 43};
        INITIALS_DATA = iArr12;
        int[] iArr13 = {2, 5, 4, 44};
        GENERATIONQUALIFIER_DATA = iArr13;
        int[] iArr14 = {2, 5, 4, 46};
        DNQUALIFIER_DATA = iArr14;
        int[] iArr15 = {1, 3, 6, 1, 4, 1, 42, 2, 11, 2, 1};
        ipAddress_data = iArr15;
        int[] iArr16 = iArr15;
        int[] iArr17 = {0, 9, 2342, 19200300, 100, 1, 25};
        DOMAIN_COMPONENT_DATA = iArr17;
        int[] iArr18 = {0, 9, 2342, 19200300, 100, 1, 1};
        userid_data = iArr18;
        commonName_oid = intern(ObjectIdentifier.newInternal(iArr));
        SERIALNUMBER_OID = intern(ObjectIdentifier.newInternal(iArr3));
        countryName_oid = intern(ObjectIdentifier.newInternal(iArr4));
        localityName_oid = intern(ObjectIdentifier.newInternal(iArr5));
        orgName_oid = intern(ObjectIdentifier.newInternal(iArr8));
        orgUnitName_oid = intern(ObjectIdentifier.newInternal(iArr9));
        stateName_oid = intern(ObjectIdentifier.newInternal(iArr6));
        streetAddress_oid = intern(ObjectIdentifier.newInternal(iArr7));
        title_oid = intern(ObjectIdentifier.newInternal(iArr10));
        DNQUALIFIER_OID = intern(ObjectIdentifier.newInternal(iArr14));
        SURNAME_OID = intern(ObjectIdentifier.newInternal(iArr2));
        GIVENNAME_OID = intern(ObjectIdentifier.newInternal(iArr11));
        INITIALS_OID = intern(ObjectIdentifier.newInternal(iArr12));
        GENERATIONQUALIFIER_OID = intern(ObjectIdentifier.newInternal(iArr13));
        ipAddress_oid = intern(ObjectIdentifier.newInternal(iArr16));
        DOMAIN_COMPONENT_OID = intern(ObjectIdentifier.newInternal(iArr17));
        userid_oid = intern(ObjectIdentifier.newInternal(iArr18));
        try {
            Object[] objArr = (Object[]) AccessController.doPrivileged(new PrivilegedExceptionAction<Object[]>() {
                public Object[] run() throws Exception {
                    Class<X500Principal> cls = X500Principal.class;
                    Constructor<X500Principal> declaredConstructor = cls.getDeclaredConstructor(X500Name.class);
                    declaredConstructor.setAccessible(true);
                    Field declaredField = cls.getDeclaredField("thisX500Name");
                    declaredField.setAccessible(true);
                    return new Object[]{declaredConstructor, declaredField};
                }
            });
            principalConstructor = (Constructor) objArr[0];
            principalField = (Field) objArr[1];
        } catch (Exception e) {
            throw new InternalError("Could not obtain X500Principal access", e);
        }
    }

    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        if (generalNameInterface == null || generalNameInterface.getType() != 4) {
            return -1;
        }
        X500Name x500Name = (X500Name) generalNameInterface;
        if (x500Name.equals(this)) {
            return 0;
        }
        if (x500Name.names.length != 0) {
            if (this.names.length == 0 || x500Name.isWithinSubtree(this)) {
                return 1;
            }
            if (!isWithinSubtree(x500Name)) {
                return 3;
            }
        }
        return 2;
    }

    private boolean isWithinSubtree(X500Name x500Name) {
        if (this == x500Name) {
            return true;
        }
        if (x500Name == null) {
            return false;
        }
        RDN[] rdnArr = x500Name.names;
        if (rdnArr.length == 0) {
            return true;
        }
        RDN[] rdnArr2 = this.names;
        if (rdnArr2.length == 0 || rdnArr2.length < rdnArr.length) {
            return false;
        }
        int i = 0;
        while (true) {
            RDN[] rdnArr3 = x500Name.names;
            if (i >= rdnArr3.length) {
                return true;
            }
            if (!this.names[i].equals(rdnArr3[i])) {
                return false;
            }
            i++;
        }
    }

    public int subtreeDepth() throws UnsupportedOperationException {
        return this.names.length;
    }

    public X500Name commonAncestor(X500Name x500Name) {
        if (x500Name == null) {
            return null;
        }
        int length = x500Name.names.length;
        int length2 = this.names.length;
        if (!(length2 == 0 || length == 0)) {
            if (length2 < length) {
                length = length2;
            }
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (this.names[i].equals(x500Name.names[i])) {
                    i++;
                } else if (i == 0) {
                    return null;
                }
            }
            RDN[] rdnArr = new RDN[i];
            for (int i2 = 0; i2 < i; i2++) {
                rdnArr[i2] = this.names[i2];
            }
            try {
                return new X500Name(rdnArr);
            } catch (IOException unused) {
            }
        }
        return null;
    }

    public X500Principal asX500Principal() {
        if (this.x500Principal == null) {
            try {
                this.x500Principal = principalConstructor.newInstance(this);
            } catch (Exception e) {
                throw new RuntimeException("Unexpected exception", e);
            }
        }
        return this.x500Principal;
    }

    public static X500Name asX500Name(X500Principal x500Principal2) {
        try {
            X500Name x500Name = (X500Name) principalField.get(x500Principal2);
            x500Name.x500Principal = x500Principal2;
            return x500Name;
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }
}
