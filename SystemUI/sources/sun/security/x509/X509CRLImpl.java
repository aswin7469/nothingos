package sun.security.x509;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.security.auth.x500.X500Principal;
import sun.misc.HexDumpEncoder;
import sun.security.provider.X509Factory;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class X509CRLImpl extends X509CRL implements DerEncoder {
    private static final long YR_2050 = 2524636800000L;
    private static final boolean isExplicit = true;
    private CRLExtensions extensions;
    private AlgorithmId infoSigAlgId;
    private X500Name issuer;
    private X500Principal issuerPrincipal;
    private Date nextUpdate;
    private boolean readOnly;
    private List<X509CRLEntry> revokedList;
    private Map<X509IssuerSerial, X509CRLEntry> revokedMap;
    private AlgorithmId sigAlgId;
    private byte[] signature;
    private byte[] signedCRL;
    private byte[] tbsCertList;
    private Date thisUpdate;
    private String verifiedProvider;
    private PublicKey verifiedPublicKey;
    private int version;

    private X509CRLImpl() {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
    }

    public X509CRLImpl(byte[] bArr) throws CRLException {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        try {
            parse(new DerValue(bArr));
        } catch (IOException e) {
            this.signedCRL = null;
            throw new CRLException("Parsing error: " + e.getMessage());
        }
    }

    public X509CRLImpl(DerValue derValue) throws CRLException {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        try {
            parse(derValue);
        } catch (IOException e) {
            this.signedCRL = null;
            throw new CRLException("Parsing error: " + e.getMessage());
        }
    }

    public X509CRLImpl(InputStream inputStream) throws CRLException {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        try {
            parse(new DerValue(inputStream));
        } catch (IOException e) {
            this.signedCRL = null;
            throw new CRLException("Parsing error: " + e.getMessage());
        }
    }

    public X509CRLImpl(X500Name x500Name, Date date, Date date2) {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        this.readOnly = false;
        this.issuer = x500Name;
        this.thisUpdate = date;
        this.nextUpdate = date2;
    }

    public X509CRLImpl(X500Name x500Name, Date date, Date date2, X509CRLEntry[] x509CRLEntryArr) throws CRLException {
        this.signedCRL = null;
        this.signature = null;
        this.tbsCertList = null;
        this.sigAlgId = null;
        this.issuer = null;
        this.issuerPrincipal = null;
        this.thisUpdate = null;
        this.nextUpdate = null;
        this.revokedMap = new TreeMap();
        this.revokedList = new LinkedList();
        this.extensions = null;
        int i = 0;
        this.readOnly = false;
        this.issuer = x500Name;
        this.thisUpdate = date;
        this.nextUpdate = date2;
        if (x509CRLEntryArr != null) {
            X500Principal issuerX500Principal = getIssuerX500Principal();
            X500Principal x500Principal = issuerX500Principal;
            while (i < x509CRLEntryArr.length) {
                X509CRLEntryImpl x509CRLEntryImpl = x509CRLEntryArr[i];
                try {
                    x500Principal = getCertIssuer(x509CRLEntryImpl, x500Principal);
                    x509CRLEntryImpl.setCertificateIssuer(issuerX500Principal, x500Principal);
                    this.revokedMap.put(new X509IssuerSerial(x500Principal, x509CRLEntryImpl.getSerialNumber()), x509CRLEntryImpl);
                    this.revokedList.add(x509CRLEntryImpl);
                    if (x509CRLEntryImpl.hasExtensions()) {
                        this.version = 1;
                    }
                    i++;
                } catch (IOException e) {
                    throw new CRLException((Throwable) e);
                }
            }
        }
    }

    public X509CRLImpl(X500Name x500Name, Date date, Date date2, X509CRLEntry[] x509CRLEntryArr, CRLExtensions cRLExtensions) throws CRLException {
        this(x500Name, date, date2, x509CRLEntryArr);
        if (cRLExtensions != null) {
            this.extensions = cRLExtensions;
            this.version = 1;
        }
    }

    public byte[] getEncodedInternal() throws CRLException {
        byte[] bArr = this.signedCRL;
        if (bArr != null) {
            return bArr;
        }
        throw new CRLException("Null CRL to encode");
    }

    public byte[] getEncoded() throws CRLException {
        return (byte[]) getEncodedInternal().clone();
    }

    public void encodeInfo(OutputStream outputStream) throws CRLException {
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            DerOutputStream derOutputStream2 = new DerOutputStream();
            DerOutputStream derOutputStream3 = new DerOutputStream();
            int i = this.version;
            if (i != 0) {
                derOutputStream.putInteger(i);
            }
            this.infoSigAlgId.encode(derOutputStream);
            if (this.version == 0) {
                if (this.issuer.toString() == null) {
                    throw new CRLException("Null Issuer DN not allowed in v1 CRL");
                }
            }
            this.issuer.encode(derOutputStream);
            if (this.thisUpdate.getTime() < YR_2050) {
                derOutputStream.putUTCTime(this.thisUpdate);
            } else {
                derOutputStream.putGeneralizedTime(this.thisUpdate);
            }
            Date date = this.nextUpdate;
            if (date != null) {
                if (date.getTime() < YR_2050) {
                    derOutputStream.putUTCTime(this.nextUpdate);
                } else {
                    derOutputStream.putGeneralizedTime(this.nextUpdate);
                }
            }
            if (!this.revokedList.isEmpty()) {
                Iterator<X509CRLEntry> it = this.revokedList.iterator();
                while (it.hasNext()) {
                    ((X509CRLEntryImpl) it.next()).encode(derOutputStream2);
                }
                derOutputStream.write((byte) 48, derOutputStream2);
            }
            CRLExtensions cRLExtensions = this.extensions;
            if (cRLExtensions != null) {
                cRLExtensions.encode(derOutputStream, true);
            }
            derOutputStream3.write((byte) 48, derOutputStream);
            byte[] byteArray = derOutputStream3.toByteArray();
            this.tbsCertList = byteArray;
            outputStream.write(byteArray);
        } catch (IOException e) {
            throw new CRLException("Encoding error: " + e.getMessage());
        }
    }

    public void verify(PublicKey publicKey) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        verify(publicKey, "");
    }

    public synchronized void verify(PublicKey publicKey, String str) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        Signature signature2;
        if (str == null) {
            str = "";
        }
        PublicKey publicKey2 = this.verifiedPublicKey;
        if (publicKey2 != null && publicKey2.equals(publicKey) && str.equals(this.verifiedProvider)) {
            return;
        }
        if (this.signedCRL != null) {
            if (str.length() == 0) {
                signature2 = Signature.getInstance(this.sigAlgId.getName());
            } else {
                signature2 = Signature.getInstance(this.sigAlgId.getName(), str);
            }
            signature2.initVerify(publicKey);
            byte[] bArr = this.tbsCertList;
            if (bArr != null) {
                signature2.update(bArr, 0, bArr.length);
                if (signature2.verify(this.signature)) {
                    this.verifiedPublicKey = publicKey;
                    this.verifiedProvider = str;
                    return;
                }
                throw new SignatureException("Signature does not match.");
            }
            throw new CRLException("Uninitialized CRL");
        }
        throw new CRLException("Uninitialized CRL");
    }

    public synchronized void verify(PublicKey publicKey, Provider provider) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature2;
        if (this.signedCRL != null) {
            if (provider == null) {
                signature2 = Signature.getInstance(this.sigAlgId.getName());
            } else {
                signature2 = Signature.getInstance(this.sigAlgId.getName(), provider);
            }
            signature2.initVerify(publicKey);
            byte[] bArr = this.tbsCertList;
            if (bArr != null) {
                signature2.update(bArr, 0, bArr.length);
                if (signature2.verify(this.signature)) {
                    this.verifiedPublicKey = publicKey;
                } else {
                    throw new SignatureException("Signature does not match.");
                }
            } else {
                throw new CRLException("Uninitialized CRL");
            }
        } else {
            throw new CRLException("Uninitialized CRL");
        }
    }

    public void sign(PrivateKey privateKey, String str) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        sign(privateKey, str, (String) null);
    }

    public void sign(PrivateKey privateKey, String str, String str2) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        Signature signature2;
        try {
            if (!this.readOnly) {
                if (str2 != null) {
                    if (str2.length() != 0) {
                        signature2 = Signature.getInstance(str, str2);
                        signature2.initSign(privateKey);
                        AlgorithmId algorithmId = AlgorithmId.get(signature2.getAlgorithm());
                        this.sigAlgId = algorithmId;
                        this.infoSigAlgId = algorithmId;
                        DerOutputStream derOutputStream = new DerOutputStream();
                        DerOutputStream derOutputStream2 = new DerOutputStream();
                        encodeInfo(derOutputStream2);
                        this.sigAlgId.encode(derOutputStream2);
                        byte[] bArr = this.tbsCertList;
                        signature2.update(bArr, 0, bArr.length);
                        byte[] sign = signature2.sign();
                        this.signature = sign;
                        derOutputStream2.putBitString(sign);
                        derOutputStream.write((byte) 48, derOutputStream2);
                        this.signedCRL = derOutputStream.toByteArray();
                        this.readOnly = true;
                        return;
                    }
                }
                signature2 = Signature.getInstance(str);
                signature2.initSign(privateKey);
                AlgorithmId algorithmId2 = AlgorithmId.get(signature2.getAlgorithm());
                this.sigAlgId = algorithmId2;
                this.infoSigAlgId = algorithmId2;
                DerOutputStream derOutputStream3 = new DerOutputStream();
                DerOutputStream derOutputStream22 = new DerOutputStream();
                encodeInfo(derOutputStream22);
                this.sigAlgId.encode(derOutputStream22);
                byte[] bArr2 = this.tbsCertList;
                signature2.update(bArr2, 0, bArr2.length);
                byte[] sign2 = signature2.sign();
                this.signature = sign2;
                derOutputStream22.putBitString(sign2);
                derOutputStream3.write((byte) 48, derOutputStream22);
                this.signedCRL = derOutputStream3.toByteArray();
                this.readOnly = true;
                return;
            }
            throw new CRLException("cannot over-write existing CRL");
        } catch (IOException e) {
            throw new CRLException("Error while encoding data: " + e.getMessage());
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        StringBuilder sb = new StringBuilder("X.509 CRL v");
        int i = 1;
        sb.append(this.version + 1);
        sb.append("\n");
        stringBuffer.append(sb.toString());
        if (this.sigAlgId != null) {
            stringBuffer.append("Signature Algorithm: " + this.sigAlgId.toString() + ", OID=" + this.sigAlgId.getOID().toString() + "\n");
        }
        if (this.issuer != null) {
            stringBuffer.append("Issuer: " + this.issuer.toString() + "\n");
        }
        if (this.thisUpdate != null) {
            stringBuffer.append("\nThis Update: " + this.thisUpdate.toString() + "\n");
        }
        if (this.nextUpdate != null) {
            stringBuffer.append("Next Update: " + this.nextUpdate.toString() + "\n");
        }
        if (this.revokedList.isEmpty()) {
            stringBuffer.append("\nNO certificates have been revoked\n");
        } else {
            stringBuffer.append("\nRevoked Certificates: " + this.revokedList.size());
            for (X509CRLEntry x509CRLEntry : this.revokedList) {
                stringBuffer.append("\n[" + i + "] " + x509CRLEntry.toString());
                i++;
            }
        }
        CRLExtensions cRLExtensions = this.extensions;
        if (cRLExtensions != null) {
            Object[] array = cRLExtensions.getAllExtensions().toArray();
            stringBuffer.append("\nCRL Extensions: " + array.length);
            int i2 = 0;
            while (i2 < array.length) {
                StringBuilder sb2 = new StringBuilder("\n[");
                int i3 = i2 + 1;
                sb2.append(i3);
                sb2.append("]: ");
                stringBuffer.append(sb2.toString());
                Extension extension = (Extension) array[i2];
                try {
                    if (OIDMap.getClass(extension.getExtensionId()) == null) {
                        stringBuffer.append(extension.toString());
                        byte[] extensionValue = extension.getExtensionValue();
                        if (extensionValue != null) {
                            DerOutputStream derOutputStream = new DerOutputStream();
                            derOutputStream.putOctetString(extensionValue);
                            byte[] byteArray = derOutputStream.toByteArray();
                            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
                            stringBuffer.append("Extension unknown: DER encoded OCTET string =\n" + hexDumpEncoder.encodeBuffer(byteArray) + "\n");
                        }
                    } else {
                        stringBuffer.append(extension.toString());
                    }
                } catch (Exception unused) {
                    stringBuffer.append(", Error parsing this extension");
                }
                i2 = i3;
            }
        }
        if (this.signature != null) {
            HexDumpEncoder hexDumpEncoder2 = new HexDumpEncoder();
            stringBuffer.append("\nSignature:\n" + hexDumpEncoder2.encodeBuffer(this.signature) + "\n");
        } else {
            stringBuffer.append("NOT signed yet\n");
        }
        return stringBuffer.toString();
    }

    public boolean isRevoked(Certificate certificate) {
        if (this.revokedMap.isEmpty() || !(certificate instanceof X509Certificate)) {
            return false;
        }
        return this.revokedMap.containsKey(new X509IssuerSerial((X509Certificate) certificate));
    }

    public int getVersion() {
        return this.version + 1;
    }

    public Principal getIssuerDN() {
        return this.issuer;
    }

    public X500Principal getIssuerX500Principal() {
        if (this.issuerPrincipal == null) {
            this.issuerPrincipal = this.issuer.asX500Principal();
        }
        return this.issuerPrincipal;
    }

    public Date getThisUpdate() {
        return new Date(this.thisUpdate.getTime());
    }

    public Date getNextUpdate() {
        if (this.nextUpdate == null) {
            return null;
        }
        return new Date(this.nextUpdate.getTime());
    }

    public X509CRLEntry getRevokedCertificate(BigInteger bigInteger) {
        if (this.revokedMap.isEmpty()) {
            return null;
        }
        return this.revokedMap.get(new X509IssuerSerial(getIssuerX500Principal(), bigInteger));
    }

    public X509CRLEntry getRevokedCertificate(X509Certificate x509Certificate) {
        if (this.revokedMap.isEmpty()) {
            return null;
        }
        return this.revokedMap.get(new X509IssuerSerial(x509Certificate));
    }

    public Set<X509CRLEntry> getRevokedCertificates() {
        if (this.revokedList.isEmpty()) {
            return null;
        }
        return new TreeSet(this.revokedList);
    }

    public byte[] getTBSCertList() throws CRLException {
        byte[] bArr = this.tbsCertList;
        if (bArr != null) {
            return (byte[]) bArr.clone();
        }
        throw new CRLException("Uninitialized CRL");
    }

    public byte[] getSignature() {
        byte[] bArr = this.signature;
        if (bArr == null) {
            return null;
        }
        return (byte[]) bArr.clone();
    }

    public String getSigAlgName() {
        AlgorithmId algorithmId = this.sigAlgId;
        if (algorithmId == null) {
            return null;
        }
        return algorithmId.getName();
    }

    public String getSigAlgOID() {
        AlgorithmId algorithmId = this.sigAlgId;
        if (algorithmId == null) {
            return null;
        }
        return algorithmId.getOID().toString();
    }

    public byte[] getSigAlgParams() {
        AlgorithmId algorithmId = this.sigAlgId;
        if (algorithmId == null) {
            return null;
        }
        try {
            return algorithmId.getEncodedParams();
        } catch (IOException unused) {
            return null;
        }
    }

    public AlgorithmId getSigAlgId() {
        return this.sigAlgId;
    }

    public KeyIdentifier getAuthKeyId() throws IOException {
        AuthorityKeyIdentifierExtension authKeyIdExtension = getAuthKeyIdExtension();
        if (authKeyIdExtension != null) {
            return (KeyIdentifier) authKeyIdExtension.get("key_id");
        }
        return null;
    }

    public AuthorityKeyIdentifierExtension getAuthKeyIdExtension() throws IOException {
        return (AuthorityKeyIdentifierExtension) getExtension(PKIXExtensions.AuthorityKey_Id);
    }

    public CRLNumberExtension getCRLNumberExtension() throws IOException {
        return (CRLNumberExtension) getExtension(PKIXExtensions.CRLNumber_Id);
    }

    public BigInteger getCRLNumber() throws IOException {
        CRLNumberExtension cRLNumberExtension = getCRLNumberExtension();
        if (cRLNumberExtension != null) {
            return cRLNumberExtension.get("value");
        }
        return null;
    }

    public DeltaCRLIndicatorExtension getDeltaCRLIndicatorExtension() throws IOException {
        return (DeltaCRLIndicatorExtension) getExtension(PKIXExtensions.DeltaCRLIndicator_Id);
    }

    public BigInteger getBaseCRLNumber() throws IOException {
        DeltaCRLIndicatorExtension deltaCRLIndicatorExtension = getDeltaCRLIndicatorExtension();
        if (deltaCRLIndicatorExtension != null) {
            return deltaCRLIndicatorExtension.get("value");
        }
        return null;
    }

    public IssuerAlternativeNameExtension getIssuerAltNameExtension() throws IOException {
        return (IssuerAlternativeNameExtension) getExtension(PKIXExtensions.IssuerAlternativeName_Id);
    }

    public IssuingDistributionPointExtension getIssuingDistributionPointExtension() throws IOException {
        return (IssuingDistributionPointExtension) getExtension(PKIXExtensions.IssuingDistributionPoint_Id);
    }

    public boolean hasUnsupportedCriticalExtension() {
        CRLExtensions cRLExtensions = this.extensions;
        if (cRLExtensions == null) {
            return false;
        }
        return cRLExtensions.hasUnsupportedCriticalExtension();
    }

    public Set<String> getCriticalExtensionOIDs() {
        if (this.extensions == null) {
            return null;
        }
        TreeSet treeSet = new TreeSet();
        for (Extension next : this.extensions.getAllExtensions()) {
            if (next.isCritical()) {
                treeSet.add(next.getExtensionId().toString());
            }
        }
        return treeSet;
    }

    public Set<String> getNonCriticalExtensionOIDs() {
        if (this.extensions == null) {
            return null;
        }
        TreeSet treeSet = new TreeSet();
        for (Extension next : this.extensions.getAllExtensions()) {
            if (!next.isCritical()) {
                treeSet.add(next.getExtensionId().toString());
            }
        }
        return treeSet;
    }

    public byte[] getExtensionValue(String str) {
        Extension extension;
        byte[] extensionValue;
        if (this.extensions == null) {
            return null;
        }
        try {
            String name = OIDMap.getName(new ObjectIdentifier(str));
            if (name == null) {
                ObjectIdentifier objectIdentifier = new ObjectIdentifier(str);
                Enumeration<Extension> elements = this.extensions.getElements();
                while (true) {
                    if (!elements.hasMoreElements()) {
                        extension = null;
                        break;
                    }
                    extension = elements.nextElement();
                    if (extension.getExtensionId().equals((Object) objectIdentifier)) {
                        break;
                    }
                }
            } else {
                extension = this.extensions.get(name);
            }
            if (extension == null || (extensionValue = extension.getExtensionValue()) == null) {
                return null;
            }
            DerOutputStream derOutputStream = new DerOutputStream();
            derOutputStream.putOctetString(extensionValue);
            return derOutputStream.toByteArray();
        } catch (Exception unused) {
            return null;
        }
    }

    public Object getExtension(ObjectIdentifier objectIdentifier) {
        CRLExtensions cRLExtensions = this.extensions;
        if (cRLExtensions == null) {
            return null;
        }
        return cRLExtensions.get(OIDMap.getName(objectIdentifier));
    }

    private void parse(DerValue derValue) throws CRLException, IOException {
        if (this.readOnly) {
            throw new CRLException("cannot over-write existing CRL");
        } else if (derValue.getData() == null || derValue.tag != 48) {
            throw new CRLException("Invalid DER-encoded CRL data");
        } else {
            this.signedCRL = derValue.toByteArray();
            DerValue[] derValueArr = {derValue.data.getDerValue(), derValue.data.getDerValue(), derValue.data.getDerValue()};
            if (derValue.data.available() != 0) {
                throw new CRLException("signed overrun, bytes = " + derValue.data.available());
            } else if (derValueArr[0].tag == 48) {
                this.sigAlgId = AlgorithmId.parse(derValueArr[1]);
                this.signature = derValueArr[2].getBitString();
                if (derValueArr[1].data.available() != 0) {
                    throw new CRLException("AlgorithmId field overrun");
                } else if (derValueArr[2].data.available() == 0) {
                    this.tbsCertList = derValueArr[0].toByteArray();
                    DerInputStream derInputStream = derValueArr[0].data;
                    this.version = 0;
                    if (((byte) derInputStream.peekByte()) == 2) {
                        int integer = derInputStream.getInteger();
                        this.version = integer;
                        if (integer != 1) {
                            throw new CRLException("Invalid version");
                        }
                    }
                    AlgorithmId parse = AlgorithmId.parse(derInputStream.getDerValue());
                    if (parse.equals(this.sigAlgId)) {
                        this.infoSigAlgId = parse;
                        X500Name x500Name = new X500Name(derInputStream);
                        this.issuer = x500Name;
                        if (!x500Name.isEmpty()) {
                            byte peekByte = (byte) derInputStream.peekByte();
                            if (peekByte == 23) {
                                this.thisUpdate = derInputStream.getUTCTime();
                            } else if (peekByte == 24) {
                                this.thisUpdate = derInputStream.getGeneralizedTime();
                            } else {
                                throw new CRLException("Invalid encoding for thisUpdate (tag=" + peekByte + NavigationBarInflaterView.KEY_CODE_END);
                            }
                            if (derInputStream.available() != 0) {
                                byte peekByte2 = (byte) derInputStream.peekByte();
                                if (peekByte2 == 23) {
                                    this.nextUpdate = derInputStream.getUTCTime();
                                } else if (peekByte2 == 24) {
                                    this.nextUpdate = derInputStream.getGeneralizedTime();
                                }
                                if (derInputStream.available() != 0) {
                                    byte peekByte3 = (byte) derInputStream.peekByte();
                                    if (peekByte3 == 48 && (peekByte3 & DerValue.TAG_PRIVATE) != 128) {
                                        DerValue[] sequence = derInputStream.getSequence(4);
                                        X500Principal issuerX500Principal = getIssuerX500Principal();
                                        X500Principal x500Principal = issuerX500Principal;
                                        for (DerValue x509CRLEntryImpl : sequence) {
                                            X509CRLEntryImpl x509CRLEntryImpl2 = new X509CRLEntryImpl(x509CRLEntryImpl);
                                            x500Principal = getCertIssuer(x509CRLEntryImpl2, x500Principal);
                                            x509CRLEntryImpl2.setCertificateIssuer(issuerX500Principal, x500Principal);
                                            this.revokedMap.put(new X509IssuerSerial(x500Principal, x509CRLEntryImpl2.getSerialNumber()), x509CRLEntryImpl2);
                                            this.revokedList.add(x509CRLEntryImpl2);
                                        }
                                    }
                                    if (derInputStream.available() != 0) {
                                        DerValue derValue2 = derInputStream.getDerValue();
                                        if (derValue2.isConstructed() && derValue2.isContextSpecific((byte) 0)) {
                                            this.extensions = new CRLExtensions(derValue2.data);
                                        }
                                        this.readOnly = true;
                                        return;
                                    }
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        throw new CRLException("Empty issuer DN not allowed in X509CRLs");
                    }
                    throw new CRLException("Signature algorithm mismatch");
                } else {
                    throw new CRLException("Signature field overrun");
                }
            } else {
                throw new CRLException("signed CRL fields invalid");
            }
        }
    }

    public static X500Principal getIssuerX500Principal(X509CRL x509crl) {
        try {
            DerInputStream derInputStream = new DerInputStream(x509crl.getEncoded()).getSequence(3)[0].data;
            if (((byte) derInputStream.peekByte()) == 2) {
                derInputStream.getDerValue();
            }
            derInputStream.getDerValue();
            return new X500Principal(derInputStream.getDerValue().toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Could not parse issuer", e);
        }
    }

    public static byte[] getEncodedInternal(X509CRL x509crl) throws CRLException {
        if (x509crl instanceof X509CRLImpl) {
            return ((X509CRLImpl) x509crl).getEncodedInternal();
        }
        return x509crl.getEncoded();
    }

    public static X509CRLImpl toImpl(X509CRL x509crl) throws CRLException {
        if (x509crl instanceof X509CRLImpl) {
            return (X509CRLImpl) x509crl;
        }
        return X509Factory.intern(x509crl);
    }

    private X500Principal getCertIssuer(X509CRLEntryImpl x509CRLEntryImpl, X500Principal x500Principal) throws IOException {
        CertificateIssuerExtension certificateIssuerExtension = x509CRLEntryImpl.getCertificateIssuerExtension();
        return certificateIssuerExtension != null ? ((X500Name) certificateIssuerExtension.get("issuer").get(0).getName()).asX500Principal() : x500Principal;
    }

    public void derEncode(OutputStream outputStream) throws IOException {
        byte[] bArr = this.signedCRL;
        if (bArr != null) {
            outputStream.write((byte[]) bArr.clone());
            return;
        }
        throw new IOException("Null CRL to encode");
    }

    private static final class X509IssuerSerial implements Comparable<X509IssuerSerial> {
        volatile int hashcode;
        final X500Principal issuer;
        final BigInteger serial;

        X509IssuerSerial(X500Principal x500Principal, BigInteger bigInteger) {
            this.hashcode = 0;
            this.issuer = x500Principal;
            this.serial = bigInteger;
        }

        X509IssuerSerial(X509Certificate x509Certificate) {
            this(x509Certificate.getIssuerX500Principal(), x509Certificate.getSerialNumber());
        }

        /* access modifiers changed from: package-private */
        public X500Principal getIssuer() {
            return this.issuer;
        }

        /* access modifiers changed from: package-private */
        public BigInteger getSerial() {
            return this.serial;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof X509IssuerSerial)) {
                return false;
            }
            X509IssuerSerial x509IssuerSerial = (X509IssuerSerial) obj;
            return this.serial.equals(x509IssuerSerial.getSerial()) && this.issuer.equals(x509IssuerSerial.getIssuer());
        }

        public int hashCode() {
            if (this.hashcode == 0) {
                this.hashcode = ((629 + this.issuer.hashCode()) * 37) + this.serial.hashCode();
            }
            return this.hashcode;
        }

        public int compareTo(X509IssuerSerial x509IssuerSerial) {
            int compareTo = this.issuer.toString().compareTo(x509IssuerSerial.issuer.toString());
            if (compareTo != 0) {
                return compareTo;
            }
            return this.serial.compareTo(x509IssuerSerial.serial);
        }
    }
}
