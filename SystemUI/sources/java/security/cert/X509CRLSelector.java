package java.security.cert;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.math.BigInteger;
import java.p026io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import javax.security.auth.x500.X500Principal;
import sun.security.util.Debug;
import sun.security.util.DerInputStream;
import sun.security.x509.CRLNumberExtension;
import sun.security.x509.X500Name;

public class X509CRLSelector implements CRLSelector {
    private static final Debug debug = Debug.getInstance("certpath");
    private X509Certificate certChecking;
    private Date dateAndTime;
    private HashSet<Object> issuerNames;
    private HashSet<X500Principal> issuerX500Principals;
    private BigInteger maxCRL;
    private BigInteger minCRL;
    private long skew = 0;

    static {
        CertPathHelperImpl.initialize();
    }

    public void setIssuers(Collection<X500Principal> collection) {
        if (collection == null || collection.isEmpty()) {
            this.issuerNames = null;
            this.issuerX500Principals = null;
            return;
        }
        this.issuerX500Principals = new HashSet<>(collection);
        this.issuerNames = new HashSet<>();
        Iterator<X500Principal> it = this.issuerX500Principals.iterator();
        while (it.hasNext()) {
            this.issuerNames.add(it.next().getEncoded());
        }
    }

    public void setIssuerNames(Collection<?> collection) throws IOException {
        if (collection == null || collection.size() == 0) {
            this.issuerNames = null;
            this.issuerX500Principals = null;
            return;
        }
        HashSet<Object> cloneAndCheckIssuerNames = cloneAndCheckIssuerNames(collection);
        this.issuerX500Principals = parseIssuerNames(cloneAndCheckIssuerNames);
        this.issuerNames = cloneAndCheckIssuerNames;
    }

    public void addIssuer(X500Principal x500Principal) {
        addIssuerNameInternal(x500Principal.getEncoded(), x500Principal);
    }

    public void addIssuerName(String str) throws IOException {
        addIssuerNameInternal(str, new X500Name(str).asX500Principal());
    }

    public void addIssuerName(byte[] bArr) throws IOException {
        addIssuerNameInternal(bArr.clone(), new X500Name(bArr).asX500Principal());
    }

    private void addIssuerNameInternal(Object obj, X500Principal x500Principal) {
        if (this.issuerNames == null) {
            this.issuerNames = new HashSet<>();
        }
        if (this.issuerX500Principals == null) {
            this.issuerX500Principals = new HashSet<>();
        }
        this.issuerNames.add(obj);
        this.issuerX500Principals.add(x500Principal);
    }

    private static HashSet<Object> cloneAndCheckIssuerNames(Collection<?> collection) throws IOException {
        HashSet<Object> hashSet = new HashSet<>();
        for (Object next : collection) {
            boolean z = next instanceof byte[];
            if (!z && !(next instanceof String)) {
                throw new IOException("name not byte array or String");
            } else if (z) {
                hashSet.add(((byte[]) next).clone());
            } else {
                hashSet.add(next);
            }
        }
        return hashSet;
    }

    private static HashSet<Object> cloneIssuerNames(Collection<Object> collection) {
        try {
            return cloneAndCheckIssuerNames(collection);
        } catch (IOException e) {
            throw new RuntimeException((Throwable) e);
        }
    }

    private static HashSet<X500Principal> parseIssuerNames(Collection<Object> collection) throws IOException {
        HashSet<X500Principal> hashSet = new HashSet<>();
        for (Object next : collection) {
            if (next instanceof String) {
                hashSet.add(new X500Name((String) next).asX500Principal());
            } else {
                try {
                    hashSet.add(new X500Principal((byte[]) next));
                } catch (IllegalArgumentException e) {
                    throw ((IOException) new IOException("Invalid name").initCause(e));
                }
            }
        }
        return hashSet;
    }

    public void setMinCRLNumber(BigInteger bigInteger) {
        this.minCRL = bigInteger;
    }

    public void setMaxCRLNumber(BigInteger bigInteger) {
        this.maxCRL = bigInteger;
    }

    public void setDateAndTime(Date date) {
        if (date == null) {
            this.dateAndTime = null;
        } else {
            this.dateAndTime = new Date(date.getTime());
        }
        this.skew = 0;
    }

    /* access modifiers changed from: package-private */
    public void setDateAndTime(Date date, long j) {
        this.dateAndTime = date == null ? null : new Date(date.getTime());
        this.skew = j;
    }

    public void setCertificateChecking(X509Certificate x509Certificate) {
        this.certChecking = x509Certificate;
    }

    public Collection<X500Principal> getIssuers() {
        HashSet<X500Principal> hashSet = this.issuerX500Principals;
        if (hashSet == null) {
            return null;
        }
        return Collections.unmodifiableCollection(hashSet);
    }

    public Collection<Object> getIssuerNames() {
        HashSet<Object> hashSet = this.issuerNames;
        if (hashSet == null) {
            return null;
        }
        return cloneIssuerNames(hashSet);
    }

    public BigInteger getMinCRL() {
        return this.minCRL;
    }

    public BigInteger getMaxCRL() {
        return this.maxCRL;
    }

    public Date getDateAndTime() {
        Date date = this.dateAndTime;
        if (date == null) {
            return null;
        }
        return (Date) date.clone();
    }

    public X509Certificate getCertificateChecking() {
        return this.certChecking;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("X509CRLSelector: [\n");
        if (this.issuerNames != null) {
            sb.append("  IssuerNames:\n");
            Iterator<Object> it = this.issuerNames.iterator();
            while (it.hasNext()) {
                sb.append("    " + it.next() + "\n");
            }
        }
        if (this.minCRL != null) {
            sb.append("  minCRLNumber: " + this.minCRL + "\n");
        }
        if (this.maxCRL != null) {
            sb.append("  maxCRLNumber: " + this.maxCRL + "\n");
        }
        if (this.dateAndTime != null) {
            sb.append("  dateAndTime: " + this.dateAndTime + "\n");
        }
        if (this.certChecking != null) {
            sb.append("  Certificate being checked: " + this.certChecking + "\n");
        }
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public boolean match(CRL crl) {
        Date date;
        Debug debug2;
        if (!(crl instanceof X509CRL)) {
            return false;
        }
        X509CRL x509crl = (X509CRL) crl;
        if (this.issuerNames != null) {
            X500Principal issuerX500Principal = x509crl.getIssuerX500Principal();
            Iterator<X500Principal> it = this.issuerX500Principals.iterator();
            boolean z = false;
            while (!z && it.hasNext()) {
                if (it.next().equals(issuerX500Principal)) {
                    z = true;
                }
            }
            if (!z) {
                Debug debug3 = debug;
                if (debug3 != null) {
                    debug3.println("X509CRLSelector.match: issuer DNs don't match");
                }
                return false;
            }
        }
        if (!(this.minCRL == null && this.maxCRL == null)) {
            byte[] extensionValue = x509crl.getExtensionValue("2.5.29.20");
            if (extensionValue == null && (debug2 = debug) != null) {
                debug2.println("X509CRLSelector.match: no CRLNumber");
            }
            try {
                BigInteger bigInteger = new CRLNumberExtension(Boolean.FALSE, new DerInputStream(extensionValue).getOctetString()).get("value");
                BigInteger bigInteger2 = this.minCRL;
                if (bigInteger2 == null || bigInteger.compareTo(bigInteger2) >= 0) {
                    BigInteger bigInteger3 = this.maxCRL;
                    if (bigInteger3 != null && bigInteger.compareTo(bigInteger3) > 0) {
                        Debug debug4 = debug;
                        if (debug4 != null) {
                            debug4.println("X509CRLSelector.match: CRLNumber too large");
                        }
                        return false;
                    }
                } else {
                    Debug debug5 = debug;
                    if (debug5 != null) {
                        debug5.println("X509CRLSelector.match: CRLNumber too small");
                    }
                    return false;
                }
            } catch (IOException unused) {
                Debug debug6 = debug;
                if (debug6 != null) {
                    debug6.println("X509CRLSelector.match: exception in decoding CRL number");
                }
                return false;
            }
        }
        if (this.dateAndTime != null) {
            Date thisUpdate = x509crl.getThisUpdate();
            Date nextUpdate = x509crl.getNextUpdate();
            if (nextUpdate == null) {
                Debug debug7 = debug;
                if (debug7 != null) {
                    debug7.println("X509CRLSelector.match: nextUpdate null");
                }
                return false;
            }
            Date date2 = this.dateAndTime;
            if (this.skew > 0) {
                date = new Date(this.dateAndTime.getTime() + this.skew);
                date2 = new Date(this.dateAndTime.getTime() - this.skew);
            } else {
                date = date2;
            }
            if (date2.after(nextUpdate) || date.before(thisUpdate)) {
                Debug debug8 = debug;
                if (debug8 != null) {
                    debug8.println("X509CRLSelector.match: update out-of-range");
                }
                return false;
            }
        }
        return true;
    }

    public Object clone() {
        try {
            X509CRLSelector x509CRLSelector = (X509CRLSelector) super.clone();
            if (this.issuerNames != null) {
                x509CRLSelector.issuerNames = new HashSet<>(this.issuerNames);
                x509CRLSelector.issuerX500Principals = new HashSet<>(this.issuerX500Principals);
            }
            return x509CRLSelector;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString(), e);
        }
    }
}
