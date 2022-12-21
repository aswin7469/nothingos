package sun.security.x509;

import android.net.wifi.WifiEnterpriseConfig;
import java.lang.reflect.Field;
import sun.misc.HexDumpEncoder;

/* compiled from: CertificateExtensions */
class UnparseableExtension extends Extension {
    private String name = "";
    private Throwable why;

    public UnparseableExtension(Extension extension, Throwable th) {
        super(extension);
        try {
            Class<?> cls = OIDMap.getClass(extension.getExtensionId());
            if (cls != null) {
                Field declaredField = cls.getDeclaredField("NAME");
                this.name = ((String) declaredField.get((Object) null)) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            }
        } catch (Exception unused) {
        }
        this.why = th;
    }

    public String toString() {
        return super.toString() + "Unparseable " + this.name + "extension due to\n" + this.why + "\n\n" + new HexDumpEncoder().encodeBuffer(getExtensionValue());
    }
}
