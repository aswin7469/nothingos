package javax.net.ssl;

import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SSLParameters {
    private AlgorithmConstraints algorithmConstraints;
    private String[] applicationProtocols = new String[0];
    private String[] cipherSuites;
    private String identificationAlgorithm;
    private boolean needClientAuth;
    private boolean preferLocalCipherSuites;
    private String[] protocols;
    private Map<Integer, SNIMatcher> sniMatchers = null;
    private Map<Integer, SNIServerName> sniNames = null;
    private boolean wantClientAuth;

    public SSLParameters() {
    }

    public SSLParameters(String[] strArr) {
        setCipherSuites(strArr);
    }

    public SSLParameters(String[] strArr, String[] strArr2) {
        setCipherSuites(strArr);
        setProtocols(strArr2);
    }

    private static String[] clone(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        return (String[]) strArr.clone();
    }

    public String[] getCipherSuites() {
        return clone(this.cipherSuites);
    }

    public void setCipherSuites(String[] strArr) {
        this.cipherSuites = clone(strArr);
    }

    public String[] getProtocols() {
        return clone(this.protocols);
    }

    public void setProtocols(String[] strArr) {
        this.protocols = clone(strArr);
    }

    public boolean getWantClientAuth() {
        return this.wantClientAuth;
    }

    public void setWantClientAuth(boolean z) {
        this.wantClientAuth = z;
        this.needClientAuth = false;
    }

    public boolean getNeedClientAuth() {
        return this.needClientAuth;
    }

    public void setNeedClientAuth(boolean z) {
        this.wantClientAuth = false;
        this.needClientAuth = z;
    }

    public AlgorithmConstraints getAlgorithmConstraints() {
        return this.algorithmConstraints;
    }

    public void setAlgorithmConstraints(AlgorithmConstraints algorithmConstraints2) {
        this.algorithmConstraints = algorithmConstraints2;
    }

    public String getEndpointIdentificationAlgorithm() {
        return this.identificationAlgorithm;
    }

    public void setEndpointIdentificationAlgorithm(String str) {
        this.identificationAlgorithm = str;
    }

    public final void setServerNames(List<SNIServerName> list) {
        if (list == null) {
            this.sniNames = null;
        } else if (!list.isEmpty()) {
            this.sniNames = new LinkedHashMap(list.size());
            for (SNIServerName next : list) {
                if (this.sniNames.put(Integer.valueOf(next.getType()), next) != null) {
                    throw new IllegalArgumentException("Duplicated server name of type " + next.getType());
                }
            }
        } else {
            this.sniNames = Collections.emptyMap();
        }
    }

    public final List<SNIServerName> getServerNames() {
        Map<Integer, SNIServerName> map = this.sniNames;
        if (map == null) {
            return null;
        }
        if (!map.isEmpty()) {
            return Collections.unmodifiableList(new ArrayList(this.sniNames.values()));
        }
        return Collections.emptyList();
    }

    public final void setSNIMatchers(Collection<SNIMatcher> collection) {
        if (collection == null) {
            this.sniMatchers = null;
        } else if (!collection.isEmpty()) {
            this.sniMatchers = new HashMap(collection.size());
            for (SNIMatcher next : collection) {
                if (this.sniMatchers.put(Integer.valueOf(next.getType()), next) != null) {
                    throw new IllegalArgumentException("Duplicated server name of type " + next.getType());
                }
            }
        } else {
            this.sniMatchers = Collections.emptyMap();
        }
    }

    public final Collection<SNIMatcher> getSNIMatchers() {
        Map<Integer, SNIMatcher> map = this.sniMatchers;
        if (map == null) {
            return null;
        }
        if (!map.isEmpty()) {
            return Collections.unmodifiableList(new ArrayList(this.sniMatchers.values()));
        }
        return Collections.emptyList();
    }

    public final void setUseCipherSuitesOrder(boolean z) {
        this.preferLocalCipherSuites = z;
    }

    public final boolean getUseCipherSuitesOrder() {
        return this.preferLocalCipherSuites;
    }

    public String[] getApplicationProtocols() {
        return (String[]) this.applicationProtocols.clone();
    }

    public void setApplicationProtocols(String[] strArr) {
        if (strArr != null) {
            String[] strArr2 = (String[]) strArr.clone();
            for (String str : strArr2) {
                if (str == null || str.equals("")) {
                    throw new IllegalArgumentException("An element of protocols was null/empty");
                }
            }
            this.applicationProtocols = strArr2;
            return;
        }
        throw new IllegalArgumentException("protocols was null");
    }
}
