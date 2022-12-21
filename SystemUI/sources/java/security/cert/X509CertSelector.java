package java.security.cert;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.p026io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.security.auth.x500.X500Principal;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;
import sun.security.util.HexDumpEncoder;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.AlgorithmId;
import sun.security.x509.CertificatePoliciesExtension;
import sun.security.x509.CertificatePolicyId;
import sun.security.x509.CertificatePolicySet;
import sun.security.x509.DNSName;
import sun.security.x509.EDIPartyName;
import sun.security.x509.ExtendedKeyUsageExtension;
import sun.security.x509.GeneralName;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.GeneralSubtree;
import sun.security.x509.GeneralSubtrees;
import sun.security.x509.IPAddressName;
import sun.security.x509.NameConstraintsExtension;
import sun.security.x509.OIDName;
import sun.security.x509.OtherName;
import sun.security.x509.PolicyInformation;
import sun.security.x509.PrivateKeyUsageExtension;
import sun.security.x509.RFC822Name;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.URIName;
import sun.security.x509.X400Address;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;
import sun.security.x509.X509Key;

public class X509CertSelector implements CertSelector {
    private static final ObjectIdentifier ANY_EXTENDED_KEY_USAGE = ObjectIdentifier.newInternal(new int[]{2, 5, 29, 37, 0});
    private static final int CERT_POLICIES_ID = 3;
    private static final int EXTENDED_KEY_USAGE_ID = 4;
    private static final String[] EXTENSION_OIDS;
    private static final Boolean FALSE = Boolean.FALSE;
    static final int NAME_ANY = 0;
    private static final int NAME_CONSTRAINTS_ID = 2;
    static final int NAME_DIRECTORY = 4;
    static final int NAME_DNS = 2;
    static final int NAME_EDI = 5;
    static final int NAME_IP = 7;
    static final int NAME_OID = 8;
    static final int NAME_RFC822 = 1;
    static final int NAME_URI = 6;
    static final int NAME_X400 = 3;
    private static final int NUM_OF_EXTENSIONS = 5;
    private static final int PRIVATE_KEY_USAGE_ID = 0;
    private static final int SUBJECT_ALT_NAME_ID = 1;
    private static final Debug debug = Debug.getInstance("certpath");
    private byte[] authorityKeyID;
    private int basicConstraints = -1;
    private Date certificateValid;
    private X500Principal issuer;
    private Set<ObjectIdentifier> keyPurposeOIDSet;
    private Set<String> keyPurposeSet;
    private boolean[] keyUsage;
    private boolean matchAllSubjectAltNames = true;

    /* renamed from: nc */
    private NameConstraintsExtension f200nc;
    private byte[] ncBytes;
    private Set<GeneralNameInterface> pathToGeneralNames;
    private Set<List<?>> pathToNames;
    private CertificatePolicySet policy;
    private Set<String> policySet;
    private Date privateKeyValid;
    private BigInteger serialNumber;
    private X500Principal subject;
    private Set<GeneralNameInterface> subjectAlternativeGeneralNames;
    private Set<List<?>> subjectAlternativeNames;
    private byte[] subjectKeyID;
    private PublicKey subjectPublicKey;
    private ObjectIdentifier subjectPublicKeyAlgID;
    private byte[] subjectPublicKeyBytes;
    private X509Certificate x509Cert;

    static {
        CertPathHelperImpl.initialize();
        String[] strArr = new String[5];
        EXTENSION_OIDS = strArr;
        strArr[0] = "2.5.29.16";
        strArr[1] = "2.5.29.17";
        strArr[2] = "2.5.29.30";
        strArr[3] = "2.5.29.32";
        strArr[4] = "2.5.29.37";
    }

    public void setCertificate(X509Certificate x509Certificate) {
        this.x509Cert = x509Certificate;
    }

    public void setSerialNumber(BigInteger bigInteger) {
        this.serialNumber = bigInteger;
    }

    public void setIssuer(X500Principal x500Principal) {
        this.issuer = x500Principal;
    }

    public void setIssuer(String str) throws IOException {
        if (str == null) {
            this.issuer = null;
        } else {
            this.issuer = new X500Name(str).asX500Principal();
        }
    }

    public void setIssuer(byte[] bArr) throws IOException {
        X500Principal x500Principal;
        if (bArr == null) {
            x500Principal = null;
        } else {
            try {
                x500Principal = new X500Principal(bArr);
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid name", e);
            }
        }
        this.issuer = x500Principal;
    }

    public void setSubject(X500Principal x500Principal) {
        this.subject = x500Principal;
    }

    public void setSubject(String str) throws IOException {
        if (str == null) {
            this.subject = null;
        } else {
            this.subject = new X500Name(str).asX500Principal();
        }
    }

    public void setSubject(byte[] bArr) throws IOException {
        X500Principal x500Principal;
        if (bArr == null) {
            x500Principal = null;
        } else {
            try {
                x500Principal = new X500Principal(bArr);
            } catch (IllegalArgumentException e) {
                throw new IOException("Invalid name", e);
            }
        }
        this.subject = x500Principal;
    }

    public void setSubjectKeyIdentifier(byte[] bArr) {
        if (bArr == null) {
            this.subjectKeyID = null;
        } else {
            this.subjectKeyID = (byte[]) bArr.clone();
        }
    }

    public void setAuthorityKeyIdentifier(byte[] bArr) {
        if (bArr == null) {
            this.authorityKeyID = null;
        } else {
            this.authorityKeyID = (byte[]) bArr.clone();
        }
    }

    public void setCertificateValid(Date date) {
        if (date == null) {
            this.certificateValid = null;
        } else {
            this.certificateValid = (Date) date.clone();
        }
    }

    public void setPrivateKeyValid(Date date) {
        if (date == null) {
            this.privateKeyValid = null;
        } else {
            this.privateKeyValid = (Date) date.clone();
        }
    }

    public void setSubjectPublicKeyAlgID(String str) throws IOException {
        if (str == null) {
            this.subjectPublicKeyAlgID = null;
        } else {
            this.subjectPublicKeyAlgID = new ObjectIdentifier(str);
        }
    }

    public void setSubjectPublicKey(PublicKey publicKey) {
        if (publicKey == null) {
            this.subjectPublicKey = null;
            this.subjectPublicKeyBytes = null;
            return;
        }
        this.subjectPublicKey = publicKey;
        this.subjectPublicKeyBytes = publicKey.getEncoded();
    }

    public void setSubjectPublicKey(byte[] bArr) throws IOException {
        if (bArr == null) {
            this.subjectPublicKey = null;
            this.subjectPublicKeyBytes = null;
            return;
        }
        byte[] bArr2 = (byte[]) bArr.clone();
        this.subjectPublicKeyBytes = bArr2;
        this.subjectPublicKey = X509Key.parse(new DerValue(bArr2));
    }

    public void setKeyUsage(boolean[] zArr) {
        if (zArr == null) {
            this.keyUsage = null;
        } else {
            this.keyUsage = (boolean[]) zArr.clone();
        }
    }

    public void setExtendedKeyUsage(Set<String> set) throws IOException {
        if (set == null || set.isEmpty()) {
            this.keyPurposeSet = null;
            this.keyPurposeOIDSet = null;
            return;
        }
        this.keyPurposeSet = Collections.unmodifiableSet(new HashSet(set));
        this.keyPurposeOIDSet = new HashSet();
        for (String objectIdentifier : this.keyPurposeSet) {
            this.keyPurposeOIDSet.add(new ObjectIdentifier(objectIdentifier));
        }
    }

    public void setMatchAllSubjectAltNames(boolean z) {
        this.matchAllSubjectAltNames = z;
    }

    public void setSubjectAlternativeNames(Collection<List<?>> collection) throws IOException {
        if (collection == null) {
            this.subjectAlternativeNames = null;
            this.subjectAlternativeGeneralNames = null;
        } else if (collection.isEmpty()) {
            this.subjectAlternativeNames = null;
            this.subjectAlternativeGeneralNames = null;
        } else {
            Set<List<?>> cloneAndCheckNames = cloneAndCheckNames(collection);
            this.subjectAlternativeGeneralNames = parseNames(cloneAndCheckNames);
            this.subjectAlternativeNames = cloneAndCheckNames;
        }
    }

    public void addSubjectAlternativeName(int i, String str) throws IOException {
        addSubjectAlternativeNameInternal(i, str);
    }

    public void addSubjectAlternativeName(int i, byte[] bArr) throws IOException {
        addSubjectAlternativeNameInternal(i, bArr.clone());
    }

    private void addSubjectAlternativeNameInternal(int i, Object obj) throws IOException {
        GeneralNameInterface makeGeneralNameInterface = makeGeneralNameInterface(i, obj);
        if (this.subjectAlternativeNames == null) {
            this.subjectAlternativeNames = new HashSet();
        }
        if (this.subjectAlternativeGeneralNames == null) {
            this.subjectAlternativeGeneralNames = new HashSet();
        }
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(i));
        arrayList.add(obj);
        this.subjectAlternativeNames.add(arrayList);
        this.subjectAlternativeGeneralNames.add(makeGeneralNameInterface);
    }

    private static Set<GeneralNameInterface> parseNames(Collection<List<?>> collection) throws IOException {
        HashSet hashSet = new HashSet();
        for (List next : collection) {
            if (next.size() == 2) {
                Object obj = next.get(0);
                if (obj instanceof Integer) {
                    hashSet.add(makeGeneralNameInterface(((Integer) obj).intValue(), next.get(1)));
                } else {
                    throw new IOException("expected an Integer");
                }
            } else {
                throw new IOException("name list size not 2");
            }
        }
        return hashSet;
    }

    static boolean equalNames(Collection<?> collection, Collection<?> collection2) {
        if (collection == null || collection2 == null) {
            return collection == collection2;
        }
        return collection.equals(collection2);
    }

    static GeneralNameInterface makeGeneralNameInterface(int i, Object obj) throws IOException {
        GeneralNameInterface generalNameInterface;
        GeneralNameInterface otherName;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("X509CertSelector.makeGeneralNameInterface(" + i + ")...");
        }
        if (obj instanceof String) {
            if (debug2 != null) {
                debug2.println("X509CertSelector.makeGeneralNameInterface() name is String: " + obj);
            }
            if (i == 1) {
                generalNameInterface = new RFC822Name((String) obj);
            } else if (i == 2) {
                generalNameInterface = new DNSName((String) obj);
            } else if (i == 4) {
                generalNameInterface = new X500Name((String) obj);
            } else if (i == 6) {
                generalNameInterface = new URIName((String) obj);
            } else if (i == 7) {
                generalNameInterface = new IPAddressName((String) obj);
            } else if (i == 8) {
                generalNameInterface = new OIDName((String) obj);
            } else {
                throw new IOException("unable to parse String names of type " + i);
            }
            if (debug2 != null) {
                debug2.println("X509CertSelector.makeGeneralNameInterface() result: " + generalNameInterface.toString());
            }
        } else if (obj instanceof byte[]) {
            DerValue derValue = new DerValue((byte[]) obj);
            if (debug2 != null) {
                debug2.println("X509CertSelector.makeGeneralNameInterface() is byte[]");
            }
            switch (i) {
                case 0:
                    otherName = new OtherName(derValue);
                    break;
                case 1:
                    otherName = new RFC822Name(derValue);
                    break;
                case 2:
                    otherName = new DNSName(derValue);
                    break;
                case 3:
                    otherName = new X400Address(derValue);
                    break;
                case 4:
                    otherName = new X500Name(derValue);
                    break;
                case 5:
                    otherName = new EDIPartyName(derValue);
                    break;
                case 6:
                    otherName = new URIName(derValue);
                    break;
                case 7:
                    otherName = new IPAddressName(derValue);
                    break;
                case 8:
                    otherName = new OIDName(derValue);
                    break;
                default:
                    throw new IOException("unable to parse byte array names of type " + i);
            }
            if (debug2 != null) {
                debug2.println("X509CertSelector.makeGeneralNameInterface() result: " + generalNameInterface.toString());
            }
        } else {
            if (debug2 != null) {
                debug2.println("X509CertSelector.makeGeneralName() input name not String or byte array");
            }
            throw new IOException("name not String or byte array");
        }
        return generalNameInterface;
    }

    public void setNameConstraints(byte[] bArr) throws IOException {
        if (bArr == null) {
            this.ncBytes = null;
            this.f200nc = null;
            return;
        }
        this.ncBytes = (byte[]) bArr.clone();
        this.f200nc = new NameConstraintsExtension(FALSE, (Object) bArr);
    }

    public void setBasicConstraints(int i) {
        if (i >= -2) {
            this.basicConstraints = i;
            return;
        }
        throw new IllegalArgumentException("basic constraints less than -2");
    }

    public void setPolicy(Set<String> set) throws IOException {
        if (set == null) {
            this.policySet = null;
            this.policy = null;
            return;
        }
        Set<String> unmodifiableSet = Collections.unmodifiableSet(new HashSet(set));
        Vector vector = new Vector();
        for (String next : unmodifiableSet) {
            if (next instanceof String) {
                vector.add(new CertificatePolicyId(new ObjectIdentifier(next)));
            } else {
                throw new IOException("non String in certPolicySet");
            }
        }
        this.policySet = unmodifiableSet;
        this.policy = new CertificatePolicySet((Vector<CertificatePolicyId>) vector);
    }

    public void setPathToNames(Collection<List<?>> collection) throws IOException {
        if (collection == null || collection.isEmpty()) {
            this.pathToNames = null;
            this.pathToGeneralNames = null;
            return;
        }
        Set<List<?>> cloneAndCheckNames = cloneAndCheckNames(collection);
        this.pathToGeneralNames = parseNames(cloneAndCheckNames);
        this.pathToNames = cloneAndCheckNames;
    }

    /* access modifiers changed from: package-private */
    public void setPathToNamesInternal(Set<GeneralNameInterface> set) {
        this.pathToNames = Collections.emptySet();
        this.pathToGeneralNames = set;
    }

    public void addPathToName(int i, String str) throws IOException {
        addPathToNameInternal(i, str);
    }

    public void addPathToName(int i, byte[] bArr) throws IOException {
        addPathToNameInternal(i, bArr.clone());
    }

    private void addPathToNameInternal(int i, Object obj) throws IOException {
        GeneralNameInterface makeGeneralNameInterface = makeGeneralNameInterface(i, obj);
        if (this.pathToGeneralNames == null) {
            this.pathToNames = new HashSet();
            this.pathToGeneralNames = new HashSet();
        }
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(Integer.valueOf(i));
        arrayList.add(obj);
        this.pathToNames.add(arrayList);
        this.pathToGeneralNames.add(makeGeneralNameInterface);
    }

    public X509Certificate getCertificate() {
        return this.x509Cert;
    }

    public BigInteger getSerialNumber() {
        return this.serialNumber;
    }

    public X500Principal getIssuer() {
        return this.issuer;
    }

    public String getIssuerAsString() {
        X500Principal x500Principal = this.issuer;
        if (x500Principal == null) {
            return null;
        }
        return x500Principal.getName();
    }

    public byte[] getIssuerAsBytes() throws IOException {
        X500Principal x500Principal = this.issuer;
        if (x500Principal == null) {
            return null;
        }
        return x500Principal.getEncoded();
    }

    public X500Principal getSubject() {
        return this.subject;
    }

    public String getSubjectAsString() {
        X500Principal x500Principal = this.subject;
        if (x500Principal == null) {
            return null;
        }
        return x500Principal.getName();
    }

    public byte[] getSubjectAsBytes() throws IOException {
        X500Principal x500Principal = this.subject;
        if (x500Principal == null) {
            return null;
        }
        return x500Principal.getEncoded();
    }

    public byte[] getSubjectKeyIdentifier() {
        byte[] bArr = this.subjectKeyID;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public byte[] getAuthorityKeyIdentifier() {
        byte[] bArr = this.authorityKeyID;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public Date getCertificateValid() {
        Date date = this.certificateValid;
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    public Date getPrivateKeyValid() {
        Date date = this.privateKeyValid;
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    public String getSubjectPublicKeyAlgID() {
        ObjectIdentifier objectIdentifier = this.subjectPublicKeyAlgID;
        if (objectIdentifier == null) {
            return null;
        }
        return objectIdentifier.toString();
    }

    public PublicKey getSubjectPublicKey() {
        return this.subjectPublicKey;
    }

    public boolean[] getKeyUsage() {
        boolean[] zArr = this.keyUsage;
        if (zArr == null) {
            return null;
        }
        return (boolean[]) zArr.clone();
    }

    public Set<String> getExtendedKeyUsage() {
        return this.keyPurposeSet;
    }

    public boolean getMatchAllSubjectAltNames() {
        return this.matchAllSubjectAltNames;
    }

    public Collection<List<?>> getSubjectAlternativeNames() {
        Set<List<?>> set = this.subjectAlternativeNames;
        if (set == null) {
            return null;
        }
        return cloneNames(set);
    }

    private static Set<List<?>> cloneNames(Collection<List<?>> collection) {
        try {
            return cloneAndCheckNames(collection);
        } catch (IOException e) {
            throw new RuntimeException("cloneNames encountered IOException: " + e.getMessage());
        }
    }

    private static Set<List<?>> cloneAndCheckNames(Collection<List<?>> collection) throws IOException {
        HashSet<List> hashSet = new HashSet<>();
        for (List<?> arrayList : collection) {
            hashSet.add(new ArrayList(arrayList));
        }
        for (List list : hashSet) {
            if (list.size() == 2) {
                Object obj = list.get(0);
                if (obj instanceof Integer) {
                    int intValue = ((Integer) obj).intValue();
                    if (intValue < 0 || intValue > 8) {
                        throw new IOException("name type not 0-8");
                    }
                    Object obj2 = list.get(1);
                    boolean z = obj2 instanceof byte[];
                    if (!z && !(obj2 instanceof String)) {
                        Debug debug2 = debug;
                        if (debug2 != null) {
                            debug2.println("X509CertSelector.cloneAndCheckNames() name not byte array");
                        }
                        throw new IOException("name not byte array or String");
                    } else if (z) {
                        list.set(1, ((byte[]) obj2).clone());
                    }
                } else {
                    throw new IOException("expected an Integer");
                }
            } else {
                throw new IOException("name list size not 2");
            }
        }
        return hashSet;
    }

    public byte[] getNameConstraints() {
        byte[] bArr = this.ncBytes;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public int getBasicConstraints() {
        return this.basicConstraints;
    }

    public Set<String> getPolicy() {
        return this.policySet;
    }

    public Collection<List<?>> getPathToNames() {
        Set<List<?>> set = this.pathToNames;
        if (set == null) {
            return null;
        }
        return cloneNames(set);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("X509CertSelector: [\n");
        if (this.x509Cert != null) {
            sb.append("  Certificate: " + this.x509Cert.toString() + "\n");
        }
        if (this.serialNumber != null) {
            sb.append("  Serial Number: " + this.serialNumber.toString() + "\n");
        }
        if (this.issuer != null) {
            sb.append("  Issuer: " + getIssuerAsString() + "\n");
        }
        if (this.subject != null) {
            sb.append("  Subject: " + getSubjectAsString() + "\n");
        }
        sb.append("  matchAllSubjectAltNames flag: " + String.valueOf(this.matchAllSubjectAltNames) + "\n");
        if (this.subjectAlternativeNames != null) {
            sb.append("  SubjectAlternativeNames:\n");
            for (List next : this.subjectAlternativeNames) {
                sb.append("    type " + next.get(0) + ", name " + next.get(1) + "\n");
            }
        }
        if (this.subjectKeyID != null) {
            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
            sb.append("  Subject Key Identifier: " + hexDumpEncoder.encodeBuffer(this.subjectKeyID) + "\n");
        }
        if (this.authorityKeyID != null) {
            HexDumpEncoder hexDumpEncoder2 = new HexDumpEncoder();
            sb.append("  Authority Key Identifier: " + hexDumpEncoder2.encodeBuffer(this.authorityKeyID) + "\n");
        }
        if (this.certificateValid != null) {
            sb.append("  Certificate Valid: " + this.certificateValid.toString() + "\n");
        }
        if (this.privateKeyValid != null) {
            sb.append("  Private Key Valid: " + this.privateKeyValid.toString() + "\n");
        }
        if (this.subjectPublicKeyAlgID != null) {
            sb.append("  Subject Public Key AlgID: " + this.subjectPublicKeyAlgID.toString() + "\n");
        }
        if (this.subjectPublicKey != null) {
            sb.append("  Subject Public Key: " + this.subjectPublicKey.toString() + "\n");
        }
        if (this.keyUsage != null) {
            sb.append("  Key Usage: " + keyUsageToString(this.keyUsage) + "\n");
        }
        if (this.keyPurposeSet != null) {
            sb.append("  Extended Key Usage: " + this.keyPurposeSet.toString() + "\n");
        }
        if (this.policy != null) {
            sb.append("  Policy: " + this.policy.toString() + "\n");
        }
        if (this.pathToGeneralNames != null) {
            sb.append("  Path to names:\n");
            for (GeneralNameInterface generalNameInterface : this.pathToGeneralNames) {
                sb.append("    " + generalNameInterface + "\n");
            }
        }
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    private static String keyUsageToString(boolean[] zArr) {
        String str = "KeyUsage [\n";
        try {
            if (zArr[0]) {
                str = "KeyUsage [\n  DigitalSignature\n";
            }
            if (zArr[1]) {
                str = str + "  Non_repudiation\n";
            }
            if (zArr[2]) {
                str = str + "  Key_Encipherment\n";
            }
            if (zArr[3]) {
                str = str + "  Data_Encipherment\n";
            }
            if (zArr[4]) {
                str = str + "  Key_Agreement\n";
            }
            if (zArr[5]) {
                str = str + "  Key_CertSign\n";
            }
            if (zArr[6]) {
                str = str + "  Crl_Sign\n";
            }
            if (zArr[7]) {
                str = str + "  Encipher_Only\n";
            }
            if (zArr[8]) {
                str = str + "  Decipher_Only\n";
            }
        } catch (ArrayIndexOutOfBoundsException unused) {
        }
        return str + "]\n";
    }

    private static Extension getExtensionObject(X509Certificate x509Certificate, int i) throws IOException {
        if (x509Certificate instanceof X509CertImpl) {
            X509CertImpl x509CertImpl = (X509CertImpl) x509Certificate;
            if (i == 0) {
                return x509CertImpl.getPrivateKeyUsageExtension();
            }
            if (i == 1) {
                return x509CertImpl.getSubjectAlternativeNameExtension();
            }
            if (i == 2) {
                return x509CertImpl.getNameConstraintsExtension();
            }
            if (i == 3) {
                return x509CertImpl.getCertificatePoliciesExtension();
            }
            if (i != 4) {
                return null;
            }
            return x509CertImpl.getExtendedKeyUsageExtension();
        }
        byte[] extensionValue = x509Certificate.getExtensionValue(EXTENSION_OIDS[i]);
        if (extensionValue == null) {
            return null;
        }
        byte[] octetString = new DerInputStream(extensionValue).getOctetString();
        if (i == 0) {
            try {
                return new PrivateKeyUsageExtension(FALSE, (Object) octetString);
            } catch (CertificateException e) {
                throw new IOException(e.getMessage());
            }
        } else if (i == 1) {
            return new SubjectAlternativeNameExtension(FALSE, (Object) octetString);
        } else {
            if (i == 2) {
                return new NameConstraintsExtension(FALSE, (Object) octetString);
            }
            if (i == 3) {
                return new CertificatePoliciesExtension(FALSE, (Object) octetString);
            }
            if (i != 4) {
                return null;
            }
            return new ExtendedKeyUsageExtension(FALSE, (Object) octetString);
        }
    }

    public boolean match(Certificate certificate) {
        boolean z = false;
        if (!(certificate instanceof X509Certificate)) {
            return false;
        }
        X509Certificate x509Certificate = (X509Certificate) certificate;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("X509CertSelector.match(SN: " + x509Certificate.getSerialNumber().toString(16) + "\n  Issuer: " + x509Certificate.getIssuerDN() + "\n  Subject: " + x509Certificate.getSubjectDN() + NavigationBarInflaterView.KEY_CODE_END);
        }
        X509Certificate x509Certificate2 = this.x509Cert;
        if (x509Certificate2 == null || x509Certificate2.equals(x509Certificate)) {
            BigInteger bigInteger = this.serialNumber;
            if (bigInteger == null || bigInteger.equals(x509Certificate.getSerialNumber())) {
                X500Principal x500Principal = this.issuer;
                if (x500Principal == null || x500Principal.equals(x509Certificate.getIssuerX500Principal())) {
                    X500Principal x500Principal2 = this.subject;
                    if (x500Principal2 == null || x500Principal2.equals(x509Certificate.getSubjectX500Principal())) {
                        Date date = this.certificateValid;
                        if (date != null) {
                            try {
                                x509Certificate.checkValidity(date);
                            } catch (CertificateException unused) {
                                Debug debug3 = debug;
                                if (debug3 != null) {
                                    debug3.println("X509CertSelector.match: certificate not within validity period");
                                }
                                return false;
                            }
                        }
                        if (this.subjectPublicKeyBytes != null) {
                            if (!Arrays.equals(this.subjectPublicKeyBytes, x509Certificate.getPublicKey().getEncoded())) {
                                if (debug2 != null) {
                                    debug2.println("X509CertSelector.match: subject public keys don't match");
                                }
                                return false;
                            }
                        }
                        if (matchBasicConstraints(x509Certificate) && matchKeyUsage(x509Certificate) && matchExtendedKeyUsage(x509Certificate) && matchSubjectKeyID(x509Certificate) && matchAuthorityKeyID(x509Certificate) && matchPrivateKeyValid(x509Certificate) && matchSubjectPublicKeyAlgID(x509Certificate) && matchPolicy(x509Certificate) && matchSubjectAlternativeNames(x509Certificate) && matchPathToNames(x509Certificate) && matchNameConstraints(x509Certificate)) {
                            z = true;
                        }
                        if (z && debug2 != null) {
                            debug2.println("X509CertSelector.match returning: true");
                        }
                        return z;
                    }
                    if (debug2 != null) {
                        debug2.println("X509CertSelector.match: subject DNs don't match");
                    }
                    return false;
                }
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: issuer DNs don't match");
                }
                return false;
            }
            if (debug2 != null) {
                debug2.println("X509CertSelector.match: serial numbers don't match");
            }
            return false;
        }
        if (debug2 != null) {
            debug2.println("X509CertSelector.match: certs don't match");
        }
        return false;
    }

    private boolean matchSubjectKeyID(X509Certificate x509Certificate) {
        if (this.subjectKeyID == null) {
            return true;
        }
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue("2.5.29.14");
            if (extensionValue == null) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: no subject key ID extension");
                }
                return false;
            }
            byte[] octetString = new DerInputStream(extensionValue).getOctetString();
            if (octetString != null) {
                if (Arrays.equals(this.subjectKeyID, octetString)) {
                    return true;
                }
            }
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("X509CertSelector.match: subject key IDs don't match");
            }
            return false;
        } catch (IOException unused) {
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("X509CertSelector.match: exception in subject key ID check");
            }
            return false;
        }
    }

    private boolean matchAuthorityKeyID(X509Certificate x509Certificate) {
        if (this.authorityKeyID == null) {
            return true;
        }
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue("2.5.29.35");
            if (extensionValue == null) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: no authority key ID extension");
                }
                return false;
            }
            byte[] octetString = new DerInputStream(extensionValue).getOctetString();
            if (octetString != null) {
                if (Arrays.equals(this.authorityKeyID, octetString)) {
                    return true;
                }
            }
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("X509CertSelector.match: authority key IDs don't match");
            }
            return false;
        } catch (IOException unused) {
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("X509CertSelector.match: exception in authority key ID check");
            }
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0021, code lost:
        r7 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0022, code lost:
        r0 = debug;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
        if (r0 != null) goto L_0x0026;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0026, code lost:
        r0.println("X509CertSelector.match: IOException in private key usage check; X509CertSelector: " + toString());
        r7.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003e, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003f, code lost:
        r3 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        r1 = r4.get(sun.security.x509.PrivateKeyUsageExtension.NOT_BEFORE).toString();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004e, code lost:
        r7 = debug;
        r7.println("X509CertSelector.match: private key usage not within validity date; ext.NOT_BEFORE: " + r1 + "; X509CertSelector: " + toString());
        r3.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006f, code lost:
        r3 = e;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0021 A[ExcHandler: IOException (r7v9 'e' java.io.IOException A[CUSTOM_DECLARE]), Splitter:B:4:0x000c] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0044  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean matchPrivateKeyValid(java.security.cert.X509Certificate r7) {
        /*
            r6 = this;
            java.lang.String r0 = "; X509CertSelector: "
            java.lang.String r1 = "n/a"
            java.util.Date r2 = r6.privateKeyValid
            r3 = 1
            if (r2 != 0) goto L_0x000a
            return r3
        L_0x000a:
            r2 = 0
            r4 = 0
            java.security.cert.Extension r7 = getExtensionObject(r7, r2)     // Catch:{ CertificateExpiredException -> 0x006f, CertificateNotYetValidException -> 0x003f, IOException -> 0x0021 }
            sun.security.x509.PrivateKeyUsageExtension r7 = (sun.security.x509.PrivateKeyUsageExtension) r7     // Catch:{ CertificateExpiredException -> 0x006f, CertificateNotYetValidException -> 0x003f, IOException -> 0x0021 }
            if (r7 == 0) goto L_0x0020
            java.util.Date r4 = r6.privateKeyValid     // Catch:{ CertificateExpiredException -> 0x001d, CertificateNotYetValidException -> 0x001a, IOException -> 0x0021 }
            r7.valid(r4)     // Catch:{ CertificateExpiredException -> 0x001d, CertificateNotYetValidException -> 0x001a, IOException -> 0x0021 }
            goto L_0x0020
        L_0x001a:
            r3 = move-exception
            r4 = r7
            goto L_0x0040
        L_0x001d:
            r3 = move-exception
            r4 = r7
            goto L_0x0070
        L_0x0020:
            return r3
        L_0x0021:
            r7 = move-exception
            sun.security.util.Debug r0 = debug
            if (r0 == 0) goto L_0x003e
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r3 = "X509CertSelector.match: IOException in private key usage check; X509CertSelector: "
            r1.<init>((java.lang.String) r3)
            java.lang.String r6 = r6.toString()
            r1.append((java.lang.String) r6)
            java.lang.String r6 = r1.toString()
            r0.println(r6)
            r7.printStackTrace()
        L_0x003e:
            return r2
        L_0x003f:
            r3 = move-exception
        L_0x0040:
            sun.security.util.Debug r7 = debug
            if (r7 == 0) goto L_0x006e
            java.lang.String r7 = "not_before"
            java.util.Date r7 = r4.get((java.lang.String) r7)     // Catch:{ CertificateException -> 0x004e }
            java.lang.String r1 = r7.toString()     // Catch:{ CertificateException -> 0x004e }
        L_0x004e:
            sun.security.util.Debug r7 = debug
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "X509CertSelector.match: private key usage not within validity date; ext.NOT_BEFORE: "
            r4.<init>((java.lang.String) r5)
            r4.append((java.lang.String) r1)
            r4.append((java.lang.String) r0)
            java.lang.String r6 = r6.toString()
            r4.append((java.lang.String) r6)
            java.lang.String r6 = r4.toString()
            r7.println(r6)
            r3.printStackTrace()
        L_0x006e:
            return r2
        L_0x006f:
            r3 = move-exception
        L_0x0070:
            sun.security.util.Debug r7 = debug
            if (r7 == 0) goto L_0x009e
            java.lang.String r7 = "not_after"
            java.util.Date r7 = r4.get((java.lang.String) r7)     // Catch:{ CertificateException -> 0x007e }
            java.lang.String r1 = r7.toString()     // Catch:{ CertificateException -> 0x007e }
        L_0x007e:
            sun.security.util.Debug r7 = debug
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r5 = "X509CertSelector.match: private key usage not within validity date; ext.NOT_After: "
            r4.<init>((java.lang.String) r5)
            r4.append((java.lang.String) r1)
            r4.append((java.lang.String) r0)
            java.lang.String r6 = r6.toString()
            r4.append((java.lang.String) r6)
            java.lang.String r6 = r4.toString()
            r7.println(r6)
            r3.printStackTrace()
        L_0x009e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.security.cert.X509CertSelector.matchPrivateKeyValid(java.security.cert.X509Certificate):boolean");
    }

    private boolean matchSubjectPublicKeyAlgID(X509Certificate x509Certificate) {
        if (this.subjectPublicKeyAlgID == null) {
            return true;
        }
        try {
            DerValue derValue = new DerValue(x509Certificate.getPublicKey().getEncoded());
            if (derValue.tag == 48) {
                AlgorithmId parse = AlgorithmId.parse(derValue.data.getDerValue());
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: subjectPublicKeyAlgID = " + this.subjectPublicKeyAlgID + ", xcert subjectPublicKeyAlgID = " + parse.getOID());
                }
                if (this.subjectPublicKeyAlgID.equals(parse.getOID())) {
                    return true;
                }
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: subject public key alg IDs don't match");
                }
                return false;
            }
            throw new IOException("invalid key format");
        } catch (IOException unused) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("X509CertSelector.match: IOException in subject public key algorithm OID check");
            }
            return false;
        }
    }

    private boolean matchKeyUsage(X509Certificate x509Certificate) {
        boolean[] keyUsage2;
        if (this.keyUsage != null && (keyUsage2 = x509Certificate.getKeyUsage()) != null) {
            int i = 0;
            while (true) {
                boolean[] zArr = this.keyUsage;
                if (i >= zArr.length) {
                    break;
                } else if (!zArr[i] || (i < keyUsage2.length && keyUsage2[i])) {
                    i++;
                }
            }
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("X509CertSelector.match: key usage bits don't match");
            }
            return false;
        }
        return true;
    }

    private boolean matchExtendedKeyUsage(X509Certificate x509Certificate) {
        Set<String> set = this.keyPurposeSet;
        if (set == null || set.isEmpty()) {
            return true;
        }
        try {
            ExtendedKeyUsageExtension extendedKeyUsageExtension = (ExtendedKeyUsageExtension) getExtensionObject(x509Certificate, 4);
            if (extendedKeyUsageExtension != null) {
                Vector vector = extendedKeyUsageExtension.get(ExtendedKeyUsageExtension.USAGES);
                if (!vector.contains(ANY_EXTENDED_KEY_USAGE) && !vector.containsAll(this.keyPurposeOIDSet)) {
                    Debug debug2 = debug;
                    if (debug2 != null) {
                        debug2.println("X509CertSelector.match: cert failed extendedKeyUsage criterion");
                    }
                    return false;
                }
            }
            return true;
        } catch (IOException unused) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("X509CertSelector.match: IOException in extended key usage check");
            }
            return false;
        }
    }

    private boolean matchSubjectAlternativeNames(X509Certificate x509Certificate) {
        Set<List<?>> set = this.subjectAlternativeNames;
        if (set == null || set.isEmpty()) {
            return true;
        }
        try {
            SubjectAlternativeNameExtension subjectAlternativeNameExtension = (SubjectAlternativeNameExtension) getExtensionObject(x509Certificate, 1);
            if (subjectAlternativeNameExtension == null) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: no subject alternative name extension");
                }
                return false;
            }
            GeneralNames generalNames = subjectAlternativeNameExtension.get(SubjectAlternativeNameExtension.SUBJECT_NAME);
            Iterator<GeneralNameInterface> it = this.subjectAlternativeGeneralNames.iterator();
            while (it.hasNext()) {
                GeneralNameInterface next = it.next();
                Iterator<GeneralName> it2 = generalNames.iterator();
                boolean z = false;
                while (it2.hasNext() && !z) {
                    z = it2.next().getName().equals(next);
                }
                if (z || (!this.matchAllSubjectAltNames && it.hasNext())) {
                    if (z && !this.matchAllSubjectAltNames) {
                        break;
                    }
                } else {
                    Debug debug3 = debug;
                    if (debug3 != null) {
                        debug3.println("X509CertSelector.match: subject alternative name " + next + " not found");
                    }
                    return false;
                }
            }
            return true;
        } catch (IOException unused) {
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("X509CertSelector.match: IOException in subject alternative name check");
            }
            return false;
        }
    }

    private boolean matchNameConstraints(X509Certificate x509Certificate) {
        NameConstraintsExtension nameConstraintsExtension = this.f200nc;
        if (nameConstraintsExtension == null) {
            return true;
        }
        try {
            if (nameConstraintsExtension.verify(x509Certificate)) {
                return true;
            }
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("X509CertSelector.match: name constraints not satisfied");
            }
            return false;
        } catch (IOException unused) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("X509CertSelector.match: IOException in name constraints check");
            }
            return false;
        }
    }

    private boolean matchPolicy(X509Certificate x509Certificate) {
        boolean z;
        if (this.policy == null) {
            return true;
        }
        try {
            CertificatePoliciesExtension certificatePoliciesExtension = (CertificatePoliciesExtension) getExtensionObject(x509Certificate, 3);
            if (certificatePoliciesExtension == null) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: no certificate policy extension");
                }
                return false;
            }
            List<PolicyInformation> list = certificatePoliciesExtension.get(CertificatePoliciesExtension.POLICIES);
            ArrayList arrayList = new ArrayList(list.size());
            for (PolicyInformation policyIdentifier : list) {
                arrayList.add(policyIdentifier.getPolicyIdentifier());
            }
            CertificatePolicySet certificatePolicySet = this.policy;
            if (certificatePolicySet != null) {
                if (!certificatePolicySet.getCertPolicyIds().isEmpty()) {
                    Iterator<CertificatePolicyId> it = this.policy.getCertPolicyIds().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (arrayList.contains(it.next())) {
                                z = true;
                                break;
                            }
                        } else {
                            z = false;
                            break;
                        }
                    }
                    if (!z) {
                        Debug debug3 = debug;
                        if (debug3 != null) {
                            debug3.println("X509CertSelector.match: cert failed policyAny criterion");
                        }
                        return false;
                    }
                } else if (arrayList.isEmpty()) {
                    Debug debug4 = debug;
                    if (debug4 != null) {
                        debug4.println("X509CertSelector.match: cert failed policyAny criterion");
                    }
                    return false;
                }
            }
            return true;
        } catch (IOException unused) {
            Debug debug5 = debug;
            if (debug5 != null) {
                debug5.println("X509CertSelector.match: IOException in certificate policy ID check");
            }
            return false;
        }
    }

    private boolean matchPathToNames(X509Certificate x509Certificate) {
        if (this.pathToGeneralNames == null) {
            return true;
        }
        try {
            NameConstraintsExtension nameConstraintsExtension = (NameConstraintsExtension) getExtensionObject(x509Certificate, 2);
            if (nameConstraintsExtension == null) {
                return true;
            }
            Debug debug2 = debug;
            if (debug2 != null && Debug.isOn("certpath")) {
                debug2.println("X509CertSelector.match pathToNames:\n");
                for (GeneralNameInterface generalNameInterface : this.pathToGeneralNames) {
                    Debug debug3 = debug;
                    debug3.println("    " + generalNameInterface + "\n");
                }
            }
            GeneralSubtrees generalSubtrees = nameConstraintsExtension.get(NameConstraintsExtension.PERMITTED_SUBTREES);
            GeneralSubtrees generalSubtrees2 = nameConstraintsExtension.get(NameConstraintsExtension.EXCLUDED_SUBTREES);
            if (generalSubtrees2 == null || matchExcluded(generalSubtrees2)) {
                return generalSubtrees == null || matchPermitted(generalSubtrees);
            }
            return false;
        } catch (IOException unused) {
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("X509CertSelector.match: IOException in name constraints check");
            }
            return false;
        }
    }

    private boolean matchExcluded(GeneralSubtrees generalSubtrees) {
        GeneralNameInterface next;
        int constrains;
        Iterator<GeneralSubtree> it = generalSubtrees.iterator();
        loop0:
        while (it.hasNext()) {
            GeneralNameInterface name = it.next().getName().getName();
            Iterator<GeneralNameInterface> it2 = this.pathToGeneralNames.iterator();
            while (true) {
                if (it2.hasNext()) {
                    next = it2.next();
                    if (!(name.getType() == next.getType() && ((constrains = next.constrains(name)) == 0 || constrains == 2))) {
                    }
                }
            }
            Debug debug2 = debug;
            if (debug2 == null) {
                return false;
            }
            debug2.println("X509CertSelector.match: name constraints inhibit path to specified name");
            debug2.println("X509CertSelector.match: excluded name: " + next);
            return false;
        }
        return true;
    }

    private boolean matchPermitted(GeneralSubtrees generalSubtrees) {
        boolean z;
        for (GeneralNameInterface next : this.pathToGeneralNames) {
            Iterator<GeneralSubtree> it = generalSubtrees.iterator();
            String str = "";
            boolean z2 = false;
            while (true) {
                z = z2;
                while (it.hasNext() && !z2) {
                    GeneralNameInterface name = it.next().getName().getName();
                    if (name.getType() == next.getType()) {
                        str = str + "  " + name;
                        int constrains = next.constrains(name);
                        if (constrains == 0 || constrains == 2) {
                            z2 = true;
                        } else {
                            z = true;
                        }
                    }
                }
            }
            if (!z2 && z) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: name constraints inhibit path to specified name; permitted names of type " + next.getType() + ": " + str);
                }
                return false;
            }
        }
        return true;
    }

    private boolean matchBasicConstraints(X509Certificate x509Certificate) {
        if (this.basicConstraints == -1) {
            return true;
        }
        int basicConstraints2 = x509Certificate.getBasicConstraints();
        int i = this.basicConstraints;
        if (i == -2) {
            if (basicConstraints2 != -1) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("X509CertSelector.match: not an EE cert");
                }
                return false;
            }
        } else if (basicConstraints2 < i) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("X509CertSelector.match: cert's maxPathLen is less than the min maxPathLen set by basicConstraints. (" + basicConstraints2 + " < " + this.basicConstraints + NavigationBarInflaterView.KEY_CODE_END);
            }
            return false;
        }
        return true;
    }

    private static <T> Set<T> cloneSet(Set<T> set) {
        if (set instanceof HashSet) {
            return (Set) ((HashSet) set).clone();
        }
        return new HashSet(set);
    }

    public Object clone() {
        try {
            X509CertSelector x509CertSelector = (X509CertSelector) super.clone();
            Set<List<?>> set = this.subjectAlternativeNames;
            if (set != null) {
                x509CertSelector.subjectAlternativeNames = cloneSet(set);
                x509CertSelector.subjectAlternativeGeneralNames = cloneSet(this.subjectAlternativeGeneralNames);
            }
            if (this.pathToGeneralNames != null) {
                x509CertSelector.pathToNames = cloneSet(this.pathToNames);
                x509CertSelector.pathToGeneralNames = cloneSet(this.pathToGeneralNames);
            }
            return x509CertSelector;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString(), e);
        }
    }
}
