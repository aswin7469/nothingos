package java.security;

import java.p026io.Serializable;

public abstract class Permission implements Guard, Serializable {
    private String name;

    public void checkGuard(Object obj) throws SecurityException {
    }

    public abstract String getActions();

    public abstract boolean implies(Permission permission);

    public Permission(String str) {
        this.name = str;
    }

    public final String getName() {
        return this.name;
    }

    public PermissionCollection newPermissionCollection() {
        return new Permissions();
    }
}
