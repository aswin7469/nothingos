package sun.security.provider.certpath;

import android.net.wifi.WifiEnterpriseConfig;
import java.security.cert.PolicyNode;
import java.security.cert.PolicyQualifierInfo;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

final class PolicyNodeImpl implements PolicyNode {
    private static final String ANY_POLICY = "2.5.29.32.0";
    private boolean isImmutable;
    private HashSet<PolicyNodeImpl> mChildren;
    private boolean mCriticalityIndicator;
    private int mDepth;
    private HashSet<String> mExpectedPolicySet;
    private boolean mOriginalExpectedPolicySet;
    private PolicyNodeImpl mParent;
    private HashSet<PolicyQualifierInfo> mQualifierSet;
    private String mValidPolicy;

    PolicyNodeImpl(PolicyNodeImpl policyNodeImpl, String str, Set<PolicyQualifierInfo> set, boolean z, Set<String> set2, boolean z2) {
        this.isImmutable = false;
        this.mParent = policyNodeImpl;
        this.mChildren = new HashSet<>();
        if (str != null) {
            this.mValidPolicy = str;
        } else {
            this.mValidPolicy = "";
        }
        if (set != null) {
            this.mQualifierSet = new HashSet<>(set);
        } else {
            this.mQualifierSet = new HashSet<>();
        }
        this.mCriticalityIndicator = z;
        if (set2 != null) {
            this.mExpectedPolicySet = new HashSet<>(set2);
        } else {
            this.mExpectedPolicySet = new HashSet<>();
        }
        this.mOriginalExpectedPolicySet = !z2;
        PolicyNodeImpl policyNodeImpl2 = this.mParent;
        if (policyNodeImpl2 != null) {
            this.mDepth = policyNodeImpl2.getDepth() + 1;
            this.mParent.addChild(this);
            return;
        }
        this.mDepth = 0;
    }

    PolicyNodeImpl(PolicyNodeImpl policyNodeImpl, PolicyNodeImpl policyNodeImpl2) {
        this(policyNodeImpl, policyNodeImpl2.mValidPolicy, policyNodeImpl2.mQualifierSet, policyNodeImpl2.mCriticalityIndicator, policyNodeImpl2.mExpectedPolicySet, false);
    }

    public PolicyNode getParent() {
        return this.mParent;
    }

    public Iterator<PolicyNodeImpl> getChildren() {
        return Collections.unmodifiableSet(this.mChildren).iterator();
    }

    public int getDepth() {
        return this.mDepth;
    }

    public String getValidPolicy() {
        return this.mValidPolicy;
    }

    public Set<PolicyQualifierInfo> getPolicyQualifiers() {
        return Collections.unmodifiableSet(this.mQualifierSet);
    }

    public Set<String> getExpectedPolicies() {
        return Collections.unmodifiableSet(this.mExpectedPolicySet);
    }

    public boolean isCritical() {
        return this.mCriticalityIndicator;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(asString());
        Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
        while (it.hasNext()) {
            sb.append((Object) it.next());
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public boolean isImmutable() {
        return this.isImmutable;
    }

    /* access modifiers changed from: package-private */
    public void setImmutable() {
        if (!this.isImmutable) {
            Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
            while (it.hasNext()) {
                it.next().setImmutable();
            }
            this.isImmutable = true;
        }
    }

    private void addChild(PolicyNodeImpl policyNodeImpl) {
        if (!this.isImmutable) {
            this.mChildren.add(policyNodeImpl);
            return;
        }
        throw new IllegalStateException("PolicyNode is immutable");
    }

    /* access modifiers changed from: package-private */
    public void addExpectedPolicy(String str) {
        if (!this.isImmutable) {
            if (this.mOriginalExpectedPolicySet) {
                this.mExpectedPolicySet.clear();
                this.mOriginalExpectedPolicySet = false;
            }
            this.mExpectedPolicySet.add(str);
            return;
        }
        throw new IllegalStateException("PolicyNode is immutable");
    }

    /* access modifiers changed from: package-private */
    public void prune(int i) {
        if (this.isImmutable) {
            throw new IllegalStateException("PolicyNode is immutable");
        } else if (this.mChildren.size() != 0) {
            Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
            while (it.hasNext()) {
                PolicyNodeImpl next = it.next();
                next.prune(i);
                if (next.mChildren.size() == 0 && i > this.mDepth + 1) {
                    it.remove();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void deleteChild(PolicyNode policyNode) {
        if (!this.isImmutable) {
            this.mChildren.remove(policyNode);
            return;
        }
        throw new IllegalStateException("PolicyNode is immutable");
    }

    /* access modifiers changed from: package-private */
    public PolicyNodeImpl copyTree() {
        return copyTree((PolicyNodeImpl) null);
    }

    private PolicyNodeImpl copyTree(PolicyNodeImpl policyNodeImpl) {
        PolicyNodeImpl policyNodeImpl2 = new PolicyNodeImpl(policyNodeImpl, this);
        Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
        while (it.hasNext()) {
            it.next().copyTree(policyNodeImpl2);
        }
        return policyNodeImpl2;
    }

    /* access modifiers changed from: package-private */
    public Set<PolicyNodeImpl> getPolicyNodes(int i) {
        HashSet hashSet = new HashSet();
        getPolicyNodes(i, hashSet);
        return hashSet;
    }

    private void getPolicyNodes(int i, Set<PolicyNodeImpl> set) {
        if (this.mDepth == i) {
            set.add(this);
            return;
        }
        Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
        while (it.hasNext()) {
            it.next().getPolicyNodes(i, set);
        }
    }

    /* access modifiers changed from: package-private */
    public Set<PolicyNodeImpl> getPolicyNodesExpected(int i, String str, boolean z) {
        if (str.equals(ANY_POLICY)) {
            return getPolicyNodes(i);
        }
        return getPolicyNodesExpectedHelper(i, str, z);
    }

    private Set<PolicyNodeImpl> getPolicyNodesExpectedHelper(int i, String str, boolean z) {
        HashSet hashSet = new HashSet();
        if (this.mDepth < i) {
            Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
            while (it.hasNext()) {
                hashSet.addAll(it.next().getPolicyNodesExpectedHelper(i, str, z));
            }
        } else if (z) {
            if (this.mExpectedPolicySet.contains(ANY_POLICY)) {
                hashSet.add(this);
            }
        } else if (this.mExpectedPolicySet.contains(str)) {
            hashSet.add(this);
        }
        return hashSet;
    }

    /* access modifiers changed from: package-private */
    public Set<PolicyNodeImpl> getPolicyNodesValid(int i, String str) {
        HashSet hashSet = new HashSet();
        if (this.mDepth < i) {
            Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
            while (it.hasNext()) {
                hashSet.addAll(it.next().getPolicyNodesValid(i, str));
            }
        } else if (this.mValidPolicy.equals(str)) {
            hashSet.add(this);
        }
        return hashSet;
    }

    private static String policyToString(String str) {
        return str.equals(ANY_POLICY) ? "anyPolicy" : str;
    }

    /* access modifiers changed from: package-private */
    public String asString() {
        if (this.mParent == null) {
            return "anyPolicy  ROOT\n";
        }
        StringBuilder sb = new StringBuilder();
        int depth = getDepth();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        sb.append(policyToString(getValidPolicy()));
        sb.append("  CRIT: ");
        sb.append(isCritical());
        sb.append("  EP: ");
        for (String policyToString : getExpectedPolicies()) {
            sb.append(policyToString(policyToString));
            sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        }
        sb.append(" (");
        sb.append(getDepth());
        sb.append(")\n");
        return sb.toString();
    }
}
