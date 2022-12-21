package sun.security.x509;

import java.p026io.IOException;
import java.p026io.Reader;
import java.p026io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.security.auth.x500.X500Principal;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;
import sun.security.util.ObjectIdentifier;

public class RDN {
    final AVA[] assertion;
    private volatile List<AVA> avaList;
    private volatile String canonicalString;

    public RDN(String str) throws IOException {
        this(str, (Map<String, String>) Collections.emptyMap());
    }

    public RDN(String str, Map<String, String> map) throws IOException {
        ArrayList arrayList = new ArrayList(3);
        int indexOf = str.indexOf(43);
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (indexOf >= 0) {
            i2 += X500Name.countQuotes(str, i3, indexOf);
            if (!(indexOf <= 0 || str.charAt(indexOf - 1) == '\\' || i2 == 1)) {
                String substring = str.substring(i, indexOf);
                if (substring.length() != 0) {
                    arrayList.add(new AVA((Reader) new StringReader(substring), map));
                    i = indexOf + 1;
                    i2 = 0;
                } else {
                    throw new IOException("empty AVA in RDN \"" + str + "\"");
                }
            }
            i3 = indexOf + 1;
            indexOf = str.indexOf(43, i3);
        }
        String substring2 = str.substring(i);
        if (substring2.length() != 0) {
            arrayList.add(new AVA((Reader) new StringReader(substring2), map));
            this.assertion = (AVA[]) arrayList.toArray(new AVA[arrayList.size()]);
            return;
        }
        throw new IOException("empty AVA in RDN \"" + str + "\"");
    }

    RDN(String str, String str2) throws IOException {
        this(str, str2, Collections.emptyMap());
    }

    RDN(String str, String str2, Map<String, String> map) throws IOException {
        if (str2.equalsIgnoreCase(X500Principal.RFC2253)) {
            ArrayList arrayList = new ArrayList(3);
            int indexOf = str.indexOf(43);
            int i = 0;
            while (indexOf >= 0) {
                if (indexOf > 0 && str.charAt(indexOf - 1) != '\\') {
                    String substring = str.substring(i, indexOf);
                    if (substring.length() != 0) {
                        arrayList.add(new AVA(new StringReader(substring), 3, map));
                        i = indexOf + 1;
                    } else {
                        throw new IOException("empty AVA in RDN \"" + str + "\"");
                    }
                }
                indexOf = str.indexOf(43, indexOf + 1);
            }
            String substring2 = str.substring(i);
            if (substring2.length() != 0) {
                arrayList.add(new AVA(new StringReader(substring2), 3, map));
                this.assertion = (AVA[]) arrayList.toArray(new AVA[arrayList.size()]);
                return;
            }
            throw new IOException("empty AVA in RDN \"" + str + "\"");
        }
        throw new IOException("Unsupported format " + str2);
    }

    RDN(DerValue derValue) throws IOException {
        if (derValue.tag == 49) {
            DerValue[] set = new DerInputStream(derValue.toByteArray()).getSet(5);
            this.assertion = new AVA[set.length];
            for (int i = 0; i < set.length; i++) {
                this.assertion[i] = new AVA(set[i]);
            }
            return;
        }
        throw new IOException("X500 RDN");
    }

    RDN(int i) {
        this.assertion = new AVA[i];
    }

    public RDN(AVA ava) {
        ava.getClass();
        this.assertion = new AVA[]{ava};
    }

    public RDN(AVA[] avaArr) {
        this.assertion = (AVA[]) avaArr.clone();
        int i = 0;
        while (true) {
            AVA[] avaArr2 = this.assertion;
            if (i < avaArr2.length) {
                avaArr2[i].getClass();
                i++;
            } else {
                return;
            }
        }
    }

    public List<AVA> avas() {
        List<AVA> list = this.avaList;
        if (list != null) {
            return list;
        }
        List<AVA> unmodifiableList = Collections.unmodifiableList(Arrays.asList(this.assertion));
        this.avaList = unmodifiableList;
        return unmodifiableList;
    }

    public int size() {
        return this.assertion.length;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RDN)) {
            return false;
        }
        RDN rdn = (RDN) obj;
        if (this.assertion.length != rdn.assertion.length) {
            return false;
        }
        return toRFC2253String(true).equals(rdn.toRFC2253String(true));
    }

    public int hashCode() {
        return toRFC2253String(true).hashCode();
    }

    /* access modifiers changed from: package-private */
    public DerValue findAttribute(ObjectIdentifier objectIdentifier) {
        int i = 0;
        while (true) {
            AVA[] avaArr = this.assertion;
            if (i >= avaArr.length) {
                return null;
            }
            if (avaArr[i].oid.equals((Object) objectIdentifier)) {
                return this.assertion[i].value;
            }
            i++;
        }
    }

    /* access modifiers changed from: package-private */
    public void encode(DerOutputStream derOutputStream) throws IOException {
        derOutputStream.putOrderedSetOf((byte) 49, this.assertion);
    }

    public String toString() {
        AVA[] avaArr = this.assertion;
        if (avaArr.length == 1) {
            return avaArr[0].toString();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.assertion.length; i++) {
            if (i != 0) {
                sb.append(" + ");
            }
            sb.append(this.assertion[i].toString());
        }
        return sb.toString();
    }

    public String toRFC1779String() {
        return toRFC1779String(Collections.emptyMap());
    }

    public String toRFC1779String(Map<String, String> map) {
        AVA[] avaArr = this.assertion;
        if (avaArr.length == 1) {
            return avaArr[0].toRFC1779String(map);
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.assertion.length; i++) {
            if (i != 0) {
                sb.append(" + ");
            }
            sb.append(this.assertion[i].toRFC1779String(map));
        }
        return sb.toString();
    }

    public String toRFC2253String() {
        return toRFC2253StringInternal(false, Collections.emptyMap());
    }

    public String toRFC2253String(Map<String, String> map) {
        return toRFC2253StringInternal(false, map);
    }

    public String toRFC2253String(boolean z) {
        if (!z) {
            return toRFC2253StringInternal(false, Collections.emptyMap());
        }
        String str = this.canonicalString;
        if (str != null) {
            return str;
        }
        String rFC2253StringInternal = toRFC2253StringInternal(true, Collections.emptyMap());
        this.canonicalString = rFC2253StringInternal;
        return rFC2253StringInternal;
    }

    private String toRFC2253StringInternal(boolean z, Map<String, String> map) {
        String str;
        AVA[] avaArr = this.assertion;
        if (avaArr.length != 1) {
            if (z) {
                avaArr = (AVA[]) avaArr.clone();
                Arrays.sort(avaArr, AVAComparator.getInstance());
            }
            StringJoiner stringJoiner = new StringJoiner("+");
            for (AVA ava : avaArr) {
                if (z) {
                    str = ava.toRFC2253CanonicalString();
                } else {
                    str = ava.toRFC2253String(map);
                }
                stringJoiner.add(str);
            }
            return stringJoiner.toString();
        } else if (z) {
            return avaArr[0].toRFC2253CanonicalString();
        } else {
            return avaArr[0].toRFC2253String(map);
        }
    }
}
