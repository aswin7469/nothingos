package javax.xml.namespace;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;

public class QName implements Serializable {
    private static final long compatibilitySerialVersionUID = 4418622981026545151L;
    private static final long defaultSerialVersionUID = -9120448754896609940L;
    private static final long serialVersionUID = (!"1.0".equals(System.getProperty("org.apache.xml.namespace.QName.useCompatibleSerialVersionUID")) ? defaultSerialVersionUID : compatibilitySerialVersionUID);
    private final String localPart;
    private final String namespaceURI;
    private String prefix;
    private transient String qNameAsString;

    public QName(String str, String str2) {
        this(str, str2, "");
    }

    public QName(String str, String str2, String str3) {
        if (str == null) {
            this.namespaceURI = "";
        } else {
            this.namespaceURI = str;
        }
        if (str2 != null) {
            this.localPart = str2;
            if (str3 != null) {
                this.prefix = str3;
                return;
            }
            throw new IllegalArgumentException("prefix cannot be \"null\" when creating a QName");
        }
        throw new IllegalArgumentException("local part cannot be \"null\" when creating a QName");
    }

    public QName(String str) {
        this("", str, "");
    }

    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    public String getLocalPart() {
        return this.localPart;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof QName)) {
            return false;
        }
        QName qName = (QName) obj;
        if (!this.localPart.equals(qName.localPart) || !this.namespaceURI.equals(qName.namespaceURI)) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        return this.localPart.hashCode() ^ this.namespaceURI.hashCode();
    }

    public String toString() {
        String str = this.qNameAsString;
        if (str == null) {
            int length = this.namespaceURI.length();
            if (length == 0) {
                str = this.localPart;
            } else {
                StringBuilder sb = new StringBuilder(length + this.localPart.length() + 2);
                sb.append('{');
                sb.append(this.namespaceURI);
                sb.append('}');
                sb.append(this.localPart);
                str = sb.toString();
            }
            this.qNameAsString = str;
        }
        return str;
    }

    public static QName valueOf(String str) {
        if (str == null) {
            throw new IllegalArgumentException("cannot create QName from \"null\" or \"\" String");
        } else if (str.length() == 0) {
            return new QName("", str, "");
        } else {
            if (str.charAt(0) != '{') {
                return new QName("", str, "");
            }
            if (!str.startsWith("{}")) {
                int indexOf = str.indexOf(125);
                if (indexOf != -1) {
                    return new QName(str.substring(1, indexOf), str.substring(indexOf + 1), "");
                }
                throw new IllegalArgumentException("cannot create QName from \"" + str + "\", missing closing \"}\"");
            }
            throw new IllegalArgumentException("Namespace URI .equals(XMLConstants.NULL_NS_URI), .equals(\"\"), only the local part, \"" + str.substring(2) + "\", should be provided.");
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.prefix == null) {
            this.prefix = "";
        }
    }
}
