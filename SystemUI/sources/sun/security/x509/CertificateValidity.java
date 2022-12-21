package sun.security.x509;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.util.Date;
import java.util.Enumeration;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

public class CertificateValidity implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.validity";
    public static final String NAME = "validity";
    public static final String NOT_AFTER = "notAfter";
    public static final String NOT_BEFORE = "notBefore";
    private static final long YR_2050 = 2524636800000L;
    private Date notAfter;
    private Date notBefore;

    public String getName() {
        return "validity";
    }

    private Date getNotBefore() {
        return new Date(this.notBefore.getTime());
    }

    private Date getNotAfter() {
        return new Date(this.notAfter.getTime());
    }

    private void construct(DerValue derValue) throws IOException {
        if (derValue.tag != 48) {
            throw new IOException("Invalid encoded CertificateValidity, starting sequence tag missing.");
        } else if (derValue.data.available() != 0) {
            DerValue[] sequence = new DerInputStream(derValue.toByteArray()).getSequence(2);
            if (sequence.length == 2) {
                if (sequence[0].tag == 23) {
                    this.notBefore = derValue.data.getUTCTime();
                } else if (sequence[0].tag == 24) {
                    this.notBefore = derValue.data.getGeneralizedTime();
                } else {
                    throw new IOException("Invalid encoding for CertificateValidity");
                }
                if (sequence[1].tag == 23) {
                    this.notAfter = derValue.data.getUTCTime();
                } else if (sequence[1].tag == 24) {
                    this.notAfter = derValue.data.getGeneralizedTime();
                } else {
                    throw new IOException("Invalid encoding for CertificateValidity");
                }
            } else {
                throw new IOException("Invalid encoding for CertificateValidity");
            }
        } else {
            throw new IOException("No data encoded for CertificateValidity");
        }
    }

    public CertificateValidity() {
    }

    public CertificateValidity(Date date, Date date2) {
        this.notBefore = date;
        this.notAfter = date2;
    }

    public CertificateValidity(DerInputStream derInputStream) throws IOException {
        construct(derInputStream.getDerValue());
    }

    public String toString() {
        if (this.notBefore == null || this.notAfter == null) {
            return "";
        }
        return "Validity: [From: " + this.notBefore.toString() + ",\n               To: " + this.notAfter.toString() + NavigationBarInflaterView.SIZE_MOD_END;
    }

    public void encode(OutputStream outputStream) throws IOException {
        if (this.notBefore == null || this.notAfter == null) {
            throw new IOException("CertAttrSet:CertificateValidity: null values to encode.\n");
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.notBefore.getTime() < YR_2050) {
            derOutputStream.putUTCTime(this.notBefore);
        } else {
            derOutputStream.putGeneralizedTime(this.notBefore);
        }
        if (this.notAfter.getTime() < YR_2050) {
            derOutputStream.putUTCTime(this.notAfter);
        } else {
            derOutputStream.putGeneralizedTime(this.notAfter);
        }
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.write((byte) 48, derOutputStream);
        outputStream.write(derOutputStream2.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (!(obj instanceof Date)) {
            throw new IOException("Attribute must be of type Date.");
        } else if (str.equalsIgnoreCase(NOT_BEFORE)) {
            this.notBefore = (Date) obj;
        } else if (str.equalsIgnoreCase(NOT_AFTER)) {
            this.notAfter = (Date) obj;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet: CertificateValidity.");
        }
    }

    public Date get(String str) throws IOException {
        if (str.equalsIgnoreCase(NOT_BEFORE)) {
            return getNotBefore();
        }
        if (str.equalsIgnoreCase(NOT_AFTER)) {
            return getNotAfter();
        }
        throw new IOException("Attribute name not recognized by CertAttrSet: CertificateValidity.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(NOT_BEFORE)) {
            this.notBefore = null;
        } else if (str.equalsIgnoreCase(NOT_AFTER)) {
            this.notAfter = null;
        } else {
            throw new IOException("Attribute name not recognized by CertAttrSet: CertificateValidity.");
        }
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(NOT_BEFORE);
        attributeNameEnumeration.addElement(NOT_AFTER);
        return attributeNameEnumeration.elements();
    }

    public void valid() throws CertificateNotYetValidException, CertificateExpiredException {
        valid(new Date());
    }

    public void valid(Date date) throws CertificateNotYetValidException, CertificateExpiredException {
        if (this.notBefore.after(date)) {
            throw new CertificateNotYetValidException("NotBefore: " + this.notBefore.toString());
        } else if (this.notAfter.before(date)) {
            throw new CertificateExpiredException("NotAfter: " + this.notAfter.toString());
        }
    }
}
