package sun.security.x509;

import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class GeneralSubtrees implements Cloneable {
    private static final int NAME_DIFF_TYPE = -1;
    private static final int NAME_MATCH = 0;
    private static final int NAME_NARROWS = 1;
    private static final int NAME_SAME_TYPE = 3;
    private static final int NAME_WIDENS = 2;
    private final List<GeneralSubtree> trees;

    public GeneralSubtrees() {
        this.trees = new ArrayList();
    }

    private GeneralSubtrees(GeneralSubtrees generalSubtrees) {
        this.trees = new ArrayList(generalSubtrees.trees);
    }

    public GeneralSubtrees(DerValue derValue) throws IOException {
        this();
        if (derValue.tag == 48) {
            while (derValue.data.available() != 0) {
                add(new GeneralSubtree(derValue.data.getDerValue()));
            }
            return;
        }
        throw new IOException("Invalid encoding of GeneralSubtrees.");
    }

    public GeneralSubtree get(int i) {
        return this.trees.get(i);
    }

    public void remove(int i) {
        this.trees.remove(i);
    }

    public void add(GeneralSubtree generalSubtree) {
        generalSubtree.getClass();
        this.trees.add(generalSubtree);
    }

    public boolean contains(GeneralSubtree generalSubtree) {
        generalSubtree.getClass();
        return this.trees.contains(generalSubtree);
    }

    public int size() {
        return this.trees.size();
    }

    public Iterator<GeneralSubtree> iterator() {
        return this.trees.iterator();
    }

    public List<GeneralSubtree> trees() {
        return this.trees;
    }

    public Object clone() {
        return new GeneralSubtrees(this);
    }

    public String toString() {
        return "   GeneralSubtrees:\n" + this.trees.toString() + "\n";
    }

    public void encode(DerOutputStream derOutputStream) throws IOException {
        DerOutputStream derOutputStream2 = new DerOutputStream();
        int size = size();
        for (int i = 0; i < size; i++) {
            get(i).encode(derOutputStream2);
        }
        derOutputStream.write((byte) 48, derOutputStream2);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GeneralSubtrees)) {
            return false;
        }
        return this.trees.equals(((GeneralSubtrees) obj).trees);
    }

    public int hashCode() {
        return this.trees.hashCode();
    }

    private GeneralNameInterface getGeneralNameInterface(int i) {
        return getGeneralNameInterface(get(i));
    }

    private static GeneralNameInterface getGeneralNameInterface(GeneralSubtree generalSubtree) {
        return generalSubtree.getName().getName();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0032, code lost:
        r2 = true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void minimize() {
        /*
            r7 = this;
            r0 = 0
            r1 = r0
        L_0x0002:
            int r2 = r7.size()
            r3 = 1
            int r2 = r2 - r3
            if (r1 >= r2) goto L_0x0040
            sun.security.x509.GeneralNameInterface r2 = r7.getGeneralNameInterface((int) r1)
            int r4 = r1 + 1
        L_0x0010:
            int r5 = r7.size()
            if (r4 >= r5) goto L_0x0036
            sun.security.x509.GeneralNameInterface r5 = r7.getGeneralNameInterface((int) r4)
            int r5 = r2.constrains(r5)
            r6 = -1
            if (r5 == r6) goto L_0x0034
            if (r5 == 0) goto L_0x0032
            if (r5 == r3) goto L_0x002c
            r6 = 2
            if (r5 == r6) goto L_0x0032
            r6 = 3
            if (r5 == r6) goto L_0x0034
            goto L_0x0036
        L_0x002c:
            r7.remove(r4)
            int r4 = r4 + -1
            goto L_0x0034
        L_0x0032:
            r2 = r3
            goto L_0x0037
        L_0x0034:
            int r4 = r4 + r3
            goto L_0x0010
        L_0x0036:
            r2 = r0
        L_0x0037:
            if (r2 == 0) goto L_0x003e
            r7.remove(r1)
            int r1 = r1 + -1
        L_0x003e:
            int r1 = r1 + r3
            goto L_0x0002
        L_0x0040:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.security.x509.GeneralSubtrees.minimize():void");
    }

    private GeneralSubtree createWidestSubtree(GeneralNameInterface generalNameInterface) {
        GeneralName generalName;
        try {
            switch (generalNameInterface.getType()) {
                case 0:
                    generalName = new GeneralName((GeneralNameInterface) new OtherName(((OtherName) generalNameInterface).getOID(), (byte[]) null));
                    break;
                case 1:
                    generalName = new GeneralName((GeneralNameInterface) new RFC822Name(""));
                    break;
                case 2:
                    generalName = new GeneralName((GeneralNameInterface) new DNSName(""));
                    break;
                case 3:
                    byte[] bArr = null;
                    generalName = new GeneralName((GeneralNameInterface) new X400Address((byte[]) null));
                    break;
                case 4:
                    generalName = new GeneralName((GeneralNameInterface) new X500Name(""));
                    break;
                case 5:
                    generalName = new GeneralName((GeneralNameInterface) new EDIPartyName(""));
                    break;
                case 6:
                    generalName = new GeneralName((GeneralNameInterface) new URIName(""));
                    break;
                case 7:
                    byte[] bArr2 = null;
                    generalName = new GeneralName((GeneralNameInterface) new IPAddressName((byte[]) null));
                    break;
                case 8:
                    int[] iArr = null;
                    generalName = new GeneralName((GeneralNameInterface) new OIDName(new ObjectIdentifier((int[]) null)));
                    break;
                default:
                    throw new IOException("Unsupported GeneralNameInterface type: " + generalNameInterface.getType());
            }
            return new GeneralSubtree(generalName, 0, -1);
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error: " + e, e);
        }
    }

    public GeneralSubtrees intersect(GeneralSubtrees generalSubtrees) {
        if (generalSubtrees != null) {
            GeneralSubtrees generalSubtrees2 = new GeneralSubtrees();
            GeneralSubtrees generalSubtrees3 = null;
            if (size() == 0) {
                union(generalSubtrees);
                return null;
            }
            minimize();
            generalSubtrees.minimize();
            int i = 0;
            while (i < size()) {
                GeneralNameInterface generalNameInterface = getGeneralNameInterface(i);
                int i2 = 0;
                boolean z = false;
                while (true) {
                    if (i2 >= generalSubtrees.size()) {
                        break;
                    }
                    GeneralSubtree generalSubtree = generalSubtrees.get(i2);
                    int constrains = generalNameInterface.constrains(getGeneralNameInterface(generalSubtree));
                    if (constrains != 0) {
                        if (constrains != 1) {
                            if (constrains == 2) {
                                break;
                            }
                            if (constrains == 3) {
                                z = true;
                            }
                            i2++;
                        } else {
                            remove(i);
                            i--;
                            generalSubtrees2.add(generalSubtree);
                            break;
                        }
                    } else {
                        break;
                    }
                }
                z = false;
                if (z) {
                    boolean z2 = false;
                    for (int i3 = 0; i3 < size(); i3++) {
                        GeneralNameInterface generalNameInterface2 = getGeneralNameInterface(i3);
                        if (generalNameInterface2.getType() == generalNameInterface.getType()) {
                            int i4 = 0;
                            while (true) {
                                if (i4 >= generalSubtrees.size()) {
                                    break;
                                }
                                int constrains2 = generalNameInterface2.constrains(generalSubtrees.getGeneralNameInterface(i4));
                                if (constrains2 == 0 || constrains2 == 2 || constrains2 == 1) {
                                    z2 = true;
                                } else {
                                    i4++;
                                }
                            }
                        }
                    }
                    if (!z2) {
                        if (generalSubtrees3 == null) {
                            generalSubtrees3 = new GeneralSubtrees();
                        }
                        GeneralSubtree createWidestSubtree = createWidestSubtree(generalNameInterface);
                        if (!generalSubtrees3.contains(createWidestSubtree)) {
                            generalSubtrees3.add(createWidestSubtree);
                        }
                    }
                    remove(i);
                    i--;
                }
                i++;
            }
            if (generalSubtrees2.size() > 0) {
                union(generalSubtrees2);
            }
            for (int i5 = 0; i5 < generalSubtrees.size(); i5++) {
                GeneralSubtree generalSubtree2 = generalSubtrees.get(i5);
                GeneralNameInterface generalNameInterface3 = getGeneralNameInterface(generalSubtree2);
                int i6 = 0;
                boolean z3 = false;
                while (true) {
                    if (i6 >= size()) {
                        break;
                    }
                    int constrains3 = getGeneralNameInterface(i6).constrains(generalNameInterface3);
                    if (constrains3 == -1) {
                        z3 = true;
                    } else if (constrains3 == 0 || constrains3 == 1 || constrains3 == 2 || constrains3 == 3) {
                        z3 = false;
                    }
                    i6++;
                }
                z3 = false;
                if (z3) {
                    add(generalSubtree2);
                }
            }
            return generalSubtrees3;
        }
        throw new NullPointerException("other GeneralSubtrees must not be null");
    }

    public void union(GeneralSubtrees generalSubtrees) {
        if (generalSubtrees != null) {
            int size = generalSubtrees.size();
            for (int i = 0; i < size; i++) {
                add(generalSubtrees.get(i));
            }
            minimize();
        }
    }

    public void reduce(GeneralSubtrees generalSubtrees) {
        if (generalSubtrees != null) {
            int size = generalSubtrees.size();
            for (int i = 0; i < size; i++) {
                GeneralNameInterface generalNameInterface = generalSubtrees.getGeneralNameInterface(i);
                int i2 = 0;
                while (i2 < size()) {
                    int constrains = generalNameInterface.constrains(getGeneralNameInterface(i2));
                    if (constrains == 0) {
                        remove(i2);
                    } else if (constrains != 1) {
                        i2++;
                    } else {
                        remove(i2);
                    }
                    i2--;
                    i2++;
                }
            }
        }
    }
}
