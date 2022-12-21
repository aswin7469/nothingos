package java.security;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.List;

public final class Timestamp implements Serializable {
    private static final long serialVersionUID = -5502683707821851294L;
    private transient int myhash = -1;
    private CertPath signerCertPath;
    private Date timestamp;

    public Timestamp(Date date, CertPath certPath) {
        if (date == null || certPath == null) {
            throw null;
        }
        this.timestamp = new Date(date.getTime());
        this.signerCertPath = certPath;
    }

    public Date getTimestamp() {
        return new Date(this.timestamp.getTime());
    }

    public CertPath getSignerCertPath() {
        return this.signerCertPath;
    }

    public int hashCode() {
        if (this.myhash == -1) {
            this.myhash = this.timestamp.hashCode() + this.signerCertPath.hashCode();
        }
        return this.myhash;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Timestamp)) {
            return false;
        }
        Timestamp timestamp2 = (Timestamp) obj;
        if (this == timestamp2) {
            return true;
        }
        if (!this.timestamp.equals(timestamp2.getTimestamp()) || !this.signerCertPath.equals(timestamp2.getSignerCertPath())) {
            return false;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.KEY_CODE_START);
        sb.append("timestamp: " + this.timestamp);
        List<? extends Certificate> certificates = this.signerCertPath.getCertificates();
        if (!certificates.isEmpty()) {
            sb.append("TSA: " + certificates.get(0));
        } else {
            sb.append("TSA: <empty>");
        }
        sb.append(NavigationBarInflaterView.KEY_CODE_END);
        return sb.toString();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.myhash = -1;
        this.timestamp = new Date(this.timestamp.getTime());
    }
}
