package sun.security.provider.certpath;

import java.security.AlgorithmConstraints;
import java.security.AlgorithmParameters;
import java.security.CryptoPrimitive;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import sun.security.util.AnchorCertificates;
import sun.security.util.CertConstraintParameters;
import sun.security.util.Debug;
import sun.security.util.DisabledAlgorithmConstraints;
import sun.security.util.KeyUtil;
import sun.security.x509.AlgorithmId;
import sun.security.x509.X509CRLImpl;
import sun.security.x509.X509CertImpl;

public final class AlgorithmChecker extends PKIXCertPathChecker {
    private static final Set<CryptoPrimitive> KU_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.m1719of(CryptoPrimitive.SIGNATURE, CryptoPrimitive.KEY_ENCAPSULATION, CryptoPrimitive.PUBLIC_KEY_ENCRYPTION, CryptoPrimitive.KEY_AGREEMENT));
    private static final Set<CryptoPrimitive> SIGNATURE_PRIMITIVE_SET = Collections.unmodifiableSet(EnumSet.m1716of(CryptoPrimitive.SIGNATURE));
    private static final DisabledAlgorithmConstraints certPathDefaultConstraints;
    private static final Debug debug = Debug.getInstance("certpath");
    private static final boolean publicCALimits;
    private final AlgorithmConstraints constraints;
    private PublicKey prevPubKey;
    private boolean trustedMatch;
    private final PublicKey trustedPubKey;

    public Set<String> getSupportedExtensions() {
        return null;
    }

    public boolean isForwardCheckingSupported() {
        return false;
    }

    static {
        DisabledAlgorithmConstraints disabledAlgorithmConstraints = new DisabledAlgorithmConstraints(DisabledAlgorithmConstraints.PROPERTY_CERTPATH_DISABLED_ALGS);
        certPathDefaultConstraints = disabledAlgorithmConstraints;
        publicCALimits = disabledAlgorithmConstraints.checkProperty("jdkCA");
    }

    public AlgorithmChecker(TrustAnchor trustAnchor) {
        this(trustAnchor, certPathDefaultConstraints);
    }

    public AlgorithmChecker(AlgorithmConstraints algorithmConstraints) {
        this.trustedMatch = false;
        this.prevPubKey = null;
        this.trustedPubKey = null;
        this.constraints = algorithmConstraints;
    }

    public AlgorithmChecker(TrustAnchor trustAnchor, AlgorithmConstraints algorithmConstraints) {
        Debug debug2;
        this.trustedMatch = false;
        if (trustAnchor != null) {
            if (trustAnchor.getTrustedCert() != null) {
                this.trustedPubKey = trustAnchor.getTrustedCert().getPublicKey();
                boolean checkFingerprint = checkFingerprint(trustAnchor.getTrustedCert());
                this.trustedMatch = checkFingerprint;
                if (checkFingerprint && (debug2 = debug) != null) {
                    debug2.println("trustedMatch = true");
                }
            } else {
                this.trustedPubKey = trustAnchor.getCAPublicKey();
            }
            this.prevPubKey = this.trustedPubKey;
            this.constraints = algorithmConstraints;
            return;
        }
        throw new IllegalArgumentException("The trust anchor cannot be null");
    }

    private static boolean checkFingerprint(X509Certificate x509Certificate) {
        if (!publicCALimits) {
            return false;
        }
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("AlgorithmChecker.contains: " + x509Certificate.getSigAlgName());
        }
        return AnchorCertificates.contains(x509Certificate);
    }

    public void init(boolean z) throws CertPathValidatorException {
        if (!z) {
            PublicKey publicKey = this.trustedPubKey;
            if (publicKey != null) {
                this.prevPubKey = publicKey;
            } else {
                this.prevPubKey = null;
            }
        } else {
            throw new CertPathValidatorException("forward checking not supported");
        }
    }

    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        if ((certificate instanceof X509Certificate) && this.constraints != null) {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            boolean[] keyUsage = x509Certificate.getKeyUsage();
            if (keyUsage == null || keyUsage.length >= 9) {
                Set set = KU_PRIMITIVE_SET;
                if (keyUsage != null) {
                    set = EnumSet.noneOf(CryptoPrimitive.class);
                    if (keyUsage[0] || keyUsage[1] || keyUsage[5] || keyUsage[6]) {
                        set.add(CryptoPrimitive.SIGNATURE);
                    }
                    if (keyUsage[2]) {
                        set.add(CryptoPrimitive.KEY_ENCAPSULATION);
                    }
                    if (keyUsage[3]) {
                        set.add(CryptoPrimitive.PUBLIC_KEY_ENCRYPTION);
                    }
                    if (keyUsage[4]) {
                        set.add(CryptoPrimitive.KEY_AGREEMENT);
                    }
                    if (set.isEmpty()) {
                        throw new CertPathValidatorException("incorrect KeyUsage extension bits", (Throwable) null, (CertPath) null, -1, PKIXReason.INVALID_KEY_USAGE);
                    }
                }
                PublicKey publicKey = certificate.getPublicKey();
                AlgorithmConstraints algorithmConstraints = this.constraints;
                if (algorithmConstraints instanceof DisabledAlgorithmConstraints) {
                    ((DisabledAlgorithmConstraints) algorithmConstraints).permits((Set<CryptoPrimitive>) set, new CertConstraintParameters(x509Certificate, this.trustedMatch));
                    if (this.prevPubKey == null) {
                        this.prevPubKey = publicKey;
                        return;
                    }
                }
                try {
                    X509CertImpl impl = X509CertImpl.toImpl((X509Certificate) certificate);
                    AlgorithmParameters parameters = ((AlgorithmId) impl.get(X509CertImpl.SIG_ALG)).getParameters();
                    String sigAlgName = impl.getSigAlgName();
                    AlgorithmConstraints algorithmConstraints2 = this.constraints;
                    if (!(algorithmConstraints2 instanceof DisabledAlgorithmConstraints)) {
                        if (!algorithmConstraints2.permits(SIGNATURE_PRIMITIVE_SET, sigAlgName, parameters)) {
                            throw new CertPathValidatorException("Algorithm constraints check failed on signature algorithm: " + sigAlgName, (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
                        } else if (!this.constraints.permits(set, publicKey)) {
                            throw new CertPathValidatorException("Algorithm constraints check failed on keysize: " + KeyUtil.getKeySize(publicKey), (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
                        }
                    }
                    PublicKey publicKey2 = this.prevPubKey;
                    if (publicKey2 != null) {
                        if (!this.constraints.permits(SIGNATURE_PRIMITIVE_SET, sigAlgName, publicKey2, parameters)) {
                            throw new CertPathValidatorException("Algorithm constraints check failed on signature algorithm: " + sigAlgName, (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
                        } else if (PKIX.isDSAPublicKeyWithoutParams(publicKey)) {
                            PublicKey publicKey3 = this.prevPubKey;
                            if (publicKey3 instanceof DSAPublicKey) {
                                DSAParams params = ((DSAPublicKey) publicKey3).getParams();
                                if (params != null) {
                                    try {
                                        publicKey = KeyFactory.getInstance("DSA").generatePublic(new DSAPublicKeySpec(((DSAPublicKey) publicKey).getY(), params.getP(), params.getQ(), params.getG()));
                                    } catch (GeneralSecurityException e) {
                                        throw new CertPathValidatorException("Unable to generate key with inherited parameters: " + e.getMessage(), e);
                                    }
                                } else {
                                    throw new CertPathValidatorException("Key parameters missing from public key.");
                                }
                            } else {
                                throw new CertPathValidatorException("Input key is not of a appropriate type for inheriting parameters");
                            }
                        }
                    }
                    this.prevPubKey = publicKey;
                } catch (CertificateException e2) {
                    throw new CertPathValidatorException((Throwable) e2);
                }
            } else {
                throw new CertPathValidatorException("incorrect KeyUsage extension", (Throwable) null, (CertPath) null, -1, PKIXReason.INVALID_KEY_USAGE);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void trySetTrustAnchor(TrustAnchor trustAnchor) {
        Debug debug2;
        if (this.prevPubKey != null) {
            return;
        }
        if (trustAnchor == null) {
            throw new IllegalArgumentException("The trust anchor cannot be null");
        } else if (trustAnchor.getTrustedCert() != null) {
            this.prevPubKey = trustAnchor.getTrustedCert().getPublicKey();
            boolean checkFingerprint = checkFingerprint(trustAnchor.getTrustedCert());
            this.trustedMatch = checkFingerprint;
            if (checkFingerprint && (debug2 = debug) != null) {
                debug2.println("trustedMatch = true");
            }
        } else {
            this.prevPubKey = trustAnchor.getCAPublicKey();
        }
    }

    static void check(PublicKey publicKey, X509CRL x509crl) throws CertPathValidatorException {
        try {
            check(publicKey, X509CRLImpl.toImpl(x509crl).getSigAlgId());
        } catch (CRLException e) {
            throw new CertPathValidatorException((Throwable) e);
        }
    }

    static void check(PublicKey publicKey, AlgorithmId algorithmId) throws CertPathValidatorException {
        String name = algorithmId.getName();
        if (!certPathDefaultConstraints.permits(SIGNATURE_PRIMITIVE_SET, name, publicKey, algorithmId.getParameters())) {
            throw new CertPathValidatorException("Algorithm constraints check failed on signature algorithm: " + name + " is disabled", (Throwable) null, (CertPath) null, -1, CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED);
        }
    }
}
