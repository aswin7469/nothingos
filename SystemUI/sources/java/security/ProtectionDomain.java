package java.security;

public class ProtectionDomain {
    public final ClassLoader getClassLoader() {
        return null;
    }

    public final CodeSource getCodeSource() {
        return null;
    }

    public final PermissionCollection getPermissions() {
        return null;
    }

    public final Principal[] getPrincipals() {
        return null;
    }

    public boolean implies(Permission permission) {
        return true;
    }

    public ProtectionDomain(CodeSource codeSource, PermissionCollection permissionCollection) {
    }

    public ProtectionDomain(CodeSource codeSource, PermissionCollection permissionCollection, ClassLoader classLoader, Principal[] principalArr) {
    }
}
