package java.security;

import java.p026io.Serializable;

public abstract class BasicPermission extends Permission implements Serializable {
    public String getActions() {
        return "";
    }

    public boolean implies(Permission permission) {
        return true;
    }

    public BasicPermission(String str) {
        super("");
    }

    public BasicPermission(String str, String str2) {
        super("");
    }
}
