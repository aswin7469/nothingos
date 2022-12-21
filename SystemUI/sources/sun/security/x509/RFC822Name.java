package sun.security.x509;

import com.android.launcher3.icons.cache.BaseIconCache;
import java.p026io.IOException;
import java.util.Locale;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class RFC822Name implements GeneralNameInterface {
    private String name;

    public int getType() {
        return 1;
    }

    public RFC822Name(DerValue derValue) throws IOException {
        String iA5String = derValue.getIA5String();
        this.name = iA5String;
        parseName(iA5String);
    }

    public RFC822Name(String str) throws IOException {
        parseName(str);
        this.name = str;
    }

    public void parseName(String str) throws IOException {
        if (str == null || str.length() == 0) {
            throw new IOException("RFC822Name may not be null or empty");
        }
        String substring = str.substring(str.indexOf(64) + 1);
        if (substring.length() == 0) {
            throw new IOException("RFC822Name may not end with @");
        } else if (substring.startsWith(BaseIconCache.EMPTY_CLASS_NAME) && substring.length() == 1) {
            throw new IOException("RFC822Name domain may not be just .");
        }
    }

    public String getName() {
        return this.name;
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putIA5String(this.name);
    }

    public String toString() {
        return "RFC822Name: " + this.name;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RFC822Name)) {
            return false;
        }
        return this.name.equalsIgnoreCase(((RFC822Name) obj).name);
    }

    public int hashCode() {
        return this.name.toUpperCase(Locale.ENGLISH).hashCode();
    }

    public int constrains(GeneralNameInterface generalNameInterface) throws UnsupportedOperationException {
        if (generalNameInterface == null || generalNameInterface.getType() != 1) {
            return -1;
        }
        String lowerCase = ((RFC822Name) generalNameInterface).getName().toLowerCase(Locale.ENGLISH);
        String lowerCase2 = this.name.toLowerCase(Locale.ENGLISH);
        if (lowerCase.equals(lowerCase2)) {
            return 0;
        }
        if (lowerCase2.endsWith(lowerCase)) {
            if (lowerCase.indexOf(64) == -1 && (lowerCase.startsWith(BaseIconCache.EMPTY_CLASS_NAME) || lowerCase2.charAt(lowerCase2.lastIndexOf(lowerCase) - 1) == '@')) {
                return 2;
            }
        } else if (lowerCase.endsWith(lowerCase2) && lowerCase2.indexOf(64) == -1 && (lowerCase2.startsWith(BaseIconCache.EMPTY_CLASS_NAME) || lowerCase.charAt(lowerCase.lastIndexOf(lowerCase2) - 1) == '@')) {
            return 1;
        }
        return 3;
    }

    public int subtreeDepth() throws UnsupportedOperationException {
        String str = this.name;
        int lastIndexOf = str.lastIndexOf(64);
        int i = 1;
        if (lastIndexOf >= 0) {
            str = str.substring(lastIndexOf + 1);
            i = 2;
        }
        while (str.lastIndexOf(46) >= 0) {
            str = str.substring(0, str.lastIndexOf(46));
            i++;
        }
        return i;
    }
}
