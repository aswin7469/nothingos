package sun.security.provider.certpath;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertStore;
import java.security.cert.CertStoreException;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.x509.AccessDescription;
import sun.security.x509.AuthorityInfoAccessExtension;
import sun.security.x509.AuthorityKeyIdentifierExtension;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.X500Name;
import sun.security.x509.X509CertImpl;

class ForwardBuilder extends Builder {
    /* access modifiers changed from: private */
    public static final Debug debug = Debug.getInstance("certpath");
    private AdaptableX509CertSelector caSelector;
    private X509CertSelector caTargetSelector;
    private X509CertSelector eeSelector;
    private boolean searchAllCertStores = true;
    TrustAnchor trustAnchor;
    private final Set<TrustAnchor> trustAnchors;
    private final Set<X509Certificate> trustedCerts;
    private final Set<X500Principal> trustedSubjectDNs;

    ForwardBuilder(PKIX.BuilderParams builderParams, boolean z) {
        super(builderParams);
        Set<TrustAnchor> trustAnchors2 = builderParams.trustAnchors();
        this.trustAnchors = trustAnchors2;
        this.trustedCerts = new HashSet(trustAnchors2.size());
        this.trustedSubjectDNs = new HashSet(trustAnchors2.size());
        for (TrustAnchor next : trustAnchors2) {
            X509Certificate trustedCert = next.getTrustedCert();
            if (trustedCert != null) {
                this.trustedCerts.add(trustedCert);
                this.trustedSubjectDNs.add(trustedCert.getSubjectX500Principal());
            } else {
                this.trustedSubjectDNs.add(next.getCA());
            }
        }
        this.searchAllCertStores = z;
    }

    /* access modifiers changed from: package-private */
    public Collection<X509Certificate> getMatchingCerts(State state, List<CertStore> list) throws CertStoreException, CertificateException, IOException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("ForwardBuilder.getMatchingCerts()...");
        }
        ForwardState forwardState = (ForwardState) state;
        TreeSet treeSet = new TreeSet(new PKIXCertComparator(this.trustedSubjectDNs, forwardState.cert));
        if (forwardState.isInitial()) {
            getMatchingEECerts(forwardState, list, treeSet);
        }
        getMatchingCACerts(forwardState, list, treeSet);
        return treeSet;
    }

    private void getMatchingEECerts(ForwardState forwardState, List<CertStore> list, Collection<X509Certificate> collection) throws IOException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("ForwardBuilder.getMatchingEECerts()...");
        }
        if (this.eeSelector == null) {
            X509CertSelector x509CertSelector = (X509CertSelector) this.targetCertConstraints.clone();
            this.eeSelector = x509CertSelector;
            x509CertSelector.setCertificateValid(this.buildParams.date());
            if (this.buildParams.explicitPolicyRequired()) {
                this.eeSelector.setPolicy(getMatchingPolicies());
            }
            this.eeSelector.setBasicConstraints(-2);
        }
        addMatchingCerts(this.eeSelector, list, collection, this.searchAllCertStores);
    }

    private void getMatchingCACerts(ForwardState forwardState, List<CertStore> list, Collection<X509Certificate> collection) throws IOException {
        X509CertSelector x509CertSelector;
        AuthorityInfoAccessExtension authorityInfoAccessExtension;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("ForwardBuilder.getMatchingCACerts()...");
        }
        int size = collection.size();
        if (!forwardState.isInitial()) {
            if (this.caSelector == null) {
                this.caSelector = new AdaptableX509CertSelector();
                if (this.buildParams.explicitPolicyRequired()) {
                    this.caSelector.setPolicy(getMatchingPolicies());
                }
            }
            this.caSelector.setSubject(forwardState.issuerDN);
            CertPathHelper.setPathToNames(this.caSelector, forwardState.subjectNamesTraversed);
            this.caSelector.setValidityPeriod(forwardState.cert.getNotBefore(), forwardState.cert.getNotAfter());
            x509CertSelector = this.caSelector;
        } else if (this.targetCertConstraints.getBasicConstraints() != -2) {
            if (debug2 != null) {
                debug2.println("ForwardBuilder.getMatchingCACerts(): the target is a CA");
            }
            if (this.caTargetSelector == null) {
                this.caTargetSelector = (X509CertSelector) this.targetCertConstraints.clone();
                if (this.buildParams.explicitPolicyRequired()) {
                    this.caTargetSelector.setPolicy(getMatchingPolicies());
                }
            }
            x509CertSelector = this.caTargetSelector;
        } else {
            return;
        }
        x509CertSelector.setBasicConstraints(-1);
        for (X509Certificate next : this.trustedCerts) {
            if (x509CertSelector.match(next)) {
                Debug debug3 = debug;
                if (debug3 != null) {
                    debug3.println("ForwardBuilder.getMatchingCACerts: found matching trust anchor.\n  SN: " + Debug.toHexString(next.getSerialNumber()) + "\n  Subject: " + next.getSubjectX500Principal() + "\n  Issuer: " + next.getIssuerX500Principal());
                }
                if (collection.add(next) && !this.searchAllCertStores) {
                    return;
                }
            }
        }
        x509CertSelector.setCertificateValid(this.buildParams.date());
        x509CertSelector.setBasicConstraints(forwardState.traversedCACerts);
        if ((!forwardState.isInitial() && this.buildParams.maxPathLength() != -1 && this.buildParams.maxPathLength() <= forwardState.traversedCACerts) || !addMatchingCerts(x509CertSelector, list, collection, this.searchAllCertStores) || this.searchAllCertStores) {
            if (!forwardState.isInitial() && Builder.USE_AIA && (authorityInfoAccessExtension = forwardState.cert.getAuthorityInfoAccessExtension()) != null) {
                getCerts(authorityInfoAccessExtension, collection);
            }
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("ForwardBuilder.getMatchingCACerts: found " + (collection.size() - size) + " CA certs");
            }
        }
    }

    private boolean getCerts(AuthorityInfoAccessExtension authorityInfoAccessExtension, Collection<X509Certificate> collection) {
        boolean z = false;
        if (!Builder.USE_AIA) {
            return false;
        }
        List<AccessDescription> accessDescriptions = authorityInfoAccessExtension.getAccessDescriptions();
        if (accessDescriptions != null && !accessDescriptions.isEmpty()) {
            for (AccessDescription instance : accessDescriptions) {
                CertStore instance2 = URICertStore.getInstance(instance);
                if (instance2 != null) {
                    try {
                        if (collection.addAll(instance2.getCertificates(this.caSelector))) {
                            z = true;
                            if (!this.searchAllCertStores) {
                                return true;
                            }
                        } else {
                            continue;
                        }
                    } catch (CertStoreException e) {
                        Debug debug2 = debug;
                        if (debug2 != null) {
                            debug2.println("exception getting certs from CertStore:");
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return z;
    }

    static class PKIXCertComparator implements Comparator<X509Certificate> {
        static final String METHOD_NME = "PKIXCertComparator.compare()";
        private final X509CertSelector certSkidSelector;
        private final Set<X500Principal> trustedSubjectDNs;

        PKIXCertComparator(Set<X500Principal> set, X509CertImpl x509CertImpl) throws IOException {
            this.trustedSubjectDNs = set;
            this.certSkidSelector = getSelector(x509CertImpl);
        }

        private X509CertSelector getSelector(X509CertImpl x509CertImpl) throws IOException {
            AuthorityKeyIdentifierExtension authorityKeyIdentifierExtension;
            byte[] encodedKeyIdentifier;
            if (x509CertImpl == null || (authorityKeyIdentifierExtension = x509CertImpl.getAuthorityKeyIdentifierExtension()) == null || (encodedKeyIdentifier = authorityKeyIdentifierExtension.getEncodedKeyIdentifier()) == null) {
                return null;
            }
            X509CertSelector x509CertSelector = new X509CertSelector();
            x509CertSelector.setSubjectKeyIdentifier(encodedKeyIdentifier);
            return x509CertSelector;
        }

        public int compare(X509Certificate x509Certificate, X509Certificate x509Certificate2) {
            if (x509Certificate.equals(x509Certificate2)) {
                return 0;
            }
            X509CertSelector x509CertSelector = this.certSkidSelector;
            if (x509CertSelector != null) {
                if (x509CertSelector.match(x509Certificate)) {
                    return -1;
                }
                if (this.certSkidSelector.match(x509Certificate2)) {
                    return 1;
                }
            }
            X500Principal issuerX500Principal = x509Certificate.getIssuerX500Principal();
            X500Principal issuerX500Principal2 = x509Certificate2.getIssuerX500Principal();
            X500Name asX500Name = X500Name.asX500Name(issuerX500Principal);
            X500Name asX500Name2 = X500Name.asX500Name(issuerX500Principal2);
            if (ForwardBuilder.debug != null) {
                Debug r6 = ForwardBuilder.debug;
                r6.println("PKIXCertComparator.compare() o1 Issuer:  " + issuerX500Principal);
                Debug r62 = ForwardBuilder.debug;
                r62.println("PKIXCertComparator.compare() o2 Issuer:  " + issuerX500Principal2);
            }
            if (ForwardBuilder.debug != null) {
                ForwardBuilder.debug.println("PKIXCertComparator.compare() MATCH TRUSTED SUBJECT TEST...");
            }
            boolean contains = this.trustedSubjectDNs.contains(issuerX500Principal);
            boolean contains2 = this.trustedSubjectDNs.contains(issuerX500Principal2);
            if (ForwardBuilder.debug != null) {
                Debug r63 = ForwardBuilder.debug;
                r63.println("PKIXCertComparator.compare() m1: " + contains);
                Debug r64 = ForwardBuilder.debug;
                r64.println("PKIXCertComparator.compare() m2: " + contains2);
            }
            if ((contains && contains2) || contains) {
                return -1;
            }
            if (contains2) {
                return 1;
            }
            if (ForwardBuilder.debug != null) {
                ForwardBuilder.debug.println("PKIXCertComparator.compare() NAMING DESCENDANT TEST...");
            }
            for (X500Principal asX500Name3 : this.trustedSubjectDNs) {
                X500Name asX500Name4 = X500Name.asX500Name(asX500Name3);
                int distance = Builder.distance(asX500Name4, asX500Name, -1);
                int distance2 = Builder.distance(asX500Name4, asX500Name2, -1);
                if (ForwardBuilder.debug != null) {
                    Debug r9 = ForwardBuilder.debug;
                    r9.println("PKIXCertComparator.compare() distanceTto1: " + distance);
                    Debug r7 = ForwardBuilder.debug;
                    r7.println("PKIXCertComparator.compare() distanceTto2: " + distance2);
                }
                if (distance <= 0) {
                    if (distance2 > 0) {
                    }
                }
                if (distance == distance2) {
                    return -1;
                }
                if (distance <= 0 || distance2 > 0) {
                    return ((distance > 0 || distance2 <= 0) && distance < distance2) ? -1 : 1;
                }
                return -1;
            }
            if (ForwardBuilder.debug != null) {
                ForwardBuilder.debug.println("PKIXCertComparator.compare() NAMING ANCESTOR TEST...");
            }
            for (X500Principal asX500Name5 : this.trustedSubjectDNs) {
                X500Name asX500Name6 = X500Name.asX500Name(asX500Name5);
                int distance3 = Builder.distance(asX500Name6, asX500Name, Integer.MAX_VALUE);
                int distance4 = Builder.distance(asX500Name6, asX500Name2, Integer.MAX_VALUE);
                if (ForwardBuilder.debug != null) {
                    Debug r8 = ForwardBuilder.debug;
                    r8.println("PKIXCertComparator.compare() distanceTto1: " + distance3);
                    Debug r82 = ForwardBuilder.debug;
                    r82.println("PKIXCertComparator.compare() distanceTto2: " + distance4);
                }
                if (distance3 >= 0) {
                    if (distance4 < 0) {
                    }
                }
                if (distance3 == distance4) {
                    return -1;
                }
                if (distance3 >= 0 || distance4 < 0) {
                    return ((distance3 < 0 || distance4 >= 0) && distance3 > distance4) ? -1 : 1;
                }
                return -1;
            }
            if (ForwardBuilder.debug != null) {
                ForwardBuilder.debug.println("PKIXCertComparator.compare() SAME NAMESPACE AS TRUSTED TEST...");
            }
            for (X500Principal asX500Name7 : this.trustedSubjectDNs) {
                X500Name asX500Name8 = X500Name.asX500Name(asX500Name7);
                X500Name commonAncestor = asX500Name8.commonAncestor(asX500Name);
                X500Name commonAncestor2 = asX500Name8.commonAncestor(asX500Name2);
                if (ForwardBuilder.debug != null) {
                    Debug r72 = ForwardBuilder.debug;
                    r72.println("PKIXCertComparator.compare() tAo1: " + String.valueOf((Object) commonAncestor));
                    Debug r73 = ForwardBuilder.debug;
                    r73.println("PKIXCertComparator.compare() tAo2: " + String.valueOf((Object) commonAncestor2));
                }
                if (commonAncestor != null || commonAncestor2 != null) {
                    if (commonAncestor == null || commonAncestor2 == null) {
                        return commonAncestor == null ? 1 : -1;
                    }
                    int hops = Builder.hops(asX500Name8, asX500Name, Integer.MAX_VALUE);
                    int hops2 = Builder.hops(asX500Name8, asX500Name2, Integer.MAX_VALUE);
                    if (ForwardBuilder.debug != null) {
                        Debug r65 = ForwardBuilder.debug;
                        r65.println("PKIXCertComparator.compare() hopsTto1: " + hops);
                        Debug r66 = ForwardBuilder.debug;
                        r66.println("PKIXCertComparator.compare() hopsTto2: " + hops2);
                    }
                    if (hops != hops2) {
                        return hops > hops2 ? 1 : -1;
                    }
                }
            }
            if (ForwardBuilder.debug != null) {
                ForwardBuilder.debug.println("PKIXCertComparator.compare() CERT ISSUER/SUBJECT COMPARISON TEST...");
            }
            X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
            X500Principal subjectX500Principal2 = x509Certificate2.getSubjectX500Principal();
            X500Name asX500Name9 = X500Name.asX500Name(subjectX500Principal);
            X500Name asX500Name10 = X500Name.asX500Name(subjectX500Principal2);
            if (ForwardBuilder.debug != null) {
                Debug r3 = ForwardBuilder.debug;
                r3.println("PKIXCertComparator.compare() o1 Subject: " + subjectX500Principal);
                Debug r11 = ForwardBuilder.debug;
                r11.println("PKIXCertComparator.compare() o2 Subject: " + subjectX500Principal2);
            }
            int distance5 = Builder.distance(asX500Name9, asX500Name, Integer.MAX_VALUE);
            int distance6 = Builder.distance(asX500Name10, asX500Name2, Integer.MAX_VALUE);
            if (ForwardBuilder.debug != null) {
                Debug r13 = ForwardBuilder.debug;
                r13.println("PKIXCertComparator.compare() distanceStoI1: " + distance5);
                Debug r132 = ForwardBuilder.debug;
                r132.println("PKIXCertComparator.compare() distanceStoI2: " + distance6);
            }
            if (distance6 > distance5) {
                return -1;
            }
            if (distance6 < distance5) {
                return 1;
            }
            if (ForwardBuilder.debug != null) {
                ForwardBuilder.debug.println("PKIXCertComparator.compare() no tests matched; RETURN 0");
            }
            return -1;
        }
    }

    /* access modifiers changed from: package-private */
    public void verifyCert(X509Certificate x509Certificate, State state, List<X509Certificate> list) throws GeneralSecurityException {
        Set<String> supportedExtensions;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("ForwardBuilder.verifyCert(SN: " + Debug.toHexString(x509Certificate.getSerialNumber()) + "\n  Issuer: " + x509Certificate.getIssuerX500Principal() + ")\n  Subject: " + x509Certificate.getSubjectX500Principal() + NavigationBarInflaterView.KEY_CODE_END);
        }
        ForwardState forwardState = (ForwardState) state;
        if (list != null) {
            for (X509Certificate equals : list) {
                if (x509Certificate.equals(equals)) {
                    Debug debug3 = debug;
                    if (debug3 != null) {
                        debug3.println("loop detected!!");
                    }
                    throw new CertPathValidatorException("loop detected");
                }
            }
        }
        boolean contains = this.trustedCerts.contains(x509Certificate);
        if (!contains) {
            Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
            if (criticalExtensionOIDs == null) {
                criticalExtensionOIDs = Collections.emptySet();
            }
            Iterator<PKIXCertPathChecker> it = forwardState.forwardCheckers.iterator();
            while (it.hasNext()) {
                it.next().check(x509Certificate, criticalExtensionOIDs);
            }
            for (PKIXCertPathChecker next : this.buildParams.certPathCheckers()) {
                if (!next.isForwardCheckingSupported() && (supportedExtensions = next.getSupportedExtensions()) != null) {
                    criticalExtensionOIDs.removeAll(supportedExtensions);
                }
            }
            if (!criticalExtensionOIDs.isEmpty()) {
                criticalExtensionOIDs.remove(PKIXExtensions.BasicConstraints_Id.toString());
                criticalExtensionOIDs.remove(PKIXExtensions.NameConstraints_Id.toString());
                criticalExtensionOIDs.remove(PKIXExtensions.CertificatePolicies_Id.toString());
                criticalExtensionOIDs.remove(PKIXExtensions.PolicyMappings_Id.toString());
                criticalExtensionOIDs.remove(PKIXExtensions.PolicyConstraints_Id.toString());
                criticalExtensionOIDs.remove(PKIXExtensions.InhibitAnyPolicy_Id.toString());
                criticalExtensionOIDs.remove(PKIXExtensions.SubjectAlternativeName_Id.toString());
                criticalExtensionOIDs.remove(PKIXExtensions.KeyUsage_Id.toString());
                criticalExtensionOIDs.remove(PKIXExtensions.ExtendedKeyUsage_Id.toString());
                if (!criticalExtensionOIDs.isEmpty()) {
                    throw new CertPathValidatorException("Unrecognized critical extension(s)", (Throwable) null, (CertPath) null, -1, PKIXReason.UNRECOGNIZED_CRIT_EXT);
                }
            }
        }
        if (!forwardState.isInitial()) {
            if (!contains) {
                if (x509Certificate.getBasicConstraints() != -1) {
                    KeyChecker.verifyCAKeyUsage(x509Certificate);
                } else {
                    throw new CertificateException("cert is NOT a CA cert");
                }
            }
            if (forwardState.keyParamsNeeded()) {
                return;
            }
            if (this.buildParams.sigProvider() != null) {
                forwardState.cert.verify(x509Certificate.getPublicKey(), this.buildParams.sigProvider());
            } else {
                forwardState.cert.verify(x509Certificate.getPublicKey());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isPathCompleted(X509Certificate x509Certificate) {
        ArrayList<TrustAnchor> arrayList = new ArrayList<>();
        for (TrustAnchor next : this.trustAnchors) {
            if (next.getTrustedCert() == null) {
                X500Principal ca = next.getCA();
                PublicKey cAPublicKey = next.getCAPublicKey();
                if (ca == null || cAPublicKey == null || !ca.equals(x509Certificate.getSubjectX500Principal()) || !cAPublicKey.equals(x509Certificate.getPublicKey())) {
                    arrayList.add(next);
                } else {
                    this.trustAnchor = next;
                    return true;
                }
            } else if (x509Certificate.equals(next.getTrustedCert())) {
                this.trustAnchor = next;
                return true;
            }
        }
        for (TrustAnchor trustAnchor2 : arrayList) {
            X500Principal ca2 = trustAnchor2.getCA();
            PublicKey cAPublicKey2 = trustAnchor2.getCAPublicKey();
            if (ca2 != null && ca2.equals(x509Certificate.getIssuerX500Principal()) && !PKIX.isDSAPublicKeyWithoutParams(cAPublicKey2)) {
                try {
                    if (this.buildParams.sigProvider() != null) {
                        x509Certificate.verify(cAPublicKey2, this.buildParams.sigProvider());
                    } else {
                        x509Certificate.verify(cAPublicKey2);
                    }
                    this.trustAnchor = trustAnchor2;
                    return true;
                } catch (InvalidKeyException unused) {
                    Debug debug2 = debug;
                    if (debug2 != null) {
                        debug2.println("ForwardBuilder.isPathCompleted() invalid DSA key found");
                    }
                } catch (GeneralSecurityException e) {
                    Debug debug3 = debug;
                    if (debug3 != null) {
                        debug3.println("ForwardBuilder.isPathCompleted() unexpected exception");
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public void addCertToPath(X509Certificate x509Certificate, LinkedList<X509Certificate> linkedList) {
        linkedList.addFirst(x509Certificate);
    }

    /* access modifiers changed from: package-private */
    public void removeFinalCertFromPath(LinkedList<X509Certificate> linkedList) {
        linkedList.removeFirst();
    }
}
