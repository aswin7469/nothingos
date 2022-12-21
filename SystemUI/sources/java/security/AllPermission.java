package java.security;

public final class AllPermission extends Permission {
    public String getActions() {
        return null;
    }

    public boolean implies(Permission permission) {
        return true;
    }

    public AllPermission() {
        super("");
    }

    public AllPermission(String str, String str2) {
        super("");
    }
}
