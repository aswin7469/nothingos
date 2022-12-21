package java.security;

import java.p026io.Serializable;
import java.security.cert.Certificate;

public final class UnresolvedPermission extends Permission implements Serializable {
    public String getActions() {
        return null;
    }

    public String getUnresolvedActions() {
        return null;
    }

    public Certificate[] getUnresolvedCerts() {
        return null;
    }

    public String getUnresolvedName() {
        return null;
    }

    public String getUnresolvedType() {
        return null;
    }

    public boolean implies(Permission permission) {
        return false;
    }

    public UnresolvedPermission(String str, String str2, String str3, Certificate[] certificateArr) {
        super("");
    }
}
