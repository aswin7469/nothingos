package sun.net.www.protocol.jar;

import java.net.SocketPermission;
import java.net.URL;
import java.net.URLConnection;
import java.p026io.FileNotFoundException;
import java.p026io.FilePermission;
import java.p026io.IOException;
import java.security.Permission;
import java.util.HashMap;
import java.util.jar.JarFile;
import sun.net.util.URLUtil;
import sun.net.www.protocol.jar.URLJarFile;
import sun.security.util.SecurityConstants;

class JarFileFactory implements URLJarFile.URLJarFileCloseController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final HashMap<String, JarFile> fileCache = new HashMap<>();
    private static final JarFileFactory instance = new JarFileFactory();
    private static final HashMap<JarFile, URL> urlCache = new HashMap<>();

    private JarFileFactory() {
    }

    public static JarFileFactory getInstance() {
        return instance;
    }

    /* access modifiers changed from: package-private */
    public URLConnection getConnection(JarFile jarFile) throws IOException {
        URL url;
        synchronized (instance) {
            url = urlCache.get(jarFile);
        }
        if (url != null) {
            return url.openConnection();
        }
        return null;
    }

    public JarFile get(URL url) throws IOException {
        return get(url, true);
    }

    /* access modifiers changed from: package-private */
    public JarFile get(URL url, boolean z) throws IOException {
        JarFile jarFile;
        if (z) {
            JarFileFactory jarFileFactory = instance;
            synchronized (jarFileFactory) {
                jarFile = getCachedJarFile(url);
            }
            if (jarFile == null) {
                jarFile = URLJarFile.getJarFile(url, this);
                synchronized (jarFileFactory) {
                    JarFile cachedJarFile = getCachedJarFile(url);
                    if (cachedJarFile == null) {
                        fileCache.put(URLUtil.urlNoFragString(url), jarFile);
                        urlCache.put(jarFile, url);
                    } else {
                        if (jarFile != null) {
                            jarFile.close();
                        }
                        jarFile = cachedJarFile;
                    }
                }
            }
        } else {
            jarFile = URLJarFile.getJarFile(url, this);
        }
        if (jarFile != null) {
            return jarFile;
        }
        throw new FileNotFoundException(url.toString());
    }

    public void close(JarFile jarFile) {
        synchronized (instance) {
            URL remove = urlCache.remove(jarFile);
            if (remove != null) {
                fileCache.remove(URLUtil.urlNoFragString(remove));
            }
        }
    }

    private JarFile getCachedJarFile(URL url) {
        Permission permission;
        SecurityManager securityManager;
        JarFile jarFile = fileCache.get(URLUtil.urlNoFragString(url));
        if (!(jarFile == null || (permission = getPermission(jarFile)) == null || (securityManager = System.getSecurityManager()) == null)) {
            try {
                securityManager.checkPermission(permission);
            } catch (SecurityException e) {
                if ((permission instanceof FilePermission) && permission.getActions().indexOf("read") != -1) {
                    securityManager.checkRead(permission.getName());
                } else if (!(permission instanceof SocketPermission) || permission.getActions().indexOf(SecurityConstants.SOCKET_CONNECT_ACTION) == -1) {
                    throw e;
                } else {
                    securityManager.checkConnect(url.getHost(), url.getPort());
                }
            }
        }
        return jarFile;
    }

    private Permission getPermission(JarFile jarFile) {
        try {
            URLConnection connection = getConnection(jarFile);
            if (connection != null) {
                return connection.getPermission();
            }
            return null;
        } catch (IOException unused) {
            return null;
        }
    }
}
