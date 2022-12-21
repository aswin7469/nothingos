package sun.security.provider.certpath;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidatorException;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.PKIXReason;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import sun.security.util.Debug;

class PKIXMasterCertPathValidator {
    private static final Debug debug = Debug.getInstance("certpath");

    PKIXMasterCertPathValidator() {
    }

    static void validate(CertPath certPath, List<X509Certificate> list, List<PKIXCertPathChecker> list2) throws CertPathValidatorException {
        int size = list.size();
        Debug debug2 = debug;
        if (debug2 != null) {
            debug2.println("--------------------------------------------------------------");
            debug2.println("Executing PKIX certification path validation algorithm.");
        }
        int i = 0;
        while (i < size) {
            X509Certificate x509Certificate = list.get(i);
            Debug debug3 = debug;
            if (debug3 != null) {
                debug3.println("Checking cert" + (i + 1) + " - Subject: " + x509Certificate.getSubjectX500Principal());
            }
            Set<String> criticalExtensionOIDs = x509Certificate.getCriticalExtensionOIDs();
            if (criticalExtensionOIDs == null) {
                criticalExtensionOIDs = Collections.emptySet();
            }
            if (debug3 != null && !criticalExtensionOIDs.isEmpty()) {
                StringJoiner stringJoiner = new StringJoiner(", ", "{", "}");
                for (String add : criticalExtensionOIDs) {
                    stringJoiner.add(add);
                }
                debug.println("Set of critical extensions: " + stringJoiner.toString());
            }
            int i2 = 0;
            while (i2 < list2.size()) {
                PKIXCertPathChecker pKIXCertPathChecker = list2.get(i2);
                Debug debug4 = debug;
                if (debug4 != null) {
                    debug4.println("-Using checker" + (i2 + 1) + " ... [" + pKIXCertPathChecker.getClass().getName() + NavigationBarInflaterView.SIZE_MOD_END);
                }
                if (i == 0) {
                    pKIXCertPathChecker.init(false);
                }
                try {
                    pKIXCertPathChecker.check(x509Certificate, criticalExtensionOIDs);
                    if (debug4 != null) {
                        debug4.println("-checker" + (i2 + 1) + " validation succeeded");
                    }
                    i2++;
                } catch (CertPathValidatorException e) {
                    throw new CertPathValidatorException(e.getMessage(), e.getCause() != null ? e.getCause() : e, certPath, size - (i + 1), e.getReason());
                }
            }
            if (criticalExtensionOIDs.isEmpty()) {
                Debug debug5 = debug;
                if (debug5 != null) {
                    debug5.println("\ncert" + (i + 1) + " validation succeeded.\n");
                }
                i++;
            } else {
                throw new CertPathValidatorException("unrecognized critical extension(s)", (Throwable) null, certPath, size - (i + 1), PKIXReason.UNRECOGNIZED_CRIT_EXT);
            }
        }
        Debug debug6 = debug;
        if (debug6 != null) {
            debug6.println("Cert path validation succeeded. (PKIX validation algorithm)");
            debug6.println("--------------------------------------------------------------");
        }
    }
}
