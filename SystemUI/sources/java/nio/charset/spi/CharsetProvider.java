package java.nio.charset.spi;

import java.nio.charset.Charset;
import java.util.Iterator;

public abstract class CharsetProvider {
    public abstract Charset charsetForName(String str);

    public abstract Iterator<Charset> charsets();

    protected CharsetProvider() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("charsetProvider"));
        }
    }
}
