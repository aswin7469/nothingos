package sun.security.provider.certpath;

import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.Certificate;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import sun.security.util.Debug;
import sun.security.x509.PKIXExtensions;

class KeyChecker extends PKIXCertPathChecker {
    private static final int KEY_CERT_SIGN = 5;
    private static final Debug debug = Debug.getInstance("certpath");
    private final int certPathLen;
    private int remainingCerts;
    private Set<String> supportedExts;
    private final CertSelector targetConstraints;

    public boolean isForwardCheckingSupported() {
        return false;
    }

    KeyChecker(int i, CertSelector certSelector) {
        this.certPathLen = i;
        this.targetConstraints = certSelector;
    }

    public void init(boolean z) throws CertPathValidatorException {
        if (!z) {
            this.remainingCerts = this.certPathLen;
            return;
        }
        throw new CertPathValidatorException("forward checking not supported");
    }

    public Set<String> getSupportedExtensions() {
        if (this.supportedExts == null) {
            HashSet hashSet = new HashSet(3);
            this.supportedExts = hashSet;
            hashSet.add(PKIXExtensions.KeyUsage_Id.toString());
            this.supportedExts.add(PKIXExtensions.ExtendedKeyUsage_Id.toString());
            this.supportedExts.add(PKIXExtensions.SubjectAlternativeName_Id.toString());
            this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
        }
        return this.supportedExts;
    }

    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        int i = this.remainingCerts - 1;
        this.remainingCerts = i;
        if (i == 0) {
            CertSelector certSelector = this.targetConstraints;
            if (certSelector != null && !certSelector.match(x509Certificate)) {
                throw new CertPathValidatorException("target certificate constraints check failed");
            }
        } else {
            verifyCAKeyUsage(x509Certificate);
        }
        if (collection != null && !collection.isEmpty()) {
            collection.remove(PKIXExtensions.KeyUsage_Id.toString());
            collection.remove(PKIXExtensions.ExtendedKeyUsage_Id.toString());
            collection.remove(PKIXExtensions.SubjectAlternativeName_Id.toString());
        }
    }

    static void verifyCAKeyUsage(X509Certificate x509Certificate) throws CertPathValidatorException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("KeyChecker.verifyCAKeyUsage() ---checking CA key usage...");
        }
        boolean[] keyUsage = x509Certificate.getKeyUsage();
        if (keyUsage != null) {
            if (!keyUsage[5]) {
                throw new CertPathValidatorException("CA key usage check failed: keyCertSign bit is not set", (Throwable) null, (CertPath) null, -1, PKIXReason.INVALID_KEY_USAGE);
            } else if (debug2 != null) {
                debug2.println("KeyChecker.verifyCAKeyUsage() CA key usage verified.");
            }
        }
    }
}
