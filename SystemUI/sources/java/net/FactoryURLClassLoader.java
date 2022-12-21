package java.net;

import java.security.AccessControlContext;

/* compiled from: URLClassLoader */
final class FactoryURLClassLoader extends URLClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    FactoryURLClassLoader(URL[] urlArr, ClassLoader classLoader, AccessControlContext accessControlContext) {
        super(urlArr, classLoader, accessControlContext);
    }

    FactoryURLClassLoader(URL[] urlArr, AccessControlContext accessControlContext) {
        super(urlArr, accessControlContext);
    }

    public final Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
        int lastIndexOf;
        SecurityManager securityManager = System.getSecurityManager();
        if (!(securityManager == null || (lastIndexOf = str.lastIndexOf(46)) == -1)) {
            securityManager.checkPackageAccess(str.substring(0, lastIndexOf));
        }
        return super.loadClass(str, z);
    }
}
