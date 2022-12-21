package sun.security.provider.certpath;

import java.security.InvalidAlgorithmParameterException;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathParameters;
import java.security.cert.CertSelector;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.interfaces.DSAPublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.util.Debug;

class PKIX {
    /* access modifiers changed from: private */
    public static final Debug debug = Debug.getInstance("certpath");

    private PKIX() {
    }

    static boolean isDSAPublicKeyWithoutParams(PublicKey publicKey) {
        return (publicKey instanceof DSAPublicKey) && ((DSAPublicKey) publicKey).getParams() == null;
    }

    static ValidatorParams checkParams(CertPath certPath, CertPathParameters certPathParameters) throws InvalidAlgorithmParameterException {
        if (certPathParameters instanceof PKIXParameters) {
            return new ValidatorParams(certPath, (PKIXParameters) certPathParameters);
        }
        throw new InvalidAlgorithmParameterException("inappropriate params, must be an instance of PKIXParameters");
    }

    static BuilderParams checkBuilderParams(CertPathParameters certPathParameters) throws InvalidAlgorithmParameterException {
        if (certPathParameters instanceof PKIXBuilderParameters) {
            return new BuilderParams((PKIXBuilderParameters) certPathParameters);
        }
        throw new InvalidAlgorithmParameterException("inappropriate params, must be an instance of PKIXBuilderParameters");
    }

    static class ValidatorParams {
        private Set<TrustAnchor> anchors;
        private CertPath certPath;
        private List<X509Certificate> certs;
        private List<PKIXCertPathChecker> checkers;
        private CertSelector constraints;
        private Date date;
        private boolean gotConstraints;
        private boolean gotDate;
        private final PKIXParameters params;
        private Set<String> policies;
        private List<CertStore> stores;

        ValidatorParams(CertPath certPath2, PKIXParameters pKIXParameters) throws InvalidAlgorithmParameterException {
            this(pKIXParameters);
            if (certPath2.getType().equals("X.509") || certPath2.getType().equals("X509")) {
                this.certPath = certPath2;
                return;
            }
            throw new InvalidAlgorithmParameterException("inappropriate CertPath type specified, must be X.509 or X509");
        }

        ValidatorParams(PKIXParameters pKIXParameters) throws InvalidAlgorithmParameterException {
            Set<TrustAnchor> trustAnchors = pKIXParameters.getTrustAnchors();
            this.anchors = trustAnchors;
            for (TrustAnchor nameConstraints : trustAnchors) {
                if (nameConstraints.getNameConstraints() != null) {
                    throw new InvalidAlgorithmParameterException("name constraints in trust anchor not supported");
                }
            }
            this.params = pKIXParameters;
        }

        /* access modifiers changed from: package-private */
        public CertPath certPath() {
            return this.certPath;
        }

        /* access modifiers changed from: package-private */
        public void setCertPath(CertPath certPath2) {
            this.certPath = certPath2;
        }

        /* access modifiers changed from: package-private */
        public List<X509Certificate> certificates() {
            if (this.certs == null) {
                if (this.certPath == null) {
                    this.certs = Collections.emptyList();
                } else {
                    ArrayList arrayList = new ArrayList(this.certPath.getCertificates());
                    Collections.reverse(arrayList);
                    this.certs = arrayList;
                }
            }
            return this.certs;
        }

        /* access modifiers changed from: package-private */
        public List<PKIXCertPathChecker> certPathCheckers() {
            if (this.checkers == null) {
                this.checkers = this.params.getCertPathCheckers();
            }
            return this.checkers;
        }

        /* access modifiers changed from: package-private */
        public List<CertStore> certStores() {
            if (this.stores == null) {
                this.stores = this.params.getCertStores();
            }
            return this.stores;
        }

        /* access modifiers changed from: package-private */
        public Date date() {
            if (!this.gotDate) {
                Date date2 = this.params.getDate();
                this.date = date2;
                if (date2 == null) {
                    this.date = new Date();
                }
                this.gotDate = true;
            }
            return this.date;
        }

        /* access modifiers changed from: package-private */
        public Set<String> initialPolicies() {
            if (this.policies == null) {
                this.policies = this.params.getInitialPolicies();
            }
            return this.policies;
        }

        /* access modifiers changed from: package-private */
        public CertSelector targetCertConstraints() {
            if (!this.gotConstraints) {
                this.constraints = this.params.getTargetCertConstraints();
                this.gotConstraints = true;
            }
            return this.constraints;
        }

        /* access modifiers changed from: package-private */
        public Set<TrustAnchor> trustAnchors() {
            return this.anchors;
        }

        /* access modifiers changed from: package-private */
        public boolean revocationEnabled() {
            return this.params.isRevocationEnabled();
        }

        /* access modifiers changed from: package-private */
        public boolean policyMappingInhibited() {
            return this.params.isPolicyMappingInhibited();
        }

        /* access modifiers changed from: package-private */
        public boolean explicitPolicyRequired() {
            return this.params.isExplicitPolicyRequired();
        }

        /* access modifiers changed from: package-private */
        public boolean policyQualifiersRejected() {
            return this.params.getPolicyQualifiersRejected();
        }

        /* access modifiers changed from: package-private */
        public String sigProvider() {
            return this.params.getSigProvider();
        }

        /* access modifiers changed from: package-private */
        public boolean anyPolicyInhibited() {
            return this.params.isAnyPolicyInhibited();
        }

        /* access modifiers changed from: package-private */
        public PKIXParameters getPKIXParameters() {
            return this.params;
        }
    }

    static class BuilderParams extends ValidatorParams {
        private PKIXBuilderParameters params;
        private List<CertStore> stores;
        private X500Principal targetSubject;

        BuilderParams(PKIXBuilderParameters pKIXBuilderParameters) throws InvalidAlgorithmParameterException {
            super(pKIXBuilderParameters);
            checkParams(pKIXBuilderParameters);
        }

        private void checkParams(PKIXBuilderParameters pKIXBuilderParameters) throws InvalidAlgorithmParameterException {
            if (targetCertConstraints() instanceof X509CertSelector) {
                this.params = pKIXBuilderParameters;
                this.targetSubject = getTargetSubject(certStores(), (X509CertSelector) targetCertConstraints());
                return;
            }
            throw new InvalidAlgorithmParameterException("the targetCertConstraints parameter must be an X509CertSelector");
        }

        /* access modifiers changed from: package-private */
        public List<CertStore> certStores() {
            if (this.stores == null) {
                ArrayList arrayList = new ArrayList(this.params.getCertStores());
                this.stores = arrayList;
                Collections.sort(arrayList, new CertStoreComparator());
            }
            return this.stores;
        }

        /* access modifiers changed from: package-private */
        public int maxPathLength() {
            return this.params.getMaxPathLength();
        }

        /* access modifiers changed from: package-private */
        public PKIXBuilderParameters params() {
            return this.params;
        }

        /* access modifiers changed from: package-private */
        public X500Principal targetSubject() {
            return this.targetSubject;
        }

        private static X500Principal getTargetSubject(List<CertStore> list, X509CertSelector x509CertSelector) throws InvalidAlgorithmParameterException {
            X500Principal subject = x509CertSelector.getSubject();
            if (subject != null) {
                return subject;
            }
            X509Certificate certificate = x509CertSelector.getCertificate();
            if (certificate != null) {
                subject = certificate.getSubjectX500Principal();
            }
            if (subject != null) {
                return subject;
            }
            for (CertStore certificates : list) {
                try {
                    Collection<? extends Certificate> certificates2 = certificates.getCertificates(x509CertSelector);
                    if (!certificates2.isEmpty()) {
                        return ((X509Certificate) certificates2.iterator().next()).getSubjectX500Principal();
                    }
                } catch (CertStoreException e) {
                    if (PKIX.debug != null) {
                        Debug r1 = PKIX.debug;
                        r1.println("BuilderParams.getTargetSubjectDN: non-fatal exception retrieving certs: " + e);
                        e.printStackTrace();
                    }
                }
            }
            throw new InvalidAlgorithmParameterException("Could not determine unique target subject");
        }
    }

    static class CertStoreTypeException extends CertStoreException {
        private static final long serialVersionUID = 7463352639238322556L;
        private final String type;

        CertStoreTypeException(String str, CertStoreException certStoreException) {
            super(certStoreException.getMessage(), certStoreException.getCause());
            this.type = str;
        }

        /* access modifiers changed from: package-private */
        public String getType() {
            return this.type;
        }
    }

    private static class CertStoreComparator implements Comparator<CertStore> {
        private CertStoreComparator() {
        }

        public int compare(CertStore certStore, CertStore certStore2) {
            return (certStore.getType().equals("Collection") || (certStore.getCertStoreParameters() instanceof CollectionCertStoreParameters)) ? -1 : 1;
        }
    }
}
