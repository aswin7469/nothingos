package java.security;

import java.p026io.Serializable;
import java.util.Enumeration;

public final class Permissions extends PermissionCollection implements Serializable {
    public void add(Permission permission) {
    }

    public Enumeration<Permission> elements() {
        return null;
    }

    public boolean implies(Permission permission) {
        return true;
    }
}