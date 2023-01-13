package sun.misc;

import java.p026io.PrintStream;

public class RegexpPool {
    private static final int BIG = Integer.MAX_VALUE;
    private int lastDepth = Integer.MAX_VALUE;
    private RegexpNode prefixMachine = new RegexpNode();
    private RegexpNode suffixMachine = new RegexpNode();

    public void add(String str, Object obj) throws REException {
        add(str, obj, false);
    }

    public void replace(String str, Object obj) {
        try {
            add(str, obj, true);
        } catch (Exception unused) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x005e  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x006b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object delete(java.lang.String r10) {
        /*
            r9 = this;
            sun.misc.RegexpNode r0 = r9.prefixMachine
            int r1 = r10.length()
            r2 = 1
            int r1 = r1 - r2
            java.lang.String r3 = "*"
            boolean r4 = r10.startsWith(r3)
            if (r4 == 0) goto L_0x0016
            boolean r3 = r10.endsWith(r3)
            if (r3 != 0) goto L_0x0018
        L_0x0016:
            int r1 = r1 + 1
        L_0x0018:
            r3 = 0
            if (r1 > 0) goto L_0x001c
            return r3
        L_0x001c:
            r4 = 0
            r5 = r0
            r6 = r4
        L_0x001f:
            r7 = 2147483647(0x7fffffff, float:NaN)
            if (r0 == 0) goto L_0x0041
            java.lang.Object r8 = r0.result
            if (r8 == 0) goto L_0x0033
            int r8 = r0.depth
            if (r8 >= r7) goto L_0x0033
            boolean r8 = r0.exact
            if (r8 == 0) goto L_0x0032
            if (r6 != r1) goto L_0x0033
        L_0x0032:
            r5 = r0
        L_0x0033:
            if (r6 < r1) goto L_0x0036
            goto L_0x0041
        L_0x0036:
            char r7 = r10.charAt(r6)
            sun.misc.RegexpNode r0 = r0.find(r7)
            int r6 = r6 + 1
            goto L_0x001f
        L_0x0041:
            sun.misc.RegexpNode r9 = r9.suffixMachine
        L_0x0043:
            int r1 = r1 + -1
            if (r1 < 0) goto L_0x005c
            if (r9 == 0) goto L_0x005c
            java.lang.Object r0 = r9.result
            if (r0 == 0) goto L_0x0053
            int r0 = r9.depth
            if (r0 >= r7) goto L_0x0053
            r5 = r9
            r2 = r4
        L_0x0053:
            char r0 = r10.charAt(r1)
            sun.misc.RegexpNode r9 = r9.find(r0)
            goto L_0x0043
        L_0x005c:
            if (r2 == 0) goto L_0x006b
            java.lang.String r9 = r5.f860re
            boolean r9 = r10.equals(r9)
            if (r9 == 0) goto L_0x0078
            java.lang.Object r9 = r5.result
            r5.result = r3
            goto L_0x0077
        L_0x006b:
            java.lang.String r9 = r5.f860re
            boolean r9 = r10.equals(r9)
            if (r9 == 0) goto L_0x0078
            java.lang.Object r9 = r5.result
            r5.result = r3
        L_0x0077:
            r3 = r9
        L_0x0078:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.RegexpPool.delete(java.lang.String):java.lang.Object");
    }

    public Object match(String str) {
        return matchAfter(str, Integer.MAX_VALUE);
    }

    public Object matchNext(String str) {
        return matchAfter(str, this.lastDepth);
    }

    private void add(String str, Object obj, boolean z) throws REException {
        RegexpNode regexpNode;
        int length = str.length();
        boolean z2 = true;
        if (str.charAt(0) == '*') {
            regexpNode = this.suffixMachine;
            while (length > 1) {
                length--;
                regexpNode = regexpNode.add(str.charAt(length));
            }
        } else {
            if (str.charAt(length - 1) == '*') {
                length--;
                z2 = false;
            }
            RegexpNode regexpNode2 = this.prefixMachine;
            for (int i = 0; i < length; i++) {
                regexpNode2 = regexpNode.add(str.charAt(i));
            }
            regexpNode.exact = z2;
        }
        if (regexpNode.result == null || z) {
            regexpNode.f860re = str;
            regexpNode.result = obj;
            return;
        }
        throw new REException(str + " is a duplicate");
    }

    private Object matchAfter(String str, int i) {
        RegexpNode regexpNode = this.prefixMachine;
        int length = str.length();
        if (length <= 0) {
            return null;
        }
        RegexpNode regexpNode2 = regexpNode;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (regexpNode != null) {
            if (regexpNode.result != null && regexpNode.depth < i && (!regexpNode.exact || i2 == length)) {
                this.lastDepth = regexpNode.depth;
                regexpNode2 = regexpNode;
                i4 = length;
                i3 = i2;
            }
            if (i2 >= length) {
                break;
            }
            regexpNode = regexpNode.find(str.charAt(i2));
            i2++;
        }
        RegexpNode regexpNode3 = this.suffixMachine;
        while (true) {
            length--;
            if (length < 0 || regexpNode3 == null) {
                Object obj = regexpNode2.result;
            } else {
                if (regexpNode3.result != null && regexpNode3.depth < i) {
                    this.lastDepth = regexpNode3.depth;
                    i3 = 0;
                    i4 = length + 1;
                    regexpNode2 = regexpNode3;
                }
                regexpNode3 = regexpNode3.find(str.charAt(length));
            }
        }
        Object obj2 = regexpNode2.result;
        return (obj2 == null || !(obj2 instanceof RegexpTarget)) ? obj2 : ((RegexpTarget) obj2).found(str.substring(i3, i4));
    }

    public void reset() {
        this.lastDepth = Integer.MAX_VALUE;
    }

    public void print(PrintStream printStream) {
        printStream.print("Regexp pool:\n");
        if (this.suffixMachine.firstchild != null) {
            printStream.print(" Suffix machine: ");
            this.suffixMachine.firstchild.print(printStream);
            printStream.print("\n");
        }
        if (this.prefixMachine.firstchild != null) {
            printStream.print(" Prefix machine: ");
            this.prefixMachine.firstchild.print(printStream);
            printStream.print("\n");
        }
    }
}
