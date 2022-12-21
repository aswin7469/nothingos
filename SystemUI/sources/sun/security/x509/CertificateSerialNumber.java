package sun.security.x509;

import java.math.BigInteger;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.Enumeration;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CertificateSerialNumber implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.serialNumber";
    public static final String NAME = "serialNumber";
    public static final String NUMBER = "number";
    private SerialNumber serial;

    public String getName() {
        return "serialNumber";
    }

    public CertificateSerialNumber(BigInteger bigInteger) {
        this.serial = new SerialNumber(bigInteger);
    }

    public CertificateSerialNumber(int i) {
        this.serial = new SerialNumber(i);
    }

    public CertificateSerialNumber(DerInputStream derInputStream) throws IOException {
        this.serial = new SerialNumber(derInputStream);
    }

    public CertificateSerialNumber(InputStream inputStream) throws IOException {
        this.serial = new SerialNumber(inputStream);
    }

    public CertificateSerialNumber(DerValue derValue) throws IOException {
        this.serial = new SerialNumber(derValue);
    }

    public String toString() {
        SerialNumber serialNumber = this.serial;
        if (serialNumber == null) {
            return "";
        }
        return serialNumber.toString();
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        this.serial.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof SerialNumber)) {
            throw new IOException("Attribute must be of type SerialNumber.");
        } else if (str.equalsIgnoreCase("number")) {
            this.serial = (SerialNumber) obj;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
        }
    }

    public SerialNumber get(String str) throws IOException {
        if (str.equalsIgnoreCase("number")) {
            return this.serial;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase("number")) {
            this.serial = null;
            return;
        }
        throw new IOException("Attribute name not recognized by CertAttrSet:CertificateSerialNumber.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement("number");
        return attributeNameEnumeration.elements();
    }
}
