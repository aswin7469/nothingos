package javax.security.auth.x500;

import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.NotActiveException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import sun.security.util.DerValue;
import sun.security.util.ResourcesMgr;
import sun.security.x509.X500Name;

public final class X500Principal implements Principal, Serializable {
    public static final String CANONICAL = "CANONICAL";
    public static final String RFC1779 = "RFC1779";
    public static final String RFC2253 = "RFC2253";
    private static final long serialVersionUID = -500463348111345721L;
    private transient X500Name thisX500Name;

    X500Principal(X500Name x500Name) {
        this.thisX500Name = x500Name;
    }

    public X500Principal(String str) {
        this(str, Collections.emptyMap());
    }

    public X500Principal(String str, Map<String, String> map) {
        if (str == null) {
            throw new NullPointerException(ResourcesMgr.getString("provided.null.name"));
        } else if (map != null) {
            try {
                this.thisX500Name = new X500Name(str, map);
            } catch (Exception e) {
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException("improperly specified input name: " + str);
                illegalArgumentException.initCause(e);
                throw illegalArgumentException;
            }
        } else {
            throw new NullPointerException(ResourcesMgr.getString("provided.null.keyword.map"));
        }
    }

    public X500Principal(byte[] bArr) {
        try {
            this.thisX500Name = new X500Name(bArr);
        } catch (Exception e) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("improperly specified input name");
            illegalArgumentException.initCause(e);
            throw illegalArgumentException;
        }
    }

    public X500Principal(InputStream inputStream) {
        if (inputStream != null) {
            try {
                if (inputStream.markSupported()) {
                    inputStream.mark(inputStream.available() + 1);
                }
                this.thisX500Name = new X500Name(new DerValue(inputStream).data);
            } catch (Exception e) {
                if (inputStream.markSupported()) {
                    try {
                        inputStream.reset();
                    } catch (IOException unused) {
                        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("improperly specified input stream and unable to reset input stream");
                        illegalArgumentException.initCause(e);
                        throw illegalArgumentException;
                    }
                }
                IllegalArgumentException illegalArgumentException2 = new IllegalArgumentException("improperly specified input stream");
                illegalArgumentException2.initCause(e);
                throw illegalArgumentException2;
            }
        } else {
            throw new NullPointerException("provided null input stream");
        }
    }

    public String getName() {
        return getName(RFC2253);
    }

    public String getName(String str) {
        if (str != null) {
            if (str.equalsIgnoreCase(RFC1779)) {
                return this.thisX500Name.getRFC1779Name();
            }
            if (str.equalsIgnoreCase(RFC2253)) {
                return this.thisX500Name.getRFC2253Name();
            }
            if (str.equalsIgnoreCase(CANONICAL)) {
                return this.thisX500Name.getRFC2253CanonicalName();
            }
        }
        throw new IllegalArgumentException("invalid format specified");
    }

    public String getName(String str, Map<String, String> map) {
        if (map != null) {
            if (str != null) {
                if (str.equalsIgnoreCase(RFC1779)) {
                    return this.thisX500Name.getRFC1779Name(map);
                }
                if (str.equalsIgnoreCase(RFC2253)) {
                    return this.thisX500Name.getRFC2253Name(map);
                }
            }
            throw new IllegalArgumentException("invalid format specified");
        }
        throw new NullPointerException(ResourcesMgr.getString("provided.null.OID.map"));
    }

    public byte[] getEncoded() {
        try {
            return this.thisX500Name.getEncoded();
        } catch (IOException e) {
            throw new RuntimeException("unable to get encoding", e);
        }
    }

    public String toString() {
        return this.thisX500Name.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof X500Principal)) {
            return false;
        }
        return this.thisX500Name.equals(((X500Principal) obj).thisX500Name);
    }

    public int hashCode() {
        return this.thisX500Name.hashCode();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.thisX500Name.getEncodedInternal());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, NotActiveException, ClassNotFoundException {
        this.thisX500Name = new X500Name((byte[]) objectInputStream.readObject());
    }
}
