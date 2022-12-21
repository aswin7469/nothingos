package sun.security.util;

import java.p026io.File;
import java.p026io.FileInputStream;
import java.security.AccessController;
import java.security.KeyStore;
import java.security.PrivilegedAction;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashSet;
import sun.security.x509.X509CertImpl;

public class AnchorCertificates {
    private static final String HASH = "SHA-256";
    /* access modifiers changed from: private */
    public static HashSet<String> certs;
    /* access modifiers changed from: private */
    public static final Debug debug = Debug.getInstance("certpath");

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                FileInputStream fileInputStream;
                File file = new File(System.getProperty("java.home"), "lib/security/cacerts");
                try {
                    KeyStore instance = KeyStore.getInstance("JKS");
                    fileInputStream = new FileInputStream(file);
                    instance.load(fileInputStream, (char[]) null);
                    AnchorCertificates.certs = new HashSet();
                    Enumeration<String> aliases = instance.aliases();
                    while (aliases.hasMoreElements()) {
                        String nextElement = aliases.nextElement();
                        if (nextElement.contains(" [jdk")) {
                            AnchorCertificates.certs.add(X509CertImpl.getFingerprint(AnchorCertificates.HASH, (X509Certificate) instance.getCertificate(nextElement)));
                        }
                    }
                    fileInputStream.close();
                } catch (Exception e) {
                    if (AnchorCertificates.debug != null) {
                        AnchorCertificates.debug.println("Error parsing cacerts");
                    }
                    e.printStackTrace();
                } catch (Throwable th) {
                    th.addSuppressed(th);
                }
                return null;
                throw th;
            }
        });
    }

    public static boolean contains(X509Certificate x509Certificate) {
        Debug debug2;
        boolean contains = certs.contains(X509CertImpl.getFingerprint(HASH, x509Certificate));
        if (contains && (debug2 = debug) != null) {
            debug2.println("AnchorCertificate.contains: matched " + x509Certificate.getSubjectDN());
        }
        return contains;
    }

    private AnchorCertificates() {
    }
}
