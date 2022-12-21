package java.security.cert;

import java.net.URI;

public final class URICertStoreParameters implements CertStoreParameters {
    private int myhash = -1;
    private final URI uri;

    public URICertStoreParameters(URI uri2) {
        uri2.getClass();
        this.uri = uri2;
    }

    public URI getURI() {
        return this.uri;
    }

    public URICertStoreParameters clone() {
        try {
            return new URICertStoreParameters(this.uri);
        } catch (NullPointerException e) {
            throw new InternalError(e.toString(), e);
        }
    }

    public int hashCode() {
        if (this.myhash == -1) {
            this.myhash = this.uri.hashCode() * 7;
        }
        return this.myhash;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof URICertStoreParameters)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return this.uri.equals(((URICertStoreParameters) obj).getURI());
    }

    public String toString() {
        return "URICertStoreParameters: " + this.uri.toString();
    }
}
