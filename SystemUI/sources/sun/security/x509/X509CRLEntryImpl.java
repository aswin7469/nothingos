package sun.security.x509;

import java.math.BigInteger;
import java.p026io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CRLReason;
import java.security.cert.Extension;
import java.security.cert.X509CRLEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.security.auth.x500.X500Principal;
import sun.misc.HexDumpEncoder;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class X509CRLEntryImpl extends X509CRLEntry implements Comparable<X509CRLEntryImpl> {
    private static final long YR_2050 = 2524636800000L;
    private static final boolean isExplicit = false;
    private X500Principal certIssuer;
    private CRLExtensions extensions = null;
    private Date revocationDate = null;
    private byte[] revokedCert = null;
    private SerialNumber serialNumber = null;

    public X509CRLEntryImpl(BigInteger bigInteger, Date date) {
        this.serialNumber = new SerialNumber(bigInteger);
        this.revocationDate = date;
    }

    public X509CRLEntryImpl(BigInteger bigInteger, Date date, CRLExtensions cRLExtensions) {
        this.serialNumber = new SerialNumber(bigInteger);
        this.revocationDate = date;
        this.extensions = cRLExtensions;
    }

    public X509CRLEntryImpl(byte[] bArr) throws CRLException {
        try {
            parse(new DerValue(bArr));
        } catch (IOException e) {
            this.revokedCert = null;
            throw new CRLException("Parsing error: " + e.toString());
        }
    }

    public X509CRLEntryImpl(DerValue derValue) throws CRLException {
        try {
            parse(derValue);
        } catch (IOException e) {
            this.revokedCert = null;
            throw new CRLException("Parsing error: " + e.toString());
        }
    }

    public boolean hasExtensions() {
        return this.extensions != null;
    }

    public void encode(DerOutputStream derOutputStream) throws CRLException {
        try {
            if (this.revokedCert == null) {
                DerOutputStream derOutputStream2 = new DerOutputStream();
                this.serialNumber.encode(derOutputStream2);
                if (this.revocationDate.getTime() < YR_2050) {
                    derOutputStream2.putUTCTime(this.revocationDate);
                } else {
                    derOutputStream2.putGeneralizedTime(this.revocationDate);
                }
                CRLExtensions cRLExtensions = this.extensions;
                if (cRLExtensions != null) {
                    cRLExtensions.encode(derOutputStream2, false);
                }
                DerOutputStream derOutputStream3 = new DerOutputStream();
                derOutputStream3.write((byte) 48, derOutputStream2);
                this.revokedCert = derOutputStream3.toByteArray();
            }
            derOutputStream.write(this.revokedCert);
        } catch (IOException e) {
            throw new CRLException("Encoding error: " + e.toString());
        }
    }

    public byte[] getEncoded() throws CRLException {
        return (byte[]) getEncoded0().clone();
    }

    private byte[] getEncoded0() throws CRLException {
        if (this.revokedCert == null) {
            encode(new DerOutputStream());
        }
        return this.revokedCert;
    }

    public X500Principal getCertificateIssuer() {
        return this.certIssuer;
    }

    /* access modifiers changed from: package-private */
    public void setCertificateIssuer(X500Principal x500Principal, X500Principal x500Principal2) {
        if (x500Principal.equals(x500Principal2)) {
            this.certIssuer = null;
        } else {
            this.certIssuer = x500Principal2;
        }
    }

    public BigInteger getSerialNumber() {
        return this.serialNumber.getNumber();
    }

    public Date getRevocationDate() {
        return new Date(this.revocationDate.getTime());
    }

    public CRLReason getRevocationReason() {
        Extension extension = getExtension(PKIXExtensions.ReasonCode_Id);
        if (extension == null) {
            return null;
        }
        return ((CRLReasonCodeExtension) extension).getReasonCode();
    }

    public static CRLReason getRevocationReason(X509CRLEntry x509CRLEntry) {
        try {
            byte[] extensionValue = x509CRLEntry.getExtensionValue("2.5.29.21");
            if (extensionValue == null) {
                return null;
            }
            return new CRLReasonCodeExtension(Boolean.FALSE, (Object) new DerValue(extensionValue).getOctetString()).getReasonCode();
        } catch (IOException unused) {
            return null;
        }
    }

    public Integer getReasonCode() throws IOException {
        Extension extension = getExtension(PKIXExtensions.ReasonCode_Id);
        if (extension == null) {
            return null;
        }
        return ((CRLReasonCodeExtension) extension).get("reason");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.serialNumber.toString());
        sb.append("  On: " + this.revocationDate.toString());
        if (this.certIssuer != null) {
            sb.append("\n    Certificate issuer: " + this.certIssuer);
        }
        CRLExtensions cRLExtensions = this.extensions;
        if (cRLExtensions != null) {
            int i = 0;
            Extension[] extensionArr = (Extension[]) cRLExtensions.getAllExtensions().toArray((T[]) new Extension[0]);
            sb.append("\n    CRL Entry Extensions: " + extensionArr.length);
            while (i < extensionArr.length) {
                StringBuilder sb2 = new StringBuilder("\n    [");
                int i2 = i + 1;
                sb2.append(i2);
                sb2.append("]: ");
                sb.append(sb2.toString());
                Extension extension = extensionArr[i];
                try {
                    if (OIDMap.getClass(extension.getExtensionId()) == null) {
                        sb.append(extension.toString());
                        byte[] extensionValue = extension.getExtensionValue();
                        if (extensionValue != null) {
                            DerOutputStream derOutputStream = new DerOutputStream();
                            derOutputStream.putOctetString(extensionValue);
                            byte[] byteArray = derOutputStream.toByteArray();
                            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
                            sb.append("Extension unknown: DER encoded OCTET string =\n" + hexDumpEncoder.encodeBuffer(byteArray) + "\n");
                        }
                    } else {
                        sb.append(extension.toString());
                    }
                } catch (Exception unused) {
                    sb.append(", Error parsing this extension");
                }
                i = i2;
            }
        }
        sb.append("\n");
        return sb.toString();
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

    public Extension getExtension(ObjectIdentifier objectIdentifier) {
        CRLExtensions cRLExtensions = this.extensions;
        if (cRLExtensions == null) {
            return null;
        }
        return cRLExtensions.get(OIDMap.getName(objectIdentifier));
    }

    private void parse(DerValue derValue) throws CRLException, IOException {
        if (derValue.tag != 48) {
            throw new CRLException("Invalid encoded RevokedCertificate, starting sequence tag missing.");
        } else if (derValue.data.available() != 0) {
            this.revokedCert = derValue.toByteArray();
            this.serialNumber = new SerialNumber(derValue.toDerInputStream().getDerValue());
            byte peekByte = (byte) derValue.data.peekByte();
            if (peekByte == 23) {
                this.revocationDate = derValue.data.getUTCTime();
            } else if (peekByte == 24) {
                this.revocationDate = derValue.data.getGeneralizedTime();
            } else {
                throw new CRLException("Invalid encoding for revocation date");
            }
            if (derValue.data.available() != 0) {
                this.extensions = new CRLExtensions(derValue.toDerInputStream());
            }
        } else {
            throw new CRLException("No data encoded for RevokedCertificates");
        }
    }

    public static X509CRLEntryImpl toImpl(X509CRLEntry x509CRLEntry) throws CRLException {
        if (x509CRLEntry instanceof X509CRLEntryImpl) {
            return (X509CRLEntryImpl) x509CRLEntry;
        }
        return new X509CRLEntryImpl(x509CRLEntry.getEncoded());
    }

    /* access modifiers changed from: package-private */
    public CertificateIssuerExtension getCertificateIssuerExtension() {
        return (CertificateIssuerExtension) getExtension(PKIXExtensions.CertificateIssuer_Id);
    }

    public Map<String, Extension> getExtensions() {
        CRLExtensions cRLExtensions = this.extensions;
        if (cRLExtensions == null) {
            return Collections.emptyMap();
        }
        Collection<Extension> allExtensions = cRLExtensions.getAllExtensions();
        TreeMap treeMap = new TreeMap();
        for (Extension next : allExtensions) {
            treeMap.put(next.getId(), next);
        }
        return treeMap;
    }

    public int compareTo(X509CRLEntryImpl x509CRLEntryImpl) {
        int compareTo = getSerialNumber().compareTo(x509CRLEntryImpl.getSerialNumber());
        if (compareTo != 0) {
            return compareTo;
        }
        try {
            byte[] encoded0 = getEncoded0();
            byte[] encoded02 = x509CRLEntryImpl.getEncoded0();
            int i = 0;
            while (i < encoded0.length && i < encoded02.length) {
                byte b = encoded0[i] & 255;
                byte b2 = encoded02[i] & 255;
                if (b != b2) {
                    return b - b2;
                }
                i++;
            }
            return encoded0.length - encoded02.length;
        } catch (CRLException unused) {
            return -1;
        }
    }
}
