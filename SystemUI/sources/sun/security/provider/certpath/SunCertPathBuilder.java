package sun.security.provider.certpath;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.PublicKey;
import java.security.cert.CertPath;
import java.security.cert.CertPathBuilderException;
import java.security.cert.CertPathBuilderResult;
import java.security.cert.CertPathBuilderSpi;
import java.security.cert.CertPathChecker;
import java.security.cert.CertPathParameters;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertSelector;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.PolicyNode;
import java.security.cert.PolicyQualifierInfo;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.security.auth.x500.X500Principal;
import sun.security.provider.certpath.PKIX;
import sun.security.util.Debug;
import sun.security.x509.PKIXExtensions;

public final class SunCertPathBuilder extends CertPathBuilderSpi {
    private static final Debug debug = Debug.getInstance("certpath");
    private PKIX.BuilderParams buildParams;

    /* renamed from: cf */
    private CertificateFactory f917cf;
    private PublicKey finalPublicKey;
    private boolean pathCompleted = false;
    private PolicyNode policyTreeResult;
    private TrustAnchor trustAnchor;

    public SunCertPathBuilder() throws CertPathBuilderException {
        try {
            this.f917cf = CertificateFactory.getInstance("X.509");
        } catch (CertificateException e) {
            throw new CertPathBuilderException((Throwable) e);
        }
    }

    public CertPathChecker engineGetRevocationChecker() {
        return new RevocationChecker();
    }

    public CertPathBuilderResult engineBuild(CertPathParameters certPathParameters) throws CertPathBuilderException, InvalidAlgorithmParameterException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("SunCertPathBuilder.engineBuild(" + certPathParameters + NavigationBarInflaterView.KEY_CODE_END);
        }
        this.buildParams = PKIX.checkBuilderParams(certPathParameters);
        return build();
    }

    private PKIXCertPathBuilderResult build() throws CertPathBuilderException {
        ArrayList arrayList = new ArrayList();
        PKIXCertPathBuilderResult buildCertPath = buildCertPath(false, arrayList);
        if (buildCertPath == null) {
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("SunCertPathBuilder.engineBuild: 2nd pass; try building again searching all certstores");
            }
            arrayList.clear();
            buildCertPath = buildCertPath(true, arrayList);
            if (buildCertPath == null) {
                throw new SunCertPathBuilderException("unable to find valid certification path to requested target", new AdjacencyList(arrayList));
            }
        }
        return buildCertPath;
    }

    private PKIXCertPathBuilderResult buildCertPath(boolean z, List<List<Vertex>> list) throws CertPathBuilderException {
        this.pathCompleted = false;
        this.trustAnchor = null;
        this.finalPublicKey = null;
        this.policyTreeResult = null;
        LinkedList linkedList = new LinkedList();
        try {
            buildForward(list, linkedList, z);
            try {
                if (!this.pathCompleted) {
                    return null;
                }
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("SunCertPathBuilder.engineBuild() pathCompleted");
                }
                Collections.reverse(linkedList);
                return new SunCertPathBuilderResult(this.f917cf.generateCertPath((List<? extends Certificate>) linkedList), this.trustAnchor, this.policyTreeResult, this.finalPublicKey, new AdjacencyList(list));
            } catch (CertificateException e) {
                Debug debug3 = debug;
                if (debug3 != null) {
                    debug3.println("SunCertPathBuilder.engineBuild() exception in wrap-up");
                    e.printStackTrace();
                }
                throw new SunCertPathBuilderException("unable to find valid certification path to requested target", e, new AdjacencyList(list));
            }
        } catch (IOException | GeneralSecurityException e2) {
            Debug debug4 = debug;
            if (debug4 != null) {
                debug4.println("SunCertPathBuilder.engineBuild() exception in build");
                e2.printStackTrace();
            }
            throw new SunCertPathBuilderException("unable to find valid certification path to requested target", e2, new AdjacencyList(list));
        }
    }

    private void buildForward(List<List<Vertex>> list, LinkedList<X509Certificate> linkedList, boolean z) throws GeneralSecurityException, IOException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("SunCertPathBuilder.buildForward()...");
        }
        ForwardState forwardState = new ForwardState();
        forwardState.initState(this.buildParams.certPathCheckers());
        list.clear();
        list.add(new LinkedList());
        depthFirstSearchForward(this.buildParams.targetSubject(), forwardState, new ForwardBuilder(this.buildParams, z), list, linkedList);
    }

    private void depthFirstSearchForward(X500Principal x500Principal, ForwardState forwardState, ForwardBuilder forwardBuilder, List<List<Vertex>> list, LinkedList<X509Certificate> linkedList) throws GeneralSecurityException, IOException {
        Certificate certificate;
        ArrayList arrayList;
        Set<String> supportedExtensions;
        ArrayList arrayList2;
        Iterator it;
        ForwardBuilder forwardBuilder2 = forwardBuilder;
        List<List<Vertex>> list2 = list;
        LinkedList<X509Certificate> linkedList2 = linkedList;
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("SunCertPathBuilder.depthFirstSearchForward(" + x500Principal + ", " + forwardState.toString() + NavigationBarInflaterView.KEY_CODE_END);
        }
        List<Vertex> addVertices = addVertices(forwardBuilder2.getMatchingCerts(forwardState, this.buildParams.certStores()), list2);
        if (debug2 != null) {
            debug2.println("SunCertPathBuilder.depthFirstSearchForward(): certs.size=" + addVertices.size());
        }
        for (Vertex next : addVertices) {
            ForwardState forwardState2 = (ForwardState) forwardState.clone();
            X509Certificate certificate2 = next.getCertificate();
            try {
                forwardBuilder2.verifyCert(certificate2, forwardState2, linkedList2);
                if (forwardBuilder2.isPathCompleted(certificate2)) {
                    Debug debug3 = debug;
                    if (debug3 != null) {
                        debug3.println("SunCertPathBuilder.depthFirstSearchForward(): commencing final verification");
                    }
                    ArrayList arrayList3 = new ArrayList(linkedList2);
                    if (forwardBuilder2.trustAnchor.getTrustedCert() == null) {
                        arrayList3.add(0, certificate2);
                    }
                    PolicyNodeImpl policyNodeImpl = new PolicyNodeImpl((PolicyNodeImpl) null, "2.5.29.32.0", (Set<PolicyQualifierInfo>) null, false, Collections.singleton("2.5.29.32.0"), false);
                    ArrayList arrayList4 = new ArrayList();
                    PolicyChecker policyChecker = new PolicyChecker(this.buildParams.initialPolicies(), arrayList3.size(), this.buildParams.explicitPolicyRequired(), this.buildParams.policyMappingInhibited(), this.buildParams.anyPolicyInhibited(), this.buildParams.policyQualifiersRejected(), policyNodeImpl);
                    arrayList4.add(policyChecker);
                    arrayList4.add(new AlgorithmChecker(forwardBuilder2.trustAnchor));
                    BasicChecker basicChecker = null;
                    if (forwardState2.keyParamsNeeded()) {
                        PublicKey publicKey = certificate2.getPublicKey();
                        if (forwardBuilder2.trustAnchor.getTrustedCert() == null) {
                            publicKey = forwardBuilder2.trustAnchor.getCAPublicKey();
                            if (debug3 != null) {
                                debug3.println("SunCertPathBuilder.depthFirstSearchForward using buildParams public key: " + publicKey.toString());
                            }
                        }
                        basicChecker = new BasicChecker(new TrustAnchor(certificate2.getSubjectX500Principal(), publicKey, (byte[]) null), this.buildParams.date(), this.buildParams.sigProvider(), true);
                        arrayList4.add(basicChecker);
                    }
                    this.buildParams.setCertPath(this.f917cf.generateCertPath((List<? extends Certificate>) arrayList3));
                    List<PKIXCertPathChecker> certPathCheckers = this.buildParams.certPathCheckers();
                    Iterator<PKIXCertPathChecker> it2 = certPathCheckers.iterator();
                    boolean z = false;
                    while (it2.hasNext()) {
                        PKIXCertPathChecker next2 = it2.next();
                        Iterator<PKIXCertPathChecker> it3 = it2;
                        if (next2 instanceof PKIXRevocationChecker) {
                            if (!z) {
                                if (next2 instanceof RevocationChecker) {
                                    ((RevocationChecker) next2).init(forwardBuilder2.trustAnchor, this.buildParams);
                                }
                                z = true;
                            } else {
                                throw new CertPathValidatorException("Only one PKIXRevocationChecker can be specified");
                            }
                        }
                        it2 = it3;
                    }
                    if (this.buildParams.revocationEnabled() && !z) {
                        arrayList4.add(new RevocationChecker(forwardBuilder2.trustAnchor, this.buildParams));
                    }
                    arrayList4.addAll(certPathCheckers);
                    int i = 0;
                    while (i < arrayList3.size()) {
                        X509Certificate x509Certificate = (X509Certificate) arrayList3.get(i);
                        Debug debug4 = debug;
                        if (debug4 != null) {
                            arrayList = arrayList3;
                            debug4.println("current subject = " + x509Certificate.getSubjectX500Principal());
                        } else {
                            arrayList = arrayList3;
                        }
                        Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
                        if (criticalExtensionOIDs == null) {
                            criticalExtensionOIDs = Collections.emptySet();
                        }
                        Iterator it4 = arrayList4.iterator();
                        while (it4.hasNext()) {
                            PKIXCertPathChecker pKIXCertPathChecker = (PKIXCertPathChecker) it4.next();
                            if (!pKIXCertPathChecker.isForwardCheckingSupported()) {
                                it = it4;
                                if (i == 0) {
                                    pKIXCertPathChecker.init(false);
                                    if (pKIXCertPathChecker instanceof AlgorithmChecker) {
                                        arrayList2 = arrayList4;
                                        ((AlgorithmChecker) pKIXCertPathChecker).trySetTrustAnchor(forwardBuilder2.trustAnchor);
                                        pKIXCertPathChecker.check(x509Certificate, criticalExtensionOIDs);
                                    }
                                }
                                arrayList2 = arrayList4;
                                try {
                                    pKIXCertPathChecker.check(x509Certificate, criticalExtensionOIDs);
                                } catch (CertPathValidatorException e) {
                                    CertPathValidatorException certPathValidatorException = e;
                                    if (debug != null) {
                                        Debug debug5 = debug;
                                        debug5.println("SunCertPathBuilder.depthFirstSearchForward(): final verification failed: " + certPathValidatorException);
                                    }
                                    if (!this.buildParams.targetCertConstraints().match(x509Certificate) || certPathValidatorException.getReason() != CertPathValidatorException.BasicReason.REVOKED) {
                                        next.setThrowable(certPathValidatorException);
                                    } else {
                                        throw certPathValidatorException;
                                    }
                                }
                            } else {
                                it = it4;
                                arrayList2 = arrayList4;
                            }
                            it4 = it;
                            arrayList4 = arrayList2;
                        }
                        ArrayList arrayList5 = arrayList4;
                        for (PKIXCertPathChecker next3 : this.buildParams.certPathCheckers()) {
                            if (next3.isForwardCheckingSupported() && (supportedExtensions = next3.getSupportedExtensions()) != null) {
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
                                throw new CertPathValidatorException("unrecognized critical extension(s)", (Throwable) null, (CertPath) null, -1, PKIXReason.UNRECOGNIZED_CRIT_EXT);
                            }
                        }
                        i++;
                        arrayList3 = arrayList;
                        arrayList4 = arrayList5;
                    }
                    Debug debug6 = debug;
                    if (debug6 != null) {
                        debug6.println("SunCertPathBuilder.depthFirstSearchForward(): final verification succeeded - path completed!");
                    }
                    this.pathCompleted = true;
                    if (forwardBuilder2.trustAnchor.getTrustedCert() == null) {
                        forwardBuilder2.addCertToPath(certificate2, linkedList2);
                    }
                    this.trustAnchor = forwardBuilder2.trustAnchor;
                    if (basicChecker != null) {
                        this.finalPublicKey = basicChecker.getPublicKey();
                    } else {
                        if (linkedList.isEmpty()) {
                            certificate = forwardBuilder2.trustAnchor.getTrustedCert();
                        } else {
                            certificate = linkedList.getLast();
                        }
                        this.finalPublicKey = certificate.getPublicKey();
                    }
                    this.policyTreeResult = policyChecker.getPolicyTree();
                    return;
                }
                forwardBuilder2.addCertToPath(certificate2, linkedList2);
                forwardState2.updateState(certificate2);
                list2.add(new LinkedList());
                next.setIndex(list.size() - 1);
                depthFirstSearchForward(certificate2.getIssuerX500Principal(), forwardState2, forwardBuilder, list, linkedList);
                if (!this.pathCompleted) {
                    Debug debug7 = debug;
                    if (debug7 != null) {
                        debug7.println("SunCertPathBuilder.depthFirstSearchForward(): backtracking");
                    }
                    forwardBuilder2.removeFinalCertFromPath(linkedList2);
                } else {
                    return;
                }
            } catch (GeneralSecurityException e2) {
                GeneralSecurityException generalSecurityException = e2;
                if (debug != null) {
                    Debug debug8 = debug;
                    debug8.println("SunCertPathBuilder.depthFirstSearchForward(): validation failed: " + generalSecurityException);
                    generalSecurityException.printStackTrace();
                }
                next.setThrowable(generalSecurityException);
            }
        }
    }

    private static List<Vertex> addVertices(Collection<X509Certificate> collection, List<List<Vertex>> list) {
        List<Vertex> list2 = list.get(list.size() - 1);
        for (X509Certificate vertex : collection) {
            list2.add(new Vertex(vertex));
        }
        return list2;
    }

    private static boolean anchorIsTarget(TrustAnchor trustAnchor2, CertSelector certSelector) {
        X509Certificate trustedCert = trustAnchor2.getTrustedCert();
        if (trustedCert != null) {
            return certSelector.match(trustedCert);
        }
        return false;
    }
}
