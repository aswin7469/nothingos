package sun.security.provider.certpath;

import java.math.BigInteger;
import java.p026io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.SerialNumber;

class AdaptableX509CertSelector extends X509CertSelector {
    private static final Debug debug = Debug.getInstance("certpath");
    private Date endDate;
    private BigInteger serial;
    private byte[] ski;
    private Date startDate;

    AdaptableX509CertSelector() {
    }

    /* access modifiers changed from: package-private */
    public void setValidityPeriod(Date date, Date date2) {
        this.startDate = date;
        this.endDate = date2;
    }

    public void setSubjectKeyIdentifier(byte[] bArr) {
        throw new IllegalArgumentException();
    }

    public void setSerialNumber(BigInteger bigInteger) {
        throw new IllegalArgumentException();
    }

    /* access modifiers changed from: package-private */
    public void setSkiAndSerialNumber(AuthorityKeyIdentifierExtension authorityKeyIdentifierExtension) throws IOException {
        this.ski = null;
        this.serial = null;
        if (authorityKeyIdentifierExtension != null) {
            this.ski = authorityKeyIdentifierExtension.getEncodedKeyIdentifier();
            SerialNumber serialNumber = (SerialNumber) authorityKeyIdentifierExtension.get(AuthorityKeyIdentifierExtension.SERIAL_NUMBER);
            if (serialNumber != null) {
                this.serial = serialNumber.getNumber();
            }
        }
    }

    public boolean match(Certificate certificate) {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        if (!matchSubjectKeyID(x509Certificate)) {
            return false;
        }
        int version = x509Certificate.getVersion();
        BigInteger bigInteger = this.serial;
        if (bigInteger != null && version > 2 && !bigInteger.equals(x509Certificate.getSerialNumber())) {
            return false;
        }
        if (version < 3) {
            Date date = this.startDate;
            if (date != null) {
                try {
                    x509Certificate.checkValidity(date);
                } catch (CertificateException unused) {
                    return false;
                }
            }
            Date date2 = this.endDate;
            if (date2 != null) {
                try {
                    x509Certificate.checkValidity(date2);
                } catch (CertificateException unused2) {
                    return false;
                }
            }
        }
        if (!super.match(certificate)) {
            return false;
        }
        return true;
    }

    private boolean matchSubjectKeyID(X509Certificate x509Certificate) {
        if (this.ski == null) {
            return true;
        }
        try {
            byte[] extensionValue = x509Certificate.getExtensionValue("2.5.29.14");
            if (extensionValue == null) {
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("AdaptableX509CertSelector.match: no subject key ID extension. Subject: " + x509Certificate.getSubjectX500Principal());
                }
                return true;
            }
            byte[] octetString = new DerInputStream(extensionValue).getOctetString();
            if (octetString != null) {
                if (Arrays.equals(this.ski, octetString)) {
                    return true;
                }
            }
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("AdaptableX509CertSelector.match: subject key IDs don't match. Expected: " + Arrays.toString(this.ski) + " Cert's: " + Arrays.toString(octetString));
            }
            return false;
        } catch (IOException unused) {
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("AdaptableX509CertSelector.match: exception in subject key ID check");
            }
            return false;
        }
    }

    public Object clone() {
        AdaptableX509CertSelector adaptableX509CertSelector = (AdaptableX509CertSelector) super.clone();
        Date date = this.startDate;
        if (date != null) {
            adaptableX509CertSelector.startDate = (Date) date.clone();
        }
        Date date2 = this.endDate;
        if (date2 != null) {
            adaptableX509CertSelector.endDate = (Date) date2.clone();
        }
        byte[] bArr = this.ski;
        if (bArr != null) {
            adaptableX509CertSelector.ski = (byte[]) bArr.clone();
        }
        return adaptableX509CertSelector;
    }
}
