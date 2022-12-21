package java.net;

import java.nio.ByteBuffer;
import java.p026io.Closeable;
import java.p026io.File;
import java.p026io.FilePermission;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.WeakHashMap;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import sun.misc.Resource;
import sun.misc.URLClassPath;
import sun.net.www.ParseUtil;
import sun.net.www.protocol.file.FileURLConnection;
import sun.security.util.SecurityConstants;
import sun.util.locale.LanguageTag;

public class URLClassLoader extends SecureClassLoader implements Closeable {
    /* access modifiers changed from: private */
    public final AccessControlContext acc;
    private WeakHashMap<Closeable, Void> closeables = new WeakHashMap<>();
    /* access modifiers changed from: private */
    public final URLClassPath ucp;

    public URLClassLoader(URL[] urlArr, ClassLoader classLoader) {
        super(classLoader);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        AccessControlContext context = AccessController.getContext();
        this.acc = context;
        this.ucp = new URLClassPath(urlArr, context);
    }

    URLClassLoader(URL[] urlArr, ClassLoader classLoader, AccessControlContext accessControlContext) {
        super(classLoader);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.acc = accessControlContext;
        this.ucp = new URLClassPath(urlArr, accessControlContext);
    }

    public URLClassLoader(URL[] urlArr) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        AccessControlContext context = AccessController.getContext();
        this.acc = context;
        this.ucp = new URLClassPath(urlArr, context);
    }

    URLClassLoader(URL[] urlArr, AccessControlContext accessControlContext) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        this.acc = accessControlContext;
        this.ucp = new URLClassPath(urlArr, accessControlContext);
    }

    public URLClassLoader(URL[] urlArr, ClassLoader classLoader, URLStreamHandlerFactory uRLStreamHandlerFactory) {
        super(classLoader);
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkCreateClassLoader();
        }
        AccessControlContext context = AccessController.getContext();
        this.acc = context;
        this.ucp = new URLClassPath(urlArr, uRLStreamHandlerFactory, context);
    }

    public InputStream getResourceAsStream(String str) {
        URL resource = getResource(str);
        if (resource == null) {
            return null;
        }
        try {
            URLConnection openConnection = resource.openConnection();
            InputStream inputStream = openConnection.getInputStream();
            if (openConnection instanceof JarURLConnection) {
                JarFile jarFile = ((JarURLConnection) openConnection).getJarFile();
                synchronized (this.closeables) {
                    if (!this.closeables.containsKey(jarFile)) {
                        this.closeables.put(jarFile, null);
                    }
                }
            } else if (openConnection instanceof FileURLConnection) {
                synchronized (this.closeables) {
                    this.closeables.put(inputStream, null);
                }
            }
            return inputStream;
        } catch (IOException unused) {
            return null;
        }
    }

    public void close() throws IOException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("closeClassLoader"));
        }
        List<IOException> closeLoaders = this.ucp.closeLoaders();
        synchronized (this.closeables) {
            for (Closeable close : this.closeables.keySet()) {
                try {
                    close.close();
                } catch (IOException e) {
                    closeLoaders.add(e);
                }
            }
            this.closeables.clear();
        }
        if (!closeLoaders.isEmpty()) {
            IOException remove = closeLoaders.remove(0);
            for (IOException addSuppressed : closeLoaders) {
                remove.addSuppressed(addSuppressed);
            }
            throw remove;
        }
    }

    /* access modifiers changed from: protected */
    public void addURL(URL url) {
        this.ucp.addURL(url);
    }

    public URL[] getURLs() {
        return this.ucp.getURLs();
    }

    /* access modifiers changed from: protected */
    public Class<?> findClass(final String str) throws ClassNotFoundException {
        try {
            Class<?> cls = (Class) AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
                public Class<?> run() throws ClassNotFoundException {
                    Resource resource = URLClassLoader.this.ucp.getResource(str.replace('.', '/').concat(".class"), false);
                    if (resource == null) {
                        return null;
                    }
                    try {
                        return URLClassLoader.this.defineClass(str, resource);
                    } catch (IOException e) {
                        throw new ClassNotFoundException(str, e);
                    }
                }
            }, this.acc);
            if (cls != null) {
                return cls;
            }
            throw new ClassNotFoundException(str);
        } catch (PrivilegedActionException e) {
            throw ((ClassNotFoundException) e.getException());
        }
    }

    private Package getAndVerifyPackage(String str, Manifest manifest, URL url) {
        Package packageR = getPackage(str);
        if (packageR != null) {
            if (packageR.isSealed()) {
                if (!packageR.isSealed(url)) {
                    throw new SecurityException("sealing violation: package " + str + " is sealed");
                }
            } else if (manifest != null && isSealed(str, manifest)) {
                throw new SecurityException("sealing violation: can't seal package " + str + ": already loaded");
            }
        }
        return packageR;
    }

    private void definePackageInternal(String str, Manifest manifest, URL url) {
        if (getAndVerifyPackage(str, manifest, url) != null) {
            return;
        }
        if (manifest != null) {
            try {
                definePackage(str, manifest, url);
            } catch (IllegalArgumentException unused) {
                if (getAndVerifyPackage(str, manifest, url) == null) {
                    throw new AssertionError((Object) "Cannot find package " + str);
                }
            }
        } else {
            definePackage(str, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null, (URL) null);
        }
    }

    /* access modifiers changed from: private */
    public Class<?> defineClass(String str, Resource resource) throws IOException {
        System.nanoTime();
        int lastIndexOf = str.lastIndexOf(46);
        URL codeSourceURL = resource.getCodeSourceURL();
        if (lastIndexOf != -1) {
            definePackageInternal(str.substring(0, lastIndexOf), resource.getManifest(), codeSourceURL);
        }
        ByteBuffer byteBuffer = resource.getByteBuffer();
        if (byteBuffer != null) {
            return defineClass(str, byteBuffer, new CodeSource(codeSourceURL, resource.getCodeSigners()));
        }
        byte[] bytes = resource.getBytes();
        CodeSource codeSource = new CodeSource(codeSourceURL, resource.getCodeSigners());
        return defineClass(str, bytes, 0, bytes.length, codeSource);
    }

    /* access modifiers changed from: protected */
    public Package definePackage(String str, Manifest manifest, URL url) throws IllegalArgumentException {
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        Attributes attributes = manifest.getAttributes(str.replace('.', '/').concat("/"));
        if (attributes != null) {
            str7 = attributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            str6 = attributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            str5 = attributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            str4 = attributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            str3 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            str2 = attributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            str8 = attributes.getValue(Attributes.Name.SEALED);
        } else {
            str8 = null;
            str7 = null;
            str6 = null;
            str5 = null;
            str4 = null;
            str3 = null;
            str2 = null;
        }
        Attributes mainAttributes = manifest.getMainAttributes();
        if (mainAttributes != null) {
            if (str7 == null) {
                str7 = mainAttributes.getValue(Attributes.Name.SPECIFICATION_TITLE);
            }
            if (str6 == null) {
                str6 = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VERSION);
            }
            if (str5 == null) {
                str5 = mainAttributes.getValue(Attributes.Name.SPECIFICATION_VENDOR);
            }
            if (str4 == null) {
                str4 = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE);
            }
            if (str3 == null) {
                str3 = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
            }
            if (str2 == null) {
                str2 = mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR);
            }
            if (str8 == null) {
                str8 = mainAttributes.getValue(Attributes.Name.SEALED);
            }
        }
        return definePackage(str, str7, str6, str5, str4, str3, str2, "true".equalsIgnoreCase(str8) ? url : null);
    }

    private boolean isSealed(String str, Manifest manifest) {
        Attributes mainAttributes;
        Attributes attributes = manifest.getAttributes(str.replace('.', '/').concat("/"));
        String value = attributes != null ? attributes.getValue(Attributes.Name.SEALED) : null;
        if (value == null && (mainAttributes = manifest.getMainAttributes()) != null) {
            value = mainAttributes.getValue(Attributes.Name.SEALED);
        }
        return "true".equalsIgnoreCase(value);
    }

    public URL findResource(final String str) {
        URL url = (URL) AccessController.doPrivileged(new PrivilegedAction<URL>() {
            public URL run() {
                return URLClassLoader.this.ucp.findResource(str, true);
            }
        }, this.acc);
        if (url != null) {
            return this.ucp.checkURL(url);
        }
        return null;
    }

    public Enumeration<URL> findResources(String str) throws IOException {
        final Enumeration<URL> findResources = this.ucp.findResources(str, true);
        return new Enumeration<URL>() {
            private URL url = null;

            private boolean next() {
                URL checkURL;
                if (this.url != null) {
                    return true;
                }
                do {
                    URL url2 = (URL) AccessController.doPrivileged(new PrivilegedAction<URL>() {
                        public URL run() {
                            if (!findResources.hasMoreElements()) {
                                return null;
                            }
                            return (URL) findResources.nextElement();
                        }
                    }, URLClassLoader.this.acc);
                    if (url2 == null) {
                        break;
                    }
                    checkURL = URLClassLoader.this.ucp.checkURL(url2);
                    this.url = checkURL;
                } while (checkURL == null);
                if (this.url != null) {
                    return true;
                }
                return false;
            }

            public URL nextElement() {
                if (next()) {
                    URL url2 = this.url;
                    this.url = null;
                    return url2;
                }
                throw new NoSuchElementException();
            }

            public boolean hasMoreElements() {
                return next();
            }
        };
    }

    /* access modifiers changed from: protected */
    public PermissionCollection getPermissions(CodeSource codeSource) {
        final Permission permission;
        URLConnection uRLConnection;
        PermissionCollection permissions = super.getPermissions(codeSource);
        URL location = codeSource.getLocation();
        try {
            uRLConnection = location.openConnection();
            permission = uRLConnection.getPermission();
        } catch (IOException unused) {
            uRLConnection = null;
            permission = null;
        }
        if (permission instanceof FilePermission) {
            String name = permission.getName();
            if (name.endsWith(File.separator)) {
                permission = new FilePermission(name + LanguageTag.SEP, "read");
            }
        } else if (permission != null || !location.getProtocol().equals("file")) {
            if (uRLConnection instanceof JarURLConnection) {
                location = ((JarURLConnection) uRLConnection).getJarFileURL();
            }
            String host = location.getHost();
            if (host != null && host.length() > 0) {
                permission = new SocketPermission(host, SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION);
            }
        } else {
            String decode = ParseUtil.decode(location.getFile().replace('/', File.separatorChar));
            if (decode.endsWith(File.separator)) {
                decode = decode + LanguageTag.SEP;
            }
            permission = new FilePermission(decode, "read");
        }
        if (permission != null) {
            final SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() throws SecurityException {
                        securityManager.checkPermission(permission);
                        return null;
                    }
                }, this.acc);
            }
            permissions.add(permission);
        }
        return permissions;
    }

    public static URLClassLoader newInstance(final URL[] urlArr, final ClassLoader classLoader) {
        final AccessControlContext context = AccessController.getContext();
        return (URLClassLoader) AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {
            public URLClassLoader run() {
                return new FactoryURLClassLoader(urlArr, classLoader, context);
            }
        });
    }

    public static URLClassLoader newInstance(final URL[] urlArr) {
        final AccessControlContext context = AccessController.getContext();
        return (URLClassLoader) AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {
            public URLClassLoader run() {
                return new FactoryURLClassLoader(urlArr, context);
            }
        });
    }

    static {
        ClassLoader.registerAsParallelCapable();
    }
}
