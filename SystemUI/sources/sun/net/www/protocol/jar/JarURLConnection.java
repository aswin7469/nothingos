package sun.net.www.protocol.jar;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.p026io.BufferedInputStream;
import java.p026io.FileNotFoundException;
import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.Permission;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarURLConnection extends java.net.JarURLConnection {
    private static final boolean debug = false;
    private static final JarFileFactory factory = JarFileFactory.getInstance();
    private String contentType;
    private String entryName = getEntryName();
    private JarEntry jarEntry;
    /* access modifiers changed from: private */
    public JarFile jarFile;
    private URL jarFileURL;
    private URLConnection jarFileURLConnection;
    private Permission permission;

    public JarURLConnection(URL url, Handler handler) throws MalformedURLException, IOException {
        super(url);
        URL jarFileURL2 = getJarFileURL();
        this.jarFileURL = jarFileURL2;
        this.jarFileURLConnection = jarFileURL2.openConnection();
    }

    public JarFile getJarFile() throws IOException {
        connect();
        return this.jarFile;
    }

    public JarEntry getJarEntry() throws IOException {
        connect();
        return this.jarEntry;
    }

    public Permission getPermission() throws IOException {
        return this.jarFileURLConnection.getPermission();
    }

    class JarURLInputStream extends FilterInputStream {
        JarURLInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public void close() throws IOException {
            try {
                super.close();
            } finally {
                if (!JarURLConnection.this.getUseCaches()) {
                    JarURLConnection.this.jarFile.close();
                }
            }
        }
    }

    public void connect() throws IOException {
        if (!this.connected) {
            JarFileFactory jarFileFactory = factory;
            this.jarFile = jarFileFactory.get(getJarFileURL(), getUseCaches());
            if (getUseCaches()) {
                boolean useCaches = this.jarFileURLConnection.getUseCaches();
                URLConnection connection = jarFileFactory.getConnection(this.jarFile);
                this.jarFileURLConnection = connection;
                connection.setUseCaches(useCaches);
            }
            String str = this.entryName;
            if (str != null) {
                JarEntry jarEntry2 = (JarEntry) this.jarFile.getEntry(str);
                this.jarEntry = jarEntry2;
                if (jarEntry2 == null) {
                    try {
                        if (!getUseCaches()) {
                            this.jarFile.close();
                        }
                    } catch (Exception unused) {
                    }
                    throw new FileNotFoundException("JAR entry " + this.entryName + " not found in " + this.jarFile.getName());
                }
            }
            this.connected = true;
        }
    }

    public InputStream getInputStream() throws IOException {
        connect();
        if (this.entryName != null) {
            JarEntry jarEntry2 = this.jarEntry;
            if (jarEntry2 != null) {
                return new JarURLInputStream(this.jarFile.getInputStream(jarEntry2));
            }
            throw new FileNotFoundException("JAR entry " + this.entryName + " not found in " + this.jarFile.getName());
        }
        throw new IOException("no entry name specified");
    }

    public int getContentLength() {
        long contentLengthLong = getContentLengthLong();
        if (contentLengthLong > 2147483647L) {
            return -1;
        }
        return (int) contentLengthLong;
    }

    public long getContentLengthLong() {
        try {
            connect();
            if (this.jarEntry == null) {
                return this.jarFileURLConnection.getContentLengthLong();
            }
            return getJarEntry().getSize();
        } catch (IOException unused) {
            return -1;
        }
    }

    public Object getContent() throws IOException {
        connect();
        if (this.entryName == null) {
            return this.jarFile;
        }
        return super.getContent();
    }

    public String getContentType() {
        if (this.contentType == null) {
            if (this.entryName == null) {
                this.contentType = "x-java/jar";
            } else {
                try {
                    connect();
                    InputStream inputStream = this.jarFile.getInputStream(this.jarEntry);
                    this.contentType = guessContentTypeFromStream(new BufferedInputStream(inputStream));
                    inputStream.close();
                } catch (IOException unused) {
                }
            }
            if (this.contentType == null) {
                this.contentType = guessContentTypeFromName(this.entryName);
            }
            if (this.contentType == null) {
                this.contentType = "content/unknown";
            }
        }
        return this.contentType;
    }

    public String getHeaderField(String str) {
        return this.jarFileURLConnection.getHeaderField(str);
    }

    public void setRequestProperty(String str, String str2) {
        this.jarFileURLConnection.setRequestProperty(str, str2);
    }

    public String getRequestProperty(String str) {
        return this.jarFileURLConnection.getRequestProperty(str);
    }

    public void addRequestProperty(String str, String str2) {
        this.jarFileURLConnection.addRequestProperty(str, str2);
    }

    public Map<String, List<String>> getRequestProperties() {
        return this.jarFileURLConnection.getRequestProperties();
    }

    public void setAllowUserInteraction(boolean z) {
        this.jarFileURLConnection.setAllowUserInteraction(z);
    }

    public boolean getAllowUserInteraction() {
        return this.jarFileURLConnection.getAllowUserInteraction();
    }

    public void setUseCaches(boolean z) {
        this.jarFileURLConnection.setUseCaches(z);
    }

    public boolean getUseCaches() {
        return this.jarFileURLConnection.getUseCaches();
    }

    public void setIfModifiedSince(long j) {
        this.jarFileURLConnection.setIfModifiedSince(j);
    }

    public void setDefaultUseCaches(boolean z) {
        this.jarFileURLConnection.setDefaultUseCaches(z);
    }

    public boolean getDefaultUseCaches() {
        return this.jarFileURLConnection.getDefaultUseCaches();
    }
}
