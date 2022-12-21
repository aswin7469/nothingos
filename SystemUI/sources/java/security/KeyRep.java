package java.security;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.NotSerializableException;
import java.p026io.ObjectStreamException;
import java.p026io.Serializable;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Locale;
import javax.crypto.spec.SecretKeySpec;

public class KeyRep implements Serializable {
    private static final String PKCS8 = "PKCS#8";
    private static final String RAW = "RAW";
    private static final String X509 = "X.509";
    private static final long serialVersionUID = -4757683898830641853L;
    private String algorithm;
    private byte[] encoded;
    private String format;
    private Type type;

    public enum Type {
        SECRET,
        PUBLIC,
        PRIVATE
    }

    public KeyRep(Type type2, String str, String str2, byte[] bArr) {
        if (type2 == null || str == null || str2 == null || bArr == null) {
            throw new NullPointerException("invalid null input(s)");
        }
        this.type = type2;
        this.algorithm = str;
        this.format = str2.toUpperCase(Locale.ENGLISH);
        this.encoded = (byte[]) bArr.clone();
    }

    /* access modifiers changed from: protected */
    public Object readResolve() throws ObjectStreamException {
        try {
            if (this.type == Type.SECRET && RAW.equals(this.format)) {
                return new SecretKeySpec(this.encoded, this.algorithm);
            }
            if (this.type == Type.PUBLIC && X509.equals(this.format)) {
                return KeyFactory.getInstance(this.algorithm).generatePublic(new X509EncodedKeySpec(this.encoded));
            }
            if (this.type == Type.PRIVATE && PKCS8.equals(this.format)) {
                return KeyFactory.getInstance(this.algorithm).generatePrivate(new PKCS8EncodedKeySpec(this.encoded));
            }
            throw new NotSerializableException("unrecognized type/format combination: " + this.type + "/" + this.format);
        } catch (NotSerializableException e) {
            throw e;
        } catch (Exception e2) {
            NotSerializableException notSerializableException = new NotSerializableException("java.security.Key: [" + this.type + "] [" + this.algorithm + "] [" + this.format + NavigationBarInflaterView.SIZE_MOD_END);
            notSerializableException.initCause(e2);
            throw notSerializableException;
        }
    }
}
