package java.net;

import java.p026io.Serializable;
import java.security.Permission;

public final class SocketPermission extends Permission implements Serializable {
    public String getActions() {
        return null;
    }

    public boolean implies(Permission permission) {
        return true;
    }

    public SocketPermission(String str, String str2) {
        super("");
    }
}
