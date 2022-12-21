package sun.security.x509;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class ExtendedKeyUsageExtension extends Extension implements CertAttrSet<String> {
    public static final String IDENT = "x509.info.extensions.ExtendedKeyUsage";
    public static final String NAME = "ExtendedKeyUsage";
    private static final int[] OCSPSigningOidData;
    public static final String USAGES = "usages";
    private static final int[] anyExtendedKeyUsageOidData;
    private static final int[] clientAuthOidData;
    private static final int[] codeSigningOidData;
    private static final int[] emailProtectionOidData;
    private static final int[] ipsecEndSystemOidData;
    private static final int[] ipsecTunnelOidData;
    private static final int[] ipsecUserOidData;
    private static final Map<ObjectIdentifier, String> map;
    private static final int[] serverAuthOidData;
    private static final int[] timeStampingOidData;
    private Vector<ObjectIdentifier> keyUsages;

    public String getName() {
        return NAME;
    }

    static {
        HashMap hashMap = new HashMap();
        map = hashMap;
        int[] iArr = {2, 5, 29, 37, 0};
        anyExtendedKeyUsageOidData = iArr;
        int[] iArr2 = {1, 3, 6, 1, 5, 5, 7, 3, 1};
        serverAuthOidData = iArr2;
        int[] iArr3 = {1, 3, 6, 1, 5, 5, 7, 3, 2};
        clientAuthOidData = iArr3;
        int[] iArr4 = {1, 3, 6, 1, 5, 5, 7, 3, 3};
        codeSigningOidData = iArr4;
        int[] iArr5 = {1, 3, 6, 1, 5, 5, 7, 3, 4};
        emailProtectionOidData = iArr5;
        int[] iArr6 = {1, 3, 6, 1, 5, 5, 7, 3, 5};
        ipsecEndSystemOidData = iArr6;
        int[] iArr7 = {1, 3, 6, 1, 5, 5, 7, 3, 6};
        ipsecTunnelOidData = iArr7;
        int[] iArr8 = {1, 3, 6, 1, 5, 5, 7, 3, 7};
        ipsecUserOidData = iArr8;
        int[] iArr9 = {1, 3, 6, 1, 5, 5, 7, 3, 8};
        timeStampingOidData = iArr9;
        int[] iArr10 = {1, 3, 6, 1, 5, 5, 7, 3, 9};
        OCSPSigningOidData = iArr10;
        hashMap.put(ObjectIdentifier.newInternal(iArr), "anyExtendedKeyUsage");
        hashMap.put(ObjectIdentifier.newInternal(iArr2), "serverAuth");
        hashMap.put(ObjectIdentifier.newInternal(iArr3), "clientAuth");
        hashMap.put(ObjectIdentifier.newInternal(iArr4), "codeSigning");
        hashMap.put(ObjectIdentifier.newInternal(iArr5), "emailProtection");
        hashMap.put(ObjectIdentifier.newInternal(iArr6), "ipsecEndSystem");
        hashMap.put(ObjectIdentifier.newInternal(iArr7), "ipsecTunnel");
        hashMap.put(ObjectIdentifier.newInternal(iArr8), "ipsecUser");
        hashMap.put(ObjectIdentifier.newInternal(iArr9), "timeStamping");
        hashMap.put(ObjectIdentifier.newInternal(iArr10), "OCSPSigning");
    }

    private void encodeThis() throws IOException {
        Vector<ObjectIdentifier> vector = this.keyUsages;
        if (vector == null || vector.isEmpty()) {
            this.extensionValue = null;
            return;
        }
        DerOutputStream derOutputStream = new DerOutputStream();
        DerOutputStream derOutputStream2 = new DerOutputStream();
        for (int i = 0; i < this.keyUsages.size(); i++) {
            derOutputStream2.putOID(this.keyUsages.elementAt(i));
        }
        derOutputStream.write((byte) 48, derOutputStream2);
        this.extensionValue = derOutputStream.toByteArray();
    }

    public ExtendedKeyUsageExtension(Vector<ObjectIdentifier> vector) throws IOException {
        this(Boolean.FALSE, vector);
    }

    public ExtendedKeyUsageExtension(Boolean bool, Vector<ObjectIdentifier> vector) throws IOException {
        this.keyUsages = vector;
        this.extensionId = PKIXExtensions.ExtendedKeyUsage_Id;
        this.critical = bool.booleanValue();
        encodeThis();
    }

    public ExtendedKeyUsageExtension(Boolean bool, Object obj) throws IOException {
        this.extensionId = PKIXExtensions.ExtendedKeyUsage_Id;
        this.critical = bool.booleanValue();
        this.extensionValue = (byte[]) obj;
        DerValue derValue = new DerValue(this.extensionValue);
        if (derValue.tag == 48) {
            this.keyUsages = new Vector<>();
            while (derValue.data.available() != 0) {
                this.keyUsages.addElement(derValue.data.getDerValue().getOID());
            }
            return;
        }
        throw new IOException("Invalid encoding for ExtendedKeyUsageExtension.");
    }

    public String toString() {
        Vector<ObjectIdentifier> vector = this.keyUsages;
        if (vector == null) {
            return "";
        }
        Iterator<ObjectIdentifier> it = vector.iterator();
        String str = "  ";
        boolean z = true;
        while (it.hasNext()) {
            ObjectIdentifier next = it.next();
            if (!z) {
                str = str + "\n  ";
            }
            String str2 = map.get(next);
            if (str2 != null) {
                str = str + str2;
            } else {
                str = str + next.toString();
            }
            z = false;
        }
        return super.toString() + "ExtendedKeyUsages [\n" + str + "\n]\n";
    }

    public void encode(OutputStream outputStream) throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        if (this.extensionValue == null) {
            this.extensionId = PKIXExtensions.ExtendedKeyUsage_Id;
            this.critical = false;
            encodeThis();
        }
        super.encode(derOutputStream);
        outputStream.write(derOutputStream.toByteArray());
    }

    public void set(String str, Object obj) throws IOException {
        if (!str.equalsIgnoreCase(USAGES)) {
            throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:ExtendedKeyUsageExtension.");
        } else if (obj instanceof Vector) {
            this.keyUsages = (Vector) obj;
            encodeThis();
        } else {
            throw new IOException("Attribute value should be of type Vector.");
        }
    }

    public Vector<ObjectIdentifier> get(String str) throws IOException {
        if (str.equalsIgnoreCase(USAGES)) {
            return this.keyUsages;
        }
        throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:ExtendedKeyUsageExtension.");
    }

    public void delete(String str) throws IOException {
        if (str.equalsIgnoreCase(USAGES)) {
            this.keyUsages = null;
            encodeThis();
            return;
        }
        throw new IOException("Attribute name [" + str + "] not recognized by CertAttrSet:ExtendedKeyUsageExtension.");
    }

    public Enumeration<String> getElements() {
        AttributeNameEnumeration attributeNameEnumeration = new AttributeNameEnumeration();
        attributeNameEnumeration.addElement(USAGES);
        return attributeNameEnumeration.elements();
    }

    public List<String> getExtendedKeyUsage() {
        ArrayList arrayList = new ArrayList(this.keyUsages.size());
        Iterator<ObjectIdentifier> it = this.keyUsages.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().toString());
        }
        return arrayList;
    }
}
