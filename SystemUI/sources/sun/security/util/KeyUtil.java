package sun.security.util;

import java.security.Key;
import java.security.interfaces.DSAKey;
import java.security.interfaces.DSAParams;
import java.security.interfaces.ECKey;
import java.security.interfaces.RSAKey;
import java.security.spec.ECParameterSpec;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHKey;

public final class KeyUtil {
    public static final int getKeySize(Key key) {
        int i;
        int i2 = -1;
        if (key instanceof Length) {
            try {
                i = ((Length) key).length();
            } catch (UnsupportedOperationException unused) {
                i = -1;
            }
            if (i >= 0) {
                return i;
            }
        } else {
            i = -1;
        }
        if (key instanceof SecretKey) {
            SecretKey secretKey = (SecretKey) key;
            if (!"RAW".equals(secretKey.getFormat()) || secretKey.getEncoded() == null) {
                return i;
            }
            return secretKey.getEncoded().length * 8;
        } else if (key instanceof RSAKey) {
            return ((RSAKey) key).getModulus().bitLength();
        } else {
            if (key instanceof ECKey) {
                ECParameterSpec params = ((ECKey) key).getParams();
                if (params != null) {
                    return params.getOrder().bitLength();
                }
                return i;
            } else if (!(key instanceof DSAKey)) {
                return key instanceof DHKey ? ((DHKey) key).getParams().getP().bitLength() : i;
            } else {
                DSAParams params2 = ((DSAKey) key).getParams();
                if (params2 != null) {
                    i2 = params2.getP().bitLength();
                }
                return i2;
            }
        }
    }
}
