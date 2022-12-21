package java.nio.file.attribute;

import java.p026io.IOException;

public interface FileOwnerAttributeView extends FileAttributeView {
    UserPrincipal getOwner() throws IOException;

    String name();

    void setOwner(UserPrincipal userPrincipal) throws IOException;
}
