package sun.security.provider.certpath;

import java.p026io.IOException;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import sun.security.action.GetBooleanAction;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.x509.GeneralNameInterface;
import sun.security.x509.GeneralNames;
import sun.security.x509.GeneralSubtrees;
import sun.security.x509.NameConstraintsExtension;
import sun.security.x509.SubjectAlternativeNameExtension;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;

public abstract class Builder {
    static final boolean USE_AIA = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("com.sun.security.enableAIAcaIssuers"))).booleanValue();
    private static final Debug debug = Debug.getInstance("certpath");
    final PKIX.BuilderParams buildParams;
    private Set<String> matchingPolicies;
    final X509CertSelector targetCertConstraints;

    /* access modifiers changed from: package-private */
    public abstract void addCertToPath(X509Certificate x509Certificate, LinkedList<X509Certificate> linkedList);

    /* access modifiers changed from: package-private */
    public abstract Collection<X509Certificate> getMatchingCerts(State state, List<CertStore> list) throws CertStoreException, CertificateException, IOException;

    /* access modifiers changed from: package-private */
    public abstract boolean isPathCompleted(X509Certificate x509Certificate);

    /* access modifiers changed from: package-private */
    public abstract void removeFinalCertFromPath(LinkedList<X509Certificate> linkedList);

    /* access modifiers changed from: package-private */
    public abstract void verifyCert(X509Certificate x509Certificate, State state, List<X509Certificate> list) throws GeneralSecurityException;

    Builder(PKIX.BuilderParams builderParams) {
        this.buildParams = builderParams;
        this.targetCertConstraints = (X509CertSelector) builderParams.targetCertConstraints();
    }

    static int distance(GeneralNameInterface generalNameInterface, GeneralNameInterface generalNameInterface2, int i) {
        Debug debug2;
        int constrains = generalNameInterface.constrains(generalNameInterface2);
        if (constrains == -1) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("Builder.distance(): Names are different types");
            }
            return i;
        } else if (constrains == 0) {
            return 0;
        } else {
            if (constrains == 1 || constrains == 2) {
                return generalNameInterface2.subtreeDepth() - generalNameInterface.subtreeDepth();
            }
            if (constrains == 3 && (debug2 = debug) != null) {
                debug2.println("Builder.distance(): Names are same type but in different subtrees");
            }
            return i;
        }
    }

    static int hops(GeneralNameInterface generalNameInterface, GeneralNameInterface generalNameInterface2, int i) {
        int subtreeDepth;
        int subtreeDepth2;
        int constrains = generalNameInterface.constrains(generalNameInterface2);
        if (constrains == -1) {
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("Builder.hops(): Names are different types");
            }
            return i;
        } else if (constrains == 0) {
            return 0;
        } else {
            if (constrains == 1) {
                subtreeDepth = generalNameInterface2.subtreeDepth();
                subtreeDepth2 = generalNameInterface.subtreeDepth();
            } else if (constrains == 2) {
                subtreeDepth = generalNameInterface2.subtreeDepth();
                subtreeDepth2 = generalNameInterface.subtreeDepth();
            } else if (constrains != 3) {
                return i;
            } else {
                if (generalNameInterface.getType() != 4) {
                    Debug debug3 = debug;
                    if (debug3 != null) {
                        debug3.println("Builder.hops(): hopDistance not implemented for this name type");
                    }
                    return i;
                }
                X500Name x500Name = (X500Name) generalNameInterface;
                X500Name x500Name2 = (X500Name) generalNameInterface2;
                X500Name commonAncestor = x500Name.commonAncestor(x500Name2);
                if (commonAncestor == null) {
                    Debug debug4 = debug;
                    if (debug4 != null) {
                        debug4.println("Builder.hops(): Names are in different namespaces");
                    }
                    return i;
                }
                return (x500Name.subtreeDepth() + x500Name2.subtreeDepth()) - (commonAncestor.subtreeDepth() * 2);
            }
            return subtreeDepth - subtreeDepth2;
        }
    }

    static int targetDistance(NameConstraintsExtension nameConstraintsExtension, X509Certificate x509Certificate, GeneralNameInterface generalNameInterface) throws IOException {
        GeneralNames generalNames;
        if (nameConstraintsExtension == null || nameConstraintsExtension.verify(x509Certificate)) {
            try {
                X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
                if (X500Name.asX500Name(impl.getSubjectX500Principal()).equals(generalNameInterface)) {
                    return 0;
                }
                SubjectAlternativeNameExtension subjectAlternativeNameExtension = impl.getSubjectAlternativeNameExtension();
                if (!(subjectAlternativeNameExtension == null || (generalNames = subjectAlternativeNameExtension.get((String) SubjectAlternativeNameExtension.SUBJECT_NAME)) == null)) {
                    int size = generalNames.size();
                    for (int i = 0; i < size; i++) {
                        if (generalNames.get(i).getName().equals(generalNameInterface)) {
                            return 0;
                        }
                    }
                }
                NameConstraintsExtension nameConstraintsExtension2 = impl.getNameConstraintsExtension();
                if (nameConstraintsExtension2 == null) {
                    return -1;
                }
                if (nameConstraintsExtension != null) {
                    nameConstraintsExtension.merge(nameConstraintsExtension2);
                } else {
                    nameConstraintsExtension = (NameConstraintsExtension) nameConstraintsExtension2.clone();
                }
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("Builder.targetDistance() merged constraints: " + String.valueOf((Object) nameConstraintsExtension));
                }
                GeneralSubtrees generalSubtrees = nameConstraintsExtension.get(NameConstraintsExtension.PERMITTED_SUBTREES);
                GeneralSubtrees generalSubtrees2 = nameConstraintsExtension.get(NameConstraintsExtension.EXCLUDED_SUBTREES);
                if (generalSubtrees != null) {
                    generalSubtrees.reduce(generalSubtrees2);
                }
                if (debug2 != null) {
                    debug2.println("Builder.targetDistance() reduced constraints: " + generalSubtrees);
                }
                if (!nameConstraintsExtension.verify(generalNameInterface)) {
                    throw new IOException("New certificate not allowed to sign certificate for target");
                } else if (generalSubtrees == null) {
                    return -1;
                } else {
                    int size2 = generalSubtrees.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        int distance = distance(generalSubtrees.get(i2).getName().getName(), generalNameInterface, -1);
                        if (distance >= 0) {
                            return distance + 1;
                        }
                    }
                    return -1;
                }
            } catch (CertificateException e) {
                throw new IOException("Invalid certificate", e);
            }
        } else {
            throw new IOException("certificate does not satisfy existing name constraints");
        }
    }

    /* access modifiers changed from: package-private */
    public Set<String> getMatchingPolicies() {
        if (this.matchingPolicies != null) {
            Set<String> initialPolicies = this.buildParams.initialPolicies();
            if (initialPolicies.isEmpty() || initialPolicies.contains("2.5.29.32.0") || !this.buildParams.policyMappingInhibited()) {
                this.matchingPolicies = Collections.emptySet();
            } else {
                HashSet hashSet = new HashSet(initialPolicies);
                this.matchingPolicies = hashSet;
                hashSet.add("2.5.29.32.0");
            }
        }
        return this.matchingPolicies;
    }

    /* access modifiers changed from: package-private */
    public boolean addMatchingCerts(X509CertSelector x509CertSelector, Collection<CertStore> collection, Collection<X509Certificate> collection2, boolean z) {
        X509Certificate certificate = x509CertSelector.getCertificate();
        boolean z2 = false;
        if (certificate == null) {
            for (CertStore certificates : collection) {
                try {
                    for (Certificate certificate2 : certificates.getCertificates(x509CertSelector)) {
                        if (!X509CertImpl.isSelfSigned((X509Certificate) certificate2, this.buildParams.sigProvider()) && collection2.add((X509Certificate) certificate2)) {
                            z2 = true;
                        }
                    }
                    if (!z && z2) {
                        return true;
                    }
                } catch (CertStoreException e) {
                    Debug debug2 = debug;
                    if (debug2 != null) {
                        debug2.println("Builder.addMatchingCerts, non-fatal exception retrieving certs: " + e);
                        e.printStackTrace();
                    }
                }
            }
            return z2;
        } else if (!x509CertSelector.match(certificate) || X509CertImpl.isSelfSigned(certificate, this.buildParams.sigProvider())) {
            return false;
        } else {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("Builder.addMatchingCerts: adding target cert\n  SN: " + Debug.toHexString(certificate.getSerialNumber()) + "\n  Subject: " + certificate.getSubjectX500Principal() + "\n  Issuer: " + certificate.getIssuerX500Principal());
            }
            return collection2.add(certificate);
        }
    }
}
