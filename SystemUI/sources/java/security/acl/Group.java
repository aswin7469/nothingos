package java.security.acl;

import java.security.Principal;
import java.util.Enumeration;

@Deprecated(forRemoval = true, since = "9")
public interface Group extends Principal {
    boolean addMember(Principal principal);

    boolean isMember(Principal principal);

    Enumeration<? extends Principal> members();

    boolean removeMember(Principal principal);
}
