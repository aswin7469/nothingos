package java.util.concurrent;

import java.util.Collection;

class Helpers {
    private Helpers() {
    }

    static String collectionToString(Collection<?> collection) {
        String str;
        Object[] array = collection.toArray();
        int length = array.length;
        if (length == 0) {
            return "[]";
        }
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            Object obj = array[i2];
            if (obj == collection) {
                str = "(this Collection)";
            } else {
                str = objectToString(obj);
            }
            array[i2] = str;
            i += str.length();
        }
        return toString(array, length, i);
    }

    static String toString(Object[] objArr, int i, int i2) {
        char[] cArr = new char[(i2 + (i * 2))];
        cArr[0] = '[';
        int i3 = 1;
        for (int i4 = 0; i4 < i; i4++) {
            if (i4 > 0) {
                int i5 = i3 + 1;
                cArr[i3] = ',';
                i3 = i5 + 1;
                cArr[i5] = ' ';
            }
            String str = objArr[i4];
            int length = str.length();
            str.getChars(0, length, cArr, i3);
            i3 += length;
        }
        cArr[i3] = ']';
        return new String(cArr);
    }

    static String mapEntryToString(Object obj, Object obj2) {
        String objectToString = objectToString(obj);
        int length = objectToString.length();
        String objectToString2 = objectToString(obj2);
        int length2 = objectToString2.length();
        char[] cArr = new char[(length + length2 + 1)];
        objectToString.getChars(0, length, cArr, 0);
        cArr[length] = '=';
        objectToString2.getChars(0, length2, cArr, length + 1);
        return new String(cArr);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:1:0x0002, code lost:
        r0 = r0.toString();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String objectToString(java.lang.Object r0) {
        /*
            if (r0 == 0) goto L_0x0008
            java.lang.String r0 = r0.toString()
            if (r0 != 0) goto L_0x000a
        L_0x0008:
            java.lang.String r0 = "null"
        L_0x000a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.Helpers.objectToString(java.lang.Object):java.lang.String");
    }
}
