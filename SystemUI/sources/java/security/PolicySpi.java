package java.security;

public abstract class PolicySpi {
    /* access modifiers changed from: protected */
    public abstract boolean engineImplies(ProtectionDomain protectionDomain, Permission permission);

    /* access modifiers changed from: protected */
    public void engineRefresh() {
    }

    /* access modifiers changed from: protected */
    public PermissionCollection engineGetPermissions(CodeSource codeSource) {
        return Policy.UNSUPPORTED_EMPTY_COLLECTION;
    }

    /* access modifiers changed from: protected */
    public PermissionCollection engineGetPermissions(ProtectionDomain protectionDomain) {
        return Policy.UNSUPPORTED_EMPTY_COLLECTION;
    }
}
