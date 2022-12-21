package java.security;

public final class AccessControlContext {
    public void checkPermission(Permission permission) throws AccessControlException {
    }

    public DomainCombiner getDomainCombiner() {
        return null;
    }

    public AccessControlContext(ProtectionDomain[] protectionDomainArr) {
    }

    public AccessControlContext(AccessControlContext accessControlContext, DomainCombiner domainCombiner) {
    }
}
