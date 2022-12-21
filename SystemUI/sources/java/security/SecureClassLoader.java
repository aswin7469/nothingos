package java.security;

import java.nio.ByteBuffer;
import java.util.HashMap;
import sun.security.util.Debug;

public class SecureClassLoader extends ClassLoader {
    private static final Debug debug = Debug.getInstance("scl");
    private final boolean initialized;
    private final HashMap<CodeSource, ProtectionDomain> pdcache = new HashMap<>(11);

    static {
        ClassLoader.registerAsParallelCapable();
    }

    protected SecureClassLoader(ClassLoader classLoader) {
        super(classLoader);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.initialized = true;
    }

    protected SecureClassLoader() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.initialized = true;
    }

    /* access modifiers changed from: protected */
    public final Class<?> defineClass(String str, byte[] bArr, int i, int i2, CodeSource codeSource) {
        return defineClass(str, bArr, i, i2, getProtectionDomain(codeSource));
    }

    /* access modifiers changed from: protected */
    public final Class<?> defineClass(String str, ByteBuffer byteBuffer, CodeSource codeSource) {
        return defineClass(str, byteBuffer, getProtectionDomain(codeSource));
    }

    /* access modifiers changed from: protected */
    public PermissionCollection getPermissions(CodeSource codeSource) {
        check();
        return new Permissions();
    }

    private ProtectionDomain getProtectionDomain(CodeSource codeSource) {
        ProtectionDomain protectionDomain;
        if (codeSource == null) {
            return null;
        }
        synchronized (this.pdcache) {
            protectionDomain = this.pdcache.get(codeSource);
            if (protectionDomain == null) {
                ProtectionDomain protectionDomain2 = new ProtectionDomain(codeSource, getPermissions(codeSource), this, (Principal[]) null);
                this.pdcache.put(codeSource, protectionDomain2);
                Debug debug2 = debug;
                if (debug2 != null) {
                    debug2.println(" getPermissions " + protectionDomain2);
                    debug2.println("");
                }
                protectionDomain = protectionDomain2;
            }
        }
        return protectionDomain;
    }

    private void check() {
        if (!this.initialized) {
            throw new SecurityException("ClassLoader object not initialized");
        }
    }
}
