package jdk.internal.util;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Preconditions {
    private static RuntimeException outOfBounds(BiFunction<String, List<Integer>, ? extends RuntimeException> biFunction, String str, Integer... numArr) {
        RuntimeException runtimeException;
        List of = List.m1739of((E[]) numArr);
        if (biFunction == null) {
            runtimeException = null;
        } else {
            runtimeException = (RuntimeException) biFunction.apply(str, of);
        }
        return runtimeException == null ? new IndexOutOfBoundsException(outOfBoundsMessage(str, of)) : runtimeException;
    }

    private static RuntimeException outOfBoundsCheckIndex(BiFunction<String, List<Integer>, ? extends RuntimeException> biFunction, int i, int i2) {
        return outOfBounds(biFunction, "checkIndex", Integer.valueOf(i), Integer.valueOf(i2));
    }

    private static RuntimeException outOfBoundsCheckFromToIndex(BiFunction<String, List<Integer>, ? extends RuntimeException> biFunction, int i, int i2, int i3) {
        return outOfBounds(biFunction, "checkFromToIndex", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
    }

    private static RuntimeException outOfBoundsCheckFromIndexSize(BiFunction<String, List<Integer>, ? extends RuntimeException> biFunction, int i, int i2, int i3) {
        return outOfBounds(biFunction, "checkFromIndexSize", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
    }

    public static <X extends RuntimeException> BiFunction<String, List<Integer>, X> outOfBoundsExceptionFormatter(final Function<String, X> function) {
        return new BiFunction<String, List<Integer>, X>() {
            public X apply(String str, List<Integer> list) {
                return (RuntimeException) Function.this.apply(Preconditions.outOfBoundsMessage(str, list));
            }
        };
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String outOfBoundsMessage(java.lang.String r10, java.util.List<java.lang.Integer> r11) {
        /*
            r0 = 0
            if (r10 != 0) goto L_0x000e
            if (r11 != 0) goto L_0x000e
            java.lang.String r10 = "Range check failed"
            java.lang.Object[] r11 = new java.lang.Object[r0]
            java.lang.String r10 = java.lang.String.format(r10, r11)
            return r10
        L_0x000e:
            java.lang.String r1 = "Range check failed: %s"
            r2 = 1
            if (r10 != 0) goto L_0x001c
            java.lang.Object[] r10 = new java.lang.Object[r2]
            r10[r0] = r11
            java.lang.String r10 = java.lang.String.format(r1, r10)
            return r10
        L_0x001c:
            if (r11 != 0) goto L_0x0027
            java.lang.Object[] r11 = new java.lang.Object[r2]
            r11[r0] = r10
            java.lang.String r10 = java.lang.String.format(r1, r11)
            return r10
        L_0x0027:
            r10.hashCode()
            int r1 = r10.hashCode()
            java.lang.String r3 = "checkFromIndexSize"
            java.lang.String r4 = "checkFromToIndex"
            java.lang.String r5 = "checkIndex"
            r6 = -1
            r7 = 2
            switch(r1) {
                case -538822486: goto L_0x004d;
                case 1844394469: goto L_0x0044;
                case 1848935233: goto L_0x003b;
                default: goto L_0x0039;
            }
        L_0x0039:
            r1 = r6
            goto L_0x0055
        L_0x003b:
            boolean r1 = r10.equals(r3)
            if (r1 != 0) goto L_0x0042
            goto L_0x0039
        L_0x0042:
            r1 = r7
            goto L_0x0055
        L_0x0044:
            boolean r1 = r10.equals(r4)
            if (r1 != 0) goto L_0x004b
            goto L_0x0039
        L_0x004b:
            r1 = r2
            goto L_0x0055
        L_0x004d:
            boolean r1 = r10.equals(r5)
            if (r1 != 0) goto L_0x0054
            goto L_0x0039
        L_0x0054:
            r1 = r0
        L_0x0055:
            r8 = 3
            switch(r1) {
                case 0: goto L_0x005d;
                case 1: goto L_0x005b;
                case 2: goto L_0x005b;
                default: goto L_0x0059;
            }
        L_0x0059:
            r1 = r0
            goto L_0x005e
        L_0x005b:
            r1 = r8
            goto L_0x005e
        L_0x005d:
            r1 = r7
        L_0x005e:
            int r9 = r11.size()
            if (r9 == r1) goto L_0x0067
            java.lang.String r1 = ""
            goto L_0x0068
        L_0x0067:
            r1 = r10
        L_0x0068:
            r1.hashCode()
            int r9 = r1.hashCode()
            switch(r9) {
                case -538822486: goto L_0x0085;
                case 1844394469: goto L_0x007c;
                case 1848935233: goto L_0x0073;
                default: goto L_0x0072;
            }
        L_0x0072:
            goto L_0x008d
        L_0x0073:
            boolean r1 = r1.equals(r3)
            if (r1 != 0) goto L_0x007a
            goto L_0x008d
        L_0x007a:
            r6 = r7
            goto L_0x008d
        L_0x007c:
            boolean r1 = r1.equals(r4)
            if (r1 != 0) goto L_0x0083
            goto L_0x008d
        L_0x0083:
            r6 = r2
            goto L_0x008d
        L_0x0085:
            boolean r1 = r1.equals(r5)
            if (r1 != 0) goto L_0x008c
            goto L_0x008d
        L_0x008c:
            r6 = r0
        L_0x008d:
            switch(r6) {
                case 0: goto L_0x00d3;
                case 1: goto L_0x00b8;
                case 2: goto L_0x009d;
                default: goto L_0x0090;
            }
        L_0x0090:
            java.lang.Object[] r1 = new java.lang.Object[r7]
            r1[r0] = r10
            r1[r2] = r11
            java.lang.String r10 = "Range check failed: %s %s"
            java.lang.String r10 = java.lang.String.format(r10, r1)
            return r10
        L_0x009d:
            java.lang.Object[] r10 = new java.lang.Object[r8]
            java.lang.Object r1 = r11.get(r0)
            r10[r0] = r1
            java.lang.Object r0 = r11.get(r2)
            r10[r2] = r0
            java.lang.Object r11 = r11.get(r7)
            r10[r7] = r11
            java.lang.String r11 = "Range [%s, %<s + %s) out of bounds for length %s"
            java.lang.String r10 = java.lang.String.format(r11, r10)
            return r10
        L_0x00b8:
            java.lang.Object[] r10 = new java.lang.Object[r8]
            java.lang.Object r1 = r11.get(r0)
            r10[r0] = r1
            java.lang.Object r0 = r11.get(r2)
            r10[r2] = r0
            java.lang.Object r11 = r11.get(r7)
            r10[r7] = r11
            java.lang.String r11 = "Range [%s, %s) out of bounds for length %s"
            java.lang.String r10 = java.lang.String.format(r11, r10)
            return r10
        L_0x00d3:
            java.lang.Object[] r10 = new java.lang.Object[r7]
            java.lang.Object r1 = r11.get(r0)
            r10[r0] = r1
            java.lang.Object r11 = r11.get(r2)
            r10[r2] = r11
            java.lang.String r11 = "Index %s out of bounds for length %s"
            java.lang.String r10 = java.lang.String.format(r11, r10)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: jdk.internal.util.Preconditions.outOfBoundsMessage(java.lang.String, java.util.List):java.lang.String");
    }

    public static <X extends RuntimeException> int checkIndex(int i, int i2, BiFunction<String, List<Integer>, X> biFunction) {
        if (i >= 0 && i < i2) {
            return i;
        }
        throw outOfBoundsCheckIndex(biFunction, i, i2);
    }

    public static <X extends RuntimeException> int checkFromToIndex(int i, int i2, int i3, BiFunction<String, List<Integer>, X> biFunction) {
        if (i >= 0 && i <= i2 && i2 <= i3) {
            return i;
        }
        throw outOfBoundsCheckFromToIndex(biFunction, i, i2, i3);
    }

    public static <X extends RuntimeException> int checkFromIndexSize(int i, int i2, int i3, BiFunction<String, List<Integer>, X> biFunction) {
        if ((i3 | i | i2) >= 0 && i2 <= i3 - i) {
            return i;
        }
        throw outOfBoundsCheckFromIndexSize(biFunction, i, i2, i3);
    }
}
