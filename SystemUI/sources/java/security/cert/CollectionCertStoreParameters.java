package java.security.cert;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Collection;
import java.util.Collections;

public class CollectionCertStoreParameters implements CertStoreParameters {
    private Collection<?> coll;

    public CollectionCertStoreParameters(Collection<?> collection) {
        collection.getClass();
        this.coll = collection;
    }

    public CollectionCertStoreParameters() {
        this.coll = Collections.EMPTY_SET;
    }

    public Collection<?> getCollection() {
        return this.coll;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("CollectionCertStoreParameters: [\n");
        sb.append("  collection: " + this.coll + "\n");
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
