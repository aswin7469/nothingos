package java.security.cert;

import java.util.Date;
import java.util.Set;
import sun.security.provider.certpath.CertPathHelper;
import sun.security.x509.GeneralNameInterface;

class CertPathHelperImpl extends CertPathHelper {
    private CertPathHelperImpl() {
    }

    static synchronized void initialize() {
        synchronized (CertPathHelperImpl.class) {
            if (CertPathHelper.instance == null) {
                CertPathHelper.instance = new CertPathHelperImpl();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void implSetPathToNames(X509CertSelector x509CertSelector, Set<GeneralNameInterface> set) {
        x509CertSelector.setPathToNamesInternal(set);
    }

    /* access modifiers changed from: protected */
    public void implSetDateAndTime(X509CRLSelector x509CRLSelector, Date date, long j) {
        x509CRLSelector.setDateAndTime(date, j);
    }
}
