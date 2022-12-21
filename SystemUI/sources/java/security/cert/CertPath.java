package java.security.cert;

import java.p026io.ByteArrayInputStream;
import java.p026io.InputStream;
import java.p026io.NotSerializableException;
import java.p026io.ObjectStreamException;
import java.p026io.Serializable;
import java.util.Iterator;
import java.util.List;

public abstract class CertPath implements Serializable {
    private static final long serialVersionUID = 6068470306649138683L;
    private String type;

    public abstract List<? extends Certificate> getCertificates();

    public abstract byte[] getEncoded() throws CertificateEncodingException;

    public abstract byte[] getEncoded(String str) throws CertificateEncodingException;

    public abstract Iterator<String> getEncodings();

    protected CertPath(String str) {
        this.type = str;
    }

    public String getType() {
        return this.type;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CertPath)) {
            return false;
        }
        CertPath certPath = (CertPath) obj;
        if (!certPath.getType().equals(this.type)) {
            return false;
        }
        return getCertificates().equals(certPath.getCertificates());
    }

    public int hashCode() {
        return (this.type.hashCode() * 31) + getCertificates().hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n" + this.type + " Cert Path: length = " + getCertificates().size() + ".\n");
        sb.append("[\n");
        int i = 1;
        for (Certificate certificate : getCertificates()) {
            sb.append("=========================================================Certificate " + i + " start.\n");
            sb.append(certificate.toString());
            sb.append("\n=========================================================Certificate " + i + " end.\n\n\n");
            i++;
        }
        sb.append("\n]");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public Object writeReplace() throws ObjectStreamException {
        try {
            return new CertPathRep(this.type, getEncoded());
        } catch (CertificateException e) {
            NotSerializableException notSerializableException = new NotSerializableException("java.security.cert.CertPath: " + this.type);
            notSerializableException.initCause(e);
            throw notSerializableException;
        }
    }

    protected static class CertPathRep implements Serializable {
        private static final long serialVersionUID = 3015633072427920915L;
        private byte[] data;
        private String type;

        protected CertPathRep(String str, byte[] bArr) {
            this.type = str;
            this.data = bArr;
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws ObjectStreamException {
            try {
                return CertificateFactory.getInstance(this.type).generateCertPath((InputStream) new ByteArrayInputStream(this.data));
            } catch (CertificateException e) {
                NotSerializableException notSerializableException = new NotSerializableException("java.security.cert.CertPath: " + this.type);
                notSerializableException.initCause(e);
                throw notSerializableException;
            }
        }
    }
}
