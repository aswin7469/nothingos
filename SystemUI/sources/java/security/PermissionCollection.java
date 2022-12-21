package java.security;

import java.p026io.Serializable;
import java.util.Enumeration;

public abstract class PermissionCollection implements Serializable {
    public abstract void add(Permission permission);

    public abstract Enumeration<Permission> elements();

    public abstract boolean implies(Permission permission);

    public boolean isReadOnly() {
        return true;
    }

    public void setReadOnly() {
    }
}
