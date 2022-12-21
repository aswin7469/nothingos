package java.security.cert;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class PKIXRevocationChecker extends PKIXCertPathChecker {
    private List<Extension> ocspExtensions = Collections.emptyList();
    private URI ocspResponder;
    private X509Certificate ocspResponderCert;
    private Map<X509Certificate, byte[]> ocspResponses = Collections.emptyMap();
    private Set<Option> options = Collections.emptySet();

    public enum Option {
        ONLY_END_ENTITY,
        PREFER_CRLS,
        NO_FALLBACK,
        SOFT_FAIL
    }

    public abstract List<CertPathValidatorException> getSoftFailExceptions();

    protected PKIXRevocationChecker() {
    }

    public void setOcspResponder(URI uri) {
        this.ocspResponder = uri;
    }

    public URI getOcspResponder() {
        return this.ocspResponder;
    }

    public void setOcspResponderCert(X509Certificate x509Certificate) {
        this.ocspResponderCert = x509Certificate;
    }

    public X509Certificate getOcspResponderCert() {
        return this.ocspResponderCert;
    }

    public void setOcspExtensions(List<Extension> list) {
        List<Extension> list2;
        if (list == null) {
            list2 = Collections.emptyList();
        } else {
            list2 = new ArrayList<>(list);
        }
        this.ocspExtensions = list2;
    }

    public List<Extension> getOcspExtensions() {
        return Collections.unmodifiableList(this.ocspExtensions);
    }

    public void setOcspResponses(Map<X509Certificate, byte[]> map) {
        if (map == null) {
            this.ocspResponses = Collections.emptyMap();
            return;
        }
        HashMap hashMap = new HashMap(map.size());
        for (Map.Entry next : map.entrySet()) {
            hashMap.put((X509Certificate) next.getKey(), (byte[]) ((byte[]) next.getValue()).clone());
        }
        this.ocspResponses = hashMap;
    }

    public Map<X509Certificate, byte[]> getOcspResponses() {
        HashMap hashMap = new HashMap(this.ocspResponses.size());
        for (Map.Entry next : this.ocspResponses.entrySet()) {
            hashMap.put((X509Certificate) next.getKey(), (byte[]) ((byte[]) next.getValue()).clone());
        }
        return hashMap;
    }

    public void setOptions(Set<Option> set) {
        Set<Option> set2;
        if (set == null) {
            set2 = Collections.emptySet();
        } else {
            set2 = new HashSet<>(set);
        }
        this.options = set2;
    }

    public Set<Option> getOptions() {
        return Collections.unmodifiableSet(this.options);
    }

    public PKIXRevocationChecker clone() {
        PKIXRevocationChecker pKIXRevocationChecker = (PKIXRevocationChecker) super.clone();
        pKIXRevocationChecker.ocspExtensions = new ArrayList(this.ocspExtensions);
        HashMap hashMap = new HashMap(this.ocspResponses);
        pKIXRevocationChecker.ocspResponses = hashMap;
        for (Map.Entry entry : hashMap.entrySet()) {
            entry.setValue((byte[]) ((byte[]) entry.getValue()).clone());
        }
        pKIXRevocationChecker.options = new HashSet(this.options);
        return pKIXRevocationChecker;
    }
}
