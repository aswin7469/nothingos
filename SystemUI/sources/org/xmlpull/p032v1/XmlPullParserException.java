package org.xmlpull.p032v1;

import java.p026io.PrintStream;

/* renamed from: org.xmlpull.v1.XmlPullParserException */
public class XmlPullParserException extends Exception {
    protected int column;
    protected Throwable detail;
    protected int row;

    public XmlPullParserException(String str) {
        super(str);
        this.row = -1;
        this.column = -1;
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public XmlPullParserException(java.lang.String r4, org.xmlpull.p032v1.XmlPullParser r5, java.lang.Throwable r6) {
        /*
            r3 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = ""
            if (r4 != 0) goto L_0x000b
            r4 = r1
            goto L_0x001c
        L_0x000b:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append((java.lang.String) r4)
            java.lang.String r4 = " "
            r2.append((java.lang.String) r4)
            java.lang.String r4 = r2.toString()
        L_0x001c:
            r0.append((java.lang.String) r4)
            if (r5 != 0) goto L_0x0023
            r4 = r1
            goto L_0x003a
        L_0x0023:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r2 = "(position:"
            r4.<init>((java.lang.String) r2)
            java.lang.String r2 = r5.getPositionDescription()
            r4.append((java.lang.String) r2)
            java.lang.String r2 = ") "
            r4.append((java.lang.String) r2)
            java.lang.String r4 = r4.toString()
        L_0x003a:
            r0.append((java.lang.String) r4)
            if (r6 != 0) goto L_0x0040
            goto L_0x004e
        L_0x0040:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r1 = "caused by: "
            r4.<init>((java.lang.String) r1)
            r4.append((java.lang.Object) r6)
            java.lang.String r1 = r4.toString()
        L_0x004e:
            r0.append((java.lang.String) r1)
            java.lang.String r4 = r0.toString()
            r3.<init>((java.lang.String) r4)
            r4 = -1
            r3.row = r4
            r3.column = r4
            if (r5 == 0) goto L_0x006b
            int r4 = r5.getLineNumber()
            r3.row = r4
            int r4 = r5.getColumnNumber()
            r3.column = r4
        L_0x006b:
            r3.detail = r6
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xmlpull.p032v1.XmlPullParserException.<init>(java.lang.String, org.xmlpull.v1.XmlPullParser, java.lang.Throwable):void");
    }

    public Throwable getDetail() {
        return this.detail;
    }

    public int getLineNumber() {
        return this.row;
    }

    public int getColumnNumber() {
        return this.column;
    }

    public void printStackTrace() {
        if (this.detail == null) {
            super.printStackTrace();
            return;
        }
        synchronized (System.err) {
            PrintStream printStream = System.err;
            printStream.println(super.getMessage() + "; nested exception is:");
            this.detail.printStackTrace();
        }
    }
}
