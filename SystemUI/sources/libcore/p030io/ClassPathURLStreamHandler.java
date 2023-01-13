package libcore.p030io;

import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.p026io.File;
import java.p026io.FileNotFoundException;
import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import sun.net.www.ParseUtil;
import sun.net.www.protocol.jar.Handler;

/* renamed from: libcore.io.ClassPathURLStreamHandler */
public class ClassPathURLStreamHandler extends Handler {
    private final String fileUri;
    /* access modifiers changed from: private */
    public final JarFile jarFile;

    public ClassPathURLStreamHandler(String str) throws IOException {
        this.jarFile = new JarFile(str);
        this.fileUri = new File(str).toURI().toString();
    }

    public URL getEntryUrlOrNull(String str) {
        if (this.jarFile.getEntry(str) == null) {
            return null;
        }
        try {
            String encodePath = ParseUtil.encodePath(str, false);
            return new URL("jar", (String) null, -1, this.fileUri + "!/" + encodePath, this);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid entry name", e);
        }
    }

    public boolean isEntryStored(String str) {
        ZipEntry entry = this.jarFile.getEntry(str);
        return entry != null && entry.getMethod() == 0;
    }

    /* access modifiers changed from: protected */
    public URLConnection openConnection(URL url) throws IOException {
        return new ClassPathURLConnection(url);
    }

    public void close() throws IOException {
        this.jarFile.close();
    }

    /* renamed from: libcore.io.ClassPathURLStreamHandler$ClassPathURLConnection */
    private class ClassPathURLConnection extends JarURLConnection {
        /* access modifiers changed from: private */
        public boolean closed;
        /* access modifiers changed from: private */
        public JarFile connectionJarFile;
        private ZipEntry jarEntry;
        private InputStream jarInput;
        /* access modifiers changed from: private */
        public boolean useCachedJarFile;

        public ClassPathURLConnection(URL url) throws MalformedURLException {
            super(url);
        }

        public void connect() throws IOException {
            if (!this.connected) {
                ZipEntry entry = ClassPathURLStreamHandler.this.jarFile.getEntry(getEntryName());
                this.jarEntry = entry;
                if (entry != null) {
                    this.useCachedJarFile = getUseCaches();
                    this.connected = true;
                    return;
                }
                throw new FileNotFoundException("URL does not correspond to an entry in the zip file. URL=" + this.url + ", zipfile=" + ClassPathURLStreamHandler.this.jarFile.getName());
            }
        }

        public JarFile getJarFile() throws IOException {
            connect();
            if (this.useCachedJarFile) {
                this.connectionJarFile = ClassPathURLStreamHandler.this.jarFile;
            } else {
                this.connectionJarFile = new JarFile(ClassPathURLStreamHandler.this.jarFile.getName());
            }
            return this.connectionJarFile;
        }

        public InputStream getInputStream() throws IOException {
            if (!this.closed) {
                connect();
                InputStream inputStream = this.jarInput;
                if (inputStream != null) {
                    return inputStream;
                }
                C47101 r0 = new FilterInputStream(ClassPathURLStreamHandler.this.jarFile.getInputStream(this.jarEntry)) {
                    public void close() throws IOException {
                        super.close();
                        if (ClassPathURLConnection.this.connectionJarFile != null && !ClassPathURLConnection.this.useCachedJarFile) {
                            ClassPathURLConnection.this.connectionJarFile.close();
                            ClassPathURLConnection.this.closed = true;
                        }
                    }
                };
                this.jarInput = r0;
                return r0;
            }
            throw new IllegalStateException("JarURLConnection InputStream has been closed");
        }

        public String getContentType() {
            String guessContentTypeFromName = guessContentTypeFromName(getEntryName());
            return guessContentTypeFromName == null ? "content/unknown" : guessContentTypeFromName;
        }

        public int getContentLength() {
            try {
                connect();
                return (int) getJarEntry().getSize();
            } catch (IOException unused) {
                return -1;
            }
        }
    }
}
