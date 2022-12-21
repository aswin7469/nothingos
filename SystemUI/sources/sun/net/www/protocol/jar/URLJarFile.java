package sun.net.www.protocol.jar;

import android.net.ProxyInfo;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.p026io.File;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import sun.net.www.ParseUtil;

public class URLJarFile extends JarFile {
    private static int BUF_SIZE = 2048;
    private URLJarFileCloseController closeController;
    private Attributes superAttr;
    /* access modifiers changed from: private */
    public Map<String, Attributes> superEntries;
    private Manifest superMan;

    public interface URLJarFileCloseController {
        void close(JarFile jarFile);
    }

    static JarFile getJarFile(URL url) throws IOException {
        return getJarFile(url, (URLJarFileCloseController) null);
    }

    static JarFile getJarFile(URL url, URLJarFileCloseController uRLJarFileCloseController) throws IOException {
        if (isFileURL(url)) {
            return new URLJarFile(url, uRLJarFileCloseController);
        }
        return retrieve(url, uRLJarFileCloseController);
    }

    public URLJarFile(File file) throws IOException {
        this(file, (URLJarFileCloseController) null);
    }

    public URLJarFile(File file, URLJarFileCloseController uRLJarFileCloseController) throws IOException {
        super(file, true, 5);
        this.closeController = uRLJarFileCloseController;
    }

    private URLJarFile(URL url, URLJarFileCloseController uRLJarFileCloseController) throws IOException {
        super(ParseUtil.decode(url.getFile()));
        this.closeController = uRLJarFileCloseController;
    }

    private static boolean isFileURL(URL url) {
        if (!url.getProtocol().equalsIgnoreCase("file")) {
            return false;
        }
        String host = url.getHost();
        return host == null || host.equals("") || host.equals("~") || host.equalsIgnoreCase(ProxyInfo.LOCAL_HOST);
    }

    /* access modifiers changed from: protected */
    public void finalize() throws IOException {
        close();
    }

    public ZipEntry getEntry(String str) {
        ZipEntry entry = super.getEntry(str);
        if (entry == null) {
            return null;
        }
        if (entry instanceof JarEntry) {
            return new URLJarFileEntry((JarEntry) entry);
        }
        throw new InternalError(super.getClass() + " returned unexpected entry type " + entry.getClass());
    }

    public Manifest getManifest() throws IOException {
        if (!isSuperMan()) {
            return null;
        }
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().putAll((Map) this.superAttr.clone());
        if (this.superEntries != null) {
            Map<String, Attributes> entries = manifest.getEntries();
            for (String next : this.superEntries.keySet()) {
                entries.put(next, (Attributes) this.superEntries.get(next).clone());
            }
        }
        return manifest;
    }

    public void close() throws IOException {
        URLJarFileCloseController uRLJarFileCloseController = this.closeController;
        if (uRLJarFileCloseController != null) {
            uRLJarFileCloseController.close(this);
        }
        super.close();
    }

    /* access modifiers changed from: private */
    public synchronized boolean isSuperMan() throws IOException {
        if (this.superMan == null) {
            this.superMan = super.getManifest();
        }
        Manifest manifest = this.superMan;
        if (manifest == null) {
            return false;
        }
        this.superAttr = manifest.getMainAttributes();
        this.superEntries = this.superMan.getEntries();
        return true;
    }

    private static JarFile retrieve(URL url) throws IOException {
        return retrieve(url, (URLJarFileCloseController) null);
    }

    private static JarFile retrieve(URL url, final URLJarFileCloseController uRLJarFileCloseController) throws IOException {
        final InputStream inputStream;
        try {
            inputStream = url.openConnection().getInputStream();
            JarFile jarFile = (JarFile) AccessController.doPrivileged(new PrivilegedExceptionAction<JarFile>() {
                public JarFile run() throws IOException {
                    Path createTempFile = Files.createTempFile("jar_cache", (String) null, new FileAttribute[0]);
                    try {
                        Files.copy(InputStream.this, createTempFile, StandardCopyOption.REPLACE_EXISTING);
                        URLJarFile uRLJarFile = new URLJarFile(createTempFile.toFile(), uRLJarFileCloseController);
                        createTempFile.toFile().deleteOnExit();
                        return uRLJarFile;
                    } catch (Throwable th) {
                        try {
                            Files.delete(createTempFile);
                        } catch (IOException e) {
                            th.addSuppressed(e);
                        }
                        throw th;
                    }
                }
            });
            if (inputStream != null) {
                inputStream.close();
            }
            return jarFile;
        } catch (PrivilegedActionException e) {
            throw ((IOException) e.getException());
        } catch (Throwable th) {
            th.addSuppressed(th);
        }
        throw th;
    }

    private class URLJarFileEntry extends JarEntry {

        /* renamed from: je */
        private JarEntry f872je;

        URLJarFileEntry(JarEntry jarEntry) {
            super(jarEntry);
            this.f872je = jarEntry;
        }

        public Attributes getAttributes() throws IOException {
            Map r0;
            Attributes attributes;
            if (!URLJarFile.this.isSuperMan() || (r0 = URLJarFile.this.superEntries) == null || (attributes = (Attributes) r0.get(getName())) == null) {
                return null;
            }
            return (Attributes) attributes.clone();
        }

        public Certificate[] getCertificates() {
            Certificate[] certificates = this.f872je.getCertificates();
            if (certificates == null) {
                return null;
            }
            return (Certificate[]) certificates.clone();
        }

        public CodeSigner[] getCodeSigners() {
            CodeSigner[] codeSigners = this.f872je.getCodeSigners();
            if (codeSigners == null) {
                return null;
            }
            return (CodeSigner[]) codeSigners.clone();
        }
    }
}
