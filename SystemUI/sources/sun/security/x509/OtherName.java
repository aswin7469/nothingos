package sun.security.x509;

import java.p026io.IOException;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class OtherName implements GeneralNameInterface {
    private static final byte TAG_VALUE = 0;
    private GeneralNameInterface gni = null;
    private int myhash = -1;
    private String name;
    private byte[] nameValue = null;
    private ObjectIdentifier oid;

    public int getType() {
        return 0;
    }

    public OtherName(ObjectIdentifier objectIdentifier, byte[] bArr) throws IOException {
        if (objectIdentifier == null || bArr == null) {
            throw new NullPointerException("parameters may not be null");
        }
        this.oid = objectIdentifier;
        this.nameValue = bArr;
        GeneralNameInterface gni2 = getGNI(objectIdentifier, bArr);
        this.gni = gni2;
        if (gni2 != null) {
            this.name = gni2.toString();
            return;
        }
        this.name = "Unrecognized ObjectIdentifier: " + objectIdentifier.toString();
    }

    public OtherName(DerValue derValue) throws IOException {
        DerInputStream derInputStream = derValue.toDerInputStream();
        this.oid = derInputStream.getOID();
        byte[] byteArray = derInputStream.getDerValue().toByteArray();
        this.nameValue = byteArray;
        GeneralNameInterface gni2 = getGNI(this.oid, byteArray);
        this.gni = gni2;
        if (gni2 != null) {
            this.name = gni2.toString();
            return;
        }
        this.name = "Unrecognized ObjectIdentifier: " + this.oid.toString();
    }

    public ObjectIdentifier getOID() {
        return this.oid;
    }

    public byte[] getNameValue() {
        return (byte[]) this.nameValue.clone();
    }

    private GeneralNameInterface getGNI(ObjectIdentifier objectIdentifier, byte[] bArr) throws IOException {
        try {
            Class<?> cls = OIDMap.getClass(objectIdentifier);
            if (cls == null) {
                return null;
            }
            return (GeneralNameInterface) cls.getConstructor(Object.class).newInstance(bArr);
        } catch (Exception e) {
            throw new IOException("Instantiation error: " + e, e);
        }
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        GeneralNameInterface generalNameInterface = this.gni;
        if (generalNameInterface != null) {
            generalNameInterface.encode(derOutputStream);
            return;
        }
        DerOutputStream derOutputStream2 = new DerOutputStream();
        derOutputStream2.putOID(this.oid);
        derOutputStream2.write(DerValue.createTag(Byte.MIN_VALUE, true, (byte) 0), this.nameValue);
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r5) {
        /*
            r4 = this;
            r0 = 1
            if (r4 != r5) goto L_0x0004
            return r0
        L_0x0004:
            boolean r1 = r5 instanceof sun.security.x509.OtherName
            r2 = 0
            if (r1 != 0) goto L_0x000a
            return r2
        L_0x000a:
            sun.security.x509.OtherName r5 = (sun.security.x509.OtherName) r5
            sun.security.util.ObjectIdentifier r1 = r5.oid
            sun.security.util.ObjectIdentifier r3 = r4.oid
            boolean r1 = r1.equals((java.lang.Object) r3)
            if (r1 != 0) goto L_0x0017
            return r2
        L_0x0017:
            sun.security.util.ObjectIdentifier r1 = r5.oid     // Catch:{ IOException -> 0x0033 }
            byte[] r3 = r5.nameValue     // Catch:{ IOException -> 0x0033 }
            sun.security.x509.GeneralNameInterface r1 = r4.getGNI(r1, r3)     // Catch:{ IOException -> 0x0033 }
            if (r1 == 0) goto L_0x002b
            int r4 = r1.constrains(r4)     // Catch:{  }
            if (r4 != 0) goto L_0x0028
            goto L_0x0029
        L_0x0028:
            r0 = r2
        L_0x0029:
            r2 = r0
            goto L_0x0033
        L_0x002b:
            byte[] r4 = r4.nameValue
            byte[] r5 = r5.nameValue
            boolean r2 = java.util.Arrays.equals((byte[]) r4, (byte[]) r5)
        L_0x0033:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.OtherName.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        if (this.myhash == -1) {
            this.myhash = this.oid.hashCode() + 37;
            int i = 0;
            while (true) {
                byte[] bArr = this.nameValue;
                if (i >= bArr.length) {
                    break;
                }
                this.myhash = (this.myhash * 37) + bArr[i];
                i++;
            }
        }
        return this.myhash;
    }

    public String toString() {
        return "Other-Name: " + this.name;
    }

    public int constrains(GeneralNameInterface generalNameInterface) {
        if (generalNameInterface == null || generalNameInterface.getType() != 0) {
            return -1;
        }
        throw new UnsupportedOperationException("Narrowing, widening, and matching are not supported for OtherName.");
    }

    public int subtreeDepth() {
        throw new UnsupportedOperationException("subtreeDepth() not supported for generic OtherName");
    }
}
