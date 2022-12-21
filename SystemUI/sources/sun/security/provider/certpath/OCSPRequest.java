package sun.security.provider.certpath;

import android.net.wifi.WifiEnterpriseConfig;
import java.p026io.IOException;
import java.security.cert.Extension;
import java.util.Collections;
import java.util.List;
import sun.misc.HexDumpEncoder;
import sun.security.util.Debug;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

class OCSPRequest {
    private static final Debug debug;
    private static final boolean dump;
    private final List<CertId> certIds;
    private final List<Extension> extensions;
    private byte[] nonce;

    static {
        Debug instance = Debug.getInstance("certpath");
        debug = instance;
        dump = instance != null && Debug.isOn(WifiEnterpriseConfig.OCSP);
    }

    OCSPRequest(CertId certId) {
        this((List<CertId>) Collections.singletonList(certId));
    }

    OCSPRequest(List<CertId> list) {
        this.certIds = list;
        this.extensions = Collections.emptyList();
    }

    OCSPRequest(List<CertId> list, List<Extension> list2) {
        this.certIds = list;
        this.extensions = list2;
    }

    /* access modifiers changed from: package-private */
    public byte[] encodeBytes() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (CertId encode : this.certIds) {
            DerOutputStream derOutputStream3 = new DerOutputStream();
            encode.encode(derOutputStream3);
            derOutputStream2.write((byte) 48, derOutputStream3);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        if (!this.extensions.isEmpty()) {
            DerOutputStream derOutputStream4 = new DerOutputStream();
            for (Extension next : this.extensions) {
                next.encode(derOutputStream4);
                if (next.getId().equals(OCSP.NONCE_EXTENSION_OID.toString())) {
                    this.nonce = next.getValue();
                }
            }
            DerOutputStream derOutputStream5 = new DerOutputStream();
            derOutputStream5.write((byte) 48, derOutputStream4);
            derOutputStream.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 2), derOutputStream5);
        }
        DerOutputStream derOutputStream6 = new DerOutputStream();
        derOutputStream6.write((byte) 48, derOutputStream);
        DerOutputStream derOutputStream7 = new DerOutputStream();
        derOutputStream7.write((byte) 48, derOutputStream6);
        byte[] byteArray = derOutputStream7.toByteArray();
        if (dump) {
            HexDumpEncoder hexDumpEncoder = new HexDumpEncoder();
            Debug debug2 = debug;
            debug2.println("OCSPRequest bytes...\n\n" + hexDumpEncoder.encode(byteArray) + "\n");
        }
        return byteArray;
    }

    /* access modifiers changed from: package-private */
    public List<CertId> getCertIds() {
        return this.certIds;
    }

    /* access modifiers changed from: package-private */
    public byte[] getNonce() {
        return this.nonce;
    }
}
