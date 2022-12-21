package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.cert.Extension;
import java.util.Date;
import java.util.Enumeration;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class InvalidityDateExtension extends Extension implements CertAttrSet<String> {
    public static final String DATE = "date";
    public static final String NAME = "InvalidityDate";
    private Date date;

    public String getName() {
        return NAME;
    }

    private void encodeThis() throws IOException {
        if (this.date == null) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putGeneralizedTime(this.date);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public InvalidityDateExtension(Date date2) throws IOException {
        this(false, date2);
    }

    public InvalidityDateExtension(boolean z, Date date2) throws IOException {
        this.extensionId = PKIXExtensions.InvalidityDate_Id;
        this.critical = z;
        this.date = date2;
        encodeThis();
    }

    public InvalidityDateExtension(Boolean bool, Object obj) throws IOException {
        this.extensionId = PKIXExtensions.InvalidityDate_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        this.date = new DerValue(this.extensionValue).getGeneralizedTime();
    }

    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof Date)) {
            throw new IOException("Attribute must be of type Date.");
        } else if (str.equalsIgnoreCase(DATE)) {
            this.date = (Date) obj;
            encodeThis();
        } else {
            throw new IOException("Name not supported by InvalidityDateExtension");
        }
    }

    public Date get(String str) throws IOException {
        if (!str.equalsIgnoreCase(DATE)) {
            throw new IOException("Name not supported by InvalidityDateExtension");
        } else if (this.date == null) {
            return null;
        } else {
            return new Date(this.date.getTime());
        }
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(DATE)) {
            this.date = null;
            encodeThis();
            return;
        }
        throw new IOException("Name not supported by InvalidityDateExtension");
    }

    public String toString() {
        return super.toString() + "    Invalidity Date: " + String.valueOf((Object) this.date);
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.InvalidityDate_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(DATE);
        return attributeNameEnumeration.elements();
    }

    public static InvalidityDateExtension toImpl(Extension extension) throws IOException {
        if (extension instanceof InvalidityDateExtension) {
            return (InvalidityDateExtension) extension;
        }
        return new InvalidityDateExtension(Boolean.valueOf(extension.isCritical()), (Object) extension.getValue());
    }
}
