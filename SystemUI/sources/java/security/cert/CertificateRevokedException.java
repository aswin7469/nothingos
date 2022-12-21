package java.security.cert;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import sun.security.util.ObjectIdentifier;
import sun.security.x509.Extension;
import sun.security.x509.InvalidityDateExtension;

public class CertificateRevokedException extends CertificateException {
    private static final long serialVersionUID = 7839996631571608627L;
    private final X500Principal authority;
    private transient Map<String, Extension> extensions;
    private final CRLReason reason;
    private Date revocationDate;

    public CertificateRevokedException(Date date, CRLReason cRLReason, X500Principal x500Principal, Map<String, Extension> map) {
        if (date == null || cRLReason == null || x500Principal == null || map == null) {
            throw null;
        }
        this.revocationDate = new Date(date.getTime());
        this.reason = cRLReason;
        this.authority = x500Principal;
        Map<String, Extension> checkedMap = Collections.checkedMap(new HashMap(), String.class, Extension.class);
        this.extensions = checkedMap;
        checkedMap.putAll(map);
    }

    public Date getRevocationDate() {
        return (Date) this.revocationDate.clone();
    }

    public CRLReason getRevocationReason() {
        return this.reason;
    }

    public X500Principal getAuthorityName() {
        return this.authority;
    }

    public Date getInvalidityDate() {
        Extension extension = getExtensions().get("2.5.29.24");
        if (extension == null) {
            return null;
        }
        try {
            return new Date(InvalidityDateExtension.toImpl(extension).get("DATE").getTime());
        } catch (IOException unused) {
            return null;
        }
    }

    public Map<String, Extension> getExtensions() {
        return Collections.unmodifiableMap(this.extensions);
    }

    public String getMessage() {
        return "Certificate has been revoked, reason: " + this.reason + ", revocation date: " + this.revocationDate + ", authority: " + this.authority + ", extension OIDs: " + this.extensions.keySet();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.extensions.size());
        for (Map.Entry<String, Extension> value : this.extensions.entrySet()) {
            Extension extension = (Extension) value.getValue();
            objectOutputStream.writeObject(extension.getId());
            objectOutputStream.writeBoolean(extension.isCritical());
            byte[] value2 = extension.getValue();
            objectOutputStream.writeInt(value2.length);
            objectOutputStream.write(value2);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.revocationDate = new Date(this.revocationDate.getTime());
        int readInt = objectInputStream.readInt();
        if (readInt == 0) {
            this.extensions = Collections.emptyMap();
        } else {
            this.extensions = new HashMap(readInt);
        }
        for (int i = 0; i < readInt; i++) {
            String str = (String) objectInputStream.readObject();
            boolean readBoolean = objectInputStream.readBoolean();
            byte[] bArr = new byte[objectInputStream.readInt()];
            objectInputStream.readFully(bArr);
            this.extensions.put(str, Extension.newExtension(new ObjectIdentifier(str), readBoolean, bArr));
        }
    }
}
