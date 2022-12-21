package sun.security.provider.certpath;

import java.p026io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.cert.CertPath;
import java.security.cert.CertPathChecker;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertPathValidatorResult;
import java.security.cert.CertPathValidatorSpi;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXReason;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.PolicyQualifierInfo;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.x509.X509CertImpl;

public final class PKIXCertPathValidator extends CertPathValidatorSpi {
    private static final Debug debug = Debug.getInstance("certpath");

    public CertPathChecker engineGetRevocationChecker() {
        return new RevocationChecker();
    }

    public CertPathValidatorResult engineValidate(CertPath certPath, CertPathParameters certPathParameters) throws CertPathValidatorException, InvalidAlgorithmParameterException {
        return validate(PKIX.checkParams(certPath, certPathParameters));
    }

    private static PKIXCertPathValidatorResult validate(PKIX.ValidatorParams validatorParams) throws CertPathValidatorException {
        AdaptableX509CertSelector adaptableX509CertSelector;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("PKIXCertPathValidator.engineValidate()...");
        }
        List<X509Certificate> certificates = validatorParams.certificates();
        CertPathValidatorException e = null;
        if (!certificates.isEmpty()) {
            adaptableX509CertSelector = new AdaptableX509CertSelector();
            X509Certificate x509Certificate = certificates.get(0);
            adaptableX509CertSelector.setSubject(x509Certificate.getIssuerX500Principal());
            try {
                adaptableX509CertSelector.setSkiAndSerialNumber(X509CertImpl.toImpl(x509Certificate).getAuthorityKeyIdentifierExtension());
            } catch (IOException | CertificateException unused) {
            }
        } else {
            adaptableX509CertSelector = null;
        }
        for (TrustAnchor next : validatorParams.trustAnchors()) {
            X509Certificate trustedCert = next.getTrustedCert();
            if (trustedCert == null) {
                Debug debug3 = debug;
                if (debug3 != null) {
                    debug3.println("PKIXCertPathValidator.engineValidate(): anchor.getTrustedCert() == null");
                }
            } else if (adaptableX509CertSelector == null || adaptableX509CertSelector.match(trustedCert)) {
                Debug debug4 = debug;
                if (debug4 != null) {
                    debug4.println("YES - try this trustedCert");
                    debug4.println("anchor.getTrustedCert().getSubjectX500Principal() = " + trustedCert.getSubjectX500Principal());
                }
            } else {
                Debug debug5 = debug;
                if (debug5 != null) {
                    debug5.println("NO - don't try this trustedCert");
                }
            }
            try {
                return validate(next, validatorParams);
            } catch (CertPathValidatorException e2) {
                e = e2;
            }
        }
        if (e != null) {
            throw e;
        }
        throw new CertPathValidatorException("Path does not chain with any of the trust anchors", (Throwable) null, (CertPath) null, -1, PKIXReason.NO_TRUST_ANCHOR);
    }

    private static PKIXCertPathValidatorResult validate(TrustAnchor trustAnchor, PKIX.ValidatorParams validatorParams) throws CertPathValidatorException {
        int size = validatorParams.certificates().size();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new AlgorithmChecker(trustAnchor));
        arrayList.add(new KeyChecker(size, validatorParams.targetCertConstraints()));
        arrayList.add(new ConstraintsChecker(size));
        PolicyChecker policyChecker = new PolicyChecker(validatorParams.initialPolicies(), size, validatorParams.explicitPolicyRequired(), validatorParams.policyMappingInhibited(), validatorParams.anyPolicyInhibited(), validatorParams.policyQualifiersRejected(), new PolicyNodeImpl((PolicyNodeImpl) null, "2.5.29.32.0", (Set<PolicyQualifierInfo>) null, false, Collections.singleton("2.5.29.32.0"), false));
        arrayList.add(policyChecker);
        boolean z = false;
        BasicChecker basicChecker = new BasicChecker(trustAnchor, validatorParams.date(), validatorParams.sigProvider(), false);
        arrayList.add(basicChecker);
        List<PKIXCertPathChecker> certPathCheckers = validatorParams.certPathCheckers();
        for (PKIXCertPathChecker next : certPathCheckers) {
            if (next instanceof PKIXRevocationChecker) {
                if (!z) {
                    if (next instanceof RevocationChecker) {
                        ((RevocationChecker) next).init(trustAnchor, validatorParams);
                    }
                    z = true;
                } else {
                    throw new CertPathValidatorException("Only one PKIXRevocationChecker can be specified");
                }
            }
        }
        if (validatorParams.revocationEnabled() && !z) {
            arrayList.add(new RevocationChecker(trustAnchor, validatorParams));
        }
        arrayList.addAll(certPathCheckers);
        PKIXMasterCertPathValidator.validate(validatorParams.certPath(), validatorParams.certificates(), arrayList);
        return new PKIXCertPathValidatorResult(trustAnchor, policyChecker.getPolicyTree(), basicChecker.getPublicKey());
    }
}
