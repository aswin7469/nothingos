package sun.security.provider.certpath;

import java.p026io.IOException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.PolicyNode;
import java.security.cert.PolicyQualifierInfo;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import sun.security.util.Debug;
import sun.security.x509.CertificatePoliciesExtension;
import sun.security.x509.CertificatePolicyMap;
import sun.security.x509.InhibitAnyPolicyExtension;
import sun.security.x509.PKIXExtensions;
import sun.security.x509.PolicyConstraintsExtension;
import sun.security.x509.PolicyInformation;
import sun.security.x509.PolicyMappingsExtension;
import sun.security.x509.X509CertImpl;

class PolicyChecker extends PKIXCertPathChecker {
    static final String ANY_POLICY = "2.5.29.32.0";
    private static final Debug debug = Debug.getInstance("certpath");
    private final boolean anyPolicyInhibited;
    private int certIndex;
    private final int certPathLen;
    private final boolean expPolicyRequired;
    private int explicitPolicy;
    private int inhibitAnyPolicy;
    private final Set<String> initPolicies;
    private final boolean polMappingInhibited;
    private int policyMapping;
    private final boolean rejectPolicyQualifiers;
    private PolicyNodeImpl rootNode;
    private Set<String> supportedExts;

    public boolean isForwardCheckingSupported() {
        return false;
    }

    PolicyChecker(Set<String> set, int i, boolean z, boolean z2, boolean z3, boolean z4, PolicyNodeImpl policyNodeImpl) {
        if (set.isEmpty()) {
            HashSet hashSet = new HashSet(1);
            this.initPolicies = hashSet;
            hashSet.add(ANY_POLICY);
        } else {
            this.initPolicies = new HashSet(set);
        }
        this.certPathLen = i;
        this.expPolicyRequired = z;
        this.polMappingInhibited = z2;
        this.anyPolicyInhibited = z3;
        this.rejectPolicyQualifiers = z4;
        this.rootNode = policyNodeImpl;
    }

    public void init(boolean z) throws CertPathValidatorException {
        if (!z) {
            this.certIndex = 1;
            int i = 0;
            this.explicitPolicy = this.expPolicyRequired ? 0 : this.certPathLen + 1;
            this.policyMapping = this.polMappingInhibited ? 0 : this.certPathLen + 1;
            if (!this.anyPolicyInhibited) {
                i = this.certPathLen + 1;
            }
            this.inhibitAnyPolicy = i;
            return;
        }
        throw new CertPathValidatorException("forward checking not supported");
    }

    public Set<String> getSupportedExtensions() {
        if (this.supportedExts == null) {
            HashSet hashSet = new HashSet(4);
            this.supportedExts = hashSet;
            hashSet.add(PKIXExtensions.CertificatePolicies_Id.toString());
            this.supportedExts.add(PKIXExtensions.PolicyMappings_Id.toString());
            this.supportedExts.add(PKIXExtensions.PolicyConstraints_Id.toString());
            this.supportedExts.add(PKIXExtensions.InhibitAnyPolicy_Id.toString());
            this.supportedExts = Collections.unmodifiableSet(this.supportedExts);
        }
        return this.supportedExts;
    }

    public void check(Certificate certificate, Collection<String> collection) throws CertPathValidatorException {
        checkPolicy((X509Certificate) certificate);
        if (collection != null && !collection.isEmpty()) {
            collection.remove(PKIXExtensions.CertificatePolicies_Id.toString());
            collection.remove(PKIXExtensions.PolicyMappings_Id.toString());
            collection.remove(PKIXExtensions.PolicyConstraints_Id.toString());
            collection.remove(PKIXExtensions.InhibitAnyPolicy_Id.toString());
        }
    }

    private void checkPolicy(X509Certificate x509Certificate) throws CertPathValidatorException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("PolicyChecker.checkPolicy() ---checking certificate policies...");
            debug2.println("PolicyChecker.checkPolicy() certIndex = " + this.certIndex);
            debug2.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: explicitPolicy = " + this.explicitPolicy);
            debug2.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: policyMapping = " + this.policyMapping);
            debug2.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: inhibitAnyPolicy = " + this.inhibitAnyPolicy);
            debug2.println("PolicyChecker.checkPolicy() BEFORE PROCESSING: policyTree = " + this.rootNode);
        }
        try {
            X509CertImpl impl = X509CertImpl.toImpl(x509Certificate);
            int i = this.certIndex;
            boolean z = i == this.certPathLen;
            this.rootNode = processPolicies(i, this.initPolicies, this.explicitPolicy, this.policyMapping, this.inhibitAnyPolicy, this.rejectPolicyQualifiers, this.rootNode, impl, z);
            if (!z) {
                this.explicitPolicy = mergeExplicitPolicy(this.explicitPolicy, impl, z);
                this.policyMapping = mergePolicyMapping(this.policyMapping, impl);
                this.inhibitAnyPolicy = mergeInhibitAnyPolicy(this.inhibitAnyPolicy, impl);
            }
            this.certIndex++;
            if (debug2 != null) {
                debug2.println("PolicyChecker.checkPolicy() AFTER PROCESSING: explicitPolicy = " + this.explicitPolicy);
                debug2.println("PolicyChecker.checkPolicy() AFTER PROCESSING: policyMapping = " + this.policyMapping);
                debug2.println("PolicyChecker.checkPolicy() AFTER PROCESSING: inhibitAnyPolicy = " + this.inhibitAnyPolicy);
                debug2.println("PolicyChecker.checkPolicy() AFTER PROCESSING: policyTree = " + this.rootNode);
                debug2.println("PolicyChecker.checkPolicy() certificate policies verified");
            }
        } catch (CertificateException e) {
            throw new CertPathValidatorException((Throwable) e);
        }
    }

    static int mergeExplicitPolicy(int i, X509CertImpl x509CertImpl, boolean z) throws CertPathValidatorException {
        if (i > 0 && !X509CertImpl.isSelfIssued(x509CertImpl)) {
            i--;
        }
        try {
            PolicyConstraintsExtension policyConstraintsExtension = x509CertImpl.getPolicyConstraintsExtension();
            if (policyConstraintsExtension == null) {
                return i;
            }
            int intValue = policyConstraintsExtension.get(PolicyConstraintsExtension.REQUIRE).intValue();
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("PolicyChecker.mergeExplicitPolicy() require Index from cert = " + intValue);
            }
            if (!z) {
                if (intValue == -1) {
                    return i;
                }
                if (i != -1 && intValue >= i) {
                    return i;
                }
            } else if (intValue != 0) {
                return i;
            }
            return intValue;
        } catch (IOException e) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("PolicyChecker.mergeExplicitPolicy unexpected exception");
                e.printStackTrace();
            }
            throw new CertPathValidatorException((Throwable) e);
        }
    }

    static int mergePolicyMapping(int i, X509CertImpl x509CertImpl) throws CertPathValidatorException {
        if (i > 0 && !X509CertImpl.isSelfIssued(x509CertImpl)) {
            i--;
        }
        try {
            PolicyConstraintsExtension policyConstraintsExtension = x509CertImpl.getPolicyConstraintsExtension();
            if (policyConstraintsExtension == null) {
                return i;
            }
            int intValue = policyConstraintsExtension.get(PolicyConstraintsExtension.INHIBIT).intValue();
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("PolicyChecker.mergePolicyMapping() inhibit Index from cert = " + intValue);
            }
            if (intValue != -1) {
                return (i == -1 || intValue < i) ? intValue : i;
            }
            return i;
        } catch (IOException e) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("PolicyChecker.mergePolicyMapping unexpected exception");
                e.printStackTrace();
            }
            throw new CertPathValidatorException((Throwable) e);
        }
    }

    static int mergeInhibitAnyPolicy(int i, X509CertImpl x509CertImpl) throws CertPathValidatorException {
        if (i > 0 && !X509CertImpl.isSelfIssued(x509CertImpl)) {
            i--;
        }
        try {
            InhibitAnyPolicyExtension inhibitAnyPolicyExtension = (InhibitAnyPolicyExtension) x509CertImpl.getExtension(PKIXExtensions.InhibitAnyPolicy_Id);
            if (inhibitAnyPolicyExtension == null) {
                return i;
            }
            int intValue = inhibitAnyPolicyExtension.get(InhibitAnyPolicyExtension.SKIP_CERTS).intValue();
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("PolicyChecker.mergeInhibitAnyPolicy() skipCerts Index from cert = " + intValue);
            }
            return (intValue == -1 || intValue >= i) ? i : intValue;
        } catch (IOException e) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("PolicyChecker.mergeInhibitAnyPolicy unexpected exception");
                e.printStackTrace();
            }
            throw new CertPathValidatorException((Throwable) e);
        }
    }

    static PolicyNodeImpl processPolicies(int i, Set<String> set, int i2, int i3, int i4, boolean z, PolicyNodeImpl policyNodeImpl, X509CertImpl x509CertImpl, boolean z2) throws CertPathValidatorException {
        PolicyNodeImpl policyNodeImpl2;
        Set set2;
        boolean z3;
        PolicyNodeImpl policyNodeImpl3;
        int i5 = i;
        Set<String> set3 = set;
        boolean z4 = z;
        boolean z5 = z2;
        Set hashSet = new HashSet();
        PolicyNodeImpl policyNodeImpl4 = null;
        if (policyNodeImpl == null) {
            policyNodeImpl2 = null;
        } else {
            policyNodeImpl2 = policyNodeImpl.copyTree();
        }
        CertificatePoliciesExtension certificatePoliciesExtension = x509CertImpl.getCertificatePoliciesExtension();
        boolean z6 = false;
        if (certificatePoliciesExtension != null && policyNodeImpl2 != null) {
            boolean isCritical = certificatePoliciesExtension.isCritical();
            Debug debug2 = debug;
            if (debug2 != null) {
                debug2.println("PolicyChecker.processPolicies() policiesCritical = " + isCritical);
            }
            try {
                List<PolicyInformation> list = certificatePoliciesExtension.get(CertificatePoliciesExtension.POLICIES);
                if (debug2 != null) {
                    debug2.println("PolicyChecker.processPolicies() rejectPolicyQualifiers = " + z4);
                }
                while (true) {
                    Set set4 = hashSet;
                    boolean z7 = z6;
                    for (PolicyInformation policyInformation : list) {
                        String objectIdentifier = policyInformation.getPolicyIdentifier().getIdentifier().toString();
                        if (objectIdentifier.equals(ANY_POLICY)) {
                            hashSet = policyInformation.getPolicyQualifiers();
                            z6 = true;
                        } else {
                            Debug debug3 = debug;
                            if (debug3 != null) {
                                debug3.println("PolicyChecker.processPolicies() processing policy: " + objectIdentifier);
                            }
                            Set<PolicyQualifierInfo> policyQualifiers = policyInformation.getPolicyQualifiers();
                            if (policyQualifiers.isEmpty() || !z4 || !isCritical) {
                                String str = objectIdentifier;
                                if (!processParents(i, isCritical, z, policyNodeImpl2, objectIdentifier, policyQualifiers, false)) {
                                    processParents(i, isCritical, z, policyNodeImpl2, str, policyQualifiers, true);
                                }
                            } else {
                                throw new CertPathValidatorException("critical policy qualifiers present in certificate", (Throwable) null, (CertPath) null, -1, PKIXReason.INVALID_POLICY);
                            }
                        }
                    }
                    if (z7 && (i4 > 0 || (!z5 && X509CertImpl.isSelfIssued(x509CertImpl)))) {
                        Debug debug4 = debug;
                        if (debug4 != null) {
                            debug4.println("PolicyChecker.processPolicies() processing policy: 2.5.29.32.0");
                        }
                        processParents(i, isCritical, z, policyNodeImpl2, ANY_POLICY, set4, true);
                    }
                    policyNodeImpl2.prune(i5);
                    if (policyNodeImpl2.getChildren().hasNext()) {
                        policyNodeImpl4 = policyNodeImpl2;
                    }
                    policyNodeImpl3 = policyNodeImpl4;
                    z3 = isCritical;
                    set2 = set4;
                }
            } catch (IOException e) {
                throw new CertPathValidatorException("Exception while retrieving policyOIDs", e);
            }
        } else if (certificatePoliciesExtension == null) {
            Debug debug5 = debug;
            if (debug5 != null) {
                debug5.println("PolicyChecker.processPolicies() no policies present in cert");
            }
            set2 = hashSet;
            z3 = false;
            policyNodeImpl3 = null;
        } else {
            set2 = hashSet;
            z3 = false;
            policyNodeImpl3 = policyNodeImpl2;
        }
        if (policyNodeImpl3 != null && !z5) {
            policyNodeImpl3 = processPolicyMappings(x509CertImpl, i, i3, policyNodeImpl3, z3, set2);
        }
        if (!(policyNodeImpl3 == null || set3.contains(ANY_POLICY) || certificatePoliciesExtension == null || (policyNodeImpl3 = removeInvalidNodes(policyNodeImpl3, i5, set3, certificatePoliciesExtension)) == null || !z5)) {
            policyNodeImpl3 = rewriteLeafNodes(i5, set3, policyNodeImpl3);
        }
        int i6 = i2;
        if (z5) {
            i6 = mergeExplicitPolicy(i6, x509CertImpl, z5);
        }
        if (i6 != 0 || policyNodeImpl3 != null) {
            return policyNodeImpl3;
        }
        throw new CertPathValidatorException("non-null policy tree required and policy tree is null", (Throwable) null, (CertPath) null, -1, PKIXReason.INVALID_POLICY);
    }

    private static PolicyNodeImpl rewriteLeafNodes(int i, Set<String> set, PolicyNodeImpl policyNodeImpl) {
        Set<PolicyNodeImpl> policyNodesValid = policyNodeImpl.getPolicyNodesValid(i, ANY_POLICY);
        if (policyNodesValid.isEmpty()) {
            return policyNodeImpl;
        }
        PolicyNodeImpl next = policyNodesValid.iterator().next();
        PolicyNodeImpl policyNodeImpl2 = (PolicyNodeImpl) next.getParent();
        policyNodeImpl2.deleteChild(next);
        HashSet<String> hashSet = new HashSet<>(set);
        for (PolicyNodeImpl validPolicy : policyNodeImpl.getPolicyNodes(i)) {
            hashSet.remove(validPolicy.getValidPolicy());
        }
        if (hashSet.isEmpty()) {
            policyNodeImpl.prune(i);
            if (!policyNodeImpl.getChildren().hasNext()) {
                return null;
            }
            return policyNodeImpl;
        }
        boolean isCritical = next.isCritical();
        Set<PolicyQualifierInfo> policyQualifiers = next.getPolicyQualifiers();
        for (String str : hashSet) {
            new PolicyNodeImpl(policyNodeImpl2, str, policyQualifiers, isCritical, Collections.singleton(str), false);
        }
        return policyNodeImpl;
    }

    private static boolean processParents(int i, boolean z, boolean z2, PolicyNodeImpl policyNodeImpl, String str, Set<PolicyQualifierInfo> set, boolean z3) throws CertPathValidatorException {
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("PolicyChecker.processParents(): matchAny = " + z3);
        }
        boolean z4 = false;
        for (PolicyNodeImpl next : policyNodeImpl.getPolicyNodesExpected(i - 1, str, z3)) {
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("PolicyChecker.processParents() found parent:\n" + next.asString());
            }
            next.getValidPolicy();
            if (str.equals(ANY_POLICY)) {
                for (String next2 : next.getExpectedPolicies()) {
                    Iterator<PolicyNodeImpl> children = next.getChildren();
                    while (true) {
                        if (!children.hasNext()) {
                            HashSet hashSet = new HashSet();
                            hashSet.add(next2);
                            new PolicyNodeImpl(next, next2, set, z, hashSet, false);
                            break;
                        }
                        String validPolicy = children.next().getValidPolicy();
                        if (next2.equals(validPolicy)) {
                            Debug debug4 = debug;
                            if (debug4 != null) {
                                debug4.println(validPolicy + " in parent's expected policy set already appears in child node");
                            }
                        }
                    }
                }
            } else {
                HashSet hashSet2 = new HashSet();
                hashSet2.add(str);
                new PolicyNodeImpl(next, str, set, z, hashSet2, false);
            }
            z4 = true;
        }
        return z4;
    }

    private static PolicyNodeImpl processPolicyMappings(X509CertImpl x509CertImpl, int i, int i2, PolicyNodeImpl policyNodeImpl, boolean z, Set<PolicyQualifierInfo> set) throws CertPathValidatorException {
        int i3 = i;
        int i4 = i2;
        PolicyNodeImpl policyNodeImpl2 = policyNodeImpl;
        PolicyMappingsExtension policyMappingsExtension = x509CertImpl.getPolicyMappingsExtension();
        if (policyMappingsExtension == null) {
            return policyNodeImpl2;
        }
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("PolicyChecker.processPolicyMappings() inside policyMapping check");
        }
        try {
            boolean z2 = false;
            for (CertificatePolicyMap certificatePolicyMap : policyMappingsExtension.get(PolicyMappingsExtension.MAP)) {
                String objectIdentifier = certificatePolicyMap.getIssuerIdentifier().getIdentifier().toString();
                String objectIdentifier2 = certificatePolicyMap.getSubjectIdentifier().getIdentifier().toString();
                Debug debug3 = debug;
                if (debug3 != null) {
                    debug3.println("PolicyChecker.processPolicyMappings() issuerDomain = " + objectIdentifier);
                    debug3.println("PolicyChecker.processPolicyMappings() subjectDomain = " + objectIdentifier2);
                }
                if (objectIdentifier.equals(ANY_POLICY)) {
                    throw new CertPathValidatorException("encountered an issuerDomainPolicy of ANY_POLICY", (Throwable) null, (CertPath) null, -1, PKIXReason.INVALID_POLICY);
                } else if (!objectIdentifier2.equals(ANY_POLICY)) {
                    Set<PolicyNodeImpl> policyNodesValid = policyNodeImpl2.getPolicyNodesValid(i3, objectIdentifier);
                    if (!policyNodesValid.isEmpty()) {
                        for (PolicyNodeImpl next : policyNodesValid) {
                            if (i4 > 0 || i4 == -1) {
                                next.addExpectedPolicy(objectIdentifier2);
                            } else if (i4 == 0) {
                                PolicyNodeImpl policyNodeImpl3 = (PolicyNodeImpl) next.getParent();
                                Debug debug4 = debug;
                                if (debug4 != null) {
                                    debug4.println("PolicyChecker.processPolicyMappings() before deleting: policy tree = " + policyNodeImpl2);
                                }
                                policyNodeImpl3.deleteChild(next);
                                if (debug4 != null) {
                                    debug4.println("PolicyChecker.processPolicyMappings() after deleting: policy tree = " + policyNodeImpl2);
                                }
                                z2 = true;
                            }
                        }
                    } else if (i4 > 0 || i4 == -1) {
                        for (PolicyNodeImpl parent : policyNodeImpl2.getPolicyNodesValid(i3, ANY_POLICY)) {
                            HashSet hashSet = new HashSet();
                            hashSet.add(objectIdentifier2);
                            new PolicyNodeImpl((PolicyNodeImpl) parent.getParent(), objectIdentifier, set, z, hashSet, true);
                        }
                    }
                } else {
                    throw new CertPathValidatorException("encountered a subjectDomainPolicy of ANY_POLICY", (Throwable) null, (CertPath) null, -1, PKIXReason.INVALID_POLICY);
                }
            }
            if (z2) {
                policyNodeImpl2.prune(i3);
                if (!policyNodeImpl.getChildren().hasNext()) {
                    Debug debug5 = debug;
                    if (debug5 != null) {
                        debug5.println("setting rootNode to null");
                    }
                    return null;
                }
            }
            return policyNodeImpl2;
        } catch (IOException e) {
            Debug debug6 = debug;
            if (debug6 != null) {
                debug6.println("PolicyChecker.processPolicyMappings() mapping exception");
                e.printStackTrace();
            }
            throw new CertPathValidatorException("Exception while checking mapping", e);
        }
    }

    private static PolicyNodeImpl removeInvalidNodes(PolicyNodeImpl policyNodeImpl, int i, Set<String> set, CertificatePoliciesExtension certificatePoliciesExtension) throws CertPathValidatorException {
        try {
            boolean z = false;
            for (PolicyInformation policyIdentifier : certificatePoliciesExtension.get(CertificatePoliciesExtension.POLICIES)) {
                String objectIdentifier = policyIdentifier.getPolicyIdentifier().getIdentifier().toString();
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println("PolicyChecker.processPolicies() processing policy second time: " + objectIdentifier);
                }
                for (PolicyNodeImpl next : policyNodeImpl.getPolicyNodesValid(i, objectIdentifier)) {
                    PolicyNodeImpl policyNodeImpl2 = (PolicyNodeImpl) next.getParent();
                    if (policyNodeImpl2.getValidPolicy().equals(ANY_POLICY) && !set.contains(objectIdentifier) && !objectIdentifier.equals(ANY_POLICY)) {
                        Debug debug3 = debug;
                        if (debug3 != null) {
                            debug3.println("PolicyChecker.processPolicies() before deleting: policy tree = " + policyNodeImpl);
                        }
                        policyNodeImpl2.deleteChild(next);
                        if (debug3 != null) {
                            debug3.println("PolicyChecker.processPolicies() after deleting: policy tree = " + policyNodeImpl);
                        }
                        z = true;
                    }
                }
            }
            if (!z) {
                return policyNodeImpl;
            }
            policyNodeImpl.prune(i);
            if (!policyNodeImpl.getChildren().hasNext()) {
                return null;
            }
            return policyNodeImpl;
        } catch (IOException e) {
            throw new CertPathValidatorException("Exception while retrieving policyOIDs", e);
        }
    }

    /* access modifiers changed from: package-private */
    public PolicyNode getPolicyTree() {
        PolicyNodeImpl policyNodeImpl = this.rootNode;
        if (policyNodeImpl == null) {
            return null;
        }
        PolicyNodeImpl copyTree = policyNodeImpl.copyTree();
        copyTree.setImmutable();
        return copyTree;
    }
}
