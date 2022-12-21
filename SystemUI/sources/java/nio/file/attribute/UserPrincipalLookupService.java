package java.nio.file.attribute;

import java.p026io.IOException;

public abstract class UserPrincipalLookupService {
    public abstract GroupPrincipal lookupPrincipalByGroupName(String str) throws IOException;

    public abstract UserPrincipal lookupPrincipalByName(String str) throws IOException;

    protected UserPrincipalLookupService() {
    }
}
