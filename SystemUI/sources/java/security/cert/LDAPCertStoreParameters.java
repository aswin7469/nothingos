package java.security.cert;

import android.net.ProxyInfo;
import com.android.systemui.navigationbar.NavigationBarInflaterView;

public class LDAPCertStoreParameters implements CertStoreParameters {
    private static final int LDAP_DEFAULT_PORT = 389;
    private int port;
    private String serverName;

    public LDAPCertStoreParameters(String str, int i) {
        str.getClass();
        this.serverName = str;
        this.port = i;
    }

    public LDAPCertStoreParameters(String str) {
        this(str, LDAP_DEFAULT_PORT);
    }

    public LDAPCertStoreParameters() {
        this(ProxyInfo.LOCAL_HOST, LDAP_DEFAULT_PORT);
    }

    public String getServerName() {
        return this.serverName;
    }

    public int getPort() {
        return this.port;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("LDAPCertStoreParameters: [\n");
        sb.append("  serverName: " + this.serverName + "\n");
        sb.append("  port: " + this.port + "\n");
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
