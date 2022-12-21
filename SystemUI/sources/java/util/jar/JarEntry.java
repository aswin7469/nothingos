package java.util.jar;

import java.p026io.IOException;
import java.security.CodeSigner;
import java.security.cert.Certificate;
import java.util.zip.ZipEntry;

public class JarEntry extends ZipEntry {
    Attributes attr;
    Certificate[] certs;
    CodeSigner[] signers;

    public JarEntry(String str) {
        super(str);
    }

    public JarEntry(ZipEntry zipEntry) {
        super(zipEntry);
    }

    public JarEntry(JarEntry jarEntry) {
        this((ZipEntry) jarEntry);
        this.attr = jarEntry.attr;
        this.certs = jarEntry.certs;
        this.signers = jarEntry.signers;
    }

    public Attributes getAttributes() throws IOException {
        return this.attr;
    }

    public Certificate[] getCertificates() {
        Certificate[] certificateArr = this.certs;
        if (certificateArr == null) {
            return null;
        }
        return (Certificate[]) certificateArr.clone();
    }

    public CodeSigner[] getCodeSigners() {
        CodeSigner[] codeSignerArr = this.signers;
        if (codeSignerArr == null) {
            return null;
        }
        return (CodeSigner[]) codeSignerArr.clone();
    }

    public String getRealName() {
        return super.getName();
    }
}
