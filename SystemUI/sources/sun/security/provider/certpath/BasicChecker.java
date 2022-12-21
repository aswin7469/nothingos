package sun.security.provider.certpath;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.util.Debug;
import sun.security.x509.X500Name;

class BasicChecker extends PKIXCertPathChecker {
    private static final Debug debug = Debug.getInstance("certpath");
    private final X500Principal caName;
    private final Date date;
    private PublicKey prevPubKey;
    private X500Principal prevSubject;
    private final boolean sigOnly;
    private final String sigProvider;
    private final PublicKey trustedPubKey;

    public Set<String> getSupportedExtensions() {
        return null;
    }

    public boolean isForwardCheckingSupported() {
        return false;
    }

    BasicChecker(TrustAnchor trustAnchor, Date date2, String str, boolean z) {
        if (trustAnchor.getTrustedCert() != null) {
            this.trustedPubKey = trustAnchor.getTrustedCert().getPublicKey();
            this.caName = trustAnchor.getTrustedCert().getSubjectX500Principal();
        } else {
            this.trustedPubKey = trustAnchor.getCAPublicKey();
            this.caName = trustAnchor.getCA();
        }
        this.date = date2;
        this.sigProvider = str;
        this.sigOnly = z;
        this.prevPubKey = this.trustedPubKey;
    }

    public void init(boolean z) throws CertPathValidatorException {
        if (!z) {
            PublicKey publicKey = this.trustedPubKey;
            this.prevPubKey = publicKey;
            if (!PKIX.isDSAPublicKeyWithoutParams(publicKey)) {
                this.prevSubject = this.caName;
                return;
            }
            throw new CertPathValidatorException("Key parameters missing");
        }
        throw new CertPathValidatorException("forward checking not supported");
    }

    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        X509Certificate x509Certificate = (X509Certificate) certificate;
        if (!this.sigOnly) {
            verifyTimestamp(x509Certificate);
            verifyNameChaining(x509Certificate);
        }
        verifySignature(x509Certificate);
        updateState(x509Certificate);
    }

    private void verifySignature(X509Certificate x509Certificate) throws CertPathValidatorException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("---checking signature...");
        }
        try {
            String str = this.sigProvider;
            if (str != null) {
                x509Certificate.verify(this.prevPubKey, str);
            } else {
                x509Certificate.verify(this.prevPubKey);
            }
            if (debug2 != null) {
                debug2.println("signature verified.");
            }
        } catch (SignatureException e) {
            throw new CertPathValidatorException("signature check failed", e, (CertPath) null, -1, CertPathValidatorException.BasicReason.INVALID_SIGNATURE);
        } catch (GeneralSecurityException e2) {
            throw new CertPathValidatorException("signature check failed", e2);
        }
    }

    private void verifyTimestamp(X509Certificate x509Certificate) throws CertPathValidatorException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("---checking timestamp:" + this.date.toString() + "...");
        }
        try {
            x509Certificate.checkValidity(this.date);
            if (debug2 != null) {
                debug2.println("timestamp verified.");
            }
        } catch (CertificateExpiredException e) {
            throw new CertPathValidatorException("timestamp check failed", e, (CertPath) null, -1, CertPathValidatorException.BasicReason.EXPIRED);
        } catch (CertificateNotYetValidException e2) {
            throw new CertPathValidatorException("timestamp check failed", e2, (CertPath) null, -1, CertPathValidatorException.BasicReason.NOT_YET_VALID);
        }
    }

    private void verifyNameChaining(X509Certificate x509Certificate) throws CertPathValidatorException {
        if (this.prevSubject != null) {
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("---checking subject/issuer name chaining...");
            }
            X500Principal issuerX500Principal = x509Certificate.getIssuerX500Principal();
            if (X500Name.asX500Name(issuerX500Principal).isEmpty()) {
                throw new CertPathValidatorException("subject/issuer name chaining check failed: empty/null issuer DN in certificate is invalid", (Throwable) null, (CertPath) null, -1, PKIXReason.NAME_CHAINING);
            } else if (!issuerX500Principal.equals(this.prevSubject)) {
                throw new CertPathValidatorException("subject/issuer name chaining check failed", (Throwable) null, (CertPath) null, -1, PKIXReason.NAME_CHAINING);
            } else if (debug2 != null) {
                debug2.println("subject/issuer name chaining verified.");
            }
        }
    }

    private void updateState(X509Certificate x509Certificate) throws CertPathValidatorException {
        PublicKey publicKey = x509Certificate.getPublicKey();
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("BasicChecker.updateState issuer: " + x509Certificate.getIssuerX500Principal().toString() + "; subject: " + x509Certificate.getSubjectX500Principal() + "; serial#: " + x509Certificate.getSerialNumber().toString());
        }
        if (PKIX.isDSAPublicKeyWithoutParams(publicKey)) {
            publicKey = makeInheritedParamsKey(publicKey, this.prevPubKey);
            if (debug2 != null) {
                debug2.println("BasicChecker.updateState Made key with inherited params");
            }
        }
        this.prevPubKey = publicKey;
        this.prevSubject = x509Certificate.getSubjectX500Principal();
    }

    static PublicKey makeInheritedParamsKey(PublicKey publicKey, PublicKey publicKey2) throws CertPathValidatorException {
        if (!(publicKey instanceof DSAPublicKey) || !(publicKey2 instanceof DSAPublicKey)) {
            throw new CertPathValidatorException("Input key is not appropriate type for inheriting parameters");
        }
        DSAParams params = ((DSAPublicKey) publicKey2).getParams();
        if (params != null) {
            try {
                return KeyFactory.getInstance("DSA").generatePublic(new DSAPublicKeySpec(((DSAPublicKey) publicKey).getY(), params.getP(), params.getQ(), params.getG()));
            } catch (GeneralSecurityException e) {
                throw new CertPathValidatorException("Unable to generate key with inherited parameters: " + e.getMessage(), e);
            }
        } else {
            throw new CertPathValidatorException("Key parameters missing");
        }
    }

    /* access modifiers changed from: package-private */
    public PublicKey getPublicKey() {
        return this.prevPubKey;
    }
}
