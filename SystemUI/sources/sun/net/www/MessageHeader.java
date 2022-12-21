package sun.net.www;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

public class MessageHeader {
    /* access modifiers changed from: private */
    public String[] keys;
    /* access modifiers changed from: private */
    public int nkeys;
    /* access modifiers changed from: private */
    public String[] values;

    public MessageHeader() {
        grow();
    }

    public MessageHeader(InputStream inputStream) throws IOException {
        parseHeader(inputStream);
    }

    public synchronized String getHeaderNamesInList() {
        StringJoiner stringJoiner;
        stringJoiner = new StringJoiner(NavigationBarInflaterView.BUTTON_SEPARATOR);
        for (int i = 0; i < this.nkeys; i++) {
            stringJoiner.add(this.keys[i]);
        }
        return stringJoiner.toString();
    }

    public synchronized void reset() {
        this.keys = null;
        this.values = null;
        this.nkeys = 0;
        grow();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x002c, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String findValue(java.lang.String r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 != 0) goto L_0x0015
            int r3 = r2.nkeys     // Catch:{ all -> 0x002e }
        L_0x0005:
            int r3 = r3 + -1
            if (r3 < 0) goto L_0x002b
            java.lang.String[] r0 = r2.keys     // Catch:{ all -> 0x002e }
            r0 = r0[r3]     // Catch:{ all -> 0x002e }
            if (r0 != 0) goto L_0x0005
            java.lang.String[] r0 = r2.values     // Catch:{ all -> 0x002e }
            r3 = r0[r3]     // Catch:{ all -> 0x002e }
            monitor-exit(r2)
            return r3
        L_0x0015:
            int r0 = r2.nkeys     // Catch:{ all -> 0x002e }
        L_0x0017:
            int r0 = r0 + -1
            if (r0 < 0) goto L_0x002b
            java.lang.String[] r1 = r2.keys     // Catch:{ all -> 0x002e }
            r1 = r1[r0]     // Catch:{ all -> 0x002e }
            boolean r1 = r3.equalsIgnoreCase(r1)     // Catch:{ all -> 0x002e }
            if (r1 == 0) goto L_0x0017
            java.lang.String[] r3 = r2.values     // Catch:{ all -> 0x002e }
            r3 = r3[r0]     // Catch:{ all -> 0x002e }
            monitor-exit(r2)
            return r3
        L_0x002b:
            monitor-exit(r2)
            r2 = 0
            return r2
        L_0x002e:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MessageHeader.findValue(java.lang.String):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0016, code lost:
        return r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int getKey(java.lang.String r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            int r0 = r2.nkeys     // Catch:{ all -> 0x0019 }
        L_0x0003:
            r1 = -1
            int r0 = r0 + r1
            if (r0 < 0) goto L_0x0017
            java.lang.String[] r1 = r2.keys     // Catch:{ all -> 0x0019 }
            r1 = r1[r0]     // Catch:{ all -> 0x0019 }
            if (r1 == r3) goto L_0x0015
            if (r3 == 0) goto L_0x0003
            boolean r1 = r3.equalsIgnoreCase(r1)     // Catch:{ all -> 0x0019 }
            if (r1 == 0) goto L_0x0003
        L_0x0015:
            monitor-exit(r2)
            return r0
        L_0x0017:
            monitor-exit(r2)
            return r1
        L_0x0019:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MessageHeader.getKey(java.lang.String):int");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0012, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getKey(int r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 < 0) goto L_0x0011
            int r0 = r1.nkeys     // Catch:{ all -> 0x000e }
            if (r2 < r0) goto L_0x0008
            goto L_0x0011
        L_0x0008:
            java.lang.String[] r0 = r1.keys     // Catch:{ all -> 0x000e }
            r2 = r0[r2]     // Catch:{ all -> 0x000e }
            monitor-exit(r1)
            return r2
        L_0x000e:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        L_0x0011:
            monitor-exit(r1)
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MessageHeader.getKey(int):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0012, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String getValue(int r2) {
        /*
            r1 = this;
            monitor-enter(r1)
            if (r2 < 0) goto L_0x0011
            int r0 = r1.nkeys     // Catch:{ all -> 0x000e }
            if (r2 < r0) goto L_0x0008
            goto L_0x0011
        L_0x0008:
            java.lang.String[] r0 = r1.values     // Catch:{ all -> 0x000e }
            r2 = r0[r2]     // Catch:{ all -> 0x000e }
            monitor-exit(r1)
            return r2
        L_0x000e:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        L_0x0011:
            monitor-exit(r1)
            r1 = 0
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MessageHeader.getValue(int):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0042, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String findNextValue(java.lang.String r5, java.lang.String r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            r0 = 1
            r1 = 0
            if (r5 != 0) goto L_0x0021
            int r5 = r4.nkeys     // Catch:{ all -> 0x0044 }
        L_0x0007:
            int r5 = r5 + -1
            if (r5 < 0) goto L_0x0041
            java.lang.String[] r2 = r4.keys     // Catch:{ all -> 0x0044 }
            r2 = r2[r5]     // Catch:{ all -> 0x0044 }
            if (r2 != 0) goto L_0x0007
            if (r1 == 0) goto L_0x0019
            java.lang.String[] r6 = r4.values     // Catch:{ all -> 0x0044 }
            r5 = r6[r5]     // Catch:{ all -> 0x0044 }
            monitor-exit(r4)
            return r5
        L_0x0019:
            java.lang.String[] r2 = r4.values     // Catch:{ all -> 0x0044 }
            r2 = r2[r5]     // Catch:{ all -> 0x0044 }
            if (r2 != r6) goto L_0x0007
            r1 = r0
            goto L_0x0007
        L_0x0021:
            int r2 = r4.nkeys     // Catch:{ all -> 0x0044 }
        L_0x0023:
            int r2 = r2 + -1
            if (r2 < 0) goto L_0x0041
            java.lang.String[] r3 = r4.keys     // Catch:{ all -> 0x0044 }
            r3 = r3[r2]     // Catch:{ all -> 0x0044 }
            boolean r3 = r5.equalsIgnoreCase(r3)     // Catch:{ all -> 0x0044 }
            if (r3 == 0) goto L_0x0023
            if (r1 == 0) goto L_0x0039
            java.lang.String[] r5 = r4.values     // Catch:{ all -> 0x0044 }
            r5 = r5[r2]     // Catch:{ all -> 0x0044 }
            monitor-exit(r4)
            return r5
        L_0x0039:
            java.lang.String[] r3 = r4.values     // Catch:{ all -> 0x0044 }
            r3 = r3[r2]     // Catch:{ all -> 0x0044 }
            if (r3 != r6) goto L_0x0023
            r1 = r0
            goto L_0x0023
        L_0x0041:
            monitor-exit(r4)
            r4 = 0
            return r4
        L_0x0044:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MessageHeader.findNextValue(java.lang.String, java.lang.String):java.lang.String");
    }

    public boolean filterNTLMResponses(String str) {
        boolean z;
        int i;
        String str2;
        int i2 = 0;
        while (true) {
            if (i2 < this.nkeys) {
                if (str.equalsIgnoreCase(this.keys[i2]) && (str2 = this.values[i2]) != null && str2.length() > 5 && this.values[i2].substring(0, 5).equalsIgnoreCase("NTLM ")) {
                    z = true;
                    break;
                }
                i2++;
            } else {
                z = false;
                break;
            }
        }
        if (z) {
            int i3 = 0;
            int i4 = 0;
            while (true) {
                i = this.nkeys;
                if (i3 >= i) {
                    break;
                }
                if (!str.equalsIgnoreCase(this.keys[i3]) || (!"Negotiate".equalsIgnoreCase(this.values[i3]) && !"Kerberos".equalsIgnoreCase(this.values[i3]))) {
                    if (i3 != i4) {
                        String[] strArr = this.keys;
                        strArr[i4] = strArr[i3];
                        String[] strArr2 = this.values;
                        strArr2[i4] = strArr2[i3];
                    }
                    i4++;
                }
                i3++;
            }
            if (i4 != i) {
                this.nkeys = i4;
                return true;
            }
        }
        return false;
    }

    class HeaderIterator implements Iterator<String> {
        boolean haveNext = false;
        int index = 0;
        String key;
        Object lock;
        int next = -1;

        public HeaderIterator(String str, Object obj) {
            this.key = str;
            this.lock = obj;
        }

        public boolean hasNext() {
            synchronized (this.lock) {
                if (this.haveNext) {
                    return true;
                }
                while (this.index < MessageHeader.this.nkeys) {
                    if (this.key.equalsIgnoreCase(MessageHeader.this.keys[this.index])) {
                        this.haveNext = true;
                        int i = this.index;
                        this.index = i + 1;
                        this.next = i;
                        return true;
                    }
                    this.index++;
                }
                return false;
            }
        }

        public String next() {
            synchronized (this.lock) {
                if (this.haveNext) {
                    this.haveNext = false;
                    String str = MessageHeader.this.values[this.next];
                    return str;
                } else if (hasNext()) {
                    String next2 = next();
                    return next2;
                } else {
                    throw new NoSuchElementException("No more elements");
                }
            }
        }

        public void remove() {
            throw new UnsupportedOperationException("remove not allowed");
        }
    }

    public Iterator<String> multiValueIterator(String str) {
        return new HeaderIterator(str, this);
    }

    public synchronized Map<String, List<String>> getHeaders() {
        return getHeaders((String[]) null);
    }

    public synchronized Map<String, List<String>> getHeaders(String[] strArr) {
        return filterAndAddHeaders(strArr, (Map<String, List<String>>) null);
    }

    public synchronized Map<String, List<String>> filterAndAddHeaders(String[] strArr, Map<String, List<String>> map) {
        HashMap hashMap;
        hashMap = new HashMap();
        int i = this.nkeys;
        loop0:
        while (true) {
            boolean z = false;
            while (true) {
                i--;
                if (i < 0) {
                    break loop0;
                }
                if (strArr != null) {
                    int i2 = 0;
                    while (true) {
                        if (i2 < strArr.length) {
                            String str = strArr[i2];
                            if (str != null && str.equalsIgnoreCase(this.keys[i])) {
                                z = true;
                                break;
                            }
                            i2++;
                        } else {
                            break;
                        }
                    }
                }
                if (!z) {
                    List list = (List) hashMap.get(this.keys[i]);
                    if (list == null) {
                        list = new ArrayList();
                        hashMap.put(this.keys[i], list);
                    }
                    list.add(this.values[i]);
                }
            }
        }
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                List list2 = (List) hashMap.get(next.getKey());
                if (list2 == null) {
                    list2 = new ArrayList();
                    hashMap.put((String) next.getKey(), list2);
                }
                list2.addAll((Collection) next.getValue());
            }
        }
        for (String str2 : hashMap.keySet()) {
            hashMap.put(str2, Collections.unmodifiableList((List) hashMap.get(str2)));
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public synchronized void print(PrintStream printStream) {
        String str;
        for (int i = 0; i < this.nkeys; i++) {
            if (this.keys[i] != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(this.keys[i]);
                if (this.values[i] != null) {
                    str = ": " + this.values[i];
                } else {
                    str = "";
                }
                sb.append(str);
                sb.append("\r\n");
                printStream.print(sb.toString());
            }
        }
        printStream.print("\r\n");
        printStream.flush();
    }

    public synchronized void add(String str, String str2) {
        grow();
        String[] strArr = this.keys;
        int i = this.nkeys;
        strArr[i] = str;
        this.values[i] = str2;
        this.nkeys = i + 1;
    }

    public synchronized void prepend(String str, String str2) {
        grow();
        for (int i = this.nkeys; i > 0; i--) {
            String[] strArr = this.keys;
            int i2 = i - 1;
            strArr[i] = strArr[i2];
            String[] strArr2 = this.values;
            strArr2[i] = strArr2[i2];
        }
        this.keys[0] = str;
        this.values[0] = str2;
        this.nkeys++;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0019, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void set(int r2, java.lang.String r3, java.lang.String r4) {
        /*
            r1 = this;
            monitor-enter(r1)
            r1.grow()     // Catch:{ all -> 0x001a }
            if (r2 >= 0) goto L_0x0008
            monitor-exit(r1)
            return
        L_0x0008:
            int r0 = r1.nkeys     // Catch:{ all -> 0x001a }
            if (r2 < r0) goto L_0x0010
            r1.add(r3, r4)     // Catch:{ all -> 0x001a }
            goto L_0x0018
        L_0x0010:
            java.lang.String[] r0 = r1.keys     // Catch:{ all -> 0x001a }
            r0[r2] = r3     // Catch:{ all -> 0x001a }
            java.lang.String[] r3 = r1.values     // Catch:{ all -> 0x001a }
            r3[r2] = r4     // Catch:{ all -> 0x001a }
        L_0x0018:
            monitor-exit(r1)
            return
        L_0x001a:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MessageHeader.set(int, java.lang.String, java.lang.String):void");
    }

    private void grow() {
        String[] strArr = this.keys;
        if (strArr == null || this.nkeys >= strArr.length) {
            int i = this.nkeys;
            String[] strArr2 = new String[(i + 4)];
            String[] strArr3 = new String[(i + 4)];
            if (strArr != null) {
                System.arraycopy((Object) strArr, 0, (Object) strArr2, 0, i);
            }
            String[] strArr4 = this.values;
            if (strArr4 != null) {
                System.arraycopy((Object) strArr4, 0, (Object) strArr3, 0, this.nkeys);
            }
            this.keys = strArr2;
            this.values = strArr3;
        }
    }

    public synchronized void remove(String str) {
        int i;
        int i2;
        int i3 = 0;
        if (str == null) {
            while (i3 < this.nkeys) {
                while (this.keys[i3] == null && i3 < this.nkeys) {
                    int i4 = i3;
                    while (true) {
                        i2 = this.nkeys;
                        if (i4 >= i2 - 1) {
                            break;
                        }
                        String[] strArr = this.keys;
                        int i5 = i4 + 1;
                        strArr[i4] = strArr[i5];
                        String[] strArr2 = this.values;
                        strArr2[i4] = strArr2[i5];
                        i4 = i5;
                    }
                    this.nkeys = i2 - 1;
                }
                i3++;
            }
        } else {
            while (i3 < this.nkeys) {
                while (str.equalsIgnoreCase(this.keys[i3]) && i3 < this.nkeys) {
                    int i6 = i3;
                    while (true) {
                        i = this.nkeys;
                        if (i6 >= i - 1) {
                            break;
                        }
                        String[] strArr3 = this.keys;
                        int i7 = i6 + 1;
                        strArr3[i6] = strArr3[i7];
                        String[] strArr4 = this.values;
                        strArr4[i6] = strArr4[i7];
                        i6 = i7;
                    }
                    this.nkeys = i - 1;
                }
                i3++;
            }
        }
    }

    public synchronized void set(String str, String str2) {
        int i = this.nkeys;
        do {
            i--;
            if (i < 0) {
                add(str, str2);
                return;
            }
        } while (!str.equalsIgnoreCase(this.keys[i]));
        this.values[i] = str2;
    }

    public synchronized void setIfNotSet(String str, String str2) {
        if (findValue(str) == null) {
            add(str, str2);
        }
    }

    public static String canonicalID(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        int i = 0;
        boolean z = false;
        while (i < length && ((r5 = str.charAt(i)) == '<' || r5 <= ' ')) {
            i++;
            z = true;
        }
        while (i < length) {
            char charAt = str.charAt(length - 1);
            if (charAt != '>' && charAt > ' ') {
                break;
            }
            length--;
            z = true;
        }
        return z ? str.substring(i, length) : str;
    }

    public void parseHeader(InputStream inputStream) throws IOException {
        synchronized (this) {
            this.nkeys = 0;
        }
        mergeHeader(inputStream);
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0067 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void mergeHeader(java.p026io.InputStream r13) throws java.p026io.IOException {
        /*
            r12 = this;
            if (r13 != 0) goto L_0x0003
            return
        L_0x0003:
            r0 = 10
            char[] r1 = new char[r0]
            int r2 = r13.read()
        L_0x000b:
            if (r2 == r0) goto L_0x00a5
            r3 = 13
            if (r2 == r3) goto L_0x00a5
            if (r2 < 0) goto L_0x00a5
            r4 = 1
            r5 = 32
            r6 = 0
            if (r2 <= r5) goto L_0x001b
            r7 = r4
            goto L_0x001c
        L_0x001b:
            r7 = r6
        L_0x001c:
            char r2 = (char) r2
            r1[r6] = r2
            r2 = -1
            r8 = r2
        L_0x0021:
            int r9 = r13.read()
            r10 = 58
            if (r9 < 0) goto L_0x006e
            r11 = 9
            if (r9 == r11) goto L_0x0059
            if (r9 == r0) goto L_0x003c
            if (r9 == r3) goto L_0x003c
            if (r9 == r5) goto L_0x005a
            if (r9 == r10) goto L_0x0036
            goto L_0x005b
        L_0x0036:
            if (r7 == 0) goto L_0x005a
            if (r4 <= 0) goto L_0x005a
            r8 = r4
            goto L_0x005a
        L_0x003c:
            int r11 = r13.read()
            if (r9 != r3) goto L_0x004e
            if (r11 != r0) goto L_0x004e
            int r11 = r13.read()
            if (r11 != r3) goto L_0x004e
            int r11 = r13.read()
        L_0x004e:
            if (r11 == r0) goto L_0x0057
            if (r11 == r3) goto L_0x0057
            if (r11 <= r5) goto L_0x0055
            goto L_0x0057
        L_0x0055:
            r9 = r5
            goto L_0x005b
        L_0x0057:
            r2 = r11
            goto L_0x006e
        L_0x0059:
            r9 = r5
        L_0x005a:
            r7 = r6
        L_0x005b:
            int r10 = r1.length
            if (r4 < r10) goto L_0x0067
            int r10 = r1.length
            int r10 = r10 * 2
            char[] r10 = new char[r10]
            java.lang.System.arraycopy((java.lang.Object) r1, (int) r6, (java.lang.Object) r10, (int) r6, (int) r4)
            r1 = r10
        L_0x0067:
            int r10 = r4 + 1
            char r9 = (char) r9
            r1[r4] = r9
            r4 = r10
            goto L_0x0021
        L_0x006e:
            if (r4 <= 0) goto L_0x0079
            int r3 = r4 + -1
            char r3 = r1[r3]
            if (r3 > r5) goto L_0x0079
            int r4 = r4 + -1
            goto L_0x006e
        L_0x0079:
            if (r8 > 0) goto L_0x007d
            r3 = 0
            goto L_0x0093
        L_0x007d:
            java.lang.String r3 = java.lang.String.copyValueOf(r1, r6, r8)
            if (r8 >= r4) goto L_0x0089
            char r6 = r1[r8]
            if (r6 != r10) goto L_0x0089
            int r8 = r8 + 1
        L_0x0089:
            r6 = r8
        L_0x008a:
            if (r6 >= r4) goto L_0x0093
            char r7 = r1[r6]
            if (r7 > r5) goto L_0x0093
            int r6 = r6 + 1
            goto L_0x008a
        L_0x0093:
            if (r6 < r4) goto L_0x009b
            java.lang.String r4 = new java.lang.String
            r4.<init>()
            goto L_0x00a0
        L_0x009b:
            int r4 = r4 - r6
            java.lang.String r4 = java.lang.String.copyValueOf(r1, r6, r4)
        L_0x00a0:
            r12.add(r3, r4)
            goto L_0x000b
        L_0x00a5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.www.MessageHeader.mergeHeader(java.io.InputStream):void");
    }

    public synchronized String toString() {
        String str;
        str = super.toString() + this.nkeys + " pairs: ";
        int i = 0;
        while (i < this.keys.length && i < this.nkeys) {
            str = str + "{" + this.keys[i] + ": " + this.values[i] + "}";
            i++;
        }
        return str;
    }
}
