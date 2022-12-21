package java.security.cert;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PKIXParameters implements CertPathParameters {
    private boolean anyPolicyInhibited = false;
    private List<PKIXCertPathChecker> certPathCheckers;
    private CertSelector certSelector;
    private List<CertStore> certStores;
    private Date date;
    private boolean explicitPolicyRequired = false;
    private boolean policyMappingInhibited = false;
    private boolean policyQualifiersRejected = true;
    private boolean revocationEnabled = true;
    private String sigProvider;
    private Set<String> unmodInitialPolicies;
    private Set<TrustAnchor> unmodTrustAnchors;

    public PKIXParameters(Set<TrustAnchor> set) throws InvalidAlgorithmParameterException {
        setTrustAnchors(set);
        this.unmodInitialPolicies = Collections.emptySet();
        this.certPathCheckers = new ArrayList();
        this.certStores = new ArrayList();
    }

    public PKIXParameters(KeyStore keyStore) throws KeyStoreException, InvalidAlgorithmParameterException {
        if (keyStore != null) {
            HashSet hashSet = new HashSet();
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String nextElement = aliases.nextElement();
                if (keyStore.isCertificateEntry(nextElement)) {
                    Certificate certificate = keyStore.getCertificate(nextElement);
                    if (certificate instanceof X509Certificate) {
                        hashSet.add(new TrustAnchor((X509Certificate) certificate, (byte[]) null));
                    }
                }
            }
            setTrustAnchors(hashSet);
            this.unmodInitialPolicies = Collections.emptySet();
            this.certPathCheckers = new ArrayList();
            this.certStores = new ArrayList();
            return;
        }
        throw new NullPointerException("the keystore parameter must be non-null");
    }

    public Set<TrustAnchor> getTrustAnchors() {
        return this.unmodTrustAnchors;
    }

    public void setTrustAnchors(Set<TrustAnchor> set) throws InvalidAlgorithmParameterException {
        if (set == null) {
            throw new NullPointerException("the trustAnchors parameters must be non-null");
        } else if (!set.isEmpty()) {
            for (TrustAnchor trustAnchor : set) {
                if (!(trustAnchor instanceof TrustAnchor)) {
                    throw new ClassCastException("all elements of set must be of type java.security.cert.TrustAnchor");
                }
            }
            this.unmodTrustAnchors = Collections.unmodifiableSet(new HashSet(set));
        } else {
            throw new InvalidAlgorithmParameterException("the trustAnchors parameter must be non-empty");
        }
    }

    public Set<String> getInitialPolicies() {
        return this.unmodInitialPolicies;
    }

    public void setInitialPolicies(Set<String> set) {
        if (set != null) {
            for (String str : set) {
                if (!(str instanceof String)) {
                    throw new ClassCastException("all elements of set must be of type java.lang.String");
                }
            }
            this.unmodInitialPolicies = Collections.unmodifiableSet(new HashSet(set));
            return;
        }
        this.unmodInitialPolicies = Collections.emptySet();
    }

    public void setCertStores(List<CertStore> list) {
        if (list == null) {
            this.certStores = new ArrayList();
            return;
        }
        for (CertStore certStore : list) {
            if (!(certStore instanceof CertStore)) {
                throw new ClassCastException("all elements of list must be of type java.security.cert.CertStore");
            }
        }
        this.certStores = new ArrayList(list);
    }

    public void addCertStore(CertStore certStore) {
        if (certStore != null) {
            this.certStores.add(certStore);
        }
    }

    public List<CertStore> getCertStores() {
        return Collections.unmodifiableList(new ArrayList(this.certStores));
    }

    public void setRevocationEnabled(boolean z) {
        this.revocationEnabled = z;
    }

    public boolean isRevocationEnabled() {
        return this.revocationEnabled;
    }

    public void setExplicitPolicyRequired(boolean z) {
        this.explicitPolicyRequired = z;
    }

    public boolean isExplicitPolicyRequired() {
        return this.explicitPolicyRequired;
    }

    public void setPolicyMappingInhibited(boolean z) {
        this.policyMappingInhibited = z;
    }

    public boolean isPolicyMappingInhibited() {
        return this.policyMappingInhibited;
    }

    public void setAnyPolicyInhibited(boolean z) {
        this.anyPolicyInhibited = z;
    }

    public boolean isAnyPolicyInhibited() {
        return this.anyPolicyInhibited;
    }

    public void setPolicyQualifiersRejected(boolean z) {
        this.policyQualifiersRejected = z;
    }

    public boolean getPolicyQualifiersRejected() {
        return this.policyQualifiersRejected;
    }

    public Date getDate() {
        Date date2 = this.date;
        if (date2 == null) {
            return null;
        }
        return (Date) date2.clone();
    }

    public void setDate(Date date2) {
        if (date2 != null) {
            this.date = (Date) date2.clone();
        }
    }

    public void setCertPathCheckers(List<PKIXCertPathChecker> list) {
        if (list != null) {
            ArrayList arrayList = new ArrayList();
            for (PKIXCertPathChecker clone : list) {
                arrayList.add((PKIXCertPathChecker) clone.clone());
            }
            this.certPathCheckers = arrayList;
            return;
        }
        this.certPathCheckers = new ArrayList();
    }

    public List<PKIXCertPathChecker> getCertPathCheckers() {
        ArrayList arrayList = new ArrayList();
        for (PKIXCertPathChecker clone : this.certPathCheckers) {
            arrayList.add((PKIXCertPathChecker) clone.clone());
        }
        return Collections.unmodifiableList(arrayList);
    }

    public void addCertPathChecker(PKIXCertPathChecker pKIXCertPathChecker) {
        if (pKIXCertPathChecker != null) {
            this.certPathCheckers.add((PKIXCertPathChecker) pKIXCertPathChecker.clone());
        }
    }

    public String getSigProvider() {
        return this.sigProvider;
    }

    public void setSigProvider(String str) {
        this.sigProvider = str;
    }

    public CertSelector getTargetCertConstraints() {
        CertSelector certSelector2 = this.certSelector;
        if (certSelector2 != null) {
            return (CertSelector) certSelector2.clone();
        }
        return null;
    }

    public void setTargetCertConstraints(CertSelector certSelector2) {
        if (certSelector2 != null) {
            this.certSelector = (CertSelector) certSelector2.clone();
        } else {
            this.certSelector = null;
        }
    }

    public Object clone() {
        try {
            PKIXParameters pKIXParameters = (PKIXParameters) super.clone();
            if (this.certStores != null) {
                pKIXParameters.certStores = new ArrayList(this.certStores);
            }
            if (this.certPathCheckers != null) {
                pKIXParameters.certPathCheckers = new ArrayList(this.certPathCheckers.size());
                for (PKIXCertPathChecker clone : this.certPathCheckers) {
                    pKIXParameters.certPathCheckers.add((PKIXCertPathChecker) clone.clone());
                }
            }
            return pKIXParameters;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[\n");
        if (this.unmodTrustAnchors != null) {
            sb.append("  Trust Anchors: " + this.unmodTrustAnchors.toString() + "\n");
        }
        Set<String> set = this.unmodInitialPolicies;
        if (set != null) {
            if (set.isEmpty()) {
                sb.append("  Initial Policy OIDs: any\n");
            } else {
                sb.append("  Initial Policy OIDs: [" + this.unmodInitialPolicies.toString() + "]\n");
            }
        }
        sb.append("  Validity Date: " + String.valueOf((Object) this.date) + "\n");
        sb.append("  Signature Provider: " + String.valueOf((Object) this.sigProvider) + "\n");
        sb.append("  Default Revocation Enabled: " + this.revocationEnabled + "\n");
        sb.append("  Explicit Policy Required: " + this.explicitPolicyRequired + "\n");
        sb.append("  Policy Mapping Inhibited: " + this.policyMappingInhibited + "\n");
        sb.append("  Any Policy Inhibited: " + this.anyPolicyInhibited + "\n");
        sb.append("  Policy Qualifiers Rejected: " + this.policyQualifiersRejected + "\n");
        sb.append("  Target Cert Constraints: " + String.valueOf((Object) this.certSelector) + "\n");
        if (this.certPathCheckers != null) {
            sb.append("  Certification Path Checkers: [" + this.certPathCheckers.toString() + "]\n");
        }
        if (this.certStores != null) {
            sb.append("  CertStores: [" + this.certStores.toString() + "]\n");
        }
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
