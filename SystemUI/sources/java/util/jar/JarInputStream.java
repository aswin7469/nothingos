package java.util.jar;

import java.p026io.BufferedInputStream;
import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import sun.misc.JarIndex;
import sun.security.util.ManifestEntryVerifier;

public class JarInputStream extends ZipInputStream {
    private final boolean doVerify;
    private JarEntry first;

    /* renamed from: jv */
    private JarVerifier f770jv;
    private Manifest man;
    private ManifestEntryVerifier mev;
    private boolean tryManifest;

    public JarInputStream(InputStream inputStream) throws IOException {
        this(inputStream, true);
    }

    public JarInputStream(InputStream inputStream, boolean z) throws IOException {
        super(inputStream);
        this.doVerify = z;
        JarEntry jarEntry = (JarEntry) super.getNextEntry();
        if (jarEntry != null && jarEntry.getName().equalsIgnoreCase("META-INF/")) {
            jarEntry = (JarEntry) super.getNextEntry();
        }
        this.first = checkManifest(jarEntry);
    }

    private JarEntry checkManifest(JarEntry jarEntry) throws IOException {
        if (jarEntry == null || !JarFile.MANIFEST_NAME.equalsIgnoreCase(jarEntry.getName())) {
            return jarEntry;
        }
        this.man = new Manifest();
        byte[] bytes = getBytes(new BufferedInputStream(this));
        this.man.read(new ByteArrayInputStream(bytes));
        closeEntry();
        if (this.doVerify) {
            this.f770jv = new JarVerifier(bytes);
            this.mev = new ManifestEntryVerifier(this.man);
        }
        return (JarEntry) super.getNextEntry();
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[8192];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
        while (true) {
            int read = inputStream.read(bArr, 0, 8192);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public Manifest getManifest() {
        return this.man;
    }

    public ZipEntry getNextEntry() throws IOException {
        JarEntry jarEntry = this.first;
        if (jarEntry == null) {
            jarEntry = (JarEntry) super.getNextEntry();
            if (this.tryManifest) {
                jarEntry = checkManifest(jarEntry);
                this.tryManifest = false;
            }
        } else {
            if (jarEntry.getName().equalsIgnoreCase(JarIndex.INDEX_NAME)) {
                this.tryManifest = true;
            }
            this.first = null;
        }
        JarVerifier jarVerifier = this.f770jv;
        if (!(jarVerifier == null || jarEntry == null)) {
            if (jarVerifier.nothingToVerify()) {
                this.f770jv = null;
                this.mev = null;
            } else {
                this.f770jv.beginEntry(jarEntry, this.mev);
            }
        }
        return jarEntry;
    }

    public JarEntry getNextJarEntry() throws IOException {
        return (JarEntry) getNextEntry();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = this.first == null ? super.read(bArr, i, i2) : -1;
        JarVerifier jarVerifier = this.f770jv;
        if (jarVerifier != null) {
            jarVerifier.update(read, bArr, i, i2, this.mev);
        }
        return read;
    }

    /* access modifiers changed from: protected */
    public ZipEntry createZipEntry(String str) {
        JarEntry jarEntry = new JarEntry(str);
        Manifest manifest = this.man;
        if (manifest != null) {
            jarEntry.attr = manifest.getAttributes(str);
        }
        return jarEntry;
    }
}
