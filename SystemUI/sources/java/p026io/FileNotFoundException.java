package java.p026io;

/* renamed from: java.io.FileNotFoundException */
public class FileNotFoundException extends IOException {
    private static final long serialVersionUID = -897856973823710492L;

    public FileNotFoundException() {
    }

    public FileNotFoundException(String str) {
        super(str);
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private FileNotFoundException(java.lang.String r3, java.lang.String r4) {
        /*
            r2 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append((java.lang.String) r3)
            if (r4 != 0) goto L_0x000d
            java.lang.String r3 = ""
            goto L_0x0020
        L_0x000d:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r1 = " ("
            r3.<init>((java.lang.String) r1)
            r3.append((java.lang.String) r4)
            java.lang.String r4 = ")"
            r3.append((java.lang.String) r4)
            java.lang.String r3 = r3.toString()
        L_0x0020:
            r0.append((java.lang.String) r3)
            java.lang.String r3 = r0.toString()
            r2.<init>((java.lang.String) r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.FileNotFoundException.<init>(java.lang.String, java.lang.String):void");
    }
}
