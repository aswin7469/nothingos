package java.security.cert;

import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.security.GeneralSecurityException;

public class CertPathValidatorException extends GeneralSecurityException {
    private static final long serialVersionUID = -3083180014971893139L;
    private CertPath certPath;
    private int index;
    private Reason reason;

    public enum BasicReason implements Reason {
        UNSPECIFIED,
        EXPIRED,
        NOT_YET_VALID,
        REVOKED,
        UNDETERMINED_REVOCATION_STATUS,
        INVALID_SIGNATURE,
        ALGORITHM_CONSTRAINED
    }

    public interface Reason extends Serializable {
    }

    public CertPathValidatorException() {
        this((String) null, (Throwable) null);
    }

    public CertPathValidatorException(String str) {
        this(str, (Throwable) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public CertPathValidatorException(Throwable th) {
        this(th == null ? null : th.toString(), th);
    }

    public CertPathValidatorException(String str, Throwable th) {
        this(str, th, (CertPath) null, -1);
    }

    public CertPathValidatorException(String str, Throwable th, CertPath certPath2, int i) {
        this(str, th, certPath2, i, BasicReason.UNSPECIFIED);
    }

    public CertPathValidatorException(String str, Throwable th, CertPath certPath2, int i, Reason reason2) {
        super(str, th);
        this.index = -1;
        this.reason = BasicReason.UNSPECIFIED;
        if (certPath2 == null && i != -1) {
            throw new IllegalArgumentException();
        } else if (i < -1 || (certPath2 != null && i >= certPath2.getCertificates().size())) {
            throw new IndexOutOfBoundsException();
        } else if (reason2 != null) {
            this.certPath = certPath2;
            this.index = i;
            this.reason = reason2;
        } else {
            throw new NullPointerException("reason can't be null");
        }
    }

    public CertPath getCertPath() {
        return this.certPath;
    }

    public int getIndex() {
        return this.index;
    }

    public Reason getReason() {
        return this.reason;
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        if (this.reason == null) {
            this.reason = BasicReason.UNSPECIFIED;
        }
        CertPath certPath2 = this.certPath;
        if (certPath2 != null || this.index == -1) {
            int i = this.index;
            if (i < -1 || (certPath2 != null && i >= certPath2.getCertificates().size())) {
                throw new InvalidObjectException("index out of range");
            }
            return;
        }
        throw new InvalidObjectException("certpath is null and index != -1");
    }
}
