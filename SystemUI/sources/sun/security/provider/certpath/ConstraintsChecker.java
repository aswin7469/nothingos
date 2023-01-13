package sun.security.provider.certpath;

import java.p026io.IOException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import sun.security.util.Debug;
import sun.security.x509.NameConstraintsExtension;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X509CertImpl;

class ConstraintsChecker extends PKIXCertPathChecker {
    private static final Debug debug = Debug.getInstance("certpath");
    private final int certPathLength;

    /* renamed from: i */
    private int f913i;
    private int maxPathLength;
    private NameConstraintsExtension prevNC;
    private Set<String> supportedExts;

    public boolean isForwardCheckingSupported() {
        return false;
    }

    ConstraintsChecker(int i) {
        this.certPathLength = i;
    }

    public void init(boolean z) throws CertPathValidatorException {
        if (!z) {
            this.f913i = 0;
            this.maxPathLength = this.certPathLength;
            this.prevNC = null;
            return;
        }
        throw new CertPathValidatorException("forward checking not supported");
    }

    public Set<String> getSupportedExtensions() {
        if (this.supportedExts == null) {
            HashSet hashSet = new HashSet(2);
            this.supportedExts = hashSet;
            hashSet.add(PKIXExtensions.BasicConstraints_Id.toString());
            this.supportedExts.add(PKIXExtensions.NameConstraints_Id.toString());
            this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
        }
        return this.supportedExts;
    }

    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        this.f913i++;
        checkBasicConstraints(x509Certificate);
        verifyNameConstraints(x509Certificate);
        if (collection != null && !collection.isEmpty()) {
            collection.remove(PKIXExtensions.BasicConstraints_Id.toString());
            collection.remove(PKIXExtensions.NameConstraints_Id.toString());
        }
    }

    private void verifyNameConstraints(X509Certificate x509Certificate) throws CertPathValidatorException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("---checking name constraints...");
        }
        if (this.prevNC != null && (this.f913i == this.certPathLength || !X509CertImpl.isSelfIssued(x509Certificate))) {
            if (debug2 != null) {
                debug2.println("prevNC = " + this.prevNC + ", currDN = " + x509Certificate.getSubjectX500Principal());
            }
            try {
                if (!this.prevNC.verify(x509Certificate)) {
                    throw new CertPathValidatorException("name constraints check failed", (Throwable) null, (CertPath) null, -1, PKIXReason.INVALID_NAME);
                }
            } catch (IOException e) {
                throw new CertPathValidatorException((Throwable) e);
            }
        }
        this.prevNC = mergeNameConstraints(x509Certificate, this.prevNC);
        if (debug2 != null) {
            debug2.println("name constraints verified.");
        }
    }

    static NameConstraintsExtension mergeNameConstraints(X509Certificate x509Certificate, NameConstraintsExtension nameConstraintsExtension) throws CertPathValidatorException {
        try {
            NameConstraintsExtension nameConstraintsExtension2 = X509CertImpl.toImpl(x509Certificate).getNameConstraintsExtension();
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("prevNC = " + nameConstraintsExtension + ", newNC = " + String.valueOf((Object) nameConstraintsExtension2));
            }
            if (nameConstraintsExtension == null) {
                if (debug2 != null) {
                    debug2.println("mergedNC = " + String.valueOf((Object) nameConstraintsExtension2));
                }
                if (nameConstraintsExtension2 == null) {
                    return nameConstraintsExtension2;
                }
                return (NameConstraintsExtension) nameConstraintsExtension2.clone();
            }
            try {
                nameConstraintsExtension.merge(nameConstraintsExtension2);
                if (debug2 != null) {
                    debug2.println("mergedNC = " + nameConstraintsExtension);
                }
                return nameConstraintsExtension;
            } catch (IOException e) {
                throw new CertPathValidatorException((Throwable) e);
            }
        } catch (CertificateException e2) {
            throw new CertPathValidatorException((Throwable) e2);
        }
    }

    private void checkBasicConstraints(X509Certificate x509Certificate) throws CertPathValidatorException {
        int i;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("---checking basic constraints...");
            debug2.println("i = " + this.f913i + ", maxPathLength = " + this.maxPathLength);
        }
        if (this.f913i < this.certPathLength) {
            if (x509Certificate.getVersion() < 3) {
                i = (this.f913i != 1 || !X509CertImpl.isSelfIssued(x509Certificate)) ? -1 : Integer.MAX_VALUE;
            } else {
                i = x509Certificate.getBasicConstraints();
            }
            if (i != -1) {
                if (!X509CertImpl.isSelfIssued(x509Certificate)) {
                    int i2 = this.maxPathLength;
                    if (i2 > 0) {
                        this.maxPathLength = i2 - 1;
                    } else {
                        throw new CertPathValidatorException("basic constraints check failed: pathLenConstraint violated - this cert must be the last cert in the certification path", (Throwable) null, (CertPath) null, -1, PKIXReason.PATH_TOO_LONG);
                    }
                }
                if (i < this.maxPathLength) {
                    this.maxPathLength = i;
                }
            } else {
                throw new CertPathValidatorException("basic constraints check failed: this is not a CA certificate", (Throwable) null, (CertPath) null, -1, PKIXReason.NOT_CA_CERT);
            }
        }
        if (debug2 != null) {
            debug2.println("after processing, maxPathLength = " + this.maxPathLength);
            debug2.println("basic constraints verified.");
        }
    }

    static int mergeBasicConstraints(X509Certificate x509Certificate, int i) {
        int basicConstraints = x509Certificate.getBasicConstraints();
        if (!X509CertImpl.isSelfIssued(x509Certificate)) {
            i--;
        }
        return basicConstraints < i ? basicConstraints : i;
    }
}
