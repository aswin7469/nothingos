package sun.security.provider.certpath;

import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import sun.security.pkcs.ContentInfo;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.x509.AlgorithmId;

public class X509CertPath extends CertPath {
    private static final String COUNT_ENCODING = "count";
    private static final String PKCS7_ENCODING = "PKCS7";
    private static final String PKIPATH_ENCODING = "PkiPath";
    private static final Collection<String> encodingList;
    private static final long serialVersionUID = 4989800333263052980L;
    private List<X509Certificate> certs;

    static {
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(PKIPATH_ENCODING);
        arrayList.add(PKCS7_ENCODING);
        encodingList = Collections.unmodifiableCollection(arrayList);
    }

    public X509CertPath(List<? extends Certificate> list) throws CertificateException {
        super("X.509");
        for (Object next : list) {
            if (!(next instanceof X509Certificate)) {
                throw new CertificateException("List is not all X509Certificates: " + next.getClass().getName());
            }
        }
        this.certs = Collections.unmodifiableList(new ArrayList(list));
    }

    public X509CertPath(InputStream inputStream) throws CertificateException {
        this(inputStream, PKIPATH_ENCODING);
    }

    public X509CertPath(InputStream inputStream, String str) throws CertificateException {
        super("X.509");
        str.hashCode();
        if (str.equals(PKCS7_ENCODING)) {
            this.certs = parsePKCS7(inputStream);
        } else if (str.equals(PKIPATH_ENCODING)) {
            this.certs = parsePKIPATH(inputStream);
        } else {
            throw new CertificateException("unsupported encoding");
        }
    }

    private static List<X509Certificate> parsePKIPATH(InputStream inputStream) throws CertificateException {
        if (inputStream != null) {
            try {
                DerValue[] sequence = new DerInputStream(readAllBytes(inputStream)).getSequence(3);
                if (sequence.length == 0) {
                    return Collections.emptyList();
                }
                CertificateFactory instance = CertificateFactory.getInstance("X.509");
                ArrayList arrayList = new ArrayList(sequence.length);
                for (int length = sequence.length - 1; length >= 0; length--) {
                    arrayList.add((X509Certificate) instance.generateCertificate(new ByteArrayInputStream(sequence[length].toByteArray())));
                }
                return Collections.unmodifiableList(arrayList);
            } catch (IOException e) {
                throw new CertificateException("IOException parsing PkiPath data: " + e, e);
            }
        } else {
            throw new CertificateException("input stream is null");
        }
    }

    private static List<X509Certificate> parsePKCS7(InputStream inputStream) throws CertificateException {
        List list;
        if (inputStream != null) {
            try {
                if (!inputStream.markSupported()) {
                    inputStream = new ByteArrayInputStream(readAllBytes(inputStream));
                }
                X509Certificate[] certificates = new PKCS7(inputStream).getCertificates();
                if (certificates != null) {
                    list = Arrays.asList(certificates);
                } else {
                    list = new ArrayList(0);
                }
                return Collections.unmodifiableList(list);
            } catch (IOException e) {
                throw new CertificateException("IOException parsing PKCS7 data: " + e);
            }
        } else {
            throw new CertificateException("input stream is null");
        }
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[8192];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
        while (true) {
            int read = inputStream.read(bArr);
            if (read == -1) {
                return byteArrayOutputStream.toByteArray();
            }
            byteArrayOutputStream.write(bArr, 0, read);
        }
    }

    public byte[] getEncoded() throws CertificateEncodingException {
        return encodePKIPATH();
    }

    private byte[] encodePKIPATH() throws CertificateEncodingException {
        List<X509Certificate> list = this.certs;
        ListIterator<X509Certificate> listIterator = list.listIterator(list.size());
        try {
            DerOutputStream derOutputStream = new DerOutputStream();
            while (listIterator.hasPrevious()) {
                X509Certificate previous = listIterator.previous();
                if (this.certs.lastIndexOf(previous) == this.certs.indexOf(previous)) {
                    derOutputStream.write(previous.getEncoded());
                } else {
                    throw new CertificateEncodingException("Duplicate Certificate");
                }
            }
            DerOutputStream derOutputStream2 = new DerOutputStream();
            derOutputStream2.write((byte) 48, derOutputStream);
            return derOutputStream2.toByteArray();
        } catch (IOException e) {
            throw new CertificateEncodingException("IOException encoding PkiPath data: " + e, e);
        }
    }

    private byte[] encodePKCS7() throws CertificateEncodingException {
        ContentInfo contentInfo = new ContentInfo(ContentInfo.DATA_OID, (DerValue) null);
        List<X509Certificate> list = this.certs;
        PKCS7 pkcs7 = new PKCS7(new AlgorithmId[0], contentInfo, (X509Certificate[]) list.toArray(new X509Certificate[list.size()]), new SignerInfo[0]);
        DerOutputStream derOutputStream = new DerOutputStream();
        try {
            pkcs7.encodeSignedData(derOutputStream);
            return derOutputStream.toByteArray();
        } catch (IOException e) {
            throw new CertificateEncodingException(e.getMessage());
        }
    }

    public byte[] getEncoded(String str) throws CertificateEncodingException {
        str.hashCode();
        if (str.equals(PKCS7_ENCODING)) {
            return encodePKCS7();
        }
        if (str.equals(PKIPATH_ENCODING)) {
            return encodePKIPATH();
        }
        throw new CertificateEncodingException("unsupported encoding");
    }

    public static Iterator<String> getEncodingsStatic() {
        return encodingList.iterator();
    }

    public Iterator<String> getEncodings() {
        return getEncodingsStatic();
    }

    public List<X509Certificate> getCertificates() {
        return this.certs;
    }
}
