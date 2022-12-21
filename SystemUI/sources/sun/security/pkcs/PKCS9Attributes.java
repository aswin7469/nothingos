package sun.security.pkcs;

import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.Hashtable;
import sun.security.util.DerEncoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class PKCS9Attributes {
    private final Hashtable<ObjectIdentifier, PKCS9Attribute> attributes;
    private final byte[] derEncoding;
    private boolean ignoreUnsupportedAttributes;
    private final Hashtable<ObjectIdentifier, ObjectIdentifier> permittedAttributes;

    public PKCS9Attributes(ObjectIdentifier[] objectIdentifierArr, DerInputStream derInputStream) throws IOException {
        this.attributes = new Hashtable<>(3);
        this.ignoreUnsupportedAttributes = false;
        if (objectIdentifierArr != null) {
            this.permittedAttributes = new Hashtable<>(objectIdentifierArr.length);
            for (ObjectIdentifier objectIdentifier : objectIdentifierArr) {
                this.permittedAttributes.put(objectIdentifier, objectIdentifier);
            }
        } else {
            this.permittedAttributes = null;
        }
        this.derEncoding = decode(derInputStream);
    }

    public PKCS9Attributes(DerInputStream derInputStream) throws IOException {
        this(derInputStream, false);
    }

    public PKCS9Attributes(DerInputStream derInputStream, boolean z) throws IOException {
        this.attributes = new Hashtable<>(3);
        this.ignoreUnsupportedAttributes = z;
        this.derEncoding = decode(derInputStream);
        this.permittedAttributes = null;
    }

    public PKCS9Attributes(PKCS9Attribute[] pKCS9AttributeArr) throws IllegalArgumentException, IOException {
        this.attributes = new Hashtable<>(3);
        int i = 0;
        this.ignoreUnsupportedAttributes = false;
        while (i < pKCS9AttributeArr.length) {
            ObjectIdentifier oid = pKCS9AttributeArr[i].getOID();
            if (!this.attributes.containsKey(oid)) {
                this.attributes.put(oid, pKCS9AttributeArr[i]);
                i++;
            } else {
                throw new IllegalArgumentException("PKCSAttribute " + pKCS9AttributeArr[i].getOID() + " duplicated while constructing PKCS9Attributes.");
            }
        }
        this.derEncoding = generateDerEncoding();
        this.permittedAttributes = null;
    }

    private byte[] decode(DerInputStream derInputStream) throws IOException {
        byte[] byteArray = derInputStream.getDerValue().toByteArray();
        byteArray[0] = 49;
        boolean z = true;
        DerValue[] set = new DerInputStream(byteArray).getSet(3, true);
        int i = 0;
        while (i < set.length) {
            try {
                PKCS9Attribute pKCS9Attribute = new PKCS9Attribute(set[i]);
                ObjectIdentifier oid = pKCS9Attribute.getOID();
                if (this.attributes.get(oid) == null) {
                    Hashtable<ObjectIdentifier, ObjectIdentifier> hashtable = this.permittedAttributes;
                    if (hashtable == null || hashtable.containsKey(oid)) {
                        this.attributes.put(oid, pKCS9Attribute);
                        i++;
                    } else {
                        throw new IOException("Attribute " + oid + " not permitted in this attribute set");
                    }
                } else {
                    throw new IOException("Duplicate PKCS9 attribute: " + oid);
                }
            } catch (ParsingException e) {
                if (this.ignoreUnsupportedAttributes) {
                    z = false;
                } else {
                    throw e;
                }
            }
        }
        return z ? byteArray : generateDerEncoding();
    }

    public void encode(byte b, OutputStream outputStream) throws IOException {
        outputStream.write((int) b);
        byte[] bArr = this.derEncoding;
        outputStream.write(bArr, 1, bArr.length - 1);
    }

    private byte[] generateDerEncoding() throws IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putOrderedSetOf((byte) 49, castToDerEncoder(this.attributes.values().toArray()));
        return derOutputStream.toByteArray();
    }

    public byte[] getDerEncoding() throws IOException {
        return (byte[]) this.derEncoding.clone();
    }

    public PKCS9Attribute getAttribute(ObjectIdentifier objectIdentifier) {
        return this.attributes.get(objectIdentifier);
    }

    public PKCS9Attribute getAttribute(String str) {
        return this.attributes.get(PKCS9Attribute.getOID(str));
    }

    public PKCS9Attribute[] getAttributes() {
        int size = this.attributes.size();
        PKCS9Attribute[] pKCS9AttributeArr = new PKCS9Attribute[size];
        int i = 0;
        for (int i2 = 1; i2 < PKCS9Attribute.PKCS9_OIDS.length && i < size; i2++) {
            PKCS9Attribute attribute = getAttribute(PKCS9Attribute.PKCS9_OIDS[i2]);
            pKCS9AttributeArr[i] = attribute;
            if (attribute != null) {
                i++;
            }
        }
        return pKCS9AttributeArr;
    }

    public Object getAttributeValue(ObjectIdentifier objectIdentifier) throws IOException {
        try {
            return getAttribute(objectIdentifier).getValue();
        } catch (NullPointerException unused) {
            throw new IOException("No value found for attribute " + objectIdentifier);
        }
    }

    public Object getAttributeValue(String str) throws IOException {
        ObjectIdentifier oid = PKCS9Attribute.getOID(str);
        if (oid != null) {
            return getAttributeValue(oid);
        }
        throw new IOException("Attribute name " + str + " not recognized or not supported.");
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(200);
        stringBuffer.append("PKCS9 Attributes: [\n\t");
        boolean z = true;
        for (int i = 1; i < PKCS9Attribute.PKCS9_OIDS.length; i++) {
            PKCS9Attribute attribute = getAttribute(PKCS9Attribute.PKCS9_OIDS[i]);
            if (attribute != null) {
                if (z) {
                    z = false;
                } else {
                    stringBuffer.append(";\n\t");
                }
                stringBuffer.append(attribute.toString());
            }
        }
        stringBuffer.append("\n\t] (end PKCS9 Attributes)");
        return stringBuffer.toString();
    }

    static DerEncoder[] castToDerEncoder(Object[] objArr) {
        int length = objArr.length;
        DerEncoder[] derEncoderArr = new DerEncoder[length];
        for (int i = 0; i < length; i++) {
            derEncoderArr[i] = objArr[i];
        }
        return derEncoderArr;
    }
}
