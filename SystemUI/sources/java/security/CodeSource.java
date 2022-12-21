package java.security;

import java.net.URL;
import java.p026io.Serializable;
import java.security.cert.Certificate;

public class CodeSource implements Serializable {
    private URL location;

    public final Certificate[] getCertificates() {
        return null;
    }

    public final CodeSigner[] getCodeSigners() {
        return null;
    }

    public boolean implies(CodeSource codeSource) {
        return true;
    }

    public CodeSource(URL url, Certificate[] certificateArr) {
        this.location = url;
    }

    public CodeSource(URL url, CodeSigner[] codeSignerArr) {
        this.location = url;
    }

    public final URL getLocation() {
        return this.location;
    }
}
