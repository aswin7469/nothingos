package java.net;

import java.p026io.IOException;
import java.security.cert.Certificate;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import sun.net.www.ParseUtil;

public abstract class JarURLConnection extends URLConnection {
    private String entryName;
    private URL jarFileURL;
    protected URLConnection jarFileURLConnection;

    public abstract JarFile getJarFile() throws IOException;

    protected JarURLConnection(URL url) throws MalformedURLException {
        super(url);
        parseSpecs(url);
    }

    private void parseSpecs(URL url) throws MalformedURLException {
        String file = url.getFile();
        int indexOf = file.indexOf("!/");
        if (indexOf != -1) {
            this.jarFileURL = new URL(file.substring(0, indexOf));
            int i = indexOf + 1 + 1;
            if (i != file.length()) {
                String substring = file.substring(i, file.length());
                this.entryName = substring;
                this.entryName = ParseUtil.decode(substring);
                return;
            }
            return;
        }
        throw new MalformedURLException("no !/ found in url spec:" + file);
    }

    public URL getJarFileURL() {
        return this.jarFileURL;
    }

    public String getEntryName() {
        return this.entryName;
    }

    public Manifest getManifest() throws IOException {
        return getJarFile().getManifest();
    }

    public JarEntry getJarEntry() throws IOException {
        if (this.entryName == null) {
            return null;
        }
        return getJarFile().getJarEntry(this.entryName);
    }

    public Attributes getAttributes() throws IOException {
        JarEntry jarEntry = getJarEntry();
        if (jarEntry != null) {
            return jarEntry.getAttributes();
        }
        return null;
    }

    public Attributes getMainAttributes() throws IOException {
        Manifest manifest = getManifest();
        if (manifest != null) {
            return manifest.getMainAttributes();
        }
        return null;
    }

    public Certificate[] getCertificates() throws IOException {
        JarEntry jarEntry = getJarEntry();
        if (jarEntry != null) {
            return jarEntry.getCertificates();
        }
        return null;
    }
}
